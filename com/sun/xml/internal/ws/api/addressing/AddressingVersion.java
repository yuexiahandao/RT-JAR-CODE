/*     */ package com.sun.xml.internal.ws.api.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
/*     */ import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
/*     */ import com.sun.xml.internal.ws.message.stream.OutboundStreamHeader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.soap.AddressingFeature;
/*     */ import javax.xml.ws.wsaddressing.W3CEndpointReference;
/*     */ 
/*     */ public enum AddressingVersion
/*     */ {
/*  59 */   W3C("http://www.w3.org/2005/08/addressing", "<EndpointReference xmlns=\"http://www.w3.org/2005/08/addressing\">\n    <Address>http://www.w3.org/2005/08/addressing/anonymous</Address>\n</EndpointReference>", "http://www.w3.org/2006/05/addressing/wsdl", "http://www.w3.org/2006/05/addressing/wsdl", "http://www.w3.org/2005/08/addressing/anonymous", "http://www.w3.org/2005/08/addressing/none", new EPR(W3CEndpointReference.class, "Address", "ServiceName", "EndpointName", "InterfaceName", new QName("http://www.w3.org/2005/08/addressing", "Metadata"), "ReferenceParameters", null)), 
/*     */ 
/* 140 */   MEMBER("http://schemas.xmlsoap.org/ws/2004/08/addressing", "<EndpointReference xmlns=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">\n    <Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</Address>\n</EndpointReference>", "http://schemas.xmlsoap.org/ws/2004/08/addressing", "http://schemas.xmlsoap.org/ws/2004/08/addressing/policy", "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous", "", new EPR(MemberSubmissionEndpointReference.class, "Address", "ServiceName", "PortName", "PortType", MemberSubmissionAddressingConstants.MEX_METADATA, "ReferenceParameters", "ReferenceProperties"));
/*     */ 
/*     */   public final String nsUri;
/*     */   public final String wsdlNsUri;
/*     */   public final EPR eprType;
/*     */   public final String policyNsUri;
/*     */ 
/*     */   @NotNull
/*     */   public final String anonymousUri;
/*     */ 
/*     */   @NotNull
/*     */   public final String noneUri;
/*     */   public final WSEndpointReference anonymousEpr;
/*     */   public final QName toTag;
/*     */   public final QName fromTag;
/*     */   public final QName replyToTag;
/*     */   public final QName faultToTag;
/*     */   public final QName actionTag;
/*     */   public final QName messageIDTag;
/*     */   public final QName relatesToTag;
/*     */   public final QName mapRequiredTag;
/*     */   public final QName actionMismatchTag;
/*     */   public final QName actionNotSupportedTag;
/*     */   public final String actionNotSupportedText;
/*     */   public final QName invalidMapTag;
/*     */   public final QName invalidCardinalityTag;
/*     */   public final QName invalidAddressTag;
/*     */   public final QName problemHeaderQNameTag;
/*     */   public final QName problemActionTag;
/*     */   public final QName faultDetailTag;
/*     */   public final QName fault_missingAddressInEpr;
/*     */   public final QName wsdlActionTag;
/*     */   public final QName wsdlExtensionTag;
/*     */   public final QName wsdlAnonymousTag;
/*     */   public final QName isReferenceParameterTag;
/*     */   private static final String EXTENDED_FAULT_NAMESPACE = "http://jax-ws.dev.java.net/addressing/fault";
/*     */   public static final String UNSET_OUTPUT_ACTION = "http://jax-ws.dev.java.net/addressing/output-action-not-set";
/*     */   public static final String UNSET_INPUT_ACTION = "http://jax-ws.dev.java.net/addressing/input-action-not-set";
/* 385 */   public static final QName fault_duplicateAddressInEpr = new QName("http://jax-ws.dev.java.net/addressing/fault", "DuplicateAddressInEpr");
/*     */ 
/*     */   private AddressingVersion(String nsUri, String anonymousEprString, String wsdlNsUri, String policyNsUri, String anonymousUri, String noneUri, EPR eprType)
/*     */   {
/* 392 */     this.nsUri = nsUri;
/* 393 */     this.wsdlNsUri = wsdlNsUri;
/* 394 */     this.policyNsUri = policyNsUri;
/* 395 */     this.anonymousUri = anonymousUri;
/* 396 */     this.noneUri = noneUri;
/* 397 */     this.toTag = new QName(nsUri, "To");
/* 398 */     this.fromTag = new QName(nsUri, "From");
/* 399 */     this.replyToTag = new QName(nsUri, "ReplyTo");
/* 400 */     this.faultToTag = new QName(nsUri, "FaultTo");
/* 401 */     this.actionTag = new QName(nsUri, "Action");
/* 402 */     this.messageIDTag = new QName(nsUri, "MessageID");
/* 403 */     this.relatesToTag = new QName(nsUri, "RelatesTo");
/*     */ 
/* 405 */     this.mapRequiredTag = new QName(nsUri, getMapRequiredLocalName());
/* 406 */     this.actionMismatchTag = new QName(nsUri, getActionMismatchLocalName());
/* 407 */     this.actionNotSupportedTag = new QName(nsUri, "ActionNotSupported");
/* 408 */     this.actionNotSupportedText = "The \"%s\" cannot be processed at the receiver";
/* 409 */     this.invalidMapTag = new QName(nsUri, getInvalidMapLocalName());
/* 410 */     this.invalidAddressTag = new QName(nsUri, getInvalidAddressLocalName());
/* 411 */     this.invalidCardinalityTag = new QName(nsUri, getInvalidCardinalityLocalName());
/* 412 */     this.faultDetailTag = new QName(nsUri, "FaultDetail");
/*     */ 
/* 414 */     this.problemHeaderQNameTag = new QName(nsUri, "ProblemHeaderQName");
/* 415 */     this.problemActionTag = new QName(nsUri, "ProblemAction");
/*     */ 
/* 417 */     this.fault_missingAddressInEpr = new QName(nsUri, "MissingAddressInEPR", "wsa");
/* 418 */     this.isReferenceParameterTag = new QName(nsUri, getIsReferenceParameterLocalName());
/*     */ 
/* 420 */     this.wsdlActionTag = new QName(wsdlNsUri, "Action");
/* 421 */     this.wsdlExtensionTag = new QName(wsdlNsUri, "UsingAddressing");
/* 422 */     this.wsdlAnonymousTag = new QName(wsdlNsUri, getWsdlAnonymousLocalName());
/*     */     try
/*     */     {
/* 426 */       this.anonymousEpr = new WSEndpointReference(new ByteArrayInputStream(anonymousEprString.getBytes("UTF-8")), this);
/*     */     } catch (XMLStreamException e) {
/* 428 */       throw new Error(e);
/*     */     } catch (UnsupportedEncodingException e) {
/* 430 */       throw new Error(e);
/*     */     }
/* 432 */     this.eprType = eprType;
/*     */   }
/*     */ 
/*     */   abstract String getActionMismatchLocalName();
/*     */ 
/*     */   public static AddressingVersion fromNsUri(String nsUri)
/*     */   {
/* 453 */     if (nsUri.equals(W3C.nsUri)) {
/* 454 */       return W3C;
/*     */     }
/* 456 */     if (nsUri.equals(MEMBER.nsUri)) {
/* 457 */       return MEMBER;
/*     */     }
/* 459 */     return null;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public static AddressingVersion fromBinding(WSBinding binding)
/*     */   {
/* 473 */     if (binding.isFeatureEnabled(AddressingFeature.class)) {
/* 474 */       return W3C;
/*     */     }
/* 476 */     if (binding.isFeatureEnabled(MemberSubmissionAddressingFeature.class)) {
/* 477 */       return MEMBER;
/*     */     }
/* 479 */     return null;
/*     */   }
/*     */ 
/*     */   public static AddressingVersion fromPort(WSDLPort port)
/*     */   {
/* 489 */     if (port == null) {
/* 490 */       return null;
/*     */     }
/* 492 */     WebServiceFeature wsf = port.getFeature(AddressingFeature.class);
/* 493 */     if (wsf == null) {
/* 494 */       wsf = port.getFeature(MemberSubmissionAddressingFeature.class);
/*     */     }
/* 496 */     if (wsf == null) {
/* 497 */       return null;
/*     */     }
/* 499 */     return fromFeature(wsf);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getNsUri()
/*     */   {
/* 510 */     return this.nsUri;
/*     */   }
/*     */ 
/*     */   public abstract boolean isReferenceParameter(String paramString);
/*     */ 
/*     */   /** @deprecated */
/*     */   public abstract WsaTubeHelper getWsaHelper(WSDLPort paramWSDLPort, SEIModel paramSEIModel, WSBinding paramWSBinding);
/*     */ 
/*     */   /** @deprecated */
/*     */   public final String getNoneUri()
/*     */   {
/* 543 */     return this.noneUri;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final String getAnonymousUri()
/*     */   {
/* 553 */     return this.anonymousUri;
/*     */   }
/*     */ 
/*     */   public String getDefaultFaultAction()
/*     */   {
/* 562 */     return this.nsUri + "/fault";
/*     */   }
/*     */ 
/*     */   abstract String getMapRequiredLocalName();
/*     */ 
/*     */   public abstract String getMapRequiredText();
/*     */ 
/*     */   abstract String getInvalidAddressLocalName();
/*     */ 
/*     */   abstract String getInvalidMapLocalName();
/*     */ 
/*     */   public abstract String getInvalidMapText();
/*     */ 
/*     */   abstract String getInvalidCardinalityLocalName();
/*     */ 
/*     */   abstract String getWsdlAnonymousLocalName();
/*     */ 
/*     */   public abstract String getPrefix();
/*     */ 
/*     */   public abstract String getWsdlPrefix();
/*     */ 
/*     */   public abstract Class<? extends WebServiceFeature> getFeatureClass();
/*     */ 
/*     */   abstract Header createReferenceParameterHeader(XMLStreamBuffer paramXMLStreamBuffer, String paramString1, String paramString2);
/*     */ 
/*     */   abstract String getIsReferenceParameterLocalName();
/*     */ 
/*     */   public static AddressingVersion fromFeature(WebServiceFeature af)
/*     */   {
/* 632 */     if (af.getID().equals("http://www.w3.org/2005/08/addressing/module"))
/* 633 */       return W3C;
/* 634 */     if (af.getID().equals("http://java.sun.com/xml/ns/jaxws/2004/08/addressing")) {
/* 635 */       return MEMBER;
/*     */     }
/* 637 */     return null;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static WebServiceFeature getFeature(String nsUri, boolean enabled, boolean required)
/*     */   {
/* 653 */     if (nsUri.equals(W3C.policyNsUri))
/* 654 */       return new AddressingFeature(enabled, required);
/* 655 */     if (nsUri.equals(MEMBER.policyNsUri)) {
/* 656 */       return new MemberSubmissionAddressingFeature(enabled, required);
/*     */     }
/* 658 */     throw new WebServiceException("Unsupported namespace URI: " + nsUri);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static AddressingVersion fromSpecClass(Class<? extends EndpointReference> eprClass)
/*     */   {
/* 666 */     if (eprClass == W3CEndpointReference.class)
/* 667 */       return W3C;
/* 668 */     if (eprClass == MemberSubmissionEndpointReference.class)
/* 669 */       return MEMBER;
/* 670 */     throw new WebServiceException("Unsupported EPR type: " + eprClass);
/*     */   }
/*     */ 
/*     */   public static boolean isRequired(WebServiceFeature wsf)
/*     */   {
/* 683 */     if (wsf.getID().equals("http://www.w3.org/2005/08/addressing/module"))
/* 684 */       return ((AddressingFeature)wsf).isRequired();
/* 685 */     if (wsf.getID().equals("http://java.sun.com/xml/ns/jaxws/2004/08/addressing")) {
/* 686 */       return ((MemberSubmissionAddressingFeature)wsf).isRequired();
/*     */     }
/* 688 */     throw new WebServiceException("WebServiceFeature not an Addressing feature: " + wsf.getID());
/*     */   }
/*     */ 
/*     */   public static boolean isRequired(WSBinding binding)
/*     */   {
/* 699 */     AddressingFeature af = (AddressingFeature)binding.getFeature(AddressingFeature.class);
/* 700 */     if (af != null)
/* 701 */       return af.isRequired();
/* 702 */     MemberSubmissionAddressingFeature msaf = (MemberSubmissionAddressingFeature)binding.getFeature(MemberSubmissionAddressingFeature.class);
/* 703 */     if (msaf != null) {
/* 704 */       return msaf.isRequired();
/*     */     }
/* 706 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isEnabled(WSBinding binding)
/*     */   {
/* 717 */     return (binding.isFeatureEnabled(MemberSubmissionAddressingFeature.class)) || (binding.isFeatureEnabled(AddressingFeature.class));
/*     */   }
/*     */ 
/*     */   public static final class EPR
/*     */   {
/*     */     public final Class<? extends EndpointReference> eprClass;
/*     */     public final String address;
/*     */     public final String serviceName;
/*     */     public final String portName;
/*     */     public final String portTypeName;
/*     */     public final String referenceParameters;
/*     */     public final QName wsdlMetadata;
/*     */     public final String referenceProperties;
/*     */ 
/*     */     public EPR(Class<? extends EndpointReference> eprClass, String address, String serviceName, String portName, String portTypeName, QName wsdlMetadata, String referenceParameters, String referenceProperties) {
/* 739 */       this.eprClass = eprClass;
/* 740 */       this.address = address;
/* 741 */       this.serviceName = serviceName;
/* 742 */       this.portName = portName;
/* 743 */       this.portTypeName = portTypeName;
/* 744 */       this.referenceParameters = referenceParameters;
/* 745 */       this.referenceProperties = referenceProperties;
/* 746 */       this.wsdlMetadata = wsdlMetadata;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.addressing.AddressingVersion
 * JD-Core Version:    0.6.2
 */