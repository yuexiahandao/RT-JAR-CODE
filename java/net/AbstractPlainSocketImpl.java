/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import sun.net.ConnectionResetException;
/*     */ import sun.net.NetHooks;
/*     */ import sun.net.ResourceManager;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ abstract class AbstractPlainSocketImpl extends SocketImpl
/*     */ {
/*     */   int timeout;
/*     */   private int trafficClass;
/*  51 */   private boolean shut_rd = false;
/*  52 */   private boolean shut_wr = false;
/*     */ 
/*  54 */   private SocketInputStream socketInputStream = null;
/*     */ 
/*  57 */   protected int fdUseCount = 0;
/*     */ 
/*  60 */   protected final Object fdLock = new Object();
/*     */ 
/*  63 */   protected boolean closePending = false;
/*     */ 
/*  66 */   private int CONNECTION_NOT_RESET = 0;
/*  67 */   private int CONNECTION_RESET_PENDING = 1;
/*  68 */   private int CONNECTION_RESET = 2;
/*     */   private int resetState;
/*  70 */   private final Object resetLock = new Object();
/*     */   protected boolean stream;
/*     */   public static final int SHUT_RD = 0;
/*     */   public static final int SHUT_WR = 1;
/*     */ 
/*     */   protected synchronized void create(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  89 */     this.stream = paramBoolean;
/*  90 */     if (!paramBoolean) {
/*  91 */       ResourceManager.beforeUdpCreate();
/*     */ 
/*  93 */       this.fd = new FileDescriptor();
/*     */       try {
/*  95 */         socketCreate(false);
/*     */       } catch (IOException localIOException) {
/*  97 */         ResourceManager.afterUdpClose();
/*  98 */         this.fd = null;
/*  99 */         throw localIOException;
/*     */       }
/*     */     } else {
/* 102 */       this.fd = new FileDescriptor();
/* 103 */       socketCreate(true);
/*     */     }
/* 105 */     if (this.socket != null)
/* 106 */       this.socket.setCreated();
/* 107 */     if (this.serverSocket != null)
/* 108 */       this.serverSocket.setCreated();
/*     */   }
/*     */ 
/*     */   protected void connect(String paramString, int paramInt)
/*     */     throws UnknownHostException, IOException
/*     */   {
/* 120 */     int i = 0;
/*     */     try {
/* 122 */       InetAddress localInetAddress = InetAddress.getByName(paramString);
/* 123 */       this.port = paramInt;
/* 124 */       this.address = localInetAddress;
/*     */ 
/* 126 */       connectToAddress(localInetAddress, paramInt, this.timeout);
/* 127 */       i = 1;
/*     */     } finally {
/* 129 */       if (i == 0)
/*     */         try {
/* 131 */           close();
/*     */         }
/*     */         catch (IOException localIOException2)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void connect(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 147 */     this.port = paramInt;
/* 148 */     this.address = paramInetAddress;
/*     */     try
/*     */     {
/* 151 */       connectToAddress(paramInetAddress, paramInt, this.timeout);
/* 152 */       return;
/*     */     }
/*     */     catch (IOException localIOException) {
/* 155 */       close();
/* 156 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void connect(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 172 */     int i = 0;
/*     */     try {
/* 174 */       if ((paramSocketAddress == null) || (!(paramSocketAddress instanceof InetSocketAddress)))
/* 175 */         throw new IllegalArgumentException("unsupported address type");
/* 176 */       InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/* 177 */       if (localInetSocketAddress.isUnresolved())
/* 178 */         throw new UnknownHostException(localInetSocketAddress.getHostName());
/* 179 */       this.port = localInetSocketAddress.getPort();
/* 180 */       this.address = localInetSocketAddress.getAddress();
/*     */ 
/* 182 */       connectToAddress(this.address, this.port, paramInt);
/* 183 */       i = 1;
/*     */     } finally {
/* 185 */       if (i == 0)
/*     */         try {
/* 187 */           close();
/*     */         }
/*     */         catch (IOException localIOException2)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void connectToAddress(InetAddress paramInetAddress, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 197 */     if (paramInetAddress.isAnyLocalAddress())
/* 198 */       doConnect(InetAddress.getLocalHost(), paramInt1, paramInt2);
/*     */     else
/* 200 */       doConnect(paramInetAddress, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setOption(int paramInt, Object paramObject) throws SocketException
/*     */   {
/* 205 */     if (isClosedOrPending()) {
/* 206 */       throw new SocketException("Socket Closed");
/*     */     }
/* 208 */     boolean bool = true;
/* 209 */     switch (paramInt)
/*     */     {
/*     */     case 128:
/* 215 */       if ((paramObject == null) || ((!(paramObject instanceof Integer)) && (!(paramObject instanceof Boolean))))
/* 216 */         throw new SocketException("Bad parameter for option");
/* 217 */       if ((paramObject instanceof Boolean))
/*     */       {
/* 219 */         bool = false; } break;
/*     */     case 4102:
/* 223 */       if ((paramObject == null) || (!(paramObject instanceof Integer)))
/* 224 */         throw new SocketException("Bad parameter for SO_TIMEOUT");
/* 225 */       int i = ((Integer)paramObject).intValue();
/* 226 */       if (i < 0)
/* 227 */         throw new IllegalArgumentException("timeout < 0");
/* 228 */       this.timeout = i;
/* 229 */       break;
/*     */     case 3:
/* 231 */       if ((paramObject == null) || (!(paramObject instanceof Integer))) {
/* 232 */         throw new SocketException("bad argument for IP_TOS");
/*     */       }
/* 234 */       this.trafficClass = ((Integer)paramObject).intValue();
/* 235 */       break;
/*     */     case 15:
/* 237 */       throw new SocketException("Cannot re-bind socket");
/*     */     case 1:
/* 239 */       if ((paramObject == null) || (!(paramObject instanceof Boolean)))
/* 240 */         throw new SocketException("bad parameter for TCP_NODELAY");
/* 241 */       bool = ((Boolean)paramObject).booleanValue();
/* 242 */       break;
/*     */     case 4097:
/*     */     case 4098:
/* 245 */       if ((paramObject == null) || (!(paramObject instanceof Integer)) || (((Integer)paramObject).intValue() <= 0))
/*     */       {
/* 247 */         throw new SocketException("bad parameter for SO_SNDBUF or SO_RCVBUF");
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 8:
/* 252 */       if ((paramObject == null) || (!(paramObject instanceof Boolean)))
/* 253 */         throw new SocketException("bad parameter for SO_KEEPALIVE");
/* 254 */       bool = ((Boolean)paramObject).booleanValue();
/* 255 */       break;
/*     */     case 4099:
/* 257 */       if ((paramObject == null) || (!(paramObject instanceof Boolean)))
/* 258 */         throw new SocketException("bad parameter for SO_OOBINLINE");
/* 259 */       bool = ((Boolean)paramObject).booleanValue();
/* 260 */       break;
/*     */     case 4:
/* 262 */       if ((paramObject == null) || (!(paramObject instanceof Boolean)))
/* 263 */         throw new SocketException("bad parameter for SO_REUSEADDR");
/* 264 */       bool = ((Boolean)paramObject).booleanValue();
/* 265 */       break;
/*     */     default:
/* 267 */       throw new SocketException("unrecognized TCP option: " + paramInt);
/*     */     }
/* 269 */     socketSetOption(paramInt, bool, paramObject);
/*     */   }
/*     */   public Object getOption(int paramInt) throws SocketException {
/* 272 */     if (isClosedOrPending()) {
/* 273 */       throw new SocketException("Socket Closed");
/*     */     }
/* 275 */     if (paramInt == 4102) {
/* 276 */       return new Integer(this.timeout);
/*     */     }
/* 278 */     int i = 0;
/*     */ 
/* 287 */     switch (paramInt) {
/*     */     case 1:
/* 289 */       i = socketGetOption(paramInt, null);
/* 290 */       return Boolean.valueOf(i != -1);
/*     */     case 4099:
/* 292 */       i = socketGetOption(paramInt, null);
/* 293 */       return Boolean.valueOf(i != -1);
/*     */     case 128:
/* 295 */       i = socketGetOption(paramInt, null);
/* 296 */       return i == -1 ? Boolean.FALSE : new Integer(i);
/*     */     case 4:
/* 298 */       i = socketGetOption(paramInt, null);
/* 299 */       return Boolean.valueOf(i != -1);
/*     */     case 15:
/* 301 */       InetAddressContainer localInetAddressContainer = new InetAddressContainer();
/* 302 */       i = socketGetOption(paramInt, localInetAddressContainer);
/* 303 */       return localInetAddressContainer.addr;
/*     */     case 4097:
/*     */     case 4098:
/* 306 */       i = socketGetOption(paramInt, null);
/* 307 */       return new Integer(i);
/*     */     case 3:
/* 309 */       i = socketGetOption(paramInt, null);
/* 310 */       if (i == -1) {
/* 311 */         return new Integer(this.trafficClass);
/*     */       }
/* 313 */       return new Integer(i);
/*     */     case 8:
/* 316 */       i = socketGetOption(paramInt, null);
/* 317 */       return Boolean.valueOf(i != -1);
/*     */     }
/*     */ 
/* 320 */     return null;
/*     */   }
/*     */ 
/*     */   synchronized void doConnect(InetAddress paramInetAddress, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 331 */     synchronized (this.fdLock) {
/* 332 */       if ((!this.closePending) && ((this.socket == null) || (!this.socket.isBound())))
/* 333 */         NetHooks.beforeTcpConnect(this.fd, paramInetAddress, paramInt1);
/*     */     }
/*     */     try
/*     */     {
/* 337 */       acquireFD();
/*     */       try {
/* 339 */         socketConnect(paramInetAddress, paramInt1, paramInt2);
/*     */ 
/* 341 */         synchronized (this.fdLock) {
/* 342 */           if (this.closePending) {
/* 343 */             throw new SocketException("Socket closed");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 350 */         if (this.socket != null) {
/* 351 */           this.socket.setBound();
/* 352 */           this.socket.setConnected();
/*     */         }
/*     */       } finally {
/* 355 */         releaseFD();
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 358 */       close();
/* 359 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected synchronized void bind(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 371 */     synchronized (this.fdLock) {
/* 372 */       if ((!this.closePending) && ((this.socket == null) || (!this.socket.isBound()))) {
/* 373 */         NetHooks.beforeTcpBind(this.fd, paramInetAddress, paramInt);
/*     */       }
/*     */     }
/* 376 */     socketBind(paramInetAddress, paramInt);
/* 377 */     if (this.socket != null)
/* 378 */       this.socket.setBound();
/* 379 */     if (this.serverSocket != null)
/* 380 */       this.serverSocket.setBound();
/*     */   }
/*     */ 
/*     */   protected synchronized void listen(int paramInt)
/*     */     throws IOException
/*     */   {
/* 388 */     socketListen(paramInt);
/*     */   }
/*     */ 
/*     */   protected void accept(SocketImpl paramSocketImpl)
/*     */     throws IOException
/*     */   {
/* 396 */     acquireFD();
/*     */     try {
/* 398 */       socketAccept(paramSocketImpl);
/*     */     } finally {
/* 400 */       releaseFD();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected synchronized InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 408 */     if (isClosedOrPending()) {
/* 409 */       throw new IOException("Socket Closed");
/*     */     }
/* 411 */     if (this.shut_rd) {
/* 412 */       throw new IOException("Socket input is shutdown");
/*     */     }
/* 414 */     if (this.socketInputStream == null) {
/* 415 */       this.socketInputStream = new SocketInputStream(this);
/*     */     }
/* 417 */     return this.socketInputStream;
/*     */   }
/*     */ 
/*     */   void setInputStream(SocketInputStream paramSocketInputStream) {
/* 421 */     this.socketInputStream = paramSocketInputStream;
/*     */   }
/*     */ 
/*     */   protected synchronized OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 428 */     if (isClosedOrPending()) {
/* 429 */       throw new IOException("Socket Closed");
/*     */     }
/* 431 */     if (this.shut_wr) {
/* 432 */       throw new IOException("Socket output is shutdown");
/*     */     }
/* 434 */     return new SocketOutputStream(this);
/*     */   }
/*     */ 
/*     */   void setFileDescriptor(FileDescriptor paramFileDescriptor) {
/* 438 */     this.fd = paramFileDescriptor;
/*     */   }
/*     */ 
/*     */   void setAddress(InetAddress paramInetAddress) {
/* 442 */     this.address = paramInetAddress;
/*     */   }
/*     */ 
/*     */   void setPort(int paramInt) {
/* 446 */     this.port = paramInt;
/*     */   }
/*     */ 
/*     */   void setLocalPort(int paramInt) {
/* 450 */     this.localport = paramInt;
/*     */   }
/*     */ 
/*     */   protected synchronized int available()
/*     */     throws IOException
/*     */   {
/* 457 */     if (isClosedOrPending()) {
/* 458 */       throw new IOException("Stream closed.");
/*     */     }
/*     */ 
/* 465 */     if (isConnectionReset()) {
/* 466 */       return 0;
/*     */     }
/*     */ 
/* 476 */     int i = 0;
/*     */     try {
/* 478 */       i = socketAvailable();
/* 479 */       if ((i == 0) && (isConnectionResetPending()))
/* 480 */         setConnectionReset();
/*     */     }
/*     */     catch (ConnectionResetException localConnectionResetException1) {
/* 483 */       setConnectionResetPending();
/*     */       try {
/* 485 */         i = socketAvailable();
/* 486 */         if (i == 0)
/* 487 */           setConnectionReset();
/*     */       }
/*     */       catch (ConnectionResetException localConnectionResetException2) {
/*     */       }
/*     */     }
/* 492 */     return i;
/*     */   }
/*     */ 
/*     */   protected void close()
/*     */     throws IOException
/*     */   {
/* 499 */     synchronized (this.fdLock) {
/* 500 */       if (this.fd != null) {
/* 501 */         if (!this.stream) {
/* 502 */           ResourceManager.afterUdpClose();
/*     */         }
/* 504 */         if (this.fdUseCount == 0) {
/* 505 */           if (this.closePending) {
/* 506 */             return;
/*     */           }
/* 508 */           this.closePending = true;
/*     */           try
/*     */           {
/* 518 */             socketPreClose();
/*     */           } finally {
/* 520 */             socketClose();
/*     */           }
/* 522 */           this.fd = null;
/* 523 */           return;
/*     */         }
/*     */ 
/* 531 */         if (!this.closePending) {
/* 532 */           this.closePending = true;
/* 533 */           this.fdUseCount -= 1;
/* 534 */           socketPreClose();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void reset() throws IOException
/*     */   {
/* 542 */     if (this.fd != null) {
/* 543 */       socketClose();
/*     */     }
/* 545 */     this.fd = null;
/* 546 */     super.reset();
/*     */   }
/*     */ 
/*     */   protected void shutdownInput()
/*     */     throws IOException
/*     */   {
/* 554 */     if (this.fd != null) {
/* 555 */       socketShutdown(0);
/* 556 */       if (this.socketInputStream != null) {
/* 557 */         this.socketInputStream.setEOF(true);
/*     */       }
/* 559 */       this.shut_rd = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void shutdownOutput()
/*     */     throws IOException
/*     */   {
/* 567 */     if (this.fd != null) {
/* 568 */       socketShutdown(1);
/* 569 */       this.shut_wr = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean supportsUrgentData() {
/* 574 */     return true;
/*     */   }
/*     */ 
/*     */   protected void sendUrgentData(int paramInt) throws IOException {
/* 578 */     if (this.fd == null) {
/* 579 */       throw new IOException("Socket Closed");
/*     */     }
/* 581 */     socketSendUrgentData(paramInt);
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws IOException
/*     */   {
/* 588 */     close();
/*     */   }
/*     */ 
/*     */   FileDescriptor acquireFD()
/*     */   {
/* 598 */     synchronized (this.fdLock) {
/* 599 */       this.fdUseCount += 1;
/* 600 */       return this.fd;
/*     */     }
/*     */   }
/*     */ 
/*     */   void releaseFD()
/*     */   {
/* 610 */     synchronized (this.fdLock) {
/* 611 */       this.fdUseCount -= 1;
/* 612 */       if ((this.fdUseCount == -1) && 
/* 613 */         (this.fd != null))
/*     */         try {
/* 615 */           socketClose();
/*     */         } catch (IOException localIOException) {
/*     */         } finally {
/* 618 */           this.fd = null;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isConnectionReset()
/*     */   {
/* 626 */     synchronized (this.resetLock) {
/* 627 */       return this.resetState == this.CONNECTION_RESET;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isConnectionResetPending() {
/* 632 */     synchronized (this.resetLock) {
/* 633 */       return this.resetState == this.CONNECTION_RESET_PENDING;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setConnectionReset() {
/* 638 */     synchronized (this.resetLock) {
/* 639 */       this.resetState = this.CONNECTION_RESET;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setConnectionResetPending() {
/* 644 */     synchronized (this.resetLock) {
/* 645 */       if (this.resetState == this.CONNECTION_NOT_RESET)
/* 646 */         this.resetState = this.CONNECTION_RESET_PENDING;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isClosedOrPending()
/*     */   {
/* 660 */     synchronized (this.fdLock) {
/* 661 */       if ((this.closePending) || (this.fd == null)) {
/* 662 */         return true;
/*     */       }
/* 664 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getTimeout()
/*     */   {
/* 673 */     return this.timeout;
/*     */   }
/*     */ 
/*     */   private void socketPreClose()
/*     */     throws IOException
/*     */   {
/* 681 */     socketClose0(true);
/*     */   }
/*     */ 
/*     */   protected void socketClose()
/*     */     throws IOException
/*     */   {
/* 688 */     socketClose0(false);
/*     */   }
/*     */ 
/*     */   abstract void socketCreate(boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   abstract void socketConnect(InetAddress paramInetAddress, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   abstract void socketBind(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   abstract void socketListen(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   abstract void socketAccept(SocketImpl paramSocketImpl)
/*     */     throws IOException;
/*     */ 
/*     */   abstract int socketAvailable()
/*     */     throws IOException;
/*     */ 
/*     */   abstract void socketClose0(boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   abstract void socketShutdown(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   abstract void socketSetOption(int paramInt, boolean paramBoolean, Object paramObject)
/*     */     throws SocketException;
/*     */ 
/*     */   abstract int socketGetOption(int paramInt, Object paramObject)
/*     */     throws SocketException;
/*     */ 
/*     */   abstract void socketSendUrgentData(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static
/*     */   {
/*  80 */     AccessController.doPrivileged(new LoadLibraryAction("net"));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.AbstractPlainSocketImpl
 * JD-Core Version:    0.6.2
 */