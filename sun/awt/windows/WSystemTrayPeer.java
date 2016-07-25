/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.SystemTray;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.peer.SystemTrayPeer;
/*    */ 
/*    */ public class WSystemTrayPeer extends WObjectPeer
/*    */   implements SystemTrayPeer
/*    */ {
/*    */   WSystemTrayPeer(SystemTray paramSystemTray)
/*    */   {
/* 35 */     this.target = paramSystemTray;
/*    */   }
/*    */ 
/*    */   public Dimension getTrayIconSize() {
/* 39 */     return new Dimension(16, 16);
/*    */   }
/*    */ 
/*    */   public boolean isSupported() {
/* 43 */     return ((WToolkit)Toolkit.getDefaultToolkit()).isTraySupported();
/*    */   }
/*    */ 
/*    */   protected void disposeImpl()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WSystemTrayPeer
 * JD-Core Version:    0.6.2
 */