/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.List;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ 
/*      */ class ConfirmRemovePolicyEntryOKButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog us;
/*      */ 
/*      */   ConfirmRemovePolicyEntryOKButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog)
/*      */   {
/* 3612 */     this.tool = paramPolicyTool;
/* 3613 */     this.tw = paramToolWindow;
/* 3614 */     this.us = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 3619 */     List localList = (List)this.tw.getComponent(3);
/* 3620 */     int i = localList.getSelectedIndex();
/* 3621 */     PolicyEntry[] arrayOfPolicyEntry = this.tool.getEntry();
/* 3622 */     this.tool.removeEntry(arrayOfPolicyEntry[i]);
/*      */ 
/* 3625 */     localList = new List(40, false);
/* 3626 */     localList.addActionListener(new PolicyListListener(this.tool, this.tw));
/* 3627 */     arrayOfPolicyEntry = this.tool.getEntry();
/* 3628 */     if (arrayOfPolicyEntry != null) {
/* 3629 */       for (int j = 0; j < arrayOfPolicyEntry.length; j++)
/* 3630 */         localList.add(arrayOfPolicyEntry[j].headerToString());
/*      */     }
/* 3632 */     this.tw.replacePolicyList(localList);
/* 3633 */     this.us.setVisible(false);
/* 3634 */     this.us.dispose();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.ConfirmRemovePolicyEntryOKButtonListener
 * JD-Core Version:    0.6.2
 */