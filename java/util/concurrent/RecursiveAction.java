/*     */ package java.util.concurrent;
/*     */ 
/*     */ public abstract class RecursiveAction extends ForkJoinTask<Void>
/*     */ {
/*     */   private static final long serialVersionUID = 5232453952276485070L;
/*     */ 
/*     */   protected abstract void compute();
/*     */ 
/*     */   public final Void getRawResult()
/*     */   {
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   protected final void setRawResult(Void paramVoid)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected final boolean exec()
/*     */   {
/* 177 */     compute();
/* 178 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.RecursiveAction
 * JD-Core Version:    0.6.2
 */