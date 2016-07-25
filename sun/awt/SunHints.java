/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.RenderingHints.Key;
/*     */ 
/*     */ public class SunHints
/*     */ {
/*     */   private static final int NUM_KEYS = 9;
/*     */   private static final int VALS_PER_KEY = 8;
/*     */   public static final int INTKEY_RENDERING = 0;
/*     */   public static final int INTVAL_RENDER_DEFAULT = 0;
/*     */   public static final int INTVAL_RENDER_SPEED = 1;
/*     */   public static final int INTVAL_RENDER_QUALITY = 2;
/*     */   public static final int INTKEY_ANTIALIASING = 1;
/*     */   public static final int INTVAL_ANTIALIAS_DEFAULT = 0;
/*     */   public static final int INTVAL_ANTIALIAS_OFF = 1;
/*     */   public static final int INTVAL_ANTIALIAS_ON = 2;
/*     */   public static final int INTKEY_TEXT_ANTIALIASING = 2;
/*     */   public static final int INTVAL_TEXT_ANTIALIAS_DEFAULT = 0;
/*     */   public static final int INTVAL_TEXT_ANTIALIAS_OFF = 1;
/*     */   public static final int INTVAL_TEXT_ANTIALIAS_ON = 2;
/*     */   public static final int INTVAL_TEXT_ANTIALIAS_GASP = 3;
/*     */   public static final int INTVAL_TEXT_ANTIALIAS_LCD_HRGB = 4;
/*     */   public static final int INTVAL_TEXT_ANTIALIAS_LCD_HBGR = 5;
/*     */   public static final int INTVAL_TEXT_ANTIALIAS_LCD_VRGB = 6;
/*     */   public static final int INTVAL_TEXT_ANTIALIAS_LCD_VBGR = 7;
/*     */   public static final int INTKEY_FRACTIONALMETRICS = 3;
/*     */   public static final int INTVAL_FRACTIONALMETRICS_DEFAULT = 0;
/*     */   public static final int INTVAL_FRACTIONALMETRICS_OFF = 1;
/*     */   public static final int INTVAL_FRACTIONALMETRICS_ON = 2;
/*     */   public static final int INTKEY_DITHERING = 4;
/*     */   public static final int INTVAL_DITHER_DEFAULT = 0;
/*     */   public static final int INTVAL_DITHER_DISABLE = 1;
/*     */   public static final int INTVAL_DITHER_ENABLE = 2;
/*     */   public static final int INTKEY_INTERPOLATION = 5;
/*     */   public static final int INTVAL_INTERPOLATION_NEAREST_NEIGHBOR = 0;
/*     */   public static final int INTVAL_INTERPOLATION_BILINEAR = 1;
/*     */   public static final int INTVAL_INTERPOLATION_BICUBIC = 2;
/*     */   public static final int INTKEY_ALPHA_INTERPOLATION = 6;
/*     */   public static final int INTVAL_ALPHA_INTERPOLATION_DEFAULT = 0;
/*     */   public static final int INTVAL_ALPHA_INTERPOLATION_SPEED = 1;
/*     */   public static final int INTVAL_ALPHA_INTERPOLATION_QUALITY = 2;
/*     */   public static final int INTKEY_COLOR_RENDERING = 7;
/*     */   public static final int INTVAL_COLOR_RENDER_DEFAULT = 0;
/*     */   public static final int INTVAL_COLOR_RENDER_SPEED = 1;
/*     */   public static final int INTVAL_COLOR_RENDER_QUALITY = 2;
/*     */   public static final int INTKEY_STROKE_CONTROL = 8;
/*     */   public static final int INTVAL_STROKE_DEFAULT = 0;
/*     */   public static final int INTVAL_STROKE_NORMALIZE = 1;
/*     */   public static final int INTVAL_STROKE_PURE = 2;
/*     */   public static final int INTKEY_AATEXT_LCD_CONTRAST = 100;
/* 264 */   public static final Key KEY_RENDERING = new Key(0, "Global rendering quality key");
/*     */ 
/* 267 */   public static final Object VALUE_RENDER_SPEED = new Value(KEY_RENDERING, 1, "Fastest rendering methods");
/*     */ 
/* 271 */   public static final Object VALUE_RENDER_QUALITY = new Value(KEY_RENDERING, 2, "Highest quality rendering methods");
/*     */ 
/* 275 */   public static final Object VALUE_RENDER_DEFAULT = new Value(KEY_RENDERING, 0, "Default rendering methods");
/*     */ 
/* 283 */   public static final Key KEY_ANTIALIASING = new Key(1, "Global antialiasing enable key");
/*     */ 
/* 286 */   public static final Object VALUE_ANTIALIAS_ON = new Value(KEY_ANTIALIASING, 2, "Antialiased rendering mode");
/*     */ 
/* 290 */   public static final Object VALUE_ANTIALIAS_OFF = new Value(KEY_ANTIALIASING, 1, "Nonantialiased rendering mode");
/*     */ 
/* 294 */   public static final Object VALUE_ANTIALIAS_DEFAULT = new Value(KEY_ANTIALIASING, 0, "Default antialiasing rendering mode");
/*     */ 
/* 302 */   public static final Key KEY_TEXT_ANTIALIASING = new Key(2, "Text-specific antialiasing enable key");
/*     */ 
/* 305 */   public static final Object VALUE_TEXT_ANTIALIAS_ON = new Value(KEY_TEXT_ANTIALIASING, 2, "Antialiased text mode");
/*     */ 
/* 309 */   public static final Object VALUE_TEXT_ANTIALIAS_OFF = new Value(KEY_TEXT_ANTIALIASING, 1, "Nonantialiased text mode");
/*     */ 
/* 313 */   public static final Object VALUE_TEXT_ANTIALIAS_DEFAULT = new Value(KEY_TEXT_ANTIALIASING, 0, "Default antialiasing text mode");
/*     */ 
/* 317 */   public static final Object VALUE_TEXT_ANTIALIAS_GASP = new Value(KEY_TEXT_ANTIALIASING, 3, "gasp antialiasing text mode");
/*     */ 
/* 321 */   public static final Object VALUE_TEXT_ANTIALIAS_LCD_HRGB = new Value(KEY_TEXT_ANTIALIASING, 4, "LCD HRGB antialiasing text mode");
/*     */ 
/* 325 */   public static final Object VALUE_TEXT_ANTIALIAS_LCD_HBGR = new Value(KEY_TEXT_ANTIALIASING, 5, "LCD HBGR antialiasing text mode");
/*     */ 
/* 329 */   public static final Object VALUE_TEXT_ANTIALIAS_LCD_VRGB = new Value(KEY_TEXT_ANTIALIASING, 6, "LCD VRGB antialiasing text mode");
/*     */ 
/* 333 */   public static final Object VALUE_TEXT_ANTIALIAS_LCD_VBGR = new Value(KEY_TEXT_ANTIALIASING, 7, "LCD VBGR antialiasing text mode");
/*     */ 
/* 341 */   public static final Key KEY_FRACTIONALMETRICS = new Key(3, "Fractional metrics enable key");
/*     */ 
/* 344 */   public static final Object VALUE_FRACTIONALMETRICS_ON = new Value(KEY_FRACTIONALMETRICS, 2, "Fractional text metrics mode");
/*     */ 
/* 348 */   public static final Object VALUE_FRACTIONALMETRICS_OFF = new Value(KEY_FRACTIONALMETRICS, 1, "Integer text metrics mode");
/*     */ 
/* 352 */   public static final Object VALUE_FRACTIONALMETRICS_DEFAULT = new Value(KEY_FRACTIONALMETRICS, 0, "Default fractional text metrics mode");
/*     */ 
/* 360 */   public static final Key KEY_DITHERING = new Key(4, "Dithering quality key");
/*     */ 
/* 363 */   public static final Object VALUE_DITHER_ENABLE = new Value(KEY_DITHERING, 2, "Dithered rendering mode");
/*     */ 
/* 367 */   public static final Object VALUE_DITHER_DISABLE = new Value(KEY_DITHERING, 1, "Nondithered rendering mode");
/*     */ 
/* 371 */   public static final Object VALUE_DITHER_DEFAULT = new Value(KEY_DITHERING, 0, "Default dithering mode");
/*     */ 
/* 379 */   public static final Key KEY_INTERPOLATION = new Key(5, "Image interpolation method key");
/*     */ 
/* 382 */   public static final Object VALUE_INTERPOLATION_NEAREST_NEIGHBOR = new Value(KEY_INTERPOLATION, 0, "Nearest Neighbor image interpolation mode");
/*     */ 
/* 386 */   public static final Object VALUE_INTERPOLATION_BILINEAR = new Value(KEY_INTERPOLATION, 1, "Bilinear image interpolation mode");
/*     */ 
/* 390 */   public static final Object VALUE_INTERPOLATION_BICUBIC = new Value(KEY_INTERPOLATION, 2, "Bicubic image interpolation mode");
/*     */ 
/* 398 */   public static final Key KEY_ALPHA_INTERPOLATION = new Key(6, "Alpha blending interpolation method key");
/*     */ 
/* 401 */   public static final Object VALUE_ALPHA_INTERPOLATION_SPEED = new Value(KEY_ALPHA_INTERPOLATION, 1, "Fastest alpha blending methods");
/*     */ 
/* 405 */   public static final Object VALUE_ALPHA_INTERPOLATION_QUALITY = new Value(KEY_ALPHA_INTERPOLATION, 2, "Highest quality alpha blending methods");
/*     */ 
/* 409 */   public static final Object VALUE_ALPHA_INTERPOLATION_DEFAULT = new Value(KEY_ALPHA_INTERPOLATION, 0, "Default alpha blending methods");
/*     */ 
/* 417 */   public static final Key KEY_COLOR_RENDERING = new Key(7, "Color rendering quality key");
/*     */ 
/* 420 */   public static final Object VALUE_COLOR_RENDER_SPEED = new Value(KEY_COLOR_RENDERING, 1, "Fastest color rendering mode");
/*     */ 
/* 424 */   public static final Object VALUE_COLOR_RENDER_QUALITY = new Value(KEY_COLOR_RENDERING, 2, "Highest quality color rendering mode");
/*     */ 
/* 428 */   public static final Object VALUE_COLOR_RENDER_DEFAULT = new Value(KEY_COLOR_RENDERING, 0, "Default color rendering mode");
/*     */ 
/* 436 */   public static final Key KEY_STROKE_CONTROL = new Key(8, "Stroke normalization control key");
/*     */ 
/* 439 */   public static final Object VALUE_STROKE_DEFAULT = new Value(KEY_STROKE_CONTROL, 0, "Default stroke normalization");
/*     */ 
/* 443 */   public static final Object VALUE_STROKE_NORMALIZE = new Value(KEY_STROKE_CONTROL, 1, "Normalize strokes for consistent rendering");
/*     */ 
/* 447 */   public static final Object VALUE_STROKE_PURE = new Value(KEY_STROKE_CONTROL, 2, "Pure stroke conversion for accurate paths");
/*     */ 
/* 477 */   public static final RenderingHints.Key KEY_TEXT_ANTIALIAS_LCD_CONTRAST = new LCDContrastKey(100, "Text-specific LCD contrast key");
/*     */ 
/*     */   public static class Key extends RenderingHints.Key
/*     */   {
/*     */     String description;
/*     */ 
/*     */     public Key(int paramInt, String paramString)
/*     */     {
/*  57 */       super();
/*  58 */       this.description = paramString;
/*     */     }
/*     */ 
/*     */     public final int getIndex()
/*     */     {
/*  67 */       return intKey();
/*     */     }
/*     */ 
/*     */     public final String toString()
/*     */     {
/*  74 */       return this.description;
/*     */     }
/*     */ 
/*     */     public boolean isCompatibleValue(Object paramObject)
/*     */     {
/*  82 */       if ((paramObject instanceof SunHints.Value)) {
/*  83 */         return ((SunHints.Value)paramObject).isCompatibleKey(this);
/*     */       }
/*  85 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class LCDContrastKey extends SunHints.Key
/*     */   {
/*     */     public LCDContrastKey(int paramInt, String paramString)
/*     */     {
/* 456 */       super(paramString);
/*     */     }
/*     */ 
/*     */     public final boolean isCompatibleValue(Object paramObject)
/*     */     {
/* 464 */       if ((paramObject instanceof Integer)) {
/* 465 */         int i = ((Integer)paramObject).intValue();
/* 466 */         return (i >= 100) && (i <= 250);
/*     */       }
/* 468 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Value
/*     */   {
/*     */     private SunHints.Key myKey;
/*     */     private int index;
/*     */     private String description;
/* 101 */     private static Value[][] ValueObjects = new Value[9][8];
/*     */ 
/*     */     private static synchronized void register(SunHints.Key paramKey, Value paramValue)
/*     */     {
/* 106 */       int i = paramKey.getIndex();
/* 107 */       int j = paramValue.getIndex();
/* 108 */       if (ValueObjects[i][j] != null) {
/* 109 */         throw new InternalError("duplicate index: " + j);
/*     */       }
/* 111 */       ValueObjects[i][j] = paramValue;
/*     */     }
/*     */ 
/*     */     public static Value get(int paramInt1, int paramInt2) {
/* 115 */       return ValueObjects[paramInt1][paramInt2];
/*     */     }
/*     */ 
/*     */     public Value(SunHints.Key paramKey, int paramInt, String paramString)
/*     */     {
/* 125 */       this.myKey = paramKey;
/* 126 */       this.index = paramInt;
/* 127 */       this.description = paramString;
/*     */ 
/* 129 */       register(paramKey, this);
/*     */     }
/*     */ 
/*     */     public final int getIndex()
/*     */     {
/* 138 */       return this.index;
/*     */     }
/*     */ 
/*     */     public final String toString()
/*     */     {
/* 145 */       return this.description;
/*     */     }
/*     */ 
/*     */     public final boolean isCompatibleKey(SunHints.Key paramKey)
/*     */     {
/* 153 */       return this.myKey == paramKey;
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 162 */       return System.identityHashCode(this);
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 170 */       return this == paramObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.SunHints
 * JD-Core Version:    0.6.2
 */