/*     */ package sun.management.jdp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.StandardProtocolFamily;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.nio.channels.UnsupportedAddressTypeException;
/*     */ 
/*     */ public final class JdpBroadcaster
/*     */ {
/*     */   private final InetAddress addr;
/*     */   private final int port;
/*     */   private final DatagramChannel channel;
/*     */ 
/*     */   public JdpBroadcaster(InetAddress paramInetAddress1, InetAddress paramInetAddress2, int paramInt1, int paramInt2)
/*     */     throws IOException, JdpException
/*     */   {
/*  67 */     this.addr = paramInetAddress1;
/*  68 */     this.port = paramInt1;
/*     */ 
/*  70 */     StandardProtocolFamily localStandardProtocolFamily = (paramInetAddress1 instanceof Inet6Address) ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET;
/*     */ 
/*  73 */     this.channel = DatagramChannel.open(localStandardProtocolFamily);
/*  74 */     this.channel.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.valueOf(true));
/*  75 */     this.channel.setOption(StandardSocketOptions.IP_MULTICAST_TTL, Integer.valueOf(paramInt2));
/*     */ 
/*  79 */     if (paramInetAddress2 != null)
/*     */     {
/*  81 */       NetworkInterface localNetworkInterface = NetworkInterface.getByInetAddress(paramInetAddress2);
/*     */       try {
/*  83 */         this.channel.bind(new InetSocketAddress(paramInetAddress2, 0));
/*     */       } catch (UnsupportedAddressTypeException localUnsupportedAddressTypeException) {
/*  85 */         throw new JdpException("Unable to bind to source address");
/*     */       }
/*  87 */       this.channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, localNetworkInterface);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JdpBroadcaster(InetAddress paramInetAddress, int paramInt1, int paramInt2)
/*     */     throws IOException, JdpException
/*     */   {
/* 101 */     this(paramInetAddress, null, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void sendPacket(JdpPacket paramJdpPacket)
/*     */     throws IOException
/*     */   {
/* 112 */     byte[] arrayOfByte = paramJdpPacket.getPacketData();
/*     */ 
/* 114 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
/* 115 */     this.channel.send(localByteBuffer, new InetSocketAddress(this.addr, this.port));
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */     throws IOException
/*     */   {
/* 124 */     this.channel.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jdp.JdpBroadcaster
 * JD-Core Version:    0.6.2
 */