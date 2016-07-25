/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import sun.font.GlyphList;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.FontInfo;
/*     */ 
/*     */ public abstract class GlyphListPipe
/*     */   implements TextPipe
/*     */ {
/*     */   public void drawString(SunGraphics2D paramSunGraphics2D, String paramString, double paramDouble1, double paramDouble2)
/*     */   {
/*  50 */     FontInfo localFontInfo = paramSunGraphics2D.getFontInfo();
/*  51 */     if (localFontInfo.pixelHeight > 100) {
/*  52 */       SurfaceData.outlineTextRenderer.drawString(paramSunGraphics2D, paramString, paramDouble1, paramDouble2);
/*     */       return;
/*     */     }
/*     */     float f1;
/*     */     float f2;
/*  57 */     if (paramSunGraphics2D.transformState >= 3) {
/*  58 */       localObject = new double[] { paramDouble1 + localFontInfo.originX, paramDouble2 + localFontInfo.originY };
/*  59 */       paramSunGraphics2D.transform.transform((double[])localObject, 0, (double[])localObject, 0, 1);
/*  60 */       f1 = (float)localObject[0];
/*  61 */       f2 = (float)localObject[1];
/*     */     } else {
/*  63 */       f1 = (float)(paramDouble1 + localFontInfo.originX + paramSunGraphics2D.transX);
/*  64 */       f2 = (float)(paramDouble2 + localFontInfo.originY + paramSunGraphics2D.transY);
/*     */     }
/*     */ 
/*  70 */     Object localObject = GlyphList.getInstance();
/*  71 */     if (((GlyphList)localObject).setFromString(localFontInfo, paramString, f1, f2)) {
/*  72 */       drawGlyphList(paramSunGraphics2D, (GlyphList)localObject);
/*  73 */       ((GlyphList)localObject).dispose();
/*     */     } else {
/*  75 */       ((GlyphList)localObject).dispose();
/*  76 */       TextLayout localTextLayout = new TextLayout(paramString, paramSunGraphics2D.getFont(), paramSunGraphics2D.getFontRenderContext());
/*     */ 
/*  78 */       localTextLayout.draw(paramSunGraphics2D, (float)paramDouble1, (float)paramDouble2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drawChars(SunGraphics2D paramSunGraphics2D, char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  86 */     FontInfo localFontInfo = paramSunGraphics2D.getFontInfo();
/*     */ 
/*  88 */     if (localFontInfo.pixelHeight > 100) {
/*  89 */       SurfaceData.outlineTextRenderer.drawChars(paramSunGraphics2D, paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       return;
/*     */     }
/*     */     float f1;
/*     */     float f2;
/*  93 */     if (paramSunGraphics2D.transformState >= 3) {
/*  94 */       localObject = new double[] { paramInt3 + localFontInfo.originX, paramInt4 + localFontInfo.originY };
/*  95 */       paramSunGraphics2D.transform.transform((double[])localObject, 0, (double[])localObject, 0, 1);
/*  96 */       f1 = (float)localObject[0];
/*  97 */       f2 = (float)localObject[1];
/*     */     } else {
/*  99 */       f1 = paramInt3 + localFontInfo.originX + paramSunGraphics2D.transX;
/* 100 */       f2 = paramInt4 + localFontInfo.originY + paramSunGraphics2D.transY;
/*     */     }
/* 102 */     Object localObject = GlyphList.getInstance();
/* 103 */     if (((GlyphList)localObject).setFromChars(localFontInfo, paramArrayOfChar, paramInt1, paramInt2, f1, f2)) {
/* 104 */       drawGlyphList(paramSunGraphics2D, (GlyphList)localObject);
/* 105 */       ((GlyphList)localObject).dispose();
/*     */     } else {
/* 107 */       ((GlyphList)localObject).dispose();
/* 108 */       TextLayout localTextLayout = new TextLayout(new String(paramArrayOfChar, paramInt1, paramInt2), paramSunGraphics2D.getFont(), paramSunGraphics2D.getFontRenderContext());
/*     */ 
/* 111 */       localTextLayout.draw(paramSunGraphics2D, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drawGlyphVector(SunGraphics2D paramSunGraphics2D, GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*     */   {
/* 119 */     FontRenderContext localFontRenderContext = paramGlyphVector.getFontRenderContext();
/* 120 */     FontInfo localFontInfo = paramSunGraphics2D.getGVFontInfo(paramGlyphVector.getFont(), localFontRenderContext);
/* 121 */     if (localFontInfo.pixelHeight > 100) {
/* 122 */       SurfaceData.outlineTextRenderer.drawGlyphVector(paramSunGraphics2D, paramGlyphVector, paramFloat1, paramFloat2);
/* 123 */       return;
/*     */     }
/* 125 */     if (paramSunGraphics2D.transformState >= 3) {
/* 126 */       localObject = new double[] { paramFloat1, paramFloat2 };
/* 127 */       paramSunGraphics2D.transform.transform((double[])localObject, 0, (double[])localObject, 0, 1);
/* 128 */       paramFloat1 = (float)localObject[0];
/* 129 */       paramFloat2 = (float)localObject[1];
/*     */     } else {
/* 131 */       paramFloat1 += paramSunGraphics2D.transX;
/* 132 */       paramFloat2 += paramSunGraphics2D.transY;
/*     */     }
/*     */ 
/* 135 */     Object localObject = GlyphList.getInstance();
/* 136 */     ((GlyphList)localObject).setFromGlyphVector(localFontInfo, paramGlyphVector, paramFloat1, paramFloat2);
/* 137 */     drawGlyphList(paramSunGraphics2D, (GlyphList)localObject, localFontInfo.aaHint);
/* 138 */     ((GlyphList)localObject).dispose();
/*     */   }
/*     */ 
/*     */   protected abstract void drawGlyphList(SunGraphics2D paramSunGraphics2D, GlyphList paramGlyphList);
/*     */ 
/*     */   protected void drawGlyphList(SunGraphics2D paramSunGraphics2D, GlyphList paramGlyphList, int paramInt)
/*     */   {
/* 145 */     drawGlyphList(paramSunGraphics2D, paramGlyphList);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.GlyphListPipe
 * JD-Core Version:    0.6.2
 */