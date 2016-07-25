/*      */ package javax.swing.text.html;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Vector;
/*      */ import javax.swing.SizeRequirements;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.ElementChange;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BoxView;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ 
/*      */ class TableView extends BoxView
/*      */   implements ViewFactory
/*      */ {
/*      */   private AttributeSet attr;
/*      */   private StyleSheet.BoxPainter painter;
/*      */   private int cellSpacing;
/*      */   private int borderWidth;
/*      */   private int captionIndex;
/*      */   private boolean relativeCells;
/*      */   private boolean multiRowCells;
/*      */   int[] columnSpans;
/*      */   int[] columnOffsets;
/*      */   SizeRequirements totalColumnRequirements;
/*      */   SizeRequirements[] columnRequirements;
/*  990 */   RowIterator rowIterator = new RowIterator();
/*  991 */   ColumnIterator colIterator = new ColumnIterator();
/*      */   Vector<RowView> rows;
/*  996 */   boolean skipComments = false;
/*      */   boolean gridValid;
/*  999 */   private static final BitSet EMPTY = new BitSet();
/*      */ 
/*      */   public TableView(Element paramElement)
/*      */   {
/*   50 */     super(paramElement, 1);
/*   51 */     this.rows = new Vector();
/*   52 */     this.gridValid = false;
/*   53 */     this.captionIndex = -1;
/*   54 */     this.totalColumnRequirements = new SizeRequirements();
/*      */   }
/*      */ 
/*      */   protected RowView createTableRow(Element paramElement)
/*      */   {
/*   67 */     Object localObject = paramElement.getAttributes().getAttribute(StyleConstants.NameAttribute);
/*   68 */     if (localObject == HTML.Tag.TR) {
/*   69 */       return new RowView(paramElement);
/*      */     }
/*   71 */     return null;
/*      */   }
/*      */ 
/*      */   public int getColumnCount()
/*      */   {
/*   78 */     return this.columnSpans.length;
/*      */   }
/*      */ 
/*      */   public int getColumnSpan(int paramInt)
/*      */   {
/*   87 */     if (paramInt < this.columnSpans.length) {
/*   88 */       return this.columnSpans[paramInt];
/*      */     }
/*   90 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getRowCount()
/*      */   {
/*   97 */     return this.rows.size();
/*      */   }
/*      */ 
/*      */   public int getMultiRowSpan(int paramInt1, int paramInt2)
/*      */   {
/*  105 */     RowView localRowView1 = getRow(paramInt1);
/*  106 */     RowView localRowView2 = getRow(paramInt2);
/*  107 */     if ((localRowView1 != null) && (localRowView2 != null)) {
/*  108 */       int i = localRowView1.viewIndex;
/*  109 */       int j = localRowView2.viewIndex;
/*  110 */       int k = getOffset(1, j) - getOffset(1, i) + getSpan(1, j);
/*      */ 
/*  112 */       return k;
/*      */     }
/*  114 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getRowSpan(int paramInt)
/*      */   {
/*  121 */     RowView localRowView = getRow(paramInt);
/*  122 */     if (localRowView != null) {
/*  123 */       return getSpan(1, localRowView.viewIndex);
/*      */     }
/*  125 */     return 0;
/*      */   }
/*      */ 
/*      */   RowView getRow(int paramInt) {
/*  129 */     if (paramInt < this.rows.size()) {
/*  130 */       return (RowView)this.rows.elementAt(paramInt);
/*      */     }
/*  132 */     return null;
/*      */   }
/*      */ 
/*      */   protected View getViewAtPoint(int paramInt1, int paramInt2, Rectangle paramRectangle) {
/*  136 */     int i = getViewCount();
/*      */ 
/*  138 */     Rectangle localRectangle = new Rectangle();
/*  139 */     for (int j = 0; j < i; j++) {
/*  140 */       localRectangle.setBounds(paramRectangle);
/*  141 */       childAllocation(j, localRectangle);
/*  142 */       View localView = getView(j);
/*  143 */       if ((localView instanceof RowView)) {
/*  144 */         localView = ((RowView)localView).findViewAtPoint(paramInt1, paramInt2, localRectangle);
/*  145 */         if (localView != null) {
/*  146 */           paramRectangle.setBounds(localRectangle);
/*  147 */           return localView;
/*      */         }
/*      */       }
/*      */     }
/*  151 */     return super.getViewAtPoint(paramInt1, paramInt2, paramRectangle);
/*      */   }
/*      */ 
/*      */   protected int getColumnsOccupied(View paramView)
/*      */   {
/*  159 */     AttributeSet localAttributeSet = paramView.getElement().getAttributes();
/*      */ 
/*  161 */     if (localAttributeSet.isDefined(HTML.Attribute.COLSPAN)) {
/*  162 */       String str = (String)localAttributeSet.getAttribute(HTML.Attribute.COLSPAN);
/*  163 */       if (str != null) {
/*      */         try {
/*  165 */           return Integer.parseInt(str);
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*  172 */     return 1;
/*      */   }
/*      */ 
/*      */   protected int getRowsOccupied(View paramView)
/*      */   {
/*  180 */     AttributeSet localAttributeSet = paramView.getElement().getAttributes();
/*      */ 
/*  182 */     if (localAttributeSet.isDefined(HTML.Attribute.ROWSPAN)) {
/*  183 */       String str = (String)localAttributeSet.getAttribute(HTML.Attribute.ROWSPAN);
/*  184 */       if (str != null) {
/*      */         try {
/*  186 */           return Integer.parseInt(str);
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*  193 */     return 1;
/*      */   }
/*      */ 
/*      */   protected void invalidateGrid() {
/*  197 */     this.gridValid = false;
/*      */   }
/*      */ 
/*      */   protected StyleSheet getStyleSheet() {
/*  201 */     HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/*  202 */     return localHTMLDocument.getStyleSheet();
/*      */   }
/*      */ 
/*      */   void updateInsets()
/*      */   {
/*  210 */     short s1 = (short)(int)this.painter.getInset(1, this);
/*  211 */     short s2 = (short)(int)this.painter.getInset(3, this);
/*  212 */     if (this.captionIndex != -1) {
/*  213 */       View localView = getView(this.captionIndex);
/*  214 */       short s3 = (short)(int)localView.getPreferredSpan(1);
/*  215 */       AttributeSet localAttributeSet = localView.getAttributes();
/*  216 */       Object localObject = localAttributeSet.getAttribute(CSS.Attribute.CAPTION_SIDE);
/*  217 */       if ((localObject != null) && (localObject.equals("bottom")))
/*  218 */         s2 = (short)(s2 + s3);
/*      */       else {
/*  220 */         s1 = (short)(s1 + s3);
/*      */       }
/*      */     }
/*  223 */     setInsets(s1, (short)(int)this.painter.getInset(2, this), s2, (short)(int)this.painter.getInset(4, this));
/*      */   }
/*      */ 
/*      */   protected void setPropertiesFromAttributes()
/*      */   {
/*  231 */     StyleSheet localStyleSheet = getStyleSheet();
/*  232 */     this.attr = localStyleSheet.getViewAttributes(this);
/*  233 */     this.painter = localStyleSheet.getBoxPainter(this.attr);
/*  234 */     if (this.attr != null) {
/*  235 */       setInsets((short)(int)this.painter.getInset(1, this), (short)(int)this.painter.getInset(2, this), (short)(int)this.painter.getInset(3, this), (short)(int)this.painter.getInset(4, this));
/*      */ 
/*  240 */       CSS.LengthValue localLengthValue = (CSS.LengthValue)this.attr.getAttribute(CSS.Attribute.BORDER_SPACING);
/*      */ 
/*  242 */       if (localLengthValue != null) {
/*  243 */         this.cellSpacing = ((int)localLengthValue.getValue());
/*      */       }
/*      */       else {
/*  246 */         this.cellSpacing = 2;
/*      */       }
/*  248 */       localLengthValue = (CSS.LengthValue)this.attr.getAttribute(CSS.Attribute.BORDER_TOP_WIDTH);
/*      */ 
/*  250 */       if (localLengthValue != null)
/*  251 */         this.borderWidth = ((int)localLengthValue.getValue());
/*      */       else
/*  253 */         this.borderWidth = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   void updateGrid()
/*      */   {
/*  264 */     if (!this.gridValid) {
/*  265 */       this.relativeCells = false;
/*  266 */       this.multiRowCells = false;
/*      */ 
/*  270 */       this.captionIndex = -1;
/*  271 */       this.rows.removeAllElements();
/*  272 */       int i = getViewCount();
/*      */       Object localObject2;
/*  273 */       for (int j = 0; j < i; j++) {
/*  274 */         View localView1 = getView(j);
/*      */         Object localObject1;
/*  275 */         if ((localView1 instanceof RowView)) {
/*  276 */           this.rows.addElement((RowView)localView1);
/*  277 */           localObject1 = (RowView)localView1;
/*  278 */           ((RowView)localObject1).clearFilledColumns();
/*  279 */           ((RowView)localObject1).rowIndex = (this.rows.size() - 1);
/*  280 */           ((RowView)localObject1).viewIndex = j;
/*      */         } else {
/*  282 */           localObject1 = localView1.getElement().getAttributes().getAttribute(StyleConstants.NameAttribute);
/*  283 */           if ((localObject1 instanceof HTML.Tag)) {
/*  284 */             localObject2 = (HTML.Tag)localObject1;
/*  285 */             if (localObject2 == HTML.Tag.CAPTION) {
/*  286 */               this.captionIndex = j;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  292 */       j = 0;
/*  293 */       int k = this.rows.size();
/*  294 */       for (int m = 0; m < k; m++) {
/*  295 */         localObject2 = getRow(m);
/*  296 */         int n = 0;
/*  297 */         for (int i1 = 0; i1 < ((RowView)localObject2).getViewCount(); n++) {
/*  298 */           View localView2 = ((RowView)localObject2).getView(i1);
/*  299 */           if (!this.relativeCells) {
/*  300 */             AttributeSet localAttributeSet = localView2.getAttributes();
/*  301 */             CSS.LengthValue localLengthValue = (CSS.LengthValue)localAttributeSet.getAttribute(CSS.Attribute.WIDTH);
/*      */ 
/*  303 */             if ((localLengthValue != null) && (localLengthValue.isPercentage())) {
/*  304 */               this.relativeCells = true;
/*      */             }
/*      */           }
/*      */ 
/*  308 */           while (((RowView)localObject2).isFilled(n)) n++;
/*  309 */           int i2 = getRowsOccupied(localView2);
/*  310 */           if (i2 > 1) {
/*  311 */             this.multiRowCells = true;
/*      */           }
/*  313 */           int i3 = getColumnsOccupied(localView2);
/*  314 */           if ((i3 > 1) || (i2 > 1))
/*      */           {
/*  316 */             int i4 = m + i2;
/*  317 */             int i5 = n + i3;
/*  318 */             for (int i6 = m; i6 < i4; i6++) {
/*  319 */               for (int i7 = n; i7 < i5; i7++) {
/*  320 */                 if ((i6 != m) || (i7 != n)) {
/*  321 */                   addFill(i6, i7);
/*      */                 }
/*      */               }
/*      */             }
/*  325 */             if (i3 > 1)
/*  326 */               n += i3 - 1;
/*      */           }
/*  297 */           i1++;
/*      */         }
/*      */ 
/*  330 */         j = Math.max(j, n);
/*      */       }
/*      */ 
/*  334 */       this.columnSpans = new int[j];
/*  335 */       this.columnOffsets = new int[j];
/*  336 */       this.columnRequirements = new SizeRequirements[j];
/*  337 */       for (m = 0; m < j; m++) {
/*  338 */         this.columnRequirements[m] = new SizeRequirements();
/*  339 */         this.columnRequirements[m].maximum = 2147483647;
/*      */       }
/*  341 */       this.gridValid = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   void addFill(int paramInt1, int paramInt2)
/*      */   {
/*  349 */     RowView localRowView = getRow(paramInt1);
/*  350 */     if (localRowView != null)
/*  351 */       localRowView.fillColumn(paramInt2);
/*      */   }
/*      */ 
/*      */   protected void layoutColumns(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, SizeRequirements[] paramArrayOfSizeRequirements)
/*      */   {
/*  373 */     Arrays.fill(paramArrayOfInt1, 0);
/*  374 */     Arrays.fill(paramArrayOfInt2, 0);
/*  375 */     this.colIterator.setLayoutArrays(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*  376 */     CSS.calculateTiledLayout(this.colIterator, paramInt);
/*      */   }
/*      */ 
/*      */   void calculateColumnRequirements(int paramInt)
/*      */   {
/*  395 */     for (Object localObject2 : this.columnRequirements) {
/*  396 */       localObject2.minimum = 0;
/*  397 */       localObject2.preferred = 0;
/*  398 */       localObject2.maximum = 2147483647;
/*      */     }
/*  400 */     ??? = getContainer();
/*  401 */     if (??? != null) {
/*  402 */       if ((??? instanceof JTextComponent))
/*  403 */         this.skipComments = (!((JTextComponent)???).isEditable());
/*      */       else {
/*  405 */         this.skipComments = true;
/*      */       }
/*      */     }
/*      */ 
/*  409 */     ??? = 0;
/*  410 */     ??? = getRowCount();
/*      */     RowView localRowView;
/*      */     int m;
/*      */     int n;
/*      */     int i1;
/*      */     View localView;
/*      */     int i2;
/*  411 */     for (int k = 0; k < ???; k++) {
/*  412 */       localRowView = getRow(k);
/*  413 */       m = 0;
/*  414 */       n = localRowView.getViewCount();
/*  415 */       for (i1 = 0; i1 < n; i1++) {
/*  416 */         localView = localRowView.getView(i1);
/*  417 */         if ((!this.skipComments) || ((localView instanceof CellView)))
/*      */         {
/*  420 */           while (localRowView.isFilled(m)) m++;
/*  421 */           i2 = getRowsOccupied(localView);
/*  422 */           int i3 = getColumnsOccupied(localView);
/*  423 */           if (i3 == 1) {
/*  424 */             checkSingleColumnCell(paramInt, m, localView);
/*      */           } else {
/*  426 */             ??? = 1;
/*  427 */             m += i3 - 1;
/*      */           }
/*  429 */           m++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  434 */     if (??? != 0)
/*  435 */       for (k = 0; k < ???; k++) {
/*  436 */         localRowView = getRow(k);
/*  437 */         m = 0;
/*  438 */         n = localRowView.getViewCount();
/*  439 */         for (i1 = 0; i1 < n; i1++) {
/*  440 */           localView = localRowView.getView(i1);
/*  441 */           if ((!this.skipComments) || ((localView instanceof CellView)))
/*      */           {
/*  444 */             while (localRowView.isFilled(m)) m++;
/*  445 */             i2 = getColumnsOccupied(localView);
/*  446 */             if (i2 > 1) {
/*  447 */               checkMultiColumnCell(paramInt, m, i2, localView);
/*  448 */               m += i2 - 1;
/*      */             }
/*  450 */             m++;
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   void checkSingleColumnCell(int paramInt1, int paramInt2, View paramView)
/*      */   {
/*  460 */     SizeRequirements localSizeRequirements = this.columnRequirements[paramInt2];
/*  461 */     localSizeRequirements.minimum = Math.max((int)paramView.getMinimumSpan(paramInt1), localSizeRequirements.minimum);
/*  462 */     localSizeRequirements.preferred = Math.max((int)paramView.getPreferredSpan(paramInt1), localSizeRequirements.preferred);
/*      */   }
/*      */ 
/*      */   void checkMultiColumnCell(int paramInt1, int paramInt2, int paramInt3, View paramView)
/*      */   {
/*  471 */     long l1 = 0L;
/*  472 */     long l2 = 0L;
/*  473 */     long l3 = 0L;
/*      */     Object localObject1;
/*  474 */     for (int i = 0; i < paramInt3; i++) {
/*  475 */       localObject1 = this.columnRequirements[(paramInt2 + i)];
/*  476 */       l1 += ((SizeRequirements)localObject1).minimum;
/*  477 */       l2 += ((SizeRequirements)localObject1).preferred;
/*  478 */       l3 += ((SizeRequirements)localObject1).maximum;
/*      */     }
/*      */ 
/*  482 */     i = (int)paramView.getMinimumSpan(paramInt1);
/*      */     Object localObject2;
/*  483 */     if (i > l1)
/*      */     {
/*  488 */       localObject1 = new SizeRequirements[paramInt3];
/*  489 */       for (int k = 0; k < paramInt3; k++) {
/*  490 */         localObject1[k] = this.columnRequirements[(paramInt2 + k)];
/*      */       }
/*  492 */       localObject2 = new int[paramInt3];
/*  493 */       int[] arrayOfInt1 = new int[paramInt3];
/*  494 */       SizeRequirements.calculateTiledPositions(i, null, (SizeRequirements[])localObject1, arrayOfInt1, (int[])localObject2);
/*      */ 
/*  497 */       for (int n = 0; n < paramInt3; n++) {
/*  498 */         Object localObject3 = localObject1[n];
/*  499 */         localObject3.minimum = Math.max(localObject2[n], localObject3.minimum);
/*  500 */         localObject3.preferred = Math.max(localObject3.minimum, localObject3.preferred);
/*  501 */         localObject3.maximum = Math.max(localObject3.preferred, localObject3.maximum);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  506 */     int j = (int)paramView.getPreferredSpan(paramInt1);
/*  507 */     if (j > l2)
/*      */     {
/*  512 */       localObject2 = new SizeRequirements[paramInt3];
/*  513 */       for (int m = 0; m < paramInt3; m++) {
/*  514 */         localObject2[m] = this.columnRequirements[(paramInt2 + m)];
/*      */       }
/*  516 */       int[] arrayOfInt2 = new int[paramInt3];
/*  517 */       int[] arrayOfInt3 = new int[paramInt3];
/*  518 */       SizeRequirements.calculateTiledPositions(j, null, (SizeRequirements[])localObject2, arrayOfInt3, arrayOfInt2);
/*      */ 
/*  521 */       for (int i1 = 0; i1 < paramInt3; i1++) {
/*  522 */         Object localObject4 = localObject2[i1];
/*  523 */         localObject4.preferred = Math.max(arrayOfInt2[i1], localObject4.preferred);
/*  524 */         localObject4.maximum = Math.max(localObject4.preferred, localObject4.maximum);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */   {
/*  543 */     updateGrid();
/*      */ 
/*  546 */     calculateColumnRequirements(paramInt);
/*      */ 
/*  550 */     if (paramSizeRequirements == null) {
/*  551 */       paramSizeRequirements = new SizeRequirements();
/*      */     }
/*  553 */     long l1 = 0L;
/*  554 */     long l2 = 0L;
/*  555 */     int i = this.columnRequirements.length;
/*  556 */     for (int j = 0; j < i; j++) {
/*  557 */       localObject1 = this.columnRequirements[j];
/*  558 */       l1 += ((SizeRequirements)localObject1).minimum;
/*  559 */       l2 += ((SizeRequirements)localObject1).preferred;
/*      */     }
/*  561 */     j = (i + 1) * this.cellSpacing + 2 * this.borderWidth;
/*  562 */     l1 += j;
/*  563 */     l2 += j;
/*  564 */     paramSizeRequirements.minimum = ((int)l1);
/*  565 */     paramSizeRequirements.preferred = ((int)l2);
/*  566 */     paramSizeRequirements.maximum = ((int)l2);
/*      */ 
/*  569 */     Object localObject1 = getAttributes();
/*  570 */     CSS.LengthValue localLengthValue = (CSS.LengthValue)((AttributeSet)localObject1).getAttribute(CSS.Attribute.WIDTH);
/*      */ 
/*  573 */     if ((BlockView.spanSetFromAttributes(paramInt, paramSizeRequirements, localLengthValue, null)) && 
/*  574 */       (paramSizeRequirements.minimum < (int)l1))
/*      */     {
/*  577 */       paramSizeRequirements.maximum = (paramSizeRequirements.minimum = paramSizeRequirements.preferred = (int)l1);
/*      */     }
/*      */ 
/*  580 */     this.totalColumnRequirements.minimum = paramSizeRequirements.minimum;
/*  581 */     this.totalColumnRequirements.preferred = paramSizeRequirements.preferred;
/*  582 */     this.totalColumnRequirements.maximum = paramSizeRequirements.maximum;
/*      */ 
/*  585 */     Object localObject2 = ((AttributeSet)localObject1).getAttribute(CSS.Attribute.TEXT_ALIGN);
/*  586 */     if (localObject2 != null)
/*      */     {
/*  588 */       String str = localObject2.toString();
/*  589 */       if (str.equals("left"))
/*  590 */         paramSizeRequirements.alignment = 0.0F;
/*  591 */       else if (str.equals("center"))
/*  592 */         paramSizeRequirements.alignment = 0.5F;
/*  593 */       else if (str.equals("right"))
/*  594 */         paramSizeRequirements.alignment = 1.0F;
/*      */       else
/*  596 */         paramSizeRequirements.alignment = 0.0F;
/*      */     }
/*      */     else {
/*  599 */       paramSizeRequirements.alignment = 0.0F;
/*      */     }
/*      */ 
/*  602 */     return paramSizeRequirements;
/*      */   }
/*      */ 
/*      */   protected SizeRequirements calculateMajorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */   {
/*  614 */     updateInsets();
/*  615 */     this.rowIterator.updateAdjustments();
/*  616 */     paramSizeRequirements = CSS.calculateTiledRequirements(this.rowIterator, paramSizeRequirements);
/*  617 */     paramSizeRequirements.maximum = paramSizeRequirements.preferred;
/*  618 */     return paramSizeRequirements;
/*      */   }
/*      */ 
/*      */   protected void layoutMinorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  647 */     updateGrid();
/*      */ 
/*  650 */     int i = getRowCount();
/*  651 */     for (int j = 0; j < i; j++) {
/*  652 */       RowView localRowView = getRow(j);
/*  653 */       localRowView.layoutChanged(paramInt2);
/*      */     }
/*      */ 
/*  657 */     layoutColumns(paramInt1, this.columnOffsets, this.columnSpans, this.columnRequirements);
/*      */ 
/*  660 */     super.layoutMinorAxis(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/*      */   }
/*      */ 
/*      */   protected void layoutMajorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  689 */     this.rowIterator.setLayoutArrays(paramArrayOfInt1, paramArrayOfInt2);
/*  690 */     CSS.calculateTiledLayout(this.rowIterator, paramInt1);
/*      */ 
/*  692 */     if (this.captionIndex != -1)
/*      */     {
/*  694 */       View localView = getView(this.captionIndex);
/*  695 */       int i = (int)localView.getPreferredSpan(1);
/*  696 */       paramArrayOfInt2[this.captionIndex] = i;
/*  697 */       int j = (short)(int)this.painter.getInset(3, this);
/*  698 */       if (j != getBottomInset())
/*  699 */         paramArrayOfInt1[this.captionIndex] = (paramInt1 + j);
/*      */       else
/*  701 */         paramArrayOfInt1[this.captionIndex] = (-getTopInset());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected View getViewAtPosition(int paramInt, Rectangle paramRectangle)
/*      */   {
/*  720 */     int i = getViewCount();
/*  721 */     for (int j = 0; j < i; j++) {
/*  722 */       View localView2 = getView(j);
/*  723 */       int k = localView2.getStartOffset();
/*  724 */       int m = localView2.getEndOffset();
/*  725 */       if ((paramInt >= k) && (paramInt < m))
/*      */       {
/*  727 */         if (paramRectangle != null) {
/*  728 */           childAllocation(j, paramRectangle);
/*      */         }
/*  730 */         return localView2;
/*      */       }
/*      */     }
/*  733 */     if (paramInt == getEndOffset()) {
/*  734 */       View localView1 = getView(i - 1);
/*  735 */       if (paramRectangle != null) {
/*  736 */         childAllocation(i - 1, paramRectangle);
/*      */       }
/*  738 */       return localView1;
/*      */     }
/*  740 */     return null;
/*      */   }
/*      */ 
/*      */   public AttributeSet getAttributes()
/*      */   {
/*  751 */     if (this.attr == null) {
/*  752 */       StyleSheet localStyleSheet = getStyleSheet();
/*  753 */       this.attr = localStyleSheet.getViewAttributes(this);
/*      */     }
/*  755 */     return this.attr;
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, Shape paramShape)
/*      */   {
/*  775 */     Rectangle localRectangle = paramShape.getBounds();
/*  776 */     setSize(localRectangle.width, localRectangle.height);
/*  777 */     if (this.captionIndex != -1)
/*      */     {
/*  779 */       i = (short)(int)this.painter.getInset(1, this);
/*  780 */       j = (short)(int)this.painter.getInset(3, this);
/*  781 */       if (i != getTopInset()) {
/*  782 */         int k = getTopInset() - i;
/*  783 */         localRectangle.y += k;
/*  784 */         localRectangle.height -= k;
/*      */       } else {
/*  786 */         localRectangle.height -= getBottomInset() - j;
/*      */       }
/*      */     }
/*  789 */     this.painter.paint(paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, this);
/*      */ 
/*  791 */     int i = getViewCount();
/*  792 */     for (int j = 0; j < i; j++) {
/*  793 */       View localView = getView(j);
/*  794 */       localView.paint(paramGraphics, getChildAllocation(j, paramShape));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setParent(View paramView)
/*      */   {
/*  817 */     super.setParent(paramView);
/*  818 */     if (paramView != null)
/*  819 */       setPropertiesFromAttributes();
/*      */   }
/*      */ 
/*      */   public ViewFactory getViewFactory()
/*      */   {
/*  835 */     return this;
/*      */   }
/*      */ 
/*      */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  853 */     super.insertUpdate(paramDocumentEvent, paramShape, this);
/*      */   }
/*      */ 
/*      */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  871 */     super.removeUpdate(paramDocumentEvent, paramShape, this);
/*      */   }
/*      */ 
/*      */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  889 */     super.changedUpdate(paramDocumentEvent, paramShape, this);
/*      */   }
/*      */ 
/*      */   protected void forwardUpdate(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  894 */     super.forwardUpdate(paramElementChange, paramDocumentEvent, paramShape, paramViewFactory);
/*      */ 
/*  897 */     if (paramShape != null) {
/*  898 */       Container localContainer = getContainer();
/*  899 */       if (localContainer != null) {
/*  900 */         Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*      */ 
/*  902 */         localContainer.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void replace(int paramInt1, int paramInt2, View[] paramArrayOfView)
/*      */   {
/*  913 */     super.replace(paramInt1, paramInt2, paramArrayOfView);
/*  914 */     invalidateGrid();
/*      */   }
/*      */ 
/*      */   public View create(Element paramElement)
/*      */   {
/*  926 */     Object localObject1 = paramElement.getAttributes().getAttribute(StyleConstants.NameAttribute);
/*  927 */     if ((localObject1 instanceof HTML.Tag)) {
/*  928 */       localObject2 = (HTML.Tag)localObject1;
/*  929 */       if (localObject2 == HTML.Tag.TR)
/*  930 */         return createTableRow(paramElement);
/*  931 */       if ((localObject2 == HTML.Tag.TD) || (localObject2 == HTML.Tag.TH))
/*  932 */         return new CellView(paramElement);
/*  933 */       if (localObject2 == HTML.Tag.CAPTION) {
/*  934 */         return new ParagraphView(paramElement);
/*      */       }
/*      */     }
/*      */ 
/*  938 */     Object localObject2 = getParent();
/*  939 */     if (localObject2 != null) {
/*  940 */       ViewFactory localViewFactory = ((View)localObject2).getViewFactory();
/*  941 */       if (localViewFactory != null) {
/*  942 */         return localViewFactory.create(paramElement);
/*      */       }
/*      */     }
/*  945 */     return null;
/*      */   }
/*      */ 
/*      */   class CellView extends BlockView
/*      */   {
/*      */     public CellView(Element arg2)
/*      */     {
/* 1702 */       super(1);
/*      */     }
/*      */ 
/*      */     protected void layoutMajorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */     {
/* 1730 */       super.layoutMajorAxis(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/*      */ 
/* 1732 */       int i = 0;
/* 1733 */       int j = paramArrayOfInt2.length;
/* 1734 */       for (int k = 0; k < j; k++) {
/* 1735 */         i += paramArrayOfInt2[k];
/*      */       }
/*      */ 
/* 1739 */       k = 0;
/* 1740 */       if (i < paramInt1)
/*      */       {
/* 1742 */         String str = (String)getElement().getAttributes().getAttribute(HTML.Attribute.VALIGN);
/*      */ 
/* 1744 */         if (str == null) {
/* 1745 */           AttributeSet localAttributeSet = getElement().getParentElement().getAttributes();
/* 1746 */           str = (String)localAttributeSet.getAttribute(HTML.Attribute.VALIGN);
/*      */         }
/* 1748 */         if ((str == null) || (str.equals("middle")))
/* 1749 */           k = (paramInt1 - i) / 2;
/* 1750 */         else if (str.equals("bottom")) {
/* 1751 */           k = paramInt1 - i;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1756 */       if (k != 0)
/* 1757 */         for (int m = 0; m < j; m++)
/* 1758 */           paramArrayOfInt1[m] += k;
/*      */     }
/*      */ 
/*      */     protected SizeRequirements calculateMajorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */     {
/* 1779 */       SizeRequirements localSizeRequirements = super.calculateMajorAxisRequirements(paramInt, paramSizeRequirements);
/* 1780 */       localSizeRequirements.maximum = 2147483647;
/* 1781 */       return localSizeRequirements;
/*      */     }
/*      */ 
/*      */     protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */     {
/* 1786 */       SizeRequirements localSizeRequirements = super.calculateMinorAxisRequirements(paramInt, paramSizeRequirements);
/*      */ 
/* 1789 */       int i = getViewCount();
/* 1790 */       int j = 0;
/* 1791 */       for (int k = 0; k < i; k++) {
/* 1792 */         View localView = getView(k);
/* 1793 */         j = Math.max((int)localView.getMinimumSpan(paramInt), j);
/*      */       }
/* 1795 */       localSizeRequirements.minimum = Math.min(localSizeRequirements.minimum, j);
/* 1796 */       return localSizeRequirements;
/*      */     }
/*      */   }
/*      */ 
/*      */   class ColumnIterator
/*      */     implements CSS.LayoutIterator
/*      */   {
/*      */     private int col;
/*      */     private int[] percentages;
/*      */     private int[] adjustmentWeights;
/*      */     private int[] offsets;
/*      */     private int[] spans;
/*      */ 
/*      */     ColumnIterator()
/*      */     {
/*      */     }
/*      */ 
/*      */     void disablePercentages()
/*      */     {
/* 1008 */       this.percentages = null;
/*      */     }
/*      */ 
/*      */     private void updatePercentagesAndAdjustmentWeights(int paramInt)
/*      */     {
/* 1015 */       this.adjustmentWeights = new int[TableView.this.columnRequirements.length];
/* 1016 */       for (int i = 0; i < TableView.this.columnRequirements.length; i++) {
/* 1017 */         this.adjustmentWeights[i] = 0;
/*      */       }
/* 1019 */       if (TableView.this.relativeCells)
/* 1020 */         this.percentages = new int[TableView.this.columnRequirements.length];
/*      */       else {
/* 1022 */         this.percentages = null;
/*      */       }
/* 1024 */       i = TableView.this.getRowCount();
/* 1025 */       for (int j = 0; j < i; j++) {
/* 1026 */         TableView.RowView localRowView = TableView.this.getRow(j);
/* 1027 */         int k = 0;
/* 1028 */         int m = localRowView.getViewCount();
/* 1029 */         for (int n = 0; n < m; k++) {
/* 1030 */           View localView = localRowView.getView(n);
/* 1031 */           while (localRowView.isFilled(k)) k++;
/* 1032 */           int i1 = TableView.this.getRowsOccupied(localView);
/* 1033 */           int i2 = TableView.this.getColumnsOccupied(localView);
/* 1034 */           AttributeSet localAttributeSet = localView.getAttributes();
/* 1035 */           CSS.LengthValue localLengthValue = (CSS.LengthValue)localAttributeSet.getAttribute(CSS.Attribute.WIDTH);
/*      */ 
/* 1037 */           if (localLengthValue != null) {
/* 1038 */             int i3 = (int)(localLengthValue.getValue(paramInt) / i2 + 0.5F);
/* 1039 */             for (int i4 = 0; i4 < i2; i4++) {
/* 1040 */               if (localLengthValue.isPercentage())
/*      */               {
/* 1042 */                 this.percentages[(k + i4)] = Math.max(this.percentages[(k + i4)], i3);
/* 1043 */                 this.adjustmentWeights[(k + i4)] = Math.max(this.adjustmentWeights[(k + i4)], 2);
/*      */               } else {
/* 1045 */                 this.adjustmentWeights[(k + i4)] = Math.max(this.adjustmentWeights[(k + i4)], 1);
/*      */               }
/*      */             }
/*      */           }
/* 1049 */           k += i2 - 1;
/*      */ 
/* 1029 */           n++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void setLayoutArrays(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */     {
/* 1058 */       this.offsets = paramArrayOfInt1;
/* 1059 */       this.spans = paramArrayOfInt2;
/* 1060 */       updatePercentagesAndAdjustmentWeights(paramInt);
/*      */     }
/*      */ 
/*      */     public int getCount()
/*      */     {
/* 1066 */       return TableView.this.columnRequirements.length;
/*      */     }
/*      */ 
/*      */     public void setIndex(int paramInt) {
/* 1070 */       this.col = paramInt;
/*      */     }
/*      */ 
/*      */     public void setOffset(int paramInt) {
/* 1074 */       this.offsets[this.col] = paramInt;
/*      */     }
/*      */ 
/*      */     public int getOffset() {
/* 1078 */       return this.offsets[this.col];
/*      */     }
/*      */ 
/*      */     public void setSpan(int paramInt) {
/* 1082 */       this.spans[this.col] = paramInt;
/*      */     }
/*      */ 
/*      */     public int getSpan() {
/* 1086 */       return this.spans[this.col];
/*      */     }
/*      */ 
/*      */     public float getMinimumSpan(float paramFloat)
/*      */     {
/* 1093 */       return TableView.this.columnRequirements[this.col].minimum;
/*      */     }
/*      */ 
/*      */     public float getPreferredSpan(float paramFloat) {
/* 1097 */       if ((this.percentages != null) && (this.percentages[this.col] != 0)) {
/* 1098 */         return Math.max(this.percentages[this.col], TableView.this.columnRequirements[this.col].minimum);
/*      */       }
/* 1100 */       return TableView.this.columnRequirements[this.col].preferred;
/*      */     }
/*      */ 
/*      */     public float getMaximumSpan(float paramFloat) {
/* 1104 */       return TableView.this.columnRequirements[this.col].maximum;
/*      */     }
/*      */ 
/*      */     public float getBorderWidth() {
/* 1108 */       return TableView.this.borderWidth;
/*      */     }
/*      */ 
/*      */     public float getLeadingCollapseSpan()
/*      */     {
/* 1113 */       return TableView.this.cellSpacing;
/*      */     }
/*      */ 
/*      */     public float getTrailingCollapseSpan() {
/* 1117 */       return TableView.this.cellSpacing;
/*      */     }
/*      */ 
/*      */     public int getAdjustmentWeight() {
/* 1121 */       return this.adjustmentWeights[this.col];
/*      */     }
/*      */   }
/*      */ 
/*      */   class RowIterator
/*      */     implements CSS.LayoutIterator
/*      */   {
/*      */     private int row;
/*      */     private int[] adjustments;
/*      */     private int[] offsets;
/*      */     private int[] spans;
/*      */ 
/*      */     RowIterator()
/*      */     {
/*      */     }
/*      */ 
/*      */     void updateAdjustments()
/*      */     {
/* 1147 */       int i = 1;
/* 1148 */       if (TableView.this.multiRowCells)
/*      */       {
/* 1150 */         int j = TableView.this.getRowCount();
/* 1151 */         this.adjustments = new int[j];
/* 1152 */         for (int k = 0; k < j; k++) {
/* 1153 */           TableView.RowView localRowView = TableView.this.getRow(k);
/* 1154 */           if (localRowView.multiRowCells == true) {
/* 1155 */             int m = localRowView.getViewCount();
/* 1156 */             for (int n = 0; n < m; n++) {
/* 1157 */               View localView = localRowView.getView(n);
/* 1158 */               int i1 = TableView.this.getRowsOccupied(localView);
/* 1159 */               if (i1 > 1) {
/* 1160 */                 int i2 = (int)localView.getPreferredSpan(i);
/* 1161 */                 adjustMultiRowSpan(i2, i1, k);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } else {
/* 1167 */         this.adjustments = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     void adjustMultiRowSpan(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1178 */       if (paramInt3 + paramInt2 > getCount())
/*      */       {
/* 1182 */         paramInt2 = getCount() - paramInt3;
/* 1183 */         if (paramInt2 < 1) {
/* 1184 */           return;
/*      */         }
/*      */       }
/* 1187 */       int i = 0;
/* 1188 */       for (int j = 0; j < paramInt2; j++) {
/* 1189 */         TableView.RowView localRowView1 = TableView.this.getRow(paramInt3 + j);
/* 1190 */         i = (int)(i + localRowView1.getPreferredSpan(1));
/*      */       }
/* 1192 */       if (paramInt1 > i) {
/* 1193 */         j = paramInt1 - i;
/* 1194 */         int k = j / paramInt2;
/* 1195 */         int m = k + (j - k * paramInt2);
/* 1196 */         TableView.RowView localRowView2 = TableView.this.getRow(paramInt3);
/* 1197 */         this.adjustments[paramInt3] = Math.max(this.adjustments[paramInt3], m);
/*      */ 
/* 1199 */         for (int n = 1; n < paramInt2; n++)
/* 1200 */           this.adjustments[(paramInt3 + n)] = Math.max(this.adjustments[(paramInt3 + n)], k);
/*      */       }
/*      */     }
/*      */ 
/*      */     void setLayoutArrays(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */     {
/* 1207 */       this.offsets = paramArrayOfInt1;
/* 1208 */       this.spans = paramArrayOfInt2;
/*      */     }
/*      */ 
/*      */     public void setOffset(int paramInt)
/*      */     {
/* 1214 */       TableView.RowView localRowView = TableView.this.getRow(this.row);
/* 1215 */       if (localRowView != null)
/* 1216 */         this.offsets[localRowView.viewIndex] = paramInt;
/*      */     }
/*      */ 
/*      */     public int getOffset()
/*      */     {
/* 1221 */       TableView.RowView localRowView = TableView.this.getRow(this.row);
/* 1222 */       if (localRowView != null) {
/* 1223 */         return this.offsets[localRowView.viewIndex];
/*      */       }
/* 1225 */       return 0;
/*      */     }
/*      */ 
/*      */     public void setSpan(int paramInt) {
/* 1229 */       TableView.RowView localRowView = TableView.this.getRow(this.row);
/* 1230 */       if (localRowView != null)
/* 1231 */         this.spans[localRowView.viewIndex] = paramInt;
/*      */     }
/*      */ 
/*      */     public int getSpan()
/*      */     {
/* 1236 */       TableView.RowView localRowView = TableView.this.getRow(this.row);
/* 1237 */       if (localRowView != null) {
/* 1238 */         return this.spans[localRowView.viewIndex];
/*      */       }
/* 1240 */       return 0;
/*      */     }
/*      */ 
/*      */     public int getCount() {
/* 1244 */       return TableView.this.rows.size();
/*      */     }
/*      */ 
/*      */     public void setIndex(int paramInt) {
/* 1248 */       this.row = paramInt;
/*      */     }
/*      */ 
/*      */     public float getMinimumSpan(float paramFloat) {
/* 1252 */       return getPreferredSpan(paramFloat);
/*      */     }
/*      */ 
/*      */     public float getPreferredSpan(float paramFloat) {
/* 1256 */       TableView.RowView localRowView = TableView.this.getRow(this.row);
/* 1257 */       if (localRowView != null) {
/* 1258 */         int i = this.adjustments != null ? this.adjustments[this.row] : 0;
/* 1259 */         return localRowView.getPreferredSpan(TableView.this.getAxis()) + i;
/*      */       }
/* 1261 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     public float getMaximumSpan(float paramFloat) {
/* 1265 */       return getPreferredSpan(paramFloat);
/*      */     }
/*      */ 
/*      */     public float getBorderWidth() {
/* 1269 */       return TableView.this.borderWidth;
/*      */     }
/*      */ 
/*      */     public float getLeadingCollapseSpan() {
/* 1273 */       return TableView.this.cellSpacing;
/*      */     }
/*      */ 
/*      */     public float getTrailingCollapseSpan() {
/* 1277 */       return TableView.this.cellSpacing;
/*      */     }
/*      */ 
/*      */     public int getAdjustmentWeight() {
/* 1281 */       return 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class RowView extends BoxView
/*      */   {
/*      */     private StyleSheet.BoxPainter painter;
/*      */     private AttributeSet attr;
/*      */     BitSet fillColumns;
/*      */     int rowIndex;
/*      */     int viewIndex;
/*      */     boolean multiRowCells;
/*      */ 
/*      */     public RowView(Element arg2)
/*      */     {
/* 1310 */       super(0);
/* 1311 */       this.fillColumns = new BitSet();
/* 1312 */       setPropertiesFromAttributes();
/*      */     }
/*      */ 
/*      */     void clearFilledColumns() {
/* 1316 */       this.fillColumns.and(TableView.EMPTY);
/*      */     }
/*      */ 
/*      */     void fillColumn(int paramInt) {
/* 1320 */       this.fillColumns.set(paramInt);
/*      */     }
/*      */ 
/*      */     boolean isFilled(int paramInt) {
/* 1324 */       return this.fillColumns.get(paramInt);
/*      */     }
/*      */ 
/*      */     int getColumnCount()
/*      */     {
/* 1331 */       int i = 0;
/* 1332 */       int j = this.fillColumns.size();
/* 1333 */       for (int k = 0; k < j; k++) {
/* 1334 */         if (this.fillColumns.get(k)) {
/* 1335 */           i++;
/*      */         }
/*      */       }
/* 1338 */       return getViewCount() + i;
/*      */     }
/*      */ 
/*      */     public AttributeSet getAttributes()
/*      */     {
/* 1347 */       return this.attr;
/*      */     }
/*      */ 
/*      */     View findViewAtPoint(int paramInt1, int paramInt2, Rectangle paramRectangle) {
/* 1351 */       int i = getViewCount();
/* 1352 */       for (int j = 0; j < i; j++) {
/* 1353 */         if (getChildAllocation(j, paramRectangle).contains(paramInt1, paramInt2)) {
/* 1354 */           childAllocation(j, paramRectangle);
/* 1355 */           return getView(j);
/*      */         }
/*      */       }
/* 1358 */       return null;
/*      */     }
/*      */ 
/*      */     protected StyleSheet getStyleSheet() {
/* 1362 */       HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/* 1363 */       return localHTMLDocument.getStyleSheet();
/*      */     }
/*      */ 
/*      */     public void preferenceChanged(View paramView, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 1380 */       super.preferenceChanged(paramView, paramBoolean1, paramBoolean2);
/* 1381 */       if ((TableView.this.multiRowCells) && (paramBoolean2))
/* 1382 */         for (int i = this.rowIndex - 1; i >= 0; i--) {
/* 1383 */           RowView localRowView = TableView.this.getRow(i);
/* 1384 */           if (localRowView.multiRowCells) {
/* 1385 */             localRowView.preferenceChanged(null, false, true);
/* 1386 */             break;
/*      */           }
/*      */         }
/*      */     }
/*      */ 
/*      */     protected SizeRequirements calculateMajorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */     {
/* 1396 */       SizeRequirements localSizeRequirements = new SizeRequirements();
/* 1397 */       localSizeRequirements.minimum = TableView.this.totalColumnRequirements.minimum;
/* 1398 */       localSizeRequirements.maximum = TableView.this.totalColumnRequirements.maximum;
/* 1399 */       localSizeRequirements.preferred = TableView.this.totalColumnRequirements.preferred;
/* 1400 */       localSizeRequirements.alignment = 0.0F;
/* 1401 */       return localSizeRequirements;
/*      */     }
/*      */ 
/*      */     public float getMinimumSpan(int paramInt)
/*      */     {
/*      */       float f;
/* 1407 */       if (paramInt == 0) {
/* 1408 */         f = TableView.this.totalColumnRequirements.minimum + getLeftInset() + getRightInset();
/*      */       }
/*      */       else
/*      */       {
/* 1412 */         f = super.getMinimumSpan(paramInt);
/*      */       }
/* 1414 */       return f;
/*      */     }
/*      */ 
/*      */     public float getMaximumSpan(int paramInt)
/*      */     {
/*      */       float f;
/* 1420 */       if (paramInt == 0)
/*      */       {
/* 1422 */         f = 2.147484E+009F;
/*      */       }
/*      */       else {
/* 1425 */         f = super.getMaximumSpan(paramInt);
/*      */       }
/* 1427 */       return f;
/*      */     }
/*      */ 
/*      */     public float getPreferredSpan(int paramInt)
/*      */     {
/*      */       float f;
/* 1433 */       if (paramInt == 0) {
/* 1434 */         f = TableView.this.totalColumnRequirements.preferred + getLeftInset() + getRightInset();
/*      */       }
/*      */       else
/*      */       {
/* 1438 */         f = super.getPreferredSpan(paramInt);
/*      */       }
/* 1440 */       return f;
/*      */     }
/*      */ 
/*      */     public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory) {
/* 1444 */       super.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/* 1445 */       int i = paramDocumentEvent.getOffset();
/* 1446 */       if ((i <= getStartOffset()) && (i + paramDocumentEvent.getLength() >= getEndOffset()))
/*      */       {
/* 1448 */         setPropertiesFromAttributes();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void paint(Graphics paramGraphics, Shape paramShape)
/*      */     {
/* 1463 */       Rectangle localRectangle = (Rectangle)paramShape;
/* 1464 */       this.painter.paint(paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, this);
/* 1465 */       super.paint(paramGraphics, localRectangle);
/*      */     }
/*      */ 
/*      */     public void replace(int paramInt1, int paramInt2, View[] paramArrayOfView)
/*      */     {
/* 1474 */       super.replace(paramInt1, paramInt2, paramArrayOfView);
/* 1475 */       TableView.this.invalidateGrid();
/*      */     }
/*      */ 
/*      */     protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */     {
/* 1488 */       long l1 = 0L;
/* 1489 */       long l2 = 0L;
/* 1490 */       long l3 = 0L;
/* 1491 */       this.multiRowCells = false;
/* 1492 */       int i = getViewCount();
/* 1493 */       for (int j = 0; j < i; j++) {
/* 1494 */         View localView = getView(j);
/* 1495 */         if (TableView.this.getRowsOccupied(localView) > 1) {
/* 1496 */           this.multiRowCells = true;
/* 1497 */           l3 = Math.max((int)localView.getMaximumSpan(paramInt), l3);
/*      */         } else {
/* 1499 */           l1 = Math.max((int)localView.getMinimumSpan(paramInt), l1);
/* 1500 */           l2 = Math.max((int)localView.getPreferredSpan(paramInt), l2);
/* 1501 */           l3 = Math.max((int)localView.getMaximumSpan(paramInt), l3);
/*      */         }
/*      */       }
/*      */ 
/* 1505 */       if (paramSizeRequirements == null) {
/* 1506 */         paramSizeRequirements = new SizeRequirements();
/* 1507 */         paramSizeRequirements.alignment = 0.5F;
/*      */       }
/* 1509 */       paramSizeRequirements.preferred = ((int)l2);
/* 1510 */       paramSizeRequirements.minimum = ((int)l1);
/* 1511 */       paramSizeRequirements.maximum = ((int)l3);
/* 1512 */       return paramSizeRequirements;
/*      */     }
/*      */ 
/*      */     protected void layoutMajorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */     {
/* 1537 */       int i = 0;
/* 1538 */       int j = getViewCount();
/* 1539 */       for (int k = 0; k < j; k++) {
/* 1540 */         View localView = getView(k);
/* 1541 */         if ((!TableView.this.skipComments) || ((localView instanceof TableView.CellView)))
/*      */         {
/* 1544 */           while (isFilled(i)) i++;
/* 1545 */           int m = TableView.this.getColumnsOccupied(localView);
/* 1546 */           paramArrayOfInt2[k] = TableView.this.columnSpans[i];
/* 1547 */           paramArrayOfInt1[k] = TableView.this.columnOffsets[i];
/* 1548 */           if (m > 1) {
/* 1549 */             int n = TableView.this.columnSpans.length;
/* 1550 */             for (int i1 = 1; i1 < m; i1++)
/*      */             {
/* 1554 */               if (i + i1 < n) {
/* 1555 */                 paramArrayOfInt2[k] += TableView.this.columnSpans[(i + i1)];
/* 1556 */                 paramArrayOfInt2[k] += TableView.this.cellSpacing;
/*      */               }
/*      */             }
/* 1559 */             i += m - 1;
/*      */           }
/* 1561 */           i++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void layoutMinorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */     {
/* 1588 */       super.layoutMinorAxis(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/* 1589 */       int i = 0;
/* 1590 */       int j = getViewCount();
/* 1591 */       for (int k = 0; k < j; i++) {
/* 1592 */         View localView = getView(k);
/* 1593 */         while (isFilled(i)) i++;
/* 1594 */         int m = TableView.this.getColumnsOccupied(localView);
/* 1595 */         int n = TableView.this.getRowsOccupied(localView);
/* 1596 */         if (n > 1)
/*      */         {
/* 1598 */           int i1 = this.rowIndex;
/* 1599 */           int i2 = Math.min(this.rowIndex + n - 1, TableView.this.getRowCount() - 1);
/* 1600 */           paramArrayOfInt2[k] = TableView.this.getMultiRowSpan(i1, i2);
/*      */         }
/* 1602 */         if (m > 1)
/* 1603 */           i += m - 1;
/* 1591 */         k++;
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getResizeWeight(int paramInt)
/*      */     {
/* 1617 */       return 1;
/*      */     }
/*      */ 
/*      */     protected View getViewAtPosition(int paramInt, Rectangle paramRectangle)
/*      */     {
/* 1634 */       int i = getViewCount();
/* 1635 */       for (int j = 0; j < i; j++) {
/* 1636 */         View localView2 = getView(j);
/* 1637 */         int k = localView2.getStartOffset();
/* 1638 */         int m = localView2.getEndOffset();
/* 1639 */         if ((paramInt >= k) && (paramInt < m))
/*      */         {
/* 1641 */           if (paramRectangle != null) {
/* 1642 */             childAllocation(j, paramRectangle);
/*      */           }
/* 1644 */           return localView2;
/*      */         }
/*      */       }
/* 1647 */       if (paramInt == getEndOffset()) {
/* 1648 */         View localView1 = getView(i - 1);
/* 1649 */         if (paramRectangle != null) {
/* 1650 */           childAllocation(i - 1, paramRectangle);
/*      */         }
/* 1652 */         return localView1;
/*      */       }
/* 1654 */       return null;
/*      */     }
/*      */ 
/*      */     void setPropertiesFromAttributes()
/*      */     {
/* 1661 */       StyleSheet localStyleSheet = getStyleSheet();
/* 1662 */       this.attr = localStyleSheet.getViewAttributes(this);
/* 1663 */       this.painter = localStyleSheet.getBoxPainter(this.attr);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.TableView
 * JD-Core Version:    0.6.2
 */