/*    */ package com.sun.xml.internal.ws.client.dispatch;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*    */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Messages;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*    */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*    */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage;
/*    */ import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
/*    */ import java.io.IOException;
/*    */ import javax.activation.DataSource;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import javax.xml.ws.Service.Mode;
/*    */ 
/*    */ final class RESTSourceDispatch extends DispatchImpl<Source>
/*    */ {
/*    */   @Deprecated
/*    */   public RESTSourceDispatch(QName port, Service.Mode mode, WSServiceDelegate owner, Tube pipe, BindingImpl binding, WSEndpointReference epr)
/*    */   {
/* 57 */     super(port, mode, owner, pipe, binding, epr);
/* 58 */     assert (isXMLHttp(binding));
/*    */   }
/*    */ 
/*    */   public RESTSourceDispatch(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
/* 62 */     super(portInfo, mode, binding, epr);
/* 63 */     assert (isXMLHttp(binding));
/*    */   }
/*    */ 
/*    */   Source toReturnValue(Packet response)
/*    */   {
/* 68 */     Message msg = response.getMessage();
/*    */     try {
/* 70 */       return new StreamSource(XMLMessage.getDataSource(msg, this.binding).getInputStream());
/*    */     } catch (IOException e) {
/* 72 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */ 
/*    */   Packet createPacket(Source msg)
/*    */   {
/*    */     Message message;
/*    */     Message message;
/* 80 */     if (msg == null)
/* 81 */       message = Messages.createEmpty(this.soapVersion);
/*    */     else {
/* 83 */       message = new PayloadSourceMessage(null, msg, setOutboundAttachments(), this.soapVersion);
/*    */     }
/* 85 */     return new Packet(message);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.dispatch.RESTSourceDispatch
 * JD-Core Version:    0.6.2
 */