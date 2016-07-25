/*     */ package com.sun.xml.internal.ws.transport.http;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocumentSource;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*     */ import com.sun.xml.internal.ws.handler.HandlerChainsModel;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import com.sun.xml.internal.ws.resources.WsservletMessages;
/*     */ import com.sun.xml.internal.ws.server.EndpointFactory;
/*     */ import com.sun.xml.internal.ws.server.ServerRtException;
/*     */ import com.sun.xml.internal.ws.streaming.Attributes;
/*     */ import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import com.sun.xml.internal.ws.util.HandlerAnnotationInfo;
/*     */ import com.sun.xml.internal.ws.util.exception.LocatableWebServiceException;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.MTOMFeature;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ import org.xml.sax.EntityResolver;
/*     */ 
/*     */ public class DeploymentDescriptorParser<A>
/*     */ {
/*     */   private final Container container;
/*     */   private final ClassLoader classLoader;
/*     */   private final ResourceLoader loader;
/*     */   private final AdapterFactory<A> adapterFactory;
/*  98 */   private final Set<String> names = new HashSet();
/*     */ 
/* 103 */   private final Map<String, SDDocumentSource> docs = new HashMap();
/*     */   public static final String NS_RUNTIME = "http://java.sun.com/xml/ns/jax-ws/ri/runtime";
/*     */   public static final String JAXWS_WSDL_DD_DIR = "WEB-INF/wsdl";
/* 547 */   public static final QName QNAME_ENDPOINTS = new QName("http://java.sun.com/xml/ns/jax-ws/ri/runtime", "endpoints");
/*     */ 
/* 549 */   public static final QName QNAME_ENDPOINT = new QName("http://java.sun.com/xml/ns/jax-ws/ri/runtime", "endpoint");
/*     */   public static final String ATTR_VERSION = "version";
/*     */   public static final String ATTR_NAME = "name";
/*     */   public static final String ATTR_IMPLEMENTATION = "implementation";
/*     */   public static final String ATTR_WSDL = "wsdl";
/*     */   public static final String ATTR_SERVICE = "service";
/*     */   public static final String ATTR_PORT = "port";
/*     */   public static final String ATTR_URL_PATTERN = "url-pattern";
/*     */   public static final String ATTR_ENABLE_MTOM = "enable-mtom";
/*     */   public static final String ATTR_MTOM_THRESHOLD_VALUE = "mtom-threshold-value";
/*     */   public static final String ATTR_BINDING = "binding";
/*     */   public static final String ATTRVALUE_VERSION_1_0 = "2.0";
/* 564 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server.http");
/*     */ 
/*     */   public DeploymentDescriptorParser(ClassLoader cl, ResourceLoader loader, Container container, AdapterFactory<A> adapterFactory)
/*     */     throws MalformedURLException
/*     */   {
/* 117 */     this.classLoader = cl;
/* 118 */     this.loader = loader;
/* 119 */     this.container = container;
/* 120 */     this.adapterFactory = adapterFactory;
/*     */ 
/* 122 */     collectDocs("/WEB-INF/wsdl/");
/* 123 */     logger.fine("war metadata=" + this.docs);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public List<A> parse(String systemId, InputStream is)
/*     */   {
/* 131 */     XMLStreamReader reader = null;
/*     */     try {
/* 133 */       reader = new TidyXMLStreamReader(XMLStreamReaderFactory.create(systemId, is, true), is);
/*     */ 
/* 135 */       XMLStreamReaderUtil.nextElementContent(reader);
/* 136 */       return parseAdapters(reader);
/*     */     } finally {
/* 138 */       if (reader != null)
/*     */         try {
/* 140 */           reader.close();
/*     */         } catch (XMLStreamException e) {
/* 142 */           throw new ServerRtException("runtime.parser.xmlReader", new Object[] { e });
/*     */         }
/*     */       try
/*     */       {
/* 146 */         is.close();
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public List<A> parse(File f)
/*     */     throws IOException
/*     */   {
/* 158 */     FileInputStream in = new FileInputStream(f);
/*     */     try {
/* 160 */       return parse(f.getPath(), in);
/*     */     } finally {
/* 162 */       in.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void collectDocs(String dirPath)
/*     */     throws MalformedURLException
/*     */   {
/* 170 */     Set paths = this.loader.getResourcePaths(dirPath);
/* 171 */     if (paths != null)
/* 172 */       for (String path : paths)
/* 173 */         if (path.endsWith("/")) {
/* 174 */           if ((!path.endsWith("/CVS/")) && (!path.endsWith("/.svn/")))
/*     */           {
/* 176 */             collectDocs(path);
/*     */           }
/*     */         } else { URL res = this.loader.getResource(path);
/* 179 */           this.docs.put(res.toString(), SDDocumentSource.create(res));
/*     */         }
/*     */   }
/*     */ 
/*     */   private List<A> parseAdapters(XMLStreamReader reader)
/*     */   {
/* 187 */     if (!reader.getName().equals(QNAME_ENDPOINTS)) {
/* 188 */       failWithFullName("runtime.parser.invalidElement", reader);
/*     */     }
/*     */ 
/* 191 */     List adapters = new ArrayList();
/*     */ 
/* 193 */     Attributes attrs = XMLStreamReaderUtil.getAttributes(reader);
/* 194 */     String version = getMandatoryNonEmptyAttribute(reader, attrs, "version");
/* 195 */     if (!version.equals("2.0")) {
/* 196 */       failWithLocalName("runtime.parser.invalidVersionNumber", reader, version);
/*     */     }
/*     */ 
/* 200 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2)
/* 201 */       if (reader.getName().equals(QNAME_ENDPOINT))
/*     */       {
/* 203 */         attrs = XMLStreamReaderUtil.getAttributes(reader);
/* 204 */         String name = getMandatoryNonEmptyAttribute(reader, attrs, "name");
/* 205 */         if (!this.names.add(name)) {
/* 206 */           logger.warning(WsservletMessages.SERVLET_WARNING_DUPLICATE_ENDPOINT_NAME());
/*     */         }
/*     */ 
/* 210 */         String implementationName = getMandatoryNonEmptyAttribute(reader, attrs, "implementation");
/*     */ 
/* 212 */         Class implementorClass = getImplementorClass(implementationName, reader);
/* 213 */         EndpointFactory.verifyImplementorClass(implementorClass);
/*     */ 
/* 215 */         SDDocumentSource primaryWSDL = getPrimaryWSDL(reader, attrs, implementorClass);
/*     */ 
/* 217 */         QName serviceName = getQNameAttribute(attrs, "service");
/* 218 */         if (serviceName == null) {
/* 219 */           serviceName = EndpointFactory.getDefaultServiceName(implementorClass);
/*     */         }
/* 221 */         QName portName = getQNameAttribute(attrs, "port");
/* 222 */         if (portName == null) {
/* 223 */           portName = EndpointFactory.getDefaultPortName(serviceName, implementorClass);
/*     */         }
/*     */ 
/* 226 */         String enable_mtom = getAttribute(attrs, "enable-mtom");
/* 227 */         String mtomThreshold = getAttribute(attrs, "mtom-threshold-value");
/* 228 */         String bindingId = getAttribute(attrs, "binding");
/* 229 */         if (bindingId != null)
/*     */         {
/* 231 */           bindingId = getBindingIdForToken(bindingId);
/* 232 */         }WSBinding binding = createBinding(bindingId, implementorClass, enable_mtom, mtomThreshold);
/*     */ 
/* 234 */         String urlPattern = getMandatoryNonEmptyAttribute(reader, attrs, "url-pattern");
/*     */ 
/* 239 */         boolean handlersSetInDD = setHandlersAndRoles(binding, reader, serviceName, portName);
/*     */ 
/* 241 */         ensureNoContent(reader);
/* 242 */         WSEndpoint endpoint = WSEndpoint.create(implementorClass, !handlersSetInDD, null, serviceName, portName, this.container, binding, primaryWSDL, this.docs.values(), createEntityResolver(), false);
/*     */ 
/* 248 */         adapters.add(this.adapterFactory.createAdapter(name, urlPattern, endpoint));
/*     */       } else {
/* 250 */         failWithLocalName("runtime.parser.invalidElement", reader);
/*     */       }
/* 252 */     return adapters;
/*     */   }
/*     */ 
/*     */   private static WSBinding createBinding(String ddBindingId, Class implClass, String mtomEnabled, String mtomThreshold)
/*     */   {
/* 272 */     MTOMFeature mtomfeature = null;
/* 273 */     if (mtomEnabled != null)
/* 274 */       if (mtomThreshold != null) {
/* 275 */         mtomfeature = new MTOMFeature(Boolean.valueOf(mtomEnabled).booleanValue(), Integer.valueOf(mtomThreshold).intValue());
/*     */       }
/*     */       else
/* 278 */         mtomfeature = new MTOMFeature(Boolean.valueOf(mtomEnabled).booleanValue());
/*     */     BindingID bindingID;
/*     */     WebServiceFeatureList features;
/* 283 */     if (ddBindingId != null) {
/* 284 */       BindingID bindingID = BindingID.parse(ddBindingId);
/* 285 */       WebServiceFeatureList features = bindingID.createBuiltinFeatureList();
/*     */ 
/* 287 */       if (checkMtomConflict((MTOMFeature)features.get(MTOMFeature.class), mtomfeature))
/* 288 */         throw new ServerRtException(ServerMessages.DD_MTOM_CONFLICT(ddBindingId, mtomEnabled), new Object[0]);
/*     */     }
/*     */     else {
/* 291 */       bindingID = BindingID.parse(implClass);
/*     */ 
/* 295 */       features = new WebServiceFeatureList();
/* 296 */       if (mtomfeature != null)
/* 297 */         features.add(mtomfeature);
/* 298 */       features.addAll(bindingID.createBuiltinFeatureList());
/*     */     }
/*     */ 
/* 301 */     return bindingID.createBinding(features.toArray());
/*     */   }
/*     */ 
/*     */   private static boolean checkMtomConflict(MTOMFeature lhs, MTOMFeature rhs) {
/* 305 */     if ((lhs == null) || (rhs == null)) return false;
/* 306 */     return lhs.isEnabled() ^ rhs.isEnabled();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static String getBindingIdForToken(@NotNull String lexical)
/*     */   {
/* 320 */     if (lexical.equals("##SOAP11_HTTP"))
/* 321 */       return "http://schemas.xmlsoap.org/wsdl/soap/http";
/* 322 */     if (lexical.equals("##SOAP11_HTTP_MTOM"))
/* 323 */       return "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true";
/* 324 */     if (lexical.equals("##SOAP12_HTTP"))
/* 325 */       return "http://www.w3.org/2003/05/soap/bindings/HTTP/";
/* 326 */     if (lexical.equals("##SOAP12_HTTP_MTOM"))
/* 327 */       return "http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true";
/* 328 */     if (lexical.equals("##XML_HTTP")) {
/* 329 */       return "http://www.w3.org/2004/08/wsdl/http";
/*     */     }
/* 331 */     return lexical;
/*     */   }
/*     */ 
/*     */   private SDDocumentSource getPrimaryWSDL(XMLStreamReader xsr, Attributes attrs, Class<?> implementorClass)
/*     */   {
/* 354 */     String wsdlFile = getAttribute(attrs, "wsdl");
/* 355 */     if (wsdlFile == null) {
/* 356 */       wsdlFile = EndpointFactory.getWsdlLocation(implementorClass);
/*     */     }
/*     */ 
/* 359 */     if (wsdlFile != null) {
/* 360 */       if (!wsdlFile.startsWith("WEB-INF/wsdl")) {
/* 361 */         logger.warning("Ignoring wrong wsdl=" + wsdlFile + ". It should start with " + "WEB-INF/wsdl" + ". Going to generate and publish a new WSDL.");
/*     */ 
/* 364 */         return null;
/*     */       }
/*     */       URL wsdl;
/*     */       try
/*     */       {
/* 369 */         wsdl = this.loader.getResource('/' + wsdlFile);
/*     */       } catch (MalformedURLException e) {
/* 371 */         throw new LocatableWebServiceException(ServerMessages.RUNTIME_PARSER_WSDL_NOT_FOUND(wsdlFile), e, xsr);
/*     */       }
/*     */ 
/* 374 */       if (wsdl == null) {
/* 375 */         throw new LocatableWebServiceException(ServerMessages.RUNTIME_PARSER_WSDL_NOT_FOUND(wsdlFile), xsr);
/*     */       }
/*     */ 
/* 378 */       SDDocumentSource docInfo = (SDDocumentSource)this.docs.get(wsdl.toExternalForm());
/* 379 */       assert (docInfo != null);
/* 380 */       return docInfo;
/*     */     }
/*     */ 
/* 383 */     return null;
/*     */   }
/*     */ 
/*     */   private EntityResolver createEntityResolver()
/*     */   {
/*     */     try
/*     */     {
/* 391 */       return XmlUtil.createEntityResolver(this.loader.getCatalogFile());
/*     */     } catch (MalformedURLException e) {
/* 393 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String getAttribute(Attributes attrs, String name) {
/* 398 */     String value = attrs.getValue(name);
/* 399 */     if (value != null) {
/* 400 */       value = value.trim();
/*     */     }
/* 402 */     return value;
/*     */   }
/*     */ 
/*     */   protected QName getQNameAttribute(Attributes attrs, String name) {
/* 406 */     String value = getAttribute(attrs, name);
/* 407 */     if ((value == null) || (value.equals(""))) {
/* 408 */       return null;
/*     */     }
/* 410 */     return QName.valueOf(value);
/*     */   }
/*     */ 
/*     */   protected String getNonEmptyAttribute(XMLStreamReader reader, Attributes attrs, String name)
/*     */   {
/* 415 */     String value = getAttribute(attrs, name);
/* 416 */     if ((value != null) && (value.equals(""))) {
/* 417 */       failWithLocalName("runtime.parser.invalidAttributeValue", reader, name);
/*     */     }
/*     */ 
/* 422 */     return value;
/*     */   }
/*     */ 
/*     */   protected String getMandatoryAttribute(XMLStreamReader reader, Attributes attrs, String name) {
/* 426 */     String value = getAttribute(attrs, name);
/* 427 */     if (value == null) {
/* 428 */       failWithLocalName("runtime.parser.missing.attribute", reader, name);
/*     */     }
/* 430 */     return value;
/*     */   }
/*     */ 
/*     */   protected String getMandatoryNonEmptyAttribute(XMLStreamReader reader, Attributes attributes, String name)
/*     */   {
/* 435 */     String value = getAttribute(attributes, name);
/* 436 */     if (value == null)
/* 437 */       failWithLocalName("runtime.parser.missing.attribute", reader, name);
/* 438 */     else if (value.equals("")) {
/* 439 */       failWithLocalName("runtime.parser.invalidAttributeValue", reader, name);
/*     */     }
/*     */ 
/* 444 */     return value;
/*     */   }
/*     */ 
/*     */   protected boolean setHandlersAndRoles(WSBinding binding, XMLStreamReader reader, QName serviceName, QName portName)
/*     */   {
/* 455 */     if ((XMLStreamReaderUtil.nextElementContent(reader) == 2) || (!reader.getName().equals(HandlerChainsModel.QNAME_HANDLER_CHAINS)))
/*     */     {
/* 460 */       return false;
/*     */     }
/*     */ 
/* 463 */     HandlerAnnotationInfo handlerInfo = HandlerChainsModel.parseHandlerFile(reader, this.classLoader, serviceName, portName, binding);
/*     */ 
/* 466 */     binding.setHandlerChain(handlerInfo.getHandlers());
/* 467 */     if ((binding instanceof SOAPBinding)) {
/* 468 */       ((SOAPBinding)binding).setRoles(handlerInfo.getRoles());
/*     */     }
/*     */ 
/* 472 */     XMLStreamReaderUtil.nextContent(reader);
/* 473 */     return true;
/*     */   }
/*     */ 
/*     */   protected static void ensureNoContent(XMLStreamReader reader) {
/* 477 */     if (reader.getEventType() != 2)
/* 478 */       fail("runtime.parser.unexpectedContent", reader);
/*     */   }
/*     */ 
/*     */   protected static void fail(String key, XMLStreamReader reader)
/*     */   {
/* 483 */     logger.log(Level.SEVERE, key + reader.getLocation().getLineNumber());
/* 484 */     throw new ServerRtException(key, new Object[] { Integer.toString(reader.getLocation().getLineNumber()) });
/*     */   }
/*     */ 
/*     */   protected static void failWithFullName(String key, XMLStreamReader reader)
/*     */   {
/* 490 */     throw new ServerRtException(key, new Object[] { Integer.valueOf(reader.getLocation().getLineNumber()), reader.getName() });
/*     */   }
/*     */ 
/*     */   protected static void failWithLocalName(String key, XMLStreamReader reader)
/*     */   {
/* 497 */     throw new ServerRtException(key, new Object[] { Integer.valueOf(reader.getLocation().getLineNumber()), reader.getLocalName() });
/*     */   }
/*     */ 
/*     */   protected static void failWithLocalName(String key, XMLStreamReader reader, String arg)
/*     */   {
/* 507 */     throw new ServerRtException(key, new Object[] { Integer.valueOf(reader.getLocation().getLineNumber()), reader.getLocalName(), arg }); } 
/*     */   protected Class loadClass(String name) { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: iconst_1
/*     */     //   2: aload_0
/*     */     //   3: getfield 591	com/sun/xml/internal/ws/transport/http/DeploymentDescriptorParser:classLoader	Ljava/lang/ClassLoader;
/*     */     //   6: invokestatic 655	java/lang/Class:forName	(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
/*     */     //   9: areturn
/*     */     //   10: astore_2
/*     */     //   11: getstatic 594	com/sun/xml/internal/ws/transport/http/DeploymentDescriptorParser:logger	Ljava/util/logging/Logger;
/*     */     //   14: getstatic 597	java/util/logging/Level:SEVERE	Ljava/util/logging/Level;
/*     */     //   17: aload_2
/*     */     //   18: invokevirtual 656	java/lang/ClassNotFoundException:getMessage	()Ljava/lang/String;
/*     */     //   21: aload_2
/*     */     //   22: invokevirtual 681	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   25: new 322	com/sun/xml/internal/ws/server/ServerRtException
/*     */     //   28: dup
/*     */     //   29: ldc 31
/*     */     //   31: iconst_1
/*     */     //   32: anewarray 341	java/lang/Object
/*     */     //   35: dup
/*     */     //   36: iconst_0
/*     */     //   37: aload_1
/*     */     //   38: aastore
/*     */     //   39: invokespecial 619	com/sun/xml/internal/ws/server/ServerRtException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   42: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	9	10	java/lang/ClassNotFoundException } 
/*     */   private Class getImplementorClass(String name, XMLStreamReader xsr) { try { return Class.forName(name, true, this.classLoader);
/*     */     } catch (ClassNotFoundException e) {
/* 536 */       logger.log(Level.SEVERE, e.getMessage(), e);
/* 537 */       throw new LocatableWebServiceException(ServerMessages.RUNTIME_PARSER_CLASS_NOT_FOUND(name), e, xsr);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface AdapterFactory<A>
/*     */   {
/*     */     public abstract A createAdapter(String paramString1, String paramString2, WSEndpoint<?> paramWSEndpoint);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser
 * JD-Core Version:    0.6.2
 */