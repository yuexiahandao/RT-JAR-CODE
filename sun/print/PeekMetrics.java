/*     */ package sun.print;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Paint;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.awt.image.renderable.RenderableImage;
/*     */ 
/*     */ public class PeekMetrics
/*     */ {
/*     */   private boolean mHasNonSolidColors;
/*     */   private boolean mHasCompositing;
/*     */   private boolean mHasText;
/*     */   private boolean mHasImages;
/*     */ 
/*     */   public boolean hasNonSolidColors()
/*     */   {
/*  60 */     return this.mHasNonSolidColors;
/*     */   }
/*     */ 
/*     */   public boolean hasCompositing()
/*     */   {
/*  69 */     return this.mHasCompositing;
/*     */   }
/*     */ 
/*     */   public boolean hasText()
/*     */   {
/*  77 */     return this.mHasText;
/*     */   }
/*     */ 
/*     */   public boolean hasImages()
/*     */   {
/*  85 */     return this.mHasImages;
/*     */   }
/*     */ 
/*     */   public void fill(Graphics2D paramGraphics2D)
/*     */   {
/*  93 */     checkDrawingMode(paramGraphics2D);
/*     */   }
/*     */ 
/*     */   public void draw(Graphics2D paramGraphics2D)
/*     */   {
/* 101 */     checkDrawingMode(paramGraphics2D);
/*     */   }
/*     */ 
/*     */   public void clear(Graphics2D paramGraphics2D)
/*     */   {
/* 109 */     checkPaint(paramGraphics2D.getBackground());
/*     */   }
/*     */ 
/*     */   public void drawText(Graphics2D paramGraphics2D)
/*     */   {
/* 116 */     this.mHasText = true;
/* 117 */     checkDrawingMode(paramGraphics2D);
/*     */   }
/*     */ 
/*     */   public void drawText(Graphics2D paramGraphics2D, TextLayout paramTextLayout)
/*     */   {
/* 126 */     this.mHasText = true;
/* 127 */     checkDrawingMode(paramGraphics2D);
/*     */   }
/*     */ 
/*     */   public void drawImage(Graphics2D paramGraphics2D, Image paramImage)
/*     */   {
/* 135 */     this.mHasImages = true;
/*     */   }
/*     */ 
/*     */   public void drawImage(Graphics2D paramGraphics2D, RenderedImage paramRenderedImage)
/*     */   {
/* 143 */     this.mHasImages = true;
/*     */   }
/*     */ 
/*     */   public void drawImage(Graphics2D paramGraphics2D, RenderableImage paramRenderableImage)
/*     */   {
/* 151 */     this.mHasImages = true;
/*     */   }
/*     */ 
/*     */   private void checkDrawingMode(Graphics2D paramGraphics2D)
/*     */   {
/* 160 */     checkPaint(paramGraphics2D.getPaint());
/* 161 */     checkAlpha(paramGraphics2D.getComposite());
/*     */   }
/*     */ 
/*     */   private void checkPaint(Paint paramPaint)
/*     */   {
/* 171 */     if ((paramPaint instanceof Color)) {
/* 172 */       if (((Color)paramPaint).getAlpha() < 255)
/* 173 */         this.mHasNonSolidColors = true;
/*     */     }
/*     */     else
/* 176 */       this.mHasNonSolidColors = true;
/*     */   }
/*     */ 
/*     */   private void checkAlpha(Composite paramComposite)
/*     */   {
/* 186 */     if ((paramComposite instanceof AlphaComposite)) {
/* 187 */       AlphaComposite localAlphaComposite = (AlphaComposite)paramComposite;
/* 188 */       float f = localAlphaComposite.getAlpha();
/* 189 */       int i = localAlphaComposite.getRule();
/*     */ 
/* 191 */       if ((f != 1.0D) || ((i != 2) && (i != 3)))
/*     */       {
/* 195 */         this.mHasCompositing = true;
/*     */       }
/*     */     }
/*     */     else {
/* 199 */       this.mHasCompositing = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PeekMetrics
 * JD-Core Version:    0.6.2
 */