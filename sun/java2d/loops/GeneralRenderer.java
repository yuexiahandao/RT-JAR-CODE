/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.font.GlyphList;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public final class GeneralRenderer
/*     */ {
/*     */   static final int OUTCODE_TOP = 1;
/*     */   static final int OUTCODE_BOTTOM = 2;
/*     */   static final int OUTCODE_LEFT = 4;
/*     */   static final int OUTCODE_RIGHT = 8;
/*     */ 
/*     */   public static void register()
/*     */   {
/*  53 */     GeneralRenderer localGeneralRenderer = GeneralRenderer.class;
/*  54 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive = { new GraphicsPrimitiveProxy(localGeneralRenderer, "SetFillRectANY", FillRect.methodSignature, FillRect.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "SetFillPathANY", FillPath.methodSignature, FillPath.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "SetFillSpansANY", FillSpans.methodSignature, FillSpans.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "SetDrawLineANY", DrawLine.methodSignature, DrawLine.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "SetDrawPolygonsANY", DrawPolygons.methodSignature, DrawPolygons.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "SetDrawPathANY", DrawPath.methodSignature, DrawPath.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "SetDrawRectANY", DrawRect.methodSignature, DrawRect.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorFillRectANY", FillRect.methodSignature, FillRect.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorFillPathANY", FillPath.methodSignature, FillPath.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorFillSpansANY", FillSpans.methodSignature, FillSpans.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorDrawLineANY", DrawLine.methodSignature, DrawLine.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorDrawPolygonsANY", DrawPolygons.methodSignature, DrawPolygons.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorDrawPathANY", DrawPath.methodSignature, DrawPath.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorDrawRectANY", DrawRect.methodSignature, DrawRect.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorDrawGlyphListANY", DrawGlyphList.methodSignature, DrawGlyphList.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(localGeneralRenderer, "XorDrawGlyphListAAANY", DrawGlyphListAA.methodSignature, DrawGlyphListAA.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any) };
/*     */ 
/* 153 */     GraphicsPrimitiveMgr.register(arrayOfGraphicsPrimitive);
/*     */   }
/*     */ 
/*     */   static void doDrawPoly(SurfaceData paramSurfaceData, PixelWriter paramPixelWriter, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2, Region paramRegion, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 161 */     int[] arrayOfInt = null;
/*     */ 
/* 163 */     if (paramInt2 <= 0)
/*     */       return;
/*     */     int k;
/* 166 */     int i = k = paramArrayOfInt1[paramInt1] + paramInt3;
/*     */     int m;
/* 167 */     int j = m = paramArrayOfInt2[paramInt1] + paramInt4;
/*     */     while (true) { paramInt2--; if (paramInt2 <= 0) break;
/* 169 */       paramInt1++;
/* 170 */       int n = paramArrayOfInt1[paramInt1] + paramInt3;
/* 171 */       int i1 = paramArrayOfInt2[paramInt1] + paramInt4;
/* 172 */       arrayOfInt = doDrawLine(paramSurfaceData, paramPixelWriter, arrayOfInt, paramRegion, k, m, n, i1);
/*     */ 
/* 174 */       k = n;
/* 175 */       m = i1;
/*     */     }
/* 177 */     if ((paramBoolean) && ((k != i) || (m != j)))
/* 178 */       arrayOfInt = doDrawLine(paramSurfaceData, paramPixelWriter, arrayOfInt, paramRegion, k, m, i, j);
/*     */   }
/*     */ 
/*     */   static void doSetRect(SurfaceData paramSurfaceData, PixelWriter paramPixelWriter, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 185 */     WritableRaster localWritableRaster = (WritableRaster)paramSurfaceData.getRaster(paramInt1, paramInt2, paramInt3 - paramInt1, paramInt4 - paramInt2);
/*     */ 
/* 187 */     paramPixelWriter.setRaster(localWritableRaster);
/*     */ 
/* 189 */     while (paramInt2 < paramInt4) {
/* 190 */       for (int i = paramInt1; i < paramInt3; i++) {
/* 191 */         paramPixelWriter.writePixel(i, paramInt2);
/*     */       }
/* 193 */       paramInt2++;
/*     */     }
/*     */   }
/*     */ 
/*     */   static int[] doDrawLine(SurfaceData paramSurfaceData, PixelWriter paramPixelWriter, int[] paramArrayOfInt, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 201 */     if (paramArrayOfInt == null) {
/* 202 */       paramArrayOfInt = new int[8];
/*     */     }
/* 204 */     paramArrayOfInt[0] = paramInt1;
/* 205 */     paramArrayOfInt[1] = paramInt2;
/* 206 */     paramArrayOfInt[2] = paramInt3;
/* 207 */     paramArrayOfInt[3] = paramInt4;
/* 208 */     if (!adjustLine(paramArrayOfInt, paramRegion.getLoX(), paramRegion.getLoY(), paramRegion.getHiX(), paramRegion.getHiY()))
/*     */     {
/* 212 */       return paramArrayOfInt;
/*     */     }
/* 214 */     int i = paramArrayOfInt[0];
/* 215 */     int j = paramArrayOfInt[1];
/* 216 */     int k = paramArrayOfInt[2];
/* 217 */     int m = paramArrayOfInt[3];
/*     */ 
/* 219 */     WritableRaster localWritableRaster = (WritableRaster)paramSurfaceData.getRaster(Math.min(i, k), Math.min(j, m), Math.abs(i - k) + 1, Math.abs(j - m) + 1);
/*     */ 
/* 222 */     paramPixelWriter.setRaster(localWritableRaster);
/*     */ 
/* 225 */     if (i == k) {
/* 226 */       if (j > m)
/*     */         do {
/* 228 */           paramPixelWriter.writePixel(i, j);
/* 229 */           j--;
/* 230 */         }while (j >= m);
/*     */       else
/*     */         do {
/* 233 */           paramPixelWriter.writePixel(i, j);
/* 234 */           j++;
/* 235 */         }while (j <= m);
/*     */     }
/* 237 */     else if (j == m) {
/* 238 */       if (i > k)
/*     */         do {
/* 240 */           paramPixelWriter.writePixel(i, j);
/* 241 */           i--;
/* 242 */         }while (i >= k);
/*     */       else
/*     */         do {
/* 245 */           paramPixelWriter.writePixel(i, j);
/* 246 */           i++;
/* 247 */         }while (i <= k);
/*     */     }
/*     */     else {
/* 250 */       int n = paramArrayOfInt[4];
/* 251 */       int i1 = paramArrayOfInt[5];
/* 252 */       int i2 = paramArrayOfInt[6];
/* 253 */       int i3 = paramArrayOfInt[7];
/*     */       int i10;
/*     */       int i8;
/*     */       int i7;
/*     */       int i5;
/*     */       int i6;
/*     */       int i4;
/* 262 */       if (i2 >= i3)
/*     */       {
/* 264 */         i10 = 1;
/* 265 */         i8 = i3 * 2;
/* 266 */         i7 = i2 * 2;
/* 267 */         i5 = n < 0 ? -1 : 1;
/* 268 */         i6 = i1 < 0 ? -1 : 1;
/* 269 */         i2 = -i2;
/* 270 */         i4 = k - i;
/*     */       }
/*     */       else {
/* 273 */         i10 = 0;
/* 274 */         i8 = i2 * 2;
/* 275 */         i7 = i3 * 2;
/* 276 */         i5 = i1 < 0 ? -1 : 1;
/* 277 */         i6 = n < 0 ? -1 : 1;
/* 278 */         i3 = -i3;
/* 279 */         i4 = m - j;
/*     */       }
/* 281 */       int i9 = -(i7 / 2);
/*     */       int i11;
/* 282 */       if (j != paramInt2) {
/* 283 */         i11 = j - paramInt2;
/* 284 */         if (i11 < 0) {
/* 285 */           i11 = -i11;
/*     */         }
/* 287 */         i9 += i11 * i2 * 2;
/*     */       }
/* 289 */       if (i != paramInt1) {
/* 290 */         i11 = i - paramInt1;
/* 291 */         if (i11 < 0) {
/* 292 */           i11 = -i11;
/*     */         }
/* 294 */         i9 += i11 * i3 * 2;
/*     */       }
/* 296 */       if (i4 < 0) {
/* 297 */         i4 = -i4;
/*     */       }
/* 299 */       if (i10 != 0)
/*     */         do {
/* 301 */           paramPixelWriter.writePixel(i, j);
/* 302 */           i += i5;
/* 303 */           i9 += i8;
/* 304 */           if (i9 >= 0) {
/* 305 */             j += i6;
/* 306 */             i9 -= i7;
/*     */           }
/* 308 */           i4--; } while (i4 >= 0);
/*     */       else {
/*     */         do {
/* 311 */           paramPixelWriter.writePixel(i, j);
/* 312 */           j += i5;
/* 313 */           i9 += i8;
/* 314 */           if (i9 >= 0) {
/* 315 */             i += i6;
/* 316 */             i9 -= i7;
/*     */           }
/* 318 */           i4--; } while (i4 >= 0);
/*     */       }
/*     */     }
/* 321 */     return paramArrayOfInt;
/*     */   }
/*     */ 
/*     */   public static void doDrawRect(PixelWriter paramPixelWriter, SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 328 */     if ((paramInt3 < 0) || (paramInt4 < 0)) {
/* 329 */       return;
/*     */     }
/* 331 */     int i = Region.dimAdd(Region.dimAdd(paramInt1, paramInt3), 1);
/* 332 */     int j = Region.dimAdd(Region.dimAdd(paramInt2, paramInt4), 1);
/* 333 */     Region localRegion = paramSunGraphics2D.getCompClip().getBoundsIntersectionXYXY(paramInt1, paramInt2, i, j);
/* 334 */     if (localRegion.isEmpty()) {
/* 335 */       return;
/*     */     }
/* 337 */     int k = localRegion.getLoX();
/* 338 */     int m = localRegion.getLoY();
/* 339 */     int n = localRegion.getHiX();
/* 340 */     int i1 = localRegion.getHiY();
/*     */ 
/* 342 */     if ((paramInt3 < 2) || (paramInt4 < 2)) {
/* 343 */       doSetRect(paramSurfaceData, paramPixelWriter, k, m, n, i1);
/* 344 */       return;
/*     */     }
/*     */ 
/* 348 */     if (m == paramInt2) {
/* 349 */       doSetRect(paramSurfaceData, paramPixelWriter, k, m, n, m + 1);
/*     */     }
/* 351 */     if (k == paramInt1) {
/* 352 */       doSetRect(paramSurfaceData, paramPixelWriter, k, m + 1, k + 1, i1 - 1);
/*     */     }
/* 354 */     if (n == i) {
/* 355 */       doSetRect(paramSurfaceData, paramPixelWriter, n - 1, m + 1, n, i1 - 1);
/*     */     }
/* 357 */     if (i1 == j)
/* 358 */       doSetRect(paramSurfaceData, paramPixelWriter, k, i1 - 1, n, i1);
/*     */   }
/*     */ 
/*     */   static void doDrawGlyphList(SurfaceData paramSurfaceData, PixelWriter paramPixelWriter, GlyphList paramGlyphList, Region paramRegion)
/*     */   {
/* 372 */     int[] arrayOfInt1 = paramGlyphList.getBounds();
/* 373 */     paramRegion.clipBoxToBounds(arrayOfInt1);
/* 374 */     int i = arrayOfInt1[0];
/* 375 */     int j = arrayOfInt1[1];
/* 376 */     int k = arrayOfInt1[2];
/* 377 */     int m = arrayOfInt1[3];
/*     */ 
/* 379 */     WritableRaster localWritableRaster = (WritableRaster)paramSurfaceData.getRaster(i, j, k - i, m - j);
/*     */ 
/* 381 */     paramPixelWriter.setRaster(localWritableRaster);
/*     */ 
/* 383 */     int n = paramGlyphList.getNumGlyphs();
/* 384 */     for (int i1 = 0; i1 < n; i1++) {
/* 385 */       paramGlyphList.setGlyphIndex(i1);
/* 386 */       int[] arrayOfInt2 = paramGlyphList.getMetrics();
/* 387 */       int i2 = arrayOfInt2[0];
/* 388 */       int i3 = arrayOfInt2[1];
/* 389 */       int i4 = arrayOfInt2[2];
/* 390 */       int i5 = i2 + i4;
/* 391 */       int i6 = i3 + arrayOfInt2[3];
/* 392 */       int i7 = 0;
/* 393 */       if (i2 < i) {
/* 394 */         i7 = i - i2;
/* 395 */         i2 = i;
/*     */       }
/* 397 */       if (i3 < j) {
/* 398 */         i7 += (j - i3) * i4;
/* 399 */         i3 = j;
/*     */       }
/* 401 */       if (i5 > k) i5 = k;
/* 402 */       if (i6 > m) i6 = m;
/* 403 */       if ((i5 > i2) && (i6 > i3)) {
/* 404 */         byte[] arrayOfByte = paramGlyphList.getGrayBits();
/* 405 */         i4 -= i5 - i2;
/* 406 */         for (int i8 = i3; i8 < i6; i8++) {
/* 407 */           for (int i9 = i2; i9 < i5; i9++) {
/* 408 */             if (arrayOfByte[(i7++)] < 0) {
/* 409 */               paramPixelWriter.writePixel(i9, i8);
/*     */             }
/*     */           }
/* 412 */           i7 += i4;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static int outcode(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*     */     int i;
/* 425 */     if (paramInt2 < paramInt4)
/* 426 */       i = 1;
/* 427 */     else if (paramInt2 > paramInt6)
/* 428 */       i = 2;
/*     */     else {
/* 430 */       i = 0;
/*     */     }
/* 432 */     if (paramInt1 < paramInt3)
/* 433 */       i |= 4;
/* 434 */     else if (paramInt1 > paramInt5) {
/* 435 */       i |= 8;
/*     */     }
/* 437 */     return i;
/*     */   }
/*     */ 
/*     */   public static boolean adjustLine(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 443 */     int i = paramInt3 - 1;
/* 444 */     int j = paramInt4 - 1;
/* 445 */     int k = paramArrayOfInt[0];
/* 446 */     int m = paramArrayOfInt[1];
/* 447 */     int n = paramArrayOfInt[2];
/* 448 */     int i1 = paramArrayOfInt[3];
/*     */ 
/* 450 */     if ((i < paramInt1) || (j < paramInt2))
/* 451 */       return false;
/*     */     int i2;
/* 454 */     if (k == n) {
/* 455 */       if ((k < paramInt1) || (k > i)) {
/* 456 */         return false;
/*     */       }
/* 458 */       if (m > i1) {
/* 459 */         i2 = m;
/* 460 */         m = i1;
/* 461 */         i1 = i2;
/*     */       }
/* 463 */       if (m < paramInt2) {
/* 464 */         m = paramInt2;
/*     */       }
/* 466 */       if (i1 > j) {
/* 467 */         i1 = j;
/*     */       }
/* 469 */       if (m > i1) {
/* 470 */         return false;
/*     */       }
/* 472 */       paramArrayOfInt[1] = m;
/* 473 */       paramArrayOfInt[3] = i1;
/* 474 */     } else if (m == i1) {
/* 475 */       if ((m < paramInt2) || (m > j)) {
/* 476 */         return false;
/*     */       }
/* 478 */       if (k > n) {
/* 479 */         i2 = k;
/* 480 */         k = n;
/* 481 */         n = i2;
/*     */       }
/* 483 */       if (k < paramInt1) {
/* 484 */         k = paramInt1;
/*     */       }
/* 486 */       if (n > i) {
/* 487 */         n = i;
/*     */       }
/* 489 */       if (k > n) {
/* 490 */         return false;
/*     */       }
/* 492 */       paramArrayOfInt[0] = k;
/* 493 */       paramArrayOfInt[2] = n;
/*     */     }
/*     */     else
/*     */     {
/* 497 */       int i4 = n - k;
/* 498 */       int i5 = i1 - m;
/* 499 */       int i6 = i4 < 0 ? -i4 : i4;
/* 500 */       int i7 = i5 < 0 ? -i5 : i5;
/* 501 */       int i8 = i6 >= i7 ? 1 : 0;
/*     */ 
/* 503 */       i2 = outcode(k, m, paramInt1, paramInt2, i, j);
/* 504 */       int i3 = outcode(n, i1, paramInt1, paramInt2, i, j);
/* 505 */       while ((i2 | i3) != 0)
/*     */       {
/* 507 */         if ((i2 & i3) != 0)
/* 508 */           return false;
/*     */         int i10;
/*     */         int i9;
/* 510 */         if (i2 != 0) {
/* 511 */           if (0 != (i2 & 0x3)) {
/* 512 */             if (0 != (i2 & 0x1))
/* 513 */               m = paramInt2;
/*     */             else {
/* 515 */               m = j;
/*     */             }
/* 517 */             i10 = m - paramArrayOfInt[1];
/* 518 */             if (i10 < 0) {
/* 519 */               i10 = -i10;
/*     */             }
/* 521 */             i9 = 2 * i10 * i6 + i7;
/* 522 */             if (i8 != 0) {
/* 523 */               i9 += i7 - i6 - 1;
/*     */             }
/* 525 */             i9 /= 2 * i7;
/* 526 */             if (i4 < 0) {
/* 527 */               i9 = -i9;
/*     */             }
/* 529 */             k = paramArrayOfInt[0] + i9;
/* 530 */           } else if (0 != (i2 & 0xC))
/*     */           {
/* 532 */             if (0 != (i2 & 0x4))
/* 533 */               k = paramInt1;
/*     */             else {
/* 535 */               k = i;
/*     */             }
/* 537 */             i9 = k - paramArrayOfInt[0];
/* 538 */             if (i9 < 0) {
/* 539 */               i9 = -i9;
/*     */             }
/* 541 */             i10 = 2 * i9 * i7 + i6;
/* 542 */             if (i8 == 0) {
/* 543 */               i10 += i6 - i7 - 1;
/*     */             }
/* 545 */             i10 /= 2 * i6;
/* 546 */             if (i5 < 0) {
/* 547 */               i10 = -i10;
/*     */             }
/* 549 */             m = paramArrayOfInt[1] + i10;
/*     */           }
/* 551 */           i2 = outcode(k, m, paramInt1, paramInt2, i, j);
/*     */         } else {
/* 553 */           if (0 != (i3 & 0x3)) {
/* 554 */             if (0 != (i3 & 0x1))
/* 555 */               i1 = paramInt2;
/*     */             else {
/* 557 */               i1 = j;
/*     */             }
/* 559 */             i10 = i1 - paramArrayOfInt[3];
/* 560 */             if (i10 < 0) {
/* 561 */               i10 = -i10;
/*     */             }
/* 563 */             i9 = 2 * i10 * i6 + i7;
/* 564 */             if (i8 != 0)
/* 565 */               i9 += i7 - i6;
/*     */             else {
/* 567 */               i9--;
/*     */             }
/* 569 */             i9 /= 2 * i7;
/* 570 */             if (i4 > 0) {
/* 571 */               i9 = -i9;
/*     */             }
/* 573 */             n = paramArrayOfInt[2] + i9;
/* 574 */           } else if (0 != (i3 & 0xC))
/*     */           {
/* 576 */             if (0 != (i3 & 0x4))
/* 577 */               n = paramInt1;
/*     */             else {
/* 579 */               n = i;
/*     */             }
/* 581 */             i9 = n - paramArrayOfInt[2];
/* 582 */             if (i9 < 0) {
/* 583 */               i9 = -i9;
/*     */             }
/* 585 */             i10 = 2 * i9 * i7 + i6;
/* 586 */             if (i8 != 0)
/* 587 */               i10--;
/*     */             else {
/* 589 */               i10 += i6 - i7;
/*     */             }
/* 591 */             i10 /= 2 * i6;
/* 592 */             if (i5 > 0) {
/* 593 */               i10 = -i10;
/*     */             }
/* 595 */             i1 = paramArrayOfInt[3] + i10;
/*     */           }
/* 597 */           i3 = outcode(n, i1, paramInt1, paramInt2, i, j);
/*     */         }
/*     */       }
/* 600 */       paramArrayOfInt[0] = k;
/* 601 */       paramArrayOfInt[1] = m;
/* 602 */       paramArrayOfInt[2] = n;
/* 603 */       paramArrayOfInt[3] = i1;
/* 604 */       paramArrayOfInt[4] = i4;
/* 605 */       paramArrayOfInt[5] = i5;
/* 606 */       paramArrayOfInt[6] = i6;
/* 607 */       paramArrayOfInt[7] = i7;
/*     */     }
/* 609 */     return true;
/*     */   }
/*     */ 
/*     */   static PixelWriter createSolidPixelWriter(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData)
/*     */   {
/* 615 */     ColorModel localColorModel = paramSurfaceData.getColorModel();
/* 616 */     Object localObject = localColorModel.getDataElements(paramSunGraphics2D.eargb, null);
/*     */ 
/* 618 */     return new SolidPixelWriter(localObject);
/*     */   }
/*     */ 
/*     */   static PixelWriter createXorPixelWriter(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData)
/*     */   {
/* 624 */     ColorModel localColorModel = paramSurfaceData.getColorModel();
/*     */ 
/* 626 */     Object localObject1 = localColorModel.getDataElements(paramSunGraphics2D.eargb, null);
/*     */ 
/* 628 */     XORComposite localXORComposite = (XORComposite)paramSunGraphics2D.getComposite();
/* 629 */     int i = localXORComposite.getXorColor().getRGB();
/* 630 */     Object localObject2 = localColorModel.getDataElements(i, null);
/*     */ 
/* 632 */     switch (localColorModel.getTransferType()) {
/*     */     case 0:
/* 634 */       return new XorPixelWriter.ByteData(localObject1, localObject2);
/*     */     case 1:
/*     */     case 2:
/* 637 */       return new XorPixelWriter.ShortData(localObject1, localObject2);
/*     */     case 3:
/* 639 */       return new XorPixelWriter.IntData(localObject1, localObject2);
/*     */     case 4:
/* 641 */       return new XorPixelWriter.FloatData(localObject1, localObject2);
/*     */     case 5:
/* 643 */       return new XorPixelWriter.DoubleData(localObject1, localObject2);
/*     */     }
/* 645 */     throw new InternalError("Unsupported XOR pixel type");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.GeneralRenderer
 * JD-Core Version:    0.6.2
 */