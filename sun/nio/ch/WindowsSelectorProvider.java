/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.spi.AbstractSelector;
/*    */ 
/*    */ public class WindowsSelectorProvider extends SelectorProviderImpl
/*    */ {
/*    */   public AbstractSelector openSelector()
/*    */     throws IOException
/*    */   {
/* 44 */     return new WindowsSelectorImpl(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.WindowsSelectorProvider
 * JD-Core Version:    0.6.2
 */