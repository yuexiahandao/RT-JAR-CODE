/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.List;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class EditPermButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog td;
/*      */   private boolean editPolicyEntry;
/*      */ 
/*      */   EditPermButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog, boolean paramBoolean)
/*      */   {
/* 3229 */     this.tool = paramPolicyTool;
/* 3230 */     this.tw = paramToolWindow;
/* 3231 */     this.td = paramToolDialog;
/* 3232 */     this.editPolicyEntry = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 3238 */     List localList = (List)this.td.getComponent(8);
/* 3239 */     int i = localList.getSelectedIndex();
/*      */ 
/* 3241 */     if (i < 0) {
/* 3242 */       this.tw.displayErrorDialog(this.td, new Exception(PolicyTool.rb.getString("No.permission.selected")));
/*      */ 
/* 3244 */       return;
/*      */     }
/* 3246 */     this.td.displayPermissionDialog(this.editPolicyEntry, true);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.EditPermButtonListener
 * JD-Core Version:    0.6.2
 */