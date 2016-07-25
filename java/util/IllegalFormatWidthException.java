/*    */ package java.util;
/*    */ 
/*    */ public class IllegalFormatWidthException extends IllegalFormatException
/*    */ {
/*    */   private static final long serialVersionUID = 16660902L;
/*    */   private int w;
/*    */ 
/*    */   public IllegalFormatWidthException(int paramInt)
/*    */   {
/* 47 */     this.w = paramInt;
/*    */   }
/*    */ 
/*    */   public int getWidth()
/*    */   {
/* 56 */     return this.w;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 60 */     return Integer.toString(this.w);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.IllegalFormatWidthException
 * JD-Core Version:    0.6.2
 */