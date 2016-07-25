/*     */ package com.sun.xml.internal.ws.api.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*     */ import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
/*     */ import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.api.DistributedPropertySet;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.Property;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.PropertyMap;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.server.TransportBackChannel;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
/*     */ import com.sun.xml.internal.ws.client.ContentNegotiation;
/*     */ import com.sun.xml.internal.ws.client.HandlerConfiguration;
/*     */ import com.sun.xml.internal.ws.client.Stub;
/*     */ import com.sun.xml.internal.ws.message.RelatesToHeader;
/*     */ import com.sun.xml.internal.ws.message.StringHeader;
/*     */ import com.sun.xml.internal.ws.server.WSEndpointImpl;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.xml.internal.ws.wsdl.DispatchException;
/*     */ import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.BindingProvider;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class Packet extends DistributedPropertySet
/*     */ {
/*     */   private Message message;
/*     */   private QName wsdlOperation;
/*     */   public boolean wasTransportSecure;
/*     */   public static final String INBOUND_TRANSPORT_HEADERS = "com.sun.xml.internal.ws.api.message.packet.inbound.transport.headers";
/*     */   public static final String OUTBOUND_TRANSPORT_HEADERS = "com.sun.xml.internal.ws.api.message.packet.outbound.transport.headers";
/*     */   public static final String HA_INFO = "com.sun.xml.internal.ws.api.message.packet.hainfo";
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.handler.config"})
/*     */   public HandlerConfiguration handlerConfig;
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.client.handle"})
/*     */   public BindingProvider proxy;
/*     */   public EndpointAddress endpointAddress;
/*     */   public ContentNegotiation contentNegotiation;
/*     */   public String acceptableMimeTypes;
/*     */   public WebServiceContextDelegate webServiceContextDelegate;
/*     */ 
/*     */   @Nullable
/*     */   public TransportBackChannel transportBackChannel;
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.api.server.WSEndpoint"})
/*     */   public WSEndpoint endpoint;
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.soap.http.soapaction.uri"})
/*     */   public String soapAction;
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.server.OneWayOperation"})
/*     */   public Boolean expectReply;
/*     */ 
/*     */   @Deprecated
/*     */   public Boolean isOneWay;
/*     */   private Set<String> handlerScopePropertyNames;
/*     */   public final Map<String, Object> invocationProperties;
/* 869 */   private static final PropertySet.PropertyMap model = parse(Packet.class);
/*     */ 
/* 876 */   private static final Logger LOGGER = Logger.getLogger(Packet.class.getName());
/*     */ 
/*     */   public Packet(Message request)
/*     */   {
/* 160 */     this();
/* 161 */     this.message = request;
/*     */   }
/*     */ 
/*     */   public Packet()
/*     */   {
/* 168 */     this.invocationProperties = new HashMap();
/*     */   }
/*     */ 
/*     */   private Packet(Packet that)
/*     */   {
/* 175 */     that.copySatelliteInto(this);
/* 176 */     this.handlerConfig = that.handlerConfig;
/* 177 */     this.invocationProperties = that.invocationProperties;
/* 178 */     this.handlerScopePropertyNames = that.handlerScopePropertyNames;
/* 179 */     this.contentNegotiation = that.contentNegotiation;
/* 180 */     this.wasTransportSecure = that.wasTransportSecure;
/* 181 */     this.endpointAddress = that.endpointAddress;
/* 182 */     this.wsdlOperation = that.wsdlOperation;
/*     */ 
/* 184 */     this.acceptableMimeTypes = that.acceptableMimeTypes;
/* 185 */     this.endpoint = that.endpoint;
/* 186 */     this.proxy = that.proxy;
/* 187 */     this.webServiceContextDelegate = that.webServiceContextDelegate;
/* 188 */     this.soapAction = that.soapAction;
/* 189 */     this.expectReply = that.expectReply;
/*     */   }
/*     */ 
/*     */   public Packet copy(boolean copyMessage)
/*     */   {
/* 204 */     Packet copy = new Packet(this);
/* 205 */     if ((copyMessage) && (this.message != null)) {
/* 206 */       copy.message = this.message.copy();
/*     */     }
/*     */ 
/* 209 */     return copy;
/*     */   }
/*     */ 
/*     */   public Message getMessage()
/*     */   {
/* 221 */     return this.message;
/*     */   }
/*     */ 
/*     */   public void setMessage(Message message)
/*     */   {
/* 231 */     this.message = message;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.wsdl.operation"})
/*     */   @Nullable
/*     */   public final QName getWSDLOperation()
/*     */   {
/* 247 */     if (this.wsdlOperation != null) {
/* 248 */       return this.wsdlOperation;
/*     */     }
/* 250 */     OperationDispatcher opDispatcher = null;
/* 251 */     if (this.endpoint != null)
/* 252 */       opDispatcher = ((WSEndpointImpl)this.endpoint).getOperationDispatcher();
/* 253 */     else if (this.proxy != null) {
/* 254 */       opDispatcher = ((Stub)this.proxy).getOperationDispatcher();
/*     */     }
/*     */ 
/* 257 */     if (opDispatcher != null) {
/*     */       try {
/* 259 */         this.wsdlOperation = opDispatcher.getWSDLOperationQName(this);
/*     */       }
/*     */       catch (DispatchException e)
/*     */       {
/*     */       }
/*     */     }
/* 265 */     return this.wsdlOperation;
/*     */   }
/*     */ 
/*     */   public void setWSDLOperation(QName wsdlOp)
/*     */   {
/* 276 */     this.wsdlOperation = wsdlOp;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   @PropertySet.Property({"javax.xml.ws.service.endpoint.address"})
/*     */   public String getEndPointAddressString()
/*     */   {
/* 353 */     if (this.endpointAddress == null) {
/* 354 */       return null;
/*     */     }
/* 356 */     return this.endpointAddress.toString();
/*     */   }
/*     */ 
/*     */   public void setEndPointAddressString(String s) {
/* 360 */     if (s == null)
/* 361 */       this.endpointAddress = null;
/*     */     else
/* 363 */       this.endpointAddress = EndpointAddress.create(s);
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.client.ContentNegotiation"})
/*     */   public String getContentNegotiationString()
/*     */   {
/* 376 */     return this.contentNegotiation != null ? this.contentNegotiation.toString() : null;
/*     */   }
/*     */ 
/*     */   public void setContentNegotiationString(String s) {
/* 380 */     if (s == null)
/* 381 */       this.contentNegotiation = null;
/*     */     else
/*     */       try {
/* 384 */         this.contentNegotiation = ContentNegotiation.valueOf(s);
/*     */       }
/*     */       catch (IllegalArgumentException e) {
/* 387 */         this.contentNegotiation = ContentNegotiation.none;
/*     */       }
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.reference.parameters"})
/*     */   @NotNull
/*     */   public List<Element> getReferenceParameters()
/*     */   {
/* 401 */     List refParams = new ArrayList();
/* 402 */     HeaderList hl = this.message.getHeaders();
/* 403 */     for (Header h : hl) {
/* 404 */       String attr = h.getAttribute(AddressingVersion.W3C.nsUri, "IsReferenceParameter");
/* 405 */       if ((attr != null) && ((attr.equals("true")) || (attr.equals("1")))) {
/* 406 */         Document d = DOMUtil.createDom();
/* 407 */         SAX2DOMEx s2d = new SAX2DOMEx(d);
/*     */         try {
/* 409 */           h.writeTo(s2d, XmlUtil.DRACONIAN_ERROR_HANDLER);
/* 410 */           refParams.add((Element)d.getLastChild());
/*     */         } catch (SAXException e) {
/* 412 */           throw new WebServiceException(e);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 426 */     return refParams;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.api.message.HeaderList"})
/*     */   HeaderList getHeaderList()
/*     */   {
/* 436 */     if (this.message == null) return null;
/* 437 */     return this.message.getHeaders();
/*     */   }
/*     */ 
/*     */   public TransportBackChannel keepTransportBackChannelOpen()
/*     */   {
/* 491 */     TransportBackChannel r = this.transportBackChannel;
/* 492 */     this.transportBackChannel = null;
/* 493 */     return r;
/*     */   }
/*     */ 
/*     */   public final Set<String> getHandlerScopePropertyNames(boolean readOnly)
/*     */   {
/* 650 */     Set o = this.handlerScopePropertyNames;
/* 651 */     if (o == null) {
/* 652 */       if (readOnly)
/* 653 */         return Collections.emptySet();
/* 654 */       o = new HashSet();
/* 655 */       this.handlerScopePropertyNames = o;
/*     */     }
/* 657 */     return o;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final Set<String> getApplicationScopePropertyNames(boolean readOnly)
/*     */   {
/* 668 */     if (!$assertionsDisabled) throw new AssertionError();
/* 669 */     return new HashSet();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Packet createResponse(Message msg)
/*     */   {
/* 689 */     Packet response = new Packet(this);
/* 690 */     response.setMessage(msg);
/* 691 */     return response;
/*     */   }
/*     */ 
/*     */   public Packet createClientResponse(Message msg)
/*     */   {
/* 705 */     Packet response = new Packet(this);
/* 706 */     response.soapAction = null;
/* 707 */     response.setMessage(msg);
/* 708 */     return response;
/*     */   }
/*     */ 
/*     */   public Packet createServerResponse(@Nullable Message responseMessage, @Nullable WSDLPort wsdlPort, @Nullable SEIModel seiModel, @NotNull WSBinding binding)
/*     */   {
/* 728 */     Packet r = createClientResponse(responseMessage);
/*     */ 
/* 730 */     AddressingVersion av = binding.getAddressingVersion();
/*     */ 
/* 732 */     if (av == null) {
/* 733 */       return r;
/*     */     }
/* 735 */     String inputAction = getMessage().getHeaders().getAction(av, binding.getSOAPVersion());
/* 736 */     if (inputAction == null) {
/* 737 */       return r;
/*     */     }
/*     */ 
/* 740 */     if ((responseMessage == null) || ((wsdlPort != null) && (this.message.isOneWay(wsdlPort)))) {
/* 741 */       return r;
/*     */     }
/*     */ 
/* 744 */     populateAddressingHeaders(binding, r, wsdlPort, seiModel);
/* 745 */     return r;
/*     */   }
/*     */ 
/*     */   public Packet createServerResponse(@Nullable Message responseMessage, @NotNull AddressingVersion addressingVersion, @NotNull SOAPVersion soapVersion, @NotNull String action)
/*     */   {
/* 765 */     Packet responsePacket = createClientResponse(responseMessage);
/*     */ 
/* 768 */     if (addressingVersion == null) {
/* 769 */       return responsePacket;
/*     */     }
/* 771 */     String inputAction = getMessage().getHeaders().getAction(addressingVersion, soapVersion);
/* 772 */     if (inputAction == null) {
/* 773 */       return responsePacket;
/*     */     }
/*     */ 
/* 776 */     populateAddressingHeaders(responsePacket, addressingVersion, soapVersion, action, false);
/* 777 */     return responsePacket;
/*     */   }
/*     */ 
/*     */   public void setResponseMessage(@NotNull Packet request, @Nullable Message responseMessage, @NotNull AddressingVersion addressingVersion, @NotNull SOAPVersion soapVersion, @NotNull String action)
/*     */   {
/* 791 */     Packet temp = request.createServerResponse(responseMessage, addressingVersion, soapVersion, action);
/* 792 */     setMessage(temp.getMessage());
/*     */   }
/*     */ 
/*     */   private void populateAddressingHeaders(Packet responsePacket, AddressingVersion av, SOAPVersion sv, String action, boolean mustUnderstand)
/*     */   {
/* 797 */     if (av == null) return;
/*     */ 
/* 800 */     if (responsePacket.getMessage() == null) {
/* 801 */       return;
/*     */     }
/* 803 */     HeaderList hl = responsePacket.getMessage().getHeaders();
/*     */     WSEndpointReference replyTo;
/*     */     try
/*     */     {
/* 808 */       replyTo = this.message.getHeaders().getReplyTo(av, sv);
/* 809 */       if (replyTo != null)
/* 810 */         hl.add(new StringHeader(av.toTag, replyTo.getAddress()));
/*     */     } catch (InvalidAddressingHeaderException e) {
/* 812 */       replyTo = null;
/*     */     }
/*     */ 
/* 818 */     if (responsePacket.getMessage().getHeaders().getAction(av, sv) == null)
/*     */     {
/* 820 */       hl.add(new StringHeader(av.actionTag, action, sv, mustUnderstand));
/*     */     }
/*     */ 
/* 824 */     hl.add(new StringHeader(av.messageIDTag, responsePacket.getMessage().getID(av, sv)));
/*     */ 
/* 827 */     String mid = getMessage().getHeaders().getMessageID(av, sv);
/* 828 */     if (mid != null)
/* 829 */       hl.add(new RelatesToHeader(av.relatesToTag, mid));
/*     */     WSEndpointReference refpEPR;
/* 833 */     if (responsePacket.getMessage().isFault())
/*     */     {
/* 835 */       WSEndpointReference refpEPR = this.message.getHeaders().getFaultTo(av, sv);
/*     */ 
/* 838 */       if (refpEPR == null)
/* 839 */         refpEPR = replyTo;
/*     */     }
/*     */     else {
/* 842 */       refpEPR = replyTo;
/*     */     }
/* 844 */     if (refpEPR != null)
/* 845 */       refpEPR.addReferenceParameters(hl);
/*     */   }
/*     */ 
/*     */   private void populateAddressingHeaders(WSBinding binding, Packet responsePacket, WSDLPort wsdlPort, SEIModel seiModel)
/*     */   {
/* 850 */     AddressingVersion addressingVersion = binding.getAddressingVersion();
/*     */ 
/* 852 */     if (addressingVersion == null) return;
/*     */ 
/* 854 */     WsaTubeHelper wsaHelper = addressingVersion.getWsaHelper(wsdlPort, seiModel, binding);
/* 855 */     String action = responsePacket.message.isFault() ? wsaHelper.getFaultAction(this, responsePacket) : wsaHelper.getOutputAction(this);
/*     */ 
/* 858 */     if (action == null) {
/* 859 */       LOGGER.info("WSA headers are not added as value for wsa:Action cannot be resolved for this message");
/* 860 */       return;
/*     */     }
/* 862 */     populateAddressingHeaders(responsePacket, addressingVersion, binding.getSOAPVersion(), action, AddressingVersion.isRequired(binding));
/*     */   }
/*     */ 
/*     */   protected PropertySet.PropertyMap getPropertyMap()
/*     */   {
/* 873 */     return model;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.Packet
 * JD-Core Version:    0.6.2
 */