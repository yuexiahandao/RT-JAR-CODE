/*      */ package javax.swing.table;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.Transient;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.util.Locale;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleAction;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JComponent.AccessibleJComponent;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.ToolTipManager;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.TableColumnModelEvent;
/*      */ import javax.swing.event.TableColumnModelListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TableHeaderUI;
/*      */ import sun.swing.table.DefaultTableCellHeaderRenderer;
/*      */ 
/*      */ public class JTableHeader extends JComponent
/*      */   implements TableColumnModelListener, Accessible
/*      */ {
/*      */   private static final String uiClassID = "TableHeaderUI";
/*      */   protected JTable table;
/*      */   protected TableColumnModel columnModel;
/*      */   protected boolean reorderingAllowed;
/*      */   protected boolean resizingAllowed;
/*      */   protected boolean updateTableInRealTime;
/*      */   protected transient TableColumn resizingColumn;
/*      */   protected transient TableColumn draggedColumn;
/*      */   protected transient int draggedDistance;
/*      */   private TableCellRenderer defaultRenderer;
/*      */ 
/*      */   public JTableHeader()
/*      */   {
/*  133 */     this(null);
/*      */   }
/*      */ 
/*      */   public JTableHeader(TableColumnModel paramTableColumnModel)
/*      */   {
/*  151 */     if (paramTableColumnModel == null)
/*  152 */       paramTableColumnModel = createDefaultColumnModel();
/*  153 */     setColumnModel(paramTableColumnModel);
/*      */ 
/*  156 */     initializeLocalVars();
/*      */ 
/*  159 */     updateUI();
/*      */   }
/*      */ 
/*      */   public void setTable(JTable paramJTable)
/*      */   {
/*  174 */     JTable localJTable = this.table;
/*  175 */     this.table = paramJTable;
/*  176 */     firePropertyChange("table", localJTable, paramJTable);
/*      */   }
/*      */ 
/*      */   public JTable getTable()
/*      */   {
/*  184 */     return this.table;
/*      */   }
/*      */ 
/*      */   public void setReorderingAllowed(boolean paramBoolean)
/*      */   {
/*  198 */     boolean bool = this.reorderingAllowed;
/*  199 */     this.reorderingAllowed = paramBoolean;
/*  200 */     firePropertyChange("reorderingAllowed", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getReorderingAllowed()
/*      */   {
/*  212 */     return this.reorderingAllowed;
/*      */   }
/*      */ 
/*      */   public void setResizingAllowed(boolean paramBoolean)
/*      */   {
/*  226 */     boolean bool = this.resizingAllowed;
/*  227 */     this.resizingAllowed = paramBoolean;
/*  228 */     firePropertyChange("resizingAllowed", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getResizingAllowed()
/*      */   {
/*  240 */     return this.resizingAllowed;
/*      */   }
/*      */ 
/*      */   public TableColumn getDraggedColumn()
/*      */   {
/*  252 */     return this.draggedColumn;
/*      */   }
/*      */ 
/*      */   public int getDraggedDistance()
/*      */   {
/*  266 */     return this.draggedDistance;
/*      */   }
/*      */ 
/*      */   public TableColumn getResizingColumn()
/*      */   {
/*  277 */     return this.resizingColumn;
/*      */   }
/*      */ 
/*      */   public void setUpdateTableInRealTime(boolean paramBoolean)
/*      */   {
/*  293 */     this.updateTableInRealTime = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getUpdateTableInRealTime()
/*      */   {
/*  310 */     return this.updateTableInRealTime;
/*      */   }
/*      */ 
/*      */   public void setDefaultRenderer(TableCellRenderer paramTableCellRenderer)
/*      */   {
/*  320 */     this.defaultRenderer = paramTableCellRenderer;
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public TableCellRenderer getDefaultRenderer()
/*      */   {
/*  331 */     return this.defaultRenderer;
/*      */   }
/*      */ 
/*      */   public int columnAtPoint(Point paramPoint)
/*      */   {
/*  342 */     int i = paramPoint.x;
/*  343 */     if (!getComponentOrientation().isLeftToRight()) {
/*  344 */       i = getWidthInRightToLeft() - i - 1;
/*      */     }
/*  346 */     return getColumnModel().getColumnIndexAtX(i);
/*      */   }
/*      */ 
/*      */   public Rectangle getHeaderRect(int paramInt)
/*      */   {
/*  358 */     Rectangle localRectangle = new Rectangle();
/*  359 */     TableColumnModel localTableColumnModel = getColumnModel();
/*      */ 
/*  361 */     localRectangle.height = getHeight();
/*      */ 
/*  363 */     if (paramInt < 0)
/*      */     {
/*  365 */       if (!getComponentOrientation().isLeftToRight()) {
/*  366 */         localRectangle.x = getWidthInRightToLeft();
/*      */       }
/*      */     }
/*  369 */     else if (paramInt >= localTableColumnModel.getColumnCount()) {
/*  370 */       if (getComponentOrientation().isLeftToRight())
/*  371 */         localRectangle.x = getWidth();
/*      */     }
/*      */     else
/*      */     {
/*  375 */       for (int i = 0; i < paramInt; i++) {
/*  376 */         localRectangle.x += localTableColumnModel.getColumn(i).getWidth();
/*      */       }
/*  378 */       if (!getComponentOrientation().isLeftToRight()) {
/*  379 */         localRectangle.x = (getWidthInRightToLeft() - localRectangle.x - localTableColumnModel.getColumn(paramInt).getWidth());
/*      */       }
/*      */ 
/*  382 */       localRectangle.width = localTableColumnModel.getColumn(paramInt).getWidth();
/*      */     }
/*  384 */     return localRectangle;
/*      */   }
/*      */ 
/*      */   public String getToolTipText(MouseEvent paramMouseEvent)
/*      */   {
/*  395 */     String str = null;
/*  396 */     Point localPoint = paramMouseEvent.getPoint();
/*      */     int i;
/*  400 */     if ((i = columnAtPoint(localPoint)) != -1) {
/*  401 */       TableColumn localTableColumn = this.columnModel.getColumn(i);
/*  402 */       TableCellRenderer localTableCellRenderer = localTableColumn.getHeaderRenderer();
/*  403 */       if (localTableCellRenderer == null) {
/*  404 */         localTableCellRenderer = this.defaultRenderer;
/*      */       }
/*  406 */       Component localComponent = localTableCellRenderer.getTableCellRendererComponent(getTable(), localTableColumn.getHeaderValue(), false, false, -1, i);
/*      */ 
/*  412 */       if ((localComponent instanceof JComponent))
/*      */       {
/*  415 */         Rectangle localRectangle = getHeaderRect(i);
/*      */ 
/*  417 */         localPoint.translate(-localRectangle.x, -localRectangle.y);
/*  418 */         MouseEvent localMouseEvent = new MouseEvent(localComponent, paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*      */ 
/*  424 */         str = ((JComponent)localComponent).getToolTipText(localMouseEvent);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  429 */     if (str == null) {
/*  430 */       str = getToolTipText();
/*      */     }
/*  432 */     return str;
/*      */   }
/*      */ 
/*      */   public TableHeaderUI getUI()
/*      */   {
/*  445 */     return (TableHeaderUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(TableHeaderUI paramTableHeaderUI)
/*      */   {
/*  455 */     if (this.ui != paramTableHeaderUI) {
/*  456 */       super.setUI(paramTableHeaderUI);
/*  457 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  470 */     setUI((TableHeaderUI)UIManager.getUI(this));
/*      */ 
/*  472 */     TableCellRenderer localTableCellRenderer = getDefaultRenderer();
/*  473 */     if ((localTableCellRenderer instanceof Component))
/*  474 */       SwingUtilities.updateComponentTreeUI((Component)localTableCellRenderer);
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  489 */     return "TableHeaderUI";
/*      */   }
/*      */ 
/*      */   public void setColumnModel(TableColumnModel paramTableColumnModel)
/*      */   {
/*  511 */     if (paramTableColumnModel == null) {
/*  512 */       throw new IllegalArgumentException("Cannot set a null ColumnModel");
/*      */     }
/*  514 */     TableColumnModel localTableColumnModel = this.columnModel;
/*  515 */     if (paramTableColumnModel != localTableColumnModel) {
/*  516 */       if (localTableColumnModel != null) {
/*  517 */         localTableColumnModel.removeColumnModelListener(this);
/*      */       }
/*  519 */       this.columnModel = paramTableColumnModel;
/*  520 */       paramTableColumnModel.addColumnModelListener(this);
/*      */ 
/*  522 */       firePropertyChange("columnModel", localTableColumnModel, paramTableColumnModel);
/*  523 */       resizeAndRepaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public TableColumnModel getColumnModel()
/*      */   {
/*  535 */     return this.columnModel;
/*      */   }
/*      */ 
/*      */   public void columnAdded(TableColumnModelEvent paramTableColumnModelEvent)
/*      */   {
/*  551 */     resizeAndRepaint();
/*      */   }
/*      */ 
/*      */   public void columnRemoved(TableColumnModelEvent paramTableColumnModelEvent)
/*      */   {
/*  563 */     resizeAndRepaint();
/*      */   }
/*      */ 
/*      */   public void columnMoved(TableColumnModelEvent paramTableColumnModelEvent)
/*      */   {
/*  575 */     repaint();
/*      */   }
/*      */ 
/*      */   public void columnMarginChanged(ChangeEvent paramChangeEvent)
/*      */   {
/*  587 */     resizeAndRepaint();
/*      */   }
/*      */ 
/*      */   public void columnSelectionChanged(ListSelectionEvent paramListSelectionEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected TableColumnModel createDefaultColumnModel()
/*      */   {
/*  618 */     return new DefaultTableColumnModel();
/*      */   }
/*      */ 
/*      */   protected TableCellRenderer createDefaultRenderer()
/*      */   {
/*  629 */     return new DefaultTableCellHeaderRenderer();
/*      */   }
/*      */ 
/*      */   protected void initializeLocalVars()
/*      */   {
/*  638 */     setOpaque(true);
/*  639 */     this.table = null;
/*  640 */     this.reorderingAllowed = true;
/*  641 */     this.resizingAllowed = true;
/*  642 */     this.draggedColumn = null;
/*  643 */     this.draggedDistance = 0;
/*  644 */     this.resizingColumn = null;
/*  645 */     this.updateTableInRealTime = true;
/*      */ 
/*  649 */     ToolTipManager localToolTipManager = ToolTipManager.sharedInstance();
/*  650 */     localToolTipManager.registerComponent(this);
/*  651 */     setDefaultRenderer(createDefaultRenderer());
/*      */   }
/*      */ 
/*      */   public void resizeAndRepaint()
/*      */   {
/*  659 */     revalidate();
/*  660 */     repaint();
/*      */   }
/*      */ 
/*      */   public void setDraggedColumn(TableColumn paramTableColumn)
/*      */   {
/*  673 */     this.draggedColumn = paramTableColumn;
/*      */   }
/*      */ 
/*      */   public void setDraggedDistance(int paramInt)
/*      */   {
/*  681 */     this.draggedDistance = paramInt;
/*      */   }
/*      */ 
/*      */   public void setResizingColumn(TableColumn paramTableColumn)
/*      */   {
/*  694 */     this.resizingColumn = paramTableColumn;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  703 */     paramObjectOutputStream.defaultWriteObject();
/*  704 */     if ((this.ui != null) && (getUIClassID().equals("TableHeaderUI")))
/*  705 */       this.ui.installUI(this);
/*      */   }
/*      */ 
/*      */   private int getWidthInRightToLeft()
/*      */   {
/*  710 */     if ((this.table != null) && (this.table.getAutoResizeMode() != 0))
/*      */     {
/*  712 */       return this.table.getWidth();
/*      */     }
/*  714 */     return super.getWidth();
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/*  730 */     String str1 = this.reorderingAllowed ? "true" : "false";
/*      */ 
/*  732 */     String str2 = this.resizingAllowed ? "true" : "false";
/*      */ 
/*  734 */     String str3 = this.updateTableInRealTime ? "true" : "false";
/*      */ 
/*  737 */     return super.paramString() + ",draggedDistance=" + this.draggedDistance + ",reorderingAllowed=" + str1 + ",resizingAllowed=" + str2 + ",updateTableInRealTime=" + str3;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/*  758 */     if (this.accessibleContext == null) {
/*  759 */       this.accessibleContext = new AccessibleJTableHeader();
/*      */     }
/*  761 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJTableHeader extends JComponent.AccessibleJComponent
/*      */   {
/*      */     protected AccessibleJTableHeader()
/*      */     {
/*  783 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/*  793 */       return AccessibleRole.PANEL;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/*      */       int i;
/*  809 */       if ((i = JTableHeader.this.columnAtPoint(paramPoint)) != -1) {
/*  810 */         TableColumn localTableColumn = JTableHeader.this.columnModel.getColumn(i);
/*  811 */         TableCellRenderer localTableCellRenderer = localTableColumn.getHeaderRenderer();
/*  812 */         if (localTableCellRenderer == null) {
/*  813 */           if (JTableHeader.this.defaultRenderer != null)
/*  814 */             localTableCellRenderer = JTableHeader.this.defaultRenderer;
/*      */           else {
/*  816 */             return null;
/*      */           }
/*      */         }
/*  819 */         Component localComponent = localTableCellRenderer.getTableCellRendererComponent(JTableHeader.this.getTable(), localTableColumn.getHeaderValue(), false, false, -1, i);
/*      */ 
/*  824 */         return new AccessibleJTableHeaderEntry(i, JTableHeader.this, JTableHeader.this.table);
/*      */       }
/*  826 */       return null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/*  838 */       return JTableHeader.this.columnModel.getColumnCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/*  848 */       if ((paramInt < 0) || (paramInt >= getAccessibleChildrenCount())) {
/*  849 */         return null;
/*      */       }
/*  851 */       TableColumn localTableColumn = JTableHeader.this.columnModel.getColumn(paramInt);
/*      */ 
/*  853 */       TableCellRenderer localTableCellRenderer = localTableColumn.getHeaderRenderer();
/*  854 */       if (localTableCellRenderer == null) {
/*  855 */         if (JTableHeader.this.defaultRenderer != null)
/*  856 */           localTableCellRenderer = JTableHeader.this.defaultRenderer;
/*      */         else {
/*  858 */           return null;
/*      */         }
/*      */       }
/*  861 */       Component localComponent = localTableCellRenderer.getTableCellRendererComponent(JTableHeader.this.getTable(), localTableColumn.getHeaderValue(), false, false, -1, paramInt);
/*      */ 
/*  866 */       return new AccessibleJTableHeaderEntry(paramInt, JTableHeader.this, JTableHeader.this.table);
/*      */     }
/*      */ 
/*      */     protected class AccessibleJTableHeaderEntry extends AccessibleContext
/*      */       implements Accessible, AccessibleComponent
/*      */     {
/*      */       private JTableHeader parent;
/*      */       private int column;
/*      */       private JTable table;
/*      */ 
/*      */       public AccessibleJTableHeaderEntry(int paramJTableHeader, JTableHeader paramJTable, JTable arg4)
/*      */       {
/*  886 */         this.parent = paramJTable;
/*  887 */         this.column = paramJTableHeader;
/*      */         Object localObject;
/*  888 */         this.table = localObject;
/*  889 */         setAccessibleParent(this.parent);
/*      */       }
/*      */ 
/*      */       public AccessibleContext getAccessibleContext()
/*      */       {
/*  901 */         return this;
/*      */       }
/*      */ 
/*      */       private AccessibleContext getCurrentAccessibleContext() {
/*  905 */         TableColumnModel localTableColumnModel = this.table.getColumnModel();
/*  906 */         if (localTableColumnModel != null)
/*      */         {
/*  909 */           if ((this.column < 0) || (this.column >= localTableColumnModel.getColumnCount())) {
/*  910 */             return null;
/*      */           }
/*  912 */           TableColumn localTableColumn = localTableColumnModel.getColumn(this.column);
/*  913 */           TableCellRenderer localTableCellRenderer = localTableColumn.getHeaderRenderer();
/*  914 */           if (localTableCellRenderer == null) {
/*  915 */             if (JTableHeader.this.defaultRenderer != null)
/*  916 */               localTableCellRenderer = JTableHeader.this.defaultRenderer;
/*      */             else {
/*  918 */               return null;
/*      */             }
/*      */           }
/*  921 */           Component localComponent = localTableCellRenderer.getTableCellRendererComponent(JTableHeader.this.getTable(), localTableColumn.getHeaderValue(), false, false, -1, this.column);
/*      */ 
/*  925 */           if ((localComponent instanceof AccessibleComponent)) {
/*  926 */             return ((AccessibleComponent)localComponent).getAccessibleContext();
/*      */           }
/*      */         }
/*  929 */         return null;
/*      */       }
/*      */ 
/*      */       private Component getCurrentComponent() {
/*  933 */         TableColumnModel localTableColumnModel = this.table.getColumnModel();
/*  934 */         if (localTableColumnModel != null)
/*      */         {
/*  937 */           if ((this.column < 0) || (this.column >= localTableColumnModel.getColumnCount())) {
/*  938 */             return null;
/*      */           }
/*  940 */           TableColumn localTableColumn = localTableColumnModel.getColumn(this.column);
/*  941 */           TableCellRenderer localTableCellRenderer = localTableColumn.getHeaderRenderer();
/*  942 */           if (localTableCellRenderer == null) {
/*  943 */             if (JTableHeader.this.defaultRenderer != null)
/*  944 */               localTableCellRenderer = JTableHeader.this.defaultRenderer;
/*      */             else {
/*  946 */               return null;
/*      */             }
/*      */           }
/*  949 */           return localTableCellRenderer.getTableCellRendererComponent(JTableHeader.this.getTable(), localTableColumn.getHeaderValue(), false, false, -1, this.column);
/*      */         }
/*      */ 
/*  954 */         return null;
/*      */       }
/*      */ 
/*      */       public String getAccessibleName()
/*      */       {
/*  961 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/*  962 */         if (localAccessibleContext != null) {
/*  963 */           str = localAccessibleContext.getAccessibleName();
/*  964 */           if ((str != null) && (str != ""))
/*      */           {
/*  966 */             return str;
/*      */           }
/*      */         }
/*  969 */         if ((this.accessibleName != null) && (this.accessibleName != "")) {
/*  970 */           return this.accessibleName;
/*      */         }
/*      */ 
/*  973 */         String str = (String)JTableHeader.this.getClientProperty("AccessibleName");
/*  974 */         if (str != null) {
/*  975 */           return str;
/*      */         }
/*  977 */         return this.table.getColumnName(this.column);
/*      */       }
/*      */ 
/*      */       public void setAccessibleName(String paramString)
/*      */       {
/*  983 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/*  984 */         if (localAccessibleContext != null)
/*  985 */           localAccessibleContext.setAccessibleName(paramString);
/*      */         else
/*  987 */           super.setAccessibleName(paramString);
/*      */       }
/*      */ 
/*      */       public String getAccessibleDescription()
/*      */       {
/*  995 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/*  996 */         if (localAccessibleContext != null) {
/*  997 */           return localAccessibleContext.getAccessibleDescription();
/*      */         }
/*  999 */         return super.getAccessibleDescription();
/*      */       }
/*      */ 
/*      */       public void setAccessibleDescription(String paramString)
/*      */       {
/* 1004 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1005 */         if (localAccessibleContext != null)
/* 1006 */           localAccessibleContext.setAccessibleDescription(paramString);
/*      */         else
/* 1008 */           super.setAccessibleDescription(paramString);
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/* 1013 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1014 */         if (localAccessibleContext != null) {
/* 1015 */           return localAccessibleContext.getAccessibleRole();
/*      */         }
/* 1017 */         return AccessibleRole.COLUMN_HEADER;
/*      */       }
/*      */ 
/*      */       public AccessibleStateSet getAccessibleStateSet()
/*      */       {
/* 1022 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1023 */         if (localAccessibleContext != null) {
/* 1024 */           AccessibleStateSet localAccessibleStateSet = localAccessibleContext.getAccessibleStateSet();
/* 1025 */           if (isShowing()) {
/* 1026 */             localAccessibleStateSet.add(AccessibleState.SHOWING);
/*      */           }
/* 1028 */           return localAccessibleStateSet;
/*      */         }
/* 1030 */         return new AccessibleStateSet();
/*      */       }
/*      */ 
/*      */       public int getAccessibleIndexInParent()
/*      */       {
/* 1035 */         return this.column;
/*      */       }
/*      */ 
/*      */       public int getAccessibleChildrenCount() {
/* 1039 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1040 */         if (localAccessibleContext != null) {
/* 1041 */           return localAccessibleContext.getAccessibleChildrenCount();
/*      */         }
/* 1043 */         return 0;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleChild(int paramInt)
/*      */       {
/* 1048 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1049 */         if (localAccessibleContext != null) {
/* 1050 */           Accessible localAccessible = localAccessibleContext.getAccessibleChild(paramInt);
/* 1051 */           localAccessibleContext.setAccessibleParent(this);
/* 1052 */           return localAccessible;
/*      */         }
/* 1054 */         return null;
/*      */       }
/*      */ 
/*      */       public Locale getLocale()
/*      */       {
/* 1059 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1060 */         if (localAccessibleContext != null) {
/* 1061 */           return localAccessibleContext.getLocale();
/*      */         }
/* 1063 */         return null;
/*      */       }
/*      */ 
/*      */       public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 1068 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1069 */         if (localAccessibleContext != null)
/* 1070 */           localAccessibleContext.addPropertyChangeListener(paramPropertyChangeListener);
/*      */         else
/* 1072 */           super.addPropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 1077 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1078 */         if (localAccessibleContext != null)
/* 1079 */           localAccessibleContext.removePropertyChangeListener(paramPropertyChangeListener);
/*      */         else
/* 1081 */           super.removePropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public AccessibleAction getAccessibleAction()
/*      */       {
/* 1086 */         return getCurrentAccessibleContext().getAccessibleAction();
/*      */       }
/*      */ 
/*      */       public AccessibleComponent getAccessibleComponent()
/*      */       {
/* 1098 */         return this;
/*      */       }
/*      */ 
/*      */       public AccessibleSelection getAccessibleSelection() {
/* 1102 */         return getCurrentAccessibleContext().getAccessibleSelection();
/*      */       }
/*      */ 
/*      */       public AccessibleText getAccessibleText() {
/* 1106 */         return getCurrentAccessibleContext().getAccessibleText();
/*      */       }
/*      */ 
/*      */       public AccessibleValue getAccessibleValue() {
/* 1110 */         return getCurrentAccessibleContext().getAccessibleValue();
/*      */       }
/*      */ 
/*      */       public Color getBackground()
/*      */       {
/* 1117 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1118 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1119 */           return ((AccessibleComponent)localAccessibleContext).getBackground();
/*      */         }
/* 1121 */         Component localComponent = getCurrentComponent();
/* 1122 */         if (localComponent != null) {
/* 1123 */           return localComponent.getBackground();
/*      */         }
/* 1125 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBackground(Color paramColor)
/*      */       {
/* 1131 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1132 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1133 */           ((AccessibleComponent)localAccessibleContext).setBackground(paramColor);
/*      */         } else {
/* 1135 */           Component localComponent = getCurrentComponent();
/* 1136 */           if (localComponent != null)
/* 1137 */             localComponent.setBackground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Color getForeground()
/*      */       {
/* 1143 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1144 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1145 */           return ((AccessibleComponent)localAccessibleContext).getForeground();
/*      */         }
/* 1147 */         Component localComponent = getCurrentComponent();
/* 1148 */         if (localComponent != null) {
/* 1149 */           return localComponent.getForeground();
/*      */         }
/* 1151 */         return null;
/*      */       }
/*      */ 
/*      */       public void setForeground(Color paramColor)
/*      */       {
/* 1157 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1158 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1159 */           ((AccessibleComponent)localAccessibleContext).setForeground(paramColor);
/*      */         } else {
/* 1161 */           Component localComponent = getCurrentComponent();
/* 1162 */           if (localComponent != null)
/* 1163 */             localComponent.setForeground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Cursor getCursor()
/*      */       {
/* 1169 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1170 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1171 */           return ((AccessibleComponent)localAccessibleContext).getCursor();
/*      */         }
/* 1173 */         Component localComponent = getCurrentComponent();
/* 1174 */         if (localComponent != null) {
/* 1175 */           return localComponent.getCursor();
/*      */         }
/* 1177 */         Accessible localAccessible = getAccessibleParent();
/* 1178 */         if ((localAccessible instanceof AccessibleComponent)) {
/* 1179 */           return ((AccessibleComponent)localAccessible).getCursor();
/*      */         }
/* 1181 */         return null;
/*      */       }
/*      */ 
/*      */       public void setCursor(Cursor paramCursor)
/*      */       {
/* 1188 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1189 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1190 */           ((AccessibleComponent)localAccessibleContext).setCursor(paramCursor);
/*      */         } else {
/* 1192 */           Component localComponent = getCurrentComponent();
/* 1193 */           if (localComponent != null)
/* 1194 */             localComponent.setCursor(paramCursor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Font getFont()
/*      */       {
/* 1200 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1201 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1202 */           return ((AccessibleComponent)localAccessibleContext).getFont();
/*      */         }
/* 1204 */         Component localComponent = getCurrentComponent();
/* 1205 */         if (localComponent != null) {
/* 1206 */           return localComponent.getFont();
/*      */         }
/* 1208 */         return null;
/*      */       }
/*      */ 
/*      */       public void setFont(Font paramFont)
/*      */       {
/* 1214 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1215 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1216 */           ((AccessibleComponent)localAccessibleContext).setFont(paramFont);
/*      */         } else {
/* 1218 */           Component localComponent = getCurrentComponent();
/* 1219 */           if (localComponent != null)
/* 1220 */             localComponent.setFont(paramFont);
/*      */         }
/*      */       }
/*      */ 
/*      */       public FontMetrics getFontMetrics(Font paramFont)
/*      */       {
/* 1226 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1227 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1228 */           return ((AccessibleComponent)localAccessibleContext).getFontMetrics(paramFont);
/*      */         }
/* 1230 */         Component localComponent = getCurrentComponent();
/* 1231 */         if (localComponent != null) {
/* 1232 */           return localComponent.getFontMetrics(paramFont);
/*      */         }
/* 1234 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isEnabled()
/*      */       {
/* 1240 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1241 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1242 */           return ((AccessibleComponent)localAccessibleContext).isEnabled();
/*      */         }
/* 1244 */         Component localComponent = getCurrentComponent();
/* 1245 */         if (localComponent != null) {
/* 1246 */           return localComponent.isEnabled();
/*      */         }
/* 1248 */         return false;
/*      */       }
/*      */ 
/*      */       public void setEnabled(boolean paramBoolean)
/*      */       {
/* 1254 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1255 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1256 */           ((AccessibleComponent)localAccessibleContext).setEnabled(paramBoolean);
/*      */         } else {
/* 1258 */           Component localComponent = getCurrentComponent();
/* 1259 */           if (localComponent != null)
/* 1260 */             localComponent.setEnabled(paramBoolean);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isVisible()
/*      */       {
/* 1266 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1267 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1268 */           return ((AccessibleComponent)localAccessibleContext).isVisible();
/*      */         }
/* 1270 */         Component localComponent = getCurrentComponent();
/* 1271 */         if (localComponent != null) {
/* 1272 */           return localComponent.isVisible();
/*      */         }
/* 1274 */         return false;
/*      */       }
/*      */ 
/*      */       public void setVisible(boolean paramBoolean)
/*      */       {
/* 1280 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1281 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1282 */           ((AccessibleComponent)localAccessibleContext).setVisible(paramBoolean);
/*      */         } else {
/* 1284 */           Component localComponent = getCurrentComponent();
/* 1285 */           if (localComponent != null)
/* 1286 */             localComponent.setVisible(paramBoolean);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isShowing()
/*      */       {
/* 1292 */         if ((isVisible()) && (JTableHeader.this.isShowing())) {
/* 1293 */           return true;
/*      */         }
/* 1295 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean contains(Point paramPoint)
/*      */       {
/* 1300 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1301 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1302 */           localObject = ((AccessibleComponent)localAccessibleContext).getBounds();
/* 1303 */           return ((Rectangle)localObject).contains(paramPoint);
/*      */         }
/* 1305 */         Object localObject = getCurrentComponent();
/* 1306 */         if (localObject != null) {
/* 1307 */           Rectangle localRectangle = ((Component)localObject).getBounds();
/* 1308 */           return localRectangle.contains(paramPoint);
/*      */         }
/* 1310 */         return getBounds().contains(paramPoint);
/*      */       }
/*      */ 
/*      */       public Point getLocationOnScreen()
/*      */       {
/* 1316 */         if (this.parent != null) {
/* 1317 */           Point localPoint1 = this.parent.getLocationOnScreen();
/* 1318 */           Point localPoint2 = getLocation();
/* 1319 */           localPoint2.translate(localPoint1.x, localPoint1.y);
/* 1320 */           return localPoint2;
/*      */         }
/* 1322 */         return null;
/*      */       }
/*      */ 
/*      */       public Point getLocation()
/*      */       {
/* 1327 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1328 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1329 */           localObject = ((AccessibleComponent)localAccessibleContext).getBounds();
/* 1330 */           return ((Rectangle)localObject).getLocation();
/*      */         }
/* 1332 */         Object localObject = getCurrentComponent();
/* 1333 */         if (localObject != null) {
/* 1334 */           Rectangle localRectangle = ((Component)localObject).getBounds();
/* 1335 */           return localRectangle.getLocation();
/*      */         }
/* 1337 */         return getBounds().getLocation();
/*      */       }
/*      */ 
/*      */       public void setLocation(Point paramPoint)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Rectangle getBounds()
/*      */       {
/* 1349 */         Rectangle localRectangle = this.table.getCellRect(-1, this.column, false);
/* 1350 */         localRectangle.y = 0;
/* 1351 */         return localRectangle;
/*      */       }
/*      */ 
/*      */       public void setBounds(Rectangle paramRectangle)
/*      */       {
/* 1369 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1370 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1371 */           ((AccessibleComponent)localAccessibleContext).setBounds(paramRectangle);
/*      */         } else {
/* 1373 */           Component localComponent = getCurrentComponent();
/* 1374 */           if (localComponent != null)
/* 1375 */             localComponent.setBounds(paramRectangle);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Dimension getSize()
/*      */       {
/* 1381 */         return getBounds().getSize();
/*      */       }
/*      */ 
/*      */       public void setSize(Dimension paramDimension)
/*      */       {
/* 1398 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1399 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1400 */           ((AccessibleComponent)localAccessibleContext).setSize(paramDimension);
/*      */         } else {
/* 1402 */           Component localComponent = getCurrentComponent();
/* 1403 */           if (localComponent != null)
/* 1404 */             localComponent.setSize(paramDimension);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleAt(Point paramPoint)
/*      */       {
/* 1410 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1411 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1412 */           return ((AccessibleComponent)localAccessibleContext).getAccessibleAt(paramPoint);
/*      */         }
/* 1414 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isFocusTraversable()
/*      */       {
/* 1419 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1420 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1421 */           return ((AccessibleComponent)localAccessibleContext).isFocusTraversable();
/*      */         }
/* 1423 */         Component localComponent = getCurrentComponent();
/* 1424 */         if (localComponent != null) {
/* 1425 */           return localComponent.isFocusTraversable();
/*      */         }
/* 1427 */         return false;
/*      */       }
/*      */ 
/*      */       public void requestFocus()
/*      */       {
/* 1433 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1434 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1435 */           ((AccessibleComponent)localAccessibleContext).requestFocus();
/*      */         } else {
/* 1437 */           Component localComponent = getCurrentComponent();
/* 1438 */           if (localComponent != null)
/* 1439 */             localComponent.requestFocus();
/*      */         }
/*      */       }
/*      */ 
/*      */       public void addFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 1445 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1446 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1447 */           ((AccessibleComponent)localAccessibleContext).addFocusListener(paramFocusListener);
/*      */         } else {
/* 1449 */           Component localComponent = getCurrentComponent();
/* 1450 */           if (localComponent != null)
/* 1451 */             localComponent.addFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void removeFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 1457 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 1458 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 1459 */           ((AccessibleComponent)localAccessibleContext).removeFocusListener(paramFocusListener);
/*      */         } else {
/* 1461 */           Component localComponent = getCurrentComponent();
/* 1462 */           if (localComponent != null)
/* 1463 */             localComponent.removeFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.table.JTableHeader
 * JD-Core Version:    0.6.2
 */