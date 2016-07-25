/*    */ package sun.awt;
/*    */ 
/*    */ import java.awt.AWTEvent;
/*    */ 
/*    */ public class EventQueueItem
/*    */ {
/*    */   public AWTEvent event;
/*    */   public EventQueueItem next;
/*    */ 
/*    */   public EventQueueItem(AWTEvent paramAWTEvent)
/*    */   {
/* 35 */     this.event = paramAWTEvent;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.EventQueueItem
 * JD-Core Version:    0.6.2
 */