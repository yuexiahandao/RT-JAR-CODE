/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.Choice;
/*      */ import java.awt.TextField;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ 
/*      */ class PermissionNameMenuListener
/*      */   implements ItemListener
/*      */ {
/*      */   private ToolDialog td;
/*      */ 
/*      */   PermissionNameMenuListener(ToolDialog paramToolDialog)
/*      */   {
/* 3366 */     this.td = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void itemStateChanged(ItemEvent paramItemEvent)
/*      */   {
/* 3371 */     Choice localChoice = (Choice)this.td.getComponent(3);
/* 3372 */     localChoice.getAccessibleContext().setAccessibleName(PolicyTool.splitToWords((String)paramItemEvent.getItem()));
/*      */ 
/* 3375 */     if (((String)paramItemEvent.getItem()).indexOf(ToolDialog.PERM_NAME) != -1) {
/* 3376 */       return;
/*      */     }
/* 3378 */     TextField localTextField = (TextField)this.td.getComponent(4);
/* 3379 */     localTextField.setText((String)paramItemEvent.getItem());
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.PermissionNameMenuListener
 * JD-Core Version:    0.6.2
 */