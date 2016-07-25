/*    */ package com.sun.xml.internal.ws.server;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*    */ import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
/*    */ import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
/*    */ import java.security.Principal;
/*    */ import javax.xml.ws.EndpointReference;
/*    */ import javax.xml.ws.handler.MessageContext;
/*    */ import javax.xml.ws.wsaddressing.W3CEndpointReference;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public abstract class AbstractWebServiceContext
/*    */   implements WSWebServiceContext
/*    */ {
/*    */   private final WSEndpoint endpoint;
/*    */ 
/*    */   public AbstractWebServiceContext(@NotNull WSEndpoint endpoint)
/*    */   {
/* 53 */     this.endpoint = endpoint;
/*    */   }
/*    */ 
/*    */   public MessageContext getMessageContext() {
/* 57 */     Packet packet = getRequestPacket();
/* 58 */     if (packet == null) {
/* 59 */       throw new IllegalStateException("getMessageContext() can only be called while servicing a request");
/*    */     }
/* 61 */     return new EndpointMessageContextImpl(packet);
/*    */   }
/*    */ 
/*    */   public Principal getUserPrincipal() {
/* 65 */     Packet packet = getRequestPacket();
/* 66 */     if (packet == null) {
/* 67 */       throw new IllegalStateException("getUserPrincipal() can only be called while servicing a request");
/*    */     }
/* 69 */     return packet.webServiceContextDelegate.getUserPrincipal(packet);
/*    */   }
/*    */ 
/*    */   public boolean isUserInRole(String role) {
/* 73 */     Packet packet = getRequestPacket();
/* 74 */     if (packet == null) {
/* 75 */       throw new IllegalStateException("isUserInRole() can only be called while servicing a request");
/*    */     }
/* 77 */     return packet.webServiceContextDelegate.isUserInRole(packet, role);
/*    */   }
/*    */ 
/*    */   public EndpointReference getEndpointReference(Element[] referenceParameters) {
/* 81 */     return getEndpointReference(W3CEndpointReference.class, referenceParameters);
/*    */   }
/*    */ 
/*    */   public <T extends EndpointReference> T getEndpointReference(Class<T> clazz, Element[] referenceParameters) {
/* 85 */     Packet packet = getRequestPacket();
/* 86 */     if (packet == null) {
/* 87 */       throw new IllegalStateException("getEndpointReference() can only be called while servicing a request");
/*    */     }
/* 89 */     String address = packet.webServiceContextDelegate.getEPRAddress(packet, this.endpoint);
/* 90 */     String wsdlAddress = null;
/* 91 */     if (this.endpoint.getServiceDefinition() != null) {
/* 92 */       wsdlAddress = packet.webServiceContextDelegate.getWSDLAddress(packet, this.endpoint);
/*    */     }
/* 94 */     return (EndpointReference)clazz.cast(((WSEndpointImpl)this.endpoint).getEndpointReference(clazz, address, wsdlAddress, referenceParameters));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.AbstractWebServiceContext
 * JD-Core Version:    0.6.2
 */