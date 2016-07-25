/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.AffineTransformOp;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.loops.TransformBlit;
/*     */ import sun.java2d.pipe.DrawImage;
/*     */ 
/*     */ public class OGLDrawImage extends DrawImage
/*     */ {
/*     */   protected void renderImageXform(SunGraphics2D paramSunGraphics2D, Image paramImage, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Color paramColor)
/*     */   {
/*  59 */     if (paramInt1 != 3) {
/*  60 */       SurfaceData localSurfaceData1 = paramSunGraphics2D.surfaceData;
/*  61 */       SurfaceData localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(paramImage, 4, paramSunGraphics2D.imageComp, paramColor);
/*     */ 
/*  67 */       if ((localSurfaceData2 != null) && (!isBgOperation(localSurfaceData2, paramColor)) && ((localSurfaceData2.getSurfaceType() == OGLSurfaceData.OpenGLTexture) || (localSurfaceData2.getSurfaceType() == OGLSurfaceData.OpenGLSurfaceRTT) || (paramInt1 == 1)))
/*     */       {
/*  73 */         SurfaceType localSurfaceType1 = localSurfaceData2.getSurfaceType();
/*  74 */         SurfaceType localSurfaceType2 = localSurfaceData1.getSurfaceType();
/*  75 */         TransformBlit localTransformBlit = TransformBlit.getFromCache(localSurfaceType1, paramSunGraphics2D.imageComp, localSurfaceType2);
/*     */ 
/*  79 */         if (localTransformBlit != null) {
/*  80 */           localTransformBlit.Transform(localSurfaceData2, localSurfaceData1, paramSunGraphics2D.composite, paramSunGraphics2D.getCompClip(), paramAffineTransform, paramInt1, paramInt2, paramInt3, 0, 0, paramInt4 - paramInt2, paramInt5 - paramInt3);
/*     */ 
/*  84 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  89 */     super.renderImageXform(paramSunGraphics2D, paramImage, paramAffineTransform, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramColor);
/*     */   }
/*     */ 
/*     */   public void transformImage(SunGraphics2D paramSunGraphics2D, BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp, int paramInt1, int paramInt2)
/*     */   {
/*  97 */     if (paramBufferedImageOp != null) {
/*  98 */       if ((paramBufferedImageOp instanceof AffineTransformOp)) {
/*  99 */         AffineTransformOp localAffineTransformOp = (AffineTransformOp)paramBufferedImageOp;
/* 100 */         transformImage(paramSunGraphics2D, paramBufferedImage, paramInt1, paramInt2, localAffineTransformOp.getTransform(), localAffineTransformOp.getInterpolationType());
/*     */ 
/* 103 */         return;
/*     */       }
/* 105 */       if (OGLBufImgOps.renderImageWithOp(paramSunGraphics2D, paramBufferedImage, paramBufferedImageOp, paramInt1, paramInt2)) {
/* 106 */         return;
/*     */       }
/*     */ 
/* 109 */       paramBufferedImage = paramBufferedImageOp.filter(paramBufferedImage, null);
/*     */     }
/* 111 */     copyImage(paramSunGraphics2D, paramBufferedImage, paramInt1, paramInt2, null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLDrawImage
 * JD-Core Version:    0.6.2
 */