/*     */ package java.security.cert;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public class CertificateFactory
/*     */ {
/*     */   private String type;
/*     */   private Provider provider;
/*     */   private CertificateFactorySpi certFacSpi;
/*     */ 
/*     */   protected CertificateFactory(CertificateFactorySpi paramCertificateFactorySpi, Provider paramProvider, String paramString)
/*     */   {
/* 148 */     this.certFacSpi = paramCertificateFactorySpi;
/* 149 */     this.provider = paramProvider;
/* 150 */     this.type = paramString;
/*     */   }
/*     */ 
/*     */   public static final CertificateFactory getInstance(String paramString)
/*     */     throws CertificateException
/*     */   {
/*     */     try
/*     */     {
/* 183 */       GetInstance.Instance localInstance = GetInstance.getInstance("CertificateFactory", CertificateFactorySpi.class, paramString);
/*     */ 
/* 185 */       return new CertificateFactory((CertificateFactorySpi)localInstance.impl, localInstance.provider, paramString);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 188 */       throw new CertificateException(paramString + " not found", localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final CertificateFactory getInstance(String paramString1, String paramString2)
/*     */     throws CertificateException, NoSuchProviderException
/*     */   {
/*     */     try
/*     */     {
/* 230 */       GetInstance.Instance localInstance = GetInstance.getInstance("CertificateFactory", CertificateFactorySpi.class, paramString1, paramString2);
/*     */ 
/* 232 */       return new CertificateFactory((CertificateFactorySpi)localInstance.impl, localInstance.provider, paramString1);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 235 */       throw new CertificateException(paramString1 + " not found", localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final CertificateFactory getInstance(String paramString, Provider paramProvider)
/*     */     throws CertificateException
/*     */   {
/*     */     try
/*     */     {
/* 271 */       GetInstance.Instance localInstance = GetInstance.getInstance("CertificateFactory", CertificateFactorySpi.class, paramString, paramProvider);
/*     */ 
/* 273 */       return new CertificateFactory((CertificateFactorySpi)localInstance.impl, localInstance.provider, paramString);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 276 */       throw new CertificateException(paramString + " not found", localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 286 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final String getType()
/*     */   {
/* 297 */     return this.type;
/*     */   }
/*     */ 
/*     */   public final Certificate generateCertificate(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 339 */     return this.certFacSpi.engineGenerateCertificate(paramInputStream);
/*     */   }
/*     */ 
/*     */   public final Iterator<String> getCertPathEncodings()
/*     */   {
/* 359 */     return this.certFacSpi.engineGetCertPathEncodings();
/*     */   }
/*     */ 
/*     */   public final CertPath generateCertPath(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 378 */     return this.certFacSpi.engineGenerateCertPath(paramInputStream);
/*     */   }
/*     */ 
/*     */   public final CertPath generateCertPath(InputStream paramInputStream, String paramString)
/*     */     throws CertificateException
/*     */   {
/* 401 */     return this.certFacSpi.engineGenerateCertPath(paramInputStream, paramString);
/*     */   }
/*     */ 
/*     */   public final CertPath generateCertPath(List<? extends Certificate> paramList)
/*     */     throws CertificateException
/*     */   {
/* 422 */     return this.certFacSpi.engineGenerateCertPath(paramList);
/*     */   }
/*     */ 
/*     */   public final Collection<? extends Certificate> generateCertificates(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 462 */     return this.certFacSpi.engineGenerateCertificates(paramInputStream);
/*     */   }
/*     */ 
/*     */   public final CRL generateCRL(InputStream paramInputStream)
/*     */     throws CRLException
/*     */   {
/* 497 */     return this.certFacSpi.engineGenerateCRL(paramInputStream);
/*     */   }
/*     */ 
/*     */   public final Collection<? extends CRL> generateCRLs(InputStream paramInputStream)
/*     */     throws CRLException
/*     */   {
/* 535 */     return this.certFacSpi.engineGenerateCRLs(paramInputStream);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertificateFactory
 * JD-Core Version:    0.6.2
 */