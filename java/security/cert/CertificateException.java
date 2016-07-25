/*    */ package java.security.cert;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ 
/*    */ public class CertificateException extends GeneralSecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 3192535253797119798L;
/*    */ 
/*    */   public CertificateException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public CertificateException(String paramString)
/*    */   {
/* 56 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public CertificateException(String paramString, Throwable paramThrowable)
/*    */   {
/* 71 */     super(paramString, paramThrowable);
/*    */   }
/*    */ 
/*    */   public CertificateException(Throwable paramThrowable)
/*    */   {
/* 86 */     super(paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertificateException
 * JD-Core Version:    0.6.2
 */