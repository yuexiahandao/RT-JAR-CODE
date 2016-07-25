/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketOption;
/*     */ import java.nio.channels.spi.AsynchronousChannelProvider;
/*     */ import java.util.concurrent.Future;
/*     */ 
/*     */ public abstract class AsynchronousServerSocketChannel
/*     */   implements AsynchronousChannel, NetworkChannel
/*     */ {
/*     */   private final AsynchronousChannelProvider provider;
/*     */ 
/*     */   protected AsynchronousServerSocketChannel(AsynchronousChannelProvider paramAsynchronousChannelProvider)
/*     */   {
/* 103 */     this.provider = paramAsynchronousChannelProvider;
/*     */   }
/*     */ 
/*     */   public final AsynchronousChannelProvider provider()
/*     */   {
/* 110 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public static AsynchronousServerSocketChannel open(AsynchronousChannelGroup paramAsynchronousChannelGroup)
/*     */     throws IOException
/*     */   {
/* 138 */     AsynchronousChannelProvider localAsynchronousChannelProvider = paramAsynchronousChannelGroup == null ? AsynchronousChannelProvider.provider() : paramAsynchronousChannelGroup.provider();
/*     */ 
/* 140 */     return localAsynchronousChannelProvider.openAsynchronousServerSocketChannel(paramAsynchronousChannelGroup);
/*     */   }
/*     */ 
/*     */   public static AsynchronousServerSocketChannel open()
/*     */     throws IOException
/*     */   {
/* 161 */     return open(null);
/*     */   }
/*     */ 
/*     */   public final AsynchronousServerSocketChannel bind(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/* 188 */     return bind(paramSocketAddress, 0);
/*     */   }
/*     */ 
/*     */   public abstract AsynchronousServerSocketChannel bind(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <T> AsynchronousServerSocketChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract <A> void accept(A paramA, CompletionHandler<AsynchronousSocketChannel, ? super A> paramCompletionHandler);
/*     */ 
/*     */   public abstract Future<AsynchronousSocketChannel> accept();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.AsynchronousServerSocketChannel
 * JD-Core Version:    0.6.2
 */