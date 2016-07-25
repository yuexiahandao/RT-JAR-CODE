/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.NameConstraintsExtension;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ class ConstraintsChecker extends PKIXCertPathChecker
/*     */ {
/*  54 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private final int certPathLength;
/*     */   private int maxPathLength;
/*     */   private int i;
/*     */   private NameConstraintsExtension prevNC;
/*     */   private Set<String> supportedExts;
/*     */ 
/*     */   ConstraintsChecker(int paramInt)
/*     */     throws CertPathValidatorException
/*     */   {
/*  72 */     this.certPathLength = paramInt;
/*  73 */     init(false);
/*     */   }
/*     */ 
/*     */   public void init(boolean paramBoolean) throws CertPathValidatorException {
/*  77 */     if (!paramBoolean) {
/*  78 */       this.i = 0;
/*  79 */       this.maxPathLength = this.certPathLength;
/*  80 */       this.prevNC = null;
/*     */     } else {
/*  82 */       throw new CertPathValidatorException("forward checking not supported");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isForwardCheckingSupported()
/*     */   {
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   public Set<String> getSupportedExtensions() {
/*  92 */     if (this.supportedExts == null) {
/*  93 */       this.supportedExts = new HashSet();
/*  94 */       this.supportedExts.add(PKIXExtensions.BasicConstraints_Id.toString());
/*  95 */       this.supportedExts.add(PKIXExtensions.NameConstraints_Id.toString());
/*  96 */       this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
/*     */     }
/*  98 */     return this.supportedExts;
/*     */   }
/*     */ 
/*     */   public void check(Certificate paramCertificate, Collection<String> paramCollection)
/*     */     throws CertPathValidatorException
/*     */   {
/* 114 */     X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
/*     */ 
/* 116 */     this.i += 1;
/*     */ 
/* 119 */     checkBasicConstraints(localX509Certificate);
/* 120 */     verifyNameConstraints(localX509Certificate);
/*     */ 
/* 122 */     if ((paramCollection != null) && (!paramCollection.isEmpty())) {
/* 123 */       paramCollection.remove(PKIXExtensions.BasicConstraints_Id.toString());
/* 124 */       paramCollection.remove(PKIXExtensions.NameConstraints_Id.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void verifyNameConstraints(X509Certificate paramX509Certificate)
/*     */     throws CertPathValidatorException
/*     */   {
/* 134 */     String str = "name constraints";
/* 135 */     if (debug != null) {
/* 136 */       debug.println("---checking " + str + "...");
/*     */     }
/*     */ 
/* 142 */     if ((this.prevNC != null) && ((this.i == this.certPathLength) || (!X509CertImpl.isSelfIssued(paramX509Certificate))))
/*     */     {
/* 144 */       if (debug != null) {
/* 145 */         debug.println("prevNC = " + this.prevNC);
/* 146 */         debug.println("currDN = " + paramX509Certificate.getSubjectX500Principal());
/*     */       }
/*     */       try
/*     */       {
/* 150 */         if (!this.prevNC.verify(paramX509Certificate))
/* 151 */           throw new CertPathValidatorException(str + " check failed", null, null, -1, PKIXReason.INVALID_NAME);
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 155 */         throw new CertPathValidatorException(localIOException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 160 */     this.prevNC = mergeNameConstraints(paramX509Certificate, this.prevNC);
/*     */ 
/* 162 */     if (debug != null)
/* 163 */       debug.println(str + " verified.");
/*     */   }
/*     */ 
/*     */   static NameConstraintsExtension mergeNameConstraints(X509Certificate paramX509Certificate, NameConstraintsExtension paramNameConstraintsExtension)
/*     */     throws CertPathValidatorException
/*     */   {
/*     */     X509CertImpl localX509CertImpl;
/*     */     try
/*     */     {
/* 175 */       localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/*     */     } catch (CertificateException localCertificateException) {
/* 177 */       throw new CertPathValidatorException(localCertificateException);
/*     */     }
/*     */ 
/* 180 */     NameConstraintsExtension localNameConstraintsExtension = localX509CertImpl.getNameConstraintsExtension();
/*     */ 
/* 183 */     if (debug != null) {
/* 184 */       debug.println("prevNC = " + paramNameConstraintsExtension);
/* 185 */       debug.println("newNC = " + String.valueOf(localNameConstraintsExtension));
/*     */     }
/*     */ 
/* 190 */     if (paramNameConstraintsExtension == null) {
/* 191 */       if (debug != null) {
/* 192 */         debug.println("mergedNC = " + String.valueOf(localNameConstraintsExtension));
/*     */       }
/* 194 */       if (localNameConstraintsExtension == null) {
/* 195 */         return localNameConstraintsExtension;
/*     */       }
/*     */ 
/* 200 */       return (NameConstraintsExtension)localNameConstraintsExtension.clone();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 205 */       paramNameConstraintsExtension.merge(localNameConstraintsExtension);
/*     */     } catch (IOException localIOException) {
/* 207 */       throw new CertPathValidatorException(localIOException);
/*     */     }
/* 209 */     if (debug != null) {
/* 210 */       debug.println("mergedNC = " + paramNameConstraintsExtension);
/*     */     }
/* 212 */     return paramNameConstraintsExtension;
/*     */   }
/*     */ 
/*     */   private void checkBasicConstraints(X509Certificate paramX509Certificate)
/*     */     throws CertPathValidatorException
/*     */   {
/* 222 */     String str = "basic constraints";
/* 223 */     if (debug != null) {
/* 224 */       debug.println("---checking " + str + "...");
/* 225 */       debug.println("i = " + this.i);
/* 226 */       debug.println("maxPathLength = " + this.maxPathLength);
/*     */     }
/*     */ 
/* 230 */     if (this.i < this.certPathLength)
/*     */     {
/* 244 */       int j = -1;
/* 245 */       if (paramX509Certificate.getVersion() < 3) {
/* 246 */         if ((this.i == 1) && 
/* 247 */           (X509CertImpl.isSelfIssued(paramX509Certificate))) {
/* 248 */           j = 2147483647;
/*     */         }
/*     */       }
/*     */       else {
/* 252 */         j = paramX509Certificate.getBasicConstraints();
/*     */       }
/*     */ 
/* 255 */       if (j == -1) {
/* 256 */         throw new CertPathValidatorException(str + " check failed: this is not a CA certificate", null, null, -1, PKIXReason.NOT_CA_CERT);
/*     */       }
/*     */ 
/* 261 */       if (!X509CertImpl.isSelfIssued(paramX509Certificate)) {
/* 262 */         if (this.maxPathLength <= 0) {
/* 263 */           throw new CertPathValidatorException(str + " check failed: pathLenConstraint violated - " + "this cert must be the last cert in the " + "certification path", null, null, -1, PKIXReason.PATH_TOO_LONG);
/*     */         }
/*     */ 
/* 269 */         this.maxPathLength -= 1;
/*     */       }
/* 271 */       if (j < this.maxPathLength) {
/* 272 */         this.maxPathLength = j;
/*     */       }
/*     */     }
/* 275 */     if (debug != null) {
/* 276 */       debug.println("after processing, maxPathLength = " + this.maxPathLength);
/* 277 */       debug.println(str + " verified.");
/*     */     }
/*     */   }
/*     */ 
/*     */   static int mergeBasicConstraints(X509Certificate paramX509Certificate, int paramInt)
/*     */   {
/* 293 */     int j = paramX509Certificate.getBasicConstraints();
/*     */ 
/* 295 */     if (!X509CertImpl.isSelfIssued(paramX509Certificate)) {
/* 296 */       paramInt--;
/*     */     }
/*     */ 
/* 299 */     if (j < paramInt) {
/* 300 */       paramInt = j;
/*     */     }
/*     */ 
/* 303 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.ConstraintsChecker
 * JD-Core Version:    0.6.2
 */