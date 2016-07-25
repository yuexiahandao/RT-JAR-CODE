/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ 
/*      */ class UserSaveCancelButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private ToolDialog us;
/*      */ 
/*      */   UserSaveCancelButtonListener(ToolDialog paramToolDialog)
/*      */   {
/* 3589 */     this.us = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent) {
/* 3593 */     this.us.setVisible(false);
/* 3594 */     this.us.dispose();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.UserSaveCancelButtonListener
 * JD-Core Version:    0.6.2
 */