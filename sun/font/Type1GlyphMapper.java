/*     */ package sun.font;
/*     */ 
/*     */ public final class Type1GlyphMapper extends CharToGlyphMapper
/*     */ {
/*     */   Type1Font font;
/*     */   FontScaler scaler;
/*     */ 
/*     */   public Type1GlyphMapper(Type1Font paramType1Font)
/*     */   {
/*  40 */     this.font = paramType1Font;
/*  41 */     initMapper();
/*     */   }
/*     */ 
/*     */   private void initMapper() {
/*  45 */     this.scaler = this.font.getScaler();
/*     */     try {
/*  47 */       this.missingGlyph = this.scaler.getMissingGlyphCode();
/*     */     } catch (FontScalerException localFontScalerException1) {
/*  49 */       this.scaler = FontScaler.getNullScaler();
/*     */       try {
/*  51 */         this.missingGlyph = this.scaler.getMissingGlyphCode();
/*     */       } catch (FontScalerException localFontScalerException2) {
/*  53 */         this.missingGlyph = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs() {
/*     */     try {
/*  60 */       return this.scaler.getNumGlyphs();
/*     */     } catch (FontScalerException localFontScalerException) {
/*  62 */       this.scaler = FontScaler.getNullScaler();
/*  63 */     }return getNumGlyphs();
/*     */   }
/*     */ 
/*     */   public int getMissingGlyphCode()
/*     */   {
/*  68 */     return this.missingGlyph;
/*     */   }
/*     */ 
/*     */   public boolean canDisplay(char paramChar) {
/*     */     try {
/*  73 */       return this.scaler.getGlyphCode(paramChar) != this.missingGlyph;
/*     */     } catch (FontScalerException localFontScalerException) {
/*  75 */       this.scaler = FontScaler.getNullScaler();
/*  76 */     }return canDisplay(paramChar);
/*     */   }
/*     */ 
/*     */   public int charToGlyph(char paramChar)
/*     */   {
/*     */     try {
/*  82 */       return this.scaler.getGlyphCode(paramChar);
/*     */     } catch (FontScalerException localFontScalerException) {
/*  84 */       this.scaler = FontScaler.getNullScaler();
/*  85 */     }return charToGlyph(paramChar);
/*     */   }
/*     */ 
/*     */   public int charToGlyph(int paramInt)
/*     */   {
/*  90 */     if ((paramInt < 0) || (paramInt > 65535))
/*  91 */       return this.missingGlyph;
/*     */     try
/*     */     {
/*  94 */       return this.scaler.getGlyphCode((char)paramInt);
/*     */     } catch (FontScalerException localFontScalerException) {
/*  96 */       this.scaler = FontScaler.getNullScaler();
/*  97 */     }return charToGlyph(paramInt);
/*     */   }
/*     */ 
/*     */   public void charsToGlyphs(int paramInt, char[] paramArrayOfChar, int[] paramArrayOfInt)
/*     */   {
/* 107 */     for (int i = 0; i < paramInt; i++) {
/* 108 */       int j = paramArrayOfChar[i];
/*     */ 
/* 110 */       if ((j >= 55296) && (j <= 56319) && (i < paramInt - 1))
/*     */       {
/* 112 */         int k = paramArrayOfChar[(i + 1)];
/*     */ 
/* 114 */         if ((k >= 56320) && (k <= 57343))
/*     */         {
/* 116 */           j = (j - 55296) * 1024 + k - 56320 + 65536;
/*     */ 
/* 118 */           paramArrayOfInt[(i + 1)] = 65535;
/*     */         }
/*     */       }
/* 121 */       paramArrayOfInt[i] = charToGlyph(j);
/* 122 */       if (j >= 65536)
/* 123 */         i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void charsToGlyphs(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 133 */     for (int i = 0; i < paramInt; i++)
/* 134 */       paramArrayOfInt2[i] = charToGlyph(paramArrayOfInt1[i]);
/*     */   }
/*     */ 
/*     */   public boolean charsToGlyphsNS(int paramInt, char[] paramArrayOfChar, int[] paramArrayOfInt)
/*     */   {
/* 146 */     for (int i = 0; i < paramInt; i++) {
/* 147 */       int j = paramArrayOfChar[i];
/*     */ 
/* 149 */       if ((j >= 55296) && (j <= 56319) && (i < paramInt - 1))
/*     */       {
/* 151 */         int k = paramArrayOfChar[(i + 1)];
/*     */ 
/* 153 */         if ((k >= 56320) && (k <= 57343))
/*     */         {
/* 155 */           j = (j - 55296) * 1024 + k - 56320 + 65536;
/*     */ 
/* 157 */           paramArrayOfInt[(i + 1)] = 65535;
/*     */         }
/*     */       }
/*     */ 
/* 161 */       paramArrayOfInt[i] = charToGlyph(j);
/*     */ 
/* 163 */       if (j >= 768)
/*     */       {
/* 166 */         if (FontUtilities.isComplexCharCode(j)) {
/* 167 */           return true;
/*     */         }
/* 169 */         if (j >= 65536) {
/* 170 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 175 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.Type1GlyphMapper
 * JD-Core Version:    0.6.2
 */