/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ 
/*     */ @Deprecated
/*     */ public class TrustManagerFactory
/*     */ {
/*     */   private Provider provider;
/*     */   private TrustManagerFactorySpi factorySpi;
/*     */   private String algorithm;
/*     */ 
/*     */   public static final String getDefaultAlgorithm()
/*     */   {
/*  65 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/*  67 */         return Security.getProperty("sun.ssl.trustmanager.type");
/*     */       }
/*     */     });
/*  70 */     if (str == null) {
/*  71 */       str = "SunX509";
/*     */     }
/*  73 */     return str;
/*     */   }
/*     */ 
/*     */   protected TrustManagerFactory(TrustManagerFactorySpi paramTrustManagerFactorySpi, Provider paramProvider, String paramString)
/*     */   {
/*  86 */     this.factorySpi = paramTrustManagerFactorySpi;
/*  87 */     this.provider = paramProvider;
/*  88 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 103 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public static final TrustManagerFactory getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*     */     try
/*     */     {
/* 128 */       Object[] arrayOfObject = SSLSecurity.getImpl(paramString, "TrustManagerFactory", (String)null);
/*     */ 
/* 130 */       return new TrustManagerFactory((TrustManagerFactorySpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*     */     }
/*     */     catch (NoSuchProviderException localNoSuchProviderException) {
/*     */     }
/* 134 */     throw new NoSuchAlgorithmException(paramString + " not found");
/*     */   }
/*     */ 
/*     */   public static final TrustManagerFactory getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 157 */     if ((paramString2 == null) || (paramString2.length() == 0))
/* 158 */       throw new IllegalArgumentException("missing provider");
/* 159 */     Object[] arrayOfObject = SSLSecurity.getImpl(paramString1, "TrustManagerFactory", paramString2);
/*     */ 
/* 161 */     return new TrustManagerFactory((TrustManagerFactorySpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString1);
/*     */   }
/*     */ 
/*     */   public static final TrustManagerFactory getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 182 */     if (paramProvider == null)
/* 183 */       throw new IllegalArgumentException("missing provider");
/* 184 */     Object[] arrayOfObject = SSLSecurity.getImpl(paramString, "TrustManagerFactory", paramProvider);
/*     */ 
/* 186 */     return new TrustManagerFactory((TrustManagerFactorySpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 196 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public void init(KeyStore paramKeyStore)
/*     */     throws KeyStoreException
/*     */   {
/* 209 */     this.factorySpi.engineInit(paramKeyStore);
/*     */   }
/*     */ 
/*     */   public TrustManager[] getTrustManagers()
/*     */   {
/* 217 */     return this.factorySpi.engineGetTrustManagers();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.TrustManagerFactory
 * JD-Core Version:    0.6.2
 */