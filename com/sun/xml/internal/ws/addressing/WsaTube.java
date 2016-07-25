/*     */ package com.sun.xml.internal.ws.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
/*     */ import com.sun.xml.internal.ws.message.FaultDetailHeader;
/*     */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.AddressingFeature;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ 
/*     */ abstract class WsaTube extends AbstractFilterTubeImpl
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   protected final WSDLPort wsdlPort;
/*     */   protected final WSBinding binding;
/*     */   final WsaTubeHelper helper;
/*     */ 
/*     */   @NotNull
/*     */   protected final AddressingVersion addressingVersion;
/*     */   protected final SOAPVersion soapVersion;
/*     */   private final boolean addressingRequired;
/* 385 */   private static final Logger LOGGER = Logger.getLogger(WsaTube.class.getName());
/*     */ 
/*     */   public WsaTube(WSDLPort wsdlPort, WSBinding binding, Tube next)
/*     */   {
/*  83 */     super(next);
/*  84 */     this.wsdlPort = wsdlPort;
/*  85 */     this.binding = binding;
/*  86 */     this.addressingVersion = binding.getAddressingVersion();
/*  87 */     this.soapVersion = binding.getSOAPVersion();
/*  88 */     this.helper = getTubeHelper();
/*  89 */     this.addressingRequired = AddressingVersion.isRequired(binding);
/*     */   }
/*     */ 
/*     */   public WsaTube(WsaTube that, TubeCloner cloner) {
/*  93 */     super(that, cloner);
/*  94 */     this.wsdlPort = that.wsdlPort;
/*  95 */     this.binding = that.binding;
/*  96 */     this.helper = that.helper;
/*  97 */     this.addressingVersion = that.addressingVersion;
/*  98 */     this.soapVersion = that.soapVersion;
/*  99 */     this.addressingRequired = that.addressingRequired;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public NextAction processException(Throwable t) {
/* 104 */     return super.processException(t);
/*     */   }
/*     */ 
/*     */   protected WsaTubeHelper getTubeHelper() {
/* 108 */     if (this.binding.isFeatureEnabled(AddressingFeature.class))
/* 109 */       return new WsaTubeHelperImpl(this.wsdlPort, null, this.binding);
/* 110 */     if (this.binding.isFeatureEnabled(MemberSubmissionAddressingFeature.class))
/*     */     {
/* 112 */       return new com.sun.xml.internal.ws.addressing.v200408.WsaTubeHelperImpl(this.wsdlPort, null, this.binding);
/*     */     }
/*     */ 
/* 115 */     throw new WebServiceException(AddressingMessages.ADDRESSING_NOT_ENABLED(getClass().getSimpleName()));
/*     */   }
/*     */ 
/*     */   protected Packet validateInboundHeaders(Packet packet)
/*     */   {
/*     */     SOAPFault soapFault;
/*     */     FaultDetailHeader s11FaultDetailHeader;
/*     */     try
/*     */     {
/* 129 */       checkMessageAddressingProperties(packet);
/* 130 */       return packet;
/*     */     } catch (InvalidAddressingHeaderException e) {
/* 132 */       LOGGER.log(Level.WARNING, this.addressingVersion.getInvalidMapText() + ", Problem header:" + e.getProblemHeader() + ", Reason: " + e.getSubsubcode(), e);
/*     */ 
/* 134 */       soapFault = this.helper.createInvalidAddressingHeaderFault(e, this.addressingVersion);
/* 135 */       s11FaultDetailHeader = new FaultDetailHeader(this.addressingVersion, this.addressingVersion.problemHeaderQNameTag.getLocalPart(), e.getProblemHeader());
/*     */     } catch (MissingAddressingHeaderException e) {
/* 137 */       LOGGER.log(Level.WARNING, this.addressingVersion.getMapRequiredText() + ", Problem header:" + e.getMissingHeaderQName(), e);
/* 138 */       soapFault = this.helper.newMapRequiredFault(e);
/* 139 */       s11FaultDetailHeader = new FaultDetailHeader(this.addressingVersion, this.addressingVersion.problemHeaderQNameTag.getLocalPart(), e.getMissingHeaderQName());
/*     */     }
/*     */ 
/* 142 */     if (soapFault != null)
/*     */     {
/* 144 */       if ((this.wsdlPort != null) && (packet.getMessage().isOneWay(this.wsdlPort))) {
/* 145 */         return packet.createServerResponse(null, this.wsdlPort, null, this.binding);
/*     */       }
/*     */ 
/* 148 */       Message m = Messages.create(soapFault);
/* 149 */       if (this.soapVersion == SOAPVersion.SOAP_11) {
/* 150 */         m.getHeaders().add(s11FaultDetailHeader);
/*     */       }
/*     */ 
/* 153 */       return packet.createServerResponse(m, this.wsdlPort, null, this.binding);
/*     */     }
/*     */ 
/* 156 */     return packet;
/*     */   }
/*     */ 
/*     */   protected void checkMessageAddressingProperties(Packet packet)
/*     */   {
/* 172 */     checkCardinality(packet);
/*     */   }
/*     */ 
/*     */   final boolean isAddressingEngagedOrRequired(Packet packet, WSBinding binding) {
/* 176 */     if (AddressingVersion.isRequired(binding)) {
/* 177 */       return true;
/*     */     }
/* 179 */     if (packet == null) {
/* 180 */       return false;
/*     */     }
/* 182 */     if (packet.getMessage() == null) {
/* 183 */       return false;
/*     */     }
/* 185 */     if (packet.getMessage().getHeaders() != null) {
/* 186 */       return false;
/*     */     }
/* 188 */     String action = packet.getMessage().getHeaders().getAction(this.addressingVersion, this.soapVersion);
/* 189 */     if (action == null) {
/* 190 */       return true;
/*     */     }
/* 192 */     return true;
/*     */   }
/*     */ 
/*     */   protected void checkCardinality(Packet packet)
/*     */   {
/* 210 */     Message message = packet.getMessage();
/* 211 */     if (message == null) {
/* 212 */       if (this.addressingRequired) {
/* 213 */         throw new WebServiceException(AddressingMessages.NULL_MESSAGE());
/*     */       }
/* 215 */       return;
/*     */     }
/*     */ 
/* 218 */     Iterator hIter = message.getHeaders().getHeaders(this.addressingVersion.nsUri, true);
/*     */ 
/* 220 */     if (!hIter.hasNext())
/*     */     {
/* 222 */       if (this.addressingRequired)
/*     */       {
/* 224 */         throw new MissingAddressingHeaderException(this.addressingVersion.actionTag, packet);
/*     */       }
/*     */ 
/* 227 */       return;
/*     */     }
/*     */ 
/* 230 */     boolean foundFrom = false;
/* 231 */     boolean foundTo = false;
/* 232 */     boolean foundReplyTo = false;
/* 233 */     boolean foundFaultTo = false;
/* 234 */     boolean foundAction = false;
/* 235 */     boolean foundMessageId = false;
/* 236 */     boolean foundRelatesTo = false;
/* 237 */     QName duplicateHeader = null;
/*     */ 
/* 239 */     while (hIter.hasNext()) {
/* 240 */       Header h = (Header)hIter.next();
/*     */ 
/* 243 */       if (isInCurrentRole(h, this.binding))
/*     */       {
/* 247 */         String local = h.getLocalPart();
/* 248 */         if (local.equals(this.addressingVersion.fromTag.getLocalPart())) {
/* 249 */           if (foundFrom) {
/* 250 */             duplicateHeader = this.addressingVersion.fromTag;
/* 251 */             break;
/*     */           }
/* 253 */           foundFrom = true;
/* 254 */         } else if (local.equals(this.addressingVersion.toTag.getLocalPart())) {
/* 255 */           if (foundTo) {
/* 256 */             duplicateHeader = this.addressingVersion.toTag;
/* 257 */             break;
/*     */           }
/* 259 */           foundTo = true;
/* 260 */         } else if (local.equals(this.addressingVersion.replyToTag.getLocalPart())) {
/* 261 */           if (foundReplyTo) {
/* 262 */             duplicateHeader = this.addressingVersion.replyToTag;
/* 263 */             break;
/*     */           }
/* 265 */           foundReplyTo = true;
/*     */           try {
/* 267 */             h.readAsEPR(this.addressingVersion);
/*     */           } catch (XMLStreamException e) {
/* 269 */             throw new WebServiceException(AddressingMessages.REPLY_TO_CANNOT_PARSE(), e);
/*     */           }
/* 271 */         } else if (local.equals(this.addressingVersion.faultToTag.getLocalPart())) {
/* 272 */           if (foundFaultTo) {
/* 273 */             duplicateHeader = this.addressingVersion.faultToTag;
/* 274 */             break;
/*     */           }
/* 276 */           foundFaultTo = true;
/*     */           try {
/* 278 */             h.readAsEPR(this.addressingVersion);
/*     */           } catch (XMLStreamException e) {
/* 280 */             throw new WebServiceException(AddressingMessages.FAULT_TO_CANNOT_PARSE(), e);
/*     */           }
/* 282 */         } else if (local.equals(this.addressingVersion.actionTag.getLocalPart())) {
/* 283 */           if (foundAction) {
/* 284 */             duplicateHeader = this.addressingVersion.actionTag;
/* 285 */             break;
/*     */           }
/* 287 */           foundAction = true;
/* 288 */         } else if (local.equals(this.addressingVersion.messageIDTag.getLocalPart())) {
/* 289 */           if (foundMessageId) {
/* 290 */             duplicateHeader = this.addressingVersion.messageIDTag;
/* 291 */             break;
/*     */           }
/* 293 */           foundMessageId = true;
/* 294 */         } else if (local.equals(this.addressingVersion.relatesToTag.getLocalPart())) {
/* 295 */           foundRelatesTo = true;
/* 296 */         } else if (!local.equals(this.addressingVersion.faultDetailTag.getLocalPart()))
/*     */         {
/* 300 */           System.err.println(AddressingMessages.UNKNOWN_WSA_HEADER());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 305 */     if (duplicateHeader != null) {
/* 306 */       throw new InvalidAddressingHeaderException(duplicateHeader, this.addressingVersion.invalidCardinalityTag);
/*     */     }
/*     */ 
/* 310 */     boolean engaged = foundAction;
/*     */ 
/* 317 */     if ((engaged) || (this.addressingRequired))
/*     */     {
/* 326 */       checkMandatoryHeaders(packet, foundAction, foundTo, foundReplyTo, foundFaultTo, foundMessageId, foundRelatesTo);
/*     */     }
/*     */   }
/*     */ 
/*     */   final boolean isInCurrentRole(Header header, WSBinding binding)
/*     */   {
/* 335 */     if (binding == null)
/* 336 */       return true;
/* 337 */     return ((SOAPBinding)binding).getRoles().contains(header.getRole(this.soapVersion));
/*     */   }
/*     */ 
/*     */   protected final WSDLBoundOperation getWSDLBoundOperation(Packet packet)
/*     */   {
/* 343 */     if (this.wsdlPort == null)
/* 344 */       return null;
/* 345 */     QName opName = packet.getWSDLOperation();
/* 346 */     if (opName != null)
/* 347 */       return this.wsdlPort.getBinding().get(opName);
/* 348 */     return null;
/*     */   }
/*     */ 
/*     */   protected void validateSOAPAction(Packet packet) {
/* 352 */     String gotA = packet.getMessage().getHeaders().getAction(this.addressingVersion, this.soapVersion);
/* 353 */     if (gotA == null)
/* 354 */       throw new WebServiceException(AddressingMessages.VALIDATION_SERVER_NULL_ACTION());
/* 355 */     if ((packet.soapAction != null) && (!packet.soapAction.equals("\"\"")) && (!packet.soapAction.equals("\"" + gotA + "\"")))
/* 356 */       throw new InvalidAddressingHeaderException(this.addressingVersion.actionTag, this.addressingVersion.actionMismatchTag);
/*     */   }
/*     */ 
/*     */   protected abstract void validateAction(Packet paramPacket);
/*     */ 
/*     */   protected void checkMandatoryHeaders(Packet packet, boolean foundAction, boolean foundTo, boolean foundReplyTo, boolean foundFaultTo, boolean foundMessageId, boolean foundRelatesTo)
/*     */   {
/* 381 */     if (!foundAction)
/* 382 */       throw new MissingAddressingHeaderException(this.addressingVersion.actionTag, packet);
/* 383 */     validateSOAPAction(packet);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.WsaTube
 * JD-Core Version:    0.6.2
 */