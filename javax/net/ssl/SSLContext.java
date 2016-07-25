/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.security.SecureRandom;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public class SSLContext
/*     */ {
/*     */   private final Provider provider;
/*     */   private final SSLContextSpi contextSpi;
/*     */   private final String protocol;
/*     */   private static SSLContext defaultContext;
/*     */ 
/*     */   protected SSLContext(SSLContextSpi paramSSLContextSpi, Provider paramProvider, String paramString)
/*     */   {
/*  70 */     this.contextSpi = paramSSLContextSpi;
/*  71 */     this.provider = paramProvider;
/*  72 */     this.protocol = paramString;
/*     */   }
/*     */ 
/*     */   public static synchronized SSLContext getDefault()
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  96 */     if (defaultContext == null) {
/*  97 */       defaultContext = getInstance("Default");
/*     */     }
/*  99 */     return defaultContext;
/*     */   }
/*     */ 
/*     */   public static synchronized void setDefault(SSLContext paramSSLContext)
/*     */   {
/* 115 */     if (paramSSLContext == null) {
/* 116 */       throw new NullPointerException();
/*     */     }
/* 118 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 119 */     if (localSecurityManager != null) {
/* 120 */       localSecurityManager.checkPermission(new SSLPermission("setDefaultSSLContext"));
/*     */     }
/* 122 */     defaultContext = paramSSLContext;
/*     */   }
/*     */ 
/*     */   public static SSLContext getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 156 */     GetInstance.Instance localInstance = GetInstance.getInstance("SSLContext", SSLContextSpi.class, paramString);
/*     */ 
/* 158 */     return new SSLContext((SSLContextSpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public static SSLContext getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 199 */     GetInstance.Instance localInstance = GetInstance.getInstance("SSLContext", SSLContextSpi.class, paramString1, paramString2);
/*     */ 
/* 201 */     return new SSLContext((SSLContextSpi)localInstance.impl, localInstance.provider, paramString1);
/*     */   }
/*     */ 
/*     */   public static SSLContext getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 236 */     GetInstance.Instance localInstance = GetInstance.getInstance("SSLContext", SSLContextSpi.class, paramString, paramProvider);
/*     */ 
/* 238 */     return new SSLContext((SSLContextSpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public final String getProtocol()
/*     */   {
/* 252 */     return this.protocol;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 261 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final void init(KeyManager[] paramArrayOfKeyManager, TrustManager[] paramArrayOfTrustManager, SecureRandom paramSecureRandom)
/*     */     throws KeyManagementException
/*     */   {
/* 283 */     this.contextSpi.engineInit(paramArrayOfKeyManager, paramArrayOfTrustManager, paramSecureRandom);
/*     */   }
/*     */ 
/*     */   public final SSLSocketFactory getSocketFactory()
/*     */   {
/* 295 */     return this.contextSpi.engineGetSocketFactory();
/*     */   }
/*     */ 
/*     */   public final SSLServerSocketFactory getServerSocketFactory()
/*     */   {
/* 307 */     return this.contextSpi.engineGetServerSocketFactory();
/*     */   }
/*     */ 
/*     */   public final SSLEngine createSSLEngine()
/*     */   {
/*     */     try
/*     */     {
/* 330 */       return this.contextSpi.engineCreateSSLEngine();
/*     */     } catch (AbstractMethodError localAbstractMethodError) {
/* 332 */       UnsupportedOperationException localUnsupportedOperationException = new UnsupportedOperationException("Provider: " + getProvider() + " doesn't support this operation");
/*     */ 
/* 336 */       localUnsupportedOperationException.initCause(localAbstractMethodError);
/* 337 */       throw localUnsupportedOperationException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final SSLEngine createSSLEngine(String paramString, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 362 */       return this.contextSpi.engineCreateSSLEngine(paramString, paramInt);
/*     */     } catch (AbstractMethodError localAbstractMethodError) {
/* 364 */       UnsupportedOperationException localUnsupportedOperationException = new UnsupportedOperationException("Provider: " + getProvider() + " does not support this operation");
/*     */ 
/* 368 */       localUnsupportedOperationException.initCause(localAbstractMethodError);
/* 369 */       throw localUnsupportedOperationException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final SSLSessionContext getServerSessionContext()
/*     */   {
/* 387 */     return this.contextSpi.engineGetServerSessionContext();
/*     */   }
/*     */ 
/*     */   public final SSLSessionContext getClientSessionContext()
/*     */   {
/* 404 */     return this.contextSpi.engineGetClientSessionContext();
/*     */   }
/*     */ 
/*     */   public final SSLParameters getDefaultSSLParameters()
/*     */   {
/* 420 */     return this.contextSpi.engineGetDefaultSSLParameters();
/*     */   }
/*     */ 
/*     */   public final SSLParameters getSupportedSSLParameters()
/*     */   {
/* 437 */     return this.contextSpi.engineGetSupportedSSLParameters();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLContext
 * JD-Core Version:    0.6.2
 */