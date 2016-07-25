/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.font.GlyphVector;
/*     */ import sun.java2d.loops.FontInfo;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public final class GlyphList
/*     */ {
/*     */   private static final int MINGRAYLENGTH = 1024;
/*     */   private static final int MAXGRAYLENGTH = 8192;
/*     */   private static final int DEFAULT_LENGTH = 32;
/*     */   int glyphindex;
/*     */   int[] metrics;
/*     */   byte[] graybits;
/*     */   Object strikelist;
/* 106 */   int len = 0;
/* 107 */   int maxLen = 0;
/* 108 */   int maxPosLen = 0;
/*     */   int[] glyphData;
/*     */   char[] chData;
/*     */   long[] images;
/*     */   float[] positions;
/*     */   float x;
/*     */   float y;
/*     */   float gposx;
/*     */   float gposy;
/*     */   boolean usePositions;
/*     */   boolean lcdRGBOrder;
/*     */   boolean lcdSubPixPos;
/* 154 */   private static GlyphList reusableGL = new GlyphList();
/*     */   private static boolean inUse;
/*     */ 
/*     */   void ensureCapacity(int paramInt)
/*     */   {
/* 162 */     if (paramInt < 0) {
/* 163 */       paramInt = 0;
/*     */     }
/* 165 */     if ((this.usePositions) && (paramInt > this.maxPosLen)) {
/* 166 */       this.positions = new float[paramInt * 2 + 2];
/* 167 */       this.maxPosLen = paramInt;
/*     */     }
/*     */ 
/* 170 */     if ((this.maxLen == 0) || (paramInt > this.maxLen)) {
/* 171 */       this.glyphData = new int[paramInt];
/* 172 */       this.chData = new char[paramInt];
/* 173 */       this.images = new long[paramInt];
/* 174 */       this.maxLen = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static GlyphList getInstance()
/*     */   {
/* 194 */     if (inUse) {
/* 195 */       return new GlyphList();
/*     */     }
/* 197 */     synchronized (GlyphList.class) {
/* 198 */       if (inUse) {
/* 199 */         return new GlyphList();
/*     */       }
/* 201 */       inUse = true;
/* 202 */       return reusableGL;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean setFromString(FontInfo paramFontInfo, String paramString, float paramFloat1, float paramFloat2)
/*     */   {
/* 236 */     this.x = paramFloat1;
/* 237 */     this.y = paramFloat2;
/* 238 */     this.strikelist = paramFontInfo.fontStrike;
/* 239 */     this.lcdRGBOrder = paramFontInfo.lcdRGBOrder;
/* 240 */     this.lcdSubPixPos = paramFontInfo.lcdSubPixPos;
/* 241 */     this.len = paramString.length();
/* 242 */     ensureCapacity(this.len);
/* 243 */     paramString.getChars(0, this.len, this.chData, 0);
/* 244 */     return mapChars(paramFontInfo, this.len);
/*     */   }
/*     */ 
/*     */   public boolean setFromChars(FontInfo paramFontInfo, char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
/*     */   {
/* 249 */     this.x = paramFloat1;
/* 250 */     this.y = paramFloat2;
/* 251 */     this.strikelist = paramFontInfo.fontStrike;
/* 252 */     this.lcdRGBOrder = paramFontInfo.lcdRGBOrder;
/* 253 */     this.lcdSubPixPos = paramFontInfo.lcdSubPixPos;
/* 254 */     this.len = paramInt2;
/* 255 */     if (paramInt2 < 0)
/* 256 */       this.len = 0;
/*     */     else {
/* 258 */       this.len = paramInt2;
/*     */     }
/* 260 */     ensureCapacity(this.len);
/* 261 */     System.arraycopy(paramArrayOfChar, paramInt1, this.chData, 0, this.len);
/* 262 */     return mapChars(paramFontInfo, this.len);
/*     */   }
/*     */ 
/*     */   private final boolean mapChars(FontInfo paramFontInfo, int paramInt)
/*     */   {
/* 269 */     if (paramFontInfo.font2D.getMapper().charsToGlyphsNS(paramInt, this.chData, this.glyphData)) {
/* 270 */       return false;
/*     */     }
/* 272 */     paramFontInfo.fontStrike.getGlyphImagePtrs(this.glyphData, this.images, paramInt);
/* 273 */     this.glyphindex = -1;
/* 274 */     return true;
/*     */   }
/*     */ 
/*     */   public void setFromGlyphVector(FontInfo paramFontInfo, GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*     */   {
/* 280 */     this.x = paramFloat1;
/* 281 */     this.y = paramFloat2;
/* 282 */     this.lcdRGBOrder = paramFontInfo.lcdRGBOrder;
/* 283 */     this.lcdSubPixPos = paramFontInfo.lcdSubPixPos;
/*     */ 
/* 288 */     StandardGlyphVector localStandardGlyphVector = StandardGlyphVector.getStandardGV(paramGlyphVector, paramFontInfo);
/*     */ 
/* 290 */     this.usePositions = localStandardGlyphVector.needsPositions(paramFontInfo.devTx);
/* 291 */     this.len = localStandardGlyphVector.getNumGlyphs();
/* 292 */     ensureCapacity(this.len);
/* 293 */     this.strikelist = localStandardGlyphVector.setupGlyphImages(this.images, this.usePositions ? this.positions : null, paramFontInfo.devTx);
/*     */ 
/* 296 */     this.glyphindex = -1;
/*     */   }
/*     */ 
/*     */   public int[] getBounds()
/*     */   {
/* 304 */     if (this.glyphindex >= 0) {
/* 305 */       throw new InternalError("calling getBounds after setGlyphIndex");
/*     */     }
/* 307 */     if (this.metrics == null) {
/* 308 */       this.metrics = new int[5];
/*     */     }
/*     */ 
/* 312 */     this.gposx = (this.x + 0.5F);
/* 313 */     this.gposy = (this.y + 0.5F);
/* 314 */     fillBounds(this.metrics);
/* 315 */     return this.metrics;
/*     */   }
/*     */ 
/*     */   public void setGlyphIndex(int paramInt)
/*     */   {
/* 325 */     this.glyphindex = paramInt;
/* 326 */     float f1 = StrikeCache.unsafe.getFloat(this.images[paramInt] + StrikeCache.topLeftXOffset);
/*     */ 
/* 328 */     float f2 = StrikeCache.unsafe.getFloat(this.images[paramInt] + StrikeCache.topLeftYOffset);
/*     */ 
/* 331 */     if (this.usePositions) {
/* 332 */       this.metrics[0] = ((int)Math.floor(this.positions[(paramInt << 1)] + this.gposx + f1));
/* 333 */       this.metrics[1] = ((int)Math.floor(this.positions[((paramInt << 1) + 1)] + this.gposy + f2));
/*     */     } else {
/* 335 */       this.metrics[0] = ((int)Math.floor(this.gposx + f1));
/* 336 */       this.metrics[1] = ((int)Math.floor(this.gposy + f2));
/*     */ 
/* 338 */       this.gposx += StrikeCache.unsafe.getFloat(this.images[paramInt] + StrikeCache.xAdvanceOffset);
/*     */ 
/* 340 */       this.gposy += StrikeCache.unsafe.getFloat(this.images[paramInt] + StrikeCache.yAdvanceOffset);
/*     */     }
/*     */ 
/* 343 */     this.metrics[2] = StrikeCache.unsafe.getChar(this.images[paramInt] + StrikeCache.widthOffset);
/*     */ 
/* 345 */     this.metrics[3] = StrikeCache.unsafe.getChar(this.images[paramInt] + StrikeCache.heightOffset);
/*     */ 
/* 347 */     this.metrics[4] = StrikeCache.unsafe.getChar(this.images[paramInt] + StrikeCache.rowBytesOffset);
/*     */   }
/*     */ 
/*     */   public int[] getMetrics()
/*     */   {
/* 352 */     return this.metrics;
/*     */   }
/*     */ 
/*     */   public byte[] getGrayBits() {
/* 356 */     int i = this.metrics[4] * this.metrics[3];
/* 357 */     if (this.graybits == null) {
/* 358 */       this.graybits = new byte[Math.max(i, 1024)];
/*     */     }
/* 360 */     else if (i > this.graybits.length) {
/* 361 */       this.graybits = new byte[i];
/*     */     }
/*     */ 
/* 364 */     long l = StrikeCache.unsafe.getAddress(this.images[this.glyphindex] + StrikeCache.pixelDataOffset);
/*     */ 
/* 368 */     if (l == 0L) {
/* 369 */       return this.graybits;
/*     */     }
/*     */ 
/* 377 */     for (int j = 0; j < i; j++) {
/* 378 */       this.graybits[j] = StrikeCache.unsafe.getByte(l + j);
/*     */     }
/* 380 */     return this.graybits;
/*     */   }
/*     */ 
/*     */   public long[] getImages() {
/* 384 */     return this.images;
/*     */   }
/*     */ 
/*     */   public boolean usePositions() {
/* 388 */     return this.usePositions;
/*     */   }
/*     */ 
/*     */   public float[] getPositions() {
/* 392 */     return this.positions;
/*     */   }
/*     */ 
/*     */   public float getX() {
/* 396 */     return this.x;
/*     */   }
/*     */ 
/*     */   public float getY() {
/* 400 */     return this.y;
/*     */   }
/*     */ 
/*     */   public Object getStrike() {
/* 404 */     return this.strikelist;
/*     */   }
/*     */ 
/*     */   public boolean isSubPixPos() {
/* 408 */     return this.lcdSubPixPos;
/*     */   }
/*     */ 
/*     */   public boolean isRGBOrder() {
/* 412 */     return this.lcdRGBOrder;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 420 */     if (this == reusableGL) {
/* 421 */       if ((this.graybits != null) && (this.graybits.length > 8192)) {
/* 422 */         this.graybits = null;
/*     */       }
/* 424 */       this.usePositions = false;
/* 425 */       this.strikelist = null;
/* 426 */       inUse = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs()
/*     */   {
/* 442 */     return this.len;
/*     */   }
/*     */ 
/*     */   private void fillBounds(int[] paramArrayOfInt)
/*     */   {
/* 450 */     int i = StrikeCache.topLeftXOffset;
/* 451 */     int j = StrikeCache.topLeftYOffset;
/* 452 */     int k = StrikeCache.widthOffset;
/* 453 */     int m = StrikeCache.heightOffset;
/* 454 */     int n = StrikeCache.xAdvanceOffset;
/* 455 */     int i1 = StrikeCache.yAdvanceOffset;
/*     */ 
/* 457 */     if (this.len == 0)
/*     */     {
/*     */       int tmp48_47 = (paramArrayOfInt[2] = paramArrayOfInt[3] = 0); paramArrayOfInt[1] = tmp48_47; paramArrayOfInt[0] = tmp48_47;
/*     */       return;
/*     */     }
/*     */     float f2;
/* 462 */     float f1 = f2 = (1.0F / 1.0F);
/*     */     float f4;
/* 463 */     float f3 = f4 = (1.0F / -1.0F);
/*     */ 
/* 465 */     int i2 = 0;
/* 466 */     float f5 = this.x + 0.5F;
/* 467 */     float f6 = this.y + 0.5F;
/*     */ 
/* 470 */     for (int i5 = 0; i5 < this.len; i5++) {
/* 471 */       float f7 = StrikeCache.unsafe.getFloat(this.images[i5] + i);
/* 472 */       float f8 = StrikeCache.unsafe.getFloat(this.images[i5] + j);
/* 473 */       int i3 = StrikeCache.unsafe.getChar(this.images[i5] + k);
/* 474 */       int i4 = StrikeCache.unsafe.getChar(this.images[i5] + m);
/*     */       float f9;
/*     */       float f10;
/* 476 */       if (this.usePositions) {
/* 477 */         f9 = this.positions[(i2++)] + f7 + f5;
/* 478 */         f10 = this.positions[(i2++)] + f8 + f6;
/*     */       } else {
/* 480 */         f9 = f5 + f7;
/* 481 */         f10 = f6 + f8;
/* 482 */         f5 += StrikeCache.unsafe.getFloat(this.images[i5] + n);
/* 483 */         f6 += StrikeCache.unsafe.getFloat(this.images[i5] + i1);
/*     */       }
/* 485 */       float f11 = f9 + i3;
/* 486 */       float f12 = f10 + i4;
/* 487 */       if (f1 > f9) f1 = f9;
/* 488 */       if (f2 > f10) f2 = f10;
/* 489 */       if (f3 < f11) f3 = f11;
/* 490 */       if (f4 < f12) f4 = f12;
/*     */ 
/*     */     }
/*     */ 
/* 495 */     paramArrayOfInt[0] = ((int)Math.floor(f1));
/* 496 */     paramArrayOfInt[1] = ((int)Math.floor(f2));
/* 497 */     paramArrayOfInt[2] = ((int)Math.floor(f3));
/* 498 */     paramArrayOfInt[3] = ((int)Math.floor(f4));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.GlyphList
 * JD-Core Version:    0.6.2
 */