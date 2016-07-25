/*     */ package sun.nio.ch;
/*     */ 
/*     */ import com.sun.nio.sctp.Association;
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import com.sun.nio.sctp.NotificationHandler;
/*     */ import com.sun.nio.sctp.SctpChannel;
/*     */ import com.sun.nio.sctp.SctpSocketOption;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SctpChannelImpl extends SctpChannel
/*     */ {
/*     */   private static final String message = "SCTP not supported on this platform";
/*     */ 
/*     */   public SctpChannelImpl(SelectorProvider paramSelectorProvider)
/*     */   {
/*  47 */     super(paramSelectorProvider);
/*  48 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public Association association()
/*     */   {
/*  53 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpChannel bind(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/*  59 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpChannel bindAddress(InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/*  65 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpChannel unbindAddress(InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/*  71 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public boolean connect(SocketAddress paramSocketAddress) throws IOException
/*     */   {
/*  76 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public boolean connect(SocketAddress paramSocketAddress, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  82 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public boolean isConnectionPending()
/*     */   {
/*  87 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public boolean finishConnect() throws IOException
/*     */   {
/*  92 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public Set<SocketAddress> getAllLocalAddresses()
/*     */     throws IOException
/*     */   {
/*  98 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public Set<SocketAddress> getRemoteAddresses()
/*     */     throws IOException
/*     */   {
/* 104 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpChannel shutdown() throws IOException
/*     */   {
/* 109 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public <T> T getOption(SctpSocketOption<T> paramSctpSocketOption)
/*     */     throws IOException
/*     */   {
/* 115 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public <T> SctpChannel setOption(SctpSocketOption<T> paramSctpSocketOption, T paramT)
/*     */     throws IOException
/*     */   {
/* 121 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public Set<SctpSocketOption<?>> supportedOptions()
/*     */   {
/* 126 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public <T> MessageInfo receive(ByteBuffer paramByteBuffer, T paramT, NotificationHandler<T> paramNotificationHandler)
/*     */     throws IOException
/*     */   {
/* 132 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public int send(ByteBuffer paramByteBuffer, MessageInfo paramMessageInfo)
/*     */     throws IOException
/*     */   {
/* 138 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   protected void implConfigureBlocking(boolean paramBoolean) throws IOException
/*     */   {
/* 143 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public void implCloseSelectableChannel() throws IOException
/*     */   {
/* 148 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SctpChannelImpl
 * JD-Core Version:    0.6.2
 */