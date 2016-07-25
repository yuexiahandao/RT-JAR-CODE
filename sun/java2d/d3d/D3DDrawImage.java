/*    */ package sun.java2d.d3d;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Image;
/*    */ import java.awt.geom.AffineTransform;
/*    */ import java.awt.image.AffineTransformOp;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.BufferedImageOp;
/*    */ import sun.java2d.SunGraphics2D;
/*    */ import sun.java2d.SurfaceData;
/*    */ import sun.java2d.loops.SurfaceType;
/*    */ import sun.java2d.loops.TransformBlit;
/*    */ import sun.java2d.pipe.DrawImage;
/*    */ 
/*    */ public class D3DDrawImage extends DrawImage
/*    */ {
/*    */   protected void renderImageXform(SunGraphics2D paramSunGraphics2D, Image paramImage, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Color paramColor)
/*    */   {
/* 52 */     if (paramInt1 != 3) {
/* 53 */       SurfaceData localSurfaceData1 = paramSunGraphics2D.surfaceData;
/* 54 */       SurfaceData localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(paramImage, 4, paramSunGraphics2D.imageComp, paramColor);
/*    */ 
/* 60 */       if ((localSurfaceData2 != null) && (!isBgOperation(localSurfaceData2, paramColor))) {
/* 61 */         SurfaceType localSurfaceType1 = localSurfaceData2.getSurfaceType();
/* 62 */         SurfaceType localSurfaceType2 = localSurfaceData1.getSurfaceType();
/* 63 */         TransformBlit localTransformBlit = TransformBlit.getFromCache(localSurfaceType1, paramSunGraphics2D.imageComp, localSurfaceType2);
/*    */ 
/* 67 */         if (localTransformBlit != null) {
/* 68 */           localTransformBlit.Transform(localSurfaceData2, localSurfaceData1, paramSunGraphics2D.composite, paramSunGraphics2D.getCompClip(), paramAffineTransform, paramInt1, paramInt2, paramInt3, 0, 0, paramInt4 - paramInt2, paramInt5 - paramInt3);
/*    */ 
/* 72 */           return;
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 77 */     super.renderImageXform(paramSunGraphics2D, paramImage, paramAffineTransform, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramColor);
/*    */   }
/*    */ 
/*    */   public void transformImage(SunGraphics2D paramSunGraphics2D, BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp, int paramInt1, int paramInt2)
/*    */   {
/* 85 */     if (paramBufferedImageOp != null) {
/* 86 */       if ((paramBufferedImageOp instanceof AffineTransformOp)) {
/* 87 */         AffineTransformOp localAffineTransformOp = (AffineTransformOp)paramBufferedImageOp;
/* 88 */         transformImage(paramSunGraphics2D, paramBufferedImage, paramInt1, paramInt2, localAffineTransformOp.getTransform(), localAffineTransformOp.getInterpolationType());
/*    */ 
/* 91 */         return;
/*    */       }
/* 93 */       if (D3DBufImgOps.renderImageWithOp(paramSunGraphics2D, paramBufferedImage, paramBufferedImageOp, paramInt1, paramInt2)) {
/* 94 */         return;
/*    */       }
/*    */ 
/* 97 */       paramBufferedImage = paramBufferedImageOp.filter(paramBufferedImage, null);
/*    */     }
/* 99 */     copyImage(paramSunGraphics2D, paramBufferedImage, paramInt1, paramInt2, null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DDrawImage
 * JD-Core Version:    0.6.2
 */