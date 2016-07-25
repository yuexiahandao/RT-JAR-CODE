/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.text.DateFormat;
/*     */ import java.text.Format;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Date;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTable.DropLocation;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTableUI;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ 
/*     */ public class SynthTableUI extends BasicTableUI
/*     */   implements SynthUI, PropertyChangeListener
/*     */ {
/*     */   private SynthStyle style;
/*     */   private boolean useTableColors;
/*     */   private boolean useUIBorder;
/*     */   private Color alternateColor;
/*     */   private TableCellRenderer dateRenderer;
/*     */   private TableCellRenderer numberRenderer;
/*     */   private TableCellRenderer doubleRender;
/*     */   private TableCellRenderer floatRenderer;
/*     */   private TableCellRenderer iconRenderer;
/*     */   private TableCellRenderer imageIconRenderer;
/*     */   private TableCellRenderer booleanRenderer;
/*     */   private TableCellRenderer objectRenderer;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  97 */     return new SynthTableUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 110 */     this.dateRenderer = installRendererIfPossible(Date.class, null);
/* 111 */     this.numberRenderer = installRendererIfPossible(Number.class, null);
/* 112 */     this.doubleRender = installRendererIfPossible(Double.class, null);
/* 113 */     this.floatRenderer = installRendererIfPossible(Float.class, null);
/* 114 */     this.iconRenderer = installRendererIfPossible(Icon.class, null);
/* 115 */     this.imageIconRenderer = installRendererIfPossible(ImageIcon.class, null);
/* 116 */     this.booleanRenderer = installRendererIfPossible(Boolean.class, new SynthBooleanTableCellRenderer());
/*     */ 
/* 118 */     this.objectRenderer = installRendererIfPossible(Object.class, new SynthTableCellRenderer(null));
/*     */ 
/* 120 */     updateStyle(this.table);
/*     */   }
/*     */ 
/*     */   private TableCellRenderer installRendererIfPossible(Class paramClass, TableCellRenderer paramTableCellRenderer)
/*     */   {
/* 125 */     TableCellRenderer localTableCellRenderer = this.table.getDefaultRenderer(paramClass);
/*     */ 
/* 127 */     if ((localTableCellRenderer instanceof UIResource)) {
/* 128 */       this.table.setDefaultRenderer(paramClass, paramTableCellRenderer);
/*     */     }
/* 130 */     return localTableCellRenderer;
/*     */   }
/*     */ 
/*     */   private void updateStyle(JTable paramJTable) {
/* 134 */     SynthContext localSynthContext = getContext(paramJTable, 1);
/* 135 */     SynthStyle localSynthStyle = this.style;
/* 136 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 137 */     if (this.style != localSynthStyle) {
/* 138 */       localSynthContext.setComponentState(513);
/*     */ 
/* 140 */       Color localColor1 = this.table.getSelectionBackground();
/* 141 */       if ((localColor1 == null) || ((localColor1 instanceof UIResource))) {
/* 142 */         this.table.setSelectionBackground(this.style.getColor(localSynthContext, ColorType.TEXT_BACKGROUND));
/*     */       }
/*     */ 
/* 146 */       Color localColor2 = this.table.getSelectionForeground();
/* 147 */       if ((localColor2 == null) || ((localColor2 instanceof UIResource))) {
/* 148 */         this.table.setSelectionForeground(this.style.getColor(localSynthContext, ColorType.TEXT_FOREGROUND));
/*     */       }
/*     */ 
/* 152 */       localSynthContext.setComponentState(1);
/*     */ 
/* 154 */       Color localColor3 = this.table.getGridColor();
/* 155 */       if ((localColor3 == null) || ((localColor3 instanceof UIResource))) {
/* 156 */         localColor3 = (Color)this.style.get(localSynthContext, "Table.gridColor");
/* 157 */         if (localColor3 == null) {
/* 158 */           localColor3 = this.style.getColor(localSynthContext, ColorType.FOREGROUND);
/*     */         }
/* 160 */         this.table.setGridColor(localColor3 == null ? new ColorUIResource(Color.GRAY) : localColor3);
/*     */       }
/*     */ 
/* 163 */       this.useTableColors = this.style.getBoolean(localSynthContext, "Table.rendererUseTableColors", true);
/*     */ 
/* 165 */       this.useUIBorder = this.style.getBoolean(localSynthContext, "Table.rendererUseUIBorder", true);
/*     */ 
/* 168 */       Object localObject = this.style.get(localSynthContext, "Table.rowHeight");
/* 169 */       if (localObject != null) {
/* 170 */         LookAndFeel.installProperty(this.table, "rowHeight", localObject);
/*     */       }
/* 172 */       boolean bool = this.style.getBoolean(localSynthContext, "Table.showGrid", true);
/* 173 */       if (!bool) {
/* 174 */         this.table.setShowGrid(false);
/*     */       }
/* 176 */       Dimension localDimension = this.table.getIntercellSpacing();
/*     */ 
/* 178 */       if (localDimension != null) {
/* 179 */         localDimension = (Dimension)this.style.get(localSynthContext, "Table.intercellSpacing");
/*     */       }
/* 181 */       this.alternateColor = ((Color)this.style.get(localSynthContext, "Table.alternateRowColor"));
/* 182 */       if (localDimension != null) {
/* 183 */         this.table.setIntercellSpacing(localDimension);
/*     */       }
/*     */ 
/* 187 */       if (localSynthStyle != null) {
/* 188 */         uninstallKeyboardActions();
/* 189 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 192 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 200 */     super.installListeners();
/* 201 */     this.table.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 209 */     this.table.setDefaultRenderer(Date.class, this.dateRenderer);
/* 210 */     this.table.setDefaultRenderer(Number.class, this.numberRenderer);
/* 211 */     this.table.setDefaultRenderer(Double.class, this.doubleRender);
/* 212 */     this.table.setDefaultRenderer(Float.class, this.floatRenderer);
/* 213 */     this.table.setDefaultRenderer(Icon.class, this.iconRenderer);
/* 214 */     this.table.setDefaultRenderer(ImageIcon.class, this.imageIconRenderer);
/* 215 */     this.table.setDefaultRenderer(Boolean.class, this.booleanRenderer);
/* 216 */     this.table.setDefaultRenderer(Object.class, this.objectRenderer);
/*     */ 
/* 218 */     if ((this.table.getTransferHandler() instanceof UIResource)) {
/* 219 */       this.table.setTransferHandler(null);
/*     */     }
/* 221 */     SynthContext localSynthContext = getContext(this.table, 1);
/* 222 */     this.style.uninstallDefaults(localSynthContext);
/* 223 */     localSynthContext.dispose();
/* 224 */     this.style = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 232 */     this.table.removePropertyChangeListener(this);
/* 233 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 245 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 249 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 271 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 273 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 274 */     localSynthContext.getPainter().paintTableBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 276 */     paint(localSynthContext, paramGraphics);
/* 277 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 286 */     paramSynthContext.getPainter().paintTableBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 300 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 302 */     paint(localSynthContext, paramGraphics);
/* 303 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 314 */     Rectangle localRectangle1 = paramGraphics.getClipBounds();
/*     */ 
/* 316 */     Rectangle localRectangle2 = this.table.getBounds();
/*     */ 
/* 319 */     localRectangle2.x = (localRectangle2.y = 0);
/*     */ 
/* 321 */     if ((this.table.getRowCount() <= 0) || (this.table.getColumnCount() <= 0) || (!localRectangle2.intersects(localRectangle1)))
/*     */     {
/* 326 */       paintDropLines(paramSynthContext, paramGraphics);
/* 327 */       return;
/*     */     }
/*     */ 
/* 330 */     boolean bool = this.table.getComponentOrientation().isLeftToRight();
/*     */ 
/* 332 */     Point localPoint1 = localRectangle1.getLocation();
/*     */ 
/* 334 */     Point localPoint2 = new Point(localRectangle1.x + localRectangle1.width - 1, localRectangle1.y + localRectangle1.height - 1);
/*     */ 
/* 337 */     int i = this.table.rowAtPoint(localPoint1);
/* 338 */     int j = this.table.rowAtPoint(localPoint2);
/*     */ 
/* 341 */     if (i == -1) {
/* 342 */       i = 0;
/*     */     }
/*     */ 
/* 348 */     if (j == -1) {
/* 349 */       j = this.table.getRowCount() - 1;
/*     */     }
/*     */ 
/* 352 */     int k = this.table.columnAtPoint(bool ? localPoint1 : localPoint2);
/* 353 */     int m = this.table.columnAtPoint(bool ? localPoint2 : localPoint1);
/*     */ 
/* 355 */     if (k == -1) {
/* 356 */       k = 0;
/*     */     }
/*     */ 
/* 360 */     if (m == -1) {
/* 361 */       m = this.table.getColumnCount() - 1;
/*     */     }
/*     */ 
/* 365 */     paintCells(paramSynthContext, paramGraphics, i, j, k, m);
/*     */ 
/* 370 */     paintGrid(paramSynthContext, paramGraphics, i, j, k, m);
/*     */ 
/* 372 */     paintDropLines(paramSynthContext, paramGraphics);
/*     */   }
/*     */ 
/*     */   private void paintDropLines(SynthContext paramSynthContext, Graphics paramGraphics) {
/* 376 */     JTable.DropLocation localDropLocation = this.table.getDropLocation();
/* 377 */     if (localDropLocation == null) {
/* 378 */       return;
/*     */     }
/*     */ 
/* 381 */     Color localColor1 = (Color)this.style.get(paramSynthContext, "Table.dropLineColor");
/* 382 */     Color localColor2 = (Color)this.style.get(paramSynthContext, "Table.dropLineShortColor");
/* 383 */     if ((localColor1 == null) && (localColor2 == null)) {
/* 384 */       return;
/*     */     }
/*     */ 
/* 389 */     Rectangle localRectangle = getHDropLineRect(localDropLocation);
/*     */     int i;
/*     */     int j;
/* 390 */     if (localRectangle != null) {
/* 391 */       i = localRectangle.x;
/* 392 */       j = localRectangle.width;
/* 393 */       if (localColor1 != null) {
/* 394 */         extendRect(localRectangle, true);
/* 395 */         paramGraphics.setColor(localColor1);
/* 396 */         paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */       }
/* 398 */       if ((!localDropLocation.isInsertColumn()) && (localColor2 != null)) {
/* 399 */         paramGraphics.setColor(localColor2);
/* 400 */         paramGraphics.fillRect(i, localRectangle.y, j, localRectangle.height);
/*     */       }
/*     */     }
/*     */ 
/* 404 */     localRectangle = getVDropLineRect(localDropLocation);
/* 405 */     if (localRectangle != null) {
/* 406 */       i = localRectangle.y;
/* 407 */       j = localRectangle.height;
/* 408 */       if (localColor1 != null) {
/* 409 */         extendRect(localRectangle, false);
/* 410 */         paramGraphics.setColor(localColor1);
/* 411 */         paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */       }
/* 413 */       if ((!localDropLocation.isInsertRow()) && (localColor2 != null)) {
/* 414 */         paramGraphics.setColor(localColor2);
/* 415 */         paramGraphics.fillRect(localRectangle.x, i, localRectangle.width, j);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private Rectangle getHDropLineRect(JTable.DropLocation paramDropLocation) {
/* 421 */     if (!paramDropLocation.isInsertRow()) {
/* 422 */       return null;
/*     */     }
/*     */ 
/* 425 */     int i = paramDropLocation.getRow();
/* 426 */     int j = paramDropLocation.getColumn();
/* 427 */     if (j >= this.table.getColumnCount()) {
/* 428 */       j--;
/*     */     }
/*     */ 
/* 431 */     Rectangle localRectangle1 = this.table.getCellRect(i, j, true);
/*     */ 
/* 433 */     if (i >= this.table.getRowCount()) {
/* 434 */       i--;
/* 435 */       Rectangle localRectangle2 = this.table.getCellRect(i, j, true);
/* 436 */       localRectangle2.y += localRectangle2.height;
/*     */     }
/*     */ 
/* 439 */     if (localRectangle1.y == 0)
/* 440 */       localRectangle1.y = -1;
/*     */     else {
/* 442 */       localRectangle1.y -= 2;
/*     */     }
/*     */ 
/* 445 */     localRectangle1.height = 3;
/*     */ 
/* 447 */     return localRectangle1;
/*     */   }
/*     */ 
/*     */   private Rectangle getVDropLineRect(JTable.DropLocation paramDropLocation) {
/* 451 */     if (!paramDropLocation.isInsertColumn()) {
/* 452 */       return null;
/*     */     }
/*     */ 
/* 455 */     boolean bool = this.table.getComponentOrientation().isLeftToRight();
/* 456 */     int i = paramDropLocation.getColumn();
/* 457 */     Rectangle localRectangle = this.table.getCellRect(paramDropLocation.getRow(), i, true);
/*     */ 
/* 459 */     if (i >= this.table.getColumnCount()) {
/* 460 */       i--;
/* 461 */       localRectangle = this.table.getCellRect(paramDropLocation.getRow(), i, true);
/* 462 */       if (bool)
/* 463 */         localRectangle.x += localRectangle.width;
/*     */     }
/* 465 */     else if (!bool) {
/* 466 */       localRectangle.x += localRectangle.width;
/*     */     }
/*     */ 
/* 469 */     if (localRectangle.x == 0)
/* 470 */       localRectangle.x = -1;
/*     */     else {
/* 472 */       localRectangle.x -= 2;
/*     */     }
/*     */ 
/* 475 */     localRectangle.width = 3;
/*     */ 
/* 477 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   private Rectangle extendRect(Rectangle paramRectangle, boolean paramBoolean) {
/* 481 */     if (paramRectangle == null) {
/* 482 */       return paramRectangle;
/*     */     }
/*     */ 
/* 485 */     if (paramBoolean) {
/* 486 */       paramRectangle.x = 0;
/* 487 */       paramRectangle.width = this.table.getWidth();
/*     */     } else {
/* 489 */       paramRectangle.y = 0;
/*     */ 
/* 491 */       if (this.table.getRowCount() != 0) {
/* 492 */         Rectangle localRectangle = this.table.getCellRect(this.table.getRowCount() - 1, 0, true);
/* 493 */         paramRectangle.height = (localRectangle.y + localRectangle.height);
/*     */       } else {
/* 495 */         paramRectangle.height = this.table.getHeight();
/*     */       }
/*     */     }
/*     */ 
/* 499 */     return paramRectangle;
/*     */   }
/*     */ 
/*     */   private void paintGrid(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 511 */     paramGraphics.setColor(this.table.getGridColor());
/*     */ 
/* 513 */     Rectangle localRectangle1 = this.table.getCellRect(paramInt1, paramInt3, true);
/* 514 */     Rectangle localRectangle2 = this.table.getCellRect(paramInt2, paramInt4, true);
/* 515 */     Rectangle localRectangle3 = localRectangle1.union(localRectangle2);
/* 516 */     SynthGraphicsUtils localSynthGraphicsUtils = paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext);
/*     */     int j;
/*     */     int k;
/* 519 */     if (this.table.getShowHorizontalLines()) {
/* 520 */       int i = localRectangle3.x + localRectangle3.width;
/* 521 */       j = localRectangle3.y;
/* 522 */       for (k = paramInt1; k <= paramInt2; k++) {
/* 523 */         j += this.table.getRowHeight(k);
/* 524 */         localSynthGraphicsUtils.drawLine(paramSynthContext, "Table.grid", paramGraphics, localRectangle3.x, j - 1, i - 1, j - 1);
/*     */       }
/*     */     }
/*     */ 
/* 528 */     if (this.table.getShowVerticalLines()) {
/* 529 */       TableColumnModel localTableColumnModel = this.table.getColumnModel();
/* 530 */       j = localRectangle3.y + localRectangle3.height;
/*     */       int m;
/*     */       int n;
/* 532 */       if (this.table.getComponentOrientation().isLeftToRight()) {
/* 533 */         k = localRectangle3.x;
/* 534 */         for (m = paramInt3; m <= paramInt4; m++) {
/* 535 */           n = localTableColumnModel.getColumn(m).getWidth();
/* 536 */           k += n;
/* 537 */           localSynthGraphicsUtils.drawLine(paramSynthContext, "Table.grid", paramGraphics, k - 1, 0, k - 1, j - 1);
/*     */         }
/*     */       }
/*     */       else {
/* 541 */         k = localRectangle3.x;
/* 542 */         for (m = paramInt4; m >= paramInt3; m--) {
/* 543 */           n = localTableColumnModel.getColumn(m).getWidth();
/* 544 */           k += n;
/* 545 */           localSynthGraphicsUtils.drawLine(paramSynthContext, "Table.grid", paramGraphics, k - 1, 0, k - 1, j - 1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int viewIndexForColumn(TableColumn paramTableColumn)
/*     */   {
/* 553 */     TableColumnModel localTableColumnModel = this.table.getColumnModel();
/* 554 */     for (int i = 0; i < localTableColumnModel.getColumnCount(); i++) {
/* 555 */       if (localTableColumnModel.getColumn(i) == paramTableColumn) {
/* 556 */         return i;
/*     */       }
/*     */     }
/* 559 */     return -1;
/*     */   }
/*     */ 
/*     */   private void paintCells(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 564 */     JTableHeader localJTableHeader = this.table.getTableHeader();
/* 565 */     TableColumn localTableColumn1 = localJTableHeader == null ? null : localJTableHeader.getDraggedColumn();
/*     */ 
/* 567 */     TableColumnModel localTableColumnModel = this.table.getColumnModel();
/* 568 */     int i = localTableColumnModel.getColumnMargin();
/*     */     int k;
/*     */     Rectangle localRectangle;
/*     */     int m;
/*     */     TableColumn localTableColumn2;
/*     */     int j;
/* 573 */     if (this.table.getComponentOrientation().isLeftToRight())
/* 574 */       for (k = paramInt1; k <= paramInt2; k++) {
/* 575 */         localRectangle = this.table.getCellRect(k, paramInt3, false);
/* 576 */         for (m = paramInt3; m <= paramInt4; m++) {
/* 577 */           localTableColumn2 = localTableColumnModel.getColumn(m);
/* 578 */           j = localTableColumn2.getWidth();
/* 579 */           localRectangle.width = (j - i);
/* 580 */           if (localTableColumn2 != localTableColumn1) {
/* 581 */             paintCell(paramSynthContext, paramGraphics, localRectangle, k, m);
/*     */           }
/* 583 */           localRectangle.x += j;
/*     */         }
/*     */       }
/*     */     else {
/* 587 */       for (k = paramInt1; k <= paramInt2; k++) {
/* 588 */         localRectangle = this.table.getCellRect(k, paramInt3, false);
/* 589 */         localTableColumn2 = localTableColumnModel.getColumn(paramInt3);
/* 590 */         if (localTableColumn2 != localTableColumn1) {
/* 591 */           j = localTableColumn2.getWidth();
/* 592 */           localRectangle.width = (j - i);
/* 593 */           paintCell(paramSynthContext, paramGraphics, localRectangle, k, paramInt3);
/*     */         }
/* 595 */         for (m = paramInt3 + 1; m <= paramInt4; m++) {
/* 596 */           localTableColumn2 = localTableColumnModel.getColumn(m);
/* 597 */           j = localTableColumn2.getWidth();
/* 598 */           localRectangle.width = (j - i);
/* 599 */           localRectangle.x -= j;
/* 600 */           if (localTableColumn2 != localTableColumn1) {
/* 601 */             paintCell(paramSynthContext, paramGraphics, localRectangle, k, m);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 608 */     if (localTableColumn1 != null) {
/* 609 */       paintDraggedArea(paramSynthContext, paramGraphics, paramInt1, paramInt2, localTableColumn1, localJTableHeader.getDraggedDistance());
/*     */     }
/*     */ 
/* 613 */     this.rendererPane.removeAll();
/*     */   }
/*     */ 
/*     */   private void paintDraggedArea(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, TableColumn paramTableColumn, int paramInt3) {
/* 617 */     int i = viewIndexForColumn(paramTableColumn);
/*     */ 
/* 619 */     Rectangle localRectangle1 = this.table.getCellRect(paramInt1, i, true);
/* 620 */     Rectangle localRectangle2 = this.table.getCellRect(paramInt2, i, true);
/*     */ 
/* 622 */     Rectangle localRectangle3 = localRectangle1.union(localRectangle2);
/*     */ 
/* 625 */     paramGraphics.setColor(this.table.getParent().getBackground());
/* 626 */     paramGraphics.fillRect(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*     */ 
/* 630 */     localRectangle3.x += paramInt3;
/*     */ 
/* 633 */     paramGraphics.setColor(paramSynthContext.getStyle().getColor(paramSynthContext, ColorType.BACKGROUND));
/* 634 */     paramGraphics.fillRect(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*     */ 
/* 637 */     SynthGraphicsUtils localSynthGraphicsUtils = paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext);
/*     */     int n;
/* 642 */     if (this.table.getShowVerticalLines()) {
/* 643 */       paramGraphics.setColor(this.table.getGridColor());
/* 644 */       j = localRectangle3.x;
/* 645 */       int k = localRectangle3.y;
/* 646 */       int m = j + localRectangle3.width - 1;
/* 647 */       n = k + localRectangle3.height - 1;
/*     */ 
/* 649 */       localSynthGraphicsUtils.drawLine(paramSynthContext, "Table.grid", paramGraphics, j - 1, k, j - 1, n);
/*     */ 
/* 651 */       localSynthGraphicsUtils.drawLine(paramSynthContext, "Table.grid", paramGraphics, m, k, m, n);
/*     */     }
/*     */ 
/* 654 */     for (int j = paramInt1; j <= paramInt2; j++)
/*     */     {
/* 656 */       Rectangle localRectangle4 = this.table.getCellRect(j, i, false);
/* 657 */       localRectangle4.x += paramInt3;
/* 658 */       paintCell(paramSynthContext, paramGraphics, localRectangle4, j, i);
/*     */ 
/* 661 */       if (this.table.getShowHorizontalLines()) {
/* 662 */         paramGraphics.setColor(this.table.getGridColor());
/* 663 */         Rectangle localRectangle5 = this.table.getCellRect(j, i, true);
/* 664 */         localRectangle5.x += paramInt3;
/* 665 */         n = localRectangle5.x;
/* 666 */         int i1 = localRectangle5.y;
/* 667 */         int i2 = n + localRectangle5.width - 1;
/* 668 */         int i3 = i1 + localRectangle5.height - 1;
/* 669 */         localSynthGraphicsUtils.drawLine(paramSynthContext, "Table.grid", paramGraphics, n, i3, i2, i3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void paintCell(SynthContext paramSynthContext, Graphics paramGraphics, Rectangle paramRectangle, int paramInt1, int paramInt2)
/*     */   {
/*     */     Object localObject;
/* 676 */     if ((this.table.isEditing()) && (this.table.getEditingRow() == paramInt1) && (this.table.getEditingColumn() == paramInt2))
/*     */     {
/* 678 */       localObject = this.table.getEditorComponent();
/* 679 */       ((Component)localObject).setBounds(paramRectangle);
/* 680 */       ((Component)localObject).validate();
/*     */     }
/*     */     else {
/* 683 */       localObject = this.table.getCellRenderer(paramInt1, paramInt2);
/* 684 */       Component localComponent = this.table.prepareRenderer((TableCellRenderer)localObject, paramInt1, paramInt2);
/* 685 */       Color localColor = localComponent.getBackground();
/* 686 */       if (((localColor == null) || ((localColor instanceof UIResource)) || ((localComponent instanceof SynthBooleanTableCellRenderer))) && (!this.table.isCellSelected(paramInt1, paramInt2)))
/*     */       {
/* 689 */         if ((this.alternateColor != null) && (paramInt1 % 2 != 0)) {
/* 690 */           localComponent.setBackground(this.alternateColor);
/*     */         }
/*     */       }
/* 693 */       this.rendererPane.paintComponent(paramGraphics, localComponent, this.table, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 703 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 704 */       updateStyle((JTable)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ 
/*     */   private class SynthBooleanTableCellRenderer extends JCheckBox
/*     */     implements TableCellRenderer
/*     */   {
/*     */     private boolean isRowSelected;
/*     */ 
/*     */     public SynthBooleanTableCellRenderer()
/*     */     {
/* 714 */       setHorizontalAlignment(0);
/* 715 */       setName("Table.cellRenderer");
/*     */     }
/*     */ 
/*     */     public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*     */     {
/* 721 */       this.isRowSelected = paramBoolean1;
/*     */ 
/* 723 */       if (paramBoolean1) {
/* 724 */         setForeground(unwrap(paramJTable.getSelectionForeground()));
/* 725 */         setBackground(unwrap(paramJTable.getSelectionBackground()));
/*     */       } else {
/* 727 */         setForeground(unwrap(paramJTable.getForeground()));
/* 728 */         setBackground(unwrap(paramJTable.getBackground()));
/*     */       }
/*     */ 
/* 731 */       setSelected((paramObject != null) && (((Boolean)paramObject).booleanValue()));
/* 732 */       return this;
/*     */     }
/*     */ 
/*     */     private Color unwrap(Color paramColor) {
/* 736 */       if ((paramColor instanceof UIResource)) {
/* 737 */         return new Color(paramColor.getRGB());
/*     */       }
/* 739 */       return paramColor;
/*     */     }
/*     */ 
/*     */     public boolean isOpaque() {
/* 743 */       return this.isRowSelected ? true : super.isOpaque();
/*     */     }
/*     */   }
/*     */   private class SynthTableCellRenderer extends DefaultTableCellRenderer { private Object numberFormat;
/*     */     private Object dateFormat;
/*     */     private boolean opaque;
/*     */ 
/*     */     private SynthTableCellRenderer() {  } 
/* 753 */     public void setOpaque(boolean paramBoolean) { this.opaque = paramBoolean; }
/*     */ 
/*     */     public boolean isOpaque()
/*     */     {
/* 757 */       return this.opaque;
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 761 */       String str = super.getName();
/* 762 */       if (str == null) {
/* 763 */         return "Table.cellRenderer";
/*     */       }
/* 765 */       return str;
/*     */     }
/*     */ 
/*     */     public void setBorder(Border paramBorder) {
/* 769 */       if ((SynthTableUI.this.useUIBorder) || ((paramBorder instanceof SynthBorder)))
/* 770 */         super.setBorder(paramBorder);
/*     */     }
/*     */ 
/*     */     public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*     */     {
/* 777 */       if ((!SynthTableUI.this.useTableColors) && ((paramBoolean1) || (paramBoolean2))) {
/* 778 */         SynthLookAndFeel.setSelectedUI((SynthLabelUI)SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), paramBoolean1, paramBoolean2, paramJTable.isEnabled(), false);
/*     */       }
/*     */       else
/*     */       {
/* 783 */         SynthLookAndFeel.resetSelectedUI();
/*     */       }
/* 785 */       super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
/*     */ 
/* 788 */       setIcon(null);
/* 789 */       if (paramJTable != null) {
/* 790 */         configureValue(paramObject, paramJTable.getColumnClass(paramInt2));
/*     */       }
/* 792 */       return this;
/*     */     }
/*     */ 
/*     */     private void configureValue(Object paramObject, Class paramClass) {
/* 796 */       if ((paramClass == Object.class) || (paramClass == null)) {
/* 797 */         setHorizontalAlignment(10);
/* 798 */       } else if ((paramClass == Float.class) || (paramClass == Double.class)) {
/* 799 */         if (this.numberFormat == null) {
/* 800 */           this.numberFormat = NumberFormat.getInstance();
/*     */         }
/* 802 */         setHorizontalAlignment(11);
/* 803 */         setText(paramObject == null ? "" : ((NumberFormat)this.numberFormat).format(paramObject));
/*     */       }
/* 805 */       else if (paramClass == Number.class) {
/* 806 */         setHorizontalAlignment(11);
/*     */       }
/* 809 */       else if ((paramClass == Icon.class) || (paramClass == ImageIcon.class)) {
/* 810 */         setHorizontalAlignment(0);
/* 811 */         setIcon((paramObject instanceof Icon) ? (Icon)paramObject : null);
/* 812 */         setText("");
/*     */       }
/* 814 */       else if (paramClass == Date.class) {
/* 815 */         if (this.dateFormat == null) {
/* 816 */           this.dateFormat = DateFormat.getDateInstance();
/*     */         }
/* 818 */         setHorizontalAlignment(10);
/* 819 */         setText(paramObject == null ? "" : ((Format)this.dateFormat).format(paramObject));
/*     */       }
/*     */       else {
/* 822 */         configureValue(paramObject, paramClass.getSuperclass());
/*     */       }
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics) {
/* 827 */       super.paint(paramGraphics);
/* 828 */       SynthLookAndFeel.resetSelectedUI();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthTableUI
 * JD-Core Version:    0.6.2
 */