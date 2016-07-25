/*     */ package java.awt.image;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import sun.awt.image.ImagingLib;
/*     */ 
/*     */ public class ConvolveOp
/*     */   implements BufferedImageOp, RasterOp
/*     */ {
/*     */   Kernel kernel;
/*     */   int edgeHint;
/*     */   RenderingHints hints;
/*     */   public static final int EDGE_ZERO_FILL = 0;
/*     */   public static final int EDGE_NO_OP = 1;
/*     */ 
/*     */   public ConvolveOp(Kernel paramKernel, int paramInt, RenderingHints paramRenderingHints)
/*     */   {
/* 101 */     this.kernel = paramKernel;
/* 102 */     this.edgeHint = paramInt;
/* 103 */     this.hints = paramRenderingHints;
/*     */   }
/*     */ 
/*     */   public ConvolveOp(Kernel paramKernel)
/*     */   {
/* 114 */     this.kernel = paramKernel;
/* 115 */     this.edgeHint = 0;
/*     */   }
/*     */ 
/*     */   public int getEdgeCondition()
/*     */   {
/* 125 */     return this.edgeHint;
/*     */   }
/*     */ 
/*     */   public final Kernel getKernel()
/*     */   {
/* 133 */     return (Kernel)this.kernel.clone();
/*     */   }
/*     */ 
/*     */   public final BufferedImage filter(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2)
/*     */   {
/* 156 */     if (paramBufferedImage1 == null) {
/* 157 */       throw new NullPointerException("src image is null");
/*     */     }
/* 159 */     if (paramBufferedImage1 == paramBufferedImage2) {
/* 160 */       throw new IllegalArgumentException("src image cannot be the same as the dst image");
/*     */     }
/*     */ 
/* 164 */     int i = 0;
/* 165 */     ColorModel localColorModel1 = paramBufferedImage1.getColorModel();
/*     */ 
/* 167 */     BufferedImage localBufferedImage = paramBufferedImage2;
/*     */     Object localObject1;
/* 170 */     if ((localColorModel1 instanceof IndexColorModel)) {
/* 171 */       localObject1 = (IndexColorModel)localColorModel1;
/* 172 */       paramBufferedImage1 = ((IndexColorModel)localObject1).convertToIntDiscrete(paramBufferedImage1.getRaster(), false);
/* 173 */       localColorModel1 = paramBufferedImage1.getColorModel();
/*     */     }
/*     */     ColorModel localColorModel2;
/* 176 */     if (paramBufferedImage2 == null) {
/* 177 */       paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/* 178 */       localColorModel2 = localColorModel1;
/* 179 */       localBufferedImage = paramBufferedImage2;
/*     */     }
/*     */     else {
/* 182 */       localColorModel2 = paramBufferedImage2.getColorModel();
/* 183 */       if (localColorModel1.getColorSpace().getType() != localColorModel2.getColorSpace().getType())
/*     */       {
/* 186 */         i = 1;
/* 187 */         paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/* 188 */         localColorModel2 = paramBufferedImage2.getColorModel();
/*     */       }
/* 190 */       else if ((localColorModel2 instanceof IndexColorModel)) {
/* 191 */         paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/* 192 */         localColorModel2 = paramBufferedImage2.getColorModel();
/*     */       }
/*     */     }
/*     */ 
/* 196 */     if (ImagingLib.filter(this, paramBufferedImage1, paramBufferedImage2) == null) {
/* 197 */       throw new ImagingOpException("Unable to convolve src image");
/*     */     }
/*     */ 
/* 200 */     if (i != 0) {
/* 201 */       localObject1 = new ColorConvertOp(this.hints);
/* 202 */       ((ColorConvertOp)localObject1).filter(paramBufferedImage2, localBufferedImage);
/*     */     }
/* 204 */     else if (localBufferedImage != paramBufferedImage2) {
/* 205 */       localObject1 = localBufferedImage.createGraphics();
/*     */       try {
/* 207 */         ((Graphics2D)localObject1).drawImage(paramBufferedImage2, 0, 0, null);
/*     */       } finally {
/* 209 */         ((Graphics2D)localObject1).dispose();
/*     */       }
/*     */     }
/*     */ 
/* 213 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public final WritableRaster filter(Raster paramRaster, WritableRaster paramWritableRaster)
/*     */   {
/* 235 */     if (paramWritableRaster == null) {
/* 236 */       paramWritableRaster = createCompatibleDestRaster(paramRaster);
/*     */     } else {
/* 238 */       if (paramRaster == paramWritableRaster) {
/* 239 */         throw new IllegalArgumentException("src image cannot be the same as the dst image");
/*     */       }
/*     */ 
/* 242 */       if (paramRaster.getNumBands() != paramWritableRaster.getNumBands()) {
/* 243 */         throw new ImagingOpException("Different number of bands in src  and dst Rasters");
/*     */       }
/*     */     }
/*     */ 
/* 247 */     if (ImagingLib.filter(this, paramRaster, paramWritableRaster) == null) {
/* 248 */       throw new ImagingOpException("Unable to convolve src image");
/*     */     }
/*     */ 
/* 251 */     return paramWritableRaster;
/*     */   }
/*     */ 
/*     */   public BufferedImage createCompatibleDestImage(BufferedImage paramBufferedImage, ColorModel paramColorModel)
/*     */   {
/* 266 */     int i = paramBufferedImage.getWidth();
/* 267 */     int j = paramBufferedImage.getHeight();
/*     */ 
/* 269 */     WritableRaster localWritableRaster = null;
/*     */ 
/* 271 */     if (paramColorModel == null) {
/* 272 */       paramColorModel = paramBufferedImage.getColorModel();
/*     */ 
/* 274 */       if ((paramColorModel instanceof IndexColorModel)) {
/* 275 */         paramColorModel = ColorModel.getRGBdefault();
/*     */       }
/*     */       else
/*     */       {
/* 280 */         localWritableRaster = paramBufferedImage.getData().createCompatibleWritableRaster(i, j);
/*     */       }
/*     */     }
/*     */ 
/* 284 */     if (localWritableRaster == null)
/*     */     {
/* 291 */       localWritableRaster = paramColorModel.createCompatibleWritableRaster(i, j);
/*     */     }
/*     */ 
/* 294 */     BufferedImage localBufferedImage = new BufferedImage(paramColorModel, localWritableRaster, paramColorModel.isAlphaPremultiplied(), null);
/*     */ 
/* 297 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleDestRaster(Raster paramRaster)
/*     */   {
/* 305 */     return paramRaster.createCompatibleWritableRaster();
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(BufferedImage paramBufferedImage)
/*     */   {
/* 314 */     return getBounds2D(paramBufferedImage.getRaster());
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(Raster paramRaster)
/*     */   {
/* 323 */     return paramRaster.getBounds();
/*     */   }
/*     */ 
/*     */   public final Point2D getPoint2D(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */   {
/* 333 */     if (paramPoint2D2 == null) {
/* 334 */       paramPoint2D2 = new Point2D.Float();
/*     */     }
/* 336 */     paramPoint2D2.setLocation(paramPoint2D1.getX(), paramPoint2D1.getY());
/*     */ 
/* 338 */     return paramPoint2D2;
/*     */   }
/*     */ 
/*     */   public final RenderingHints getRenderingHints()
/*     */   {
/* 345 */     return this.hints;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ConvolveOp
 * JD-Core Version:    0.6.2
 */