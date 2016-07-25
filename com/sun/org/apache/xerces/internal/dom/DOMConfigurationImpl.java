/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMErrorHandlerWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
/*      */ import com.sun.org.apache.xerces.internal.util.PropertyState;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.DOMConfiguration;
/*      */ import org.w3c.dom.DOMErrorHandler;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.DOMStringList;
/*      */ import org.w3c.dom.ls.LSResourceResolver;
/*      */ 
/*      */ public class DOMConfigurationImpl extends ParserConfigurationSettings
/*      */   implements XMLParserConfiguration, DOMConfiguration
/*      */ {
/*      */   protected static final String XERCES_VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String XERCES_NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String SCHEMA = "http://apache.org/xml/features/validation/schema";
/*      */   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
/*      */   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
/*      */   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
/*      */   protected static final String SEND_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
/*      */   protected static final String DTD_VALIDATOR_FACTORY_PROPERTY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
/*      */   protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
/*      */   protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*      */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
/*      */   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
/*      */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*      */   protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
/*      */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*      */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*      */   XMLDocumentHandler fDocumentHandler;
/*  174 */   protected short features = 0;
/*      */   protected static final short NAMESPACES = 1;
/*      */   protected static final short DTNORMALIZATION = 2;
/*      */   protected static final short ENTITIES = 4;
/*      */   protected static final short CDATA = 8;
/*      */   protected static final short SPLITCDATA = 16;
/*      */   protected static final short COMMENTS = 32;
/*      */   protected static final short VALIDATE = 64;
/*      */   protected static final short PSVI = 128;
/*      */   protected static final short WELLFORMED = 256;
/*      */   protected static final short NSDECL = 512;
/*      */   protected static final short INFOSET_TRUE_PARAMS = 801;
/*      */   protected static final short INFOSET_FALSE_PARAMS = 14;
/*      */   protected static final short INFOSET_MASK = 815;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected ArrayList fComponents;
/*      */   protected ValidationManager fValidationManager;
/*      */   protected Locale fLocale;
/*      */   protected XMLErrorReporter fErrorReporter;
/*  207 */   protected final DOMErrorHandlerWrapper fErrorHandlerWrapper = new DOMErrorHandlerWrapper();
/*      */   private DOMStringList fRecognizedParameters;
/*      */ 
/*      */   protected DOMConfigurationImpl()
/*      */   {
/*  221 */     this(null, null);
/*      */   }
/*      */ 
/*      */   protected DOMConfigurationImpl(SymbolTable symbolTable)
/*      */   {
/*  230 */     this(symbolTable, null);
/*      */   }
/*      */ 
/*      */   protected DOMConfigurationImpl(SymbolTable symbolTable, XMLComponentManager parentSettings)
/*      */   {
/*  242 */     super(parentSettings);
/*      */ 
/*  246 */     this.fFeatures = new HashMap();
/*  247 */     this.fProperties = new HashMap();
/*      */ 
/*  250 */     String[] recognizedFeatures = { "http://xml.org/sax/features/validation", "http://xml.org/sax/features/namespaces", "http://apache.org/xml/features/validation/schema", "http://apache.org/xml/features/validation/schema-full-checking", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/schema/normalized-value", "http://apache.org/xml/features/validation/schema/augment-psvi", "http://apache.org/xml/features/namespace-growth", "http://apache.org/xml/features/internal/tolerate-duplicates" };
/*      */ 
/*  261 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/*  264 */     setFeature("http://xml.org/sax/features/validation", false);
/*  265 */     setFeature("http://apache.org/xml/features/validation/schema", false);
/*  266 */     setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
/*  267 */     setFeature("http://apache.org/xml/features/validation/dynamic", false);
/*  268 */     setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
/*  269 */     setFeature("http://xml.org/sax/features/namespaces", true);
/*  270 */     setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
/*  271 */     setFeature("http://apache.org/xml/features/namespace-growth", false);
/*      */ 
/*  274 */     String[] recognizedProperties = { "http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/internal/grammar-pool", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation/schema/dv-factory", "http://apache.org/xml/properties/security-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" };
/*      */ 
/*  290 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/*  293 */     this.features = ((short)(this.features | 0x1));
/*  294 */     this.features = ((short)(this.features | 0x4));
/*  295 */     this.features = ((short)(this.features | 0x20));
/*  296 */     this.features = ((short)(this.features | 0x8));
/*  297 */     this.features = ((short)(this.features | 0x10));
/*  298 */     this.features = ((short)(this.features | 0x100));
/*  299 */     this.features = ((short)(this.features | 0x200));
/*      */ 
/*  301 */     if (symbolTable == null) {
/*  302 */       symbolTable = new SymbolTable();
/*      */     }
/*  304 */     this.fSymbolTable = symbolTable;
/*      */ 
/*  306 */     this.fComponents = new ArrayList();
/*      */ 
/*  308 */     setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/*  309 */     this.fErrorReporter = new XMLErrorReporter();
/*  310 */     setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*  311 */     addComponent(this.fErrorReporter);
/*      */ 
/*  313 */     setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", DTDDVFactory.getInstance());
/*      */ 
/*  315 */     XMLEntityManager manager = new XMLEntityManager();
/*  316 */     setProperty("http://apache.org/xml/properties/internal/entity-manager", manager);
/*  317 */     addComponent(manager);
/*      */ 
/*  319 */     this.fValidationManager = createValidationManager();
/*  320 */     setProperty("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
/*      */ 
/*  322 */     setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager(true));
/*      */ 
/*  324 */     setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", new XMLSecurityPropertyManager());
/*      */ 
/*  328 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/*  329 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/*  330 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/*  331 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*      */     }
/*      */ 
/*  337 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/*  338 */       MessageFormatter xmft = null;
/*      */       try {
/*  340 */         xmft = (MessageFormatter)ObjectFactory.newInstance("com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter", true);
/*      */       }
/*      */       catch (Exception exception)
/*      */       {
/*      */       }
/*  345 */       if (xmft != null) {
/*  346 */         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", xmft);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  353 */       setLocale(Locale.getDefault());
/*      */     }
/*      */     catch (XNIException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parse(XMLInputSource inputSource)
/*      */     throws XNIException, IOException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*      */   {
/*  404 */     this.fDocumentHandler = documentHandler;
/*      */   }
/*      */ 
/*      */   public XMLDocumentHandler getDocumentHandler()
/*      */   {
/*  409 */     return this.fDocumentHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(XMLDTDHandler dtdHandler)
/*      */   {
/*      */   }
/*      */ 
/*      */   public XMLDTDHandler getDTDHandler()
/*      */   {
/*  423 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDTDContentModelHandler(XMLDTDContentModelHandler handler)
/*      */   {
/*      */   }
/*      */ 
/*      */   public XMLDTDContentModelHandler getDTDContentModelHandler()
/*      */   {
/*  438 */     return null;
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(XMLEntityResolver resolver)
/*      */   {
/*  449 */     if (resolver != null)
/*  450 */       this.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", resolver);
/*      */   }
/*      */ 
/*      */   public XMLEntityResolver getEntityResolver()
/*      */   {
/*  462 */     return (XMLEntityResolver)this.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(XMLErrorHandler errorHandler)
/*      */   {
/*  484 */     if (errorHandler != null)
/*  485 */       this.fProperties.put("http://apache.org/xml/properties/internal/error-handler", errorHandler);
/*      */   }
/*      */ 
/*      */   public XMLErrorHandler getErrorHandler()
/*      */   {
/*  497 */     return (XMLErrorHandler)this.fProperties.get("http://apache.org/xml/properties/internal/error-handler");
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  517 */     super.setFeature(featureId, state);
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  531 */     super.setProperty(propertyId, value);
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */     throws XNIException
/*      */   {
/*  544 */     this.fLocale = locale;
/*  545 */     this.fErrorReporter.setLocale(locale);
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  551 */     return this.fLocale;
/*      */   }
/*      */ 
/*      */   public void setParameter(String name, Object value)
/*      */     throws DOMException
/*      */   {
/*  559 */     boolean found = true;
/*      */ 
/*  563 */     if ((value instanceof Boolean)) {
/*  564 */       boolean state = ((Boolean)value).booleanValue();
/*      */ 
/*  566 */       if (name.equalsIgnoreCase("comments")) {
/*  567 */         this.features = ((short)(state ? this.features | 0x20 : this.features & 0xFFFFFFDF));
/*      */       }
/*  569 */       else if (name.equalsIgnoreCase("datatype-normalization")) {
/*  570 */         setFeature("http://apache.org/xml/features/validation/schema/normalized-value", state);
/*  571 */         this.features = ((short)(state ? this.features | 0x2 : this.features & 0xFFFFFFFD));
/*      */ 
/*  573 */         if (state) {
/*  574 */           this.features = ((short)(this.features | 0x40));
/*      */         }
/*      */       }
/*  577 */       else if (name.equalsIgnoreCase("namespaces")) {
/*  578 */         this.features = ((short)(state ? this.features | 0x1 : this.features & 0xFFFFFFFE));
/*      */       }
/*  580 */       else if (name.equalsIgnoreCase("cdata-sections")) {
/*  581 */         this.features = ((short)(state ? this.features | 0x8 : this.features & 0xFFFFFFF7));
/*      */       }
/*  583 */       else if (name.equalsIgnoreCase("entities")) {
/*  584 */         this.features = ((short)(state ? this.features | 0x4 : this.features & 0xFFFFFFFB));
/*      */       }
/*  586 */       else if (name.equalsIgnoreCase("split-cdata-sections")) {
/*  587 */         this.features = ((short)(state ? this.features | 0x10 : this.features & 0xFFFFFFEF));
/*      */       }
/*  589 */       else if (name.equalsIgnoreCase("validate")) {
/*  590 */         this.features = ((short)(state ? this.features | 0x40 : this.features & 0xFFFFFFBF));
/*      */       }
/*  592 */       else if (name.equalsIgnoreCase("well-formed")) {
/*  593 */         this.features = ((short)(state ? this.features | 0x100 : this.features & 0xFFFFFEFF));
/*      */       }
/*  595 */       else if (name.equalsIgnoreCase("namespace-declarations")) {
/*  596 */         this.features = ((short)(state ? this.features | 0x200 : this.features & 0xFFFFFDFF));
/*      */       }
/*  598 */       else if (name.equalsIgnoreCase("infoset"))
/*      */       {
/*  600 */         if (state) {
/*  601 */           this.features = ((short)(this.features | 0x321));
/*  602 */           this.features = ((short)(this.features & 0xFFFFFFF1));
/*  603 */           setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
/*      */         }
/*      */       }
/*  606 */       else if ((name.equalsIgnoreCase("normalize-characters")) || (name.equalsIgnoreCase("canonical-form")) || (name.equalsIgnoreCase("validate-if-schema")) || (name.equalsIgnoreCase("check-character-normalization")))
/*      */       {
/*  611 */         if (state) {
/*  612 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/*  617 */           throw new DOMException((short)9, msg);
/*      */         }
/*      */       }
/*  620 */       else if (name.equalsIgnoreCase("element-content-whitespace")) {
/*  621 */         if (!state) {
/*  622 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/*  627 */           throw new DOMException((short)9, msg);
/*      */         }
/*      */       }
/*  630 */       else if (name.equalsIgnoreCase("http://apache.org/xml/features/validation/schema/augment-psvi"))
/*      */       {
/*  634 */         if (!state) {
/*  635 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/*  640 */           throw new DOMException((short)9, msg);
/*      */         }
/*      */       }
/*  643 */       else if (name.equalsIgnoreCase("psvi")) {
/*  644 */         this.features = ((short)(state ? this.features | 0x80 : this.features & 0xFFFFFF7F));
/*      */       }
/*      */       else {
/*  647 */         found = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  660 */     if ((!found) || (!(value instanceof Boolean))) {
/*  661 */       found = true;
/*      */ 
/*  663 */       if (name.equalsIgnoreCase("error-handler")) {
/*  664 */         if (((value instanceof DOMErrorHandler)) || (value == null)) {
/*  665 */           this.fErrorHandlerWrapper.setErrorHandler((DOMErrorHandler)value);
/*  666 */           setErrorHandler(this.fErrorHandlerWrapper);
/*      */         }
/*      */         else
/*      */         {
/*  671 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  676 */           throw new DOMException((short)17, msg);
/*      */         }
/*      */       }
/*  679 */       else if (name.equalsIgnoreCase("resource-resolver")) {
/*  680 */         if (((value instanceof LSResourceResolver)) || (value == null)) {
/*      */           try {
/*  682 */             setEntityResolver(new DOMEntityResolverWrapper((LSResourceResolver)value));
/*      */           }
/*      */           catch (XMLConfigurationException e) {
/*      */           }
/*      */         }
/*      */         else {
/*  688 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  693 */           throw new DOMException((short)17, msg);
/*      */         }
/*      */ 
/*      */       }
/*  697 */       else if (name.equalsIgnoreCase("schema-location")) {
/*  698 */         if (((value instanceof String)) || (value == null))
/*      */         {
/*      */           try {
/*  701 */             setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", value);
/*      */           }
/*      */           catch (XMLConfigurationException e)
/*      */           {
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  709 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  714 */           throw new DOMException((short)17, msg);
/*      */         }
/*      */ 
/*      */       }
/*  718 */       else if (name.equalsIgnoreCase("schema-type")) {
/*  719 */         if (((value instanceof String)) || (value == null)) {
/*      */           try {
/*  721 */             if (value == null) {
/*  722 */               setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", null);
/*      */             }
/*  726 */             else if (value.equals(Constants.NS_XMLSCHEMA))
/*      */             {
/*  728 */               setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
/*      */             }
/*  732 */             else if (value.equals(Constants.NS_DTD))
/*      */             {
/*  734 */               setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
/*      */             }
/*      */           }
/*      */           catch (XMLConfigurationException e) {
/*      */           }
/*      */         }
/*      */         else {
/*  741 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  746 */           throw new DOMException((short)17, msg);
/*      */         }
/*      */ 
/*      */       }
/*  750 */       else if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table"))
/*      */       {
/*  752 */         if ((value instanceof SymbolTable)) {
/*  753 */           setProperty("http://apache.org/xml/properties/internal/symbol-table", value);
/*      */         }
/*      */         else
/*      */         {
/*  757 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  762 */           throw new DOMException((short)17, msg);
/*      */         }
/*      */       }
/*  765 */       else if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool")) {
/*  766 */         if ((value instanceof XMLGrammarPool)) {
/*  767 */           setProperty("http://apache.org/xml/properties/internal/grammar-pool", value);
/*      */         }
/*      */         else
/*      */         {
/*  771 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  776 */           throw new DOMException((short)17, msg);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  783 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[] { name });
/*      */ 
/*  788 */         throw new DOMException((short)8, msg);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getParameter(String name)
/*      */     throws DOMException
/*      */   {
/*  804 */     if (name.equalsIgnoreCase("comments")) {
/*  805 */       return (this.features & 0x20) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  807 */     if (name.equalsIgnoreCase("namespaces")) {
/*  808 */       return (this.features & 0x1) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  810 */     if (name.equalsIgnoreCase("datatype-normalization"))
/*      */     {
/*  812 */       return (this.features & 0x2) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  814 */     if (name.equalsIgnoreCase("cdata-sections")) {
/*  815 */       return (this.features & 0x8) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  817 */     if (name.equalsIgnoreCase("entities")) {
/*  818 */       return (this.features & 0x4) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  820 */     if (name.equalsIgnoreCase("split-cdata-sections")) {
/*  821 */       return (this.features & 0x10) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  823 */     if (name.equalsIgnoreCase("validate")) {
/*  824 */       return (this.features & 0x40) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  826 */     if (name.equalsIgnoreCase("well-formed")) {
/*  827 */       return (this.features & 0x100) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  829 */     if (name.equalsIgnoreCase("namespace-declarations")) {
/*  830 */       return (this.features & 0x200) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  832 */     if (name.equalsIgnoreCase("infoset")) {
/*  833 */       return (this.features & 0x32F) == 801 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  835 */     if ((name.equalsIgnoreCase("normalize-characters")) || (name.equalsIgnoreCase("canonical-form")) || (name.equalsIgnoreCase("validate-if-schema")) || (name.equalsIgnoreCase("check-character-normalization")))
/*      */     {
/*  840 */       return Boolean.FALSE;
/*      */     }
/*  842 */     if (name.equalsIgnoreCase("http://apache.org/xml/features/validation/schema/augment-psvi")) {
/*  843 */       return Boolean.TRUE;
/*      */     }
/*  845 */     if (name.equalsIgnoreCase("psvi")) {
/*  846 */       return (this.features & 0x80) != 0 ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  848 */     if (name.equalsIgnoreCase("element-content-whitespace")) {
/*  849 */       return Boolean.TRUE;
/*      */     }
/*  851 */     if (name.equalsIgnoreCase("error-handler")) {
/*  852 */       return this.fErrorHandlerWrapper.getErrorHandler();
/*      */     }
/*  854 */     if (name.equalsIgnoreCase("resource-resolver")) {
/*  855 */       XMLEntityResolver entityResolver = getEntityResolver();
/*  856 */       if ((entityResolver != null) && ((entityResolver instanceof DOMEntityResolverWrapper))) {
/*  857 */         return ((DOMEntityResolverWrapper)entityResolver).getEntityResolver();
/*      */       }
/*  859 */       return null;
/*      */     }
/*  861 */     if (name.equalsIgnoreCase("schema-type")) {
/*  862 */       return getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
/*      */     }
/*  864 */     if (name.equalsIgnoreCase("schema-location")) {
/*  865 */       return getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
/*      */     }
/*  867 */     if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
/*  868 */       return getProperty("http://apache.org/xml/properties/internal/symbol-table");
/*      */     }
/*  870 */     if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool")) {
/*  871 */       return getProperty("http://apache.org/xml/properties/internal/grammar-pool");
/*      */     }
/*      */ 
/*  874 */     String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[] { name });
/*      */ 
/*  879 */     throw new DOMException((short)8, msg);
/*      */   }
/*      */ 
/*      */   public boolean canSetParameter(String name, Object value)
/*      */   {
/*  899 */     if (value == null)
/*      */     {
/*  906 */       return true;
/*      */     }
/*  908 */     if ((value instanceof Boolean))
/*      */     {
/*  912 */       if ((name.equalsIgnoreCase("comments")) || (name.equalsIgnoreCase("datatype-normalization")) || (name.equalsIgnoreCase("cdata-sections")) || (name.equalsIgnoreCase("entities")) || (name.equalsIgnoreCase("split-cdata-sections")) || (name.equalsIgnoreCase("namespaces")) || (name.equalsIgnoreCase("validate")) || (name.equalsIgnoreCase("well-formed")) || (name.equalsIgnoreCase("infoset")) || (name.equalsIgnoreCase("namespace-declarations")))
/*      */       {
/*  923 */         return true;
/*      */       }
/*  925 */       if ((name.equalsIgnoreCase("normalize-characters")) || (name.equalsIgnoreCase("canonical-form")) || (name.equalsIgnoreCase("validate-if-schema")) || (name.equalsIgnoreCase("check-character-normalization")))
/*      */       {
/*  931 */         return !value.equals(Boolean.TRUE);
/*      */       }
/*  933 */       if ((name.equalsIgnoreCase("element-content-whitespace")) || (name.equalsIgnoreCase("http://apache.org/xml/features/validation/schema/augment-psvi")))
/*      */       {
/*  936 */         return value.equals(Boolean.TRUE);
/*      */       }
/*      */ 
/*  939 */       return false;
/*      */     }
/*      */ 
/*  942 */     if (name.equalsIgnoreCase("error-handler")) {
/*  943 */       return (value instanceof DOMErrorHandler);
/*      */     }
/*  945 */     if (name.equalsIgnoreCase("resource-resolver")) {
/*  946 */       return (value instanceof LSResourceResolver);
/*      */     }
/*  948 */     if (name.equalsIgnoreCase("schema-location")) {
/*  949 */       return (value instanceof String);
/*      */     }
/*  951 */     if (name.equalsIgnoreCase("schema-type"))
/*      */     {
/*  954 */       return ((value instanceof String)) && (value.equals(Constants.NS_XMLSCHEMA));
/*      */     }
/*  956 */     if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table"))
/*      */     {
/*  958 */       return (value instanceof SymbolTable);
/*      */     }
/*  960 */     if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool")) {
/*  961 */       return (value instanceof XMLGrammarPool);
/*      */     }
/*      */ 
/*  965 */     return false;
/*      */   }
/*      */ 
/*      */   public DOMStringList getParameterNames()
/*      */   {
/*  979 */     if (this.fRecognizedParameters == null) {
/*  980 */       Vector parameters = new Vector();
/*      */ 
/*  985 */       parameters.add("comments");
/*  986 */       parameters.add("datatype-normalization");
/*  987 */       parameters.add("cdata-sections");
/*  988 */       parameters.add("entities");
/*  989 */       parameters.add("split-cdata-sections");
/*  990 */       parameters.add("namespaces");
/*  991 */       parameters.add("validate");
/*      */ 
/*  993 */       parameters.add("infoset");
/*  994 */       parameters.add("normalize-characters");
/*  995 */       parameters.add("canonical-form");
/*  996 */       parameters.add("validate-if-schema");
/*  997 */       parameters.add("check-character-normalization");
/*  998 */       parameters.add("well-formed");
/*      */ 
/* 1000 */       parameters.add("namespace-declarations");
/* 1001 */       parameters.add("element-content-whitespace");
/*      */ 
/* 1003 */       parameters.add("error-handler");
/* 1004 */       parameters.add("schema-type");
/* 1005 */       parameters.add("schema-location");
/* 1006 */       parameters.add("resource-resolver");
/*      */ 
/* 1009 */       parameters.add("http://apache.org/xml/properties/internal/grammar-pool");
/* 1010 */       parameters.add("http://apache.org/xml/properties/internal/symbol-table");
/* 1011 */       parameters.add("http://apache.org/xml/features/validation/schema/augment-psvi");
/*      */ 
/* 1013 */       this.fRecognizedParameters = new DOMStringListImpl(parameters);
/*      */     }
/*      */ 
/* 1017 */     return this.fRecognizedParameters;
/*      */   }
/*      */ 
/*      */   protected void reset()
/*      */     throws XNIException
/*      */   {
/* 1029 */     if (this.fValidationManager != null) {
/* 1030 */       this.fValidationManager.reset();
/*      */     }
/* 1032 */     int count = this.fComponents.size();
/* 1033 */     for (int i = 0; i < count; i++) {
/* 1034 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/* 1035 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected PropertyState checkProperty(String propertyId)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1053 */     if (propertyId.startsWith("http://xml.org/sax/properties/")) {
/* 1054 */       int suffixLength = propertyId.length() - "http://xml.org/sax/properties/".length();
/*      */ 
/* 1066 */       if ((suffixLength == "xml-string".length()) && (propertyId.endsWith("xml-string")))
/*      */       {
/* 1071 */         return PropertyState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1076 */     return super.checkProperty(propertyId);
/*      */   }
/*      */ 
/*      */   protected void addComponent(XMLComponent component)
/*      */   {
/* 1084 */     if (this.fComponents.contains(component)) {
/* 1085 */       return;
/*      */     }
/* 1087 */     this.fComponents.add(component);
/*      */ 
/* 1090 */     String[] recognizedFeatures = component.getRecognizedFeatures();
/* 1091 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/* 1094 */     String[] recognizedProperties = component.getRecognizedProperties();
/* 1095 */     addRecognizedProperties(recognizedProperties);
/*      */   }
/*      */ 
/*      */   protected ValidationManager createValidationManager()
/*      */   {
/* 1100 */     return new ValidationManager();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMConfigurationImpl
 * JD-Core Version:    0.6.2
 */