/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.nio.channels.spi.AsynchronousChannelProvider;
/*    */ 
/*    */ public class DefaultAsynchronousChannelProvider
/*    */ {
/*    */   public static AsynchronousChannelProvider create()
/*    */   {
/* 41 */     return new WindowsAsynchronousChannelProvider();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.DefaultAsynchronousChannelProvider
 * JD-Core Version:    0.6.2
 */