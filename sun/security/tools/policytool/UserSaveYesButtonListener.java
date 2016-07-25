/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.TextField;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class UserSaveYesButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private ToolDialog us;
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private int select;
/*      */ 
/*      */   UserSaveYesButtonListener(ToolDialog paramToolDialog, PolicyTool paramPolicyTool, ToolWindow paramToolWindow, int paramInt)
/*      */   {
/* 3511 */     this.us = paramToolDialog;
/* 3512 */     this.tool = paramPolicyTool;
/* 3513 */     this.tw = paramToolWindow;
/* 3514 */     this.select = paramInt;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 3520 */     this.us.setVisible(false);
/* 3521 */     this.us.dispose();
/*      */     try
/*      */     {
/* 3524 */       String str = ((TextField)this.tw.getComponent(1)).getText();
/*      */ 
/* 3526 */       if ((str == null) || (str.equals(""))) {
/* 3527 */         this.us.displaySaveAsDialog(this.select);
/*      */       }
/*      */       else
/*      */       {
/* 3533 */         this.tool.savePolicy(str);
/*      */ 
/* 3536 */         MessageFormat localMessageFormat = new MessageFormat(PolicyTool.rb.getString("Policy.successfully.written.to.filename"));
/*      */ 
/* 3539 */         Object[] arrayOfObject = { str };
/* 3540 */         this.tw.displayStatusDialog(null, localMessageFormat.format(arrayOfObject));
/*      */ 
/* 3544 */         this.us.userSaveContinue(this.tool, this.tw, this.us, this.select);
/*      */       }
/*      */     }
/*      */     catch (Exception localException) {
/* 3548 */       this.tw.displayErrorDialog(null, localException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.UserSaveYesButtonListener
 * JD-Core Version:    0.6.2
 */