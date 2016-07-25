/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ 
/*      */ class AddPrinButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog td;
/*      */   private boolean editPolicyEntry;
/*      */ 
/*      */   AddPrinButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog, boolean paramBoolean)
/*      */   {
/* 2939 */     this.tool = paramPolicyTool;
/* 2940 */     this.tw = paramToolWindow;
/* 2941 */     this.td = paramToolDialog;
/* 2942 */     this.editPolicyEntry = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 2948 */     this.td.displayPrincipalDialog(this.editPolicyEntry, false);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.AddPrinButtonListener
 * JD-Core Version:    0.6.2
 */