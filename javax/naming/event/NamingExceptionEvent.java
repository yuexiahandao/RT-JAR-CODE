/*    */ package javax.naming.event;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ import javax.naming.NamingException;
/*    */ 
/*    */ public class NamingExceptionEvent extends EventObject
/*    */ {
/*    */   private NamingException exception;
/*    */   private static final long serialVersionUID = -4877678086134736336L;
/*    */ 
/*    */   public NamingExceptionEvent(EventContext paramEventContext, NamingException paramNamingException)
/*    */   {
/* 62 */     super(paramEventContext);
/* 63 */     this.exception = paramNamingException;
/*    */   }
/*    */ 
/*    */   public NamingException getException()
/*    */   {
/* 71 */     return this.exception;
/*    */   }
/*    */ 
/*    */   public EventContext getEventContext()
/*    */   {
/* 80 */     return (EventContext)getSource();
/*    */   }
/*    */ 
/*    */   public void dispatch(NamingListener paramNamingListener)
/*    */   {
/* 90 */     paramNamingListener.namingExceptionThrown(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.event.NamingExceptionEvent
 * JD-Core Version:    0.6.2
 */