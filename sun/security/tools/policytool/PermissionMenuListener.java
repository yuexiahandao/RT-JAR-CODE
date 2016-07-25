/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.Choice;
/*      */ import java.awt.TextField;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.text.Collator;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ 
/*      */ class PermissionMenuListener
/*      */   implements ItemListener
/*      */ {
/*      */   private ToolDialog td;
/*      */ 
/*      */   PermissionMenuListener(ToolDialog paramToolDialog)
/*      */   {
/* 3304 */     this.td = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void itemStateChanged(ItemEvent paramItemEvent)
/*      */   {
/* 3309 */     Choice localChoice1 = (Choice)this.td.getComponent(1);
/* 3310 */     Choice localChoice2 = (Choice)this.td.getComponent(3);
/* 3311 */     Choice localChoice3 = (Choice)this.td.getComponent(5);
/* 3312 */     TextField localTextField1 = (TextField)this.td.getComponent(4);
/*      */ 
/* 3314 */     TextField localTextField2 = (TextField)this.td.getComponent(6);
/*      */ 
/* 3316 */     TextField localTextField3 = (TextField)this.td.getComponent(2);
/* 3317 */     TextField localTextField4 = (TextField)this.td.getComponent(8);
/*      */ 
/* 3320 */     localChoice1.getAccessibleContext().setAccessibleName(PolicyTool.splitToWords((String)paramItemEvent.getItem()));
/*      */ 
/* 3324 */     if (PolicyTool.collator.compare((String)paramItemEvent.getItem(), ToolDialog.PERM) == 0) {
/* 3325 */       if ((localTextField3.getText() != null) && (localTextField3.getText().length() > 0))
/*      */       {
/* 3328 */         localPerm = ToolDialog.getPerm(localTextField3.getText(), true);
/* 3329 */         if (localPerm != null) {
/* 3330 */           localChoice1.select(localPerm.CLASS);
/*      */         }
/*      */       }
/* 3333 */       return;
/*      */     }
/*      */ 
/* 3337 */     if (localTextField3.getText().indexOf((String)paramItemEvent.getItem()) == -1) {
/* 3338 */       localTextField1.setText("");
/* 3339 */       localTextField2.setText("");
/* 3340 */       localTextField4.setText("");
/*      */     }
/*      */ 
/* 3347 */     Perm localPerm = ToolDialog.getPerm((String)paramItemEvent.getItem(), false);
/* 3348 */     if (localPerm == null)
/* 3349 */       localTextField3.setText("");
/*      */     else {
/* 3351 */       localTextField3.setText(localPerm.FULL_CLASS);
/*      */     }
/* 3353 */     this.td.setPermissionNames(localPerm, localChoice2, localTextField1);
/* 3354 */     this.td.setPermissionActions(localPerm, localChoice3, localTextField2);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.PermissionMenuListener
 * JD-Core Version:    0.6.2
 */