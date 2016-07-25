/*    */ package org.w3c.dom.events;
/*    */ 
/*    */ public class EventException extends RuntimeException
/*    */ {
/*    */   public short code;
/*    */   public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;
/*    */ 
/*    */   public EventException(short code, String message)
/*    */   {
/* 52 */     super(message);
/* 53 */     this.code = code;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.events.EventException
 * JD-Core Version:    0.6.2
 */