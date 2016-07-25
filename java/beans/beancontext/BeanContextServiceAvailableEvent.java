/*    */ package java.beans.beancontext;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class BeanContextServiceAvailableEvent extends BeanContextEvent
/*    */ {
/*    */   private static final long serialVersionUID = -5333985775656400778L;
/*    */   protected Class serviceClass;
/*    */ 
/*    */   public BeanContextServiceAvailableEvent(BeanContextServices paramBeanContextServices, Class paramClass)
/*    */   {
/* 51 */     super(paramBeanContextServices);
/*    */ 
/* 53 */     this.serviceClass = paramClass;
/*    */   }
/*    */ 
/*    */   public BeanContextServices getSourceAsBeanContextServices()
/*    */   {
/* 61 */     return (BeanContextServices)getBeanContext();
/*    */   }
/*    */ 
/*    */   public Class getServiceClass()
/*    */   {
/* 68 */     return this.serviceClass;
/*    */   }
/*    */ 
/*    */   public Iterator getCurrentServiceSelectors()
/*    */   {
/* 75 */     return ((BeanContextServices)getSource()).getCurrentServiceSelectors(this.serviceClass);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextServiceAvailableEvent
 * JD-Core Version:    0.6.2
 */