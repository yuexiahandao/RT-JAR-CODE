/*    */ package sun.java2d.pipe.hw;
/*    */ 
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.GraphicsConfiguration;
/*    */ import sun.awt.image.SunVolatileImage;
/*    */ 
/*    */ public class AccelTypedVolatileImage extends SunVolatileImage
/*    */ {
/*    */   public AccelTypedVolatileImage(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 56 */     super(null, paramGraphicsConfiguration, paramInt1, paramInt2, null, paramInt3, null, paramInt4);
/*    */   }
/*    */ 
/*    */   public Graphics2D createGraphics()
/*    */   {
/* 68 */     if (getForcedAccelSurfaceType() == 3) {
/* 69 */       throw new UnsupportedOperationException("Can't render to a non-RT Texture");
/*    */     }
/*    */ 
/* 72 */     return super.createGraphics();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.hw.AccelTypedVolatileImage
 * JD-Core Version:    0.6.2
 */