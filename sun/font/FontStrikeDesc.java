/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import sun.awt.SunHints;
/*     */ 
/*     */ public class FontStrikeDesc
/*     */ {
/*     */   static final int AA_ON = 16;
/*     */   static final int AA_LCD_H = 32;
/*     */   static final int AA_LCD_V = 64;
/*     */   static final int FRAC_METRICS_ON = 256;
/*     */   static final int FRAC_METRICS_SP = 512;
/*     */   AffineTransform devTx;
/*     */   AffineTransform glyphTx;
/*     */   int style;
/*     */   int aaHint;
/*     */   int fmHint;
/*     */   private int hashCode;
/*     */   private int valuemask;
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  81 */     if (this.hashCode == 0) {
/*  82 */       this.hashCode = (this.glyphTx.hashCode() + this.devTx.hashCode() + this.valuemask);
/*     */     }
/*  84 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*     */     try {
/*  89 */       FontStrikeDesc localFontStrikeDesc = (FontStrikeDesc)paramObject;
/*  90 */       return (localFontStrikeDesc.valuemask == this.valuemask) && (localFontStrikeDesc.glyphTx.equals(this.glyphTx)) && (localFontStrikeDesc.devTx.equals(this.devTx));
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   FontStrikeDesc()
/*     */   {
/*     */   }
/*     */ 
/*     */   public static int getAAHintIntVal(Object paramObject, Font2D paramFont2D, int paramInt)
/*     */   {
/* 113 */     if ((paramObject == SunHints.VALUE_TEXT_ANTIALIAS_OFF) || (paramObject == SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT))
/*     */     {
/* 115 */       return 1;
/* 116 */     }if (paramObject == SunHints.VALUE_TEXT_ANTIALIAS_ON)
/* 117 */       return 2;
/* 118 */     if (paramObject == SunHints.VALUE_TEXT_ANTIALIAS_GASP) {
/* 119 */       if (paramFont2D.useAAForPtSize(paramInt)) {
/* 120 */         return 2;
/*     */       }
/* 122 */       return 1;
/*     */     }
/* 124 */     if ((paramObject == SunHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB) || (paramObject == SunHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR))
/*     */     {
/* 126 */       return 4;
/* 127 */     }if ((paramObject == SunHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB) || (paramObject == SunHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR))
/*     */     {
/* 129 */       return 6;
/*     */     }
/* 131 */     return 1;
/*     */   }
/*     */ 
/*     */   public static int getAAHintIntVal(Font2D paramFont2D, Font paramFont, FontRenderContext paramFontRenderContext)
/*     */   {
/* 144 */     Object localObject = paramFontRenderContext.getAntiAliasingHint();
/* 145 */     if ((localObject == SunHints.VALUE_TEXT_ANTIALIAS_OFF) || (localObject == SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT))
/*     */     {
/* 147 */       return 1;
/* 148 */     }if (localObject == SunHints.VALUE_TEXT_ANTIALIAS_ON)
/* 149 */       return 2;
/* 150 */     if (localObject == SunHints.VALUE_TEXT_ANTIALIAS_GASP)
/*     */     {
/* 153 */       AffineTransform localAffineTransform = paramFontRenderContext.getTransform();
/*     */       int i;
/* 154 */       if ((localAffineTransform.isIdentity()) && (!paramFont.isTransformed())) {
/* 155 */         i = paramFont.getSize();
/*     */       }
/*     */       else {
/* 158 */         float f = paramFont.getSize2D();
/* 159 */         if (localAffineTransform.isIdentity()) {
/* 160 */           localAffineTransform = paramFont.getTransform();
/* 161 */           localAffineTransform.scale(f, f);
/*     */         } else {
/* 163 */           localAffineTransform.scale(f, f);
/* 164 */           if (paramFont.isTransformed()) {
/* 165 */             localAffineTransform.concatenate(paramFont.getTransform());
/*     */           }
/*     */         }
/* 168 */         double d1 = localAffineTransform.getShearX();
/* 169 */         double d2 = localAffineTransform.getScaleY();
/* 170 */         if (d1 != 0.0D) {
/* 171 */           d2 = Math.sqrt(d1 * d1 + d2 * d2);
/*     */         }
/* 173 */         i = (int)(Math.abs(d2) + 0.5D);
/*     */       }
/* 175 */       if (paramFont2D.useAAForPtSize(i)) {
/* 176 */         return 2;
/*     */       }
/* 178 */       return 1;
/*     */     }
/* 180 */     if ((localObject == SunHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB) || (localObject == SunHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR))
/*     */     {
/* 182 */       return 4;
/* 183 */     }if ((localObject == SunHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB) || (localObject == SunHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR))
/*     */     {
/* 185 */       return 6;
/*     */     }
/* 187 */     return 1;
/*     */   }
/*     */ 
/*     */   public static int getFMHintIntVal(Object paramObject)
/*     */   {
/* 192 */     if ((paramObject == SunHints.VALUE_FRACTIONALMETRICS_OFF) || (paramObject == SunHints.VALUE_FRACTIONALMETRICS_DEFAULT))
/*     */     {
/* 194 */       return 1;
/*     */     }
/* 196 */     return 2;
/*     */   }
/*     */ 
/*     */   public FontStrikeDesc(AffineTransform paramAffineTransform1, AffineTransform paramAffineTransform2, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 202 */     this.devTx = paramAffineTransform1;
/* 203 */     this.glyphTx = paramAffineTransform2;
/* 204 */     this.style = paramInt1;
/* 205 */     this.aaHint = paramInt2;
/* 206 */     this.fmHint = paramInt3;
/* 207 */     this.valuemask = paramInt1;
/* 208 */     switch (paramInt2) {
/*     */     case 1:
/* 210 */       break;
/*     */     case 2:
/* 212 */       this.valuemask |= 16;
/* 213 */       break;
/*     */     case 4:
/*     */     case 5:
/* 216 */       this.valuemask |= 32;
/* 217 */       break;
/*     */     case 6:
/*     */     case 7:
/* 220 */       this.valuemask |= 64;
/* 221 */       break;
/*     */     case 3:
/*     */     }
/* 224 */     if (paramInt3 == 2)
/* 225 */       this.valuemask |= 256;
/*     */   }
/*     */ 
/*     */   FontStrikeDesc(FontStrikeDesc paramFontStrikeDesc)
/*     */   {
/* 230 */     this.devTx = paramFontStrikeDesc.devTx;
/*     */ 
/* 233 */     this.glyphTx = ((AffineTransform)paramFontStrikeDesc.glyphTx.clone());
/* 234 */     this.style = paramFontStrikeDesc.style;
/* 235 */     this.aaHint = paramFontStrikeDesc.aaHint;
/* 236 */     this.fmHint = paramFontStrikeDesc.fmHint;
/* 237 */     this.hashCode = paramFontStrikeDesc.hashCode;
/* 238 */     this.valuemask = paramFontStrikeDesc.valuemask;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 243 */     return "FontStrikeDesc: Style=" + this.style + " AA=" + this.aaHint + " FM=" + this.fmHint + " devTx=" + this.devTx + " devTx.FontTx.ptSize=" + this.glyphTx;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontStrikeDesc
 * JD-Core Version:    0.6.2
 */