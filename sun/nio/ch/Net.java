/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.ProtocolFamily;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketOption;
/*     */ import java.net.StandardProtocolFamily;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.channels.AlreadyBoundException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.NotYetBoundException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.nio.channels.UnresolvedAddressException;
/*     */ import java.nio.channels.UnsupportedAddressTypeException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ class Net
/*     */ {
/*  42 */   static final ProtocolFamily UNSPEC = new ProtocolFamily() {
/*     */     public String name() {
/*  44 */       return "UNSPEC";
/*     */     }
/*  42 */   };
/*     */   private static boolean revealLocalAddress;
/*     */   private static volatile boolean propRevealLocalAddress;
/*     */   private static final boolean exclusiveBind;
/*     */   private static volatile boolean checkedIPv6;
/*     */   private static volatile boolean isIPv6Available;
/*     */   public static final int SHUT_RD = 0;
/*     */   public static final int SHUT_WR = 1;
/*     */   public static final int SHUT_RDWR = 2;
/*     */ 
/*     */   static boolean isIPv6Available()
/*     */   {
/*  91 */     if (!checkedIPv6) {
/*  92 */       isIPv6Available = isIPv6Available0();
/*  93 */       checkedIPv6 = true;
/*     */     }
/*  95 */     return isIPv6Available;
/*     */   }
/*     */ 
/*     */   static boolean useExclusiveBind()
/*     */   {
/* 102 */     return exclusiveBind;
/*     */   }
/*     */ 
/*     */   static boolean canIPv6SocketJoinIPv4Group()
/*     */   {
/* 109 */     return canIPv6SocketJoinIPv4Group0();
/*     */   }
/*     */ 
/*     */   static boolean canJoin6WithIPv4Group()
/*     */   {
/* 117 */     return canJoin6WithIPv4Group0();
/*     */   }
/*     */ 
/*     */   static InetSocketAddress checkAddress(SocketAddress paramSocketAddress) {
/* 121 */     if (paramSocketAddress == null)
/* 122 */       throw new NullPointerException();
/* 123 */     if (!(paramSocketAddress instanceof InetSocketAddress))
/* 124 */       throw new UnsupportedAddressTypeException();
/* 125 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/* 126 */     if (localInetSocketAddress.isUnresolved())
/* 127 */       throw new UnresolvedAddressException();
/* 128 */     InetAddress localInetAddress = localInetSocketAddress.getAddress();
/* 129 */     if ((!(localInetAddress instanceof Inet4Address)) && (!(localInetAddress instanceof Inet6Address)))
/* 130 */       throw new IllegalArgumentException("Invalid address type");
/* 131 */     return localInetSocketAddress;
/*     */   }
/*     */ 
/*     */   static InetSocketAddress asInetSocketAddress(SocketAddress paramSocketAddress) {
/* 135 */     if (!(paramSocketAddress instanceof InetSocketAddress))
/* 136 */       throw new UnsupportedAddressTypeException();
/* 137 */     return (InetSocketAddress)paramSocketAddress;
/*     */   }
/*     */ 
/*     */   static void translateToSocketException(Exception paramException)
/*     */     throws SocketException
/*     */   {
/* 143 */     if ((paramException instanceof SocketException))
/* 144 */       throw ((SocketException)paramException);
/* 145 */     Object localObject = paramException;
/* 146 */     if ((paramException instanceof ClosedChannelException))
/* 147 */       localObject = new SocketException("Socket is closed");
/* 148 */     else if ((paramException instanceof NotYetConnectedException))
/* 149 */       localObject = new SocketException("Socket is not connected");
/* 150 */     else if ((paramException instanceof AlreadyBoundException))
/* 151 */       localObject = new SocketException("Already bound");
/* 152 */     else if ((paramException instanceof NotYetBoundException))
/* 153 */       localObject = new SocketException("Socket is not bound yet");
/* 154 */     else if ((paramException instanceof UnsupportedAddressTypeException))
/* 155 */       localObject = new SocketException("Unsupported address type");
/* 156 */     else if ((paramException instanceof UnresolvedAddressException)) {
/* 157 */       localObject = new SocketException("Unresolved address");
/*     */     }
/* 159 */     if (localObject != paramException) {
/* 160 */       ((Exception)localObject).initCause(paramException);
/*     */     }
/* 162 */     if ((localObject instanceof SocketException))
/* 163 */       throw ((SocketException)localObject);
/* 164 */     if ((localObject instanceof RuntimeException)) {
/* 165 */       throw ((RuntimeException)localObject);
/*     */     }
/* 167 */     throw new Error("Untranslated exception", (Throwable)localObject);
/*     */   }
/*     */ 
/*     */   static void translateException(Exception paramException, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 174 */     if ((paramException instanceof IOException)) {
/* 175 */       throw ((IOException)paramException);
/*     */     }
/*     */ 
/* 178 */     if ((paramBoolean) && ((paramException instanceof UnresolvedAddressException)))
/*     */     {
/* 181 */       throw new UnknownHostException();
/*     */     }
/* 183 */     translateToSocketException(paramException);
/*     */   }
/*     */ 
/*     */   static void translateException(Exception paramException)
/*     */     throws IOException
/*     */   {
/* 189 */     translateException(paramException, false);
/*     */   }
/*     */ 
/*     */   static InetSocketAddress getRevealedLocalAddress(InetSocketAddress paramInetSocketAddress)
/*     */   {
/* 196 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 197 */     if ((paramInetSocketAddress == null) || (localSecurityManager == null)) {
/* 198 */       return paramInetSocketAddress;
/*     */     }
/* 200 */     if (!getRevealLocalAddress()) {
/*     */       try
/*     */       {
/* 203 */         localSecurityManager.checkConnect(paramInetSocketAddress.getAddress().getHostAddress(), -1);
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/* 207 */         paramInetSocketAddress = getLoopbackAddress(paramInetSocketAddress.getPort());
/*     */       }
/*     */     }
/* 210 */     return paramInetSocketAddress;
/*     */   }
/*     */ 
/*     */   static String getRevealedLocalAddressAsString(InetSocketAddress paramInetSocketAddress) {
/* 214 */     if ((!getRevealLocalAddress()) && (System.getSecurityManager() != null))
/* 215 */       paramInetSocketAddress = getLoopbackAddress(paramInetSocketAddress.getPort());
/* 216 */     return paramInetSocketAddress.toString();
/*     */   }
/*     */ 
/*     */   private static boolean getRevealLocalAddress() {
/* 220 */     if (!propRevealLocalAddress) {
/*     */       try {
/* 222 */         revealLocalAddress = Boolean.parseBoolean((String)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public String run()
/*     */           {
/* 226 */             return System.getProperty("jdk.net.revealLocalAddress");
/*     */           }
/*     */         }));
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */ 
/* 234 */       propRevealLocalAddress = true;
/*     */     }
/* 236 */     return revealLocalAddress;
/*     */   }
/*     */ 
/*     */   private static InetSocketAddress getLoopbackAddress(int paramInt) {
/* 240 */     return new InetSocketAddress(InetAddress.getLoopbackAddress(), paramInt);
/*     */   }
/*     */ 
/*     */   static Inet4Address anyInet4Address(NetworkInterface paramNetworkInterface)
/*     */   {
/* 249 */     return (Inet4Address)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Inet4Address run() {
/* 251 */         Enumeration localEnumeration = this.val$interf.getInetAddresses();
/* 252 */         while (localEnumeration.hasMoreElements()) {
/* 253 */           InetAddress localInetAddress = (InetAddress)localEnumeration.nextElement();
/* 254 */           if ((localInetAddress instanceof Inet4Address)) {
/* 255 */             return (Inet4Address)localInetAddress;
/*     */           }
/*     */         }
/* 258 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static int inet4AsInt(InetAddress paramInetAddress)
/*     */   {
/* 267 */     if ((paramInetAddress instanceof Inet4Address)) {
/* 268 */       byte[] arrayOfByte = paramInetAddress.getAddress();
/* 269 */       int i = arrayOfByte[3] & 0xFF;
/* 270 */       i |= arrayOfByte[2] << 8 & 0xFF00;
/* 271 */       i |= arrayOfByte[1] << 16 & 0xFF0000;
/* 272 */       i |= arrayOfByte[0] << 24 & 0xFF000000;
/* 273 */       return i;
/*     */     }
/* 275 */     throw new AssertionError("Should not reach here");
/*     */   }
/*     */ 
/*     */   static InetAddress inet4FromInt(int paramInt)
/*     */   {
/* 283 */     byte[] arrayOfByte = new byte[4];
/* 284 */     arrayOfByte[0] = ((byte)(paramInt >>> 24 & 0xFF));
/* 285 */     arrayOfByte[1] = ((byte)(paramInt >>> 16 & 0xFF));
/* 286 */     arrayOfByte[2] = ((byte)(paramInt >>> 8 & 0xFF));
/* 287 */     arrayOfByte[3] = ((byte)(paramInt & 0xFF));
/*     */     try {
/* 289 */       return InetAddress.getByAddress(arrayOfByte); } catch (UnknownHostException localUnknownHostException) {
/*     */     }
/* 291 */     throw new AssertionError("Should not reach here");
/*     */   }
/*     */ 
/*     */   static byte[] inet6AsByteArray(InetAddress paramInetAddress)
/*     */   {
/* 299 */     if ((paramInetAddress instanceof Inet6Address)) {
/* 300 */       return paramInetAddress.getAddress();
/*     */     }
/*     */ 
/* 304 */     if ((paramInetAddress instanceof Inet4Address)) {
/* 305 */       byte[] arrayOfByte1 = paramInetAddress.getAddress();
/* 306 */       byte[] arrayOfByte2 = new byte[16];
/* 307 */       arrayOfByte2[10] = -1;
/* 308 */       arrayOfByte2[11] = -1;
/* 309 */       arrayOfByte2[12] = arrayOfByte1[0];
/* 310 */       arrayOfByte2[13] = arrayOfByte1[1];
/* 311 */       arrayOfByte2[14] = arrayOfByte1[2];
/* 312 */       arrayOfByte2[15] = arrayOfByte1[3];
/* 313 */       return arrayOfByte2;
/*     */     }
/*     */ 
/* 316 */     throw new AssertionError("Should not reach here");
/*     */   }
/*     */ 
/*     */   static void setSocketOption(FileDescriptor paramFileDescriptor, ProtocolFamily paramProtocolFamily, SocketOption<?> paramSocketOption, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 325 */     if (paramObject == null) {
/* 326 */       throw new IllegalArgumentException("Invalid option value");
/*     */     }
/*     */ 
/* 329 */     Class localClass = paramSocketOption.type();
/* 330 */     if ((localClass != Integer.class) && (localClass != Boolean.class))
/* 331 */       throw new AssertionError("Should not reach here");
/*     */     int i;
/* 334 */     if ((paramSocketOption == StandardSocketOptions.SO_RCVBUF) || (paramSocketOption == StandardSocketOptions.SO_SNDBUF))
/*     */     {
/* 337 */       i = ((Integer)paramObject).intValue();
/* 338 */       if (i < 0)
/* 339 */         throw new IllegalArgumentException("Invalid send/receive buffer size");
/*     */     }
/* 341 */     if (paramSocketOption == StandardSocketOptions.SO_LINGER) {
/* 342 */       i = ((Integer)paramObject).intValue();
/* 343 */       if (i < 0)
/* 344 */         paramObject = Integer.valueOf(-1);
/* 345 */       if (i > 65535)
/* 346 */         paramObject = Integer.valueOf(65535);
/*     */     }
/* 348 */     if (paramSocketOption == StandardSocketOptions.IP_TOS) {
/* 349 */       i = ((Integer)paramObject).intValue();
/* 350 */       if ((i < 0) || (i > 255))
/* 351 */         throw new IllegalArgumentException("Invalid IP_TOS value");
/*     */     }
/* 353 */     if (paramSocketOption == StandardSocketOptions.IP_MULTICAST_TTL) {
/* 354 */       i = ((Integer)paramObject).intValue();
/* 355 */       if ((i < 0) || (i > 255)) {
/* 356 */         throw new IllegalArgumentException("Invalid TTL/hop value");
/*     */       }
/*     */     }
/*     */ 
/* 360 */     OptionKey localOptionKey = SocketOptionRegistry.findOption(paramSocketOption, paramProtocolFamily);
/* 361 */     if (localOptionKey == null)
/* 362 */       throw new AssertionError("Option not found");
/*     */     int j;
/* 365 */     if (localClass == Integer.class) {
/* 366 */       j = ((Integer)paramObject).intValue();
/*     */     } else {
/* 368 */       bool = ((Boolean)paramObject).booleanValue();
/* 369 */       j = bool ? 1 : 0;
/*     */     }
/*     */ 
/* 372 */     boolean bool = paramProtocolFamily == UNSPEC;
/* 373 */     setIntOption0(paramFileDescriptor, bool, localOptionKey.level(), localOptionKey.name(), j);
/*     */   }
/*     */ 
/*     */   static Object getSocketOption(FileDescriptor paramFileDescriptor, ProtocolFamily paramProtocolFamily, SocketOption<?> paramSocketOption)
/*     */     throws IOException
/*     */   {
/* 380 */     Class localClass = paramSocketOption.type();
/*     */ 
/* 383 */     if ((localClass != Integer.class) && (localClass != Boolean.class)) {
/* 384 */       throw new AssertionError("Should not reach here");
/*     */     }
/*     */ 
/* 387 */     OptionKey localOptionKey = SocketOptionRegistry.findOption(paramSocketOption, paramProtocolFamily);
/* 388 */     if (localOptionKey == null) {
/* 389 */       throw new AssertionError("Option not found");
/*     */     }
/* 391 */     boolean bool = paramProtocolFamily == UNSPEC;
/* 392 */     int i = getIntOption0(paramFileDescriptor, bool, localOptionKey.level(), localOptionKey.name());
/*     */ 
/* 394 */     if (localClass == Integer.class) {
/* 395 */       return Integer.valueOf(i);
/*     */     }
/* 397 */     return i == 0 ? Boolean.FALSE : Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   private static native boolean isIPv6Available0();
/*     */ 
/*     */   private static native int isExclusiveBindAvailable();
/*     */ 
/*     */   private static native boolean canIPv6SocketJoinIPv4Group0();
/*     */ 
/*     */   private static native boolean canJoin6WithIPv4Group0();
/*     */ 
/*     */   static FileDescriptor socket(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 416 */     return socket(UNSPEC, paramBoolean);
/*     */   }
/*     */ 
/*     */   static FileDescriptor socket(ProtocolFamily paramProtocolFamily, boolean paramBoolean) throws IOException
/*     */   {
/* 421 */     boolean bool = (isIPv6Available()) && (paramProtocolFamily != StandardProtocolFamily.INET);
/*     */ 
/* 423 */     return IOUtil.newFD(socket0(bool, paramBoolean, false));
/*     */   }
/*     */ 
/*     */   static FileDescriptor serverSocket(boolean paramBoolean) {
/* 427 */     return IOUtil.newFD(socket0(isIPv6Available(), paramBoolean, true));
/*     */   }
/*     */ 
/*     */   private static native int socket0(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);
/*     */ 
/*     */   static void bind(FileDescriptor paramFileDescriptor, InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 436 */     bind(UNSPEC, paramFileDescriptor, paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   static void bind(ProtocolFamily paramProtocolFamily, FileDescriptor paramFileDescriptor, InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 442 */     boolean bool = (isIPv6Available()) && (paramProtocolFamily != StandardProtocolFamily.INET);
/*     */ 
/* 444 */     bind0(paramFileDescriptor, bool, exclusiveBind, paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   private static native void bind0(FileDescriptor paramFileDescriptor, boolean paramBoolean1, boolean paramBoolean2, InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static native void listen(FileDescriptor paramFileDescriptor, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static int connect(FileDescriptor paramFileDescriptor, InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 457 */     return connect(UNSPEC, paramFileDescriptor, paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   static int connect(ProtocolFamily paramProtocolFamily, FileDescriptor paramFileDescriptor, InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 463 */     boolean bool = (isIPv6Available()) && (paramProtocolFamily != StandardProtocolFamily.INET);
/*     */ 
/* 465 */     return connect0(bool, paramFileDescriptor, paramInetAddress, paramInt);
/*     */   }
/*     */ 
/*     */   private static native int connect0(boolean paramBoolean, FileDescriptor paramFileDescriptor, InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static native void shutdown(FileDescriptor paramFileDescriptor, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   private static native int localPort(FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   private static native InetAddress localInetAddress(FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   static InetSocketAddress localAddress(FileDescriptor paramFileDescriptor)
/*     */     throws IOException
/*     */   {
/* 490 */     return new InetSocketAddress(localInetAddress(paramFileDescriptor), localPort(paramFileDescriptor));
/*     */   }
/*     */ 
/*     */   private static native int remotePort(FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   private static native InetAddress remoteInetAddress(FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   static InetSocketAddress remoteAddress(FileDescriptor paramFileDescriptor)
/*     */     throws IOException
/*     */   {
/* 502 */     return new InetSocketAddress(remoteInetAddress(paramFileDescriptor), remotePort(paramFileDescriptor));
/*     */   }
/*     */ 
/*     */   private static native int getIntOption0(FileDescriptor paramFileDescriptor, boolean paramBoolean, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void setIntOption0(FileDescriptor paramFileDescriptor, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException;
/*     */ 
/*     */   static int join4(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException
/*     */   {
/* 522 */     return joinOrDrop4(true, paramFileDescriptor, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   static void drop4(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException
/*     */   {
/* 531 */     joinOrDrop4(false, paramFileDescriptor, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   private static native int joinOrDrop4(boolean paramBoolean, FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException;
/*     */ 
/*     */   static int block4(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException
/*     */   {
/* 543 */     return blockOrUnblock4(true, paramFileDescriptor, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   static void unblock4(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException
/*     */   {
/* 552 */     blockOrUnblock4(false, paramFileDescriptor, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   private static native int blockOrUnblock4(boolean paramBoolean, FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException;
/*     */ 
/*     */   static int join6(FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
/*     */     throws IOException
/*     */   {
/* 565 */     return joinOrDrop6(true, paramFileDescriptor, paramArrayOfByte1, paramInt, paramArrayOfByte2);
/*     */   }
/*     */ 
/*     */   static void drop6(FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
/*     */     throws IOException
/*     */   {
/* 574 */     joinOrDrop6(false, paramFileDescriptor, paramArrayOfByte1, paramInt, paramArrayOfByte2);
/*     */   }
/*     */ 
/*     */   private static native int joinOrDrop6(boolean paramBoolean, FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
/*     */     throws IOException;
/*     */ 
/*     */   static int block6(FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
/*     */     throws IOException
/*     */   {
/* 586 */     return blockOrUnblock6(true, paramFileDescriptor, paramArrayOfByte1, paramInt, paramArrayOfByte2);
/*     */   }
/*     */ 
/*     */   static void unblock6(FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
/*     */     throws IOException
/*     */   {
/* 595 */     blockOrUnblock6(false, paramFileDescriptor, paramArrayOfByte1, paramInt, paramArrayOfByte2);
/*     */   }
/*     */ 
/*     */   static native int blockOrUnblock6(boolean paramBoolean, FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
/*     */     throws IOException;
/*     */ 
/*     */   static native void setInterface4(FileDescriptor paramFileDescriptor, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static native int getInterface4(FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   static native void setInterface6(FileDescriptor paramFileDescriptor, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static native int getInterface6(FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/*  58 */     int i = isExclusiveBindAvailable();
/*  59 */     if (i >= 0) {
/*  60 */       String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run()
/*     */         {
/*  65 */           return System.getProperty("sun.net.useExclusiveBind");
/*     */         }
/*     */       });
/*  69 */       if (str != null) {
/*  70 */         exclusiveBind = str.length() == 0 ? true : Boolean.parseBoolean(str);
/*     */       }
/*  72 */       else if (i == 1)
/*  73 */         exclusiveBind = true;
/*     */       else
/*  75 */         exclusiveBind = false;
/*     */     }
/*     */     else {
/*  78 */       exclusiveBind = false;
/*     */     }
/*     */ 
/*  84 */     checkedIPv6 = false;
/*     */ 
/* 612 */     Util.load();
/* 613 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.Net
 * JD-Core Version:    0.6.2
 */