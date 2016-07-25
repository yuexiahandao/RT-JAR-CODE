/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
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
/*     */ abstract class D3DPaints
/*     */ {
/*  47 */   private static Map<Integer, D3DPaints> impls = new HashMap(4, 1.0F);
/*     */ 
/*     */   static boolean isValid(SunGraphics2D paramSunGraphics2D)
/*     */   {
/*  65 */     D3DPaints localD3DPaints = (D3DPaints)impls.get(Integer.valueOf(paramSunGraphics2D.paintState));
/*  66 */     return (localD3DPaints != null) && (localD3DPaints.isPaintValid(paramSunGraphics2D));
/*     */   }
/*     */ 
/*     */   abstract boolean isPaintValid(SunGraphics2D paramSunGraphics2D);
/*     */ 
/*     */   static
/*     */   {
/*  51 */     impls.put(Integer.valueOf(2), new Gradient(null));
/*  52 */     impls.put(Integer.valueOf(3), new LinearGradient(null));
/*  53 */     impls.put(Integer.valueOf(4), new RadialGradient(null));
/*  54 */     impls.put(Integer.valueOf(5), new Texture(null));
/*     */   }
/*     */ 
/*     */   private static class Gradient extends D3DPaints
/*     */   {
/*     */     boolean isPaintValid(SunGraphics2D paramSunGraphics2D)
/*     */     {
/*  89 */       D3DSurfaceData localD3DSurfaceData = (D3DSurfaceData)paramSunGraphics2D.surfaceData;
/*  90 */       D3DGraphicsDevice localD3DGraphicsDevice = (D3DGraphicsDevice)localD3DSurfaceData.getDeviceConfiguration().getDevice();
/*     */ 
/*  92 */       return localD3DGraphicsDevice.isCapPresent(65536);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LinearGradient extends D3DPaints.MultiGradient
/*     */   {
/*     */     boolean isPaintValid(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 208 */       LinearGradientPaint localLinearGradientPaint = (LinearGradientPaint)paramSunGraphics2D.paint;
/*     */ 
/* 210 */       if ((localLinearGradientPaint.getFractions().length == 2) && (localLinearGradientPaint.getCycleMethod() != MultipleGradientPaint.CycleMethod.REPEAT) && (localLinearGradientPaint.getColorSpace() != MultipleGradientPaint.ColorSpaceType.LINEAR_RGB))
/*     */       {
/* 214 */         D3DSurfaceData localD3DSurfaceData = (D3DSurfaceData)paramSunGraphics2D.surfaceData;
/* 215 */         D3DGraphicsDevice localD3DGraphicsDevice = (D3DGraphicsDevice)localD3DSurfaceData.getDeviceConfiguration().getDevice();
/*     */ 
/* 217 */         if (localD3DGraphicsDevice.isCapPresent(65536))
/*     */         {
/* 220 */           return true;
/*     */         }
/*     */       }
/*     */ 
/* 224 */       return super.isPaintValid(paramSunGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class MultiGradient extends D3DPaints
/*     */   {
/*     */     public static final int MULTI_MAX_FRACTIONS_D3D = 8;
/*     */ 
/*     */     boolean isPaintValid(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 184 */       MultipleGradientPaint localMultipleGradientPaint = (MultipleGradientPaint)paramSunGraphics2D.paint;
/*     */ 
/* 187 */       if (localMultipleGradientPaint.getFractions().length > 8) {
/* 188 */         return false;
/*     */       }
/*     */ 
/* 191 */       D3DSurfaceData localD3DSurfaceData = (D3DSurfaceData)paramSunGraphics2D.surfaceData;
/* 192 */       D3DGraphicsDevice localD3DGraphicsDevice = (D3DGraphicsDevice)localD3DSurfaceData.getDeviceConfiguration().getDevice();
/*     */ 
/* 194 */       if (!localD3DGraphicsDevice.isCapPresent(65536)) {
/* 195 */         return false;
/*     */       }
/* 197 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class RadialGradient extends D3DPaints.MultiGradient
/*     */   {
/*     */   }
/*     */ 
/*     */   private static class Texture extends D3DPaints
/*     */   {
/*     */     public boolean isPaintValid(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 113 */       TexturePaint localTexturePaint = (TexturePaint)paramSunGraphics2D.paint;
/* 114 */       D3DSurfaceData localD3DSurfaceData1 = (D3DSurfaceData)paramSunGraphics2D.surfaceData;
/* 115 */       BufferedImage localBufferedImage = localTexturePaint.getImage();
/*     */ 
/* 118 */       D3DGraphicsDevice localD3DGraphicsDevice = (D3DGraphicsDevice)localD3DSurfaceData1.getDeviceConfiguration().getDevice();
/*     */ 
/* 120 */       int i = localBufferedImage.getWidth();
/* 121 */       int j = localBufferedImage.getHeight();
/* 122 */       if ((!localD3DGraphicsDevice.isCapPresent(32)) && (
/* 123 */         ((i & i - 1) != 0) || ((j & j - 1) != 0))) {
/* 124 */         return false;
/*     */       }
/*     */ 
/* 128 */       if ((!localD3DGraphicsDevice.isCapPresent(64)) && (i != j))
/*     */       {
/* 130 */         return false;
/*     */       }
/*     */ 
/* 133 */       SurfaceData localSurfaceData = localD3DSurfaceData1.getSourceSurfaceData(localBufferedImage, 0, CompositeType.SrcOver, null);
/*     */ 
/* 136 */       if (!(localSurfaceData instanceof D3DSurfaceData))
/*     */       {
/* 140 */         localSurfaceData = localD3DSurfaceData1.getSourceSurfaceData(localBufferedImage, 0, CompositeType.SrcOver, null);
/*     */ 
/* 143 */         if (!(localSurfaceData instanceof D3DSurfaceData)) {
/* 144 */           return false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 149 */       D3DSurfaceData localD3DSurfaceData2 = (D3DSurfaceData)localSurfaceData;
/* 150 */       if (localD3DSurfaceData2.getType() != 3) {
/* 151 */         return false;
/*     */       }
/*     */ 
/* 154 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DPaints
 * JD-Core Version:    0.6.2
 */