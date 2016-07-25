/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import sun.net.ResourceManager;
/*     */ 
/*     */ class TwoStacksPlainSocketImpl extends AbstractPlainSocketImpl
/*     */ {
/*     */   private FileDescriptor fd1;
/*  62 */   private InetAddress anyLocalBoundAddr = null;
/*     */ 
/*  67 */   private int lastfd = -1;
/*     */   private final boolean exclusiveBind;
/*     */   private boolean isReuseAddress;
/*     */ 
/*     */   public TwoStacksPlainSocketImpl(boolean paramBoolean)
/*     */   {
/*  80 */     this.exclusiveBind = paramBoolean;
/*     */   }
/*     */ 
/*     */   public TwoStacksPlainSocketImpl(FileDescriptor paramFileDescriptor, boolean paramBoolean) {
/*  84 */     this.fd = paramFileDescriptor;
/*  85 */     this.exclusiveBind = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected synchronized void create(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  93 */     this.fd1 = new FileDescriptor();
/*     */     try {
/*  95 */       super.create(paramBoolean);
/*     */     } catch (IOException localIOException) {
/*  97 */       this.fd1 = null;
/*  98 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected synchronized void bind(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 110 */     super.bind(paramInetAddress, paramInt);
/* 111 */     if (paramInetAddress.isAnyLocalAddress())
/* 112 */       this.anyLocalBoundAddr = paramInetAddress;
/*     */   }
/*     */ 
/*     */   public Object getOption(int paramInt) throws SocketException
/*     */   {
/* 117 */     if (isClosedOrPending()) {
/* 118 */       throw new SocketException("Socket Closed");
/*     */     }
/* 120 */     if (paramInt == 15) {
/* 121 */       if ((this.fd != null) && (this.fd1 != null))
/*     */       {
/* 123 */         return this.anyLocalBoundAddr;
/*     */       }
/* 125 */       InetAddressContainer localInetAddressContainer = new InetAddressContainer();
/* 126 */       socketGetOption(paramInt, localInetAddressContainer);
/* 127 */       return localInetAddressContainer.addr;
/* 128 */     }if ((paramInt == 4) && (this.exclusiveBind))
/*     */     {
/* 130 */       return Boolean.valueOf(this.isReuseAddress);
/*     */     }
/* 132 */     return super.getOption(paramInt);
/*     */   }
/*     */ 
/*     */   void socketBind(InetAddress paramInetAddress, int paramInt) throws IOException
/*     */   {
/* 137 */     socketBind(paramInetAddress, paramInt, this.exclusiveBind);
/*     */   }
/*     */ 
/*     */   void socketSetOption(int paramInt, boolean paramBoolean, Object paramObject)
/*     */     throws SocketException
/*     */   {
/* 145 */     if ((paramInt == 4) && (this.exclusiveBind))
/* 146 */       this.isReuseAddress = paramBoolean;
/*     */     else
/* 148 */       socketNativeSetOption(paramInt, paramBoolean, paramObject);
/*     */   }
/*     */ 
/*     */   protected void close()
/*     */     throws IOException
/*     */   {
/* 156 */     synchronized (this.fdLock) {
/* 157 */       if ((this.fd != null) || (this.fd1 != null)) {
/* 158 */         if (!this.stream) {
/* 159 */           ResourceManager.afterUdpClose();
/*     */         }
/* 161 */         if (this.fdUseCount == 0) {
/* 162 */           if (this.closePending) {
/* 163 */             return;
/*     */           }
/* 165 */           this.closePending = true;
/* 166 */           socketClose();
/* 167 */           this.fd = null;
/* 168 */           this.fd1 = null;
/* 169 */           return;
/*     */         }
/*     */ 
/* 177 */         if (!this.closePending) {
/* 178 */           this.closePending = true;
/* 179 */           this.fdUseCount -= 1;
/* 180 */           socketClose();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void reset()
/*     */     throws IOException
/*     */   {
/* 189 */     if ((this.fd != null) || (this.fd1 != null)) {
/* 190 */       socketClose();
/*     */     }
/* 192 */     this.fd = null;
/* 193 */     this.fd1 = null;
/* 194 */     super.reset();
/*     */   }
/*     */ 
/*     */   public boolean isClosedOrPending()
/*     */   {
/* 206 */     synchronized (this.fdLock) {
/* 207 */       if ((this.closePending) || ((this.fd == null) && (this.fd1 == null))) {
/* 208 */         return true;
/*     */       }
/* 210 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static native void initProto();
/*     */ 
/*     */   native void socketCreate(boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   native void socketConnect(InetAddress paramInetAddress, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   native void socketBind(InetAddress paramInetAddress, int paramInt, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   native void socketListen(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   native void socketAccept(SocketImpl paramSocketImpl)
/*     */     throws IOException;
/*     */ 
/*     */   native int socketAvailable()
/*     */     throws IOException;
/*     */ 
/*     */   native void socketClose0(boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   native void socketShutdown(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   native void socketNativeSetOption(int paramInt, boolean paramBoolean, Object paramObject)
/*     */     throws SocketException;
/*     */ 
/*     */   native int socketGetOption(int paramInt, Object paramObject)
/*     */     throws SocketException;
/*     */ 
/*     */   native void socketSendUrgentData(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static
/*     */   {
/*  76 */     initProto();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.TwoStacksPlainSocketImpl
 * JD-Core Version:    0.6.2
 */