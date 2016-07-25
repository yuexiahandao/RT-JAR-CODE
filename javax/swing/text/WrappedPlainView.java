/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.lang.ref.SoftReference;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentEvent.ElementChange;
/*     */ 
/*     */ public class WrappedPlainView extends BoxView
/*     */   implements TabExpander
/*     */ {
/*     */   FontMetrics metrics;
/*     */   Segment lineBuffer;
/*     */   boolean widthChanging;
/*     */   int tabBase;
/*     */   int tabSize;
/*     */   boolean wordWrap;
/*     */   int sel0;
/*     */   int sel1;
/*     */   Color unselected;
/*     */   Color selected;
/*     */ 
/*     */   public WrappedPlainView(Element paramElement)
/*     */   {
/*  62 */     this(paramElement, false);
/*     */   }
/*     */ 
/*     */   public WrappedPlainView(Element paramElement, boolean paramBoolean)
/*     */   {
/*  74 */     super(paramElement, 1);
/*  75 */     this.wordWrap = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected int getTabSize()
/*     */   {
/*  84 */     Integer localInteger = (Integer)getDocument().getProperty("tabSize");
/*  85 */     int i = localInteger != null ? localInteger.intValue() : 8;
/*  86 */     return i;
/*     */   }
/*     */ 
/*     */   protected void drawLine(int paramInt1, int paramInt2, Graphics paramGraphics, int paramInt3, int paramInt4)
/*     */   {
/* 105 */     Element localElement1 = getElement();
/* 106 */     Element localElement2 = localElement1.getElement(localElement1.getElementIndex(paramInt1));
/*     */     try
/*     */     {
/* 110 */       if (localElement2.isLeaf()) {
/* 111 */         drawText(localElement2, paramInt1, paramInt2, paramGraphics, paramInt3, paramInt4);
/*     */       }
/*     */       else {
/* 114 */         int i = localElement2.getElementIndex(paramInt1);
/* 115 */         int j = localElement2.getElementIndex(paramInt2);
/* 116 */         for (; i <= j; i++) {
/* 117 */           Element localElement3 = localElement2.getElement(i);
/* 118 */           int k = Math.max(localElement3.getStartOffset(), paramInt1);
/* 119 */           int m = Math.min(localElement3.getEndOffset(), paramInt2);
/* 120 */           paramInt3 = drawText(localElement3, k, m, paramGraphics, paramInt3, paramInt4);
/*     */         }
/*     */       }
/*     */     } catch (BadLocationException localBadLocationException) {
/* 124 */       throw new StateInvariantError("Can't render: " + paramInt1 + "," + paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int drawText(Element paramElement, int paramInt1, int paramInt2, Graphics paramGraphics, int paramInt3, int paramInt4) throws BadLocationException {
/* 129 */     paramInt2 = Math.min(getDocument().getLength(), paramInt2);
/* 130 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*     */ 
/* 132 */     if (Utilities.isComposedTextAttributeDefined(localAttributeSet)) {
/* 133 */       paramGraphics.setColor(this.unselected);
/* 134 */       paramInt3 = Utilities.drawComposedText(this, localAttributeSet, paramGraphics, paramInt3, paramInt4, paramInt1 - paramElement.getStartOffset(), paramInt2 - paramElement.getStartOffset());
/*     */     }
/* 138 */     else if ((this.sel0 == this.sel1) || (this.selected == this.unselected))
/*     */     {
/* 140 */       paramInt3 = drawUnselectedText(paramGraphics, paramInt3, paramInt4, paramInt1, paramInt2);
/* 141 */     } else if ((paramInt1 >= this.sel0) && (paramInt1 <= this.sel1) && (paramInt2 >= this.sel0) && (paramInt2 <= this.sel1)) {
/* 142 */       paramInt3 = drawSelectedText(paramGraphics, paramInt3, paramInt4, paramInt1, paramInt2);
/* 143 */     } else if ((this.sel0 >= paramInt1) && (this.sel0 <= paramInt2)) {
/* 144 */       if ((this.sel1 >= paramInt1) && (this.sel1 <= paramInt2)) {
/* 145 */         paramInt3 = drawUnselectedText(paramGraphics, paramInt3, paramInt4, paramInt1, this.sel0);
/* 146 */         paramInt3 = drawSelectedText(paramGraphics, paramInt3, paramInt4, this.sel0, this.sel1);
/* 147 */         paramInt3 = drawUnselectedText(paramGraphics, paramInt3, paramInt4, this.sel1, paramInt2);
/*     */       } else {
/* 149 */         paramInt3 = drawUnselectedText(paramGraphics, paramInt3, paramInt4, paramInt1, this.sel0);
/* 150 */         paramInt3 = drawSelectedText(paramGraphics, paramInt3, paramInt4, this.sel0, paramInt2);
/*     */       }
/* 152 */     } else if ((this.sel1 >= paramInt1) && (this.sel1 <= paramInt2)) {
/* 153 */       paramInt3 = drawSelectedText(paramGraphics, paramInt3, paramInt4, paramInt1, this.sel1);
/* 154 */       paramInt3 = drawUnselectedText(paramGraphics, paramInt3, paramInt4, this.sel1, paramInt2);
/*     */     } else {
/* 156 */       paramInt3 = drawUnselectedText(paramGraphics, paramInt3, paramInt4, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 160 */     return paramInt3;
/*     */   }
/*     */ 
/*     */   protected int drawUnselectedText(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws BadLocationException
/*     */   {
/* 177 */     paramGraphics.setColor(this.unselected);
/* 178 */     Document localDocument = getDocument();
/* 179 */     Segment localSegment = SegmentCache.getSharedSegment();
/* 180 */     localDocument.getText(paramInt3, paramInt4 - paramInt3, localSegment);
/* 181 */     int i = Utilities.drawTabbedText(this, localSegment, paramInt1, paramInt2, paramGraphics, this, paramInt3);
/* 182 */     SegmentCache.releaseSharedSegment(localSegment);
/* 183 */     return i;
/*     */   }
/*     */ 
/*     */   protected int drawSelectedText(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws BadLocationException
/*     */   {
/* 202 */     paramGraphics.setColor(this.selected);
/* 203 */     Document localDocument = getDocument();
/* 204 */     Segment localSegment = SegmentCache.getSharedSegment();
/* 205 */     localDocument.getText(paramInt3, paramInt4 - paramInt3, localSegment);
/* 206 */     int i = Utilities.drawTabbedText(this, localSegment, paramInt1, paramInt2, paramGraphics, this, paramInt3);
/* 207 */     SegmentCache.releaseSharedSegment(localSegment);
/* 208 */     return i;
/*     */   }
/*     */ 
/*     */   protected final Segment getLineBuffer()
/*     */   {
/* 218 */     if (this.lineBuffer == null) {
/* 219 */       this.lineBuffer = new Segment();
/*     */     }
/* 221 */     return this.lineBuffer;
/*     */   }
/*     */ 
/*     */   protected int calculateBreakPosition(int paramInt1, int paramInt2)
/*     */   {
/* 234 */     Segment localSegment = SegmentCache.getSharedSegment();
/* 235 */     loadText(localSegment, paramInt1, paramInt2);
/* 236 */     int j = getWidth();
/*     */     int i;
/* 237 */     if (this.wordWrap) {
/* 238 */       i = paramInt1 + Utilities.getBreakLocation(localSegment, this.metrics, this.tabBase, this.tabBase + j, this, paramInt1);
/*     */     }
/*     */     else
/*     */     {
/* 242 */       i = paramInt1 + Utilities.getTabbedTextOffset(localSegment, this.metrics, this.tabBase, this.tabBase + j, this, paramInt1, false);
/*     */     }
/*     */ 
/* 246 */     SegmentCache.releaseSharedSegment(localSegment);
/* 247 */     return i;
/*     */   }
/*     */ 
/*     */   protected void loadChildren(ViewFactory paramViewFactory)
/*     */   {
/* 261 */     Element localElement = getElement();
/* 262 */     int i = localElement.getElementCount();
/* 263 */     if (i > 0) {
/* 264 */       View[] arrayOfView = new View[i];
/* 265 */       for (int j = 0; j < i; j++) {
/* 266 */         arrayOfView[j] = new WrappedLine(localElement.getElement(j));
/*     */       }
/* 268 */       replace(0, 0, arrayOfView);
/*     */     }
/*     */   }
/*     */ 
/*     */   void updateChildren(DocumentEvent paramDocumentEvent, Shape paramShape)
/*     */   {
/* 277 */     Element localElement = getElement();
/* 278 */     DocumentEvent.ElementChange localElementChange = paramDocumentEvent.getChange(localElement);
/* 279 */     if (localElementChange != null)
/*     */     {
/* 281 */       Element[] arrayOfElement1 = localElementChange.getChildrenRemoved();
/* 282 */       Element[] arrayOfElement2 = localElementChange.getChildrenAdded();
/* 283 */       View[] arrayOfView = new View[arrayOfElement2.length];
/* 284 */       for (int i = 0; i < arrayOfElement2.length; i++) {
/* 285 */         arrayOfView[i] = new WrappedLine(arrayOfElement2[i]);
/*     */       }
/* 287 */       replace(localElementChange.getIndex(), arrayOfElement1.length, arrayOfView);
/*     */ 
/* 290 */       if (paramShape != null) {
/* 291 */         preferenceChanged(null, true, true);
/* 292 */         getContainer().repaint();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 297 */     updateMetrics();
/*     */   }
/*     */ 
/*     */   final void loadText(Segment paramSegment, int paramInt1, int paramInt2)
/*     */   {
/*     */     try
/*     */     {
/* 308 */       Document localDocument = getDocument();
/* 309 */       localDocument.getText(paramInt1, paramInt2 - paramInt1, paramSegment);
/*     */     } catch (BadLocationException localBadLocationException) {
/* 311 */       throw new StateInvariantError("Can't get line text");
/*     */     }
/*     */   }
/*     */ 
/*     */   final void updateMetrics() {
/* 316 */     Container localContainer = getContainer();
/* 317 */     Font localFont = localContainer.getFont();
/* 318 */     this.metrics = localContainer.getFontMetrics(localFont);
/* 319 */     this.tabSize = (getTabSize() * this.metrics.charWidth('m'));
/*     */   }
/*     */ 
/*     */   public float nextTabStop(float paramFloat, int paramInt)
/*     */   {
/* 335 */     if (this.tabSize == 0)
/* 336 */       return paramFloat;
/* 337 */     int i = ((int)paramFloat - this.tabBase) / this.tabSize;
/* 338 */     return this.tabBase + (i + 1) * this.tabSize;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/* 356 */     Rectangle localRectangle = (Rectangle)paramShape;
/* 357 */     this.tabBase = localRectangle.x;
/* 358 */     JTextComponent localJTextComponent = (JTextComponent)getContainer();
/* 359 */     this.sel0 = localJTextComponent.getSelectionStart();
/* 360 */     this.sel1 = localJTextComponent.getSelectionEnd();
/* 361 */     this.unselected = (localJTextComponent.isEnabled() ? localJTextComponent.getForeground() : localJTextComponent.getDisabledTextColor());
/*     */ 
/* 363 */     Caret localCaret = localJTextComponent.getCaret();
/* 364 */     this.selected = ((localCaret.isSelectionVisible()) && (localJTextComponent.getHighlighter() != null) ? localJTextComponent.getSelectedTextColor() : this.unselected);
/*     */ 
/* 366 */     paramGraphics.setFont(localJTextComponent.getFont());
/*     */ 
/* 369 */     super.paint(paramGraphics, paramShape);
/*     */   }
/*     */ 
/*     */   public void setSize(float paramFloat1, float paramFloat2)
/*     */   {
/* 381 */     updateMetrics();
/* 382 */     if ((int)paramFloat1 != getWidth())
/*     */     {
/* 385 */       preferenceChanged(null, true, true);
/* 386 */       this.widthChanging = true;
/*     */     }
/* 388 */     super.setSize(paramFloat1, paramFloat2);
/* 389 */     this.widthChanging = false;
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 408 */     updateMetrics();
/* 409 */     return super.getPreferredSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt)
/*     */   {
/* 428 */     updateMetrics();
/* 429 */     return super.getMinimumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt)
/*     */   {
/* 448 */     updateMetrics();
/* 449 */     return super.getMaximumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 463 */     updateChildren(paramDocumentEvent, paramShape);
/*     */ 
/* 465 */     Rectangle localRectangle = (paramShape != null) && (isAllocationValid()) ? getInsideAllocation(paramShape) : null;
/*     */ 
/* 467 */     int i = paramDocumentEvent.getOffset();
/* 468 */     View localView = getViewAtPosition(i, localRectangle);
/* 469 */     if (localView != null)
/* 470 */       localView.insertUpdate(paramDocumentEvent, localRectangle, paramViewFactory);
/*     */   }
/*     */ 
/*     */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 485 */     updateChildren(paramDocumentEvent, paramShape);
/*     */ 
/* 487 */     Rectangle localRectangle = (paramShape != null) && (isAllocationValid()) ? getInsideAllocation(paramShape) : null;
/*     */ 
/* 489 */     int i = paramDocumentEvent.getOffset();
/* 490 */     View localView = getViewAtPosition(i, localRectangle);
/* 491 */     if (localView != null)
/* 492 */       localView.removeUpdate(paramDocumentEvent, localRectangle, paramViewFactory);
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 506 */     updateChildren(paramDocumentEvent, paramShape);
/*     */   }
/*     */ 
/*     */   class WrappedLine extends View
/*     */   {
/*     */     int lineCount;
/* 851 */     SoftReference<int[]> lineCache = null;
/*     */ 
/*     */     WrappedLine(Element arg2)
/*     */     {
/* 534 */       super();
/* 535 */       this.lineCount = -1;
/*     */     }
/*     */ 
/*     */     public float getPreferredSpan(int paramInt)
/*     */     {
/* 550 */       switch (paramInt) {
/*     */       case 0:
/* 552 */         float f = WrappedPlainView.this.getWidth();
/* 553 */         if (f == 2.147484E+009F)
/*     */         {
/* 556 */           return 100.0F;
/*     */         }
/* 558 */         return f;
/*     */       case 1:
/* 560 */         if ((this.lineCount < 0) || (WrappedPlainView.this.widthChanging)) {
/* 561 */           breakLines(getStartOffset());
/*     */         }
/* 563 */         return this.lineCount * WrappedPlainView.this.metrics.getHeight();
/*     */       }
/* 565 */       throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics, Shape paramShape)
/*     */     {
/* 579 */       Rectangle localRectangle = (Rectangle)paramShape;
/* 580 */       int i = localRectangle.y + WrappedPlainView.this.metrics.getAscent();
/* 581 */       int j = localRectangle.x;
/*     */ 
/* 583 */       JTextComponent localJTextComponent = (JTextComponent)getContainer();
/* 584 */       Highlighter localHighlighter = localJTextComponent.getHighlighter();
/* 585 */       Object localObject = (localHighlighter instanceof LayeredHighlighter) ? (LayeredHighlighter)localHighlighter : null;
/*     */ 
/* 588 */       int k = getStartOffset();
/* 589 */       int m = getEndOffset();
/* 590 */       int n = k;
/* 591 */       int[] arrayOfInt = getLineEnds();
/* 592 */       for (int i1 = 0; i1 < this.lineCount; i1++) {
/* 593 */         int i2 = arrayOfInt == null ? m : k + arrayOfInt[i1];
/*     */ 
/* 595 */         if (localObject != null) {
/* 596 */           int i3 = i2 == m ? i2 - 1 : i2;
/*     */ 
/* 599 */           localObject.paintLayeredHighlights(paramGraphics, n, i3, paramShape, localJTextComponent, this);
/*     */         }
/* 601 */         WrappedPlainView.this.drawLine(n, i2, paramGraphics, j, i);
/*     */ 
/* 603 */         n = i2;
/* 604 */         i += WrappedPlainView.this.metrics.getHeight();
/*     */       }
/*     */     }
/*     */ 
/*     */     public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */       throws BadLocationException
/*     */     {
/* 621 */       Rectangle localRectangle = paramShape.getBounds();
/* 622 */       localRectangle.height = WrappedPlainView.this.metrics.getHeight();
/* 623 */       localRectangle.width = 1;
/*     */ 
/* 625 */       int i = getStartOffset();
/* 626 */       if ((paramInt < i) || (paramInt > getEndOffset())) {
/* 627 */         throw new BadLocationException("Position out of range", paramInt);
/*     */       }
/*     */ 
/* 630 */       int j = paramBias == Position.Bias.Forward ? paramInt : Math.max(i, paramInt - 1);
/*     */ 
/* 632 */       int k = 0;
/* 633 */       int[] arrayOfInt = getLineEnds();
/* 634 */       if (arrayOfInt != null) {
/* 635 */         k = findLine(j - i);
/* 636 */         if (k > 0) {
/* 637 */           i += arrayOfInt[(k - 1)];
/*     */         }
/* 639 */         localRectangle.y += localRectangle.height * k;
/*     */       }
/*     */ 
/* 642 */       if (paramInt > i) {
/* 643 */         Segment localSegment = SegmentCache.getSharedSegment();
/* 644 */         WrappedPlainView.this.loadText(localSegment, i, paramInt);
/* 645 */         localRectangle.x += Utilities.getTabbedTextWidth(localSegment, WrappedPlainView.this.metrics, localRectangle.x, WrappedPlainView.this, i);
/*     */ 
/* 647 */         SegmentCache.releaseSharedSegment(localSegment);
/*     */       }
/* 649 */       return localRectangle;
/*     */     }
/*     */ 
/*     */     public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */     {
/* 665 */       paramArrayOfBias[0] = Position.Bias.Forward;
/*     */ 
/* 667 */       Rectangle localRectangle = (Rectangle)paramShape;
/* 668 */       int i = (int)paramFloat1;
/* 669 */       int j = (int)paramFloat2;
/* 670 */       if (j < localRectangle.y)
/*     */       {
/* 673 */         return getStartOffset();
/* 674 */       }if (j > localRectangle.y + localRectangle.height)
/*     */       {
/* 677 */         return getEndOffset() - 1;
/*     */       }
/*     */ 
/* 684 */       localRectangle.height = WrappedPlainView.this.metrics.getHeight();
/* 685 */       int k = localRectangle.height > 0 ? (j - localRectangle.y) / localRectangle.height : this.lineCount - 1;
/*     */ 
/* 687 */       if (k >= this.lineCount) {
/* 688 */         return getEndOffset() - 1;
/*     */       }
/* 690 */       int m = getStartOffset();
/*     */       int n;
/* 692 */       if (this.lineCount == 1) {
/* 693 */         n = getEndOffset();
/*     */       } else {
/* 695 */         localObject = getLineEnds();
/* 696 */         n = m + localObject[k];
/* 697 */         if (k > 0) {
/* 698 */           m += localObject[(k - 1)];
/*     */         }
/*     */       }
/*     */ 
/* 702 */       if (i < localRectangle.x)
/*     */       {
/* 704 */         return m;
/* 705 */       }if (i > localRectangle.x + localRectangle.width)
/*     */       {
/* 707 */         return n - 1;
/*     */       }
/*     */ 
/* 710 */       Object localObject = SegmentCache.getSharedSegment();
/* 711 */       WrappedPlainView.this.loadText((Segment)localObject, m, n);
/* 712 */       int i1 = Utilities.getTabbedTextOffset((Segment)localObject, WrappedPlainView.this.metrics, localRectangle.x, i, WrappedPlainView.this, m);
/*     */ 
/* 715 */       SegmentCache.releaseSharedSegment((Segment)localObject);
/* 716 */       return Math.min(m + i1, n - 1);
/*     */     }
/*     */ 
/*     */     public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */     {
/* 723 */       update(paramDocumentEvent, paramShape);
/*     */     }
/*     */ 
/*     */     public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory) {
/* 727 */       update(paramDocumentEvent, paramShape);
/*     */     }
/*     */ 
/*     */     private void update(DocumentEvent paramDocumentEvent, Shape paramShape) {
/* 731 */       int i = this.lineCount;
/* 732 */       breakLines(paramDocumentEvent.getOffset());
/* 733 */       if (i != this.lineCount) {
/* 734 */         WrappedPlainView.this.preferenceChanged(this, false, true);
/*     */ 
/* 736 */         getContainer().repaint();
/* 737 */       } else if (paramShape != null) {
/* 738 */         Container localContainer = getContainer();
/* 739 */         Rectangle localRectangle = (Rectangle)paramShape;
/* 740 */         localContainer.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */       }
/*     */     }
/*     */ 
/*     */     final int[] getLineEnds()
/*     */     {
/* 749 */       if (this.lineCache == null) {
/* 750 */         return null;
/*     */       }
/* 752 */       int[] arrayOfInt = (int[])this.lineCache.get();
/* 753 */       if (arrayOfInt == null)
/*     */       {
/* 755 */         return breakLines(getStartOffset());
/*     */       }
/* 757 */       return arrayOfInt;
/*     */     }
/*     */ 
/*     */     final int[] breakLines(int paramInt)
/*     */     {
/* 768 */       Object localObject1 = this.lineCache == null ? null : (int[])this.lineCache.get();
/* 769 */       Object localObject2 = localObject1;
/* 770 */       int i = getStartOffset();
/* 771 */       int j = 0;
/* 772 */       if (localObject1 != null) {
/* 773 */         j = findLine(paramInt - i);
/* 774 */         if (j > 0) {
/* 775 */           j--;
/*     */         }
/*     */       }
/*     */ 
/* 779 */       int k = j == 0 ? i : i + localObject1[(j - 1)];
/* 780 */       int m = getEndOffset();
/*     */       int n;
/* 781 */       while (k < m) {
/* 782 */         n = WrappedPlainView.this.calculateBreakPosition(k, m);
/* 783 */         n++; k = n == k ? n : n;
/*     */ 
/* 785 */         if ((j == 0) && (k >= m))
/*     */         {
/* 787 */           this.lineCache = null;
/* 788 */           localObject1 = null;
/* 789 */           j = 1;
/* 790 */           break;
/* 791 */         }if ((localObject1 == null) || (j >= localObject1.length))
/*     */         {
/* 794 */           double d = (m - i) / (k - i);
/* 795 */           int i1 = (int)Math.ceil((j + 1) * d);
/* 796 */           i1 = Math.max(i1, j + 2);
/* 797 */           int[] arrayOfInt2 = new int[i1];
/* 798 */           if (localObject1 != null) {
/* 799 */             System.arraycopy(localObject1, 0, arrayOfInt2, 0, j);
/*     */           }
/* 801 */           localObject1 = arrayOfInt2;
/*     */         }
/* 803 */         localObject1[(j++)] = (k - i);
/*     */       }
/*     */ 
/* 806 */       this.lineCount = j;
/* 807 */       if (this.lineCount > 1)
/*     */       {
/* 809 */         n = this.lineCount + this.lineCount / 3;
/* 810 */         if (localObject1.length > n) {
/* 811 */           int[] arrayOfInt1 = new int[n];
/* 812 */           System.arraycopy(localObject1, 0, arrayOfInt1, 0, this.lineCount);
/* 813 */           localObject1 = arrayOfInt1;
/*     */         }
/*     */       }
/*     */ 
/* 817 */       if ((localObject1 != null) && (localObject1 != localObject2)) {
/* 818 */         this.lineCache = new SoftReference(localObject1);
/*     */       }
/* 820 */       return localObject1;
/*     */     }
/*     */ 
/*     */     private int findLine(int paramInt)
/*     */     {
/* 829 */       int[] arrayOfInt = (int[])this.lineCache.get();
/* 830 */       if (paramInt < arrayOfInt[0])
/* 831 */         return 0;
/* 832 */       if (paramInt > arrayOfInt[(this.lineCount - 1)]) {
/* 833 */         return this.lineCount;
/*     */       }
/* 835 */       return findLine(arrayOfInt, paramInt, 0, this.lineCount - 1);
/*     */     }
/*     */ 
/*     */     private int findLine(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 840 */       if (paramInt3 - paramInt2 <= 1) {
/* 841 */         return paramInt3;
/*     */       }
/* 843 */       int i = (paramInt3 + paramInt2) / 2;
/* 844 */       return paramInt1 < paramArrayOfInt[i] ? findLine(paramArrayOfInt, paramInt1, paramInt2, i) : findLine(paramArrayOfInt, paramInt1, i, paramInt3);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.WrappedPlainView
 * JD-Core Version:    0.6.2
 */