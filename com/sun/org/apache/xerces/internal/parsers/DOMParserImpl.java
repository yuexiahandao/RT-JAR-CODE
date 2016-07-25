/*      */ package com.sun.org.apache.xerces.internal.parsers;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMStringListImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMErrorHandlerWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*      */ import java.io.StringReader;
/*      */ import java.util.Locale;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.DOMConfiguration;
/*      */ import org.w3c.dom.DOMErrorHandler;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.DOMStringList;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.ls.LSException;
/*      */ import org.w3c.dom.ls.LSInput;
/*      */ import org.w3c.dom.ls.LSParser;
/*      */ import org.w3c.dom.ls.LSParserFilter;
/*      */ import org.w3c.dom.ls.LSResourceResolver;
/*      */ 
/*      */ public class DOMParserImpl extends AbstractDOMParser
/*      */   implements LSParser, DOMConfiguration
/*      */ {
/*      */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
/*      */   protected static final String XMLSCHEMA = "http://apache.org/xml/features/validation/schema";
/*      */   protected static final String XMLSCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
/*      */   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
/*      */   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
/*      */   protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
/*      */   protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
/*      */   protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String PSVI_AUGMENT = "http://apache.org/xml/features/validation/schema/augment-psvi";
/*  142 */   protected boolean fNamespaceDeclarations = true;
/*      */ 
/*  146 */   protected String fSchemaType = null;
/*      */ 
/*  148 */   protected boolean fBusy = false;
/*      */ 
/*  150 */   private boolean abortNow = false;
/*      */   private Thread currentThread;
/*      */   protected static final boolean DEBUG = false;
/*  156 */   private Vector fSchemaLocations = new Vector();
/*  157 */   private String fSchemaLocation = null;
/*      */   private DOMStringList fRecognizedParameters;
/*  160 */   private AbortHandler abortHandler = null;
/*      */ 
/*      */   public DOMParserImpl(XMLParserConfiguration config, String schemaType)
/*      */   {
/*  170 */     this(config);
/*  171 */     if (schemaType != null)
/*  172 */       if (schemaType.equals(Constants.NS_DTD))
/*      */       {
/*  178 */         this.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
/*      */ 
/*  181 */         this.fSchemaType = Constants.NS_DTD;
/*      */       }
/*  183 */       else if (schemaType.equals(Constants.NS_XMLSCHEMA))
/*      */       {
/*  185 */         this.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
/*      */       }
/*      */   }
/*      */ 
/*      */   public DOMParserImpl(XMLParserConfiguration config)
/*      */   {
/*  197 */     super(config);
/*      */ 
/*  200 */     String[] domRecognizedFeatures = { "canonical-form", "cdata-sections", "charset-overrides-xml-encoding", "infoset", "namespace-declarations", "split-cdata-sections", "supported-media-types-only", "certified", "well-formed", "ignore-unknown-character-denormalizations" };
/*      */ 
/*  213 */     this.fConfiguration.addRecognizedFeatures(domRecognizedFeatures);
/*      */ 
/*  216 */     this.fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
/*      */ 
/*  227 */     this.fConfiguration.setFeature("namespace-declarations", true);
/*  228 */     this.fConfiguration.setFeature("well-formed", true);
/*  229 */     this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
/*  230 */     this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
/*  231 */     this.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
/*      */ 
/*  233 */     this.fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
/*  234 */     this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
/*  235 */     this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
/*      */ 
/*  238 */     this.fConfiguration.setFeature("canonical-form", false);
/*  239 */     this.fConfiguration.setFeature("charset-overrides-xml-encoding", true);
/*  240 */     this.fConfiguration.setFeature("split-cdata-sections", true);
/*  241 */     this.fConfiguration.setFeature("supported-media-types-only", false);
/*  242 */     this.fConfiguration.setFeature("ignore-unknown-character-denormalizations", true);
/*      */ 
/*  246 */     this.fConfiguration.setFeature("certified", true);
/*      */     try
/*      */     {
/*  252 */       this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
/*      */     }
/*      */     catch (XMLConfigurationException exc)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public DOMParserImpl(SymbolTable symbolTable)
/*      */   {
/*  262 */     this(new XIncludeAwareParserConfiguration());
/*  263 */     this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
/*      */   }
/*      */ 
/*      */   public DOMParserImpl(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*      */   {
/*  274 */     this(new XIncludeAwareParserConfiguration());
/*  275 */     this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
/*      */ 
/*  278 */     this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", grammarPool);
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  290 */     super.reset();
/*      */ 
/*  293 */     this.fNamespaceDeclarations = this.fConfiguration.getFeature("namespace-declarations");
/*      */ 
/*  297 */     if (this.fSkippedElemStack != null) {
/*  298 */       this.fSkippedElemStack.removeAllElements();
/*      */     }
/*  300 */     this.fSchemaLocations.clear();
/*  301 */     this.fRejectedElementDepth = 0;
/*  302 */     this.fFilterReject = false;
/*  303 */     this.fSchemaType = null;
/*      */   }
/*      */ 
/*      */   public DOMConfiguration getDomConfig()
/*      */   {
/*  312 */     return this;
/*      */   }
/*      */ 
/*      */   public LSParserFilter getFilter()
/*      */   {
/*  326 */     return this.fDOMFilter;
/*      */   }
/*      */ 
/*      */   public void setFilter(LSParserFilter filter)
/*      */   {
/*  339 */     this.fDOMFilter = filter;
/*  340 */     if (this.fSkippedElemStack == null)
/*  341 */       this.fSkippedElemStack = new Stack();
/*      */   }
/*      */ 
/*      */   public void setParameter(String name, Object value)
/*      */     throws DOMException
/*      */   {
/*  351 */     if ((value instanceof Boolean)) {
/*  352 */       boolean state = ((Boolean)value).booleanValue();
/*      */       try {
/*  354 */         if (name.equalsIgnoreCase("comments")) {
/*  355 */           this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", state);
/*      */         }
/*  357 */         else if (name.equalsIgnoreCase("datatype-normalization")) {
/*  358 */           this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", state);
/*      */         }
/*  360 */         else if (name.equalsIgnoreCase("entities")) {
/*  361 */           this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", state);
/*      */         }
/*  363 */         else if (name.equalsIgnoreCase("disallow-doctype")) {
/*  364 */           this.fConfiguration.setFeature("http://apache.org/xml/features/disallow-doctype-decl", state);
/*      */         }
/*  366 */         else if ((name.equalsIgnoreCase("supported-media-types-only")) || (name.equalsIgnoreCase("normalize-characters")) || (name.equalsIgnoreCase("check-character-normalization")) || (name.equalsIgnoreCase("canonical-form")))
/*      */         {
/*  370 */           if (state) {
/*  371 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/*  376 */             throw new DOMException((short)9, msg);
/*      */           }
/*      */ 
/*      */         }
/*  380 */         else if (name.equalsIgnoreCase("namespaces")) {
/*  381 */           this.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", state);
/*      */         }
/*  383 */         else if (name.equalsIgnoreCase("infoset"))
/*      */         {
/*  385 */           if (state)
/*      */           {
/*  388 */             this.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
/*  389 */             this.fConfiguration.setFeature("namespace-declarations", true);
/*  390 */             this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
/*  391 */             this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
/*      */ 
/*  395 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
/*  396 */             this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
/*  397 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
/*  398 */             this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
/*      */           }
/*      */         }
/*  401 */         else if (name.equalsIgnoreCase("cdata-sections")) {
/*  402 */           this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", state);
/*      */         }
/*  404 */         else if (name.equalsIgnoreCase("namespace-declarations")) {
/*  405 */           this.fConfiguration.setFeature("namespace-declarations", state);
/*      */         }
/*  407 */         else if ((name.equalsIgnoreCase("well-formed")) || (name.equalsIgnoreCase("ignore-unknown-character-denormalizations")))
/*      */         {
/*  409 */           if (!state) {
/*  410 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/*  415 */             throw new DOMException((short)9, msg);
/*      */           }
/*      */ 
/*      */         }
/*  420 */         else if (name.equalsIgnoreCase("validate")) {
/*  421 */           this.fConfiguration.setFeature("http://xml.org/sax/features/validation", state);
/*  422 */           if (this.fSchemaType != Constants.NS_DTD) {
/*  423 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", state);
/*  424 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", state);
/*      */           }
/*  426 */           if (state) {
/*  427 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
/*      */           }
/*      */         }
/*  430 */         else if (name.equalsIgnoreCase("validate-if-schema")) {
/*  431 */           this.fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", state);
/*      */ 
/*  433 */           if (state) {
/*  434 */             this.fConfiguration.setFeature("http://xml.org/sax/features/validation", false);
/*      */           }
/*      */         }
/*  437 */         else if (name.equalsIgnoreCase("element-content-whitespace")) {
/*  438 */           this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", state);
/*      */         }
/*  440 */         else if (name.equalsIgnoreCase("psvi"))
/*      */         {
/*  442 */           this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
/*  443 */           this.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", "com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl");
/*      */         }
/*      */         else
/*      */         {
/*      */           String normalizedName;
/*      */           String normalizedName;
/*  451 */           if (name.equals("http://apache.org/xml/features/namespace-growth")) {
/*  452 */             normalizedName = "http://apache.org/xml/features/namespace-growth";
/*      */           }
/*      */           else
/*      */           {
/*      */             String normalizedName;
/*  454 */             if (name.equals("http://apache.org/xml/features/internal/tolerate-duplicates")) {
/*  455 */               normalizedName = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */             }
/*      */             else
/*  458 */               normalizedName = name.toLowerCase(Locale.ENGLISH);
/*      */           }
/*  460 */           this.fConfiguration.setFeature(normalizedName, state);
/*      */         }
/*      */       }
/*      */       catch (XMLConfigurationException e)
/*      */       {
/*  465 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[] { name });
/*      */ 
/*  470 */         throw new DOMException((short)8, msg);
/*      */       }
/*      */ 
/*      */     }
/*  474 */     else if (name.equalsIgnoreCase("error-handler")) {
/*  475 */       if (((value instanceof DOMErrorHandler)) || (value == null)) {
/*      */         try {
/*  477 */           this.fErrorHandler = new DOMErrorHandlerWrapper((DOMErrorHandler)value);
/*  478 */           this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", this.fErrorHandler);
/*      */         }
/*      */         catch (XMLConfigurationException e) {
/*      */         }
/*      */       }
/*      */       else {
/*  484 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  489 */         throw new DOMException((short)17, msg);
/*      */       }
/*      */ 
/*      */     }
/*  493 */     else if (name.equalsIgnoreCase("resource-resolver")) {
/*  494 */       if (((value instanceof LSResourceResolver)) || (value == null)) {
/*      */         try {
/*  496 */           this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper((LSResourceResolver)value));
/*      */         }
/*      */         catch (XMLConfigurationException e) {
/*      */         }
/*      */       }
/*      */       else {
/*  502 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  507 */         throw new DOMException((short)17, msg);
/*      */       }
/*      */ 
/*      */     }
/*  511 */     else if (name.equalsIgnoreCase("schema-location")) {
/*  512 */       if (((value instanceof String)) || (value == null)) {
/*      */         try {
/*  514 */           if (value == null) {
/*  515 */             this.fSchemaLocation = null;
/*  516 */             this.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", null);
/*      */           }
/*      */           else
/*      */           {
/*  521 */             this.fSchemaLocation = ((String)value);
/*      */ 
/*  524 */             StringTokenizer t = new StringTokenizer(this.fSchemaLocation, " \n\t\r");
/*  525 */             if (t.hasMoreTokens()) {
/*  526 */               this.fSchemaLocations.clear();
/*  527 */               this.fSchemaLocations.add(t.nextToken());
/*  528 */               while (t.hasMoreTokens()) {
/*  529 */                 this.fSchemaLocations.add(t.nextToken());
/*      */               }
/*  531 */               this.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", this.fSchemaLocations.toArray());
/*      */             }
/*      */             else
/*      */             {
/*  536 */               this.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", value);
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (XMLConfigurationException e)
/*      */         {
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  546 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  551 */         throw new DOMException((short)17, msg);
/*      */       }
/*      */ 
/*      */     }
/*  555 */     else if (name.equalsIgnoreCase("schema-type")) {
/*  556 */       if (((value instanceof String)) || (value == null)) {
/*      */         try {
/*  558 */           if (value == null)
/*      */           {
/*  560 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", false);
/*  561 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
/*      */ 
/*  563 */             this.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", null);
/*      */ 
/*  566 */             this.fSchemaType = null;
/*      */           }
/*  568 */           else if (value.equals(Constants.NS_XMLSCHEMA))
/*      */           {
/*  570 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", true);
/*  571 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
/*      */ 
/*  573 */             this.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
/*      */ 
/*  576 */             this.fSchemaType = Constants.NS_XMLSCHEMA;
/*      */           }
/*  578 */           else if (value.equals(Constants.NS_DTD))
/*      */           {
/*  580 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", false);
/*  581 */             this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
/*      */ 
/*  583 */             this.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
/*      */ 
/*  586 */             this.fSchemaType = Constants.NS_DTD;
/*      */           }
/*      */         } catch (XMLConfigurationException e) {
/*      */         }
/*      */       }
/*      */       else {
/*  592 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/*  597 */         throw new DOMException((short)17, msg);
/*      */       }
/*      */ 
/*      */     }
/*  601 */     else if (name.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
/*  602 */       this.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", value);
/*      */     }
/*      */     else
/*      */     {
/*  606 */       String normalizedName = name.toLowerCase(Locale.ENGLISH);
/*      */       try {
/*  608 */         this.fConfiguration.setProperty(normalizedName, value);
/*  609 */         return;
/*      */       }
/*      */       catch (XMLConfigurationException e)
/*      */       {
/*      */         try
/*      */         {
/*  615 */           if (name.equals("http://apache.org/xml/features/namespace-growth")) {
/*  616 */             normalizedName = "http://apache.org/xml/features/namespace-growth";
/*      */           }
/*  618 */           else if (name.equals("http://apache.org/xml/features/internal/tolerate-duplicates")) {
/*  619 */             normalizedName = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */           }
/*  621 */           this.fConfiguration.getFeature(normalizedName);
/*  622 */           throw newTypeMismatchError(name);
/*      */         }
/*      */         catch (XMLConfigurationException e)
/*      */         {
/*  628 */           throw newFeatureNotFoundError(name);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getParameter(String name)
/*      */     throws DOMException
/*      */   {
/*  637 */     if (name.equalsIgnoreCase("comments")) {
/*  638 */       return this.fConfiguration.getFeature("http://apache.org/xml/features/include-comments") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  642 */     if (name.equalsIgnoreCase("datatype-normalization")) {
/*  643 */       return this.fConfiguration.getFeature("http://apache.org/xml/features/validation/schema/normalized-value") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  647 */     if (name.equalsIgnoreCase("entities")) {
/*  648 */       return this.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  652 */     if (name.equalsIgnoreCase("namespaces")) {
/*  653 */       return this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  657 */     if (name.equalsIgnoreCase("validate")) {
/*  658 */       return this.fConfiguration.getFeature("http://xml.org/sax/features/validation") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  662 */     if (name.equalsIgnoreCase("validate-if-schema")) {
/*  663 */       return this.fConfiguration.getFeature("http://apache.org/xml/features/validation/dynamic") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  667 */     if (name.equalsIgnoreCase("element-content-whitespace")) {
/*  668 */       return this.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  672 */     if (name.equalsIgnoreCase("disallow-doctype")) {
/*  673 */       return this.fConfiguration.getFeature("http://apache.org/xml/features/disallow-doctype-decl") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  677 */     if (name.equalsIgnoreCase("infoset"))
/*      */     {
/*  682 */       boolean infoset = (this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces")) && (this.fConfiguration.getFeature("namespace-declarations")) && (this.fConfiguration.getFeature("http://apache.org/xml/features/include-comments")) && (this.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace")) && (!this.fConfiguration.getFeature("http://apache.org/xml/features/validation/dynamic")) && (!this.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes")) && (!this.fConfiguration.getFeature("http://apache.org/xml/features/validation/schema/normalized-value")) && (!this.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes"));
/*      */ 
/*  690 */       return infoset ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*  692 */     if (name.equalsIgnoreCase("cdata-sections")) {
/*  693 */       return this.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes") ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  696 */     if ((name.equalsIgnoreCase("check-character-normalization")) || (name.equalsIgnoreCase("normalize-characters")))
/*      */     {
/*  698 */       return Boolean.FALSE;
/*      */     }
/*  700 */     if ((name.equalsIgnoreCase("namespace-declarations")) || (name.equalsIgnoreCase("well-formed")) || (name.equalsIgnoreCase("ignore-unknown-character-denormalizations")) || (name.equalsIgnoreCase("canonical-form")) || (name.equalsIgnoreCase("supported-media-types-only")) || (name.equalsIgnoreCase("split-cdata-sections")) || (name.equalsIgnoreCase("charset-overrides-xml-encoding")))
/*      */     {
/*  707 */       return this.fConfiguration.getFeature(name.toLowerCase(Locale.ENGLISH)) ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */ 
/*  711 */     if (name.equalsIgnoreCase("error-handler")) {
/*  712 */       if (this.fErrorHandler != null) {
/*  713 */         return this.fErrorHandler.getErrorHandler();
/*      */       }
/*  715 */       return null;
/*      */     }
/*  717 */     if (name.equalsIgnoreCase("resource-resolver")) {
/*      */       try {
/*  719 */         XMLEntityResolver entityResolver = (XMLEntityResolver)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
/*      */ 
/*  721 */         if ((entityResolver != null) && ((entityResolver instanceof DOMEntityResolverWrapper)))
/*      */         {
/*  723 */           return ((DOMEntityResolverWrapper)entityResolver).getEntityResolver();
/*      */         }
/*  725 */         return null;
/*      */       } catch (XMLConfigurationException e) {
/*      */       }
/*      */     } else {
/*  729 */       if (name.equalsIgnoreCase("schema-type")) {
/*  730 */         return this.fConfiguration.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
/*      */       }
/*      */ 
/*  733 */       if (name.equalsIgnoreCase("schema-location")) {
/*  734 */         return this.fSchemaLocation;
/*      */       }
/*  736 */       if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
/*  737 */         return this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/symbol-table");
/*      */       }
/*  739 */       if (name.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name"))
/*  740 */         return this.fConfiguration.getProperty("http://apache.org/xml/properties/dom/document-class-name");
/*      */       String normalizedName;
/*      */       String normalizedName;
/*  746 */       if (name.equals("http://apache.org/xml/features/namespace-growth")) {
/*  747 */         normalizedName = "http://apache.org/xml/features/namespace-growth";
/*      */       }
/*      */       else
/*      */       {
/*      */         String normalizedName;
/*  749 */         if (name.equals("http://apache.org/xml/features/internal/tolerate-duplicates")) {
/*  750 */           normalizedName = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */         }
/*      */         else
/*  753 */           normalizedName = name.toLowerCase(Locale.ENGLISH);
/*      */       }
/*      */       try {
/*  756 */         return this.fConfiguration.getFeature(normalizedName) ? Boolean.TRUE : Boolean.FALSE;
/*      */       }
/*      */       catch (XMLConfigurationException e)
/*      */       {
/*      */         try
/*      */         {
/*  763 */           return this.fConfiguration.getProperty(normalizedName);
/*      */         }
/*      */         catch (XMLConfigurationException e)
/*      */         {
/*  767 */           throw newFeatureNotFoundError(name);
/*      */         }
/*      */       }
/*      */     }
/*  769 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean canSetParameter(String name, Object value) {
/*  773 */     if (value == null) {
/*  774 */       return true;
/*      */     }
/*      */ 
/*  777 */     if ((value instanceof Boolean)) {
/*  778 */       boolean state = ((Boolean)value).booleanValue();
/*  779 */       if ((name.equalsIgnoreCase("supported-media-types-only")) || (name.equalsIgnoreCase("normalize-characters")) || (name.equalsIgnoreCase("check-character-normalization")) || (name.equalsIgnoreCase("canonical-form")))
/*      */       {
/*  784 */         return !state;
/*      */       }
/*  786 */       if ((name.equalsIgnoreCase("well-formed")) || (name.equalsIgnoreCase("ignore-unknown-character-denormalizations")))
/*      */       {
/*  789 */         return state;
/*      */       }
/*  791 */       if ((name.equalsIgnoreCase("cdata-sections")) || (name.equalsIgnoreCase("charset-overrides-xml-encoding")) || (name.equalsIgnoreCase("comments")) || (name.equalsIgnoreCase("datatype-normalization")) || (name.equalsIgnoreCase("disallow-doctype")) || (name.equalsIgnoreCase("entities")) || (name.equalsIgnoreCase("infoset")) || (name.equalsIgnoreCase("namespaces")) || (name.equalsIgnoreCase("namespace-declarations")) || (name.equalsIgnoreCase("validate")) || (name.equalsIgnoreCase("validate-if-schema")) || (name.equalsIgnoreCase("element-content-whitespace")) || (name.equalsIgnoreCase("xml-declaration")))
/*      */       {
/*  804 */         return true;
/*      */       }
/*      */       try
/*      */       {
/*      */         String normalizedName;
/*      */         String normalizedName;
/*  810 */         if (name.equalsIgnoreCase("http://apache.org/xml/features/namespace-growth")) {
/*  811 */           normalizedName = "http://apache.org/xml/features/namespace-growth";
/*      */         }
/*      */         else
/*      */         {
/*      */           String normalizedName;
/*  813 */           if (name.equalsIgnoreCase("http://apache.org/xml/features/internal/tolerate-duplicates")) {
/*  814 */             normalizedName = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */           }
/*      */           else
/*  817 */             normalizedName = name.toLowerCase(Locale.ENGLISH);
/*      */         }
/*  819 */         this.fConfiguration.getFeature(normalizedName);
/*  820 */         return true;
/*      */       }
/*      */       catch (XMLConfigurationException e) {
/*  823 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*  827 */     if (name.equalsIgnoreCase("error-handler")) {
/*  828 */       if (((value instanceof DOMErrorHandler)) || (value == null)) {
/*  829 */         return true;
/*      */       }
/*  831 */       return false;
/*      */     }
/*  833 */     if (name.equalsIgnoreCase("resource-resolver")) {
/*  834 */       if (((value instanceof LSResourceResolver)) || (value == null)) {
/*  835 */         return true;
/*      */       }
/*  837 */       return false;
/*      */     }
/*  839 */     if (name.equalsIgnoreCase("schema-type")) {
/*  840 */       if ((((value instanceof String)) && ((value.equals(Constants.NS_XMLSCHEMA)) || (value.equals(Constants.NS_DTD)))) || (value == null))
/*      */       {
/*  843 */         return true;
/*      */       }
/*  845 */       return false;
/*      */     }
/*  847 */     if (name.equalsIgnoreCase("schema-location")) {
/*  848 */       if (((value instanceof String)) || (value == null))
/*  849 */         return true;
/*  850 */       return false;
/*      */     }
/*  852 */     if (name.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
/*  853 */       return true;
/*      */     }
/*  855 */     return false;
/*      */   }
/*      */ 
/*      */   public DOMStringList getParameterNames()
/*      */   {
/*  868 */     if (this.fRecognizedParameters == null) {
/*  869 */       Vector parameters = new Vector();
/*      */ 
/*  872 */       parameters.add("namespaces");
/*  873 */       parameters.add("cdata-sections");
/*  874 */       parameters.add("canonical-form");
/*  875 */       parameters.add("namespace-declarations");
/*  876 */       parameters.add("split-cdata-sections");
/*      */ 
/*  878 */       parameters.add("entities");
/*  879 */       parameters.add("validate-if-schema");
/*  880 */       parameters.add("validate");
/*  881 */       parameters.add("datatype-normalization");
/*      */ 
/*  883 */       parameters.add("charset-overrides-xml-encoding");
/*  884 */       parameters.add("check-character-normalization");
/*  885 */       parameters.add("supported-media-types-only");
/*  886 */       parameters.add("ignore-unknown-character-denormalizations");
/*      */ 
/*  888 */       parameters.add("normalize-characters");
/*  889 */       parameters.add("well-formed");
/*  890 */       parameters.add("infoset");
/*  891 */       parameters.add("disallow-doctype");
/*  892 */       parameters.add("element-content-whitespace");
/*  893 */       parameters.add("comments");
/*      */ 
/*  895 */       parameters.add("error-handler");
/*  896 */       parameters.add("resource-resolver");
/*  897 */       parameters.add("schema-location");
/*  898 */       parameters.add("schema-type");
/*      */ 
/*  900 */       this.fRecognizedParameters = new DOMStringListImpl(parameters);
/*      */     }
/*      */ 
/*  904 */     return this.fRecognizedParameters;
/*      */   }
/*      */ 
/*      */   public Document parseURI(String uri)
/*      */     throws LSException
/*      */   {
/*  917 */     if (this.fBusy) {
/*  918 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null);
/*      */ 
/*  921 */       throw new DOMException((short)11, msg);
/*      */     }
/*      */ 
/*  924 */     XMLInputSource source = new XMLInputSource(null, uri, null);
/*      */     try {
/*  926 */       this.currentThread = Thread.currentThread();
/*  927 */       this.fBusy = true;
/*  928 */       parse(source);
/*  929 */       this.fBusy = false;
/*  930 */       if ((this.abortNow) && (this.currentThread.isInterrupted()))
/*      */       {
/*  932 */         this.abortNow = false;
/*  933 */         Thread.interrupted();
/*      */       }
/*      */     } catch (Exception e) {
/*  936 */       this.fBusy = false;
/*  937 */       if ((this.abortNow) && (this.currentThread.isInterrupted())) {
/*  938 */         Thread.interrupted();
/*      */       }
/*  940 */       if (this.abortNow) {
/*  941 */         this.abortNow = false;
/*  942 */         restoreHandlers();
/*  943 */         return null;
/*      */       }
/*      */ 
/*  947 */       if (e != AbstractDOMParser.Abort.INSTANCE) {
/*  948 */         if ((!(e instanceof XMLParseException)) && (this.fErrorHandler != null)) {
/*  949 */           DOMErrorImpl error = new DOMErrorImpl();
/*  950 */           error.fException = e;
/*  951 */           error.fMessage = e.getMessage();
/*  952 */           error.fSeverity = 3;
/*  953 */           this.fErrorHandler.getErrorHandler().handleError(error);
/*      */         }
/*      */ 
/*  958 */         throw ((LSException)DOMUtil.createLSException((short)81, e).fillInStackTrace());
/*      */       }
/*      */     }
/*  961 */     Document doc = getDocument();
/*  962 */     dropDocumentReferences();
/*  963 */     return doc;
/*      */   }
/*      */ 
/*      */   public Document parse(LSInput is)
/*      */     throws LSException
/*      */   {
/*  974 */     XMLInputSource xmlInputSource = dom2xmlInputSource(is);
/*  975 */     if (this.fBusy) {
/*  976 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null);
/*      */ 
/*  979 */       throw new DOMException((short)11, msg);
/*      */     }
/*      */     try
/*      */     {
/*  983 */       this.currentThread = Thread.currentThread();
/*  984 */       this.fBusy = true;
/*  985 */       parse(xmlInputSource);
/*  986 */       this.fBusy = false;
/*  987 */       if ((this.abortNow) && (this.currentThread.isInterrupted()))
/*      */       {
/*  989 */         this.abortNow = false;
/*  990 */         Thread.interrupted();
/*      */       }
/*      */     } catch (Exception e) {
/*  993 */       this.fBusy = false;
/*  994 */       if ((this.abortNow) && (this.currentThread.isInterrupted())) {
/*  995 */         Thread.interrupted();
/*      */       }
/*  997 */       if (this.abortNow) {
/*  998 */         this.abortNow = false;
/*  999 */         restoreHandlers();
/* 1000 */         return null;
/*      */       }
/*      */ 
/* 1004 */       if (e != AbstractDOMParser.Abort.INSTANCE) {
/* 1005 */         if ((!(e instanceof XMLParseException)) && (this.fErrorHandler != null)) {
/* 1006 */           DOMErrorImpl error = new DOMErrorImpl();
/* 1007 */           error.fException = e;
/* 1008 */           error.fMessage = e.getMessage();
/* 1009 */           error.fSeverity = 3;
/* 1010 */           this.fErrorHandler.getErrorHandler().handleError(error);
/*      */         }
/*      */ 
/* 1015 */         throw ((LSException)DOMUtil.createLSException((short)81, e).fillInStackTrace());
/*      */       }
/*      */     }
/* 1018 */     Document doc = getDocument();
/* 1019 */     dropDocumentReferences();
/* 1020 */     return doc;
/*      */   }
/*      */ 
/*      */   private void restoreHandlers()
/*      */   {
/* 1025 */     this.fConfiguration.setDocumentHandler(this);
/* 1026 */     this.fConfiguration.setDTDHandler(this);
/* 1027 */     this.fConfiguration.setDTDContentModelHandler(this);
/*      */   }
/*      */ 
/*      */   public Node parseWithContext(LSInput is, Node cnode, short action)
/*      */     throws DOMException, LSException
/*      */   {
/* 1052 */     throw new DOMException((short)9, "Not supported");
/*      */   }
/*      */ 
/*      */   XMLInputSource dom2xmlInputSource(LSInput is)
/*      */   {
/* 1064 */     XMLInputSource xis = null;
/*      */ 
/* 1067 */     if (is.getCharacterStream() != null) {
/* 1068 */       xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), is.getCharacterStream(), "UTF-16");
/*      */     }
/* 1073 */     else if (is.getByteStream() != null) {
/* 1074 */       xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), is.getByteStream(), is.getEncoding());
/*      */     }
/* 1080 */     else if ((is.getStringData() != null) && (is.getStringData().length() > 0)) {
/* 1081 */       xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), new StringReader(is.getStringData()), "UTF-16");
/*      */     }
/* 1086 */     else if (((is.getSystemId() != null) && (is.getSystemId().length() > 0)) || ((is.getPublicId() != null) && (is.getPublicId().length() > 0)))
/*      */     {
/* 1088 */       xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI());
/*      */     }
/*      */     else
/*      */     {
/* 1093 */       if (this.fErrorHandler != null) {
/* 1094 */         DOMErrorImpl error = new DOMErrorImpl();
/* 1095 */         error.fType = "no-input-specified";
/* 1096 */         error.fMessage = "no-input-specified";
/* 1097 */         error.fSeverity = 3;
/* 1098 */         this.fErrorHandler.getErrorHandler().handleError(error);
/*      */       }
/* 1100 */       throw new LSException((short)81, "no-input-specified");
/*      */     }
/* 1102 */     return xis;
/*      */   }
/*      */ 
/*      */   public boolean getAsync()
/*      */   {
/* 1109 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean getBusy()
/*      */   {
/* 1116 */     return this.fBusy;
/*      */   }
/*      */ 
/*      */   public void abort()
/*      */   {
/* 1124 */     if (this.fBusy) {
/* 1125 */       this.fBusy = false;
/* 1126 */       if (this.currentThread != null) {
/* 1127 */         this.abortNow = true;
/* 1128 */         if (this.abortHandler == null) {
/* 1129 */           this.abortHandler = new AbortHandler(null);
/*      */         }
/* 1131 */         this.fConfiguration.setDocumentHandler(this.abortHandler);
/* 1132 */         this.fConfiguration.setDTDHandler(this.abortHandler);
/* 1133 */         this.fConfiguration.setDTDContentModelHandler(this.abortHandler);
/*      */ 
/* 1135 */         if (this.currentThread == Thread.currentThread()) {
/* 1136 */           throw AbstractDOMParser.Abort.INSTANCE;
/*      */         }
/* 1138 */         this.currentThread.interrupt();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */   {
/* 1158 */     if ((!this.fNamespaceDeclarations) && (this.fNamespaceAware)) {
/* 1159 */       int len = attributes.getLength();
/* 1160 */       for (int i = len - 1; i >= 0; i--) {
/* 1161 */         if ((XMLSymbols.PREFIX_XMLNS == attributes.getPrefix(i)) || (XMLSymbols.PREFIX_XMLNS == attributes.getQName(i)))
/*      */         {
/* 1163 */           attributes.removeAttributeAt(i);
/*      */         }
/*      */       }
/*      */     }
/* 1167 */     super.startElement(element, attributes, augs);
/*      */   }
/*      */ 
/*      */   private static DOMException newFeatureNotFoundError(String name)
/*      */   {
/* 1375 */     String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[] { name });
/*      */ 
/* 1380 */     return new DOMException((short)8, msg);
/*      */   }
/*      */ 
/*      */   private static DOMException newTypeMismatchError(String name) {
/* 1384 */     String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { name });
/*      */ 
/* 1389 */     return new DOMException((short)17, msg);
/*      */   }
/*      */ 
/*      */   private class AbortHandler
/*      */     implements XMLDocumentHandler, XMLDTDHandler, XMLDTDContentModelHandler
/*      */   {
/*      */     private XMLDocumentSource documentSource;
/*      */     private XMLDTDContentModelSource dtdContentSource;
/*      */     private XMLDTDSource dtdSource;
/*      */ 
/*      */     private AbortHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*      */       throws XNIException
/*      */     {
/* 1177 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
/* 1181 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
/* 1185 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void comment(XMLString text, Augmentations augs) throws XNIException {
/* 1189 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
/* 1193 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 1197 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 1201 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
/* 1205 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
/* 1209 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
/* 1213 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void characters(XMLString text, Augmentations augs) throws XNIException {
/* 1217 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
/* 1221 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endElement(QName element, Augmentations augs) throws XNIException {
/* 1225 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void startCDATA(Augmentations augs) throws XNIException {
/* 1229 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endCDATA(Augmentations augs) throws XNIException {
/* 1233 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endDocument(Augmentations augs) throws XNIException {
/* 1237 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void setDocumentSource(XMLDocumentSource source) {
/* 1241 */       this.documentSource = source;
/*      */     }
/*      */ 
/*      */     public XMLDocumentSource getDocumentSource() {
/* 1245 */       return this.documentSource;
/*      */     }
/*      */ 
/*      */     public void startDTD(XMLLocator locator, Augmentations augmentations) throws XNIException {
/* 1249 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augmentations) throws XNIException {
/* 1253 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endParameterEntity(String name, Augmentations augmentations) throws XNIException {
/* 1257 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
/* 1261 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endExternalSubset(Augmentations augmentations) throws XNIException {
/* 1265 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void elementDecl(String name, String contentModel, Augmentations augmentations) throws XNIException {
/* 1269 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void startAttlist(String elementName, Augmentations augmentations) throws XNIException {
/* 1273 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augmentations) throws XNIException {
/* 1277 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endAttlist(Augmentations augmentations) throws XNIException {
/* 1281 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augmentations) throws XNIException {
/* 1285 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
/* 1289 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augmentations) throws XNIException {
/* 1293 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
/* 1297 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void startConditional(short type, Augmentations augmentations) throws XNIException {
/* 1301 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void ignoredCharacters(XMLString text, Augmentations augmentations) throws XNIException {
/* 1305 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endConditional(Augmentations augmentations) throws XNIException {
/* 1309 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endDTD(Augmentations augmentations) throws XNIException {
/* 1313 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void setDTDSource(XMLDTDSource source) {
/* 1317 */       this.dtdSource = source;
/*      */     }
/*      */ 
/*      */     public XMLDTDSource getDTDSource() {
/* 1321 */       return this.dtdSource;
/*      */     }
/*      */ 
/*      */     public void startContentModel(String elementName, Augmentations augmentations) throws XNIException {
/* 1325 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void any(Augmentations augmentations) throws XNIException {
/* 1329 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void empty(Augmentations augmentations) throws XNIException {
/* 1333 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void startGroup(Augmentations augmentations) throws XNIException {
/* 1337 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void pcdata(Augmentations augmentations) throws XNIException {
/* 1341 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void element(String elementName, Augmentations augmentations) throws XNIException {
/* 1345 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void separator(short separator, Augmentations augmentations) throws XNIException {
/* 1349 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void occurrence(short occurrence, Augmentations augmentations) throws XNIException {
/* 1353 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endGroup(Augmentations augmentations) throws XNIException {
/* 1357 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void endContentModel(Augmentations augmentations) throws XNIException {
/* 1361 */       throw AbstractDOMParser.Abort.INSTANCE;
/*      */     }
/*      */ 
/*      */     public void setDTDContentModelSource(XMLDTDContentModelSource source) {
/* 1365 */       this.dtdContentSource = source;
/*      */     }
/*      */ 
/*      */     public XMLDTDContentModelSource getDTDContentModelSource() {
/* 1369 */       return this.dtdContentSource;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.DOMParserImpl
 * JD-Core Version:    0.6.2
 */