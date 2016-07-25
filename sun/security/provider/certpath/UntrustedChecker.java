/*    */ package sun.security.provider.certpath;
/*    */ 
/*    */ import java.security.cert.CertPathValidatorException;
/*    */ import java.security.cert.Certificate;
/*    */ import java.security.cert.PKIXCertPathChecker;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ import sun.security.util.Debug;
/*    */ import sun.security.util.UntrustedCertificates;
/*    */ 
/*    */ public final class UntrustedChecker extends PKIXCertPathChecker
/*    */ {
/* 46 */   private static final Debug debug = Debug.getInstance("certpath");
/*    */ 
/*    */   public void init(boolean paramBoolean)
/*    */     throws CertPathValidatorException
/*    */   {
/*    */   }
/*    */ 
/*    */   public boolean isForwardCheckingSupported()
/*    */   {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */   public Set<String> getSupportedExtensions()
/*    */   {
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   public void check(Certificate paramCertificate, Collection<String> paramCollection)
/*    */     throws CertPathValidatorException
/*    */   {
/* 76 */     X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
/*    */ 
/* 78 */     if (UntrustedCertificates.isUntrusted(localX509Certificate)) {
/* 79 */       if (debug != null) {
/* 80 */         debug.println("UntrustedChecker: untrusted certificate " + localX509Certificate.getSubjectX500Principal());
/*    */       }
/*    */ 
/* 84 */       throw new CertPathValidatorException("Untrusted certificate: " + localX509Certificate.getSubjectX500Principal());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.UntrustedChecker
 * JD-Core Version:    0.6.2
 */