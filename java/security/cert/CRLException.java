/*    */ package java.security.cert;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ 
/*    */ public class CRLException extends GeneralSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = -6694728944094197147L;
/*    */ 
/*    */   public CRLException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public CRLException(String paramString)
/*    */   {
/* 56 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public CRLException(String paramString, Throwable paramThrowable)
/*    */   {
/* 71 */     super(paramString, paramThrowable);
/*    */   }
/*    */ 
/*    */   public CRLException(Throwable paramThrowable)
/*    */   {
/* 86 */     super(paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CRLException
 * JD-Core Version:    0.6.2
 */