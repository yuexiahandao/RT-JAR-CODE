/*     */ package java.security.cert;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.Collection;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public class CertStore
/*     */ {
/*     */   private static final String CERTSTORE_TYPE = "certstore.type";
/*     */   private CertStoreSpi storeSpi;
/*     */   private Provider provider;
/*     */   private String type;
/*     */   private CertStoreParameters params;
/*     */ 
/*     */   protected CertStore(CertStoreSpi paramCertStoreSpi, Provider paramProvider, String paramString, CertStoreParameters paramCertStoreParameters)
/*     */   {
/* 117 */     this.storeSpi = paramCertStoreSpi;
/* 118 */     this.provider = paramProvider;
/* 119 */     this.type = paramString;
/* 120 */     if (paramCertStoreParameters != null)
/* 121 */       this.params = ((CertStoreParameters)paramCertStoreParameters.clone());
/*     */   }
/*     */ 
/*     */   public final Collection<? extends Certificate> getCertificates(CertSelector paramCertSelector)
/*     */     throws CertStoreException
/*     */   {
/* 151 */     return this.storeSpi.engineGetCertificates(paramCertSelector);
/*     */   }
/*     */ 
/*     */   public final Collection<? extends CRL> getCRLs(CRLSelector paramCRLSelector)
/*     */     throws CertStoreException
/*     */   {
/* 181 */     return this.storeSpi.engineGetCRLs(paramCRLSelector);
/*     */   }
/*     */ 
/*     */   public static CertStore getInstance(String paramString, CertStoreParameters paramCertStoreParameters)
/*     */     throws InvalidAlgorithmParameterException, NoSuchAlgorithmException
/*     */   {
/*     */     try
/*     */     {
/* 228 */       GetInstance.Instance localInstance = GetInstance.getInstance("CertStore", CertStoreSpi.class, paramString, paramCertStoreParameters);
/*     */ 
/* 230 */       return new CertStore((CertStoreSpi)localInstance.impl, localInstance.provider, paramString, paramCertStoreParameters);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 233 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static CertStore handleException(NoSuchAlgorithmException paramNoSuchAlgorithmException) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/* 239 */     Throwable localThrowable = paramNoSuchAlgorithmException.getCause();
/* 240 */     if ((localThrowable instanceof InvalidAlgorithmParameterException)) {
/* 241 */       throw ((InvalidAlgorithmParameterException)localThrowable);
/*     */     }
/* 243 */     throw paramNoSuchAlgorithmException;
/*     */   }
/*     */ 
/*     */   public static CertStore getInstance(String paramString1, CertStoreParameters paramCertStoreParameters, String paramString2)
/*     */     throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/*     */     try
/*     */     {
/* 298 */       GetInstance.Instance localInstance = GetInstance.getInstance("CertStore", CertStoreSpi.class, paramString1, paramCertStoreParameters, paramString2);
/*     */ 
/* 300 */       return new CertStore((CertStoreSpi)localInstance.impl, localInstance.provider, paramString1, paramCertStoreParameters);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 303 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static CertStore getInstance(String paramString, CertStoreParameters paramCertStoreParameters, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/*     */     try
/*     */     {
/* 352 */       GetInstance.Instance localInstance = GetInstance.getInstance("CertStore", CertStoreSpi.class, paramString, paramCertStoreParameters, paramProvider);
/*     */ 
/* 354 */       return new CertStore((CertStoreSpi)localInstance.impl, localInstance.provider, paramString, paramCertStoreParameters);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 357 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final CertStoreParameters getCertStoreParameters()
/*     */   {
/* 370 */     return this.params == null ? null : (CertStoreParameters)this.params.clone();
/*     */   }
/*     */ 
/*     */   public final String getType()
/*     */   {
/* 379 */     return this.type;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 388 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public static final String getDefaultType()
/*     */   {
/* 414 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/* 416 */         return Security.getProperty("certstore.type");
/*     */       }
/*     */     });
/* 419 */     if (str == null) {
/* 420 */       str = "LDAP";
/*     */     }
/* 422 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertStore
 * JD-Core Version:    0.6.2
 */