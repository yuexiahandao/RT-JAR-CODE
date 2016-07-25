/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketImpl;
/*     */ import java.net.SocketOption;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.IllegalBlockingModeException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Set;
/*     */ import sun.misc.IoTrace;
/*     */ 
/*     */ public class SocketAdaptor extends Socket
/*     */ {
/*     */   private final SocketChannelImpl sc;
/*  60 */   private volatile int timeout = 0;
/*     */ 
/* 246 */   private InputStream socketInputStream = null;
/*     */ 
/*     */   private SocketAdaptor(SocketChannelImpl paramSocketChannelImpl)
/*     */     throws SocketException
/*     */   {
/*  63 */     super((SocketImpl)null);
/*  64 */     this.sc = paramSocketChannelImpl;
/*     */   }
/*     */ 
/*     */   public static Socket create(SocketChannelImpl paramSocketChannelImpl) {
/*     */     try {
/*  69 */       return new SocketAdaptor(paramSocketChannelImpl); } catch (SocketException localSocketException) {
/*     */     }
/*  71 */     throw new InternalError("Should not reach here");
/*     */   }
/*     */ 
/*     */   public SocketChannel getChannel()
/*     */   {
/*  76 */     return this.sc;
/*     */   }
/*     */ 
/*     */   public void connect(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/*  82 */     connect(paramSocketAddress, 0);
/*     */   }
/*     */ 
/*     */   public void connect(SocketAddress paramSocketAddress, int paramInt) throws IOException {
/*  86 */     if (paramSocketAddress == null)
/*  87 */       throw new IllegalArgumentException("connect: The address can't be null");
/*  88 */     if (paramInt < 0) {
/*  89 */       throw new IllegalArgumentException("connect: timeout can't be negative");
/*     */     }
/*  91 */     synchronized (this.sc.blockingLock()) {
/*  92 */       if (!this.sc.isBlocking()) {
/*  93 */         throw new IllegalBlockingModeException();
/*     */       }
/*     */       try
/*     */       {
/*  97 */         if (paramInt == 0) {
/*  98 */           this.sc.connect(paramSocketAddress);
/*  99 */           return;
/*     */         }
/*     */ 
/* 103 */         SelectionKey localSelectionKey = null;
/* 104 */         Selector localSelector = null;
/* 105 */         this.sc.configureBlocking(false);
/*     */         try {
/* 107 */           if (this.sc.connect(paramSocketAddress))
/*     */           {
/* 130 */             if (localSelectionKey != null)
/* 131 */               localSelectionKey.cancel();
/* 132 */             if (this.sc.isOpen())
/* 133 */               this.sc.configureBlocking(true);
/* 134 */             if (localSelector != null)
/* 135 */               Util.releaseTemporarySelector(localSelector);
/*     */           }
/*     */           else
/*     */           {
/* 109 */             localSelector = Util.getTemporarySelector(this.sc);
/* 110 */             localSelectionKey = this.sc.register(localSelector, 8);
/* 111 */             long l1 = paramInt;
/*     */             while (true) {
/* 113 */               if (!this.sc.isOpen())
/* 114 */                 throw new ClosedChannelException();
/* 115 */               long l2 = System.currentTimeMillis();
/* 116 */               int i = localSelector.select(l1);
/* 117 */               if ((i > 0) && (localSelectionKey.isConnectable()) && (this.sc.finishConnect())) {
/*     */                 break;
/*     */               }
/* 120 */               localSelector.selectedKeys().remove(localSelectionKey);
/* 121 */               l1 -= System.currentTimeMillis() - l2;
/* 122 */               if (l1 <= 0L) {
/*     */                 try {
/* 124 */                   this.sc.close(); } catch (IOException localIOException) {
/*     */                 }
/* 126 */                 throw new SocketTimeoutException();
/*     */               }
/*     */             }
/*     */           }
/*     */         } finally { if (localSelectionKey != null)
/* 131 */             localSelectionKey.cancel();
/* 132 */           if (this.sc.isOpen())
/* 133 */             this.sc.configureBlocking(true);
/* 134 */           if (localSelector != null)
/* 135 */             Util.releaseTemporarySelector(localSelector); }
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 139 */         Net.translateException(localException, true);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(SocketAddress paramSocketAddress) throws IOException
/*     */   {
/*     */     try {
/* 147 */       this.sc.bind(paramSocketAddress);
/*     */     } catch (Exception localException) {
/* 149 */       Net.translateException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public InetAddress getInetAddress() {
/* 154 */     SocketAddress localSocketAddress = this.sc.remoteAddress();
/* 155 */     if (localSocketAddress == null) {
/* 156 */       return null;
/*     */     }
/* 158 */     return ((InetSocketAddress)localSocketAddress).getAddress();
/*     */   }
/*     */ 
/*     */   public InetAddress getLocalAddress()
/*     */   {
/* 163 */     if (this.sc.isOpen()) {
/* 164 */       InetSocketAddress localInetSocketAddress = this.sc.localAddress();
/* 165 */       if (localInetSocketAddress != null)
/* 166 */         return Net.getRevealedLocalAddress(localInetSocketAddress).getAddress();
/*     */     }
/* 168 */     return new InetSocketAddress(0).getAddress();
/*     */   }
/*     */ 
/*     */   public int getPort() {
/* 172 */     SocketAddress localSocketAddress = this.sc.remoteAddress();
/* 173 */     if (localSocketAddress == null) {
/* 174 */       return 0;
/*     */     }
/* 176 */     return ((InetSocketAddress)localSocketAddress).getPort();
/*     */   }
/*     */ 
/*     */   public int getLocalPort()
/*     */   {
/* 181 */     InetSocketAddress localInetSocketAddress = this.sc.localAddress();
/* 182 */     if (localInetSocketAddress == null) {
/* 183 */       return -1;
/*     */     }
/* 185 */     return ((InetSocketAddress)localInetSocketAddress).getPort();
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 249 */     if (!this.sc.isOpen())
/* 250 */       throw new SocketException("Socket is closed");
/* 251 */     if (!this.sc.isConnected())
/* 252 */       throw new SocketException("Socket is not connected");
/* 253 */     if (!this.sc.isInputOpen())
/* 254 */       throw new SocketException("Socket input is shutdown");
/* 255 */     if (this.socketInputStream == null) {
/*     */       try {
/* 257 */         this.socketInputStream = ((InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public InputStream run() throws IOException {
/* 260 */             return new SocketAdaptor.SocketInputStream(SocketAdaptor.this, null);
/*     */           } } ));
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException) {
/* 264 */         throw ((IOException)localPrivilegedActionException.getException());
/*     */       }
/*     */     }
/* 267 */     return this.socketInputStream;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream() throws IOException {
/* 271 */     if (!this.sc.isOpen())
/* 272 */       throw new SocketException("Socket is closed");
/* 273 */     if (!this.sc.isConnected())
/* 274 */       throw new SocketException("Socket is not connected");
/* 275 */     if (!this.sc.isOutputOpen())
/* 276 */       throw new SocketException("Socket output is shutdown");
/* 277 */     OutputStream localOutputStream = null;
/*     */     try {
/* 279 */       localOutputStream = (OutputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public OutputStream run() throws IOException {
/* 282 */           return Channels.newOutputStream(SocketAdaptor.this.sc);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 286 */       throw ((IOException)localPrivilegedActionException.getException());
/*     */     }
/* 288 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   private void setBooleanOption(SocketOption<Boolean> paramSocketOption, boolean paramBoolean) throws SocketException
/*     */   {
/*     */     try
/*     */     {
/* 295 */       this.sc.setOption(paramSocketOption, Boolean.valueOf(paramBoolean));
/*     */     } catch (IOException localIOException) {
/* 297 */       Net.translateToSocketException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setIntOption(SocketOption<Integer> paramSocketOption, int paramInt) throws SocketException
/*     */   {
/*     */     try
/*     */     {
/* 305 */       this.sc.setOption(paramSocketOption, Integer.valueOf(paramInt));
/*     */     } catch (IOException localIOException) {
/* 307 */       Net.translateToSocketException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean getBooleanOption(SocketOption<Boolean> paramSocketOption) throws SocketException {
/*     */     try {
/* 313 */       return ((Boolean)this.sc.getOption(paramSocketOption)).booleanValue();
/*     */     } catch (IOException localIOException) {
/* 315 */       Net.translateToSocketException(localIOException);
/* 316 */     }return false;
/*     */   }
/*     */ 
/*     */   private int getIntOption(SocketOption<Integer> paramSocketOption) throws SocketException
/*     */   {
/*     */     try {
/* 322 */       return ((Integer)this.sc.getOption(paramSocketOption)).intValue();
/*     */     } catch (IOException localIOException) {
/* 324 */       Net.translateToSocketException(localIOException);
/* 325 */     }return -1;
/*     */   }
/*     */ 
/*     */   public void setTcpNoDelay(boolean paramBoolean) throws SocketException
/*     */   {
/* 330 */     setBooleanOption(StandardSocketOptions.TCP_NODELAY, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getTcpNoDelay() throws SocketException {
/* 334 */     return getBooleanOption(StandardSocketOptions.TCP_NODELAY);
/*     */   }
/*     */ 
/*     */   public void setSoLinger(boolean paramBoolean, int paramInt) throws SocketException {
/* 338 */     if (!paramBoolean)
/* 339 */       paramInt = -1;
/* 340 */     setIntOption(StandardSocketOptions.SO_LINGER, paramInt);
/*     */   }
/*     */ 
/*     */   public int getSoLinger() throws SocketException {
/* 344 */     return getIntOption(StandardSocketOptions.SO_LINGER);
/*     */   }
/*     */ 
/*     */   public void sendUrgentData(int paramInt) throws IOException {
/* 348 */     synchronized (this.sc.blockingLock()) {
/* 349 */       if (!this.sc.isBlocking())
/* 350 */         throw new IllegalBlockingModeException();
/* 351 */       int i = this.sc.sendOutOfBandData((byte)paramInt);
/* 352 */       if ((!$assertionsDisabled) && (i != 1)) throw new AssertionError(); 
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setOOBInline(boolean paramBoolean) throws SocketException {
/* 357 */     setBooleanOption(ExtendedSocketOption.SO_OOBINLINE, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getOOBInline() throws SocketException {
/* 361 */     return getBooleanOption(ExtendedSocketOption.SO_OOBINLINE);
/*     */   }
/*     */ 
/*     */   public void setSoTimeout(int paramInt) throws SocketException {
/* 365 */     if (paramInt < 0)
/* 366 */       throw new IllegalArgumentException("timeout can't be negative");
/* 367 */     this.timeout = paramInt;
/*     */   }
/*     */ 
/*     */   public int getSoTimeout() throws SocketException {
/* 371 */     return this.timeout;
/*     */   }
/*     */ 
/*     */   public void setSendBufferSize(int paramInt) throws SocketException
/*     */   {
/* 376 */     if (paramInt <= 0)
/* 377 */       throw new IllegalArgumentException("Invalid send size");
/* 378 */     setIntOption(StandardSocketOptions.SO_SNDBUF, paramInt);
/*     */   }
/*     */ 
/*     */   public int getSendBufferSize() throws SocketException {
/* 382 */     return getIntOption(StandardSocketOptions.SO_SNDBUF);
/*     */   }
/*     */ 
/*     */   public void setReceiveBufferSize(int paramInt) throws SocketException
/*     */   {
/* 387 */     if (paramInt <= 0)
/* 388 */       throw new IllegalArgumentException("Invalid receive size");
/* 389 */     setIntOption(StandardSocketOptions.SO_RCVBUF, paramInt);
/*     */   }
/*     */ 
/*     */   public int getReceiveBufferSize() throws SocketException {
/* 393 */     return getIntOption(StandardSocketOptions.SO_RCVBUF);
/*     */   }
/*     */ 
/*     */   public void setKeepAlive(boolean paramBoolean) throws SocketException {
/* 397 */     setBooleanOption(StandardSocketOptions.SO_KEEPALIVE, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getKeepAlive() throws SocketException {
/* 401 */     return getBooleanOption(StandardSocketOptions.SO_KEEPALIVE);
/*     */   }
/*     */ 
/*     */   public void setTrafficClass(int paramInt) throws SocketException {
/* 405 */     setIntOption(StandardSocketOptions.IP_TOS, paramInt);
/*     */   }
/*     */ 
/*     */   public int getTrafficClass() throws SocketException {
/* 409 */     return getIntOption(StandardSocketOptions.IP_TOS);
/*     */   }
/*     */ 
/*     */   public void setReuseAddress(boolean paramBoolean) throws SocketException {
/* 413 */     setBooleanOption(StandardSocketOptions.SO_REUSEADDR, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getReuseAddress() throws SocketException {
/* 417 */     return getBooleanOption(StandardSocketOptions.SO_REUSEADDR);
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 421 */     this.sc.close();
/*     */   }
/*     */ 
/*     */   public void shutdownInput() throws IOException {
/*     */     try {
/* 426 */       this.sc.shutdownInput();
/*     */     } catch (Exception localException) {
/* 428 */       Net.translateException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void shutdownOutput() throws IOException {
/*     */     try {
/* 434 */       this.sc.shutdownOutput();
/*     */     } catch (Exception localException) {
/* 436 */       Net.translateException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 441 */     if (this.sc.isConnected()) {
/* 442 */       return "Socket[addr=" + getInetAddress() + ",port=" + getPort() + ",localport=" + getLocalPort() + "]";
/*     */     }
/*     */ 
/* 445 */     return "Socket[unconnected]";
/*     */   }
/*     */ 
/*     */   public boolean isConnected() {
/* 449 */     return this.sc.isConnected();
/*     */   }
/*     */ 
/*     */   public boolean isBound() {
/* 453 */     return this.sc.localAddress() != null;
/*     */   }
/*     */ 
/*     */   public boolean isClosed() {
/* 457 */     return !this.sc.isOpen();
/*     */   }
/*     */ 
/*     */   public boolean isInputShutdown() {
/* 461 */     return !this.sc.isInputOpen();
/*     */   }
/*     */ 
/*     */   public boolean isOutputShutdown() {
/* 465 */     return !this.sc.isOutputOpen();
/*     */   }
/*     */ 
/*     */   private class SocketInputStream extends ChannelInputStream
/*     */   {
/*     */     private SocketInputStream()
/*     */     {
/* 193 */       super();
/*     */     }
/*     */ 
/*     */     protected int read(ByteBuffer paramByteBuffer)
/*     */       throws IOException
/*     */     {
/* 199 */       synchronized (SocketAdaptor.this.sc.blockingLock()) {
/* 200 */         if (!SocketAdaptor.this.sc.isBlocking())
/* 201 */           throw new IllegalBlockingModeException();
/* 202 */         if (SocketAdaptor.this.timeout == 0) {
/* 203 */           return SocketAdaptor.this.sc.read(paramByteBuffer);
/*     */         }
/*     */ 
/* 206 */         SelectionKey localSelectionKey = null;
/* 207 */         Selector localSelector = null;
/* 208 */         SocketAdaptor.this.sc.configureBlocking(false);
/* 209 */         int i = 0;
/* 210 */         Object localObject1 = IoTrace.socketReadBegin();
/*     */         try {
/* 212 */           if ((i = SocketAdaptor.this.sc.read(paramByteBuffer)) != 0) {
/* 213 */             int j = i;
/*     */ 
/* 232 */             IoTrace.socketReadEnd(localObject1, SocketAdaptor.this.getInetAddress(), SocketAdaptor.this.getPort(), SocketAdaptor.this.timeout, i > 0 ? i : 0L);
/*     */ 
/* 234 */             if (localSelectionKey != null)
/* 235 */               localSelectionKey.cancel();
/* 236 */             if (SocketAdaptor.this.sc.isOpen())
/* 237 */               SocketAdaptor.this.sc.configureBlocking(true);
/* 238 */             if (localSelector != null)
/* 239 */               Util.releaseTemporarySelector(localSelector); return j;
/*     */           }
/* 214 */           localSelector = Util.getTemporarySelector(SocketAdaptor.this.sc);
/* 215 */           localSelectionKey = SocketAdaptor.this.sc.register(localSelector, 1);
/* 216 */           long l1 = SocketAdaptor.this.timeout;
/*     */           while (true) {
/* 218 */             if (!SocketAdaptor.this.sc.isOpen())
/* 219 */               throw new ClosedChannelException();
/* 220 */             long l2 = System.currentTimeMillis();
/* 221 */             int k = localSelector.select(l1);
/* 222 */             if ((k > 0) && (localSelectionKey.isReadable()) && 
/* 223 */               ((i = SocketAdaptor.this.sc.read(paramByteBuffer)) != 0)) {
/* 224 */               int m = i;
/*     */ 
/* 232 */               IoTrace.socketReadEnd(localObject1, SocketAdaptor.this.getInetAddress(), SocketAdaptor.this.getPort(), SocketAdaptor.this.timeout, i > 0 ? i : 0L);
/*     */ 
/* 234 */               if (localSelectionKey != null)
/* 235 */                 localSelectionKey.cancel();
/* 236 */               if (SocketAdaptor.this.sc.isOpen())
/* 237 */                 SocketAdaptor.this.sc.configureBlocking(true);
/* 238 */               if (localSelector != null)
/* 239 */                 Util.releaseTemporarySelector(localSelector); return m;
/*     */             }
/* 226 */             localSelector.selectedKeys().remove(localSelectionKey);
/* 227 */             l1 -= System.currentTimeMillis() - l2;
/* 228 */             if (l1 <= 0L)
/* 229 */               throw new SocketTimeoutException();
/*     */           }
/*     */         } finally {
/* 232 */           IoTrace.socketReadEnd(localObject1, SocketAdaptor.this.getInetAddress(), SocketAdaptor.this.getPort(), SocketAdaptor.this.timeout, i > 0 ? i : 0L);
/*     */ 
/* 234 */           if (localSelectionKey != null)
/* 235 */             localSelectionKey.cancel();
/* 236 */           if (SocketAdaptor.this.sc.isOpen())
/* 237 */             SocketAdaptor.this.sc.configureBlocking(true);
/* 238 */           if (localSelector != null)
/* 239 */             Util.releaseTemporarySelector(localSelector);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SocketAdaptor
 * JD-Core Version:    0.6.2
 */