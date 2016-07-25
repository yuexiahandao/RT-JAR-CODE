/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ 
/*     */ class DefaultSSLSocketFactory extends SSLSocketFactory
/*     */ {
/*     */   private Exception reason;
/*     */ 
/*     */   DefaultSSLSocketFactory(Exception paramException)
/*     */   {
/* 194 */     this.reason = paramException;
/*     */   }
/*     */ 
/*     */   private Socket throwException() throws SocketException {
/* 198 */     throw ((SocketException)new SocketException(this.reason.toString()).initCause(this.reason));
/*     */   }
/*     */ 
/*     */   public Socket createSocket()
/*     */     throws IOException
/*     */   {
/* 205 */     return throwException();
/*     */   }
/*     */ 
/*     */   public Socket createSocket(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 211 */     return throwException();
/*     */   }
/*     */ 
/*     */   public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 218 */     return throwException();
/*     */   }
/*     */ 
/*     */   public Socket createSocket(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 224 */     return throwException();
/*     */   }
/*     */ 
/*     */   public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 231 */     return throwException();
/*     */   }
/*     */ 
/*     */   public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 238 */     return throwException();
/*     */   }
/*     */ 
/*     */   public String[] getDefaultCipherSuites() {
/* 242 */     return new String[0];
/*     */   }
/*     */ 
/*     */   public String[] getSupportedCipherSuites() {
/* 246 */     return new String[0];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.DefaultSSLSocketFactory
 * JD-Core Version:    0.6.2
 */