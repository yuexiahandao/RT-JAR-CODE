/*    */ package com.sun.xml.internal.ws.addressing;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*    */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public class WsaClientTube extends WsaTube
/*    */ {
/* 51 */   protected boolean expectReply = true;
/*    */ 
/* 53 */   public WsaClientTube(WSDLPort wsdlPort, WSBinding binding, Tube next) { super(wsdlPort, binding, next); }
/*    */ 
/*    */   public WsaClientTube(WsaClientTube that, TubeCloner cloner)
/*    */   {
/* 57 */     super(that, cloner);
/*    */   }
/*    */ 
/*    */   public WsaClientTube copy(TubeCloner cloner) {
/* 61 */     return new WsaClientTube(this, cloner);
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public NextAction processRequest(Packet request) {
/* 66 */     this.expectReply = request.expectReply.booleanValue();
/* 67 */     return doInvoke(this.next, request);
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public NextAction processResponse(Packet response)
/*    */   {
/* 73 */     if (response.getMessage() != null) {
/* 74 */       response = validateInboundHeaders(response);
/* 75 */       response.addSatellite(new WsaPropertyBag(this.addressingVersion, this.soapVersion, response));
/*    */     }
/*    */ 
/* 78 */     return doReturnWith(response);
/*    */   }
/*    */ 
/*    */   protected void validateAction(Packet packet)
/*    */   {
/* 86 */     WSDLBoundOperation wbo = getWSDLBoundOperation(packet);
/*    */ 
/* 88 */     if (wbo == null) return;
/*    */ 
/* 90 */     String gotA = packet.getMessage().getHeaders().getAction(this.addressingVersion, this.soapVersion);
/* 91 */     if (gotA == null) {
/* 92 */       throw new WebServiceException(AddressingMessages.VALIDATION_CLIENT_NULL_ACTION());
/*    */     }
/* 94 */     String expected = this.helper.getOutputAction(packet);
/*    */ 
/* 96 */     if ((expected != null) && (!gotA.equals(expected)))
/* 97 */       throw new ActionNotSupportedException(gotA);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.WsaClientTube
 * JD-Core Version:    0.6.2
 */