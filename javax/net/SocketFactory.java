/*     */ package javax.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ public abstract class SocketFactory
/*     */ {
/*     */   private static SocketFactory theFactory;
/*     */ 
/*     */   public static SocketFactory getDefault()
/*     */   {
/*  92 */     synchronized (SocketFactory.class) {
/*  93 */       if (theFactory == null)
/*     */       {
/* 100 */         theFactory = new DefaultSocketFactory();
/*     */       }
/*     */     }
/*     */ 
/* 104 */     return theFactory;
/*     */   }
/*     */ 
/*     */   public Socket createSocket()
/*     */     throws IOException
/*     */   {
/* 123 */     UnsupportedOperationException localUnsupportedOperationException = new UnsupportedOperationException();
/*     */ 
/* 125 */     SocketException localSocketException = new SocketException("Unconnected sockets not implemented");
/*     */ 
/* 127 */     localSocketException.initCause(localUnsupportedOperationException);
/* 128 */     throw localSocketException;
/*     */   }
/*     */ 
/*     */   public abstract Socket createSocket(String paramString, int paramInt)
/*     */     throws IOException, UnknownHostException;
/*     */ 
/*     */   public abstract Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*     */     throws IOException, UnknownHostException;
/*     */ 
/*     */   public abstract Socket createSocket(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.SocketFactory
 * JD-Core Version:    0.6.2
 */