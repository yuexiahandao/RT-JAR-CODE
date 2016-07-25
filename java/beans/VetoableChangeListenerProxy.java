/*    */ package java.beans;
/*    */ 
/*    */ import java.util.EventListenerProxy;
/*    */ 
/*    */ public class VetoableChangeListenerProxy extends EventListenerProxy<VetoableChangeListener>
/*    */   implements VetoableChangeListener
/*    */ {
/*    */   private final String propertyName;
/*    */ 
/*    */   public VetoableChangeListenerProxy(String paramString, VetoableChangeListener paramVetoableChangeListener)
/*    */   {
/* 60 */     super(paramVetoableChangeListener);
/* 61 */     this.propertyName = paramString;
/*    */   }
/*    */ 
/*    */   public void vetoableChange(PropertyChangeEvent paramPropertyChangeEvent)
/*    */     throws PropertyVetoException
/*    */   {
/* 73 */     ((VetoableChangeListener)getListener()).vetoableChange(paramPropertyChangeEvent);
/*    */   }
/*    */ 
/*    */   public String getPropertyName()
/*    */   {
/* 82 */     return this.propertyName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.VetoableChangeListenerProxy
 * JD-Core Version:    0.6.2
 */