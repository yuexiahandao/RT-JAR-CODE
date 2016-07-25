/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import javax.swing.SizeRequirements;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.plaf.TextUI;
/*      */ 
/*      */ public class ParagraphView extends FlowView
/*      */   implements TabExpander
/*      */ {
/*      */   private int justification;
/*      */   private float lineSpacing;
/*  804 */   protected int firstLineIndent = 0;
/*      */   private int tabBase;
/*      */   static Class i18nStrategy;
/*  824 */   static char[] tabChars = new char[1];
/*      */   static char[] tabDecimalChars;
/*      */ 
/*      */   public ParagraphView(Element paramElement)
/*      */   {
/*   58 */     super(paramElement, 1);
/*   59 */     setPropertiesFromAttributes();
/*   60 */     Document localDocument = paramElement.getDocument();
/*   61 */     Object localObject1 = localDocument.getProperty("i18n");
/*   62 */     if ((localObject1 != null) && (localObject1.equals(Boolean.TRUE)))
/*      */       try {
/*   64 */         if (i18nStrategy == null)
/*      */         {
/*   66 */           localObject2 = "javax.swing.text.TextLayoutStrategy";
/*   67 */           ClassLoader localClassLoader = getClass().getClassLoader();
/*   68 */           if (localClassLoader != null)
/*   69 */             i18nStrategy = localClassLoader.loadClass((String)localObject2);
/*      */           else {
/*   71 */             i18nStrategy = Class.forName((String)localObject2);
/*      */           }
/*      */         }
/*   74 */         Object localObject2 = i18nStrategy.newInstance();
/*   75 */         if ((localObject2 instanceof FlowView.FlowStrategy))
/*   76 */           this.strategy = ((FlowView.FlowStrategy)localObject2);
/*      */       }
/*      */       catch (Throwable localThrowable) {
/*   79 */         throw new StateInvariantError("ParagraphView: Can't create i18n strategy: " + localThrowable.getMessage());
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void setJustification(int paramInt)
/*      */   {
/*   96 */     this.justification = paramInt;
/*      */   }
/*      */ 
/*      */   protected void setLineSpacing(float paramFloat)
/*      */   {
/*  105 */     this.lineSpacing = paramFloat;
/*      */   }
/*      */ 
/*      */   protected void setFirstLineIndent(float paramFloat)
/*      */   {
/*  114 */     this.firstLineIndent = ((int)paramFloat);
/*      */   }
/*      */ 
/*      */   protected void setPropertiesFromAttributes()
/*      */   {
/*  121 */     AttributeSet localAttributeSet = getAttributes();
/*  122 */     if (localAttributeSet != null) {
/*  123 */       setParagraphInsets(localAttributeSet);
/*  124 */       Integer localInteger = (Integer)localAttributeSet.getAttribute(StyleConstants.Alignment);
/*      */       int i;
/*  126 */       if (localInteger == null) {
/*  127 */         Document localDocument = getElement().getDocument();
/*  128 */         Object localObject = localDocument.getProperty(TextAttribute.RUN_DIRECTION);
/*  129 */         if ((localObject != null) && (localObject.equals(TextAttribute.RUN_DIRECTION_RTL)))
/*  130 */           i = 2;
/*      */         else
/*  132 */           i = 0;
/*      */       }
/*      */       else {
/*  135 */         i = localInteger.intValue();
/*      */       }
/*  137 */       setJustification(i);
/*  138 */       setLineSpacing(StyleConstants.getLineSpacing(localAttributeSet));
/*  139 */       setFirstLineIndent(StyleConstants.getFirstLineIndent(localAttributeSet));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int getLayoutViewCount()
/*      */   {
/*  157 */     return this.layoutPool.getViewCount();
/*      */   }
/*      */ 
/*      */   protected View getLayoutView(int paramInt)
/*      */   {
/*  174 */     return this.layoutPool.getView(paramInt);
/*      */   }
/*      */ 
/*      */   protected int getNextNorthSouthVisualPositionFrom(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*      */     throws BadLocationException
/*      */   {
/*      */     int i;
/*  197 */     if (paramInt1 == -1) {
/*  198 */       i = paramInt2 == 1 ? getViewCount() - 1 : 0;
/*      */     }
/*      */     else
/*      */     {
/*  202 */       if ((paramBias == Position.Bias.Backward) && (paramInt1 > 0)) {
/*  203 */         i = getViewIndexAtPosition(paramInt1 - 1);
/*      */       }
/*      */       else {
/*  206 */         i = getViewIndexAtPosition(paramInt1);
/*      */       }
/*  208 */       if (paramInt2 == 1) {
/*  209 */         if (i == 0) {
/*  210 */           return -1;
/*      */         }
/*  212 */         i--;
/*      */       } else {
/*  214 */         i++; if (i >= getViewCount()) {
/*  215 */           return -1;
/*      */         }
/*      */       }
/*      */     }
/*  219 */     JTextComponent localJTextComponent = (JTextComponent)getContainer();
/*  220 */     Caret localCaret = localJTextComponent.getCaret();
/*      */ 
/*  222 */     Object localObject = localCaret != null ? localCaret.getMagicCaretPosition() : null;
/*      */     int j;
/*  224 */     if (localObject == null) {
/*      */       Rectangle localRectangle;
/*      */       try {
/*  227 */         localRectangle = localJTextComponent.getUI().modelToView(localJTextComponent, paramInt1, paramBias);
/*      */       } catch (BadLocationException localBadLocationException) {
/*  229 */         localRectangle = null;
/*      */       }
/*  231 */       if (localRectangle == null) {
/*  232 */         j = 0;
/*      */       }
/*      */       else
/*  235 */         j = localRectangle.getBounds().x;
/*      */     }
/*      */     else
/*      */     {
/*  239 */       j = localObject.x;
/*      */     }
/*  241 */     return getClosestPositionTo(paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias, i, j);
/*      */   }
/*      */ 
/*      */   protected int getClosestPositionTo(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias, int paramInt3, int paramInt4)
/*      */     throws BadLocationException
/*      */   {
/*  268 */     JTextComponent localJTextComponent = (JTextComponent)getContainer();
/*  269 */     Document localDocument = getDocument();
/*  270 */     Object localObject = (localDocument instanceof AbstractDocument) ? (AbstractDocument)localDocument : null;
/*      */ 
/*  272 */     View localView1 = getView(paramInt3);
/*  273 */     int i = -1;
/*      */ 
/*  275 */     paramArrayOfBias[0] = Position.Bias.Forward;
/*  276 */     int j = 0; for (int k = localView1.getViewCount(); j < k; j++) {
/*  277 */       View localView2 = localView1.getView(j);
/*  278 */       int m = localView2.getStartOffset();
/*  279 */       int n = localObject != null ? localObject.isLeftToRight(m, m + 1) : 1;
/*      */ 
/*  281 */       if (n != 0) {
/*  282 */         i = m;
/*  283 */         for (int i1 = localView2.getEndOffset(); i < i1; i++) {
/*  284 */           float f2 = localJTextComponent.modelToView(i).getBounds().x;
/*  285 */           if (f2 >= paramInt4) {
/*      */             do i++; while ((i < i1) && (localJTextComponent.modelToView(i).getBounds().x == f2));
/*      */ 
/*  289 */             i--; return i;
/*      */           }
/*      */         }
/*  292 */         i--;
/*      */       }
/*      */       else {
/*  295 */         for (i = localView2.getEndOffset() - 1; i >= m; 
/*  296 */           i--) {
/*  297 */           float f1 = localJTextComponent.modelToView(i).getBounds().x;
/*  298 */           if (f1 >= paramInt4) {
/*      */             do i--; while ((i >= m) && (localJTextComponent.modelToView(i).getBounds().x == f1));
/*      */ 
/*  302 */             i++; return i;
/*      */           }
/*      */         }
/*  305 */         i++;
/*      */       }
/*      */     }
/*  308 */     if (i == -1) {
/*  309 */       return getStartOffset();
/*      */     }
/*  311 */     return i;
/*      */   }
/*      */ 
/*      */   protected boolean flipEastAndWestAtEnds(int paramInt, Position.Bias paramBias)
/*      */   {
/*  340 */     Document localDocument = getDocument();
/*  341 */     if (((localDocument instanceof AbstractDocument)) && (!((AbstractDocument)localDocument).isLeftToRight(getStartOffset(), getStartOffset() + 1)))
/*      */     {
/*  344 */       return true;
/*      */     }
/*  346 */     return false;
/*      */   }
/*      */ 
/*      */   public int getFlowSpan(int paramInt)
/*      */   {
/*  360 */     View localView = getView(paramInt);
/*  361 */     int i = 0;
/*  362 */     if ((localView instanceof Row)) {
/*  363 */       Row localRow = (Row)localView;
/*  364 */       i = localRow.getLeftInset() + localRow.getRightInset();
/*      */     }
/*  366 */     return this.layoutSpan == 2147483647 ? this.layoutSpan : this.layoutSpan - i;
/*      */   }
/*      */ 
/*      */   public int getFlowStart(int paramInt)
/*      */   {
/*  379 */     View localView = getView(paramInt);
/*  380 */     int i = 0;
/*  381 */     if ((localView instanceof Row)) {
/*  382 */       Row localRow = (Row)localView;
/*  383 */       i = localRow.getLeftInset();
/*      */     }
/*  385 */     return this.tabBase + i;
/*      */   }
/*      */ 
/*      */   protected View createRow()
/*      */   {
/*  395 */     return new Row(getElement());
/*      */   }
/*      */ 
/*      */   public float nextTabStop(float paramFloat, int paramInt)
/*      */   {
/*  427 */     if (this.justification != 0)
/*  428 */       return paramFloat + 10.0F;
/*  429 */     paramFloat -= this.tabBase;
/*  430 */     TabSet localTabSet = getTabSet();
/*  431 */     if (localTabSet == null)
/*      */     {
/*  433 */       return this.tabBase + ((int)paramFloat / 72 + 1) * 72;
/*      */     }
/*  435 */     TabStop localTabStop = localTabSet.getTabAfter(paramFloat + 0.01F);
/*  436 */     if (localTabStop == null)
/*      */     {
/*  439 */       return this.tabBase + paramFloat + 5.0F;
/*      */     }
/*  441 */     int i = localTabStop.getAlignment();
/*      */     int j;
/*  443 */     switch (i) {
/*      */     case 0:
/*      */     case 3:
/*      */     default:
/*  447 */       return this.tabBase + localTabStop.getPosition();
/*      */     case 5:
/*  450 */       return this.tabBase + localTabStop.getPosition();
/*      */     case 1:
/*      */     case 2:
/*  453 */       j = findOffsetToCharactersInString(tabChars, paramInt + 1);
/*      */ 
/*  455 */       break;
/*      */     case 4:
/*  457 */       j = findOffsetToCharactersInString(tabDecimalChars, paramInt + 1);
/*      */     }
/*      */ 
/*  461 */     if (j == -1) {
/*  462 */       j = getEndOffset();
/*      */     }
/*  464 */     float f = getPartialSize(paramInt + 1, j);
/*  465 */     switch (i)
/*      */     {
/*      */     case 1:
/*      */     case 4:
/*  471 */       return this.tabBase + Math.max(paramFloat, localTabStop.getPosition() - f);
/*      */     case 2:
/*  474 */       return this.tabBase + Math.max(paramFloat, localTabStop.getPosition() - f / 2.0F);
/*      */     case 3:
/*      */     }
/*  477 */     return paramFloat;
/*      */   }
/*      */ 
/*      */   protected TabSet getTabSet()
/*      */   {
/*  486 */     return StyleConstants.getTabSet(getElement().getAttributes());
/*      */   }
/*      */ 
/*      */   protected float getPartialSize(int paramInt1, int paramInt2)
/*      */   {
/*  504 */     float f = 0.0F;
/*      */ 
/*  506 */     int j = getViewCount();
/*      */ 
/*  514 */     int i = getElement().getElementIndex(paramInt1);
/*  515 */     j = this.layoutPool.getViewCount();
/*  516 */     while ((paramInt1 < paramInt2) && (i < j)) {
/*  517 */       View localView = this.layoutPool.getView(i++);
/*  518 */       int k = localView.getEndOffset();
/*  519 */       int m = Math.min(paramInt2, k);
/*  520 */       if ((localView instanceof TabableView))
/*  521 */         f += ((TabableView)localView).getPartialSpan(paramInt1, m);
/*  522 */       else if ((paramInt1 == localView.getStartOffset()) && (m == localView.getEndOffset()))
/*      */       {
/*  524 */         f += localView.getPreferredSpan(0);
/*      */       }
/*      */       else
/*  527 */         return 0.0F;
/*  528 */       paramInt1 = k;
/*      */     }
/*  530 */     return f;
/*      */   }
/*      */ 
/*      */   protected int findOffsetToCharactersInString(char[] paramArrayOfChar, int paramInt)
/*      */   {
/*  544 */     int i = paramArrayOfChar.length;
/*  545 */     int j = getEndOffset();
/*  546 */     Segment localSegment = new Segment();
/*      */     try {
/*  548 */       getDocument().getText(paramInt, j - paramInt, localSegment);
/*      */     } catch (BadLocationException localBadLocationException) {
/*  550 */       return -1;
/*      */     }
/*  552 */     int k = localSegment.offset; int m = localSegment.offset + localSegment.count;
/*  553 */     for (; k < m; k++) {
/*  554 */       int n = localSegment.array[k];
/*  555 */       for (int i1 = 0; i1 < i; 
/*  556 */         i1++) {
/*  557 */         if (n == paramArrayOfChar[i1]) {
/*  558 */           return k - localSegment.offset + paramInt;
/*      */         }
/*      */       }
/*      */     }
/*  562 */     return -1;
/*      */   }
/*      */ 
/*      */   protected float getTabBase()
/*      */   {
/*  570 */     return this.tabBase;
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, Shape paramShape)
/*      */   {
/*  585 */     Rectangle localRectangle1 = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*  586 */     this.tabBase = (localRectangle1.x + getLeftInset());
/*  587 */     super.paint(paramGraphics, paramShape);
/*      */ 
/*  591 */     if (this.firstLineIndent < 0) {
/*  592 */       Shape localShape = getChildAllocation(0, paramShape);
/*  593 */       if ((localShape != null) && (localShape.intersects(localRectangle1))) {
/*  594 */         int i = localRectangle1.x + getLeftInset() + this.firstLineIndent;
/*  595 */         int j = localRectangle1.y + getTopInset();
/*      */ 
/*  597 */         Rectangle localRectangle2 = paramGraphics.getClipBounds();
/*  598 */         this.tempRect.x = (i + getOffset(0, 0));
/*  599 */         this.tempRect.y = (j + getOffset(1, 0));
/*  600 */         this.tempRect.width = (getSpan(0, 0) - this.firstLineIndent);
/*  601 */         this.tempRect.height = getSpan(1, 0);
/*  602 */         if (this.tempRect.intersects(localRectangle2)) {
/*  603 */           this.tempRect.x -= this.firstLineIndent;
/*  604 */           paintChild(paramGraphics, this.tempRect, 0);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public float getAlignment(int paramInt)
/*      */   {
/*  625 */     switch (paramInt) {
/*      */     case 1:
/*  627 */       float f = 0.5F;
/*  628 */       if (getViewCount() != 0) {
/*  629 */         int i = (int)getPreferredSpan(1);
/*  630 */         View localView = getView(0);
/*  631 */         int j = (int)localView.getPreferredSpan(1);
/*  632 */         f = i != 0 ? j / 2 / i : 0.0F;
/*      */       }
/*  634 */       return f;
/*      */     case 0:
/*  636 */       return 0.5F;
/*      */     }
/*  638 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*      */   }
/*      */ 
/*      */   public View breakView(int paramInt, float paramFloat, Shape paramShape)
/*      */   {
/*  661 */     if (paramInt == 1) {
/*  662 */       if (paramShape != null) {
/*  663 */         Rectangle localRectangle = paramShape.getBounds();
/*  664 */         setSize(localRectangle.width, localRectangle.height);
/*      */       }
/*      */ 
/*  669 */       return this;
/*      */     }
/*  671 */     return this;
/*      */   }
/*      */ 
/*      */   public int getBreakWeight(int paramInt, float paramFloat)
/*      */   {
/*  691 */     if (paramInt == 1)
/*      */     {
/*  697 */       return 0;
/*      */     }
/*  699 */     return 0;
/*      */   }
/*      */ 
/*      */   protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */   {
/*  724 */     paramSizeRequirements = super.calculateMinorAxisRequirements(paramInt, paramSizeRequirements);
/*      */ 
/*  726 */     float f1 = 0.0F;
/*  727 */     float f2 = 0.0F;
/*  728 */     int i = getLayoutViewCount();
/*  729 */     for (int j = 0; j < i; j++) {
/*  730 */       View localView = getLayoutView(j);
/*  731 */       float f3 = localView.getMinimumSpan(paramInt);
/*  732 */       if (localView.getBreakWeight(paramInt, 0.0F, localView.getMaximumSpan(paramInt)) > 0)
/*      */       {
/*  734 */         int k = localView.getStartOffset();
/*  735 */         int m = localView.getEndOffset();
/*  736 */         float f4 = findEdgeSpan(localView, paramInt, k, k, m);
/*  737 */         float f5 = findEdgeSpan(localView, paramInt, m, k, m);
/*  738 */         f2 += f4;
/*  739 */         f1 = Math.max(f1, Math.max(f3, f2));
/*  740 */         f2 = f5;
/*      */       }
/*      */       else {
/*  743 */         f2 += f3;
/*  744 */         f1 = Math.max(f1, f2);
/*      */       }
/*      */     }
/*  747 */     paramSizeRequirements.minimum = Math.max(paramSizeRequirements.minimum, (int)f1);
/*  748 */     paramSizeRequirements.preferred = Math.max(paramSizeRequirements.minimum, paramSizeRequirements.preferred);
/*  749 */     paramSizeRequirements.maximum = Math.max(paramSizeRequirements.preferred, paramSizeRequirements.maximum);
/*      */ 
/*  751 */     return paramSizeRequirements;
/*      */   }
/*      */ 
/*      */   private float findEdgeSpan(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  758 */     int i = paramInt4 - paramInt3;
/*  759 */     if (i <= 1)
/*      */     {
/*  761 */       return paramView.getMinimumSpan(paramInt1);
/*      */     }
/*  763 */     int j = paramInt3 + i / 2;
/*  764 */     int k = j > paramInt2 ? 1 : 0;
/*      */ 
/*  766 */     View localView = k != 0 ? paramView.createFragment(paramInt2, j) : paramView.createFragment(j, paramInt2);
/*      */ 
/*  768 */     int m = localView.getBreakWeight(paramInt1, 0.0F, localView.getMaximumSpan(paramInt1)) > 0 ? 1 : 0;
/*      */ 
/*  770 */     if (m == k)
/*  771 */       paramInt4 = j;
/*      */     else {
/*  773 */       paramInt3 = j;
/*      */     }
/*  775 */     return findEdgeSpan(localView, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  792 */     setPropertiesFromAttributes();
/*  793 */     layoutChanged(0);
/*  794 */     layoutChanged(1);
/*  795 */     super.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  825 */     tabChars[0] = '\t';
/*  826 */     tabDecimalChars = new char[2];
/*  827 */     tabDecimalChars[0] = '\t';
/*  828 */     tabDecimalChars[1] = '.';
/*      */   }
/*      */ 
/*      */   class Row extends BoxView
/*      */   {
/*      */     static final int SPACE_ADDON = 0;
/*      */     static final int SPACE_ADDON_LEFTOVER_END = 1;
/*      */     static final int START_JUSTIFIABLE = 2;
/*      */     static final int END_JUSTIFIABLE = 3;
/* 1198 */     int[] justificationData = null;
/*      */ 
/*      */     Row(Element arg2)
/*      */     {
/*  839 */       super(0);
/*      */     }
/*      */ 
/*      */     protected void loadChildren(ViewFactory paramViewFactory)
/*      */     {
/*      */     }
/*      */ 
/*      */     public AttributeSet getAttributes()
/*      */     {
/*  856 */       View localView = getParent();
/*  857 */       return localView != null ? localView.getAttributes() : null;
/*      */     }
/*      */ 
/*      */     public float getAlignment(int paramInt) {
/*  861 */       if (paramInt == 0) {
/*  862 */         switch (ParagraphView.this.justification) {
/*      */         case 0:
/*  864 */           return 0.0F;
/*      */         case 2:
/*  866 */           return 1.0F;
/*      */         case 1:
/*  868 */           return 0.5F;
/*      */         case 3:
/*  870 */           float f = 0.5F;
/*      */ 
/*  873 */           if (isJustifiableDocument()) {
/*  874 */             f = 0.0F;
/*      */           }
/*  876 */           return f;
/*      */         }
/*      */       }
/*  879 */       return super.getAlignment(paramInt);
/*      */     }
/*      */ 
/*      */     public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*      */       throws BadLocationException
/*      */     {
/*  898 */       Rectangle localRectangle = paramShape.getBounds();
/*  899 */       View localView = getViewAtPosition(paramInt, localRectangle);
/*  900 */       if ((localView != null) && (!localView.getElement().isLeaf()))
/*      */       {
/*  902 */         return super.modelToView(paramInt, paramShape, paramBias);
/*      */       }
/*  904 */       localRectangle = paramShape.getBounds();
/*  905 */       int i = localRectangle.height;
/*  906 */       int j = localRectangle.y;
/*  907 */       Shape localShape = super.modelToView(paramInt, paramShape, paramBias);
/*  908 */       localRectangle = localShape.getBounds();
/*  909 */       localRectangle.height = i;
/*  910 */       localRectangle.y = j;
/*  911 */       return localRectangle;
/*      */     }
/*      */ 
/*      */     public int getStartOffset()
/*      */     {
/*  920 */       int i = 2147483647;
/*  921 */       int j = getViewCount();
/*  922 */       for (int k = 0; k < j; k++) {
/*  923 */         View localView = getView(k);
/*  924 */         i = Math.min(i, localView.getStartOffset());
/*      */       }
/*  926 */       return i;
/*      */     }
/*      */ 
/*      */     public int getEndOffset() {
/*  930 */       int i = 0;
/*  931 */       int j = getViewCount();
/*  932 */       for (int k = 0; k < j; k++) {
/*  933 */         View localView = getView(k);
/*  934 */         i = Math.max(i, localView.getEndOffset());
/*      */       }
/*  936 */       return i;
/*      */     }
/*      */ 
/*      */     protected void layoutMinorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */     {
/*  960 */       baselineLayout(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/*      */     }
/*      */ 
/*      */     protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */     {
/*  965 */       return baselineRequirements(paramInt, paramSizeRequirements);
/*      */     }
/*      */ 
/*      */     private boolean isLastRow()
/*      */     {
/*      */       View localView;
/*  971 */       return ((localView = getParent()) == null) || (this == localView.getView(localView.getViewCount() - 1));
/*      */     }
/*      */ 
/*      */     private boolean isBrokenRow()
/*      */     {
/*  976 */       boolean bool = false;
/*  977 */       int i = getViewCount();
/*  978 */       if (i > 0) {
/*  979 */         View localView = getView(i - 1);
/*  980 */         if (localView.getBreakWeight(0, 0.0F, 0.0F) >= 3000)
/*      */         {
/*  982 */           bool = true;
/*      */         }
/*      */       }
/*  985 */       return bool;
/*      */     }
/*      */ 
/*      */     private boolean isJustifiableDocument() {
/*  989 */       return !Boolean.TRUE.equals(getDocument().getProperty("i18n"));
/*      */     }
/*      */ 
/*      */     private boolean isJustifyEnabled()
/*      */     {
/* 1001 */       boolean bool = ParagraphView.this.justification == 3;
/*      */ 
/* 1004 */       bool = (bool) && (isJustifiableDocument());
/*      */ 
/* 1007 */       bool = (bool) && (!isLastRow());
/*      */ 
/* 1010 */       bool = (bool) && (!isBrokenRow());
/*      */ 
/* 1012 */       return bool;
/*      */     }
/*      */ 
/*      */     protected SizeRequirements calculateMajorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */     {
/* 1021 */       int[] arrayOfInt = this.justificationData;
/* 1022 */       this.justificationData = null;
/* 1023 */       SizeRequirements localSizeRequirements = super.calculateMajorAxisRequirements(paramInt, paramSizeRequirements);
/* 1024 */       if (isJustifyEnabled()) {
/* 1025 */         this.justificationData = arrayOfInt;
/*      */       }
/* 1027 */       return localSizeRequirements;
/*      */     }
/*      */ 
/*      */     protected void layoutMajorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */     {
/* 1033 */       int[] arrayOfInt1 = this.justificationData;
/* 1034 */       this.justificationData = null;
/* 1035 */       super.layoutMajorAxis(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/* 1036 */       if (!isJustifyEnabled()) {
/* 1037 */         return;
/*      */       }
/*      */ 
/* 1040 */       int i = 0;
/* 1041 */       for (n : paramArrayOfInt2) {
/* 1042 */         i += n;
/*      */       }
/* 1044 */       if (i == paramInt1)
/*      */       {
/* 1046 */         return;
/*      */       }
/*      */ 
/* 1057 */       int j = 0;
/* 1058 */       ??? = -1;
/* 1059 */       ??? = -1;
/* 1060 */       int n = 0;
/*      */ 
/* 1062 */       int i1 = getStartOffset();
/* 1063 */       int i2 = getEndOffset();
/* 1064 */       int[] arrayOfInt3 = new int[i2 - i1];
/* 1065 */       Arrays.fill(arrayOfInt3, 0);
/* 1066 */       for (int i3 = getViewCount() - 1; i3 >= 0; i3--) {
/* 1067 */         View localView = getView(i3);
/* 1068 */         if ((localView instanceof GlyphView)) {
/* 1069 */           GlyphView.JustificationInfo localJustificationInfo = ((GlyphView)localView).getJustificationInfo(i1);
/*      */ 
/* 1071 */           i6 = localView.getStartOffset();
/* 1072 */           i7 = i6 - i1;
/* 1073 */           for (int i8 = 0; i8 < localJustificationInfo.spaceMap.length(); i8++) {
/* 1074 */             if (localJustificationInfo.spaceMap.get(i8)) {
/* 1075 */               arrayOfInt3[(i8 + i7)] = 1;
/*      */             }
/*      */           }
/* 1078 */           if (??? > 0) {
/* 1079 */             if (localJustificationInfo.end >= 0)
/* 1080 */               j += localJustificationInfo.trailingSpaces;
/*      */             else {
/* 1082 */               n += localJustificationInfo.trailingSpaces;
/*      */             }
/*      */           }
/* 1085 */           if (localJustificationInfo.start >= 0) {
/* 1086 */             ??? = localJustificationInfo.start + i6;
/*      */ 
/* 1088 */             j += n;
/*      */           }
/* 1090 */           if ((localJustificationInfo.end >= 0) && (??? < 0))
/*      */           {
/* 1092 */             ??? = localJustificationInfo.end + i6;
/*      */           }
/*      */ 
/* 1095 */           j += localJustificationInfo.contentSpaces;
/* 1096 */           n = localJustificationInfo.leadingSpaces;
/* 1097 */           if (localJustificationInfo.hasTab) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1102 */       if (j <= 0)
/*      */       {
/* 1104 */         return;
/*      */       }
/* 1106 */       i3 = paramInt1 - i;
/* 1107 */       int i4 = j > 0 ? i3 / j : 0;
/*      */ 
/* 1110 */       int i5 = -1;
/* 1111 */       int i6 = ??? - i1;
/* 1112 */       int i7 = i3 - i4 * j;
/* 1113 */       while (i7 > 0)
/*      */       {
/* 1116 */         i5 = i6;
/*      */ 
/* 1114 */         i7 -= arrayOfInt3[i6];
/* 1115 */         i6++;
/*      */       }
/*      */ 
/* 1118 */       if ((i4 > 0) || (i5 >= 0)) {
/* 1119 */         this.justificationData = (arrayOfInt1 != null ? arrayOfInt1 : new int[4]);
/*      */ 
/* 1122 */         this.justificationData[0] = i4;
/* 1123 */         this.justificationData[1] = i5;
/*      */ 
/* 1125 */         this.justificationData[2] = (??? - i1);
/*      */ 
/* 1127 */         this.justificationData[3] = (??? - i1);
/*      */ 
/* 1129 */         super.layoutMajorAxis(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/*      */       }
/*      */     }
/*      */ 
/*      */     public float getMaximumSpan(int paramInt)
/*      */     {
/*      */       float f;
/* 1138 */       if ((0 == paramInt) && (isJustifyEnabled()))
/*      */       {
/* 1140 */         f = 3.4028235E+38F;
/*      */       }
/* 1142 */       else f = super.getMaximumSpan(paramInt);
/*      */ 
/* 1144 */       return f;
/*      */     }
/*      */ 
/*      */     protected int getViewIndexAtPosition(int paramInt)
/*      */     {
/* 1158 */       if ((paramInt < getStartOffset()) || (paramInt >= getEndOffset()))
/* 1159 */         return -1;
/* 1160 */       for (int i = getViewCount() - 1; i >= 0; i--) {
/* 1161 */         View localView = getView(i);
/* 1162 */         if ((paramInt >= localView.getStartOffset()) && (paramInt < localView.getEndOffset()))
/*      */         {
/* 1164 */           return i;
/*      */         }
/*      */       }
/* 1167 */       return -1;
/*      */     }
/*      */ 
/*      */     protected short getLeftInset()
/*      */     {
/* 1177 */       int i = 0;
/*      */       View localView;
/* 1178 */       if (((localView = getParent()) != null) && 
/* 1179 */         (this == localView.getView(0))) {
/* 1180 */         i = ParagraphView.this.firstLineIndent;
/*      */       }
/*      */ 
/* 1183 */       return (short)(super.getLeftInset() + i);
/*      */     }
/*      */ 
/*      */     protected short getBottomInset() {
/* 1187 */       return (short)(int)(super.getBottomInset() + (this.minorRequest != null ? this.minorRequest.preferred : 0) * ParagraphView.this.lineSpacing);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.ParagraphView
 * JD-Core Version:    0.6.2
 */