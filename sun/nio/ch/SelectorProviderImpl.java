/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.ProtocolFamily;
/*    */ import java.nio.channels.DatagramChannel;
/*    */ import java.nio.channels.Pipe;
/*    */ import java.nio.channels.ServerSocketChannel;
/*    */ import java.nio.channels.SocketChannel;
/*    */ import java.nio.channels.spi.AbstractSelector;
/*    */ import java.nio.channels.spi.SelectorProvider;
/*    */ 
/*    */ public abstract class SelectorProviderImpl extends SelectorProvider
/*    */ {
/*    */   public DatagramChannel openDatagramChannel()
/*    */     throws IOException
/*    */   {
/* 42 */     return new DatagramChannelImpl(this);
/*    */   }
/*    */ 
/*    */   public DatagramChannel openDatagramChannel(ProtocolFamily paramProtocolFamily) throws IOException {
/* 46 */     return new DatagramChannelImpl(this, paramProtocolFamily);
/*    */   }
/*    */ 
/*    */   public Pipe openPipe() throws IOException {
/* 50 */     return new PipeImpl(this);
/*    */   }
/*    */ 
/*    */   public abstract AbstractSelector openSelector() throws IOException;
/*    */ 
/*    */   public ServerSocketChannel openServerSocketChannel() throws IOException {
/* 56 */     return new ServerSocketChannelImpl(this);
/*    */   }
/*    */ 
/*    */   public SocketChannel openSocketChannel() throws IOException {
/* 60 */     return new SocketChannelImpl(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SelectorProviderImpl
 * JD-Core Version:    0.6.2
 */