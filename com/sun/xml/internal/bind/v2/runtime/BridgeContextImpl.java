/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.BridgeContext;
/*    */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.ValidationEventHandler;
/*    */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*    */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*    */ 
/*    */ public final class BridgeContextImpl extends BridgeContext
/*    */ {
/*    */   public final UnmarshallerImpl unmarshaller;
/*    */   public final MarshallerImpl marshaller;
/*    */ 
/*    */   BridgeContextImpl(JAXBContextImpl context)
/*    */   {
/* 47 */     this.unmarshaller = context.createUnmarshaller();
/* 48 */     this.marshaller = context.createMarshaller();
/*    */   }
/*    */ 
/*    */   public void setErrorHandler(ValidationEventHandler handler) {
/*    */     try {
/* 53 */       this.unmarshaller.setEventHandler(handler);
/* 54 */       this.marshaller.setEventHandler(handler);
/*    */     }
/*    */     catch (JAXBException e) {
/* 57 */       throw new Error(e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setAttachmentMarshaller(AttachmentMarshaller m) {
/* 62 */     this.marshaller.setAttachmentMarshaller(m);
/*    */   }
/*    */ 
/*    */   public void setAttachmentUnmarshaller(AttachmentUnmarshaller u) {
/* 66 */     this.unmarshaller.setAttachmentUnmarshaller(u);
/*    */   }
/*    */ 
/*    */   public AttachmentMarshaller getAttachmentMarshaller() {
/* 70 */     return this.marshaller.getAttachmentMarshaller();
/*    */   }
/*    */ 
/*    */   public AttachmentUnmarshaller getAttachmentUnmarshaller() {
/* 74 */     return this.unmarshaller.getAttachmentUnmarshaller();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.BridgeContextImpl
 * JD-Core Version:    0.6.2
 */