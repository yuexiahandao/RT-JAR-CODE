/*     */ package java.security.cert;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class PKIXCertPathChecker
/*     */   implements Cloneable
/*     */ {
/*     */   public abstract void init(boolean paramBoolean)
/*     */     throws CertPathValidatorException;
/*     */ 
/*     */   public abstract boolean isForwardCheckingSupported();
/*     */ 
/*     */   public abstract Set<String> getSupportedExtensions();
/*     */ 
/*     */   public abstract void check(Certificate paramCertificate, Collection<String> paramCollection)
/*     */     throws CertPathValidatorException;
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 175 */       return super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 178 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.PKIXCertPathChecker
 * JD-Core Version:    0.6.2
 */