/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import sun.security.provider.PolicyParser.PrincipalEntry;
/*      */ 
/*      */ class NewPolicyPrinOKButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog listDialog;
/*      */   private ToolDialog infoDialog;
/*      */   private boolean edit;
/*      */ 
/*      */   NewPolicyPrinOKButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog1, ToolDialog paramToolDialog2, boolean paramBoolean)
/*      */   {
/* 2993 */     this.tool = paramPolicyTool;
/* 2994 */     this.tw = paramToolWindow;
/* 2995 */     this.listDialog = paramToolDialog1;
/* 2996 */     this.infoDialog = paramToolDialog2;
/* 2997 */     this.edit = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/*      */     try
/*      */     {
/* 3004 */       PolicyParser.PrincipalEntry localPrincipalEntry = this.infoDialog.getPrinFromDialog();
/*      */ 
/* 3006 */       if (localPrincipalEntry != null) {
/*      */         try {
/* 3008 */           this.tool.verifyPrincipal(localPrincipalEntry.getPrincipalClass(), localPrincipalEntry.getPrincipalName());
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException) {
/* 3011 */           localObject = new MessageFormat(PolicyTool.rb.getString("Warning.Class.not.found.class"));
/*      */ 
/* 3014 */           Object[] arrayOfObject = { localPrincipalEntry.getPrincipalClass() };
/* 3015 */           this.tool.warnings.addElement(((MessageFormat)localObject).format(arrayOfObject));
/* 3016 */           this.tw.displayStatusDialog(this.infoDialog, ((MessageFormat)localObject).format(arrayOfObject));
/*      */         }
/*      */ 
/* 3020 */         TaggedList localTaggedList = (TaggedList)this.listDialog.getComponent(6);
/*      */ 
/* 3023 */         Object localObject = ToolDialog.PrincipalEntryToUserFriendlyString(localPrincipalEntry);
/* 3024 */         if (this.edit)
/*      */         {
/* 3026 */           int i = localTaggedList.getSelectedIndex();
/* 3027 */           localTaggedList.replaceTaggedItem((String)localObject, localPrincipalEntry, i);
/*      */         }
/*      */         else {
/* 3030 */           localTaggedList.addTaggedItem((String)localObject, localPrincipalEntry);
/*      */         }
/*      */       }
/* 3033 */       this.infoDialog.dispose();
/*      */     } catch (Exception localException) {
/* 3035 */       this.tw.displayErrorDialog(this.infoDialog, localException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.NewPolicyPrinOKButtonListener
 * JD-Core Version:    0.6.2
 */