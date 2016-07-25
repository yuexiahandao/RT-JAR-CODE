/*    */ package java.security;
/*    */ 
/*    */ public class InvalidAlgorithmParameterException extends GeneralSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 2864672297499471472L;
/*    */ 
/*    */   public InvalidAlgorithmParameterException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidAlgorithmParameterException(String paramString)
/*    */   {
/* 64 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public InvalidAlgorithmParameterException(String paramString, Throwable paramThrowable)
/*    */   {
/* 79 */     super(paramString, paramThrowable);
/*    */   }
/*    */ 
/*    */   public InvalidAlgorithmParameterException(Throwable paramThrowable)
/*    */   {
/* 95 */     super(paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.InvalidAlgorithmParameterException
 * JD-Core Version:    0.6.2
 */