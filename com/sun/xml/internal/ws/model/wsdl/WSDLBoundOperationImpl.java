/*     */ package com.sun.xml.internal.ws.model.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPart;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.jws.soap.SOAPBinding.Style;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public final class WSDLBoundOperationImpl extends AbstractExtensibleImpl
/*     */   implements WSDLBoundOperation
/*     */ {
/*     */   private final QName name;
/*     */   private final Map<String, ParameterBinding> inputParts;
/*     */   private final Map<String, ParameterBinding> outputParts;
/*     */   private final Map<String, ParameterBinding> faultParts;
/*     */   private final Map<String, String> inputMimeTypes;
/*     */   private final Map<String, String> outputMimeTypes;
/*     */   private final Map<String, String> faultMimeTypes;
/*  55 */   private boolean explicitInputSOAPBodyParts = false;
/*  56 */   private boolean explicitOutputSOAPBodyParts = false;
/*  57 */   private boolean explicitFaultSOAPBodyParts = false;
/*     */   private Boolean emptyInputBody;
/*     */   private Boolean emptyOutputBody;
/*     */   private Boolean emptyFaultBody;
/*     */   private final Map<String, WSDLPartImpl> inParts;
/*     */   private final Map<String, WSDLPartImpl> outParts;
/*     */   private final Map<String, WSDLPartImpl> fltParts;
/*     */   private final List<WSDLBoundFaultImpl> wsdlBoundFaults;
/*     */   private WSDLOperationImpl operation;
/*     */   private String soapAction;
/*     */   private WSDLBoundOperation.ANONYMOUS anonymous;
/*     */   private final WSDLBoundPortTypeImpl owner;
/* 314 */   private SOAPBinding.Style style = SOAPBinding.Style.DOCUMENT;
/*     */   private String reqNamespace;
/*     */   private String respNamespace;
/*     */   private QName requestPayloadName;
/*     */   private QName responsePayloadName;
/*     */   private boolean emptyRequestPayload;
/*     */   private boolean emptyResponsePayload;
/*     */   private Map<QName, WSDLMessageImpl> messages;
/*     */ 
/*     */   public WSDLBoundOperationImpl(XMLStreamReader xsr, WSDLBoundPortTypeImpl owner, QName name)
/*     */   {
/*  78 */     super(xsr);
/*  79 */     this.name = name;
/*  80 */     this.inputParts = new HashMap();
/*  81 */     this.outputParts = new HashMap();
/*  82 */     this.faultParts = new HashMap();
/*  83 */     this.inputMimeTypes = new HashMap();
/*  84 */     this.outputMimeTypes = new HashMap();
/*  85 */     this.faultMimeTypes = new HashMap();
/*  86 */     this.inParts = new HashMap();
/*  87 */     this.outParts = new HashMap();
/*  88 */     this.fltParts = new HashMap();
/*  89 */     this.wsdlBoundFaults = new ArrayList();
/*  90 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  94 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getSOAPAction() {
/*  98 */     return this.soapAction;
/*     */   }
/*     */ 
/*     */   public void setSoapAction(String soapAction) {
/* 102 */     this.soapAction = (soapAction != null ? soapAction : "");
/*     */   }
/*     */ 
/*     */   public WSDLPartImpl getPart(String partName, WebParam.Mode mode) {
/* 106 */     if (mode == WebParam.Mode.IN)
/* 107 */       return (WSDLPartImpl)this.inParts.get(partName);
/* 108 */     if (mode == WebParam.Mode.OUT) {
/* 109 */       return (WSDLPartImpl)this.outParts.get(partName);
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   public void addPart(WSDLPartImpl part, WebParam.Mode mode) {
/* 115 */     if (mode == WebParam.Mode.IN)
/* 116 */       this.inParts.put(part.getName(), part);
/* 117 */     else if (mode == WebParam.Mode.OUT)
/* 118 */       this.outParts.put(part.getName(), part);
/*     */   }
/*     */ 
/*     */   public Map<String, ParameterBinding> getInputParts()
/*     */   {
/* 127 */     return this.inputParts;
/*     */   }
/*     */ 
/*     */   public Map<String, ParameterBinding> getOutputParts()
/*     */   {
/* 136 */     return this.outputParts;
/*     */   }
/*     */ 
/*     */   public Map<String, ParameterBinding> getFaultParts()
/*     */   {
/* 145 */     return this.faultParts;
/*     */   }
/*     */ 
/*     */   public Map<String, WSDLPart> getInParts()
/*     */   {
/* 150 */     return Collections.unmodifiableMap(this.inParts);
/*     */   }
/*     */ 
/*     */   public Map<String, WSDLPart> getOutParts() {
/* 154 */     return Collections.unmodifiableMap(this.outParts);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public List<WSDLBoundFaultImpl> getFaults() {
/* 159 */     return this.wsdlBoundFaults;
/*     */   }
/*     */ 
/*     */   public void addFault(@NotNull WSDLBoundFaultImpl fault) {
/* 163 */     this.wsdlBoundFaults.add(fault);
/*     */   }
/*     */ 
/*     */   public Map<String, String> getInputMimeTypes()
/*     */   {
/* 173 */     return this.inputMimeTypes;
/*     */   }
/*     */ 
/*     */   public Map<String, String> getOutputMimeTypes()
/*     */   {
/* 182 */     return this.outputMimeTypes;
/*     */   }
/*     */ 
/*     */   public Map<String, String> getFaultMimeTypes()
/*     */   {
/* 191 */     return this.faultMimeTypes;
/*     */   }
/*     */ 
/*     */   public ParameterBinding getInputBinding(String part)
/*     */   {
/* 201 */     if (this.emptyInputBody == null) {
/* 202 */       if (this.inputParts.get(" ") != null)
/* 203 */         this.emptyInputBody = Boolean.valueOf(true);
/*     */       else
/* 205 */         this.emptyInputBody = Boolean.valueOf(false);
/*     */     }
/* 207 */     ParameterBinding block = (ParameterBinding)this.inputParts.get(part);
/* 208 */     if (block == null) {
/* 209 */       if ((this.explicitInputSOAPBodyParts) || (this.emptyInputBody.booleanValue()))
/* 210 */         return ParameterBinding.UNBOUND;
/* 211 */       return ParameterBinding.BODY;
/*     */     }
/*     */ 
/* 214 */     return block;
/*     */   }
/*     */ 
/*     */   public ParameterBinding getOutputBinding(String part)
/*     */   {
/* 224 */     if (this.emptyOutputBody == null) {
/* 225 */       if (this.outputParts.get(" ") != null)
/* 226 */         this.emptyOutputBody = Boolean.valueOf(true);
/*     */       else
/* 228 */         this.emptyOutputBody = Boolean.valueOf(false);
/*     */     }
/* 230 */     ParameterBinding block = (ParameterBinding)this.outputParts.get(part);
/* 231 */     if (block == null) {
/* 232 */       if ((this.explicitOutputSOAPBodyParts) || (this.emptyOutputBody.booleanValue()))
/* 233 */         return ParameterBinding.UNBOUND;
/* 234 */       return ParameterBinding.BODY;
/*     */     }
/*     */ 
/* 237 */     return block;
/*     */   }
/*     */ 
/*     */   public ParameterBinding getFaultBinding(String part)
/*     */   {
/* 247 */     if (this.emptyFaultBody == null) {
/* 248 */       if (this.faultParts.get(" ") != null)
/* 249 */         this.emptyFaultBody = Boolean.valueOf(true);
/*     */       else
/* 251 */         this.emptyFaultBody = Boolean.valueOf(false);
/*     */     }
/* 253 */     ParameterBinding block = (ParameterBinding)this.faultParts.get(part);
/* 254 */     if (block == null) {
/* 255 */       if ((this.explicitFaultSOAPBodyParts) || (this.emptyFaultBody.booleanValue()))
/* 256 */         return ParameterBinding.UNBOUND;
/* 257 */       return ParameterBinding.BODY;
/*     */     }
/*     */ 
/* 260 */     return block;
/*     */   }
/*     */ 
/*     */   public String getMimeTypeForInputPart(String part)
/*     */   {
/* 270 */     return (String)this.inputMimeTypes.get(part);
/*     */   }
/*     */ 
/*     */   public String getMimeTypeForOutputPart(String part)
/*     */   {
/* 280 */     return (String)this.outputMimeTypes.get(part);
/*     */   }
/*     */ 
/*     */   public String getMimeTypeForFaultPart(String part)
/*     */   {
/* 290 */     return (String)this.faultMimeTypes.get(part);
/*     */   }
/*     */ 
/*     */   public WSDLOperationImpl getOperation() {
/* 294 */     return this.operation;
/*     */   }
/*     */ 
/*     */   public WSDLBoundPortType getBoundPortType()
/*     */   {
/* 299 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public void setInputExplicitBodyParts(boolean b) {
/* 303 */     this.explicitInputSOAPBodyParts = b;
/*     */   }
/*     */ 
/*     */   public void setOutputExplicitBodyParts(boolean b) {
/* 307 */     this.explicitOutputSOAPBodyParts = b;
/*     */   }
/*     */ 
/*     */   public void setFaultExplicitBodyParts(boolean b) {
/* 311 */     this.explicitFaultSOAPBodyParts = b;
/*     */   }
/*     */ 
/*     */   public void setStyle(SOAPBinding.Style style)
/*     */   {
/* 316 */     this.style = style;
/*     */   }
/*     */   @Nullable
/*     */   public QName getReqPayloadName() {
/* 320 */     if (this.emptyRequestPayload) {
/* 321 */       return null;
/*     */     }
/* 323 */     if (this.requestPayloadName != null) {
/* 324 */       return this.requestPayloadName;
/*     */     }
/* 326 */     if (this.style.equals(SOAPBinding.Style.RPC)) {
/* 327 */       String ns = getRequestNamespace() != null ? getRequestNamespace() : this.name.getNamespaceURI();
/* 328 */       this.requestPayloadName = new QName(ns, this.name.getLocalPart());
/* 329 */       return this.requestPayloadName;
/*     */     }
/* 331 */     QName inMsgName = this.operation.getInput().getMessage().getName();
/* 332 */     WSDLMessageImpl message = (WSDLMessageImpl)this.messages.get(inMsgName);
/* 333 */     for (WSDLPartImpl part : message.parts()) {
/* 334 */       ParameterBinding binding = getInputBinding(part.getName());
/* 335 */       if (binding.isBody()) {
/* 336 */         this.requestPayloadName = part.getDescriptor().name();
/* 337 */         return this.requestPayloadName;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 342 */     this.emptyRequestPayload = true;
/*     */ 
/* 345 */     return null;
/*     */   }
/*     */   @Nullable
/*     */   public QName getResPayloadName() {
/* 349 */     if (this.emptyResponsePayload) {
/* 350 */       return null;
/*     */     }
/* 352 */     if (this.responsePayloadName != null) {
/* 353 */       return this.responsePayloadName;
/*     */     }
/* 355 */     if (this.style.equals(SOAPBinding.Style.RPC)) {
/* 356 */       String ns = getResponseNamespace() != null ? getResponseNamespace() : this.name.getNamespaceURI();
/* 357 */       this.responsePayloadName = new QName(ns, this.name.getLocalPart() + "Response");
/* 358 */       return this.responsePayloadName;
/*     */     }
/* 360 */     QName outMsgName = this.operation.getOutput().getMessage().getName();
/* 361 */     WSDLMessageImpl message = (WSDLMessageImpl)this.messages.get(outMsgName);
/* 362 */     for (WSDLPartImpl part : message.parts()) {
/* 363 */       ParameterBinding binding = getOutputBinding(part.getName());
/* 364 */       if (binding.isBody()) {
/* 365 */         this.responsePayloadName = part.getDescriptor().name();
/* 366 */         return this.responsePayloadName;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 371 */     this.emptyResponsePayload = true;
/*     */ 
/* 374 */     return null;
/*     */   }
/*     */ 
/*     */   public String getRequestNamespace()
/*     */   {
/* 388 */     return this.reqNamespace != null ? this.reqNamespace : this.name.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public void setRequestNamespace(String ns) {
/* 392 */     this.reqNamespace = ns;
/*     */   }
/*     */ 
/*     */   public String getResponseNamespace()
/*     */   {
/* 403 */     return this.respNamespace != null ? this.respNamespace : this.name.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public void setResponseNamespace(String ns) {
/* 407 */     this.respNamespace = ns;
/*     */   }
/*     */ 
/*     */   WSDLBoundPortTypeImpl getOwner() {
/* 411 */     return this.owner;
/*     */   }
/*     */ 
/*     */   void freeze(WSDLModelImpl parent)
/*     */   {
/* 421 */     this.messages = parent.getMessages();
/* 422 */     this.operation = this.owner.getPortType().get(this.name.getLocalPart());
/* 423 */     for (WSDLBoundFaultImpl bf : this.wsdlBoundFaults)
/* 424 */       bf.freeze(this);
/*     */   }
/*     */ 
/*     */   public void setAnonymous(WSDLBoundOperation.ANONYMOUS anonymous)
/*     */   {
/* 429 */     this.anonymous = anonymous;
/*     */   }
/*     */ 
/*     */   public WSDLBoundOperation.ANONYMOUS getAnonymous()
/*     */   {
/* 436 */     return this.anonymous;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLBoundOperationImpl
 * JD-Core Version:    0.6.2
 */