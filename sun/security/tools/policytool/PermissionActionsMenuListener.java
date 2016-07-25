/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.Choice;
/*      */ import java.awt.TextField;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ 
/*      */ class PermissionActionsMenuListener
/*      */   implements ItemListener
/*      */ {
/*      */   private ToolDialog td;
/*      */ 
/*      */   PermissionActionsMenuListener(ToolDialog paramToolDialog)
/*      */   {
/* 3391 */     this.td = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void itemStateChanged(ItemEvent paramItemEvent)
/*      */   {
/* 3396 */     Choice localChoice = (Choice)this.td.getComponent(5);
/* 3397 */     localChoice.getAccessibleContext().setAccessibleName((String)paramItemEvent.getItem());
/*      */ 
/* 3399 */     if (((String)paramItemEvent.getItem()).indexOf(ToolDialog.PERM_ACTIONS) != -1) {
/* 3400 */       return;
/*      */     }
/* 3402 */     TextField localTextField = (TextField)this.td.getComponent(6);
/* 3403 */     if ((localTextField.getText() == null) || (localTextField.getText().equals(""))) {
/* 3404 */       localTextField.setText((String)paramItemEvent.getItem());
/*      */     }
/* 3406 */     else if (localTextField.getText().indexOf((String)paramItemEvent.getItem()) == -1)
/* 3407 */       localTextField.setText(localTextField.getText() + ", " + (String)paramItemEvent.getItem());
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.PermissionActionsMenuListener
 * JD-Core Version:    0.6.2
 */