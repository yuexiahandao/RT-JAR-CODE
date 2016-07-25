/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ 
/*      */ class AddPermButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog td;
/*      */   private boolean editPolicyEntry;
/*      */ 
/*      */   AddPermButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog, boolean paramBoolean)
/*      */   {
/* 2964 */     this.tool = paramPolicyTool;
/* 2965 */     this.tw = paramToolWindow;
/* 2966 */     this.td = paramToolDialog;
/* 2967 */     this.editPolicyEntry = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 2973 */     this.td.displayPermissionDialog(this.editPolicyEntry, false);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.AddPermButtonListener
 * JD-Core Version:    0.6.2
 */