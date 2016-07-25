/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.PopupMenuPeer;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.PopupMenuAccessor;
/*     */ 
/*     */ public class PopupMenu extends Menu
/*     */ {
/*     */   private static final String base = "popup";
/*  48 */   static int nameCounter = 0;
/*     */ 
/*  50 */   transient boolean isTrayIconPopup = false;
/*     */   private static final long serialVersionUID = -4620452533522760060L;
/*     */ 
/*     */   public PopupMenu()
/*     */     throws HeadlessException
/*     */   {
/*  73 */     this("");
/*     */   }
/*     */ 
/*     */   public PopupMenu(String paramString)
/*     */     throws HeadlessException
/*     */   {
/*  86 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public MenuContainer getParent()
/*     */   {
/*  93 */     if (this.isTrayIconPopup) {
/*  94 */       return null;
/*     */     }
/*  96 */     return super.getParent();
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 104 */     synchronized (PopupMenu.class) {
/* 105 */       return "popup" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 115 */     synchronized (getTreeLock())
/*     */     {
/* 118 */       if ((this.parent != null) && (!(this.parent instanceof Component))) {
/* 119 */         super.addNotify();
/*     */       }
/*     */       else {
/* 122 */         if (this.peer == null)
/* 123 */           this.peer = Toolkit.getDefaultToolkit().createPopupMenu(this);
/* 124 */         int i = getItemCount();
/* 125 */         for (int j = 0; j < i; j++) {
/* 126 */           MenuItem localMenuItem = getItem(j);
/* 127 */           localMenuItem.parent = this;
/* 128 */           localMenuItem.addNotify();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void show(Component paramComponent, int paramInt1, int paramInt2)
/*     */   {
/* 157 */     MenuContainer localMenuContainer = this.parent;
/* 158 */     if (localMenuContainer == null) {
/* 159 */       throw new NullPointerException("parent is null");
/*     */     }
/* 161 */     if (!(localMenuContainer instanceof Component)) {
/* 162 */       throw new IllegalArgumentException("PopupMenus with non-Component parents cannot be shown");
/*     */     }
/*     */ 
/* 165 */     Component localComponent = (Component)localMenuContainer;
/*     */ 
/* 169 */     if (localComponent != paramComponent) {
/* 170 */       if ((localComponent instanceof Container)) {
/* 171 */         if (!((Container)localComponent).isAncestorOf(paramComponent))
/* 172 */           throw new IllegalArgumentException("origin not in parent's hierarchy");
/*     */       }
/*     */       else {
/* 175 */         throw new IllegalArgumentException("origin not in parent's hierarchy");
/*     */       }
/*     */     }
/* 178 */     if ((localComponent.getPeer() == null) || (!localComponent.isShowing())) {
/* 179 */       throw new RuntimeException("parent not showing on screen");
/*     */     }
/* 181 */     if (this.peer == null) {
/* 182 */       addNotify();
/*     */     }
/* 184 */     synchronized (getTreeLock()) {
/* 185 */       if (this.peer != null)
/* 186 */         ((PopupMenuPeer)this.peer).show(new Event(paramComponent, 0L, 501, paramInt1, paramInt2, 0, 0));
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 206 */     if (this.accessibleContext == null) {
/* 207 */       this.accessibleContext = new AccessibleAWTPopupMenu();
/*     */     }
/* 209 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  53 */     AWTAccessor.setPopupMenuAccessor(new AWTAccessor.PopupMenuAccessor()
/*     */     {
/*     */       public boolean isTrayIconPopup(PopupMenu paramAnonymousPopupMenu) {
/*  56 */         return paramAnonymousPopupMenu.isTrayIconPopup;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTPopupMenu extends Menu.AccessibleAWTMenu
/*     */   {
/*     */     private static final long serialVersionUID = -4282044795947239955L;
/*     */ 
/*     */     protected AccessibleAWTPopupMenu()
/*     */     {
/* 221 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 235 */       return AccessibleRole.POPUP_MENU;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.PopupMenu
 * JD-Core Version:    0.6.2
 */