/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Properties;
/*     */ 
/*     */ class PlainSocketImpl extends AbstractPlainSocketImpl
/*     */ {
/*     */   private AbstractPlainSocketImpl impl;
/*     */   private static float version;
/*  52 */   private static boolean preferIPv4Stack = false;
/*     */ 
/*  55 */   private static boolean useDualStackImpl = false;
/*     */   private static String exclBindProp;
/*  61 */   private static boolean exclusiveBind = true;
/*     */ 
/*     */   PlainSocketImpl()
/*     */   {
/*  96 */     if (useDualStackImpl)
/*  97 */       this.impl = new DualStackPlainSocketImpl(exclusiveBind);
/*     */     else
/*  99 */       this.impl = new TwoStacksPlainSocketImpl(exclusiveBind);
/*     */   }
/*     */ 
/*     */   PlainSocketImpl(FileDescriptor paramFileDescriptor)
/*     */   {
/* 107 */     if (useDualStackImpl)
/* 108 */       this.impl = new DualStackPlainSocketImpl(paramFileDescriptor, exclusiveBind);
/*     */     else
/* 110 */       this.impl = new TwoStacksPlainSocketImpl(paramFileDescriptor, exclusiveBind);
/*     */   }
/*     */ 
/*     */   protected FileDescriptor getFileDescriptor()
/*     */   {
/* 117 */     return this.impl.getFileDescriptor();
/*     */   }
/*     */ 
/*     */   protected InetAddress getInetAddress() {
/* 121 */     return this.impl.getInetAddress();
/*     */   }
/*     */ 
/*     */   protected int getPort() {
/* 125 */     return this.impl.getPort();
/*     */   }
/*     */ 
/*     */   protected int getLocalPort() {
/* 129 */     return this.impl.getLocalPort();
/*     */   }
/*     */ 
/*     */   void setSocket(Socket paramSocket) {
/* 133 */     this.impl.setSocket(paramSocket);
/*     */   }
/*     */ 
/*     */   Socket getSocket() {
/* 137 */     return this.impl.getSocket();
/*     */   }
/*     */ 
/*     */   void setServerSocket(ServerSocket paramServerSocket) {
/* 141 */     this.impl.setServerSocket(paramServerSocket);
/*     */   }
/*     */ 
/*     */   ServerSocket getServerSocket() {
/* 145 */     return this.impl.getServerSocket();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 149 */     return this.impl.toString();
/*     */   }
/*     */ 
/*     */   protected synchronized void create(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 155 */     this.impl.create(paramBoolean);
/*     */ 
/* 158 */     this.fd = this.impl.fd;
/*     */   }
/*     */ 
/*     */   protected void connect(String paramString, int paramInt)
/*     */     throws UnknownHostException, IOException
/*     */   {
/* 164 */     this.impl.connect(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   protected void connect(InetAddress paramInetAddress, int paramInt) throws IOException {
/* 168 */     this.impl.connect(paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   protected void connect(SocketAddress paramSocketAddress, int paramInt) throws IOException {
/* 172 */     this.impl.connect(paramSocketAddress, paramInt);
/*     */   }
/*     */ 
/*     */   public void setOption(int paramInt, Object paramObject) throws SocketException {
/* 176 */     this.impl.setOption(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   public Object getOption(int paramInt) throws SocketException {
/* 180 */     return this.impl.getOption(paramInt);
/*     */   }
/*     */ 
/*     */   synchronized void doConnect(InetAddress paramInetAddress, int paramInt1, int paramInt2) throws IOException {
/* 184 */     this.impl.doConnect(paramInetAddress, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected synchronized void bind(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 190 */     this.impl.bind(paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   protected synchronized void accept(SocketImpl paramSocketImpl) throws IOException {
/* 194 */     if ((paramSocketImpl instanceof PlainSocketImpl))
/*     */     {
/* 196 */       AbstractPlainSocketImpl localAbstractPlainSocketImpl = ((PlainSocketImpl)paramSocketImpl).impl;
/* 197 */       localAbstractPlainSocketImpl.address = new InetAddress();
/* 198 */       localAbstractPlainSocketImpl.fd = new FileDescriptor();
/* 199 */       this.impl.accept(localAbstractPlainSocketImpl);
/*     */ 
/* 201 */       paramSocketImpl.fd = localAbstractPlainSocketImpl.fd;
/*     */     } else {
/* 203 */       this.impl.accept(paramSocketImpl);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setFileDescriptor(FileDescriptor paramFileDescriptor) {
/* 208 */     this.impl.setFileDescriptor(paramFileDescriptor);
/*     */   }
/*     */ 
/*     */   void setAddress(InetAddress paramInetAddress) {
/* 212 */     this.impl.setAddress(paramInetAddress);
/*     */   }
/*     */ 
/*     */   void setPort(int paramInt) {
/* 216 */     this.impl.setPort(paramInt);
/*     */   }
/*     */ 
/*     */   void setLocalPort(int paramInt) {
/* 220 */     this.impl.setLocalPort(paramInt);
/*     */   }
/*     */ 
/*     */   protected synchronized InputStream getInputStream() throws IOException {
/* 224 */     return this.impl.getInputStream();
/*     */   }
/*     */ 
/*     */   void setInputStream(SocketInputStream paramSocketInputStream) {
/* 228 */     this.impl.setInputStream(paramSocketInputStream);
/*     */   }
/*     */ 
/*     */   protected synchronized OutputStream getOutputStream() throws IOException {
/* 232 */     return this.impl.getOutputStream();
/*     */   }
/*     */ 
/*     */   protected void close() throws IOException {
/*     */     try {
/* 237 */       this.impl.close();
/*     */     }
/*     */     finally {
/* 240 */       this.fd = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   void reset() throws IOException {
/*     */     try {
/* 246 */       this.impl.reset();
/*     */     }
/*     */     finally {
/* 249 */       this.fd = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void shutdownInput() throws IOException {
/* 254 */     this.impl.shutdownInput();
/*     */   }
/*     */ 
/*     */   protected void shutdownOutput() throws IOException {
/* 258 */     this.impl.shutdownOutput();
/*     */   }
/*     */ 
/*     */   protected void sendUrgentData(int paramInt) throws IOException {
/* 262 */     this.impl.sendUrgentData(paramInt);
/*     */   }
/*     */ 
/*     */   FileDescriptor acquireFD() {
/* 266 */     return this.impl.acquireFD();
/*     */   }
/*     */ 
/*     */   void releaseFD() {
/* 270 */     this.impl.releaseFD();
/*     */   }
/*     */ 
/*     */   public boolean isConnectionReset() {
/* 274 */     return this.impl.isConnectionReset();
/*     */   }
/*     */ 
/*     */   public boolean isConnectionResetPending() {
/* 278 */     return this.impl.isConnectionResetPending();
/*     */   }
/*     */ 
/*     */   public void setConnectionReset() {
/* 282 */     this.impl.setConnectionReset();
/*     */   }
/*     */ 
/*     */   public void setConnectionResetPending() {
/* 286 */     this.impl.setConnectionResetPending();
/*     */   }
/*     */ 
/*     */   public boolean isClosedOrPending() {
/* 290 */     return this.impl.isClosedOrPending();
/*     */   }
/*     */ 
/*     */   public int getTimeout() {
/* 294 */     return this.impl.getTimeout();
/*     */   }
/*     */ 
/*     */   void socketCreate(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 300 */     this.impl.socketCreate(paramBoolean);
/*     */   }
/*     */ 
/*     */   void socketConnect(InetAddress paramInetAddress, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 305 */     this.impl.socketConnect(paramInetAddress, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   void socketBind(InetAddress paramInetAddress, int paramInt) throws IOException
/*     */   {
/* 310 */     this.impl.socketBind(paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   void socketListen(int paramInt) throws IOException {
/* 314 */     this.impl.socketListen(paramInt);
/*     */   }
/*     */ 
/*     */   void socketAccept(SocketImpl paramSocketImpl) throws IOException {
/* 318 */     this.impl.socketAccept(paramSocketImpl);
/*     */   }
/*     */ 
/*     */   int socketAvailable() throws IOException {
/* 322 */     return this.impl.socketAvailable();
/*     */   }
/*     */ 
/*     */   void socketClose0(boolean paramBoolean) throws IOException {
/* 326 */     this.impl.socketClose0(paramBoolean);
/*     */   }
/*     */ 
/*     */   void socketShutdown(int paramInt) throws IOException {
/* 330 */     this.impl.socketShutdown(paramInt);
/*     */   }
/*     */ 
/*     */   void socketSetOption(int paramInt, boolean paramBoolean, Object paramObject) throws SocketException
/*     */   {
/* 335 */     socketSetOption(paramInt, paramBoolean, paramObject);
/*     */   }
/*     */ 
/*     */   int socketGetOption(int paramInt, Object paramObject) throws SocketException {
/* 339 */     return this.impl.socketGetOption(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   void socketSendUrgentData(int paramInt) throws IOException {
/* 343 */     this.impl.socketSendUrgentData(paramInt);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  64 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*  66 */         PlainSocketImpl.access$002(0.0F);
/*     */         try {
/*  68 */           PlainSocketImpl.access$002(Float.parseFloat(System.getProperties().getProperty("os.version")));
/*  69 */           PlainSocketImpl.access$102(Boolean.parseBoolean(System.getProperties().getProperty("java.net.preferIPv4Stack")));
/*     */ 
/*  71 */           PlainSocketImpl.access$202(System.getProperty("sun.net.useExclusiveBind"));
/*     */         } catch (NumberFormatException localNumberFormatException) {
/*  73 */           if (!$assertionsDisabled) throw new AssertionError(localNumberFormatException);
/*     */         }
/*  75 */         return null;
/*     */       }
/*     */     });
/*  79 */     if ((version >= 6.0D) && (!preferIPv4Stack)) {
/*  80 */       useDualStackImpl = true;
/*     */     }
/*     */ 
/*  83 */     if (exclBindProp != null)
/*     */     {
/*  85 */       exclusiveBind = exclBindProp.length() == 0 ? true : Boolean.parseBoolean(exclBindProp);
/*     */     }
/*  87 */     else if (version < 6.0D)
/*  88 */       exclusiveBind = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.PlainSocketImpl
 * JD-Core Version:    0.6.2
 */