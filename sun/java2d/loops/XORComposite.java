/*    */ package sun.java2d.loops;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Composite;
/*    */ import java.awt.CompositeContext;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.image.ColorModel;
/*    */ import sun.java2d.SunCompositeContext;
/*    */ import sun.java2d.SurfaceData;
/*    */ 
/*    */ public final class XORComposite
/*    */   implements Composite
/*    */ {
/*    */   Color xorColor;
/*    */   int xorPixel;
/*    */   int alphaMask;
/*    */ 
/*    */   public XORComposite(Color paramColor, SurfaceData paramSurfaceData)
/*    */   {
/* 47 */     this.xorColor = paramColor;
/*    */ 
/* 49 */     SurfaceType localSurfaceType = paramSurfaceData.getSurfaceType();
/*    */ 
/* 51 */     this.xorPixel = paramSurfaceData.pixelFor(paramColor.getRGB());
/* 52 */     this.alphaMask = localSurfaceType.getAlphaMask();
/*    */   }
/*    */ 
/*    */   public Color getXorColor() {
/* 56 */     return this.xorColor;
/*    */   }
/*    */ 
/*    */   public int getXorPixel() {
/* 60 */     return this.xorPixel;
/*    */   }
/*    */ 
/*    */   public int getAlphaMask() {
/* 64 */     return this.alphaMask;
/*    */   }
/*    */ 
/*    */   public CompositeContext createContext(ColorModel paramColorModel1, ColorModel paramColorModel2, RenderingHints paramRenderingHints)
/*    */   {
/* 70 */     return new SunCompositeContext(this, paramColorModel1, paramColorModel2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.XORComposite
 * JD-Core Version:    0.6.2
 */