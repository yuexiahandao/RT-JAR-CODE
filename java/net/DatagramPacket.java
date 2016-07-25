/*     */ package java.net;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public final class DatagramPacket
/*     */ {
/*     */   byte[] buf;
/*     */   int offset;
/*     */   int length;
/*     */   int bufLength;
/*     */   InetAddress address;
/*     */   int port;
/*     */ 
/*     */   public DatagramPacket(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  79 */     setData(paramArrayOfByte, paramInt1, paramInt2);
/*  80 */     this.address = null;
/*  81 */     this.port = -1;
/*     */   }
/*     */ 
/*     */   public DatagramPacket(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  95 */     this(paramArrayOfByte, 0, paramInt);
/*     */   }
/*     */ 
/*     */   public DatagramPacket(byte[] paramArrayOfByte, int paramInt1, int paramInt2, InetAddress paramInetAddress, int paramInt3)
/*     */   {
/* 116 */     setData(paramArrayOfByte, paramInt1, paramInt2);
/* 117 */     setAddress(paramInetAddress);
/* 118 */     setPort(paramInt3);
/*     */   }
/*     */ 
/*     */   public DatagramPacket(byte[] paramArrayOfByte, int paramInt1, int paramInt2, SocketAddress paramSocketAddress)
/*     */     throws SocketException
/*     */   {
/* 139 */     setData(paramArrayOfByte, paramInt1, paramInt2);
/* 140 */     setSocketAddress(paramSocketAddress);
/*     */   }
/*     */ 
/*     */   public DatagramPacket(byte[] paramArrayOfByte, int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*     */   {
/* 157 */     this(paramArrayOfByte, 0, paramInt1, paramInetAddress, paramInt2);
/*     */   }
/*     */ 
/*     */   public DatagramPacket(byte[] paramArrayOfByte, int paramInt, SocketAddress paramSocketAddress)
/*     */     throws SocketException
/*     */   {
/* 175 */     this(paramArrayOfByte, 0, paramInt, paramSocketAddress);
/*     */   }
/*     */ 
/*     */   public synchronized InetAddress getAddress()
/*     */   {
/* 188 */     return this.address;
/*     */   }
/*     */ 
/*     */   public synchronized int getPort()
/*     */   {
/* 200 */     return this.port;
/*     */   }
/*     */ 
/*     */   public synchronized byte[] getData()
/*     */   {
/* 212 */     return this.buf;
/*     */   }
/*     */ 
/*     */   public synchronized int getOffset()
/*     */   {
/* 225 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public synchronized int getLength()
/*     */   {
/* 237 */     return this.length;
/*     */   }
/*     */ 
/*     */   public synchronized void setData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 261 */     if ((paramInt2 < 0) || (paramInt1 < 0) || (paramInt2 + paramInt1 < 0) || (paramInt2 + paramInt1 > paramArrayOfByte.length))
/*     */     {
/* 264 */       throw new IllegalArgumentException("illegal length or offset");
/*     */     }
/* 266 */     this.buf = paramArrayOfByte;
/* 267 */     this.length = paramInt2;
/* 268 */     this.bufLength = paramInt2;
/* 269 */     this.offset = paramInt1;
/*     */   }
/*     */ 
/*     */   public synchronized void setAddress(InetAddress paramInetAddress)
/*     */   {
/* 280 */     this.address = paramInetAddress;
/*     */   }
/*     */ 
/*     */   public synchronized void setPort(int paramInt)
/*     */   {
/* 291 */     if ((paramInt < 0) || (paramInt > 65535)) {
/* 292 */       throw new IllegalArgumentException("Port out of range:" + paramInt);
/*     */     }
/* 294 */     this.port = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized void setSocketAddress(SocketAddress paramSocketAddress)
/*     */   {
/* 309 */     if ((paramSocketAddress == null) || (!(paramSocketAddress instanceof InetSocketAddress)))
/* 310 */       throw new IllegalArgumentException("unsupported address type");
/* 311 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/* 312 */     if (localInetSocketAddress.isUnresolved())
/* 313 */       throw new IllegalArgumentException("unresolved address");
/* 314 */     setAddress(localInetSocketAddress.getAddress());
/* 315 */     setPort(localInetSocketAddress.getPort());
/*     */   }
/*     */ 
/*     */   public synchronized SocketAddress getSocketAddress()
/*     */   {
/* 327 */     return new InetSocketAddress(getAddress(), getPort());
/*     */   }
/*     */ 
/*     */   public synchronized void setData(byte[] paramArrayOfByte)
/*     */   {
/* 345 */     if (paramArrayOfByte == null) {
/* 346 */       throw new NullPointerException("null packet buffer");
/*     */     }
/* 348 */     this.buf = paramArrayOfByte;
/* 349 */     this.offset = 0;
/* 350 */     this.length = paramArrayOfByte.length;
/* 351 */     this.bufLength = paramArrayOfByte.length;
/*     */   }
/*     */ 
/*     */   public synchronized void setLength(int paramInt)
/*     */   {
/* 373 */     if ((paramInt + this.offset > this.buf.length) || (paramInt < 0) || (paramInt + this.offset < 0))
/*     */     {
/* 375 */       throw new IllegalArgumentException("illegal length");
/*     */     }
/* 377 */     this.length = paramInt;
/* 378 */     this.bufLength = this.length;
/*     */   }
/*     */ 
/*     */   private static native void init();
/*     */ 
/*     */   static
/*     */   {
/*  49 */     AccessController.doPrivileged(new LoadLibraryAction("net"));
/*     */ 
/*  51 */     init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.DatagramPacket
 * JD-Core Version:    0.6.2
 */