/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.LineMetrics;
/*     */ 
/*     */ public class StandardTextSource extends TextSource
/*     */ {
/*     */   char[] chars;
/*     */   int start;
/*     */   int len;
/*     */   int cstart;
/*     */   int clen;
/*     */   int level;
/*     */   int flags;
/*     */   Font font;
/*     */   FontRenderContext frc;
/*     */   CoreMetrics cm;
/*     */ 
/*     */   public StandardTextSource(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Font paramFont, FontRenderContext paramFontRenderContext, CoreMetrics paramCoreMetrics)
/*     */   {
/*  72 */     if (paramArrayOfChar == null) {
/*  73 */       throw new IllegalArgumentException("bad chars: null");
/*     */     }
/*  75 */     if (paramInt3 < 0) {
/*  76 */       throw new IllegalArgumentException("bad cstart: " + paramInt3);
/*     */     }
/*  78 */     if (paramInt1 < paramInt3) {
/*  79 */       throw new IllegalArgumentException("bad start: " + paramInt1 + " for cstart: " + paramInt3);
/*     */     }
/*  81 */     if (paramInt4 < 0) {
/*  82 */       throw new IllegalArgumentException("bad clen: " + paramInt4);
/*     */     }
/*  84 */     if (paramInt3 + paramInt4 > paramArrayOfChar.length) {
/*  85 */       throw new IllegalArgumentException("bad clen: " + paramInt4 + " cstart: " + paramInt3 + " for array len: " + paramArrayOfChar.length);
/*     */     }
/*  87 */     if (paramInt2 < 0) {
/*  88 */       throw new IllegalArgumentException("bad len: " + paramInt2);
/*     */     }
/*  90 */     if (paramInt1 + paramInt2 > paramInt3 + paramInt4) {
/*  91 */       throw new IllegalArgumentException("bad len: " + paramInt2 + " start: " + paramInt1 + " for cstart: " + paramInt3 + " clen: " + paramInt4);
/*     */     }
/*  93 */     if (paramFont == null) {
/*  94 */       throw new IllegalArgumentException("bad font: null");
/*     */     }
/*  96 */     if (paramFontRenderContext == null) {
/*  97 */       throw new IllegalArgumentException("bad frc: null");
/*     */     }
/*     */ 
/* 100 */     this.chars = paramArrayOfChar;
/* 101 */     this.start = paramInt1;
/* 102 */     this.len = paramInt2;
/* 103 */     this.cstart = paramInt3;
/* 104 */     this.clen = paramInt4;
/* 105 */     this.level = paramInt5;
/* 106 */     this.flags = paramInt6;
/* 107 */     this.font = paramFont;
/* 108 */     this.frc = paramFontRenderContext;
/*     */ 
/* 110 */     if (paramCoreMetrics != null) {
/* 111 */       this.cm = paramCoreMetrics;
/*     */     } else {
/* 113 */       LineMetrics localLineMetrics = paramFont.getLineMetrics(paramArrayOfChar, paramInt3, paramInt4, paramFontRenderContext);
/* 114 */       this.cm = ((FontLineMetrics)localLineMetrics).cm;
/*     */     }
/*     */   }
/*     */ 
/*     */   public StandardTextSource(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Font paramFont, FontRenderContext paramFontRenderContext, CoreMetrics paramCoreMetrics)
/*     */   {
/* 127 */     this(paramArrayOfChar, paramInt1, paramInt2, paramInt1, paramInt2, paramInt3, paramInt4, paramFont, paramFontRenderContext, paramCoreMetrics);
/*     */   }
/*     */ 
/*     */   public StandardTextSource(char[] paramArrayOfChar, int paramInt1, int paramInt2, Font paramFont, FontRenderContext paramFontRenderContext)
/*     */   {
/* 136 */     this(paramArrayOfChar, 0, paramArrayOfChar.length, 0, paramArrayOfChar.length, paramInt1, paramInt2, paramFont, paramFontRenderContext, null);
/*     */   }
/*     */ 
/*     */   public StandardTextSource(String paramString, int paramInt1, int paramInt2, Font paramFont, FontRenderContext paramFontRenderContext)
/*     */   {
/* 145 */     this(paramString.toCharArray(), 0, paramString.length(), 0, paramString.length(), paramInt1, paramInt2, paramFont, paramFontRenderContext, null);
/*     */   }
/*     */ 
/*     */   public char[] getChars()
/*     */   {
/* 151 */     return this.chars;
/*     */   }
/*     */ 
/*     */   public int getStart() {
/* 155 */     return this.start;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/* 159 */     return this.len;
/*     */   }
/*     */ 
/*     */   public int getContextStart() {
/* 163 */     return this.cstart;
/*     */   }
/*     */ 
/*     */   public int getContextLength() {
/* 167 */     return this.clen;
/*     */   }
/*     */ 
/*     */   public int getLayoutFlags() {
/* 171 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public int getBidiLevel() {
/* 175 */     return this.level;
/*     */   }
/*     */ 
/*     */   public Font getFont() {
/* 179 */     return this.font;
/*     */   }
/*     */ 
/*     */   public FontRenderContext getFRC() {
/* 183 */     return this.frc;
/*     */   }
/*     */ 
/*     */   public CoreMetrics getCoreMetrics() {
/* 187 */     return this.cm;
/*     */   }
/*     */ 
/*     */   public TextSource getSubSource(int paramInt1, int paramInt2, int paramInt3) {
/* 191 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > this.len)) {
/* 192 */       throw new IllegalArgumentException("bad start (" + paramInt1 + ") or length (" + paramInt2 + ")");
/*     */     }
/*     */ 
/* 195 */     int i = this.level;
/* 196 */     if (paramInt3 != 2) {
/* 197 */       int j = (this.flags & 0x8) == 0 ? 1 : 0;
/* 198 */       if (((paramInt3 != 0) || (j == 0)) && ((paramInt3 != 1) || (j != 0)))
/*     */       {
/* 200 */         throw new IllegalArgumentException("direction flag is invalid");
/*     */       }
/* 202 */       i = j != 0 ? 0 : 1;
/*     */     }
/*     */ 
/* 205 */     return new StandardTextSource(this.chars, this.start + paramInt1, paramInt2, this.cstart, this.clen, i, this.flags, this.font, this.frc, this.cm);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 209 */     return toString(true);
/*     */   }
/*     */ 
/*     */   public String toString(boolean paramBoolean) {
/* 213 */     StringBuffer localStringBuffer = new StringBuffer(super.toString());
/* 214 */     localStringBuffer.append("[start:");
/* 215 */     localStringBuffer.append(this.start);
/* 216 */     localStringBuffer.append(", len:");
/* 217 */     localStringBuffer.append(this.len);
/* 218 */     localStringBuffer.append(", cstart:");
/* 219 */     localStringBuffer.append(this.cstart);
/* 220 */     localStringBuffer.append(", clen:");
/* 221 */     localStringBuffer.append(this.clen);
/* 222 */     localStringBuffer.append(", chars:\"");
/*     */     int i;
/*     */     int j;
/* 224 */     if (paramBoolean == true) {
/* 225 */       i = this.cstart;
/* 226 */       j = this.cstart + this.clen;
/*     */     }
/*     */     else {
/* 229 */       i = this.start;
/* 230 */       j = this.start + this.len;
/*     */     }
/* 232 */     for (int k = i; k < j; k++) {
/* 233 */       if (k > i) {
/* 234 */         localStringBuffer.append(" ");
/*     */       }
/* 236 */       localStringBuffer.append(Integer.toHexString(this.chars[k]));
/*     */     }
/* 238 */     localStringBuffer.append("\"");
/* 239 */     localStringBuffer.append(", level:");
/* 240 */     localStringBuffer.append(this.level);
/* 241 */     localStringBuffer.append(", flags:");
/* 242 */     localStringBuffer.append(this.flags);
/* 243 */     localStringBuffer.append(", font:");
/* 244 */     localStringBuffer.append(this.font);
/* 245 */     localStringBuffer.append(", frc:");
/* 246 */     localStringBuffer.append(this.frc);
/* 247 */     localStringBuffer.append(", cm:");
/* 248 */     localStringBuffer.append(this.cm);
/* 249 */     localStringBuffer.append("]");
/*     */ 
/* 251 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.StandardTextSource
 * JD-Core Version:    0.6.2
 */