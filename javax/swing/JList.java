/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.Transient;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleAction;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleIcon;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.event.ListDataEvent;
/*      */ import javax.swing.event.ListDataListener;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.ListUI;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.SwingUtilities2.Section;
/*      */ 
/*      */ public class JList<E> extends JComponent
/*      */   implements Scrollable, Accessible
/*      */ {
/*      */   private static final String uiClassID = "ListUI";
/*      */   public static final int VERTICAL = 0;
/*      */   public static final int VERTICAL_WRAP = 1;
/*      */   public static final int HORIZONTAL_WRAP = 2;
/*  314 */   private int fixedCellWidth = -1;
/*  315 */   private int fixedCellHeight = -1;
/*  316 */   private int horizontalScrollIncrement = -1;
/*      */   private E prototypeCellValue;
/*  318 */   private int visibleRowCount = 8;
/*      */   private Color selectionForeground;
/*      */   private Color selectionBackground;
/*      */   private boolean dragEnabled;
/*      */   private ListSelectionModel selectionModel;
/*      */   private ListModel<E> dataModel;
/*      */   private ListCellRenderer<? super E> cellRenderer;
/*      */   private ListSelectionListener selectionListener;
/*      */   private int layoutOrientation;
/*  336 */   private DropMode dropMode = DropMode.USE_SELECTION;
/*      */   private transient DropLocation dropLocation;
/*      */ 
/*      */   public JList(ListModel<E> paramListModel)
/*      */   {
/*  420 */     if (paramListModel == null) {
/*  421 */       throw new IllegalArgumentException("dataModel must be non null");
/*      */     }
/*      */ 
/*  426 */     ToolTipManager localToolTipManager = ToolTipManager.sharedInstance();
/*  427 */     localToolTipManager.registerComponent(this);
/*      */ 
/*  429 */     this.layoutOrientation = 0;
/*      */ 
/*  431 */     this.dataModel = paramListModel;
/*  432 */     this.selectionModel = createSelectionModel();
/*  433 */     setAutoscrolls(true);
/*  434 */     setOpaque(true);
/*  435 */     updateUI();
/*      */   }
/*      */ 
/*      */   public JList(E[] paramArrayOfE)
/*      */   {
/*  455 */     this(new AbstractListModel() {
/*      */       public int getSize() {
/*  457 */         return JList.this.length; } 
/*  458 */       public E getElementAt(int paramAnonymousInt) { return JList.this[paramAnonymousInt]; }
/*      */ 
/*      */     });
/*      */   }
/*      */ 
/*      */   public JList(Vector<? extends E> paramVector)
/*      */   {
/*  479 */     this(new AbstractListModel() {
/*      */       public int getSize() {
/*  481 */         return JList.this.size(); } 
/*  482 */       public E getElementAt(int paramAnonymousInt) { return JList.this.elementAt(paramAnonymousInt); }
/*      */ 
/*      */     });
/*      */   }
/*      */ 
/*      */   public JList()
/*      */   {
/*  492 */     this(new AbstractListModel() {
/*      */       public int getSize() {
/*  494 */         return 0; } 
/*  495 */       public E getElementAt(int paramAnonymousInt) { throw new IndexOutOfBoundsException("No Data Model"); }
/*      */ 
/*      */     });
/*      */   }
/*      */ 
/*      */   public ListUI getUI()
/*      */   {
/*  508 */     return (ListUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(ListUI paramListUI)
/*      */   {
/*  525 */     super.setUI(paramListUI);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  540 */     setUI((ListUI)UIManager.getUI(this));
/*      */ 
/*  542 */     ListCellRenderer localListCellRenderer = getCellRenderer();
/*  543 */     if ((localListCellRenderer instanceof Component))
/*  544 */       SwingUtilities.updateComponentTreeUI((Component)localListCellRenderer);
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  559 */     return "ListUI";
/*      */   }
/*      */ 
/*      */   private void updateFixedCellSize()
/*      */   {
/*  576 */     ListCellRenderer localListCellRenderer = getCellRenderer();
/*  577 */     Object localObject = getPrototypeCellValue();
/*      */ 
/*  579 */     if ((localListCellRenderer != null) && (localObject != null)) {
/*  580 */       Component localComponent = localListCellRenderer.getListCellRendererComponent(this, localObject, 0, false, false);
/*      */ 
/*  588 */       Font localFont = localComponent.getFont();
/*  589 */       localComponent.setFont(getFont());
/*      */ 
/*  591 */       Dimension localDimension = localComponent.getPreferredSize();
/*  592 */       this.fixedCellWidth = localDimension.width;
/*  593 */       this.fixedCellHeight = localDimension.height;
/*      */ 
/*  595 */       localComponent.setFont(localFont);
/*      */     }
/*      */   }
/*      */ 
/*      */   public E getPrototypeCellValue()
/*      */   {
/*  609 */     return this.prototypeCellValue;
/*      */   }
/*      */ 
/*      */   public void setPrototypeCellValue(E paramE)
/*      */   {
/*  649 */     Object localObject = this.prototypeCellValue;
/*  650 */     this.prototypeCellValue = paramE;
/*      */ 
/*  656 */     if ((paramE != null) && (!paramE.equals(localObject))) {
/*  657 */       updateFixedCellSize();
/*      */     }
/*      */ 
/*  660 */     firePropertyChange("prototypeCellValue", localObject, paramE);
/*      */   }
/*      */ 
/*      */   public int getFixedCellWidth()
/*      */   {
/*  671 */     return this.fixedCellWidth;
/*      */   }
/*      */ 
/*      */   public void setFixedCellWidth(int paramInt)
/*      */   {
/*  694 */     int i = this.fixedCellWidth;
/*  695 */     this.fixedCellWidth = paramInt;
/*  696 */     firePropertyChange("fixedCellWidth", i, this.fixedCellWidth);
/*      */   }
/*      */ 
/*      */   public int getFixedCellHeight()
/*      */   {
/*  707 */     return this.fixedCellHeight;
/*      */   }
/*      */ 
/*      */   public void setFixedCellHeight(int paramInt)
/*      */   {
/*  730 */     int i = this.fixedCellHeight;
/*  731 */     this.fixedCellHeight = paramInt;
/*  732 */     firePropertyChange("fixedCellHeight", i, this.fixedCellHeight);
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public ListCellRenderer<? super E> getCellRenderer()
/*      */   {
/*  744 */     return this.cellRenderer;
/*      */   }
/*      */ 
/*      */   public void setCellRenderer(ListCellRenderer<? super E> paramListCellRenderer)
/*      */   {
/*  772 */     ListCellRenderer localListCellRenderer = this.cellRenderer;
/*  773 */     this.cellRenderer = paramListCellRenderer;
/*      */ 
/*  778 */     if ((paramListCellRenderer != null) && (!paramListCellRenderer.equals(localListCellRenderer))) {
/*  779 */       updateFixedCellSize();
/*      */     }
/*      */ 
/*  782 */     firePropertyChange("cellRenderer", localListCellRenderer, paramListCellRenderer);
/*      */   }
/*      */ 
/*      */   public Color getSelectionForeground()
/*      */   {
/*  797 */     return this.selectionForeground;
/*      */   }
/*      */ 
/*      */   public void setSelectionForeground(Color paramColor)
/*      */   {
/*  827 */     Color localColor = this.selectionForeground;
/*  828 */     this.selectionForeground = paramColor;
/*  829 */     firePropertyChange("selectionForeground", localColor, paramColor);
/*      */   }
/*      */ 
/*      */   public Color getSelectionBackground()
/*      */   {
/*  844 */     return this.selectionBackground;
/*      */   }
/*      */ 
/*      */   public void setSelectionBackground(Color paramColor)
/*      */   {
/*  874 */     Color localColor = this.selectionBackground;
/*  875 */     this.selectionBackground = paramColor;
/*  876 */     firePropertyChange("selectionBackground", localColor, paramColor);
/*      */   }
/*      */ 
/*      */   public int getVisibleRowCount()
/*      */   {
/*  889 */     return this.visibleRowCount;
/*      */   }
/*      */ 
/*      */   public void setVisibleRowCount(int paramInt)
/*      */   {
/*  930 */     int i = this.visibleRowCount;
/*  931 */     this.visibleRowCount = Math.max(0, paramInt);
/*  932 */     firePropertyChange("visibleRowCount", i, paramInt);
/*      */   }
/*      */ 
/*      */   public int getLayoutOrientation()
/*      */   {
/*  948 */     return this.layoutOrientation;
/*      */   }
/*      */ 
/*      */   public void setLayoutOrientation(int paramInt)
/*      */   {
/* 1011 */     int i = this.layoutOrientation;
/* 1012 */     switch (paramInt) {
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/* 1016 */       this.layoutOrientation = paramInt;
/* 1017 */       firePropertyChange("layoutOrientation", i, paramInt);
/* 1018 */       break;
/*      */     default:
/* 1020 */       throw new IllegalArgumentException("layoutOrientation must be one of: VERTICAL, HORIZONTAL_WRAP or VERTICAL_WRAP");
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getFirstVisibleIndex()
/*      */   {
/* 1038 */     Rectangle localRectangle1 = getVisibleRect();
/*      */     int i;
/* 1040 */     if (getComponentOrientation().isLeftToRight())
/* 1041 */       i = locationToIndex(localRectangle1.getLocation());
/*      */     else {
/* 1043 */       i = locationToIndex(new Point(localRectangle1.x + localRectangle1.width - 1, localRectangle1.y));
/*      */     }
/* 1045 */     if (i != -1) {
/* 1046 */       Rectangle localRectangle2 = getCellBounds(i, i);
/* 1047 */       if (localRectangle2 != null) {
/* 1048 */         SwingUtilities.computeIntersection(localRectangle1.x, localRectangle1.y, localRectangle1.width, localRectangle1.height, localRectangle2);
/* 1049 */         if ((localRectangle2.width == 0) || (localRectangle2.height == 0)) {
/* 1050 */           i = -1;
/*      */         }
/*      */       }
/*      */     }
/* 1054 */     return i;
/*      */   }
/*      */ 
/*      */   public int getLastVisibleIndex()
/*      */   {
/* 1068 */     boolean bool = getComponentOrientation().isLeftToRight();
/* 1069 */     Rectangle localRectangle1 = getVisibleRect();
/*      */     Point localPoint1;
/* 1071 */     if (bool)
/* 1072 */       localPoint1 = new Point(localRectangle1.x + localRectangle1.width - 1, localRectangle1.y + localRectangle1.height - 1);
/*      */     else {
/* 1074 */       localPoint1 = new Point(localRectangle1.x, localRectangle1.y + localRectangle1.height - 1);
/*      */     }
/* 1076 */     int i = locationToIndex(localPoint1);
/*      */ 
/* 1078 */     if (i != -1) {
/* 1079 */       Rectangle localRectangle2 = getCellBounds(i, i);
/*      */ 
/* 1081 */       if (localRectangle2 != null) {
/* 1082 */         SwingUtilities.computeIntersection(localRectangle1.x, localRectangle1.y, localRectangle1.width, localRectangle1.height, localRectangle2);
/* 1083 */         if ((localRectangle2.width == 0) || (localRectangle2.height == 0))
/*      */         {
/* 1088 */           int j = getLayoutOrientation() == 2 ? 1 : 0;
/*      */ 
/* 1090 */           Point localPoint2 = j != 0 ? new Point(localPoint1.x, localRectangle1.y) : new Point(localRectangle1.x, localPoint1.y);
/*      */ 
/* 1094 */           int m = -1;
/* 1095 */           int n = i;
/* 1096 */           i = -1;
/*      */           int k;
/*      */           do {
/* 1099 */             k = m;
/* 1100 */             m = locationToIndex(localPoint2);
/*      */ 
/* 1102 */             if (m != -1) {
/* 1103 */               localRectangle2 = getCellBounds(m, m);
/* 1104 */               if ((m != n) && (localRectangle2 != null) && (localRectangle2.contains(localPoint2)))
/*      */               {
/* 1106 */                 i = m;
/* 1107 */                 if (j != 0) {
/* 1108 */                   localPoint2.y = (localRectangle2.y + localRectangle2.height);
/* 1109 */                   if (localPoint2.y >= localPoint1.y)
/*      */                   {
/* 1111 */                     k = m;
/*      */                   }
/*      */                 }
/*      */                 else {
/* 1115 */                   localPoint2.x = (localRectangle2.x + localRectangle2.width);
/* 1116 */                   if (localPoint2.x >= localPoint1.x)
/*      */                   {
/* 1118 */                     k = m;
/*      */                   }
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/* 1124 */                 k = m;
/*      */               }
/*      */             }
/*      */           }
/* 1127 */           while ((m != -1) && (k != m));
/*      */         }
/*      */       }
/*      */     }
/* 1131 */     return i;
/*      */   }
/*      */ 
/*      */   public void ensureIndexIsVisible(int paramInt)
/*      */   {
/* 1149 */     Rectangle localRectangle = getCellBounds(paramInt, paramInt);
/* 1150 */     if (localRectangle != null)
/* 1151 */       scrollRectToVisible(localRectangle);
/*      */   }
/*      */ 
/*      */   public void setDragEnabled(boolean paramBoolean)
/*      */   {
/* 1189 */     if ((paramBoolean) && (GraphicsEnvironment.isHeadless())) {
/* 1190 */       throw new HeadlessException();
/*      */     }
/* 1192 */     this.dragEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getDragEnabled()
/*      */   {
/* 1203 */     return this.dragEnabled;
/*      */   }
/*      */ 
/*      */   public final void setDropMode(DropMode paramDropMode)
/*      */   {
/* 1234 */     if (paramDropMode != null) {
/* 1235 */       switch (6.$SwitchMap$javax$swing$DropMode[paramDropMode.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/* 1240 */         this.dropMode = paramDropMode;
/* 1241 */         return;
/*      */       }
/*      */     }
/*      */ 
/* 1245 */     throw new IllegalArgumentException(paramDropMode + ": Unsupported drop mode for list");
/*      */   }
/*      */ 
/*      */   public final DropMode getDropMode()
/*      */   {
/* 1256 */     return this.dropMode;
/*      */   }
/*      */ 
/*      */   DropLocation dropLocationForPoint(Point paramPoint)
/*      */   {
/* 1267 */     DropLocation localDropLocation = null;
/* 1268 */     Rectangle localRectangle = null;
/*      */ 
/* 1270 */     int i = locationToIndex(paramPoint);
/* 1271 */     if (i != -1)
/* 1272 */       localRectangle = getCellBounds(i, i);
/*      */     boolean bool1;
/* 1275 */     switch (6.$SwitchMap$javax$swing$DropMode[this.dropMode.ordinal()]) {
/*      */     case 1:
/*      */     case 2:
/* 1278 */       localDropLocation = new DropLocation(paramPoint, (localRectangle != null) && (localRectangle.contains(paramPoint)) ? i : -1, false, null);
/*      */ 
/* 1282 */       break;
/*      */     case 3:
/* 1284 */       if (i == -1) {
/* 1285 */         localDropLocation = new DropLocation(paramPoint, getModel().getSize(), true, null);
/*      */       }
/*      */       else
/*      */       {
/* 1289 */         if (this.layoutOrientation == 2) {
/* 1290 */           bool1 = getComponentOrientation().isLeftToRight();
/*      */ 
/* 1292 */           if (SwingUtilities2.liesInHorizontal(localRectangle, paramPoint, bool1, false) == SwingUtilities2.Section.TRAILING) {
/* 1293 */             i++;
/*      */           }
/* 1295 */           else if ((i == getModel().getSize() - 1) && (paramPoint.y >= localRectangle.y + localRectangle.height)) {
/* 1296 */             i++;
/*      */           }
/*      */         }
/* 1299 */         else if (SwingUtilities2.liesInVertical(localRectangle, paramPoint, false) == SwingUtilities2.Section.TRAILING) {
/* 1300 */           i++;
/*      */         }
/*      */ 
/* 1304 */         localDropLocation = new DropLocation(paramPoint, i, true, null);
/*      */       }
/* 1306 */       break;
/*      */     case 4:
/* 1308 */       if (i == -1) {
/* 1309 */         localDropLocation = new DropLocation(paramPoint, getModel().getSize(), true, null);
/*      */       }
/*      */       else
/*      */       {
/* 1313 */         bool1 = false;
/*      */ 
/* 1315 */         if (this.layoutOrientation == 2) {
/* 1316 */           boolean bool2 = getComponentOrientation().isLeftToRight();
/*      */ 
/* 1318 */           SwingUtilities2.Section localSection2 = SwingUtilities2.liesInHorizontal(localRectangle, paramPoint, bool2, true);
/* 1319 */           if (localSection2 == SwingUtilities2.Section.TRAILING) {
/* 1320 */             i++;
/* 1321 */             bool1 = true;
/*      */           }
/* 1323 */           else if ((i == getModel().getSize() - 1) && (paramPoint.y >= localRectangle.y + localRectangle.height)) {
/* 1324 */             i++;
/* 1325 */             bool1 = true;
/* 1326 */           } else if (localSection2 == SwingUtilities2.Section.LEADING) {
/* 1327 */             bool1 = true;
/*      */           }
/*      */         } else {
/* 1330 */           SwingUtilities2.Section localSection1 = SwingUtilities2.liesInVertical(localRectangle, paramPoint, true);
/* 1331 */           if (localSection1 == SwingUtilities2.Section.LEADING) {
/* 1332 */             bool1 = true;
/* 1333 */           } else if (localSection1 == SwingUtilities2.Section.TRAILING) {
/* 1334 */             i++;
/* 1335 */             bool1 = true;
/*      */           }
/*      */         }
/*      */ 
/* 1339 */         localDropLocation = new DropLocation(paramPoint, i, bool1, null);
/*      */       }
/* 1341 */       break;
/*      */     default:
/* 1343 */       if (!$assertionsDisabled) throw new AssertionError("Unexpected drop mode");
/*      */       break;
/*      */     }
/* 1346 */     return localDropLocation;
/*      */   }
/*      */ 
/*      */   Object setDropLocation(TransferHandler.DropLocation paramDropLocation, Object paramObject, boolean paramBoolean)
/*      */   {
/* 1386 */     Object localObject = null;
/* 1387 */     DropLocation localDropLocation1 = (DropLocation)paramDropLocation;
/*      */ 
/* 1389 */     if (this.dropMode == DropMode.USE_SELECTION) {
/* 1390 */       if (localDropLocation1 == null) {
/* 1391 */         if ((!paramBoolean) && (paramObject != null)) {
/* 1392 */           setSelectedIndices(((int[][])(int[][])paramObject)[0]);
/*      */ 
/* 1394 */           int i = ((int[][])(int[][])paramObject)[1][0];
/* 1395 */           int k = ((int[][])(int[][])paramObject)[1][1];
/*      */ 
/* 1397 */           SwingUtilities2.setLeadAnchorWithoutSelection(getSelectionModel(), k, i);
/*      */         }
/*      */       }
/*      */       else {
/* 1401 */         if (this.dropLocation == null) {
/* 1402 */           int[] arrayOfInt = getSelectedIndices();
/* 1403 */           localObject = new int[][] { arrayOfInt, { getAnchorSelectionIndex(), getLeadSelectionIndex() } };
/*      */         }
/*      */         else {
/* 1406 */           localObject = paramObject;
/*      */         }
/*      */ 
/* 1409 */         int j = localDropLocation1.getIndex();
/* 1410 */         if (j == -1) {
/* 1411 */           clearSelection();
/* 1412 */           getSelectionModel().setAnchorSelectionIndex(-1);
/* 1413 */           getSelectionModel().setLeadSelectionIndex(-1);
/*      */         } else {
/* 1415 */           setSelectionInterval(j, j);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1420 */     DropLocation localDropLocation2 = this.dropLocation;
/* 1421 */     this.dropLocation = localDropLocation1;
/* 1422 */     firePropertyChange("dropLocation", localDropLocation2, this.dropLocation);
/*      */ 
/* 1424 */     return localObject;
/*      */   }
/*      */ 
/*      */   public final DropLocation getDropLocation()
/*      */   {
/* 1453 */     return this.dropLocation;
/*      */   }
/*      */ 
/*      */   public int getNextMatch(String paramString, int paramInt, Position.Bias paramBias)
/*      */   {
/* 1471 */     ListModel localListModel = getModel();
/* 1472 */     int i = localListModel.getSize();
/* 1473 */     if (paramString == null) {
/* 1474 */       throw new IllegalArgumentException();
/*      */     }
/* 1476 */     if ((paramInt < 0) || (paramInt >= i)) {
/* 1477 */       throw new IllegalArgumentException();
/*      */     }
/* 1479 */     paramString = paramString.toUpperCase();
/*      */ 
/* 1482 */     int j = paramBias == Position.Bias.Forward ? 1 : -1;
/* 1483 */     int k = paramInt;
/*      */     do {
/* 1485 */       Object localObject = localListModel.getElementAt(k);
/*      */ 
/* 1487 */       if (localObject != null)
/*      */       {
/*      */         String str;
/* 1490 */         if ((localObject instanceof String)) {
/* 1491 */           str = ((String)localObject).toUpperCase();
/*      */         }
/*      */         else {
/* 1494 */           str = localObject.toString();
/* 1495 */           if (str != null) {
/* 1496 */             str = str.toUpperCase();
/*      */           }
/*      */         }
/*      */ 
/* 1500 */         if ((str != null) && (str.startsWith(paramString))) {
/* 1501 */           return k;
/*      */         }
/*      */       }
/* 1504 */       k = (k + j + i) % i;
/* 1505 */     }while (k != paramInt);
/* 1506 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getToolTipText(MouseEvent paramMouseEvent)
/*      */   {
/* 1529 */     if (paramMouseEvent != null) {
/* 1530 */       Point localPoint = paramMouseEvent.getPoint();
/* 1531 */       int i = locationToIndex(localPoint);
/* 1532 */       ListCellRenderer localListCellRenderer = getCellRenderer();
/*      */       Rectangle localRectangle;
/* 1535 */       if ((i != -1) && (localListCellRenderer != null) && ((localRectangle = getCellBounds(i, i)) != null) && (localRectangle.contains(localPoint.x, localPoint.y)))
/*      */       {
/* 1538 */         ListSelectionModel localListSelectionModel = getSelectionModel();
/* 1539 */         Component localComponent = localListCellRenderer.getListCellRendererComponent(this, getModel().getElementAt(i), i, localListSelectionModel.isSelectedIndex(i), (hasFocus()) && (localListSelectionModel.getLeadSelectionIndex() == i));
/*      */ 
/* 1545 */         if ((localComponent instanceof JComponent))
/*      */         {
/* 1548 */           localPoint.translate(-localRectangle.x, -localRectangle.y);
/* 1549 */           MouseEvent localMouseEvent = new MouseEvent(localComponent, paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*      */ 
/* 1559 */           String str = ((JComponent)localComponent).getToolTipText(localMouseEvent);
/*      */ 
/* 1562 */           if (str != null) {
/* 1563 */             return str;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1568 */     return super.getToolTipText();
/*      */   }
/*      */ 
/*      */   public int locationToIndex(Point paramPoint)
/*      */   {
/* 1591 */     ListUI localListUI = getUI();
/* 1592 */     return localListUI != null ? localListUI.locationToIndex(this, paramPoint) : -1;
/*      */   }
/*      */ 
/*      */   public Point indexToLocation(int paramInt)
/*      */   {
/* 1608 */     ListUI localListUI = getUI();
/* 1609 */     return localListUI != null ? localListUI.indexToLocation(this, paramInt) : null;
/*      */   }
/*      */ 
/*      */   public Rectangle getCellBounds(int paramInt1, int paramInt2)
/*      */   {
/* 1632 */     ListUI localListUI = getUI();
/* 1633 */     return localListUI != null ? localListUI.getCellBounds(this, paramInt1, paramInt2) : null;
/*      */   }
/*      */ 
/*      */   public ListModel<E> getModel()
/*      */   {
/* 1651 */     return this.dataModel;
/*      */   }
/*      */ 
/*      */   public void setModel(ListModel<E> paramListModel)
/*      */   {
/* 1673 */     if (paramListModel == null) {
/* 1674 */       throw new IllegalArgumentException("model must be non null");
/*      */     }
/* 1676 */     ListModel localListModel = this.dataModel;
/* 1677 */     this.dataModel = paramListModel;
/* 1678 */     firePropertyChange("model", localListModel, this.dataModel);
/* 1679 */     clearSelection();
/*      */   }
/*      */ 
/*      */   public void setListData(final E[] paramArrayOfE)
/*      */   {
/* 1697 */     setModel(new AbstractListModel() {
/*      */       public int getSize() {
/* 1699 */         return paramArrayOfE.length; } 
/* 1700 */       public E getElementAt(int paramAnonymousInt) { return paramArrayOfE[paramAnonymousInt]; }
/*      */ 
/*      */     });
/*      */   }
/*      */ 
/*      */   public void setListData(final Vector<? extends E> paramVector)
/*      */   {
/* 1720 */     setModel(new AbstractListModel() {
/*      */       public int getSize() {
/* 1722 */         return paramVector.size(); } 
/* 1723 */       public E getElementAt(int paramAnonymousInt) { return paramVector.elementAt(paramAnonymousInt); }
/*      */ 
/*      */     });
/*      */   }
/*      */ 
/*      */   protected ListSelectionModel createSelectionModel()
/*      */   {
/* 1745 */     return new DefaultListSelectionModel();
/*      */   }
/*      */ 
/*      */   public ListSelectionModel getSelectionModel()
/*      */   {
/* 1761 */     return this.selectionModel;
/*      */   }
/*      */ 
/*      */   protected void fireSelectionValueChanged(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 1789 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1790 */     ListSelectionEvent localListSelectionEvent = null;
/*      */ 
/* 1792 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1793 */       if (arrayOfObject[i] == ListSelectionListener.class) {
/* 1794 */         if (localListSelectionEvent == null) {
/* 1795 */           localListSelectionEvent = new ListSelectionEvent(this, paramInt1, paramInt2, paramBoolean);
/*      */         }
/*      */ 
/* 1798 */         ((ListSelectionListener)arrayOfObject[(i + 1)]).valueChanged(localListSelectionEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void addListSelectionListener(ListSelectionListener paramListSelectionListener)
/*      */   {
/* 1833 */     if (this.selectionListener == null) {
/* 1834 */       this.selectionListener = new ListSelectionHandler(null);
/* 1835 */       getSelectionModel().addListSelectionListener(this.selectionListener);
/*      */     }
/*      */ 
/* 1838 */     this.listenerList.add(ListSelectionListener.class, paramListSelectionListener);
/*      */   }
/*      */ 
/*      */   public void removeListSelectionListener(ListSelectionListener paramListSelectionListener)
/*      */   {
/* 1850 */     this.listenerList.remove(ListSelectionListener.class, paramListSelectionListener);
/*      */   }
/*      */ 
/*      */   public ListSelectionListener[] getListSelectionListeners()
/*      */   {
/* 1864 */     return (ListSelectionListener[])this.listenerList.getListeners(ListSelectionListener.class);
/*      */   }
/*      */ 
/*      */   public void setSelectionModel(ListSelectionModel paramListSelectionModel)
/*      */   {
/* 1887 */     if (paramListSelectionModel == null) {
/* 1888 */       throw new IllegalArgumentException("selectionModel must be non null");
/*      */     }
/*      */ 
/* 1894 */     if (this.selectionListener != null) {
/* 1895 */       this.selectionModel.removeListSelectionListener(this.selectionListener);
/* 1896 */       paramListSelectionModel.addListSelectionListener(this.selectionListener);
/*      */     }
/*      */ 
/* 1899 */     ListSelectionModel localListSelectionModel = this.selectionModel;
/* 1900 */     this.selectionModel = paramListSelectionModel;
/* 1901 */     firePropertyChange("selectionModel", localListSelectionModel, paramListSelectionModel);
/*      */   }
/*      */ 
/*      */   public void setSelectionMode(int paramInt)
/*      */   {
/* 1938 */     getSelectionModel().setSelectionMode(paramInt);
/*      */   }
/*      */ 
/*      */   public int getSelectionMode()
/*      */   {
/* 1950 */     return getSelectionModel().getSelectionMode();
/*      */   }
/*      */ 
/*      */   public int getAnchorSelectionIndex()
/*      */   {
/* 1962 */     return getSelectionModel().getAnchorSelectionIndex();
/*      */   }
/*      */ 
/*      */   public int getLeadSelectionIndex()
/*      */   {
/* 1976 */     return getSelectionModel().getLeadSelectionIndex();
/*      */   }
/*      */ 
/*      */   public int getMinSelectionIndex()
/*      */   {
/* 1989 */     return getSelectionModel().getMinSelectionIndex();
/*      */   }
/*      */ 
/*      */   public int getMaxSelectionIndex()
/*      */   {
/* 2002 */     return getSelectionModel().getMaxSelectionIndex();
/*      */   }
/*      */ 
/*      */   public boolean isSelectedIndex(int paramInt)
/*      */   {
/* 2018 */     return getSelectionModel().isSelectedIndex(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean isSelectionEmpty()
/*      */   {
/* 2032 */     return getSelectionModel().isSelectionEmpty();
/*      */   }
/*      */ 
/*      */   public void clearSelection()
/*      */   {
/* 2045 */     getSelectionModel().clearSelection();
/*      */   }
/*      */ 
/*      */   public void setSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2067 */     getSelectionModel().setSelectionInterval(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void addSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2090 */     getSelectionModel().addSelectionInterval(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void removeSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2113 */     getSelectionModel().removeSelectionInterval(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void setValueIsAdjusting(boolean paramBoolean)
/*      */   {
/* 2142 */     getSelectionModel().setValueIsAdjusting(paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getValueIsAdjusting()
/*      */   {
/* 2158 */     return getSelectionModel().getValueIsAdjusting();
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public int[] getSelectedIndices()
/*      */   {
/* 2173 */     ListSelectionModel localListSelectionModel = getSelectionModel();
/* 2174 */     int i = localListSelectionModel.getMinSelectionIndex();
/* 2175 */     int j = localListSelectionModel.getMaxSelectionIndex();
/*      */ 
/* 2177 */     if ((i < 0) || (j < 0)) {
/* 2178 */       return new int[0];
/*      */     }
/*      */ 
/* 2181 */     int[] arrayOfInt1 = new int[1 + (j - i)];
/* 2182 */     int k = 0;
/* 2183 */     for (int m = i; m <= j; m++) {
/* 2184 */       if (localListSelectionModel.isSelectedIndex(m)) {
/* 2185 */         arrayOfInt1[(k++)] = m;
/*      */       }
/*      */     }
/* 2188 */     int[] arrayOfInt2 = new int[k];
/* 2189 */     System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, k);
/* 2190 */     return arrayOfInt2;
/*      */   }
/*      */ 
/*      */   public void setSelectedIndex(int paramInt)
/*      */   {
/* 2209 */     if (paramInt >= getModel().getSize()) {
/* 2210 */       return;
/*      */     }
/* 2212 */     getSelectionModel().setSelectionInterval(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public void setSelectedIndices(int[] paramArrayOfInt)
/*      */   {
/* 2232 */     ListSelectionModel localListSelectionModel = getSelectionModel();
/* 2233 */     localListSelectionModel.clearSelection();
/* 2234 */     int i = getModel().getSize();
/* 2235 */     for (int m : paramArrayOfInt)
/* 2236 */       if (m < i)
/* 2237 */         localListSelectionModel.addSelectionInterval(m, m);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Object[] getSelectedValues()
/*      */   {
/* 2256 */     ListSelectionModel localListSelectionModel = getSelectionModel();
/* 2257 */     ListModel localListModel = getModel();
/*      */ 
/* 2259 */     int i = localListSelectionModel.getMinSelectionIndex();
/* 2260 */     int j = localListSelectionModel.getMaxSelectionIndex();
/*      */ 
/* 2262 */     if ((i < 0) || (j < 0)) {
/* 2263 */       return new Object[0];
/*      */     }
/*      */ 
/* 2266 */     Object[] arrayOfObject1 = new Object[1 + (j - i)];
/* 2267 */     int k = 0;
/* 2268 */     for (int m = i; m <= j; m++) {
/* 2269 */       if (localListSelectionModel.isSelectedIndex(m)) {
/* 2270 */         arrayOfObject1[(k++)] = localListModel.getElementAt(m);
/*      */       }
/*      */     }
/* 2273 */     Object[] arrayOfObject2 = new Object[k];
/* 2274 */     System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, k);
/* 2275 */     return arrayOfObject2;
/*      */   }
/*      */ 
/*      */   public List<E> getSelectedValuesList()
/*      */   {
/* 2290 */     ListSelectionModel localListSelectionModel = getSelectionModel();
/* 2291 */     ListModel localListModel = getModel();
/*      */ 
/* 2293 */     int i = localListSelectionModel.getMinSelectionIndex();
/* 2294 */     int j = localListSelectionModel.getMaxSelectionIndex();
/*      */ 
/* 2296 */     if ((i < 0) || (j < 0)) {
/* 2297 */       return Collections.emptyList();
/*      */     }
/*      */ 
/* 2300 */     ArrayList localArrayList = new ArrayList();
/* 2301 */     for (int k = i; k <= j; k++) {
/* 2302 */       if (localListSelectionModel.isSelectedIndex(k)) {
/* 2303 */         localArrayList.add(localListModel.getElementAt(k));
/*      */       }
/*      */     }
/* 2306 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public int getSelectedIndex()
/*      */   {
/* 2323 */     return getMinSelectionIndex();
/*      */   }
/*      */ 
/*      */   public E getSelectedValue()
/*      */   {
/* 2342 */     int i = getMinSelectionIndex();
/* 2343 */     return i == -1 ? null : getModel().getElementAt(i);
/*      */   }
/*      */ 
/*      */   public void setSelectedValue(Object paramObject, boolean paramBoolean)
/*      */   {
/* 2355 */     if (paramObject == null) {
/* 2356 */       setSelectedIndex(-1);
/* 2357 */     } else if (!paramObject.equals(getSelectedValue()))
/*      */     {
/* 2359 */       ListModel localListModel = getModel();
/* 2360 */       int i = 0; for (int j = localListModel.getSize(); i < j; i++)
/* 2361 */         if (paramObject.equals(localListModel.getElementAt(i))) {
/* 2362 */           setSelectedIndex(i);
/* 2363 */           if (paramBoolean)
/* 2364 */             ensureIndexIsVisible(i);
/* 2365 */           repaint();
/* 2366 */           return;
/*      */         }
/* 2368 */       setSelectedIndex(-1);
/*      */     }
/* 2370 */     repaint();
/*      */   }
/*      */ 
/*      */   private void checkScrollableParameters(Rectangle paramRectangle, int paramInt)
/*      */   {
/* 2380 */     if (paramRectangle == null) {
/* 2381 */       throw new IllegalArgumentException("visibleRect must be non-null");
/*      */     }
/* 2383 */     switch (paramInt) {
/*      */     case 0:
/*      */     case 1:
/* 2386 */       break;
/*      */     default:
/* 2388 */       throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredScrollableViewportSize()
/*      */   {
/* 2432 */     if (getLayoutOrientation() != 0) {
/* 2433 */       return getPreferredSize();
/*      */     }
/* 2435 */     Insets localInsets = getInsets();
/* 2436 */     int i = localInsets.left + localInsets.right;
/* 2437 */     int j = localInsets.top + localInsets.bottom;
/*      */ 
/* 2439 */     int k = getVisibleRowCount();
/* 2440 */     int m = getFixedCellWidth();
/* 2441 */     int n = getFixedCellHeight();
/*      */     int i1;
/*      */     int i2;
/* 2443 */     if ((m > 0) && (n > 0)) {
/* 2444 */       i1 = m + i;
/* 2445 */       i2 = k * n + j;
/* 2446 */       return new Dimension(i1, i2);
/*      */     }
/* 2448 */     if (getModel().getSize() > 0) {
/* 2449 */       i1 = getPreferredSize().width;
/*      */ 
/* 2451 */       Rectangle localRectangle = getCellBounds(0, 0);
/* 2452 */       if (localRectangle != null) {
/* 2453 */         i2 = k * localRectangle.height + j;
/*      */       }
/*      */       else
/*      */       {
/* 2457 */         i2 = 1;
/*      */       }
/* 2459 */       return new Dimension(i1, i2);
/*      */     }
/*      */ 
/* 2462 */     m = m > 0 ? m : 256;
/* 2463 */     n = n > 0 ? n : 16;
/* 2464 */     return new Dimension(m, n * k);
/*      */   }
/*      */ 
/*      */   public int getScrollableUnitIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 2492 */     checkScrollableParameters(paramRectangle, paramInt1);
/*      */     Point localPoint;
/* 2494 */     if (paramInt1 == 1) {
/* 2495 */       int i = locationToIndex(paramRectangle.getLocation());
/*      */ 
/* 2497 */       if (i == -1) {
/* 2498 */         return 0;
/*      */       }
/*      */ 
/* 2502 */       if (paramInt2 > 0) {
/* 2503 */         localRectangle1 = getCellBounds(i, i);
/* 2504 */         return localRectangle1 == null ? 0 : localRectangle1.height - (paramRectangle.y - localRectangle1.y);
/*      */       }
/*      */ 
/* 2508 */       Rectangle localRectangle1 = getCellBounds(i, i);
/*      */ 
/* 2513 */       if ((localRectangle1.y == paramRectangle.y) && (i == 0)) {
/* 2514 */         return 0;
/*      */       }
/*      */ 
/* 2520 */       if (localRectangle1.y == paramRectangle.y) {
/* 2521 */         localPoint = localRectangle1.getLocation();
/* 2522 */         localPoint.y -= 1;
/* 2523 */         int k = locationToIndex(localPoint);
/* 2524 */         Rectangle localRectangle3 = getCellBounds(k, k);
/*      */ 
/* 2526 */         if ((localRectangle3 == null) || (localRectangle3.y >= localRectangle1.y)) {
/* 2527 */           return 0;
/*      */         }
/* 2529 */         return localRectangle3.height;
/*      */       }
/*      */ 
/* 2535 */       return paramRectangle.y - localRectangle1.y;
/*      */     }
/*      */ 
/* 2539 */     if ((paramInt1 == 0) && (getLayoutOrientation() != 0))
/*      */     {
/* 2541 */       boolean bool = getComponentOrientation().isLeftToRight();
/*      */ 
/* 2545 */       if (bool) {
/* 2546 */         localPoint = paramRectangle.getLocation();
/*      */       }
/*      */       else {
/* 2549 */         localPoint = new Point(paramRectangle.x + paramRectangle.width - 1, paramRectangle.y);
/*      */       }
/*      */ 
/* 2552 */       int j = locationToIndex(localPoint);
/*      */ 
/* 2554 */       if (j != -1) {
/* 2555 */         Rectangle localRectangle2 = getCellBounds(j, j);
/* 2556 */         if ((localRectangle2 != null) && (localRectangle2.contains(localPoint)))
/*      */         {
/*      */           int m;
/*      */           int n;
/* 2560 */           if (bool) {
/* 2561 */             m = paramRectangle.x;
/* 2562 */             n = localRectangle2.x;
/*      */           }
/*      */           else {
/* 2565 */             m = paramRectangle.x + paramRectangle.width;
/* 2566 */             n = localRectangle2.x + localRectangle2.width;
/*      */           }
/*      */ 
/* 2569 */           if (n != m) {
/* 2570 */             if (paramInt2 < 0)
/*      */             {
/* 2572 */               return Math.abs(m - n);
/*      */             }
/*      */ 
/* 2575 */             if (bool)
/*      */             {
/* 2577 */               return n + localRectangle2.width - m;
/*      */             }
/*      */ 
/* 2581 */             return m - localRectangle2.x;
/*      */           }
/*      */ 
/* 2585 */           return localRectangle2.width;
/*      */         }
/*      */       }
/*      */     }
/* 2589 */     Font localFont = getFont();
/* 2590 */     return localFont != null ? localFont.getSize() : 1;
/*      */   }
/*      */ 
/*      */   public int getScrollableBlockIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 2637 */     checkScrollableParameters(paramRectangle, paramInt1);
/*      */     int j;
/*      */     int k;
/*      */     Rectangle localRectangle3;
/* 2638 */     if (paramInt1 == 1) {
/* 2639 */       int i = paramRectangle.height;
/*      */ 
/* 2641 */       if (paramInt2 > 0)
/*      */       {
/* 2643 */         j = locationToIndex(new Point(paramRectangle.x, paramRectangle.y + paramRectangle.height - 1));
/* 2644 */         if (j != -1) {
/* 2645 */           Rectangle localRectangle1 = getCellBounds(j, j);
/* 2646 */           if (localRectangle1 != null) {
/* 2647 */             i = localRectangle1.y - paramRectangle.y;
/* 2648 */             if ((i == 0) && (j < getModel().getSize() - 1)) {
/* 2649 */               i = localRectangle1.height;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2656 */         j = locationToIndex(new Point(paramRectangle.x, paramRectangle.y - paramRectangle.height));
/* 2657 */         k = getFirstVisibleIndex();
/* 2658 */         if (j != -1) {
/* 2659 */           if (k == -1) {
/* 2660 */             k = locationToIndex(paramRectangle.getLocation());
/*      */           }
/* 2662 */           Rectangle localRectangle2 = getCellBounds(j, j);
/* 2663 */           localRectangle3 = getCellBounds(k, k);
/* 2664 */           if ((localRectangle2 != null) && (localRectangle3 != null))
/*      */           {
/* 2666 */             while ((localRectangle2.y + paramRectangle.height < localRectangle3.y + localRectangle3.height) && (localRectangle2.y < localRectangle3.y))
/*      */             {
/* 2668 */               j++;
/* 2669 */               localRectangle2 = getCellBounds(j, j);
/*      */             }
/* 2671 */             i = paramRectangle.y - localRectangle2.y;
/* 2672 */             if ((i <= 0) && (localRectangle2.y > 0)) {
/* 2673 */               j--;
/* 2674 */               localRectangle2 = getCellBounds(j, j);
/* 2675 */               if (localRectangle2 != null) {
/* 2676 */                 i = paramRectangle.y - localRectangle2.y;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2682 */       return i;
/*      */     }
/* 2684 */     if ((paramInt1 == 0) && (getLayoutOrientation() != 0))
/*      */     {
/* 2686 */       boolean bool = getComponentOrientation().isLeftToRight();
/* 2687 */       j = paramRectangle.width;
/*      */       int m;
/* 2689 */       if (paramInt2 > 0)
/*      */       {
/* 2691 */         k = paramRectangle.x + (bool ? paramRectangle.width - 1 : 0);
/* 2692 */         m = locationToIndex(new Point(k, paramRectangle.y));
/*      */ 
/* 2694 */         if (m != -1) {
/* 2695 */           localRectangle3 = getCellBounds(m, m);
/* 2696 */           if (localRectangle3 != null) {
/* 2697 */             if (bool)
/* 2698 */               j = localRectangle3.x - paramRectangle.x;
/*      */             else {
/* 2700 */               j = paramRectangle.x + paramRectangle.width - (localRectangle3.x + localRectangle3.width);
/*      */             }
/*      */ 
/* 2703 */             if (j < 0)
/* 2704 */               j += localRectangle3.width;
/* 2705 */             else if ((j == 0) && (m < getModel().getSize() - 1)) {
/* 2706 */               j = localRectangle3.width;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2716 */         k = paramRectangle.x + (bool ? -paramRectangle.width : paramRectangle.width - 1 + paramRectangle.width);
/*      */ 
/* 2719 */         m = locationToIndex(new Point(k, paramRectangle.y));
/*      */ 
/* 2721 */         if (m != -1) {
/* 2722 */           localRectangle3 = getCellBounds(m, m);
/* 2723 */           if (localRectangle3 != null)
/*      */           {
/* 2725 */             int n = localRectangle3.x + localRectangle3.width;
/*      */ 
/* 2727 */             if (bool) {
/* 2728 */               if ((localRectangle3.x < paramRectangle.x - paramRectangle.width) && (n < paramRectangle.x))
/*      */               {
/* 2730 */                 j = paramRectangle.x - n;
/*      */               }
/* 2732 */               else j = paramRectangle.x - localRectangle3.x; 
/*      */             }
/*      */             else
/*      */             {
/* 2735 */               int i1 = paramRectangle.x + paramRectangle.width;
/*      */ 
/* 2737 */               if ((n > i1 + paramRectangle.width) && (localRectangle3.x > i1))
/*      */               {
/* 2739 */                 j = localRectangle3.x - i1;
/*      */               }
/* 2741 */               else j = n - i1;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2747 */       return j;
/*      */     }
/* 2749 */     return paramRectangle.width;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportWidth()
/*      */   {
/* 2768 */     if ((getLayoutOrientation() == 2) && (getVisibleRowCount() <= 0))
/*      */     {
/* 2770 */       return true;
/*      */     }
/* 2772 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 2773 */     if ((localContainer instanceof JViewport)) {
/* 2774 */       return localContainer.getWidth() > getPreferredSize().width;
/*      */     }
/* 2776 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportHeight()
/*      */   {
/* 2794 */     if ((getLayoutOrientation() == 1) && (getVisibleRowCount() <= 0))
/*      */     {
/* 2796 */       return true;
/*      */     }
/* 2798 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 2799 */     if ((localContainer instanceof JViewport)) {
/* 2800 */       return localContainer.getHeight() > getPreferredSize().height;
/*      */     }
/* 2802 */     return false;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 2811 */     paramObjectOutputStream.defaultWriteObject();
/* 2812 */     if (getUIClassID().equals("ListUI")) {
/* 2813 */       byte b = JComponent.getWriteObjCounter(this);
/* 2814 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 2815 */       if ((b == 0) && (this.ui != null))
/* 2816 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 2832 */     String str1 = this.selectionForeground != null ? this.selectionForeground.toString() : "";
/*      */ 
/* 2835 */     String str2 = this.selectionBackground != null ? this.selectionBackground.toString() : "";
/*      */ 
/* 2839 */     return super.paramString() + ",fixedCellHeight=" + this.fixedCellHeight + ",fixedCellWidth=" + this.fixedCellWidth + ",horizontalScrollIncrement=" + this.horizontalScrollIncrement + ",selectionBackground=" + str2 + ",selectionForeground=" + str1 + ",visibleRowCount=" + this.visibleRowCount + ",layoutOrientation=" + this.layoutOrientation;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 2865 */     if (this.accessibleContext == null) {
/* 2866 */       this.accessibleContext = new AccessibleJList();
/*      */     }
/* 2868 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJList extends JComponent.AccessibleJComponent
/*      */     implements AccessibleSelection, PropertyChangeListener, ListSelectionListener, ListDataListener
/*      */   {
/*      */     int leadSelectionIndex;
/*      */ 
/*      */     public AccessibleJList()
/*      */     {
/* 2893 */       super();
/* 2894 */       JList.this.addPropertyChangeListener(this);
/* 2895 */       JList.this.getSelectionModel().addListSelectionListener(this);
/* 2896 */       JList.this.getModel().addListDataListener(this);
/* 2897 */       this.leadSelectionIndex = JList.this.getLeadSelectionIndex();
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 2909 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 2910 */       Object localObject1 = paramPropertyChangeEvent.getOldValue();
/* 2911 */       Object localObject2 = paramPropertyChangeEvent.getNewValue();
/*      */ 
/* 2914 */       if (str.compareTo("model") == 0)
/*      */       {
/* 2916 */         if ((localObject1 != null) && ((localObject1 instanceof ListModel))) {
/* 2917 */           ((ListModel)localObject1).removeListDataListener(this);
/*      */         }
/* 2919 */         if ((localObject2 != null) && ((localObject2 instanceof ListModel))) {
/* 2920 */           ((ListModel)localObject2).addListDataListener(this);
/*      */         }
/*      */ 
/*      */       }
/* 2924 */       else if (str.compareTo("selectionModel") == 0)
/*      */       {
/* 2926 */         if ((localObject1 != null) && ((localObject1 instanceof ListSelectionModel))) {
/* 2927 */           ((ListSelectionModel)localObject1).removeListSelectionListener(this);
/*      */         }
/* 2929 */         if ((localObject2 != null) && ((localObject2 instanceof ListSelectionModel))) {
/* 2930 */           ((ListSelectionModel)localObject2).addListSelectionListener(this);
/*      */         }
/*      */ 
/* 2933 */         firePropertyChange("AccessibleSelection", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */       }
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/* 2947 */       int i = this.leadSelectionIndex;
/* 2948 */       this.leadSelectionIndex = JList.this.getLeadSelectionIndex();
/* 2949 */       if (i != this.leadSelectionIndex)
/*      */       {
/* 2951 */         localObject1 = i >= 0 ? getAccessibleChild(i) : null;
/*      */ 
/* 2954 */         localObject2 = this.leadSelectionIndex >= 0 ? getAccessibleChild(this.leadSelectionIndex) : null;
/*      */ 
/* 2957 */         firePropertyChange("AccessibleActiveDescendant", localObject1, localObject2);
/*      */       }
/*      */ 
/* 2961 */       firePropertyChange("AccessibleVisibleData", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */ 
/* 2963 */       firePropertyChange("AccessibleSelection", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */ 
/* 2967 */       Object localObject1 = getAccessibleStateSet();
/* 2968 */       Object localObject2 = JList.this.getSelectionModel();
/* 2969 */       if (((ListSelectionModel)localObject2).getSelectionMode() != 0) {
/* 2970 */         if (!((AccessibleStateSet)localObject1).contains(AccessibleState.MULTISELECTABLE)) {
/* 2971 */           ((AccessibleStateSet)localObject1).add(AccessibleState.MULTISELECTABLE);
/* 2972 */           firePropertyChange("AccessibleState", null, AccessibleState.MULTISELECTABLE);
/*      */         }
/*      */ 
/*      */       }
/* 2976 */       else if (((AccessibleStateSet)localObject1).contains(AccessibleState.MULTISELECTABLE)) {
/* 2977 */         ((AccessibleStateSet)localObject1).remove(AccessibleState.MULTISELECTABLE);
/* 2978 */         firePropertyChange("AccessibleState", AccessibleState.MULTISELECTABLE, null);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void intervalAdded(ListDataEvent paramListDataEvent)
/*      */     {
/* 2991 */       firePropertyChange("AccessibleVisibleData", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */     }
/*      */ 
/*      */     public void intervalRemoved(ListDataEvent paramListDataEvent)
/*      */     {
/* 3002 */       firePropertyChange("AccessibleVisibleData", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */     }
/*      */ 
/*      */     public void contentsChanged(ListDataEvent paramListDataEvent)
/*      */     {
/* 3013 */       firePropertyChange("AccessibleVisibleData", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 3027 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 3028 */       if (JList.this.selectionModel.getSelectionMode() != 0)
/*      */       {
/* 3030 */         localAccessibleStateSet.add(AccessibleState.MULTISELECTABLE);
/*      */       }
/* 3032 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 3043 */       return AccessibleRole.LIST;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/* 3055 */       int i = JList.this.locationToIndex(paramPoint);
/* 3056 */       if (i >= 0) {
/* 3057 */         return new AccessibleJListChild(JList.this, i);
/*      */       }
/* 3059 */       return null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 3071 */       return JList.this.getModel().getSize();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 3081 */       if (paramInt >= JList.this.getModel().getSize()) {
/* 3082 */         return null;
/*      */       }
/* 3084 */       return new AccessibleJListChild(JList.this, paramInt);
/*      */     }
/*      */ 
/*      */     public AccessibleSelection getAccessibleSelection()
/*      */     {
/* 3097 */       return this;
/*      */     }
/*      */ 
/*      */     public int getAccessibleSelectionCount()
/*      */     {
/* 3110 */       return JList.this.getSelectedIndices().length;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleSelection(int paramInt)
/*      */     {
/* 3123 */       int i = getAccessibleSelectionCount();
/* 3124 */       if ((paramInt < 0) || (paramInt >= i)) {
/* 3125 */         return null;
/*      */       }
/* 3127 */       return getAccessibleChild(JList.this.getSelectedIndices()[paramInt]);
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleChildSelected(int paramInt)
/*      */     {
/* 3139 */       return JList.this.isSelectedIndex(paramInt);
/*      */     }
/*      */ 
/*      */     public void addAccessibleSelection(int paramInt)
/*      */     {
/* 3152 */       JList.this.addSelectionInterval(paramInt, paramInt);
/*      */     }
/*      */ 
/*      */     public void removeAccessibleSelection(int paramInt)
/*      */     {
/* 3163 */       JList.this.removeSelectionInterval(paramInt, paramInt);
/*      */     }
/*      */ 
/*      */     public void clearAccessibleSelection()
/*      */     {
/* 3171 */       JList.this.clearSelection();
/*      */     }
/*      */ 
/*      */     public void selectAllAccessibleSelection()
/*      */     {
/* 3179 */       JList.this.addSelectionInterval(0, getAccessibleChildrenCount() - 1);
/*      */     }
/*      */ 
/*      */     protected class AccessibleJListChild extends AccessibleContext
/*      */       implements Accessible, AccessibleComponent
/*      */     {
/* 3188 */       private JList<E> parent = null;
/*      */       private int indexInParent;
/* 3190 */       private Component component = null;
/* 3191 */       private AccessibleContext accessibleContext = null;
/*      */       private ListModel<E> listModel;
/* 3193 */       private ListCellRenderer<? super E> cellRenderer = null;
/*      */ 
/*      */       public AccessibleJListChild(int arg2)
/*      */       {
/*      */         Accessible localAccessible;
/* 3196 */         this.parent = localAccessible;
/* 3197 */         setAccessibleParent(localAccessible);
/*      */         int i;
/* 3198 */         this.indexInParent = i;
/* 3199 */         if (localAccessible != null) {
/* 3200 */           this.listModel = localAccessible.getModel();
/* 3201 */           this.cellRenderer = localAccessible.getCellRenderer();
/*      */         }
/*      */       }
/*      */ 
/*      */       private Component getCurrentComponent() {
/* 3206 */         return getComponentAtIndex(this.indexInParent);
/*      */       }
/*      */ 
/*      */       private AccessibleContext getCurrentAccessibleContext() {
/* 3210 */         Component localComponent = getComponentAtIndex(this.indexInParent);
/* 3211 */         if ((localComponent instanceof AccessibleComponent)) {
/* 3212 */           return localComponent.getAccessibleContext();
/*      */         }
/* 3214 */         return null;
/*      */       }
/*      */ 
/*      */       private Component getComponentAtIndex(int paramInt)
/*      */       {
/* 3219 */         if ((paramInt < 0) || (paramInt >= this.listModel.getSize())) {
/* 3220 */           return null;
/*      */         }
/* 3222 */         if ((this.parent != null) && (this.listModel != null) && (this.cellRenderer != null))
/*      */         {
/* 3225 */           Object localObject = this.listModel.getElementAt(paramInt);
/* 3226 */           boolean bool1 = this.parent.isSelectedIndex(paramInt);
/* 3227 */           boolean bool2 = (this.parent.isFocusOwner()) && (paramInt == this.parent.getLeadSelectionIndex());
/*      */ 
/* 3229 */           return this.cellRenderer.getListCellRendererComponent(this.parent, localObject, paramInt, bool1, bool2);
/*      */         }
/*      */ 
/* 3236 */         return null;
/*      */       }
/*      */ 
/*      */       public AccessibleContext getAccessibleContext()
/*      */       {
/* 3250 */         return this;
/*      */       }
/*      */ 
/*      */       public String getAccessibleName()
/*      */       {
/* 3257 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3258 */         if (localAccessibleContext != null) {
/* 3259 */           return localAccessibleContext.getAccessibleName();
/*      */         }
/* 3261 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleName(String paramString)
/*      */       {
/* 3266 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3267 */         if (localAccessibleContext != null)
/* 3268 */           localAccessibleContext.setAccessibleName(paramString);
/*      */       }
/*      */ 
/*      */       public String getAccessibleDescription()
/*      */       {
/* 3273 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3274 */         if (localAccessibleContext != null) {
/* 3275 */           return localAccessibleContext.getAccessibleDescription();
/*      */         }
/* 3277 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleDescription(String paramString)
/*      */       {
/* 3282 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3283 */         if (localAccessibleContext != null)
/* 3284 */           localAccessibleContext.setAccessibleDescription(paramString);
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/* 3289 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3290 */         if (localAccessibleContext != null) {
/* 3291 */           return localAccessibleContext.getAccessibleRole();
/*      */         }
/* 3293 */         return null;
/*      */       }
/*      */ 
/*      */       public AccessibleStateSet getAccessibleStateSet()
/*      */       {
/* 3298 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/*      */         AccessibleStateSet localAccessibleStateSet;
/* 3300 */         if (localAccessibleContext != null)
/* 3301 */           localAccessibleStateSet = localAccessibleContext.getAccessibleStateSet();
/*      */         else {
/* 3303 */           localAccessibleStateSet = new AccessibleStateSet();
/*      */         }
/*      */ 
/* 3306 */         localAccessibleStateSet.add(AccessibleState.SELECTABLE);
/* 3307 */         if ((this.parent.isFocusOwner()) && (this.indexInParent == this.parent.getLeadSelectionIndex()))
/*      */         {
/* 3309 */           localAccessibleStateSet.add(AccessibleState.ACTIVE);
/*      */         }
/* 3311 */         if (this.parent.isSelectedIndex(this.indexInParent)) {
/* 3312 */           localAccessibleStateSet.add(AccessibleState.SELECTED);
/*      */         }
/* 3314 */         if (isShowing())
/* 3315 */           localAccessibleStateSet.add(AccessibleState.SHOWING);
/* 3316 */         else if (localAccessibleStateSet.contains(AccessibleState.SHOWING)) {
/* 3317 */           localAccessibleStateSet.remove(AccessibleState.SHOWING);
/*      */         }
/* 3319 */         if (isVisible())
/* 3320 */           localAccessibleStateSet.add(AccessibleState.VISIBLE);
/* 3321 */         else if (localAccessibleStateSet.contains(AccessibleState.VISIBLE)) {
/* 3322 */           localAccessibleStateSet.remove(AccessibleState.VISIBLE);
/*      */         }
/* 3324 */         localAccessibleStateSet.add(AccessibleState.TRANSIENT);
/* 3325 */         return localAccessibleStateSet;
/*      */       }
/*      */ 
/*      */       public int getAccessibleIndexInParent() {
/* 3329 */         return this.indexInParent;
/*      */       }
/*      */ 
/*      */       public int getAccessibleChildrenCount() {
/* 3333 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3334 */         if (localAccessibleContext != null) {
/* 3335 */           return localAccessibleContext.getAccessibleChildrenCount();
/*      */         }
/* 3337 */         return 0;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleChild(int paramInt)
/*      */       {
/* 3342 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3343 */         if (localAccessibleContext != null) {
/* 3344 */           Accessible localAccessible = localAccessibleContext.getAccessibleChild(paramInt);
/* 3345 */           localAccessibleContext.setAccessibleParent(this);
/* 3346 */           return localAccessible;
/*      */         }
/* 3348 */         return null;
/*      */       }
/*      */ 
/*      */       public Locale getLocale()
/*      */       {
/* 3353 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3354 */         if (localAccessibleContext != null) {
/* 3355 */           return localAccessibleContext.getLocale();
/*      */         }
/* 3357 */         return null;
/*      */       }
/*      */ 
/*      */       public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 3362 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3363 */         if (localAccessibleContext != null)
/* 3364 */           localAccessibleContext.addPropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 3369 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3370 */         if (localAccessibleContext != null)
/* 3371 */           localAccessibleContext.removePropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public AccessibleAction getAccessibleAction()
/*      */       {
/* 3376 */         return getCurrentAccessibleContext().getAccessibleAction();
/*      */       }
/*      */ 
/*      */       public AccessibleComponent getAccessibleComponent()
/*      */       {
/* 3388 */         return this;
/*      */       }
/*      */ 
/*      */       public AccessibleSelection getAccessibleSelection() {
/* 3392 */         return getCurrentAccessibleContext().getAccessibleSelection();
/*      */       }
/*      */ 
/*      */       public AccessibleText getAccessibleText() {
/* 3396 */         return getCurrentAccessibleContext().getAccessibleText();
/*      */       }
/*      */ 
/*      */       public AccessibleValue getAccessibleValue() {
/* 3400 */         return getCurrentAccessibleContext().getAccessibleValue();
/*      */       }
/*      */ 
/*      */       public Color getBackground()
/*      */       {
/* 3407 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3408 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3409 */           return ((AccessibleComponent)localAccessibleContext).getBackground();
/*      */         }
/* 3411 */         Component localComponent = getCurrentComponent();
/* 3412 */         if (localComponent != null) {
/* 3413 */           return localComponent.getBackground();
/*      */         }
/* 3415 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBackground(Color paramColor)
/*      */       {
/* 3421 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3422 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3423 */           ((AccessibleComponent)localAccessibleContext).setBackground(paramColor);
/*      */         } else {
/* 3425 */           Component localComponent = getCurrentComponent();
/* 3426 */           if (localComponent != null)
/* 3427 */             localComponent.setBackground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Color getForeground()
/*      */       {
/* 3433 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3434 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3435 */           return ((AccessibleComponent)localAccessibleContext).getForeground();
/*      */         }
/* 3437 */         Component localComponent = getCurrentComponent();
/* 3438 */         if (localComponent != null) {
/* 3439 */           return localComponent.getForeground();
/*      */         }
/* 3441 */         return null;
/*      */       }
/*      */ 
/*      */       public void setForeground(Color paramColor)
/*      */       {
/* 3447 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3448 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3449 */           ((AccessibleComponent)localAccessibleContext).setForeground(paramColor);
/*      */         } else {
/* 3451 */           Component localComponent = getCurrentComponent();
/* 3452 */           if (localComponent != null)
/* 3453 */             localComponent.setForeground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Cursor getCursor()
/*      */       {
/* 3459 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3460 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3461 */           return ((AccessibleComponent)localAccessibleContext).getCursor();
/*      */         }
/* 3463 */         Component localComponent = getCurrentComponent();
/* 3464 */         if (localComponent != null) {
/* 3465 */           return localComponent.getCursor();
/*      */         }
/* 3467 */         Accessible localAccessible = getAccessibleParent();
/* 3468 */         if ((localAccessible instanceof AccessibleComponent)) {
/* 3469 */           return ((AccessibleComponent)localAccessible).getCursor();
/*      */         }
/* 3471 */         return null;
/*      */       }
/*      */ 
/*      */       public void setCursor(Cursor paramCursor)
/*      */       {
/* 3478 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3479 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3480 */           ((AccessibleComponent)localAccessibleContext).setCursor(paramCursor);
/*      */         } else {
/* 3482 */           Component localComponent = getCurrentComponent();
/* 3483 */           if (localComponent != null)
/* 3484 */             localComponent.setCursor(paramCursor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Font getFont()
/*      */       {
/* 3490 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3491 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3492 */           return ((AccessibleComponent)localAccessibleContext).getFont();
/*      */         }
/* 3494 */         Component localComponent = getCurrentComponent();
/* 3495 */         if (localComponent != null) {
/* 3496 */           return localComponent.getFont();
/*      */         }
/* 3498 */         return null;
/*      */       }
/*      */ 
/*      */       public void setFont(Font paramFont)
/*      */       {
/* 3504 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3505 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3506 */           ((AccessibleComponent)localAccessibleContext).setFont(paramFont);
/*      */         } else {
/* 3508 */           Component localComponent = getCurrentComponent();
/* 3509 */           if (localComponent != null)
/* 3510 */             localComponent.setFont(paramFont);
/*      */         }
/*      */       }
/*      */ 
/*      */       public FontMetrics getFontMetrics(Font paramFont)
/*      */       {
/* 3516 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3517 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3518 */           return ((AccessibleComponent)localAccessibleContext).getFontMetrics(paramFont);
/*      */         }
/* 3520 */         Component localComponent = getCurrentComponent();
/* 3521 */         if (localComponent != null) {
/* 3522 */           return localComponent.getFontMetrics(paramFont);
/*      */         }
/* 3524 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isEnabled()
/*      */       {
/* 3530 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3531 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3532 */           return ((AccessibleComponent)localAccessibleContext).isEnabled();
/*      */         }
/* 3534 */         Component localComponent = getCurrentComponent();
/* 3535 */         if (localComponent != null) {
/* 3536 */           return localComponent.isEnabled();
/*      */         }
/* 3538 */         return false;
/*      */       }
/*      */ 
/*      */       public void setEnabled(boolean paramBoolean)
/*      */       {
/* 3544 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3545 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3546 */           ((AccessibleComponent)localAccessibleContext).setEnabled(paramBoolean);
/*      */         } else {
/* 3548 */           Component localComponent = getCurrentComponent();
/* 3549 */           if (localComponent != null)
/* 3550 */             localComponent.setEnabled(paramBoolean);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isVisible()
/*      */       {
/* 3556 */         int i = this.parent.getFirstVisibleIndex();
/* 3557 */         int j = this.parent.getLastVisibleIndex();
/*      */ 
/* 3561 */         if (j == -1) {
/* 3562 */           j = this.parent.getModel().getSize() - 1;
/*      */         }
/* 3564 */         return (this.indexInParent >= i) && (this.indexInParent <= j);
/*      */       }
/*      */ 
/*      */       public void setVisible(boolean paramBoolean)
/*      */       {
/*      */       }
/*      */ 
/*      */       public boolean isShowing() {
/* 3572 */         return (this.parent.isShowing()) && (isVisible());
/*      */       }
/*      */ 
/*      */       public boolean contains(Point paramPoint) {
/* 3576 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3577 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3578 */           localObject = ((AccessibleComponent)localAccessibleContext).getBounds();
/* 3579 */           return ((Rectangle)localObject).contains(paramPoint);
/*      */         }
/* 3581 */         Object localObject = getCurrentComponent();
/* 3582 */         if (localObject != null) {
/* 3583 */           Rectangle localRectangle = ((Component)localObject).getBounds();
/* 3584 */           return localRectangle.contains(paramPoint);
/*      */         }
/* 3586 */         return getBounds().contains(paramPoint);
/*      */       }
/*      */ 
/*      */       public Point getLocationOnScreen()
/*      */       {
/* 3592 */         if (this.parent != null) {
/* 3593 */           Point localPoint1 = this.parent.getLocationOnScreen();
/* 3594 */           Point localPoint2 = this.parent.indexToLocation(this.indexInParent);
/* 3595 */           if (localPoint2 != null) {
/* 3596 */             localPoint2.translate(localPoint1.x, localPoint1.y);
/* 3597 */             return localPoint2;
/*      */           }
/* 3599 */           return null;
/*      */         }
/*      */ 
/* 3602 */         return null;
/*      */       }
/*      */ 
/*      */       public Point getLocation()
/*      */       {
/* 3607 */         if (this.parent != null) {
/* 3608 */           return this.parent.indexToLocation(this.indexInParent);
/*      */         }
/* 3610 */         return null;
/*      */       }
/*      */ 
/*      */       public void setLocation(Point paramPoint)
/*      */       {
/* 3615 */         if ((this.parent != null) && (this.parent.contains(paramPoint)))
/* 3616 */           JList.this.ensureIndexIsVisible(this.indexInParent);
/*      */       }
/*      */ 
/*      */       public Rectangle getBounds()
/*      */       {
/* 3621 */         if (this.parent != null) {
/* 3622 */           return this.parent.getCellBounds(this.indexInParent, this.indexInParent);
/*      */         }
/* 3624 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBounds(Rectangle paramRectangle)
/*      */       {
/* 3629 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3630 */         if ((localAccessibleContext instanceof AccessibleComponent))
/* 3631 */           ((AccessibleComponent)localAccessibleContext).setBounds(paramRectangle);
/*      */       }
/*      */ 
/*      */       public Dimension getSize()
/*      */       {
/* 3636 */         Rectangle localRectangle = getBounds();
/* 3637 */         if (localRectangle != null) {
/* 3638 */           return localRectangle.getSize();
/*      */         }
/* 3640 */         return null;
/*      */       }
/*      */ 
/*      */       public void setSize(Dimension paramDimension)
/*      */       {
/* 3645 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3646 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3647 */           ((AccessibleComponent)localAccessibleContext).setSize(paramDimension);
/*      */         } else {
/* 3649 */           Component localComponent = getCurrentComponent();
/* 3650 */           if (localComponent != null)
/* 3651 */             localComponent.setSize(paramDimension);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleAt(Point paramPoint)
/*      */       {
/* 3657 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3658 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3659 */           return ((AccessibleComponent)localAccessibleContext).getAccessibleAt(paramPoint);
/*      */         }
/* 3661 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isFocusTraversable()
/*      */       {
/* 3666 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3667 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3668 */           return ((AccessibleComponent)localAccessibleContext).isFocusTraversable();
/*      */         }
/* 3670 */         Component localComponent = getCurrentComponent();
/* 3671 */         if (localComponent != null) {
/* 3672 */           return localComponent.isFocusTraversable();
/*      */         }
/* 3674 */         return false;
/*      */       }
/*      */ 
/*      */       public void requestFocus()
/*      */       {
/* 3680 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3681 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3682 */           ((AccessibleComponent)localAccessibleContext).requestFocus();
/*      */         } else {
/* 3684 */           Component localComponent = getCurrentComponent();
/* 3685 */           if (localComponent != null)
/* 3686 */             localComponent.requestFocus();
/*      */         }
/*      */       }
/*      */ 
/*      */       public void addFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 3692 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3693 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3694 */           ((AccessibleComponent)localAccessibleContext).addFocusListener(paramFocusListener);
/*      */         } else {
/* 3696 */           Component localComponent = getCurrentComponent();
/* 3697 */           if (localComponent != null)
/* 3698 */             localComponent.addFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void removeFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 3704 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3705 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 3706 */           ((AccessibleComponent)localAccessibleContext).removeFocusListener(paramFocusListener);
/*      */         } else {
/* 3708 */           Component localComponent = getCurrentComponent();
/* 3709 */           if (localComponent != null)
/* 3710 */             localComponent.removeFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */ 
/*      */       public AccessibleIcon[] getAccessibleIcon()
/*      */       {
/* 3726 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 3727 */         if (localAccessibleContext != null) {
/* 3728 */           return localAccessibleContext.getAccessibleIcon();
/*      */         }
/* 3730 */         return null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class DropLocation extends TransferHandler.DropLocation
/*      */   {
/*      */     private final int index;
/*      */     private final boolean isInsert;
/*      */ 
/*      */     private DropLocation(Point paramPoint, int paramInt, boolean paramBoolean)
/*      */     {
/*  355 */       super();
/*  356 */       this.index = paramInt;
/*  357 */       this.isInsert = paramBoolean;
/*      */     }
/*      */ 
/*      */     public int getIndex()
/*      */     {
/*  378 */       return this.index;
/*      */     }
/*      */ 
/*      */     public boolean isInsert()
/*      */     {
/*  388 */       return this.isInsert;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  400 */       return getClass().getName() + "[dropPoint=" + getDropPoint() + "," + "index=" + this.index + "," + "insert=" + this.isInsert + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ListSelectionHandler
/*      */     implements ListSelectionListener, Serializable
/*      */   {
/*      */     private ListSelectionHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/* 1812 */       JList.this.fireSelectionValueChanged(paramListSelectionEvent.getFirstIndex(), paramListSelectionEvent.getLastIndex(), paramListSelectionEvent.getValueIsAdjusting());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JList
 * JD-Core Version:    0.6.2
 */