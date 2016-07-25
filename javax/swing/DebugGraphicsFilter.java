/*    */ package javax.swing;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.image.RGBImageFilter;
/*    */ 
/*    */ class DebugGraphicsFilter extends RGBImageFilter
/*    */ {
/*    */   Color color;
/*    */ 
/*    */   DebugGraphicsFilter(Color paramColor)
/*    */   {
/* 39 */     this.canFilterIndexColorModel = true;
/* 40 */     this.color = paramColor;
/*    */   }
/*    */ 
/*    */   public int filterRGB(int paramInt1, int paramInt2, int paramInt3) {
/* 44 */     return this.color.getRGB() | paramInt3 & 0xFF000000;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DebugGraphicsFilter
 * JD-Core Version:    0.6.2
 */