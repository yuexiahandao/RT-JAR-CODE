/*    */ package com.sun.xml.internal.ws.server.provider;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*    */ 
/*    */ final class MessageProviderArgumentBuilder extends ProviderArgumentsBuilder<Message>
/*    */ {
/*    */   private final SOAPVersion soapVersion;
/*    */ 
/*    */   public MessageProviderArgumentBuilder(SOAPVersion soapVersion)
/*    */   {
/* 40 */     this.soapVersion = soapVersion;
/*    */   }
/*    */ 
/*    */   protected Message getParameter(Packet packet)
/*    */   {
/* 45 */     return packet.getMessage();
/*    */   }
/*    */ 
/*    */   protected Message getResponseMessage(Message returnValue)
/*    */   {
/* 50 */     return returnValue;
/*    */   }
/*    */ 
/*    */   protected Message getResponseMessage(Exception e)
/*    */   {
/* 55 */     return SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, null, e);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.provider.MessageProviderArgumentBuilder
 * JD-Core Version:    0.6.2
 */