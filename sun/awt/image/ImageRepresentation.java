/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ImageRepresentation extends ImageWatched
/*     */   implements ImageConsumer
/*     */ {
/*     */   InputStreamImageSource src;
/*     */   ToolkitImage image;
/*     */   int tag;
/*     */   long pData;
/*  58 */   int width = -1;
/*  59 */   int height = -1;
/*     */   int hints;
/*     */   int availinfo;
/*     */   Rectangle newbits;
/*     */   BufferedImage bimage;
/*     */   WritableRaster biRaster;
/*     */   protected ColorModel cmodel;
/*  69 */   ColorModel srcModel = null;
/*  70 */   int[] srcLUT = null;
/*  71 */   int srcLUTtransIndex = -1;
/*  72 */   int numSrcLUT = 0;
/*     */   boolean forceCMhint;
/*     */   int sstride;
/*  75 */   boolean isDefaultBI = false;
/*  76 */   boolean isSameCM = false;
/*     */ 
/* 343 */   static boolean s_useNative = true;
/*     */ 
/* 681 */   private boolean consuming = false;
/*     */   private int numWaiters;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public ImageRepresentation(ToolkitImage paramToolkitImage, ColorModel paramColorModel, boolean paramBoolean)
/*     */   {
/*  95 */     this.image = paramToolkitImage;
/*     */ 
/*  97 */     if ((this.image.getSource() instanceof InputStreamImageSource)) {
/*  98 */       this.src = ((InputStreamImageSource)this.image.getSource());
/*     */     }
/*     */ 
/* 101 */     setColorModel(paramColorModel);
/*     */ 
/* 103 */     this.forceCMhint = paramBoolean;
/*     */   }
/*     */ 
/*     */   public synchronized void reconstruct(int paramInt)
/*     */   {
/* 108 */     if (this.src != null) {
/* 109 */       this.src.checkSecurity(null, false);
/*     */     }
/* 111 */     int i = paramInt & (this.availinfo ^ 0xFFFFFFFF);
/* 112 */     if (((this.availinfo & 0x40) == 0) && (i != 0)) {
/* 113 */       this.numWaiters += 1;
/*     */       try {
/* 115 */         startProduction();
/* 116 */         i = paramInt & (this.availinfo ^ 0xFFFFFFFF);
/* 117 */         while (((this.availinfo & 0x40) == 0) && (i != 0))
/*     */         {
/*     */           try
/*     */           {
/* 121 */             wait();
/*     */           } catch (InterruptedException localInterruptedException) { Thread.currentThread().interrupt();
/*     */             return;
/*     */           }
/* 126 */           i = paramInt & (this.availinfo ^ 0xFFFFFFFF);
/*     */         }
/*     */       } finally {
/* 129 */         decrementWaiters();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2) {
/* 135 */     if (this.src != null) {
/* 136 */       this.src.checkSecurity(null, false);
/*     */     }
/*     */ 
/* 139 */     this.image.setDimensions(paramInt1, paramInt2);
/*     */ 
/* 141 */     newInfo(this.image, 3, 0, 0, paramInt1, paramInt2);
/*     */ 
/* 144 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 145 */       imageComplete(1);
/* 146 */       return;
/*     */     }
/*     */ 
/* 149 */     if ((this.width != paramInt1) || (this.height != paramInt2))
/*     */     {
/* 151 */       this.bimage = null;
/*     */     }
/*     */ 
/* 154 */     this.width = paramInt1;
/* 155 */     this.height = paramInt2;
/*     */ 
/* 157 */     this.availinfo |= 3;
/*     */   }
/*     */ 
/*     */   public int getWidth() {
/* 161 */     return this.width;
/*     */   }
/*     */ 
/*     */   public int getHeight() {
/* 165 */     return this.height;
/*     */   }
/*     */ 
/*     */   ColorModel getColorModel() {
/* 169 */     return this.cmodel;
/*     */   }
/*     */ 
/*     */   BufferedImage getBufferedImage() {
/* 173 */     return this.bimage;
/*     */   }
/*     */ 
/*     */   protected BufferedImage createImage(ColorModel paramColorModel, WritableRaster paramWritableRaster, boolean paramBoolean, Hashtable paramHashtable)
/*     */   {
/* 190 */     BufferedImage localBufferedImage = new BufferedImage(paramColorModel, paramWritableRaster, paramBoolean, null);
/*     */ 
/* 192 */     localBufferedImage.setAccelerationPriority(this.image.getAccelerationPriority());
/* 193 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable<?, ?> paramHashtable) {
/* 197 */     if (this.src != null) {
/* 198 */       this.src.checkSecurity(null, false);
/*     */     }
/* 200 */     this.image.setProperties(paramHashtable);
/* 201 */     newInfo(this.image, 4, 0, 0, 0, 0);
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel) {
/* 205 */     if (this.src != null) {
/* 206 */       this.src.checkSecurity(null, false);
/*     */     }
/* 208 */     this.srcModel = paramColorModel;
/*     */     Object localObject;
/* 211 */     if ((paramColorModel instanceof IndexColorModel)) {
/* 212 */       if (paramColorModel.getTransparency() == 3)
/*     */       {
/* 215 */         this.cmodel = ColorModel.getRGBdefault();
/* 216 */         this.srcLUT = null;
/*     */       }
/*     */       else {
/* 219 */         localObject = (IndexColorModel)paramColorModel;
/* 220 */         this.numSrcLUT = ((IndexColorModel)localObject).getMapSize();
/* 221 */         this.srcLUT = new int[Math.max(this.numSrcLUT, 256)];
/* 222 */         ((IndexColorModel)localObject).getRGBs(this.srcLUT);
/* 223 */         this.srcLUTtransIndex = ((IndexColorModel)localObject).getTransparentPixel();
/* 224 */         this.cmodel = paramColorModel;
/*     */       }
/*     */ 
/*     */     }
/* 228 */     else if (this.cmodel == null) {
/* 229 */       this.cmodel = paramColorModel;
/* 230 */       this.srcLUT = null;
/*     */     }
/* 232 */     else if ((paramColorModel instanceof DirectColorModel))
/*     */     {
/* 234 */       localObject = (DirectColorModel)paramColorModel;
/* 235 */       if ((((DirectColorModel)localObject).getRedMask() == 16711680) && (((DirectColorModel)localObject).getGreenMask() == 65280) && (((DirectColorModel)localObject).getBlueMask() == 255))
/*     */       {
/* 238 */         this.cmodel = paramColorModel;
/* 239 */         this.srcLUT = null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 244 */     this.isSameCM = (this.cmodel == paramColorModel);
/*     */   }
/*     */ 
/*     */   void createBufferedImage()
/*     */   {
/* 251 */     this.isDefaultBI = false;
/*     */     try {
/* 253 */       this.biRaster = this.cmodel.createCompatibleWritableRaster(this.width, this.height);
/* 254 */       this.bimage = createImage(this.cmodel, this.biRaster, this.cmodel.isAlphaPremultiplied(), null);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 258 */       this.cmodel = ColorModel.getRGBdefault();
/* 259 */       this.biRaster = this.cmodel.createCompatibleWritableRaster(this.width, this.height);
/* 260 */       this.bimage = createImage(this.cmodel, this.biRaster, false, null);
/*     */     }
/* 262 */     int i = this.bimage.getType();
/*     */ 
/* 264 */     if ((this.cmodel == ColorModel.getRGBdefault()) || (i == 1) || (i == 3))
/*     */     {
/* 267 */       this.isDefaultBI = true;
/*     */     }
/* 269 */     else if ((this.cmodel instanceof DirectColorModel)) {
/* 270 */       DirectColorModel localDirectColorModel = (DirectColorModel)this.cmodel;
/* 271 */       if ((localDirectColorModel.getRedMask() == 16711680) && (localDirectColorModel.getGreenMask() == 65280) && (localDirectColorModel.getBlueMask() == 255))
/*     */       {
/* 274 */         this.isDefaultBI = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void convertToRGB() {
/* 280 */     int i = this.bimage.getWidth();
/* 281 */     int j = this.bimage.getHeight();
/* 282 */     int k = i * j;
/*     */ 
/* 284 */     DataBufferInt localDataBufferInt = new DataBufferInt(k);
/*     */ 
/* 287 */     int[] arrayOfInt = SunWritableRaster.stealData(localDataBufferInt, 0);
/*     */     int n;
/*     */     int i1;
/* 288 */     if (((this.cmodel instanceof IndexColorModel)) && ((this.biRaster instanceof ByteComponentRaster)) && (this.biRaster.getNumDataElements() == 1))
/*     */     {
/* 292 */       localObject = (ByteComponentRaster)this.biRaster;
/* 293 */       byte[] arrayOfByte = ((ByteComponentRaster)localObject).getDataStorage();
/* 294 */       n = ((ByteComponentRaster)localObject).getDataOffset(0);
/* 295 */       for (i1 = 0; i1 < k; i1++)
/* 296 */         arrayOfInt[i1] = this.srcLUT[(arrayOfByte[(n + i1)] & 0xFF)];
/*     */     }
/*     */     else
/*     */     {
/* 300 */       localObject = null;
/* 301 */       int m = 0;
/* 302 */       for (n = 0; n < j; n++) {
/* 303 */         for (i1 = 0; i1 < i; i1++) {
/* 304 */           localObject = this.biRaster.getDataElements(i1, n, localObject);
/* 305 */           arrayOfInt[(m++)] = this.cmodel.getRGB(localObject);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 310 */     SunWritableRaster.markDirty(localDataBufferInt);
/*     */ 
/* 312 */     this.isSameCM = false;
/* 313 */     this.cmodel = ColorModel.getRGBdefault();
/*     */ 
/* 315 */     Object localObject = { 16711680, 65280, 255, -16777216 };
/*     */ 
/* 320 */     this.biRaster = Raster.createPackedRaster(localDataBufferInt, i, j, i, (int[])localObject, null);
/*     */ 
/* 323 */     this.bimage = createImage(this.cmodel, this.biRaster, this.cmodel.isAlphaPremultiplied(), null);
/*     */ 
/* 325 */     this.srcLUT = null;
/* 326 */     this.isDefaultBI = true;
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt) {
/* 330 */     if (this.src != null) {
/* 331 */       this.src.checkSecurity(null, false);
/*     */     }
/* 333 */     this.hints = paramInt;
/*     */   }
/*     */ 
/*     */   private native boolean setICMpixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt5, int paramInt6, IntegerComponentRaster paramIntegerComponentRaster);
/*     */ 
/*     */   private native boolean setDiffICM(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6, IndexColorModel paramIndexColorModel, byte[] paramArrayOfByte, int paramInt7, int paramInt8, ByteComponentRaster paramByteComponentRaster, int paramInt9);
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 348 */     int i = paramInt5;
/*     */ 
/* 350 */     Object localObject1 = null;
/*     */ 
/* 352 */     if (this.src != null) {
/* 353 */       this.src.checkSecurity(null, false);
/*     */     }
/*     */ 
/* 357 */     synchronized (this) {
/* 358 */       if (this.bimage == null) {
/* 359 */         if (this.cmodel == null) {
/* 360 */           this.cmodel = paramColorModel;
/*     */         }
/* 362 */         createBufferedImage();
/*     */       }
/*     */ 
/* 365 */       if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 366 */         return;
/*     */       }
/*     */ 
/* 369 */       int k = this.biRaster.getWidth();
/* 370 */       int m = this.biRaster.getHeight();
/*     */ 
/* 372 */       int n = paramInt1 + paramInt3;
/* 373 */       int i1 = paramInt2 + paramInt4;
/* 374 */       if (paramInt1 < 0) {
/* 375 */         paramInt5 -= paramInt1;
/* 376 */         paramInt1 = 0;
/* 377 */       } else if (n < 0) {
/* 378 */         n = k;
/*     */       }
/* 380 */       if (paramInt2 < 0) {
/* 381 */         paramInt5 -= paramInt2 * paramInt6;
/* 382 */         paramInt2 = 0;
/* 383 */       } else if (i1 < 0) {
/* 384 */         i1 = m;
/*     */       }
/* 386 */       if (n > k) {
/* 387 */         n = k;
/*     */       }
/* 389 */       if (i1 > m) {
/* 390 */         i1 = m;
/*     */       }
/* 392 */       if ((paramInt1 >= n) || (paramInt2 >= i1)) {
/* 393 */         return;
/*     */       }
/*     */ 
/* 396 */       paramInt3 = n - paramInt1;
/* 397 */       paramInt4 = i1 - paramInt2;
/*     */ 
/* 399 */       if ((paramInt5 < 0) || (paramInt5 >= paramArrayOfByte.length))
/*     */       {
/* 401 */         throw new ArrayIndexOutOfBoundsException("Data offset out of bounds.");
/*     */       }
/*     */ 
/* 404 */       int i2 = paramArrayOfByte.length - paramInt5;
/* 405 */       if (i2 < paramInt3)
/*     */       {
/* 407 */         throw new ArrayIndexOutOfBoundsException("Data array is too short.");
/*     */       }
/*     */       int i3;
/* 410 */       if (paramInt6 < 0)
/* 411 */         i3 = paramInt5 / -paramInt6 + 1;
/* 412 */       else if (paramInt6 > 0)
/* 413 */         i3 = (i2 - paramInt3) / paramInt6 + 1;
/*     */       else {
/* 415 */         i3 = paramInt4;
/*     */       }
/* 417 */       if (paramInt4 > i3)
/*     */       {
/* 419 */         throw new ArrayIndexOutOfBoundsException("Data array is too short.");
/*     */       }
/*     */       Object localObject2;
/*     */       Object localObject3;
/*     */       boolean bool;
/*     */       int i9;
/* 422 */       if ((this.isSameCM) && (this.cmodel != paramColorModel) && (this.srcLUT != null) && ((paramColorModel instanceof IndexColorModel)) && ((this.biRaster instanceof ByteComponentRaster)))
/*     */       {
/* 426 */         localObject2 = (IndexColorModel)paramColorModel;
/* 427 */         localObject3 = (ByteComponentRaster)this.biRaster;
/* 428 */         int i6 = this.numSrcLUT;
/* 429 */         if (!setDiffICM(paramInt1, paramInt2, paramInt3, paramInt4, this.srcLUT, this.srcLUTtransIndex, this.numSrcLUT, (IndexColorModel)localObject2, paramArrayOfByte, paramInt5, paramInt6, (ByteComponentRaster)localObject3, ((ByteComponentRaster)localObject3).getDataOffset(0)))
/*     */         {
/* 433 */           convertToRGB();
/*     */         }
/*     */         else
/*     */         {
/* 438 */           ((ByteComponentRaster)localObject3).markDirty();
/* 439 */           if (i6 != this.numSrcLUT) {
/* 440 */             bool = ((IndexColorModel)localObject2).hasAlpha();
/* 441 */             if (this.srcLUTtransIndex != -1) {
/* 442 */               bool = true;
/*     */             }
/* 444 */             i9 = ((IndexColorModel)localObject2).getPixelSize();
/* 445 */             localObject2 = new IndexColorModel(i9, this.numSrcLUT, this.srcLUT, 0, bool, this.srcLUTtransIndex, i9 > 8 ? 1 : 0);
/*     */ 
/* 452 */             this.cmodel = ((ColorModel)localObject2);
/* 453 */             this.bimage = createImage((ColorModel)localObject2, (WritableRaster)localObject3, false, null);
/*     */           }
/*     */           return;
/*     */         }
/*     */       }
/*     */       int j;
/* 459 */       if (this.isDefaultBI)
/*     */       {
/* 461 */         localObject3 = (IntegerComponentRaster)this.biRaster;
/*     */         int[] arrayOfInt;
/* 463 */         if ((this.srcLUT != null) && ((paramColorModel instanceof IndexColorModel))) {
/* 464 */           if (paramColorModel != this.srcModel)
/*     */           {
/* 466 */             ((IndexColorModel)paramColorModel).getRGBs(this.srcLUT);
/* 467 */             this.srcModel = paramColorModel;
/*     */           }
/*     */ 
/* 470 */           if (s_useNative)
/*     */           {
/* 473 */             if (setICMpixels(paramInt1, paramInt2, paramInt3, paramInt4, this.srcLUT, paramArrayOfByte, paramInt5, paramInt6, (IntegerComponentRaster)localObject3))
/*     */             {
/* 476 */               ((IntegerComponentRaster)localObject3).markDirty();
/*     */             }
/* 478 */             else abort();
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 483 */             arrayOfInt = new int[paramInt3 * paramInt4];
/* 484 */             bool = false;
/*     */ 
/* 486 */             for (i9 = 0; i9 < paramInt4; )
/*     */             {
/* 488 */               j = i;
/* 489 */               for (int i10 = 0; i10 < paramInt3; i10++)
/* 490 */                 arrayOfInt[(bool++)] = this.srcLUT[(paramArrayOfByte[(j++)] & 0xFF)];
/* 486 */               i9++;
/* 487 */               i += paramInt6;
/*     */             }
/*     */ 
/* 493 */             ((IntegerComponentRaster)localObject3).setDataElements(paramInt1, paramInt2, paramInt3, paramInt4, arrayOfInt);
/*     */           }
/*     */         }
/*     */         else {
/* 497 */           arrayOfInt = new int[paramInt3];
/* 498 */           for (int i8 = paramInt2; i8 < paramInt2 + paramInt4; i += paramInt6) {
/* 499 */             j = i;
/* 500 */             for (i9 = 0; i9 < paramInt3; i9++) {
/* 501 */               arrayOfInt[i9] = paramColorModel.getRGB(paramArrayOfByte[(j++)] & 0xFF);
/*     */             }
/* 503 */             ((IntegerComponentRaster)localObject3).setDataElements(paramInt1, i8, paramInt3, 1, arrayOfInt);
/*     */ 
/* 498 */             i8++;
/*     */           }
/*     */ 
/* 505 */           this.availinfo |= 8;
/*     */         }
/*     */       }
/* 508 */       else if ((this.cmodel == paramColorModel) && ((this.biRaster instanceof ByteComponentRaster)) && (this.biRaster.getNumDataElements() == 1))
/*     */       {
/* 511 */         localObject2 = (ByteComponentRaster)this.biRaster;
/* 512 */         if ((paramInt5 == 0) && (paramInt6 == paramInt3)) {
/* 513 */           ((ByteComponentRaster)localObject2).putByteData(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
/*     */         }
/*     */         else {
/* 516 */           localObject3 = new byte[paramInt3];
/* 517 */           j = paramInt5;
/* 518 */           for (int i7 = paramInt2; i7 < paramInt2 + paramInt4; i7++) {
/* 519 */             System.arraycopy(paramArrayOfByte, j, localObject3, 0, paramInt3);
/* 520 */             ((ByteComponentRaster)localObject2).putByteData(paramInt1, i7, paramInt3, 1, (byte[])localObject3);
/* 521 */             j += paramInt6;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 526 */         for (int i4 = paramInt2; i4 < paramInt2 + paramInt4; i += paramInt6) {
/* 527 */           j = i;
/* 528 */           for (int i5 = paramInt1; i5 < paramInt1 + paramInt3; i5++)
/* 529 */             this.bimage.setRGB(i5, i4, paramColorModel.getRGB(paramArrayOfByte[(j++)] & 0xFF));
/* 526 */           i4++;
/*     */         }
/*     */ 
/* 533 */         this.availinfo |= 8;
/*     */       }
/*     */     }
/*     */ 
/* 537 */     if ((this.availinfo & 0x10) == 0)
/* 538 */       newInfo(this.image, 8, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 546 */     int i = paramInt5;
/*     */ 
/* 549 */     if (this.src != null) {
/* 550 */       this.src.checkSecurity(null, false);
/*     */     }
/*     */ 
/* 554 */     synchronized (this) {
/* 555 */       if (this.bimage == null) {
/* 556 */         if (this.cmodel == null) {
/* 557 */           this.cmodel = paramColorModel;
/*     */         }
/* 559 */         createBufferedImage();
/*     */       }
/*     */ 
/* 562 */       int[] arrayOfInt1 = new int[paramInt3];
/*     */ 
/* 566 */       if ((this.cmodel instanceof IndexColorModel))
/*     */       {
/* 569 */         convertToRGB();
/*     */       }
/*     */       Object localObject1;
/*     */       int k;
/* 572 */       if ((paramColorModel == this.cmodel) && ((this.biRaster instanceof IntegerComponentRaster)))
/*     */       {
/* 574 */         localObject1 = (IntegerComponentRaster)this.biRaster;
/*     */ 
/* 577 */         if ((paramInt5 == 0) && (paramInt6 == paramInt3)) {
/* 578 */           ((IntegerComponentRaster)localObject1).setDataElements(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
/*     */         }
/*     */         else
/*     */         {
/* 582 */           for (k = paramInt2; k < paramInt2 + paramInt4; i += paramInt6) {
/* 583 */             System.arraycopy(paramArrayOfInt, i, arrayOfInt1, 0, paramInt3);
/* 584 */             ((IntegerComponentRaster)localObject1).setDataElements(paramInt1, k, paramInt3, 1, arrayOfInt1);
/*     */ 
/* 582 */             k++;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 589 */         if ((paramColorModel.getTransparency() != 1) && (this.cmodel.getTransparency() == 1))
/*     */         {
/* 591 */           convertToRGB();
/*     */         }
/*     */         int j;
/* 594 */         if (this.isDefaultBI) {
/* 595 */           localObject1 = (IntegerComponentRaster)this.biRaster;
/*     */ 
/* 597 */           int[] arrayOfInt2 = ((IntegerComponentRaster)localObject1).getDataStorage();
/*     */           int i1;
/* 598 */           if (this.cmodel.equals(paramColorModel)) {
/* 599 */             i1 = ((IntegerComponentRaster)localObject1).getScanlineStride();
/* 600 */             int i2 = paramInt2 * i1 + paramInt1;
/* 601 */             for (k = 0; k < paramInt4; i += paramInt6) {
/* 602 */               System.arraycopy(paramArrayOfInt, i, arrayOfInt2, i2, paramInt3);
/* 603 */               i2 += i1;
/*     */ 
/* 601 */               k++;
/*     */             }
/*     */ 
/* 607 */             ((IntegerComponentRaster)localObject1).markDirty();
/*     */           }
/*     */           else {
/* 610 */             for (k = paramInt2; k < paramInt2 + paramInt4; i += paramInt6) {
/* 611 */               j = i;
/* 612 */               for (i1 = 0; i1 < paramInt3; i1++) {
/* 613 */                 arrayOfInt1[i1] = paramColorModel.getRGB(paramArrayOfInt[(j++)]);
/*     */               }
/* 615 */               ((IntegerComponentRaster)localObject1).setDataElements(paramInt1, k, paramInt3, 1, arrayOfInt1);
/*     */ 
/* 610 */               k++;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 619 */           this.availinfo |= 8;
/*     */         }
/*     */         else {
/* 622 */           localObject1 = null;
/*     */ 
/* 624 */           for (k = paramInt2; k < paramInt2 + paramInt4; i += paramInt6) {
/* 625 */             j = i;
/* 626 */             for (int n = paramInt1; n < paramInt1 + paramInt3; n++) {
/* 627 */               int m = paramColorModel.getRGB(paramArrayOfInt[(j++)]);
/* 628 */               localObject1 = this.cmodel.getDataElements(m, localObject1);
/* 629 */               this.biRaster.setDataElements(n, k, localObject1);
/*     */             }
/* 624 */             k++;
/*     */           }
/*     */ 
/* 632 */           this.availinfo |= 8;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 639 */     if ((this.availinfo & 0x10) == 0)
/* 640 */       newInfo(this.image, 8, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public BufferedImage getOpaqueRGBImage()
/*     */   {
/* 645 */     if (this.bimage.getType() == 2) {
/* 646 */       int i = this.bimage.getWidth();
/* 647 */       int j = this.bimage.getHeight();
/* 648 */       int k = i * j;
/*     */ 
/* 651 */       DataBufferInt localDataBufferInt = (DataBufferInt)this.biRaster.getDataBuffer();
/* 652 */       int[] arrayOfInt1 = SunWritableRaster.stealData(localDataBufferInt, 0);
/*     */ 
/* 654 */       for (int m = 0; m < k; m++) {
/* 655 */         if (arrayOfInt1[m] >>> 24 != 255) {
/* 656 */           return this.bimage;
/*     */         }
/*     */       }
/*     */ 
/* 660 */       DirectColorModel localDirectColorModel = new DirectColorModel(24, 16711680, 65280, 255);
/*     */ 
/* 665 */       int[] arrayOfInt2 = { 16711680, 65280, 255 };
/* 666 */       WritableRaster localWritableRaster = Raster.createPackedRaster(localDataBufferInt, i, j, i, arrayOfInt2, null);
/*     */       try
/*     */       {
/* 671 */         return createImage(localDirectColorModel, localWritableRaster, false, null);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 675 */         return this.bimage;
/*     */       }
/*     */     }
/* 678 */     return this.bimage;
/*     */   }
/*     */ 
/*     */   public void imageComplete(int paramInt)
/*     */   {
/* 684 */     if (this.src != null)
/* 685 */       this.src.checkSecurity(null, false);
/*     */     int i;
/*     */     int j;
/* 689 */     switch (paramInt) {
/*     */     case 4:
/*     */     default:
/* 692 */       i = 1;
/* 693 */       j = 128;
/* 694 */       break;
/*     */     case 1:
/* 696 */       this.image.addInfo(64);
/* 697 */       i = 1;
/* 698 */       j = 64;
/* 699 */       dispose();
/* 700 */       break;
/*     */     case 3:
/* 702 */       i = 1;
/* 703 */       j = 32;
/* 704 */       break;
/*     */     case 2:
/* 706 */       i = 0;
/* 707 */       j = 16;
/*     */     }
/*     */ 
/* 710 */     synchronized (this) {
/* 711 */       if (i != 0) {
/* 712 */         this.image.getSource().removeConsumer(this);
/* 713 */         this.consuming = false;
/* 714 */         this.newbits = null;
/*     */ 
/* 716 */         if (this.bimage != null) {
/* 717 */           this.bimage = getOpaqueRGBImage();
/*     */         }
/*     */       }
/* 720 */       this.availinfo |= j;
/* 721 */       notifyAll();
/*     */     }
/*     */ 
/* 724 */     newInfo(this.image, j, 0, 0, this.width, this.height);
/*     */ 
/* 726 */     this.image.infoDone(paramInt);
/*     */   }
/*     */ 
/*     */   void startProduction() {
/* 730 */     if (!this.consuming) {
/* 731 */       this.consuming = true;
/* 732 */       this.image.getSource().startProduction(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void checkConsumption()
/*     */   {
/* 739 */     if ((isWatcherListEmpty()) && (this.numWaiters == 0) && ((this.availinfo & 0x20) == 0))
/*     */     {
/* 742 */       dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void notifyWatcherListEmpty() {
/* 747 */     checkConsumption();
/*     */   }
/*     */ 
/*     */   private synchronized void decrementWaiters() {
/* 751 */     this.numWaiters -= 1;
/* 752 */     checkConsumption();
/*     */   }
/*     */ 
/*     */   public boolean prepare(ImageObserver paramImageObserver) {
/* 756 */     if (this.src != null) {
/* 757 */       this.src.checkSecurity(null, false);
/*     */     }
/* 759 */     if ((this.availinfo & 0x40) != 0) {
/* 760 */       if (paramImageObserver != null) {
/* 761 */         paramImageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
/*     */       }
/*     */ 
/* 764 */       return false;
/*     */     }
/* 766 */     boolean bool = (this.availinfo & 0x20) != 0;
/* 767 */     if (!bool) {
/* 768 */       addWatcher(paramImageObserver);
/* 769 */       startProduction();
/*     */ 
/* 771 */       bool = (this.availinfo & 0x20) != 0;
/*     */     }
/* 773 */     return bool;
/*     */   }
/*     */ 
/*     */   public int check(ImageObserver paramImageObserver)
/*     */   {
/* 778 */     if (this.src != null) {
/* 779 */       this.src.checkSecurity(null, false);
/*     */     }
/* 781 */     if ((this.availinfo & 0x60) == 0) {
/* 782 */       addWatcher(paramImageObserver);
/*     */     }
/*     */ 
/* 785 */     return this.availinfo;
/*     */   }
/*     */ 
/*     */   public boolean drawToBufImage(Graphics paramGraphics, ToolkitImage paramToolkitImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver)
/*     */   {
/* 792 */     if (this.src != null) {
/* 793 */       this.src.checkSecurity(null, false);
/*     */     }
/* 795 */     if ((this.availinfo & 0x40) != 0) {
/* 796 */       if (paramImageObserver != null) {
/* 797 */         paramImageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
/*     */       }
/*     */ 
/* 800 */       return false;
/*     */     }
/* 802 */     boolean bool = (this.availinfo & 0x20) != 0;
/* 803 */     int i = (this.availinfo & 0x80) != 0 ? 1 : 0;
/*     */ 
/* 805 */     if ((!bool) && (i == 0)) {
/* 806 */       addWatcher(paramImageObserver);
/* 807 */       startProduction();
/*     */ 
/* 809 */       bool = (this.availinfo & 0x20) != 0;
/*     */     }
/*     */ 
/* 812 */     if ((bool) || (0 != (this.availinfo & 0x10))) {
/* 813 */       paramGraphics.drawImage(this.bimage, paramInt1, paramInt2, paramColor, null);
/*     */     }
/*     */ 
/* 816 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean drawToBufImage(Graphics paramGraphics, ToolkitImage paramToolkitImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver)
/*     */   {
/* 823 */     if (this.src != null) {
/* 824 */       this.src.checkSecurity(null, false);
/*     */     }
/* 826 */     if ((this.availinfo & 0x40) != 0) {
/* 827 */       if (paramImageObserver != null) {
/* 828 */         paramImageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
/*     */       }
/*     */ 
/* 831 */       return false;
/*     */     }
/*     */ 
/* 834 */     boolean bool = (this.availinfo & 0x20) != 0;
/* 835 */     int i = (this.availinfo & 0x80) != 0 ? 1 : 0;
/*     */ 
/* 837 */     if ((!bool) && (i == 0)) {
/* 838 */       addWatcher(paramImageObserver);
/* 839 */       startProduction();
/*     */ 
/* 841 */       bool = (this.availinfo & 0x20) != 0;
/*     */     }
/*     */ 
/* 844 */     if ((bool) || (0 != (this.availinfo & 0x10))) {
/* 845 */       paramGraphics.drawImage(this.bimage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, null);
/*     */     }
/*     */ 
/* 848 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean drawToBufImage(Graphics paramGraphics, ToolkitImage paramToolkitImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*     */   {
/* 856 */     if (this.src != null) {
/* 857 */       this.src.checkSecurity(null, false);
/*     */     }
/* 859 */     if ((this.availinfo & 0x40) != 0) {
/* 860 */       if (paramImageObserver != null) {
/* 861 */         paramImageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
/*     */       }
/*     */ 
/* 864 */       return false;
/*     */     }
/* 866 */     boolean bool = (this.availinfo & 0x20) != 0;
/* 867 */     int i = (this.availinfo & 0x80) != 0 ? 1 : 0;
/*     */ 
/* 869 */     if ((!bool) && (i == 0)) {
/* 870 */       addWatcher(paramImageObserver);
/* 871 */       startProduction();
/*     */ 
/* 873 */       bool = (this.availinfo & 0x20) != 0;
/*     */     }
/*     */ 
/* 876 */     if ((bool) || (0 != (this.availinfo & 0x10))) {
/* 877 */       paramGraphics.drawImage(this.bimage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, null);
/*     */     }
/*     */ 
/* 883 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean drawToBufImage(Graphics paramGraphics, ToolkitImage paramToolkitImage, AffineTransform paramAffineTransform, ImageObserver paramImageObserver)
/*     */   {
/* 890 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*     */ 
/* 892 */     if (this.src != null) {
/* 893 */       this.src.checkSecurity(null, false);
/*     */     }
/* 895 */     if ((this.availinfo & 0x40) != 0) {
/* 896 */       if (paramImageObserver != null) {
/* 897 */         paramImageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
/*     */       }
/*     */ 
/* 900 */       return false;
/*     */     }
/* 902 */     boolean bool = (this.availinfo & 0x20) != 0;
/* 903 */     int i = (this.availinfo & 0x80) != 0 ? 1 : 0;
/*     */ 
/* 905 */     if ((!bool) && (i == 0)) {
/* 906 */       addWatcher(paramImageObserver);
/* 907 */       startProduction();
/*     */ 
/* 909 */       bool = (this.availinfo & 0x20) != 0;
/*     */     }
/*     */ 
/* 912 */     if ((bool) || (0 != (this.availinfo & 0x10))) {
/* 913 */       localGraphics2D.drawImage(this.bimage, paramAffineTransform, null);
/*     */     }
/*     */ 
/* 916 */     return bool;
/*     */   }
/*     */ 
/*     */   synchronized void abort() {
/* 920 */     this.image.getSource().removeConsumer(this);
/* 921 */     this.consuming = false;
/* 922 */     this.newbits = null;
/* 923 */     this.bimage = null;
/* 924 */     this.biRaster = null;
/* 925 */     this.cmodel = null;
/* 926 */     this.srcLUT = null;
/* 927 */     this.isDefaultBI = false;
/* 928 */     this.isSameCM = false;
/*     */ 
/* 930 */     newInfo(this.image, 128, -1, -1, -1, -1);
/* 931 */     this.availinfo &= -121;
/*     */   }
/*     */ 
/*     */   synchronized void dispose()
/*     */   {
/* 938 */     this.image.getSource().removeConsumer(this);
/* 939 */     this.consuming = false;
/* 940 */     this.newbits = null;
/* 941 */     this.availinfo &= -57;
/*     */   }
/*     */ 
/*     */   public void setAccelerationPriority(float paramFloat)
/*     */   {
/* 947 */     if (this.bimage != null)
/* 948 */       this.bimage.setAccelerationPriority(paramFloat);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  82 */     NativeLibLoader.loadLibraries();
/*  83 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ImageRepresentation
 * JD-Core Version:    0.6.2
 */