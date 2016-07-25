/*     */ package com.sun.xml.internal.ws.model;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*     */ import com.sun.xml.internal.ws.developer.JAXBContextFactory;
/*     */ import com.sun.xml.internal.ws.developer.UsesJAXBContextFeature;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundOperationImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPartImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.resources.ModelerMessages;
/*     */ import com.sun.xml.internal.ws.util.Pool.Marshaller;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ public abstract class AbstractSEIModelImpl
/*     */   implements SEIModel
/*     */ {
/* 401 */   private List<Class> additionalClasses = new ArrayList();
/*     */   private Pool.Marshaller marshallers;
/*     */   protected JAXBRIContext jaxbContext;
/*     */   private String wsdlLocation;
/*     */   private QName serviceName;
/*     */   private QName portName;
/*     */   private QName portTypeName;
/* 409 */   private Map<Method, JavaMethodImpl> methodToJM = new HashMap();
/*     */ 
/* 413 */   private Map<QName, JavaMethodImpl> nameToJM = new HashMap();
/*     */ 
/* 417 */   private Map<QName, JavaMethodImpl> wsdlOpToJM = new HashMap();
/*     */ 
/* 419 */   private List<JavaMethodImpl> javaMethods = new ArrayList();
/* 420 */   private final Map<TypeReference, Bridge> bridgeMap = new HashMap();
/* 421 */   protected final QName emptyBodyName = new QName("");
/* 422 */   private String targetNamespace = "";
/* 423 */   private List<String> knownNamespaceURIs = null;
/*     */   private WSDLPortImpl port;
/*     */   private final WebServiceFeature[] features;
/* 427 */   private static final Logger LOGGER = Logger.getLogger(AbstractSEIModelImpl.class.getName());
/*     */ 
/*     */   protected AbstractSEIModelImpl(WebServiceFeature[] features)
/*     */   {
/*  76 */     this.features = features;
/*     */   }
/*     */ 
/*     */   void postProcess()
/*     */   {
/*  81 */     if (this.jaxbContext != null)
/*  82 */       return;
/*  83 */     populateMaps();
/*  84 */     createJAXBContext();
/*     */   }
/*     */ 
/*     */   public void freeze(WSDLPortImpl port)
/*     */   {
/*  92 */     this.port = port;
/*  93 */     for (JavaMethodImpl m : this.javaMethods) {
/*  94 */       m.freeze(port);
/*  95 */       putOp(m.getOperation().getName(), m);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void populateMaps();
/*     */ 
/*     */   public Pool.Marshaller getMarshallerPool()
/*     */   {
/* 106 */     return this.marshallers;
/*     */   }
/*     */ 
/*     */   public JAXBRIContext getJAXBContext()
/*     */   {
/* 113 */     return this.jaxbContext;
/*     */   }
/*     */ 
/*     */   public List<String> getKnownNamespaceURIs()
/*     */   {
/* 120 */     return this.knownNamespaceURIs;
/*     */   }
/*     */ 
/*     */   public final Bridge getBridge(TypeReference type)
/*     */   {
/* 127 */     Bridge b = (Bridge)this.bridgeMap.get(type);
/* 128 */     assert (b != null);
/* 129 */     return b;
/*     */   }
/*     */ 
/*     */   private JAXBRIContext createJAXBContext() {
/* 133 */     final List types = getAllTypeReferences();
/* 134 */     final List cls = new ArrayList(types.size() + this.additionalClasses.size());
/*     */ 
/* 136 */     cls.addAll(this.additionalClasses);
/* 137 */     for (TypeReference type : types) {
/* 138 */       cls.add((Class)type.type);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 143 */       this.jaxbContext = ((JAXBRIContext)AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public JAXBRIContext run() throws Exception {
/* 145 */           if (AbstractSEIModelImpl.LOGGER.isLoggable(Level.FINE)) {
/* 146 */             AbstractSEIModelImpl.LOGGER.log(Level.FINE, "Creating JAXBContext with classes=" + cls + " and types=" + types);
/*     */           }
/* 148 */           UsesJAXBContextFeature f = (UsesJAXBContextFeature)WebServiceFeatureList.getFeature(AbstractSEIModelImpl.this.features, UsesJAXBContextFeature.class);
/* 149 */           JAXBContextFactory factory = f != null ? f.getFactory() : null;
/* 150 */           if (factory == null) factory = JAXBContextFactory.DEFAULT;
/* 151 */           return factory.createJAXBContext(AbstractSEIModelImpl.this, cls, types);
/*     */         }
/*     */       }));
/* 154 */       createBridgeMap(types);
/*     */     } catch (PrivilegedActionException e) {
/* 156 */       throw new WebServiceException(ModelerMessages.UNABLE_TO_CREATE_JAXB_CONTEXT(), e);
/*     */     }
/* 158 */     this.knownNamespaceURIs = new ArrayList();
/* 159 */     for (String namespace : this.jaxbContext.getKnownNamespaceURIs()) {
/* 160 */       if ((namespace.length() > 0) && 
/* 161 */         (!namespace.equals("http://www.w3.org/2001/XMLSchema")) && (!namespace.equals("http://www.w3.org/XML/1998/namespace"))) {
/* 162 */         this.knownNamespaceURIs.add(namespace);
/*     */       }
/*     */     }
/*     */ 
/* 166 */     this.marshallers = new Pool.Marshaller(this.jaxbContext);
/*     */ 
/* 168 */     return this.jaxbContext;
/*     */   }
/*     */ 
/*     */   private List<TypeReference> getAllTypeReferences()
/*     */   {
/* 175 */     List types = new ArrayList();
/* 176 */     Collection methods = this.methodToJM.values();
/* 177 */     for (JavaMethodImpl m : methods) {
/* 178 */       m.fillTypes(types);
/*     */     }
/* 180 */     return types;
/*     */   }
/*     */ 
/*     */   private void createBridgeMap(List<TypeReference> types) {
/* 184 */     for (TypeReference type : types) {
/* 185 */       Bridge bridge = this.jaxbContext.createBridge(type);
/* 186 */       this.bridgeMap.put(type, bridge);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isKnownFault(QName name, Method method)
/*     */   {
/* 196 */     JavaMethodImpl m = getJavaMethod(method);
/* 197 */     for (CheckedExceptionImpl ce : m.getCheckedExceptions()) {
/* 198 */       if (ce.getDetailType().tagName.equals(name))
/* 199 */         return true;
/*     */     }
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCheckedException(Method m, Class ex)
/*     */   {
/* 209 */     JavaMethodImpl jm = getJavaMethod(m);
/* 210 */     for (CheckedExceptionImpl ce : jm.getCheckedExceptions()) {
/* 211 */       if (ce.getExceptionClass().equals(ex))
/* 212 */         return true;
/*     */     }
/* 214 */     return false;
/*     */   }
/*     */ 
/*     */   public JavaMethodImpl getJavaMethod(Method method)
/*     */   {
/* 221 */     return (JavaMethodImpl)this.methodToJM.get(method);
/*     */   }
/*     */ 
/*     */   public JavaMethodImpl getJavaMethod(QName name)
/*     */   {
/* 229 */     return (JavaMethodImpl)this.nameToJM.get(name);
/*     */   }
/*     */ 
/*     */   public JavaMethod getJavaMethodForWsdlOperation(QName operationName) {
/* 233 */     return (JavaMethod)this.wsdlOpToJM.get(operationName);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public QName getQNameForJM(JavaMethodImpl jm)
/*     */   {
/* 245 */     for (QName key : this.nameToJM.keySet()) {
/* 246 */       JavaMethodImpl jmethod = (JavaMethodImpl)this.nameToJM.get(key);
/* 247 */       if (jmethod.getOperationName().equals(jm.getOperationName())) {
/* 248 */         return key;
/*     */       }
/*     */     }
/* 251 */     return null;
/*     */   }
/*     */ 
/*     */   public final Collection<JavaMethodImpl> getJavaMethods()
/*     */   {
/* 259 */     return Collections.unmodifiableList(this.javaMethods);
/*     */   }
/*     */ 
/*     */   void addJavaMethod(JavaMethodImpl jm) {
/* 263 */     if (jm != null)
/* 264 */       this.javaMethods.add(jm);
/*     */   }
/*     */ 
/*     */   private List<ParameterImpl> applyRpcLitParamBinding(JavaMethodImpl method, WrapperParameter wrapperParameter, WSDLBoundPortTypeImpl boundPortType, WebParam.Mode mode)
/*     */   {
/* 273 */     QName opName = new QName(boundPortType.getPortTypeName().getNamespaceURI(), method.getOperationName());
/* 274 */     WSDLBoundOperationImpl bo = boundPortType.get(opName);
/* 275 */     Map bodyParams = new HashMap();
/* 276 */     List unboundParams = new ArrayList();
/* 277 */     List attachParams = new ArrayList();
/* 278 */     for (ParameterImpl param : wrapperParameter.wrapperChildren) {
/* 279 */       String partName = param.getPartName();
/* 280 */       if (partName != null)
/*     */       {
/* 283 */         ParameterBinding paramBinding = boundPortType.getBinding(opName, partName, mode);
/*     */ 
/* 285 */         if (paramBinding != null) {
/* 286 */           if (mode == WebParam.Mode.IN)
/* 287 */             param.setInBinding(paramBinding);
/* 288 */           else if ((mode == WebParam.Mode.OUT) || (mode == WebParam.Mode.INOUT)) {
/* 289 */             param.setOutBinding(paramBinding);
/*     */           }
/* 291 */           if (paramBinding.isUnbound())
/* 292 */             unboundParams.add(param);
/* 293 */           else if (paramBinding.isAttachment())
/* 294 */             attachParams.add(param);
/* 295 */           else if (paramBinding.isBody()) {
/* 296 */             if (bo != null) {
/* 297 */               WSDLPartImpl p = bo.getPart(param.getPartName(), mode);
/* 298 */               if (p != null)
/* 299 */                 bodyParams.put(Integer.valueOf(p.getIndex()), param);
/*     */               else
/* 301 */                 bodyParams.put(Integer.valueOf(bodyParams.size()), param);
/*     */             } else {
/* 303 */               bodyParams.put(Integer.valueOf(bodyParams.size()), param);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 309 */     wrapperParameter.clear();
/* 310 */     for (int i = 0; i < bodyParams.size(); i++) {
/* 311 */       ParameterImpl p = (ParameterImpl)bodyParams.get(Integer.valueOf(i));
/* 312 */       wrapperParameter.addWrapperChild(p);
/*     */     }
/*     */ 
/* 316 */     for (ParameterImpl p : unboundParams) {
/* 317 */       wrapperParameter.addWrapperChild(p);
/*     */     }
/* 319 */     return attachParams;
/*     */   }
/*     */ 
/*     */   void put(QName name, JavaMethodImpl jm)
/*     */   {
/* 324 */     this.nameToJM.put(name, jm);
/*     */   }
/*     */ 
/*     */   void put(Method method, JavaMethodImpl jm) {
/* 328 */     this.methodToJM.put(method, jm);
/*     */   }
/*     */ 
/*     */   void putOp(QName opName, JavaMethodImpl jm) {
/* 332 */     this.wsdlOpToJM.put(opName, jm);
/*     */   }
/*     */   public String getWSDLLocation() {
/* 335 */     return this.wsdlLocation;
/*     */   }
/*     */ 
/*     */   void setWSDLLocation(String location) {
/* 339 */     this.wsdlLocation = location;
/*     */   }
/*     */ 
/*     */   public QName getServiceQName() {
/* 343 */     return this.serviceName;
/*     */   }
/*     */ 
/*     */   public WSDLPort getPort() {
/* 347 */     return this.port;
/*     */   }
/*     */ 
/*     */   public QName getPortName() {
/* 351 */     return this.portName;
/*     */   }
/*     */ 
/*     */   public QName getPortTypeName() {
/* 355 */     return this.portTypeName;
/*     */   }
/*     */ 
/*     */   void setServiceQName(QName name) {
/* 359 */     this.serviceName = name;
/*     */   }
/*     */ 
/*     */   void setPortName(QName name) {
/* 363 */     this.portName = name;
/*     */   }
/*     */ 
/*     */   void setPortTypeName(QName name) {
/* 367 */     this.portTypeName = name;
/*     */   }
/*     */ 
/*     */   void setTargetNamespace(String namespace)
/*     */   {
/* 375 */     this.targetNamespace = namespace;
/*     */   }
/*     */ 
/*     */   public String getTargetNamespace()
/*     */   {
/* 383 */     return this.targetNamespace;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public QName getBoundPortTypeName() {
/* 388 */     assert (this.portName != null);
/* 389 */     return new QName(this.portName.getNamespaceURI(), this.portName.getLocalPart() + "Binding");
/*     */   }
/*     */ 
/*     */   public void addAdditionalClasses(Class[] additionalClasses)
/*     */   {
/* 397 */     for (Class cls : additionalClasses)
/* 398 */       this.additionalClasses.add(cls);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.AbstractSEIModelImpl
 * JD-Core Version:    0.6.2
 */