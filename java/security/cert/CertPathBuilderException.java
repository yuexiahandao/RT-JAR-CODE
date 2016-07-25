/*     */ package java.security.cert;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ 
/*     */ public class CertPathBuilderException extends GeneralSecurityException
/*     */ {
/*     */   private static final long serialVersionUID = 5316471420178794402L;
/*     */ 
/*     */   public CertPathBuilderException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CertPathBuilderException(String paramString)
/*     */   {
/*  71 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public CertPathBuilderException(Throwable paramThrowable)
/*     */   {
/*  88 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public CertPathBuilderException(String paramString, Throwable paramThrowable)
/*     */   {
/* 101 */     super(paramString, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertPathBuilderException
 * JD-Core Version:    0.6.2
 */