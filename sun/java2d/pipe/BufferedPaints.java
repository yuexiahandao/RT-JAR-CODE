/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.LinearGradientPaint;
/*     */ import java.awt.MultipleGradientPaint.ColorSpaceType;
/*     */ import java.awt.MultipleGradientPaint.CycleMethod;
/*     */ import java.awt.Paint;
/*     */ import java.awt.RadialGradientPaint;
/*     */ import java.awt.TexturePaint;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.NoninvertibleTransformException;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import sun.awt.image.PixelConverter;
/*     */ import sun.awt.image.PixelConverter.ArgbPre;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ 
/*     */ public class BufferedPaints
/*     */ {
/*     */   public static final int MULTI_MAX_FRACTIONS = 12;
/*     */ 
/*     */   static void setPaint(RenderQueue paramRenderQueue, SunGraphics2D paramSunGraphics2D, Paint paramPaint, int paramInt)
/*     */   {
/*  54 */     if (paramSunGraphics2D.paintState <= 1) {
/*  55 */       setColor(paramRenderQueue, paramSunGraphics2D.pixel);
/*     */     } else {
/*  57 */       boolean bool = (paramInt & 0x2) != 0;
/*  58 */       switch (paramSunGraphics2D.paintState) {
/*     */       case 2:
/*  60 */         setGradientPaint(paramRenderQueue, paramSunGraphics2D, (GradientPaint)paramPaint, bool);
/*     */ 
/*  62 */         break;
/*     */       case 3:
/*  64 */         setLinearGradientPaint(paramRenderQueue, paramSunGraphics2D, (LinearGradientPaint)paramPaint, bool);
/*     */ 
/*  66 */         break;
/*     */       case 4:
/*  68 */         setRadialGradientPaint(paramRenderQueue, paramSunGraphics2D, (RadialGradientPaint)paramPaint, bool);
/*     */ 
/*  70 */         break;
/*     */       case 5:
/*  72 */         setTexturePaint(paramRenderQueue, paramSunGraphics2D, (TexturePaint)paramPaint, bool);
/*     */ 
/*  74 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void resetPaint(RenderQueue paramRenderQueue)
/*     */   {
/*  83 */     paramRenderQueue.ensureCapacity(4);
/*  84 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/*  85 */     localRenderBuffer.putInt(100);
/*     */   }
/*     */ 
/*     */   private static void setColor(RenderQueue paramRenderQueue, int paramInt)
/*     */   {
/*  92 */     paramRenderQueue.ensureCapacity(8);
/*  93 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/*  94 */     localRenderBuffer.putInt(101);
/*  95 */     localRenderBuffer.putInt(paramInt);
/*     */   }
/*     */ 
/*     */   private static void setGradientPaint(RenderQueue paramRenderQueue, AffineTransform paramAffineTransform, Color paramColor1, Color paramColor2, Point2D paramPoint2D1, Point2D paramPoint2D2, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 162 */     PixelConverter localPixelConverter = PixelConverter.ArgbPre.instance;
/* 163 */     int i = localPixelConverter.rgbToPixel(paramColor1.getRGB(), null);
/* 164 */     int j = localPixelConverter.rgbToPixel(paramColor2.getRGB(), null);
/*     */ 
/* 167 */     double d1 = paramPoint2D1.getX();
/* 168 */     double d2 = paramPoint2D1.getY();
/* 169 */     paramAffineTransform.translate(d1, d2);
/*     */ 
/* 171 */     d1 = paramPoint2D2.getX() - d1;
/* 172 */     d2 = paramPoint2D2.getY() - d2;
/* 173 */     double d3 = Math.sqrt(d1 * d1 + d2 * d2);
/* 174 */     paramAffineTransform.rotate(d1, d2);
/*     */ 
/* 176 */     paramAffineTransform.scale(2.0D * d3, 1.0D);
/*     */ 
/* 178 */     paramAffineTransform.translate(-0.25D, 0.0D);
/*     */     double d4;
/*     */     double d5;
/*     */     double d6;
/*     */     try
/*     */     {
/* 183 */       paramAffineTransform.invert();
/* 184 */       d4 = paramAffineTransform.getScaleX();
/* 185 */       d5 = paramAffineTransform.getShearX();
/* 186 */       d6 = paramAffineTransform.getTranslateX();
/*     */     } catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/* 188 */       d4 = d5 = d6 = 0.0D;
/*     */     }
/*     */ 
/* 192 */     paramRenderQueue.ensureCapacityAndAlignment(44, 12);
/* 193 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 194 */     localRenderBuffer.putInt(102);
/* 195 */     localRenderBuffer.putInt(paramBoolean2 ? 1 : 0);
/* 196 */     localRenderBuffer.putInt(paramBoolean1 ? 1 : 0);
/* 197 */     localRenderBuffer.putDouble(d4).putDouble(d5).putDouble(d6);
/* 198 */     localRenderBuffer.putInt(i).putInt(j);
/*     */   }
/*     */ 
/*     */   private static void setGradientPaint(RenderQueue paramRenderQueue, SunGraphics2D paramSunGraphics2D, GradientPaint paramGradientPaint, boolean paramBoolean)
/*     */   {
/* 206 */     setGradientPaint(paramRenderQueue, (AffineTransform)paramSunGraphics2D.transform.clone(), paramGradientPaint.getColor1(), paramGradientPaint.getColor2(), paramGradientPaint.getPoint1(), paramGradientPaint.getPoint2(), paramGradientPaint.isCyclic(), paramBoolean);
/*     */   }
/*     */ 
/*     */   private static void setTexturePaint(RenderQueue paramRenderQueue, SunGraphics2D paramSunGraphics2D, TexturePaint paramTexturePaint, boolean paramBoolean)
/*     */   {
/* 247 */     BufferedImage localBufferedImage = paramTexturePaint.getImage();
/* 248 */     SurfaceData localSurfaceData1 = paramSunGraphics2D.surfaceData;
/* 249 */     SurfaceData localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(localBufferedImage, 0, CompositeType.SrcOver, null);
/*     */ 
/* 252 */     int i = paramSunGraphics2D.interpolationType != 1 ? 1 : 0;
/*     */ 
/* 257 */     AffineTransform localAffineTransform = (AffineTransform)paramSunGraphics2D.transform.clone();
/* 258 */     Rectangle2D localRectangle2D = paramTexturePaint.getAnchorRect();
/* 259 */     localAffineTransform.translate(localRectangle2D.getX(), localRectangle2D.getY());
/* 260 */     localAffineTransform.scale(localRectangle2D.getWidth(), localRectangle2D.getHeight());
/*     */     double d1;
/*     */     double d2;
/*     */     double d3;
/*     */     double d4;
/*     */     double d5;
/*     */     double d6;
/*     */     try
/*     */     {
/* 264 */       localAffineTransform.invert();
/* 265 */       d1 = localAffineTransform.getScaleX();
/* 266 */       d2 = localAffineTransform.getShearX();
/* 267 */       d3 = localAffineTransform.getTranslateX();
/* 268 */       d4 = localAffineTransform.getShearY();
/* 269 */       d5 = localAffineTransform.getScaleY();
/* 270 */       d6 = localAffineTransform.getTranslateY();
/*     */     } catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/* 272 */       d1 = d2 = d3 = d4 = d5 = d6 = 0.0D;
/*     */     }
/*     */ 
/* 276 */     paramRenderQueue.ensureCapacityAndAlignment(68, 12);
/* 277 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 278 */     localRenderBuffer.putInt(105);
/* 279 */     localRenderBuffer.putInt(paramBoolean ? 1 : 0);
/* 280 */     localRenderBuffer.putInt(i != 0 ? 1 : 0);
/* 281 */     localRenderBuffer.putLong(localSurfaceData2.getNativeOps());
/* 282 */     localRenderBuffer.putDouble(d1).putDouble(d2).putDouble(d3);
/* 283 */     localRenderBuffer.putDouble(d4).putDouble(d5).putDouble(d6);
/*     */   }
/*     */ 
/*     */   public static int convertSRGBtoLinearRGB(int paramInt)
/*     */   {
/* 313 */     float f1 = paramInt / 255.0F;
/*     */     float f2;
/* 314 */     if (f1 <= 0.04045F)
/* 315 */       f2 = f1 / 12.92F;
/*     */     else {
/* 317 */       f2 = (float)Math.pow((f1 + 0.055D) / 1.055D, 2.4D);
/*     */     }
/*     */ 
/* 320 */     return Math.round(f2 * 255.0F);
/*     */   }
/*     */ 
/*     */   private static int colorToIntArgbPrePixel(Color paramColor, boolean paramBoolean)
/*     */   {
/* 329 */     int i = paramColor.getRGB();
/* 330 */     if ((!paramBoolean) && (i >> 24 == -1)) {
/* 331 */       return i;
/*     */     }
/* 333 */     int j = i >>> 24;
/* 334 */     int k = i >> 16 & 0xFF;
/* 335 */     int m = i >> 8 & 0xFF;
/* 336 */     int n = i & 0xFF;
/* 337 */     if (paramBoolean) {
/* 338 */       k = convertSRGBtoLinearRGB(k);
/* 339 */       m = convertSRGBtoLinearRGB(m);
/* 340 */       n = convertSRGBtoLinearRGB(n);
/*     */     }
/* 342 */     int i1 = j + (j >> 7);
/* 343 */     k = k * i1 >> 8;
/* 344 */     m = m * i1 >> 8;
/* 345 */     n = n * i1 >> 8;
/* 346 */     return j << 24 | k << 16 | m << 8 | n;
/*     */   }
/*     */ 
/*     */   private static int[] convertToIntArgbPrePixels(Color[] paramArrayOfColor, boolean paramBoolean)
/*     */   {
/* 358 */     int[] arrayOfInt = new int[paramArrayOfColor.length];
/* 359 */     for (int i = 0; i < paramArrayOfColor.length; i++) {
/* 360 */       arrayOfInt[i] = colorToIntArgbPrePixel(paramArrayOfColor[i], paramBoolean);
/*     */     }
/* 362 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static void setLinearGradientPaint(RenderQueue paramRenderQueue, SunGraphics2D paramSunGraphics2D, LinearGradientPaint paramLinearGradientPaint, boolean paramBoolean)
/*     */   {
/* 391 */     boolean bool1 = paramLinearGradientPaint.getColorSpace() == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB;
/*     */ 
/* 393 */     Color[] arrayOfColor = paramLinearGradientPaint.getColors();
/* 394 */     int i = arrayOfColor.length;
/* 395 */     Point2D localPoint2D1 = paramLinearGradientPaint.getStartPoint();
/* 396 */     Point2D localPoint2D2 = paramLinearGradientPaint.getEndPoint();
/* 397 */     AffineTransform localAffineTransform = paramLinearGradientPaint.getTransform();
/* 398 */     localAffineTransform.preConcatenate(paramSunGraphics2D.transform);
/*     */ 
/* 400 */     if ((!bool1) && (i == 2) && (paramLinearGradientPaint.getCycleMethod() != MultipleGradientPaint.CycleMethod.REPEAT))
/*     */     {
/* 404 */       boolean bool2 = paramLinearGradientPaint.getCycleMethod() != MultipleGradientPaint.CycleMethod.NO_CYCLE;
/*     */ 
/* 406 */       setGradientPaint(paramRenderQueue, localAffineTransform, arrayOfColor[0], arrayOfColor[1], localPoint2D1, localPoint2D2, bool2, paramBoolean);
/*     */ 
/* 410 */       return; } 
/*     */ int j = paramLinearGradientPaint.getCycleMethod().ordinal();
/* 414 */     float[] arrayOfFloat = paramLinearGradientPaint.getFractions();
/* 415 */     int[] arrayOfInt = convertToIntArgbPrePixels(arrayOfColor, bool1);
/*     */ 
/* 418 */     double d1 = localPoint2D1.getX();
/* 419 */     double d2 = localPoint2D1.getY();
/* 420 */     localAffineTransform.translate(d1, d2);
/*     */ 
/* 422 */     d1 = localPoint2D2.getX() - d1;
/* 423 */     d2 = localPoint2D2.getY() - d2;
/* 424 */     double d3 = Math.sqrt(d1 * d1 + d2 * d2);
/* 425 */     localAffineTransform.rotate(d1, d2);
/*     */ 
/* 427 */     localAffineTransform.scale(d3, 1.0D);
/*     */     float f1;
/*     */     float f2;
/*     */     float f3;
/*     */     try { localAffineTransform.invert();
/* 433 */       f1 = (float)localAffineTransform.getScaleX();
/* 434 */       f2 = (float)localAffineTransform.getShearX();
/* 435 */       f3 = (float)localAffineTransform.getTranslateX();
/*     */     } catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/* 437 */       f1 = f2 = f3 = 0.0F;
/*     */     }
/*     */ 
/* 441 */     paramRenderQueue.ensureCapacity(32 + i * 4 * 2);
/* 442 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 443 */     localRenderBuffer.putInt(103);
/* 444 */     localRenderBuffer.putInt(paramBoolean ? 1 : 0);
/* 445 */     localRenderBuffer.putInt(bool1 ? 1 : 0);
/* 446 */     localRenderBuffer.putInt(j);
/* 447 */     localRenderBuffer.putInt(i);
/* 448 */     localRenderBuffer.putFloat(f1);
/* 449 */     localRenderBuffer.putFloat(f2);
/* 450 */     localRenderBuffer.putFloat(f3);
/* 451 */     localRenderBuffer.put(arrayOfFloat);
/* 452 */     localRenderBuffer.put(arrayOfInt);
/*     */   }
/*     */ 
/*     */   private static void setRadialGradientPaint(RenderQueue paramRenderQueue, SunGraphics2D paramSunGraphics2D, RadialGradientPaint paramRadialGradientPaint, boolean paramBoolean)
/*     */   {
/* 474 */     boolean bool = paramRadialGradientPaint.getColorSpace() == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB;
/*     */ 
/* 476 */     int i = paramRadialGradientPaint.getCycleMethod().ordinal();
/* 477 */     float[] arrayOfFloat = paramRadialGradientPaint.getFractions();
/* 478 */     Color[] arrayOfColor = paramRadialGradientPaint.getColors();
/* 479 */     int j = arrayOfColor.length;
/* 480 */     int[] arrayOfInt = convertToIntArgbPrePixels(arrayOfColor, bool);
/* 481 */     Point2D localPoint2D1 = paramRadialGradientPaint.getCenterPoint();
/* 482 */     Point2D localPoint2D2 = paramRadialGradientPaint.getFocusPoint();
/* 483 */     float f = paramRadialGradientPaint.getRadius();
/*     */ 
/* 486 */     double d1 = localPoint2D1.getX();
/* 487 */     double d2 = localPoint2D1.getY();
/* 488 */     double d3 = localPoint2D2.getX();
/* 489 */     double d4 = localPoint2D2.getY();
/*     */ 
/* 492 */     AffineTransform localAffineTransform = paramRadialGradientPaint.getTransform();
/* 493 */     localAffineTransform.preConcatenate(paramSunGraphics2D.transform);
/* 494 */     localPoint2D2 = localAffineTransform.transform(localPoint2D2, localPoint2D2);
/*     */ 
/* 499 */     localAffineTransform.translate(d1, d2);
/* 500 */     localAffineTransform.rotate(d3 - d1, d4 - d2);
/* 501 */     localAffineTransform.scale(f, f);
/*     */     try
/*     */     {
/* 505 */       localAffineTransform.invert();
/*     */     } catch (Exception localException) {
/* 507 */       localAffineTransform.setToScale(0.0D, 0.0D);
/*     */     }
/* 509 */     localPoint2D2 = localAffineTransform.transform(localPoint2D2, localPoint2D2);
/*     */ 
/* 513 */     d3 = Math.min(localPoint2D2.getX(), 0.99D);
/*     */ 
/* 516 */     paramRenderQueue.ensureCapacity(48 + j * 4 * 2);
/* 517 */     RenderBuffer localRenderBuffer = paramRenderQueue.getBuffer();
/* 518 */     localRenderBuffer.putInt(104);
/* 519 */     localRenderBuffer.putInt(paramBoolean ? 1 : 0);
/* 520 */     localRenderBuffer.putInt(bool ? 1 : 0);
/* 521 */     localRenderBuffer.putInt(j);
/* 522 */     localRenderBuffer.putInt(i);
/* 523 */     localRenderBuffer.putFloat((float)localAffineTransform.getScaleX());
/* 524 */     localRenderBuffer.putFloat((float)localAffineTransform.getShearX());
/* 525 */     localRenderBuffer.putFloat((float)localAffineTransform.getTranslateX());
/* 526 */     localRenderBuffer.putFloat((float)localAffineTransform.getShearY());
/* 527 */     localRenderBuffer.putFloat((float)localAffineTransform.getScaleY());
/* 528 */     localRenderBuffer.putFloat((float)localAffineTransform.getTranslateY());
/* 529 */     localRenderBuffer.putFloat((float)d3);
/* 530 */     localRenderBuffer.put(arrayOfFloat);
/* 531 */     localRenderBuffer.put(arrayOfInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.BufferedPaints
 * JD-Core Version:    0.6.2
 */