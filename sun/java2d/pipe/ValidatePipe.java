/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import java.awt.image.ImageObserver;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ 
/*     */ public class ValidatePipe
/*     */   implements PixelDrawPipe, PixelFillPipe, ShapeDrawPipe, TextPipe, DrawImagePipe
/*     */ {
/*     */   public boolean validate(SunGraphics2D paramSunGraphics2D)
/*     */   {
/*  55 */     paramSunGraphics2D.validatePipe();
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */   public void drawLine(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  61 */     if (validate(paramSunGraphics2D))
/*  62 */       paramSunGraphics2D.drawpipe.drawLine(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void drawRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  68 */     if (validate(paramSunGraphics2D))
/*  69 */       paramSunGraphics2D.drawpipe.drawRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void fillRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  75 */     if (validate(paramSunGraphics2D))
/*  76 */       paramSunGraphics2D.fillpipe.fillRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void drawRoundRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*  83 */     if (validate(paramSunGraphics2D))
/*  84 */       paramSunGraphics2D.drawpipe.drawRoundRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void fillRoundRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*  92 */     if (validate(paramSunGraphics2D))
/*  93 */       paramSunGraphics2D.fillpipe.fillRoundRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void drawOval(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 100 */     if (validate(paramSunGraphics2D))
/* 101 */       paramSunGraphics2D.drawpipe.drawOval(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void fillOval(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 107 */     if (validate(paramSunGraphics2D))
/* 108 */       paramSunGraphics2D.fillpipe.fillOval(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void drawArc(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 115 */     if (validate(paramSunGraphics2D))
/* 116 */       paramSunGraphics2D.drawpipe.drawArc(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void fillArc(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 123 */     if (validate(paramSunGraphics2D))
/* 124 */       paramSunGraphics2D.fillpipe.fillArc(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void drawPolyline(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/* 131 */     if (validate(paramSunGraphics2D))
/* 132 */       paramSunGraphics2D.drawpipe.drawPolyline(paramSunGraphics2D, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*     */   }
/*     */ 
/*     */   public void drawPolygon(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/* 139 */     if (validate(paramSunGraphics2D))
/* 140 */       paramSunGraphics2D.drawpipe.drawPolygon(paramSunGraphics2D, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*     */   }
/*     */ 
/*     */   public void fillPolygon(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/* 147 */     if (validate(paramSunGraphics2D))
/* 148 */       paramSunGraphics2D.fillpipe.fillPolygon(paramSunGraphics2D, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*     */   }
/*     */ 
/*     */   public void draw(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/* 153 */     if (validate(paramSunGraphics2D))
/* 154 */       paramSunGraphics2D.shapepipe.draw(paramSunGraphics2D, paramShape);
/*     */   }
/*     */ 
/*     */   public void fill(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/* 159 */     if (validate(paramSunGraphics2D))
/* 160 */       paramSunGraphics2D.shapepipe.fill(paramSunGraphics2D, paramShape);
/*     */   }
/*     */ 
/*     */   public void drawString(SunGraphics2D paramSunGraphics2D, String paramString, double paramDouble1, double paramDouble2) {
/* 164 */     if (validate(paramSunGraphics2D))
/* 165 */       paramSunGraphics2D.textpipe.drawString(paramSunGraphics2D, paramString, paramDouble1, paramDouble2);
/*     */   }
/*     */ 
/*     */   public void drawGlyphVector(SunGraphics2D paramSunGraphics2D, GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*     */   {
/* 170 */     if (validate(paramSunGraphics2D))
/* 171 */       paramSunGraphics2D.textpipe.drawGlyphVector(paramSunGraphics2D, paramGlyphVector, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   public void drawChars(SunGraphics2D paramSunGraphics2D, char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 177 */     if (validate(paramSunGraphics2D))
/* 178 */       paramSunGraphics2D.textpipe.drawChars(paramSunGraphics2D, paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public boolean copyImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver)
/*     */   {
/* 185 */     if (validate(paramSunGraphics2D)) {
/* 186 */       return paramSunGraphics2D.imagepipe.copyImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramColor, paramImageObserver);
/*     */     }
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean copyImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Color paramColor, ImageObserver paramImageObserver)
/*     */   {
/* 195 */     if (validate(paramSunGraphics2D)) {
/* 196 */       return paramSunGraphics2D.imagepipe.copyImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramColor, paramImageObserver);
/*     */     }
/*     */ 
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean scaleImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver)
/*     */   {
/* 206 */     if (validate(paramSunGraphics2D)) {
/* 207 */       return paramSunGraphics2D.imagepipe.scaleImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, paramImageObserver);
/*     */     }
/*     */ 
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean scaleImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*     */   {
/* 218 */     if (validate(paramSunGraphics2D)) {
/* 219 */       return paramSunGraphics2D.imagepipe.scaleImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*     */     }
/*     */ 
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean transformImage(SunGraphics2D paramSunGraphics2D, Image paramImage, AffineTransform paramAffineTransform, ImageObserver paramImageObserver)
/*     */   {
/* 229 */     if (validate(paramSunGraphics2D)) {
/* 230 */       return paramSunGraphics2D.imagepipe.transformImage(paramSunGraphics2D, paramImage, paramAffineTransform, paramImageObserver);
/*     */     }
/* 232 */     return false;
/*     */   }
/*     */ 
/*     */   public void transformImage(SunGraphics2D paramSunGraphics2D, BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp, int paramInt1, int paramInt2)
/*     */   {
/* 237 */     if (validate(paramSunGraphics2D))
/* 238 */       paramSunGraphics2D.imagepipe.transformImage(paramSunGraphics2D, paramBufferedImage, paramBufferedImageOp, paramInt1, paramInt2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.ValidatePipe
 * JD-Core Version:    0.6.2
 */