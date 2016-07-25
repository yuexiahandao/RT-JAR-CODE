/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.SecureRandom;
/*     */ 
/*     */ public abstract class SSLContextSpi
/*     */ {
/*     */   protected abstract void engineInit(KeyManager[] paramArrayOfKeyManager, TrustManager[] paramArrayOfTrustManager, SecureRandom paramSecureRandom)
/*     */     throws KeyManagementException;
/*     */ 
/*     */   protected abstract SSLSocketFactory engineGetSocketFactory();
/*     */ 
/*     */   protected abstract SSLServerSocketFactory engineGetServerSocketFactory();
/*     */ 
/*     */   protected abstract SSLEngine engineCreateSSLEngine();
/*     */ 
/*     */   protected abstract SSLEngine engineCreateSSLEngine(String paramString, int paramInt);
/*     */ 
/*     */   protected abstract SSLSessionContext engineGetServerSessionContext();
/*     */ 
/*     */   protected abstract SSLSessionContext engineGetClientSessionContext();
/*     */ 
/*     */   private SSLSocket getDefaultSocket()
/*     */   {
/*     */     try
/*     */     {
/* 143 */       SSLSocketFactory localSSLSocketFactory = engineGetSocketFactory();
/* 144 */       return (SSLSocket)localSSLSocketFactory.createSocket();
/*     */     } catch (IOException localIOException) {
/* 146 */       throw new UnsupportedOperationException("Could not obtain parameters", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected SSLParameters engineGetDefaultSSLParameters()
/*     */   {
/* 169 */     SSLSocket localSSLSocket = getDefaultSocket();
/* 170 */     return localSSLSocket.getSSLParameters();
/*     */   }
/*     */ 
/*     */   protected SSLParameters engineGetSupportedSSLParameters()
/*     */   {
/* 193 */     SSLSocket localSSLSocket = getDefaultSocket();
/* 194 */     SSLParameters localSSLParameters = new SSLParameters();
/* 195 */     localSSLParameters.setCipherSuites(localSSLSocket.getSupportedCipherSuites());
/* 196 */     localSSLParameters.setProtocols(localSSLSocket.getSupportedProtocols());
/* 197 */     return localSSLParameters;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLContextSpi
 * JD-Core Version:    0.6.2
 */