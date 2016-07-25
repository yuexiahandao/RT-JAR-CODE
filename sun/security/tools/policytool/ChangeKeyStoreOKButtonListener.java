/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.TextField;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class ChangeKeyStoreOKButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog td;
/*      */ 
/*      */   ChangeKeyStoreOKButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog)
/*      */   {
/* 2892 */     this.tool = paramPolicyTool;
/* 2893 */     this.tw = paramToolWindow;
/* 2894 */     this.td = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/* 2899 */     String str1 = ((TextField)this.td.getComponent(1)).getText().trim();
/*      */ 
/* 2901 */     String str2 = ((TextField)this.td.getComponent(3)).getText().trim();
/*      */ 
/* 2903 */     String str3 = ((TextField)this.td.getComponent(5)).getText().trim();
/*      */ 
/* 2905 */     String str4 = ((TextField)this.td.getComponent(7)).getText().trim();
/*      */     try
/*      */     {
/* 2909 */       this.tool.openKeyStore(str1.length() == 0 ? null : str1, str2.length() == 0 ? null : str2, str3.length() == 0 ? null : str3, str4.length() == 0 ? null : str4);
/*      */ 
/* 2914 */       this.tool.modified = true;
/*      */     } catch (Exception localException) {
/* 2916 */       MessageFormat localMessageFormat = new MessageFormat(PolicyTool.rb.getString("Unable.to.open.KeyStore.ex.toString."));
/*      */ 
/* 2918 */       Object[] arrayOfObject = { localException.toString() };
/* 2919 */       this.tw.displayErrorDialog(this.td, localMessageFormat.format(arrayOfObject));
/* 2920 */       return;
/*      */     }
/*      */ 
/* 2923 */     this.td.dispose();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.ChangeKeyStoreOKButtonListener
 * JD-Core Version:    0.6.2
 */