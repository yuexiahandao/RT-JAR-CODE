/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Toolkit;
/*     */ 
/*     */ class GlyphPainter1 extends GlyphView.GlyphPainter
/*     */ {
/*     */   FontMetrics metrics;
/*     */ 
/*     */   public float getSpan(GlyphView paramGlyphView, int paramInt1, int paramInt2, TabExpander paramTabExpander, float paramFloat)
/*     */   {
/*  59 */     sync(paramGlyphView);
/*  60 */     Segment localSegment = paramGlyphView.getText(paramInt1, paramInt2);
/*  61 */     int[] arrayOfInt = getJustificationData(paramGlyphView);
/*  62 */     int i = Utilities.getTabbedTextWidth(paramGlyphView, localSegment, this.metrics, (int)paramFloat, paramTabExpander, paramInt1, arrayOfInt);
/*     */ 
/*  64 */     SegmentCache.releaseSharedSegment(localSegment);
/*  65 */     return i;
/*     */   }
/*     */ 
/*     */   public float getHeight(GlyphView paramGlyphView) {
/*  69 */     sync(paramGlyphView);
/*  70 */     return this.metrics.getHeight();
/*     */   }
/*     */ 
/*     */   public float getAscent(GlyphView paramGlyphView)
/*     */   {
/*  78 */     sync(paramGlyphView);
/*  79 */     return this.metrics.getAscent();
/*     */   }
/*     */ 
/*     */   public float getDescent(GlyphView paramGlyphView)
/*     */   {
/*  87 */     sync(paramGlyphView);
/*  88 */     return this.metrics.getDescent();
/*     */   }
/*     */ 
/*     */   public void paint(GlyphView paramGlyphView, Graphics paramGraphics, Shape paramShape, int paramInt1, int paramInt2)
/*     */   {
/*  95 */     sync(paramGlyphView);
/*     */ 
/*  97 */     TabExpander localTabExpander = paramGlyphView.getTabExpander();
/*  98 */     Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*     */ 
/* 101 */     int i = localRectangle.x;
/* 102 */     int j = paramGlyphView.getStartOffset();
/* 103 */     int[] arrayOfInt = getJustificationData(paramGlyphView);
/* 104 */     if (j != paramInt1) {
/* 105 */       localSegment = paramGlyphView.getText(j, paramInt1);
/* 106 */       k = Utilities.getTabbedTextWidth(paramGlyphView, localSegment, this.metrics, i, localTabExpander, j, arrayOfInt);
/*     */ 
/* 108 */       i += k;
/* 109 */       SegmentCache.releaseSharedSegment(localSegment);
/*     */     }
/*     */ 
/* 113 */     int k = localRectangle.y + this.metrics.getHeight() - this.metrics.getDescent();
/*     */ 
/* 116 */     Segment localSegment = paramGlyphView.getText(paramInt1, paramInt2);
/* 117 */     paramGraphics.setFont(this.metrics.getFont());
/*     */ 
/* 119 */     Utilities.drawTabbedText(paramGlyphView, localSegment, i, k, paramGraphics, localTabExpander, paramInt1, arrayOfInt);
/*     */ 
/* 121 */     SegmentCache.releaseSharedSegment(localSegment);
/*     */   }
/*     */ 
/*     */   public Shape modelToView(GlyphView paramGlyphView, int paramInt, Position.Bias paramBias, Shape paramShape)
/*     */     throws BadLocationException
/*     */   {
/* 127 */     sync(paramGlyphView);
/* 128 */     Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/* 129 */     int i = paramGlyphView.getStartOffset();
/* 130 */     int j = paramGlyphView.getEndOffset();
/* 131 */     TabExpander localTabExpander = paramGlyphView.getTabExpander();
/*     */ 
/* 134 */     if (paramInt == j)
/*     */     {
/* 137 */       return new Rectangle(localRectangle.x + localRectangle.width, localRectangle.y, 0, this.metrics.getHeight());
/*     */     }
/*     */ 
/* 140 */     if ((paramInt >= i) && (paramInt <= j))
/*     */     {
/* 142 */       Segment localSegment = paramGlyphView.getText(i, paramInt);
/* 143 */       int[] arrayOfInt = getJustificationData(paramGlyphView);
/* 144 */       int k = Utilities.getTabbedTextWidth(paramGlyphView, localSegment, this.metrics, localRectangle.x, localTabExpander, i, arrayOfInt);
/*     */ 
/* 146 */       SegmentCache.releaseSharedSegment(localSegment);
/* 147 */       return new Rectangle(localRectangle.x + k, localRectangle.y, 0, this.metrics.getHeight());
/*     */     }
/* 149 */     throw new BadLocationException("modelToView - can't convert", j);
/*     */   }
/*     */ 
/*     */   public int viewToModel(GlyphView paramGlyphView, float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 169 */     sync(paramGlyphView);
/* 170 */     Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/* 171 */     int i = paramGlyphView.getStartOffset();
/* 172 */     int j = paramGlyphView.getEndOffset();
/* 173 */     TabExpander localTabExpander = paramGlyphView.getTabExpander();
/* 174 */     Segment localSegment = paramGlyphView.getText(i, j);
/* 175 */     int[] arrayOfInt = getJustificationData(paramGlyphView);
/* 176 */     int k = Utilities.getTabbedTextOffset(paramGlyphView, localSegment, this.metrics, localRectangle.x, (int)paramFloat1, localTabExpander, i, arrayOfInt);
/*     */ 
/* 179 */     SegmentCache.releaseSharedSegment(localSegment);
/* 180 */     int m = i + k;
/* 181 */     if (m == j)
/*     */     {
/* 184 */       m--;
/*     */     }
/* 186 */     paramArrayOfBias[0] = Position.Bias.Forward;
/* 187 */     return m;
/*     */   }
/*     */ 
/*     */   public int getBoundedPosition(GlyphView paramGlyphView, int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/* 209 */     sync(paramGlyphView);
/* 210 */     TabExpander localTabExpander = paramGlyphView.getTabExpander();
/* 211 */     Segment localSegment = paramGlyphView.getText(paramInt, paramGlyphView.getEndOffset());
/* 212 */     int[] arrayOfInt = getJustificationData(paramGlyphView);
/* 213 */     int i = Utilities.getTabbedTextOffset(paramGlyphView, localSegment, this.metrics, (int)paramFloat1, (int)(paramFloat1 + paramFloat2), localTabExpander, paramInt, false, arrayOfInt);
/*     */ 
/* 216 */     SegmentCache.releaseSharedSegment(localSegment);
/* 217 */     int j = paramInt + i;
/* 218 */     return j;
/*     */   }
/*     */ 
/*     */   void sync(GlyphView paramGlyphView) {
/* 222 */     Font localFont = paramGlyphView.getFont();
/* 223 */     if ((this.metrics == null) || (!localFont.equals(this.metrics.getFont())))
/*     */     {
/* 225 */       Container localContainer = paramGlyphView.getContainer();
/* 226 */       this.metrics = (localContainer != null ? localContainer.getFontMetrics(localFont) : Toolkit.getDefaultToolkit().getFontMetrics(localFont));
/*     */     }
/*     */   }
/*     */ 
/*     */   private int[] getJustificationData(GlyphView paramGlyphView)
/*     */   {
/* 238 */     View localView = paramGlyphView.getParent();
/* 239 */     int[] arrayOfInt = null;
/* 240 */     if ((localView instanceof ParagraphView.Row)) {
/* 241 */       ParagraphView.Row localRow = (ParagraphView.Row)localView;
/* 242 */       arrayOfInt = localRow.justificationData;
/*     */     }
/* 244 */     return arrayOfInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.GlyphPainter1
 * JD-Core Version:    0.6.2
 */