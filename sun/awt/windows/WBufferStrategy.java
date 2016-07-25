/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Image;
/*    */ 
/*    */ public class WBufferStrategy
/*    */ {
/*    */   private static native void initIDs(Class paramClass);
/*    */ 
/*    */   public static native Image getDrawBuffer(Component paramComponent);
/*    */ 
/*    */   static
/*    */   {
/* 43 */     initIDs(Component.class);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WBufferStrategy
 * JD-Core Version:    0.6.2
 */