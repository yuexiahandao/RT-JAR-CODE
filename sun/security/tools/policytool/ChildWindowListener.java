/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ 
/*      */ class ChildWindowListener
/*      */   implements WindowListener
/*      */ {
/*      */   private ToolDialog td;
/*      */ 
/*      */   ChildWindowListener(ToolDialog paramToolDialog)
/*      */   {
/* 3420 */     this.td = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void windowOpened(WindowEvent paramWindowEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void windowClosing(WindowEvent paramWindowEvent) {
/* 3428 */     this.td.setVisible(false);
/* 3429 */     this.td.dispose();
/*      */   }
/*      */ 
/*      */   public void windowClosed(WindowEvent paramWindowEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void windowIconified(WindowEvent paramWindowEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void windowDeiconified(WindowEvent paramWindowEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void windowActivated(WindowEvent paramWindowEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void windowDeactivated(WindowEvent paramWindowEvent)
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.ChildWindowListener
 * JD-Core Version:    0.6.2
 */