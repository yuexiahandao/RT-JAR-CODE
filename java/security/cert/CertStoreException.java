/*     */ package java.security.cert;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ 
/*     */ public class CertStoreException extends GeneralSecurityException
/*     */ {
/*     */   private static final long serialVersionUID = 2395296107471573245L;
/*     */ 
/*     */   public CertStoreException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CertStoreException(String paramString)
/*     */   {
/*  71 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public CertStoreException(Throwable paramThrowable)
/*     */   {
/*  87 */     super(paramThrowable);
/*     */   }
/*     */ 
/*     */   public CertStoreException(String paramString, Throwable paramThrowable)
/*     */   {
/* 100 */     super(paramString, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertStoreException
 * JD-Core Version:    0.6.2
 */