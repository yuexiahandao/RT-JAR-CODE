/*     */ package com.sun.xml.internal.ws.model.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import com.sun.xml.internal.ws.util.QNameMap;
/*     */ import com.sun.xml.internal.ws.util.exception.LocatableWebServiceException;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.jws.soap.SOAPBinding.Style;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.ws.soap.MTOMFeature;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public final class WSDLBoundPortTypeImpl extends AbstractFeaturedObjectImpl
/*     */   implements WSDLBoundPortType
/*     */ {
/*     */   private final QName name;
/*     */   private final QName portTypeName;
/*     */   private WSDLPortTypeImpl portType;
/*     */   private BindingID bindingId;
/*     */ 
/*     */   @NotNull
/*     */   private final WSDLModelImpl owner;
/*  56 */   private final QNameMap<WSDLBoundOperationImpl> bindingOperations = new QNameMap();
/*     */   private QNameMap<WSDLBoundOperationImpl> payloadMap;
/*     */   private WSDLBoundOperationImpl emptyPayloadOperation;
/* 125 */   private SOAPBinding.Style style = SOAPBinding.Style.DOCUMENT;
/*     */ 
/*     */   public WSDLBoundPortTypeImpl(XMLStreamReader xsr, @NotNull WSDLModelImpl owner, QName name, QName portTypeName)
/*     */   {
/*  70 */     super(xsr);
/*  71 */     this.owner = owner;
/*  72 */     this.name = name;
/*  73 */     this.portTypeName = portTypeName;
/*  74 */     owner.addBinding(this);
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  78 */     return this.name;
/*     */   }
/*     */   @NotNull
/*     */   public WSDLModelImpl getOwner() {
/*  82 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public WSDLBoundOperationImpl get(QName operationName) {
/*  86 */     return (WSDLBoundOperationImpl)this.bindingOperations.get(operationName);
/*     */   }
/*     */ 
/*     */   public void put(QName opName, WSDLBoundOperationImpl ptOp)
/*     */   {
/*  97 */     this.bindingOperations.put(opName, ptOp);
/*     */   }
/*     */ 
/*     */   public QName getPortTypeName() {
/* 101 */     return this.portTypeName;
/*     */   }
/*     */ 
/*     */   public WSDLPortTypeImpl getPortType() {
/* 105 */     return this.portType;
/*     */   }
/*     */ 
/*     */   public Iterable<WSDLBoundOperationImpl> getBindingOperations() {
/* 109 */     return this.bindingOperations.values();
/*     */   }
/*     */ 
/*     */   public BindingID getBindingId()
/*     */   {
/* 115 */     return this.bindingId == null ? BindingID.SOAP11_HTTP : this.bindingId;
/*     */   }
/*     */ 
/*     */   public void setBindingId(BindingID bindingId) {
/* 119 */     this.bindingId = bindingId;
/*     */   }
/*     */ 
/*     */   public void setStyle(SOAPBinding.Style style)
/*     */   {
/* 127 */     this.style = style;
/*     */   }
/*     */ 
/*     */   public SOAPBinding.Style getStyle() {
/* 131 */     return this.style;
/*     */   }
/*     */ 
/*     */   public boolean isRpcLit() {
/* 135 */     return SOAPBinding.Style.RPC == this.style;
/*     */   }
/*     */ 
/*     */   public boolean isDoclit() {
/* 139 */     return SOAPBinding.Style.DOCUMENT == this.style;
/*     */   }
/*     */ 
/*     */   public ParameterBinding getBinding(QName operation, String part, WebParam.Mode mode)
/*     */   {
/* 152 */     WSDLBoundOperationImpl op = get(operation);
/* 153 */     if (op == null)
/*     */     {
/* 155 */       return null;
/*     */     }
/* 157 */     if ((WebParam.Mode.IN == mode) || (WebParam.Mode.INOUT == mode)) {
/* 158 */       return op.getInputBinding(part);
/*     */     }
/* 160 */     return op.getOutputBinding(part);
/*     */   }
/*     */ 
/*     */   public String getMimeType(QName operation, String part, WebParam.Mode mode)
/*     */   {
/* 172 */     WSDLBoundOperationImpl op = get(operation);
/* 173 */     if (WebParam.Mode.IN == mode) {
/* 174 */       return op.getMimeTypeForInputPart(part);
/*     */     }
/* 176 */     return op.getMimeTypeForOutputPart(part);
/*     */   }
/*     */ 
/*     */   public WSDLBoundOperationImpl getOperation(String namespaceUri, String localName) {
/* 180 */     if ((namespaceUri == null) && (localName == null)) {
/* 181 */       return this.emptyPayloadOperation;
/*     */     }
/* 183 */     return (WSDLBoundOperationImpl)this.payloadMap.get(namespaceUri == null ? "" : namespaceUri, localName);
/*     */   }
/*     */ 
/*     */   public void enableMTOM()
/*     */   {
/* 188 */     this.features.add(new MTOMFeature());
/*     */   }
/*     */ 
/*     */   public boolean isMTOMEnabled() {
/* 192 */     return this.features.isEnabled(MTOMFeature.class);
/*     */   }
/*     */ 
/*     */   public SOAPVersion getSOAPVersion() {
/* 196 */     return getBindingId().getSOAPVersion();
/*     */   }
/*     */ 
/*     */   void freeze() {
/* 200 */     this.portType = this.owner.getPortType(this.portTypeName);
/* 201 */     if (this.portType == null) {
/* 202 */       throw new LocatableWebServiceException(ClientMessages.UNDEFINED_PORT_TYPE(this.portTypeName), new Locator[] { getLocation() });
/*     */     }
/*     */ 
/* 205 */     this.portType.freeze();
/*     */ 
/* 207 */     for (WSDLBoundOperationImpl op : this.bindingOperations.values()) {
/* 208 */       op.freeze(this.owner);
/*     */     }
/*     */ 
/* 211 */     freezePayloadMap();
/* 212 */     this.owner.finalizeRpcLitBinding(this);
/*     */   }
/*     */ 
/*     */   private void freezePayloadMap() {
/* 216 */     if (this.style == SOAPBinding.Style.RPC) {
/* 217 */       this.payloadMap = new QNameMap();
/* 218 */       for (WSDLBoundOperationImpl op : this.bindingOperations.values())
/* 219 */         this.payloadMap.put(op.getReqPayloadName(), op);
/*     */     }
/*     */     else {
/* 222 */       this.payloadMap = new QNameMap();
/*     */ 
/* 224 */       for (WSDLBoundOperationImpl op : this.bindingOperations.values()) {
/* 225 */         QName name = op.getReqPayloadName();
/* 226 */         if (name == null)
/*     */         {
/* 228 */           this.emptyPayloadOperation = op;
/*     */         }
/*     */         else
/*     */         {
/* 232 */           this.payloadMap.put(name, op);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl
 * JD-Core Version:    0.6.2
 */