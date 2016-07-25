/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import sun.security.provider.PolicyParser.PermissionEntry;
/*      */ 
/*      */ class NewPolicyPermOKButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog listDialog;
/*      */   private ToolDialog infoDialog;
/*      */   private boolean edit;
/*      */ 
/*      */   NewPolicyPermOKButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog1, ToolDialog paramToolDialog2, boolean paramBoolean)
/*      */   {
/* 3056 */     this.tool = paramPolicyTool;
/* 3057 */     this.tw = paramToolWindow;
/* 3058 */     this.listDialog = paramToolDialog1;
/* 3059 */     this.infoDialog = paramToolDialog2;
/* 3060 */     this.edit = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/*      */     try
/*      */     {
/* 3067 */       PolicyParser.PermissionEntry localPermissionEntry = this.infoDialog.getPermFromDialog();
/*      */       try
/*      */       {
/* 3071 */         this.tool.verifyPermission(localPermissionEntry.permission, localPermissionEntry.name, localPermissionEntry.action);
/*      */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 3073 */         localObject = new MessageFormat(PolicyTool.rb.getString("Warning.Class.not.found.class"));
/*      */ 
/* 3075 */         Object[] arrayOfObject = { localPermissionEntry.permission };
/* 3076 */         this.tool.warnings.addElement(((MessageFormat)localObject).format(arrayOfObject));
/* 3077 */         this.tw.displayStatusDialog(this.infoDialog, ((MessageFormat)localObject).format(arrayOfObject));
/*      */       }
/*      */ 
/* 3081 */       TaggedList localTaggedList = (TaggedList)this.listDialog.getComponent(8);
/*      */ 
/* 3084 */       Object localObject = ToolDialog.PermissionEntryToUserFriendlyString(localPermissionEntry);
/* 3085 */       if (this.edit)
/*      */       {
/* 3087 */         int i = localTaggedList.getSelectedIndex();
/* 3088 */         localTaggedList.replaceTaggedItem((String)localObject, localPermissionEntry, i);
/*      */       }
/*      */       else {
/* 3091 */         localTaggedList.addTaggedItem((String)localObject, localPermissionEntry);
/*      */       }
/* 3093 */       this.infoDialog.dispose();
/*      */     }
/*      */     catch (InvocationTargetException localInvocationTargetException) {
/* 3096 */       this.tw.displayErrorDialog(this.infoDialog, localInvocationTargetException.getTargetException());
/*      */     } catch (Exception localException) {
/* 3098 */       this.tw.displayErrorDialog(this.infoDialog, localException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.NewPolicyPermOKButtonListener
 * JD-Core Version:    0.6.2
 */