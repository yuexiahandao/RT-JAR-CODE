/*    */ package javax.swing.plaf.nimbus;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JViewport;
/*    */ 
/*    */ class TextAreaNotInScrollPaneState extends State
/*    */ {
/*    */   TextAreaNotInScrollPaneState()
/*    */   {
/* 33 */     super("NotInScrollPane");
/*    */   }
/*    */ 
/*    */   protected boolean isInState(JComponent paramJComponent)
/*    */   {
/* 38 */     return !(paramJComponent.getParent() instanceof JViewport);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TextAreaNotInScrollPaneState
 * JD-Core Version:    0.6.2
 */