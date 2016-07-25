/*    */ package javax.management;
/*    */ 
/*    */ public class BadStringOperationException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 7802201238441662100L;
/*    */   private String op;
/*    */ 
/*    */   public BadStringOperationException(String paramString)
/*    */   {
/* 52 */     this.op = paramString;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 60 */     return "BadStringOperationException: " + this.op;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.BadStringOperationException
 * JD-Core Version:    0.6.2
 */