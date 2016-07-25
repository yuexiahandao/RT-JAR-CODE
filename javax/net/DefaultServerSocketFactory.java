/*     */ package javax.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ 
/*     */ class DefaultServerSocketFactory extends ServerSocketFactory
/*     */ {
/*     */   public ServerSocket createServerSocket()
/*     */     throws IOException
/*     */   {
/* 212 */     return new ServerSocket();
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/* 218 */     return new ServerSocket(paramInt);
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 224 */     return new ServerSocket(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(int paramInt1, int paramInt2, InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/* 231 */     return new ServerSocket(paramInt1, paramInt2, paramInetAddress);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.DefaultServerSocketFactory
 * JD-Core Version:    0.6.2
 */