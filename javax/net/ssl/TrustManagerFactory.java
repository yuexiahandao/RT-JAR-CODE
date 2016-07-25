/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public class TrustManagerFactory
/*     */ {
/*     */   private Provider provider;
/*     */   private TrustManagerFactorySpi factorySpi;
/*     */   private String algorithm;
/*     */ 
/*     */   public static final String getDefaultAlgorithm()
/*     */   {
/*  67 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/*  69 */         return Security.getProperty("ssl.TrustManagerFactory.algorithm");
/*     */       }
/*     */     });
/*  73 */     if (str == null) {
/*  74 */       str = "SunX509";
/*     */     }
/*  76 */     return str;
/*     */   }
/*     */ 
/*     */   protected TrustManagerFactory(TrustManagerFactorySpi paramTrustManagerFactorySpi, Provider paramProvider, String paramString)
/*     */   {
/*  88 */     this.factorySpi = paramTrustManagerFactorySpi;
/*  89 */     this.provider = paramProvider;
/*  90 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 105 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public static final TrustManagerFactory getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 138 */     GetInstance.Instance localInstance = GetInstance.getInstance("TrustManagerFactory", TrustManagerFactorySpi.class, paramString);
/*     */ 
/* 141 */     return new TrustManagerFactory((TrustManagerFactorySpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public static final TrustManagerFactory getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 182 */     GetInstance.Instance localInstance = GetInstance.getInstance("TrustManagerFactory", TrustManagerFactorySpi.class, paramString1, paramString2);
/*     */ 
/* 185 */     return new TrustManagerFactory((TrustManagerFactorySpi)localInstance.impl, localInstance.provider, paramString1);
/*     */   }
/*     */ 
/*     */   public static final TrustManagerFactory getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 219 */     GetInstance.Instance localInstance = GetInstance.getInstance("TrustManagerFactory", TrustManagerFactorySpi.class, paramString, paramProvider);
/*     */ 
/* 222 */     return new TrustManagerFactory((TrustManagerFactorySpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 232 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final void init(KeyStore paramKeyStore)
/*     */     throws KeyStoreException
/*     */   {
/* 250 */     this.factorySpi.engineInit(paramKeyStore);
/*     */   }
/*     */ 
/*     */   public final void init(ManagerFactoryParameters paramManagerFactoryParameters)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 273 */     this.factorySpi.engineInit(paramManagerFactoryParameters);
/*     */   }
/*     */ 
/*     */   public final TrustManager[] getTrustManagers()
/*     */   {
/* 285 */     return this.factorySpi.engineGetTrustManagers();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.TrustManagerFactory
 * JD-Core Version:    0.6.2
 */