/*     */ package java.awt.image;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import sun.awt.image.ImagingLib;
/*     */ 
/*     */ public class AffineTransformOp
/*     */   implements BufferedImageOp, RasterOp
/*     */ {
/*     */   private AffineTransform xform;
/*     */   RenderingHints hints;
/*     */   public static final int TYPE_NEAREST_NEIGHBOR = 1;
/*     */   public static final int TYPE_BILINEAR = 2;
/*     */   public static final int TYPE_BICUBIC = 3;
/*  84 */   int interpolationType = 1;
/*     */ 
/*     */   public AffineTransformOp(AffineTransform paramAffineTransform, RenderingHints paramRenderingHints)
/*     */   {
/* 107 */     validateTransform(paramAffineTransform);
/* 108 */     this.xform = ((AffineTransform)paramAffineTransform.clone());
/* 109 */     this.hints = paramRenderingHints;
/*     */ 
/* 111 */     if (paramRenderingHints != null) {
/* 112 */       Object localObject = paramRenderingHints.get(RenderingHints.KEY_INTERPOLATION);
/* 113 */       if (localObject == null) {
/* 114 */         localObject = paramRenderingHints.get(RenderingHints.KEY_RENDERING);
/* 115 */         if (localObject == RenderingHints.VALUE_RENDER_SPEED) {
/* 116 */           this.interpolationType = 1;
/*     */         }
/* 118 */         else if (localObject == RenderingHints.VALUE_RENDER_QUALITY) {
/* 119 */           this.interpolationType = 2;
/*     */         }
/*     */       }
/* 122 */       else if (localObject == RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR) {
/* 123 */         this.interpolationType = 1;
/*     */       }
/* 125 */       else if (localObject == RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
/* 126 */         this.interpolationType = 2;
/*     */       }
/* 128 */       else if (localObject == RenderingHints.VALUE_INTERPOLATION_BICUBIC) {
/* 129 */         this.interpolationType = 3;
/*     */       }
/*     */     }
/*     */     else {
/* 133 */       this.interpolationType = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public AffineTransformOp(AffineTransform paramAffineTransform, int paramInt)
/*     */   {
/* 150 */     validateTransform(paramAffineTransform);
/* 151 */     this.xform = ((AffineTransform)paramAffineTransform.clone());
/* 152 */     switch (paramInt) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 156 */       break;
/*     */     default:
/* 158 */       throw new IllegalArgumentException("Unknown interpolation type: " + paramInt);
/*     */     }
/*     */ 
/* 161 */     this.interpolationType = paramInt;
/*     */   }
/*     */ 
/*     */   public final int getInterpolationType()
/*     */   {
/* 172 */     return this.interpolationType;
/*     */   }
/*     */ 
/*     */   public final BufferedImage filter(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2)
/*     */   {
/* 212 */     if (paramBufferedImage1 == null) {
/* 213 */       throw new NullPointerException("src image is null");
/*     */     }
/* 215 */     if (paramBufferedImage1 == paramBufferedImage2) {
/* 216 */       throw new IllegalArgumentException("src image cannot be the same as the dst image");
/*     */     }
/*     */ 
/* 220 */     int i = 0;
/* 221 */     ColorModel localColorModel1 = paramBufferedImage1.getColorModel();
/*     */ 
/* 223 */     BufferedImage localBufferedImage1 = paramBufferedImage2;
/*     */     ColorModel localColorModel2;
/* 225 */     if (paramBufferedImage2 == null) {
/* 226 */       paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/* 227 */       localColorModel2 = localColorModel1;
/* 228 */       localBufferedImage1 = paramBufferedImage2;
/*     */     }
/*     */     else {
/* 231 */       localColorModel2 = paramBufferedImage2.getColorModel();
/* 232 */       if (localColorModel1.getColorSpace().getType() != localColorModel2.getColorSpace().getType())
/*     */       {
/* 235 */         int j = this.xform.getType();
/* 236 */         int k = (j & (0x18 | 0x20)) != 0 ? 1 : 0;
/*     */         Object localObject2;
/* 240 */         if ((k == 0) && (j != 1) && (j != 0))
/*     */         {
/* 242 */           localObject2 = new double[4];
/* 243 */           this.xform.getMatrix((double[])localObject2);
/*     */ 
/* 246 */           k = (localObject2[0] != (int)localObject2[0]) || (localObject2[3] != (int)localObject2[3]) ? 1 : 0;
/*     */         }
/*     */ 
/* 249 */         if ((k != 0) && (localColorModel1.getTransparency() == 1))
/*     */         {
/* 253 */           localObject2 = new ColorConvertOp(this.hints);
/* 254 */           BufferedImage localBufferedImage2 = null;
/* 255 */           int m = paramBufferedImage1.getWidth();
/* 256 */           int n = paramBufferedImage1.getHeight();
/* 257 */           if (localColorModel2.getTransparency() == 1) {
/* 258 */             localBufferedImage2 = new BufferedImage(m, n, 2);
/*     */           }
/*     */           else
/*     */           {
/* 262 */             WritableRaster localWritableRaster = localColorModel2.createCompatibleWritableRaster(m, n);
/*     */ 
/* 264 */             localBufferedImage2 = new BufferedImage(localColorModel2, localWritableRaster, localColorModel2.isAlphaPremultiplied(), null);
/*     */           }
/*     */ 
/* 268 */           paramBufferedImage1 = ((ColorConvertOp)localObject2).filter(paramBufferedImage1, localBufferedImage2);
/*     */         }
/*     */         else {
/* 271 */           i = 1;
/* 272 */           paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 278 */     if ((this.interpolationType != 1) && ((paramBufferedImage2.getColorModel() instanceof IndexColorModel)))
/*     */     {
/* 280 */       paramBufferedImage2 = new BufferedImage(paramBufferedImage2.getWidth(), paramBufferedImage2.getHeight(), 2);
/*     */     }
/*     */ 
/* 283 */     if (ImagingLib.filter(this, paramBufferedImage1, paramBufferedImage2) == null)
/* 284 */       throw new ImagingOpException("Unable to transform src image");
/*     */     Object localObject1;
/* 287 */     if (i != 0) {
/* 288 */       localObject1 = new ColorConvertOp(this.hints);
/* 289 */       ((ColorConvertOp)localObject1).filter(paramBufferedImage2, localBufferedImage1);
/*     */     }
/* 291 */     else if (localBufferedImage1 != paramBufferedImage2) {
/* 292 */       localObject1 = localBufferedImage1.createGraphics();
/*     */       try {
/* 294 */         ((Graphics2D)localObject1).setComposite(AlphaComposite.Src);
/* 295 */         ((Graphics2D)localObject1).drawImage(paramBufferedImage2, 0, 0, null);
/*     */       } finally {
/* 297 */         ((Graphics2D)localObject1).dispose();
/*     */       }
/*     */     }
/*     */ 
/* 301 */     return localBufferedImage1;
/*     */   }
/*     */ 
/*     */   public final WritableRaster filter(Raster paramRaster, WritableRaster paramWritableRaster)
/*     */   {
/* 338 */     if (paramRaster == null) {
/* 339 */       throw new NullPointerException("src image is null");
/*     */     }
/* 341 */     if (paramWritableRaster == null) {
/* 342 */       paramWritableRaster = createCompatibleDestRaster(paramRaster);
/*     */     }
/* 344 */     if (paramRaster == paramWritableRaster) {
/* 345 */       throw new IllegalArgumentException("src image cannot be the same as the dst image");
/*     */     }
/*     */ 
/* 348 */     if (paramRaster.getNumBands() != paramWritableRaster.getNumBands()) {
/* 349 */       throw new IllegalArgumentException("Number of src bands (" + paramRaster.getNumBands() + ") does not match number of " + " dst bands (" + paramWritableRaster.getNumBands() + ")");
/*     */     }
/*     */ 
/* 356 */     if (ImagingLib.filter(this, paramRaster, paramWritableRaster) == null) {
/* 357 */       throw new ImagingOpException("Unable to transform src image");
/*     */     }
/* 359 */     return paramWritableRaster;
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(BufferedImage paramBufferedImage)
/*     */   {
/* 374 */     return getBounds2D(paramBufferedImage.getRaster());
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(Raster paramRaster)
/*     */   {
/* 389 */     int i = paramRaster.getWidth();
/* 390 */     int j = paramRaster.getHeight();
/*     */ 
/* 393 */     float[] arrayOfFloat = { 0.0F, 0.0F, i, 0.0F, i, j, 0.0F, j };
/* 394 */     this.xform.transform(arrayOfFloat, 0, arrayOfFloat, 0, 4);
/*     */ 
/* 397 */     float f1 = arrayOfFloat[0];
/* 398 */     float f2 = arrayOfFloat[1];
/* 399 */     float f3 = arrayOfFloat[0];
/* 400 */     float f4 = arrayOfFloat[1];
/* 401 */     for (int k = 2; k < 8; k += 2) {
/* 402 */       if (arrayOfFloat[k] > f1) {
/* 403 */         f1 = arrayOfFloat[k];
/*     */       }
/* 405 */       else if (arrayOfFloat[k] < f3) {
/* 406 */         f3 = arrayOfFloat[k];
/*     */       }
/* 408 */       if (arrayOfFloat[(k + 1)] > f2) {
/* 409 */         f2 = arrayOfFloat[(k + 1)];
/*     */       }
/* 411 */       else if (arrayOfFloat[(k + 1)] < f4) {
/* 412 */         f4 = arrayOfFloat[(k + 1)];
/*     */       }
/*     */     }
/*     */ 
/* 416 */     return new Rectangle2D.Float(f3, f4, f1 - f3, f2 - f4);
/*     */   }
/*     */ 
/*     */   public BufferedImage createCompatibleDestImage(BufferedImage paramBufferedImage, ColorModel paramColorModel)
/*     */   {
/* 438 */     Rectangle localRectangle = getBounds2D(paramBufferedImage).getBounds();
/*     */ 
/* 444 */     int i = localRectangle.x + localRectangle.width;
/* 445 */     int j = localRectangle.y + localRectangle.height;
/* 446 */     if (i <= 0) {
/* 447 */       throw new RasterFormatException("Transformed width (" + i + ") is less than or equal to 0.");
/*     */     }
/*     */ 
/* 450 */     if (j <= 0)
/* 451 */       throw new RasterFormatException("Transformed height (" + j + ") is less than or equal to 0.");
/*     */     BufferedImage localBufferedImage;
/* 455 */     if (paramColorModel == null) {
/* 456 */       ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 457 */       if ((this.interpolationType != 1) && (((localColorModel instanceof IndexColorModel)) || (localColorModel.getTransparency() == 1)))
/*     */       {
/* 461 */         localBufferedImage = new BufferedImage(i, j, 2);
/*     */       }
/*     */       else
/*     */       {
/* 465 */         localBufferedImage = new BufferedImage(localColorModel, paramBufferedImage.getRaster().createCompatibleWritableRaster(i, j), localColorModel.isAlphaPremultiplied(), null);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 471 */       localBufferedImage = new BufferedImage(paramColorModel, paramColorModel.createCompatibleWritableRaster(i, j), paramColorModel.isAlphaPremultiplied(), null);
/*     */     }
/*     */ 
/* 476 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleDestRaster(Raster paramRaster)
/*     */   {
/* 489 */     Rectangle2D localRectangle2D = getBounds2D(paramRaster);
/*     */ 
/* 491 */     return paramRaster.createCompatibleWritableRaster((int)localRectangle2D.getX(), (int)localRectangle2D.getY(), (int)localRectangle2D.getWidth(), (int)localRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public final Point2D getPoint2D(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */   {
/* 510 */     return this.xform.transform(paramPoint2D1, paramPoint2D2);
/*     */   }
/*     */ 
/*     */   public final AffineTransform getTransform()
/*     */   {
/* 519 */     return (AffineTransform)this.xform.clone();
/*     */   }
/*     */ 
/*     */   public final RenderingHints getRenderingHints()
/*     */   {
/* 528 */     if (this.hints == null)
/*     */     {
/*     */       Object localObject;
/* 530 */       switch (this.interpolationType) {
/*     */       case 1:
/* 532 */         localObject = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
/* 533 */         break;
/*     */       case 2:
/* 535 */         localObject = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
/* 536 */         break;
/*     */       case 3:
/* 538 */         localObject = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
/* 539 */         break;
/*     */       default:
/* 542 */         throw new InternalError("Unknown interpolation type " + this.interpolationType);
/*     */       }
/*     */ 
/* 546 */       this.hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, localObject);
/*     */     }
/*     */ 
/* 549 */     return this.hints;
/*     */   }
/*     */ 
/*     */   void validateTransform(AffineTransform paramAffineTransform)
/*     */   {
/* 556 */     if (Math.abs(paramAffineTransform.getDeterminant()) <= 4.9E-324D)
/* 557 */       throw new ImagingOpException("Unable to invert transform " + paramAffineTransform);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.AffineTransformOp
 * JD-Core Version:    0.6.2
 */