/*     */ package java.util;
/*     */ 
/*     */ class RandomAccessSubList<E> extends SubList<E>
/*     */   implements RandomAccess
/*     */ {
/*     */   RandomAccessSubList(AbstractList<E> paramAbstractList, int paramInt1, int paramInt2)
/*     */   {
/* 775 */     super(paramAbstractList, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public List<E> subList(int paramInt1, int paramInt2) {
/* 779 */     return new RandomAccessSubList(this, paramInt1, paramInt2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.RandomAccessSubList
 * JD-Core Version:    0.6.2
 */