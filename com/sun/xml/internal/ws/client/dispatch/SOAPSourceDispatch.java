/*     */ package com.sun.xml.internal.ws.client.dispatch;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*     */ import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ final class SOAPSourceDispatch extends DispatchImpl<Source>
/*     */ {
/*     */   @Deprecated
/*     */   public SOAPSourceDispatch(QName port, Service.Mode mode, WSServiceDelegate owner, Tube pipe, BindingImpl binding, WSEndpointReference epr)
/*     */   {
/*  59 */     super(port, mode, owner, pipe, binding, epr);
/*  60 */     assert (!isXMLHttp(binding));
/*     */   }
/*     */ 
/*     */   public SOAPSourceDispatch(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
/*  64 */     super(portInfo, mode, binding, epr);
/*  65 */     assert (!isXMLHttp(binding));
/*     */   }
/*     */ 
/*     */   Source toReturnValue(Packet response)
/*     */   {
/*  70 */     Message msg = response.getMessage();
/*     */ 
/*  72 */     switch (1.$SwitchMap$javax$xml$ws$Service$Mode[this.mode.ordinal()]) {
/*     */     case 1:
/*  74 */       return msg.readPayloadAsSource();
/*     */     case 2:
/*  76 */       return msg.readEnvelopeAsSource();
/*     */     }
/*  78 */     throw new WebServiceException("Unrecognized dispatch mode");
/*     */   }
/*     */ 
/*     */   Packet createPacket(Source msg)
/*     */   {
/*     */     Message message;
/*     */     Message message;
/*  87 */     if (msg == null)
/*  88 */       message = Messages.createEmpty(this.soapVersion);
/*     */     else {
/*  90 */       switch (1.$SwitchMap$javax$xml$ws$Service$Mode[this.mode.ordinal()]) {
/*     */       case 1:
/*  92 */         message = new PayloadSourceMessage(null, msg, setOutboundAttachments(), this.soapVersion);
/*  93 */         break;
/*     */       case 2:
/*  95 */         message = Messages.create(msg, this.soapVersion);
/*  96 */         break;
/*     */       default:
/*  98 */         throw new WebServiceException("Unrecognized message mode");
/*     */       }
/*     */     }
/*     */ 
/* 102 */     return new Packet(message);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.dispatch.SOAPSourceDispatch
 * JD-Core Version:    0.6.2
 */