/*     */ package java.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ public abstract class AbstractCollection<E>
/*     */   implements Collection<E>
/*     */ {
/*     */   private static final int MAX_ARRAY_SIZE = 2147483639;
/*     */ 
/*     */   public abstract Iterator<E> iterator();
/*     */ 
/*     */   public abstract int size();
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  86 */     return size() == 0;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/*  99 */     Iterator localIterator = iterator();
/* 100 */     if (paramObject == null) {
/*     */       do if (!localIterator.hasNext())
/*     */           break; while (localIterator.next() != null);
/* 103 */       return true;
/*     */     }
/* 105 */     while (localIterator.hasNext()) {
/* 106 */       if (paramObject.equals(localIterator.next()))
/* 107 */         return true;
/*     */     }
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 136 */     Object[] arrayOfObject = new Object[size()];
/* 137 */     Iterator localIterator = iterator();
/* 138 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 139 */       if (!localIterator.hasNext())
/* 140 */         return Arrays.copyOf(arrayOfObject, i);
/* 141 */       arrayOfObject[i] = localIterator.next();
/*     */     }
/* 143 */     return localIterator.hasNext() ? finishToArray(arrayOfObject, localIterator) : arrayOfObject;
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 175 */     int i = size();
/* 176 */     Object[] arrayOfObject = paramArrayOfT.length >= i ? paramArrayOfT : (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/*     */ 
/* 179 */     Iterator localIterator = iterator();
/*     */ 
/* 181 */     for (int j = 0; j < arrayOfObject.length; j++) {
/* 182 */       if (!localIterator.hasNext()) {
/* 183 */         if (paramArrayOfT == arrayOfObject) {
/* 184 */           arrayOfObject[j] = null; } else {
/* 185 */           if (paramArrayOfT.length < j) {
/* 186 */             return Arrays.copyOf(arrayOfObject, j);
/*     */           }
/* 188 */           System.arraycopy(arrayOfObject, 0, paramArrayOfT, 0, j);
/* 189 */           if (paramArrayOfT.length > j) {
/* 190 */             paramArrayOfT[j] = null;
/*     */           }
/*     */         }
/* 193 */         return paramArrayOfT;
/*     */       }
/* 195 */       arrayOfObject[j] = localIterator.next();
/*     */     }
/*     */ 
/* 198 */     return localIterator.hasNext() ? finishToArray(arrayOfObject, localIterator) : arrayOfObject;
/*     */   }
/*     */ 
/*     */   private static <T> T[] finishToArray(T[] paramArrayOfT, Iterator<?> paramIterator)
/*     */   {
/* 220 */     int i = paramArrayOfT.length;
/* 221 */     while (paramIterator.hasNext()) {
/* 222 */       int j = paramArrayOfT.length;
/* 223 */       if (i == j) {
/* 224 */         int k = j + (j >> 1) + 1;
/*     */ 
/* 226 */         if (k - 2147483639 > 0)
/* 227 */           k = hugeCapacity(j + 1);
/* 228 */         paramArrayOfT = Arrays.copyOf(paramArrayOfT, k);
/*     */       }
/* 230 */       paramArrayOfT[(i++)] = paramIterator.next();
/*     */     }
/*     */ 
/* 233 */     return i == paramArrayOfT.length ? paramArrayOfT : Arrays.copyOf(paramArrayOfT, i);
/*     */   }
/*     */ 
/*     */   private static int hugeCapacity(int paramInt) {
/* 237 */     if (paramInt < 0) {
/* 238 */       throw new OutOfMemoryError("Required array size too large");
/*     */     }
/* 240 */     return paramInt > 2147483639 ? 2147483647 : 2147483639;
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 260 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 280 */     Iterator localIterator = iterator();
/* 281 */     if (paramObject == null) {
/*     */       do if (!localIterator.hasNext())
/*     */           break; while (localIterator.next() != null);
/* 284 */       localIterator.remove();
/* 285 */       return true;
/*     */     }
/*     */ 
/* 289 */     while (localIterator.hasNext()) {
/* 290 */       if (paramObject.equals(localIterator.next())) {
/* 291 */         localIterator.remove();
/* 292 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 296 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsAll(Collection<?> paramCollection)
/*     */   {
/* 315 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 316 */       if (!contains(localObject))
/* 317 */         return false; }
/* 318 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection)
/*     */   {
/* 340 */     boolean bool = false;
/* 341 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 342 */       if (add(localObject))
/* 343 */         bool = true; }
/* 344 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> paramCollection)
/*     */   {
/* 369 */     boolean bool = false;
/* 370 */     Iterator localIterator = iterator();
/* 371 */     while (localIterator.hasNext()) {
/* 372 */       if (paramCollection.contains(localIterator.next())) {
/* 373 */         localIterator.remove();
/* 374 */         bool = true;
/*     */       }
/*     */     }
/* 377 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean retainAll(Collection<?> paramCollection)
/*     */   {
/* 402 */     boolean bool = false;
/* 403 */     Iterator localIterator = iterator();
/* 404 */     while (localIterator.hasNext()) {
/* 405 */       if (!paramCollection.contains(localIterator.next())) {
/* 406 */         localIterator.remove();
/* 407 */         bool = true;
/*     */       }
/*     */     }
/* 410 */     return bool;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 429 */     Iterator localIterator = iterator();
/* 430 */     while (localIterator.hasNext()) {
/* 431 */       localIterator.next();
/* 432 */       localIterator.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 450 */     Iterator localIterator = iterator();
/* 451 */     if (!localIterator.hasNext()) {
/* 452 */       return "[]";
/*     */     }
/* 454 */     StringBuilder localStringBuilder = new StringBuilder();
/* 455 */     localStringBuilder.append('[');
/*     */     while (true) {
/* 457 */       Object localObject = localIterator.next();
/* 458 */       localStringBuilder.append(localObject == this ? "(this Collection)" : localObject);
/* 459 */       if (!localIterator.hasNext())
/* 460 */         return ']';
/* 461 */       localStringBuilder.append(',').append(' ');
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.AbstractCollection
 * JD-Core Version:    0.6.2
 */