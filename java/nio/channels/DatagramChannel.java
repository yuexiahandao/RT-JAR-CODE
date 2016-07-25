/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.ProtocolFamily;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketOption;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.spi.AbstractSelectableChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ 
/*     */ public abstract class DatagramChannel extends AbstractSelectableChannel
/*     */   implements ByteChannel, ScatteringByteChannel, GatheringByteChannel, MulticastChannel
/*     */ {
/*     */   protected DatagramChannel(SelectorProvider paramSelectorProvider)
/*     */   {
/* 122 */     super(paramSelectorProvider);
/*     */   }
/*     */ 
/*     */   public static DatagramChannel open()
/*     */     throws IOException
/*     */   {
/* 146 */     return SelectorProvider.provider().openDatagramChannel();
/*     */   }
/*     */ 
/*     */   public static DatagramChannel open(ProtocolFamily paramProtocolFamily)
/*     */     throws IOException
/*     */   {
/* 179 */     return SelectorProvider.provider().openDatagramChannel(paramProtocolFamily);
/*     */   }
/*     */ 
/*     */   public final int validOps()
/*     */   {
/* 193 */     return 5;
/*     */   }
/*     */ 
/*     */   public abstract DatagramChannel bind(SocketAddress paramSocketAddress)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <T> DatagramChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract DatagramSocket socket();
/*     */ 
/*     */   public abstract boolean isConnected();
/*     */ 
/*     */   public abstract DatagramChannel connect(SocketAddress paramSocketAddress)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract DatagramChannel disconnect()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SocketAddress getRemoteAddress()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SocketAddress receive(ByteBuffer paramByteBuffer)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract int send(ByteBuffer paramByteBuffer, SocketAddress paramSocketAddress)
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
/* 511 */     return read(paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length);
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
/* 565 */     return write(paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.DatagramChannel
 * JD-Core Version:    0.6.2
 */