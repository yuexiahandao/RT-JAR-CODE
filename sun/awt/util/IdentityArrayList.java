/*     */ package sun.awt.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ public class IdentityArrayList<E> extends AbstractList<E>
/*     */   implements List<E>, RandomAccess
/*     */ {
/*     */   private transient Object[] elementData;
/*     */   private int size;
/*     */ 
/*     */   public IdentityArrayList(int paramInt)
/*     */   {
/* 121 */     if (paramInt < 0) {
/* 122 */       throw new IllegalArgumentException("Illegal Capacity: " + paramInt);
/*     */     }
/* 124 */     this.elementData = new Object[paramInt];
/*     */   }
/*     */ 
/*     */   public IdentityArrayList()
/*     */   {
/* 131 */     this(10);
/*     */   }
/*     */ 
/*     */   public IdentityArrayList(Collection<? extends E> paramCollection)
/*     */   {
/* 143 */     this.elementData = paramCollection.toArray();
/* 144 */     this.size = this.elementData.length;
/*     */ 
/* 146 */     if (this.elementData.getClass() != [Ljava.lang.Object.class)
/* 147 */       this.elementData = Arrays.copyOf(this.elementData, this.size, [Ljava.lang.Object.class);
/*     */   }
/*     */ 
/*     */   public void trimToSize()
/*     */   {
/* 156 */     this.modCount += 1;
/* 157 */     int i = this.elementData.length;
/* 158 */     if (this.size < i)
/* 159 */       this.elementData = Arrays.copyOf(this.elementData, this.size);
/*     */   }
/*     */ 
/*     */   public void ensureCapacity(int paramInt)
/*     */   {
/* 171 */     this.modCount += 1;
/* 172 */     int i = this.elementData.length;
/* 173 */     if (paramInt > i) {
/* 174 */       Object[] arrayOfObject = this.elementData;
/* 175 */       int j = i * 3 / 2 + 1;
/* 176 */       if (j < paramInt) {
/* 177 */         j = paramInt;
/*     */       }
/* 179 */       this.elementData = Arrays.copyOf(this.elementData, j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 189 */     return this.size;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 198 */     return this.size == 0;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 211 */     return indexOf(paramObject) >= 0;
/*     */   }
/*     */ 
/*     */   public int indexOf(Object paramObject)
/*     */   {
/* 222 */     for (int i = 0; i < this.size; i++) {
/* 223 */       if (paramObject == this.elementData[i]) {
/* 224 */         return i;
/*     */       }
/*     */     }
/* 227 */     return -1;
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(Object paramObject)
/*     */   {
/* 238 */     for (int i = this.size - 1; i >= 0; i--) {
/* 239 */       if (paramObject == this.elementData[i]) {
/* 240 */         return i;
/*     */       }
/*     */     }
/* 243 */     return -1;
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 261 */     return Arrays.copyOf(this.elementData, this.size);
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 289 */     if (paramArrayOfT.length < this.size)
/*     */     {
/* 291 */       return (Object[])Arrays.copyOf(this.elementData, this.size, paramArrayOfT.getClass());
/* 292 */     }System.arraycopy(this.elementData, 0, paramArrayOfT, 0, this.size);
/* 293 */     if (paramArrayOfT.length > this.size)
/* 294 */       paramArrayOfT[this.size] = null;
/* 295 */     return paramArrayOfT;
/*     */   }
/*     */ 
/*     */   public E get(int paramInt)
/*     */   {
/* 308 */     rangeCheck(paramInt);
/*     */ 
/* 310 */     return this.elementData[paramInt];
/*     */   }
/*     */ 
/*     */   public E set(int paramInt, E paramE)
/*     */   {
/* 323 */     rangeCheck(paramInt);
/*     */ 
/* 325 */     Object localObject = this.elementData[paramInt];
/* 326 */     this.elementData[paramInt] = paramE;
/* 327 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 337 */     ensureCapacity(this.size + 1);
/* 338 */     this.elementData[(this.size++)] = paramE;
/* 339 */     return true;
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, E paramE)
/*     */   {
/* 352 */     rangeCheckForAdd(paramInt);
/*     */ 
/* 354 */     ensureCapacity(this.size + 1);
/* 355 */     System.arraycopy(this.elementData, paramInt, this.elementData, paramInt + 1, this.size - paramInt);
/*     */ 
/* 357 */     this.elementData[paramInt] = paramE;
/* 358 */     this.size += 1;
/*     */   }
/*     */ 
/*     */   public E remove(int paramInt)
/*     */   {
/* 371 */     rangeCheck(paramInt);
/*     */ 
/* 373 */     this.modCount += 1;
/* 374 */     Object localObject = this.elementData[paramInt];
/*     */ 
/* 376 */     int i = this.size - paramInt - 1;
/* 377 */     if (i > 0) {
/* 378 */       System.arraycopy(this.elementData, paramInt + 1, this.elementData, paramInt, i);
/*     */     }
/* 380 */     this.elementData[(--this.size)] = null;
/*     */ 
/* 382 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 399 */     for (int i = 0; i < this.size; i++) {
/* 400 */       if (paramObject == this.elementData[i]) {
/* 401 */         fastRemove(i);
/* 402 */         return true;
/*     */       }
/*     */     }
/* 405 */     return false;
/*     */   }
/*     */ 
/*     */   private void fastRemove(int paramInt)
/*     */   {
/* 413 */     this.modCount += 1;
/* 414 */     int i = this.size - paramInt - 1;
/* 415 */     if (i > 0) {
/* 416 */       System.arraycopy(this.elementData, paramInt + 1, this.elementData, paramInt, i);
/*     */     }
/* 418 */     this.elementData[(--this.size)] = null;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 426 */     this.modCount += 1;
/*     */ 
/* 429 */     for (int i = 0; i < this.size; i++) {
/* 430 */       this.elementData[i] = null;
/*     */     }
/* 432 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection)
/*     */   {
/* 449 */     Object[] arrayOfObject = paramCollection.toArray();
/* 450 */     int i = arrayOfObject.length;
/* 451 */     ensureCapacity(this.size + i);
/* 452 */     System.arraycopy(arrayOfObject, 0, this.elementData, this.size, i);
/* 453 */     this.size += i;
/* 454 */     return i != 0;
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, Collection<? extends E> paramCollection)
/*     */   {
/* 473 */     rangeCheckForAdd(paramInt);
/*     */ 
/* 475 */     Object[] arrayOfObject = paramCollection.toArray();
/* 476 */     int i = arrayOfObject.length;
/* 477 */     ensureCapacity(this.size + i);
/*     */ 
/* 479 */     int j = this.size - paramInt;
/* 480 */     if (j > 0) {
/* 481 */       System.arraycopy(this.elementData, paramInt, this.elementData, paramInt + i, j);
/*     */     }
/*     */ 
/* 484 */     System.arraycopy(arrayOfObject, 0, this.elementData, paramInt, i);
/* 485 */     this.size += i;
/* 486 */     return i != 0;
/*     */   }
/*     */ 
/*     */   protected void removeRange(int paramInt1, int paramInt2)
/*     */   {
/* 503 */     this.modCount += 1;
/* 504 */     int i = this.size - paramInt2;
/* 505 */     System.arraycopy(this.elementData, paramInt2, this.elementData, paramInt1, i);
/*     */ 
/* 509 */     int j = this.size - (paramInt2 - paramInt1);
/* 510 */     while (this.size != j)
/* 511 */       this.elementData[(--this.size)] = null;
/*     */   }
/*     */ 
/*     */   private void rangeCheck(int paramInt)
/*     */   {
/* 521 */     if (paramInt >= this.size)
/* 522 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*     */   }
/*     */ 
/*     */   private void rangeCheckForAdd(int paramInt)
/*     */   {
/* 529 */     if ((paramInt > this.size) || (paramInt < 0))
/* 530 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*     */   }
/*     */ 
/*     */   private String outOfBoundsMsg(int paramInt)
/*     */   {
/* 539 */     return "Index: " + paramInt + ", Size: " + this.size;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.util.IdentityArrayList
 * JD-Core Version:    0.6.2
 */