/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ 
/*      */ class ErrorOKButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private ToolDialog ed;
/*      */ 
/*      */   ErrorOKButtonListener(ToolDialog paramToolDialog)
/*      */   {
/* 3473 */     this.ed = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent) {
/* 3477 */     this.ed.setVisible(false);
/* 3478 */     this.ed.dispose();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.ErrorOKButtonListener
 * JD-Core Version:    0.6.2
 */