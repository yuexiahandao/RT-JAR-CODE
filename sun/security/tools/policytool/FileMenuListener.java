/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.TextField;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.text.Collator;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class FileMenuListener
/*      */   implements ActionListener
/*      */ {
/*      */   private PolicyTool tool;
/*      */   private ToolWindow tw;
/*      */ 
/*      */   FileMenuListener(PolicyTool paramPolicyTool, ToolWindow paramToolWindow)
/*      */   {
/* 2655 */     this.tool = paramPolicyTool;
/* 2656 */     this.tw = paramToolWindow;
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/*      */     Object localObject1;
/* 2661 */     if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.QUIT) == 0)
/*      */     {
/* 2664 */       localObject1 = new ToolDialog(PolicyTool.rb.getString("Save.Changes"), this.tool, this.tw, true);
/*      */ 
/* 2666 */       ((ToolDialog)localObject1).displayUserSave(1);
/*      */     }
/* 2671 */     else if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.NEW_POLICY_FILE) == 0)
/*      */     {
/* 2675 */       localObject1 = new ToolDialog(PolicyTool.rb.getString("Save.Changes"), this.tool, this.tw, true);
/*      */ 
/* 2677 */       ((ToolDialog)localObject1).displayUserSave(2);
/*      */     }
/* 2682 */     else if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.OPEN_POLICY_FILE) == 0)
/*      */     {
/* 2686 */       localObject1 = new ToolDialog(PolicyTool.rb.getString("Save.Changes"), this.tool, this.tw, true);
/*      */ 
/* 2688 */       ((ToolDialog)localObject1).displayUserSave(3);
/*      */     }
/* 2693 */     else if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.SAVE_POLICY_FILE) == 0)
/*      */     {
/* 2697 */       localObject1 = ((TextField)this.tw.getComponent(1)).getText();
/*      */       Object localObject2;
/* 2701 */       if ((localObject1 == null) || (((String)localObject1).length() == 0))
/*      */       {
/* 2703 */         localObject2 = new ToolDialog(PolicyTool.rb.getString("Save.As"), this.tool, this.tw, true);
/*      */ 
/* 2705 */         ((ToolDialog)localObject2).displaySaveAsDialog(0);
/*      */       }
/*      */       else {
/*      */         try {
/* 2709 */           this.tool.savePolicy((String)localObject1);
/*      */ 
/* 2712 */           localObject2 = new MessageFormat(PolicyTool.rb.getString("Policy.successfully.written.to.filename"));
/*      */ 
/* 2715 */           Object[] arrayOfObject = { localObject1 };
/* 2716 */           this.tw.displayStatusDialog(null, ((MessageFormat)localObject2).format(arrayOfObject));
/*      */         } catch (FileNotFoundException localFileNotFoundException) {
/* 2718 */           if ((localObject1 == null) || (((String)localObject1).equals(""))) {
/* 2719 */             this.tw.displayErrorDialog(null, new FileNotFoundException(PolicyTool.rb.getString("null.filename")));
/*      */           }
/*      */           else
/* 2722 */             this.tw.displayErrorDialog(null, localFileNotFoundException);
/*      */         }
/*      */         catch (Exception localException) {
/* 2725 */           this.tw.displayErrorDialog(null, localException);
/*      */         }
/*      */       }
/* 2728 */     } else if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.SAVE_AS_POLICY_FILE) == 0)
/*      */     {
/* 2732 */       localObject1 = new ToolDialog(PolicyTool.rb.getString("Save.As"), this.tool, this.tw, true);
/*      */ 
/* 2734 */       ((ToolDialog)localObject1).displaySaveAsDialog(0);
/*      */     }
/* 2736 */     else if (PolicyTool.collator.compare(paramActionEvent.getActionCommand(), ToolWindow.VIEW_WARNINGS) == 0)
/*      */     {
/* 2738 */       this.tw.displayWarningLog(null);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.FileMenuListener
 * JD-Core Version:    0.6.2
 */