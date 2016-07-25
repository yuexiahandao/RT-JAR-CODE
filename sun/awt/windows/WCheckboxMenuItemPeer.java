/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.CheckboxMenuItem;
/*    */ import java.awt.event.ItemEvent;
/*    */ import java.awt.peer.CheckboxMenuItemPeer;
/*    */ 
/*    */ class WCheckboxMenuItemPeer extends WMenuItemPeer
/*    */   implements CheckboxMenuItemPeer
/*    */ {
/*    */   public native void setState(boolean paramBoolean);
/*    */ 
/*    */   WCheckboxMenuItemPeer(CheckboxMenuItem paramCheckboxMenuItem)
/*    */   {
/* 40 */     super(paramCheckboxMenuItem, true);
/* 41 */     setState(paramCheckboxMenuItem.getState());
/*    */   }
/*    */ 
/*    */   public void handleAction(final boolean paramBoolean)
/*    */   {
/* 47 */     final CheckboxMenuItem localCheckboxMenuItem = (CheckboxMenuItem)this.target;
/* 48 */     WToolkit.executeOnEventHandlerThread(localCheckboxMenuItem, new Runnable() {
/*    */       public void run() {
/* 50 */         localCheckboxMenuItem.setState(paramBoolean);
/* 51 */         WCheckboxMenuItemPeer.this.postEvent(new ItemEvent(localCheckboxMenuItem, 701, localCheckboxMenuItem.getLabel(), paramBoolean ? 1 : 2));
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WCheckboxMenuItemPeer
 * JD-Core Version:    0.6.2
 */