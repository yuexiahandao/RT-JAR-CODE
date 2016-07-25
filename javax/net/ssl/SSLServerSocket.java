/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ 
/*     */ public abstract class SSLServerSocket extends ServerSocket
/*     */ {
/*     */   protected SSLServerSocket()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected SSLServerSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/* 100 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected SSLServerSocket(int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 136 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected SSLServerSocket(int paramInt1, int paramInt2, InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/* 181 */     super(paramInt1, paramInt2, paramInetAddress);
/*     */   }
/*     */ 
/*     */   public abstract String[] getEnabledCipherSuites();
/*     */ 
/*     */   public abstract void setEnabledCipherSuites(String[] paramArrayOfString);
/*     */ 
/*     */   public abstract String[] getSupportedCipherSuites();
/*     */ 
/*     */   public abstract String[] getSupportedProtocols();
/*     */ 
/*     */   public abstract String[] getEnabledProtocols();
/*     */ 
/*     */   public abstract void setEnabledProtocols(String[] paramArrayOfString);
/*     */ 
/*     */   public abstract void setNeedClientAuth(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getNeedClientAuth();
/*     */ 
/*     */   public abstract void setWantClientAuth(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getWantClientAuth();
/*     */ 
/*     */   public abstract void setUseClientMode(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getUseClientMode();
/*     */ 
/*     */   public abstract void setEnableSessionCreation(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getEnableSessionCreation();
/*     */ 
/*     */   public SSLParameters getSSLParameters()
/*     */   {
/* 469 */     SSLParameters localSSLParameters = new SSLParameters();
/*     */ 
/* 471 */     localSSLParameters.setCipherSuites(getEnabledCipherSuites());
/* 472 */     localSSLParameters.setProtocols(getEnabledProtocols());
/* 473 */     if (getNeedClientAuth())
/* 474 */       localSSLParameters.setNeedClientAuth(true);
/* 475 */     else if (getWantClientAuth()) {
/* 476 */       localSSLParameters.setWantClientAuth(true);
/*     */     }
/*     */ 
/* 479 */     return localSSLParameters;
/*     */   }
/*     */ 
/*     */   public void setSSLParameters(SSLParameters paramSSLParameters)
/*     */   {
/* 508 */     String[] arrayOfString = paramSSLParameters.getCipherSuites();
/* 509 */     if (arrayOfString != null) {
/* 510 */       setEnabledCipherSuites(arrayOfString);
/*     */     }
/*     */ 
/* 513 */     arrayOfString = paramSSLParameters.getProtocols();
/* 514 */     if (arrayOfString != null) {
/* 515 */       setEnabledProtocols(arrayOfString);
/*     */     }
/*     */ 
/* 518 */     if (paramSSLParameters.getNeedClientAuth())
/* 519 */       setNeedClientAuth(true);
/* 520 */     else if (paramSSLParameters.getWantClientAuth())
/* 521 */       setWantClientAuth(true);
/*     */     else
/* 523 */       setWantClientAuth(false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLServerSocket
 * JD-Core Version:    0.6.2
 */