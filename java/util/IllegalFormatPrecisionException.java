/*    */ package java.util;
/*    */ 
/*    */ public class IllegalFormatPrecisionException extends IllegalFormatException
/*    */ {
/*    */   private static final long serialVersionUID = 18711008L;
/*    */   private int p;
/*    */ 
/*    */   public IllegalFormatPrecisionException(int paramInt)
/*    */   {
/* 48 */     this.p = paramInt;
/*    */   }
/*    */ 
/*    */   public int getPrecision()
/*    */   {
/* 57 */     return this.p;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 61 */     return Integer.toString(this.p);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.IllegalFormatPrecisionException
 * JD-Core Version:    0.6.2
 */