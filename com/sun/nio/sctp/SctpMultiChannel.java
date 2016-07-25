/*     */ package com.sun.nio.sctp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.spi.AbstractSelectableChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Set;
/*     */ import sun.nio.ch.SctpMultiChannelImpl;
/*     */ 
/*     */ public abstract class SctpMultiChannel extends AbstractSelectableChannel
/*     */ {
/*     */   protected SctpMultiChannel(SelectorProvider paramSelectorProvider)
/*     */   {
/* 147 */     super(paramSelectorProvider);
/*     */   }
/*     */ 
/*     */   public static SctpMultiChannel open()
/*     */     throws IOException
/*     */   {
/* 165 */     return new SctpMultiChannelImpl((SelectorProvider)null);
/*     */   }
/*     */ 
/*     */   public abstract Set<Association> associations()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SctpMultiChannel bind(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public final SctpMultiChannel bind(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/* 281 */     return bind(paramSocketAddress, 0);
/*     */   }
/*     */ 
/*     */   public abstract SctpMultiChannel bindAddress(InetAddress paramInetAddress)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SctpMultiChannel unbindAddress(InetAddress paramInetAddress)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Set<SocketAddress> getAllLocalAddresses()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Set<SocketAddress> getRemoteAddresses(Association paramAssociation)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SctpMultiChannel shutdown(Association paramAssociation)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <T> T getOption(SctpSocketOption<T> paramSctpSocketOption, Association paramAssociation)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <T> SctpMultiChannel setOption(SctpSocketOption<T> paramSctpSocketOption, T paramT, Association paramAssociation)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Set<SctpSocketOption<?>> supportedOptions();
/*     */ 
/*     */   public final int validOps()
/*     */   {
/* 521 */     return 5;
/*     */   }
/*     */ 
/*     */   public abstract <T> MessageInfo receive(ByteBuffer paramByteBuffer, T paramT, NotificationHandler<T> paramNotificationHandler)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract int send(ByteBuffer paramByteBuffer, MessageInfo paramMessageInfo)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SctpChannel branch(Association paramAssociation)
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.SctpMultiChannel
 * JD-Core Version:    0.6.2
 */