/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.nio.channels.Pipe.SinkChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ 
/*     */ class SinkChannelImpl extends Pipe.SinkChannel
/*     */   implements SelChImpl
/*     */ {
/*     */   SocketChannel sc;
/*     */ 
/*     */   public FileDescriptor getFD()
/*     */   {
/*  50 */     return ((SocketChannelImpl)this.sc).getFD();
/*     */   }
/*     */ 
/*     */   public int getFDVal() {
/*  54 */     return ((SocketChannelImpl)this.sc).getFDVal();
/*     */   }
/*     */ 
/*     */   SinkChannelImpl(SelectorProvider paramSelectorProvider, SocketChannel paramSocketChannel) {
/*  58 */     super(paramSelectorProvider);
/*  59 */     this.sc = paramSocketChannel;
/*     */   }
/*     */ 
/*     */   protected void implCloseSelectableChannel() throws IOException {
/*  63 */     if (!isRegistered())
/*  64 */       kill();
/*     */   }
/*     */ 
/*     */   public void kill() throws IOException {
/*  68 */     this.sc.close();
/*     */   }
/*     */ 
/*     */   protected void implConfigureBlocking(boolean paramBoolean) throws IOException {
/*  72 */     this.sc.configureBlocking(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean translateReadyOps(int paramInt1, int paramInt2, SelectionKeyImpl paramSelectionKeyImpl)
/*     */   {
/*  77 */     int i = paramSelectionKeyImpl.nioInterestOps();
/*  78 */     int j = paramSelectionKeyImpl.nioReadyOps();
/*  79 */     int k = paramInt2;
/*     */ 
/*  81 */     if ((paramInt1 & 0x20) != 0) {
/*  82 */       throw new Error("POLLNVAL detected");
/*     */     }
/*  84 */     if ((paramInt1 & 0x18) != 0)
/*     */     {
/*  86 */       k = i;
/*  87 */       paramSelectionKeyImpl.nioReadyOps(k);
/*  88 */       return (k & (j ^ 0xFFFFFFFF)) != 0;
/*     */     }
/*     */ 
/*  91 */     if (((paramInt1 & 0x4) != 0) && ((i & 0x4) != 0))
/*     */     {
/*  93 */       k |= 4;
/*     */     }
/*  95 */     paramSelectionKeyImpl.nioReadyOps(k);
/*  96 */     return (k & (j ^ 0xFFFFFFFF)) != 0;
/*     */   }
/*     */ 
/*     */   public boolean translateAndUpdateReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 100 */     return translateReadyOps(paramInt, paramSelectionKeyImpl.nioReadyOps(), paramSelectionKeyImpl);
/*     */   }
/*     */ 
/*     */   public boolean translateAndSetReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 104 */     return translateReadyOps(paramInt, 0, paramSelectionKeyImpl);
/*     */   }
/*     */ 
/*     */   public void translateAndSetInterestOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 108 */     if ((paramInt & 0x4) != 0)
/* 109 */       paramInt = 4;
/* 110 */     paramSelectionKeyImpl.selector.putEventOps(paramSelectionKeyImpl, paramInt);
/*     */   }
/*     */ 
/*     */   public int write(ByteBuffer paramByteBuffer) throws IOException {
/*     */     try {
/* 115 */       return this.sc.write(paramByteBuffer);
/*     */     } catch (AsynchronousCloseException localAsynchronousCloseException) {
/* 117 */       close();
/* 118 */       throw localAsynchronousCloseException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long write(ByteBuffer[] paramArrayOfByteBuffer) throws IOException {
/*     */     try {
/* 124 */       return this.sc.write(paramArrayOfByteBuffer);
/*     */     } catch (AsynchronousCloseException localAsynchronousCloseException) {
/* 126 */       close();
/* 127 */       throw localAsynchronousCloseException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long write(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 134 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/* 135 */       throw new IndexOutOfBoundsException();
/*     */     try {
/* 137 */       return write(Util.subsequence(paramArrayOfByteBuffer, paramInt1, paramInt2));
/*     */     } catch (AsynchronousCloseException localAsynchronousCloseException) {
/* 139 */       close();
/* 140 */       throw localAsynchronousCloseException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SinkChannelImpl
 * JD-Core Version:    0.6.2
 */