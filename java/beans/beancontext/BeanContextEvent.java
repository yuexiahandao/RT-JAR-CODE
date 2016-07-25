/*    */ package java.beans.beancontext;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ 
/*    */ public abstract class BeanContextEvent extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 7267998073569045052L;
/*    */   protected BeanContext propagatedFrom;
/*    */ 
/*    */   protected BeanContextEvent(BeanContext paramBeanContext)
/*    */   {
/* 59 */     super(paramBeanContext);
/*    */   }
/*    */ 
/*    */   public BeanContext getBeanContext()
/*    */   {
/* 66 */     return (BeanContext)getSource();
/*    */   }
/*    */ 
/*    */   public synchronized void setPropagatedFrom(BeanContext paramBeanContext)
/*    */   {
/* 74 */     this.propagatedFrom = paramBeanContext;
/*    */   }
/*    */ 
/*    */   public synchronized BeanContext getPropagatedFrom()
/*    */   {
/* 83 */     return this.propagatedFrom;
/*    */   }
/*    */ 
/*    */   public synchronized boolean isPropagated()
/*    */   {
/* 93 */     return this.propagatedFrom != null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextEvent
 * JD-Core Version:    0.6.2
 */