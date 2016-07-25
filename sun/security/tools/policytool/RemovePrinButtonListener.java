/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class RemovePrinButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog td;
/*      */   private boolean edit;
/*      */ 
/*      */   RemovePrinButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog, boolean paramBoolean)
/*      */   {
/* 3115 */     this.tool = paramPolicyTool;
/* 3116 */     this.tw = paramToolWindow;
/* 3117 */     this.td = paramToolDialog;
/* 3118 */     this.edit = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 3124 */     TaggedList localTaggedList = (TaggedList)this.td.getComponent(6);
/* 3125 */     int i = localTaggedList.getSelectedIndex();
/*      */ 
/* 3127 */     if (i < 0) {
/* 3128 */       this.tw.displayErrorDialog(this.td, new Exception(PolicyTool.rb.getString("No.principal.selected")));
/*      */ 
/* 3130 */       return;
/*      */     }
/*      */ 
/* 3133 */     localTaggedList.removeTaggedItem(i);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.RemovePrinButtonListener
 * JD-Core Version:    0.6.2
 */