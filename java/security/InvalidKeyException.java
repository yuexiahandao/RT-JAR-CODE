/*    */ package java.security;
/*    */ 
/*    */ public class InvalidKeyException extends KeyException
/*    */ {
/*    */   private static final long serialVersionUID = 5698479920593359816L;
/*    */ 
/*    */   public InvalidKeyException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidKeyException(String paramString)
/*    */   {
/* 57 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public InvalidKeyException(String paramString, Throwable paramThrowable)
/*    */   {
/* 72 */     super(paramString, paramThrowable);
/*    */   }
/*    */ 
/*    */   public InvalidKeyException(Throwable paramThrowable)
/*    */   {
/* 87 */     super(paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.InvalidKeyException
 * JD-Core Version:    0.6.2
 */