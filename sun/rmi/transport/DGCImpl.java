/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.dgc.DGC;
/*     */ import java.rmi.dgc.Lease;
/*     */ import java.rmi.dgc.VMID;
/*     */ import java.rmi.server.LogStream;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.RemoteServer;
/*     */ import java.rmi.server.ServerNotActiveException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.runtime.RuntimeUtil;
/*     */ import sun.rmi.runtime.RuntimeUtil.GetInstanceAction;
/*     */ import sun.rmi.server.UnicastRef;
/*     */ import sun.rmi.server.UnicastServerRef;
/*     */ import sun.rmi.server.Util;
/*     */ import sun.security.action.GetLongAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ final class DGCImpl
/*     */   implements DGC
/*     */ {
/*  65 */   static final Log dgcLog = Log.getLog("sun.rmi.dgc", "dgc", LogStream.parseLevel((String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.dgc.logLevel"))));
/*     */ 
/*  70 */   private static final long leaseValue = ((Long)AccessController.doPrivileged(new GetLongAction("java.rmi.dgc.leaseValue", 600000L))).longValue();
/*     */ 
/*  75 */   private static final long leaseCheckInterval = ((Long)AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.checkInterval", leaseValue / 2L))).longValue();
/*     */ 
/*  80 */   private static final ScheduledExecutorService scheduler = ((RuntimeUtil)AccessController.doPrivileged(new RuntimeUtil.GetInstanceAction())).getScheduler();
/*     */   private static DGCImpl dgc;
/*  87 */   private Map<VMID, LeaseInfo> leaseTable = new HashMap();
/*     */ 
/*  89 */   private Future<?> checker = null;
/*     */ 
/*     */   static DGCImpl getDGCImpl()
/*     */   {
/*  96 */     return dgc;
/*     */   }
/*     */ 
/*     */   public Lease dirty(ObjID[] paramArrayOfObjID, long paramLong, Lease paramLease)
/*     */   {
/* 119 */     VMID localVMID = paramLease.getVMID();
/*     */ 
/* 124 */     long l = leaseValue;
/*     */ 
/* 126 */     if (dgcLog.isLoggable(Log.VERBOSE)) {
/* 127 */       dgcLog.log(Log.VERBOSE, "vmid = " + localVMID);
/*     */     }
/*     */ 
/* 131 */     if (localVMID == null) {
/* 132 */       localVMID = new VMID();
/*     */ 
/* 134 */       if (dgcLog.isLoggable(Log.BRIEF)) {
/*     */         String str;
/*     */         try {
/* 137 */           str = RemoteServer.getClientHost();
/*     */         } catch (ServerNotActiveException localServerNotActiveException) {
/* 139 */           str = "<unknown host>";
/*     */         }
/* 141 */         dgcLog.log(Log.BRIEF, " assigning vmid " + localVMID + " to client " + str);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 146 */     paramLease = new Lease(localVMID, l);
/*     */ 
/* 148 */     synchronized (this.leaseTable) {
/* 149 */       LeaseInfo localLeaseInfo = (LeaseInfo)this.leaseTable.get(localVMID);
/* 150 */       if (localLeaseInfo == null) {
/* 151 */         this.leaseTable.put(localVMID, new LeaseInfo(localVMID, l));
/* 152 */         if (this.checker == null) {
/* 153 */           this.checker = scheduler.scheduleWithFixedDelay(new Runnable()
/*     */           {
/*     */             public void run() {
/* 156 */               DGCImpl.this.checkLeases();
/*     */             }
/*     */           }
/*     */           , leaseCheckInterval, leaseCheckInterval, TimeUnit.MILLISECONDS);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 163 */         localLeaseInfo.renew(l);
/*     */       }
/*     */     }
/*     */ 
/* 167 */     for (Object localObject2 : paramArrayOfObjID) {
/* 168 */       if (dgcLog.isLoggable(Log.VERBOSE)) {
/* 169 */         dgcLog.log(Log.VERBOSE, "id = " + localObject2 + ", vmid = " + localVMID + ", duration = " + l);
/*     */       }
/*     */ 
/* 173 */       ObjectTable.referenced(localObject2, paramLong, localVMID);
/*     */     }
/*     */ 
/* 177 */     return paramLease;
/*     */   }
/*     */ 
/*     */   public void clean(ObjID[] paramArrayOfObjID, long paramLong, VMID paramVMID, boolean paramBoolean)
/*     */   {
/* 190 */     for (ObjID localObjID : paramArrayOfObjID) {
/* 191 */       if (dgcLog.isLoggable(Log.VERBOSE)) {
/* 192 */         dgcLog.log(Log.VERBOSE, "id = " + localObjID + ", vmid = " + paramVMID + ", strong = " + paramBoolean);
/*     */       }
/*     */ 
/* 196 */       ObjectTable.unreferenced(localObjID, paramLong, paramVMID, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   void registerTarget(VMID paramVMID, Target paramTarget)
/*     */   {
/* 205 */     synchronized (this.leaseTable) {
/* 206 */       LeaseInfo localLeaseInfo = (LeaseInfo)this.leaseTable.get(paramVMID);
/* 207 */       if (localLeaseInfo == null)
/* 208 */         paramTarget.vmidDead(paramVMID);
/*     */       else
/* 210 */         localLeaseInfo.notifySet.add(paramTarget);
/*     */     }
/*     */   }
/*     */ 
/*     */   void unregisterTarget(VMID paramVMID, Target paramTarget)
/*     */   {
/* 219 */     synchronized (this.leaseTable) {
/* 220 */       LeaseInfo localLeaseInfo = (LeaseInfo)this.leaseTable.get(paramVMID);
/* 221 */       if (localLeaseInfo != null)
/* 222 */         localLeaseInfo.notifySet.remove(paramTarget);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkLeases()
/*     */   {
/* 236 */     long l = System.currentTimeMillis();
/*     */ 
/* 239 */     ArrayList localArrayList = new ArrayList();
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 245 */     synchronized (this.leaseTable) {
/* 246 */       localObject1 = this.leaseTable.values().iterator();
/* 247 */       while (((Iterator)localObject1).hasNext()) {
/* 248 */         localObject2 = (LeaseInfo)((Iterator)localObject1).next();
/* 249 */         if (((LeaseInfo)localObject2).expired(l)) {
/* 250 */           localArrayList.add(localObject2);
/* 251 */           ((Iterator)localObject1).remove();
/*     */         }
/*     */       }
/*     */ 
/* 255 */       if (this.leaseTable.isEmpty()) {
/* 256 */         this.checker.cancel(false);
/* 257 */         this.checker = null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 264 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { localObject1 = (LeaseInfo)((Iterator)???).next();
/* 265 */       for (localObject2 = ((LeaseInfo)localObject1).notifySet.iterator(); ((Iterator)localObject2).hasNext(); ) { Target localTarget = (Target)((Iterator)localObject2).next();
/* 266 */         localTarget.vmidDead(((LeaseInfo)localObject1).vmid);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 276 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/* 278 */         ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */         try
/*     */         {
/* 281 */           Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
/*     */           try
/*     */           {
/* 290 */             DGCImpl.access$102(new DGCImpl(null));
/* 291 */             ObjID localObjID = new ObjID(2);
/* 292 */             LiveRef localLiveRef = new LiveRef(localObjID, 0);
/* 293 */             UnicastServerRef localUnicastServerRef = new UnicastServerRef(localLiveRef);
/* 294 */             Remote localRemote = Util.createProxy(DGCImpl.class, new UnicastRef(localLiveRef), true);
/*     */ 
/* 297 */             localUnicastServerRef.setSkeleton(DGCImpl.dgc);
/* 298 */             Target localTarget = new Target(DGCImpl.dgc, localUnicastServerRef, localRemote, localObjID, true);
/*     */ 
/* 300 */             ObjectTable.putTarget(localTarget);
/*     */           } catch (RemoteException localRemoteException) {
/* 302 */             throw new Error("exception initializing server-side DGC", localRemoteException);
/*     */           }
/*     */         }
/*     */         finally {
/* 306 */           Thread.currentThread().setContextClassLoader(localClassLoader);
/*     */         }
/* 308 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static class LeaseInfo
/*     */   {
/*     */     VMID vmid;
/*     */     long expiration;
/* 316 */     Set<Target> notifySet = new HashSet();
/*     */ 
/*     */     LeaseInfo(VMID paramVMID, long paramLong) {
/* 319 */       this.vmid = paramVMID;
/* 320 */       this.expiration = (System.currentTimeMillis() + paramLong);
/*     */     }
/*     */ 
/*     */     synchronized void renew(long paramLong) {
/* 324 */       long l = System.currentTimeMillis() + paramLong;
/* 325 */       if (l > this.expiration)
/* 326 */         this.expiration = l;
/*     */     }
/*     */ 
/*     */     boolean expired(long paramLong) {
/* 330 */       if (this.expiration < paramLong) {
/* 331 */         if (DGCImpl.dgcLog.isLoggable(Log.BRIEF)) {
/* 332 */           DGCImpl.dgcLog.log(Log.BRIEF, this.vmid.toString());
/*     */         }
/* 334 */         return true;
/*     */       }
/* 336 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.DGCImpl
 * JD-Core Version:    0.6.2
 */