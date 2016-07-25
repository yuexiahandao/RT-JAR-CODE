/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.AsynchronousChannelGroup;
/*    */ import java.nio.channels.AsynchronousServerSocketChannel;
/*    */ import java.nio.channels.AsynchronousSocketChannel;
/*    */ import java.nio.channels.IllegalChannelGroupException;
/*    */ import java.nio.channels.spi.AsynchronousChannelProvider;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ 
/*    */ public class WindowsAsynchronousChannelProvider extends AsynchronousChannelProvider
/*    */ {
/*    */   private static volatile Iocp defaultIocp;
/*    */ 
/*    */   private Iocp defaultIocp()
/*    */     throws IOException
/*    */   {
/* 44 */     if (defaultIocp == null) {
/* 45 */       synchronized (WindowsAsynchronousChannelProvider.class) {
/* 46 */         if (defaultIocp == null)
/*    */         {
/* 48 */           defaultIocp = new Iocp(this, ThreadPool.getDefault()).start();
/*    */         }
/*    */       }
/*    */     }
/* 52 */     return defaultIocp;
/*    */   }
/*    */ 
/*    */   public AsynchronousChannelGroup openAsynchronousChannelGroup(int paramInt, ThreadFactory paramThreadFactory)
/*    */     throws IOException
/*    */   {
/* 59 */     return new Iocp(this, ThreadPool.create(paramInt, paramThreadFactory)).start();
/*    */   }
/*    */ 
/*    */   public AsynchronousChannelGroup openAsynchronousChannelGroup(ExecutorService paramExecutorService, int paramInt)
/*    */     throws IOException
/*    */   {
/* 66 */     return new Iocp(this, ThreadPool.wrap(paramExecutorService, paramInt)).start();
/*    */   }
/*    */ 
/*    */   private Iocp toIocp(AsynchronousChannelGroup paramAsynchronousChannelGroup) throws IOException {
/* 70 */     if (paramAsynchronousChannelGroup == null) {
/* 71 */       return defaultIocp();
/*    */     }
/* 73 */     if (!(paramAsynchronousChannelGroup instanceof Iocp))
/* 74 */       throw new IllegalChannelGroupException();
/* 75 */     return (Iocp)paramAsynchronousChannelGroup;
/*    */   }
/*    */ 
/*    */   public AsynchronousServerSocketChannel openAsynchronousServerSocketChannel(AsynchronousChannelGroup paramAsynchronousChannelGroup)
/*    */     throws IOException
/*    */   {
/* 83 */     return new WindowsAsynchronousServerSocketChannelImpl(toIocp(paramAsynchronousChannelGroup));
/*    */   }
/*    */ 
/*    */   public AsynchronousSocketChannel openAsynchronousSocketChannel(AsynchronousChannelGroup paramAsynchronousChannelGroup)
/*    */     throws IOException
/*    */   {
/* 90 */     return new WindowsAsynchronousSocketChannelImpl(toIocp(paramAsynchronousChannelGroup));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.WindowsAsynchronousChannelProvider
 * JD-Core Version:    0.6.2
 */