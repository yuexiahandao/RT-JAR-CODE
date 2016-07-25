/*      */ package java.util.concurrent.locks;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 7373984972572414691L;
/*      */   private volatile transient Node head;
/*      */   private volatile transient Node tail;
/*      */   private volatile int state;
/*      */   static final long spinForTimeoutThreshold = 1000L;
/* 2275 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*      */   private static final long stateOffset;
/*      */   private static final long headOffset;
/*      */   private static final long tailOffset;
/*      */   private static final long waitStatusOffset;
/*      */   private static final long nextOffset;
/*      */ 
/*      */   protected final int getState()
/*      */   {
/*  541 */     return this.state;
/*      */   }
/*      */ 
/*      */   protected final void setState(int paramInt)
/*      */   {
/*  550 */     this.state = paramInt;
/*      */   }
/*      */ 
/*      */   protected final boolean compareAndSetState(int paramInt1, int paramInt2)
/*      */   {
/*  566 */     return unsafe.compareAndSwapInt(this, stateOffset, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private Node enq(Node paramNode)
/*      */   {
/*      */     while (true)
/*      */     {
/*  585 */       Node localNode = this.tail;
/*  586 */       if (localNode == null) {
/*  587 */         if (compareAndSetHead(new Node()))
/*  588 */           this.tail = this.head;
/*      */       } else {
/*  590 */         paramNode.prev = localNode;
/*  591 */         if (compareAndSetTail(localNode, paramNode)) {
/*  592 */           localNode.next = paramNode;
/*  593 */           return localNode;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node addWaiter(Node paramNode)
/*      */   {
/*  606 */     Node localNode1 = new Node(Thread.currentThread(), paramNode);
/*      */ 
/*  608 */     Node localNode2 = this.tail;
/*  609 */     if (localNode2 != null) {
/*  610 */       localNode1.prev = localNode2;
/*  611 */       if (compareAndSetTail(localNode2, localNode1)) {
/*  612 */         localNode2.next = localNode1;
/*  613 */         return localNode1;
/*      */       }
/*      */     }
/*  616 */     enq(localNode1);
/*  617 */     return localNode1;
/*      */   }
/*      */ 
/*      */   private void setHead(Node paramNode)
/*      */   {
/*  628 */     this.head = paramNode;
/*  629 */     paramNode.thread = null;
/*  630 */     paramNode.prev = null;
/*      */   }
/*      */ 
/*      */   private void unparkSuccessor(Node paramNode)
/*      */   {
/*  644 */     int i = paramNode.waitStatus;
/*  645 */     if (i < 0) {
/*  646 */       compareAndSetWaitStatus(paramNode, i, 0);
/*      */     }
/*      */ 
/*  654 */     Object localObject = paramNode.next;
/*  655 */     if ((localObject == null) || (((Node)localObject).waitStatus > 0)) {
/*  656 */       localObject = null;
/*  657 */       for (Node localNode = this.tail; (localNode != null) && (localNode != paramNode); localNode = localNode.prev)
/*  658 */         if (localNode.waitStatus <= 0)
/*  659 */           localObject = localNode;
/*      */     }
/*  661 */     if (localObject != null)
/*  662 */       LockSupport.unpark(((Node)localObject).thread);
/*      */   }
/*      */ 
/*      */   private void doReleaseShared()
/*      */   {
/*      */     while (true)
/*      */     {
/*  683 */       Node localNode = this.head;
/*  684 */       if ((localNode != null) && (localNode != this.tail)) {
/*  685 */         int i = localNode.waitStatus;
/*  686 */         if (i == -1) {
/*  687 */           if (compareAndSetWaitStatus(localNode, -1, 0))
/*      */           {
/*  689 */             unparkSuccessor(localNode);
/*      */           }
/*  691 */         } else if ((i == 0) && (!compareAndSetWaitStatus(localNode, 0, -3)));
/*      */       }
/*      */       else
/*      */       {
/*  695 */         if (localNode == this.head)
/*      */           break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setHeadAndPropagate(Node paramNode, int paramInt)
/*      */   {
/*  709 */     Node localNode1 = this.head;
/*  710 */     setHead(paramNode);
/*      */ 
/*  726 */     if ((paramInt > 0) || (localNode1 == null) || (localNode1.waitStatus < 0)) {
/*  727 */       Node localNode2 = paramNode.next;
/*  728 */       if ((localNode2 == null) || (localNode2.isShared()))
/*  729 */         doReleaseShared();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void cancelAcquire(Node paramNode)
/*      */   {
/*  742 */     if (paramNode == null) {
/*  743 */       return;
/*      */     }
/*  745 */     paramNode.thread = null;
/*      */ 
/*  748 */     Node localNode1 = paramNode.prev;
/*  749 */     while (localNode1.waitStatus > 0) {
/*  750 */       paramNode.prev = (localNode1 = localNode1.prev);
/*      */     }
/*      */ 
/*  755 */     Node localNode2 = localNode1.next;
/*      */ 
/*  760 */     paramNode.waitStatus = 1;
/*      */ 
/*  763 */     if ((paramNode == this.tail) && (compareAndSetTail(paramNode, localNode1))) {
/*  764 */       compareAndSetNext(localNode1, localNode2, null);
/*      */     }
/*      */     else
/*      */     {
/*      */       int i;
/*  769 */       if ((localNode1 != this.head) && (((i = localNode1.waitStatus) == -1) || ((i <= 0) && (compareAndSetWaitStatus(localNode1, i, -1)))) && (localNode1.thread != null))
/*      */       {
/*  773 */         Node localNode3 = paramNode.next;
/*  774 */         if ((localNode3 != null) && (localNode3.waitStatus <= 0))
/*  775 */           compareAndSetNext(localNode1, localNode2, localNode3);
/*      */       } else {
/*  777 */         unparkSuccessor(paramNode);
/*      */       }
/*      */ 
/*  780 */       paramNode.next = paramNode;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean shouldParkAfterFailedAcquire(Node paramNode1, Node paramNode2)
/*      */   {
/*  794 */     int i = paramNode1.waitStatus;
/*  795 */     if (i == -1)
/*      */     {
/*  800 */       return true;
/*  801 */     }if (i > 0)
/*      */     {
/*      */       do
/*      */       {
/*  807 */         paramNode2.prev = (paramNode1 = paramNode1.prev);
/*  808 */       }while (paramNode1.waitStatus > 0);
/*  809 */       paramNode1.next = paramNode2;
/*      */     }
/*      */     else
/*      */     {
/*  816 */       compareAndSetWaitStatus(paramNode1, i, -1);
/*      */     }
/*  818 */     return false;
/*      */   }
/*      */ 
/*      */   private static void selfInterrupt()
/*      */   {
/*  825 */     Thread.currentThread().interrupt();
/*      */   }
/*      */ 
/*      */   private final boolean parkAndCheckInterrupt()
/*      */   {
/*  834 */     LockSupport.park(this);
/*  835 */     return Thread.interrupted();
/*      */   }
/*      */ 
/*      */   final boolean acquireQueued(Node paramNode, int paramInt)
/*      */   {
/*  856 */     int i = 1;
/*      */     try {
/*  858 */       boolean bool1 = false;
/*      */       while (true) {
/*  860 */         Node localNode = paramNode.predecessor();
/*  861 */         if ((localNode == this.head) && (tryAcquire(paramInt))) {
/*  862 */           setHead(paramNode);
/*  863 */           localNode.next = null;
/*  864 */           i = 0;
/*  865 */           return bool1;
/*      */         }
/*  867 */         if ((shouldParkAfterFailedAcquire(localNode, paramNode)) && (parkAndCheckInterrupt()))
/*      */         {
/*  869 */           bool1 = true;
/*      */         }
/*      */       }
/*      */     } finally { if (i != 0)
/*  873 */         cancelAcquire(paramNode);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doAcquireInterruptibly(int paramInt)
/*      */     throws InterruptedException
/*      */   {
/*  883 */     Node localNode1 = addWaiter(Node.EXCLUSIVE);
/*  884 */     int i = 1;
/*      */     try {
/*      */       while (true) {
/*  887 */         Node localNode2 = localNode1.predecessor();
/*  888 */         if ((localNode2 == this.head) && (tryAcquire(paramInt))) { setHead(localNode1);
/*  890 */           localNode2.next = null;
/*  891 */           i = 0;
/*      */           return; } if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (parkAndCheckInterrupt()))
/*      */         {
/*  896 */           throw new InterruptedException();
/*      */         }
/*      */       }
/*      */     } finally { if (i != 0)
/*  900 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean doAcquireNanos(int paramInt, long paramLong)
/*      */     throws InterruptedException
/*      */   {
/*  913 */     long l1 = System.nanoTime();
/*  914 */     Node localNode1 = addWaiter(Node.EXCLUSIVE);
/*  915 */     int i = 1;
/*      */     try {
/*      */       while (true) {
/*  918 */         Node localNode2 = localNode1.predecessor();
/*      */         boolean bool;
/*  919 */         if ((localNode2 == this.head) && (tryAcquire(paramInt))) {
/*  920 */           setHead(localNode1);
/*  921 */           localNode2.next = null;
/*  922 */           i = 0;
/*  923 */           return true;
/*      */         }
/*  925 */         if (paramLong <= 0L)
/*  926 */           return false;
/*  927 */         if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (paramLong > 1000L))
/*      */         {
/*  929 */           LockSupport.parkNanos(this, paramLong);
/*  930 */         }long l2 = System.nanoTime();
/*  931 */         paramLong -= l2 - l1;
/*  932 */         l1 = l2;
/*  933 */         if (Thread.interrupted())
/*  934 */           throw new InterruptedException();
/*      */       }
/*      */     } finally {
/*  937 */       if (i != 0)
/*  938 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doAcquireShared(int paramInt)
/*      */   {
/*  947 */     Node localNode1 = addWaiter(Node.SHARED);
/*  948 */     int i = 1;
/*      */     try {
/*  950 */       int j = 0;
/*      */       while (true) {
/*  952 */         Node localNode2 = localNode1.predecessor();
/*  953 */         if (localNode2 == this.head) {
/*  954 */           int k = tryAcquireShared(paramInt);
/*  955 */           if (k >= 0) {
/*  956 */             setHeadAndPropagate(localNode1, k);
/*  957 */             localNode2.next = null;
/*  958 */             if (j != 0)
/*  959 */               selfInterrupt(); i = 0;
/*      */             return;
/*      */           }
/*      */         }
/*  964 */         if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (parkAndCheckInterrupt()))
/*      */         {
/*  966 */           j = 1;
/*      */         }
/*      */       }
/*      */     } finally { if (i != 0)
/*  970 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doAcquireSharedInterruptibly(int paramInt)
/*      */     throws InterruptedException
/*      */   {
/*  980 */     Node localNode1 = addWaiter(Node.SHARED);
/*  981 */     int i = 1;
/*      */     try {
/*      */       while (true) {
/*  984 */         Node localNode2 = localNode1.predecessor();
/*  985 */         if (localNode2 == this.head) {
/*  986 */           int j = tryAcquireShared(paramInt);
/*  987 */           if (j >= 0) { setHeadAndPropagate(localNode1, j);
/*  989 */             localNode2.next = null;
/*  990 */             i = 0;
/*      */             return; }
/*  994 */         }if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (parkAndCheckInterrupt()))
/*      */         {
/*  996 */           throw new InterruptedException();
/*      */         }
/*      */       }
/*      */     } finally { if (i != 0)
/* 1000 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean doAcquireSharedNanos(int paramInt, long paramLong)
/*      */     throws InterruptedException
/*      */   {
/* 1014 */     long l1 = System.nanoTime();
/* 1015 */     Node localNode1 = addWaiter(Node.SHARED);
/* 1016 */     int i = 1;
/*      */     try {
/*      */       while (true) {
/* 1019 */         Node localNode2 = localNode1.predecessor();
/*      */         int j;
/* 1020 */         if (localNode2 == this.head) {
/* 1021 */           j = tryAcquireShared(paramInt);
/* 1022 */           if (j >= 0) {
/* 1023 */             setHeadAndPropagate(localNode1, j);
/* 1024 */             localNode2.next = null;
/* 1025 */             i = 0;
/* 1026 */             return true;
/*      */           }
/*      */         }
/* 1029 */         if (paramLong <= 0L)
/* 1030 */           return 0;
/* 1031 */         if ((shouldParkAfterFailedAcquire(localNode2, localNode1)) && (paramLong > 1000L))
/*      */         {
/* 1033 */           LockSupport.parkNanos(this, paramLong);
/* 1034 */         }long l2 = System.nanoTime();
/* 1035 */         paramLong -= l2 - l1;
/* 1036 */         l1 = l2;
/* 1037 */         if (Thread.interrupted())
/* 1038 */           throw new InterruptedException();
/*      */       }
/*      */     } finally {
/* 1041 */       if (i != 0)
/* 1042 */         cancelAcquire(localNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean tryAcquire(int paramInt)
/*      */   {
/* 1075 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean tryRelease(int paramInt)
/*      */   {
/* 1101 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected int tryAcquireShared(int paramInt)
/*      */   {
/* 1137 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean tryReleaseShared(int paramInt)
/*      */   {
/* 1162 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean isHeldExclusively()
/*      */   {
/* 1181 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public final void acquire(int paramInt)
/*      */   {
/* 1197 */     if ((!tryAcquire(paramInt)) && (acquireQueued(addWaiter(Node.EXCLUSIVE), paramInt)))
/*      */     {
/* 1199 */       selfInterrupt();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void acquireInterruptibly(int paramInt)
/*      */     throws InterruptedException
/*      */   {
/* 1218 */     if (Thread.interrupted())
/* 1219 */       throw new InterruptedException();
/* 1220 */     if (!tryAcquire(paramInt))
/* 1221 */       doAcquireInterruptibly(paramInt);
/*      */   }
/*      */ 
/*      */   public final boolean tryAcquireNanos(int paramInt, long paramLong)
/*      */     throws InterruptedException
/*      */   {
/* 1243 */     if (Thread.interrupted())
/* 1244 */       throw new InterruptedException();
/* 1245 */     return (tryAcquire(paramInt)) || (doAcquireNanos(paramInt, paramLong));
/*      */   }
/*      */ 
/*      */   public final boolean release(int paramInt)
/*      */   {
/* 1260 */     if (tryRelease(paramInt)) {
/* 1261 */       Node localNode = this.head;
/* 1262 */       if ((localNode != null) && (localNode.waitStatus != 0))
/* 1263 */         unparkSuccessor(localNode);
/* 1264 */       return true;
/*      */     }
/* 1266 */     return false;
/*      */   }
/*      */ 
/*      */   public final void acquireShared(int paramInt)
/*      */   {
/* 1281 */     if (tryAcquireShared(paramInt) < 0)
/* 1282 */       doAcquireShared(paramInt);
/*      */   }
/*      */ 
/*      */   public final void acquireSharedInterruptibly(int paramInt)
/*      */     throws InterruptedException
/*      */   {
/* 1300 */     if (Thread.interrupted())
/* 1301 */       throw new InterruptedException();
/* 1302 */     if (tryAcquireShared(paramInt) < 0)
/* 1303 */       doAcquireSharedInterruptibly(paramInt);
/*      */   }
/*      */ 
/*      */   public final boolean tryAcquireSharedNanos(int paramInt, long paramLong)
/*      */     throws InterruptedException
/*      */   {
/* 1324 */     if (Thread.interrupted())
/* 1325 */       throw new InterruptedException();
/* 1326 */     return (tryAcquireShared(paramInt) >= 0) || (doAcquireSharedNanos(paramInt, paramLong));
/*      */   }
/*      */ 
/*      */   public final boolean releaseShared(int paramInt)
/*      */   {
/* 1340 */     if (tryReleaseShared(paramInt)) {
/* 1341 */       doReleaseShared();
/* 1342 */       return true;
/*      */     }
/* 1344 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean hasQueuedThreads()
/*      */   {
/* 1361 */     return this.head != this.tail;
/*      */   }
/*      */ 
/*      */   public final boolean hasContended()
/*      */   {
/* 1374 */     return this.head != null;
/*      */   }
/*      */ 
/*      */   public final Thread getFirstQueuedThread()
/*      */   {
/* 1390 */     return this.head == this.tail ? null : fullGetFirstQueuedThread();
/*      */   }
/*      */ 
/*      */   private Thread fullGetFirstQueuedThread()
/*      */   {
/*      */     Node localNode1;
/*      */     Node localNode2;
/*      */     Thread localThread1;
/* 1407 */     if ((((localNode1 = this.head) != null) && ((localNode2 = localNode1.next) != null) && (localNode2.prev == this.head) && ((localThread1 = localNode2.thread) != null)) || (((localNode1 = this.head) != null) && ((localNode2 = localNode1.next) != null) && (localNode2.prev == this.head) && ((localThread1 = localNode2.thread) != null)))
/*      */     {
/* 1411 */       return localThread1;
/*      */     }
/*      */ 
/* 1421 */     Node localNode3 = this.tail;
/* 1422 */     Object localObject = null;
/* 1423 */     while ((localNode3 != null) && (localNode3 != this.head)) {
/* 1424 */       Thread localThread2 = localNode3.thread;
/* 1425 */       if (localThread2 != null)
/* 1426 */         localObject = localThread2;
/* 1427 */       localNode3 = localNode3.prev;
/*      */     }
/* 1429 */     return localObject;
/*      */   }
/*      */ 
/*      */   public final boolean isQueued(Thread paramThread)
/*      */   {
/* 1443 */     if (paramThread == null)
/* 1444 */       throw new NullPointerException();
/* 1445 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev)
/* 1446 */       if (localNode.thread == paramThread)
/* 1447 */         return true;
/* 1448 */     return false;
/*      */   }
/*      */ 
/*      */   final boolean apparentlyFirstQueuedIsExclusive()
/*      */   {
/*      */     Node localNode1;
/*      */     Node localNode2;
/* 1462 */     return ((localNode1 = this.head) != null) && ((localNode2 = localNode1.next) != null) && (!localNode2.isShared()) && (localNode2.thread != null);
/*      */   }
/*      */ 
/*      */   public final boolean hasQueuedPredecessors()
/*      */   {
/* 1515 */     Node localNode1 = this.tail;
/* 1516 */     Node localNode2 = this.head;
/*      */     Node localNode3;
/* 1518 */     return (localNode2 != localNode1) && (((localNode3 = localNode2.next) == null) || (localNode3.thread != Thread.currentThread()));
/*      */   }
/*      */ 
/*      */   public final int getQueueLength()
/*      */   {
/* 1536 */     int i = 0;
/* 1537 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev) {
/* 1538 */       if (localNode.thread != null)
/* 1539 */         i++;
/*      */     }
/* 1541 */     return i;
/*      */   }
/*      */ 
/*      */   public final Collection<Thread> getQueuedThreads()
/*      */   {
/* 1556 */     ArrayList localArrayList = new ArrayList();
/* 1557 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev) {
/* 1558 */       Thread localThread = localNode.thread;
/* 1559 */       if (localThread != null)
/* 1560 */         localArrayList.add(localThread);
/*      */     }
/* 1562 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public final Collection<Thread> getExclusiveQueuedThreads()
/*      */   {
/* 1574 */     ArrayList localArrayList = new ArrayList();
/* 1575 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev) {
/* 1576 */       if (!localNode.isShared()) {
/* 1577 */         Thread localThread = localNode.thread;
/* 1578 */         if (localThread != null)
/* 1579 */           localArrayList.add(localThread);
/*      */       }
/*      */     }
/* 1582 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public final Collection<Thread> getSharedQueuedThreads()
/*      */   {
/* 1594 */     ArrayList localArrayList = new ArrayList();
/* 1595 */     for (Node localNode = this.tail; localNode != null; localNode = localNode.prev) {
/* 1596 */       if (localNode.isShared()) {
/* 1597 */         Thread localThread = localNode.thread;
/* 1598 */         if (localThread != null)
/* 1599 */           localArrayList.add(localThread);
/*      */       }
/*      */     }
/* 1602 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1615 */     int i = getState();
/* 1616 */     String str = hasQueuedThreads() ? "non" : "";
/* 1617 */     return super.toString() + "[State = " + i + ", " + str + "empty queue]";
/*      */   }
/*      */ 
/*      */   final boolean isOnSyncQueue(Node paramNode)
/*      */   {
/* 1631 */     if ((paramNode.waitStatus == -2) || (paramNode.prev == null))
/* 1632 */       return false;
/* 1633 */     if (paramNode.next != null) {
/* 1634 */       return true;
/*      */     }
/*      */ 
/* 1643 */     return findNodeFromTail(paramNode);
/*      */   }
/*      */ 
/*      */   private boolean findNodeFromTail(Node paramNode)
/*      */   {
/* 1652 */     Node localNode = this.tail;
/*      */     while (true) {
/* 1654 */       if (localNode == paramNode)
/* 1655 */         return true;
/* 1656 */       if (localNode == null)
/* 1657 */         return false;
/* 1658 */       localNode = localNode.prev;
/*      */     }
/*      */   }
/*      */ 
/*      */   final boolean transferForSignal(Node paramNode)
/*      */   {
/* 1673 */     if (!compareAndSetWaitStatus(paramNode, -2, 0)) {
/* 1674 */       return false;
/*      */     }
/*      */ 
/* 1682 */     Node localNode = enq(paramNode);
/* 1683 */     int i = localNode.waitStatus;
/* 1684 */     if ((i > 0) || (!compareAndSetWaitStatus(localNode, i, -1)))
/* 1685 */       LockSupport.unpark(paramNode.thread);
/* 1686 */     return true;
/*      */   }
/*      */ 
/*      */   final boolean transferAfterCancelledWait(Node paramNode)
/*      */   {
/* 1698 */     if (compareAndSetWaitStatus(paramNode, -2, 0)) {
/* 1699 */       enq(paramNode);
/* 1700 */       return true;
/*      */     }
/*      */ 
/* 1708 */     while (!isOnSyncQueue(paramNode))
/* 1709 */       Thread.yield();
/* 1710 */     return false;
/*      */   }
/*      */ 
/*      */   final int fullyRelease(Node paramNode)
/*      */   {
/* 1720 */     int i = 1;
/*      */     try {
/* 1722 */       int j = getState();
/* 1723 */       if (release(j)) {
/* 1724 */         i = 0;
/* 1725 */         return j;
/*      */       }
/* 1727 */       throw new IllegalMonitorStateException();
/*      */     }
/*      */     finally {
/* 1730 */       if (i != 0)
/* 1731 */         paramNode.waitStatus = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean owns(ConditionObject paramConditionObject)
/*      */   {
/* 1746 */     if (paramConditionObject == null)
/* 1747 */       throw new NullPointerException();
/* 1748 */     return paramConditionObject.isOwnedBy(this);
/*      */   }
/*      */ 
/*      */   public final boolean hasWaiters(ConditionObject paramConditionObject)
/*      */   {
/* 1768 */     if (!owns(paramConditionObject))
/* 1769 */       throw new IllegalArgumentException("Not owner");
/* 1770 */     return paramConditionObject.hasWaiters();
/*      */   }
/*      */ 
/*      */   public final int getWaitQueueLength(ConditionObject paramConditionObject)
/*      */   {
/* 1790 */     if (!owns(paramConditionObject))
/* 1791 */       throw new IllegalArgumentException("Not owner");
/* 1792 */     return paramConditionObject.getWaitQueueLength();
/*      */   }
/*      */ 
/*      */   public final Collection<Thread> getWaitingThreads(ConditionObject paramConditionObject)
/*      */   {
/* 1812 */     if (!owns(paramConditionObject))
/* 1813 */       throw new IllegalArgumentException("Not owner");
/* 1814 */     return paramConditionObject.getWaitingThreads();
/*      */   }
/*      */ 
/*      */   private final boolean compareAndSetHead(Node paramNode)
/*      */   {
/* 2302 */     return unsafe.compareAndSwapObject(this, headOffset, null, paramNode);
/*      */   }
/*      */ 
/*      */   private final boolean compareAndSetTail(Node paramNode1, Node paramNode2)
/*      */   {
/* 2309 */     return unsafe.compareAndSwapObject(this, tailOffset, paramNode1, paramNode2);
/*      */   }
/*      */ 
/*      */   private static final boolean compareAndSetWaitStatus(Node paramNode, int paramInt1, int paramInt2)
/*      */   {
/* 2318 */     return unsafe.compareAndSwapInt(paramNode, waitStatusOffset, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private static final boolean compareAndSetNext(Node paramNode1, Node paramNode2, Node paramNode3)
/*      */   {
/* 2328 */     return unsafe.compareAndSwapObject(paramNode1, nextOffset, paramNode2, paramNode3);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/* 2284 */       stateOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("state"));
/*      */ 
/* 2286 */       headOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("head"));
/*      */ 
/* 2288 */       tailOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
/*      */ 
/* 2290 */       waitStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
/*      */ 
/* 2292 */       nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("next"));
/*      */     }
/*      */     catch (Exception localException) {
/* 2295 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ConditionObject
/*      */     implements Condition, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1173984872572414699L;
/*      */     private transient AbstractQueuedSynchronizer.Node firstWaiter;
/*      */     private transient AbstractQueuedSynchronizer.Node lastWaiter;
/*      */     private static final int REINTERRUPT = 1;
/*      */     private static final int THROW_IE = -1;
/*      */ 
/*      */     public ConditionObject()
/*      */     {
/*      */     }
/*      */ 
/*      */     private AbstractQueuedSynchronizer.Node addConditionWaiter()
/*      */     {
/* 1851 */       AbstractQueuedSynchronizer.Node localNode1 = this.lastWaiter;
/*      */ 
/* 1853 */       if ((localNode1 != null) && (localNode1.waitStatus != -2)) {
/* 1854 */         unlinkCancelledWaiters();
/* 1855 */         localNode1 = this.lastWaiter;
/*      */       }
/* 1857 */       AbstractQueuedSynchronizer.Node localNode2 = new AbstractQueuedSynchronizer.Node(Thread.currentThread(), -2);
/* 1858 */       if (localNode1 == null)
/* 1859 */         this.firstWaiter = localNode2;
/*      */       else
/* 1861 */         localNode1.nextWaiter = localNode2;
/* 1862 */       this.lastWaiter = localNode2;
/* 1863 */       return localNode2;
/*      */     }
/*      */ 
/*      */     private void doSignal(AbstractQueuedSynchronizer.Node paramNode)
/*      */     {
/*      */       do
/*      */       {
/* 1874 */         if ((this.firstWaiter = paramNode.nextWaiter) == null)
/* 1875 */           this.lastWaiter = null;
/* 1876 */         paramNode.nextWaiter = null;
/* 1877 */       }while ((!AbstractQueuedSynchronizer.this.transferForSignal(paramNode)) && ((paramNode = this.firstWaiter) != null));
/*      */     }
/*      */ 
/*      */     private void doSignalAll(AbstractQueuedSynchronizer.Node paramNode)
/*      */     {
/* 1886 */       this.lastWaiter = (this.firstWaiter = null);
/*      */       do {
/* 1888 */         AbstractQueuedSynchronizer.Node localNode = paramNode.nextWaiter;
/* 1889 */         paramNode.nextWaiter = null;
/* 1890 */         AbstractQueuedSynchronizer.this.transferForSignal(paramNode);
/* 1891 */         paramNode = localNode;
/* 1892 */       }while (paramNode != null);
/*      */     }
/*      */ 
/*      */     private void unlinkCancelledWaiters()
/*      */     {
/* 1910 */       Object localObject1 = this.firstWaiter;
/* 1911 */       Object localObject2 = null;
/* 1912 */       while (localObject1 != null) {
/* 1913 */         AbstractQueuedSynchronizer.Node localNode = ((AbstractQueuedSynchronizer.Node)localObject1).nextWaiter;
/* 1914 */         if (((AbstractQueuedSynchronizer.Node)localObject1).waitStatus != -2) {
/* 1915 */           ((AbstractQueuedSynchronizer.Node)localObject1).nextWaiter = null;
/* 1916 */           if (localObject2 == null)
/* 1917 */             this.firstWaiter = localNode;
/*      */           else
/* 1919 */             localObject2.nextWaiter = localNode;
/* 1920 */           if (localNode == null)
/* 1921 */             this.lastWaiter = localObject2;
/*      */         }
/*      */         else {
/* 1924 */           localObject2 = localObject1;
/* 1925 */         }localObject1 = localNode;
/*      */       }
/*      */     }
/*      */ 
/*      */     public final void signal()
/*      */     {
/* 1940 */       if (!AbstractQueuedSynchronizer.this.isHeldExclusively())
/* 1941 */         throw new IllegalMonitorStateException();
/* 1942 */       AbstractQueuedSynchronizer.Node localNode = this.firstWaiter;
/* 1943 */       if (localNode != null)
/* 1944 */         doSignal(localNode);
/*      */     }
/*      */ 
/*      */     public final void signalAll()
/*      */     {
/* 1955 */       if (!AbstractQueuedSynchronizer.this.isHeldExclusively())
/* 1956 */         throw new IllegalMonitorStateException();
/* 1957 */       AbstractQueuedSynchronizer.Node localNode = this.firstWaiter;
/* 1958 */       if (localNode != null)
/* 1959 */         doSignalAll(localNode);
/*      */     }
/*      */ 
/*      */     public final void awaitUninterruptibly()
/*      */     {
/* 1975 */       AbstractQueuedSynchronizer.Node localNode = addConditionWaiter();
/* 1976 */       int i = AbstractQueuedSynchronizer.this.fullyRelease(localNode);
/* 1977 */       int j = 0;
/* 1978 */       while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(localNode)) {
/* 1979 */         LockSupport.park(this);
/* 1980 */         if (Thread.interrupted())
/* 1981 */           j = 1;
/*      */       }
/* 1983 */       if ((AbstractQueuedSynchronizer.this.acquireQueued(localNode, i)) || (j != 0))
/* 1984 */         AbstractQueuedSynchronizer.access$000();
/*      */     }
/*      */ 
/*      */     private int checkInterruptWhileWaiting(AbstractQueuedSynchronizer.Node paramNode)
/*      */     {
/* 2005 */       return Thread.interrupted() ? 1 : AbstractQueuedSynchronizer.this.transferAfterCancelledWait(paramNode) ? -1 : 0;
/*      */     }
/*      */ 
/*      */     private void reportInterruptAfterWait(int paramInt)
/*      */       throws InterruptedException
/*      */     {
/* 2016 */       if (paramInt == -1)
/* 2017 */         throw new InterruptedException();
/* 2018 */       if (paramInt == 1)
/* 2019 */         AbstractQueuedSynchronizer.access$000();
/*      */     }
/*      */ 
/*      */     public final void await()
/*      */       throws InterruptedException
/*      */     {
/* 2037 */       if (Thread.interrupted())
/* 2038 */         throw new InterruptedException();
/* 2039 */       AbstractQueuedSynchronizer.Node localNode = addConditionWaiter();
/* 2040 */       int i = AbstractQueuedSynchronizer.this.fullyRelease(localNode);
/* 2041 */       int j = 0;
/* 2042 */       while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(localNode)) {
/* 2043 */         LockSupport.park(this);
/* 2044 */         if ((j = checkInterruptWhileWaiting(localNode)) != 0)
/* 2045 */           break;
/*      */       }
/* 2047 */       if ((AbstractQueuedSynchronizer.this.acquireQueued(localNode, i)) && (j != -1))
/* 2048 */         j = 1;
/* 2049 */       if (localNode.nextWaiter != null)
/* 2050 */         unlinkCancelledWaiters();
/* 2051 */       if (j != 0)
/* 2052 */         reportInterruptAfterWait(j);
/*      */     }
/*      */ 
/*      */     public final long awaitNanos(long paramLong)
/*      */       throws InterruptedException
/*      */     {
/* 2071 */       if (Thread.interrupted())
/* 2072 */         throw new InterruptedException();
/* 2073 */       AbstractQueuedSynchronizer.Node localNode = addConditionWaiter();
/* 2074 */       int i = AbstractQueuedSynchronizer.this.fullyRelease(localNode);
/* 2075 */       long l1 = System.nanoTime();
/* 2076 */       int j = 0;
/* 2077 */       while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(localNode)) {
/* 2078 */         if (paramLong <= 0L) {
/* 2079 */           AbstractQueuedSynchronizer.this.transferAfterCancelledWait(localNode);
/* 2080 */           break;
/*      */         }
/* 2082 */         LockSupport.parkNanos(this, paramLong);
/* 2083 */         if ((j = checkInterruptWhileWaiting(localNode)) != 0) {
/*      */           break;
/*      */         }
/* 2086 */         long l2 = System.nanoTime();
/* 2087 */         paramLong -= l2 - l1;
/* 2088 */         l1 = l2;
/*      */       }
/* 2090 */       if ((AbstractQueuedSynchronizer.this.acquireQueued(localNode, i)) && (j != -1))
/* 2091 */         j = 1;
/* 2092 */       if (localNode.nextWaiter != null)
/* 2093 */         unlinkCancelledWaiters();
/* 2094 */       if (j != 0)
/* 2095 */         reportInterruptAfterWait(j);
/* 2096 */       return paramLong - (System.nanoTime() - l1);
/*      */     }
/*      */ 
/*      */     public final boolean awaitUntil(Date paramDate)
/*      */       throws InterruptedException
/*      */     {
/* 2116 */       if (paramDate == null)
/* 2117 */         throw new NullPointerException();
/* 2118 */       long l = paramDate.getTime();
/* 2119 */       if (Thread.interrupted())
/* 2120 */         throw new InterruptedException();
/* 2121 */       AbstractQueuedSynchronizer.Node localNode = addConditionWaiter();
/* 2122 */       int i = AbstractQueuedSynchronizer.this.fullyRelease(localNode);
/* 2123 */       boolean bool = false;
/* 2124 */       int j = 0;
/* 2125 */       while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(localNode))
/* 2126 */         if (System.currentTimeMillis() > l) {
/* 2127 */           bool = AbstractQueuedSynchronizer.this.transferAfterCancelledWait(localNode);
/*      */         }
/*      */         else {
/* 2130 */           LockSupport.parkUntil(this, l);
/* 2131 */           if ((j = checkInterruptWhileWaiting(localNode)) != 0)
/* 2132 */             break;
/*      */         }
/* 2134 */       if ((AbstractQueuedSynchronizer.this.acquireQueued(localNode, i)) && (j != -1))
/* 2135 */         j = 1;
/* 2136 */       if (localNode.nextWaiter != null)
/* 2137 */         unlinkCancelledWaiters();
/* 2138 */       if (j != 0)
/* 2139 */         reportInterruptAfterWait(j);
/* 2140 */       return !bool;
/*      */     }
/*      */ 
/*      */     public final boolean await(long paramLong, TimeUnit paramTimeUnit)
/*      */       throws InterruptedException
/*      */     {
/* 2160 */       if (paramTimeUnit == null)
/* 2161 */         throw new NullPointerException();
/* 2162 */       long l1 = paramTimeUnit.toNanos(paramLong);
/* 2163 */       if (Thread.interrupted())
/* 2164 */         throw new InterruptedException();
/* 2165 */       AbstractQueuedSynchronizer.Node localNode = addConditionWaiter();
/* 2166 */       int i = AbstractQueuedSynchronizer.this.fullyRelease(localNode);
/* 2167 */       long l2 = System.nanoTime();
/* 2168 */       boolean bool = false;
/* 2169 */       int j = 0;
/* 2170 */       while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(localNode)) {
/* 2171 */         if (l1 <= 0L) {
/* 2172 */           bool = AbstractQueuedSynchronizer.this.transferAfterCancelledWait(localNode);
/* 2173 */           break;
/*      */         }
/* 2175 */         if (l1 >= 1000L)
/* 2176 */           LockSupport.parkNanos(this, l1);
/* 2177 */         if ((j = checkInterruptWhileWaiting(localNode)) != 0)
/*      */           break;
/* 2179 */         long l3 = System.nanoTime();
/* 2180 */         l1 -= l3 - l2;
/* 2181 */         l2 = l3;
/*      */       }
/* 2183 */       if ((AbstractQueuedSynchronizer.this.acquireQueued(localNode, i)) && (j != -1))
/* 2184 */         j = 1;
/* 2185 */       if (localNode.nextWaiter != null)
/* 2186 */         unlinkCancelledWaiters();
/* 2187 */       if (j != 0)
/* 2188 */         reportInterruptAfterWait(j);
/* 2189 */       return !bool;
/*      */     }
/*      */ 
/*      */     final boolean isOwnedBy(AbstractQueuedSynchronizer paramAbstractQueuedSynchronizer)
/*      */     {
/* 2201 */       return paramAbstractQueuedSynchronizer == AbstractQueuedSynchronizer.this;
/*      */     }
/*      */ 
/*      */     protected final boolean hasWaiters()
/*      */     {
/* 2213 */       if (!AbstractQueuedSynchronizer.this.isHeldExclusively())
/* 2214 */         throw new IllegalMonitorStateException();
/* 2215 */       for (AbstractQueuedSynchronizer.Node localNode = this.firstWaiter; localNode != null; localNode = localNode.nextWaiter) {
/* 2216 */         if (localNode.waitStatus == -2)
/* 2217 */           return true;
/*      */       }
/* 2219 */       return false;
/*      */     }
/*      */ 
/*      */     protected final int getWaitQueueLength()
/*      */     {
/* 2232 */       if (!AbstractQueuedSynchronizer.this.isHeldExclusively())
/* 2233 */         throw new IllegalMonitorStateException();
/* 2234 */       int i = 0;
/* 2235 */       for (AbstractQueuedSynchronizer.Node localNode = this.firstWaiter; localNode != null; localNode = localNode.nextWaiter) {
/* 2236 */         if (localNode.waitStatus == -2)
/* 2237 */           i++;
/*      */       }
/* 2239 */       return i;
/*      */     }
/*      */ 
/*      */     protected final Collection<Thread> getWaitingThreads()
/*      */     {
/* 2252 */       if (!AbstractQueuedSynchronizer.this.isHeldExclusively())
/* 2253 */         throw new IllegalMonitorStateException();
/* 2254 */       ArrayList localArrayList = new ArrayList();
/* 2255 */       for (AbstractQueuedSynchronizer.Node localNode = this.firstWaiter; localNode != null; localNode = localNode.nextWaiter) {
/* 2256 */         if (localNode.waitStatus == -2) {
/* 2257 */           Thread localThread = localNode.thread;
/* 2258 */           if (localThread != null)
/* 2259 */             localArrayList.add(localThread);
/*      */         }
/*      */       }
/* 2262 */       return localArrayList;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Node
/*      */   {
/*  382 */     static final Node SHARED = new Node();
/*      */ 
/*  384 */     static final Node EXCLUSIVE = null;
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
/*  484 */       return this.nextWaiter == SHARED;
/*      */     }
/*      */ 
/*      */     final Node predecessor()
/*      */       throws NullPointerException
/*      */     {
/*  495 */       Node localNode = this.prev;
/*  496 */       if (localNode == null) {
/*  497 */         throw new NullPointerException();
/*      */       }
/*  499 */       return localNode;
/*      */     }
/*      */ 
/*      */     Node() {
/*      */     }
/*      */ 
/*      */     Node(Thread paramThread, Node paramNode) {
/*  506 */       this.nextWaiter = paramNode;
/*  507 */       this.thread = paramThread;
/*      */     }
/*      */ 
/*      */     Node(Thread paramThread, int paramInt) {
/*  511 */       this.waitStatus = paramInt;
/*  512 */       this.thread = paramThread;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.AbstractQueuedSynchronizer
 * JD-Core Version:    0.6.2
 */