/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public abstract class Font2D
/*     */ {
/*     */   public static final int FONT_CONFIG_RANK = 2;
/*     */   public static final int JRE_RANK = 2;
/*     */   public static final int TTF_RANK = 3;
/*     */   public static final int TYPE1_RANK = 4;
/*     */   public static final int NATIVE_RANK = 5;
/*     */   public static final int UNKNOWN_RANK = 6;
/*     */   public static final int DEFAULT_RANK = 4;
/*  57 */   private static final String[] boldNames = { "bold", "demibold", "demi-bold", "demi bold", "negreta", "demi" };
/*     */ 
/*  60 */   private static final String[] italicNames = { "italic", "cursiva", "oblique", "inclined" };
/*     */ 
/*  63 */   private static final String[] boldItalicNames = { "bolditalic", "bold-italic", "bold italic", "boldoblique", "bold-oblique", "bold oblique", "demibold italic", "negreta cursiva", "demi oblique" };
/*     */ 
/*  68 */   private static final FontRenderContext DEFAULT_FRC = new FontRenderContext(null, false, false);
/*     */   public Font2DHandle handle;
/*     */   protected String familyName;
/*     */   protected String fullName;
/*  74 */   protected int style = 0;
/*     */   protected FontFamily family;
/*  76 */   protected int fontRank = 4;
/*     */   protected CharToGlyphMapper mapper;
/*  94 */   protected ConcurrentHashMap<FontStrikeDesc, Reference> strikeCache = new ConcurrentHashMap();
/*     */ 
/* 108 */   protected Reference lastFontStrike = new SoftReference(null);
/*     */ 
/*     */   public int getStyle()
/*     */   {
/* 132 */     return this.style;
/*     */   }
/*     */ 
/*     */   protected void setStyle() {
/* 136 */     String str = this.fullName.toLowerCase();
/*     */ 
/* 138 */     for (int i = 0; i < boldItalicNames.length; i++) {
/* 139 */       if (str.indexOf(boldItalicNames[i]) != -1) {
/* 140 */         this.style = 3;
/* 141 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 145 */     for (i = 0; i < italicNames.length; i++) {
/* 146 */       if (str.indexOf(italicNames[i]) != -1) {
/* 147 */         this.style = 2;
/* 148 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 152 */     for (i = 0; i < boldNames.length; i++)
/* 153 */       if (str.indexOf(boldNames[i]) != -1) {
/* 154 */         this.style = 1;
/* 155 */         return;
/*     */       }
/*     */   }
/*     */ 
/*     */   int getRank()
/*     */   {
/* 162 */     return this.fontRank;
/*     */   }
/*     */ 
/*     */   void setRank(int paramInt) {
/* 166 */     this.fontRank = paramInt;
/*     */   }
/*     */ 
/*     */   abstract CharToGlyphMapper getMapper();
/*     */ 
/*     */   protected int getValidatedGlyphCode(int paramInt)
/*     */   {
/* 179 */     if ((paramInt < 0) || (paramInt >= getMapper().getNumGlyphs())) {
/* 180 */       paramInt = getMapper().getMissingGlyphCode();
/*     */     }
/* 182 */     return paramInt;
/*     */   }
/*     */ 
/*     */   abstract FontStrike createStrike(FontStrikeDesc paramFontStrikeDesc);
/*     */ 
/*     */   public FontStrike getStrike(Font paramFont)
/*     */   {
/* 198 */     FontStrike localFontStrike = (FontStrike)this.lastFontStrike.get();
/* 199 */     if (localFontStrike != null) {
/* 200 */       return localFontStrike;
/*     */     }
/* 202 */     return getStrike(paramFont, DEFAULT_FRC);
/*     */   }
/*     */ 
/*     */   public FontStrike getStrike(Font paramFont, AffineTransform paramAffineTransform, int paramInt1, int paramInt2)
/*     */   {
/* 238 */     double d = paramFont.getSize2D();
/* 239 */     AffineTransform localAffineTransform = (AffineTransform)paramAffineTransform.clone();
/* 240 */     localAffineTransform.scale(d, d);
/* 241 */     if (paramFont.isTransformed()) {
/* 242 */       localAffineTransform.concatenate(paramFont.getTransform());
/*     */     }
/* 244 */     if ((localAffineTransform.getTranslateX() != 0.0D) || (localAffineTransform.getTranslateY() != 0.0D)) {
/* 245 */       localAffineTransform.setTransform(localAffineTransform.getScaleX(), localAffineTransform.getShearY(), localAffineTransform.getShearX(), localAffineTransform.getScaleY(), 0.0D, 0.0D);
/*     */     }
/*     */ 
/* 251 */     FontStrikeDesc localFontStrikeDesc = new FontStrikeDesc(paramAffineTransform, localAffineTransform, paramFont.getStyle(), paramInt1, paramInt2);
/*     */ 
/* 253 */     return getStrike(localFontStrikeDesc, false);
/*     */   }
/*     */ 
/*     */   public FontStrike getStrike(Font paramFont, AffineTransform paramAffineTransform1, AffineTransform paramAffineTransform2, int paramInt1, int paramInt2)
/*     */   {
/* 264 */     FontStrikeDesc localFontStrikeDesc = new FontStrikeDesc(paramAffineTransform1, paramAffineTransform2, paramFont.getStyle(), paramInt1, paramInt2);
/*     */ 
/* 266 */     return getStrike(localFontStrikeDesc, false);
/*     */   }
/*     */ 
/*     */   public FontStrike getStrike(Font paramFont, FontRenderContext paramFontRenderContext)
/*     */   {
/* 271 */     AffineTransform localAffineTransform = paramFontRenderContext.getTransform();
/* 272 */     double d = paramFont.getSize2D();
/* 273 */     localAffineTransform.scale(d, d);
/* 274 */     if (paramFont.isTransformed()) {
/* 275 */       localAffineTransform.concatenate(paramFont.getTransform());
/* 276 */       if ((localAffineTransform.getTranslateX() != 0.0D) || (localAffineTransform.getTranslateY() != 0.0D)) {
/* 277 */         localAffineTransform.setTransform(localAffineTransform.getScaleX(), localAffineTransform.getShearY(), localAffineTransform.getShearX(), localAffineTransform.getScaleY(), 0.0D, 0.0D);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 284 */     int i = FontStrikeDesc.getAAHintIntVal(this, paramFont, paramFontRenderContext);
/* 285 */     int j = FontStrikeDesc.getFMHintIntVal(paramFontRenderContext.getFractionalMetricsHint());
/* 286 */     FontStrikeDesc localFontStrikeDesc = new FontStrikeDesc(paramFontRenderContext.getTransform(), localAffineTransform, paramFont.getStyle(), i, j);
/*     */ 
/* 289 */     return getStrike(localFontStrikeDesc, false);
/*     */   }
/*     */ 
/*     */   FontStrike getStrike(FontStrikeDesc paramFontStrikeDesc) {
/* 293 */     return getStrike(paramFontStrikeDesc, true);
/*     */   }
/*     */ 
/*     */   private FontStrike getStrike(FontStrikeDesc paramFontStrikeDesc, boolean paramBoolean)
/*     */   {
/* 310 */     FontStrike localFontStrike = (FontStrike)this.lastFontStrike.get();
/* 311 */     if ((localFontStrike != null) && (paramFontStrikeDesc.equals(localFontStrike.desc)))
/*     */     {
/* 313 */       return localFontStrike;
/*     */     }
/* 315 */     Reference localReference = (Reference)this.strikeCache.get(paramFontStrikeDesc);
/* 316 */     if (localReference != null) {
/* 317 */       localFontStrike = (FontStrike)localReference.get();
/* 318 */       if (localFontStrike != null)
/*     */       {
/* 320 */         this.lastFontStrike = new SoftReference(localFontStrike);
/* 321 */         StrikeCache.refStrike(localFontStrike);
/* 322 */         return localFontStrike;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 341 */     if (paramBoolean) {
/* 342 */       paramFontStrikeDesc = new FontStrikeDesc(paramFontStrikeDesc);
/*     */     }
/* 344 */     localFontStrike = createStrike(paramFontStrikeDesc);
/*     */ 
/* 353 */     int i = paramFontStrikeDesc.glyphTx.getType();
/* 354 */     if ((i == 32) || (((i & 0x10) != 0) && (this.strikeCache.size() > 10)))
/*     */     {
/* 357 */       localReference = StrikeCache.getStrikeRef(localFontStrike, true);
/*     */     }
/* 359 */     else localReference = StrikeCache.getStrikeRef(localFontStrike);
/*     */ 
/* 361 */     this.strikeCache.put(paramFontStrikeDesc, localReference);
/*     */ 
/* 363 */     this.lastFontStrike = new SoftReference(localFontStrike);
/* 364 */     StrikeCache.refStrike(localFontStrike);
/* 365 */     return localFontStrike;
/*     */   }
/*     */ 
/*     */   void removeFromCache(FontStrikeDesc paramFontStrikeDesc)
/*     */   {
/* 370 */     Reference localReference = (Reference)this.strikeCache.get(paramFontStrikeDesc);
/* 371 */     if (localReference != null) {
/* 372 */       Object localObject = localReference.get();
/* 373 */       if (localObject == null)
/* 374 */         this.strikeCache.remove(paramFontStrikeDesc);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getFontMetrics(Font paramFont, AffineTransform paramAffineTransform, Object paramObject1, Object paramObject2, float[] paramArrayOfFloat)
/*     */   {
/* 397 */     int i = FontStrikeDesc.getAAHintIntVal(paramObject1, this, paramFont.getSize());
/* 398 */     int j = FontStrikeDesc.getFMHintIntVal(paramObject2);
/* 399 */     FontStrike localFontStrike = getStrike(paramFont, paramAffineTransform, i, j);
/* 400 */     StrikeMetrics localStrikeMetrics = localFontStrike.getFontMetrics();
/* 401 */     paramArrayOfFloat[0] = localStrikeMetrics.getAscent();
/* 402 */     paramArrayOfFloat[1] = localStrikeMetrics.getDescent();
/* 403 */     paramArrayOfFloat[2] = localStrikeMetrics.getLeading();
/* 404 */     paramArrayOfFloat[3] = localStrikeMetrics.getMaxAdvance();
/*     */ 
/* 406 */     getStyleMetrics(paramFont.getSize2D(), paramArrayOfFloat, 4);
/*     */   }
/*     */ 
/*     */   public void getStyleMetrics(float paramFloat, float[] paramArrayOfFloat, int paramInt)
/*     */   {
/* 422 */     paramArrayOfFloat[paramInt] = (-paramArrayOfFloat[0] / 2.5F);
/* 423 */     paramArrayOfFloat[(paramInt + 1)] = (paramFloat / 12.0F);
/* 424 */     paramArrayOfFloat[(paramInt + 2)] = (paramArrayOfFloat[(paramInt + 1)] / 1.5F);
/* 425 */     paramArrayOfFloat[(paramInt + 3)] = paramArrayOfFloat[(paramInt + 1)];
/*     */   }
/*     */ 
/*     */   public void getFontMetrics(Font paramFont, FontRenderContext paramFontRenderContext, float[] paramArrayOfFloat)
/*     */   {
/* 438 */     StrikeMetrics localStrikeMetrics = getStrike(paramFont, paramFontRenderContext).getFontMetrics();
/* 439 */     paramArrayOfFloat[0] = localStrikeMetrics.getAscent();
/* 440 */     paramArrayOfFloat[1] = localStrikeMetrics.getDescent();
/* 441 */     paramArrayOfFloat[2] = localStrikeMetrics.getLeading();
/* 442 */     paramArrayOfFloat[3] = localStrikeMetrics.getMaxAdvance();
/*     */   }
/*     */ 
/*     */   byte[] getTableBytes(int paramInt)
/*     */   {
/* 450 */     return null;
/*     */   }
/*     */ 
/*     */   protected long getUnitsPerEm()
/*     */   {
/* 455 */     return 2048L;
/*     */   }
/*     */ 
/*     */   boolean supportsEncoding(String paramString) {
/* 459 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean canDoStyle(int paramInt) {
/* 463 */     return paramInt == this.style;
/*     */   }
/*     */ 
/*     */   public boolean useAAForPtSize(int paramInt)
/*     */   {
/* 471 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hasSupplementaryChars() {
/* 475 */     return false;
/*     */   }
/*     */ 
/*     */   public String getPostscriptName()
/*     */   {
/* 480 */     return this.fullName;
/*     */   }
/*     */ 
/*     */   public String getFontName(Locale paramLocale) {
/* 484 */     return this.fullName;
/*     */   }
/*     */ 
/*     */   public String getFamilyName(Locale paramLocale) {
/* 488 */     return this.familyName;
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs() {
/* 492 */     return getMapper().getNumGlyphs();
/*     */   }
/*     */ 
/*     */   public int charToGlyph(int paramInt) {
/* 496 */     return getMapper().charToGlyph(paramInt);
/*     */   }
/*     */ 
/*     */   public int getMissingGlyphCode() {
/* 500 */     return getMapper().getMissingGlyphCode();
/*     */   }
/*     */ 
/*     */   public boolean canDisplay(char paramChar) {
/* 504 */     return getMapper().canDisplay(paramChar);
/*     */   }
/*     */ 
/*     */   public boolean canDisplay(int paramInt) {
/* 508 */     return getMapper().canDisplay(paramInt);
/*     */   }
/*     */ 
/*     */   public byte getBaselineFor(char paramChar) {
/* 512 */     return 0;
/*     */   }
/*     */ 
/*     */   public float getItalicAngle(Font paramFont, AffineTransform paramAffineTransform, Object paramObject1, Object paramObject2)
/*     */   {
/* 520 */     int i = FontStrikeDesc.getAAHintIntVal(paramObject1, this, 12);
/* 521 */     int j = FontStrikeDesc.getFMHintIntVal(paramObject2);
/* 522 */     FontStrike localFontStrike = getStrike(paramFont, paramAffineTransform, i, j);
/* 523 */     StrikeMetrics localStrikeMetrics = localFontStrike.getFontMetrics();
/* 524 */     if ((localStrikeMetrics.ascentY == 0.0F) || (localStrikeMetrics.ascentX == 0.0F)) {
/* 525 */       return 0.0F;
/*     */     }
/*     */ 
/* 530 */     return localStrikeMetrics.ascentX / -localStrikeMetrics.ascentY;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.Font2D
 * JD-Core Version:    0.6.2
 */