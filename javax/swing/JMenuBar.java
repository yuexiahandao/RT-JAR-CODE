/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.Transient;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleSelection;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.MenuBarUI;
/*     */ 
/*     */ public class JMenuBar extends JComponent
/*     */   implements Accessible, MenuElement
/*     */ {
/*     */   private static final String uiClassID = "MenuBarUI";
/*     */   private transient SingleSelectionModel selectionModel;
/*  98 */   private boolean paintBorder = true;
/*  99 */   private Insets margin = null;
/*     */   private static final boolean TRACE = false;
/*     */   private static final boolean VERBOSE = false;
/*     */   private static final boolean DEBUG = false;
/*     */ 
/*     */   public JMenuBar()
/*     */   {
/* 111 */     setFocusTraversalKeysEnabled(false);
/* 112 */     setSelectionModel(new DefaultSingleSelectionModel());
/* 113 */     updateUI();
/*     */   }
/*     */ 
/*     */   public MenuBarUI getUI()
/*     */   {
/* 121 */     return (MenuBarUI)this.ui;
/*     */   }
/*     */ 
/*     */   public void setUI(MenuBarUI paramMenuBarUI)
/*     */   {
/* 136 */     super.setUI(paramMenuBarUI);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 145 */     setUI((MenuBarUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 157 */     return "MenuBarUI";
/*     */   }
/*     */ 
/*     */   public SingleSelectionModel getSelectionModel()
/*     */   {
/* 168 */     return this.selectionModel;
/*     */   }
/*     */ 
/*     */   public void setSelectionModel(SingleSelectionModel paramSingleSelectionModel)
/*     */   {
/* 181 */     SingleSelectionModel localSingleSelectionModel = this.selectionModel;
/* 182 */     this.selectionModel = paramSingleSelectionModel;
/* 183 */     firePropertyChange("selectionModel", localSingleSelectionModel, this.selectionModel);
/*     */   }
/*     */ 
/*     */   public JMenu add(JMenu paramJMenu)
/*     */   {
/* 194 */     super.add(paramJMenu);
/* 195 */     return paramJMenu;
/*     */   }
/*     */ 
/*     */   public JMenu getMenu(int paramInt)
/*     */   {
/* 208 */     Component localComponent = getComponentAtIndex(paramInt);
/* 209 */     if ((localComponent instanceof JMenu))
/* 210 */       return (JMenu)localComponent;
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */   public int getMenuCount()
/*     */   {
/* 220 */     return getComponentCount();
/*     */   }
/*     */ 
/*     */   public void setHelpMenu(JMenu paramJMenu)
/*     */   {
/* 231 */     throw new Error("setHelpMenu() not yet implemented.");
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public JMenu getHelpMenu()
/*     */   {
/* 242 */     throw new Error("getHelpMenu() not yet implemented.");
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Component getComponentAtIndex(int paramInt)
/*     */   {
/* 255 */     if ((paramInt < 0) || (paramInt >= getComponentCount())) {
/* 256 */       return null;
/*     */     }
/* 258 */     return getComponent(paramInt);
/*     */   }
/*     */ 
/*     */   public int getComponentIndex(Component paramComponent)
/*     */   {
/* 269 */     int i = getComponentCount();
/* 270 */     Component[] arrayOfComponent = getComponents();
/* 271 */     for (int j = 0; j < i; j++) {
/* 272 */       Component localComponent = arrayOfComponent[j];
/* 273 */       if (localComponent == paramComponent)
/* 274 */         return j;
/*     */     }
/* 276 */     return -1;
/*     */   }
/*     */ 
/*     */   public void setSelected(Component paramComponent)
/*     */   {
/* 286 */     SingleSelectionModel localSingleSelectionModel = getSelectionModel();
/* 287 */     int i = getComponentIndex(paramComponent);
/* 288 */     localSingleSelectionModel.setSelectedIndex(i);
/*     */   }
/*     */ 
/*     */   public boolean isSelected()
/*     */   {
/* 297 */     return this.selectionModel.isSelected();
/*     */   }
/*     */ 
/*     */   public boolean isBorderPainted()
/*     */   {
/* 306 */     return this.paintBorder;
/*     */   }
/*     */ 
/*     */   public void setBorderPainted(boolean paramBoolean)
/*     */   {
/* 321 */     boolean bool = this.paintBorder;
/* 322 */     this.paintBorder = paramBoolean;
/* 323 */     firePropertyChange("borderPainted", bool, this.paintBorder);
/* 324 */     if (paramBoolean != bool) {
/* 325 */       revalidate();
/* 326 */       repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintBorder(Graphics paramGraphics)
/*     */   {
/* 339 */     if (isBorderPainted())
/* 340 */       super.paintBorder(paramGraphics);
/*     */   }
/*     */ 
/*     */   public void setMargin(Insets paramInsets)
/*     */   {
/* 357 */     Insets localInsets = this.margin;
/* 358 */     this.margin = paramInsets;
/* 359 */     firePropertyChange("margin", localInsets, paramInsets);
/* 360 */     if ((localInsets == null) || (!localInsets.equals(paramInsets))) {
/* 361 */       revalidate();
/* 362 */       repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Insets getMargin()
/*     */   {
/* 375 */     if (this.margin == null) {
/* 376 */       return new Insets(0, 0, 0, 0);
/*     */     }
/* 378 */     return this.margin;
/*     */   }
/*     */ 
/*     */   public void processMouseEvent(MouseEvent paramMouseEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processKeyEvent(KeyEvent paramKeyEvent, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void menuSelectionChanged(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public MenuElement[] getSubElements()
/*     */   {
/* 417 */     Vector localVector = new Vector();
/* 418 */     int i = getComponentCount();
/*     */ 
/* 422 */     for (int j = 0; j < i; j++) {
/* 423 */       Component localComponent = getComponent(j);
/* 424 */       if ((localComponent instanceof MenuElement)) {
/* 425 */         localVector.addElement((MenuElement)localComponent);
/*     */       }
/*     */     }
/* 428 */     MenuElement[] arrayOfMenuElement = new MenuElement[localVector.size()];
/* 429 */     j = 0; for (i = localVector.size(); j < i; j++)
/* 430 */       arrayOfMenuElement[j] = ((MenuElement)localVector.elementAt(j));
/* 431 */     return arrayOfMenuElement;
/*     */   }
/*     */ 
/*     */   public Component getComponent()
/*     */   {
/* 441 */     return this;
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 456 */     String str1 = this.paintBorder ? "true" : "false";
/*     */ 
/* 458 */     String str2 = this.margin != null ? this.margin.toString() : "";
/*     */ 
/* 461 */     return super.paramString() + ",margin=" + str2 + ",paintBorder=" + str1;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 480 */     if (this.accessibleContext == null) {
/* 481 */       this.accessibleContext = new AccessibleJMenuBar();
/*     */     }
/* 483 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected boolean processKeyBinding(KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean)
/*     */   {
/* 664 */     boolean bool = super.processKeyBinding(paramKeyStroke, paramKeyEvent, paramInt, paramBoolean);
/* 665 */     if (!bool) {
/* 666 */       MenuElement[] arrayOfMenuElement1 = getSubElements();
/* 667 */       for (MenuElement localMenuElement : arrayOfMenuElement1) {
/* 668 */         if (processBindingForKeyStrokeRecursive(localMenuElement, paramKeyStroke, paramKeyEvent, paramInt, paramBoolean))
/*     */         {
/* 670 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 674 */     return bool;
/*     */   }
/*     */ 
/*     */   static boolean processBindingForKeyStrokeRecursive(MenuElement paramMenuElement, KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean)
/*     */   {
/* 679 */     if (paramMenuElement == null) {
/* 680 */       return false;
/*     */     }
/*     */ 
/* 683 */     Component localComponent = paramMenuElement.getComponent();
/*     */ 
/* 685 */     if (((!localComponent.isVisible()) && (!(localComponent instanceof JPopupMenu))) || (!localComponent.isEnabled())) {
/* 686 */       return false;
/*     */     }
/*     */ 
/* 689 */     if ((localComponent != null) && ((localComponent instanceof JComponent)) && (((JComponent)localComponent).processKeyBinding(paramKeyStroke, paramKeyEvent, paramInt, paramBoolean)))
/*     */     {
/* 692 */       return true;
/*     */     }
/*     */ 
/* 695 */     MenuElement[] arrayOfMenuElement1 = paramMenuElement.getSubElements();
/* 696 */     for (MenuElement localMenuElement : arrayOfMenuElement1) {
/* 697 */       if (processBindingForKeyStrokeRecursive(localMenuElement, paramKeyStroke, paramKeyEvent, paramInt, paramBoolean)) {
/* 698 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 702 */     return false;
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 710 */     super.addNotify();
/* 711 */     KeyboardManager.getCurrentManager().registerMenuBar(this);
/*     */   }
/*     */ 
/*     */   public void removeNotify()
/*     */   {
/* 719 */     super.removeNotify();
/* 720 */     KeyboardManager.getCurrentManager().unregisterMenuBar(this);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 725 */     paramObjectOutputStream.defaultWriteObject();
/* 726 */     if (getUIClassID().equals("MenuBarUI")) {
/* 727 */       byte b = JComponent.getWriteObjCounter(this);
/* 728 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 729 */       if ((b == 0) && (this.ui != null)) {
/* 730 */         this.ui.installUI(this);
/*     */       }
/*     */     }
/*     */ 
/* 734 */     Object[] arrayOfObject = new Object[4];
/* 735 */     int i = 0;
/*     */ 
/* 737 */     if ((this.selectionModel instanceof Serializable)) {
/* 738 */       arrayOfObject[(i++)] = "selectionModel";
/* 739 */       arrayOfObject[(i++)] = this.selectionModel;
/*     */     }
/*     */ 
/* 742 */     paramObjectOutputStream.writeObject(arrayOfObject);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 752 */     paramObjectInputStream.defaultReadObject();
/* 753 */     Object[] arrayOfObject = (Object[])paramObjectInputStream.readObject();
/*     */ 
/* 755 */     for (int i = 0; (i < arrayOfObject.length) && 
/* 756 */       (arrayOfObject[i] != null); i += 2)
/*     */     {
/* 759 */       if (arrayOfObject[i].equals("selectionModel"))
/* 760 */         this.selectionModel = ((SingleSelectionModel)arrayOfObject[(i + 1)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class AccessibleJMenuBar extends JComponent.AccessibleJComponent
/*     */     implements AccessibleSelection
/*     */   {
/*     */     protected AccessibleJMenuBar()
/*     */     {
/* 501 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 511 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 512 */       return localAccessibleStateSet;
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 522 */       return AccessibleRole.MENU_BAR;
/*     */     }
/*     */ 
/*     */     public AccessibleSelection getAccessibleSelection()
/*     */     {
/* 534 */       return this;
/*     */     }
/*     */ 
/*     */     public int getAccessibleSelectionCount()
/*     */     {
/* 543 */       if (JMenuBar.this.isSelected()) {
/* 544 */         return 1;
/*     */       }
/* 546 */       return 0;
/*     */     }
/*     */ 
/*     */     public Accessible getAccessibleSelection(int paramInt)
/*     */     {
/* 555 */       if (JMenuBar.this.isSelected()) {
/* 556 */         if (paramInt != 0) {
/* 557 */           return null;
/*     */         }
/* 559 */         int i = JMenuBar.this.getSelectionModel().getSelectedIndex();
/* 560 */         if ((JMenuBar.this.getComponentAtIndex(i) instanceof Accessible)) {
/* 561 */           return (Accessible)JMenuBar.this.getComponentAtIndex(i);
/*     */         }
/*     */       }
/* 564 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean isAccessibleChildSelected(int paramInt)
/*     */     {
/* 575 */       return paramInt == JMenuBar.this.getSelectionModel().getSelectedIndex();
/*     */     }
/*     */ 
/*     */     public void addAccessibleSelection(int paramInt)
/*     */     {
/* 589 */       int i = JMenuBar.this.getSelectionModel().getSelectedIndex();
/* 590 */       if (paramInt == i) {
/* 591 */         return;
/*     */       }
/* 593 */       if ((i >= 0) && (i < JMenuBar.this.getMenuCount())) {
/* 594 */         localJMenu = JMenuBar.this.getMenu(i);
/* 595 */         if (localJMenu != null) {
/* 596 */           MenuSelectionManager.defaultManager().setSelectedPath(null);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 601 */       JMenuBar.this.getSelectionModel().setSelectedIndex(paramInt);
/* 602 */       JMenu localJMenu = JMenuBar.this.getMenu(paramInt);
/* 603 */       if (localJMenu != null) {
/* 604 */         MenuElement[] arrayOfMenuElement = new MenuElement[3];
/* 605 */         arrayOfMenuElement[0] = JMenuBar.this;
/* 606 */         arrayOfMenuElement[1] = localJMenu;
/* 607 */         arrayOfMenuElement[2] = localJMenu.getPopupMenu();
/* 608 */         MenuSelectionManager.defaultManager().setSelectedPath(arrayOfMenuElement);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void removeAccessibleSelection(int paramInt)
/*     */     {
/* 621 */       if ((paramInt >= 0) && (paramInt < JMenuBar.this.getMenuCount())) {
/* 622 */         JMenu localJMenu = JMenuBar.this.getMenu(paramInt);
/* 623 */         if (localJMenu != null) {
/* 624 */           MenuSelectionManager.defaultManager().setSelectedPath(null);
/*     */         }
/*     */ 
/* 627 */         JMenuBar.this.getSelectionModel().setSelectedIndex(-1);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void clearAccessibleSelection()
/*     */     {
/* 636 */       int i = JMenuBar.this.getSelectionModel().getSelectedIndex();
/* 637 */       if ((i >= 0) && (i < JMenuBar.this.getMenuCount())) {
/* 638 */         JMenu localJMenu = JMenuBar.this.getMenu(i);
/* 639 */         if (localJMenu != null) {
/* 640 */           MenuSelectionManager.defaultManager().setSelectedPath(null);
/*     */         }
/*     */       }
/*     */ 
/* 644 */       JMenuBar.this.getSelectionModel().setSelectedIndex(-1);
/*     */     }
/*     */ 
/*     */     public void selectAllAccessibleSelection()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JMenuBar
 * JD-Core Version:    0.6.2
 */