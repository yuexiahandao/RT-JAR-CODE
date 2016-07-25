/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.nio.channels.spi.SelectorProvider;
/*    */ 
/*    */ public class DefaultSelectorProvider
/*    */ {
/*    */   public static SelectorProvider create()
/*    */   {
/* 46 */     return new WindowsSelectorProvider();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.DefaultSelectorProvider
 * JD-Core Version:    0.6.2
 */