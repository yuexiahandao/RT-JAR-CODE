/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Enumeration;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.RowSorter;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.TableHeaderUI;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.SwingUtilities2;
/*     */ import sun.swing.UIAction;
/*     */ 
/*     */ public class BasicTableHeaderUI extends TableHeaderUI
/*     */ {
/*  46 */   private static Cursor resizeCursor = Cursor.getPredefinedCursor(11);
/*     */   protected JTableHeader header;
/*     */   protected CellRendererPane rendererPane;
/*     */   protected MouseInputListener mouseInputListener;
/*     */   private int rolloverColumn;
/*     */   private int selectedColumnIndex;
/*  65 */   private static FocusListener focusListener = new FocusListener() {
/*     */     public void focusGained(FocusEvent paramAnonymousFocusEvent) {
/*  67 */       repaintHeader(paramAnonymousFocusEvent.getSource());
/*     */     }
/*     */ 
/*     */     public void focusLost(FocusEvent paramAnonymousFocusEvent) {
/*  71 */       repaintHeader(paramAnonymousFocusEvent.getSource());
/*     */     }
/*     */ 
/*     */     private void repaintHeader(Object paramAnonymousObject) {
/*  75 */       if ((paramAnonymousObject instanceof JTableHeader)) {
/*  76 */         JTableHeader localJTableHeader = (JTableHeader)paramAnonymousObject;
/*  77 */         BasicTableHeaderUI localBasicTableHeaderUI = (BasicTableHeaderUI)BasicLookAndFeel.getUIOfType(localJTableHeader.getUI(), BasicTableHeaderUI.class);
/*     */ 
/*  81 */         if (localBasicTableHeaderUI == null) {
/*  82 */           return;
/*     */         }
/*     */ 
/*  85 */         localJTableHeader.repaint(localJTableHeader.getHeaderRect(localBasicTableHeaderUI.getSelectedColumnIndex()));
/*     */       }
/*     */     }
/*  65 */   };
/*     */ 
/*     */   public BasicTableHeaderUI()
/*     */   {
/*  60 */     this.rolloverColumn = -1;
/*     */ 
/*  63 */     this.selectedColumnIndex = 0;
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener()
/*     */   {
/* 306 */     return new MouseInputHandler();
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 314 */     return new BasicTableHeaderUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 320 */     this.header = ((JTableHeader)paramJComponent);
/*     */ 
/* 322 */     this.rendererPane = new CellRendererPane();
/* 323 */     this.header.add(this.rendererPane);
/*     */ 
/* 325 */     installDefaults();
/* 326 */     installListeners();
/* 327 */     installKeyboardActions();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 339 */     LookAndFeel.installColorsAndFont(this.header, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
/*     */ 
/* 341 */     LookAndFeel.installProperty(this.header, "opaque", Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 348 */     this.mouseInputListener = createMouseInputListener();
/*     */ 
/* 350 */     this.header.addMouseListener(this.mouseInputListener);
/* 351 */     this.header.addMouseMotionListener(this.mouseInputListener);
/* 352 */     this.header.addFocusListener(focusListener);
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions()
/*     */   {
/* 359 */     InputMap localInputMap = (InputMap)DefaultLookup.get(this.header, this, "TableHeader.ancestorInputMap");
/*     */ 
/* 361 */     SwingUtilities.replaceUIInputMap(this.header, 1, localInputMap);
/*     */ 
/* 363 */     LazyActionMap.installLazyActionMap(this.header, BasicTableHeaderUI.class, "TableHeader.actionMap");
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 370 */     uninstallDefaults();
/* 371 */     uninstallListeners();
/* 372 */     uninstallKeyboardActions();
/*     */ 
/* 374 */     this.header.remove(this.rendererPane);
/* 375 */     this.rendererPane = null;
/* 376 */     this.header = null;
/*     */   }
/*     */   protected void uninstallDefaults() {
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/* 382 */     this.header.removeMouseListener(this.mouseInputListener);
/* 383 */     this.header.removeMouseMotionListener(this.mouseInputListener);
/*     */ 
/* 385 */     this.mouseInputListener = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions()
/*     */   {
/* 392 */     SwingUtilities.replaceUIInputMap(this.header, 0, null);
/* 393 */     SwingUtilities.replaceUIActionMap(this.header, null);
/*     */   }
/*     */ 
/*     */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*     */   {
/* 400 */     paramLazyActionMap.put(new Actions("toggleSortOrder"));
/* 401 */     paramLazyActionMap.put(new Actions("selectColumnToLeft"));
/* 402 */     paramLazyActionMap.put(new Actions("selectColumnToRight"));
/* 403 */     paramLazyActionMap.put(new Actions("moveColumnLeft"));
/* 404 */     paramLazyActionMap.put(new Actions("moveColumnRight"));
/* 405 */     paramLazyActionMap.put(new Actions("resizeLeft"));
/* 406 */     paramLazyActionMap.put(new Actions("resizeRight"));
/* 407 */     paramLazyActionMap.put(new Actions("focusTable"));
/*     */   }
/*     */ 
/*     */   protected int getRolloverColumn()
/*     */   {
/* 424 */     return this.rolloverColumn;
/*     */   }
/*     */ 
/*     */   protected void rolloverColumnUpdated(int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void updateRolloverColumn(MouseEvent paramMouseEvent)
/*     */   {
/* 444 */     if ((this.header.getDraggedColumn() == null) && (this.header.contains(paramMouseEvent.getPoint())))
/*     */     {
/* 447 */       int i = this.header.columnAtPoint(paramMouseEvent.getPoint());
/* 448 */       if (i != this.rolloverColumn) {
/* 449 */         int j = this.rolloverColumn;
/* 450 */         this.rolloverColumn = i;
/* 451 */         rolloverColumnUpdated(j, this.rolloverColumn);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int selectNextColumn(boolean paramBoolean)
/*     */   {
/* 460 */     int i = getSelectedColumnIndex();
/* 461 */     if (i < this.header.getColumnModel().getColumnCount() - 1) {
/* 462 */       i++;
/* 463 */       if (paramBoolean) {
/* 464 */         selectColumn(i);
/*     */       }
/*     */     }
/* 467 */     return i;
/*     */   }
/*     */ 
/*     */   private int selectPreviousColumn(boolean paramBoolean) {
/* 471 */     int i = getSelectedColumnIndex();
/* 472 */     if (i > 0) {
/* 473 */       i--;
/* 474 */       if (paramBoolean) {
/* 475 */         selectColumn(i);
/*     */       }
/*     */     }
/* 478 */     return i;
/*     */   }
/*     */ 
/*     */   void selectColumn(int paramInt)
/*     */   {
/* 486 */     selectColumn(paramInt, true);
/*     */   }
/*     */ 
/*     */   void selectColumn(int paramInt, boolean paramBoolean) {
/* 490 */     Rectangle localRectangle = this.header.getHeaderRect(this.selectedColumnIndex);
/* 491 */     this.header.repaint(localRectangle);
/* 492 */     this.selectedColumnIndex = paramInt;
/* 493 */     localRectangle = this.header.getHeaderRect(paramInt);
/* 494 */     this.header.repaint(localRectangle);
/* 495 */     if (paramBoolean)
/* 496 */       scrollToColumn(paramInt);
/*     */   }
/*     */ 
/*     */   private void scrollToColumn(int paramInt)
/*     */   {
/*     */     Container localContainer;
/*     */     JTable localJTable;
/* 509 */     if ((this.header.getParent() == null) || ((localContainer = this.header.getParent().getParent()) == null) || (!(localContainer instanceof JScrollPane)) || ((localJTable = this.header.getTable()) == null))
/*     */     {
/* 513 */       return;
/*     */     }
/*     */ 
/* 517 */     Rectangle localRectangle1 = localJTable.getVisibleRect();
/* 518 */     Rectangle localRectangle2 = localJTable.getCellRect(0, paramInt, true);
/* 519 */     localRectangle1.x = localRectangle2.x;
/* 520 */     localRectangle1.width = localRectangle2.width;
/* 521 */     localJTable.scrollRectToVisible(localRectangle1);
/*     */   }
/*     */ 
/*     */   private int getSelectedColumnIndex() {
/* 525 */     int i = this.header.getColumnModel().getColumnCount();
/* 526 */     if ((this.selectedColumnIndex >= i) && (i > 0)) {
/* 527 */       this.selectedColumnIndex = (i - 1);
/*     */     }
/* 529 */     return this.selectedColumnIndex;
/*     */   }
/*     */ 
/*     */   private static boolean canResize(TableColumn paramTableColumn, JTableHeader paramJTableHeader)
/*     */   {
/* 534 */     return (paramTableColumn != null) && (paramJTableHeader.getResizingAllowed()) && (paramTableColumn.getResizable());
/*     */   }
/*     */ 
/*     */   private int changeColumnWidth(TableColumn paramTableColumn, JTableHeader paramJTableHeader, int paramInt1, int paramInt2)
/*     */   {
/* 541 */     paramTableColumn.setWidth(paramInt2);
/*     */     Container localContainer;
/*     */     JTable localJTable;
/* 546 */     if ((paramJTableHeader.getParent() == null) || ((localContainer = paramJTableHeader.getParent().getParent()) == null) || (!(localContainer instanceof JScrollPane)) || ((localJTable = paramJTableHeader.getTable()) == null))
/*     */     {
/* 550 */       return 0;
/*     */     }
/*     */ 
/* 553 */     if ((!localContainer.getComponentOrientation().isLeftToRight()) && (!paramJTableHeader.getComponentOrientation().isLeftToRight()))
/*     */     {
/* 555 */       JViewport localJViewport = ((JScrollPane)localContainer).getViewport();
/* 556 */       int i = localJViewport.getWidth();
/* 557 */       int j = paramInt2 - paramInt1;
/* 558 */       int k = localJTable.getWidth() + j;
/*     */ 
/* 561 */       Dimension localDimension = localJTable.getSize();
/* 562 */       localDimension.width += j;
/* 563 */       localJTable.setSize(localDimension);
/*     */ 
/* 569 */       if ((k >= i) && (localJTable.getAutoResizeMode() == 0))
/*     */       {
/* 571 */         Point localPoint = localJViewport.getViewPosition();
/* 572 */         localPoint.x = Math.max(0, Math.min(k - i, localPoint.x + j));
/*     */ 
/* 574 */         localJViewport.setViewPosition(localPoint);
/* 575 */         return j;
/*     */       }
/*     */     }
/* 578 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 594 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 595 */     int i = -1;
/* 596 */     TableColumnModel localTableColumnModel = this.header.getColumnModel();
/* 597 */     for (int j = 0; j < localTableColumnModel.getColumnCount(); 
/* 598 */       j++) {
/* 599 */       TableColumn localTableColumn = localTableColumnModel.getColumn(j);
/* 600 */       Component localComponent = getHeaderRenderer(j);
/* 601 */       Dimension localDimension = localComponent.getPreferredSize();
/* 602 */       int k = localComponent.getBaseline(localDimension.width, paramInt2);
/* 603 */       if (k >= 0) {
/* 604 */         if (i == -1) {
/* 605 */           i = k;
/*     */         }
/* 607 */         else if (i != k) {
/* 608 */           i = -1;
/* 609 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 613 */     return i;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 621 */     if (this.header.getColumnModel().getColumnCount() <= 0) {
/* 622 */       return;
/*     */     }
/* 624 */     boolean bool = this.header.getComponentOrientation().isLeftToRight();
/*     */ 
/* 626 */     Rectangle localRectangle1 = paramGraphics.getClipBounds();
/* 627 */     Point localPoint1 = localRectangle1.getLocation();
/* 628 */     Point localPoint2 = new Point(localRectangle1.x + localRectangle1.width - 1, localRectangle1.y);
/* 629 */     TableColumnModel localTableColumnModel = this.header.getColumnModel();
/* 630 */     int i = this.header.columnAtPoint(bool ? localPoint1 : localPoint2);
/* 631 */     int j = this.header.columnAtPoint(bool ? localPoint2 : localPoint1);
/*     */ 
/* 633 */     if (i == -1) {
/* 634 */       i = 0;
/*     */     }
/*     */ 
/* 638 */     if (j == -1) {
/* 639 */       j = localTableColumnModel.getColumnCount() - 1;
/*     */     }
/*     */ 
/* 642 */     TableColumn localTableColumn1 = this.header.getDraggedColumn();
/*     */ 
/* 644 */     Rectangle localRectangle2 = this.header.getHeaderRect(bool ? i : j);
/*     */     int m;
/*     */     TableColumn localTableColumn2;
/*     */     int k;
/* 646 */     if (bool)
/* 647 */       for (m = i; m <= j; m++) {
/* 648 */         localTableColumn2 = localTableColumnModel.getColumn(m);
/* 649 */         k = localTableColumn2.getWidth();
/* 650 */         localRectangle2.width = k;
/* 651 */         if (localTableColumn2 != localTableColumn1) {
/* 652 */           paintCell(paramGraphics, localRectangle2, m);
/*     */         }
/* 654 */         localRectangle2.x += k;
/*     */       }
/*     */     else {
/* 657 */       for (m = j; m >= i; m--) {
/* 658 */         localTableColumn2 = localTableColumnModel.getColumn(m);
/* 659 */         k = localTableColumn2.getWidth();
/* 660 */         localRectangle2.width = k;
/* 661 */         if (localTableColumn2 != localTableColumn1) {
/* 662 */           paintCell(paramGraphics, localRectangle2, m);
/*     */         }
/* 664 */         localRectangle2.x += k;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 669 */     if (localTableColumn1 != null) {
/* 670 */       m = viewIndexForColumn(localTableColumn1);
/* 671 */       Rectangle localRectangle3 = this.header.getHeaderRect(m);
/*     */ 
/* 674 */       paramGraphics.setColor(this.header.getParent().getBackground());
/* 675 */       paramGraphics.fillRect(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*     */ 
/* 678 */       localRectangle3.x += this.header.getDraggedDistance();
/*     */ 
/* 681 */       paramGraphics.setColor(this.header.getBackground());
/* 682 */       paramGraphics.fillRect(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*     */ 
/* 685 */       paintCell(paramGraphics, localRectangle3, m);
/*     */     }
/*     */ 
/* 689 */     this.rendererPane.removeAll();
/*     */   }
/*     */ 
/*     */   private Component getHeaderRenderer(int paramInt) {
/* 693 */     TableColumn localTableColumn = this.header.getColumnModel().getColumn(paramInt);
/* 694 */     TableCellRenderer localTableCellRenderer = localTableColumn.getHeaderRenderer();
/* 695 */     if (localTableCellRenderer == null) {
/* 696 */       localTableCellRenderer = this.header.getDefaultRenderer();
/*     */     }
/*     */ 
/* 699 */     boolean bool = (!this.header.isPaintingForPrint()) && (paramInt == getSelectedColumnIndex()) && (this.header.hasFocus());
/*     */ 
/* 702 */     return localTableCellRenderer.getTableCellRendererComponent(this.header.getTable(), localTableColumn.getHeaderValue(), false, bool, -1, paramInt);
/*     */   }
/*     */ 
/*     */   private void paintCell(Graphics paramGraphics, Rectangle paramRectangle, int paramInt)
/*     */   {
/* 709 */     Component localComponent = getHeaderRenderer(paramInt);
/* 710 */     this.rendererPane.paintComponent(paramGraphics, localComponent, this.header, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, true);
/*     */   }
/*     */ 
/*     */   private int viewIndexForColumn(TableColumn paramTableColumn)
/*     */   {
/* 715 */     TableColumnModel localTableColumnModel = this.header.getColumnModel();
/* 716 */     for (int i = 0; i < localTableColumnModel.getColumnCount(); i++) {
/* 717 */       if (localTableColumnModel.getColumn(i) == paramTableColumn) {
/* 718 */         return i;
/*     */       }
/*     */     }
/* 721 */     return -1;
/*     */   }
/*     */ 
/*     */   private int getHeaderHeight()
/*     */   {
/* 729 */     int i = 0;
/* 730 */     int j = 0;
/* 731 */     TableColumnModel localTableColumnModel = this.header.getColumnModel();
/* 732 */     for (int k = 0; k < localTableColumnModel.getColumnCount(); k++) {
/* 733 */       TableColumn localTableColumn = localTableColumnModel.getColumn(k);
/* 734 */       int m = localTableColumn.getHeaderRenderer() == null ? 1 : 0;
/*     */ 
/* 736 */       if ((m == 0) || (j == 0)) {
/* 737 */         Component localComponent = getHeaderRenderer(k);
/* 738 */         int n = localComponent.getPreferredSize().height;
/* 739 */         i = Math.max(i, n);
/*     */ 
/* 745 */         if ((m != 0) && (n > 0)) {
/* 746 */           Object localObject = localTableColumn.getHeaderValue();
/* 747 */           if (localObject != null) {
/* 748 */             localObject = localObject.toString();
/*     */ 
/* 750 */             if ((localObject != null) && (!localObject.equals(""))) {
/* 751 */               j = 1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 757 */     return i;
/*     */   }
/*     */ 
/*     */   private Dimension createHeaderSize(long paramLong)
/*     */   {
/* 762 */     if (paramLong > 2147483647L) {
/* 763 */       paramLong = 2147483647L;
/*     */     }
/* 765 */     return new Dimension((int)paramLong, getHeaderHeight());
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 774 */     long l = 0L;
/* 775 */     Enumeration localEnumeration = this.header.getColumnModel().getColumns();
/* 776 */     while (localEnumeration.hasMoreElements()) {
/* 777 */       TableColumn localTableColumn = (TableColumn)localEnumeration.nextElement();
/* 778 */       l += localTableColumn.getMinWidth();
/*     */     }
/* 780 */     return createHeaderSize(l);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 790 */     long l = 0L;
/* 791 */     Enumeration localEnumeration = this.header.getColumnModel().getColumns();
/* 792 */     while (localEnumeration.hasMoreElements()) {
/* 793 */       TableColumn localTableColumn = (TableColumn)localEnumeration.nextElement();
/* 794 */       l += localTableColumn.getPreferredWidth();
/*     */     }
/* 796 */     return createHeaderSize(l);
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 804 */     long l = 0L;
/* 805 */     Enumeration localEnumeration = this.header.getColumnModel().getColumns();
/* 806 */     while (localEnumeration.hasMoreElements()) {
/* 807 */       TableColumn localTableColumn = (TableColumn)localEnumeration.nextElement();
/* 808 */       l += localTableColumn.getMaxWidth();
/*     */     }
/* 810 */     return createHeaderSize(l);
/*     */   }
/*     */ 
/*     */   private static class Actions extends UIAction
/*     */   {
/*     */     public static final String TOGGLE_SORT_ORDER = "toggleSortOrder";
/*     */     public static final String SELECT_COLUMN_TO_LEFT = "selectColumnToLeft";
/*     */     public static final String SELECT_COLUMN_TO_RIGHT = "selectColumnToRight";
/*     */     public static final String MOVE_COLUMN_LEFT = "moveColumnLeft";
/*     */     public static final String MOVE_COLUMN_RIGHT = "moveColumnRight";
/*     */     public static final String RESIZE_LEFT = "resizeLeft";
/*     */     public static final String RESIZE_RIGHT = "resizeRight";
/*     */     public static final String FOCUS_TABLE = "focusTable";
/*     */ 
/*     */     public Actions(String paramString) {
/* 832 */       super();
/*     */     }
/*     */ 
/*     */     public boolean isEnabled(Object paramObject) {
/* 836 */       if ((paramObject instanceof JTableHeader)) {
/* 837 */         JTableHeader localJTableHeader = (JTableHeader)paramObject;
/* 838 */         TableColumnModel localTableColumnModel = localJTableHeader.getColumnModel();
/* 839 */         if (localTableColumnModel.getColumnCount() <= 0) {
/* 840 */           return false;
/*     */         }
/*     */ 
/* 843 */         String str = getName();
/* 844 */         BasicTableHeaderUI localBasicTableHeaderUI = (BasicTableHeaderUI)BasicLookAndFeel.getUIOfType(localJTableHeader.getUI(), BasicTableHeaderUI.class);
/*     */ 
/* 847 */         if (localBasicTableHeaderUI != null) {
/* 848 */           if (str == "moveColumnLeft") {
/* 849 */             return (localJTableHeader.getReorderingAllowed()) && (maybeMoveColumn(true, localJTableHeader, localBasicTableHeaderUI, false));
/*     */           }
/* 851 */           if (str == "moveColumnRight") {
/* 852 */             return (localJTableHeader.getReorderingAllowed()) && (maybeMoveColumn(false, localJTableHeader, localBasicTableHeaderUI, false));
/*     */           }
/* 854 */           if ((str == "resizeLeft") || (str == "resizeRight"))
/*     */           {
/* 856 */             return BasicTableHeaderUI.canResize(localTableColumnModel.getColumn(BasicTableHeaderUI.access$000(localBasicTableHeaderUI)), localJTableHeader);
/* 857 */           }if (str == "focusTable") {
/* 858 */             return localJTableHeader.getTable() != null;
/*     */           }
/*     */         }
/*     */       }
/* 862 */       return true;
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 866 */       JTableHeader localJTableHeader = (JTableHeader)paramActionEvent.getSource();
/* 867 */       BasicTableHeaderUI localBasicTableHeaderUI = (BasicTableHeaderUI)BasicLookAndFeel.getUIOfType(localJTableHeader.getUI(), BasicTableHeaderUI.class);
/*     */ 
/* 871 */       if (localBasicTableHeaderUI == null) {
/* 872 */         return;
/*     */       }
/*     */ 
/* 875 */       String str = getName();
/*     */       JTable localJTable;
/* 876 */       if ("toggleSortOrder" == str) {
/* 877 */         localJTable = localJTableHeader.getTable();
/* 878 */         RowSorter localRowSorter = localJTable == null ? null : localJTable.getRowSorter();
/* 879 */         if (localRowSorter != null) {
/* 880 */           int i = localBasicTableHeaderUI.getSelectedColumnIndex();
/* 881 */           i = localJTable.convertColumnIndexToModel(i);
/*     */ 
/* 883 */           localRowSorter.toggleSortOrder(i);
/*     */         }
/* 885 */       } else if ("selectColumnToLeft" == str) {
/* 886 */         if (localJTableHeader.getComponentOrientation().isLeftToRight())
/* 887 */           localBasicTableHeaderUI.selectPreviousColumn(true);
/*     */         else
/* 889 */           localBasicTableHeaderUI.selectNextColumn(true);
/*     */       }
/* 891 */       else if ("selectColumnToRight" == str) {
/* 892 */         if (localJTableHeader.getComponentOrientation().isLeftToRight())
/* 893 */           localBasicTableHeaderUI.selectNextColumn(true);
/*     */         else
/* 895 */           localBasicTableHeaderUI.selectPreviousColumn(true);
/*     */       }
/* 897 */       else if ("moveColumnLeft" == str) {
/* 898 */         moveColumn(true, localJTableHeader, localBasicTableHeaderUI);
/* 899 */       } else if ("moveColumnRight" == str) {
/* 900 */         moveColumn(false, localJTableHeader, localBasicTableHeaderUI);
/* 901 */       } else if ("resizeLeft" == str) {
/* 902 */         resize(true, localJTableHeader, localBasicTableHeaderUI);
/* 903 */       } else if ("resizeRight" == str) {
/* 904 */         resize(false, localJTableHeader, localBasicTableHeaderUI);
/* 905 */       } else if ("focusTable" == str) {
/* 906 */         localJTable = localJTableHeader.getTable();
/* 907 */         if (localJTable != null)
/* 908 */           localJTable.requestFocusInWindow();
/*     */       }
/*     */     }
/*     */ 
/*     */     private void moveColumn(boolean paramBoolean, JTableHeader paramJTableHeader, BasicTableHeaderUI paramBasicTableHeaderUI)
/*     */     {
/* 915 */       maybeMoveColumn(paramBoolean, paramJTableHeader, paramBasicTableHeaderUI, true);
/*     */     }
/*     */ 
/*     */     private boolean maybeMoveColumn(boolean paramBoolean1, JTableHeader paramJTableHeader, BasicTableHeaderUI paramBasicTableHeaderUI, boolean paramBoolean2)
/*     */     {
/* 920 */       int i = paramBasicTableHeaderUI.getSelectedColumnIndex();
/*     */       int j;
/* 923 */       if (paramJTableHeader.getComponentOrientation().isLeftToRight()) {
/* 924 */         j = paramBoolean1 ? paramBasicTableHeaderUI.selectPreviousColumn(paramBoolean2) : paramBasicTableHeaderUI.selectNextColumn(paramBoolean2);
/*     */       }
/*     */       else {
/* 927 */         j = paramBoolean1 ? paramBasicTableHeaderUI.selectNextColumn(paramBoolean2) : paramBasicTableHeaderUI.selectPreviousColumn(paramBoolean2);
/*     */       }
/*     */ 
/* 931 */       if (j != i) {
/* 932 */         if (paramBoolean2)
/* 933 */           paramJTableHeader.getColumnModel().moveColumn(i, j);
/*     */         else {
/* 935 */           return true;
/*     */         }
/*     */       }
/*     */ 
/* 939 */       return false;
/*     */     }
/*     */ 
/*     */     private void resize(boolean paramBoolean, JTableHeader paramJTableHeader, BasicTableHeaderUI paramBasicTableHeaderUI)
/*     */     {
/* 944 */       int i = paramBasicTableHeaderUI.getSelectedColumnIndex();
/* 945 */       TableColumn localTableColumn = paramJTableHeader.getColumnModel().getColumn(i);
/*     */ 
/* 948 */       paramJTableHeader.setResizingColumn(localTableColumn);
/* 949 */       int j = localTableColumn.getWidth();
/* 950 */       int k = j;
/*     */ 
/* 952 */       if (paramJTableHeader.getComponentOrientation().isLeftToRight())
/* 953 */         k += (paramBoolean ? -1 : 1);
/*     */       else {
/* 955 */         k += (paramBoolean ? 1 : -1);
/*     */       }
/*     */ 
/* 958 */       paramBasicTableHeaderUI.changeColumnWidth(localTableColumn, paramJTableHeader, j, k);
/*     */     }
/*     */   }
/*     */ 
/*     */   public class MouseInputHandler
/*     */     implements MouseInputListener
/*     */   {
/*     */     private int mouseXOffset;
/*  97 */     private Cursor otherCursor = BasicTableHeaderUI.resizeCursor;
/*     */ 
/*     */     public MouseInputHandler() {  } 
/* 100 */     public void mouseClicked(MouseEvent paramMouseEvent) { if (!BasicTableHeaderUI.this.header.isEnabled()) {
/* 101 */         return;
/*     */       }
/* 103 */       if ((paramMouseEvent.getClickCount() % 2 == 1) && (SwingUtilities.isLeftMouseButton(paramMouseEvent)))
/*     */       {
/* 105 */         JTable localJTable = BasicTableHeaderUI.this.header.getTable();
/*     */         RowSorter localRowSorter;
/* 107 */         if ((localJTable != null) && ((localRowSorter = localJTable.getRowSorter()) != null)) {
/* 108 */           int i = BasicTableHeaderUI.this.header.columnAtPoint(paramMouseEvent.getPoint());
/* 109 */           if (i != -1) {
/* 110 */             i = localJTable.convertColumnIndexToModel(i);
/*     */ 
/* 112 */             localRowSorter.toggleSortOrder(i);
/*     */           }
/*     */         }
/*     */       } }
/*     */ 
/*     */     private TableColumn getResizingColumn(Point paramPoint)
/*     */     {
/* 119 */       return getResizingColumn(paramPoint, BasicTableHeaderUI.this.header.columnAtPoint(paramPoint));
/*     */     }
/*     */ 
/*     */     private TableColumn getResizingColumn(Point paramPoint, int paramInt) {
/* 123 */       if (paramInt == -1) {
/* 124 */         return null;
/*     */       }
/* 126 */       Rectangle localRectangle = BasicTableHeaderUI.this.header.getHeaderRect(paramInt);
/* 127 */       localRectangle.grow(-3, 0);
/* 128 */       if (localRectangle.contains(paramPoint)) {
/* 129 */         return null;
/*     */       }
/* 131 */       int i = localRectangle.x + localRectangle.width / 2;
/*     */       int j;
/* 133 */       if (BasicTableHeaderUI.this.header.getComponentOrientation().isLeftToRight())
/* 134 */         j = paramPoint.x < i ? paramInt - 1 : paramInt;
/*     */       else {
/* 136 */         j = paramPoint.x < i ? paramInt : paramInt - 1;
/*     */       }
/* 138 */       if (j == -1) {
/* 139 */         return null;
/*     */       }
/* 141 */       return BasicTableHeaderUI.this.header.getColumnModel().getColumn(j);
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 145 */       if (!BasicTableHeaderUI.this.header.isEnabled()) {
/* 146 */         return;
/*     */       }
/* 148 */       BasicTableHeaderUI.this.header.setDraggedColumn(null);
/* 149 */       BasicTableHeaderUI.this.header.setResizingColumn(null);
/* 150 */       BasicTableHeaderUI.this.header.setDraggedDistance(0);
/*     */ 
/* 152 */       Point localPoint = paramMouseEvent.getPoint();
/*     */ 
/* 155 */       TableColumnModel localTableColumnModel = BasicTableHeaderUI.this.header.getColumnModel();
/* 156 */       int i = BasicTableHeaderUI.this.header.columnAtPoint(localPoint);
/*     */ 
/* 158 */       if (i != -1)
/*     */       {
/* 160 */         TableColumn localTableColumn1 = getResizingColumn(localPoint, i);
/* 161 */         if (BasicTableHeaderUI.canResize(localTableColumn1, BasicTableHeaderUI.this.header)) {
/* 162 */           BasicTableHeaderUI.this.header.setResizingColumn(localTableColumn1);
/* 163 */           if (BasicTableHeaderUI.this.header.getComponentOrientation().isLeftToRight())
/* 164 */             this.mouseXOffset = (localPoint.x - localTableColumn1.getWidth());
/*     */           else {
/* 166 */             this.mouseXOffset = (localPoint.x + localTableColumn1.getWidth());
/*     */           }
/*     */         }
/* 169 */         else if (BasicTableHeaderUI.this.header.getReorderingAllowed()) {
/* 170 */           TableColumn localTableColumn2 = localTableColumnModel.getColumn(i);
/* 171 */           BasicTableHeaderUI.this.header.setDraggedColumn(localTableColumn2);
/* 172 */           this.mouseXOffset = localPoint.x;
/*     */         }
/*     */       }
/*     */ 
/* 176 */       if (BasicTableHeaderUI.this.header.getReorderingAllowed()) {
/* 177 */         int j = BasicTableHeaderUI.this.rolloverColumn;
/* 178 */         BasicTableHeaderUI.this.rolloverColumn = -1;
/* 179 */         BasicTableHeaderUI.this.rolloverColumnUpdated(j, BasicTableHeaderUI.this.rolloverColumn);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void swapCursor() {
/* 184 */       Cursor localCursor = BasicTableHeaderUI.this.header.getCursor();
/* 185 */       BasicTableHeaderUI.this.header.setCursor(this.otherCursor);
/* 186 */       this.otherCursor = localCursor;
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 190 */       if (!BasicTableHeaderUI.this.header.isEnabled()) {
/* 191 */         return;
/*     */       }
/* 193 */       if (BasicTableHeaderUI.canResize(getResizingColumn(paramMouseEvent.getPoint()), BasicTableHeaderUI.this.header) != (BasicTableHeaderUI.this.header.getCursor() == BasicTableHeaderUI.resizeCursor))
/*     */       {
/* 195 */         swapCursor();
/*     */       }
/* 197 */       BasicTableHeaderUI.this.updateRolloverColumn(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 201 */       if (!BasicTableHeaderUI.this.header.isEnabled()) {
/* 202 */         return;
/*     */       }
/* 204 */       int i = paramMouseEvent.getX();
/*     */ 
/* 206 */       TableColumn localTableColumn1 = BasicTableHeaderUI.this.header.getResizingColumn();
/* 207 */       TableColumn localTableColumn2 = BasicTableHeaderUI.this.header.getDraggedColumn();
/*     */ 
/* 209 */       boolean bool = BasicTableHeaderUI.this.header.getComponentOrientation().isLeftToRight();
/*     */       int k;
/* 211 */       if (localTableColumn1 != null) {
/* 212 */         int j = localTableColumn1.getWidth();
/*     */ 
/* 214 */         if (bool)
/* 215 */           k = i - this.mouseXOffset;
/*     */         else {
/* 217 */           k = this.mouseXOffset - i;
/*     */         }
/* 219 */         this.mouseXOffset += BasicTableHeaderUI.this.changeColumnWidth(localTableColumn1, BasicTableHeaderUI.this.header, j, k);
/*     */       }
/* 222 */       else if (localTableColumn2 != null) {
/* 223 */         TableColumnModel localTableColumnModel = BasicTableHeaderUI.this.header.getColumnModel();
/* 224 */         k = i - this.mouseXOffset;
/* 225 */         int m = k < 0 ? -1 : 1;
/* 226 */         int n = BasicTableHeaderUI.this.viewIndexForColumn(localTableColumn2);
/* 227 */         int i1 = n + (bool ? m : -m);
/* 228 */         if ((0 <= i1) && (i1 < localTableColumnModel.getColumnCount())) {
/* 229 */           int i2 = localTableColumnModel.getColumn(i1).getWidth();
/* 230 */           if (Math.abs(k) > i2 / 2)
/*     */           {
/* 232 */             this.mouseXOffset += m * i2;
/* 233 */             BasicTableHeaderUI.this.header.setDraggedDistance(k - m * i2);
/*     */ 
/* 236 */             int i3 = SwingUtilities2.convertColumnIndexToModel(BasicTableHeaderUI.this.header.getColumnModel(), BasicTableHeaderUI.this.getSelectedColumnIndex());
/*     */ 
/* 242 */             localTableColumnModel.moveColumn(n, i1);
/*     */ 
/* 245 */             BasicTableHeaderUI.this.selectColumn(SwingUtilities2.convertColumnIndexToView(BasicTableHeaderUI.this.header.getColumnModel(), i3), false);
/*     */ 
/* 250 */             return;
/*     */           }
/*     */         }
/* 253 */         setDraggedDistance(k, n);
/*     */       }
/*     */ 
/* 256 */       BasicTableHeaderUI.this.updateRolloverColumn(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 260 */       if (!BasicTableHeaderUI.this.header.isEnabled()) {
/* 261 */         return;
/*     */       }
/* 263 */       setDraggedDistance(0, BasicTableHeaderUI.this.viewIndexForColumn(BasicTableHeaderUI.this.header.getDraggedColumn()));
/*     */ 
/* 265 */       BasicTableHeaderUI.this.header.setResizingColumn(null);
/* 266 */       BasicTableHeaderUI.this.header.setDraggedColumn(null);
/*     */ 
/* 268 */       BasicTableHeaderUI.this.updateRolloverColumn(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/* 272 */       if (!BasicTableHeaderUI.this.header.isEnabled()) {
/* 273 */         return;
/*     */       }
/* 275 */       BasicTableHeaderUI.this.updateRolloverColumn(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 279 */       if (!BasicTableHeaderUI.this.header.isEnabled()) {
/* 280 */         return;
/*     */       }
/* 282 */       int i = BasicTableHeaderUI.this.rolloverColumn;
/* 283 */       BasicTableHeaderUI.this.rolloverColumn = -1;
/* 284 */       BasicTableHeaderUI.this.rolloverColumnUpdated(i, BasicTableHeaderUI.this.rolloverColumn);
/*     */     }
/*     */ 
/*     */     private void setDraggedDistance(int paramInt1, int paramInt2)
/*     */     {
/* 291 */       BasicTableHeaderUI.this.header.setDraggedDistance(paramInt1);
/* 292 */       if (paramInt2 != -1)
/* 293 */         BasicTableHeaderUI.this.header.getColumnModel().moveColumn(paramInt2, paramInt2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTableHeaderUI
 * JD-Core Version:    0.6.2
 */