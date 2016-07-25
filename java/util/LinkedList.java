/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ 
/*      */ public class LinkedList<E> extends AbstractSequentialList<E>
/*      */   implements List<E>, Deque<E>, Cloneable, Serializable
/*      */ {
/*   85 */   transient int size = 0;
/*      */   transient Node<E> first;
/*      */   transient Node<E> last;
/*      */   private static final long serialVersionUID = 876323262645176354L;
/*      */ 
/*      */   public LinkedList()
/*      */   {
/*      */   }
/*      */ 
/*      */   public LinkedList(Collection<? extends E> paramCollection)
/*      */   {
/*  116 */     this();
/*  117 */     addAll(paramCollection);
/*      */   }
/*      */ 
/*      */   private void linkFirst(E paramE)
/*      */   {
/*  124 */     Node localNode1 = this.first;
/*  125 */     Node localNode2 = new Node(null, paramE, localNode1);
/*  126 */     this.first = localNode2;
/*  127 */     if (localNode1 == null)
/*  128 */       this.last = localNode2;
/*      */     else
/*  130 */       localNode1.prev = localNode2;
/*  131 */     this.size += 1;
/*  132 */     this.modCount += 1;
/*      */   }
/*      */ 
/*      */   void linkLast(E paramE)
/*      */   {
/*  139 */     Node localNode1 = this.last;
/*  140 */     Node localNode2 = new Node(localNode1, paramE, null);
/*  141 */     this.last = localNode2;
/*  142 */     if (localNode1 == null)
/*  143 */       this.first = localNode2;
/*      */     else
/*  145 */       localNode1.next = localNode2;
/*  146 */     this.size += 1;
/*  147 */     this.modCount += 1;
/*      */   }
/*      */ 
/*      */   void linkBefore(E paramE, Node<E> paramNode)
/*      */   {
/*  155 */     Node localNode1 = paramNode.prev;
/*  156 */     Node localNode2 = new Node(localNode1, paramE, paramNode);
/*  157 */     paramNode.prev = localNode2;
/*  158 */     if (localNode1 == null)
/*  159 */       this.first = localNode2;
/*      */     else
/*  161 */       localNode1.next = localNode2;
/*  162 */     this.size += 1;
/*  163 */     this.modCount += 1;
/*      */   }
/*      */ 
/*      */   private E unlinkFirst(Node<E> paramNode)
/*      */   {
/*  171 */     Object localObject = paramNode.item;
/*  172 */     Node localNode = paramNode.next;
/*  173 */     paramNode.item = null;
/*  174 */     paramNode.next = null;
/*  175 */     this.first = localNode;
/*  176 */     if (localNode == null)
/*  177 */       this.last = null;
/*      */     else
/*  179 */       localNode.prev = null;
/*  180 */     this.size -= 1;
/*  181 */     this.modCount += 1;
/*  182 */     return localObject;
/*      */   }
/*      */ 
/*      */   private E unlinkLast(Node<E> paramNode)
/*      */   {
/*  190 */     Object localObject = paramNode.item;
/*  191 */     Node localNode = paramNode.prev;
/*  192 */     paramNode.item = null;
/*  193 */     paramNode.prev = null;
/*  194 */     this.last = localNode;
/*  195 */     if (localNode == null)
/*  196 */       this.first = null;
/*      */     else
/*  198 */       localNode.next = null;
/*  199 */     this.size -= 1;
/*  200 */     this.modCount += 1;
/*  201 */     return localObject;
/*      */   }
/*      */ 
/*      */   E unlink(Node<E> paramNode)
/*      */   {
/*  209 */     Object localObject = paramNode.item;
/*  210 */     Node localNode1 = paramNode.next;
/*  211 */     Node localNode2 = paramNode.prev;
/*      */ 
/*  213 */     if (localNode2 == null) {
/*  214 */       this.first = localNode1;
/*      */     } else {
/*  216 */       localNode2.next = localNode1;
/*  217 */       paramNode.prev = null;
/*      */     }
/*      */ 
/*  220 */     if (localNode1 == null) {
/*  221 */       this.last = localNode2;
/*      */     } else {
/*  223 */       localNode1.prev = localNode2;
/*  224 */       paramNode.next = null;
/*      */     }
/*      */ 
/*  227 */     paramNode.item = null;
/*  228 */     this.size -= 1;
/*  229 */     this.modCount += 1;
/*  230 */     return localObject;
/*      */   }
/*      */ 
/*      */   public E getFirst()
/*      */   {
/*  240 */     Node localNode = this.first;
/*  241 */     if (localNode == null)
/*  242 */       throw new NoSuchElementException();
/*  243 */     return localNode.item;
/*      */   }
/*      */ 
/*      */   public E getLast()
/*      */   {
/*  253 */     Node localNode = this.last;
/*  254 */     if (localNode == null)
/*  255 */       throw new NoSuchElementException();
/*  256 */     return localNode.item;
/*      */   }
/*      */ 
/*      */   public E removeFirst()
/*      */   {
/*  266 */     Node localNode = this.first;
/*  267 */     if (localNode == null)
/*  268 */       throw new NoSuchElementException();
/*  269 */     return unlinkFirst(localNode);
/*      */   }
/*      */ 
/*      */   public E removeLast()
/*      */   {
/*  279 */     Node localNode = this.last;
/*  280 */     if (localNode == null)
/*  281 */       throw new NoSuchElementException();
/*  282 */     return unlinkLast(localNode);
/*      */   }
/*      */ 
/*      */   public void addFirst(E paramE)
/*      */   {
/*  291 */     linkFirst(paramE);
/*      */   }
/*      */ 
/*      */   public void addLast(E paramE)
/*      */   {
/*  302 */     linkLast(paramE);
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/*  315 */     return indexOf(paramObject) != -1;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  324 */     return this.size;
/*      */   }
/*      */ 
/*      */   public boolean add(E paramE)
/*      */   {
/*  336 */     linkLast(paramE);
/*  337 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/*      */     Node localNode;
/*  354 */     if (paramObject == null) {
/*  355 */       for (localNode = this.first; localNode != null; localNode = localNode.next)
/*  356 */         if (localNode.item == null) {
/*  357 */           unlink(localNode);
/*  358 */           return true;
/*      */         }
/*      */     }
/*      */     else {
/*  362 */       for (localNode = this.first; localNode != null; localNode = localNode.next) {
/*  363 */         if (paramObject.equals(localNode.item)) {
/*  364 */           unlink(localNode);
/*  365 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  369 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean addAll(Collection<? extends E> paramCollection)
/*      */   {
/*  385 */     return addAll(this.size, paramCollection);
/*      */   }
/*      */ 
/*      */   public boolean addAll(int paramInt, Collection<? extends E> paramCollection)
/*      */   {
/*  404 */     checkPositionIndex(paramInt);
/*      */ 
/*  406 */     Object[] arrayOfObject1 = paramCollection.toArray();
/*  407 */     int i = arrayOfObject1.length;
/*  408 */     if (i == 0)
/*  409 */       return false;
/*      */     Node localNode1;
/*      */     Object localObject1;
/*  412 */     if (paramInt == this.size) {
/*  413 */       localNode1 = null;
/*  414 */       localObject1 = this.last;
/*      */     } else {
/*  416 */       localNode1 = node(paramInt);
/*  417 */       localObject1 = localNode1.prev;
/*      */     }
/*      */ 
/*  420 */     for (Object localObject2 : arrayOfObject1) {
/*  421 */       Object localObject3 = localObject2;
/*  422 */       Node localNode2 = new Node((Node)localObject1, localObject3, null);
/*  423 */       if (localObject1 == null)
/*  424 */         this.first = localNode2;
/*      */       else
/*  426 */         ((Node)localObject1).next = localNode2;
/*  427 */       localObject1 = localNode2;
/*      */     }
/*      */ 
/*  430 */     if (localNode1 == null) {
/*  431 */       this.last = ((Node)localObject1);
/*      */     } else {
/*  433 */       ((Node)localObject1).next = localNode1;
/*  434 */       localNode1.prev = ((Node)localObject1);
/*      */     }
/*      */ 
/*  437 */     this.size += i;
/*  438 */     this.modCount += 1;
/*  439 */     return true;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  451 */     for (Object localObject = this.first; localObject != null; ) {
/*  452 */       Node localNode = ((Node)localObject).next;
/*  453 */       ((Node)localObject).item = null;
/*  454 */       ((Node)localObject).next = null;
/*  455 */       ((Node)localObject).prev = null;
/*  456 */       localObject = localNode;
/*      */     }
/*  458 */     this.first = (this.last = null);
/*  459 */     this.size = 0;
/*  460 */     this.modCount += 1;
/*      */   }
/*      */ 
/*      */   public E get(int paramInt)
/*      */   {
/*  474 */     checkElementIndex(paramInt);
/*  475 */     return node(paramInt).item;
/*      */   }
/*      */ 
/*      */   public E set(int paramInt, E paramE)
/*      */   {
/*  488 */     checkElementIndex(paramInt);
/*  489 */     Node localNode = node(paramInt);
/*  490 */     Object localObject = localNode.item;
/*  491 */     localNode.item = paramE;
/*  492 */     return localObject;
/*      */   }
/*      */ 
/*      */   public void add(int paramInt, E paramE)
/*      */   {
/*  505 */     checkPositionIndex(paramInt);
/*      */ 
/*  507 */     if (paramInt == this.size)
/*  508 */       linkLast(paramE);
/*      */     else
/*  510 */       linkBefore(paramE, node(paramInt));
/*      */   }
/*      */ 
/*      */   public E remove(int paramInt)
/*      */   {
/*  523 */     checkElementIndex(paramInt);
/*  524 */     return unlink(node(paramInt));
/*      */   }
/*      */ 
/*      */   private boolean isElementIndex(int paramInt)
/*      */   {
/*  531 */     return (paramInt >= 0) && (paramInt < this.size);
/*      */   }
/*      */ 
/*      */   private boolean isPositionIndex(int paramInt)
/*      */   {
/*  539 */     return (paramInt >= 0) && (paramInt <= this.size);
/*      */   }
/*      */ 
/*      */   private String outOfBoundsMsg(int paramInt)
/*      */   {
/*  548 */     return "Index: " + paramInt + ", Size: " + this.size;
/*      */   }
/*      */ 
/*      */   private void checkElementIndex(int paramInt) {
/*  552 */     if (!isElementIndex(paramInt))
/*  553 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*      */   }
/*      */ 
/*      */   private void checkPositionIndex(int paramInt) {
/*  557 */     if (!isPositionIndex(paramInt))
/*  558 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*      */   }
/*      */ 
/*      */   Node<E> node(int paramInt)
/*      */   {
/*  567 */     if (paramInt < this.size >> 1) {
/*  568 */       localNode = this.first;
/*  569 */       for (i = 0; i < paramInt; i++)
/*  570 */         localNode = localNode.next;
/*  571 */       return localNode;
/*      */     }
/*  573 */     Node localNode = this.last;
/*  574 */     for (int i = this.size - 1; i > paramInt; i--)
/*  575 */       localNode = localNode.prev;
/*  576 */     return localNode;
/*      */   }
/*      */ 
/*      */   public int indexOf(Object paramObject)
/*      */   {
/*  594 */     int i = 0;
/*      */     Node localNode;
/*  595 */     if (paramObject == null)
/*  596 */       for (localNode = this.first; localNode != null; localNode = localNode.next) {
/*  597 */         if (localNode.item == null)
/*  598 */           return i;
/*  599 */         i++;
/*      */       }
/*      */     else {
/*  602 */       for (localNode = this.first; localNode != null; localNode = localNode.next) {
/*  603 */         if (paramObject.equals(localNode.item))
/*  604 */           return i;
/*  605 */         i++;
/*      */       }
/*      */     }
/*  608 */     return -1;
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(Object paramObject)
/*      */   {
/*  623 */     int i = this.size;
/*      */     Node localNode;
/*  624 */     if (paramObject == null)
/*  625 */       for (localNode = this.last; localNode != null; localNode = localNode.prev) {
/*  626 */         i--;
/*  627 */         if (localNode.item == null)
/*  628 */           return i;
/*      */       }
/*      */     else {
/*  631 */       for (localNode = this.last; localNode != null; localNode = localNode.prev) {
/*  632 */         i--;
/*  633 */         if (paramObject.equals(localNode.item))
/*  634 */           return i;
/*      */       }
/*      */     }
/*  637 */     return -1;
/*      */   }
/*      */ 
/*      */   public E peek()
/*      */   {
/*  649 */     Node localNode = this.first;
/*  650 */     return localNode == null ? null : localNode.item;
/*      */   }
/*      */ 
/*      */   public E element()
/*      */   {
/*  661 */     return getFirst();
/*      */   }
/*      */ 
/*      */   public E poll()
/*      */   {
/*  671 */     Node localNode = this.first;
/*  672 */     return localNode == null ? null : unlinkFirst(localNode);
/*      */   }
/*      */ 
/*      */   public E remove()
/*      */   {
/*  683 */     return removeFirst();
/*      */   }
/*      */ 
/*      */   public boolean offer(E paramE)
/*      */   {
/*  694 */     return add(paramE);
/*      */   }
/*      */ 
/*      */   public boolean offerFirst(E paramE)
/*      */   {
/*  706 */     addFirst(paramE);
/*  707 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean offerLast(E paramE)
/*      */   {
/*  718 */     addLast(paramE);
/*  719 */     return true;
/*      */   }
/*      */ 
/*      */   public E peekFirst()
/*      */   {
/*  731 */     Node localNode = this.first;
/*  732 */     return localNode == null ? null : localNode.item;
/*      */   }
/*      */ 
/*      */   public E peekLast()
/*      */   {
/*  744 */     Node localNode = this.last;
/*  745 */     return localNode == null ? null : localNode.item;
/*      */   }
/*      */ 
/*      */   public E pollFirst()
/*      */   {
/*  757 */     Node localNode = this.first;
/*  758 */     return localNode == null ? null : unlinkFirst(localNode);
/*      */   }
/*      */ 
/*      */   public E pollLast()
/*      */   {
/*  770 */     Node localNode = this.last;
/*  771 */     return localNode == null ? null : unlinkLast(localNode);
/*      */   }
/*      */ 
/*      */   public void push(E paramE)
/*      */   {
/*  784 */     addFirst(paramE);
/*      */   }
/*      */ 
/*      */   public E pop()
/*      */   {
/*  799 */     return removeFirst();
/*      */   }
/*      */ 
/*      */   public boolean removeFirstOccurrence(Object paramObject)
/*      */   {
/*  812 */     return remove(paramObject);
/*      */   }
/*      */ 
/*      */   public boolean removeLastOccurrence(Object paramObject)
/*      */   {
/*      */     Node localNode;
/*  825 */     if (paramObject == null) {
/*  826 */       for (localNode = this.last; localNode != null; localNode = localNode.prev)
/*  827 */         if (localNode.item == null) {
/*  828 */           unlink(localNode);
/*  829 */           return true;
/*      */         }
/*      */     }
/*      */     else {
/*  833 */       for (localNode = this.last; localNode != null; localNode = localNode.prev) {
/*  834 */         if (paramObject.equals(localNode.item)) {
/*  835 */           unlink(localNode);
/*  836 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  840 */     return false;
/*      */   }
/*      */ 
/*      */   public ListIterator<E> listIterator(int paramInt)
/*      */   {
/*  865 */     checkPositionIndex(paramInt);
/*  866 */     return new ListItr(paramInt);
/*      */   }
/*      */ 
/*      */   public Iterator<E> descendingIterator()
/*      */   {
/*  973 */     return new DescendingIterator(null);
/*      */   }
/*      */ 
/*      */   private LinkedList<E> superClone()
/*      */   {
/*      */     try
/*      */     {
/*  995 */       return (LinkedList)super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  997 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 1008 */     LinkedList localLinkedList = superClone();
/*      */ 
/* 1011 */     localLinkedList.first = (localLinkedList.last = null);
/* 1012 */     localLinkedList.size = 0;
/* 1013 */     localLinkedList.modCount = 0;
/*      */ 
/* 1016 */     for (Node localNode = this.first; localNode != null; localNode = localNode.next) {
/* 1017 */       localLinkedList.add(localNode.item);
/*      */     }
/* 1019 */     return localLinkedList;
/*      */   }
/*      */ 
/*      */   public Object[] toArray()
/*      */   {
/* 1037 */     Object[] arrayOfObject = new Object[this.size];
/* 1038 */     int i = 0;
/* 1039 */     for (Node localNode = this.first; localNode != null; localNode = localNode.next)
/* 1040 */       arrayOfObject[(i++)] = localNode.item;
/* 1041 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public <T> T[] toArray(T[] paramArrayOfT)
/*      */   {
/* 1084 */     if (paramArrayOfT.length < this.size) {
/* 1085 */       paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), this.size);
/*      */     }
/* 1087 */     int i = 0;
/* 1088 */     T[] arrayOfT = paramArrayOfT;
/* 1089 */     for (Node localNode = this.first; localNode != null; localNode = localNode.next) {
/* 1090 */       arrayOfT[(i++)] = localNode.item;
/*      */     }
/* 1092 */     if (paramArrayOfT.length > this.size) {
/* 1093 */       paramArrayOfT[this.size] = null;
/*      */     }
/* 1095 */     return paramArrayOfT;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1111 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1114 */     paramObjectOutputStream.writeInt(this.size);
/*      */ 
/* 1117 */     for (Node localNode = this.first; localNode != null; localNode = localNode.next)
/* 1118 */       paramObjectOutputStream.writeObject(localNode.item);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1129 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1132 */     int i = paramObjectInputStream.readInt();
/*      */ 
/* 1135 */     for (int j = 0; j < i; j++)
/* 1136 */       linkLast(paramObjectInputStream.readObject());
/*      */   }
/*      */ 
/*      */   private class DescendingIterator
/*      */     implements Iterator<E>
/*      */   {
/*  980 */     private final LinkedList<E>.ListItr itr = new LinkedList.ListItr(LinkedList.this, LinkedList.this.size());
/*      */ 
/*      */     private DescendingIterator() {  } 
/*  982 */     public boolean hasNext() { return this.itr.hasPrevious(); }
/*      */ 
/*      */     public E next() {
/*  985 */       return this.itr.previous();
/*      */     }
/*      */     public void remove() {
/*  988 */       this.itr.remove();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ListItr
/*      */     implements ListIterator<E>
/*      */   {
/*  870 */     private LinkedList.Node<E> lastReturned = null;
/*      */     private LinkedList.Node<E> next;
/*      */     private int nextIndex;
/*  873 */     private int expectedModCount = LinkedList.this.modCount;
/*      */ 
/*      */     ListItr(int arg2)
/*      */     {
/*      */       int i;
/*  877 */       this.next = (i == LinkedList.this.size ? null : LinkedList.this.node(i));
/*  878 */       this.nextIndex = i;
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/*  882 */       return this.nextIndex < LinkedList.this.size;
/*      */     }
/*      */ 
/*      */     public E next() {
/*  886 */       checkForComodification();
/*  887 */       if (!hasNext()) {
/*  888 */         throw new NoSuchElementException();
/*      */       }
/*  890 */       this.lastReturned = this.next;
/*  891 */       this.next = this.next.next;
/*  892 */       this.nextIndex += 1;
/*  893 */       return this.lastReturned.item;
/*      */     }
/*      */ 
/*      */     public boolean hasPrevious() {
/*  897 */       return this.nextIndex > 0;
/*      */     }
/*      */ 
/*      */     public E previous() {
/*  901 */       checkForComodification();
/*  902 */       if (!hasPrevious()) {
/*  903 */         throw new NoSuchElementException();
/*      */       }
/*  905 */       this.lastReturned = (this.next = this.next == null ? LinkedList.this.last : this.next.prev);
/*  906 */       this.nextIndex -= 1;
/*  907 */       return this.lastReturned.item;
/*      */     }
/*      */ 
/*      */     public int nextIndex() {
/*  911 */       return this.nextIndex;
/*      */     }
/*      */ 
/*      */     public int previousIndex() {
/*  915 */       return this.nextIndex - 1;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  919 */       checkForComodification();
/*  920 */       if (this.lastReturned == null) {
/*  921 */         throw new IllegalStateException();
/*      */       }
/*  923 */       LinkedList.Node localNode = this.lastReturned.next;
/*  924 */       LinkedList.this.unlink(this.lastReturned);
/*  925 */       if (this.next == this.lastReturned)
/*  926 */         this.next = localNode;
/*      */       else
/*  928 */         this.nextIndex -= 1;
/*  929 */       this.lastReturned = null;
/*  930 */       this.expectedModCount += 1;
/*      */     }
/*      */ 
/*      */     public void set(E paramE) {
/*  934 */       if (this.lastReturned == null)
/*  935 */         throw new IllegalStateException();
/*  936 */       checkForComodification();
/*  937 */       this.lastReturned.item = paramE;
/*      */     }
/*      */ 
/*      */     public void add(E paramE) {
/*  941 */       checkForComodification();
/*  942 */       this.lastReturned = null;
/*  943 */       if (this.next == null)
/*  944 */         LinkedList.this.linkLast(paramE);
/*      */       else
/*  946 */         LinkedList.this.linkBefore(paramE, this.next);
/*  947 */       this.nextIndex += 1;
/*  948 */       this.expectedModCount += 1;
/*      */     }
/*      */ 
/*      */     final void checkForComodification() {
/*  952 */       if (LinkedList.this.modCount != this.expectedModCount)
/*  953 */         throw new ConcurrentModificationException(); 
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Node<E> { E item;
/*      */     Node<E> next;
/*      */     Node<E> prev;
/*      */ 
/*  963 */     Node(Node<E> paramNode1, E paramE, Node<E> paramNode2) { this.item = paramE;
/*  964 */       this.next = paramNode2;
/*  965 */       this.prev = paramNode1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.LinkedList
 * JD-Core Version:    0.6.2
 */