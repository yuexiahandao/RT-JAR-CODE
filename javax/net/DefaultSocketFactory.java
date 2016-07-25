/*     */ package javax.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ class DefaultSocketFactory extends SocketFactory
/*     */ {
/*     */   public Socket createSocket()
/*     */   {
/* 265 */     return new Socket();
/*     */   }
/*     */ 
/*     */   public Socket createSocket(String paramString, int paramInt)
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 271 */     return new Socket(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public Socket createSocket(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 277 */     return new Socket(paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 284 */     return new Socket(paramString, paramInt1, paramInetAddress, paramInt2);
/*     */   }
/*     */ 
/*     */   public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 291 */     return new Socket(paramInetAddress1, paramInt1, paramInetAddress2, paramInt2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.DefaultSocketFactory
 * JD-Core Version:    0.6.2
 */