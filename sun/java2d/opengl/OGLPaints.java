/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.LinearGradientPaint;
/*     */ import java.awt.MultipleGradientPaint;
/*     */ import java.awt.MultipleGradientPaint.ColorSpaceType;
/*     */ import java.awt.MultipleGradientPaint.CycleMethod;
/*     */ import java.awt.TexturePaint;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ 
/*     */ abstract class OGLPaints
/*     */ {
/*  50 */   private static Map<Integer, OGLPaints> impls = new HashMap(4, 1.0F);
/*     */ 
/*     */   static boolean isValid(SunGraphics2D paramSunGraphics2D)
/*     */   {
/*  68 */     OGLPaints localOGLPaints = (OGLPaints)impls.get(Integer.valueOf(paramSunGraphics2D.paintState));
/*  69 */     return (localOGLPaints != null) && (localOGLPaints.isPaintValid(paramSunGraphics2D));
/*     */   }
/*     */ 
/*     */   abstract boolean isPaintValid(SunGraphics2D paramSunGraphics2D);
/*     */ 
/*     */   static
/*     */   {
/*  54 */     impls.put(Integer.valueOf(2), new Gradient(null));
/*  55 */     impls.put(Integer.valueOf(3), new LinearGradient(null));
/*  56 */     impls.put(Integer.valueOf(4), new RadialGradient(null));
/*  57 */     impls.put(Integer.valueOf(5), new Texture(null));
/*     */   }
/*     */ 
/*     */   private static class Gradient extends OGLPaints
/*     */   {
/*     */     boolean isPaintValid(SunGraphics2D paramSunGraphics2D)
/*     */     {
/*  90 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LinearGradient extends OGLPaints.MultiGradient
/*     */   {
/*     */     boolean isPaintValid(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 189 */       LinearGradientPaint localLinearGradientPaint = (LinearGradientPaint)paramSunGraphics2D.paint;
/*     */ 
/* 191 */       if ((localLinearGradientPaint.getFractions().length == 2) && (localLinearGradientPaint.getCycleMethod() != MultipleGradientPaint.CycleMethod.REPEAT) && (localLinearGradientPaint.getColorSpace() != MultipleGradientPaint.ColorSpaceType.LINEAR_RGB))
/*     */       {
/* 197 */         return true;
/*     */       }
/*     */ 
/* 200 */       return super.isPaintValid(paramSunGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class MultiGradient extends OGLPaints
/*     */   {
/*     */     boolean isPaintValid(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 165 */       MultipleGradientPaint localMultipleGradientPaint = (MultipleGradientPaint)paramSunGraphics2D.paint;
/*     */ 
/* 168 */       if (localMultipleGradientPaint.getFractions().length > 12) {
/* 169 */         return false;
/*     */       }
/*     */ 
/* 172 */       OGLSurfaceData localOGLSurfaceData = (OGLSurfaceData)paramSunGraphics2D.surfaceData;
/* 173 */       OGLGraphicsConfig localOGLGraphicsConfig = localOGLSurfaceData.getOGLGraphicsConfig();
/* 174 */       if (!localOGLGraphicsConfig.isCapPresent(524288)) {
/* 175 */         return false;
/*     */       }
/*     */ 
/* 178 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class RadialGradient extends OGLPaints.MultiGradient
/*     */   {
/*     */   }
/*     */ 
/*     */   private static class Texture extends OGLPaints
/*     */   {
/*     */     boolean isPaintValid(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 110 */       TexturePaint localTexturePaint = (TexturePaint)paramSunGraphics2D.paint;
/* 111 */       OGLSurfaceData localOGLSurfaceData1 = (OGLSurfaceData)paramSunGraphics2D.surfaceData;
/* 112 */       BufferedImage localBufferedImage = localTexturePaint.getImage();
/*     */ 
/* 115 */       if (!localOGLSurfaceData1.isTexNonPow2Available()) {
/* 116 */         int i = localBufferedImage.getWidth();
/* 117 */         int j = localBufferedImage.getHeight();
/*     */ 
/* 120 */         if (((i & i - 1) != 0) || ((j & j - 1) != 0)) {
/* 121 */           return false;
/*     */         }
/*     */       }
/*     */ 
/* 125 */       SurfaceData localSurfaceData = localOGLSurfaceData1.getSourceSurfaceData(localBufferedImage, 0, CompositeType.SrcOver, null);
/*     */ 
/* 128 */       if (!(localSurfaceData instanceof OGLSurfaceData))
/*     */       {
/* 132 */         localSurfaceData = localOGLSurfaceData1.getSourceSurfaceData(localBufferedImage, 0, CompositeType.SrcOver, null);
/*     */ 
/* 135 */         if (!(localSurfaceData instanceof OGLSurfaceData)) {
/* 136 */           return false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 141 */       OGLSurfaceData localOGLSurfaceData2 = (OGLSurfaceData)localSurfaceData;
/* 142 */       if (localOGLSurfaceData2.getType() != 3) {
/* 143 */         return false;
/*     */       }
/*     */ 
/* 146 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLPaints
 * JD-Core Version:    0.6.2
 */