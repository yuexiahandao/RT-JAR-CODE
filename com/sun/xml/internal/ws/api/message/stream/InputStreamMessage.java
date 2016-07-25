/*    */ package com.sun.xml.internal.ws.api.message.stream;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class InputStreamMessage extends StreamBasedMessage
/*    */ {
/*    */   public final String contentType;
/*    */   public final InputStream msg;
/*    */ 
/*    */   public InputStreamMessage(Packet properties, String contentType, InputStream msg)
/*    */   {
/* 61 */     super(properties);
/*    */ 
/* 63 */     this.contentType = contentType;
/* 64 */     this.msg = msg;
/*    */   }
/*    */ 
/*    */   public InputStreamMessage(Packet properties, AttachmentSet attachments, String contentType, InputStream msg)
/*    */   {
/* 85 */     super(properties, attachments);
/*    */ 
/* 87 */     this.contentType = contentType;
/* 88 */     this.msg = msg;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.stream.InputStreamMessage
 * JD-Core Version:    0.6.2
 */