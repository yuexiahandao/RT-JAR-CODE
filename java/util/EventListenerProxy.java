/*    */ package java.util;
/*    */ 
/*    */ public abstract class EventListenerProxy<T extends EventListener>
/*    */   implements EventListener
/*    */ {
/*    */   private final T listener;
/*    */ 
/*    */   public EventListenerProxy(T paramT)
/*    */   {
/* 64 */     this.listener = paramT;
/*    */   }
/*    */ 
/*    */   public T getListener()
/*    */   {
/* 73 */     return this.listener;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.EventListenerProxy
 * JD-Core Version:    0.6.2
 */