/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Menu;
/*    */ import java.awt.MenuBar;
/*    */ import java.awt.MenuContainer;
/*    */ import java.awt.MenuItem;
/*    */ import java.awt.peer.MenuPeer;
/*    */ 
/*    */ class WMenuPeer extends WMenuItemPeer
/*    */   implements MenuPeer
/*    */ {
/*    */   public native void addSeparator();
/*    */ 
/*    */   public void addItem(MenuItem paramMenuItem)
/*    */   {
/* 36 */     WMenuItemPeer localWMenuItemPeer = (WMenuItemPeer)WToolkit.targetToPeer(paramMenuItem);
/*    */   }
/*    */ 
/*    */   public native void delItem(int paramInt);
/*    */ 
/*    */   WMenuPeer() {
/*    */   }
/*    */ 
/*    */   WMenuPeer(Menu paramMenu) {
/* 45 */     this.target = paramMenu;
/* 46 */     MenuContainer localMenuContainer = paramMenu.getParent();
/*    */ 
/* 48 */     if ((localMenuContainer instanceof MenuBar)) {
/* 49 */       WMenuBarPeer localWMenuBarPeer = (WMenuBarPeer)WToolkit.targetToPeer(localMenuContainer);
/* 50 */       this.parent = localWMenuBarPeer;
/* 51 */       createMenu(localWMenuBarPeer);
/*    */     }
/* 53 */     else if ((localMenuContainer instanceof Menu)) {
/* 54 */       this.parent = ((WMenuPeer)WToolkit.targetToPeer(localMenuContainer));
/* 55 */       createSubMenu(this.parent);
/*    */     }
/*    */     else {
/* 58 */       throw new IllegalArgumentException("unknown menu container class");
/*    */     }
/*    */ 
/* 61 */     checkMenuCreation();
/*    */   }
/*    */ 
/*    */   native void createMenu(WMenuBarPeer paramWMenuBarPeer);
/*    */ 
/*    */   native void createSubMenu(WMenuPeer paramWMenuPeer);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WMenuPeer
 * JD-Core Version:    0.6.2
 */