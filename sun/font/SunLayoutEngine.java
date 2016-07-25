/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public final class SunLayoutEngine
/*     */   implements GlyphLayout.LayoutEngine, GlyphLayout.LayoutEngineFactory
/*     */ {
/*     */   private GlyphLayout.LayoutEngineKey key;
/*     */   private static GlyphLayout.LayoutEngineFactory instance;
/* 146 */   private SoftReference cacheref = new SoftReference(null);
/*     */ 
/*     */   private static native void initGVIDs();
/*     */ 
/*     */   public static GlyphLayout.LayoutEngineFactory instance()
/*     */   {
/* 116 */     if (instance == null) {
/* 117 */       instance = new SunLayoutEngine();
/*     */     }
/* 119 */     return instance;
/*     */   }
/*     */ 
/*     */   private SunLayoutEngine()
/*     */   {
/*     */   }
/*     */ 
/*     */   public GlyphLayout.LayoutEngine getEngine(Font2D paramFont2D, int paramInt1, int paramInt2) {
/* 127 */     return getEngine(new GlyphLayout.LayoutEngineKey(paramFont2D, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public GlyphLayout.LayoutEngine getEngine(GlyphLayout.LayoutEngineKey paramLayoutEngineKey)
/*     */   {
/* 132 */     ConcurrentHashMap localConcurrentHashMap = (ConcurrentHashMap)this.cacheref.get();
/* 133 */     if (localConcurrentHashMap == null) {
/* 134 */       localConcurrentHashMap = new ConcurrentHashMap();
/* 135 */       this.cacheref = new SoftReference(localConcurrentHashMap);
/*     */     }
/*     */ 
/* 138 */     Object localObject = (GlyphLayout.LayoutEngineFactory)localConcurrentHashMap.get(paramLayoutEngineKey);
/* 139 */     if (localObject == null) {
/* 140 */       GlyphLayout.LayoutEngineKey localLayoutEngineKey = paramLayoutEngineKey.copy();
/* 141 */       localObject = new SunLayoutEngine(localLayoutEngineKey);
/* 142 */       localConcurrentHashMap.put(localLayoutEngineKey, localObject);
/*     */     }
/* 144 */     return localObject;
/*     */   }
/*     */ 
/*     */   private SunLayoutEngine(GlyphLayout.LayoutEngineKey paramLayoutEngineKey)
/*     */   {
/* 149 */     this.key = paramLayoutEngineKey;
/*     */   }
/*     */ 
/*     */   public void layout(FontStrikeDesc paramFontStrikeDesc, float[] paramArrayOfFloat, int paramInt1, int paramInt2, TextRecord paramTextRecord, int paramInt3, Point2D.Float paramFloat, GlyphLayout.GVData paramGVData)
/*     */   {
/* 155 */     Font2D localFont2D = this.key.font();
/* 156 */     FontStrike localFontStrike = localFont2D.getStrike(paramFontStrikeDesc);
/* 157 */     long l = 0L;
/* 158 */     if ((localFont2D instanceof TrueTypeFont)) {
/* 159 */       l = ((TrueTypeFont)localFont2D).getLayoutTableCache();
/*     */     }
/* 161 */     nativeLayout(localFont2D, localFontStrike, paramArrayOfFloat, paramInt1, paramInt2, paramTextRecord.text, paramTextRecord.start, paramTextRecord.limit, paramTextRecord.min, paramTextRecord.max, this.key.script(), this.key.lang(), paramInt3, paramFloat, paramGVData, localFont2D.getUnitsPerEm(), l);
/*     */   }
/*     */ 
/*     */   private static native void nativeLayout(Font2D paramFont2D, FontStrike paramFontStrike, float[] paramArrayOfFloat, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, Point2D.Float paramFloat, GlyphLayout.GVData paramGVData, long paramLong1, long paramLong2);
/*     */ 
/*     */   static
/*     */   {
/* 107 */     FontManagerNativeLibrary.load();
/* 108 */     initGVIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.SunLayoutEngine
 * JD-Core Version:    0.6.2
 */