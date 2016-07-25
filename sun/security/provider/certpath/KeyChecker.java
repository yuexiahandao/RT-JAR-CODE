/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertSelector;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ 
/*     */ class KeyChecker extends PKIXCertPathChecker
/*     */ {
/*  47 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private static final int keyCertSign = 5;
/*     */   private final int certPathLen;
/*     */   private CertSelector targetConstraints;
/*     */   private int remainingCerts;
/*     */   private Set<String> supportedExts;
/*     */ 
/*     */   KeyChecker(int paramInt, CertSelector paramCertSelector)
/*     */     throws CertPathValidatorException
/*     */   {
/*  66 */     this.certPathLen = paramInt;
/*  67 */     this.targetConstraints = paramCertSelector;
/*  68 */     init(false);
/*     */   }
/*     */ 
/*     */   public void init(boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/*  76 */     if (!paramBoolean)
/*  77 */       this.remainingCerts = this.certPathLen;
/*     */     else
/*  79 */       throw new CertPathValidatorException("forward checking not supported");
/*     */   }
/*     */ 
/*     */   public final boolean isForwardCheckingSupported()
/*     */   {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   public Set<String> getSupportedExtensions() {
/*  89 */     if (this.supportedExts == null) {
/*  90 */       this.supportedExts = new HashSet();
/*  91 */       this.supportedExts.add(PKIXExtensions.KeyUsage_Id.toString());
/*  92 */       this.supportedExts.add(PKIXExtensions.ExtendedKeyUsage_Id.toString());
/*  93 */       this.supportedExts.add(PKIXExtensions.SubjectAlternativeName_Id.toString());
/*  94 */       this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
/*     */     }
/*  96 */     return this.supportedExts;
/*     */   }
/*     */ 
/*     */   public void check(Certificate paramCertificate, Collection<String> paramCollection)
/*     */     throws CertPathValidatorException
/*     */   {
/* 111 */     X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
/*     */ 
/* 113 */     this.remainingCerts -= 1;
/*     */ 
/* 116 */     if (this.remainingCerts == 0) {
/* 117 */       if ((this.targetConstraints != null) && (!this.targetConstraints.match(localX509Certificate)))
/*     */       {
/* 119 */         throw new CertPathValidatorException("target certificate constraints check failed");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 124 */       verifyCAKeyUsage(localX509Certificate);
/*     */     }
/*     */ 
/* 128 */     if ((paramCollection != null) && (!paramCollection.isEmpty())) {
/* 129 */       paramCollection.remove(PKIXExtensions.KeyUsage_Id.toString());
/* 130 */       paramCollection.remove(PKIXExtensions.ExtendedKeyUsage_Id.toString());
/* 131 */       paramCollection.remove(PKIXExtensions.SubjectAlternativeName_Id.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   static void verifyCAKeyUsage(X509Certificate paramX509Certificate)
/*     */     throws CertPathValidatorException
/*     */   {
/* 144 */     String str = "CA key usage";
/* 145 */     if (debug != null) {
/* 146 */       debug.println("KeyChecker.verifyCAKeyUsage() ---checking " + str + "...");
/*     */     }
/*     */ 
/* 150 */     boolean[] arrayOfBoolean = paramX509Certificate.getKeyUsage();
/*     */ 
/* 154 */     if (arrayOfBoolean == null) {
/* 155 */       return;
/*     */     }
/*     */ 
/* 159 */     if (arrayOfBoolean[5] == 0) {
/* 160 */       throw new CertPathValidatorException(str + " check failed: keyCertSign bit is not set", null, null, -1, PKIXReason.INVALID_KEY_USAGE);
/*     */     }
/*     */ 
/* 165 */     if (debug != null)
/* 166 */       debug.println("KeyChecker.verifyCAKeyUsage() " + str + " verified.");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.KeyChecker
 * JD-Core Version:    0.6.2
 */