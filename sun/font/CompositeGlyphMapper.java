/*     */ package sun.font;
/*     */ 
/*     */ public final class CompositeGlyphMapper extends CharToGlyphMapper
/*     */ {
/*     */   public static final int SLOTMASK = -16777216;
/*     */   public static final int GLYPHMASK = 16777215;
/*     */   public static final int NBLOCKS = 216;
/*     */   public static final int BLOCKSZ = 256;
/*     */   public static final int MAXUNICODE = 55296;
/*     */   CompositeFont font;
/*     */   CharToGlyphMapper[] slotMappers;
/*     */   int[][] glyphMaps;
/*     */   private boolean hasExcludes;
/*     */ 
/*     */   public CompositeGlyphMapper(CompositeFont paramCompositeFont)
/*     */   {
/*  61 */     this.font = paramCompositeFont;
/*  62 */     initMapper();
/*     */ 
/*  66 */     this.hasExcludes = ((paramCompositeFont.exclusionRanges != null) && (paramCompositeFont.maxIndices != null));
/*     */   }
/*     */ 
/*     */   public final int compositeGlyphCode(int paramInt1, int paramInt2)
/*     */   {
/*  71 */     return paramInt1 << 24 | paramInt2 & 0xFFFFFF;
/*     */   }
/*     */ 
/*     */   private final void initMapper() {
/*  75 */     if (this.missingGlyph == -1) {
/*  76 */       if (this.glyphMaps == null) {
/*  77 */         this.glyphMaps = new int['Ø'][];
/*     */       }
/*  79 */       this.slotMappers = new CharToGlyphMapper[this.font.numSlots];
/*     */ 
/*  81 */       this.missingGlyph = this.font.getSlotFont(0).getMissingGlyphCode();
/*  82 */       this.missingGlyph = compositeGlyphCode(0, this.missingGlyph);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getCachedGlyphCode(int paramInt) {
/*  87 */     if (paramInt >= 55296)
/*  88 */       return -1;
/*     */     int[] arrayOfInt;
/*  91 */     if ((arrayOfInt = this.glyphMaps[(paramInt >> 8)]) == null) {
/*  92 */       return -1;
/*     */     }
/*  94 */     return arrayOfInt[(paramInt & 0xFF)];
/*     */   }
/*     */ 
/*     */   private void setCachedGlyphCode(int paramInt1, int paramInt2) {
/*  98 */     if (paramInt1 >= 55296) {
/*  99 */       return;
/*     */     }
/* 101 */     int i = paramInt1 >> 8;
/* 102 */     if (this.glyphMaps[i] == null) {
/* 103 */       this.glyphMaps[i] = new int[256];
/* 104 */       for (int j = 0; j < 256; j++) {
/* 105 */         this.glyphMaps[i][j] = -1;
/*     */       }
/*     */     }
/* 108 */     this.glyphMaps[i][(paramInt1 & 0xFF)] = paramInt2;
/*     */   }
/*     */ 
/*     */   private final CharToGlyphMapper getSlotMapper(int paramInt) {
/* 112 */     CharToGlyphMapper localCharToGlyphMapper = this.slotMappers[paramInt];
/* 113 */     if (localCharToGlyphMapper == null) {
/* 114 */       localCharToGlyphMapper = this.font.getSlotFont(paramInt).getMapper();
/* 115 */       this.slotMappers[paramInt] = localCharToGlyphMapper;
/*     */     }
/* 117 */     return localCharToGlyphMapper;
/*     */   }
/*     */ 
/*     */   private final int convertToGlyph(int paramInt)
/*     */   {
/* 122 */     for (int i = 0; i < this.font.numSlots; i++) {
/* 123 */       if ((!this.hasExcludes) || (!this.font.isExcludedChar(i, paramInt))) {
/* 124 */         CharToGlyphMapper localCharToGlyphMapper = getSlotMapper(i);
/* 125 */         int j = localCharToGlyphMapper.charToGlyph(paramInt);
/* 126 */         if (j != localCharToGlyphMapper.getMissingGlyphCode()) {
/* 127 */           j = compositeGlyphCode(i, j);
/* 128 */           setCachedGlyphCode(paramInt, j);
/* 129 */           return j;
/*     */         }
/*     */       }
/*     */     }
/* 133 */     return this.missingGlyph;
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs() {
/* 137 */     int i = 0;
/*     */ 
/* 147 */     for (int j = 0; j < 1; j++) {
/* 148 */       CharToGlyphMapper localCharToGlyphMapper = this.slotMappers[j];
/* 149 */       if (localCharToGlyphMapper == null) {
/* 150 */         localCharToGlyphMapper = this.font.getSlotFont(j).getMapper();
/* 151 */         this.slotMappers[j] = localCharToGlyphMapper;
/*     */       }
/* 153 */       i += localCharToGlyphMapper.getNumGlyphs();
/*     */     }
/* 155 */     return i;
/*     */   }
/*     */ 
/*     */   public int charToGlyph(int paramInt)
/*     */   {
/* 160 */     int i = getCachedGlyphCode(paramInt);
/* 161 */     if (i == -1) {
/* 162 */       i = convertToGlyph(paramInt);
/*     */     }
/* 164 */     return i;
/*     */   }
/*     */ 
/*     */   public int charToGlyph(int paramInt1, int paramInt2) {
/* 168 */     if (paramInt2 >= 0) {
/* 169 */       CharToGlyphMapper localCharToGlyphMapper = getSlotMapper(paramInt2);
/* 170 */       int i = localCharToGlyphMapper.charToGlyph(paramInt1);
/* 171 */       if (i != localCharToGlyphMapper.getMissingGlyphCode()) {
/* 172 */         return compositeGlyphCode(paramInt2, i);
/*     */       }
/*     */     }
/* 175 */     return charToGlyph(paramInt1);
/*     */   }
/*     */ 
/*     */   public int charToGlyph(char paramChar)
/*     */   {
/* 180 */     int i = getCachedGlyphCode(paramChar);
/* 181 */     if (i == -1) {
/* 182 */       i = convertToGlyph(paramChar);
/*     */     }
/* 184 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean charsToGlyphsNS(int paramInt, char[] paramArrayOfChar, int[] paramArrayOfInt)
/*     */   {
/* 194 */     for (int i = 0; i < paramInt; i++) {
/* 195 */       int j = paramArrayOfChar[i];
/*     */ 
/* 197 */       if ((j >= 55296) && (j <= 56319) && (i < paramInt - 1))
/*     */       {
/* 199 */         k = paramArrayOfChar[(i + 1)];
/*     */ 
/* 201 */         if ((k >= 56320) && (k <= 57343))
/*     */         {
/* 203 */           j = (j - 55296) * 1024 + k - 56320 + 65536;
/*     */ 
/* 205 */           paramArrayOfInt[(i + 1)] = 65535;
/*     */         }
/*     */       }
/*     */ 
/* 209 */       int k = paramArrayOfInt[i] = getCachedGlyphCode(j);
/* 210 */       if (k == -1) {
/* 211 */         paramArrayOfInt[i] = convertToGlyph(j);
/*     */       }
/*     */ 
/* 214 */       if (j >= 768)
/*     */       {
/* 217 */         if (FontUtilities.isComplexCharCode(j)) {
/* 218 */           return true;
/*     */         }
/* 220 */         if (j >= 65536) {
/* 221 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 226 */     return false;
/*     */   }
/*     */ 
/*     */   public void charsToGlyphs(int paramInt, char[] paramArrayOfChar, int[] paramArrayOfInt)
/*     */   {
/* 233 */     for (int i = 0; i < paramInt; i++) {
/* 234 */       int j = paramArrayOfChar[i];
/*     */ 
/* 236 */       if ((j >= 55296) && (j <= 56319) && (i < paramInt - 1))
/*     */       {
/* 238 */         k = paramArrayOfChar[(i + 1)];
/*     */ 
/* 240 */         if ((k >= 56320) && (k <= 57343))
/*     */         {
/* 242 */           j = (j - 55296) * 1024 + k - 56320 + 65536;
/*     */ 
/* 245 */           int m = paramArrayOfInt[i] = getCachedGlyphCode(j);
/* 246 */           if (m == -1) {
/* 247 */             paramArrayOfInt[i] = convertToGlyph(j);
/*     */           }
/* 249 */           i++;
/* 250 */           paramArrayOfInt[i] = 65535;
/* 251 */           continue;
/*     */         }
/*     */       }
/*     */ 
/* 255 */       int k = paramArrayOfInt[i] = getCachedGlyphCode(j);
/* 256 */       if (k == -1)
/* 257 */         paramArrayOfInt[i] = convertToGlyph(j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void charsToGlyphs(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 263 */     for (int i = 0; i < paramInt; i++) {
/* 264 */       int j = paramArrayOfInt1[i];
/*     */ 
/* 266 */       paramArrayOfInt2[i] = getCachedGlyphCode(j);
/* 267 */       if (paramArrayOfInt2[i] == -1)
/* 268 */         paramArrayOfInt2[i] = convertToGlyph(j);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.CompositeGlyphMapper
 * JD-Core Version:    0.6.2
 */