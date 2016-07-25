/*    */ package java.util.concurrent;
/*    */ 
/*    */ public abstract class RecursiveTask<V> extends ForkJoinTask<V>
/*    */ {
/*    */   private static final long serialVersionUID = 5232453952276485270L;
/*    */   V result;
/*    */ 
/*    */   protected abstract V compute();
/*    */ 
/*    */   public final V getRawResult()
/*    */   {
/* 82 */     return this.result;
/*    */   }
/*    */ 
/*    */   protected final void setRawResult(V paramV) {
/* 86 */     this.result = paramV;
/*    */   }
/*    */ 
/*    */   protected final boolean exec()
/*    */   {
/* 93 */     this.result = compute();
/* 94 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.RecursiveTask
 * JD-Core Version:    0.6.2
 */