/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.peer.MenuBarPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.MenuBarAccessor;
/*     */ 
/*     */ public class MenuBar extends MenuComponent
/*     */   implements MenuContainer, Accessible
/*     */ {
/*  97 */   Vector menus = new Vector();
/*     */   Menu helpMenu;
/*     */   private static final String base = "menubar";
/* 112 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = -4930327919388951260L;
/* 408 */   private int menuBarSerializedDataVersion = 1;
/*     */ 
/*     */   public MenuBar()
/*     */     throws HeadlessException
/*     */   {
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 133 */     synchronized (MenuBar.class) {
/* 134 */       return "menubar" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 144 */     synchronized (getTreeLock()) {
/* 145 */       if (this.peer == null) {
/* 146 */         this.peer = Toolkit.getDefaultToolkit().createMenuBar(this);
/*     */       }
/* 148 */       int i = getMenuCount();
/* 149 */       for (int j = 0; j < i; j++)
/* 150 */         getMenu(j).addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeNotify()
/*     */   {
/* 161 */     synchronized (getTreeLock()) {
/* 162 */       int i = getMenuCount();
/* 163 */       for (int j = 0; j < i; j++) {
/* 164 */         getMenu(j).removeNotify();
/*     */       }
/* 166 */       super.removeNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Menu getHelpMenu()
/*     */   {
/* 175 */     return this.helpMenu;
/*     */   }
/*     */ 
/*     */   public void setHelpMenu(Menu paramMenu)
/*     */   {
/* 185 */     synchronized (getTreeLock()) {
/* 186 */       if (this.helpMenu == paramMenu) {
/* 187 */         return;
/*     */       }
/* 189 */       if (this.helpMenu != null) {
/* 190 */         remove(this.helpMenu);
/*     */       }
/* 192 */       if (paramMenu.parent != this) {
/* 193 */         add(paramMenu);
/*     */       }
/* 195 */       this.helpMenu = paramMenu;
/* 196 */       if (paramMenu != null) {
/* 197 */         paramMenu.isHelpMenu = true;
/* 198 */         paramMenu.parent = this;
/* 199 */         MenuBarPeer localMenuBarPeer = (MenuBarPeer)this.peer;
/* 200 */         if (localMenuBarPeer != null) {
/* 201 */           if (paramMenu.peer == null) {
/* 202 */             paramMenu.addNotify();
/*     */           }
/* 204 */           localMenuBarPeer.addHelpMenu(paramMenu);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Menu add(Menu paramMenu)
/*     */   {
/* 221 */     synchronized (getTreeLock()) {
/* 222 */       if (paramMenu.parent != null) {
/* 223 */         paramMenu.parent.remove(paramMenu);
/*     */       }
/* 225 */       this.menus.addElement(paramMenu);
/* 226 */       paramMenu.parent = this;
/*     */ 
/* 228 */       MenuBarPeer localMenuBarPeer = (MenuBarPeer)this.peer;
/* 229 */       if (localMenuBarPeer != null) {
/* 230 */         if (paramMenu.peer == null) {
/* 231 */           paramMenu.addNotify();
/*     */         }
/* 233 */         localMenuBarPeer.addMenu(paramMenu);
/*     */       }
/* 235 */       return paramMenu;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(int paramInt)
/*     */   {
/* 246 */     synchronized (getTreeLock()) {
/* 247 */       Menu localMenu = getMenu(paramInt);
/* 248 */       this.menus.removeElementAt(paramInt);
/* 249 */       MenuBarPeer localMenuBarPeer = (MenuBarPeer)this.peer;
/* 250 */       if (localMenuBarPeer != null) {
/* 251 */         localMenu.removeNotify();
/* 252 */         localMenu.parent = null;
/* 253 */         localMenuBarPeer.delMenu(paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(MenuComponent paramMenuComponent)
/*     */   {
/* 264 */     synchronized (getTreeLock()) {
/* 265 */       int i = this.menus.indexOf(paramMenuComponent);
/* 266 */       if (i >= 0)
/* 267 */         remove(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMenuCount()
/*     */   {
/* 278 */     return countMenus();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int countMenus()
/*     */   {
/* 287 */     return getMenuCountImpl();
/*     */   }
/*     */ 
/*     */   final int getMenuCountImpl()
/*     */   {
/* 295 */     return this.menus.size();
/*     */   }
/*     */ 
/*     */   public Menu getMenu(int paramInt)
/*     */   {
/* 304 */     return getMenuImpl(paramInt);
/*     */   }
/*     */ 
/*     */   final Menu getMenuImpl(int paramInt)
/*     */   {
/* 312 */     return (Menu)this.menus.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration<MenuShortcut> shortcuts()
/*     */   {
/* 324 */     Vector localVector = new Vector();
/* 325 */     int i = getMenuCount();
/* 326 */     for (int j = 0; j < i; j++) {
/* 327 */       Enumeration localEnumeration = getMenu(j).shortcuts();
/* 328 */       while (localEnumeration.hasMoreElements()) {
/* 329 */         localVector.addElement(localEnumeration.nextElement());
/*     */       }
/*     */     }
/* 332 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   public MenuItem getShortcutMenuItem(MenuShortcut paramMenuShortcut)
/*     */   {
/* 347 */     int i = getMenuCount();
/* 348 */     for (int j = 0; j < i; j++) {
/* 349 */       MenuItem localMenuItem = getMenu(j).getShortcutMenuItem(paramMenuShortcut);
/* 350 */       if (localMenuItem != null) {
/* 351 */         return localMenuItem;
/*     */       }
/*     */     }
/* 354 */     return null;
/*     */   }
/*     */ 
/*     */   boolean handleShortcut(KeyEvent paramKeyEvent)
/*     */   {
/* 365 */     int i = paramKeyEvent.getID();
/* 366 */     if ((i != 401) && (i != 402)) {
/* 367 */       return false;
/*     */     }
/*     */ 
/* 371 */     int j = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
/* 372 */     if ((paramKeyEvent.getModifiers() & j) == 0) {
/* 373 */       return false;
/*     */     }
/*     */ 
/* 377 */     int k = getMenuCount();
/* 378 */     for (int m = 0; m < k; m++) {
/* 379 */       Menu localMenu = getMenu(m);
/* 380 */       if (localMenu.handleShortcut(paramKeyEvent)) {
/* 381 */         return true;
/*     */       }
/*     */     }
/* 384 */     return false;
/*     */   }
/*     */ 
/*     */   public void deleteShortcut(MenuShortcut paramMenuShortcut)
/*     */   {
/* 393 */     int i = getMenuCount();
/* 394 */     for (int j = 0; j < i; j++)
/* 395 */       getMenu(j).deleteShortcut(paramMenuShortcut);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 421 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/* 439 */     paramObjectInputStream.defaultReadObject();
/* 440 */     for (int i = 0; i < this.menus.size(); i++) {
/* 441 */       Menu localMenu = (Menu)this.menus.elementAt(i);
/* 442 */       localMenu.parent = this;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 467 */     if (this.accessibleContext == null) {
/* 468 */       this.accessibleContext = new AccessibleAWTMenuBar();
/*     */     }
/* 470 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   int getAccessibleChildIndex(MenuComponent paramMenuComponent)
/*     */   {
/* 477 */     return this.menus.indexOf(paramMenuComponent);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  74 */     Toolkit.loadLibraries();
/*  75 */     if (!GraphicsEnvironment.isHeadless()) {
/*  76 */       initIDs();
/*     */     }
/*  78 */     AWTAccessor.setMenuBarAccessor(new AWTAccessor.MenuBarAccessor()
/*     */     {
/*     */       public Menu getHelpMenu(MenuBar paramAnonymousMenuBar) {
/*  81 */         return paramAnonymousMenuBar.helpMenu;
/*     */       }
/*     */ 
/*     */       public Vector getMenus(MenuBar paramAnonymousMenuBar) {
/*  85 */         return paramAnonymousMenuBar.menus;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTMenuBar extends MenuComponent.AccessibleAWTMenuComponent
/*     */   {
/*     */     private static final long serialVersionUID = -8577604491830083815L;
/*     */ 
/*     */     protected AccessibleAWTMenuBar()
/*     */     {
/* 491 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 506 */       return AccessibleRole.MENU_BAR;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MenuBar
 * JD-Core Version:    0.6.2
 */