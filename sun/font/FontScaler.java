/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.DisposerRecord;
/*     */ 
/*     */ public abstract class FontScaler
/*     */   implements DisposerRecord
/*     */ {
/*  84 */   private static FontScaler nullScaler = null;
/*  85 */   private static Constructor<FontScaler> scalerConstructor = null;
/*     */ 
/* 156 */   protected WeakReference<Font2D> font = null;
/* 157 */   protected long nativeScaler = 0L;
/*     */ 
/* 159 */   protected boolean disposed = false;
/*     */ 
/*     */   public static FontScaler getScaler(Font2D paramFont2D, int paramInt1, boolean paramBoolean, int paramInt2)
/*     */   {
/* 123 */     FontScaler localFontScaler = null;
/*     */     try
/*     */     {
/* 126 */       Object[] arrayOfObject = { paramFont2D, Integer.valueOf(paramInt1), Boolean.valueOf(paramBoolean), Integer.valueOf(paramInt2) };
/*     */ 
/* 128 */       localFontScaler = (FontScaler)scalerConstructor.newInstance(arrayOfObject);
/* 129 */       Disposer.addObjectRecord(paramFont2D, localFontScaler);
/*     */     } catch (Throwable localThrowable) {
/* 131 */       localFontScaler = nullScaler;
/*     */ 
/* 136 */       FontManager localFontManager = FontManagerFactory.getInstance();
/* 137 */       localFontManager.deRegisterBadFont(paramFont2D);
/*     */     }
/* 139 */     return localFontScaler;
/*     */   }
/*     */ 
/*     */   public static synchronized FontScaler getNullScaler()
/*     */   {
/* 150 */     if (nullScaler == null) {
/* 151 */       nullScaler = new NullFontScaler();
/*     */     }
/* 153 */     return nullScaler;
/*     */   }
/*     */ 
/*     */   abstract StrikeMetrics getFontMetrics(long paramLong)
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract float getGlyphAdvance(long paramLong, int paramInt)
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract void getGlyphMetrics(long paramLong, int paramInt, Point2D.Float paramFloat)
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract long getGlyphImage(long paramLong, int paramInt)
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract Rectangle2D.Float getGlyphOutlineBounds(long paramLong, int paramInt)
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract GeneralPath getGlyphOutline(long paramLong, int paramInt, float paramFloat1, float paramFloat2)
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract GeneralPath getGlyphVectorOutline(long paramLong, int[] paramArrayOfInt, int paramInt, float paramFloat1, float paramFloat2)
/*     */     throws FontScalerException;
/*     */ 
/*     */   public void dispose()
/*     */   {
/*     */   }
/*     */ 
/*     */   abstract int getNumGlyphs()
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract int getMissingGlyphCode()
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract int getGlyphCode(char paramChar)
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract long getLayoutTableCache()
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract Point2D.Float getGlyphPoint(long paramLong, int paramInt1, int paramInt2)
/*     */     throws FontScalerException;
/*     */ 
/*     */   abstract long getUnitsPerEm();
/*     */ 
/*     */   abstract long createScalerContext(double[] paramArrayOfDouble, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, boolean paramBoolean);
/*     */ 
/*     */   abstract void invalidateScalerContext(long paramLong);
/*     */ 
/*     */   static
/*     */   {
/*  92 */     Object localObject = null;
/*  93 */     Class[] arrayOfClass = { Font2D.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE };
/*     */     try
/*     */     {
/*  97 */       if (FontUtilities.isOpenJDK)
/*  98 */         localObject = Class.forName("sun.font.FreetypeFontScaler");
/*     */       else
/* 100 */         localObject = Class.forName("sun.font.T2KFontScaler");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 103 */       localObject = NullFontScaler.class;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 108 */       scalerConstructor = ((Class)localObject).getConstructor(arrayOfClass);
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontScaler
 * JD-Core Version:    0.6.2
 */