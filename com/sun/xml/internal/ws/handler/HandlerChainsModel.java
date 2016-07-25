/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
/*     */ import com.sun.xml.internal.ws.util.HandlerAnnotationInfo;
/*     */ import com.sun.xml.internal.ws.util.JAXWSUtils;
/*     */ import com.sun.xml.internal.ws.util.UtilException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import javax.xml.ws.handler.PortInfo;
/*     */ 
/*     */ public class HandlerChainsModel
/*     */ {
/*  52 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.util");
/*     */   private Class annotatedClass;
/*     */   private List<HandlerChainType> handlerChains;
/*     */   private String id;
/*     */   public static final String PROTOCOL_SOAP11_TOKEN = "##SOAP11_HTTP";
/*     */   public static final String PROTOCOL_SOAP12_TOKEN = "##SOAP12_HTTP";
/*     */   public static final String PROTOCOL_XML_TOKEN = "##XML_HTTP";
/*     */   public static final String NS_109 = "http://java.sun.com/xml/ns/javaee";
/* 429 */   public static final QName QNAME_CHAIN_PORT_PATTERN = new QName("http://java.sun.com/xml/ns/javaee", "port-name-pattern");
/*     */ 
/* 431 */   public static final QName QNAME_CHAIN_PROTOCOL_BINDING = new QName("http://java.sun.com/xml/ns/javaee", "protocol-bindings");
/*     */ 
/* 433 */   public static final QName QNAME_CHAIN_SERVICE_PATTERN = new QName("http://java.sun.com/xml/ns/javaee", "service-name-pattern");
/*     */ 
/* 435 */   public static final QName QNAME_HANDLER_CHAIN = new QName("http://java.sun.com/xml/ns/javaee", "handler-chain");
/*     */ 
/* 437 */   public static final QName QNAME_HANDLER_CHAINS = new QName("http://java.sun.com/xml/ns/javaee", "handler-chains");
/*     */ 
/* 439 */   public static final QName QNAME_HANDLER = new QName("http://java.sun.com/xml/ns/javaee", "handler");
/*     */ 
/* 441 */   public static final QName QNAME_HANDLER_NAME = new QName("http://java.sun.com/xml/ns/javaee", "handler-name");
/*     */ 
/* 443 */   public static final QName QNAME_HANDLER_CLASS = new QName("http://java.sun.com/xml/ns/javaee", "handler-class");
/*     */ 
/* 445 */   public static final QName QNAME_HANDLER_PARAM = new QName("http://java.sun.com/xml/ns/javaee", "init-param");
/*     */ 
/* 447 */   public static final QName QNAME_HANDLER_PARAM_NAME = new QName("http://java.sun.com/xml/ns/javaee", "param-name");
/*     */ 
/* 449 */   public static final QName QNAME_HANDLER_PARAM_VALUE = new QName("http://java.sun.com/xml/ns/javaee", "param-value");
/*     */ 
/* 451 */   public static final QName QNAME_HANDLER_HEADER = new QName("http://java.sun.com/xml/ns/javaee", "soap-header");
/*     */ 
/* 453 */   public static final QName QNAME_HANDLER_ROLE = new QName("http://java.sun.com/xml/ns/javaee", "soap-role");
/*     */ 
/*     */   private HandlerChainsModel(Class annotatedClass)
/*     */   {
/*  60 */     this.annotatedClass = annotatedClass;
/*     */   }
/*     */ 
/*     */   private List<HandlerChainType> getHandlerChain() {
/*  64 */     if (this.handlerChains == null) {
/*  65 */       this.handlerChains = new ArrayList();
/*     */     }
/*  67 */     return this.handlerChains;
/*     */   }
/*     */ 
/*     */   public String getId() {
/*  71 */     return this.id;
/*     */   }
/*     */ 
/*     */   public void setId(String value) {
/*  75 */     this.id = value;
/*     */   }
/*     */ 
/*     */   public static HandlerChainsModel parseHandlerConfigFile(Class annotatedClass, XMLStreamReader reader)
/*     */   {
/*  81 */     ensureProperName(reader, QNAME_HANDLER_CHAINS);
/*  82 */     HandlerChainsModel handlerModel = new HandlerChainsModel(annotatedClass);
/*  83 */     List hChains = handlerModel.getHandlerChain();
/*  84 */     XMLStreamReaderUtil.nextElementContent(reader);
/*     */ 
/*  86 */     while (reader.getName().equals(QNAME_HANDLER_CHAIN)) {
/*  87 */       HandlerChainType hChain = new HandlerChainType();
/*  88 */       XMLStreamReaderUtil.nextElementContent(reader);
/*     */ 
/*  90 */       if (reader.getName().equals(QNAME_CHAIN_PORT_PATTERN)) {
/*  91 */         QName portNamePattern = XMLStreamReaderUtil.getElementQName(reader);
/*  92 */         hChain.setPortNamePattern(portNamePattern);
/*  93 */         XMLStreamReaderUtil.nextElementContent(reader);
/*  94 */       } else if (reader.getName().equals(QNAME_CHAIN_PROTOCOL_BINDING)) {
/*  95 */         String bindingList = XMLStreamReaderUtil.getElementText(reader);
/*  96 */         StringTokenizer stk = new StringTokenizer(bindingList);
/*  97 */         while (stk.hasMoreTokens()) {
/*  98 */           String token = stk.nextToken();
/*     */ 
/* 100 */           hChain.addProtocolBinding(token);
/*     */         }
/* 102 */         XMLStreamReaderUtil.nextElementContent(reader);
/* 103 */       } else if (reader.getName().equals(QNAME_CHAIN_SERVICE_PATTERN)) {
/* 104 */         QName serviceNamepattern = XMLStreamReaderUtil.getElementQName(reader);
/* 105 */         hChain.setServiceNamePattern(serviceNamepattern);
/* 106 */         XMLStreamReaderUtil.nextElementContent(reader);
/*     */       }
/* 108 */       List handlers = hChain.getHandlers();
/*     */ 
/* 110 */       while (reader.getName().equals(QNAME_HANDLER)) {
/* 111 */         HandlerType handler = new HandlerType();
/*     */ 
/* 113 */         XMLStreamReaderUtil.nextContent(reader);
/* 114 */         if (reader.getName().equals(QNAME_HANDLER_NAME)) {
/* 115 */           String handlerName = XMLStreamReaderUtil.getElementText(reader).trim();
/*     */ 
/* 117 */           handler.setHandlerName(handlerName);
/* 118 */           XMLStreamReaderUtil.nextContent(reader);
/*     */         }
/*     */ 
/* 122 */         ensureProperName(reader, QNAME_HANDLER_CLASS);
/* 123 */         String handlerClass = XMLStreamReaderUtil.getElementText(reader).trim();
/*     */ 
/* 125 */         handler.setHandlerClass(handlerClass);
/* 126 */         XMLStreamReaderUtil.nextContent(reader);
/*     */ 
/* 129 */         while (reader.getName().equals(QNAME_HANDLER_PARAM)) {
/* 130 */           skipInitParamElement(reader);
/*     */         }
/*     */ 
/* 134 */         while (reader.getName().equals(QNAME_HANDLER_HEADER)) {
/* 135 */           skipTextElement(reader);
/*     */         }
/*     */ 
/* 139 */         while (reader.getName().equals(QNAME_HANDLER_ROLE)) {
/* 140 */           List soapRoles = handler.getSoapRoles();
/* 141 */           soapRoles.add(XMLStreamReaderUtil.getElementText(reader));
/* 142 */           XMLStreamReaderUtil.nextContent(reader);
/*     */         }
/*     */ 
/* 145 */         handlers.add(handler);
/*     */ 
/* 148 */         ensureProperName(reader, QNAME_HANDLER);
/* 149 */         XMLStreamReaderUtil.nextContent(reader);
/*     */       }
/*     */ 
/* 153 */       ensureProperName(reader, QNAME_HANDLER_CHAIN);
/* 154 */       hChains.add(hChain);
/* 155 */       XMLStreamReaderUtil.nextContent(reader);
/*     */     }
/*     */ 
/* 158 */     return handlerModel;
/*     */   }
/*     */ 
/*     */   public static HandlerAnnotationInfo parseHandlerFile(XMLStreamReader reader, ClassLoader classLoader, QName serviceName, QName portName, WSBinding wsbinding)
/*     */   {
/* 177 */     ensureProperName(reader, QNAME_HANDLER_CHAINS);
/* 178 */     String bindingId = wsbinding.getBindingId().toString();
/* 179 */     HandlerAnnotationInfo info = new HandlerAnnotationInfo();
/*     */ 
/* 181 */     XMLStreamReaderUtil.nextElementContent(reader);
/*     */ 
/* 183 */     List handlerChain = new ArrayList();
/* 184 */     Set roles = new HashSet();
/*     */ 
/* 186 */     while (reader.getName().equals(QNAME_HANDLER_CHAIN))
/*     */     {
/* 188 */       XMLStreamReaderUtil.nextElementContent(reader);
/*     */ 
/* 190 */       if (reader.getName().equals(QNAME_CHAIN_PORT_PATTERN)) {
/* 191 */         if (portName == null) {
/* 192 */           logger.warning("handler chain sepcified for port but port QName passed to parser is null");
/*     */         }
/*     */ 
/* 195 */         boolean parseChain = JAXWSUtils.matchQNames(portName, XMLStreamReaderUtil.getElementQName(reader));
/*     */ 
/* 197 */         if (!parseChain) {
/* 198 */           skipChain(reader);
/*     */         }
/*     */         else
/* 201 */           XMLStreamReaderUtil.nextElementContent(reader);
/* 202 */       } else if (reader.getName().equals(QNAME_CHAIN_PROTOCOL_BINDING)) {
/* 203 */         if (bindingId == null) {
/* 204 */           logger.warning("handler chain sepcified for bindingId but bindingId passed to parser is null");
/*     */         }
/*     */ 
/* 207 */         String bindingConstraint = XMLStreamReaderUtil.getElementText(reader);
/* 208 */         boolean skipThisChain = true;
/* 209 */         StringTokenizer stk = new StringTokenizer(bindingConstraint);
/* 210 */         List bindingList = new ArrayList();
/* 211 */         while (stk.hasMoreTokens()) {
/* 212 */           String tokenOrURI = stk.nextToken();
/*     */ 
/* 217 */           tokenOrURI = DeploymentDescriptorParser.getBindingIdForToken(tokenOrURI);
/* 218 */           String binding = BindingID.parse(tokenOrURI).toString();
/* 219 */           bindingList.add(binding);
/*     */         }
/* 221 */         if (bindingList.contains(bindingId)) {
/* 222 */           skipThisChain = false;
/*     */         }
/*     */ 
/* 225 */         if (skipThisChain) {
/* 226 */           skipChain(reader);
/*     */         }
/*     */         else
/* 229 */           XMLStreamReaderUtil.nextElementContent(reader);
/* 230 */       } else if (reader.getName().equals(QNAME_CHAIN_SERVICE_PATTERN)) {
/* 231 */         if (serviceName == null) {
/* 232 */           logger.warning("handler chain sepcified for service but service QName passed to parser is null");
/*     */         }
/*     */ 
/* 235 */         boolean parseChain = JAXWSUtils.matchQNames(serviceName, XMLStreamReaderUtil.getElementQName(reader));
/*     */ 
/* 238 */         if (!parseChain) {
/* 239 */           skipChain(reader);
/*     */         }
/*     */         else
/* 242 */           XMLStreamReaderUtil.nextElementContent(reader);
/*     */       }
/*     */       else
/*     */       {
/* 246 */         while (reader.getName().equals(QNAME_HANDLER))
/*     */         {
/* 249 */           XMLStreamReaderUtil.nextContent(reader);
/* 250 */           if (reader.getName().equals(QNAME_HANDLER_NAME)) {
/* 251 */             skipTextElement(reader);
/* 255 */           }
/*     */ ensureProperName(reader, QNAME_HANDLER_CLASS);
/*     */           Handler handler;
/*     */           try {
/* 257 */             handler = (Handler)loadClass(classLoader, XMLStreamReaderUtil.getElementText(reader).trim()).newInstance();
/*     */           }
/*     */           catch (InstantiationException ie) {
/* 260 */             throw new RuntimeException(ie);
/*     */           } catch (IllegalAccessException e) {
/* 262 */             throw new RuntimeException(e);
/*     */           }
/* 264 */           XMLStreamReaderUtil.nextContent(reader);
/*     */ 
/* 267 */           while (reader.getName().equals(QNAME_HANDLER_PARAM)) {
/* 268 */             skipInitParamElement(reader);
/*     */           }
/*     */ 
/* 272 */           while (reader.getName().equals(QNAME_HANDLER_HEADER)) {
/* 273 */             skipTextElement(reader);
/*     */           }
/*     */ 
/* 277 */           while (reader.getName().equals(QNAME_HANDLER_ROLE)) {
/* 278 */             roles.add(XMLStreamReaderUtil.getElementText(reader));
/* 279 */             XMLStreamReaderUtil.nextContent(reader);
/*     */           }
/*     */ 
/* 283 */           for (Method method : handler.getClass().getMethods()) {
/* 284 */             if (method.getAnnotation(PostConstruct.class) != null)
/*     */             {
/*     */               try
/*     */               {
/* 288 */                 method.invoke(handler, new Object[0]);
/*     */               }
/*     */               catch (Exception e) {
/* 291 */                 throw new RuntimeException(e);
/*     */               }
/*     */             }
/*     */           }
/* 295 */           handlerChain.add(handler);
/*     */ 
/* 298 */           ensureProperName(reader, QNAME_HANDLER);
/* 299 */           XMLStreamReaderUtil.nextContent(reader);
/*     */         }
/*     */ 
/* 303 */         ensureProperName(reader, QNAME_HANDLER_CHAIN);
/* 304 */         XMLStreamReaderUtil.nextContent(reader);
/*     */       }
/*     */     }
/* 307 */     info.setHandlers(handlerChain);
/* 308 */     info.setRoles(roles);
/* 309 */     return info;
/*     */   }
/*     */ 
/*     */   public HandlerAnnotationInfo getHandlersForPortInfo(PortInfo info)
/*     */   {
/* 314 */     HandlerAnnotationInfo handlerInfo = new HandlerAnnotationInfo();
/* 315 */     List handlerClassList = new ArrayList();
/* 316 */     Set roles = new HashSet();
/*     */ 
/* 318 */     for (HandlerChainType hchain : this.handlerChains) {
/* 319 */       boolean hchainMatched = false;
/* 320 */       if ((!hchain.isConstraintSet()) || (JAXWSUtils.matchQNames(info.getServiceName(), hchain.getServiceNamePattern())) || (JAXWSUtils.matchQNames(info.getPortName(), hchain.getPortNamePattern())) || (hchain.getProtocolBindings().contains(info.getBindingID())))
/*     */       {
/* 324 */         hchainMatched = true;
/*     */       }
/*     */ 
/* 327 */       if (hchainMatched) {
/* 328 */         for (HandlerType handler : hchain.getHandlers()) {
/*     */           try {
/* 330 */             Handler handlerClass = (Handler)loadClass(this.annotatedClass.getClassLoader(), handler.getHandlerClass()).newInstance();
/*     */ 
/* 332 */             callHandlerPostConstruct(handlerClass);
/* 333 */             handlerClassList.add(handlerClass);
/*     */           } catch (InstantiationException ie) {
/* 335 */             throw new RuntimeException(ie);
/*     */           } catch (IllegalAccessException e) {
/* 337 */             throw new RuntimeException(e);
/*     */           }
/*     */ 
/* 340 */           roles.addAll(handler.getSoapRoles());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 346 */     handlerInfo.setHandlers(handlerClassList);
/* 347 */     handlerInfo.setRoles(roles);
/* 348 */     return handlerInfo; } 
/*     */   private static Class loadClass(ClassLoader loader, String name) { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: iconst_1
/*     */     //   2: aload_0
/*     */     //   3: invokestatic 467	java/lang/Class:forName	(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
/*     */     //   6: areturn
/*     */     //   7: astore_2
/*     */     //   8: new 248	com/sun/xml/internal/ws/util/UtilException
/*     */     //   11: dup
/*     */     //   12: ldc 19
/*     */     //   14: iconst_1
/*     */     //   15: anewarray 256	java/lang/Object
/*     */     //   18: dup
/*     */     //   19: iconst_0
/*     */     //   20: aload_1
/*     */     //   21: aastore
/*     */     //   22: invokespecial 463	com/sun/xml/internal/ws/util/UtilException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   25: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	6	7	java/lang/ClassNotFoundException } 
/* 364 */   private static void callHandlerPostConstruct(Object handlerClass) { for (Method method : handlerClass.getClass().getMethods())
/* 365 */       if (method.getAnnotation(PostConstruct.class) != null)
/*     */       {
/*     */         try
/*     */         {
/* 369 */           method.invoke(handlerClass, new Object[0]);
/*     */         }
/*     */         catch (Exception e) {
/* 372 */           throw new RuntimeException(e);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private static void skipChain(XMLStreamReader reader)
/*     */   {
/* 379 */     while ((XMLStreamReaderUtil.nextContent(reader) != 2) || (!reader.getName().equals(QNAME_HANDLER_CHAIN)));
/* 381 */     XMLStreamReaderUtil.nextElementContent(reader);
/*     */   }
/*     */ 
/*     */   private static void skipTextElement(XMLStreamReader reader) {
/* 385 */     XMLStreamReaderUtil.nextContent(reader);
/* 386 */     XMLStreamReaderUtil.nextElementContent(reader);
/* 387 */     XMLStreamReaderUtil.nextElementContent(reader);
/*     */   }
/*     */ 
/*     */   private static void skipInitParamElement(XMLStreamReader reader) {
/*     */     int state;
/*     */     do
/* 393 */       state = XMLStreamReaderUtil.nextContent(reader);
/* 394 */     while ((state != 2) || (!reader.getName().equals(QNAME_HANDLER_PARAM)));
/*     */ 
/* 396 */     XMLStreamReaderUtil.nextElementContent(reader);
/*     */   }
/*     */ 
/*     */   private static void ensureProperName(XMLStreamReader reader, QName expectedName)
/*     */   {
/* 402 */     if (!reader.getName().equals(expectedName))
/* 403 */       failWithLocalName("util.parser.wrong.element", reader, expectedName.getLocalPart());
/*     */   }
/*     */ 
/*     */   static void ensureProperName(XMLStreamReader reader, String expectedName)
/*     */   {
/* 409 */     if (!reader.getLocalName().equals(expectedName))
/* 410 */       failWithLocalName("util.parser.wrong.element", reader, expectedName);
/*     */   }
/*     */ 
/*     */   private static void failWithLocalName(String key, XMLStreamReader reader, String arg)
/*     */   {
/* 417 */     throw new UtilException(key, new Object[] { Integer.toString(reader.getLocation().getLineNumber()), reader.getLocalName(), arg });
/*     */   }
/*     */ 
/*     */   static class HandlerChainType
/*     */   {
/*     */     QName serviceNamePattern;
/*     */     QName portNamePattern;
/*     */     List<String> protocolBindings;
/* 463 */     boolean constraintSet = false;
/*     */     List<HandlerChainsModel.HandlerType> handlers;
/*     */     String id;
/*     */ 
/*     */     public HandlerChainType()
/*     */     {
/* 471 */       this.protocolBindings = new ArrayList();
/*     */     }
/*     */ 
/*     */     public void setServiceNamePattern(QName value) {
/* 475 */       this.serviceNamePattern = value;
/* 476 */       this.constraintSet = true;
/*     */     }
/*     */ 
/*     */     public QName getServiceNamePattern() {
/* 480 */       return this.serviceNamePattern;
/*     */     }
/*     */ 
/*     */     public void setPortNamePattern(QName value) {
/* 484 */       this.portNamePattern = value;
/* 485 */       this.constraintSet = true;
/*     */     }
/*     */ 
/*     */     public QName getPortNamePattern() {
/* 489 */       return this.portNamePattern;
/*     */     }
/*     */ 
/*     */     public List<String> getProtocolBindings() {
/* 493 */       return this.protocolBindings;
/*     */     }
/*     */ 
/*     */     public void addProtocolBinding(String tokenOrURI)
/*     */     {
/* 501 */       tokenOrURI = DeploymentDescriptorParser.getBindingIdForToken(tokenOrURI);
/* 502 */       String binding = BindingID.parse(tokenOrURI).toString();
/* 503 */       this.protocolBindings.add(binding);
/* 504 */       this.constraintSet = true;
/*     */     }
/*     */ 
/*     */     public boolean isConstraintSet() {
/* 508 */       return (this.constraintSet) || (!this.protocolBindings.isEmpty());
/*     */     }
/*     */     public String getId() {
/* 511 */       return this.id;
/*     */     }
/*     */ 
/*     */     public void setId(String value) {
/* 515 */       this.id = value;
/*     */     }
/*     */ 
/*     */     public List<HandlerChainsModel.HandlerType> getHandlers() {
/* 519 */       if (this.handlers == null) {
/* 520 */         this.handlers = new ArrayList();
/*     */       }
/* 522 */       return this.handlers;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class HandlerType
/*     */   {
/*     */     String handlerName;
/*     */     String handlerClass;
/*     */     List<String> soapRoles;
/*     */     String id;
/*     */ 
/*     */     public String getHandlerName()
/*     */     {
/* 538 */       return this.handlerName;
/*     */     }
/*     */ 
/*     */     public void setHandlerName(String value) {
/* 542 */       this.handlerName = value;
/*     */     }
/*     */ 
/*     */     public String getHandlerClass() {
/* 546 */       return this.handlerClass;
/*     */     }
/*     */ 
/*     */     public void setHandlerClass(String value) {
/* 550 */       this.handlerClass = value;
/*     */     }
/*     */ 
/*     */     public String getId() {
/* 554 */       return this.id;
/*     */     }
/*     */ 
/*     */     public void setId(String value) {
/* 558 */       this.id = value;
/*     */     }
/*     */ 
/*     */     public List<String> getSoapRoles() {
/* 562 */       if (this.soapRoles == null) {
/* 563 */         this.soapRoles = new ArrayList();
/*     */       }
/* 565 */       return this.soapRoles;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.HandlerChainsModel
 * JD-Core Version:    0.6.2
 */