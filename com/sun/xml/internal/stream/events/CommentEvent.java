/*    */ package com.sun.xml.internal.stream.events;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import javax.xml.stream.events.Comment;
/*    */ 
/*    */ public class CommentEvent extends DummyEvent
/*    */   implements Comment
/*    */ {
/*    */   private String fText;
/*    */ 
/*    */   public CommentEvent()
/*    */   {
/* 42 */     init();
/*    */   }
/*    */ 
/*    */   public CommentEvent(String text) {
/* 46 */     init();
/* 47 */     this.fText = text;
/*    */   }
/*    */ 
/*    */   protected void init() {
/* 51 */     setEventType(5);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 58 */     return "<!--" + getText() + "-->";
/*    */   }
/*    */ 
/*    */   public String getText()
/*    */   {
/* 67 */     return this.fText;
/*    */   }
/*    */ 
/*    */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*    */     throws IOException
/*    */   {
/* 73 */     writer.write("<!--" + getText() + "-->");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.CommentEvent
 * JD-Core Version:    0.6.2
 */