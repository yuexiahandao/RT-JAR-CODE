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
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public class KeyManagerFactory
/*     */ {
/*     */   private Provider provider;
/*     */   private KeyManagerFactorySpi factorySpi;
/*     */   private String algorithm;
/*     */ 
/*     */   public static final String getDefaultAlgorithm()
/*     */   {
/*  70 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/*  72 */         return Security.getProperty("ssl.KeyManagerFactory.algorithm");
/*     */       }
/*     */     });
/*  76 */     if (str == null) {
/*  77 */       str = "SunX509";
/*     */     }
/*  79 */     return str;
/*     */   }
/*     */ 
/*     */   protected KeyManagerFactory(KeyManagerFactorySpi paramKeyManagerFactorySpi, Provider paramProvider, String paramString)
/*     */   {
/*  91 */     this.factorySpi = paramKeyManagerFactorySpi;
/*  92 */     this.provider = paramProvider;
/*  93 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 106 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public static final KeyManagerFactory getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 139 */     GetInstance.Instance localInstance = GetInstance.getInstance("KeyManagerFactory", KeyManagerFactorySpi.class, paramString);
/*     */ 
/* 142 */     return new KeyManagerFactory((KeyManagerFactorySpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public static final KeyManagerFactory getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 183 */     GetInstance.Instance localInstance = GetInstance.getInstance("KeyManagerFactory", KeyManagerFactorySpi.class, paramString1, paramString2);
/*     */ 
/* 186 */     return new KeyManagerFactory((KeyManagerFactorySpi)localInstance.impl, localInstance.provider, paramString1);
/*     */   }
/*     */ 
/*     */   public static final KeyManagerFactory getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 220 */     GetInstance.Instance localInstance = GetInstance.getInstance("KeyManagerFactory", KeyManagerFactorySpi.class, paramString, paramProvider);
/*     */ 
/* 223 */     return new KeyManagerFactory((KeyManagerFactorySpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 233 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final void init(KeyStore paramKeyStore, char[] paramArrayOfChar)
/*     */     throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
/*     */   {
/* 259 */     this.factorySpi.engineInit(paramKeyStore, paramArrayOfChar);
/*     */   }
/*     */ 
/*     */   public final void init(ManagerFactoryParameters paramManagerFactoryParameters)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 281 */     this.factorySpi.engineInit(paramManagerFactoryParameters);
/*     */   }
/*     */ 
/*     */   public final KeyManager[] getKeyManagers()
/*     */   {
/* 292 */     return this.factorySpi.engineGetKeyManagers();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.KeyManagerFactory
 * JD-Core Version:    0.6.2
 */