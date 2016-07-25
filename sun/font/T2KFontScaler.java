/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.misc.InnocuousThread;
/*     */ 
/*     */ class T2KFontScaler extends FontScaler
/*     */ {
/*     */   private int[] bwGlyphs;
/*     */   private static final int TRUETYPE_FONT = 1;
/*     */   private static final int TYPE1_FONT = 2;
/* 173 */   private long layoutTablePtr = 0L;
/*     */ 
/*     */   private void initBWGlyphs()
/*     */   {
/*  28 */     if ((this.font.get() != null) && ("Courier New".equals(((Font2D)this.font.get()).getFontName(null)))) {
/*  29 */       this.bwGlyphs = new int[2];
/*  30 */       CharToGlyphMapper localCharToGlyphMapper = ((Font2D)this.font.get()).getMapper();
/*  31 */       this.bwGlyphs[0] = localCharToGlyphMapper.charToGlyph('W');
/*  32 */       this.bwGlyphs[1] = localCharToGlyphMapper.charToGlyph('w');
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void initIDs(Class paramClass);
/*     */ 
/*     */   private void invalidateScaler()
/*     */     throws FontScalerException
/*     */   {
/*  64 */     this.nativeScaler = 0L;
/*  65 */     this.font = null;
/*  66 */     throw new FontScalerException();
/*     */   }
/*     */ 
/*     */   public T2KFontScaler(Font2D paramFont2D, int paramInt1, boolean paramBoolean, int paramInt2)
/*     */   {
/*  71 */     int i = 1;
/*  72 */     if ((paramFont2D instanceof Type1Font)) {
/*  73 */       i = 2;
/*     */     }
/*  75 */     this.font = new WeakReference(paramFont2D);
/*  76 */     initBWGlyphs();
/*  77 */     this.nativeScaler = initNativeScaler(paramFont2D, i, paramInt1, paramBoolean, paramInt2, this.bwGlyphs);
/*     */   }
/*     */ 
/*     */   synchronized StrikeMetrics getFontMetrics(long paramLong)
/*     */     throws FontScalerException
/*     */   {
/*  88 */     if (this.nativeScaler != 0L) {
/*  89 */       return getFontMetricsNative((Font2D)this.font.get(), paramLong, this.nativeScaler);
/*     */     }
/*     */ 
/*  92 */     return getNullScaler().getFontMetrics(0L);
/*     */   }
/*     */ 
/*     */   synchronized float getGlyphAdvance(long paramLong, int paramInt) throws FontScalerException
/*     */   {
/*  97 */     if (this.nativeScaler != 0L) {
/*  98 */       return getGlyphAdvanceNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt);
/*     */     }
/*     */ 
/* 101 */     return getNullScaler().getGlyphAdvance(0L, paramInt);
/*     */   }
/*     */ 
/*     */   synchronized void getGlyphMetrics(long paramLong, int paramInt, Point2D.Float paramFloat) throws FontScalerException
/*     */   {
/* 106 */     if (this.nativeScaler != 0L) {
/* 107 */       getGlyphMetricsNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt, paramFloat);
/*     */     }
/*     */     else
/* 110 */       getNullScaler().getGlyphMetrics(0L, paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   synchronized long getGlyphImage(long paramLong, int paramInt)
/*     */     throws FontScalerException
/*     */   {
/* 116 */     if (this.nativeScaler != 0L) {
/* 117 */       return getGlyphImageNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt);
/*     */     }
/*     */ 
/* 120 */     return getNullScaler().getGlyphImage(0L, paramInt);
/*     */   }
/*     */ 
/*     */   synchronized Rectangle2D.Float getGlyphOutlineBounds(long paramLong, int paramInt) throws FontScalerException
/*     */   {
/* 125 */     if (this.nativeScaler != 0L) {
/* 126 */       return getGlyphOutlineBoundsNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt);
/*     */     }
/*     */ 
/* 129 */     return getNullScaler().getGlyphOutlineBounds(0L, paramInt);
/*     */   }
/*     */ 
/*     */   synchronized GeneralPath getGlyphOutline(long paramLong, int paramInt, float paramFloat1, float paramFloat2) throws FontScalerException
/*     */   {
/* 134 */     if (this.nativeScaler != 0L) {
/* 135 */       return getGlyphOutlineNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt, paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/* 138 */     return getNullScaler().getGlyphOutline(0L, paramInt, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   synchronized GeneralPath getGlyphVectorOutline(long paramLong, int[] paramArrayOfInt, int paramInt, float paramFloat1, float paramFloat2)
/*     */     throws FontScalerException
/*     */   {
/* 144 */     if (this.nativeScaler != 0L) {
/* 145 */       return getGlyphVectorOutlineNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramArrayOfInt, paramInt, paramFloat1, paramFloat2);
/*     */     }
/*     */ 
/* 148 */     return getNullScaler().getGlyphVectorOutline(0L, paramArrayOfInt, paramInt, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   synchronized int getNumGlyphs() throws FontScalerException
/*     */   {
/* 153 */     if (this.nativeScaler != 0L) {
/* 154 */       return getNumGlyphsNative(this.nativeScaler);
/*     */     }
/* 156 */     return getNullScaler().getNumGlyphs();
/*     */   }
/*     */ 
/*     */   synchronized int getMissingGlyphCode() throws FontScalerException {
/* 160 */     if (this.nativeScaler != 0L) {
/* 161 */       return getMissingGlyphCodeNative(this.nativeScaler);
/*     */     }
/* 163 */     return getNullScaler().getMissingGlyphCode();
/*     */   }
/*     */ 
/*     */   synchronized int getGlyphCode(char paramChar) throws FontScalerException {
/* 167 */     if (this.nativeScaler != 0L) {
/* 168 */       return getGlyphCodeNative(this.nativeScaler, paramChar);
/*     */     }
/* 170 */     return getNullScaler().getGlyphCode(paramChar);
/*     */   }
/*     */ 
/*     */   synchronized long getLayoutTableCache()
/*     */     throws FontScalerException
/*     */   {
/* 176 */     if (this.nativeScaler == 0L) {
/* 177 */       return getNullScaler().getLayoutTableCache();
/*     */     }
/*     */ 
/* 180 */     if (this.layoutTablePtr == 0L) {
/* 181 */       this.layoutTablePtr = getLayoutTableCacheNative(this.nativeScaler);
/*     */     }
/*     */ 
/* 184 */     return this.layoutTablePtr;
/*     */   }
/*     */ 
/*     */   private synchronized void disposeScaler() {
/* 188 */     disposeNativeScaler(this.nativeScaler, this.layoutTablePtr);
/* 189 */     this.nativeScaler = 0L;
/* 190 */     this.layoutTablePtr = 0L;
/*     */   }
/*     */ 
/*     */   public synchronized void dispose() {
/* 194 */     if ((this.nativeScaler != 0L) || (this.layoutTablePtr != 0L))
/*     */     {
/* 204 */       final T2KFontScaler localT2KFontScaler = this;
/* 205 */       Runnable local2 = new Runnable() {
/*     */         public void run() {
/* 207 */           localT2KFontScaler.disposeScaler();
/*     */         }
/*     */       };
/* 210 */       new InnocuousThread(local2).start();
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized Point2D.Float getGlyphPoint(long paramLong, int paramInt1, int paramInt2)
/*     */     throws FontScalerException
/*     */   {
/* 217 */     if (this.nativeScaler != 0L) {
/* 218 */       return getGlyphPointNative((Font2D)this.font.get(), paramLong, this.nativeScaler, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 221 */     return getNullScaler().getGlyphPoint(paramLong, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   synchronized long getUnitsPerEm()
/*     */   {
/* 226 */     if (this.nativeScaler != 0L) {
/* 227 */       return getUnitsPerEMNative(this.nativeScaler);
/*     */     }
/* 229 */     return getNullScaler().getUnitsPerEm();
/*     */   }
/*     */ 
/*     */   synchronized long createScalerContext(double[] paramArrayOfDouble, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, boolean paramBoolean)
/*     */   {
/* 235 */     if (this.nativeScaler != 0L) {
/* 236 */       return createScalerContextNative(this.nativeScaler, paramArrayOfDouble, paramInt1, paramInt2, paramFloat1, paramFloat2, paramBoolean);
/*     */     }
/*     */ 
/* 239 */     return NullFontScaler.getNullScalerContext();
/*     */   }
/*     */ 
/*     */   private native long initNativeScaler(Font2D paramFont2D, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, int[] paramArrayOfInt);
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
/*     */   private native int getGlyphCodeNative(long paramLong, char paramChar);
/*     */ 
/*     */   private native long getLayoutTableCacheNative(long paramLong);
/*     */ 
/*     */   private native void disposeNativeScaler(long paramLong1, long paramLong2);
/*     */ 
/*     */   private native int getNumGlyphsNative(long paramLong);
/*     */ 
/*     */   private native int getMissingGlyphCodeNative(long paramLong);
/*     */ 
/*     */   private native long getUnitsPerEMNative(long paramLong);
/*     */ 
/*     */   private native long createScalerContextNative(long paramLong, double[] paramArrayOfDouble, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, boolean paramBoolean);
/*     */ 
/*     */   private native Point2D.Float getGlyphPointNative(Font2D paramFont2D, long paramLong1, long paramLong2, int paramInt1, int paramInt2);
/*     */ 
/*     */   void invalidateScalerContext(long paramLong)
/*     */   {
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  41 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*  49 */         FontManagerNativeLibrary.load();
/*  50 */         System.loadLibrary("t2k");
/*  51 */         return null;
/*     */       }
/*     */     });
/*  54 */     initIDs(T2KFontScaler.class);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.T2KFontScaler
 * JD-Core Version:    0.6.2
 */