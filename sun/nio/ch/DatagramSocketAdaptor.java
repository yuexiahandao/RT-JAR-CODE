/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.DatagramSocketImpl;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketOption;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.nio.channels.IllegalBlockingModeException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class DatagramSocketAdaptor extends DatagramSocket
/*     */ {
/*     */   private final DatagramChannelImpl dc;
/*  49 */   private volatile int timeout = 0;
/*     */ 
/* 378 */   private static final DatagramSocketImpl dummyDatagramSocket = new DatagramSocketImpl() {
/*     */     protected void create() throws SocketException {
/*     */     }
/*     */     protected void bind(int paramAnonymousInt, InetAddress paramAnonymousInetAddress) throws SocketException {
/*     */     }
/*     */     protected void send(DatagramPacket paramAnonymousDatagramPacket) throws IOException {
/*     */     }
/*     */ 
/*     */     protected int peek(InetAddress paramAnonymousInetAddress) throws IOException {
/* 387 */       return 0;
/*     */     }
/* 389 */     protected int peekData(DatagramPacket paramAnonymousDatagramPacket) throws IOException { return 0; } 
/*     */     protected void receive(DatagramPacket paramAnonymousDatagramPacket) throws IOException {
/*     */     }
/*     */     protected void setTTL(byte paramAnonymousByte) throws IOException {
/*     */     }
/*     */     protected byte getTTL() throws IOException {
/* 395 */       return 0;
/*     */     }
/*     */     protected void setTimeToLive(int paramAnonymousInt) throws IOException {
/*     */     }
/* 399 */     protected int getTimeToLive() throws IOException { return 0; } 
/*     */     protected void join(InetAddress paramAnonymousInetAddress) throws IOException {
/*     */     }
/*     */     protected void leave(InetAddress paramAnonymousInetAddress) throws IOException {
/*     */     }
/*     */     protected void joinGroup(SocketAddress paramAnonymousSocketAddress, NetworkInterface paramAnonymousNetworkInterface) throws IOException {
/*     */     }
/*     */     protected void leaveGroup(SocketAddress paramAnonymousSocketAddress, NetworkInterface paramAnonymousNetworkInterface) throws IOException {
/*     */     }
/*     */ 
/*     */     protected void close() {
/*     */     }
/*     */ 
/*     */     public Object getOption(int paramAnonymousInt) throws SocketException {
/* 413 */       return null;
/*     */     }
/*     */ 
/*     */     public void setOption(int paramAnonymousInt, Object paramAnonymousObject)
/*     */       throws SocketException
/*     */     {
/*     */     }
/* 378 */   };
/*     */ 
/*     */   private DatagramSocketAdaptor(DatagramChannelImpl paramDatagramChannelImpl)
/*     */     throws IOException
/*     */   {
/*  57 */     super(dummyDatagramSocket);
/*  58 */     this.dc = paramDatagramChannelImpl;
/*     */   }
/*     */ 
/*     */   public static DatagramSocket create(DatagramChannelImpl paramDatagramChannelImpl) {
/*     */     try {
/*  63 */       return new DatagramSocketAdaptor(paramDatagramChannelImpl);
/*     */     } catch (IOException localIOException) {
/*  65 */       throw new Error(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void connectInternal(SocketAddress paramSocketAddress)
/*     */     throws SocketException
/*     */   {
/*  72 */     InetSocketAddress localInetSocketAddress = Net.asInetSocketAddress(paramSocketAddress);
/*  73 */     int i = localInetSocketAddress.getPort();
/*  74 */     if ((i < 0) || (i > 65535))
/*  75 */       throw new IllegalArgumentException("connect: " + i);
/*  76 */     if (paramSocketAddress == null)
/*  77 */       throw new IllegalArgumentException("connect: null address");
/*  78 */     if (isClosed())
/*  79 */       return;
/*     */     try {
/*  81 */       this.dc.connect(paramSocketAddress);
/*     */     } catch (Exception localException) {
/*  83 */       Net.translateToSocketException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(SocketAddress paramSocketAddress) throws SocketException {
/*     */     try {
/*  89 */       if (paramSocketAddress == null)
/*  90 */         paramSocketAddress = new InetSocketAddress(0);
/*  91 */       this.dc.bind(paramSocketAddress);
/*     */     } catch (Exception localException) {
/*  93 */       Net.translateToSocketException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void connect(InetAddress paramInetAddress, int paramInt) {
/*     */     try {
/*  99 */       connectInternal(new InetSocketAddress(paramInetAddress, paramInt));
/*     */     }
/*     */     catch (SocketException localSocketException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void connect(SocketAddress paramSocketAddress) throws SocketException {
/* 106 */     if (paramSocketAddress == null)
/* 107 */       throw new IllegalArgumentException("Address can't be null");
/* 108 */     connectInternal(paramSocketAddress);
/*     */   }
/*     */ 
/*     */   public void disconnect() {
/*     */     try {
/* 113 */       this.dc.disconnect();
/*     */     } catch (IOException localIOException) {
/* 115 */       throw new Error(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isBound() {
/* 120 */     return this.dc.localAddress() != null;
/*     */   }
/*     */ 
/*     */   public boolean isConnected() {
/* 124 */     return this.dc.remoteAddress() != null;
/*     */   }
/*     */ 
/*     */   public InetAddress getInetAddress() {
/* 128 */     return isConnected() ? Net.asInetSocketAddress(this.dc.remoteAddress()).getAddress() : null;
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 134 */     return isConnected() ? Net.asInetSocketAddress(this.dc.remoteAddress()).getPort() : -1;
/*     */   }
/*     */ 
/*     */   public void send(DatagramPacket paramDatagramPacket)
/*     */     throws IOException
/*     */   {
/* 140 */     synchronized (this.dc.blockingLock()) {
/* 141 */       if (!this.dc.isBlocking())
/* 142 */         throw new IllegalBlockingModeException();
/*     */       try {
/* 144 */         synchronized (paramDatagramPacket) {
/* 145 */           ByteBuffer localByteBuffer = ByteBuffer.wrap(paramDatagramPacket.getData(), paramDatagramPacket.getOffset(), paramDatagramPacket.getLength());
/*     */ 
/* 148 */           if (this.dc.isConnected()) {
/* 149 */             if (paramDatagramPacket.getAddress() == null)
/*     */             {
/* 152 */               InetSocketAddress localInetSocketAddress = (InetSocketAddress)this.dc.remoteAddress();
/*     */ 
/* 154 */               paramDatagramPacket.setPort(localInetSocketAddress.getPort());
/* 155 */               paramDatagramPacket.setAddress(localInetSocketAddress.getAddress());
/* 156 */               this.dc.write(localByteBuffer);
/*     */             }
/*     */             else {
/* 159 */               this.dc.send(localByteBuffer, paramDatagramPacket.getSocketAddress());
/*     */             }
/*     */           }
/*     */           else
/* 163 */             this.dc.send(localByteBuffer, paramDatagramPacket.getSocketAddress());
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException) {
/* 167 */         Net.translateException(localIOException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private SocketAddress receive(ByteBuffer paramByteBuffer)
/*     */     throws IOException
/*     */   {
/* 175 */     if (this.timeout == 0) {
/* 176 */       return this.dc.receive(paramByteBuffer);
/*     */     }
/*     */ 
/* 180 */     SelectionKey localSelectionKey = null;
/* 181 */     Selector localSelector = null;
/* 182 */     this.dc.configureBlocking(false);
/*     */     try
/*     */     {
/*     */       SocketAddress localSocketAddress1;
/* 186 */       if ((localSocketAddress1 = this.dc.receive(paramByteBuffer)) != null)
/* 187 */         return localSocketAddress1;
/* 188 */       localSelector = Util.getTemporarySelector(this.dc);
/* 189 */       localSelectionKey = this.dc.register(localSelector, 1);
/* 190 */       long l1 = this.timeout;
/*     */       while (true) {
/* 192 */         if (!this.dc.isOpen())
/* 193 */           throw new ClosedChannelException();
/* 194 */         long l2 = System.currentTimeMillis();
/* 195 */         int i = localSelector.select(l1);
/* 196 */         if ((i > 0) && (localSelectionKey.isReadable()) && 
/* 197 */           ((localSocketAddress1 = this.dc.receive(paramByteBuffer)) != null)) {
/* 198 */           return localSocketAddress1;
/*     */         }
/* 200 */         localSelector.selectedKeys().remove(localSelectionKey);
/* 201 */         l1 -= System.currentTimeMillis() - l2;
/* 202 */         if (l1 <= 0L)
/* 203 */           throw new SocketTimeoutException();
/*     */       }
/*     */     }
/*     */     finally {
/* 207 */       if (localSelectionKey != null)
/* 208 */         localSelectionKey.cancel();
/* 209 */       if (this.dc.isOpen())
/* 210 */         this.dc.configureBlocking(true);
/* 211 */       if (localSelector != null)
/* 212 */         Util.releaseTemporarySelector(localSelector);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void receive(DatagramPacket paramDatagramPacket) throws IOException {
/* 217 */     synchronized (this.dc.blockingLock()) {
/* 218 */       if (!this.dc.isBlocking())
/* 219 */         throw new IllegalBlockingModeException();
/*     */       try {
/* 221 */         synchronized (paramDatagramPacket) {
/* 222 */           ByteBuffer localByteBuffer = ByteBuffer.wrap(paramDatagramPacket.getData(), paramDatagramPacket.getOffset(), paramDatagramPacket.getLength());
/*     */ 
/* 225 */           SocketAddress localSocketAddress = receive(localByteBuffer);
/* 226 */           paramDatagramPacket.setSocketAddress(localSocketAddress);
/* 227 */           paramDatagramPacket.setLength(localByteBuffer.position() - paramDatagramPacket.getOffset());
/*     */         }
/*     */       } catch (IOException localIOException) {
/* 230 */         Net.translateException(localIOException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public InetAddress getLocalAddress() {
/* 236 */     if (isClosed())
/* 237 */       return null;
/* 238 */     Object localObject = this.dc.localAddress();
/* 239 */     if (localObject == null)
/* 240 */       localObject = new InetSocketAddress(0);
/* 241 */     InetAddress localInetAddress = ((InetSocketAddress)localObject).getAddress();
/* 242 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 243 */     if (localSecurityManager != null) {
/*     */       try {
/* 245 */         localSecurityManager.checkConnect(localInetAddress.getHostAddress(), -1);
/*     */       } catch (SecurityException localSecurityException) {
/* 247 */         return new InetSocketAddress(0).getAddress();
/*     */       }
/*     */     }
/* 250 */     return localInetAddress;
/*     */   }
/*     */ 
/*     */   public int getLocalPort() {
/* 254 */     if (isClosed())
/* 255 */       return -1;
/*     */     try {
/* 257 */       SocketAddress localSocketAddress = this.dc.getLocalAddress();
/* 258 */       if (localSocketAddress != null)
/* 259 */         return ((InetSocketAddress)localSocketAddress).getPort();
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 263 */     return 0;
/*     */   }
/*     */ 
/*     */   public void setSoTimeout(int paramInt) throws SocketException {
/* 267 */     this.timeout = paramInt;
/*     */   }
/*     */ 
/*     */   public int getSoTimeout() throws SocketException {
/* 271 */     return this.timeout;
/*     */   }
/*     */ 
/*     */   private void setBooleanOption(SocketOption<Boolean> paramSocketOption, boolean paramBoolean) throws SocketException
/*     */   {
/*     */     try
/*     */     {
/* 278 */       this.dc.setOption(paramSocketOption, Boolean.valueOf(paramBoolean));
/*     */     } catch (IOException localIOException) {
/* 280 */       Net.translateToSocketException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setIntOption(SocketOption<Integer> paramSocketOption, int paramInt) throws SocketException
/*     */   {
/*     */     try
/*     */     {
/* 288 */       this.dc.setOption(paramSocketOption, Integer.valueOf(paramInt));
/*     */     } catch (IOException localIOException) {
/* 290 */       Net.translateToSocketException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean getBooleanOption(SocketOption<Boolean> paramSocketOption) throws SocketException {
/*     */     try {
/* 296 */       return ((Boolean)this.dc.getOption(paramSocketOption)).booleanValue();
/*     */     } catch (IOException localIOException) {
/* 298 */       Net.translateToSocketException(localIOException);
/* 299 */     }return false;
/*     */   }
/*     */ 
/*     */   private int getIntOption(SocketOption<Integer> paramSocketOption) throws SocketException
/*     */   {
/*     */     try {
/* 305 */       return ((Integer)this.dc.getOption(paramSocketOption)).intValue();
/*     */     } catch (IOException localIOException) {
/* 307 */       Net.translateToSocketException(localIOException);
/* 308 */     }return -1;
/*     */   }
/*     */ 
/*     */   public void setSendBufferSize(int paramInt) throws SocketException
/*     */   {
/* 313 */     if (paramInt <= 0)
/* 314 */       throw new IllegalArgumentException("Invalid send size");
/* 315 */     setIntOption(StandardSocketOptions.SO_SNDBUF, paramInt);
/*     */   }
/*     */ 
/*     */   public int getSendBufferSize() throws SocketException {
/* 319 */     return getIntOption(StandardSocketOptions.SO_SNDBUF);
/*     */   }
/*     */ 
/*     */   public void setReceiveBufferSize(int paramInt) throws SocketException {
/* 323 */     if (paramInt <= 0)
/* 324 */       throw new IllegalArgumentException("Invalid receive size");
/* 325 */     setIntOption(StandardSocketOptions.SO_RCVBUF, paramInt);
/*     */   }
/*     */ 
/*     */   public int getReceiveBufferSize() throws SocketException {
/* 329 */     return getIntOption(StandardSocketOptions.SO_RCVBUF);
/*     */   }
/*     */ 
/*     */   public void setReuseAddress(boolean paramBoolean) throws SocketException {
/* 333 */     setBooleanOption(StandardSocketOptions.SO_REUSEADDR, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getReuseAddress() throws SocketException {
/* 337 */     return getBooleanOption(StandardSocketOptions.SO_REUSEADDR);
/*     */   }
/*     */ 
/*     */   public void setBroadcast(boolean paramBoolean) throws SocketException
/*     */   {
/* 342 */     setBooleanOption(StandardSocketOptions.SO_BROADCAST, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getBroadcast() throws SocketException {
/* 346 */     return getBooleanOption(StandardSocketOptions.SO_BROADCAST);
/*     */   }
/*     */ 
/*     */   public void setTrafficClass(int paramInt) throws SocketException {
/* 350 */     setIntOption(StandardSocketOptions.IP_TOS, paramInt);
/*     */   }
/*     */ 
/*     */   public int getTrafficClass() throws SocketException {
/* 354 */     return getIntOption(StandardSocketOptions.IP_TOS);
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 359 */       this.dc.close();
/*     */     } catch (IOException localIOException) {
/* 361 */       throw new Error(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isClosed() {
/* 366 */     return !this.dc.isOpen();
/*     */   }
/*     */ 
/*     */   public DatagramChannel getChannel() {
/* 370 */     return this.dc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.DatagramSocketAdaptor
 * JD-Core Version:    0.6.2
 */