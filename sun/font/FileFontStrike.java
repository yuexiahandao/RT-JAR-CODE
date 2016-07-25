/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.NoninvertibleTransformException;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class FileFontStrike extends PhysicalStrike
/*     */ {
/*     */   static final int INVISIBLE_GLYPHS = 65534;
/*     */   private FileFont fileFont;
/*     */   private static final int UNINITIALISED = 0;
/*     */   private static final int INTARRAY = 1;
/*     */   private static final int LONGARRAY = 2;
/*     */   private static final int SEGINTARRAY = 3;
/*     */   private static final int SEGLONGARRAY = 4;
/*  61 */   private volatile int glyphCacheFormat = 0;
/*     */   private static final int SEGSHIFT = 5;
/*     */   private static final int SEGSIZE = 32;
/*     */   private boolean segmentedCache;
/*     */   private int[][] segIntGlyphImages;
/*     */   private long[][] segLongGlyphImages;
/*     */   private float[] horizontalAdvances;
/*     */   private float[][] segHorizontalAdvances;
/*     */   ConcurrentHashMap<Integer, Rectangle2D.Float> boundsMap;
/*     */   SoftReference<ConcurrentHashMap<Integer, Point2D.Float>> glyphMetricsMapRef;
/*     */   AffineTransform invertDevTx;
/*     */   boolean useNatives;
/*     */   NativeStrike[] nativeStrikes;
/*     */   private int intPtSize;
/* 115 */   private static boolean isXPorLater = false;
/*     */   private static final int SLOTZEROMAX = 16777215;
/*     */   private WeakReference<ConcurrentHashMap<Integer, GeneralPath>> outlineMapRef;
/*     */ 
/*     */   private static native boolean initNative();
/*     */ 
/*     */   FileFontStrike(FileFont paramFileFont, FontStrikeDesc paramFontStrikeDesc)
/*     */   {
/* 124 */     super(paramFileFont, paramFontStrikeDesc);
/* 125 */     this.fileFont = paramFileFont;
/*     */ 
/* 127 */     if (paramFontStrikeDesc.style != paramFileFont.style)
/*     */     {
/* 132 */       if (((paramFontStrikeDesc.style & 0x2) == 2) && ((paramFileFont.style & 0x2) == 0))
/*     */       {
/* 134 */         this.algoStyle = true;
/* 135 */         this.italic = 0.7F;
/*     */       }
/* 137 */       if (((paramFontStrikeDesc.style & 0x1) == 1) && ((paramFileFont.style & 0x1) == 0))
/*     */       {
/* 139 */         this.algoStyle = true;
/* 140 */         this.boldness = 1.33F;
/*     */       }
/*     */     }
/* 143 */     double[] arrayOfDouble = new double[4];
/* 144 */     AffineTransform localAffineTransform = paramFontStrikeDesc.glyphTx;
/* 145 */     localAffineTransform.getMatrix(arrayOfDouble);
/* 146 */     if ((!paramFontStrikeDesc.devTx.isIdentity()) && (paramFontStrikeDesc.devTx.getType() != 1)) {
/*     */       try
/*     */       {
/* 149 */         this.invertDevTx = paramFontStrikeDesc.devTx.createInverse();
/*     */       }
/*     */       catch (NoninvertibleTransformException localNoninvertibleTransformException)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 168 */     boolean bool = (paramFontStrikeDesc.aaHint != 1) && (paramFileFont.familyName.startsWith("Amble"));
/*     */ 
/* 177 */     if ((Double.isNaN(arrayOfDouble[0])) || (Double.isNaN(arrayOfDouble[1])) || (Double.isNaN(arrayOfDouble[2])) || (Double.isNaN(arrayOfDouble[3])) || (paramFileFont.getScaler() == null))
/*     */     {
/* 180 */       this.pScalerContext = NullFontScaler.getNullScalerContext();
/*     */     }
/* 182 */     else this.pScalerContext = paramFileFont.getScaler().createScalerContext(arrayOfDouble, paramFontStrikeDesc.aaHint, paramFontStrikeDesc.fmHint, this.boldness, this.italic, bool);
/*     */ 
/* 187 */     this.mapper = paramFileFont.getMapper();
/* 188 */     int i = this.mapper.getNumGlyphs();
/*     */ 
/* 198 */     float f = (float)arrayOfDouble[3];
/* 199 */     int j = this.intPtSize = (int)f;
/* 200 */     int k = (localAffineTransform.getType() & 0x7C) == 0 ? 1 : 0;
/* 201 */     this.segmentedCache = ((i > 256) || ((i > 64) && ((k == 0) || (f != j) || (j < 6) || (j > 36))));
/*     */ 
/* 213 */     if (this.pScalerContext == 0L)
/*     */     {
/* 217 */       this.disposer = new FontStrikeDisposer(paramFileFont, paramFontStrikeDesc);
/* 218 */       initGlyphCache();
/* 219 */       this.pScalerContext = NullFontScaler.getNullScalerContext();
/* 220 */       SunFontManager.getInstance().deRegisterBadFont(paramFileFont);
/* 221 */       return;
/*     */     }
/*     */ 
/* 230 */     if ((FontUtilities.isWindows) && (isXPorLater) && (!FontUtilities.useT2K) && (!GraphicsEnvironment.isHeadless()) && (!paramFileFont.useJavaRasterizer) && ((paramFontStrikeDesc.aaHint == 4) || (paramFontStrikeDesc.aaHint == 5)) && (arrayOfDouble[1] == 0.0D) && (arrayOfDouble[2] == 0.0D) && (arrayOfDouble[0] == arrayOfDouble[3]) && (arrayOfDouble[0] >= 3.0D) && (arrayOfDouble[0] <= 100.0D) && (!((TrueTypeFont)paramFileFont).useEmbeddedBitmapsForSize(this.intPtSize)))
/*     */     {
/* 240 */       this.useNatives = true;
/*     */     }
/* 242 */     else if ((paramFileFont.checkUseNatives()) && (paramFontStrikeDesc.aaHint == 0) && (!this.algoStyle))
/*     */     {
/* 245 */       if ((arrayOfDouble[1] == 0.0D) && (arrayOfDouble[2] == 0.0D) && (arrayOfDouble[0] >= 6.0D) && (arrayOfDouble[0] <= 36.0D) && (arrayOfDouble[0] == arrayOfDouble[3]))
/*     */       {
/* 248 */         this.useNatives = true;
/* 249 */         int m = paramFileFont.nativeFonts.length;
/* 250 */         this.nativeStrikes = new NativeStrike[m];
/*     */ 
/* 254 */         for (int n = 0; n < m; n++) {
/* 255 */           this.nativeStrikes[n] = new NativeStrike(paramFileFont.nativeFonts[n], paramFontStrikeDesc, false);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 260 */     if ((FontUtilities.isLogging()) && (FontUtilities.isWindows)) {
/* 261 */       FontUtilities.getLogger().info("Strike for " + paramFileFont + " at size = " + this.intPtSize + " use natives = " + this.useNatives + " useJavaRasteriser = " + paramFileFont.useJavaRasterizer + " AAHint = " + paramFontStrikeDesc.aaHint + " Has Embedded bitmaps = " + ((TrueTypeFont)paramFileFont).useEmbeddedBitmapsForSize(this.intPtSize));
/*     */     }
/*     */ 
/* 270 */     this.disposer = new FontStrikeDisposer(paramFileFont, paramFontStrikeDesc, this.pScalerContext);
/*     */ 
/* 278 */     double d = 48.0D;
/* 279 */     this.getImageWithAdvance = ((Math.abs(localAffineTransform.getScaleX()) <= d) && (Math.abs(localAffineTransform.getScaleY()) <= d) && (Math.abs(localAffineTransform.getShearX()) <= d) && (Math.abs(localAffineTransform.getShearY()) <= d));
/*     */ 
/* 294 */     if (!this.getImageWithAdvance)
/*     */     {
/*     */       int i1;
/* 295 */       if (!this.segmentedCache) {
/* 296 */         this.horizontalAdvances = new float[i];
/*     */ 
/* 298 */         for (i1 = 0; i1 < i; i1++)
/* 299 */           this.horizontalAdvances[i1] = 3.4028235E+38F;
/*     */       }
/*     */       else {
/* 302 */         i1 = (i + 32 - 1) / 32;
/* 303 */         this.segHorizontalAdvances = new float[i1][];
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs()
/*     */   {
/* 313 */     return this.fileFont.getNumGlyphs();
/*     */   }
/*     */ 
/*     */   long getGlyphImageFromNative(int paramInt) {
/* 317 */     if (FontUtilities.isWindows) {
/* 318 */       return getGlyphImageFromWindows(paramInt);
/*     */     }
/* 320 */     return getGlyphImageFromX11(paramInt);
/*     */   }
/*     */ 
/*     */   private native long _getGlyphImageFromWindows(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
/*     */ 
/*     */   long getGlyphImageFromWindows(int paramInt)
/*     */   {
/* 334 */     String str = this.fileFont.getFamilyName(null);
/* 335 */     int i = this.desc.style & 0x1 | this.desc.style & 0x2 | this.fileFont.getStyle();
/*     */ 
/* 337 */     int j = this.intPtSize;
/* 338 */     long l = _getGlyphImageFromWindows(str, i, j, paramInt, this.desc.fmHint == 2);
/*     */ 
/* 341 */     if (l != 0L)
/*     */     {
/* 349 */       float f = getGlyphAdvance(paramInt, false);
/* 350 */       StrikeCache.unsafe.putFloat(l + StrikeCache.xAdvanceOffset, f);
/*     */ 
/* 352 */       return l;
/*     */     }
/* 354 */     return this.fileFont.getGlyphImage(this.pScalerContext, paramInt);
/*     */   }
/*     */ 
/*     */   long getGlyphImageFromX11(int paramInt)
/*     */   {
/* 361 */     char c = this.fileFont.glyphToCharMap[paramInt];
/* 362 */     for (int i = 0; i < this.nativeStrikes.length; i++) {
/* 363 */       CharToGlyphMapper localCharToGlyphMapper = this.fileFont.nativeFonts[i].getMapper();
/* 364 */       int j = localCharToGlyphMapper.charToGlyph(c) & 0xFFFF;
/* 365 */       if (j != localCharToGlyphMapper.getMissingGlyphCode()) {
/* 366 */         long l = this.nativeStrikes[i].getGlyphImagePtrNoCache(j);
/* 367 */         if (l != 0L) {
/* 368 */           return l;
/*     */         }
/*     */       }
/*     */     }
/* 372 */     return this.fileFont.getGlyphImage(this.pScalerContext, paramInt);
/*     */   }
/*     */ 
/*     */   long getGlyphImagePtr(int paramInt) {
/* 376 */     if (paramInt >= 65534) {
/* 377 */       return StrikeCache.invisibleGlyphPtr;
/*     */     }
/* 379 */     long l = 0L;
/* 380 */     if ((l = getCachedGlyphPtr(paramInt)) != 0L) {
/* 381 */       return l;
/*     */     }
/* 383 */     if (this.useNatives) {
/* 384 */       l = getGlyphImageFromNative(paramInt);
/* 385 */       if ((l == 0L) && (FontUtilities.isLogging())) {
/* 386 */         FontUtilities.getLogger().info("Strike for " + this.fileFont + " at size = " + this.intPtSize + " couldn't get native glyph for code = " + paramInt);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 391 */     if (l == 0L) {
/* 392 */       l = this.fileFont.getGlyphImage(this.pScalerContext, paramInt);
/*     */     }
/*     */ 
/* 395 */     return setCachedGlyphPtr(paramInt, l);
/*     */   }
/*     */ 
/*     */   void getGlyphImagePtrs(int[] paramArrayOfInt, long[] paramArrayOfLong, int paramInt)
/*     */   {
/* 401 */     for (int i = 0; i < paramInt; i++) {
/* 402 */       int j = paramArrayOfInt[i];
/* 403 */       if (j >= 65534) {
/* 404 */         paramArrayOfLong[i] = StrikeCache.invisibleGlyphPtr;
/*     */       }
/* 406 */       else if ((paramArrayOfLong[i] = getCachedGlyphPtr(j)) == 0L)
/*     */       {
/* 409 */         long l = 0L;
/* 410 */         if (this.useNatives)
/* 411 */           l = getGlyphImageFromNative(j);
/* 412 */         if (l == 0L) {
/* 413 */           l = this.fileFont.getGlyphImage(this.pScalerContext, j);
/*     */         }
/*     */ 
/* 416 */         paramArrayOfLong[i] = setCachedGlyphPtr(j, l);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   int getSlot0GlyphImagePtrs(int[] paramArrayOfInt, long[] paramArrayOfLong, int paramInt)
/*     */   {
/* 426 */     int i = 0;
/*     */ 
/* 428 */     for (int j = 0; j < paramInt; j++) {
/* 429 */       int k = paramArrayOfInt[j];
/* 430 */       if (k >= 16777215) {
/* 431 */         return i;
/*     */       }
/* 433 */       i++;
/*     */ 
/* 435 */       if (k >= 65534) {
/* 436 */         paramArrayOfLong[j] = StrikeCache.invisibleGlyphPtr;
/*     */       }
/* 438 */       else if ((paramArrayOfLong[j] = getCachedGlyphPtr(k)) == 0L)
/*     */       {
/* 441 */         long l = 0L;
/* 442 */         if (this.useNatives) {
/* 443 */           l = getGlyphImageFromNative(k);
/*     */         }
/* 445 */         if (l == 0L) {
/* 446 */           l = this.fileFont.getGlyphImage(this.pScalerContext, k);
/*     */         }
/*     */ 
/* 449 */         paramArrayOfLong[j] = setCachedGlyphPtr(k, l);
/*     */       }
/*     */     }
/* 452 */     return i;
/*     */   }
/*     */ 
/*     */   long getCachedGlyphPtr(int paramInt)
/*     */   {
/*     */     int i;
/*     */     int j;
/* 457 */     switch (this.glyphCacheFormat) {
/*     */     case 1:
/* 459 */       return this.intGlyphImages[paramInt] & 0xFFFFFFFF;
/*     */     case 3:
/* 461 */       i = paramInt >> 5;
/* 462 */       if (this.segIntGlyphImages[i] != null) {
/* 463 */         j = paramInt % 32;
/* 464 */         return this.segIntGlyphImages[i][j] & 0xFFFFFFFF;
/*     */       }
/* 466 */       return 0L;
/*     */     case 2:
/* 469 */       return this.longGlyphImages[paramInt];
/*     */     case 4:
/* 471 */       i = paramInt >> 5;
/* 472 */       if (this.segLongGlyphImages[i] != null) {
/* 473 */         j = paramInt % 32;
/* 474 */         return this.segLongGlyphImages[i][j];
/*     */       }
/* 476 */       return 0L;
/*     */     }
/*     */ 
/* 480 */     return 0L;
/*     */   }
/*     */ 
/*     */   private synchronized long setCachedGlyphPtr(int paramInt, long paramLong)
/*     */   {
/*     */     int i;
/*     */     int j;
/* 484 */     switch (this.glyphCacheFormat) {
/*     */     case 1:
/* 486 */       if (this.intGlyphImages[paramInt] == 0) {
/* 487 */         this.intGlyphImages[paramInt] = ((int)paramLong);
/* 488 */         return paramLong;
/*     */       }
/* 490 */       StrikeCache.freeIntPointer((int)paramLong);
/* 491 */       return this.intGlyphImages[paramInt] & 0xFFFFFFFF;
/*     */     case 3:
/* 495 */       i = paramInt >> 5;
/* 496 */       j = paramInt % 32;
/* 497 */       if (this.segIntGlyphImages[i] == null) {
/* 498 */         this.segIntGlyphImages[i] = new int[32];
/*     */       }
/* 500 */       if (this.segIntGlyphImages[i][j] == 0) {
/* 501 */         this.segIntGlyphImages[i][j] = ((int)paramLong);
/* 502 */         return paramLong;
/*     */       }
/* 504 */       StrikeCache.freeIntPointer((int)paramLong);
/* 505 */       return this.segIntGlyphImages[i][j] & 0xFFFFFFFF;
/*     */     case 2:
/* 509 */       if (this.longGlyphImages[paramInt] == 0L) {
/* 510 */         this.longGlyphImages[paramInt] = paramLong;
/* 511 */         return paramLong;
/*     */       }
/* 513 */       StrikeCache.freeLongPointer(paramLong);
/* 514 */       return this.longGlyphImages[paramInt];
/*     */     case 4:
/* 518 */       i = paramInt >> 5;
/* 519 */       j = paramInt % 32;
/* 520 */       if (this.segLongGlyphImages[i] == null) {
/* 521 */         this.segLongGlyphImages[i] = new long[32];
/*     */       }
/* 523 */       if (this.segLongGlyphImages[i][j] == 0L) {
/* 524 */         this.segLongGlyphImages[i][j] = paramLong;
/* 525 */         return paramLong;
/*     */       }
/* 527 */       StrikeCache.freeLongPointer(paramLong);
/* 528 */       return this.segLongGlyphImages[i][j];
/*     */     }
/*     */ 
/* 536 */     initGlyphCache();
/* 537 */     return setCachedGlyphPtr(paramInt, paramLong);
/*     */   }
/*     */ 
/*     */   private synchronized void initGlyphCache()
/*     */   {
/* 543 */     int i = this.mapper.getNumGlyphs();
/* 544 */     int j = 0;
/* 545 */     if (this.segmentedCache) {
/* 546 */       int k = (i + 32 - 1) / 32;
/* 547 */       if (longAddresses) {
/* 548 */         j = 4;
/* 549 */         this.segLongGlyphImages = new long[k][];
/* 550 */         this.disposer.segLongGlyphImages = this.segLongGlyphImages;
/*     */       } else {
/* 552 */         j = 3;
/* 553 */         this.segIntGlyphImages = new int[k][];
/* 554 */         this.disposer.segIntGlyphImages = this.segIntGlyphImages;
/*     */       }
/*     */     }
/* 557 */     else if (longAddresses) {
/* 558 */       j = 2;
/* 559 */       this.longGlyphImages = new long[i];
/* 560 */       this.disposer.longGlyphImages = this.longGlyphImages;
/*     */     } else {
/* 562 */       j = 1;
/* 563 */       this.intGlyphImages = new int[i];
/* 564 */       this.disposer.intGlyphImages = this.intGlyphImages;
/*     */     }
/*     */ 
/* 567 */     this.glyphCacheFormat = j;
/*     */   }
/*     */ 
/*     */   float getGlyphAdvance(int paramInt) {
/* 571 */     return getGlyphAdvance(paramInt, true);
/*     */   }
/*     */ 
/*     */   private float getGlyphAdvance(int paramInt, boolean paramBoolean)
/*     */   {
/* 582 */     if (paramInt >= 65534)
/* 583 */       return 0.0F;
/*     */     float f;
/* 613 */     if (this.horizontalAdvances != null) {
/* 614 */       f = this.horizontalAdvances[paramInt];
/* 615 */       if (f != 3.4028235E+38F) {
/* 616 */         if ((!paramBoolean) && (this.invertDevTx != null)) {
/* 617 */           Point2D.Float localFloat1 = new Point2D.Float(f, 0.0F);
/* 618 */           this.desc.devTx.deltaTransform(localFloat1, localFloat1);
/* 619 */           return localFloat1.x;
/*     */         }
/* 621 */         return f;
/*     */       }
/*     */     }
/* 624 */     else if ((this.segmentedCache) && (this.segHorizontalAdvances != null)) {
/* 625 */       int i = paramInt >> 5;
/* 626 */       float[] arrayOfFloat = this.segHorizontalAdvances[i];
/* 627 */       if (arrayOfFloat != null) {
/* 628 */         f = arrayOfFloat[(paramInt % 32)];
/* 629 */         if (f != 3.4028235E+38F) {
/* 630 */           if ((!paramBoolean) && (this.invertDevTx != null)) {
/* 631 */             Point2D.Float localFloat3 = new Point2D.Float(f, 0.0F);
/* 632 */             this.desc.devTx.deltaTransform(localFloat3, localFloat3);
/* 633 */             return localFloat3.x;
/*     */           }
/* 635 */           return f;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 641 */     if ((!paramBoolean) && (this.invertDevTx != null)) {
/* 642 */       Point2D.Float localFloat2 = new Point2D.Float();
/* 643 */       this.fileFont.getGlyphMetrics(this.pScalerContext, paramInt, localFloat2);
/* 644 */       return localFloat2.x;
/*     */     }
/*     */ 
/* 647 */     if ((this.invertDevTx != null) || (!paramBoolean))
/*     */     {
/* 651 */       f = getGlyphMetrics(paramInt, paramBoolean).x;
/*     */     }
/*     */     else
/*     */     {
/*     */       long l;
/* 654 */       if (this.getImageWithAdvance)
/*     */       {
/* 660 */         l = getGlyphImagePtr(paramInt);
/*     */       }
/* 662 */       else l = getCachedGlyphPtr(paramInt);
/*     */ 
/* 664 */       if (l != 0L) {
/* 665 */         f = StrikeCache.unsafe.getFloat(l + StrikeCache.xAdvanceOffset);
/*     */       }
/*     */       else
/*     */       {
/* 669 */         f = this.fileFont.getGlyphAdvance(this.pScalerContext, paramInt);
/*     */       }
/*     */     }
/*     */ 
/* 673 */     if (this.horizontalAdvances != null) {
/* 674 */       this.horizontalAdvances[paramInt] = f;
/* 675 */     } else if ((this.segmentedCache) && (this.segHorizontalAdvances != null)) {
/* 676 */       int j = paramInt >> 5;
/* 677 */       int k = paramInt % 32;
/* 678 */       if (this.segHorizontalAdvances[j] == null) {
/* 679 */         this.segHorizontalAdvances[j] = new float[32];
/* 680 */         for (int m = 0; m < 32; m++) {
/* 681 */           this.segHorizontalAdvances[j][m] = 3.4028235E+38F;
/*     */         }
/*     */       }
/* 684 */       this.segHorizontalAdvances[j][k] = f;
/*     */     }
/* 686 */     return f;
/*     */   }
/*     */ 
/*     */   float getCodePointAdvance(int paramInt) {
/* 690 */     return getGlyphAdvance(this.mapper.charToGlyph(paramInt));
/*     */   }
/*     */ 
/*     */   void getGlyphImageBounds(int paramInt, Point2D.Float paramFloat, Rectangle paramRectangle)
/*     */   {
/* 699 */     long l = getGlyphImagePtr(paramInt);
/*     */ 
/* 705 */     if (l == 0L) {
/* 706 */       paramRectangle.x = ((int)Math.floor(paramFloat.x));
/* 707 */       paramRectangle.y = ((int)Math.floor(paramFloat.y));
/* 708 */       paramRectangle.width = (paramRectangle.height = 0);
/* 709 */       return;
/*     */     }
/*     */ 
/* 712 */     float f1 = StrikeCache.unsafe.getFloat(l + StrikeCache.topLeftXOffset);
/* 713 */     float f2 = StrikeCache.unsafe.getFloat(l + StrikeCache.topLeftYOffset);
/*     */ 
/* 715 */     paramRectangle.x = ((int)Math.floor(paramFloat.x + f1));
/* 716 */     paramRectangle.y = ((int)Math.floor(paramFloat.y + f2));
/* 717 */     paramRectangle.width = (StrikeCache.unsafe.getShort(l + StrikeCache.widthOffset) & 0xFFFF);
/*     */ 
/* 719 */     paramRectangle.height = (StrikeCache.unsafe.getShort(l + StrikeCache.heightOffset) & 0xFFFF);
/*     */ 
/* 728 */     if (((this.desc.aaHint == 4) || (this.desc.aaHint == 5)) && (f1 <= -2.0F))
/*     */     {
/* 731 */       int i = getGlyphImageMinX(l, paramRectangle.x);
/* 732 */       if (i > paramRectangle.x) {
/* 733 */         paramRectangle.x += 1;
/* 734 */         paramRectangle.width -= 1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getGlyphImageMinX(long paramLong, int paramInt)
/*     */   {
/* 741 */     int i = StrikeCache.unsafe.getChar(paramLong + StrikeCache.widthOffset);
/* 742 */     int j = StrikeCache.unsafe.getChar(paramLong + StrikeCache.heightOffset);
/* 743 */     int k = StrikeCache.unsafe.getChar(paramLong + StrikeCache.rowBytesOffset);
/*     */ 
/* 746 */     if (k == i) {
/* 747 */       return paramInt;
/*     */     }
/*     */ 
/* 750 */     long l = StrikeCache.unsafe.getAddress(paramLong + StrikeCache.pixelDataOffset);
/*     */ 
/* 753 */     if (l == 0L) {
/* 754 */       return paramInt;
/*     */     }
/*     */ 
/* 757 */     for (int m = 0; m < j; m++) {
/* 758 */       for (int n = 0; n < 3; n++) {
/* 759 */         if (StrikeCache.unsafe.getByte(l + m * k + n) != 0) {
/* 760 */           return paramInt;
/*     */         }
/*     */       }
/*     */     }
/* 764 */     return paramInt + 1;
/*     */   }
/*     */ 
/*     */   StrikeMetrics getFontMetrics()
/*     */   {
/* 771 */     if (this.strikeMetrics == null) {
/* 772 */       this.strikeMetrics = this.fileFont.getFontMetrics(this.pScalerContext);
/*     */ 
/* 774 */       if (this.invertDevTx != null) {
/* 775 */         this.strikeMetrics.convertToUserSpace(this.invertDevTx);
/*     */       }
/*     */     }
/* 778 */     return this.strikeMetrics;
/*     */   }
/*     */ 
/*     */   Point2D.Float getGlyphMetrics(int paramInt) {
/* 782 */     return getGlyphMetrics(paramInt, true);
/*     */   }
/*     */ 
/*     */   private Point2D.Float getGlyphMetrics(int paramInt, boolean paramBoolean) {
/* 786 */     Point2D.Float localFloat1 = new Point2D.Float();
/*     */ 
/* 789 */     if (paramInt >= 65534)
/* 790 */       return localFloat1;
/*     */     long l;
/* 793 */     if ((this.getImageWithAdvance) && (paramBoolean))
/*     */     {
/* 799 */       l = getGlyphImagePtr(paramInt);
/*     */     }
/* 801 */     else l = getCachedGlyphPtr(paramInt);
/*     */ 
/* 803 */     if (l != 0L) {
/* 804 */       localFloat1 = new Point2D.Float();
/* 805 */       localFloat1.x = StrikeCache.unsafe.getFloat(l + StrikeCache.xAdvanceOffset);
/*     */ 
/* 807 */       localFloat1.y = StrikeCache.unsafe.getFloat(l + StrikeCache.yAdvanceOffset);
/*     */ 
/* 812 */       if (this.invertDevTx != null) {
/* 813 */         this.invertDevTx.deltaTransform(localFloat1, localFloat1);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 823 */       Integer localInteger = Integer.valueOf(paramInt);
/* 824 */       Point2D.Float localFloat2 = null;
/* 825 */       ConcurrentHashMap localConcurrentHashMap = null;
/* 826 */       if (this.glyphMetricsMapRef != null) {
/* 827 */         localConcurrentHashMap = (ConcurrentHashMap)this.glyphMetricsMapRef.get();
/*     */       }
/* 829 */       if (localConcurrentHashMap != null) {
/* 830 */         localFloat2 = (Point2D.Float)localConcurrentHashMap.get(localInteger);
/* 831 */         if (localFloat2 != null) {
/* 832 */           localFloat1.x = localFloat2.x;
/* 833 */           localFloat1.y = localFloat2.y;
/*     */ 
/* 835 */           return localFloat1;
/*     */         }
/*     */       }
/* 838 */       if (localFloat2 == null) {
/* 839 */         this.fileFont.getGlyphMetrics(this.pScalerContext, paramInt, localFloat1);
/*     */ 
/* 843 */         if (this.invertDevTx != null) {
/* 844 */           this.invertDevTx.deltaTransform(localFloat1, localFloat1);
/*     */         }
/* 846 */         localFloat2 = new Point2D.Float(localFloat1.x, localFloat1.y);
/*     */ 
/* 850 */         if (localConcurrentHashMap == null) {
/* 851 */           localConcurrentHashMap = new ConcurrentHashMap();
/*     */ 
/* 853 */           this.glyphMetricsMapRef = new SoftReference(localConcurrentHashMap);
/*     */         }
/*     */ 
/* 857 */         localConcurrentHashMap.put(localInteger, localFloat2);
/*     */       }
/*     */     }
/* 860 */     return localFloat1;
/*     */   }
/*     */ 
/*     */   Point2D.Float getCharMetrics(char paramChar) {
/* 864 */     return getGlyphMetrics(this.mapper.charToGlyph(paramChar));
/*     */   }
/*     */ 
/*     */   Rectangle2D.Float getGlyphOutlineBounds(int paramInt)
/*     */   {
/* 886 */     if (this.boundsMap == null) {
/* 887 */       this.boundsMap = new ConcurrentHashMap();
/*     */     }
/*     */ 
/* 890 */     Integer localInteger = Integer.valueOf(paramInt);
/* 891 */     Rectangle2D.Float localFloat = (Rectangle2D.Float)this.boundsMap.get(localInteger);
/*     */ 
/* 893 */     if (localFloat == null) {
/* 894 */       localFloat = this.fileFont.getGlyphOutlineBounds(this.pScalerContext, paramInt);
/* 895 */       this.boundsMap.put(localInteger, localFloat);
/*     */     }
/* 897 */     return localFloat;
/*     */   }
/*     */ 
/*     */   public Rectangle2D getOutlineBounds(int paramInt) {
/* 901 */     return this.fileFont.getGlyphOutlineBounds(this.pScalerContext, paramInt);
/*     */   }
/*     */ 
/*     */   GeneralPath getGlyphOutline(int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/* 909 */     GeneralPath localGeneralPath = null;
/* 910 */     ConcurrentHashMap localConcurrentHashMap = null;
/*     */ 
/* 912 */     if (this.outlineMapRef != null) {
/* 913 */       localConcurrentHashMap = (ConcurrentHashMap)this.outlineMapRef.get();
/* 914 */       if (localConcurrentHashMap != null) {
/* 915 */         localGeneralPath = (GeneralPath)localConcurrentHashMap.get(Integer.valueOf(paramInt));
/*     */       }
/*     */     }
/*     */ 
/* 919 */     if (localGeneralPath == null) {
/* 920 */       localGeneralPath = this.fileFont.getGlyphOutline(this.pScalerContext, paramInt, 0.0F, 0.0F);
/* 921 */       if (localConcurrentHashMap == null) {
/* 922 */         localConcurrentHashMap = new ConcurrentHashMap();
/* 923 */         this.outlineMapRef = new WeakReference(localConcurrentHashMap);
/*     */       }
/*     */ 
/* 927 */       localConcurrentHashMap.put(Integer.valueOf(paramInt), localGeneralPath);
/*     */     }
/* 929 */     localGeneralPath = (GeneralPath)localGeneralPath.clone();
/* 930 */     if ((paramFloat1 != 0.0F) || (paramFloat2 != 0.0F)) {
/* 931 */       localGeneralPath.transform(AffineTransform.getTranslateInstance(paramFloat1, paramFloat2));
/*     */     }
/* 933 */     return localGeneralPath;
/*     */   }
/*     */ 
/*     */   GeneralPath getGlyphVectorOutline(int[] paramArrayOfInt, float paramFloat1, float paramFloat2) {
/* 937 */     return this.fileFont.getGlyphVectorOutline(this.pScalerContext, paramArrayOfInt, paramArrayOfInt.length, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   protected void adjustPoint(Point2D.Float paramFloat)
/*     */   {
/* 942 */     if (this.invertDevTx != null)
/* 943 */       this.invertDevTx.deltaTransform(paramFloat, paramFloat);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 117 */     if ((FontUtilities.isWindows) && (!FontUtilities.useT2K) && (!GraphicsEnvironment.isHeadless()))
/*     */     {
/* 119 */       isXPorLater = initNative();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FileFontStrike
 * JD-Core Version:    0.6.2
 */