/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.peer.MenuPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.MenuAccessor;
/*     */ 
/*     */ public class Menu extends MenuItem
/*     */   implements MenuContainer, Accessible
/*     */ {
/*  81 */   Vector items = new Vector();
/*     */   boolean tearOff;
/*     */   boolean isHelpMenu;
/*     */   private static final String base = "menu";
/* 108 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = -8809584163345499784L;
/* 515 */   private int menuSerializedDataVersion = 1;
/*     */ 
/*     */   public Menu()
/*     */     throws HeadlessException
/*     */   {
/* 124 */     this("", false);
/*     */   }
/*     */ 
/*     */   public Menu(String paramString)
/*     */     throws HeadlessException
/*     */   {
/* 137 */     this(paramString, false);
/*     */   }
/*     */ 
/*     */   public Menu(String paramString, boolean paramBoolean)
/*     */     throws HeadlessException
/*     */   {
/* 157 */     super(paramString);
/* 158 */     this.tearOff = paramBoolean;
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 166 */     synchronized (Menu.class) {
/* 167 */       return "menu" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 176 */     synchronized (getTreeLock()) {
/* 177 */       if (this.peer == null)
/* 178 */         this.peer = Toolkit.getDefaultToolkit().createMenu(this);
/* 179 */       int i = getItemCount();
/* 180 */       for (int j = 0; j < i; j++) {
/* 181 */         MenuItem localMenuItem = getItem(j);
/* 182 */         localMenuItem.parent = this;
/* 183 */         localMenuItem.addNotify();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeNotify()
/*     */   {
/* 193 */     synchronized (getTreeLock()) {
/* 194 */       int i = getItemCount();
/* 195 */       for (int j = 0; j < i; j++) {
/* 196 */         getItem(j).removeNotify();
/*     */       }
/* 198 */       super.removeNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isTearOff()
/*     */   {
/* 212 */     return this.tearOff;
/*     */   }
/*     */ 
/*     */   public int getItemCount()
/*     */   {
/* 221 */     return countItems();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int countItems()
/*     */   {
/* 230 */     return countItemsImpl();
/*     */   }
/*     */ 
/*     */   final int countItemsImpl()
/*     */   {
/* 238 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   public MenuItem getItem(int paramInt)
/*     */   {
/* 247 */     return getItemImpl(paramInt);
/*     */   }
/*     */ 
/*     */   final MenuItem getItemImpl(int paramInt)
/*     */   {
/* 255 */     return (MenuItem)this.items.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public MenuItem add(MenuItem paramMenuItem)
/*     */   {
/* 269 */     synchronized (getTreeLock()) {
/* 270 */       if (paramMenuItem.parent != null) {
/* 271 */         paramMenuItem.parent.remove(paramMenuItem);
/*     */       }
/* 273 */       this.items.addElement(paramMenuItem);
/* 274 */       paramMenuItem.parent = this;
/* 275 */       MenuPeer localMenuPeer = (MenuPeer)this.peer;
/* 276 */       if (localMenuPeer != null) {
/* 277 */         paramMenuItem.addNotify();
/* 278 */         localMenuPeer.addItem(paramMenuItem);
/*     */       }
/* 280 */       return paramMenuItem;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void add(String paramString)
/*     */   {
/* 292 */     add(new MenuItem(paramString));
/*     */   }
/*     */ 
/*     */   public void insert(MenuItem paramMenuItem, int paramInt)
/*     */   {
/* 310 */     synchronized (getTreeLock()) {
/* 311 */       if (paramInt < 0) {
/* 312 */         throw new IllegalArgumentException("index less than zero.");
/*     */       }
/*     */ 
/* 315 */       int i = getItemCount();
/* 316 */       Vector localVector = new Vector();
/*     */ 
/* 322 */       for (int j = paramInt; j < i; j++) {
/* 323 */         localVector.addElement(getItem(paramInt));
/* 324 */         remove(paramInt);
/*     */       }
/*     */ 
/* 327 */       add(paramMenuItem);
/*     */ 
/* 332 */       for (j = 0; j < localVector.size(); j++)
/* 333 */         add((MenuItem)localVector.elementAt(j));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void insert(String paramString, int paramInt)
/*     */   {
/* 354 */     insert(new MenuItem(paramString), paramInt);
/*     */   }
/*     */ 
/*     */   public void addSeparator()
/*     */   {
/* 362 */     add("-");
/*     */   }
/*     */ 
/*     */   public void insertSeparator(int paramInt)
/*     */   {
/* 376 */     synchronized (getTreeLock()) {
/* 377 */       if (paramInt < 0) {
/* 378 */         throw new IllegalArgumentException("index less than zero.");
/*     */       }
/*     */ 
/* 381 */       int i = getItemCount();
/* 382 */       Vector localVector = new Vector();
/*     */ 
/* 388 */       for (int j = paramInt; j < i; j++) {
/* 389 */         localVector.addElement(getItem(paramInt));
/* 390 */         remove(paramInt);
/*     */       }
/*     */ 
/* 393 */       addSeparator();
/*     */ 
/* 398 */       for (j = 0; j < localVector.size(); j++)
/* 399 */         add((MenuItem)localVector.elementAt(j));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(int paramInt)
/*     */   {
/* 409 */     synchronized (getTreeLock()) {
/* 410 */       MenuItem localMenuItem = getItem(paramInt);
/* 411 */       this.items.removeElementAt(paramInt);
/* 412 */       MenuPeer localMenuPeer = (MenuPeer)this.peer;
/* 413 */       if (localMenuPeer != null) {
/* 414 */         localMenuItem.removeNotify();
/* 415 */         localMenuItem.parent = null;
/* 416 */         localMenuPeer.delItem(paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(MenuComponent paramMenuComponent)
/*     */   {
/* 429 */     synchronized (getTreeLock()) {
/* 430 */       int i = this.items.indexOf(paramMenuComponent);
/* 431 */       if (i >= 0)
/* 432 */         remove(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 442 */     synchronized (getTreeLock()) {
/* 443 */       int i = getItemCount();
/* 444 */       for (int j = i - 1; j >= 0; j--)
/* 445 */         remove(j);
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean handleShortcut(KeyEvent paramKeyEvent)
/*     */   {
/* 457 */     int i = getItemCount();
/* 458 */     for (int j = 0; j < i; j++) {
/* 459 */       MenuItem localMenuItem = getItem(j);
/* 460 */       if (localMenuItem.handleShortcut(paramKeyEvent)) {
/* 461 */         return true;
/*     */       }
/*     */     }
/* 464 */     return false;
/*     */   }
/*     */ 
/*     */   MenuItem getShortcutMenuItem(MenuShortcut paramMenuShortcut) {
/* 468 */     int i = getItemCount();
/* 469 */     for (int j = 0; j < i; j++) {
/* 470 */       MenuItem localMenuItem = getItem(j).getShortcutMenuItem(paramMenuShortcut);
/* 471 */       if (localMenuItem != null) {
/* 472 */         return localMenuItem;
/*     */       }
/*     */     }
/* 475 */     return null;
/*     */   }
/*     */ 
/*     */   synchronized Enumeration shortcuts() {
/* 479 */     Vector localVector = new Vector();
/* 480 */     int i = getItemCount();
/* 481 */     for (int j = 0; j < i; j++) {
/* 482 */       MenuItem localMenuItem = getItem(j);
/*     */       Object localObject;
/* 483 */       if ((localMenuItem instanceof Menu)) {
/* 484 */         localObject = ((Menu)localMenuItem).shortcuts();
/* 485 */         while (((Enumeration)localObject).hasMoreElements())
/* 486 */           localVector.addElement(((Enumeration)localObject).nextElement());
/*     */       }
/*     */       else {
/* 489 */         localObject = localMenuItem.getShortcut();
/* 490 */         if (localObject != null) {
/* 491 */           localVector.addElement(localObject);
/*     */         }
/*     */       }
/*     */     }
/* 495 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   void deleteShortcut(MenuShortcut paramMenuShortcut) {
/* 499 */     int i = getItemCount();
/* 500 */     for (int j = 0; j < i; j++)
/* 501 */       getItem(j).deleteShortcut(paramMenuShortcut);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 527 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException, HeadlessException
/*     */   {
/* 545 */     paramObjectInputStream.defaultReadObject();
/* 546 */     for (int i = 0; i < this.items.size(); i++) {
/* 547 */       MenuItem localMenuItem = (MenuItem)this.items.elementAt(i);
/* 548 */       localMenuItem.parent = this;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/* 562 */     String str = ",tearOff=" + this.tearOff + ",isHelpMenu=" + this.isHelpMenu;
/* 563 */     return super.paramString() + str;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 587 */     if (this.accessibleContext == null) {
/* 588 */       this.accessibleContext = new AccessibleAWTMenu();
/*     */     }
/* 590 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   int getAccessibleChildIndex(MenuComponent paramMenuComponent)
/*     */   {
/* 597 */     return this.items.indexOf(paramMenuComponent);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  62 */     Toolkit.loadLibraries();
/*  63 */     if (!GraphicsEnvironment.isHeadless()) {
/*  64 */       initIDs();
/*     */     }
/*     */ 
/*  67 */     AWTAccessor.setMenuAccessor(new AWTAccessor.MenuAccessor()
/*     */     {
/*     */       public Vector getItems(Menu paramAnonymousMenu) {
/*  70 */         return paramAnonymousMenu.items;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTMenu extends MenuItem.AccessibleAWTMenuItem
/*     */   {
/*     */     private static final long serialVersionUID = 5228160894980069094L;
/*     */ 
/*     */     protected AccessibleAWTMenu()
/*     */     {
/* 611 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 625 */       return AccessibleRole.MENU;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Menu
 * JD-Core Version:    0.6.2
 */