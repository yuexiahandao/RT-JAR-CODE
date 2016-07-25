/*     */ package sun.nio.ch;
/*     */ 
/*     */ import com.sun.nio.sctp.Association;
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import com.sun.nio.sctp.NotificationHandler;
/*     */ import com.sun.nio.sctp.SctpChannel;
/*     */ import com.sun.nio.sctp.SctpMultiChannel;
/*     */ import com.sun.nio.sctp.SctpSocketOption;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SctpMultiChannelImpl extends SctpMultiChannel
/*     */ {
/*     */   private static final String message = "SCTP not supported on this platform";
/*     */ 
/*     */   public SctpMultiChannelImpl(SelectorProvider paramSelectorProvider)
/*     */   {
/*  48 */     super(paramSelectorProvider);
/*  49 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public Set<Association> associations()
/*     */   {
/*  54 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpMultiChannel bind(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/*  60 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpMultiChannel bindAddress(InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/*  66 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpMultiChannel unbindAddress(InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/*  72 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public Set<SocketAddress> getAllLocalAddresses()
/*     */     throws IOException
/*     */   {
/*  78 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public Set<SocketAddress> getRemoteAddresses(Association paramAssociation)
/*     */     throws IOException
/*     */   {
/*  84 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpMultiChannel shutdown(Association paramAssociation)
/*     */     throws IOException
/*     */   {
/*  90 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public <T> T getOption(SctpSocketOption<T> paramSctpSocketOption, Association paramAssociation)
/*     */     throws IOException
/*     */   {
/*  96 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public <T> SctpMultiChannel setOption(SctpSocketOption<T> paramSctpSocketOption, T paramT, Association paramAssociation)
/*     */     throws IOException
/*     */   {
/* 102 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public Set<SctpSocketOption<?>> supportedOptions()
/*     */   {
/* 107 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public <T> MessageInfo receive(ByteBuffer paramByteBuffer, T paramT, NotificationHandler<T> paramNotificationHandler)
/*     */     throws IOException
/*     */   {
/* 113 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public int send(ByteBuffer paramByteBuffer, MessageInfo paramMessageInfo)
/*     */     throws IOException
/*     */   {
/* 119 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public SctpChannel branch(Association paramAssociation)
/*     */     throws IOException
/*     */   {
/* 125 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   protected void implConfigureBlocking(boolean paramBoolean) throws IOException
/*     */   {
/* 130 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ 
/*     */   public void implCloseSelectableChannel() throws IOException
/*     */   {
/* 135 */     throw new UnsupportedOperationException("SCTP not supported on this platform");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SctpMultiChannelImpl
 * JD-Core Version:    0.6.2
 */