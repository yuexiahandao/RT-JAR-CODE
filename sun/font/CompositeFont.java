/*     */ package sun.font;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public final class CompositeFont extends Font2D
/*     */ {
/*     */   private boolean[] deferredInitialisation;
/*     */   String[] componentFileNames;
/*     */   String[] componentNames;
/*     */   private PhysicalFont[] components;
/*     */   int numSlots;
/*     */   int numMetricsSlots;
/*     */   int[] exclusionRanges;
/*     */   int[] maxIndices;
/*  54 */   int numGlyphs = 0;
/*  55 */   int localeSlot = -1;
/*     */ 
/*  58 */   boolean isStdComposite = true;
/*     */ 
/*     */   public CompositeFont(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean, SunFontManager paramSunFontManager)
/*     */   {
/*  65 */     this.handle = new Font2DHandle(this);
/*  66 */     this.fullName = paramString;
/*  67 */     this.componentFileNames = paramArrayOfString1;
/*  68 */     this.componentNames = paramArrayOfString2;
/*  69 */     if (paramArrayOfString2 == null)
/*  70 */       this.numSlots = this.componentFileNames.length;
/*     */     else {
/*  72 */       this.numSlots = this.componentNames.length;
/*     */     }
/*     */ 
/*  78 */     this.numMetricsSlots = paramInt;
/*  79 */     this.exclusionRanges = paramArrayOfInt1;
/*  80 */     this.maxIndices = paramArrayOfInt2;
/*     */ 
/*  88 */     if (paramSunFontManager.getEUDCFont() != null) {
/*  89 */       this.numSlots += 1;
/*  90 */       if (this.componentNames != null) {
/*  91 */         this.componentNames = new String[this.numSlots];
/*  92 */         System.arraycopy(paramArrayOfString2, 0, this.componentNames, 0, this.numSlots - 1);
/*  93 */         this.componentNames[(this.numSlots - 1)] = paramSunFontManager.getEUDCFont().getFontName(null);
/*     */       }
/*     */ 
/*  96 */       if (this.componentFileNames != null) {
/*  97 */         this.componentFileNames = new String[this.numSlots];
/*  98 */         System.arraycopy(paramArrayOfString1, 0, this.componentFileNames, 0, this.numSlots - 1);
/*     */       }
/*     */ 
/* 101 */       this.components = new PhysicalFont[this.numSlots];
/* 102 */       this.components[(this.numSlots - 1)] = paramSunFontManager.getEUDCFont();
/* 103 */       this.deferredInitialisation = new boolean[this.numSlots];
/* 104 */       if (paramBoolean)
/* 105 */         for (i = 0; i < this.numSlots - 1; i++)
/* 106 */           this.deferredInitialisation[i] = true;
/*     */     }
/*     */     else
/*     */     {
/* 110 */       this.components = new PhysicalFont[this.numSlots];
/* 111 */       this.deferredInitialisation = new boolean[this.numSlots];
/* 112 */       if (paramBoolean) {
/* 113 */         for (i = 0; i < this.numSlots; i++) {
/* 114 */           this.deferredInitialisation[i] = true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 119 */     this.fontRank = 2;
/*     */ 
/* 121 */     int i = this.fullName.indexOf('.');
/* 122 */     if (i > 0) {
/* 123 */       this.familyName = this.fullName.substring(0, i);
/*     */ 
/* 128 */       if (i + 1 < this.fullName.length()) {
/* 129 */         String str = this.fullName.substring(i + 1);
/* 130 */         if ("plain".equals(str))
/* 131 */           this.style = 0;
/* 132 */         else if ("bold".equals(str))
/* 133 */           this.style = 1;
/* 134 */         else if ("italic".equals(str))
/* 135 */           this.style = 2;
/* 136 */         else if ("bolditalic".equals(str))
/* 137 */           this.style = 3;
/*     */       }
/*     */     }
/*     */     else {
/* 141 */       this.familyName = this.fullName;
/*     */     }
/*     */   }
/*     */ 
/*     */   CompositeFont(PhysicalFont paramPhysicalFont, CompositeFont paramCompositeFont)
/*     */   {
/* 152 */     this.isStdComposite = false;
/* 153 */     this.handle = new Font2DHandle(this);
/* 154 */     this.fullName = paramPhysicalFont.fullName;
/* 155 */     this.familyName = paramPhysicalFont.familyName;
/* 156 */     this.style = paramPhysicalFont.style;
/*     */ 
/* 158 */     this.numMetricsSlots = 1;
/* 159 */     paramCompositeFont.numSlots += 1;
/*     */ 
/* 168 */     synchronized (FontManagerFactory.getInstance()) {
/* 169 */       this.components = new PhysicalFont[this.numSlots];
/* 170 */       this.components[0] = paramPhysicalFont;
/* 171 */       System.arraycopy(paramCompositeFont.components, 0, this.components, 1, paramCompositeFont.numSlots);
/*     */ 
/* 174 */       if (paramCompositeFont.componentNames != null) {
/* 175 */         this.componentNames = new String[this.numSlots];
/* 176 */         this.componentNames[0] = paramPhysicalFont.fullName;
/* 177 */         System.arraycopy(paramCompositeFont.componentNames, 0, this.componentNames, 1, paramCompositeFont.numSlots);
/*     */       }
/*     */ 
/* 180 */       if (paramCompositeFont.componentFileNames != null) {
/* 181 */         this.componentFileNames = new String[this.numSlots];
/* 182 */         this.componentFileNames[0] = null;
/* 183 */         System.arraycopy(paramCompositeFont.componentFileNames, 0, this.componentFileNames, 1, paramCompositeFont.numSlots);
/*     */       }
/*     */ 
/* 186 */       this.deferredInitialisation = new boolean[this.numSlots];
/* 187 */       this.deferredInitialisation[0] = false;
/* 188 */       System.arraycopy(paramCompositeFont.deferredInitialisation, 0, this.deferredInitialisation, 1, paramCompositeFont.numSlots);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void doDeferredInitialisation(int paramInt)
/*     */   {
/* 229 */     if (this.deferredInitialisation[paramInt] == 0) {
/* 230 */       return;
/*     */     }
/*     */ 
/* 238 */     SunFontManager localSunFontManager = SunFontManager.getInstance();
/* 239 */     synchronized (localSunFontManager) {
/* 240 */       if (this.componentNames == null) {
/* 241 */         this.componentNames = new String[this.numSlots];
/*     */       }
/* 243 */       if (this.components[paramInt] == null)
/*     */       {
/* 253 */         if ((this.componentFileNames != null) && (this.componentFileNames[paramInt] != null))
/*     */         {
/* 255 */           this.components[paramInt] = localSunFontManager.initialiseDeferredFont(this.componentFileNames[paramInt]);
/*     */         }
/*     */ 
/* 259 */         if (this.components[paramInt] == null) {
/* 260 */           this.components[paramInt] = localSunFontManager.getDefaultPhysicalFont();
/*     */         }
/* 262 */         String str = this.components[paramInt].getFontName(null);
/* 263 */         if (this.componentNames[paramInt] == null)
/* 264 */           this.componentNames[paramInt] = str;
/* 265 */         else if (!this.componentNames[paramInt].equalsIgnoreCase(str)) {
/* 266 */           this.components[paramInt] = ((PhysicalFont)localSunFontManager.findFont2D(this.componentNames[paramInt], this.style, 1));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 272 */       this.deferredInitialisation[paramInt] = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   void replaceComponentFont(PhysicalFont paramPhysicalFont1, PhysicalFont paramPhysicalFont2)
/*     */   {
/* 278 */     if (this.components == null) {
/* 279 */       return;
/*     */     }
/* 281 */     for (int i = 0; i < this.numSlots; i++)
/* 282 */       if (this.components[i] == paramPhysicalFont1) {
/* 283 */         this.components[i] = paramPhysicalFont2;
/* 284 */         if (this.componentNames != null)
/* 285 */           this.componentNames[i] = paramPhysicalFont2.getFontName(null);
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean isExcludedChar(int paramInt1, int paramInt2)
/*     */   {
/* 293 */     if ((this.exclusionRanges == null) || (this.maxIndices == null) || (paramInt1 >= this.numMetricsSlots))
/*     */     {
/* 295 */       return false;
/*     */     }
/*     */ 
/* 298 */     int i = 0;
/* 299 */     int j = this.maxIndices[paramInt1];
/* 300 */     if (paramInt1 > 0) {
/* 301 */       i = this.maxIndices[(paramInt1 - 1)];
/*     */     }
/* 303 */     int k = i;
/* 304 */     while (j > k) {
/* 305 */       if ((paramInt2 >= this.exclusionRanges[k]) && (paramInt2 <= this.exclusionRanges[(k + 1)]))
/*     */       {
/* 307 */         return true;
/*     */       }
/* 309 */       k += 2;
/*     */     }
/* 311 */     return false;
/*     */   }
/*     */ 
/*     */   public void getStyleMetrics(float paramFloat, float[] paramArrayOfFloat, int paramInt) {
/* 315 */     PhysicalFont localPhysicalFont = getSlotFont(0);
/* 316 */     if (localPhysicalFont == null)
/* 317 */       super.getStyleMetrics(paramFloat, paramArrayOfFloat, paramInt);
/*     */     else
/* 319 */       localPhysicalFont.getStyleMetrics(paramFloat, paramArrayOfFloat, paramInt);
/*     */   }
/*     */ 
/*     */   public int getNumSlots()
/*     */   {
/* 324 */     return this.numSlots;
/*     */   }
/*     */ 
/*     */   public PhysicalFont getSlotFont(int paramInt)
/*     */   {
/* 333 */     if (this.deferredInitialisation[paramInt] != 0) {
/* 334 */       doDeferredInitialisation(paramInt);
/*     */     }
/* 336 */     SunFontManager localSunFontManager = SunFontManager.getInstance();
/*     */     try {
/* 338 */       PhysicalFont localPhysicalFont = this.components[paramInt];
/* 339 */       if (localPhysicalFont == null) {
/*     */         try {
/* 341 */           localPhysicalFont = (PhysicalFont)localSunFontManager.findFont2D(this.componentNames[paramInt], this.style, 1);
/*     */ 
/* 344 */           this.components[paramInt] = localPhysicalFont;
/*     */         } catch (ClassCastException localClassCastException) {
/* 346 */           localPhysicalFont = localSunFontManager.getDefaultPhysicalFont();
/*     */         }
/*     */       }
/* 349 */       return localPhysicalFont; } catch (Exception localException) {
/*     */     }
/* 351 */     return localSunFontManager.getDefaultPhysicalFont();
/*     */   }
/*     */ 
/*     */   FontStrike createStrike(FontStrikeDesc paramFontStrikeDesc)
/*     */   {
/* 356 */     return new CompositeStrike(this, paramFontStrikeDesc);
/*     */   }
/*     */ 
/*     */   public boolean isStdComposite()
/*     */   {
/* 365 */     return this.isStdComposite;
/*     */   }
/*     */ 
/*     */   protected int getValidatedGlyphCode(int paramInt)
/*     */   {
/* 374 */     int i = paramInt >>> 24;
/* 375 */     if (i >= this.numSlots) {
/* 376 */       return getMapper().getMissingGlyphCode();
/*     */     }
/*     */ 
/* 379 */     int j = paramInt & 0xFFFFFF;
/* 380 */     PhysicalFont localPhysicalFont = getSlotFont(i);
/* 381 */     if (localPhysicalFont.getValidatedGlyphCode(j) == localPhysicalFont.getMissingGlyphCode())
/*     */     {
/* 383 */       return getMapper().getMissingGlyphCode();
/*     */     }
/* 385 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public CharToGlyphMapper getMapper()
/*     */   {
/* 390 */     if (this.mapper == null) {
/* 391 */       this.mapper = new CompositeGlyphMapper(this);
/*     */     }
/* 393 */     return this.mapper;
/*     */   }
/*     */ 
/*     */   public boolean hasSupplementaryChars() {
/* 397 */     for (int i = 0; i < this.numSlots; i++) {
/* 398 */       if (getSlotFont(i).hasSupplementaryChars()) {
/* 399 */         return true;
/*     */       }
/*     */     }
/* 402 */     return false;
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs() {
/* 406 */     if (this.numGlyphs == 0) {
/* 407 */       this.numGlyphs = getMapper().getNumGlyphs();
/*     */     }
/* 409 */     return this.numGlyphs;
/*     */   }
/*     */ 
/*     */   public int getMissingGlyphCode() {
/* 413 */     return getMapper().getMissingGlyphCode();
/*     */   }
/*     */ 
/*     */   public boolean canDisplay(char paramChar) {
/* 417 */     return getMapper().canDisplay(paramChar);
/*     */   }
/*     */ 
/*     */   public boolean useAAForPtSize(int paramInt)
/*     */   {
/* 427 */     if (this.localeSlot == -1)
/*     */     {
/* 432 */       int i = this.numMetricsSlots;
/* 433 */       if ((i == 1) && (!isStdComposite())) {
/* 434 */         i = this.numSlots;
/*     */       }
/* 436 */       for (int j = 0; j < i; j++) {
/* 437 */         if (getSlotFont(j).supportsEncoding(null)) {
/* 438 */           this.localeSlot = j;
/* 439 */           break;
/*     */         }
/*     */       }
/* 442 */       if (this.localeSlot == -1) {
/* 443 */         this.localeSlot = 0;
/*     */       }
/*     */     }
/* 446 */     return getSlotFont(this.localeSlot).useAAForPtSize(paramInt);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 450 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("line.separator"));
/*     */ 
/* 452 */     String str2 = "";
/* 453 */     for (int i = 0; i < this.numSlots; i++) {
/* 454 */       str2 = str2 + "    Slot[" + i + "]=" + getSlotFont(i) + str1;
/*     */     }
/* 456 */     return "** Composite Font: Family=" + this.familyName + " Name=" + this.fullName + " style=" + this.style + str1 + str2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.CompositeFont
 * JD-Core Version:    0.6.2
 */