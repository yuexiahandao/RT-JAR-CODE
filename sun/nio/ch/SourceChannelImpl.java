/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.nio.channels.Pipe.SourceChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ 
/*     */ class SourceChannelImpl extends Pipe.SourceChannel
/*     */   implements SelChImpl
/*     */ {
/*     */   SocketChannel sc;
/*     */ 
/*     */   public FileDescriptor getFD()
/*     */   {
/*  49 */     return ((SocketChannelImpl)this.sc).getFD();
/*     */   }
/*     */ 
/*     */   public int getFDVal() {
/*  53 */     return ((SocketChannelImpl)this.sc).getFDVal();
/*     */   }
/*     */ 
/*     */   SourceChannelImpl(SelectorProvider paramSelectorProvider, SocketChannel paramSocketChannel) {
/*  57 */     super(paramSelectorProvider);
/*  58 */     this.sc = paramSocketChannel;
/*     */   }
/*     */ 
/*     */   protected void implCloseSelectableChannel() throws IOException {
/*  62 */     if (!isRegistered())
/*  63 */       kill();
/*     */   }
/*     */ 
/*     */   public void kill() throws IOException {
/*  67 */     this.sc.close();
/*     */   }
/*     */ 
/*     */   protected void implConfigureBlocking(boolean paramBoolean) throws IOException {
/*  71 */     this.sc.configureBlocking(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean translateReadyOps(int paramInt1, int paramInt2, SelectionKeyImpl paramSelectionKeyImpl)
/*     */   {
/*  76 */     int i = paramSelectionKeyImpl.nioInterestOps();
/*  77 */     int j = paramSelectionKeyImpl.nioReadyOps();
/*  78 */     int k = paramInt2;
/*     */ 
/*  80 */     if ((paramInt1 & 0x20) != 0) {
/*  81 */       throw new Error("POLLNVAL detected");
/*     */     }
/*  83 */     if ((paramInt1 & 0x18) != 0)
/*     */     {
/*  85 */       k = i;
/*  86 */       paramSelectionKeyImpl.nioReadyOps(k);
/*  87 */       return (k & (j ^ 0xFFFFFFFF)) != 0;
/*     */     }
/*     */ 
/*  90 */     if (((paramInt1 & 0x1) != 0) && ((i & 0x1) != 0))
/*     */     {
/*  92 */       k |= 1;
/*     */     }
/*  94 */     paramSelectionKeyImpl.nioReadyOps(k);
/*  95 */     return (k & (j ^ 0xFFFFFFFF)) != 0;
/*     */   }
/*     */ 
/*     */   public boolean translateAndUpdateReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/*  99 */     return translateReadyOps(paramInt, paramSelectionKeyImpl.nioReadyOps(), paramSelectionKeyImpl);
/*     */   }
/*     */ 
/*     */   public boolean translateAndSetReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 103 */     return translateReadyOps(paramInt, 0, paramSelectionKeyImpl);
/*     */   }
/*     */ 
/*     */   public void translateAndSetInterestOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 107 */     if ((paramInt & 0x1) != 0)
/* 108 */       paramInt = 1;
/* 109 */     paramSelectionKeyImpl.selector.putEventOps(paramSelectionKeyImpl, paramInt);
/*     */   }
/*     */ 
/*     */   public int read(ByteBuffer paramByteBuffer) throws IOException {
/*     */     try {
/* 114 */       return this.sc.read(paramByteBuffer);
/*     */     } catch (AsynchronousCloseException localAsynchronousCloseException) {
/* 116 */       close();
/* 117 */       throw localAsynchronousCloseException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long read(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 124 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/* 125 */       throw new IndexOutOfBoundsException();
/*     */     try {
/* 127 */       return read(Util.subsequence(paramArrayOfByteBuffer, paramInt1, paramInt2));
/*     */     } catch (AsynchronousCloseException localAsynchronousCloseException) {
/* 129 */       close();
/* 130 */       throw localAsynchronousCloseException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long read(ByteBuffer[] paramArrayOfByteBuffer) throws IOException {
/*     */     try {
/* 136 */       return this.sc.read(paramArrayOfByteBuffer);
/*     */     } catch (AsynchronousCloseException localAsynchronousCloseException) {
/* 138 */       close();
/* 139 */       throw localAsynchronousCloseException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SourceChannelImpl
 * JD-Core Version:    0.6.2
 */