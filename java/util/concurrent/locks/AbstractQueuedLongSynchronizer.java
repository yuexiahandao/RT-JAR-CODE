/*      */ package java.util.concurrent.locks;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public abstract class AbstractQueuedLongSynchronizer extends AbstractOwnableSynchronizer
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 7373984972572414692L;
/*      */   private volatile transient Node head;
/*      */   private volatile transient Node tail;
/*      */   private volatile long state;
/*      */   static final long spinForTimeoutThreshold = 1000L;
/* 2054 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*      */   private static final long stateOffset;
/*      */   private static final long headOffset;
/*      */   private static final long tailOffset;
/*      */   private static final long waitStatusOffset;
/*      */   private static final long nextOffset;
/*      */ 
/*      */   protected final long getState()
/*      */   {
/*  318 */     return this.state;
/*      */   }
/*      */ 
/*      */   protected final void setState(long paramLong)
/*      */   {
/*  327 */     this.state = paramLong;
/*      */   }
/*      */ 
/*      */   protected final boolean compareAndSetState(long paramLong1, long paramLong2)
/*      */   {
/*  343 */     return unsafe.compareAndSwapLong(this, stateOffset, paramLong1, paramLong2);
/*      */   }
/*      */ 
/*      */   private Node enq(Node paramNode)
/*      */   {
/*      */     while (true)
/*      */     {
/*  362 */       Node localNode = this.tail;
/*  363 */       if (localNode == null) {
/*  364 */         if (compareAndSetHead(new Node()))
/*  365 */           this.tail = this.head;
/*      */       } else {
/*  367 */         paramNode.prev = localNode;
/*  368 */         if (compareAndSetTail(localNode, paramNode)) {
/*  369 */           localNode.next = paramNode;
/*  370 */           return localNode;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node addWaiter(Node paramNode)
/*      */   {
/*  383 */     Node localNode1 = new Node(Thread.currentThread(), paramNode);
/*      */ 
/*  385 */     Node localNode2 = this.tail;
/*  386 */     if (localNode2 != null) {
/*  387 */       localNode1.prev = localNode2;
/*  388 */       if (compareAndSetTail(localNode2, localNode1)) {
/*  389 */         localNode2.next = localNode1;
/*  390 */         return localNode1;
/*      */       }
/*      */     }
/*  393 */     enq(localNode1);
/*  394 */     return localNode1;
/*      */   }
/*      */ 
/*      */   private void setHead(Node paramNode)
/*      */   {
/*  405 */     this.head = paramNode;
/*  406 */     paramNode.thread = null;
/*  407 */     paramNode.prev = null;
/*      */   }
/*      */ 
/*      */   private void unparkSuccessor(Node paramNode)
/*      */   {
/*  421 */     int i = paramNode.waitStatus;
/*  422 */     if (i < 0) {
/*  423 */       compareAndSetWaitStatus(paramNode, i, 0);
/*      */     }
/*      */ 
/*  431 */     Object localObject = paramNode.next;
/*  432 */     if ((localObject == null) || (((Node)localObject).waitStatus > 0)) {
/*  433 */       localObject = null;
/*  434 */       for (Node localNode = this.tail; (localNode != null) && (localNode != paramNode); localNode = localNode.prev)
/*  435 */         if (localNode.waitStatus <= 0)
/*  436 */           localObject = localNode;
/*      */     }
/*  438 */     if (localObject != null)
/*  439 */       LockSupport.unpark(((Node)localObject).thread);
/*      */   }
/*      */ 
/*      */   private void doReleaseShared()
/*      */   {
/*      */     while (true)
/*      */     {
/*  460 */       Node localNode = this.head;
/*  461 */       if ((localNode != null) && (localNode != this.tail)) {
/*  462 */         int i = localNode.waitStatus;
/*  463 */         if (i == -1) {
/*  464 */           if (compareAndSetWaitStatus(localNode, -1, 0))
/*      */           {
/*  466 */             unparkSuccessor(localNode);
/*      */           }
/*  468 */         } else if ((i == 0) && (!compareAndSetWaitStatus(localNode, 0, -3)));
/*      */       }
/*      */       else
/*      */       {
/*  472 */         if (localNode == this.head)
/*      */           break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setHeadAndPropagate(Node paramNode, long paramLong)
/*      */   {
/*  486 */     Node localNode1 = this.head;
/*  487 */     setHead(paramNode);
/*      */ 
/*  503 */     if ((paramLong > 0L) || (localNode1 == null) || (localNode1.waitStatus < 0)) {
/*  504 */       Node localNode2 = paramNode.next;
/*  505 */       if ((localNode2 == null) || (localNode2.isShared()))
/*  506 */         doReleaseShared();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void cancelAcquire(Node paramNode)
/*      */   {
/*  519 */     if (paramNode == null) {
/*  520 */       return;
/*      */     }
/*  522 */     paramNode.thread = null;
/*      */ 
/*  525 */     Node localNode1 = paramNode.prev;
/*  526 */     while (localNode1.waitStatus > 0) {
/*  527 */       paramNode.prev = (localNode1 = localNode1.prev);
/*      */     }
/*      */ 
/*  532 */     Node localNode2 = localNode1.next;
/*      */ 
/*  537 */     paramNode.waitStatus = 1;
/*      */ 
/*  540 */     if ((paramNode == this.tail) && (compareAndSetTail(paramNode, localNode1))) {
/*  541 */       compareAndSetNext(localNode1, localNode2, null);
/*      */     }
/*      */     else
/*      */     {
/*      */       int i;
/*  546 */       if ((localNode1 != this.head) && (((i = localNode1.waitStatus) == -1) || ((i <= 0) && (compareAndSetWaitStatus(localNode1, i, -1)))) && (localNode1.thread != null))
/*      */       {
/*  550 */         Node localNode3 = paramNode.next;
/*  551 */         if ((localNode3 != null) && (localNode3.waitStatus <= 0))
/*  552 */           compareAndSetNext(localNode1, localNode2, localNode3);
/*      */       } else {
/*  554 */         unparkSuccessor(paramNode);
/*      */       }
/*      */ 
/*  557 */       paramNode.next = paramNode;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean shouldParkAfterFailedAcquire(Node paramNode1, Node paramNode2)
/*      */   {
/*  571 */     int i = paramNode1.waitStatus;
/*  572 */     if (i == -1)
/*      */     {
/*  577 */       return true;
/*  578 */     }if (i > 0)
/*      */     {
/*      */       do
/*      */       {
/*  584 */         paramNode2.prev = (paramNode1 = paramNode1.prev);
/*  585 */       }while (paramNode1.waitStatus > 0);
/*  586 */       paramNode1.next = paramNode2;
/*      */     }
/*      */     else
/*      */     {
/*  593 */       compareAndSetWaitStatus(paramNode1, i, -1);
/*      */     }
/*  595 */     return false;
/*      */   }
/*      */ 
/*      */   private static void selfInterrupt()
/*      */   {
/*  602 */     Thread.currentThread().interrupt();
/*      */   }
/*      */ 
/*      */   private final boolean parkAndCheckInterrupt()
/*      */   {
/*  611 */     LockSupport.park(this);
/*  612 */     return Thread.interrupted();
/*      */   }
/*      */ 
/*      */   final boolean acquireQueued(Node paramNode, long paramLong)
/*      */   {
/*  633 */     int i = 1;
/*      */     try {
/*  635 */       boolean bool1 = false;
/*      */       while (true) {
/*  637 */         Node localNode = paramNode.predecessor();
/*  638 */         if ((localNode == this.head) && (tryAcquire(paramLong))) {
/*  639 */           setHead(paramNode);
/*  640 */           localNode.next = null;
/*  641 */           i = 0;
/*  642 */           return bool1;
/*      */         }
/*  644 */         if ((shouldParkAfterFailedAcquire(localNode, paramNode)) && (parkAndCheckInterrupt()))
/*      */         {
/*  646 */           bool1 = true;
/*      */         }
/*      */       }
/*      */     } finally { if (i != 0)
/*  650 */         cancelAcquire(paramNode);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doAcquireInterruptibly(long paramLong)
/*      */     throws InterruptedException
/*      */   {
/*  660 */     Node localNode1 = addWaiter(Node.EXCLUSIVE);
/*  661 */     int i = 1;
/*      */     try {
/*      */       while (true) {
/*  664 */         Node localNode2 = localNode1.predecessor();
/*  665 */         if ((localNode2 == this.head) && (tryAcquire(paramLong))) { setHead(localNode1);
/*  667 */           localNode2.next = null;
/*  668 */           i = 0;
/*      */           return; } if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (parkAndCheckInterrupt()))
/*      */         {
/*  673 */           throw new InterruptedException();
/*      */         }
/*      */       }
/*      */     } finally { if (i != 0)
/*  677 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean doAcquireNanos(long paramLong1, long paramLong2)
/*      */     throws InterruptedException
/*      */   {
/*  690 */     long l1 = System.nanoTime();
/*  691 */     Node localNode1 = addWaiter(Node.EXCLUSIVE);
/*  692 */     int i = 1;
/*      */     try {
/*      */       while (true) {
/*  695 */         Node localNode2 = localNode1.predecessor();
/*      */         boolean bool;
/*  696 */         if ((localNode2 == this.head) && (tryAcquire(paramLong1))) {
/*  697 */           setHead(localNode1);
/*  698 */           localNode2.next = null;
/*  699 */           i = 0;
/*  700 */           return true;
/*      */         }
/*  702 */         if (paramLong2 <= 0L)
/*  703 */           return false;
/*  704 */         if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (paramLong2 > 1000L))
/*      */         {
/*  706 */           LockSupport.parkNanos(this, paramLong2);
/*  707 */         }long l2 = System.nanoTime();
/*  708 */         paramLong2 -= l2 - l1;
/*  709 */         l1 = l2;
/*  710 */         if (Thread.interrupted())
/*  711 */           throw new InterruptedException();
/*      */       }
/*      */     } finally {
/*  714 */       if (i != 0)
/*  715 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doAcquireShared(long paramLong)
/*      */   {
/*  724 */     Node localNode1 = addWaiter(Node.SHARED);
/*  725 */     int i = 1;
/*      */     try {
/*  727 */       int j = 0;
/*      */       while (true) {
/*  729 */         Node localNode2 = localNode1.predecessor();
/*  730 */         if (localNode2 == this.head) {
/*  731 */           long l = tryAcquireShared(paramLong);
/*  732 */           if (l >= 0L) {
/*  733 */             setHeadAndPropagate(localNode1, l);
/*  734 */             localNode2.next = null;
/*  735 */             if (j != 0)
/*  736 */               selfInterrupt(); i = 0;
/*      */             return;
/*      */           }
/*      */         }
/*  741 */         if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (parkAndCheckInterrupt()))
/*      */         {
/*  743 */           j = 1;
/*      */         }
/*      */       }
/*      */     } finally { if (i != 0)
/*  747 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doAcquireSharedInterruptibly(long paramLong)
/*      */     throws InterruptedException
/*      */   {
/*  757 */     Node localNode1 = addWaiter(Node.SHARED);
/*  758 */     int i = 1;
/*      */     try {
/*      */       while (true) {
/*  761 */         Node localNode2 = localNode1.predecessor();
/*  762 */         if (localNode2 == this.head) {
/*  763 */           long l = tryAcquireShared(paramLong);
/*  764 */           if (l >= 0L) { setHeadAndPropagate(localNode1, l);
/*  766 */             localNode2.next = null;
/*  767 */             i = 0;
/*      */             return; }
/*  771 */         }if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (parkAndCheckInterrupt()))
/*      */         {
/*  773 */           throw new InterruptedException();
/*      */         }
/*      */       }
/*      */     } finally { if (i != 0)
/*  777 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean doAcquireSharedNanos(long paramLong1, long paramLong2)
/*      */     throws InterruptedException
/*      */   {
/*  791 */     long l1 = System.nanoTime();
/*  792 */     Node localNode1 = addWaiter(Node.SHARED);
/*  793 */     int i = 1;
/*      */     try {
/*      */       while (true) {
/*  796 */         Node localNode2 = localNode1.predecessor();
/*  797 */         if (localNode2 == this.head) {
/*  798 */           long l2 = tryAcquireShared(paramLong1);
/*  799 */           if (l2 >= 0L) {
/*  800 */             setHeadAndPropagate(localNode1, l2);
/*  801 */             localNode2.next = null;
/*  802 */             i = 0;
/*  803 */             return true;
/*      */           }
/*      */         }
/*  806 */         if (paramLong2 <= 0L)
/*  807 */           return false;
/*  808 */         if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (paramLong2 > 1000L))
/*      */         {
/*  810 */           LockSupport.parkNanos(this, paramLong2);
/*  811 */         }long l3 = System.nanoTime();
/*  812 */         paramLong2 -= l3 - l1;
/*  813 */         l1 = l3;
/*  814 */         if (Thread.interrupted())
/*  815 */           throw new InterruptedException();
/*      */       }
/*      */     } finally {
/*  818 */       if (i != 0)
/*  819 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean tryAcquire(long paramLong)
/*      */   {
/*  852 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean tryRelease(long paramLong)
/*      */   {
/*  878 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected long tryAcquireShared(long paramLong)
/*      */   {
/*  914 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean tryReleaseShared(long paramLong)
/*      */   {
/*  939 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean isHeldExclusively()
/*      */   {
/*  958 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public final void acquire(long paramLong)
/*      */   {
/*  974 */     if ((!tryAcquire(paramLong)) && (acquireQueued(addWaiter(Node.EXCLUSIVE), paramLong)))
/*      */     {
/*  976 */       selfInterrupt();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void acquireInterruptibly(long paramLong)
/*      */     throws InterruptedException
/*      */   {
/*  995 */     if (Thread.interrupted())
/*  996 */       throw new InterruptedException();
/*  997 */     if (!tryAcquire(paramLong))
/*  998 */       doAcquireInterruptibly(paramLong);
/*      */   }
/*      */ 
/*      */   public final boolean tryAcquireNanos(long paramLong1, long paramLong2)
/*      */     throws InterruptedException
/*      */   {
/* 1020 */     if (Thread.interrupted())
/* 1021 */       throw new InterruptedException();
/* 1022 */     return (tryAcquire(paramLong1)) || (doAcquireNanos(paramLong1, paramLong2));
/*      */   }
/*      */ 
/*      */   public final boolean release(long paramLong)
/*      */   {
/* 1037 */     if (tryRelease(paramLong)) {
/* 1038 */       Node localNode = this.head;
/* 1039 */       if ((localNode != null) && (localNode.waitStatus != 0))
/* 1040 */         unparkSuccessor(localNode);
/* 1041 */       return true;
/*      */     }
/* 1043 */     return false;
/*      */   }
/*      */ 
/*      */   public final void acquireShared(long paramLong)
/*      */   {
/* 1058 */     if (tryAcquireShared(paramLong) < 0L)
/* 1059 */       doAcquireShared(paramLong);
/*      */   }
/*      */ 
/*      */   public final void acquireSharedInterruptibly(long paramLong)
/*      */     throws InterruptedException
/*      */   {
/* 1077 */     if (Thread.interrupted())
/* 1078 */       throw new InterruptedException();
/* 1079 */     if (tryAcquireShared(paramLong) < 0L)
/* 1080 */       doAcquireSharedInterruptibly(paramLong);
/*      */   }
/*      */ 
/*      */   public final boolean tryAcquireSharedNanos(long paramLong1, long paramLong2)
/*      */     throws InterruptedException
/*      */   {
/* 1101 */     if (Thread.interrupted())
/* 1102 */       throw new InterruptedException();
/* 1103 */     return (tryAcquireShared(paramLong1) >= 0L) || (doAcquireSharedNanos(paramLong1, paramLong2));
/*      */   }
/*      */ 
/*      */   public final boolean releaseShared(long paramLong)
/*      */   {
/* 1117 */     if (tryReleaseShared(paramLong)) {
/* 1118 */       doReleaseShared();
/* 1119 */       return true;
/*      */     }
/* 1121 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean hasQueuedThreads()
/*      */   {
/* 1138 */     return this.head != this.tail;
/*      */   }
/*      */ 
/*      */   public final boolean hasContended()
/*      */   {
/* 1151 */     return this.head != null;
/*      */   }
/*      */ 
/*      */   public final Thread getFirstQueuedThread()
/*      */   {
/* 1167 */     return this.head == this.tail ? null : fullGetFirstQueuedThread();
/*      */   }
/*      */ 
/*      */   private Thread fullGetFirstQueuedThread()
/*      */   {
/*      */     Node localNode1;
/*      */     Node localNode2;
/*      */     Thread localThread1;
/* 1184 */     if ((((localNode1 = this.head) != null) && ((localNode2 = localNode1.next) != null) && (localNode2.prev == this.head) && ((localThread1 = localNode2.thread) != null)) || (((localNode1 = this.head) != null) && ((localNode2 = localNode1.next) != null) && (localNode2.prev == this.head) && ((localThread1 = localNode2.thread) != null)))
/*      */     {
/* 1188 */       return localThread1;
/*      */     }
/*      */ 
/* 1198 */     Node localNode3 = this.tail;
/* 1199 */     Object localObject = null;
/* 1200 */     while ((localNode3 != null) && (localNode3 != this.head)) {
/* 1201 */       Thread localThread2 = localNode3.thread;
/* 1202 */       if (localThread2 != null)
/* 1203 */         localObject = localThread2;
/* 1204 */       localNode3 = localNode3.prev;
/*      */     }
/* 1206 */     return localObject;
/*      */   }
/*      */ 
/*      */   public final boolean isQueued(Thread paramThread)
/*      */   {
/* 1220 */     if (paramThread == null)
/* 1221 */       throw new NullPointerException();
/* 1222 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev)
/* 1223 */       if (localNode.thread == paramThread)
/* 1224 */         return true;
/* 1225 */     return false;
/*      */   }
/*      */ 
/*      */   final boolean apparentlyFirstQueuedIsExclusive()
/*      */   {
/*      */     Node localNode1;
/*      */     Node localNode2;
/* 1239 */     return ((localNode1 = this.head) != null) && ((localNode2 = localNode1.next) != null) && (!localNode2.isShared()) && (localNode2.thread != null);
/*      */   }
/*      */ 
/*      */   public final boolean hasQueuedPredecessors()
/*      */   {
/* 1292 */     Node localNode1 = this.tail;
/* 1293 */     Node localNode2 = this.head;
/*      */     Node localNode3;
/* 1295 */     return (localNode2 != localNode1) && (((localNode3 = localNode2.next) == null) || (localNode3.thread != Thread.currentThread()));
/*      */   }
/*      */ 
/*      */   public final int getQueueLength()
/*      */   {
/* 1313 */     int i = 0;
/* 1314 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev) {
/* 1315 */       if (localNode.thread != null)
/* 1316 */         i++;
/*      */     }
/* 1318 */     return i;
/*      */   }
/*      */ 
/*      */   public final Collection<Thread> getQueuedThreads()
/*      */   {
/* 1333 */     ArrayList localArrayList = new ArrayList();
/* 1334 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev) {
/* 1335 */       Thread localThread = localNode.thread;
/* 1336 */       if (localThread != null)
/* 1337 */         localArrayList.add(localThread);
/*      */     }
/* 1339 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public final Collection<Thread> getExclusiveQueuedThreads()
/*      */   {
/* 1351 */     ArrayList localArrayList = new ArrayList();
/* 1352 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev) {
/* 1353 */       if (!localNode.isShared()) {
/* 1354 */         Thread localThread = localNode.thread;
/* 1355 */         if (localThread != null)
/* 1356 */           localArrayList.add(localThread);
/*      */       }
/*      */     }
/* 1359 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public final Collection<Thread> getSharedQueuedThreads()
/*      */   {
/* 1371 */     ArrayList localArrayList = new ArrayList();
/* 1372 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev) {
/* 1373 */       if (localNode.isShared()) {
/* 1374 */         Thread localThread = localNode.thread;
/* 1375 */         if (localThread != null)
/* 1376 */           localArrayList.add(localThread);
/*      */       }
/*      */     }
/* 1379 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1392 */     long l = getState();
/* 1393 */     String str = hasQueuedThreads() ? "non" : "";
/* 1394 */     return super.toString() + "[State = " + l + ", " + str + "empty queue]";
/*      */   }
/*      */ 
/*      */   final boolean isOnSyncQueue(Node paramNode)
/*      */   {
/* 1408 */     if ((paramNode.waitStatus == -2) || (paramNode.prev == null))
/* 1409 */       return false;
/* 1410 */     if (paramNode.next != null) {
/* 1411 */       return true;
/*      */     }
/*      */ 
/* 1420 */     return findNodeFromTail(paramNode);
/*      */   }
/*      */ 
/*      */   private boolean findNodeFromTail(Node paramNode)
/*      */   {
/* 1429 */     Node localNode = this.tail;
/*      */     while (true) {
/* 1431 */       if (localNode == paramNode)
/* 1432 */         return true;
/* 1433 */       if (localNode == null)
/* 1434 */         return false;
/* 1435 */       localNode = localNode.prev;
/*      */     }
/*      */   }
/*      */ 
/*      */   final boolean transferForSignal(Node paramNode)
/*      */   {
/* 1450 */     if (!compareAndSetWaitStatus(paramNode, -2, 0)) {
/* 1451 */       return false;
/*      */     }
/*      */ 
/* 1459 */     Node localNode = enq(paramNode);
/* 1460 */     int i = localNode.waitStatus;
/* 1461 */     if ((i > 0) || (!compareAndSetWaitStatus(localNode, i, -1)))
/* 1462 */       LockSupport.unpark(paramNode.thread);
/* 1463 */     return true;
/*      */   }
/*      */ 
/*      */   final boolean transferAfterCancelledWait(Node paramNode)
/*      */   {
/* 1475 */     if (compareAndSetWaitStatus(paramNode, -2, 0)) {
/* 1476 */       enq(paramNode);
/* 1477 */       return true;
/*      */     }
/*      */ 
/* 1485 */     while (!isOnSyncQueue(paramNode))
/* 1486 */       Thread.yield();
/* 1487 */     return false;
/*      */   }
/*      */ 
/*      */   final long fullyRelease(Node paramNode)
/*      */   {
/* 1497 */     int i = 1;
/*      */     try {
/* 1499 */       long l1 = getState();
/* 1500 */       if (release(l1)) {
/* 1501 */         i = 0;
/* 1502 */         return l1;
/*      */       }
/* 1504 */       throw new IllegalMonitorStateException();
/*      */     }
/*      */     finally {
/* 1507 */       if (i != 0)
/* 1508 */         paramNode.waitStatus = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean owns(ConditionObject paramConditionObject)
/*      */   {
/* 1523 */     if (paramConditionObject == null)
/* 1524 */       throw new NullPointerException();
/* 1525 */     return paramConditionObject.isOwnedBy(this);
/*      */   }
/*      */ 
/*      */   public final boolean hasWaiters(ConditionObject paramConditionObject)
/*      */   {
/* 1545 */     if (!owns(paramConditionObject))
/* 1546 */       throw new IllegalArgumentException("Not owner");
/* 1547 */     return paramConditionObject.hasWaiters();
/*      */   }
/*      */ 
/*      */   public final int getWaitQueueLength(ConditionObject paramConditionObject)
/*      */   {
/* 1567 */     if (!owns(paramConditionObject))
/* 1568 */       throw new IllegalArgumentException("Not owner");
/* 1569 */     return paramConditionObject.getWaitQueueLength();
/*      */   }
/*      */ 
/*      */   public final Collection<Thread> getWaitingThreads(ConditionObject paramConditionObject)
/*      */   {
/* 1589 */     if (!owns(paramConditionObject))
/* 1590 */       throw new IllegalArgumentException("Not owner");
/* 1591 */     return paramConditionObject.getWaitingThreads();
/*      */   }
/*      */ 
/*      */   private final boolean compareAndSetHead(Node paramNode)
/*      */   {
/* 2081 */     return unsafe.compareAndSwapObject(this, headOffset, null, paramNode);
/*      */   }
/*      */ 
/*      */   private final boolean compareAndSetTail(Node paramNode1, Node paramNode2)
/*      */   {
/* 2088 */     return unsafe.compareAndSwapObject(this, tailOffset, paramNode1, paramNode2);
/*      */   }
/*      */ 
/*      */   private static final boolean compareAndSetWaitStatus(Node paramNode, int paramInt1, int paramInt2)
/*      */   {
/* 2097 */     return unsafe.compareAndSwapInt(paramNode, waitStatusOffset, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private static final boolean compareAndSetNext(Node paramNode1, Node paramNode2, Node paramNode3)
/*      */   {
/* 2107 */     return unsafe.compareAndSwapObject(paramNode1, nextOffset, paramNode2, paramNode3);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/* 2063 */       stateOffset = unsafe.objectFieldOffset(AbstractQueuedLongSynchronizer.class.getDeclaredField("state"));
/*      */ 
/* 2065 */       headOffset = unsafe.objectFieldOffset(AbstractQueuedLongSynchronizer.class.getDeclaredField("head"));
/*      */ 
/* 2067 */       tailOffset = unsafe.objectFieldOffset(AbstractQueuedLongSynchronizer.class.getDeclaredField("tail"));
/*      */ 
/* 2069 */       waitStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
/*      */ 
/* 2071 */       nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("next"));
/*      */     }
/*      */     catch (Exception localException) {
/* 2074 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ConditionObject
/*      */     implements Condition, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1173984872572414699L;
/*      */     private transient AbstractQueuedLongSynchronizer.Node firstWaiter;
/*      */     private transient AbstractQueuedLongSynchronizer.Node lastWaiter;
/*      */     private static final int REINTERRUPT = 1;
/*      */     private static final int THROW_IE = -1;
/*      */ 
/*      */     public ConditionObject()
/*      */     {
/*      */     }
/*      */ 
/*      */     private AbstractQueuedLongSynchronizer.Node addConditionWaiter()
/*      */     {
/* 1630 */       AbstractQueuedLongSynchronizer.Node localNode1 = this.lastWaiter;
/*      */ 
/* 1632 */       if ((localNode1 != null) && (localNode1.waitStatus != -2)) {
/* 1633 */         unlinkCancelledWaiters();
/* 1634 */         localNode1 = this.lastWaiter;
/*      */       }
/* 1636 */       AbstractQueuedLongSynchronizer.Node localNode2 = new AbstractQueuedLongSynchronizer.Node(Thread.currentThread(), -2);
/* 1637 */       if (localNode1 == null)
/* 1638 */         this.firstWaiter = localNode2;
/*      */       else
/* 1640 */         localNode1.nextWaiter = localNode2;
/* 1641 */       this.lastWaiter = localNode2;
/* 1642 */       return localNode2;
/*      */     }
/*      */ 
/*      */     private void doSignal(AbstractQueuedLongSynchronizer.Node paramNode)
/*      */     {
/*      */       do
/*      */       {
/* 1653 */         if ((this.firstWaiter = paramNode.nextWaiter) == null)
/* 1654 */           this.lastWaiter = null;
/* 1655 */         paramNode.nextWaiter = null;
/* 1656 */       }while ((!AbstractQueuedLongSynchronizer.this.transferForSignal(paramNode)) && ((paramNode = this.firstWaiter) != null));
/*      */     }
/*      */ 
/*      */     private void doSignalAll(AbstractQueuedLongSynchronizer.Node paramNode)
/*      */     {
/* 1665 */       this.lastWaiter = (this.firstWaiter = null);
/*      */       do {
/* 1667 */         AbstractQueuedLongSynchronizer.Node localNode = paramNode.nextWaiter;
/* 1668 */         paramNode.nextWaiter = null;
/* 1669 */         AbstractQueuedLongSynchronizer.this.transferForSignal(paramNode);
/* 1670 */         paramNode = localNode;
/* 1671 */       }while (paramNode != null);
/*      */     }
/*      */ 
/*      */     private void unlinkCancelledWaiters()
/*      */     {
/* 1689 */       Object localObject1 = this.firstWaiter;
/* 1690 */       Object localObject2 = null;
/* 1691 */       while (localObject1 != null) {
/* 1692 */         AbstractQueuedLongSynchronizer.Node localNode = ((AbstractQueuedLongSynchronizer.Node)localObject1).nextWaiter;
/* 1693 */         if (((AbstractQueuedLongSynchronizer.Node)localObject1).waitStatus != -2) {
/* 1694 */           ((AbstractQueuedLongSynchronizer.Node)localObject1).nextWaiter = null;
/* 1695 */           if (localObject2 == null)
/* 1696 */             this.firstWaiter = localNode;
/*      */           else
/* 1698 */             localObject2.nextWaiter = localNode;
/* 1699 */           if (localNode == null)
/* 1700 */             this.lastWaiter = localObject2;
/*      */         }
/*      */         else {
/* 1703 */           localObject2 = localObject1;
/* 1704 */         }localObject1 = localNode;
/*      */       }
/*      */     }
/*      */ 
/*      */     public final void signal()
/*      */     {
/* 1719 */       if (!AbstractQueuedLongSynchronizer.this.isHeldExclusively())
/* 1720 */         throw new IllegalMonitorStateException();
/* 1721 */       AbstractQueuedLongSynchronizer.Node localNode = this.firstWaiter;
/* 1722 */       if (localNode != null)
/* 1723 */         doSignal(localNode);
/*      */     }
/*      */ 
/*      */     public final void signalAll()
/*      */     {
/* 1734 */       if (!AbstractQueuedLongSynchronizer.this.isHeldExclusively())
/* 1735 */         throw new IllegalMonitorStateException();
/* 1736 */       AbstractQueuedLongSynchronizer.Node localNode = this.firstWaiter;
/* 1737 */       if (localNode != null)
/* 1738 */         doSignalAll(localNode);
/*      */     }
/*      */ 
/*      */     public final void awaitUninterruptibly()
/*      */     {
/* 1754 */       AbstractQueuedLongSynchronizer.Node localNode = addConditionWaiter();
/* 1755 */       long l = AbstractQueuedLongSynchronizer.this.fullyRelease(localNode);
/* 1756 */       int i = 0;
/* 1757 */       while (!AbstractQueuedLongSynchronizer.this.isOnSyncQueue(localNode)) {
/* 1758 */         LockSupport.park(this);
/* 1759 */         if (Thread.interrupted())
/* 1760 */           i = 1;
/*      */       }
/* 1762 */       if ((AbstractQueuedLongSynchronizer.this.acquireQueued(localNode, l)) || (i != 0))
/* 1763 */         AbstractQueuedLongSynchronizer.access$000();
/*      */     }
/*      */ 
/*      */     private int checkInterruptWhileWaiting(AbstractQueuedLongSynchronizer.Node paramNode)
/*      */     {
/* 1784 */       return Thread.interrupted() ? 1 : AbstractQueuedLongSynchronizer.this.transferAfterCancelledWait(paramNode) ? -1 : 0;
/*      */     }
/*      */ 
/*      */     private void reportInterruptAfterWait(int paramInt)
/*      */       throws InterruptedException
/*      */     {
/* 1795 */       if (paramInt == -1)
/* 1796 */         throw new InterruptedException();
/* 1797 */       if (paramInt == 1)
/* 1798 */         AbstractQueuedLongSynchronizer.access$000();
/*      */     }
/*      */ 
/*      */     public final void await()
/*      */       throws InterruptedException
/*      */     {
/* 1816 */       if (Thread.interrupted())
/* 1817 */         throw new InterruptedException();
/* 1818 */       AbstractQueuedLongSynchronizer.Node localNode = addConditionWaiter();
/* 1819 */       long l = AbstractQueuedLongSynchronizer.this.fullyRelease(localNode);
/* 1820 */       int i = 0;
/* 1821 */       while (!AbstractQueuedLongSynchronizer.this.isOnSyncQueue(localNode)) {
/* 1822 */         LockSupport.park(this);
/* 1823 */         if ((i = checkInterruptWhileWaiting(localNode)) != 0)
/* 1824 */           break;
/*      */       }
/* 1826 */       if ((AbstractQueuedLongSynchronizer.this.acquireQueued(localNode, l)) && (i != -1))
/* 1827 */         i = 1;
/* 1828 */       if (localNode.nextWaiter != null)
/* 1829 */         unlinkCancelledWaiters();
/* 1830 */       if (i != 0)
/* 1831 */         reportInterruptAfterWait(i);
/*      */     }
/*      */ 
/*      */     public final long awaitNanos(long paramLong)
/*      */       throws InterruptedException
/*      */     {
/* 1850 */       if (Thread.interrupted())
/* 1851 */         throw new InterruptedException();
/* 1852 */       AbstractQueuedLongSynchronizer.Node localNode = addConditionWaiter();
/* 1853 */       long l1 = AbstractQueuedLongSynchronizer.this.fullyRelease(localNode);
/* 1854 */       long l2 = System.nanoTime();
/* 1855 */       int i = 0;
/* 1856 */       while (!AbstractQueuedLongSynchronizer.this.isOnSyncQueue(localNode)) {
/* 1857 */         if (paramLong <= 0L) {
/* 1858 */           AbstractQueuedLongSynchronizer.this.transferAfterCancelledWait(localNode);
/* 1859 */           break;
/*      */         }
/* 1861 */         LockSupport.parkNanos(this, paramLong);
/* 1862 */         if ((i = checkInterruptWhileWaiting(localNode)) != 0) {
/*      */           break;
/*      */         }
/* 1865 */         long l3 = System.nanoTime();
/* 1866 */         paramLong -= l3 - l2;
/* 1867 */         l2 = l3;
/*      */       }
/* 1869 */       if ((AbstractQueuedLongSynchronizer.this.acquireQueued(localNode, l1)) && (i != -1))
/* 1870 */         i = 1;
/* 1871 */       if (localNode.nextWaiter != null)
/* 1872 */         unlinkCancelledWaiters();
/* 1873 */       if (i != 0)
/* 1874 */         reportInterruptAfterWait(i);
/* 1875 */       return paramLong - (System.nanoTime() - l2);
/*      */     }
/*      */ 
/*      */     public final boolean awaitUntil(Date paramDate)
/*      */       throws InterruptedException
/*      */     {
/* 1895 */       if (paramDate == null)
/* 1896 */         throw new NullPointerException();
/* 1897 */       long l1 = paramDate.getTime();
/* 1898 */       if (Thread.interrupted())
/* 1899 */         throw new InterruptedException();
/* 1900 */       AbstractQueuedLongSynchronizer.Node localNode = addConditionWaiter();
/* 1901 */       long l2 = AbstractQueuedLongSynchronizer.this.fullyRelease(localNode);
/* 1902 */       boolean bool = false;
/* 1903 */       int i = 0;
/* 1904 */       while (!AbstractQueuedLongSynchronizer.this.isOnSyncQueue(localNode))
/* 1905 */         if (System.currentTimeMillis() > l1) {
/* 1906 */           bool = AbstractQueuedLongSynchronizer.this.transferAfterCancelledWait(localNode);
/*      */         }
/*      */         else {
/* 1909 */           LockSupport.parkUntil(this, l1);
/* 1910 */           if ((i = checkInterruptWhileWaiting(localNode)) != 0)
/* 1911 */             break;
/*      */         }
/* 1913 */       if ((AbstractQueuedLongSynchronizer.this.acquireQueued(localNode, l2)) && (i != -1))
/* 1914 */         i = 1;
/* 1915 */       if (localNode.nextWaiter != null)
/* 1916 */         unlinkCancelledWaiters();
/* 1917 */       if (i != 0)
/* 1918 */         reportInterruptAfterWait(i);
/* 1919 */       return !bool;
/*      */     }
/*      */ 
/*      */     public final boolean await(long paramLong, TimeUnit paramTimeUnit)
/*      */       throws InterruptedException
/*      */     {
/* 1939 */       if (paramTimeUnit == null)
/* 1940 */         throw new NullPointerException();
/* 1941 */       long l1 = paramTimeUnit.toNanos(paramLong);
/* 1942 */       if (Thread.interrupted())
/* 1943 */         throw new InterruptedException();
/* 1944 */       AbstractQueuedLongSynchronizer.Node localNode = addConditionWaiter();
/* 1945 */       long l2 = AbstractQueuedLongSynchronizer.this.fullyRelease(localNode);
/* 1946 */       long l3 = System.nanoTime();
/* 1947 */       boolean bool = false;
/* 1948 */       int i = 0;
/* 1949 */       while (!AbstractQueuedLongSynchronizer.this.isOnSyncQueue(localNode)) {
/* 1950 */         if (l1 <= 0L) {
/* 1951 */           bool = AbstractQueuedLongSynchronizer.this.transferAfterCancelledWait(localNode);
/* 1952 */           break;
/*      */         }
/* 1954 */         if (l1 >= 1000L)
/* 1955 */           LockSupport.parkNanos(this, l1);
/* 1956 */         if ((i = checkInterruptWhileWaiting(localNode)) != 0)
/*      */           break;
/* 1958 */         long l4 = System.nanoTime();
/* 1959 */         l1 -= l4 - l3;
/* 1960 */         l3 = l4;
/*      */       }
/* 1962 */       if ((AbstractQueuedLongSynchronizer.this.acquireQueued(localNode, l2)) && (i != -1))
/* 1963 */         i = 1;
/* 1964 */       if (localNode.nextWaiter != null)
/* 1965 */         unlinkCancelledWaiters();
/* 1966 */       if (i != 0)
/* 1967 */         reportInterruptAfterWait(i);
/* 1968 */       return !bool;
/*      */     }
/*      */ 
/*      */     final boolean isOwnedBy(AbstractQueuedLongSynchronizer paramAbstractQueuedLongSynchronizer)
/*      */     {
/* 1980 */       return paramAbstractQueuedLongSynchronizer == AbstractQueuedLongSynchronizer.this;
/*      */     }
/*      */ 
/*      */     protected final boolean hasWaiters()
/*      */     {
/* 1992 */       if (!AbstractQueuedLongSynchronizer.this.isHeldExclusively())
/* 1993 */         throw new IllegalMonitorStateException();
/* 1994 */       for (AbstractQueuedLongSynchronizer.Node localNode = this.firstWaiter; localNode != null; localNode = localNode.nextWaiter) {
/* 1995 */         if (localNode.waitStatus == -2)
/* 1996 */           return true;
/*      */       }
/* 1998 */       return false;
/*      */     }
/*      */ 
/*      */     protected final int getWaitQueueLength()
/*      */     {
/* 2011 */       if (!AbstractQueuedLongSynchronizer.this.isHeldExclusively())
/* 2012 */         throw new IllegalMonitorStateException();
/* 2013 */       int i = 0;
/* 2014 */       for (AbstractQueuedLongSynchronizer.Node localNode = this.firstWaiter; localNode != null; localNode = localNode.nextWaiter) {
/* 2015 */         if (localNode.waitStatus == -2)
/* 2016 */           i++;
/*      */       }
/* 2018 */       return i;
/*      */     }
/*      */ 
/*      */     protected final Collection<Thread> getWaitingThreads()
/*      */     {
/* 2031 */       if (!AbstractQueuedLongSynchronizer.this.isHeldExclusively())
/* 2032 */         throw new IllegalMonitorStateException();
/* 2033 */       ArrayList localArrayList = new ArrayList();
/* 2034 */       for (AbstractQueuedLongSynchronizer.Node localNode = this.firstWaiter; localNode != null; localNode = localNode.nextWaiter) {
/* 2035 */         if (localNode.waitStatus == -2) {
/* 2036 */           Thread localThread = localNode.thread;
/* 2037 */           if (localThread != null)
/* 2038 */             localArrayList.add(localThread);
/*      */         }
/*      */       }
/* 2041 */       return localArrayList;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Node
/*      */   {
/*  159 */     static final Node SHARED = new Node();
/*      */ 
/*  161 */     static final Node EXCLUSIVE = null;
/*      */     static final int CANCELLED = 1;
/*      */     static final int SIGNAL = -1;
/*      */     static final int CONDITION = -2;
/*      */     static final int PROPAGATE = -3;
/*      */     volatile int waitStatus;
/*      */     volatile Node prev;
/*      */     volatile Node next;
/*      */     volatile Thread thread;
/*      */     Node nextWaiter;
/*      */ 
/*      */     final boolean isShared()
/*      */     {
/*  261 */       return this.nextWaiter == SHARED;
/*      */     }
/*      */ 
/*      */     final Node predecessor()
/*      */       throws NullPointerException
/*      */     {
/*  272 */       Node localNode = this.prev;
/*  273 */       if (localNode == null) {
/*  274 */         throw new NullPointerException();
/*      */       }
/*  276 */       return localNode;
/*      */     }
/*      */ 
/*      */     Node() {
/*      */     }
/*      */ 
/*      */     Node(Thread paramThread, Node paramNode) {
/*  283 */       this.nextWaiter = paramNode;
/*  284 */       this.thread = paramThread;
/*      */     }
/*      */ 
/*      */     Node(Thread paramThread, int paramInt) {
/*  288 */       this.waitStatus = paramInt;
/*  289 */       this.thread = paramThread;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.AbstractQueuedLongSynchronizer
 * JD-Core Version:    0.6.2
 */