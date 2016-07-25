/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.security.SecureRandom;
/*     */ import javax.net.ssl.SSLServerSocketFactory;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ @Deprecated
/*     */ public class SSLContext
/*     */ {
/*     */   private Provider provider;
/*     */   private SSLContextSpi contextSpi;
/*     */   private String protocol;
/*     */ 
/*     */   protected SSLContext(SSLContextSpi paramSSLContextSpi, Provider paramProvider, String paramString)
/*     */   {
/*  65 */     this.contextSpi = paramSSLContextSpi;
/*  66 */     this.provider = paramProvider;
/*  67 */     this.protocol = paramString;
/*     */   }
/*     */ 
/*     */   public static SSLContext getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*     */     try
/*     */     {
/*  86 */       Object[] arrayOfObject = SSLSecurity.getImpl(paramString, "SSLContext", (String)null);
/*     */ 
/*  88 */       return new SSLContext((SSLContextSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*     */     } catch (NoSuchProviderException localNoSuchProviderException) {
/*     */     }
/*  91 */     throw new NoSuchAlgorithmException(paramString + " not found");
/*     */   }
/*     */ 
/*     */   public static SSLContext getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 112 */     if ((paramString2 == null) || (paramString2.length() == 0))
/* 113 */       throw new IllegalArgumentException("missing provider");
/* 114 */     Object[] arrayOfObject = SSLSecurity.getImpl(paramString1, "SSLContext", paramString2);
/*     */ 
/* 116 */     return new SSLContext((SSLContextSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString1);
/*     */   }
/*     */ 
/*     */   public static SSLContext getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 135 */     if (paramProvider == null)
/* 136 */       throw new IllegalArgumentException("missing provider");
/* 137 */     Object[] arrayOfObject = SSLSecurity.getImpl(paramString, "SSLContext", paramProvider);
/*     */ 
/* 139 */     return new SSLContext((SSLContextSpi)arrayOfObject[0], (Provider)arrayOfObject[1], paramString);
/*     */   }
/*     */ 
/*     */   public final String getProtocol()
/*     */   {
/* 153 */     return this.protocol;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 162 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final void init(KeyManager[] paramArrayOfKeyManager, TrustManager[] paramArrayOfTrustManager, SecureRandom paramSecureRandom)
/*     */     throws KeyManagementException
/*     */   {
/* 179 */     this.contextSpi.engineInit(paramArrayOfKeyManager, paramArrayOfTrustManager, paramSecureRandom);
/*     */   }
/*     */ 
/*     */   public final SSLSocketFactory getSocketFactory()
/*     */   {
/* 189 */     return this.contextSpi.engineGetSocketFactory();
/*     */   }
/*     */ 
/*     */   public final SSLServerSocketFactory getServerSocketFactory()
/*     */   {
/* 199 */     return this.contextSpi.engineGetServerSocketFactory();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.SSLContext
 * JD-Core Version:    0.6.2
 */