/*    */ package sun.java2d.pipe;
/*    */ 
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.Shape;
/*    */ import sun.java2d.SunGraphics2D;
/*    */ import sun.java2d.loops.MaskFill;
/*    */ 
/*    */ public class AlphaColorPipe
/*    */   implements CompositePipe, ParallelogramPipe
/*    */ {
/*    */   public Object startSequence(SunGraphics2D paramSunGraphics2D, Shape paramShape, Rectangle paramRectangle, int[] paramArrayOfInt)
/*    */   {
/* 43 */     return paramSunGraphics2D;
/*    */   }
/*    */ 
/*    */   public boolean needTile(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   public void renderPathTile(Object paramObject, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*    */   {
/* 53 */     SunGraphics2D localSunGraphics2D = (SunGraphics2D)paramObject;
/*    */ 
/* 55 */     localSunGraphics2D.alphafill.MaskFill(localSunGraphics2D, localSunGraphics2D.getSurfaceData(), localSunGraphics2D.composite, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public void skipTile(Object paramObject, int paramInt1, int paramInt2)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void endSequence(Object paramObject)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void fillParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10)
/*    */   {
/* 75 */     paramSunGraphics2D.alphafill.FillAAPgram(paramSunGraphics2D, paramSunGraphics2D.getSurfaceData(), paramSunGraphics2D.composite, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10);
/*    */   }
/*    */ 
/*    */   public void drawParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12)
/*    */   {
/* 87 */     paramSunGraphics2D.alphafill.DrawAAPgram(paramSunGraphics2D, paramSunGraphics2D.getSurfaceData(), paramSunGraphics2D.composite, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, paramDouble11, paramDouble12);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.AlphaColorPipe
 * JD-Core Version:    0.6.2
 */