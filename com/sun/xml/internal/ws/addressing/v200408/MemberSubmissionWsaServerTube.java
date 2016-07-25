/*    */ package com.sun.xml.internal.ws.addressing.v200408;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.addressing.WsaServerTube;
/*    */ import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*    */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*    */ import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation;
/*    */ import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
/*    */ 
/*    */ public class MemberSubmissionWsaServerTube extends WsaServerTube
/*    */ {
/*    */   private final MemberSubmissionAddressing.Validation validation;
/*    */ 
/*    */   public MemberSubmissionWsaServerTube(WSEndpoint endpoint, @NotNull WSDLPort wsdlPort, WSBinding binding, Tube next)
/*    */   {
/* 47 */     super(endpoint, wsdlPort, binding, next);
/* 48 */     this.validation = ((MemberSubmissionAddressingFeature)binding.getFeature(MemberSubmissionAddressingFeature.class)).getValidation();
/*    */   }
/*    */ 
/*    */   public MemberSubmissionWsaServerTube(MemberSubmissionWsaServerTube that, TubeCloner cloner) {
/* 52 */     super(that, cloner);
/* 53 */     this.validation = that.validation;
/*    */   }
/*    */ 
/*    */   public MemberSubmissionWsaServerTube copy(TubeCloner cloner)
/*    */   {
/* 58 */     return new MemberSubmissionWsaServerTube(this, cloner);
/*    */   }
/*    */ 
/*    */   protected void checkMandatoryHeaders(Packet packet, boolean foundAction, boolean foundTo, boolean foundReplyTo, boolean foundFaultTo, boolean foundMessageId, boolean foundRelatesTo)
/*    */   {
/* 65 */     super.checkMandatoryHeaders(packet, foundAction, foundTo, foundReplyTo, foundFaultTo, foundMessageId, foundRelatesTo);
/*    */ 
/* 69 */     if (!foundTo) {
/* 70 */       throw new MissingAddressingHeaderException(this.addressingVersion.toTag, packet);
/*    */     }
/*    */ 
/* 73 */     if (this.wsdlPort != null) {
/* 74 */       WSDLBoundOperation wbo = getWSDLBoundOperation(packet);
/*    */ 
/* 78 */       if ((wbo != null) && (!wbo.getOperation().isOneWay()) && (!foundReplyTo)) {
/* 79 */         throw new MissingAddressingHeaderException(this.addressingVersion.replyToTag, packet);
/*    */       }
/*    */     }
/* 82 */     if (!this.validation.equals(MemberSubmissionAddressing.Validation.LAX))
/*    */     {
/* 84 */       if (((foundReplyTo) || (foundFaultTo)) && (!foundMessageId))
/* 85 */         throw new MissingAddressingHeaderException(this.addressingVersion.messageIDTag, packet);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionWsaServerTube
 * JD-Core Version:    0.6.2
 */