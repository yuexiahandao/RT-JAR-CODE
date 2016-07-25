/*     */ package com.sun.xml.internal.ws.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import javax.xml.ws.soap.AddressingFeature;
/*     */ import javax.xml.ws.soap.AddressingFeature.Responses;
/*     */ 
/*     */ public class W3CWsaServerTube extends WsaServerTube
/*     */ {
/*     */   private final AddressingFeature af;
/*     */ 
/*     */   public W3CWsaServerTube(WSEndpoint endpoint, @NotNull WSDLPort wsdlPort, WSBinding binding, Tube next)
/*     */   {
/*  54 */     super(endpoint, wsdlPort, binding, next);
/*  55 */     this.af = ((AddressingFeature)binding.getFeature(AddressingFeature.class));
/*     */   }
/*     */ 
/*     */   public W3CWsaServerTube(W3CWsaServerTube that, TubeCloner cloner) {
/*  59 */     super(that, cloner);
/*  60 */     this.af = that.af;
/*     */   }
/*     */ 
/*     */   public W3CWsaServerTube copy(TubeCloner cloner)
/*     */   {
/*  65 */     return new W3CWsaServerTube(this, cloner);
/*     */   }
/*     */ 
/*     */   protected void checkMandatoryHeaders(Packet packet, boolean foundAction, boolean foundTo, boolean foundReplyTo, boolean foundFaultTo, boolean foundMessageId, boolean foundRelatesTo)
/*     */   {
/*  72 */     super.checkMandatoryHeaders(packet, foundAction, foundTo, foundReplyTo, foundFaultTo, foundMessageId, foundRelatesTo);
/*     */ 
/*  76 */     WSDLBoundOperation wbo = getWSDLBoundOperation(packet);
/*     */ 
/*  78 */     if (wbo != null)
/*     */     {
/*  80 */       if ((!wbo.getOperation().isOneWay()) && (!foundMessageId))
/*  81 */         throw new MissingAddressingHeaderException(this.addressingVersion.messageIDTag, packet);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isAnonymousRequired(@Nullable WSDLBoundOperation wbo)
/*     */   {
/*  89 */     return getResponseRequirement(wbo) == WSDLBoundOperation.ANONYMOUS.required;
/*     */   }
/*     */ 
/*     */   private WSDLBoundOperation.ANONYMOUS getResponseRequirement(@Nullable WSDLBoundOperation wbo) {
/*     */     try {
/*  94 */       if (this.af.getResponses() == AddressingFeature.Responses.ANONYMOUS)
/*  95 */         return WSDLBoundOperation.ANONYMOUS.required;
/*  96 */       if (this.af.getResponses() == AddressingFeature.Responses.NON_ANONYMOUS) {
/*  97 */         return WSDLBoundOperation.ANONYMOUS.prohibited;
/*     */       }
/*     */     }
/*     */     catch (NoSuchMethodError e)
/*     */     {
/*     */     }
/* 103 */     return wbo != null ? wbo.getAnonymous() : WSDLBoundOperation.ANONYMOUS.optional;
/*     */   }
/*     */ 
/*     */   protected void checkAnonymousSemantics(WSDLBoundOperation wbo, WSEndpointReference replyTo, WSEndpointReference faultTo)
/*     */   {
/* 108 */     String replyToValue = null;
/* 109 */     String faultToValue = null;
/*     */ 
/* 111 */     if (replyTo != null) {
/* 112 */       replyToValue = replyTo.getAddress();
/*     */     }
/* 114 */     if (faultTo != null)
/* 115 */       faultToValue = faultTo.getAddress();
/* 116 */     WSDLBoundOperation.ANONYMOUS responseRequirement = getResponseRequirement(wbo);
/*     */ 
/* 118 */     switch (1.$SwitchMap$com$sun$xml$internal$ws$api$model$wsdl$WSDLBoundOperation$ANONYMOUS[responseRequirement.ordinal()]) {
/*     */     case 1:
/* 120 */       if ((replyToValue != null) && (replyToValue.equals(this.addressingVersion.anonymousUri))) {
/* 121 */         throw new InvalidAddressingHeaderException(this.addressingVersion.replyToTag, W3CAddressingConstants.ONLY_NON_ANONYMOUS_ADDRESS_SUPPORTED);
/*     */       }
/* 123 */       if ((faultToValue != null) && (faultToValue.equals(this.addressingVersion.anonymousUri)))
/* 124 */         throw new InvalidAddressingHeaderException(this.addressingVersion.faultToTag, W3CAddressingConstants.ONLY_NON_ANONYMOUS_ADDRESS_SUPPORTED);
/*     */       break;
/*     */     case 2:
/* 127 */       if ((replyToValue != null) && (!replyToValue.equals(this.addressingVersion.anonymousUri))) {
/* 128 */         throw new InvalidAddressingHeaderException(this.addressingVersion.replyToTag, W3CAddressingConstants.ONLY_ANONYMOUS_ADDRESS_SUPPORTED);
/*     */       }
/* 130 */       if ((faultToValue != null) && (!faultToValue.equals(this.addressingVersion.anonymousUri)))
/* 131 */         throw new InvalidAddressingHeaderException(this.addressingVersion.faultToTag, W3CAddressingConstants.ONLY_ANONYMOUS_ADDRESS_SUPPORTED);
/*     */       break;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.W3CWsaServerTube
 * JD-Core Version:    0.6.2
 */