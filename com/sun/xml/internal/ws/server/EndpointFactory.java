/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.WSFeatureList;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver.ServerContext;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
/*     */ import com.sun.xml.internal.ws.api.server.AsyncProvider;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.ContainerResolver;
/*     */ import com.sun.xml.internal.ws.api.server.InstanceResolver;
/*     */ import com.sun.xml.internal.ws.api.server.Invoker;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument.WSDL;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocumentSource;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver.Parser;
/*     */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
/*     */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*     */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*     */ import com.sun.xml.internal.ws.model.RuntimeModeler;
/*     */ import com.sun.xml.internal.ws.model.SOAPSEIModel;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLModelImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLServiceImpl;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMapMutator;
/*     */ import com.sun.xml.internal.ws.policy.jaxws.PolicyUtil;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import com.sun.xml.internal.ws.server.provider.ProviderInvokerTube;
/*     */ import com.sun.xml.internal.ws.server.sei.SEIInvokerTube;
/*     */ import com.sun.xml.internal.ws.util.HandlerAnnotationInfo;
/*     */ import com.sun.xml.internal.ws.util.HandlerAnnotationProcessor;
/*     */ import com.sun.xml.internal.ws.util.ServiceConfigurationError;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser;
/*     */ import com.sun.xml.internal.ws.wsdl.writer.WSDLGenerator;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.ws.Provider;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceProvider;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class EndpointFactory
/*     */ {
/* 570 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server.endpoint");
/*     */ 
/*     */   public static <T> WSEndpoint<T> createEndpoint(Class<T> implType, boolean processHandlerAnnotation, @Nullable Invoker invoker, @Nullable QName serviceName, @Nullable QName portName, @Nullable Container container, @Nullable WSBinding binding, @Nullable SDDocumentSource primaryWsdl, @Nullable Collection<? extends SDDocumentSource> metadata, EntityResolver resolver, boolean isTransportSynchronous)
/*     */   {
/* 106 */     if (implType == null) {
/* 107 */       throw new IllegalArgumentException();
/*     */     }
/* 109 */     verifyImplementorClass(implType);
/*     */ 
/* 111 */     if (invoker == null) {
/* 112 */       invoker = InstanceResolver.createDefault(implType).createInvoker();
/*     */     }
/*     */ 
/* 115 */     List md = new ArrayList();
/* 116 */     if (metadata != null) {
/* 117 */       md.addAll(metadata);
/*     */     }
/* 119 */     if ((primaryWsdl != null) && (!md.contains(primaryWsdl))) {
/* 120 */       md.add(primaryWsdl);
/*     */     }
/* 122 */     if (container == null) {
/* 123 */       container = ContainerResolver.getInstance().getContainer();
/*     */     }
/* 125 */     if (serviceName == null) {
/* 126 */       serviceName = getDefaultServiceName(implType);
/*     */     }
/* 128 */     if (portName == null) {
/* 129 */       portName = getDefaultPortName(serviceName, implType);
/*     */     }
/*     */ 
/* 132 */     String serviceNS = serviceName.getNamespaceURI();
/* 133 */     String portNS = portName.getNamespaceURI();
/* 134 */     if (!serviceNS.equals(portNS)) {
/* 135 */       throw new ServerRtException("wrong.tns.for.port", new Object[] { portNS, serviceNS });
/*     */     }
/*     */ 
/* 140 */     if (binding == null) {
/* 141 */       binding = BindingImpl.create(BindingID.parse(implType));
/*     */     }
/* 143 */     if (primaryWsdl != null) {
/* 144 */       verifyPrimaryWSDL(primaryWsdl, serviceName);
/*     */     }
/*     */ 
/* 147 */     QName portTypeName = null;
/* 148 */     if (implType.getAnnotation(WebServiceProvider.class) == null) {
/* 149 */       portTypeName = RuntimeModeler.getPortTypeName(implType);
/*     */     }
/*     */ 
/* 153 */     List docList = categoriseMetadata(md, serviceName, portTypeName);
/*     */ 
/* 156 */     SDDocumentImpl primaryDoc = findPrimary(docList);
/*     */ 
/* 159 */     WSDLPortImpl wsdlPort = null;
/* 160 */     AbstractSEIModelImpl seiModel = null;
/*     */ 
/* 162 */     if (primaryDoc != null) {
/* 163 */       wsdlPort = getWSDLPort(primaryDoc, docList, serviceName, portName, container);
/*     */     }
/*     */ 
/* 166 */     WebServiceFeatureList features = ((BindingImpl)binding).getFeatures();
/* 167 */     features.parseAnnotations(implType);
/* 168 */     PolicyMap policyMap = null;
/*     */     InvokerTube terminal;
/*     */     InvokerTube terminal;
/* 170 */     if (implType.getAnnotation(WebServiceProvider.class) != null)
/*     */     {
/*     */       Iterable configFtrs;
/*     */       Iterable configFtrs;
/* 176 */       if (wsdlPort != null) {
/* 177 */         policyMap = wsdlPort.getOwner().getParent().getPolicyMap();
/*     */ 
/* 179 */         configFtrs = wsdlPort.getFeatures();
/*     */       }
/*     */       else {
/* 182 */         policyMap = PolicyResolverFactory.create().resolve(new PolicyResolver.ServerContext(null, container, implType, false, new PolicyMapMutator[0]));
/*     */ 
/* 184 */         configFtrs = PolicyUtil.getPortScopedFeatures(policyMap, serviceName, portName);
/*     */       }
/* 186 */       features.mergeFeatures(configFtrs, true);
/* 187 */       terminal = ProviderInvokerTube.create(implType, binding, invoker);
/*     */     }
/*     */     else {
/* 190 */       seiModel = createSEIModel(wsdlPort, implType, serviceName, portName, binding);
/* 191 */       if ((binding instanceof SOAPBindingImpl))
/*     */       {
/* 193 */         ((SOAPBindingImpl)binding).setPortKnownHeaders(((SOAPSEIModel)seiModel).getKnownHeaders());
/*     */       }
/*     */ 
/* 197 */       if (primaryDoc == null) {
/* 198 */         primaryDoc = generateWSDL(binding, seiModel, docList, container, implType);
/*     */ 
/* 200 */         wsdlPort = getWSDLPort(primaryDoc, docList, serviceName, portName, container);
/* 201 */         seiModel.freeze(wsdlPort);
/*     */       }
/* 203 */       policyMap = wsdlPort.getOwner().getParent().getPolicyMap();
/*     */ 
/* 207 */       features.mergeFeatures(wsdlPort.getFeatures(), true);
/* 208 */       terminal = new SEIInvokerTube(seiModel, invoker, binding);
/*     */     }
/*     */ 
/* 212 */     if (processHandlerAnnotation) {
/* 213 */       processHandlerAnnotation(binding, implType, serviceName, portName);
/*     */     }
/*     */ 
/* 216 */     if (primaryDoc != null) {
/* 217 */       docList = findMetadataClosure(primaryDoc, docList);
/*     */     }
/* 219 */     ServiceDefinitionImpl serviceDefiniton = primaryDoc != null ? new ServiceDefinitionImpl(docList, primaryDoc) : null;
/*     */ 
/* 221 */     return new WSEndpointImpl(serviceName, portName, binding, container, seiModel, wsdlPort, implType, serviceDefiniton, terminal, isTransportSynchronous, policyMap);
/*     */   }
/*     */ 
/*     */   private static List<SDDocumentImpl> findMetadataClosure(SDDocumentImpl primaryDoc, List<SDDocumentImpl> docList)
/*     */   {
/* 236 */     Map oldMap = new HashMap();
/* 237 */     for (SDDocumentImpl doc : docList) {
/* 238 */       oldMap.put(doc.getSystemId().toString(), doc);
/*     */     }
/*     */ 
/* 241 */     Map newMap = new HashMap();
/* 242 */     newMap.put(primaryDoc.getSystemId().toString(), primaryDoc);
/*     */ 
/* 244 */     List remaining = new ArrayList();
/* 245 */     remaining.addAll(primaryDoc.getImports());
/* 246 */     while (!remaining.isEmpty()) {
/* 247 */       String url = (String)remaining.remove(0);
/* 248 */       SDDocumentImpl doc = (SDDocumentImpl)oldMap.get(url);
/* 249 */       if (doc != null)
/*     */       {
/* 254 */         if (!newMap.containsKey(url)) {
/* 255 */           newMap.put(url, doc);
/* 256 */           remaining.addAll(doc.getImports());
/*     */         }
/*     */       }
/*     */     }
/* 259 */     List newMetadata = new ArrayList();
/* 260 */     newMetadata.addAll(newMap.values());
/* 261 */     return newMetadata;
/*     */   }
/*     */ 
/*     */   private static <T> void processHandlerAnnotation(WSBinding binding, Class<T> implType, QName serviceName, QName portName) {
/* 265 */     HandlerAnnotationInfo chainInfo = HandlerAnnotationProcessor.buildHandlerInfo(implType, serviceName, portName, binding);
/*     */ 
/* 268 */     if (chainInfo != null) {
/* 269 */       binding.setHandlerChain(chainInfo.getHandlers());
/* 270 */       if ((binding instanceof SOAPBinding))
/* 271 */         ((SOAPBinding)binding).setRoles(chainInfo.getRoles());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean verifyImplementorClass(Class<?> clz)
/*     */   {
/* 289 */     WebServiceProvider wsProvider = (WebServiceProvider)clz.getAnnotation(WebServiceProvider.class);
/* 290 */     WebService ws = (WebService)clz.getAnnotation(WebService.class);
/* 291 */     if ((wsProvider == null) && (ws == null)) {
/* 292 */       throw new IllegalArgumentException(clz + " has neither @WebService nor @WebServiceProvider annotation");
/*     */     }
/* 294 */     if ((wsProvider != null) && (ws != null)) {
/* 295 */       throw new IllegalArgumentException(clz + " has both @WebService and @WebServiceProvider annotations");
/*     */     }
/* 297 */     if (wsProvider != null) {
/* 298 */       if ((Provider.class.isAssignableFrom(clz)) || (AsyncProvider.class.isAssignableFrom(clz))) {
/* 299 */         return true;
/*     */       }
/* 301 */       throw new IllegalArgumentException(clz + " doesn't implement Provider or AsyncProvider interface");
/*     */     }
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */   private static AbstractSEIModelImpl createSEIModel(WSDLPort wsdlPort, Class<?> implType, @NotNull QName serviceName, @NotNull QName portName, WSBinding binding)
/*     */   {
/*     */     RuntimeModeler rap;
/*     */     RuntimeModeler rap;
/* 315 */     if (wsdlPort == null) {
/* 316 */       rap = new RuntimeModeler(implType, serviceName, binding.getBindingId(), binding.getFeatures().toArray());
/*     */     }
/*     */     else
/*     */     {
/* 324 */       rap = new RuntimeModeler(implType, serviceName, (WSDLPortImpl)wsdlPort, binding.getFeatures().toArray());
/*     */     }
/* 326 */     rap.setClassLoader(implType.getClassLoader());
/* 327 */     rap.setPortName(portName);
/* 328 */     return rap.buildRuntimeModel();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static QName getDefaultServiceName(Class<?> implType)
/*     */   {
/* 356 */     WebServiceProvider wsProvider = (WebServiceProvider)implType.getAnnotation(WebServiceProvider.class);
/*     */     QName serviceName;
/*     */     QName serviceName;
/* 357 */     if (wsProvider != null) {
/* 358 */       String tns = wsProvider.targetNamespace();
/* 359 */       String local = wsProvider.serviceName();
/* 360 */       serviceName = new QName(tns, local);
/*     */     } else {
/* 362 */       serviceName = RuntimeModeler.getServiceName(implType);
/*     */     }
/* 364 */     assert (serviceName != null);
/* 365 */     return serviceName;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static QName getDefaultPortName(QName serviceName, Class<?> implType)
/*     */   {
/* 376 */     WebServiceProvider wsProvider = (WebServiceProvider)implType.getAnnotation(WebServiceProvider.class);
/*     */     QName portName;
/*     */     QName portName;
/* 377 */     if (wsProvider != null) {
/* 378 */       String tns = wsProvider.targetNamespace();
/* 379 */       String local = wsProvider.portName();
/* 380 */       portName = new QName(tns, local);
/*     */     } else {
/* 382 */       portName = RuntimeModeler.getPortName(implType, serviceName.getNamespaceURI());
/*     */     }
/* 384 */     assert (portName != null);
/* 385 */     return portName;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public static String getWsdlLocation(Class<?> implType)
/*     */   {
/* 399 */     WebService ws = (WebService)implType.getAnnotation(WebService.class);
/*     */     String wsdl;
/*     */     String wsdl;
/* 400 */     if (ws != null) {
/* 401 */       wsdl = ws.wsdlLocation();
/*     */     } else {
/* 403 */       WebServiceProvider wsProvider = (WebServiceProvider)implType.getAnnotation(WebServiceProvider.class);
/* 404 */       assert (wsProvider != null);
/* 405 */       wsdl = wsProvider.wsdlLocation();
/*     */     }
/* 407 */     if (wsdl.length() < 1) {
/* 408 */       wsdl = null;
/*     */     }
/* 410 */     return wsdl;
/*     */   }
/*     */ 
/*     */   private static SDDocumentImpl generateWSDL(WSBinding binding, AbstractSEIModelImpl seiModel, List<SDDocumentImpl> docs, Container container, Class implType)
/*     */   {
/* 419 */     BindingID bindingId = binding.getBindingId();
/* 420 */     if (!bindingId.canGenerateWSDL()) {
/* 421 */       throw new ServerRtException("can.not.generate.wsdl", new Object[] { bindingId });
/*     */     }
/*     */ 
/* 424 */     if (bindingId.toString().equals("http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")) {
/* 425 */       String msg = ServerMessages.GENERATE_NON_STANDARD_WSDL();
/* 426 */       logger.warning(msg);
/*     */     }
/*     */ 
/* 430 */     WSDLGenResolver wsdlResolver = new WSDLGenResolver(docs, seiModel.getServiceQName(), seiModel.getPortTypeName());
/* 431 */     WSDLGenerator wsdlGen = new WSDLGenerator(seiModel, wsdlResolver, binding, container, implType, false, (WSDLGeneratorExtension[])ServiceFinder.find(WSDLGeneratorExtension.class).toArray());
/*     */ 
/* 433 */     wsdlGen.doGeneration();
/* 434 */     return wsdlResolver.updateDocs();
/*     */   }
/*     */ 
/*     */   private static List<SDDocumentImpl> categoriseMetadata(List<SDDocumentSource> src, QName serviceName, QName portTypeName)
/*     */   {
/* 443 */     List r = new ArrayList(src.size());
/* 444 */     for (SDDocumentSource doc : src) {
/* 445 */       r.add(SDDocumentImpl.create(doc, serviceName, portTypeName));
/*     */     }
/* 447 */     return r;
/*     */   }
/*     */ 
/*     */   private static void verifyPrimaryWSDL(@NotNull SDDocumentSource primaryWsdl, @NotNull QName serviceName)
/*     */   {
/* 455 */     SDDocumentImpl primaryDoc = SDDocumentImpl.create(primaryWsdl, serviceName, null);
/* 456 */     if (!(primaryDoc instanceof SDDocument.WSDL)) {
/* 457 */       throw new WebServiceException(primaryWsdl.getSystemId() + " is not a WSDL. But it is passed as a primary WSDL");
/*     */     }
/*     */ 
/* 460 */     SDDocument.WSDL wsdlDoc = (SDDocument.WSDL)primaryDoc;
/* 461 */     if (!wsdlDoc.hasService()) {
/* 462 */       if (wsdlDoc.getAllServices().isEmpty()) {
/* 463 */         throw new WebServiceException("Not a primary WSDL=" + primaryWsdl.getSystemId() + " since it doesn't have Service " + serviceName);
/*     */       }
/*     */ 
/* 466 */       throw new WebServiceException("WSDL " + primaryDoc.getSystemId() + " has the following services " + wsdlDoc.getAllServices() + " but not " + serviceName + ". Maybe you forgot to specify a serviceName and/or targetNamespace in @WebService/@WebServiceProvider?");
/*     */     }
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   private static SDDocumentImpl findPrimary(@NotNull List<SDDocumentImpl> docList)
/*     */   {
/* 482 */     SDDocumentImpl primaryDoc = null;
/* 483 */     boolean foundConcrete = false;
/* 484 */     boolean foundAbstract = false;
/* 485 */     for (SDDocumentImpl doc : docList) {
/* 486 */       if ((doc instanceof SDDocument.WSDL)) {
/* 487 */         SDDocument.WSDL wsdlDoc = (SDDocument.WSDL)doc;
/* 488 */         if (wsdlDoc.hasService()) {
/* 489 */           primaryDoc = doc;
/* 490 */           if (foundConcrete) {
/* 491 */             throw new ServerRtException("duplicate.primary.wsdl", new Object[] { doc.getSystemId() });
/*     */           }
/* 493 */           foundConcrete = true;
/*     */         }
/* 495 */         if (wsdlDoc.hasPortType()) {
/* 496 */           if (foundAbstract) {
/* 497 */             throw new ServerRtException("duplicate.abstract.wsdl", new Object[] { doc.getSystemId() });
/*     */           }
/* 499 */           foundAbstract = true;
/*     */         }
/*     */       }
/*     */     }
/* 503 */     return primaryDoc; } 
/*     */   @NotNull
/*     */   private static WSDLPortImpl getWSDLPort(SDDocumentSource primaryWsdl, List<? extends SDDocumentSource> metadata, @NotNull QName serviceName, @NotNull QName portName, Container container) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 615	com/sun/xml/internal/ws/api/server/SDDocumentSource:getSystemId	()Ljava/net/URL;
/*     */     //   4: astore 5
/*     */     //   6: new 296	com/sun/xml/internal/ws/api/wsdl/parser/XMLEntityResolver$Parser
/*     */     //   9: dup
/*     */     //   10: aload_0
/*     */     //   11: invokespecial 616	com/sun/xml/internal/ws/api/wsdl/parser/XMLEntityResolver$Parser:<init>	(Lcom/sun/xml/internal/ws/api/server/SDDocumentSource;)V
/*     */     //   14: new 312	com/sun/xml/internal/ws/server/EndpointFactory$EntityResolverImpl
/*     */     //   17: dup
/*     */     //   18: aload_1
/*     */     //   19: invokespecial 657	com/sun/xml/internal/ws/server/EndpointFactory$EntityResolverImpl:<init>	(Ljava/util/List;)V
/*     */     //   22: iconst_0
/*     */     //   23: aload 4
/*     */     //   25: ldc_w 294
/*     */     //   28: invokestatic 673	com/sun/xml/internal/ws/util/ServiceFinder:find	(Ljava/lang/Class;)Lcom/sun/xml/internal/ws/util/ServiceFinder;
/*     */     //   31: invokevirtual 672	com/sun/xml/internal/ws/util/ServiceFinder:toArray	()[Ljava/lang/Object;
/*     */     //   34: checkcast 278	[Lcom/sun/xml/internal/ws/api/wsdl/parser/WSDLParserExtension;
/*     */     //   37: invokestatic 674	com/sun/xml/internal/ws/wsdl/parser/RuntimeWSDLParser:parse	(Lcom/sun/xml/internal/ws/api/wsdl/parser/XMLEntityResolver$Parser;Lcom/sun/xml/internal/ws/api/wsdl/parser/XMLEntityResolver;ZLcom/sun/xml/internal/ws/api/server/Container;[Lcom/sun/xml/internal/ws/api/wsdl/parser/WSDLParserExtension;)Lcom/sun/xml/internal/ws/model/wsdl/WSDLModelImpl;
/*     */     //   40: astore 6
/*     */     //   42: aload 6
/*     */     //   44: invokevirtual 635	com/sun/xml/internal/ws/model/wsdl/WSDLModelImpl:getServices	()Ljava/util/Map;
/*     */     //   47: invokeinterface 719 1 0
/*     */     //   52: ifne +16 -> 68
/*     */     //   55: new 315	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   58: dup
/*     */     //   59: aload 5
/*     */     //   61: invokestatic 643	com/sun/xml/internal/ws/resources/ServerMessages:localizableRUNTIME_PARSER_WSDL_NOSERVICE_IN_WSDLMODEL	(Ljava/lang/Object;)Lcom/sun/xml/internal/ws/util/localization/Localizable;
/*     */     //   64: invokespecial 661	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Lcom/sun/xml/internal/ws/util/localization/Localizable;)V
/*     */     //   67: athrow
/*     */     //   68: aload 6
/*     */     //   70: aload_2
/*     */     //   71: invokevirtual 636	com/sun/xml/internal/ws/model/wsdl/WSDLModelImpl:getService	(Ljavax/xml/namespace/QName;)Lcom/sun/xml/internal/ws/model/wsdl/WSDLServiceImpl;
/*     */     //   74: astore 7
/*     */     //   76: aload 7
/*     */     //   78: ifnonnull +17 -> 95
/*     */     //   81: new 315	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   84: dup
/*     */     //   85: aload_2
/*     */     //   86: aload 5
/*     */     //   88: invokestatic 644	com/sun/xml/internal/ws/resources/ServerMessages:localizableRUNTIME_PARSER_WSDL_INCORRECTSERVICE	(Ljava/lang/Object;Ljava/lang/Object;)Lcom/sun/xml/internal/ws/util/localization/Localizable;
/*     */     //   91: invokespecial 661	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Lcom/sun/xml/internal/ws/util/localization/Localizable;)V
/*     */     //   94: athrow
/*     */     //   95: aload 7
/*     */     //   97: aload_3
/*     */     //   98: invokevirtual 640	com/sun/xml/internal/ws/model/wsdl/WSDLServiceImpl:get	(Ljavax/xml/namespace/QName;)Lcom/sun/xml/internal/ws/model/wsdl/WSDLPortImpl;
/*     */     //   101: astore 8
/*     */     //   103: aload 8
/*     */     //   105: ifnonnull +18 -> 123
/*     */     //   108: new 315	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   111: dup
/*     */     //   112: aload_2
/*     */     //   113: aload_3
/*     */     //   114: aload 5
/*     */     //   116: invokestatic 645	com/sun/xml/internal/ws/resources/ServerMessages:localizableRUNTIME_PARSER_WSDL_INCORRECTSERVICEPORT	(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/sun/xml/internal/ws/util/localization/Localizable;
/*     */     //   119: invokespecial 661	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Lcom/sun/xml/internal/ws/util/localization/Localizable;)V
/*     */     //   122: athrow
/*     */     //   123: aload 8
/*     */     //   125: areturn
/*     */     //   126: astore 6
/*     */     //   128: new 315	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   131: dup
/*     */     //   132: ldc 16
/*     */     //   134: iconst_2
/*     */     //   135: anewarray 332	java/lang/Object
/*     */     //   138: dup
/*     */     //   139: iconst_0
/*     */     //   140: aload 5
/*     */     //   142: aastore
/*     */     //   143: dup
/*     */     //   144: iconst_1
/*     */     //   145: aload 6
/*     */     //   147: aastore
/*     */     //   148: invokespecial 662	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   151: athrow
/*     */     //   152: astore 6
/*     */     //   154: new 315	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   157: dup
/*     */     //   158: ldc 17
/*     */     //   160: iconst_3
/*     */     //   161: anewarray 332	java/lang/Object
/*     */     //   164: dup
/*     */     //   165: iconst_0
/*     */     //   166: aload 6
/*     */     //   168: invokevirtual 699	javax/xml/stream/XMLStreamException:getMessage	()Ljava/lang/String;
/*     */     //   171: aastore
/*     */     //   172: dup
/*     */     //   173: iconst_1
/*     */     //   174: aload 6
/*     */     //   176: invokevirtual 700	javax/xml/stream/XMLStreamException:getLocation	()Ljavax/xml/stream/Location;
/*     */     //   179: aastore
/*     */     //   180: dup
/*     */     //   181: iconst_2
/*     */     //   182: aload 6
/*     */     //   184: aastore
/*     */     //   185: invokespecial 662	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   188: athrow
/*     */     //   189: astore 6
/*     */     //   191: new 315	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   194: dup
/*     */     //   195: ldc 16
/*     */     //   197: iconst_2
/*     */     //   198: anewarray 332	java/lang/Object
/*     */     //   201: dup
/*     */     //   202: iconst_0
/*     */     //   203: aload 5
/*     */     //   205: aastore
/*     */     //   206: dup
/*     */     //   207: iconst_1
/*     */     //   208: aload 6
/*     */     //   210: aastore
/*     */     //   211: invokespecial 662	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   214: athrow
/*     */     //   215: astore 6
/*     */     //   217: new 315	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   220: dup
/*     */     //   221: ldc 16
/*     */     //   223: iconst_2
/*     */     //   224: anewarray 332	java/lang/Object
/*     */     //   227: dup
/*     */     //   228: iconst_0
/*     */     //   229: aload 5
/*     */     //   231: aastore
/*     */     //   232: dup
/*     */     //   233: iconst_1
/*     */     //   234: aload 6
/*     */     //   236: aastore
/*     */     //   237: invokespecial 662	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   240: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	125	126	java/io/IOException
/*     */     //   6	125	152	javax/xml/stream/XMLStreamException
/*     */     //   6	125	189	org/xml/sax/SAXException
/*     */     //   6	125	215	com/sun/xml/internal/ws/util/ServiceConfigurationError } 
/* 551 */   private static final class EntityResolverImpl implements XMLEntityResolver { private Map<String, SDDocumentSource> metadata = new HashMap();
/*     */ 
/*     */     public EntityResolverImpl(List<? extends SDDocumentSource> metadata) {
/* 554 */       for (SDDocumentSource doc : metadata)
/* 555 */         this.metadata.put(doc.getSystemId().toExternalForm(), doc);
/*     */     }
/*     */ 
/*     */     public XMLEntityResolver.Parser resolveEntity(String publicId, String systemId) throws IOException, XMLStreamException
/*     */     {
/* 560 */       if (systemId != null) {
/* 561 */         SDDocumentSource doc = (SDDocumentSource)this.metadata.get(systemId);
/* 562 */         if (doc != null)
/* 563 */           return new XMLEntityResolver.Parser(doc);
/*     */       }
/* 565 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.EndpointFactory
 * JD-Core Version:    0.6.2
 */