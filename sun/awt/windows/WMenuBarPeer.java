/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Menu;
/*    */ import java.awt.MenuBar;
/*    */ import java.awt.peer.MenuBarPeer;
/*    */ 
/*    */ class WMenuBarPeer extends WMenuPeer
/*    */   implements MenuBarPeer
/*    */ {
/*    */   public native void addMenu(Menu paramMenu);
/*    */ 
/*    */   public native void delMenu(int paramInt);
/*    */ 
/*    */   public void addHelpMenu(Menu paramMenu)
/*    */   {
/* 38 */     addMenu(paramMenu);
/*    */   }
/*    */ 
/*    */   WMenuBarPeer(MenuBar paramMenuBar)
/*    */   {
/* 43 */     this.target = paramMenuBar;
/* 44 */     WFramePeer localWFramePeer = (WFramePeer)WToolkit.targetToPeer(paramMenuBar.getParent());
/*    */ 
/* 46 */     create(localWFramePeer);
/*    */ 
/* 48 */     checkMenuCreation();
/*    */   }
/*    */ 
/*    */   native void create(WFramePeer paramWFramePeer);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WMenuBarPeer
 * JD-Core Version:    0.6.2
 */