/*    */ package java.beans.beancontext;
/*    */ 
/*    */ public class BeanContextServiceRevokedEvent extends BeanContextEvent
/*    */ {
/*    */   private static final long serialVersionUID = -1295543154724961754L;
/*    */   protected Class serviceClass;
/*    */   private boolean invalidateRefs;
/*    */ 
/*    */   public BeanContextServiceRevokedEvent(BeanContextServices paramBeanContextServices, Class paramClass, boolean paramBoolean)
/*    */   {
/* 50 */     super(paramBeanContextServices);
/*    */ 
/* 52 */     this.serviceClass = paramClass;
/* 53 */     this.invalidateRefs = paramBoolean;
/*    */   }
/*    */ 
/*    */   public BeanContextServices getSourceAsBeanContextServices()
/*    */   {
/* 62 */     return (BeanContextServices)getBeanContext();
/*    */   }
/*    */ 
/*    */   public Class getServiceClass()
/*    */   {
/* 70 */     return this.serviceClass;
/*    */   }
/*    */ 
/*    */   public boolean isServiceClass(Class paramClass)
/*    */   {
/* 80 */     return this.serviceClass.equals(paramClass);
/*    */   }
/*    */ 
/*    */   public boolean isCurrentServiceInvalidNow()
/*    */   {
/* 88 */     return this.invalidateRefs;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextServiceRevokedEvent
 * JD-Core Version:    0.6.2
 */