/*    */ package com.sun.xml.internal.ws.api.message.stream;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.message.AttachmentSetImpl;
/*    */ 
/*    */ abstract class StreamBasedMessage
/*    */ {
/*    */   public final Packet properties;
/*    */   public final AttachmentSet attachments;
/*    */ 
/*    */   protected StreamBasedMessage(Packet properties)
/*    */   {
/* 56 */     this.properties = properties;
/* 57 */     this.attachments = new AttachmentSetImpl();
/*    */   }
/*    */ 
/*    */   protected StreamBasedMessage(Packet properties, AttachmentSet attachments)
/*    */   {
/* 70 */     this.properties = properties;
/* 71 */     this.attachments = attachments;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.stream.StreamBasedMessage
 * JD-Core Version:    0.6.2
 */