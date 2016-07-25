/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketImpl;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ class WrappedSocket extends Socket
/*     */ {
/*     */   protected Socket socket;
/*  47 */   protected InputStream in = null;
/*     */ 
/*  50 */   protected OutputStream out = null;
/*     */ 
/*     */   public WrappedSocket(Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  65 */     super((SocketImpl)null);
/*  66 */     this.socket = paramSocket;
/*  67 */     this.in = paramInputStream;
/*  68 */     this.out = paramOutputStream;
/*     */   }
/*     */ 
/*     */   public InetAddress getInetAddress()
/*     */   {
/*  76 */     return this.socket.getInetAddress();
/*     */   }
/*     */ 
/*     */   public InetAddress getLocalAddress()
/*     */   {
/*  83 */     return (InetAddress)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public InetAddress run()
/*     */       {
/*  87 */         return WrappedSocket.this.socket.getLocalAddress();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/*  98 */     return this.socket.getPort();
/*     */   }
/*     */ 
/*     */   public int getLocalPort()
/*     */   {
/* 106 */     return this.socket.getLocalPort();
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 114 */     if (this.in == null)
/* 115 */       this.in = this.socket.getInputStream();
/* 116 */     return this.in;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 124 */     if (this.out == null)
/* 125 */       this.out = this.socket.getOutputStream();
/* 126 */     return this.out;
/*     */   }
/*     */ 
/*     */   public void setTcpNoDelay(boolean paramBoolean)
/*     */     throws SocketException
/*     */   {
/* 134 */     this.socket.setTcpNoDelay(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getTcpNoDelay()
/*     */     throws SocketException
/*     */   {
/* 142 */     return this.socket.getTcpNoDelay();
/*     */   }
/*     */ 
/*     */   public void setSoLinger(boolean paramBoolean, int paramInt)
/*     */     throws SocketException
/*     */   {
/* 150 */     this.socket.setSoLinger(paramBoolean, paramInt);
/*     */   }
/*     */ 
/*     */   public int getSoLinger()
/*     */     throws SocketException
/*     */   {
/* 158 */     return this.socket.getSoLinger();
/*     */   }
/*     */ 
/*     */   public synchronized void setSoTimeout(int paramInt)
/*     */     throws SocketException
/*     */   {
/* 166 */     this.socket.setSoTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized int getSoTimeout()
/*     */     throws SocketException
/*     */   {
/* 174 */     return this.socket.getSoTimeout();
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws IOException
/*     */   {
/* 182 */     this.socket.close();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 190 */     return "Wrapped" + this.socket.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.WrappedSocket
 * JD-Core Version:    0.6.2
 */