/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class EditPrinButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog td;
/*      */   private boolean editPolicyEntry;
/*      */ 
/*      */   EditPrinButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog, boolean paramBoolean)
/*      */   {
/* 3190 */     this.tool = paramPolicyTool;
/* 3191 */     this.tw = paramToolWindow;
/* 3192 */     this.td = paramToolDialog;
/* 3193 */     this.editPolicyEntry = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 3199 */     TaggedList localTaggedList = (TaggedList)this.td.getComponent(6);
/* 3200 */     int i = localTaggedList.getSelectedIndex();
/*      */ 
/* 3202 */     if (i < 0) {
/* 3203 */       this.tw.displayErrorDialog(this.td, new Exception(PolicyTool.rb.getString("No.principal.selected")));
/*      */ 
/* 3205 */       return;
/*      */     }
/* 3207 */     this.td.displayPrincipalDialog(this.editPolicyEntry, true);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.EditPrinButtonListener
 * JD-Core Version:    0.6.2
 */