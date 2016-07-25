/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public class SynchronousQueue<E> extends AbstractQueue<E>
/*      */   implements BlockingQueue<E>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -3223113410248163686L;
/*  184 */   static final int NCPUS = Runtime.getRuntime().availableProcessors();
/*      */ 
/*  193 */   static final int maxTimedSpins = NCPUS < 2 ? 0 : 32;
/*      */ 
/*  200 */   static final int maxUntimedSpins = maxTimedSpins * 16;
/*      */   static final long spinForTimeoutThreshold = 1000L;
/*      */   private volatile transient Transferer transferer;
/*      */   private ReentrantLock qlock;
/*      */   private WaitQueue waitingProducers;
/*      */   private WaitQueue waitingConsumers;
/*      */ 
/*      */   public SynchronousQueue()
/*      */   {
/*  856 */     this(false);
/*      */   }
/*      */ 
/*      */   public SynchronousQueue(boolean paramBoolean)
/*      */   {
/*  866 */     this.transferer = (paramBoolean ? new TransferQueue() : new TransferStack());
/*      */   }
/*      */ 
/*      */   public void put(E paramE)
/*      */     throws InterruptedException
/*      */   {
/*  877 */     if (paramE == null) throw new NullPointerException();
/*  878 */     if (this.transferer.transfer(paramE, false, 0L) == null) {
/*  879 */       Thread.interrupted();
/*  880 */       throw new InterruptedException();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException
/*      */   {
/*  895 */     if (paramE == null) throw new NullPointerException();
/*  896 */     if (this.transferer.transfer(paramE, true, paramTimeUnit.toNanos(paramLong)) != null)
/*  897 */       return true;
/*  898 */     if (!Thread.interrupted())
/*  899 */       return false;
/*  900 */     throw new InterruptedException();
/*      */   }
/*      */ 
/*      */   public boolean offer(E paramE)
/*      */   {
/*  913 */     if (paramE == null) throw new NullPointerException();
/*  914 */     return this.transferer.transfer(paramE, true, 0L) != null;
/*      */   }
/*      */ 
/*      */   public E take()
/*      */     throws InterruptedException
/*      */   {
/*  925 */     Object localObject = this.transferer.transfer(null, false, 0L);
/*  926 */     if (localObject != null)
/*  927 */       return localObject;
/*  928 */     Thread.interrupted();
/*  929 */     throw new InterruptedException();
/*      */   }
/*      */ 
/*      */   public E poll(long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException
/*      */   {
/*  942 */     Object localObject = this.transferer.transfer(null, true, paramTimeUnit.toNanos(paramLong));
/*  943 */     if ((localObject != null) || (!Thread.interrupted()))
/*  944 */       return localObject;
/*  945 */     throw new InterruptedException();
/*      */   }
/*      */ 
/*      */   public E poll()
/*      */   {
/*  956 */     return this.transferer.transfer(null, true, 0L);
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  966 */     return true;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  976 */     return 0;
/*      */   }
/*      */ 
/*      */   public int remainingCapacity()
/*      */   {
/*  986 */     return 0;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/* 1004 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/* 1015 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean containsAll(Collection<?> paramCollection)
/*      */   {
/* 1026 */     return paramCollection.isEmpty();
/*      */   }
/*      */ 
/*      */   public boolean removeAll(Collection<?> paramCollection)
/*      */   {
/* 1037 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean retainAll(Collection<?> paramCollection)
/*      */   {
/* 1048 */     return false;
/*      */   }
/*      */ 
/*      */   public E peek()
/*      */   {
/* 1059 */     return null;
/*      */   }
/*      */ 
/*      */   public Iterator<E> iterator()
/*      */   {
/* 1069 */     return Collections.emptyIterator();
/*      */   }
/*      */ 
/*      */   public Object[] toArray()
/*      */   {
/* 1077 */     return new Object[0];
/*      */   }
/*      */ 
/*      */   public <T> T[] toArray(T[] paramArrayOfT)
/*      */   {
/* 1089 */     if (paramArrayOfT.length > 0)
/* 1090 */       paramArrayOfT[0] = null;
/* 1091 */     return paramArrayOfT;
/*      */   }
/*      */ 
/*      */   public int drainTo(Collection<? super E> paramCollection)
/*      */   {
/* 1101 */     if (paramCollection == null)
/* 1102 */       throw new NullPointerException();
/* 1103 */     if (paramCollection == this)
/* 1104 */       throw new IllegalArgumentException();
/* 1105 */     int i = 0;
/*      */     Object localObject;
/* 1107 */     while ((localObject = poll()) != null) {
/* 1108 */       paramCollection.add(localObject);
/* 1109 */       i++;
/*      */     }
/* 1111 */     return i;
/*      */   }
/*      */ 
/*      */   public int drainTo(Collection<? super E> paramCollection, int paramInt)
/*      */   {
/* 1121 */     if (paramCollection == null)
/* 1122 */       throw new NullPointerException();
/* 1123 */     if (paramCollection == this)
/* 1124 */       throw new IllegalArgumentException();
/* 1125 */     int i = 0;
/*      */     Object localObject;
/* 1127 */     while ((i < paramInt) && ((localObject = poll()) != null)) {
/* 1128 */       paramCollection.add(localObject);
/* 1129 */       i++;
/*      */     }
/* 1131 */     return i;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1160 */     boolean bool = this.transferer instanceof TransferQueue;
/* 1161 */     if (bool) {
/* 1162 */       this.qlock = new ReentrantLock(true);
/* 1163 */       this.waitingProducers = new FifoWaitQueue();
/* 1164 */       this.waitingConsumers = new FifoWaitQueue();
/*      */     }
/*      */     else {
/* 1167 */       this.qlock = new ReentrantLock();
/* 1168 */       this.waitingProducers = new LifoWaitQueue();
/* 1169 */       this.waitingConsumers = new LifoWaitQueue();
/*      */     }
/* 1171 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*      */   {
/* 1176 */     paramObjectInputStream.defaultReadObject();
/* 1177 */     if ((this.waitingProducers instanceof FifoWaitQueue))
/* 1178 */       this.transferer = new TransferQueue();
/*      */     else
/* 1180 */       this.transferer = new TransferStack();
/*      */   }
/*      */ 
/*      */   static long objectFieldOffset(Unsafe paramUnsafe, String paramString, Class<?> paramClass)
/*      */   {
/*      */     try
/*      */     {
/* 1187 */       return paramUnsafe.objectFieldOffset(paramClass.getDeclaredField(paramString));
/*      */     }
/*      */     catch (NoSuchFieldException localNoSuchFieldException) {
/* 1190 */       NoSuchFieldError localNoSuchFieldError = new NoSuchFieldError(paramString);
/* 1191 */       localNoSuchFieldError.initCause(localNoSuchFieldException);
/* 1192 */       throw localNoSuchFieldError;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class FifoWaitQueue extends SynchronousQueue.WaitQueue
/*      */   {
/*      */     private static final long serialVersionUID = -3623113410248163686L;
/*      */   }
/*      */ 
/*      */   static class LifoWaitQueue extends SynchronousQueue.WaitQueue
/*      */   {
/*      */     private static final long serialVersionUID = -3633113410248163686L;
/*      */   }
/*      */ 
/*      */   static final class TransferQueue extends SynchronousQueue.Transferer
/*      */   {
/*      */     volatile transient QNode head;
/*      */     volatile transient QNode tail;
/*      */     volatile transient QNode cleanMe;
/*      */     private static final Unsafe UNSAFE;
/*      */     private static final long headOffset;
/*      */     private static final long tailOffset;
/*      */     private static final long cleanMeOffset;
/*      */ 
/*      */     TransferQueue()
/*      */     {
/*  609 */       QNode localQNode = new QNode(null, false);
/*  610 */       this.head = localQNode;
/*  611 */       this.tail = localQNode;
/*      */     }
/*      */ 
/*      */     void advanceHead(QNode paramQNode1, QNode paramQNode2)
/*      */     {
/*  619 */       if ((paramQNode1 == this.head) && (UNSAFE.compareAndSwapObject(this, headOffset, paramQNode1, paramQNode2)))
/*      */       {
/*  621 */         paramQNode1.next = paramQNode1;
/*      */       }
/*      */     }
/*      */ 
/*      */     void advanceTail(QNode paramQNode1, QNode paramQNode2)
/*      */     {
/*  628 */       if (this.tail == paramQNode1)
/*  629 */         UNSAFE.compareAndSwapObject(this, tailOffset, paramQNode1, paramQNode2);
/*      */     }
/*      */ 
/*      */     boolean casCleanMe(QNode paramQNode1, QNode paramQNode2)
/*      */     {
/*  636 */       return (this.cleanMe == paramQNode1) && (UNSAFE.compareAndSwapObject(this, cleanMeOffset, paramQNode1, paramQNode2));
/*      */     }
/*      */ 
/*      */     Object transfer(Object paramObject, boolean paramBoolean, long paramLong)
/*      */     {
/*  669 */       QNode localQNode1 = null;
/*  670 */       boolean bool = paramObject != null;
/*      */       QNode localQNode3;
/*      */       QNode localQNode4;
/*      */       Object localObject;
/*      */       while (true)
/*      */       {
/*  673 */         QNode localQNode2 = this.tail;
/*  674 */         localQNode3 = this.head;
/*  675 */         if ((localQNode2 != null) && (localQNode3 != null))
/*      */         {
/*  678 */           if ((localQNode3 == localQNode2) || (localQNode2.isData == bool)) {
/*  679 */             localQNode4 = localQNode2.next;
/*  680 */             if (localQNode2 == this.tail)
/*      */             {
/*  682 */               if (localQNode4 != null) {
/*  683 */                 advanceTail(localQNode2, localQNode4);
/*      */               }
/*      */               else {
/*  686 */                 if ((paramBoolean) && (paramLong <= 0L))
/*  687 */                   return null;
/*  688 */                 if (localQNode1 == null)
/*  689 */                   localQNode1 = new QNode(paramObject, bool);
/*  690 */                 if (localQNode2.casNext(null, localQNode1))
/*      */                 {
/*  693 */                   advanceTail(localQNode2, localQNode1);
/*  694 */                   localObject = awaitFulfill(localQNode1, paramObject, paramBoolean, paramLong);
/*  695 */                   if (localObject == localQNode1) {
/*  696 */                     clean(localQNode2, localQNode1);
/*  697 */                     return null;
/*      */                   }
/*      */ 
/*  700 */                   if (!localQNode1.isOffList()) {
/*  701 */                     advanceHead(localQNode2, localQNode1);
/*  702 */                     if (localObject != null)
/*  703 */                       localQNode1.item = localQNode1;
/*  704 */                     localQNode1.waiter = null;
/*      */                   }
/*  706 */                   return localObject != null ? localObject : paramObject;
/*      */                 }
/*      */               }
/*      */             } } else { localQNode4 = localQNode3.next;
/*  710 */             if ((localQNode2 == this.tail) && (localQNode4 != null) && (localQNode3 == this.head))
/*      */             {
/*  713 */               localObject = localQNode4.item;
/*  714 */               if (bool != (localObject != null)) if ((localObject != localQNode4) && (localQNode4.casItem(localObject, paramObject))) {
/*      */                   break;
/*      */                 }
/*  717 */               advanceHead(localQNode3, localQNode4);
/*      */             } }
/*      */         }
/*      */       }
/*  721 */       advanceHead(localQNode3, localQNode4);
/*  722 */       LockSupport.unpark(localQNode4.waiter);
/*  723 */       return localObject != null ? localObject : paramObject;
/*      */     }
/*      */ 
/*      */     Object awaitFulfill(QNode paramQNode, Object paramObject, boolean paramBoolean, long paramLong)
/*      */     {
/*  739 */       long l1 = paramBoolean ? System.nanoTime() : 0L;
/*  740 */       Thread localThread = Thread.currentThread();
/*  741 */       int i = this.head.next == paramQNode ? SynchronousQueue.maxUntimedSpins : paramBoolean ? SynchronousQueue.maxTimedSpins : 0;
/*      */       while (true)
/*      */       {
/*  744 */         if (localThread.isInterrupted())
/*  745 */           paramQNode.tryCancel(paramObject);
/*  746 */         Object localObject = paramQNode.item;
/*  747 */         if (localObject != paramObject)
/*  748 */           return localObject;
/*  749 */         if (paramBoolean) {
/*  750 */           long l2 = System.nanoTime();
/*  751 */           paramLong -= l2 - l1;
/*  752 */           l1 = l2;
/*  753 */           if (paramLong <= 0L) {
/*  754 */             paramQNode.tryCancel(paramObject);
/*      */           }
/*      */ 
/*      */         }
/*  758 */         else if (i > 0) {
/*  759 */           i--;
/*  760 */         } else if (paramQNode.waiter == null) {
/*  761 */           paramQNode.waiter = localThread;
/*  762 */         } else if (!paramBoolean) {
/*  763 */           LockSupport.park(this);
/*  764 */         } else if (paramLong > 1000L) {
/*  765 */           LockSupport.parkNanos(this, paramLong);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void clean(QNode paramQNode1, QNode paramQNode2)
/*      */     {
/*  773 */       paramQNode2.waiter = null;
/*      */ 
/*  782 */       while (paramQNode1.next == paramQNode2) {
/*  783 */         QNode localQNode1 = this.head;
/*  784 */         QNode localQNode2 = localQNode1.next;
/*  785 */         if ((localQNode2 != null) && (localQNode2.isCancelled())) {
/*  786 */           advanceHead(localQNode1, localQNode2);
/*      */         }
/*      */         else {
/*  789 */           QNode localQNode3 = this.tail;
/*  790 */           if (localQNode3 == localQNode1)
/*  791 */             return;
/*  792 */           QNode localQNode4 = localQNode3.next;
/*  793 */           if (localQNode3 == this.tail)
/*      */           {
/*  795 */             if (localQNode4 != null) {
/*  796 */               advanceTail(localQNode3, localQNode4);
/*      */             }
/*      */             else {
/*  799 */               if (paramQNode2 != localQNode3) {
/*  800 */                 localQNode5 = paramQNode2.next;
/*  801 */                 if ((localQNode5 == paramQNode2) || (paramQNode1.casNext(paramQNode2, localQNode5)))
/*  802 */                   return;
/*      */               }
/*  804 */               QNode localQNode5 = this.cleanMe;
/*  805 */               if (localQNode5 != null) {
/*  806 */                 QNode localQNode6 = localQNode5.next;
/*      */                 QNode localQNode7;
/*  808 */                 if ((localQNode6 == null) || (localQNode6 == localQNode5) || (!localQNode6.isCancelled()) || ((localQNode6 != localQNode3) && ((localQNode7 = localQNode6.next) != null) && (localQNode7 != localQNode6) && (localQNode5.casNext(localQNode6, localQNode7))))
/*      */                 {
/*  815 */                   casCleanMe(localQNode5, null);
/*  816 */                 }if (localQNode5 == paramQNode1)
/*  817 */                   return;
/*  818 */               } else if (casCleanMe(null, paramQNode1)) {
/*  819 */                 return;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static {
/*      */       try {
/*  829 */         UNSAFE = Unsafe.getUnsafe();
/*  830 */         TransferQueue localTransferQueue = TransferQueue.class;
/*  831 */         headOffset = UNSAFE.objectFieldOffset(localTransferQueue.getDeclaredField("head"));
/*      */ 
/*  833 */         tailOffset = UNSAFE.objectFieldOffset(localTransferQueue.getDeclaredField("tail"));
/*      */ 
/*  835 */         cleanMeOffset = UNSAFE.objectFieldOffset(localTransferQueue.getDeclaredField("cleanMe"));
/*      */       }
/*      */       catch (Exception localException) {
/*  838 */         throw new Error(localException);
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class QNode
/*      */     {
/*      */       volatile QNode next;
/*      */       volatile Object item;
/*      */       volatile Thread waiter;
/*      */       final boolean isData;
/*      */       private static final Unsafe UNSAFE;
/*      */       private static final long itemOffset;
/*      */       private static final long nextOffset;
/*      */ 
/*      */       QNode(Object paramObject, boolean paramBoolean)
/*      */       {
/*  544 */         this.item = paramObject;
/*  545 */         this.isData = paramBoolean;
/*      */       }
/*      */ 
/*      */       boolean casNext(QNode paramQNode1, QNode paramQNode2) {
/*  549 */         return (this.next == paramQNode1) && (UNSAFE.compareAndSwapObject(this, nextOffset, paramQNode1, paramQNode2));
/*      */       }
/*      */ 
/*      */       boolean casItem(Object paramObject1, Object paramObject2)
/*      */       {
/*  554 */         return (this.item == paramObject1) && (UNSAFE.compareAndSwapObject(this, itemOffset, paramObject1, paramObject2));
/*      */       }
/*      */ 
/*      */       void tryCancel(Object paramObject)
/*      */       {
/*  562 */         UNSAFE.compareAndSwapObject(this, itemOffset, paramObject, this);
/*      */       }
/*      */ 
/*      */       boolean isCancelled() {
/*  566 */         return this.item == this;
/*      */       }
/*      */ 
/*      */       boolean isOffList()
/*      */       {
/*  575 */         return this.next == this;
/*      */       }
/*      */ 
/*      */       static
/*      */       {
/*      */         try
/*      */         {
/*  585 */           UNSAFE = Unsafe.getUnsafe();
/*  586 */           QNode localQNode = QNode.class;
/*  587 */           itemOffset = UNSAFE.objectFieldOffset(localQNode.getDeclaredField("item"));
/*      */ 
/*  589 */           nextOffset = UNSAFE.objectFieldOffset(localQNode.getDeclaredField("next"));
/*      */         }
/*      */         catch (Exception localException) {
/*  592 */           throw new Error(localException);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class TransferStack extends SynchronousQueue.Transferer
/*      */   {
/*      */     static final int REQUEST = 0;
/*      */     static final int DATA = 1;
/*      */     static final int FULFILLING = 2;
/*      */     volatile SNode head;
/*      */     private static final Unsafe UNSAFE;
/*      */     private static final long headOffset;
/*      */ 
/*      */     static boolean isFulfilling(int paramInt)
/*      */     {
/*  227 */       return (paramInt & 0x2) != 0;
/*      */     }
/*      */ 
/*      */     boolean casHead(SNode paramSNode1, SNode paramSNode2)
/*      */     {
/*  304 */       return (paramSNode1 == this.head) && (UNSAFE.compareAndSwapObject(this, headOffset, paramSNode1, paramSNode2));
/*      */     }
/*      */ 
/*      */     static SNode snode(SNode paramSNode1, Object paramObject, SNode paramSNode2, int paramInt)
/*      */     {
/*  316 */       if (paramSNode1 == null) paramSNode1 = new SNode(paramObject);
/*  317 */       paramSNode1.mode = paramInt;
/*  318 */       paramSNode1.next = paramSNode2;
/*  319 */       return paramSNode1;
/*      */     }
/*      */ 
/*      */     Object transfer(Object paramObject, boolean paramBoolean, long paramLong)
/*      */     {
/*  347 */       SNode localSNode1 = null;
/*  348 */       int i = paramObject == null ? 0 : 1;
/*      */       while (true)
/*      */       {
/*  351 */         SNode localSNode2 = this.head;
/*      */         SNode localSNode3;
/*  352 */         if ((localSNode2 == null) || (localSNode2.mode == i)) {
/*  353 */           if ((paramBoolean) && (paramLong <= 0L)) {
/*  354 */             if ((localSNode2 != null) && (localSNode2.isCancelled()))
/*  355 */               casHead(localSNode2, localSNode2.next);
/*      */             else
/*  357 */               return null;
/*  358 */           } else if (casHead(localSNode2, localSNode1 = snode(localSNode1, paramObject, localSNode2, i))) {
/*  359 */             localSNode3 = awaitFulfill(localSNode1, paramBoolean, paramLong);
/*  360 */             if (localSNode3 == localSNode1) {
/*  361 */               clean(localSNode1);
/*  362 */               return null;
/*      */             }
/*  364 */             if (((localSNode2 = this.head) != null) && (localSNode2.next == localSNode1))
/*  365 */               casHead(localSNode2, localSNode1.next);
/*  366 */             return i == 0 ? localSNode3.item : localSNode1.item;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           SNode localSNode4;
/*  368 */           if (!isFulfilling(localSNode2.mode)) {
/*  369 */             if (localSNode2.isCancelled())
/*  370 */               casHead(localSNode2, localSNode2.next);
/*  371 */             else if (casHead(localSNode2, localSNode1 = snode(localSNode1, paramObject, localSNode2, 0x2 | i)))
/*      */               while (true) {
/*  373 */                 localSNode3 = localSNode1.next;
/*  374 */                 if (localSNode3 == null) {
/*  375 */                   casHead(localSNode1, null);
/*  376 */                   localSNode1 = null;
/*  377 */                   break;
/*      */                 }
/*  379 */                 localSNode4 = localSNode3.next;
/*  380 */                 if (localSNode3.tryMatch(localSNode1)) {
/*  381 */                   casHead(localSNode1, localSNode4);
/*  382 */                   return i == 0 ? localSNode3.item : localSNode1.item;
/*      */                 }
/*  384 */                 localSNode1.casNext(localSNode3, localSNode4);
/*      */               }
/*      */           }
/*      */           else {
/*  388 */             localSNode3 = localSNode2.next;
/*  389 */             if (localSNode3 == null) {
/*  390 */               casHead(localSNode2, null);
/*      */             } else {
/*  392 */               localSNode4 = localSNode3.next;
/*  393 */               if (localSNode3.tryMatch(localSNode2))
/*  394 */                 casHead(localSNode2, localSNode4);
/*      */               else
/*  396 */                 localSNode2.casNext(localSNode3, localSNode4);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     SNode awaitFulfill(SNode paramSNode, boolean paramBoolean, long paramLong)
/*      */     {
/*  433 */       long l1 = paramBoolean ? System.nanoTime() : 0L;
/*  434 */       Thread localThread = Thread.currentThread();
/*  435 */       SNode localSNode1 = this.head;
/*  436 */       int i = shouldSpin(paramSNode) ? SynchronousQueue.maxUntimedSpins : paramBoolean ? SynchronousQueue.maxTimedSpins : 0;
/*      */       while (true)
/*      */       {
/*  439 */         if (localThread.isInterrupted())
/*  440 */           paramSNode.tryCancel();
/*  441 */         SNode localSNode2 = paramSNode.match;
/*  442 */         if (localSNode2 != null)
/*  443 */           return localSNode2;
/*  444 */         if (paramBoolean) {
/*  445 */           long l2 = System.nanoTime();
/*  446 */           paramLong -= l2 - l1;
/*  447 */           l1 = l2;
/*  448 */           if (paramLong <= 0L) {
/*  449 */             paramSNode.tryCancel();
/*      */           }
/*      */ 
/*      */         }
/*  453 */         else if (i > 0) {
/*  454 */           i = shouldSpin(paramSNode) ? i - 1 : 0;
/*  455 */         } else if (paramSNode.waiter == null) {
/*  456 */           paramSNode.waiter = localThread;
/*  457 */         } else if (!paramBoolean) {
/*  458 */           LockSupport.park(this);
/*  459 */         } else if (paramLong > 1000L) {
/*  460 */           LockSupport.parkNanos(this, paramLong);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     boolean shouldSpin(SNode paramSNode)
/*      */     {
/*  469 */       SNode localSNode = this.head;
/*  470 */       return (localSNode == paramSNode) || (localSNode == null) || (isFulfilling(localSNode.mode));
/*      */     }
/*      */ 
/*      */     void clean(SNode paramSNode)
/*      */     {
/*  477 */       paramSNode.item = null;
/*  478 */       paramSNode.waiter = null;
/*      */ 
/*  491 */       SNode localSNode1 = paramSNode.next;
/*  492 */       if ((localSNode1 != null) && (localSNode1.isCancelled()))
/*  493 */         localSNode1 = localSNode1.next;
/*      */       Object localObject;
/*  497 */       while (((localObject = this.head) != null) && (localObject != localSNode1) && (((SNode)localObject).isCancelled())) {
/*  498 */         casHead((SNode)localObject, ((SNode)localObject).next);
/*      */       }
/*      */ 
/*  501 */       while ((localObject != null) && (localObject != localSNode1)) {
/*  502 */         SNode localSNode2 = ((SNode)localObject).next;
/*  503 */         if ((localSNode2 != null) && (localSNode2.isCancelled()))
/*  504 */           ((SNode)localObject).casNext(localSNode2, localSNode2.next);
/*      */         else
/*  506 */           localObject = localSNode2;
/*      */       }
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/*  515 */         UNSAFE = Unsafe.getUnsafe();
/*  516 */         TransferStack localTransferStack = TransferStack.class;
/*  517 */         headOffset = UNSAFE.objectFieldOffset(localTransferStack.getDeclaredField("head"));
/*      */       }
/*      */       catch (Exception localException) {
/*  520 */         throw new Error(localException);
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class SNode
/*      */     {
/*      */       volatile SNode next;
/*      */       volatile SNode match;
/*      */       volatile Thread waiter;
/*      */       Object item;
/*      */       int mode;
/*      */       private static final Unsafe UNSAFE;
/*      */       private static final long matchOffset;
/*      */       private static final long nextOffset;
/*      */ 
/*      */       SNode(Object paramObject)
/*      */       {
/*  241 */         this.item = paramObject;
/*      */       }
/*      */ 
/*      */       boolean casNext(SNode paramSNode1, SNode paramSNode2) {
/*  245 */         return (paramSNode1 == this.next) && (UNSAFE.compareAndSwapObject(this, nextOffset, paramSNode1, paramSNode2));
/*      */       }
/*      */ 
/*      */       boolean tryMatch(SNode paramSNode)
/*      */       {
/*  258 */         if ((this.match == null) && (UNSAFE.compareAndSwapObject(this, matchOffset, null, paramSNode)))
/*      */         {
/*  260 */           Thread localThread = this.waiter;
/*  261 */           if (localThread != null) {
/*  262 */             this.waiter = null;
/*  263 */             LockSupport.unpark(localThread);
/*      */           }
/*  265 */           return true;
/*      */         }
/*  267 */         return this.match == paramSNode;
/*      */       }
/*      */ 
/*      */       void tryCancel()
/*      */       {
/*  274 */         UNSAFE.compareAndSwapObject(this, matchOffset, null, this);
/*      */       }
/*      */ 
/*      */       boolean isCancelled() {
/*  278 */         return this.match == this;
/*      */       }
/*      */ 
/*      */       static
/*      */       {
/*      */         try
/*      */         {
/*  288 */           UNSAFE = Unsafe.getUnsafe();
/*  289 */           SNode localSNode = SNode.class;
/*  290 */           matchOffset = UNSAFE.objectFieldOffset(localSNode.getDeclaredField("match"));
/*      */ 
/*  292 */           nextOffset = UNSAFE.objectFieldOffset(localSNode.getDeclaredField("next"));
/*      */         }
/*      */         catch (Exception localException) {
/*  295 */           throw new Error(localException);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class Transferer
/*      */   {
/*      */     abstract Object transfer(Object paramObject, boolean paramBoolean, long paramLong);
/*      */   }
/*      */ 
/*      */   static class WaitQueue
/*      */     implements Serializable
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.SynchronousQueue
 * JD-Core Version:    0.6.2
 */