/*    */ package javax.swing.plaf.nimbus;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import javax.swing.JComboBox;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ class ComboBoxArrowButtonEditableState extends State
/*    */ {
/*    */   ComboBoxArrowButtonEditableState()
/*    */   {
/* 33 */     super("Editable");
/*    */   }
/*    */ 
/*    */   protected boolean isInState(JComponent paramJComponent)
/*    */   {
/* 38 */     Container localContainer = paramJComponent.getParent();
/* 39 */     return ((localContainer instanceof JComboBox)) && (((JComboBox)localContainer).isEditable());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ComboBoxArrowButtonEditableState
 * JD-Core Version:    0.6.2
 */