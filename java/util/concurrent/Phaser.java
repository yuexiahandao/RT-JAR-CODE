/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public class Phaser
/*      */ {
/*      */   private volatile long state;
/*      */   private static final int MAX_PARTIES = 65535;
/*      */   private static final int MAX_PHASE = 2147483647;
/*      */   private static final int PARTIES_SHIFT = 16;
/*      */   private static final int PHASE_SHIFT = 32;
/*      */   private static final int UNARRIVED_MASK = 65535;
/*      */   private static final long PARTIES_MASK = 4294901760L;
/*      */   private static final long TERMINATION_BIT = -9223372036854775808L;
/*      */   private static final int ONE_ARRIVAL = 1;
/*      */   private static final int ONE_PARTY = 65536;
/*      */   private static final int EMPTY = 1;
/*      */   private final Phaser parent;
/*      */   private final Phaser root;
/*      */   private final AtomicReference<QNode> evenQ;
/*      */   private final AtomicReference<QNode> oddQ;
/* 1002 */   private static final int NCPU = Runtime.getRuntime().availableProcessors();
/*      */ 
/* 1015 */   static final int SPINS_PER_ARRIVAL = NCPU < 2 ? 1 : 256;
/*      */   private static final Unsafe UNSAFE;
/*      */   private static final long stateOffset;
/*      */ 
/*      */   private static int unarrivedOf(long paramLong)
/*      */   {
/*  313 */     int i = (int)paramLong;
/*  314 */     return i == 1 ? 0 : i & 0xFFFF;
/*      */   }
/*      */ 
/*      */   private static int partiesOf(long paramLong) {
/*  318 */     return (int)paramLong >>> 16;
/*      */   }
/*      */ 
/*      */   private static int phaseOf(long paramLong) {
/*  322 */     return (int)(paramLong >>> 32);
/*      */   }
/*      */ 
/*      */   private static int arrivedOf(long paramLong) {
/*  326 */     int i = (int)paramLong;
/*  327 */     return i == 1 ? 0 : (i >>> 16) - (i & 0xFFFF);
/*      */   }
/*      */ 
/*      */   private AtomicReference<QNode> queueFor(int paramInt)
/*      */   {
/*  351 */     return (paramInt & 0x1) == 0 ? this.evenQ : this.oddQ;
/*      */   }
/*      */ 
/*      */   private String badArrive(long paramLong)
/*      */   {
/*  358 */     return "Attempted arrival of unregistered party for " + stateToString(paramLong);
/*      */   }
/*      */ 
/*      */   private String badRegister(long paramLong)
/*      */   {
/*  366 */     return "Attempt to register more than 65535 parties for " + stateToString(paramLong);
/*      */   }
/*      */ 
/*      */   private int doArrive(boolean paramBoolean)
/*      */   {
/*  378 */     int i = paramBoolean ? 65537 : 1;
/*  379 */     Phaser localPhaser = this.root;
/*      */     while (true) {
/*  381 */       long l1 = localPhaser == this ? this.state : reconcileState();
/*  382 */       int j = (int)(l1 >>> 32);
/*  383 */       int k = (int)l1;
/*  384 */       int m = (k & 0xFFFF) - 1;
/*  385 */       if (j < 0)
/*  386 */         return j;
/*  387 */       if ((k == 1) || (m < 0)) {
/*  388 */         if ((localPhaser == this) || (reconcileState() == l1))
/*  389 */           throw new IllegalStateException(badArrive(l1));
/*      */       }
/*  391 */       else if (UNSAFE.compareAndSwapLong(this, stateOffset, l1, l1 -= i)) {
/*  392 */         if (m == 0) {
/*  393 */           long l2 = l1 & 0xFFFF0000;
/*  394 */           int n = (int)l2 >>> 16;
/*  395 */           if (localPhaser != this)
/*  396 */             return this.parent.doArrive(n == 0);
/*  397 */           if (onAdvance(j, n))
/*  398 */             l2 |= -9223372036854775808L;
/*  399 */           else if (n == 0)
/*  400 */             l2 |= 1L;
/*      */           else
/*  402 */             l2 |= n;
/*  403 */           l2 |= (j + 1 & 0x7FFFFFFF) << 32;
/*  404 */           UNSAFE.compareAndSwapLong(this, stateOffset, l1, l2);
/*  405 */           releaseWaiters(j);
/*      */         }
/*  407 */         return j;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int doRegister(int paramInt)
/*      */   {
/*  420 */     long l1 = paramInt << 16 | paramInt;
/*  421 */     Phaser localPhaser = this.parent;
/*      */     int i;
/*      */     while (true)
/*      */     {
/*  424 */       long l2 = this.state;
/*  425 */       int j = (int)l2;
/*  426 */       int k = j >>> 16;
/*  427 */       int m = j & 0xFFFF;
/*  428 */       if (paramInt > 65535 - k)
/*  429 */         throw new IllegalStateException(badRegister(l2));
/*  430 */       if ((i = (int)(l2 >>> 32)) < 0)
/*      */         break;
/*  432 */       if (j != 1) {
/*  433 */         if ((localPhaser == null) || (reconcileState() == l2))
/*  434 */           if (m == 0)
/*  435 */             this.root.internalAwaitAdvance(i, null);
/*  436 */           else if (UNSAFE.compareAndSwapLong(this, stateOffset, l2, l2 + l1))
/*      */           {
/*  438 */             break;
/*      */           }
/*      */       }
/*  441 */       else if (localPhaser == null) {
/*  442 */         long l3 = i << 32 | l1;
/*  443 */         if (UNSAFE.compareAndSwapLong(this, stateOffset, l2, l3))
/*      */           break;
/*      */       }
/*      */       else {
/*  447 */         synchronized (this) {
/*  448 */           if (this.state == l2) {
/*  449 */             localPhaser.doRegister(1);
/*      */             do {
/*  451 */               i = (int)(this.root.state >>> 32);
/*      */             }
/*  453 */             while (!UNSAFE.compareAndSwapLong(this, stateOffset, this.state, i << 32 | l1));
/*      */ 
/*  456 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  461 */     return i;
/*      */   }
/*      */ 
/*      */   private long reconcileState()
/*      */   {
/*  478 */     Phaser localPhaser = this.root;
/*  479 */     long l = this.state;
/*  480 */     if (localPhaser != this)
/*      */     {
/*      */       int i;
/*  484 */       while ((i = (int)(localPhaser.state >>> 32)) != (int)(l >>> 32))
/*      */       {
/*      */         int k;
/*      */         int j;
/*  484 */         if (UNSAFE.compareAndSwapLong(this, stateOffset, l, l = i << 32 | l & 0xFFFF0000 | ((j = (int)l & 0xFFFF) == 0 ? k : (k = (int)l >>> 16) == 0 ? 1 : j)))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  491 */         l = this.state;
/*      */       }
/*      */     }
/*  493 */     return l;
/*      */   }
/*      */ 
/*      */   public Phaser()
/*      */   {
/*  502 */     this(null, 0);
/*      */   }
/*      */ 
/*      */   public Phaser(int paramInt)
/*      */   {
/*  515 */     this(null, paramInt);
/*      */   }
/*      */ 
/*      */   public Phaser(Phaser paramPhaser)
/*      */   {
/*  524 */     this(paramPhaser, 0);
/*      */   }
/*      */ 
/*      */   public Phaser(Phaser paramPhaser, int paramInt)
/*      */   {
/*  540 */     if (paramInt >>> 16 != 0)
/*  541 */       throw new IllegalArgumentException("Illegal number of parties");
/*  542 */     int i = 0;
/*  543 */     this.parent = paramPhaser;
/*  544 */     if (paramPhaser != null) {
/*  545 */       Phaser localPhaser = paramPhaser.root;
/*  546 */       this.root = localPhaser;
/*  547 */       this.evenQ = localPhaser.evenQ;
/*  548 */       this.oddQ = localPhaser.oddQ;
/*  549 */       if (paramInt != 0)
/*  550 */         i = paramPhaser.doRegister(1);
/*      */     }
/*      */     else {
/*  553 */       this.root = this;
/*  554 */       this.evenQ = new AtomicReference();
/*  555 */       this.oddQ = new AtomicReference();
/*      */     }
/*  557 */     this.state = (paramInt == 0 ? 1L : i << 32 | paramInt << 16 | paramInt);
/*      */   }
/*      */ 
/*      */   public int register()
/*      */   {
/*  579 */     return doRegister(1);
/*      */   }
/*      */ 
/*      */   public int bulkRegister(int paramInt)
/*      */   {
/*  602 */     if (paramInt < 0)
/*  603 */       throw new IllegalArgumentException();
/*  604 */     if (paramInt == 0)
/*  605 */       return getPhase();
/*  606 */     return doRegister(paramInt);
/*      */   }
/*      */ 
/*      */   public int arrive()
/*      */   {
/*  622 */     return doArrive(false);
/*      */   }
/*      */ 
/*      */   public int arriveAndDeregister()
/*      */   {
/*  642 */     return doArrive(true);
/*      */   }
/*      */ 
/*      */   public int arriveAndAwaitAdvance()
/*      */   {
/*  665 */     Phaser localPhaser = this.root;
/*      */     while (true) {
/*  667 */       long l1 = localPhaser == this ? this.state : reconcileState();
/*  668 */       int i = (int)(l1 >>> 32);
/*  669 */       int j = (int)l1;
/*  670 */       int k = (j & 0xFFFF) - 1;
/*  671 */       if (i < 0)
/*  672 */         return i;
/*  673 */       if ((j == 1) || (k < 0)) {
/*  674 */         if (reconcileState() == l1)
/*  675 */           throw new IllegalStateException(badArrive(l1));
/*      */       }
/*  677 */       else if (UNSAFE.compareAndSwapLong(this, stateOffset, l1, --l1))
/*      */       {
/*  679 */         if (k != 0)
/*  680 */           return localPhaser.internalAwaitAdvance(i, null);
/*  681 */         if (localPhaser != this)
/*  682 */           return this.parent.arriveAndAwaitAdvance();
/*  683 */         long l2 = l1 & 0xFFFF0000;
/*  684 */         int m = (int)l2 >>> 16;
/*  685 */         if (onAdvance(i, m))
/*  686 */           l2 |= -9223372036854775808L;
/*  687 */         else if (m == 0)
/*  688 */           l2 |= 1L;
/*      */         else
/*  690 */           l2 |= m;
/*  691 */         int n = i + 1 & 0x7FFFFFFF;
/*  692 */         l2 |= n << 32;
/*  693 */         if (!UNSAFE.compareAndSwapLong(this, stateOffset, l1, l2))
/*  694 */           return (int)(this.state >>> 32);
/*  695 */         releaseWaiters(i);
/*  696 */         return n;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int awaitAdvance(int paramInt)
/*      */   {
/*  714 */     Phaser localPhaser = this.root;
/*  715 */     long l = localPhaser == this ? this.state : reconcileState();
/*  716 */     int i = (int)(l >>> 32);
/*  717 */     if (paramInt < 0)
/*  718 */       return paramInt;
/*  719 */     if (i == paramInt)
/*  720 */       return localPhaser.internalAwaitAdvance(paramInt, null);
/*  721 */     return i;
/*      */   }
/*      */ 
/*      */   public int awaitAdvanceInterruptibly(int paramInt)
/*      */     throws InterruptedException
/*      */   {
/*  741 */     Phaser localPhaser = this.root;
/*  742 */     long l = localPhaser == this ? this.state : reconcileState();
/*  743 */     int i = (int)(l >>> 32);
/*  744 */     if (paramInt < 0)
/*  745 */       return paramInt;
/*  746 */     if (i == paramInt) {
/*  747 */       QNode localQNode = new QNode(this, paramInt, true, false, 0L);
/*  748 */       i = localPhaser.internalAwaitAdvance(paramInt, localQNode);
/*  749 */       if (localQNode.wasInterrupted)
/*  750 */         throw new InterruptedException();
/*      */     }
/*  752 */     return i;
/*      */   }
/*      */ 
/*      */   public int awaitAdvanceInterruptibly(int paramInt, long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException, TimeoutException
/*      */   {
/*  778 */     long l1 = paramTimeUnit.toNanos(paramLong);
/*  779 */     Phaser localPhaser = this.root;
/*  780 */     long l2 = localPhaser == this ? this.state : reconcileState();
/*  781 */     int i = (int)(l2 >>> 32);
/*  782 */     if (paramInt < 0)
/*  783 */       return paramInt;
/*  784 */     if (i == paramInt) {
/*  785 */       QNode localQNode = new QNode(this, paramInt, true, true, l1);
/*  786 */       i = localPhaser.internalAwaitAdvance(paramInt, localQNode);
/*  787 */       if (localQNode.wasInterrupted)
/*  788 */         throw new InterruptedException();
/*  789 */       if (i == paramInt)
/*  790 */         throw new TimeoutException();
/*      */     }
/*  792 */     return i;
/*      */   }
/*      */ 
/*      */   public void forceTermination()
/*      */   {
/*  806 */     Phaser localPhaser = this.root;
/*      */     long l;
/*  808 */     while ((l = localPhaser.state) >= 0L)
/*  809 */       if (UNSAFE.compareAndSwapLong(localPhaser, stateOffset, l, l | 0x0))
/*      */       {
/*  812 */         releaseWaiters(0);
/*  813 */         releaseWaiters(1);
/*  814 */         return;
/*      */       }
/*      */   }
/*      */ 
/*      */   public final int getPhase()
/*      */   {
/*  829 */     return (int)(this.root.state >>> 32);
/*      */   }
/*      */ 
/*      */   public int getRegisteredParties()
/*      */   {
/*  838 */     return partiesOf(this.state);
/*      */   }
/*      */ 
/*      */   public int getArrivedParties()
/*      */   {
/*  849 */     return arrivedOf(reconcileState());
/*      */   }
/*      */ 
/*      */   public int getUnarrivedParties()
/*      */   {
/*  860 */     return unarrivedOf(reconcileState());
/*      */   }
/*      */ 
/*      */   public Phaser getParent()
/*      */   {
/*  869 */     return this.parent;
/*      */   }
/*      */ 
/*      */   public Phaser getRoot()
/*      */   {
/*  879 */     return this.root;
/*      */   }
/*      */ 
/*      */   public boolean isTerminated()
/*      */   {
/*  888 */     return this.root.state < 0L;
/*      */   }
/*      */ 
/*      */   protected boolean onAdvance(int paramInt1, int paramInt2)
/*      */   {
/*  932 */     return paramInt2 == 0;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  945 */     return stateToString(reconcileState());
/*      */   }
/*      */ 
/*      */   private String stateToString(long paramLong)
/*      */   {
/*  952 */     return super.toString() + "[phase = " + phaseOf(paramLong) + " parties = " + partiesOf(paramLong) + " arrived = " + arrivedOf(paramLong) + "]";
/*      */   }
/*      */ 
/*      */   private void releaseWaiters(int paramInt)
/*      */   {
/*  966 */     AtomicReference localAtomicReference = (paramInt & 0x1) == 0 ? this.evenQ : this.oddQ;
/*      */     QNode localQNode;
/*  967 */     while (((localQNode = (QNode)localAtomicReference.get()) != null) && (localQNode.phase != (int)(this.root.state >>> 32)))
/*      */     {
/*      */       Thread localThread;
/*  969 */       if ((localAtomicReference.compareAndSet(localQNode, localQNode.next)) && ((localThread = localQNode.thread) != null))
/*      */       {
/*  971 */         localQNode.thread = null;
/*  972 */         LockSupport.unpark(localThread);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int abortWait(int paramInt)
/*      */   {
/*  987 */     AtomicReference localAtomicReference = (paramInt & 0x1) == 0 ? this.evenQ : this.oddQ;
/*      */     while (true)
/*      */     {
/*  990 */       QNode localQNode = (QNode)localAtomicReference.get();
/*  991 */       int i = (int)(this.root.state >>> 32);
/*      */       Thread localThread;
/*  992 */       if ((localQNode == null) || (((localThread = localQNode.thread) != null) && (localQNode.phase == i)))
/*  993 */         return i;
/*  994 */       if ((localAtomicReference.compareAndSet(localQNode, localQNode.next)) && (localThread != null)) {
/*  995 */         localQNode.thread = null;
/*  996 */         LockSupport.unpark(localThread);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int internalAwaitAdvance(int paramInt, QNode paramQNode)
/*      */   {
/* 1027 */     releaseWaiters(paramInt - 1);
/* 1028 */     boolean bool1 = false;
/* 1029 */     int i = 0;
/* 1030 */     int j = SPINS_PER_ARRIVAL;
/*      */     long l;
/*      */     int k;
/* 1033 */     while ((k = (int)((l = this.state) >>> 32)) == paramInt) {
/* 1034 */       if (paramQNode == null) {
/* 1035 */         int m = (int)l & 0xFFFF;
/* 1036 */         if ((m != i) && ((i = m) < NCPU))
/*      */         {
/* 1038 */           j += SPINS_PER_ARRIVAL;
/* 1039 */         }boolean bool2 = Thread.interrupted();
/* 1040 */         if (!bool2) { j--; if (j >= 0); } else { paramQNode = new QNode(this, paramInt, false, false, 0L);
/* 1042 */           paramQNode.wasInterrupted = bool2; }
/*      */       }
/*      */       else {
/* 1045 */         if (paramQNode.isReleasable())
/*      */           break;
/* 1047 */         if (!bool1) {
/* 1048 */           AtomicReference localAtomicReference = (paramInt & 0x1) == 0 ? this.evenQ : this.oddQ;
/* 1049 */           QNode localQNode = paramQNode.next = (QNode)localAtomicReference.get();
/* 1050 */           if (((localQNode == null) || (localQNode.phase == paramInt)) && ((int)(this.state >>> 32) == paramInt))
/*      */           {
/* 1052 */             bool1 = localAtomicReference.compareAndSet(localQNode, paramQNode);
/*      */           }
/*      */         } else {
/*      */           try {
/* 1056 */             ForkJoinPool.managedBlock(paramQNode);
/*      */           } catch (InterruptedException localInterruptedException) {
/* 1058 */             paramQNode.wasInterrupted = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1063 */     if (paramQNode != null) {
/* 1064 */       if (paramQNode.thread != null)
/* 1065 */         paramQNode.thread = null;
/* 1066 */       if ((paramQNode.wasInterrupted) && (!paramQNode.interruptible))
/* 1067 */         Thread.currentThread().interrupt();
/* 1068 */       if ((k == paramInt) && ((k = (int)(this.state >>> 32)) == paramInt))
/* 1069 */         return abortWait(paramInt);
/*      */     }
/* 1071 */     releaseWaiters(paramInt);
/* 1072 */     return k;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/* 1144 */       UNSAFE = Unsafe.getUnsafe();
/* 1145 */       Phaser localPhaser = Phaser.class;
/* 1146 */       stateOffset = UNSAFE.objectFieldOffset(localPhaser.getDeclaredField("state"));
/*      */     }
/*      */     catch (Exception localException) {
/* 1149 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class QNode
/*      */     implements ForkJoinPool.ManagedBlocker
/*      */   {
/*      */     final Phaser phaser;
/*      */     final int phase;
/*      */     final boolean interruptible;
/*      */     final boolean timed;
/*      */     boolean wasInterrupted;
/*      */     long nanos;
/*      */     long lastTime;
/*      */     volatile Thread thread;
/*      */     QNode next;
/*      */ 
/*      */     QNode(Phaser paramPhaser, int paramInt, boolean paramBoolean1, boolean paramBoolean2, long paramLong)
/*      */     {
/* 1091 */       this.phaser = paramPhaser;
/* 1092 */       this.phase = paramInt;
/* 1093 */       this.interruptible = paramBoolean1;
/* 1094 */       this.nanos = paramLong;
/* 1095 */       this.timed = paramBoolean2;
/* 1096 */       this.lastTime = (paramBoolean2 ? System.nanoTime() : 0L);
/* 1097 */       this.thread = Thread.currentThread();
/*      */     }
/*      */ 
/*      */     public boolean isReleasable() {
/* 1101 */       if (this.thread == null)
/* 1102 */         return true;
/* 1103 */       if (this.phaser.getPhase() != this.phase) {
/* 1104 */         this.thread = null;
/* 1105 */         return true;
/*      */       }
/* 1107 */       if (Thread.interrupted())
/* 1108 */         this.wasInterrupted = true;
/* 1109 */       if ((this.wasInterrupted) && (this.interruptible)) {
/* 1110 */         this.thread = null;
/* 1111 */         return true;
/*      */       }
/* 1113 */       if (this.timed) {
/* 1114 */         if (this.nanos > 0L) {
/* 1115 */           long l = System.nanoTime();
/* 1116 */           this.nanos -= l - this.lastTime;
/* 1117 */           this.lastTime = l;
/*      */         }
/* 1119 */         if (this.nanos <= 0L) {
/* 1120 */           this.thread = null;
/* 1121 */           return true;
/*      */         }
/*      */       }
/* 1124 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean block() {
/* 1128 */       if (isReleasable())
/* 1129 */         return true;
/* 1130 */       if (!this.timed)
/* 1131 */         LockSupport.park(this);
/* 1132 */       else if (this.nanos > 0L)
/* 1133 */         LockSupport.parkNanos(this, this.nanos);
/* 1134 */       return isReleasable();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.Phaser
 * JD-Core Version:    0.6.2
 */