/*    */ package javax.swing;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.FocusTraversalPolicy;
/*    */ 
/*    */ public abstract class InternalFrameFocusTraversalPolicy extends FocusTraversalPolicy
/*    */ {
/*    */   public Component getInitialComponent(JInternalFrame paramJInternalFrame)
/*    */   {
/* 66 */     return getDefaultComponent(paramJInternalFrame);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.InternalFrameFocusTraversalPolicy
 * JD-Core Version:    0.6.2
 */