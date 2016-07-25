/*     */ package com.sun.org.apache.xerces.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer;
/*     */ import com.sun.org.apache.xerces.internal.parsers.DOMParser;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.State;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.Property;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.State;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public class DocumentBuilderImpl extends DocumentBuilder
/*     */   implements JAXPConstants
/*     */ {
/*     */   private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
/*     */   private static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
/*     */   private static final String CREATE_ENTITY_REF_NODES_FEATURE = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
/*     */   private static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
/*     */   private static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
/*     */   private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
/*     */   private static final String XMLSCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
/*     */   private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*     */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*     */   public static final String ACCESS_EXTERNAL_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";
/*     */   public static final String ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";
/*     */   private final DOMParser domParser;
/*     */   private final Schema grammar;
/*     */   private final XMLComponent fSchemaValidator;
/*     */   private final XMLComponentManager fSchemaValidatorComponentManager;
/*     */   private final ValidationManager fSchemaValidationManager;
/*     */   private final UnparsedEntityHandler fUnparsedEntityHandler;
/*     */   private final ErrorHandler fInitErrorHandler;
/*     */   private final EntityResolver fInitEntityResolver;
/*     */   private XMLSecurityManager fSecurityManager;
/*     */   private XMLSecurityPropertyManager fSecurityPropertyMgr;
/*     */ 
/*     */   DocumentBuilderImpl(DocumentBuilderFactoryImpl dbf, Hashtable dbfAttrs, Hashtable features)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 131 */     this(dbf, dbfAttrs, features, false);
/*     */   }
/*     */ 
/*     */   DocumentBuilderImpl(DocumentBuilderFactoryImpl dbf, Hashtable dbfAttrs, Hashtable features, boolean secureProcessing)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 137 */     this.domParser = new DOMParser();
/*     */ 
/* 142 */     if (dbf.isValidating()) {
/* 143 */       this.fInitErrorHandler = new DefaultValidationErrorHandler(this.domParser.getXMLParserConfiguration().getLocale());
/* 144 */       setErrorHandler(this.fInitErrorHandler);
/*     */     }
/*     */     else {
/* 147 */       this.fInitErrorHandler = this.domParser.getErrorHandler();
/*     */     }
/*     */ 
/* 150 */     this.domParser.setFeature("http://xml.org/sax/features/validation", dbf.isValidating());
/*     */ 
/* 153 */     this.domParser.setFeature("http://xml.org/sax/features/namespaces", dbf.isNamespaceAware());
/*     */ 
/* 156 */     this.domParser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", !dbf.isIgnoringElementContentWhitespace());
/*     */ 
/* 158 */     this.domParser.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", !dbf.isExpandEntityReferences());
/*     */ 
/* 160 */     this.domParser.setFeature("http://apache.org/xml/features/include-comments", !dbf.isIgnoringComments());
/*     */ 
/* 162 */     this.domParser.setFeature("http://apache.org/xml/features/create-cdata-nodes", !dbf.isCoalescing());
/*     */ 
/* 168 */     if (dbf.isXIncludeAware()) {
/* 169 */       this.domParser.setFeature("http://apache.org/xml/features/xinclude", true);
/*     */     }
/*     */ 
/* 172 */     this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
/* 173 */     this.domParser.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/*     */ 
/* 175 */     this.fSecurityManager = new XMLSecurityManager(secureProcessing);
/* 176 */     this.domParser.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/*     */ 
/* 178 */     if (secureProcessing)
/*     */     {
/* 184 */       if (features != null) {
/* 185 */         Object temp = features.get("http://javax.xml.XMLConstants/feature/secure-processing");
/* 186 */         if (temp != null) {
/* 187 */           boolean value = ((Boolean)temp).booleanValue();
/* 188 */           if ((value) && (Constants.IS_JDK8_OR_ABOVE)) {
/* 189 */             this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, XMLSecurityPropertyManager.State.FSP, "");
/*     */ 
/* 191 */             this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA, XMLSecurityPropertyManager.State.FSP, "");
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 198 */     this.grammar = dbf.getSchema();
/* 199 */     if (this.grammar != null) {
/* 200 */       XMLParserConfiguration config = this.domParser.getXMLParserConfiguration();
/* 201 */       XMLComponent validatorComponent = null;
/*     */ 
/* 203 */       if ((this.grammar instanceof XSGrammarPoolContainer)) {
/* 204 */         validatorComponent = new XMLSchemaValidator();
/* 205 */         this.fSchemaValidationManager = new ValidationManager();
/* 206 */         this.fUnparsedEntityHandler = new UnparsedEntityHandler(this.fSchemaValidationManager);
/* 207 */         config.setDTDHandler(this.fUnparsedEntityHandler);
/* 208 */         this.fUnparsedEntityHandler.setDTDHandler(this.domParser);
/* 209 */         this.domParser.setDTDSource(this.fUnparsedEntityHandler);
/* 210 */         this.fSchemaValidatorComponentManager = new SchemaValidatorConfiguration(config, (XSGrammarPoolContainer)this.grammar, this.fSchemaValidationManager);
/*     */       }
/*     */       else
/*     */       {
/* 215 */         validatorComponent = new JAXPValidatorComponent(this.grammar.newValidatorHandler());
/* 216 */         this.fSchemaValidationManager = null;
/* 217 */         this.fUnparsedEntityHandler = null;
/* 218 */         this.fSchemaValidatorComponentManager = config;
/*     */       }
/* 220 */       config.addRecognizedFeatures(validatorComponent.getRecognizedFeatures());
/* 221 */       config.addRecognizedProperties(validatorComponent.getRecognizedProperties());
/* 222 */       setFeatures(features);
/* 223 */       config.setDocumentHandler((XMLDocumentHandler)validatorComponent);
/* 224 */       ((XMLDocumentSource)validatorComponent).setDocumentHandler(this.domParser);
/* 225 */       this.domParser.setDocumentSource((XMLDocumentSource)validatorComponent);
/* 226 */       this.fSchemaValidator = validatorComponent;
/*     */     }
/*     */     else {
/* 229 */       this.fSchemaValidationManager = null;
/* 230 */       this.fUnparsedEntityHandler = null;
/* 231 */       this.fSchemaValidatorComponentManager = null;
/* 232 */       this.fSchemaValidator = null;
/* 233 */       setFeatures(features);
/*     */     }
/*     */ 
/* 237 */     setDocumentBuilderFactoryAttributes(dbfAttrs);
/*     */ 
/* 240 */     this.fInitEntityResolver = this.domParser.getEntityResolver();
/*     */   }
/*     */ 
/*     */   private void setFeatures(Hashtable features) throws SAXNotSupportedException, SAXNotRecognizedException
/*     */   {
/* 245 */     if (features != null) {
/* 246 */       Iterator entries = features.entrySet().iterator();
/* 247 */       while (entries.hasNext()) {
/* 248 */         Map.Entry entry = (Map.Entry)entries.next();
/* 249 */         String feature = (String)entry.getKey();
/* 250 */         boolean value = ((Boolean)entry.getValue()).booleanValue();
/* 251 */         this.domParser.setFeature(feature, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setDocumentBuilderFactoryAttributes(Hashtable dbfAttrs)
/*     */     throws SAXNotSupportedException, SAXNotRecognizedException
/*     */   {
/* 266 */     if (dbfAttrs == null)
/*     */     {
/* 268 */       return;
/*     */     }
/*     */ 
/* 271 */     Iterator entries = dbfAttrs.entrySet().iterator();
/* 272 */     while (entries.hasNext()) {
/* 273 */       Map.Entry entry = (Map.Entry)entries.next();
/* 274 */       String name = (String)entry.getKey();
/* 275 */       Object val = entry.getValue();
/* 276 */       if ((val instanceof Boolean))
/*     */       {
/* 278 */         this.domParser.setFeature(name, ((Boolean)val).booleanValue());
/*     */       }
/* 281 */       else if ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(name))
/*     */       {
/* 284 */         if (("http://www.w3.org/2001/XMLSchema".equals(val)) && 
/* 285 */           (isValidating())) {
/* 286 */           this.domParser.setFeature("http://apache.org/xml/features/validation/schema", true);
/*     */ 
/* 289 */           this.domParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
/*     */         }
/*     */       }
/* 292 */       else if ("http://java.sun.com/xml/jaxp/properties/schemaSource".equals(name)) {
/* 293 */         if (isValidating()) {
/* 294 */           String value = (String)dbfAttrs.get("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
/* 295 */           if ((value != null) && ("http://www.w3.org/2001/XMLSchema".equals(value)))
/* 296 */             this.domParser.setProperty(name, val);
/*     */           else {
/* 298 */             throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "jaxp-order-not-supported", new Object[] { "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://java.sun.com/xml/jaxp/properties/schemaSource" }));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/* 306 */       else if ((this.fSecurityManager == null) || (!this.fSecurityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, val)))
/*     */       {
/* 309 */         if ((this.fSecurityPropertyMgr == null) || (!this.fSecurityPropertyMgr.setValue(name, XMLSecurityPropertyManager.State.APIPROPERTY, val)))
/*     */         {
/* 312 */           this.domParser.setProperty(name, val);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Document newDocument()
/*     */   {
/* 327 */     return new DocumentImpl();
/*     */   }
/*     */ 
/*     */   public DOMImplementation getDOMImplementation() {
/* 331 */     return DOMImplementationImpl.getDOMImplementation();
/*     */   }
/*     */ 
/*     */   public Document parse(InputSource is) throws SAXException, IOException {
/* 335 */     if (is == null) {
/* 336 */       throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "jaxp-null-input-source", null));
/*     */     }
/*     */ 
/* 340 */     if (this.fSchemaValidator != null) {
/* 341 */       if (this.fSchemaValidationManager != null) {
/* 342 */         this.fSchemaValidationManager.reset();
/* 343 */         this.fUnparsedEntityHandler.reset();
/*     */       }
/* 345 */       resetSchemaValidator();
/*     */     }
/* 347 */     this.domParser.parse(is);
/* 348 */     Document doc = this.domParser.getDocument();
/* 349 */     this.domParser.dropDocumentReferences();
/* 350 */     return doc;
/*     */   }
/*     */ 
/*     */   public boolean isNamespaceAware() {
/*     */     try {
/* 355 */       return this.domParser.getFeature("http://xml.org/sax/features/namespaces");
/*     */     }
/*     */     catch (SAXException x) {
/* 358 */       throw new IllegalStateException(x.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isValidating() {
/*     */     try {
/* 364 */       return this.domParser.getFeature("http://xml.org/sax/features/validation");
/*     */     }
/*     */     catch (SAXException x) {
/* 367 */       throw new IllegalStateException(x.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isXIncludeAware()
/*     */   {
/*     */     try
/*     */     {
/* 377 */       return this.domParser.getFeature("http://apache.org/xml/features/xinclude");
/*     */     } catch (SAXException exc) {
/*     */     }
/* 380 */     return false;
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver er)
/*     */   {
/* 385 */     this.domParser.setEntityResolver(er);
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler eh) {
/* 389 */     this.domParser.setErrorHandler(eh);
/*     */   }
/*     */ 
/*     */   public Schema getSchema() {
/* 393 */     return this.grammar;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 398 */     if (this.domParser.getErrorHandler() != this.fInitErrorHandler) {
/* 399 */       this.domParser.setErrorHandler(this.fInitErrorHandler);
/*     */     }
/*     */ 
/* 402 */     if (this.domParser.getEntityResolver() != this.fInitEntityResolver)
/* 403 */       this.domParser.setEntityResolver(this.fInitEntityResolver);
/*     */   }
/*     */ 
/*     */   DOMParser getDOMParser()
/*     */   {
/* 409 */     return this.domParser;
/*     */   }
/*     */ 
/*     */   private void resetSchemaValidator() throws SAXException {
/*     */     try {
/* 414 */       this.fSchemaValidator.reset(this.fSchemaValidatorComponentManager);
/*     */     }
/*     */     catch (XMLConfigurationException e)
/*     */     {
/* 418 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderImpl
 * JD-Core Version:    0.6.2
 */