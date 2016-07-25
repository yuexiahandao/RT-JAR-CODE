/*      */ package sun.java2d.pipe;
/*      */ 
/*      */ import java.awt.AlphaComposite;
/*      */ import java.awt.Color;
/*      */ import java.awt.Image;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.NoninvertibleTransformException;
/*      */ import java.awt.image.AffineTransformOp;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.BufferedImageOp;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.VolatileImage;
/*      */ import sun.awt.image.BytePackedRaster;
/*      */ import sun.awt.image.ImageRepresentation;
/*      */ import sun.awt.image.SurfaceManager;
/*      */ import sun.awt.image.ToolkitImage;
/*      */ import sun.java2d.InvalidPipeException;
/*      */ import sun.java2d.SunGraphics2D;
/*      */ import sun.java2d.SurfaceData;
/*      */ import sun.java2d.loops.Blit;
/*      */ import sun.java2d.loops.BlitBg;
/*      */ import sun.java2d.loops.CompositeType;
/*      */ import sun.java2d.loops.MaskBlit;
/*      */ import sun.java2d.loops.ScaledBlit;
/*      */ import sun.java2d.loops.SurfaceType;
/*      */ import sun.java2d.loops.TransformHelper;
/*      */ 
/*      */ public class DrawImage
/*      */   implements DrawImagePipe
/*      */ {
/*      */   private static final double MAX_TX_ERROR = 0.0001D;
/*      */ 
/*      */   public boolean copyImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, Color paramColor)
/*      */   {
/*   64 */     int i = paramImage.getWidth(null);
/*   65 */     int j = paramImage.getHeight(null);
/*   66 */     if (isSimpleTranslate(paramSunGraphics2D)) {
/*   67 */       return renderImageCopy(paramSunGraphics2D, paramImage, paramColor, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, 0, 0, i, j);
/*      */     }
/*      */ 
/*   71 */     AffineTransform localAffineTransform = paramSunGraphics2D.transform;
/*   72 */     if ((paramInt1 | paramInt2) != 0) {
/*   73 */       localAffineTransform = new AffineTransform(localAffineTransform);
/*   74 */       localAffineTransform.translate(paramInt1, paramInt2);
/*      */     }
/*   76 */     transformImage(paramSunGraphics2D, paramImage, localAffineTransform, paramSunGraphics2D.interpolationType, 0, 0, i, j, paramColor);
/*      */ 
/*   78 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean copyImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Color paramColor)
/*      */   {
/*   85 */     if (isSimpleTranslate(paramSunGraphics2D)) {
/*   86 */       return renderImageCopy(paramSunGraphics2D, paramImage, paramColor, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     }
/*      */ 
/*   90 */     scaleImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramInt1 + paramInt5, paramInt2 + paramInt6, paramInt3, paramInt4, paramInt3 + paramInt5, paramInt4 + paramInt6, paramColor);
/*      */ 
/*   92 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean scaleImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*      */   {
/*   99 */     int i = paramImage.getWidth(null);
/*  100 */     int j = paramImage.getHeight(null);
/*      */ 
/*  105 */     if ((paramInt3 > 0) && (paramInt4 > 0) && (isSimpleTranslate(paramSunGraphics2D))) {
/*  106 */       double d1 = paramInt1 + paramSunGraphics2D.transX;
/*  107 */       double d2 = paramInt2 + paramSunGraphics2D.transY;
/*  108 */       double d3 = d1 + paramInt3;
/*  109 */       double d4 = d2 + paramInt4;
/*  110 */       if (renderImageScale(paramSunGraphics2D, paramImage, paramColor, paramSunGraphics2D.interpolationType, 0, 0, i, j, d1, d2, d3, d4))
/*      */       {
/*  114 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  118 */     AffineTransform localAffineTransform = paramSunGraphics2D.transform;
/*  119 */     if (((paramInt1 | paramInt2) != 0) || (paramInt3 != i) || (paramInt4 != j)) {
/*  120 */       localAffineTransform = new AffineTransform(localAffineTransform);
/*  121 */       localAffineTransform.translate(paramInt1, paramInt2);
/*  122 */       localAffineTransform.scale(paramInt3 / i, paramInt4 / j);
/*      */     }
/*  124 */     transformImage(paramSunGraphics2D, paramImage, localAffineTransform, paramSunGraphics2D.interpolationType, 0, 0, i, j, paramColor);
/*      */ 
/*  126 */     return true;
/*      */   }
/*      */ 
/*      */   protected void transformImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, AffineTransform paramAffineTransform, int paramInt3)
/*      */   {
/*  140 */     int i = paramAffineTransform.getType();
/*  141 */     int j = paramImage.getWidth(null);
/*  142 */     int k = paramImage.getHeight(null);
/*      */     int m;
/*  145 */     if ((paramSunGraphics2D.transformState <= 2) && ((i == 0) || (i == 1)))
/*      */     {
/*  152 */       double d1 = paramAffineTransform.getTranslateX();
/*  153 */       double d2 = paramAffineTransform.getTranslateY();
/*  154 */       d1 += paramSunGraphics2D.transform.getTranslateX();
/*  155 */       d2 += paramSunGraphics2D.transform.getTranslateY();
/*  156 */       int n = (int)Math.floor(d1 + 0.5D);
/*  157 */       int i1 = (int)Math.floor(d2 + 0.5D);
/*  158 */       if ((paramInt3 == 1) || ((closeToInteger(n, d1)) && (closeToInteger(i1, d2))))
/*      */       {
/*  161 */         renderImageCopy(paramSunGraphics2D, paramImage, null, paramInt1 + n, paramInt2 + i1, 0, 0, j, k);
/*  162 */         return;
/*      */       }
/*  164 */       m = 0;
/*  165 */     } else if ((paramSunGraphics2D.transformState <= 3) && ((i & 0x78) == 0))
/*      */     {
/*  177 */       localObject = new double[] { 0.0D, 0.0D, j, k };
/*      */ 
/*  180 */       paramAffineTransform.transform((double[])localObject, 0, (double[])localObject, 0, 2);
/*  181 */       localObject[0] += paramInt1;
/*  182 */       localObject[1] += paramInt2;
/*  183 */       localObject[2] += paramInt1;
/*  184 */       localObject[3] += paramInt2;
/*  185 */       paramSunGraphics2D.transform.transform((double[])localObject, 0, (double[])localObject, 0, 2);
/*      */ 
/*  187 */       if (tryCopyOrScale(paramSunGraphics2D, paramImage, 0, 0, j, k, null, paramInt3, (double[])localObject))
/*      */       {
/*  190 */         return;
/*      */       }
/*  192 */       m = 0;
/*      */     } else {
/*  194 */       m = 1;
/*      */     }
/*      */ 
/*  198 */     Object localObject = new AffineTransform(paramSunGraphics2D.transform);
/*  199 */     ((AffineTransform)localObject).translate(paramInt1, paramInt2);
/*  200 */     ((AffineTransform)localObject).concatenate(paramAffineTransform);
/*      */ 
/*  205 */     if (m != 0)
/*      */     {
/*  210 */       transformImage(paramSunGraphics2D, paramImage, (AffineTransform)localObject, paramInt3, 0, 0, j, k, null);
/*      */     }
/*  212 */     else renderImageXform(paramSunGraphics2D, paramImage, (AffineTransform)localObject, paramInt3, 0, 0, j, k, null);
/*      */   }
/*      */ 
/*      */   protected void transformImage(SunGraphics2D paramSunGraphics2D, Image paramImage, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Color paramColor)
/*      */   {
/*  244 */     double[] arrayOfDouble = new double[6];
/*      */ 
/*  247 */     arrayOfDouble[2] = (paramInt4 - paramInt2);
/*      */     double tmp28_27 = (paramInt5 - paramInt3); arrayOfDouble[5] = tmp28_27; arrayOfDouble[3] = tmp28_27;
/*  249 */     paramAffineTransform.transform(arrayOfDouble, 0, arrayOfDouble, 0, 3);
/*      */ 
/*  256 */     if ((Math.abs(arrayOfDouble[0] - arrayOfDouble[4]) < 0.0001D) && (Math.abs(arrayOfDouble[3] - arrayOfDouble[5]) < 0.0001D) && (tryCopyOrScale(paramSunGraphics2D, paramImage, paramInt2, paramInt3, paramInt4, paramInt5, paramColor, paramInt1, arrayOfDouble)))
/*      */     {
/*  261 */       return;
/*      */     }
/*      */ 
/*  264 */     renderImageXform(paramSunGraphics2D, paramImage, paramAffineTransform, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramColor);
/*      */   }
/*      */ 
/*      */   protected boolean tryCopyOrScale(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, int paramInt5, double[] paramArrayOfDouble)
/*      */   {
/*  281 */     double d1 = paramArrayOfDouble[0];
/*  282 */     double d2 = paramArrayOfDouble[1];
/*  283 */     double d3 = paramArrayOfDouble[2] - d1;
/*  284 */     double d4 = paramArrayOfDouble[3] - d2;
/*      */ 
/*  286 */     if ((closeToInteger(paramInt3 - paramInt1, d3)) && (closeToInteger(paramInt4 - paramInt2, d4)))
/*      */     {
/*  289 */       int i = (int)Math.floor(d1 + 0.5D);
/*  290 */       int j = (int)Math.floor(d2 + 0.5D);
/*  291 */       if ((paramInt5 == 1) || ((closeToInteger(i, d1)) && (closeToInteger(j, d2))))
/*      */       {
/*  294 */         renderImageCopy(paramSunGraphics2D, paramImage, paramColor, i, j, paramInt1, paramInt2, paramInt3 - paramInt1, paramInt4 - paramInt2);
/*      */ 
/*  297 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  302 */     if ((d3 > 0.0D) && (d4 > 0.0D) && 
/*  303 */       (renderImageScale(paramSunGraphics2D, paramImage, paramColor, paramInt5, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfDouble[0], paramArrayOfDouble[1], paramArrayOfDouble[2], paramArrayOfDouble[3])))
/*      */     {
/*  307 */       return true;
/*      */     }
/*      */ 
/*  310 */     return false;
/*      */   }
/*      */ 
/*      */   BufferedImage makeBufferedImage(Image paramImage, Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  322 */     int i = paramInt4 - paramInt2;
/*  323 */     int j = paramInt5 - paramInt3;
/*  324 */     BufferedImage localBufferedImage = new BufferedImage(i, j, paramInt1);
/*  325 */     SunGraphics2D localSunGraphics2D = (SunGraphics2D)localBufferedImage.createGraphics();
/*  326 */     localSunGraphics2D.setComposite(AlphaComposite.Src);
/*  327 */     if (paramColor != null) {
/*  328 */       localSunGraphics2D.setColor(paramColor);
/*  329 */       localSunGraphics2D.fillRect(0, 0, i, j);
/*  330 */       localSunGraphics2D.setComposite(AlphaComposite.SrcOver);
/*      */     }
/*  332 */     localSunGraphics2D.copyImage(paramImage, 0, 0, paramInt2, paramInt3, i, j, null, null);
/*  333 */     localSunGraphics2D.dispose();
/*  334 */     return localBufferedImage;
/*      */   }
/*      */ 
/*      */   protected void renderImageXform(SunGraphics2D paramSunGraphics2D, Image paramImage, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Color paramColor)
/*      */   {
/*  342 */     Region localRegion = paramSunGraphics2D.getCompClip();
/*  343 */     SurfaceData localSurfaceData1 = paramSunGraphics2D.surfaceData;
/*  344 */     SurfaceData localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(paramImage, 4, paramSunGraphics2D.imageComp, paramColor);
/*      */ 
/*  349 */     if (localSurfaceData2 == null) {
/*  350 */       paramImage = getBufferedImage(paramImage);
/*  351 */       localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(paramImage, 4, paramSunGraphics2D.imageComp, paramColor);
/*      */ 
/*  355 */       if (localSurfaceData2 == null)
/*      */       {
/*  357 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  361 */     if (isBgOperation(localSurfaceData2, paramColor))
/*      */     {
/*  365 */       paramImage = makeBufferedImage(paramImage, paramColor, 1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */ 
/*  368 */       paramInt4 -= paramInt2;
/*  369 */       paramInt5 -= paramInt3;
/*  370 */       paramInt2 = paramInt3 = 0;
/*      */ 
/*  372 */       localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(paramImage, 4, paramSunGraphics2D.imageComp, paramColor);
/*      */     }
/*      */ 
/*  378 */     SurfaceType localSurfaceType1 = localSurfaceData2.getSurfaceType();
/*  379 */     TransformHelper localTransformHelper = TransformHelper.getFromCache(localSurfaceType1);
/*      */ 
/*  381 */     if (localTransformHelper == null)
/*      */     {
/*  389 */       int i = localSurfaceData2.getTransparency() == 1 ? 1 : 2;
/*      */ 
/*  392 */       paramImage = makeBufferedImage(paramImage, null, i, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */ 
/*  394 */       paramInt4 -= paramInt2;
/*  395 */       paramInt5 -= paramInt3;
/*  396 */       paramInt2 = paramInt3 = 0;
/*      */ 
/*  398 */       localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(paramImage, 4, paramSunGraphics2D.imageComp, null);
/*      */ 
/*  402 */       localSurfaceType1 = localSurfaceData2.getSurfaceType();
/*  403 */       localTransformHelper = TransformHelper.getFromCache(localSurfaceType1);
/*      */     }
/*      */ 
/*      */     AffineTransform localAffineTransform;
/*      */     try
/*      */     {
/*  409 */       localAffineTransform = paramAffineTransform.createInverse();
/*      */     }
/*      */     catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/*  412 */       return;
/*      */     }
/*      */ 
/*  424 */     double[] arrayOfDouble = new double[8];
/*      */     double tmp260_259 = (paramInt4 - paramInt2); arrayOfDouble[6] = tmp260_259; arrayOfDouble[2] = tmp260_259;
/*      */     double tmp276_275 = (paramInt5 - paramInt3); arrayOfDouble[7] = tmp276_275; arrayOfDouble[5] = tmp276_275;
/*  430 */     paramAffineTransform.transform(arrayOfDouble, 0, arrayOfDouble, 0, 4);
/*      */     double d3;
/*  432 */     double d1 = d3 = arrayOfDouble[0];
/*      */     double d4;
/*  433 */     double d2 = d4 = arrayOfDouble[1];
/*  434 */     for (int j = 2; j < arrayOfDouble.length; j += 2) {
/*  435 */       double d5 = arrayOfDouble[j];
/*  436 */       if (d1 > d5) d1 = d5;
/*  437 */       else if (d3 < d5) d3 = d5;
/*  438 */       d5 = arrayOfDouble[(j + 1)];
/*  439 */       if (d2 > d5) d2 = d5;
/*  440 */       else if (d4 < d5) d4 = d5;
/*      */     }
/*  442 */     j = (int)Math.floor(d1);
/*  443 */     int k = (int)Math.floor(d2);
/*  444 */     int m = (int)Math.ceil(d3);
/*  445 */     int n = (int)Math.ceil(d4);
/*      */ 
/*  447 */     SurfaceType localSurfaceType2 = localSurfaceData1.getSurfaceType();
/*      */     MaskBlit localMaskBlit1;
/*      */     Blit localBlit;
/*  450 */     if (paramSunGraphics2D.compositeState <= 1)
/*      */     {
/*  454 */       localMaskBlit1 = MaskBlit.getFromCache(SurfaceType.IntArgbPre, paramSunGraphics2D.imageComp, localSurfaceType2);
/*      */ 
/*  464 */       if (localMaskBlit1.getNativePrim() != 0L)
/*      */       {
/*  466 */         localTransformHelper.Transform(localMaskBlit1, localSurfaceData2, localSurfaceData1, paramSunGraphics2D.composite, localRegion, localAffineTransform, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, j, k, m, n, null, 0, 0);
/*      */ 
/*  472 */         return;
/*      */       }
/*  474 */       localBlit = null;
/*      */     }
/*      */     else
/*      */     {
/*  479 */       localMaskBlit1 = null;
/*  480 */       localBlit = Blit.getFromCache(SurfaceType.IntArgbPre, paramSunGraphics2D.imageComp, localSurfaceType2);
/*      */     }
/*      */ 
/*  487 */     BufferedImage localBufferedImage = new BufferedImage(m - j, n - k, 2);
/*      */ 
/*  489 */     SurfaceData localSurfaceData3 = SurfaceData.getPrimarySurfaceData(localBufferedImage);
/*  490 */     SurfaceType localSurfaceType3 = localSurfaceData3.getSurfaceType();
/*  491 */     MaskBlit localMaskBlit2 = MaskBlit.getFromCache(SurfaceType.IntArgbPre, CompositeType.SrcNoEa, localSurfaceType3);
/*      */ 
/*  509 */     int[] arrayOfInt = new int[(n - k) * 2 + 2];
/*      */ 
/*  513 */     localTransformHelper.Transform(localMaskBlit2, localSurfaceData2, localSurfaceData3, AlphaComposite.Src, null, localAffineTransform, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, 0, 0, m - j, n - k, arrayOfInt, j, k);
/*      */ 
/*  524 */     int i1 = 2;
/*  525 */     for (int i2 = arrayOfInt[0]; i2 < arrayOfInt[1]; i2++) {
/*  526 */       int i3 = arrayOfInt[(i1++)];
/*  527 */       int i4 = arrayOfInt[(i1++)];
/*  528 */       if (i3 < i4)
/*      */       {
/*  531 */         if (localMaskBlit1 != null) {
/*  532 */           localMaskBlit1.MaskBlit(localSurfaceData3, localSurfaceData1, paramSunGraphics2D.composite, localRegion, i3, i2, j + i3, k + i2, i4 - i3, 1, null, 0, 0);
/*      */         }
/*      */         else
/*      */         {
/*  539 */           localBlit.Blit(localSurfaceData3, localSurfaceData1, paramSunGraphics2D.composite, localRegion, i3, i2, j + i3, k + i2, i4 - i3, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean renderImageCopy(SunGraphics2D paramSunGraphics2D, Image paramImage, Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  556 */     Region localRegion = paramSunGraphics2D.getCompClip();
/*  557 */     SurfaceData localSurfaceData1 = paramSunGraphics2D.surfaceData;
/*      */ 
/*  559 */     int i = 0;
/*      */     while (true)
/*      */     {
/*  564 */       SurfaceData localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(paramImage, 0, paramSunGraphics2D.imageComp, paramColor);
/*      */ 
/*  569 */       if (localSurfaceData2 == null) {
/*  570 */         return false;
/*      */       }
/*      */       try
/*      */       {
/*  574 */         SurfaceType localSurfaceType1 = localSurfaceData2.getSurfaceType();
/*  575 */         SurfaceType localSurfaceType2 = localSurfaceData1.getSurfaceType();
/*  576 */         blitSurfaceData(paramSunGraphics2D, localRegion, localSurfaceData2, localSurfaceData1, localSurfaceType1, localSurfaceType2, paramInt3, paramInt4, paramInt1, paramInt2, paramInt5, paramInt6, paramColor);
/*      */ 
/*  579 */         return true;
/*      */       } catch (NullPointerException localNullPointerException) {
/*  581 */         if ((!SurfaceData.isNull(localSurfaceData1)) && (!SurfaceData.isNull(localSurfaceData2)))
/*      */         {
/*  585 */           throw localNullPointerException;
/*      */         }
/*  587 */         return false;
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException)
/*      */       {
/*  593 */         i++;
/*  594 */         localRegion = paramSunGraphics2D.getCompClip();
/*  595 */         localSurfaceData1 = paramSunGraphics2D.surfaceData;
/*  596 */         if ((SurfaceData.isNull(localSurfaceData1)) || (SurfaceData.isNull(localSurfaceData2)) || (i > 1))
/*      */         {
/*  599 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean renderImageScale(SunGraphics2D paramSunGraphics2D, Image paramImage, Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/*  615 */     if (paramInt1 != 1) {
/*  616 */       return false;
/*      */     }
/*      */ 
/*  619 */     Region localRegion = paramSunGraphics2D.getCompClip();
/*  620 */     SurfaceData localSurfaceData1 = paramSunGraphics2D.surfaceData;
/*      */ 
/*  622 */     int i = 0;
/*      */     while (true)
/*      */     {
/*  627 */       SurfaceData localSurfaceData2 = localSurfaceData1.getSourceSurfaceData(paramImage, 3, paramSunGraphics2D.imageComp, paramColor);
/*      */ 
/*  633 */       if ((localSurfaceData2 == null) || (isBgOperation(localSurfaceData2, paramColor))) {
/*  634 */         return false;
/*      */       }
/*      */       try
/*      */       {
/*  638 */         SurfaceType localSurfaceType1 = localSurfaceData2.getSurfaceType();
/*  639 */         SurfaceType localSurfaceType2 = localSurfaceData1.getSurfaceType();
/*  640 */         return scaleSurfaceData(paramSunGraphics2D, localRegion, localSurfaceData2, localSurfaceData1, localSurfaceType1, localSurfaceType2, paramInt2, paramInt3, paramInt4, paramInt5, paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*      */       }
/*      */       catch (NullPointerException localNullPointerException)
/*      */       {
/*  645 */         if (!SurfaceData.isNull(localSurfaceData1))
/*      */         {
/*  647 */           throw localNullPointerException;
/*      */         }
/*  649 */         return false;
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException)
/*      */       {
/*  655 */         i++;
/*  656 */         localRegion = paramSunGraphics2D.getCompClip();
/*  657 */         localSurfaceData1 = paramSunGraphics2D.surfaceData;
/*  658 */         if ((SurfaceData.isNull(localSurfaceData1)) || (SurfaceData.isNull(localSurfaceData2)) || (i > 1))
/*      */         {
/*  661 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean scaleImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor)
/*      */   {
/*  674 */     int i4 = 0;
/*  675 */     int i5 = 0;
/*  676 */     int i6 = 0;
/*  677 */     int i7 = 0;
/*      */     int i;
/*      */     int n;
/*  679 */     if (paramInt7 > paramInt5) {
/*  680 */       i = paramInt7 - paramInt5;
/*  681 */       n = paramInt5;
/*      */     } else {
/*  683 */       i4 = 1;
/*  684 */       i = paramInt5 - paramInt7;
/*  685 */       n = paramInt7;
/*      */     }
/*      */     int j;
/*      */     int i1;
/*  687 */     if (paramInt8 > paramInt6) {
/*  688 */       j = paramInt8 - paramInt6;
/*  689 */       i1 = paramInt6;
/*      */     } else {
/*  691 */       i5 = 1;
/*  692 */       j = paramInt6 - paramInt8;
/*  693 */       i1 = paramInt8;
/*      */     }
/*      */     int k;
/*      */     int i2;
/*  695 */     if (paramInt3 > paramInt1) {
/*  696 */       k = paramInt3 - paramInt1;
/*  697 */       i2 = paramInt1;
/*      */     } else {
/*  699 */       k = paramInt1 - paramInt3;
/*  700 */       i6 = 1;
/*  701 */       i2 = paramInt3;
/*      */     }
/*      */     int m;
/*      */     int i3;
/*  703 */     if (paramInt4 > paramInt2) {
/*  704 */       m = paramInt4 - paramInt2;
/*  705 */       i3 = paramInt2;
/*      */     } else {
/*  707 */       m = paramInt2 - paramInt4;
/*  708 */       i7 = 1;
/*  709 */       i3 = paramInt4;
/*      */     }
/*  711 */     if ((i <= 0) || (j <= 0)) {
/*  712 */       return true;
/*      */     }
/*      */ 
/*  715 */     if ((i4 == i6) && (i5 == i7) && (isSimpleTranslate(paramSunGraphics2D)))
/*      */     {
/*  719 */       double d1 = i2 + paramSunGraphics2D.transX;
/*  720 */       double d3 = i3 + paramSunGraphics2D.transY;
/*  721 */       double d5 = d1 + k;
/*  722 */       double d6 = d3 + m;
/*  723 */       if (renderImageScale(paramSunGraphics2D, paramImage, paramColor, paramSunGraphics2D.interpolationType, n, i1, n + i, i1 + j, d1, d3, d5, d6))
/*      */       {
/*  727 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  731 */     AffineTransform localAffineTransform = new AffineTransform(paramSunGraphics2D.transform);
/*  732 */     localAffineTransform.translate(paramInt1, paramInt2);
/*  733 */     double d2 = (paramInt3 - paramInt1) / (paramInt7 - paramInt5);
/*  734 */     double d4 = (paramInt4 - paramInt2) / (paramInt8 - paramInt6);
/*  735 */     localAffineTransform.scale(d2, d4);
/*  736 */     localAffineTransform.translate(n - paramInt5, i1 - paramInt6);
/*      */ 
/*  738 */     int i8 = SurfaceManager.getImageScale(paramImage);
/*  739 */     int i9 = paramImage.getWidth(null) * i8;
/*  740 */     int i10 = paramImage.getHeight(null) * i8;
/*  741 */     i += n;
/*  742 */     j += i1;
/*      */ 
/*  744 */     if (i > i9) {
/*  745 */       i = i9;
/*      */     }
/*  747 */     if (j > i10) {
/*  748 */       j = i10;
/*      */     }
/*  750 */     if (n < 0) {
/*  751 */       localAffineTransform.translate(-n, 0.0D);
/*  752 */       n = 0;
/*      */     }
/*  754 */     if (i1 < 0) {
/*  755 */       localAffineTransform.translate(0.0D, -i1);
/*  756 */       i1 = 0;
/*      */     }
/*  758 */     if ((n >= i) || (i1 >= j)) {
/*  759 */       return true;
/*      */     }
/*      */ 
/*  769 */     transformImage(paramSunGraphics2D, paramImage, localAffineTransform, paramSunGraphics2D.interpolationType, n, i1, i, j, paramColor);
/*      */ 
/*  771 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean closeToInteger(int paramInt, double paramDouble)
/*      */   {
/*  797 */     return Math.abs(paramDouble - paramInt) < 0.0001D;
/*      */   }
/*      */ 
/*      */   public static boolean isSimpleTranslate(SunGraphics2D paramSunGraphics2D) {
/*  801 */     int i = paramSunGraphics2D.transformState;
/*  802 */     if (i <= 1)
/*      */     {
/*  804 */       return true;
/*      */     }
/*  806 */     if (i >= 3)
/*      */     {
/*  808 */       return false;
/*      */     }
/*      */ 
/*  811 */     if (paramSunGraphics2D.interpolationType == 1) {
/*  812 */       return true;
/*      */     }
/*  814 */     return false;
/*      */   }
/*      */ 
/*      */   protected static boolean isBgOperation(SurfaceData paramSurfaceData, Color paramColor)
/*      */   {
/*  820 */     return (paramSurfaceData == null) || ((paramColor != null) && (paramSurfaceData.getTransparency() != 1));
/*      */   }
/*      */ 
/*      */   protected BufferedImage getBufferedImage(Image paramImage)
/*      */   {
/*  826 */     if ((paramImage instanceof BufferedImage)) {
/*  827 */       return (BufferedImage)paramImage;
/*      */     }
/*      */ 
/*  830 */     return ((VolatileImage)paramImage).getSnapshot();
/*      */   }
/*      */ 
/*      */   private ColorModel getTransformColorModel(SunGraphics2D paramSunGraphics2D, BufferedImage paramBufferedImage, AffineTransform paramAffineTransform)
/*      */   {
/*  840 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*  841 */     Object localObject1 = localColorModel;
/*      */ 
/*  843 */     if (paramAffineTransform.isIdentity()) {
/*  844 */       return localObject1;
/*      */     }
/*  846 */     int i = paramAffineTransform.getType();
/*  847 */     int j = (i & (0x18 | 0x20)) != 0 ? 1 : 0;
/*      */     Object localObject2;
/*  849 */     if ((j == 0) && (i != 1) && (i != 0))
/*      */     {
/*  851 */       localObject2 = new double[4];
/*  852 */       paramAffineTransform.getMatrix((double[])localObject2);
/*      */ 
/*  855 */       j = (localObject2[0] != (int)localObject2[0]) || (localObject2[3] != (int)localObject2[3]) ? 1 : 0;
/*      */     }
/*      */ 
/*  858 */     if (paramSunGraphics2D.renderHint != 2) {
/*  859 */       if ((localColorModel instanceof IndexColorModel)) {
/*  860 */         localObject2 = paramBufferedImage.getRaster();
/*  861 */         IndexColorModel localIndexColorModel = (IndexColorModel)localColorModel;
/*      */ 
/*  863 */         if ((j != 0) && (localColorModel.getTransparency() == 1))
/*      */         {
/*  865 */           if ((localObject2 instanceof BytePackedRaster)) {
/*  866 */             localObject1 = ColorModel.getRGBdefault();
/*      */           }
/*      */           else {
/*  869 */             double[] arrayOfDouble = new double[6];
/*  870 */             paramAffineTransform.getMatrix(arrayOfDouble);
/*  871 */             if ((arrayOfDouble[1] != 0.0D) || (arrayOfDouble[2] != 0.0D) || (arrayOfDouble[4] != 0.0D) || (arrayOfDouble[5] != 0.0D))
/*      */             {
/*  876 */               int k = localIndexColorModel.getMapSize();
/*  877 */               if (k < 256) {
/*  878 */                 int[] arrayOfInt = new int[k + 1];
/*  879 */                 localIndexColorModel.getRGBs(arrayOfInt);
/*  880 */                 arrayOfInt[k] = 0;
/*  881 */                 localObject1 = new IndexColorModel(localIndexColorModel.getPixelSize(), k + 1, arrayOfInt, 0, true, k, 0);
/*      */               }
/*      */               else
/*      */               {
/*  888 */                 localObject1 = ColorModel.getRGBdefault();
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  894 */       else if ((j != 0) && (localColorModel.getTransparency() == 1))
/*      */       {
/*  898 */         localObject1 = ColorModel.getRGBdefault();
/*      */       }
/*      */ 
/*      */     }
/*  903 */     else if (((localColorModel instanceof IndexColorModel)) || ((j != 0) && (localColorModel.getTransparency() == 1)))
/*      */     {
/*  909 */       localObject1 = ColorModel.getRGBdefault();
/*      */     }
/*      */ 
/*  913 */     return localObject1;
/*      */   }
/*      */ 
/*      */   protected void blitSurfaceData(SunGraphics2D paramSunGraphics2D, Region paramRegion, SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, SurfaceType paramSurfaceType1, SurfaceType paramSurfaceType2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Color paramColor)
/*      */   {
/*  926 */     if ((paramInt5 <= 0) || (paramInt6 <= 0))
/*      */     {
/*  943 */       return;
/*      */     }
/*  945 */     CompositeType localCompositeType = paramSunGraphics2D.imageComp;
/*  946 */     if ((CompositeType.SrcOverNoEa.equals(localCompositeType)) && ((paramSurfaceData1.getTransparency() == 1) || ((paramColor != null) && (paramColor.getTransparency() == 1))))
/*      */     {
/*  951 */       localCompositeType = CompositeType.SrcNoEa;
/*      */     }
/*      */     Object localObject;
/*  953 */     if (!isBgOperation(paramSurfaceData1, paramColor)) {
/*  954 */       localObject = Blit.getFromCache(paramSurfaceType1, localCompositeType, paramSurfaceType2);
/*  955 */       ((Blit)localObject).Blit(paramSurfaceData1, paramSurfaceData2, paramSunGraphics2D.composite, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     }
/*      */     else {
/*  958 */       localObject = BlitBg.getFromCache(paramSurfaceType1, localCompositeType, paramSurfaceType2);
/*  959 */       ((BlitBg)localObject).BlitBg(paramSurfaceData1, paramSurfaceData2, paramSunGraphics2D.composite, paramRegion, paramColor.getRGB(), paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean scaleSurfaceData(SunGraphics2D paramSunGraphics2D, Region paramRegion, SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, SurfaceType paramSurfaceType1, SurfaceType paramSurfaceType2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/*  975 */     CompositeType localCompositeType = paramSunGraphics2D.imageComp;
/*  976 */     if ((CompositeType.SrcOverNoEa.equals(localCompositeType)) && (paramSurfaceData1.getTransparency() == 1))
/*      */     {
/*  979 */       localCompositeType = CompositeType.SrcNoEa;
/*      */     }
/*      */ 
/*  982 */     ScaledBlit localScaledBlit = ScaledBlit.getFromCache(paramSurfaceType1, localCompositeType, paramSurfaceType2);
/*  983 */     if (localScaledBlit != null) {
/*  984 */       localScaledBlit.Scale(paramSurfaceData1, paramSurfaceData2, paramSunGraphics2D.composite, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*      */ 
/*  986 */       return true;
/*      */     }
/*  988 */     return false;
/*      */   }
/*      */ 
/*      */   protected static boolean imageReady(ToolkitImage paramToolkitImage, ImageObserver paramImageObserver)
/*      */   {
/*  994 */     if (paramToolkitImage.hasError()) {
/*  995 */       if (paramImageObserver != null) {
/*  996 */         paramImageObserver.imageUpdate(paramToolkitImage, 192, -1, -1, -1, -1);
/*      */       }
/*      */ 
/* 1000 */       return false;
/*      */     }
/* 1002 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean copyImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1009 */     if (!(paramImage instanceof ToolkitImage)) {
/* 1010 */       return copyImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramColor);
/*      */     }
/* 1012 */     ToolkitImage localToolkitImage = (ToolkitImage)paramImage;
/* 1013 */     if (!imageReady(localToolkitImage, paramImageObserver)) {
/* 1014 */       return false;
/*      */     }
/* 1016 */     ImageRepresentation localImageRepresentation = localToolkitImage.getImageRep();
/* 1017 */     return localImageRepresentation.drawToBufImage(paramSunGraphics2D, localToolkitImage, paramInt1, paramInt2, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean copyImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1025 */     if (!(paramImage instanceof ToolkitImage)) {
/* 1026 */       return copyImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramColor);
/*      */     }
/* 1028 */     ToolkitImage localToolkitImage = (ToolkitImage)paramImage;
/* 1029 */     if (!imageReady(localToolkitImage, paramImageObserver)) {
/* 1030 */       return false;
/*      */     }
/* 1032 */     ImageRepresentation localImageRepresentation = localToolkitImage.getImageRep();
/* 1033 */     return localImageRepresentation.drawToBufImage(paramSunGraphics2D, localToolkitImage, paramInt1, paramInt2, paramInt1 + paramInt5, paramInt2 + paramInt6, paramInt3, paramInt4, paramInt3 + paramInt5, paramInt4 + paramInt6, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean scaleImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1045 */     if (!(paramImage instanceof ToolkitImage)) {
/* 1046 */       return scaleImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor);
/*      */     }
/* 1048 */     ToolkitImage localToolkitImage = (ToolkitImage)paramImage;
/* 1049 */     if (!imageReady(localToolkitImage, paramImageObserver)) {
/* 1050 */       return false;
/*      */     }
/* 1052 */     ImageRepresentation localImageRepresentation = localToolkitImage.getImageRep();
/* 1053 */     return localImageRepresentation.drawToBufImage(paramSunGraphics2D, localToolkitImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean scaleImage(SunGraphics2D paramSunGraphics2D, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1063 */     if (!(paramImage instanceof ToolkitImage)) {
/* 1064 */       return scaleImage(paramSunGraphics2D, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor);
/*      */     }
/*      */ 
/* 1067 */     ToolkitImage localToolkitImage = (ToolkitImage)paramImage;
/* 1068 */     if (!imageReady(localToolkitImage, paramImageObserver)) {
/* 1069 */       return false;
/*      */     }
/* 1071 */     ImageRepresentation localImageRepresentation = localToolkitImage.getImageRep();
/* 1072 */     return localImageRepresentation.drawToBufImage(paramSunGraphics2D, localToolkitImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean transformImage(SunGraphics2D paramSunGraphics2D, Image paramImage, AffineTransform paramAffineTransform, ImageObserver paramImageObserver)
/*      */   {
/* 1080 */     if (!(paramImage instanceof ToolkitImage)) {
/* 1081 */       transformImage(paramSunGraphics2D, paramImage, 0, 0, paramAffineTransform, paramSunGraphics2D.interpolationType);
/* 1082 */       return true;
/*      */     }
/* 1084 */     ToolkitImage localToolkitImage = (ToolkitImage)paramImage;
/* 1085 */     if (!imageReady(localToolkitImage, paramImageObserver)) {
/* 1086 */       return false;
/*      */     }
/* 1088 */     ImageRepresentation localImageRepresentation = localToolkitImage.getImageRep();
/* 1089 */     return localImageRepresentation.drawToBufImage(paramSunGraphics2D, localToolkitImage, paramAffineTransform, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public void transformImage(SunGraphics2D paramSunGraphics2D, BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp, int paramInt1, int paramInt2)
/*      */   {
/* 1096 */     if (paramBufferedImageOp != null) {
/* 1097 */       if ((paramBufferedImageOp instanceof AffineTransformOp)) {
/* 1098 */         AffineTransformOp localAffineTransformOp = (AffineTransformOp)paramBufferedImageOp;
/* 1099 */         transformImage(paramSunGraphics2D, paramBufferedImage, paramInt1, paramInt2, localAffineTransformOp.getTransform(), localAffineTransformOp.getInterpolationType());
/*      */ 
/* 1102 */         return;
/*      */       }
/* 1104 */       paramBufferedImage = paramBufferedImageOp.filter(paramBufferedImage, null);
/*      */     }
/*      */ 
/* 1107 */     copyImage(paramSunGraphics2D, paramBufferedImage, paramInt1, paramInt2, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.DrawImage
 * JD-Core Version:    0.6.2
 */