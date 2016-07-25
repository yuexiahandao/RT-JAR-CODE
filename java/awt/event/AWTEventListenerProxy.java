/*    */ package java.awt.event;
/*    */ 
/*    */ import java.awt.AWTEvent;
/*    */ import java.util.EventListenerProxy;
/*    */ 
/*    */ public class AWTEventListenerProxy extends EventListenerProxy<AWTEventListener>
/*    */   implements AWTEventListener
/*    */ {
/*    */   private final long eventMask;
/*    */ 
/*    */   public AWTEventListenerProxy(long paramLong, AWTEventListener paramAWTEventListener)
/*    */   {
/* 60 */     super(paramAWTEventListener);
/* 61 */     this.eventMask = paramLong;
/*    */   }
/*    */ 
/*    */   public void eventDispatched(AWTEvent paramAWTEvent)
/*    */   {
/* 70 */     ((AWTEventListener)getListener()).eventDispatched(paramAWTEvent);
/*    */   }
/*    */ 
/*    */   public long getEventMask()
/*    */   {
/* 79 */     return this.eventMask;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.AWTEventListenerProxy
 * JD-Core Version:    0.6.2
 */