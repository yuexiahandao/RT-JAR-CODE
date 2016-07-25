/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.Enumeration;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.CellEditor;
/*      */ import javax.swing.CellRendererPane;
/*      */ import javax.swing.DefaultListSelectionModel;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTable.DropLocation;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListSelectionModel;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.event.MouseInputListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TableHeaderUI;
/*      */ import javax.swing.plaf.TableUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.table.DefaultTableCellRenderer;
/*      */ import javax.swing.table.JTableHeader;
/*      */ import javax.swing.table.TableCellEditor;
/*      */ import javax.swing.table.TableCellRenderer;
/*      */ import javax.swing.table.TableColumn;
/*      */ import javax.swing.table.TableColumnModel;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicTableUI extends TableUI
/*      */ {
/*   59 */   private static final StringBuilder BASELINE_COMPONENT_KEY = new StringBuilder("Table.baselineComponent");
/*      */   protected JTable table;
/*      */   protected CellRendererPane rendererPane;
/*      */   protected KeyListener keyListener;
/*      */   protected FocusListener focusListener;
/*      */   protected MouseInputListener mouseInputListener;
/*      */   private Handler handler;
/*      */   private boolean isFileList;
/* 2135 */   private static final TransferHandler defaultTransferHandler = new TableTransferHandler();
/*      */ 
/*      */   public BasicTableUI()
/*      */   {
/*   80 */     this.isFileList = false;
/*      */   }
/*      */ 
/*      */   private boolean pointOutsidePrefSize(int paramInt1, int paramInt2, Point paramPoint)
/*      */   {
/* 1334 */     if (!this.isFileList) {
/* 1335 */       return false;
/*      */     }
/*      */ 
/* 1338 */     return SwingUtilities2.pointOutsidePrefSize(this.table, paramInt1, paramInt2, paramPoint);
/*      */   }
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/* 1346 */     if (this.handler == null) {
/* 1347 */       this.handler = new Handler(null);
/*      */     }
/* 1349 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected KeyListener createKeyListener()
/*      */   {
/* 1356 */     return null;
/*      */   }
/*      */ 
/*      */   protected FocusListener createFocusListener()
/*      */   {
/* 1363 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected MouseInputListener createMouseInputListener()
/*      */   {
/* 1370 */     return getHandler();
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/* 1378 */     return new BasicTableUI();
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/* 1384 */     this.table = ((JTable)paramJComponent);
/*      */ 
/* 1386 */     this.rendererPane = new CellRendererPane();
/* 1387 */     this.table.add(this.rendererPane);
/* 1388 */     installDefaults();
/* 1389 */     installDefaults2();
/* 1390 */     installListeners();
/* 1391 */     installKeyboardActions();
/*      */   }
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/* 1403 */     LookAndFeel.installColorsAndFont(this.table, "Table.background", "Table.foreground", "Table.font");
/*      */ 
/* 1413 */     LookAndFeel.installProperty(this.table, "opaque", Boolean.TRUE);
/*      */ 
/* 1415 */     Color localColor1 = this.table.getSelectionBackground();
/* 1416 */     if ((localColor1 == null) || ((localColor1 instanceof UIResource))) {
/* 1417 */       localColor1 = UIManager.getColor("Table.selectionBackground");
/* 1418 */       this.table.setSelectionBackground(localColor1 != null ? localColor1 : UIManager.getColor("textHighlight"));
/*      */     }
/*      */ 
/* 1421 */     Color localColor2 = this.table.getSelectionForeground();
/* 1422 */     if ((localColor2 == null) || ((localColor2 instanceof UIResource))) {
/* 1423 */       localColor2 = UIManager.getColor("Table.selectionForeground");
/* 1424 */       this.table.setSelectionForeground(localColor2 != null ? localColor2 : UIManager.getColor("textHighlightText"));
/*      */     }
/*      */ 
/* 1427 */     Color localColor3 = this.table.getGridColor();
/* 1428 */     if ((localColor3 == null) || ((localColor3 instanceof UIResource))) {
/* 1429 */       localColor3 = UIManager.getColor("Table.gridColor");
/* 1430 */       this.table.setGridColor(localColor3 != null ? localColor3 : Color.GRAY);
/*      */     }
/*      */ 
/* 1434 */     Container localContainer = SwingUtilities.getUnwrappedParent(this.table);
/* 1435 */     if (localContainer != null) {
/* 1436 */       localContainer = localContainer.getParent();
/* 1437 */       if ((localContainer != null) && ((localContainer instanceof JScrollPane))) {
/* 1438 */         LookAndFeel.installBorder((JScrollPane)localContainer, "Table.scrollPaneBorder");
/*      */       }
/*      */     }
/*      */ 
/* 1442 */     this.isFileList = Boolean.TRUE.equals(this.table.getClientProperty("Table.isFileList"));
/*      */   }
/*      */ 
/*      */   private void installDefaults2() {
/* 1446 */     TransferHandler localTransferHandler = this.table.getTransferHandler();
/* 1447 */     if ((localTransferHandler == null) || ((localTransferHandler instanceof UIResource))) {
/* 1448 */       this.table.setTransferHandler(defaultTransferHandler);
/*      */ 
/* 1451 */       if ((this.table.getDropTarget() instanceof UIResource))
/* 1452 */         this.table.setDropTarget(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/* 1461 */     this.focusListener = createFocusListener();
/* 1462 */     this.keyListener = createKeyListener();
/* 1463 */     this.mouseInputListener = createMouseInputListener();
/*      */ 
/* 1465 */     this.table.addFocusListener(this.focusListener);
/* 1466 */     this.table.addKeyListener(this.keyListener);
/* 1467 */     this.table.addMouseListener(this.mouseInputListener);
/* 1468 */     this.table.addMouseMotionListener(this.mouseInputListener);
/* 1469 */     this.table.addPropertyChangeListener(getHandler());
/* 1470 */     if (this.isFileList)
/* 1471 */       this.table.getSelectionModel().addListSelectionListener(getHandler());
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/* 1479 */     LazyActionMap.installLazyActionMap(this.table, BasicTableUI.class, "Table.actionMap");
/*      */ 
/* 1482 */     InputMap localInputMap = getInputMap(1);
/*      */ 
/* 1484 */     SwingUtilities.replaceUIInputMap(this.table, 1, localInputMap);
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt)
/*      */   {
/* 1490 */     if (paramInt == 1) {
/* 1491 */       InputMap localInputMap1 = (InputMap)DefaultLookup.get(this.table, this, "Table.ancestorInputMap");
/*      */       InputMap localInputMap2;
/* 1496 */       if ((this.table.getComponentOrientation().isLeftToRight()) || ((localInputMap2 = (InputMap)DefaultLookup.get(this.table, this, "Table.ancestorInputMap.RightToLeft")) == null))
/*      */       {
/* 1499 */         return localInputMap1;
/*      */       }
/* 1501 */       localInputMap2.setParent(localInputMap1);
/* 1502 */       return localInputMap2;
/*      */     }
/*      */ 
/* 1505 */     return null;
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/* 1521 */     paramLazyActionMap.put(new Actions("selectNextColumn", 1, 0, false, false));
/*      */ 
/* 1523 */     paramLazyActionMap.put(new Actions("selectNextColumnChangeLead", 1, 0, false, false));
/*      */ 
/* 1525 */     paramLazyActionMap.put(new Actions("selectPreviousColumn", -1, 0, false, false));
/*      */ 
/* 1527 */     paramLazyActionMap.put(new Actions("selectPreviousColumnChangeLead", -1, 0, false, false));
/*      */ 
/* 1529 */     paramLazyActionMap.put(new Actions("selectNextRow", 0, 1, false, false));
/*      */ 
/* 1531 */     paramLazyActionMap.put(new Actions("selectNextRowChangeLead", 0, 1, false, false));
/*      */ 
/* 1533 */     paramLazyActionMap.put(new Actions("selectPreviousRow", 0, -1, false, false));
/*      */ 
/* 1535 */     paramLazyActionMap.put(new Actions("selectPreviousRowChangeLead", 0, -1, false, false));
/*      */ 
/* 1537 */     paramLazyActionMap.put(new Actions("selectNextColumnExtendSelection", 1, 0, true, false));
/*      */ 
/* 1539 */     paramLazyActionMap.put(new Actions("selectPreviousColumnExtendSelection", -1, 0, true, false));
/*      */ 
/* 1541 */     paramLazyActionMap.put(new Actions("selectNextRowExtendSelection", 0, 1, true, false));
/*      */ 
/* 1543 */     paramLazyActionMap.put(new Actions("selectPreviousRowExtendSelection", 0, -1, true, false));
/*      */ 
/* 1545 */     paramLazyActionMap.put(new Actions("scrollUpChangeSelection", false, false, true, false));
/*      */ 
/* 1547 */     paramLazyActionMap.put(new Actions("scrollDownChangeSelection", false, true, true, false));
/*      */ 
/* 1549 */     paramLazyActionMap.put(new Actions("selectFirstColumn", false, false, false, true));
/*      */ 
/* 1551 */     paramLazyActionMap.put(new Actions("selectLastColumn", false, true, false, true));
/*      */ 
/* 1554 */     paramLazyActionMap.put(new Actions("scrollUpExtendSelection", true, false, true, false));
/*      */ 
/* 1556 */     paramLazyActionMap.put(new Actions("scrollDownExtendSelection", true, true, true, false));
/*      */ 
/* 1558 */     paramLazyActionMap.put(new Actions("selectFirstColumnExtendSelection", true, false, false, true));
/*      */ 
/* 1560 */     paramLazyActionMap.put(new Actions("selectLastColumnExtendSelection", true, true, false, true));
/*      */ 
/* 1563 */     paramLazyActionMap.put(new Actions("selectFirstRow", false, false, true, true));
/* 1564 */     paramLazyActionMap.put(new Actions("selectLastRow", false, true, true, true));
/*      */ 
/* 1566 */     paramLazyActionMap.put(new Actions("selectFirstRowExtendSelection", true, false, true, true));
/*      */ 
/* 1568 */     paramLazyActionMap.put(new Actions("selectLastRowExtendSelection", true, true, true, true));
/*      */ 
/* 1571 */     paramLazyActionMap.put(new Actions("selectNextColumnCell", 1, 0, false, true));
/*      */ 
/* 1573 */     paramLazyActionMap.put(new Actions("selectPreviousColumnCell", -1, 0, false, true));
/*      */ 
/* 1575 */     paramLazyActionMap.put(new Actions("selectNextRowCell", 0, 1, false, true));
/* 1576 */     paramLazyActionMap.put(new Actions("selectPreviousRowCell", 0, -1, false, true));
/*      */ 
/* 1579 */     paramLazyActionMap.put(new Actions("selectAll"));
/* 1580 */     paramLazyActionMap.put(new Actions("clearSelection"));
/* 1581 */     paramLazyActionMap.put(new Actions("cancel"));
/* 1582 */     paramLazyActionMap.put(new Actions("startEditing"));
/*      */ 
/* 1584 */     paramLazyActionMap.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
/*      */ 
/* 1586 */     paramLazyActionMap.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
/*      */ 
/* 1588 */     paramLazyActionMap.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
/*      */ 
/* 1591 */     paramLazyActionMap.put(new Actions("scrollLeftChangeSelection", false, false, false, false));
/*      */ 
/* 1593 */     paramLazyActionMap.put(new Actions("scrollRightChangeSelection", false, true, false, false));
/*      */ 
/* 1595 */     paramLazyActionMap.put(new Actions("scrollLeftExtendSelection", true, false, false, false));
/*      */ 
/* 1597 */     paramLazyActionMap.put(new Actions("scrollRightExtendSelection", true, true, false, false));
/*      */ 
/* 1600 */     paramLazyActionMap.put(new Actions("addToSelection"));
/* 1601 */     paramLazyActionMap.put(new Actions("toggleAndAnchor"));
/* 1602 */     paramLazyActionMap.put(new Actions("extendTo"));
/* 1603 */     paramLazyActionMap.put(new Actions("moveSelectionTo"));
/* 1604 */     paramLazyActionMap.put(new Actions("focusHeader"));
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/* 1610 */     uninstallDefaults();
/* 1611 */     uninstallListeners();
/* 1612 */     uninstallKeyboardActions();
/*      */ 
/* 1614 */     this.table.remove(this.rendererPane);
/* 1615 */     this.rendererPane = null;
/* 1616 */     this.table = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults() {
/* 1620 */     if ((this.table.getTransferHandler() instanceof UIResource))
/* 1621 */       this.table.setTransferHandler(null);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/* 1626 */     this.table.removeFocusListener(this.focusListener);
/* 1627 */     this.table.removeKeyListener(this.keyListener);
/* 1628 */     this.table.removeMouseListener(this.mouseInputListener);
/* 1629 */     this.table.removeMouseMotionListener(this.mouseInputListener);
/* 1630 */     this.table.removePropertyChangeListener(getHandler());
/* 1631 */     if (this.isFileList) {
/* 1632 */       this.table.getSelectionModel().removeListSelectionListener(getHandler());
/*      */     }
/*      */ 
/* 1635 */     this.focusListener = null;
/* 1636 */     this.keyListener = null;
/* 1637 */     this.mouseInputListener = null;
/* 1638 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions() {
/* 1642 */     SwingUtilities.replaceUIInputMap(this.table, 1, null);
/*      */ 
/* 1644 */     SwingUtilities.replaceUIActionMap(this.table, null);
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/* 1656 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 1657 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 1658 */     Component localComponent = (Component)localUIDefaults.get(BASELINE_COMPONENT_KEY);
/*      */ 
/* 1660 */     if (localComponent == null) {
/* 1661 */       DefaultTableCellRenderer localDefaultTableCellRenderer = new DefaultTableCellRenderer();
/* 1662 */       localComponent = localDefaultTableCellRenderer.getTableCellRendererComponent(this.table, "a", false, false, -1, -1);
/*      */ 
/* 1664 */       localUIDefaults.put(BASELINE_COMPONENT_KEY, localComponent);
/*      */     }
/* 1666 */     localComponent.setFont(this.table.getFont());
/* 1667 */     int i = this.table.getRowMargin();
/* 1668 */     return localComponent.getBaseline(2147483647, this.table.getRowHeight() - i) + i / 2;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/* 1682 */     super.getBaselineResizeBehavior(paramJComponent);
/* 1683 */     return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */   }
/*      */ 
/*      */   private Dimension createTableSize(long paramLong)
/*      */   {
/* 1691 */     int i = 0;
/* 1692 */     int j = this.table.getRowCount();
/* 1693 */     if ((j > 0) && (this.table.getColumnCount() > 0)) {
/* 1694 */       Rectangle localRectangle = this.table.getCellRect(j - 1, 0, true);
/* 1695 */       i = localRectangle.y + localRectangle.height;
/*      */     }
/*      */ 
/* 1699 */     long l = Math.abs(paramLong);
/* 1700 */     if (l > 2147483647L) {
/* 1701 */       l = 2147483647L;
/*      */     }
/* 1703 */     return new Dimension((int)l, i);
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/* 1712 */     long l = 0L;
/* 1713 */     Enumeration localEnumeration = this.table.getColumnModel().getColumns();
/* 1714 */     while (localEnumeration.hasMoreElements()) {
/* 1715 */       TableColumn localTableColumn = (TableColumn)localEnumeration.nextElement();
/* 1716 */       l += localTableColumn.getMinWidth();
/*      */     }
/* 1718 */     return createTableSize(l);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/* 1727 */     long l = 0L;
/* 1728 */     Enumeration localEnumeration = this.table.getColumnModel().getColumns();
/* 1729 */     while (localEnumeration.hasMoreElements()) {
/* 1730 */       TableColumn localTableColumn = (TableColumn)localEnumeration.nextElement();
/* 1731 */       l += localTableColumn.getPreferredWidth();
/*      */     }
/* 1733 */     return createTableSize(l);
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/* 1742 */     long l = 0L;
/* 1743 */     Enumeration localEnumeration = this.table.getColumnModel().getColumns();
/* 1744 */     while (localEnumeration.hasMoreElements()) {
/* 1745 */       TableColumn localTableColumn = (TableColumn)localEnumeration.nextElement();
/* 1746 */       l += localTableColumn.getMaxWidth();
/*      */     }
/* 1748 */     return createTableSize(l);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/* 1759 */     Rectangle localRectangle1 = paramGraphics.getClipBounds();
/*      */ 
/* 1761 */     Rectangle localRectangle2 = this.table.getBounds();
/*      */ 
/* 1764 */     localRectangle2.x = (localRectangle2.y = 0);
/*      */ 
/* 1766 */     if ((this.table.getRowCount() <= 0) || (this.table.getColumnCount() <= 0) || (!localRectangle2.intersects(localRectangle1)))
/*      */     {
/* 1771 */       paintDropLines(paramGraphics);
/* 1772 */       return;
/*      */     }
/*      */ 
/* 1775 */     boolean bool = this.table.getComponentOrientation().isLeftToRight();
/*      */ 
/* 1777 */     Point localPoint1 = localRectangle1.getLocation();
/* 1778 */     Point localPoint2 = new Point(localRectangle1.x + localRectangle1.width - 1, localRectangle1.y + localRectangle1.height - 1);
/*      */ 
/* 1781 */     int i = this.table.rowAtPoint(localPoint1);
/* 1782 */     int j = this.table.rowAtPoint(localPoint2);
/*      */ 
/* 1785 */     if (i == -1) {
/* 1786 */       i = 0;
/*      */     }
/*      */ 
/* 1792 */     if (j == -1) {
/* 1793 */       j = this.table.getRowCount() - 1;
/*      */     }
/*      */ 
/* 1796 */     int k = this.table.columnAtPoint(bool ? localPoint1 : localPoint2);
/* 1797 */     int m = this.table.columnAtPoint(bool ? localPoint2 : localPoint1);
/*      */ 
/* 1799 */     if (k == -1) {
/* 1800 */       k = 0;
/*      */     }
/*      */ 
/* 1804 */     if (m == -1) {
/* 1805 */       m = this.table.getColumnCount() - 1;
/*      */     }
/*      */ 
/* 1809 */     paintGrid(paramGraphics, i, j, k, m);
/*      */ 
/* 1812 */     paintCells(paramGraphics, i, j, k, m);
/*      */ 
/* 1814 */     paintDropLines(paramGraphics);
/*      */   }
/*      */ 
/*      */   private void paintDropLines(Graphics paramGraphics) {
/* 1818 */     JTable.DropLocation localDropLocation = this.table.getDropLocation();
/* 1819 */     if (localDropLocation == null) {
/* 1820 */       return;
/*      */     }
/*      */ 
/* 1823 */     Color localColor1 = UIManager.getColor("Table.dropLineColor");
/* 1824 */     Color localColor2 = UIManager.getColor("Table.dropLineShortColor");
/* 1825 */     if ((localColor1 == null) && (localColor2 == null)) {
/* 1826 */       return;
/*      */     }
/*      */ 
/* 1831 */     Rectangle localRectangle = getHDropLineRect(localDropLocation);
/*      */     int i;
/*      */     int j;
/* 1832 */     if (localRectangle != null) {
/* 1833 */       i = localRectangle.x;
/* 1834 */       j = localRectangle.width;
/* 1835 */       if (localColor1 != null) {
/* 1836 */         extendRect(localRectangle, true);
/* 1837 */         paramGraphics.setColor(localColor1);
/* 1838 */         paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/* 1840 */       if ((!localDropLocation.isInsertColumn()) && (localColor2 != null)) {
/* 1841 */         paramGraphics.setColor(localColor2);
/* 1842 */         paramGraphics.fillRect(i, localRectangle.y, j, localRectangle.height);
/*      */       }
/*      */     }
/*      */ 
/* 1846 */     localRectangle = getVDropLineRect(localDropLocation);
/* 1847 */     if (localRectangle != null) {
/* 1848 */       i = localRectangle.y;
/* 1849 */       j = localRectangle.height;
/* 1850 */       if (localColor1 != null) {
/* 1851 */         extendRect(localRectangle, false);
/* 1852 */         paramGraphics.setColor(localColor1);
/* 1853 */         paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/* 1855 */       if ((!localDropLocation.isInsertRow()) && (localColor2 != null)) {
/* 1856 */         paramGraphics.setColor(localColor2);
/* 1857 */         paramGraphics.fillRect(localRectangle.x, i, localRectangle.width, j);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Rectangle getHDropLineRect(JTable.DropLocation paramDropLocation) {
/* 1863 */     if (!paramDropLocation.isInsertRow()) {
/* 1864 */       return null;
/*      */     }
/*      */ 
/* 1867 */     int i = paramDropLocation.getRow();
/* 1868 */     int j = paramDropLocation.getColumn();
/* 1869 */     if (j >= this.table.getColumnCount()) {
/* 1870 */       j--;
/*      */     }
/*      */ 
/* 1873 */     Rectangle localRectangle1 = this.table.getCellRect(i, j, true);
/*      */ 
/* 1875 */     if (i >= this.table.getRowCount()) {
/* 1876 */       i--;
/* 1877 */       Rectangle localRectangle2 = this.table.getCellRect(i, j, true);
/* 1878 */       localRectangle2.y += localRectangle2.height;
/*      */     }
/*      */ 
/* 1881 */     if (localRectangle1.y == 0)
/* 1882 */       localRectangle1.y = -1;
/*      */     else {
/* 1884 */       localRectangle1.y -= 2;
/*      */     }
/*      */ 
/* 1887 */     localRectangle1.height = 3;
/*      */ 
/* 1889 */     return localRectangle1;
/*      */   }
/*      */ 
/*      */   private Rectangle getVDropLineRect(JTable.DropLocation paramDropLocation) {
/* 1893 */     if (!paramDropLocation.isInsertColumn()) {
/* 1894 */       return null;
/*      */     }
/*      */ 
/* 1897 */     boolean bool = this.table.getComponentOrientation().isLeftToRight();
/* 1898 */     int i = paramDropLocation.getColumn();
/* 1899 */     Rectangle localRectangle = this.table.getCellRect(paramDropLocation.getRow(), i, true);
/*      */ 
/* 1901 */     if (i >= this.table.getColumnCount()) {
/* 1902 */       i--;
/* 1903 */       localRectangle = this.table.getCellRect(paramDropLocation.getRow(), i, true);
/* 1904 */       if (bool)
/* 1905 */         localRectangle.x += localRectangle.width;
/*      */     }
/* 1907 */     else if (!bool) {
/* 1908 */       localRectangle.x += localRectangle.width;
/*      */     }
/*      */ 
/* 1911 */     if (localRectangle.x == 0)
/* 1912 */       localRectangle.x = -1;
/*      */     else {
/* 1914 */       localRectangle.x -= 2;
/*      */     }
/*      */ 
/* 1917 */     localRectangle.width = 3;
/*      */ 
/* 1919 */     return localRectangle;
/*      */   }
/*      */ 
/*      */   private Rectangle extendRect(Rectangle paramRectangle, boolean paramBoolean) {
/* 1923 */     if (paramRectangle == null) {
/* 1924 */       return paramRectangle;
/*      */     }
/*      */ 
/* 1927 */     if (paramBoolean) {
/* 1928 */       paramRectangle.x = 0;
/* 1929 */       paramRectangle.width = this.table.getWidth();
/*      */     } else {
/* 1931 */       paramRectangle.y = 0;
/*      */ 
/* 1933 */       if (this.table.getRowCount() != 0) {
/* 1934 */         Rectangle localRectangle = this.table.getCellRect(this.table.getRowCount() - 1, 0, true);
/* 1935 */         paramRectangle.height = (localRectangle.y + localRectangle.height);
/*      */       } else {
/* 1937 */         paramRectangle.height = this.table.getHeight();
/*      */       }
/*      */     }
/*      */ 
/* 1941 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   private void paintGrid(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1952 */     paramGraphics.setColor(this.table.getGridColor());
/*      */ 
/* 1954 */     Rectangle localRectangle1 = this.table.getCellRect(paramInt1, paramInt3, true);
/* 1955 */     Rectangle localRectangle2 = this.table.getCellRect(paramInt2, paramInt4, true);
/* 1956 */     Rectangle localRectangle3 = localRectangle1.union(localRectangle2);
/*      */     int j;
/*      */     int k;
/* 1958 */     if (this.table.getShowHorizontalLines()) {
/* 1959 */       int i = localRectangle3.x + localRectangle3.width;
/* 1960 */       j = localRectangle3.y;
/* 1961 */       for (k = paramInt1; k <= paramInt2; k++) {
/* 1962 */         j += this.table.getRowHeight(k);
/* 1963 */         paramGraphics.drawLine(localRectangle3.x, j - 1, i - 1, j - 1);
/*      */       }
/*      */     }
/* 1966 */     if (this.table.getShowVerticalLines()) {
/* 1967 */       TableColumnModel localTableColumnModel = this.table.getColumnModel();
/* 1968 */       j = localRectangle3.y + localRectangle3.height;
/*      */       int m;
/*      */       int n;
/* 1970 */       if (this.table.getComponentOrientation().isLeftToRight()) {
/* 1971 */         k = localRectangle3.x;
/* 1972 */         for (m = paramInt3; m <= paramInt4; m++) {
/* 1973 */           n = localTableColumnModel.getColumn(m).getWidth();
/* 1974 */           k += n;
/* 1975 */           paramGraphics.drawLine(k - 1, 0, k - 1, j - 1);
/*      */         }
/*      */       } else {
/* 1978 */         k = localRectangle3.x;
/* 1979 */         for (m = paramInt4; m >= paramInt3; m--) {
/* 1980 */           n = localTableColumnModel.getColumn(m).getWidth();
/* 1981 */           k += n;
/* 1982 */           paramGraphics.drawLine(k - 1, 0, k - 1, j - 1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int viewIndexForColumn(TableColumn paramTableColumn) {
/* 1989 */     TableColumnModel localTableColumnModel = this.table.getColumnModel();
/* 1990 */     for (int i = 0; i < localTableColumnModel.getColumnCount(); i++) {
/* 1991 */       if (localTableColumnModel.getColumn(i) == paramTableColumn) {
/* 1992 */         return i;
/*      */       }
/*      */     }
/* 1995 */     return -1;
/*      */   }
/*      */ 
/*      */   private void paintCells(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 1999 */     JTableHeader localJTableHeader = this.table.getTableHeader();
/* 2000 */     TableColumn localTableColumn1 = localJTableHeader == null ? null : localJTableHeader.getDraggedColumn();
/*      */ 
/* 2002 */     TableColumnModel localTableColumnModel = this.table.getColumnModel();
/* 2003 */     int i = localTableColumnModel.getColumnMargin();
/*      */     int k;
/*      */     Rectangle localRectangle;
/*      */     int m;
/*      */     TableColumn localTableColumn2;
/*      */     int j;
/* 2008 */     if (this.table.getComponentOrientation().isLeftToRight())
/* 2009 */       for (k = paramInt1; k <= paramInt2; k++) {
/* 2010 */         localRectangle = this.table.getCellRect(k, paramInt3, false);
/* 2011 */         for (m = paramInt3; m <= paramInt4; m++) {
/* 2012 */           localTableColumn2 = localTableColumnModel.getColumn(m);
/* 2013 */           j = localTableColumn2.getWidth();
/* 2014 */           localRectangle.width = (j - i);
/* 2015 */           if (localTableColumn2 != localTableColumn1) {
/* 2016 */             paintCell(paramGraphics, localRectangle, k, m);
/*      */           }
/* 2018 */           localRectangle.x += j;
/*      */         }
/*      */       }
/*      */     else {
/* 2022 */       for (k = paramInt1; k <= paramInt2; k++) {
/* 2023 */         localRectangle = this.table.getCellRect(k, paramInt3, false);
/* 2024 */         localTableColumn2 = localTableColumnModel.getColumn(paramInt3);
/* 2025 */         if (localTableColumn2 != localTableColumn1) {
/* 2026 */           j = localTableColumn2.getWidth();
/* 2027 */           localRectangle.width = (j - i);
/* 2028 */           paintCell(paramGraphics, localRectangle, k, paramInt3);
/*      */         }
/* 2030 */         for (m = paramInt3 + 1; m <= paramInt4; m++) {
/* 2031 */           localTableColumn2 = localTableColumnModel.getColumn(m);
/* 2032 */           j = localTableColumn2.getWidth();
/* 2033 */           localRectangle.width = (j - i);
/* 2034 */           localRectangle.x -= j;
/* 2035 */           if (localTableColumn2 != localTableColumn1) {
/* 2036 */             paintCell(paramGraphics, localRectangle, k, m);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2043 */     if (localTableColumn1 != null) {
/* 2044 */       paintDraggedArea(paramGraphics, paramInt1, paramInt2, localTableColumn1, localJTableHeader.getDraggedDistance());
/*      */     }
/*      */ 
/* 2048 */     this.rendererPane.removeAll();
/*      */   }
/*      */ 
/*      */   private void paintDraggedArea(Graphics paramGraphics, int paramInt1, int paramInt2, TableColumn paramTableColumn, int paramInt3) {
/* 2052 */     int i = viewIndexForColumn(paramTableColumn);
/*      */ 
/* 2054 */     Rectangle localRectangle1 = this.table.getCellRect(paramInt1, i, true);
/* 2055 */     Rectangle localRectangle2 = this.table.getCellRect(paramInt2, i, true);
/*      */ 
/* 2057 */     Rectangle localRectangle3 = localRectangle1.union(localRectangle2);
/*      */ 
/* 2060 */     paramGraphics.setColor(this.table.getParent().getBackground());
/* 2061 */     paramGraphics.fillRect(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*      */ 
/* 2065 */     localRectangle3.x += paramInt3;
/*      */ 
/* 2068 */     paramGraphics.setColor(this.table.getBackground());
/* 2069 */     paramGraphics.fillRect(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*      */     int n;
/* 2073 */     if (this.table.getShowVerticalLines()) {
/* 2074 */       paramGraphics.setColor(this.table.getGridColor());
/* 2075 */       j = localRectangle3.x;
/* 2076 */       int k = localRectangle3.y;
/* 2077 */       int m = j + localRectangle3.width - 1;
/* 2078 */       n = k + localRectangle3.height - 1;
/*      */ 
/* 2080 */       paramGraphics.drawLine(j - 1, k, j - 1, n);
/*      */ 
/* 2082 */       paramGraphics.drawLine(m, k, m, n);
/*      */     }
/*      */ 
/* 2085 */     for (int j = paramInt1; j <= paramInt2; j++)
/*      */     {
/* 2087 */       Rectangle localRectangle4 = this.table.getCellRect(j, i, false);
/* 2088 */       localRectangle4.x += paramInt3;
/* 2089 */       paintCell(paramGraphics, localRectangle4, j, i);
/*      */ 
/* 2092 */       if (this.table.getShowHorizontalLines()) {
/* 2093 */         paramGraphics.setColor(this.table.getGridColor());
/* 2094 */         Rectangle localRectangle5 = this.table.getCellRect(j, i, true);
/* 2095 */         localRectangle5.x += paramInt3;
/* 2096 */         n = localRectangle5.x;
/* 2097 */         int i1 = localRectangle5.y;
/* 2098 */         int i2 = n + localRectangle5.width - 1;
/* 2099 */         int i3 = i1 + localRectangle5.height - 1;
/* 2100 */         paramGraphics.drawLine(n, i3, i2, i3);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintCell(Graphics paramGraphics, Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/*      */     Object localObject;
/* 2106 */     if ((this.table.isEditing()) && (this.table.getEditingRow() == paramInt1) && (this.table.getEditingColumn() == paramInt2))
/*      */     {
/* 2108 */       localObject = this.table.getEditorComponent();
/* 2109 */       ((Component)localObject).setBounds(paramRectangle);
/* 2110 */       ((Component)localObject).validate();
/*      */     }
/*      */     else {
/* 2113 */       localObject = this.table.getCellRenderer(paramInt1, paramInt2);
/* 2114 */       Component localComponent = this.table.prepareRenderer((TableCellRenderer)localObject, paramInt1, paramInt2);
/* 2115 */       this.rendererPane.paintComponent(paramGraphics, localComponent, this.table, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int getAdjustedLead(JTable paramJTable, boolean paramBoolean, ListSelectionModel paramListSelectionModel)
/*      */   {
/* 2124 */     int i = paramListSelectionModel.getLeadSelectionIndex();
/* 2125 */     int j = paramBoolean ? paramJTable.getRowCount() : paramJTable.getColumnCount();
/* 2126 */     return i < j ? i : -1;
/*      */   }
/*      */ 
/*      */   private static int getAdjustedLead(JTable paramJTable, boolean paramBoolean) {
/* 2130 */     return paramBoolean ? getAdjustedLead(paramJTable, paramBoolean, paramJTable.getSelectionModel()) : getAdjustedLead(paramJTable, paramBoolean, paramJTable.getColumnModel().getSelectionModel());
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     private static final String CANCEL_EDITING = "cancel";
/*      */     private static final String SELECT_ALL = "selectAll";
/*      */     private static final String CLEAR_SELECTION = "clearSelection";
/*      */     private static final String START_EDITING = "startEditing";
/*      */     private static final String NEXT_ROW = "selectNextRow";
/*      */     private static final String NEXT_ROW_CELL = "selectNextRowCell";
/*      */     private static final String NEXT_ROW_EXTEND_SELECTION = "selectNextRowExtendSelection";
/*      */     private static final String NEXT_ROW_CHANGE_LEAD = "selectNextRowChangeLead";
/*      */     private static final String PREVIOUS_ROW = "selectPreviousRow";
/*      */     private static final String PREVIOUS_ROW_CELL = "selectPreviousRowCell";
/*      */     private static final String PREVIOUS_ROW_EXTEND_SELECTION = "selectPreviousRowExtendSelection";
/*      */     private static final String PREVIOUS_ROW_CHANGE_LEAD = "selectPreviousRowChangeLead";
/*      */     private static final String NEXT_COLUMN = "selectNextColumn";
/*      */     private static final String NEXT_COLUMN_CELL = "selectNextColumnCell";
/*      */     private static final String NEXT_COLUMN_EXTEND_SELECTION = "selectNextColumnExtendSelection";
/*      */     private static final String NEXT_COLUMN_CHANGE_LEAD = "selectNextColumnChangeLead";
/*      */     private static final String PREVIOUS_COLUMN = "selectPreviousColumn";
/*      */     private static final String PREVIOUS_COLUMN_CELL = "selectPreviousColumnCell";
/*      */     private static final String PREVIOUS_COLUMN_EXTEND_SELECTION = "selectPreviousColumnExtendSelection";
/*      */     private static final String PREVIOUS_COLUMN_CHANGE_LEAD = "selectPreviousColumnChangeLead";
/*      */     private static final String SCROLL_LEFT_CHANGE_SELECTION = "scrollLeftChangeSelection";
/*      */     private static final String SCROLL_LEFT_EXTEND_SELECTION = "scrollLeftExtendSelection";
/*      */     private static final String SCROLL_RIGHT_CHANGE_SELECTION = "scrollRightChangeSelection";
/*      */     private static final String SCROLL_RIGHT_EXTEND_SELECTION = "scrollRightExtendSelection";
/*      */     private static final String SCROLL_UP_CHANGE_SELECTION = "scrollUpChangeSelection";
/*      */     private static final String SCROLL_UP_EXTEND_SELECTION = "scrollUpExtendSelection";
/*      */     private static final String SCROLL_DOWN_CHANGE_SELECTION = "scrollDownChangeSelection";
/*      */     private static final String SCROLL_DOWN_EXTEND_SELECTION = "scrollDownExtendSelection";
/*      */     private static final String FIRST_COLUMN = "selectFirstColumn";
/*      */     private static final String FIRST_COLUMN_EXTEND_SELECTION = "selectFirstColumnExtendSelection";
/*      */     private static final String LAST_COLUMN = "selectLastColumn";
/*      */     private static final String LAST_COLUMN_EXTEND_SELECTION = "selectLastColumnExtendSelection";
/*      */     private static final String FIRST_ROW = "selectFirstRow";
/*      */     private static final String FIRST_ROW_EXTEND_SELECTION = "selectFirstRowExtendSelection";
/*      */     private static final String LAST_ROW = "selectLastRow";
/*      */     private static final String LAST_ROW_EXTEND_SELECTION = "selectLastRowExtendSelection";
/*      */     private static final String ADD_TO_SELECTION = "addToSelection";
/*      */     private static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
/*      */     private static final String EXTEND_TO = "extendTo";
/*      */     private static final String MOVE_SELECTION_TO = "moveSelectionTo";
/*      */     private static final String FOCUS_HEADER = "focusHeader";
/*      */     protected int dx;
/*      */     protected int dy;
/*      */     protected boolean extend;
/*      */     protected boolean inSelection;
/*      */     protected boolean forwards;
/*      */     protected boolean vertically;
/*      */     protected boolean toLimit;
/*      */     protected int leadRow;
/*      */     protected int leadColumn;
/*      */ 
/*      */     Actions(String paramString)
/*      */     {
/*  185 */       super();
/*      */     }
/*      */ 
/*      */     Actions(String paramString, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*  190 */       super();
/*      */ 
/*  198 */       if (paramBoolean2) {
/*  199 */         this.inSelection = true;
/*      */ 
/*  202 */         paramInt1 = sign(paramInt1);
/*  203 */         paramInt2 = sign(paramInt2);
/*      */ 
/*  206 */         assert (((paramInt1 == 0) || (paramInt2 == 0)) && ((paramInt1 != 0) || (paramInt2 != 0)));
/*      */       }
/*      */ 
/*  209 */       this.dx = paramInt1;
/*  210 */       this.dy = paramInt2;
/*  211 */       this.extend = paramBoolean1;
/*      */     }
/*      */ 
/*      */     Actions(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
/*      */     {
/*  216 */       this(paramString, 0, 0, paramBoolean1, false);
/*  217 */       this.forwards = paramBoolean2;
/*  218 */       this.vertically = paramBoolean3;
/*  219 */       this.toLimit = paramBoolean4;
/*      */     }
/*      */ 
/*      */     private static int clipToRange(int paramInt1, int paramInt2, int paramInt3) {
/*  223 */       return Math.min(Math.max(paramInt1, paramInt2), paramInt3 - 1);
/*      */     }
/*      */ 
/*      */     private void moveWithinTableRange(JTable paramJTable, int paramInt1, int paramInt2) {
/*  227 */       this.leadRow = clipToRange(this.leadRow + paramInt2, 0, paramJTable.getRowCount());
/*  228 */       this.leadColumn = clipToRange(this.leadColumn + paramInt1, 0, paramJTable.getColumnCount());
/*      */     }
/*      */ 
/*      */     private static int sign(int paramInt) {
/*  232 */       return paramInt == 0 ? 0 : paramInt < 0 ? -1 : 1;
/*      */     }
/*      */ 
/*      */     private boolean moveWithinSelectedRange(JTable paramJTable, int paramInt1, int paramInt2, ListSelectionModel paramListSelectionModel1, ListSelectionModel paramListSelectionModel2)
/*      */     {
/*  258 */       boolean bool1 = paramJTable.getRowSelectionAllowed();
/*  259 */       boolean bool2 = paramJTable.getColumnSelectionAllowed();
/*      */       int i;
/*      */       int j;
/*      */       int k;
/*      */       int m;
/*      */       int n;
/*  262 */       if ((bool1) && (bool2)) {
/*  263 */         i = paramJTable.getSelectedRowCount() * paramJTable.getSelectedColumnCount();
/*  264 */         j = paramListSelectionModel2.getMinSelectionIndex();
/*  265 */         k = paramListSelectionModel2.getMaxSelectionIndex();
/*  266 */         m = paramListSelectionModel1.getMinSelectionIndex();
/*  267 */         n = paramListSelectionModel1.getMaxSelectionIndex();
/*      */       }
/*  269 */       else if (bool1) {
/*  270 */         i = paramJTable.getSelectedRowCount();
/*  271 */         j = 0;
/*  272 */         k = paramJTable.getColumnCount() - 1;
/*  273 */         m = paramListSelectionModel1.getMinSelectionIndex();
/*  274 */         n = paramListSelectionModel1.getMaxSelectionIndex();
/*      */       }
/*  276 */       else if (bool2) {
/*  277 */         i = paramJTable.getSelectedColumnCount();
/*  278 */         j = paramListSelectionModel2.getMinSelectionIndex();
/*  279 */         k = paramListSelectionModel2.getMaxSelectionIndex();
/*  280 */         m = 0;
/*  281 */         n = paramJTable.getRowCount() - 1;
/*      */       }
/*      */       else {
/*  284 */         i = 0;
/*      */ 
/*  288 */         j = k = m = n = 0;
/*      */       }
/*      */       boolean bool3;
/*  297 */       if ((i == 0) || ((i == 1) && (paramJTable.isCellSelected(this.leadRow, this.leadColumn))))
/*      */       {
/*  301 */         bool3 = false;
/*      */ 
/*  303 */         k = paramJTable.getColumnCount() - 1;
/*  304 */         n = paramJTable.getRowCount() - 1;
/*      */ 
/*  307 */         j = Math.min(0, k);
/*  308 */         m = Math.min(0, n);
/*      */       } else {
/*  310 */         bool3 = true;
/*      */       }
/*      */ 
/*  315 */       if ((paramInt2 == 1) && (this.leadColumn == -1)) {
/*  316 */         this.leadColumn = j;
/*  317 */         this.leadRow = -1;
/*  318 */       } else if ((paramInt1 == 1) && (this.leadRow == -1)) {
/*  319 */         this.leadRow = m;
/*  320 */         this.leadColumn = -1;
/*  321 */       } else if ((paramInt2 == -1) && (this.leadColumn == -1)) {
/*  322 */         this.leadColumn = k;
/*  323 */         this.leadRow = (n + 1);
/*  324 */       } else if ((paramInt1 == -1) && (this.leadRow == -1)) {
/*  325 */         this.leadRow = n;
/*  326 */         this.leadColumn = (k + 1);
/*      */       }
/*      */ 
/*  332 */       this.leadRow = Math.min(Math.max(this.leadRow, m - 1), n + 1);
/*  333 */       this.leadColumn = Math.min(Math.max(this.leadColumn, j - 1), k + 1);
/*      */       do
/*      */       {
/*  337 */         calcNextPos(paramInt1, j, k, paramInt2, m, n);
/*  338 */       }while ((bool3) && (!paramJTable.isCellSelected(this.leadRow, this.leadColumn)));
/*      */ 
/*  340 */       return bool3;
/*      */     }
/*      */ 
/*      */     private void calcNextPos(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */     {
/*  350 */       if (paramInt1 != 0) {
/*  351 */         this.leadColumn += paramInt1;
/*  352 */         if (this.leadColumn > paramInt3) {
/*  353 */           this.leadColumn = paramInt2;
/*  354 */           this.leadRow += 1;
/*  355 */           if (this.leadRow > paramInt6)
/*  356 */             this.leadRow = paramInt5;
/*      */         }
/*  358 */         else if (this.leadColumn < paramInt2) {
/*  359 */           this.leadColumn = paramInt3;
/*  360 */           this.leadRow -= 1;
/*  361 */           if (this.leadRow < paramInt5)
/*  362 */             this.leadRow = paramInt6;
/*      */         }
/*      */       }
/*      */       else {
/*  366 */         this.leadRow += paramInt4;
/*  367 */         if (this.leadRow > paramInt6) {
/*  368 */           this.leadRow = paramInt5;
/*  369 */           this.leadColumn += 1;
/*  370 */           if (this.leadColumn > paramInt3)
/*  371 */             this.leadColumn = paramInt2;
/*      */         }
/*  373 */         else if (this.leadRow < paramInt5) {
/*  374 */           this.leadRow = paramInt6;
/*  375 */           this.leadColumn -= 1;
/*  376 */           if (this.leadColumn < paramInt2)
/*  377 */             this.leadColumn = paramInt3;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  384 */       String str = getName();
/*  385 */       JTable localJTable = (JTable)paramActionEvent.getSource();
/*      */ 
/*  387 */       ListSelectionModel localListSelectionModel1 = localJTable.getSelectionModel();
/*  388 */       this.leadRow = BasicTableUI.getAdjustedLead(localJTable, true, localListSelectionModel1);
/*      */ 
/*  390 */       ListSelectionModel localListSelectionModel2 = localJTable.getColumnModel().getSelectionModel();
/*  391 */       this.leadColumn = BasicTableUI.getAdjustedLead(localJTable, false, localListSelectionModel2);
/*      */       Rectangle localRectangle;
/*  393 */       if ((str == "scrollLeftChangeSelection") || (str == "scrollLeftExtendSelection") || (str == "scrollRightChangeSelection") || (str == "scrollRightExtendSelection") || (str == "scrollUpChangeSelection") || (str == "scrollUpExtendSelection") || (str == "scrollDownChangeSelection") || (str == "scrollDownExtendSelection") || (str == "selectFirstColumn") || (str == "selectFirstColumnExtendSelection") || (str == "selectFirstRow") || (str == "selectFirstRowExtendSelection") || (str == "selectLastColumn") || (str == "selectLastColumnExtendSelection") || (str == "selectLastRow") || (str == "selectLastRowExtendSelection"))
/*      */       {
/*  409 */         if (this.toLimit)
/*      */         {
/*      */           int i;
/*  410 */           if (this.vertically) {
/*  411 */             i = localJTable.getRowCount();
/*  412 */             this.dx = 0;
/*  413 */             this.dy = (this.forwards ? i : -i);
/*      */           }
/*      */           else {
/*  416 */             i = localJTable.getColumnCount();
/*  417 */             this.dx = (this.forwards ? i : -i);
/*  418 */             this.dy = 0;
/*      */           }
/*      */         }
/*      */         else {
/*  422 */           if (!(SwingUtilities.getUnwrappedParent(localJTable).getParent() instanceof JScrollPane))
/*      */           {
/*  424 */             return;
/*      */           }
/*      */ 
/*  427 */           Dimension localDimension = localJTable.getParent().getSize();
/*      */           int n;
/*  429 */           if (this.vertically) {
/*  430 */             localRectangle = localJTable.getCellRect(this.leadRow, 0, true);
/*  431 */             if (this.forwards)
/*      */             {
/*  433 */               localRectangle.y += Math.max(localDimension.height, localRectangle.height);
/*      */             }
/*  435 */             else localRectangle.y -= localDimension.height;
/*      */ 
/*  438 */             this.dx = 0;
/*  439 */             n = localJTable.rowAtPoint(localRectangle.getLocation());
/*  440 */             if ((n == -1) && (this.forwards)) {
/*  441 */               n = localJTable.getRowCount();
/*      */             }
/*  443 */             this.dy = (n - this.leadRow);
/*      */           }
/*      */           else {
/*  446 */             localRectangle = localJTable.getCellRect(0, this.leadColumn, true);
/*      */ 
/*  448 */             if (this.forwards)
/*      */             {
/*  450 */               localRectangle.x += Math.max(localDimension.width, localRectangle.width);
/*      */             }
/*  452 */             else localRectangle.x -= localDimension.width;
/*      */ 
/*  455 */             n = localJTable.columnAtPoint(localRectangle.getLocation());
/*  456 */             if (n == -1) {
/*  457 */               boolean bool = localJTable.getComponentOrientation().isLeftToRight();
/*      */ 
/*  459 */               n = bool ? 0 : this.forwards ? 0 : bool ? localJTable.getColumnCount() : localJTable.getColumnCount();
/*      */             }
/*      */ 
/*  463 */             this.dx = (n - this.leadColumn);
/*  464 */             this.dy = 0;
/*      */           }
/*      */         }
/*      */       }
/*  468 */       if ((str == "selectNextRow") || (str == "selectNextRowCell") || (str == "selectNextRowExtendSelection") || (str == "selectNextRowChangeLead") || (str == "selectNextColumn") || (str == "selectNextColumnCell") || (str == "selectNextColumnExtendSelection") || (str == "selectNextColumnChangeLead") || (str == "selectPreviousRow") || (str == "selectPreviousRowCell") || (str == "selectPreviousRowExtendSelection") || (str == "selectPreviousRowChangeLead") || (str == "selectPreviousColumn") || (str == "selectPreviousColumnCell") || (str == "selectPreviousColumnExtendSelection") || (str == "selectPreviousColumnChangeLead") || (str == "scrollLeftChangeSelection") || (str == "scrollLeftExtendSelection") || (str == "scrollRightChangeSelection") || (str == "scrollRightExtendSelection") || (str == "scrollUpChangeSelection") || (str == "scrollUpExtendSelection") || (str == "scrollDownChangeSelection") || (str == "scrollDownExtendSelection") || (str == "selectFirstColumn") || (str == "selectFirstColumnExtendSelection") || (str == "selectFirstRow") || (str == "selectFirstRowExtendSelection") || (str == "selectLastColumn") || (str == "selectLastColumnExtendSelection") || (str == "selectLastRow") || (str == "selectLastRowExtendSelection"))
/*      */       {
/*  502 */         if ((localJTable.isEditing()) && (!localJTable.getCellEditor().stopCellEditing()))
/*      */         {
/*  504 */           return;
/*      */         }
/*      */ 
/*  522 */         int j = 0;
/*  523 */         if ((str == "selectNextRowChangeLead") || (str == "selectPreviousRowChangeLead")) {
/*  524 */           j = localListSelectionModel1.getSelectionMode() == 2 ? 1 : 0;
/*      */         }
/*  526 */         else if ((str == "selectNextColumnChangeLead") || (str == "selectPreviousColumnChangeLead")) {
/*  527 */           j = localListSelectionModel2.getSelectionMode() == 2 ? 1 : 0;
/*      */         }
/*      */ 
/*  531 */         if (j != 0) {
/*  532 */           moveWithinTableRange(localJTable, this.dx, this.dy);
/*  533 */           if (this.dy != 0)
/*      */           {
/*  536 */             ((DefaultListSelectionModel)localListSelectionModel1).moveLeadSelectionIndex(this.leadRow);
/*  537 */             if ((BasicTableUI.getAdjustedLead(localJTable, false, localListSelectionModel2) == -1) && (localJTable.getColumnCount() > 0))
/*      */             {
/*  540 */               ((DefaultListSelectionModel)localListSelectionModel2).moveLeadSelectionIndex(0);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  545 */             ((DefaultListSelectionModel)localListSelectionModel2).moveLeadSelectionIndex(this.leadColumn);
/*  546 */             if ((BasicTableUI.getAdjustedLead(localJTable, true, localListSelectionModel1) == -1) && (localJTable.getRowCount() > 0))
/*      */             {
/*  549 */               ((DefaultListSelectionModel)localListSelectionModel1).moveLeadSelectionIndex(0);
/*      */             }
/*      */           }
/*      */ 
/*  553 */           localRectangle = localJTable.getCellRect(this.leadRow, this.leadColumn, false);
/*  554 */           if (localRectangle != null)
/*  555 */             localJTable.scrollRectToVisible(localRectangle);
/*      */         }
/*  557 */         else if (!this.inSelection) {
/*  558 */           moveWithinTableRange(localJTable, this.dx, this.dy);
/*  559 */           localJTable.changeSelection(this.leadRow, this.leadColumn, false, this.extend);
/*      */         }
/*      */         else {
/*  562 */           if ((localJTable.getRowCount() <= 0) || (localJTable.getColumnCount() <= 0))
/*      */           {
/*  564 */             return;
/*      */           }
/*      */ 
/*  567 */           if (moveWithinSelectedRange(localJTable, this.dx, this.dy, localListSelectionModel1, localListSelectionModel2))
/*      */           {
/*  570 */             if (localListSelectionModel1.isSelectedIndex(this.leadRow))
/*  571 */               localListSelectionModel1.addSelectionInterval(this.leadRow, this.leadRow);
/*      */             else {
/*  573 */               localListSelectionModel1.removeSelectionInterval(this.leadRow, this.leadRow);
/*      */             }
/*      */ 
/*  576 */             if (localListSelectionModel2.isSelectedIndex(this.leadColumn))
/*  577 */               localListSelectionModel2.addSelectionInterval(this.leadColumn, this.leadColumn);
/*      */             else {
/*  579 */               localListSelectionModel2.removeSelectionInterval(this.leadColumn, this.leadColumn);
/*      */             }
/*      */ 
/*  582 */             localRectangle = localJTable.getCellRect(this.leadRow, this.leadColumn, false);
/*  583 */             if (localRectangle != null)
/*  584 */               localJTable.scrollRectToVisible(localRectangle);
/*      */           }
/*      */           else
/*      */           {
/*  588 */             localJTable.changeSelection(this.leadRow, this.leadColumn, false, false);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*  606 */       else if (str == "cancel") {
/*  607 */         localJTable.removeEditor();
/*  608 */       } else if (str == "selectAll") {
/*  609 */         localJTable.selectAll();
/*  610 */       } else if (str == "clearSelection") {
/*  611 */         localJTable.clearSelection();
/*  612 */       } else if (str == "startEditing") {
/*  613 */         if (!localJTable.hasFocus()) {
/*  614 */           localObject = localJTable.getCellEditor();
/*  615 */           if ((localObject != null) && (!((CellEditor)localObject).stopCellEditing())) {
/*  616 */             return;
/*      */           }
/*  618 */           localJTable.requestFocus();
/*  619 */           return;
/*      */         }
/*  621 */         localJTable.editCellAt(this.leadRow, this.leadColumn, paramActionEvent);
/*  622 */         Object localObject = localJTable.getEditorComponent();
/*  623 */         if (localObject != null)
/*  624 */           ((Component)localObject).requestFocus();
/*      */       }
/*      */       else
/*      */       {
/*      */         int m;
/*  626 */         if (str == "addToSelection") {
/*  627 */           if (!localJTable.isCellSelected(this.leadRow, this.leadColumn)) {
/*  628 */             int k = localListSelectionModel1.getAnchorSelectionIndex();
/*  629 */             m = localListSelectionModel2.getAnchorSelectionIndex();
/*  630 */             localListSelectionModel1.setValueIsAdjusting(true);
/*  631 */             localListSelectionModel2.setValueIsAdjusting(true);
/*  632 */             localJTable.changeSelection(this.leadRow, this.leadColumn, true, false);
/*  633 */             localListSelectionModel1.setAnchorSelectionIndex(k);
/*  634 */             localListSelectionModel2.setAnchorSelectionIndex(m);
/*  635 */             localListSelectionModel1.setValueIsAdjusting(false);
/*  636 */             localListSelectionModel2.setValueIsAdjusting(false);
/*      */           }
/*  638 */         } else if (str == "toggleAndAnchor") {
/*  639 */           localJTable.changeSelection(this.leadRow, this.leadColumn, true, false);
/*  640 */         } else if (str == "extendTo") {
/*  641 */           localJTable.changeSelection(this.leadRow, this.leadColumn, false, true);
/*  642 */         } else if (str == "moveSelectionTo") {
/*  643 */           localJTable.changeSelection(this.leadRow, this.leadColumn, false, false);
/*  644 */         } else if (str == "focusHeader") {
/*  645 */           JTableHeader localJTableHeader = localJTable.getTableHeader();
/*  646 */           if (localJTableHeader != null)
/*      */           {
/*  648 */             m = localJTable.getSelectedColumn();
/*  649 */             if (m >= 0) {
/*  650 */               TableHeaderUI localTableHeaderUI = localJTableHeader.getUI();
/*  651 */               if ((localTableHeaderUI instanceof BasicTableHeaderUI)) {
/*  652 */                 ((BasicTableHeaderUI)localTableHeaderUI).selectColumn(m);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  657 */             localJTableHeader.requestFocusInWindow();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  663 */     public boolean isEnabled(Object paramObject) { String str = getName();
/*      */ 
/*  665 */       if (((paramObject instanceof JTable)) && (Boolean.TRUE.equals(((JTable)paramObject).getClientProperty("Table.isFileList"))))
/*      */       {
/*  667 */         if ((str == "selectNextColumn") || (str == "selectNextColumnCell") || (str == "selectNextColumnExtendSelection") || (str == "selectNextColumnChangeLead") || (str == "selectPreviousColumn") || (str == "selectPreviousColumnCell") || (str == "selectPreviousColumnExtendSelection") || (str == "selectPreviousColumnChangeLead") || (str == "scrollLeftChangeSelection") || (str == "scrollLeftExtendSelection") || (str == "scrollRightChangeSelection") || (str == "scrollRightExtendSelection") || (str == "selectFirstColumn") || (str == "selectFirstColumnExtendSelection") || (str == "selectLastColumn") || (str == "selectLastColumnExtendSelection") || (str == "selectNextRowCell") || (str == "selectPreviousRowCell"))
/*      */         {
/*  686 */           return false;
/*      */         }
/*      */       }
/*      */ 
/*  690 */       if ((str == "cancel") && ((paramObject instanceof JTable)))
/*  691 */         return ((JTable)paramObject).isEditing();
/*  692 */       if ((str == "selectNextRowChangeLead") || (str == "selectPreviousRowChangeLead"))
/*      */       {
/*  696 */         return (paramObject != null) && ((((JTable)paramObject).getSelectionModel() instanceof DefaultListSelectionModel));
/*      */       }
/*      */ 
/*  699 */       if ((str == "selectNextColumnChangeLead") || (str == "selectPreviousColumnChangeLead"))
/*      */       {
/*  703 */         return (paramObject != null) && ((((JTable)paramObject).getColumnModel().getSelectionModel() instanceof DefaultListSelectionModel));
/*      */       }
/*      */       JTable localJTable;
/*  706 */       if ((str == "addToSelection") && ((paramObject instanceof JTable)))
/*      */       {
/*  714 */         localJTable = (JTable)paramObject;
/*  715 */         int i = BasicTableUI.getAdjustedLead(localJTable, true);
/*  716 */         int j = BasicTableUI.getAdjustedLead(localJTable, false);
/*  717 */         return (!localJTable.isEditing()) && (!localJTable.isCellSelected(i, j));
/*  718 */       }if ((str == "focusHeader") && ((paramObject instanceof JTable))) {
/*  719 */         localJTable = (JTable)paramObject;
/*  720 */         return localJTable.getTableHeader() != null;
/*      */       }
/*      */ 
/*  723 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class FocusHandler
/*      */     implements FocusListener
/*      */   {
/*      */     public FocusHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/*  772 */       BasicTableUI.this.getHandler().focusGained(paramFocusEvent);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/*  776 */       BasicTableUI.this.getHandler().focusLost(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements FocusListener, MouseInputListener, PropertyChangeListener, ListSelectionListener, ActionListener, DragRecognitionSupport.BeforeDrag
/*      */   {
/*      */     private Component dispatchComponent;
/*      */     private int pressedRow;
/*      */     private int pressedCol;
/*      */     private MouseEvent pressedEvent;
/*      */     private boolean dragPressDidSelection;
/*      */     private boolean dragStarted;
/*      */     private boolean shouldStartTimer;
/*      */     private boolean outsidePrefSize;
/*  982 */     private Timer timer = null;
/*      */ 
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     private void repaintLeadCell()
/*      */     {
/*  828 */       int i = BasicTableUI.getAdjustedLead(BasicTableUI.this.table, true);
/*  829 */       int j = BasicTableUI.getAdjustedLead(BasicTableUI.this.table, false);
/*      */ 
/*  831 */       if ((i < 0) || (j < 0)) {
/*  832 */         return;
/*      */       }
/*      */ 
/*  835 */       Rectangle localRectangle = BasicTableUI.this.table.getCellRect(i, j, false);
/*  836 */       BasicTableUI.this.table.repaint(localRectangle);
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent) {
/*  840 */       repaintLeadCell();
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/*  844 */       repaintLeadCell();
/*      */     }
/*      */ 
/*      */     public void keyPressed(KeyEvent paramKeyEvent) {
/*      */     }
/*      */ 
/*      */     public void keyReleased(KeyEvent paramKeyEvent) {
/*      */     }
/*      */ 
/*      */     public void keyTyped(KeyEvent paramKeyEvent) {
/*  854 */       KeyStroke localKeyStroke = KeyStroke.getKeyStroke(paramKeyEvent.getKeyChar(), paramKeyEvent.getModifiers());
/*      */ 
/*  862 */       InputMap localInputMap = BasicTableUI.this.table.getInputMap(0);
/*  863 */       if ((localInputMap != null) && (localInputMap.get(localKeyStroke) != null)) {
/*  864 */         return;
/*      */       }
/*  866 */       localInputMap = BasicTableUI.this.table.getInputMap(1);
/*      */ 
/*  868 */       if ((localInputMap != null) && (localInputMap.get(localKeyStroke) != null)) {
/*  869 */         return;
/*      */       }
/*      */ 
/*  872 */       localKeyStroke = KeyStroke.getKeyStrokeForEvent(paramKeyEvent);
/*      */ 
/*  876 */       if (paramKeyEvent.getKeyChar() == '\r') {
/*  877 */         return;
/*      */       }
/*      */ 
/*  880 */       int i = BasicTableUI.getAdjustedLead(BasicTableUI.this.table, true);
/*  881 */       int j = BasicTableUI.getAdjustedLead(BasicTableUI.this.table, false);
/*  882 */       if ((i != -1) && (j != -1) && (!BasicTableUI.this.table.isEditing()) && 
/*  883 */         (!BasicTableUI.this.table.editCellAt(i, j))) {
/*  884 */         return;
/*      */       }
/*      */ 
/*  896 */       Component localComponent = BasicTableUI.this.table.getEditorComponent();
/*  897 */       if ((BasicTableUI.this.table.isEditing()) && (localComponent != null) && 
/*  898 */         ((localComponent instanceof JComponent))) {
/*  899 */         JComponent localJComponent = (JComponent)localComponent;
/*  900 */         localInputMap = localJComponent.getInputMap(0);
/*  901 */         Object localObject = localInputMap != null ? localInputMap.get(localKeyStroke) : null;
/*  902 */         if (localObject == null) {
/*  903 */           localInputMap = localJComponent.getInputMap(1);
/*      */ 
/*  905 */           localObject = localInputMap != null ? localInputMap.get(localKeyStroke) : null;
/*      */         }
/*  907 */         if (localObject != null) {
/*  908 */           ActionMap localActionMap = localJComponent.getActionMap();
/*  909 */           Action localAction = localActionMap != null ? localActionMap.get(localObject) : null;
/*  910 */           if ((localAction != null) && (SwingUtilities.notifyAction(localAction, localKeyStroke, paramKeyEvent, localJComponent, paramKeyEvent.getModifiers())))
/*      */           {
/*  913 */             paramKeyEvent.consume();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     private void setDispatchComponent(MouseEvent paramMouseEvent)
/*      */     {
/*  930 */       Component localComponent = BasicTableUI.this.table.getEditorComponent();
/*  931 */       Point localPoint1 = paramMouseEvent.getPoint();
/*  932 */       Point localPoint2 = SwingUtilities.convertPoint(BasicTableUI.this.table, localPoint1, localComponent);
/*  933 */       this.dispatchComponent = SwingUtilities.getDeepestComponentAt(localComponent, localPoint2.x, localPoint2.y);
/*      */ 
/*  936 */       SwingUtilities2.setSkipClickCount(this.dispatchComponent, paramMouseEvent.getClickCount() - 1);
/*      */     }
/*      */ 
/*      */     private boolean repostEvent(MouseEvent paramMouseEvent)
/*      */     {
/*  943 */       if ((this.dispatchComponent == null) || (!BasicTableUI.this.table.isEditing())) {
/*  944 */         return false;
/*      */       }
/*  946 */       MouseEvent localMouseEvent = SwingUtilities.convertMouseEvent(BasicTableUI.this.table, paramMouseEvent, this.dispatchComponent);
/*      */ 
/*  948 */       this.dispatchComponent.dispatchEvent(localMouseEvent);
/*  949 */       return true;
/*      */     }
/*      */ 
/*      */     private void setValueIsAdjusting(boolean paramBoolean) {
/*  953 */       BasicTableUI.this.table.getSelectionModel().setValueIsAdjusting(paramBoolean);
/*  954 */       BasicTableUI.this.table.getColumnModel().getSelectionModel().setValueIsAdjusting(paramBoolean);
/*      */     }
/*      */ 
/*      */     private boolean canStartDrag()
/*      */     {
/*  985 */       if ((this.pressedRow == -1) || (this.pressedCol == -1)) {
/*  986 */         return false;
/*      */       }
/*      */ 
/*  989 */       if (BasicTableUI.this.isFileList) {
/*  990 */         return !this.outsidePrefSize;
/*      */       }
/*      */ 
/*  994 */       if ((BasicTableUI.this.table.getSelectionModel().getSelectionMode() == 0) && (BasicTableUI.this.table.getColumnModel().getSelectionModel().getSelectionMode() == 0))
/*      */       {
/*  999 */         return true;
/*      */       }
/*      */ 
/* 1002 */       return BasicTableUI.this.table.isCellSelected(this.pressedRow, this.pressedCol);
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 1006 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicTableUI.this.table)) {
/* 1007 */         return;
/*      */       }
/*      */ 
/* 1010 */       if ((BasicTableUI.this.table.isEditing()) && (!BasicTableUI.this.table.getCellEditor().stopCellEditing())) {
/* 1011 */         localObject = BasicTableUI.this.table.getEditorComponent();
/* 1012 */         if ((localObject != null) && (!((Component)localObject).hasFocus())) {
/* 1013 */           SwingUtilities2.compositeRequestFocus((Component)localObject);
/*      */         }
/* 1015 */         return;
/*      */       }
/*      */ 
/* 1018 */       Object localObject = paramMouseEvent.getPoint();
/* 1019 */       this.pressedRow = BasicTableUI.this.table.rowAtPoint((Point)localObject);
/* 1020 */       this.pressedCol = BasicTableUI.this.table.columnAtPoint((Point)localObject);
/* 1021 */       this.outsidePrefSize = BasicTableUI.this.pointOutsidePrefSize(this.pressedRow, this.pressedCol, (Point)localObject);
/*      */ 
/* 1023 */       if (BasicTableUI.this.isFileList) {
/* 1024 */         this.shouldStartTimer = ((BasicTableUI.this.table.isCellSelected(this.pressedRow, this.pressedCol)) && (!paramMouseEvent.isShiftDown()) && (!BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent)) && (!this.outsidePrefSize));
/*      */       }
/*      */ 
/* 1031 */       if (BasicTableUI.this.table.getDragEnabled()) {
/* 1032 */         mousePressedDND(paramMouseEvent);
/*      */       } else {
/* 1034 */         SwingUtilities2.adjustFocus(BasicTableUI.this.table);
/* 1035 */         if (!BasicTableUI.this.isFileList) {
/* 1036 */           setValueIsAdjusting(true);
/*      */         }
/* 1038 */         adjustSelection(paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void mousePressedDND(MouseEvent paramMouseEvent) {
/* 1043 */       this.pressedEvent = paramMouseEvent;
/* 1044 */       int i = 1;
/* 1045 */       this.dragStarted = false;
/*      */ 
/* 1047 */       if ((canStartDrag()) && (DragRecognitionSupport.mousePressed(paramMouseEvent)))
/*      */       {
/* 1049 */         this.dragPressDidSelection = false;
/*      */ 
/* 1051 */         if ((BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent)) && (BasicTableUI.this.isFileList))
/*      */         {
/* 1054 */           return;
/* 1055 */         }if ((!paramMouseEvent.isShiftDown()) && (BasicTableUI.this.table.isCellSelected(this.pressedRow, this.pressedCol)))
/*      */         {
/* 1058 */           BasicTableUI.this.table.getSelectionModel().addSelectionInterval(this.pressedRow, this.pressedRow);
/*      */ 
/* 1060 */           BasicTableUI.this.table.getColumnModel().getSelectionModel().addSelectionInterval(this.pressedCol, this.pressedCol);
/*      */ 
/* 1063 */           return;
/*      */         }
/*      */ 
/* 1066 */         this.dragPressDidSelection = true;
/*      */ 
/* 1069 */         i = 0;
/* 1070 */       } else if (!BasicTableUI.this.isFileList)
/*      */       {
/* 1073 */         setValueIsAdjusting(true);
/*      */       }
/*      */ 
/* 1076 */       if (i != 0) {
/* 1077 */         SwingUtilities2.adjustFocus(BasicTableUI.this.table);
/*      */       }
/*      */ 
/* 1080 */       adjustSelection(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     private void adjustSelection(MouseEvent paramMouseEvent)
/*      */     {
/* 1085 */       if (this.outsidePrefSize)
/*      */       {
/* 1088 */         if ((paramMouseEvent.getID() == 501) && ((!paramMouseEvent.isShiftDown()) || (BasicTableUI.this.table.getSelectionModel().getSelectionMode() == 0)))
/*      */         {
/* 1092 */           BasicTableUI.this.table.clearSelection();
/* 1093 */           TableCellEditor localTableCellEditor1 = BasicTableUI.this.table.getCellEditor();
/* 1094 */           if (localTableCellEditor1 != null) {
/* 1095 */             localTableCellEditor1.stopCellEditing();
/*      */           }
/*      */         }
/* 1098 */         return;
/*      */       }
/*      */ 
/* 1102 */       if ((this.pressedCol == -1) || (this.pressedRow == -1)) {
/* 1103 */         return;
/*      */       }
/*      */ 
/* 1106 */       boolean bool = BasicTableUI.this.table.getDragEnabled();
/*      */ 
/* 1108 */       if ((!bool) && (!BasicTableUI.this.isFileList) && (BasicTableUI.this.table.editCellAt(this.pressedRow, this.pressedCol, paramMouseEvent))) {
/* 1109 */         setDispatchComponent(paramMouseEvent);
/* 1110 */         repostEvent(paramMouseEvent);
/*      */       }
/*      */ 
/* 1113 */       TableCellEditor localTableCellEditor2 = BasicTableUI.this.table.getCellEditor();
/* 1114 */       if ((bool) || (localTableCellEditor2 == null) || (localTableCellEditor2.shouldSelectCell(paramMouseEvent)))
/* 1115 */         BasicTableUI.this.table.changeSelection(this.pressedRow, this.pressedCol, BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent), paramMouseEvent.isShiftDown());
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/* 1122 */       if (this.timer != null) {
/* 1123 */         this.timer.stop();
/* 1124 */         this.timer = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1129 */       BasicTableUI.this.table.editCellAt(this.pressedRow, this.pressedCol, null);
/* 1130 */       Component localComponent = BasicTableUI.this.table.getEditorComponent();
/* 1131 */       if ((localComponent != null) && (!localComponent.hasFocus()))
/* 1132 */         SwingUtilities2.compositeRequestFocus(localComponent);
/*      */     }
/*      */ 
/*      */     private void maybeStartTimer()
/*      */     {
/* 1138 */       if (!this.shouldStartTimer) {
/* 1139 */         return;
/*      */       }
/*      */ 
/* 1142 */       if (this.timer == null) {
/* 1143 */         this.timer = new Timer(1200, this);
/* 1144 */         this.timer.setRepeats(false);
/*      */       }
/*      */ 
/* 1147 */       this.timer.start();
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 1151 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicTableUI.this.table)) {
/* 1152 */         return;
/*      */       }
/*      */ 
/* 1155 */       if (BasicTableUI.this.table.getDragEnabled()) {
/* 1156 */         mouseReleasedDND(paramMouseEvent);
/*      */       }
/* 1158 */       else if (BasicTableUI.this.isFileList) {
/* 1159 */         maybeStartTimer();
/*      */       }
/*      */ 
/* 1163 */       this.pressedEvent = null;
/* 1164 */       repostEvent(paramMouseEvent);
/* 1165 */       this.dispatchComponent = null;
/* 1166 */       setValueIsAdjusting(false);
/*      */     }
/*      */ 
/*      */     private void mouseReleasedDND(MouseEvent paramMouseEvent) {
/* 1170 */       MouseEvent localMouseEvent = DragRecognitionSupport.mouseReleased(paramMouseEvent);
/* 1171 */       if (localMouseEvent != null) {
/* 1172 */         SwingUtilities2.adjustFocus(BasicTableUI.this.table);
/* 1173 */         if (!this.dragPressDidSelection) {
/* 1174 */           adjustSelection(localMouseEvent);
/*      */         }
/*      */       }
/*      */ 
/* 1178 */       if (!this.dragStarted) {
/* 1179 */         if (BasicTableUI.this.isFileList) {
/* 1180 */           maybeStartTimer();
/* 1181 */           return;
/*      */         }
/*      */ 
/* 1184 */         Point localPoint = paramMouseEvent.getPoint();
/*      */ 
/* 1186 */         if ((this.pressedEvent != null) && (BasicTableUI.this.table.rowAtPoint(localPoint) == this.pressedRow) && (BasicTableUI.this.table.columnAtPoint(localPoint) == this.pressedCol) && (BasicTableUI.this.table.editCellAt(this.pressedRow, this.pressedCol, this.pressedEvent)))
/*      */         {
/* 1191 */           setDispatchComponent(this.pressedEvent);
/* 1192 */           repostEvent(this.pressedEvent);
/*      */ 
/* 1197 */           TableCellEditor localTableCellEditor = BasicTableUI.this.table.getCellEditor();
/* 1198 */           if (localTableCellEditor != null)
/* 1199 */             localTableCellEditor.shouldSelectCell(this.pressedEvent); 
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {  }
/*      */ 
/*      */ 
/* 1212 */     public void dragStarting(MouseEvent paramMouseEvent) { this.dragStarted = true;
/*      */ 
/* 1214 */       if ((BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent)) && (BasicTableUI.this.isFileList)) {
/* 1215 */         BasicTableUI.this.table.getSelectionModel().addSelectionInterval(this.pressedRow, this.pressedRow);
/*      */ 
/* 1217 */         BasicTableUI.this.table.getColumnModel().getSelectionModel().addSelectionInterval(this.pressedCol, this.pressedCol);
/*      */       }
/*      */ 
/* 1221 */       this.pressedEvent = null; }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/* 1225 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicTableUI.this.table)) {
/* 1226 */         return;
/*      */       }
/*      */ 
/* 1229 */       if ((BasicTableUI.this.table.getDragEnabled()) && ((DragRecognitionSupport.mouseDragged(paramMouseEvent, this)) || (this.dragStarted)))
/*      */       {
/* 1232 */         return;
/*      */       }
/*      */ 
/* 1235 */       repostEvent(paramMouseEvent);
/*      */ 
/* 1240 */       if ((BasicTableUI.this.isFileList) || (BasicTableUI.this.table.isEditing())) {
/* 1241 */         return;
/*      */       }
/*      */ 
/* 1244 */       Point localPoint = paramMouseEvent.getPoint();
/* 1245 */       int i = BasicTableUI.this.table.rowAtPoint(localPoint);
/* 1246 */       int j = BasicTableUI.this.table.columnAtPoint(localPoint);
/*      */ 
/* 1249 */       if ((j == -1) || (i == -1)) {
/* 1250 */         return;
/*      */       }
/*      */ 
/* 1253 */       BasicTableUI.this.table.changeSelection(i, j, BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent), true);
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1260 */       String str = paramPropertyChangeEvent.getPropertyName();
/*      */       Object localObject;
/* 1262 */       if ("componentOrientation" == str) {
/* 1263 */         localObject = BasicTableUI.this.getInputMap(1);
/*      */ 
/* 1266 */         SwingUtilities.replaceUIInputMap(BasicTableUI.this.table, 1, (InputMap)localObject);
/*      */ 
/* 1270 */         JTableHeader localJTableHeader = BasicTableUI.this.table.getTableHeader();
/* 1271 */         if (localJTableHeader != null) {
/* 1272 */           localJTableHeader.setComponentOrientation((ComponentOrientation)paramPropertyChangeEvent.getNewValue());
/*      */         }
/*      */       }
/* 1275 */       else if ("dropLocation" == str) {
/* 1276 */         localObject = (JTable.DropLocation)paramPropertyChangeEvent.getOldValue();
/* 1277 */         repaintDropLocation((JTable.DropLocation)localObject);
/* 1278 */         repaintDropLocation(BasicTableUI.this.table.getDropLocation());
/* 1279 */       } else if ("Table.isFileList" == str) {
/* 1280 */         BasicTableUI.this.isFileList = Boolean.TRUE.equals(BasicTableUI.this.table.getClientProperty("Table.isFileList"));
/* 1281 */         BasicTableUI.this.table.revalidate();
/* 1282 */         BasicTableUI.this.table.repaint();
/* 1283 */         if (BasicTableUI.this.isFileList) {
/* 1284 */           BasicTableUI.this.table.getSelectionModel().addListSelectionListener(BasicTableUI.this.getHandler());
/*      */         } else {
/* 1286 */           BasicTableUI.this.table.getSelectionModel().removeListSelectionListener(BasicTableUI.this.getHandler());
/* 1287 */           this.timer = null;
/*      */         }
/* 1289 */       } else if (("selectionModel" == str) && 
/* 1290 */         (BasicTableUI.this.isFileList)) {
/* 1291 */         localObject = (ListSelectionModel)paramPropertyChangeEvent.getOldValue();
/* 1292 */         ((ListSelectionModel)localObject).removeListSelectionListener(BasicTableUI.this.getHandler());
/* 1293 */         BasicTableUI.this.table.getSelectionModel().addListSelectionListener(BasicTableUI.this.getHandler());
/*      */       }
/*      */     }
/*      */ 
/*      */     private void repaintDropLocation(JTable.DropLocation paramDropLocation)
/*      */     {
/* 1299 */       if (paramDropLocation == null)
/*      */         return;
/*      */       Rectangle localRectangle;
/* 1303 */       if ((!paramDropLocation.isInsertRow()) && (!paramDropLocation.isInsertColumn())) {
/* 1304 */         localRectangle = BasicTableUI.this.table.getCellRect(paramDropLocation.getRow(), paramDropLocation.getColumn(), false);
/* 1305 */         if (localRectangle != null) {
/* 1306 */           BasicTableUI.this.table.repaint(localRectangle);
/*      */         }
/* 1308 */         return;
/*      */       }
/*      */ 
/* 1311 */       if (paramDropLocation.isInsertRow()) {
/* 1312 */         localRectangle = BasicTableUI.this.extendRect(BasicTableUI.access$500(BasicTableUI.this, paramDropLocation), true);
/* 1313 */         if (localRectangle != null) {
/* 1314 */           BasicTableUI.this.table.repaint(localRectangle);
/*      */         }
/*      */       }
/*      */ 
/* 1318 */       if (paramDropLocation.isInsertColumn()) {
/* 1319 */         localRectangle = BasicTableUI.this.extendRect(BasicTableUI.access$700(BasicTableUI.this, paramDropLocation), false);
/* 1320 */         if (localRectangle != null)
/* 1321 */           BasicTableUI.this.table.repaint(localRectangle);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class KeyHandler
/*      */     implements KeyListener
/*      */   {
/*      */     public KeyHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void keyPressed(KeyEvent paramKeyEvent)
/*      */     {
/*  746 */       BasicTableUI.this.getHandler().keyPressed(paramKeyEvent);
/*      */     }
/*      */ 
/*      */     public void keyReleased(KeyEvent paramKeyEvent) {
/*  750 */       BasicTableUI.this.getHandler().keyReleased(paramKeyEvent);
/*      */     }
/*      */ 
/*      */     public void keyTyped(KeyEvent paramKeyEvent) {
/*  754 */       BasicTableUI.this.getHandler().keyTyped(paramKeyEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class MouseInputHandler
/*      */     implements MouseInputListener
/*      */   {
/*      */     public MouseInputHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*  794 */       BasicTableUI.this.getHandler().mouseClicked(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/*  798 */       BasicTableUI.this.getHandler().mousePressed(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  802 */       BasicTableUI.this.getHandler().mouseReleased(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*  806 */       BasicTableUI.this.getHandler().mouseEntered(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/*  810 */       BasicTableUI.this.getHandler().mouseExited(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/*  814 */       BasicTableUI.this.getHandler().mouseMoved(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/*  818 */       BasicTableUI.this.getHandler().mouseDragged(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class TableTransferHandler extends TransferHandler
/*      */     implements UIResource
/*      */   {
/*      */     protected Transferable createTransferable(JComponent paramJComponent)
/*      */     {
/* 2149 */       if ((paramJComponent instanceof JTable)) {
/* 2150 */         JTable localJTable = (JTable)paramJComponent;
/*      */ 
/* 2154 */         if ((!localJTable.getRowSelectionAllowed()) && (!localJTable.getColumnSelectionAllowed()))
/* 2155 */           return null;
/*      */         int i;
/*      */         int[] arrayOfInt1;
/*      */         int j;
/* 2158 */         if (!localJTable.getRowSelectionAllowed()) {
/* 2159 */           i = localJTable.getRowCount();
/*      */ 
/* 2161 */           arrayOfInt1 = new int[i];
/* 2162 */           for (j = 0; j < i; j++)
/* 2163 */             arrayOfInt1[j] = j;
/*      */         }
/*      */         else {
/* 2166 */           arrayOfInt1 = localJTable.getSelectedRows();
/*      */         }
/*      */         int[] arrayOfInt2;
/* 2169 */         if (!localJTable.getColumnSelectionAllowed()) {
/* 2170 */           i = localJTable.getColumnCount();
/*      */ 
/* 2172 */           arrayOfInt2 = new int[i];
/* 2173 */           for (j = 0; j < i; j++)
/* 2174 */             arrayOfInt2[j] = j;
/*      */         }
/*      */         else {
/* 2177 */           arrayOfInt2 = localJTable.getSelectedColumns();
/*      */         }
/*      */ 
/* 2180 */         if ((arrayOfInt1 == null) || (arrayOfInt2 == null) || (arrayOfInt1.length == 0) || (arrayOfInt2.length == 0)) {
/* 2181 */           return null;
/*      */         }
/*      */ 
/* 2184 */         StringBuffer localStringBuffer1 = new StringBuffer();
/* 2185 */         StringBuffer localStringBuffer2 = new StringBuffer();
/*      */ 
/* 2187 */         localStringBuffer2.append("<html>\n<body>\n<table>\n");
/*      */ 
/* 2189 */         for (int k = 0; k < arrayOfInt1.length; k++) {
/* 2190 */           localStringBuffer2.append("<tr>\n");
/* 2191 */           for (int m = 0; m < arrayOfInt2.length; m++) {
/* 2192 */             Object localObject = localJTable.getValueAt(arrayOfInt1[k], arrayOfInt2[m]);
/* 2193 */             String str = localObject == null ? "" : localObject.toString();
/* 2194 */             localStringBuffer1.append(str + "\t");
/* 2195 */             localStringBuffer2.append("  <td>" + str + "</td>\n");
/*      */           }
/*      */ 
/* 2198 */           localStringBuffer1.deleteCharAt(localStringBuffer1.length() - 1).append("\n");
/* 2199 */           localStringBuffer2.append("</tr>\n");
/*      */         }
/*      */ 
/* 2203 */         localStringBuffer1.deleteCharAt(localStringBuffer1.length() - 1);
/* 2204 */         localStringBuffer2.append("</table>\n</body>\n</html>");
/*      */ 
/* 2206 */         return new BasicTransferable(localStringBuffer1.toString(), localStringBuffer2.toString());
/*      */       }
/*      */ 
/* 2209 */       return null;
/*      */     }
/*      */ 
/*      */     public int getSourceActions(JComponent paramJComponent) {
/* 2213 */       return 1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTableUI
 * JD-Core Version:    0.6.2
 */