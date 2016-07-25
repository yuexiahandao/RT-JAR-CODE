/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.loops.FontInfo;
/*     */ 
/*     */ public class OutlineTextRenderer
/*     */   implements TextPipe
/*     */ {
/*     */   public static final int THRESHHOLD = 100;
/*     */ 
/*     */   public void drawChars(SunGraphics2D paramSunGraphics2D, char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  71 */     String str = new String(paramArrayOfChar, paramInt1, paramInt2);
/*  72 */     drawString(paramSunGraphics2D, str, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void drawString(SunGraphics2D paramSunGraphics2D, String paramString, double paramDouble1, double paramDouble2)
/*     */   {
/*  77 */     if ("".equals(paramString)) {
/*  78 */       return;
/*     */     }
/*  80 */     TextLayout localTextLayout = new TextLayout(paramString, paramSunGraphics2D.getFont(), paramSunGraphics2D.getFontRenderContext());
/*     */ 
/*  82 */     Shape localShape = localTextLayout.getOutline(AffineTransform.getTranslateInstance(paramDouble1, paramDouble2));
/*     */ 
/*  84 */     int i = paramSunGraphics2D.getFontInfo().aaHint;
/*     */ 
/*  86 */     int j = -1;
/*  87 */     if ((i != 1) && (paramSunGraphics2D.antialiasHint != 2))
/*     */     {
/*  89 */       j = paramSunGraphics2D.antialiasHint;
/*  90 */       paramSunGraphics2D.antialiasHint = 2;
/*  91 */       paramSunGraphics2D.validatePipe();
/*  92 */     } else if ((i == 1) && (paramSunGraphics2D.antialiasHint != 1))
/*     */     {
/*  94 */       j = paramSunGraphics2D.antialiasHint;
/*  95 */       paramSunGraphics2D.antialiasHint = 1;
/*  96 */       paramSunGraphics2D.validatePipe();
/*     */     }
/*     */ 
/*  99 */     paramSunGraphics2D.fill(localShape);
/*     */ 
/* 101 */     if (j != -1) {
/* 102 */       paramSunGraphics2D.antialiasHint = j;
/* 103 */       paramSunGraphics2D.validatePipe();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drawGlyphVector(SunGraphics2D paramSunGraphics2D, GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*     */   {
/* 111 */     Shape localShape = paramGlyphVector.getOutline(paramFloat1, paramFloat2);
/* 112 */     int i = -1;
/* 113 */     FontRenderContext localFontRenderContext = paramGlyphVector.getFontRenderContext();
/* 114 */     boolean bool = localFontRenderContext.isAntiAliased();
/*     */ 
/* 121 */     if ((bool) && 
/* 122 */       (paramSunGraphics2D.getGVFontInfo(paramGlyphVector.getFont(), localFontRenderContext).aaHint == 1))
/*     */     {
/* 124 */       bool = false;
/*     */     }
/*     */ 
/* 128 */     if ((bool) && (paramSunGraphics2D.antialiasHint != 2)) {
/* 129 */       i = paramSunGraphics2D.antialiasHint;
/* 130 */       paramSunGraphics2D.antialiasHint = 2;
/* 131 */       paramSunGraphics2D.validatePipe();
/* 132 */     } else if ((!bool) && (paramSunGraphics2D.antialiasHint != 1)) {
/* 133 */       i = paramSunGraphics2D.antialiasHint;
/* 134 */       paramSunGraphics2D.antialiasHint = 1;
/* 135 */       paramSunGraphics2D.validatePipe();
/*     */     }
/*     */ 
/* 138 */     paramSunGraphics2D.fill(localShape);
/*     */ 
/* 140 */     if (i != -1) {
/* 141 */       paramSunGraphics2D.antialiasHint = i;
/* 142 */       paramSunGraphics2D.validatePipe();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.OutlineTextRenderer
 * JD-Core Version:    0.6.2
 */