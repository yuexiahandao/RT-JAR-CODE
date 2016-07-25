/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.text.BreakIterator;
/*      */ import java.util.BitSet;
/*      */ import java.util.Locale;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class GlyphView extends View
/*      */   implements TabableView, Cloneable
/*      */ {
/* 1082 */   private byte[] selections = null;
/*      */   int offset;
/*      */   int length;
/*      */   boolean impliedCR;
/*      */   boolean skipWidth;
/*      */   TabExpander expander;
/* 1096 */   private float minimumSpan = -1.0F;
/*      */ 
/* 1099 */   private int[] breakSpots = null;
/*      */   int x;
/*      */   GlyphPainter painter;
/*      */   static GlyphPainter defaultPainter;
/* 1116 */   private JustificationInfo justificationInfo = null;
/*      */ 
/*      */   public GlyphView(Element paramElement)
/*      */   {
/*   73 */     super(paramElement);
/*   74 */     this.offset = 0;
/*   75 */     this.length = 0;
/*   76 */     Element localElement = paramElement.getParentElement();
/*   77 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*      */ 
/*   80 */     this.impliedCR = ((localAttributeSet != null) && (localAttributeSet.getAttribute("CR") != null) && (localElement != null) && (localElement.getElementCount() > 1));
/*      */ 
/*   83 */     this.skipWidth = paramElement.getName().equals("br");
/*      */   }
/*      */ 
/*      */   protected final Object clone()
/*      */   {
/*      */     Object localObject;
/*      */     try
/*      */     {
/*   95 */       localObject = super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*   97 */       localObject = null;
/*      */     }
/*   99 */     return localObject;
/*      */   }
/*      */ 
/*      */   public GlyphPainter getGlyphPainter()
/*      */   {
/*  108 */     return this.painter;
/*      */   }
/*      */ 
/*      */   public void setGlyphPainter(GlyphPainter paramGlyphPainter)
/*      */   {
/*  115 */     this.painter = paramGlyphPainter;
/*      */   }
/*      */ 
/*      */   public Segment getText(int paramInt1, int paramInt2)
/*      */   {
/*  132 */     Segment localSegment = SegmentCache.getSharedSegment();
/*      */     try {
/*  134 */       Document localDocument = getDocument();
/*  135 */       localDocument.getText(paramInt1, paramInt2 - paramInt1, localSegment);
/*      */     } catch (BadLocationException localBadLocationException) {
/*  137 */       throw new StateInvariantError("GlyphView: Stale view: " + localBadLocationException);
/*      */     }
/*  139 */     return localSegment;
/*      */   }
/*      */ 
/*      */   public Color getBackground()
/*      */   {
/*  150 */     Document localDocument = getDocument();
/*  151 */     if ((localDocument instanceof StyledDocument)) {
/*  152 */       AttributeSet localAttributeSet = getAttributes();
/*  153 */       if (localAttributeSet.isDefined(StyleConstants.Background)) {
/*  154 */         return ((StyledDocument)localDocument).getBackground(localAttributeSet);
/*      */       }
/*      */     }
/*  157 */     return null;
/*      */   }
/*      */ 
/*      */   public Color getForeground()
/*      */   {
/*  171 */     Document localDocument = getDocument();
/*  172 */     if ((localDocument instanceof StyledDocument)) {
/*  173 */       localObject = getAttributes();
/*  174 */       return ((StyledDocument)localDocument).getForeground((AttributeSet)localObject);
/*      */     }
/*  176 */     Object localObject = getContainer();
/*  177 */     if (localObject != null) {
/*  178 */       return ((Component)localObject).getForeground();
/*      */     }
/*  180 */     return null;
/*      */   }
/*      */ 
/*      */   public Font getFont()
/*      */   {
/*  193 */     Document localDocument = getDocument();
/*  194 */     if ((localDocument instanceof StyledDocument)) {
/*  195 */       localObject = getAttributes();
/*  196 */       return ((StyledDocument)localDocument).getFont((AttributeSet)localObject);
/*      */     }
/*  198 */     Object localObject = getContainer();
/*  199 */     if (localObject != null) {
/*  200 */       return ((Component)localObject).getFont();
/*      */     }
/*  202 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isUnderline()
/*      */   {
/*  210 */     AttributeSet localAttributeSet = getAttributes();
/*  211 */     return StyleConstants.isUnderline(localAttributeSet);
/*      */   }
/*      */ 
/*      */   public boolean isStrikeThrough()
/*      */   {
/*  220 */     AttributeSet localAttributeSet = getAttributes();
/*  221 */     return StyleConstants.isStrikeThrough(localAttributeSet);
/*      */   }
/*      */ 
/*      */   public boolean isSubscript()
/*      */   {
/*  228 */     AttributeSet localAttributeSet = getAttributes();
/*  229 */     return StyleConstants.isSubscript(localAttributeSet);
/*      */   }
/*      */ 
/*      */   public boolean isSuperscript()
/*      */   {
/*  236 */     AttributeSet localAttributeSet = getAttributes();
/*  237 */     return StyleConstants.isSuperscript(localAttributeSet);
/*      */   }
/*      */ 
/*      */   public TabExpander getTabExpander()
/*      */   {
/*  244 */     return this.expander;
/*      */   }
/*      */ 
/*      */   protected void checkPainter()
/*      */   {
/*  252 */     if (this.painter == null) {
/*  253 */       if (defaultPainter == null)
/*      */       {
/*  255 */         String str = "javax.swing.text.GlyphPainter1";
/*      */         try
/*      */         {
/*  258 */           ClassLoader localClassLoader = getClass().getClassLoader();
/*      */           Class localClass;
/*  259 */           if (localClassLoader != null)
/*  260 */             localClass = localClassLoader.loadClass(str);
/*      */           else {
/*  262 */             localClass = Class.forName(str);
/*      */           }
/*  264 */           Object localObject = localClass.newInstance();
/*  265 */           if ((localObject instanceof GlyphPainter))
/*  266 */             defaultPainter = (GlyphPainter)localObject;
/*      */         }
/*      */         catch (Throwable localThrowable) {
/*  269 */           throw new StateInvariantError("GlyphView: Can't load glyph painter: " + str);
/*      */         }
/*      */       }
/*      */ 
/*  273 */       setGlyphPainter(defaultPainter.getPainter(this, getStartOffset(), getEndOffset()));
/*      */     }
/*      */   }
/*      */ 
/*      */   public float getTabbedSpan(float paramFloat, TabExpander paramTabExpander)
/*      */   {
/*  291 */     checkPainter();
/*      */ 
/*  293 */     TabExpander localTabExpander = this.expander;
/*  294 */     this.expander = paramTabExpander;
/*      */ 
/*  296 */     if (this.expander != localTabExpander)
/*      */     {
/*  299 */       preferenceChanged(null, true, false);
/*      */     }
/*      */ 
/*  302 */     this.x = ((int)paramFloat);
/*  303 */     int i = getStartOffset();
/*  304 */     int j = getEndOffset();
/*  305 */     float f = this.painter.getSpan(this, i, j, this.expander, paramFloat);
/*  306 */     return f;
/*      */   }
/*      */ 
/*      */   public float getPartialSpan(int paramInt1, int paramInt2)
/*      */   {
/*  329 */     checkPainter();
/*  330 */     float f = this.painter.getSpan(this, paramInt1, paramInt2, this.expander, this.x);
/*  331 */     return f;
/*      */   }
/*      */ 
/*      */   public int getStartOffset()
/*      */   {
/*  343 */     Element localElement = getElement();
/*  344 */     return this.length > 0 ? localElement.getStartOffset() + this.offset : localElement.getStartOffset();
/*      */   }
/*      */ 
/*      */   public int getEndOffset()
/*      */   {
/*  354 */     Element localElement = getElement();
/*  355 */     return this.length > 0 ? localElement.getStartOffset() + this.offset + this.length : localElement.getEndOffset();
/*      */   }
/*      */ 
/*      */   private void initSelections(int paramInt1, int paramInt2)
/*      */   {
/*  362 */     int i = paramInt2 - paramInt1 + 1;
/*  363 */     if ((this.selections == null) || (i > this.selections.length)) {
/*  364 */       this.selections = new byte[i];
/*  365 */       return;
/*      */     }
/*  367 */     for (int j = 0; j < i; this.selections[(j++)] = 0);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, Shape paramShape)
/*      */   {
/*  377 */     checkPainter();
/*      */ 
/*  379 */     int i = 0;
/*  380 */     Container localContainer = getContainer();
/*  381 */     int j = getStartOffset();
/*  382 */     int k = getEndOffset();
/*  383 */     Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*  384 */     Color localColor1 = getBackground();
/*  385 */     Color localColor2 = getForeground();
/*      */ 
/*  387 */     if ((localContainer != null) && (!localContainer.isEnabled())) {
/*  388 */       localColor2 = (localContainer instanceof JTextComponent) ? ((JTextComponent)localContainer).getDisabledTextColor() : UIManager.getColor("textInactiveText");
/*      */     }
/*      */ 
/*  392 */     if (localColor1 != null) {
/*  393 */       paramGraphics.setColor(localColor1);
/*  394 */       paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     }
/*      */     JTextComponent localJTextComponent;
/*      */     Object localObject;
/*  396 */     if ((localContainer instanceof JTextComponent)) {
/*  397 */       localJTextComponent = (JTextComponent)localContainer;
/*  398 */       localObject = localJTextComponent.getHighlighter();
/*  399 */       if ((localObject instanceof LayeredHighlighter)) {
/*  400 */         ((LayeredHighlighter)localObject).paintLayeredHighlights(paramGraphics, j, k, paramShape, localJTextComponent, this);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  405 */     if (Utilities.isComposedTextElement(getElement())) {
/*  406 */       Utilities.paintComposedText(paramGraphics, paramShape.getBounds(), this);
/*  407 */       i = 1;
/*  408 */     } else if ((localContainer instanceof JTextComponent)) {
/*  409 */       localJTextComponent = (JTextComponent)localContainer;
/*  410 */       localObject = localJTextComponent.getSelectedTextColor();
/*      */ 
/*  412 */       if ((localJTextComponent.getHighlighter() != null) && (localObject != null) && (!((Color)localObject).equals(localColor2)))
/*      */       {
/*  417 */         Highlighter.Highlight[] arrayOfHighlight = localJTextComponent.getHighlighter().getHighlights();
/*  418 */         if (arrayOfHighlight.length != 0) {
/*  419 */           int m = 0;
/*  420 */           int n = 0;
/*      */           int i3;
/*      */           int i4;
/*  421 */           for (int i1 = 0; i1 < arrayOfHighlight.length; i1++) {
/*  422 */             Highlighter.Highlight localHighlight = arrayOfHighlight[i1];
/*  423 */             i3 = localHighlight.getStartOffset();
/*  424 */             i4 = localHighlight.getEndOffset();
/*  425 */             if ((i3 <= k) && (i4 >= j))
/*      */             {
/*  429 */               if (SwingUtilities2.useSelectedTextColor(localHighlight, localJTextComponent))
/*      */               {
/*  432 */                 if ((i3 <= j) && (i4 >= k))
/*      */                 {
/*  434 */                   paintTextUsingColor(paramGraphics, paramShape, (Color)localObject, j, k);
/*  435 */                   i = 1;
/*  436 */                   break;
/*      */                 }
/*      */ 
/*  440 */                 if (m == 0) {
/*  441 */                   initSelections(j, k);
/*  442 */                   m = 1;
/*      */                 }
/*  444 */                 i3 = Math.max(j, i3);
/*  445 */                 i4 = Math.min(k, i4);
/*  446 */                 paintTextUsingColor(paramGraphics, paramShape, (Color)localObject, i3, i4);
/*      */                 int tmp426_425 = (i3 - j);
/*      */                 byte[] tmp426_418 = this.selections; tmp426_418[tmp426_425] = ((byte)(tmp426_418[tmp426_425] + 1));
/*      */                 int tmp441_440 = (i4 - j);
/*      */                 byte[] tmp441_433 = this.selections; tmp441_433[tmp441_440] = ((byte)(tmp441_433[tmp441_440] - 1));
/*      */ 
/*  453 */                 n++;
/*      */               }
/*      */             }
/*      */           }
/*  456 */           if ((i == 0) && (n > 0))
/*      */           {
/*  458 */             i1 = -1;
/*  459 */             int i2 = 0;
/*  460 */             i3 = k - j;
/*  461 */             while (i1++ < i3)
/*      */             {
/*  463 */               while ((i1 < i3) && (this.selections[i1] == 0))
/*  464 */                 i1++;
/*  465 */               if (i2 != i1)
/*      */               {
/*  467 */                 paintTextUsingColor(paramGraphics, paramShape, localColor2, j + i2, j + i1);
/*      */               }
/*      */ 
/*  470 */               i4 = 0;
/*      */ 
/*  472 */               while ((i1 < i3) && (i4 += this.selections[i1] != 0))
/*  473 */                 i1++;
/*  474 */               i2 = i1;
/*      */             }
/*  476 */             i = 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  481 */     if (i == 0)
/*  482 */       paintTextUsingColor(paramGraphics, paramShape, localColor2, j, k);
/*      */   }
/*      */ 
/*      */   final void paintTextUsingColor(Graphics paramGraphics, Shape paramShape, Color paramColor, int paramInt1, int paramInt2)
/*      */   {
/*  490 */     paramGraphics.setColor(paramColor);
/*  491 */     this.painter.paint(this, paramGraphics, paramShape, paramInt1, paramInt2);
/*      */ 
/*  494 */     boolean bool1 = isUnderline();
/*  495 */     boolean bool2 = isStrikeThrough();
/*  496 */     if ((bool1) || (bool2))
/*      */     {
/*  498 */       Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*  499 */       View localView = getParent();
/*  500 */       if ((localView != null) && (localView.getEndOffset() == paramInt2))
/*      */       {
/*  502 */         Segment localSegment = getText(paramInt1, paramInt2);
/*  503 */         while (Character.isWhitespace(localSegment.last())) {
/*  504 */           paramInt2--;
/*  505 */           localSegment.count -= 1;
/*      */         }
/*  507 */         SegmentCache.releaseSharedSegment(localSegment);
/*      */       }
/*  509 */       int i = localRectangle.x;
/*  510 */       int j = getStartOffset();
/*  511 */       if (j != paramInt1) {
/*  512 */         i += (int)this.painter.getSpan(this, j, paramInt1, getTabExpander(), i);
/*      */       }
/*  514 */       int k = i + (int)this.painter.getSpan(this, paramInt1, paramInt2, getTabExpander(), i);
/*      */ 
/*  517 */       int m = localRectangle.y + (int)(this.painter.getHeight(this) - this.painter.getDescent(this));
/*      */       int n;
/*  518 */       if (bool1) {
/*  519 */         n = m + 1;
/*  520 */         paramGraphics.drawLine(i, n, k, n);
/*      */       }
/*  522 */       if (bool2)
/*      */       {
/*  524 */         n = m - (int)(this.painter.getAscent(this) * 0.3F);
/*  525 */         paramGraphics.drawLine(i, n, k, n);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public float getMinimumSpan(int paramInt)
/*      */   {
/*  544 */     switch (paramInt) {
/*      */     case 0:
/*  546 */       if (this.minimumSpan < 0.0F) {
/*  547 */         this.minimumSpan = 0.0F;
/*  548 */         int i = getStartOffset();
/*  549 */         int j = getEndOffset();
/*  550 */         while (j > i) {
/*  551 */           int k = getBreakSpot(i, j);
/*  552 */           if (k == -1)
/*      */           {
/*  554 */             k = i;
/*      */           }
/*  556 */           this.minimumSpan = Math.max(this.minimumSpan, getPartialSpan(k, j));
/*      */ 
/*  559 */           j = k - 1;
/*      */         }
/*      */       }
/*  562 */       return this.minimumSpan;
/*      */     case 1:
/*  564 */       return super.getMinimumSpan(paramInt);
/*      */     }
/*  566 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*      */   }
/*      */ 
/*      */   public float getPreferredSpan(int paramInt)
/*      */   {
/*  581 */     if (this.impliedCR) {
/*  582 */       return 0.0F;
/*      */     }
/*  584 */     checkPainter();
/*  585 */     int i = getStartOffset();
/*  586 */     int j = getEndOffset();
/*  587 */     switch (paramInt) {
/*      */     case 0:
/*  589 */       if (this.skipWidth) {
/*  590 */         return 0.0F;
/*      */       }
/*  592 */       return this.painter.getSpan(this, i, j, this.expander, this.x);
/*      */     case 1:
/*  594 */       float f = this.painter.getHeight(this);
/*  595 */       if (isSuperscript()) {
/*  596 */         f += f / 3.0F;
/*      */       }
/*  598 */       return f;
/*      */     }
/*  600 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*      */   }
/*      */ 
/*      */   public float getAlignment(int paramInt)
/*      */   {
/*  618 */     checkPainter();
/*  619 */     if (paramInt == 1) {
/*  620 */       boolean bool1 = isSuperscript();
/*  621 */       boolean bool2 = isSubscript();
/*  622 */       float f1 = this.painter.getHeight(this);
/*  623 */       float f2 = this.painter.getDescent(this);
/*  624 */       float f3 = this.painter.getAscent(this);
/*      */       float f4;
/*  626 */       if (bool1)
/*  627 */         f4 = 1.0F;
/*  628 */       else if (bool2)
/*  629 */         f4 = f1 > 0.0F ? (f1 - (f2 + f3 / 2.0F)) / f1 : 0.0F;
/*      */       else {
/*  631 */         f4 = f1 > 0.0F ? (f1 - f2) / f1 : 0.0F;
/*      */       }
/*  633 */       return f4;
/*      */     }
/*  635 */     return super.getAlignment(paramInt);
/*      */   }
/*      */ 
/*      */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*      */     throws BadLocationException
/*      */   {
/*  652 */     checkPainter();
/*  653 */     return this.painter.modelToView(this, paramInt, paramBias, paramShape);
/*      */   }
/*      */ 
/*      */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*      */   {
/*  671 */     checkPainter();
/*  672 */     return this.painter.viewToModel(this, paramFloat1, paramFloat2, paramShape, paramArrayOfBias);
/*      */   }
/*      */ 
/*      */   public int getBreakWeight(int paramInt, float paramFloat1, float paramFloat2)
/*      */   {
/*  719 */     if (paramInt == 0) {
/*  720 */       checkPainter();
/*  721 */       int i = getStartOffset();
/*  722 */       int j = this.painter.getBoundedPosition(this, i, paramFloat1, paramFloat2);
/*  723 */       return getBreakSpot(i, j) != -1 ? 2000 : j == i ? 0 : 1000;
/*      */     }
/*      */ 
/*  727 */     return super.getBreakWeight(paramInt, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public View breakView(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
/*      */   {
/*  752 */     if (paramInt1 == 0) {
/*  753 */       checkPainter();
/*  754 */       int i = this.painter.getBoundedPosition(this, paramInt2, paramFloat1, paramFloat2);
/*  755 */       int j = getBreakSpot(paramInt2, i);
/*      */ 
/*  757 */       if (j != -1) {
/*  758 */         i = j;
/*      */       }
/*      */ 
/*  762 */       if ((paramInt2 == getStartOffset()) && (i == getEndOffset())) {
/*  763 */         return this;
/*      */       }
/*  765 */       GlyphView localGlyphView = (GlyphView)createFragment(paramInt2, i);
/*  766 */       localGlyphView.x = ((int)paramFloat1);
/*  767 */       return localGlyphView;
/*      */     }
/*  769 */     return this;
/*      */   }
/*      */ 
/*      */   private int getBreakSpot(int paramInt1, int paramInt2)
/*      */   {
/*  778 */     if (this.breakSpots == null)
/*      */     {
/*  780 */       i = getStartOffset();
/*  781 */       j = getEndOffset();
/*  782 */       int[] arrayOfInt = new int[j + 1 - i];
/*  783 */       int m = 0;
/*      */ 
/*  787 */       Element localElement = getElement().getParentElement();
/*  788 */       int n = localElement == null ? i : localElement.getStartOffset();
/*  789 */       int i1 = localElement == null ? j : localElement.getEndOffset();
/*      */ 
/*  791 */       Segment localSegment = getText(n, i1);
/*  792 */       localSegment.first();
/*  793 */       BreakIterator localBreakIterator = getBreaker();
/*  794 */       localBreakIterator.setText(localSegment);
/*      */ 
/*  797 */       int i2 = j + (i1 > j ? 1 : 0);
/*      */       while (true) {
/*  799 */         i2 = localBreakIterator.preceding(localSegment.offset + (i2 - n)) + (n - localSegment.offset);
/*      */ 
/*  801 */         if (i2 <= i)
/*      */           break;
/*  803 */         arrayOfInt[(m++)] = i2;
/*      */       }
/*      */ 
/*  809 */       SegmentCache.releaseSharedSegment(localSegment);
/*  810 */       this.breakSpots = new int[m];
/*  811 */       System.arraycopy(arrayOfInt, 0, this.breakSpots, 0, m);
/*      */     }
/*      */ 
/*  814 */     int i = -1;
/*  815 */     for (int j = 0; j < this.breakSpots.length; j++) {
/*  816 */       int k = this.breakSpots[j];
/*  817 */       if (k <= paramInt2) {
/*  818 */         if (k <= paramInt1) break;
/*  819 */         i = k; break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  824 */     return i;
/*      */   }
/*      */ 
/*      */   private BreakIterator getBreaker()
/*      */   {
/*  833 */     Document localDocument = getDocument();
/*  834 */     if ((localDocument != null) && (Boolean.TRUE.equals(localDocument.getProperty(AbstractDocument.MultiByteProperty))))
/*      */     {
/*  836 */       Container localContainer = getContainer();
/*  837 */       Locale localLocale = localContainer == null ? Locale.getDefault() : localContainer.getLocale();
/*  838 */       return BreakIterator.getLineInstance(localLocale);
/*      */     }
/*  840 */     return new WhitespaceBasedBreakIterator();
/*      */   }
/*      */ 
/*      */   public View createFragment(int paramInt1, int paramInt2)
/*      */   {
/*  866 */     checkPainter();
/*  867 */     Element localElement = getElement();
/*  868 */     GlyphView localGlyphView = (GlyphView)clone();
/*  869 */     localGlyphView.offset = (paramInt1 - localElement.getStartOffset());
/*  870 */     localGlyphView.length = (paramInt2 - paramInt1);
/*  871 */     localGlyphView.painter = this.painter.getPainter(localGlyphView, paramInt1, paramInt2);
/*  872 */     localGlyphView.justificationInfo = null;
/*  873 */     return localGlyphView;
/*      */   }
/*      */ 
/*      */   public int getNextVisualPositionFrom(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*      */     throws BadLocationException
/*      */   {
/*  899 */     return this.painter.getNextVisualPositionFrom(this, paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*      */   }
/*      */ 
/*      */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  914 */     this.justificationInfo = null;
/*  915 */     this.breakSpots = null;
/*  916 */     this.minimumSpan = -1.0F;
/*  917 */     syncCR();
/*  918 */     preferenceChanged(null, true, false);
/*      */   }
/*      */ 
/*      */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  933 */     this.justificationInfo = null;
/*  934 */     this.breakSpots = null;
/*  935 */     this.minimumSpan = -1.0F;
/*  936 */     syncCR();
/*  937 */     preferenceChanged(null, true, false);
/*      */   }
/*      */ 
/*      */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  952 */     this.minimumSpan = -1.0F;
/*  953 */     syncCR();
/*  954 */     preferenceChanged(null, true, true);
/*      */   }
/*      */ 
/*      */   private void syncCR()
/*      */   {
/*  960 */     if (this.impliedCR) {
/*  961 */       Element localElement = getElement().getParentElement();
/*  962 */       this.impliedCR = ((localElement != null) && (localElement.getElementCount() > 1));
/*      */     }
/*      */   }
/*      */ 
/*      */   JustificationInfo getJustificationInfo(int paramInt)
/*      */   {
/*  998 */     if (this.justificationInfo != null) {
/*  999 */       return this.justificationInfo;
/*      */     }
/*      */ 
/* 1005 */     int i = getStartOffset();
/* 1006 */     int j = getEndOffset();
/* 1007 */     Segment localSegment = getText(i, j);
/* 1008 */     int k = localSegment.offset;
/* 1009 */     int m = localSegment.offset + localSegment.count - 1;
/* 1010 */     int n = m + 1;
/* 1011 */     int i1 = k - 1;
/* 1012 */     int i2 = k - 1;
/* 1013 */     int i3 = 0;
/* 1014 */     int i4 = 0;
/* 1015 */     int i5 = 0;
/* 1016 */     boolean bool = false;
/* 1017 */     BitSet localBitSet = new BitSet(j - i + 1);
/*      */ 
/* 1023 */     int i6 = m; for (int i7 = 0; i6 >= k; i6--) {
/* 1024 */       if (' ' == localSegment.array[i6]) {
/* 1025 */         localBitSet.set(i6 - k);
/* 1026 */         if (i7 == 0) {
/* 1027 */           i3++;
/* 1028 */         } else if (i7 == 1) {
/* 1029 */           i7 = 2;
/* 1030 */           i5 = 1;
/* 1031 */         } else if (i7 == 2) {
/* 1032 */           i5++;
/*      */         }
/*      */       } else { if ('\t' == localSegment.array[i6]) {
/* 1035 */           bool = true;
/* 1036 */           break;
/*      */         }
/* 1038 */         if (i7 == 0) {
/* 1039 */           if (('\n' != localSegment.array[i6]) && ('\r' != localSegment.array[i6]))
/*      */           {
/* 1041 */             i7 = 1;
/* 1042 */             i1 = i6;
/*      */           }
/* 1044 */         } else if (i7 != 1)
/*      */         {
/* 1046 */           if (i7 == 2) {
/* 1047 */             i4 += i5;
/* 1048 */             i5 = 0;
/*      */           }
/*      */         }
/* 1050 */         n = i6;
/*      */       }
/*      */     }
/*      */ 
/* 1054 */     SegmentCache.releaseSharedSegment(localSegment);
/*      */ 
/* 1056 */     i6 = -1;
/* 1057 */     if (n < m) {
/* 1058 */       i6 = n - k;
/*      */     }
/*      */ 
/* 1061 */     i7 = -1;
/* 1062 */     if (i1 > k) {
/* 1063 */       i7 = i1 - k;
/*      */     }
/*      */ 
/* 1066 */     this.justificationInfo = new JustificationInfo(i6, i7, i5, i4, i3, bool, localBitSet);
/*      */ 
/* 1074 */     return this.justificationInfo;
/*      */   }
/*      */ 
/*      */   public static abstract class GlyphPainter
/*      */   {
/*      */     public abstract float getSpan(GlyphView paramGlyphView, int paramInt1, int paramInt2, TabExpander paramTabExpander, float paramFloat);
/*      */ 
/*      */     public abstract float getHeight(GlyphView paramGlyphView);
/*      */ 
/*      */     public abstract float getAscent(GlyphView paramGlyphView);
/*      */ 
/*      */     public abstract float getDescent(GlyphView paramGlyphView);
/*      */ 
/*      */     public abstract void paint(GlyphView paramGlyphView, Graphics paramGraphics, Shape paramShape, int paramInt1, int paramInt2);
/*      */ 
/*      */     public abstract Shape modelToView(GlyphView paramGlyphView, int paramInt, Position.Bias paramBias, Shape paramShape)
/*      */       throws BadLocationException;
/*      */ 
/*      */     public abstract int viewToModel(GlyphView paramGlyphView, float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias);
/*      */ 
/*      */     public abstract int getBoundedPosition(GlyphView paramGlyphView, int paramInt, float paramFloat1, float paramFloat2);
/*      */ 
/*      */     public GlyphPainter getPainter(GlyphView paramGlyphView, int paramInt1, int paramInt2)
/*      */     {
/* 1221 */       return this;
/*      */     }
/*      */ 
/*      */     public int getNextVisualPositionFrom(GlyphView paramGlyphView, int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*      */       throws BadLocationException
/*      */     {
/* 1253 */       int i = paramGlyphView.getStartOffset();
/* 1254 */       int j = paramGlyphView.getEndOffset();
/*      */ 
/* 1257 */       switch (paramInt2) {
/*      */       case 1:
/*      */       case 5:
/* 1260 */         if (paramInt1 != -1)
/*      */         {
/* 1264 */           return -1;
/*      */         }
/* 1266 */         Container localContainer = paramGlyphView.getContainer();
/*      */ 
/* 1268 */         if ((localContainer instanceof JTextComponent)) {
/* 1269 */           Caret localCaret = ((JTextComponent)localContainer).getCaret();
/*      */ 
/* 1271 */           Object localObject = localCaret != null ? localCaret.getMagicCaretPosition() : null;
/*      */ 
/* 1273 */           if (localObject == null) {
/* 1274 */             paramArrayOfBias[0] = Position.Bias.Forward;
/* 1275 */             return i;
/*      */           }
/* 1277 */           int k = paramGlyphView.viewToModel(localObject.x, 0.0F, paramShape, paramArrayOfBias);
/* 1278 */           return k;
/*      */         }
/*      */         break;
/*      */       case 3:
/* 1282 */         if (i == paramGlyphView.getDocument().getLength()) {
/* 1283 */           if (paramInt1 == -1) {
/* 1284 */             paramArrayOfBias[0] = Position.Bias.Forward;
/* 1285 */             return i;
/*      */           }
/*      */ 
/* 1289 */           return -1;
/*      */         }
/* 1291 */         if (paramInt1 == -1) {
/* 1292 */           paramArrayOfBias[0] = Position.Bias.Forward;
/* 1293 */           return i;
/*      */         }
/* 1295 */         if (paramInt1 == j) {
/* 1296 */           return -1;
/*      */         }
/* 1298 */         paramInt1++; if (paramInt1 == j)
/*      */         {
/* 1301 */           return -1;
/*      */         }
/*      */ 
/* 1304 */         paramArrayOfBias[0] = Position.Bias.Forward;
/*      */ 
/* 1306 */         return paramInt1;
/*      */       case 7:
/* 1308 */         if (i == paramGlyphView.getDocument().getLength()) {
/* 1309 */           if (paramInt1 == -1) {
/* 1310 */             paramArrayOfBias[0] = Position.Bias.Forward;
/* 1311 */             return i;
/*      */           }
/*      */ 
/* 1315 */           return -1;
/*      */         }
/* 1317 */         if (paramInt1 == -1)
/*      */         {
/* 1320 */           paramArrayOfBias[0] = Position.Bias.Forward;
/* 1321 */           return j - 1;
/*      */         }
/* 1323 */         if (paramInt1 == i) {
/* 1324 */           return -1;
/*      */         }
/* 1326 */         paramArrayOfBias[0] = Position.Bias.Forward;
/* 1327 */         return paramInt1 - 1;
/*      */       case 2:
/*      */       case 4:
/*      */       case 6:
/*      */       default:
/* 1329 */         throw new IllegalArgumentException("Bad direction: " + paramInt2);
/*      */       }
/* 1331 */       return paramInt1;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class JustificationInfo
/*      */   {
/*      */     final int start;
/*      */     final int end;
/*      */     final int leadingSpaces;
/*      */     final int contentSpaces;
/*      */     final int trailingSpaces;
/*      */     final boolean hasTab;
/*      */     final BitSet spaceMap;
/*      */ 
/*      */     JustificationInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, BitSet paramBitSet)
/*      */     {
/*  985 */       this.start = paramInt1;
/*  986 */       this.end = paramInt2;
/*  987 */       this.leadingSpaces = paramInt3;
/*  988 */       this.contentSpaces = paramInt4;
/*  989 */       this.trailingSpaces = paramInt5;
/*  990 */       this.hasTab = paramBoolean;
/*  991 */       this.spaceMap = paramBitSet;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.GlyphView
 * JD-Core Version:    0.6.2
 */