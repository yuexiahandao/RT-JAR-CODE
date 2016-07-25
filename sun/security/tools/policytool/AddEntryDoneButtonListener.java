/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.List;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.text.Collator;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import sun.security.provider.PolicyParser.GrantEntry;
/*      */ 
/*      */ class AddEntryDoneButtonListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */   private ToolDialog td;
/*      */   private boolean edit;
/*      */ 
/*      */   AddEntryDoneButtonListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow, ToolDialog paramToolDialog, boolean paramBoolean)
/*      */   {
/* 2828 */     this.tool = paramPolicyTool;
/* 2829 */     this.tw = paramToolWindow;
/* 2830 */     this.td = paramToolDialog;
/* 2831 */     this.edit = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/*      */     try
/*      */     {
/* 2838 */       PolicyEntry localPolicyEntry = this.td.getPolicyEntryFromDialog();
/* 2839 */       PolicyParser.GrantEntry localGrantEntry = localPolicyEntry.getGrantEntry();
/*      */       int i;
/*      */       Object localObject2;
/* 2842 */       if (localGrantEntry.signedBy != null) {
/* 2843 */         localObject1 = this.tool.parseSigners(localGrantEntry.signedBy);
/* 2844 */         for (i = 0; i < localObject1.length; i++) {
/* 2845 */           localObject2 = this.tool.getPublicKeyAlias(localObject1[i]);
/* 2846 */           if (localObject2 == null) {
/* 2847 */             MessageFormat localMessageFormat = new MessageFormat(PolicyTool.rb.getString("Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured."));
/*      */ 
/* 2850 */             Object[] arrayOfObject = { localObject1[i] };
/* 2851 */             this.tool.warnings.addElement(localMessageFormat.format(arrayOfObject));
/* 2852 */             this.tw.displayStatusDialog(this.td, localMessageFormat.format(arrayOfObject));
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2858 */       Object localObject1 = (List)this.tw.getComponent(3);
/* 2859 */       if (this.edit) {
/* 2860 */         i = ((List)localObject1).getSelectedIndex();
/* 2861 */         this.tool.addEntry(localPolicyEntry, i);
/* 2862 */         localObject2 = localPolicyEntry.headerToString();
/* 2863 */         if (PolicyTool.collator.compare((String)localObject2, ((List)localObject1).getItem(i)) != 0)
/*      */         {
/* 2865 */           this.tool.modified = true;
/* 2866 */         }((List)localObject1).replaceItem((String)localObject2, i);
/*      */       } else {
/* 2868 */         this.tool.addEntry(localPolicyEntry, -1);
/* 2869 */         ((List)localObject1).add(localPolicyEntry.headerToString());
/* 2870 */         this.tool.modified = true;
/*      */       }
/* 2872 */       this.td.setVisible(false);
/* 2873 */       this.td.dispose();
/*      */     }
/*      */     catch (Exception localException) {
/* 2876 */       this.tw.displayErrorDialog(this.td, localException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.AddEntryDoneButtonListener
 * JD-Core Version:    0.6.2
 */