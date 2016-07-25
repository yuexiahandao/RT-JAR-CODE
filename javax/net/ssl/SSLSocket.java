/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ public abstract class SSLSocket extends Socket
/*     */ {
/*     */   protected SSLSocket()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected SSLSocket(String paramString, int paramInt)
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 174 */     super(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   protected SSLSocket(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 199 */     super(paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   protected SSLSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 231 */     super(paramString, paramInt1, paramInetAddress, paramInt2);
/*     */   }
/*     */ 
/*     */   protected SSLSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 262 */     super(paramInetAddress1, paramInt1, paramInetAddress2, paramInt2);
/*     */   }
/*     */ 
/*     */   public abstract String[] getSupportedCipherSuites();
/*     */ 
/*     */   public abstract String[] getEnabledCipherSuites();
/*     */ 
/*     */   public abstract void setEnabledCipherSuites(String[] paramArrayOfString);
/*     */ 
/*     */   public abstract String[] getSupportedProtocols();
/*     */ 
/*     */   public abstract String[] getEnabledProtocols();
/*     */ 
/*     */   public abstract void setEnabledProtocols(String[] paramArrayOfString);
/*     */ 
/*     */   public abstract SSLSession getSession();
/*     */ 
/*     */   public SSLSession getHandshakeSession()
/*     */   {
/* 414 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract void addHandshakeCompletedListener(HandshakeCompletedListener paramHandshakeCompletedListener);
/*     */ 
/*     */   public abstract void removeHandshakeCompletedListener(HandshakeCompletedListener paramHandshakeCompletedListener);
/*     */ 
/*     */   public abstract void startHandshake()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void setUseClientMode(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getUseClientMode();
/*     */ 
/*     */   public abstract void setNeedClientAuth(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getNeedClientAuth();
/*     */ 
/*     */   public abstract void setWantClientAuth(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getWantClientAuth();
/*     */ 
/*     */   public abstract void setEnableSessionCreation(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getEnableSessionCreation();
/*     */ 
/*     */   public SSLParameters getSSLParameters()
/*     */   {
/* 613 */     SSLParameters localSSLParameters = new SSLParameters();
/* 614 */     localSSLParameters.setCipherSuites(getEnabledCipherSuites());
/* 615 */     localSSLParameters.setProtocols(getEnabledProtocols());
/* 616 */     if (getNeedClientAuth())
/* 617 */       localSSLParameters.setNeedClientAuth(true);
/* 618 */     else if (getWantClientAuth()) {
/* 619 */       localSSLParameters.setWantClientAuth(true);
/*     */     }
/* 621 */     return localSSLParameters;
/*     */   }
/*     */ 
/*     */   public void setSSLParameters(SSLParameters paramSSLParameters)
/*     */   {
/* 647 */     String[] arrayOfString = paramSSLParameters.getCipherSuites();
/* 648 */     if (arrayOfString != null) {
/* 649 */       setEnabledCipherSuites(arrayOfString);
/*     */     }
/* 651 */     arrayOfString = paramSSLParameters.getProtocols();
/* 652 */     if (arrayOfString != null) {
/* 653 */       setEnabledProtocols(arrayOfString);
/*     */     }
/* 655 */     if (paramSSLParameters.getNeedClientAuth())
/* 656 */       setNeedClientAuth(true);
/* 657 */     else if (paramSSLParameters.getWantClientAuth())
/* 658 */       setWantClientAuth(true);
/*     */     else
/* 660 */       setWantClientAuth(false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLSocket
 * JD-Core Version:    0.6.2
 */