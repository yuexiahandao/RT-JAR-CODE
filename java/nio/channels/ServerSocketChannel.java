/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketOption;
/*     */ import java.nio.channels.spi.AbstractSelectableChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ 
/*     */ public abstract class ServerSocketChannel extends AbstractSelectableChannel
/*     */   implements NetworkChannel
/*     */ {
/*     */   protected ServerSocketChannel(SelectorProvider paramSelectorProvider)
/*     */   {
/*  83 */     super(paramSelectorProvider);
/*     */   }
/*     */ 
/*     */   public static ServerSocketChannel open()
/*     */     throws IOException
/*     */   {
/* 105 */     return SelectorProvider.provider().openServerSocketChannel();
/*     */   }
/*     */ 
/*     */   public final int validOps()
/*     */   {
/* 119 */     return 16;
/*     */   }
/*     */ 
/*     */   public final ServerSocketChannel bind(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/* 154 */     return bind(paramSocketAddress, 0);
/*     */   }
/*     */ 
/*     */   public abstract ServerSocketChannel bind(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <T> ServerSocketChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract ServerSocket socket();
/*     */ 
/*     */   public abstract SocketChannel accept()
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.ServerSocketChannel
 * JD-Core Version:    0.6.2
 */