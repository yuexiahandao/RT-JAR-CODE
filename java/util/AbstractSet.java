/*     */ package java.util;
/*     */ 
/*     */ public abstract class AbstractSet<E> extends AbstractCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  86 */     if (paramObject == this) {
/*  87 */       return true;
/*     */     }
/*  89 */     if (!(paramObject instanceof Set))
/*  90 */       return false;
/*  91 */     Collection localCollection = (Collection)paramObject;
/*  92 */     if (localCollection.size() != size())
/*  93 */       return false;
/*     */     try {
/*  95 */       return containsAll(localCollection);
/*     */     } catch (ClassCastException localClassCastException) {
/*  97 */       return false; } catch (NullPointerException localNullPointerException) {
/*     */     }
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 121 */     int i = 0;
/* 122 */     Iterator localIterator = iterator();
/* 123 */     while (localIterator.hasNext()) {
/* 124 */       Object localObject = localIterator.next();
/* 125 */       if (localObject != null)
/* 126 */         i += localObject.hashCode();
/*     */     }
/* 128 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> paramCollection)
/*     */   {
/* 169 */     boolean bool = false;
/*     */     Iterator localIterator;
/* 171 */     if (size() > paramCollection.size())
/* 172 */       for (localIterator = paramCollection.iterator(); localIterator.hasNext(); )
/* 173 */         bool |= remove(localIterator.next());
/*     */     else {
/* 175 */       for (localIterator = iterator(); localIterator.hasNext(); ) {
/* 176 */         if (paramCollection.contains(localIterator.next())) {
/* 177 */           localIterator.remove();
/* 178 */           bool = true;
/*     */         }
/*     */       }
/*     */     }
/* 182 */     return bool;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.AbstractSet
 * JD-Core Version:    0.6.2
 */