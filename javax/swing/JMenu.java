/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GraphicsDevice;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.event.MenuEvent;
/*      */ import javax.swing.event.MenuListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.MenuItemUI;
/*      */ import javax.swing.plaf.PopupMenuUI;
/*      */ 
/*      */ public class JMenu extends JMenuItem
/*      */   implements Accessible, MenuElement
/*      */ {
/*      */   private static final String uiClassID = "MenuUI";
/*      */   private JPopupMenu popupMenu;
/*  128 */   private ChangeListener menuChangeListener = null;
/*      */ 
/*  135 */   private MenuEvent menuEvent = null;
/*      */ 
/*  142 */   private static Hashtable listenerRegistry = null;
/*      */   private int delay;
/*  154 */   private Point customMenuLocation = null;
/*      */   private static final boolean TRACE = false;
/*      */   private static final boolean VERBOSE = false;
/*      */   private static final boolean DEBUG = false;
/*      */   protected WinListener popupListener;
/*      */ 
/*      */   public JMenu()
/*      */   {
/*  165 */     this("");
/*      */   }
/*      */ 
/*      */   public JMenu(String paramString)
/*      */   {
/*  175 */     super(paramString);
/*      */   }
/*      */ 
/*      */   public JMenu(Action paramAction)
/*      */   {
/*  186 */     this();
/*  187 */     setAction(paramAction);
/*      */   }
/*      */ 
/*      */   public JMenu(String paramString, boolean paramBoolean)
/*      */   {
/*  198 */     this(paramString);
/*      */   }
/*      */ 
/*      */   void initFocusability()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  223 */     setUI((MenuItemUI)UIManager.getUI(this));
/*      */ 
/*  225 */     if (this.popupMenu != null)
/*      */     {
/*  227 */       this.popupMenu.setUI((PopupMenuUI)UIManager.getUI(this.popupMenu));
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  241 */     return "MenuUI";
/*      */   }
/*      */ 
/*      */   public void setModel(ButtonModel paramButtonModel)
/*      */   {
/*  262 */     ButtonModel localButtonModel = getModel();
/*      */ 
/*  264 */     super.setModel(paramButtonModel);
/*      */ 
/*  266 */     if ((localButtonModel != null) && (this.menuChangeListener != null)) {
/*  267 */       localButtonModel.removeChangeListener(this.menuChangeListener);
/*  268 */       this.menuChangeListener = null;
/*      */     }
/*      */ 
/*  271 */     this.model = paramButtonModel;
/*      */ 
/*  273 */     if (paramButtonModel != null) {
/*  274 */       this.menuChangeListener = createMenuChangeListener();
/*  275 */       paramButtonModel.addChangeListener(this.menuChangeListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isSelected()
/*      */   {
/*  285 */     return getModel().isSelected();
/*      */   }
/*      */ 
/*      */   public void setSelected(boolean paramBoolean)
/*      */   {
/*  299 */     ButtonModel localButtonModel = getModel();
/*  300 */     boolean bool = localButtonModel.isSelected();
/*      */ 
/*  310 */     if (paramBoolean != localButtonModel.isSelected())
/*  311 */       getModel().setSelected(paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean isPopupMenuVisible()
/*      */   {
/*  321 */     ensurePopupMenuCreated();
/*  322 */     return this.popupMenu.isVisible();
/*      */   }
/*      */ 
/*      */   public void setPopupMenuVisible(boolean paramBoolean)
/*      */   {
/*  342 */     boolean bool = isPopupMenuVisible();
/*  343 */     if ((paramBoolean != bool) && ((isEnabled()) || (!paramBoolean))) {
/*  344 */       ensurePopupMenuCreated();
/*  345 */       if ((paramBoolean == true) && (isShowing()))
/*      */       {
/*  347 */         Point localPoint = getCustomMenuLocation();
/*  348 */         if (localPoint == null) {
/*  349 */           localPoint = getPopupMenuOrigin();
/*      */         }
/*  351 */         getPopupMenu().show(this, localPoint.x, localPoint.y);
/*      */       } else {
/*  353 */         getPopupMenu().setVisible(false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Point getPopupMenuOrigin()
/*      */   {
/*  376 */     JPopupMenu localJPopupMenu = getPopupMenu();
/*      */ 
/*  378 */     Dimension localDimension1 = getSize();
/*  379 */     Dimension localDimension2 = localJPopupMenu.getSize();
/*      */ 
/*  382 */     if (localDimension2.width == 0) {
/*  383 */       localDimension2 = localJPopupMenu.getPreferredSize();
/*      */     }
/*  385 */     Point localPoint = getLocationOnScreen();
/*  386 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  387 */     Object localObject1 = getGraphicsConfiguration();
/*  388 */     Rectangle localRectangle = new Rectangle(localToolkit.getScreenSize());
/*  389 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*      */ 
/*  391 */     GraphicsDevice[] arrayOfGraphicsDevice = localGraphicsEnvironment.getScreenDevices();
/*  392 */     for (int k = 0; k < arrayOfGraphicsDevice.length; k++) {
/*  393 */       if (arrayOfGraphicsDevice[k].getType() == 0) {
/*  394 */         GraphicsConfiguration localGraphicsConfiguration = arrayOfGraphicsDevice[k].getDefaultConfiguration();
/*      */ 
/*  396 */         if (localGraphicsConfiguration.getBounds().contains(localPoint)) {
/*  397 */           localObject1 = localGraphicsConfiguration;
/*  398 */           break;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  404 */     if (localObject1 != null) {
/*  405 */       localRectangle = ((GraphicsConfiguration)localObject1).getBounds();
/*      */ 
/*  407 */       localObject2 = localToolkit.getScreenInsets((GraphicsConfiguration)localObject1);
/*      */ 
/*  409 */       localRectangle.width -= Math.abs(((Insets)localObject2).left + ((Insets)localObject2).right);
/*      */ 
/*  411 */       localRectangle.height -= Math.abs(((Insets)localObject2).top + ((Insets)localObject2).bottom);
/*      */ 
/*  413 */       localPoint.x -= Math.abs(((Insets)localObject2).left);
/*  414 */       localPoint.y -= Math.abs(((Insets)localObject2).top);
/*      */     }
/*      */ 
/*  417 */     Object localObject2 = getParent();
/*      */     int m;
/*      */     int n;
/*      */     int i;
/*      */     int j;
/*  418 */     if ((localObject2 instanceof JPopupMenu))
/*      */     {
/*  420 */       m = UIManager.getInt("Menu.submenuPopupOffsetX");
/*  421 */       n = UIManager.getInt("Menu.submenuPopupOffsetY");
/*      */ 
/*  423 */       if (SwingUtilities.isLeftToRight(this))
/*      */       {
/*  425 */         i = localDimension1.width + m;
/*  426 */         if ((localPoint.x + i + localDimension2.width >= localRectangle.width + localRectangle.x) && (localRectangle.width - localDimension1.width < 2 * (localPoint.x - localRectangle.x)))
/*      */         {
/*  432 */           i = 0 - m - localDimension2.width;
/*      */         }
/*      */       }
/*      */       else {
/*  436 */         i = 0 - m - localDimension2.width;
/*  437 */         if ((localPoint.x + i < localRectangle.x) && (localRectangle.width - localDimension1.width > 2 * (localPoint.x - localRectangle.x)))
/*      */         {
/*  442 */           i = localDimension1.width + m;
/*      */         }
/*      */       }
/*      */ 
/*  446 */       j = n;
/*  447 */       if ((localPoint.y + j + localDimension2.height >= localRectangle.height + localRectangle.y) && (localRectangle.height - localDimension1.height < 2 * (localPoint.y - localRectangle.y)))
/*      */       {
/*  453 */         j = localDimension1.height - n - localDimension2.height;
/*      */       }
/*      */     }
/*      */     else {
/*  457 */       m = UIManager.getInt("Menu.menuPopupOffsetX");
/*  458 */       n = UIManager.getInt("Menu.menuPopupOffsetY");
/*      */ 
/*  460 */       if (SwingUtilities.isLeftToRight(this))
/*      */       {
/*  462 */         i = m;
/*  463 */         if ((localPoint.x + i + localDimension2.width >= localRectangle.width + localRectangle.x) && (localRectangle.width - localDimension1.width < 2 * (localPoint.x - localRectangle.x)))
/*      */         {
/*  469 */           i = localDimension1.width - m - localDimension2.width;
/*      */         }
/*      */       }
/*      */       else {
/*  473 */         i = localDimension1.width - m - localDimension2.width;
/*  474 */         if ((localPoint.x + i < localRectangle.x) && (localRectangle.width - localDimension1.width > 2 * (localPoint.x - localRectangle.x)))
/*      */         {
/*  479 */           i = m;
/*      */         }
/*      */       }
/*      */ 
/*  483 */       j = localDimension1.height + n;
/*  484 */       if ((localPoint.y + j + localDimension2.height >= localRectangle.height) && (localRectangle.height - localDimension1.height < 2 * (localPoint.y - localRectangle.y)))
/*      */       {
/*  489 */         j = 0 - n - localDimension2.height;
/*      */       }
/*      */     }
/*  492 */     return new Point(i, j);
/*      */   }
/*      */ 
/*      */   public int getDelay()
/*      */   {
/*  510 */     return this.delay;
/*      */   }
/*      */ 
/*      */   public void setDelay(int paramInt)
/*      */   {
/*  529 */     if (paramInt < 0) {
/*  530 */       throw new IllegalArgumentException("Delay must be a positive integer");
/*      */     }
/*  532 */     this.delay = paramInt;
/*      */   }
/*      */ 
/*      */   private void ensurePopupMenuCreated()
/*      */   {
/*  543 */     if (this.popupMenu == null) {
/*  544 */       JMenu localJMenu = this;
/*  545 */       this.popupMenu = new JPopupMenu();
/*  546 */       this.popupMenu.setInvoker(this);
/*  547 */       this.popupListener = createWinListener(this.popupMenu);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Point getCustomMenuLocation()
/*      */   {
/*  555 */     return this.customMenuLocation;
/*      */   }
/*      */ 
/*      */   public void setMenuLocation(int paramInt1, int paramInt2)
/*      */   {
/*  565 */     this.customMenuLocation = new Point(paramInt1, paramInt2);
/*  566 */     if (this.popupMenu != null)
/*  567 */       this.popupMenu.setLocation(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public JMenuItem add(JMenuItem paramJMenuItem)
/*      */   {
/*  578 */     ensurePopupMenuCreated();
/*  579 */     return this.popupMenu.add(paramJMenuItem);
/*      */   }
/*      */ 
/*      */   public Component add(Component paramComponent)
/*      */   {
/*  590 */     ensurePopupMenuCreated();
/*  591 */     this.popupMenu.add(paramComponent);
/*  592 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public Component add(Component paramComponent, int paramInt)
/*      */   {
/*  606 */     ensurePopupMenuCreated();
/*  607 */     this.popupMenu.add(paramComponent, paramInt);
/*  608 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public JMenuItem add(String paramString)
/*      */   {
/*  618 */     return add(new JMenuItem(paramString));
/*      */   }
/*      */ 
/*      */   public JMenuItem add(Action paramAction)
/*      */   {
/*  629 */     JMenuItem localJMenuItem = createActionComponent(paramAction);
/*  630 */     localJMenuItem.setAction(paramAction);
/*  631 */     add(localJMenuItem);
/*  632 */     return localJMenuItem;
/*      */   }
/*      */ 
/*      */   protected JMenuItem createActionComponent(Action paramAction)
/*      */   {
/*  646 */     JMenuItem local1 = new JMenuItem() {
/*      */       protected PropertyChangeListener createActionPropertyChangeListener(Action paramAnonymousAction) {
/*  648 */         PropertyChangeListener localPropertyChangeListener = JMenu.this.createActionChangeListener(this);
/*  649 */         if (localPropertyChangeListener == null) {
/*  650 */           localPropertyChangeListener = super.createActionPropertyChangeListener(paramAnonymousAction);
/*      */         }
/*  652 */         return localPropertyChangeListener;
/*      */       }
/*      */     };
/*  655 */     local1.setHorizontalTextPosition(11);
/*  656 */     local1.setVerticalTextPosition(0);
/*  657 */     return local1;
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createActionChangeListener(JMenuItem paramJMenuItem)
/*      */   {
/*  665 */     return paramJMenuItem.createActionPropertyChangeListener0(paramJMenuItem.getAction());
/*      */   }
/*      */ 
/*      */   public void addSeparator()
/*      */   {
/*  673 */     ensurePopupMenuCreated();
/*  674 */     this.popupMenu.addSeparator();
/*      */   }
/*      */ 
/*      */   public void insert(String paramString, int paramInt)
/*      */   {
/*  688 */     if (paramInt < 0) {
/*  689 */       throw new IllegalArgumentException("index less than zero.");
/*      */     }
/*      */ 
/*  692 */     ensurePopupMenuCreated();
/*  693 */     this.popupMenu.insert(new JMenuItem(paramString), paramInt);
/*      */   }
/*      */ 
/*      */   public JMenuItem insert(JMenuItem paramJMenuItem, int paramInt)
/*      */   {
/*  707 */     if (paramInt < 0) {
/*  708 */       throw new IllegalArgumentException("index less than zero.");
/*      */     }
/*  710 */     ensurePopupMenuCreated();
/*  711 */     this.popupMenu.insert(paramJMenuItem, paramInt);
/*  712 */     return paramJMenuItem;
/*      */   }
/*      */ 
/*      */   public JMenuItem insert(Action paramAction, int paramInt)
/*      */   {
/*  726 */     if (paramInt < 0) {
/*  727 */       throw new IllegalArgumentException("index less than zero.");
/*      */     }
/*      */ 
/*  730 */     ensurePopupMenuCreated();
/*  731 */     JMenuItem localJMenuItem = new JMenuItem(paramAction);
/*  732 */     localJMenuItem.setHorizontalTextPosition(11);
/*  733 */     localJMenuItem.setVerticalTextPosition(0);
/*  734 */     this.popupMenu.insert(localJMenuItem, paramInt);
/*  735 */     return localJMenuItem;
/*      */   }
/*      */ 
/*      */   public void insertSeparator(int paramInt)
/*      */   {
/*  747 */     if (paramInt < 0) {
/*  748 */       throw new IllegalArgumentException("index less than zero.");
/*      */     }
/*      */ 
/*  751 */     ensurePopupMenuCreated();
/*  752 */     this.popupMenu.insert(new JPopupMenu.Separator(), paramInt);
/*      */   }
/*      */ 
/*      */   public JMenuItem getItem(int paramInt)
/*      */   {
/*  768 */     if (paramInt < 0) {
/*  769 */       throw new IllegalArgumentException("index less than zero.");
/*      */     }
/*      */ 
/*  772 */     Component localComponent = getMenuComponent(paramInt);
/*  773 */     if ((localComponent instanceof JMenuItem)) {
/*  774 */       JMenuItem localJMenuItem = (JMenuItem)localComponent;
/*  775 */       return localJMenuItem;
/*      */     }
/*      */ 
/*  779 */     return null;
/*      */   }
/*      */ 
/*      */   public int getItemCount()
/*      */   {
/*  790 */     return getMenuComponentCount();
/*      */   }
/*      */ 
/*      */   public boolean isTearOff()
/*      */   {
/*  801 */     throw new Error("boolean isTearOff() {} not yet implemented");
/*      */   }
/*      */ 
/*      */   public void remove(JMenuItem paramJMenuItem)
/*      */   {
/*  811 */     if (this.popupMenu != null)
/*  812 */       this.popupMenu.remove(paramJMenuItem);
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt)
/*      */   {
/*  824 */     if (paramInt < 0) {
/*  825 */       throw new IllegalArgumentException("index less than zero.");
/*      */     }
/*  827 */     if (paramInt > getItemCount()) {
/*  828 */       throw new IllegalArgumentException("index greater than the number of items.");
/*      */     }
/*  830 */     if (this.popupMenu != null)
/*  831 */       this.popupMenu.remove(paramInt);
/*      */   }
/*      */ 
/*      */   public void remove(Component paramComponent)
/*      */   {
/*  840 */     if (this.popupMenu != null)
/*  841 */       this.popupMenu.remove(paramComponent);
/*      */   }
/*      */ 
/*      */   public void removeAll()
/*      */   {
/*  848 */     if (this.popupMenu != null)
/*  849 */       this.popupMenu.removeAll();
/*      */   }
/*      */ 
/*      */   public int getMenuComponentCount()
/*      */   {
/*  858 */     int i = 0;
/*  859 */     if (this.popupMenu != null)
/*  860 */       i = this.popupMenu.getComponentCount();
/*  861 */     return i;
/*      */   }
/*      */ 
/*      */   public Component getMenuComponent(int paramInt)
/*      */   {
/*  873 */     if (this.popupMenu != null) {
/*  874 */       return this.popupMenu.getComponent(paramInt);
/*      */     }
/*  876 */     return null;
/*      */   }
/*      */ 
/*      */   public Component[] getMenuComponents()
/*      */   {
/*  888 */     if (this.popupMenu != null) {
/*  889 */       return this.popupMenu.getComponents();
/*      */     }
/*  891 */     return new Component[0];
/*      */   }
/*      */ 
/*      */   public boolean isTopLevelMenu()
/*      */   {
/*  903 */     return getParent() instanceof JMenuBar;
/*      */   }
/*      */ 
/*      */   public boolean isMenuComponent(Component paramComponent)
/*      */   {
/*  916 */     if (paramComponent == this) {
/*  917 */       return true;
/*      */     }
/*  919 */     if ((paramComponent instanceof JPopupMenu)) {
/*  920 */       JPopupMenu localJPopupMenu = (JPopupMenu)paramComponent;
/*  921 */       if (localJPopupMenu == getPopupMenu()) {
/*  922 */         return true;
/*      */       }
/*      */     }
/*  925 */     int i = getMenuComponentCount();
/*  926 */     Component[] arrayOfComponent = getMenuComponents();
/*  927 */     for (int j = 0; j < i; j++) {
/*  928 */       Component localComponent = arrayOfComponent[j];
/*      */ 
/*  930 */       if (localComponent == paramComponent) {
/*  931 */         return true;
/*      */       }
/*      */ 
/*  935 */       if ((localComponent instanceof JMenu)) {
/*  936 */         JMenu localJMenu = (JMenu)localComponent;
/*  937 */         if (localJMenu.isMenuComponent(paramComponent))
/*  938 */           return true;
/*      */       }
/*      */     }
/*  941 */     return false;
/*      */   }
/*      */ 
/*      */   private Point translateToPopupMenu(Point paramPoint)
/*      */   {
/*  954 */     return translateToPopupMenu(paramPoint.x, paramPoint.y);
/*      */   }
/*      */ 
/*      */   private Point translateToPopupMenu(int paramInt1, int paramInt2)
/*      */   {
/*      */     int i;
/*      */     int j;
/*  969 */     if ((getParent() instanceof JPopupMenu)) {
/*  970 */       i = paramInt1 - getSize().width;
/*  971 */       j = paramInt2;
/*      */     } else {
/*  973 */       i = paramInt1;
/*  974 */       j = paramInt2 - getSize().height;
/*      */     }
/*      */ 
/*  977 */     return new Point(i, j);
/*      */   }
/*      */ 
/*      */   public JPopupMenu getPopupMenu()
/*      */   {
/*  985 */     ensurePopupMenuCreated();
/*  986 */     return this.popupMenu;
/*      */   }
/*      */ 
/*      */   public void addMenuListener(MenuListener paramMenuListener)
/*      */   {
/*  995 */     this.listenerList.add(MenuListener.class, paramMenuListener);
/*      */   }
/*      */ 
/*      */   public void removeMenuListener(MenuListener paramMenuListener)
/*      */   {
/* 1004 */     this.listenerList.remove(MenuListener.class, paramMenuListener);
/*      */   }
/*      */ 
/*      */   public MenuListener[] getMenuListeners()
/*      */   {
/* 1016 */     return (MenuListener[])this.listenerList.getListeners(MenuListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireMenuSelected()
/*      */   {
/* 1032 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/* 1035 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1036 */       if (arrayOfObject[i] == MenuListener.class) {
/* 1037 */         if (arrayOfObject[(i + 1)] == null) {
/* 1038 */           throw new Error(getText() + " has a NULL Listener!! " + i);
/*      */         }
/*      */ 
/* 1041 */         if (this.menuEvent == null)
/* 1042 */           this.menuEvent = new MenuEvent(this);
/* 1043 */         ((MenuListener)arrayOfObject[(i + 1)]).menuSelected(this.menuEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void fireMenuDeselected()
/*      */   {
/* 1062 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/* 1065 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1066 */       if (arrayOfObject[i] == MenuListener.class) {
/* 1067 */         if (arrayOfObject[(i + 1)] == null) {
/* 1068 */           throw new Error(getText() + " has a NULL Listener!! " + i);
/*      */         }
/*      */ 
/* 1071 */         if (this.menuEvent == null)
/* 1072 */           this.menuEvent = new MenuEvent(this);
/* 1073 */         ((MenuListener)arrayOfObject[(i + 1)]).menuDeselected(this.menuEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void fireMenuCanceled()
/*      */   {
/* 1092 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/* 1095 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1096 */       if (arrayOfObject[i] == MenuListener.class) {
/* 1097 */         if (arrayOfObject[(i + 1)] == null) {
/* 1098 */           throw new Error(getText() + " has a NULL Listener!! " + i);
/*      */         }
/*      */ 
/* 1102 */         if (this.menuEvent == null)
/* 1103 */           this.menuEvent = new MenuEvent(this);
/* 1104 */         ((MenuListener)arrayOfObject[(i + 1)]).menuCanceled(this.menuEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   void configureAcceleratorFromAction(Action paramAction)
/*      */   {
/*      */   }
/*      */ 
/*      */   private ChangeListener createMenuChangeListener()
/*      */   {
/* 1132 */     return new MenuChangeListener();
/*      */   }
/*      */ 
/*      */   protected WinListener createWinListener(JPopupMenu paramJPopupMenu)
/*      */   {
/* 1145 */     return new WinListener(paramJPopupMenu);
/*      */   }
/*      */ 
/*      */   public void menuSelectionChanged(boolean paramBoolean)
/*      */   {
/* 1190 */     setSelected(paramBoolean);
/*      */   }
/*      */ 
/*      */   public MenuElement[] getSubElements()
/*      */   {
/* 1204 */     if (this.popupMenu == null) {
/* 1205 */       return new MenuElement[0];
/*      */     }
/* 1207 */     MenuElement[] arrayOfMenuElement = new MenuElement[1];
/* 1208 */     arrayOfMenuElement[0] = this.popupMenu;
/* 1209 */     return arrayOfMenuElement;
/*      */   }
/*      */ 
/*      */   public Component getComponent()
/*      */   {
/* 1222 */     return this;
/*      */   }
/*      */ 
/*      */   public void applyComponentOrientation(ComponentOrientation paramComponentOrientation)
/*      */   {
/* 1239 */     super.applyComponentOrientation(paramComponentOrientation);
/*      */ 
/* 1241 */     if (this.popupMenu != null) {
/* 1242 */       int i = getMenuComponentCount();
/* 1243 */       for (int j = 0; j < i; j++) {
/* 1244 */         getMenuComponent(j).applyComponentOrientation(paramComponentOrientation);
/*      */       }
/* 1246 */       this.popupMenu.setComponentOrientation(paramComponentOrientation);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setComponentOrientation(ComponentOrientation paramComponentOrientation) {
/* 1251 */     super.setComponentOrientation(paramComponentOrientation);
/* 1252 */     if (this.popupMenu != null)
/* 1253 */       this.popupMenu.setComponentOrientation(paramComponentOrientation);
/*      */   }
/*      */ 
/*      */   public void setAccelerator(KeyStroke paramKeyStroke)
/*      */   {
/* 1272 */     throw new Error("setAccelerator() is not defined for JMenu.  Use setMnemonic() instead.");
/*      */   }
/*      */ 
/*      */   protected void processKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/* 1281 */     MenuSelectionManager.defaultManager().processKeyEvent(paramKeyEvent);
/* 1282 */     if (paramKeyEvent.isConsumed()) {
/* 1283 */       return;
/*      */     }
/* 1285 */     super.processKeyEvent(paramKeyEvent);
/*      */   }
/*      */ 
/*      */   public void doClick(int paramInt)
/*      */   {
/* 1295 */     MenuElement[] arrayOfMenuElement = buildMenuElementArray(this);
/* 1296 */     MenuSelectionManager.defaultManager().setSelectedPath(arrayOfMenuElement);
/*      */   }
/*      */ 
/*      */   private MenuElement[] buildMenuElementArray(JMenu paramJMenu)
/*      */   {
/* 1306 */     Vector localVector = new Vector();
/* 1307 */     Object localObject = paramJMenu.getPopupMenu();
/*      */     do
/*      */     {
/*      */       while (true)
/*      */       {
/* 1313 */         if ((localObject instanceof JPopupMenu)) {
/* 1314 */           JPopupMenu localJPopupMenu = (JPopupMenu)localObject;
/* 1315 */           localVector.insertElementAt(localJPopupMenu, 0);
/* 1316 */           localObject = localJPopupMenu.getInvoker(); } else {
/* 1317 */           if (!(localObject instanceof JMenu)) break;
/* 1318 */           JMenu localJMenu = (JMenu)localObject;
/* 1319 */           localVector.insertElementAt(localJMenu, 0);
/* 1320 */           localObject = localJMenu.getParent(); }  } 
/* 1321 */     }while (!(localObject instanceof JMenuBar));
/* 1322 */     JMenuBar localJMenuBar = (JMenuBar)localObject;
/* 1323 */     localVector.insertElementAt(localJMenuBar, 0);
/* 1324 */     MenuElement[] arrayOfMenuElement = new MenuElement[localVector.size()];
/* 1325 */     localVector.copyInto(arrayOfMenuElement);
/* 1326 */     return arrayOfMenuElement;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1338 */     paramObjectOutputStream.defaultWriteObject();
/* 1339 */     if (getUIClassID().equals("MenuUI")) {
/* 1340 */       byte b = JComponent.getWriteObjCounter(this);
/* 1341 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 1342 */       if ((b == 0) && (this.ui != null))
/* 1343 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1359 */     return super.paramString();
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1377 */     if (this.accessibleContext == null) {
/* 1378 */       this.accessibleContext = new AccessibleJMenu();
/*      */     }
/* 1380 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJMenu extends JMenuItem.AccessibleJMenuItem
/*      */     implements AccessibleSelection
/*      */   {
/*      */     protected AccessibleJMenu()
/*      */     {
/* 1397 */       super();
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 1408 */       Component[] arrayOfComponent1 = JMenu.this.getMenuComponents();
/* 1409 */       int i = 0;
/* 1410 */       for (Component localComponent : arrayOfComponent1) {
/* 1411 */         if ((localComponent instanceof Accessible)) {
/* 1412 */           i++;
/*      */         }
/*      */       }
/* 1415 */       return i;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 1425 */       Component[] arrayOfComponent1 = JMenu.this.getMenuComponents();
/* 1426 */       int i = 0;
/* 1427 */       for (Component localComponent : arrayOfComponent1) {
/* 1428 */         if ((localComponent instanceof Accessible)) {
/* 1429 */           if (i == paramInt) {
/* 1430 */             if ((localComponent instanceof JComponent))
/*      */             {
/* 1435 */               AccessibleContext localAccessibleContext = localComponent.getAccessibleContext();
/* 1436 */               localAccessibleContext.setAccessibleParent(JMenu.this);
/*      */             }
/* 1438 */             return (Accessible)localComponent;
/*      */           }
/* 1440 */           i++;
/*      */         }
/*      */       }
/*      */ 
/* 1444 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1455 */       return AccessibleRole.MENU;
/*      */     }
/*      */ 
/*      */     public AccessibleSelection getAccessibleSelection()
/*      */     {
/* 1467 */       return this;
/*      */     }
/*      */ 
/*      */     public int getAccessibleSelectionCount()
/*      */     {
/* 1476 */       MenuElement[] arrayOfMenuElement = MenuSelectionManager.defaultManager().getSelectedPath();
/*      */ 
/* 1478 */       if (arrayOfMenuElement != null) {
/* 1479 */         for (int i = 0; i < arrayOfMenuElement.length; i++) {
/* 1480 */           if ((arrayOfMenuElement[i] == JMenu.this) && 
/* 1481 */             (i + 1 < arrayOfMenuElement.length)) {
/* 1482 */             return 1;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1487 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleSelection(int paramInt)
/*      */     {
/* 1498 */       if ((paramInt < 0) || (paramInt >= JMenu.this.getItemCount())) {
/* 1499 */         return null;
/*      */       }
/* 1501 */       MenuElement[] arrayOfMenuElement = MenuSelectionManager.defaultManager().getSelectedPath();
/*      */ 
/* 1503 */       if (arrayOfMenuElement != null) {
/* 1504 */         for (int i = 0; i < arrayOfMenuElement.length; i++) {
/* 1505 */           if (arrayOfMenuElement[i] == JMenu.this)
/*      */           {
/*      */             do {
/* 1508 */               i++; if (i >= arrayOfMenuElement.length) break; 
/* 1509 */             }while (!(arrayOfMenuElement[i] instanceof JMenuItem));
/* 1510 */             return (Accessible)arrayOfMenuElement[i];
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1516 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleChildSelected(int paramInt)
/*      */     {
/* 1529 */       MenuElement[] arrayOfMenuElement = MenuSelectionManager.defaultManager().getSelectedPath();
/*      */ 
/* 1531 */       if (arrayOfMenuElement != null) {
/* 1532 */         JMenuItem localJMenuItem = JMenu.this.getItem(paramInt);
/* 1533 */         for (int i = 0; i < arrayOfMenuElement.length; i++) {
/* 1534 */           if (arrayOfMenuElement[i] == localJMenuItem) {
/* 1535 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 1539 */       return false;
/*      */     }
/*      */ 
/*      */     public void addAccessibleSelection(int paramInt)
/*      */     {
/* 1555 */       if ((paramInt < 0) || (paramInt >= JMenu.this.getItemCount())) {
/* 1556 */         return;
/*      */       }
/* 1558 */       JMenuItem localJMenuItem = JMenu.this.getItem(paramInt);
/* 1559 */       if (localJMenuItem != null)
/* 1560 */         if ((localJMenuItem instanceof JMenu)) {
/* 1561 */           MenuElement[] arrayOfMenuElement = JMenu.this.buildMenuElementArray((JMenu)localJMenuItem);
/* 1562 */           MenuSelectionManager.defaultManager().setSelectedPath(arrayOfMenuElement);
/*      */         } else {
/* 1564 */           MenuSelectionManager.defaultManager().setSelectedPath(null);
/*      */         }
/*      */     }
/*      */ 
/*      */     public void removeAccessibleSelection(int paramInt)
/*      */     {
/* 1577 */       if ((paramInt < 0) || (paramInt >= JMenu.this.getItemCount())) {
/* 1578 */         return;
/*      */       }
/* 1580 */       JMenuItem localJMenuItem = JMenu.this.getItem(paramInt);
/* 1581 */       if ((localJMenuItem != null) && ((localJMenuItem instanceof JMenu)) && 
/* 1582 */         (localJMenuItem.isSelected())) {
/* 1583 */         MenuElement[] arrayOfMenuElement1 = MenuSelectionManager.defaultManager().getSelectedPath();
/*      */ 
/* 1585 */         MenuElement[] arrayOfMenuElement2 = new MenuElement[arrayOfMenuElement1.length - 2];
/* 1586 */         for (int i = 0; i < arrayOfMenuElement1.length - 2; i++) {
/* 1587 */           arrayOfMenuElement2[i] = arrayOfMenuElement1[i];
/*      */         }
/* 1589 */         MenuSelectionManager.defaultManager().setSelectedPath(arrayOfMenuElement2);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void clearAccessibleSelection()
/*      */     {
/* 1601 */       MenuElement[] arrayOfMenuElement1 = MenuSelectionManager.defaultManager().getSelectedPath();
/*      */ 
/* 1603 */       if (arrayOfMenuElement1 != null)
/* 1604 */         for (int i = 0; i < arrayOfMenuElement1.length; i++)
/* 1605 */           if (arrayOfMenuElement1[i] == JMenu.this) {
/* 1606 */             MenuElement[] arrayOfMenuElement2 = new MenuElement[i + 1];
/* 1607 */             System.arraycopy(arrayOfMenuElement1, 0, arrayOfMenuElement2, 0, i);
/* 1608 */             arrayOfMenuElement2[i] = JMenu.this.getPopupMenu();
/* 1609 */             MenuSelectionManager.defaultManager().setSelectedPath(arrayOfMenuElement2);
/*      */           }
/*      */     }
/*      */ 
/*      */     public void selectAllAccessibleSelection()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   class MenuChangeListener
/*      */     implements ChangeListener, Serializable
/*      */   {
/* 1115 */     boolean isSelected = false;
/*      */ 
/*      */     MenuChangeListener() {  } 
/* 1117 */     public void stateChanged(ChangeEvent paramChangeEvent) { ButtonModel localButtonModel = (ButtonModel)paramChangeEvent.getSource();
/* 1118 */       boolean bool = localButtonModel.isSelected();
/*      */ 
/* 1120 */       if (bool != this.isSelected) {
/* 1121 */         if (bool == true)
/* 1122 */           JMenu.this.fireMenuSelected();
/*      */         else {
/* 1124 */           JMenu.this.fireMenuDeselected();
/*      */         }
/* 1126 */         this.isSelected = bool;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class WinListener extends WindowAdapter
/*      */     implements Serializable
/*      */   {
/*      */     JPopupMenu popupMenu;
/*      */ 
/*      */     public WinListener(JPopupMenu arg2)
/*      */     {
/*      */       Object localObject;
/* 1168 */       this.popupMenu = localObject;
/*      */     }
/*      */ 
/*      */     public void windowClosing(WindowEvent paramWindowEvent)
/*      */     {
/* 1174 */       JMenu.this.setSelected(false);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JMenu
 * JD-Core Version:    0.6.2
 */