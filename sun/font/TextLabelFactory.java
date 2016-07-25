/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.text.Bidi;
/*     */ 
/*     */ public class TextLabelFactory
/*     */ {
/*     */   private FontRenderContext frc;
/*     */   private char[] text;
/*     */   private Bidi bidi;
/*     */   private Bidi lineBidi;
/*     */   private int flags;
/*     */   private int lineStart;
/*     */   private int lineLimit;
/*     */ 
/*     */   public TextLabelFactory(FontRenderContext paramFontRenderContext, char[] paramArrayOfChar, Bidi paramBidi, int paramInt)
/*     */   {
/*  72 */     this.frc = paramFontRenderContext;
/*  73 */     this.text = paramArrayOfChar;
/*  74 */     this.bidi = paramBidi;
/*  75 */     this.flags = paramInt;
/*  76 */     this.lineBidi = paramBidi;
/*  77 */     this.lineStart = 0;
/*  78 */     this.lineLimit = paramArrayOfChar.length;
/*     */   }
/*     */ 
/*     */   public FontRenderContext getFontRenderContext() {
/*  82 */     return this.frc;
/*     */   }
/*     */ 
/*     */   public char[] getText() {
/*  86 */     return this.text;
/*     */   }
/*     */ 
/*     */   public Bidi getParagraphBidi() {
/*  90 */     return this.bidi;
/*     */   }
/*     */ 
/*     */   public Bidi getLineBidi() {
/*  94 */     return this.lineBidi;
/*     */   }
/*     */ 
/*     */   public int getLayoutFlags() {
/*  98 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public int getLineStart() {
/* 102 */     return this.lineStart;
/*     */   }
/*     */ 
/*     */   public int getLineLimit() {
/* 106 */     return this.lineLimit;
/*     */   }
/*     */ 
/*     */   public void setLineContext(int paramInt1, int paramInt2)
/*     */   {
/* 116 */     this.lineStart = paramInt1;
/* 117 */     this.lineLimit = paramInt2;
/* 118 */     if (this.bidi != null)
/* 119 */       this.lineBidi = this.bidi.createLineBidi(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public ExtendedTextLabel createExtended(Font paramFont, CoreMetrics paramCoreMetrics, Decoration paramDecoration, int paramInt1, int paramInt2)
/*     */   {
/* 143 */     if ((paramInt1 >= paramInt2) || (paramInt1 < this.lineStart) || (paramInt2 > this.lineLimit)) {
/* 144 */       throw new IllegalArgumentException("bad start: " + paramInt1 + " or limit: " + paramInt2);
/*     */     }
/*     */ 
/* 147 */     int i = this.lineBidi == null ? 0 : this.lineBidi.getLevelAt(paramInt1 - this.lineStart);
/* 148 */     int j = (this.lineBidi == null) || (this.lineBidi.baseIsLeftToRight()) ? 0 : 1;
/* 149 */     int k = this.flags & 0xFFFFFFF6;
/* 150 */     if ((i & 0x1) != 0) k |= 1;
/* 151 */     if ((j & 0x1) != 0) k |= 8;
/*     */ 
/* 153 */     StandardTextSource localStandardTextSource = new StandardTextSource(this.text, paramInt1, paramInt2 - paramInt1, this.lineStart, this.lineLimit - this.lineStart, i, k, paramFont, this.frc, paramCoreMetrics);
/* 154 */     return new ExtendedTextSourceLabel(localStandardTextSource, paramDecoration);
/*     */   }
/*     */ 
/*     */   public TextLabel createSimple(Font paramFont, CoreMetrics paramCoreMetrics, int paramInt1, int paramInt2)
/*     */   {
/* 169 */     if ((paramInt1 >= paramInt2) || (paramInt1 < this.lineStart) || (paramInt2 > this.lineLimit)) {
/* 170 */       throw new IllegalArgumentException("bad start: " + paramInt1 + " or limit: " + paramInt2);
/*     */     }
/*     */ 
/* 173 */     int i = this.lineBidi == null ? 0 : this.lineBidi.getLevelAt(paramInt1 - this.lineStart);
/* 174 */     int j = (this.lineBidi == null) || (this.lineBidi.baseIsLeftToRight()) ? 0 : 1;
/* 175 */     int k = this.flags & 0xFFFFFFF6;
/* 176 */     if ((i & 0x1) != 0) k |= 1;
/* 177 */     if ((j & 0x1) != 0) k |= 8;
/* 178 */     StandardTextSource localStandardTextSource = new StandardTextSource(this.text, paramInt1, paramInt2 - paramInt1, this.lineStart, this.lineLimit - this.lineStart, i, k, paramFont, this.frc, paramCoreMetrics);
/* 179 */     return new TextSourceLabel(localStandardTextSource);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.TextLabelFactory
 * JD-Core Version:    0.6.2
 */