/*     */ package com.sun.xml.internal.ws.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;
/*     */ import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.WSService;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.pipe.TransportTubeFactory;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.server.TransportBackChannel;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.message.FaultDetailHeader;
/*     */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*     */ import java.net.URI;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public class WsaServerTube extends WsaTube
/*     */ {
/*     */   private WSEndpoint endpoint;
/*     */   private WSEndpointReference replyTo;
/*     */   private WSEndpointReference faultTo;
/*  72 */   private boolean isAnonymousRequired = false;
/*     */   private WSDLBoundOperation wbo;
/*     */ 
/*     */   /** @deprecated */
/*     */   public static final String REQUEST_MESSAGE_ID = "com.sun.xml.internal.ws.addressing.request.messageID";
/* 306 */   private static final Logger LOGGER = Logger.getLogger(WsaServerTube.class.getName());
/*     */ 
/*     */   public WsaServerTube(WSEndpoint endpoint, @NotNull WSDLPort wsdlPort, WSBinding binding, Tube next)
/*     */   {
/*  79 */     super(wsdlPort, binding, next);
/*  80 */     this.endpoint = endpoint;
/*     */   }
/*     */ 
/*     */   public WsaServerTube(WsaServerTube that, TubeCloner cloner)
/*     */   {
/*  85 */     super(that, cloner);
/*  86 */     this.endpoint = that.endpoint;
/*     */   }
/*     */ 
/*     */   public WsaServerTube copy(TubeCloner cloner) {
/*  90 */     return new WsaServerTube(this, cloner);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public NextAction processRequest(Packet request) {
/*  95 */     Message msg = request.getMessage();
/*  96 */     if (msg == null) return doInvoke(this.next, request);
/*     */ 
/*  99 */     request.addSatellite(new WsaPropertyBag(this.addressingVersion, this.soapVersion, request));
/*     */ 
/* 105 */     HeaderList hl = request.getMessage().getHeaders();
/*     */     try {
/* 107 */       this.replyTo = hl.getReplyTo(this.addressingVersion, this.soapVersion);
/* 108 */       this.faultTo = hl.getFaultTo(this.addressingVersion, this.soapVersion);
/*     */     } catch (InvalidAddressingHeaderException e) {
/* 110 */       LOGGER.log(Level.WARNING, this.addressingVersion.getInvalidMapText() + ", Problem header:" + e.getProblemHeader() + ", Reason: " + e.getSubsubcode(), e);
/*     */ 
/* 112 */       SOAPFault soapFault = this.helper.createInvalidAddressingHeaderFault(e, this.addressingVersion);
/*     */ 
/* 114 */       if ((this.wsdlPort != null) && (request.getMessage().isOneWay(this.wsdlPort))) {
/* 115 */         Packet response = request.createServerResponse(null, this.wsdlPort, null, this.binding);
/* 116 */         return doReturnWith(response);
/*     */       }
/*     */ 
/* 119 */       Message m = Messages.create(soapFault);
/* 120 */       if (this.soapVersion == SOAPVersion.SOAP_11) {
/* 121 */         FaultDetailHeader s11FaultDetailHeader = new FaultDetailHeader(this.addressingVersion, this.addressingVersion.problemHeaderQNameTag.getLocalPart(), e.getProblemHeader());
/* 122 */         m.getHeaders().add(s11FaultDetailHeader);
/*     */       }
/*     */ 
/* 125 */       Packet response = request.createServerResponse(m, this.wsdlPort, null, this.binding);
/* 126 */       return doReturnWith(response);
/*     */     }
/*     */ 
/* 130 */     if (this.replyTo == null) this.replyTo = this.addressingVersion.anonymousEpr;
/* 131 */     if (this.faultTo == null) this.faultTo = this.replyTo;
/*     */ 
/* 133 */     this.wbo = getWSDLBoundOperation(request);
/* 134 */     this.isAnonymousRequired = isAnonymousRequired(this.wbo);
/*     */ 
/* 136 */     Packet p = validateInboundHeaders(request);
/*     */ 
/* 139 */     if (p.getMessage() == null)
/*     */     {
/* 141 */       return doReturnWith(p);
/*     */     }
/*     */ 
/* 144 */     if (p.getMessage().isFault())
/*     */     {
/* 147 */       if ((!this.isAnonymousRequired) && (!this.faultTo.isAnonymous()) && (request.transportBackChannel != null))
/*     */       {
/* 149 */         request.transportBackChannel.close();
/* 150 */       }return processResponse(p);
/*     */     }
/*     */ 
/* 154 */     if ((!this.isAnonymousRequired) && (!this.replyTo.isAnonymous()) && (!this.faultTo.isAnonymous()) && (request.transportBackChannel != null))
/*     */     {
/* 157 */       request.transportBackChannel.close();
/* 158 */     }return doInvoke(this.next, p);
/*     */   }
/*     */ 
/*     */   protected boolean isAnonymousRequired(@Nullable WSDLBoundOperation wbo)
/*     */   {
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   protected void checkAnonymousSemantics(WSDLBoundOperation wbo, WSEndpointReference replyTo, WSEndpointReference faultTo)
/*     */   {
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public NextAction processResponse(Packet response) {
/* 172 */     Message msg = response.getMessage();
/* 173 */     if (msg == null) {
/* 174 */       return doReturnWith(response);
/*     */     }
/* 176 */     WSEndpointReference target = msg.isFault() ? this.faultTo : this.replyTo;
/*     */ 
/* 178 */     if ((target.isAnonymous()) || (this.isAnonymousRequired))
/*     */     {
/* 180 */       return doReturnWith(response);
/*     */     }
/* 182 */     if (target.isNone())
/*     */     {
/* 184 */       response.setMessage(null);
/* 185 */       return doReturnWith(response);
/*     */     }
/*     */ 
/* 189 */     processNonAnonymousReply(response, target);
/*     */ 
/* 192 */     response.setMessage(null);
/* 193 */     return doReturnWith(response);
/*     */   }
/*     */ 
/*     */   private void processNonAnonymousReply(Packet packet, WSEndpointReference target)
/*     */   {
/* 211 */     if (packet.transportBackChannel != null) {
/* 212 */       packet.transportBackChannel.close();
/*     */     }
/* 214 */     if ((this.wsdlPort != null) && (packet.getMessage().isOneWay(this.wsdlPort)))
/*     */     {
/* 216 */       LOGGER.fine(AddressingMessages.NON_ANONYMOUS_RESPONSE_ONEWAY());
/*     */       return;
/*     */     }
/*     */     EndpointAddress adrs;
/*     */     try {
/* 222 */       adrs = new EndpointAddress(URI.create(target.getAddress()));
/*     */     } catch (NullPointerException e) {
/* 224 */       throw new WebServiceException(e);
/*     */     } catch (IllegalArgumentException e) {
/* 226 */       throw new WebServiceException(e);
/*     */     }
/*     */ 
/* 231 */     Tube transport = TransportTubeFactory.create(Thread.currentThread().getContextClassLoader(), new ClientTubeAssemblerContext(adrs, this.wsdlPort, (WSService)null, this.binding, this.endpoint.getContainer(), ((BindingImpl)this.binding).createCodec(), null));
/*     */ 
/* 234 */     packet.endpointAddress = adrs;
/* 235 */     String action = packet.getMessage().isFault() ? this.helper.getFaultAction(this.wbo, packet) : this.helper.getOutputAction(this.wbo);
/*     */ 
/* 239 */     packet.soapAction = action;
/* 240 */     packet.expectReply = Boolean.valueOf(false);
/* 241 */     Fiber.current().runSync(transport, packet);
/*     */   }
/*     */ 
/*     */   protected void validateAction(Packet packet)
/*     */   {
/* 248 */     WSDLBoundOperation wsdlBoundOperation = getWSDLBoundOperation(packet);
/*     */ 
/* 250 */     if (wsdlBoundOperation == null) {
/* 251 */       return;
/*     */     }
/* 253 */     String gotA = packet.getMessage().getHeaders().getAction(this.addressingVersion, this.soapVersion);
/*     */ 
/* 255 */     if (gotA == null) {
/* 256 */       throw new WebServiceException(AddressingMessages.VALIDATION_SERVER_NULL_ACTION());
/*     */     }
/* 258 */     String expected = this.helper.getInputAction(packet);
/* 259 */     String soapAction = this.helper.getSOAPAction(packet);
/* 260 */     if ((this.helper.isInputActionDefault(packet)) && (soapAction != null) && (!soapAction.equals(""))) {
/* 261 */       expected = soapAction;
/*     */     }
/* 263 */     if ((expected != null) && (!gotA.equals(expected)))
/* 264 */       throw new ActionNotSupportedException(gotA);
/*     */   }
/*     */ 
/*     */   protected void checkMessageAddressingProperties(Packet packet)
/*     */   {
/* 270 */     super.checkMessageAddressingProperties(packet);
/*     */ 
/* 273 */     WSDLBoundOperation wsdlBoundOperation = getWSDLBoundOperation(packet);
/* 274 */     checkAnonymousSemantics(wsdlBoundOperation, this.replyTo, this.faultTo);
/*     */ 
/* 276 */     checkNonAnonymousAddresses(this.replyTo, this.faultTo);
/*     */   }
/*     */ 
/*     */   private void checkNonAnonymousAddresses(WSEndpointReference replyTo, WSEndpointReference faultTo) {
/* 280 */     if (!replyTo.isAnonymous())
/*     */       try {
/* 282 */         new EndpointAddress(URI.create(replyTo.getAddress()));
/*     */       } catch (Exception e) {
/* 284 */         throw new InvalidAddressingHeaderException(this.addressingVersion.replyToTag, this.addressingVersion.invalidAddressTag);
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.WsaServerTube
 * JD-Core Version:    0.6.2
 */