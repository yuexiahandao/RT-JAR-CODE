/*    */ package com.sun.xml.internal.fastinfoset.stax.events;
/*    */ 
/*    */ import javax.xml.stream.events.EndDocument;
/*    */ 
/*    */ public class EndDocumentEvent extends EventBase
/*    */   implements EndDocument
/*    */ {
/*    */   public EndDocumentEvent()
/*    */   {
/* 36 */     super(8);
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 40 */     return "<? EndDocument ?>";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.EndDocumentEvent
 * JD-Core Version:    0.6.2
 */