/*      */ package javax.swing;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterAbortException;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.text.DateFormat;
/*      */ import java.text.MessageFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventObject;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleAction;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleExtendedTable;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleTable;
/*      */ import javax.accessibility.AccessibleTableModelChange;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.print.PrintService;
/*      */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintRequestAttributeSet;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.border.LineBorder;
/*      */ import javax.swing.event.CellEditorListener;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.event.RowSorterEvent;
/*      */ import javax.swing.event.RowSorterEvent.Type;
/*      */ import javax.swing.event.RowSorterListener;
/*      */ import javax.swing.event.TableColumnModelEvent;
/*      */ import javax.swing.event.TableColumnModelListener;
/*      */ import javax.swing.event.TableModelEvent;
/*      */ import javax.swing.event.TableModelListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TableUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.table.AbstractTableModel;
/*      */ import javax.swing.table.DefaultTableCellRenderer.UIResource;
/*      */ import javax.swing.table.DefaultTableColumnModel;
/*      */ import javax.swing.table.DefaultTableModel;
/*      */ import javax.swing.table.JTableHeader;
/*      */ import javax.swing.table.TableCellEditor;
/*      */ import javax.swing.table.TableCellRenderer;
/*      */ import javax.swing.table.TableColumn;
/*      */ import javax.swing.table.TableColumnModel;
/*      */ import javax.swing.table.TableModel;
/*      */ import javax.swing.table.TableRowSorter;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.swing.PrintingStatus;
/*      */ import sun.swing.SwingLazyValue;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.SwingUtilities2.Section;
/*      */ 
/*      */ public class JTable extends JComponent
/*      */   implements TableModelListener, Scrollable, TableColumnModelListener, ListSelectionListener, CellEditorListener, Accessible, RowSorterListener
/*      */ {
/*      */   private static final String uiClassID = "TableUI";
/*      */   public static final int AUTO_RESIZE_OFF = 0;
/*      */   public static final int AUTO_RESIZE_NEXT_COLUMN = 1;
/*      */   public static final int AUTO_RESIZE_SUBSEQUENT_COLUMNS = 2;
/*      */   public static final int AUTO_RESIZE_LAST_COLUMN = 3;
/*      */   public static final int AUTO_RESIZE_ALL_COLUMNS = 4;
/*      */   protected TableModel dataModel;
/*      */   protected TableColumnModel columnModel;
/*      */   protected ListSelectionModel selectionModel;
/*      */   protected JTableHeader tableHeader;
/*      */   protected int rowHeight;
/*      */   protected int rowMargin;
/*      */   protected Color gridColor;
/*      */   protected boolean showHorizontalLines;
/*      */   protected boolean showVerticalLines;
/*      */   protected int autoResizeMode;
/*      */   protected boolean autoCreateColumnsFromModel;
/*      */   protected Dimension preferredViewportSize;
/*      */   protected boolean rowSelectionAllowed;
/*      */   protected boolean cellSelectionEnabled;
/*      */   protected transient Component editorComp;
/*      */   protected transient TableCellEditor cellEditor;
/*      */   protected transient int editingColumn;
/*      */   protected transient int editingRow;
/*      */   protected transient Hashtable defaultRenderersByColumnClass;
/*      */   protected transient Hashtable defaultEditorsByColumnClass;
/*      */   protected Color selectionForeground;
/*      */   protected Color selectionBackground;
/*      */   private SizeSequence rowModel;
/*      */   private boolean dragEnabled;
/*      */   private boolean surrendersFocusOnKeystroke;
/*  386 */   private PropertyChangeListener editorRemover = null;
/*      */   private boolean columnSelectionAdjusting;
/*      */   private boolean rowSelectionAdjusting;
/*      */   private Throwable printError;
/*      */   private boolean isRowHeightSet;
/*      */   private boolean updateSelectionOnSort;
/*      */   private transient SortManager sortManager;
/*      */   private boolean ignoreSortChange;
/*      */   private boolean sorterChanged;
/*      */   private boolean autoCreateRowSorter;
/*      */   private boolean fillsViewportHeight;
/*  444 */   private DropMode dropMode = DropMode.USE_SELECTION;
/*      */   private transient DropLocation dropLocation;
/*      */ 
/*      */   public JTable()
/*      */   {
/*  562 */     this(null, null, null);
/*      */   }
/*      */ 
/*      */   public JTable(TableModel paramTableModel)
/*      */   {
/*  575 */     this(paramTableModel, null, null);
/*      */   }
/*      */ 
/*      */   public JTable(TableModel paramTableModel, TableColumnModel paramTableColumnModel)
/*      */   {
/*  588 */     this(paramTableModel, paramTableColumnModel, null);
/*      */   }
/*      */ 
/*      */   public JTable(TableModel paramTableModel, TableColumnModel paramTableColumnModel, ListSelectionModel paramListSelectionModel)
/*      */   {
/*  611 */     setLayout(null);
/*      */ 
/*  613 */     setFocusTraversalKeys(0, JComponent.getManagingFocusForwardTraversalKeys());
/*      */ 
/*  615 */     setFocusTraversalKeys(1, JComponent.getManagingFocusBackwardTraversalKeys());
/*      */ 
/*  617 */     if (paramTableColumnModel == null) {
/*  618 */       paramTableColumnModel = createDefaultColumnModel();
/*  619 */       this.autoCreateColumnsFromModel = true;
/*      */     }
/*  621 */     setColumnModel(paramTableColumnModel);
/*      */ 
/*  623 */     if (paramListSelectionModel == null) {
/*  624 */       paramListSelectionModel = createDefaultSelectionModel();
/*      */     }
/*  626 */     setSelectionModel(paramListSelectionModel);
/*      */ 
/*  631 */     if (paramTableModel == null) {
/*  632 */       paramTableModel = createDefaultDataModel();
/*      */     }
/*  634 */     setModel(paramTableModel);
/*      */ 
/*  636 */     initializeLocalVars();
/*  637 */     updateUI();
/*      */   }
/*      */ 
/*      */   public JTable(int paramInt1, int paramInt2)
/*      */   {
/*  651 */     this(new DefaultTableModel(paramInt1, paramInt2));
/*      */   }
/*      */ 
/*      */   public JTable(Vector paramVector1, Vector paramVector2)
/*      */   {
/*  669 */     this(new DefaultTableModel(paramVector1, paramVector2));
/*      */   }
/*      */ 
/*      */   public JTable(final Object[][] paramArrayOfObject, Object[] paramArrayOfObject1)
/*      */   {
/*  686 */     this(new AbstractTableModel() {
/*  687 */       public String getColumnName(int paramAnonymousInt) { return JTable.this[paramAnonymousInt].toString(); } 
/*  688 */       public int getRowCount() { return paramArrayOfObject.length; } 
/*  689 */       public int getColumnCount() { return JTable.this.length; } 
/*  690 */       public Object getValueAt(int paramAnonymousInt1, int paramAnonymousInt2) { return paramArrayOfObject[paramAnonymousInt1][paramAnonymousInt2]; } 
/*  691 */       public boolean isCellEditable(int paramAnonymousInt1, int paramAnonymousInt2) { return true; } 
/*      */       public void setValueAt(Object paramAnonymousObject, int paramAnonymousInt1, int paramAnonymousInt2) {
/*  693 */         paramArrayOfObject[paramAnonymousInt1][paramAnonymousInt2] = paramAnonymousObject;
/*  694 */         fireTableCellUpdated(paramAnonymousInt1, paramAnonymousInt2);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/*  705 */     super.addNotify();
/*  706 */     configureEnclosingScrollPane();
/*      */   }
/*      */ 
/*      */   protected void configureEnclosingScrollPane()
/*      */   {
/*  723 */     Container localContainer1 = SwingUtilities.getUnwrappedParent(this);
/*  724 */     if ((localContainer1 instanceof JViewport)) {
/*  725 */       JViewport localJViewport1 = (JViewport)localContainer1;
/*  726 */       Container localContainer2 = localJViewport1.getParent();
/*  727 */       if ((localContainer2 instanceof JScrollPane)) {
/*  728 */         JScrollPane localJScrollPane = (JScrollPane)localContainer2;
/*      */ 
/*  732 */         JViewport localJViewport2 = localJScrollPane.getViewport();
/*  733 */         if ((localJViewport2 == null) || (SwingUtilities.getUnwrappedView(localJViewport2) != this))
/*      */         {
/*  735 */           return;
/*      */         }
/*  737 */         localJScrollPane.setColumnHeaderView(getTableHeader());
/*      */ 
/*  739 */         configureEnclosingScrollPaneUI();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void configureEnclosingScrollPaneUI()
/*      */   {
/*  757 */     Container localContainer1 = SwingUtilities.getUnwrappedParent(this);
/*  758 */     if ((localContainer1 instanceof JViewport)) {
/*  759 */       JViewport localJViewport1 = (JViewport)localContainer1;
/*  760 */       Container localContainer2 = localJViewport1.getParent();
/*  761 */       if ((localContainer2 instanceof JScrollPane)) {
/*  762 */         JScrollPane localJScrollPane = (JScrollPane)localContainer2;
/*      */ 
/*  766 */         JViewport localJViewport2 = localJScrollPane.getViewport();
/*  767 */         if ((localJViewport2 == null) || (SwingUtilities.getUnwrappedView(localJViewport2) != this))
/*      */         {
/*  769 */           return;
/*      */         }
/*      */ 
/*  772 */         Border localBorder = localJScrollPane.getBorder();
/*  773 */         if ((localBorder == null) || ((localBorder instanceof UIResource))) {
/*  774 */           localObject = UIManager.getBorder("Table.scrollPaneBorder");
/*      */ 
/*  776 */           if (localObject != null) {
/*  777 */             localJScrollPane.setBorder((Border)localObject);
/*      */           }
/*      */         }
/*      */ 
/*  781 */         Object localObject = localJScrollPane.getCorner("UPPER_TRAILING_CORNER");
/*      */ 
/*  783 */         if ((localObject == null) || ((localObject instanceof UIResource))) {
/*  784 */           localObject = null;
/*      */           try {
/*  786 */             localObject = (Component)UIManager.get("Table.scrollPaneCornerComponent");
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/*      */           }
/*  791 */           localJScrollPane.setCorner("UPPER_TRAILING_CORNER", (Component)localObject);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/*  804 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("permanentFocusOwner", this.editorRemover);
/*      */ 
/*  806 */     this.editorRemover = null;
/*  807 */     unconfigureEnclosingScrollPane();
/*  808 */     super.removeNotify();
/*      */   }
/*      */ 
/*      */   protected void unconfigureEnclosingScrollPane()
/*      */   {
/*  824 */     Container localContainer1 = SwingUtilities.getUnwrappedParent(this);
/*  825 */     if ((localContainer1 instanceof JViewport)) {
/*  826 */       JViewport localJViewport1 = (JViewport)localContainer1;
/*  827 */       Container localContainer2 = localJViewport1.getParent();
/*  828 */       if ((localContainer2 instanceof JScrollPane)) {
/*  829 */         JScrollPane localJScrollPane = (JScrollPane)localContainer2;
/*      */ 
/*  833 */         JViewport localJViewport2 = localJScrollPane.getViewport();
/*  834 */         if ((localJViewport2 == null) || (SwingUtilities.getUnwrappedView(localJViewport2) != this))
/*      */         {
/*  836 */           return;
/*      */         }
/*  838 */         localJScrollPane.setColumnHeaderView(null);
/*      */ 
/*  840 */         Component localComponent = localJScrollPane.getCorner("UPPER_TRAILING_CORNER");
/*      */ 
/*  842 */         if ((localComponent instanceof UIResource))
/*  843 */           localJScrollPane.setCorner("UPPER_TRAILING_CORNER", null);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void setUIProperty(String paramString, Object paramObject)
/*      */   {
/*  851 */     if (paramString == "rowHeight") {
/*  852 */       if (!this.isRowHeightSet) {
/*  853 */         setRowHeight(((Number)paramObject).intValue());
/*  854 */         this.isRowHeightSet = false;
/*      */       }
/*  856 */       return;
/*      */     }
/*  858 */     super.setUIProperty(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static JScrollPane createScrollPaneForTable(JTable paramJTable)
/*      */   {
/*  873 */     return new JScrollPane(paramJTable);
/*      */   }
/*      */ 
/*      */   public void setTableHeader(JTableHeader paramJTableHeader)
/*      */   {
/*  891 */     if (this.tableHeader != paramJTableHeader) {
/*  892 */       JTableHeader localJTableHeader = this.tableHeader;
/*      */ 
/*  894 */       if (localJTableHeader != null) {
/*  895 */         localJTableHeader.setTable(null);
/*      */       }
/*  897 */       this.tableHeader = paramJTableHeader;
/*  898 */       if (paramJTableHeader != null) {
/*  899 */         paramJTableHeader.setTable(this);
/*      */       }
/*  901 */       firePropertyChange("tableHeader", localJTableHeader, paramJTableHeader);
/*      */     }
/*      */   }
/*      */ 
/*      */   public JTableHeader getTableHeader()
/*      */   {
/*  912 */     return this.tableHeader;
/*      */   }
/*      */ 
/*      */   public void setRowHeight(int paramInt)
/*      */   {
/*  930 */     if (paramInt <= 0) {
/*  931 */       throw new IllegalArgumentException("New row height less than 1");
/*      */     }
/*  933 */     int i = this.rowHeight;
/*  934 */     this.rowHeight = paramInt;
/*  935 */     this.rowModel = null;
/*  936 */     if (this.sortManager != null) {
/*  937 */       this.sortManager.modelRowSizes = null;
/*      */     }
/*  939 */     this.isRowHeightSet = true;
/*  940 */     resizeAndRepaint();
/*  941 */     firePropertyChange("rowHeight", i, paramInt);
/*      */   }
/*      */ 
/*      */   public int getRowHeight()
/*      */   {
/*  951 */     return this.rowHeight;
/*      */   }
/*      */ 
/*      */   private SizeSequence getRowModel() {
/*  955 */     if (this.rowModel == null) {
/*  956 */       this.rowModel = new SizeSequence(getRowCount(), getRowHeight());
/*      */     }
/*  958 */     return this.rowModel;
/*      */   }
/*      */ 
/*      */   public void setRowHeight(int paramInt1, int paramInt2)
/*      */   {
/*  977 */     if (paramInt2 <= 0) {
/*  978 */       throw new IllegalArgumentException("New row height less than 1");
/*      */     }
/*  980 */     getRowModel().setSize(paramInt1, paramInt2);
/*  981 */     if (this.sortManager != null) {
/*  982 */       this.sortManager.setViewRowHeight(paramInt1, paramInt2);
/*      */     }
/*  984 */     resizeAndRepaint();
/*      */   }
/*      */ 
/*      */   public int getRowHeight(int paramInt)
/*      */   {
/*  994 */     return this.rowModel == null ? getRowHeight() : this.rowModel.getSize(paramInt);
/*      */   }
/*      */ 
/*      */   public void setRowMargin(int paramInt)
/*      */   {
/* 1007 */     int i = this.rowMargin;
/* 1008 */     this.rowMargin = paramInt;
/* 1009 */     resizeAndRepaint();
/* 1010 */     firePropertyChange("rowMargin", i, paramInt);
/*      */   }
/*      */ 
/*      */   public int getRowMargin()
/*      */   {
/* 1021 */     return this.rowMargin;
/*      */   }
/*      */ 
/*      */   public void setIntercellSpacing(Dimension paramDimension)
/*      */   {
/* 1039 */     setRowMargin(paramDimension.height);
/* 1040 */     getColumnModel().setColumnMargin(paramDimension.width);
/*      */ 
/* 1042 */     resizeAndRepaint();
/*      */   }
/*      */ 
/*      */   public Dimension getIntercellSpacing()
/*      */   {
/* 1053 */     return new Dimension(getColumnModel().getColumnMargin(), this.rowMargin);
/*      */   }
/*      */ 
/*      */   public void setGridColor(Color paramColor)
/*      */   {
/* 1068 */     if (paramColor == null) {
/* 1069 */       throw new IllegalArgumentException("New color is null");
/*      */     }
/* 1071 */     Color localColor = this.gridColor;
/* 1072 */     this.gridColor = paramColor;
/* 1073 */     firePropertyChange("gridColor", localColor, paramColor);
/*      */ 
/* 1075 */     repaint();
/*      */   }
/*      */ 
/*      */   public Color getGridColor()
/*      */   {
/* 1086 */     return this.gridColor;
/*      */   }
/*      */ 
/*      */   public void setShowGrid(boolean paramBoolean)
/*      */   {
/* 1104 */     setShowHorizontalLines(paramBoolean);
/* 1105 */     setShowVerticalLines(paramBoolean);
/*      */ 
/* 1108 */     repaint();
/*      */   }
/*      */ 
/*      */   public void setShowHorizontalLines(boolean paramBoolean)
/*      */   {
/* 1124 */     boolean bool = this.showHorizontalLines;
/* 1125 */     this.showHorizontalLines = paramBoolean;
/* 1126 */     firePropertyChange("showHorizontalLines", bool, paramBoolean);
/*      */ 
/* 1129 */     repaint();
/*      */   }
/*      */ 
/*      */   public void setShowVerticalLines(boolean paramBoolean)
/*      */   {
/* 1145 */     boolean bool = this.showVerticalLines;
/* 1146 */     this.showVerticalLines = paramBoolean;
/* 1147 */     firePropertyChange("showVerticalLines", bool, paramBoolean);
/*      */ 
/* 1149 */     repaint();
/*      */   }
/*      */ 
/*      */   public boolean getShowHorizontalLines()
/*      */   {
/* 1161 */     return this.showHorizontalLines;
/*      */   }
/*      */ 
/*      */   public boolean getShowVerticalLines()
/*      */   {
/* 1173 */     return this.showVerticalLines;
/*      */   }
/*      */ 
/*      */   public void setAutoResizeMode(int paramInt)
/*      */   {
/* 1200 */     if ((paramInt == 0) || (paramInt == 1) || (paramInt == 2) || (paramInt == 3) || (paramInt == 4))
/*      */     {
/* 1205 */       int i = this.autoResizeMode;
/* 1206 */       this.autoResizeMode = paramInt;
/* 1207 */       resizeAndRepaint();
/* 1208 */       if (this.tableHeader != null) {
/* 1209 */         this.tableHeader.resizeAndRepaint();
/*      */       }
/* 1211 */       firePropertyChange("autoResizeMode", i, this.autoResizeMode);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getAutoResizeMode()
/*      */   {
/* 1225 */     return this.autoResizeMode;
/*      */   }
/*      */ 
/*      */   public void setAutoCreateColumnsFromModel(boolean paramBoolean)
/*      */   {
/* 1241 */     if (this.autoCreateColumnsFromModel != paramBoolean) {
/* 1242 */       boolean bool = this.autoCreateColumnsFromModel;
/* 1243 */       this.autoCreateColumnsFromModel = paramBoolean;
/* 1244 */       if (paramBoolean) {
/* 1245 */         createDefaultColumnsFromModel();
/*      */       }
/* 1247 */       firePropertyChange("autoCreateColumnsFromModel", bool, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getAutoCreateColumnsFromModel()
/*      */   {
/* 1264 */     return this.autoCreateColumnsFromModel;
/*      */   }
/*      */ 
/*      */   public void createDefaultColumnsFromModel()
/*      */   {
/* 1278 */     TableModel localTableModel = getModel();
/* 1279 */     if (localTableModel != null)
/*      */     {
/* 1281 */       TableColumnModel localTableColumnModel = getColumnModel();
/* 1282 */       while (localTableColumnModel.getColumnCount() > 0) {
/* 1283 */         localTableColumnModel.removeColumn(localTableColumnModel.getColumn(0));
/*      */       }
/*      */ 
/* 1287 */       for (int i = 0; i < localTableModel.getColumnCount(); i++) {
/* 1288 */         TableColumn localTableColumn = new TableColumn(i);
/* 1289 */         addColumn(localTableColumn);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDefaultRenderer(Class<?> paramClass, TableCellRenderer paramTableCellRenderer)
/*      */   {
/* 1306 */     if (paramTableCellRenderer != null) {
/* 1307 */       this.defaultRenderersByColumnClass.put(paramClass, paramTableCellRenderer);
/*      */     }
/*      */     else
/* 1310 */       this.defaultRenderersByColumnClass.remove(paramClass);
/*      */   }
/*      */ 
/*      */   public TableCellRenderer getDefaultRenderer(Class<?> paramClass)
/*      */   {
/* 1330 */     if (paramClass == null) {
/* 1331 */       return null;
/*      */     }
/*      */ 
/* 1334 */     Object localObject1 = this.defaultRenderersByColumnClass.get(paramClass);
/* 1335 */     if (localObject1 != null) {
/* 1336 */       return (TableCellRenderer)localObject1;
/*      */     }
/*      */ 
/* 1339 */     Object localObject2 = paramClass.getSuperclass();
/* 1340 */     if ((localObject2 == null) && (paramClass != Object.class)) {
/* 1341 */       localObject2 = Object.class;
/*      */     }
/* 1343 */     return getDefaultRenderer((Class)localObject2);
/*      */   }
/*      */ 
/*      */   public void setDefaultEditor(Class<?> paramClass, TableCellEditor paramTableCellEditor)
/*      */   {
/* 1364 */     if (paramTableCellEditor != null) {
/* 1365 */       this.defaultEditorsByColumnClass.put(paramClass, paramTableCellEditor);
/*      */     }
/*      */     else
/* 1368 */       this.defaultEditorsByColumnClass.remove(paramClass);
/*      */   }
/*      */ 
/*      */   public TableCellEditor getDefaultEditor(Class<?> paramClass)
/*      */   {
/* 1387 */     if (paramClass == null) {
/* 1388 */       return null;
/*      */     }
/*      */ 
/* 1391 */     Object localObject = this.defaultEditorsByColumnClass.get(paramClass);
/* 1392 */     if (localObject != null) {
/* 1393 */       return (TableCellEditor)localObject;
/*      */     }
/*      */ 
/* 1396 */     return getDefaultEditor(paramClass.getSuperclass());
/*      */   }
/*      */ 
/*      */   public void setDragEnabled(boolean paramBoolean)
/*      */   {
/* 1436 */     if ((paramBoolean) && (GraphicsEnvironment.isHeadless())) {
/* 1437 */       throw new HeadlessException();
/*      */     }
/* 1439 */     this.dragEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getDragEnabled()
/*      */   {
/* 1450 */     return this.dragEnabled;
/*      */   }
/*      */ 
/*      */   public final void setDropMode(DropMode paramDropMode)
/*      */   {
/* 1486 */     if (paramDropMode != null) {
/* 1487 */       switch (7.$SwitchMap$javax$swing$DropMode[paramDropMode.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/* 1496 */         this.dropMode = paramDropMode;
/* 1497 */         return;
/*      */       }
/*      */     }
/*      */ 
/* 1501 */     throw new IllegalArgumentException(paramDropMode + ": Unsupported drop mode for table");
/*      */   }
/*      */ 
/*      */   public final DropMode getDropMode()
/*      */   {
/* 1512 */     return this.dropMode;
/*      */   }
/*      */ 
/*      */   DropLocation dropLocationForPoint(Point paramPoint)
/*      */   {
/* 1523 */     DropLocation localDropLocation = null;
/*      */ 
/* 1525 */     int i = rowAtPoint(paramPoint);
/* 1526 */     int j = columnAtPoint(paramPoint);
/* 1527 */     int k = (Boolean.TRUE == getClientProperty("Table.isFileList")) && (SwingUtilities2.pointOutsidePrefSize(this, i, j, paramPoint)) ? 1 : 0;
/*      */ 
/* 1530 */     Rectangle localRectangle = getCellRect(i, j, true);
/*      */ 
/* 1532 */     boolean bool1 = false;
/* 1533 */     boolean bool2 = getComponentOrientation().isLeftToRight();
/*      */     SwingUtilities2.Section localSection1;
/*      */     SwingUtilities2.Section localSection2;
/* 1535 */     switch (7.$SwitchMap$javax$swing$DropMode[this.dropMode.ordinal()]) {
/*      */     case 1:
/*      */     case 2:
/* 1538 */       if ((i == -1) || (j == -1) || (k != 0))
/* 1539 */         localDropLocation = new DropLocation(paramPoint, -1, -1, false, false, null);
/*      */       else {
/* 1541 */         localDropLocation = new DropLocation(paramPoint, i, j, false, false, null);
/*      */       }
/* 1543 */       break;
/*      */     case 3:
/* 1545 */       if ((i == -1) && (j == -1)) {
/* 1546 */         localDropLocation = new DropLocation(paramPoint, 0, 0, true, true, null);
/*      */       }
/*      */       else
/*      */       {
/* 1550 */         localSection1 = SwingUtilities2.liesInHorizontal(localRectangle, paramPoint, bool2, true);
/*      */ 
/* 1552 */         if (i == -1) {
/* 1553 */           if (localSection1 == SwingUtilities2.Section.LEADING)
/* 1554 */             localDropLocation = new DropLocation(paramPoint, getRowCount(), j, true, true, null);
/* 1555 */           else if (localSection1 == SwingUtilities2.Section.TRAILING)
/* 1556 */             localDropLocation = new DropLocation(paramPoint, getRowCount(), j + 1, true, true, null);
/*      */           else
/* 1558 */             localDropLocation = new DropLocation(paramPoint, getRowCount(), j, true, false, null);
/*      */         }
/* 1560 */         else if ((localSection1 == SwingUtilities2.Section.LEADING) || (localSection1 == SwingUtilities2.Section.TRAILING)) {
/* 1561 */           localSection2 = SwingUtilities2.liesInVertical(localRectangle, paramPoint, true);
/* 1562 */           if (localSection2 == SwingUtilities2.Section.LEADING) {
/* 1563 */             bool1 = true;
/* 1564 */           } else if (localSection2 == SwingUtilities2.Section.TRAILING) {
/* 1565 */             i++;
/* 1566 */             bool1 = true;
/*      */           }
/*      */ 
/* 1569 */           localDropLocation = new DropLocation(paramPoint, i, localSection1 == SwingUtilities2.Section.TRAILING ? j + 1 : j, bool1, true, null);
/*      */         }
/*      */         else
/*      */         {
/* 1573 */           if (SwingUtilities2.liesInVertical(localRectangle, paramPoint, false) == SwingUtilities2.Section.TRAILING) {
/* 1574 */             i++;
/*      */           }
/*      */ 
/* 1577 */           localDropLocation = new DropLocation(paramPoint, i, j, true, false, null);
/*      */         }
/*      */       }
/* 1580 */       break;
/*      */     case 4:
/* 1582 */       if ((i == -1) && (j == -1)) {
/* 1583 */         localDropLocation = new DropLocation(paramPoint, -1, -1, false, false, null);
/*      */       }
/* 1587 */       else if (i == -1) {
/* 1588 */         localDropLocation = new DropLocation(paramPoint, getRowCount(), j, true, false, null);
/*      */       }
/*      */       else
/*      */       {
/* 1592 */         if (SwingUtilities2.liesInVertical(localRectangle, paramPoint, false) == SwingUtilities2.Section.TRAILING) {
/* 1593 */           i++;
/*      */         }
/*      */ 
/* 1596 */         localDropLocation = new DropLocation(paramPoint, i, j, true, false, null);
/* 1597 */       }break;
/*      */     case 7:
/* 1599 */       if ((i == -1) && (j == -1)) {
/* 1600 */         localDropLocation = new DropLocation(paramPoint, -1, -1, false, false, null);
/*      */       }
/* 1604 */       else if (i == -1) {
/* 1605 */         localDropLocation = new DropLocation(paramPoint, getRowCount(), j, true, false, null);
/*      */       }
/*      */       else
/*      */       {
/* 1609 */         localSection2 = SwingUtilities2.liesInVertical(localRectangle, paramPoint, true);
/* 1610 */         if (localSection2 == SwingUtilities2.Section.LEADING) {
/* 1611 */           bool1 = true;
/* 1612 */         } else if (localSection2 == SwingUtilities2.Section.TRAILING) {
/* 1613 */           i++;
/* 1614 */           bool1 = true;
/*      */         }
/*      */ 
/* 1617 */         localDropLocation = new DropLocation(paramPoint, i, j, bool1, false, null);
/* 1618 */       }break;
/*      */     case 5:
/* 1620 */       if (i == -1) {
/* 1621 */         localDropLocation = new DropLocation(paramPoint, -1, -1, false, false, null);
/*      */       }
/* 1625 */       else if (j == -1) {
/* 1626 */         localDropLocation = new DropLocation(paramPoint, getColumnCount(), j, false, true, null);
/*      */       }
/*      */       else
/*      */       {
/* 1630 */         if (SwingUtilities2.liesInHorizontal(localRectangle, paramPoint, bool2, false) == SwingUtilities2.Section.TRAILING) {
/* 1631 */           j++;
/*      */         }
/*      */ 
/* 1634 */         localDropLocation = new DropLocation(paramPoint, i, j, false, true, null);
/* 1635 */       }break;
/*      */     case 8:
/* 1637 */       if (i == -1) {
/* 1638 */         localDropLocation = new DropLocation(paramPoint, -1, -1, false, false, null);
/*      */       }
/* 1642 */       else if (j == -1) {
/* 1643 */         localDropLocation = new DropLocation(paramPoint, i, getColumnCount(), false, true, null);
/*      */       }
/*      */       else
/*      */       {
/* 1647 */         localSection1 = SwingUtilities2.liesInHorizontal(localRectangle, paramPoint, bool2, true);
/* 1648 */         if (localSection1 == SwingUtilities2.Section.LEADING) {
/* 1649 */           bool1 = true;
/* 1650 */         } else if (localSection1 == SwingUtilities2.Section.TRAILING) {
/* 1651 */           j++;
/* 1652 */           bool1 = true;
/*      */         }
/*      */ 
/* 1655 */         localDropLocation = new DropLocation(paramPoint, i, j, false, bool1, null);
/* 1656 */       }break;
/*      */     case 6:
/* 1658 */       if ((i == -1) && (j == -1)) {
/* 1659 */         localDropLocation = new DropLocation(paramPoint, 0, 0, true, true, null);
/*      */       }
/*      */       else
/*      */       {
/* 1663 */         localSection1 = SwingUtilities2.liesInHorizontal(localRectangle, paramPoint, bool2, true);
/*      */ 
/* 1665 */         if (i == -1) {
/* 1666 */           if (localSection1 == SwingUtilities2.Section.LEADING)
/* 1667 */             localDropLocation = new DropLocation(paramPoint, getRowCount(), j, true, true, null);
/* 1668 */           else if (localSection1 == SwingUtilities2.Section.TRAILING)
/* 1669 */             localDropLocation = new DropLocation(paramPoint, getRowCount(), j + 1, true, true, null);
/*      */           else {
/* 1671 */             localDropLocation = new DropLocation(paramPoint, getRowCount(), j, true, false, null);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1677 */           localSection2 = SwingUtilities2.liesInVertical(localRectangle, paramPoint, true);
/* 1678 */           if (localSection2 == SwingUtilities2.Section.LEADING) {
/* 1679 */             bool1 = true;
/* 1680 */           } else if (localSection2 == SwingUtilities2.Section.TRAILING) {
/* 1681 */             i++;
/* 1682 */             bool1 = true;
/*      */           }
/*      */ 
/* 1685 */           localDropLocation = new DropLocation(paramPoint, i, localSection1 == SwingUtilities2.Section.TRAILING ? j + 1 : j, bool1, localSection1 != SwingUtilities2.Section.MIDDLE, null);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1690 */       break;
/*      */     default:
/* 1692 */       if (!$assertionsDisabled) throw new AssertionError("Unexpected drop mode");
/*      */       break;
/*      */     }
/* 1695 */     return localDropLocation;
/*      */   }
/*      */ 
/*      */   Object setDropLocation(TransferHandler.DropLocation paramDropLocation, Object paramObject, boolean paramBoolean)
/*      */   {
/* 1735 */     Object localObject1 = null;
/* 1736 */     DropLocation localDropLocation = (DropLocation)paramDropLocation;
/*      */ 
/* 1738 */     if (this.dropMode == DropMode.USE_SELECTION) {
/* 1739 */       if (localDropLocation == null) {
/* 1740 */         if ((!paramBoolean) && (paramObject != null)) {
/* 1741 */           clearSelection();
/*      */ 
/* 1743 */           localObject2 = ((int[][])(int[][])paramObject)[0];
/* 1744 */           int[] arrayOfInt1 = ((int[][])(int[][])paramObject)[1];
/* 1745 */           int[] arrayOfInt2 = ((int[][])(int[][])paramObject)[2];
/*      */           int k;
/* 1747 */           for (k : localObject2) {
/* 1748 */             addRowSelectionInterval(k, k);
/*      */           }
/*      */ 
/* 1751 */           for (k : arrayOfInt1) {
/* 1752 */             addColumnSelectionInterval(k, k);
/*      */           }
/*      */ 
/* 1755 */           SwingUtilities2.setLeadAnchorWithoutSelection(getSelectionModel(), arrayOfInt2[1], arrayOfInt2[0]);
/*      */ 
/* 1758 */           SwingUtilities2.setLeadAnchorWithoutSelection(getColumnModel().getSelectionModel(), arrayOfInt2[3], arrayOfInt2[2]);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1763 */         if (this.dropLocation == null) {
/* 1764 */           localObject1 = new int[][] { getSelectedRows(), getSelectedColumns(), { getAdjustedIndex(getSelectionModel().getAnchorSelectionIndex(), true), getAdjustedIndex(getSelectionModel().getLeadSelectionIndex(), true), getAdjustedIndex(getColumnModel().getSelectionModel().getAnchorSelectionIndex(), false), getAdjustedIndex(getColumnModel().getSelectionModel().getLeadSelectionIndex(), false) } };
/*      */         }
/*      */         else
/*      */         {
/* 1776 */           localObject1 = paramObject;
/*      */         }
/*      */ 
/* 1779 */         if (localDropLocation.getRow() == -1) {
/* 1780 */           clearSelectionAndLeadAnchor();
/*      */         } else {
/* 1782 */           setRowSelectionInterval(localDropLocation.getRow(), localDropLocation.getRow());
/*      */ 
/* 1784 */           setColumnSelectionInterval(localDropLocation.getColumn(), localDropLocation.getColumn());
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1790 */     Object localObject2 = this.dropLocation;
/* 1791 */     this.dropLocation = localDropLocation;
/* 1792 */     firePropertyChange("dropLocation", localObject2, this.dropLocation);
/*      */ 
/* 1794 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public final DropLocation getDropLocation()
/*      */   {
/* 1816 */     return this.dropLocation;
/*      */   }
/*      */ 
/*      */   public void setAutoCreateRowSorter(boolean paramBoolean)
/*      */   {
/* 1839 */     boolean bool = this.autoCreateRowSorter;
/* 1840 */     this.autoCreateRowSorter = paramBoolean;
/* 1841 */     if (paramBoolean) {
/* 1842 */       setRowSorter(new TableRowSorter(getModel()));
/*      */     }
/* 1844 */     firePropertyChange("autoCreateRowSorter", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getAutoCreateRowSorter()
/*      */   {
/* 1858 */     return this.autoCreateRowSorter;
/*      */   }
/*      */ 
/*      */   public void setUpdateSelectionOnSort(boolean paramBoolean)
/*      */   {
/* 1875 */     if (this.updateSelectionOnSort != paramBoolean) {
/* 1876 */       this.updateSelectionOnSort = paramBoolean;
/* 1877 */       firePropertyChange("updateSelectionOnSort", !paramBoolean, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getUpdateSelectionOnSort()
/*      */   {
/* 1888 */     return this.updateSelectionOnSort;
/*      */   }
/*      */ 
/*      */   public void setRowSorter(RowSorter<? extends TableModel> paramRowSorter)
/*      */   {
/* 1914 */     RowSorter localRowSorter = null;
/* 1915 */     if (this.sortManager != null) {
/* 1916 */       localRowSorter = this.sortManager.sorter;
/* 1917 */       this.sortManager.dispose();
/* 1918 */       this.sortManager = null;
/*      */     }
/* 1920 */     this.rowModel = null;
/* 1921 */     clearSelectionAndLeadAnchor();
/* 1922 */     if (paramRowSorter != null) {
/* 1923 */       this.sortManager = new SortManager(paramRowSorter);
/*      */     }
/* 1925 */     resizeAndRepaint();
/* 1926 */     firePropertyChange("rowSorter", localRowSorter, paramRowSorter);
/* 1927 */     firePropertyChange("sorter", localRowSorter, paramRowSorter);
/*      */   }
/*      */ 
/*      */   public RowSorter<? extends TableModel> getRowSorter()
/*      */   {
/* 1937 */     return this.sortManager != null ? this.sortManager.sorter : null;
/*      */   }
/*      */ 
/*      */   public void setSelectionMode(int paramInt)
/*      */   {
/* 1970 */     clearSelection();
/* 1971 */     getSelectionModel().setSelectionMode(paramInt);
/* 1972 */     getColumnModel().getSelectionModel().setSelectionMode(paramInt);
/*      */   }
/*      */ 
/*      */   public void setRowSelectionAllowed(boolean paramBoolean)
/*      */   {
/* 1986 */     boolean bool = this.rowSelectionAllowed;
/* 1987 */     this.rowSelectionAllowed = paramBoolean;
/* 1988 */     if (bool != paramBoolean) {
/* 1989 */       repaint();
/*      */     }
/* 1991 */     firePropertyChange("rowSelectionAllowed", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getRowSelectionAllowed()
/*      */   {
/* 2001 */     return this.rowSelectionAllowed;
/*      */   }
/*      */ 
/*      */   public void setColumnSelectionAllowed(boolean paramBoolean)
/*      */   {
/* 2015 */     boolean bool = this.columnModel.getColumnSelectionAllowed();
/* 2016 */     this.columnModel.setColumnSelectionAllowed(paramBoolean);
/* 2017 */     if (bool != paramBoolean) {
/* 2018 */       repaint();
/*      */     }
/* 2020 */     firePropertyChange("columnSelectionAllowed", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getColumnSelectionAllowed()
/*      */   {
/* 2030 */     return this.columnModel.getColumnSelectionAllowed();
/*      */   }
/*      */ 
/*      */   public void setCellSelectionEnabled(boolean paramBoolean)
/*      */   {
/* 2054 */     setRowSelectionAllowed(paramBoolean);
/* 2055 */     setColumnSelectionAllowed(paramBoolean);
/* 2056 */     boolean bool = this.cellSelectionEnabled;
/* 2057 */     this.cellSelectionEnabled = paramBoolean;
/* 2058 */     firePropertyChange("cellSelectionEnabled", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getCellSelectionEnabled()
/*      */   {
/* 2071 */     return (getRowSelectionAllowed()) && (getColumnSelectionAllowed());
/*      */   }
/*      */ 
/*      */   public void selectAll()
/*      */   {
/* 2079 */     if (isEditing()) {
/* 2080 */       removeEditor();
/*      */     }
/* 2082 */     if ((getRowCount() > 0) && (getColumnCount() > 0))
/*      */     {
/* 2087 */       ListSelectionModel localListSelectionModel = this.selectionModel;
/* 2088 */       localListSelectionModel.setValueIsAdjusting(true);
/* 2089 */       int i = getAdjustedIndex(localListSelectionModel.getLeadSelectionIndex(), true);
/* 2090 */       int j = getAdjustedIndex(localListSelectionModel.getAnchorSelectionIndex(), true);
/*      */ 
/* 2092 */       setRowSelectionInterval(0, getRowCount() - 1);
/*      */ 
/* 2095 */       SwingUtilities2.setLeadAnchorWithoutSelection(localListSelectionModel, i, j);
/*      */ 
/* 2097 */       localListSelectionModel.setValueIsAdjusting(false);
/*      */ 
/* 2099 */       localListSelectionModel = this.columnModel.getSelectionModel();
/* 2100 */       localListSelectionModel.setValueIsAdjusting(true);
/* 2101 */       i = getAdjustedIndex(localListSelectionModel.getLeadSelectionIndex(), false);
/* 2102 */       j = getAdjustedIndex(localListSelectionModel.getAnchorSelectionIndex(), false);
/*      */ 
/* 2104 */       setColumnSelectionInterval(0, getColumnCount() - 1);
/*      */ 
/* 2107 */       SwingUtilities2.setLeadAnchorWithoutSelection(localListSelectionModel, i, j);
/*      */ 
/* 2109 */       localListSelectionModel.setValueIsAdjusting(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearSelection()
/*      */   {
/* 2117 */     this.selectionModel.clearSelection();
/* 2118 */     this.columnModel.getSelectionModel().clearSelection();
/*      */   }
/*      */ 
/*      */   private void clearSelectionAndLeadAnchor() {
/* 2122 */     this.selectionModel.setValueIsAdjusting(true);
/* 2123 */     this.columnModel.getSelectionModel().setValueIsAdjusting(true);
/*      */ 
/* 2125 */     clearSelection();
/*      */ 
/* 2127 */     this.selectionModel.setAnchorSelectionIndex(-1);
/* 2128 */     this.selectionModel.setLeadSelectionIndex(-1);
/* 2129 */     this.columnModel.getSelectionModel().setAnchorSelectionIndex(-1);
/* 2130 */     this.columnModel.getSelectionModel().setLeadSelectionIndex(-1);
/*      */ 
/* 2132 */     this.selectionModel.setValueIsAdjusting(false);
/* 2133 */     this.columnModel.getSelectionModel().setValueIsAdjusting(false);
/*      */   }
/*      */ 
/*      */   private int getAdjustedIndex(int paramInt, boolean paramBoolean) {
/* 2137 */     int i = paramBoolean ? getRowCount() : getColumnCount();
/* 2138 */     return paramInt < i ? paramInt : -1;
/*      */   }
/*      */ 
/*      */   private int boundRow(int paramInt) throws IllegalArgumentException {
/* 2142 */     if ((paramInt < 0) || (paramInt >= getRowCount())) {
/* 2143 */       throw new IllegalArgumentException("Row index out of range");
/*      */     }
/* 2145 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int boundColumn(int paramInt) {
/* 2149 */     if ((paramInt < 0) || (paramInt >= getColumnCount())) {
/* 2150 */       throw new IllegalArgumentException("Column index out of range");
/*      */     }
/* 2152 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public void setRowSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2166 */     this.selectionModel.setSelectionInterval(boundRow(paramInt1), boundRow(paramInt2));
/*      */   }
/*      */ 
/*      */   public void setColumnSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2180 */     this.columnModel.getSelectionModel().setSelectionInterval(boundColumn(paramInt1), boundColumn(paramInt2));
/*      */   }
/*      */ 
/*      */   public void addRowSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2193 */     this.selectionModel.addSelectionInterval(boundRow(paramInt1), boundRow(paramInt2));
/*      */   }
/*      */ 
/*      */   public void addColumnSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2207 */     this.columnModel.getSelectionModel().addSelectionInterval(boundColumn(paramInt1), boundColumn(paramInt2));
/*      */   }
/*      */ 
/*      */   public void removeRowSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2220 */     this.selectionModel.removeSelectionInterval(boundRow(paramInt1), boundRow(paramInt2));
/*      */   }
/*      */ 
/*      */   public void removeColumnSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2233 */     this.columnModel.getSelectionModel().removeSelectionInterval(boundColumn(paramInt1), boundColumn(paramInt2));
/*      */   }
/*      */ 
/*      */   public int getSelectedRow()
/*      */   {
/* 2241 */     return this.selectionModel.getMinSelectionIndex();
/*      */   }
/*      */ 
/*      */   public int getSelectedColumn()
/*      */   {
/* 2250 */     return this.columnModel.getSelectionModel().getMinSelectionIndex();
/*      */   }
/*      */ 
/*      */   public int[] getSelectedRows()
/*      */   {
/* 2261 */     int i = this.selectionModel.getMinSelectionIndex();
/* 2262 */     int j = this.selectionModel.getMaxSelectionIndex();
/*      */ 
/* 2264 */     if ((i == -1) || (j == -1)) {
/* 2265 */       return new int[0];
/*      */     }
/*      */ 
/* 2268 */     int[] arrayOfInt1 = new int[1 + (j - i)];
/* 2269 */     int k = 0;
/* 2270 */     for (int m = i; m <= j; m++) {
/* 2271 */       if (this.selectionModel.isSelectedIndex(m)) {
/* 2272 */         arrayOfInt1[(k++)] = m;
/*      */       }
/*      */     }
/* 2275 */     int[] arrayOfInt2 = new int[k];
/* 2276 */     System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, k);
/* 2277 */     return arrayOfInt2;
/*      */   }
/*      */ 
/*      */   public int[] getSelectedColumns()
/*      */   {
/* 2288 */     return this.columnModel.getSelectedColumns();
/*      */   }
/*      */ 
/*      */   public int getSelectedRowCount()
/*      */   {
/* 2297 */     int i = this.selectionModel.getMinSelectionIndex();
/* 2298 */     int j = this.selectionModel.getMaxSelectionIndex();
/* 2299 */     int k = 0;
/*      */ 
/* 2301 */     for (int m = i; m <= j; m++) {
/* 2302 */       if (this.selectionModel.isSelectedIndex(m)) {
/* 2303 */         k++;
/*      */       }
/*      */     }
/* 2306 */     return k;
/*      */   }
/*      */ 
/*      */   public int getSelectedColumnCount()
/*      */   {
/* 2315 */     return this.columnModel.getSelectedColumnCount();
/*      */   }
/*      */ 
/*      */   public boolean isRowSelected(int paramInt)
/*      */   {
/* 2326 */     return this.selectionModel.isSelectedIndex(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean isColumnSelected(int paramInt)
/*      */   {
/* 2338 */     return this.columnModel.getSelectionModel().isSelectedIndex(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean isCellSelected(int paramInt1, int paramInt2)
/*      */   {
/* 2352 */     if ((!getRowSelectionAllowed()) && (!getColumnSelectionAllowed())) {
/* 2353 */       return false;
/*      */     }
/* 2355 */     return ((!getRowSelectionAllowed()) || (isRowSelected(paramInt1))) && ((!getColumnSelectionAllowed()) || (isColumnSelected(paramInt2)));
/*      */   }
/*      */ 
/*      */   private void changeSelectionModel(ListSelectionModel paramListSelectionModel, int paramInt1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt2, boolean paramBoolean4)
/*      */   {
/* 2362 */     if (paramBoolean2) {
/* 2363 */       if (paramBoolean1) {
/* 2364 */         if (paramBoolean4) {
/* 2365 */           paramListSelectionModel.addSelectionInterval(paramInt2, paramInt1);
/*      */         } else {
/* 2367 */           paramListSelectionModel.removeSelectionInterval(paramInt2, paramInt1);
/*      */ 
/* 2369 */           if (Boolean.TRUE == getClientProperty("Table.isFileList")) {
/* 2370 */             paramListSelectionModel.addSelectionInterval(paramInt1, paramInt1);
/* 2371 */             paramListSelectionModel.setAnchorSelectionIndex(paramInt2);
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 2376 */         paramListSelectionModel.setSelectionInterval(paramInt2, paramInt1);
/*      */       }
/*      */ 
/*      */     }
/* 2380 */     else if (paramBoolean1) {
/* 2381 */       if (paramBoolean3) {
/* 2382 */         paramListSelectionModel.removeSelectionInterval(paramInt1, paramInt1);
/*      */       }
/*      */       else {
/* 2385 */         paramListSelectionModel.addSelectionInterval(paramInt1, paramInt1);
/*      */       }
/*      */     }
/*      */     else
/* 2389 */       paramListSelectionModel.setSelectionInterval(paramInt1, paramInt1);
/*      */   }
/*      */ 
/*      */   public void changeSelection(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 2424 */     ListSelectionModel localListSelectionModel1 = getSelectionModel();
/* 2425 */     ListSelectionModel localListSelectionModel2 = getColumnModel().getSelectionModel();
/*      */ 
/* 2427 */     int i = getAdjustedIndex(localListSelectionModel1.getAnchorSelectionIndex(), true);
/* 2428 */     int j = getAdjustedIndex(localListSelectionModel2.getAnchorSelectionIndex(), false);
/*      */ 
/* 2430 */     boolean bool1 = true;
/*      */ 
/* 2432 */     if (i == -1) {
/* 2433 */       if (getRowCount() > 0) {
/* 2434 */         i = 0;
/*      */       }
/* 2436 */       bool1 = false;
/*      */     }
/*      */ 
/* 2439 */     if (j == -1) {
/* 2440 */       if (getColumnCount() > 0) {
/* 2441 */         j = 0;
/*      */       }
/* 2443 */       bool1 = false;
/*      */     }
/*      */ 
/* 2453 */     boolean bool2 = isCellSelected(paramInt1, paramInt2);
/* 2454 */     bool1 = (bool1) && (isCellSelected(i, j));
/*      */ 
/* 2456 */     changeSelectionModel(localListSelectionModel2, paramInt2, paramBoolean1, paramBoolean2, bool2, j, bool1);
/*      */ 
/* 2458 */     changeSelectionModel(localListSelectionModel1, paramInt1, paramBoolean1, paramBoolean2, bool2, i, bool1);
/*      */ 
/* 2464 */     if (getAutoscrolls()) {
/* 2465 */       Rectangle localRectangle = getCellRect(paramInt1, paramInt2, false);
/* 2466 */       if (localRectangle != null)
/* 2467 */         scrollRectToVisible(localRectangle);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Color getSelectionForeground()
/*      */   {
/* 2480 */     return this.selectionForeground;
/*      */   }
/*      */ 
/*      */   public void setSelectionForeground(Color paramColor)
/*      */   {
/* 2505 */     Color localColor = this.selectionForeground;
/* 2506 */     this.selectionForeground = paramColor;
/* 2507 */     firePropertyChange("selectionForeground", localColor, paramColor);
/* 2508 */     repaint();
/*      */   }
/*      */ 
/*      */   public Color getSelectionBackground()
/*      */   {
/* 2519 */     return this.selectionBackground;
/*      */   }
/*      */ 
/*      */   public void setSelectionBackground(Color paramColor)
/*      */   {
/* 2543 */     Color localColor = this.selectionBackground;
/* 2544 */     this.selectionBackground = paramColor;
/* 2545 */     firePropertyChange("selectionBackground", localColor, paramColor);
/* 2546 */     repaint();
/*      */   }
/*      */ 
/*      */   public TableColumn getColumn(Object paramObject)
/*      */   {
/* 2560 */     TableColumnModel localTableColumnModel = getColumnModel();
/* 2561 */     int i = localTableColumnModel.getColumnIndex(paramObject);
/* 2562 */     return localTableColumnModel.getColumn(i);
/*      */   }
/*      */ 
/*      */   public int convertColumnIndexToModel(int paramInt)
/*      */   {
/* 2582 */     return SwingUtilities2.convertColumnIndexToModel(getColumnModel(), paramInt);
/*      */   }
/*      */ 
/*      */   public int convertColumnIndexToView(int paramInt)
/*      */   {
/* 2600 */     return SwingUtilities2.convertColumnIndexToView(getColumnModel(), paramInt);
/*      */   }
/*      */ 
/*      */   public int convertRowIndexToView(int paramInt)
/*      */   {
/* 2618 */     RowSorter localRowSorter = getRowSorter();
/* 2619 */     if (localRowSorter != null) {
/* 2620 */       return localRowSorter.convertRowIndexToView(paramInt);
/*      */     }
/* 2622 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public int convertRowIndexToModel(int paramInt)
/*      */   {
/* 2640 */     RowSorter localRowSorter = getRowSorter();
/* 2641 */     if (localRowSorter != null) {
/* 2642 */       return localRowSorter.convertRowIndexToModel(paramInt);
/*      */     }
/* 2644 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public int getRowCount()
/*      */   {
/* 2658 */     RowSorter localRowSorter = getRowSorter();
/* 2659 */     if (localRowSorter != null) {
/* 2660 */       return localRowSorter.getViewRowCount();
/*      */     }
/* 2662 */     return getModel().getRowCount();
/*      */   }
/*      */ 
/*      */   public int getColumnCount()
/*      */   {
/* 2674 */     return getColumnModel().getColumnCount();
/*      */   }
/*      */ 
/*      */   public String getColumnName(int paramInt)
/*      */   {
/* 2686 */     return getModel().getColumnName(convertColumnIndexToModel(paramInt));
/*      */   }
/*      */ 
/*      */   public Class<?> getColumnClass(int paramInt)
/*      */   {
/* 2698 */     return getModel().getColumnClass(convertColumnIndexToModel(paramInt));
/*      */   }
/*      */ 
/*      */   public Object getValueAt(int paramInt1, int paramInt2)
/*      */   {
/* 2717 */     return getModel().getValueAt(convertRowIndexToModel(paramInt1), convertColumnIndexToModel(paramInt2));
/*      */   }
/*      */ 
/*      */   public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
/*      */   {
/* 2741 */     getModel().setValueAt(paramObject, convertRowIndexToModel(paramInt1), convertColumnIndexToModel(paramInt2));
/*      */   }
/*      */ 
/*      */   public boolean isCellEditable(int paramInt1, int paramInt2)
/*      */   {
/* 2765 */     return getModel().isCellEditable(convertRowIndexToModel(paramInt1), convertColumnIndexToModel(paramInt2));
/*      */   }
/*      */ 
/*      */   public void addColumn(TableColumn paramTableColumn)
/*      */   {
/* 2799 */     if (paramTableColumn.getHeaderValue() == null) {
/* 2800 */       int i = paramTableColumn.getModelIndex();
/* 2801 */       String str = getModel().getColumnName(i);
/* 2802 */       paramTableColumn.setHeaderValue(str);
/*      */     }
/* 2804 */     getColumnModel().addColumn(paramTableColumn);
/*      */   }
/*      */ 
/*      */   public void removeColumn(TableColumn paramTableColumn)
/*      */   {
/* 2817 */     getColumnModel().removeColumn(paramTableColumn);
/*      */   }
/*      */ 
/*      */   public void moveColumn(int paramInt1, int paramInt2)
/*      */   {
/* 2830 */     getColumnModel().moveColumn(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public int columnAtPoint(Point paramPoint)
/*      */   {
/* 2849 */     int i = paramPoint.x;
/* 2850 */     if (!getComponentOrientation().isLeftToRight()) {
/* 2851 */       i = getWidth() - i - 1;
/*      */     }
/* 2853 */     return getColumnModel().getColumnIndexAtX(i);
/*      */   }
/*      */ 
/*      */   public int rowAtPoint(Point paramPoint)
/*      */   {
/* 2868 */     int i = paramPoint.y;
/* 2869 */     int j = this.rowModel == null ? i / getRowHeight() : this.rowModel.getIndex(i);
/* 2870 */     if (j < 0) {
/* 2871 */       return -1;
/*      */     }
/* 2873 */     if (j >= getRowCount()) {
/* 2874 */       return -1;
/*      */     }
/*      */ 
/* 2877 */     return j;
/*      */   }
/*      */ 
/*      */   public Rectangle getCellRect(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 2926 */     Rectangle localRectangle = new Rectangle();
/* 2927 */     int i = 1;
/* 2928 */     if (paramInt1 < 0)
/*      */     {
/* 2930 */       i = 0;
/*      */     }
/* 2932 */     else if (paramInt1 >= getRowCount()) {
/* 2933 */       localRectangle.y = getHeight();
/* 2934 */       i = 0;
/*      */     }
/*      */     else {
/* 2937 */       localRectangle.height = getRowHeight(paramInt1);
/* 2938 */       localRectangle.y = (this.rowModel == null ? paramInt1 * localRectangle.height : this.rowModel.getPosition(paramInt1));
/*      */     }
/*      */     int k;
/* 2941 */     if (paramInt2 < 0) {
/* 2942 */       if (!getComponentOrientation().isLeftToRight()) {
/* 2943 */         localRectangle.x = getWidth();
/*      */       }
/*      */ 
/* 2946 */       i = 0;
/*      */     }
/* 2948 */     else if (paramInt2 >= getColumnCount()) {
/* 2949 */       if (getComponentOrientation().isLeftToRight()) {
/* 2950 */         localRectangle.x = getWidth();
/*      */       }
/*      */ 
/* 2953 */       i = 0;
/*      */     }
/*      */     else {
/* 2956 */       TableColumnModel localTableColumnModel = getColumnModel();
/* 2957 */       if (getComponentOrientation().isLeftToRight()) {
/* 2958 */         for (k = 0; k < paramInt2; k++)
/* 2959 */           localRectangle.x += localTableColumnModel.getColumn(k).getWidth();
/*      */       }
/*      */       else {
/* 2962 */         for (k = localTableColumnModel.getColumnCount() - 1; k > paramInt2; k--) {
/* 2963 */           localRectangle.x += localTableColumnModel.getColumn(k).getWidth();
/*      */         }
/*      */       }
/* 2966 */       localRectangle.width = localTableColumnModel.getColumn(paramInt2).getWidth();
/*      */     }
/*      */ 
/* 2969 */     if ((i != 0) && (!paramBoolean))
/*      */     {
/* 2972 */       int j = Math.min(getRowMargin(), localRectangle.height);
/* 2973 */       k = Math.min(getColumnModel().getColumnMargin(), localRectangle.width);
/*      */ 
/* 2975 */       localRectangle.setBounds(localRectangle.x + k / 2, localRectangle.y + j / 2, localRectangle.width - k, localRectangle.height - j);
/*      */     }
/* 2977 */     return localRectangle;
/*      */   }
/*      */ 
/*      */   private int viewIndexForColumn(TableColumn paramTableColumn) {
/* 2981 */     TableColumnModel localTableColumnModel = getColumnModel();
/* 2982 */     for (int i = 0; i < localTableColumnModel.getColumnCount(); i++) {
/* 2983 */       if (localTableColumnModel.getColumn(i) == paramTableColumn) {
/* 2984 */         return i;
/*      */       }
/*      */     }
/* 2987 */     return -1;
/*      */   }
/*      */ 
/*      */   public void doLayout()
/*      */   {
/* 3126 */     TableColumn localTableColumn = getResizingColumn();
/* 3127 */     if (localTableColumn == null) {
/* 3128 */       setWidthsFromPreferredWidths(false);
/*      */     }
/*      */     else
/*      */     {
/* 3137 */       int i = viewIndexForColumn(localTableColumn);
/* 3138 */       int j = getWidth() - getColumnModel().getTotalColumnWidth();
/* 3139 */       accommodateDelta(i, j);
/* 3140 */       j = getWidth() - getColumnModel().getTotalColumnWidth();
/*      */ 
/* 3152 */       if (j != 0) {
/* 3153 */         localTableColumn.setWidth(localTableColumn.getWidth() + j);
/*      */       }
/*      */ 
/* 3162 */       setWidthsFromPreferredWidths(true);
/*      */     }
/*      */ 
/* 3165 */     super.doLayout();
/*      */   }
/*      */ 
/*      */   private TableColumn getResizingColumn() {
/* 3169 */     return this.tableHeader == null ? null : this.tableHeader.getResizingColumn();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void sizeColumnsToFit(boolean paramBoolean)
/*      */   {
/* 3181 */     int i = this.autoResizeMode;
/* 3182 */     setAutoResizeMode(paramBoolean ? 3 : 4);
/*      */ 
/* 3184 */     sizeColumnsToFit(-1);
/* 3185 */     setAutoResizeMode(i);
/*      */   }
/*      */ 
/*      */   public void sizeColumnsToFit(int paramInt)
/*      */   {
/* 3196 */     if (paramInt == -1) {
/* 3197 */       setWidthsFromPreferredWidths(false);
/*      */     }
/* 3200 */     else if (this.autoResizeMode == 0) {
/* 3201 */       TableColumn localTableColumn = getColumnModel().getColumn(paramInt);
/* 3202 */       localTableColumn.setPreferredWidth(localTableColumn.getWidth());
/*      */     }
/*      */     else {
/* 3205 */       int i = getWidth() - getColumnModel().getTotalColumnWidth();
/* 3206 */       accommodateDelta(paramInt, i);
/* 3207 */       setWidthsFromPreferredWidths(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setWidthsFromPreferredWidths(final boolean paramBoolean)
/*      */   {
/* 3213 */     int i = getWidth();
/* 3214 */     int j = getPreferredSize().width;
/* 3215 */     int k = !paramBoolean ? i : j;
/*      */ 
/* 3217 */     final TableColumnModel localTableColumnModel = this.columnModel;
/* 3218 */     Resizable3 local2 = new Resizable3() {
/* 3219 */       public int getElementCount() { return localTableColumnModel.getColumnCount(); } 
/* 3220 */       public int getLowerBoundAt(int paramAnonymousInt) { return localTableColumnModel.getColumn(paramAnonymousInt).getMinWidth(); } 
/* 3221 */       public int getUpperBoundAt(int paramAnonymousInt) { return localTableColumnModel.getColumn(paramAnonymousInt).getMaxWidth(); } 
/*      */       public int getMidPointAt(int paramAnonymousInt) {
/* 3223 */         if (!paramBoolean) {
/* 3224 */           return localTableColumnModel.getColumn(paramAnonymousInt).getPreferredWidth();
/*      */         }
/*      */ 
/* 3227 */         return localTableColumnModel.getColumn(paramAnonymousInt).getWidth();
/*      */       }
/*      */ 
/*      */       public void setSizeAt(int paramAnonymousInt1, int paramAnonymousInt2) {
/* 3231 */         if (!paramBoolean) {
/* 3232 */           localTableColumnModel.getColumn(paramAnonymousInt2).setWidth(paramAnonymousInt1);
/*      */         }
/*      */         else
/* 3235 */           localTableColumnModel.getColumn(paramAnonymousInt2).setPreferredWidth(paramAnonymousInt1);
/*      */       }
/*      */     };
/* 3240 */     adjustSizes(k, local2, paramBoolean);
/*      */   }
/*      */ 
/*      */   private void accommodateDelta(int paramInt1, int paramInt2)
/*      */   {
/* 3246 */     int i = getColumnCount();
/* 3247 */     int j = paramInt1;
/*      */     int k;
/* 3251 */     switch (this.autoResizeMode) {
/*      */     case 1:
/* 3253 */       j += 1;
/* 3254 */       k = Math.min(j + 1, i); break;
/*      */     case 2:
/* 3256 */       j += 1;
/* 3257 */       k = i; break;
/*      */     case 3:
/* 3259 */       j = i - 1;
/* 3260 */       k = j + 1; break;
/*      */     case 4:
/* 3262 */       j = 0;
/* 3263 */       k = i; break;
/*      */     default:
/* 3265 */       return;
/*      */     }
/*      */ 
/* 3268 */     final int m = j;
/* 3269 */     final int n = k;
/* 3270 */     final TableColumnModel localTableColumnModel = this.columnModel;
/* 3271 */     Resizable3 local3 = new Resizable3() {
/* 3272 */       public int getElementCount() { return n - m; } 
/* 3273 */       public int getLowerBoundAt(int paramAnonymousInt) { return localTableColumnModel.getColumn(paramAnonymousInt + m).getMinWidth(); } 
/* 3274 */       public int getUpperBoundAt(int paramAnonymousInt) { return localTableColumnModel.getColumn(paramAnonymousInt + m).getMaxWidth(); } 
/* 3275 */       public int getMidPointAt(int paramAnonymousInt) { return localTableColumnModel.getColumn(paramAnonymousInt + m).getWidth(); } 
/* 3276 */       public void setSizeAt(int paramAnonymousInt1, int paramAnonymousInt2) { localTableColumnModel.getColumn(paramAnonymousInt2 + m).setWidth(paramAnonymousInt1); }
/*      */ 
/*      */     };
/* 3279 */     int i1 = 0;
/* 3280 */     for (int i2 = j; i2 < k; i2++) {
/* 3281 */       TableColumn localTableColumn = this.columnModel.getColumn(i2);
/* 3282 */       int i3 = localTableColumn.getWidth();
/* 3283 */       i1 += i3;
/*      */     }
/*      */ 
/* 3286 */     adjustSizes(i1 + paramInt2, local3, false);
/*      */   }
/*      */ 
/*      */   private void adjustSizes(long paramLong, final Resizable3 paramResizable3, boolean paramBoolean)
/*      */   {
/* 3302 */     int i = paramResizable3.getElementCount();
/* 3303 */     long l = 0L;
/* 3304 */     for (int j = 0; j < i; j++)
/* 3305 */       l += paramResizable3.getMidPointAt(j);
/*      */     Object localObject;
/* 3308 */     if ((paramLong < l ? 1 : 0) == (!paramBoolean ? 1 : 0)) {
/* 3309 */       localObject = new Resizable2() {
/* 3310 */         public int getElementCount() { return paramResizable3.getElementCount(); } 
/* 3311 */         public int getLowerBoundAt(int paramAnonymousInt) { return paramResizable3.getLowerBoundAt(paramAnonymousInt); } 
/* 3312 */         public int getUpperBoundAt(int paramAnonymousInt) { return paramResizable3.getMidPointAt(paramAnonymousInt); } 
/* 3313 */         public void setSizeAt(int paramAnonymousInt1, int paramAnonymousInt2) { paramResizable3.setSizeAt(paramAnonymousInt1, paramAnonymousInt2); }
/*      */ 
/*      */       };
/*      */     }
/*      */     else {
/* 3318 */       localObject = new Resizable2() {
/* 3319 */         public int getElementCount() { return paramResizable3.getElementCount(); } 
/* 3320 */         public int getLowerBoundAt(int paramAnonymousInt) { return paramResizable3.getMidPointAt(paramAnonymousInt); } 
/* 3321 */         public int getUpperBoundAt(int paramAnonymousInt) { return paramResizable3.getUpperBoundAt(paramAnonymousInt); } 
/* 3322 */         public void setSizeAt(int paramAnonymousInt1, int paramAnonymousInt2) { paramResizable3.setSizeAt(paramAnonymousInt1, paramAnonymousInt2); }
/*      */ 
/*      */       };
/*      */     }
/* 3326 */     adjustSizes(paramLong, (Resizable2)localObject, !paramBoolean);
/*      */   }
/*      */ 
/*      */   private void adjustSizes(long paramLong, Resizable2 paramResizable2, boolean paramBoolean) {
/* 3330 */     long l1 = 0L;
/* 3331 */     long l2 = 0L;
/* 3332 */     for (int i = 0; i < paramResizable2.getElementCount(); i++) {
/* 3333 */       l1 += paramResizable2.getLowerBoundAt(i);
/* 3334 */       l2 += paramResizable2.getUpperBoundAt(i);
/*      */     }
/*      */ 
/* 3337 */     if (paramBoolean) {
/* 3338 */       paramLong = Math.min(Math.max(l1, paramLong), l2);
/*      */     }
/*      */ 
/* 3341 */     for (i = 0; i < paramResizable2.getElementCount(); i++) {
/* 3342 */       int j = paramResizable2.getLowerBoundAt(i);
/* 3343 */       int k = paramResizable2.getUpperBoundAt(i);
/*      */       int m;
/* 3348 */       if (l1 == l2) {
/* 3349 */         m = j;
/*      */       }
/*      */       else {
/* 3352 */         double d = (paramLong - l1) / (l2 - l1);
/* 3353 */         m = (int)Math.round(j + d * (k - j));
/*      */       }
/*      */ 
/* 3358 */       paramResizable2.setSizeAt(m, i);
/* 3359 */       paramLong -= m;
/* 3360 */       l1 -= j;
/* 3361 */       l2 -= k;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getToolTipText(MouseEvent paramMouseEvent)
/*      */   {
/* 3382 */     String str = null;
/* 3383 */     Point localPoint = paramMouseEvent.getPoint();
/*      */ 
/* 3386 */     int i = columnAtPoint(localPoint);
/* 3387 */     int j = rowAtPoint(localPoint);
/*      */ 
/* 3389 */     if ((i != -1) && (j != -1)) {
/* 3390 */       TableCellRenderer localTableCellRenderer = getCellRenderer(j, i);
/* 3391 */       Component localComponent = prepareRenderer(localTableCellRenderer, j, i);
/*      */ 
/* 3395 */       if ((localComponent instanceof JComponent))
/*      */       {
/* 3397 */         Rectangle localRectangle = getCellRect(j, i, false);
/* 3398 */         localPoint.translate(-localRectangle.x, -localRectangle.y);
/* 3399 */         MouseEvent localMouseEvent = new MouseEvent(localComponent, paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*      */ 
/* 3408 */         str = ((JComponent)localComponent).getToolTipText(localMouseEvent);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3413 */     if (str == null) {
/* 3414 */       str = getToolTipText();
/*      */     }
/* 3416 */     return str;
/*      */   }
/*      */ 
/*      */   public void setSurrendersFocusOnKeystroke(boolean paramBoolean)
/*      */   {
/* 3439 */     this.surrendersFocusOnKeystroke = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getSurrendersFocusOnKeystroke()
/*      */   {
/* 3454 */     return this.surrendersFocusOnKeystroke;
/*      */   }
/*      */ 
/*      */   public boolean editCellAt(int paramInt1, int paramInt2)
/*      */   {
/* 3470 */     return editCellAt(paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */   public boolean editCellAt(int paramInt1, int paramInt2, EventObject paramEventObject)
/*      */   {
/* 3491 */     if ((this.cellEditor != null) && (!this.cellEditor.stopCellEditing())) {
/* 3492 */       return false;
/*      */     }
/*      */ 
/* 3495 */     if ((paramInt1 < 0) || (paramInt1 >= getRowCount()) || (paramInt2 < 0) || (paramInt2 >= getColumnCount()))
/*      */     {
/* 3497 */       return false;
/*      */     }
/*      */ 
/* 3500 */     if (!isCellEditable(paramInt1, paramInt2)) {
/* 3501 */       return false;
/*      */     }
/* 3503 */     if (this.editorRemover == null) {
/* 3504 */       localObject = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */ 
/* 3506 */       this.editorRemover = new CellEditorRemover((KeyboardFocusManager)localObject);
/* 3507 */       ((KeyboardFocusManager)localObject).addPropertyChangeListener("permanentFocusOwner", this.editorRemover);
/*      */     }
/*      */ 
/* 3510 */     Object localObject = getCellEditor(paramInt1, paramInt2);
/* 3511 */     if ((localObject != null) && (((TableCellEditor)localObject).isCellEditable(paramEventObject))) {
/* 3512 */       this.editorComp = prepareEditor((TableCellEditor)localObject, paramInt1, paramInt2);
/* 3513 */       if (this.editorComp == null) {
/* 3514 */         removeEditor();
/* 3515 */         return false;
/*      */       }
/* 3517 */       this.editorComp.setBounds(getCellRect(paramInt1, paramInt2, false));
/* 3518 */       add(this.editorComp);
/* 3519 */       this.editorComp.validate();
/* 3520 */       this.editorComp.repaint();
/*      */ 
/* 3522 */       setCellEditor((TableCellEditor)localObject);
/* 3523 */       setEditingRow(paramInt1);
/* 3524 */       setEditingColumn(paramInt2);
/* 3525 */       ((TableCellEditor)localObject).addCellEditorListener(this);
/*      */ 
/* 3527 */       return true;
/*      */     }
/* 3529 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isEditing()
/*      */   {
/* 3540 */     return this.cellEditor != null;
/*      */   }
/*      */ 
/*      */   public Component getEditorComponent()
/*      */   {
/* 3550 */     return this.editorComp;
/*      */   }
/*      */ 
/*      */   public int getEditingColumn()
/*      */   {
/* 3562 */     return this.editingColumn;
/*      */   }
/*      */ 
/*      */   public int getEditingRow()
/*      */   {
/* 3574 */     return this.editingRow;
/*      */   }
/*      */ 
/*      */   public TableUI getUI()
/*      */   {
/* 3587 */     return (TableUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(TableUI paramTableUI)
/*      */   {
/* 3602 */     if (this.ui != paramTableUI) {
/* 3603 */       super.setUI(paramTableUI);
/* 3604 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/* 3617 */     TableColumnModel localTableColumnModel = getColumnModel();
/* 3618 */     for (int i = 0; i < localTableColumnModel.getColumnCount(); i++) {
/* 3619 */       localObject = localTableColumnModel.getColumn(i);
/* 3620 */       SwingUtilities.updateRendererOrEditorUI(((TableColumn)localObject).getCellRenderer());
/* 3621 */       SwingUtilities.updateRendererOrEditorUI(((TableColumn)localObject).getCellEditor());
/* 3622 */       SwingUtilities.updateRendererOrEditorUI(((TableColumn)localObject).getHeaderRenderer());
/*      */     }
/*      */ 
/* 3626 */     Enumeration localEnumeration = this.defaultRenderersByColumnClass.elements();
/* 3627 */     while (localEnumeration.hasMoreElements()) {
/* 3628 */       SwingUtilities.updateRendererOrEditorUI(localEnumeration.nextElement());
/*      */     }
/*      */ 
/* 3632 */     Object localObject = this.defaultEditorsByColumnClass.elements();
/* 3633 */     while (((Enumeration)localObject).hasMoreElements()) {
/* 3634 */       SwingUtilities.updateRendererOrEditorUI(((Enumeration)localObject).nextElement());
/*      */     }
/*      */ 
/* 3638 */     if ((this.tableHeader != null) && (this.tableHeader.getParent() == null)) {
/* 3639 */       this.tableHeader.updateUI();
/*      */     }
/*      */ 
/* 3643 */     configureEnclosingScrollPaneUI();
/*      */ 
/* 3645 */     setUI((TableUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/* 3657 */     return "TableUI";
/*      */   }
/*      */ 
/*      */   public void setModel(TableModel paramTableModel)
/*      */   {
/* 3677 */     if (paramTableModel == null) {
/* 3678 */       throw new IllegalArgumentException("Cannot set a null TableModel");
/*      */     }
/* 3680 */     if (this.dataModel != paramTableModel) {
/* 3681 */       TableModel localTableModel = this.dataModel;
/* 3682 */       if (localTableModel != null) {
/* 3683 */         localTableModel.removeTableModelListener(this);
/*      */       }
/* 3685 */       this.dataModel = paramTableModel;
/* 3686 */       paramTableModel.addTableModelListener(this);
/*      */ 
/* 3688 */       tableChanged(new TableModelEvent(paramTableModel, -1));
/*      */ 
/* 3690 */       firePropertyChange("model", localTableModel, paramTableModel);
/*      */ 
/* 3692 */       if (getAutoCreateRowSorter())
/* 3693 */         setRowSorter(new TableRowSorter(paramTableModel));
/*      */     }
/*      */   }
/*      */ 
/*      */   public TableModel getModel()
/*      */   {
/* 3706 */     return this.dataModel;
/*      */   }
/*      */ 
/*      */   public void setColumnModel(TableColumnModel paramTableColumnModel)
/*      */   {
/* 3722 */     if (paramTableColumnModel == null) {
/* 3723 */       throw new IllegalArgumentException("Cannot set a null ColumnModel");
/*      */     }
/* 3725 */     TableColumnModel localTableColumnModel = this.columnModel;
/* 3726 */     if (paramTableColumnModel != localTableColumnModel) {
/* 3727 */       if (localTableColumnModel != null) {
/* 3728 */         localTableColumnModel.removeColumnModelListener(this);
/*      */       }
/* 3730 */       this.columnModel = paramTableColumnModel;
/* 3731 */       paramTableColumnModel.addColumnModelListener(this);
/*      */ 
/* 3734 */       if (this.tableHeader != null) {
/* 3735 */         this.tableHeader.setColumnModel(paramTableColumnModel);
/*      */       }
/*      */ 
/* 3738 */       firePropertyChange("columnModel", localTableColumnModel, paramTableColumnModel);
/* 3739 */       resizeAndRepaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public TableColumnModel getColumnModel()
/*      */   {
/* 3751 */     return this.columnModel;
/*      */   }
/*      */ 
/*      */   public void setSelectionModel(ListSelectionModel paramListSelectionModel)
/*      */   {
/* 3766 */     if (paramListSelectionModel == null) {
/* 3767 */       throw new IllegalArgumentException("Cannot set a null SelectionModel");
/*      */     }
/*      */ 
/* 3770 */     ListSelectionModel localListSelectionModel = this.selectionModel;
/*      */ 
/* 3772 */     if (paramListSelectionModel != localListSelectionModel) {
/* 3773 */       if (localListSelectionModel != null) {
/* 3774 */         localListSelectionModel.removeListSelectionListener(this);
/*      */       }
/*      */ 
/* 3777 */       this.selectionModel = paramListSelectionModel;
/* 3778 */       paramListSelectionModel.addListSelectionListener(this);
/*      */ 
/* 3780 */       firePropertyChange("selectionModel", localListSelectionModel, paramListSelectionModel);
/* 3781 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ListSelectionModel getSelectionModel()
/*      */   {
/* 3794 */     return this.selectionModel;
/*      */   }
/*      */ 
/*      */   public void sorterChanged(RowSorterEvent paramRowSorterEvent)
/*      */   {
/* 3810 */     if (paramRowSorterEvent.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
/* 3811 */       JTableHeader localJTableHeader = getTableHeader();
/* 3812 */       if (localJTableHeader != null) {
/* 3813 */         localJTableHeader.repaint();
/*      */       }
/*      */     }
/* 3816 */     else if (paramRowSorterEvent.getType() == RowSorterEvent.Type.SORTED) {
/* 3817 */       this.sorterChanged = true;
/* 3818 */       if (!this.ignoreSortChange)
/* 3819 */         sortedTableChanged(paramRowSorterEvent, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void sortedTableChanged(RowSorterEvent paramRowSorterEvent, TableModelEvent paramTableModelEvent)
/*      */   {
/* 4103 */     int i = -1;
/* 4104 */     ModelChange localModelChange = paramTableModelEvent != null ? new ModelChange(paramTableModelEvent) : null;
/*      */ 
/* 4106 */     if (((localModelChange == null) || (!localModelChange.allRowsChanged)) && (this.editingRow != -1))
/*      */     {
/* 4108 */       i = convertRowIndexToModel(paramRowSorterEvent, this.editingRow);
/*      */     }
/*      */ 
/* 4112 */     this.sortManager.prepareForChange(paramRowSorterEvent, localModelChange);
/*      */ 
/* 4114 */     if (paramTableModelEvent != null) {
/* 4115 */       if (localModelChange.type == 0) {
/* 4116 */         repaintSortedRows(localModelChange);
/*      */       }
/* 4118 */       notifySorter(localModelChange);
/* 4119 */       if (localModelChange.type != 0)
/*      */       {
/* 4122 */         this.sorterChanged = true;
/*      */       }
/*      */     }
/*      */     else {
/* 4126 */       this.sorterChanged = true;
/*      */     }
/*      */ 
/* 4129 */     this.sortManager.processChange(paramRowSorterEvent, localModelChange, this.sorterChanged);
/*      */ 
/* 4131 */     if (this.sorterChanged)
/*      */     {
/* 4133 */       if (this.editingRow != -1) {
/* 4134 */         int j = i == -1 ? -1 : convertRowIndexToView(i, localModelChange);
/*      */ 
/* 4136 */         restoreSortingEditingRow(j);
/*      */       }
/*      */ 
/* 4140 */       if ((paramTableModelEvent == null) || (localModelChange.type != 0)) {
/* 4141 */         resizeAndRepaint();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4146 */     if ((localModelChange != null) && (localModelChange.allRowsChanged)) {
/* 4147 */       clearSelectionAndLeadAnchor();
/* 4148 */       resizeAndRepaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void repaintSortedRows(ModelChange paramModelChange)
/*      */   {
/* 4156 */     if ((paramModelChange.startModelIndex > paramModelChange.endModelIndex) || (paramModelChange.startModelIndex + 10 < paramModelChange.endModelIndex))
/*      */     {
/* 4159 */       repaint();
/* 4160 */       return;
/*      */     }
/* 4162 */     int i = paramModelChange.event.getColumn();
/* 4163 */     int j = i;
/* 4164 */     if (j == -1) {
/* 4165 */       j = 0;
/*      */     }
/*      */     else {
/* 4168 */       j = convertColumnIndexToView(j);
/* 4169 */       if (j == -1) {
/* 4170 */         return;
/*      */       }
/*      */     }
/* 4173 */     int k = paramModelChange.startModelIndex;
/* 4174 */     while (k <= paramModelChange.endModelIndex) {
/* 4175 */       int m = convertRowIndexToView(k++);
/* 4176 */       if (m != -1) {
/* 4177 */         Rectangle localRectangle = getCellRect(m, j, false);
/*      */ 
/* 4179 */         int n = localRectangle.x;
/* 4180 */         int i1 = localRectangle.width;
/* 4181 */         if (i == -1) {
/* 4182 */           n = 0;
/* 4183 */           i1 = getWidth();
/*      */         }
/* 4185 */         repaint(n, localRectangle.y, i1, localRectangle.height);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void restoreSortingSelection(int[] paramArrayOfInt, int paramInt, ModelChange paramModelChange)
/*      */   {
/* 4197 */     for (int i = paramArrayOfInt.length - 1; i >= 0; i--) {
/* 4198 */       paramArrayOfInt[i] = convertRowIndexToView(paramArrayOfInt[i], paramModelChange);
/*      */     }
/* 4200 */     paramInt = convertRowIndexToView(paramInt, paramModelChange);
/*      */ 
/* 4203 */     if ((paramArrayOfInt.length == 0) || ((paramArrayOfInt.length == 1) && (paramArrayOfInt[0] == getSelectedRow())))
/*      */     {
/* 4205 */       return;
/*      */     }
/*      */ 
/* 4209 */     this.selectionModel.setValueIsAdjusting(true);
/* 4210 */     this.selectionModel.clearSelection();
/* 4211 */     for (i = paramArrayOfInt.length - 1; i >= 0; i--) {
/* 4212 */       if (paramArrayOfInt[i] != -1) {
/* 4213 */         this.selectionModel.addSelectionInterval(paramArrayOfInt[i], paramArrayOfInt[i]);
/*      */       }
/*      */     }
/*      */ 
/* 4217 */     SwingUtilities2.setLeadAnchorWithoutSelection(this.selectionModel, paramInt, paramInt);
/*      */ 
/* 4219 */     this.selectionModel.setValueIsAdjusting(false);
/*      */   }
/*      */ 
/*      */   private void restoreSortingEditingRow(int paramInt)
/*      */   {
/* 4228 */     if (paramInt == -1)
/*      */     {
/* 4230 */       TableCellEditor localTableCellEditor = getCellEditor();
/* 4231 */       if (localTableCellEditor != null)
/*      */       {
/* 4233 */         localTableCellEditor.cancelCellEditing();
/* 4234 */         if (getCellEditor() != null)
/*      */         {
/* 4237 */           removeEditor();
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 4243 */       this.editingRow = paramInt;
/* 4244 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void notifySorter(ModelChange paramModelChange)
/*      */   {
/*      */     try
/*      */     {
/* 4253 */       this.ignoreSortChange = true;
/* 4254 */       this.sorterChanged = false;
/* 4255 */       switch (paramModelChange.type) {
/*      */       case 0:
/* 4257 */         if (paramModelChange.event.getLastRow() == 2147483647)
/* 4258 */           this.sortManager.sorter.allRowsChanged();
/* 4259 */         else if (paramModelChange.event.getColumn() == -1)
/*      */         {
/* 4261 */           this.sortManager.sorter.rowsUpdated(paramModelChange.startModelIndex, paramModelChange.endModelIndex);
/*      */         }
/*      */         else {
/* 4264 */           this.sortManager.sorter.rowsUpdated(paramModelChange.startModelIndex, paramModelChange.endModelIndex, paramModelChange.event.getColumn());
/*      */         }
/*      */ 
/* 4268 */         break;
/*      */       case 1:
/* 4270 */         this.sortManager.sorter.rowsInserted(paramModelChange.startModelIndex, paramModelChange.endModelIndex);
/*      */ 
/* 4272 */         break;
/*      */       case -1:
/* 4274 */         this.sortManager.sorter.rowsDeleted(paramModelChange.startModelIndex, paramModelChange.endModelIndex);
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 4279 */       this.ignoreSortChange = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private int convertRowIndexToView(int paramInt, ModelChange paramModelChange)
/*      */   {
/* 4291 */     if (paramInt < 0) {
/* 4292 */       return -1;
/*      */     }
/* 4294 */     if ((paramModelChange != null) && (paramInt >= paramModelChange.startModelIndex)) {
/* 4295 */       if (paramModelChange.type == 1) {
/* 4296 */         if (paramInt + paramModelChange.length >= paramModelChange.modelRowCount) {
/* 4297 */           return -1;
/*      */         }
/* 4299 */         return this.sortManager.sorter.convertRowIndexToView(paramInt + paramModelChange.length);
/*      */       }
/*      */ 
/* 4302 */       if (paramModelChange.type == -1) {
/* 4303 */         if (paramInt <= paramModelChange.endModelIndex)
/*      */         {
/* 4305 */           return -1;
/*      */         }
/*      */ 
/* 4308 */         if (paramInt - paramModelChange.length >= paramModelChange.modelRowCount) {
/* 4309 */           return -1;
/*      */         }
/* 4311 */         return this.sortManager.sorter.convertRowIndexToView(paramInt - paramModelChange.length);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4317 */     if (paramInt >= getModel().getRowCount()) {
/* 4318 */       return -1;
/*      */     }
/* 4320 */     return this.sortManager.sorter.convertRowIndexToView(paramInt);
/*      */   }
/*      */ 
/*      */   private int[] convertSelectionToModel(RowSorterEvent paramRowSorterEvent)
/*      */   {
/* 4328 */     int[] arrayOfInt = getSelectedRows();
/* 4329 */     for (int i = arrayOfInt.length - 1; i >= 0; i--) {
/* 4330 */       arrayOfInt[i] = convertRowIndexToModel(paramRowSorterEvent, arrayOfInt[i]);
/*      */     }
/* 4332 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private int convertRowIndexToModel(RowSorterEvent paramRowSorterEvent, int paramInt) {
/* 4336 */     if (paramRowSorterEvent != null) {
/* 4337 */       if (paramRowSorterEvent.getPreviousRowCount() == 0) {
/* 4338 */         return paramInt;
/*      */       }
/*      */ 
/* 4341 */       return paramRowSorterEvent.convertPreviousRowIndexToModel(paramInt);
/*      */     }
/*      */ 
/* 4344 */     if ((paramInt < 0) || (paramInt >= getRowCount())) {
/* 4345 */       return -1;
/*      */     }
/* 4347 */     return convertRowIndexToModel(paramInt);
/*      */   }
/*      */ 
/*      */   public void tableChanged(TableModelEvent paramTableModelEvent)
/*      */   {
/* 4368 */     if ((paramTableModelEvent == null) || (paramTableModelEvent.getFirstRow() == -1))
/*      */     {
/* 4370 */       clearSelectionAndLeadAnchor();
/*      */ 
/* 4372 */       this.rowModel = null;
/*      */ 
/* 4374 */       if (this.sortManager != null) {
/*      */         try {
/* 4376 */           this.ignoreSortChange = true;
/* 4377 */           this.sortManager.sorter.modelStructureChanged();
/*      */         } finally {
/* 4379 */           this.ignoreSortChange = false;
/*      */         }
/* 4381 */         this.sortManager.allChanged();
/*      */       }
/*      */ 
/* 4384 */       if (getAutoCreateColumnsFromModel())
/*      */       {
/* 4386 */         createDefaultColumnsFromModel();
/* 4387 */         return;
/*      */       }
/*      */ 
/* 4390 */       resizeAndRepaint();
/* 4391 */       return;
/*      */     }
/*      */ 
/* 4394 */     if (this.sortManager != null) {
/* 4395 */       sortedTableChanged(null, paramTableModelEvent);
/* 4396 */       return;
/*      */     }
/*      */ 
/* 4402 */     if (this.rowModel != null) {
/* 4403 */       repaint();
/*      */     }
/*      */ 
/* 4406 */     if (paramTableModelEvent.getType() == 1) {
/* 4407 */       tableRowsInserted(paramTableModelEvent);
/* 4408 */       return;
/*      */     }
/*      */ 
/* 4411 */     if (paramTableModelEvent.getType() == -1) {
/* 4412 */       tableRowsDeleted(paramTableModelEvent);
/* 4413 */       return;
/*      */     }
/*      */ 
/* 4416 */     int i = paramTableModelEvent.getColumn();
/* 4417 */     int j = paramTableModelEvent.getFirstRow();
/* 4418 */     int k = paramTableModelEvent.getLastRow();
/*      */     Rectangle localRectangle;
/* 4421 */     if (i == -1)
/*      */     {
/* 4423 */       localRectangle = new Rectangle(0, j * getRowHeight(), getColumnModel().getTotalColumnWidth(), 0);
/*      */     }
/*      */     else
/*      */     {
/* 4432 */       int m = convertColumnIndexToView(i);
/* 4433 */       localRectangle = getCellRect(j, m, false);
/*      */     }
/*      */ 
/* 4438 */     if (k != 2147483647) {
/* 4439 */       localRectangle.height = ((k - j + 1) * getRowHeight());
/* 4440 */       repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     }
/*      */     else
/*      */     {
/* 4445 */       clearSelectionAndLeadAnchor();
/* 4446 */       resizeAndRepaint();
/* 4447 */       this.rowModel = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void tableRowsInserted(TableModelEvent paramTableModelEvent)
/*      */   {
/* 4460 */     int i = paramTableModelEvent.getFirstRow();
/* 4461 */     int j = paramTableModelEvent.getLastRow();
/* 4462 */     if (i < 0) {
/* 4463 */       i = 0;
/*      */     }
/* 4465 */     if (j < 0) {
/* 4466 */       j = getRowCount() - 1;
/*      */     }
/*      */ 
/* 4470 */     int k = j - i + 1;
/* 4471 */     this.selectionModel.insertIndexInterval(i, k, true);
/*      */ 
/* 4474 */     if (this.rowModel != null) {
/* 4475 */       this.rowModel.insertEntries(i, k, getRowHeight());
/*      */     }
/* 4477 */     int m = getRowHeight();
/* 4478 */     Rectangle localRectangle = new Rectangle(0, i * m, getColumnModel().getTotalColumnWidth(), (getRowCount() - i) * m);
/*      */ 
/* 4482 */     revalidate();
/*      */ 
/* 4485 */     repaint(localRectangle);
/*      */   }
/*      */ 
/*      */   private void tableRowsDeleted(TableModelEvent paramTableModelEvent)
/*      */   {
/* 4497 */     int i = paramTableModelEvent.getFirstRow();
/* 4498 */     int j = paramTableModelEvent.getLastRow();
/* 4499 */     if (i < 0) {
/* 4500 */       i = 0;
/*      */     }
/* 4502 */     if (j < 0) {
/* 4503 */       j = getRowCount() - 1;
/*      */     }
/*      */ 
/* 4506 */     int k = j - i + 1;
/* 4507 */     int m = getRowCount() + k;
/*      */ 
/* 4509 */     this.selectionModel.removeIndexInterval(i, j);
/*      */ 
/* 4512 */     if (this.rowModel != null) {
/* 4513 */       this.rowModel.removeEntries(i, k);
/*      */     }
/*      */ 
/* 4516 */     int n = getRowHeight();
/* 4517 */     Rectangle localRectangle = new Rectangle(0, i * n, getColumnModel().getTotalColumnWidth(), (m - i) * n);
/*      */ 
/* 4521 */     revalidate();
/*      */ 
/* 4524 */     repaint(localRectangle);
/*      */   }
/*      */ 
/*      */   public void columnAdded(TableColumnModelEvent paramTableColumnModelEvent)
/*      */   {
/* 4541 */     if (isEditing()) {
/* 4542 */       removeEditor();
/*      */     }
/* 4544 */     resizeAndRepaint();
/*      */   }
/*      */ 
/*      */   public void columnRemoved(TableColumnModelEvent paramTableColumnModelEvent)
/*      */   {
/* 4557 */     if (isEditing()) {
/* 4558 */       removeEditor();
/*      */     }
/* 4560 */     resizeAndRepaint();
/*      */   }
/*      */ 
/*      */   public void columnMoved(TableColumnModelEvent paramTableColumnModelEvent)
/*      */   {
/* 4574 */     if ((isEditing()) && (!getCellEditor().stopCellEditing())) {
/* 4575 */       getCellEditor().cancelCellEditing();
/*      */     }
/* 4577 */     repaint();
/*      */   }
/*      */ 
/*      */   public void columnMarginChanged(ChangeEvent paramChangeEvent)
/*      */   {
/* 4592 */     if ((isEditing()) && (!getCellEditor().stopCellEditing())) {
/* 4593 */       getCellEditor().cancelCellEditing();
/*      */     }
/* 4595 */     TableColumn localTableColumn = getResizingColumn();
/*      */ 
/* 4598 */     if ((localTableColumn != null) && (this.autoResizeMode == 0)) {
/* 4599 */       localTableColumn.setPreferredWidth(localTableColumn.getWidth());
/*      */     }
/* 4601 */     resizeAndRepaint();
/*      */   }
/*      */ 
/*      */   private int limit(int paramInt1, int paramInt2, int paramInt3) {
/* 4605 */     return Math.min(paramInt3, Math.max(paramInt1, paramInt2));
/*      */   }
/*      */ 
/*      */   public void columnSelectionChanged(ListSelectionEvent paramListSelectionEvent)
/*      */   {
/* 4619 */     boolean bool = paramListSelectionEvent.getValueIsAdjusting();
/* 4620 */     if ((this.columnSelectionAdjusting) && (!bool))
/*      */     {
/* 4624 */       this.columnSelectionAdjusting = false;
/* 4625 */       return;
/*      */     }
/* 4627 */     this.columnSelectionAdjusting = bool;
/*      */ 
/* 4629 */     if ((getRowCount() <= 0) || (getColumnCount() <= 0)) {
/* 4630 */       return;
/*      */     }
/* 4632 */     int i = limit(paramListSelectionEvent.getFirstIndex(), 0, getColumnCount() - 1);
/* 4633 */     int j = limit(paramListSelectionEvent.getLastIndex(), 0, getColumnCount() - 1);
/* 4634 */     int k = 0;
/* 4635 */     int m = getRowCount() - 1;
/* 4636 */     if (getRowSelectionAllowed()) {
/* 4637 */       k = this.selectionModel.getMinSelectionIndex();
/* 4638 */       m = this.selectionModel.getMaxSelectionIndex();
/* 4639 */       int n = getAdjustedIndex(this.selectionModel.getLeadSelectionIndex(), true);
/*      */ 
/* 4641 */       if ((k == -1) || (m == -1)) {
/* 4642 */         if (n == -1)
/*      */         {
/* 4644 */           return;
/*      */         }
/*      */ 
/* 4648 */         k = m = n;
/*      */       }
/* 4653 */       else if (n != -1) {
/* 4654 */         k = Math.min(k, n);
/* 4655 */         m = Math.max(m, n);
/*      */       }
/*      */     }
/*      */ 
/* 4659 */     Rectangle localRectangle1 = getCellRect(k, i, false);
/* 4660 */     Rectangle localRectangle2 = getCellRect(m, j, false);
/* 4661 */     Rectangle localRectangle3 = localRectangle1.union(localRectangle2);
/* 4662 */     repaint(localRectangle3);
/*      */   }
/*      */ 
/*      */   public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */   {
/* 4680 */     if (this.sortManager != null) {
/* 4681 */       this.sortManager.viewSelectionChanged(paramListSelectionEvent);
/*      */     }
/* 4683 */     boolean bool = paramListSelectionEvent.getValueIsAdjusting();
/* 4684 */     if ((this.rowSelectionAdjusting) && (!bool))
/*      */     {
/* 4688 */       this.rowSelectionAdjusting = false;
/* 4689 */       return;
/*      */     }
/* 4691 */     this.rowSelectionAdjusting = bool;
/*      */ 
/* 4693 */     if ((getRowCount() <= 0) || (getColumnCount() <= 0)) {
/* 4694 */       return;
/*      */     }
/* 4696 */     int i = limit(paramListSelectionEvent.getFirstIndex(), 0, getRowCount() - 1);
/* 4697 */     int j = limit(paramListSelectionEvent.getLastIndex(), 0, getRowCount() - 1);
/* 4698 */     Rectangle localRectangle1 = getCellRect(i, 0, false);
/* 4699 */     Rectangle localRectangle2 = getCellRect(j, getColumnCount() - 1, false);
/* 4700 */     Rectangle localRectangle3 = localRectangle1.union(localRectangle2);
/* 4701 */     repaint(localRectangle3);
/*      */   }
/*      */ 
/*      */   public void editingStopped(ChangeEvent paramChangeEvent)
/*      */   {
/* 4720 */     TableCellEditor localTableCellEditor = getCellEditor();
/* 4721 */     if (localTableCellEditor != null) {
/* 4722 */       Object localObject = localTableCellEditor.getCellEditorValue();
/* 4723 */       setValueAt(localObject, this.editingRow, this.editingColumn);
/* 4724 */       removeEditor();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void editingCanceled(ChangeEvent paramChangeEvent)
/*      */   {
/* 4739 */     removeEditor();
/*      */   }
/*      */ 
/*      */   public void setPreferredScrollableViewportSize(Dimension paramDimension)
/*      */   {
/* 4756 */     this.preferredViewportSize = paramDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredScrollableViewportSize()
/*      */   {
/* 4767 */     return this.preferredViewportSize;
/*      */   }
/*      */ 
/*      */   public int getScrollableUnitIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 4795 */     int i = getLeadingRow(paramRectangle);
/* 4796 */     int j = getLeadingCol(paramRectangle);
/* 4797 */     if ((paramInt1 == 1) && (i < 0))
/*      */     {
/* 4799 */       return getRowHeight();
/*      */     }
/* 4801 */     if ((paramInt1 == 0) && (j < 0))
/*      */     {
/* 4803 */       return 100;
/*      */     }
/*      */ 
/* 4809 */     Rectangle localRectangle = getCellRect(i, j, true);
/* 4810 */     int k = leadingEdge(paramRectangle, paramInt1);
/* 4811 */     int m = leadingEdge(localRectangle, paramInt1);
/*      */     int n;
/* 4813 */     if (paramInt1 == 1) {
/* 4814 */       n = localRectangle.height;
/*      */     }
/*      */     else
/*      */     {
/* 4818 */       n = localRectangle.width;
/*      */     }
/*      */ 
/* 4827 */     if (k == m)
/*      */     {
/* 4830 */       if (paramInt2 < 0) {
/* 4831 */         i1 = 0;
/*      */ 
/* 4833 */         if (paramInt1 == 1) {
/*      */           do {
/* 4835 */             i--; if (i < 0) break;
/* 4836 */             i1 = getRowHeight(i);
/* 4837 */           }while (i1 == 0);
/*      */         }
/*      */         else
/*      */         {
/*      */           while (true)
/*      */           {
/* 4844 */             j--; if (j >= 0) {
/* 4845 */               i1 = getCellRect(i, j, true).width;
/* 4846 */               if (i1 != 0)
/* 4847 */                 break;
/*      */             }
/*      */           }
/*      */         }
/* 4851 */         return i1;
/*      */       }
/*      */ 
/* 4854 */       return n;
/*      */     }
/*      */ 
/* 4859 */     int i1 = Math.abs(k - m);
/* 4860 */     int i2 = n - i1;
/*      */ 
/* 4862 */     if (paramInt2 > 0)
/*      */     {
/* 4864 */       return i2;
/*      */     }
/*      */ 
/* 4867 */     return i1;
/*      */   }
/*      */ 
/*      */   public int getScrollableBlockIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/*      */     int i;
/* 4888 */     if (getRowCount() == 0)
/*      */     {
/* 4890 */       if (1 == paramInt1) {
/* 4891 */         i = getRowHeight();
/* 4892 */         return i > 0 ? Math.max(i, paramRectangle.height / i * i) : paramRectangle.height;
/*      */       }
/*      */ 
/* 4896 */       return paramRectangle.width;
/*      */     }
/*      */ 
/* 4900 */     if ((null == this.rowModel) && (1 == paramInt1)) {
/* 4901 */       i = rowAtPoint(paramRectangle.getLocation());
/* 4902 */       assert (i != -1);
/* 4903 */       int j = columnAtPoint(paramRectangle.getLocation());
/* 4904 */       Rectangle localRectangle = getCellRect(i, j, true);
/*      */ 
/* 4906 */       if (localRectangle.y == paramRectangle.y) {
/* 4907 */         int k = getRowHeight();
/* 4908 */         assert (k > 0);
/* 4909 */         return Math.max(k, paramRectangle.height / k * k);
/*      */       }
/*      */     }
/* 4912 */     if (paramInt2 < 0) {
/* 4913 */       return getPreviousBlockIncrement(paramRectangle, paramInt1);
/*      */     }
/*      */ 
/* 4916 */     return getNextBlockIncrement(paramRectangle, paramInt1);
/*      */   }
/*      */ 
/*      */   private int getPreviousBlockIncrement(Rectangle paramRectangle, int paramInt)
/*      */   {
/* 4937 */     int m = leadingEdge(paramRectangle, paramInt);
/* 4938 */     boolean bool = getComponentOrientation().isLeftToRight();
/*      */     int k;
/*      */     Point localPoint;
/* 4944 */     if (paramInt == 1) {
/* 4945 */       k = m - paramRectangle.height;
/* 4946 */       int i1 = paramRectangle.x + (bool ? 0 : paramRectangle.width);
/* 4947 */       localPoint = new Point(i1, k);
/*      */     }
/* 4949 */     else if (bool) {
/* 4950 */       k = m - paramRectangle.width;
/* 4951 */       localPoint = new Point(k, paramRectangle.y);
/*      */     }
/*      */     else {
/* 4954 */       k = m + paramRectangle.width;
/* 4955 */       localPoint = new Point(k - 1, paramRectangle.y);
/*      */     }
/* 4957 */     int i = rowAtPoint(localPoint);
/* 4958 */     int j = columnAtPoint(localPoint);
/*      */     int n;
/* 4962 */     if (((paramInt == 1 ? 1 : 0) & (i < 0 ? 1 : 0)) != 0) {
/* 4963 */       n = 0;
/*      */     }
/* 4965 */     else if (((paramInt == 0 ? 1 : 0) & (j < 0 ? 1 : 0)) != 0) {
/* 4966 */       if (bool) {
/* 4967 */         n = 0;
/*      */       }
/*      */       else {
/* 4970 */         n = getWidth();
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 4975 */       Rectangle localRectangle = getCellRect(i, j, true);
/* 4976 */       int i2 = leadingEdge(localRectangle, paramInt);
/* 4977 */       int i3 = trailingEdge(localRectangle, paramInt);
/*      */ 
/* 4989 */       if (((paramInt == 1) || (bool)) && (i3 >= m))
/*      */       {
/* 4991 */         n = i2;
/*      */       }
/* 4993 */       else if ((paramInt == 0) && (!bool) && (i3 <= m))
/*      */       {
/* 4996 */         n = i2;
/*      */       }
/* 4999 */       else if (k == i2) {
/* 5000 */         n = i2;
/*      */       }
/*      */       else
/*      */       {
/* 5004 */         n = i3;
/*      */       }
/*      */     }
/* 5007 */     return Math.abs(m - n);
/*      */   }
/*      */ 
/*      */   private int getNextBlockIncrement(Rectangle paramRectangle, int paramInt)
/*      */   {
/* 5019 */     int i = getTrailingRow(paramRectangle);
/* 5020 */     int j = getTrailingCol(paramRectangle);
/*      */ 
/* 5028 */     int i2 = leadingEdge(paramRectangle, paramInt);
/*      */ 
/* 5036 */     if ((paramInt == 1) && (i < 0)) {
/* 5037 */       return paramRectangle.height;
/*      */     }
/* 5039 */     if ((paramInt == 0) && (j < 0)) {
/* 5040 */       return paramRectangle.width;
/*      */     }
/* 5042 */     Rectangle localRectangle = getCellRect(i, j, true);
/* 5043 */     int m = leadingEdge(localRectangle, paramInt);
/* 5044 */     int n = trailingEdge(localRectangle, paramInt);
/*      */     int k;
/* 5046 */     if ((paramInt == 1) || (getComponentOrientation().isLeftToRight()))
/*      */     {
/* 5048 */       k = m <= i2 ? 1 : 0;
/*      */     }
/*      */     else
/* 5051 */       k = m >= i2 ? 1 : 0;
/*      */     int i1;
/* 5054 */     if (k != 0)
/*      */     {
/* 5057 */       i1 = n;
/*      */     }
/* 5059 */     else if (n == trailingEdge(paramRectangle, paramInt))
/*      */     {
/* 5062 */       i1 = n;
/*      */     }
/*      */     else
/*      */     {
/* 5068 */       i1 = m;
/*      */     }
/* 5070 */     return Math.abs(i1 - i2);
/*      */   }
/*      */ 
/*      */   private int getLeadingRow(Rectangle paramRectangle)
/*      */   {
/*      */     Point localPoint;
/* 5081 */     if (getComponentOrientation().isLeftToRight()) {
/* 5082 */       localPoint = new Point(paramRectangle.x, paramRectangle.y);
/*      */     }
/*      */     else {
/* 5085 */       localPoint = new Point(paramRectangle.x + paramRectangle.width - 1, paramRectangle.y);
/*      */     }
/*      */ 
/* 5088 */     return rowAtPoint(localPoint);
/*      */   }
/*      */ 
/*      */   private int getLeadingCol(Rectangle paramRectangle)
/*      */   {
/*      */     Point localPoint;
/* 5099 */     if (getComponentOrientation().isLeftToRight()) {
/* 5100 */       localPoint = new Point(paramRectangle.x, paramRectangle.y);
/*      */     }
/*      */     else {
/* 5103 */       localPoint = new Point(paramRectangle.x + paramRectangle.width - 1, paramRectangle.y);
/*      */     }
/*      */ 
/* 5106 */     return columnAtPoint(localPoint);
/*      */   }
/*      */ 
/*      */   private int getTrailingRow(Rectangle paramRectangle)
/*      */   {
/*      */     Point localPoint;
/* 5117 */     if (getComponentOrientation().isLeftToRight()) {
/* 5118 */       localPoint = new Point(paramRectangle.x, paramRectangle.y + paramRectangle.height - 1);
/*      */     }
/*      */     else
/*      */     {
/* 5122 */       localPoint = new Point(paramRectangle.x + paramRectangle.width - 1, paramRectangle.y + paramRectangle.height - 1);
/*      */     }
/*      */ 
/* 5125 */     return rowAtPoint(localPoint);
/*      */   }
/*      */ 
/*      */   private int getTrailingCol(Rectangle paramRectangle)
/*      */   {
/*      */     Point localPoint;
/* 5136 */     if (getComponentOrientation().isLeftToRight()) {
/* 5137 */       localPoint = new Point(paramRectangle.x + paramRectangle.width - 1, paramRectangle.y);
/*      */     }
/*      */     else
/*      */     {
/* 5141 */       localPoint = new Point(paramRectangle.x, paramRectangle.y);
/*      */     }
/* 5143 */     return columnAtPoint(localPoint);
/*      */   }
/*      */ 
/*      */   private int leadingEdge(Rectangle paramRectangle, int paramInt)
/*      */   {
/* 5152 */     if (paramInt == 1) {
/* 5153 */       return paramRectangle.y;
/*      */     }
/* 5155 */     if (getComponentOrientation().isLeftToRight()) {
/* 5156 */       return paramRectangle.x;
/*      */     }
/*      */ 
/* 5159 */     return paramRectangle.x + paramRectangle.width;
/*      */   }
/*      */ 
/*      */   private int trailingEdge(Rectangle paramRectangle, int paramInt)
/*      */   {
/* 5169 */     if (paramInt == 1) {
/* 5170 */       return paramRectangle.y + paramRectangle.height;
/*      */     }
/* 5172 */     if (getComponentOrientation().isLeftToRight()) {
/* 5173 */       return paramRectangle.x + paramRectangle.width;
/*      */     }
/*      */ 
/* 5176 */     return paramRectangle.x;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportWidth()
/*      */   {
/* 5191 */     return this.autoResizeMode != 0;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportHeight()
/*      */   {
/* 5208 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 5209 */     return (getFillsViewportHeight()) && ((localContainer instanceof JViewport)) && (localContainer.getHeight() > getPreferredSize().height);
/*      */   }
/*      */ 
/*      */   public void setFillsViewportHeight(boolean paramBoolean)
/*      */   {
/* 5234 */     boolean bool = this.fillsViewportHeight;
/* 5235 */     this.fillsViewportHeight = paramBoolean;
/* 5236 */     resizeAndRepaint();
/* 5237 */     firePropertyChange("fillsViewportHeight", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getFillsViewportHeight()
/*      */   {
/* 5250 */     return this.fillsViewportHeight;
/*      */   }
/*      */ 
/*      */   protected boolean processKeyBinding(KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean)
/*      */   {
/* 5259 */     boolean bool = super.processKeyBinding(paramKeyStroke, paramKeyEvent, paramInt, paramBoolean);
/*      */ 
/* 5263 */     if ((!bool) && (paramInt == 1) && (isFocusOwner()) && (!Boolean.FALSE.equals(getClientProperty("JTable.autoStartsEdit"))))
/*      */     {
/* 5267 */       Component localComponent = getEditorComponent();
/* 5268 */       if (localComponent == null)
/*      */       {
/* 5270 */         if ((paramKeyEvent == null) || (paramKeyEvent.getID() != 401)) {
/* 5271 */           return false;
/*      */         }
/*      */ 
/* 5274 */         int i = paramKeyEvent.getKeyCode();
/* 5275 */         if ((i == 16) || (i == 17) || (i == 18))
/*      */         {
/* 5277 */           return false;
/*      */         }
/*      */ 
/* 5280 */         int j = getSelectionModel().getLeadSelectionIndex();
/* 5281 */         int k = getColumnModel().getSelectionModel().getLeadSelectionIndex();
/*      */ 
/* 5283 */         if ((j != -1) && (k != -1) && (!isEditing()) && 
/* 5284 */           (!editCellAt(j, k, paramKeyEvent))) {
/* 5285 */           return false;
/*      */         }
/*      */ 
/* 5288 */         localComponent = getEditorComponent();
/* 5289 */         if (localComponent == null) {
/* 5290 */           return false;
/*      */         }
/*      */       }
/*      */ 
/* 5294 */       if ((localComponent instanceof JComponent)) {
/* 5295 */         bool = ((JComponent)localComponent).processKeyBinding(paramKeyStroke, paramKeyEvent, 0, paramBoolean);
/*      */ 
/* 5300 */         if (getSurrendersFocusOnKeystroke()) {
/* 5301 */           localComponent.requestFocus();
/*      */         }
/*      */       }
/*      */     }
/* 5305 */     return bool;
/*      */   }
/*      */ 
/*      */   private void setLazyValue(Hashtable paramHashtable, Class paramClass, String paramString) {
/* 5309 */     paramHashtable.put(paramClass, new SwingLazyValue(paramString));
/*      */   }
/*      */ 
/*      */   private void setLazyRenderer(Class paramClass, String paramString) {
/* 5313 */     setLazyValue(this.defaultRenderersByColumnClass, paramClass, paramString);
/*      */   }
/*      */ 
/*      */   protected void createDefaultRenderers()
/*      */   {
/* 5323 */     this.defaultRenderersByColumnClass = new UIDefaults(8, 0.75F);
/*      */ 
/* 5326 */     setLazyRenderer(Object.class, "javax.swing.table.DefaultTableCellRenderer$UIResource");
/*      */ 
/* 5329 */     setLazyRenderer(Number.class, "javax.swing.JTable$NumberRenderer");
/*      */ 
/* 5332 */     setLazyRenderer(Float.class, "javax.swing.JTable$DoubleRenderer");
/* 5333 */     setLazyRenderer(Double.class, "javax.swing.JTable$DoubleRenderer");
/*      */ 
/* 5336 */     setLazyRenderer(Date.class, "javax.swing.JTable$DateRenderer");
/*      */ 
/* 5339 */     setLazyRenderer(Icon.class, "javax.swing.JTable$IconRenderer");
/* 5340 */     setLazyRenderer(ImageIcon.class, "javax.swing.JTable$IconRenderer");
/*      */ 
/* 5343 */     setLazyRenderer(Boolean.class, "javax.swing.JTable$BooleanRenderer");
/*      */   }
/*      */ 
/*      */   private void setLazyEditor(Class paramClass, String paramString)
/*      */   {
/* 5422 */     setLazyValue(this.defaultEditorsByColumnClass, paramClass, paramString);
/*      */   }
/*      */ 
/*      */   protected void createDefaultEditors()
/*      */   {
/* 5430 */     this.defaultEditorsByColumnClass = new UIDefaults(3, 0.75F);
/*      */ 
/* 5433 */     setLazyEditor(Object.class, "javax.swing.JTable$GenericEditor");
/*      */ 
/* 5436 */     setLazyEditor(Number.class, "javax.swing.JTable$NumberEditor");
/*      */ 
/* 5439 */     setLazyEditor(Boolean.class, "javax.swing.JTable$BooleanEditor");
/*      */   }
/*      */ 
/*      */   protected void initializeLocalVars()
/*      */   {
/* 5531 */     this.updateSelectionOnSort = true;
/* 5532 */     setOpaque(true);
/* 5533 */     createDefaultRenderers();
/* 5534 */     createDefaultEditors();
/*      */ 
/* 5536 */     setTableHeader(createDefaultTableHeader());
/*      */ 
/* 5538 */     setShowGrid(true);
/* 5539 */     setAutoResizeMode(2);
/* 5540 */     setRowHeight(16);
/* 5541 */     this.isRowHeightSet = false;
/* 5542 */     setRowMargin(1);
/* 5543 */     setRowSelectionAllowed(true);
/* 5544 */     setCellEditor(null);
/* 5545 */     setEditingColumn(-1);
/* 5546 */     setEditingRow(-1);
/* 5547 */     setSurrendersFocusOnKeystroke(false);
/* 5548 */     setPreferredScrollableViewportSize(new Dimension(450, 400));
/*      */ 
/* 5551 */     ToolTipManager localToolTipManager = ToolTipManager.sharedInstance();
/* 5552 */     localToolTipManager.registerComponent(this);
/*      */ 
/* 5554 */     setAutoscrolls(true);
/*      */   }
/*      */ 
/*      */   protected TableModel createDefaultDataModel()
/*      */   {
/* 5566 */     return new DefaultTableModel();
/*      */   }
/*      */ 
/*      */   protected TableColumnModel createDefaultColumnModel()
/*      */   {
/* 5578 */     return new DefaultTableColumnModel();
/*      */   }
/*      */ 
/*      */   protected ListSelectionModel createDefaultSelectionModel()
/*      */   {
/* 5590 */     return new DefaultListSelectionModel();
/*      */   }
/*      */ 
/*      */   protected JTableHeader createDefaultTableHeader()
/*      */   {
/* 5602 */     return new JTableHeader(this.columnModel);
/*      */   }
/*      */ 
/*      */   protected void resizeAndRepaint()
/*      */   {
/* 5609 */     revalidate();
/* 5610 */     repaint();
/*      */   }
/*      */ 
/*      */   public TableCellEditor getCellEditor()
/*      */   {
/* 5623 */     return this.cellEditor;
/*      */   }
/*      */ 
/*      */   public void setCellEditor(TableCellEditor paramTableCellEditor)
/*      */   {
/* 5636 */     TableCellEditor localTableCellEditor = this.cellEditor;
/* 5637 */     this.cellEditor = paramTableCellEditor;
/* 5638 */     firePropertyChange("tableCellEditor", localTableCellEditor, paramTableCellEditor);
/*      */   }
/*      */ 
/*      */   public void setEditingColumn(int paramInt)
/*      */   {
/* 5648 */     this.editingColumn = paramInt;
/*      */   }
/*      */ 
/*      */   public void setEditingRow(int paramInt)
/*      */   {
/* 5658 */     this.editingRow = paramInt;
/*      */   }
/*      */ 
/*      */   public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2)
/*      */   {
/* 5684 */     TableColumn localTableColumn = getColumnModel().getColumn(paramInt2);
/* 5685 */     TableCellRenderer localTableCellRenderer = localTableColumn.getCellRenderer();
/* 5686 */     if (localTableCellRenderer == null) {
/* 5687 */       localTableCellRenderer = getDefaultRenderer(getColumnClass(paramInt2));
/*      */     }
/* 5689 */     return localTableCellRenderer;
/*      */   }
/*      */ 
/*      */   public Component prepareRenderer(TableCellRenderer paramTableCellRenderer, int paramInt1, int paramInt2)
/*      */   {
/* 5719 */     Object localObject = getValueAt(paramInt1, paramInt2);
/*      */ 
/* 5721 */     boolean bool1 = false;
/* 5722 */     boolean bool2 = false;
/*      */ 
/* 5725 */     if (!isPaintingForPrint()) {
/* 5726 */       bool1 = isCellSelected(paramInt1, paramInt2);
/*      */ 
/* 5728 */       int i = this.selectionModel.getLeadSelectionIndex() == paramInt1 ? 1 : 0;
/*      */ 
/* 5730 */       int j = this.columnModel.getSelectionModel().getLeadSelectionIndex() == paramInt2 ? 1 : 0;
/*      */ 
/* 5733 */       bool2 = (i != 0) && (j != 0) && (isFocusOwner());
/*      */     }
/*      */ 
/* 5736 */     return paramTableCellRenderer.getTableCellRendererComponent(this, localObject, bool1, bool2, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public TableCellEditor getCellEditor(int paramInt1, int paramInt2)
/*      */   {
/* 5763 */     TableColumn localTableColumn = getColumnModel().getColumn(paramInt2);
/* 5764 */     TableCellEditor localTableCellEditor = localTableColumn.getCellEditor();
/* 5765 */     if (localTableCellEditor == null) {
/* 5766 */       localTableCellEditor = getDefaultEditor(getColumnClass(paramInt2));
/*      */     }
/* 5768 */     return localTableCellEditor;
/*      */   }
/*      */ 
/*      */   public Component prepareEditor(TableCellEditor paramTableCellEditor, int paramInt1, int paramInt2)
/*      */   {
/* 5789 */     Object localObject = getValueAt(paramInt1, paramInt2);
/* 5790 */     boolean bool = isCellSelected(paramInt1, paramInt2);
/* 5791 */     Component localComponent = paramTableCellEditor.getTableCellEditorComponent(this, localObject, bool, paramInt1, paramInt2);
/*      */ 
/* 5793 */     if ((localComponent instanceof JComponent)) {
/* 5794 */       JComponent localJComponent = (JComponent)localComponent;
/* 5795 */       if (localJComponent.getNextFocusableComponent() == null) {
/* 5796 */         localJComponent.setNextFocusableComponent(this);
/*      */       }
/*      */     }
/* 5799 */     return localComponent;
/*      */   }
/*      */ 
/*      */   public void removeEditor()
/*      */   {
/* 5807 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("permanentFocusOwner", this.editorRemover);
/*      */ 
/* 5809 */     this.editorRemover = null;
/*      */ 
/* 5811 */     TableCellEditor localTableCellEditor = getCellEditor();
/* 5812 */     if (localTableCellEditor != null) {
/* 5813 */       localTableCellEditor.removeCellEditorListener(this);
/* 5814 */       if (this.editorComp != null) {
/* 5815 */         localObject = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */ 
/* 5817 */         int i = localObject != null ? SwingUtilities.isDescendingFrom((Component)localObject, this) : 0;
/*      */ 
/* 5819 */         remove(this.editorComp);
/* 5820 */         if (i != 0) {
/* 5821 */           requestFocusInWindow();
/*      */         }
/*      */       }
/*      */ 
/* 5825 */       Object localObject = getCellRect(this.editingRow, this.editingColumn, false);
/*      */ 
/* 5827 */       setCellEditor(null);
/* 5828 */       setEditingColumn(-1);
/* 5829 */       setEditingRow(-1);
/* 5830 */       this.editorComp = null;
/*      */ 
/* 5832 */       repaint((Rectangle)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 5845 */     paramObjectOutputStream.defaultWriteObject();
/* 5846 */     if (getUIClassID().equals("TableUI")) {
/* 5847 */       byte b = JComponent.getWriteObjCounter(this);
/* 5848 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 5849 */       if ((b == 0) && (this.ui != null))
/* 5850 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 5858 */     paramObjectInputStream.defaultReadObject();
/* 5859 */     if ((this.ui != null) && (getUIClassID().equals("TableUI"))) {
/* 5860 */       this.ui.installUI(this);
/*      */     }
/* 5862 */     createDefaultRenderers();
/* 5863 */     createDefaultEditors();
/*      */ 
/* 5868 */     if (getToolTipText() == null)
/* 5869 */       ToolTipManager.sharedInstance().registerComponent(this);
/*      */   }
/*      */ 
/*      */   void compWriteObjectNotify()
/*      */   {
/* 5877 */     super.compWriteObjectNotify();
/*      */ 
/* 5880 */     if (getToolTipText() == null)
/* 5881 */       ToolTipManager.sharedInstance().unregisterComponent(this);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 5895 */     String str1 = this.gridColor != null ? this.gridColor.toString() : "";
/*      */ 
/* 5897 */     String str2 = this.showHorizontalLines ? "true" : "false";
/*      */ 
/* 5899 */     String str3 = this.showVerticalLines ? "true" : "false";
/*      */     String str4;
/* 5902 */     if (this.autoResizeMode == 0)
/* 5903 */       str4 = "AUTO_RESIZE_OFF";
/* 5904 */     else if (this.autoResizeMode == 1)
/* 5905 */       str4 = "AUTO_RESIZE_NEXT_COLUMN";
/* 5906 */     else if (this.autoResizeMode == 2)
/* 5907 */       str4 = "AUTO_RESIZE_SUBSEQUENT_COLUMNS";
/* 5908 */     else if (this.autoResizeMode == 3)
/* 5909 */       str4 = "AUTO_RESIZE_LAST_COLUMN";
/* 5910 */     else if (this.autoResizeMode == 4)
/* 5911 */       str4 = "AUTO_RESIZE_ALL_COLUMNS";
/* 5912 */     else str4 = "";
/* 5913 */     String str5 = this.autoCreateColumnsFromModel ? "true" : "false";
/*      */ 
/* 5915 */     String str6 = this.preferredViewportSize != null ? this.preferredViewportSize.toString() : "";
/*      */ 
/* 5918 */     String str7 = this.rowSelectionAllowed ? "true" : "false";
/*      */ 
/* 5920 */     String str8 = this.cellSelectionEnabled ? "true" : "false";
/*      */ 
/* 5922 */     String str9 = this.selectionForeground != null ? this.selectionForeground.toString() : "";
/*      */ 
/* 5925 */     String str10 = this.selectionBackground != null ? this.selectionBackground.toString() : "";
/*      */ 
/* 5929 */     return super.paramString() + ",autoCreateColumnsFromModel=" + str5 + ",autoResizeMode=" + str4 + ",cellSelectionEnabled=" + str8 + ",editingColumn=" + this.editingColumn + ",editingRow=" + this.editingRow + ",gridColor=" + str1 + ",preferredViewportSize=" + str6 + ",rowHeight=" + this.rowHeight + ",rowMargin=" + this.rowMargin + ",rowSelectionAllowed=" + str7 + ",selectionBackground=" + str10 + ",selectionForeground=" + str9 + ",showHorizontalLines=" + str2 + ",showVerticalLines=" + str3;
/*      */   }
/*      */ 
/*      */   public boolean print()
/*      */     throws PrinterException
/*      */   {
/* 6007 */     return print(PrintMode.FIT_WIDTH);
/*      */   }
/*      */ 
/*      */   public boolean print(PrintMode paramPrintMode)
/*      */     throws PrinterException
/*      */   {
/* 6033 */     return print(paramPrintMode, null, null);
/*      */   }
/*      */ 
/*      */   public boolean print(PrintMode paramPrintMode, MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2)
/*      */     throws PrinterException
/*      */   {
/* 6067 */     boolean bool = !GraphicsEnvironment.isHeadless();
/* 6068 */     return print(paramPrintMode, paramMessageFormat1, paramMessageFormat2, bool, null, bool);
/*      */   }
/*      */ 
/*      */   public boolean print(PrintMode paramPrintMode, MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2, boolean paramBoolean1, PrintRequestAttributeSet paramPrintRequestAttributeSet, boolean paramBoolean2)
/*      */     throws PrinterException, HeadlessException
/*      */   {
/* 6113 */     return print(paramPrintMode, paramMessageFormat1, paramMessageFormat2, paramBoolean1, paramPrintRequestAttributeSet, paramBoolean2, null);
/*      */   }
/*      */ 
/*      */   public boolean print(PrintMode paramPrintMode, MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2, boolean paramBoolean1, PrintRequestAttributeSet paramPrintRequestAttributeSet, boolean paramBoolean2, PrintService paramPrintService)
/*      */     throws PrinterException, HeadlessException
/*      */   {
/* 6210 */     boolean bool = GraphicsEnvironment.isHeadless();
/* 6211 */     if (bool) {
/* 6212 */       if (paramBoolean1) {
/* 6213 */         throw new HeadlessException("Can't show print dialog.");
/*      */       }
/*      */ 
/* 6216 */       if (paramBoolean2) {
/* 6217 */         throw new HeadlessException("Can't run interactively.");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 6224 */     final PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
/*      */ 
/* 6226 */     if (isEditing())
/*      */     {
/* 6228 */       if (!getCellEditor().stopCellEditing()) {
/* 6229 */         getCellEditor().cancelCellEditing();
/*      */       }
/*      */     }
/*      */ 
/* 6233 */     if (paramPrintRequestAttributeSet == null) {
/* 6234 */       paramPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
/*      */     }
/*      */ 
/* 6240 */     Object localObject1 = getPrintable(paramPrintMode, paramMessageFormat1, paramMessageFormat2);
/*      */     final PrintingStatus localPrintingStatus;
/* 6243 */     if (paramBoolean2)
/*      */     {
/* 6245 */       localObject1 = new ThreadSafePrintable((Printable)localObject1);
/* 6246 */       localPrintingStatus = PrintingStatus.createPrintingStatus(this, localPrinterJob);
/* 6247 */       localObject1 = localPrintingStatus.createNotificationPrintable((Printable)localObject1);
/*      */     }
/*      */     else {
/* 6250 */       localPrintingStatus = null;
/*      */     }
/*      */ 
/* 6254 */     localPrinterJob.setPrintable((Printable)localObject1);
/*      */ 
/* 6257 */     if (paramPrintService != null) {
/* 6258 */       localPrinterJob.setPrintService(paramPrintService);
/*      */     }
/*      */ 
/* 6262 */     if ((paramBoolean1) && (!localPrinterJob.printDialog(paramPrintRequestAttributeSet)))
/*      */     {
/* 6264 */       return false;
/*      */     }
/*      */ 
/* 6268 */     if (!paramBoolean2)
/*      */     {
/* 6270 */       localPrinterJob.print(paramPrintRequestAttributeSet);
/*      */ 
/* 6273 */       return true;
/*      */     }
/*      */ 
/* 6277 */     this.printError = null;
/*      */ 
/* 6280 */     final Object localObject2 = new Object();
/*      */ 
/* 6283 */     final PrintRequestAttributeSet localPrintRequestAttributeSet = paramPrintRequestAttributeSet;
/*      */ 
/* 6287 */     Runnable local6 = new Runnable()
/*      */     {
/*      */       public void run() {
/*      */         try {
/* 6291 */           localPrinterJob.print(localPrintRequestAttributeSet);
/*      */         }
/*      */         catch (Throwable localThrowable) {
/* 6294 */           synchronized (localObject2) {
/* 6295 */             JTable.this.printError = localThrowable;
/*      */           }
/*      */         }
/*      */         finally {
/* 6299 */           localPrintingStatus.dispose();
/*      */         }
/*      */       }
/*      */     };
/* 6305 */     Thread localThread = new Thread(local6);
/* 6306 */     localThread.start();
/*      */ 
/* 6308 */     localPrintingStatus.showModal(true);
/*      */     Throwable localThrowable;
/* 6312 */     synchronized (localObject2) {
/* 6313 */       localThrowable = this.printError;
/* 6314 */       this.printError = null;
/*      */     }
/*      */ 
/* 6318 */     if (localThrowable != null)
/*      */     {
/* 6321 */       if ((localThrowable instanceof PrinterAbortException))
/* 6322 */         return false;
/* 6323 */       if ((localThrowable instanceof PrinterException))
/* 6324 */         throw ((PrinterException)localThrowable);
/* 6325 */       if ((localThrowable instanceof RuntimeException))
/* 6326 */         throw ((RuntimeException)localThrowable);
/* 6327 */       if ((localThrowable instanceof Error)) {
/* 6328 */         throw ((Error)localThrowable);
/*      */       }
/*      */ 
/* 6332 */       throw new AssertionError(localThrowable);
/*      */     }
/*      */ 
/* 6335 */     return true;
/*      */   }
/*      */ 
/*      */   public Printable getPrintable(PrintMode paramPrintMode, MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2)
/*      */   {
/* 6443 */     return new TablePrintable(this, paramPrintMode, paramMessageFormat1, paramMessageFormat2);
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 6561 */     if (this.accessibleContext == null) {
/* 6562 */       this.accessibleContext = new AccessibleJTable();
/*      */     }
/* 6564 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJTable extends JComponent.AccessibleJComponent
/*      */     implements AccessibleSelection, ListSelectionListener, TableModelListener, TableColumnModelListener, CellEditorListener, PropertyChangeListener, AccessibleExtendedTable
/*      */   {
/*      */     int previousFocusedRow;
/*      */     int previousFocusedCol;
/*      */     private Accessible caption;
/*      */     private Accessible summary;
/*      */     private Accessible[] rowDescription;
/*      */     private Accessible[] columnDescription;
/*      */ 
/*      */     protected AccessibleJTable()
/*      */     {
/* 6599 */       super();
/* 6600 */       JTable.this.addPropertyChangeListener(this);
/* 6601 */       JTable.this.getSelectionModel().addListSelectionListener(this);
/* 6602 */       TableColumnModel localTableColumnModel = JTable.this.getColumnModel();
/* 6603 */       localTableColumnModel.addColumnModelListener(this);
/* 6604 */       localTableColumnModel.getSelectionModel().addListSelectionListener(this);
/* 6605 */       JTable.this.getModel().addTableModelListener(this);
/* 6606 */       this.previousFocusedRow = JTable.this.getSelectionModel().getLeadSelectionIndex();
/*      */ 
/* 6608 */       this.previousFocusedCol = JTable.this.getColumnModel().getSelectionModel().getLeadSelectionIndex();
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 6621 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 6622 */       Object localObject1 = paramPropertyChangeEvent.getOldValue();
/* 6623 */       Object localObject2 = paramPropertyChangeEvent.getNewValue();
/*      */ 
/* 6626 */       if (str.compareTo("model") == 0)
/*      */       {
/* 6628 */         if ((localObject1 != null) && ((localObject1 instanceof TableModel))) {
/* 6629 */           ((TableModel)localObject1).removeTableModelListener(this);
/*      */         }
/* 6631 */         if ((localObject2 != null) && ((localObject2 instanceof TableModel)))
/* 6632 */           ((TableModel)localObject2).addTableModelListener(this);
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject3;
/* 6636 */         if (str.compareTo("selectionModel") == 0)
/*      */         {
/* 6638 */           localObject3 = paramPropertyChangeEvent.getSource();
/* 6639 */           if (localObject3 == JTable.this)
/*      */           {
/* 6641 */             if ((localObject1 != null) && ((localObject1 instanceof ListSelectionModel)))
/*      */             {
/* 6643 */               ((ListSelectionModel)localObject1).removeListSelectionListener(this);
/*      */             }
/* 6645 */             if ((localObject2 != null) && ((localObject2 instanceof ListSelectionModel)))
/*      */             {
/* 6647 */               ((ListSelectionModel)localObject2).addListSelectionListener(this);
/*      */             }
/*      */           }
/* 6650 */           else if (localObject3 == JTable.this.getColumnModel())
/*      */           {
/* 6652 */             if ((localObject1 != null) && ((localObject1 instanceof ListSelectionModel)))
/*      */             {
/* 6654 */               ((ListSelectionModel)localObject1).removeListSelectionListener(this);
/*      */             }
/* 6656 */             if ((localObject2 != null) && ((localObject2 instanceof ListSelectionModel)))
/*      */             {
/* 6658 */               ((ListSelectionModel)localObject2).addListSelectionListener(this);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/* 6667 */         else if (str.compareTo("columnModel") == 0)
/*      */         {
/* 6669 */           if ((localObject1 != null) && ((localObject1 instanceof TableColumnModel))) {
/* 6670 */             localObject3 = (TableColumnModel)localObject1;
/* 6671 */             ((TableColumnModel)localObject3).removeColumnModelListener(this);
/* 6672 */             ((TableColumnModel)localObject3).getSelectionModel().removeListSelectionListener(this);
/*      */           }
/* 6674 */           if ((localObject2 != null) && ((localObject2 instanceof TableColumnModel))) {
/* 6675 */             localObject3 = (TableColumnModel)localObject2;
/* 6676 */             ((TableColumnModel)localObject3).addColumnModelListener(this);
/* 6677 */             ((TableColumnModel)localObject3).getSelectionModel().addListSelectionListener(this);
/*      */           }
/*      */ 
/*      */         }
/* 6681 */         else if (str.compareTo("tableCellEditor") == 0)
/*      */         {
/* 6683 */           if ((localObject1 != null) && ((localObject1 instanceof TableCellEditor))) {
/* 6684 */             ((TableCellEditor)localObject1).removeCellEditorListener(this);
/*      */           }
/* 6686 */           if ((localObject2 != null) && ((localObject2 instanceof TableCellEditor)))
/* 6687 */             ((TableCellEditor)localObject2).addCellEditorListener(this);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void tableChanged(TableModelEvent paramTableModelEvent)
/*      */     {
/* 6742 */       firePropertyChange("AccessibleVisibleData", null, null);
/*      */ 
/* 6744 */       if (paramTableModelEvent != null) {
/* 6745 */         int i = paramTableModelEvent.getColumn();
/* 6746 */         int j = paramTableModelEvent.getColumn();
/* 6747 */         if (i == -1) {
/* 6748 */           i = 0;
/* 6749 */           j = JTable.this.getColumnCount() - 1;
/*      */         }
/*      */ 
/* 6754 */         AccessibleJTableModelChange localAccessibleJTableModelChange = new AccessibleJTableModelChange(paramTableModelEvent.getType(), paramTableModelEvent.getFirstRow(), paramTableModelEvent.getLastRow(), i, j);
/*      */ 
/* 6760 */         firePropertyChange("accessibleTableModelChanged", null, localAccessibleJTableModelChange);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void tableRowsInserted(TableModelEvent paramTableModelEvent)
/*      */     {
/* 6769 */       firePropertyChange("AccessibleVisibleData", null, null);
/*      */ 
/* 6774 */       int i = paramTableModelEvent.getColumn();
/* 6775 */       int j = paramTableModelEvent.getColumn();
/* 6776 */       if (i == -1) {
/* 6777 */         i = 0;
/* 6778 */         j = JTable.this.getColumnCount() - 1;
/*      */       }
/* 6780 */       AccessibleJTableModelChange localAccessibleJTableModelChange = new AccessibleJTableModelChange(paramTableModelEvent.getType(), paramTableModelEvent.getFirstRow(), paramTableModelEvent.getLastRow(), i, j);
/*      */ 
/* 6786 */       firePropertyChange("accessibleTableModelChanged", null, localAccessibleJTableModelChange);
/*      */     }
/*      */ 
/*      */     public void tableRowsDeleted(TableModelEvent paramTableModelEvent)
/*      */     {
/* 6794 */       firePropertyChange("AccessibleVisibleData", null, null);
/*      */ 
/* 6799 */       int i = paramTableModelEvent.getColumn();
/* 6800 */       int j = paramTableModelEvent.getColumn();
/* 6801 */       if (i == -1) {
/* 6802 */         i = 0;
/* 6803 */         j = JTable.this.getColumnCount() - 1;
/*      */       }
/* 6805 */       AccessibleJTableModelChange localAccessibleJTableModelChange = new AccessibleJTableModelChange(paramTableModelEvent.getType(), paramTableModelEvent.getFirstRow(), paramTableModelEvent.getLastRow(), i, j);
/*      */ 
/* 6811 */       firePropertyChange("accessibleTableModelChanged", null, localAccessibleJTableModelChange);
/*      */     }
/*      */ 
/*      */     public void columnAdded(TableColumnModelEvent paramTableColumnModelEvent)
/*      */     {
/* 6819 */       firePropertyChange("AccessibleVisibleData", null, null);
/*      */ 
/* 6824 */       int i = 1;
/* 6825 */       AccessibleJTableModelChange localAccessibleJTableModelChange = new AccessibleJTableModelChange(i, 0, 0, paramTableColumnModelEvent.getFromIndex(), paramTableColumnModelEvent.getToIndex());
/*      */ 
/* 6831 */       firePropertyChange("accessibleTableModelChanged", null, localAccessibleJTableModelChange);
/*      */     }
/*      */ 
/*      */     public void columnRemoved(TableColumnModelEvent paramTableColumnModelEvent)
/*      */     {
/* 6839 */       firePropertyChange("AccessibleVisibleData", null, null);
/*      */ 
/* 6843 */       int i = -1;
/* 6844 */       AccessibleJTableModelChange localAccessibleJTableModelChange = new AccessibleJTableModelChange(i, 0, 0, paramTableColumnModelEvent.getFromIndex(), paramTableColumnModelEvent.getToIndex());
/*      */ 
/* 6850 */       firePropertyChange("accessibleTableModelChanged", null, localAccessibleJTableModelChange);
/*      */     }
/*      */ 
/*      */     public void columnMoved(TableColumnModelEvent paramTableColumnModelEvent)
/*      */     {
/* 6860 */       firePropertyChange("AccessibleVisibleData", null, null);
/*      */ 
/* 6865 */       int i = -1;
/* 6866 */       AccessibleJTableModelChange localAccessibleJTableModelChange1 = new AccessibleJTableModelChange(i, 0, 0, paramTableColumnModelEvent.getFromIndex(), paramTableColumnModelEvent.getFromIndex());
/*      */ 
/* 6872 */       firePropertyChange("accessibleTableModelChanged", null, localAccessibleJTableModelChange1);
/*      */ 
/* 6875 */       int j = 1;
/* 6876 */       AccessibleJTableModelChange localAccessibleJTableModelChange2 = new AccessibleJTableModelChange(j, 0, 0, paramTableColumnModelEvent.getToIndex(), paramTableColumnModelEvent.getToIndex());
/*      */ 
/* 6882 */       firePropertyChange("accessibleTableModelChanged", null, localAccessibleJTableModelChange2);
/*      */     }
/*      */ 
/*      */     public void columnMarginChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 6892 */       firePropertyChange("AccessibleVisibleData", null, null);
/*      */     }
/*      */ 
/*      */     public void columnSelectionChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void editingStopped(ChangeEvent paramChangeEvent)
/*      */     {
/* 6916 */       firePropertyChange("AccessibleVisibleData", null, null);
/*      */     }
/*      */ 
/*      */     public void editingCanceled(ChangeEvent paramChangeEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/* 6934 */       firePropertyChange("AccessibleSelection", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */ 
/* 6939 */       int i = JTable.this.getSelectionModel().getLeadSelectionIndex();
/* 6940 */       int j = JTable.this.getColumnModel().getSelectionModel().getLeadSelectionIndex();
/*      */ 
/* 6943 */       if ((i != this.previousFocusedRow) || (j != this.previousFocusedCol))
/*      */       {
/* 6945 */         Accessible localAccessible1 = getAccessibleAt(this.previousFocusedRow, this.previousFocusedCol);
/* 6946 */         Accessible localAccessible2 = getAccessibleAt(i, j);
/* 6947 */         firePropertyChange("AccessibleActiveDescendant", localAccessible1, localAccessible2);
/*      */ 
/* 6949 */         this.previousFocusedRow = i;
/* 6950 */         this.previousFocusedCol = j;
/*      */       }
/*      */     }
/*      */ 
/*      */     public AccessibleSelection getAccessibleSelection()
/*      */     {
/* 6968 */       return this;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 6979 */       return AccessibleRole.TABLE;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/* 6993 */       int i = JTable.this.columnAtPoint(paramPoint);
/* 6994 */       int j = JTable.this.rowAtPoint(paramPoint);
/*      */ 
/* 6996 */       if ((i != -1) && (j != -1)) {
/* 6997 */         TableColumn localTableColumn = JTable.this.getColumnModel().getColumn(i);
/* 6998 */         TableCellRenderer localTableCellRenderer = localTableColumn.getCellRenderer();
/* 6999 */         if (localTableCellRenderer == null) {
/* 7000 */           localObject = JTable.this.getColumnClass(i);
/* 7001 */           localTableCellRenderer = JTable.this.getDefaultRenderer((Class)localObject);
/*      */         }
/* 7003 */         Object localObject = localTableCellRenderer.getTableCellRendererComponent(JTable.this, null, false, false, j, i);
/*      */ 
/* 7006 */         return new AccessibleJTableCell(JTable.this, j, i, getAccessibleIndexAt(j, i));
/*      */       }
/*      */ 
/* 7009 */       return null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 7020 */       return JTable.this.getColumnCount() * JTable.this.getRowCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 7030 */       if ((paramInt < 0) || (paramInt >= getAccessibleChildrenCount())) {
/* 7031 */         return null;
/*      */       }
/*      */ 
/* 7035 */       int i = getAccessibleColumnAtIndex(paramInt);
/* 7036 */       int j = getAccessibleRowAtIndex(paramInt);
/*      */ 
/* 7038 */       TableColumn localTableColumn = JTable.this.getColumnModel().getColumn(i);
/* 7039 */       TableCellRenderer localTableCellRenderer = localTableColumn.getCellRenderer();
/* 7040 */       if (localTableCellRenderer == null) {
/* 7041 */         localObject = JTable.this.getColumnClass(i);
/* 7042 */         localTableCellRenderer = JTable.this.getDefaultRenderer((Class)localObject);
/*      */       }
/* 7044 */       Object localObject = localTableCellRenderer.getTableCellRendererComponent(JTable.this, null, false, false, j, i);
/*      */ 
/* 7047 */       return new AccessibleJTableCell(JTable.this, j, i, getAccessibleIndexAt(j, i));
/*      */     }
/*      */ 
/*      */     public int getAccessibleSelectionCount()
/*      */     {
/* 7062 */       int i = JTable.this.getSelectedRowCount();
/* 7063 */       int j = JTable.this.getSelectedColumnCount();
/*      */ 
/* 7065 */       if (JTable.this.cellSelectionEnabled) {
/* 7066 */         return i * j;
/*      */       }
/*      */ 
/* 7070 */       if ((JTable.this.getRowSelectionAllowed()) && (JTable.this.getColumnSelectionAllowed()))
/*      */       {
/* 7072 */         return i * JTable.this.getColumnCount() + j * JTable.this.getRowCount() - i * j;
/*      */       }
/*      */ 
/* 7077 */       if (JTable.this.getRowSelectionAllowed()) {
/* 7078 */         return i * JTable.this.getColumnCount();
/*      */       }
/*      */ 
/* 7081 */       if (JTable.this.getColumnSelectionAllowed()) {
/* 7082 */         return j * JTable.this.getRowCount();
/*      */       }
/*      */ 
/* 7085 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleSelection(int paramInt)
/*      */     {
/* 7104 */       if ((paramInt < 0) || (paramInt > getAccessibleSelectionCount())) {
/* 7105 */         return null;
/*      */       }
/*      */ 
/* 7108 */       int i = JTable.this.getSelectedRowCount();
/* 7109 */       int j = JTable.this.getSelectedColumnCount();
/* 7110 */       int[] arrayOfInt1 = JTable.this.getSelectedRows();
/* 7111 */       int[] arrayOfInt2 = JTable.this.getSelectedColumns();
/* 7112 */       int k = JTable.this.getColumnCount();
/* 7113 */       int m = JTable.this.getRowCount();
/*      */       int n;
/*      */       int i1;
/* 7117 */       if (JTable.this.cellSelectionEnabled) {
/* 7118 */         n = arrayOfInt1[(paramInt / j)];
/* 7119 */         i1 = arrayOfInt2[(paramInt % j)];
/* 7120 */         return getAccessibleChild(n * k + i1);
/*      */       }
/*      */ 
/* 7124 */       if ((JTable.this.getRowSelectionAllowed()) && (JTable.this.getColumnSelectionAllowed()))
/*      */       {
/* 7148 */         int i2 = paramInt;
/*      */ 
/* 7151 */         int i3 = arrayOfInt1[0] == 0 ? 0 : 1;
/* 7152 */         int i4 = 0;
/* 7153 */         int i5 = -1;
/* 7154 */         while (i4 < arrayOfInt1.length) {
/* 7155 */           switch (i3)
/*      */           {
/*      */           case 0:
/* 7158 */             if (i2 < k) {
/* 7159 */               i1 = i2 % k;
/* 7160 */               n = arrayOfInt1[i4];
/* 7161 */               return getAccessibleChild(n * k + i1);
/*      */             }
/* 7163 */             i2 -= k;
/*      */ 
/* 7166 */             if ((i4 + 1 == arrayOfInt1.length) || (arrayOfInt1[i4] != arrayOfInt1[(i4 + 1)] - 1))
/*      */             {
/* 7168 */               i3 = 1;
/* 7169 */               i5 = arrayOfInt1[i4];
/*      */             }
/* 7171 */             i4++;
/* 7172 */             break;
/*      */           case 1:
/* 7175 */             if (i2 < j * (arrayOfInt1[i4] - (i5 == -1 ? 0 : i5 + 1)))
/*      */             {
/* 7180 */               i1 = arrayOfInt2[(i2 % j)];
/* 7181 */               n = (i4 > 0 ? arrayOfInt1[(i4 - 1)] + 1 : 0) + i2 / j;
/*      */ 
/* 7183 */               return getAccessibleChild(n * k + i1);
/*      */             }
/* 7185 */             i2 -= j * (arrayOfInt1[i4] - (i5 == -1 ? 0 : i5 + 1));
/*      */ 
/* 7188 */             i3 = 0;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 7194 */         if (i2 < j * (m - (i5 == -1 ? 0 : i5 + 1)))
/*      */         {
/* 7197 */           i1 = arrayOfInt2[(i2 % j)];
/* 7198 */           n = arrayOfInt1[(i4 - 1)] + i2 / j + 1;
/* 7199 */           return getAccessibleChild(n * k + i1);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 7206 */         if (JTable.this.getRowSelectionAllowed()) {
/* 7207 */           i1 = paramInt % k;
/* 7208 */           n = arrayOfInt1[(paramInt / k)];
/* 7209 */           return getAccessibleChild(n * k + i1);
/*      */         }
/*      */ 
/* 7212 */         if (JTable.this.getColumnSelectionAllowed()) {
/* 7213 */           i1 = arrayOfInt2[(paramInt % j)];
/* 7214 */           n = paramInt / j;
/* 7215 */           return getAccessibleChild(n * k + i1);
/*      */         }
/*      */       }
/* 7218 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleChildSelected(int paramInt)
/*      */     {
/* 7230 */       int i = getAccessibleColumnAtIndex(paramInt);
/* 7231 */       int j = getAccessibleRowAtIndex(paramInt);
/* 7232 */       return JTable.this.isCellSelected(j, i);
/*      */     }
/*      */ 
/*      */     public void addAccessibleSelection(int paramInt)
/*      */     {
/* 7251 */       int i = getAccessibleColumnAtIndex(paramInt);
/* 7252 */       int j = getAccessibleRowAtIndex(paramInt);
/* 7253 */       JTable.this.changeSelection(j, i, true, false);
/*      */     }
/*      */ 
/*      */     public void removeAccessibleSelection(int paramInt)
/*      */     {
/* 7268 */       if (JTable.this.cellSelectionEnabled) {
/* 7269 */         int i = getAccessibleColumnAtIndex(paramInt);
/* 7270 */         int j = getAccessibleRowAtIndex(paramInt);
/* 7271 */         JTable.this.removeRowSelectionInterval(j, j);
/* 7272 */         JTable.this.removeColumnSelectionInterval(i, i);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void clearAccessibleSelection()
/*      */     {
/* 7281 */       JTable.this.clearSelection();
/*      */     }
/*      */ 
/*      */     public void selectAllAccessibleSelection()
/*      */     {
/* 7290 */       if (JTable.this.cellSelectionEnabled)
/* 7291 */         JTable.this.selectAll();
/*      */     }
/*      */ 
/*      */     public int getAccessibleRow(int paramInt)
/*      */     {
/* 7306 */       return getAccessibleRowAtIndex(paramInt);
/*      */     }
/*      */ 
/*      */     public int getAccessibleColumn(int paramInt)
/*      */     {
/* 7318 */       return getAccessibleColumnAtIndex(paramInt);
/*      */     }
/*      */ 
/*      */     public int getAccessibleIndex(int paramInt1, int paramInt2)
/*      */     {
/* 7331 */       return getAccessibleIndexAt(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public AccessibleTable getAccessibleTable()
/*      */     {
/* 7354 */       return this;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleCaption()
/*      */     {
/* 7364 */       return this.caption;
/*      */     }
/*      */ 
/*      */     public void setAccessibleCaption(Accessible paramAccessible)
/*      */     {
/* 7374 */       Accessible localAccessible = this.caption;
/* 7375 */       this.caption = paramAccessible;
/* 7376 */       firePropertyChange("accessibleTableCaptionChanged", localAccessible, this.caption);
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleSummary()
/*      */     {
/* 7387 */       return this.summary;
/*      */     }
/*      */ 
/*      */     public void setAccessibleSummary(Accessible paramAccessible)
/*      */     {
/* 7397 */       Accessible localAccessible = this.summary;
/* 7398 */       this.summary = paramAccessible;
/* 7399 */       firePropertyChange("accessibleTableSummaryChanged", localAccessible, this.summary);
/*      */     }
/*      */ 
/*      */     public int getAccessibleRowCount()
/*      */     {
/* 7409 */       return JTable.this.getRowCount();
/*      */     }
/*      */ 
/*      */     public int getAccessibleColumnCount()
/*      */     {
/* 7418 */       return JTable.this.getColumnCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(int paramInt1, int paramInt2)
/*      */     {
/* 7431 */       return getAccessibleChild(paramInt1 * getAccessibleColumnCount() + paramInt2);
/*      */     }
/*      */ 
/*      */     public int getAccessibleRowExtentAt(int paramInt1, int paramInt2)
/*      */     {
/* 7443 */       return 1;
/*      */     }
/*      */ 
/*      */     public int getAccessibleColumnExtentAt(int paramInt1, int paramInt2)
/*      */     {
/* 7455 */       return 1;
/*      */     }
/*      */ 
/*      */     public AccessibleTable getAccessibleRowHeader()
/*      */     {
/* 7467 */       return null;
/*      */     }
/*      */ 
/*      */     public void setAccessibleRowHeader(AccessibleTable paramAccessibleTable)
/*      */     {
/*      */     }
/*      */ 
/*      */     public AccessibleTable getAccessibleColumnHeader()
/*      */     {
/* 7490 */       JTableHeader localJTableHeader = JTable.this.getTableHeader();
/* 7491 */       return localJTableHeader == null ? null : new AccessibleTableHeader(localJTableHeader);
/*      */     }
/*      */ 
/*      */     public void setAccessibleColumnHeader(AccessibleTable paramAccessibleTable)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleRowDescription(int paramInt)
/*      */     {
/* 7739 */       if ((paramInt < 0) || (paramInt >= getAccessibleRowCount())) {
/* 7740 */         throw new IllegalArgumentException(Integer.toString(paramInt));
/*      */       }
/* 7742 */       if (this.rowDescription == null) {
/* 7743 */         return null;
/*      */       }
/* 7745 */       return this.rowDescription[paramInt];
/*      */     }
/*      */ 
/*      */     public void setAccessibleRowDescription(int paramInt, Accessible paramAccessible)
/*      */     {
/* 7757 */       if ((paramInt < 0) || (paramInt >= getAccessibleRowCount())) {
/* 7758 */         throw new IllegalArgumentException(Integer.toString(paramInt));
/*      */       }
/* 7760 */       if (this.rowDescription == null) {
/* 7761 */         int i = getAccessibleRowCount();
/* 7762 */         this.rowDescription = new Accessible[i];
/*      */       }
/* 7764 */       this.rowDescription[paramInt] = paramAccessible;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleColumnDescription(int paramInt)
/*      */     {
/* 7775 */       if ((paramInt < 0) || (paramInt >= getAccessibleColumnCount())) {
/* 7776 */         throw new IllegalArgumentException(Integer.toString(paramInt));
/*      */       }
/* 7778 */       if (this.columnDescription == null) {
/* 7779 */         return null;
/*      */       }
/* 7781 */       return this.columnDescription[paramInt];
/*      */     }
/*      */ 
/*      */     public void setAccessibleColumnDescription(int paramInt, Accessible paramAccessible)
/*      */     {
/* 7793 */       if ((paramInt < 0) || (paramInt >= getAccessibleColumnCount())) {
/* 7794 */         throw new IllegalArgumentException(Integer.toString(paramInt));
/*      */       }
/* 7796 */       if (this.columnDescription == null) {
/* 7797 */         int i = getAccessibleColumnCount();
/* 7798 */         this.columnDescription = new Accessible[i];
/*      */       }
/* 7800 */       this.columnDescription[paramInt] = paramAccessible;
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleSelected(int paramInt1, int paramInt2)
/*      */     {
/* 7814 */       return JTable.this.isCellSelected(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleRowSelected(int paramInt)
/*      */     {
/* 7827 */       return JTable.this.isRowSelected(paramInt);
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleColumnSelected(int paramInt)
/*      */     {
/* 7840 */       return JTable.this.isColumnSelected(paramInt);
/*      */     }
/*      */ 
/*      */     public int[] getSelectedAccessibleRows()
/*      */     {
/* 7851 */       return JTable.this.getSelectedRows();
/*      */     }
/*      */ 
/*      */     public int[] getSelectedAccessibleColumns()
/*      */     {
/* 7862 */       return JTable.this.getSelectedColumns();
/*      */     }
/*      */ 
/*      */     public int getAccessibleRowAtIndex(int paramInt)
/*      */     {
/* 7873 */       int i = getAccessibleColumnCount();
/* 7874 */       if (i == 0) {
/* 7875 */         return -1;
/*      */       }
/* 7877 */       return paramInt / i;
/*      */     }
/*      */ 
/*      */     public int getAccessibleColumnAtIndex(int paramInt)
/*      */     {
/* 7889 */       int i = getAccessibleColumnCount();
/* 7890 */       if (i == 0) {
/* 7891 */         return -1;
/*      */       }
/* 7893 */       return paramInt % i;
/*      */     }
/*      */ 
/*      */     public int getAccessibleIndexAt(int paramInt1, int paramInt2)
/*      */     {
/* 7906 */       return paramInt1 * getAccessibleColumnCount() + paramInt2;
/*      */     }
/*      */ 
/*      */     protected class AccessibleJTableCell extends AccessibleContext
/*      */       implements Accessible, AccessibleComponent
/*      */     {
/*      */       private JTable parent;
/*      */       private int row;
/*      */       private int column;
/*      */       private int index;
/*      */ 
/*      */       public AccessibleJTableCell(JTable paramInt1, int paramInt2, int paramInt3, int arg5)
/*      */       {
/* 7928 */         this.parent = paramInt1;
/* 7929 */         this.row = paramInt2;
/* 7930 */         this.column = paramInt3;
/*      */         int i;
/* 7931 */         this.index = i;
/* 7932 */         setAccessibleParent(this.parent);
/*      */       }
/*      */ 
/*      */       public AccessibleContext getAccessibleContext()
/*      */       {
/* 7944 */         return this;
/*      */       }
/*      */ 
/*      */       protected AccessibleContext getCurrentAccessibleContext()
/*      */       {
/* 7956 */         TableColumn localTableColumn = JTable.this.getColumnModel().getColumn(this.column);
/* 7957 */         TableCellRenderer localTableCellRenderer = localTableColumn.getCellRenderer();
/* 7958 */         if (localTableCellRenderer == null) {
/* 7959 */           localObject = JTable.this.getColumnClass(this.column);
/* 7960 */           localTableCellRenderer = JTable.this.getDefaultRenderer((Class)localObject);
/*      */         }
/* 7962 */         Object localObject = localTableCellRenderer.getTableCellRendererComponent(JTable.this, JTable.this.getValueAt(this.row, this.column), false, false, this.row, this.column);
/*      */ 
/* 7965 */         if ((localObject instanceof AccessibleComponent)) {
/* 7966 */           return ((Component)localObject).getAccessibleContext();
/*      */         }
/* 7968 */         return null;
/*      */       }
/*      */ 
/*      */       protected Component getCurrentComponent()
/*      */       {
/* 7980 */         TableColumn localTableColumn = JTable.this.getColumnModel().getColumn(this.column);
/* 7981 */         TableCellRenderer localTableCellRenderer = localTableColumn.getCellRenderer();
/* 7982 */         if (localTableCellRenderer == null) {
/* 7983 */           Class localClass = JTable.this.getColumnClass(this.column);
/* 7984 */           localTableCellRenderer = JTable.this.getDefaultRenderer(localClass);
/*      */         }
/* 7986 */         return localTableCellRenderer.getTableCellRendererComponent(JTable.this, null, false, false, this.row, this.column);
/*      */       }
/*      */ 
/*      */       public String getAccessibleName()
/*      */       {
/* 8000 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8001 */         if (localAccessibleContext != null) {
/* 8002 */           String str = localAccessibleContext.getAccessibleName();
/* 8003 */           if ((str != null) && (str != ""))
/*      */           {
/* 8005 */             return str;
/*      */           }
/*      */         }
/* 8008 */         if ((this.accessibleName != null) && (this.accessibleName != "")) {
/* 8009 */           return this.accessibleName;
/*      */         }
/*      */ 
/* 8012 */         return (String)JTable.this.getClientProperty("AccessibleName");
/*      */       }
/*      */ 
/*      */       public void setAccessibleName(String paramString)
/*      */       {
/* 8022 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8023 */         if (localAccessibleContext != null)
/* 8024 */           localAccessibleContext.setAccessibleName(paramString);
/*      */         else
/* 8026 */           super.setAccessibleName(paramString);
/*      */       }
/*      */ 
/*      */       public String getAccessibleDescription()
/*      */       {
/* 8041 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8042 */         if (localAccessibleContext != null) {
/* 8043 */           return localAccessibleContext.getAccessibleDescription();
/*      */         }
/* 8045 */         return super.getAccessibleDescription();
/*      */       }
/*      */ 
/*      */       public void setAccessibleDescription(String paramString)
/*      */       {
/* 8055 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8056 */         if (localAccessibleContext != null)
/* 8057 */           localAccessibleContext.setAccessibleDescription(paramString);
/*      */         else
/* 8059 */           super.setAccessibleDescription(paramString);
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/* 8071 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8072 */         if (localAccessibleContext != null) {
/* 8073 */           return localAccessibleContext.getAccessibleRole();
/*      */         }
/* 8075 */         return AccessibleRole.UNKNOWN;
/*      */       }
/*      */ 
/*      */       public AccessibleStateSet getAccessibleStateSet()
/*      */       {
/* 8087 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8088 */         AccessibleStateSet localAccessibleStateSet = null;
/*      */ 
/* 8090 */         if (localAccessibleContext != null) {
/* 8091 */           localAccessibleStateSet = localAccessibleContext.getAccessibleStateSet();
/*      */         }
/* 8093 */         if (localAccessibleStateSet == null) {
/* 8094 */           localAccessibleStateSet = new AccessibleStateSet();
/*      */         }
/* 8096 */         Rectangle localRectangle1 = JTable.this.getVisibleRect();
/* 8097 */         Rectangle localRectangle2 = JTable.this.getCellRect(this.row, this.column, false);
/* 8098 */         if (localRectangle1.intersects(localRectangle2)) {
/* 8099 */           localAccessibleStateSet.add(AccessibleState.SHOWING);
/*      */         }
/* 8101 */         else if (localAccessibleStateSet.contains(AccessibleState.SHOWING)) {
/* 8102 */           localAccessibleStateSet.remove(AccessibleState.SHOWING);
/*      */         }
/*      */ 
/* 8105 */         if (this.parent.isCellSelected(this.row, this.column))
/* 8106 */           localAccessibleStateSet.add(AccessibleState.SELECTED);
/* 8107 */         else if (localAccessibleStateSet.contains(AccessibleState.SELECTED)) {
/* 8108 */           localAccessibleStateSet.remove(AccessibleState.SELECTED);
/*      */         }
/* 8110 */         if ((this.row == JTable.this.getSelectedRow()) && (this.column == JTable.this.getSelectedColumn())) {
/* 8111 */           localAccessibleStateSet.add(AccessibleState.ACTIVE);
/*      */         }
/* 8113 */         localAccessibleStateSet.add(AccessibleState.TRANSIENT);
/* 8114 */         return localAccessibleStateSet;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleParent()
/*      */       {
/* 8125 */         return this.parent;
/*      */       }
/*      */ 
/*      */       public int getAccessibleIndexInParent()
/*      */       {
/* 8136 */         return this.index;
/*      */       }
/*      */ 
/*      */       public int getAccessibleChildrenCount()
/*      */       {
/* 8145 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8146 */         if (localAccessibleContext != null) {
/* 8147 */           return localAccessibleContext.getAccessibleChildrenCount();
/*      */         }
/* 8149 */         return 0;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleChild(int paramInt)
/*      */       {
/* 8161 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8162 */         if (localAccessibleContext != null) {
/* 8163 */           Accessible localAccessible = localAccessibleContext.getAccessibleChild(paramInt);
/* 8164 */           localAccessibleContext.setAccessibleParent(this);
/* 8165 */           return localAccessible;
/*      */         }
/* 8167 */         return null;
/*      */       }
/*      */ 
/*      */       public Locale getLocale()
/*      */       {
/* 8186 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8187 */         if (localAccessibleContext != null) {
/* 8188 */           return localAccessibleContext.getLocale();
/*      */         }
/* 8190 */         return null;
/*      */       }
/*      */ 
/*      */       public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 8202 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8203 */         if (localAccessibleContext != null)
/* 8204 */           localAccessibleContext.addPropertyChangeListener(paramPropertyChangeListener);
/*      */         else
/* 8206 */           super.addPropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 8219 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8220 */         if (localAccessibleContext != null)
/* 8221 */           localAccessibleContext.removePropertyChangeListener(paramPropertyChangeListener);
/*      */         else
/* 8223 */           super.removePropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public AccessibleAction getAccessibleAction()
/*      */       {
/* 8234 */         return getCurrentAccessibleContext().getAccessibleAction();
/*      */       }
/*      */ 
/*      */       public AccessibleComponent getAccessibleComponent()
/*      */       {
/* 8245 */         return this;
/*      */       }
/*      */ 
/*      */       public AccessibleSelection getAccessibleSelection()
/*      */       {
/* 8256 */         return getCurrentAccessibleContext().getAccessibleSelection();
/*      */       }
/*      */ 
/*      */       public AccessibleText getAccessibleText()
/*      */       {
/* 8266 */         return getCurrentAccessibleContext().getAccessibleText();
/*      */       }
/*      */ 
/*      */       public AccessibleValue getAccessibleValue()
/*      */       {
/* 8276 */         return getCurrentAccessibleContext().getAccessibleValue();
/*      */       }
/*      */ 
/*      */       public Color getBackground()
/*      */       {
/* 8289 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8290 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8291 */           return ((AccessibleComponent)localAccessibleContext).getBackground();
/*      */         }
/* 8293 */         Component localComponent = getCurrentComponent();
/* 8294 */         if (localComponent != null) {
/* 8295 */           return localComponent.getBackground();
/*      */         }
/* 8297 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBackground(Color paramColor)
/*      */       {
/* 8308 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8309 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8310 */           ((AccessibleComponent)localAccessibleContext).setBackground(paramColor);
/*      */         } else {
/* 8312 */           Component localComponent = getCurrentComponent();
/* 8313 */           if (localComponent != null)
/* 8314 */             localComponent.setBackground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Color getForeground()
/*      */       {
/* 8326 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8327 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8328 */           return ((AccessibleComponent)localAccessibleContext).getForeground();
/*      */         }
/* 8330 */         Component localComponent = getCurrentComponent();
/* 8331 */         if (localComponent != null) {
/* 8332 */           return localComponent.getForeground();
/*      */         }
/* 8334 */         return null;
/*      */       }
/*      */ 
/*      */       public void setForeground(Color paramColor)
/*      */       {
/* 8345 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8346 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8347 */           ((AccessibleComponent)localAccessibleContext).setForeground(paramColor);
/*      */         } else {
/* 8349 */           Component localComponent = getCurrentComponent();
/* 8350 */           if (localComponent != null)
/* 8351 */             localComponent.setForeground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Cursor getCursor()
/*      */       {
/* 8363 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8364 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8365 */           return ((AccessibleComponent)localAccessibleContext).getCursor();
/*      */         }
/* 8367 */         Component localComponent = getCurrentComponent();
/* 8368 */         if (localComponent != null) {
/* 8369 */           return localComponent.getCursor();
/*      */         }
/* 8371 */         Accessible localAccessible = getAccessibleParent();
/* 8372 */         if ((localAccessible instanceof AccessibleComponent)) {
/* 8373 */           return ((AccessibleComponent)localAccessible).getCursor();
/*      */         }
/* 8375 */         return null;
/*      */       }
/*      */ 
/*      */       public void setCursor(Cursor paramCursor)
/*      */       {
/* 8387 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8388 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8389 */           ((AccessibleComponent)localAccessibleContext).setCursor(paramCursor);
/*      */         } else {
/* 8391 */           Component localComponent = getCurrentComponent();
/* 8392 */           if (localComponent != null)
/* 8393 */             localComponent.setCursor(paramCursor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Font getFont()
/*      */       {
/* 8405 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8406 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8407 */           return ((AccessibleComponent)localAccessibleContext).getFont();
/*      */         }
/* 8409 */         Component localComponent = getCurrentComponent();
/* 8410 */         if (localComponent != null) {
/* 8411 */           return localComponent.getFont();
/*      */         }
/* 8413 */         return null;
/*      */       }
/*      */ 
/*      */       public void setFont(Font paramFont)
/*      */       {
/* 8424 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8425 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8426 */           ((AccessibleComponent)localAccessibleContext).setFont(paramFont);
/*      */         } else {
/* 8428 */           Component localComponent = getCurrentComponent();
/* 8429 */           if (localComponent != null)
/* 8430 */             localComponent.setFont(paramFont);
/*      */         }
/*      */       }
/*      */ 
/*      */       public FontMetrics getFontMetrics(Font paramFont)
/*      */       {
/* 8444 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8445 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8446 */           return ((AccessibleComponent)localAccessibleContext).getFontMetrics(paramFont);
/*      */         }
/* 8448 */         Component localComponent = getCurrentComponent();
/* 8449 */         if (localComponent != null) {
/* 8450 */           return localComponent.getFontMetrics(paramFont);
/*      */         }
/* 8452 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isEnabled()
/*      */       {
/* 8463 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8464 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8465 */           return ((AccessibleComponent)localAccessibleContext).isEnabled();
/*      */         }
/* 8467 */         Component localComponent = getCurrentComponent();
/* 8468 */         if (localComponent != null) {
/* 8469 */           return localComponent.isEnabled();
/*      */         }
/* 8471 */         return false;
/*      */       }
/*      */ 
/*      */       public void setEnabled(boolean paramBoolean)
/*      */       {
/* 8482 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8483 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8484 */           ((AccessibleComponent)localAccessibleContext).setEnabled(paramBoolean);
/*      */         } else {
/* 8486 */           Component localComponent = getCurrentComponent();
/* 8487 */           if (localComponent != null)
/* 8488 */             localComponent.setEnabled(paramBoolean);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isVisible()
/*      */       {
/* 8503 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8504 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8505 */           return ((AccessibleComponent)localAccessibleContext).isVisible();
/*      */         }
/* 8507 */         Component localComponent = getCurrentComponent();
/* 8508 */         if (localComponent != null) {
/* 8509 */           return localComponent.isVisible();
/*      */         }
/* 8511 */         return false;
/*      */       }
/*      */ 
/*      */       public void setVisible(boolean paramBoolean)
/*      */       {
/* 8522 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8523 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8524 */           ((AccessibleComponent)localAccessibleContext).setVisible(paramBoolean);
/*      */         } else {
/* 8526 */           Component localComponent = getCurrentComponent();
/* 8527 */           if (localComponent != null)
/* 8528 */             localComponent.setVisible(paramBoolean);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isShowing()
/*      */       {
/* 8543 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8544 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8545 */           if (localAccessibleContext.getAccessibleParent() != null) {
/* 8546 */             return ((AccessibleComponent)localAccessibleContext).isShowing();
/*      */           }
/*      */ 
/* 8551 */           return isVisible();
/*      */         }
/*      */ 
/* 8554 */         Component localComponent = getCurrentComponent();
/* 8555 */         if (localComponent != null) {
/* 8556 */           return localComponent.isShowing();
/*      */         }
/* 8558 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean contains(Point paramPoint)
/*      */       {
/* 8575 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8576 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8577 */           localObject = ((AccessibleComponent)localAccessibleContext).getBounds();
/* 8578 */           return ((Rectangle)localObject).contains(paramPoint);
/*      */         }
/* 8580 */         Object localObject = getCurrentComponent();
/* 8581 */         if (localObject != null) {
/* 8582 */           Rectangle localRectangle = ((Component)localObject).getBounds();
/* 8583 */           return localRectangle.contains(paramPoint);
/*      */         }
/* 8585 */         return getBounds().contains(paramPoint);
/*      */       }
/*      */ 
/*      */       public Point getLocationOnScreen()
/*      */       {
/* 8597 */         if ((this.parent != null) && (this.parent.isShowing())) {
/* 8598 */           Point localPoint1 = this.parent.getLocationOnScreen();
/* 8599 */           Point localPoint2 = getLocation();
/* 8600 */           localPoint2.translate(localPoint1.x, localPoint1.y);
/* 8601 */           return localPoint2;
/*      */         }
/* 8603 */         return null;
/*      */       }
/*      */ 
/*      */       public Point getLocation()
/*      */       {
/* 8618 */         if (this.parent != null) {
/* 8619 */           Rectangle localRectangle = this.parent.getCellRect(this.row, this.column, false);
/* 8620 */           if (localRectangle != null) {
/* 8621 */             return localRectangle.getLocation();
/*      */           }
/*      */         }
/* 8624 */         return null;
/*      */       }
/*      */ 
/*      */       public void setLocation(Point paramPoint)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Rectangle getBounds()
/*      */       {
/* 8637 */         if (this.parent != null) {
/* 8638 */           return this.parent.getCellRect(this.row, this.column, false);
/*      */         }
/* 8640 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBounds(Rectangle paramRectangle)
/*      */       {
/* 8645 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8646 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8647 */           ((AccessibleComponent)localAccessibleContext).setBounds(paramRectangle);
/*      */         } else {
/* 8649 */           Component localComponent = getCurrentComponent();
/* 8650 */           if (localComponent != null)
/* 8651 */             localComponent.setBounds(paramRectangle);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Dimension getSize()
/*      */       {
/* 8657 */         if (this.parent != null) {
/* 8658 */           Rectangle localRectangle = this.parent.getCellRect(this.row, this.column, false);
/* 8659 */           if (localRectangle != null) {
/* 8660 */             return localRectangle.getSize();
/*      */           }
/*      */         }
/* 8663 */         return null;
/*      */       }
/*      */ 
/*      */       public void setSize(Dimension paramDimension) {
/* 8667 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8668 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8669 */           ((AccessibleComponent)localAccessibleContext).setSize(paramDimension);
/*      */         } else {
/* 8671 */           Component localComponent = getCurrentComponent();
/* 8672 */           if (localComponent != null)
/* 8673 */             localComponent.setSize(paramDimension);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleAt(Point paramPoint)
/*      */       {
/* 8679 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8680 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8681 */           return ((AccessibleComponent)localAccessibleContext).getAccessibleAt(paramPoint);
/*      */         }
/* 8683 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isFocusTraversable()
/*      */       {
/* 8688 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8689 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8690 */           return ((AccessibleComponent)localAccessibleContext).isFocusTraversable();
/*      */         }
/* 8692 */         Component localComponent = getCurrentComponent();
/* 8693 */         if (localComponent != null) {
/* 8694 */           return localComponent.isFocusTraversable();
/*      */         }
/* 8696 */         return false;
/*      */       }
/*      */ 
/*      */       public void requestFocus()
/*      */       {
/* 8702 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8703 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8704 */           ((AccessibleComponent)localAccessibleContext).requestFocus();
/*      */         } else {
/* 8706 */           Component localComponent = getCurrentComponent();
/* 8707 */           if (localComponent != null)
/* 8708 */             localComponent.requestFocus();
/*      */         }
/*      */       }
/*      */ 
/*      */       public void addFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 8714 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8715 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8716 */           ((AccessibleComponent)localAccessibleContext).addFocusListener(paramFocusListener);
/*      */         } else {
/* 8718 */           Component localComponent = getCurrentComponent();
/* 8719 */           if (localComponent != null)
/* 8720 */             localComponent.addFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void removeFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 8726 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8727 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 8728 */           ((AccessibleComponent)localAccessibleContext).removeFocusListener(paramFocusListener);
/*      */         } else {
/* 8730 */           Component localComponent = getCurrentComponent();
/* 8731 */           if (localComponent != null)
/* 8732 */             localComponent.removeFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private class AccessibleJTableHeaderCell extends AccessibleContext
/*      */       implements Accessible, AccessibleComponent
/*      */     {
/*      */       private int row;
/*      */       private int column;
/*      */       private JTableHeader parent;
/*      */       private Component rendererComponent;
/*      */ 
/*      */       public AccessibleJTableHeaderCell(int paramInt1, int paramJTableHeader, JTableHeader paramComponent, Component arg5)
/*      */       {
/* 8763 */         this.row = paramInt1;
/* 8764 */         this.column = paramJTableHeader;
/* 8765 */         this.parent = paramComponent;
/*      */         Object localObject;
/* 8766 */         this.rendererComponent = localObject;
/* 8767 */         setAccessibleParent(paramComponent);
/*      */       }
/*      */ 
/*      */       public AccessibleContext getAccessibleContext()
/*      */       {
/* 8779 */         return this;
/*      */       }
/*      */ 
/*      */       private AccessibleContext getCurrentAccessibleContext()
/*      */       {
/* 8787 */         return this.rendererComponent.getAccessibleContext();
/*      */       }
/*      */ 
/*      */       private Component getCurrentComponent()
/*      */       {
/* 8794 */         return this.rendererComponent;
/*      */       }
/*      */ 
/*      */       public String getAccessibleName()
/*      */       {
/* 8806 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8807 */         if (localAccessibleContext != null) {
/* 8808 */           String str = localAccessibleContext.getAccessibleName();
/* 8809 */           if ((str != null) && (str != "")) {
/* 8810 */             return localAccessibleContext.getAccessibleName();
/*      */           }
/*      */         }
/* 8813 */         if ((this.accessibleName != null) && (this.accessibleName != "")) {
/* 8814 */           return this.accessibleName;
/*      */         }
/* 8816 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleName(String paramString)
/*      */       {
/* 8826 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8827 */         if (localAccessibleContext != null)
/* 8828 */           localAccessibleContext.setAccessibleName(paramString);
/*      */         else
/* 8830 */           super.setAccessibleName(paramString);
/*      */       }
/*      */ 
/*      */       public String getAccessibleDescription()
/*      */       {
/* 8842 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8843 */         if (localAccessibleContext != null) {
/* 8844 */           return localAccessibleContext.getAccessibleDescription();
/*      */         }
/* 8846 */         return super.getAccessibleDescription();
/*      */       }
/*      */ 
/*      */       public void setAccessibleDescription(String paramString)
/*      */       {
/* 8856 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8857 */         if (localAccessibleContext != null)
/* 8858 */           localAccessibleContext.setAccessibleDescription(paramString);
/*      */         else
/* 8860 */           super.setAccessibleDescription(paramString);
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/* 8872 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8873 */         if (localAccessibleContext != null) {
/* 8874 */           return localAccessibleContext.getAccessibleRole();
/*      */         }
/* 8876 */         return AccessibleRole.UNKNOWN;
/*      */       }
/*      */ 
/*      */       public AccessibleStateSet getAccessibleStateSet()
/*      */       {
/* 8888 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8889 */         AccessibleStateSet localAccessibleStateSet = null;
/*      */ 
/* 8891 */         if (localAccessibleContext != null) {
/* 8892 */           localAccessibleStateSet = localAccessibleContext.getAccessibleStateSet();
/*      */         }
/* 8894 */         if (localAccessibleStateSet == null) {
/* 8895 */           localAccessibleStateSet = new AccessibleStateSet();
/*      */         }
/* 8897 */         Rectangle localRectangle1 = JTable.this.getVisibleRect();
/* 8898 */         Rectangle localRectangle2 = JTable.this.getCellRect(this.row, this.column, false);
/* 8899 */         if (localRectangle1.intersects(localRectangle2)) {
/* 8900 */           localAccessibleStateSet.add(AccessibleState.SHOWING);
/*      */         }
/* 8902 */         else if (localAccessibleStateSet.contains(AccessibleState.SHOWING)) {
/* 8903 */           localAccessibleStateSet.remove(AccessibleState.SHOWING);
/*      */         }
/*      */ 
/* 8906 */         if (JTable.this.isCellSelected(this.row, this.column))
/* 8907 */           localAccessibleStateSet.add(AccessibleState.SELECTED);
/* 8908 */         else if (localAccessibleStateSet.contains(AccessibleState.SELECTED)) {
/* 8909 */           localAccessibleStateSet.remove(AccessibleState.SELECTED);
/*      */         }
/* 8911 */         if ((this.row == JTable.this.getSelectedRow()) && (this.column == JTable.this.getSelectedColumn())) {
/* 8912 */           localAccessibleStateSet.add(AccessibleState.ACTIVE);
/*      */         }
/* 8914 */         localAccessibleStateSet.add(AccessibleState.TRANSIENT);
/* 8915 */         return localAccessibleStateSet;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleParent()
/*      */       {
/* 8926 */         return this.parent;
/*      */       }
/*      */ 
/*      */       public int getAccessibleIndexInParent()
/*      */       {
/* 8937 */         return this.column;
/*      */       }
/*      */ 
/*      */       public int getAccessibleChildrenCount()
/*      */       {
/* 8946 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8947 */         if (localAccessibleContext != null) {
/* 8948 */           return localAccessibleContext.getAccessibleChildrenCount();
/*      */         }
/* 8950 */         return 0;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleChild(int paramInt)
/*      */       {
/* 8962 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8963 */         if (localAccessibleContext != null) {
/* 8964 */           Accessible localAccessible = localAccessibleContext.getAccessibleChild(paramInt);
/* 8965 */           localAccessibleContext.setAccessibleParent(this);
/* 8966 */           return localAccessible;
/*      */         }
/* 8968 */         return null;
/*      */       }
/*      */ 
/*      */       public Locale getLocale()
/*      */       {
/* 8987 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 8988 */         if (localAccessibleContext != null) {
/* 8989 */           return localAccessibleContext.getLocale();
/*      */         }
/* 8991 */         return null;
/*      */       }
/*      */ 
/*      */       public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 9003 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9004 */         if (localAccessibleContext != null)
/* 9005 */           localAccessibleContext.addPropertyChangeListener(paramPropertyChangeListener);
/*      */         else
/* 9007 */           super.addPropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 9020 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9021 */         if (localAccessibleContext != null)
/* 9022 */           localAccessibleContext.removePropertyChangeListener(paramPropertyChangeListener);
/*      */         else
/* 9024 */           super.removePropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public AccessibleAction getAccessibleAction()
/*      */       {
/* 9035 */         return getCurrentAccessibleContext().getAccessibleAction();
/*      */       }
/*      */ 
/*      */       public AccessibleComponent getAccessibleComponent()
/*      */       {
/* 9046 */         return this;
/*      */       }
/*      */ 
/*      */       public AccessibleSelection getAccessibleSelection()
/*      */       {
/* 9057 */         return getCurrentAccessibleContext().getAccessibleSelection();
/*      */       }
/*      */ 
/*      */       public AccessibleText getAccessibleText()
/*      */       {
/* 9067 */         return getCurrentAccessibleContext().getAccessibleText();
/*      */       }
/*      */ 
/*      */       public AccessibleValue getAccessibleValue()
/*      */       {
/* 9077 */         return getCurrentAccessibleContext().getAccessibleValue();
/*      */       }
/*      */ 
/*      */       public Color getBackground()
/*      */       {
/* 9090 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9091 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9092 */           return ((AccessibleComponent)localAccessibleContext).getBackground();
/*      */         }
/* 9094 */         Component localComponent = getCurrentComponent();
/* 9095 */         if (localComponent != null) {
/* 9096 */           return localComponent.getBackground();
/*      */         }
/* 9098 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBackground(Color paramColor)
/*      */       {
/* 9109 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9110 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9111 */           ((AccessibleComponent)localAccessibleContext).setBackground(paramColor);
/*      */         } else {
/* 9113 */           Component localComponent = getCurrentComponent();
/* 9114 */           if (localComponent != null)
/* 9115 */             localComponent.setBackground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Color getForeground()
/*      */       {
/* 9127 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9128 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9129 */           return ((AccessibleComponent)localAccessibleContext).getForeground();
/*      */         }
/* 9131 */         Component localComponent = getCurrentComponent();
/* 9132 */         if (localComponent != null) {
/* 9133 */           return localComponent.getForeground();
/*      */         }
/* 9135 */         return null;
/*      */       }
/*      */ 
/*      */       public void setForeground(Color paramColor)
/*      */       {
/* 9146 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9147 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9148 */           ((AccessibleComponent)localAccessibleContext).setForeground(paramColor);
/*      */         } else {
/* 9150 */           Component localComponent = getCurrentComponent();
/* 9151 */           if (localComponent != null)
/* 9152 */             localComponent.setForeground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Cursor getCursor()
/*      */       {
/* 9164 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9165 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9166 */           return ((AccessibleComponent)localAccessibleContext).getCursor();
/*      */         }
/* 9168 */         Component localComponent = getCurrentComponent();
/* 9169 */         if (localComponent != null) {
/* 9170 */           return localComponent.getCursor();
/*      */         }
/* 9172 */         Accessible localAccessible = getAccessibleParent();
/* 9173 */         if ((localAccessible instanceof AccessibleComponent)) {
/* 9174 */           return ((AccessibleComponent)localAccessible).getCursor();
/*      */         }
/* 9176 */         return null;
/*      */       }
/*      */ 
/*      */       public void setCursor(Cursor paramCursor)
/*      */       {
/* 9188 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9189 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9190 */           ((AccessibleComponent)localAccessibleContext).setCursor(paramCursor);
/*      */         } else {
/* 9192 */           Component localComponent = getCurrentComponent();
/* 9193 */           if (localComponent != null)
/* 9194 */             localComponent.setCursor(paramCursor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Font getFont()
/*      */       {
/* 9206 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9207 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9208 */           return ((AccessibleComponent)localAccessibleContext).getFont();
/*      */         }
/* 9210 */         Component localComponent = getCurrentComponent();
/* 9211 */         if (localComponent != null) {
/* 9212 */           return localComponent.getFont();
/*      */         }
/* 9214 */         return null;
/*      */       }
/*      */ 
/*      */       public void setFont(Font paramFont)
/*      */       {
/* 9225 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9226 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9227 */           ((AccessibleComponent)localAccessibleContext).setFont(paramFont);
/*      */         } else {
/* 9229 */           Component localComponent = getCurrentComponent();
/* 9230 */           if (localComponent != null)
/* 9231 */             localComponent.setFont(paramFont);
/*      */         }
/*      */       }
/*      */ 
/*      */       public FontMetrics getFontMetrics(Font paramFont)
/*      */       {
/* 9245 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9246 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9247 */           return ((AccessibleComponent)localAccessibleContext).getFontMetrics(paramFont);
/*      */         }
/* 9249 */         Component localComponent = getCurrentComponent();
/* 9250 */         if (localComponent != null) {
/* 9251 */           return localComponent.getFontMetrics(paramFont);
/*      */         }
/* 9253 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isEnabled()
/*      */       {
/* 9264 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9265 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9266 */           return ((AccessibleComponent)localAccessibleContext).isEnabled();
/*      */         }
/* 9268 */         Component localComponent = getCurrentComponent();
/* 9269 */         if (localComponent != null) {
/* 9270 */           return localComponent.isEnabled();
/*      */         }
/* 9272 */         return false;
/*      */       }
/*      */ 
/*      */       public void setEnabled(boolean paramBoolean)
/*      */       {
/* 9283 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9284 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9285 */           ((AccessibleComponent)localAccessibleContext).setEnabled(paramBoolean);
/*      */         } else {
/* 9287 */           Component localComponent = getCurrentComponent();
/* 9288 */           if (localComponent != null)
/* 9289 */             localComponent.setEnabled(paramBoolean);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isVisible()
/*      */       {
/* 9304 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9305 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9306 */           return ((AccessibleComponent)localAccessibleContext).isVisible();
/*      */         }
/* 9308 */         Component localComponent = getCurrentComponent();
/* 9309 */         if (localComponent != null) {
/* 9310 */           return localComponent.isVisible();
/*      */         }
/* 9312 */         return false;
/*      */       }
/*      */ 
/*      */       public void setVisible(boolean paramBoolean)
/*      */       {
/* 9323 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9324 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9325 */           ((AccessibleComponent)localAccessibleContext).setVisible(paramBoolean);
/*      */         } else {
/* 9327 */           Component localComponent = getCurrentComponent();
/* 9328 */           if (localComponent != null)
/* 9329 */             localComponent.setVisible(paramBoolean);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isShowing()
/*      */       {
/* 9344 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9345 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9346 */           if (localAccessibleContext.getAccessibleParent() != null) {
/* 9347 */             return ((AccessibleComponent)localAccessibleContext).isShowing();
/*      */           }
/*      */ 
/* 9352 */           return isVisible();
/*      */         }
/*      */ 
/* 9355 */         Component localComponent = getCurrentComponent();
/* 9356 */         if (localComponent != null) {
/* 9357 */           return localComponent.isShowing();
/*      */         }
/* 9359 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean contains(Point paramPoint)
/*      */       {
/* 9376 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9377 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9378 */           localObject = ((AccessibleComponent)localAccessibleContext).getBounds();
/* 9379 */           return ((Rectangle)localObject).contains(paramPoint);
/*      */         }
/* 9381 */         Object localObject = getCurrentComponent();
/* 9382 */         if (localObject != null) {
/* 9383 */           Rectangle localRectangle = ((Component)localObject).getBounds();
/* 9384 */           return localRectangle.contains(paramPoint);
/*      */         }
/* 9386 */         return getBounds().contains(paramPoint);
/*      */       }
/*      */ 
/*      */       public Point getLocationOnScreen()
/*      */       {
/* 9398 */         if ((this.parent != null) && (this.parent.isShowing())) {
/* 9399 */           Point localPoint1 = this.parent.getLocationOnScreen();
/* 9400 */           Point localPoint2 = getLocation();
/* 9401 */           localPoint2.translate(localPoint1.x, localPoint1.y);
/* 9402 */           return localPoint2;
/*      */         }
/* 9404 */         return null;
/*      */       }
/*      */ 
/*      */       public Point getLocation()
/*      */       {
/* 9419 */         if (this.parent != null) {
/* 9420 */           Rectangle localRectangle = this.parent.getHeaderRect(this.column);
/* 9421 */           if (localRectangle != null) {
/* 9422 */             return localRectangle.getLocation();
/*      */           }
/*      */         }
/* 9425 */         return null;
/*      */       }
/*      */ 
/*      */       public void setLocation(Point paramPoint)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Rectangle getBounds()
/*      */       {
/* 9446 */         if (this.parent != null) {
/* 9447 */           return this.parent.getHeaderRect(this.column);
/*      */         }
/* 9449 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBounds(Rectangle paramRectangle)
/*      */       {
/* 9462 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9463 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9464 */           ((AccessibleComponent)localAccessibleContext).setBounds(paramRectangle);
/*      */         } else {
/* 9466 */           Component localComponent = getCurrentComponent();
/* 9467 */           if (localComponent != null)
/* 9468 */             localComponent.setBounds(paramRectangle);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Dimension getSize()
/*      */       {
/* 9484 */         if (this.parent != null) {
/* 9485 */           Rectangle localRectangle = this.parent.getHeaderRect(this.column);
/* 9486 */           if (localRectangle != null) {
/* 9487 */             return localRectangle.getSize();
/*      */           }
/*      */         }
/* 9490 */         return null;
/*      */       }
/*      */ 
/*      */       public void setSize(Dimension paramDimension)
/*      */       {
/* 9500 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9501 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9502 */           ((AccessibleComponent)localAccessibleContext).setSize(paramDimension);
/*      */         } else {
/* 9504 */           Component localComponent = getCurrentComponent();
/* 9505 */           if (localComponent != null)
/* 9506 */             localComponent.setSize(paramDimension);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleAt(Point paramPoint)
/*      */       {
/* 9520 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9521 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9522 */           return ((AccessibleComponent)localAccessibleContext).getAccessibleAt(paramPoint);
/*      */         }
/* 9524 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isFocusTraversable()
/*      */       {
/* 9540 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9541 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9542 */           return ((AccessibleComponent)localAccessibleContext).isFocusTraversable();
/*      */         }
/* 9544 */         Component localComponent = getCurrentComponent();
/* 9545 */         if (localComponent != null) {
/* 9546 */           return localComponent.isFocusTraversable();
/*      */         }
/* 9548 */         return false;
/*      */       }
/*      */ 
/*      */       public void requestFocus()
/*      */       {
/* 9560 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9561 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9562 */           ((AccessibleComponent)localAccessibleContext).requestFocus();
/*      */         } else {
/* 9564 */           Component localComponent = getCurrentComponent();
/* 9565 */           if (localComponent != null)
/* 9566 */             localComponent.requestFocus();
/*      */         }
/*      */       }
/*      */ 
/*      */       public void addFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 9579 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9580 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9581 */           ((AccessibleComponent)localAccessibleContext).addFocusListener(paramFocusListener);
/*      */         } else {
/* 9583 */           Component localComponent = getCurrentComponent();
/* 9584 */           if (localComponent != null)
/* 9585 */             localComponent.addFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void removeFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 9598 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 9599 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 9600 */           ((AccessibleComponent)localAccessibleContext).removeFocusListener(paramFocusListener);
/*      */         } else {
/* 9602 */           Component localComponent = getCurrentComponent();
/* 9603 */           if (localComponent != null)
/* 9604 */             localComponent.removeFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected class AccessibleJTableModelChange
/*      */       implements AccessibleTableModelChange
/*      */     {
/*      */       protected int type;
/*      */       protected int firstRow;
/*      */       protected int lastRow;
/*      */       protected int firstColumn;
/*      */       protected int lastColumn;
/*      */ 
/*      */       protected AccessibleJTableModelChange(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int arg6)
/*      */       {
/* 6710 */         this.type = paramInt1;
/* 6711 */         this.firstRow = paramInt2;
/* 6712 */         this.lastRow = paramInt3;
/* 6713 */         this.firstColumn = paramInt4;
/*      */         int i;
/* 6714 */         this.lastColumn = i;
/*      */       }
/*      */ 
/*      */       public int getType() {
/* 6718 */         return this.type;
/*      */       }
/*      */ 
/*      */       public int getFirstRow() {
/* 6722 */         return this.firstRow;
/*      */       }
/*      */ 
/*      */       public int getLastRow() {
/* 6726 */         return this.lastRow;
/*      */       }
/*      */ 
/*      */       public int getFirstColumn() {
/* 6730 */         return this.firstColumn;
/*      */       }
/*      */ 
/*      */       public int getLastColumn() {
/* 6734 */         return this.lastColumn;
/*      */       }
/*      */     }
/*      */ 
/*      */     private class AccessibleTableHeader
/*      */       implements AccessibleTable
/*      */     {
/*      */       private JTableHeader header;
/*      */       private TableColumnModel headerModel;
/*      */ 
/*      */       AccessibleTableHeader(JTableHeader arg2)
/*      */       {
/*      */         Object localObject;
/* 7502 */         this.header = localObject;
/* 7503 */         this.headerModel = localObject.getColumnModel();
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleCaption()
/*      */       {
/* 7511 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleCaption(Accessible paramAccessible)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleSummary()
/*      */       {
/* 7526 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleSummary(Accessible paramAccessible)
/*      */       {
/*      */       }
/*      */ 
/*      */       public int getAccessibleRowCount()
/*      */       {
/* 7540 */         return 1;
/*      */       }
/*      */ 
/*      */       public int getAccessibleColumnCount()
/*      */       {
/* 7548 */         return this.headerModel.getColumnCount();
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleAt(int paramInt1, int paramInt2)
/*      */       {
/* 7563 */         TableColumn localTableColumn = this.headerModel.getColumn(paramInt2);
/* 7564 */         TableCellRenderer localTableCellRenderer = localTableColumn.getHeaderRenderer();
/* 7565 */         if (localTableCellRenderer == null) {
/* 7566 */           localTableCellRenderer = this.header.getDefaultRenderer();
/*      */         }
/* 7568 */         Component localComponent = localTableCellRenderer.getTableCellRendererComponent(this.header.getTable(), localTableColumn.getHeaderValue(), false, false, -1, paramInt2);
/*      */ 
/* 7573 */         return new JTable.AccessibleJTable.AccessibleJTableHeaderCell(JTable.AccessibleJTable.this, paramInt1, paramInt2, JTable.this.getTableHeader(), localComponent);
/*      */       }
/*      */ 
/*      */       public int getAccessibleRowExtentAt(int paramInt1, int paramInt2)
/*      */       {
/* 7585 */         return 1;
/*      */       }
/*      */ 
/*      */       public int getAccessibleColumnExtentAt(int paramInt1, int paramInt2)
/*      */       {
/* 7594 */         return 1;
/*      */       }
/*      */ 
/*      */       public AccessibleTable getAccessibleRowHeader()
/*      */       {
/* 7602 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleRowHeader(AccessibleTable paramAccessibleTable)
/*      */       {
/*      */       }
/*      */ 
/*      */       public AccessibleTable getAccessibleColumnHeader()
/*      */       {
/* 7618 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleColumnHeader(AccessibleTable paramAccessibleTable)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleRowDescription(int paramInt)
/*      */       {
/* 7636 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleRowDescription(int paramInt, Accessible paramAccessible)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleColumnDescription(int paramInt)
/*      */       {
/* 7654 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleColumnDescription(int paramInt, Accessible paramAccessible)
/*      */       {
/*      */       }
/*      */ 
/*      */       public boolean isAccessibleSelected(int paramInt1, int paramInt2)
/*      */       {
/* 7676 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean isAccessibleRowSelected(int paramInt)
/*      */       {
/* 7687 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean isAccessibleColumnSelected(int paramInt)
/*      */       {
/* 7698 */         return false;
/*      */       }
/*      */ 
/*      */       public int[] getSelectedAccessibleRows()
/*      */       {
/* 7707 */         return new int[0];
/*      */       }
/*      */ 
/*      */       public int[] getSelectedAccessibleColumns()
/*      */       {
/* 7716 */         return new int[0];
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BooleanEditor extends DefaultCellEditor
/*      */   {
/*      */     public BooleanEditor()
/*      */     {
/* 5521 */       super();
/* 5522 */       JCheckBox localJCheckBox = (JCheckBox)getComponent();
/* 5523 */       localJCheckBox.setHorizontalAlignment(0);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BooleanRenderer extends JCheckBox
/*      */     implements TableCellRenderer, UIResource
/*      */   {
/* 5391 */     private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
/*      */ 
/*      */     public BooleanRenderer()
/*      */     {
/* 5395 */       setHorizontalAlignment(0);
/* 5396 */       setBorderPainted(true);
/*      */     }
/*      */ 
/*      */     public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*      */     {
/* 5401 */       if (paramBoolean1) {
/* 5402 */         setForeground(paramJTable.getSelectionForeground());
/* 5403 */         super.setBackground(paramJTable.getSelectionBackground());
/*      */       }
/*      */       else {
/* 5406 */         setForeground(paramJTable.getForeground());
/* 5407 */         setBackground(paramJTable.getBackground());
/*      */       }
/* 5409 */       setSelected((paramObject != null) && (((Boolean)paramObject).booleanValue()));
/*      */ 
/* 5411 */       if (paramBoolean2)
/* 5412 */         setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
/*      */       else {
/* 5414 */         setBorder(noFocusBorder);
/*      */       }
/*      */ 
/* 5417 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   class CellEditorRemover
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     KeyboardFocusManager focusManager;
/*      */ 
/*      */     public CellEditorRemover(KeyboardFocusManager arg2)
/*      */     {
/*      */       Object localObject;
/* 5954 */       this.focusManager = localObject;
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 5958 */       if ((!JTable.this.isEditing()) || (JTable.this.getClientProperty("terminateEditOnFocusLost") != Boolean.TRUE)) {
/* 5959 */         return;
/*      */       }
/*      */ 
/* 5962 */       Object localObject = this.focusManager.getPermanentFocusOwner();
/* 5963 */       while (localObject != null) {
/* 5964 */         if (localObject == JTable.this)
/*      */         {
/* 5966 */           return;
/* 5967 */         }if (((localObject instanceof Window)) || (((localObject instanceof Applet)) && (((Component)localObject).getParent() == null)))
/*      */         {
/* 5969 */           if ((localObject != SwingUtilities.getRoot(JTable.this)) || 
/* 5970 */             (JTable.this.getCellEditor().stopCellEditing())) break;
/* 5971 */           JTable.this.getCellEditor().cancelCellEditing(); break;
/*      */         }
/*      */ 
/* 5976 */         localObject = ((Component)localObject).getParent();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DateRenderer extends DefaultTableCellRenderer.UIResource
/*      */   {
/*      */     DateFormat formatter;
/*      */ 
/*      */     public void setValue(Object paramObject)
/*      */     {
/* 5373 */       if (this.formatter == null) {
/* 5374 */         this.formatter = DateFormat.getDateInstance();
/*      */       }
/* 5376 */       setText(paramObject == null ? "" : this.formatter.format(paramObject));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DoubleRenderer extends JTable.NumberRenderer
/*      */   {
/*      */     NumberFormat formatter;
/*      */ 
/*      */     public void setValue(Object paramObject)
/*      */     {
/* 5361 */       if (this.formatter == null) {
/* 5362 */         this.formatter = NumberFormat.getInstance();
/*      */       }
/* 5364 */       setText(paramObject == null ? "" : this.formatter.format(paramObject));
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class DropLocation extends TransferHandler.DropLocation
/*      */   {
/*      */     private final int row;
/*      */     private final int col;
/*      */     private final boolean isInsertRow;
/*      */     private final boolean isInsertCol;
/*      */ 
/*      */     private DropLocation(Point paramPoint, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*  467 */       super();
/*  468 */       this.row = paramInt1;
/*  469 */       this.col = paramInt2;
/*  470 */       this.isInsertRow = paramBoolean1;
/*  471 */       this.isInsertCol = paramBoolean2;
/*      */     }
/*      */ 
/*      */     public int getRow()
/*      */     {
/*  489 */       return this.row;
/*      */     }
/*      */ 
/*      */     public int getColumn()
/*      */     {
/*  507 */       return this.col;
/*      */     }
/*      */ 
/*      */     public boolean isInsertRow()
/*      */     {
/*  517 */       return this.isInsertRow;
/*      */     }
/*      */ 
/*      */     public boolean isInsertColumn()
/*      */     {
/*  527 */       return this.isInsertCol;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  539 */       return getClass().getName() + "[dropPoint=" + getDropPoint() + "," + "row=" + this.row + "," + "column=" + this.col + "," + "insertRow=" + this.isInsertRow + "," + "insertColumn=" + this.isInsertCol + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   static class GenericEditor extends DefaultCellEditor
/*      */   {
/* 5447 */     Class[] argTypes = { String.class };
/*      */     Constructor constructor;
/*      */     Object value;
/*      */ 
/*      */     public GenericEditor()
/*      */     {
/* 5452 */       super();
/* 5453 */       getComponent().setName("Table.editor");
/*      */     }
/*      */ 
/*      */     public boolean stopCellEditing() {
/* 5457 */       String str = (String)super.getCellEditorValue();
/*      */       try
/*      */       {
/* 5466 */         if ("".equals(str)) {
/* 5467 */           if (this.constructor.getDeclaringClass() == String.class) {
/* 5468 */             this.value = str;
/*      */           }
/* 5470 */           return super.stopCellEditing();
/*      */         }
/*      */ 
/* 5473 */         SwingUtilities2.checkAccess(this.constructor.getModifiers());
/* 5474 */         this.value = this.constructor.newInstance(new Object[] { str });
/*      */       }
/*      */       catch (Exception localException) {
/* 5477 */         ((JComponent)getComponent()).setBorder(new LineBorder(Color.red));
/* 5478 */         return false;
/*      */       }
/* 5480 */       return super.stopCellEditing();
/*      */     }
/*      */ 
/*      */     public Component getTableCellEditorComponent(JTable paramJTable, Object paramObject, boolean paramBoolean, int paramInt1, int paramInt2)
/*      */     {
/* 5486 */       this.value = null;
/* 5487 */       ((JComponent)getComponent()).setBorder(new LineBorder(Color.black));
/*      */       try {
/* 5489 */         Object localObject = paramJTable.getColumnClass(paramInt2);
/*      */ 
/* 5494 */         if (localObject == Object.class) {
/* 5495 */           localObject = String.class;
/*      */         }
/* 5497 */         ReflectUtil.checkPackageAccess((Class)localObject);
/* 5498 */         SwingUtilities2.checkAccess(((Class)localObject).getModifiers());
/* 5499 */         this.constructor = ((Class)localObject).getConstructor(this.argTypes);
/*      */       }
/*      */       catch (Exception localException) {
/* 5502 */         return null;
/*      */       }
/* 5504 */       return super.getTableCellEditorComponent(paramJTable, paramObject, paramBoolean, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public Object getCellEditorValue() {
/* 5508 */       return this.value;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class IconRenderer extends DefaultTableCellRenderer.UIResource
/*      */   {
/*      */     public IconRenderer()
/*      */     {
/* 5383 */       setHorizontalAlignment(0);
/*      */     }
/* 5385 */     public void setValue(Object paramObject) { setIcon((paramObject instanceof Icon) ? (Icon)paramObject : null); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private final class ModelChange
/*      */   {
/*      */     int startModelIndex;
/*      */     int endModelIndex;
/*      */     int type;
/*      */     int modelRowCount;
/*      */     TableModelEvent event;
/*      */     int length;
/*      */     boolean allRowsChanged;
/*      */ 
/*      */     ModelChange(TableModelEvent arg2)
/*      */     {
/*      */       Object localObject;
/* 4084 */       this.startModelIndex = Math.max(0, localObject.getFirstRow());
/* 4085 */       this.endModelIndex = localObject.getLastRow();
/* 4086 */       this.modelRowCount = JTable.this.getModel().getRowCount();
/* 4087 */       if (this.endModelIndex < 0) {
/* 4088 */         this.endModelIndex = Math.max(0, this.modelRowCount - 1);
/*      */       }
/* 4090 */       this.length = (this.endModelIndex - this.startModelIndex + 1);
/* 4091 */       this.type = localObject.getType();
/* 4092 */       this.event = localObject;
/* 4093 */       this.allRowsChanged = (localObject.getLastRow() == 2147483647);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NumberEditor extends JTable.GenericEditor
/*      */   {
/*      */     public NumberEditor()
/*      */     {
/* 5515 */       ((JTextField)getComponent()).setHorizontalAlignment(4);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NumberRenderer extends DefaultTableCellRenderer.UIResource
/*      */   {
/*      */     public NumberRenderer()
/*      */     {
/* 5352 */       setHorizontalAlignment(4);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum PrintMode
/*      */   {
/*  266 */     NORMAL, 
/*      */ 
/*  273 */     FIT_WIDTH;
/*      */   }
/*      */ 
/*      */   private static abstract interface Resizable2
/*      */   {
/*      */     public abstract int getElementCount();
/*      */ 
/*      */     public abstract int getLowerBoundAt(int paramInt);
/*      */ 
/*      */     public abstract int getUpperBoundAt(int paramInt);
/*      */ 
/*      */     public abstract void setSizeAt(int paramInt1, int paramInt2);
/*      */   }
/*      */ 
/*      */   private static abstract interface Resizable3 extends JTable.Resizable2
/*      */   {
/*      */     public abstract int getMidPointAt(int paramInt);
/*      */   }
/*      */ 
/*      */   private final class SortManager
/*      */   {
/*      */     RowSorter<? extends TableModel> sorter;
/*      */     private ListSelectionModel modelSelection;
/*      */     private int modelLeadIndex;
/*      */     private boolean syncingSelection;
/*      */     private int[] lastModelSelection;
/*      */     private SizeSequence modelRowSizes;
/*      */ 
/*      */     SortManager()
/*      */     {
/*      */       Object localObject;
/* 3849 */       this.sorter = localObject;
/* 3850 */       localObject.addRowSorterListener(JTable.this);
/*      */     }
/*      */ 
/*      */     public void dispose()
/*      */     {
/* 3857 */       if (this.sorter != null)
/* 3858 */         this.sorter.removeRowSorterListener(JTable.this);
/*      */     }
/*      */ 
/*      */     public void setViewRowHeight(int paramInt1, int paramInt2)
/*      */     {
/* 3866 */       if (this.modelRowSizes == null) {
/* 3867 */         this.modelRowSizes = new SizeSequence(JTable.this.getModel().getRowCount(), JTable.this.getRowHeight());
/*      */       }
/*      */ 
/* 3870 */       this.modelRowSizes.setSize(JTable.this.convertRowIndexToModel(paramInt1), paramInt2);
/*      */     }
/*      */ 
/*      */     public void allChanged()
/*      */     {
/* 3877 */       this.modelLeadIndex = -1;
/* 3878 */       this.modelSelection = null;
/* 3879 */       this.modelRowSizes = null;
/*      */     }
/*      */ 
/*      */     public void viewSelectionChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/* 3886 */       if ((!this.syncingSelection) && (this.modelSelection != null))
/* 3887 */         this.modelSelection = null;
/*      */     }
/*      */ 
/*      */     public void prepareForChange(RowSorterEvent paramRowSorterEvent, JTable.ModelChange paramModelChange)
/*      */     {
/* 3898 */       if (JTable.this.getUpdateSelectionOnSort())
/* 3899 */         cacheSelection(paramRowSorterEvent, paramModelChange);
/*      */     }
/*      */ 
/*      */     private void cacheSelection(RowSorterEvent paramRowSorterEvent, JTable.ModelChange paramModelChange)
/*      */     {
/* 3908 */       if (paramRowSorterEvent != null)
/*      */       {
/* 3913 */         if ((this.modelSelection == null) && (this.sorter.getViewRowCount() != JTable.this.getModel().getRowCount()))
/*      */         {
/* 3915 */           this.modelSelection = new DefaultListSelectionModel();
/* 3916 */           ListSelectionModel localListSelectionModel = JTable.this.getSelectionModel();
/* 3917 */           int i = localListSelectionModel.getMinSelectionIndex();
/* 3918 */           int j = localListSelectionModel.getMaxSelectionIndex();
/*      */ 
/* 3920 */           for (int m = i; m <= j; m++) {
/* 3921 */             if (localListSelectionModel.isSelectedIndex(m)) {
/* 3922 */               k = JTable.this.convertRowIndexToModel(paramRowSorterEvent, m);
/*      */ 
/* 3924 */               if (k != -1) {
/* 3925 */                 this.modelSelection.addSelectionInterval(k, k);
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/* 3930 */           int k = JTable.this.convertRowIndexToModel(paramRowSorterEvent, localListSelectionModel.getLeadSelectionIndex());
/*      */ 
/* 3932 */           SwingUtilities2.setLeadAnchorWithoutSelection(this.modelSelection, k, k);
/*      */         }
/* 3934 */         else if (this.modelSelection == null)
/*      */         {
/* 3937 */           cacheModelSelection(paramRowSorterEvent);
/*      */         }
/* 3939 */       } else if (paramModelChange.allRowsChanged)
/*      */       {
/* 3941 */         this.modelSelection = null;
/* 3942 */       } else if (this.modelSelection != null)
/*      */       {
/* 3944 */         switch (paramModelChange.type) {
/*      */         case -1:
/* 3946 */           this.modelSelection.removeIndexInterval(paramModelChange.startModelIndex, paramModelChange.endModelIndex);
/*      */ 
/* 3948 */           break;
/*      */         case 1:
/* 3950 */           this.modelSelection.insertIndexInterval(paramModelChange.startModelIndex, paramModelChange.length, true);
/*      */ 
/* 3953 */           break;
/*      */         default:
/* 3955 */           break;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 3960 */         cacheModelSelection(null);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void cacheModelSelection(RowSorterEvent paramRowSorterEvent) {
/* 3965 */       this.lastModelSelection = JTable.this.convertSelectionToModel(paramRowSorterEvent);
/* 3966 */       this.modelLeadIndex = JTable.this.convertRowIndexToModel(paramRowSorterEvent, JTable.this.selectionModel.getLeadSelectionIndex());
/*      */     }
/*      */ 
/*      */     public void processChange(RowSorterEvent paramRowSorterEvent, JTable.ModelChange paramModelChange, boolean paramBoolean)
/*      */     {
/* 3978 */       if (paramModelChange != null) {
/* 3979 */         if (paramModelChange.allRowsChanged) {
/* 3980 */           this.modelRowSizes = null;
/* 3981 */           JTable.this.rowModel = null;
/* 3982 */         } else if (this.modelRowSizes != null) {
/* 3983 */           if (paramModelChange.type == 1) {
/* 3984 */             this.modelRowSizes.insertEntries(paramModelChange.startModelIndex, paramModelChange.endModelIndex - paramModelChange.startModelIndex + 1, JTable.this.getRowHeight());
/*      */           }
/* 3988 */           else if (paramModelChange.type == -1) {
/* 3989 */             this.modelRowSizes.removeEntries(paramModelChange.startModelIndex, paramModelChange.endModelIndex - paramModelChange.startModelIndex + 1);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3995 */       if (paramBoolean) {
/* 3996 */         setViewRowHeightsFromModel();
/* 3997 */         restoreSelection(paramModelChange);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void setViewRowHeightsFromModel()
/*      */     {
/* 4006 */       if (this.modelRowSizes != null) {
/* 4007 */         JTable.this.rowModel.setSizes(JTable.this.getRowCount(), JTable.this.getRowHeight());
/* 4008 */         for (int i = JTable.this.getRowCount() - 1; i >= 0; 
/* 4009 */           i--) {
/* 4010 */           int j = JTable.this.convertRowIndexToModel(i);
/* 4011 */           JTable.this.rowModel.setSize(i, this.modelRowSizes.getSize(j));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void restoreSelection(JTable.ModelChange paramModelChange)
/*      */     {
/* 4021 */       this.syncingSelection = true;
/* 4022 */       if (this.lastModelSelection != null) {
/* 4023 */         JTable.this.restoreSortingSelection(this.lastModelSelection, this.modelLeadIndex, paramModelChange);
/*      */ 
/* 4025 */         this.lastModelSelection = null;
/* 4026 */       } else if (this.modelSelection != null) {
/* 4027 */         ListSelectionModel localListSelectionModel = JTable.this.getSelectionModel();
/* 4028 */         localListSelectionModel.setValueIsAdjusting(true);
/* 4029 */         localListSelectionModel.clearSelection();
/* 4030 */         int i = this.modelSelection.getMinSelectionIndex();
/* 4031 */         int j = this.modelSelection.getMaxSelectionIndex();
/*      */ 
/* 4033 */         for (int m = i; m <= j; m++) {
/* 4034 */           if (this.modelSelection.isSelectedIndex(m)) {
/* 4035 */             int k = JTable.this.convertRowIndexToView(m);
/* 4036 */             if (k != -1) {
/* 4037 */               localListSelectionModel.addSelectionInterval(k, k);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 4043 */         m = this.modelSelection.getLeadSelectionIndex();
/* 4044 */         if ((m != -1) && (!this.modelSelection.isSelectionEmpty())) {
/* 4045 */           m = JTable.this.convertRowIndexToView(m);
/*      */         }
/* 4047 */         SwingUtilities2.setLeadAnchorWithoutSelection(localListSelectionModel, m, m);
/*      */ 
/* 4049 */         localListSelectionModel.setValueIsAdjusting(false);
/*      */       }
/* 4051 */       this.syncingSelection = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ThreadSafePrintable
/*      */     implements Printable
/*      */   {
/*      */     private Printable printDelegate;
/*      */     private int retVal;
/*      */     private Throwable retThrowable;
/*      */ 
/*      */     public ThreadSafePrintable(Printable arg2)
/*      */     {
/*      */       Object localObject;
/* 6473 */       this.printDelegate = localObject;
/*      */     }
/*      */ 
/*      */     public int print(final Graphics paramGraphics, final PageFormat paramPageFormat, final int paramInt)
/*      */       throws PrinterException
/*      */     {
/* 6495 */       Runnable local1 = new Runnable()
/*      */       {
/*      */         public synchronized void run() {
/*      */           try {
/* 6499 */             JTable.ThreadSafePrintable.this.retVal = JTable.ThreadSafePrintable.this.printDelegate.print(paramGraphics, paramPageFormat, paramInt);
/*      */           }
/*      */           catch (Throwable localThrowable) {
/* 6502 */             JTable.ThreadSafePrintable.this.retThrowable = localThrowable;
/*      */           }
/*      */           finally {
/* 6505 */             notifyAll();
/*      */           }
/*      */         }
/*      */       };
/* 6510 */       synchronized (local1)
/*      */       {
/* 6512 */         this.retVal = -1;
/* 6513 */         this.retThrowable = null;
/*      */ 
/* 6516 */         SwingUtilities.invokeLater(local1);
/*      */ 
/* 6519 */         while ((this.retVal == -1) && (this.retThrowable == null)) {
/*      */           try {
/* 6521 */             local1.wait();
/*      */           }
/*      */           catch (InterruptedException localInterruptedException)
/*      */           {
/*      */           }
/*      */         }
/*      */ 
/* 6528 */         if (this.retThrowable != null) {
/* 6529 */           if ((this.retThrowable instanceof PrinterException))
/* 6530 */             throw ((PrinterException)this.retThrowable);
/* 6531 */           if ((this.retThrowable instanceof RuntimeException))
/* 6532 */             throw ((RuntimeException)this.retThrowable);
/* 6533 */           if ((this.retThrowable instanceof Error)) {
/* 6534 */             throw ((Error)this.retThrowable);
/*      */           }
/*      */ 
/* 6538 */           throw new AssertionError(this.retThrowable);
/*      */         }
/*      */ 
/* 6541 */         return this.retVal;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JTable
 * JD-Core Version:    0.6.2
 */