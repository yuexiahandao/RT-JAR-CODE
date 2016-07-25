/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.awt.Choice;
/*      */ import java.awt.TextField;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ 
/*      */ class PrincipalTypeMenuListener
/*      */   implements ItemListener
/*      */ {
/*      */   private ToolDialog td;
/*      */ 
/*      */   PrincipalTypeMenuListener(ToolDialog paramToolDialog)
/*      */   {
/* 3258 */     this.td = paramToolDialog;
/*      */   }
/*      */ 
/*      */   public void itemStateChanged(ItemEvent paramItemEvent)
/*      */   {
/* 3263 */     Choice localChoice = (Choice)this.td.getComponent(1);
/* 3264 */     TextField localTextField1 = (TextField)this.td.getComponent(2);
/*      */ 
/* 3266 */     TextField localTextField2 = (TextField)this.td.getComponent(4);
/*      */ 
/* 3269 */     localChoice.getAccessibleContext().setAccessibleName(PolicyTool.splitToWords((String)paramItemEvent.getItem()));
/*      */ 
/* 3271 */     if (((String)paramItemEvent.getItem()).equals(ToolDialog.PRIN_TYPE))
/*      */     {
/* 3273 */       if ((localTextField1.getText() != null) && (localTextField1.getText().length() > 0))
/*      */       {
/* 3275 */         localPrin = ToolDialog.getPrin(localTextField1.getText(), true);
/* 3276 */         localChoice.select(localPrin.CLASS);
/*      */       }
/* 3278 */       return;
/*      */     }
/*      */ 
/* 3282 */     if (localTextField1.getText().indexOf((String)paramItemEvent.getItem()) == -1) {
/* 3283 */       localTextField2.setText("");
/*      */     }
/*      */ 
/* 3289 */     Prin localPrin = ToolDialog.getPrin((String)paramItemEvent.getItem(), false);
/* 3290 */     if (localPrin != null)
/* 3291 */       localTextField1.setText(localPrin.FULL_CLASS);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.PrincipalTypeMenuListener
 * JD-Core Version:    0.6.2
 */