/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class PolicyListListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */ 
/*      */   PolicyListListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow)
/*      */   {
/* 2632 */     this.tool = paramPolicyTool;
/* 2633 */     this.tw = paramToolWindow;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 2640 */     ToolDialog localToolDialog = new ToolDialog(PolicyTool.rb.getString("Policy.Entry"), this.tool, this.tw, true);
/*      */ 
/* 2642 */     localToolDialog.displayPolicyEntryDialog(true);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.PolicyListListener
 * JD-Core Version:    0.6.2
 */