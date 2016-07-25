/*    */ package java.security.spec;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ 
/*    */ public class InvalidParameterSpecException extends GeneralSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = -970468769593399342L;
/*    */ 
/*    */   public InvalidParameterSpecException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidParameterSpecException(String paramString)
/*    */   {
/* 64 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.InvalidParameterSpecException
 * JD-Core Version:    0.6.2
 */