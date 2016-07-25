/*     */ package sun.awt.image.codec;
/*     */ 
/*     */ import com.sun.image.codec.jpeg.ImageFormatException;
/*     */ import com.sun.image.codec.jpeg.JPEGDecodeParam;
/*     */ import com.sun.image.codec.jpeg.JPEGEncodeParam;
/*     */ import com.sun.image.codec.jpeg.JPEGImageEncoder;
/*     */ import java.awt.Point;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ComponentColorModel;
/*     */ import java.awt.image.ComponentSampleModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RescaleOp;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class JPEGImageEncoderImpl
/*     */   implements JPEGImageEncoder
/*     */ {
/*  62 */   private OutputStream outStream = null;
/*  63 */   private JPEGParam param = null;
/*  64 */   private boolean pack = false;
/*     */ 
/*  66 */   private static final Class OutputStreamClass = OutputStream.class;
/*     */ 
/*     */   public JPEGImageEncoderImpl(OutputStream paramOutputStream)
/*     */   {
/*  83 */     if (paramOutputStream == null) {
/*  84 */       throw new IllegalArgumentException("OutputStream is null.");
/*     */     }
/*  86 */     this.outStream = paramOutputStream;
/*  87 */     initEncoder(OutputStreamClass);
/*     */   }
/*     */ 
/*     */   public JPEGImageEncoderImpl(OutputStream paramOutputStream, JPEGEncodeParam paramJPEGEncodeParam)
/*     */   {
/*  97 */     this(paramOutputStream);
/*  98 */     setJPEGEncodeParam(paramJPEGEncodeParam);
/*     */   }
/*     */ 
/*     */   public int getDefaultColorId(ColorModel paramColorModel)
/*     */   {
/* 113 */     boolean bool = paramColorModel.hasAlpha();
/* 114 */     ColorSpace localColorSpace1 = paramColorModel.getColorSpace();
/* 115 */     ColorSpace localColorSpace2 = null;
/* 116 */     switch (localColorSpace1.getType()) {
/*     */     case 6:
/* 118 */       return 1;
/*     */     case 5:
/* 121 */       if (bool) {
/* 122 */         return 7;
/*     */       }
/* 124 */       return 3;
/*     */     case 3:
/* 127 */       if (localColorSpace2 == null) {
/*     */         try {
/* 129 */           localColorSpace2 = ColorSpace.getInstance(1002);
/*     */         }
/*     */         catch (IllegalArgumentException localIllegalArgumentException)
/*     */         {
/*     */         }
/*     */       }
/*     */ 
/* 136 */       if (localColorSpace1 == localColorSpace2) {
/* 137 */         return bool ? 10 : 5;
/*     */       }
/*     */ 
/* 141 */       return bool ? 7 : 3;
/*     */     case 9:
/* 146 */       return 4;
/*     */     case 4:
/*     */     case 7:
/* 148 */     case 8: } return 0;
/*     */   }
/*     */ 
/*     */   public synchronized OutputStream getOutputStream()
/*     */   {
/* 153 */     return this.outStream;
/*     */   }
/*     */ 
/*     */   public synchronized void setJPEGEncodeParam(JPEGEncodeParam paramJPEGEncodeParam) {
/* 157 */     this.param = new JPEGParam(paramJPEGEncodeParam);
/*     */   }
/*     */   public synchronized JPEGEncodeParam getJPEGEncodeParam() {
/* 160 */     return (JPEGEncodeParam)this.param.clone();
/*     */   }
/*     */ 
/*     */   public JPEGEncodeParam getDefaultJPEGEncodeParam(Raster paramRaster, int paramInt) {
/* 164 */     JPEGParam localJPEGParam = new JPEGParam(paramInt, paramRaster.getNumBands());
/* 165 */     localJPEGParam.setWidth(paramRaster.getWidth());
/* 166 */     localJPEGParam.setHeight(paramRaster.getHeight());
/*     */ 
/* 168 */     return localJPEGParam;
/*     */   }
/*     */ 
/*     */   public JPEGEncodeParam getDefaultJPEGEncodeParam(BufferedImage paramBufferedImage)
/*     */   {
/* 173 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 174 */     int i = getDefaultColorId(localColorModel);
/*     */ 
/* 176 */     if (!(localColorModel instanceof IndexColorModel))
/* 177 */       return getDefaultJPEGEncodeParam(paramBufferedImage.getRaster(), i);
/* 182 */     JPEGParam localJPEGParam;
/* 182 */     if (localColorModel.hasAlpha()) localJPEGParam = new JPEGParam(i, 4); else {
/* 183 */       localJPEGParam = new JPEGParam(i, 3);
/*     */     }
/* 185 */     localJPEGParam.setWidth(paramBufferedImage.getWidth());
/* 186 */     localJPEGParam.setHeight(paramBufferedImage.getHeight());
/* 187 */     return localJPEGParam;
/*     */   }
/*     */ 
/*     */   public JPEGEncodeParam getDefaultJPEGEncodeParam(int paramInt1, int paramInt2)
/*     */   {
/* 192 */     return new JPEGParam(paramInt2, paramInt1);
/*     */   }
/*     */ 
/*     */   public JPEGEncodeParam getDefaultJPEGEncodeParam(JPEGDecodeParam paramJPEGDecodeParam) throws ImageFormatException
/*     */   {
/* 197 */     return new JPEGParam(paramJPEGDecodeParam);
/*     */   }
/*     */ 
/*     */   public synchronized void encode(BufferedImage paramBufferedImage)
/*     */     throws IOException, ImageFormatException
/*     */   {
/* 203 */     if (this.param == null) {
/* 204 */       setJPEGEncodeParam(getDefaultJPEGEncodeParam(paramBufferedImage));
/*     */     }
/* 206 */     if ((paramBufferedImage.getWidth() != this.param.getWidth()) || (paramBufferedImage.getHeight() != this.param.getHeight()))
/*     */     {
/* 208 */       throw new ImageFormatException("Param block's width/height doesn't match the BufferedImage");
/*     */     }
/*     */ 
/* 211 */     if (this.param.getEncodedColorID() != getDefaultColorId(paramBufferedImage.getColorModel()))
/*     */     {
/* 213 */       throw new ImageFormatException("The encoded COLOR_ID doesn't match the BufferedImage");
/*     */     }
/*     */ 
/* 216 */     WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 217 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*     */ 
/* 220 */     if ((localColorModel instanceof IndexColorModel)) {
/* 221 */       IndexColorModel localIndexColorModel = (IndexColorModel)localColorModel;
/* 222 */       paramBufferedImage = localIndexColorModel.convertToIntDiscrete(localWritableRaster, false);
/* 223 */       localWritableRaster = paramBufferedImage.getRaster();
/* 224 */       localColorModel = paramBufferedImage.getColorModel();
/*     */     }
/*     */ 
/* 227 */     encode(localWritableRaster, localColorModel);
/*     */   }
/*     */ 
/*     */   public synchronized void encode(BufferedImage paramBufferedImage, JPEGEncodeParam paramJPEGEncodeParam)
/*     */     throws IOException, ImageFormatException
/*     */   {
/* 233 */     setJPEGEncodeParam(paramJPEGEncodeParam);
/* 234 */     encode(paramBufferedImage);
/*     */   }
/*     */ 
/*     */   public void encode(Raster paramRaster)
/*     */     throws IOException, ImageFormatException
/*     */   {
/* 240 */     if (this.param == null) {
/* 241 */       setJPEGEncodeParam(getDefaultJPEGEncodeParam(paramRaster, 0));
/*     */     }
/*     */ 
/* 244 */     if (paramRaster.getNumBands() != paramRaster.getSampleModel().getNumBands()) {
/* 245 */       throw new ImageFormatException("Raster's number of bands doesn't match the SampleModel");
/*     */     }
/*     */ 
/* 248 */     if ((paramRaster.getWidth() != this.param.getWidth()) || (paramRaster.getHeight() != this.param.getHeight()))
/*     */     {
/* 250 */       throw new ImageFormatException("Param block's width/height doesn't match the Raster");
/*     */     }
/*     */ 
/* 253 */     if ((this.param.getEncodedColorID() != 0) && (this.param.getNumComponents() != paramRaster.getNumBands()))
/*     */     {
/* 255 */       throw new ImageFormatException("Param block's COLOR_ID doesn't match the Raster.");
/*     */     }
/*     */ 
/* 258 */     encode(paramRaster, (ColorModel)null);
/*     */   }
/*     */ 
/*     */   public void encode(Raster paramRaster, JPEGEncodeParam paramJPEGEncodeParam)
/*     */     throws IOException, ImageFormatException
/*     */   {
/* 264 */     setJPEGEncodeParam(paramJPEGEncodeParam);
/* 265 */     encode(paramRaster);
/*     */   }
/*     */ 
/*     */   private boolean useGiven(Raster paramRaster) {
/* 269 */     SampleModel localSampleModel = paramRaster.getSampleModel();
/* 270 */     if (localSampleModel.getDataType() != 0) {
/* 271 */       return false;
/*     */     }
/* 273 */     if (!(localSampleModel instanceof ComponentSampleModel)) {
/* 274 */       return false;
/*     */     }
/* 276 */     ComponentSampleModel localComponentSampleModel = (ComponentSampleModel)localSampleModel;
/* 277 */     if (localComponentSampleModel.getPixelStride() != localSampleModel.getNumBands())
/* 278 */       return false;
/* 279 */     int[] arrayOfInt = localComponentSampleModel.getBandOffsets();
/* 280 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 281 */       if (arrayOfInt[i] != i) return false;
/*     */     }
/* 283 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean canPack(Raster paramRaster)
/*     */   {
/* 289 */     SampleModel localSampleModel = paramRaster.getSampleModel();
/* 290 */     if (localSampleModel.getDataType() != 3) {
/* 291 */       return false;
/*     */     }
/* 293 */     if (!(localSampleModel instanceof SinglePixelPackedSampleModel)) {
/* 294 */       return false;
/*     */     }
/* 296 */     SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = (SinglePixelPackedSampleModel)localSampleModel;
/*     */ 
/* 299 */     int[] arrayOfInt1 = { 16711680, 65280, 255, -16777216 };
/* 300 */     int[] arrayOfInt2 = localSinglePixelPackedSampleModel.getBitMasks();
/* 301 */     if ((arrayOfInt2.length != 3) && (arrayOfInt2.length != 4)) {
/* 302 */       return false;
/*     */     }
/* 304 */     for (int i = 0; i < arrayOfInt2.length; i++) {
/* 305 */       if (arrayOfInt2[i] != arrayOfInt1[i]) return false;
/*     */     }
/* 307 */     return true;
/*     */   }
/*     */ 
/*     */   private void encode(Raster paramRaster, ColorModel paramColorModel)
/*     */     throws IOException, ImageFormatException
/*     */   {
/* 313 */     SampleModel localSampleModel = paramRaster.getSampleModel();
/*     */ 
/* 315 */     int j = localSampleModel.getNumBands();
/*     */     int i;
/* 316 */     if (paramColorModel == null)
/*     */     {
/* 321 */       for (i = 0; i < j; i++) {
/* 322 */         if (localSampleModel.getSampleSize(i) > 8) {
/* 323 */           throw new ImageFormatException("JPEG encoder can only accept 8 bit data.");
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 329 */     int k = this.param.getEncodedColorID();
/* 330 */     switch (this.param.getNumComponents())
/*     */     {
/*     */     case 1:
/* 333 */       if ((k != 1) && (k != 0) && (this.param.findAPP0() != null))
/*     */       {
/* 336 */         throw new ImageFormatException("1 band JFIF files imply Y or unknown encoding.\nParam block indicates alternate encoding.");
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 3:
/* 341 */       if ((k != 3) && (this.param.findAPP0() != null))
/*     */       {
/* 343 */         throw new ImageFormatException("3 band JFIF files imply YCbCr encoding.\nParam block indicates alternate encoding.");
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 4:
/* 348 */       if ((k != 4) && (this.param.findAPP0() != null))
/*     */       {
/* 350 */         throw new ImageFormatException("4 band JFIF files imply CMYK encoding.\nParam block indicates alternate encoding.");
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 2:
/*     */     }
/*     */ 
/* 359 */     if (!this.param.isImageInfoValid())
/*     */     {
/* 361 */       writeJPEGStream(this.param, paramColorModel, this.outStream, null, 0, 0);
/* 362 */       return;
/*     */     }
/*     */ 
/* 365 */     DataBuffer localDataBuffer = paramRaster.getDataBuffer();
/*     */ 
/* 369 */     int i1 = 0;
/* 370 */     int i2 = 1;
/* 371 */     int[] arrayOfInt1 = null;
/*     */ 
/* 373 */     if (paramColorModel != null) {
/* 374 */       if ((paramColorModel.hasAlpha()) && (paramColorModel.isAlphaPremultiplied())) {
/* 375 */         i1 = 1;
/* 376 */         i2 = 0;
/*     */       }
/* 378 */       arrayOfInt1 = paramColorModel.getComponentSize();
/* 379 */       for (i = 0; i < j; i++) {
/* 380 */         if (arrayOfInt1[i] != 8) {
/* 381 */           i2 = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 386 */     this.pack = false;
/*     */     Object localObject2;
/*     */     int n;
/*     */     int m;
/*     */     Object localObject1;
/* 387 */     if ((i2 != 0) && (useGiven(paramRaster))) {
/* 388 */       localObject2 = (ComponentSampleModel)localSampleModel;
/*     */ 
/* 390 */       n = localDataBuffer.getOffset() + ((ComponentSampleModel)localObject2).getOffset(paramRaster.getMinX() - paramRaster.getSampleModelTranslateX(), paramRaster.getMinY() - paramRaster.getSampleModelTranslateY());
/*     */ 
/* 395 */       m = ((ComponentSampleModel)localObject2).getScanlineStride();
/* 396 */       localObject1 = ((DataBufferByte)localDataBuffer).getData();
/*     */     }
/* 398 */     else if ((i2 != 0) && (canPack(paramRaster)))
/*     */     {
/* 400 */       localObject2 = (SinglePixelPackedSampleModel)localSampleModel;
/*     */ 
/* 402 */       n = localDataBuffer.getOffset() + ((SinglePixelPackedSampleModel)localObject2).getOffset(paramRaster.getMinX() - paramRaster.getSampleModelTranslateX(), paramRaster.getMinY() - paramRaster.getSampleModelTranslateY());
/*     */ 
/* 407 */       m = ((SinglePixelPackedSampleModel)localObject2).getScanlineStride();
/* 408 */       localObject1 = ((DataBufferInt)localDataBuffer).getData();
/* 409 */       this.pack = true;
/*     */     }
/*     */     else {
/* 412 */       int[] arrayOfInt2 = new int[j];
/* 413 */       float[] arrayOfFloat1 = new float[j];
/* 414 */       for (i = 0; i < j; i++) {
/* 415 */         arrayOfInt2[i] = i;
/* 416 */         if (i2 == 0)
/*     */         {
/* 419 */           if (arrayOfInt1[i] != 8) {
/* 420 */             arrayOfFloat1[i] = (255.0F / ((1 << arrayOfInt1[i]) - 1));
/*     */           }
/*     */           else
/* 423 */             arrayOfFloat1[i] = 1.0F;
/*     */         }
/*     */       }
/* 426 */       localObject2 = new ComponentSampleModel(0, paramRaster.getWidth(), paramRaster.getHeight(), j, j * paramRaster.getWidth(), arrayOfInt2);
/*     */ 
/* 430 */       WritableRaster localWritableRaster = Raster.createWritableRaster((SampleModel)localObject2, new Point(paramRaster.getMinX(), paramRaster.getMinY()));
/*     */ 
/* 433 */       if (i2 != 0) {
/* 434 */         localWritableRaster.setRect(paramRaster);
/*     */       }
/*     */       else
/*     */       {
/* 443 */         float[] arrayOfFloat2 = new float[j];
/*     */ 
/* 445 */         RescaleOp localRescaleOp = new RescaleOp(arrayOfFloat1, arrayOfFloat2, null);
/*     */ 
/* 447 */         localRescaleOp.filter(paramRaster, localWritableRaster);
/* 448 */         if (i1 != 0) {
/* 449 */           int[] arrayOfInt3 = new int[j];
/* 450 */           for (i = 0; i < j; i++) {
/* 451 */             arrayOfInt3[i] = 8;
/*     */           }
/* 453 */           ComponentColorModel localComponentColorModel = new ComponentColorModel(paramColorModel.getColorSpace(), arrayOfInt3, true, true, 3, 0);
/*     */ 
/* 458 */           localComponentColorModel.coerceData(localWritableRaster, false);
/*     */         }
/*     */       }
/*     */ 
/* 462 */       localDataBuffer = localWritableRaster.getDataBuffer();
/* 463 */       localObject1 = ((DataBufferByte)localDataBuffer).getData();
/*     */ 
/* 470 */       n = localDataBuffer.getOffset() + ((ComponentSampleModel)localObject2).getOffset(0, 0);
/* 471 */       m = ((ComponentSampleModel)localObject2).getScanlineStride();
/*     */     }
/*     */ 
/* 474 */     verify(n, m, localDataBuffer.getSize());
/*     */ 
/* 476 */     writeJPEGStream(this.param, paramColorModel, this.outStream, localObject1, n, m);
/*     */   }
/*     */ 
/*     */   private void verify(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws ImageFormatException
/*     */   {
/* 482 */     int i = this.param.getWidth();
/* 483 */     int j = this.param.getHeight();
/*     */ 
/* 486 */     int k = this.pack ? 1 : this.param.getNumComponents();
/*     */ 
/* 488 */     if ((i <= 0) || (j <= 0) || (j > 2147483647 / i))
/*     */     {
/* 490 */       throw new ImageFormatException("Invalid image dimensions");
/*     */     }
/*     */ 
/* 493 */     if ((paramInt2 < 0) || (paramInt2 > 2147483647 / j) || (paramInt2 > paramInt3))
/*     */     {
/* 496 */       throw new ImageFormatException("Invalid scanline stride: " + paramInt2);
/*     */     }
/*     */ 
/* 500 */     int m = (j - 1) * paramInt2;
/*     */ 
/* 502 */     if ((k < 0) || (k > 2147483647 / i) || (k > paramInt3) || (k * i > paramInt2))
/*     */     {
/* 506 */       throw new ImageFormatException("Invalid pixel stride: " + k);
/*     */     }
/*     */ 
/* 510 */     int n = i * k;
/* 511 */     if (n > 2147483647 - m) {
/* 512 */       throw new ImageFormatException("Invalid raster attributes");
/*     */     }
/*     */ 
/* 515 */     int i1 = m + n;
/* 516 */     if ((paramInt1 < 0) || (paramInt1 > 2147483647 - i1)) {
/* 517 */       throw new ImageFormatException("Invalid data offset");
/*     */     }
/*     */ 
/* 520 */     int i2 = paramInt1 + i1;
/* 521 */     if (i2 > paramInt3)
/* 522 */       throw new ImageFormatException("Computed buffer size doesn't match DataBuffer");
/*     */   }
/*     */ 
/*     */   private int getNearestColorId(ColorModel paramColorModel)
/*     */   {
/* 539 */     ColorSpace localColorSpace = paramColorModel.getColorSpace();
/* 540 */     switch (localColorSpace.getType()) {
/*     */     case 5:
/* 542 */       if (paramColorModel.hasAlpha()) return 6;
/* 543 */       return 2;
/*     */     }
/*     */ 
/* 546 */     return getDefaultColorId(paramColorModel);
/*     */   }
/*     */ 
/*     */   private native void initEncoder(Class paramClass);
/*     */ 
/*     */   private synchronized native void writeJPEGStream(JPEGEncodeParam paramJPEGEncodeParam, ColorModel paramColorModel, OutputStream paramOutputStream, Object paramObject, int paramInt1, int paramInt2)
/*     */     throws IOException, ImageFormatException;
/*     */ 
/*     */   static
/*     */   {
/*  73 */     AccessController.doPrivileged(new LoadLibraryAction("jpeg"));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.codec.JPEGImageEncoderImpl
 * JD-Core Version:    0.6.2
 */