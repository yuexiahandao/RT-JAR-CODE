/*    */ package javax.swing.plaf.nimbus;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JProgressBar;
/*    */ 
/*    */ class ProgressBarFinishedState extends State
/*    */ {
/*    */   ProgressBarFinishedState()
/*    */   {
/* 33 */     super("Finished");
/*    */   }
/*    */ 
/*    */   protected boolean isInState(JComponent paramJComponent)
/*    */   {
/* 38 */     return ((paramJComponent instanceof JProgressBar)) && (((JProgressBar)paramJComponent).getPercentComplete() == 1.0D);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ProgressBarFinishedState
 * JD-Core Version:    0.6.2
 */