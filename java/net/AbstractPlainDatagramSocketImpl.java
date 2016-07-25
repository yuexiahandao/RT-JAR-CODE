/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import sun.net.ResourceManager;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ abstract class AbstractPlainDatagramSocketImpl extends DatagramSocketImpl
/*     */ {
/*  47 */   int timeout = 0;
/*  48 */   boolean connected = false;
/*  49 */   private int trafficClass = 0;
/*  50 */   private InetAddress connectedAddress = null;
/*  51 */   private int connectedPort = -1;
/*     */ 
/*  54 */   private int multicastInterface = 0;
/*  55 */   private boolean loopbackMode = true;
/*  56 */   private int ttl = -1;
/*     */ 
/*  58 */   private static final String os = (String)AccessController.doPrivileged(new GetPropertyAction("os.name"));
/*     */ 
/*  65 */   private static final boolean connectDisabled = os.contains("OS X");
/*     */ 
/*     */   protected synchronized void create()
/*     */     throws SocketException
/*     */   {
/*  80 */     ResourceManager.beforeUdpCreate();
/*  81 */     this.fd = new FileDescriptor();
/*     */     try {
/*  83 */       datagramSocketCreate();
/*     */     } catch (SocketException localSocketException) {
/*  85 */       ResourceManager.afterUdpClose();
/*  86 */       this.fd = null;
/*  87 */       throw localSocketException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected synchronized void bind(int paramInt, InetAddress paramInetAddress)
/*     */     throws SocketException
/*     */   {
/*  96 */     bind0(paramInt, paramInetAddress);
/*     */   }
/*     */ 
/*     */   protected abstract void bind0(int paramInt, InetAddress paramInetAddress)
/*     */     throws SocketException;
/*     */ 
/*     */   protected abstract void send(DatagramPacket paramDatagramPacket)
/*     */     throws IOException;
/*     */ 
/*     */   protected void connect(InetAddress paramInetAddress, int paramInt)
/*     */     throws SocketException
/*     */   {
/* 117 */     connect0(paramInetAddress, paramInt);
/* 118 */     this.connectedAddress = paramInetAddress;
/* 119 */     this.connectedPort = paramInt;
/* 120 */     this.connected = true;
/*     */   }
/*     */ 
/*     */   protected void disconnect()
/*     */   {
/* 128 */     disconnect0(this.connectedAddress.holder().getFamily());
/* 129 */     this.connected = false;
/* 130 */     this.connectedAddress = null;
/* 131 */     this.connectedPort = -1;
/*     */   }
/*     */ 
/*     */   protected abstract int peek(InetAddress paramInetAddress)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract int peekData(DatagramPacket paramDatagramPacket)
/*     */     throws IOException;
/*     */ 
/*     */   protected synchronized void receive(DatagramPacket paramDatagramPacket)
/*     */     throws IOException
/*     */   {
/* 146 */     receive0(paramDatagramPacket);
/*     */   }
/*     */ 
/*     */   protected abstract void receive0(DatagramPacket paramDatagramPacket)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void setTimeToLive(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract int getTimeToLive()
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void setTTL(byte paramByte)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract byte getTTL()
/*     */     throws IOException;
/*     */ 
/*     */   protected void join(InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/* 179 */     join(paramInetAddress, null);
/*     */   }
/*     */ 
/*     */   protected void leave(InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/* 187 */     leave(paramInetAddress, null);
/*     */   }
/*     */ 
/*     */   protected void joinGroup(SocketAddress paramSocketAddress, NetworkInterface paramNetworkInterface)
/*     */     throws IOException
/*     */   {
/* 201 */     if ((paramSocketAddress == null) || (!(paramSocketAddress instanceof InetSocketAddress)))
/* 202 */       throw new IllegalArgumentException("Unsupported address type");
/* 203 */     join(((InetSocketAddress)paramSocketAddress).getAddress(), paramNetworkInterface);
/*     */   }
/*     */ 
/*     */   protected abstract void join(InetAddress paramInetAddress, NetworkInterface paramNetworkInterface)
/*     */     throws IOException;
/*     */ 
/*     */   protected void leaveGroup(SocketAddress paramSocketAddress, NetworkInterface paramNetworkInterface)
/*     */     throws IOException
/*     */   {
/* 219 */     if ((paramSocketAddress == null) || (!(paramSocketAddress instanceof InetSocketAddress)))
/* 220 */       throw new IllegalArgumentException("Unsupported address type");
/* 221 */     leave(((InetSocketAddress)paramSocketAddress).getAddress(), paramNetworkInterface);
/*     */   }
/*     */ 
/*     */   protected abstract void leave(InetAddress paramInetAddress, NetworkInterface paramNetworkInterface)
/*     */     throws IOException;
/*     */ 
/*     */   protected void close()
/*     */   {
/* 231 */     if (this.fd != null) {
/* 232 */       datagramSocketClose();
/* 233 */       ResourceManager.afterUdpClose();
/* 234 */       this.fd = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isClosed() {
/* 239 */     return this.fd == null;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/* 243 */     close();
/*     */   }
/*     */ 
/*     */   public void setOption(int paramInt, Object paramObject)
/*     */     throws SocketException
/*     */   {
/* 252 */     if (isClosed()) {
/* 253 */       throw new SocketException("Socket Closed");
/*     */     }
/* 255 */     switch (paramInt)
/*     */     {
/*     */     case 4102:
/* 261 */       if ((paramObject == null) || (!(paramObject instanceof Integer))) {
/* 262 */         throw new SocketException("bad argument for SO_TIMEOUT");
/*     */       }
/* 264 */       int i = ((Integer)paramObject).intValue();
/* 265 */       if (i < 0)
/* 266 */         throw new IllegalArgumentException("timeout < 0");
/* 267 */       this.timeout = i;
/* 268 */       return;
/*     */     case 3:
/* 270 */       if ((paramObject == null) || (!(paramObject instanceof Integer))) {
/* 271 */         throw new SocketException("bad argument for IP_TOS");
/*     */       }
/* 273 */       this.trafficClass = ((Integer)paramObject).intValue();
/* 274 */       break;
/*     */     case 4:
/* 276 */       if ((paramObject == null) || (!(paramObject instanceof Boolean))) {
/* 277 */         throw new SocketException("bad argument for SO_REUSEADDR");
/*     */       }
/*     */       break;
/*     */     case 32:
/* 281 */       if ((paramObject == null) || (!(paramObject instanceof Boolean))) {
/* 282 */         throw new SocketException("bad argument for SO_BROADCAST");
/*     */       }
/*     */       break;
/*     */     case 15:
/* 286 */       throw new SocketException("Cannot re-bind Socket");
/*     */     case 4097:
/*     */     case 4098:
/* 289 */       if ((paramObject == null) || (!(paramObject instanceof Integer)) || (((Integer)paramObject).intValue() < 0))
/*     */       {
/* 291 */         throw new SocketException("bad argument for SO_SNDBUF or SO_RCVBUF");
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 16:
/* 296 */       if ((paramObject == null) || (!(paramObject instanceof InetAddress)))
/* 297 */         throw new SocketException("bad argument for IP_MULTICAST_IF");
/*     */       break;
/*     */     case 31:
/* 300 */       if ((paramObject == null) || (!(paramObject instanceof NetworkInterface)))
/* 301 */         throw new SocketException("bad argument for IP_MULTICAST_IF2");
/*     */       break;
/*     */     case 18:
/* 304 */       if ((paramObject == null) || (!(paramObject instanceof Boolean)))
/* 305 */         throw new SocketException("bad argument for IP_MULTICAST_LOOP");
/*     */       break;
/*     */     default:
/* 308 */       throw new SocketException("invalid option: " + paramInt);
/*     */     }
/* 310 */     socketSetOption(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   public Object getOption(int paramInt)
/*     */     throws SocketException
/*     */   {
/* 318 */     if (isClosed())
/* 319 */       throw new SocketException("Socket Closed");
/*     */     Object localObject;
/* 324 */     switch (paramInt) {
/*     */     case 4102:
/* 326 */       localObject = new Integer(this.timeout);
/* 327 */       break;
/*     */     case 3:
/* 330 */       localObject = socketGetOption(paramInt);
/* 331 */       if (((Integer)localObject).intValue() == -1)
/* 332 */         localObject = new Integer(this.trafficClass); break;
/*     */     case 4:
/*     */     case 15:
/*     */     case 16:
/*     */     case 18:
/*     */     case 31:
/*     */     case 32:
/*     */     case 4097:
/*     */     case 4098:
/* 344 */       localObject = socketGetOption(paramInt);
/* 345 */       break;
/*     */     default:
/* 348 */       throw new SocketException("invalid option: " + paramInt);
/*     */     }
/*     */ 
/* 351 */     return localObject; } 
/*     */   protected abstract void datagramSocketCreate() throws SocketException;
/*     */ 
/*     */   protected abstract void datagramSocketClose();
/*     */ 
/*     */   protected abstract void socketSetOption(int paramInt, Object paramObject) throws SocketException;
/*     */ 
/*     */   protected abstract Object socketGetOption(int paramInt) throws SocketException;
/*     */ 
/*     */   protected abstract void connect0(InetAddress paramInetAddress, int paramInt) throws SocketException;
/*     */ 
/*     */   protected abstract void disconnect0(int paramInt);
/*     */ 
/* 364 */   protected boolean nativeConnectDisabled() { return connectDisabled; }
/*     */ 
/*     */ 
/*     */   native int dataAvailable();
/*     */ 
/*     */   private static native void init();
/*     */ 
/*     */   static
/*     */   {
/*  71 */     AccessController.doPrivileged(new LoadLibraryAction("net"));
/*     */ 
/*  73 */     init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.AbstractPlainDatagramSocketImpl
 * JD-Core Version:    0.6.2
 */