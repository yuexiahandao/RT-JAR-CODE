/*      */ package com.sun.imageio.plugins.common;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ComponentColorModel;
/*      */ import java.awt.image.ComponentSampleModel;
/*      */ import java.awt.image.DataBuffer;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.DataBufferInt;
/*      */ import java.awt.image.DataBufferShort;
/*      */ import java.awt.image.DataBufferUShort;
/*      */ import java.awt.image.DirectColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.MultiPixelPackedSampleModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.SinglePixelPackedSampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.ImageWriter;
/*      */ import javax.imageio.spi.ImageWriterSpi;
/*      */ 
/*      */ public class ImageUtil
/*      */ {
/*      */   public static final ColorModel createColorModel(SampleModel paramSampleModel)
/*      */   {
/*  141 */     if (paramSampleModel == null) {
/*  142 */       throw new IllegalArgumentException("sampleModel == null!");
/*      */     }
/*      */ 
/*  146 */     int i = paramSampleModel.getDataType();
/*      */ 
/*  149 */     switch (i) {
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*  156 */       break;
/*      */     default:
/*  159 */       return null;
/*      */     }
/*      */ 
/*  163 */     Object localObject1 = null;
/*      */ 
/*  166 */     int[] arrayOfInt = paramSampleModel.getSampleSize();
/*      */     Object localObject2;
/*      */     boolean bool1;
/*      */     boolean bool2;
/*      */     int i2;
/*  169 */     if ((paramSampleModel instanceof ComponentSampleModel))
/*      */     {
/*  171 */       int j = paramSampleModel.getNumBands();
/*      */ 
/*  174 */       localObject2 = null;
/*  175 */       if (j <= 2)
/*  176 */         localObject2 = ColorSpace.getInstance(1003);
/*  177 */       else if (j <= 4)
/*  178 */         localObject2 = ColorSpace.getInstance(1000);
/*      */       else {
/*  180 */         localObject2 = new BogusColorSpace(j);
/*      */       }
/*      */ 
/*  183 */       bool1 = (j == 2) || (j == 4);
/*  184 */       bool2 = false;
/*  185 */       i2 = bool1 ? 3 : 1;
/*      */ 
/*  188 */       localObject1 = new ComponentColorModel((ColorSpace)localObject2, arrayOfInt, bool1, bool2, i2, i);
/*      */     }
/*      */     else
/*      */     {
/*      */       int i1;
/*  194 */       if ((paramSampleModel.getNumBands() <= 4) && ((paramSampleModel instanceof SinglePixelPackedSampleModel)))
/*      */       {
/*  196 */         SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = (SinglePixelPackedSampleModel)paramSampleModel;
/*      */ 
/*  199 */         localObject2 = localSinglePixelPackedSampleModel.getBitMasks();
/*  200 */         bool1 = false;
/*  201 */         bool2 = false;
/*  202 */         i2 = 0;
/*  203 */         int i3 = 0;
/*      */ 
/*  205 */         int i4 = localObject2.length;
/*      */         int n;
/*  206 */         if (i4 <= 2) {
/*  207 */           n = i1 = i2 = localObject2[0];
/*  208 */           if (i4 == 2)
/*  209 */             i3 = localObject2[1];
/*      */         }
/*      */         else {
/*  212 */           n = localObject2[0];
/*  213 */           i1 = localObject2[1];
/*  214 */           i2 = localObject2[2];
/*  215 */           if (i4 == 4) {
/*  216 */             i3 = localObject2[3];
/*      */           }
/*      */         }
/*      */ 
/*  220 */         int i5 = 0;
/*  221 */         for (int i6 = 0; i6 < arrayOfInt.length; i6++) {
/*  222 */           i5 += arrayOfInt[i6];
/*      */         }
/*      */ 
/*  225 */         return new DirectColorModel(i5, n, i1, i2, i3);
/*      */       }
/*  227 */       if ((paramSampleModel instanceof MultiPixelPackedSampleModel))
/*      */       {
/*  229 */         int k = arrayOfInt[0];
/*  230 */         int m = 1 << k;
/*  231 */         byte[] arrayOfByte = new byte[m];
/*  232 */         for (i1 = 0; i1 < m; i1++) {
/*  233 */           arrayOfByte[i1] = ((byte)(i1 * 255 / (m - 1)));
/*      */         }
/*      */ 
/*  236 */         localObject1 = new IndexColorModel(k, m, arrayOfByte, arrayOfByte, arrayOfByte);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  241 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public static byte[] getPackedBinaryData(Raster paramRaster, Rectangle paramRectangle)
/*      */   {
/*  261 */     SampleModel localSampleModel = paramRaster.getSampleModel();
/*  262 */     if (!isBinary(localSampleModel)) {
/*  263 */       throw new IllegalArgumentException(I18N.getString("ImageUtil0"));
/*      */     }
/*      */ 
/*  266 */     int i = paramRectangle.x;
/*  267 */     int j = paramRectangle.y;
/*  268 */     int k = paramRectangle.width;
/*  269 */     int m = paramRectangle.height;
/*      */ 
/*  271 */     DataBuffer localDataBuffer = paramRaster.getDataBuffer();
/*      */ 
/*  273 */     int n = i - paramRaster.getSampleModelTranslateX();
/*  274 */     int i1 = j - paramRaster.getSampleModelTranslateY();
/*      */ 
/*  276 */     MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = (MultiPixelPackedSampleModel)localSampleModel;
/*  277 */     int i2 = localMultiPixelPackedSampleModel.getScanlineStride();
/*  278 */     int i3 = localDataBuffer.getOffset() + localMultiPixelPackedSampleModel.getOffset(n, i1);
/*  279 */     int i4 = localMultiPixelPackedSampleModel.getBitOffset(n);
/*      */ 
/*  281 */     int i5 = (k + 7) / 8;
/*  282 */     if (((localDataBuffer instanceof DataBufferByte)) && (i3 == 0) && (i4 == 0) && (i5 == i2) && (((DataBufferByte)localDataBuffer).getData().length == i5 * m))
/*      */     {
/*  287 */       return ((DataBufferByte)localDataBuffer).getData();
/*      */     }
/*      */ 
/*  290 */     byte[] arrayOfByte = new byte[i5 * m];
/*      */ 
/*  292 */     int i6 = 0;
/*      */     Object localObject;
/*      */     int i7;
/*      */     int i8;
/*      */     int i9;
/*      */     int i10;
/*  294 */     if (i4 == 0) {
/*  295 */       if ((localDataBuffer instanceof DataBufferByte)) {
/*  296 */         localObject = ((DataBufferByte)localDataBuffer).getData();
/*  297 */         i7 = i5;
/*  298 */         i8 = 0;
/*  299 */         for (i9 = 0; i9 < m; i9++) {
/*  300 */           System.arraycopy(localObject, i3, arrayOfByte, i8, i7);
/*      */ 
/*  303 */           i8 += i7;
/*  304 */           i3 += i2;
/*      */         }
/*  306 */       } else if (((localDataBuffer instanceof DataBufferShort)) || ((localDataBuffer instanceof DataBufferUShort)))
/*      */       {
/*  308 */         localObject = (localDataBuffer instanceof DataBufferShort) ? ((DataBufferShort)localDataBuffer).getData() : ((DataBufferUShort)localDataBuffer).getData();
/*      */ 
/*  312 */         for (i7 = 0; i7 < m; i7++) {
/*  313 */           i8 = k;
/*  314 */           i9 = i3;
/*  315 */           while (i8 > 8) {
/*  316 */             i10 = localObject[(i9++)];
/*  317 */             arrayOfByte[(i6++)] = ((byte)(i10 >>> 8 & 0xFF));
/*  318 */             arrayOfByte[(i6++)] = ((byte)(i10 & 0xFF));
/*  319 */             i8 -= 16;
/*      */           }
/*  321 */           if (i8 > 0) {
/*  322 */             arrayOfByte[(i6++)] = ((byte)(localObject[i9] >>> 8 & 0xFF));
/*      */           }
/*  324 */           i3 += i2;
/*      */         }
/*  326 */       } else if ((localDataBuffer instanceof DataBufferInt)) {
/*  327 */         localObject = ((DataBufferInt)localDataBuffer).getData();
/*      */ 
/*  329 */         for (i7 = 0; i7 < m; i7++) {
/*  330 */           i8 = k;
/*  331 */           i9 = i3;
/*  332 */           while (i8 > 24) {
/*  333 */             i10 = localObject[(i9++)];
/*  334 */             arrayOfByte[(i6++)] = ((byte)(i10 >>> 24 & 0xFF));
/*  335 */             arrayOfByte[(i6++)] = ((byte)(i10 >>> 16 & 0xFF));
/*  336 */             arrayOfByte[(i6++)] = ((byte)(i10 >>> 8 & 0xFF));
/*  337 */             arrayOfByte[(i6++)] = ((byte)(i10 & 0xFF));
/*  338 */             i8 -= 32;
/*      */           }
/*  340 */           i10 = 24;
/*  341 */           while (i8 > 0) {
/*  342 */             arrayOfByte[(i6++)] = ((byte)(localObject[i9] >>> i10 & 0xFF));
/*      */ 
/*  344 */             i10 -= 8;
/*  345 */             i8 -= 8;
/*      */           }
/*  347 */           i3 += i2;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       int i11;
/*  351 */       if ((localDataBuffer instanceof DataBufferByte)) {
/*  352 */         localObject = ((DataBufferByte)localDataBuffer).getData();
/*      */ 
/*  354 */         if ((i4 & 0x7) == 0) {
/*  355 */           i7 = i5;
/*  356 */           i8 = 0;
/*  357 */           for (i9 = 0; i9 < m; i9++) {
/*  358 */             System.arraycopy(localObject, i3, arrayOfByte, i8, i7);
/*      */ 
/*  361 */             i8 += i7;
/*  362 */             i3 += i2;
/*      */           }
/*      */         } else {
/*  365 */           i7 = i4 & 0x7;
/*  366 */           i8 = 8 - i7;
/*  367 */           for (i9 = 0; i9 < m; i9++) {
/*  368 */             i10 = i3;
/*  369 */             i11 = k;
/*  370 */             while (i11 > 0) {
/*  371 */               if (i11 > i8) {
/*  372 */                 arrayOfByte[(i6++)] = ((byte)((localObject[(i10++)] & 0xFF) << i7 | (localObject[i10] & 0xFF) >>> i8));
/*      */               }
/*      */               else
/*      */               {
/*  376 */                 arrayOfByte[(i6++)] = ((byte)((localObject[i10] & 0xFF) << i7));
/*      */               }
/*      */ 
/*  379 */               i11 -= 8;
/*      */             }
/*  381 */             i3 += i2;
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int i12;
/*      */         int i13;
/*      */         int i14;
/*  384 */         if (((localDataBuffer instanceof DataBufferShort)) || ((localDataBuffer instanceof DataBufferUShort)))
/*      */         {
/*  386 */           localObject = (localDataBuffer instanceof DataBufferShort) ? ((DataBufferShort)localDataBuffer).getData() : ((DataBufferUShort)localDataBuffer).getData();
/*      */ 
/*  390 */           for (i7 = 0; i7 < m; i7++) {
/*  391 */             i8 = i4;
/*  392 */             for (i9 = 0; i9 < k; i8 += 8) {
/*  393 */               i10 = i3 + i8 / 16;
/*  394 */               i11 = i8 % 16;
/*  395 */               i12 = localObject[i10] & 0xFFFF;
/*  396 */               if (i11 <= 8) {
/*  397 */                 arrayOfByte[(i6++)] = ((byte)(i12 >>> 8 - i11));
/*      */               } else {
/*  399 */                 i13 = i11 - 8;
/*  400 */                 i14 = localObject[(i10 + 1)] & 0xFFFF;
/*  401 */                 arrayOfByte[(i6++)] = ((byte)(i12 << i13 | i14 >>> 16 - i13));
/*      */               }
/*  392 */               i9 += 8;
/*      */             }
/*      */ 
/*  406 */             i3 += i2;
/*      */           }
/*  408 */         } else if ((localDataBuffer instanceof DataBufferInt)) {
/*  409 */           localObject = ((DataBufferInt)localDataBuffer).getData();
/*      */ 
/*  411 */           for (i7 = 0; i7 < m; i7++) {
/*  412 */             i8 = i4;
/*  413 */             for (i9 = 0; i9 < k; i8 += 8) {
/*  414 */               i10 = i3 + i8 / 32;
/*  415 */               i11 = i8 % 32;
/*  416 */               i12 = localObject[i10];
/*  417 */               if (i11 <= 24) {
/*  418 */                 arrayOfByte[(i6++)] = ((byte)(i12 >>> 24 - i11));
/*      */               }
/*      */               else {
/*  421 */                 i13 = i11 - 24;
/*  422 */                 i14 = localObject[(i10 + 1)];
/*  423 */                 arrayOfByte[(i6++)] = ((byte)(i12 << i13 | i14 >>> 32 - i13));
/*      */               }
/*  413 */               i9 += 8;
/*      */             }
/*      */ 
/*  428 */             i3 += i2;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  433 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public static byte[] getUnpackedBinaryData(Raster paramRaster, Rectangle paramRectangle)
/*      */   {
/*  446 */     SampleModel localSampleModel = paramRaster.getSampleModel();
/*  447 */     if (!isBinary(localSampleModel)) {
/*  448 */       throw new IllegalArgumentException(I18N.getString("ImageUtil0"));
/*      */     }
/*      */ 
/*  451 */     int i = paramRectangle.x;
/*  452 */     int j = paramRectangle.y;
/*  453 */     int k = paramRectangle.width;
/*  454 */     int m = paramRectangle.height;
/*      */ 
/*  456 */     DataBuffer localDataBuffer = paramRaster.getDataBuffer();
/*      */ 
/*  458 */     int n = i - paramRaster.getSampleModelTranslateX();
/*  459 */     int i1 = j - paramRaster.getSampleModelTranslateY();
/*      */ 
/*  461 */     MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = (MultiPixelPackedSampleModel)localSampleModel;
/*  462 */     int i2 = localMultiPixelPackedSampleModel.getScanlineStride();
/*  463 */     int i3 = localDataBuffer.getOffset() + localMultiPixelPackedSampleModel.getOffset(n, i1);
/*  464 */     int i4 = localMultiPixelPackedSampleModel.getBitOffset(n);
/*      */ 
/*  466 */     byte[] arrayOfByte = new byte[k * m];
/*  467 */     int i5 = j + m;
/*  468 */     int i6 = i + k;
/*  469 */     int i7 = 0;
/*      */     Object localObject;
/*      */     int i8;
/*      */     int i9;
/*      */     int i10;
/*      */     int i11;
/*  471 */     if ((localDataBuffer instanceof DataBufferByte)) {
/*  472 */       localObject = ((DataBufferByte)localDataBuffer).getData();
/*  473 */       for (i8 = j; i8 < i5; i8++) {
/*  474 */         i9 = i3 * 8 + i4;
/*  475 */         for (i10 = i; i10 < i6; i10++) {
/*  476 */           i11 = localObject[(i9 / 8)];
/*  477 */           arrayOfByte[(i7++)] = ((byte)(i11 >>> (7 - i9 & 0x7) & 0x1));
/*      */ 
/*  479 */           i9++;
/*      */         }
/*  481 */         i3 += i2;
/*      */       }
/*  483 */     } else if (((localDataBuffer instanceof DataBufferShort)) || ((localDataBuffer instanceof DataBufferUShort)))
/*      */     {
/*  485 */       localObject = (localDataBuffer instanceof DataBufferShort) ? ((DataBufferShort)localDataBuffer).getData() : ((DataBufferUShort)localDataBuffer).getData();
/*      */ 
/*  488 */       for (i8 = j; i8 < i5; i8++) {
/*  489 */         i9 = i3 * 16 + i4;
/*  490 */         for (i10 = i; i10 < i6; i10++) {
/*  491 */           i11 = localObject[(i9 / 16)];
/*  492 */           arrayOfByte[(i7++)] = ((byte)(i11 >>> 15 - i9 % 16 & 0x1));
/*      */ 
/*  495 */           i9++;
/*      */         }
/*  497 */         i3 += i2;
/*      */       }
/*  499 */     } else if ((localDataBuffer instanceof DataBufferInt)) {
/*  500 */       localObject = ((DataBufferInt)localDataBuffer).getData();
/*  501 */       for (i8 = j; i8 < i5; i8++) {
/*  502 */         i9 = i3 * 32 + i4;
/*  503 */         for (i10 = i; i10 < i6; i10++) {
/*  504 */           i11 = localObject[(i9 / 32)];
/*  505 */           arrayOfByte[(i7++)] = ((byte)(i11 >>> 31 - i9 % 32 & 0x1));
/*      */ 
/*  508 */           i9++;
/*      */         }
/*  510 */         i3 += i2;
/*      */       }
/*      */     }
/*      */ 
/*  514 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public static void setPackedBinaryData(byte[] paramArrayOfByte, WritableRaster paramWritableRaster, Rectangle paramRectangle)
/*      */   {
/*  529 */     SampleModel localSampleModel = paramWritableRaster.getSampleModel();
/*  530 */     if (!isBinary(localSampleModel)) {
/*  531 */       throw new IllegalArgumentException(I18N.getString("ImageUtil0"));
/*      */     }
/*      */ 
/*  534 */     int i = paramRectangle.x;
/*  535 */     int j = paramRectangle.y;
/*  536 */     int k = paramRectangle.width;
/*  537 */     int m = paramRectangle.height;
/*      */ 
/*  539 */     DataBuffer localDataBuffer = paramWritableRaster.getDataBuffer();
/*      */ 
/*  541 */     int n = i - paramWritableRaster.getSampleModelTranslateX();
/*  542 */     int i1 = j - paramWritableRaster.getSampleModelTranslateY();
/*      */ 
/*  544 */     MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = (MultiPixelPackedSampleModel)localSampleModel;
/*  545 */     int i2 = localMultiPixelPackedSampleModel.getScanlineStride();
/*  546 */     int i3 = localDataBuffer.getOffset() + localMultiPixelPackedSampleModel.getOffset(n, i1);
/*  547 */     int i4 = localMultiPixelPackedSampleModel.getBitOffset(n);
/*      */ 
/*  549 */     int i5 = 0;
/*      */     int i7;
/*      */     int i9;
/*      */     int i10;
/*  551 */     if (i4 == 0)
/*      */     {
/*      */       Object localObject1;
/*      */       int i8;
/*  552 */       if ((localDataBuffer instanceof DataBufferByte)) {
/*  553 */         localObject1 = ((DataBufferByte)localDataBuffer).getData();
/*  554 */         if (localObject1 == paramArrayOfByte)
/*      */         {
/*  556 */           return;
/*      */         }
/*  558 */         i7 = (k + 7) / 8;
/*  559 */         i8 = 0;
/*  560 */         for (i9 = 0; i9 < m; i9++) {
/*  561 */           System.arraycopy(paramArrayOfByte, i8, localObject1, i3, i7);
/*      */ 
/*  564 */           i8 += i7;
/*  565 */           i3 += i2;
/*      */         }
/*  567 */       } else if (((localDataBuffer instanceof DataBufferShort)) || ((localDataBuffer instanceof DataBufferUShort)))
/*      */       {
/*  569 */         localObject1 = (localDataBuffer instanceof DataBufferShort) ? ((DataBufferShort)localDataBuffer).getData() : ((DataBufferUShort)localDataBuffer).getData();
/*      */ 
/*  573 */         for (i7 = 0; i7 < m; i7++) {
/*  574 */           i8 = k;
/*  575 */           i9 = i3;
/*  576 */           while (i8 > 8) {
/*  577 */             localObject1[(i9++)] = ((short)((paramArrayOfByte[(i5++)] & 0xFF) << 8 | paramArrayOfByte[(i5++)] & 0xFF));
/*      */ 
/*  580 */             i8 -= 16;
/*      */           }
/*  582 */           if (i8 > 0) {
/*  583 */             localObject1[(i9++)] = ((short)((paramArrayOfByte[(i5++)] & 0xFF) << 8));
/*      */           }
/*      */ 
/*  586 */           i3 += i2;
/*      */         }
/*  588 */       } else if ((localDataBuffer instanceof DataBufferInt)) {
/*  589 */         localObject1 = ((DataBufferInt)localDataBuffer).getData();
/*      */ 
/*  591 */         for (i7 = 0; i7 < m; i7++) {
/*  592 */           i8 = k;
/*  593 */           i9 = i3;
/*  594 */           while (i8 > 24) {
/*  595 */             localObject1[(i9++)] = ((paramArrayOfByte[(i5++)] & 0xFF) << 24 | (paramArrayOfByte[(i5++)] & 0xFF) << 16 | (paramArrayOfByte[(i5++)] & 0xFF) << 8 | paramArrayOfByte[(i5++)] & 0xFF);
/*      */ 
/*  600 */             i8 -= 32;
/*      */           }
/*  602 */           i10 = 24;
/*  603 */           while (i8 > 0) {
/*  604 */             localObject1[i9] |= (paramArrayOfByte[(i5++)] & 0xFF) << i10;
/*      */ 
/*  606 */             i10 -= 8;
/*  607 */             i8 -= 8;
/*      */           }
/*  609 */           i3 += i2;
/*      */         }
/*      */       }
/*      */     } else {
/*  613 */       int i6 = (k + 7) / 8;
/*  614 */       i7 = 0;
/*      */       Object localObject2;
/*      */       int i11;
/*      */       int i12;
/*      */       int i13;
/*      */       int i14;
/*      */       int i15;
/*      */       int i16;
/*      */       int i17;
/*      */       int i18;
/*  615 */       if ((localDataBuffer instanceof DataBufferByte)) {
/*  616 */         localObject2 = ((DataBufferByte)localDataBuffer).getData();
/*      */ 
/*  618 */         if ((i4 & 0x7) == 0) {
/*  619 */           for (i9 = 0; i9 < m; i9++) {
/*  620 */             System.arraycopy(paramArrayOfByte, i7, localObject2, i3, i6);
/*      */ 
/*  623 */             i7 += i6;
/*  624 */             i3 += i2;
/*      */           }
/*      */         } else {
/*  627 */           i9 = i4 & 0x7;
/*  628 */           i10 = 8 - i9;
/*  629 */           i11 = 8 + i10;
/*  630 */           i12 = (byte)(255 << i10);
/*  631 */           i13 = (byte)(i12 ^ 0xFFFFFFFF);
/*      */ 
/*  633 */           for (i14 = 0; i14 < m; i14++) {
/*  634 */             i15 = i3;
/*  635 */             i16 = k;
/*  636 */             while (i16 > 0) {
/*  637 */               i17 = paramArrayOfByte[(i5++)];
/*      */ 
/*  639 */               if (i16 > i11)
/*      */               {
/*  642 */                 localObject2[i15] = ((byte)(localObject2[i15] & i12 | (i17 & 0xFF) >>> i9));
/*      */ 
/*  644 */                 localObject2[(++i15)] = ((byte)((i17 & 0xFF) << i10));
/*  645 */               } else if (i16 > i10)
/*      */               {
/*  649 */                 localObject2[i15] = ((byte)(localObject2[i15] & i12 | (i17 & 0xFF) >>> i9));
/*      */ 
/*  651 */                 i15++;
/*  652 */                 localObject2[i15] = ((byte)(localObject2[i15] & i13 | (i17 & 0xFF) << i10));
/*      */               }
/*      */               else
/*      */               {
/*  657 */                 i18 = (1 << i10 - i16) - 1;
/*  658 */                 localObject2[i15] = ((byte)(localObject2[i15] & (i12 | i18) | (i17 & 0xFF) >>> i9 & (i18 ^ 0xFFFFFFFF)));
/*      */               }
/*      */ 
/*  662 */               i16 -= 8;
/*      */             }
/*  664 */             i3 += i2;
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int i19;
/*      */         int i20;
/*      */         int i21;
/*  667 */         if (((localDataBuffer instanceof DataBufferShort)) || ((localDataBuffer instanceof DataBufferUShort)))
/*      */         {
/*  669 */           localObject2 = (localDataBuffer instanceof DataBufferShort) ? ((DataBufferShort)localDataBuffer).getData() : ((DataBufferUShort)localDataBuffer).getData();
/*      */ 
/*  673 */           i9 = i4 & 0x7;
/*  674 */           i10 = 8 - i9;
/*  675 */           i11 = 16 + i10;
/*  676 */           i12 = (short)(255 << i10 ^ 0xFFFFFFFF);
/*  677 */           i13 = (short)(65535 << i10);
/*  678 */           i14 = (short)(i13 ^ 0xFFFFFFFF);
/*      */ 
/*  680 */           for (i15 = 0; i15 < m; i15++) {
/*  681 */             i16 = i4;
/*  682 */             i17 = k;
/*  683 */             for (i18 = 0; i18 < k; 
/*  684 */               i17 -= 8) {
/*  685 */               i19 = i3 + (i16 >> 4);
/*  686 */               i20 = i16 & 0xF;
/*  687 */               i21 = paramArrayOfByte[(i5++)] & 0xFF;
/*  688 */               if (i20 <= 8)
/*      */               {
/*  690 */                 if (i17 < 8)
/*      */                 {
/*  692 */                   i21 &= 255 << 8 - i17;
/*      */                 }
/*  694 */                 localObject2[i19] = ((short)(localObject2[i19] & i12 | i21 << i10));
/*  695 */               } else if (i17 > i11)
/*      */               {
/*  697 */                 localObject2[i19] = ((short)(localObject2[i19] & i13 | i21 >>> i9 & 0xFFFF));
/*  698 */                 localObject2[(++i19)] = ((short)(i21 << i10 & 0xFFFF));
/*      */               }
/*  700 */               else if (i17 > i10)
/*      */               {
/*  703 */                 localObject2[i19] = ((short)(localObject2[i19] & i13 | i21 >>> i9 & 0xFFFF));
/*  704 */                 i19++;
/*  705 */                 localObject2[i19] = ((short)(localObject2[i19] & i14 | i21 << i10 & 0xFFFF));
/*      */               }
/*      */               else
/*      */               {
/*  710 */                 int i22 = (1 << i10 - i17) - 1;
/*  711 */                 localObject2[i19] = ((short)(localObject2[i19] & (i13 | i22) | i21 >>> i9 & 0xFFFF & (i22 ^ 0xFFFFFFFF)));
/*      */               }
/*  684 */               i18 += 8; i16 += 8;
/*      */             }
/*      */ 
/*  715 */             i3 += i2;
/*      */           }
/*  717 */         } else if ((localDataBuffer instanceof DataBufferInt)) {
/*  718 */           localObject2 = ((DataBufferInt)localDataBuffer).getData();
/*  719 */           i9 = i4 & 0x7;
/*  720 */           i10 = 8 - i9;
/*  721 */           i11 = 32 + i10;
/*  722 */           i12 = -1 << i10;
/*  723 */           i13 = i12 ^ 0xFFFFFFFF;
/*      */ 
/*  725 */           for (i14 = 0; i14 < m; i14++) {
/*  726 */             i15 = i4;
/*  727 */             i16 = k;
/*  728 */             for (i17 = 0; i17 < k; 
/*  729 */               i16 -= 8) {
/*  730 */               i18 = i3 + (i15 >> 5);
/*  731 */               i19 = i15 & 0x1F;
/*  732 */               i20 = paramArrayOfByte[(i5++)] & 0xFF;
/*  733 */               if (i19 <= 24)
/*      */               {
/*  735 */                 i21 = 24 - i19;
/*  736 */                 if (i16 < 8)
/*      */                 {
/*  738 */                   i20 &= 255 << 8 - i16;
/*      */                 }
/*  740 */                 localObject2[i18] = (localObject2[i18] & (255 << i21 ^ 0xFFFFFFFF) | i20 << i21);
/*  741 */               } else if (i16 > i11)
/*      */               {
/*  743 */                 localObject2[i18] = (localObject2[i18] & i12 | i20 >>> i9);
/*  744 */                 localObject2[(++i18)] = (i20 << i10);
/*  745 */               } else if (i16 > i10)
/*      */               {
/*  748 */                 localObject2[i18] = (localObject2[i18] & i12 | i20 >>> i9);
/*  749 */                 i18++;
/*  750 */                 localObject2[i18] = (localObject2[i18] & i13 | i20 << i10);
/*      */               }
/*      */               else {
/*  753 */                 i21 = (1 << i10 - i16) - 1;
/*  754 */                 localObject2[i18] = (localObject2[i18] & (i12 | i21) | i20 >>> i9 & (i21 ^ 0xFFFFFFFF));
/*      */               }
/*  729 */               i17 += 8; i15 += 8;
/*      */             }
/*      */ 
/*  758 */             i3 += i2;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void setUnpackedBinaryData(byte[] paramArrayOfByte, WritableRaster paramWritableRaster, Rectangle paramRectangle)
/*      */   {
/*  779 */     SampleModel localSampleModel = paramWritableRaster.getSampleModel();
/*  780 */     if (!isBinary(localSampleModel)) {
/*  781 */       throw new IllegalArgumentException(I18N.getString("ImageUtil0"));
/*      */     }
/*      */ 
/*  784 */     int i = paramRectangle.x;
/*  785 */     int j = paramRectangle.y;
/*  786 */     int k = paramRectangle.width;
/*  787 */     int m = paramRectangle.height;
/*      */ 
/*  789 */     DataBuffer localDataBuffer = paramWritableRaster.getDataBuffer();
/*      */ 
/*  791 */     int n = i - paramWritableRaster.getSampleModelTranslateX();
/*  792 */     int i1 = j - paramWritableRaster.getSampleModelTranslateY();
/*      */ 
/*  794 */     MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = (MultiPixelPackedSampleModel)localSampleModel;
/*  795 */     int i2 = localMultiPixelPackedSampleModel.getScanlineStride();
/*  796 */     int i3 = localDataBuffer.getOffset() + localMultiPixelPackedSampleModel.getOffset(n, i1);
/*  797 */     int i4 = localMultiPixelPackedSampleModel.getBitOffset(n);
/*      */ 
/*  799 */     int i5 = 0;
/*      */     Object localObject;
/*      */     int i6;
/*      */     int i7;
/*      */     int i8;
/*  801 */     if ((localDataBuffer instanceof DataBufferByte)) {
/*  802 */       localObject = ((DataBufferByte)localDataBuffer).getData();
/*  803 */       for (i6 = 0; i6 < m; i6++) {
/*  804 */         i7 = i3 * 8 + i4;
/*  805 */         for (i8 = 0; i8 < k; i8++) {
/*  806 */           if (paramArrayOfByte[(i5++)] != 0)
/*      */           {
/*      */             int tmp180_179 = (i7 / 8);
/*      */             Object tmp180_173 = localObject; tmp180_173[tmp180_179] = ((byte)(tmp180_173[tmp180_179] | (byte)(1 << (7 - i7 & 0x7))));
/*      */           }
/*      */ 
/*  810 */           i7++;
/*      */         }
/*  812 */         i3 += i2;
/*      */       }
/*  814 */     } else if (((localDataBuffer instanceof DataBufferShort)) || ((localDataBuffer instanceof DataBufferUShort)))
/*      */     {
/*  816 */       localObject = (localDataBuffer instanceof DataBufferShort) ? ((DataBufferShort)localDataBuffer).getData() : ((DataBufferUShort)localDataBuffer).getData();
/*      */ 
/*  819 */       for (i6 = 0; i6 < m; i6++) {
/*  820 */         i7 = i3 * 16 + i4;
/*  821 */         for (i8 = 0; i8 < k; i8++) {
/*  822 */           if (paramArrayOfByte[(i5++)] != 0)
/*      */           {
/*      */             int tmp313_312 = (i7 / 16);
/*      */             Object tmp313_306 = localObject; tmp313_306[tmp313_312] = ((short)(tmp313_306[tmp313_312] | (short)(1 << 15 - i7 % 16)));
/*      */           }
/*      */ 
/*  827 */           i7++;
/*      */         }
/*  829 */         i3 += i2;
/*      */       }
/*  831 */     } else if ((localDataBuffer instanceof DataBufferInt)) {
/*  832 */       localObject = ((DataBufferInt)localDataBuffer).getData();
/*  833 */       for (i6 = 0; i6 < m; i6++) {
/*  834 */         i7 = i3 * 32 + i4;
/*  835 */         for (i8 = 0; i8 < k; i8++) {
/*  836 */           if (paramArrayOfByte[(i5++)] != 0) {
/*  837 */             localObject[(i7 / 32)] |= 1 << 31 - i7 % 32;
/*      */           }
/*      */ 
/*  841 */           i7++;
/*      */         }
/*  843 */         i3 += i2;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static boolean isBinary(SampleModel paramSampleModel) {
/*  849 */     return ((paramSampleModel instanceof MultiPixelPackedSampleModel)) && (((MultiPixelPackedSampleModel)paramSampleModel).getPixelBitStride() == 1) && (paramSampleModel.getNumBands() == 1);
/*      */   }
/*      */ 
/*      */   public static ColorModel createColorModel(ColorSpace paramColorSpace, SampleModel paramSampleModel)
/*      */   {
/*  856 */     Object localObject = null;
/*      */ 
/*  858 */     if (paramSampleModel == null) {
/*  859 */       throw new IllegalArgumentException(I18N.getString("ImageUtil1"));
/*      */     }
/*      */ 
/*  862 */     int i = paramSampleModel.getNumBands();
/*  863 */     if ((i < 1) || (i > 4)) {
/*  864 */       return null;
/*      */     }
/*      */ 
/*  867 */     int j = paramSampleModel.getDataType();
/*      */     boolean bool2;
/*      */     int i2;
/*      */     int i4;
/*  868 */     if ((paramSampleModel instanceof ComponentSampleModel)) {
/*  869 */       if ((j < 0) || (j > 5))
/*      */       {
/*  872 */         return null;
/*      */       }
/*      */ 
/*  875 */       if (paramColorSpace == null) {
/*  876 */         paramColorSpace = i <= 2 ? ColorSpace.getInstance(1003) : ColorSpace.getInstance(1000);
/*      */       }
/*      */ 
/*  881 */       boolean bool1 = (i == 2) || (i == 4);
/*  882 */       int m = bool1 ? 3 : 1;
/*      */ 
/*  885 */       bool2 = false;
/*      */ 
/*  887 */       i2 = DataBuffer.getDataTypeSize(j);
/*  888 */       int[] arrayOfInt2 = new int[i];
/*  889 */       for (i4 = 0; i4 < i; i4++) {
/*  890 */         arrayOfInt2[i4] = i2;
/*      */       }
/*      */ 
/*  893 */       localObject = new ComponentColorModel(paramColorSpace, arrayOfInt2, bool1, bool2, m, j);
/*      */     }
/*  899 */     else if ((paramSampleModel instanceof SinglePixelPackedSampleModel)) {
/*  900 */       SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = (SinglePixelPackedSampleModel)paramSampleModel;
/*      */ 
/*  903 */       int[] arrayOfInt1 = localSinglePixelPackedSampleModel.getBitMasks();
/*  904 */       bool2 = false;
/*  905 */       i2 = 0;
/*  906 */       int i3 = 0;
/*  907 */       i4 = 0;
/*      */ 
/*  909 */       i = arrayOfInt1.length;
/*      */       int i1;
/*  910 */       if (i <= 2) {
/*  911 */         i1 = i2 = i3 = arrayOfInt1[0];
/*  912 */         if (i == 2)
/*  913 */           i4 = arrayOfInt1[1];
/*      */       }
/*      */       else {
/*  916 */         i1 = arrayOfInt1[0];
/*  917 */         i2 = arrayOfInt1[1];
/*  918 */         i3 = arrayOfInt1[2];
/*  919 */         if (i == 4) {
/*  920 */           i4 = arrayOfInt1[3];
/*      */         }
/*      */       }
/*      */ 
/*  924 */       int[] arrayOfInt3 = localSinglePixelPackedSampleModel.getSampleSize();
/*  925 */       int i5 = 0;
/*  926 */       for (int i6 = 0; i6 < arrayOfInt3.length; i6++) {
/*  927 */         i5 += arrayOfInt3[i6];
/*      */       }
/*      */ 
/*  930 */       if (paramColorSpace == null) {
/*  931 */         paramColorSpace = ColorSpace.getInstance(1000);
/*      */       }
/*  933 */       localObject = new DirectColorModel(paramColorSpace, i5, i1, i2, i3, i4, false, paramSampleModel.getDataType());
/*      */     }
/*  938 */     else if ((paramSampleModel instanceof MultiPixelPackedSampleModel)) {
/*  939 */       int k = ((MultiPixelPackedSampleModel)paramSampleModel).getPixelBitStride();
/*      */ 
/*  941 */       int n = 1 << k;
/*  942 */       byte[] arrayOfByte = new byte[n];
/*      */ 
/*  944 */       for (i2 = 0; i2 < n; i2++) {
/*  945 */         arrayOfByte[i2] = ((byte)(255 * i2 / (n - 1)));
/*      */       }
/*  947 */       localObject = new IndexColorModel(k, n, arrayOfByte, arrayOfByte, arrayOfByte);
/*      */     }
/*      */ 
/*  950 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static int getElementSize(SampleModel paramSampleModel) {
/*  954 */     int i = DataBuffer.getDataTypeSize(paramSampleModel.getDataType());
/*      */ 
/*  956 */     if ((paramSampleModel instanceof MultiPixelPackedSampleModel)) {
/*  957 */       MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = (MultiPixelPackedSampleModel)paramSampleModel;
/*      */ 
/*  959 */       return localMultiPixelPackedSampleModel.getSampleSize(0) * localMultiPixelPackedSampleModel.getNumBands();
/*  960 */     }if ((paramSampleModel instanceof ComponentSampleModel))
/*  961 */       return paramSampleModel.getNumBands() * i;
/*  962 */     if ((paramSampleModel instanceof SinglePixelPackedSampleModel)) {
/*  963 */       return i;
/*      */     }
/*      */ 
/*  966 */     return i * paramSampleModel.getNumBands();
/*      */   }
/*      */ 
/*      */   public static long getTileSize(SampleModel paramSampleModel)
/*      */   {
/*  971 */     int i = DataBuffer.getDataTypeSize(paramSampleModel.getDataType());
/*      */     Object localObject;
/*  973 */     if ((paramSampleModel instanceof MultiPixelPackedSampleModel)) {
/*  974 */       localObject = (MultiPixelPackedSampleModel)paramSampleModel;
/*      */ 
/*  976 */       return (((MultiPixelPackedSampleModel)localObject).getScanlineStride() * ((MultiPixelPackedSampleModel)localObject).getHeight() + (((MultiPixelPackedSampleModel)localObject).getDataBitOffset() + i - 1) / i) * ((i + 7) / 8);
/*      */     }
/*      */ 
/*  979 */     if ((paramSampleModel instanceof ComponentSampleModel)) {
/*  980 */       localObject = (ComponentSampleModel)paramSampleModel;
/*  981 */       int[] arrayOfInt1 = ((ComponentSampleModel)localObject).getBandOffsets();
/*  982 */       int j = arrayOfInt1[0];
/*  983 */       for (int k = 1; k < arrayOfInt1.length; k++) {
/*  984 */         j = Math.max(j, arrayOfInt1[k]);
/*      */       }
/*  986 */       long l2 = 0L;
/*  987 */       int m = ((ComponentSampleModel)localObject).getPixelStride();
/*  988 */       int n = ((ComponentSampleModel)localObject).getScanlineStride();
/*  989 */       if (j >= 0)
/*  990 */         l2 += j + 1;
/*  991 */       if (m > 0)
/*  992 */         l2 += m * (paramSampleModel.getWidth() - 1);
/*  993 */       if (n > 0) {
/*  994 */         l2 += n * (paramSampleModel.getHeight() - 1);
/*      */       }
/*  996 */       int[] arrayOfInt2 = ((ComponentSampleModel)localObject).getBankIndices();
/*  997 */       j = arrayOfInt2[0];
/*  998 */       for (int i1 = 1; i1 < arrayOfInt2.length; i1++)
/*  999 */         j = Math.max(j, arrayOfInt2[i1]);
/* 1000 */       return l2 * (j + 1) * ((i + 7) / 8);
/* 1001 */     }if ((paramSampleModel instanceof SinglePixelPackedSampleModel)) {
/* 1002 */       localObject = (SinglePixelPackedSampleModel)paramSampleModel;
/*      */ 
/* 1004 */       long l1 = ((SinglePixelPackedSampleModel)localObject).getScanlineStride() * (((SinglePixelPackedSampleModel)localObject).getHeight() - 1) + ((SinglePixelPackedSampleModel)localObject).getWidth();
/*      */ 
/* 1006 */       return l1 * ((i + 7) / 8);
/*      */     }
/*      */ 
/* 1009 */     return 0L;
/*      */   }
/*      */ 
/*      */   public static long getBandSize(SampleModel paramSampleModel) {
/* 1013 */     int i = DataBuffer.getDataTypeSize(paramSampleModel.getDataType());
/*      */ 
/* 1015 */     if ((paramSampleModel instanceof ComponentSampleModel)) {
/* 1016 */       ComponentSampleModel localComponentSampleModel = (ComponentSampleModel)paramSampleModel;
/* 1017 */       int j = localComponentSampleModel.getPixelStride();
/* 1018 */       int k = localComponentSampleModel.getScanlineStride();
/* 1019 */       long l = Math.min(j, k);
/*      */ 
/* 1021 */       if (j > 0)
/* 1022 */         l += j * (paramSampleModel.getWidth() - 1);
/* 1023 */       if (k > 0)
/* 1024 */         l += k * (paramSampleModel.getHeight() - 1);
/* 1025 */       return l * ((i + 7) / 8);
/*      */     }
/* 1027 */     return getTileSize(paramSampleModel);
/*      */   }
/*      */ 
/*      */   public static boolean isIndicesForGrayscale(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*      */   {
/* 1039 */     if ((paramArrayOfByte1.length != paramArrayOfByte2.length) || (paramArrayOfByte1.length != paramArrayOfByte3.length)) {
/* 1040 */       return false;
/*      */     }
/* 1042 */     int i = paramArrayOfByte1.length;
/*      */ 
/* 1044 */     if (i != 256) {
/* 1045 */       return false;
/*      */     }
/* 1047 */     for (int j = 0; j < i; j++) {
/* 1048 */       int k = (byte)j;
/*      */ 
/* 1050 */       if ((paramArrayOfByte1[j] != k) || (paramArrayOfByte2[j] != k) || (paramArrayOfByte3[j] != k)) {
/* 1051 */         return false;
/*      */       }
/*      */     }
/* 1054 */     return true;
/*      */   }
/*      */ 
/*      */   public static String convertObjectToString(Object paramObject)
/*      */   {
/* 1059 */     if (paramObject == null) {
/* 1060 */       return "";
/*      */     }
/* 1062 */     String str = "";
/*      */     Object localObject;
/*      */     int i;
/* 1063 */     if ((paramObject instanceof byte[])) {
/* 1064 */       localObject = (byte[])paramObject;
/* 1065 */       for (i = 0; i < localObject.length; i++)
/* 1066 */         str = str + localObject[i] + " ";
/* 1067 */       return str;
/*      */     }
/*      */ 
/* 1070 */     if ((paramObject instanceof int[])) {
/* 1071 */       localObject = (int[])paramObject;
/* 1072 */       for (i = 0; i < localObject.length; i++)
/* 1073 */         str = str + localObject[i] + " ";
/* 1074 */       return str;
/*      */     }
/*      */ 
/* 1077 */     if ((paramObject instanceof short[])) {
/* 1078 */       localObject = (short[])paramObject;
/* 1079 */       for (i = 0; i < localObject.length; i++)
/* 1080 */         str = str + localObject[i] + " ";
/* 1081 */       return str;
/*      */     }
/*      */ 
/* 1084 */     return paramObject.toString();
/*      */   }
/*      */ 
/*      */   public static final void canEncodeImage(ImageWriter paramImageWriter, ImageTypeSpecifier paramImageTypeSpecifier)
/*      */     throws IIOException
/*      */   {
/* 1098 */     ImageWriterSpi localImageWriterSpi = paramImageWriter.getOriginatingProvider();
/*      */ 
/* 1100 */     if ((paramImageTypeSpecifier != null) && (localImageWriterSpi != null) && (!localImageWriterSpi.canEncodeImage(paramImageTypeSpecifier)))
/* 1101 */       throw new IIOException(I18N.getString("ImageUtil2") + " " + paramImageWriter.getClass().getName());
/*      */   }
/*      */ 
/*      */   public static final void canEncodeImage(ImageWriter paramImageWriter, ColorModel paramColorModel, SampleModel paramSampleModel)
/*      */     throws IIOException
/*      */   {
/* 1118 */     ImageTypeSpecifier localImageTypeSpecifier = null;
/* 1119 */     if ((paramColorModel != null) && (paramSampleModel != null))
/* 1120 */       localImageTypeSpecifier = new ImageTypeSpecifier(paramColorModel, paramSampleModel);
/* 1121 */     canEncodeImage(paramImageWriter, localImageTypeSpecifier);
/*      */   }
/*      */ 
/*      */   public static final boolean imageIsContiguous(RenderedImage paramRenderedImage)
/*      */   {
/*      */     Object localObject;
/*      */     SampleModel localSampleModel;
/* 1129 */     if ((paramRenderedImage instanceof BufferedImage)) {
/* 1130 */       localObject = ((BufferedImage)paramRenderedImage).getRaster();
/* 1131 */       localSampleModel = ((WritableRaster)localObject).getSampleModel();
/*      */     } else {
/* 1133 */       localSampleModel = paramRenderedImage.getSampleModel();
/*      */     }
/*      */ 
/* 1136 */     if ((localSampleModel instanceof ComponentSampleModel))
/*      */     {
/* 1139 */       localObject = (ComponentSampleModel)localSampleModel;
/*      */ 
/* 1141 */       if (((ComponentSampleModel)localObject).getPixelStride() != ((ComponentSampleModel)localObject).getNumBands()) {
/* 1142 */         return false;
/*      */       }
/*      */ 
/* 1145 */       int[] arrayOfInt1 = ((ComponentSampleModel)localObject).getBandOffsets();
/* 1146 */       for (int i = 0; i < arrayOfInt1.length; i++) {
/* 1147 */         if (arrayOfInt1[i] != i) {
/* 1148 */           return false;
/*      */         }
/*      */       }
/*      */ 
/* 1152 */       int[] arrayOfInt2 = ((ComponentSampleModel)localObject).getBankIndices();
/* 1153 */       for (int j = 0; j < arrayOfInt1.length; j++) {
/* 1154 */         if (arrayOfInt2[j] != 0) {
/* 1155 */           return false;
/*      */         }
/*      */       }
/*      */ 
/* 1159 */       return true;
/*      */     }
/*      */ 
/* 1165 */     return isBinary(localSampleModel);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.common.ImageUtil
 * JD-Core Version:    0.6.2
 */