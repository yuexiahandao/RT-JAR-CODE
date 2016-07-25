/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMInputSource;
/*     */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.SAXInputSource;
/*     */ import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.StAXInputSource;
/*     */ import com.sun.org.apache.xerces.internal.util.Status;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.State;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.Property;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.State;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public final class XMLSchemaFactory extends SchemaFactory
/*     */ {
/*     */   private static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
/*     */   private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*     */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*  95 */   private final XMLSchemaLoader fXMLSchemaLoader = new XMLSchemaLoader();
/*     */   private ErrorHandler fErrorHandler;
/*     */   private LSResourceResolver fLSResourceResolver;
/*     */   private final DOMEntityResolverWrapper fDOMEntityResolverWrapper;
/*     */   private ErrorHandlerWrapper fErrorHandlerWrapper;
/*     */   private XMLSecurityManager fSecurityManager;
/*     */   private XMLSecurityPropertyManager fSecurityPropertyMgr;
/*     */   private XMLGrammarPoolWrapper fXMLGrammarPoolWrapper;
/*     */   private final boolean fUseServicesMechanism;
/*     */ 
/*     */   public XMLSchemaFactory()
/*     */   {
/* 125 */     this(true);
/*     */   }
/*     */   public static XMLSchemaFactory newXMLSchemaFactoryNoServiceLoader() {
/* 128 */     return new XMLSchemaFactory(false);
/*     */   }
/*     */   private XMLSchemaFactory(boolean useServicesMechanism) {
/* 131 */     this.fUseServicesMechanism = useServicesMechanism;
/* 132 */     this.fErrorHandlerWrapper = new ErrorHandlerWrapper(DraconianErrorHandler.getInstance());
/* 133 */     this.fDOMEntityResolverWrapper = new DOMEntityResolverWrapper();
/* 134 */     this.fXMLGrammarPoolWrapper = new XMLGrammarPoolWrapper();
/* 135 */     this.fXMLSchemaLoader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
/* 136 */     this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fXMLGrammarPoolWrapper);
/* 137 */     this.fXMLSchemaLoader.setEntityResolver(this.fDOMEntityResolverWrapper);
/* 138 */     this.fXMLSchemaLoader.setErrorHandler(this.fErrorHandlerWrapper);
/*     */ 
/* 141 */     this.fSecurityManager = new XMLSecurityManager(true);
/* 142 */     this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/*     */ 
/* 144 */     this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
/* 145 */     this.fXMLSchemaLoader.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/*     */   }
/*     */ 
/*     */   public boolean isSchemaLanguageSupported(String schemaLanguage)
/*     */   {
/* 162 */     if (schemaLanguage == null) {
/* 163 */       throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaLanguageNull", null));
/*     */     }
/*     */ 
/* 166 */     if (schemaLanguage.length() == 0) {
/* 167 */       throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaLanguageLengthZero", null));
/*     */     }
/*     */ 
/* 171 */     return schemaLanguage.equals("http://www.w3.org/2001/XMLSchema");
/*     */   }
/*     */ 
/*     */   public LSResourceResolver getResourceResolver() {
/* 175 */     return this.fLSResourceResolver;
/*     */   }
/*     */ 
/*     */   public void setResourceResolver(LSResourceResolver resourceResolver) {
/* 179 */     this.fLSResourceResolver = resourceResolver;
/* 180 */     this.fDOMEntityResolverWrapper.setEntityResolver(resourceResolver);
/* 181 */     this.fXMLSchemaLoader.setEntityResolver(this.fDOMEntityResolverWrapper);
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler() {
/* 185 */     return this.fErrorHandler;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 189 */     this.fErrorHandler = errorHandler;
/* 190 */     this.fErrorHandlerWrapper.setErrorHandler(errorHandler != null ? errorHandler : DraconianErrorHandler.getInstance());
/* 191 */     this.fXMLSchemaLoader.setErrorHandler(this.fErrorHandlerWrapper);
/*     */   }
/*     */ 
/*     */   public Schema newSchema(Source[] schemas)
/*     */     throws SAXException
/*     */   {
/* 197 */     XMLGrammarPoolImplExtension pool = new XMLGrammarPoolImplExtension();
/* 198 */     this.fXMLGrammarPoolWrapper.setGrammarPool(pool);
/*     */ 
/* 200 */     XMLInputSource[] xmlInputSources = new XMLInputSource[schemas.length];
/*     */ 
/* 203 */     for (int i = 0; i < schemas.length; i++) {
/* 204 */       Source source = schemas[i];
/* 205 */       if ((source instanceof StreamSource)) {
/* 206 */         StreamSource streamSource = (StreamSource)source;
/* 207 */         String publicId = streamSource.getPublicId();
/* 208 */         String systemId = streamSource.getSystemId();
/* 209 */         InputStream inputStream = streamSource.getInputStream();
/* 210 */         Reader reader = streamSource.getReader();
/* 211 */         xmlInputSources[i] = new XMLInputSource(publicId, systemId, null);
/* 212 */         xmlInputSources[i].setByteStream(inputStream);
/* 213 */         xmlInputSources[i].setCharacterStream(reader);
/*     */       }
/* 215 */       else if ((source instanceof SAXSource)) {
/* 216 */         SAXSource saxSource = (SAXSource)source;
/* 217 */         InputSource inputSource = saxSource.getInputSource();
/* 218 */         if (inputSource == null) {
/* 219 */           throw new SAXException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SAXSourceNullInputSource", null));
/*     */         }
/*     */ 
/* 222 */         xmlInputSources[i] = new SAXInputSource(saxSource.getXMLReader(), inputSource);
/*     */       }
/* 224 */       else if ((source instanceof DOMSource)) {
/* 225 */         DOMSource domSource = (DOMSource)source;
/* 226 */         Node node = domSource.getNode();
/* 227 */         String systemID = domSource.getSystemId();
/* 228 */         xmlInputSources[i] = new DOMInputSource(node, systemID);
/*     */       }
/* 230 */       else if ((source instanceof StAXSource)) {
/* 231 */         StAXSource staxSource = (StAXSource)source;
/* 232 */         XMLEventReader eventReader = staxSource.getXMLEventReader();
/* 233 */         if (eventReader != null) {
/* 234 */           xmlInputSources[i] = new StAXInputSource(eventReader);
/*     */         }
/*     */         else
/* 237 */           xmlInputSources[i] = new StAXInputSource(staxSource.getXMLStreamReader());
/*     */       }
/*     */       else {
/* 240 */         if (source == null) {
/* 241 */           throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaSourceArrayMemberNull", null));
/*     */         }
/*     */ 
/* 245 */         throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaFactorySourceUnrecognized", new Object[] { source.getClass().getName() }));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 252 */       this.fXMLSchemaLoader.loadGrammar(xmlInputSources);
/*     */     }
/*     */     catch (XNIException e)
/*     */     {
/* 256 */       throw Util.toSAXException(e);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 260 */       SAXParseException se = new SAXParseException(e.getMessage(), null, e);
/* 261 */       this.fErrorHandler.error(se);
/* 262 */       throw se;
/*     */     }
/*     */ 
/* 266 */     this.fXMLGrammarPoolWrapper.setGrammarPool(null);
/*     */ 
/* 269 */     int grammarCount = pool.getGrammarCount();
/* 270 */     AbstractXMLSchema schema = null;
/* 271 */     if (grammarCount > 1) {
/* 272 */       schema = new XMLSchema(new ReadOnlyGrammarPool(pool));
/*     */     }
/* 274 */     else if (grammarCount == 1) {
/* 275 */       Grammar[] grammars = pool.retrieveInitialGrammarSet("http://www.w3.org/2001/XMLSchema");
/* 276 */       schema = new SimpleXMLSchema(grammars[0]);
/*     */     }
/*     */     else {
/* 279 */       schema = new EmptyXMLSchema();
/*     */     }
/* 281 */     propagateFeatures(schema);
/* 282 */     propagateProperties(schema);
/* 283 */     return schema;
/*     */   }
/*     */ 
/*     */   public Schema newSchema() throws SAXException
/*     */   {
/* 288 */     AbstractXMLSchema schema = new WeakReferenceXMLSchema();
/* 289 */     propagateFeatures(schema);
/* 290 */     propagateProperties(schema);
/* 291 */     return schema; } 
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ifnonnull +24 -> 25
/*     */     //   4: new 256	java/lang/NullPointerException
/*     */     //   7: dup
/*     */     //   8: aload_0
/*     */     //   9: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   12: invokevirtual 443	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getLocale	()Ljava/util/Locale;
/*     */     //   15: ldc 2
/*     */     //   17: aconst_null
/*     */     //   18: invokestatic 450	com/sun/org/apache/xerces/internal/jaxp/validation/JAXPValidationMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   21: invokespecial 489	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
/*     */     //   24: athrow
/*     */     //   25: aload_1
/*     */     //   26: ldc 14
/*     */     //   28: invokevirtual 492	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   31: ifeq +26 -> 57
/*     */     //   34: aload_0
/*     */     //   35: getfield 425	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fSecurityManager	Lcom/sun/org/apache/xerces/internal/utils/XMLSecurityManager;
/*     */     //   38: ifnull +17 -> 55
/*     */     //   41: aload_0
/*     */     //   42: getfield 425	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fSecurityManager	Lcom/sun/org/apache/xerces/internal/utils/XMLSecurityManager;
/*     */     //   45: invokevirtual 473	com/sun/org/apache/xerces/internal/utils/XMLSecurityManager:isSecureProcessing	()Z
/*     */     //   48: ifeq +7 -> 55
/*     */     //   51: iconst_1
/*     */     //   52: goto +4 -> 56
/*     */     //   55: iconst_0
/*     */     //   56: ireturn
/*     */     //   57: aload_0
/*     */     //   58: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   61: aload_1
/*     */     //   62: invokevirtual 441	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getFeature	(Ljava/lang/String;)Z
/*     */     //   65: ireturn
/*     */     //   66: astore_2
/*     */     //   67: aload_2
/*     */     //   68: invokevirtual 482	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getIdentifier	()Ljava/lang/String;
/*     */     //   71: astore_3
/*     */     //   72: aload_2
/*     */     //   73: invokevirtual 481	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getType	()Lcom/sun/org/apache/xerces/internal/util/Status;
/*     */     //   76: getstatic 429	com/sun/org/apache/xerces/internal/util/Status:NOT_RECOGNIZED	Lcom/sun/org/apache/xerces/internal/util/Status;
/*     */     //   79: if_acmpne +31 -> 110
/*     */     //   82: new 270	org/xml/sax/SAXNotRecognizedException
/*     */     //   85: dup
/*     */     //   86: aload_0
/*     */     //   87: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   90: invokevirtual 443	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getLocale	()Ljava/util/Locale;
/*     */     //   93: ldc 9
/*     */     //   95: iconst_1
/*     */     //   96: anewarray 257	java/lang/Object
/*     */     //   99: dup
/*     */     //   100: iconst_0
/*     */     //   101: aload_3
/*     */     //   102: aastore
/*     */     //   103: invokestatic 470	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   106: invokespecial 506	org/xml/sax/SAXNotRecognizedException:<init>	(Ljava/lang/String;)V
/*     */     //   109: athrow
/*     */     //   110: new 271	org/xml/sax/SAXNotSupportedException
/*     */     //   113: dup
/*     */     //   114: aload_0
/*     */     //   115: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   118: invokevirtual 443	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getLocale	()Ljava/util/Locale;
/*     */     //   121: ldc 10
/*     */     //   123: iconst_1
/*     */     //   124: anewarray 257	java/lang/Object
/*     */     //   127: dup
/*     */     //   128: iconst_0
/*     */     //   129: aload_3
/*     */     //   130: aastore
/*     */     //   131: invokestatic 470	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   134: invokespecial 507	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*     */     //   137: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   57	65	66	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException } 
/*     */   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ifnonnull +24 -> 25
/*     */     //   4: new 256	java/lang/NullPointerException
/*     */     //   7: dup
/*     */     //   8: aload_0
/*     */     //   9: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   12: invokevirtual 443	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getLocale	()Ljava/util/Locale;
/*     */     //   15: ldc 3
/*     */     //   17: aconst_null
/*     */     //   18: invokestatic 450	com/sun/org/apache/xerces/internal/jaxp/validation/JAXPValidationMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   21: invokespecial 489	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
/*     */     //   24: athrow
/*     */     //   25: aload_1
/*     */     //   26: ldc 13
/*     */     //   28: invokevirtual 492	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   31: ifeq +8 -> 39
/*     */     //   34: aload_0
/*     */     //   35: getfield 425	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fSecurityManager	Lcom/sun/org/apache/xerces/internal/utils/XMLSecurityManager;
/*     */     //   38: areturn
/*     */     //   39: aload_1
/*     */     //   40: ldc 12
/*     */     //   42: invokevirtual 492	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   45: ifeq +31 -> 76
/*     */     //   48: new 271	org/xml/sax/SAXNotSupportedException
/*     */     //   51: dup
/*     */     //   52: aload_0
/*     */     //   53: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   56: invokevirtual 443	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getLocale	()Ljava/util/Locale;
/*     */     //   59: ldc 20
/*     */     //   61: iconst_1
/*     */     //   62: anewarray 257	java/lang/Object
/*     */     //   65: dup
/*     */     //   66: iconst_0
/*     */     //   67: aload_1
/*     */     //   68: aastore
/*     */     //   69: invokestatic 470	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   72: invokespecial 507	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*     */     //   75: athrow
/*     */     //   76: aload_0
/*     */     //   77: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   80: aload_1
/*     */     //   81: invokevirtual 444	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getProperty	(Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   84: areturn
/*     */     //   85: astore_2
/*     */     //   86: aload_2
/*     */     //   87: invokevirtual 482	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getIdentifier	()Ljava/lang/String;
/*     */     //   90: astore_3
/*     */     //   91: aload_2
/*     */     //   92: invokevirtual 481	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException:getType	()Lcom/sun/org/apache/xerces/internal/util/Status;
/*     */     //   95: getstatic 429	com/sun/org/apache/xerces/internal/util/Status:NOT_RECOGNIZED	Lcom/sun/org/apache/xerces/internal/util/Status;
/*     */     //   98: if_acmpne +31 -> 129
/*     */     //   101: new 270	org/xml/sax/SAXNotRecognizedException
/*     */     //   104: dup
/*     */     //   105: aload_0
/*     */     //   106: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   109: invokevirtual 443	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getLocale	()Ljava/util/Locale;
/*     */     //   112: ldc 19
/*     */     //   114: iconst_1
/*     */     //   115: anewarray 257	java/lang/Object
/*     */     //   118: dup
/*     */     //   119: iconst_0
/*     */     //   120: aload_3
/*     */     //   121: aastore
/*     */     //   122: invokestatic 470	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   125: invokespecial 506	org/xml/sax/SAXNotRecognizedException:<init>	(Ljava/lang/String;)V
/*     */     //   128: athrow
/*     */     //   129: new 271	org/xml/sax/SAXNotSupportedException
/*     */     //   132: dup
/*     */     //   133: aload_0
/*     */     //   134: getfield 421	com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory:fXMLSchemaLoader	Lcom/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader;
/*     */     //   137: invokevirtual 443	com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader:getLocale	()Ljava/util/Locale;
/*     */     //   140: ldc 20
/*     */     //   142: iconst_1
/*     */     //   143: anewarray 257	java/lang/Object
/*     */     //   146: dup
/*     */     //   147: iconst_0
/*     */     //   148: aload_3
/*     */     //   149: aastore
/*     */     //   150: invokestatic 470	com/sun/org/apache/xerces/internal/util/SAXMessageFormatter:formatMessage	(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   153: invokespecial 507	org/xml/sax/SAXNotSupportedException:<init>	(Ljava/lang/String;)V
/*     */     //   156: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   76	84	85	com/sun/org/apache/xerces/internal/xni/parser/XMLConfigurationException } 
/* 355 */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException { if (name == null) {
/* 356 */       throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "FeatureNameNull", null));
/*     */     }
/*     */ 
/* 359 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 360 */       if ((System.getSecurityManager() != null) && (!value)) {
/* 361 */         throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(null, "jaxp-secureprocessing-feature", null));
/*     */       }
/*     */ 
/* 366 */       this.fSecurityManager.setSecureProcessing(value);
/* 367 */       if ((value) && 
/* 368 */         (Constants.IS_JDK8_OR_ABOVE)) {
/* 369 */         this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, XMLSecurityPropertyManager.State.FSP, "");
/*     */ 
/* 371 */         this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA, XMLSecurityPropertyManager.State.FSP, "");
/*     */       }
/*     */ 
/* 376 */       this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/* 377 */       return;
/* 378 */     }if (name.equals("http://www.oracle.com/feature/use-service-mechanism"))
/*     */     {
/* 380 */       if (System.getSecurityManager() != null)
/* 381 */         return;
/*     */     }
/*     */     try {
/* 384 */       this.fXMLSchemaLoader.setFeature(name, value);
/*     */     }
/*     */     catch (XMLConfigurationException e) {
/* 387 */       String identifier = e.getIdentifier();
/* 388 */       if (e.getType() == Status.NOT_RECOGNIZED) {
/* 389 */         throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-recognized", new Object[] { identifier }));
/*     */       }
/*     */ 
/* 394 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-supported", new Object[] { identifier }));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object object)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 403 */     if (name == null) {
/* 404 */       throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "ProperyNameNull", null));
/*     */     }
/*     */ 
/* 407 */     if (name.equals("http://apache.org/xml/properties/security-manager")) {
/* 408 */       this.fSecurityManager = XMLSecurityManager.convert(object, this.fSecurityManager);
/* 409 */       this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/* 410 */       return;
/* 411 */     }if (name.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
/* 412 */       if (object == null)
/* 413 */         this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
/*     */       else {
/* 415 */         this.fSecurityPropertyMgr = ((XMLSecurityPropertyManager)object);
/*     */       }
/* 417 */       this.fXMLSchemaLoader.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/* 418 */       return;
/*     */     }
/* 420 */     if (name.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
/* 421 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[] { name }));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 427 */       if ((this.fSecurityManager == null) || (!this.fSecurityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, object)))
/*     */       {
/* 430 */         if ((this.fSecurityPropertyMgr == null) || (!this.fSecurityPropertyMgr.setValue(name, XMLSecurityPropertyManager.State.APIPROPERTY, object)))
/*     */         {
/* 433 */           this.fXMLSchemaLoader.setProperty(name, object);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (XMLConfigurationException e) {
/* 438 */       String identifier = e.getIdentifier();
/* 439 */       if (e.getType() == Status.NOT_RECOGNIZED) {
/* 440 */         throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-recognized", new Object[] { identifier }));
/*     */       }
/*     */ 
/* 445 */       throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[] { identifier }));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void propagateFeatures(AbstractXMLSchema schema)
/*     */   {
/* 453 */     schema.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", (this.fSecurityManager != null) && (this.fSecurityManager.isSecureProcessing()));
/*     */ 
/* 455 */     schema.setFeature("http://www.oracle.com/feature/use-service-mechanism", this.fUseServicesMechanism);
/* 456 */     String[] features = this.fXMLSchemaLoader.getRecognizedFeatures();
/* 457 */     for (int i = 0; i < features.length; i++) {
/* 458 */       boolean state = this.fXMLSchemaLoader.getFeature(features[i]);
/* 459 */       schema.setFeature(features[i], state);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void propagateProperties(AbstractXMLSchema schema) {
/* 464 */     String[] properties = this.fXMLSchemaLoader.getRecognizedProperties();
/* 465 */     for (int i = 0; i < properties.length; i++) {
/* 466 */       Object state = this.fXMLSchemaLoader.getProperty(properties[i]);
/* 467 */       schema.setProperty(properties[i], state);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class XMLGrammarPoolImplExtension extends XMLGrammarPoolImpl
/*     */   {
/*     */     public XMLGrammarPoolImplExtension()
/*     */     {
/*     */     }
/*     */ 
/*     */     public XMLGrammarPoolImplExtension(int initialCapacity)
/*     */     {
/* 485 */       super();
/*     */     }
/*     */ 
/*     */     int getGrammarCount()
/*     */     {
/* 490 */       return this.fGrammarCount;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class XMLGrammarPoolWrapper
/*     */     implements XMLGrammarPool
/*     */   {
/*     */     private XMLGrammarPool fGrammarPool;
/*     */ 
/*     */     public Grammar[] retrieveInitialGrammarSet(String grammarType)
/*     */     {
/* 507 */       return this.fGrammarPool.retrieveInitialGrammarSet(grammarType);
/*     */     }
/*     */ 
/*     */     public void cacheGrammars(String grammarType, Grammar[] grammars) {
/* 511 */       this.fGrammarPool.cacheGrammars(grammarType, grammars);
/*     */     }
/*     */ 
/*     */     public Grammar retrieveGrammar(XMLGrammarDescription desc) {
/* 515 */       return this.fGrammarPool.retrieveGrammar(desc);
/*     */     }
/*     */ 
/*     */     public void lockPool() {
/* 519 */       this.fGrammarPool.lockPool();
/*     */     }
/*     */ 
/*     */     public void unlockPool() {
/* 523 */       this.fGrammarPool.unlockPool();
/*     */     }
/*     */ 
/*     */     public void clear() {
/* 527 */       this.fGrammarPool.clear();
/*     */     }
/*     */ 
/*     */     void setGrammarPool(XMLGrammarPool grammarPool)
/*     */     {
/* 535 */       this.fGrammarPool = grammarPool;
/*     */     }
/*     */ 
/*     */     XMLGrammarPool getGrammarPool() {
/* 539 */       return this.fGrammarPool;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory
 * JD-Core Version:    0.6.2
 */