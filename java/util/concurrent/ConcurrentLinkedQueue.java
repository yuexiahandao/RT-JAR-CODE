/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class ConcurrentLinkedQueue<E> extends AbstractQueue<E>
/*     */   implements Queue<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 196745693267521676L;
/*     */   private volatile transient Node<E> head;
/*     */   private volatile transient Node<E> tail;
/*     */   private static final Unsafe UNSAFE;
/*     */   private static final long headOffset;
/*     */   private static final long tailOffset;
/*     */ 
/*     */   public ConcurrentLinkedQueue()
/*     */   {
/* 255 */     this.head = (this.tail = new Node(null));
/*     */   }
/*     */ 
/*     */   public ConcurrentLinkedQueue(Collection<? extends E> paramCollection)
/*     */   {
/* 268 */     Object localObject1 = null; Object localObject2 = null;
/* 269 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject3 = localIterator.next();
/* 270 */       checkNotNull(localObject3);
/* 271 */       Node localNode = new Node(localObject3);
/* 272 */       if (localObject1 == null) {
/* 273 */         localObject1 = localObject2 = localNode;
/*     */       } else {
/* 275 */         ((Node)localObject2).lazySetNext(localNode);
/* 276 */         localObject2 = localNode;
/*     */       }
/*     */     }
/* 279 */     if (localObject1 == null)
/* 280 */       localObject1 = localObject2 = new Node(null);
/* 281 */     this.head = ((Node)localObject1);
/* 282 */     this.tail = ((Node)localObject2);
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 296 */     return offer(paramE);
/*     */   }
/*     */ 
/*     */   final void updateHead(Node<E> paramNode1, Node<E> paramNode2)
/*     */   {
/* 304 */     if ((paramNode1 != paramNode2) && (casHead(paramNode1, paramNode2)))
/* 305 */       paramNode1.lazySetNext(paramNode1);
/*     */   }
/*     */ 
/*     */   final Node<E> succ(Node<E> paramNode)
/*     */   {
/* 314 */     Node localNode = paramNode.next;
/* 315 */     return paramNode == localNode ? this.head : localNode;
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE)
/*     */   {
/* 326 */     checkNotNull(paramE);
/* 327 */     Node localNode1 = new Node(paramE);
/*     */ 
/* 329 */     Node localNode2 = this.tail; Object localObject = localNode2;
/*     */     while (true) { Node localNode3 = ((Node)localObject).next;
/* 331 */       if (localNode3 == null)
/*     */       {
/* 333 */         if (((Node)localObject).casNext(null, localNode1))
/*     */         {
/* 337 */           if (localObject != localNode2)
/* 338 */             casTail(localNode2, localNode1);
/* 339 */           return true;
/*     */         }
/*     */ 
/*     */       }
/* 343 */       else if (localObject == localNode3)
/*     */       {
/* 348 */         localObject = localNode2 != (localNode2 = this.tail) ? localNode2 : this.head;
/*     */       }
/*     */       else
/* 351 */         localObject = (localObject != localNode2) && (localNode2 != (localNode2 = this.tail)) ? localNode2 : localNode3;
/*     */     }
/*     */   }
/*     */ 
/*     */   public E poll()
/*     */   {
/* 358 */     Node localNode1 = this.head; Object localObject1 = localNode1;
/*     */     while (true) { Object localObject2 = ((Node)localObject1).item;
/*     */       Node localNode2;
/* 361 */       if ((localObject2 != null) && (((Node)localObject1).casItem(localObject2, null)))
/*     */       {
/* 364 */         if (localObject1 != localNode1)
/* 365 */           updateHead(localNode1, (localNode2 = ((Node)localObject1).next) != null ? localNode2 : (Node)localObject1);
/* 366 */         return localObject2;
/*     */       }
/* 368 */       if ((localNode2 = ((Node)localObject1).next) == null) {
/* 369 */         updateHead(localNode1, (Node)localObject1);
/* 370 */         return null;
/*     */       }
/* 372 */       if (localObject1 == localNode2) {
/*     */         break;
/*     */       }
/* 375 */       localObject1 = localNode2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public E peek()
/*     */   {
/* 383 */     Node localNode1 = this.head; Object localObject1 = localNode1;
/*     */     while (true) { Object localObject2 = ((Node)localObject1).item;
/*     */       Node localNode2;
/* 385 */       if ((localObject2 != null) || ((localNode2 = ((Node)localObject1).next) == null)) {
/* 386 */         updateHead(localNode1, (Node)localObject1);
/* 387 */         return localObject2;
/*     */       }
/* 389 */       if (localObject1 == localNode2) {
/*     */         break;
/*     */       }
/* 392 */       localObject1 = localNode2;
/*     */     }
/*     */   }
/*     */ 
/*     */   Node<E> first()
/*     */   {
/* 408 */     Node localNode1 = this.head; Object localObject = localNode1;
/*     */     while (true) { int i = ((Node)localObject).item != null ? 1 : 0;
/*     */       Node localNode2;
/* 410 */       if ((i != 0) || ((localNode2 = ((Node)localObject).next) == null)) {
/* 411 */         updateHead(localNode1, (Node)localObject);
/* 412 */         return i != 0 ? localObject : null;
/*     */       }
/* 414 */       if (localObject == localNode2) {
/*     */         break;
/*     */       }
/* 417 */       localObject = localNode2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 428 */     return first() == null;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 448 */     int i = 0;
/* 449 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode))
/* 450 */       if (localNode.item != null)
/*     */       {
/* 452 */         i++; if (i == 2147483647) break;
/*     */       }
/* 454 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 466 */     if (paramObject == null) return false;
/* 467 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/* 468 */       Object localObject = localNode.item;
/* 469 */       if ((localObject != null) && (paramObject.equals(localObject)))
/* 470 */         return true;
/*     */     }
/* 472 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 487 */     if (paramObject == null) return false;
/* 488 */     Object localObject1 = null;
/* 489 */     for (Node localNode1 = first(); localNode1 != null; localNode1 = succ(localNode1)) {
/* 490 */       Object localObject2 = localNode1.item;
/* 491 */       if ((localObject2 != null) && (paramObject.equals(localObject2)) && (localNode1.casItem(localObject2, null)))
/*     */       {
/* 494 */         Node localNode2 = succ(localNode1);
/* 495 */         if ((localObject1 != null) && (localNode2 != null))
/* 496 */           localObject1.casNext(localNode1, localNode2);
/* 497 */         return true;
/*     */       }
/* 499 */       localObject1 = localNode1;
/*     */     }
/* 501 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection)
/*     */   {
/* 517 */     if (paramCollection == this)
/*     */     {
/* 519 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 522 */     Object localObject1 = null; Object localObject2 = null;
/* 523 */     for (Object localObject3 = paramCollection.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = ((Iterator)localObject3).next();
/* 524 */       checkNotNull(localObject4);
/* 525 */       localNode = new Node(localObject4);
/* 526 */       if (localObject1 == null) {
/* 527 */         localObject1 = localObject2 = localNode;
/*     */       } else {
/* 529 */         localObject2.lazySetNext(localNode);
/* 530 */         localObject2 = localNode;
/*     */       }
/*     */     }
/*     */     Node localNode;
/* 533 */     if (localObject1 == null) {
/* 534 */       return false;
/*     */     }
/*     */ 
/* 537 */     localObject3 = this.tail; Object localObject4 = localObject3;
/*     */     while (true) { localNode = ((Node)localObject4).next;
/* 539 */       if (localNode == null)
/*     */       {
/* 541 */         if (((Node)localObject4).casNext(null, localObject1))
/*     */         {
/* 544 */           if (!casTail((Node)localObject3, localObject2))
/*     */           {
/* 547 */             localObject3 = this.tail;
/* 548 */             if (localObject2.next == null)
/* 549 */               casTail((Node)localObject3, localObject2);
/*     */           }
/* 551 */           return true;
/*     */         }
/*     */ 
/*     */       }
/* 555 */       else if (localObject4 == localNode)
/*     */       {
/* 560 */         localObject4 = localObject3 != (localObject3 = this.tail) ? localObject3 : this.head;
/*     */       }
/*     */       else
/* 563 */         localObject4 = (localObject4 != localObject3) && (localObject3 != (localObject3 = this.tail)) ? localObject3 : localNode;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 582 */     ArrayList localArrayList = new ArrayList();
/* 583 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/* 584 */       Object localObject = localNode.item;
/* 585 */       if (localObject != null)
/* 586 */         localArrayList.add(localObject);
/*     */     }
/* 588 */     return localArrayList.toArray();
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 630 */     int i = 0;
/*     */ 
/* 632 */     for (Node localNode1 = first(); (localNode1 != null) && (i < paramArrayOfT.length); localNode1 = succ(localNode1)) {
/* 633 */       localObject1 = localNode1.item;
/* 634 */       if (localObject1 != null)
/* 635 */         paramArrayOfT[(i++)] = localObject1;
/*     */     }
/* 637 */     if (localNode1 == null) {
/* 638 */       if (i < paramArrayOfT.length)
/* 639 */         paramArrayOfT[i] = null;
/* 640 */       return paramArrayOfT;
/*     */     }
/*     */ 
/* 644 */     Object localObject1 = new ArrayList();
/* 645 */     for (Node localNode2 = first(); localNode2 != null; localNode2 = succ(localNode2)) {
/* 646 */       Object localObject2 = localNode2.item;
/* 647 */       if (localObject2 != null)
/* 648 */         ((ArrayList)localObject1).add(localObject2);
/*     */     }
/* 650 */     return ((ArrayList)localObject1).toArray(paramArrayOfT);
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 667 */     return new Itr();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 760 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 763 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/* 764 */       Object localObject = localNode.item;
/* 765 */       if (localObject != null) {
/* 766 */         paramObjectOutputStream.writeObject(localObject);
/*     */       }
/*     */     }
/*     */ 
/* 770 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 779 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 782 */     Object localObject1 = null; Object localObject2 = null;
/*     */     Object localObject3;
/* 784 */     while ((localObject3 = paramObjectInputStream.readObject()) != null)
/*     */     {
/* 786 */       Node localNode = new Node(localObject3);
/* 787 */       if (localObject1 == null) {
/* 788 */         localObject1 = localObject2 = localNode;
/*     */       } else {
/* 790 */         ((Node)localObject2).lazySetNext(localNode);
/* 791 */         localObject2 = localNode;
/*     */       }
/*     */     }
/* 794 */     if (localObject1 == null)
/* 795 */       localObject1 = localObject2 = new Node(null);
/* 796 */     this.head = ((Node)localObject1);
/* 797 */     this.tail = ((Node)localObject2);
/*     */   }
/*     */ 
/*     */   private static void checkNotNull(Object paramObject)
/*     */   {
/* 806 */     if (paramObject == null)
/* 807 */       throw new NullPointerException();
/*     */   }
/*     */ 
/*     */   private boolean casTail(Node<E> paramNode1, Node<E> paramNode2) {
/* 811 */     return UNSAFE.compareAndSwapObject(this, tailOffset, paramNode1, paramNode2);
/*     */   }
/*     */ 
/*     */   private boolean casHead(Node<E> paramNode1, Node<E> paramNode2) {
/* 815 */     return UNSAFE.compareAndSwapObject(this, headOffset, paramNode1, paramNode2);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 825 */       UNSAFE = Unsafe.getUnsafe();
/* 826 */       ConcurrentLinkedQueue localConcurrentLinkedQueue = ConcurrentLinkedQueue.class;
/* 827 */       headOffset = UNSAFE.objectFieldOffset(localConcurrentLinkedQueue.getDeclaredField("head"));
/*     */ 
/* 829 */       tailOffset = UNSAFE.objectFieldOffset(localConcurrentLinkedQueue.getDeclaredField("tail"));
/*     */     }
/*     */     catch (Exception localException) {
/* 832 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Itr
/*     */     implements Iterator<E>
/*     */   {
/*     */     private ConcurrentLinkedQueue.Node<E> nextNode;
/*     */     private E nextItem;
/*     */     private ConcurrentLinkedQueue.Node<E> lastRet;
/*     */ 
/*     */     Itr()
/*     */     {
/* 690 */       advance();
/*     */     }
/*     */ 
/*     */     private E advance()
/*     */     {
/* 698 */       this.lastRet = this.nextNode;
/* 699 */       Object localObject1 = this.nextItem;
/*     */       Object localObject2;
/*     */       ConcurrentLinkedQueue.Node localNode1;
/* 702 */       if (this.nextNode == null) {
/* 703 */         localObject2 = ConcurrentLinkedQueue.this.first();
/* 704 */         localNode1 = null;
/*     */       } else {
/* 706 */         localNode1 = this.nextNode;
/* 707 */         localObject2 = ConcurrentLinkedQueue.this.succ(this.nextNode);
/*     */       }
/*     */       while (true)
/*     */       {
/* 711 */         if (localObject2 == null) {
/* 712 */           this.nextNode = null;
/* 713 */           this.nextItem = null;
/* 714 */           return localObject1;
/*     */         }
/* 716 */         Object localObject3 = ((ConcurrentLinkedQueue.Node)localObject2).item;
/* 717 */         if (localObject3 != null) {
/* 718 */           this.nextNode = ((ConcurrentLinkedQueue.Node)localObject2);
/* 719 */           this.nextItem = localObject3;
/* 720 */           return localObject1;
/*     */         }
/*     */ 
/* 723 */         ConcurrentLinkedQueue.Node localNode2 = ConcurrentLinkedQueue.this.succ((ConcurrentLinkedQueue.Node)localObject2);
/* 724 */         if ((localNode1 != null) && (localNode2 != null))
/* 725 */           localNode1.casNext((ConcurrentLinkedQueue.Node)localObject2, localNode2);
/* 726 */         localObject2 = localNode2;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 732 */       return this.nextNode != null;
/*     */     }
/*     */ 
/*     */     public E next() {
/* 736 */       if (this.nextNode == null) throw new NoSuchElementException();
/* 737 */       return advance();
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 741 */       ConcurrentLinkedQueue.Node localNode = this.lastRet;
/* 742 */       if (localNode == null) throw new IllegalStateException();
/*     */ 
/* 744 */       localNode.item = null;
/* 745 */       this.lastRet = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Node<E>
/*     */   {
/*     */     volatile E item;
/*     */     volatile Node<E> next;
/*     */     private static final Unsafe UNSAFE;
/*     */     private static final long itemOffset;
/*     */     private static final long nextOffset;
/*     */ 
/*     */     Node(E paramE)
/*     */     {
/* 187 */       UNSAFE.putObject(this, itemOffset, paramE);
/*     */     }
/*     */ 
/*     */     boolean casItem(E paramE1, E paramE2) {
/* 191 */       return UNSAFE.compareAndSwapObject(this, itemOffset, paramE1, paramE2);
/*     */     }
/*     */ 
/*     */     void lazySetNext(Node<E> paramNode) {
/* 195 */       UNSAFE.putOrderedObject(this, nextOffset, paramNode);
/*     */     }
/*     */ 
/*     */     boolean casNext(Node<E> paramNode1, Node<E> paramNode2) {
/* 199 */       return UNSAFE.compareAndSwapObject(this, nextOffset, paramNode1, paramNode2);
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/* 210 */         UNSAFE = Unsafe.getUnsafe();
/* 211 */         Node localNode = Node.class;
/* 212 */         itemOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("item"));
/*     */ 
/* 214 */         nextOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("next"));
/*     */       }
/*     */       catch (Exception localException) {
/* 217 */         throw new Error(localException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ConcurrentLinkedQueue
 * JD-Core Version:    0.6.2
 */