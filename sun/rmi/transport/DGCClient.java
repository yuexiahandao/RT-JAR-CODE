/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.dgc.DGC;
/*     */ import java.rmi.dgc.Lease;
/*     */ import java.rmi.dgc.VMID;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.misc.GC;
/*     */ import sun.misc.GC.LatencyRequest;
/*     */ import sun.rmi.runtime.NewThreadAction;
/*     */ import sun.rmi.server.UnicastRef;
/*     */ import sun.rmi.server.Util;
/*     */ import sun.security.action.GetLongAction;
/*     */ 
/*     */ final class DGCClient
/*     */ {
/*  81 */   private static long nextSequenceNum = -9223372036854775808L;
/*     */ 
/*  84 */   private static VMID vmid = new VMID();
/*     */ 
/*  87 */   private static final long leaseValue = ((Long)AccessController.doPrivileged(new GetLongAction("java.rmi.dgc.leaseValue", 600000L))).longValue();
/*     */ 
/*  93 */   private static final long cleanInterval = ((Long)AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.cleanInterval", 180000L))).longValue();
/*     */ 
/*  99 */   private static final long gcInterval = ((Long)AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.client.gcInterval", 3600000L))).longValue();
/*     */   private static final int dirtyFailureRetries = 5;
/*     */   private static final int cleanFailureRetries = 5;
/* 111 */   private static final ObjID[] emptyObjIDArray = new ObjID[0];
/*     */ 
/* 114 */   private static final ObjID dgcID = new ObjID(2);
/*     */ 
/*     */   static void registerRefs(Endpoint paramEndpoint, List<LiveRef> paramList)
/*     */   {
/*     */     EndpointEntry localEndpointEntry;
/*     */     do
/* 138 */       localEndpointEntry = EndpointEntry.lookup(paramEndpoint);
/* 139 */     while (!localEndpointEntry.registerRefs(paramList));
/*     */   }
/*     */ 
/*     */   private static synchronized long getNextSequenceNum()
/*     */   {
/* 149 */     return nextSequenceNum++;
/*     */   }
/*     */ 
/*     */   private static long computeRenewTime(long paramLong1, long paramLong2)
/*     */   {
/* 162 */     return paramLong1 + paramLong2 / 2L;
/*     */   }
/*     */ 
/*     */   private static class EndpointEntry
/*     */   {
/*     */     private Endpoint endpoint;
/*     */     private DGC dgc;
/* 179 */     private Map<LiveRef, RefEntry> refTable = new HashMap(5);
/*     */ 
/* 181 */     private Set<RefEntry> invalidRefs = new HashSet(5);
/*     */ 
/* 184 */     private boolean removed = false;
/*     */ 
/* 187 */     private long renewTime = 9223372036854775807L;
/*     */ 
/* 189 */     private long expirationTime = -9223372036854775808L;
/*     */ 
/* 191 */     private int dirtyFailures = 0;
/*     */     private long dirtyFailureStartTime;
/*     */     private long dirtyFailureDuration;
/*     */     private Thread renewCleanThread;
/* 200 */     private boolean interruptible = false;
/*     */ 
/* 203 */     private ReferenceQueue<LiveRef> refQueue = new ReferenceQueue();
/*     */ 
/* 205 */     private Set<CleanRequest> pendingCleans = new HashSet(5);
/*     */ 
/* 208 */     private static Map<Endpoint, EndpointEntry> endpointTable = new HashMap(5);
/*     */ 
/* 210 */     private static GC.LatencyRequest gcLatencyRequest = null;
/*     */ 
/*     */     public static EndpointEntry lookup(Endpoint paramEndpoint)
/*     */     {
/* 217 */       synchronized (endpointTable) {
/* 218 */         EndpointEntry localEndpointEntry = (EndpointEntry)endpointTable.get(paramEndpoint);
/* 219 */         if (localEndpointEntry == null) {
/* 220 */           localEndpointEntry = new EndpointEntry(paramEndpoint);
/* 221 */           endpointTable.put(paramEndpoint, localEndpointEntry);
/*     */ 
/* 229 */           if (gcLatencyRequest == null) {
/* 230 */             gcLatencyRequest = GC.requestLatency(DGCClient.gcInterval);
/*     */           }
/*     */         }
/* 233 */         return localEndpointEntry;
/*     */       }
/*     */     }
/*     */ 
/*     */     private EndpointEntry(Endpoint paramEndpoint) {
/* 238 */       this.endpoint = paramEndpoint;
/*     */       try {
/* 240 */         LiveRef localLiveRef = new LiveRef(DGCClient.dgcID, paramEndpoint, false);
/* 241 */         this.dgc = ((DGC)Util.createProxy(DGCImpl.class, new UnicastRef(localLiveRef), true));
/*     */       }
/*     */       catch (RemoteException localRemoteException) {
/* 244 */         throw new Error("internal error creating DGC stub");
/*     */       }
/* 246 */       this.renewCleanThread = ((Thread)AccessController.doPrivileged(new NewThreadAction(new RenewCleanThread(null), "RenewClean-" + paramEndpoint, true)));
/*     */ 
/* 249 */       this.renewCleanThread.start();
/*     */     }
/*     */ 
/*     */     public boolean registerRefs(List<LiveRef> paramList)
/*     */     {
/* 264 */       assert (!Thread.holdsLock(this));
/*     */ 
/* 266 */       HashSet localHashSet = null;
/*     */       long l;
/* 269 */       synchronized (this) {
/* 270 */         if (this.removed) {
/* 271 */           return false;
/*     */         }
/*     */ 
/* 274 */         Iterator localIterator = paramList.iterator();
/* 275 */         while (localIterator.hasNext()) {
/* 276 */           LiveRef localLiveRef1 = (LiveRef)localIterator.next();
/* 277 */           assert (localLiveRef1.getEndpoint().equals(this.endpoint));
/*     */ 
/* 279 */           RefEntry localRefEntry = (RefEntry)this.refTable.get(localLiveRef1);
/* 280 */           if (localRefEntry == null) {
/* 281 */             LiveRef localLiveRef2 = (LiveRef)localLiveRef1.clone();
/* 282 */             localRefEntry = new RefEntry(localLiveRef2);
/* 283 */             this.refTable.put(localLiveRef2, localRefEntry);
/* 284 */             if (localHashSet == null) {
/* 285 */               localHashSet = new HashSet(5);
/*     */             }
/* 287 */             localHashSet.add(localRefEntry);
/*     */           }
/*     */ 
/* 290 */           localRefEntry.addInstanceToRefSet(localLiveRef1);
/*     */         }
/*     */ 
/* 293 */         if (localHashSet == null) {
/* 294 */           return true;
/*     */         }
/*     */ 
/* 297 */         localHashSet.addAll(this.invalidRefs);
/* 298 */         this.invalidRefs.clear();
/*     */ 
/* 300 */         l = DGCClient.access$300();
/*     */       }
/*     */ 
/* 303 */       makeDirtyCall(localHashSet, l);
/* 304 */       return true;
/*     */     }
/*     */ 
/*     */     private void removeRefEntry(RefEntry paramRefEntry)
/*     */     {
/* 315 */       assert (Thread.holdsLock(this));
/* 316 */       assert (!this.removed);
/* 317 */       assert (this.refTable.containsKey(paramRefEntry.getRef()));
/*     */ 
/* 319 */       this.refTable.remove(paramRefEntry.getRef());
/* 320 */       this.invalidRefs.remove(paramRefEntry);
/* 321 */       if (this.refTable.isEmpty())
/* 322 */         synchronized (endpointTable) {
/* 323 */           endpointTable.remove(this.endpoint);
/* 324 */           Transport localTransport = this.endpoint.getOutboundTransport();
/* 325 */           localTransport.free(this.endpoint);
/*     */ 
/* 331 */           if (endpointTable.isEmpty()) {
/* 332 */             assert (gcLatencyRequest != null);
/* 333 */             gcLatencyRequest.cancel();
/* 334 */             gcLatencyRequest = null;
/*     */           }
/* 336 */           this.removed = true;
/*     */         }
/*     */     }
/*     */ 
/*     */     private void makeDirtyCall(Set<RefEntry> paramSet, long paramLong)
/*     */     {
/* 349 */       assert (!Thread.holdsLock(this));
/*     */       ObjID[] arrayOfObjID;
/* 352 */       if (paramSet != null)
/* 353 */         arrayOfObjID = createObjIDArray(paramSet);
/*     */       else {
/* 355 */         arrayOfObjID = DGCClient.emptyObjIDArray;
/*     */       }
/*     */ 
/* 358 */       long l1 = System.currentTimeMillis();
/*     */       try {
/* 360 */         Lease localLease = this.dgc.dirty(arrayOfObjID, paramLong, new Lease(DGCClient.vmid, DGCClient.leaseValue));
/*     */ 
/* 362 */         l2 = localLease.getValue();
/*     */ 
/* 364 */         long l3 = DGCClient.computeRenewTime(l1, l2);
/* 365 */         l4 = l1 + l2;
/*     */ 
/* 367 */         synchronized (this) {
/* 368 */           this.dirtyFailures = 0;
/* 369 */           setRenewTime(l3);
/* 370 */           this.expirationTime = l4;
/*     */         }
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */         long l4;
/* 374 */         long l2 = System.currentTimeMillis();
/*     */ 
/* 376 */         synchronized (this) {
/* 377 */           this.dirtyFailures += 1;
/*     */ 
/* 379 */           if (this.dirtyFailures == 1)
/*     */           {
/* 387 */             this.dirtyFailureStartTime = l1;
/* 388 */             this.dirtyFailureDuration = (l2 - l1);
/* 389 */             setRenewTime(l2);
/*     */           }
/*     */           else
/*     */           {
/* 396 */             int i = this.dirtyFailures - 2;
/* 397 */             if (i == 0)
/*     */             {
/* 404 */               this.dirtyFailureDuration = Math.max(this.dirtyFailureDuration + (l2 - l1) >> 1, 1000L);
/*     */             }
/*     */ 
/* 408 */             l4 = l2 + (this.dirtyFailureDuration << i);
/*     */ 
/* 417 */             if ((l4 < this.expirationTime) || (this.dirtyFailures < 5) || (l4 < this.dirtyFailureStartTime + DGCClient.leaseValue))
/*     */             {
/* 421 */               setRenewTime(l4);
/*     */             }
/*     */             else
/*     */             {
/* 427 */               setRenewTime(9223372036854775807L);
/*     */             }
/*     */           }
/*     */ 
/* 431 */           if (paramSet != null)
/*     */           {
/* 439 */             this.invalidRefs.addAll(paramSet);
/*     */ 
/* 446 */             Iterator localIterator = paramSet.iterator();
/* 447 */             while (localIterator.hasNext()) {
/* 448 */               RefEntry localRefEntry = (RefEntry)localIterator.next();
/* 449 */               localRefEntry.markDirtyFailed();
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 457 */           if (this.renewTime >= this.expirationTime)
/* 458 */             this.invalidRefs.addAll(this.refTable.values());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private void setRenewTime(long paramLong)
/*     */     {
/* 471 */       assert (Thread.holdsLock(this));
/*     */ 
/* 473 */       if (paramLong < this.renewTime) {
/* 474 */         this.renewTime = paramLong;
/* 475 */         if (this.interruptible)
/* 476 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Void run() {
/* 479 */               DGCClient.EndpointEntry.this.renewCleanThread.interrupt();
/* 480 */               return null;
/*     */             }
/*     */           });
/*     */       }
/*     */       else {
/* 485 */         this.renewTime = paramLong;
/*     */       }
/*     */     }
/*     */ 
/*     */     private void processPhantomRefs(DGCClient.EndpointEntry.RefEntry.PhantomLiveRef paramPhantomLiveRef)
/*     */     {
/* 595 */       assert (Thread.holdsLock(this));
/*     */ 
/* 597 */       HashSet localHashSet1 = null;
/* 598 */       HashSet localHashSet2 = null;
/*     */       do
/*     */       {
/* 601 */         RefEntry localRefEntry = paramPhantomLiveRef.getRefEntry();
/* 602 */         localRefEntry.removeInstanceFromRefSet(paramPhantomLiveRef);
/* 603 */         if (localRefEntry.isRefSetEmpty()) {
/* 604 */           if (localRefEntry.hasDirtyFailed()) {
/* 605 */             if (localHashSet1 == null) {
/* 606 */               localHashSet1 = new HashSet(5);
/*     */             }
/* 608 */             localHashSet1.add(localRefEntry);
/*     */           } else {
/* 610 */             if (localHashSet2 == null) {
/* 611 */               localHashSet2 = new HashSet(5);
/*     */             }
/* 613 */             localHashSet2.add(localRefEntry);
/*     */           }
/* 615 */           removeRefEntry(localRefEntry);
/*     */         }
/*     */       }
/* 618 */       while ((paramPhantomLiveRef = (DGCClient.EndpointEntry.RefEntry.PhantomLiveRef)this.refQueue.poll()) != null);
/*     */ 
/* 620 */       if (localHashSet1 != null) {
/* 621 */         this.pendingCleans.add(new CleanRequest(createObjIDArray(localHashSet1), DGCClient.access$300(), true));
/*     */       }
/*     */ 
/* 625 */       if (localHashSet2 != null)
/* 626 */         this.pendingCleans.add(new CleanRequest(createObjIDArray(localHashSet2), DGCClient.access$300(), false));
/*     */     }
/*     */ 
/*     */     private void makeCleanCalls()
/*     */     {
/* 660 */       assert (!Thread.holdsLock(this));
/*     */ 
/* 662 */       Iterator localIterator = this.pendingCleans.iterator();
/* 663 */       while (localIterator.hasNext()) {
/* 664 */         CleanRequest localCleanRequest = (CleanRequest)localIterator.next();
/*     */         try {
/* 666 */           this.dgc.clean(localCleanRequest.objIDs, localCleanRequest.sequenceNum, DGCClient.vmid, localCleanRequest.strong);
/*     */ 
/* 668 */           localIterator.remove();
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/* 675 */           if (++localCleanRequest.failures >= 5)
/* 676 */             localIterator.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private static ObjID[] createObjIDArray(Set<RefEntry> paramSet)
/*     */     {
/* 687 */       ObjID[] arrayOfObjID = new ObjID[paramSet.size()];
/* 688 */       Iterator localIterator = paramSet.iterator();
/* 689 */       for (int i = 0; i < arrayOfObjID.length; i++) {
/* 690 */         arrayOfObjID[i] = ((RefEntry)localIterator.next()).getRef().getObjID();
/*     */       }
/* 692 */       return arrayOfObjID;
/*     */     }
/*     */ 
/*     */     private static class CleanRequest
/*     */     {
/*     */       final ObjID[] objIDs;
/*     */       final long sequenceNum;
/*     */       final boolean strong;
/* 643 */       int failures = 0;
/*     */ 
/*     */       CleanRequest(ObjID[] paramArrayOfObjID, long paramLong, boolean paramBoolean) {
/* 646 */         this.objIDs = paramArrayOfObjID;
/* 647 */         this.sequenceNum = paramLong;
/* 648 */         this.strong = paramBoolean;
/*     */       }
/*     */     }
/*     */ 
/*     */     private class RefEntry
/*     */     {
/*     */       private LiveRef ref;
/* 707 */       private Set<PhantomLiveRef> refSet = new HashSet(5);
/*     */ 
/* 709 */       private boolean dirtyFailed = false;
/*     */ 
/*     */       public RefEntry(LiveRef arg2)
/*     */       {
/*     */         Object localObject;
/* 712 */         this.ref = localObject;
/*     */       }
/*     */ 
/*     */       public LiveRef getRef()
/*     */       {
/* 720 */         return this.ref;
/*     */       }
/*     */ 
/*     */       public void addInstanceToRefSet(LiveRef paramLiveRef)
/*     */       {
/* 730 */         assert (Thread.holdsLock(DGCClient.EndpointEntry.this));
/* 731 */         assert (paramLiveRef.equals(this.ref));
/*     */ 
/* 738 */         this.refSet.add(new PhantomLiveRef(paramLiveRef));
/*     */       }
/*     */ 
/*     */       public void removeInstanceFromRefSet(PhantomLiveRef paramPhantomLiveRef)
/*     */       {
/* 748 */         assert (Thread.holdsLock(DGCClient.EndpointEntry.this));
/* 749 */         assert (this.refSet.contains(paramPhantomLiveRef));
/* 750 */         this.refSet.remove(paramPhantomLiveRef);
/*     */       }
/*     */ 
/*     */       public boolean isRefSetEmpty()
/*     */       {
/* 761 */         assert (Thread.holdsLock(DGCClient.EndpointEntry.this));
/* 762 */         return this.refSet.size() == 0;
/*     */       }
/*     */ 
/*     */       public void markDirtyFailed()
/*     */       {
/* 773 */         assert (Thread.holdsLock(DGCClient.EndpointEntry.this));
/* 774 */         this.dirtyFailed = true;
/*     */       }
/*     */ 
/*     */       public boolean hasDirtyFailed()
/*     */       {
/* 786 */         assert (Thread.holdsLock(DGCClient.EndpointEntry.this));
/* 787 */         return this.dirtyFailed;
/*     */       }
/*     */ 
/*     */       private class PhantomLiveRef extends PhantomReference<LiveRef>
/*     */       {
/*     */         public PhantomLiveRef(LiveRef arg2)
/*     */         {
/* 798 */           super(DGCClient.EndpointEntry.this.refQueue);
/*     */         }
/*     */ 
/*     */         public DGCClient.EndpointEntry.RefEntry getRefEntry() {
/* 802 */           return DGCClient.EndpointEntry.RefEntry.this;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private class RenewCleanThread
/*     */       implements Runnable
/*     */     {
/*     */       private RenewCleanThread()
/*     */       {
/*     */       }
/*     */ 
/*     */       public void run()
/*     */       {
/*     */         do
/*     */         {
/* 498 */           DGCClient.EndpointEntry.RefEntry.PhantomLiveRef localPhantomLiveRef = null;
/* 499 */           int i = 0;
/* 500 */           Set localSet = null;
/* 501 */           long l2 = -9223372036854775808L;
/*     */           long l3;
/*     */           long l1;
/* 503 */           synchronized (DGCClient.EndpointEntry.this)
/*     */           {
/* 514 */             l3 = DGCClient.EndpointEntry.this.renewTime - System.currentTimeMillis();
/*     */ 
/* 516 */             l1 = Math.max(l3, 1L);
/* 517 */             if (!DGCClient.EndpointEntry.this.pendingCleans.isEmpty()) {
/* 518 */               l1 = Math.min(l1, DGCClient.cleanInterval);
/*     */             }
/*     */ 
/* 527 */             DGCClient.EndpointEntry.this.interruptible = true;
/*     */           }
/*     */ 
/*     */           try
/*     */           {
/* 535 */             localPhantomLiveRef = (DGCClient.EndpointEntry.RefEntry.PhantomLiveRef)DGCClient.EndpointEntry.this.refQueue.remove(l1);
/*     */           }
/*     */           catch (InterruptedException )
/*     */           {
/*     */           }
/* 540 */           synchronized (DGCClient.EndpointEntry.this)
/*     */           {
/* 547 */             DGCClient.EndpointEntry.this.interruptible = false;
/* 548 */             Thread.interrupted();
/*     */ 
/* 555 */             if (localPhantomLiveRef != null) {
/* 556 */               DGCClient.EndpointEntry.this.processPhantomRefs(localPhantomLiveRef);
/*     */             }
/*     */ 
/* 562 */             l3 = System.currentTimeMillis();
/* 563 */             if (l3 > DGCClient.EndpointEntry.this.renewTime) {
/* 564 */               i = 1;
/* 565 */               if (!DGCClient.EndpointEntry.this.invalidRefs.isEmpty()) {
/* 566 */                 localSet = DGCClient.EndpointEntry.this.invalidRefs;
/* 567 */                 DGCClient.EndpointEntry.this.invalidRefs = new HashSet(5);
/*     */               }
/* 569 */               l2 = DGCClient.access$300();
/*     */             }
/*     */           }
/*     */ 
/* 573 */           if (i != 0) {
/* 574 */             DGCClient.EndpointEntry.this.makeDirtyCall(localSet, l2);
/*     */           }
/*     */ 
/* 577 */           if (!DGCClient.EndpointEntry.this.pendingCleans.isEmpty())
/* 578 */             DGCClient.EndpointEntry.this.makeCleanCalls();
/*     */         }
/* 580 */         while ((!DGCClient.EndpointEntry.this.removed) || (!DGCClient.EndpointEntry.this.pendingCleans.isEmpty()));
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.DGCClient
 * JD-Core Version:    0.6.2
 */