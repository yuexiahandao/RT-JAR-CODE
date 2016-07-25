/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.spi.AsynchronousChannelProvider;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ public abstract class AsynchronousChannelGroup
/*     */ {
/*     */   private final AsynchronousChannelProvider provider;
/*     */ 
/*     */   protected AsynchronousChannelGroup(AsynchronousChannelProvider paramAsynchronousChannelProvider)
/*     */   {
/* 144 */     this.provider = paramAsynchronousChannelProvider;
/*     */   }
/*     */ 
/*     */   public final AsynchronousChannelProvider provider()
/*     */   {
/* 153 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public static AsynchronousChannelGroup withFixedThreadPool(int paramInt, ThreadFactory paramThreadFactory)
/*     */     throws IOException
/*     */   {
/* 186 */     return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(paramInt, paramThreadFactory);
/*     */   }
/*     */ 
/*     */   public static AsynchronousChannelGroup withCachedThreadPool(ExecutorService paramExecutorService, int paramInt)
/*     */     throws IOException
/*     */   {
/* 233 */     return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(paramExecutorService, paramInt);
/*     */   }
/*     */ 
/*     */   public static AsynchronousChannelGroup withThreadPool(ExecutorService paramExecutorService)
/*     */     throws IOException
/*     */   {
/* 273 */     return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(paramExecutorService, 0);
/*     */   }
/*     */ 
/*     */   public abstract boolean isShutdown();
/*     */ 
/*     */   public abstract boolean isTerminated();
/*     */ 
/*     */   public abstract void shutdown();
/*     */ 
/*     */   public abstract void shutdownNow()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.AsynchronousChannelGroup
 * JD-Core Version:    0.6.2
 */