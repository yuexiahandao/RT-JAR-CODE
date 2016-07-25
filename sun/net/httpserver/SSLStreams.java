/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import com.sun.net.httpserver.HttpsConfigurator;
/*     */ import com.sun.net.httpserver.HttpsParameters;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*     */ import javax.net.ssl.SSLEngineResult.Status;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSession;
/*     */ 
/*     */ class SSLStreams
/*     */ {
/*     */   SSLContext sslctx;
/*     */   SocketChannel chan;
/*     */   TimeSource time;
/*     */   ServerImpl server;
/*     */   SSLEngine engine;
/*     */   EngineWrapper wrapper;
/*     */   OutputStream os;
/*     */   InputStream is;
/*  58 */   Lock handshaking = new ReentrantLock();
/*     */   int app_buf_size;
/*     */   int packet_buf_size;
/*     */ 
/*     */   SSLStreams(ServerImpl paramServerImpl, SSLContext paramSSLContext, SocketChannel paramSocketChannel)
/*     */     throws IOException
/*     */   {
/*  61 */     this.server = paramServerImpl;
/*  62 */     this.time = paramServerImpl;
/*  63 */     this.sslctx = paramSSLContext;
/*  64 */     this.chan = paramSocketChannel;
/*  65 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketChannel.socket().getRemoteSocketAddress();
/*     */ 
/*  67 */     this.engine = paramSSLContext.createSSLEngine(localInetSocketAddress.getHostName(), localInetSocketAddress.getPort());
/*  68 */     this.engine.setUseClientMode(false);
/*  69 */     HttpsConfigurator localHttpsConfigurator = paramServerImpl.getHttpsConfigurator();
/*  70 */     configureEngine(localHttpsConfigurator, localInetSocketAddress);
/*  71 */     this.wrapper = new EngineWrapper(paramSocketChannel, this.engine);
/*     */   }
/*     */ 
/*     */   private void configureEngine(HttpsConfigurator paramHttpsConfigurator, InetSocketAddress paramInetSocketAddress) {
/*  75 */     if (paramHttpsConfigurator != null) {
/*  76 */       Parameters localParameters = new Parameters(paramHttpsConfigurator, paramInetSocketAddress);
/*     */ 
/*  78 */       paramHttpsConfigurator.configure(localParameters);
/*  79 */       SSLParameters localSSLParameters = localParameters.getSSLParameters();
/*  80 */       if (localSSLParameters != null) {
/*  81 */         this.engine.setSSLParameters(localSSLParameters);
/*     */       }
/*     */       else
/*     */       {
/*  86 */         if (localParameters.getCipherSuites() != null)
/*     */           try {
/*  88 */             this.engine.setEnabledCipherSuites(localParameters.getCipherSuites());
/*     */           }
/*     */           catch (IllegalArgumentException localIllegalArgumentException1)
/*     */           {
/*     */           }
/*  93 */         this.engine.setNeedClientAuth(localParameters.getNeedClientAuth());
/*  94 */         this.engine.setWantClientAuth(localParameters.getWantClientAuth());
/*  95 */         if (localParameters.getProtocols() != null)
/*     */           try {
/*  97 */             this.engine.setEnabledProtocols(localParameters.getProtocols());
/*     */           }
/*     */           catch (IllegalArgumentException localIllegalArgumentException2)
/*     */           {
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void close()
/*     */     throws IOException
/*     */   {
/* 135 */     this.wrapper.close();
/*     */   }
/*     */ 
/*     */   InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 142 */     if (this.is == null) {
/* 143 */       this.is = new InputStream();
/*     */     }
/* 145 */     return this.is;
/*     */   }
/*     */ 
/*     */   OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 152 */     if (this.os == null) {
/* 153 */       this.os = new OutputStream();
/*     */     }
/* 155 */     return this.os;
/*     */   }
/*     */ 
/*     */   SSLEngine getSSLEngine() {
/* 159 */     return this.engine;
/*     */   }
/*     */ 
/*     */   void beginHandshake()
/*     */     throws SSLException
/*     */   {
/* 168 */     this.engine.beginHandshake();
/*     */   }
/*     */ 
/*     */   private ByteBuffer allocate(BufType paramBufType)
/*     */   {
/* 188 */     return allocate(paramBufType, -1);
/*     */   }
/*     */ 
/*     */   private ByteBuffer allocate(BufType paramBufType, int paramInt) {
/* 192 */     assert (this.engine != null);
/* 193 */     synchronized (this)
/*     */     {
/*     */       SSLSession localSSLSession;
/*     */       int i;
/* 195 */       if (paramBufType == BufType.PACKET) {
/* 196 */         if (this.packet_buf_size == 0) {
/* 197 */           localSSLSession = this.engine.getSession();
/* 198 */           this.packet_buf_size = localSSLSession.getPacketBufferSize();
/*     */         }
/* 200 */         if (paramInt > this.packet_buf_size) {
/* 201 */           this.packet_buf_size = paramInt;
/*     */         }
/* 203 */         i = this.packet_buf_size;
/*     */       } else {
/* 205 */         if (this.app_buf_size == 0) {
/* 206 */           localSSLSession = this.engine.getSession();
/* 207 */           this.app_buf_size = localSSLSession.getApplicationBufferSize();
/*     */         }
/* 209 */         if (paramInt > this.app_buf_size) {
/* 210 */           this.app_buf_size = paramInt;
/*     */         }
/* 212 */         i = this.app_buf_size;
/*     */       }
/* 214 */       return ByteBuffer.allocate(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private ByteBuffer realloc(ByteBuffer paramByteBuffer, boolean paramBoolean, BufType paramBufType)
/*     */   {
/* 227 */     synchronized (this) {
/* 228 */       int i = 2 * paramByteBuffer.capacity();
/* 229 */       ByteBuffer localByteBuffer = allocate(paramBufType, i);
/* 230 */       if (paramBoolean) {
/* 231 */         paramByteBuffer.flip();
/*     */       }
/* 233 */       localByteBuffer.put(paramByteBuffer);
/* 234 */       paramByteBuffer = localByteBuffer;
/*     */     }
/* 236 */     return paramByteBuffer;
/*     */   }
/*     */ 
/*     */   public WrapperResult sendData(ByteBuffer paramByteBuffer)
/*     */     throws IOException
/*     */   {
/* 380 */     WrapperResult localWrapperResult = null;
/* 381 */     while (paramByteBuffer.remaining() > 0) {
/* 382 */       localWrapperResult = this.wrapper.wrapAndSend(paramByteBuffer);
/* 383 */       SSLEngineResult.Status localStatus = localWrapperResult.result.getStatus();
/* 384 */       if (localStatus == SSLEngineResult.Status.CLOSED) {
/* 385 */         doClosure();
/* 386 */         return localWrapperResult;
/*     */       }
/* 388 */       SSLEngineResult.HandshakeStatus localHandshakeStatus = localWrapperResult.result.getHandshakeStatus();
/* 389 */       if ((localHandshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED) && (localHandshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING))
/*     */       {
/* 392 */         doHandshake(localHandshakeStatus);
/*     */       }
/*     */     }
/* 395 */     return localWrapperResult;
/*     */   }
/*     */ 
/*     */   public WrapperResult recvData(ByteBuffer paramByteBuffer)
/*     */     throws IOException
/*     */   {
/* 406 */     WrapperResult localWrapperResult = null;
/* 407 */     assert (paramByteBuffer.position() == 0);
/* 408 */     while (paramByteBuffer.position() == 0) {
/* 409 */       localWrapperResult = this.wrapper.recvAndUnwrap(paramByteBuffer);
/* 410 */       paramByteBuffer = localWrapperResult.buf != paramByteBuffer ? localWrapperResult.buf : paramByteBuffer;
/* 411 */       SSLEngineResult.Status localStatus = localWrapperResult.result.getStatus();
/* 412 */       if (localStatus == SSLEngineResult.Status.CLOSED) {
/* 413 */         doClosure();
/* 414 */         return localWrapperResult;
/*     */       }
/*     */ 
/* 417 */       SSLEngineResult.HandshakeStatus localHandshakeStatus = localWrapperResult.result.getHandshakeStatus();
/* 418 */       if ((localHandshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED) && (localHandshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING))
/*     */       {
/* 421 */         doHandshake(localHandshakeStatus);
/*     */       }
/*     */     }
/* 424 */     paramByteBuffer.flip();
/* 425 */     return localWrapperResult;
/*     */   }
/*     */ 
/*     */   void doClosure() throws IOException
/*     */   {
/*     */     try {
/* 433 */       this.handshaking.lock();
/* 434 */       ByteBuffer localByteBuffer = allocate(BufType.APPLICATION);
/*     */       WrapperResult localWrapperResult;
/*     */       do {
/* 437 */         localByteBuffer.clear();
/* 438 */         localByteBuffer.flip();
/* 439 */         localWrapperResult = this.wrapper.wrapAndSendX(localByteBuffer, true);
/* 440 */       }while (localWrapperResult.result.getStatus() != SSLEngineResult.Status.CLOSED);
/*     */     } finally {
/* 442 */       this.handshaking.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   void doHandshake(SSLEngineResult.HandshakeStatus paramHandshakeStatus)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 453 */       this.handshaking.lock();
/* 454 */       ByteBuffer localByteBuffer = allocate(BufType.APPLICATION);
/* 455 */       while ((paramHandshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED) && (paramHandshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING))
/*     */       {
/* 458 */         WrapperResult localWrapperResult = null;
/* 459 */         switch (1.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[paramHandshakeStatus.ordinal()])
/*     */         {
/*     */         case 1:
/*     */           Runnable localRunnable;
/* 462 */           while ((localRunnable = this.engine.getDelegatedTask()) != null)
/*     */           {
/* 466 */             localRunnable.run();
/*     */           }
/*     */ 
/*     */         case 2:
/* 470 */           localByteBuffer.clear();
/* 471 */           localByteBuffer.flip();
/* 472 */           localWrapperResult = this.wrapper.wrapAndSend(localByteBuffer);
/* 473 */           break;
/*     */         case 3:
/* 476 */           localByteBuffer.clear();
/* 477 */           localWrapperResult = this.wrapper.recvAndUnwrap(localByteBuffer);
/* 478 */           if (localWrapperResult.buf != localByteBuffer) {
/* 479 */             localByteBuffer = localWrapperResult.buf;
/*     */           }
/* 481 */           assert (localByteBuffer.position() == 0);
/*     */         }
/*     */ 
/* 484 */         paramHandshakeStatus = localWrapperResult.result.getHandshakeStatus();
/*     */       }
/*     */     } finally {
/* 487 */       this.handshaking.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   static enum BufType
/*     */   {
/* 184 */     PACKET, APPLICATION;
/*     */   }
/*     */ 
/*     */   class EngineWrapper
/*     */   {
/*     */     SocketChannel chan;
/*     */     SSLEngine engine;
/*     */     Object wrapLock;
/*     */     Object unwrapLock;
/*     */     ByteBuffer unwrap_src;
/*     */     ByteBuffer wrap_dst;
/* 253 */     boolean closed = false;
/*     */     int u_remaining;
/*     */ 
/*     */     EngineWrapper(SocketChannel paramSSLEngine, SSLEngine arg3)
/*     */       throws IOException
/*     */     {
/* 257 */       this.chan = paramSSLEngine;
/*     */       Object localObject;
/* 258 */       this.engine = localObject;
/* 259 */       this.wrapLock = new Object();
/* 260 */       this.unwrapLock = new Object();
/* 261 */       this.unwrap_src = SSLStreams.this.allocate(SSLStreams.BufType.PACKET);
/* 262 */       this.wrap_dst = SSLStreams.this.allocate(SSLStreams.BufType.PACKET);
/*     */     }
/*     */ 
/*     */     void close()
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     SSLStreams.WrapperResult wrapAndSend(ByteBuffer paramByteBuffer)
/*     */       throws IOException
/*     */     {
/* 274 */       return wrapAndSendX(paramByteBuffer, false);
/*     */     }
/*     */ 
/*     */     SSLStreams.WrapperResult wrapAndSendX(ByteBuffer paramByteBuffer, boolean paramBoolean) throws IOException {
/* 278 */       if ((this.closed) && (!paramBoolean)) {
/* 279 */         throw new IOException("Engine is closed");
/*     */       }
/*     */ 
/* 282 */       SSLStreams.WrapperResult localWrapperResult = new SSLStreams.WrapperResult(SSLStreams.this);
/* 283 */       synchronized (this.wrapLock) { this.wrap_dst.clear();
/*     */         SSLEngineResult.Status localStatus;
/*     */         do { localWrapperResult.result = this.engine.wrap(paramByteBuffer, this.wrap_dst);
/* 287 */           localStatus = localWrapperResult.result.getStatus();
/* 288 */           if (localStatus == SSLEngineResult.Status.BUFFER_OVERFLOW)
/* 289 */             this.wrap_dst = SSLStreams.this.realloc(this.wrap_dst, true, SSLStreams.BufType.PACKET);
/*     */         }
/* 291 */         while (localStatus == SSLEngineResult.Status.BUFFER_OVERFLOW);
/* 292 */         if ((localStatus == SSLEngineResult.Status.CLOSED) && (!paramBoolean)) {
/* 293 */           this.closed = true;
/* 294 */           return localWrapperResult;
/*     */         }
/* 296 */         if (localWrapperResult.result.bytesProduced() > 0) {
/* 297 */           this.wrap_dst.flip();
/* 298 */           int i = this.wrap_dst.remaining();
/* 299 */           assert (i == localWrapperResult.result.bytesProduced());
/* 300 */           while (i > 0) {
/* 301 */             i -= this.chan.write(this.wrap_dst);
/*     */           }
/*     */         }
/*     */       }
/* 305 */       return localWrapperResult;
/*     */     }
/*     */ 
/*     */     SSLStreams.WrapperResult recvAndUnwrap(ByteBuffer paramByteBuffer)
/*     */       throws IOException
/*     */     {
/* 314 */       SSLEngineResult.Status localStatus = SSLEngineResult.Status.OK;
/* 315 */       SSLStreams.WrapperResult localWrapperResult = new SSLStreams.WrapperResult(SSLStreams.this);
/* 316 */       localWrapperResult.buf = paramByteBuffer;
/* 317 */       if (this.closed)
/* 318 */         throw new IOException("Engine is closed");
/*     */       int i;
/* 321 */       if (this.u_remaining > 0) {
/* 322 */         this.unwrap_src.compact();
/* 323 */         this.unwrap_src.flip();
/* 324 */         i = 0;
/*     */       } else {
/* 326 */         this.unwrap_src.clear();
/* 327 */         i = 1;
/*     */       }
/* 329 */       synchronized (this.unwrapLock)
/*     */       {
/*     */         do {
/* 332 */           if (i != 0) {
/*     */             int j;
/*     */             do j = this.chan.read(this.unwrap_src);
/* 335 */             while (j == 0);
/* 336 */             if (j == -1) {
/* 337 */               throw new IOException("connection closed for reading");
/*     */             }
/* 339 */             this.unwrap_src.flip();
/*     */           }
/* 341 */           localWrapperResult.result = this.engine.unwrap(this.unwrap_src, localWrapperResult.buf);
/* 342 */           localStatus = localWrapperResult.result.getStatus();
/* 343 */           if (localStatus == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
/* 344 */             if (this.unwrap_src.limit() == this.unwrap_src.capacity())
/*     */             {
/* 346 */               this.unwrap_src = SSLStreams.this.realloc(this.unwrap_src, false, SSLStreams.BufType.PACKET);
/*     */             }
/*     */             else
/*     */             {
/* 354 */               this.unwrap_src.position(this.unwrap_src.limit());
/* 355 */               this.unwrap_src.limit(this.unwrap_src.capacity());
/*     */             }
/* 357 */             i = 1;
/* 358 */           } else if (localStatus == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/* 359 */             localWrapperResult.buf = SSLStreams.this.realloc(localWrapperResult.buf, true, SSLStreams.BufType.APPLICATION);
/* 360 */             i = 0;
/* 361 */           } else if (localStatus == SSLEngineResult.Status.CLOSED) {
/* 362 */             this.closed = true;
/* 363 */             localWrapperResult.buf.flip();
/* 364 */             return localWrapperResult;
/*     */           }
/*     */         }
/* 366 */         while (localStatus != SSLEngineResult.Status.OK);
/*     */       }
/* 368 */       this.u_remaining = this.unwrap_src.remaining();
/* 369 */       return localWrapperResult;
/*     */     }
/*     */   }
/*     */ 
/*     */   class InputStream extends InputStream
/*     */   {
/*     */     ByteBuffer bbuf;
/* 499 */     boolean closed = false;
/*     */ 
/* 502 */     boolean eof = false;
/*     */ 
/* 504 */     boolean needData = true;
/*     */ 
/* 590 */     byte[] single = new byte[1];
/*     */ 
/*     */     InputStream()
/*     */     {
/* 507 */       this.bbuf = SSLStreams.this.allocate(SSLStreams.BufType.APPLICATION);
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 511 */       if (this.closed) {
/* 512 */         throw new IOException("SSL stream is closed");
/*     */       }
/* 514 */       if (this.eof) {
/* 515 */         return 0;
/*     */       }
/* 517 */       int i = 0;
/* 518 */       if (!this.needData) {
/* 519 */         i = this.bbuf.remaining();
/* 520 */         this.needData = (i == 0);
/*     */       }
/* 522 */       if (this.needData) {
/* 523 */         this.bbuf.clear();
/* 524 */         SSLStreams.WrapperResult localWrapperResult = SSLStreams.this.recvData(this.bbuf);
/* 525 */         this.bbuf = (localWrapperResult.buf == this.bbuf ? this.bbuf : localWrapperResult.buf);
/* 526 */         if ((i = this.bbuf.remaining()) == 0) {
/* 527 */           this.eof = true;
/* 528 */           return 0;
/*     */         }
/* 530 */         this.needData = false;
/*     */       }
/*     */ 
/* 534 */       if (paramInt2 > i) {
/* 535 */         paramInt2 = i;
/*     */       }
/* 537 */       this.bbuf.get(paramArrayOfByte, paramInt1, paramInt2);
/* 538 */       return paramInt2;
/*     */     }
/*     */ 
/*     */     public int available() throws IOException {
/* 542 */       return this.bbuf.remaining();
/*     */     }
/*     */ 
/*     */     public boolean markSupported() {
/* 546 */       return false;
/*     */     }
/*     */ 
/*     */     public void reset() throws IOException {
/* 550 */       throw new IOException("mark/reset not supported");
/*     */     }
/*     */ 
/*     */     public long skip(long paramLong) throws IOException {
/* 554 */       int i = (int)paramLong;
/* 555 */       if (this.closed) {
/* 556 */         throw new IOException("SSL stream is closed");
/*     */       }
/* 558 */       if (this.eof) {
/* 559 */         return 0L;
/*     */       }
/* 561 */       int j = i;
/* 562 */       while (i > 0) {
/* 563 */         if (this.bbuf.remaining() >= i) {
/* 564 */           this.bbuf.position(this.bbuf.position() + i);
/* 565 */           return j;
/*     */         }
/* 567 */         i -= this.bbuf.remaining();
/* 568 */         this.bbuf.clear();
/* 569 */         SSLStreams.WrapperResult localWrapperResult = SSLStreams.this.recvData(this.bbuf);
/* 570 */         this.bbuf = (localWrapperResult.buf == this.bbuf ? this.bbuf : localWrapperResult.buf);
/*     */       }
/*     */ 
/* 573 */       return j;
/*     */     }
/*     */ 
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 582 */       this.eof = true;
/* 583 */       SSLStreams.this.engine.closeInbound();
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte) throws IOException {
/* 587 */       return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public int read()
/*     */       throws IOException
/*     */     {
/* 593 */       int i = read(this.single, 0, 1);
/* 594 */       if (i == 0) {
/* 595 */         return -1;
/*     */       }
/* 597 */       return this.single[0] & 0xFF;
/*     */     }
/*     */   }
/*     */ 
/*     */   class OutputStream extends OutputStream
/*     */   {
/*     */     ByteBuffer buf;
/* 609 */     boolean closed = false;
/* 610 */     byte[] single = new byte[1];
/*     */ 
/*     */     OutputStream() {
/* 613 */       this.buf = SSLStreams.this.allocate(SSLStreams.BufType.APPLICATION);
/*     */     }
/*     */ 
/*     */     public void write(int paramInt) throws IOException {
/* 617 */       this.single[0] = ((byte)paramInt);
/* 618 */       write(this.single, 0, 1);
/*     */     }
/*     */ 
/*     */     public void write(byte[] paramArrayOfByte) throws IOException {
/* 622 */       write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 625 */       if (this.closed) {
/* 626 */         throw new IOException("output stream is closed");
/*     */       }
/* 628 */       while (paramInt2 > 0) {
/* 629 */         int i = paramInt2 > this.buf.capacity() ? this.buf.capacity() : paramInt2;
/* 630 */         this.buf.clear();
/* 631 */         this.buf.put(paramArrayOfByte, paramInt1, i);
/* 632 */         paramInt2 -= i;
/* 633 */         paramInt1 += i;
/* 634 */         this.buf.flip();
/* 635 */         SSLStreams.WrapperResult localWrapperResult = SSLStreams.this.sendData(this.buf);
/* 636 */         if (localWrapperResult.result.getStatus() == SSLEngineResult.Status.CLOSED) {
/* 637 */           this.closed = true;
/* 638 */           if (paramInt2 > 0)
/* 639 */             throw new IOException("output stream is closed");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void flush() throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 650 */       SSLStreams.WrapperResult localWrapperResult = null;
/* 651 */       SSLStreams.this.engine.closeOutbound();
/* 652 */       this.closed = true;
/* 653 */       SSLEngineResult.HandshakeStatus localHandshakeStatus = SSLEngineResult.HandshakeStatus.NEED_WRAP;
/* 654 */       this.buf.clear();
/* 655 */       while (localHandshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
/* 656 */         localWrapperResult = SSLStreams.this.wrapper.wrapAndSend(this.buf);
/* 657 */         localHandshakeStatus = localWrapperResult.result.getHandshakeStatus();
/*     */       }
/* 659 */       assert (localWrapperResult.result.getStatus() == SSLEngineResult.Status.CLOSED);
/*     */     }
/*     */   }
/*     */ 
/*     */   class Parameters extends HttpsParameters
/*     */   {
/*     */     InetSocketAddress addr;
/*     */     HttpsConfigurator cfg;
/*     */     SSLParameters params;
/*     */ 
/*     */     Parameters(HttpsConfigurator paramInetSocketAddress, InetSocketAddress arg3)
/*     */     {
/*     */       Object localObject;
/* 111 */       this.addr = localObject;
/* 112 */       this.cfg = paramInetSocketAddress;
/*     */     }
/*     */     public InetSocketAddress getClientAddress() {
/* 115 */       return this.addr;
/*     */     }
/*     */     public HttpsConfigurator getHttpsConfigurator() {
/* 118 */       return this.cfg;
/*     */     }
/*     */ 
/*     */     public void setSSLParameters(SSLParameters paramSSLParameters)
/*     */     {
/* 123 */       this.params = paramSSLParameters;
/*     */     }
/*     */     SSLParameters getSSLParameters() {
/* 126 */       return this.params;
/*     */     }
/*     */   }
/*     */ 
/*     */   class WrapperResult
/*     */   {
/*     */     SSLEngineResult result;
/*     */     ByteBuffer buf;
/*     */ 
/*     */     WrapperResult()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.SSLStreams
 * JD-Core Version:    0.6.2
 */