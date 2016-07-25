/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.GraphicsDevice;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.peer.RobotPeer;
/*    */ 
/*    */ class WRobotPeer extends WObjectPeer
/*    */   implements RobotPeer
/*    */ {
/*    */   WRobotPeer()
/*    */   {
/* 34 */     create();
/*    */   }
/*    */   WRobotPeer(GraphicsDevice paramGraphicsDevice) {
/* 37 */     create();
/*    */   }
/*    */ 
/*    */   private synchronized native void _dispose();
/*    */ 
/*    */   protected void disposeImpl() {
/* 43 */     _dispose();
/*    */   }
/*    */   public native void create();
/*    */ 
/*    */   public native void mouseMoveImpl(int paramInt1, int paramInt2);
/*    */ 
/* 49 */   public void mouseMove(int paramInt1, int paramInt2) { mouseMoveImpl(paramInt1, paramInt2); } 
/*    */   public native void mousePress(int paramInt);
/*    */ 
/*    */   public native void mouseRelease(int paramInt);
/*    */ 
/*    */   public native void mouseWheel(int paramInt);
/*    */ 
/*    */   public native void keyPress(int paramInt);
/*    */ 
/*    */   public native void keyRelease(int paramInt);
/*    */ 
/* 59 */   public int getRGBPixel(int paramInt1, int paramInt2) { return getRGBPixelImpl(paramInt1, paramInt2); }
/*    */ 
/*    */   public native int getRGBPixelImpl(int paramInt1, int paramInt2);
/*    */ 
/*    */   public int[] getRGBPixels(Rectangle paramRectangle) {
/* 64 */     int[] arrayOfInt = new int[paramRectangle.width * paramRectangle.height];
/* 65 */     getRGBPixels(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, arrayOfInt);
/* 66 */     return arrayOfInt;
/*    */   }
/*    */ 
/*    */   private native void getRGBPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WRobotPeer
 * JD-Core Version:    0.6.2
 */