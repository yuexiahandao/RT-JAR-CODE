/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionAdapter;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.Serializable;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.swing.BoxLayout;
/*      */ import javax.swing.ComboBoxEditor;
/*      */ import javax.swing.ComboBoxModel;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JScrollBar;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.ListModel;
/*      */ import javax.swing.MenuElement;
/*      */ import javax.swing.MenuSelectionManager;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.LineBorder;
/*      */ import javax.swing.event.ListDataEvent;
/*      */ import javax.swing.event.ListDataListener;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ 
/*      */ public class BasicComboPopup extends JPopupMenu
/*      */   implements ComboPopup
/*      */ {
/*   74 */   static final ListModel EmptyListModel = new EmptyListModelClass(null);
/*      */ 
/*   76 */   private static Border LIST_BORDER = new LineBorder(Color.BLACK, 1);
/*      */   protected JComboBox comboBox;
/*      */   protected JList list;
/*      */   protected JScrollPane scroller;
/*   99 */   protected boolean valueIsAdjusting = false;
/*      */   private Handler handler;
/*      */   protected MouseMotionListener mouseMotionListener;
/*      */   protected MouseListener mouseListener;
/*      */   protected KeyListener keyListener;
/*      */   protected ListSelectionListener listSelectionListener;
/*      */   protected MouseListener listMouseListener;
/*      */   protected MouseMotionListener listMouseMotionListener;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   protected ListDataListener listDataListener;
/*      */   protected ItemListener itemListener;
/*      */   protected Timer autoscrollTimer;
/*  189 */   protected boolean hasEntered = false;
/*  190 */   protected boolean isAutoScrolling = false;
/*  191 */   protected int scrollDirection = 0;
/*      */   protected static final int SCROLL_UP = 0;
/*      */   protected static final int SCROLL_DOWN = 1;
/*      */ 
/*      */   public void show()
/*      */   {
/*  205 */     this.comboBox.firePopupMenuWillBecomeVisible();
/*  206 */     setListSelection(this.comboBox.getSelectedIndex());
/*  207 */     Point localPoint = getPopupLocation();
/*  208 */     show(this.comboBox, localPoint.x, localPoint.y);
/*      */   }
/*      */ 
/*      */   public void hide()
/*      */   {
/*  216 */     MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  217 */     MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/*  218 */     for (int i = 0; i < arrayOfMenuElement.length; i++) {
/*  219 */       if (arrayOfMenuElement[i] == this) {
/*  220 */         localMenuSelectionManager.clearSelectedPath();
/*  221 */         break;
/*      */       }
/*      */     }
/*  224 */     if (arrayOfMenuElement.length > 0)
/*  225 */       this.comboBox.repaint();
/*      */   }
/*      */ 
/*      */   public JList getList()
/*      */   {
/*  233 */     return this.list;
/*      */   }
/*      */ 
/*      */   public MouseListener getMouseListener()
/*      */   {
/*  243 */     if (this.mouseListener == null) {
/*  244 */       this.mouseListener = createMouseListener();
/*      */     }
/*  246 */     return this.mouseListener;
/*      */   }
/*      */ 
/*      */   public MouseMotionListener getMouseMotionListener()
/*      */   {
/*  256 */     if (this.mouseMotionListener == null) {
/*  257 */       this.mouseMotionListener = createMouseMotionListener();
/*      */     }
/*  259 */     return this.mouseMotionListener;
/*      */   }
/*      */ 
/*      */   public KeyListener getKeyListener()
/*      */   {
/*  269 */     if (this.keyListener == null) {
/*  270 */       this.keyListener = createKeyListener();
/*      */     }
/*  272 */     return this.keyListener;
/*      */   }
/*      */ 
/*      */   public void uninstallingUI()
/*      */   {
/*  281 */     if (this.propertyChangeListener != null) {
/*  282 */       this.comboBox.removePropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*  284 */     if (this.itemListener != null) {
/*  285 */       this.comboBox.removeItemListener(this.itemListener);
/*      */     }
/*  287 */     uninstallComboBoxModelListeners(this.comboBox.getModel());
/*  288 */     uninstallKeyboardActions();
/*  289 */     uninstallListListeners();
/*      */ 
/*  293 */     this.list.setModel(EmptyListModel);
/*      */   }
/*      */ 
/*      */   protected void uninstallComboBoxModelListeners(ComboBoxModel paramComboBoxModel)
/*      */   {
/*  307 */     if ((paramComboBoxModel != null) && (this.listDataListener != null))
/*  308 */       paramComboBoxModel.removeListDataListener(this.listDataListener);
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/*      */   }
/*      */ 
/*      */   public BasicComboPopup(JComboBox paramJComboBox)
/*      */   {
/*  324 */     setName("ComboPopup.popup");
/*  325 */     this.comboBox = paramJComboBox;
/*      */ 
/*  327 */     setLightWeightPopupEnabled(this.comboBox.isLightWeightPopupEnabled());
/*      */ 
/*  330 */     this.list = createList();
/*  331 */     this.list.setName("ComboBox.list");
/*  332 */     configureList();
/*  333 */     this.scroller = createScroller();
/*  334 */     this.scroller.setName("ComboBox.scrollPane");
/*  335 */     configureScroller();
/*  336 */     configurePopup();
/*      */ 
/*  338 */     installComboBoxListeners();
/*  339 */     installKeyboardActions();
/*      */   }
/*      */ 
/*      */   protected void firePopupMenuWillBecomeVisible()
/*      */   {
/*  346 */     super.firePopupMenuWillBecomeVisible();
/*      */   }
/*      */ 
/*      */   protected void firePopupMenuWillBecomeInvisible()
/*      */   {
/*  352 */     super.firePopupMenuWillBecomeInvisible();
/*  353 */     this.comboBox.firePopupMenuWillBecomeInvisible();
/*      */   }
/*      */ 
/*      */   protected void firePopupMenuCanceled() {
/*  357 */     super.firePopupMenuCanceled();
/*  358 */     this.comboBox.firePopupMenuCanceled();
/*      */   }
/*      */ 
/*      */   protected MouseListener createMouseListener()
/*      */   {
/*  373 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected MouseMotionListener createMouseMotionListener()
/*      */   {
/*  388 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected KeyListener createKeyListener()
/*      */   {
/*  398 */     return null;
/*      */   }
/*      */ 
/*      */   protected ListSelectionListener createListSelectionListener()
/*      */   {
/*  409 */     return null;
/*      */   }
/*      */ 
/*      */   protected ListDataListener createListDataListener()
/*      */   {
/*  420 */     return null;
/*      */   }
/*      */ 
/*      */   protected MouseListener createListMouseListener()
/*      */   {
/*  431 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected MouseMotionListener createListMouseMotionListener()
/*      */   {
/*  442 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/*  453 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ItemListener createItemListener()
/*      */   {
/*  467 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/*  471 */     if (this.handler == null) {
/*  472 */       this.handler = new Handler(null);
/*      */     }
/*  474 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected JList createList()
/*      */   {
/*  485 */     return new JList(this.comboBox.getModel()) {
/*      */       public void processMouseEvent(MouseEvent paramAnonymousMouseEvent) {
/*  487 */         if (BasicGraphicsUtils.isMenuShortcutKeyDown(paramAnonymousMouseEvent))
/*      */         {
/*  490 */           Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  491 */           paramAnonymousMouseEvent = new MouseEvent((Component)paramAnonymousMouseEvent.getSource(), paramAnonymousMouseEvent.getID(), paramAnonymousMouseEvent.getWhen(), paramAnonymousMouseEvent.getModifiers() ^ localToolkit.getMenuShortcutKeyMask(), paramAnonymousMouseEvent.getX(), paramAnonymousMouseEvent.getY(), paramAnonymousMouseEvent.getXOnScreen(), paramAnonymousMouseEvent.getYOnScreen(), paramAnonymousMouseEvent.getClickCount(), paramAnonymousMouseEvent.isPopupTrigger(), 0);
/*      */         }
/*      */ 
/*  499 */         super.processMouseEvent(paramAnonymousMouseEvent);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   protected void configureList()
/*      */   {
/*  512 */     this.list.setFont(this.comboBox.getFont());
/*  513 */     this.list.setForeground(this.comboBox.getForeground());
/*  514 */     this.list.setBackground(this.comboBox.getBackground());
/*  515 */     this.list.setSelectionForeground(UIManager.getColor("ComboBox.selectionForeground"));
/*  516 */     this.list.setSelectionBackground(UIManager.getColor("ComboBox.selectionBackground"));
/*  517 */     this.list.setBorder(null);
/*  518 */     this.list.setCellRenderer(this.comboBox.getRenderer());
/*  519 */     this.list.setFocusable(false);
/*  520 */     this.list.setSelectionMode(0);
/*  521 */     setListSelection(this.comboBox.getSelectedIndex());
/*  522 */     installListListeners();
/*      */   }
/*      */ 
/*      */   protected void installListListeners()
/*      */   {
/*  529 */     if ((this.listMouseListener = createListMouseListener()) != null) {
/*  530 */       this.list.addMouseListener(this.listMouseListener);
/*      */     }
/*  532 */     if ((this.listMouseMotionListener = createListMouseMotionListener()) != null) {
/*  533 */       this.list.addMouseMotionListener(this.listMouseMotionListener);
/*      */     }
/*  535 */     if ((this.listSelectionListener = createListSelectionListener()) != null)
/*  536 */       this.list.addListSelectionListener(this.listSelectionListener);
/*      */   }
/*      */ 
/*      */   void uninstallListListeners()
/*      */   {
/*  541 */     if (this.listMouseListener != null) {
/*  542 */       this.list.removeMouseListener(this.listMouseListener);
/*  543 */       this.listMouseListener = null;
/*      */     }
/*  545 */     if (this.listMouseMotionListener != null) {
/*  546 */       this.list.removeMouseMotionListener(this.listMouseMotionListener);
/*  547 */       this.listMouseMotionListener = null;
/*      */     }
/*  549 */     if (this.listSelectionListener != null) {
/*  550 */       this.list.removeListSelectionListener(this.listSelectionListener);
/*  551 */       this.listSelectionListener = null;
/*      */     }
/*  553 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected JScrollPane createScroller()
/*      */   {
/*  560 */     JScrollPane localJScrollPane = new JScrollPane(this.list, 20, 31);
/*      */ 
/*  563 */     localJScrollPane.setHorizontalScrollBar(null);
/*  564 */     return localJScrollPane;
/*      */   }
/*      */ 
/*      */   protected void configureScroller()
/*      */   {
/*  573 */     this.scroller.setFocusable(false);
/*  574 */     this.scroller.getVerticalScrollBar().setFocusable(false);
/*  575 */     this.scroller.setBorder(null);
/*      */   }
/*      */ 
/*      */   protected void configurePopup()
/*      */   {
/*  583 */     setLayout(new BoxLayout(this, 1));
/*  584 */     setBorderPainted(true);
/*  585 */     setBorder(LIST_BORDER);
/*  586 */     setOpaque(false);
/*  587 */     add(this.scroller);
/*  588 */     setDoubleBuffered(true);
/*  589 */     setFocusable(false);
/*      */   }
/*      */ 
/*      */   protected void installComboBoxListeners()
/*      */   {
/*  596 */     if ((this.propertyChangeListener = createPropertyChangeListener()) != null) {
/*  597 */       this.comboBox.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*  599 */     if ((this.itemListener = createItemListener()) != null) {
/*  600 */       this.comboBox.addItemListener(this.itemListener);
/*      */     }
/*  602 */     installComboBoxModelListeners(this.comboBox.getModel());
/*      */   }
/*      */ 
/*      */   protected void installComboBoxModelListeners(ComboBoxModel paramComboBoxModel)
/*      */   {
/*  614 */     if ((paramComboBoxModel != null) && ((this.listDataListener = createListDataListener()) != null))
/*  615 */       paramComboBoxModel.addListDataListener(this.listDataListener);
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isFocusTraversable()
/*      */   {
/*  996 */     return false;
/*      */   }
/*      */ 
/*      */   protected void startAutoScrolling(int paramInt)
/*      */   {
/* 1010 */     if (this.isAutoScrolling) {
/* 1011 */       this.autoscrollTimer.stop();
/*      */     }
/*      */ 
/* 1014 */     this.isAutoScrolling = true;
/*      */     Object localObject;
/* 1016 */     if (paramInt == 0) {
/* 1017 */       this.scrollDirection = 0;
/* 1018 */       localObject = SwingUtilities.convertPoint(this.scroller, new Point(1, 1), this.list);
/* 1019 */       int i = this.list.locationToIndex((Point)localObject);
/* 1020 */       this.list.setSelectedIndex(i);
/*      */ 
/* 1022 */       this.autoscrollTimer = new Timer(100, new AutoScrollActionHandler(0));
/*      */     }
/* 1025 */     else if (paramInt == 1) {
/* 1026 */       this.scrollDirection = 1;
/* 1027 */       localObject = this.scroller.getSize();
/* 1028 */       Point localPoint = SwingUtilities.convertPoint(this.scroller, new Point(1, ((Dimension)localObject).height - 1 - 2), this.list);
/*      */ 
/* 1031 */       int j = this.list.locationToIndex(localPoint);
/* 1032 */       this.list.setSelectedIndex(j);
/*      */ 
/* 1034 */       this.autoscrollTimer = new Timer(100, new AutoScrollActionHandler(1));
/*      */     }
/*      */ 
/* 1037 */     this.autoscrollTimer.start();
/*      */   }
/*      */ 
/*      */   protected void stopAutoScrolling()
/*      */   {
/* 1045 */     this.isAutoScrolling = false;
/*      */ 
/* 1047 */     if (this.autoscrollTimer != null) {
/* 1048 */       this.autoscrollTimer.stop();
/* 1049 */       this.autoscrollTimer = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void autoScrollUp()
/*      */   {
/* 1058 */     int i = this.list.getSelectedIndex();
/* 1059 */     if (i > 0) {
/* 1060 */       this.list.setSelectedIndex(i - 1);
/* 1061 */       this.list.ensureIndexIsVisible(i - 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void autoScrollDown()
/*      */   {
/* 1070 */     int i = this.list.getSelectedIndex();
/* 1071 */     int j = this.list.getModel().getSize() - 1;
/* 1072 */     if (i < j) {
/* 1073 */       this.list.setSelectedIndex(i + 1);
/* 1074 */       this.list.ensureIndexIsVisible(i + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1095 */     AccessibleContext localAccessibleContext = super.getAccessibleContext();
/* 1096 */     localAccessibleContext.setAccessibleParent(this.comboBox);
/* 1097 */     return localAccessibleContext;
/*      */   }
/*      */ 
/*      */   protected void delegateFocus(MouseEvent paramMouseEvent)
/*      */   {
/* 1108 */     if (this.comboBox.isEditable()) {
/* 1109 */       Component localComponent = this.comboBox.getEditor().getEditorComponent();
/* 1110 */       if ((!(localComponent instanceof JComponent)) || (((JComponent)localComponent).isRequestFocusEnabled())) {
/* 1111 */         localComponent.requestFocus();
/*      */       }
/*      */     }
/* 1114 */     else if (this.comboBox.isRequestFocusEnabled()) {
/* 1115 */       this.comboBox.requestFocus();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void togglePopup()
/*      */   {
/* 1124 */     if (isVisible()) {
/* 1125 */       hide();
/*      */     }
/*      */     else
/* 1128 */       show();
/*      */   }
/*      */ 
/*      */   private void setListSelection(int paramInt)
/*      */   {
/* 1140 */     if (paramInt == -1) {
/* 1141 */       this.list.clearSelection();
/*      */     }
/*      */     else {
/* 1144 */       this.list.setSelectedIndex(paramInt);
/* 1145 */       this.list.ensureIndexIsVisible(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected MouseEvent convertMouseEvent(MouseEvent paramMouseEvent) {
/* 1150 */     Point localPoint = SwingUtilities.convertPoint((Component)paramMouseEvent.getSource(), paramMouseEvent.getPoint(), this.list);
/*      */ 
/* 1152 */     MouseEvent localMouseEvent = new MouseEvent((Component)paramMouseEvent.getSource(), paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*      */ 
/* 1163 */     return localMouseEvent;
/*      */   }
/*      */ 
/*      */   protected int getPopupHeightForRowCount(int paramInt)
/*      */   {
/* 1173 */     int i = Math.min(paramInt, this.comboBox.getItemCount());
/* 1174 */     int j = 0;
/* 1175 */     ListCellRenderer localListCellRenderer = this.list.getCellRenderer();
/* 1176 */     Object localObject1 = null;
/*      */     Object localObject2;
/* 1178 */     for (int k = 0; k < i; k++) {
/* 1179 */       localObject1 = this.list.getModel().getElementAt(k);
/* 1180 */       localObject2 = localListCellRenderer.getListCellRendererComponent(this.list, localObject1, k, false, false);
/* 1181 */       j += ((Component)localObject2).getPreferredSize().height;
/*      */     }
/*      */ 
/* 1184 */     if (j == 0) {
/* 1185 */       j = this.comboBox.getHeight();
/*      */     }
/*      */ 
/* 1188 */     Border localBorder = this.scroller.getViewportBorder();
/* 1189 */     if (localBorder != null) {
/* 1190 */       localObject2 = localBorder.getBorderInsets(null);
/* 1191 */       j += ((Insets)localObject2).top + ((Insets)localObject2).bottom;
/*      */     }
/*      */ 
/* 1194 */     localBorder = this.scroller.getBorder();
/* 1195 */     if (localBorder != null) {
/* 1196 */       localObject2 = localBorder.getBorderInsets(null);
/* 1197 */       j += ((Insets)localObject2).top + ((Insets)localObject2).bottom;
/*      */     }
/*      */ 
/* 1200 */     return j;
/*      */   }
/*      */ 
/*      */   protected Rectangle computePopupBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1216 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*      */ 
/* 1220 */     GraphicsConfiguration localGraphicsConfiguration = this.comboBox.getGraphicsConfiguration();
/* 1221 */     Point localPoint = new Point();
/* 1222 */     SwingUtilities.convertPointFromScreen(localPoint, this.comboBox);
/*      */     Rectangle localRectangle;
/* 1223 */     if (localGraphicsConfiguration != null) {
/* 1224 */       localObject = localToolkit.getScreenInsets(localGraphicsConfiguration);
/* 1225 */       localRectangle = localGraphicsConfiguration.getBounds();
/* 1226 */       localRectangle.width -= ((Insets)localObject).left + ((Insets)localObject).right;
/* 1227 */       localRectangle.height -= ((Insets)localObject).top + ((Insets)localObject).bottom;
/* 1228 */       localRectangle.x += localPoint.x + ((Insets)localObject).left;
/* 1229 */       localRectangle.y += localPoint.y + ((Insets)localObject).top;
/*      */     }
/*      */     else {
/* 1232 */       localRectangle = new Rectangle(localPoint, localToolkit.getScreenSize());
/*      */     }
/*      */ 
/* 1235 */     Object localObject = new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4);
/* 1236 */     if ((paramInt2 + paramInt4 > localRectangle.y + localRectangle.height) && (paramInt4 < localRectangle.height))
/*      */     {
/* 1238 */       ((Rectangle)localObject).y = (-((Rectangle)localObject).height);
/*      */     }
/* 1240 */     return localObject;
/*      */   }
/*      */ 
/*      */   private Point getPopupLocation()
/*      */   {
/* 1247 */     Dimension localDimension1 = this.comboBox.getSize();
/* 1248 */     Insets localInsets = getInsets();
/*      */ 
/* 1252 */     localDimension1.setSize(localDimension1.width - (localInsets.right + localInsets.left), getPopupHeightForRowCount(this.comboBox.getMaximumRowCount()));
/*      */ 
/* 1254 */     Rectangle localRectangle = computePopupBounds(0, this.comboBox.getBounds().height, localDimension1.width, localDimension1.height);
/*      */ 
/* 1256 */     Dimension localDimension2 = localRectangle.getSize();
/* 1257 */     Point localPoint = localRectangle.getLocation();
/*      */ 
/* 1259 */     this.scroller.setMaximumSize(localDimension2);
/* 1260 */     this.scroller.setPreferredSize(localDimension2);
/* 1261 */     this.scroller.setMinimumSize(localDimension2);
/*      */ 
/* 1263 */     this.list.revalidate();
/*      */ 
/* 1265 */     return localPoint;
/*      */   }
/*      */ 
/*      */   protected void updateListBoxSelectionForEvent(MouseEvent paramMouseEvent, boolean paramBoolean)
/*      */   {
/* 1275 */     Point localPoint = paramMouseEvent.getPoint();
/* 1276 */     if (this.list == null)
/* 1277 */       return;
/* 1278 */     int i = this.list.locationToIndex(localPoint);
/* 1279 */     if (i == -1) {
/* 1280 */       if (localPoint.y < 0)
/* 1281 */         i = 0;
/*      */       else
/* 1283 */         i = this.comboBox.getModel().getSize() - 1;
/*      */     }
/* 1285 */     if (this.list.getSelectedIndex() != i) {
/* 1286 */       this.list.setSelectedIndex(i);
/* 1287 */       if (paramBoolean)
/* 1288 */         this.list.ensureIndexIsVisible(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class AutoScrollActionHandler
/*      */     implements ActionListener
/*      */   {
/*      */     private int direction;
/*      */ 
/*      */     AutoScrollActionHandler(int arg2)
/*      */     {
/*      */       int i;
/*  785 */       this.direction = i;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  789 */       if (this.direction == 0) {
/*  790 */         BasicComboPopup.this.autoScrollUp();
/*      */       }
/*      */       else
/*  793 */         BasicComboPopup.this.autoScrollDown();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class EmptyListModelClass
/*      */     implements ListModel, Serializable
/*      */   {
/*      */     public int getSize()
/*      */     {
/*   68 */       return 0; } 
/*   69 */     public Object getElementAt(int paramInt) { return null; }
/*      */ 
/*      */ 
/*      */     public void addListDataListener(ListDataListener paramListDataListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeListDataListener(ListDataListener paramListDataListener)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements ItemListener, MouseListener, MouseMotionListener, PropertyChangeListener, Serializable
/*      */   {
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/*  810 */       if (paramMouseEvent.getSource() == BasicComboPopup.this.list) {
/*  811 */         return;
/*      */       }
/*  813 */       if ((!SwingUtilities.isLeftMouseButton(paramMouseEvent)) || (!BasicComboPopup.this.comboBox.isEnabled())) {
/*  814 */         return;
/*      */       }
/*  816 */       if (BasicComboPopup.this.comboBox.isEditable()) {
/*  817 */         Component localComponent = BasicComboPopup.this.comboBox.getEditor().getEditorComponent();
/*  818 */         if ((!(localComponent instanceof JComponent)) || (((JComponent)localComponent).isRequestFocusEnabled())) {
/*  819 */           localComponent.requestFocus();
/*      */         }
/*      */       }
/*  822 */       else if (BasicComboPopup.this.comboBox.isRequestFocusEnabled()) {
/*  823 */         BasicComboPopup.this.comboBox.requestFocus();
/*      */       }
/*  825 */       BasicComboPopup.this.togglePopup();
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  829 */       if (paramMouseEvent.getSource() == BasicComboPopup.this.list) {
/*  830 */         if (BasicComboPopup.this.list.getModel().getSize() > 0)
/*      */         {
/*  832 */           if (BasicComboPopup.this.comboBox.getSelectedIndex() == BasicComboPopup.this.list.getSelectedIndex()) {
/*  833 */             BasicComboPopup.this.comboBox.getEditor().setItem(BasicComboPopup.this.list.getSelectedValue());
/*      */           }
/*  835 */           BasicComboPopup.this.comboBox.setSelectedIndex(BasicComboPopup.this.list.getSelectedIndex());
/*      */         }
/*  837 */         BasicComboPopup.this.comboBox.setPopupVisible(false);
/*      */ 
/*  839 */         if ((BasicComboPopup.this.comboBox.isEditable()) && (BasicComboPopup.this.comboBox.getEditor() != null)) {
/*  840 */           BasicComboPopup.this.comboBox.configureEditor(BasicComboPopup.this.comboBox.getEditor(), BasicComboPopup.this.comboBox.getSelectedItem());
/*      */         }
/*      */ 
/*  843 */         return;
/*      */       }
/*      */ 
/*  846 */       Component localComponent = (Component)paramMouseEvent.getSource();
/*  847 */       Dimension localDimension = localComponent.getSize();
/*  848 */       Rectangle localRectangle1 = new Rectangle(0, 0, localDimension.width - 1, localDimension.height - 1);
/*  849 */       if (!localRectangle1.contains(paramMouseEvent.getPoint())) {
/*  850 */         MouseEvent localMouseEvent = BasicComboPopup.this.convertMouseEvent(paramMouseEvent);
/*  851 */         Point localPoint = localMouseEvent.getPoint();
/*  852 */         Rectangle localRectangle2 = new Rectangle();
/*  853 */         BasicComboPopup.this.list.computeVisibleRect(localRectangle2);
/*  854 */         if (localRectangle2.contains(localPoint)) {
/*  855 */           if (BasicComboPopup.this.comboBox.getSelectedIndex() == BasicComboPopup.this.list.getSelectedIndex()) {
/*  856 */             BasicComboPopup.this.comboBox.getEditor().setItem(BasicComboPopup.this.list.getSelectedValue());
/*      */           }
/*  858 */           BasicComboPopup.this.comboBox.setSelectedIndex(BasicComboPopup.this.list.getSelectedIndex());
/*      */         }
/*  860 */         BasicComboPopup.this.comboBox.setPopupVisible(false);
/*      */       }
/*  862 */       BasicComboPopup.this.hasEntered = false;
/*  863 */       BasicComboPopup.this.stopAutoScrolling();
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/*  877 */       if (paramMouseEvent.getSource() == BasicComboPopup.this.list) {
/*  878 */         Point localPoint = paramMouseEvent.getPoint();
/*  879 */         Rectangle localRectangle = new Rectangle();
/*  880 */         BasicComboPopup.this.list.computeVisibleRect(localRectangle);
/*  881 */         if (localRectangle.contains(localPoint))
/*  882 */           BasicComboPopup.this.updateListBoxSelectionForEvent(paramMouseEvent, false);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/*  888 */       if (paramMouseEvent.getSource() == BasicComboPopup.this.list) {
/*  889 */         return;
/*      */       }
/*  891 */       if (BasicComboPopup.this.isVisible()) {
/*  892 */         MouseEvent localMouseEvent = BasicComboPopup.this.convertMouseEvent(paramMouseEvent);
/*  893 */         Rectangle localRectangle = new Rectangle();
/*  894 */         BasicComboPopup.this.list.computeVisibleRect(localRectangle);
/*      */ 
/*  896 */         if ((localMouseEvent.getPoint().y >= localRectangle.y) && (localMouseEvent.getPoint().y <= localRectangle.y + localRectangle.height - 1)) {
/*  897 */           BasicComboPopup.this.hasEntered = true;
/*  898 */           if (BasicComboPopup.this.isAutoScrolling) {
/*  899 */             BasicComboPopup.this.stopAutoScrolling();
/*      */           }
/*  901 */           Point localPoint = localMouseEvent.getPoint();
/*  902 */           if (localRectangle.contains(localPoint)) {
/*  903 */             BasicComboPopup.this.updateListBoxSelectionForEvent(localMouseEvent, false);
/*      */           }
/*      */ 
/*      */         }
/*  907 */         else if (BasicComboPopup.this.hasEntered) {
/*  908 */           int i = localMouseEvent.getPoint().y < localRectangle.y ? 0 : 1;
/*  909 */           if ((BasicComboPopup.this.isAutoScrolling) && (BasicComboPopup.this.scrollDirection != i)) {
/*  910 */             BasicComboPopup.this.stopAutoScrolling();
/*  911 */             BasicComboPopup.this.startAutoScrolling(i);
/*      */           }
/*  913 */           else if (!BasicComboPopup.this.isAutoScrolling) {
/*  914 */             BasicComboPopup.this.startAutoScrolling(i);
/*      */           }
/*      */ 
/*      */         }
/*  918 */         else if (paramMouseEvent.getPoint().y < 0) {
/*  919 */           BasicComboPopup.this.hasEntered = true;
/*  920 */           BasicComboPopup.this.startAutoScrolling(0);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  931 */       JComboBox localJComboBox = (JComboBox)paramPropertyChangeEvent.getSource();
/*  932 */       String str = paramPropertyChangeEvent.getPropertyName();
/*      */       Object localObject1;
/*      */       Object localObject2;
/*  934 */       if (str == "model") {
/*  935 */         localObject1 = (ComboBoxModel)paramPropertyChangeEvent.getOldValue();
/*  936 */         localObject2 = (ComboBoxModel)paramPropertyChangeEvent.getNewValue();
/*  937 */         BasicComboPopup.this.uninstallComboBoxModelListeners((ComboBoxModel)localObject1);
/*  938 */         BasicComboPopup.this.installComboBoxModelListeners((ComboBoxModel)localObject2);
/*      */ 
/*  940 */         BasicComboPopup.this.list.setModel((ListModel)localObject2);
/*      */ 
/*  942 */         if (BasicComboPopup.this.isVisible()) {
/*  943 */           BasicComboPopup.this.hide();
/*      */         }
/*      */       }
/*  946 */       else if (str == "renderer") {
/*  947 */         BasicComboPopup.this.list.setCellRenderer(localJComboBox.getRenderer());
/*  948 */         if (BasicComboPopup.this.isVisible()) {
/*  949 */           BasicComboPopup.this.hide();
/*      */         }
/*      */       }
/*  952 */       else if (str == "componentOrientation")
/*      */       {
/*  956 */         localObject1 = (ComponentOrientation)paramPropertyChangeEvent.getNewValue();
/*      */ 
/*  958 */         localObject2 = BasicComboPopup.this.getList();
/*  959 */         if ((localObject2 != null) && (((JList)localObject2).getComponentOrientation() != localObject1)) {
/*  960 */           ((JList)localObject2).setComponentOrientation((ComponentOrientation)localObject1);
/*      */         }
/*      */ 
/*  963 */         if ((BasicComboPopup.this.scroller != null) && (BasicComboPopup.this.scroller.getComponentOrientation() != localObject1)) {
/*  964 */           BasicComboPopup.this.scroller.setComponentOrientation((ComponentOrientation)localObject1);
/*      */         }
/*      */ 
/*  967 */         if (localObject1 != BasicComboPopup.this.getComponentOrientation()) {
/*  968 */           BasicComboPopup.this.setComponentOrientation((ComponentOrientation)localObject1);
/*      */         }
/*      */       }
/*  971 */       else if (str == "lightWeightPopupEnabled") {
/*  972 */         BasicComboPopup.this.setLightWeightPopupEnabled(localJComboBox.isLightWeightPopupEnabled());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void itemStateChanged(ItemEvent paramItemEvent)
/*      */     {
/*  980 */       if (paramItemEvent.getStateChange() == 1) {
/*  981 */         JComboBox localJComboBox = (JComboBox)paramItemEvent.getSource();
/*  982 */         BasicComboPopup.this.setListSelection(localJComboBox.getSelectedIndex());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class InvocationKeyHandler extends KeyAdapter
/*      */   {
/*      */     public InvocationKeyHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void keyReleased(KeyEvent paramKeyEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class InvocationMouseHandler extends MouseAdapter
/*      */   {
/*      */     protected InvocationMouseHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/*  670 */       BasicComboPopup.this.getHandler().mousePressed(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/*  680 */       BasicComboPopup.this.getHandler().mouseReleased(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class InvocationMouseMotionHandler extends MouseMotionAdapter
/*      */   {
/*      */     protected InvocationMouseMotionHandler() {
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/*  690 */       BasicComboPopup.this.getHandler().mouseDragged(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ItemHandler
/*      */     implements ItemListener
/*      */   {
/*      */     protected ItemHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void itemStateChanged(ItemEvent paramItemEvent)
/*      */     {
/*  760 */       BasicComboPopup.this.getHandler().itemStateChanged(paramItemEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ListDataHandler
/*      */     implements ListDataListener
/*      */   {
/*      */     public ListDataHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void contentsChanged(ListDataEvent paramListDataEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void intervalAdded(ListDataEvent paramListDataEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void intervalRemoved(ListDataEvent paramListDataEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ListMouseHandler extends MouseAdapter
/*      */   {
/*      */     protected ListMouseHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/*  740 */       BasicComboPopup.this.getHandler().mouseReleased(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ListMouseMotionHandler extends MouseMotionAdapter
/*      */   {
/*      */     protected ListMouseMotionHandler() {
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/*  750 */       BasicComboPopup.this.getHandler().mouseMoved(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ListSelectionHandler
/*      */     implements ListSelectionListener
/*      */   {
/*      */     protected ListSelectionHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class PropertyChangeHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     protected PropertyChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  776 */       BasicComboPopup.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicComboPopup
 * JD-Core Version:    0.6.2
 */