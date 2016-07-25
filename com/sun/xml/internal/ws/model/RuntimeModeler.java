/*      */ package com.sun.xml.internal.ws.model;
/*      */ 
/*      */ import com.sun.istack.internal.NotNull;
/*      */ import com.sun.xml.internal.bind.api.CompositeStructure;
/*      */ import com.sun.xml.internal.bind.api.TypeReference;
/*      */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*      */ import com.sun.xml.internal.ws.api.BindingID;
/*      */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*      */ import com.sun.xml.internal.ws.api.model.ExceptionType;
/*      */ import com.sun.xml.internal.ws.api.model.MEP;
/*      */ import com.sun.xml.internal.ws.api.model.Parameter;
/*      */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*      */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPart;
/*      */ import com.sun.xml.internal.ws.model.soap.SOAPBindingImpl;
/*      */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundOperationImpl;
/*      */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*      */ import com.sun.xml.internal.ws.model.wsdl.WSDLInputImpl;
/*      */ import com.sun.xml.internal.ws.model.wsdl.WSDLOperationImpl;
/*      */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*      */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortTypeImpl;
/*      */ import com.sun.xml.internal.ws.resources.ModelerMessages;
/*      */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*      */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Type;
/*      */ import java.rmi.RemoteException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Collection;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.logging.Logger;
/*      */ import javax.jws.Oneway;
/*      */ import javax.jws.WebMethod;
/*      */ import javax.jws.WebParam;
/*      */ import javax.jws.WebParam.Mode;
/*      */ import javax.jws.WebResult;
/*      */ import javax.jws.WebService;
/*      */ import javax.jws.soap.SOAPBinding;
/*      */ import javax.jws.soap.SOAPBinding.ParameterStyle;
/*      */ import javax.jws.soap.SOAPBinding.Style;
/*      */ import javax.xml.bind.annotation.XmlElement;
/*      */ import javax.xml.bind.annotation.XmlSeeAlso;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.ws.Action;
/*      */ import javax.xml.ws.AsyncHandler;
/*      */ import javax.xml.ws.FaultAction;
/*      */ import javax.xml.ws.Holder;
/*      */ import javax.xml.ws.RequestWrapper;
/*      */ import javax.xml.ws.Response;
/*      */ import javax.xml.ws.ResponseWrapper;
/*      */ import javax.xml.ws.WebFault;
/*      */ import javax.xml.ws.WebServiceFeature;
/*      */ 
/*      */ public class RuntimeModeler
/*      */ {
/*      */   private final WebServiceFeature[] features;
/*      */   private final BindingID bindingId;
/*      */   private final Class portClass;
/*      */   private AbstractSEIModelImpl model;
/*      */   private SOAPBindingImpl defaultBinding;
/*      */   private String packageName;
/*      */   private String targetNamespace;
/*   87 */   private boolean isWrapped = true;
/*      */   private ClassLoader classLoader;
/*      */   private final WSDLPortImpl binding;
/*      */   private QName serviceName;
/*      */   private QName portName;
/*      */   public static final String PD_JAXWS_PACKAGE_PD = ".jaxws.";
/*      */   public static final String JAXWS_PACKAGE_PD = "jaxws.";
/*      */   public static final String RESPONSE = "Response";
/*      */   public static final String RETURN = "return";
/*      */   public static final String BEAN = "Bean";
/*      */   public static final String SERVICE = "Service";
/*      */   public static final String PORT = "Port";
/*  106 */   public static final Class HOLDER_CLASS = Holder.class;
/*  107 */   public static final Class<RemoteException> REMOTE_EXCEPTION_CLASS = RemoteException.class;
/*  108 */   public static final Class<RuntimeException> RUNTIME_EXCEPTION_CLASS = RuntimeException.class;
/*  109 */   public static final Class<Exception> EXCEPTION_CLASS = Exception.class;
/*      */ 
/*  176 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server");
/*      */ 
/*      */   public RuntimeModeler(@NotNull Class portClass, @NotNull QName serviceName, @NotNull BindingID bindingId, @NotNull WebServiceFeature[] features)
/*      */   {
/*  112 */     this(portClass, serviceName, null, bindingId, features);
/*      */   }
/*      */ 
/*      */   public RuntimeModeler(@NotNull Class portClass, @NotNull QName serviceName, @NotNull WSDLPortImpl wsdlPort, @NotNull WebServiceFeature[] features)
/*      */   {
/*  124 */     this(portClass, serviceName, wsdlPort, wsdlPort.getBinding().getBindingId(), features);
/*      */   }
/*      */ 
/*      */   private RuntimeModeler(@NotNull Class portClass, @NotNull QName serviceName, WSDLPortImpl binding, BindingID bindingId, @NotNull WebServiceFeature[] features) {
/*  128 */     this.portClass = portClass;
/*  129 */     this.serviceName = serviceName;
/*  130 */     this.binding = binding;
/*  131 */     this.bindingId = bindingId;
/*  132 */     this.features = features;
/*      */   }
/*      */ 
/*      */   public void setClassLoader(ClassLoader classLoader)
/*      */   {
/*  140 */     this.classLoader = classLoader;
/*      */   }
/*      */ 
/*      */   public void setPortName(QName portName)
/*      */   {
/*  149 */     this.portName = portName;
/*      */   }
/*      */ 
/*      */   private static <T extends Annotation> T getPrivClassAnnotation(Class<?> clazz, final Class<T> T) {
/*  153 */     return (Annotation)AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public T run() {
/*  155 */         return this.val$clazz.getAnnotation(T);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static <T extends Annotation> T getPrivMethodAnnotation(Method method, final Class<T> T) {
/*  161 */     return (Annotation)AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public T run() {
/*  163 */         return this.val$method.getAnnotation(T);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static Annotation[][] getPrivParameterAnnotations(Method method) {
/*  169 */     return (Annotation[][])AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Annotation[][] run() {
/*  171 */         return this.val$method.getParameterAnnotations();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public AbstractSEIModelImpl buildRuntimeModel()
/*      */   {
/*  187 */     this.model = new SOAPSEIModel(this.features);
/*  188 */     Class clazz = this.portClass;
/*  189 */     WebService webService = (WebService)getPrivClassAnnotation(this.portClass, WebService.class);
/*  190 */     if (webService == null) {
/*  191 */       throw new RuntimeModelerException("runtime.modeler.no.webservice.annotation", new Object[] { this.portClass.getCanonicalName() });
/*      */     }
/*      */ 
/*  194 */     if (webService.endpointInterface().length() > 0) {
/*  195 */       clazz = getClass(webService.endpointInterface(), ModelerMessages.localizableRUNTIME_MODELER_CLASS_NOT_FOUND(webService.endpointInterface()));
/*  196 */       WebService seiService = (WebService)getPrivClassAnnotation(clazz, WebService.class);
/*  197 */       if (seiService == null) {
/*  198 */         throw new RuntimeModelerException("runtime.modeler.endpoint.interface.no.webservice", new Object[] { webService.endpointInterface() });
/*      */       }
/*      */ 
/*  203 */       SOAPBinding sbPortClass = (SOAPBinding)getPrivClassAnnotation(this.portClass, SOAPBinding.class);
/*  204 */       SOAPBinding sbSei = (SOAPBinding)getPrivClassAnnotation(clazz, SOAPBinding.class);
/*  205 */       if ((sbPortClass != null) && (
/*  206 */         (sbSei == null) || (sbSei.style() != sbPortClass.style()) || (sbSei.use() != sbPortClass.use()))) {
/*  207 */         logger.warning(ServerMessages.RUNTIMEMODELER_INVALIDANNOTATION_ON_IMPL("@SOAPBinding", this.portClass.getName(), clazz.getName()));
/*      */       }
/*      */     }
/*      */ 
/*  211 */     if (this.serviceName == null)
/*  212 */       this.serviceName = getServiceName(this.portClass);
/*  213 */     this.model.setServiceQName(this.serviceName);
/*      */ 
/*  215 */     String portLocalName = this.portClass.getSimpleName() + "Port";
/*  216 */     if (webService.portName().length() > 0)
/*  217 */       portLocalName = webService.portName();
/*  218 */     else if (webService.name().length() > 0) {
/*  219 */       portLocalName = webService.name() + "Port";
/*      */     }
/*      */ 
/*  222 */     if (this.portName == null)
/*  223 */       this.portName = new QName(this.serviceName.getNamespaceURI(), portLocalName);
/*  224 */     if (!this.portName.getNamespaceURI().equals(this.serviceName.getNamespaceURI())) {
/*  225 */       throw new RuntimeModelerException("runtime.modeler.portname.servicename.namespace.mismatch", new Object[] { this.serviceName, this.portName });
/*      */     }
/*      */ 
/*  228 */     this.model.setPortName(this.portName);
/*      */ 
/*  230 */     processClass(clazz);
/*  231 */     if (this.model.getJavaMethods().size() == 0) {
/*  232 */       throw new RuntimeModelerException("runtime.modeler.no.operations", new Object[] { this.portClass.getName() });
/*      */     }
/*  234 */     this.model.postProcess();
/*      */ 
/*  239 */     if (this.binding != null) {
/*  240 */       this.model.freeze(this.binding);
/*      */     }
/*  242 */     return this.model;
/*      */   }
/*      */ 
/*      */   private Class getClass(String className, Localizable errorMessage)
/*      */   {
/*      */     try
/*      */     {
/*  254 */       if (this.classLoader == null) {
/*  255 */         return Thread.currentThread().getContextClassLoader().loadClass(className);
/*      */       }
/*  257 */       return this.classLoader.loadClass(className); } catch (ClassNotFoundException e) {
/*      */     }
/*  259 */     throw new RuntimeModelerException(errorMessage);
/*      */   }
/*      */ 
/*      */   private Class getRequestWrapperClass(String className, Method method, QName reqElemName)
/*      */   {
/*  264 */     ClassLoader loader = this.classLoader == null ? Thread.currentThread().getContextClassLoader() : this.classLoader;
/*      */     try {
/*  266 */       return loader.loadClass(className);
/*      */     } catch (ClassNotFoundException e) {
/*  268 */       logger.fine("Dynamically creating request wrapper Class " + className);
/*  269 */     }return WrapperBeanGenerator.createRequestWrapperBean(className, method, reqElemName, loader);
/*      */   }
/*      */ 
/*      */   private Class getResponseWrapperClass(String className, Method method, QName resElemName)
/*      */   {
/*  274 */     ClassLoader loader = this.classLoader == null ? Thread.currentThread().getContextClassLoader() : this.classLoader;
/*      */     try {
/*  276 */       return loader.loadClass(className);
/*      */     } catch (ClassNotFoundException e) {
/*  278 */       logger.fine("Dynamically creating response wrapper bean Class " + className);
/*  279 */     }return WrapperBeanGenerator.createResponseWrapperBean(className, method, resElemName, loader);
/*      */   }
/*      */ 
/*      */   private Class getExceptionBeanClass(String className, Class exception, String name, String namespace)
/*      */   {
/*  285 */     ClassLoader loader = this.classLoader == null ? Thread.currentThread().getContextClassLoader() : this.classLoader;
/*      */     try {
/*  287 */       return loader.loadClass(className);
/*      */     } catch (ClassNotFoundException e) {
/*  289 */       logger.fine("Dynamically creating exception bean Class " + className);
/*  290 */     }return WrapperBeanGenerator.createExceptionBean(className, exception, this.targetNamespace, name, namespace, loader);
/*      */   }
/*      */ 
/*      */   void processClass(Class clazz)
/*      */   {
/*  295 */     WebService webService = (WebService)getPrivClassAnnotation(clazz, WebService.class);
/*  296 */     String portTypeLocalName = clazz.getSimpleName();
/*  297 */     if (webService.name().length() > 0) {
/*  298 */       portTypeLocalName = webService.name();
/*      */     }
/*      */ 
/*  301 */     this.targetNamespace = webService.targetNamespace();
/*  302 */     this.packageName = "";
/*  303 */     if (clazz.getPackage() != null)
/*  304 */       this.packageName = clazz.getPackage().getName();
/*  305 */     if (this.targetNamespace.length() == 0) {
/*  306 */       this.targetNamespace = getNamespace(this.packageName);
/*      */     }
/*  308 */     this.model.setTargetNamespace(this.targetNamespace);
/*  309 */     QName portTypeName = new QName(this.targetNamespace, portTypeLocalName);
/*  310 */     this.model.setPortTypeName(portTypeName);
/*  311 */     this.model.setWSDLLocation(webService.wsdlLocation());
/*      */ 
/*  313 */     SOAPBinding soapBinding = (SOAPBinding)getPrivClassAnnotation(clazz, SOAPBinding.class);
/*  314 */     if (soapBinding != null) {
/*  315 */       if ((soapBinding.style() == SOAPBinding.Style.RPC) && (soapBinding.parameterStyle() == SOAPBinding.ParameterStyle.BARE)) {
/*  316 */         throw new RuntimeModelerException("runtime.modeler.invalid.soapbinding.parameterstyle", new Object[] { soapBinding, clazz });
/*      */       }
/*      */ 
/*  320 */       this.isWrapped = (soapBinding.parameterStyle() == SOAPBinding.ParameterStyle.WRAPPED);
/*      */     }
/*  322 */     this.defaultBinding = createBinding(soapBinding);
/*      */ 
/*  342 */     for (Method method : clazz.getMethods()) {
/*  343 */       if ((clazz.isInterface()) || 
/*  344 */         (isWebMethodBySpec(method, clazz)))
/*      */       {
/*  350 */         processMethod(method);
/*      */       }
/*      */     }
/*  353 */     XmlSeeAlso xmlSeeAlso = (XmlSeeAlso)getPrivClassAnnotation(clazz, XmlSeeAlso.class);
/*  354 */     if (xmlSeeAlso != null)
/*  355 */       this.model.addAdditionalClasses(xmlSeeAlso.value());
/*      */   }
/*      */ 
/*      */   private boolean isWebMethodBySpec(Method method, Class clazz)
/*      */   {
/*  371 */     int modifiers = method.getModifiers();
/*  372 */     boolean staticFinal = (Modifier.isStatic(modifiers)) || (Modifier.isFinal(modifiers));
/*      */ 
/*  374 */     assert (Modifier.isPublic(modifiers));
/*  375 */     assert (!clazz.isInterface());
/*      */ 
/*  377 */     WebMethod webMethod = (WebMethod)getPrivMethodAnnotation(method, WebMethod.class);
/*  378 */     if (webMethod != null) {
/*  379 */       if (webMethod.exclude()) {
/*  380 */         return false;
/*      */       }
/*  382 */       if (staticFinal) {
/*  383 */         throw new RuntimeModelerException(ModelerMessages.localizableRUNTIME_MODELER_WEBMETHOD_MUST_BE_NONSTATICFINAL(method));
/*      */       }
/*  385 */       return true;
/*      */     }
/*      */ 
/*  388 */     if (staticFinal) {
/*  389 */       return false;
/*      */     }
/*      */ 
/*  392 */     Class declClass = method.getDeclaringClass();
/*  393 */     return getPrivClassAnnotation(declClass, WebService.class) != null;
/*      */   }
/*      */ 
/*      */   protected SOAPBindingImpl createBinding(SOAPBinding soapBinding)
/*      */   {
/*  402 */     SOAPBindingImpl rtSOAPBinding = new SOAPBindingImpl();
/*  403 */     SOAPBinding.Style style = soapBinding != null ? soapBinding.style() : SOAPBinding.Style.DOCUMENT;
/*  404 */     rtSOAPBinding.setStyle(style);
/*  405 */     assert (this.bindingId != null);
/*  406 */     SOAPVersion soapVersion = this.bindingId.getSOAPVersion();
/*  407 */     rtSOAPBinding.setSOAPVersion(soapVersion);
/*  408 */     return rtSOAPBinding;
/*      */   }
/*      */ 
/*      */   public static String getNamespace(@NotNull String packageName)
/*      */   {
/*  418 */     if (packageName.length() == 0) {
/*  419 */       return null;
/*      */     }
/*  421 */     StringTokenizer tokenizer = new StringTokenizer(packageName, ".");
/*      */     String[] tokens;
/*      */     String[] tokens;
/*  423 */     if (tokenizer.countTokens() == 0) {
/*  424 */       tokens = new String[0];
/*      */     } else {
/*  426 */       tokens = new String[tokenizer.countTokens()];
/*  427 */       for (int i = tokenizer.countTokens() - 1; i >= 0; i--) {
/*  428 */         tokens[i] = tokenizer.nextToken();
/*      */       }
/*      */     }
/*  431 */     StringBuilder namespace = new StringBuilder("http://");
/*  432 */     for (int i = 0; i < tokens.length; i++) {
/*  433 */       if (i != 0)
/*  434 */         namespace.append('.');
/*  435 */       namespace.append(tokens[i]);
/*      */     }
/*  437 */     namespace.append('/');
/*  438 */     return namespace.toString();
/*      */   }
/*      */ 
/*      */   private boolean isServiceException(Class<?> exception)
/*      */   {
/*  447 */     return (EXCEPTION_CLASS.isAssignableFrom(exception)) && (!RUNTIME_EXCEPTION_CLASS.isAssignableFrom(exception)) && (!REMOTE_EXCEPTION_CLASS.isAssignableFrom(exception));
/*      */   }
/*      */ 
/*      */   protected void processMethod(Method method)
/*      */   {
/*  456 */     int mods = method.getModifiers();
/*  457 */     if ((!Modifier.isPublic(mods)) || (Modifier.isStatic(mods))) {
/*  458 */       if (method.getAnnotation(WebMethod.class) != null)
/*      */       {
/*  461 */         if (Modifier.isStatic(mods)) {
/*  462 */           throw new RuntimeModelerException(ModelerMessages.localizableRUNTIME_MODELER_WEBMETHOD_MUST_BE_NONSTATIC(method));
/*      */         }
/*  464 */         throw new RuntimeModelerException(ModelerMessages.localizableRUNTIME_MODELER_WEBMETHOD_MUST_BE_PUBLIC(method));
/*      */       }
/*  466 */       return;
/*      */     }
/*      */ 
/*  469 */     WebMethod webMethod = (WebMethod)getPrivMethodAnnotation(method, WebMethod.class);
/*  470 */     if ((webMethod != null) && (webMethod.exclude())) {
/*  471 */       return;
/*      */     }
/*  473 */     String methodName = method.getName();
/*  474 */     boolean isOneway = method.isAnnotationPresent(Oneway.class);
/*      */ 
/*  477 */     if (isOneway)
/*  478 */       for (Class exception : method.getExceptionTypes())
/*  479 */         if (isServiceException(exception))
/*  480 */           throw new RuntimeModelerException("runtime.modeler.oneway.operation.no.checked.exceptions", new Object[] { this.portClass.getCanonicalName(), methodName, exception.getName() });
/*      */     JavaMethodImpl javaMethod;
/*      */     JavaMethodImpl javaMethod;
/*  488 */     if (method.getDeclaringClass() == this.portClass)
/*  489 */       javaMethod = new JavaMethodImpl(this.model, method, method);
/*      */     else {
/*      */       try {
/*  492 */         Method tmpMethod = this.portClass.getMethod(method.getName(), method.getParameterTypes());
/*      */ 
/*  494 */         javaMethod = new JavaMethodImpl(this.model, tmpMethod, method);
/*      */       } catch (NoSuchMethodException e) {
/*  496 */         throw new RuntimeModelerException("runtime.modeler.method.not.found", new Object[] { method.getName(), this.portClass.getName() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  504 */     MEP mep = getMEP(method);
/*  505 */     javaMethod.setMEP(mep);
/*      */ 
/*  507 */     String action = null;
/*      */ 
/*  510 */     String operationName = method.getName();
/*  511 */     if (webMethod != null) {
/*  512 */       action = webMethod.action();
/*  513 */       operationName = webMethod.operationName().length() > 0 ? webMethod.operationName() : operationName;
/*      */     }
/*      */ 
/*  519 */     if (this.binding != null) {
/*  520 */       WSDLBoundOperationImpl bo = this.binding.getBinding().get(new QName(this.targetNamespace, operationName));
/*  521 */       if (bo != null) {
/*  522 */         WSDLInputImpl wsdlInput = bo.getOperation().getInput();
/*  523 */         String wsaAction = wsdlInput.getAction();
/*  524 */         if ((wsaAction != null) && (!wsdlInput.isDefaultAction()))
/*  525 */           action = wsaAction;
/*      */         else {
/*  527 */           action = bo.getSOAPAction();
/*      */         }
/*      */       }
/*      */     }
/*  531 */     javaMethod.setOperationName(operationName);
/*  532 */     SOAPBinding methodBinding = (SOAPBinding)method.getAnnotation(SOAPBinding.class);
/*      */ 
/*  534 */     if ((methodBinding != null) && (methodBinding.style() == SOAPBinding.Style.RPC)) {
/*  535 */       logger.warning(ModelerMessages.RUNTIMEMODELER_INVALID_SOAPBINDING_ON_METHOD(methodBinding, method.getName(), method.getDeclaringClass().getName()));
/*  536 */     } else if ((methodBinding == null) && (!method.getDeclaringClass().equals(this.portClass))) {
/*  537 */       methodBinding = (SOAPBinding)method.getDeclaringClass().getAnnotation(SOAPBinding.class);
/*  538 */       if ((methodBinding != null) && (methodBinding.style() == SOAPBinding.Style.RPC) && (methodBinding.parameterStyle() == SOAPBinding.ParameterStyle.BARE)) {
/*  539 */         throw new RuntimeModelerException("runtime.modeler.invalid.soapbinding.parameterstyle", new Object[] { methodBinding, method.getDeclaringClass() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  544 */     if ((methodBinding != null) && (this.defaultBinding.getStyle() != methodBinding.style())) {
/*  545 */       throw new RuntimeModelerException("runtime.modeler.soapbinding.conflict", new Object[] { methodBinding.style(), method.getName(), this.defaultBinding.getStyle() });
/*      */     }
/*      */ 
/*  549 */     boolean methodIsWrapped = this.isWrapped;
/*  550 */     SOAPBinding.Style style = this.defaultBinding.getStyle();
/*  551 */     if (methodBinding != null) {
/*  552 */       SOAPBindingImpl mySOAPBinding = createBinding(methodBinding);
/*  553 */       style = mySOAPBinding.getStyle();
/*  554 */       if (action != null)
/*  555 */         mySOAPBinding.setSOAPAction(action);
/*  556 */       methodIsWrapped = methodBinding.parameterStyle().equals(SOAPBinding.ParameterStyle.WRAPPED);
/*      */ 
/*  558 */       javaMethod.setBinding(mySOAPBinding);
/*      */     } else {
/*  560 */       SOAPBindingImpl sb = new SOAPBindingImpl(this.defaultBinding);
/*  561 */       if (action != null)
/*  562 */         sb.setSOAPAction(action);
/*      */       else
/*  564 */         sb.setSOAPAction("");
/*  565 */       javaMethod.setBinding(sb);
/*      */     }
/*  567 */     if (!methodIsWrapped)
/*  568 */       processDocBareMethod(javaMethod, operationName, method);
/*  569 */     else if (style.equals(SOAPBinding.Style.DOCUMENT)) {
/*  570 */       processDocWrappedMethod(javaMethod, methodName, operationName, method);
/*      */     }
/*      */     else {
/*  573 */       processRpcMethod(javaMethod, methodName, operationName, method);
/*      */     }
/*  575 */     this.model.addJavaMethod(javaMethod);
/*      */   }
/*      */ 
/*      */   private MEP getMEP(Method m) {
/*  579 */     if (m.isAnnotationPresent(Oneway.class)) {
/*  580 */       return MEP.ONE_WAY;
/*      */     }
/*  582 */     if (Response.class.isAssignableFrom(m.getReturnType()))
/*  583 */       return MEP.ASYNC_POLL;
/*  584 */     if (Future.class.isAssignableFrom(m.getReturnType())) {
/*  585 */       return MEP.ASYNC_CALLBACK;
/*      */     }
/*  587 */     return MEP.REQUEST_RESPONSE;
/*      */   }
/*      */ 
/*      */   protected void processDocWrappedMethod(JavaMethodImpl javaMethod, String methodName, String operationName, Method method)
/*      */   {
/*  599 */     boolean methodHasHeaderParams = false;
/*  600 */     boolean isOneway = method.isAnnotationPresent(Oneway.class);
/*  601 */     RequestWrapper reqWrapper = (RequestWrapper)method.getAnnotation(RequestWrapper.class);
/*  602 */     ResponseWrapper resWrapper = (ResponseWrapper)method.getAnnotation(ResponseWrapper.class);
/*  603 */     String beanPackage = this.packageName + ".jaxws.";
/*  604 */     if ((this.packageName == null) || ((this.packageName != null) && (this.packageName.length() == 0)))
/*  605 */       beanPackage = "jaxws.";
/*      */     String requestClassName;
/*      */     String requestClassName;
/*  607 */     if ((reqWrapper != null) && (reqWrapper.className().length() > 0))
/*  608 */       requestClassName = reqWrapper.className();
/*      */     else
/*  610 */       requestClassName = beanPackage + capitalize(method.getName());
/*      */     String responseClassName;
/*      */     String responseClassName;
/*  615 */     if ((resWrapper != null) && (resWrapper.className().length() > 0))
/*  616 */       responseClassName = resWrapper.className();
/*      */     else {
/*  618 */       responseClassName = beanPackage + capitalize(method.getName()) + "Response";
/*      */     }
/*      */ 
/*  621 */     String reqName = operationName;
/*  622 */     String reqNamespace = this.targetNamespace;
/*  623 */     String reqPartName = "parameters";
/*  624 */     if (reqWrapper != null) {
/*  625 */       if (reqWrapper.targetNamespace().length() > 0)
/*  626 */         reqNamespace = reqWrapper.targetNamespace();
/*  627 */       if (reqWrapper.localName().length() > 0)
/*  628 */         reqName = reqWrapper.localName();
/*      */       try {
/*  630 */         if (reqWrapper.partName().length() > 0)
/*  631 */           reqPartName = reqWrapper.partName();
/*      */       }
/*      */       catch (LinkageError e)
/*      */       {
/*      */       }
/*      */     }
/*  637 */     QName reqElementName = new QName(reqNamespace, reqName);
/*  638 */     Class requestClass = getRequestWrapperClass(requestClassName, method, reqElementName);
/*      */ 
/*  640 */     Class responseClass = null;
/*  641 */     String resName = operationName + "Response";
/*  642 */     String resNamespace = this.targetNamespace;
/*  643 */     QName resElementName = null;
/*  644 */     String resPartName = "parameters";
/*  645 */     if (!isOneway) {
/*  646 */       if (resWrapper != null) {
/*  647 */         if (resWrapper.targetNamespace().length() > 0)
/*  648 */           resNamespace = resWrapper.targetNamespace();
/*  649 */         if (resWrapper.localName().length() > 0)
/*  650 */           resName = resWrapper.localName();
/*      */         try {
/*  652 */           if (resWrapper.partName().length() > 0)
/*  653 */             resPartName = resWrapper.partName();
/*      */         }
/*      */         catch (LinkageError e)
/*      */         {
/*      */         }
/*      */       }
/*  659 */       resElementName = new QName(resNamespace, resName);
/*  660 */       responseClass = getResponseWrapperClass(responseClassName, method, resElementName);
/*      */     }
/*      */ 
/*  663 */     TypeReference typeRef = new TypeReference(reqElementName, requestClass, new Annotation[0]);
/*      */ 
/*  665 */     WrapperParameter requestWrapper = new WrapperParameter(javaMethod, typeRef, WebParam.Mode.IN, 0);
/*      */ 
/*  667 */     requestWrapper.setPartName(reqPartName);
/*  668 */     requestWrapper.setBinding(ParameterBinding.BODY);
/*  669 */     javaMethod.addParameter(requestWrapper);
/*  670 */     WrapperParameter responseWrapper = null;
/*  671 */     if (!isOneway) {
/*  672 */       typeRef = new TypeReference(resElementName, responseClass, new Annotation[0]);
/*  673 */       responseWrapper = new WrapperParameter(javaMethod, typeRef, WebParam.Mode.OUT, -1);
/*  674 */       javaMethod.addParameter(responseWrapper);
/*  675 */       responseWrapper.setBinding(ParameterBinding.BODY);
/*      */     }
/*      */ 
/*  681 */     WebResult webResult = (WebResult)method.getAnnotation(WebResult.class);
/*  682 */     XmlElement xmlElem = (XmlElement)method.getAnnotation(XmlElement.class);
/*  683 */     QName resultQName = getReturnQName(method, webResult, xmlElem);
/*  684 */     Class returnType = method.getReturnType();
/*  685 */     boolean isResultHeader = false;
/*  686 */     if (webResult != null) {
/*  687 */       isResultHeader = webResult.header();
/*  688 */       methodHasHeaderParams = (isResultHeader) || (methodHasHeaderParams);
/*  689 */       if ((isResultHeader) && (xmlElem != null)) {
/*  690 */         throw new RuntimeModelerException("@XmlElement cannot be specified on method " + method + " as the return value is bound to header", new Object[0]);
/*      */       }
/*  692 */       if ((resultQName.getNamespaceURI().length() == 0) && (webResult.header()))
/*      */       {
/*  694 */         resultQName = new QName(this.targetNamespace, resultQName.getLocalPart());
/*      */       }
/*      */     }
/*      */ 
/*  698 */     if (javaMethod.isAsync()) {
/*  699 */       returnType = getAsyncReturnType(method, returnType);
/*  700 */       resultQName = new QName("return");
/*      */     }
/*      */ 
/*  703 */     if ((!isOneway) && (returnType != null) && (!returnType.getName().equals("void"))) {
/*  704 */       Annotation[] rann = method.getAnnotations();
/*  705 */       if (resultQName.getLocalPart() != null) {
/*  706 */         TypeReference rTypeReference = new TypeReference(resultQName, returnType, rann);
/*  707 */         ParameterImpl returnParameter = new ParameterImpl(javaMethod, rTypeReference, WebParam.Mode.OUT, -1);
/*  708 */         if (isResultHeader) {
/*  709 */           returnParameter.setBinding(ParameterBinding.HEADER);
/*  710 */           javaMethod.addParameter(returnParameter);
/*      */         } else {
/*  712 */           returnParameter.setBinding(ParameterBinding.BODY);
/*  713 */           responseWrapper.addWrapperChild(returnParameter);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  719 */     Class[] parameterTypes = method.getParameterTypes();
/*  720 */     Type[] genericParameterTypes = method.getGenericParameterTypes();
/*  721 */     Annotation[][] pannotations = getPrivParameterAnnotations(method);
/*  722 */     int pos = 0;
/*  723 */     for (Class clazzType : parameterTypes) {
/*  724 */       String partName = null;
/*  725 */       String paramName = "arg" + pos;
/*      */ 
/*  727 */       boolean isHeader = false;
/*      */ 
/*  729 */       if ((!javaMethod.isAsync()) || (!AsyncHandler.class.isAssignableFrom(clazzType)))
/*      */       {
/*  733 */         boolean isHolder = HOLDER_CLASS.isAssignableFrom(clazzType);
/*      */ 
/*  735 */         if ((isHolder) && 
/*  736 */           (clazzType == Holder.class)) {
/*  737 */           clazzType = (Class)Utils.REFLECTION_NAVIGATOR.erasure(((java.lang.reflect.ParameterizedType)genericParameterTypes[pos]).getActualTypeArguments()[0]);
/*      */         }
/*      */ 
/*  740 */         WebParam.Mode paramMode = isHolder ? WebParam.Mode.INOUT : WebParam.Mode.IN;
/*  741 */         WebParam webParam = null;
/*  742 */         xmlElem = null;
/*  743 */         for (Annotation annotation : pannotations[pos]) {
/*  744 */           if (annotation.annotationType() == WebParam.class)
/*  745 */             webParam = (WebParam)annotation;
/*  746 */           else if (annotation.annotationType() == XmlElement.class) {
/*  747 */             xmlElem = (XmlElement)annotation;
/*      */           }
/*      */         }
/*  750 */         QName paramQName = getParameterQName(method, webParam, xmlElem, paramName);
/*  751 */         if (webParam != null) {
/*  752 */           isHeader = webParam.header();
/*  753 */           methodHasHeaderParams = (isHeader) || (methodHasHeaderParams);
/*  754 */           if ((isHeader) && (xmlElem != null)) {
/*  755 */             throw new RuntimeModelerException("@XmlElement cannot be specified on method " + method + " parameter that is bound to header", new Object[0]);
/*      */           }
/*  757 */           if (webParam.partName().length() > 0)
/*  758 */             partName = webParam.partName();
/*      */           else
/*  760 */             partName = paramQName.getLocalPart();
/*  761 */           if ((isHeader) && (paramQName.getNamespaceURI().equals(""))) {
/*  762 */             paramQName = new QName(this.targetNamespace, paramQName.getLocalPart());
/*      */           }
/*  764 */           paramMode = webParam.mode();
/*  765 */           if ((isHolder) && (paramMode == WebParam.Mode.IN))
/*  766 */             paramMode = WebParam.Mode.INOUT;
/*      */         }
/*  768 */         typeRef = new TypeReference(paramQName, clazzType, pannotations[pos]);
/*      */ 
/*  770 */         ParameterImpl param = new ParameterImpl(javaMethod, typeRef, paramMode, pos++);
/*      */ 
/*  772 */         if (isHeader) {
/*  773 */           param.setBinding(ParameterBinding.HEADER);
/*  774 */           javaMethod.addParameter(param);
/*  775 */           param.setPartName(partName);
/*      */         } else {
/*  777 */           param.setBinding(ParameterBinding.BODY);
/*  778 */           if (paramMode != WebParam.Mode.OUT) {
/*  779 */             requestWrapper.addWrapperChild(param);
/*      */           }
/*  781 */           if (paramMode != WebParam.Mode.IN) {
/*  782 */             if (isOneway) {
/*  783 */               throw new RuntimeModelerException("runtime.modeler.oneway.operation.no.out.parameters", new Object[] { this.portClass.getCanonicalName(), methodName });
/*      */             }
/*      */ 
/*  786 */             responseWrapper.addWrapperChild(param);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  794 */     if (methodHasHeaderParams) {
/*  795 */       resPartName = "result";
/*      */     }
/*  797 */     if (responseWrapper != null)
/*  798 */       responseWrapper.setPartName(resPartName);
/*  799 */     processExceptions(javaMethod, method);
/*      */   }
/*      */ 
/*      */   protected void processRpcMethod(JavaMethodImpl javaMethod, String methodName, String operationName, Method method)
/*      */   {
/*  812 */     boolean isOneway = method.isAnnotationPresent(Oneway.class);
/*      */ 
/*  819 */     Map resRpcParams = new TreeMap();
/*  820 */     Map reqRpcParams = new TreeMap();
/*      */ 
/*  823 */     String reqNamespace = this.targetNamespace;
/*  824 */     String respNamespace = this.targetNamespace;
/*      */ 
/*  826 */     if ((this.binding != null) && (this.binding.getBinding().isRpcLit())) {
/*  827 */       QName opQName = new QName(this.binding.getBinding().getPortTypeName().getNamespaceURI(), operationName);
/*  828 */       WSDLBoundOperationImpl op = this.binding.getBinding().get(opQName);
/*  829 */       if (op != null)
/*      */       {
/*  831 */         if (op.getRequestNamespace() != null) {
/*  832 */           reqNamespace = op.getRequestNamespace();
/*      */         }
/*      */ 
/*  836 */         if (op.getResponseNamespace() != null) {
/*  837 */           respNamespace = op.getResponseNamespace();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  842 */     QName reqElementName = new QName(reqNamespace, operationName);
/*  843 */     QName resElementName = null;
/*  844 */     if (!isOneway) {
/*  845 */       resElementName = new QName(respNamespace, operationName + "Response");
/*      */     }
/*      */ 
/*  848 */     Class wrapperType = CompositeStructure.class;
/*  849 */     TypeReference typeRef = new TypeReference(reqElementName, wrapperType, new Annotation[0]);
/*  850 */     WrapperParameter requestWrapper = new WrapperParameter(javaMethod, typeRef, WebParam.Mode.IN, 0);
/*  851 */     requestWrapper.setInBinding(ParameterBinding.BODY);
/*  852 */     javaMethod.addParameter(requestWrapper);
/*  853 */     WrapperParameter responseWrapper = null;
/*  854 */     if (!isOneway) {
/*  855 */       typeRef = new TypeReference(resElementName, wrapperType, new Annotation[0]);
/*  856 */       responseWrapper = new WrapperParameter(javaMethod, typeRef, WebParam.Mode.OUT, -1);
/*  857 */       responseWrapper.setOutBinding(ParameterBinding.BODY);
/*  858 */       javaMethod.addParameter(responseWrapper);
/*      */     }
/*      */ 
/*  861 */     Class returnType = method.getReturnType();
/*  862 */     String resultName = "return";
/*  863 */     String resultTNS = this.targetNamespace;
/*  864 */     String resultPartName = resultName;
/*  865 */     boolean isResultHeader = false;
/*  866 */     WebResult webResult = (WebResult)method.getAnnotation(WebResult.class);
/*      */ 
/*  868 */     if (webResult != null) {
/*  869 */       isResultHeader = webResult.header();
/*  870 */       if (webResult.name().length() > 0)
/*  871 */         resultName = webResult.name();
/*  872 */       if (webResult.partName().length() > 0) {
/*  873 */         resultPartName = webResult.partName();
/*  874 */         if (!isResultHeader)
/*  875 */           resultName = resultPartName;
/*      */       } else {
/*  877 */         resultPartName = resultName;
/*  878 */       }if (webResult.targetNamespace().length() > 0)
/*  879 */         resultTNS = webResult.targetNamespace();
/*  880 */       isResultHeader = webResult.header();
/*      */     }
/*      */     QName resultQName;
/*      */     QName resultQName;
/*  883 */     if (isResultHeader)
/*  884 */       resultQName = new QName(resultTNS, resultName);
/*      */     else {
/*  886 */       resultQName = new QName(resultName);
/*      */     }
/*  888 */     if (javaMethod.isAsync()) {
/*  889 */       returnType = getAsyncReturnType(method, returnType);
/*      */     }
/*      */ 
/*  892 */     if ((!isOneway) && (returnType != null) && (returnType != Void.TYPE)) {
/*  893 */       Annotation[] rann = method.getAnnotations();
/*  894 */       TypeReference rTypeReference = new TypeReference(resultQName, returnType, rann);
/*  895 */       ParameterImpl returnParameter = new ParameterImpl(javaMethod, rTypeReference, WebParam.Mode.OUT, -1);
/*  896 */       returnParameter.setPartName(resultPartName);
/*  897 */       if (isResultHeader) {
/*  898 */         returnParameter.setBinding(ParameterBinding.HEADER);
/*  899 */         javaMethod.addParameter(returnParameter);
/*      */       } else {
/*  901 */         ParameterBinding rb = getBinding(operationName, resultPartName, false, WebParam.Mode.OUT);
/*  902 */         returnParameter.setBinding(rb);
/*  903 */         if (rb.isBody()) {
/*  904 */           WSDLPart p = getPart(new QName(this.targetNamespace, operationName), resultPartName, WebParam.Mode.OUT);
/*  905 */           if (p == null)
/*  906 */             resRpcParams.put(Integer.valueOf(resRpcParams.size() + 10000), returnParameter);
/*      */           else
/*  908 */             resRpcParams.put(Integer.valueOf(p.getIndex()), returnParameter);
/*      */         } else {
/*  910 */           javaMethod.addParameter(returnParameter);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  916 */     Class[] parameterTypes = method.getParameterTypes();
/*  917 */     Type[] genericParameterTypes = method.getGenericParameterTypes();
/*  918 */     Annotation[][] pannotations = getPrivParameterAnnotations(method);
/*  919 */     int pos = 0;
/*  920 */     for (Class clazzType : parameterTypes) {
/*  921 */       String paramName = "";
/*  922 */       String paramNamespace = "";
/*  923 */       String partName = "";
/*  924 */       boolean isHeader = false;
/*      */ 
/*  926 */       if ((!javaMethod.isAsync()) || (!AsyncHandler.class.isAssignableFrom(clazzType)))
/*      */       {
/*  930 */         boolean isHolder = HOLDER_CLASS.isAssignableFrom(clazzType);
/*      */ 
/*  932 */         if ((isHolder) && 
/*  933 */           (clazzType == Holder.class)) {
/*  934 */           clazzType = (Class)Utils.REFLECTION_NAVIGATOR.erasure(((java.lang.reflect.ParameterizedType)genericParameterTypes[pos]).getActualTypeArguments()[0]);
/*      */         }
/*  936 */         WebParam.Mode paramMode = isHolder ? WebParam.Mode.INOUT : WebParam.Mode.IN;
/*  937 */         for (Annotation annotation : pannotations[pos]) {
/*  938 */           if (annotation.annotationType() == WebParam.class) {
/*  939 */             WebParam webParam = (WebParam)annotation;
/*  940 */             paramName = webParam.name();
/*  941 */             partName = webParam.partName();
/*  942 */             isHeader = webParam.header();
/*  943 */             WebParam.Mode mode = webParam.mode();
/*  944 */             paramNamespace = webParam.targetNamespace();
/*  945 */             if ((isHolder) && (mode == WebParam.Mode.IN))
/*  946 */               mode = WebParam.Mode.INOUT;
/*  947 */             paramMode = mode;
/*  948 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  952 */         if (paramName.length() == 0) {
/*  953 */           paramName = "arg" + pos;
/*      */         }
/*  955 */         if (partName.length() == 0)
/*  956 */           partName = paramName;
/*  957 */         else if (!isHeader) {
/*  958 */           paramName = partName;
/*      */         }
/*  960 */         if (partName.length() == 0)
/*  961 */           partName = paramName;
/*      */         QName paramQName;
/*      */         QName paramQName;
/*  965 */         if (!isHeader)
/*      */         {
/*  967 */           paramQName = new QName("", paramName);
/*      */         } else {
/*  969 */           if (paramNamespace.length() == 0)
/*  970 */             paramNamespace = this.targetNamespace;
/*  971 */           paramQName = new QName(paramNamespace, paramName);
/*      */         }
/*  973 */         typeRef = new TypeReference(paramQName, clazzType, pannotations[pos]);
/*      */ 
/*  976 */         ParameterImpl param = new ParameterImpl(javaMethod, typeRef, paramMode, pos++);
/*  977 */         param.setPartName(partName);
/*      */ 
/*  979 */         if (paramMode == WebParam.Mode.INOUT) {
/*  980 */           ParameterBinding pb = getBinding(operationName, partName, isHeader, WebParam.Mode.IN);
/*  981 */           param.setInBinding(pb);
/*  982 */           pb = getBinding(operationName, partName, isHeader, WebParam.Mode.OUT);
/*  983 */           param.setOutBinding(pb);
/*      */         }
/*  985 */         else if (isHeader) {
/*  986 */           param.setBinding(ParameterBinding.HEADER);
/*      */         } else {
/*  988 */           ParameterBinding pb = getBinding(operationName, partName, false, paramMode);
/*  989 */           param.setBinding(pb);
/*      */         }
/*      */ 
/*  992 */         if (param.getInBinding().isBody()) {
/*  993 */           if (!param.isOUT()) {
/*  994 */             WSDLPart p = getPart(new QName(this.targetNamespace, operationName), partName, WebParam.Mode.IN);
/*  995 */             if (p == null)
/*  996 */               reqRpcParams.put(Integer.valueOf(reqRpcParams.size() + 10000), param);
/*      */             else {
/*  998 */               reqRpcParams.put(Integer.valueOf(p.getIndex()), param);
/*      */             }
/*      */           }
/* 1001 */           if (!param.isIN()) {
/* 1002 */             if (isOneway) {
/* 1003 */               throw new RuntimeModelerException("runtime.modeler.oneway.operation.no.out.parameters", new Object[] { this.portClass.getCanonicalName(), methodName });
/*      */             }
/*      */ 
/* 1006 */             WSDLPart p = getPart(new QName(this.targetNamespace, operationName), partName, WebParam.Mode.OUT);
/* 1007 */             if (p == null)
/* 1008 */               resRpcParams.put(Integer.valueOf(resRpcParams.size() + 10000), param);
/*      */             else
/* 1010 */               resRpcParams.put(Integer.valueOf(p.getIndex()), param);
/*      */           }
/*      */         } else {
/* 1013 */           javaMethod.addParameter(param);
/*      */         }
/*      */       }
/*      */     }
/* 1016 */     for (ParameterImpl p : reqRpcParams.values())
/* 1017 */       requestWrapper.addWrapperChild(p);
/* 1018 */     for (ParameterImpl p : resRpcParams.values())
/* 1019 */       responseWrapper.addWrapperChild(p);
/* 1020 */     processExceptions(javaMethod, method);
/*      */   }
/*      */ 
/*      */   protected void processExceptions(JavaMethodImpl javaMethod, Method method)
/*      */   {
/* 1030 */     Action actionAnn = (Action)method.getAnnotation(Action.class);
/* 1031 */     FaultAction[] faultActions = new FaultAction[0];
/* 1032 */     if (actionAnn != null)
/* 1033 */       faultActions = actionAnn.fault();
/* 1034 */     for (Class exception : method.getExceptionTypes())
/*      */     {
/* 1037 */       if (EXCEPTION_CLASS.isAssignableFrom(exception))
/*      */       {
/* 1039 */         if ((!RUNTIME_EXCEPTION_CLASS.isAssignableFrom(exception)) && (!REMOTE_EXCEPTION_CLASS.isAssignableFrom(exception)))
/*      */         {
/* 1044 */           WebFault webFault = (WebFault)getPrivClassAnnotation(exception, WebFault.class);
/* 1045 */           Method faultInfoMethod = getWSDLExceptionFaultInfo(exception);
/* 1046 */           ExceptionType exceptionType = ExceptionType.WSDLException;
/* 1047 */           String namespace = this.targetNamespace;
/* 1048 */           String name = exception.getSimpleName();
/* 1049 */           String beanPackage = this.packageName + ".jaxws.";
/* 1050 */           if (this.packageName.length() == 0)
/* 1051 */             beanPackage = "jaxws.";
/* 1052 */           String className = beanPackage + name + "Bean";
/* 1053 */           String messageName = exception.getSimpleName();
/* 1054 */           if (webFault != null) {
/* 1055 */             if (webFault.faultBean().length() > 0)
/* 1056 */               className = webFault.faultBean();
/* 1057 */             if (webFault.name().length() > 0)
/* 1058 */               name = webFault.name();
/* 1059 */             if (webFault.targetNamespace().length() > 0)
/* 1060 */               namespace = webFault.targetNamespace();
/* 1061 */             if (webFault.messageName().length() > 0)
/* 1062 */               messageName = webFault.messageName();
/*      */           }
/*      */           Annotation[] anns;
/*      */           Class exceptionBean;
/*      */           Annotation[] anns;
/* 1064 */           if (faultInfoMethod == null) {
/* 1065 */             Class exceptionBean = getExceptionBeanClass(className, exception, name, namespace);
/* 1066 */             exceptionType = ExceptionType.UserDefined;
/* 1067 */             anns = exceptionBean.getAnnotations();
/*      */           } else {
/* 1069 */             exceptionBean = faultInfoMethod.getReturnType();
/* 1070 */             anns = faultInfoMethod.getAnnotations();
/*      */           }
/* 1072 */           QName faultName = new QName(namespace, name);
/* 1073 */           TypeReference typeRef = new TypeReference(faultName, exceptionBean, anns);
/* 1074 */           CheckedExceptionImpl checkedException = new CheckedExceptionImpl(javaMethod, exception, typeRef, exceptionType);
/*      */ 
/* 1076 */           checkedException.setMessageName(messageName);
/* 1077 */           for (FaultAction fa : faultActions) {
/* 1078 */             if ((fa.className().equals(exception)) && (!fa.value().equals(""))) {
/* 1079 */               checkedException.setFaultAction(fa.value());
/* 1080 */               break;
/*      */             }
/*      */           }
/* 1083 */           javaMethod.addException(checkedException);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Method getWSDLExceptionFaultInfo(Class exception)
/*      */   {
/* 1095 */     if (!exception.isAnnotationPresent(WebFault.class))
/* 1096 */       return null;
/*      */     try {
/* 1098 */       return exception.getMethod("getFaultInfo", new Class[0]); } catch (NoSuchMethodException e) {
/*      */     }
/* 1100 */     return null;
/*      */   }
/*      */ 
/*      */   protected void processDocBareMethod(JavaMethodImpl javaMethod, String operationName, Method method)
/*      */   {
/* 1113 */     String resultName = operationName + "Response";
/* 1114 */     String resultTNS = this.targetNamespace;
/* 1115 */     String resultPartName = null;
/* 1116 */     boolean isResultHeader = false;
/* 1117 */     WebResult webResult = (WebResult)method.getAnnotation(WebResult.class);
/* 1118 */     if (webResult != null) {
/* 1119 */       if (webResult.name().length() > 0)
/* 1120 */         resultName = webResult.name();
/* 1121 */       if (webResult.targetNamespace().length() > 0)
/* 1122 */         resultTNS = webResult.targetNamespace();
/* 1123 */       resultPartName = webResult.partName();
/* 1124 */       isResultHeader = webResult.header();
/*      */     }
/*      */ 
/* 1127 */     Class returnType = method.getReturnType();
/*      */ 
/* 1129 */     if (javaMethod.isAsync()) {
/* 1130 */       returnType = getAsyncReturnType(method, returnType);
/*      */     }
/*      */ 
/* 1133 */     if ((returnType != null) && (!returnType.getName().equals("void"))) {
/* 1134 */       Annotation[] rann = method.getAnnotations();
/* 1135 */       if (resultName != null) {
/* 1136 */         QName responseQName = new QName(resultTNS, resultName);
/* 1137 */         TypeReference rTypeReference = new TypeReference(responseQName, returnType, rann);
/* 1138 */         ParameterImpl returnParameter = new ParameterImpl(javaMethod, rTypeReference, WebParam.Mode.OUT, -1);
/*      */ 
/* 1140 */         if ((resultPartName == null) || (resultPartName.length() == 0)) {
/* 1141 */           resultPartName = resultName;
/*      */         }
/* 1143 */         returnParameter.setPartName(resultPartName);
/* 1144 */         if (isResultHeader) {
/* 1145 */           returnParameter.setBinding(ParameterBinding.HEADER);
/*      */         } else {
/* 1147 */           ParameterBinding rb = getBinding(operationName, resultPartName, false, WebParam.Mode.OUT);
/* 1148 */           returnParameter.setBinding(rb);
/*      */         }
/* 1150 */         javaMethod.addParameter(returnParameter);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1155 */     Class[] parameterTypes = method.getParameterTypes();
/* 1156 */     Type[] genericParameterTypes = method.getGenericParameterTypes();
/* 1157 */     Annotation[][] pannotations = getPrivParameterAnnotations(method);
/* 1158 */     int pos = 0;
/* 1159 */     for (Class clazzType : parameterTypes) {
/* 1160 */       String paramName = operationName;
/* 1161 */       String partName = null;
/* 1162 */       String requestNamespace = this.targetNamespace;
/* 1163 */       boolean isHeader = false;
/*      */ 
/* 1166 */       if ((!javaMethod.isAsync()) || (!AsyncHandler.class.isAssignableFrom(clazzType)))
/*      */       {
/* 1170 */         boolean isHolder = HOLDER_CLASS.isAssignableFrom(clazzType);
/*      */ 
/* 1172 */         if ((isHolder) && 
/* 1173 */           (clazzType == Holder.class)) {
/* 1174 */           clazzType = (Class)Utils.REFLECTION_NAVIGATOR.erasure(((java.lang.reflect.ParameterizedType)genericParameterTypes[pos]).getActualTypeArguments()[0]);
/*      */         }
/*      */ 
/* 1177 */         WebParam.Mode paramMode = isHolder ? WebParam.Mode.INOUT : WebParam.Mode.IN;
/* 1178 */         for (Annotation annotation : pannotations[pos]) {
/* 1179 */           if (annotation.annotationType() == WebParam.class) {
/* 1180 */             WebParam webParam = (WebParam)annotation;
/* 1181 */             paramMode = webParam.mode();
/* 1182 */             if ((isHolder) && (paramMode == WebParam.Mode.IN))
/* 1183 */               paramMode = WebParam.Mode.INOUT;
/* 1184 */             isHeader = webParam.header();
/* 1185 */             if (isHeader)
/* 1186 */               paramName = "arg" + pos;
/* 1187 */             if ((paramMode == WebParam.Mode.OUT) && (!isHeader))
/* 1188 */               paramName = operationName + "Response";
/* 1189 */             if (webParam.name().length() > 0)
/* 1190 */               paramName = webParam.name();
/* 1191 */             partName = webParam.partName();
/* 1192 */             if (webParam.targetNamespace().equals("")) break;
/* 1193 */             requestNamespace = webParam.targetNamespace(); break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1199 */         QName requestQName = new QName(requestNamespace, paramName);
/*      */ 
/* 1201 */         TypeReference typeRef = new TypeReference(requestQName, clazzType, pannotations[pos]);
/*      */ 
/* 1205 */         ParameterImpl param = new ParameterImpl(javaMethod, typeRef, paramMode, pos++);
/* 1206 */         if ((partName == null) || (partName.length() == 0)) {
/* 1207 */           partName = paramName;
/*      */         }
/* 1209 */         param.setPartName(partName);
/* 1210 */         if (paramMode == WebParam.Mode.INOUT) {
/* 1211 */           ParameterBinding pb = getBinding(operationName, partName, isHeader, WebParam.Mode.IN);
/* 1212 */           param.setInBinding(pb);
/* 1213 */           pb = getBinding(operationName, partName, isHeader, WebParam.Mode.OUT);
/* 1214 */           param.setOutBinding(pb);
/*      */         }
/* 1216 */         else if (isHeader) {
/* 1217 */           param.setBinding(ParameterBinding.HEADER);
/*      */         } else {
/* 1219 */           ParameterBinding pb = getBinding(operationName, partName, false, paramMode);
/* 1220 */           param.setBinding(pb);
/*      */         }
/*      */ 
/* 1223 */         javaMethod.addParameter(param);
/*      */       }
/*      */     }
/* 1225 */     validateDocBare(javaMethod);
/* 1226 */     processExceptions(javaMethod, method);
/*      */   }
/*      */ 
/*      */   private void validateDocBare(JavaMethodImpl javaMethod)
/*      */   {
/* 1236 */     int numInBodyBindings = 0;
/* 1237 */     for (Parameter param : javaMethod.getRequestParameters()) {
/* 1238 */       if ((param.getBinding().equals(ParameterBinding.BODY)) && (param.isIN())) {
/* 1239 */         numInBodyBindings++;
/*      */       }
/* 1241 */       if (numInBodyBindings > 1) {
/* 1242 */         throw new RuntimeModelerException(ModelerMessages.localizableNOT_A_VALID_BARE_METHOD(this.portClass.getName(), javaMethod.getMethod().getName()));
/*      */       }
/*      */     }
/*      */ 
/* 1246 */     int numOutBodyBindings = 0;
/* 1247 */     for (Parameter param : javaMethod.getResponseParameters()) {
/* 1248 */       if ((param.getBinding().equals(ParameterBinding.BODY)) && (param.isOUT())) {
/* 1249 */         numOutBodyBindings++;
/*      */       }
/* 1251 */       if (numOutBodyBindings > 1)
/* 1252 */         throw new RuntimeModelerException(ModelerMessages.localizableNOT_A_VALID_BARE_METHOD(this.portClass.getName(), javaMethod.getMethod().getName()));
/*      */     }
/*      */   }
/*      */ 
/*      */   private Class getAsyncReturnType(Method method, Class returnType)
/*      */   {
/* 1258 */     if (Response.class.isAssignableFrom(returnType)) {
/* 1259 */       Type ret = method.getGenericReturnType();
/* 1260 */       return (Class)Utils.REFLECTION_NAVIGATOR.erasure(((java.lang.reflect.ParameterizedType)ret).getActualTypeArguments()[0]);
/*      */     }
/* 1262 */     Type[] types = method.getGenericParameterTypes();
/* 1263 */     Class[] params = method.getParameterTypes();
/* 1264 */     int i = 0;
/* 1265 */     for (Class cls : params) {
/* 1266 */       if (AsyncHandler.class.isAssignableFrom(cls)) {
/* 1267 */         return (Class)Utils.REFLECTION_NAVIGATOR.erasure(((java.lang.reflect.ParameterizedType)types[i]).getActualTypeArguments()[0]);
/*      */       }
/* 1269 */       i++;
/*      */     }
/*      */ 
/* 1272 */     return returnType;
/*      */   }
/*      */ 
/*      */   public static String capitalize(String name)
/*      */   {
/* 1281 */     if ((name == null) || (name.length() == 0)) {
/* 1282 */       return name;
/*      */     }
/* 1284 */     char[] chars = name.toCharArray();
/* 1285 */     chars[0] = Character.toUpperCase(chars[0]);
/* 1286 */     return new String(chars);
/*      */   }
/*      */ 
/*      */   public static QName getServiceName(Class<?> implClass)
/*      */   {
/* 1298 */     if (implClass.isInterface()) {
/* 1299 */       throw new RuntimeModelerException("runtime.modeler.cannot.get.serviceName.from.interface", new Object[] { implClass.getCanonicalName() });
/*      */     }
/*      */ 
/* 1303 */     String name = implClass.getSimpleName() + "Service";
/* 1304 */     String packageName = "";
/* 1305 */     if (implClass.getPackage() != null) {
/* 1306 */       packageName = implClass.getPackage().getName();
/*      */     }
/* 1308 */     WebService webService = (WebService)implClass.getAnnotation(WebService.class);
/* 1309 */     if (webService == null) {
/* 1310 */       throw new RuntimeModelerException("runtime.modeler.no.webservice.annotation", new Object[] { implClass.getCanonicalName() });
/*      */     }
/*      */ 
/* 1313 */     if (webService.serviceName().length() > 0) {
/* 1314 */       name = webService.serviceName();
/*      */     }
/* 1316 */     String targetNamespace = getNamespace(packageName);
/* 1317 */     if (webService.targetNamespace().length() > 0)
/* 1318 */       targetNamespace = webService.targetNamespace();
/* 1319 */     else if (targetNamespace == null) {
/* 1320 */       throw new RuntimeModelerException("runtime.modeler.no.package", new Object[] { implClass.getName() });
/*      */     }
/*      */ 
/* 1326 */     return new QName(targetNamespace, name);
/*      */   }
/*      */ 
/*      */   public static QName getPortName(Class<?> implClass, String targetNamespace)
/*      */   {
/* 1336 */     WebService webService = (WebService)implClass.getAnnotation(WebService.class);
/* 1337 */     if (webService == null)
/* 1338 */       throw new RuntimeModelerException("runtime.modeler.no.webservice.annotation", new Object[] { implClass.getCanonicalName() });
/*      */     String name;
/*      */     String name;
/* 1342 */     if (webService.portName().length() > 0) {
/* 1343 */       name = webService.portName();
/*      */     }
/*      */     else
/*      */     {
/*      */       String name;
/* 1344 */       if (webService.name().length() > 0)
/* 1345 */         name = webService.name() + "Port";
/*      */       else {
/* 1347 */         name = implClass.getSimpleName() + "Port";
/*      */       }
/*      */     }
/* 1350 */     if (targetNamespace == null) {
/* 1351 */       if (webService.targetNamespace().length() > 0) {
/* 1352 */         targetNamespace = webService.targetNamespace();
/*      */       } else {
/* 1354 */         String packageName = null;
/* 1355 */         if (implClass.getPackage() != null) {
/* 1356 */           packageName = implClass.getPackage().getName();
/*      */         }
/* 1358 */         targetNamespace = getNamespace(packageName);
/* 1359 */         if (targetNamespace == null) {
/* 1360 */           throw new RuntimeModelerException("runtime.modeler.no.package", new Object[] { implClass.getName() });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1367 */     return new QName(targetNamespace, name);
/*      */   }
/*      */ 
/*      */   public static QName getPortTypeName(Class<?> implOrSeiClass)
/*      */   {
/* 1376 */     assert (implOrSeiClass != null);
/* 1377 */     Class clazz = implOrSeiClass;
/* 1378 */     if (!implOrSeiClass.isAnnotationPresent(WebService.class)) {
/* 1379 */       throw new RuntimeModelerException("runtime.modeler.no.webservice.annotation", new Object[] { implOrSeiClass.getCanonicalName() });
/*      */     }
/*      */ 
/* 1382 */     if (!implOrSeiClass.isInterface()) {
/* 1383 */       WebService webService = (WebService)implOrSeiClass.getAnnotation(WebService.class);
/* 1384 */       String epi = webService.endpointInterface();
/* 1385 */       if (epi.length() > 0) {
/*      */         try {
/* 1387 */           clazz = Thread.currentThread().getContextClassLoader().loadClass(epi);
/*      */         } catch (ClassNotFoundException e) {
/* 1389 */           throw new RuntimeModelerException("runtime.modeler.class.not.found", new Object[] { epi });
/*      */         }
/* 1391 */         if (!clazz.isAnnotationPresent(WebService.class)) {
/* 1392 */           throw new RuntimeModelerException("runtime.modeler.endpoint.interface.no.webservice", new Object[] { webService.endpointInterface() });
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1398 */     WebService webService = (WebService)clazz.getAnnotation(WebService.class);
/* 1399 */     String name = webService.name();
/* 1400 */     if (name.length() == 0) {
/* 1401 */       name = clazz.getSimpleName();
/*      */     }
/*      */ 
/* 1404 */     String tns = webService.targetNamespace();
/* 1405 */     if (tns.length() == 0)
/* 1406 */       tns = getNamespace(clazz.getPackage().getName());
/* 1407 */     if (tns == null) {
/* 1408 */       throw new RuntimeModelerException("runtime.modeler.no.package", new Object[] { clazz.getName() });
/*      */     }
/* 1410 */     return new QName(tns, name);
/*      */   }
/*      */ 
/*      */   private ParameterBinding getBinding(String operation, String part, boolean isHeader, WebParam.Mode mode) {
/* 1414 */     if (this.binding == null) {
/* 1415 */       if (isHeader) {
/* 1416 */         return ParameterBinding.HEADER;
/*      */       }
/* 1418 */       return ParameterBinding.BODY;
/*      */     }
/* 1420 */     QName opName = new QName(this.binding.getBinding().getPortType().getName().getNamespaceURI(), operation);
/* 1421 */     return this.binding.getBinding().getBinding(opName, part, mode);
/*      */   }
/*      */ 
/*      */   private WSDLPart getPart(QName opName, String partName, WebParam.Mode mode) {
/* 1425 */     if (this.binding != null) {
/* 1426 */       WSDLBoundOperationImpl bo = this.binding.getBinding().get(opName);
/* 1427 */       if (bo != null)
/* 1428 */         return bo.getPart(partName, mode);
/*      */     }
/* 1430 */     return null;
/*      */   }
/*      */ 
/*      */   private static QName getReturnQName(Method method, WebResult webResult, XmlElement xmlElem) {
/* 1434 */     String webResultName = null;
/* 1435 */     if ((webResult != null) && (webResult.name().length() > 0)) {
/* 1436 */       webResultName = webResult.name();
/*      */     }
/* 1438 */     String xmlElemName = null;
/* 1439 */     if ((xmlElem != null) && (!xmlElem.name().equals("##default"))) {
/* 1440 */       xmlElemName = xmlElem.name();
/*      */     }
/* 1442 */     if ((xmlElemName != null) && (webResultName != null) && (!xmlElemName.equals(webResultName))) {
/* 1443 */       throw new RuntimeModelerException("@XmlElement(name)=" + xmlElemName + " and @WebResult(name)=" + webResultName + " are different for method " + method, new Object[0]);
/*      */     }
/* 1445 */     String localPart = "return";
/* 1446 */     if (webResultName != null)
/* 1447 */       localPart = webResultName;
/* 1448 */     else if (xmlElemName != null) {
/* 1449 */       localPart = xmlElemName;
/*      */     }
/*      */ 
/* 1452 */     String webResultNS = null;
/* 1453 */     if ((webResult != null) && (webResult.targetNamespace().length() > 0)) {
/* 1454 */       webResultNS = webResult.targetNamespace();
/*      */     }
/* 1456 */     String xmlElemNS = null;
/* 1457 */     if ((xmlElem != null) && (!xmlElem.namespace().equals("##default"))) {
/* 1458 */       xmlElemNS = xmlElem.namespace();
/*      */     }
/* 1460 */     if ((xmlElemNS != null) && (webResultNS != null) && (!xmlElemNS.equals(webResultNS))) {
/* 1461 */       throw new RuntimeModelerException("@XmlElement(namespace)=" + xmlElemNS + " and @WebResult(targetNamespace)=" + webResultNS + " are different for method " + method, new Object[0]);
/*      */     }
/* 1463 */     String ns = "";
/* 1464 */     if (webResultNS != null)
/* 1465 */       ns = webResultNS;
/* 1466 */     else if (xmlElemNS != null) {
/* 1467 */       ns = xmlElemNS;
/*      */     }
/*      */ 
/* 1470 */     return new QName(ns, localPart);
/*      */   }
/*      */ 
/*      */   private static QName getParameterQName(Method method, WebParam webParam, XmlElement xmlElem, String paramDefault) {
/* 1474 */     String webParamName = null;
/* 1475 */     if ((webParam != null) && (webParam.name().length() > 0)) {
/* 1476 */       webParamName = webParam.name();
/*      */     }
/* 1478 */     String xmlElemName = null;
/* 1479 */     if ((xmlElem != null) && (!xmlElem.name().equals("##default"))) {
/* 1480 */       xmlElemName = xmlElem.name();
/*      */     }
/* 1482 */     if ((xmlElemName != null) && (webParamName != null) && (!xmlElemName.equals(webParamName))) {
/* 1483 */       throw new RuntimeModelerException("@XmlElement(name)=" + xmlElemName + " and @WebParam(name)=" + webParamName + " are different for method " + method, new Object[0]);
/*      */     }
/* 1485 */     String localPart = paramDefault;
/* 1486 */     if (webParamName != null)
/* 1487 */       localPart = webParamName;
/* 1488 */     else if (xmlElemName != null) {
/* 1489 */       localPart = xmlElemName;
/*      */     }
/*      */ 
/* 1492 */     String webParamNS = null;
/* 1493 */     if ((webParam != null) && (webParam.targetNamespace().length() > 0)) {
/* 1494 */       webParamNS = webParam.targetNamespace();
/*      */     }
/* 1496 */     String xmlElemNS = null;
/* 1497 */     if ((xmlElem != null) && (!xmlElem.namespace().equals("##default"))) {
/* 1498 */       xmlElemNS = xmlElem.namespace();
/*      */     }
/* 1500 */     if ((xmlElemNS != null) && (webParamNS != null) && (!xmlElemNS.equals(webParamNS))) {
/* 1501 */       throw new RuntimeModelerException("@XmlElement(namespace)=" + xmlElemNS + " and @WebParam(targetNamespace)=" + webParamNS + " are different for method " + method, new Object[0]);
/*      */     }
/* 1503 */     String ns = "";
/* 1504 */     if (webParamNS != null)
/* 1505 */       ns = webParamNS;
/* 1506 */     else if (xmlElemNS != null) {
/* 1507 */       ns = xmlElemNS;
/*      */     }
/*      */ 
/* 1510 */     return new QName(ns, localPart);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.RuntimeModeler
 * JD-Core Version:    0.6.2
 */