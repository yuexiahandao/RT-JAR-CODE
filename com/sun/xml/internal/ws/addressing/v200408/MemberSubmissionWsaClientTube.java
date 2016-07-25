/*    */ package com.sun.xml.internal.ws.addressing.v200408;
/*    */ 
/*    */ import com.sun.xml.internal.ws.addressing.WsaClientTube;
/*    */ import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*    */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*    */ import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation;
/*    */ import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
/*    */ 
/*    */ public class MemberSubmissionWsaClientTube extends WsaClientTube
/*    */ {
/*    */   private final MemberSubmissionAddressing.Validation validation;
/*    */ 
/*    */   public MemberSubmissionWsaClientTube(WSDLPort wsdlPort, WSBinding binding, Tube next)
/*    */   {
/* 45 */     super(wsdlPort, binding, next);
/* 46 */     this.validation = ((MemberSubmissionAddressingFeature)binding.getFeature(MemberSubmissionAddressingFeature.class)).getValidation();
/*    */   }
/*    */ 
/*    */   public MemberSubmissionWsaClientTube(MemberSubmissionWsaClientTube that, TubeCloner cloner)
/*    */   {
/* 51 */     super(that, cloner);
/* 52 */     this.validation = that.validation;
/*    */   }
/*    */ 
/*    */   public MemberSubmissionWsaClientTube copy(TubeCloner cloner) {
/* 56 */     return new MemberSubmissionWsaClientTube(this, cloner);
/*    */   }
/*    */ 
/*    */   protected void checkMandatoryHeaders(Packet packet, boolean foundAction, boolean foundTo, boolean foundReplyTo, boolean foundFaultTo, boolean foundMessageID, boolean foundRelatesTo)
/*    */   {
/* 62 */     super.checkMandatoryHeaders(packet, foundAction, foundTo, foundReplyTo, foundFaultTo, foundMessageID, foundRelatesTo);
/*    */ 
/* 65 */     if (!foundTo) {
/* 66 */       throw new MissingAddressingHeaderException(this.addressingVersion.toTag, packet);
/*    */     }
/*    */ 
/* 69 */     if (!this.validation.equals(MemberSubmissionAddressing.Validation.LAX))
/*    */     {
/* 74 */       if ((this.expectReply) && (packet.getMessage() != null) && (!foundRelatesTo)) {
/* 75 */         String action = packet.getMessage().getHeaders().getAction(this.addressingVersion, this.soapVersion);
/*    */ 
/* 78 */         if ((!packet.getMessage().isFault()) || (!action.equals(this.addressingVersion.getDefaultFaultAction())))
/* 79 */           throw new MissingAddressingHeaderException(this.addressingVersion.relatesToTag, packet);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionWsaClientTube
 * JD-Core Version:    0.6.2
 */