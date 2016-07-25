/*    */ package com.sun.xml.internal.stream.events;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import javax.xml.stream.events.EndDocument;
/*    */ 
/*    */ public class EndDocumentEvent extends DummyEvent
/*    */   implements EndDocument
/*    */ {
/*    */   public EndDocumentEvent()
/*    */   {
/* 43 */     init();
/*    */   }
/*    */ 
/*    */   protected void init() {
/* 47 */     setEventType(8);
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return "ENDDOCUMENT";
/*    */   }
/*    */ 
/*    */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.EndDocumentEvent
 * JD-Core Version:    0.6.2
 */