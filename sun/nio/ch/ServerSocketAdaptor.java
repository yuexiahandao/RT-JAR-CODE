/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.IllegalBlockingModeException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ServerSocketAdaptor extends ServerSocket
/*     */ {
/*     */   private final ServerSocketChannelImpl ssc;
/*  48 */   private volatile int timeout = 0;
/*     */ 
/*     */   public static ServerSocket create(ServerSocketChannelImpl paramServerSocketChannelImpl) {
/*     */     try {
/*  52 */       return new ServerSocketAdaptor(paramServerSocketChannelImpl);
/*     */     } catch (IOException localIOException) {
/*  54 */       throw new Error(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private ServerSocketAdaptor(ServerSocketChannelImpl paramServerSocketChannelImpl)
/*     */     throws IOException
/*     */   {
/*  62 */     this.ssc = paramServerSocketChannelImpl;
/*     */   }
/*     */ 
/*     */   public void bind(SocketAddress paramSocketAddress) throws IOException
/*     */   {
/*  67 */     bind(paramSocketAddress, 50);
/*     */   }
/*     */ 
/*     */   public void bind(SocketAddress paramSocketAddress, int paramInt) throws IOException {
/*  71 */     if (paramSocketAddress == null)
/*  72 */       paramSocketAddress = new InetSocketAddress(0);
/*     */     try {
/*  74 */       this.ssc.bind(paramSocketAddress, paramInt);
/*     */     } catch (Exception localException) {
/*  76 */       Net.translateException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public InetAddress getInetAddress() {
/*  81 */     if (!this.ssc.isBound())
/*  82 */       return null;
/*  83 */     return Net.getRevealedLocalAddress(this.ssc.localAddress()).getAddress();
/*     */   }
/*     */ 
/*     */   public int getLocalPort()
/*     */   {
/*  88 */     if (!this.ssc.isBound())
/*  89 */       return -1;
/*  90 */     return Net.asInetSocketAddress(this.ssc.localAddress()).getPort();
/*     */   }
/*     */ 
/*     */   public Socket accept() throws IOException
/*     */   {
/*  95 */     synchronized (this.ssc.blockingLock()) {
/*  96 */       if (!this.ssc.isBound())
/*  97 */         throw new IllegalBlockingModeException();
/*     */       try {
/*  99 */         if (this.timeout == 0) {
/* 100 */           localObject1 = this.ssc.accept();
/* 101 */           if ((localObject1 == null) && (!this.ssc.isBlocking()))
/* 102 */             throw new IllegalBlockingModeException();
/* 103 */           return ((SocketChannel)localObject1).socket();
/*     */         }
/*     */ 
/* 107 */         Object localObject1 = null;
/* 108 */         Selector localSelector = null;
/* 109 */         this.ssc.configureBlocking(false);
/*     */         try
/*     */         {
/*     */           SocketChannel localSocketChannel;
/* 112 */           if ((localSocketChannel = this.ssc.accept()) != null) {
/* 113 */             Socket localSocket1 = localSocketChannel.socket();
/*     */ 
/* 131 */             if (localObject1 != null)
/* 132 */               ((SelectionKey)localObject1).cancel();
/* 133 */             if (this.ssc.isOpen())
/* 134 */               this.ssc.configureBlocking(true);
/* 135 */             if (localSelector != null)
/* 136 */               Util.releaseTemporarySelector(localSelector); return localSocket1;
/*     */           }
/* 114 */           localSelector = Util.getTemporarySelector(this.ssc);
/* 115 */           localObject1 = this.ssc.register(localSelector, 16);
/* 116 */           long l1 = this.timeout;
/*     */           while (true) {
/* 118 */             if (!this.ssc.isOpen())
/* 119 */               throw new ClosedChannelException();
/* 120 */             long l2 = System.currentTimeMillis();
/* 121 */             int i = localSelector.select(l1);
/* 122 */             if ((i > 0) && (((SelectionKey)localObject1).isAcceptable()) && ((localSocketChannel = this.ssc.accept()) != null))
/*     */             {
/* 124 */               Socket localSocket2 = localSocketChannel.socket();
/*     */ 
/* 131 */               if (localObject1 != null)
/* 132 */                 ((SelectionKey)localObject1).cancel();
/* 133 */               if (this.ssc.isOpen())
/* 134 */                 this.ssc.configureBlocking(true);
/* 135 */               if (localSelector != null)
/* 136 */                 Util.releaseTemporarySelector(localSelector); return localSocket2;
/*     */             }
/* 125 */             localSelector.selectedKeys().remove(localObject1);
/* 126 */             l1 -= System.currentTimeMillis() - l2;
/* 127 */             if (l1 <= 0L)
/* 128 */               throw new SocketTimeoutException();
/*     */           }
/*     */         } finally {
/* 131 */           if (localObject1 != null)
/* 132 */             ((SelectionKey)localObject1).cancel();
/* 133 */           if (this.ssc.isOpen())
/* 134 */             this.ssc.configureBlocking(true);
/* 135 */           if (localSelector != null)
/* 136 */             Util.releaseTemporarySelector(localSelector);
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {
/* 140 */         Net.translateException(localException);
/* 141 */         if (!$assertionsDisabled) throw new AssertionError();
/* 142 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 148 */     this.ssc.close();
/*     */   }
/*     */ 
/*     */   public ServerSocketChannel getChannel() {
/* 152 */     return this.ssc;
/*     */   }
/*     */ 
/*     */   public boolean isBound() {
/* 156 */     return this.ssc.isBound();
/*     */   }
/*     */ 
/*     */   public boolean isClosed() {
/* 160 */     return !this.ssc.isOpen();
/*     */   }
/*     */ 
/*     */   public void setSoTimeout(int paramInt) throws SocketException {
/* 164 */     this.timeout = paramInt;
/*     */   }
/*     */ 
/*     */   public int getSoTimeout() throws SocketException {
/* 168 */     return this.timeout;
/*     */   }
/*     */ 
/*     */   public void setReuseAddress(boolean paramBoolean) throws SocketException {
/*     */     try {
/* 173 */       this.ssc.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.valueOf(paramBoolean));
/*     */     } catch (IOException localIOException) {
/* 175 */       Net.translateToSocketException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getReuseAddress() throws SocketException {
/*     */     try {
/* 181 */       return ((Boolean)this.ssc.getOption(StandardSocketOptions.SO_REUSEADDR)).booleanValue();
/*     */     } catch (IOException localIOException) {
/* 183 */       Net.translateToSocketException(localIOException);
/* 184 */     }return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 189 */     if (!isBound())
/* 190 */       return "ServerSocket[unbound]";
/* 191 */     return "ServerSocket[addr=" + getInetAddress() + ",localport=" + getLocalPort() + "]";
/*     */   }
/*     */ 
/*     */   public void setReceiveBufferSize(int paramInt)
/*     */     throws SocketException
/*     */   {
/* 198 */     if (paramInt <= 0)
/* 199 */       throw new IllegalArgumentException("size cannot be 0 or negative");
/*     */     try {
/* 201 */       this.ssc.setOption(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(paramInt));
/*     */     } catch (IOException localIOException) {
/* 203 */       Net.translateToSocketException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getReceiveBufferSize() throws SocketException {
/*     */     try {
/* 209 */       return ((Integer)this.ssc.getOption(StandardSocketOptions.SO_RCVBUF)).intValue();
/*     */     } catch (IOException localIOException) {
/* 211 */       Net.translateToSocketException(localIOException);
/* 212 */     }return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.ServerSocketAdaptor
 * JD-Core Version:    0.6.2
 */