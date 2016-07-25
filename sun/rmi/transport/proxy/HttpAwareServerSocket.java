/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import sun.rmi.runtime.Log;
/*     */ 
/*     */ class HttpAwareServerSocket extends ServerSocket
/*     */ {
/*     */   public HttpAwareServerSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/*  50 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public HttpAwareServerSocket(int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  64 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Socket accept()
/*     */     throws IOException
/*     */   {
/*  80 */     Socket localSocket = super.accept();
/*  81 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(localSocket.getInputStream());
/*     */ 
/*  84 */     RMIMasterSocketFactory.proxyLog.log(Log.BRIEF, "socket accepted (checking for POST)");
/*     */ 
/*  87 */     localBufferedInputStream.mark(4);
/*  88 */     int i = (localBufferedInputStream.read() == 80) && (localBufferedInputStream.read() == 79) && (localBufferedInputStream.read() == 83) && (localBufferedInputStream.read() == 84) ? 1 : 0;
/*     */ 
/*  92 */     localBufferedInputStream.reset();
/*     */ 
/*  94 */     if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.BRIEF)) {
/*  95 */       RMIMasterSocketFactory.proxyLog.log(Log.BRIEF, i != 0 ? "POST found, HTTP socket returned" : "POST not found, direct socket returned");
/*     */     }
/*     */ 
/* 100 */     if (i != 0) {
/* 101 */       return new HttpReceiveSocket(localSocket, localBufferedInputStream, null);
/*     */     }
/* 103 */     return new WrappedSocket(localSocket, localBufferedInputStream, null);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 112 */     return "HttpAware" + super.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.HttpAwareServerSocket
 * JD-Core Version:    0.6.2
 */