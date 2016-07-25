/*    */ package com.sun.xml.internal.ws.protocol.soap;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class MessageCreationException extends ExceptionHasMessage
/*    */ {
/*    */   private final SOAPVersion soapVersion;
/*    */ 
/*    */   public MessageCreationException(SOAPVersion soapVersion, Object[] args)
/*    */   {
/* 46 */     super("soap.msg.create.err", args);
/* 47 */     this.soapVersion = soapVersion;
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 51 */     return "com.sun.xml.internal.ws.resources.soap";
/*    */   }
/*    */ 
/*    */   public Message getFaultMessage() {
/* 55 */     QName faultCode = this.soapVersion.faultCodeClient;
/* 56 */     return SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, getLocalizedMessage(), faultCode);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.protocol.soap.MessageCreationException
 * JD-Core Version:    0.6.2
 */