/*     */ package sun.font;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Locale;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class TrueTypeGlyphMapper extends CharToGlyphMapper
/*     */ {
/*     */   static final char REVERSE_SOLIDUS = '\\';
/*     */   static final char JA_YEN = '¥';
/*     */   static final char JA_FULLWIDTH_TILDE_CHAR = '～';
/*     */   static final char JA_WAVE_DASH_CHAR = '〜';
/*  42 */   static final boolean isJAlocale = Locale.JAPAN.equals(Locale.getDefault());
/*     */   private final boolean needsJAremapping;
/*     */   private boolean remapJAWaveDash;
/*     */   TrueTypeFont font;
/*     */   CMap cmap;
/*     */   int numGlyphs;
/*     */ 
/*     */   public TrueTypeGlyphMapper(TrueTypeFont paramTrueTypeFont)
/*     */   {
/*  51 */     this.font = paramTrueTypeFont;
/*     */     try {
/*  53 */       this.cmap = CMap.initialize(paramTrueTypeFont);
/*     */     } catch (Exception localException) {
/*  55 */       this.cmap = null;
/*     */     }
/*  57 */     if (this.cmap == null) {
/*  58 */       handleBadCMAP();
/*     */     }
/*  60 */     this.missingGlyph = 0;
/*  61 */     ByteBuffer localByteBuffer = paramTrueTypeFont.getTableBuffer(1835104368);
/*  62 */     this.numGlyphs = localByteBuffer.getChar(4);
/*  63 */     if ((FontUtilities.isSolaris) && (isJAlocale) && (paramTrueTypeFont.supportsJA())) {
/*  64 */       this.needsJAremapping = true;
/*  65 */       if ((FontUtilities.isSolaris8) && (getGlyphFromCMAP(12316) == this.missingGlyph))
/*     */       {
/*  67 */         this.remapJAWaveDash = true;
/*     */       }
/*     */     } else {
/*  70 */       this.needsJAremapping = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs() {
/*  75 */     return this.numGlyphs;
/*     */   }
/*     */ 
/*     */   private char getGlyphFromCMAP(int paramInt) {
/*     */     try {
/*  80 */       int i = this.cmap.getGlyph(paramInt);
/*  81 */       if ((i < this.numGlyphs) || (i >= 65534))
/*     */       {
/*  83 */         return i;
/*     */       }
/*  85 */       if (FontUtilities.isLogging()) {
/*  86 */         FontUtilities.getLogger().warning(this.font + " out of range glyph id=" + Integer.toHexString(i) + " for char " + Integer.toHexString(paramInt));
/*     */       }
/*     */ 
/*  91 */       return (char)this.missingGlyph;
/*     */     }
/*     */     catch (Exception localException) {
/*  94 */       handleBadCMAP();
/*  95 */     }return (char)this.missingGlyph;
/*     */   }
/*     */ 
/*     */   private void handleBadCMAP()
/*     */   {
/* 100 */     if (FontUtilities.isLogging()) {
/* 101 */       FontUtilities.getLogger().severe("Null Cmap for " + this.font + "substituting for this font");
/*     */     }
/*     */ 
/* 104 */     SunFontManager.getInstance().deRegisterBadFont(this.font);
/*     */ 
/* 109 */     this.cmap = CMap.theNullCmap;
/*     */   }
/*     */ 
/*     */   private final char remapJAChar(char paramChar) {
/* 113 */     switch (paramChar) {
/*     */     case '\\':
/* 115 */       return '¥';
/*     */     case '〜':
/* 120 */       if (this.remapJAWaveDash)
/* 121 */         return 65374; break;
/*     */     }
/* 123 */     return paramChar;
/*     */   }
/*     */ 
/*     */   private final int remapJAIntChar(int paramInt) {
/* 127 */     switch (paramInt) {
/*     */     case 92:
/* 129 */       return 165;
/*     */     case 12316:
/* 134 */       if (this.remapJAWaveDash)
/* 135 */         return 65374; break;
/*     */     }
/* 137 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int charToGlyph(char paramChar)
/*     */   {
/* 142 */     if (this.needsJAremapping) {
/* 143 */       paramChar = remapJAChar(paramChar);
/*     */     }
/* 145 */     int i = getGlyphFromCMAP(paramChar);
/* 146 */     if ((this.font.checkUseNatives()) && (i < this.font.glyphToCharMap.length)) {
/* 147 */       this.font.glyphToCharMap[i] = paramChar;
/*     */     }
/* 149 */     return i;
/*     */   }
/*     */ 
/*     */   public int charToGlyph(int paramInt) {
/* 153 */     if (this.needsJAremapping) {
/* 154 */       paramInt = remapJAIntChar(paramInt);
/*     */     }
/* 156 */     int i = getGlyphFromCMAP(paramInt);
/* 157 */     if ((this.font.checkUseNatives()) && (i < this.font.glyphToCharMap.length)) {
/* 158 */       this.font.glyphToCharMap[i] = ((char)paramInt);
/*     */     }
/* 160 */     return i;
/*     */   }
/*     */ 
/*     */   public void charsToGlyphs(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
/* 164 */     for (int i = 0; i < paramInt; i++) {
/* 165 */       if (this.needsJAremapping)
/* 166 */         paramArrayOfInt2[i] = getGlyphFromCMAP(remapJAIntChar(paramArrayOfInt1[i]));
/*     */       else {
/* 168 */         paramArrayOfInt2[i] = getGlyphFromCMAP(paramArrayOfInt1[i]);
/*     */       }
/* 170 */       if ((this.font.checkUseNatives()) && (paramArrayOfInt2[i] < this.font.glyphToCharMap.length))
/*     */       {
/* 172 */         this.font.glyphToCharMap[paramArrayOfInt2[i]] = ((char)paramArrayOfInt1[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void charsToGlyphs(int paramInt, char[] paramArrayOfChar, int[] paramArrayOfInt)
/*     */   {
/* 179 */     for (int i = 0; i < paramInt; i++)
/*     */     {
/*     */       int j;
/* 181 */       if (this.needsJAremapping)
/* 182 */         j = remapJAChar(paramArrayOfChar[i]);
/*     */       else {
/* 184 */         j = paramArrayOfChar[i];
/*     */       }
/*     */ 
/* 187 */       if ((j >= 55296) && (j <= 56319) && (i < paramInt - 1))
/*     */       {
/* 189 */         int k = paramArrayOfChar[(i + 1)];
/*     */ 
/* 191 */         if ((k >= 56320) && (k <= 57343))
/*     */         {
/* 193 */           j = (j - 55296) * 1024 + k - 56320 + 65536;
/*     */ 
/* 196 */           paramArrayOfInt[i] = getGlyphFromCMAP(j);
/* 197 */           i++;
/* 198 */           paramArrayOfInt[i] = 65535;
/* 199 */           continue;
/*     */         }
/*     */       }
/* 202 */       paramArrayOfInt[i] = getGlyphFromCMAP(j);
/*     */ 
/* 204 */       if ((this.font.checkUseNatives()) && (paramArrayOfInt[i] < this.font.glyphToCharMap.length))
/*     */       {
/* 206 */         this.font.glyphToCharMap[paramArrayOfInt[i]] = ((char)j);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean charsToGlyphsNS(int paramInt, char[] paramArrayOfChar, int[] paramArrayOfInt)
/*     */   {
/* 219 */     for (int i = 0; i < paramInt; i++)
/*     */     {
/*     */       int j;
/* 221 */       if (this.needsJAremapping)
/* 222 */         j = remapJAChar(paramArrayOfChar[i]);
/*     */       else {
/* 224 */         j = paramArrayOfChar[i];
/*     */       }
/*     */ 
/* 227 */       if ((j >= 55296) && (j <= 56319) && (i < paramInt - 1))
/*     */       {
/* 229 */         int k = paramArrayOfChar[(i + 1)];
/*     */ 
/* 231 */         if ((k >= 56320) && (k <= 57343))
/*     */         {
/* 233 */           j = (j - 55296) * 1024 + k - 56320 + 65536;
/*     */ 
/* 235 */           paramArrayOfInt[(i + 1)] = 65535;
/*     */         }
/*     */       }
/*     */ 
/* 239 */       paramArrayOfInt[i] = getGlyphFromCMAP(j);
/* 240 */       if ((this.font.checkUseNatives()) && (paramArrayOfInt[i] < this.font.glyphToCharMap.length))
/*     */       {
/* 242 */         this.font.glyphToCharMap[paramArrayOfInt[i]] = ((char)j);
/*     */       }
/*     */ 
/* 245 */       if (j >= 768)
/*     */       {
/* 248 */         if (FontUtilities.isComplexCharCode(j)) {
/* 249 */           return true;
/*     */         }
/* 251 */         if (j >= 65536) {
/* 252 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */   boolean hasSupplementaryChars()
/*     */   {
/* 264 */     return ((this.cmap instanceof CMap.CMapFormat8)) || ((this.cmap instanceof CMap.CMapFormat10)) || ((this.cmap instanceof CMap.CMapFormat12));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.TrueTypeGlyphMapper
 * JD-Core Version:    0.6.2
 */