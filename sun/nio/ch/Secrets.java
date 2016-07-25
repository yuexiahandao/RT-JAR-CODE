/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.io.FileDescriptor;
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.ServerSocketChannel;
/*    */ import java.nio.channels.SocketChannel;
/*    */ import java.nio.channels.spi.SelectorProvider;
/*    */ 
/*    */ public final class Secrets
/*    */ {
/*    */   private static SelectorProvider provider()
/*    */   {
/* 42 */     SelectorProvider localSelectorProvider = SelectorProvider.provider();
/* 43 */     if (!(localSelectorProvider instanceof SelectorProviderImpl))
/* 44 */       throw new UnsupportedOperationException();
/* 45 */     return localSelectorProvider;
/*    */   }
/*    */ 
/*    */   public static SocketChannel newSocketChannel(FileDescriptor paramFileDescriptor) {
/*    */     try {
/* 50 */       return new SocketChannelImpl(provider(), paramFileDescriptor, false);
/*    */     } catch (IOException localIOException) {
/* 52 */       throw new AssertionError(localIOException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static ServerSocketChannel newServerSocketChannel(FileDescriptor paramFileDescriptor) {
/*    */     try {
/* 58 */       return new ServerSocketChannelImpl(provider(), paramFileDescriptor, false);
/*    */     } catch (IOException localIOException) {
/* 60 */       throw new AssertionError(localIOException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.Secrets
 * JD-Core Version:    0.6.2
 */