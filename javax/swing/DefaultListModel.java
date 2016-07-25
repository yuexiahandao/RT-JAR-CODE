/*     */ package javax.swing;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class DefaultListModel<E> extends AbstractListModel<E>
/*     */ {
/*  57 */   private Vector<E> delegate = new Vector();
/*     */ 
/*     */   public int getSize()
/*     */   {
/*  71 */     return this.delegate.size();
/*     */   }
/*     */ 
/*     */   public E getElementAt(int paramInt)
/*     */   {
/*  89 */     return this.delegate.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public void copyInto(Object[] paramArrayOfObject)
/*     */   {
/* 101 */     this.delegate.copyInto(paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void trimToSize()
/*     */   {
/* 110 */     this.delegate.trimToSize();
/*     */   }
/*     */ 
/*     */   public void ensureCapacity(int paramInt)
/*     */   {
/* 122 */     this.delegate.ensureCapacity(paramInt);
/*     */   }
/*     */ 
/*     */   public void setSize(int paramInt)
/*     */   {
/* 132 */     int i = this.delegate.size();
/* 133 */     this.delegate.setSize(paramInt);
/* 134 */     if (i > paramInt) {
/* 135 */       fireIntervalRemoved(this, paramInt, i - 1);
/*     */     }
/* 137 */     else if (i < paramInt)
/* 138 */       fireIntervalAdded(this, i, paramInt - 1);
/*     */   }
/*     */ 
/*     */   public int capacity()
/*     */   {
/* 149 */     return this.delegate.capacity();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 159 */     return this.delegate.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 171 */     return this.delegate.isEmpty();
/*     */   }
/*     */ 
/*     */   public Enumeration<E> elements()
/*     */   {
/* 181 */     return this.delegate.elements();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 193 */     return this.delegate.contains(paramObject);
/*     */   }
/*     */ 
/*     */   public int indexOf(Object paramObject)
/*     */   {
/* 205 */     return this.delegate.indexOf(paramObject);
/*     */   }
/*     */ 
/*     */   public int indexOf(Object paramObject, int paramInt)
/*     */   {
/* 220 */     return this.delegate.indexOf(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(Object paramObject)
/*     */   {
/* 232 */     return this.delegate.lastIndexOf(paramObject);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(Object paramObject, int paramInt)
/*     */   {
/* 247 */     return this.delegate.lastIndexOf(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public E elementAt(int paramInt)
/*     */   {
/* 266 */     return this.delegate.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public E firstElement()
/*     */   {
/* 277 */     return this.delegate.firstElement();
/*     */   }
/*     */ 
/*     */   public E lastElement()
/*     */   {
/* 289 */     return this.delegate.lastElement();
/*     */   }
/*     */ 
/*     */   public void setElementAt(E paramE, int paramInt)
/*     */   {
/* 311 */     this.delegate.setElementAt(paramE, paramInt);
/* 312 */     fireContentsChanged(this, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public void removeElementAt(int paramInt)
/*     */   {
/* 331 */     this.delegate.removeElementAt(paramInt);
/* 332 */     fireIntervalRemoved(this, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public void insertElementAt(E paramE, int paramInt)
/*     */   {
/* 354 */     this.delegate.insertElementAt(paramE, paramInt);
/* 355 */     fireIntervalAdded(this, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public void addElement(E paramE)
/*     */   {
/* 365 */     int i = this.delegate.size();
/* 366 */     this.delegate.addElement(paramE);
/* 367 */     fireIntervalAdded(this, i, i);
/*     */   }
/*     */ 
/*     */   public boolean removeElement(Object paramObject)
/*     */   {
/* 380 */     int i = indexOf(paramObject);
/* 381 */     boolean bool = this.delegate.removeElement(paramObject);
/* 382 */     if (i >= 0) {
/* 383 */       fireIntervalRemoved(this, i, i);
/*     */     }
/* 385 */     return bool;
/*     */   }
/*     */ 
/*     */   public void removeAllElements()
/*     */   {
/* 401 */     int i = this.delegate.size() - 1;
/* 402 */     this.delegate.removeAllElements();
/* 403 */     if (i >= 0)
/* 404 */       fireIntervalRemoved(this, 0, i);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 416 */     return this.delegate.toString();
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 432 */     Object[] arrayOfObject = new Object[this.delegate.size()];
/* 433 */     this.delegate.copyInto(arrayOfObject);
/* 434 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public E get(int paramInt)
/*     */   {
/* 447 */     return this.delegate.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public E set(int paramInt, E paramE)
/*     */   {
/* 463 */     Object localObject = this.delegate.elementAt(paramInt);
/* 464 */     this.delegate.setElementAt(paramE, paramInt);
/* 465 */     fireContentsChanged(this, paramInt, paramInt);
/* 466 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, E paramE)
/*     */   {
/* 480 */     this.delegate.insertElementAt(paramE, paramInt);
/* 481 */     fireIntervalAdded(this, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public E remove(int paramInt)
/*     */   {
/* 496 */     Object localObject = this.delegate.elementAt(paramInt);
/* 497 */     this.delegate.removeElementAt(paramInt);
/* 498 */     fireIntervalRemoved(this, paramInt, paramInt);
/* 499 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 507 */     int i = this.delegate.size() - 1;
/* 508 */     this.delegate.removeAllElements();
/* 509 */     if (i >= 0)
/* 510 */       fireIntervalRemoved(this, 0, i);
/*     */   }
/*     */ 
/*     */   public void removeRange(int paramInt1, int paramInt2)
/*     */   {
/* 530 */     if (paramInt1 > paramInt2) {
/* 531 */       throw new IllegalArgumentException("fromIndex must be <= toIndex");
/*     */     }
/* 533 */     for (int i = paramInt2; i >= paramInt1; i--) {
/* 534 */       this.delegate.removeElementAt(i);
/*     */     }
/* 536 */     fireIntervalRemoved(this, paramInt1, paramInt2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultListModel
 * JD-Core Version:    0.6.2
 */