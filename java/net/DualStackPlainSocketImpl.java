/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import sun.misc.JavaIOFileDescriptorAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ class DualStackPlainSocketImpl extends AbstractPlainSocketImpl
/*     */ {
/*  43 */   static JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
/*     */   private final boolean exclusiveBind;
/*     */   private boolean isReuseAddress;
/*     */   static final int WOULDBLOCK = -2;
/*     */ 
/*     */   public DualStackPlainSocketImpl(boolean paramBoolean)
/*     */   {
/*  53 */     this.exclusiveBind = paramBoolean;
/*     */   }
/*     */ 
/*     */   public DualStackPlainSocketImpl(FileDescriptor paramFileDescriptor, boolean paramBoolean) {
/*  57 */     this.fd = paramFileDescriptor;
/*  58 */     this.exclusiveBind = paramBoolean;
/*     */   }
/*     */ 
/*     */   void socketCreate(boolean paramBoolean) throws IOException {
/*  62 */     if (this.fd == null) {
/*  63 */       throw new SocketException("Socket closed");
/*     */     }
/*  65 */     int i = socket0(paramBoolean, false);
/*     */ 
/*  67 */     fdAccess.set(this.fd, i);
/*     */   }
/*     */ 
/*     */   void socketConnect(InetAddress paramInetAddress, int paramInt1, int paramInt2) throws IOException
/*     */   {
/*  72 */     int i = checkAndReturnNativeFD();
/*     */ 
/*  74 */     if (paramInetAddress == null)
/*  75 */       throw new NullPointerException("inet address argument is null.");
/*     */     int j;
/*  78 */     if (paramInt2 <= 0) {
/*  79 */       j = connect0(i, paramInetAddress, paramInt1);
/*     */     } else {
/*  81 */       configureBlocking(i, false);
/*     */       try {
/*  83 */         j = connect0(i, paramInetAddress, paramInt1);
/*  84 */         if (j == -2)
/*  85 */           waitForConnect(i, paramInt2);
/*     */       }
/*     */       finally {
/*  88 */         configureBlocking(i, true);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  96 */     if (this.localport == 0)
/*  97 */       this.localport = localPort0(i);
/*     */   }
/*     */ 
/*     */   void socketBind(InetAddress paramInetAddress, int paramInt) throws IOException {
/* 101 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 103 */     if (paramInetAddress == null) {
/* 104 */       throw new NullPointerException("inet address argument is null.");
/*     */     }
/* 106 */     bind0(i, paramInetAddress, paramInt, this.exclusiveBind);
/* 107 */     if (paramInt == 0)
/* 108 */       this.localport = localPort0(i);
/*     */     else {
/* 110 */       this.localport = paramInt;
/*     */     }
/*     */ 
/* 113 */     this.address = paramInetAddress;
/*     */   }
/*     */ 
/*     */   void socketListen(int paramInt) throws IOException {
/* 117 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 119 */     listen0(i, paramInt);
/*     */   }
/*     */ 
/*     */   void socketAccept(SocketImpl paramSocketImpl) throws IOException {
/* 123 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 125 */     if (paramSocketImpl == null) {
/* 126 */       throw new NullPointerException("socket is null");
/*     */     }
/* 128 */     int j = -1;
/* 129 */     InetSocketAddress[] arrayOfInetSocketAddress = new InetSocketAddress[1];
/* 130 */     if (this.timeout <= 0) {
/* 131 */       j = accept0(i, arrayOfInetSocketAddress);
/*     */     } else {
/* 133 */       configureBlocking(i, false);
/*     */       try {
/* 135 */         waitForNewConnection(i, this.timeout);
/* 136 */         j = accept0(i, arrayOfInetSocketAddress);
/* 137 */         if (j != -1)
/* 138 */           configureBlocking(j, true);
/*     */       }
/*     */       finally {
/* 141 */         configureBlocking(i, true);
/*     */       }
/*     */     }
/*     */ 
/* 145 */     fdAccess.set(paramSocketImpl.fd, j);
/*     */ 
/* 147 */     InetSocketAddress localInetSocketAddress = arrayOfInetSocketAddress[0];
/* 148 */     paramSocketImpl.port = localInetSocketAddress.getPort();
/* 149 */     paramSocketImpl.address = localInetSocketAddress.getAddress();
/* 150 */     paramSocketImpl.localport = this.localport;
/*     */   }
/*     */ 
/*     */   int socketAvailable() throws IOException {
/* 154 */     int i = checkAndReturnNativeFD();
/* 155 */     return available0(i);
/*     */   }
/*     */ 
/*     */   void socketClose0(boolean paramBoolean) throws IOException {
/* 159 */     if (this.fd == null) {
/* 160 */       throw new SocketException("Socket closed");
/*     */     }
/* 162 */     if (!this.fd.valid()) {
/* 163 */       return;
/*     */     }
/* 165 */     int i = fdAccess.get(this.fd);
/* 166 */     fdAccess.set(this.fd, -1);
/* 167 */     close0(i);
/*     */   }
/*     */ 
/*     */   void socketShutdown(int paramInt) throws IOException {
/* 171 */     int i = checkAndReturnNativeFD();
/* 172 */     shutdown0(i, paramInt);
/*     */   }
/*     */ 
/*     */   void socketSetOption(int paramInt, boolean paramBoolean, Object paramObject)
/*     */     throws SocketException
/*     */   {
/* 179 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 181 */     if (paramInt == 4102) {
/* 182 */       return;
/*     */     }
/*     */ 
/* 185 */     int j = 0;
/*     */ 
/* 187 */     switch (paramInt) {
/*     */     case 4:
/* 189 */       if (this.exclusiveBind)
/*     */       {
/* 191 */         this.isReuseAddress = paramBoolean;
/* 192 */         return;
/*     */       }
/*     */ 
/*     */     case 1:
/*     */     case 8:
/*     */     case 4099:
/* 198 */       j = paramBoolean ? 1 : 0;
/* 199 */       break;
/*     */     case 3:
/*     */     case 4097:
/*     */     case 4098:
/* 203 */       j = ((Integer)paramObject).intValue();
/* 204 */       break;
/*     */     case 128:
/* 206 */       if (paramBoolean)
/* 207 */         j = ((Integer)paramObject).intValue();
/*     */       else {
/* 209 */         j = -1;
/*     */       }
/* 211 */       break;
/*     */     }
/* 213 */     throw new SocketException("Option not supported");
/*     */ 
/* 216 */     setIntOption(i, paramInt, j);
/*     */   }
/*     */ 
/*     */   int socketGetOption(int paramInt, Object paramObject) throws SocketException {
/* 220 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 223 */     if (paramInt == 15) {
/* 224 */       localAddress(i, (InetAddressContainer)paramObject);
/* 225 */       return 0;
/*     */     }
/*     */ 
/* 229 */     if ((paramInt == 4) && (this.exclusiveBind)) {
/* 230 */       return this.isReuseAddress ? 1 : -1;
/*     */     }
/* 232 */     int j = getIntOption(i, paramInt);
/*     */ 
/* 234 */     switch (paramInt) {
/*     */     case 1:
/*     */     case 4:
/*     */     case 8:
/*     */     case 4099:
/* 239 */       return j == 0 ? -1 : 1;
/*     */     }
/* 241 */     return j;
/*     */   }
/*     */ 
/*     */   void socketSendUrgentData(int paramInt) throws IOException {
/* 245 */     int i = checkAndReturnNativeFD();
/* 246 */     sendOOB(i, paramInt);
/*     */   }
/*     */ 
/*     */   private int checkAndReturnNativeFD() throws SocketException {
/* 250 */     if ((this.fd == null) || (!this.fd.valid())) {
/* 251 */       throw new SocketException("Socket closed");
/*     */     }
/* 253 */     return fdAccess.get(this.fd); } 
/*     */   static native void initIDs();
/*     */ 
/*     */   static native int socket0(boolean paramBoolean1, boolean paramBoolean2) throws IOException;
/*     */ 
/*     */   static native void bind0(int paramInt1, InetAddress paramInetAddress, int paramInt2, boolean paramBoolean) throws IOException;
/*     */ 
/*     */   static native int connect0(int paramInt1, InetAddress paramInetAddress, int paramInt2) throws IOException;
/*     */ 
/*     */   static native void waitForConnect(int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   static native int localPort0(int paramInt) throws IOException;
/*     */ 
/*     */   static native void localAddress(int paramInt, InetAddressContainer paramInetAddressContainer) throws SocketException;
/*     */ 
/*     */   static native void listen0(int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   static native int accept0(int paramInt, InetSocketAddress[] paramArrayOfInetSocketAddress) throws IOException;
/*     */ 
/*     */   static native void waitForNewConnection(int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   static native int available0(int paramInt) throws IOException;
/*     */ 
/*     */   static native void close0(int paramInt) throws IOException;
/*     */ 
/*     */   static native void shutdown0(int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   static native void setIntOption(int paramInt1, int paramInt2, int paramInt3) throws SocketException;
/*     */ 
/*     */   static native int getIntOption(int paramInt1, int paramInt2) throws SocketException;
/*     */ 
/*     */   static native void sendOOB(int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   static native void configureBlocking(int paramInt, boolean paramBoolean) throws IOException;
/*     */ 
/* 259 */   static { initIDs(); }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.DualStackPlainSocketImpl
 * JD-Core Version:    0.6.2
 */