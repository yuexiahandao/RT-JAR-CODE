/*    */ package javax.swing.plaf.basic;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ 
/*    */ public class BasicTextPaneUI extends BasicEditorPaneUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 59 */     return new BasicTextPaneUI();
/*    */   }
/*    */ 
/*    */   protected String getPropertyPrefix()
/*    */   {
/* 77 */     return "TextPane";
/*    */   }
/*    */ 
/*    */   public void installUI(JComponent paramJComponent) {
/* 81 */     super.installUI(paramJComponent);
/*    */   }
/*    */ 
/*    */   protected void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*    */   {
/* 96 */     super.propertyChange(paramPropertyChangeEvent);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTextPaneUI
 * JD-Core Version:    0.6.2
 */