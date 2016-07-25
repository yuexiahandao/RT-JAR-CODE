/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ 
/*      */ class ToolWindowListener
/*      */   implements WindowListener
/*      */ {
/*      */   private ToolWindow tw;
/*      */ 
/*      */   ToolWindowListener(ToolWindow paramToolWindow)
/*      */   {
/* 2587 */     this.tw = paramToolWindow;
/*      */   }
/*      */ 
/*      */   public void windowOpened(WindowEvent paramWindowEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void windowClosing(WindowEvent paramWindowEvent)
/*      */   {
/* 2601 */     this.tw.setVisible(false);
/* 2602 */     this.tw.dispose();
/* 2603 */     System.exit(0);
/*      */   }
/*      */ 
/*      */   public void windowClosed(WindowEvent paramWindowEvent) {
/* 2607 */     System.exit(0);
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
 * Qualified Name:     sun.security.tools.policytool.ToolWindowListener
 * JD-Core Version:    0.6.2
 */