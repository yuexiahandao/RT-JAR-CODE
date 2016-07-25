/*    */ package java.beans;
/*    */ 
/*    */ import java.util.EventListenerProxy;
/*    */ 
/*    */ public class PropertyChangeListenerProxy extends EventListenerProxy<PropertyChangeListener>
/*    */   implements PropertyChangeListener
/*    */ {
/*    */   private final String propertyName;
/*    */ 
/*    */   public PropertyChangeListenerProxy(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*    */   {
/* 60 */     super(paramPropertyChangeListener);
/* 61 */     this.propertyName = paramString;
/*    */   }
/*    */ 
/*    */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*    */   {
/* 70 */     ((PropertyChangeListener)getListener()).propertyChange(paramPropertyChangeEvent);
/*    */   }
/*    */ 
/*    */   public String getPropertyName()
/*    */   {
/* 79 */     return this.propertyName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PropertyChangeListenerProxy
 * JD-Core Version:    0.6.2
 */