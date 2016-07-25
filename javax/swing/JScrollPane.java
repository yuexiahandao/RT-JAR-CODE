/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.Transient;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRelation;
/*      */ import javax.accessibility.AccessibleRelationSet;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.ScrollPaneUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ 
/*      */ public class JScrollPane extends JComponent
/*      */   implements ScrollPaneConstants, Accessible
/*      */ {
/*      */   private Border viewportBorder;
/*      */   private static final String uiClassID = "ScrollPaneUI";
/*  187 */   protected int verticalScrollBarPolicy = 20;
/*      */ 
/*  196 */   protected int horizontalScrollBarPolicy = 30;
/*      */   protected JViewport viewport;
/*      */   protected JScrollBar verticalScrollBar;
/*      */   protected JScrollBar horizontalScrollBar;
/*      */   protected JViewport rowHeader;
/*      */   protected JViewport columnHeader;
/*      */   protected Component lowerLeft;
/*      */   protected Component lowerRight;
/*      */   protected Component upperLeft;
/*      */   protected Component upperRight;
/*  271 */   private boolean wheelScrollState = true;
/*      */ 
/*      */   public JScrollPane(Component paramComponent, int paramInt1, int paramInt2)
/*      */   {
/*  295 */     setLayout(new ScrollPaneLayout.UIResource());
/*  296 */     setVerticalScrollBarPolicy(paramInt1);
/*  297 */     setHorizontalScrollBarPolicy(paramInt2);
/*  298 */     setViewport(createViewport());
/*  299 */     setVerticalScrollBar(createVerticalScrollBar());
/*  300 */     setHorizontalScrollBar(createHorizontalScrollBar());
/*  301 */     if (paramComponent != null) {
/*  302 */       setViewportView(paramComponent);
/*      */     }
/*  304 */     setUIProperty("opaque", Boolean.valueOf(true));
/*  305 */     updateUI();
/*      */ 
/*  307 */     if (!getComponentOrientation().isLeftToRight())
/*  308 */       this.viewport.setViewPosition(new Point(2147483647, 0));
/*      */   }
/*      */ 
/*      */   public JScrollPane(Component paramComponent)
/*      */   {
/*  323 */     this(paramComponent, 20, 30);
/*      */   }
/*      */ 
/*      */   public JScrollPane(int paramInt1, int paramInt2)
/*      */   {
/*  342 */     this(null, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public JScrollPane()
/*      */   {
/*  351 */     this(null, 20, 30);
/*      */   }
/*      */ 
/*      */   public ScrollPaneUI getUI()
/*      */   {
/*  368 */     return (ScrollPaneUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(ScrollPaneUI paramScrollPaneUI)
/*      */   {
/*  380 */     super.setUI(paramScrollPaneUI);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  393 */     setUI((ScrollPaneUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  409 */     return "ScrollPaneUI";
/*      */   }
/*      */ 
/*      */   public void setLayout(LayoutManager paramLayoutManager)
/*      */   {
/*  433 */     if ((paramLayoutManager instanceof ScrollPaneLayout)) {
/*  434 */       super.setLayout(paramLayoutManager);
/*  435 */       ((ScrollPaneLayout)paramLayoutManager).syncWithScrollPane(this);
/*      */     }
/*  437 */     else if (paramLayoutManager == null) {
/*  438 */       super.setLayout(paramLayoutManager);
/*      */     }
/*      */     else {
/*  441 */       String str = "layout of JScrollPane must be a ScrollPaneLayout";
/*  442 */       throw new ClassCastException(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isValidateRoot()
/*      */   {
/*  463 */     return true;
/*      */   }
/*      */ 
/*      */   public int getVerticalScrollBarPolicy()
/*      */   {
/*  473 */     return this.verticalScrollBarPolicy;
/*      */   }
/*      */ 
/*      */   public void setVerticalScrollBarPolicy(int paramInt)
/*      */   {
/*  500 */     switch (paramInt) {
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*  504 */       break;
/*      */     default:
/*  506 */       throw new IllegalArgumentException("invalid verticalScrollBarPolicy");
/*      */     }
/*  508 */     int i = this.verticalScrollBarPolicy;
/*  509 */     this.verticalScrollBarPolicy = paramInt;
/*  510 */     firePropertyChange("verticalScrollBarPolicy", i, paramInt);
/*  511 */     revalidate();
/*  512 */     repaint();
/*      */   }
/*      */ 
/*      */   public int getHorizontalScrollBarPolicy()
/*      */   {
/*  522 */     return this.horizontalScrollBarPolicy;
/*      */   }
/*      */ 
/*      */   public void setHorizontalScrollBarPolicy(int paramInt)
/*      */   {
/*  548 */     switch (paramInt) {
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*  552 */       break;
/*      */     default:
/*  554 */       throw new IllegalArgumentException("invalid horizontalScrollBarPolicy");
/*      */     }
/*  556 */     int i = this.horizontalScrollBarPolicy;
/*  557 */     this.horizontalScrollBarPolicy = paramInt;
/*  558 */     firePropertyChange("horizontalScrollBarPolicy", i, paramInt);
/*  559 */     revalidate();
/*  560 */     repaint();
/*      */   }
/*      */ 
/*      */   public Border getViewportBorder()
/*      */   {
/*  571 */     return this.viewportBorder;
/*      */   }
/*      */ 
/*      */   public void setViewportBorder(Border paramBorder)
/*      */   {
/*  595 */     Border localBorder = this.viewportBorder;
/*  596 */     this.viewportBorder = paramBorder;
/*  597 */     firePropertyChange("viewportBorder", localBorder, paramBorder);
/*      */   }
/*      */ 
/*      */   public Rectangle getViewportBorderBounds()
/*      */   {
/*  608 */     Rectangle localRectangle = new Rectangle(getSize());
/*      */ 
/*  610 */     Insets localInsets = getInsets();
/*  611 */     localRectangle.x = localInsets.left;
/*  612 */     localRectangle.y = localInsets.top;
/*  613 */     localRectangle.width -= localInsets.left + localInsets.right;
/*  614 */     localRectangle.height -= localInsets.top + localInsets.bottom;
/*      */ 
/*  616 */     boolean bool = SwingUtilities.isLeftToRight(this);
/*      */ 
/*  622 */     JViewport localJViewport1 = getColumnHeader();
/*  623 */     if ((localJViewport1 != null) && (localJViewport1.isVisible())) {
/*  624 */       int i = localJViewport1.getHeight();
/*  625 */       localRectangle.y += i;
/*  626 */       localRectangle.height -= i;
/*      */     }
/*      */ 
/*  633 */     JViewport localJViewport2 = getRowHeader();
/*  634 */     if ((localJViewport2 != null) && (localJViewport2.isVisible())) {
/*  635 */       int j = localJViewport2.getWidth();
/*  636 */       if (bool) {
/*  637 */         localRectangle.x += j;
/*      */       }
/*  639 */       localRectangle.width -= j;
/*      */     }
/*      */ 
/*  645 */     JScrollBar localJScrollBar1 = getVerticalScrollBar();
/*  646 */     if ((localJScrollBar1 != null) && (localJScrollBar1.isVisible())) {
/*  647 */       int k = localJScrollBar1.getWidth();
/*  648 */       if (!bool) {
/*  649 */         localRectangle.x += k;
/*      */       }
/*  651 */       localRectangle.width -= k;
/*      */     }
/*      */ 
/*  657 */     JScrollBar localJScrollBar2 = getHorizontalScrollBar();
/*  658 */     if ((localJScrollBar2 != null) && (localJScrollBar2.isVisible())) {
/*  659 */       localRectangle.height -= localJScrollBar2.getHeight();
/*      */     }
/*      */ 
/*  662 */     return localRectangle;
/*      */   }
/*      */ 
/*      */   public JScrollBar createHorizontalScrollBar()
/*      */   {
/*  814 */     return new ScrollBar(0);
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public JScrollBar getHorizontalScrollBar()
/*      */   {
/*  827 */     return this.horizontalScrollBar;
/*      */   }
/*      */ 
/*      */   public void setHorizontalScrollBar(JScrollBar paramJScrollBar)
/*      */   {
/*  847 */     JScrollBar localJScrollBar = getHorizontalScrollBar();
/*  848 */     this.horizontalScrollBar = paramJScrollBar;
/*  849 */     if (paramJScrollBar != null) {
/*  850 */       add(paramJScrollBar, "HORIZONTAL_SCROLLBAR");
/*      */     }
/*  852 */     else if (localJScrollBar != null) {
/*  853 */       remove(localJScrollBar);
/*      */     }
/*  855 */     firePropertyChange("horizontalScrollBar", localJScrollBar, paramJScrollBar);
/*      */ 
/*  857 */     revalidate();
/*  858 */     repaint();
/*      */   }
/*      */ 
/*      */   public JScrollBar createVerticalScrollBar()
/*      */   {
/*  873 */     return new ScrollBar(1);
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public JScrollBar getVerticalScrollBar()
/*      */   {
/*  886 */     return this.verticalScrollBar;
/*      */   }
/*      */ 
/*      */   public void setVerticalScrollBar(JScrollBar paramJScrollBar)
/*      */   {
/*  906 */     JScrollBar localJScrollBar = getVerticalScrollBar();
/*  907 */     this.verticalScrollBar = paramJScrollBar;
/*  908 */     add(paramJScrollBar, "VERTICAL_SCROLLBAR");
/*  909 */     firePropertyChange("verticalScrollBar", localJScrollBar, paramJScrollBar);
/*      */ 
/*  911 */     revalidate();
/*  912 */     repaint();
/*      */   }
/*      */ 
/*      */   protected JViewport createViewport()
/*      */   {
/*  927 */     return new JViewport();
/*      */   }
/*      */ 
/*      */   public JViewport getViewport()
/*      */   {
/*  938 */     return this.viewport;
/*      */   }
/*      */ 
/*      */   public void setViewport(JViewport paramJViewport)
/*      */   {
/*  968 */     JViewport localJViewport = getViewport();
/*  969 */     this.viewport = paramJViewport;
/*  970 */     if (paramJViewport != null) {
/*  971 */       add(paramJViewport, "VIEWPORT");
/*      */     }
/*  973 */     else if (localJViewport != null) {
/*  974 */       remove(localJViewport);
/*      */     }
/*  976 */     firePropertyChange("viewport", localJViewport, paramJViewport);
/*      */ 
/*  978 */     if (this.accessibleContext != null) {
/*  979 */       ((AccessibleJScrollPane)this.accessibleContext).resetViewPort();
/*      */     }
/*      */ 
/*  982 */     revalidate();
/*  983 */     repaint();
/*      */   }
/*      */ 
/*      */   public void setViewportView(Component paramComponent)
/*      */   {
/* 1004 */     if (getViewport() == null) {
/* 1005 */       setViewport(createViewport());
/*      */     }
/* 1007 */     getViewport().setView(paramComponent);
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public JViewport getRowHeader()
/*      */   {
/* 1019 */     return this.rowHeader;
/*      */   }
/*      */ 
/*      */   public void setRowHeader(JViewport paramJViewport)
/*      */   {
/* 1045 */     JViewport localJViewport = getRowHeader();
/* 1046 */     this.rowHeader = paramJViewport;
/* 1047 */     if (paramJViewport != null) {
/* 1048 */       add(paramJViewport, "ROW_HEADER");
/*      */     }
/* 1050 */     else if (localJViewport != null) {
/* 1051 */       remove(localJViewport);
/*      */     }
/* 1053 */     firePropertyChange("rowHeader", localJViewport, paramJViewport);
/* 1054 */     revalidate();
/* 1055 */     repaint();
/*      */   }
/*      */ 
/*      */   public void setRowHeaderView(Component paramComponent)
/*      */   {
/* 1074 */     if (getRowHeader() == null) {
/* 1075 */       setRowHeader(createViewport());
/*      */     }
/* 1077 */     getRowHeader().setView(paramComponent);
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public JViewport getColumnHeader()
/*      */   {
/* 1089 */     return this.columnHeader;
/*      */   }
/*      */ 
/*      */   public void setColumnHeader(JViewport paramJViewport)
/*      */   {
/* 1111 */     JViewport localJViewport = getColumnHeader();
/* 1112 */     this.columnHeader = paramJViewport;
/* 1113 */     if (paramJViewport != null) {
/* 1114 */       add(paramJViewport, "COLUMN_HEADER");
/*      */     }
/* 1116 */     else if (localJViewport != null) {
/* 1117 */       remove(localJViewport);
/*      */     }
/* 1119 */     firePropertyChange("columnHeader", localJViewport, paramJViewport);
/*      */ 
/* 1121 */     revalidate();
/* 1122 */     repaint();
/*      */   }
/*      */ 
/*      */   public void setColumnHeaderView(Component paramComponent)
/*      */   {
/* 1143 */     if (getColumnHeader() == null) {
/* 1144 */       setColumnHeader(createViewport());
/*      */     }
/* 1146 */     getColumnHeader().setView(paramComponent);
/*      */   }
/*      */ 
/*      */   public Component getCorner(String paramString)
/*      */   {
/* 1171 */     boolean bool = getComponentOrientation().isLeftToRight();
/* 1172 */     if (paramString.equals("LOWER_LEADING_CORNER"))
/* 1173 */       paramString = bool ? "LOWER_LEFT_CORNER" : "LOWER_RIGHT_CORNER";
/* 1174 */     else if (paramString.equals("LOWER_TRAILING_CORNER"))
/* 1175 */       paramString = bool ? "LOWER_RIGHT_CORNER" : "LOWER_LEFT_CORNER";
/* 1176 */     else if (paramString.equals("UPPER_LEADING_CORNER"))
/* 1177 */       paramString = bool ? "UPPER_LEFT_CORNER" : "UPPER_RIGHT_CORNER";
/* 1178 */     else if (paramString.equals("UPPER_TRAILING_CORNER")) {
/* 1179 */       paramString = bool ? "UPPER_RIGHT_CORNER" : "UPPER_LEFT_CORNER";
/*      */     }
/* 1181 */     if (paramString.equals("LOWER_LEFT_CORNER")) {
/* 1182 */       return this.lowerLeft;
/*      */     }
/* 1184 */     if (paramString.equals("LOWER_RIGHT_CORNER")) {
/* 1185 */       return this.lowerRight;
/*      */     }
/* 1187 */     if (paramString.equals("UPPER_LEFT_CORNER")) {
/* 1188 */       return this.upperLeft;
/*      */     }
/* 1190 */     if (paramString.equals("UPPER_RIGHT_CORNER")) {
/* 1191 */       return this.upperRight;
/*      */     }
/*      */ 
/* 1194 */     return null;
/*      */   }
/*      */ 
/*      */   public void setCorner(String paramString, Component paramComponent)
/*      */   {
/* 1234 */     boolean bool = getComponentOrientation().isLeftToRight();
/* 1235 */     if (paramString.equals("LOWER_LEADING_CORNER"))
/* 1236 */       paramString = bool ? "LOWER_LEFT_CORNER" : "LOWER_RIGHT_CORNER";
/* 1237 */     else if (paramString.equals("LOWER_TRAILING_CORNER"))
/* 1238 */       paramString = bool ? "LOWER_RIGHT_CORNER" : "LOWER_LEFT_CORNER";
/* 1239 */     else if (paramString.equals("UPPER_LEADING_CORNER"))
/* 1240 */       paramString = bool ? "UPPER_LEFT_CORNER" : "UPPER_RIGHT_CORNER";
/* 1241 */     else if (paramString.equals("UPPER_TRAILING_CORNER"))
/* 1242 */       paramString = bool ? "UPPER_RIGHT_CORNER" : "UPPER_LEFT_CORNER";
/*      */     Component localComponent;
/* 1244 */     if (paramString.equals("LOWER_LEFT_CORNER")) {
/* 1245 */       localComponent = this.lowerLeft;
/* 1246 */       this.lowerLeft = paramComponent;
/*      */     }
/* 1248 */     else if (paramString.equals("LOWER_RIGHT_CORNER")) {
/* 1249 */       localComponent = this.lowerRight;
/* 1250 */       this.lowerRight = paramComponent;
/*      */     }
/* 1252 */     else if (paramString.equals("UPPER_LEFT_CORNER")) {
/* 1253 */       localComponent = this.upperLeft;
/* 1254 */       this.upperLeft = paramComponent;
/*      */     }
/* 1256 */     else if (paramString.equals("UPPER_RIGHT_CORNER")) {
/* 1257 */       localComponent = this.upperRight;
/* 1258 */       this.upperRight = paramComponent;
/*      */     }
/*      */     else {
/* 1261 */       throw new IllegalArgumentException("invalid corner key");
/*      */     }
/* 1263 */     if (localComponent != null) {
/* 1264 */       remove(localComponent);
/*      */     }
/* 1266 */     if (paramComponent != null) {
/* 1267 */       add(paramComponent, paramString);
/*      */     }
/* 1269 */     firePropertyChange(paramString, localComponent, paramComponent);
/* 1270 */     revalidate();
/* 1271 */     repaint();
/*      */   }
/*      */ 
/*      */   public void setComponentOrientation(ComponentOrientation paramComponentOrientation)
/*      */   {
/* 1288 */     super.setComponentOrientation(paramComponentOrientation);
/* 1289 */     if (this.verticalScrollBar != null)
/* 1290 */       this.verticalScrollBar.setComponentOrientation(paramComponentOrientation);
/* 1291 */     if (this.horizontalScrollBar != null)
/* 1292 */       this.horizontalScrollBar.setComponentOrientation(paramComponentOrientation);
/*      */   }
/*      */ 
/*      */   public boolean isWheelScrollingEnabled()
/*      */   {
/* 1305 */     return this.wheelScrollState;
/*      */   }
/*      */ 
/*      */   public void setWheelScrollingEnabled(boolean paramBoolean)
/*      */   {
/* 1323 */     boolean bool = this.wheelScrollState;
/* 1324 */     this.wheelScrollState = paramBoolean;
/* 1325 */     firePropertyChange("wheelScrollingEnabled", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1334 */     paramObjectOutputStream.defaultWriteObject();
/* 1335 */     if (getUIClassID().equals("ScrollPaneUI")) {
/* 1336 */       byte b = JComponent.getWriteObjCounter(this);
/* 1337 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 1338 */       if ((b == 0) && (this.ui != null))
/* 1339 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1356 */     String str1 = this.viewportBorder != null ? this.viewportBorder.toString() : "";
/*      */ 
/* 1358 */     String str2 = this.viewport != null ? this.viewport.toString() : "";
/*      */     String str3;
/* 1361 */     if (this.verticalScrollBarPolicy == 20)
/* 1362 */       str3 = "VERTICAL_SCROLLBAR_AS_NEEDED";
/* 1363 */     else if (this.verticalScrollBarPolicy == 21)
/* 1364 */       str3 = "VERTICAL_SCROLLBAR_NEVER";
/* 1365 */     else if (this.verticalScrollBarPolicy == 22)
/* 1366 */       str3 = "VERTICAL_SCROLLBAR_ALWAYS";
/* 1367 */     else str3 = "";
/*      */     String str4;
/* 1369 */     if (this.horizontalScrollBarPolicy == 30)
/* 1370 */       str4 = "HORIZONTAL_SCROLLBAR_AS_NEEDED";
/* 1371 */     else if (this.horizontalScrollBarPolicy == 31)
/* 1372 */       str4 = "HORIZONTAL_SCROLLBAR_NEVER";
/* 1373 */     else if (this.horizontalScrollBarPolicy == 32)
/* 1374 */       str4 = "HORIZONTAL_SCROLLBAR_ALWAYS";
/* 1375 */     else str4 = "";
/* 1376 */     String str5 = this.horizontalScrollBar != null ? this.horizontalScrollBar.toString() : "";
/*      */ 
/* 1379 */     String str6 = this.verticalScrollBar != null ? this.verticalScrollBar.toString() : "";
/*      */ 
/* 1381 */     String str7 = this.columnHeader != null ? this.columnHeader.toString() : "";
/*      */ 
/* 1383 */     String str8 = this.rowHeader != null ? this.rowHeader.toString() : "";
/*      */ 
/* 1385 */     String str9 = this.lowerLeft != null ? this.lowerLeft.toString() : "";
/*      */ 
/* 1387 */     String str10 = this.lowerRight != null ? this.lowerRight.toString() : "";
/*      */ 
/* 1389 */     String str11 = this.upperLeft != null ? this.upperLeft.toString() : "";
/*      */ 
/* 1391 */     String str12 = this.upperRight != null ? this.upperRight.toString() : "";
/*      */ 
/* 1394 */     return super.paramString() + ",columnHeader=" + str7 + ",horizontalScrollBar=" + str5 + ",horizontalScrollBarPolicy=" + str4 + ",lowerLeft=" + str9 + ",lowerRight=" + str10 + ",rowHeader=" + str8 + ",upperLeft=" + str11 + ",upperRight=" + str12 + ",verticalScrollBar=" + str6 + ",verticalScrollBarPolicy=" + str3 + ",viewport=" + str2 + ",viewportBorder=" + str1;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1423 */     if (this.accessibleContext == null) {
/* 1424 */       this.accessibleContext = new AccessibleJScrollPane();
/*      */     }
/* 1426 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJScrollPane extends JComponent.AccessibleJComponent
/*      */     implements ChangeListener, PropertyChangeListener
/*      */   {
/* 1447 */     protected JViewport viewPort = null;
/*      */ 
/*      */     public void resetViewPort()
/*      */     {
/* 1453 */       if (this.viewPort != null) {
/* 1454 */         this.viewPort.removeChangeListener(this);
/* 1455 */         this.viewPort.removePropertyChangeListener(this);
/*      */       }
/* 1457 */       this.viewPort = JScrollPane.this.getViewport();
/* 1458 */       if (this.viewPort != null) {
/* 1459 */         this.viewPort.addChangeListener(this);
/* 1460 */         this.viewPort.addPropertyChangeListener(this);
/*      */       }
/*      */     }
/*      */ 
/*      */     public AccessibleJScrollPane()
/*      */     {
/* 1468 */       super();
/*      */ 
/* 1470 */       resetViewPort();
/*      */ 
/* 1474 */       JScrollBar localJScrollBar = JScrollPane.this.getHorizontalScrollBar();
/* 1475 */       if (localJScrollBar != null) {
/* 1476 */         setScrollBarRelations(localJScrollBar);
/*      */       }
/* 1478 */       localJScrollBar = JScrollPane.this.getVerticalScrollBar();
/* 1479 */       if (localJScrollBar != null)
/* 1480 */         setScrollBarRelations(localJScrollBar);
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1492 */       return AccessibleRole.SCROLL_PANE;
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1503 */       if (paramChangeEvent == null) {
/* 1504 */         throw new NullPointerException();
/*      */       }
/* 1506 */       firePropertyChange("AccessibleVisibleData", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1520 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1521 */       if ((str == "horizontalScrollBar") || (str == "verticalScrollBar"))
/*      */       {
/* 1524 */         if ((paramPropertyChangeEvent.getNewValue() instanceof JScrollBar))
/* 1525 */           setScrollBarRelations((JScrollBar)paramPropertyChangeEvent.getNewValue());
/*      */       }
/*      */     }
/*      */ 
/*      */     void setScrollBarRelations(JScrollBar paramJScrollBar)
/*      */     {
/* 1540 */       AccessibleRelation localAccessibleRelation1 = new AccessibleRelation(AccessibleRelation.CONTROLLED_BY, paramJScrollBar);
/*      */ 
/* 1543 */       AccessibleRelation localAccessibleRelation2 = new AccessibleRelation(AccessibleRelation.CONTROLLER_FOR, JScrollPane.this);
/*      */ 
/* 1548 */       AccessibleContext localAccessibleContext = paramJScrollBar.getAccessibleContext();
/* 1549 */       localAccessibleContext.getAccessibleRelationSet().add(localAccessibleRelation2);
/*      */ 
/* 1552 */       getAccessibleRelationSet().add(localAccessibleRelation1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ScrollBar extends JScrollBar
/*      */     implements UIResource
/*      */   {
/*      */     private boolean unitIncrementSet;
/*      */     private boolean blockIncrementSet;
/*      */ 
/*      */     public ScrollBar(int arg2)
/*      */     {
/*  718 */       super();
/*  719 */       putClientProperty("JScrollBar.fastWheelScrolling", Boolean.TRUE);
/*      */     }
/*      */ 
/*      */     public void setUnitIncrement(int paramInt)
/*      */     {
/*  730 */       this.unitIncrementSet = true;
/*  731 */       putClientProperty("JScrollBar.fastWheelScrolling", null);
/*  732 */       super.setUnitIncrement(paramInt);
/*      */     }
/*      */ 
/*      */     public int getUnitIncrement(int paramInt)
/*      */     {
/*  746 */       JViewport localJViewport = JScrollPane.this.getViewport();
/*  747 */       if ((!this.unitIncrementSet) && (localJViewport != null) && ((localJViewport.getView() instanceof Scrollable)))
/*      */       {
/*  749 */         Scrollable localScrollable = (Scrollable)localJViewport.getView();
/*  750 */         Rectangle localRectangle = localJViewport.getViewRect();
/*  751 */         return localScrollable.getScrollableUnitIncrement(localRectangle, getOrientation(), paramInt);
/*      */       }
/*      */ 
/*  754 */       return super.getUnitIncrement(paramInt);
/*      */     }
/*      */ 
/*      */     public void setBlockIncrement(int paramInt)
/*      */     {
/*  765 */       this.blockIncrementSet = true;
/*  766 */       putClientProperty("JScrollBar.fastWheelScrolling", null);
/*  767 */       super.setBlockIncrement(paramInt);
/*      */     }
/*      */ 
/*      */     public int getBlockIncrement(int paramInt)
/*      */     {
/*  783 */       JViewport localJViewport = JScrollPane.this.getViewport();
/*  784 */       if ((this.blockIncrementSet) || (localJViewport == null)) {
/*  785 */         return super.getBlockIncrement(paramInt);
/*      */       }
/*  787 */       if ((localJViewport.getView() instanceof Scrollable)) {
/*  788 */         Scrollable localScrollable = (Scrollable)localJViewport.getView();
/*  789 */         Rectangle localRectangle = localJViewport.getViewRect();
/*  790 */         return localScrollable.getScrollableBlockIncrement(localRectangle, getOrientation(), paramInt);
/*      */       }
/*  792 */       if (getOrientation() == 1) {
/*  793 */         return localJViewport.getExtentSize().height;
/*      */       }
/*      */ 
/*  796 */       return localJViewport.getExtentSize().width;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JScrollPane
 * JD-Core Version:    0.6.2
 */