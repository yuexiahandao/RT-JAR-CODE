/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public class ConcurrentLinkedDeque<E> extends AbstractCollection<E>
/*      */   implements Deque<E>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 876323262645176354L;
/*      */   private volatile transient Node<E> head;
/*      */   private volatile transient Node<E> tail;
/* 1454 */   private static final Node<Object> PREV_TERMINATOR = new Node();
/*      */   private static final Node<Object> NEXT_TERMINATOR;
/*      */   private static final int HOPS = 2;
/*      */   private static final Unsafe UNSAFE;
/*      */   private static final long headOffset;
/*      */   private static final long tailOffset;
/*      */ 
/*      */   Node<E> prevTerminator()
/*      */   {
/*  284 */     return PREV_TERMINATOR;
/*      */   }
/*      */ 
/*      */   Node<E> nextTerminator()
/*      */   {
/*  289 */     return NEXT_TERMINATOR;
/*      */   }
/*      */ 
/*      */   private void linkFirst(E paramE)
/*      */   {
/*  355 */     checkNotNull(paramE);
/*  356 */     Node localNode1 = new Node(paramE);
/*      */ 
/*  360 */     Node localNode2 = this.head; Object localObject = localNode2;
/*      */     do
/*      */     {
/*      */       Node localNode3;
/*  361 */       while (((localNode3 = ((Node)localObject).prev) != null) && ((localNode3 = (localObject = localNode3).prev) != null))
/*      */       {
/*  365 */         localObject = localNode2 != (localNode2 = this.head) ? localNode2 : localNode3;
/*  366 */       }if (((Node)localObject).next == localObject)
/*      */       {
/*      */         break;
/*      */       }
/*  370 */       localNode1.lazySetNext((Node)localObject);
/*  371 */     }while (!((Node)localObject).casPrev(null, localNode1));
/*      */ 
/*  375 */     if (localObject != localNode2)
/*  376 */       casHead(localNode2, localNode1);
/*      */   }
/*      */ 
/*      */   private void linkLast(E paramE)
/*      */   {
/*  388 */     checkNotNull(paramE);
/*  389 */     Node localNode1 = new Node(paramE);
/*      */ 
/*  393 */     Node localNode2 = this.tail; Object localObject = localNode2;
/*      */     do
/*      */     {
/*      */       Node localNode3;
/*  394 */       while (((localNode3 = ((Node)localObject).next) != null) && ((localNode3 = (localObject = localNode3).next) != null))
/*      */       {
/*  398 */         localObject = localNode2 != (localNode2 = this.tail) ? localNode2 : localNode3;
/*  399 */       }if (((Node)localObject).prev == localObject)
/*      */       {
/*      */         break;
/*      */       }
/*  403 */       localNode1.lazySetPrev((Node)localObject);
/*  404 */     }while (!((Node)localObject).casNext(null, localNode1));
/*      */ 
/*  408 */     if (localObject != localNode2)
/*  409 */       casTail(localNode2, localNode1);
/*      */   }
/*      */ 
/*      */   void unlink(Node<E> paramNode)
/*      */   {
/*  428 */     Node localNode1 = paramNode.prev;
/*  429 */     Node localNode2 = paramNode.next;
/*  430 */     if (localNode1 == null) {
/*  431 */       unlinkFirst(paramNode, localNode2);
/*  432 */     } else if (localNode2 == null) {
/*  433 */       unlinkLast(paramNode, localNode1);
/*      */     }
/*      */     else
/*      */     {
/*  456 */       int k = 1;
/*      */       Object localObject1;
/*      */       int i;
/*      */       Node localNode3;
/*  459 */       for (Object localObject3 = localNode1; ; k++) {
/*  460 */         if (((Node)localObject3).item != null) {
/*  461 */           localObject1 = localObject3;
/*  462 */           i = 0;
/*  463 */           break;
/*      */         }
/*  465 */         localNode3 = ((Node)localObject3).prev;
/*  466 */         if (localNode3 == null) {
/*  467 */           if (((Node)localObject3).next == localObject3)
/*  468 */             return;
/*  469 */           localObject1 = localObject3;
/*  470 */           i = 1;
/*  471 */           break;
/*      */         }
/*  473 */         if (localObject3 == localNode3) {
/*  474 */           return;
/*      */         }
/*  476 */         localObject3 = localNode3;
/*      */       }
/*      */       Object localObject2;
/*      */       int j;
/*  480 */       for (localObject3 = localNode2; ; k++) {
/*  481 */         if (((Node)localObject3).item != null) {
/*  482 */           localObject2 = localObject3;
/*  483 */           j = 0;
/*  484 */           break;
/*      */         }
/*  486 */         localNode3 = ((Node)localObject3).next;
/*  487 */         if (localNode3 == null) {
/*  488 */           if (((Node)localObject3).prev == localObject3)
/*  489 */             return;
/*  490 */           localObject2 = localObject3;
/*  491 */           j = 1;
/*  492 */           break;
/*      */         }
/*  494 */         if (localObject3 == localNode3) {
/*  495 */           return;
/*      */         }
/*  497 */         localObject3 = localNode3;
/*      */       }
/*      */ 
/*  501 */       if ((k < 2) && ((i | j) != 0))
/*      */       {
/*  504 */         return;
/*      */       }
/*      */ 
/*  508 */       skipDeletedSuccessors(localObject1);
/*  509 */       skipDeletedPredecessors(localObject2);
/*      */ 
/*  512 */       if (((i | j) != 0) && (localObject1.next == localObject2) && (localObject2.prev == localObject1) && (i != 0 ? localObject1.prev == null : localObject1.item != null) && (j != 0 ? localObject2.next == null : localObject2.item != null))
/*      */       {
/*  520 */         updateHead();
/*  521 */         updateTail();
/*      */ 
/*  524 */         paramNode.lazySetPrev(i != 0 ? prevTerminator() : paramNode);
/*  525 */         paramNode.lazySetNext(j != 0 ? nextTerminator() : paramNode);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void unlinkFirst(Node<E> paramNode1, Node<E> paramNode2)
/*      */   {
/*  537 */     Object localObject1 = null; Object localObject2 = paramNode2;
/*      */     while (true)
/*      */     {
/*      */       Node localNode;
/*  538 */       if ((((Node)localObject2).item != null) || ((localNode = ((Node)localObject2).next) == null)) {
/*  539 */         if ((localObject1 != null) && (((Node)localObject2).prev != localObject2) && (paramNode1.casNext(paramNode2, (Node)localObject2))) {
/*  540 */           skipDeletedPredecessors((Node)localObject2);
/*  541 */           if ((paramNode1.prev == null) && ((((Node)localObject2).next == null) || (((Node)localObject2).item != null)) && (((Node)localObject2).prev == paramNode1))
/*      */           {
/*  545 */             updateHead();
/*  546 */             updateTail();
/*      */ 
/*  549 */             ((Node)localObject1).lazySetNext((Node)localObject1);
/*  550 */             ((Node)localObject1).lazySetPrev(prevTerminator());
/*      */           }
/*      */         }
/*  553 */         return;
/*      */       }
/*  555 */       if (localObject2 == localNode) {
/*  556 */         return;
/*      */       }
/*  558 */       localObject1 = localObject2;
/*  559 */       localObject2 = localNode;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void unlinkLast(Node<E> paramNode1, Node<E> paramNode2)
/*      */   {
/*  571 */     Object localObject1 = null; Object localObject2 = paramNode2;
/*      */     while (true)
/*      */     {
/*      */       Node localNode;
/*  572 */       if ((((Node)localObject2).item != null) || ((localNode = ((Node)localObject2).prev) == null)) {
/*  573 */         if ((localObject1 != null) && (((Node)localObject2).next != localObject2) && (paramNode1.casPrev(paramNode2, (Node)localObject2))) {
/*  574 */           skipDeletedSuccessors((Node)localObject2);
/*  575 */           if ((paramNode1.next == null) && ((((Node)localObject2).prev == null) || (((Node)localObject2).item != null)) && (((Node)localObject2).next == paramNode1))
/*      */           {
/*  579 */             updateHead();
/*  580 */             updateTail();
/*      */ 
/*  583 */             ((Node)localObject1).lazySetPrev((Node)localObject1);
/*  584 */             ((Node)localObject1).lazySetNext(nextTerminator());
/*      */           }
/*      */         }
/*  587 */         return;
/*      */       }
/*  589 */       if (localObject2 == localNode) {
/*  590 */         return;
/*      */       }
/*  592 */       localObject1 = localObject2;
/*  593 */       localObject2 = localNode;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void updateHead()
/*      */   {
/*      */     Node localNode1;
/*      */     Object localObject;
/*  609 */     if (((localNode1 = this.head).item == null) && ((localObject = localNode1.prev) != null))
/*      */       while (true)
/*      */       {
/*      */         Node localNode2;
/*  611 */         if (((localNode2 = ((Node)localObject).prev) == null) || ((localNode2 = (localObject = localNode2).prev) == null))
/*      */         {
/*  615 */           if (!casHead(localNode1, (Node)localObject)) break;
/*  616 */           return;
/*      */         }
/*      */ 
/*  620 */         if (localNode1 != this.head) {
/*      */           break;
/*      */         }
/*  623 */         localObject = localNode2;
/*      */       }
/*      */   }
/*      */ 
/*      */   private final void updateTail()
/*      */   {
/*      */     Node localNode1;
/*      */     Object localObject;
/*  639 */     if (((localNode1 = this.tail).item == null) && ((localObject = localNode1.next) != null))
/*      */       while (true)
/*      */       {
/*      */         Node localNode2;
/*  641 */         if (((localNode2 = ((Node)localObject).next) == null) || ((localNode2 = (localObject = localNode2).next) == null))
/*      */         {
/*  645 */           if (!casTail(localNode1, (Node)localObject)) break;
/*  646 */           return;
/*      */         }
/*      */ 
/*  650 */         if (localNode1 != this.tail) {
/*      */           break;
/*      */         }
/*  653 */         localObject = localNode2;
/*      */       }
/*      */   }
/*      */ 
/*      */   private void skipDeletedPredecessors(Node<E> paramNode)
/*      */   {
/*      */     label69: 
/*      */     do {
/*  661 */       Node localNode1 = paramNode.prev;
/*      */ 
/*  665 */       Object localObject = localNode1;
/*      */ 
/*  668 */       while (((Node)localObject).item == null)
/*      */       {
/*  670 */         Node localNode2 = ((Node)localObject).prev;
/*  671 */         if (localNode2 == null) {
/*  672 */           if (((Node)localObject).next != localObject) break;
/*  673 */           break label69;
/*      */         }
/*      */ 
/*  676 */         if (localObject == localNode2) {
/*      */           break label69;
/*      */         }
/*  679 */         localObject = localNode2;
/*      */       }
/*      */ 
/*  683 */       if ((localNode1 == localObject) || (paramNode.casPrev(localNode1, (Node)localObject)))
/*  684 */         return;
/*      */     }
/*  686 */     while ((paramNode.item != null) || (paramNode.next == null));
/*      */   }
/*      */ 
/*      */   private void skipDeletedSuccessors(Node<E> paramNode) {
/*      */     label69: 
/*      */     do {
/*  692 */       Node localNode1 = paramNode.next;
/*      */ 
/*  696 */       Object localObject = localNode1;
/*      */ 
/*  699 */       while (((Node)localObject).item == null)
/*      */       {
/*  701 */         Node localNode2 = ((Node)localObject).next;
/*  702 */         if (localNode2 == null) {
/*  703 */           if (((Node)localObject).prev != localObject) break;
/*  704 */           break label69;
/*      */         }
/*      */ 
/*  707 */         if (localObject == localNode2) {
/*      */           break label69;
/*      */         }
/*  710 */         localObject = localNode2;
/*      */       }
/*      */ 
/*  714 */       if ((localNode1 == localObject) || (paramNode.casNext(localNode1, (Node)localObject)))
/*  715 */         return;
/*      */     }
/*  717 */     while ((paramNode.item != null) || (paramNode.prev == null));
/*      */   }
/*      */ 
/*      */   final Node<E> succ(Node<E> paramNode)
/*      */   {
/*  727 */     Node localNode = paramNode.next;
/*  728 */     return paramNode == localNode ? first() : localNode;
/*      */   }
/*      */ 
/*      */   final Node<E> pred(Node<E> paramNode)
/*      */   {
/*  737 */     Node localNode = paramNode.prev;
/*  738 */     return paramNode == localNode ? last() : localNode;
/*      */   }
/*      */ 
/*      */   Node<E> first()
/*      */   {
/*      */     Node localNode1;
/*      */     Object localObject;
/*      */     do
/*      */     {
/*  750 */       localNode1 = this.head; localObject = localNode1;
/*      */       Node localNode2;
/*  751 */       while (((localNode2 = ((Node)localObject).prev) != null) && ((localNode2 = (localObject = localNode2).prev) != null))
/*      */       {
/*  755 */         localObject = localNode1 != (localNode1 = this.head) ? localNode1 : localNode2; } 
/*  756 */     }while ((localObject != localNode1) && (!casHead(localNode1, (Node)localObject)));
/*      */ 
/*  760 */     return localObject;
/*      */   }
/*      */ 
/*      */   Node<E> last()
/*      */   {
/*      */     Node localNode1;
/*      */     Object localObject;
/*      */     do
/*      */     {
/*  775 */       localNode1 = this.tail; localObject = localNode1;
/*      */       Node localNode2;
/*  776 */       while (((localNode2 = ((Node)localObject).next) != null) && ((localNode2 = (localObject = localNode2).next) != null))
/*      */       {
/*  780 */         localObject = localNode1 != (localNode1 = this.tail) ? localNode1 : localNode2; } 
/*  781 */     }while ((localObject != localNode1) && (!casTail(localNode1, (Node)localObject)));
/*      */ 
/*  785 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static void checkNotNull(Object paramObject)
/*      */   {
/*  799 */     if (paramObject == null)
/*  800 */       throw new NullPointerException();
/*      */   }
/*      */ 
/*      */   private E screenNullResult(E paramE)
/*      */   {
/*  811 */     if (paramE == null)
/*  812 */       throw new NoSuchElementException();
/*  813 */     return paramE;
/*      */   }
/*      */ 
/*      */   private ArrayList<E> toArrayList()
/*      */   {
/*  823 */     ArrayList localArrayList = new ArrayList();
/*  824 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/*  825 */       Object localObject = localNode.item;
/*  826 */       if (localObject != null)
/*  827 */         localArrayList.add(localObject);
/*      */     }
/*  829 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public ConcurrentLinkedDeque()
/*      */   {
/*  836 */     this.head = (this.tail = new Node(null));
/*      */   }
/*      */ 
/*      */   public ConcurrentLinkedDeque(Collection<? extends E> paramCollection)
/*      */   {
/*  850 */     Object localObject1 = null; Object localObject2 = null;
/*  851 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject3 = localIterator.next();
/*  852 */       checkNotNull(localObject3);
/*  853 */       Node localNode = new Node(localObject3);
/*  854 */       if (localObject1 == null) {
/*  855 */         localObject1 = localObject2 = localNode;
/*      */       } else {
/*  857 */         localObject2.lazySetNext(localNode);
/*  858 */         localNode.lazySetPrev(localObject2);
/*  859 */         localObject2 = localNode;
/*      */       }
/*      */     }
/*  862 */     initHeadTail(localObject1, localObject2);
/*      */   }
/*      */ 
/*      */   private void initHeadTail(Node<E> paramNode1, Node<E> paramNode2)
/*      */   {
/*  869 */     if (paramNode1 == paramNode2) {
/*  870 */       if (paramNode1 == null) {
/*  871 */         paramNode1 = paramNode2 = new Node(null);
/*      */       }
/*      */       else {
/*  874 */         Node localNode = new Node(null);
/*  875 */         paramNode2.lazySetNext(localNode);
/*  876 */         localNode.lazySetPrev(paramNode2);
/*  877 */         paramNode2 = localNode;
/*      */       }
/*      */     }
/*  880 */     this.head = paramNode1;
/*  881 */     this.tail = paramNode2;
/*      */   }
/*      */ 
/*      */   public void addFirst(E paramE)
/*      */   {
/*  892 */     linkFirst(paramE);
/*      */   }
/*      */ 
/*      */   public void addLast(E paramE)
/*      */   {
/*  905 */     linkLast(paramE);
/*      */   }
/*      */ 
/*      */   public boolean offerFirst(E paramE)
/*      */   {
/*  916 */     linkFirst(paramE);
/*  917 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean offerLast(E paramE)
/*      */   {
/*  930 */     linkLast(paramE);
/*  931 */     return true;
/*      */   }
/*      */ 
/*      */   public E peekFirst() {
/*  935 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/*  936 */       Object localObject = localNode.item;
/*  937 */       if (localObject != null)
/*  938 */         return localObject;
/*      */     }
/*  940 */     return null;
/*      */   }
/*      */ 
/*      */   public E peekLast() {
/*  944 */     for (Node localNode = last(); localNode != null; localNode = pred(localNode)) {
/*  945 */       Object localObject = localNode.item;
/*  946 */       if (localObject != null)
/*  947 */         return localObject;
/*      */     }
/*  949 */     return null;
/*      */   }
/*      */ 
/*      */   public E getFirst()
/*      */   {
/*  956 */     return screenNullResult(peekFirst());
/*      */   }
/*      */ 
/*      */   public E getLast()
/*      */   {
/*  963 */     return screenNullResult(peekLast());
/*      */   }
/*      */ 
/*      */   public E pollFirst() {
/*  967 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/*  968 */       Object localObject = localNode.item;
/*  969 */       if ((localObject != null) && (localNode.casItem(localObject, null))) {
/*  970 */         unlink(localNode);
/*  971 */         return localObject;
/*      */       }
/*      */     }
/*  974 */     return null;
/*      */   }
/*      */ 
/*      */   public E pollLast() {
/*  978 */     for (Node localNode = last(); localNode != null; localNode = pred(localNode)) {
/*  979 */       Object localObject = localNode.item;
/*  980 */       if ((localObject != null) && (localNode.casItem(localObject, null))) {
/*  981 */         unlink(localNode);
/*  982 */         return localObject;
/*      */       }
/*      */     }
/*  985 */     return null;
/*      */   }
/*      */ 
/*      */   public E removeFirst()
/*      */   {
/*  992 */     return screenNullResult(pollFirst());
/*      */   }
/*      */ 
/*      */   public E removeLast()
/*      */   {
/*  999 */     return screenNullResult(pollLast());
/*      */   }
/*      */ 
/*      */   public boolean offer(E paramE)
/*      */   {
/* 1012 */     return offerLast(paramE);
/*      */   }
/*      */ 
/*      */   public boolean add(E paramE)
/*      */   {
/* 1024 */     return offerLast(paramE);
/*      */   }
/*      */   public E poll() {
/* 1027 */     return pollFirst(); } 
/* 1028 */   public E remove() { return removeFirst(); } 
/* 1029 */   public E peek() { return peekFirst(); } 
/* 1030 */   public E element() { return getFirst(); } 
/* 1031 */   public void push(E paramE) { addFirst(paramE); } 
/* 1032 */   public E pop() { return removeFirst(); }
/*      */ 
/*      */ 
/*      */   public boolean removeFirstOccurrence(Object paramObject)
/*      */   {
/* 1044 */     checkNotNull(paramObject);
/* 1045 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/* 1046 */       Object localObject = localNode.item;
/* 1047 */       if ((localObject != null) && (paramObject.equals(localObject)) && (localNode.casItem(localObject, null))) {
/* 1048 */         unlink(localNode);
/* 1049 */         return true;
/*      */       }
/*      */     }
/* 1052 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean removeLastOccurrence(Object paramObject)
/*      */   {
/* 1065 */     checkNotNull(paramObject);
/* 1066 */     for (Node localNode = last(); localNode != null; localNode = pred(localNode)) {
/* 1067 */       Object localObject = localNode.item;
/* 1068 */       if ((localObject != null) && (paramObject.equals(localObject)) && (localNode.casItem(localObject, null))) {
/* 1069 */         unlink(localNode);
/* 1070 */         return true;
/*      */       }
/*      */     }
/* 1073 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/* 1084 */     if (paramObject == null) return false;
/* 1085 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/* 1086 */       Object localObject = localNode.item;
/* 1087 */       if ((localObject != null) && (paramObject.equals(localObject)))
/* 1088 */         return true;
/*      */     }
/* 1090 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1099 */     return peekFirst() == null;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 1119 */     int i = 0;
/* 1120 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode))
/* 1121 */       if (localNode.item != null)
/*      */       {
/* 1123 */         i++; if (i == 2147483647) break;
/*      */       }
/* 1125 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/* 1138 */     return removeFirstOccurrence(paramObject);
/*      */   }
/*      */ 
/*      */   public boolean addAll(Collection<? extends E> paramCollection)
/*      */   {
/* 1154 */     if (paramCollection == this)
/*      */     {
/* 1156 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/* 1159 */     Object localObject1 = null; Object localObject2 = null;
/* 1160 */     for (Object localObject3 = paramCollection.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = ((Iterator)localObject3).next();
/* 1161 */       checkNotNull(localObject4);
/* 1162 */       localNode = new Node(localObject4);
/* 1163 */       if (localObject1 == null) {
/* 1164 */         localObject1 = localObject2 = localNode;
/*      */       } else {
/* 1166 */         localObject2.lazySetNext(localNode);
/* 1167 */         localNode.lazySetPrev(localObject2);
/* 1168 */         localObject2 = localNode;
/*      */       }
/*      */     }
/*      */     Node localNode;
/* 1171 */     if (localObject1 == null) {
/* 1172 */       return false;
/*      */     }
/*      */ 
/* 1177 */     localObject3 = this.tail; Object localObject4 = localObject3;
/*      */     do { while (((localNode = localObject4.next) != null) && ((localNode = (localObject4 = localNode).next) != null))
/*      */       {
/* 1182 */         localObject4 = localObject3 != (localObject3 = this.tail) ? localObject3 : localNode;
/* 1183 */       }if (localObject4.prev == localObject4)
/*      */       {
/*      */         break;
/*      */       }
/* 1187 */       localObject1.lazySetPrev(localObject4); }
/* 1188 */     while (!localObject4.casNext(null, localObject1));
/*      */ 
/* 1191 */     if (!casTail((Node)localObject3, localObject2))
/*      */     {
/* 1194 */       localObject3 = this.tail;
/* 1195 */       if (localObject2.next == null)
/* 1196 */         casTail((Node)localObject3, localObject2);
/*      */     }
/* 1198 */     return true;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/* 1209 */     while (pollFirst() != null);
/*      */   }
/*      */ 
/*      */   public Object[] toArray()
/*      */   {
/* 1227 */     return toArrayList().toArray();
/*      */   }
/*      */ 
/*      */   public <T> T[] toArray(T[] paramArrayOfT)
/*      */   {
/* 1269 */     return toArrayList().toArray(paramArrayOfT);
/*      */   }
/*      */ 
/*      */   public Iterator<E> iterator()
/*      */   {
/* 1286 */     return new Itr(null);
/*      */   }
/*      */ 
/*      */   public Iterator<E> descendingIterator()
/*      */   {
/* 1304 */     return new DescendingItr(null);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1401 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1404 */     for (Node localNode = first(); localNode != null; localNode = succ(localNode)) {
/* 1405 */       Object localObject = localNode.item;
/* 1406 */       if (localObject != null) {
/* 1407 */         paramObjectOutputStream.writeObject(localObject);
/*      */       }
/*      */     }
/*      */ 
/* 1411 */     paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1420 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1423 */     Object localObject1 = null; Object localObject2 = null;
/*      */     Object localObject3;
/* 1425 */     while ((localObject3 = paramObjectInputStream.readObject()) != null)
/*      */     {
/* 1427 */       Node localNode = new Node(localObject3);
/* 1428 */       if (localObject1 == null) {
/* 1429 */         localObject1 = localObject2 = localNode;
/*      */       } else {
/* 1431 */         localObject2.lazySetNext(localNode);
/* 1432 */         localNode.lazySetPrev(localObject2);
/* 1433 */         localObject2 = localNode;
/*      */       }
/*      */     }
/* 1436 */     initHeadTail(localObject1, localObject2);
/*      */   }
/*      */ 
/*      */   private boolean casHead(Node<E> paramNode1, Node<E> paramNode2)
/*      */   {
/* 1441 */     return UNSAFE.compareAndSwapObject(this, headOffset, paramNode1, paramNode2);
/*      */   }
/*      */ 
/*      */   private boolean casTail(Node<E> paramNode1, Node<E> paramNode2) {
/* 1445 */     return UNSAFE.compareAndSwapObject(this, tailOffset, paramNode1, paramNode2);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1455 */     PREV_TERMINATOR.next = PREV_TERMINATOR;
/* 1456 */     NEXT_TERMINATOR = new Node();
/* 1457 */     NEXT_TERMINATOR.prev = NEXT_TERMINATOR;
/*      */     try {
/* 1459 */       UNSAFE = Unsafe.getUnsafe();
/* 1460 */       ConcurrentLinkedDeque localConcurrentLinkedDeque = ConcurrentLinkedDeque.class;
/* 1461 */       headOffset = UNSAFE.objectFieldOffset(localConcurrentLinkedDeque.getDeclaredField("head"));
/*      */ 
/* 1463 */       tailOffset = UNSAFE.objectFieldOffset(localConcurrentLinkedDeque.getDeclaredField("tail"));
/*      */     }
/*      */     catch (Exception localException) {
/* 1466 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private abstract class AbstractItr
/*      */     implements Iterator<E>
/*      */   {
/*      */     private ConcurrentLinkedDeque.Node<E> nextNode;
/*      */     private E nextItem;
/*      */     private ConcurrentLinkedDeque.Node<E> lastRet;
/*      */ 
/*      */     abstract ConcurrentLinkedDeque.Node<E> startNode();
/*      */ 
/*      */     abstract ConcurrentLinkedDeque.Node<E> nextNode(ConcurrentLinkedDeque.Node<E> paramNode);
/*      */ 
/*      */     AbstractItr()
/*      */     {
/* 1331 */       advance();
/*      */     }
/*      */ 
/*      */     private void advance()
/*      */     {
/* 1339 */       this.lastRet = this.nextNode;
/*      */ 
/* 1341 */       ConcurrentLinkedDeque.Node localNode = this.nextNode == null ? startNode() : nextNode(this.nextNode);
/* 1342 */       for (; ; localNode = nextNode(localNode)) {
/* 1343 */         if (localNode == null)
/*      */         {
/* 1345 */           this.nextNode = null;
/* 1346 */           this.nextItem = null;
/* 1347 */           break;
/*      */         }
/* 1349 */         Object localObject = localNode.item;
/* 1350 */         if (localObject != null) {
/* 1351 */           this.nextNode = localNode;
/* 1352 */           this.nextItem = localObject;
/* 1353 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/* 1359 */       return this.nextItem != null;
/*      */     }
/*      */ 
/*      */     public E next() {
/* 1363 */       Object localObject = this.nextItem;
/* 1364 */       if (localObject == null) throw new NoSuchElementException();
/* 1365 */       advance();
/* 1366 */       return localObject;
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 1370 */       ConcurrentLinkedDeque.Node localNode = this.lastRet;
/* 1371 */       if (localNode == null) throw new IllegalStateException();
/* 1372 */       localNode.item = null;
/* 1373 */       ConcurrentLinkedDeque.this.unlink(localNode);
/* 1374 */       this.lastRet = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DescendingItr extends ConcurrentLinkedDeque<E>.AbstractItr
/*      */   {
/*      */     private DescendingItr()
/*      */     {
/* 1385 */       super(); } 
/* 1386 */     ConcurrentLinkedDeque.Node<E> startNode() { return ConcurrentLinkedDeque.this.last(); } 
/* 1387 */     ConcurrentLinkedDeque.Node<E> nextNode(ConcurrentLinkedDeque.Node<E> paramNode) { return ConcurrentLinkedDeque.this.pred(paramNode); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private class Itr extends ConcurrentLinkedDeque<E>.AbstractItr
/*      */   {
/*      */     private Itr()
/*      */     {
/* 1379 */       super(); } 
/* 1380 */     ConcurrentLinkedDeque.Node<E> startNode() { return ConcurrentLinkedDeque.this.first(); } 
/* 1381 */     ConcurrentLinkedDeque.Node<E> nextNode(ConcurrentLinkedDeque.Node<E> paramNode) { return ConcurrentLinkedDeque.this.succ(paramNode); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class Node<E>
/*      */   {
/*      */     volatile Node<E> prev;
/*      */     volatile E item;
/*      */     volatile Node<E> next;
/*      */     private static final Unsafe UNSAFE;
/*      */     private static final long prevOffset;
/*      */     private static final long itemOffset;
/*      */     private static final long nextOffset;
/*      */ 
/*      */     Node()
/*      */     {
/*      */     }
/*      */ 
/*      */     Node(E paramE)
/*      */     {
/*  305 */       UNSAFE.putObject(this, itemOffset, paramE);
/*      */     }
/*      */ 
/*      */     boolean casItem(E paramE1, E paramE2) {
/*  309 */       return UNSAFE.compareAndSwapObject(this, itemOffset, paramE1, paramE2);
/*      */     }
/*      */ 
/*      */     void lazySetNext(Node<E> paramNode) {
/*  313 */       UNSAFE.putOrderedObject(this, nextOffset, paramNode);
/*      */     }
/*      */ 
/*      */     boolean casNext(Node<E> paramNode1, Node<E> paramNode2) {
/*  317 */       return UNSAFE.compareAndSwapObject(this, nextOffset, paramNode1, paramNode2);
/*      */     }
/*      */ 
/*      */     void lazySetPrev(Node<E> paramNode) {
/*  321 */       UNSAFE.putOrderedObject(this, prevOffset, paramNode);
/*      */     }
/*      */ 
/*      */     boolean casPrev(Node<E> paramNode1, Node<E> paramNode2) {
/*  325 */       return UNSAFE.compareAndSwapObject(this, prevOffset, paramNode1, paramNode2);
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/*  337 */         UNSAFE = Unsafe.getUnsafe();
/*  338 */         Node localNode = Node.class;
/*  339 */         prevOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("prev"));
/*      */ 
/*  341 */         itemOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("item"));
/*      */ 
/*  343 */         nextOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("next"));
/*      */       }
/*      */       catch (Exception localException) {
/*  346 */         throw new Error(localException);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ConcurrentLinkedDeque
 * JD-Core Version:    0.6.2
 */