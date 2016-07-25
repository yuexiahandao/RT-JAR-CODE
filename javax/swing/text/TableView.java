/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.util.BitSet;
/*     */ import java.util.Vector;
/*     */ import javax.swing.SizeRequirements;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentEvent.ElementChange;
/*     */ import javax.swing.text.html.HTML.Attribute;
/*     */ 
/*     */ public abstract class TableView extends BoxView
/*     */ {
/*     */   int[] columnSpans;
/*     */   int[] columnOffsets;
/*     */   SizeRequirements[] columnRequirements;
/*     */   Vector<TableRow> rows;
/*     */   boolean gridValid;
/* 582 */   private static final BitSet EMPTY = new BitSet();
/*     */ 
/*     */   public TableView(Element paramElement)
/*     */   {
/*  78 */     super(paramElement, 1);
/*  79 */     this.rows = new Vector();
/*  80 */     this.gridValid = false;
/*     */   }
/*     */ 
/*     */   protected TableRow createTableRow(Element paramElement)
/*     */   {
/*  90 */     return new TableRow(paramElement);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected TableCell createTableCell(Element paramElement)
/*     */   {
/* 103 */     return new TableCell(paramElement);
/*     */   }
/*     */ 
/*     */   int getColumnCount()
/*     */   {
/* 110 */     return this.columnSpans.length;
/*     */   }
/*     */ 
/*     */   int getColumnSpan(int paramInt)
/*     */   {
/* 119 */     return this.columnSpans[paramInt];
/*     */   }
/*     */ 
/*     */   int getRowCount()
/*     */   {
/* 126 */     return this.rows.size();
/*     */   }
/*     */ 
/*     */   int getRowSpan(int paramInt)
/*     */   {
/* 133 */     TableRow localTableRow = getRow(paramInt);
/* 134 */     if (localTableRow != null) {
/* 135 */       return (int)localTableRow.getPreferredSpan(1);
/*     */     }
/* 137 */     return 0;
/*     */   }
/*     */ 
/*     */   TableRow getRow(int paramInt) {
/* 141 */     if (paramInt < this.rows.size()) {
/* 142 */       return (TableRow)this.rows.elementAt(paramInt);
/*     */     }
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */   int getColumnsOccupied(View paramView)
/*     */   {
/* 154 */     AttributeSet localAttributeSet = paramView.getElement().getAttributes();
/* 155 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.COLSPAN);
/* 156 */     if (str != null) {
/*     */       try {
/* 158 */         return Integer.parseInt(str);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException)
/*     */       {
/*     */       }
/*     */     }
/* 164 */     return 1;
/*     */   }
/*     */ 
/*     */   int getRowsOccupied(View paramView)
/*     */   {
/* 174 */     AttributeSet localAttributeSet = paramView.getElement().getAttributes();
/* 175 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.ROWSPAN);
/* 176 */     if (str != null) {
/*     */       try {
/* 178 */         return Integer.parseInt(str);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException)
/*     */       {
/*     */       }
/*     */     }
/* 184 */     return 1;
/*     */   }
/*     */ 
/*     */   void invalidateGrid() {
/* 188 */     this.gridValid = false;
/*     */   }
/*     */ 
/*     */   protected void forwardUpdate(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 193 */     super.forwardUpdate(paramElementChange, paramDocumentEvent, paramShape, paramViewFactory);
/*     */ 
/* 196 */     if (paramShape != null) {
/* 197 */       Container localContainer = getContainer();
/* 198 */       if (localContainer != null) {
/* 199 */         Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*     */ 
/* 201 */         localContainer.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void replace(int paramInt1, int paramInt2, View[] paramArrayOfView)
/*     */   {
/* 212 */     super.replace(paramInt1, paramInt2, paramArrayOfView);
/* 213 */     invalidateGrid();
/*     */   }
/*     */ 
/*     */   void updateGrid()
/*     */   {
/* 222 */     if (!this.gridValid)
/*     */     {
/* 225 */       this.rows.removeAllElements();
/* 226 */       int i = getViewCount();
/* 227 */       for (int j = 0; j < i; j++) {
/* 228 */         View localView1 = getView(j);
/* 229 */         if ((localView1 instanceof TableRow)) {
/* 230 */           this.rows.addElement((TableRow)localView1);
/* 231 */           TableRow localTableRow1 = (TableRow)localView1;
/* 232 */           localTableRow1.clearFilledColumns();
/* 233 */           localTableRow1.setRow(j);
/*     */         }
/*     */       }
/*     */ 
/* 237 */       j = 0;
/* 238 */       int k = this.rows.size();
/* 239 */       for (int m = 0; m < k; m++) {
/* 240 */         TableRow localTableRow2 = getRow(m);
/* 241 */         int n = 0;
/* 242 */         for (int i1 = 0; i1 < localTableRow2.getViewCount(); n++) {
/* 243 */           View localView2 = localTableRow2.getView(i1);
/*     */ 
/* 245 */           while (localTableRow2.isFilled(n)) n++;
/* 246 */           int i2 = getRowsOccupied(localView2);
/* 247 */           int i3 = getColumnsOccupied(localView2);
/* 248 */           if ((i3 > 1) || (i2 > 1))
/*     */           {
/* 250 */             int i4 = m + i2;
/* 251 */             int i5 = n + i3;
/* 252 */             for (int i6 = m; i6 < i4; i6++) {
/* 253 */               for (int i7 = n; i7 < i5; i7++) {
/* 254 */                 if ((i6 != m) || (i7 != n)) {
/* 255 */                   addFill(i6, i7);
/*     */                 }
/*     */               }
/*     */             }
/* 259 */             if (i3 > 1)
/* 260 */               n += i3 - 1;
/*     */           }
/* 242 */           i1++;
/*     */         }
/*     */ 
/* 264 */         j = Math.max(j, n);
/*     */       }
/*     */ 
/* 268 */       this.columnSpans = new int[j];
/* 269 */       this.columnOffsets = new int[j];
/* 270 */       this.columnRequirements = new SizeRequirements[j];
/* 271 */       for (m = 0; m < j; m++) {
/* 272 */         this.columnRequirements[m] = new SizeRequirements();
/*     */       }
/* 274 */       this.gridValid = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   void addFill(int paramInt1, int paramInt2)
/*     */   {
/* 282 */     TableRow localTableRow = getRow(paramInt1);
/* 283 */     if (localTableRow != null)
/* 284 */       localTableRow.fillColumn(paramInt2);
/*     */   }
/*     */ 
/*     */   protected void layoutColumns(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, SizeRequirements[] paramArrayOfSizeRequirements)
/*     */   {
/* 305 */     SizeRequirements.calculateTiledPositions(paramInt, null, paramArrayOfSizeRequirements, paramArrayOfInt1, paramArrayOfInt2);
/*     */   }
/*     */ 
/*     */   protected void layoutMinorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 333 */     updateGrid();
/*     */ 
/* 336 */     int i = getRowCount();
/* 337 */     for (int j = 0; j < i; j++) {
/* 338 */       TableRow localTableRow = getRow(j);
/* 339 */       localTableRow.layoutChanged(paramInt2);
/*     */     }
/*     */ 
/* 343 */     layoutColumns(paramInt1, this.columnOffsets, this.columnSpans, this.columnRequirements);
/*     */ 
/* 346 */     super.layoutMinorAxis(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/*     */   }
/*     */ 
/*     */   protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*     */   {
/* 358 */     updateGrid();
/*     */ 
/* 361 */     calculateColumnRequirements(paramInt);
/*     */ 
/* 365 */     if (paramSizeRequirements == null) {
/* 366 */       paramSizeRequirements = new SizeRequirements();
/*     */     }
/* 368 */     long l1 = 0L;
/* 369 */     long l2 = 0L;
/* 370 */     long l3 = 0L;
/* 371 */     for (SizeRequirements localSizeRequirements : this.columnRequirements) {
/* 372 */       l1 += localSizeRequirements.minimum;
/* 373 */       l2 += localSizeRequirements.preferred;
/* 374 */       l3 += localSizeRequirements.maximum;
/*     */     }
/* 376 */     paramSizeRequirements.minimum = ((int)l1);
/* 377 */     paramSizeRequirements.preferred = ((int)l2);
/* 378 */     paramSizeRequirements.maximum = ((int)l3);
/* 379 */     paramSizeRequirements.alignment = 0.0F;
/* 380 */     return paramSizeRequirements;
/*     */   }
/*     */ 
/*     */   void calculateColumnRequirements(int paramInt)
/*     */   {
/* 410 */     int i = 0;
/* 411 */     int j = getRowCount();
/*     */     TableRow localTableRow;
/*     */     int m;
/*     */     int n;
/*     */     int i1;
/*     */     View localView;
/*     */     int i2;
/* 412 */     for (int k = 0; k < j; k++) {
/* 413 */       localTableRow = getRow(k);
/* 414 */       m = 0;
/* 415 */       n = localTableRow.getViewCount();
/* 416 */       for (i1 = 0; i1 < n; m++) {
/* 417 */         localView = localTableRow.getView(i1);
/* 418 */         while (localTableRow.isFilled(m)) m++;
/* 419 */         i2 = getRowsOccupied(localView);
/* 420 */         int i3 = getColumnsOccupied(localView);
/* 421 */         if (i3 == 1) {
/* 422 */           checkSingleColumnCell(paramInt, m, localView);
/*     */         } else {
/* 424 */           i = 1;
/* 425 */           m += i3 - 1;
/*     */         }
/* 416 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 431 */     if (i != 0)
/* 432 */       for (k = 0; k < j; k++) {
/* 433 */         localTableRow = getRow(k);
/* 434 */         m = 0;
/* 435 */         n = localTableRow.getViewCount();
/* 436 */         for (i1 = 0; i1 < n; m++) {
/* 437 */           localView = localTableRow.getView(i1);
/* 438 */           while (localTableRow.isFilled(m)) m++;
/* 439 */           i2 = getColumnsOccupied(localView);
/* 440 */           if (i2 > 1) {
/* 441 */             checkMultiColumnCell(paramInt, m, i2, localView);
/* 442 */             m += i2 - 1;
/*     */           }
/* 436 */           i1++;
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   void checkSingleColumnCell(int paramInt1, int paramInt2, View paramView)
/*     */   {
/* 462 */     SizeRequirements localSizeRequirements = this.columnRequirements[paramInt2];
/* 463 */     localSizeRequirements.minimum = Math.max((int)paramView.getMinimumSpan(paramInt1), localSizeRequirements.minimum);
/* 464 */     localSizeRequirements.preferred = Math.max((int)paramView.getPreferredSpan(paramInt1), localSizeRequirements.preferred);
/* 465 */     localSizeRequirements.maximum = Math.max((int)paramView.getMaximumSpan(paramInt1), localSizeRequirements.maximum);
/*     */   }
/*     */ 
/*     */   void checkMultiColumnCell(int paramInt1, int paramInt2, int paramInt3, View paramView)
/*     */   {
/* 474 */     long l1 = 0L;
/* 475 */     long l2 = 0L;
/* 476 */     long l3 = 0L;
/*     */     Object localObject1;
/* 477 */     for (int i = 0; i < paramInt3; i++) {
/* 478 */       localObject1 = this.columnRequirements[(paramInt2 + i)];
/* 479 */       l1 += ((SizeRequirements)localObject1).minimum;
/* 480 */       l2 += ((SizeRequirements)localObject1).preferred;
/* 481 */       l3 += ((SizeRequirements)localObject1).maximum;
/*     */     }
/*     */ 
/* 485 */     i = (int)paramView.getMinimumSpan(paramInt1);
/*     */     Object localObject2;
/* 486 */     if (i > l1)
/*     */     {
/* 493 */       localObject1 = new SizeRequirements[paramInt3];
/* 494 */       for (int k = 0; k < paramInt3; k++) {
/* 495 */         localObject3 = localObject1[k] =  = this.columnRequirements[(paramInt2 + k)];
/* 496 */         ((SizeRequirements)localObject3).maximum = Math.max(((SizeRequirements)localObject3).maximum, (int)paramView.getMaximumSpan(paramInt1));
/*     */       }
/* 498 */       localObject2 = new int[paramInt3];
/* 499 */       Object localObject3 = new int[paramInt3];
/* 500 */       SizeRequirements.calculateTiledPositions(i, null, (SizeRequirements[])localObject1, (int[])localObject3, (int[])localObject2);
/*     */ 
/* 503 */       for (int n = 0; n < paramInt3; n++) {
/* 504 */         Object localObject5 = localObject1[n];
/* 505 */         localObject5.minimum = Math.max(localObject2[n], localObject5.minimum);
/* 506 */         localObject5.preferred = Math.max(localObject5.minimum, localObject5.preferred);
/* 507 */         localObject5.maximum = Math.max(localObject5.preferred, localObject5.maximum);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 512 */     int j = (int)paramView.getPreferredSpan(paramInt1);
/* 513 */     if (j > l2)
/*     */     {
/* 520 */       localObject2 = new SizeRequirements[paramInt3];
/* 521 */       for (int m = 0; m < paramInt3; m++) {
/* 522 */         localObject4 = localObject2[m] =  = this.columnRequirements[(paramInt2 + m)];
/*     */       }
/* 524 */       int[] arrayOfInt = new int[paramInt3];
/* 525 */       Object localObject4 = new int[paramInt3];
/* 526 */       SizeRequirements.calculateTiledPositions(j, null, (SizeRequirements[])localObject2, (int[])localObject4, arrayOfInt);
/*     */ 
/* 529 */       for (int i1 = 0; i1 < paramInt3; i1++) {
/* 530 */         Object localObject6 = localObject2[i1];
/* 531 */         localObject6.preferred = Math.max(arrayOfInt[i1], localObject6.preferred);
/* 532 */         localObject6.maximum = Math.max(localObject6.preferred, localObject6.maximum);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected View getViewAtPosition(int paramInt, Rectangle paramRectangle)
/*     */   {
/* 552 */     int i = getViewCount();
/* 553 */     for (int j = 0; j < i; j++) {
/* 554 */       View localView2 = getView(j);
/* 555 */       int k = localView2.getStartOffset();
/* 556 */       int m = localView2.getEndOffset();
/* 557 */       if ((paramInt >= k) && (paramInt < m))
/*     */       {
/* 559 */         if (paramRectangle != null) {
/* 560 */           childAllocation(j, paramRectangle);
/*     */         }
/* 562 */         return localView2;
/*     */       }
/*     */     }
/* 565 */     if (paramInt == getEndOffset()) {
/* 566 */       View localView1 = getView(i - 1);
/* 567 */       if (paramRectangle != null) {
/* 568 */         childAllocation(i - 1, paramRectangle);
/*     */       }
/* 570 */       return localView1;
/*     */     }
/* 572 */     return null;
/*     */   }
/*     */ 
/*     */   static abstract interface GridCell
/*     */   {
/*     */     public abstract void setGridLocation(int paramInt1, int paramInt2);
/*     */ 
/*     */     public abstract int getGridRow();
/*     */ 
/*     */     public abstract int getGridColumn();
/*     */ 
/*     */     public abstract int getColumnCount();
/*     */ 
/*     */     public abstract int getRowCount();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public class TableCell extends BoxView
/*     */     implements TableView.GridCell
/*     */   {
/*     */     int row;
/*     */     int col;
/*     */ 
/*     */     public TableCell(Element arg2)
/*     */     {
/* 807 */       super(1);
/*     */     }
/*     */ 
/*     */     public int getColumnCount()
/*     */     {
/* 819 */       return 1;
/*     */     }
/*     */ 
/*     */     public int getRowCount()
/*     */     {
/* 829 */       return 1;
/*     */     }
/*     */ 
/*     */     public void setGridLocation(int paramInt1, int paramInt2)
/*     */     {
/* 840 */       this.row = paramInt1;
/* 841 */       this.col = paramInt2;
/*     */     }
/*     */ 
/*     */     public int getGridRow()
/*     */     {
/* 848 */       return this.row;
/*     */     }
/*     */ 
/*     */     public int getGridColumn()
/*     */     {
/* 855 */       return this.col;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class TableRow extends BoxView
/*     */   {
/*     */     BitSet fillColumns;
/*     */     int row;
/*     */ 
/*     */     public TableRow(Element arg2)
/*     */     {
/* 596 */       super(0);
/* 597 */       this.fillColumns = new BitSet();
/*     */     }
/*     */ 
/*     */     void clearFilledColumns() {
/* 601 */       this.fillColumns.and(TableView.EMPTY);
/*     */     }
/*     */ 
/*     */     void fillColumn(int paramInt) {
/* 605 */       this.fillColumns.set(paramInt);
/*     */     }
/*     */ 
/*     */     boolean isFilled(int paramInt) {
/* 609 */       return this.fillColumns.get(paramInt);
/*     */     }
/*     */ 
/*     */     int getRow()
/*     */     {
/* 614 */       return this.row;
/*     */     }
/*     */ 
/*     */     void setRow(int paramInt)
/*     */     {
/* 622 */       this.row = paramInt;
/*     */     }
/*     */ 
/*     */     int getColumnCount()
/*     */     {
/* 629 */       int i = 0;
/* 630 */       int j = this.fillColumns.size();
/* 631 */       for (int k = 0; k < j; k++) {
/* 632 */         if (this.fillColumns.get(k)) {
/* 633 */           i++;
/*     */         }
/*     */       }
/* 636 */       return getViewCount() + i;
/*     */     }
/*     */ 
/*     */     public void replace(int paramInt1, int paramInt2, View[] paramArrayOfView)
/*     */     {
/* 645 */       super.replace(paramInt1, paramInt2, paramArrayOfView);
/* 646 */       TableView.this.invalidateGrid();
/*     */     }
/*     */ 
/*     */     protected void layoutMajorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */     {
/* 669 */       int i = 0;
/* 670 */       int j = getViewCount();
/* 671 */       for (int k = 0; k < j; i++) {
/* 672 */         View localView = getView(k);
/* 673 */         while (isFilled(i)) i++;
/* 674 */         int m = TableView.this.getColumnsOccupied(localView);
/* 675 */         paramArrayOfInt2[k] = TableView.this.columnSpans[i];
/* 676 */         paramArrayOfInt1[k] = TableView.this.columnOffsets[i];
/* 677 */         if (m > 1) {
/* 678 */           int n = TableView.this.columnSpans.length;
/* 679 */           for (int i1 = 1; i1 < m; i1++)
/*     */           {
/* 683 */             if (i + i1 < n) {
/* 684 */               paramArrayOfInt2[k] += TableView.this.columnSpans[(i + i1)];
/*     */             }
/*     */           }
/* 687 */           i += m - 1;
/*     */         }
/* 671 */         k++;
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void layoutMinorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */     {
/* 713 */       super.layoutMinorAxis(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
/* 714 */       int i = 0;
/* 715 */       int j = getViewCount();
/* 716 */       for (int k = 0; k < j; i++) {
/* 717 */         View localView = getView(k);
/* 718 */         while (isFilled(i)) i++;
/* 719 */         int m = TableView.this.getColumnsOccupied(localView);
/* 720 */         int n = TableView.this.getRowsOccupied(localView);
/* 721 */         if (n > 1) {
/* 722 */           for (int i1 = 1; i1 < n; i1++)
/*     */           {
/* 726 */             int i2 = getRow() + i1;
/* 727 */             if (i2 < TableView.this.getViewCount()) {
/* 728 */               int i3 = TableView.this.getSpan(1, getRow() + i1);
/* 729 */               paramArrayOfInt2[k] += i3;
/*     */             }
/*     */           }
/*     */         }
/* 733 */         if (m > 1)
/* 734 */           i += m - 1;
/* 716 */         k++;
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getResizeWeight(int paramInt)
/*     */     {
/* 748 */       return 1;
/*     */     }
/*     */ 
/*     */     protected View getViewAtPosition(int paramInt, Rectangle paramRectangle)
/*     */     {
/* 765 */       int i = getViewCount();
/* 766 */       for (int j = 0; j < i; j++) {
/* 767 */         View localView2 = getView(j);
/* 768 */         int k = localView2.getStartOffset();
/* 769 */         int m = localView2.getEndOffset();
/* 770 */         if ((paramInt >= k) && (paramInt < m))
/*     */         {
/* 772 */           if (paramRectangle != null) {
/* 773 */             childAllocation(j, paramRectangle);
/*     */           }
/* 775 */           return localView2;
/*     */         }
/*     */       }
/* 778 */       if (paramInt == getEndOffset()) {
/* 779 */         View localView1 = getView(i - 1);
/* 780 */         if (paramRectangle != null) {
/* 781 */           childAllocation(i - 1, paramRectangle);
/*     */         }
/* 783 */         return localView1;
/*     */       }
/* 785 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.TableView
 * JD-Core Version:    0.6.2
 */