/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.List;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.text.Collator;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class MainWindowListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */ 
/*      */   MainWindowListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow)
/*      */   {
/* 2752 */     this.tool = paramPolicyTool;
/* 2753 */     this.tw = paramToolWindow;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/*      */     Object localObject;
/* 2758 */     if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.ADD_POLICY_ENTRY) == 0)
/*      */     {
/* 2762 */       localObject = new ToolDialog(PolicyTool.rb.getString("Policy.Entry"), this.tool, this.tw, true);
/*      */ 
/* 2764 */       ((ToolDialog)localObject).displayPolicyEntryDialog(false);
/*      */     }
/*      */     else
/*      */     {
/*      */       int i;
/*      */       ToolDialog localToolDialog;
/* 2766 */       if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.REMOVE_POLICY_ENTRY) == 0)
/*      */       {
/* 2770 */         localObject = (List)this.tw.getComponent(3);
/* 2771 */         i = ((List)localObject).getSelectedIndex();
/* 2772 */         if (i < 0) {
/* 2773 */           this.tw.displayErrorDialog(null, new Exception(PolicyTool.rb.getString("No.Policy.Entry.selected")));
/*      */ 
/* 2775 */           return;
/*      */         }
/*      */ 
/* 2779 */         localToolDialog = new ToolDialog(PolicyTool.rb.getString("Remove.Policy.Entry"), this.tool, this.tw, true);
/*      */ 
/* 2781 */         localToolDialog.displayConfirmRemovePolicyEntry();
/*      */       }
/* 2783 */       else if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.EDIT_POLICY_ENTRY) == 0)
/*      */       {
/* 2787 */         localObject = (List)this.tw.getComponent(3);
/* 2788 */         i = ((List)localObject).getSelectedIndex();
/* 2789 */         if (i < 0) {
/* 2790 */           this.tw.displayErrorDialog(null, new Exception(PolicyTool.rb.getString("No.Policy.Entry.selected")));
/*      */ 
/* 2792 */           return;
/*      */         }
/*      */ 
/* 2796 */         localToolDialog = new ToolDialog(PolicyTool.rb.getString("Policy.Entry"), this.tool, this.tw, true);
/*      */ 
/* 2798 */         localToolDialog.displayPolicyEntryDialog(true);
/*      */       }
/* 2800 */       else if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.EDIT_KEYSTORE) == 0)
/*      */       {
/* 2804 */         localObject = new ToolDialog(PolicyTool.rb.getString("KeyStore"), this.tool, this.tw, true);
/*      */ 
/* 2806 */         ((ToolDialog)localObject).keyStoreDialog(0);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.MainWindowListener
 * JD-Core Version:    0.6.2
 */