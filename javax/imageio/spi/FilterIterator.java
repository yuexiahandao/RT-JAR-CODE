/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class FilterIterator<T>
/*     */   implements Iterator<T>
/*     */ {
/*     */   private Iterator<T> iter;
/*     */   private ServiceRegistry.Filter filter;
/* 798 */   private T next = null;
/*     */ 
/*     */   public FilterIterator(Iterator<T> paramIterator, ServiceRegistry.Filter paramFilter)
/*     */   {
/* 802 */     this.iter = paramIterator;
/* 803 */     this.filter = paramFilter;
/* 804 */     advance();
/*     */   }
/*     */ 
/*     */   private void advance() {
/* 808 */     while (this.iter.hasNext()) {
/* 809 */       Object localObject = this.iter.next();
/* 810 */       if (this.filter.filter(localObject)) {
/* 811 */         this.next = localObject;
/* 812 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 816 */     this.next = null;
/*     */   }
/*     */ 
/*     */   public boolean hasNext() {
/* 820 */     return this.next != null;
/*     */   }
/*     */ 
/*     */   public T next() {
/* 824 */     if (this.next == null) {
/* 825 */       throw new NoSuchElementException();
/*     */     }
/* 827 */     Object localObject = this.next;
/* 828 */     advance();
/* 829 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void remove() {
/* 833 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.FilterIterator
 * JD-Core Version:    0.6.2
 */