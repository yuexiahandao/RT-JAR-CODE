/*     */ package sun.java2d.cmm.kcms;
/*     */ 
/*     */ import java.awt.color.CMMException;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ComponentColorModel;
/*     */ import java.awt.image.ComponentSampleModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.java2d.cmm.ColorTransform;
/*     */ import sun.java2d.cmm.ProfileDeferralMgr;
/*     */ 
/*     */ public class ICC_Transform
/*     */   implements ColorTransform
/*     */ {
/*     */   long ID;
/*     */ 
/*     */   long getID()
/*     */   {
/*  74 */     return this.ID;
/*     */   }
/*     */ 
/*     */   public void finalize()
/*     */   {
/*  83 */     CMM.checkStatus(CMM.cmmFreeTransform(this.ID));
/*     */   }
/*     */ 
/*     */   public int getNumInComponents()
/*     */   {
/*  93 */     int[] arrayOfInt = new int[2];
/*     */ 
/*  95 */     CMM.checkStatus(CMM.cmmGetNumComponents(this.ID, arrayOfInt));
/*     */ 
/*  97 */     return arrayOfInt[0];
/*     */   }
/*     */ 
/*     */   public int getNumOutComponents()
/*     */   {
/* 108 */     int[] arrayOfInt = new int[2];
/*     */ 
/* 110 */     CMM.checkStatus(CMM.cmmGetNumComponents(this.ID, arrayOfInt));
/*     */ 
/* 112 */     return arrayOfInt[1];
/*     */   }
/*     */ 
/*     */   public void colorConvert(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2)
/*     */   {
/* 121 */     CMMImageLayout localCMMImageLayout1 = getImageLayout(paramBufferedImage1);
/*     */     CMMImageLayout localCMMImageLayout2;
/* 122 */     if (localCMMImageLayout1 != null) {
/* 123 */       localCMMImageLayout2 = getImageLayout(paramBufferedImage2);
/* 124 */       if (localCMMImageLayout2 != null) {
/* 125 */         synchronized (this) {
/* 126 */           CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */         }
/* 128 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 133 */     ??? = paramBufferedImage1.getRaster();
/* 134 */     WritableRaster localWritableRaster = paramBufferedImage2.getRaster();
/* 135 */     ColorModel localColorModel1 = paramBufferedImage1.getColorModel();
/* 136 */     ColorModel localColorModel2 = paramBufferedImage2.getColorModel();
/* 137 */     Object localObject2 = paramBufferedImage1.getWidth();
/* 138 */     int i = paramBufferedImage1.getHeight();
/* 139 */     Object localObject3 = localColorModel1.getNumColorComponents();
/* 140 */     Object localObject4 = localColorModel2.getNumColorComponents();
/* 141 */     int j = 8;
/* 142 */     float f = 255.0F;
/* 143 */     for (Object localObject5 = 0; localObject5 < localObject3; localObject5++) {
/* 144 */       if (localColorModel1.getComponentSize(localObject5) > 8) {
/* 145 */         j = 16;
/* 146 */         f = 65535.0F;
/*     */       }
/*     */     }
/* 149 */     for (Object localObject6 = 0; localObject6 < localObject4; localObject6++) {
/* 150 */       if (localColorModel2.getComponentSize(localObject6) > 8) {
/* 151 */         j = 16;
/* 152 */         f = 65535.0F;
/*     */       }
/*     */     }
/* 155 */     localObject6 = new float[localObject3];
/* 156 */     float[] arrayOfFloat1 = new float[localObject3];
/* 157 */     ColorSpace localColorSpace = localColorModel1.getColorSpace();
/* 158 */     for (Object localObject7 = 0; localObject7 < localObject3; localObject7++) {
/* 159 */       localObject6[localObject7] = localColorSpace.getMinValue(localObject7);
/* 160 */       arrayOfFloat1[localObject7] = (f / (localColorSpace.getMaxValue(localObject7) - localObject6[localObject7]));
/*     */     }
/* 162 */     localColorSpace = localColorModel2.getColorSpace();
/* 163 */     localObject7 = new float[localObject4];
/* 164 */     float[] arrayOfFloat2 = new float[localObject4];
/* 165 */     for (Object localObject8 = 0; localObject8 < localObject4; localObject8++) {
/* 166 */       localObject7[localObject8] = localColorSpace.getMinValue(localObject8);
/* 167 */       arrayOfFloat2[localObject8] = ((localColorSpace.getMaxValue(localObject8) - localObject7[localObject8]) / f);
/*     */     }
/* 169 */     boolean bool = localColorModel2.hasAlpha();
/* 170 */     int k = (localColorModel1.hasAlpha()) && (bool) ? 1 : 0;
/*     */     float[] arrayOfFloat3;
/* 172 */     if (bool)
/* 173 */       arrayOfFloat3 = new float[localObject4 + 1];
/*     */     else
/* 175 */       arrayOfFloat3 = new float[localObject4];
/*     */     Object localObject9;
/*     */     Object localObject10;
/*     */     float[] arrayOfFloat5;
/*     */     pelArrayInfo localpelArrayInfo;
/*     */     Object localObject11;
/*     */     float[] arrayOfFloat4;
/*     */     int m;
/* 177 */     if (j == 8) {
/* 178 */       localObject9 = new byte[localObject2 * localObject3];
/* 179 */       localObject10 = new byte[localObject2 * localObject4];
/*     */ 
/* 182 */       arrayOfFloat5 = null;
/* 183 */       if (k != 0) {
/* 184 */         arrayOfFloat5 = new float[localObject2];
/*     */       }
/*     */ 
/* 187 */       localpelArrayInfo = new pelArrayInfo(this, (byte[])localObject9, (byte[])localObject10);
/*     */       try
/*     */       {
/* 190 */         localCMMImageLayout1 = new CMMImageLayout((byte[])localObject9, localpelArrayInfo.nPels, localpelArrayInfo.nSrc);
/*     */ 
/* 192 */         localCMMImageLayout2 = new CMMImageLayout((byte[])localObject10, localpelArrayInfo.nPels, localpelArrayInfo.nDest);
/*     */       }
/*     */       catch (CMMImageLayout.ImageLayoutException localImageLayoutException1) {
/* 195 */         throw new CMMException("Unable to convert images");
/*     */       }
/*     */ 
/* 198 */       for (int n = 0; n < i; n++)
/*     */       {
/* 200 */         localObject11 = null;
/* 201 */         arrayOfFloat4 = null;
/* 202 */         m = 0;
/* 203 */         for (Object localObject12 = 0; localObject12 < localObject2; localObject12++) {
/* 204 */           localObject11 = ((Raster)???).getDataElements(localObject12, n, localObject11);
/* 205 */           arrayOfFloat4 = localColorModel1.getNormalizedComponents(localObject11, arrayOfFloat4, 0);
/* 206 */           for (Object localObject16 = 0; localObject16 < localObject3; localObject16++) {
/* 207 */             localObject9[(m++)] = ((byte)(int)((arrayOfFloat4[localObject16] - localObject6[localObject16]) * arrayOfFloat1[localObject16] + 0.5F));
/*     */           }
/*     */ 
/* 211 */           if (k != 0) {
/* 212 */             arrayOfFloat5[localObject12] = arrayOfFloat4[localObject3];
/*     */           }
/*     */         }
/*     */ 
/* 216 */         synchronized (this) {
/* 217 */           CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */         }
/*     */ 
/* 220 */         localObject11 = null;
/* 221 */         m = 0;
/* 222 */         for (Object localObject13 = 0; localObject13 < localObject2; localObject13++) {
/* 223 */           for (Object localObject17 = 0; localObject17 < localObject4; localObject17++) {
/* 224 */             arrayOfFloat3[localObject17] = ((localObject10[(m++)] & 0xFF) * arrayOfFloat2[localObject17] + localObject7[localObject17]);
/*     */           }
/*     */ 
/* 227 */           if (k != 0)
/* 228 */             arrayOfFloat3[localObject4] = arrayOfFloat5[localObject13];
/* 229 */           else if (bool) {
/* 230 */             arrayOfFloat3[localObject4] = 1.0F;
/*     */           }
/* 232 */           localObject11 = localColorModel2.getDataElements(arrayOfFloat3, 0, localObject11);
/* 233 */           localWritableRaster.setDataElements(localObject13, n, localObject11);
/*     */         }
/*     */       }
/*     */     } else {
/* 237 */       localObject9 = new short[localObject2 * localObject3];
/* 238 */       localObject10 = new short[localObject2 * localObject4];
/*     */ 
/* 241 */       arrayOfFloat5 = null;
/* 242 */       if (k != 0) {
/* 243 */         arrayOfFloat5 = new float[localObject2];
/*     */       }
/*     */ 
/* 246 */       localpelArrayInfo = new pelArrayInfo(this, (short[])localObject9, (short[])localObject10);
/*     */       try
/*     */       {
/* 249 */         localCMMImageLayout1 = new CMMImageLayout((short[])localObject9, localpelArrayInfo.nPels, localpelArrayInfo.nSrc);
/*     */ 
/* 251 */         localCMMImageLayout2 = new CMMImageLayout((short[])localObject10, localpelArrayInfo.nPels, localpelArrayInfo.nDest);
/*     */       }
/*     */       catch (CMMImageLayout.ImageLayoutException localImageLayoutException2) {
/* 254 */         throw new CMMException("Unable to convert images");
/*     */       }
/*     */ 
/* 257 */       for (int i1 = 0; i1 < i; i1++)
/*     */       {
/* 259 */         localObject11 = null;
/* 260 */         arrayOfFloat4 = null;
/* 261 */         m = 0;
/* 262 */         for (??? = 0; ??? < localObject2; ???++) {
/* 263 */           localObject11 = ((Raster)???).getDataElements(???, i1, localObject11);
/* 264 */           arrayOfFloat4 = localColorModel1.getNormalizedComponents(localObject11, arrayOfFloat4, 0);
/* 265 */           for (Object localObject18 = 0; localObject18 < localObject3; localObject18++) {
/* 266 */             localObject9[(m++)] = ((short)(int)((arrayOfFloat4[localObject18] - localObject6[localObject18]) * arrayOfFloat1[localObject18] + 0.5F));
/*     */           }
/*     */ 
/* 270 */           if (k != 0) {
/* 271 */             arrayOfFloat5[???] = arrayOfFloat4[localObject3];
/*     */           }
/*     */         }
/*     */ 
/* 275 */         synchronized (this) {
/* 276 */           CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */         }
/*     */ 
/* 279 */         localObject11 = null;
/* 280 */         m = 0;
/* 281 */         for (Object localObject15 = 0; localObject15 < localObject2; localObject15++) {
/* 282 */           for (Object localObject19 = 0; localObject19 < localObject4; localObject19++) {
/* 283 */             arrayOfFloat3[localObject19] = ((localObject10[(m++)] & 0xFFFF) * arrayOfFloat2[localObject19] + localObject7[localObject19]);
/*     */           }
/*     */ 
/* 286 */           if (k != 0)
/* 287 */             arrayOfFloat3[localObject4] = arrayOfFloat5[localObject15];
/* 288 */           else if (bool) {
/* 289 */             arrayOfFloat3[localObject4] = 1.0F;
/*     */           }
/* 291 */           localObject11 = localColorModel2.getDataElements(arrayOfFloat3, 0, localObject11);
/* 292 */           localWritableRaster.setDataElements(localObject15, i1, localObject11);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private CMMImageLayout getImageLayout(BufferedImage paramBufferedImage)
/*     */   {
/*     */     try
/*     */     {
/* 309 */       switch (paramBufferedImage.getType()) {
/*     */       case 1:
/*     */       case 2:
/*     */       case 4:
/* 313 */         return new CMMImageLayout(paramBufferedImage);
/*     */       case 5:
/*     */       case 6:
/* 316 */         localObject = (ComponentColorModel)paramBufferedImage.getColorModel();
/*     */ 
/* 318 */         if ((localObject.getClass() == ComponentColorModel.class) || (checkMinMaxScaling((ComponentColorModel)localObject)))
/*     */         {
/* 320 */           return new CMMImageLayout(paramBufferedImage);
/*     */         }
/* 322 */         return null;
/*     */       case 10:
/* 325 */         localObject = (ComponentColorModel)paramBufferedImage.getColorModel();
/*     */ 
/* 327 */         if (((ComponentColorModel)localObject).getComponentSize(0) != 8) {
/* 328 */           return null;
/*     */         }
/* 330 */         if ((localObject.getClass() == ComponentColorModel.class) || (checkMinMaxScaling((ComponentColorModel)localObject)))
/*     */         {
/* 332 */           return new CMMImageLayout(paramBufferedImage);
/*     */         }
/* 334 */         return null;
/*     */       case 11:
/* 337 */         localObject = (ComponentColorModel)paramBufferedImage.getColorModel();
/*     */ 
/* 339 */         if (((ComponentColorModel)localObject).getComponentSize(0) != 16) {
/* 340 */           return null;
/*     */         }
/* 342 */         if ((localObject.getClass() == ComponentColorModel.class) || (checkMinMaxScaling((ComponentColorModel)localObject)))
/*     */         {
/* 344 */           return new CMMImageLayout(paramBufferedImage);
/*     */         }
/* 346 */         return null;
/*     */       case 3:
/*     */       case 7:
/*     */       case 8:
/* 349 */       case 9: } Object localObject = paramBufferedImage.getColorModel();
/*     */       SampleModel localSampleModel;
/*     */       int j;
/*     */       int k;
/* 350 */       if ((localObject instanceof DirectColorModel))
/*     */       {
/* 353 */         localSampleModel = paramBufferedImage.getSampleModel();
/* 354 */         if (!(localSampleModel instanceof SinglePixelPackedSampleModel)) {
/* 355 */           return null;
/*     */         }
/* 357 */         if (((ColorModel)localObject).getTransferType() != 3) {
/* 358 */           return null;
/*     */         }
/* 360 */         if ((((ColorModel)localObject).hasAlpha()) && (((ColorModel)localObject).isAlphaPremultiplied())) {
/* 361 */           return null;
/*     */         }
/* 363 */         DirectColorModel localDirectColorModel = (DirectColorModel)localObject;
/*     */ 
/* 365 */         j = localDirectColorModel.getRedMask();
/* 366 */         k = localDirectColorModel.getGreenMask();
/* 367 */         int m = localDirectColorModel.getBlueMask();
/* 368 */         int n = localDirectColorModel.getAlphaMask();
/*     */         int i4;
/*     */         int i3;
/*     */         int i2;
/* 370 */         int i1 = i2 = i3 = i4 = -1;
/* 371 */         int i5 = 0;
/* 372 */         int i6 = 3;
/* 373 */         if (n != 0) {
/* 374 */           i6 = 4;
/*     */         }
/* 376 */         int i7 = 0; for (int i8 = -16777216; i7 < 4; i8 >>>= 8) {
/* 377 */           if (j == i8) {
/* 378 */             i1 = i7;
/* 379 */             i5++;
/* 380 */           } else if (k == i8) {
/* 381 */             i2 = i7;
/* 382 */             i5++;
/* 383 */           } else if (m == i8) {
/* 384 */             i3 = i7;
/* 385 */             i5++;
/* 386 */           } else if (n == i8) {
/* 387 */             i4 = i7;
/* 388 */             i5++;
/*     */           }
/* 376 */           i7++;
/*     */         }
/*     */ 
/* 391 */         if (i5 != i6) {
/* 392 */           return null;
/*     */         }
/* 394 */         return new CMMImageLayout(paramBufferedImage, (SinglePixelPackedSampleModel)localSampleModel, i1, i2, i3, i4);
/*     */       }
/* 396 */       if ((localObject instanceof ComponentColorModel)) {
/* 397 */         localSampleModel = paramBufferedImage.getSampleModel();
/* 398 */         if (!(localSampleModel instanceof ComponentSampleModel)) {
/* 399 */           return null;
/*     */         }
/* 401 */         if ((((ColorModel)localObject).hasAlpha()) && (((ColorModel)localObject).isAlphaPremultiplied())) {
/* 402 */           return null;
/*     */         }
/* 404 */         int i = ((ColorModel)localObject).getNumComponents();
/* 405 */         if (localSampleModel.getNumBands() != i) {
/* 406 */           return null;
/*     */         }
/* 408 */         j = ((ColorModel)localObject).getTransferType();
/* 409 */         if (j == 0) {
/* 410 */           for (k = 0; k < i; k++) {
/* 411 */             if (((ColorModel)localObject).getComponentSize(k) != 8)
/* 412 */               return null;
/*     */           }
/*     */         }
/* 415 */         else if (j == 1) {
/* 416 */           for (k = 0; k < i; k++) {
/* 417 */             if (((ColorModel)localObject).getComponentSize(k) != 16)
/* 418 */               return null;
/*     */           }
/*     */         }
/*     */         else {
/* 422 */           return null;
/*     */         }
/* 424 */         ComponentColorModel localComponentColorModel = (ComponentColorModel)localObject;
/* 425 */         if ((localComponentColorModel.getClass() == ComponentColorModel.class) || (checkMinMaxScaling(localComponentColorModel)))
/*     */         {
/* 427 */           return new CMMImageLayout(paramBufferedImage, (ComponentSampleModel)localSampleModel);
/*     */         }
/* 429 */         return null;
/*     */       }
/*     */ 
/* 436 */       return null;
/*     */     }
/*     */     catch (CMMImageLayout.ImageLayoutException localImageLayoutException)
/*     */     {
/*     */     }
/*     */ 
/* 449 */     throw new CMMException("Unable to convert image");
/*     */   }
/*     */ 
/*     */   private boolean checkMinMaxScaling(ComponentColorModel paramComponentColorModel)
/*     */   {
/* 457 */     int i = paramComponentColorModel.getNumComponents();
/* 458 */     int j = paramComponentColorModel.getNumColorComponents();
/* 459 */     int[] arrayOfInt = paramComponentColorModel.getComponentSize();
/* 460 */     boolean bool = paramComponentColorModel.hasAlpha();
/*     */     float[] arrayOfFloat1;
/*     */     float[] arrayOfFloat2;
/*     */     float f1;
/* 462 */     switch (paramComponentColorModel.getTransferType())
/*     */     {
/*     */     case 0:
/* 465 */       localObject = new byte[i];
/* 466 */       for (k = 0; k < j; k++) {
/* 467 */         localObject[k] = 0;
/*     */       }
/* 469 */       if (bool) {
/* 470 */         localObject[j] = ((byte)((1 << arrayOfInt[j]) - 1));
/*     */       }
/*     */ 
/* 473 */       arrayOfFloat1 = paramComponentColorModel.getNormalizedComponents(localObject, null, 0);
/* 474 */       for (k = 0; k < j; k++) {
/* 475 */         localObject[k] = ((byte)((1 << arrayOfInt[k]) - 1));
/*     */       }
/* 477 */       arrayOfFloat2 = paramComponentColorModel.getNormalizedComponents(localObject, null, 0);
/* 478 */       f1 = 256.0F;
/*     */ 
/* 480 */       break;
/*     */     case 1:
/* 483 */       localObject = new short[i];
/* 484 */       for (k = 0; k < j; k++) {
/* 485 */         localObject[k] = 0;
/*     */       }
/* 487 */       if (bool) {
/* 488 */         localObject[j] = ((short)(byte)((1 << arrayOfInt[j]) - 1));
/*     */       }
/*     */ 
/* 491 */       arrayOfFloat1 = paramComponentColorModel.getNormalizedComponents(localObject, null, 0);
/* 492 */       for (k = 0; k < j; k++) {
/* 493 */         localObject[k] = ((short)(byte)((1 << arrayOfInt[k]) - 1));
/*     */       }
/* 495 */       arrayOfFloat2 = paramComponentColorModel.getNormalizedComponents(localObject, null, 0);
/* 496 */       f1 = 65536.0F;
/*     */ 
/* 498 */       break;
/*     */     default:
/* 500 */       return false;
/*     */     }
/* 502 */     Object localObject = paramComponentColorModel.getColorSpace();
/* 503 */     for (int k = 0; k < j; k++) {
/* 504 */       float f2 = ((ColorSpace)localObject).getMinValue(k);
/* 505 */       float f3 = ((ColorSpace)localObject).getMaxValue(k);
/* 506 */       float f4 = (f3 - f2) / f1;
/* 507 */       f2 -= arrayOfFloat1[k];
/* 508 */       if (f2 < 0.0F) {
/* 509 */         f2 = -f2;
/*     */       }
/* 511 */       f3 -= arrayOfFloat2[k];
/* 512 */       if (f3 < 0.0F) {
/* 513 */         f3 = -f3;
/*     */       }
/* 515 */       if ((f2 > f4) || (f3 > f4)) {
/* 516 */         return false;
/*     */       }
/*     */     }
/* 519 */     return true;
/*     */   }
/*     */ 
/*     */   public void colorConvert(Raster paramRaster, WritableRaster paramWritableRaster, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
/*     */   {
/* 531 */     SampleModel localSampleModel1 = paramRaster.getSampleModel();
/* 532 */     SampleModel localSampleModel2 = paramWritableRaster.getSampleModel();
/* 533 */     int i = paramRaster.getTransferType();
/* 534 */     int j = paramWritableRaster.getTransferType();
/*     */     int k;
/* 536 */     if ((i == 4) || (i == 5))
/*     */     {
/* 538 */       k = 1;
/*     */     }
/* 540 */     else k = 0;
/*     */     int m;
/* 542 */     if ((j == 4) || (j == 5))
/*     */     {
/* 544 */       m = 1;
/*     */     }
/* 546 */     else m = 0;
/*     */ 
/* 548 */     Object localObject1 = paramRaster.getWidth();
/* 549 */     int n = paramRaster.getHeight();
/* 550 */     Object localObject2 = paramRaster.getNumBands();
/* 551 */     Object localObject3 = paramWritableRaster.getNumBands();
/* 552 */     float[] arrayOfFloat1 = new float[localObject2];
/* 553 */     float[] arrayOfFloat2 = new float[localObject3];
/* 554 */     float[] arrayOfFloat3 = new float[localObject2];
/* 555 */     float[] arrayOfFloat4 = new float[localObject3];
/* 556 */     for (Object localObject4 = 0; localObject4 < localObject2; localObject4++) {
/* 557 */       if (k != 0) {
/* 558 */         arrayOfFloat1[localObject4] = (65535.0F / (paramArrayOfFloat2[localObject4] - paramArrayOfFloat1[localObject4]));
/* 559 */         arrayOfFloat3[localObject4] = paramArrayOfFloat1[localObject4];
/*     */       } else {
/* 561 */         if (i == 2)
/* 562 */           arrayOfFloat1[localObject4] = 2.000031F;
/*     */         else {
/* 564 */           arrayOfFloat1[localObject4] = (65535.0F / ((1 << localSampleModel1.getSampleSize(localObject4)) - 1));
/*     */         }
/*     */ 
/* 567 */         arrayOfFloat3[localObject4] = 0.0F;
/*     */       }
/*     */     }
/* 570 */     for (Object localObject5 = 0; localObject5 < localObject3; localObject5++) {
/* 571 */       if (m != 0) {
/* 572 */         arrayOfFloat2[localObject5] = ((paramArrayOfFloat4[localObject5] - paramArrayOfFloat3[localObject5]) / 65535.0F);
/* 573 */         arrayOfFloat4[localObject5] = paramArrayOfFloat3[localObject5];
/*     */       } else {
/* 575 */         if (j == 2)
/* 576 */           arrayOfFloat2[localObject5] = 0.4999924F;
/*     */         else {
/* 578 */           arrayOfFloat2[localObject5] = (((1 << localSampleModel2.getSampleSize(localObject5)) - 1) / 65535.0F);
/*     */         }
/*     */ 
/* 582 */         arrayOfFloat4[localObject5] = 0.0F; }  } int i1 = paramRaster.getMinY();
/* 586 */     int i2 = paramWritableRaster.getMinY();
/*     */ 
/* 589 */     short[] arrayOfShort1 = new short[localObject1 * localObject2];
/* 590 */     short[] arrayOfShort2 = new short[localObject1 * localObject3];
/*     */ 
/* 592 */     pelArrayInfo localpelArrayInfo = new pelArrayInfo(this, arrayOfShort1, arrayOfShort2);
/*     */     CMMImageLayout localCMMImageLayout1;
/*     */     CMMImageLayout localCMMImageLayout2;
/*     */     try { localCMMImageLayout1 = new CMMImageLayout(arrayOfShort1, localpelArrayInfo.nPels, localpelArrayInfo.nSrc);
/*     */ 
/* 597 */       localCMMImageLayout2 = new CMMImageLayout(arrayOfShort2, localpelArrayInfo.nPels, localpelArrayInfo.nDest);
/*     */     } catch (CMMImageLayout.ImageLayoutException localImageLayoutException)
/*     */     {
/* 600 */       throw new CMMException("Unable to convert rasters");
/*     */     }
/*     */ 
/* 603 */     for (int i6 = 0; i6 < n; i2++)
/*     */     {
/* 605 */       int i3 = paramRaster.getMinX();
/* 606 */       int i5 = 0;
/*     */       float f;
/* 607 */       for (Object localObject6 = 0; localObject6 < localObject1; i3++) {
/* 608 */         for (Object localObject8 = 0; localObject8 < localObject2; localObject8++) {
/* 609 */           f = paramRaster.getSampleFloat(i3, i1, localObject8);
/* 610 */           arrayOfShort1[(i5++)] = ((short)(int)((f - arrayOfFloat3[localObject8]) * arrayOfFloat1[localObject8] + 0.5F));
/*     */         }
/* 607 */         localObject6++;
/*     */       }
/*     */ 
/* 616 */       synchronized (this) {
/* 617 */         CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */       }
/*     */ 
/* 621 */       int i4 = paramWritableRaster.getMinX();
/* 622 */       i5 = 0;
/* 623 */       for (Object localObject7 = 0; localObject7 < localObject1; i4++) {
/* 624 */         for (Object localObject9 = 0; localObject9 < localObject3; localObject9++) {
/* 625 */           f = (arrayOfShort2[(i5++)] & 0xFFFF) * arrayOfFloat2[localObject9] + arrayOfFloat4[localObject9];
/*     */ 
/* 627 */           paramWritableRaster.setSample(i4, i2, localObject9, f);
/*     */         }
/* 623 */         localObject7++;
/*     */       }
/* 603 */       i6++; i1++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void colorConvert(Raster paramRaster, WritableRaster paramWritableRaster)
/*     */   {
/* 639 */     CMMImageLayout localCMMImageLayout1 = getImageLayout(paramRaster);
/*     */     CMMImageLayout localCMMImageLayout2;
/* 640 */     if (localCMMImageLayout1 != null) {
/* 641 */       localCMMImageLayout2 = getImageLayout(paramWritableRaster);
/* 642 */       if (localCMMImageLayout2 != null) {
/* 643 */         synchronized (this) {
/* 644 */           CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */         }
/* 646 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 651 */     ??? = paramRaster.getSampleModel();
/* 652 */     SampleModel localSampleModel = paramWritableRaster.getSampleModel();
/* 653 */     int i = paramRaster.getTransferType();
/* 654 */     int j = paramWritableRaster.getTransferType();
/* 655 */     Object localObject2 = paramRaster.getWidth();
/* 656 */     int k = paramRaster.getHeight();
/* 657 */     Object localObject3 = paramRaster.getNumBands();
/* 658 */     Object localObject4 = paramWritableRaster.getNumBands();
/* 659 */     int m = 8;
/* 660 */     float f = 255.0F;
/* 661 */     for (Object localObject5 = 0; localObject5 < localObject3; localObject5++) {
/* 662 */       if (((SampleModel)???).getSampleSize(localObject5) > 8) {
/* 663 */         m = 16;
/* 664 */         f = 65535.0F;
/*     */       }
/*     */     }
/* 667 */     for (Object localObject6 = 0; localObject6 < localObject4; localObject6++) {
/* 668 */       if (localSampleModel.getSampleSize(localObject6) > 8) {
/* 669 */         m = 16;
/* 670 */         f = 65535.0F;
/*     */       }
/*     */     }
/* 673 */     localObject6 = new float[localObject3];
/* 674 */     float[] arrayOfFloat = new float[localObject4];
/* 675 */     for (Object localObject7 = 0; localObject7 < localObject3; localObject7++) {
/* 676 */       if (i == 2)
/* 677 */         localObject6[localObject7] = (f / 32767.0F);
/*     */       else {
/* 679 */         localObject6[localObject7] = (f / ((1 << ((SampleModel)???).getSampleSize(localObject7)) - 1));
/*     */       }
/*     */     }
/*     */ 
/* 683 */     for (Object localObject8 = 0; localObject8 < localObject4; localObject8++) {
/* 684 */       if (j == 2)
/* 685 */         arrayOfFloat[localObject8] = (32767.0F / f);
/*     */       else {
/* 687 */         arrayOfFloat[localObject8] = (((1 << localSampleModel.getSampleSize(localObject8)) - 1) / f);
/*     */       }
/*     */     }
/*     */ 
/* 691 */     int n = paramRaster.getMinY();
/* 692 */     int i1 = paramWritableRaster.getMinY();
/*     */     Object localObject9;
/*     */     Object localObject10;
/*     */     pelArrayInfo localpelArrayInfo;
/*     */     int i2;
/*     */     int i5;
/*     */     int i4;
/*     */     int i3;
/* 695 */     if (m == 8) {
/* 696 */       localObject9 = new byte[localObject2 * localObject3];
/* 697 */       localObject10 = new byte[localObject2 * localObject4];
/*     */ 
/* 699 */       localpelArrayInfo = new pelArrayInfo(this, (byte[])localObject9, (byte[])localObject10);
/*     */       try
/*     */       {
/* 702 */         localCMMImageLayout1 = new CMMImageLayout((byte[])localObject9, localpelArrayInfo.nPels, localpelArrayInfo.nSrc);
/*     */ 
/* 704 */         localCMMImageLayout2 = new CMMImageLayout((byte[])localObject10, localpelArrayInfo.nPels, localpelArrayInfo.nDest);
/*     */       }
/*     */       catch (CMMImageLayout.ImageLayoutException localImageLayoutException1) {
/* 707 */         throw new CMMException("Unable to convert rasters");
/*     */       }
/*     */ 
/* 710 */       for (int i6 = 0; i6 < k; i1++)
/*     */       {
/* 712 */         i2 = paramRaster.getMinX();
/* 713 */         i5 = 0;
/* 714 */         for (Object localObject11 = 0; localObject11 < localObject2; i2++) {
/* 715 */           for (Object localObject15 = 0; localObject15 < localObject3; localObject15++) {
/* 716 */             i4 = paramRaster.getSample(i2, n, localObject15);
/* 717 */             localObject9[(i5++)] = ((byte)(int)(i4 * localObject6[localObject15] + 0.5F));
/*     */           }
/* 714 */           localObject11++;
/*     */         }
/*     */ 
/* 723 */         synchronized (this) {
/* 724 */           CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */         }
/*     */ 
/* 728 */         i3 = paramWritableRaster.getMinX();
/* 729 */         i5 = 0;
/* 730 */         for (Object localObject12 = 0; localObject12 < localObject2; i3++) {
/* 731 */           for (Object localObject16 = 0; localObject16 < localObject4; localObject16++) {
/* 732 */             i4 = (int)((localObject10[(i5++)] & 0xFF) * arrayOfFloat[localObject16] + 0.5F);
/*     */ 
/* 734 */             paramWritableRaster.setSample(i3, i1, localObject16, i4);
/*     */           }
/* 730 */           localObject12++;
/*     */         }
/* 710 */         i6++; n++;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 739 */       localObject9 = new short[localObject2 * localObject3];
/* 740 */       localObject10 = new short[localObject2 * localObject4];
/*     */ 
/* 742 */       localpelArrayInfo = new pelArrayInfo(this, (short[])localObject9, (short[])localObject10);
/*     */       try
/*     */       {
/* 745 */         localCMMImageLayout1 = new CMMImageLayout((short[])localObject9, localpelArrayInfo.nPels, localpelArrayInfo.nSrc);
/*     */ 
/* 747 */         localCMMImageLayout2 = new CMMImageLayout((short[])localObject10, localpelArrayInfo.nPels, localpelArrayInfo.nDest);
/*     */       }
/*     */       catch (CMMImageLayout.ImageLayoutException localImageLayoutException2) {
/* 750 */         throw new CMMException("Unable to convert rasters");
/*     */       }
/*     */ 
/* 753 */       for (int i7 = 0; i7 < k; i1++)
/*     */       {
/* 755 */         i2 = paramRaster.getMinX();
/* 756 */         i5 = 0;
/* 757 */         for (??? = 0; ??? < localObject2; i2++) {
/* 758 */           for (Object localObject17 = 0; localObject17 < localObject3; localObject17++) {
/* 759 */             i4 = paramRaster.getSample(i2, n, localObject17);
/* 760 */             localObject9[(i5++)] = ((short)(int)(i4 * localObject6[localObject17] + 0.5F));
/*     */           }
/* 757 */           ???++;
/*     */         }
/*     */ 
/* 766 */         synchronized (this) {
/* 767 */           CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */         }
/*     */ 
/* 771 */         i3 = paramWritableRaster.getMinX();
/* 772 */         i5 = 0;
/* 773 */         for (Object localObject14 = 0; localObject14 < localObject2; i3++) {
/* 774 */           for (Object localObject18 = 0; localObject18 < localObject4; localObject18++) {
/* 775 */             i4 = (int)((localObject10[(i5++)] & 0xFFFF) * arrayOfFloat[localObject18] + 0.5F);
/*     */ 
/* 777 */             paramWritableRaster.setSample(i3, i1, localObject18, i4);
/*     */           }
/* 773 */           localObject14++;
/*     */         }
/* 753 */         i7++; n++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private CMMImageLayout getImageLayout(Raster paramRaster)
/*     */   {
/* 795 */     SampleModel localSampleModel = paramRaster.getSampleModel();
/* 796 */     if ((localSampleModel instanceof ComponentSampleModel)) {
/* 797 */       int i = paramRaster.getNumBands();
/* 798 */       int j = localSampleModel.getTransferType();
/*     */       int k;
/* 799 */       if (j == 0) {
/* 800 */         for (k = 0; k < i; k++)
/*     */         {
/* 802 */           if (localSampleModel.getSampleSize(k) != 8)
/* 803 */             return null;
/*     */         }
/*     */       }
/* 806 */       else if (j == 1) {
/* 807 */         for (k = 0; k < i; k++)
/*     */         {
/* 809 */           if (localSampleModel.getSampleSize(k) != 16)
/* 810 */             return null;
/*     */         }
/*     */       }
/*     */       else
/* 814 */         return null;
/*     */       try
/*     */       {
/* 817 */         return new CMMImageLayout(paramRaster, (ComponentSampleModel)localSampleModel);
/*     */       } catch (CMMImageLayout.ImageLayoutException localImageLayoutException) {
/* 819 */         throw new CMMException("Unable to convert raster");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 825 */     return null;
/*     */   }
/*     */ 
/*     */   public short[] colorConvert(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
/*     */   {
/* 841 */     pelArrayInfo localpelArrayInfo = new pelArrayInfo(this, paramArrayOfShort1, paramArrayOfShort2);
/*     */     short[] arrayOfShort;
/* 843 */     if (paramArrayOfShort2 != null) {
/* 844 */       arrayOfShort = paramArrayOfShort2;
/*     */     }
/*     */     else
/* 847 */       arrayOfShort = new short[localpelArrayInfo.destSize];
/*     */     CMMImageLayout localCMMImageLayout1;
/*     */     CMMImageLayout localCMMImageLayout2;
/*     */     try {
/* 852 */       localCMMImageLayout1 = new CMMImageLayout(paramArrayOfShort1, localpelArrayInfo.nPels, localpelArrayInfo.nSrc);
/*     */ 
/* 856 */       localCMMImageLayout2 = new CMMImageLayout(arrayOfShort, localpelArrayInfo.nPels, localpelArrayInfo.nDest);
/*     */     }
/*     */     catch (CMMImageLayout.ImageLayoutException localImageLayoutException) {
/* 859 */       throw new CMMException("Unable to convert data");
/*     */     }
/*     */ 
/* 862 */     synchronized (this) {
/* 863 */       CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */     }
/*     */ 
/* 866 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   public byte[] colorConvert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 882 */     pelArrayInfo localpelArrayInfo = new pelArrayInfo(this, paramArrayOfByte1, paramArrayOfByte2);
/*     */     byte[] arrayOfByte;
/* 884 */     if (paramArrayOfByte2 != null) {
/* 885 */       arrayOfByte = paramArrayOfByte2;
/*     */     }
/*     */     else
/* 888 */       arrayOfByte = new byte[localpelArrayInfo.destSize];
/*     */     CMMImageLayout localCMMImageLayout1;
/*     */     CMMImageLayout localCMMImageLayout2;
/*     */     try {
/* 893 */       localCMMImageLayout1 = new CMMImageLayout(paramArrayOfByte1, localpelArrayInfo.nPels, localpelArrayInfo.nSrc);
/*     */ 
/* 897 */       localCMMImageLayout2 = new CMMImageLayout(arrayOfByte, localpelArrayInfo.nPels, localpelArrayInfo.nDest);
/*     */     }
/*     */     catch (CMMImageLayout.ImageLayoutException localImageLayoutException) {
/* 900 */       throw new CMMException("Unable to convert data");
/*     */     }
/* 902 */     synchronized (this) {
/* 903 */       CMM.checkStatus(CMM.cmmColorConvert(this.ID, localCMMImageLayout1, localCMMImageLayout2));
/*     */     }
/*     */ 
/* 906 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  56 */     if (ProfileDeferralMgr.deferring)
/*  57 */       ProfileDeferralMgr.activateProfiles();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.kcms.ICC_Transform
 * JD-Core Version:    0.6.2
 */