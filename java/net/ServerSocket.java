/*     */ package java.net;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ 
/*     */ public class ServerSocket
/*     */   implements Closeable
/*     */ {
/*  56 */   private boolean created = false;
/*  57 */   private boolean bound = false;
/*  58 */   private boolean closed = false;
/*  59 */   private Object closeLock = new Object();
/*     */   private SocketImpl impl;
/*  69 */   private boolean oldImpl = false;
/*     */ 
/* 749 */   private static SocketImplFactory factory = null;
/*     */ 
/*     */   ServerSocket(SocketImpl paramSocketImpl)
/*     */   {
/*  76 */     this.impl = paramSocketImpl;
/*  77 */     paramSocketImpl.setServerSocket(this);
/*     */   }
/*     */ 
/*     */   public ServerSocket()
/*     */     throws IOException
/*     */   {
/*  87 */     setImpl();
/*     */   }
/*     */ 
/*     */   public ServerSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/* 128 */     this(paramInt, 50, null);
/*     */   }
/*     */ 
/*     */   public ServerSocket(int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 181 */     this(paramInt1, paramInt2, null);
/*     */   }
/*     */ 
/*     */   public ServerSocket(int paramInt1, int paramInt2, InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/* 230 */     setImpl();
/* 231 */     if ((paramInt1 < 0) || (paramInt1 > 65535)) {
/* 232 */       throw new IllegalArgumentException("Port value out of range: " + paramInt1);
/*     */     }
/* 234 */     if (paramInt2 < 1)
/* 235 */       paramInt2 = 50;
/*     */     try {
/* 237 */       bind(new InetSocketAddress(paramInetAddress, paramInt1), paramInt2);
/*     */     } catch (SecurityException localSecurityException) {
/* 239 */       close();
/* 240 */       throw localSecurityException;
/*     */     } catch (IOException localIOException) {
/* 242 */       close();
/* 243 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   SocketImpl getImpl()
/*     */     throws SocketException
/*     */   {
/* 256 */     if (!this.created)
/* 257 */       createImpl();
/* 258 */     return this.impl;
/*     */   }
/*     */ 
/*     */   private void checkOldImpl() {
/* 262 */     if (this.impl == null) {
/* 263 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 267 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws NoSuchMethodException {
/* 270 */           Class[] arrayOfClass = new Class[2];
/* 271 */           arrayOfClass[0] = SocketAddress.class;
/* 272 */           arrayOfClass[1] = Integer.TYPE;
/* 273 */           ServerSocket.this.impl.getClass().getDeclaredMethod("connect", arrayOfClass);
/* 274 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 278 */       this.oldImpl = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setImpl() {
/* 283 */     if (factory != null) {
/* 284 */       this.impl = factory.createSocketImpl();
/* 285 */       checkOldImpl();
/*     */     }
/*     */     else
/*     */     {
/* 289 */       this.impl = new SocksSocketImpl();
/*     */     }
/* 291 */     if (this.impl != null)
/* 292 */       this.impl.setServerSocket(this);
/*     */   }
/*     */ 
/*     */   void createImpl()
/*     */     throws SocketException
/*     */   {
/* 302 */     if (this.impl == null)
/* 303 */       setImpl();
/*     */     try {
/* 305 */       this.impl.create(true);
/* 306 */       this.created = true;
/*     */     } catch (IOException localIOException) {
/* 308 */       throw new SocketException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/* 330 */     bind(paramSocketAddress, 50);
/*     */   }
/*     */ 
/*     */   public void bind(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 359 */     if (isClosed())
/* 360 */       throw new SocketException("Socket is closed");
/* 361 */     if ((!this.oldImpl) && (isBound()))
/* 362 */       throw new SocketException("Already bound");
/* 363 */     if (paramSocketAddress == null)
/* 364 */       paramSocketAddress = new InetSocketAddress(0);
/* 365 */     if (!(paramSocketAddress instanceof InetSocketAddress))
/* 366 */       throw new IllegalArgumentException("Unsupported address type");
/* 367 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/* 368 */     if (localInetSocketAddress.isUnresolved())
/* 369 */       throw new SocketException("Unresolved address");
/* 370 */     if (paramInt < 1)
/* 371 */       paramInt = 50;
/*     */     try {
/* 373 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 374 */       if (localSecurityManager != null)
/* 375 */         localSecurityManager.checkListen(localInetSocketAddress.getPort());
/* 376 */       getImpl().bind(localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/* 377 */       getImpl().listen(paramInt);
/* 378 */       this.bound = true;
/*     */     } catch (SecurityException localSecurityException) {
/* 380 */       this.bound = false;
/* 381 */       throw localSecurityException;
/*     */     } catch (IOException localIOException) {
/* 383 */       this.bound = false;
/* 384 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public InetAddress getInetAddress()
/*     */   {
/* 399 */     if (!isBound())
/* 400 */       return null;
/*     */     try {
/* 402 */       InetAddress localInetAddress = getImpl().getInetAddress();
/* 403 */       if (!NetUtil.doRevealLocalAddress()) {
/* 404 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 405 */         if (localSecurityManager != null)
/* 406 */           localSecurityManager.checkConnect(localInetAddress.getHostAddress(), -1);
/*     */       }
/* 408 */       return localInetAddress;
/*     */     } catch (SecurityException localSecurityException) {
/* 410 */       return InetAddress.getLoopbackAddress();
/*     */     }
/*     */     catch (SocketException localSocketException)
/*     */     {
/*     */     }
/*     */ 
/* 416 */     return null;
/*     */   }
/*     */ 
/*     */   public int getLocalPort()
/*     */   {
/* 430 */     if (!isBound())
/* 431 */       return -1;
/*     */     try {
/* 433 */       return getImpl().getLocalPort();
/*     */     }
/*     */     catch (SocketException localSocketException)
/*     */     {
/*     */     }
/*     */ 
/* 439 */     return -1;
/*     */   }
/*     */ 
/*     */   public SocketAddress getLocalSocketAddress()
/*     */   {
/* 459 */     if (!isBound())
/* 460 */       return null;
/* 461 */     return new InetSocketAddress(getInetAddress(), getLocalPort());
/*     */   }
/*     */ 
/*     */   public Socket accept()
/*     */     throws IOException
/*     */   {
/* 493 */     if (isClosed())
/* 494 */       throw new SocketException("Socket is closed");
/* 495 */     if (!isBound())
/* 496 */       throw new SocketException("Socket is not bound yet");
/* 497 */     Socket localSocket = new Socket((SocketImpl)null);
/* 498 */     implAccept(localSocket);
/* 499 */     return localSocket;
/*     */   }
/*     */ 
/*     */   protected final void implAccept(Socket paramSocket)
/*     */     throws IOException
/*     */   {
/* 519 */     SocketImpl localSocketImpl = null;
/*     */     try {
/* 521 */       if (paramSocket.impl == null)
/* 522 */         paramSocket.setImpl();
/*     */       else {
/* 524 */         paramSocket.impl.reset();
/*     */       }
/* 526 */       localSocketImpl = paramSocket.impl;
/* 527 */       paramSocket.impl = null;
/* 528 */       localSocketImpl.address = new InetAddress();
/* 529 */       localSocketImpl.fd = new FileDescriptor();
/* 530 */       getImpl().accept(localSocketImpl);
/*     */ 
/* 532 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 533 */       if (localSecurityManager != null)
/* 534 */         localSecurityManager.checkAccept(localSocketImpl.getInetAddress().getHostAddress(), localSocketImpl.getPort());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 538 */       if (localSocketImpl != null)
/* 539 */         localSocketImpl.reset();
/* 540 */       paramSocket.impl = localSocketImpl;
/* 541 */       throw localIOException;
/*     */     } catch (SecurityException localSecurityException) {
/* 543 */       if (localSocketImpl != null)
/* 544 */         localSocketImpl.reset();
/* 545 */       paramSocket.impl = localSocketImpl;
/* 546 */       throw localSecurityException;
/*     */     }
/* 548 */     paramSocket.impl = localSocketImpl;
/* 549 */     paramSocket.postAccept();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 566 */     synchronized (this.closeLock) {
/* 567 */       if (isClosed())
/* 568 */         return;
/* 569 */       if (this.created)
/* 570 */         this.impl.close();
/* 571 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServerSocketChannel getChannel()
/*     */   {
/* 592 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isBound()
/*     */   {
/* 603 */     return (this.bound) || (this.oldImpl);
/*     */   }
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 613 */     synchronized (this.closeLock) {
/* 614 */       return this.closed;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void setSoTimeout(int paramInt)
/*     */     throws SocketException
/*     */   {
/* 635 */     if (isClosed())
/* 636 */       throw new SocketException("Socket is closed");
/* 637 */     getImpl().setOption(4102, new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public synchronized int getSoTimeout()
/*     */     throws IOException
/*     */   {
/* 649 */     if (isClosed())
/* 650 */       throw new SocketException("Socket is closed");
/* 651 */     Object localObject = getImpl().getOption(4102);
/*     */ 
/* 653 */     if ((localObject instanceof Integer)) {
/* 654 */       return ((Integer)localObject).intValue();
/*     */     }
/* 656 */     return 0;
/*     */   }
/*     */ 
/*     */   public void setReuseAddress(boolean paramBoolean)
/*     */     throws SocketException
/*     */   {
/* 697 */     if (isClosed())
/* 698 */       throw new SocketException("Socket is closed");
/* 699 */     getImpl().setOption(4, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public boolean getReuseAddress()
/*     */     throws SocketException
/*     */   {
/* 712 */     if (isClosed())
/* 713 */       throw new SocketException("Socket is closed");
/* 714 */     return ((Boolean)getImpl().getOption(4)).booleanValue();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 724 */     if (!isBound())
/* 725 */       return "ServerSocket[unbound]";
/*     */     InetAddress localInetAddress;
/* 727 */     if ((!NetUtil.doRevealLocalAddress()) && (System.getSecurityManager() != null))
/*     */     {
/* 730 */       localInetAddress = InetAddress.getLoopbackAddress();
/*     */     }
/* 732 */     else localInetAddress = this.impl.getInetAddress();
/*     */ 
/* 734 */     return "ServerSocket[addr=" + localInetAddress + ",localport=" + this.impl.getLocalPort() + "]";
/*     */   }
/*     */ 
/*     */   void setBound()
/*     */   {
/* 739 */     this.bound = true;
/*     */   }
/*     */ 
/*     */   void setCreated() {
/* 743 */     this.created = true;
/*     */   }
/*     */ 
/*     */   public static synchronized void setSocketFactory(SocketImplFactory paramSocketImplFactory)
/*     */     throws IOException
/*     */   {
/* 777 */     if (factory != null) {
/* 778 */       throw new SocketException("factory already defined");
/*     */     }
/* 780 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 781 */     if (localSecurityManager != null) {
/* 782 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 784 */     factory = paramSocketImplFactory;
/*     */   }
/*     */ 
/*     */   public synchronized void setReceiveBufferSize(int paramInt)
/*     */     throws SocketException
/*     */   {
/* 823 */     if (paramInt <= 0) {
/* 824 */       throw new IllegalArgumentException("negative receive size");
/*     */     }
/* 826 */     if (isClosed())
/* 827 */       throw new SocketException("Socket is closed");
/* 828 */     getImpl().setOption(4098, new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public synchronized int getReceiveBufferSize()
/*     */     throws SocketException
/*     */   {
/* 846 */     if (isClosed())
/* 847 */       throw new SocketException("Socket is closed");
/* 848 */     int i = 0;
/* 849 */     Object localObject = getImpl().getOption(4098);
/* 850 */     if ((localObject instanceof Integer)) {
/* 851 */       i = ((Integer)localObject).intValue();
/*     */     }
/* 853 */     return i;
/*     */   }
/*     */ 
/*     */   public void setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.ServerSocket
 * JD-Core Version:    0.6.2
 */