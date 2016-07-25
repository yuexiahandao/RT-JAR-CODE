/*      */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar.BuiltinSchemaGrammar;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar.Schema4Annotations;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSDDescription;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSGrammarBucket;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSGroupDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSNotationDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.opti.ElementImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOM;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOMParser;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaParsingConfig;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;
/*      */ import com.sun.org.apache.xerces.internal.parsers.SAXParser;
/*      */ import com.sun.org.apache.xerces.internal.parsers.XML11Configuration;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMInputSource;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*      */ import com.sun.org.apache.xerces.internal.util.DefaultErrorHandler;
/*      */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.SAXInputSource;
/*      */ import com.sun.org.apache.xerces.internal.util.StAXInputSource;
/*      */ import com.sun.org.apache.xerces.internal.util.StAXLocationWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.Property;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLSchemaDescription;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*      */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSAttributeGroupDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSModelGroup;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSObject;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSParticle;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTerm;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.StringReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.xml.stream.XMLEventReader;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import javax.xml.stream.XMLStreamReader;
/*      */ import javax.xml.stream.events.XMLEvent;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.helpers.XMLReaderFactory;
/*      */ 
/*      */ public class XSDHandler
/*      */ {
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String XMLSCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
/*      */   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
/*      */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*      */   protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
/*      */   protected static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
/*      */   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
/*      */   protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
/*      */   protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
/*      */   protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
/*      */   protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */   private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
/*      */   protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
/*      */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*      */   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
/*      */   public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*      */   private static final String SECURE_PROCESSING = "http://apache.org/xml/properties/security-manager";
/*      */   protected static final String LOCALE = "http://apache.org/xml/properties/locale";
/*      */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*      */   protected static final boolean DEBUG_NODE_POOL = false;
/*      */   static final int ATTRIBUTE_TYPE = 1;
/*      */   static final int ATTRIBUTEGROUP_TYPE = 2;
/*      */   static final int ELEMENT_TYPE = 3;
/*      */   static final int GROUP_TYPE = 4;
/*      */   static final int IDENTITYCONSTRAINT_TYPE = 5;
/*      */   static final int NOTATION_TYPE = 6;
/*      */   static final int TYPEDECL_TYPE = 7;
/*      */   public static final String REDEF_IDENTIFIER = "_fn3dktizrknc9pi";
/*  253 */   protected XSDeclarationPool fDeclPool = null;
/*      */ 
/*  260 */   protected XMLSecurityManager fSecureProcessing = null;
/*      */   private String fAccessExternalSchema;
/*      */   private String fAccessExternalDTD;
/*  272 */   private boolean registryEmpty = true;
/*  273 */   private Map<String, Element> fUnparsedAttributeRegistry = new HashMap();
/*  274 */   private Map<String, Element> fUnparsedAttributeGroupRegistry = new HashMap();
/*  275 */   private Map<String, Element> fUnparsedElementRegistry = new HashMap();
/*  276 */   private Map<String, Element> fUnparsedGroupRegistry = new HashMap();
/*  277 */   private Map<String, Element> fUnparsedIdentityConstraintRegistry = new HashMap();
/*  278 */   private Map<String, Element> fUnparsedNotationRegistry = new HashMap();
/*  279 */   private Map<String, Element> fUnparsedTypeRegistry = new HashMap();
/*      */ 
/*  283 */   private Map<String, XSDocumentInfo> fUnparsedAttributeRegistrySub = new HashMap();
/*  284 */   private Map<String, XSDocumentInfo> fUnparsedAttributeGroupRegistrySub = new HashMap();
/*  285 */   private Map<String, XSDocumentInfo> fUnparsedElementRegistrySub = new HashMap();
/*  286 */   private Map<String, XSDocumentInfo> fUnparsedGroupRegistrySub = new HashMap();
/*  287 */   private Map<String, XSDocumentInfo> fUnparsedIdentityConstraintRegistrySub = new HashMap();
/*  288 */   private Map<String, XSDocumentInfo> fUnparsedNotationRegistrySub = new HashMap();
/*  289 */   private Map<String, XSDocumentInfo> fUnparsedTypeRegistrySub = new HashMap();
/*      */ 
/*  293 */   private Map[] fUnparsedRegistriesExt = { null, null, null, null, null, null, null, null };
/*      */ 
/*  307 */   private Map<XSDocumentInfo, Vector> fDependencyMap = new HashMap();
/*      */ 
/*  313 */   private Map<String, Vector> fImportMap = new HashMap();
/*      */ 
/*  317 */   private Vector fAllTNSs = new Vector();
/*      */ 
/*  319 */   private Map fLocationPairs = null;
/*  320 */   private static final Map EMPTY_TABLE = new HashMap();
/*      */ 
/*  323 */   Hashtable fHiddenNodes = null;
/*      */ 
/*  349 */   private Map fTraversed = new HashMap();
/*      */ 
/*  353 */   private Map fDoc2SystemId = new HashMap();
/*      */ 
/*  356 */   private XSDocumentInfo fRoot = null;
/*      */ 
/*  360 */   private Map fDoc2XSDocumentMap = new HashMap();
/*      */ 
/*  364 */   private Map fRedefine2XSDMap = null;
/*      */ 
/*  367 */   private Map fRedefine2NSSupport = null;
/*      */ 
/*  374 */   private Map fRedefinedRestrictedAttributeGroupRegistry = new HashMap();
/*  375 */   private Map fRedefinedRestrictedGroupRegistry = new HashMap();
/*      */   private boolean fLastSchemaWasDuplicate;
/*  382 */   private boolean fValidateAnnotations = false;
/*      */ 
/*  385 */   private boolean fHonourAllSchemaLocations = false;
/*      */ 
/*  388 */   boolean fNamespaceGrowth = false;
/*      */ 
/*  391 */   boolean fTolerateDuplicates = false;
/*      */   private XMLErrorReporter fErrorReporter;
/*      */   private XMLEntityResolver fEntityResolver;
/*      */   private XSAttributeChecker fAttributeChecker;
/*      */   private SymbolTable fSymbolTable;
/*      */   private XSGrammarBucket fGrammarBucket;
/*      */   private XSDDescription fSchemaGrammarDescription;
/*      */   private XMLGrammarPool fGrammarPool;
/*      */   XSDAttributeGroupTraverser fAttributeGroupTraverser;
/*      */   XSDAttributeTraverser fAttributeTraverser;
/*      */   XSDComplexTypeTraverser fComplexTypeTraverser;
/*      */   XSDElementTraverser fElementTraverser;
/*      */   XSDGroupTraverser fGroupTraverser;
/*      */   XSDKeyrefTraverser fKeyrefTraverser;
/*      */   XSDNotationTraverser fNotationTraverser;
/*      */   XSDSimpleTypeTraverser fSimpleTypeTraverser;
/*      */   XSDUniqueOrKeyTraverser fUniqueOrKeyTraverser;
/*      */   XSDWildcardTraverser fWildCardTraverser;
/*      */   SchemaDVFactory fDVFactory;
/*      */   SchemaDOMParser fSchemaParser;
/*      */   SchemaContentHandler fXSContentHandler;
/*      */   StAXSchemaParser fStAXSchemaParser;
/*      */   XML11Configuration fAnnotationValidator;
/*      */   XSAnnotationGrammarPool fGrammarBucketAdapter;
/*      */   private static final int INIT_STACK_SIZE = 30;
/*      */   private static final int INC_STACK_SIZE = 10;
/*  439 */   private int fLocalElemStackPos = 0;
/*      */ 
/*  441 */   private XSParticleDecl[] fParticle = new XSParticleDecl[30];
/*  442 */   private Element[] fLocalElementDecl = new Element[30];
/*  443 */   private XSDocumentInfo[] fLocalElementDecl_schema = new XSDocumentInfo[30];
/*  444 */   private int[] fAllContext = new int[30];
/*  445 */   private XSObject[] fParent = new XSObject[30];
/*  446 */   private String[][] fLocalElemNamespaceContext = new String[30][1];
/*      */   private static final int INIT_KEYREF_STACK = 2;
/*      */   private static final int INC_KEYREF_STACK_AMOUNT = 2;
/*  456 */   private int fKeyrefStackPos = 0;
/*      */ 
/*  458 */   private Element[] fKeyrefs = new Element[2];
/*  459 */   private XSDocumentInfo[] fKeyrefsMapXSDocumentInfo = new XSDocumentInfo[2];
/*  460 */   private XSElementDecl[] fKeyrefElems = new XSElementDecl[2];
/*  461 */   private String[][] fKeyrefNamespaceContext = new String[2][1];
/*      */ 
/*  464 */   SymbolHash fGlobalAttrDecls = new SymbolHash();
/*  465 */   SymbolHash fGlobalAttrGrpDecls = new SymbolHash();
/*  466 */   SymbolHash fGlobalElemDecls = new SymbolHash();
/*  467 */   SymbolHash fGlobalGroupDecls = new SymbolHash();
/*  468 */   SymbolHash fGlobalNotationDecls = new SymbolHash();
/*  469 */   SymbolHash fGlobalIDConstraintDecls = new SymbolHash();
/*  470 */   SymbolHash fGlobalTypeDecls = new SymbolHash();
/*      */ 
/*  748 */   private static final String[][] NS_ERROR_CODES = { { "src-include.2.1", "src-include.2.1" }, { "src-redefine.3.1", "src-redefine.3.1" }, { "src-import.3.1", "src-import.3.2" }, null, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" } };
/*      */ 
/*  759 */   private static final String[] ELE_ERROR_CODES = { "src-include.1", "src-redefine.2", "src-import.2", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4" };
/*      */ 
/* 1483 */   private Vector fReportedTNS = null;
/*      */ 
/* 1496 */   private static final String[] COMP_TYPE = { null, "attribute declaration", "attribute group", "element declaration", "group", "identity constraint", "notation", "type definition" };
/*      */ 
/* 1507 */   private static final String[] CIRCULAR_CODES = { "Internal-Error", "Internal-Error", "src-attribute_group.3", "e-props-correct.6", "mg-props-correct.2", "Internal-Error", "Internal-Error", "st-props-correct.2" };
/*      */ 
/* 4088 */   private SimpleLocator xl = new SimpleLocator();
/*      */ 
/*      */   private String null2EmptyString(String ns)
/*      */   {
/*  327 */     return ns == null ? XMLSymbols.EMPTY_STRING : ns;
/*      */   }
/*      */   private String emptyString2Null(String ns) {
/*  330 */     return ns == XMLSymbols.EMPTY_STRING ? null : ns;
/*      */   }
/*      */ 
/*      */   private String doc2SystemId(Element ele) {
/*  334 */     String documentURI = null;
/*      */ 
/*  338 */     if ((ele.getOwnerDocument() instanceof SchemaDOM)) {
/*  339 */       documentURI = ((SchemaDOM)ele.getOwnerDocument()).getDocumentURI();
/*      */     }
/*  341 */     return documentURI != null ? documentURI : (String)this.fDoc2SystemId.get(ele);
/*      */   }
/*      */ 
/*      */   public XSDHandler()
/*      */   {
/*  474 */     this.fHiddenNodes = new Hashtable();
/*  475 */     this.fSchemaParser = new SchemaDOMParser(new SchemaParsingConfig());
/*      */   }
/*      */ 
/*      */   public XSDHandler(XSGrammarBucket gBucket)
/*      */   {
/*  482 */     this();
/*  483 */     this.fGrammarBucket = gBucket;
/*      */ 
/*  488 */     this.fSchemaGrammarDescription = new XSDDescription();
/*      */   }
/*      */ 
/*      */   public SchemaGrammar parseSchema(XMLInputSource is, XSDDescription desc, Map locationPairs)
/*      */     throws IOException
/*      */   {
/*  505 */     this.fLocationPairs = locationPairs;
/*  506 */     this.fSchemaParser.resetNodePool();
/*  507 */     SchemaGrammar grammar = null;
/*  508 */     String schemaNamespace = null;
/*  509 */     short referType = desc.getContextType();
/*      */ 
/*  517 */     if (referType != 3)
/*      */     {
/*  519 */       if ((this.fHonourAllSchemaLocations) && (referType == 2) && (isExistingGrammar(desc, this.fNamespaceGrowth))) {
/*  520 */         grammar = this.fGrammarBucket.getGrammar(desc.getTargetNamespace());
/*      */       }
/*      */       else {
/*  523 */         grammar = findGrammar(desc, this.fNamespaceGrowth);
/*      */       }
/*  525 */       if (grammar != null) {
/*  526 */         if (!this.fNamespaceGrowth) {
/*  527 */           return grammar;
/*      */         }
/*      */         try
/*      */         {
/*  531 */           if (grammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(is.getSystemId(), is.getBaseSystemId(), false))) {
/*  532 */             return grammar;
/*      */           }
/*      */         }
/*      */         catch (URI.MalformedURIException e)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  541 */       schemaNamespace = desc.getTargetNamespace();
/*      */ 
/*  543 */       if (schemaNamespace != null) {
/*  544 */         schemaNamespace = this.fSymbolTable.addSymbol(schemaNamespace);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  550 */     prepareForParse();
/*      */ 
/*  552 */     Element schemaRoot = null;
/*      */ 
/*  554 */     if ((is instanceof DOMInputSource)) {
/*  555 */       schemaRoot = getSchemaDocument(schemaNamespace, (DOMInputSource)is, referType == 3, referType, null);
/*      */     }
/*  559 */     else if ((is instanceof SAXInputSource)) {
/*  560 */       schemaRoot = getSchemaDocument(schemaNamespace, (SAXInputSource)is, referType == 3, referType, null);
/*      */     }
/*  564 */     else if ((is instanceof StAXInputSource)) {
/*  565 */       schemaRoot = getSchemaDocument(schemaNamespace, (StAXInputSource)is, referType == 3, referType, null);
/*      */     }
/*  569 */     else if ((is instanceof XSInputSource)) {
/*  570 */       schemaRoot = getSchemaDocument((XSInputSource)is, desc);
/*      */     }
/*      */     else {
/*  573 */       schemaRoot = getSchemaDocument(schemaNamespace, is, referType == 3, referType, null);
/*      */     }
/*      */ 
/*  579 */     if (schemaRoot == null)
/*      */     {
/*  581 */       if ((is instanceof XSInputSource)) {
/*  582 */         return this.fGrammarBucket.getGrammar(desc.getTargetNamespace());
/*      */       }
/*  584 */       return grammar;
/*      */     }
/*      */ 
/*  587 */     if (referType == 3) {
/*  588 */       Element schemaElem = schemaRoot;
/*  589 */       schemaNamespace = DOMUtil.getAttrValue(schemaElem, SchemaSymbols.ATT_TARGETNAMESPACE);
/*  590 */       if ((schemaNamespace != null) && (schemaNamespace.length() > 0))
/*      */       {
/*  593 */         schemaNamespace = this.fSymbolTable.addSymbol(schemaNamespace);
/*  594 */         desc.setTargetNamespace(schemaNamespace);
/*      */       }
/*      */       else {
/*  597 */         schemaNamespace = null;
/*      */       }
/*  599 */       grammar = findGrammar(desc, this.fNamespaceGrowth);
/*  600 */       String schemaId = XMLEntityManager.expandSystemId(is.getSystemId(), is.getBaseSystemId(), false);
/*  601 */       if (grammar != null)
/*      */       {
/*  604 */         if ((!this.fNamespaceGrowth) || ((schemaId != null) && (grammar.getDocumentLocations().contains(schemaId)))) {
/*  605 */           return grammar;
/*      */         }
/*      */       }
/*      */ 
/*  609 */       XSDKey key = new XSDKey(schemaId, referType, schemaNamespace);
/*  610 */       this.fTraversed.put(key, schemaRoot);
/*  611 */       if (schemaId != null) {
/*  612 */         this.fDoc2SystemId.put(schemaRoot, schemaId);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  618 */     prepareForTraverse();
/*      */ 
/*  620 */     this.fRoot = constructTrees(schemaRoot, is.getSystemId(), desc, grammar != null);
/*  621 */     if (this.fRoot == null) {
/*  622 */       return null;
/*      */     }
/*      */ 
/*  626 */     buildGlobalNameRegistries();
/*      */ 
/*  629 */     ArrayList annotationInfo = this.fValidateAnnotations ? new ArrayList() : null;
/*  630 */     traverseSchemas(annotationInfo);
/*      */ 
/*  633 */     traverseLocalElements();
/*      */ 
/*  636 */     resolveKeyRefs();
/*      */ 
/*  644 */     for (int i = this.fAllTNSs.size() - 1; i >= 0; i--)
/*      */     {
/*  646 */       String tns = (String)this.fAllTNSs.elementAt(i);
/*      */ 
/*  648 */       Vector ins = (Vector)this.fImportMap.get(tns);
/*      */ 
/*  650 */       SchemaGrammar sg = this.fGrammarBucket.getGrammar(emptyString2Null(tns));
/*  651 */       if (sg != null)
/*      */       {
/*  655 */         int count = 0;
/*  656 */         for (int j = 0; j < ins.size(); j++)
/*      */         {
/*  658 */           SchemaGrammar isg = this.fGrammarBucket.getGrammar((String)ins.elementAt(j));
/*      */ 
/*  660 */           if (isg != null)
/*  661 */             ins.setElementAt(isg, count++);
/*      */         }
/*  663 */         ins.setSize(count);
/*      */ 
/*  665 */         sg.setImportedGrammars(ins);
/*      */       }
/*      */     }
/*      */ 
/*  669 */     if ((this.fValidateAnnotations) && (annotationInfo.size() > 0)) {
/*  670 */       validateAnnotations(annotationInfo);
/*      */     }
/*      */ 
/*  674 */     return this.fGrammarBucket.getGrammar(this.fRoot.fTargetNamespace);
/*      */   }
/*      */ 
/*      */   private void validateAnnotations(ArrayList annotationInfo) {
/*  678 */     if (this.fAnnotationValidator == null) {
/*  679 */       createAnnotationValidator();
/*      */     }
/*  681 */     int size = annotationInfo.size();
/*  682 */     XMLInputSource src = new XMLInputSource(null, null, null);
/*  683 */     this.fGrammarBucketAdapter.refreshGrammars(this.fGrammarBucket);
/*  684 */     for (int i = 0; i < size; i += 2) {
/*  685 */       src.setSystemId((String)annotationInfo.get(i));
/*  686 */       XSAnnotationInfo annotation = (XSAnnotationInfo)annotationInfo.get(i + 1);
/*  687 */       while (annotation != null) {
/*  688 */         src.setCharacterStream(new StringReader(annotation.fAnnotation));
/*      */         try {
/*  690 */           this.fAnnotationValidator.parse(src);
/*      */         } catch (IOException exc) {
/*      */         }
/*  693 */         annotation = annotation.next;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void createAnnotationValidator() {
/*  699 */     this.fAnnotationValidator = new XML11Configuration();
/*  700 */     this.fGrammarBucketAdapter = new XSAnnotationGrammarPool(null);
/*  701 */     this.fAnnotationValidator.setFeature("http://xml.org/sax/features/validation", true);
/*  702 */     this.fAnnotationValidator.setFeature("http://apache.org/xml/features/validation/schema", true);
/*  703 */     this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarBucketAdapter);
/*      */ 
/*  705 */     XMLErrorHandler errorHandler = this.fErrorReporter.getErrorHandler();
/*  706 */     this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/error-handler", errorHandler != null ? errorHandler : new DefaultErrorHandler());
/*      */ 
/*  708 */     Locale locale = this.fErrorReporter.getLocale();
/*  709 */     this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/locale", locale);
/*      */   }
/*      */ 
/*      */   SchemaGrammar getGrammar(String tns)
/*      */   {
/*  717 */     return this.fGrammarBucket.getGrammar(tns);
/*      */   }
/*      */ 
/*      */   protected SchemaGrammar findGrammar(XSDDescription desc, boolean ignoreConflict)
/*      */   {
/*  726 */     SchemaGrammar sg = this.fGrammarBucket.getGrammar(desc.getTargetNamespace());
/*  727 */     if ((sg == null) && 
/*  728 */       (this.fGrammarPool != null)) {
/*  729 */       sg = (SchemaGrammar)this.fGrammarPool.retrieveGrammar(desc);
/*  730 */       if (sg != null)
/*      */       {
/*  733 */         if (!this.fGrammarBucket.putGrammar(sg, true, ignoreConflict))
/*      */         {
/*  736 */           reportSchemaWarning("GrammarConflict", null, null);
/*  737 */           sg = null;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  742 */     return sg;
/*      */   }
/*      */ 
/*      */   protected XSDocumentInfo constructTrees(Element schemaRoot, String locationHint, XSDDescription desc, boolean nsCollision)
/*      */   {
/*  775 */     if (schemaRoot == null) return null;
/*  776 */     String callerTNS = desc.getTargetNamespace();
/*  777 */     short referType = desc.getContextType();
/*      */ 
/*  779 */     XSDocumentInfo currSchemaInfo = null;
/*      */     try
/*      */     {
/*  782 */       currSchemaInfo = new XSDocumentInfo(schemaRoot, this.fAttributeChecker, this.fSymbolTable);
/*      */     } catch (XMLSchemaException se) {
/*  784 */       reportSchemaError(ELE_ERROR_CODES[referType], new Object[] { locationHint }, schemaRoot);
/*      */ 
/*  787 */       return null;
/*      */     }
/*      */ 
/*  790 */     if ((currSchemaInfo.fTargetNamespace != null) && (currSchemaInfo.fTargetNamespace.length() == 0))
/*      */     {
/*  792 */       reportSchemaWarning("EmptyTargetNamespace", new Object[] { locationHint }, schemaRoot);
/*      */ 
/*  795 */       currSchemaInfo.fTargetNamespace = null;
/*      */     }
/*      */ 
/*  798 */     if (callerTNS != null)
/*      */     {
/*  801 */       int secondIdx = 0;
/*      */ 
/*  803 */       if ((referType == 0) || (referType == 1))
/*      */       {
/*  807 */         if (currSchemaInfo.fTargetNamespace == null) {
/*  808 */           currSchemaInfo.fTargetNamespace = callerTNS;
/*  809 */           currSchemaInfo.fIsChameleonSchema = true;
/*      */         }
/*  813 */         else if (callerTNS != currSchemaInfo.fTargetNamespace) {
/*  814 */           reportSchemaError(NS_ERROR_CODES[referType][secondIdx], new Object[] { callerTNS, currSchemaInfo.fTargetNamespace }, schemaRoot);
/*      */ 
/*  817 */           return null;
/*      */         }
/*      */ 
/*      */       }
/*  821 */       else if ((referType != 3) && (callerTNS != currSchemaInfo.fTargetNamespace)) {
/*  822 */         reportSchemaError(NS_ERROR_CODES[referType][secondIdx], new Object[] { callerTNS, currSchemaInfo.fTargetNamespace }, schemaRoot);
/*      */ 
/*  825 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*  830 */     else if (currSchemaInfo.fTargetNamespace != null)
/*      */     {
/*  832 */       if (referType == 3) {
/*  833 */         desc.setTargetNamespace(currSchemaInfo.fTargetNamespace);
/*  834 */         callerTNS = currSchemaInfo.fTargetNamespace;
/*      */       }
/*      */       else
/*      */       {
/*  839 */         int secondIdx = 1;
/*  840 */         reportSchemaError(NS_ERROR_CODES[referType][secondIdx], new Object[] { callerTNS, currSchemaInfo.fTargetNamespace }, schemaRoot);
/*      */ 
/*  843 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  850 */     currSchemaInfo.addAllowedNS(currSchemaInfo.fTargetNamespace);
/*      */ 
/*  852 */     SchemaGrammar sg = null;
/*      */ 
/*  855 */     if (nsCollision) {
/*  856 */       SchemaGrammar sg2 = this.fGrammarBucket.getGrammar(currSchemaInfo.fTargetNamespace);
/*  857 */       if (sg2.isImmutable()) {
/*  858 */         sg = new SchemaGrammar(sg2);
/*  859 */         this.fGrammarBucket.putGrammar(sg);
/*      */ 
/*  861 */         updateImportListWith(sg);
/*      */       }
/*      */       else {
/*  864 */         sg = sg2;
/*      */       }
/*      */ 
/*  868 */       updateImportListFor(sg);
/*      */     }
/*  870 */     else if ((referType == 0) || (referType == 1))
/*      */     {
/*  872 */       sg = this.fGrammarBucket.getGrammar(currSchemaInfo.fTargetNamespace);
/*      */     }
/*  874 */     else if ((this.fHonourAllSchemaLocations) && (referType == 2)) {
/*  875 */       sg = findGrammar(desc, false);
/*  876 */       if (sg == null) {
/*  877 */         sg = new SchemaGrammar(currSchemaInfo.fTargetNamespace, desc.makeClone(), this.fSymbolTable);
/*  878 */         this.fGrammarBucket.putGrammar(sg);
/*      */       }
/*      */     }
/*      */     else {
/*  882 */       sg = new SchemaGrammar(currSchemaInfo.fTargetNamespace, desc.makeClone(), this.fSymbolTable);
/*  883 */       this.fGrammarBucket.putGrammar(sg);
/*      */     }
/*      */ 
/*  888 */     sg.addDocument(null, (String)this.fDoc2SystemId.get(currSchemaInfo.fSchemaElement));
/*      */ 
/*  890 */     this.fDoc2XSDocumentMap.put(schemaRoot, currSchemaInfo);
/*  891 */     Vector dependencies = new Vector();
/*  892 */     Element rootNode = schemaRoot;
/*      */ 
/*  894 */     Element newSchemaRoot = null;
/*  895 */     for (Element child = DOMUtil.getFirstChildElement(rootNode); 
/*  896 */       child != null; 
/*  897 */       child = DOMUtil.getNextSiblingElement(child)) {
/*  898 */       String schemaNamespace = null;
/*  899 */       String schemaHint = null;
/*  900 */       String localName = DOMUtil.getLocalName(child);
/*      */ 
/*  902 */       short refType = -1;
/*  903 */       boolean importCollision = false;
/*      */ 
/*  905 */       if (!localName.equals(SchemaSymbols.ELT_ANNOTATION))
/*      */       {
/*  907 */         if (localName.equals(SchemaSymbols.ELT_IMPORT)) {
/*  908 */           refType = 2;
/*      */ 
/*  911 */           Object[] importAttrs = this.fAttributeChecker.checkAttributes(child, true, currSchemaInfo);
/*  912 */           schemaHint = (String)importAttrs[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
/*  913 */           schemaNamespace = (String)importAttrs[XSAttributeChecker.ATTIDX_NAMESPACE];
/*  914 */           if (schemaNamespace != null) {
/*  915 */             schemaNamespace = this.fSymbolTable.addSymbol(schemaNamespace);
/*      */           }
/*      */ 
/*  918 */           Element importChild = DOMUtil.getFirstChildElement(child);
/*  919 */           if (importChild != null) {
/*  920 */             String importComponentType = DOMUtil.getLocalName(importChild);
/*  921 */             if (importComponentType.equals(SchemaSymbols.ELT_ANNOTATION))
/*      */             {
/*  923 */               sg.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(importChild, importAttrs, true, currSchemaInfo));
/*      */             }
/*      */             else {
/*  926 */               reportSchemaError("s4s-elt-must-match.1", new Object[] { localName, "annotation?", importComponentType }, child);
/*      */             }
/*  928 */             if (DOMUtil.getNextSiblingElement(importChild) != null)
/*  929 */               reportSchemaError("s4s-elt-must-match.1", new Object[] { localName, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(importChild)) }, child);
/*      */           }
/*      */           else
/*      */           {
/*  933 */             String text = DOMUtil.getSyntheticAnnotation(child);
/*  934 */             if (text != null) {
/*  935 */               sg.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(child, text, importAttrs, true, currSchemaInfo));
/*      */             }
/*      */           }
/*  938 */           this.fAttributeChecker.returnAttrArray(importAttrs, currSchemaInfo);
/*      */ 
/*  941 */           if (schemaNamespace == currSchemaInfo.fTargetNamespace) {
/*  942 */             reportSchemaError(schemaNamespace != null ? "src-import.1.1" : "src-import.1.2", new Object[] { schemaNamespace }, child);
/*      */ 
/*  944 */             continue;
/*      */           }
/*      */ 
/*  949 */           if (currSchemaInfo.isAllowedNS(schemaNamespace)) {
/*  950 */             if ((!this.fHonourAllSchemaLocations) && (!this.fNamespaceGrowth))
/*  951 */               continue;
/*      */           }
/*      */           else {
/*  954 */             currSchemaInfo.addAllowedNS(schemaNamespace);
/*      */           }
/*      */ 
/*  958 */           String tns = null2EmptyString(currSchemaInfo.fTargetNamespace);
/*      */ 
/*  960 */           Vector ins = (Vector)this.fImportMap.get(tns);
/*      */ 
/*  962 */           if (ins == null)
/*      */           {
/*  964 */             this.fAllTNSs.addElement(tns);
/*  965 */             ins = new Vector();
/*  966 */             this.fImportMap.put(tns, ins);
/*  967 */             ins.addElement(schemaNamespace);
/*      */           }
/*  969 */           else if (!ins.contains(schemaNamespace)) {
/*  970 */             ins.addElement(schemaNamespace);
/*      */           }
/*      */ 
/*  973 */           this.fSchemaGrammarDescription.reset();
/*  974 */           this.fSchemaGrammarDescription.setContextType((short)2);
/*  975 */           this.fSchemaGrammarDescription.setBaseSystemId(doc2SystemId(schemaRoot));
/*  976 */           this.fSchemaGrammarDescription.setLiteralSystemId(schemaHint);
/*  977 */           this.fSchemaGrammarDescription.setLocationHints(new String[] { schemaHint });
/*  978 */           this.fSchemaGrammarDescription.setTargetNamespace(schemaNamespace);
/*      */ 
/*  982 */           SchemaGrammar isg = findGrammar(this.fSchemaGrammarDescription, this.fNamespaceGrowth);
/*  983 */           if (isg != null) {
/*  984 */             if (this.fNamespaceGrowth)
/*      */               try {
/*  986 */                 if (isg.getDocumentLocations().contains(XMLEntityManager.expandSystemId(schemaHint, this.fSchemaGrammarDescription.getBaseSystemId(), false)))
/*      */                 {
/*      */                   continue;
/*      */                 }
/*  990 */                 importCollision = true;
/*      */               }
/*      */               catch (URI.MalformedURIException e)
/*      */               {
/*      */               }
/*      */             else {
/*  996 */               if ((!this.fHonourAllSchemaLocations) || (isExistingGrammar(this.fSchemaGrammarDescription, false)))
/*      */               {
/*      */                 continue;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1008 */           newSchemaRoot = resolveSchema(this.fSchemaGrammarDescription, false, child, isg == null);
/*      */         } else {
/* 1010 */           if ((!localName.equals(SchemaSymbols.ELT_INCLUDE)) && (!localName.equals(SchemaSymbols.ELT_REDEFINE)))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/* 1015 */           Object[] includeAttrs = this.fAttributeChecker.checkAttributes(child, true, currSchemaInfo);
/* 1016 */           schemaHint = (String)includeAttrs[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
/*      */ 
/* 1018 */           if (localName.equals(SchemaSymbols.ELT_REDEFINE)) {
/* 1019 */             if (this.fRedefine2NSSupport == null) this.fRedefine2NSSupport = new HashMap();
/* 1020 */             this.fRedefine2NSSupport.put(child, new SchemaNamespaceSupport(currSchemaInfo.fNamespaceSupport));
/*      */           }
/*      */ 
/* 1025 */           if (localName.equals(SchemaSymbols.ELT_INCLUDE)) {
/* 1026 */             Element includeChild = DOMUtil.getFirstChildElement(child);
/* 1027 */             if (includeChild != null) {
/* 1028 */               String includeComponentType = DOMUtil.getLocalName(includeChild);
/* 1029 */               if (includeComponentType.equals(SchemaSymbols.ELT_ANNOTATION))
/*      */               {
/* 1031 */                 sg.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(includeChild, includeAttrs, true, currSchemaInfo));
/*      */               }
/*      */               else {
/* 1034 */                 reportSchemaError("s4s-elt-must-match.1", new Object[] { localName, "annotation?", includeComponentType }, child);
/*      */               }
/* 1036 */               if (DOMUtil.getNextSiblingElement(includeChild) != null)
/* 1037 */                 reportSchemaError("s4s-elt-must-match.1", new Object[] { localName, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(includeChild)) }, child);
/*      */             }
/*      */             else
/*      */             {
/* 1041 */               String text = DOMUtil.getSyntheticAnnotation(child);
/* 1042 */               if (text != null)
/* 1043 */                 sg.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(child, text, includeAttrs, true, currSchemaInfo));
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1048 */             for (Element redefinedChild = DOMUtil.getFirstChildElement(child); 
/* 1049 */               redefinedChild != null; 
/* 1050 */               redefinedChild = DOMUtil.getNextSiblingElement(redefinedChild)) {
/* 1051 */               String redefinedComponentType = DOMUtil.getLocalName(redefinedChild);
/* 1052 */               if (redefinedComponentType.equals(SchemaSymbols.ELT_ANNOTATION))
/*      */               {
/* 1054 */                 sg.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(redefinedChild, includeAttrs, true, currSchemaInfo));
/*      */ 
/* 1056 */                 DOMUtil.setHidden(redefinedChild, this.fHiddenNodes);
/*      */               }
/*      */               else {
/* 1059 */                 String text = DOMUtil.getSyntheticAnnotation(child);
/* 1060 */                 if (text != null) {
/* 1061 */                   sg.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(child, text, includeAttrs, true, currSchemaInfo));
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/* 1067 */           this.fAttributeChecker.returnAttrArray(includeAttrs, currSchemaInfo);
/*      */ 
/* 1069 */           if (schemaHint == null) {
/* 1070 */             reportSchemaError("s4s-att-must-appear", new Object[] { "<include> or <redefine>", "schemaLocation" }, child);
/*      */           }
/*      */ 
/* 1075 */           boolean mustResolve = false;
/* 1076 */           refType = 0;
/* 1077 */           if (localName.equals(SchemaSymbols.ELT_REDEFINE)) {
/* 1078 */             mustResolve = nonAnnotationContent(child);
/* 1079 */             refType = 1;
/*      */           }
/* 1081 */           this.fSchemaGrammarDescription.reset();
/* 1082 */           this.fSchemaGrammarDescription.setContextType(refType);
/* 1083 */           this.fSchemaGrammarDescription.setBaseSystemId(doc2SystemId(schemaRoot));
/* 1084 */           this.fSchemaGrammarDescription.setLocationHints(new String[] { schemaHint });
/* 1085 */           this.fSchemaGrammarDescription.setTargetNamespace(callerTNS);
/*      */ 
/* 1087 */           boolean alreadyTraversed = false;
/* 1088 */           XMLInputSource schemaSource = resolveSchemaSource(this.fSchemaGrammarDescription, mustResolve, child, true);
/* 1089 */           if ((this.fNamespaceGrowth) && (refType == 0)) {
/*      */             try {
/* 1091 */               String schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
/* 1092 */               alreadyTraversed = sg.getDocumentLocations().contains(schemaId);
/*      */             }
/*      */             catch (URI.MalformedURIException e)
/*      */             {
/*      */             }
/*      */           }
/*      */ 
/* 1099 */           if (!alreadyTraversed) {
/* 1100 */             newSchemaRoot = resolveSchema(schemaSource, this.fSchemaGrammarDescription, mustResolve, child);
/* 1101 */             schemaNamespace = currSchemaInfo.fTargetNamespace;
/*      */           }
/*      */           else {
/* 1104 */             this.fLastSchemaWasDuplicate = true;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1115 */         XSDocumentInfo newSchemaInfo = null;
/* 1116 */         if (this.fLastSchemaWasDuplicate) {
/* 1117 */           newSchemaInfo = newSchemaRoot == null ? null : (XSDocumentInfo)this.fDoc2XSDocumentMap.get(newSchemaRoot);
/*      */         }
/*      */         else {
/* 1120 */           newSchemaInfo = constructTrees(newSchemaRoot, schemaHint, this.fSchemaGrammarDescription, importCollision);
/*      */         }
/*      */ 
/* 1123 */         if ((localName.equals(SchemaSymbols.ELT_REDEFINE)) && (newSchemaInfo != null))
/*      */         {
/* 1127 */           if (this.fRedefine2XSDMap == null) this.fRedefine2XSDMap = new HashMap();
/* 1128 */           this.fRedefine2XSDMap.put(child, newSchemaInfo);
/*      */         }
/* 1130 */         if (newSchemaRoot != null) {
/* 1131 */           if (newSchemaInfo != null)
/* 1132 */             dependencies.addElement(newSchemaInfo);
/* 1133 */           newSchemaRoot = null;
/*      */         }
/*      */       }
/*      */     }
/* 1137 */     this.fDependencyMap.put(currSchemaInfo, dependencies);
/* 1138 */     return currSchemaInfo;
/*      */   }
/*      */ 
/*      */   private boolean isExistingGrammar(XSDDescription desc, boolean ignoreConflict) {
/* 1142 */     SchemaGrammar sg = this.fGrammarBucket.getGrammar(desc.getTargetNamespace());
/* 1143 */     if (sg == null) {
/* 1144 */       return findGrammar(desc, ignoreConflict) != null;
/*      */     }
/* 1146 */     if (sg.isImmutable()) {
/* 1147 */       return true;
/*      */     }
/*      */     try
/*      */     {
/* 1151 */       return sg.getDocumentLocations().contains(XMLEntityManager.expandSystemId(desc.getLiteralSystemId(), desc.getBaseSystemId(), false));
/*      */     } catch (URI.MalformedURIException e) {
/*      */     }
/* 1154 */     return false;
/*      */   }
/*      */ 
/*      */   private void updateImportListFor(SchemaGrammar grammar)
/*      */   {
/* 1168 */     Vector importedGrammars = grammar.getImportedGrammars();
/* 1169 */     if (importedGrammars != null)
/* 1170 */       for (int i = 0; i < importedGrammars.size(); i++) {
/* 1171 */         SchemaGrammar isg1 = (SchemaGrammar)importedGrammars.elementAt(i);
/* 1172 */         SchemaGrammar isg2 = this.fGrammarBucket.getGrammar(isg1.getTargetNamespace());
/* 1173 */         if ((isg2 != null) && (isg1 != isg2))
/* 1174 */           importedGrammars.set(i, isg2);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void updateImportListWith(SchemaGrammar newGrammar)
/*      */   {
/* 1190 */     SchemaGrammar[] schemaGrammars = this.fGrammarBucket.getGrammars();
/* 1191 */     for (int i = 0; i < schemaGrammars.length; i++) {
/* 1192 */       SchemaGrammar sg = schemaGrammars[i];
/* 1193 */       if (sg != newGrammar) {
/* 1194 */         Vector importedGrammars = sg.getImportedGrammars();
/* 1195 */         if (importedGrammars != null)
/* 1196 */           for (int j = 0; j < importedGrammars.size(); j++) {
/* 1197 */             SchemaGrammar isg = (SchemaGrammar)importedGrammars.elementAt(j);
/* 1198 */             if (null2EmptyString(isg.getTargetNamespace()).equals(null2EmptyString(newGrammar.getTargetNamespace()))) {
/* 1199 */               if (isg == newGrammar) break;
/* 1200 */               importedGrammars.set(j, newGrammar); break;
/*      */             }
/*      */           }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void buildGlobalNameRegistries()
/*      */   {
/* 1217 */     this.registryEmpty = false;
/*      */ 
/* 1228 */     Stack schemasToProcess = new Stack();
/* 1229 */     schemasToProcess.push(this.fRoot);
/*      */ 
/* 1231 */     while (!schemasToProcess.empty()) {
/* 1232 */       XSDocumentInfo currSchemaDoc = (XSDocumentInfo)schemasToProcess.pop();
/*      */ 
/* 1234 */       Element currDoc = currSchemaDoc.fSchemaElement;
/* 1235 */       if (!DOMUtil.isHidden(currDoc, this.fHiddenNodes))
/*      */       {
/* 1240 */         Element currRoot = currDoc;
/*      */ 
/* 1242 */         boolean dependenciesCanOccur = true;
/* 1243 */         for (Element globalComp = DOMUtil.getFirstChildElement(currRoot); 
/* 1245 */           globalComp != null; 
/* 1246 */           globalComp = DOMUtil.getNextSiblingElement(globalComp))
/*      */         {
/* 1249 */           if (!DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_ANNOTATION))
/*      */           {
/* 1253 */             if ((DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_INCLUDE)) || (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_IMPORT)))
/*      */             {
/* 1255 */               if (!dependenciesCanOccur) {
/* 1256 */                 reportSchemaError("s4s-elt-invalid-content.3", new Object[] { DOMUtil.getLocalName(globalComp) }, globalComp);
/*      */               }
/* 1258 */               DOMUtil.setHidden(globalComp, this.fHiddenNodes);
/*      */             }
/* 1260 */             else if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_REDEFINE)) {
/* 1261 */               if (!dependenciesCanOccur) {
/* 1262 */                 reportSchemaError("s4s-elt-invalid-content.3", new Object[] { DOMUtil.getLocalName(globalComp) }, globalComp);
/*      */               }
/* 1264 */               for (Element redefineComp = DOMUtil.getFirstChildElement(globalComp); 
/* 1265 */                 redefineComp != null; 
/* 1266 */                 redefineComp = DOMUtil.getNextSiblingElement(redefineComp)) {
/* 1267 */                 String lName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME);
/* 1268 */                 if (lName.length() != 0)
/*      */                 {
/* 1270 */                   String qName = currSchemaDoc.fTargetNamespace + "," + lName;
/*      */ 
/* 1273 */                   String componentType = DOMUtil.getLocalName(redefineComp);
/* 1274 */                   if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
/* 1275 */                     checkForDuplicateNames(qName, 2, this.fUnparsedAttributeGroupRegistry, this.fUnparsedAttributeGroupRegistrySub, redefineComp, currSchemaDoc);
/*      */ 
/* 1277 */                     String targetLName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
/*      */ 
/* 1279 */                     renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_ATTRIBUTEGROUP, lName, targetLName);
/*      */                   }
/* 1282 */                   else if ((componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) || (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)))
/*      */                   {
/* 1284 */                     checkForDuplicateNames(qName, 7, this.fUnparsedTypeRegistry, this.fUnparsedTypeRegistrySub, redefineComp, currSchemaDoc);
/*      */ 
/* 1286 */                     String targetLName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
/*      */ 
/* 1288 */                     if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
/* 1289 */                       renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_COMPLEXTYPE, lName, targetLName);
/*      */                     }
/*      */                     else
/*      */                     {
/* 1293 */                       renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_SIMPLETYPE, lName, targetLName);
/*      */                     }
/*      */ 
/*      */                   }
/* 1297 */                   else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
/* 1298 */                     checkForDuplicateNames(qName, 4, this.fUnparsedGroupRegistry, this.fUnparsedGroupRegistrySub, redefineComp, currSchemaDoc);
/*      */ 
/* 1300 */                     String targetLName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
/*      */ 
/* 1302 */                     renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_GROUP, lName, targetLName);
/*      */                   }
/*      */                 }
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 1310 */               dependenciesCanOccur = false;
/* 1311 */               String lName = DOMUtil.getAttrValue(globalComp, SchemaSymbols.ATT_NAME);
/* 1312 */               if (lName.length() != 0)
/*      */               {
/* 1314 */                 String qName = currSchemaDoc.fTargetNamespace + "," + lName;
/*      */ 
/* 1317 */                 String componentType = DOMUtil.getLocalName(globalComp);
/*      */ 
/* 1319 */                 if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
/* 1320 */                   checkForDuplicateNames(qName, 1, this.fUnparsedAttributeRegistry, this.fUnparsedAttributeRegistrySub, globalComp, currSchemaDoc);
/*      */                 }
/* 1322 */                 else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
/* 1323 */                   checkForDuplicateNames(qName, 2, this.fUnparsedAttributeGroupRegistry, this.fUnparsedAttributeGroupRegistrySub, globalComp, currSchemaDoc);
/*      */                 }
/* 1325 */                 else if ((componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) || (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)))
/*      */                 {
/* 1327 */                   checkForDuplicateNames(qName, 7, this.fUnparsedTypeRegistry, this.fUnparsedTypeRegistrySub, globalComp, currSchemaDoc);
/*      */                 }
/* 1329 */                 else if (componentType.equals(SchemaSymbols.ELT_ELEMENT)) {
/* 1330 */                   checkForDuplicateNames(qName, 3, this.fUnparsedElementRegistry, this.fUnparsedElementRegistrySub, globalComp, currSchemaDoc);
/*      */                 }
/* 1332 */                 else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
/* 1333 */                   checkForDuplicateNames(qName, 4, this.fUnparsedGroupRegistry, this.fUnparsedGroupRegistrySub, globalComp, currSchemaDoc);
/*      */                 }
/* 1335 */                 else if (componentType.equals(SchemaSymbols.ELT_NOTATION)) {
/* 1336 */                   checkForDuplicateNames(qName, 6, this.fUnparsedNotationRegistry, this.fUnparsedNotationRegistrySub, globalComp, currSchemaDoc);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1342 */         DOMUtil.setHidden(currDoc, this.fHiddenNodes);
/*      */ 
/* 1344 */         Vector currSchemaDepends = (Vector)this.fDependencyMap.get(currSchemaDoc);
/* 1345 */         for (int i = 0; i < currSchemaDepends.size(); i++)
/* 1346 */           schemasToProcess.push(currSchemaDepends.elementAt(i));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void traverseSchemas(ArrayList annotationInfo)
/*      */   {
/* 1368 */     setSchemasVisible(this.fRoot);
/* 1369 */     Stack schemasToProcess = new Stack();
/* 1370 */     schemasToProcess.push(this.fRoot);
/* 1371 */     while (!schemasToProcess.empty()) {
/* 1372 */       XSDocumentInfo currSchemaDoc = (XSDocumentInfo)schemasToProcess.pop();
/*      */ 
/* 1374 */       Element currDoc = currSchemaDoc.fSchemaElement;
/*      */ 
/* 1376 */       SchemaGrammar currSG = this.fGrammarBucket.getGrammar(currSchemaDoc.fTargetNamespace);
/*      */ 
/* 1378 */       if (!DOMUtil.isHidden(currDoc, this.fHiddenNodes))
/*      */       {
/* 1382 */         Element currRoot = currDoc;
/* 1383 */         boolean sawAnnotation = false;
/*      */ 
/* 1385 */         for (Element globalComp = DOMUtil.getFirstVisibleChildElement(currRoot, this.fHiddenNodes); 
/* 1387 */           globalComp != null; 
/* 1388 */           globalComp = DOMUtil.getNextVisibleSiblingElement(globalComp, this.fHiddenNodes)) {
/* 1389 */           DOMUtil.setHidden(globalComp, this.fHiddenNodes);
/* 1390 */           String componentType = DOMUtil.getLocalName(globalComp);
/*      */ 
/* 1392 */           if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_REDEFINE))
/*      */           {
/* 1394 */             currSchemaDoc.backupNSSupport(this.fRedefine2NSSupport != null ? (SchemaNamespaceSupport)this.fRedefine2NSSupport.get(globalComp) : null);
/* 1395 */             for (Element redefinedComp = DOMUtil.getFirstVisibleChildElement(globalComp, this.fHiddenNodes); 
/* 1396 */               redefinedComp != null; 
/* 1397 */               redefinedComp = DOMUtil.getNextVisibleSiblingElement(redefinedComp, this.fHiddenNodes)) {
/* 1398 */               String redefinedComponentType = DOMUtil.getLocalName(redefinedComp);
/* 1399 */               DOMUtil.setHidden(redefinedComp, this.fHiddenNodes);
/* 1400 */               if (redefinedComponentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
/* 1401 */                 this.fAttributeGroupTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
/*      */               }
/* 1403 */               else if (redefinedComponentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
/* 1404 */                 this.fComplexTypeTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
/*      */               }
/* 1406 */               else if (redefinedComponentType.equals(SchemaSymbols.ELT_GROUP)) {
/* 1407 */                 this.fGroupTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
/*      */               }
/* 1409 */               else if (redefinedComponentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
/* 1410 */                 this.fSimpleTypeTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
/*      */               }
/*      */               else
/*      */               {
/* 1418 */                 reportSchemaError("s4s-elt-must-match.1", new Object[] { DOMUtil.getLocalName(globalComp), "(annotation | (simpleType | complexType | group | attributeGroup))*", redefinedComponentType }, redefinedComp);
/*      */               }
/*      */             }
/* 1421 */             currSchemaDoc.restoreNSSupport();
/*      */           }
/* 1423 */           else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
/* 1424 */             this.fAttributeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
/*      */           }
/* 1426 */           else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
/* 1427 */             this.fAttributeGroupTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
/*      */           }
/* 1429 */           else if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
/* 1430 */             this.fComplexTypeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
/*      */           }
/* 1432 */           else if (componentType.equals(SchemaSymbols.ELT_ELEMENT)) {
/* 1433 */             this.fElementTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
/*      */           }
/* 1435 */           else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
/* 1436 */             this.fGroupTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
/*      */           }
/* 1438 */           else if (componentType.equals(SchemaSymbols.ELT_NOTATION)) {
/* 1439 */             this.fNotationTraverser.traverse(globalComp, currSchemaDoc, currSG);
/*      */           }
/* 1441 */           else if (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
/* 1442 */             this.fSimpleTypeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
/*      */           }
/* 1444 */           else if (componentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
/* 1445 */             currSG.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(globalComp, currSchemaDoc.getSchemaAttrs(), true, currSchemaDoc));
/* 1446 */             sawAnnotation = true;
/*      */           }
/*      */           else {
/* 1449 */             reportSchemaError("s4s-elt-invalid-content.1", new Object[] { SchemaSymbols.ELT_SCHEMA, DOMUtil.getLocalName(globalComp) }, globalComp);
/*      */           }
/*      */         }
/*      */ 
/* 1453 */         if (!sawAnnotation) {
/* 1454 */           String text = DOMUtil.getSyntheticAnnotation(currRoot);
/* 1455 */           if (text != null) {
/* 1456 */             currSG.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(currRoot, text, currSchemaDoc.getSchemaAttrs(), true, currSchemaDoc));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1461 */         if (annotationInfo != null) {
/* 1462 */           XSAnnotationInfo info = currSchemaDoc.getAnnotations();
/*      */ 
/* 1464 */           if (info != null) {
/* 1465 */             annotationInfo.add(doc2SystemId(currDoc));
/* 1466 */             annotationInfo.add(info);
/*      */           }
/*      */         }
/*      */ 
/* 1470 */         currSchemaDoc.returnSchemaAttrs();
/* 1471 */         DOMUtil.setHidden(currDoc, this.fHiddenNodes);
/*      */ 
/* 1474 */         Vector currSchemaDepends = (Vector)this.fDependencyMap.get(currSchemaDoc);
/* 1475 */         for (int i = 0; i < currSchemaDepends.size(); i++)
/* 1476 */           schemasToProcess.push(currSchemaDepends.elementAt(i));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private final boolean needReportTNSError(String uri)
/*      */   {
/* 1488 */     if (this.fReportedTNS == null)
/* 1489 */       this.fReportedTNS = new Vector();
/* 1490 */     else if (this.fReportedTNS.contains(uri))
/* 1491 */       return false;
/* 1492 */     this.fReportedTNS.addElement(uri);
/* 1493 */     return true;
/*      */   }
/*      */ 
/*      */   void addGlobalAttributeDecl(XSAttributeDecl decl)
/*      */   {
/* 1520 */     String namespace = decl.getNamespace();
/* 1521 */     String declKey = namespace + "," + decl.getName();
/*      */ 
/* 1524 */     if (this.fGlobalAttrDecls.get(declKey) == null)
/* 1525 */       this.fGlobalAttrDecls.put(declKey, decl);
/*      */   }
/*      */ 
/*      */   void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl)
/*      */   {
/* 1531 */     String namespace = decl.getNamespace();
/* 1532 */     String declKey = namespace + "," + decl.getName();
/*      */ 
/* 1535 */     if (this.fGlobalAttrGrpDecls.get(declKey) == null)
/* 1536 */       this.fGlobalAttrGrpDecls.put(declKey, decl);
/*      */   }
/*      */ 
/*      */   void addGlobalElementDecl(XSElementDecl decl)
/*      */   {
/* 1542 */     String namespace = decl.getNamespace();
/* 1543 */     String declKey = namespace + "," + decl.getName();
/*      */ 
/* 1546 */     if (this.fGlobalElemDecls.get(declKey) == null)
/* 1547 */       this.fGlobalElemDecls.put(declKey, decl);
/*      */   }
/*      */ 
/*      */   void addGlobalGroupDecl(XSGroupDecl decl)
/*      */   {
/* 1553 */     String namespace = decl.getNamespace();
/* 1554 */     String declKey = namespace + "," + decl.getName();
/*      */ 
/* 1557 */     if (this.fGlobalGroupDecls.get(declKey) == null)
/* 1558 */       this.fGlobalGroupDecls.put(declKey, decl);
/*      */   }
/*      */ 
/*      */   void addGlobalNotationDecl(XSNotationDecl decl)
/*      */   {
/* 1564 */     String namespace = decl.getNamespace();
/* 1565 */     String declKey = namespace + "," + decl.getName();
/*      */ 
/* 1568 */     if (this.fGlobalNotationDecls.get(declKey) == null)
/* 1569 */       this.fGlobalNotationDecls.put(declKey, decl);
/*      */   }
/*      */ 
/*      */   void addGlobalTypeDecl(XSTypeDefinition decl)
/*      */   {
/* 1575 */     String namespace = decl.getNamespace();
/* 1576 */     String declKey = namespace + "," + decl.getName();
/*      */ 
/* 1579 */     if (this.fGlobalTypeDecls.get(declKey) == null)
/* 1580 */       this.fGlobalTypeDecls.put(declKey, decl);
/*      */   }
/*      */ 
/*      */   void addIDConstraintDecl(IdentityConstraint decl)
/*      */   {
/* 1586 */     String namespace = decl.getNamespace();
/* 1587 */     String declKey = namespace + "," + decl.getIdentityConstraintName();
/*      */ 
/* 1590 */     if (this.fGlobalIDConstraintDecls.get(declKey) == null)
/* 1591 */       this.fGlobalIDConstraintDecls.put(declKey, decl);
/*      */   }
/*      */ 
/*      */   private XSAttributeDecl getGlobalAttributeDecl(String declKey)
/*      */   {
/* 1596 */     return (XSAttributeDecl)this.fGlobalAttrDecls.get(declKey);
/*      */   }
/*      */ 
/*      */   private XSAttributeGroupDecl getGlobalAttributeGroupDecl(String declKey) {
/* 1600 */     return (XSAttributeGroupDecl)this.fGlobalAttrGrpDecls.get(declKey);
/*      */   }
/*      */ 
/*      */   private XSElementDecl getGlobalElementDecl(String declKey) {
/* 1604 */     return (XSElementDecl)this.fGlobalElemDecls.get(declKey);
/*      */   }
/*      */ 
/*      */   private XSGroupDecl getGlobalGroupDecl(String declKey) {
/* 1608 */     return (XSGroupDecl)this.fGlobalGroupDecls.get(declKey);
/*      */   }
/*      */ 
/*      */   private XSNotationDecl getGlobalNotationDecl(String declKey) {
/* 1612 */     return (XSNotationDecl)this.fGlobalNotationDecls.get(declKey);
/*      */   }
/*      */ 
/*      */   private XSTypeDefinition getGlobalTypeDecl(String declKey) {
/* 1616 */     return (XSTypeDefinition)this.fGlobalTypeDecls.get(declKey);
/*      */   }
/*      */ 
/*      */   private IdentityConstraint getIDConstraintDecl(String declKey) {
/* 1620 */     return (IdentityConstraint)this.fGlobalIDConstraintDecls.get(declKey);
/*      */   }
/*      */ 
/*      */   protected Object getGlobalDecl(XSDocumentInfo currSchema, int declType, QName declToTraverse, Element elmNode)
/*      */   {
/* 1654 */     if ((declToTraverse.uri != null) && (declToTraverse.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA))
/*      */     {
/* 1656 */       if (declType == 7) {
/* 1657 */         Object retObj = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(declToTraverse.localpart);
/* 1658 */         if (retObj != null) {
/* 1659 */           return retObj;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1664 */     if (!currSchema.isAllowedNS(declToTraverse.uri))
/*      */     {
/* 1666 */       if (currSchema.needReportTNSError(declToTraverse.uri)) {
/* 1667 */         String code = declToTraverse.uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
/* 1668 */         reportSchemaError(code, new Object[] { this.fDoc2SystemId.get(currSchema.fSchemaElement), declToTraverse.uri, declToTraverse.rawname }, elmNode);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1675 */     SchemaGrammar sGrammar = this.fGrammarBucket.getGrammar(declToTraverse.uri);
/* 1676 */     if (sGrammar == null) {
/* 1677 */       if (needReportTNSError(declToTraverse.uri))
/* 1678 */         reportSchemaError("src-resolve", new Object[] { declToTraverse.rawname, COMP_TYPE[declType] }, elmNode);
/* 1679 */       return null;
/*      */     }
/*      */ 
/* 1683 */     Object retObj = getGlobalDeclFromGrammar(sGrammar, declType, declToTraverse.localpart);
/* 1684 */     String declKey = declToTraverse.uri + "," + declToTraverse.localpart;
/*      */ 
/* 1688 */     if (!this.fTolerateDuplicates) {
/* 1689 */       if (retObj != null)
/* 1690 */         return retObj;
/*      */     }
/*      */     else
/*      */     {
/* 1694 */       Object retObj2 = getGlobalDecl(declKey, declType);
/* 1695 */       if (retObj2 != null) {
/* 1696 */         return retObj2;
/*      */       }
/*      */     }
/*      */ 
/* 1700 */     XSDocumentInfo schemaWithDecl = null;
/* 1701 */     Element decl = null;
/* 1702 */     XSDocumentInfo declDoc = null;
/*      */ 
/* 1705 */     switch (declType) {
/*      */     case 1:
/* 1707 */       decl = getElementFromMap(this.fUnparsedAttributeRegistry, declKey);
/* 1708 */       declDoc = getDocInfoFromMap(this.fUnparsedAttributeRegistrySub, declKey);
/* 1709 */       break;
/*      */     case 2:
/* 1711 */       decl = getElementFromMap(this.fUnparsedAttributeGroupRegistry, declKey);
/* 1712 */       declDoc = getDocInfoFromMap(this.fUnparsedAttributeGroupRegistrySub, declKey);
/* 1713 */       break;
/*      */     case 3:
/* 1715 */       decl = getElementFromMap(this.fUnparsedElementRegistry, declKey);
/* 1716 */       declDoc = getDocInfoFromMap(this.fUnparsedElementRegistrySub, declKey);
/* 1717 */       break;
/*      */     case 4:
/* 1719 */       decl = getElementFromMap(this.fUnparsedGroupRegistry, declKey);
/* 1720 */       declDoc = getDocInfoFromMap(this.fUnparsedGroupRegistrySub, declKey);
/* 1721 */       break;
/*      */     case 5:
/* 1723 */       decl = getElementFromMap(this.fUnparsedIdentityConstraintRegistry, declKey);
/* 1724 */       declDoc = getDocInfoFromMap(this.fUnparsedIdentityConstraintRegistrySub, declKey);
/* 1725 */       break;
/*      */     case 6:
/* 1727 */       decl = getElementFromMap(this.fUnparsedNotationRegistry, declKey);
/* 1728 */       declDoc = getDocInfoFromMap(this.fUnparsedNotationRegistrySub, declKey);
/* 1729 */       break;
/*      */     case 7:
/* 1731 */       decl = getElementFromMap(this.fUnparsedTypeRegistry, declKey);
/* 1732 */       declDoc = getDocInfoFromMap(this.fUnparsedTypeRegistrySub, declKey);
/* 1733 */       break;
/*      */     default:
/* 1735 */       reportSchemaError("Internal-Error", new Object[] { "XSDHandler asked to locate component of type " + declType + "; it does not recognize this type!" }, elmNode);
/*      */     }
/*      */ 
/* 1739 */     if (decl == null) {
/* 1740 */       if (retObj == null) {
/* 1741 */         reportSchemaError("src-resolve", new Object[] { declToTraverse.rawname, COMP_TYPE[declType] }, elmNode);
/*      */       }
/* 1743 */       return retObj;
/*      */     }
/*      */ 
/* 1749 */     schemaWithDecl = findXSDocumentForDecl(currSchema, decl, declDoc);
/* 1750 */     if (schemaWithDecl == null)
/*      */     {
/* 1752 */       if (retObj == null) {
/* 1753 */         String code = declToTraverse.uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
/* 1754 */         reportSchemaError(code, new Object[] { this.fDoc2SystemId.get(currSchema.fSchemaElement), declToTraverse.uri, declToTraverse.rawname }, elmNode);
/*      */       }
/* 1756 */       return retObj;
/*      */     }
/*      */ 
/* 1762 */     if (DOMUtil.isHidden(decl, this.fHiddenNodes)) {
/* 1763 */       if (retObj == null) {
/* 1764 */         String code = CIRCULAR_CODES[declType];
/* 1765 */         if ((declType == 7) && 
/* 1766 */           (SchemaSymbols.ELT_COMPLEXTYPE.equals(DOMUtil.getLocalName(decl)))) {
/* 1767 */           code = "ct-props-correct.3";
/*      */         }
/*      */ 
/* 1771 */         reportSchemaError(code, new Object[] { declToTraverse.prefix + ":" + declToTraverse.localpart }, elmNode);
/*      */       }
/* 1773 */       return retObj;
/*      */     }
/*      */ 
/* 1776 */     return traverseGlobalDecl(declType, decl, schemaWithDecl, sGrammar);
/*      */   }
/*      */ 
/*      */   protected Object getGlobalDecl(String declKey, int declType)
/*      */   {
/* 1782 */     Object retObj = null;
/*      */ 
/* 1784 */     switch (declType) {
/*      */     case 1:
/* 1786 */       retObj = getGlobalAttributeDecl(declKey);
/* 1787 */       break;
/*      */     case 2:
/* 1789 */       retObj = getGlobalAttributeGroupDecl(declKey);
/* 1790 */       break;
/*      */     case 3:
/* 1792 */       retObj = getGlobalElementDecl(declKey);
/* 1793 */       break;
/*      */     case 4:
/* 1795 */       retObj = getGlobalGroupDecl(declKey);
/* 1796 */       break;
/*      */     case 5:
/* 1798 */       retObj = getIDConstraintDecl(declKey);
/* 1799 */       break;
/*      */     case 6:
/* 1801 */       retObj = getGlobalNotationDecl(declKey);
/* 1802 */       break;
/*      */     case 7:
/* 1804 */       retObj = getGlobalTypeDecl(declKey);
/*      */     }
/*      */ 
/* 1808 */     return retObj;
/*      */   }
/*      */ 
/*      */   protected Object getGlobalDeclFromGrammar(SchemaGrammar sGrammar, int declType, String localpart) {
/* 1812 */     Object retObj = null;
/*      */ 
/* 1814 */     switch (declType) {
/*      */     case 1:
/* 1816 */       retObj = sGrammar.getGlobalAttributeDecl(localpart);
/* 1817 */       break;
/*      */     case 2:
/* 1819 */       retObj = sGrammar.getGlobalAttributeGroupDecl(localpart);
/* 1820 */       break;
/*      */     case 3:
/* 1822 */       retObj = sGrammar.getGlobalElementDecl(localpart);
/* 1823 */       break;
/*      */     case 4:
/* 1825 */       retObj = sGrammar.getGlobalGroupDecl(localpart);
/* 1826 */       break;
/*      */     case 5:
/* 1828 */       retObj = sGrammar.getIDConstraintDecl(localpart);
/* 1829 */       break;
/*      */     case 6:
/* 1831 */       retObj = sGrammar.getGlobalNotationDecl(localpart);
/* 1832 */       break;
/*      */     case 7:
/* 1834 */       retObj = sGrammar.getGlobalTypeDecl(localpart);
/*      */     }
/*      */ 
/* 1838 */     return retObj;
/*      */   }
/*      */ 
/*      */   protected Object getGlobalDeclFromGrammar(SchemaGrammar sGrammar, int declType, String localpart, String schemaLoc) {
/* 1842 */     Object retObj = null;
/*      */ 
/* 1844 */     switch (declType) {
/*      */     case 1:
/* 1846 */       retObj = sGrammar.getGlobalAttributeDecl(localpart, schemaLoc);
/* 1847 */       break;
/*      */     case 2:
/* 1849 */       retObj = sGrammar.getGlobalAttributeGroupDecl(localpart, schemaLoc);
/* 1850 */       break;
/*      */     case 3:
/* 1852 */       retObj = sGrammar.getGlobalElementDecl(localpart, schemaLoc);
/* 1853 */       break;
/*      */     case 4:
/* 1855 */       retObj = sGrammar.getGlobalGroupDecl(localpart, schemaLoc);
/* 1856 */       break;
/*      */     case 5:
/* 1858 */       retObj = sGrammar.getIDConstraintDecl(localpart, schemaLoc);
/* 1859 */       break;
/*      */     case 6:
/* 1861 */       retObj = sGrammar.getGlobalNotationDecl(localpart, schemaLoc);
/* 1862 */       break;
/*      */     case 7:
/* 1864 */       retObj = sGrammar.getGlobalTypeDecl(localpart, schemaLoc);
/*      */     }
/*      */ 
/* 1868 */     return retObj;
/*      */   }
/*      */ 
/*      */   protected Object traverseGlobalDecl(int declType, Element decl, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
/* 1872 */     Object retObj = null;
/*      */ 
/* 1874 */     DOMUtil.setHidden(decl, this.fHiddenNodes);
/* 1875 */     SchemaNamespaceSupport nsSupport = null;
/*      */ 
/* 1877 */     Element parent = DOMUtil.getParent(decl);
/* 1878 */     if (DOMUtil.getLocalName(parent).equals(SchemaSymbols.ELT_REDEFINE)) {
/* 1879 */       nsSupport = this.fRedefine2NSSupport != null ? (SchemaNamespaceSupport)this.fRedefine2NSSupport.get(parent) : null;
/*      */     }
/*      */ 
/* 1882 */     schemaDoc.backupNSSupport(nsSupport);
/*      */ 
/* 1885 */     switch (declType) {
/*      */     case 7:
/* 1887 */       if (DOMUtil.getLocalName(decl).equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
/* 1888 */         retObj = this.fComplexTypeTraverser.traverseGlobal(decl, schemaDoc, grammar);
/*      */       }
/*      */       else {
/* 1891 */         retObj = this.fSimpleTypeTraverser.traverseGlobal(decl, schemaDoc, grammar);
/*      */       }
/* 1893 */       break;
/*      */     case 1:
/* 1895 */       retObj = this.fAttributeTraverser.traverseGlobal(decl, schemaDoc, grammar);
/* 1896 */       break;
/*      */     case 3:
/* 1898 */       retObj = this.fElementTraverser.traverseGlobal(decl, schemaDoc, grammar);
/* 1899 */       break;
/*      */     case 2:
/* 1901 */       retObj = this.fAttributeGroupTraverser.traverseGlobal(decl, schemaDoc, grammar);
/* 1902 */       break;
/*      */     case 4:
/* 1904 */       retObj = this.fGroupTraverser.traverseGlobal(decl, schemaDoc, grammar);
/* 1905 */       break;
/*      */     case 6:
/* 1907 */       retObj = this.fNotationTraverser.traverse(decl, schemaDoc, grammar);
/* 1908 */       break;
/*      */     case 5:
/*      */     }
/*      */ 
/* 1917 */     schemaDoc.restoreNSSupport();
/*      */ 
/* 1919 */     return retObj;
/*      */   }
/*      */ 
/*      */   public String schemaDocument2SystemId(XSDocumentInfo schemaDoc) {
/* 1923 */     return (String)this.fDoc2SystemId.get(schemaDoc.fSchemaElement);
/*      */   }
/*      */ 
/*      */   Object getGrpOrAttrGrpRedefinedByRestriction(int type, QName name, XSDocumentInfo currSchema, Element elmNode)
/*      */   {
/* 1936 */     String realName = "," + name.localpart;
/*      */ 
/* 1938 */     String nameToFind = null;
/* 1939 */     switch (type) {
/*      */     case 2:
/* 1941 */       nameToFind = (String)this.fRedefinedRestrictedAttributeGroupRegistry.get(realName);
/* 1942 */       break;
/*      */     case 4:
/* 1944 */       nameToFind = (String)this.fRedefinedRestrictedGroupRegistry.get(realName);
/* 1945 */       break;
/*      */     default:
/* 1947 */       return null;
/*      */     }
/* 1949 */     if (nameToFind == null) return null;
/* 1950 */     int commaPos = nameToFind.indexOf(",");
/* 1951 */     QName qNameToFind = new QName(XMLSymbols.EMPTY_STRING, nameToFind.substring(commaPos + 1), nameToFind.substring(commaPos), commaPos == 0 ? null : nameToFind.substring(0, commaPos));
/*      */ 
/* 1953 */     Object retObj = getGlobalDecl(currSchema, type, qNameToFind, elmNode);
/* 1954 */     if (retObj == null) {
/* 1955 */       switch (type) {
/*      */       case 2:
/* 1957 */         reportSchemaError("src-redefine.7.2.1", new Object[] { name.localpart }, elmNode);
/* 1958 */         break;
/*      */       case 4:
/* 1960 */         reportSchemaError("src-redefine.6.2.1", new Object[] { name.localpart }, elmNode);
/*      */       }
/*      */ 
/* 1963 */       return null;
/*      */     }
/* 1965 */     return retObj;
/*      */   }
/*      */ 
/*      */   protected void resolveKeyRefs()
/*      */   {
/* 1978 */     for (int i = 0; i < this.fKeyrefStackPos; i++) {
/* 1979 */       XSDocumentInfo keyrefSchemaDoc = this.fKeyrefsMapXSDocumentInfo[i];
/* 1980 */       keyrefSchemaDoc.fNamespaceSupport.makeGlobal();
/* 1981 */       keyrefSchemaDoc.fNamespaceSupport.setEffectiveContext(this.fKeyrefNamespaceContext[i]);
/* 1982 */       SchemaGrammar keyrefGrammar = this.fGrammarBucket.getGrammar(keyrefSchemaDoc.fTargetNamespace);
/*      */ 
/* 1985 */       DOMUtil.setHidden(this.fKeyrefs[i], this.fHiddenNodes);
/* 1986 */       this.fKeyrefTraverser.traverse(this.fKeyrefs[i], this.fKeyrefElems[i], keyrefSchemaDoc, keyrefGrammar);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Map getIDRegistry()
/*      */   {
/* 1993 */     return this.fUnparsedIdentityConstraintRegistry;
/*      */   }
/*      */ 
/*      */   protected Map getIDRegistry_sub() {
/* 1997 */     return this.fUnparsedIdentityConstraintRegistrySub;
/*      */   }
/*      */ 
/*      */   protected void storeKeyRef(Element keyrefToStore, XSDocumentInfo schemaDoc, XSElementDecl currElemDecl)
/*      */   {
/* 2006 */     String keyrefName = DOMUtil.getAttrValue(keyrefToStore, SchemaSymbols.ATT_NAME);
/* 2007 */     if (keyrefName.length() != 0) {
/* 2008 */       String keyrefQName = schemaDoc.fTargetNamespace + "," + keyrefName;
/*      */ 
/* 2010 */       checkForDuplicateNames(keyrefQName, 5, this.fUnparsedIdentityConstraintRegistry, this.fUnparsedIdentityConstraintRegistrySub, keyrefToStore, schemaDoc);
/*      */     }
/*      */ 
/* 2015 */     if (this.fKeyrefStackPos == this.fKeyrefs.length) {
/* 2016 */       Element[] elemArray = new Element[this.fKeyrefStackPos + 2];
/* 2017 */       System.arraycopy(this.fKeyrefs, 0, elemArray, 0, this.fKeyrefStackPos);
/* 2018 */       this.fKeyrefs = elemArray;
/* 2019 */       XSElementDecl[] declArray = new XSElementDecl[this.fKeyrefStackPos + 2];
/* 2020 */       System.arraycopy(this.fKeyrefElems, 0, declArray, 0, this.fKeyrefStackPos);
/* 2021 */       this.fKeyrefElems = declArray;
/* 2022 */       String[][] stringArray = new String[this.fKeyrefStackPos + 2][];
/* 2023 */       System.arraycopy(this.fKeyrefNamespaceContext, 0, stringArray, 0, this.fKeyrefStackPos);
/* 2024 */       this.fKeyrefNamespaceContext = stringArray;
/*      */ 
/* 2026 */       XSDocumentInfo[] xsDocumentInfo = new XSDocumentInfo[this.fKeyrefStackPos + 2];
/* 2027 */       System.arraycopy(this.fKeyrefsMapXSDocumentInfo, 0, xsDocumentInfo, 0, this.fKeyrefStackPos);
/* 2028 */       this.fKeyrefsMapXSDocumentInfo = xsDocumentInfo;
/*      */     }
/*      */ 
/* 2031 */     this.fKeyrefs[this.fKeyrefStackPos] = keyrefToStore;
/* 2032 */     this.fKeyrefElems[this.fKeyrefStackPos] = currElemDecl;
/* 2033 */     this.fKeyrefNamespaceContext[this.fKeyrefStackPos] = schemaDoc.fNamespaceSupport.getEffectiveLocalContext();
/*      */ 
/* 2035 */     this.fKeyrefsMapXSDocumentInfo[(this.fKeyrefStackPos++)] = schemaDoc;
/*      */   }
/*      */ 
/*      */   private Element resolveSchema(XSDDescription desc, boolean mustResolve, Element referElement, boolean usePairs)
/*      */   {
/* 2049 */     XMLInputSource schemaSource = null;
/*      */     try {
/* 2051 */       Map pairs = usePairs ? this.fLocationPairs : EMPTY_TABLE;
/* 2052 */       schemaSource = XMLSchemaLoader.resolveDocument(desc, pairs, this.fEntityResolver);
/*      */     }
/*      */     catch (IOException ex) {
/* 2055 */       if (mustResolve) {
/* 2056 */         reportSchemaError("schema_reference.4", new Object[] { desc.getLocationHints()[0] }, referElement);
/*      */       }
/*      */       else
/*      */       {
/* 2061 */         reportSchemaWarning("schema_reference.4", new Object[] { desc.getLocationHints()[0] }, referElement);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2066 */     if ((schemaSource instanceof DOMInputSource)) {
/* 2067 */       return getSchemaDocument(desc.getTargetNamespace(), (DOMInputSource)schemaSource, mustResolve, desc.getContextType(), referElement);
/*      */     }
/* 2069 */     if ((schemaSource instanceof SAXInputSource)) {
/* 2070 */       return getSchemaDocument(desc.getTargetNamespace(), (SAXInputSource)schemaSource, mustResolve, desc.getContextType(), referElement);
/*      */     }
/* 2072 */     if ((schemaSource instanceof StAXInputSource)) {
/* 2073 */       return getSchemaDocument(desc.getTargetNamespace(), (StAXInputSource)schemaSource, mustResolve, desc.getContextType(), referElement);
/*      */     }
/* 2075 */     if ((schemaSource instanceof XSInputSource)) {
/* 2076 */       return getSchemaDocument((XSInputSource)schemaSource, desc);
/*      */     }
/* 2078 */     return getSchemaDocument(desc.getTargetNamespace(), schemaSource, mustResolve, desc.getContextType(), referElement);
/*      */   }
/*      */ 
/*      */   private Element resolveSchema(XMLInputSource schemaSource, XSDDescription desc, boolean mustResolve, Element referElement)
/*      */   {
/* 2084 */     if ((schemaSource instanceof DOMInputSource)) {
/* 2085 */       return getSchemaDocument(desc.getTargetNamespace(), (DOMInputSource)schemaSource, mustResolve, desc.getContextType(), referElement);
/*      */     }
/* 2087 */     if ((schemaSource instanceof SAXInputSource)) {
/* 2088 */       return getSchemaDocument(desc.getTargetNamespace(), (SAXInputSource)schemaSource, mustResolve, desc.getContextType(), referElement);
/*      */     }
/* 2090 */     if ((schemaSource instanceof StAXInputSource)) {
/* 2091 */       return getSchemaDocument(desc.getTargetNamespace(), (StAXInputSource)schemaSource, mustResolve, desc.getContextType(), referElement);
/*      */     }
/* 2093 */     if ((schemaSource instanceof XSInputSource)) {
/* 2094 */       return getSchemaDocument((XSInputSource)schemaSource, desc);
/*      */     }
/* 2096 */     return getSchemaDocument(desc.getTargetNamespace(), schemaSource, mustResolve, desc.getContextType(), referElement);
/*      */   }
/*      */ 
/*      */   private XMLInputSource resolveSchemaSource(XSDDescription desc, boolean mustResolve, Element referElement, boolean usePairs)
/*      */   {
/* 2102 */     XMLInputSource schemaSource = null;
/*      */     try {
/* 2104 */       Map pairs = usePairs ? this.fLocationPairs : EMPTY_TABLE;
/* 2105 */       schemaSource = XMLSchemaLoader.resolveDocument(desc, pairs, this.fEntityResolver);
/*      */     }
/*      */     catch (IOException ex) {
/* 2108 */       if (mustResolve) {
/* 2109 */         reportSchemaError("schema_reference.4", new Object[] { desc.getLocationHints()[0] }, referElement);
/*      */       }
/*      */       else
/*      */       {
/* 2114 */         reportSchemaWarning("schema_reference.4", new Object[] { desc.getLocationHints()[0] }, referElement);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2120 */     return schemaSource;
/*      */   }
/*      */ 
/*      */   private Element getSchemaDocument(String schemaNamespace, XMLInputSource schemaSource, boolean mustResolve, short referType, Element referElement)
/*      */   {
/* 2135 */     boolean hasInput = true;
/* 2136 */     IOException exception = null;
/*      */ 
/* 2138 */     Element schemaElement = null;
/*      */     try
/*      */     {
/* 2146 */       if ((schemaSource != null) && ((schemaSource.getSystemId() != null) || (schemaSource.getByteStream() != null) || (schemaSource.getCharacterStream() != null)))
/*      */       {
/* 2155 */         XSDKey key = null;
/* 2156 */         String schemaId = null;
/* 2157 */         if (referType != 3) {
/* 2158 */           schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
/* 2159 */           key = new XSDKey(schemaId, referType, schemaNamespace);
/* 2160 */           if ((schemaElement = (Element)this.fTraversed.get(key)) != null) {
/* 2161 */             this.fLastSchemaWasDuplicate = true;
/* 2162 */             return schemaElement;
/*      */           }
/* 2164 */           if ((referType == 2) || (referType == 0) || (referType == 1))
/*      */           {
/* 2166 */             String accessError = SecuritySupport.checkAccess(schemaId, this.fAccessExternalSchema, "all");
/* 2167 */             if (accessError != null) {
/* 2168 */               reportSchemaFatalError("schema_reference.access", new Object[] { SecuritySupport.sanitizePath(schemaId), accessError }, referElement);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2175 */         this.fSchemaParser.parse(schemaSource);
/* 2176 */         Document schemaDocument = this.fSchemaParser.getDocument();
/* 2177 */         schemaElement = schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null;
/* 2178 */         return getSchemaDocument0(key, schemaId, schemaElement);
/*      */       }
/*      */ 
/* 2181 */       hasInput = false;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/* 2185 */       exception = ex;
/*      */     }
/* 2187 */     return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
/*      */   }
/*      */ 
/*      */   private Element getSchemaDocument(String schemaNamespace, SAXInputSource schemaSource, boolean mustResolve, short referType, Element referElement)
/*      */   {
/* 2201 */     XMLReader parser = schemaSource.getXMLReader();
/* 2202 */     InputSource inputSource = schemaSource.getInputSource();
/* 2203 */     boolean hasInput = true;
/* 2204 */     IOException exception = null;
/* 2205 */     Element schemaElement = null;
/*      */     try {
/* 2207 */       if ((inputSource != null) && ((inputSource.getSystemId() != null) || (inputSource.getByteStream() != null) || (inputSource.getCharacterStream() != null)))
/*      */       {
/* 2214 */         XSDKey key = null;
/* 2215 */         String schemaId = null;
/* 2216 */         if (referType != 3) {
/* 2217 */           schemaId = XMLEntityManager.expandSystemId(inputSource.getSystemId(), schemaSource.getBaseSystemId(), false);
/* 2218 */           key = new XSDKey(schemaId, referType, schemaNamespace);
/* 2219 */           if ((schemaElement = (Element)this.fTraversed.get(key)) != null) {
/* 2220 */             this.fLastSchemaWasDuplicate = true;
/* 2221 */             return schemaElement;
/*      */           }
/*      */         }
/*      */ 
/* 2225 */         boolean namespacePrefixes = false;
/* 2226 */         if (parser != null) {
/*      */           try {
/* 2228 */             namespacePrefixes = parser.getFeature("http://xml.org/sax/features/namespace-prefixes");
/*      */           } catch (SAXException se) {
/*      */           }
/*      */         }
/*      */         else {
/*      */           try {
/* 2234 */             parser = XMLReaderFactory.createXMLReader();
/*      */           }
/*      */           catch (SAXException se)
/*      */           {
/* 2239 */             parser = new SAXParser();
/*      */           }
/*      */           try {
/* 2242 */             parser.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
/* 2243 */             namespacePrefixes = true;
/*      */ 
/* 2245 */             if ((parser instanceof SAXParser)) {
/* 2246 */               Object securityManager = this.fSchemaParser.getProperty("http://apache.org/xml/properties/security-manager");
/* 2247 */               if (securityManager != null)
/* 2248 */                 parser.setProperty("http://apache.org/xml/properties/security-manager", securityManager);
/*      */             }
/*      */           }
/*      */           catch (SAXException se)
/*      */           {
/*      */           }
/*      */           try {
/* 2255 */             parser.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this.fAccessExternalDTD);
/*      */           } catch (SAXNotRecognizedException exc) {
/* 2257 */             System.err.println("Warning: " + parser.getClass().getName() + ": " + exc.getMessage());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2263 */         boolean stringsInternalized = false;
/*      */         try {
/* 2265 */           stringsInternalized = parser.getFeature("http://xml.org/sax/features/string-interning");
/*      */         }
/*      */         catch (SAXException exc)
/*      */         {
/*      */         }
/*      */ 
/* 2271 */         if (this.fXSContentHandler == null) {
/* 2272 */           this.fXSContentHandler = new SchemaContentHandler();
/*      */         }
/* 2274 */         this.fXSContentHandler.reset(this.fSchemaParser, this.fSymbolTable, namespacePrefixes, stringsInternalized);
/*      */ 
/* 2276 */         parser.setContentHandler(this.fXSContentHandler);
/* 2277 */         parser.setErrorHandler(this.fErrorReporter.getSAXErrorHandler());
/*      */ 
/* 2279 */         parser.parse(inputSource);
/*      */         try
/*      */         {
/* 2282 */           parser.setContentHandler(null);
/* 2283 */           parser.setErrorHandler(null);
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*      */         }
/*      */ 
/* 2290 */         Document schemaDocument = this.fXSContentHandler.getDocument();
/* 2291 */         schemaElement = schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null;
/* 2292 */         return getSchemaDocument0(key, schemaId, schemaElement);
/*      */       }
/*      */ 
/* 2295 */       hasInput = false;
/*      */     }
/*      */     catch (SAXParseException spe)
/*      */     {
/* 2299 */       throw SAX2XNIUtil.createXMLParseException0(spe);
/*      */     }
/*      */     catch (SAXException se) {
/* 2302 */       throw SAX2XNIUtil.createXNIException0(se);
/*      */     }
/*      */     catch (IOException ioe) {
/* 2305 */       exception = ioe;
/*      */     }
/* 2307 */     return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
/*      */   }
/*      */ 
/*      */   private Element getSchemaDocument(String schemaNamespace, DOMInputSource schemaSource, boolean mustResolve, short referType, Element referElement)
/*      */   {
/* 2321 */     boolean hasInput = true;
/* 2322 */     IOException exception = null;
/* 2323 */     Element schemaElement = null;
/* 2324 */     Element schemaRootElement = null;
/*      */ 
/* 2326 */     Node node = schemaSource.getNode();
/* 2327 */     short nodeType = -1;
/* 2328 */     if (node != null) {
/* 2329 */       nodeType = node.getNodeType();
/* 2330 */       if (nodeType == 9) {
/* 2331 */         schemaRootElement = DOMUtil.getRoot((Document)node);
/*      */       }
/* 2333 */       else if (nodeType == 1) {
/* 2334 */         schemaRootElement = (Element)node;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 2339 */       if (schemaRootElement != null)
/*      */       {
/* 2342 */         XSDKey key = null;
/* 2343 */         String schemaId = null;
/* 2344 */         if (referType != 3) {
/* 2345 */           schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
/* 2346 */           boolean isDocument = nodeType == 9;
/* 2347 */           if (!isDocument) {
/* 2348 */             Node parent = schemaRootElement.getParentNode();
/* 2349 */             if (parent != null) {
/* 2350 */               isDocument = parent.getNodeType() == 9;
/*      */             }
/*      */           }
/* 2353 */           if (isDocument) {
/* 2354 */             key = new XSDKey(schemaId, referType, schemaNamespace);
/* 2355 */             if ((schemaElement = (Element)this.fTraversed.get(key)) != null) {
/* 2356 */               this.fLastSchemaWasDuplicate = true;
/* 2357 */               return schemaElement;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 2362 */         schemaElement = schemaRootElement;
/* 2363 */         return getSchemaDocument0(key, schemaId, schemaElement);
/*      */       }
/*      */ 
/* 2366 */       hasInput = false;
/*      */     }
/*      */     catch (IOException ioe)
/*      */     {
/* 2370 */       exception = ioe;
/*      */     }
/* 2372 */     return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
/*      */   }
/*      */ 
/*      */   private Element getSchemaDocument(String schemaNamespace, StAXInputSource schemaSource, boolean mustResolve, short referType, Element referElement)
/*      */   {
/* 2386 */     IOException exception = null;
/* 2387 */     Element schemaElement = null;
/*      */     try {
/* 2389 */       boolean consumeRemainingContent = schemaSource.shouldConsumeRemainingContent();
/* 2390 */       XMLStreamReader streamReader = schemaSource.getXMLStreamReader();
/* 2391 */       XMLEventReader eventReader = schemaSource.getXMLEventReader();
/*      */ 
/* 2395 */       XSDKey key = null;
/* 2396 */       String schemaId = null;
/* 2397 */       if (referType != 3) {
/* 2398 */         schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
/* 2399 */         boolean isDocument = consumeRemainingContent;
/* 2400 */         if (!isDocument) {
/* 2401 */           if (streamReader != null) {
/* 2402 */             isDocument = streamReader.getEventType() == 7;
/*      */           }
/*      */           else {
/* 2405 */             isDocument = eventReader.peek().isStartDocument();
/*      */           }
/*      */         }
/* 2408 */         if (isDocument) {
/* 2409 */           key = new XSDKey(schemaId, referType, schemaNamespace);
/* 2410 */           if ((schemaElement = (Element)this.fTraversed.get(key)) != null) {
/* 2411 */             this.fLastSchemaWasDuplicate = true;
/* 2412 */             return schemaElement;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2417 */       if (this.fStAXSchemaParser == null) {
/* 2418 */         this.fStAXSchemaParser = new StAXSchemaParser();
/*      */       }
/* 2420 */       this.fStAXSchemaParser.reset(this.fSchemaParser, this.fSymbolTable);
/*      */ 
/* 2422 */       if (streamReader != null) {
/* 2423 */         this.fStAXSchemaParser.parse(streamReader);
/* 2424 */         if (consumeRemainingContent) {
/* 2425 */           while (streamReader.hasNext())
/* 2426 */             streamReader.next();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2431 */         this.fStAXSchemaParser.parse(eventReader);
/* 2432 */         if (consumeRemainingContent) {
/* 2433 */           while (eventReader.hasNext()) {
/* 2434 */             eventReader.nextEvent();
/*      */           }
/*      */         }
/*      */       }
/* 2438 */       Document schemaDocument = this.fStAXSchemaParser.getDocument();
/* 2439 */       schemaElement = schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null;
/* 2440 */       return getSchemaDocument0(key, schemaId, schemaElement);
/*      */     }
/*      */     catch (XMLStreamException e) {
/* 2443 */       StAXLocationWrapper slw = new StAXLocationWrapper();
/* 2444 */       slw.setLocation(e.getLocation());
/* 2445 */       throw new XMLParseException(slw, e.getMessage(), e);
/*      */     }
/*      */     catch (IOException e) {
/* 2448 */       exception = e;
/*      */     }
/* 2450 */     return getSchemaDocument1(mustResolve, true, schemaSource, referElement, exception);
/*      */   }
/*      */ 
/*      */   private Element getSchemaDocument0(XSDKey key, String schemaId, Element schemaElement)
/*      */   {
/* 2460 */     if (key != null) {
/* 2461 */       this.fTraversed.put(key, schemaElement);
/*      */     }
/* 2463 */     if (schemaId != null) {
/* 2464 */       this.fDoc2SystemId.put(schemaElement, schemaId);
/*      */     }
/* 2466 */     this.fLastSchemaWasDuplicate = false;
/* 2467 */     return schemaElement;
/*      */   }
/*      */ 
/*      */   private Element getSchemaDocument1(boolean mustResolve, boolean hasInput, XMLInputSource schemaSource, Element referElement, IOException ioe)
/*      */   {
/* 2477 */     if (mustResolve) {
/* 2478 */       if (hasInput) {
/* 2479 */         reportSchemaError("schema_reference.4", new Object[] { schemaSource.getSystemId() }, referElement, ioe);
/*      */       }
/*      */       else
/*      */       {
/* 2484 */         reportSchemaError("schema_reference.4", new Object[] { schemaSource == null ? "" : schemaSource.getSystemId() }, referElement, ioe);
/*      */       }
/*      */ 
/*      */     }
/* 2489 */     else if (hasInput) {
/* 2490 */       reportSchemaWarning("schema_reference.4", new Object[] { schemaSource.getSystemId() }, referElement, ioe);
/*      */     }
/*      */ 
/* 2495 */     this.fLastSchemaWasDuplicate = false;
/* 2496 */     return null;
/*      */   }
/*      */ 
/*      */   private Element getSchemaDocument(XSInputSource schemaSource, XSDDescription desc)
/*      */   {
/* 2510 */     SchemaGrammar[] grammars = schemaSource.getGrammars();
/* 2511 */     short referType = desc.getContextType();
/*      */ 
/* 2513 */     if ((grammars != null) && (grammars.length > 0)) {
/* 2514 */       Vector expandedGrammars = expandGrammars(grammars);
/*      */ 
/* 2518 */       if ((this.fNamespaceGrowth) || (!existingGrammars(expandedGrammars))) {
/* 2519 */         addGrammars(expandedGrammars);
/* 2520 */         if (referType == 3)
/* 2521 */           desc.setTargetNamespace(grammars[0].getTargetNamespace());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2526 */       XSObject[] components = schemaSource.getComponents();
/* 2527 */       if ((components != null) && (components.length > 0)) {
/* 2528 */         Map importDependencies = new HashMap();
/* 2529 */         Vector expandedComponents = expandComponents(components, importDependencies);
/* 2530 */         if ((this.fNamespaceGrowth) || (canAddComponents(expandedComponents))) {
/* 2531 */           addGlobalComponents(expandedComponents, importDependencies);
/* 2532 */           if (referType == 3) {
/* 2533 */             desc.setTargetNamespace(components[0].getNamespace());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2538 */     return null;
/*      */   }
/*      */ 
/*      */   private Vector expandGrammars(SchemaGrammar[] grammars) {
/* 2542 */     Vector currGrammars = new Vector();
/*      */ 
/* 2544 */     for (int i = 0; i < grammars.length; i++) {
/* 2545 */       if (!currGrammars.contains(grammars[i])) {
/* 2546 */         currGrammars.add(grammars[i]);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2553 */     for (int i = 0; i < currGrammars.size(); i++)
/*      */     {
/* 2555 */       SchemaGrammar sg1 = (SchemaGrammar)currGrammars.elementAt(i);
/*      */ 
/* 2557 */       Vector gs = sg1.getImportedGrammars();
/*      */ 
/* 2560 */       if (gs != null)
/*      */       {
/* 2564 */         for (int j = gs.size() - 1; j >= 0; j--) {
/* 2565 */           SchemaGrammar sg2 = (SchemaGrammar)gs.elementAt(j);
/* 2566 */           if (!currGrammars.contains(sg2)) {
/* 2567 */             currGrammars.addElement(sg2);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2572 */     return currGrammars;
/*      */   }
/*      */ 
/*      */   private boolean existingGrammars(Vector grammars) {
/* 2576 */     int length = grammars.size();
/* 2577 */     XSDDescription desc = new XSDDescription();
/*      */ 
/* 2579 */     for (int i = 0; i < length; i++) {
/* 2580 */       SchemaGrammar sg1 = (SchemaGrammar)grammars.elementAt(i);
/* 2581 */       desc.setNamespace(sg1.getTargetNamespace());
/*      */ 
/* 2583 */       SchemaGrammar sg2 = findGrammar(desc, false);
/* 2584 */       if (sg2 != null) {
/* 2585 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 2589 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean canAddComponents(Vector components) {
/* 2593 */     int size = components.size();
/* 2594 */     XSDDescription desc = new XSDDescription();
/* 2595 */     for (int i = 0; i < size; i++) {
/* 2596 */       XSObject component = (XSObject)components.elementAt(i);
/* 2597 */       if (!canAddComponent(component, desc)) {
/* 2598 */         return false;
/*      */       }
/*      */     }
/* 2601 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean canAddComponent(XSObject component, XSDDescription desc) {
/* 2605 */     desc.setNamespace(component.getNamespace());
/*      */ 
/* 2607 */     SchemaGrammar sg = findGrammar(desc, false);
/* 2608 */     if (sg == null) {
/* 2609 */       return true;
/*      */     }
/* 2611 */     if (sg.isImmutable()) {
/* 2612 */       return false;
/*      */     }
/*      */ 
/* 2615 */     short componentType = component.getType();
/* 2616 */     String name = component.getName();
/*      */ 
/* 2618 */     switch (componentType) {
/*      */     case 3:
/* 2620 */       if (sg.getGlobalTypeDecl(name) == component) {
/* 2621 */         return true;
/*      */       }
/*      */       break;
/*      */     case 1:
/* 2625 */       if (sg.getGlobalAttributeDecl(name) == component) {
/* 2626 */         return true;
/*      */       }
/*      */       break;
/*      */     case 5:
/* 2630 */       if (sg.getGlobalAttributeDecl(name) == component) {
/* 2631 */         return true;
/*      */       }
/*      */       break;
/*      */     case 2:
/* 2635 */       if (sg.getGlobalElementDecl(name) == component) {
/* 2636 */         return true;
/*      */       }
/*      */       break;
/*      */     case 6:
/* 2640 */       if (sg.getGlobalGroupDecl(name) == component) {
/* 2641 */         return true;
/*      */       }
/*      */       break;
/*      */     case 11:
/* 2645 */       if (sg.getGlobalNotationDecl(name) == component)
/* 2646 */         return true; break;
/*      */     case 4:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     default:
/* 2652 */       return true;
/*      */     }
/* 2654 */     return false;
/*      */   }
/*      */ 
/*      */   private void addGrammars(Vector grammars) {
/* 2658 */     int length = grammars.size();
/* 2659 */     XSDDescription desc = new XSDDescription();
/*      */ 
/* 2661 */     for (int i = 0; i < length; i++) {
/* 2662 */       SchemaGrammar sg1 = (SchemaGrammar)grammars.elementAt(i);
/* 2663 */       desc.setNamespace(sg1.getTargetNamespace());
/*      */ 
/* 2665 */       SchemaGrammar sg2 = findGrammar(desc, this.fNamespaceGrowth);
/* 2666 */       if (sg1 != sg2)
/* 2667 */         addGrammarComponents(sg1, sg2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addGrammarComponents(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar)
/*      */   {
/* 2673 */     if (dstGrammar == null) {
/* 2674 */       createGrammarFrom(srcGrammar);
/* 2675 */       return;
/*      */     }
/*      */ 
/* 2678 */     SchemaGrammar tmpGrammar = dstGrammar;
/* 2679 */     if (tmpGrammar.isImmutable()) {
/* 2680 */       tmpGrammar = createGrammarFrom(dstGrammar);
/*      */     }
/*      */ 
/* 2684 */     addNewGrammarLocations(srcGrammar, tmpGrammar);
/*      */ 
/* 2687 */     addNewImportedGrammars(srcGrammar, tmpGrammar);
/*      */ 
/* 2690 */     addNewGrammarComponents(srcGrammar, tmpGrammar);
/*      */   }
/*      */ 
/*      */   private SchemaGrammar createGrammarFrom(SchemaGrammar grammar) {
/* 2694 */     SchemaGrammar newGrammar = new SchemaGrammar(grammar);
/* 2695 */     this.fGrammarBucket.putGrammar(newGrammar);
/*      */ 
/* 2697 */     updateImportListWith(newGrammar);
/*      */ 
/* 2699 */     updateImportListFor(newGrammar);
/* 2700 */     return newGrammar;
/*      */   }
/*      */ 
/*      */   private void addNewGrammarLocations(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
/* 2704 */     StringList locations = srcGrammar.getDocumentLocations();
/* 2705 */     int locSize = locations.size();
/* 2706 */     StringList locations2 = dstGrammar.getDocumentLocations();
/*      */ 
/* 2708 */     for (int i = 0; i < locSize; i++) {
/* 2709 */       String loc = locations.item(i);
/* 2710 */       if (!locations2.contains(loc))
/* 2711 */         dstGrammar.addDocument(null, loc);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addNewImportedGrammars(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar)
/*      */   {
/* 2717 */     Vector igs1 = srcGrammar.getImportedGrammars();
/* 2718 */     if (igs1 != null) {
/* 2719 */       Vector igs2 = dstGrammar.getImportedGrammars();
/*      */ 
/* 2721 */       if (igs2 == null) {
/* 2722 */         igs2 = (Vector)igs1.clone();
/* 2723 */         dstGrammar.setImportedGrammars(igs2);
/*      */       }
/*      */       else {
/* 2726 */         updateImportList(igs1, igs2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateImportList(Vector importedSrc, Vector importedDst)
/*      */   {
/* 2733 */     int size = importedSrc.size();
/*      */ 
/* 2735 */     for (int i = 0; i < size; i++) {
/* 2736 */       SchemaGrammar sg = (SchemaGrammar)importedSrc.elementAt(i);
/* 2737 */       if (!containedImportedGrammar(importedDst, sg))
/* 2738 */         importedDst.add(sg);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addNewGrammarComponents(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar)
/*      */   {
/* 2744 */     dstGrammar.resetComponents();
/* 2745 */     addGlobalElementDecls(srcGrammar, dstGrammar);
/* 2746 */     addGlobalAttributeDecls(srcGrammar, dstGrammar);
/* 2747 */     addGlobalAttributeGroupDecls(srcGrammar, dstGrammar);
/* 2748 */     addGlobalGroupDecls(srcGrammar, dstGrammar);
/* 2749 */     addGlobalTypeDecls(srcGrammar, dstGrammar);
/* 2750 */     addGlobalNotationDecls(srcGrammar, dstGrammar);
/*      */   }
/*      */ 
/*      */   private void addGlobalElementDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
/* 2754 */     XSNamedMap components = srcGrammar.getComponents((short)2);
/* 2755 */     int len = components.getLength();
/*      */ 
/* 2759 */     for (int i = 0; i < len; i++) {
/* 2760 */       XSElementDecl srcDecl = (XSElementDecl)components.item(i);
/* 2761 */       XSElementDecl dstDecl = dstGrammar.getGlobalElementDecl(srcDecl.getName());
/* 2762 */       if (dstDecl == null) {
/* 2763 */         dstGrammar.addGlobalElementDecl(srcDecl);
/*      */       }
/* 2765 */       else if (dstDecl == srcDecl);
/*      */     }
/*      */ 
/* 2771 */     ObjectList componentsExt = srcGrammar.getComponentsExt((short)2);
/* 2772 */     len = componentsExt.getLength();
/*      */ 
/* 2774 */     for (int i = 0; i < len; i += 2) {
/* 2775 */       String key = (String)componentsExt.item(i);
/* 2776 */       int index = key.indexOf(',');
/* 2777 */       String location = key.substring(0, index);
/* 2778 */       String name = key.substring(index + 1, key.length());
/*      */ 
/* 2780 */       XSElementDecl srcDecl = (XSElementDecl)componentsExt.item(i + 1);
/* 2781 */       XSElementDecl dstDecl = dstGrammar.getGlobalElementDecl(name, location);
/* 2782 */       if (dstDecl == null) {
/* 2783 */         dstGrammar.addGlobalElementDecl(srcDecl, location);
/*      */       }
/* 2785 */       else if (dstDecl == srcDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addGlobalAttributeDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar)
/*      */   {
/* 2792 */     XSNamedMap components = srcGrammar.getComponents((short)1);
/* 2793 */     int len = components.getLength();
/*      */ 
/* 2797 */     for (int i = 0; i < len; i++) {
/* 2798 */       XSAttributeDecl srcDecl = (XSAttributeDecl)components.item(i);
/* 2799 */       XSAttributeDecl dstDecl = dstGrammar.getGlobalAttributeDecl(srcDecl.getName());
/* 2800 */       if (dstDecl == null) {
/* 2801 */         dstGrammar.addGlobalAttributeDecl(srcDecl);
/*      */       }
/* 2803 */       else if ((dstDecl != srcDecl) && (!this.fTolerateDuplicates)) {
/* 2804 */         reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2809 */     ObjectList componentsExt = srcGrammar.getComponentsExt((short)1);
/* 2810 */     len = componentsExt.getLength();
/*      */ 
/* 2812 */     for (int i = 0; i < len; i += 2) {
/* 2813 */       String key = (String)componentsExt.item(i);
/* 2814 */       int index = key.indexOf(',');
/* 2815 */       String location = key.substring(0, index);
/* 2816 */       String name = key.substring(index + 1, key.length());
/*      */ 
/* 2818 */       XSAttributeDecl srcDecl = (XSAttributeDecl)componentsExt.item(i + 1);
/* 2819 */       XSAttributeDecl dstDecl = dstGrammar.getGlobalAttributeDecl(name, location);
/* 2820 */       if (dstDecl == null) {
/* 2821 */         dstGrammar.addGlobalAttributeDecl(srcDecl, location);
/*      */       }
/* 2824 */       else if (dstDecl == srcDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addGlobalAttributeGroupDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar)
/*      */   {
/* 2830 */     XSNamedMap components = srcGrammar.getComponents((short)5);
/* 2831 */     int len = components.getLength();
/*      */ 
/* 2835 */     for (int i = 0; i < len; i++) {
/* 2836 */       XSAttributeGroupDecl srcDecl = (XSAttributeGroupDecl)components.item(i);
/* 2837 */       XSAttributeGroupDecl dstDecl = dstGrammar.getGlobalAttributeGroupDecl(srcDecl.getName());
/* 2838 */       if (dstDecl == null) {
/* 2839 */         dstGrammar.addGlobalAttributeGroupDecl(srcDecl);
/*      */       }
/* 2841 */       else if ((dstDecl != srcDecl) && (!this.fTolerateDuplicates)) {
/* 2842 */         reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2847 */     ObjectList componentsExt = srcGrammar.getComponentsExt((short)5);
/* 2848 */     len = componentsExt.getLength();
/*      */ 
/* 2850 */     for (int i = 0; i < len; i += 2) {
/* 2851 */       String key = (String)componentsExt.item(i);
/* 2852 */       int index = key.indexOf(',');
/* 2853 */       String location = key.substring(0, index);
/* 2854 */       String name = key.substring(index + 1, key.length());
/*      */ 
/* 2856 */       XSAttributeGroupDecl srcDecl = (XSAttributeGroupDecl)componentsExt.item(i + 1);
/* 2857 */       XSAttributeGroupDecl dstDecl = dstGrammar.getGlobalAttributeGroupDecl(name, location);
/* 2858 */       if (dstDecl == null) {
/* 2859 */         dstGrammar.addGlobalAttributeGroupDecl(srcDecl, location);
/*      */       }
/* 2862 */       else if (dstDecl == srcDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addGlobalNotationDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar)
/*      */   {
/* 2868 */     XSNamedMap components = srcGrammar.getComponents((short)11);
/* 2869 */     int len = components.getLength();
/*      */ 
/* 2873 */     for (int i = 0; i < len; i++) {
/* 2874 */       XSNotationDecl srcDecl = (XSNotationDecl)components.item(i);
/* 2875 */       XSNotationDecl dstDecl = dstGrammar.getGlobalNotationDecl(srcDecl.getName());
/* 2876 */       if (dstDecl == null) {
/* 2877 */         dstGrammar.addGlobalNotationDecl(srcDecl);
/*      */       }
/* 2879 */       else if ((dstDecl != srcDecl) && (!this.fTolerateDuplicates)) {
/* 2880 */         reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2885 */     ObjectList componentsExt = srcGrammar.getComponentsExt((short)11);
/* 2886 */     len = componentsExt.getLength();
/*      */ 
/* 2888 */     for (int i = 0; i < len; i += 2) {
/* 2889 */       String key = (String)componentsExt.item(i);
/* 2890 */       int index = key.indexOf(',');
/* 2891 */       String location = key.substring(0, index);
/* 2892 */       String name = key.substring(index + 1, key.length());
/*      */ 
/* 2894 */       XSNotationDecl srcDecl = (XSNotationDecl)componentsExt.item(i + 1);
/* 2895 */       XSNotationDecl dstDecl = dstGrammar.getGlobalNotationDecl(name, location);
/* 2896 */       if (dstDecl == null) {
/* 2897 */         dstGrammar.addGlobalNotationDecl(srcDecl, location);
/*      */       }
/* 2900 */       else if (dstDecl == srcDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addGlobalGroupDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar)
/*      */   {
/* 2906 */     XSNamedMap components = srcGrammar.getComponents((short)6);
/* 2907 */     int len = components.getLength();
/*      */ 
/* 2911 */     for (int i = 0; i < len; i++) {
/* 2912 */       XSGroupDecl srcDecl = (XSGroupDecl)components.item(i);
/* 2913 */       XSGroupDecl dstDecl = dstGrammar.getGlobalGroupDecl(srcDecl.getName());
/* 2914 */       if (dstDecl == null) {
/* 2915 */         dstGrammar.addGlobalGroupDecl(srcDecl);
/*      */       }
/* 2917 */       else if ((srcDecl != dstDecl) && (!this.fTolerateDuplicates)) {
/* 2918 */         reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2923 */     ObjectList componentsExt = srcGrammar.getComponentsExt((short)6);
/* 2924 */     len = componentsExt.getLength();
/*      */ 
/* 2926 */     for (int i = 0; i < len; i += 2) {
/* 2927 */       String key = (String)componentsExt.item(i);
/* 2928 */       int index = key.indexOf(',');
/* 2929 */       String location = key.substring(0, index);
/* 2930 */       String name = key.substring(index + 1, key.length());
/*      */ 
/* 2932 */       XSGroupDecl srcDecl = (XSGroupDecl)componentsExt.item(i + 1);
/* 2933 */       XSGroupDecl dstDecl = dstGrammar.getGlobalGroupDecl(name, location);
/* 2934 */       if (dstDecl == null) {
/* 2935 */         dstGrammar.addGlobalGroupDecl(srcDecl, location);
/*      */       }
/* 2938 */       else if (dstDecl == srcDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addGlobalTypeDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar)
/*      */   {
/* 2944 */     XSNamedMap components = srcGrammar.getComponents((short)3);
/* 2945 */     int len = components.getLength();
/*      */ 
/* 2949 */     for (int i = 0; i < len; i++) {
/* 2950 */       XSTypeDefinition srcDecl = (XSTypeDefinition)components.item(i);
/* 2951 */       XSTypeDefinition dstDecl = dstGrammar.getGlobalTypeDecl(srcDecl.getName());
/* 2952 */       if (dstDecl == null) {
/* 2953 */         dstGrammar.addGlobalTypeDecl(srcDecl);
/*      */       }
/* 2955 */       else if ((dstDecl != srcDecl) && (!this.fTolerateDuplicates)) {
/* 2956 */         reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2961 */     ObjectList componentsExt = srcGrammar.getComponentsExt((short)3);
/* 2962 */     len = componentsExt.getLength();
/*      */ 
/* 2964 */     for (int i = 0; i < len; i += 2) {
/* 2965 */       String key = (String)componentsExt.item(i);
/* 2966 */       int index = key.indexOf(',');
/* 2967 */       String location = key.substring(0, index);
/* 2968 */       String name = key.substring(index + 1, key.length());
/*      */ 
/* 2970 */       XSTypeDefinition srcDecl = (XSTypeDefinition)componentsExt.item(i + 1);
/* 2971 */       XSTypeDefinition dstDecl = dstGrammar.getGlobalTypeDecl(name, location);
/* 2972 */       if (dstDecl == null) {
/* 2973 */         dstGrammar.addGlobalTypeDecl(srcDecl, location);
/*      */       }
/* 2976 */       else if (dstDecl == srcDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Vector expandComponents(XSObject[] components, Map<String, Vector> dependencies)
/*      */   {
/* 2982 */     Vector newComponents = new Vector();
/*      */ 
/* 2984 */     for (int i = 0; i < components.length; i++) {
/* 2985 */       if (!newComponents.contains(components[i])) {
/* 2986 */         newComponents.add(components[i]);
/*      */       }
/*      */     }
/*      */ 
/* 2990 */     for (int i = 0; i < newComponents.size(); i++) {
/* 2991 */       XSObject component = (XSObject)newComponents.elementAt(i);
/* 2992 */       expandRelatedComponents(component, newComponents, dependencies);
/*      */     }
/*      */ 
/* 2995 */     return newComponents;
/*      */   }
/*      */ 
/*      */   private void expandRelatedComponents(XSObject component, Vector componentList, Map<String, Vector> dependencies) {
/* 2999 */     short componentType = component.getType();
/* 3000 */     switch (componentType) {
/*      */     case 3:
/* 3002 */       expandRelatedTypeComponents((XSTypeDefinition)component, componentList, component.getNamespace(), dependencies);
/* 3003 */       break;
/*      */     case 1:
/* 3005 */       expandRelatedAttributeComponents((XSAttributeDeclaration)component, componentList, component.getNamespace(), dependencies);
/* 3006 */       break;
/*      */     case 5:
/* 3008 */       expandRelatedAttributeGroupComponents((XSAttributeGroupDefinition)component, componentList, component.getNamespace(), dependencies);
/*      */     case 2:
/* 3010 */       expandRelatedElementComponents((XSElementDeclaration)component, componentList, component.getNamespace(), dependencies);
/* 3011 */       break;
/*      */     case 6:
/* 3013 */       expandRelatedModelGroupDefinitionComponents((XSModelGroupDefinition)component, componentList, component.getNamespace(), dependencies);
/*      */     case 4:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     }
/*      */   }
/*      */ 
/*      */   private void expandRelatedAttributeComponents(XSAttributeDeclaration decl, Vector componentList, String namespace, Map<String, Vector> dependencies) {
/* 3024 */     addRelatedType(decl.getTypeDefinition(), componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedElementComponents(XSElementDeclaration decl, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3033 */     addRelatedType(decl.getTypeDefinition(), componentList, namespace, dependencies);
/*      */ 
/* 3040 */     XSElementDeclaration subElemDecl = decl.getSubstitutionGroupAffiliation();
/* 3041 */     if (subElemDecl != null)
/* 3042 */       addRelatedElement(subElemDecl, componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedTypeComponents(XSTypeDefinition type, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3047 */     if ((type instanceof XSComplexTypeDecl)) {
/* 3048 */       expandRelatedComplexTypeComponents((XSComplexTypeDecl)type, componentList, namespace, dependencies);
/*      */     }
/* 3050 */     else if ((type instanceof XSSimpleTypeDecl))
/* 3051 */       expandRelatedSimpleTypeComponents((XSSimpleTypeDefinition)type, componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedModelGroupDefinitionComponents(XSModelGroupDefinition modelGroupDef, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3057 */     expandRelatedModelGroupComponents(modelGroupDef.getModelGroup(), componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedAttributeGroupComponents(XSAttributeGroupDefinition attrGroup, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3062 */     expandRelatedAttributeUsesComponents(attrGroup.getAttributeUses(), componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedComplexTypeComponents(XSComplexTypeDecl type, Vector componentList, String namespace, Map<String, Vector> dependencies) {
/* 3066 */     addRelatedType(type.getBaseType(), componentList, namespace, dependencies);
/* 3067 */     expandRelatedAttributeUsesComponents(type.getAttributeUses(), componentList, namespace, dependencies);
/* 3068 */     XSParticle particle = type.getParticle();
/* 3069 */     if (particle != null)
/* 3070 */       expandRelatedParticleComponents(particle, componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedSimpleTypeComponents(XSSimpleTypeDefinition type, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3075 */     XSTypeDefinition baseType = type.getBaseType();
/* 3076 */     if (baseType != null) {
/* 3077 */       addRelatedType(baseType, componentList, namespace, dependencies);
/*      */     }
/*      */ 
/* 3080 */     XSTypeDefinition itemType = type.getItemType();
/* 3081 */     if (itemType != null) {
/* 3082 */       addRelatedType(itemType, componentList, namespace, dependencies);
/*      */     }
/*      */ 
/* 3085 */     XSTypeDefinition primitiveType = type.getPrimitiveType();
/* 3086 */     if (primitiveType != null) {
/* 3087 */       addRelatedType(primitiveType, componentList, namespace, dependencies);
/*      */     }
/*      */ 
/* 3090 */     XSObjectList memberTypes = type.getMemberTypes();
/* 3091 */     if (memberTypes.size() > 0)
/* 3092 */       for (int i = 0; i < memberTypes.size(); i++)
/* 3093 */         addRelatedType((XSTypeDefinition)memberTypes.item(i), componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedAttributeUsesComponents(XSObjectList attrUses, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3100 */     int attrUseSize = attrUses == null ? 0 : attrUses.size();
/* 3101 */     for (int i = 0; i < attrUseSize; i++)
/* 3102 */       expandRelatedAttributeUseComponents((XSAttributeUse)attrUses.item(i), componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedAttributeUseComponents(XSAttributeUse component, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3108 */     addRelatedAttribute(component.getAttrDeclaration(), componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void expandRelatedParticleComponents(XSParticle component, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3113 */     XSTerm term = component.getTerm();
/* 3114 */     switch (term.getType()) {
/*      */     case 2:
/* 3116 */       addRelatedElement((XSElementDeclaration)term, componentList, namespace, dependencies);
/* 3117 */       break;
/*      */     case 7:
/* 3119 */       expandRelatedModelGroupComponents((XSModelGroup)term, componentList, namespace, dependencies);
/* 3120 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void expandRelatedModelGroupComponents(XSModelGroup modelGroup, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3128 */     XSObjectList particles = modelGroup.getParticles();
/* 3129 */     int length = particles == null ? 0 : particles.getLength();
/* 3130 */     for (int i = 0; i < length; i++)
/* 3131 */       expandRelatedParticleComponents((XSParticle)particles.item(i), componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void addRelatedType(XSTypeDefinition type, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3136 */     if (!type.getAnonymous()) {
/* 3137 */       if ((!type.getNamespace().equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) && 
/* 3138 */         (!componentList.contains(type))) {
/* 3139 */         Vector importedNamespaces = findDependentNamespaces(namespace, dependencies);
/* 3140 */         addNamespaceDependency(namespace, type.getNamespace(), importedNamespaces);
/* 3141 */         componentList.add(type);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 3146 */       expandRelatedTypeComponents(type, componentList, namespace, dependencies);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addRelatedElement(XSElementDeclaration decl, Vector componentList, String namespace, Map<String, Vector> dependencies) {
/* 3151 */     if (decl.getScope() == 1) {
/* 3152 */       if (!componentList.contains(decl)) {
/* 3153 */         Vector importedNamespaces = findDependentNamespaces(namespace, dependencies);
/* 3154 */         addNamespaceDependency(namespace, decl.getNamespace(), importedNamespaces);
/* 3155 */         componentList.add(decl);
/*      */       }
/*      */     }
/*      */     else
/* 3159 */       expandRelatedElementComponents(decl, componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void addRelatedAttribute(XSAttributeDeclaration decl, Vector componentList, String namespace, Map<String, Vector> dependencies)
/*      */   {
/* 3164 */     if (decl.getScope() == 1) {
/* 3165 */       if (!componentList.contains(decl)) {
/* 3166 */         Vector importedNamespaces = findDependentNamespaces(namespace, dependencies);
/* 3167 */         addNamespaceDependency(namespace, decl.getNamespace(), importedNamespaces);
/* 3168 */         componentList.add(decl);
/*      */       }
/*      */     }
/*      */     else
/* 3172 */       expandRelatedAttributeComponents(decl, componentList, namespace, dependencies);
/*      */   }
/*      */ 
/*      */   private void addGlobalComponents(Vector components, Map<String, Vector> importDependencies)
/*      */   {
/* 3177 */     XSDDescription desc = new XSDDescription();
/* 3178 */     int size = components.size();
/*      */ 
/* 3180 */     for (int i = 0; i < size; i++) {
/* 3181 */       addGlobalComponent((XSObject)components.elementAt(i), desc);
/*      */     }
/* 3183 */     updateImportDependencies(importDependencies);
/*      */   }
/*      */ 
/*      */   private void addGlobalComponent(XSObject component, XSDDescription desc) {
/* 3187 */     String namespace = component.getNamespace();
/*      */ 
/* 3189 */     desc.setNamespace(namespace);
/* 3190 */     SchemaGrammar sg = getSchemaGrammar(desc);
/*      */ 
/* 3192 */     short componentType = component.getType();
/* 3193 */     String name = component.getName();
/*      */ 
/* 3195 */     switch (componentType) {
/*      */     case 3:
/* 3197 */       if (!((XSTypeDefinition)component).getAnonymous()) {
/* 3198 */         if (sg.getGlobalTypeDecl(name) == null) {
/* 3199 */           sg.addGlobalTypeDecl((XSTypeDefinition)component);
/*      */         }
/*      */ 
/* 3202 */         if (sg.getGlobalTypeDecl(name, "") == null)
/* 3203 */           sg.addGlobalTypeDecl((XSTypeDefinition)component, "");  } break;
/*      */     case 1:
/* 3208 */       if (((XSAttributeDecl)component).getScope() == 1) {
/* 3209 */         if (sg.getGlobalAttributeDecl(name) == null) {
/* 3210 */           sg.addGlobalAttributeDecl((XSAttributeDecl)component);
/*      */         }
/*      */ 
/* 3213 */         if (sg.getGlobalAttributeDecl(name, "") == null)
/* 3214 */           sg.addGlobalAttributeDecl((XSAttributeDecl)component, "");  } break;
/*      */     case 5:
/* 3219 */       if (sg.getGlobalAttributeDecl(name) == null) {
/* 3220 */         sg.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)component);
/*      */       }
/*      */ 
/* 3223 */       if (sg.getGlobalAttributeDecl(name, "") == null)
/* 3224 */         sg.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)component, ""); break;
/*      */     case 2:
/* 3228 */       if (((XSElementDecl)component).getScope() == 1) {
/* 3229 */         sg.addGlobalElementDeclAll((XSElementDecl)component);
/*      */ 
/* 3231 */         if (sg.getGlobalElementDecl(name) == null) {
/* 3232 */           sg.addGlobalElementDecl((XSElementDecl)component);
/*      */         }
/*      */ 
/* 3235 */         if (sg.getGlobalElementDecl(name, "") == null)
/* 3236 */           sg.addGlobalElementDecl((XSElementDecl)component, "");  } break;
/*      */     case 6:
/* 3241 */       if (sg.getGlobalGroupDecl(name) == null) {
/* 3242 */         sg.addGlobalGroupDecl((XSGroupDecl)component);
/*      */       }
/*      */ 
/* 3245 */       if (sg.getGlobalGroupDecl(name, "") == null)
/* 3246 */         sg.addGlobalGroupDecl((XSGroupDecl)component, ""); break;
/*      */     case 11:
/* 3250 */       if (sg.getGlobalNotationDecl(name) == null) {
/* 3251 */         sg.addGlobalNotationDecl((XSNotationDecl)component);
/*      */       }
/*      */ 
/* 3254 */       if (sg.getGlobalNotationDecl(name, "") == null)
/* 3255 */         sg.addGlobalNotationDecl((XSNotationDecl)component, ""); break;
/*      */     case 4:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateImportDependencies(Map<String, Vector> table)
/*      */   {
/* 3266 */     if (table == null) return;
/*      */ 
/* 3270 */     for (Map.Entry entry : table.entrySet()) {
/* 3271 */       String namespace = (String)entry.getKey();
/* 3272 */       Vector importList = (Vector)entry.getValue();
/* 3273 */       if (importList.size() > 0)
/* 3274 */         expandImportList(namespace, importList);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void expandImportList(String namespace, Vector namespaceList)
/*      */   {
/* 3280 */     SchemaGrammar sg = this.fGrammarBucket.getGrammar(namespace);
/*      */ 
/* 3282 */     if (sg != null) {
/* 3283 */       Vector isgs = sg.getImportedGrammars();
/* 3284 */       if (isgs == null) {
/* 3285 */         isgs = new Vector();
/* 3286 */         addImportList(sg, isgs, namespaceList);
/* 3287 */         sg.setImportedGrammars(isgs);
/*      */       }
/*      */       else {
/* 3290 */         updateImportList(sg, isgs, namespaceList);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addImportList(SchemaGrammar sg, Vector importedGrammars, Vector namespaceList) {
/* 3296 */     int size = namespaceList.size();
/*      */ 
/* 3299 */     for (int i = 0; i < size; i++) {
/* 3300 */       SchemaGrammar isg = this.fGrammarBucket.getGrammar((String)namespaceList.elementAt(i));
/* 3301 */       if (isg != null)
/* 3302 */         importedGrammars.add(isg);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateImportList(SchemaGrammar sg, Vector importedGrammars, Vector namespaceList)
/*      */   {
/* 3311 */     int size = namespaceList.size();
/*      */ 
/* 3314 */     for (int i = 0; i < size; i++) {
/* 3315 */       SchemaGrammar isg = this.fGrammarBucket.getGrammar((String)namespaceList.elementAt(i));
/* 3316 */       if ((isg != null) && 
/* 3317 */         (!containedImportedGrammar(importedGrammars, isg)))
/* 3318 */         importedGrammars.add(isg);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean containedImportedGrammar(Vector importedGrammar, SchemaGrammar grammar)
/*      */   {
/* 3328 */     int size = importedGrammar.size();
/*      */ 
/* 3331 */     for (int i = 0; i < size; i++) {
/* 3332 */       SchemaGrammar sg = (SchemaGrammar)importedGrammar.elementAt(i);
/* 3333 */       if (null2EmptyString(sg.getTargetNamespace()).equals(null2EmptyString(grammar.getTargetNamespace()))) {
/* 3334 */         return true;
/*      */       }
/*      */     }
/* 3337 */     return false;
/*      */   }
/*      */ 
/*      */   private SchemaGrammar getSchemaGrammar(XSDDescription desc)
/*      */   {
/* 3343 */     SchemaGrammar sg = findGrammar(desc, this.fNamespaceGrowth);
/*      */ 
/* 3345 */     if (sg == null) {
/* 3346 */       sg = new SchemaGrammar(desc.getNamespace(), desc.makeClone(), this.fSymbolTable);
/* 3347 */       this.fGrammarBucket.putGrammar(sg);
/*      */     }
/* 3349 */     else if (sg.isImmutable()) {
/* 3350 */       sg = createGrammarFrom(sg);
/*      */     }
/*      */ 
/* 3353 */     return sg;
/*      */   }
/*      */ 
/*      */   private Vector findDependentNamespaces(String namespace, Map table) {
/* 3357 */     String ns = null2EmptyString(namespace);
/* 3358 */     Vector namespaceList = (Vector)getFromMap(table, ns);
/*      */ 
/* 3360 */     if (namespaceList == null) {
/* 3361 */       namespaceList = new Vector();
/* 3362 */       table.put(ns, namespaceList);
/*      */     }
/*      */ 
/* 3365 */     return namespaceList;
/*      */   }
/*      */ 
/*      */   private void addNamespaceDependency(String namespace1, String namespace2, Vector list) {
/* 3369 */     String ns1 = null2EmptyString(namespace1);
/* 3370 */     String ns2 = null2EmptyString(namespace2);
/* 3371 */     if ((!ns1.equals(ns2)) && 
/* 3372 */       (!list.contains(ns2)))
/* 3373 */       list.add(ns2);
/*      */   }
/*      */ 
/*      */   private void reportSharingError(String namespace, String name)
/*      */   {
/* 3379 */     String qName = namespace + "," + name;
/*      */ 
/* 3382 */     reportSchemaError("sch-props-correct.2", new Object[] { qName }, null);
/*      */   }
/*      */ 
/*      */   private void createTraversers()
/*      */   {
/* 3391 */     this.fAttributeChecker = new XSAttributeChecker(this);
/* 3392 */     this.fAttributeGroupTraverser = new XSDAttributeGroupTraverser(this, this.fAttributeChecker);
/* 3393 */     this.fAttributeTraverser = new XSDAttributeTraverser(this, this.fAttributeChecker);
/* 3394 */     this.fComplexTypeTraverser = new XSDComplexTypeTraverser(this, this.fAttributeChecker);
/* 3395 */     this.fElementTraverser = new XSDElementTraverser(this, this.fAttributeChecker);
/* 3396 */     this.fGroupTraverser = new XSDGroupTraverser(this, this.fAttributeChecker);
/* 3397 */     this.fKeyrefTraverser = new XSDKeyrefTraverser(this, this.fAttributeChecker);
/* 3398 */     this.fNotationTraverser = new XSDNotationTraverser(this, this.fAttributeChecker);
/* 3399 */     this.fSimpleTypeTraverser = new XSDSimpleTypeTraverser(this, this.fAttributeChecker);
/* 3400 */     this.fUniqueOrKeyTraverser = new XSDUniqueOrKeyTraverser(this, this.fAttributeChecker);
/* 3401 */     this.fWildCardTraverser = new XSDWildcardTraverser(this, this.fAttributeChecker);
/*      */   }
/*      */ 
/*      */   void prepareForParse()
/*      */   {
/* 3407 */     this.fTraversed.clear();
/* 3408 */     this.fDoc2SystemId.clear();
/* 3409 */     this.fHiddenNodes.clear();
/* 3410 */     this.fLastSchemaWasDuplicate = false;
/*      */   }
/*      */ 
/*      */   void prepareForTraverse()
/*      */   {
/* 3416 */     if (!this.registryEmpty) {
/* 3417 */       this.fUnparsedAttributeRegistry.clear();
/* 3418 */       this.fUnparsedAttributeGroupRegistry.clear();
/* 3419 */       this.fUnparsedElementRegistry.clear();
/* 3420 */       this.fUnparsedGroupRegistry.clear();
/* 3421 */       this.fUnparsedIdentityConstraintRegistry.clear();
/* 3422 */       this.fUnparsedNotationRegistry.clear();
/* 3423 */       this.fUnparsedTypeRegistry.clear();
/*      */ 
/* 3425 */       this.fUnparsedAttributeRegistrySub.clear();
/* 3426 */       this.fUnparsedAttributeGroupRegistrySub.clear();
/* 3427 */       this.fUnparsedElementRegistrySub.clear();
/* 3428 */       this.fUnparsedGroupRegistrySub.clear();
/* 3429 */       this.fUnparsedIdentityConstraintRegistrySub.clear();
/* 3430 */       this.fUnparsedNotationRegistrySub.clear();
/* 3431 */       this.fUnparsedTypeRegistrySub.clear();
/*      */     }
/*      */ 
/* 3434 */     for (int i = 1; i <= 7; i++) {
/* 3435 */       if (this.fUnparsedRegistriesExt[i] != null) {
/* 3436 */         this.fUnparsedRegistriesExt[i].clear();
/*      */       }
/*      */     }
/* 3439 */     this.fDependencyMap.clear();
/* 3440 */     this.fDoc2XSDocumentMap.clear();
/* 3441 */     if (this.fRedefine2XSDMap != null) this.fRedefine2XSDMap.clear();
/* 3442 */     if (this.fRedefine2NSSupport != null) this.fRedefine2NSSupport.clear();
/* 3443 */     this.fAllTNSs.removeAllElements();
/* 3444 */     this.fImportMap.clear();
/* 3445 */     this.fRoot = null;
/*      */ 
/* 3448 */     for (int i = 0; i < this.fLocalElemStackPos; i++) {
/* 3449 */       this.fParticle[i] = null;
/* 3450 */       this.fLocalElementDecl[i] = null;
/* 3451 */       this.fLocalElementDecl_schema[i] = null;
/* 3452 */       this.fLocalElemNamespaceContext[i] = null;
/*      */     }
/* 3454 */     this.fLocalElemStackPos = 0;
/*      */ 
/* 3457 */     for (int i = 0; i < this.fKeyrefStackPos; i++) {
/* 3458 */       this.fKeyrefs[i] = null;
/* 3459 */       this.fKeyrefElems[i] = null;
/* 3460 */       this.fKeyrefNamespaceContext[i] = null;
/* 3461 */       this.fKeyrefsMapXSDocumentInfo[i] = null;
/*      */     }
/* 3463 */     this.fKeyrefStackPos = 0;
/*      */ 
/* 3466 */     if (this.fAttributeChecker == null) {
/* 3467 */       createTraversers();
/*      */     }
/*      */ 
/* 3471 */     Locale locale = this.fErrorReporter.getLocale();
/* 3472 */     this.fAttributeChecker.reset(this.fSymbolTable);
/* 3473 */     this.fAttributeGroupTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3474 */     this.fAttributeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3475 */     this.fComplexTypeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3476 */     this.fElementTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3477 */     this.fGroupTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3478 */     this.fKeyrefTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3479 */     this.fNotationTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3480 */     this.fSimpleTypeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3481 */     this.fUniqueOrKeyTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/* 3482 */     this.fWildCardTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
/*      */ 
/* 3484 */     this.fRedefinedRestrictedAttributeGroupRegistry.clear();
/* 3485 */     this.fRedefinedRestrictedGroupRegistry.clear();
/*      */ 
/* 3487 */     this.fGlobalAttrDecls.clear();
/* 3488 */     this.fGlobalAttrGrpDecls.clear();
/* 3489 */     this.fGlobalElemDecls.clear();
/* 3490 */     this.fGlobalGroupDecls.clear();
/* 3491 */     this.fGlobalNotationDecls.clear();
/* 3492 */     this.fGlobalIDConstraintDecls.clear();
/* 3493 */     this.fGlobalTypeDecls.clear();
/*      */   }
/*      */   public void setDeclPool(XSDeclarationPool declPool) {
/* 3496 */     this.fDeclPool = declPool;
/*      */   }
/*      */   public void setDVFactory(SchemaDVFactory dvFactory) {
/* 3499 */     this.fDVFactory = dvFactory;
/*      */   }
/*      */   public SchemaDVFactory getDVFactory() {
/* 3502 */     return this.fDVFactory;
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */   {
/* 3508 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*      */ 
/* 3510 */     this.fSecureProcessing = null;
/* 3511 */     if (componentManager != null) {
/* 3512 */       this.fSecureProcessing = ((XMLSecurityManager)componentManager.getProperty("http://apache.org/xml/properties/security-manager", null));
/*      */     }
/*      */ 
/* 3516 */     this.fEntityResolver = ((XMLEntityResolver)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
/* 3517 */     XMLEntityResolver er = (XMLEntityResolver)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
/* 3518 */     if (er != null) {
/* 3519 */       this.fSchemaParser.setEntityResolver(er);
/*      */     }
/*      */ 
/* 3522 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*      */     try
/*      */     {
/* 3525 */       XMLErrorHandler currErrorHandler = this.fErrorReporter.getErrorHandler();
/*      */ 
/* 3529 */       if (currErrorHandler != this.fSchemaParser.getProperty("http://apache.org/xml/properties/internal/error-handler")) {
/* 3530 */         this.fSchemaParser.setProperty("http://apache.org/xml/properties/internal/error-handler", currErrorHandler != null ? currErrorHandler : new DefaultErrorHandler());
/* 3531 */         if (this.fAnnotationValidator != null) {
/* 3532 */           this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/error-handler", currErrorHandler != null ? currErrorHandler : new DefaultErrorHandler());
/*      */         }
/*      */       }
/* 3535 */       Locale currentLocale = this.fErrorReporter.getLocale();
/* 3536 */       if (currentLocale != this.fSchemaParser.getProperty("http://apache.org/xml/properties/locale")) {
/* 3537 */         this.fSchemaParser.setProperty("http://apache.org/xml/properties/locale", currentLocale);
/* 3538 */         if (this.fAnnotationValidator != null)
/* 3539 */           this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/locale", currentLocale);
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/* 3545 */     this.fValidateAnnotations = componentManager.getFeature("http://apache.org/xml/features/validate-annotations", false);
/* 3546 */     this.fHonourAllSchemaLocations = componentManager.getFeature("http://apache.org/xml/features/honour-all-schemaLocations", false);
/* 3547 */     this.fNamespaceGrowth = componentManager.getFeature("http://apache.org/xml/features/namespace-growth", false);
/* 3548 */     this.fTolerateDuplicates = componentManager.getFeature("http://apache.org/xml/features/internal/tolerate-duplicates", false);
/*      */     try
/*      */     {
/* 3551 */       this.fSchemaParser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", this.fErrorReporter.getFeature("http://apache.org/xml/features/continue-after-fatal-error"));
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*      */     try
/*      */     {
/* 3558 */       if (componentManager.getFeature("http://apache.org/xml/features/allow-java-encodings", false))
/* 3559 */         this.fSchemaParser.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/*      */     }
/*      */     try {
/* 3564 */       if (componentManager.getFeature("http://apache.org/xml/features/standard-uri-conformant", false))
/* 3565 */         this.fSchemaParser.setFeature("http://apache.org/xml/features/standard-uri-conformant", true);
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*      */     try {
/* 3571 */       this.fGrammarPool = ((XMLGrammarPool)componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool"));
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/* 3574 */       this.fGrammarPool = null;
/*      */     }
/*      */     try
/*      */     {
/* 3578 */       if (componentManager.getFeature("http://apache.org/xml/features/disallow-doctype-decl", false))
/* 3579 */         this.fSchemaParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/*      */     }
/*      */     try {
/* 3584 */       Object security = componentManager.getProperty("http://apache.org/xml/properties/security-manager", null);
/* 3585 */       if (security != null)
/* 3586 */         this.fSchemaParser.setProperty("http://apache.org/xml/properties/security-manager", security);
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/* 3591 */     XMLSecurityPropertyManager securityPropertyMgr = (XMLSecurityPropertyManager)componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
/*      */ 
/* 3594 */     this.fSchemaParser.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", securityPropertyMgr);
/*      */ 
/* 3596 */     this.fAccessExternalDTD = securityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */ 
/* 3599 */     this.fAccessExternalSchema = securityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA);
/*      */   }
/*      */ 
/*      */   void traverseLocalElements()
/*      */   {
/* 3610 */     this.fElementTraverser.fDeferTraversingLocalElements = false;
/*      */ 
/* 3612 */     for (int i = 0; i < this.fLocalElemStackPos; i++) {
/* 3613 */       Element currElem = this.fLocalElementDecl[i];
/*      */ 
/* 3616 */       XSDocumentInfo currSchema = this.fLocalElementDecl_schema[i];
/* 3617 */       SchemaGrammar currGrammar = this.fGrammarBucket.getGrammar(currSchema.fTargetNamespace);
/* 3618 */       this.fElementTraverser.traverseLocal(this.fParticle[i], currElem, currSchema, currGrammar, this.fAllContext[i], this.fParent[i], this.fLocalElemNamespaceContext[i]);
/*      */ 
/* 3620 */       if (this.fParticle[i].fType == 0) {
/* 3621 */         XSModelGroupImpl group = null;
/* 3622 */         if ((this.fParent[i] instanceof XSComplexTypeDecl)) {
/* 3623 */           XSParticle p = ((XSComplexTypeDecl)this.fParent[i]).getParticle();
/* 3624 */           if (p != null)
/* 3625 */             group = (XSModelGroupImpl)p.getTerm();
/*      */         }
/*      */         else {
/* 3628 */           group = ((XSGroupDecl)this.fParent[i]).fModelGroup;
/*      */         }
/* 3630 */         if (group != null)
/* 3631 */           removeParticle(group, this.fParticle[i]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean removeParticle(XSModelGroupImpl group, XSParticleDecl particle)
/*      */   {
/* 3638 */     for (int i = 0; i < group.fParticleCount; i++) {
/* 3639 */       XSParticleDecl member = group.fParticles[i];
/* 3640 */       if (member == particle) {
/* 3641 */         for (int j = i; j < group.fParticleCount - 1; j++)
/* 3642 */           group.fParticles[j] = group.fParticles[(j + 1)];
/* 3643 */         group.fParticleCount -= 1;
/* 3644 */         return true;
/*      */       }
/* 3646 */       if ((member.fType == 3) && 
/* 3647 */         (removeParticle((XSModelGroupImpl)member.fValue, particle))) {
/* 3648 */         return true;
/*      */       }
/*      */     }
/* 3651 */     return false;
/*      */   }
/*      */ 
/*      */   void fillInLocalElemInfo(Element elmDecl, XSDocumentInfo schemaDoc, int allContextFlags, XSObject parent, XSParticleDecl particle)
/*      */   {
/* 3663 */     if (this.fParticle.length == this.fLocalElemStackPos)
/*      */     {
/* 3665 */       XSParticleDecl[] newStackP = new XSParticleDecl[this.fLocalElemStackPos + 10];
/* 3666 */       System.arraycopy(this.fParticle, 0, newStackP, 0, this.fLocalElemStackPos);
/* 3667 */       this.fParticle = newStackP;
/* 3668 */       Element[] newStackE = new Element[this.fLocalElemStackPos + 10];
/* 3669 */       System.arraycopy(this.fLocalElementDecl, 0, newStackE, 0, this.fLocalElemStackPos);
/* 3670 */       this.fLocalElementDecl = newStackE;
/* 3671 */       XSDocumentInfo[] newStackE_schema = new XSDocumentInfo[this.fLocalElemStackPos + 10];
/* 3672 */       System.arraycopy(this.fLocalElementDecl_schema, 0, newStackE_schema, 0, this.fLocalElemStackPos);
/* 3673 */       this.fLocalElementDecl_schema = newStackE_schema;
/* 3674 */       int[] newStackI = new int[this.fLocalElemStackPos + 10];
/* 3675 */       System.arraycopy(this.fAllContext, 0, newStackI, 0, this.fLocalElemStackPos);
/* 3676 */       this.fAllContext = newStackI;
/* 3677 */       XSObject[] newStackC = new XSObject[this.fLocalElemStackPos + 10];
/* 3678 */       System.arraycopy(this.fParent, 0, newStackC, 0, this.fLocalElemStackPos);
/* 3679 */       this.fParent = newStackC;
/* 3680 */       String[][] newStackN = new String[this.fLocalElemStackPos + 10][];
/* 3681 */       System.arraycopy(this.fLocalElemNamespaceContext, 0, newStackN, 0, this.fLocalElemStackPos);
/* 3682 */       this.fLocalElemNamespaceContext = newStackN;
/*      */     }
/*      */ 
/* 3685 */     this.fParticle[this.fLocalElemStackPos] = particle;
/* 3686 */     this.fLocalElementDecl[this.fLocalElemStackPos] = elmDecl;
/* 3687 */     this.fLocalElementDecl_schema[this.fLocalElemStackPos] = schemaDoc;
/* 3688 */     this.fAllContext[this.fLocalElemStackPos] = allContextFlags;
/* 3689 */     this.fParent[this.fLocalElemStackPos] = parent;
/* 3690 */     this.fLocalElemNamespaceContext[(this.fLocalElemStackPos++)] = schemaDoc.fNamespaceSupport.getEffectiveLocalContext();
/*      */   }
/*      */ 
/*      */   void checkForDuplicateNames(String qName, int declType, Map<String, Element> registry, Map<String, XSDocumentInfo> registry_sub, Element currComp, XSDocumentInfo currSchema)
/*      */   {
/* 3704 */     Object objElem = null;
/*      */ 
/* 3707 */     if ((objElem = registry.get(qName)) == null)
/*      */     {
/* 3710 */       if ((this.fNamespaceGrowth) && (!this.fTolerateDuplicates)) {
/* 3711 */         checkForDuplicateNames(qName, declType, currComp);
/*      */       }
/*      */ 
/* 3714 */       registry.put(qName, currComp);
/* 3715 */       registry_sub.put(qName, currSchema);
/*      */     }
/*      */     else {
/* 3718 */       Element collidingElem = (Element)objElem;
/* 3719 */       XSDocumentInfo collidingElemSchema = (XSDocumentInfo)registry_sub.get(qName);
/* 3720 */       if (collidingElem == currComp) return;
/* 3721 */       Element elemParent = null;
/* 3722 */       XSDocumentInfo redefinedSchema = null;
/*      */ 
/* 3725 */       boolean collidedWithRedefine = true;
/* 3726 */       if (DOMUtil.getLocalName(elemParent = DOMUtil.getParent(collidingElem)).equals(SchemaSymbols.ELT_REDEFINE)) {
/* 3727 */         redefinedSchema = this.fRedefine2XSDMap != null ? (XSDocumentInfo)this.fRedefine2XSDMap.get(elemParent) : null;
/*      */       }
/* 3730 */       else if (DOMUtil.getLocalName(DOMUtil.getParent(currComp)).equals(SchemaSymbols.ELT_REDEFINE)) {
/* 3731 */         redefinedSchema = collidingElemSchema;
/* 3732 */         collidedWithRedefine = false;
/*      */       }
/* 3734 */       if (redefinedSchema != null)
/*      */       {
/* 3737 */         if (collidingElemSchema == currSchema) {
/* 3738 */           reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
/* 3739 */           return;
/*      */         }
/*      */ 
/* 3742 */         String newName = qName.substring(qName.lastIndexOf(',') + 1) + "_fn3dktizrknc9pi";
/* 3743 */         if (redefinedSchema == currSchema)
/*      */         {
/* 3745 */           currComp.setAttribute(SchemaSymbols.ATT_NAME, newName);
/* 3746 */           if (currSchema.fTargetNamespace == null) {
/* 3747 */             registry.put("," + newName, currComp);
/* 3748 */             registry_sub.put("," + newName, currSchema);
/*      */           }
/*      */           else {
/* 3751 */             registry.put(currSchema.fTargetNamespace + "," + newName, currComp);
/* 3752 */             registry_sub.put(currSchema.fTargetNamespace + "," + newName, currSchema);
/*      */           }
/*      */ 
/* 3755 */           if (currSchema.fTargetNamespace == null)
/* 3756 */             checkForDuplicateNames("," + newName, declType, registry, registry_sub, currComp, currSchema);
/*      */           else {
/* 3758 */             checkForDuplicateNames(currSchema.fTargetNamespace + "," + newName, declType, registry, registry_sub, currComp, currSchema);
/*      */           }
/*      */         }
/* 3761 */         else if (collidedWithRedefine) {
/* 3762 */           if (currSchema.fTargetNamespace == null)
/* 3763 */             checkForDuplicateNames("," + newName, declType, registry, registry_sub, currComp, currSchema);
/*      */           else
/* 3765 */             checkForDuplicateNames(currSchema.fTargetNamespace + "," + newName, declType, registry, registry_sub, currComp, currSchema);
/*      */         }
/*      */         else
/*      */         {
/* 3769 */           reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
/*      */         }
/*      */ 
/*      */       }
/* 3777 */       else if (!this.fTolerateDuplicates) {
/* 3778 */         reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
/* 3779 */       } else if ((this.fUnparsedRegistriesExt[declType] != null) && 
/* 3780 */         (this.fUnparsedRegistriesExt[declType].get(qName) == currSchema)) {
/* 3781 */         reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3788 */     if (this.fTolerateDuplicates) {
/* 3789 */       if (this.fUnparsedRegistriesExt[declType] == null)
/* 3790 */         this.fUnparsedRegistriesExt[declType] = new HashMap();
/* 3791 */       this.fUnparsedRegistriesExt[declType].put(qName, currSchema);
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkForDuplicateNames(String qName, int declType, Element currComp)
/*      */   {
/* 3797 */     int namespaceEnd = qName.indexOf(',');
/* 3798 */     String namespace = qName.substring(0, namespaceEnd);
/* 3799 */     SchemaGrammar grammar = this.fGrammarBucket.getGrammar(emptyString2Null(namespace));
/*      */ 
/* 3801 */     if (grammar != null) {
/* 3802 */       Object obj = getGlobalDeclFromGrammar(grammar, declType, qName.substring(namespaceEnd + 1));
/* 3803 */       if (obj != null)
/* 3804 */         reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void renameRedefiningComponents(XSDocumentInfo currSchema, Element child, String componentType, String oldName, String newName)
/*      */   {
/* 3818 */     if (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
/* 3819 */       Element grandKid = DOMUtil.getFirstChildElement(child);
/* 3820 */       if (grandKid == null) {
/* 3821 */         reportSchemaError("src-redefine.5.a.a", null, child);
/*      */       }
/*      */       else {
/* 3824 */         String grandKidName = DOMUtil.getLocalName(grandKid);
/* 3825 */         if (grandKidName.equals(SchemaSymbols.ELT_ANNOTATION)) {
/* 3826 */           grandKid = DOMUtil.getNextSiblingElement(grandKid);
/*      */         }
/* 3828 */         if (grandKid == null) {
/* 3829 */           reportSchemaError("src-redefine.5.a.a", null, child);
/*      */         }
/*      */         else {
/* 3832 */           grandKidName = DOMUtil.getLocalName(grandKid);
/* 3833 */           if (!grandKidName.equals(SchemaSymbols.ELT_RESTRICTION)) {
/* 3834 */             reportSchemaError("src-redefine.5.a.b", new Object[] { grandKidName }, child);
/*      */           }
/*      */           else {
/* 3837 */             Object[] attrs = this.fAttributeChecker.checkAttributes(grandKid, false, currSchema);
/* 3838 */             QName derivedBase = (QName)attrs[XSAttributeChecker.ATTIDX_BASE];
/* 3839 */             if ((derivedBase == null) || (derivedBase.uri != currSchema.fTargetNamespace) || (!derivedBase.localpart.equals(oldName)))
/*      */             {
/* 3842 */               reportSchemaError("src-redefine.5.a.c", new Object[] { grandKidName, (currSchema.fTargetNamespace == null ? "" : currSchema.fTargetNamespace) + "," + oldName }, child);
/*      */             }
/* 3850 */             else if ((derivedBase.prefix != null) && (derivedBase.prefix.length() > 0)) {
/* 3851 */               grandKid.setAttribute(SchemaSymbols.ATT_BASE, derivedBase.prefix + ":" + newName);
/*      */             }
/*      */             else {
/* 3854 */               grandKid.setAttribute(SchemaSymbols.ATT_BASE, newName);
/*      */             }
/*      */ 
/* 3857 */             this.fAttributeChecker.returnAttrArray(attrs, currSchema);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 3862 */     else if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
/* 3863 */       Element grandKid = DOMUtil.getFirstChildElement(child);
/* 3864 */       if (grandKid == null) {
/* 3865 */         reportSchemaError("src-redefine.5.b.a", null, child);
/*      */       }
/*      */       else {
/* 3868 */         if (DOMUtil.getLocalName(grandKid).equals(SchemaSymbols.ELT_ANNOTATION)) {
/* 3869 */           grandKid = DOMUtil.getNextSiblingElement(grandKid);
/*      */         }
/* 3871 */         if (grandKid == null) {
/* 3872 */           reportSchemaError("src-redefine.5.b.a", null, child);
/*      */         }
/*      */         else
/*      */         {
/* 3876 */           Element greatGrandKid = DOMUtil.getFirstChildElement(grandKid);
/* 3877 */           if (greatGrandKid == null) {
/* 3878 */             reportSchemaError("src-redefine.5.b.b", null, grandKid);
/*      */           }
/*      */           else {
/* 3881 */             String greatGrandKidName = DOMUtil.getLocalName(greatGrandKid);
/* 3882 */             if (greatGrandKidName.equals(SchemaSymbols.ELT_ANNOTATION)) {
/* 3883 */               greatGrandKid = DOMUtil.getNextSiblingElement(greatGrandKid);
/*      */             }
/* 3885 */             if (greatGrandKid == null) {
/* 3886 */               reportSchemaError("src-redefine.5.b.b", null, grandKid);
/*      */             }
/*      */             else {
/* 3889 */               greatGrandKidName = DOMUtil.getLocalName(greatGrandKid);
/* 3890 */               if ((!greatGrandKidName.equals(SchemaSymbols.ELT_RESTRICTION)) && (!greatGrandKidName.equals(SchemaSymbols.ELT_EXTENSION)))
/*      */               {
/* 3892 */                 reportSchemaError("src-redefine.5.b.c", new Object[] { greatGrandKidName }, greatGrandKid);
/*      */               }
/*      */               else {
/* 3895 */                 Object[] attrs = this.fAttributeChecker.checkAttributes(greatGrandKid, false, currSchema);
/* 3896 */                 QName derivedBase = (QName)attrs[XSAttributeChecker.ATTIDX_BASE];
/* 3897 */                 if ((derivedBase == null) || (derivedBase.uri != currSchema.fTargetNamespace) || (!derivedBase.localpart.equals(oldName)))
/*      */                 {
/* 3900 */                   reportSchemaError("src-redefine.5.b.d", new Object[] { greatGrandKidName, (currSchema.fTargetNamespace == null ? "" : currSchema.fTargetNamespace) + "," + oldName }, greatGrandKid);
/*      */                 }
/* 3908 */                 else if ((derivedBase.prefix != null) && (derivedBase.prefix.length() > 0)) {
/* 3909 */                   greatGrandKid.setAttribute(SchemaSymbols.ATT_BASE, derivedBase.prefix + ":" + newName);
/*      */                 }
/*      */                 else {
/* 3912 */                   greatGrandKid.setAttribute(SchemaSymbols.ATT_BASE, newName);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 3922 */     else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
/* 3923 */       String processedBaseName = currSchema.fTargetNamespace + "," + oldName;
/*      */ 
/* 3925 */       int attGroupRefsCount = changeRedefineGroup(processedBaseName, componentType, newName, child, currSchema);
/* 3926 */       if (attGroupRefsCount > 1) {
/* 3927 */         reportSchemaError("src-redefine.7.1", new Object[] { new Integer(attGroupRefsCount) }, child);
/*      */       }
/* 3929 */       else if (attGroupRefsCount != 1)
/*      */       {
/* 3933 */         if (currSchema.fTargetNamespace == null)
/* 3934 */           this.fRedefinedRestrictedAttributeGroupRegistry.put(processedBaseName, "," + newName);
/*      */         else
/* 3936 */           this.fRedefinedRestrictedAttributeGroupRegistry.put(processedBaseName, currSchema.fTargetNamespace + "," + newName);
/*      */       }
/* 3938 */     } else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
/* 3939 */       String processedBaseName = currSchema.fTargetNamespace + "," + oldName;
/*      */ 
/* 3941 */       int groupRefsCount = changeRedefineGroup(processedBaseName, componentType, newName, child, currSchema);
/* 3942 */       if (groupRefsCount > 1) {
/* 3943 */         reportSchemaError("src-redefine.6.1.1", new Object[] { new Integer(groupRefsCount) }, child);
/*      */       }
/* 3945 */       else if (groupRefsCount != 1)
/*      */       {
/* 3949 */         if (currSchema.fTargetNamespace == null)
/* 3950 */           this.fRedefinedRestrictedGroupRegistry.put(processedBaseName, "," + newName);
/*      */         else
/* 3952 */           this.fRedefinedRestrictedGroupRegistry.put(processedBaseName, currSchema.fTargetNamespace + "," + newName);
/*      */       }
/*      */     }
/*      */     else {
/* 3956 */       reportSchemaError("Internal-Error", new Object[] { "could not handle this particular <redefine>; please submit your schemas and instance document in a bug report!" }, child);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String findQName(String name, XSDocumentInfo schemaDoc)
/*      */   {
/* 3974 */     SchemaNamespaceSupport currNSMap = schemaDoc.fNamespaceSupport;
/* 3975 */     int colonPtr = name.indexOf(':');
/* 3976 */     String prefix = XMLSymbols.EMPTY_STRING;
/* 3977 */     if (colonPtr > 0)
/* 3978 */       prefix = name.substring(0, colonPtr);
/* 3979 */     String uri = currNSMap.getURI(this.fSymbolTable.addSymbol(prefix));
/* 3980 */     String localpart = colonPtr == 0 ? name : name.substring(colonPtr + 1);
/* 3981 */     if ((prefix == XMLSymbols.EMPTY_STRING) && (uri == null) && (schemaDoc.fIsChameleonSchema))
/* 3982 */       uri = schemaDoc.fTargetNamespace;
/* 3983 */     if (uri == null)
/* 3984 */       return "," + localpart;
/* 3985 */     return uri + "," + localpart;
/*      */   }
/*      */ 
/*      */   private int changeRedefineGroup(String originalQName, String elementSought, String newName, Element curr, XSDocumentInfo schemaDoc)
/*      */   {
/* 3997 */     int result = 0;
/* 3998 */     for (Element child = DOMUtil.getFirstChildElement(curr); 
/* 3999 */       child != null; child = DOMUtil.getNextSiblingElement(child)) {
/* 4000 */       String name = DOMUtil.getLocalName(child);
/* 4001 */       if (!name.equals(elementSought)) {
/* 4002 */         result += changeRedefineGroup(originalQName, elementSought, newName, child, schemaDoc);
/*      */       } else {
/* 4004 */         String ref = child.getAttribute(SchemaSymbols.ATT_REF);
/* 4005 */         if (ref.length() != 0) {
/* 4006 */           String processedRef = findQName(ref, schemaDoc);
/* 4007 */           if (originalQName.equals(processedRef)) {
/* 4008 */             String prefix = XMLSymbols.EMPTY_STRING;
/* 4009 */             int colonptr = ref.indexOf(":");
/* 4010 */             if (colonptr > 0) {
/* 4011 */               prefix = ref.substring(0, colonptr);
/* 4012 */               child.setAttribute(SchemaSymbols.ATT_REF, prefix + ":" + newName);
/*      */             }
/*      */             else {
/* 4015 */               child.setAttribute(SchemaSymbols.ATT_REF, newName);
/* 4016 */             }result++;
/* 4017 */             if (elementSought.equals(SchemaSymbols.ELT_GROUP)) {
/* 4018 */               String minOccurs = child.getAttribute(SchemaSymbols.ATT_MINOCCURS);
/* 4019 */               String maxOccurs = child.getAttribute(SchemaSymbols.ATT_MAXOCCURS);
/* 4020 */               if (((maxOccurs.length() != 0) && (!maxOccurs.equals("1"))) || ((minOccurs.length() != 0) && (!minOccurs.equals("1"))))
/*      */               {
/* 4022 */                 reportSchemaError("src-redefine.6.1.2", new Object[] { ref }, child);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 4029 */     return result;
/*      */   }
/*      */ 
/*      */   private XSDocumentInfo findXSDocumentForDecl(XSDocumentInfo currSchema, Element decl, XSDocumentInfo decl_Doc)
/*      */   {
/* 4046 */     Object temp = decl_Doc;
/* 4047 */     if (temp == null)
/*      */     {
/* 4049 */       return null;
/*      */     }
/* 4051 */     XSDocumentInfo declDocInfo = (XSDocumentInfo)temp;
/* 4052 */     return declDocInfo;
/*      */   }
/*      */ 
/*      */   private boolean nonAnnotationContent(Element elem)
/*      */   {
/* 4070 */     for (Element child = DOMUtil.getFirstChildElement(elem); child != null; child = DOMUtil.getNextSiblingElement(child)) {
/* 4071 */       if (!DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) return true;
/*      */     }
/* 4073 */     return false;
/*      */   }
/*      */ 
/*      */   private void setSchemasVisible(XSDocumentInfo startSchema) {
/* 4077 */     if (DOMUtil.isHidden(startSchema.fSchemaElement, this.fHiddenNodes))
/*      */     {
/* 4079 */       DOMUtil.setVisible(startSchema.fSchemaElement, this.fHiddenNodes);
/* 4080 */       Vector dependingSchemas = (Vector)this.fDependencyMap.get(startSchema);
/* 4081 */       for (int i = 0; i < dependingSchemas.size(); i++)
/* 4082 */         setSchemasVisible((XSDocumentInfo)dependingSchemas.elementAt(i));
/*      */     }
/*      */   }
/*      */ 
/*      */   public SimpleLocator element2Locator(Element e)
/*      */   {
/* 4096 */     if (!(e instanceof ElementImpl)) {
/* 4097 */       return null;
/*      */     }
/* 4099 */     SimpleLocator l = new SimpleLocator();
/* 4100 */     return element2Locator(e, l) ? l : null;
/*      */   }
/*      */ 
/*      */   public boolean element2Locator(Element e, SimpleLocator l)
/*      */   {
/* 4109 */     if (l == null)
/* 4110 */       return false;
/* 4111 */     if ((e instanceof ElementImpl)) {
/* 4112 */       ElementImpl ele = (ElementImpl)e;
/*      */ 
/* 4114 */       Document doc = ele.getOwnerDocument();
/* 4115 */       String sid = (String)this.fDoc2SystemId.get(DOMUtil.getRoot(doc));
/*      */ 
/* 4117 */       int line = ele.getLineNumber();
/* 4118 */       int column = ele.getColumnNumber();
/* 4119 */       l.setValues(sid, sid, line, column, ele.getCharacterOffset());
/* 4120 */       return true;
/*      */     }
/* 4122 */     return false;
/*      */   }
/*      */ 
/*      */   private Element getElementFromMap(Map<String, Element> registry, String declKey) {
/* 4126 */     if (registry == null) return null;
/* 4127 */     return (Element)registry.get(declKey);
/*      */   }
/*      */ 
/*      */   private XSDocumentInfo getDocInfoFromMap(Map<String, XSDocumentInfo> registry, String declKey) {
/* 4131 */     if (registry == null) return null;
/* 4132 */     return (XSDocumentInfo)registry.get(declKey);
/*      */   }
/*      */ 
/*      */   private Object getFromMap(Map registry, String key) {
/* 4136 */     if (registry == null) return null;
/* 4137 */     return registry.get(key);
/*      */   }
/*      */ 
/*      */   void reportSchemaFatalError(String key, Object[] args, Element ele) {
/* 4141 */     reportSchemaErr(key, args, ele, (short)2, null);
/*      */   }
/*      */ 
/*      */   void reportSchemaError(String key, Object[] args, Element ele) {
/* 4145 */     reportSchemaErr(key, args, ele, (short)1, null);
/*      */   }
/*      */ 
/*      */   void reportSchemaError(String key, Object[] args, Element ele, Exception exception) {
/* 4149 */     reportSchemaErr(key, args, ele, (short)1, exception);
/*      */   }
/*      */ 
/*      */   void reportSchemaWarning(String key, Object[] args, Element ele) {
/* 4153 */     reportSchemaErr(key, args, ele, (short)0, null);
/*      */   }
/*      */ 
/*      */   void reportSchemaWarning(String key, Object[] args, Element ele, Exception exception) {
/* 4157 */     reportSchemaErr(key, args, ele, (short)0, exception);
/*      */   }
/*      */ 
/*      */   void reportSchemaErr(String key, Object[] args, Element ele, short type, Exception exception) {
/* 4161 */     if (element2Locator(ele, this.xl)) {
/* 4162 */       this.fErrorReporter.reportError(this.xl, "http://www.w3.org/TR/xml-schema-1", key, args, type, exception);
/*      */     }
/*      */     else
/*      */     {
/* 4166 */       this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", key, args, type, exception);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setGenerateSyntheticAnnotations(boolean state)
/*      */   {
/* 4339 */     this.fSchemaParser.setFeature("http://apache.org/xml/features/generate-synthetic-annotations", state);
/*      */   }
/*      */ 
/*      */   private static final class SAX2XNIUtil extends ErrorHandlerWrapper
/*      */   {
/*      */     public static XMLParseException createXMLParseException0(SAXParseException exception)
/*      */     {
/* 4328 */       return createXMLParseException(exception);
/*      */     }
/*      */     public static XNIException createXNIException0(SAXException exception) {
/* 4331 */       return createXNIException(exception);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class XSAnnotationGrammarPool
/*      */     implements XMLGrammarPool
/*      */   {
/*      */     private XSGrammarBucket fGrammarBucket;
/*      */     private Grammar[] fInitialGrammarSet;
/*      */ 
/*      */     public Grammar[] retrieveInitialGrammarSet(String grammarType)
/*      */     {
/* 4183 */       if (grammarType == "http://www.w3.org/2001/XMLSchema") {
/* 4184 */         if (this.fInitialGrammarSet == null) {
/* 4185 */           if (this.fGrammarBucket == null) {
/* 4186 */             this.fInitialGrammarSet = new Grammar[] { SchemaGrammar.Schema4Annotations.INSTANCE };
/*      */           }
/*      */           else {
/* 4189 */             SchemaGrammar[] schemaGrammars = this.fGrammarBucket.getGrammars();
/*      */ 
/* 4195 */             for (int i = 0; i < schemaGrammars.length; i++) {
/* 4196 */               if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(schemaGrammars[i].getTargetNamespace())) {
/* 4197 */                 this.fInitialGrammarSet = schemaGrammars;
/* 4198 */                 return this.fInitialGrammarSet;
/*      */               }
/*      */             }
/* 4201 */             Grammar[] grammars = new Grammar[schemaGrammars.length + 1];
/* 4202 */             System.arraycopy(schemaGrammars, 0, grammars, 0, schemaGrammars.length);
/* 4203 */             grammars[(grammars.length - 1)] = SchemaGrammar.Schema4Annotations.INSTANCE;
/* 4204 */             this.fInitialGrammarSet = grammars;
/*      */           }
/*      */         }
/* 4207 */         return this.fInitialGrammarSet;
/*      */       }
/* 4209 */       return new Grammar[0];
/*      */     }
/*      */ 
/*      */     public void cacheGrammars(String grammarType, Grammar[] grammars)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Grammar retrieveGrammar(XMLGrammarDescription desc) {
/* 4217 */       if (desc.getGrammarType() == "http://www.w3.org/2001/XMLSchema") {
/* 4218 */         String tns = ((XMLSchemaDescription)desc).getTargetNamespace();
/* 4219 */         if (this.fGrammarBucket != null) {
/* 4220 */           Grammar grammar = this.fGrammarBucket.getGrammar(tns);
/* 4221 */           if (grammar != null) {
/* 4222 */             return grammar;
/*      */           }
/*      */         }
/* 4225 */         if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(tns)) {
/* 4226 */           return SchemaGrammar.Schema4Annotations.INSTANCE;
/*      */         }
/*      */       }
/* 4229 */       return null;
/*      */     }
/*      */ 
/*      */     public void refreshGrammars(XSGrammarBucket gBucket) {
/* 4233 */       this.fGrammarBucket = gBucket;
/* 4234 */       this.fInitialGrammarSet = null;
/*      */     }
/*      */ 
/*      */     public void lockPool()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void unlockPool()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void clear()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class XSDKey
/*      */   {
/*      */     String systemId;
/*      */     short referType;
/*      */     String referNS;
/*      */ 
/*      */     XSDKey(String systemId, short referType, String referNS)
/*      */     {
/* 4289 */       this.systemId = systemId;
/* 4290 */       this.referType = referType;
/* 4291 */       this.referNS = referNS;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 4297 */       return this.referNS == null ? 0 : this.referNS.hashCode();
/*      */     }
/*      */ 
/*      */     public boolean equals(Object obj) {
/* 4301 */       if (!(obj instanceof XSDKey)) {
/* 4302 */         return false;
/*      */       }
/* 4304 */       XSDKey key = (XSDKey)obj;
/*      */ 
/* 4314 */       if (this.referNS != key.referNS) {
/* 4315 */         return false;
/*      */       }
/*      */ 
/* 4318 */       if ((this.systemId == null) || (!this.systemId.equals(key.systemId))) {
/* 4319 */         return false;
/*      */       }
/*      */ 
/* 4322 */       return true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDHandler
 * JD-Core Version:    0.6.2
 */