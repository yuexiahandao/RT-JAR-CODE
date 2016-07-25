/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.AWTEventListener;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.ComponentInputMap;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JApplet;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.MenuElement;
/*      */ import javax.swing.MenuSelectionManager;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.MenuKeyEvent;
/*      */ import javax.swing.event.MenuKeyListener;
/*      */ import javax.swing.event.PopupMenuEvent;
/*      */ import javax.swing.event.PopupMenuListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.PopupMenuUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.UngrabEvent;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicPopupMenuUI extends PopupMenuUI
/*      */ {
/*   64 */   static final StringBuilder MOUSE_GRABBER_KEY = new StringBuilder("javax.swing.plaf.basic.BasicPopupMenuUI.MouseGrabber");
/*      */ 
/*   66 */   static final StringBuilder MENU_KEYBOARD_HELPER_KEY = new StringBuilder("javax.swing.plaf.basic.BasicPopupMenuUI.MenuKeyboardHelper");
/*      */ 
/*   69 */   protected JPopupMenu popupMenu = null;
/*   70 */   private transient PopupMenuListener popupMenuListener = null;
/*   71 */   private MenuKeyListener menuKeyListener = null;
/*      */   private static boolean checkedUnpostPopup;
/*      */   private static boolean unpostPopup;
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*   77 */     return new BasicPopupMenuUI();
/*      */   }
/*      */ 
/*      */   public BasicPopupMenuUI() {
/*   81 */     BasicLookAndFeel.needsEventHelper = true;
/*   82 */     LookAndFeel localLookAndFeel = UIManager.getLookAndFeel();
/*   83 */     if ((localLookAndFeel instanceof BasicLookAndFeel))
/*   84 */       ((BasicLookAndFeel)localLookAndFeel).installAWTEventListener();
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*   89 */     this.popupMenu = ((JPopupMenu)paramJComponent);
/*      */ 
/*   91 */     installDefaults();
/*   92 */     installListeners();
/*   93 */     installKeyboardActions();
/*      */   }
/*      */ 
/*      */   public void installDefaults() {
/*   97 */     if ((this.popupMenu.getLayout() == null) || ((this.popupMenu.getLayout() instanceof UIResource)))
/*      */     {
/*   99 */       this.popupMenu.setLayout(new DefaultMenuLayout(this.popupMenu, 1));
/*      */     }
/*  101 */     LookAndFeel.installProperty(this.popupMenu, "opaque", Boolean.TRUE);
/*  102 */     LookAndFeel.installBorder(this.popupMenu, "PopupMenu.border");
/*  103 */     LookAndFeel.installColorsAndFont(this.popupMenu, "PopupMenu.background", "PopupMenu.foreground", "PopupMenu.font");
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  110 */     if (this.popupMenuListener == null) {
/*  111 */       this.popupMenuListener = new BasicPopupMenuListener(null);
/*      */     }
/*  113 */     this.popupMenu.addPopupMenuListener(this.popupMenuListener);
/*      */ 
/*  115 */     if (this.menuKeyListener == null) {
/*  116 */       this.menuKeyListener = new BasicMenuKeyListener(null);
/*      */     }
/*  118 */     this.popupMenu.addMenuKeyListener(this.menuKeyListener);
/*      */ 
/*  120 */     AppContext localAppContext = AppContext.getAppContext();
/*      */     Object localObject1;
/*  121 */     synchronized (MOUSE_GRABBER_KEY) {
/*  122 */       localObject1 = (MouseGrabber)localAppContext.get(MOUSE_GRABBER_KEY);
/*      */ 
/*  124 */       if (localObject1 == null) {
/*  125 */         localObject1 = new MouseGrabber();
/*  126 */         localAppContext.put(MOUSE_GRABBER_KEY, localObject1);
/*      */       }
/*      */     }
/*  129 */     synchronized (MENU_KEYBOARD_HELPER_KEY) {
/*  130 */       localObject1 = (MenuKeyboardHelper)localAppContext.get(MENU_KEYBOARD_HELPER_KEY);
/*      */ 
/*  132 */       if (localObject1 == null) {
/*  133 */         localObject1 = new MenuKeyboardHelper();
/*  134 */         localAppContext.put(MENU_KEYBOARD_HELPER_KEY, localObject1);
/*  135 */         MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  136 */         localMenuSelectionManager.addChangeListener((ChangeListener)localObject1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions() {
/*      */   }
/*      */ 
/*      */   static InputMap getInputMap(JPopupMenu paramJPopupMenu, JComponent paramJComponent) {
/*  145 */     Object localObject = null;
/*  146 */     Object[] arrayOfObject1 = (Object[])UIManager.get("PopupMenu.selectedWindowInputMapBindings");
/*  147 */     if (arrayOfObject1 != null) {
/*  148 */       localObject = LookAndFeel.makeComponentInputMap(paramJComponent, arrayOfObject1);
/*  149 */       if (!paramJPopupMenu.getComponentOrientation().isLeftToRight()) {
/*  150 */         Object[] arrayOfObject2 = (Object[])UIManager.get("PopupMenu.selectedWindowInputMapBindings.RightToLeft");
/*  151 */         if (arrayOfObject2 != null) {
/*  152 */           ComponentInputMap localComponentInputMap = LookAndFeel.makeComponentInputMap(paramJComponent, arrayOfObject2);
/*  153 */           localComponentInputMap.setParent((InputMap)localObject);
/*  154 */           localObject = localComponentInputMap;
/*      */         }
/*      */       }
/*      */     }
/*  158 */     return localObject;
/*      */   }
/*      */ 
/*      */   static ActionMap getActionMap() {
/*  162 */     return LazyActionMap.getActionMap(BasicPopupMenuUI.class, "PopupMenu.actionMap");
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/*  167 */     paramLazyActionMap.put(new Actions("cancel"));
/*  168 */     paramLazyActionMap.put(new Actions("selectNext"));
/*  169 */     paramLazyActionMap.put(new Actions("selectPrevious"));
/*  170 */     paramLazyActionMap.put(new Actions("selectParent"));
/*  171 */     paramLazyActionMap.put(new Actions("selectChild"));
/*  172 */     paramLazyActionMap.put(new Actions("return"));
/*  173 */     BasicLookAndFeel.installAudioActionMap(paramLazyActionMap);
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent) {
/*  177 */     uninstallDefaults();
/*  178 */     uninstallListeners();
/*  179 */     uninstallKeyboardActions();
/*      */ 
/*  181 */     this.popupMenu = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults() {
/*  185 */     LookAndFeel.uninstallBorder(this.popupMenu);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners() {
/*  189 */     if (this.popupMenuListener != null) {
/*  190 */       this.popupMenu.removePopupMenuListener(this.popupMenuListener);
/*      */     }
/*  192 */     if (this.menuKeyListener != null)
/*  193 */       this.popupMenu.removeMenuKeyListener(this.menuKeyListener);
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/*  198 */     SwingUtilities.replaceUIActionMap(this.popupMenu, null);
/*  199 */     SwingUtilities.replaceUIInputMap(this.popupMenu, 2, null);
/*      */   }
/*      */ 
/*      */   static MenuElement getFirstPopup()
/*      */   {
/*  204 */     MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  205 */     MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/*  206 */     MenuElement localMenuElement = null;
/*      */ 
/*  208 */     for (int i = 0; (localMenuElement == null) && (i < arrayOfMenuElement.length); i++) {
/*  209 */       if ((arrayOfMenuElement[i] instanceof JPopupMenu)) {
/*  210 */         localMenuElement = arrayOfMenuElement[i];
/*      */       }
/*      */     }
/*  213 */     return localMenuElement;
/*      */   }
/*      */ 
/*      */   static JPopupMenu getLastPopup() {
/*  217 */     MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  218 */     MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/*  219 */     JPopupMenu localJPopupMenu = null;
/*      */ 
/*  221 */     for (int i = arrayOfMenuElement.length - 1; (localJPopupMenu == null) && (i >= 0); i--) {
/*  222 */       if ((arrayOfMenuElement[i] instanceof JPopupMenu))
/*  223 */         localJPopupMenu = (JPopupMenu)arrayOfMenuElement[i];
/*      */     }
/*  225 */     return localJPopupMenu;
/*      */   }
/*      */ 
/*      */   static List<JPopupMenu> getPopups() {
/*  229 */     MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  230 */     MenuElement[] arrayOfMenuElement1 = localMenuSelectionManager.getSelectedPath();
/*      */ 
/*  232 */     ArrayList localArrayList = new ArrayList(arrayOfMenuElement1.length);
/*  233 */     for (MenuElement localMenuElement : arrayOfMenuElement1) {
/*  234 */       if ((localMenuElement instanceof JPopupMenu)) {
/*  235 */         localArrayList.add((JPopupMenu)localMenuElement);
/*      */       }
/*      */     }
/*  238 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public boolean isPopupTrigger(MouseEvent paramMouseEvent) {
/*  242 */     return (paramMouseEvent.getID() == 502) && ((paramMouseEvent.getModifiers() & 0x4) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean checkInvokerEqual(MenuElement paramMenuElement1, MenuElement paramMenuElement2)
/*      */   {
/*  247 */     Component localComponent1 = paramMenuElement1.getComponent();
/*  248 */     Component localComponent2 = paramMenuElement2.getComponent();
/*      */ 
/*  250 */     if ((localComponent1 instanceof JPopupMenu)) {
/*  251 */       localComponent1 = ((JPopupMenu)localComponent1).getInvoker();
/*      */     }
/*  253 */     if ((localComponent2 instanceof JPopupMenu)) {
/*  254 */       localComponent2 = ((JPopupMenu)localComponent2).getInvoker();
/*      */     }
/*  256 */     return localComponent1 == localComponent2;
/*      */   }
/*      */ 
/*      */   private static MenuElement nextEnabledChild(MenuElement[] paramArrayOfMenuElement, int paramInt1, int paramInt2)
/*      */   {
/*  675 */     for (int i = paramInt1; i <= paramInt2; i++) {
/*  676 */       if (paramArrayOfMenuElement[i] != null) {
/*  677 */         Component localComponent = paramArrayOfMenuElement[i].getComponent();
/*  678 */         if ((localComponent != null) && ((localComponent.isEnabled()) || (UIManager.getBoolean("MenuItem.disabledAreNavigable"))) && (localComponent.isVisible()))
/*      */         {
/*  681 */           return paramArrayOfMenuElement[i];
/*      */         }
/*      */       }
/*      */     }
/*  685 */     return null;
/*      */   }
/*      */ 
/*      */   private static MenuElement previousEnabledChild(MenuElement[] paramArrayOfMenuElement, int paramInt1, int paramInt2)
/*      */   {
/*  690 */     for (int i = paramInt1; i >= paramInt2; i--) {
/*  691 */       if (paramArrayOfMenuElement[i] != null) {
/*  692 */         Component localComponent = paramArrayOfMenuElement[i].getComponent();
/*  693 */         if ((localComponent != null) && ((localComponent.isEnabled()) || (UIManager.getBoolean("MenuItem.disabledAreNavigable"))) && (localComponent.isVisible()))
/*      */         {
/*  696 */           return paramArrayOfMenuElement[i];
/*      */         }
/*      */       }
/*      */     }
/*  700 */     return null;
/*      */   }
/*      */ 
/*      */   static MenuElement findEnabledChild(MenuElement[] paramArrayOfMenuElement, int paramInt, boolean paramBoolean)
/*      */   {
/*      */     MenuElement localMenuElement;
/*  706 */     if (paramBoolean) {
/*  707 */       localMenuElement = nextEnabledChild(paramArrayOfMenuElement, paramInt + 1, paramArrayOfMenuElement.length - 1);
/*  708 */       if (localMenuElement == null) localMenuElement = nextEnabledChild(paramArrayOfMenuElement, 0, paramInt - 1); 
/*      */     }
/*  710 */     else { localMenuElement = previousEnabledChild(paramArrayOfMenuElement, paramInt - 1, 0);
/*  711 */       if (localMenuElement == null) localMenuElement = previousEnabledChild(paramArrayOfMenuElement, paramArrayOfMenuElement.length - 1, paramInt + 1);
/*      */     }
/*      */ 
/*  714 */     return localMenuElement;
/*      */   }
/*      */ 
/*      */   static MenuElement findEnabledChild(MenuElement[] paramArrayOfMenuElement, MenuElement paramMenuElement, boolean paramBoolean)
/*      */   {
/*  719 */     for (int i = 0; i < paramArrayOfMenuElement.length; i++) {
/*  720 */       if (paramArrayOfMenuElement[i] == paramMenuElement) {
/*  721 */         return findEnabledChild(paramArrayOfMenuElement, i, paramBoolean);
/*      */       }
/*      */     }
/*  724 */     return null;
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     private static final String CANCEL = "cancel";
/*      */     private static final String SELECT_NEXT = "selectNext";
/*      */     private static final String SELECT_PREVIOUS = "selectPrevious";
/*      */     private static final String SELECT_PARENT = "selectParent";
/*      */     private static final String SELECT_CHILD = "selectChild";
/*      */     private static final String RETURN = "return";
/*      */     private static final boolean FORWARD = true;
/*      */     private static final boolean BACKWARD = false;
/*      */     private static final boolean PARENT = false;
/*      */     private static final boolean CHILD = true;
/*      */ 
/*      */     Actions(String paramString)
/*      */     {
/*  408 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  412 */       String str = getName();
/*  413 */       if (str == "cancel") {
/*  414 */         cancel();
/*      */       }
/*  416 */       else if (str == "selectNext") {
/*  417 */         selectItem(true);
/*      */       }
/*  419 */       else if (str == "selectPrevious") {
/*  420 */         selectItem(false);
/*      */       }
/*  422 */       else if (str == "selectParent") {
/*  423 */         selectParentChild(false);
/*      */       }
/*  425 */       else if (str == "selectChild") {
/*  426 */         selectParentChild(true);
/*      */       }
/*  428 */       else if (str == "return")
/*  429 */         doReturn();
/*      */     }
/*      */ 
/*      */     private void doReturn()
/*      */     {
/*  434 */       KeyboardFocusManager localKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */ 
/*  436 */       Component localComponent = localKeyboardFocusManager.getFocusOwner();
/*  437 */       if ((localComponent != null) && (!(localComponent instanceof JRootPane))) {
/*  438 */         return;
/*      */       }
/*      */ 
/*  441 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  442 */       MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/*      */ 
/*  444 */       if (arrayOfMenuElement.length > 0) {
/*  445 */         MenuElement localMenuElement = arrayOfMenuElement[(arrayOfMenuElement.length - 1)];
/*      */         Object localObject;
/*  446 */         if ((localMenuElement instanceof JMenu)) {
/*  447 */           localObject = new MenuElement[arrayOfMenuElement.length + 1];
/*  448 */           System.arraycopy(arrayOfMenuElement, 0, localObject, 0, arrayOfMenuElement.length);
/*  449 */           localObject[arrayOfMenuElement.length] = ((JMenu)localMenuElement).getPopupMenu();
/*  450 */           localMenuSelectionManager.setSelectedPath((MenuElement[])localObject);
/*  451 */         } else if ((localMenuElement instanceof JMenuItem)) {
/*  452 */           localObject = (JMenuItem)localMenuElement;
/*      */ 
/*  454 */           if ((((JMenuItem)localObject).getUI() instanceof BasicMenuItemUI)) {
/*  455 */             ((BasicMenuItemUI)((JMenuItem)localObject).getUI()).doClick(localMenuSelectionManager);
/*      */           }
/*      */           else {
/*  458 */             localMenuSelectionManager.clearSelectedPath();
/*  459 */             ((JMenuItem)localObject).doClick(0);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  465 */     private void selectParentChild(boolean paramBoolean) { MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  466 */       MenuElement[] arrayOfMenuElement1 = localMenuSelectionManager.getSelectedPath();
/*  467 */       int i = arrayOfMenuElement1.length;
/*      */       Object localObject2;
/*      */       Object localObject1;
/*      */       MenuElement[] arrayOfMenuElement2;
/*  469 */       if (!paramBoolean)
/*      */       {
/*  471 */         int j = i - 1;
/*      */ 
/*  473 */         if ((i > 2) && (((arrayOfMenuElement1[j] instanceof JPopupMenu)) || ((arrayOfMenuElement1[(--j)] instanceof JPopupMenu))) && (!((JMenu)arrayOfMenuElement1[(j - 1)]).isTopLevelMenu()))
/*      */         {
/*  482 */           localObject2 = new MenuElement[j];
/*  483 */           System.arraycopy(arrayOfMenuElement1, 0, localObject2, 0, j);
/*  484 */           localMenuSelectionManager.setSelectedPath((MenuElement[])localObject2);
/*  485 */           return;
/*      */         }
/*      */ 
/*      */       }
/*  489 */       else if ((i > 0) && ((arrayOfMenuElement1[(i - 1)] instanceof JMenu)) && (!((JMenu)arrayOfMenuElement1[(i - 1)]).isTopLevelMenu()))
/*      */       {
/*  493 */         localObject1 = (JMenu)arrayOfMenuElement1[(i - 1)];
/*  494 */         localObject2 = ((JMenu)localObject1).getPopupMenu();
/*  495 */         arrayOfMenuElement2 = ((JPopupMenu)localObject2).getSubElements();
/*  496 */         MenuElement localMenuElement = BasicPopupMenuUI.findEnabledChild(arrayOfMenuElement2, -1, true);
/*      */         MenuElement[] arrayOfMenuElement3;
/*  499 */         if (localMenuElement == null) {
/*  500 */           arrayOfMenuElement3 = new MenuElement[i + 1];
/*      */         } else {
/*  502 */           arrayOfMenuElement3 = new MenuElement[i + 2];
/*  503 */           arrayOfMenuElement3[(i + 1)] = localMenuElement;
/*      */         }
/*  505 */         System.arraycopy(arrayOfMenuElement1, 0, arrayOfMenuElement3, 0, i);
/*  506 */         arrayOfMenuElement3[i] = localObject2;
/*  507 */         localMenuSelectionManager.setSelectedPath(arrayOfMenuElement3);
/*  508 */         return;
/*      */       }
/*      */ 
/*  514 */       if ((i > 1) && ((arrayOfMenuElement1[0] instanceof JMenuBar))) {
/*  515 */         localObject1 = arrayOfMenuElement1[1];
/*  516 */         localObject2 = BasicPopupMenuUI.findEnabledChild(arrayOfMenuElement1[0].getSubElements(), (MenuElement)localObject1, paramBoolean);
/*      */ 
/*  519 */         if ((localObject2 != null) && (localObject2 != localObject1))
/*      */         {
/*  521 */           if (i == 2)
/*      */           {
/*  523 */             arrayOfMenuElement2 = new MenuElement[2];
/*  524 */             arrayOfMenuElement2[0] = arrayOfMenuElement1[0];
/*  525 */             arrayOfMenuElement2[1] = localObject2;
/*      */           }
/*      */           else {
/*  528 */             arrayOfMenuElement2 = new MenuElement[3];
/*  529 */             arrayOfMenuElement2[0] = arrayOfMenuElement1[0];
/*  530 */             arrayOfMenuElement2[1] = localObject2;
/*  531 */             arrayOfMenuElement2[2] = ((JMenu)localObject2).getPopupMenu();
/*      */           }
/*  533 */           localMenuSelectionManager.setSelectedPath(arrayOfMenuElement2);
/*      */         }
/*      */       } }
/*      */ 
/*      */     private void selectItem(boolean paramBoolean)
/*      */     {
/*  539 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  540 */       MenuElement[] arrayOfMenuElement1 = localMenuSelectionManager.getSelectedPath();
/*  541 */       if (arrayOfMenuElement1.length == 0) {
/*  542 */         return;
/*      */       }
/*  544 */       int i = arrayOfMenuElement1.length;
/*      */       Object localObject1;
/*      */       Object localObject2;
/*  545 */       if ((i == 1) && ((arrayOfMenuElement1[0] instanceof JPopupMenu)))
/*      */       {
/*  547 */         localObject1 = (JPopupMenu)arrayOfMenuElement1[0];
/*  548 */         localObject2 = new MenuElement[2];
/*  549 */         localObject2[0] = localObject1;
/*  550 */         localObject2[1] = BasicPopupMenuUI.findEnabledChild(((JPopupMenu)localObject1).getSubElements(), -1, paramBoolean);
/*  551 */         localMenuSelectionManager.setSelectedPath((MenuElement[])localObject2);
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject3;
/*  552 */         if ((i == 2) && ((arrayOfMenuElement1[0] instanceof JMenuBar)) && ((arrayOfMenuElement1[1] instanceof JMenu)))
/*      */         {
/*  557 */           localObject1 = ((JMenu)arrayOfMenuElement1[1]).getPopupMenu();
/*  558 */           localObject2 = BasicPopupMenuUI.findEnabledChild(((JPopupMenu)localObject1).getSubElements(), -1, true);
/*      */ 
/*  562 */           if (localObject2 != null)
/*      */           {
/*  564 */             localObject3 = new MenuElement[4];
/*  565 */             localObject3[3] = localObject2;
/*      */           }
/*      */           else {
/*  568 */             localObject3 = new MenuElement[3];
/*      */           }
/*  570 */           System.arraycopy(arrayOfMenuElement1, 0, localObject3, 0, 2);
/*  571 */           localObject3[2] = localObject1;
/*  572 */           localMenuSelectionManager.setSelectedPath((MenuElement[])localObject3);
/*      */         }
/*  574 */         else if (((arrayOfMenuElement1[(i - 1)] instanceof JPopupMenu)) && ((arrayOfMenuElement1[(i - 2)] instanceof JMenu)))
/*      */         {
/*  579 */           localObject1 = (JMenu)arrayOfMenuElement1[(i - 2)];
/*  580 */           localObject2 = ((JMenu)localObject1).getPopupMenu();
/*  581 */           localObject3 = BasicPopupMenuUI.findEnabledChild(((JPopupMenu)localObject2).getSubElements(), -1, paramBoolean);
/*      */           MenuElement[] arrayOfMenuElement2;
/*  584 */           if (localObject3 != null) {
/*  585 */             arrayOfMenuElement2 = new MenuElement[i + 1];
/*  586 */             System.arraycopy(arrayOfMenuElement1, 0, arrayOfMenuElement2, 0, i);
/*  587 */             arrayOfMenuElement2[i] = localObject3;
/*  588 */             localMenuSelectionManager.setSelectedPath(arrayOfMenuElement2);
/*      */           }
/*  594 */           else if ((i > 2) && ((arrayOfMenuElement1[(i - 3)] instanceof JPopupMenu))) {
/*  595 */             localObject2 = (JPopupMenu)arrayOfMenuElement1[(i - 3)];
/*  596 */             localObject3 = BasicPopupMenuUI.findEnabledChild(((JPopupMenu)localObject2).getSubElements(), (MenuElement)localObject1, paramBoolean);
/*      */ 
/*  599 */             if ((localObject3 != null) && (localObject3 != localObject1)) {
/*  600 */               arrayOfMenuElement2 = new MenuElement[i - 1];
/*  601 */               System.arraycopy(arrayOfMenuElement1, 0, arrayOfMenuElement2, 0, i - 2);
/*  602 */               arrayOfMenuElement2[(i - 2)] = localObject3;
/*  603 */               localMenuSelectionManager.setSelectedPath(arrayOfMenuElement2);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  610 */           localObject1 = arrayOfMenuElement1[(i - 2)].getSubElements();
/*  611 */           localObject2 = BasicPopupMenuUI.findEnabledChild((MenuElement[])localObject1, arrayOfMenuElement1[(i - 1)], paramBoolean);
/*      */ 
/*  613 */           if (localObject2 == null) {
/*  614 */             localObject2 = BasicPopupMenuUI.findEnabledChild((MenuElement[])localObject1, -1, paramBoolean);
/*      */           }
/*  616 */           if (localObject2 != null) {
/*  617 */             arrayOfMenuElement1[(i - 1)] = localObject2;
/*  618 */             localMenuSelectionManager.setSelectedPath(arrayOfMenuElement1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void cancel()
/*      */     {
/*  627 */       JPopupMenu localJPopupMenu = BasicPopupMenuUI.getLastPopup();
/*  628 */       if (localJPopupMenu != null) {
/*  629 */         localJPopupMenu.putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.TRUE);
/*      */       }
/*  631 */       String str = UIManager.getString("Menu.cancelMode");
/*  632 */       if ("hideMenuTree".equals(str))
/*  633 */         MenuSelectionManager.defaultManager().clearSelectedPath();
/*      */       else
/*  635 */         shortenSelectedPath();
/*      */     }
/*      */ 
/*      */     private void shortenSelectedPath()
/*      */     {
/*  640 */       MenuElement[] arrayOfMenuElement = MenuSelectionManager.defaultManager().getSelectedPath();
/*  641 */       if (arrayOfMenuElement.length <= 2) {
/*  642 */         MenuSelectionManager.defaultManager().clearSelectedPath();
/*  643 */         return;
/*      */       }
/*      */ 
/*  646 */       int i = 2;
/*  647 */       MenuElement localMenuElement = arrayOfMenuElement[(arrayOfMenuElement.length - 1)];
/*  648 */       JPopupMenu localJPopupMenu = BasicPopupMenuUI.getLastPopup();
/*  649 */       if (localMenuElement == localJPopupMenu) {
/*  650 */         localObject = arrayOfMenuElement[(arrayOfMenuElement.length - 2)];
/*  651 */         if ((localObject instanceof JMenu)) {
/*  652 */           JMenu localJMenu = (JMenu)localObject;
/*  653 */           if ((localJMenu.isEnabled()) && (localJPopupMenu.getComponentCount() > 0))
/*      */           {
/*  655 */             i = 1;
/*      */           }
/*      */           else {
/*  658 */             i = 3;
/*      */           }
/*      */         }
/*      */       }
/*  662 */       if ((arrayOfMenuElement.length - i <= 2) && (!UIManager.getBoolean("Menu.preserveTopLevelSelection")))
/*      */       {
/*  665 */         i = arrayOfMenuElement.length;
/*      */       }
/*  667 */       Object localObject = new MenuElement[arrayOfMenuElement.length - i];
/*  668 */       System.arraycopy(arrayOfMenuElement, 0, localObject, 0, arrayOfMenuElement.length - i);
/*  669 */       MenuSelectionManager.defaultManager().setSelectedPath((MenuElement[])localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class BasicMenuKeyListener
/*      */     implements MenuKeyListener
/*      */   {
/*  284 */     MenuElement menuToOpen = null;
/*      */ 
/*      */     private BasicMenuKeyListener() {  } 
/*  287 */     public void menuKeyTyped(MenuKeyEvent paramMenuKeyEvent) { if (this.menuToOpen != null)
/*      */       {
/*  289 */         JPopupMenu localJPopupMenu = ((JMenu)this.menuToOpen).getPopupMenu();
/*  290 */         MenuElement localMenuElement = BasicPopupMenuUI.findEnabledChild(localJPopupMenu.getSubElements(), -1, true);
/*      */ 
/*  293 */         ArrayList localArrayList = new ArrayList(Arrays.asList(paramMenuKeyEvent.getPath()));
/*  294 */         localArrayList.add(this.menuToOpen);
/*  295 */         localArrayList.add(localJPopupMenu);
/*  296 */         if (localMenuElement != null) {
/*  297 */           localArrayList.add(localMenuElement);
/*      */         }
/*  299 */         MenuElement[] arrayOfMenuElement = new MenuElement[0];
/*  300 */         arrayOfMenuElement = (MenuElement[])localArrayList.toArray(arrayOfMenuElement);
/*  301 */         MenuSelectionManager.defaultManager().setSelectedPath(arrayOfMenuElement);
/*  302 */         paramMenuKeyEvent.consume();
/*      */       }
/*  304 */       this.menuToOpen = null; }
/*      */ 
/*      */     public void menuKeyPressed(MenuKeyEvent paramMenuKeyEvent)
/*      */     {
/*  308 */       char c = paramMenuKeyEvent.getKeyChar();
/*      */ 
/*  311 */       if (!Character.isLetterOrDigit(c)) {
/*  312 */         return;
/*      */       }
/*      */ 
/*  315 */       MenuSelectionManager localMenuSelectionManager = paramMenuKeyEvent.getMenuSelectionManager();
/*  316 */       MenuElement[] arrayOfMenuElement1 = paramMenuKeyEvent.getPath();
/*  317 */       MenuElement[] arrayOfMenuElement2 = BasicPopupMenuUI.this.popupMenu.getSubElements();
/*  318 */       int i = -1;
/*  319 */       int j = 0;
/*  320 */       int k = -1;
/*  321 */       int[] arrayOfInt = null;
/*      */       Object localObject2;
/*  323 */       for (int m = 0; m < arrayOfMenuElement2.length; m++) {
/*  324 */         if ((arrayOfMenuElement2[m] instanceof JMenuItem))
/*      */         {
/*  327 */           localObject2 = (JMenuItem)arrayOfMenuElement2[m];
/*  328 */           int n = ((JMenuItem)localObject2).getMnemonic();
/*  329 */           if ((((JMenuItem)localObject2).isEnabled()) && (((JMenuItem)localObject2).isVisible()) && (lower(c) == lower(n)))
/*      */           {
/*  331 */             if (j == 0) {
/*  332 */               k = m;
/*  333 */               j++;
/*      */             } else {
/*  335 */               if (arrayOfInt == null) {
/*  336 */                 arrayOfInt = new int[arrayOfMenuElement2.length];
/*  337 */                 arrayOfInt[0] = k;
/*      */               }
/*  339 */               arrayOfInt[(j++)] = m;
/*      */             }
/*      */           }
/*  342 */           if ((((JMenuItem)localObject2).isArmed()) || (((JMenuItem)localObject2).isSelected())) {
/*  343 */             i = j - 1;
/*      */           }
/*      */         }
/*      */       }
/*  347 */       if (j != 0)
/*      */       {
/*      */         Object localObject1;
/*  349 */         if (j == 1)
/*      */         {
/*  351 */           localObject1 = (JMenuItem)arrayOfMenuElement2[k];
/*  352 */           if ((localObject1 instanceof JMenu))
/*      */           {
/*  354 */             this.menuToOpen = ((MenuElement)localObject1);
/*  355 */           } else if (((JMenuItem)localObject1).isEnabled())
/*      */           {
/*  357 */             localMenuSelectionManager.clearSelectedPath();
/*  358 */             ((JMenuItem)localObject1).doClick();
/*      */           }
/*  360 */           paramMenuKeyEvent.consume();
/*      */         }
/*      */         else
/*      */         {
/*  367 */           localObject1 = arrayOfMenuElement2[arrayOfInt[((i + 1) % j)]];
/*      */ 
/*  369 */           localObject2 = new MenuElement[arrayOfMenuElement1.length + 1];
/*  370 */           System.arraycopy(arrayOfMenuElement1, 0, localObject2, 0, arrayOfMenuElement1.length);
/*  371 */           localObject2[arrayOfMenuElement1.length] = localObject1;
/*  372 */           localMenuSelectionManager.setSelectedPath((MenuElement[])localObject2);
/*  373 */           paramMenuKeyEvent.consume();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void menuKeyReleased(MenuKeyEvent paramMenuKeyEvent) {
/*      */     }
/*      */ 
/*  381 */     private char lower(char paramChar) { return Character.toLowerCase(paramChar); }
/*      */ 
/*      */     private char lower(int paramInt)
/*      */     {
/*  385 */       return Character.toLowerCase((char)paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class BasicPopupMenuListener
/*      */     implements PopupMenuListener
/*      */   {
/*      */     private BasicPopupMenuListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void popupMenuCanceled(PopupMenuEvent paramPopupMenuEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void popupMenuWillBecomeInvisible(PopupMenuEvent paramPopupMenuEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void popupMenuWillBecomeVisible(PopupMenuEvent paramPopupMenuEvent)
/*      */     {
/*  274 */       BasicLookAndFeel.playSound((JPopupMenu)paramPopupMenuEvent.getSource(), "PopupMenu.popupSound");
/*      */     }
/*      */   }
/*      */ 
/*      */   static class MenuKeyboardHelper
/*      */     implements ChangeListener, KeyListener
/*      */   {
/*  971 */     private Component lastFocused = null;
/*  972 */     private MenuElement[] lastPathSelected = new MenuElement[0];
/*      */     private JPopupMenu lastPopup;
/*      */     private JRootPane invokerRootPane;
/*  976 */     private ActionMap menuActionMap = BasicPopupMenuUI.getActionMap();
/*      */     private InputMap menuInputMap;
/*      */     private boolean focusTraversalKeysEnabled;
/*  986 */     private boolean receivedKeyPressed = false;
/*      */ 
/* 1017 */     private FocusListener rootPaneFocusListener = new FocusAdapter() {
/*      */       public void focusGained(FocusEvent paramAnonymousFocusEvent) {
/* 1019 */         Component localComponent = paramAnonymousFocusEvent.getOppositeComponent();
/* 1020 */         if (localComponent != null) {
/* 1021 */           BasicPopupMenuUI.MenuKeyboardHelper.this.lastFocused = localComponent;
/*      */         }
/* 1023 */         paramAnonymousFocusEvent.getComponent().removeFocusListener(this);
/*      */       }
/* 1017 */     };
/*      */ 
/*      */     void removeItems()
/*      */     {
/*  989 */       if (this.lastFocused != null) {
/*  990 */         if (!this.lastFocused.requestFocusInWindow())
/*      */         {
/*  996 */           Window localWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
/*      */ 
/*  999 */           if ((localWindow != null) && ("###focusableSwingPopup###".equals(localWindow.getName())))
/*      */           {
/* 1001 */             this.lastFocused.requestFocus();
/*      */           }
/*      */         }
/*      */ 
/* 1005 */         this.lastFocused = null;
/*      */       }
/* 1007 */       if (this.invokerRootPane != null) {
/* 1008 */         this.invokerRootPane.removeKeyListener(this);
/* 1009 */         this.invokerRootPane.setFocusTraversalKeysEnabled(this.focusTraversalKeysEnabled);
/* 1010 */         removeUIInputMap(this.invokerRootPane, this.menuInputMap);
/* 1011 */         removeUIActionMap(this.invokerRootPane, this.menuActionMap);
/* 1012 */         this.invokerRootPane = null;
/*      */       }
/* 1014 */       this.receivedKeyPressed = false;
/*      */     }
/*      */ 
/*      */     JPopupMenu getActivePopup(MenuElement[] paramArrayOfMenuElement)
/*      */     {
/* 1032 */       for (int i = paramArrayOfMenuElement.length - 1; i >= 0; i--) {
/* 1033 */         MenuElement localMenuElement = paramArrayOfMenuElement[i];
/* 1034 */         if ((localMenuElement instanceof JPopupMenu)) {
/* 1035 */           return (JPopupMenu)localMenuElement;
/*      */         }
/*      */       }
/* 1038 */       return null;
/*      */     }
/*      */ 
/*      */     void addUIInputMap(JComponent paramJComponent, InputMap paramInputMap) {
/* 1042 */       Object localObject = null;
/* 1043 */       InputMap localInputMap = paramJComponent.getInputMap(2);
/*      */ 
/* 1045 */       while ((localInputMap != null) && (!(localInputMap instanceof UIResource))) {
/* 1046 */         localObject = localInputMap;
/* 1047 */         localInputMap = localInputMap.getParent();
/*      */       }
/*      */ 
/* 1050 */       if (localObject == null)
/* 1051 */         paramJComponent.setInputMap(2, paramInputMap);
/*      */       else {
/* 1053 */         localObject.setParent(paramInputMap);
/*      */       }
/* 1055 */       paramInputMap.setParent(localInputMap);
/*      */     }
/*      */ 
/*      */     void addUIActionMap(JComponent paramJComponent, ActionMap paramActionMap) {
/* 1059 */       Object localObject = null;
/* 1060 */       ActionMap localActionMap = paramJComponent.getActionMap();
/*      */ 
/* 1062 */       while ((localActionMap != null) && (!(localActionMap instanceof UIResource))) {
/* 1063 */         localObject = localActionMap;
/* 1064 */         localActionMap = localActionMap.getParent();
/*      */       }
/*      */ 
/* 1067 */       if (localObject == null)
/* 1068 */         paramJComponent.setActionMap(paramActionMap);
/*      */       else {
/* 1070 */         localObject.setParent(paramActionMap);
/*      */       }
/* 1072 */       paramActionMap.setParent(localActionMap);
/*      */     }
/*      */ 
/*      */     void removeUIInputMap(JComponent paramJComponent, InputMap paramInputMap) {
/* 1076 */       Object localObject = null;
/* 1077 */       InputMap localInputMap = paramJComponent.getInputMap(2);
/*      */ 
/* 1079 */       while (localInputMap != null) {
/* 1080 */         if (localInputMap == paramInputMap) {
/* 1081 */           if (localObject == null) {
/* 1082 */             paramJComponent.setInputMap(2, paramInputMap.getParent()); break;
/*      */           }
/*      */ 
/* 1085 */           localObject.setParent(paramInputMap.getParent());
/*      */ 
/* 1087 */           break;
/*      */         }
/* 1089 */         localObject = localInputMap;
/* 1090 */         localInputMap = localInputMap.getParent();
/*      */       }
/*      */     }
/*      */ 
/*      */     void removeUIActionMap(JComponent paramJComponent, ActionMap paramActionMap) {
/* 1095 */       Object localObject = null;
/* 1096 */       ActionMap localActionMap = paramJComponent.getActionMap();
/*      */ 
/* 1098 */       while (localActionMap != null) {
/* 1099 */         if (localActionMap == paramActionMap) {
/* 1100 */           if (localObject == null) {
/* 1101 */             paramJComponent.setActionMap(paramActionMap.getParent()); break;
/*      */           }
/* 1103 */           localObject.setParent(paramActionMap.getParent());
/*      */ 
/* 1105 */           break;
/*      */         }
/* 1107 */         localObject = localActionMap;
/* 1108 */         localActionMap = localActionMap.getParent();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent) {
/* 1113 */       if (!(UIManager.getLookAndFeel() instanceof BasicLookAndFeel)) {
/* 1114 */         uninstall();
/* 1115 */         return;
/*      */       }
/* 1117 */       MenuSelectionManager localMenuSelectionManager = (MenuSelectionManager)paramChangeEvent.getSource();
/* 1118 */       MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/* 1119 */       JPopupMenu localJPopupMenu = getActivePopup(arrayOfMenuElement);
/* 1120 */       if ((localJPopupMenu != null) && (!localJPopupMenu.isFocusable()))
/*      */       {
/* 1122 */         return;
/*      */       }
/*      */ 
/* 1125 */       if ((this.lastPathSelected.length != 0) && (arrayOfMenuElement.length != 0) && 
/* 1126 */         (!BasicPopupMenuUI.checkInvokerEqual(arrayOfMenuElement[0], this.lastPathSelected[0]))) {
/* 1127 */         removeItems();
/* 1128 */         this.lastPathSelected = new MenuElement[0];
/*      */       }
/*      */ 
/* 1132 */       if ((this.lastPathSelected.length == 0) && (arrayOfMenuElement.length > 0))
/*      */       {
/*      */         Object localObject1;
/* 1136 */         if (localJPopupMenu == null) {
/* 1137 */           if ((arrayOfMenuElement.length == 2) && ((arrayOfMenuElement[0] instanceof JMenuBar)) && ((arrayOfMenuElement[1] instanceof JMenu)))
/*      */           {
/* 1140 */             localObject1 = (JComponent)arrayOfMenuElement[1];
/* 1141 */             localJPopupMenu = ((JMenu)localObject1).getPopupMenu();
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1146 */           Object localObject2 = localJPopupMenu.getInvoker();
/* 1147 */           if ((localObject2 instanceof JFrame)) {
/* 1148 */             localObject1 = ((JFrame)localObject2).getRootPane();
/* 1149 */           } else if ((localObject2 instanceof JDialog)) {
/* 1150 */             localObject1 = ((JDialog)localObject2).getRootPane();
/* 1151 */           } else if ((localObject2 instanceof JApplet)) {
/* 1152 */             localObject1 = ((JApplet)localObject2).getRootPane();
/*      */           } else {
/* 1154 */             while (!(localObject2 instanceof JComponent)) {
/* 1155 */               if (localObject2 == null) {
/* 1156 */                 return;
/*      */               }
/* 1158 */               localObject2 = ((Component)localObject2).getParent();
/*      */             }
/* 1160 */             localObject1 = (JComponent)localObject2;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1165 */         this.lastFocused = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */ 
/* 1170 */         this.invokerRootPane = SwingUtilities.getRootPane((Component)localObject1);
/* 1171 */         if (this.invokerRootPane != null) {
/* 1172 */           this.invokerRootPane.addFocusListener(this.rootPaneFocusListener);
/* 1173 */           this.invokerRootPane.requestFocus(true);
/* 1174 */           this.invokerRootPane.addKeyListener(this);
/* 1175 */           this.focusTraversalKeysEnabled = this.invokerRootPane.getFocusTraversalKeysEnabled();
/*      */ 
/* 1177 */           this.invokerRootPane.setFocusTraversalKeysEnabled(false);
/*      */ 
/* 1179 */           this.menuInputMap = BasicPopupMenuUI.getInputMap(localJPopupMenu, this.invokerRootPane);
/* 1180 */           addUIInputMap(this.invokerRootPane, this.menuInputMap);
/* 1181 */           addUIActionMap(this.invokerRootPane, this.menuActionMap);
/*      */         }
/* 1183 */       } else if ((this.lastPathSelected.length != 0) && (arrayOfMenuElement.length == 0))
/*      */       {
/* 1186 */         removeItems();
/*      */       }
/* 1188 */       else if (localJPopupMenu != this.lastPopup) {
/* 1189 */         this.receivedKeyPressed = false;
/*      */       }
/*      */ 
/* 1194 */       this.lastPathSelected = arrayOfMenuElement;
/* 1195 */       this.lastPopup = localJPopupMenu;
/*      */     }
/*      */ 
/*      */     public void keyPressed(KeyEvent paramKeyEvent) {
/* 1199 */       this.receivedKeyPressed = true;
/* 1200 */       MenuSelectionManager.defaultManager().processKeyEvent(paramKeyEvent);
/*      */     }
/*      */ 
/*      */     public void keyReleased(KeyEvent paramKeyEvent) {
/* 1204 */       if (this.receivedKeyPressed) {
/* 1205 */         this.receivedKeyPressed = false;
/* 1206 */         MenuSelectionManager.defaultManager().processKeyEvent(paramKeyEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void keyTyped(KeyEvent paramKeyEvent) {
/* 1211 */       if (this.receivedKeyPressed)
/* 1212 */         MenuSelectionManager.defaultManager().processKeyEvent(paramKeyEvent);
/*      */     }
/*      */ 
/*      */     void uninstall()
/*      */     {
/* 1217 */       synchronized (BasicPopupMenuUI.MENU_KEYBOARD_HELPER_KEY) {
/* 1218 */         MenuSelectionManager.defaultManager().removeChangeListener(this);
/* 1219 */         AppContext.getAppContext().remove(BasicPopupMenuUI.MENU_KEYBOARD_HELPER_KEY);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class MouseGrabber
/*      */     implements ChangeListener, AWTEventListener, ComponentListener, WindowListener
/*      */   {
/*      */     Window grabbedWindow;
/*      */     MenuElement[] lastPathSelected;
/*      */ 
/*      */     public MouseGrabber()
/*      */     {
/*  734 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  735 */       localMenuSelectionManager.addChangeListener(this);
/*  736 */       this.lastPathSelected = localMenuSelectionManager.getSelectedPath();
/*  737 */       if (this.lastPathSelected.length != 0)
/*  738 */         grabWindow(this.lastPathSelected);
/*      */     }
/*      */ 
/*      */     void uninstall()
/*      */     {
/*  743 */       synchronized (BasicPopupMenuUI.MOUSE_GRABBER_KEY) {
/*  744 */         MenuSelectionManager.defaultManager().removeChangeListener(this);
/*  745 */         ungrabWindow();
/*  746 */         AppContext.getAppContext().remove(BasicPopupMenuUI.MOUSE_GRABBER_KEY);
/*      */       }
/*      */     }
/*      */ 
/*      */     void grabWindow(MenuElement[] paramArrayOfMenuElement)
/*      */     {
/*  752 */       final Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  753 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/*  756 */           localToolkit.addAWTEventListener(BasicPopupMenuUI.MouseGrabber.this, -2147352464L);
/*      */ 
/*  761 */           return null;
/*      */         }
/*      */       });
/*  766 */       Component localComponent = paramArrayOfMenuElement[0].getComponent();
/*  767 */       if ((localComponent instanceof JPopupMenu)) {
/*  768 */         localComponent = ((JPopupMenu)localComponent).getInvoker();
/*      */       }
/*  770 */       this.grabbedWindow = ((localComponent instanceof Window) ? (Window)localComponent : SwingUtilities.getWindowAncestor(localComponent));
/*      */ 
/*  773 */       if (this.grabbedWindow != null)
/*  774 */         if ((localToolkit instanceof SunToolkit)) {
/*  775 */           ((SunToolkit)localToolkit).grab(this.grabbedWindow);
/*      */         } else {
/*  777 */           this.grabbedWindow.addComponentListener(this);
/*  778 */           this.grabbedWindow.addWindowListener(this);
/*      */         }
/*      */     }
/*      */ 
/*      */     void ungrabWindow()
/*      */     {
/*  784 */       final Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*      */ 
/*  786 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/*  789 */           localToolkit.removeAWTEventListener(BasicPopupMenuUI.MouseGrabber.this);
/*  790 */           return null;
/*      */         }
/*      */       });
/*  794 */       realUngrabWindow();
/*      */     }
/*      */ 
/*      */     void realUngrabWindow() {
/*  798 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  799 */       if (this.grabbedWindow != null) {
/*  800 */         if ((localToolkit instanceof SunToolkit)) {
/*  801 */           ((SunToolkit)localToolkit).ungrab(this.grabbedWindow);
/*      */         } else {
/*  803 */           this.grabbedWindow.removeComponentListener(this);
/*  804 */           this.grabbedWindow.removeWindowListener(this);
/*      */         }
/*  806 */         this.grabbedWindow = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent) {
/*  811 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*  812 */       MenuElement[] arrayOfMenuElement = localMenuSelectionManager.getSelectedPath();
/*      */ 
/*  814 */       if ((this.lastPathSelected.length == 0) && (arrayOfMenuElement.length != 0)) {
/*  815 */         grabWindow(arrayOfMenuElement);
/*      */       }
/*      */ 
/*  818 */       if ((this.lastPathSelected.length != 0) && (arrayOfMenuElement.length == 0)) {
/*  819 */         ungrabWindow();
/*      */       }
/*      */ 
/*  822 */       this.lastPathSelected = arrayOfMenuElement;
/*      */     }
/*      */ 
/*      */     public void eventDispatched(AWTEvent paramAWTEvent) {
/*  826 */       if ((paramAWTEvent instanceof UngrabEvent))
/*      */       {
/*  828 */         cancelPopupMenu();
/*  829 */         return;
/*      */       }
/*  831 */       if (!(paramAWTEvent instanceof MouseEvent))
/*      */       {
/*  833 */         return;
/*      */       }
/*  835 */       MouseEvent localMouseEvent = (MouseEvent)paramAWTEvent;
/*  836 */       Component localComponent = localMouseEvent.getComponent();
/*  837 */       switch (localMouseEvent.getID()) {
/*      */       case 501:
/*  839 */         if ((isInPopup(localComponent)) || (((localComponent instanceof JMenu)) && (((JMenu)localComponent).isSelected())))
/*      */         {
/*  841 */           return;
/*      */         }
/*  843 */         if ((!(localComponent instanceof JComponent)) || (((JComponent)localComponent).getClientProperty("doNotCancelPopup") != BasicComboBoxUI.HIDE_POPUP_KEY))
/*      */         {
/*  849 */           cancelPopupMenu();
/*      */ 
/*  852 */           boolean bool = UIManager.getBoolean("PopupMenu.consumeEventOnClose");
/*      */ 
/*  855 */           if ((bool) && (!(localComponent instanceof MenuElement)))
/*  856 */             localMouseEvent.consume();
/*      */         }
/*  858 */         break;
/*      */       case 502:
/*  862 */         if (((localComponent instanceof MenuElement)) || 
/*  864 */           (!isInPopup(localComponent)))
/*      */         {
/*  868 */           if (((localComponent instanceof JMenu)) || (!(localComponent instanceof JMenuItem)))
/*  869 */             MenuSelectionManager.defaultManager().processMouseEvent(localMouseEvent);  } break;
/*      */       case 506:
/*  874 */         if (((localComponent instanceof MenuElement)) || 
/*  879 */           (!isInPopup(localComponent)))
/*      */         {
/*  883 */           MenuSelectionManager.defaultManager().processMouseEvent(localMouseEvent);
/*      */         }
/*  885 */         break;
/*      */       case 507:
/*  887 */         if (isInPopup(localComponent)) {
/*  888 */           return;
/*      */         }
/*  890 */         cancelPopupMenu();
/*      */       case 503:
/*      */       case 504:
/*      */       case 505:
/*      */       }
/*      */     }
/*  896 */     boolean isInPopup(Component paramComponent) { for (Object localObject = paramComponent; (localObject != null) && 
/*  897 */         (!(localObject instanceof Applet)) && (!(localObject instanceof Window)); localObject = ((Component)localObject).getParent())
/*      */       {
/*  899 */         if ((localObject instanceof JPopupMenu)) {
/*  900 */           return true;
/*      */         }
/*      */       }
/*  903 */       return false;
/*      */     }
/*      */ 
/*      */     void cancelPopupMenu()
/*      */     {
/*      */       try
/*      */       {
/*  913 */         List localList = BasicPopupMenuUI.getPopups();
/*  914 */         for (JPopupMenu localJPopupMenu : localList) {
/*  915 */           localJPopupMenu.putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.TRUE);
/*      */         }
/*  917 */         MenuSelectionManager.defaultManager().clearSelectedPath();
/*      */       } catch (RuntimeException localRuntimeException) {
/*  919 */         realUngrabWindow();
/*  920 */         throw localRuntimeException;
/*      */       } catch (Error localError) {
/*  922 */         realUngrabWindow();
/*  923 */         throw localError;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void componentResized(ComponentEvent paramComponentEvent) {
/*  928 */       cancelPopupMenu();
/*      */     }
/*      */     public void componentMoved(ComponentEvent paramComponentEvent) {
/*  931 */       cancelPopupMenu();
/*      */     }
/*      */     public void componentShown(ComponentEvent paramComponentEvent) {
/*  934 */       cancelPopupMenu();
/*      */     }
/*      */     public void componentHidden(ComponentEvent paramComponentEvent) {
/*  937 */       cancelPopupMenu();
/*      */     }
/*      */     public void windowClosing(WindowEvent paramWindowEvent) {
/*  940 */       cancelPopupMenu();
/*      */     }
/*      */     public void windowClosed(WindowEvent paramWindowEvent) {
/*  943 */       cancelPopupMenu();
/*      */     }
/*      */     public void windowIconified(WindowEvent paramWindowEvent) {
/*  946 */       cancelPopupMenu();
/*      */     }
/*      */     public void windowDeactivated(WindowEvent paramWindowEvent) {
/*  949 */       cancelPopupMenu();
/*      */     }
/*      */ 
/*      */     public void windowOpened(WindowEvent paramWindowEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void windowDeiconified(WindowEvent paramWindowEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void windowActivated(WindowEvent paramWindowEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicPopupMenuUI
 * JD-Core Version:    0.6.2
 */