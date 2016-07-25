/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ 
/*      */ public class LinkedBlockingDeque<E> extends AbstractQueue<E>
/*      */   implements BlockingDeque<E>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -387911632671998426L;
/*      */   transient Node<E> first;
/*      */   transient Node<E> last;
/*      */   private transient int count;
/*      */   private final int capacity;
/*  155 */   final ReentrantLock lock = new ReentrantLock();
/*      */ 
/*  158 */   private final Condition notEmpty = this.lock.newCondition();
/*      */ 
/*  161 */   private final Condition notFull = this.lock.newCondition();
/*      */ 
/*      */   public LinkedBlockingDeque()
/*      */   {
/*  168 */     this(2147483647);
/*      */   }
/*      */ 
/*      */   public LinkedBlockingDeque(int paramInt)
/*      */   {
/*  178 */     if (paramInt <= 0) throw new IllegalArgumentException();
/*  179 */     this.capacity = paramInt;
/*      */   }
/*      */ 
/*      */   public LinkedBlockingDeque(Collection<? extends E> paramCollection)
/*      */   {
/*  193 */     this(2147483647);
/*  194 */     ReentrantLock localReentrantLock = this.lock;
/*  195 */     localReentrantLock.lock();
/*      */     try {
/*  197 */       for (localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject1 = localIterator.next();
/*  198 */         if (localObject1 == null)
/*  199 */           throw new NullPointerException();
/*  200 */         if (!linkLast(new Node(localObject1)))
/*  201 */           throw new IllegalStateException("Deque full");
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/*      */       Iterator localIterator;
/*  204 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean linkFirst(Node<E> paramNode)
/*      */   {
/*  216 */     if (this.count >= this.capacity)
/*  217 */       return false;
/*  218 */     Node localNode = this.first;
/*  219 */     paramNode.next = localNode;
/*  220 */     this.first = paramNode;
/*  221 */     if (this.last == null)
/*  222 */       this.last = paramNode;
/*      */     else
/*  224 */       localNode.prev = paramNode;
/*  225 */     this.count += 1;
/*  226 */     this.notEmpty.signal();
/*  227 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean linkLast(Node<E> paramNode)
/*      */   {
/*  235 */     if (this.count >= this.capacity)
/*  236 */       return false;
/*  237 */     Node localNode = this.last;
/*  238 */     paramNode.prev = localNode;
/*  239 */     this.last = paramNode;
/*  240 */     if (this.first == null)
/*  241 */       this.first = paramNode;
/*      */     else
/*  243 */       localNode.next = paramNode;
/*  244 */     this.count += 1;
/*  245 */     this.notEmpty.signal();
/*  246 */     return true;
/*      */   }
/*      */ 
/*      */   private E unlinkFirst()
/*      */   {
/*  254 */     Node localNode1 = this.first;
/*  255 */     if (localNode1 == null)
/*  256 */       return null;
/*  257 */     Node localNode2 = localNode1.next;
/*  258 */     Object localObject = localNode1.item;
/*  259 */     localNode1.item = null;
/*  260 */     localNode1.next = localNode1;
/*  261 */     this.first = localNode2;
/*  262 */     if (localNode2 == null)
/*  263 */       this.last = null;
/*      */     else
/*  265 */       localNode2.prev = null;
/*  266 */     this.count -= 1;
/*  267 */     this.notFull.signal();
/*  268 */     return localObject;
/*      */   }
/*      */ 
/*      */   private E unlinkLast()
/*      */   {
/*  276 */     Node localNode1 = this.last;
/*  277 */     if (localNode1 == null)
/*  278 */       return null;
/*  279 */     Node localNode2 = localNode1.prev;
/*  280 */     Object localObject = localNode1.item;
/*  281 */     localNode1.item = null;
/*  282 */     localNode1.prev = localNode1;
/*  283 */     this.last = localNode2;
/*  284 */     if (localNode2 == null)
/*  285 */       this.first = null;
/*      */     else
/*  287 */       localNode2.next = null;
/*  288 */     this.count -= 1;
/*  289 */     this.notFull.signal();
/*  290 */     return localObject;
/*      */   }
/*      */ 
/*      */   void unlink(Node<E> paramNode)
/*      */   {
/*  298 */     Node localNode1 = paramNode.prev;
/*  299 */     Node localNode2 = paramNode.next;
/*  300 */     if (localNode1 == null) {
/*  301 */       unlinkFirst();
/*  302 */     } else if (localNode2 == null) {
/*  303 */       unlinkLast();
/*      */     } else {
/*  305 */       localNode1.next = localNode2;
/*  306 */       localNode2.prev = localNode1;
/*  307 */       paramNode.item = null;
/*      */ 
/*  310 */       this.count -= 1;
/*  311 */       this.notFull.signal();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addFirst(E paramE)
/*      */   {
/*  322 */     if (!offerFirst(paramE))
/*  323 */       throw new IllegalStateException("Deque full");
/*      */   }
/*      */ 
/*      */   public void addLast(E paramE)
/*      */   {
/*  331 */     if (!offerLast(paramE))
/*  332 */       throw new IllegalStateException("Deque full");
/*      */   }
/*      */ 
/*      */   public boolean offerFirst(E paramE)
/*      */   {
/*  339 */     if (paramE == null) throw new NullPointerException();
/*  340 */     Node localNode = new Node(paramE);
/*  341 */     ReentrantLock localReentrantLock = this.lock;
/*  342 */     localReentrantLock.lock();
/*      */     try {
/*  344 */       return linkFirst(localNode);
/*      */     } finally {
/*  346 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean offerLast(E paramE)
/*      */   {
/*  354 */     if (paramE == null) throw new NullPointerException();
/*  355 */     Node localNode = new Node(paramE);
/*  356 */     ReentrantLock localReentrantLock = this.lock;
/*  357 */     localReentrantLock.lock();
/*      */     try {
/*  359 */       return linkLast(localNode);
/*      */     } finally {
/*  361 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void putFirst(E paramE)
/*      */     throws InterruptedException
/*      */   {
/*  370 */     if (paramE == null) throw new NullPointerException();
/*  371 */     Node localNode = new Node(paramE);
/*  372 */     ReentrantLock localReentrantLock = this.lock;
/*  373 */     localReentrantLock.lock();
/*      */     try {
/*  375 */       while (!linkFirst(localNode))
/*  376 */         this.notFull.await();
/*      */     } finally {
/*  378 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void putLast(E paramE)
/*      */     throws InterruptedException
/*      */   {
/*  387 */     if (paramE == null) throw new NullPointerException();
/*  388 */     Node localNode = new Node(paramE);
/*  389 */     ReentrantLock localReentrantLock = this.lock;
/*  390 */     localReentrantLock.lock();
/*      */     try {
/*  392 */       while (!linkLast(localNode))
/*  393 */         this.notFull.await();
/*      */     } finally {
/*  395 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean offerFirst(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException
/*      */   {
/*  405 */     if (paramE == null) throw new NullPointerException();
/*  406 */     Node localNode = new Node(paramE);
/*  407 */     long l = paramTimeUnit.toNanos(paramLong);
/*  408 */     ReentrantLock localReentrantLock = this.lock;
/*  409 */     localReentrantLock.lockInterruptibly();
/*      */     try
/*      */     {
/*      */       boolean bool;
/*  411 */       while (!linkFirst(localNode)) {
/*  412 */         if (l <= 0L)
/*  413 */           return false;
/*  414 */         l = this.notFull.awaitNanos(l);
/*      */       }
/*  416 */       return true;
/*      */     } finally {
/*  418 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean offerLast(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException
/*      */   {
/*  428 */     if (paramE == null) throw new NullPointerException();
/*  429 */     Node localNode = new Node(paramE);
/*  430 */     long l = paramTimeUnit.toNanos(paramLong);
/*  431 */     ReentrantLock localReentrantLock = this.lock;
/*  432 */     localReentrantLock.lockInterruptibly();
/*      */     try
/*      */     {
/*      */       boolean bool;
/*  434 */       while (!linkLast(localNode)) {
/*  435 */         if (l <= 0L)
/*  436 */           return false;
/*  437 */         l = this.notFull.awaitNanos(l);
/*      */       }
/*  439 */       return true;
/*      */     } finally {
/*  441 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E removeFirst()
/*      */   {
/*  449 */     Object localObject = pollFirst();
/*  450 */     if (localObject == null) throw new NoSuchElementException();
/*  451 */     return localObject;
/*      */   }
/*      */ 
/*      */   public E removeLast()
/*      */   {
/*  458 */     Object localObject = pollLast();
/*  459 */     if (localObject == null) throw new NoSuchElementException();
/*  460 */     return localObject;
/*      */   }
/*      */ 
/*      */   public E pollFirst() {
/*  464 */     ReentrantLock localReentrantLock = this.lock;
/*  465 */     localReentrantLock.lock();
/*      */     try {
/*  467 */       return unlinkFirst();
/*      */     } finally {
/*  469 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E pollLast() {
/*  474 */     ReentrantLock localReentrantLock = this.lock;
/*  475 */     localReentrantLock.lock();
/*      */     try {
/*  477 */       return unlinkLast();
/*      */     } finally {
/*  479 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E takeFirst() throws InterruptedException {
/*  484 */     ReentrantLock localReentrantLock = this.lock;
/*  485 */     localReentrantLock.lock();
/*      */     try
/*      */     {
/*      */       Object localObject1;
/*  488 */       while ((localObject1 = unlinkFirst()) == null)
/*  489 */         this.notEmpty.await();
/*  490 */       return localObject1;
/*      */     } finally {
/*  492 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E takeLast() throws InterruptedException {
/*  497 */     ReentrantLock localReentrantLock = this.lock;
/*  498 */     localReentrantLock.lock();
/*      */     try
/*      */     {
/*      */       Object localObject1;
/*  501 */       while ((localObject1 = unlinkLast()) == null)
/*  502 */         this.notEmpty.await();
/*  503 */       return localObject1;
/*      */     } finally {
/*  505 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E pollFirst(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
/*      */   {
/*  511 */     long l = paramTimeUnit.toNanos(paramLong);
/*  512 */     ReentrantLock localReentrantLock = this.lock;
/*  513 */     localReentrantLock.lockInterruptibly();
/*      */     try
/*      */     {
/*      */       Object localObject1;
/*      */       Object localObject2;
/*  516 */       while ((localObject1 = unlinkFirst()) == null) {
/*  517 */         if (l <= 0L)
/*  518 */           return null;
/*  519 */         l = this.notEmpty.awaitNanos(l);
/*      */       }
/*  521 */       return localObject1;
/*      */     } finally {
/*  523 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E pollLast(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
/*      */   {
/*  529 */     long l = paramTimeUnit.toNanos(paramLong);
/*  530 */     ReentrantLock localReentrantLock = this.lock;
/*  531 */     localReentrantLock.lockInterruptibly();
/*      */     try
/*      */     {
/*      */       Object localObject1;
/*      */       Object localObject2;
/*  534 */       while ((localObject1 = unlinkLast()) == null) {
/*  535 */         if (l <= 0L)
/*  536 */           return null;
/*  537 */         l = this.notEmpty.awaitNanos(l);
/*      */       }
/*  539 */       return localObject1;
/*      */     } finally {
/*  541 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E getFirst()
/*      */   {
/*  549 */     Object localObject = peekFirst();
/*  550 */     if (localObject == null) throw new NoSuchElementException();
/*  551 */     return localObject;
/*      */   }
/*      */ 
/*      */   public E getLast()
/*      */   {
/*  558 */     Object localObject = peekLast();
/*  559 */     if (localObject == null) throw new NoSuchElementException();
/*  560 */     return localObject;
/*      */   }
/*      */ 
/*      */   public E peekFirst() {
/*  564 */     ReentrantLock localReentrantLock = this.lock;
/*  565 */     localReentrantLock.lock();
/*      */     try {
/*  567 */       return this.first == null ? null : this.first.item;
/*      */     } finally {
/*  569 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E peekLast() {
/*  574 */     ReentrantLock localReentrantLock = this.lock;
/*  575 */     localReentrantLock.lock();
/*      */     try {
/*  577 */       return this.last == null ? null : this.last.item;
/*      */     } finally {
/*  579 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean removeFirstOccurrence(Object paramObject) {
/*  584 */     if (paramObject == null) return false;
/*  585 */     ReentrantLock localReentrantLock = this.lock;
/*  586 */     localReentrantLock.lock();
/*      */     try {
/*  588 */       for (Node localNode = this.first; localNode != null; localNode = localNode.next) {
/*  589 */         if (paramObject.equals(localNode.item)) {
/*  590 */           unlink(localNode);
/*  591 */           return true;
/*      */         }
/*      */       }
/*  594 */       return false;
/*      */     } finally {
/*  596 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean removeLastOccurrence(Object paramObject) {
/*  601 */     if (paramObject == null) return false;
/*  602 */     ReentrantLock localReentrantLock = this.lock;
/*  603 */     localReentrantLock.lock();
/*      */     try {
/*  605 */       for (Node localNode = this.last; localNode != null; localNode = localNode.prev) {
/*  606 */         if (paramObject.equals(localNode.item)) {
/*  607 */           unlink(localNode);
/*  608 */           return true;
/*      */         }
/*      */       }
/*  611 */       return false;
/*      */     } finally {
/*  613 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean add(E paramE)
/*      */   {
/*  631 */     addLast(paramE);
/*  632 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean offer(E paramE)
/*      */   {
/*  639 */     return offerLast(paramE);
/*      */   }
/*      */ 
/*      */   public void put(E paramE)
/*      */     throws InterruptedException
/*      */   {
/*  647 */     putLast(paramE);
/*      */   }
/*      */ 
/*      */   public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException
/*      */   {
/*  656 */     return offerLast(paramE, paramLong, paramTimeUnit);
/*      */   }
/*      */ 
/*      */   public E remove()
/*      */   {
/*  670 */     return removeFirst();
/*      */   }
/*      */ 
/*      */   public E poll() {
/*  674 */     return pollFirst();
/*      */   }
/*      */ 
/*      */   public E take() throws InterruptedException {
/*  678 */     return takeFirst();
/*      */   }
/*      */ 
/*      */   public E poll(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
/*  682 */     return pollFirst(paramLong, paramTimeUnit);
/*      */   }
/*      */ 
/*      */   public E element()
/*      */   {
/*  696 */     return getFirst();
/*      */   }
/*      */ 
/*      */   public E peek() {
/*  700 */     return peekFirst();
/*      */   }
/*      */ 
/*      */   public int remainingCapacity()
/*      */   {
/*  715 */     ReentrantLock localReentrantLock = this.lock;
/*  716 */     localReentrantLock.lock();
/*      */     try {
/*  718 */       return this.capacity - this.count;
/*      */     } finally {
/*  720 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int drainTo(Collection<? super E> paramCollection)
/*      */   {
/*  731 */     return drainTo(paramCollection, 2147483647);
/*      */   }
/*      */ 
/*      */   public int drainTo(Collection<? super E> paramCollection, int paramInt)
/*      */   {
/*  741 */     if (paramCollection == null)
/*  742 */       throw new NullPointerException();
/*  743 */     if (paramCollection == this)
/*  744 */       throw new IllegalArgumentException();
/*  745 */     ReentrantLock localReentrantLock = this.lock;
/*  746 */     localReentrantLock.lock();
/*      */     try {
/*  748 */       int i = Math.min(paramInt, this.count);
/*  749 */       for (int j = 0; j < i; j++) {
/*  750 */         paramCollection.add(this.first.item);
/*  751 */         unlinkFirst();
/*      */       }
/*  753 */       return i;
/*      */     } finally {
/*  755 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void push(E paramE)
/*      */   {
/*  766 */     addFirst(paramE);
/*      */   }
/*      */ 
/*      */   public E pop()
/*      */   {
/*  773 */     return removeFirst();
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/*  793 */     return removeFirstOccurrence(paramObject);
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  802 */     ReentrantLock localReentrantLock = this.lock;
/*  803 */     localReentrantLock.lock();
/*      */     try {
/*  805 */       return this.count;
/*      */     } finally {
/*  807 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/*  820 */     if (paramObject == null) return false;
/*  821 */     ReentrantLock localReentrantLock = this.lock;
/*  822 */     localReentrantLock.lock();
/*      */     try {
/*  824 */       for (Node localNode = this.first; localNode != null; localNode = localNode.next)
/*  825 */         if (paramObject.equals(localNode.item))
/*  826 */           return true;
/*  827 */       return false;
/*      */     } finally {
/*  829 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object[] toArray()
/*      */   {
/*  889 */     ReentrantLock localReentrantLock = this.lock;
/*  890 */     localReentrantLock.lock();
/*      */     try {
/*  892 */       Object[] arrayOfObject = new Object[this.count];
/*  893 */       int i = 0;
/*  894 */       for (Object localObject1 = this.first; localObject1 != null; localObject1 = ((Node)localObject1).next)
/*  895 */         arrayOfObject[(i++)] = ((Node)localObject1).item;
/*  896 */       return arrayOfObject;
/*      */     } finally {
/*  898 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> T[] toArray(T[] paramArrayOfT)
/*      */   {
/*  940 */     ReentrantLock localReentrantLock = this.lock;
/*  941 */     localReentrantLock.lock();
/*      */     try {
/*  943 */       if (paramArrayOfT.length < this.count) {
/*  944 */         paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), this.count);
/*      */       }
/*      */ 
/*  947 */       int i = 0;
/*  948 */       for (Object localObject1 = this.first; localObject1 != null; localObject1 = ((Node)localObject1).next)
/*  949 */         paramArrayOfT[(i++)] = ((Node)localObject1).item;
/*  950 */       if (paramArrayOfT.length > i)
/*  951 */         paramArrayOfT[i] = null;
/*  952 */       return paramArrayOfT;
/*      */     } finally {
/*  954 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public String toString() {
/*  959 */     ReentrantLock localReentrantLock = this.lock;
/*  960 */     localReentrantLock.lock();
/*      */     try {
/*  962 */       Node localNode = this.first;
/*  963 */       if (localNode == null) {
/*  964 */         return "[]";
/*      */       }
/*  966 */       Object localObject1 = new StringBuilder();
/*  967 */       ((StringBuilder)localObject1).append('[');
/*      */       while (true) {
/*  969 */         Object localObject2 = localNode.item;
/*  970 */         ((StringBuilder)localObject1).append(localObject2 == this ? "(this Collection)" : localObject2);
/*  971 */         localNode = localNode.next;
/*  972 */         if (localNode == null)
/*  973 */           return ']';
/*  974 */         ((StringBuilder)localObject1).append(',').append(' ');
/*      */       }
/*      */     } finally {
/*  977 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  986 */     ReentrantLock localReentrantLock = this.lock;
/*  987 */     localReentrantLock.lock();
/*      */     try {
/*  989 */       for (Object localObject1 = this.first; localObject1 != null; ) {
/*  990 */         ((Node)localObject1).item = null;
/*  991 */         Node localNode = ((Node)localObject1).next;
/*  992 */         ((Node)localObject1).prev = null;
/*  993 */         ((Node)localObject1).next = null;
/*  994 */         localObject1 = localNode;
/*      */       }
/*  996 */       this.first = (this.last = null);
/*  997 */       this.count = 0;
/*  998 */       this.notFull.signalAll();
/*      */     } finally {
/* 1000 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Iterator<E> iterator()
/*      */   {
/* 1018 */     return new Itr(null);
/*      */   }
/*      */ 
/*      */   public Iterator<E> descendingIterator()
/*      */   {
/* 1036 */     return new DescendingItr(null);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1162 */     ReentrantLock localReentrantLock = this.lock;
/* 1163 */     localReentrantLock.lock();
/*      */     try
/*      */     {
/* 1166 */       paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1168 */       for (Node localNode = this.first; localNode != null; localNode = localNode.next) {
/* 1169 */         paramObjectOutputStream.writeObject(localNode.item);
/*      */       }
/* 1171 */       paramObjectOutputStream.writeObject(null);
/*      */     } finally {
/* 1173 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1184 */     paramObjectInputStream.defaultReadObject();
/* 1185 */     this.count = 0;
/* 1186 */     this.first = null;
/* 1187 */     this.last = null;
/*      */     while (true)
/*      */     {
/* 1191 */       Object localObject = paramObjectInputStream.readObject();
/* 1192 */       if (localObject == null)
/*      */         break;
/* 1194 */       add(localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private abstract class AbstractItr
/*      */     implements Iterator<E>
/*      */   {
/*      */     LinkedBlockingDeque.Node<E> next;
/*      */     E nextItem;
/*      */     private LinkedBlockingDeque.Node<E> lastRet;
/*      */ 
/*      */     abstract LinkedBlockingDeque.Node<E> firstNode();
/*      */ 
/*      */     abstract LinkedBlockingDeque.Node<E> nextNode(LinkedBlockingDeque.Node<E> paramNode);
/*      */ 
/*      */     AbstractItr()
/*      */     {
/* 1067 */       ReentrantLock localReentrantLock = LinkedBlockingDeque.this.lock;
/* 1068 */       localReentrantLock.lock();
/*      */       try {
/* 1070 */         this.next = firstNode();
/* 1071 */         this.nextItem = (this.next == null ? null : this.next.item);
/*      */       } finally {
/* 1073 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     private LinkedBlockingDeque.Node<E> succ(LinkedBlockingDeque.Node<E> paramNode)
/*      */     {
/*      */       while (true)
/*      */       {
/* 1085 */         LinkedBlockingDeque.Node localNode = nextNode(paramNode);
/* 1086 */         if (localNode == null)
/* 1087 */           return null;
/* 1088 */         if (localNode.item != null)
/* 1089 */           return localNode;
/* 1090 */         if (localNode == paramNode) {
/* 1091 */           return firstNode();
/*      */         }
/* 1093 */         paramNode = localNode;
/*      */       }
/*      */     }
/*      */ 
/*      */     void advance()
/*      */     {
/* 1101 */       ReentrantLock localReentrantLock = LinkedBlockingDeque.this.lock;
/* 1102 */       localReentrantLock.lock();
/*      */       try
/*      */       {
/* 1105 */         this.next = succ(this.next);
/* 1106 */         this.nextItem = (this.next == null ? null : this.next.item);
/*      */       } finally {
/* 1108 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/* 1113 */       return this.next != null;
/*      */     }
/*      */ 
/*      */     public E next() {
/* 1117 */       if (this.next == null)
/* 1118 */         throw new NoSuchElementException();
/* 1119 */       this.lastRet = this.next;
/* 1120 */       Object localObject = this.nextItem;
/* 1121 */       advance();
/* 1122 */       return localObject;
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 1126 */       LinkedBlockingDeque.Node localNode = this.lastRet;
/* 1127 */       if (localNode == null)
/* 1128 */         throw new IllegalStateException();
/* 1129 */       this.lastRet = null;
/* 1130 */       ReentrantLock localReentrantLock = LinkedBlockingDeque.this.lock;
/* 1131 */       localReentrantLock.lock();
/*      */       try {
/* 1133 */         if (localNode.item != null)
/* 1134 */           LinkedBlockingDeque.this.unlink(localNode);
/*      */       } finally {
/* 1136 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DescendingItr extends LinkedBlockingDeque<E>.AbstractItr
/*      */   {
/*      */     private DescendingItr()
/*      */     {
/* 1148 */       super(); } 
/* 1149 */     LinkedBlockingDeque.Node<E> firstNode() { return LinkedBlockingDeque.this.last; } 
/* 1150 */     LinkedBlockingDeque.Node<E> nextNode(LinkedBlockingDeque.Node<E> paramNode) { return paramNode.prev; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private class Itr extends LinkedBlockingDeque<E>.AbstractItr
/*      */   {
/*      */     private Itr()
/*      */     {
/* 1142 */       super(); } 
/* 1143 */     LinkedBlockingDeque.Node<E> firstNode() { return LinkedBlockingDeque.this.first; } 
/* 1144 */     LinkedBlockingDeque.Node<E> nextNode(LinkedBlockingDeque.Node<E> paramNode) { return paramNode.next; }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class Node<E>
/*      */   {
/*      */     E item;
/*      */     Node<E> prev;
/*      */     Node<E> next;
/*      */ 
/*      */     Node(E paramE)
/*      */     {
/*  130 */       this.item = paramE;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.LinkedBlockingDeque
 * JD-Core Version:    0.6.2
 */