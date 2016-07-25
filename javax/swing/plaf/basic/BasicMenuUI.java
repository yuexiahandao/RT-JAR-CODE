/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.MenuDragMouseEvent;
/*     */ import javax.swing.event.MenuDragMouseListener;
/*     */ import javax.swing.event.MenuKeyEvent;
/*     */ import javax.swing.event.MenuKeyListener;
/*     */ import javax.swing.event.MenuListener;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.UIAction;
/*     */ 
/*     */ public class BasicMenuUI extends BasicMenuItemUI
/*     */ {
/*     */   protected ChangeListener changeListener;
/*     */   protected MenuListener menuListener;
/*     */   private int lastMnemonic;
/*     */   private InputMap selectedWindowInputMap;
/*     */   private static final boolean TRACE = false;
/*     */   private static final boolean VERBOSE = false;
/*     */   private static final boolean DEBUG = false;
/*  64 */   private static boolean crossMenuMnemonic = true;
/*     */ 
/*     */   public BasicMenuUI()
/*     */   {
/*  54 */     this.lastMnemonic = 0;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  67 */     return new BasicMenuUI();
/*     */   }
/*     */ 
/*     */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/*  71 */     BasicMenuItemUI.loadActionMap(paramLazyActionMap);
/*  72 */     paramLazyActionMap.put(new Actions("selectMenu", null, true));
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  77 */     super.installDefaults();
/*  78 */     updateDefaultBackgroundColor();
/*  79 */     ((JMenu)this.menuItem).setDelay(200);
/*  80 */     crossMenuMnemonic = UIManager.getBoolean("Menu.crossMenuMnemonic");
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix() {
/*  84 */     return "Menu";
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/*  88 */     super.installListeners();
/*     */ 
/*  90 */     if (this.changeListener == null) {
/*  91 */       this.changeListener = createChangeListener(this.menuItem);
/*     */     }
/*  93 */     if (this.changeListener != null) {
/*  94 */       this.menuItem.addChangeListener(this.changeListener);
/*     */     }
/*  96 */     if (this.menuListener == null) {
/*  97 */       this.menuListener = createMenuListener(this.menuItem);
/*     */     }
/*  99 */     if (this.menuListener != null)
/* 100 */       ((JMenu)this.menuItem).addMenuListener(this.menuListener);
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions() {
/* 104 */     super.installKeyboardActions();
/* 105 */     updateMnemonicBinding();
/*     */   }
/*     */ 
/*     */   void installLazyActionMap() {
/* 109 */     LazyActionMap.installLazyActionMap(this.menuItem, BasicMenuUI.class, getPropertyPrefix() + ".actionMap");
/*     */   }
/*     */ 
/*     */   void updateMnemonicBinding()
/*     */   {
/* 114 */     int i = this.menuItem.getModel().getMnemonic();
/* 115 */     int[] arrayOfInt1 = (int[])DefaultLookup.get(this.menuItem, this, "Menu.shortcutKeys");
/*     */ 
/* 117 */     if (arrayOfInt1 == null) {
/* 118 */       arrayOfInt1 = new int[] { 8 };
/*     */     }
/* 120 */     if (i == this.lastMnemonic) {
/* 121 */       return;
/*     */     }
/* 123 */     InputMap localInputMap = SwingUtilities.getUIInputMap(this.menuItem, 2);
/*     */     int m;
/* 125 */     if ((this.lastMnemonic != 0) && (localInputMap != null)) {
/* 126 */       for (m : arrayOfInt1) {
/* 127 */         localInputMap.remove(KeyStroke.getKeyStroke(this.lastMnemonic, m, false));
/*     */       }
/*     */     }
/*     */ 
/* 131 */     if (i != 0) {
/* 132 */       if (localInputMap == null) {
/* 133 */         localInputMap = createInputMap(2);
/*     */ 
/* 135 */         SwingUtilities.replaceUIInputMap(this.menuItem, 2, localInputMap);
/*     */       }
/*     */ 
/* 138 */       for (m : arrayOfInt1) {
/* 139 */         localInputMap.put(KeyStroke.getKeyStroke(i, m, false), "selectMenu");
/*     */       }
/*     */     }
/*     */ 
/* 143 */     this.lastMnemonic = i;
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions() {
/* 147 */     super.uninstallKeyboardActions();
/* 148 */     this.lastMnemonic = 0;
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener(JComponent paramJComponent) {
/* 152 */     return getHandler();
/*     */   }
/*     */ 
/*     */   protected MenuListener createMenuListener(JComponent paramJComponent) {
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */   protected ChangeListener createChangeListener(JComponent paramJComponent) {
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener(JComponent paramJComponent) {
/* 164 */     return getHandler();
/*     */   }
/*     */ 
/*     */   BasicMenuItemUI.Handler getHandler() {
/* 168 */     if (this.handler == null) {
/* 169 */       this.handler = new Handler(null);
/*     */     }
/* 171 */     return this.handler;
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults() {
/* 175 */     this.menuItem.setArmed(false);
/* 176 */     this.menuItem.setSelected(false);
/* 177 */     this.menuItem.resetKeyboardActions();
/* 178 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/* 182 */     super.uninstallListeners();
/*     */ 
/* 184 */     if (this.changeListener != null) {
/* 185 */       this.menuItem.removeChangeListener(this.changeListener);
/*     */     }
/* 187 */     if (this.menuListener != null) {
/* 188 */       ((JMenu)this.menuItem).removeMenuListener(this.menuListener);
/*     */     }
/* 190 */     this.changeListener = null;
/* 191 */     this.menuListener = null;
/* 192 */     this.handler = null;
/*     */   }
/*     */ 
/*     */   protected MenuDragMouseListener createMenuDragMouseListener(JComponent paramJComponent) {
/* 196 */     return getHandler();
/*     */   }
/*     */ 
/*     */   protected MenuKeyListener createMenuKeyListener(JComponent paramJComponent) {
/* 200 */     return (MenuKeyListener)getHandler();
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent) {
/* 204 */     if (((JMenu)this.menuItem).isTopLevelMenu() == true) {
/* 205 */       Dimension localDimension = paramJComponent.getPreferredSize();
/* 206 */       return new Dimension(localDimension.width, 32767);
/*     */     }
/* 208 */     return null;
/*     */   }
/*     */ 
/*     */   protected void setupPostTimer(JMenu paramJMenu) {
/* 212 */     Timer localTimer = new Timer(paramJMenu.getDelay(), new Actions("selectMenu", paramJMenu, false));
/*     */ 
/* 214 */     localTimer.setRepeats(false);
/* 215 */     localTimer.start();
/*     */   }
/*     */ 
/*     */   private static void appendPath(MenuElement[] paramArrayOfMenuElement, MenuElement paramMenuElement) {
/* 219 */     MenuElement[] arrayOfMenuElement = new MenuElement[paramArrayOfMenuElement.length + 1];
/* 220 */     System.arraycopy(paramArrayOfMenuElement, 0, arrayOfMenuElement, 0, paramArrayOfMenuElement.length);
/* 221 */     arrayOfMenuElement[paramArrayOfMenuElement.length] = paramMenuElement;
/* 222 */     MenuSelectionManager.defaultManager().setSelectedPath(arrayOfMenuElement);
/*     */   }
/*     */ 
/*     */   private void updateDefaultBackgroundColor()
/*     */   {
/* 298 */     if (!UIManager.getBoolean("Menu.useMenuBarBackgroundForTopLevel")) {
/* 299 */       return;
/*     */     }
/* 301 */     JMenu localJMenu = (JMenu)this.menuItem;
/* 302 */     if ((localJMenu.getBackground() instanceof UIResource))
/* 303 */       if (localJMenu.isTopLevelMenu())
/* 304 */         localJMenu.setBackground(UIManager.getColor("MenuBar.background"));
/*     */       else
/* 306 */         localJMenu.setBackground(UIManager.getColor(getPropertyPrefix() + ".background"));
/*     */   }
/*     */ 
/*     */   private static class Actions extends UIAction
/*     */   {
/*     */     private static final String SELECT = "selectMenu";
/*     */     private JMenu menu;
/* 231 */     private boolean force = false;
/*     */ 
/*     */     Actions(String paramString, JMenu paramJMenu, boolean paramBoolean) {
/* 234 */       super();
/* 235 */       this.menu = paramJMenu;
/* 236 */       this.force = paramBoolean;
/*     */     }
/*     */ 
/*     */     private JMenu getMenu(ActionEvent paramActionEvent) {
/* 240 */       if ((paramActionEvent.getSource() instanceof JMenu)) {
/* 241 */         return (JMenu)paramActionEvent.getSource();
/*     */       }
/* 243 */       return this.menu;
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 247 */       JMenu localJMenu = getMenu(paramActionEvent);
/* 248 */       if (!BasicMenuUI.crossMenuMnemonic) {
/* 249 */         localObject1 = BasicPopupMenuUI.getLastPopup();
/* 250 */         if ((localObject1 != null) && (localObject1 != localJMenu.getParent())) {
/* 251 */           return;
/*     */         }
/*     */       }
/*     */ 
/* 255 */       Object localObject1 = MenuSelectionManager.defaultManager();
/*     */       Object localObject2;
/* 256 */       if (this.force) {
/* 257 */         localObject2 = localJMenu.getParent();
/* 258 */         if ((localObject2 != null) && ((localObject2 instanceof JMenuBar)))
/*     */         {
/* 262 */           MenuElement[] arrayOfMenuElement2 = localJMenu.getPopupMenu().getSubElements();
/*     */           MenuElement[] arrayOfMenuElement1;
/* 263 */           if (arrayOfMenuElement2.length > 0) {
/* 264 */             arrayOfMenuElement1 = new MenuElement[4];
/* 265 */             arrayOfMenuElement1[0] = ((MenuElement)localObject2);
/* 266 */             arrayOfMenuElement1[1] = localJMenu;
/* 267 */             arrayOfMenuElement1[2] = localJMenu.getPopupMenu();
/* 268 */             arrayOfMenuElement1[3] = arrayOfMenuElement2[0];
/*     */           } else {
/* 270 */             arrayOfMenuElement1 = new MenuElement[3];
/* 271 */             arrayOfMenuElement1[0] = ((MenuElement)localObject2);
/* 272 */             arrayOfMenuElement1[1] = localJMenu;
/* 273 */             arrayOfMenuElement1[2] = localJMenu.getPopupMenu();
/*     */           }
/* 275 */           ((MenuSelectionManager)localObject1).setSelectedPath(arrayOfMenuElement1);
/*     */         }
/*     */       } else {
/* 278 */         localObject2 = ((MenuSelectionManager)localObject1).getSelectedPath();
/* 279 */         if ((localObject2.length > 0) && (localObject2[(localObject2.length - 1)] == localJMenu))
/* 280 */           BasicMenuUI.appendPath((MenuElement[])localObject2, localJMenu.getPopupMenu());
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isEnabled(Object paramObject)
/*     */     {
/* 286 */       if ((paramObject instanceof JMenu)) {
/* 287 */         return ((JMenu)paramObject).isEnabled();
/*     */       }
/* 289 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class ChangeHandler
/*     */     implements ChangeListener
/*     */   {
/*     */     public JMenu menu;
/*     */     public BasicMenuUI ui;
/* 393 */     public boolean isSelected = false;
/*     */     public Component wasFocused;
/*     */ 
/*     */     public ChangeHandler(JMenu paramBasicMenuUI, BasicMenuUI arg3)
/*     */     {
/* 397 */       this.menu = paramBasicMenuUI;
/*     */       Object localObject;
/* 398 */       this.ui = localObject;
/*     */     }
/*     */     public void stateChanged(ChangeEvent paramChangeEvent) {
/*     */     }
/*     */   }
/*     */   private class Handler extends BasicMenuItemUI.Handler implements MenuKeyListener {
/* 404 */     private Handler() { super(); }
/*     */ 
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 409 */       if (paramPropertyChangeEvent.getPropertyName() == "mnemonic")
/*     */       {
/* 411 */         BasicMenuUI.this.updateMnemonicBinding();
/*     */       }
/*     */       else {
/* 414 */         if (paramPropertyChangeEvent.getPropertyName().equals("ancestor")) {
/* 415 */           BasicMenuUI.this.updateDefaultBackgroundColor();
/*     */         }
/* 417 */         super.propertyChange(paramPropertyChangeEvent);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/* 435 */       JMenu localJMenu = (JMenu)BasicMenuUI.this.menuItem;
/* 436 */       if (!localJMenu.isEnabled()) {
/* 437 */         return;
/*     */       }
/* 439 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 441 */       if (localJMenu.isTopLevelMenu()) {
/* 442 */         if ((localJMenu.isSelected()) && (localJMenu.getPopupMenu().isShowing())) {
/* 443 */           localMenuSelectionManager.clearSelectedPath();
/*     */         } else {
/* 445 */           localObject = localJMenu.getParent();
/* 446 */           if ((localObject != null) && ((localObject instanceof JMenuBar))) {
/* 447 */             MenuElement[] arrayOfMenuElement = new MenuElement[2];
/* 448 */             arrayOfMenuElement[0] = ((MenuElement)localObject);
/* 449 */             arrayOfMenuElement[1] = localJMenu;
/* 450 */             localMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 455 */       Object localObject = localMenuSelectionManager.getSelectedPath();
/* 456 */       if ((localObject.length > 0) && (localObject[(localObject.length - 1)] != localJMenu.getPopupMenu()))
/*     */       {
/* 459 */         if ((localJMenu.isTopLevelMenu()) || (localJMenu.getDelay() == 0))
/*     */         {
/* 461 */           BasicMenuUI.appendPath((MenuElement[])localObject, localJMenu.getPopupMenu());
/*     */         }
/* 463 */         else BasicMenuUI.this.setupPostTimer(localJMenu);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent)
/*     */     {
/* 475 */       JMenu localJMenu = (JMenu)BasicMenuUI.this.menuItem;
/* 476 */       if (!localJMenu.isEnabled())
/* 477 */         return;
/* 478 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 480 */       localMenuSelectionManager.processMouseEvent(paramMouseEvent);
/* 481 */       if (!paramMouseEvent.isConsumed())
/* 482 */         localMenuSelectionManager.clearSelectedPath();
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent paramMouseEvent)
/*     */     {
/* 494 */       JMenu localJMenu = (JMenu)BasicMenuUI.this.menuItem;
/*     */ 
/* 497 */       if ((!localJMenu.isEnabled()) && (!UIManager.getBoolean("MenuItem.disabledAreNavigable"))) {
/* 498 */         return;
/*     */       }
/*     */ 
/* 501 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 503 */       MenuElement[] arrayOfMenuElement1 = localMenuSelectionManager.getSelectedPath();
/* 504 */       if (!localJMenu.isTopLevelMenu()) {
/* 505 */         if ((arrayOfMenuElement1.length <= 0) || (arrayOfMenuElement1[(arrayOfMenuElement1.length - 1)] != localJMenu.getPopupMenu()))
/*     */         {
/* 508 */           if (localJMenu.getDelay() == 0) {
/* 509 */             BasicMenuUI.appendPath(BasicMenuUI.this.getPath(), localJMenu.getPopupMenu());
/*     */           } else {
/* 511 */             localMenuSelectionManager.setSelectedPath(BasicMenuUI.this.getPath());
/* 512 */             BasicMenuUI.this.setupPostTimer(localJMenu);
/*     */           }
/*     */         }
/*     */       }
/* 516 */       else if ((arrayOfMenuElement1.length > 0) && (arrayOfMenuElement1[0] == localJMenu.getParent()))
/*     */       {
/* 518 */         MenuElement[] arrayOfMenuElement2 = new MenuElement[3];
/*     */ 
/* 521 */         arrayOfMenuElement2[0] = ((MenuElement)localJMenu.getParent());
/* 522 */         arrayOfMenuElement2[1] = localJMenu;
/* 523 */         if (BasicPopupMenuUI.getLastPopup() != null) {
/* 524 */           arrayOfMenuElement2[2] = localJMenu.getPopupMenu();
/*     */         }
/* 526 */         localMenuSelectionManager.setSelectedPath(arrayOfMenuElement2);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent paramMouseEvent)
/*     */     {
/* 541 */       JMenu localJMenu = (JMenu)BasicMenuUI.this.menuItem;
/* 542 */       if (!localJMenu.isEnabled())
/* 543 */         return;
/* 544 */       MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void menuDragMouseEntered(MenuDragMouseEvent paramMenuDragMouseEvent) {
/*     */     }
/*     */ 
/*     */     public void menuDragMouseDragged(MenuDragMouseEvent paramMenuDragMouseEvent) {
/* 555 */       if (!BasicMenuUI.this.menuItem.isEnabled()) {
/* 556 */         return;
/*     */       }
/* 558 */       MenuSelectionManager localMenuSelectionManager = paramMenuDragMouseEvent.getMenuSelectionManager();
/* 559 */       MenuElement[] arrayOfMenuElement1 = paramMenuDragMouseEvent.getPath();
/*     */ 
/* 561 */       Point localPoint = paramMenuDragMouseEvent.getPoint();
/*     */       Object localObject;
/* 562 */       if ((localPoint.x >= 0) && (localPoint.x < BasicMenuUI.this.menuItem.getWidth()) && (localPoint.y >= 0) && (localPoint.y < BasicMenuUI.this.menuItem.getHeight()))
/*     */       {
/* 564 */         localObject = (JMenu)BasicMenuUI.this.menuItem;
/* 565 */         MenuElement[] arrayOfMenuElement2 = localMenuSelectionManager.getSelectedPath();
/* 566 */         if ((arrayOfMenuElement2.length <= 0) || (arrayOfMenuElement2[(arrayOfMenuElement2.length - 1)] != ((JMenu)localObject).getPopupMenu()))
/*     */         {
/* 569 */           if ((((JMenu)localObject).isTopLevelMenu()) || (((JMenu)localObject).getDelay() == 0) || (paramMenuDragMouseEvent.getID() == 506))
/*     */           {
/* 572 */             BasicMenuUI.appendPath(arrayOfMenuElement1, ((JMenu)localObject).getPopupMenu());
/*     */           } else {
/* 574 */             localMenuSelectionManager.setSelectedPath(arrayOfMenuElement1);
/* 575 */             BasicMenuUI.this.setupPostTimer((JMenu)localObject);
/*     */           }
/*     */         }
/* 578 */       } else if (paramMenuDragMouseEvent.getID() == 502) {
/* 579 */         localObject = localMenuSelectionManager.componentForPoint(paramMenuDragMouseEvent.getComponent(), paramMenuDragMouseEvent.getPoint());
/* 580 */         if (localObject == null)
/* 581 */           localMenuSelectionManager.clearSelectedPath();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void menuDragMouseExited(MenuDragMouseEvent paramMenuDragMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void menuDragMouseReleased(MenuDragMouseEvent paramMenuDragMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void menuKeyTyped(MenuKeyEvent paramMenuKeyEvent)
/*     */     {
/* 595 */       if ((!BasicMenuUI.crossMenuMnemonic) && (BasicPopupMenuUI.getLastPopup() != null))
/*     */       {
/* 598 */         return;
/*     */       }
/*     */ 
/* 601 */       if (BasicPopupMenuUI.getPopups().size() != 0)
/*     */       {
/* 605 */         return;
/*     */       }
/*     */ 
/* 608 */       int i = Character.toLowerCase((char)BasicMenuUI.this.menuItem.getMnemonic());
/* 609 */       MenuElement[] arrayOfMenuElement1 = paramMenuKeyEvent.getPath();
/* 610 */       if (i == Character.toLowerCase(paramMenuKeyEvent.getKeyChar())) {
/* 611 */         JPopupMenu localJPopupMenu = ((JMenu)BasicMenuUI.this.menuItem).getPopupMenu();
/* 612 */         ArrayList localArrayList = new ArrayList(Arrays.asList(arrayOfMenuElement1));
/* 613 */         localArrayList.add(localJPopupMenu);
/* 614 */         MenuElement[] arrayOfMenuElement2 = localJPopupMenu.getSubElements();
/* 615 */         MenuElement localMenuElement = BasicPopupMenuUI.findEnabledChild(arrayOfMenuElement2, -1, true);
/*     */ 
/* 617 */         if (localMenuElement != null) {
/* 618 */           localArrayList.add(localMenuElement);
/*     */         }
/* 620 */         MenuSelectionManager localMenuSelectionManager = paramMenuKeyEvent.getMenuSelectionManager();
/* 621 */         MenuElement[] arrayOfMenuElement3 = new MenuElement[0];
/* 622 */         arrayOfMenuElement3 = (MenuElement[])localArrayList.toArray(arrayOfMenuElement3);
/* 623 */         localMenuSelectionManager.setSelectedPath(arrayOfMenuElement3);
/* 624 */         paramMenuKeyEvent.consume();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void menuKeyPressed(MenuKeyEvent paramMenuKeyEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void menuKeyReleased(MenuKeyEvent paramMenuKeyEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MouseInputHandler
/*     */     implements MouseInputListener
/*     */   {
/*     */     protected MouseInputHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent paramMouseEvent)
/*     */     {
/* 331 */       BasicMenuUI.this.getHandler().mouseClicked(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/* 342 */       BasicMenuUI.this.getHandler().mousePressed(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent)
/*     */     {
/* 352 */       BasicMenuUI.this.getHandler().mouseReleased(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent paramMouseEvent)
/*     */     {
/* 364 */       BasicMenuUI.this.getHandler().mouseEntered(paramMouseEvent);
/*     */     }
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 367 */       BasicMenuUI.this.getHandler().mouseExited(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent paramMouseEvent)
/*     */     {
/* 378 */       BasicMenuUI.this.getHandler().mouseDragged(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 382 */       BasicMenuUI.this.getHandler().mouseMoved(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicMenuUI
 * JD-Core Version:    0.6.2
 */