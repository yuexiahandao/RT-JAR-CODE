/*     */ package sun.security.validator;
/*     */ 
/*     */ import java.security.AlgorithmConstraints;
/*     */ import java.security.KeyStore;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class Validator
/*     */ {
/*  89 */   static final X509Certificate[] CHAIN0 = new X509Certificate[0];
/*     */   public static final String TYPE_SIMPLE = "Simple";
/*     */   public static final String TYPE_PKIX = "PKIX";
/*     */   public static final String VAR_GENERIC = "generic";
/*     */   public static final String VAR_CODE_SIGNING = "code signing";
/*     */   public static final String VAR_JCE_SIGNING = "jce signing";
/*     */   public static final String VAR_TLS_CLIENT = "tls client";
/*     */   public static final String VAR_TLS_SERVER = "tls server";
/*     */   public static final String VAR_TSA_SERVER = "tsa server";
/*     */   public static final String VAR_PLUGIN_CODE_SIGNING = "plugin code signing";
/*     */   final EndEntityChecker endEntityChecker;
/*     */   final String variant;
/*     */ 
/*     */   @Deprecated
/*     */   volatile Date validationDate;
/*     */ 
/*     */   Validator(String paramString1, String paramString2)
/*     */   {
/* 157 */     this.variant = paramString2;
/* 158 */     this.endEntityChecker = EndEntityChecker.getInstance(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public static Validator getInstance(String paramString1, String paramString2, KeyStore paramKeyStore)
/*     */   {
/* 167 */     return getInstance(paramString1, paramString2, KeyStores.getTrustedCerts(paramKeyStore));
/*     */   }
/*     */ 
/*     */   public static Validator getInstance(String paramString1, String paramString2, Collection<X509Certificate> paramCollection)
/*     */   {
/* 176 */     if (paramString1.equals("Simple"))
/* 177 */       return new SimpleValidator(paramString2, paramCollection);
/* 178 */     if (paramString1.equals("PKIX")) {
/* 179 */       return new PKIXValidator(paramString2, paramCollection);
/*     */     }
/* 181 */     throw new IllegalArgumentException("Unknown validator type: " + paramString1);
/*     */   }
/*     */ 
/*     */   public static Validator getInstance(String paramString1, String paramString2, PKIXBuilderParameters paramPKIXBuilderParameters)
/*     */   {
/* 192 */     if (!paramString1.equals("PKIX")) {
/* 193 */       throw new IllegalArgumentException("getInstance(PKIXBuilderParameters) can only be used with PKIX validator");
/*     */     }
/*     */ 
/* 197 */     return new PKIXValidator(paramString2, paramPKIXBuilderParameters);
/*     */   }
/*     */ 
/*     */   public final X509Certificate[] validate(X509Certificate[] paramArrayOfX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 205 */     return validate(paramArrayOfX509Certificate, null, null);
/*     */   }
/*     */ 
/*     */   public final X509Certificate[] validate(X509Certificate[] paramArrayOfX509Certificate, Collection<X509Certificate> paramCollection)
/*     */     throws CertificateException
/*     */   {
/* 215 */     return validate(paramArrayOfX509Certificate, paramCollection, null);
/*     */   }
/*     */ 
/*     */   public final X509Certificate[] validate(X509Certificate[] paramArrayOfX509Certificate, Collection<X509Certificate> paramCollection, Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 236 */     return validate(paramArrayOfX509Certificate, paramCollection, null, paramObject);
/*     */   }
/*     */ 
/*     */   public final X509Certificate[] validate(X509Certificate[] paramArrayOfX509Certificate, Collection<X509Certificate> paramCollection, AlgorithmConstraints paramAlgorithmConstraints, Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 260 */     paramArrayOfX509Certificate = engineValidate(paramArrayOfX509Certificate, paramCollection, paramAlgorithmConstraints, paramObject);
/*     */ 
/* 263 */     if (paramArrayOfX509Certificate.length > 1) {
/* 264 */       this.endEntityChecker.check(paramArrayOfX509Certificate[0], paramObject);
/*     */     }
/*     */ 
/* 267 */     return paramArrayOfX509Certificate;
/*     */   }
/*     */ 
/*     */   abstract X509Certificate[] engineValidate(X509Certificate[] paramArrayOfX509Certificate, Collection<X509Certificate> paramCollection, AlgorithmConstraints paramAlgorithmConstraints, Object paramObject)
/*     */     throws CertificateException;
/*     */ 
/*     */   public abstract Collection<X509Certificate> getTrustedCertificates();
/*     */ 
/*     */   @Deprecated
/*     */   public void setValidationDate(Date paramDate)
/*     */   {
/* 290 */     this.validationDate = paramDate;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.validator.Validator
 * JD-Core Version:    0.6.2
 */