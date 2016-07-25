/*     */ package com.sun.org.apache.xerces.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer;
/*     */ import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.Status;
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
/*     */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.PSVIProvider;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.HandlerBase;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class SAXParserImpl extends javax.xml.parsers.SAXParser
/*     */   implements JAXPConstants, PSVIProvider
/*     */ {
/*     */   private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
/*     */   private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
/*     */   private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
/*     */   private static final String XMLSCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
/*     */   private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*     */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*     */   private final JAXPSAXParser xmlReader;
/* 100 */   private String schemaLanguage = null;
/*     */   private final Schema grammar;
/*     */   private final XMLComponent fSchemaValidator;
/*     */   private final XMLComponentManager fSchemaValidatorComponentManager;
/*     */   private final ValidationManager fSchemaValidationManager;
/*     */   private final UnparsedEntityHandler fUnparsedEntityHandler;
/*     */   private final ErrorHandler fInitErrorHandler;
/*     */   private final EntityResolver fInitEntityResolver;
/*     */   private final XMLSecurityManager fSecurityManager;
/*     */   private final XMLSecurityPropertyManager fSecurityPropertyMgr;
/*     */ 
/*     */   SAXParserImpl(SAXParserFactoryImpl spf, Hashtable features)
/*     */     throws SAXException
/*     */   {
/* 123 */     this(spf, features, false);
/*     */   }
/*     */ 
/*     */   SAXParserImpl(SAXParserFactoryImpl spf, Hashtable features, boolean secureProcessing)
/*     */     throws SAXException
/*     */   {
/* 133 */     this.fSecurityManager = new XMLSecurityManager(secureProcessing);
/* 134 */     this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
/*     */ 
/* 136 */     this.xmlReader = new JAXPSAXParser(this, this.fSecurityPropertyMgr, this.fSecurityManager);
/*     */ 
/* 141 */     this.xmlReader.setFeature0("http://xml.org/sax/features/namespaces", spf.isNamespaceAware());
/*     */ 
/* 146 */     this.xmlReader.setFeature0("http://xml.org/sax/features/namespace-prefixes", !spf.isNamespaceAware());
/*     */ 
/* 151 */     if (spf.isXIncludeAware()) {
/* 152 */       this.xmlReader.setFeature0("http://apache.org/xml/features/xinclude", true);
/*     */     }
/*     */ 
/* 155 */     this.xmlReader.setProperty0("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/*     */ 
/* 157 */     this.xmlReader.setProperty0("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/*     */ 
/* 159 */     if (secureProcessing)
/*     */     {
/* 166 */       if (features != null)
/*     */       {
/* 168 */         Object temp = features.get("http://javax.xml.XMLConstants/feature/secure-processing");
/* 169 */         if (temp != null) {
/* 170 */           boolean value = ((Boolean)temp).booleanValue();
/* 171 */           if ((value) && (Constants.IS_JDK8_OR_ABOVE)) {
/* 172 */             this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, XMLSecurityPropertyManager.State.FSP, "");
/*     */ 
/* 174 */             this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA, XMLSecurityPropertyManager.State.FSP, "");
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 182 */     setFeatures(features);
/*     */ 
/* 187 */     if (spf.isValidating()) {
/* 188 */       this.fInitErrorHandler = new DefaultValidationErrorHandler(this.xmlReader.getLocale());
/* 189 */       this.xmlReader.setErrorHandler(this.fInitErrorHandler);
/*     */     }
/*     */     else {
/* 192 */       this.fInitErrorHandler = this.xmlReader.getErrorHandler();
/*     */     }
/* 194 */     this.xmlReader.setFeature0("http://xml.org/sax/features/validation", spf.isValidating());
/*     */ 
/* 197 */     this.grammar = spf.getSchema();
/* 198 */     if (this.grammar != null) {
/* 199 */       XMLParserConfiguration config = this.xmlReader.getXMLParserConfiguration();
/* 200 */       XMLComponent validatorComponent = null;
/*     */ 
/* 202 */       if ((this.grammar instanceof XSGrammarPoolContainer)) {
/* 203 */         validatorComponent = new XMLSchemaValidator();
/* 204 */         this.fSchemaValidationManager = new ValidationManager();
/* 205 */         this.fUnparsedEntityHandler = new UnparsedEntityHandler(this.fSchemaValidationManager);
/* 206 */         config.setDTDHandler(this.fUnparsedEntityHandler);
/* 207 */         this.fUnparsedEntityHandler.setDTDHandler(this.xmlReader);
/* 208 */         this.xmlReader.setDTDSource(this.fUnparsedEntityHandler);
/* 209 */         this.fSchemaValidatorComponentManager = new SchemaValidatorConfiguration(config, (XSGrammarPoolContainer)this.grammar, this.fSchemaValidationManager);
/*     */       }
/*     */       else
/*     */       {
/* 214 */         validatorComponent = new JAXPValidatorComponent(this.grammar.newValidatorHandler());
/* 215 */         this.fSchemaValidationManager = null;
/* 216 */         this.fUnparsedEntityHandler = null;
/* 217 */         this.fSchemaValidatorComponentManager = config;
/*     */       }
/* 219 */       config.addRecognizedFeatures(validatorComponent.getRecognizedFeatures());
/* 220 */       config.addRecognizedProperties(validatorComponent.getRecognizedProperties());
/* 221 */       config.setDocumentHandler((XMLDocumentHandler)validatorComponent);
/* 222 */       ((XMLDocumentSource)validatorComponent).setDocumentHandler(this.xmlReader);
/* 223 */       this.xmlReader.setDocumentSource((XMLDocumentSource)validatorComponent);
/* 224 */       this.fSchemaValidator = validatorComponent;
/*     */     }
/*     */     else {
/* 227 */       this.fSchemaValidationManager = null;
/* 228 */       this.fUnparsedEntityHandler = null;
/* 229 */       this.fSchemaValidatorComponentManager = null;
/* 230 */       this.fSchemaValidator = null;
/*     */     }
/*     */ 
/* 234 */     this.fInitEntityResolver = this.xmlReader.getEntityResolver();
/*     */   }
/*     */ 
/*     */   private void setFeatures(Hashtable features)
/*     */     throws SAXNotSupportedException, SAXNotRecognizedException
/*     */   {
/* 246 */     if (features != null) {
/* 247 */       Iterator entries = features.entrySet().iterator();
/* 248 */       while (entries.hasNext()) {
/* 249 */         Map.Entry entry = (Map.Entry)entries.next();
/* 250 */         String feature = (String)entry.getKey();
/* 251 */         boolean value = ((Boolean)entry.getValue()).booleanValue();
/* 252 */         this.xmlReader.setFeature0(feature, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Parser getParser()
/*     */     throws SAXException
/*     */   {
/* 260 */     return this.xmlReader;
/*     */   }
/*     */ 
/*     */   public XMLReader getXMLReader()
/*     */   {
/* 268 */     return this.xmlReader;
/*     */   }
/*     */ 
/*     */   public boolean isNamespaceAware() {
/*     */     try {
/* 273 */       return this.xmlReader.getFeature("http://xml.org/sax/features/namespaces");
/*     */     }
/*     */     catch (SAXException x) {
/* 276 */       throw new IllegalStateException(x.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isValidating() {
/*     */     try {
/* 282 */       return this.xmlReader.getFeature("http://xml.org/sax/features/validation");
/*     */     }
/*     */     catch (SAXException x) {
/* 285 */       throw new IllegalStateException(x.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isXIncludeAware()
/*     */   {
/*     */     try
/*     */     {
/* 295 */       return this.xmlReader.getFeature("http://apache.org/xml/features/xinclude");
/*     */     } catch (SAXException exc) {
/*     */     }
/* 298 */     return false;
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 308 */     this.xmlReader.setProperty(name, value);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 317 */     return this.xmlReader.getProperty(name);
/*     */   }
/*     */ 
/*     */   public void parse(InputSource is, DefaultHandler dh) throws SAXException, IOException
/*     */   {
/* 322 */     if (is == null) {
/* 323 */       throw new IllegalArgumentException();
/*     */     }
/* 325 */     if (dh != null) {
/* 326 */       this.xmlReader.setContentHandler(dh);
/* 327 */       this.xmlReader.setEntityResolver(dh);
/* 328 */       this.xmlReader.setErrorHandler(dh);
/* 329 */       this.xmlReader.setDTDHandler(dh);
/* 330 */       this.xmlReader.setDocumentHandler(null);
/*     */     }
/* 332 */     this.xmlReader.parse(is);
/*     */   }
/*     */ 
/*     */   public void parse(InputSource is, HandlerBase hb) throws SAXException, IOException
/*     */   {
/* 337 */     if (is == null) {
/* 338 */       throw new IllegalArgumentException();
/*     */     }
/* 340 */     if (hb != null) {
/* 341 */       this.xmlReader.setDocumentHandler(hb);
/* 342 */       this.xmlReader.setEntityResolver(hb);
/* 343 */       this.xmlReader.setErrorHandler(hb);
/* 344 */       this.xmlReader.setDTDHandler(hb);
/* 345 */       this.xmlReader.setContentHandler(null);
/*     */     }
/* 347 */     this.xmlReader.parse(is);
/*     */   }
/*     */ 
/*     */   public Schema getSchema() {
/* 351 */     return this.grammar;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*     */     try {
/* 357 */       this.xmlReader.restoreInitState();
/*     */     }
/*     */     catch (SAXException exc)
/*     */     {
/*     */     }
/*     */ 
/* 365 */     this.xmlReader.setContentHandler(null);
/* 366 */     this.xmlReader.setDTDHandler(null);
/* 367 */     if (this.xmlReader.getErrorHandler() != this.fInitErrorHandler) {
/* 368 */       this.xmlReader.setErrorHandler(this.fInitErrorHandler);
/*     */     }
/* 370 */     if (this.xmlReader.getEntityResolver() != this.fInitEntityResolver)
/* 371 */       this.xmlReader.setEntityResolver(this.fInitEntityResolver);
/*     */   }
/*     */ 
/*     */   public ElementPSVI getElementPSVI()
/*     */   {
/* 380 */     return this.xmlReader.getElementPSVI();
/*     */   }
/*     */ 
/*     */   public AttributePSVI getAttributePSVI(int index) {
/* 384 */     return this.xmlReader.getAttributePSVI(index);
/*     */   }
/*     */ 
/*     */   public AttributePSVI getAttributePSVIByName(String uri, String localname) {
/* 388 */     return this.xmlReader.getAttributePSVIByName(uri, localname);
/*     */   }
/*     */ 
/*     */   public static class JAXPSAXParser extends com.sun.org.apache.xerces.internal.parsers.SAXParser
/*     */   {
/* 398 */     private final HashMap fInitFeatures = new HashMap();
/* 399 */     private final HashMap fInitProperties = new HashMap();
/*     */     private final SAXParserImpl fSAXParser;
/*     */     private XMLSecurityManager fSecurityManager;
/*     */     private XMLSecurityPropertyManager fSecurityPropertyMgr;
/*     */ 
/*     */     public JAXPSAXParser()
/*     */     {
/* 406 */       this(null, null, null);
/*     */     }
/*     */ 
/*     */     JAXPSAXParser(SAXParserImpl saxParser, XMLSecurityPropertyManager securityPropertyMgr, XMLSecurityManager securityManager)
/*     */     {
/* 412 */       this.fSAXParser = saxParser;
/* 413 */       this.fSecurityManager = securityManager;
/* 414 */       this.fSecurityPropertyMgr = securityPropertyMgr;
/*     */ 
/* 419 */       if (this.fSecurityManager == null) {
/* 420 */         this.fSecurityManager = new XMLSecurityManager(true);
/*     */         try {
/* 422 */           super.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/*     */         } catch (SAXException e) {
/* 424 */           throw new UnsupportedOperationException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[] { "http://apache.org/xml/properties/security-manager" }), e);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 429 */       if (this.fSecurityPropertyMgr == null) {
/* 430 */         this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
/*     */         try {
/* 432 */           super.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/*     */         } catch (SAXException e) {
/* 434 */           throw new UnsupportedOperationException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[] { "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" }), e);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public synchronized void setFeature(String name, boolean value)
/*     */       throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 448 */       if (name == null)
/*     */       {
/* 450 */         throw new NullPointerException();
/*     */       }
/* 452 */       if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/*     */         try {
/* 454 */           this.fSecurityManager.setSecureProcessing(value);
/* 455 */           setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/*     */         }
/*     */         catch (SAXNotRecognizedException exc)
/*     */         {
/* 460 */           if (value) {
/* 461 */             throw exc;
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (SAXNotSupportedException exc)
/*     */         {
/* 467 */           if (value) {
/* 468 */             throw exc;
/*     */           }
/*     */         }
/* 471 */         return;
/*     */       }
/* 473 */       if (!this.fInitFeatures.containsKey(name)) {
/* 474 */         boolean current = super.getFeature(name);
/* 475 */         this.fInitFeatures.put(name, current ? Boolean.TRUE : Boolean.FALSE);
/*     */       }
/*     */ 
/* 478 */       if ((this.fSAXParser != null) && (this.fSAXParser.fSchemaValidator != null)) {
/* 479 */         setSchemaValidatorFeature(name, value);
/*     */       }
/* 481 */       super.setFeature(name, value);
/*     */     }
/*     */ 
/*     */     public synchronized boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 486 */       if (name == null)
/*     */       {
/* 488 */         throw new NullPointerException();
/*     */       }
/* 490 */       if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 491 */         return this.fSecurityManager.isSecureProcessing();
/*     */       }
/* 493 */       return super.getFeature(name);
/*     */     }
/*     */ 
/*     */     public synchronized void setProperty(String name, Object value)
/*     */       throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 503 */       if (name == null)
/*     */       {
/* 505 */         throw new NullPointerException();
/*     */       }
/* 507 */       if (this.fSAXParser != null)
/*     */       {
/* 509 */         if ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(name))
/*     */         {
/* 512 */           if (this.fSAXParser.grammar != null) {
/* 513 */             throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-already-specified", new Object[] { name }));
/*     */           }
/*     */ 
/* 516 */           if ("http://www.w3.org/2001/XMLSchema".equals(value))
/*     */           {
/* 518 */             if (this.fSAXParser.isValidating()) {
/* 519 */               this.fSAXParser.schemaLanguage = "http://www.w3.org/2001/XMLSchema";
/* 520 */               setFeature("http://apache.org/xml/features/validation/schema", true);
/*     */ 
/* 523 */               if (!this.fInitProperties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaLanguage")) {
/* 524 */                 this.fInitProperties.put("http://java.sun.com/xml/jaxp/properties/schemaLanguage", super.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage"));
/*     */               }
/* 526 */               super.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
/*     */             }
/*     */ 
/*     */           }
/* 530 */           else if (value == null) {
/* 531 */             this.fSAXParser.schemaLanguage = null;
/* 532 */             setFeature("http://apache.org/xml/features/validation/schema", false);
/*     */           }
/*     */           else
/*     */           {
/* 538 */             throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-not-supported", null));
/*     */           }
/*     */ 
/* 541 */           return;
/*     */         }
/* 543 */         if ("http://java.sun.com/xml/jaxp/properties/schemaSource".equals(name))
/*     */         {
/* 546 */           if (this.fSAXParser.grammar != null) {
/* 547 */             throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-already-specified", new Object[] { name }));
/*     */           }
/*     */ 
/* 550 */           String val = (String)getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
/* 551 */           if ((val != null) && ("http://www.w3.org/2001/XMLSchema".equals(val))) {
/* 552 */             if (!this.fInitProperties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
/* 553 */               this.fInitProperties.put("http://java.sun.com/xml/jaxp/properties/schemaSource", super.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource"));
/*     */             }
/* 555 */             super.setProperty(name, value);
/*     */           }
/*     */           else {
/* 558 */             throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "jaxp-order-not-supported", new Object[] { "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://java.sun.com/xml/jaxp/properties/schemaSource" }));
/*     */           }
/*     */ 
/* 563 */           return;
/*     */         }
/*     */       }
/*     */ 
/* 567 */       if ((this.fSAXParser != null) && (this.fSAXParser.fSchemaValidator != null)) {
/* 568 */         setSchemaValidatorProperty(name, value);
/*     */       }
/*     */ 
/* 572 */       if ((this.fSecurityManager == null) || (!this.fSecurityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, value)))
/*     */       {
/* 575 */         if ((this.fSecurityPropertyMgr == null) || (!this.fSecurityPropertyMgr.setValue(name, XMLSecurityPropertyManager.State.APIPROPERTY, value)))
/*     */         {
/* 578 */           if (!this.fInitProperties.containsKey(name)) {
/* 579 */             this.fInitProperties.put(name, super.getProperty(name));
/*     */           }
/* 581 */           super.setProperty(name, value);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public synchronized Object getProperty(String name)
/*     */       throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 589 */       if (name == null)
/*     */       {
/* 591 */         throw new NullPointerException();
/*     */       }
/* 593 */       if ((this.fSAXParser != null) && ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(name)))
/*     */       {
/* 595 */         return this.fSAXParser.schemaLanguage;
/*     */       }
/*     */ 
/* 599 */       String propertyValue = this.fSecurityManager != null ? this.fSecurityManager.getLimitAsString(name) : null;
/*     */ 
/* 601 */       if (propertyValue != null) {
/* 602 */         return propertyValue;
/*     */       }
/* 604 */       propertyValue = this.fSecurityPropertyMgr != null ? this.fSecurityPropertyMgr.getValue(name) : null;
/*     */ 
/* 606 */       if (propertyValue != null) {
/* 607 */         return propertyValue;
/*     */       }
/*     */ 
/* 611 */       return super.getProperty(name);
/*     */     }
/*     */ 
/*     */     synchronized void restoreInitState()
/*     */       throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 617 */       if (!this.fInitFeatures.isEmpty()) {
/* 618 */         Iterator iter = this.fInitFeatures.entrySet().iterator();
/* 619 */         while (iter.hasNext()) {
/* 620 */           Map.Entry entry = (Map.Entry)iter.next();
/* 621 */           String name = (String)entry.getKey();
/* 622 */           boolean value = ((Boolean)entry.getValue()).booleanValue();
/* 623 */           super.setFeature(name, value);
/*     */         }
/* 625 */         this.fInitFeatures.clear();
/*     */       }
/* 627 */       if (!this.fInitProperties.isEmpty()) {
/* 628 */         Iterator iter = this.fInitProperties.entrySet().iterator();
/* 629 */         while (iter.hasNext()) {
/* 630 */           Map.Entry entry = (Map.Entry)iter.next();
/* 631 */           String name = (String)entry.getKey();
/* 632 */           Object value = entry.getValue();
/* 633 */           super.setProperty(name, value);
/*     */         }
/* 635 */         this.fInitProperties.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void parse(InputSource inputSource) throws SAXException, IOException
/*     */     {
/* 641 */       if ((this.fSAXParser != null) && (this.fSAXParser.fSchemaValidator != null)) {
/* 642 */         if (this.fSAXParser.fSchemaValidationManager != null) {
/* 643 */           this.fSAXParser.fSchemaValidationManager.reset();
/* 644 */           this.fSAXParser.fUnparsedEntityHandler.reset();
/*     */         }
/* 646 */         resetSchemaValidator();
/*     */       }
/* 648 */       super.parse(inputSource);
/*     */     }
/*     */ 
/*     */     public void parse(String systemId) throws SAXException, IOException
/*     */     {
/* 653 */       if ((this.fSAXParser != null) && (this.fSAXParser.fSchemaValidator != null)) {
/* 654 */         if (this.fSAXParser.fSchemaValidationManager != null) {
/* 655 */           this.fSAXParser.fSchemaValidationManager.reset();
/* 656 */           this.fSAXParser.fUnparsedEntityHandler.reset();
/*     */         }
/* 658 */         resetSchemaValidator();
/*     */       }
/* 660 */       super.parse(systemId);
/*     */     }
/*     */ 
/*     */     XMLParserConfiguration getXMLParserConfiguration() {
/* 664 */       return this.fConfiguration;
/*     */     }
/*     */ 
/*     */     void setFeature0(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 669 */       super.setFeature(name, value);
/*     */     }
/*     */ 
/*     */     boolean getFeature0(String name) throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 674 */       return super.getFeature(name);
/*     */     }
/*     */ 
/*     */     void setProperty0(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 679 */       super.setProperty(name, value);
/*     */     }
/*     */ 
/*     */     Object getProperty0(String name) throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/* 684 */       return super.getProperty(name);
/*     */     }
/*     */ 
/*     */     Locale getLocale() {
/* 688 */       return this.fConfiguration.getLocale();
/*     */     }
/*     */ 
/*     */     private void setSchemaValidatorFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/*     */       try {
/* 694 */         this.fSAXParser.fSchemaValidator.setFeature(name, value);
/*     */       }
/*     */       catch (XMLConfigurationException e)
/*     */       {
/* 698 */         String identifier = e.getIdentifier();
/* 699 */         if (e.getType() == Status.NOT_RECOGNIZED) {
/* 700 */           throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[] { identifier }));
/*     */         }
/*     */ 
/* 705 */         throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[] { identifier }));
/*     */       }
/*     */     }
/*     */ 
/*     */     private void setSchemaValidatorProperty(String name, Object value)
/*     */       throws SAXNotRecognizedException, SAXNotSupportedException
/*     */     {
/*     */       try
/*     */       {
/* 715 */         this.fSAXParser.fSchemaValidator.setProperty(name, value);
/*     */       }
/*     */       catch (XMLConfigurationException e)
/*     */       {
/* 719 */         String identifier = e.getIdentifier();
/* 720 */         if (e.getType() == Status.NOT_RECOGNIZED) {
/* 721 */           throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[] { identifier }));
/*     */         }
/*     */ 
/* 726 */         throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[] { identifier }));
/*     */       }
/*     */     }
/*     */ 
/*     */     private void resetSchemaValidator()
/*     */       throws SAXException
/*     */     {
/*     */       try
/*     */       {
/* 735 */         this.fSAXParser.fSchemaValidator.reset(this.fSAXParser.fSchemaValidatorComponentManager);
/*     */       }
/*     */       catch (XMLConfigurationException e)
/*     */       {
/* 739 */         throw new SAXException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl
 * JD-Core Version:    0.6.2
 */