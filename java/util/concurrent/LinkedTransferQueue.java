/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public class LinkedTransferQueue<E> extends AbstractQueue<E>
/*      */   implements TransferQueue<E>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -3223113410248163686L;
/*  411 */   private static final boolean MP = Runtime.getRuntime().availableProcessors() > 1;
/*      */   private static final int FRONT_SPINS = 128;
/*      */   private static final int CHAINED_SPINS = 64;
/*      */   static final int SWEEP_THRESHOLD = 32;
/*      */   volatile transient Node head;
/*      */   private volatile transient Node tail;
/*      */   private volatile transient int sweepVotes;
/*      */   private static final int NOW = 0;
/*      */   private static final int ASYNC = 1;
/*      */   private static final int SYNC = 2;
/*      */   private static final int TIMED = 3;
/*      */   private static final Unsafe UNSAFE;
/*      */   private static final long headOffset;
/*      */   private static final long tailOffset;
/*      */   private static final long sweepVotesOffset;
/*      */ 
/*      */   private boolean casTail(Node paramNode1, Node paramNode2)
/*      */   {
/*  569 */     return UNSAFE.compareAndSwapObject(this, tailOffset, paramNode1, paramNode2);
/*      */   }
/*      */ 
/*      */   private boolean casHead(Node paramNode1, Node paramNode2) {
/*  573 */     return UNSAFE.compareAndSwapObject(this, headOffset, paramNode1, paramNode2);
/*      */   }
/*      */ 
/*      */   private boolean casSweepVotes(int paramInt1, int paramInt2) {
/*  577 */     return UNSAFE.compareAndSwapInt(this, sweepVotesOffset, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   static <E> E cast(Object paramObject)
/*      */   {
/*  591 */     return paramObject;
/*      */   }
/*      */ 
/*      */   private E xfer(E paramE, boolean paramBoolean, int paramInt, long paramLong)
/*      */   {
/*  605 */     if ((paramBoolean) && (paramE == null))
/*  606 */       throw new NullPointerException();
/*  607 */     Node localNode1 = null;
/*      */     Node localNode2;
/*      */     do
/*      */     {
/*  612 */       localNode2 = this.head; for (Node localNode3 = localNode2; localNode3 != null; ) {
/*  613 */         boolean bool = localNode3.isData;
/*  614 */         Object localObject = localNode3.item;
/*  615 */         if (localObject != localNode3) if ((localObject != null) == bool) {
/*  616 */             if (bool == paramBoolean)
/*      */               break;
/*  618 */             if (localNode3.casItem(localObject, paramE)) {
/*  619 */               for (localNode4 = localNode3; localNode4 != localNode2; ) {
/*  620 */                 Node localNode5 = localNode4.next;
/*  621 */                 if (this.head == localNode2) if (casHead(localNode2, localNode5 == null ? localNode4 : localNode5)) {
/*  622 */                     localNode2.forgetNext();
/*  623 */                     break;
/*      */                   }
/*  625 */                 if (((localNode2 = this.head) == null) || ((localNode4 = localNode2.next) == null) || (!localNode4.isMatched())) {
/*      */                   break;
/*      */                 }
/*      */               }
/*  629 */               LockSupport.unpark(localNode3.waiter);
/*  630 */               return cast(localObject);
/*      */             }
/*      */           }
/*  633 */         Node localNode4 = localNode3.next;
/*  634 */         localNode3 = localNode2 = this.head;
/*      */       }
/*      */ 
/*  637 */       if (paramInt == 0) break;
/*  638 */       if (localNode1 == null)
/*  639 */         localNode1 = new Node(paramE, paramBoolean);
/*  640 */       localNode2 = tryAppend(localNode1, paramBoolean);
/*  641 */     }while (localNode2 == null);
/*      */ 
/*  643 */     if (paramInt != 1) {
/*  644 */       return awaitMatch(localNode1, localNode2, paramE, paramInt == 3, paramLong);
/*      */     }
/*  646 */     return paramE;
/*      */   }
/*      */ 
/*      */   private Node tryAppend(Node paramNode, boolean paramBoolean)
/*      */   {
/*  660 */     Object localObject1 = this.tail; Object localObject2 = localObject1;
/*      */     while (true)
/*  662 */       if ((localObject2 == null) && ((localObject2 = this.head) == null)) {
/*  663 */         if (casHead(null, paramNode))
/*  664 */           return paramNode;
/*      */       } else {
/*  666 */         if (((Node)localObject2).cannotPrecede(paramBoolean))
/*  667 */           return null;
/*      */         Node localNode1;
/*  668 */         if ((localNode1 = ((Node)localObject2).next) != null)
/*      */         {
/*      */           Node localNode2;
/*  669 */           localObject2 = localObject2 != localNode1 ? localNode1 : (localObject2 != localObject1) && (localObject1 != (localNode2 = this.tail)) ? (localObject1 = localNode2) : null;
/*      */         }
/*  671 */         else if (!((Node)localObject2).casNext(null, paramNode)) {
/*  672 */           localObject2 = ((Node)localObject2).next;
/*      */         } else {
/*  674 */           while ((localObject2 != localObject1) && 
/*  678 */             ((this.tail != localObject1) || (!casTail((Node)localObject1, paramNode))) && ((localObject1 = this.tail) != null) && ((paramNode = ((Node)localObject1).next) != null) && ((paramNode = paramNode.next) != null) && (paramNode != localObject1));
/*  680 */           return localObject2;
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private E awaitMatch(Node paramNode1, Node paramNode2, E paramE, boolean paramBoolean, long paramLong)
/*      */   {
/*  698 */     long l1 = paramBoolean ? System.nanoTime() : 0L;
/*  699 */     Thread localThread = Thread.currentThread();
/*  700 */     int i = -1;
/*  701 */     ThreadLocalRandom localThreadLocalRandom = null;
/*      */     while (true)
/*      */     {
/*  704 */       Object localObject = paramNode1.item;
/*  705 */       if (localObject != paramE)
/*      */       {
/*  707 */         paramNode1.forgetContents();
/*  708 */         return cast(localObject);
/*      */       }
/*  710 */       if (((localThread.isInterrupted()) || ((paramBoolean) && (paramLong <= 0L))) && (paramNode1.casItem(paramE, paramNode1)))
/*      */       {
/*  712 */         unsplice(paramNode2, paramNode1);
/*  713 */         return paramE;
/*      */       }
/*      */ 
/*  716 */       if (i < 0) {
/*  717 */         if ((i = spinsFor(paramNode2, paramNode1.isData)) > 0)
/*  718 */           localThreadLocalRandom = ThreadLocalRandom.current();
/*      */       }
/*  720 */       else if (i > 0) {
/*  721 */         i--;
/*  722 */         if (localThreadLocalRandom.nextInt(64) == 0)
/*  723 */           Thread.yield();
/*      */       }
/*  725 */       else if (paramNode1.waiter == null) {
/*  726 */         paramNode1.waiter = localThread;
/*      */       }
/*  728 */       else if (paramBoolean) {
/*  729 */         long l2 = System.nanoTime();
/*  730 */         if (paramLong -= l2 - l1 > 0L)
/*  731 */           LockSupport.parkNanos(this, paramLong);
/*  732 */         l1 = l2;
/*      */       }
/*      */       else {
/*  735 */         LockSupport.park(this);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int spinsFor(Node paramNode, boolean paramBoolean)
/*      */   {
/*  745 */     if ((MP) && (paramNode != null)) {
/*  746 */       if (paramNode.isData != paramBoolean)
/*  747 */         return 192;
/*  748 */       if (paramNode.isMatched())
/*  749 */         return 128;
/*  750 */       if (paramNode.waiter == null)
/*  751 */         return 64;
/*      */     }
/*  753 */     return 0;
/*      */   }
/*      */ 
/*      */   final Node succ(Node paramNode)
/*      */   {
/*  764 */     Node localNode = paramNode.next;
/*  765 */     return paramNode == localNode ? this.head : localNode;
/*      */   }
/*      */ 
/*      */   private Node firstOfMode(boolean paramBoolean)
/*      */   {
/*  773 */     for (Node localNode = this.head; localNode != null; localNode = succ(localNode)) {
/*  774 */       if (!localNode.isMatched())
/*  775 */         return localNode.isData == paramBoolean ? localNode : null;
/*      */     }
/*  777 */     return null;
/*      */   }
/*      */ 
/*      */   private E firstDataItem()
/*      */   {
/*  785 */     for (Node localNode = this.head; localNode != null; localNode = succ(localNode)) {
/*  786 */       Object localObject = localNode.item;
/*  787 */       if (localNode.isData) {
/*  788 */         if ((localObject != null) && (localObject != localNode))
/*  789 */           return cast(localObject);
/*      */       }
/*  791 */       else if (localObject == null)
/*  792 */         return null;
/*      */     }
/*  794 */     return null;
/*      */   }
/*      */ 
/*      */   private int countOfMode(boolean paramBoolean)
/*      */   {
/*  802 */     int i = 0;
/*  803 */     for (Object localObject = this.head; localObject != null; ) {
/*  804 */       if (!((Node)localObject).isMatched()) {
/*  805 */         if (((Node)localObject).isData != paramBoolean)
/*  806 */           return 0;
/*  807 */         i++; if (i == 2147483647)
/*      */           break;
/*      */       }
/*  810 */       Node localNode = ((Node)localObject).next;
/*  811 */       if (localNode != localObject) {
/*  812 */         localObject = localNode;
/*      */       } else {
/*  814 */         i = 0;
/*  815 */         localObject = this.head;
/*      */       }
/*      */     }
/*  818 */     return i;
/*      */   }
/*      */ 
/*      */   final void unsplice(Node paramNode1, Node paramNode2)
/*      */   {
/*  925 */     paramNode2.forgetContents();
/*      */ 
/*  933 */     if ((paramNode1 != null) && (paramNode1 != paramNode2) && (paramNode1.next == paramNode2)) {
/*  934 */       Node localNode1 = paramNode2.next;
/*  935 */       if ((localNode1 == null) || ((localNode1 != paramNode2) && (paramNode1.casNext(paramNode2, localNode1)) && (paramNode1.isMatched())))
/*      */       {
/*      */         while (true) {
/*  938 */           Node localNode2 = this.head;
/*  939 */           if ((localNode2 == paramNode1) || (localNode2 == paramNode2) || (localNode2 == null))
/*  940 */             return;
/*  941 */           if (!localNode2.isMatched())
/*      */             break;
/*  943 */           Node localNode3 = localNode2.next;
/*  944 */           if (localNode3 == null)
/*  945 */             return;
/*  946 */           if ((localNode3 != localNode2) && (casHead(localNode2, localNode3)))
/*  947 */             localNode2.forgetNext();
/*      */         }
/*  949 */         if ((paramNode1.next != paramNode1) && (paramNode2.next != paramNode2))
/*      */           while (true) {
/*  951 */             int i = this.sweepVotes;
/*  952 */             if (i < 32) {
/*  953 */               if (casSweepVotes(i, i + 1))
/*  954 */                 break;
/*      */             }
/*  956 */             else if (casSweepVotes(i, 0)) {
/*  957 */               sweep();
/*  958 */               break;
/*      */             }
/*      */           }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void sweep()
/*      */   {
/*      */     Node localNode1;
/*  971 */     for (Object localObject = this.head; (localObject != null) && ((localNode1 = ((Node)localObject).next) != null); )
/*  972 */       if (!localNode1.isMatched())
/*      */       {
/*  974 */         localObject = localNode1;
/*      */       }
/*      */       else
/*      */       {
/*      */         Node localNode2;
/*  975 */         if ((localNode2 = localNode1.next) == null)
/*      */           break;
/*  977 */         if (localNode1 == localNode2)
/*      */         {
/*  979 */           localObject = this.head;
/*      */         }
/*  981 */         else ((Node)localObject).casNext(localNode1, localNode2);
/*      */       }
/*      */   }
/*      */ 
/*      */   private boolean findAndRemove(Object paramObject)
/*      */   {
/*      */     Object localObject1;
/*      */     Node localNode;
/*  989 */     if (paramObject != null) {
/*  990 */       localObject1 = null; for (localNode = this.head; localNode != null; ) {
/*  991 */         Object localObject2 = localNode.item;
/*  992 */         if (localNode.isData) {
/*  993 */           if ((localObject2 != null) && (localObject2 != localNode) && (paramObject.equals(localObject2)) && (localNode.tryMatchData()))
/*      */           {
/*  995 */             unsplice((Node)localObject1, localNode);
/*  996 */             return true;
/*      */           }
/*      */         }
/*  999 */         else if (localObject2 == null)
/*      */             break;
/* 1001 */         localObject1 = localNode;
/* 1002 */         if ((localNode = localNode.next) == localObject1) {
/* 1003 */           localObject1 = null;
/* 1004 */           localNode = this.head;
/*      */         }
/*      */       }
/*      */     }
/* 1008 */     return false;
/*      */   }
/*      */ 
/*      */   public LinkedTransferQueue()
/*      */   {
/*      */   }
/*      */ 
/*      */   public LinkedTransferQueue(Collection<? extends E> paramCollection)
/*      */   {
/* 1028 */     this();
/* 1029 */     addAll(paramCollection);
/*      */   }
/*      */ 
/*      */   public void put(E paramE)
/*      */   {
/* 1039 */     xfer(paramE, true, 1, 0L);
/*      */   }
/*      */ 
/*      */   public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*      */   {
/* 1052 */     xfer(paramE, true, 1, 0L);
/* 1053 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean offer(E paramE)
/*      */   {
/* 1064 */     xfer(paramE, true, 1, 0L);
/* 1065 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean add(E paramE)
/*      */   {
/* 1077 */     xfer(paramE, true, 1, 0L);
/* 1078 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean tryTransfer(E paramE)
/*      */   {
/* 1092 */     return xfer(paramE, true, 0, 0L) == null;
/*      */   }
/*      */ 
/*      */   public void transfer(E paramE)
/*      */     throws InterruptedException
/*      */   {
/* 1107 */     if (xfer(paramE, true, 2, 0L) != null) {
/* 1108 */       Thread.interrupted();
/* 1109 */       throw new InterruptedException();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean tryTransfer(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException
/*      */   {
/* 1129 */     if (xfer(paramE, true, 3, paramTimeUnit.toNanos(paramLong)) == null)
/* 1130 */       return true;
/* 1131 */     if (!Thread.interrupted())
/* 1132 */       return false;
/* 1133 */     throw new InterruptedException();
/*      */   }
/*      */ 
/*      */   public E take() throws InterruptedException {
/* 1137 */     Object localObject = xfer(null, false, 2, 0L);
/* 1138 */     if (localObject != null)
/* 1139 */       return localObject;
/* 1140 */     Thread.interrupted();
/* 1141 */     throw new InterruptedException();
/*      */   }
/*      */ 
/*      */   public E poll(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
/* 1145 */     Object localObject = xfer(null, false, 3, paramTimeUnit.toNanos(paramLong));
/* 1146 */     if ((localObject != null) || (!Thread.interrupted()))
/* 1147 */       return localObject;
/* 1148 */     throw new InterruptedException();
/*      */   }
/*      */ 
/*      */   public E poll() {
/* 1152 */     return xfer(null, false, 0, 0L);
/*      */   }
/*      */ 
/*      */   public int drainTo(Collection<? super E> paramCollection)
/*      */   {
/* 1160 */     if (paramCollection == null)
/* 1161 */       throw new NullPointerException();
/* 1162 */     if (paramCollection == this)
/* 1163 */       throw new IllegalArgumentException();
/* 1164 */     int i = 0;
/*      */     Object localObject;
/* 1166 */     while ((localObject = poll()) != null) {
/* 1167 */       paramCollection.add(localObject);
/* 1168 */       i++;
/*      */     }
/* 1170 */     return i;
/*      */   }
/*      */ 
/*      */   public int drainTo(Collection<? super E> paramCollection, int paramInt)
/*      */   {
/* 1178 */     if (paramCollection == null)
/* 1179 */       throw new NullPointerException();
/* 1180 */     if (paramCollection == this)
/* 1181 */       throw new IllegalArgumentException();
/* 1182 */     int i = 0;
/*      */     Object localObject;
/* 1184 */     while ((i < paramInt) && ((localObject = poll()) != null)) {
/* 1185 */       paramCollection.add(localObject);
/* 1186 */       i++;
/*      */     }
/* 1188 */     return i;
/*      */   }
/*      */ 
/*      */   public Iterator<E> iterator()
/*      */   {
/* 1205 */     return new Itr();
/*      */   }
/*      */ 
/*      */   public E peek() {
/* 1209 */     return firstDataItem();
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1218 */     for (Node localNode = this.head; localNode != null; localNode = succ(localNode)) {
/* 1219 */       if (!localNode.isMatched())
/* 1220 */         return !localNode.isData;
/*      */     }
/* 1222 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean hasWaitingConsumer() {
/* 1226 */     return firstOfMode(false) != null;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 1242 */     return countOfMode(true);
/*      */   }
/*      */ 
/*      */   public int getWaitingConsumerCount() {
/* 1246 */     return countOfMode(false);
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/* 1261 */     return findAndRemove(paramObject);
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/* 1273 */     if (paramObject == null) return false;
/* 1274 */     for (Node localNode = this.head; localNode != null; localNode = succ(localNode)) {
/* 1275 */       Object localObject = localNode.item;
/* 1276 */       if (localNode.isData) {
/* 1277 */         if ((localObject != null) && (localObject != localNode) && (paramObject.equals(localObject)))
/* 1278 */           return true;
/*      */       }
/* 1280 */       else if (localObject == null)
/*      */           break;
/*      */     }
/* 1283 */     return false;
/*      */   }
/*      */ 
/*      */   public int remainingCapacity()
/*      */   {
/* 1294 */     return 2147483647;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1306 */     paramObjectOutputStream.defaultWriteObject();
/* 1307 */     for (Iterator localIterator = iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 1308 */       paramObjectOutputStream.writeObject(localObject);
/*      */     }
/* 1310 */     paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1321 */     paramObjectInputStream.defaultReadObject();
/*      */     while (true) {
/* 1323 */       Object localObject = paramObjectInputStream.readObject();
/* 1324 */       if (localObject == null) {
/*      */         break;
/*      */       }
/* 1327 */       offer(localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/* 1339 */       UNSAFE = Unsafe.getUnsafe();
/* 1340 */       LinkedTransferQueue localLinkedTransferQueue = LinkedTransferQueue.class;
/* 1341 */       headOffset = UNSAFE.objectFieldOffset(localLinkedTransferQueue.getDeclaredField("head"));
/*      */ 
/* 1343 */       tailOffset = UNSAFE.objectFieldOffset(localLinkedTransferQueue.getDeclaredField("tail"));
/*      */ 
/* 1345 */       sweepVotesOffset = UNSAFE.objectFieldOffset(localLinkedTransferQueue.getDeclaredField("sweepVotes"));
/*      */     }
/*      */     catch (Exception localException) {
/* 1348 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   final class Itr
/*      */     implements Iterator<E>
/*      */   {
/*      */     private LinkedTransferQueue.Node nextNode;
/*      */     private E nextItem;
/*      */     private LinkedTransferQueue.Node lastRet;
/*      */     private LinkedTransferQueue.Node lastPred;
/*      */ 
/*      */     private void advance(LinkedTransferQueue.Node paramNode)
/*      */     {
/*      */       LinkedTransferQueue.Node localNode1;
/*      */       LinkedTransferQueue.Node localNode3;
/*  842 */       if (((localNode1 = this.lastRet) != null) && (!localNode1.isMatched())) {
/*  843 */         this.lastPred = localNode1;
/*      */       }
/*      */       else
/*      */       {
/*      */         LinkedTransferQueue.Node localNode2;
/*  844 */         if (((localNode2 = this.lastPred) == null) || (localNode2.isMatched())) {
/*  845 */           this.lastPred = null;
/*      */         }
/*      */         else
/*      */         {
/*  850 */           while (((localObject1 = localNode2.next) != null) && (localObject1 != localNode2) && (((LinkedTransferQueue.Node)localObject1).isMatched()) && ((localNode3 = ((LinkedTransferQueue.Node)localObject1).next) != null) && (localNode3 != localObject1))
/*  851 */             localNode2.casNext((LinkedTransferQueue.Node)localObject1, localNode3);
/*      */         }
/*      */       }
/*  854 */       this.lastRet = paramNode;
/*      */ 
/*  856 */       Object localObject1 = paramNode;
/*      */       while (true) { localNode3 = localObject1 == null ? LinkedTransferQueue.this.head : ((LinkedTransferQueue.Node)localObject1).next;
/*  858 */         if (localNode3 == null)
/*      */           break;
/*  860 */         if (localNode3 == localObject1) {
/*  861 */           localObject1 = null;
/*      */         }
/*      */         else {
/*  864 */           Object localObject2 = localNode3.item;
/*  865 */           if (localNode3.isData) {
/*  866 */             if ((localObject2 != null) && (localObject2 != localNode3)) {
/*  867 */               this.nextItem = LinkedTransferQueue.cast(localObject2);
/*  868 */               this.nextNode = localNode3;
/*      */             }
/*      */           }
/*      */           else {
/*  872 */             if (localObject2 == null)
/*      */               break;
/*      */           }
/*  875 */           if (localObject1 == null) {
/*  876 */             localObject1 = localNode3;
/*      */           }
/*      */           else
/*      */           {
/*      */             LinkedTransferQueue.Node localNode4;
/*  877 */             if ((localNode4 = localNode3.next) == null)
/*      */               break;
/*  879 */             if (localNode3 == localNode4)
/*  880 */               localObject1 = null;
/*      */             else
/*  882 */               ((LinkedTransferQueue.Node)localObject1).casNext(localNode3, localNode4); 
/*      */           }
/*      */         } } this.nextNode = null;
/*  885 */       this.nextItem = null;
/*      */     }
/*      */ 
/*      */     Itr() {
/*  889 */       advance(null);
/*      */     }
/*      */ 
/*      */     public final boolean hasNext() {
/*  893 */       return this.nextNode != null;
/*      */     }
/*      */ 
/*      */     public final E next() {
/*  897 */       LinkedTransferQueue.Node localNode = this.nextNode;
/*  898 */       if (localNode == null) throw new NoSuchElementException();
/*  899 */       Object localObject = this.nextItem;
/*  900 */       advance(localNode);
/*  901 */       return localObject;
/*      */     }
/*      */ 
/*      */     public final void remove() {
/*  905 */       LinkedTransferQueue.Node localNode = this.lastRet;
/*  906 */       if (localNode == null)
/*  907 */         throw new IllegalStateException();
/*  908 */       this.lastRet = null;
/*  909 */       if (localNode.tryMatchData())
/*  910 */         LinkedTransferQueue.this.unsplice(this.lastPred, localNode);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Node
/*      */   {
/*      */     final boolean isData;
/*      */     volatile Object item;
/*      */     volatile Node next;
/*      */     volatile Thread waiter;
/*      */     private static final long serialVersionUID = -3375979862319811754L;
/*      */     private static final Unsafe UNSAFE;
/*      */     private static final long itemOffset;
/*      */     private static final long nextOffset;
/*      */     private static final long waiterOffset;
/*      */ 
/*      */     final boolean casNext(Node paramNode1, Node paramNode2)
/*      */     {
/*  456 */       return UNSAFE.compareAndSwapObject(this, nextOffset, paramNode1, paramNode2);
/*      */     }
/*      */ 
/*      */     final boolean casItem(Object paramObject1, Object paramObject2)
/*      */     {
/*  461 */       return UNSAFE.compareAndSwapObject(this, itemOffset, paramObject1, paramObject2);
/*      */     }
/*      */ 
/*      */     Node(Object paramObject, boolean paramBoolean)
/*      */     {
/*  469 */       UNSAFE.putObject(this, itemOffset, paramObject);
/*  470 */       this.isData = paramBoolean;
/*      */     }
/*      */ 
/*      */     final void forgetNext()
/*      */     {
/*  478 */       UNSAFE.putObject(this, nextOffset, this);
/*      */     }
/*      */ 
/*      */     final void forgetContents()
/*      */     {
/*  491 */       UNSAFE.putObject(this, itemOffset, this);
/*  492 */       UNSAFE.putObject(this, waiterOffset, null);
/*      */     }
/*      */ 
/*      */     final boolean isMatched()
/*      */     {
/*  500 */       Object localObject = this.item;
/*  501 */       if (localObject != this);
/*  501 */       return (localObject == null) == this.isData;
/*      */     }
/*      */ 
/*      */     final boolean isUnmatchedRequest()
/*      */     {
/*  508 */       return (!this.isData) && (this.item == null);
/*      */     }
/*      */ 
/*      */     final boolean cannotPrecede(boolean paramBoolean)
/*      */     {
/*  517 */       boolean bool = this.isData;
/*      */       Object localObject;
/*  519 */       if ((bool != paramBoolean) && ((localObject = this.item) != this));
/*  519 */       return (localObject != null) == bool;
/*      */     }
/*      */ 
/*      */     final boolean tryMatchData()
/*      */     {
/*  527 */       Object localObject = this.item;
/*  528 */       if ((localObject != null) && (localObject != this) && (casItem(localObject, null))) {
/*  529 */         LockSupport.unpark(this.waiter);
/*  530 */         return true;
/*      */       }
/*  532 */       return false;
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/*  544 */         UNSAFE = Unsafe.getUnsafe();
/*  545 */         Node localNode = Node.class;
/*  546 */         itemOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("item"));
/*      */ 
/*  548 */         nextOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("next"));
/*      */ 
/*  550 */         waiterOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("waiter"));
/*      */       }
/*      */       catch (Exception localException) {
/*  553 */         throw new Error(localException);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.LinkedTransferQueue
 * JD-Core Version:    0.6.2
 */