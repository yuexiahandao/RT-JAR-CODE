/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.Status;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.State;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.State;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*     */ import java.io.IOException;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.ext.EntityResolver2;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class DOMParser extends AbstractDOMParser
/*     */ {
/*     */   protected static final String USE_ENTITY_RESOLVER2 = "http://xml.org/sax/features/use-entity-resolver2";
/*     */   protected static final String REPORT_WHITESPACE = "http://java.sun.com/xml/schema/features/report-ignored-element-content-whitespace";
/*     */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*  84 */   private static final String[] RECOGNIZED_FEATURES = { "http://java.sun.com/xml/schema/features/report-ignored-element-content-whitespace" };
/*     */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*  99 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/grammar-pool" };
/*     */ 
/* 111 */   protected boolean fUseEntityResolver2 = true;
/*     */ 
/*     */   public DOMParser(XMLParserConfiguration config)
/*     */   {
/* 121 */     super(config);
/*     */   }
/*     */ 
/*     */   public DOMParser()
/*     */   {
/* 128 */     this(null, null);
/*     */   }
/*     */ 
/*     */   public DOMParser(SymbolTable symbolTable)
/*     */   {
/* 135 */     this(symbolTable, null);
/*     */   }
/*     */ 
/*     */   public DOMParser(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 144 */     super(new XIncludeAwareParserConfiguration());
/*     */ 
/* 147 */     this.fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
/* 148 */     if (symbolTable != null) {
/* 149 */       this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
/*     */     }
/* 151 */     if (grammarPool != null) {
/* 152 */       this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", grammarPool);
/*     */     }
/*     */ 
/* 155 */     this.fConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
/*     */   }
/*     */ 
/*     */   public void parse(String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 179 */     XMLInputSource source = new XMLInputSource(null, systemId, null);
/*     */     try {
/* 181 */       parse(source);
/*     */     }
/*     */     catch (XMLParseException e)
/*     */     {
/* 186 */       Exception ex = e.getException();
/* 187 */       if (ex == null)
/*     */       {
/* 190 */         LocatorImpl locatorImpl = new LocatorImpl();
/* 191 */         locatorImpl.setPublicId(e.getPublicId());
/* 192 */         locatorImpl.setSystemId(e.getExpandedSystemId());
/* 193 */         locatorImpl.setLineNumber(e.getLineNumber());
/* 194 */         locatorImpl.setColumnNumber(e.getColumnNumber());
/* 195 */         throw new SAXParseException(e.getMessage(), locatorImpl);
/*     */       }
/* 197 */       if ((ex instanceof SAXException))
/*     */       {
/* 199 */         throw ((SAXException)ex);
/*     */       }
/* 201 */       if ((ex instanceof IOException)) {
/* 202 */         throw ((IOException)ex);
/*     */       }
/* 204 */       throw new SAXException(ex);
/*     */     }
/*     */     catch (XNIException e) {
/* 207 */       e.printStackTrace();
/* 208 */       Exception ex = e.getException();
/* 209 */       if (ex == null) {
/* 210 */         throw new SAXException(e.getMessage());
/*     */       }
/* 212 */       if ((ex instanceof SAXException)) {
/* 213 */         throw ((SAXException)ex);
/*     */       }
/* 215 */       if ((ex instanceof IOException)) {
/* 216 */         throw ((IOException)ex);
/*     */       }
/* 218 */       throw new SAXException(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void parse(InputSource inputSource)
/*     */     throws SAXException, IOException
/*     */   {
/*     */     try
/*     */     {
/* 236 */       XMLInputSource xmlInputSource = new XMLInputSource(inputSource.getPublicId(), inputSource.getSystemId(), null);
/*     */ 
/* 240 */       xmlInputSource.setByteStream(inputSource.getByteStream());
/* 241 */       xmlInputSource.setCharacterStream(inputSource.getCharacterStream());
/* 242 */       xmlInputSource.setEncoding(inputSource.getEncoding());
/* 243 */       parse(xmlInputSource);
/*     */     }
/*     */     catch (XMLParseException e)
/*     */     {
/* 248 */       Exception ex = e.getException();
/* 249 */       if (ex == null)
/*     */       {
/* 252 */         LocatorImpl locatorImpl = new LocatorImpl();
/* 253 */         locatorImpl.setPublicId(e.getPublicId());
/* 254 */         locatorImpl.setSystemId(e.getExpandedSystemId());
/* 255 */         locatorImpl.setLineNumber(e.getLineNumber());
/* 256 */         locatorImpl.setColumnNumber(e.getColumnNumber());
/* 257 */         throw new SAXParseException(e.getMessage(), locatorImpl);
/*     */       }
/* 259 */       if ((ex instanceof SAXException))
/*     */       {
/* 261 */         throw ((SAXException)ex);
/*     */       }
/* 263 */       if ((ex instanceof IOException)) {
/* 264 */         throw ((IOException)ex);
/*     */       }
/* 266 */       throw new SAXException(ex);
/*     */     }
/*     */     catch (XNIException e) {
/* 269 */       Exception ex = e.getException();
/* 270 */       if (ex == null) {
/* 271 */         throw new SAXException(e.getMessage());
/*     */       }
/* 273 */       if ((ex instanceof SAXException)) {
/* 274 */         throw ((SAXException)ex);
/*     */       }
/* 276 */       if ((ex instanceof IOException)) {
/* 277 */         throw ((IOException)ex);
/*     */       }
/* 279 */       throw new SAXException(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver resolver)
/*     */   {
/*     */     try
/*     */     {
/* 294 */       XMLEntityResolver xer = (XMLEntityResolver)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
/* 295 */       if ((this.fUseEntityResolver2) && ((resolver instanceof EntityResolver2))) {
/* 296 */         if ((xer instanceof EntityResolver2Wrapper)) {
/* 297 */           EntityResolver2Wrapper er2w = (EntityResolver2Wrapper)xer;
/* 298 */           er2w.setEntityResolver((EntityResolver2)resolver);
/*     */         }
/*     */         else {
/* 301 */           this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2)resolver));
/*     */         }
/*     */ 
/*     */       }
/* 306 */       else if ((xer instanceof EntityResolverWrapper)) {
/* 307 */         EntityResolverWrapper erw = (EntityResolverWrapper)xer;
/* 308 */         erw.setEntityResolver(resolver);
/*     */       }
/*     */       else {
/* 311 */         this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(resolver));
/*     */       }
/*     */     }
/*     */     catch (XMLConfigurationException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver()
/*     */   {
/* 331 */     EntityResolver entityResolver = null;
/*     */     try {
/* 333 */       XMLEntityResolver xmlEntityResolver = (XMLEntityResolver)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
/*     */ 
/* 335 */       if (xmlEntityResolver != null) {
/* 336 */         if ((xmlEntityResolver instanceof EntityResolverWrapper)) {
/* 337 */           entityResolver = ((EntityResolverWrapper)xmlEntityResolver).getEntityResolver();
/*     */         }
/* 340 */         else if ((xmlEntityResolver instanceof EntityResolver2Wrapper)) {
/* 341 */           entityResolver = ((EntityResolver2Wrapper)xmlEntityResolver).getEntityResolver();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (XMLConfigurationException e)
/*     */     {
/*     */     }
/*     */ 
/* 349 */     return entityResolver;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler errorHandler)
/*     */   {
/*     */     try
/*     */     {
/* 374 */       XMLErrorHandler xeh = (XMLErrorHandler)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
/* 375 */       if ((xeh instanceof ErrorHandlerWrapper)) {
/* 376 */         ErrorHandlerWrapper ehw = (ErrorHandlerWrapper)xeh;
/* 377 */         ehw.setErrorHandler(errorHandler);
/*     */       }
/*     */       else {
/* 380 */         this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(errorHandler));
/*     */       }
/*     */     }
/*     */     catch (XMLConfigurationException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler()
/*     */   {
/* 399 */     ErrorHandler errorHandler = null;
/*     */     try {
/* 401 */       XMLErrorHandler xmlErrorHandler = (XMLErrorHandler)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
/*     */ 
/* 403 */       if ((xmlErrorHandler != null) && ((xmlErrorHandler instanceof ErrorHandlerWrapper)))
/*     */       {
/* 405 */         errorHandler = ((ErrorHandlerWrapper)xmlErrorHandler).getErrorHandler();
/*     */       }
/*     */     }
/*     */     catch (XMLConfigurationException e)
/*     */     {
/*     */     }
/* 411 */     return errorHandler;
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */     try
/*     */     {
/* 438 */       if (featureId.equals("http://xml.org/sax/features/use-entity-resolver2")) {
/* 439 */         if (state != this.fUseEntityResolver2) {
/* 440 */           this.fUseEntityResolver2 = state;
/*     */ 
/* 442 */           setEntityResolver(getEntityResolver());
/*     */         }
/* 444 */         return;
/*     */       }
/*     */ 
/* 451 */       this.fConfiguration.setFeature(featureId, state);
/*     */     }
/*     */     catch (XMLConfigurationException e) {
/* 454 */       String identifier = e.getIdentifier();
/* 455 */       if (e.getType() == Status.NOT_RECOGNIZED) {
/* 456 */         throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[] { identifier }));
/*     */       }
/*     */ 
/* 461 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[] { identifier })); }  } 
/*     */   public boolean getFeature(String featureId) throws SAXNotRecognizedException, SAXNotSupportedException { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ldc 13
/*     */     //   3: invokevirtual 381	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   6: ifeq +8 -> 14
/*     */     //   9: aload_0
/*     */     //   10: getfield 331	com/sun/org/apache/xerces/internal/parsers/DOMParser:fUseEntityResolver2	Z
/*     */     //   13: ireturn
/*     */     //   14: aload_0
/*     */     //   15: getfield 334	com/sun/org/apache/xerces/internal/parsers/DOMParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*     */     //   18: aload_1
/*     */     //   19: invokeinterface 397 2 0
/*     */     //   24: ireturn
/*     */     //   25: astore_2
/*     */     //   26: aload_2
/*     */     //   27: invokevirtual 370	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getIdentifier	()Ljava/lang/String;
/*     */     //   30: astore_3
/*     */     //   31: aload_2
/*     */     //   32: invokevirtual 369	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getType	()Lcom/sun/org/apache/xerces/internal/util/Status;
/*     */     //   35: getstatic 338	com/sun/org/apache/xerces/internal/util/Status:NOT_RECOGNIZED	Lcom/sun/org/apache/xerces/internal/util/Status;
/*     */     //   38: if_acmpne +33 -> 71
/*     */     //   41: new 210	org/xml/sax/SAXNotRecognizedException
/*     */     //   44: dup
/*     */     //   45: aload_0
/*     */     //   46: getfield 334	com/sun/org/apache/xerces/internal/parsers/DOMParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*     */     //   49: invokeinterface 401 1 0
/*     */     //   54: ldc 2
/*     */     //   56: iconst_1
/*     */     //   57: anewarray 203	java/lang/Object
/*     */     //   60: dup
/*     */     //   61: iconst_0
/*     */     //   62: aload_3
/*     */     //   63: aastore
/*     */     //   64: invokestatic 358	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   67: invokespecial 389	org/xml/sax/SAXNotRecognizedException:<init>	(Ljava/lang/String;)V
/*     */     //   70: athrow
/*     */     //   71: new 211	org/xml/sax/SAXNotSupportedException
/*     */     //   74: dup
/*     */     //   75: aload_0
/*     */     //   76: getfield 334	com/sun/org/apache/xerces/internal/parsers/DOMParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*     */     //   79: invokeinterface 401 1 0
/*     */     //   84: ldc 3
/*     */     //   86: iconst_1
/*     */     //   87: anewarray 203	java/lang/Object
/*     */     //   90: dup
/*     */     //   91: iconst_0
/*     */     //   92: aload_3
/*     */     //   93: aastore
/*     */     //   94: invokestatic 358	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   97: invokespecial 390	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*     */     //   100: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	13	25	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*     */     //   14	24	25	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException } 
/* 539 */   public void setProperty(String propertyId, Object value) throws SAXNotRecognizedException, SAXNotSupportedException { if (propertyId.equals("http://apache.org/xml/properties/security-manager")) {
/* 540 */       this.securityManager = XMLSecurityManager.convert(value, this.securityManager);
/* 541 */       setProperty0("http://apache.org/xml/properties/security-manager", this.securityManager);
/* 542 */       return;
/*     */     }
/* 544 */     if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
/* 545 */       if (value == null)
/* 546 */         this.securityPropertyManager = new XMLSecurityPropertyManager();
/*     */       else {
/* 548 */         this.securityPropertyManager = ((XMLSecurityPropertyManager)value);
/*     */       }
/* 550 */       setProperty0("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.securityPropertyManager);
/* 551 */       return;
/*     */     }
/*     */ 
/* 554 */     if (this.securityManager == null) {
/* 555 */       this.securityManager = new XMLSecurityManager(true);
/* 556 */       setProperty0("http://apache.org/xml/properties/security-manager", this.securityManager);
/*     */     }
/*     */ 
/* 559 */     if (this.securityPropertyManager == null) {
/* 560 */       this.securityPropertyManager = new XMLSecurityPropertyManager();
/* 561 */       setProperty0("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.securityPropertyManager);
/*     */     }
/* 563 */     int index = this.securityPropertyManager.getIndex(propertyId);
/*     */ 
/* 565 */     if (index > -1)
/*     */     {
/* 571 */       this.securityPropertyManager.setValue(index, XMLSecurityPropertyManager.State.APIPROPERTY, (String)value);
/*     */     }
/* 574 */     else if (!this.securityManager.setLimit(propertyId, XMLSecurityManager.State.APIPROPERTY, value))
/*     */     {
/* 576 */       setProperty0(propertyId, value);
/*     */     } }
/*     */ 
/*     */   public void setProperty0(String propertyId, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */     try
/*     */     {
/* 584 */       this.fConfiguration.setProperty(propertyId, value);
/*     */     }
/*     */     catch (XMLConfigurationException e) {
/* 587 */       String identifier = e.getIdentifier();
/* 588 */       if (e.getType() == Status.NOT_RECOGNIZED) {
/* 589 */         throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[] { identifier }));
/*     */       }
/*     */ 
/* 594 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[] { identifier })); }  } 
/*     */   public Object getProperty(String propertyId) throws SAXNotRecognizedException, SAXNotSupportedException { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ldc 5
/*     */     //   3: invokevirtual 381	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   6: ifeq +59 -> 65
/*     */     //   9: iconst_0
/*     */     //   10: istore_2
/*     */     //   11: aload_0
/*     */     //   12: ldc 4
/*     */     //   14: invokevirtual 343	com/sun/org/apache/xerces/internal/parsers/DOMParser:getFeature	(Ljava/lang/String;)Z
/*     */     //   17: istore_2
/*     */     //   18: goto +4 -> 22
/*     */     //   21: astore_3
/*     */     //   22: iload_2
/*     */     //   23: ifeq +13 -> 36
/*     */     //   26: new 211	org/xml/sax/SAXNotSupportedException
/*     */     //   29: dup
/*     */     //   30: ldc 1
/*     */     //   32: invokespecial 390	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*     */     //   35: athrow
/*     */     //   36: aload_0
/*     */     //   37: getfield 337	com/sun/org/apache/xerces/internal/parsers/DOMParser:fCurrentNode	Lorg/w3c/dom/Node;
/*     */     //   40: ifnull +23 -> 63
/*     */     //   43: aload_0
/*     */     //   44: getfield 337	com/sun/org/apache/xerces/internal/parsers/DOMParser:fCurrentNode	Lorg/w3c/dom/Node;
/*     */     //   47: invokeinterface 404 1 0
/*     */     //   52: iconst_1
/*     */     //   53: if_icmpne +10 -> 63
/*     */     //   56: aload_0
/*     */     //   57: getfield 337	com/sun/org/apache/xerces/internal/parsers/DOMParser:fCurrentNode	Lorg/w3c/dom/Node;
/*     */     //   60: goto +4 -> 64
/*     */     //   63: aconst_null
/*     */     //   64: areturn
/*     */     //   65: aload_0
/*     */     //   66: getfield 334	com/sun/org/apache/xerces/internal/parsers/DOMParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*     */     //   69: ldc 12
/*     */     //   71: invokeinterface 402 2 0
/*     */     //   76: checkcast 191	com/sun/org/apache/xerces/internal/utils/XMLSecurityPropertyManager
/*     */     //   79: astore_2
/*     */     //   80: aload_2
/*     */     //   81: aload_1
/*     */     //   82: invokevirtual 364	com/sun/org/apache/xerces/internal/utils/XMLSecurityPropertyManager:getIndex	(Ljava/lang/String;)I
/*     */     //   85: istore_3
/*     */     //   86: iload_3
/*     */     //   87: iconst_m1
/*     */     //   88: if_icmple +9 -> 97
/*     */     //   91: aload_2
/*     */     //   92: iload_3
/*     */     //   93: invokevirtual 363	com/sun/org/apache/xerces/internal/utils/XMLSecurityPropertyManager:getValueByIndex	(I)Ljava/lang/String;
/*     */     //   96: areturn
/*     */     //   97: aload_0
/*     */     //   98: getfield 334	com/sun/org/apache/xerces/internal/parsers/DOMParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*     */     //   101: aload_1
/*     */     //   102: invokeinterface 402 2 0
/*     */     //   107: areturn
/*     */     //   108: astore_2
/*     */     //   109: aload_2
/*     */     //   110: invokevirtual 370	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getIdentifier	()Ljava/lang/String;
/*     */     //   113: astore_3
/*     */     //   114: aload_2
/*     */     //   115: invokevirtual 369	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getType	()Lcom/sun/org/apache/xerces/internal/util/Status;
/*     */     //   118: getstatic 338	com/sun/org/apache/xerces/internal/util/Status:NOT_RECOGNIZED	Lcom/sun/org/apache/xerces/internal/util/Status;
/*     */     //   121: if_acmpne +33 -> 154
/*     */     //   124: new 210	org/xml/sax/SAXNotRecognizedException
/*     */     //   127: dup
/*     */     //   128: aload_0
/*     */     //   129: getfield 334	com/sun/org/apache/xerces/internal/parsers/DOMParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*     */     //   132: invokeinterface 401 1 0
/*     */     //   137: ldc 14
/*     */     //   139: iconst_1
/*     */     //   140: anewarray 203	java/lang/Object
/*     */     //   143: dup
/*     */     //   144: iconst_0
/*     */     //   145: aload_3
/*     */     //   146: aastore
/*     */     //   147: invokestatic 358	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   150: invokespecial 389	org/xml/sax/SAXNotRecognizedException:<init>	(Ljava/lang/String;)V
/*     */     //   153: athrow
/*     */     //   154: new 211	org/xml/sax/SAXNotSupportedException
/*     */     //   157: dup
/*     */     //   158: aload_0
/*     */     //   159: getfield 334	com/sun/org/apache/xerces/internal/parsers/DOMParser:fConfiguration	Lcom/sun/org/apache/xerces/internal/xni/parser/XMLParserConfiguration;
/*     */     //   162: invokeinterface 401 1 0
/*     */     //   167: ldc 15
/*     */     //   169: iconst_1
/*     */     //   170: anewarray 203	java/lang/Object
/*     */     //   173: dup
/*     */     //   174: iconst_0
/*     */     //   175: aload_3
/*     */     //   176: aastore
/*     */     //   177: invokestatic 358	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   180: invokespecial 390	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*     */     //   183: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   11	18	21	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*     */     //   65	96	108	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException
/*     */     //   97	107	108	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException } 
/* 664 */   public XMLParserConfiguration getXMLParserConfiguration() { return this.fConfiguration; }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.DOMParser
 * JD-Core Version:    0.6.2
 */