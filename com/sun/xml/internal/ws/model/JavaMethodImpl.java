/*     */ package com.sun.xml.internal.ws.model;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.model.MEP;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
/*     */ import com.sun.xml.internal.ws.model.soap.SOAPBindingImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundOperationImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLInputImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLOperationImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLOutputImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortTypeImpl;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import javax.jws.WebMethod;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Action;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class JavaMethodImpl
/*     */   implements JavaMethod
/*     */ {
/*  57 */   private String inputAction = "";
/*  58 */   private String outputAction = "";
/*  59 */   private final List<CheckedExceptionImpl> exceptions = new ArrayList();
/*     */   private final Method method;
/*  61 */   final List<ParameterImpl> requestParams = new ArrayList();
/*  62 */   final List<ParameterImpl> responseParams = new ArrayList();
/*  63 */   private final List<ParameterImpl> unmReqParams = Collections.unmodifiableList(this.requestParams);
/*  64 */   private final List<ParameterImpl> unmResParams = Collections.unmodifiableList(this.responseParams);
/*     */   private SOAPBindingImpl binding;
/*     */   private MEP mep;
/*     */   private String operationName;
/*     */   private WSDLBoundOperationImpl wsdlOperation;
/*     */   final AbstractSEIModelImpl owner;
/*     */   private final Method seiMethod;
/* 388 */   private static final Logger LOGGER = Logger.getLogger(JavaMethodImpl.class.getName());
/*     */ 
/*     */   public JavaMethodImpl(AbstractSEIModelImpl owner, Method method, Method seiMethod)
/*     */   {
/*  79 */     this.owner = owner;
/*  80 */     this.method = method;
/*  81 */     this.seiMethod = seiMethod;
/*  82 */     setWsaActions();
/*     */   }
/*     */ 
/*     */   private void setWsaActions() {
/*  86 */     Action action = (Action)this.seiMethod.getAnnotation(Action.class);
/*  87 */     if (action != null) {
/*  88 */       this.inputAction = action.input();
/*  89 */       this.outputAction = action.output();
/*     */     }
/*     */ 
/*  93 */     WebMethod webMethod = (WebMethod)this.seiMethod.getAnnotation(WebMethod.class);
/*  94 */     String soapAction = "";
/*  95 */     if (webMethod != null)
/*  96 */       soapAction = webMethod.action();
/*  97 */     if (!soapAction.equals(""))
/*     */     {
/*  99 */       if (this.inputAction.equals(""))
/*     */       {
/* 101 */         this.inputAction = soapAction;
/* 102 */       } else if (!this.inputAction.equals(soapAction))
/*     */       {
/* 104 */         throw new WebServiceException("@Action and @WebMethod(action=\"\" does not match on operation " + this.method.getName());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public SEIModel getOwner() {
/* 110 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public Method getMethod()
/*     */   {
/* 119 */     return this.method;
/*     */   }
/*     */ 
/*     */   public Method getSEIMethod()
/*     */   {
/* 128 */     return this.seiMethod;
/*     */   }
/*     */ 
/*     */   public MEP getMEP()
/*     */   {
/* 135 */     return this.mep;
/*     */   }
/*     */ 
/*     */   void setMEP(MEP mep)
/*     */   {
/* 143 */     this.mep = mep;
/*     */   }
/*     */ 
/*     */   public SOAPBindingImpl getBinding()
/*     */   {
/* 150 */     if (this.binding == null)
/* 151 */       return new SOAPBindingImpl();
/* 152 */     return this.binding;
/*     */   }
/*     */ 
/*     */   void setBinding(SOAPBindingImpl binding)
/*     */   {
/* 159 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public WSDLBoundOperation getOperation()
/*     */   {
/* 169 */     assert (this.wsdlOperation != null);
/* 170 */     return this.wsdlOperation;
/*     */   }
/*     */ 
/*     */   public void setOperationName(String name) {
/* 174 */     this.operationName = name;
/*     */   }
/*     */ 
/*     */   public String getOperationName() {
/* 178 */     return this.operationName;
/*     */   }
/*     */ 
/*     */   public String getRequestMessageName() {
/* 182 */     return this.operationName;
/*     */   }
/*     */ 
/*     */   public String getResponseMessageName() {
/* 186 */     if (this.mep.isOneWay())
/* 187 */       return null;
/* 188 */     return this.operationName + "Response";
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public QName getRequestPayloadName()
/*     */   {
/* 195 */     return this.wsdlOperation.getReqPayloadName();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public QName getResponsePayloadName()
/*     */   {
/* 202 */     return this.mep == MEP.ONE_WAY ? null : this.wsdlOperation.getResPayloadName();
/*     */   }
/*     */ 
/*     */   public List<ParameterImpl> getRequestParameters()
/*     */   {
/* 209 */     return this.unmReqParams;
/*     */   }
/*     */ 
/*     */   public List<ParameterImpl> getResponseParameters()
/*     */   {
/* 216 */     return this.unmResParams;
/*     */   }
/*     */ 
/*     */   void addParameter(ParameterImpl p) {
/* 220 */     if ((p.isIN()) || (p.isINOUT())) {
/* 221 */       assert (!this.requestParams.contains(p));
/* 222 */       this.requestParams.add(p);
/*     */     }
/*     */ 
/* 225 */     if ((p.isOUT()) || (p.isINOUT()))
/*     */     {
/* 227 */       assert (!this.responseParams.contains(p));
/* 228 */       this.responseParams.add(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   void addRequestParameter(ParameterImpl p) {
/* 233 */     if ((p.isIN()) || (p.isINOUT()))
/* 234 */       this.requestParams.add(p);
/*     */   }
/*     */ 
/*     */   void addResponseParameter(ParameterImpl p)
/*     */   {
/* 239 */     if ((p.isOUT()) || (p.isINOUT()))
/* 240 */       this.responseParams.add(p);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getInputParametersCount()
/*     */   {
/* 251 */     int count = 0;
/* 252 */     for (ParameterImpl param : this.requestParams) {
/* 253 */       if (param.isWrapperStyle())
/* 254 */         count += ((WrapperParameter)param).getWrapperChildren().size();
/*     */       else {
/* 256 */         count++;
/*     */       }
/*     */     }
/*     */ 
/* 260 */     for (ParameterImpl param : this.responseParams) {
/* 261 */       if (param.isWrapperStyle()) {
/* 262 */         for (ParameterImpl wc : ((WrapperParameter)param).getWrapperChildren()) {
/* 263 */           if ((!wc.isResponse()) && (wc.isOUT()))
/* 264 */             count++;
/*     */         }
/*     */       }
/* 267 */       else if ((!param.isResponse()) && (param.isOUT())) {
/* 268 */         count++;
/*     */       }
/*     */     }
/*     */ 
/* 272 */     return count;
/*     */   }
/*     */ 
/*     */   void addException(CheckedExceptionImpl ce)
/*     */   {
/* 279 */     if (!this.exceptions.contains(ce))
/* 280 */       this.exceptions.add(ce);
/*     */   }
/*     */ 
/*     */   public CheckedExceptionImpl getCheckedException(Class exceptionClass)
/*     */   {
/* 289 */     for (CheckedExceptionImpl ce : this.exceptions) {
/* 290 */       if (ce.getExceptionClass() == exceptionClass)
/* 291 */         return ce;
/*     */     }
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */   public List<CheckedExceptionImpl> getCheckedExceptions()
/*     */   {
/* 301 */     return Collections.unmodifiableList(this.exceptions);
/*     */   }
/*     */ 
/*     */   public String getInputAction() {
/* 305 */     return this.inputAction;
/*     */   }
/*     */ 
/*     */   public String getOutputAction() {
/* 309 */     return this.outputAction;
/*     */   }
/*     */ 
/*     */   public CheckedExceptionImpl getCheckedException(TypeReference detailType)
/*     */   {
/* 318 */     for (CheckedExceptionImpl ce : this.exceptions) {
/* 319 */       TypeReference actual = ce.getDetailType();
/* 320 */       if ((actual.tagName.equals(detailType.tagName)) && (actual.type == detailType.type)) {
/* 321 */         return ce;
/*     */       }
/*     */     }
/* 324 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isAsync()
/*     */   {
/* 334 */     return this.mep.isAsync;
/*     */   }
/*     */ 
/*     */   void freeze(WSDLPortImpl portType) {
/* 338 */     this.wsdlOperation = portType.getBinding().get(new QName(portType.getBinding().getPortType().getName().getNamespaceURI(), this.operationName));
/*     */ 
/* 340 */     if (this.wsdlOperation == null) {
/* 341 */       throw new WebServiceException("Method " + this.seiMethod.getName() + " is exposed as WebMethod, but there is no corresponding wsdl operation with name " + this.operationName + " in the wsdl:portType" + portType.getBinding().getPortType().getName());
/*     */     }
/*     */ 
/* 345 */     if (this.inputAction.equals(""))
/* 346 */       this.inputAction = this.wsdlOperation.getOperation().getInput().getAction();
/* 347 */     else if (!this.inputAction.equals(this.wsdlOperation.getOperation().getInput().getAction()))
/*     */     {
/* 349 */       LOGGER.warning("Input Action on WSDL operation " + this.wsdlOperation.getName().getLocalPart() + " and @Action on its associated Web Method " + this.seiMethod.getName() + " did not match and will cause problems in dispatching the requests");
/*     */     }
/* 351 */     if (!this.mep.isOneWay()) {
/* 352 */       if (this.outputAction.equals("")) {
/* 353 */         this.outputAction = this.wsdlOperation.getOperation().getOutput().getAction();
/*     */       }
/* 355 */       for (CheckedExceptionImpl ce : this.exceptions)
/* 356 */         if (ce.getFaultAction().equals("")) {
/* 357 */           QName detailQName = ce.getDetailType().tagName;
/* 358 */           WSDLFault wsdlfault = this.wsdlOperation.getOperation().getFault(detailQName);
/* 359 */           if (wsdlfault == null)
/*     */           {
/* 361 */             LOGGER.warning("Mismatch between Java model and WSDL model found, For wsdl operation " + this.wsdlOperation.getName() + ",There is no matching wsdl fault with detail QName " + ce.getDetailType().tagName);
/*     */ 
/* 364 */             ce.setFaultAction(ce.getDefaultFaultAction());
/*     */           } else {
/* 366 */             ce.setFaultAction(wsdlfault.getAction());
/*     */           }
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   final void fillTypes(List<TypeReference> types)
/*     */   {
/* 374 */     fillTypes(this.requestParams, types);
/* 375 */     fillTypes(this.responseParams, types);
/*     */ 
/* 377 */     for (CheckedExceptionImpl ce : this.exceptions)
/* 378 */       types.add(ce.getDetailType());
/*     */   }
/*     */ 
/*     */   private void fillTypes(List<ParameterImpl> params, List<TypeReference> types)
/*     */   {
/* 383 */     for (ParameterImpl p : params)
/* 384 */       p.fillTypes(types);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.JavaMethodImpl
 * JD-Core Version:    0.6.2
 */