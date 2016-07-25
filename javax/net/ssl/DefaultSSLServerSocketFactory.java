/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketException;
/*     */ 
/*     */ class DefaultSSLServerSocketFactory extends SSLServerSocketFactory
/*     */ {
/*     */   private final Exception reason;
/*     */ 
/*     */   DefaultSSLServerSocketFactory(Exception paramException)
/*     */   {
/* 155 */     this.reason = paramException;
/*     */   }
/*     */ 
/*     */   private ServerSocket throwException() throws SocketException {
/* 159 */     throw ((SocketException)new SocketException(this.reason.toString()).initCause(this.reason));
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket() throws IOException
/*     */   {
/* 164 */     return throwException();
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/* 171 */     return throwException();
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 177 */     return throwException();
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(int paramInt1, int paramInt2, InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/* 184 */     return throwException();
/*     */   }
/*     */ 
/*     */   public String[] getDefaultCipherSuites() {
/* 188 */     return new String[0];
/*     */   }
/*     */ 
/*     */   public String[] getSupportedCipherSuites() {
/* 192 */     return new String[0];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.DefaultSSLServerSocketFactory
 * JD-Core Version:    0.6.2
 */