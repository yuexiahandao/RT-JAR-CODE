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
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentEvent.ElementChange;
/*     */ import javax.swing.event.DocumentEvent.EventType;
/*     */ 
/*     */ public class PlainView extends View
/*     */   implements TabExpander
/*     */ {
/*     */   protected FontMetrics metrics;
/*     */   Element longLine;
/*     */   Font font;
/*     */   Segment lineBuffer;
/*     */   int tabSize;
/*     */   int tabBase;
/*     */   int sel0;
/*     */   int sel1;
/*     */   Color unselected;
/*     */   Color selected;
/*     */   int firstLineOffset;
/*     */ 
/*     */   public PlainView(Element paramElement)
/*     */   {
/*  48 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   protected int getTabSize()
/*     */   {
/*  57 */     Integer localInteger = (Integer)getDocument().getProperty("tabSize");
/*  58 */     int i = localInteger != null ? localInteger.intValue() : 8;
/*  59 */     return i;
/*     */   }
/*     */ 
/*     */   protected void drawLine(int paramInt1, Graphics paramGraphics, int paramInt2, int paramInt3)
/*     */   {
/*  77 */     Element localElement1 = getElement().getElement(paramInt1);
/*     */     try
/*     */     {
/*  81 */       if (localElement1.isLeaf()) {
/*  82 */         drawElement(paramInt1, localElement1, paramGraphics, paramInt2, paramInt3);
/*     */       }
/*     */       else {
/*  85 */         int i = localElement1.getElementCount();
/*  86 */         for (int j = 0; j < i; j++) {
/*  87 */           Element localElement2 = localElement1.getElement(j);
/*  88 */           paramInt2 = drawElement(paramInt1, localElement2, paramGraphics, paramInt2, paramInt3);
/*     */         }
/*     */       }
/*     */     } catch (BadLocationException localBadLocationException) {
/*  92 */       throw new StateInvariantError("Can't render line: " + paramInt1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int drawElement(int paramInt1, Element paramElement, Graphics paramGraphics, int paramInt2, int paramInt3) throws BadLocationException {
/*  97 */     int i = paramElement.getStartOffset();
/*  98 */     int j = paramElement.getEndOffset();
/*  99 */     j = Math.min(getDocument().getLength(), j);
/*     */ 
/* 101 */     if (paramInt1 == 0) {
/* 102 */       paramInt2 += this.firstLineOffset;
/*     */     }
/* 104 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/* 105 */     if (Utilities.isComposedTextAttributeDefined(localAttributeSet)) {
/* 106 */       paramGraphics.setColor(this.unselected);
/* 107 */       paramInt2 = Utilities.drawComposedText(this, localAttributeSet, paramGraphics, paramInt2, paramInt3, i - paramElement.getStartOffset(), j - paramElement.getStartOffset());
/*     */     }
/* 111 */     else if ((this.sel0 == this.sel1) || (this.selected == this.unselected))
/*     */     {
/* 113 */       paramInt2 = drawUnselectedText(paramGraphics, paramInt2, paramInt3, i, j);
/* 114 */     } else if ((i >= this.sel0) && (i <= this.sel1) && (j >= this.sel0) && (j <= this.sel1)) {
/* 115 */       paramInt2 = drawSelectedText(paramGraphics, paramInt2, paramInt3, i, j);
/* 116 */     } else if ((this.sel0 >= i) && (this.sel0 <= j)) {
/* 117 */       if ((this.sel1 >= i) && (this.sel1 <= j)) {
/* 118 */         paramInt2 = drawUnselectedText(paramGraphics, paramInt2, paramInt3, i, this.sel0);
/* 119 */         paramInt2 = drawSelectedText(paramGraphics, paramInt2, paramInt3, this.sel0, this.sel1);
/* 120 */         paramInt2 = drawUnselectedText(paramGraphics, paramInt2, paramInt3, this.sel1, j);
/*     */       } else {
/* 122 */         paramInt2 = drawUnselectedText(paramGraphics, paramInt2, paramInt3, i, this.sel0);
/* 123 */         paramInt2 = drawSelectedText(paramGraphics, paramInt2, paramInt3, this.sel0, j);
/*     */       }
/* 125 */     } else if ((this.sel1 >= i) && (this.sel1 <= j)) {
/* 126 */       paramInt2 = drawSelectedText(paramGraphics, paramInt2, paramInt3, i, this.sel1);
/* 127 */       paramInt2 = drawUnselectedText(paramGraphics, paramInt2, paramInt3, this.sel1, j);
/*     */     } else {
/* 129 */       paramInt2 = drawUnselectedText(paramGraphics, paramInt2, paramInt3, i, j);
/*     */     }
/*     */ 
/* 133 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   protected int drawUnselectedText(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws BadLocationException
/*     */   {
/* 150 */     paramGraphics.setColor(this.unselected);
/* 151 */     Document localDocument = getDocument();
/* 152 */     Segment localSegment = SegmentCache.getSharedSegment();
/* 153 */     localDocument.getText(paramInt3, paramInt4 - paramInt3, localSegment);
/* 154 */     int i = Utilities.drawTabbedText(this, localSegment, paramInt1, paramInt2, paramGraphics, this, paramInt3);
/* 155 */     SegmentCache.releaseSharedSegment(localSegment);
/* 156 */     return i;
/*     */   }
/*     */ 
/*     */   protected int drawSelectedText(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws BadLocationException
/*     */   {
/* 175 */     paramGraphics.setColor(this.selected);
/* 176 */     Document localDocument = getDocument();
/* 177 */     Segment localSegment = SegmentCache.getSharedSegment();
/* 178 */     localDocument.getText(paramInt3, paramInt4 - paramInt3, localSegment);
/* 179 */     int i = Utilities.drawTabbedText(this, localSegment, paramInt1, paramInt2, paramGraphics, this, paramInt3);
/* 180 */     SegmentCache.releaseSharedSegment(localSegment);
/* 181 */     return i;
/*     */   }
/*     */ 
/*     */   protected final Segment getLineBuffer()
/*     */   {
/* 191 */     if (this.lineBuffer == null) {
/* 192 */       this.lineBuffer = new Segment();
/*     */     }
/* 194 */     return this.lineBuffer;
/*     */   }
/*     */ 
/*     */   protected void updateMetrics()
/*     */   {
/* 204 */     Container localContainer = getContainer();
/* 205 */     Font localFont = localContainer.getFont();
/* 206 */     if (this.font != localFont)
/*     */     {
/* 209 */       calculateLongestLine();
/* 210 */       this.tabSize = (getTabSize() * this.metrics.charWidth('m'));
/*     */     }
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 228 */     updateMetrics();
/* 229 */     switch (paramInt) {
/*     */     case 0:
/* 231 */       return getLineWidth(this.longLine);
/*     */     case 1:
/* 233 */       return getElement().getElementCount() * this.metrics.getHeight();
/*     */     }
/* 235 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/* 250 */     Shape localShape = paramShape;
/* 251 */     paramShape = adjustPaintRegion(paramShape);
/* 252 */     Rectangle localRectangle1 = (Rectangle)paramShape;
/* 253 */     this.tabBase = localRectangle1.x;
/* 254 */     JTextComponent localJTextComponent = (JTextComponent)getContainer();
/* 255 */     Highlighter localHighlighter = localJTextComponent.getHighlighter();
/* 256 */     paramGraphics.setFont(localJTextComponent.getFont());
/* 257 */     this.sel0 = localJTextComponent.getSelectionStart();
/* 258 */     this.sel1 = localJTextComponent.getSelectionEnd();
/* 259 */     this.unselected = (localJTextComponent.isEnabled() ? localJTextComponent.getForeground() : localJTextComponent.getDisabledTextColor());
/*     */ 
/* 261 */     Caret localCaret = localJTextComponent.getCaret();
/* 262 */     this.selected = ((localCaret.isSelectionVisible()) && (localHighlighter != null) ? localJTextComponent.getSelectedTextColor() : this.unselected);
/*     */ 
/* 264 */     updateMetrics();
/*     */ 
/* 270 */     Rectangle localRectangle2 = paramGraphics.getClipBounds();
/* 271 */     int i = this.metrics.getHeight();
/* 272 */     int j = localRectangle1.y + localRectangle1.height - (localRectangle2.y + localRectangle2.height);
/* 273 */     int k = localRectangle2.y - localRectangle1.y;
/*     */     int m;
/*     */     int n;
/*     */     int i1;
/* 276 */     if (i > 0) {
/* 277 */       m = Math.max(0, j / i);
/* 278 */       n = Math.max(0, k / i);
/* 279 */       i1 = localRectangle1.height / i;
/* 280 */       if (localRectangle1.height % i != 0)
/* 281 */         i1++;
/*     */     }
/*     */     else {
/* 284 */       m = n = i1 = 0;
/*     */     }
/*     */ 
/* 288 */     Rectangle localRectangle3 = lineToRect(paramShape, n);
/* 289 */     int i2 = localRectangle3.y + this.metrics.getAscent();
/* 290 */     int i3 = localRectangle3.x;
/* 291 */     Element localElement1 = getElement();
/* 292 */     int i4 = localElement1.getElementCount();
/* 293 */     int i5 = Math.min(i4, i1 - m);
/* 294 */     i4--;
/* 295 */     Object localObject = (localHighlighter instanceof LayeredHighlighter) ? (LayeredHighlighter)localHighlighter : null;
/*     */ 
/* 297 */     for (int i6 = n; i6 < i5; i6++) {
/* 298 */       if (localObject != null) {
/* 299 */         Element localElement2 = localElement1.getElement(i6);
/* 300 */         if (i6 == i4) {
/* 301 */           localObject.paintLayeredHighlights(paramGraphics, localElement2.getStartOffset(), localElement2.getEndOffset(), localShape, localJTextComponent, this);
/*     */         }
/*     */         else
/*     */         {
/* 306 */           localObject.paintLayeredHighlights(paramGraphics, localElement2.getStartOffset(), localElement2.getEndOffset() - 1, localShape, localJTextComponent, this);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 311 */       drawLine(i6, paramGraphics, i3, i2);
/* 312 */       i2 += i;
/* 313 */       if (i6 == 0)
/*     */       {
/* 317 */         i3 -= this.firstLineOffset;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   Shape adjustPaintRegion(Shape paramShape)
/*     */   {
/* 328 */     return paramShape;
/*     */   }
/*     */ 
/*     */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */     throws BadLocationException
/*     */   {
/* 344 */     Document localDocument = getDocument();
/* 345 */     Element localElement1 = getElement();
/* 346 */     int i = localElement1.getElementIndex(paramInt);
/* 347 */     if (i < 0) {
/* 348 */       return lineToRect(paramShape, 0);
/*     */     }
/* 350 */     Rectangle localRectangle = lineToRect(paramShape, i);
/*     */ 
/* 353 */     this.tabBase = localRectangle.x;
/* 354 */     Element localElement2 = localElement1.getElement(i);
/* 355 */     int j = localElement2.getStartOffset();
/* 356 */     Segment localSegment = SegmentCache.getSharedSegment();
/* 357 */     localDocument.getText(j, paramInt - j, localSegment);
/* 358 */     int k = Utilities.getTabbedTextWidth(localSegment, this.metrics, this.tabBase, this, j);
/* 359 */     SegmentCache.releaseSharedSegment(localSegment);
/*     */ 
/* 362 */     localRectangle.x += k;
/* 363 */     localRectangle.width = 1;
/* 364 */     localRectangle.height = this.metrics.getHeight();
/* 365 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 381 */     paramArrayOfBias[0] = Position.Bias.Forward;
/*     */ 
/* 383 */     Rectangle localRectangle = paramShape.getBounds();
/* 384 */     Document localDocument = getDocument();
/* 385 */     int i = (int)paramFloat1;
/* 386 */     int j = (int)paramFloat2;
/* 387 */     if (j < localRectangle.y)
/*     */     {
/* 390 */       return getStartOffset();
/* 391 */     }if (j > localRectangle.y + localRectangle.height)
/*     */     {
/* 394 */       return getEndOffset() - 1;
/*     */     }
/*     */ 
/* 401 */     Element localElement1 = localDocument.getDefaultRootElement();
/* 402 */     int k = this.metrics.getHeight();
/* 403 */     int m = k > 0 ? Math.abs((j - localRectangle.y) / k) : localElement1.getElementCount() - 1;
/*     */ 
/* 406 */     if (m >= localElement1.getElementCount()) {
/* 407 */       return getEndOffset() - 1;
/*     */     }
/* 409 */     Element localElement2 = localElement1.getElement(m);
/* 410 */     int n = 0;
/* 411 */     if (m == 0) {
/* 412 */       localRectangle.x += this.firstLineOffset;
/* 413 */       localRectangle.width -= this.firstLineOffset;
/*     */     }
/* 415 */     if (i < localRectangle.x)
/*     */     {
/* 417 */       return localElement2.getStartOffset();
/* 418 */     }if (i > localRectangle.x + localRectangle.width)
/*     */     {
/* 420 */       return localElement2.getEndOffset() - 1;
/*     */     }
/*     */     try
/*     */     {
/* 424 */       int i1 = localElement2.getStartOffset();
/* 425 */       int i2 = localElement2.getEndOffset() - 1;
/* 426 */       Segment localSegment = SegmentCache.getSharedSegment();
/* 427 */       localDocument.getText(i1, i2 - i1, localSegment);
/* 428 */       this.tabBase = localRectangle.x;
/* 429 */       int i3 = i1 + Utilities.getTabbedTextOffset(localSegment, this.metrics, this.tabBase, i, this, i1);
/*     */ 
/* 431 */       SegmentCache.releaseSharedSegment(localSegment);
/* 432 */       return i3;
/*     */     } catch (BadLocationException localBadLocationException) {
/*     */     }
/* 435 */     return -1;
/*     */   }
/*     */ 
/*     */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 451 */     updateDamage(paramDocumentEvent, paramShape, paramViewFactory);
/*     */   }
/*     */ 
/*     */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 464 */     updateDamage(paramDocumentEvent, paramShape, paramViewFactory);
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 477 */     updateDamage(paramDocumentEvent, paramShape, paramViewFactory);
/*     */   }
/*     */ 
/*     */   public void setSize(float paramFloat1, float paramFloat2)
/*     */   {
/* 489 */     super.setSize(paramFloat1, paramFloat2);
/* 490 */     updateMetrics();
/*     */   }
/*     */ 
/*     */   public float nextTabStop(float paramFloat, int paramInt)
/*     */   {
/* 506 */     if (this.tabSize == 0) {
/* 507 */       return paramFloat;
/*     */     }
/* 509 */     int i = ((int)paramFloat - this.tabBase) / this.tabSize;
/* 510 */     return this.tabBase + (i + 1) * this.tabSize;
/*     */   }
/*     */ 
/*     */   protected void updateDamage(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 526 */     Container localContainer = getContainer();
/* 527 */     updateMetrics();
/* 528 */     Element localElement1 = getElement();
/* 529 */     DocumentEvent.ElementChange localElementChange = paramDocumentEvent.getChange(localElement1);
/*     */ 
/* 531 */     Object localObject1 = localElementChange != null ? localElementChange.getChildrenAdded() : null;
/* 532 */     Object localObject2 = localElementChange != null ? localElementChange.getChildrenRemoved() : null;
/*     */     int j;
/*     */     int k;
/* 533 */     if (((localObject1 != null) && (localObject1.length > 0)) || ((localObject2 != null) && (localObject2.length > 0)))
/*     */     {
/*     */       int i;
/* 536 */       if (localObject1 != null) {
/* 537 */         i = getLineWidth(this.longLine);
/* 538 */         for (j = 0; j < localObject1.length; j++) {
/* 539 */           k = getLineWidth(localObject1[j]);
/* 540 */           if (k > i) {
/* 541 */             i = k;
/* 542 */             this.longLine = localObject1[j];
/*     */           }
/*     */         }
/*     */       }
/* 546 */       if (localObject2 != null) {
/* 547 */         for (i = 0; i < localObject2.length; i++) {
/* 548 */           if (localObject2[i] == this.longLine) {
/* 549 */             calculateLongestLine();
/* 550 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 554 */       preferenceChanged(null, true, true);
/* 555 */       localContainer.repaint();
/*     */     } else {
/* 557 */       Element localElement2 = getElement();
/* 558 */       j = localElement2.getElementIndex(paramDocumentEvent.getOffset());
/* 559 */       damageLineRange(j, j, paramShape, localContainer);
/* 560 */       if (paramDocumentEvent.getType() == DocumentEvent.EventType.INSERT)
/*     */       {
/* 563 */         k = getLineWidth(this.longLine);
/* 564 */         Element localElement3 = localElement2.getElement(j);
/* 565 */         if (localElement3 == this.longLine) {
/* 566 */           preferenceChanged(null, true, false);
/* 567 */         } else if (getLineWidth(localElement3) > k) {
/* 568 */           this.longLine = localElement3;
/* 569 */           preferenceChanged(null, true, false);
/*     */         }
/* 571 */       } else if ((paramDocumentEvent.getType() == DocumentEvent.EventType.REMOVE) && 
/* 572 */         (localElement2.getElement(j) == this.longLine))
/*     */       {
/* 574 */         calculateLongestLine();
/* 575 */         preferenceChanged(null, true, false);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void damageLineRange(int paramInt1, int paramInt2, Shape paramShape, Component paramComponent)
/*     */   {
/* 593 */     if (paramShape != null) {
/* 594 */       Rectangle localRectangle1 = lineToRect(paramShape, paramInt1);
/* 595 */       Rectangle localRectangle2 = lineToRect(paramShape, paramInt2);
/* 596 */       if ((localRectangle1 != null) && (localRectangle2 != null)) {
/* 597 */         Rectangle localRectangle3 = localRectangle1.union(localRectangle2);
/* 598 */         paramComponent.repaint(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*     */       } else {
/* 600 */         paramComponent.repaint();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Rectangle lineToRect(Shape paramShape, int paramInt)
/*     */   {
/* 614 */     Rectangle localRectangle1 = null;
/* 615 */     updateMetrics();
/* 616 */     if (this.metrics != null) {
/* 617 */       Rectangle localRectangle2 = paramShape.getBounds();
/* 618 */       if (paramInt == 0) {
/* 619 */         localRectangle2.x += this.firstLineOffset;
/* 620 */         localRectangle2.width -= this.firstLineOffset;
/*     */       }
/* 622 */       localRectangle1 = new Rectangle(localRectangle2.x, localRectangle2.y + paramInt * this.metrics.getHeight(), localRectangle2.width, this.metrics.getHeight());
/*     */     }
/*     */ 
/* 625 */     return localRectangle1;
/*     */   }
/*     */ 
/*     */   private void calculateLongestLine()
/*     */   {
/* 637 */     Container localContainer = getContainer();
/* 638 */     this.font = localContainer.getFont();
/* 639 */     this.metrics = localContainer.getFontMetrics(this.font);
/* 640 */     Document localDocument = getDocument();
/* 641 */     Element localElement1 = getElement();
/* 642 */     int i = localElement1.getElementCount();
/* 643 */     int j = -1;
/* 644 */     for (int k = 0; k < i; k++) {
/* 645 */       Element localElement2 = localElement1.getElement(k);
/* 646 */       int m = getLineWidth(localElement2);
/* 647 */       if (m > j) {
/* 648 */         j = m;
/* 649 */         this.longLine = localElement2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getLineWidth(Element paramElement)
/*     */   {
/* 660 */     if (paramElement == null) {
/* 661 */       return 0; } int i = paramElement.getStartOffset();
/* 664 */     int j = paramElement.getEndOffset();
/*     */ 
/* 666 */     Segment localSegment = SegmentCache.getSharedSegment();
/*     */     int k;
/*     */     try { paramElement.getDocument().getText(i, j - i, localSegment);
/* 669 */       k = Utilities.getTabbedTextWidth(localSegment, this.metrics, this.tabBase, this, i);
/*     */     } catch (BadLocationException localBadLocationException) {
/* 671 */       k = 0;
/*     */     }
/* 673 */     SegmentCache.releaseSharedSegment(localSegment);
/* 674 */     return k;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.PlainView
 * JD-Core Version:    0.6.2
 */