/*    */ package com.sun.xml.internal.ws.client.dispatch;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*    */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*    */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*    */ import com.sun.xml.internal.ws.message.saaj.SAAJMessage;
/*    */ import com.sun.xml.internal.ws.resources.DispatchMessages;
/*    */ import com.sun.xml.internal.ws.transport.Headers;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.MimeHeader;
/*    */ import javax.xml.soap.MimeHeaders;
/*    */ import javax.xml.soap.SOAPException;
/*    */ import javax.xml.soap.SOAPMessage;
/*    */ import javax.xml.ws.Service.Mode;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public class SOAPMessageDispatch extends DispatchImpl<SOAPMessage>
/*    */ {
/*    */   @Deprecated
/*    */   public SOAPMessageDispatch(QName port, Service.Mode mode, WSServiceDelegate owner, Tube pipe, BindingImpl binding, WSEndpointReference epr)
/*    */   {
/* 62 */     super(port, mode, owner, pipe, binding, epr);
/*    */   }
/*    */ 
/*    */   public SOAPMessageDispatch(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
/* 66 */     super(portInfo, mode, binding, epr);
/*    */   }
/*    */ 
/*    */   Packet createPacket(SOAPMessage arg) {
/* 70 */     Iterator iter = arg.getMimeHeaders().getAllHeaders();
/* 71 */     Headers ch = new Headers();
/* 72 */     while (iter.hasNext()) {
/* 73 */       MimeHeader mh = (MimeHeader)iter.next();
/* 74 */       ch.add(mh.getName(), mh.getValue());
/*    */     }
/* 76 */     Packet packet = new Packet(new SAAJMessage(arg));
/* 77 */     packet.invocationProperties.put("javax.xml.ws.http.request.headers", ch);
/* 78 */     return packet;
/*    */   }
/*    */ 
/*    */   SOAPMessage toReturnValue(Packet response)
/*    */   {
/*    */     try
/*    */     {
/* 85 */       if ((response == null) || (response.getMessage() == null)) {
/* 86 */         throw new WebServiceException(DispatchMessages.INVALID_RESPONSE());
/*    */       }
/* 88 */       return response.getMessage().readAsSOAPMessage();
/*    */     } catch (SOAPException e) {
/* 90 */       throw new WebServiceException(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.dispatch.SOAPMessageDispatch
 * JD-Core Version:    0.6.2
 */