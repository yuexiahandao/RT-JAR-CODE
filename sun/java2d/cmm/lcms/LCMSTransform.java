/*     */ package sun.java2d.cmm.lcms;
/*     */ 
/*     */ import java.awt.color.CMMException;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.color.ICC_Profile;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.java2d.cmm.ColorTransform;
/*     */ import sun.java2d.cmm.ProfileDeferralMgr;
/*     */ 
/*     */ public class LCMSTransform
/*     */   implements ColorTransform
/*     */ {
/*     */   long ID;
/*     */   private int inFormatter;
/*     */   private int outFormatter;
/*     */   ICC_Profile[] profiles;
/*     */   long[] profileIDs;
/*     */   int renderType;
/*     */   int transformType;
/*  67 */   private int numInComponents = -1;
/*  68 */   private int numOutComponents = -1;
/*     */ 
/*  70 */   private Object disposerReferent = new Object();
/*     */ 
/*     */   public LCMSTransform(ICC_Profile paramICC_Profile, int paramInt1, int paramInt2)
/*     */   {
/*  83 */     this.profiles = new ICC_Profile[1];
/*  84 */     this.profiles[0] = paramICC_Profile;
/*  85 */     this.profileIDs = new long[1];
/*  86 */     this.profileIDs[0] = LCMS.getProfileID(paramICC_Profile);
/*  87 */     this.renderType = (paramInt1 == -1 ? 0 : paramInt1);
/*     */ 
/*  89 */     this.transformType = paramInt2;
/*     */ 
/*  96 */     this.numInComponents = this.profiles[0].getNumComponents();
/*  97 */     this.numOutComponents = this.profiles[(this.profiles.length - 1)].getNumComponents();
/*     */   }
/*     */ 
/*     */   public LCMSTransform(ColorTransform[] paramArrayOfColorTransform) {
/* 101 */     int i = 0;
/* 102 */     for (int j = 0; j < paramArrayOfColorTransform.length; j++) {
/* 103 */       i += ((LCMSTransform)paramArrayOfColorTransform[j]).profiles.length;
/*     */     }
/* 105 */     this.profiles = new ICC_Profile[i];
/* 106 */     this.profileIDs = new long[i];
/* 107 */     j = 0;
/* 108 */     for (int k = 0; k < paramArrayOfColorTransform.length; k++) {
/* 109 */       LCMSTransform localLCMSTransform = (LCMSTransform)paramArrayOfColorTransform[k];
/* 110 */       System.arraycopy(localLCMSTransform.profiles, 0, this.profiles, j, localLCMSTransform.profiles.length);
/*     */ 
/* 112 */       System.arraycopy(localLCMSTransform.profileIDs, 0, this.profileIDs, j, localLCMSTransform.profileIDs.length);
/*     */ 
/* 114 */       j += localLCMSTransform.profiles.length;
/*     */     }
/* 116 */     this.renderType = ((LCMSTransform)paramArrayOfColorTransform[0]).renderType;
/*     */ 
/* 123 */     this.numInComponents = this.profiles[0].getNumComponents();
/* 124 */     this.numOutComponents = this.profiles[(this.profiles.length - 1)].getNumComponents();
/*     */   }
/*     */ 
/*     */   public int getNumInComponents() {
/* 128 */     return this.numInComponents;
/*     */   }
/*     */ 
/*     */   public int getNumOutComponents() {
/* 132 */     return this.numOutComponents;
/*     */   }
/*     */ 
/*     */   private synchronized void doTransform(LCMSImageLayout paramLCMSImageLayout1, LCMSImageLayout paramLCMSImageLayout2)
/*     */   {
/* 138 */     if ((this.ID == 0L) || (this.inFormatter != paramLCMSImageLayout1.pixelType) || (this.outFormatter != paramLCMSImageLayout2.pixelType))
/*     */     {
/* 142 */       if (this.ID != 0L)
/*     */       {
/* 144 */         this.disposerReferent = new Object();
/*     */       }
/* 146 */       this.inFormatter = paramLCMSImageLayout1.pixelType;
/* 147 */       this.outFormatter = paramLCMSImageLayout2.pixelType;
/*     */ 
/* 149 */       this.ID = LCMS.createNativeTransform(this.profileIDs, this.renderType, this.inFormatter, this.outFormatter, this.disposerReferent);
/*     */     }
/*     */ 
/* 154 */     LCMS.colorConvert(this, paramLCMSImageLayout1, paramLCMSImageLayout2);
/*     */   }
/*     */ 
/*     */   public void colorConvert(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2) {
/* 158 */     if ((LCMSImageLayout.isSupported(paramBufferedImage1)) && (LCMSImageLayout.isSupported(paramBufferedImage2)))
/*     */     {
/*     */       try
/*     */       {
/* 162 */         doTransform(new LCMSImageLayout(paramBufferedImage1), new LCMSImageLayout(paramBufferedImage2));
/* 163 */         return;
/*     */       } catch (LCMSImageLayout.ImageLayoutException localImageLayoutException1) {
/* 165 */         throw new CMMException("Unable to convert images");
/*     */       }
/*     */     }
/*     */ 
/* 169 */     WritableRaster localWritableRaster1 = paramBufferedImage1.getRaster();
/* 170 */     WritableRaster localWritableRaster2 = paramBufferedImage2.getRaster();
/* 171 */     ColorModel localColorModel1 = paramBufferedImage1.getColorModel();
/* 172 */     ColorModel localColorModel2 = paramBufferedImage2.getColorModel();
/* 173 */     int i = paramBufferedImage1.getWidth();
/* 174 */     int j = paramBufferedImage1.getHeight();
/* 175 */     int k = localColorModel1.getNumColorComponents();
/* 176 */     int m = localColorModel2.getNumColorComponents();
/* 177 */     int n = 8;
/* 178 */     float f = 255.0F;
/* 179 */     for (int i1 = 0; i1 < k; i1++) {
/* 180 */       if (localColorModel1.getComponentSize(i1) > 8) {
/* 181 */         n = 16;
/* 182 */         f = 65535.0F;
/*     */       }
/*     */     }
/* 185 */     for (i1 = 0; i1 < m; i1++) {
/* 186 */       if (localColorModel2.getComponentSize(i1) > 8) {
/* 187 */         n = 16;
/* 188 */         f = 65535.0F;
/*     */       }
/*     */     }
/* 191 */     float[] arrayOfFloat1 = new float[k];
/* 192 */     float[] arrayOfFloat2 = new float[k];
/* 193 */     ColorSpace localColorSpace = localColorModel1.getColorSpace();
/* 194 */     for (int i2 = 0; i2 < k; i2++) {
/* 195 */       arrayOfFloat1[i2] = localColorSpace.getMinValue(i2);
/* 196 */       arrayOfFloat2[i2] = (f / (localColorSpace.getMaxValue(i2) - arrayOfFloat1[i2]));
/*     */     }
/* 198 */     localColorSpace = localColorModel2.getColorSpace();
/* 199 */     float[] arrayOfFloat3 = new float[m];
/* 200 */     float[] arrayOfFloat4 = new float[m];
/* 201 */     for (int i3 = 0; i3 < m; i3++) {
/* 202 */       arrayOfFloat3[i3] = localColorSpace.getMinValue(i3);
/* 203 */       arrayOfFloat4[i3] = ((localColorSpace.getMaxValue(i3) - arrayOfFloat3[i3]) / f);
/*     */     }
/* 205 */     boolean bool = localColorModel2.hasAlpha();
/* 206 */     int i4 = (localColorModel1.hasAlpha()) && (bool) ? 1 : 0;
/*     */     float[] arrayOfFloat5;
/* 208 */     if (bool)
/* 209 */       arrayOfFloat5 = new float[m + 1];
/*     */     else
/* 211 */       arrayOfFloat5 = new float[m];
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     float[] arrayOfFloat7;
/*     */     LCMSImageLayout localLCMSImageLayout1;
/*     */     LCMSImageLayout localLCMSImageLayout2;
/*     */     Object localObject3;
/*     */     float[] arrayOfFloat6;
/*     */     int i5;
/*     */     int i8;
/*     */     int i9;
/* 213 */     if (n == 8) {
/* 214 */       localObject1 = new byte[i * k];
/* 215 */       localObject2 = new byte[i * m];
/*     */ 
/* 218 */       arrayOfFloat7 = null;
/* 219 */       if (i4 != 0) {
/* 220 */         arrayOfFloat7 = new float[i];
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 225 */         localLCMSImageLayout1 = new LCMSImageLayout((byte[])localObject1, localObject1.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(1), getNumInComponents());
/*     */ 
/* 229 */         localLCMSImageLayout2 = new LCMSImageLayout((byte[])localObject2, localObject2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(1), getNumOutComponents());
/*     */       }
/*     */       catch (LCMSImageLayout.ImageLayoutException localImageLayoutException2)
/*     */       {
/* 234 */         throw new CMMException("Unable to convert images");
/*     */       }
/*     */ 
/* 237 */       for (int i6 = 0; i6 < j; i6++)
/*     */       {
/* 239 */         localObject3 = null;
/* 240 */         arrayOfFloat6 = null;
/* 241 */         i5 = 0;
/* 242 */         for (i8 = 0; i8 < i; i8++) {
/* 243 */           localObject3 = localWritableRaster1.getDataElements(i8, i6, localObject3);
/* 244 */           arrayOfFloat6 = localColorModel1.getNormalizedComponents(localObject3, arrayOfFloat6, 0);
/* 245 */           for (i9 = 0; i9 < k; i9++) {
/* 246 */             localObject1[(i5++)] = ((byte)(int)((arrayOfFloat6[i9] - arrayOfFloat1[i9]) * arrayOfFloat2[i9] + 0.5F));
/*     */           }
/*     */ 
/* 250 */           if (i4 != 0) {
/* 251 */             arrayOfFloat7[i8] = arrayOfFloat6[k];
/*     */           }
/*     */         }
/*     */ 
/* 255 */         doTransform(localLCMSImageLayout1, localLCMSImageLayout2);
/*     */ 
/* 258 */         localObject3 = null;
/* 259 */         i5 = 0;
/* 260 */         for (i8 = 0; i8 < i; i8++) {
/* 261 */           for (i9 = 0; i9 < m; i9++) {
/* 262 */             arrayOfFloat5[i9] = ((localObject2[(i5++)] & 0xFF) * arrayOfFloat4[i9] + arrayOfFloat3[i9]);
/*     */           }
/*     */ 
/* 265 */           if (i4 != 0)
/* 266 */             arrayOfFloat5[m] = arrayOfFloat7[i8];
/* 267 */           else if (bool) {
/* 268 */             arrayOfFloat5[m] = 1.0F;
/*     */           }
/* 270 */           localObject3 = localColorModel2.getDataElements(arrayOfFloat5, 0, localObject3);
/* 271 */           localWritableRaster2.setDataElements(i8, i6, localObject3);
/*     */         }
/*     */       }
/*     */     } else {
/* 275 */       localObject1 = new short[i * k];
/* 276 */       localObject2 = new short[i * m];
/*     */ 
/* 279 */       arrayOfFloat7 = null;
/* 280 */       if (i4 != 0) {
/* 281 */         arrayOfFloat7 = new float[i];
/*     */       }
/*     */       try
/*     */       {
/* 285 */         localLCMSImageLayout1 = new LCMSImageLayout((short[])localObject1, localObject1.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(2), getNumInComponents() * 2);
/*     */ 
/* 290 */         localLCMSImageLayout2 = new LCMSImageLayout((short[])localObject2, localObject2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(2), getNumOutComponents() * 2);
/*     */       }
/*     */       catch (LCMSImageLayout.ImageLayoutException localImageLayoutException3)
/*     */       {
/* 295 */         throw new CMMException("Unable to convert images");
/*     */       }
/*     */ 
/* 298 */       for (int i7 = 0; i7 < j; i7++)
/*     */       {
/* 300 */         localObject3 = null;
/* 301 */         arrayOfFloat6 = null;
/* 302 */         i5 = 0;
/* 303 */         for (i8 = 0; i8 < i; i8++) {
/* 304 */           localObject3 = localWritableRaster1.getDataElements(i8, i7, localObject3);
/* 305 */           arrayOfFloat6 = localColorModel1.getNormalizedComponents(localObject3, arrayOfFloat6, 0);
/* 306 */           for (i9 = 0; i9 < k; i9++) {
/* 307 */             localObject1[(i5++)] = ((short)(int)((arrayOfFloat6[i9] - arrayOfFloat1[i9]) * arrayOfFloat2[i9] + 0.5F));
/*     */           }
/*     */ 
/* 311 */           if (i4 != 0) {
/* 312 */             arrayOfFloat7[i8] = arrayOfFloat6[k];
/*     */           }
/*     */         }
/*     */ 
/* 316 */         doTransform(localLCMSImageLayout1, localLCMSImageLayout2);
/*     */ 
/* 319 */         localObject3 = null;
/* 320 */         i5 = 0;
/* 321 */         for (i8 = 0; i8 < i; i8++) {
/* 322 */           for (i9 = 0; i9 < m; i9++) {
/* 323 */             arrayOfFloat5[i9] = ((localObject2[(i5++)] & 0xFFFF) * arrayOfFloat4[i9] + arrayOfFloat3[i9]);
/*     */           }
/*     */ 
/* 326 */           if (i4 != 0)
/* 327 */             arrayOfFloat5[m] = arrayOfFloat7[i8];
/* 328 */           else if (bool) {
/* 329 */             arrayOfFloat5[m] = 1.0F;
/*     */           }
/* 331 */           localObject3 = localColorModel2.getDataElements(arrayOfFloat5, 0, localObject3);
/* 332 */           localWritableRaster2.setDataElements(i8, i7, localObject3);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void colorConvert(Raster paramRaster, WritableRaster paramWritableRaster, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
/*     */   {
/* 344 */     SampleModel localSampleModel1 = paramRaster.getSampleModel();
/* 345 */     SampleModel localSampleModel2 = paramWritableRaster.getSampleModel();
/* 346 */     int i = paramRaster.getTransferType();
/* 347 */     int j = paramWritableRaster.getTransferType();
/*     */     int k;
/* 349 */     if ((i == 4) || (i == 5))
/*     */     {
/* 351 */       k = 1;
/*     */     }
/* 353 */     else k = 0;
/*     */     int m;
/* 355 */     if ((j == 4) || (j == 5))
/*     */     {
/* 357 */       m = 1;
/*     */     }
/* 359 */     else m = 0;
/*     */ 
/* 361 */     int n = paramRaster.getWidth();
/* 362 */     int i1 = paramRaster.getHeight();
/* 363 */     int i2 = paramRaster.getNumBands();
/* 364 */     int i3 = paramWritableRaster.getNumBands();
/* 365 */     float[] arrayOfFloat1 = new float[i2];
/* 366 */     float[] arrayOfFloat2 = new float[i3];
/* 367 */     float[] arrayOfFloat3 = new float[i2];
/* 368 */     float[] arrayOfFloat4 = new float[i3];
/* 369 */     for (int i4 = 0; i4 < i2; i4++) {
/* 370 */       if (k != 0) {
/* 371 */         arrayOfFloat1[i4] = (65535.0F / (paramArrayOfFloat2[i4] - paramArrayOfFloat1[i4]));
/* 372 */         arrayOfFloat3[i4] = paramArrayOfFloat1[i4];
/*     */       } else {
/* 374 */         if (i == 2)
/* 375 */           arrayOfFloat1[i4] = 2.000031F;
/*     */         else {
/* 377 */           arrayOfFloat1[i4] = (65535.0F / ((1 << localSampleModel1.getSampleSize(i4)) - 1));
/*     */         }
/*     */ 
/* 380 */         arrayOfFloat3[i4] = 0.0F;
/*     */       }
/*     */     }
/* 383 */     for (i4 = 0; i4 < i3; i4++) {
/* 384 */       if (m != 0) {
/* 385 */         arrayOfFloat2[i4] = ((paramArrayOfFloat4[i4] - paramArrayOfFloat3[i4]) / 65535.0F);
/* 386 */         arrayOfFloat4[i4] = paramArrayOfFloat3[i4];
/*     */       } else {
/* 388 */         if (j == 2)
/* 389 */           arrayOfFloat2[i4] = 0.4999924F;
/*     */         else {
/* 391 */           arrayOfFloat2[i4] = (((1 << localSampleModel2.getSampleSize(i4)) - 1) / 65535.0F);
/*     */         }
/*     */ 
/* 395 */         arrayOfFloat4[i4] = 0.0F; }  } i4 = paramRaster.getMinY();
/* 399 */     int i5 = paramWritableRaster.getMinY();
/*     */ 
/* 402 */     short[] arrayOfShort1 = new short[n * i2];
/* 403 */     short[] arrayOfShort2 = new short[n * i3];
/*     */     LCMSImageLayout localLCMSImageLayout1;
/*     */     LCMSImageLayout localLCMSImageLayout2;
/*     */     try { localLCMSImageLayout1 = new LCMSImageLayout(arrayOfShort1, arrayOfShort1.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(2), getNumInComponents() * 2);
/*     */ 
/* 411 */       localLCMSImageLayout2 = new LCMSImageLayout(arrayOfShort2, arrayOfShort2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(2), getNumOutComponents() * 2);
/*     */     }
/*     */     catch (LCMSImageLayout.ImageLayoutException localImageLayoutException)
/*     */     {
/* 416 */       throw new CMMException("Unable to convert rasters");
/*     */     }
/*     */ 
/* 419 */     for (int i9 = 0; i9 < i1; i5++)
/*     */     {
/* 421 */       int i6 = paramRaster.getMinX();
/* 422 */       int i8 = 0;
/*     */       int i11;
/*     */       float f;
/* 423 */       for (int i10 = 0; i10 < n; i6++) {
/* 424 */         for (i11 = 0; i11 < i2; i11++) {
/* 425 */           f = paramRaster.getSampleFloat(i6, i4, i11);
/* 426 */           arrayOfShort1[(i8++)] = ((short)(int)((f - arrayOfFloat3[i11]) * arrayOfFloat1[i11] + 0.5F));
/*     */         }
/* 423 */         i10++;
/*     */       }
/*     */ 
/* 432 */       doTransform(localLCMSImageLayout1, localLCMSImageLayout2);
/*     */ 
/* 435 */       int i7 = paramWritableRaster.getMinX();
/* 436 */       i8 = 0;
/* 437 */       for (i10 = 0; i10 < n; i7++) {
/* 438 */         for (i11 = 0; i11 < i3; i11++) {
/* 439 */           f = (arrayOfShort2[(i8++)] & 0xFFFF) * arrayOfFloat2[i11] + arrayOfFloat4[i11];
/*     */ 
/* 441 */           paramWritableRaster.setSample(i7, i5, i11, f);
/*     */         }
/* 437 */         i10++;
/*     */       }
/* 419 */       i9++; i4++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void colorConvert(Raster paramRaster, WritableRaster paramWritableRaster)
/*     */   {
/* 451 */     SampleModel localSampleModel1 = paramRaster.getSampleModel();
/* 452 */     SampleModel localSampleModel2 = paramWritableRaster.getSampleModel();
/* 453 */     int i = paramRaster.getTransferType();
/* 454 */     int j = paramWritableRaster.getTransferType();
/* 455 */     int k = paramRaster.getWidth();
/* 456 */     int m = paramRaster.getHeight();
/* 457 */     int n = paramRaster.getNumBands();
/* 458 */     int i1 = paramWritableRaster.getNumBands();
/* 459 */     int i2 = 8;
/* 460 */     float f = 255.0F;
/* 461 */     for (int i3 = 0; i3 < n; i3++) {
/* 462 */       if (localSampleModel1.getSampleSize(i3) > 8) {
/* 463 */         i2 = 16;
/* 464 */         f = 65535.0F;
/*     */       }
/*     */     }
/* 467 */     for (i3 = 0; i3 < i1; i3++) {
/* 468 */       if (localSampleModel2.getSampleSize(i3) > 8) {
/* 469 */         i2 = 16;
/* 470 */         f = 65535.0F;
/*     */       }
/*     */     }
/* 473 */     float[] arrayOfFloat1 = new float[n];
/* 474 */     float[] arrayOfFloat2 = new float[i1];
/* 475 */     for (int i4 = 0; i4 < n; i4++) {
/* 476 */       if (i == 2)
/* 477 */         arrayOfFloat1[i4] = (f / 32767.0F);
/*     */       else {
/* 479 */         arrayOfFloat1[i4] = (f / ((1 << localSampleModel1.getSampleSize(i4)) - 1));
/*     */       }
/*     */     }
/*     */ 
/* 483 */     for (i4 = 0; i4 < i1; i4++) {
/* 484 */       if (j == 2)
/* 485 */         arrayOfFloat2[i4] = (32767.0F / f);
/*     */       else {
/* 487 */         arrayOfFloat2[i4] = (((1 << localSampleModel2.getSampleSize(i4)) - 1) / f);
/*     */       }
/*     */     }
/*     */ 
/* 491 */     i4 = paramRaster.getMinY();
/* 492 */     int i5 = paramWritableRaster.getMinY();
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     LCMSImageLayout localLCMSImageLayout1;
/*     */     LCMSImageLayout localLCMSImageLayout2;
/*     */     int i6;
/*     */     int i9;
/*     */     int i12;
/*     */     int i13;
/*     */     int i8;
/*     */     int i7;
/* 495 */     if (i2 == 8) {
/* 496 */       localObject1 = new byte[k * n];
/* 497 */       localObject2 = new byte[k * i1];
/*     */       try
/*     */       {
/* 501 */         localLCMSImageLayout1 = new LCMSImageLayout((byte[])localObject1, localObject1.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(1), getNumInComponents());
/*     */ 
/* 505 */         localLCMSImageLayout2 = new LCMSImageLayout((byte[])localObject2, localObject2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(1), getNumOutComponents());
/*     */       }
/*     */       catch (LCMSImageLayout.ImageLayoutException localImageLayoutException1)
/*     */       {
/* 510 */         throw new CMMException("Unable to convert rasters");
/*     */       }
/*     */ 
/* 513 */       for (int i10 = 0; i10 < m; i5++)
/*     */       {
/* 515 */         i6 = paramRaster.getMinX();
/* 516 */         i9 = 0;
/* 517 */         for (i12 = 0; i12 < k; i6++) {
/* 518 */           for (i13 = 0; i13 < n; i13++) {
/* 519 */             i8 = paramRaster.getSample(i6, i4, i13);
/* 520 */             localObject1[(i9++)] = ((byte)(int)(i8 * arrayOfFloat1[i13] + 0.5F));
/*     */           }
/* 517 */           i12++;
/*     */         }
/*     */ 
/* 526 */         doTransform(localLCMSImageLayout1, localLCMSImageLayout2);
/*     */ 
/* 529 */         i7 = paramWritableRaster.getMinX();
/* 530 */         i9 = 0;
/* 531 */         for (i12 = 0; i12 < k; i7++) {
/* 532 */           for (i13 = 0; i13 < i1; i13++) {
/* 533 */             i8 = (int)((localObject2[(i9++)] & 0xFF) * arrayOfFloat2[i13] + 0.5F);
/*     */ 
/* 535 */             paramWritableRaster.setSample(i7, i5, i13, i8);
/*     */           }
/* 531 */           i12++;
/*     */         }
/* 513 */         i10++; i4++;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 540 */       localObject1 = new short[k * n];
/* 541 */       localObject2 = new short[k * i1];
/*     */       try
/*     */       {
/* 545 */         localLCMSImageLayout1 = new LCMSImageLayout((short[])localObject1, localObject1.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(2), getNumInComponents() * 2);
/*     */ 
/* 550 */         localLCMSImageLayout2 = new LCMSImageLayout((short[])localObject2, localObject2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(2), getNumOutComponents() * 2);
/*     */       }
/*     */       catch (LCMSImageLayout.ImageLayoutException localImageLayoutException2)
/*     */       {
/* 555 */         throw new CMMException("Unable to convert rasters");
/*     */       }
/*     */ 
/* 558 */       for (int i11 = 0; i11 < m; i5++)
/*     */       {
/* 560 */         i6 = paramRaster.getMinX();
/* 561 */         i9 = 0;
/* 562 */         for (i12 = 0; i12 < k; i6++) {
/* 563 */           for (i13 = 0; i13 < n; i13++) {
/* 564 */             i8 = paramRaster.getSample(i6, i4, i13);
/* 565 */             localObject1[(i9++)] = ((short)(int)(i8 * arrayOfFloat1[i13] + 0.5F));
/*     */           }
/* 562 */           i12++;
/*     */         }
/*     */ 
/* 571 */         doTransform(localLCMSImageLayout1, localLCMSImageLayout2);
/*     */ 
/* 574 */         i7 = paramWritableRaster.getMinX();
/* 575 */         i9 = 0;
/* 576 */         for (i12 = 0; i12 < k; i7++) {
/* 577 */           for (i13 = 0; i13 < i1; i13++) {
/* 578 */             i8 = (int)((localObject2[(i9++)] & 0xFFFF) * arrayOfFloat2[i13] + 0.5F);
/*     */ 
/* 580 */             paramWritableRaster.setSample(i7, i5, i13, i8);
/*     */           }
/* 576 */           i12++;
/*     */         }
/* 558 */         i11++; i4++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public short[] colorConvert(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
/*     */   {
/* 593 */     if (paramArrayOfShort2 == null) {
/* 594 */       paramArrayOfShort2 = new short[paramArrayOfShort1.length / getNumInComponents() * getNumOutComponents()];
/*     */     }
/*     */     try
/*     */     {
/* 598 */       LCMSImageLayout localLCMSImageLayout1 = new LCMSImageLayout(paramArrayOfShort1, paramArrayOfShort1.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(2), getNumInComponents() * 2);
/*     */ 
/* 603 */       LCMSImageLayout localLCMSImageLayout2 = new LCMSImageLayout(paramArrayOfShort2, paramArrayOfShort2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(2), getNumOutComponents() * 2);
/*     */ 
/* 608 */       doTransform(localLCMSImageLayout1, localLCMSImageLayout2);
/*     */ 
/* 610 */       return paramArrayOfShort2; } catch (LCMSImageLayout.ImageLayoutException localImageLayoutException) {
/*     */     }
/* 612 */     throw new CMMException("Unable to convert data");
/*     */   }
/*     */ 
/*     */   public byte[] colorConvert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 617 */     if (paramArrayOfByte2 == null) {
/* 618 */       paramArrayOfByte2 = new byte[paramArrayOfByte1.length / getNumInComponents() * getNumOutComponents()];
/*     */     }
/*     */     try
/*     */     {
/* 622 */       LCMSImageLayout localLCMSImageLayout1 = new LCMSImageLayout(paramArrayOfByte1, paramArrayOfByte1.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(1), getNumInComponents());
/*     */ 
/* 627 */       LCMSImageLayout localLCMSImageLayout2 = new LCMSImageLayout(paramArrayOfByte2, paramArrayOfByte2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(1), getNumOutComponents());
/*     */ 
/* 632 */       doTransform(localLCMSImageLayout1, localLCMSImageLayout2);
/*     */ 
/* 634 */       return paramArrayOfByte2; } catch (LCMSImageLayout.ImageLayoutException localImageLayoutException) {
/*     */     }
/* 636 */     throw new CMMException("Unable to convert data");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  74 */     if (ProfileDeferralMgr.deferring)
/*  75 */       ProfileDeferralMgr.activateProfiles();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.lcms.LCMSTransform
 * JD-Core Version:    0.6.2
 */