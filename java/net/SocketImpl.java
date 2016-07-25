/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public abstract class SocketImpl
/*     */   implements SocketOptions
/*     */ {
/*  48 */   Socket socket = null;
/*  49 */   ServerSocket serverSocket = null;
/*     */   protected FileDescriptor fd;
/*     */   protected InetAddress address;
/*     */   protected int port;
/*     */   protected int localport;
/*     */ 
/*     */   protected abstract void create(boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void connect(String paramString, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void connect(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void connect(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void bind(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void listen(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void accept(SocketImpl paramSocketImpl)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract InputStream getInputStream()
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract OutputStream getOutputStream()
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract int available()
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void close()
/*     */     throws IOException;
/*     */ 
/*     */   protected void shutdownInput()
/*     */     throws IOException
/*     */   {
/* 195 */     throw new IOException("Method not implemented!");
/*     */   }
/*     */ 
/*     */   protected void shutdownOutput()
/*     */     throws IOException
/*     */   {
/* 215 */     throw new IOException("Method not implemented!");
/*     */   }
/*     */ 
/*     */   protected FileDescriptor getFileDescriptor()
/*     */   {
/* 225 */     return this.fd;
/*     */   }
/*     */ 
/*     */   protected InetAddress getInetAddress()
/*     */   {
/* 235 */     return this.address;
/*     */   }
/*     */ 
/*     */   protected int getPort()
/*     */   {
/* 245 */     return this.port;
/*     */   }
/*     */ 
/*     */   protected boolean supportsUrgentData()
/*     */   {
/* 258 */     return false;
/*     */   }
/*     */ 
/*     */   protected abstract void sendUrgentData(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   protected int getLocalPort()
/*     */   {
/* 278 */     return this.localport;
/*     */   }
/*     */ 
/*     */   void setSocket(Socket paramSocket) {
/* 282 */     this.socket = paramSocket;
/*     */   }
/*     */ 
/*     */   Socket getSocket() {
/* 286 */     return this.socket;
/*     */   }
/*     */ 
/*     */   void setServerSocket(ServerSocket paramServerSocket) {
/* 290 */     this.serverSocket = paramServerSocket;
/*     */   }
/*     */ 
/*     */   ServerSocket getServerSocket() {
/* 294 */     return this.serverSocket;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 303 */     return "Socket[addr=" + getInetAddress() + ",port=" + getPort() + ",localport=" + getLocalPort() + "]";
/*     */   }
/*     */ 
/*     */   void reset() throws IOException
/*     */   {
/* 308 */     this.address = null;
/* 309 */     this.port = 0;
/* 310 */     this.localport = 0;
/*     */   }
/*     */ 
/*     */   protected void setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SocketImpl
 * JD-Core Version:    0.6.2
 */