/*     */ package sun.rmi.transport.tcp;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.transport.Channel;
/*     */ import sun.rmi.transport.Connection;
/*     */ import sun.rmi.transport.proxy.RMISocketInfo;
/*     */ 
/*     */ public class TCPConnection
/*     */   implements Connection
/*     */ {
/*     */   private Socket socket;
/*     */   private Channel channel;
/*  42 */   private InputStream in = null;
/*  43 */   private OutputStream out = null;
/*  44 */   private long expiration = 9223372036854775807L;
/*  45 */   private long lastuse = -9223372036854775808L;
/*  46 */   private long roundtrip = 5L;
/*     */ 
/*     */   TCPConnection(TCPChannel paramTCPChannel, Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream)
/*     */   {
/*  54 */     this.socket = paramSocket;
/*  55 */     this.channel = paramTCPChannel;
/*  56 */     this.in = paramInputStream;
/*  57 */     this.out = paramOutputStream;
/*     */   }
/*     */ 
/*     */   TCPConnection(TCPChannel paramTCPChannel, InputStream paramInputStream, OutputStream paramOutputStream)
/*     */   {
/*  66 */     this(paramTCPChannel, null, paramInputStream, paramOutputStream);
/*     */   }
/*     */ 
/*     */   TCPConnection(TCPChannel paramTCPChannel, Socket paramSocket)
/*     */   {
/*  75 */     this(paramTCPChannel, paramSocket, null, null);
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/*  83 */     if (this.out == null)
/*  84 */       this.out = new BufferedOutputStream(this.socket.getOutputStream());
/*  85 */     return this.out;
/*     */   }
/*     */ 
/*     */   public void releaseOutputStream()
/*     */     throws IOException
/*     */   {
/*  93 */     if (this.out != null)
/*  94 */       this.out.flush();
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 102 */     if (this.in == null)
/* 103 */       this.in = new BufferedInputStream(this.socket.getInputStream());
/* 104 */     return this.in;
/*     */   }
/*     */ 
/*     */   public void releaseInputStream()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isReusable()
/*     */   {
/* 123 */     if ((this.socket != null) && ((this.socket instanceof RMISocketInfo))) {
/* 124 */       return ((RMISocketInfo)this.socket).isReusable();
/*     */     }
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   void setExpiration(long paramLong)
/*     */   {
/* 135 */     this.expiration = paramLong;
/*     */   }
/*     */ 
/*     */   void setLastUseTime(long paramLong)
/*     */   {
/* 146 */     this.lastuse = paramLong;
/*     */   }
/*     */ 
/*     */   boolean expired(long paramLong)
/*     */   {
/* 156 */     return this.expiration <= paramLong;
/*     */   }
/*     */ 
/*     */   public boolean isDead()
/*     */   {
/* 175 */     long l = System.currentTimeMillis();
/* 176 */     if ((this.roundtrip > 0L) && (l < this.lastuse + this.roundtrip))
/* 177 */       return false; InputStream localInputStream;
/*     */     OutputStream localOutputStream;
/*     */     try {
/* 181 */       localInputStream = getInputStream();
/* 182 */       localOutputStream = getOutputStream();
/*     */     } catch (IOException localIOException1) {
/* 184 */       return true;
/*     */     }
/*     */ 
/* 188 */     int i = 0;
/*     */     try {
/* 190 */       localOutputStream.write(82);
/* 191 */       localOutputStream.flush();
/* 192 */       i = localInputStream.read();
/*     */     } catch (IOException localIOException2) {
/* 194 */       TCPTransport.tcpLog.log(Log.VERBOSE, "exception: ", localIOException2);
/* 195 */       TCPTransport.tcpLog.log(Log.BRIEF, "server ping failed");
/*     */ 
/* 197 */       return true;
/*     */     }
/*     */ 
/* 200 */     if (i == 83)
/*     */     {
/* 202 */       this.roundtrip = ((System.currentTimeMillis() - l) * 2L);
/*     */ 
/* 204 */       return false;
/*     */     }
/*     */ 
/* 207 */     if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
/* 208 */       TCPTransport.tcpLog.log(Log.BRIEF, "server protocol error: ping response = " + i);
/*     */     }
/*     */ 
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 219 */     TCPTransport.tcpLog.log(Log.BRIEF, "close connection");
/*     */ 
/* 221 */     if (this.socket != null) {
/* 222 */       this.socket.close();
/*     */     } else {
/* 224 */       this.in.close();
/* 225 */       this.out.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Channel getChannel()
/*     */   {
/* 234 */     return this.channel;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.tcp.TCPConnection
 * JD-Core Version:    0.6.2
 */