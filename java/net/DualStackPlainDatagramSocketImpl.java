/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import sun.misc.JavaIOFileDescriptorAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ class DualStackPlainDatagramSocketImpl extends AbstractPlainDatagramSocketImpl
/*     */ {
/*  47 */   static JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
/*     */   private final boolean exclusiveBind;
/*     */   private boolean reuseAddressEmulated;
/*     */   private boolean isReuseAddress;
/*     */ 
/*     */   DualStackPlainDatagramSocketImpl(boolean paramBoolean)
/*     */   {
/*  62 */     this.exclusiveBind = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void datagramSocketCreate() throws SocketException {
/*  66 */     if (this.fd == null) {
/*  67 */       throw new SocketException("Socket closed");
/*     */     }
/*  69 */     int i = socketCreate(false);
/*     */ 
/*  71 */     fdAccess.set(this.fd, i);
/*     */   }
/*     */ 
/*     */   protected synchronized void bind0(int paramInt, InetAddress paramInetAddress) throws SocketException
/*     */   {
/*  76 */     int i = checkAndReturnNativeFD();
/*     */ 
/*  78 */     if (paramInetAddress == null) {
/*  79 */       throw new NullPointerException("argument address");
/*     */     }
/*  81 */     socketBind(i, paramInetAddress, paramInt, this.exclusiveBind);
/*  82 */     if (paramInt == 0)
/*  83 */       this.localPort = socketLocalPort(i);
/*     */     else
/*  85 */       this.localPort = paramInt;
/*     */   }
/*     */ 
/*     */   protected synchronized int peek(InetAddress paramInetAddress) throws IOException
/*     */   {
/*  90 */     int i = checkAndReturnNativeFD();
/*     */ 
/*  92 */     if (paramInetAddress == null) {
/*  93 */       throw new NullPointerException("Null address in peek()");
/*     */     }
/*     */ 
/*  96 */     DatagramPacket localDatagramPacket = new DatagramPacket(new byte[1], 1);
/*  97 */     int j = peekData(localDatagramPacket);
/*  98 */     paramInetAddress = localDatagramPacket.getAddress();
/*  99 */     return j;
/*     */   }
/*     */ 
/*     */   protected synchronized int peekData(DatagramPacket paramDatagramPacket) throws IOException {
/* 103 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 105 */     if (paramDatagramPacket == null)
/* 106 */       throw new NullPointerException("packet");
/* 107 */     if (paramDatagramPacket.getData() == null) {
/* 108 */       throw new NullPointerException("packet buffer");
/*     */     }
/* 110 */     return socketReceiveOrPeekData(i, paramDatagramPacket, this.timeout, this.connected, true);
/*     */   }
/*     */ 
/*     */   protected synchronized void receive0(DatagramPacket paramDatagramPacket) throws IOException {
/* 114 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 116 */     if (paramDatagramPacket == null)
/* 117 */       throw new NullPointerException("packet");
/* 118 */     if (paramDatagramPacket.getData() == null) {
/* 119 */       throw new NullPointerException("packet buffer");
/*     */     }
/* 121 */     socketReceiveOrPeekData(i, paramDatagramPacket, this.timeout, this.connected, false);
/*     */   }
/*     */ 
/*     */   protected void send(DatagramPacket paramDatagramPacket) throws IOException {
/* 125 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 127 */     if (paramDatagramPacket == null) {
/* 128 */       throw new NullPointerException("null packet");
/*     */     }
/* 130 */     if ((paramDatagramPacket.getAddress() == null) || (paramDatagramPacket.getData() == null)) {
/* 131 */       throw new NullPointerException("null address || null buffer");
/*     */     }
/* 133 */     socketSend(i, paramDatagramPacket.getData(), paramDatagramPacket.getOffset(), paramDatagramPacket.getLength(), paramDatagramPacket.getAddress(), paramDatagramPacket.getPort(), this.connected);
/*     */   }
/*     */ 
/*     */   protected void connect0(InetAddress paramInetAddress, int paramInt) throws SocketException
/*     */   {
/* 138 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 140 */     if (paramInetAddress == null) {
/* 141 */       throw new NullPointerException("address");
/*     */     }
/* 143 */     socketConnect(i, paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   protected void disconnect0(int paramInt) {
/* 147 */     if ((this.fd == null) || (!this.fd.valid())) {
/* 148 */       return;
/*     */     }
/* 150 */     socketDisconnect(fdAccess.get(this.fd));
/*     */   }
/*     */ 
/*     */   protected void datagramSocketClose() {
/* 154 */     if ((this.fd == null) || (!this.fd.valid())) {
/* 155 */       return;
/*     */     }
/* 157 */     socketClose(fdAccess.get(this.fd));
/* 158 */     fdAccess.set(this.fd, -1);
/*     */   }
/*     */ 
/*     */   protected void socketSetOption(int paramInt, Object paramObject) throws SocketException
/*     */   {
/* 163 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 165 */     int j = 0;
/*     */ 
/* 167 */     switch (paramInt) {
/*     */     case 3:
/*     */     case 4097:
/*     */     case 4098:
/* 171 */       j = ((Integer)paramObject).intValue();
/* 172 */       break;
/*     */     case 4:
/* 174 */       if ((this.exclusiveBind) && (this.localPort != 0))
/*     */       {
/* 176 */         this.reuseAddressEmulated = true;
/* 177 */         this.isReuseAddress = ((Boolean)paramObject).booleanValue();
/* 178 */         return;
/*     */       }
/*     */ 
/*     */     case 32:
/* 182 */       j = ((Boolean)paramObject).booleanValue() ? 1 : 0;
/* 183 */       break;
/*     */     }
/* 185 */     throw new SocketException("Option not supported");
/*     */ 
/* 188 */     socketSetIntOption(i, paramInt, j);
/*     */   }
/*     */ 
/*     */   protected Object socketGetOption(int paramInt) throws SocketException {
/* 192 */     int i = checkAndReturnNativeFD();
/*     */ 
/* 195 */     if (paramInt == 15) {
/* 196 */       return socketLocalAddress(i);
/*     */     }
/* 198 */     if ((paramInt == 4) && (this.reuseAddressEmulated)) {
/* 199 */       return Boolean.valueOf(this.isReuseAddress);
/*     */     }
/* 201 */     int j = socketGetIntOption(i, paramInt);
/* 202 */     Object localObject = null;
/*     */ 
/* 204 */     switch (paramInt) {
/*     */     case 4:
/*     */     case 32:
/* 207 */       localObject = j == 0 ? Boolean.FALSE : Boolean.TRUE;
/* 208 */       break;
/*     */     case 3:
/*     */     case 4097:
/*     */     case 4098:
/* 212 */       localObject = new Integer(j);
/* 213 */       break;
/*     */     default:
/* 215 */       throw new SocketException("Option not supported");
/*     */     }
/*     */ 
/* 218 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected void join(InetAddress paramInetAddress, NetworkInterface paramNetworkInterface)
/*     */     throws IOException
/*     */   {
/* 228 */     throw new IOException("Method not implemented!");
/*     */   }
/*     */ 
/*     */   protected void leave(InetAddress paramInetAddress, NetworkInterface paramNetworkInterface) throws IOException
/*     */   {
/* 233 */     throw new IOException("Method not implemented!");
/*     */   }
/*     */ 
/*     */   protected void setTimeToLive(int paramInt) throws IOException {
/* 237 */     throw new IOException("Method not implemented!");
/*     */   }
/*     */ 
/*     */   protected int getTimeToLive() throws IOException {
/* 241 */     throw new IOException("Method not implemented!");
/*     */   }
/*     */ 
/*     */   protected void setTTL(byte paramByte) throws IOException
/*     */   {
/* 246 */     throw new IOException("Method not implemented!");
/*     */   }
/*     */ 
/*     */   protected byte getTTL() throws IOException {
/* 250 */     throw new IOException("Method not implemented!");
/*     */   }
/*     */ 
/*     */   private int checkAndReturnNativeFD() throws SocketException
/*     */   {
/* 255 */     if ((this.fd == null) || (!this.fd.valid())) {
/* 256 */       throw new SocketException("Socket closed");
/*     */     }
/* 258 */     return fdAccess.get(this.fd);
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private static native int socketCreate(boolean paramBoolean);
/*     */ 
/*     */   private static native void socketBind(int paramInt1, InetAddress paramInetAddress, int paramInt2, boolean paramBoolean)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native void socketConnect(int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native void socketDisconnect(int paramInt);
/*     */ 
/*     */   private static native void socketClose(int paramInt);
/*     */ 
/*     */   private static native int socketLocalPort(int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native Object socketLocalAddress(int paramInt)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native int socketReceiveOrPeekData(int paramInt1, DatagramPacket paramDatagramPacket, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void socketSend(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, InetAddress paramInetAddress, int paramInt4, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void socketSetIntOption(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws SocketException;
/*     */ 
/*     */   private static native int socketGetIntOption(int paramInt1, int paramInt2)
/*     */     throws SocketException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.DualStackPlainDatagramSocketImpl
 * JD-Core Version:    0.6.2
 */