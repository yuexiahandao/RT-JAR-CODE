/*    */ package java.beans;
/*    */ 
/*    */ public class PropertyVetoException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 129596057694162164L;
/*    */   private PropertyChangeEvent evt;
/*    */ 
/*    */   public PropertyVetoException(String paramString, PropertyChangeEvent paramPropertyChangeEvent)
/*    */   {
/* 46 */     super(paramString);
/* 47 */     this.evt = paramPropertyChangeEvent;
/*    */   }
/*    */ 
/*    */   public PropertyChangeEvent getPropertyChangeEvent()
/*    */   {
/* 56 */     return this.evt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PropertyVetoException
 * JD-Core Version:    0.6.2
 */