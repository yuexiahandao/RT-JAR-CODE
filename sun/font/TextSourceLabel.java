/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ 
/*     */ public class TextSourceLabel extends TextLabel
/*     */ {
/*     */   TextSource source;
/*     */   Rectangle2D lb;
/*     */   Rectangle2D ab;
/*     */   Rectangle2D vb;
/*     */   Rectangle2D ib;
/*     */   GlyphVector gv;
/*     */ 
/*     */   public TextSourceLabel(TextSource paramTextSource)
/*     */   {
/*  56 */     this(paramTextSource, null, null, null);
/*     */   }
/*     */ 
/*     */   public TextSourceLabel(TextSource paramTextSource, Rectangle2D paramRectangle2D1, Rectangle2D paramRectangle2D2, GlyphVector paramGlyphVector) {
/*  60 */     this.source = paramTextSource;
/*     */ 
/*  62 */     this.lb = paramRectangle2D1;
/*  63 */     this.ab = paramRectangle2D2;
/*  64 */     this.gv = paramGlyphVector;
/*     */   }
/*     */ 
/*     */   public TextSource getSource() {
/*  68 */     return this.source;
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getLogicalBounds(float paramFloat1, float paramFloat2) {
/*  72 */     if (this.lb == null) {
/*  73 */       this.lb = createLogicalBounds();
/*     */     }
/*  75 */     return new Rectangle2D.Float((float)(this.lb.getX() + paramFloat1), (float)(this.lb.getY() + paramFloat2), (float)this.lb.getWidth(), (float)this.lb.getHeight());
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getVisualBounds(float paramFloat1, float paramFloat2)
/*     */   {
/*  82 */     if (this.vb == null) {
/*  83 */       this.vb = createVisualBounds();
/*     */     }
/*     */ 
/*  86 */     return new Rectangle2D.Float((float)(this.vb.getX() + paramFloat1), (float)(this.vb.getY() + paramFloat2), (float)this.vb.getWidth(), (float)this.vb.getHeight());
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getAlignBounds(float paramFloat1, float paramFloat2)
/*     */   {
/*  93 */     if (this.ab == null) {
/*  94 */       this.ab = createAlignBounds();
/*     */     }
/*  96 */     return new Rectangle2D.Float((float)(this.ab.getX() + paramFloat1), (float)(this.ab.getY() + paramFloat2), (float)this.ab.getWidth(), (float)this.ab.getHeight());
/*     */   }
/*     */ 
/*     */   public Rectangle2D getItalicBounds(float paramFloat1, float paramFloat2)
/*     */   {
/* 103 */     if (this.ib == null) {
/* 104 */       this.ib = createItalicBounds();
/*     */     }
/* 106 */     return new Rectangle2D.Float((float)(this.ib.getX() + paramFloat1), (float)(this.ib.getY() + paramFloat2), (float)this.ib.getWidth(), (float)this.ib.getHeight());
/*     */   }
/*     */ 
/*     */   public Rectangle getPixelBounds(FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2)
/*     */   {
/* 114 */     return getGV().getPixelBounds(paramFontRenderContext, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   public AffineTransform getBaselineTransform() {
/* 118 */     Font localFont = this.source.getFont();
/* 119 */     if (localFont.hasLayoutAttributes()) {
/* 120 */       return AttributeValues.getBaselineTransform(localFont.getAttributes());
/*     */     }
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   public Shape getOutline(float paramFloat1, float paramFloat2) {
/* 126 */     return getGV().getOutline(paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   public void draw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2) {
/* 130 */     paramGraphics2D.drawGlyphVector(getGV(), paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   protected Rectangle2D createLogicalBounds() {
/* 134 */     return getGV().getLogicalBounds();
/*     */   }
/*     */ 
/*     */   protected Rectangle2D createVisualBounds() {
/* 138 */     return getGV().getVisualBounds();
/*     */   }
/*     */ 
/*     */   protected Rectangle2D createItalicBounds()
/*     */   {
/* 143 */     return getGV().getLogicalBounds();
/*     */   }
/*     */ 
/*     */   protected Rectangle2D createAlignBounds() {
/* 147 */     return createLogicalBounds();
/*     */   }
/*     */ 
/*     */   private final GlyphVector getGV() {
/* 151 */     if (this.gv == null) {
/* 152 */       this.gv = createGV();
/*     */     }
/*     */ 
/* 155 */     return this.gv;
/*     */   }
/*     */ 
/*     */   protected GlyphVector createGV() {
/* 159 */     Font localFont = this.source.getFont();
/* 160 */     FontRenderContext localFontRenderContext = this.source.getFRC();
/* 161 */     int i = this.source.getLayoutFlags();
/* 162 */     char[] arrayOfChar = this.source.getChars();
/* 163 */     int j = this.source.getStart();
/* 164 */     int k = this.source.getLength();
/*     */ 
/* 166 */     GlyphLayout localGlyphLayout = GlyphLayout.get(null);
/* 167 */     StandardGlyphVector localStandardGlyphVector = localGlyphLayout.layout(localFont, localFontRenderContext, arrayOfChar, j, k, i, null);
/*     */ 
/* 169 */     GlyphLayout.done(localGlyphLayout);
/*     */ 
/* 171 */     return localStandardGlyphVector;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.TextSourceLabel
 * JD-Core Version:    0.6.2
 */