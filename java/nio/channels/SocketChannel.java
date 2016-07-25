/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketOption;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.spi.AbstractSelectableChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ 
/*     */ public abstract class SocketChannel extends AbstractSelectableChannel
/*     */   implements ByteChannel, ScatteringByteChannel, GatheringByteChannel, NetworkChannel
/*     */ {
/*     */   protected SocketChannel(SelectorProvider paramSelectorProvider)
/*     */   {
/* 125 */     super(paramSelectorProvider);
/*     */   }
/*     */ 
/*     */   public static SocketChannel open()
/*     */     throws IOException
/*     */   {
/* 142 */     return SelectorProvider.provider().openSocketChannel();
/*     */   }
/*     */ 
/*     */   public static SocketChannel open(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/* 182 */     SocketChannel localSocketChannel = open();
/*     */     try {
/* 184 */       localSocketChannel.connect(paramSocketAddress);
/*     */     } catch (Throwable localThrowable1) {
/*     */       try {
/* 187 */         localSocketChannel.close();
/*     */       } catch (Throwable localThrowable2) {
/* 189 */         localThrowable1.addSuppressed(localThrowable2);
/*     */       }
/* 191 */       throw localThrowable1;
/*     */     }
/* 193 */     assert (localSocketChannel.isConnected());
/* 194 */     return localSocketChannel;
/*     */   }
/*     */ 
/*     */   public final int validOps()
/*     */   {
/* 209 */     return 13;
/*     */   }
/*     */ 
/*     */   public abstract SocketChannel bind(SocketAddress paramSocketAddress)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <T> SocketChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SocketChannel shutdownInput()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SocketChannel shutdownOutput()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Socket socket();
/*     */ 
/*     */   public abstract boolean isConnected();
/*     */ 
/*     */   public abstract boolean isConnectionPending();
/*     */ 
/*     */   public abstract boolean connect(SocketAddress paramSocketAddress)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean finishConnect()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SocketAddress getRemoteAddress()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract int read(ByteBuffer paramByteBuffer)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract long read(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public final long read(ByteBuffer[] paramArrayOfByteBuffer)
/*     */     throws IOException
/*     */   {
/* 472 */     return read(paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length);
/*     */   }
/*     */ 
/*     */   public abstract int write(ByteBuffer paramByteBuffer)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract long write(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public final long write(ByteBuffer[] paramArrayOfByteBuffer)
/*     */     throws IOException
/*     */   {
/* 493 */     return write(paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.SocketChannel
 * JD-Core Version:    0.6.2
 */