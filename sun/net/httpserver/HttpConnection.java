/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.logging.Logger;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ 
/*     */ class HttpConnection
/*     */ {
/*     */   HttpContextImpl context;
/*     */   SSLEngine engine;
/*     */   SSLContext sslContext;
/*     */   SSLStreams sslStreams;
/*     */   InputStream i;
/*     */   InputStream raw;
/*     */   OutputStream rawout;
/*     */   SocketChannel chan;
/*     */   SelectionKey selectionKey;
/*     */   String protocol;
/*     */   long time;
/*     */   volatile long creationTime;
/*     */   volatile long rspStartedTime;
/*     */   int remaining;
/*  61 */   boolean closed = false;
/*     */   Logger logger;
/*     */   volatile State state;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  68 */     String str = null;
/*  69 */     if (this.chan != null) {
/*  70 */       str = this.chan.toString();
/*     */     }
/*  72 */     return str;
/*     */   }
/*     */ 
/*     */   void setChannel(SocketChannel paramSocketChannel)
/*     */   {
/*  79 */     this.chan = paramSocketChannel;
/*     */   }
/*     */ 
/*     */   void setContext(HttpContextImpl paramHttpContextImpl) {
/*  83 */     this.context = paramHttpContextImpl;
/*     */   }
/*     */ 
/*     */   State getState() {
/*  87 */     return this.state;
/*     */   }
/*     */ 
/*     */   void setState(State paramState) {
/*  91 */     this.state = paramState;
/*     */   }
/*     */ 
/*     */   void setParameters(InputStream paramInputStream1, OutputStream paramOutputStream, SocketChannel paramSocketChannel, SSLEngine paramSSLEngine, SSLStreams paramSSLStreams, SSLContext paramSSLContext, String paramString, HttpContextImpl paramHttpContextImpl, InputStream paramInputStream2)
/*     */   {
/* 100 */     this.context = paramHttpContextImpl;
/* 101 */     this.i = paramInputStream1;
/* 102 */     this.rawout = paramOutputStream;
/* 103 */     this.raw = paramInputStream2;
/* 104 */     this.protocol = paramString;
/* 105 */     this.engine = paramSSLEngine;
/* 106 */     this.chan = paramSocketChannel;
/* 107 */     this.sslContext = paramSSLContext;
/* 108 */     this.sslStreams = paramSSLStreams;
/* 109 */     this.logger = paramHttpContextImpl.getLogger();
/*     */   }
/*     */ 
/*     */   SocketChannel getChannel() {
/* 113 */     return this.chan;
/*     */   }
/*     */ 
/*     */   synchronized void close() {
/* 117 */     if (this.closed) {
/* 118 */       return;
/*     */     }
/* 120 */     this.closed = true;
/* 121 */     if ((this.logger != null) && (this.chan != null)) {
/* 122 */       this.logger.finest("Closing connection: " + this.chan.toString());
/*     */     }
/*     */ 
/* 125 */     if (!this.chan.isOpen()) {
/* 126 */       ServerImpl.dprint("Channel already closed");
/* 127 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 131 */       if (this.raw != null)
/* 132 */         this.raw.close();
/*     */     }
/*     */     catch (IOException localIOException1) {
/* 135 */       ServerImpl.dprint(localIOException1);
/*     */     }
/*     */     try {
/* 138 */       if (this.rawout != null)
/* 139 */         this.rawout.close();
/*     */     }
/*     */     catch (IOException localIOException2) {
/* 142 */       ServerImpl.dprint(localIOException2);
/*     */     }
/*     */     try {
/* 145 */       if (this.sslStreams != null)
/* 146 */         this.sslStreams.close();
/*     */     }
/*     */     catch (IOException localIOException3) {
/* 149 */       ServerImpl.dprint(localIOException3);
/*     */     }
/*     */     try {
/* 152 */       this.chan.close();
/*     */     } catch (IOException localIOException4) {
/* 154 */       ServerImpl.dprint(localIOException4);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setRemaining(int paramInt)
/*     */   {
/* 162 */     this.remaining = paramInt;
/*     */   }
/*     */ 
/*     */   int getRemaining() {
/* 166 */     return this.remaining;
/*     */   }
/*     */ 
/*     */   SelectionKey getSelectionKey() {
/* 170 */     return this.selectionKey;
/*     */   }
/*     */ 
/*     */   InputStream getInputStream() {
/* 174 */     return this.i;
/*     */   }
/*     */ 
/*     */   OutputStream getRawOutputStream() {
/* 178 */     return this.rawout;
/*     */   }
/*     */ 
/*     */   String getProtocol() {
/* 182 */     return this.protocol;
/*     */   }
/*     */ 
/*     */   SSLEngine getSSLEngine() {
/* 186 */     return this.engine;
/*     */   }
/*     */ 
/*     */   SSLContext getSSLContext() {
/* 190 */     return this.sslContext;
/*     */   }
/*     */ 
/*     */   HttpContextImpl getHttpContext() {
/* 194 */     return this.context;
/*     */   }
/*     */ 
/*     */   public static enum State
/*     */   {
/*  64 */     IDLE, REQUEST, RESPONSE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.HttpConnection
 * JD-Core Version:    0.6.2
 */