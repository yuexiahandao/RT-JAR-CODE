/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.lang.ref.WeakReference;
/*     */ 
/*     */ class FreetypeFontScaler extends FontScaler
/*     */ {
/*     */   private static final int TRUETYPE_FONT = 1;
/*     */   private static final int TYPE1_FONT = 2;
/*     */ 
/*     */   private static native void initIDs(Class paramClass);
/*     */ 
/*     */   private void invalidateScaler()
/*     */     throws FontScalerException
/*     */   {
/*  56 */     this.nativeScaler = 0L;
/*  57 */     this.font = null;
/*  58 */     throw new FontScalerException();
/*     */   }
/*     */ 
/*     */   public FreetypeFontScaler(Font2D paramFont2D, int paramInt1, boolean paramBoolean, int paramInt2)
/*     */   {
/*  63 */     int i = 1;
/*  64 */     if ((paramFont2D instanceof Type1Font)) {
/*  65 */       i = 2;
/*     */     }
/*  67 */     this.nativeScaler = initNativeScaler(paramFont2D, i, paramInt1, paramBoolean, paramInt2);
/*     */ 
/*  72 */     this.font = new WeakReference(paramFont2D);
/*     */   }
/*     */ 
/*     */   synchronized StrikeMetrics getFontMetrics(long paramLong) throws FontScalerException
/*     */   {
/*  77 */     if (this.nativeScaler != 0L) {
/*  78 */       return getFontMetricsNative((Font2D)this.font.get(), paramLong, this.nativeScaler);
/*     */     }
/*     */ 
/*  82 */     return FontScaler.getNullScaler().getFontMetrics(0L);
/*     */   }
/*     */ 
/*     */   synchronized float getGlyphAdvance(long paramLong, int paramInt) throws FontScalerException
/*     */   {
/*  87 */     if (this.nativeScaler != 0L) {
/*  88 */       return getGlyphAdvanceNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt);
/*     */     }
/*     */ 
/*  93 */     return FontScaler.getNullScaler().getGlyphAdvance(0L, paramInt);
/*     */   }
/*     */ 
/*     */   synchronized void getGlyphMetrics(long paramLong, int paramInt, Point2D.Float paramFloat)
/*     */     throws FontScalerException
/*     */   {
/* 100 */     if (this.nativeScaler != 0L) {
/* 101 */       getGlyphMetricsNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt, paramFloat);
/*     */ 
/* 106 */       return;
/*     */     }
/* 108 */     FontScaler.getNullScaler().getGlyphMetrics(0L, paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   synchronized long getGlyphImage(long paramLong, int paramInt)
/*     */     throws FontScalerException
/*     */   {
/* 114 */     if (this.nativeScaler != 0L) {
/* 115 */       return getGlyphImageNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt);
/*     */     }
/*     */ 
/* 120 */     return FontScaler.getNullScaler().getGlyphImage(0L, paramInt);
/*     */   }
/*     */ 
/*     */   synchronized Rectangle2D.Float getGlyphOutlineBounds(long paramLong, int paramInt)
/*     */     throws FontScalerException
/*     */   {
/* 127 */     if (this.nativeScaler != 0L) {
/* 128 */       return getGlyphOutlineBoundsNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt);
/*     */     }
/*     */ 
/* 133 */     return FontScaler.getNullScaler().getGlyphOutlineBounds(0L, paramInt);
/*     */   }
/*     */ 
/*     */   synchronized GeneralPath getGlyphOutline(long paramLong, int paramInt, float paramFloat1, float paramFloat2)
/*     */     throws FontScalerException
/*     */   {
/* 140 */     if (this.nativeScaler != 0L) {
/* 141 */       return getGlyphOutlineNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt, paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/* 147 */     return FontScaler.getNullScaler().getGlyphOutline(0L, paramInt, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   synchronized GeneralPath getGlyphVectorOutline(long paramLong, int[] paramArrayOfInt, int paramInt, float paramFloat1, float paramFloat2)
/*     */     throws FontScalerException
/*     */   {
/* 154 */     if (this.nativeScaler != 0L) {
/* 155 */       return getGlyphVectorOutlineNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramArrayOfInt, paramInt, paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/* 162 */     return FontScaler.getNullScaler().getGlyphVectorOutline(0L, paramArrayOfInt, paramInt, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   synchronized long getLayoutTableCache() throws FontScalerException
/*     */   {
/* 167 */     return getLayoutTableCacheNative(this.nativeScaler);
/*     */   }
/*     */ 
/*     */   public synchronized void dispose() {
/* 171 */     if (this.nativeScaler != 0L) {
/* 172 */       disposeNativeScaler(this.nativeScaler);
/* 173 */       this.nativeScaler = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized int getNumGlyphs() throws FontScalerException {
/* 178 */     if (this.nativeScaler != 0L) {
/* 179 */       return getNumGlyphsNative(this.nativeScaler);
/*     */     }
/* 181 */     return FontScaler.getNullScaler().getNumGlyphs();
/*     */   }
/*     */ 
/*     */   synchronized int getMissingGlyphCode() throws FontScalerException {
/* 185 */     if (this.nativeScaler != 0L) {
/* 186 */       return getMissingGlyphCodeNative(this.nativeScaler);
/*     */     }
/* 188 */     return FontScaler.getNullScaler().getMissingGlyphCode();
/*     */   }
/*     */ 
/*     */   synchronized int getGlyphCode(char paramChar) throws FontScalerException {
/* 192 */     if (this.nativeScaler != 0L) {
/* 193 */       return getGlyphCodeNative(this.nativeScaler, paramChar);
/*     */     }
/* 195 */     return FontScaler.getNullScaler().getGlyphCode(paramChar);
/*     */   }
/*     */ 
/*     */   synchronized Point2D.Float getGlyphPoint(long paramLong, int paramInt1, int paramInt2)
/*     */     throws FontScalerException
/*     */   {
/* 201 */     if (this.nativeScaler != 0L) {
/* 202 */       return getGlyphPointNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 205 */     return FontScaler.getNullScaler().getGlyphPoint(paramLong, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   synchronized long getUnitsPerEm()
/*     */   {
/* 210 */     return getUnitsPerEMNative(this.nativeScaler);
/*     */   }
/*     */ 
/*     */   long createScalerContext(double[] paramArrayOfDouble, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, boolean paramBoolean)
/*     */   {
/* 216 */     if (this.nativeScaler != 0L) {
/* 217 */       return createScalerContextNative(this.nativeScaler, paramArrayOfDouble, paramInt1, paramInt2, paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/* 220 */     return NullFontScaler.getNullScalerContext();
/*     */   }
/*     */ 
/*     */   private native long initNativeScaler(Font2D paramFont2D, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3);
/*     */ 
/*     */   private native StrikeMetrics getFontMetricsNative(Font2D paramFont2D, long paramLong1, long paramLong2);
/*     */ 
/*     */   private native float getGlyphAdvanceNative(Font2D paramFont2D, long paramLong1, long paramLong2, int paramInt);
/*     */ 
/*     */   private native void getGlyphMetricsNative(Font2D paramFont2D, long paramLong1, long paramLong2, int paramInt, Point2D.Float paramFloat);
/*     */ 
/*     */   private native long getGlyphImageNative(Font2D paramFont2D, long paramLong1, long paramLong2, int paramInt);
/*     */ 
/*     */   private native Rectangle2D.Float getGlyphOutlineBoundsNative(Font2D paramFont2D, long paramLong1, long paramLong2, int paramInt);
/*     */ 
/*     */   private native GeneralPath getGlyphOutlineNative(Font2D paramFont2D, long paramLong1, long paramLong2, int paramInt, float paramFloat1, float paramFloat2);
/*     */ 
/*     */   private native GeneralPath getGlyphVectorOutlineNative(Font2D paramFont2D, long paramLong1, long paramLong2, int[] paramArrayOfInt, int paramInt, float paramFloat1, float paramFloat2);
/*     */ 
/*     */   native Point2D.Float getGlyphPointNative(Font2D paramFont2D, long paramLong1, long paramLong2, int paramInt1, int paramInt2);
/*     */ 
/*     */   private native long getLayoutTableCacheNative(long paramLong);
/*     */ 
/*     */   private native void disposeNativeScaler(long paramLong);
/*     */ 
/*     */   private native int getGlyphCodeNative(long paramLong, char paramChar);
/*     */ 
/*     */   private native int getNumGlyphsNative(long paramLong);
/*     */ 
/*     */   private native int getMissingGlyphCodeNative(long paramLong);
/*     */ 
/*     */   private native long getUnitsPerEMNative(long paramLong);
/*     */ 
/*     */   native long createScalerContextNative(long paramLong, double[] paramArrayOfDouble, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2);
/*     */ 
/*     */   void invalidateScalerContext(long paramLong)
/*     */   {
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  49 */     FontManagerNativeLibrary.load();
/*  50 */     initIDs(FreetypeFontScaler.class);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FreetypeFontScaler
 * JD-Core Version:    0.6.2
 */