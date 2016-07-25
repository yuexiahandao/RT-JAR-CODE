/*    */ package javax.swing.text;
/*    */ 
/*    */ public abstract interface Position
/*    */ {
/*    */   public abstract int getOffset();
/*    */ 
/*    */   public static final class Bias
/*    */   {
/* 72 */     public static final Bias Forward = new Bias("Forward");
/*    */ 
/* 78 */     public static final Bias Backward = new Bias("Backward");
/*    */     private String name;
/*    */ 
/*    */     public String toString()
/*    */     {
/* 84 */       return this.name;
/*    */     }
/*    */ 
/*    */     private Bias(String paramString) {
/* 88 */       this.name = paramString;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.Position
 * JD-Core Version:    0.6.2
 */