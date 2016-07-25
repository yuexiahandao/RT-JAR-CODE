/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GraphicsDevice;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.event.MenuKeyEvent;
/*      */ import javax.swing.event.MenuKeyListener;
/*      */ import javax.swing.event.PopupMenuEvent;
/*      */ import javax.swing.event.PopupMenuListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.PopupMenuUI;
/*      */ import javax.swing.plaf.basic.BasicComboPopup;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class JPopupMenu extends JComponent
/*      */   implements Accessible, MenuElement
/*      */ {
/*      */   private static final String uiClassID = "PopupMenuUI";
/*   97 */   private static final Object defaultLWPopupEnabledKey = new StringBuffer("JPopupMenu.defaultLWPopupEnabledKey");
/*      */ 
/*  104 */   static boolean popupPostionFixDisabled = ((String)AccessController.doPrivileged(new GetPropertyAction("javax.swing.adjustPopupLocationToFit", ""))).equals("false");
/*      */   transient Component invoker;
/*      */   transient Popup popup;
/*      */   transient Frame frame;
/*      */   private int desiredLocationX;
/*      */   private int desiredLocationY;
/*  115 */   private String label = null;
/*  116 */   private boolean paintBorder = true;
/*  117 */   private Insets margin = null;
/*      */ 
/*  122 */   private boolean lightWeightPopup = true;
/*      */   private SingleSelectionModel selectionModel;
/*  132 */   private static final Object classLock = new Object();
/*      */   private static final boolean TRACE = false;
/*      */   private static final boolean VERBOSE = false;
/*      */   private static final boolean DEBUG = false;
/*      */ 
/*      */   public static void setDefaultLightWeightPopupEnabled(boolean paramBoolean)
/*      */   {
/*  149 */     SwingUtilities.appContextPut(defaultLWPopupEnabledKey, Boolean.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public static boolean getDefaultLightWeightPopupEnabled()
/*      */   {
/*  163 */     Boolean localBoolean = (Boolean)SwingUtilities.appContextGet(defaultLWPopupEnabledKey);
/*      */ 
/*  165 */     if (localBoolean == null) {
/*  166 */       SwingUtilities.appContextPut(defaultLWPopupEnabledKey, Boolean.TRUE);
/*      */ 
/*  168 */       return true;
/*      */     }
/*  170 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   public JPopupMenu()
/*      */   {
/*  177 */     this(null);
/*      */   }
/*      */ 
/*      */   public JPopupMenu(String paramString)
/*      */   {
/*  187 */     this.label = paramString;
/*  188 */     this.lightWeightPopup = getDefaultLightWeightPopupEnabled();
/*  189 */     setSelectionModel(new DefaultSingleSelectionModel());
/*  190 */     enableEvents(16L);
/*  191 */     setFocusTraversalKeysEnabled(false);
/*  192 */     updateUI();
/*      */   }
/*      */ 
/*      */   public PopupMenuUI getUI()
/*      */   {
/*  203 */     return (PopupMenuUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(PopupMenuUI paramPopupMenuUI)
/*      */   {
/*  218 */     super.setUI(paramPopupMenuUI);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  227 */     setUI((PopupMenuUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  239 */     return "PopupMenuUI";
/*      */   }
/*      */ 
/*      */   protected void processFocusEvent(FocusEvent paramFocusEvent) {
/*  243 */     super.processFocusEvent(paramFocusEvent);
/*      */   }
/*      */ 
/*      */   protected void processKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/*  252 */     MenuSelectionManager.defaultManager().processKeyEvent(paramKeyEvent);
/*  253 */     if (paramKeyEvent.isConsumed()) {
/*  254 */       return;
/*      */     }
/*  256 */     super.processKeyEvent(paramKeyEvent);
/*      */   }
/*      */ 
/*      */   public SingleSelectionModel getSelectionModel()
/*      */   {
/*  267 */     return this.selectionModel;
/*      */   }
/*      */ 
/*      */   public void setSelectionModel(SingleSelectionModel paramSingleSelectionModel)
/*      */   {
/*  280 */     this.selectionModel = paramSingleSelectionModel;
/*      */   }
/*      */ 
/*      */   public JMenuItem add(JMenuItem paramJMenuItem)
/*      */   {
/*  290 */     super.add(paramJMenuItem);
/*  291 */     return paramJMenuItem;
/*      */   }
/*      */ 
/*      */   public JMenuItem add(String paramString)
/*      */   {
/*  301 */     return add(new JMenuItem(paramString));
/*      */   }
/*      */ 
/*      */   public JMenuItem add(Action paramAction)
/*      */   {
/*  313 */     JMenuItem localJMenuItem = createActionComponent(paramAction);
/*  314 */     localJMenuItem.setAction(paramAction);
/*  315 */     add(localJMenuItem);
/*  316 */     return localJMenuItem;
/*      */   }
/*      */ 
/*      */   Point adjustPopupLocationToFitScreen(int paramInt1, int paramInt2)
/*      */   {
/*  327 */     Point localPoint = new Point(paramInt1, paramInt2);
/*      */ 
/*  329 */     if ((popupPostionFixDisabled == true) || (GraphicsEnvironment.isHeadless())) {
/*  330 */       return localPoint;
/*      */     }
/*      */ 
/*  335 */     GraphicsConfiguration localGraphicsConfiguration = getCurrentGraphicsConfiguration(localPoint);
/*  336 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*      */     Rectangle localRectangle;
/*  337 */     if (localGraphicsConfiguration != null)
/*      */     {
/*  339 */       localRectangle = localGraphicsConfiguration.getBounds();
/*      */     }
/*      */     else {
/*  342 */       localRectangle = new Rectangle(localToolkit.getScreenSize());
/*      */     }
/*      */ 
/*  346 */     Dimension localDimension = getPreferredSize();
/*  347 */     long l1 = localPoint.x + localDimension.width;
/*  348 */     long l2 = localPoint.y + localDimension.height;
/*  349 */     int i = localRectangle.width;
/*  350 */     int j = localRectangle.height;
/*      */ 
/*  352 */     if (!canPopupOverlapTaskBar())
/*      */     {
/*  354 */       Insets localInsets = localToolkit.getScreenInsets(localGraphicsConfiguration);
/*  355 */       localRectangle.x += localInsets.left;
/*  356 */       localRectangle.y += localInsets.top;
/*  357 */       i -= localInsets.left + localInsets.right;
/*  358 */       j -= localInsets.top + localInsets.bottom;
/*      */     }
/*  360 */     int k = localRectangle.x + i;
/*  361 */     int m = localRectangle.y + j;
/*      */ 
/*  364 */     if (l1 > k) {
/*  365 */       localPoint.x = (k - localDimension.width);
/*      */     }
/*      */ 
/*  368 */     if (l2 > m) {
/*  369 */       localPoint.y = (m - localDimension.height);
/*      */     }
/*      */ 
/*  372 */     if (localPoint.x < localRectangle.x) {
/*  373 */       localPoint.x = localRectangle.x;
/*      */     }
/*      */ 
/*  376 */     if (localPoint.y < localRectangle.y) {
/*  377 */       localPoint.y = localRectangle.y;
/*      */     }
/*      */ 
/*  380 */     return localPoint;
/*      */   }
/*      */ 
/*      */   private GraphicsConfiguration getCurrentGraphicsConfiguration(Point paramPoint)
/*      */   {
/*  390 */     Object localObject = null;
/*  391 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*      */ 
/*  393 */     GraphicsDevice[] arrayOfGraphicsDevice = localGraphicsEnvironment.getScreenDevices();
/*  394 */     for (int i = 0; i < arrayOfGraphicsDevice.length; i++) {
/*  395 */       if (arrayOfGraphicsDevice[i].getType() == 0) {
/*  396 */         GraphicsConfiguration localGraphicsConfiguration = arrayOfGraphicsDevice[i].getDefaultConfiguration();
/*      */ 
/*  398 */         if (localGraphicsConfiguration.getBounds().contains(paramPoint)) {
/*  399 */           localObject = localGraphicsConfiguration;
/*  400 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  405 */     if ((localObject == null) && (getInvoker() != null)) {
/*  406 */       localObject = getInvoker().getGraphicsConfiguration();
/*      */     }
/*  408 */     return localObject;
/*      */   }
/*      */ 
/*      */   static boolean canPopupOverlapTaskBar()
/*      */   {
/*  415 */     boolean bool = true;
/*      */ 
/*  417 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  418 */     if ((localToolkit instanceof SunToolkit)) {
/*  419 */       bool = ((SunToolkit)localToolkit).canPopupOverlapTaskBar();
/*      */     }
/*      */ 
/*  422 */     return bool;
/*      */   }
/*      */ 
/*      */   protected JMenuItem createActionComponent(Action paramAction)
/*      */   {
/*  436 */     JMenuItem local1 = new JMenuItem() {
/*      */       protected PropertyChangeListener createActionPropertyChangeListener(Action paramAnonymousAction) {
/*  438 */         PropertyChangeListener localPropertyChangeListener = JPopupMenu.this.createActionChangeListener(this);
/*  439 */         if (localPropertyChangeListener == null) {
/*  440 */           localPropertyChangeListener = super.createActionPropertyChangeListener(paramAnonymousAction);
/*      */         }
/*  442 */         return localPropertyChangeListener;
/*      */       }
/*      */     };
/*  445 */     local1.setHorizontalTextPosition(11);
/*  446 */     local1.setVerticalTextPosition(0);
/*  447 */     return local1;
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createActionChangeListener(JMenuItem paramJMenuItem)
/*      */   {
/*  455 */     return paramJMenuItem.createActionPropertyChangeListener0(paramJMenuItem.getAction());
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt)
/*      */   {
/*  468 */     if (paramInt < 0) {
/*  469 */       throw new IllegalArgumentException("index less than zero.");
/*      */     }
/*  471 */     if (paramInt > getComponentCount() - 1) {
/*  472 */       throw new IllegalArgumentException("index greater than the number of items.");
/*      */     }
/*  474 */     super.remove(paramInt);
/*      */   }
/*      */ 
/*      */   public void setLightWeightPopupEnabled(boolean paramBoolean)
/*      */   {
/*  501 */     this.lightWeightPopup = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean isLightWeightPopupEnabled()
/*      */   {
/*  511 */     return this.lightWeightPopup;
/*      */   }
/*      */ 
/*      */   public String getLabel()
/*      */   {
/*  521 */     return this.label;
/*      */   }
/*      */ 
/*      */   public void setLabel(String paramString)
/*      */   {
/*  536 */     String str = this.label;
/*  537 */     this.label = paramString;
/*  538 */     firePropertyChange("label", str, paramString);
/*  539 */     if (this.accessibleContext != null) {
/*  540 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", str, paramString);
/*      */     }
/*      */ 
/*  544 */     invalidate();
/*  545 */     repaint();
/*      */   }
/*      */ 
/*      */   public void addSeparator()
/*      */   {
/*  552 */     add(new Separator());
/*      */   }
/*      */ 
/*      */   public void insert(Action paramAction, int paramInt)
/*      */   {
/*  566 */     JMenuItem localJMenuItem = createActionComponent(paramAction);
/*  567 */     localJMenuItem.setAction(paramAction);
/*  568 */     insert(localJMenuItem, paramInt);
/*      */   }
/*      */ 
/*      */   public void insert(Component paramComponent, int paramInt)
/*      */   {
/*  581 */     if (paramInt < 0) {
/*  582 */       throw new IllegalArgumentException("index less than zero.");
/*      */     }
/*      */ 
/*  585 */     int i = getComponentCount();
/*      */ 
/*  587 */     Vector localVector = new Vector();
/*      */ 
/*  593 */     for (int j = paramInt; j < i; j++) {
/*  594 */       localVector.addElement(getComponent(paramInt));
/*  595 */       remove(paramInt);
/*      */     }
/*      */ 
/*  598 */     add(paramComponent);
/*      */ 
/*  603 */     for (Component localComponent : localVector)
/*  604 */       add(localComponent);
/*      */   }
/*      */ 
/*      */   public void addPopupMenuListener(PopupMenuListener paramPopupMenuListener)
/*      */   {
/*  614 */     this.listenerList.add(PopupMenuListener.class, paramPopupMenuListener);
/*      */   }
/*      */ 
/*      */   public void removePopupMenuListener(PopupMenuListener paramPopupMenuListener)
/*      */   {
/*  623 */     this.listenerList.remove(PopupMenuListener.class, paramPopupMenuListener);
/*      */   }
/*      */ 
/*      */   public PopupMenuListener[] getPopupMenuListeners()
/*      */   {
/*  635 */     return (PopupMenuListener[])this.listenerList.getListeners(PopupMenuListener.class);
/*      */   }
/*      */ 
/*      */   public void addMenuKeyListener(MenuKeyListener paramMenuKeyListener)
/*      */   {
/*  645 */     this.listenerList.add(MenuKeyListener.class, paramMenuKeyListener);
/*      */   }
/*      */ 
/*      */   public void removeMenuKeyListener(MenuKeyListener paramMenuKeyListener)
/*      */   {
/*  655 */     this.listenerList.remove(MenuKeyListener.class, paramMenuKeyListener);
/*      */   }
/*      */ 
/*      */   public MenuKeyListener[] getMenuKeyListeners()
/*      */   {
/*  667 */     return (MenuKeyListener[])this.listenerList.getListeners(MenuKeyListener.class);
/*      */   }
/*      */ 
/*      */   protected void firePopupMenuWillBecomeVisible()
/*      */   {
/*  675 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*  676 */     PopupMenuEvent localPopupMenuEvent = null;
/*  677 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  678 */       if (arrayOfObject[i] == PopupMenuListener.class) {
/*  679 */         if (localPopupMenuEvent == null)
/*  680 */           localPopupMenuEvent = new PopupMenuEvent(this);
/*  681 */         ((PopupMenuListener)arrayOfObject[(i + 1)]).popupMenuWillBecomeVisible(localPopupMenuEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void firePopupMenuWillBecomeInvisible()
/*      */   {
/*  691 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*  692 */     PopupMenuEvent localPopupMenuEvent = null;
/*  693 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  694 */       if (arrayOfObject[i] == PopupMenuListener.class) {
/*  695 */         if (localPopupMenuEvent == null)
/*  696 */           localPopupMenuEvent = new PopupMenuEvent(this);
/*  697 */         ((PopupMenuListener)arrayOfObject[(i + 1)]).popupMenuWillBecomeInvisible(localPopupMenuEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void firePopupMenuCanceled()
/*      */   {
/*  707 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*  708 */     PopupMenuEvent localPopupMenuEvent = null;
/*  709 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  710 */       if (arrayOfObject[i] == PopupMenuListener.class) {
/*  711 */         if (localPopupMenuEvent == null)
/*  712 */           localPopupMenuEvent = new PopupMenuEvent(this);
/*  713 */         ((PopupMenuListener)arrayOfObject[(i + 1)]).popupMenuCanceled(localPopupMenuEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   boolean alwaysOnTop()
/*      */   {
/*  725 */     return true;
/*      */   }
/*      */ 
/*      */   public void pack()
/*      */   {
/*  733 */     if (this.popup != null) {
/*  734 */       Dimension localDimension = getPreferredSize();
/*      */ 
/*  736 */       if ((localDimension == null) || (localDimension.width != getWidth()) || (localDimension.height != getHeight()))
/*      */       {
/*  738 */         showPopup();
/*      */       }
/*  740 */       else validate();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setVisible(boolean paramBoolean)
/*      */   {
/*  760 */     if (paramBoolean == isVisible())
/*      */       return;
/*      */     Object localObject;
/*  764 */     if (!paramBoolean)
/*      */     {
/*  770 */       localObject = (Boolean)getClientProperty("JPopupMenu.firePopupMenuCanceled");
/*  771 */       if ((localObject != null) && (localObject == Boolean.TRUE)) {
/*  772 */         putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.FALSE);
/*  773 */         firePopupMenuCanceled();
/*      */       }
/*  775 */       getSelectionModel().clearSelection();
/*      */     }
/*  780 */     else if (isPopupMenu()) {
/*  781 */       localObject = new MenuElement[1];
/*  782 */       localObject[0] = this;
/*  783 */       MenuSelectionManager.defaultManager().setSelectedPath((MenuElement[])localObject);
/*      */     }
/*      */ 
/*  787 */     if (paramBoolean) {
/*  788 */       firePopupMenuWillBecomeVisible();
/*  789 */       showPopup();
/*  790 */       firePropertyChange("visible", Boolean.FALSE, Boolean.TRUE);
/*      */     }
/*  793 */     else if (this.popup != null) {
/*  794 */       firePopupMenuWillBecomeInvisible();
/*  795 */       this.popup.hide();
/*  796 */       this.popup = null;
/*  797 */       firePropertyChange("visible", Boolean.TRUE, Boolean.FALSE);
/*      */ 
/*  800 */       if (isPopupMenu())
/*  801 */         MenuSelectionManager.defaultManager().clearSelectedPath();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void showPopup()
/*      */   {
/*  817 */     Popup localPopup1 = this.popup;
/*      */ 
/*  819 */     if (localPopup1 != null) {
/*  820 */       localPopup1.hide();
/*      */     }
/*  822 */     PopupFactory localPopupFactory = PopupFactory.getSharedInstance();
/*      */ 
/*  824 */     if (isLightWeightPopupEnabled()) {
/*  825 */       localPopupFactory.setPopupType(0);
/*      */     }
/*      */     else {
/*  828 */       localPopupFactory.setPopupType(2);
/*      */     }
/*      */ 
/*  832 */     Point localPoint = adjustPopupLocationToFitScreen(this.desiredLocationX, this.desiredLocationY);
/*  833 */     this.desiredLocationX = localPoint.x;
/*  834 */     this.desiredLocationY = localPoint.y;
/*      */ 
/*  836 */     Popup localPopup2 = getUI().getPopup(this, this.desiredLocationX, this.desiredLocationY);
/*      */ 
/*  839 */     localPopupFactory.setPopupType(0);
/*  840 */     this.popup = localPopup2;
/*  841 */     localPopup2.show();
/*      */   }
/*      */ 
/*      */   public boolean isVisible()
/*      */   {
/*  849 */     return this.popup != null;
/*      */   }
/*      */ 
/*      */   public void setLocation(int paramInt1, int paramInt2)
/*      */   {
/*  864 */     int i = this.desiredLocationX;
/*  865 */     int j = this.desiredLocationY;
/*      */ 
/*  867 */     this.desiredLocationX = paramInt1;
/*  868 */     this.desiredLocationY = paramInt2;
/*  869 */     if ((this.popup != null) && ((paramInt1 != i) || (paramInt2 != j)))
/*  870 */       showPopup();
/*      */   }
/*      */ 
/*      */   private boolean isPopupMenu()
/*      */   {
/*  881 */     return (this.invoker != null) && (!(this.invoker instanceof JMenu));
/*      */   }
/*      */ 
/*      */   public Component getInvoker()
/*      */   {
/*  891 */     return this.invoker;
/*      */   }
/*      */ 
/*      */   public void setInvoker(Component paramComponent)
/*      */   {
/*  905 */     Component localComponent = this.invoker;
/*  906 */     this.invoker = paramComponent;
/*  907 */     if ((localComponent != this.invoker) && (this.ui != null)) {
/*  908 */       this.ui.uninstallUI(this);
/*  909 */       this.ui.installUI(this);
/*      */     }
/*  911 */     invalidate();
/*      */   }
/*      */ 
/*      */   public void show(Component paramComponent, int paramInt1, int paramInt2)
/*      */   {
/*  928 */     setInvoker(paramComponent);
/*  929 */     Frame localFrame = getFrame(paramComponent);
/*  930 */     if (localFrame != this.frame)
/*      */     {
/*  933 */       if (localFrame != null) {
/*  934 */         this.frame = localFrame;
/*  935 */         if (this.popup != null) {
/*  936 */           setVisible(false);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  941 */     if (paramComponent != null) {
/*  942 */       Point localPoint = paramComponent.getLocationOnScreen();
/*      */ 
/*  946 */       long l1 = localPoint.x + paramInt1;
/*      */ 
/*  948 */       long l2 = localPoint.y + paramInt2;
/*      */ 
/*  950 */       if (l1 > 2147483647L) l1 = 2147483647L;
/*  951 */       if (l1 < -2147483648L) l1 = -2147483648L;
/*  952 */       if (l2 > 2147483647L) l2 = 2147483647L;
/*  953 */       if (l2 < -2147483648L) l2 = -2147483648L;
/*      */ 
/*  955 */       setLocation((int)l1, (int)l2);
/*      */     } else {
/*  957 */       setLocation(paramInt1, paramInt2);
/*      */     }
/*  959 */     setVisible(true);
/*      */   }
/*      */ 
/*      */   JPopupMenu getRootPopupMenu()
/*      */   {
/*  969 */     JPopupMenu localJPopupMenu = this;
/*      */ 
/*  972 */     while ((localJPopupMenu != null) && (localJPopupMenu.isPopupMenu() != true) && (localJPopupMenu.getInvoker() != null) && (localJPopupMenu.getInvoker().getParent() != null) && ((localJPopupMenu.getInvoker().getParent() instanceof JPopupMenu)))
/*      */     {
/*  975 */       localJPopupMenu = (JPopupMenu)localJPopupMenu.getInvoker().getParent();
/*      */     }
/*  977 */     return localJPopupMenu;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Component getComponentAtIndex(int paramInt)
/*      */   {
/*  989 */     return getComponent(paramInt);
/*      */   }
/*      */ 
/*      */   public int getComponentIndex(Component paramComponent)
/*      */   {
/* 1000 */     int i = getComponentCount();
/* 1001 */     Component[] arrayOfComponent = getComponents();
/* 1002 */     for (int j = 0; j < i; j++) {
/* 1003 */       Component localComponent = arrayOfComponent[j];
/* 1004 */       if (localComponent == paramComponent)
/* 1005 */         return j;
/*      */     }
/* 1007 */     return -1;
/*      */   }
/*      */ 
/*      */   public void setPopupSize(Dimension paramDimension)
/*      */   {
/* 1020 */     Dimension localDimension1 = getPreferredSize();
/*      */ 
/* 1022 */     setPreferredSize(paramDimension);
/* 1023 */     if (this.popup != null) {
/* 1024 */       Dimension localDimension2 = getPreferredSize();
/*      */ 
/* 1026 */       if (!localDimension1.equals(localDimension2))
/* 1027 */         showPopup();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPopupSize(int paramInt1, int paramInt2)
/*      */   {
/* 1043 */     setPopupSize(new Dimension(paramInt1, paramInt2));
/*      */   }
/*      */ 
/*      */   public void setSelected(Component paramComponent)
/*      */   {
/* 1057 */     SingleSelectionModel localSingleSelectionModel = getSelectionModel();
/* 1058 */     int i = getComponentIndex(paramComponent);
/* 1059 */     localSingleSelectionModel.setSelectedIndex(i);
/*      */   }
/*      */ 
/*      */   public boolean isBorderPainted()
/*      */   {
/* 1069 */     return this.paintBorder;
/*      */   }
/*      */ 
/*      */   public void setBorderPainted(boolean paramBoolean)
/*      */   {
/* 1081 */     this.paintBorder = paramBoolean;
/* 1082 */     repaint();
/*      */   }
/*      */ 
/*      */   protected void paintBorder(Graphics paramGraphics)
/*      */   {
/* 1094 */     if (isBorderPainted())
/* 1095 */       super.paintBorder(paramGraphics);
/*      */   }
/*      */ 
/*      */   public Insets getMargin()
/*      */   {
/* 1106 */     if (this.margin == null) {
/* 1107 */       return new Insets(0, 0, 0, 0);
/*      */     }
/* 1109 */     return this.margin;
/*      */   }
/*      */ 
/*      */   boolean isSubPopupMenu(JPopupMenu paramJPopupMenu)
/*      */   {
/* 1122 */     int i = getComponentCount();
/* 1123 */     Component[] arrayOfComponent = getComponents();
/* 1124 */     for (int j = 0; j < i; j++) {
/* 1125 */       Component localComponent = arrayOfComponent[j];
/* 1126 */       if ((localComponent instanceof JMenu)) {
/* 1127 */         JMenu localJMenu = (JMenu)localComponent;
/* 1128 */         JPopupMenu localJPopupMenu = localJMenu.getPopupMenu();
/* 1129 */         if (localJPopupMenu == paramJPopupMenu)
/* 1130 */           return true;
/* 1131 */         if (localJPopupMenu.isSubPopupMenu(paramJPopupMenu))
/* 1132 */           return true;
/*      */       }
/*      */     }
/* 1135 */     return false;
/*      */   }
/*      */ 
/*      */   private static Frame getFrame(Component paramComponent)
/*      */   {
/* 1140 */     Object localObject = paramComponent;
/*      */ 
/* 1142 */     while ((!(localObject instanceof Frame)) && (localObject != null)) {
/* 1143 */       localObject = ((Component)localObject).getParent();
/*      */     }
/* 1145 */     return (Frame)localObject;
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1160 */     String str1 = this.label != null ? this.label : "";
/*      */ 
/* 1162 */     String str2 = this.paintBorder ? "true" : "false";
/*      */ 
/* 1164 */     String str3 = this.margin != null ? this.margin.toString() : "";
/*      */ 
/* 1166 */     String str4 = isLightWeightPopupEnabled() ? "true" : "false";
/*      */ 
/* 1168 */     return super.paramString() + ",desiredLocationX=" + this.desiredLocationX + ",desiredLocationY=" + this.desiredLocationY + ",label=" + str1 + ",lightWeightPopupEnabled=" + str4 + ",margin=" + str3 + ",paintBorder=" + str2;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1191 */     if (this.accessibleContext == null) {
/* 1192 */       this.accessibleContext = new AccessibleJPopupMenu();
/*      */     }
/* 1194 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1309 */     Vector localVector = new Vector();
/*      */ 
/* 1311 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1313 */     if ((this.invoker != null) && ((this.invoker instanceof Serializable))) {
/* 1314 */       localVector.addElement("invoker");
/* 1315 */       localVector.addElement(this.invoker);
/*      */     }
/*      */ 
/* 1318 */     if ((this.popup != null) && ((this.popup instanceof Serializable))) {
/* 1319 */       localVector.addElement("popup");
/* 1320 */       localVector.addElement(this.popup);
/*      */     }
/* 1322 */     paramObjectOutputStream.writeObject(localVector);
/*      */ 
/* 1324 */     if (getUIClassID().equals("PopupMenuUI")) {
/* 1325 */       byte b = JComponent.getWriteObjCounter(this);
/* 1326 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 1327 */       if ((b == 0) && (this.ui != null))
/* 1328 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1336 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1338 */     Vector localVector = (Vector)paramObjectInputStream.readObject();
/* 1339 */     int i = 0;
/* 1340 */     int j = localVector.size();
/*      */ 
/* 1342 */     if ((i < j) && (localVector.elementAt(i).equals("invoker")))
/*      */     {
/* 1344 */       this.invoker = ((Component)localVector.elementAt(++i));
/* 1345 */       i++;
/*      */     }
/* 1347 */     if ((i < j) && (localVector.elementAt(i).equals("popup")))
/*      */     {
/* 1349 */       this.popup = ((Popup)localVector.elementAt(++i));
/* 1350 */       i++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processMouseEvent(MouseEvent paramMouseEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void processKeyEvent(KeyEvent paramKeyEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*      */   {
/* 1376 */     MenuKeyEvent localMenuKeyEvent = new MenuKeyEvent(paramKeyEvent.getComponent(), paramKeyEvent.getID(), paramKeyEvent.getWhen(), paramKeyEvent.getModifiers(), paramKeyEvent.getKeyCode(), paramKeyEvent.getKeyChar(), paramArrayOfMenuElement, paramMenuSelectionManager);
/*      */ 
/* 1380 */     processMenuKeyEvent(localMenuKeyEvent);
/*      */ 
/* 1382 */     if (localMenuKeyEvent.isConsumed())
/* 1383 */       paramKeyEvent.consume();
/*      */   }
/*      */ 
/*      */   private void processMenuKeyEvent(MenuKeyEvent paramMenuKeyEvent)
/*      */   {
/* 1394 */     switch (paramMenuKeyEvent.getID()) {
/*      */     case 401:
/* 1396 */       fireMenuKeyPressed(paramMenuKeyEvent); break;
/*      */     case 402:
/* 1398 */       fireMenuKeyReleased(paramMenuKeyEvent); break;
/*      */     case 400:
/* 1400 */       fireMenuKeyTyped(paramMenuKeyEvent); break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fireMenuKeyPressed(MenuKeyEvent paramMenuKeyEvent)
/*      */   {
/* 1414 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1415 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1416 */       if (arrayOfObject[i] == MenuKeyListener.class)
/* 1417 */         ((MenuKeyListener)arrayOfObject[(i + 1)]).menuKeyPressed(paramMenuKeyEvent);
/*      */   }
/*      */ 
/*      */   private void fireMenuKeyReleased(MenuKeyEvent paramMenuKeyEvent)
/*      */   {
/* 1430 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1431 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1432 */       if (arrayOfObject[i] == MenuKeyListener.class)
/* 1433 */         ((MenuKeyListener)arrayOfObject[(i + 1)]).menuKeyReleased(paramMenuKeyEvent);
/*      */   }
/*      */ 
/*      */   private void fireMenuKeyTyped(MenuKeyEvent paramMenuKeyEvent)
/*      */   {
/* 1446 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1447 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1448 */       if (arrayOfObject[i] == MenuKeyListener.class)
/* 1449 */         ((MenuKeyListener)arrayOfObject[(i + 1)]).menuKeyTyped(paramMenuKeyEvent);
/*      */   }
/*      */ 
/*      */   public void menuSelectionChanged(boolean paramBoolean)
/*      */   {
/* 1468 */     if ((this.invoker instanceof JMenu)) {
/* 1469 */       JMenu localJMenu = (JMenu)this.invoker;
/* 1470 */       if (paramBoolean)
/* 1471 */         localJMenu.setPopupMenuVisible(true);
/*      */       else
/* 1473 */         localJMenu.setPopupMenuVisible(false);
/*      */     }
/* 1475 */     if ((isPopupMenu()) && (!paramBoolean))
/* 1476 */       setVisible(false);
/*      */   }
/*      */ 
/*      */   public MenuElement[] getSubElements()
/*      */   {
/* 1492 */     Vector localVector = new Vector();
/* 1493 */     int i = getComponentCount();
/*      */ 
/* 1497 */     for (int j = 0; j < i; j++) {
/* 1498 */       Component localComponent = getComponent(j);
/* 1499 */       if ((localComponent instanceof MenuElement)) {
/* 1500 */         localVector.addElement((MenuElement)localComponent);
/*      */       }
/*      */     }
/* 1503 */     MenuElement[] arrayOfMenuElement = new MenuElement[localVector.size()];
/* 1504 */     j = 0; for (i = localVector.size(); j < i; j++)
/* 1505 */       arrayOfMenuElement[j] = ((MenuElement)localVector.elementAt(j));
/* 1506 */     return arrayOfMenuElement;
/*      */   }
/*      */ 
/*      */   public Component getComponent()
/*      */   {
/* 1515 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean isPopupTrigger(MouseEvent paramMouseEvent)
/*      */   {
/* 1551 */     return getUI().isPopupTrigger(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   protected class AccessibleJPopupMenu extends JComponent.AccessibleJComponent
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     protected AccessibleJPopupMenu()
/*      */     {
/* 1211 */       super();
/* 1212 */       JPopupMenu.this.addPropertyChangeListener(this);
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1222 */       return AccessibleRole.POPUP_MENU;
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1234 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1235 */       if (str == "visible")
/* 1236 */         if ((paramPropertyChangeEvent.getOldValue() == Boolean.FALSE) && (paramPropertyChangeEvent.getNewValue() == Boolean.TRUE))
/*      */         {
/* 1238 */           handlePopupIsVisibleEvent(true);
/*      */         }
/* 1240 */         else if ((paramPropertyChangeEvent.getOldValue() == Boolean.TRUE) && (paramPropertyChangeEvent.getNewValue() == Boolean.FALSE))
/*      */         {
/* 1242 */           handlePopupIsVisibleEvent(false);
/*      */         }
/*      */     }
/*      */ 
/*      */     private void handlePopupIsVisibleEvent(boolean paramBoolean)
/*      */     {
/* 1251 */       if (paramBoolean)
/*      */       {
/* 1253 */         firePropertyChange("AccessibleState", null, AccessibleState.VISIBLE);
/*      */ 
/* 1256 */         fireActiveDescendant();
/*      */       }
/*      */       else {
/* 1259 */         firePropertyChange("AccessibleState", AccessibleState.VISIBLE, null);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void fireActiveDescendant()
/*      */     {
/* 1269 */       if ((JPopupMenu.this instanceof BasicComboPopup))
/*      */       {
/* 1271 */         JList localJList = ((BasicComboPopup)JPopupMenu.this).getList();
/* 1272 */         if (localJList == null) {
/* 1273 */           return;
/*      */         }
/*      */ 
/* 1277 */         AccessibleContext localAccessibleContext1 = localJList.getAccessibleContext();
/* 1278 */         AccessibleSelection localAccessibleSelection = localAccessibleContext1.getAccessibleSelection();
/* 1279 */         if (localAccessibleSelection == null) {
/* 1280 */           return;
/*      */         }
/* 1282 */         Accessible localAccessible = localAccessibleSelection.getAccessibleSelection(0);
/* 1283 */         if (localAccessible == null) {
/* 1284 */           return;
/*      */         }
/* 1286 */         AccessibleContext localAccessibleContext2 = localAccessible.getAccessibleContext();
/*      */ 
/* 1289 */         if ((localAccessibleContext2 != null) && (JPopupMenu.this.invoker != null)) {
/* 1290 */           AccessibleContext localAccessibleContext3 = JPopupMenu.this.invoker.getAccessibleContext();
/* 1291 */           if (localAccessibleContext3 != null)
/*      */           {
/* 1295 */             localAccessibleContext3.firePropertyChange("AccessibleActiveDescendant", null, localAccessibleContext2);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Separator extends JSeparator
/*      */   {
/*      */     public Separator()
/*      */     {
/* 1526 */       super();
/*      */     }
/*      */ 
/*      */     public String getUIClassID()
/*      */     {
/* 1538 */       return "PopupMenuSeparatorUI";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JPopupMenu
 * JD-Core Version:    0.6.2
 */