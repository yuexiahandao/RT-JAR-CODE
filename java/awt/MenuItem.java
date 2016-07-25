/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.peer.MenuItemPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EventListener;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleAction;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleValue;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.MenuItemAccessor;
/*     */ 
/*     */ public class MenuItem extends MenuComponent
/*     */   implements Accessible
/*     */ {
/* 114 */   boolean enabled = true;
/*     */   String label;
/*     */   String actionCommand;
/*     */   long eventMask;
/*     */   transient ActionListener actionListener;
/* 163 */   private MenuShortcut shortcut = null;
/*     */   private static final String base = "menuitem";
/* 166 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = -21757335363267194L;
/* 699 */   private int menuItemSerializedDataVersion = 1;
/*     */ 
/*     */   public MenuItem()
/*     */     throws HeadlessException
/*     */   {
/* 182 */     this("", null);
/*     */   }
/*     */ 
/*     */   public MenuItem(String paramString)
/*     */     throws HeadlessException
/*     */   {
/* 198 */     this(paramString, null);
/*     */   }
/*     */ 
/*     */   public MenuItem(String paramString, MenuShortcut paramMenuShortcut)
/*     */     throws HeadlessException
/*     */   {
/* 215 */     this.label = paramString;
/* 216 */     this.shortcut = paramMenuShortcut;
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 224 */     synchronized (MenuItem.class) {
/* 225 */       return "menuitem" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 234 */     synchronized (getTreeLock()) {
/* 235 */       if (this.peer == null)
/* 236 */         this.peer = Toolkit.getDefaultToolkit().createMenuItem(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getLabel()
/*     */   {
/* 248 */     return this.label;
/*     */   }
/*     */ 
/*     */   public synchronized void setLabel(String paramString)
/*     */   {
/* 258 */     this.label = paramString;
/* 259 */     MenuItemPeer localMenuItemPeer = (MenuItemPeer)this.peer;
/* 260 */     if (localMenuItemPeer != null)
/* 261 */       localMenuItemPeer.setLabel(paramString);
/*     */   }
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/* 271 */     return this.enabled;
/*     */   }
/*     */ 
/*     */   public synchronized void setEnabled(boolean paramBoolean)
/*     */   {
/* 282 */     enable(paramBoolean);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void enable()
/*     */   {
/* 291 */     this.enabled = true;
/* 292 */     MenuItemPeer localMenuItemPeer = (MenuItemPeer)this.peer;
/* 293 */     if (localMenuItemPeer != null)
/* 294 */       localMenuItemPeer.setEnabled(true);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void enable(boolean paramBoolean)
/*     */   {
/* 304 */     if (paramBoolean)
/* 305 */       enable();
/*     */     else
/* 307 */       disable();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void disable()
/*     */   {
/* 317 */     this.enabled = false;
/* 318 */     MenuItemPeer localMenuItemPeer = (MenuItemPeer)this.peer;
/* 319 */     if (localMenuItemPeer != null)
/* 320 */       localMenuItemPeer.setEnabled(false);
/*     */   }
/*     */ 
/*     */   public MenuShortcut getShortcut()
/*     */   {
/* 333 */     return this.shortcut;
/*     */   }
/*     */ 
/*     */   public void setShortcut(MenuShortcut paramMenuShortcut)
/*     */   {
/* 346 */     this.shortcut = paramMenuShortcut;
/* 347 */     MenuItemPeer localMenuItemPeer = (MenuItemPeer)this.peer;
/* 348 */     if (localMenuItemPeer != null)
/* 349 */       localMenuItemPeer.setLabel(this.label);
/*     */   }
/*     */ 
/*     */   public void deleteShortcut()
/*     */   {
/* 359 */     this.shortcut = null;
/* 360 */     MenuItemPeer localMenuItemPeer = (MenuItemPeer)this.peer;
/* 361 */     if (localMenuItemPeer != null)
/* 362 */       localMenuItemPeer.setLabel(this.label);
/*     */   }
/*     */ 
/*     */   void deleteShortcut(MenuShortcut paramMenuShortcut)
/*     */   {
/* 371 */     if (paramMenuShortcut.equals(this.shortcut)) {
/* 372 */       this.shortcut = null;
/* 373 */       MenuItemPeer localMenuItemPeer = (MenuItemPeer)this.peer;
/* 374 */       if (localMenuItemPeer != null)
/* 375 */         localMenuItemPeer.setLabel(this.label);
/*     */     }
/*     */   }
/*     */ 
/*     */   void doMenuEvent(long paramLong, int paramInt)
/*     */   {
/* 387 */     Toolkit.getEventQueue().postEvent(new ActionEvent(this, 1001, getActionCommand(), paramLong, paramInt));
/*     */   }
/*     */ 
/*     */   private final boolean isItemEnabled()
/*     */   {
/* 399 */     if (!isEnabled()) {
/* 400 */       return false;
/*     */     }
/* 402 */     MenuContainer localMenuContainer = getParent_NoClientCode();
/*     */     do {
/* 404 */       if (!(localMenuContainer instanceof Menu)) {
/* 405 */         return true;
/*     */       }
/* 407 */       Menu localMenu = (Menu)localMenuContainer;
/* 408 */       if (!localMenu.isEnabled()) {
/* 409 */         return false;
/*     */       }
/* 411 */       localMenuContainer = localMenu.getParent_NoClientCode();
/* 412 */     }while (localMenuContainer != null);
/* 413 */     return true;
/*     */   }
/*     */ 
/*     */   boolean handleShortcut(KeyEvent paramKeyEvent)
/*     */   {
/* 422 */     MenuShortcut localMenuShortcut1 = new MenuShortcut(paramKeyEvent.getKeyCode(), (paramKeyEvent.getModifiers() & 0x1) > 0);
/*     */ 
/* 424 */     MenuShortcut localMenuShortcut2 = new MenuShortcut(paramKeyEvent.getExtendedKeyCode(), (paramKeyEvent.getModifiers() & 0x1) > 0);
/*     */ 
/* 428 */     if (((localMenuShortcut1.equals(this.shortcut)) || (localMenuShortcut2.equals(this.shortcut))) && (isItemEnabled()))
/*     */     {
/* 430 */       if (paramKeyEvent.getID() == 401) {
/* 431 */         doMenuEvent(paramKeyEvent.getWhen(), paramKeyEvent.getModifiers());
/*     */       }
/*     */ 
/* 435 */       return true;
/*     */     }
/* 437 */     return false;
/*     */   }
/*     */ 
/*     */   MenuItem getShortcutMenuItem(MenuShortcut paramMenuShortcut) {
/* 441 */     return paramMenuShortcut.equals(this.shortcut) ? this : null;
/*     */   }
/*     */ 
/*     */   protected final void enableEvents(long paramLong)
/*     */   {
/* 461 */     this.eventMask |= paramLong;
/* 462 */     this.newEventsOnly = true;
/*     */   }
/*     */ 
/*     */   protected final void disableEvents(long paramLong)
/*     */   {
/* 476 */     this.eventMask &= (paramLong ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void setActionCommand(String paramString)
/*     */   {
/* 491 */     this.actionCommand = paramString;
/*     */   }
/*     */ 
/*     */   public String getActionCommand()
/*     */   {
/* 501 */     return getActionCommandImpl();
/*     */   }
/*     */ 
/*     */   final String getActionCommandImpl()
/*     */   {
/* 506 */     return this.actionCommand == null ? this.label : this.actionCommand;
/*     */   }
/*     */ 
/*     */   public synchronized void addActionListener(ActionListener paramActionListener)
/*     */   {
/* 524 */     if (paramActionListener == null) {
/* 525 */       return;
/*     */     }
/* 527 */     this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
/* 528 */     this.newEventsOnly = true;
/*     */   }
/*     */ 
/*     */   public synchronized void removeActionListener(ActionListener paramActionListener)
/*     */   {
/* 546 */     if (paramActionListener == null) {
/* 547 */       return;
/*     */     }
/* 549 */     this.actionListener = AWTEventMulticaster.remove(this.actionListener, paramActionListener);
/*     */   }
/*     */ 
/*     */   public synchronized ActionListener[] getActionListeners()
/*     */   {
/* 567 */     return (ActionListener[])getListeners(ActionListener.class);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 604 */     ActionListener localActionListener = null;
/* 605 */     if (paramClass == ActionListener.class) {
/* 606 */       localActionListener = this.actionListener;
/*     */     }
/* 608 */     return AWTEventMulticaster.getListeners(localActionListener, paramClass);
/*     */   }
/*     */ 
/*     */   protected void processEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 627 */     if ((paramAWTEvent instanceof ActionEvent))
/* 628 */       processActionEvent((ActionEvent)paramAWTEvent);
/*     */   }
/*     */ 
/*     */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*     */   {
/* 634 */     if (paramAWTEvent.id == 1001) {
/* 635 */       if (((this.eventMask & 0x80) != 0L) || (this.actionListener != null))
/*     */       {
/* 637 */         return true;
/*     */       }
/* 639 */       return false;
/*     */     }
/* 641 */     return super.eventEnabled(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processActionEvent(ActionEvent paramActionEvent)
/*     */   {
/* 667 */     ActionListener localActionListener = this.actionListener;
/* 668 */     if (localActionListener != null)
/* 669 */       localActionListener.actionPerformed(paramActionEvent);
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/* 683 */     String str = ",label=" + this.label;
/* 684 */     if (this.shortcut != null) {
/* 685 */       str = str + ",shortcut=" + this.shortcut;
/*     */     }
/* 687 */     return super.paramString() + str;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 721 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 723 */     AWTEventMulticaster.save(paramObjectOutputStream, "actionL", this.actionListener);
/* 724 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/* 745 */     paramObjectInputStream.defaultReadObject();
/*     */     Object localObject;
/* 748 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 749 */       String str = ((String)localObject).intern();
/*     */ 
/* 751 */       if ("actionL" == str) {
/* 752 */         addActionListener((ActionListener)paramObjectInputStream.readObject());
/*     */       }
/*     */       else
/* 755 */         paramObjectInputStream.readObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 780 */     if (this.accessibleContext == null) {
/* 781 */       this.accessibleContext = new AccessibleAWTMenuItem();
/*     */     }
/* 783 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  75 */     Toolkit.loadLibraries();
/*  76 */     if (!GraphicsEnvironment.isHeadless()) {
/*  77 */       initIDs();
/*     */     }
/*     */ 
/*  80 */     AWTAccessor.setMenuItemAccessor(new AWTAccessor.MenuItemAccessor()
/*     */     {
/*     */       public boolean isEnabled(MenuItem paramAnonymousMenuItem) {
/*  83 */         return paramAnonymousMenuItem.enabled;
/*     */       }
/*     */ 
/*     */       public String getLabel(MenuItem paramAnonymousMenuItem) {
/*  87 */         return paramAnonymousMenuItem.label;
/*     */       }
/*     */ 
/*     */       public MenuShortcut getShortcut(MenuItem paramAnonymousMenuItem) {
/*  91 */         return paramAnonymousMenuItem.shortcut;
/*     */       }
/*     */ 
/*     */       public String getActionCommandImpl(MenuItem paramAnonymousMenuItem) {
/*  95 */         return paramAnonymousMenuItem.getActionCommandImpl();
/*     */       }
/*     */ 
/*     */       public boolean isItemEnabled(MenuItem paramAnonymousMenuItem) {
/*  99 */         return paramAnonymousMenuItem.isItemEnabled();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTMenuItem extends MenuComponent.AccessibleAWTMenuComponent
/*     */     implements AccessibleAction, AccessibleValue
/*     */   {
/*     */     private static final long serialVersionUID = -217847831945965825L;
/*     */ 
/*     */     protected AccessibleAWTMenuItem()
/*     */     {
/* 797 */       super();
/*     */     }
/*     */ 
/*     */     public String getAccessibleName()
/*     */     {
/* 812 */       if (this.accessibleName != null) {
/* 813 */         return this.accessibleName;
/*     */       }
/* 815 */       if (MenuItem.this.getLabel() == null) {
/* 816 */         return super.getAccessibleName();
/*     */       }
/* 818 */       return MenuItem.this.getLabel();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 830 */       return AccessibleRole.MENU_ITEM;
/*     */     }
/*     */ 
/*     */     public AccessibleAction getAccessibleAction()
/*     */     {
/* 842 */       return this;
/*     */     }
/*     */ 
/*     */     public AccessibleValue getAccessibleValue()
/*     */     {
/* 854 */       return this;
/*     */     }
/*     */ 
/*     */     public int getAccessibleActionCount()
/*     */     {
/* 864 */       return 1;
/*     */     }
/*     */ 
/*     */     public String getAccessibleActionDescription(int paramInt)
/*     */     {
/* 873 */       if (paramInt == 0)
/*     */       {
/* 875 */         return "click";
/*     */       }
/* 877 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean doAccessibleAction(int paramInt)
/*     */     {
/* 888 */       if (paramInt == 0)
/*     */       {
/* 890 */         Toolkit.getEventQueue().postEvent(new ActionEvent(MenuItem.this, 1001, MenuItem.this.getActionCommand(), EventQueue.getMostRecentEventTime(), 0));
/*     */ 
/* 896 */         return true;
/*     */       }
/* 898 */       return false;
/*     */     }
/*     */ 
/*     */     public Number getCurrentAccessibleValue()
/*     */     {
/* 910 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*     */     {
/* 919 */       return false;
/*     */     }
/*     */ 
/*     */     public Number getMinimumAccessibleValue()
/*     */     {
/* 928 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Number getMaximumAccessibleValue()
/*     */     {
/* 937 */       return Integer.valueOf(0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MenuItem
 * JD-Core Version:    0.6.2
 */