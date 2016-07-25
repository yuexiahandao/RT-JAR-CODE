/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.dgc.VMID;
/*     */ import java.rmi.server.ExportException;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import sun.misc.GC;
/*     */ import sun.misc.GC.LatencyRequest;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.runtime.NewThreadAction;
/*     */ import sun.security.action.GetLongAction;
/*     */ 
/*     */ public final class ObjectTable
/*     */ {
/*  53 */   private static final long gcInterval = ((Long)AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.server.gcInterval", 3600000L))).longValue();
/*     */ 
/*  61 */   private static final Object tableLock = new Object();
/*     */ 
/*  64 */   private static final Map<ObjectEndpoint, Target> objTable = new HashMap();
/*     */ 
/*  66 */   private static final Map<WeakRef, Target> implTable = new HashMap();
/*     */ 
/*  73 */   private static final Object keepAliveLock = new Object();
/*     */ 
/*  76 */   private static int keepAliveCount = 0;
/*     */ 
/*  79 */   private static Thread reaper = null;
/*     */ 
/*  82 */   static final ReferenceQueue<Object> reapQueue = new ReferenceQueue();
/*     */ 
/*  85 */   private static GC.LatencyRequest gcLatencyRequest = null;
/*     */ 
/*     */   static Target getTarget(ObjectEndpoint paramObjectEndpoint)
/*     */   {
/*  96 */     synchronized (tableLock) {
/*  97 */       return (Target)objTable.get(paramObjectEndpoint);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Target getTarget(Remote paramRemote)
/*     */   {
/* 105 */     synchronized (tableLock) {
/* 106 */       return (Target)implTable.get(new WeakRef(paramRemote));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Remote getStub(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 122 */     Target localTarget = getTarget(paramRemote);
/* 123 */     if (localTarget == null) {
/* 124 */       throw new NoSuchObjectException("object not exported");
/*     */     }
/* 126 */     return localTarget.getStub();
/*     */   }
/*     */ 
/*     */   public static boolean unexportObject(Remote paramRemote, boolean paramBoolean)
/*     */     throws NoSuchObjectException
/*     */   {
/* 150 */     synchronized (tableLock) {
/* 151 */       Target localTarget = getTarget(paramRemote);
/* 152 */       if (localTarget == null) {
/* 153 */         throw new NoSuchObjectException("object not exported");
/*     */       }
/* 155 */       if (localTarget.unexport(paramBoolean)) {
/* 156 */         removeTarget(localTarget);
/* 157 */         return true;
/*     */       }
/* 159 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void putTarget(Target paramTarget)
/*     */     throws ExportException
/*     */   {
/* 171 */     ObjectEndpoint localObjectEndpoint = paramTarget.getObjectEndpoint();
/* 172 */     WeakRef localWeakRef = paramTarget.getWeakImpl();
/*     */ 
/* 174 */     if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
/* 175 */       DGCImpl.dgcLog.log(Log.VERBOSE, "add object " + localObjectEndpoint);
/*     */     }
/*     */ 
/* 178 */     synchronized (tableLock)
/*     */     {
/* 184 */       if (paramTarget.getImpl() != null) {
/* 185 */         if (objTable.containsKey(localObjectEndpoint)) {
/* 186 */           throw new ExportException("internal error: ObjID already in use");
/*     */         }
/* 188 */         if (implTable.containsKey(localWeakRef)) {
/* 189 */           throw new ExportException("object already exported");
/*     */         }
/*     */ 
/* 192 */         objTable.put(localObjectEndpoint, paramTarget);
/* 193 */         implTable.put(localWeakRef, paramTarget);
/*     */ 
/* 195 */         if (!paramTarget.isPermanent())
/* 196 */           incrementKeepAliveCount();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void removeTarget(Target paramTarget)
/*     */   {
/* 211 */     ObjectEndpoint localObjectEndpoint = paramTarget.getObjectEndpoint();
/* 212 */     WeakRef localWeakRef = paramTarget.getWeakImpl();
/*     */ 
/* 214 */     if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
/* 215 */       DGCImpl.dgcLog.log(Log.VERBOSE, "remove object " + localObjectEndpoint);
/*     */     }
/*     */ 
/* 218 */     objTable.remove(localObjectEndpoint);
/* 219 */     implTable.remove(localWeakRef);
/*     */ 
/* 221 */     paramTarget.markRemoved();
/*     */   }
/*     */ 
/*     */   static void referenced(ObjID paramObjID, long paramLong, VMID paramVMID)
/*     */   {
/* 230 */     synchronized (tableLock) {
/* 231 */       ObjectEndpoint localObjectEndpoint = new ObjectEndpoint(paramObjID, Transport.currentTransport());
/*     */ 
/* 233 */       Target localTarget = (Target)objTable.get(localObjectEndpoint);
/* 234 */       if (localTarget != null)
/* 235 */         localTarget.referenced(paramLong, paramVMID);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void unreferenced(ObjID paramObjID, long paramLong, VMID paramVMID, boolean paramBoolean)
/*     */   {
/* 248 */     synchronized (tableLock) {
/* 249 */       ObjectEndpoint localObjectEndpoint = new ObjectEndpoint(paramObjID, Transport.currentTransport());
/*     */ 
/* 251 */       Target localTarget = (Target)objTable.get(localObjectEndpoint);
/* 252 */       if (localTarget != null)
/* 253 */         localTarget.unreferenced(paramLong, paramVMID, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void incrementKeepAliveCount()
/*     */   {
/* 275 */     synchronized (keepAliveLock) {
/* 276 */       keepAliveCount += 1;
/*     */ 
/* 278 */       if (reaper == null) {
/* 279 */         reaper = (Thread)AccessController.doPrivileged(new NewThreadAction(new Reaper(null), "Reaper", false));
/*     */ 
/* 281 */         reaper.start();
/*     */       }
/*     */ 
/* 291 */       if (gcLatencyRequest == null)
/* 292 */         gcLatencyRequest = GC.requestLatency(gcInterval);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void decrementKeepAliveCount()
/*     */   {
/* 311 */     synchronized (keepAliveLock) {
/* 312 */       keepAliveCount -= 1;
/*     */ 
/* 314 */       if (keepAliveCount == 0) {
/* 315 */         if (reaper == null) throw new AssertionError();
/* 316 */         AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Void run() {
/* 318 */             ObjectTable.reaper.interrupt();
/* 319 */             return null;
/*     */           }
/*     */         });
/* 322 */         reaper = null;
/*     */ 
/* 329 */         gcLatencyRequest.cancel();
/* 330 */         gcLatencyRequest = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Reaper
/*     */     implements Runnable
/*     */   {
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*     */         do
/*     */         {
/* 351 */           WeakRef localWeakRef = (WeakRef)ObjectTable.reapQueue.remove();
/*     */ 
/* 353 */           synchronized (ObjectTable.tableLock) {
/* 354 */             Target localTarget = (Target)ObjectTable.implTable.get(localWeakRef);
/* 355 */             if (localTarget != null) {
/* 356 */               if (!localTarget.isEmpty()) {
/* 357 */                 throw new Error("object with known references collected");
/*     */               }
/* 359 */               if (localTarget.isPermanent()) {
/* 360 */                 throw new Error("permanent object collected");
/*     */               }
/* 362 */               ObjectTable.removeTarget(localTarget);
/*     */             }
/*     */           }
/*     */         }
/* 365 */         while (!Thread.interrupted());
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.ObjectTable
 * JD-Core Version:    0.6.2
 */