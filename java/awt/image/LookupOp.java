/*     */ package java.awt.image;
/*     */ 
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import sun.awt.image.ImagingLib;
/*     */ 
/*     */ public class LookupOp
/*     */   implements BufferedImageOp, RasterOp
/*     */ {
/*     */   private LookupTable ltable;
/*     */   private int numComponents;
/*     */   RenderingHints hints;
/*     */ 
/*     */   public LookupOp(LookupTable paramLookupTable, RenderingHints paramRenderingHints)
/*     */   {
/*  91 */     this.ltable = paramLookupTable;
/*  92 */     this.hints = paramRenderingHints;
/*  93 */     this.numComponents = this.ltable.getNumComponents();
/*     */   }
/*     */ 
/*     */   public final LookupTable getTable()
/*     */   {
/* 102 */     return this.ltable;
/*     */   }
/*     */ 
/*     */   public final BufferedImage filter(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2)
/*     */   {
/* 126 */     ColorModel localColorModel1 = paramBufferedImage1.getColorModel();
/* 127 */     int i = localColorModel1.getNumColorComponents();
/*     */ 
/* 129 */     if ((localColorModel1 instanceof IndexColorModel)) {
/* 130 */       throw new IllegalArgumentException("LookupOp cannot be performed on an indexed image");
/*     */     }
/*     */ 
/* 134 */     int j = this.ltable.getNumComponents();
/* 135 */     if ((j != 1) && (j != localColorModel1.getNumComponents()) && (j != localColorModel1.getNumColorComponents()))
/*     */     {
/* 139 */       throw new IllegalArgumentException("Number of arrays in the  lookup table (" + j + " is not compatible with the " + " src image: " + paramBufferedImage1);
/*     */     }
/*     */ 
/* 147 */     int k = 0;
/*     */ 
/* 149 */     int m = paramBufferedImage1.getWidth();
/* 150 */     int n = paramBufferedImage1.getHeight();
/*     */     ColorModel localColorModel2;
/* 152 */     if (paramBufferedImage2 == null) {
/* 153 */       paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/* 154 */       localColorModel2 = localColorModel1;
/*     */     }
/*     */     else {
/* 157 */       if (m != paramBufferedImage2.getWidth()) {
/* 158 */         throw new IllegalArgumentException("Src width (" + m + ") not equal to dst width (" + paramBufferedImage2.getWidth() + ")");
/*     */       }
/*     */ 
/* 163 */       if (n != paramBufferedImage2.getHeight()) {
/* 164 */         throw new IllegalArgumentException("Src height (" + n + ") not equal to dst height (" + paramBufferedImage2.getHeight() + ")");
/*     */       }
/*     */ 
/* 170 */       localColorModel2 = paramBufferedImage2.getColorModel();
/* 171 */       if (localColorModel1.getColorSpace().getType() != localColorModel2.getColorSpace().getType())
/*     */       {
/* 174 */         k = 1;
/* 175 */         paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 180 */     BufferedImage localBufferedImage = paramBufferedImage2;
/*     */     Object localObject;
/* 182 */     if (ImagingLib.filter(this, paramBufferedImage1, paramBufferedImage2) == null)
/*     */     {
/* 184 */       localObject = paramBufferedImage1.getRaster();
/* 185 */       WritableRaster localWritableRaster = paramBufferedImage2.getRaster();
/*     */       int i1;
/*     */       int i2;
/* 187 */       if ((localColorModel1.hasAlpha()) && (
/* 188 */         (i - 1 == j) || (j == 1))) {
/* 189 */         i1 = ((WritableRaster)localObject).getMinX();
/* 190 */         i2 = ((WritableRaster)localObject).getMinY();
/* 191 */         int[] arrayOfInt1 = new int[i - 1];
/* 192 */         for (int i4 = 0; i4 < i - 1; i4++) {
/* 193 */           arrayOfInt1[i4] = i4;
/*     */         }
/* 195 */         localObject = ((WritableRaster)localObject).createWritableChild(i1, i2, ((WritableRaster)localObject).getWidth(), ((WritableRaster)localObject).getHeight(), i1, i2, arrayOfInt1);
/*     */       }
/*     */ 
/* 203 */       if (localColorModel2.hasAlpha()) {
/* 204 */         i1 = localWritableRaster.getNumBands();
/* 205 */         if ((i1 - 1 == j) || (j == 1)) {
/* 206 */           i2 = localWritableRaster.getMinX();
/* 207 */           int i3 = localWritableRaster.getMinY();
/* 208 */           int[] arrayOfInt2 = new int[i - 1];
/* 209 */           for (int i5 = 0; i5 < i - 1; i5++) {
/* 210 */             arrayOfInt2[i5] = i5;
/*     */           }
/* 212 */           localWritableRaster = localWritableRaster.createWritableChild(i2, i3, localWritableRaster.getWidth(), localWritableRaster.getHeight(), i2, i3, arrayOfInt2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 221 */       filter((Raster)localObject, localWritableRaster);
/*     */     }
/*     */ 
/* 224 */     if (k != 0)
/*     */     {
/* 226 */       localObject = new ColorConvertOp(this.hints);
/* 227 */       ((ColorConvertOp)localObject).filter(paramBufferedImage2, localBufferedImage);
/*     */     }
/*     */ 
/* 230 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public final WritableRaster filter(Raster paramRaster, WritableRaster paramWritableRaster)
/*     */   {
/* 254 */     int i = paramRaster.getNumBands();
/* 255 */     int j = paramWritableRaster.getNumBands();
/* 256 */     int k = paramRaster.getHeight();
/* 257 */     int m = paramRaster.getWidth();
/* 258 */     int[] arrayOfInt = new int[i];
/*     */ 
/* 262 */     if (paramWritableRaster == null) {
/* 263 */       paramWritableRaster = createCompatibleDestRaster(paramRaster);
/*     */     }
/* 265 */     else if ((k != paramWritableRaster.getHeight()) || (m != paramWritableRaster.getWidth())) {
/* 266 */       throw new IllegalArgumentException("Width or height of Rasters do not match");
/*     */     }
/*     */ 
/* 270 */     j = paramWritableRaster.getNumBands();
/*     */ 
/* 272 */     if (i != j) {
/* 273 */       throw new IllegalArgumentException("Number of channels in the src (" + i + ") does not match number of channels" + " in the destination (" + j + ")");
/*     */     }
/*     */ 
/* 280 */     int n = this.ltable.getNumComponents();
/* 281 */     if ((n != 1) && (n != paramRaster.getNumBands())) {
/* 282 */       throw new IllegalArgumentException("Number of arrays in the  lookup table (" + n + " is not compatible with the " + " src Raster: " + paramRaster);
/*     */     }
/*     */ 
/* 290 */     if (ImagingLib.filter(this, paramRaster, paramWritableRaster) != null) {
/* 291 */       return paramWritableRaster;
/*     */     }
/*     */ 
/* 295 */     if ((this.ltable instanceof ByteLookupTable)) {
/* 296 */       byteFilter((ByteLookupTable)this.ltable, paramRaster, paramWritableRaster, m, k, i);
/*     */     }
/* 299 */     else if ((this.ltable instanceof ShortLookupTable)) {
/* 300 */       shortFilter((ShortLookupTable)this.ltable, paramRaster, paramWritableRaster, m, k, i);
/*     */     }
/*     */     else
/*     */     {
/* 305 */       int i1 = paramRaster.getMinX();
/* 306 */       int i2 = paramRaster.getMinY();
/* 307 */       int i3 = paramWritableRaster.getMinX();
/* 308 */       int i4 = paramWritableRaster.getMinY();
/* 309 */       for (int i5 = 0; i5 < k; i4++) {
/* 310 */         int i6 = i1;
/* 311 */         int i7 = i3;
/* 312 */         for (int i8 = 0; i8 < m; i7++)
/*     */         {
/* 314 */           paramRaster.getPixel(i6, i2, arrayOfInt);
/*     */ 
/* 317 */           this.ltable.lookupPixel(arrayOfInt, arrayOfInt);
/*     */ 
/* 320 */           paramWritableRaster.setPixel(i7, i4, arrayOfInt);
/*     */ 
/* 312 */           i8++; i6++;
/*     */         }
/* 309 */         i5++; i2++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 325 */     return paramWritableRaster;
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(BufferedImage paramBufferedImage)
/*     */   {
/* 336 */     return getBounds2D(paramBufferedImage.getRaster());
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(Raster paramRaster)
/*     */   {
/* 347 */     return paramRaster.getBounds();
/*     */   }
/*     */ 
/*     */   public BufferedImage createCompatibleDestImage(BufferedImage paramBufferedImage, ColorModel paramColorModel)
/*     */   {
/* 363 */     int i = paramBufferedImage.getWidth();
/* 364 */     int j = paramBufferedImage.getHeight();
/* 365 */     int k = 0;
/*     */     BufferedImage localBufferedImage;
/* 366 */     if (paramColorModel == null) {
/* 367 */       Object localObject = paramBufferedImage.getColorModel();
/* 368 */       WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 369 */       if ((localObject instanceof ComponentColorModel)) {
/* 370 */         DataBuffer localDataBuffer = localWritableRaster.getDataBuffer();
/* 371 */         boolean bool1 = ((ColorModel)localObject).hasAlpha();
/* 372 */         boolean bool2 = ((ColorModel)localObject).isAlphaPremultiplied();
/* 373 */         int m = ((ColorModel)localObject).getTransparency();
/* 374 */         int[] arrayOfInt = null;
/* 375 */         if ((this.ltable instanceof ByteLookupTable)) {
/* 376 */           if (localDataBuffer.getDataType() == 1)
/*     */           {
/* 378 */             if (bool1) {
/* 379 */               arrayOfInt = new int[2];
/* 380 */               if (m == 2) {
/* 381 */                 arrayOfInt[1] = 1;
/*     */               }
/*     */               else
/* 384 */                 arrayOfInt[1] = 8;
/*     */             }
/*     */             else
/*     */             {
/* 388 */               arrayOfInt = new int[1];
/*     */             }
/* 390 */             arrayOfInt[0] = 8;
/*     */           }
/*     */ 
/*     */         }
/* 394 */         else if ((this.ltable instanceof ShortLookupTable)) {
/* 395 */           k = 1;
/* 396 */           if (localDataBuffer.getDataType() == 0) {
/* 397 */             if (bool1) {
/* 398 */               arrayOfInt = new int[2];
/* 399 */               if (m == 2) {
/* 400 */                 arrayOfInt[1] = 1;
/*     */               }
/*     */               else
/* 403 */                 arrayOfInt[1] = 16;
/*     */             }
/*     */             else
/*     */             {
/* 407 */               arrayOfInt = new int[1];
/*     */             }
/* 409 */             arrayOfInt[0] = 16;
/*     */           }
/*     */         }
/* 412 */         if (arrayOfInt != null) {
/* 413 */           localObject = new ComponentColorModel(((ColorModel)localObject).getColorSpace(), arrayOfInt, bool1, bool2, m, k);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 418 */       localBufferedImage = new BufferedImage((ColorModel)localObject, ((ColorModel)localObject).createCompatibleWritableRaster(i, j), ((ColorModel)localObject).isAlphaPremultiplied(), null);
/*     */     }
/*     */     else
/*     */     {
/* 424 */       localBufferedImage = new BufferedImage(paramColorModel, paramColorModel.createCompatibleWritableRaster(i, j), paramColorModel.isAlphaPremultiplied(), null);
/*     */     }
/*     */ 
/* 431 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleDestRaster(Raster paramRaster)
/*     */   {
/* 441 */     return paramRaster.createCompatibleWritableRaster();
/*     */   }
/*     */ 
/*     */   public final Point2D getPoint2D(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */   {
/* 458 */     if (paramPoint2D2 == null) {
/* 459 */       paramPoint2D2 = new Point2D.Float();
/*     */     }
/* 461 */     paramPoint2D2.setLocation(paramPoint2D1.getX(), paramPoint2D1.getY());
/*     */ 
/* 463 */     return paramPoint2D2;
/*     */   }
/*     */ 
/*     */   public final RenderingHints getRenderingHints()
/*     */   {
/* 472 */     return this.hints;
/*     */   }
/*     */ 
/*     */   private final void byteFilter(ByteLookupTable paramByteLookupTable, Raster paramRaster, WritableRaster paramWritableRaster, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 478 */     int[] arrayOfInt = null;
/*     */ 
/* 481 */     byte[][] arrayOfByte = paramByteLookupTable.getTable();
/* 482 */     int i = paramByteLookupTable.getOffset();
/*     */ 
/* 484 */     int k = 1;
/*     */ 
/* 487 */     if (arrayOfByte.length == 1) {
/* 488 */       k = 0;
/*     */     }
/*     */ 
/* 494 */     int i2 = arrayOfByte[0].length;
/*     */ 
/* 497 */     for (int n = 0; n < paramInt2; n++) {
/* 498 */       int j = 0;
/* 499 */       for (int i1 = 0; i1 < paramInt3; j += k)
/*     */       {
/* 501 */         arrayOfInt = paramRaster.getSamples(0, n, paramInt1, 1, i1, arrayOfInt);
/*     */ 
/* 503 */         for (int m = 0; m < paramInt1; m++) {
/* 504 */           int i3 = arrayOfInt[m] - i;
/* 505 */           if ((i3 < 0) || (i3 > i2)) {
/* 506 */             throw new IllegalArgumentException("index (" + i3 + "(out of range: " + " srcPix[" + m + "]=" + arrayOfInt[m] + " offset=" + i);
/*     */           }
/*     */ 
/* 514 */           arrayOfInt[m] = arrayOfByte[j][i3];
/*     */         }
/*     */ 
/* 517 */         paramWritableRaster.setSamples(0, n, paramInt1, 1, i1, arrayOfInt);
/*     */ 
/* 499 */         i1++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void shortFilter(ShortLookupTable paramShortLookupTable, Raster paramRaster, WritableRaster paramWritableRaster, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 526 */     int[] arrayOfInt = null;
/*     */ 
/* 529 */     short[][] arrayOfShort = paramShortLookupTable.getTable();
/* 530 */     int j = paramShortLookupTable.getOffset();
/*     */ 
/* 532 */     int m = 1;
/*     */ 
/* 535 */     if (arrayOfShort.length == 1) {
/* 536 */       m = 0;
/*     */     }
/*     */ 
/* 539 */     int n = 0;
/* 540 */     int i1 = 0;
/*     */ 
/* 542 */     int i3 = 65535;
/*     */ 
/* 544 */     for (i1 = 0; i1 < paramInt2; i1++) {
/* 545 */       int k = 0;
/* 546 */       for (int i = 0; i < paramInt3; k += m)
/*     */       {
/* 548 */         arrayOfInt = paramRaster.getSamples(0, i1, paramInt1, 1, i, arrayOfInt);
/*     */ 
/* 550 */         for (n = 0; n < paramInt1; n++) {
/* 551 */           int i2 = arrayOfInt[n] - j;
/* 552 */           if ((i2 < 0) || (i2 > i3)) {
/* 553 */             throw new IllegalArgumentException("index out of range " + i2 + " x is " + n + "srcPix[x]=" + arrayOfInt[n] + " offset=" + j);
/*     */           }
/*     */ 
/* 560 */           arrayOfInt[n] = arrayOfShort[k][i2];
/*     */         }
/*     */ 
/* 563 */         paramWritableRaster.setSamples(0, i1, paramInt1, 1, i, arrayOfInt);
/*     */ 
/* 546 */         i++;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.LookupOp
 * JD-Core Version:    0.6.2
 */