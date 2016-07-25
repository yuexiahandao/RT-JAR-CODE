/*      */ package sun.font;
/*      */ 
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.GlyphJustificationInfo;
/*      */ import java.awt.font.LineMetrics;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Map;
/*      */ 
/*      */ class ExtendedTextSourceLabel extends ExtendedTextLabel
/*      */   implements Decoration.Label
/*      */ {
/*      */   TextSource source;
/*      */   private Decoration decorator;
/*      */   private Font font;
/*      */   private AffineTransform baseTX;
/*      */   private CoreMetrics cm;
/*      */   Rectangle2D lb;
/*      */   Rectangle2D ab;
/*      */   Rectangle2D vb;
/*      */   Rectangle2D ib;
/*      */   StandardGlyphVector gv;
/*      */   float[] charinfo;
/*      */   private static final int posx = 0;
/*      */   private static final int posy = 1;
/*      */   private static final int advx = 2;
/*      */   private static final int advy = 3;
/*      */   private static final int visx = 4;
/*      */   private static final int visy = 5;
/*      */   private static final int visw = 6;
/*      */   private static final int vish = 7;
/*      */   private static final int numvals = 8;
/*      */ 
/*      */   public ExtendedTextSourceLabel(TextSource paramTextSource, Decoration paramDecoration)
/*      */   {
/*   78 */     this.source = paramTextSource;
/*   79 */     this.decorator = paramDecoration;
/*   80 */     finishInit();
/*      */   }
/*      */ 
/*      */   public ExtendedTextSourceLabel(TextSource paramTextSource, ExtendedTextSourceLabel paramExtendedTextSourceLabel, int paramInt)
/*      */   {
/*   90 */     this.source = paramTextSource;
/*   91 */     this.decorator = paramExtendedTextSourceLabel.decorator;
/*   92 */     finishInit();
/*      */   }
/*      */ 
/*      */   private void finishInit() {
/*   96 */     this.font = this.source.getFont();
/*      */ 
/*   98 */     Map localMap = this.font.getAttributes();
/*   99 */     this.baseTX = AttributeValues.getBaselineTransform(localMap);
/*  100 */     if (this.baseTX == null) {
/*  101 */       this.cm = this.source.getCoreMetrics();
/*      */     } else {
/*  103 */       AffineTransform localAffineTransform = AttributeValues.getCharTransform(localMap);
/*  104 */       if (localAffineTransform == null) {
/*  105 */         localAffineTransform = new AffineTransform();
/*      */       }
/*  107 */       this.font = this.font.deriveFont(localAffineTransform);
/*      */ 
/*  109 */       LineMetrics localLineMetrics = this.font.getLineMetrics(this.source.getChars(), this.source.getStart(), this.source.getStart() + this.source.getLength(), this.source.getFRC());
/*      */ 
/*  111 */       this.cm = CoreMetrics.get(localLineMetrics);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Rectangle2D getLogicalBounds()
/*      */   {
/*  119 */     return getLogicalBounds(0.0F, 0.0F);
/*      */   }
/*      */ 
/*      */   public Rectangle2D getLogicalBounds(float paramFloat1, float paramFloat2) {
/*  123 */     if (this.lb == null) {
/*  124 */       this.lb = createLogicalBounds();
/*      */     }
/*  126 */     return new Rectangle2D.Float((float)(this.lb.getX() + paramFloat1), (float)(this.lb.getY() + paramFloat2), (float)this.lb.getWidth(), (float)this.lb.getHeight());
/*      */   }
/*      */ 
/*      */   public float getAdvance()
/*      */   {
/*  133 */     if (this.lb == null) {
/*  134 */       this.lb = createLogicalBounds();
/*      */     }
/*  136 */     return (float)this.lb.getWidth();
/*      */   }
/*      */ 
/*      */   public Rectangle2D getVisualBounds(float paramFloat1, float paramFloat2) {
/*  140 */     if (this.vb == null) {
/*  141 */       this.vb = this.decorator.getVisualBounds(this);
/*      */     }
/*  143 */     return new Rectangle2D.Float((float)(this.vb.getX() + paramFloat1), (float)(this.vb.getY() + paramFloat2), (float)this.vb.getWidth(), (float)this.vb.getHeight());
/*      */   }
/*      */ 
/*      */   public Rectangle2D getAlignBounds(float paramFloat1, float paramFloat2)
/*      */   {
/*  150 */     if (this.ab == null) {
/*  151 */       this.ab = createAlignBounds();
/*      */     }
/*  153 */     return new Rectangle2D.Float((float)(this.ab.getX() + paramFloat1), (float)(this.ab.getY() + paramFloat2), (float)this.ab.getWidth(), (float)this.ab.getHeight());
/*      */   }
/*      */ 
/*      */   public Rectangle2D getItalicBounds(float paramFloat1, float paramFloat2)
/*      */   {
/*  161 */     if (this.ib == null) {
/*  162 */       this.ib = createItalicBounds();
/*      */     }
/*  164 */     return new Rectangle2D.Float((float)(this.ib.getX() + paramFloat1), (float)(this.ib.getY() + paramFloat2), (float)this.ib.getWidth(), (float)this.ib.getHeight());
/*      */   }
/*      */ 
/*      */   public Rectangle getPixelBounds(FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2)
/*      */   {
/*  172 */     return getGV().getPixelBounds(paramFontRenderContext, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public boolean isSimple() {
/*  176 */     return (this.decorator == Decoration.getPlainDecoration()) && (this.baseTX == null);
/*      */   }
/*      */ 
/*      */   public AffineTransform getBaselineTransform()
/*      */   {
/*  181 */     return this.baseTX;
/*      */   }
/*      */ 
/*      */   public Shape handleGetOutline(float paramFloat1, float paramFloat2) {
/*  185 */     return getGV().getOutline(paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public Shape getOutline(float paramFloat1, float paramFloat2) {
/*  189 */     return this.decorator.getOutline(this, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public void handleDraw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2) {
/*  193 */     paramGraphics2D.drawGlyphVector(getGV(), paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public void draw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2) {
/*  197 */     this.decorator.drawTextAndDecorations(this, paramGraphics2D, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   protected Rectangle2D createLogicalBounds()
/*      */   {
/*  225 */     return getGV().getLogicalBounds();
/*      */   }
/*      */ 
/*      */   public Rectangle2D handleGetVisualBounds() {
/*  229 */     return getGV().getVisualBounds();
/*      */   }
/*      */ 
/*      */   protected Rectangle2D createAlignBounds()
/*      */   {
/*  243 */     float[] arrayOfFloat = getCharinfo();
/*      */ 
/*  245 */     float f1 = 0.0F;
/*  246 */     float f2 = -this.cm.ascent;
/*  247 */     float f3 = 0.0F;
/*  248 */     float f4 = this.cm.ascent + this.cm.descent;
/*      */ 
/*  250 */     if ((this.charinfo == null) || (this.charinfo.length == 0)) {
/*  251 */       return new Rectangle2D.Float(f1, f2, f3, f4);
/*      */     }
/*      */ 
/*  254 */     int i = (this.source.getLayoutFlags() & 0x8) == 0 ? 1 : 0;
/*  255 */     int j = arrayOfFloat.length - 8;
/*  256 */     if (i != 0) {
/*  257 */       while ((j > 0) && (arrayOfFloat[(j + 6)] == 0.0F)) {
/*  258 */         j -= 8;
/*      */       }
/*      */     }
/*      */ 
/*  262 */     if (j >= 0) {
/*  263 */       int k = 0;
/*  264 */       while ((k < j) && ((arrayOfFloat[(k + 2)] == 0.0F) || ((i == 0) && (arrayOfFloat[(k + 6)] == 0.0F)))) {
/*  265 */         k += 8;
/*      */       }
/*      */ 
/*  268 */       f1 = Math.max(0.0F, arrayOfFloat[(k + 0)]);
/*  269 */       f3 = arrayOfFloat[(j + 0)] + arrayOfFloat[(j + 2)] - f1;
/*      */     }
/*      */ 
/*  290 */     return new Rectangle2D.Float(f1, f2, f3, f4);
/*      */   }
/*      */ 
/*      */   public Rectangle2D createItalicBounds() {
/*  294 */     float f1 = this.cm.italicAngle;
/*      */ 
/*  296 */     Rectangle2D localRectangle2D = getLogicalBounds();
/*  297 */     float f2 = (float)localRectangle2D.getMinX();
/*  298 */     float f3 = -this.cm.ascent;
/*  299 */     float f4 = (float)localRectangle2D.getMaxX();
/*  300 */     float f5 = this.cm.descent;
/*  301 */     if (f1 != 0.0F) {
/*  302 */       if (f1 > 0.0F) {
/*  303 */         f2 -= f1 * (f5 - this.cm.ssOffset);
/*  304 */         f4 -= f1 * (f3 - this.cm.ssOffset);
/*      */       } else {
/*  306 */         f2 -= f1 * (f3 - this.cm.ssOffset);
/*  307 */         f4 -= f1 * (f5 - this.cm.ssOffset);
/*      */       }
/*      */     }
/*  310 */     return new Rectangle2D.Float(f2, f3, f4 - f2, f5 - f3);
/*      */   }
/*      */ 
/*      */   private final StandardGlyphVector getGV() {
/*  314 */     if (this.gv == null) {
/*  315 */       this.gv = createGV();
/*      */     }
/*      */ 
/*  318 */     return this.gv;
/*      */   }
/*      */ 
/*      */   protected StandardGlyphVector createGV() {
/*  322 */     FontRenderContext localFontRenderContext = this.source.getFRC();
/*  323 */     int i = this.source.getLayoutFlags();
/*  324 */     char[] arrayOfChar = this.source.getChars();
/*  325 */     int j = this.source.getStart();
/*  326 */     int k = this.source.getLength();
/*      */ 
/*  328 */     GlyphLayout localGlyphLayout = GlyphLayout.get(null);
/*  329 */     this.gv = localGlyphLayout.layout(this.font, localFontRenderContext, arrayOfChar, j, k, i, null);
/*  330 */     GlyphLayout.done(localGlyphLayout);
/*      */ 
/*  332 */     return this.gv;
/*      */   }
/*      */ 
/*      */   public int getNumCharacters()
/*      */   {
/*  348 */     return this.source.getLength();
/*      */   }
/*      */ 
/*      */   public CoreMetrics getCoreMetrics() {
/*  352 */     return this.cm;
/*      */   }
/*      */ 
/*      */   public float getCharX(int paramInt) {
/*  356 */     validate(paramInt);
/*  357 */     float[] arrayOfFloat = getCharinfo();
/*  358 */     int i = l2v(paramInt) * 8 + 0;
/*  359 */     if ((arrayOfFloat == null) || (i >= arrayOfFloat.length)) {
/*  360 */       return 0.0F;
/*      */     }
/*  362 */     return arrayOfFloat[i];
/*      */   }
/*      */ 
/*      */   public float getCharY(int paramInt)
/*      */   {
/*  367 */     validate(paramInt);
/*  368 */     float[] arrayOfFloat = getCharinfo();
/*  369 */     int i = l2v(paramInt) * 8 + 1;
/*  370 */     if ((arrayOfFloat == null) || (i >= arrayOfFloat.length)) {
/*  371 */       return 0.0F;
/*      */     }
/*  373 */     return arrayOfFloat[i];
/*      */   }
/*      */ 
/*      */   public float getCharAdvance(int paramInt)
/*      */   {
/*  378 */     validate(paramInt);
/*  379 */     float[] arrayOfFloat = getCharinfo();
/*  380 */     int i = l2v(paramInt) * 8 + 2;
/*  381 */     if ((arrayOfFloat == null) || (i >= arrayOfFloat.length)) {
/*  382 */       return 0.0F;
/*      */     }
/*  384 */     return arrayOfFloat[i];
/*      */   }
/*      */ 
/*      */   public Rectangle2D handleGetCharVisualBounds(int paramInt)
/*      */   {
/*  389 */     validate(paramInt);
/*  390 */     float[] arrayOfFloat = getCharinfo();
/*  391 */     paramInt = l2v(paramInt) * 8;
/*  392 */     if ((arrayOfFloat == null) || (paramInt + 7 >= arrayOfFloat.length)) {
/*  393 */       return new Rectangle2D.Float();
/*      */     }
/*  395 */     return new Rectangle2D.Float(arrayOfFloat[(paramInt + 4)], arrayOfFloat[(paramInt + 5)], arrayOfFloat[(paramInt + 6)], arrayOfFloat[(paramInt + 7)]);
/*      */   }
/*      */ 
/*      */   public Rectangle2D getCharVisualBounds(int paramInt, float paramFloat1, float paramFloat2)
/*      */   {
/*  404 */     Rectangle2D localRectangle2D = this.decorator.getCharVisualBounds(this, paramInt);
/*  405 */     if ((paramFloat1 != 0.0F) || (paramFloat2 != 0.0F)) {
/*  406 */       localRectangle2D.setRect(localRectangle2D.getX() + paramFloat1, localRectangle2D.getY() + paramFloat2, localRectangle2D.getWidth(), localRectangle2D.getHeight());
/*      */     }
/*      */ 
/*  411 */     return localRectangle2D;
/*      */   }
/*      */ 
/*      */   private void validate(int paramInt) {
/*  415 */     if (paramInt < 0)
/*  416 */       throw new IllegalArgumentException("index " + paramInt + " < 0");
/*  417 */     if (paramInt >= this.source.getLength())
/*  418 */       throw new IllegalArgumentException("index " + paramInt + " < " + this.source.getLength());
/*      */   }
/*      */ 
/*      */   public int logicalToVisual(int paramInt)
/*      */   {
/*  470 */     validate(paramInt);
/*  471 */     return l2v(paramInt);
/*      */   }
/*      */ 
/*      */   public int visualToLogical(int paramInt) {
/*  475 */     validate(paramInt);
/*  476 */     return v2l(paramInt);
/*      */   }
/*      */ 
/*      */   public int getLineBreakIndex(int paramInt, float paramFloat) {
/*  480 */     float[] arrayOfFloat = getCharinfo();
/*  481 */     int i = this.source.getLength();
/*  482 */     paramInt--;
/*  483 */     while (paramFloat >= 0.0F) { paramInt++; if (paramInt >= i) break;
/*  484 */       int j = l2v(paramInt) * 8 + 2;
/*  485 */       if (j >= arrayOfFloat.length) {
/*      */         break;
/*      */       }
/*  488 */       float f = arrayOfFloat[j];
/*  489 */       paramFloat -= f;
/*      */     }
/*      */ 
/*  492 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public float getAdvanceBetween(int paramInt1, int paramInt2) {
/*  496 */     float f = 0.0F;
/*      */ 
/*  498 */     float[] arrayOfFloat = getCharinfo();
/*  499 */     paramInt1--;
/*      */     while (true) { paramInt1++; if (paramInt1 >= paramInt2) break;
/*  501 */       int i = l2v(paramInt1) * 8 + 2;
/*  502 */       if (i >= arrayOfFloat.length) {
/*      */         break;
/*      */       }
/*  505 */       f += arrayOfFloat[i];
/*      */     }
/*      */ 
/*  508 */     return f;
/*      */   }
/*      */ 
/*      */   public boolean caretAtOffsetIsValid(int paramInt)
/*      */   {
/*  520 */     if ((paramInt == 0) || (paramInt == this.source.getLength())) {
/*  521 */       return true;
/*      */     }
/*  523 */     int i = this.source.getChars()[(this.source.getStart() + paramInt)];
/*  524 */     if ((i == 9) || (i == 10) || (i == 13)) {
/*  525 */       return true;
/*      */     }
/*  527 */     int j = l2v(paramInt);
/*      */ 
/*  537 */     int k = j * 8 + 2;
/*  538 */     float[] arrayOfFloat = getCharinfo();
/*  539 */     if ((arrayOfFloat == null) || (k >= arrayOfFloat.length)) {
/*  540 */       return false;
/*      */     }
/*  542 */     return arrayOfFloat[k] != 0.0F;
/*      */   }
/*      */ 
/*      */   private final float[] getCharinfo()
/*      */   {
/*  547 */     if (this.charinfo == null) {
/*  548 */       this.charinfo = createCharinfo();
/*      */     }
/*  550 */     return this.charinfo;
/*      */   }
/*      */ 
/*      */   protected float[] createCharinfo()
/*      */   {
/*  619 */     StandardGlyphVector localStandardGlyphVector = getGV();
/*  620 */     float[] arrayOfFloat = null;
/*      */     try {
/*  622 */       arrayOfFloat = localStandardGlyphVector.getGlyphInfo();
/*      */     }
/*      */     catch (Exception localException) {
/*  625 */       System.out.println(this.source);
/*      */     }
/*      */ 
/*  634 */     int i = localStandardGlyphVector.getNumGlyphs();
/*  635 */     if (i == 0) {
/*  636 */       return arrayOfFloat;
/*      */     }
/*  638 */     int[] arrayOfInt = localStandardGlyphVector.getGlyphCharIndices(0, i, null);
/*      */ 
/*  640 */     int j = 0;
/*  641 */     if (j != 0) {
/*  642 */       System.err.println("number of glyphs: " + i);
/*  643 */       for (f1 = 0; f1 < i; f1++) {
/*  644 */         System.err.println("g: " + f1 + ", x: " + arrayOfFloat[(f1 * 8 + 0)] + ", a: " + arrayOfFloat[(f1 * 8 + 2)] + ", n: " + arrayOfInt[f1]);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  651 */     float f1 = arrayOfInt[0];
/*  652 */     int k = f1;
/*  653 */     int m = 0;
/*  654 */     int n = 0;
/*  655 */     int i1 = 0;
/*  656 */     int i2 = 0;
/*  657 */     int i3 = 0;
/*  658 */     int i4 = i;
/*  659 */     int i5 = 8;
/*  660 */     int i6 = 1;
/*      */ 
/*  662 */     int i7 = (this.source.getLayoutFlags() & 0x1) == 0 ? 1 : 0;
/*  663 */     if (i7 == 0) {
/*  664 */       f1 = arrayOfInt[(i - 1)];
/*  665 */       k = f1;
/*  666 */       m = 0;
/*  667 */       n = arrayOfFloat.length - 8;
/*  668 */       i1 = 0;
/*  669 */       i2 = arrayOfFloat.length - 8;
/*  670 */       i3 = i - 1;
/*  671 */       i4 = -1;
/*  672 */       i5 = -8;
/*  673 */       i6 = -1;
/*      */     }
/*      */ 
/*  693 */     float f2 = 0.0F; float f3 = 0.0F; float f4 = 0.0F; float f5 = 0.0F; float f6 = 0.0F; float f7 = 0.0F;
/*  694 */     float f8 = 0.0F;
/*      */ 
/*  697 */     int i8 = 0;
/*      */     int i10;
/*      */     int i13;
/*      */     int i12;
/*  699 */     while (i3 != i4)
/*      */     {
/*  701 */       int i9 = 0;
/*  702 */       i10 = 0;
/*      */ 
/*  704 */       f1 = arrayOfInt[i3];
/*  705 */       k = f1;
/*      */ 
/*  708 */       i3 += i6;
/*  709 */       i2 += i5;
/*      */       float f9;
/*  715 */       while ((i3 != i4) && ((arrayOfFloat[(i2 + 2)] == 0.0F) || (f1 != m) || (arrayOfInt[i3] <= k) || (k - f1 > i10)))
/*      */       {
/*  721 */         if (i9 == 0) {
/*  722 */           int i11 = i2 - i5;
/*      */ 
/*  724 */           f2 = arrayOfFloat[(i11 + 0)];
/*  725 */           f3 = f2 + arrayOfFloat[(i11 + 2)];
/*  726 */           f4 = arrayOfFloat[(i11 + 4)];
/*  727 */           f5 = arrayOfFloat[(i11 + 5)];
/*  728 */           f6 = f4 + arrayOfFloat[(i11 + 6)];
/*  729 */           f7 = f5 + arrayOfFloat[(i11 + 7)];
/*      */ 
/*  731 */           i9 = 1;
/*      */         }
/*      */ 
/*  735 */         i10++;
/*      */ 
/*  738 */         f9 = arrayOfFloat[(i2 + 2)];
/*  739 */         if (f9 != 0.0F) {
/*  740 */           f10 = arrayOfFloat[(i2 + 0)];
/*  741 */           f2 = Math.min(f2, f10);
/*  742 */           f3 = Math.max(f3, f10 + f9);
/*      */         }
/*      */ 
/*  746 */         float f10 = arrayOfFloat[(i2 + 6)];
/*  747 */         if (f10 != 0.0F) {
/*  748 */           float f11 = arrayOfFloat[(i2 + 4)];
/*  749 */           float f12 = arrayOfFloat[(i2 + 5)];
/*  750 */           f4 = Math.min(f4, f11);
/*  751 */           f5 = Math.min(f5, f12);
/*  752 */           f6 = Math.max(f6, f11 + f10);
/*  753 */           f7 = Math.max(f7, f12 + arrayOfFloat[(i2 + 7)]);
/*      */         }
/*      */ 
/*  757 */         f1 = Math.min(f1, arrayOfInt[i3]);
/*  758 */         k = Math.max(k, arrayOfInt[i3]);
/*      */ 
/*  761 */         i3 += i6;
/*  762 */         i2 += i5;
/*      */       }
/*      */ 
/*  766 */       if (j != 0) {
/*  767 */         System.out.println("minIndex = " + f1 + ", maxIndex = " + k);
/*      */       }
/*      */ 
/*  770 */       m = k + 1;
/*      */ 
/*  773 */       arrayOfFloat[(n + 1)] = f8;
/*  774 */       arrayOfFloat[(n + 3)] = 0.0F;
/*      */ 
/*  776 */       if (i9 != 0)
/*      */       {
/*  778 */         arrayOfFloat[(n + 0)] = f2;
/*  779 */         arrayOfFloat[(n + 2)] = (f3 - f2);
/*  780 */         arrayOfFloat[(n + 4)] = f4;
/*  781 */         arrayOfFloat[(n + 5)] = f5;
/*  782 */         arrayOfFloat[(n + 6)] = (f6 - f4);
/*  783 */         arrayOfFloat[(n + 7)] = (f7 - f5);
/*      */ 
/*  788 */         if (k - f1 < i10) {
/*  789 */           i8 = 1;
/*      */         }
/*      */ 
/*  795 */         if (f1 < k) {
/*  796 */           if (i7 == 0)
/*      */           {
/*  798 */             f3 = f2;
/*      */           }
/*  800 */           f6 -= f4;
/*  801 */           f7 -= f5;
/*      */ 
/*  803 */           f9 = f1; i13 = n / 8;
/*      */ 
/*  805 */           while (f1 < k) {
/*  806 */             f1++;
/*  807 */             i1 += i6;
/*  808 */             n += i5;
/*      */ 
/*  810 */             if (((n < 0) || (n >= arrayOfFloat.length)) && 
/*  811 */               (j != 0)) System.out.println("minIndex = " + f9 + ", maxIndex = " + k + ", cp = " + i13);
/*      */ 
/*  814 */             arrayOfFloat[(n + 0)] = f3;
/*  815 */             arrayOfFloat[(n + 1)] = f8;
/*  816 */             arrayOfFloat[(n + 2)] = 0.0F;
/*  817 */             arrayOfFloat[(n + 3)] = 0.0F;
/*  818 */             arrayOfFloat[(n + 4)] = f4;
/*  819 */             arrayOfFloat[(n + 5)] = f5;
/*  820 */             arrayOfFloat[(n + 6)] = f6;
/*  821 */             arrayOfFloat[(n + 7)] = f7;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  826 */         i9 = 0;
/*  827 */       } else if (i8 != 0)
/*      */       {
/*  829 */         i12 = i2 - i5;
/*      */ 
/*  831 */         arrayOfFloat[(n + 0)] = arrayOfFloat[(i12 + 0)];
/*  832 */         arrayOfFloat[(n + 2)] = arrayOfFloat[(i12 + 2)];
/*  833 */         arrayOfFloat[(n + 4)] = arrayOfFloat[(i12 + 4)];
/*  834 */         arrayOfFloat[(n + 5)] = arrayOfFloat[(i12 + 5)];
/*  835 */         arrayOfFloat[(n + 6)] = arrayOfFloat[(i12 + 6)];
/*  836 */         arrayOfFloat[(n + 7)] = arrayOfFloat[(i12 + 7)];
/*      */       }
/*      */ 
/*  841 */       n += i5;
/*  842 */       i1 += i6;
/*      */     }
/*      */ 
/*  845 */     if ((i8 != 0) && (i7 == 0))
/*      */     {
/*  848 */       n -= i5;
/*  849 */       System.arraycopy(arrayOfFloat, n, arrayOfFloat, 0, arrayOfFloat.length - n);
/*      */     }
/*      */     char[] arrayOfChar;
/*  852 */     if (j != 0) {
/*  853 */       arrayOfChar = this.source.getChars();
/*  854 */       i10 = this.source.getStart();
/*  855 */       i12 = this.source.getLength();
/*  856 */       System.out.println("char info for " + i12 + " characters");
/*  857 */       for (i13 = 0; i13 < i12 * 8; ) {
/*  858 */         System.out.println(" ch: " + Integer.toHexString(arrayOfChar[(i10 + v2l(i13 / 8))]) + " x: " + arrayOfFloat[(i13++)] + " y: " + arrayOfFloat[(i13++)] + " xa: " + arrayOfFloat[(i13++)] + " ya: " + arrayOfFloat[(i13++)] + " l: " + arrayOfFloat[(i13++)] + " t: " + arrayOfFloat[(i13++)] + " w: " + arrayOfFloat[(i13++)] + " h: " + arrayOfFloat[(i13++)]);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  870 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   protected int l2v(int paramInt)
/*      */   {
/*  879 */     return (this.source.getLayoutFlags() & 0x1) == 0 ? paramInt : this.source.getLength() - 1 - paramInt;
/*      */   }
/*      */ 
/*      */   protected int v2l(int paramInt)
/*      */   {
/*  888 */     return (this.source.getLayoutFlags() & 0x1) == 0 ? paramInt : this.source.getLength() - 1 - paramInt;
/*      */   }
/*      */ 
/*      */   public TextLineComponent getSubset(int paramInt1, int paramInt2, int paramInt3) {
/*  892 */     return new ExtendedTextSourceLabel(this.source.getSubSource(paramInt1, paramInt2 - paramInt1, paramInt3), this.decorator);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  897 */     return this.source.toString(false);
/*      */   }
/*      */ 
/*      */   public int getNumJustificationInfos()
/*      */   {
/*  931 */     return getGV().getNumGlyphs();
/*      */   }
/*      */ 
/*      */   public void getJustificationInfos(GlyphJustificationInfo[] paramArrayOfGlyphJustificationInfo, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  941 */     StandardGlyphVector localStandardGlyphVector = getGV();
/*      */ 
/*  943 */     float[] arrayOfFloat = getCharinfo();
/*      */ 
/*  945 */     float f = localStandardGlyphVector.getFont().getSize2D();
/*      */ 
/*  947 */     GlyphJustificationInfo localGlyphJustificationInfo1 = new GlyphJustificationInfo(0.0F, false, 3, 0.0F, 0.0F, false, 3, 0.0F, 0.0F);
/*      */ 
/*  952 */     GlyphJustificationInfo localGlyphJustificationInfo2 = new GlyphJustificationInfo(f, true, 1, 0.0F, f, true, 1, 0.0F, f / 4.0F);
/*      */ 
/*  957 */     GlyphJustificationInfo localGlyphJustificationInfo3 = new GlyphJustificationInfo(f, true, 2, f, f, false, 3, 0.0F, 0.0F);
/*      */ 
/*  962 */     char[] arrayOfChar = this.source.getChars();
/*  963 */     int i = this.source.getStart();
/*      */ 
/*  967 */     int j = localStandardGlyphVector.getNumGlyphs();
/*  968 */     int k = 0;
/*  969 */     int m = j;
/*  970 */     int n = (this.source.getLayoutFlags() & 0x1) == 0 ? 1 : 0;
/*  971 */     if ((paramInt2 != 0) || (paramInt3 != this.source.getLength())) {
/*  972 */       if (n != 0) {
/*  973 */         k = paramInt2;
/*  974 */         m = paramInt3;
/*      */       } else {
/*  976 */         k = j - paramInt3;
/*  977 */         m = j - paramInt2;
/*      */       }
/*      */     }
/*      */ 
/*  981 */     for (int i1 = 0; i1 < j; i1++) {
/*  982 */       GlyphJustificationInfo localGlyphJustificationInfo4 = null;
/*  983 */       if ((i1 >= k) && (i1 < m)) {
/*  984 */         if (arrayOfFloat[(i1 * 8 + 2)] == 0.0F) {
/*  985 */           localGlyphJustificationInfo4 = localGlyphJustificationInfo1;
/*      */         } else {
/*  987 */           int i2 = v2l(i1);
/*  988 */           int i3 = arrayOfChar[(i + i2)];
/*  989 */           if (Character.isWhitespace(i3)) {
/*  990 */             localGlyphJustificationInfo4 = localGlyphJustificationInfo2;
/*      */           }
/*  992 */           else if (((i3 >= 19968) && (i3 < 40960)) || ((i3 >= 44032) && (i3 < 55216)) || ((i3 >= 63744) && (i3 < 64256)))
/*      */           {
/*  996 */             localGlyphJustificationInfo4 = localGlyphJustificationInfo3;
/*      */           }
/*  998 */           else localGlyphJustificationInfo4 = localGlyphJustificationInfo1;
/*      */         }
/*      */       }
/*      */ 
/* 1002 */       paramArrayOfGlyphJustificationInfo[(paramInt1 + i1)] = localGlyphJustificationInfo4;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TextLineComponent applyJustificationDeltas(float[] paramArrayOfFloat, int paramInt, boolean[] paramArrayOfBoolean)
/*      */   {
/* 1011 */     float[] arrayOfFloat1 = (float[])getCharinfo().clone();
/*      */ 
/* 1014 */     paramArrayOfBoolean[0] = false;
/*      */ 
/* 1018 */     StandardGlyphVector localStandardGlyphVector = (StandardGlyphVector)getGV().clone();
/* 1019 */     float[] arrayOfFloat2 = localStandardGlyphVector.getGlyphPositions(null);
/* 1020 */     int i = localStandardGlyphVector.getNumGlyphs();
/*      */ 
/* 1035 */     char[] arrayOfChar = this.source.getChars();
/* 1036 */     int j = this.source.getStart();
/*      */ 
/* 1042 */     float f1 = 0.0F;
/* 1043 */     for (int k = 0; k < i; k++) {
/* 1044 */       if (Character.isWhitespace(arrayOfChar[(j + v2l(k))])) {
/* 1045 */         arrayOfFloat2[(k * 2)] += f1;
/*      */ 
/* 1047 */         float f2 = paramArrayOfFloat[(paramInt + k * 2)] + paramArrayOfFloat[(paramInt + k * 2 + 1)];
/*      */ 
/* 1049 */         arrayOfFloat1[(k * 8 + 0)] += f1;
/* 1050 */         arrayOfFloat1[(k * 8 + 4)] += f1;
/* 1051 */         arrayOfFloat1[(k * 8 + 2)] += f2;
/*      */ 
/* 1053 */         f1 += f2;
/*      */       } else {
/* 1055 */         f1 += paramArrayOfFloat[(paramInt + k * 2)];
/*      */ 
/* 1057 */         arrayOfFloat2[(k * 2)] += f1;
/* 1058 */         arrayOfFloat1[(k * 8 + 0)] += f1;
/* 1059 */         arrayOfFloat1[(k * 8 + 4)] += f1;
/*      */ 
/* 1061 */         f1 += paramArrayOfFloat[(paramInt + k * 2 + 1)];
/*      */       }
/*      */     }
/* 1064 */     arrayOfFloat2[(i * 2)] += f1;
/*      */ 
/* 1066 */     localStandardGlyphVector.setGlyphPositions(arrayOfFloat2);
/*      */ 
/* 1076 */     ExtendedTextSourceLabel localExtendedTextSourceLabel = new ExtendedTextSourceLabel(this.source, this.decorator);
/* 1077 */     localExtendedTextSourceLabel.gv = localStandardGlyphVector;
/* 1078 */     localExtendedTextSourceLabel.charinfo = arrayOfFloat1;
/*      */ 
/* 1080 */     return localExtendedTextSourceLabel;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.ExtendedTextSourceLabel
 * JD-Core Version:    0.6.2
 */