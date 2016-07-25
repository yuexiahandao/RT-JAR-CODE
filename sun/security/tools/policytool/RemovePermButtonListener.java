/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class RemovePermButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog td;
/*      */   private boolean edit;
/*      */ 
/*      */   RemovePermButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog, boolean paramBoolean)
/*      */   {
/* 3149 */     this.tool = paramPolicyTool;
/* 3150 */     this.tw = paramToolWindow;
/* 3151 */     this.td = paramToolDialog;
/* 3152 */     this.edit = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 3158 */     TaggedList localTaggedList = (TaggedList)this.td.getComponent(8);
/* 3159 */     int i = localTaggedList.getSelectedIndex();
/*      */ 
/* 3161 */     if (i < 0) {
/* 3162 */       this.tw.displayErrorDialog(this.td, new Exception(PolicyTool.rb.getString("No.permission.selected")));
/*      */ 
/* 3164 */       return;
/*      */     }
/*      */ 
/* 3167 */     localTaggedList.removeTaggedItem(i);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.RemovePermButtonListener
 * JD-Core Version:    0.6.2
 */