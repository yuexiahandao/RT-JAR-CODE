/*    */ package sun.awt.event;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.event.PaintEvent;
/*    */ 
/*    */ public class IgnorePaintEvent extends PaintEvent
/*    */ {
/*    */   public IgnorePaintEvent(Component paramComponent, int paramInt, Rectangle paramRectangle)
/*    */   {
/* 40 */     super(paramComponent, paramInt, paramRectangle);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.event.IgnorePaintEvent
 * JD-Core Version:    0.6.2
 */