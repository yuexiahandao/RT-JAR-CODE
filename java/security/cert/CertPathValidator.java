/*     */ package java.security.cert;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public class CertPathValidator
/*     */ {
/*     */   private static final String CPV_TYPE = "certpathvalidator.type";
/*  99 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private CertPathValidatorSpi validatorSpi;
/*     */   private Provider provider;
/*     */   private String algorithm;
/*     */ 
/*     */   protected CertPathValidator(CertPathValidatorSpi paramCertPathValidatorSpi, Provider paramProvider, String paramString)
/*     */   {
/* 115 */     this.validatorSpi = paramCertPathValidatorSpi;
/* 116 */     this.provider = paramProvider;
/* 117 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public static CertPathValidator getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 150 */     GetInstance.Instance localInstance = GetInstance.getInstance("CertPathValidator", CertPathValidatorSpi.class, paramString);
/*     */ 
/* 152 */     return new CertPathValidator((CertPathValidatorSpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public static CertPathValidator getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 194 */     GetInstance.Instance localInstance = GetInstance.getInstance("CertPathValidator", CertPathValidatorSpi.class, paramString1, paramString2);
/*     */ 
/* 196 */     return new CertPathValidator((CertPathValidatorSpi)localInstance.impl, localInstance.provider, paramString1);
/*     */   }
/*     */ 
/*     */   public static CertPathValidator getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 231 */     GetInstance.Instance localInstance = GetInstance.getInstance("CertPathValidator", CertPathValidatorSpi.class, paramString, paramProvider);
/*     */ 
/* 233 */     return new CertPathValidator((CertPathValidatorSpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 244 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 253 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public final CertPathValidatorResult validate(CertPath paramCertPath, CertPathParameters paramCertPathParameters)
/*     */     throws CertPathValidatorException, InvalidAlgorithmParameterException
/*     */   {
/* 279 */     return this.validatorSpi.engineValidate(paramCertPath, paramCertPathParameters);
/*     */   }
/*     */ 
/*     */   public static final String getDefaultType()
/*     */   {
/* 305 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/* 307 */         return Security.getProperty("certpathvalidator.type");
/*     */       }
/*     */     });
/* 310 */     if (str == null) {
/* 311 */       str = "PKIX";
/*     */     }
/* 313 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertPathValidator
 * JD-Core Version:    0.6.2
 */