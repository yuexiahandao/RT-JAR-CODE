/*    */ package com.sun.xml.internal.ws.handler;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.ws.LogicalMessage;
/*    */ import javax.xml.ws.handler.LogicalMessageContext;
/*    */ 
/*    */ class LogicalMessageContextImpl extends MessageUpdatableContext
/*    */   implements LogicalMessageContext
/*    */ {
/*    */   private LogicalMessageImpl lm;
/*    */   private WSBinding binding;
/*    */   private JAXBContext defaultJaxbContext;
/*    */ 
/*    */   public LogicalMessageContextImpl(WSBinding binding, JAXBContext defaultJAXBContext, Packet packet)
/*    */   {
/* 57 */     super(packet);
/* 58 */     this.binding = binding;
/* 59 */     this.defaultJaxbContext = defaultJAXBContext;
/*    */   }
/*    */ 
/*    */   public LogicalMessage getMessage() {
/* 63 */     if (this.lm == null)
/* 64 */       this.lm = new LogicalMessageImpl(this.defaultJaxbContext, this.packet);
/* 65 */     return this.lm;
/*    */   }
/*    */ 
/*    */   void setPacketMessage(Message newMessage) {
/* 69 */     if (newMessage != null) {
/* 70 */       this.packet.setMessage(newMessage);
/* 71 */       this.lm = null;
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void updateMessage()
/*    */   {
/* 78 */     if (this.lm != null)
/*    */     {
/* 81 */       if (this.lm.isPayloadModifed()) {
/* 82 */         Message msg = this.packet.getMessage();
/* 83 */         Message updatedMsg = this.lm.getMessage(msg.getHeaders(), msg.getAttachments(), this.binding);
/* 84 */         this.packet.setMessage(updatedMsg);
/*    */       }
/* 86 */       this.lm = null;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.LogicalMessageContextImpl
 * JD-Core Version:    0.6.2
 */