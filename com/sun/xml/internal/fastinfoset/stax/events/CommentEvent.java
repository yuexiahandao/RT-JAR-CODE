/*    */ package com.sun.xml.internal.fastinfoset.stax.events;
/*    */ 
/*    */ import javax.xml.stream.events.Comment;
/*    */ 
/*    */ public class CommentEvent extends EventBase
/*    */   implements Comment
/*    */ {
/*    */   private String _text;
/*    */ 
/*    */   public CommentEvent()
/*    */   {
/* 39 */     super(5);
/*    */   }
/*    */ 
/*    */   public CommentEvent(String text) {
/* 43 */     this();
/* 44 */     this._text = text;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 52 */     return "<!--" + this._text + "-->";
/*    */   }
/*    */ 
/*    */   public String getText()
/*    */   {
/* 60 */     return this._text;
/*    */   }
/*    */ 
/*    */   public void setText(String text) {
/* 64 */     this._text = text;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.CommentEvent
 * JD-Core Version:    0.6.2
 */