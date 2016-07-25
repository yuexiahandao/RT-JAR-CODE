/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.dgc.VMID;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.Unreferenced;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.runtime.NewThreadAction;
/*     */ import sun.rmi.server.Dispatcher;
/*     */ 
/*     */ public final class Target
/*     */ {
/*     */   private final ObjID id;
/*     */   private final boolean permanent;
/*     */   private final WeakRef weakImpl;
/*     */   private volatile Dispatcher disp;
/*     */   private final Remote stub;
/*  56 */   private final Vector<VMID> refSet = new Vector();
/*     */ 
/*  58 */   private final Hashtable<VMID, SequenceEntry> sequenceTable = new Hashtable(5);
/*     */   private final AccessControlContext acc;
/*     */   private final ClassLoader ccl;
/*  65 */   private int callCount = 0;
/*     */ 
/*  67 */   private boolean removed = false;
/*     */ 
/*  72 */   private volatile Transport exportedTransport = null;
/*     */ 
/*  75 */   private static int nextThreadNum = 0;
/*     */ 
/*     */   public Target(Remote paramRemote1, Dispatcher paramDispatcher, Remote paramRemote2, ObjID paramObjID, boolean paramBoolean)
/*     */   {
/*  90 */     this.weakImpl = new WeakRef(paramRemote1, ObjectTable.reapQueue);
/*  91 */     this.disp = paramDispatcher;
/*  92 */     this.stub = paramRemote2;
/*  93 */     this.id = paramObjID;
/*  94 */     this.acc = AccessController.getContext();
/*     */ 
/* 108 */     ClassLoader localClassLoader1 = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 110 */     ClassLoader localClassLoader2 = paramRemote1.getClass().getClassLoader();
/* 111 */     if (checkLoaderAncestry(localClassLoader1, localClassLoader2))
/* 112 */       this.ccl = localClassLoader1;
/*     */     else {
/* 114 */       this.ccl = localClassLoader2;
/*     */     }
/*     */ 
/* 117 */     this.permanent = paramBoolean;
/* 118 */     if (paramBoolean)
/* 119 */       pinImpl();
/*     */   }
/*     */ 
/*     */   private static boolean checkLoaderAncestry(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
/*     */   {
/* 133 */     if (paramClassLoader2 == null)
/* 134 */       return true;
/* 135 */     if (paramClassLoader1 == null) {
/* 136 */       return false;
/*     */     }
/* 138 */     for (ClassLoader localClassLoader = paramClassLoader1; 
/* 139 */       localClassLoader != null; 
/* 140 */       localClassLoader = localClassLoader.getParent())
/*     */     {
/* 142 */       if (localClassLoader == paramClassLoader2) {
/* 143 */         return true;
/*     */       }
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   public Remote getStub()
/*     */   {
/* 153 */     return this.stub;
/*     */   }
/*     */ 
/*     */   ObjectEndpoint getObjectEndpoint()
/*     */   {
/* 160 */     return new ObjectEndpoint(this.id, this.exportedTransport);
/*     */   }
/*     */ 
/*     */   WeakRef getWeakImpl()
/*     */   {
/* 167 */     return this.weakImpl;
/*     */   }
/*     */ 
/*     */   Dispatcher getDispatcher()
/*     */   {
/* 174 */     return this.disp;
/*     */   }
/*     */ 
/*     */   AccessControlContext getAccessControlContext() {
/* 178 */     return this.acc;
/*     */   }
/*     */ 
/*     */   ClassLoader getContextClassLoader() {
/* 182 */     return this.ccl;
/*     */   }
/*     */ 
/*     */   Remote getImpl()
/*     */   {
/* 191 */     return (Remote)this.weakImpl.get();
/*     */   }
/*     */ 
/*     */   boolean isPermanent()
/*     */   {
/* 198 */     return this.permanent;
/*     */   }
/*     */ 
/*     */   synchronized void pinImpl()
/*     */   {
/* 208 */     this.weakImpl.pin();
/*     */   }
/*     */ 
/*     */   synchronized void unpinImpl()
/*     */   {
/* 223 */     if ((!this.permanent) && (this.refSet.isEmpty()))
/* 224 */       this.weakImpl.unpin();
/*     */   }
/*     */ 
/*     */   void setExportedTransport(Transport paramTransport)
/*     */   {
/* 233 */     if (this.exportedTransport == null)
/* 234 */       this.exportedTransport = paramTransport;
/*     */   }
/*     */ 
/*     */   synchronized void referenced(long paramLong, VMID paramVMID)
/*     */   {
/* 245 */     SequenceEntry localSequenceEntry = (SequenceEntry)this.sequenceTable.get(paramVMID);
/* 246 */     if (localSequenceEntry == null)
/* 247 */       this.sequenceTable.put(paramVMID, new SequenceEntry(paramLong));
/* 248 */     else if (localSequenceEntry.sequenceNum < paramLong) {
/* 249 */       localSequenceEntry.update(paramLong);
/*     */     }
/*     */     else {
/* 252 */       return;
/*     */     }
/*     */ 
/* 255 */     if (!this.refSet.contains(paramVMID))
/*     */     {
/* 263 */       pinImpl();
/* 264 */       if (getImpl() == null) {
/* 265 */         return;
/*     */       }
/* 267 */       if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
/* 268 */         DGCImpl.dgcLog.log(Log.VERBOSE, "add to dirty set: " + paramVMID);
/*     */       }
/*     */ 
/* 271 */       this.refSet.addElement(paramVMID);
/*     */ 
/* 273 */       DGCImpl.getDGCImpl().registerTarget(paramVMID, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void unreferenced(long paramLong, VMID paramVMID, boolean paramBoolean)
/*     */   {
/* 284 */     SequenceEntry localSequenceEntry = (SequenceEntry)this.sequenceTable.get(paramVMID);
/* 285 */     if ((localSequenceEntry == null) || (localSequenceEntry.sequenceNum > paramLong))
/*     */     {
/* 287 */       return;
/* 288 */     }if (paramBoolean)
/*     */     {
/* 290 */       localSequenceEntry.retain(paramLong);
/* 291 */     } else if (!localSequenceEntry.keep)
/*     */     {
/* 293 */       this.sequenceTable.remove(paramVMID);
/*     */     }
/*     */ 
/* 296 */     if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
/* 297 */       DGCImpl.dgcLog.log(Log.VERBOSE, "remove from dirty set: " + paramVMID);
/*     */     }
/*     */ 
/* 300 */     refSetRemove(paramVMID);
/*     */   }
/*     */ 
/*     */   private synchronized void refSetRemove(VMID paramVMID)
/*     */   {
/* 308 */     DGCImpl.getDGCImpl().unregisterTarget(paramVMID, this);
/*     */ 
/* 310 */     if ((this.refSet.removeElement(paramVMID)) && (this.refSet.isEmpty()))
/*     */     {
/* 313 */       if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
/* 314 */         DGCImpl.dgcLog.log(Log.VERBOSE, "reference set is empty: target = " + this);
/*     */       }
/*     */ 
/* 322 */       Remote localRemote = getImpl();
/* 323 */       if ((localRemote instanceof Unreferenced)) {
/* 324 */         final Unreferenced localUnreferenced = (Unreferenced)localRemote;
/* 325 */         final Thread localThread = (Thread)AccessController.doPrivileged(new NewThreadAction(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 329 */             localUnreferenced.unreferenced();
/*     */           }
/*     */         }
/*     */         , "Unreferenced-" + nextThreadNum++, false, true));
/*     */ 
/* 337 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run() {
/* 340 */             localThread.setContextClassLoader(Target.this.ccl);
/* 341 */             return null;
/*     */           }
/*     */         });
/* 345 */         localThread.start();
/*     */       }
/*     */ 
/* 348 */       unpinImpl();
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized boolean unexport(boolean paramBoolean)
/*     */   {
/* 361 */     if ((paramBoolean == true) || (this.callCount == 0) || (this.disp == null)) {
/* 362 */       this.disp = null;
/*     */ 
/* 368 */       unpinImpl();
/* 369 */       DGCImpl localDGCImpl = DGCImpl.getDGCImpl();
/* 370 */       Enumeration localEnumeration = this.refSet.elements();
/* 371 */       while (localEnumeration.hasMoreElements()) {
/* 372 */         VMID localVMID = (VMID)localEnumeration.nextElement();
/* 373 */         localDGCImpl.unregisterTarget(localVMID, this);
/*     */       }
/* 375 */       return true;
/*     */     }
/* 377 */     return false;
/*     */   }
/*     */ 
/*     */   synchronized void markRemoved()
/*     */   {
/* 385 */     if (this.removed) throw new AssertionError();
/*     */ 
/* 387 */     this.removed = true;
/* 388 */     if ((!this.permanent) && (this.callCount == 0)) {
/* 389 */       ObjectTable.decrementKeepAliveCount();
/*     */     }
/*     */ 
/* 392 */     if (this.exportedTransport != null)
/* 393 */       this.exportedTransport.targetUnexported();
/*     */   }
/*     */ 
/*     */   synchronized void incrementCallCount()
/*     */     throws NoSuchObjectException
/*     */   {
/* 402 */     if (this.disp != null)
/* 403 */       this.callCount += 1;
/*     */     else
/* 405 */       throw new NoSuchObjectException("object not accepting new calls");
/*     */   }
/*     */ 
/*     */   synchronized void decrementCallCount()
/*     */   {
/* 414 */     if (--this.callCount < 0) {
/* 415 */       throw new Error("internal error: call count less than zero");
/*     */     }
/*     */ 
/* 426 */     if ((!this.permanent) && (this.removed) && (this.callCount == 0))
/* 427 */       ObjectTable.decrementKeepAliveCount();
/*     */   }
/*     */ 
/*     */   boolean isEmpty()
/*     */   {
/* 436 */     return this.refSet.isEmpty();
/*     */   }
/*     */ 
/*     */   public synchronized void vmidDead(VMID paramVMID)
/*     */   {
/* 445 */     if (DGCImpl.dgcLog.isLoggable(Log.BRIEF)) {
/* 446 */       DGCImpl.dgcLog.log(Log.BRIEF, "removing endpoint " + paramVMID + " from reference set");
/*     */     }
/*     */ 
/* 450 */     this.sequenceTable.remove(paramVMID);
/* 451 */     refSetRemove(paramVMID);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.Target
 * JD-Core Version:    0.6.2
 */