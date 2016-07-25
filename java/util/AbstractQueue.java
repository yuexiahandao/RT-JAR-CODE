/*     */ package java.util;
/*     */ 
/*     */ public abstract class AbstractQueue<E> extends AbstractCollection<E>
/*     */   implements Queue<E>
/*     */ {
/*     */   public boolean add(E paramE)
/*     */   {
/*  95 */     if (offer(paramE)) {
/*  96 */       return true;
/*     */     }
/*  98 */     throw new IllegalStateException("Queue full");
/*     */   }
/*     */ 
/*     */   public E remove()
/*     */   {
/* 113 */     Object localObject = poll();
/* 114 */     if (localObject != null) {
/* 115 */       return localObject;
/*     */     }
/* 117 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   public E element()
/*     */   {
/* 132 */     Object localObject = peek();
/* 133 */     if (localObject != null) {
/* 134 */       return localObject;
/*     */     }
/* 136 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 147 */     while (poll() != null);
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection)
/*     */   {
/* 181 */     if (paramCollection == null)
/* 182 */       throw new NullPointerException();
/* 183 */     if (paramCollection == this)
/* 184 */       throw new IllegalArgumentException();
/* 185 */     boolean bool = false;
/* 186 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 187 */       if (add(localObject))
/* 188 */         bool = true; }
/* 189 */     return bool;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.AbstractQueue
 * JD-Core Version:    0.6.2
 */