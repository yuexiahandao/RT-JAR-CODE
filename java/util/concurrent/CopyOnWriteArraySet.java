/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class CopyOnWriteArraySet<E> extends AbstractSet<E>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5457747651344034263L;
/*     */   private final CopyOnWriteArrayList<E> al;
/*     */ 
/*     */   public CopyOnWriteArraySet()
/*     */   {
/*  99 */     this.al = new CopyOnWriteArrayList();
/*     */   }
/*     */ 
/*     */   public CopyOnWriteArraySet(Collection<? extends E> paramCollection)
/*     */   {
/* 110 */     this.al = new CopyOnWriteArrayList();
/* 111 */     this.al.addAllAbsent(paramCollection);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 120 */     return this.al.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 129 */     return this.al.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 142 */     return this.al.contains(paramObject);
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 162 */     return this.al.toArray();
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 208 */     return this.al.toArray(paramArrayOfT);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 216 */     this.al.clear();
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 232 */     return this.al.remove(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 248 */     return this.al.addIfAbsent(paramE);
/*     */   }
/*     */ 
/*     */   public boolean containsAll(Collection<?> paramCollection)
/*     */   {
/* 263 */     return this.al.containsAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection)
/*     */   {
/* 280 */     return this.al.addAllAbsent(paramCollection) > 0;
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> paramCollection)
/*     */   {
/* 299 */     return this.al.removeAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean retainAll(Collection<?> paramCollection)
/*     */   {
/* 320 */     return this.al.retainAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 335 */     return this.al.iterator();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 355 */     if (paramObject == this)
/* 356 */       return true;
/* 357 */     if (!(paramObject instanceof Set))
/* 358 */       return false;
/* 359 */     Set localSet = (Set)paramObject;
/* 360 */     Iterator localIterator = localSet.iterator();
/*     */ 
/* 366 */     Object[] arrayOfObject = this.al.getArray();
/* 367 */     int i = arrayOfObject.length;
/*     */ 
/* 369 */     boolean[] arrayOfBoolean = new boolean[i];
/* 370 */     int j = 0;
/* 371 */     if (localIterator.hasNext()) {
/* 372 */       j++; if (j > i)
/* 373 */         return false;
/* 374 */       Object localObject = localIterator.next();
/* 375 */       for (int k = 0; ; k++) { if (k >= i) break label129;
/* 376 */         if ((arrayOfBoolean[k] == 0) && (eq(localObject, arrayOfObject[k]))) {
/* 377 */           arrayOfBoolean[k] = true;
/* 378 */           break;
/*     */         }
/*     */       }
/* 381 */       label129: return false;
/*     */     }
/* 383 */     return j == i;
/*     */   }
/*     */ 
/*     */   private static boolean eq(Object paramObject1, Object paramObject2)
/*     */   {
/* 390 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.CopyOnWriteArraySet
 * JD-Core Version:    0.6.2
 */