/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.MenuDragMouseEvent;
/*     */ import javax.swing.event.MenuDragMouseListener;
/*     */ import javax.swing.event.MenuKeyEvent;
/*     */ import javax.swing.event.MenuKeyListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.MenuItemUI;
/*     */ 
/*     */ public class JMenuItem extends AbstractButton
/*     */   implements Accessible, MenuElement
/*     */ {
/*     */   private static final String uiClassID = "MenuItemUI";
/*     */   private static final boolean TRACE = false;
/*     */   private static final boolean VERBOSE = false;
/*     */   private static final boolean DEBUG = false;
/* 103 */   private boolean isMouseDragged = false;
/*     */   private KeyStroke accelerator;
/*     */ 
/*     */   public JMenuItem()
/*     */   {
/* 109 */     this(null, (Icon)null);
/*     */   }
/*     */ 
/*     */   public JMenuItem(Icon paramIcon)
/*     */   {
/* 118 */     this(null, paramIcon);
/*     */   }
/*     */ 
/*     */   public JMenuItem(String paramString)
/*     */   {
/* 127 */     this(paramString, (Icon)null);
/*     */   }
/*     */ 
/*     */   public JMenuItem(Action paramAction)
/*     */   {
/* 138 */     this();
/* 139 */     setAction(paramAction);
/*     */   }
/*     */ 
/*     */   public JMenuItem(String paramString, Icon paramIcon)
/*     */   {
/* 149 */     setModel(new DefaultButtonModel());
/* 150 */     init(paramString, paramIcon);
/* 151 */     initFocusability();
/*     */   }
/*     */ 
/*     */   public JMenuItem(String paramString, int paramInt)
/*     */   {
/* 162 */     setModel(new DefaultButtonModel());
/* 163 */     init(paramString, null);
/* 164 */     setMnemonic(paramInt);
/* 165 */     initFocusability();
/*     */   }
/*     */ 
/*     */   public void setModel(ButtonModel paramButtonModel)
/*     */   {
/* 172 */     super.setModel(paramButtonModel);
/* 173 */     if ((paramButtonModel instanceof DefaultButtonModel))
/* 174 */       ((DefaultButtonModel)paramButtonModel).setMenuItem(true);
/*     */   }
/*     */ 
/*     */   void initFocusability()
/*     */   {
/* 187 */     setFocusable(false);
/*     */   }
/*     */ 
/*     */   protected void init(String paramString, Icon paramIcon)
/*     */   {
/* 197 */     if (paramString != null) {
/* 198 */       setText(paramString);
/*     */     }
/*     */ 
/* 201 */     if (paramIcon != null) {
/* 202 */       setIcon(paramIcon);
/*     */     }
/*     */ 
/* 206 */     addFocusListener(new MenuItemFocusListener(null));
/* 207 */     setUIProperty("borderPainted", Boolean.FALSE);
/* 208 */     setFocusPainted(false);
/* 209 */     setHorizontalTextPosition(11);
/* 210 */     setHorizontalAlignment(10);
/* 211 */     updateUI();
/*     */   }
/*     */ 
/*     */   public void setUI(MenuItemUI paramMenuItemUI)
/*     */   {
/* 240 */     super.setUI(paramMenuItemUI);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 249 */     setUI((MenuItemUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 262 */     return "MenuItemUI";
/*     */   }
/*     */ 
/*     */   public void setArmed(boolean paramBoolean)
/*     */   {
/* 278 */     ButtonModel localButtonModel = getModel();
/*     */ 
/* 280 */     boolean bool = localButtonModel.isArmed();
/* 281 */     if (localButtonModel.isArmed() != paramBoolean)
/* 282 */       localButtonModel.setArmed(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean isArmed()
/*     */   {
/* 293 */     ButtonModel localButtonModel = getModel();
/* 294 */     return localButtonModel.isArmed();
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean)
/*     */   {
/* 308 */     if ((!paramBoolean) && (!UIManager.getBoolean("MenuItem.disabledAreNavigable"))) {
/* 309 */       setArmed(false);
/*     */     }
/* 311 */     super.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */   boolean alwaysOnTop()
/*     */   {
/* 324 */     if (SwingUtilities.getAncestorOfClass(JInternalFrame.class, this) != null)
/*     */     {
/* 326 */       return false;
/*     */     }
/* 328 */     return true;
/*     */   }
/*     */ 
/*     */   public void setAccelerator(KeyStroke paramKeyStroke)
/*     */   {
/* 353 */     KeyStroke localKeyStroke = this.accelerator;
/* 354 */     this.accelerator = paramKeyStroke;
/* 355 */     repaint();
/* 356 */     revalidate();
/* 357 */     firePropertyChange("accelerator", localKeyStroke, this.accelerator);
/*     */   }
/*     */ 
/*     */   public KeyStroke getAccelerator()
/*     */   {
/* 367 */     return this.accelerator;
/*     */   }
/*     */ 
/*     */   protected void configurePropertiesFromAction(Action paramAction)
/*     */   {
/* 376 */     super.configurePropertiesFromAction(paramAction);
/* 377 */     configureAcceleratorFromAction(paramAction);
/*     */   }
/*     */ 
/*     */   void setIconFromAction(Action paramAction) {
/* 381 */     Icon localIcon = null;
/* 382 */     if (paramAction != null) {
/* 383 */       localIcon = (Icon)paramAction.getValue("SmallIcon");
/*     */     }
/* 385 */     setIcon(localIcon);
/*     */   }
/*     */ 
/*     */   void largeIconChanged(Action paramAction) {
/*     */   }
/*     */ 
/*     */   void smallIconChanged(Action paramAction) {
/* 392 */     setIconFromAction(paramAction);
/*     */   }
/*     */ 
/*     */   void configureAcceleratorFromAction(Action paramAction) {
/* 396 */     KeyStroke localKeyStroke = paramAction == null ? null : (KeyStroke)paramAction.getValue("AcceleratorKey");
/*     */ 
/* 398 */     setAccelerator(localKeyStroke);
/*     */   }
/*     */ 
/*     */   protected void actionPropertyChanged(Action paramAction, String paramString)
/*     */   {
/* 406 */     if (paramString == "AcceleratorKey") {
/* 407 */       configureAcceleratorFromAction(paramAction);
/*     */     }
/*     */     else
/* 410 */       super.actionPropertyChanged(paramAction, paramString);
/*     */   }
/*     */ 
/*     */   public void processMouseEvent(MouseEvent paramMouseEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*     */   {
/* 428 */     processMenuDragMouseEvent(new MenuDragMouseEvent(paramMouseEvent.getComponent(), paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), paramArrayOfMenuElement, paramMenuSelectionManager));
/*     */   }
/*     */ 
/*     */   public void processKeyEvent(KeyEvent paramKeyEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*     */   {
/* 455 */     MenuKeyEvent localMenuKeyEvent = new MenuKeyEvent(paramKeyEvent.getComponent(), paramKeyEvent.getID(), paramKeyEvent.getWhen(), paramKeyEvent.getModifiers(), paramKeyEvent.getKeyCode(), paramKeyEvent.getKeyChar(), paramArrayOfMenuElement, paramMenuSelectionManager);
/*     */ 
/* 459 */     processMenuKeyEvent(localMenuKeyEvent);
/*     */ 
/* 461 */     if (localMenuKeyEvent.isConsumed())
/* 462 */       paramKeyEvent.consume();
/*     */   }
/*     */ 
/*     */   public void processMenuDragMouseEvent(MenuDragMouseEvent paramMenuDragMouseEvent)
/*     */   {
/* 474 */     switch (paramMenuDragMouseEvent.getID()) {
/*     */     case 504:
/* 476 */       this.isMouseDragged = false; fireMenuDragMouseEntered(paramMenuDragMouseEvent); break;
/*     */     case 505:
/* 478 */       this.isMouseDragged = false; fireMenuDragMouseExited(paramMenuDragMouseEvent); break;
/*     */     case 506:
/* 480 */       this.isMouseDragged = true; fireMenuDragMouseDragged(paramMenuDragMouseEvent); break;
/*     */     case 502:
/* 482 */       if (this.isMouseDragged) fireMenuDragMouseReleased(paramMenuDragMouseEvent); break;
/*     */     case 503:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processMenuKeyEvent(MenuKeyEvent paramMenuKeyEvent)
/*     */   {
/* 498 */     switch (paramMenuKeyEvent.getID()) {
/*     */     case 401:
/* 500 */       fireMenuKeyPressed(paramMenuKeyEvent); break;
/*     */     case 402:
/* 502 */       fireMenuKeyReleased(paramMenuKeyEvent); break;
/*     */     case 400:
/* 504 */       fireMenuKeyTyped(paramMenuKeyEvent); break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void fireMenuDragMouseEntered(MenuDragMouseEvent paramMenuDragMouseEvent)
/*     */   {
/* 519 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 522 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 523 */       if (arrayOfObject[i] == MenuDragMouseListener.class)
/*     */       {
/* 525 */         ((MenuDragMouseListener)arrayOfObject[(i + 1)]).menuDragMouseEntered(paramMenuDragMouseEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireMenuDragMouseExited(MenuDragMouseEvent paramMenuDragMouseEvent)
/*     */   {
/* 539 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 542 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 543 */       if (arrayOfObject[i] == MenuDragMouseListener.class)
/*     */       {
/* 545 */         ((MenuDragMouseListener)arrayOfObject[(i + 1)]).menuDragMouseExited(paramMenuDragMouseEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireMenuDragMouseDragged(MenuDragMouseEvent paramMenuDragMouseEvent)
/*     */   {
/* 559 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 562 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 563 */       if (arrayOfObject[i] == MenuDragMouseListener.class)
/*     */       {
/* 565 */         ((MenuDragMouseListener)arrayOfObject[(i + 1)]).menuDragMouseDragged(paramMenuDragMouseEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireMenuDragMouseReleased(MenuDragMouseEvent paramMenuDragMouseEvent)
/*     */   {
/* 579 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 582 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 583 */       if (arrayOfObject[i] == MenuDragMouseListener.class)
/*     */       {
/* 585 */         ((MenuDragMouseListener)arrayOfObject[(i + 1)]).menuDragMouseReleased(paramMenuDragMouseEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireMenuKeyPressed(MenuKeyEvent paramMenuKeyEvent)
/*     */   {
/* 603 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 606 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 607 */       if (arrayOfObject[i] == MenuKeyListener.class)
/*     */       {
/* 609 */         ((MenuKeyListener)arrayOfObject[(i + 1)]).menuKeyPressed(paramMenuKeyEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireMenuKeyReleased(MenuKeyEvent paramMenuKeyEvent)
/*     */   {
/* 627 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 630 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 631 */       if (arrayOfObject[i] == MenuKeyListener.class)
/*     */       {
/* 633 */         ((MenuKeyListener)arrayOfObject[(i + 1)]).menuKeyReleased(paramMenuKeyEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireMenuKeyTyped(MenuKeyEvent paramMenuKeyEvent)
/*     */   {
/* 651 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 654 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 655 */       if (arrayOfObject[i] == MenuKeyListener.class)
/*     */       {
/* 657 */         ((MenuKeyListener)arrayOfObject[(i + 1)]).menuKeyTyped(paramMenuKeyEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void menuSelectionChanged(boolean paramBoolean)
/*     */   {
/* 673 */     setArmed(paramBoolean);
/*     */   }
/*     */ 
/*     */   public MenuElement[] getSubElements()
/*     */   {
/* 683 */     return new MenuElement[0];
/*     */   }
/*     */ 
/*     */   public Component getComponent()
/*     */   {
/* 694 */     return this;
/*     */   }
/*     */ 
/*     */   public void addMenuDragMouseListener(MenuDragMouseListener paramMenuDragMouseListener)
/*     */   {
/* 703 */     this.listenerList.add(MenuDragMouseListener.class, paramMenuDragMouseListener);
/*     */   }
/*     */ 
/*     */   public void removeMenuDragMouseListener(MenuDragMouseListener paramMenuDragMouseListener)
/*     */   {
/* 712 */     this.listenerList.remove(MenuDragMouseListener.class, paramMenuDragMouseListener);
/*     */   }
/*     */ 
/*     */   public MenuDragMouseListener[] getMenuDragMouseListeners()
/*     */   {
/* 724 */     return (MenuDragMouseListener[])this.listenerList.getListeners(MenuDragMouseListener.class);
/*     */   }
/*     */ 
/*     */   public void addMenuKeyListener(MenuKeyListener paramMenuKeyListener)
/*     */   {
/* 733 */     this.listenerList.add(MenuKeyListener.class, paramMenuKeyListener);
/*     */   }
/*     */ 
/*     */   public void removeMenuKeyListener(MenuKeyListener paramMenuKeyListener)
/*     */   {
/* 742 */     this.listenerList.remove(MenuKeyListener.class, paramMenuKeyListener);
/*     */   }
/*     */ 
/*     */   public MenuKeyListener[] getMenuKeyListeners()
/*     */   {
/* 754 */     return (MenuKeyListener[])this.listenerList.getListeners(MenuKeyListener.class);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 764 */     paramObjectInputStream.defaultReadObject();
/* 765 */     if (getUIClassID().equals("MenuItemUI"))
/* 766 */       updateUI();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 771 */     paramObjectOutputStream.defaultWriteObject();
/* 772 */     if (getUIClassID().equals("MenuItemUI")) {
/* 773 */       byte b = JComponent.getWriteObjCounter(this);
/* 774 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 775 */       if ((b == 0) && (this.ui != null))
/* 776 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 792 */     return super.paramString();
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 810 */     if (this.accessibleContext == null) {
/* 811 */       this.accessibleContext = new AccessibleJMenuItem();
/*     */     }
/* 813 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJMenuItem extends AbstractButton.AccessibleAbstractButton
/*     */     implements ChangeListener
/*     */   {
/* 834 */     private boolean isArmed = false;
/* 835 */     private boolean hasFocus = false;
/* 836 */     private boolean isPressed = false;
/* 837 */     private boolean isSelected = false;
/*     */ 
/*     */     AccessibleJMenuItem() {
/* 840 */       super();
/* 841 */       JMenuItem.this.addChangeListener(this);
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 851 */       return AccessibleRole.MENU_ITEM;
/*     */     }
/*     */ 
/*     */     private void fireAccessibilityFocusedEvent(JMenuItem paramJMenuItem) {
/* 855 */       MenuElement[] arrayOfMenuElement = MenuSelectionManager.defaultManager().getSelectedPath();
/*     */ 
/* 857 */       if (arrayOfMenuElement.length > 0) {
/* 858 */         MenuElement localMenuElement = arrayOfMenuElement[(arrayOfMenuElement.length - 1)];
/* 859 */         if (paramJMenuItem == localMenuElement)
/* 860 */           firePropertyChange("AccessibleState", null, AccessibleState.FOCUSED);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void stateChanged(ChangeEvent paramChangeEvent)
/*     */     {
/* 871 */       firePropertyChange("AccessibleVisibleData", Boolean.valueOf(false), Boolean.valueOf(true));
/*     */ 
/* 873 */       if (JMenuItem.this.getModel().isArmed()) {
/* 874 */         if (!this.isArmed) {
/* 875 */           this.isArmed = true;
/* 876 */           firePropertyChange("AccessibleState", null, AccessibleState.ARMED);
/*     */ 
/* 882 */           fireAccessibilityFocusedEvent(JMenuItem.this);
/*     */         }
/*     */       }
/* 885 */       else if (this.isArmed) {
/* 886 */         this.isArmed = false;
/* 887 */         firePropertyChange("AccessibleState", AccessibleState.ARMED, null);
/*     */       }
/*     */ 
/* 892 */       if (JMenuItem.this.isFocusOwner()) {
/* 893 */         if (!this.hasFocus) {
/* 894 */           this.hasFocus = true;
/* 895 */           firePropertyChange("AccessibleState", null, AccessibleState.FOCUSED);
/*     */         }
/*     */ 
/*     */       }
/* 900 */       else if (this.hasFocus) {
/* 901 */         this.hasFocus = false;
/* 902 */         firePropertyChange("AccessibleState", AccessibleState.FOCUSED, null);
/*     */       }
/*     */ 
/* 907 */       if (JMenuItem.this.getModel().isPressed()) {
/* 908 */         if (!this.isPressed) {
/* 909 */           this.isPressed = true;
/* 910 */           firePropertyChange("AccessibleState", null, AccessibleState.PRESSED);
/*     */         }
/*     */ 
/*     */       }
/* 915 */       else if (this.isPressed) {
/* 916 */         this.isPressed = false;
/* 917 */         firePropertyChange("AccessibleState", AccessibleState.PRESSED, null);
/*     */       }
/*     */ 
/* 922 */       if (JMenuItem.this.getModel().isSelected()) {
/* 923 */         if (!this.isSelected) {
/* 924 */           this.isSelected = true;
/* 925 */           firePropertyChange("AccessibleState", null, AccessibleState.CHECKED);
/*     */ 
/* 932 */           fireAccessibilityFocusedEvent(JMenuItem.this);
/*     */         }
/*     */       }
/* 935 */       else if (this.isSelected) {
/* 936 */         this.isSelected = false;
/* 937 */         firePropertyChange("AccessibleState", AccessibleState.CHECKED, null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MenuItemFocusListener
/*     */     implements FocusListener, Serializable
/*     */   {
/*     */     public void focusGained(FocusEvent paramFocusEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void focusLost(FocusEvent paramFocusEvent)
/*     */     {
/* 220 */       JMenuItem localJMenuItem = (JMenuItem)paramFocusEvent.getSource();
/* 221 */       if (localJMenuItem.isFocusPainted())
/* 222 */         localJMenuItem.repaint();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JMenuItem
 * JD-Core Version:    0.6.2
 */