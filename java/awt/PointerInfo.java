/*    */ package java.awt;
/*    */ 
/*    */ public class PointerInfo
/*    */ {
/*    */   private GraphicsDevice device;
/*    */   private Point location;
/*    */ 
/*    */   PointerInfo(GraphicsDevice paramGraphicsDevice, Point paramPoint)
/*    */   {
/* 54 */     this.device = paramGraphicsDevice;
/* 55 */     this.location = paramPoint;
/*    */   }
/*    */ 
/*    */   public GraphicsDevice getDevice()
/*    */   {
/* 66 */     return this.device;
/*    */   }
/*    */ 
/*    */   public Point getLocation()
/*    */   {
/* 81 */     return this.location;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.PointerInfo
 * JD-Core Version:    0.6.2
 */