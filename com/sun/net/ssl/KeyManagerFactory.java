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
/*     */ import java.security.UnrecoverableKeyException;
/*     */ 
/*     */ @Deprecated
/*     */ public class KeyManagerFactory
/*     */ {
/*     */   private Provider provider;
/*     */   private KeyManagerFactorySpi factorySpi;
/*     */   private String algorithm;
/*     */ 
/*     */   public static final String getDefaultAlgorithm()
/*     */   {
/*  65 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/*  67 */         return Security.getProperty("sun.ssl.keymanager.type");
/*     */       }
/*     */     });
/*  70 */     if (str == null) {
/*  71 */       str = "SunX509";
/*     */     }
/*  73 */     return str;
/*     */   }
/*     */ 
/*     */   protected KeyManagerFactory(KeyManagerFactorySpi paramKeyManagerFactorySpi, Provider paramProvider, String paramString)
/*     */   {
/*  86 */     this.factorySpi = paramKeyManagerFactorySpi;
/*  87 */     this.provider = paramProvider;
/*  88 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 101 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public static final KeyManagerFactory getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*     */     try
/*     */     {
/* 126 */       Object[] arrayOfObject = SSLSecurity.getImpl(paramString, "KeyManagerFactory", (String)null);
/*     */ 
/* 128 */       return new KeyManagerFactory((KeyManagerFactorySpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*     */     }
/*     */     catch (NoSuchProviderException localNoSuchProviderException) {
/*     */     }
/* 132 */     throw new NoSuchAlgorithmException(paramString + " not found");
/*     */   }
/*     */ 
/*     */   public static final KeyManagerFactory getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 155 */     if ((paramString2 == null) || (paramString2.length() == 0))
/* 156 */       throw new IllegalArgumentException("missing provider");
/* 157 */     Object[] arrayOfObject = SSLSecurity.getImpl(paramString1, "KeyManagerFactory", paramString2);
/*     */ 
/* 159 */     return new KeyManagerFactory((KeyManagerFactorySpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString1);
/*     */   }
/*     */ 
/*     */   public static final KeyManagerFactory getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 180 */     if (paramProvider == null)
/* 181 */       throw new IllegalArgumentException("missing provider");
/* 182 */     Object[] arrayOfObject = SSLSecurity.getImpl(paramString, "KeyManagerFactory", paramProvider);
/*     */ 
/* 184 */     return new KeyManagerFactory((KeyManagerFactorySpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 194 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public void init(KeyStore paramKeyStore, char[] paramArrayOfChar)
/*     */     throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
/*     */   {
/* 209 */     this.factorySpi.engineInit(paramKeyStore, paramArrayOfChar);
/*     */   }
/*     */ 
/*     */   public KeyManager[] getKeyManagers()
/*     */   {
/* 217 */     return this.factorySpi.engineGetKeyManagers();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.KeyManagerFactory
 * JD-Core Version:    0.6.2
 */