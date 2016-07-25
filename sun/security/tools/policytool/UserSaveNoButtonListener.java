/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ 
/*      */ class UserSaveNoButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog us;
/*      */   private int select;
/*      */ 
/*      */   UserSaveNoButtonListener(ToolDialog paramToolDialog, PolicyTool paramPolicyTool, ToolWindow paramToolWindow, int paramInt)
/*      */   {
/* 3565 */     this.us = paramToolDialog;
/* 3566 */     this.tool = paramPolicyTool;
/* 3567 */     this.tw = paramToolWindow;
/* 3568 */     this.select = paramInt;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent) {
/* 3572 */     this.us.setVisible(false);
/* 3573 */     this.us.dispose();
/*      */ 
/* 3577 */     this.us.userSaveContinue(this.tool, this.tw, this.us, this.select);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.UserSaveNoButtonListener
 * JD-Core Version:    0.6.2
 */