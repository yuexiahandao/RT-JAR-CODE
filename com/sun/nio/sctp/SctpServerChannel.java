/*     */ package com.sun.nio.sctp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.spi.AbstractSelectableChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Set;
/*     */ import sun.nio.ch.SctpServerChannelImpl;
/*     */ 
/*     */ public abstract class SctpServerChannel extends AbstractSelectableChannel
/*     */ {
/*     */   protected SctpServerChannel(SelectorProvider paramSelectorProvider)
/*     */   {
/*  81 */     super(paramSelectorProvider);
/*     */   }
/*     */ 
/*     */   public static SctpServerChannel open()
/*     */     throws IOException
/*     */   {
/* 101 */     return new SctpServerChannelImpl((SelectorProvider)null);
/*     */   }
/*     */ 
/*     */   public abstract SctpChannel accept()
/*     */     throws IOException;
/*     */ 
/*     */   public final SctpServerChannel bind(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/* 184 */     return bind(paramSocketAddress, 0);
/*     */   }
/*     */ 
/*     */   public abstract SctpServerChannel bind(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SctpServerChannel bindAddress(InetAddress paramInetAddress)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SctpServerChannel unbindAddress(InetAddress paramInetAddress)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Set<SocketAddress> getAllLocalAddresses()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <T> T getOption(SctpSocketOption<T> paramSctpSocketOption)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <T> SctpServerChannel setOption(SctpSocketOption<T> paramSctpSocketOption, T paramT)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Set<SctpSocketOption<?>> supportedOptions();
/*     */ 
/*     */   public final int validOps()
/*     */   {
/* 419 */     return 16;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.SctpServerChannel
 * JD-Core Version:    0.6.2
 */