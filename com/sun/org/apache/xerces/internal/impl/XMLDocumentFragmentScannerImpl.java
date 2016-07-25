/*      */ package com.sun.org.apache.xerces.internal.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLAttributesIteratorImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.Limit;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.Property;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.xml.internal.stream.Entity.ScannedEntity;
/*      */ import com.sun.xml.internal.stream.XMLBufferListener;
/*      */ import com.sun.xml.internal.stream.XMLEntityStorage;
/*      */ import com.sun.xml.internal.stream.dtd.DTDGrammarUtil;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ 
/*      */ public class XMLDocumentFragmentScannerImpl extends XMLScanner
/*      */   implements XMLDocumentScanner, XMLComponent, XMLEntityHandler, XMLBufferListener
/*      */ {
/*      */   protected int fElementAttributeLimit;
/*      */   protected ExternalSubsetResolver fExternalSubsetResolver;
/*      */   protected static final int SCANNER_STATE_START_OF_MARKUP = 21;
/*      */   protected static final int SCANNER_STATE_CONTENT = 22;
/*      */   protected static final int SCANNER_STATE_PI = 23;
/*      */   protected static final int SCANNER_STATE_DOCTYPE = 24;
/*      */   protected static final int SCANNER_STATE_XML_DECL = 25;
/*      */   protected static final int SCANNER_STATE_ROOT_ELEMENT = 26;
/*      */   protected static final int SCANNER_STATE_COMMENT = 27;
/*      */   protected static final int SCANNER_STATE_REFERENCE = 28;
/*      */   protected static final int SCANNER_STATE_ATTRIBUTE = 29;
/*      */   protected static final int SCANNER_STATE_ATTRIBUTE_VALUE = 30;
/*      */   protected static final int SCANNER_STATE_END_OF_INPUT = 33;
/*      */   protected static final int SCANNER_STATE_TERMINATED = 34;
/*      */   protected static final int SCANNER_STATE_CDATA = 35;
/*      */   protected static final int SCANNER_STATE_TEXT_DECL = 36;
/*      */   protected static final int SCANNER_STATE_CHARACTER_DATA = 37;
/*      */   protected static final int SCANNER_STATE_START_ELEMENT_TAG = 38;
/*      */   protected static final int SCANNER_STATE_END_ELEMENT_TAG = 39;
/*      */   protected static final int SCANNER_STATE_CHAR_REFERENCE = 40;
/*      */   protected static final int SCANNER_STATE_BUILT_IN_REFS = 41;
/*      */   protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
/*      */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   protected static final String STANDARD_URI_CONFORMANT = "http://apache.org/xml/features/standard-uri-conformant";
/*      */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*      */   static final String EXTERNAL_ACCESS_DEFAULT = "all";
/*  184 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", "http://apache.org/xml/features/scanner/notify-builtin-refs", "http://apache.org/xml/features/scanner/notify-char-refs", "report-cdata-event" };
/*      */ 
/*  193 */   private static final Boolean[] FEATURE_DEFAULTS = { Boolean.TRUE, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE };
/*      */ 
/*  202 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" };
/*      */ 
/*  210 */   private static final Object[] PROPERTY_DEFAULTS = { null, null, null, "all" };
/*      */ 
/*  217 */   private static final char[] cdata = { '[', 'C', 'D', 'A', 'T', 'A', '[' };
/*  218 */   static final char[] xmlDecl = { '<', '?', 'x', 'm', 'l' };
/*  219 */   private static final char[] endTag = { '<', '/' };
/*      */   private static final boolean DEBUG_SCANNER_STATE = false;
/*      */   private static final boolean DEBUG_DISPATCHER = false;
/*      */   protected static final boolean DEBUG_START_END_ELEMENT = false;
/*      */   protected static final boolean DEBUG_NEXT = false;
/*      */   protected static final boolean DEBUG = false;
/*      */   protected static final boolean DEBUG_COALESCE = false;
/*      */   protected XMLDocumentHandler fDocumentHandler;
/*      */   protected int fScannerLastState;
/*      */   protected XMLEntityStorage fEntityStore;
/*  252 */   protected int[] fEntityStack = new int[4];
/*      */   protected int fMarkupDepth;
/*      */   protected boolean fEmptyElement;
/*  262 */   protected boolean fReadingAttributes = false;
/*      */   protected int fScannerState;
/*  268 */   protected boolean fInScanContent = false;
/*  269 */   protected boolean fLastSectionWasCData = false;
/*  270 */   protected boolean fLastSectionWasEntityReference = false;
/*  271 */   protected boolean fLastSectionWasCharacterData = false;
/*      */   protected boolean fHasExternalDTD;
/*      */   protected boolean fStandaloneSet;
/*      */   protected boolean fStandalone;
/*      */   protected String fVersion;
/*      */   protected QName fCurrentElement;
/*  287 */   protected ElementStack fElementStack = new ElementStack();
/*  288 */   protected ElementStack2 fElementStack2 = new ElementStack2();
/*      */   protected String fPITarget;
/*  300 */   protected XMLString fPIData = new XMLString();
/*      */ 
/*  306 */   protected boolean fNotifyBuiltInRefs = false;
/*      */ 
/*  310 */   protected boolean fSupportDTD = true;
/*  311 */   protected boolean fReplaceEntityReferences = true;
/*  312 */   protected boolean fSupportExternalEntities = false;
/*  313 */   protected boolean fReportCdataEvent = false;
/*  314 */   protected boolean fIsCoalesce = false;
/*  315 */   protected String fDeclaredEncoding = null;
/*      */ 
/*  317 */   protected boolean fDisallowDoctype = false;
/*      */ 
/*  323 */   protected String fAccessExternalDTD = "all";
/*      */   protected boolean fStrictURI;
/*      */   protected Driver fDriver;
/*  337 */   protected Driver fContentDriver = createContentDriver();
/*      */ 
/*  342 */   protected QName fElementQName = new QName();
/*      */ 
/*  345 */   protected QName fAttributeQName = new QName();
/*      */ 
/*  352 */   protected XMLAttributesIteratorImpl fAttributes = new XMLAttributesIteratorImpl();
/*      */ 
/*  356 */   protected XMLString fTempString = new XMLString();
/*      */ 
/*  359 */   protected XMLString fTempString2 = new XMLString();
/*      */ 
/*  362 */   private String[] fStrings = new String[3];
/*      */ 
/*  365 */   protected XMLStringBuffer fStringBuffer = new XMLStringBuffer();
/*      */ 
/*  368 */   protected XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
/*      */ 
/*  372 */   protected XMLStringBuffer fContentBuffer = new XMLStringBuffer();
/*      */ 
/*  375 */   private final char[] fSingleChar = new char[1];
/*  376 */   private String fCurrentEntityName = null;
/*      */ 
/*  379 */   protected boolean fScanToEnd = false;
/*      */ 
/*  381 */   protected DTDGrammarUtil dtdGrammarUtil = null;
/*      */ 
/*  383 */   protected boolean fAddDefaultAttr = false;
/*      */ 
/*  385 */   protected boolean foundBuiltInRefs = false;
/*      */   static final short MAX_DEPTH_LIMIT = 5;
/*      */   static final short ELEMENT_ARRAY_LENGTH = 200;
/*      */   static final short MAX_POINTER_AT_A_DEPTH = 4;
/*      */   static final boolean DEBUG_SKIP_ALGORITHM = false;
/*  394 */   String[] fElementArray = new String['È'];
/*      */ 
/*  396 */   short fLastPointerLocation = 0;
/*  397 */   short fElementPointer = 0;
/*      */ 
/*  399 */   short[][] fPointerInfo = new short[5][4];
/*      */   protected String fElementRawname;
/*  401 */   protected boolean fShouldSkip = false;
/*  402 */   protected boolean fAdd = false;
/*  403 */   protected boolean fSkip = false;
/*      */ 
/*  406 */   private Augmentations fTempAugmentations = null;
/*      */   protected boolean fUsebuffer;
/*      */ 
/*      */   public void setInputSource(XMLInputSource inputSource)
/*      */     throws IOException
/*      */   {
/*  427 */     this.fEntityManager.setEntityHandler(this);
/*  428 */     this.fEntityManager.startEntity("$fragment$", inputSource, false, true);
/*      */   }
/*      */ 
/*      */   public boolean scanDocument(boolean complete)
/*      */     throws IOException, XNIException
/*      */   {
/*  449 */     this.fEntityManager.setEntityHandler(this);
/*      */ 
/*  452 */     int event = next();
/*      */     do {
/*  454 */       switch (event)
/*      */       {
/*      */       case 7:
/*  457 */         break;
/*      */       case 1:
/*  461 */         break;
/*      */       case 4:
/*  463 */         this.fDocumentHandler.characters(getCharacterData(), null);
/*  464 */         break;
/*      */       case 6:
/*  469 */         break;
/*      */       case 9:
/*  472 */         break;
/*      */       case 3:
/*  474 */         this.fDocumentHandler.processingInstruction(getPITarget(), getPIData(), null);
/*  475 */         break;
/*      */       case 5:
/*  478 */         this.fDocumentHandler.comment(getCharacterData(), null);
/*  479 */         break;
/*      */       case 11:
/*  484 */         break;
/*      */       case 12:
/*  486 */         this.fDocumentHandler.startCDATA(null);
/*      */ 
/*  488 */         this.fDocumentHandler.characters(getCharacterData(), null);
/*  489 */         this.fDocumentHandler.endCDATA(null);
/*      */ 
/*  491 */         break;
/*      */       case 14:
/*  493 */         break;
/*      */       case 15:
/*  495 */         break;
/*      */       case 13:
/*  497 */         break;
/*      */       case 10:
/*  499 */         break;
/*      */       case 2:
/*  504 */         break;
/*      */       case 8:
/*      */       default:
/*  506 */         throw new InternalError("processing event: " + event);
/*      */       }
/*      */ 
/*  510 */       event = next();
/*      */     }
/*  512 */     while ((event != 8) && (complete));
/*      */ 
/*  514 */     if (event == 8) {
/*  515 */       this.fDocumentHandler.endDocument(null);
/*  516 */       return false;
/*      */     }
/*      */ 
/*  519 */     return true;
/*      */   }
/*      */ 
/*      */   public QName getElementQName()
/*      */   {
/*  526 */     if (this.fScannerLastState == 2) {
/*  527 */       this.fElementQName.setValues(this.fElementStack.getLastPoppedElement());
/*      */     }
/*  529 */     return this.fElementQName;
/*      */   }
/*      */ 
/*      */   public int next()
/*      */     throws IOException, XNIException
/*      */   {
/*  537 */     return this.fDriver.next();
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/*  562 */     super.reset(componentManager);
/*      */ 
/*  571 */     this.fReportCdataEvent = componentManager.getFeature("report-cdata-event", true);
/*  572 */     this.fSecurityManager = ((XMLSecurityManager)componentManager.getProperty("http://apache.org/xml/properties/security-manager", null));
/*  573 */     this.fNotifyBuiltInRefs = componentManager.getFeature("http://apache.org/xml/features/scanner/notify-builtin-refs", false);
/*      */ 
/*  575 */     Object resolver = componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver", null);
/*  576 */     this.fExternalSubsetResolver = ((resolver instanceof ExternalSubsetResolver) ? (ExternalSubsetResolver)resolver : null);
/*      */ 
/*  580 */     this.fReadingAttributes = false;
/*      */ 
/*  583 */     this.fSupportExternalEntities = true;
/*  584 */     this.fReplaceEntityReferences = true;
/*  585 */     this.fIsCoalesce = false;
/*      */ 
/*  588 */     setScannerState(22);
/*  589 */     setDriver(this.fContentDriver);
/*      */ 
/*  592 */     XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", null);
/*      */ 
/*  594 */     this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */ 
/*  596 */     this.fStrictURI = componentManager.getFeature("http://apache.org/xml/features/standard-uri-conformant", false);
/*      */ 
/*  598 */     resetCommon();
/*      */   }
/*      */ 
/*      */   public void reset(PropertyManager propertyManager)
/*      */   {
/*  605 */     super.reset(propertyManager);
/*      */ 
/*  609 */     this.fNamespaces = ((Boolean)propertyManager.getProperty("javax.xml.stream.isNamespaceAware")).booleanValue();
/*  610 */     this.fNotifyBuiltInRefs = false;
/*      */ 
/*  615 */     Boolean bo = (Boolean)propertyManager.getProperty("javax.xml.stream.isReplacingEntityReferences");
/*  616 */     this.fReplaceEntityReferences = bo.booleanValue();
/*  617 */     bo = (Boolean)propertyManager.getProperty("javax.xml.stream.isSupportingExternalEntities");
/*  618 */     this.fSupportExternalEntities = bo.booleanValue();
/*  619 */     Boolean cdata = (Boolean)propertyManager.getProperty("http://java.sun.com/xml/stream/properties/report-cdata-event");
/*  620 */     if (cdata != null)
/*  621 */       this.fReportCdataEvent = cdata.booleanValue();
/*  622 */     Boolean coalesce = (Boolean)propertyManager.getProperty("javax.xml.stream.isCoalescing");
/*  623 */     if (coalesce != null)
/*  624 */       this.fIsCoalesce = coalesce.booleanValue();
/*  625 */     this.fReportCdataEvent = (!this.fIsCoalesce);
/*      */ 
/*  628 */     this.fReplaceEntityReferences = (this.fIsCoalesce ? true : this.fReplaceEntityReferences);
/*      */ 
/*  636 */     XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)propertyManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
/*      */ 
/*  638 */     this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */ 
/*  640 */     this.fSecurityManager = ((XMLSecurityManager)propertyManager.getProperty("http://apache.org/xml/properties/security-manager"));
/*  641 */     resetCommon();
/*      */   }
/*      */ 
/*      */   void resetCommon()
/*      */   {
/*  646 */     this.fMarkupDepth = 0;
/*  647 */     this.fCurrentElement = null;
/*  648 */     this.fElementStack.clear();
/*  649 */     this.fHasExternalDTD = false;
/*  650 */     this.fStandaloneSet = false;
/*  651 */     this.fStandalone = false;
/*  652 */     this.fInScanContent = false;
/*      */ 
/*  654 */     this.fShouldSkip = false;
/*  655 */     this.fAdd = false;
/*  656 */     this.fSkip = false;
/*      */ 
/*  658 */     this.fEntityStore = this.fEntityManager.getEntityStore();
/*  659 */     this.dtdGrammarUtil = null;
/*      */ 
/*  661 */     if (this.fSecurityManager != null)
/*  662 */       this.fElementAttributeLimit = this.fSecurityManager.getLimit(XMLSecurityManager.Limit.ELEMENT_ATTRIBUTE_LIMIT);
/*      */     else {
/*  664 */       this.fElementAttributeLimit = 0;
/*      */     }
/*  666 */     this.fLimitAnalyzer = new XMLLimitAnalyzer();
/*  667 */     this.fEntityManager.setLimitAnalyzer(this.fLimitAnalyzer);
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/*  676 */     return (String[])RECOGNIZED_FEATURES.clone();
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  697 */     super.setFeature(featureId, state);
/*      */ 
/*  700 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/*  701 */       String feature = featureId.substring("http://apache.org/xml/features/".length());
/*  702 */       if (feature.equals("scanner/notify-builtin-refs"))
/*  703 */         this.fNotifyBuiltInRefs = state;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/*  715 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  736 */     super.setProperty(propertyId, value);
/*      */ 
/*  739 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/*  740 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*  741 */       if ((suffixLength == "internal/entity-manager".length()) && (propertyId.endsWith("internal/entity-manager")))
/*      */       {
/*  743 */         this.fEntityManager = ((XMLEntityManager)value);
/*  744 */         return;
/*      */       }
/*  746 */       if ((suffixLength == "internal/entity-resolver".length()) && (propertyId.endsWith("internal/entity-resolver")))
/*      */       {
/*  748 */         this.fExternalSubsetResolver = ((value instanceof ExternalSubsetResolver) ? (ExternalSubsetResolver)value : null);
/*      */ 
/*  750 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  756 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/*  757 */       String property = propertyId.substring("http://apache.org/xml/properties/".length());
/*  758 */       if (property.equals("internal/entity-manager")) {
/*  759 */         this.fEntityManager = ((XMLEntityManager)value);
/*      */       }
/*  761 */       return;
/*      */     }
/*      */ 
/*  765 */     if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"))
/*      */     {
/*  767 */       XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)value;
/*  768 */       this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/*  783 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/*  784 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/*  785 */         return FEATURE_DEFAULTS[i];
/*      */       }
/*      */     }
/*  788 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/*  801 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/*  802 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/*  803 */         return PROPERTY_DEFAULTS[i];
/*      */       }
/*      */     }
/*  806 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*      */   {
/*  819 */     this.fDocumentHandler = documentHandler;
/*      */   }
/*      */ 
/*      */   public XMLDocumentHandler getDocumentHandler()
/*      */   {
/*  826 */     return this.fDocumentHandler;
/*      */   }
/*      */ 
/*      */   public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  854 */     if (this.fEntityDepth == this.fEntityStack.length) {
/*  855 */       int[] entityarray = new int[this.fEntityStack.length * 2];
/*  856 */       System.arraycopy(this.fEntityStack, 0, entityarray, 0, this.fEntityStack.length);
/*  857 */       this.fEntityStack = entityarray;
/*      */     }
/*  859 */     this.fEntityStack[this.fEntityDepth] = this.fMarkupDepth;
/*      */ 
/*  861 */     super.startEntity(name, identifier, encoding, augs);
/*      */ 
/*  864 */     if ((this.fStandalone) && (this.fEntityStore.isEntityDeclInExternalSubset(name))) {
/*  865 */       reportFatalError("MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { name });
/*      */     }
/*      */ 
/*  871 */     if ((this.fDocumentHandler != null) && (!this.fScanningAttribute) && 
/*  872 */       (!name.equals("[xml]")))
/*  873 */       this.fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
/*      */   }
/*      */ 
/*      */   public void endEntity(String name, Augmentations augs)
/*      */     throws IOException, XNIException
/*      */   {
/*  899 */     super.endEntity(name, augs);
/*      */ 
/*  902 */     if (this.fMarkupDepth != this.fEntityStack[this.fEntityDepth]) {
/*  903 */       reportFatalError("MarkupEntityMismatch", null);
/*      */     }
/*      */ 
/*  908 */     if ((this.fDocumentHandler != null) && (!this.fScanningAttribute) && 
/*  909 */       (!name.equals("[xml]")))
/*  910 */       this.fDocumentHandler.endGeneralEntity(name, augs);
/*      */   }
/*      */ 
/*      */   protected Driver createContentDriver()
/*      */   {
/*  925 */     return new FragmentContentDriver();
/*      */   }
/*      */ 
/*      */   protected void scanXMLDeclOrTextDecl(boolean scanningTextDecl)
/*      */     throws IOException, XNIException
/*      */   {
/*  952 */     super.scanXMLDeclOrTextDecl(scanningTextDecl, this.fStrings);
/*  953 */     this.fMarkupDepth -= 1;
/*      */ 
/*  956 */     String version = this.fStrings[0];
/*  957 */     String encoding = this.fStrings[1];
/*  958 */     String standalone = this.fStrings[2];
/*  959 */     this.fDeclaredEncoding = encoding;
/*      */ 
/*  961 */     this.fStandaloneSet = (standalone != null);
/*  962 */     this.fStandalone = ((this.fStandaloneSet) && (standalone.equals("yes")));
/*      */ 
/*  965 */     this.fEntityManager.setStandalone(this.fStandalone);
/*      */ 
/*  969 */     if (this.fDocumentHandler != null) {
/*  970 */       if (scanningTextDecl)
/*  971 */         this.fDocumentHandler.textDecl(version, encoding, null);
/*      */       else {
/*  973 */         this.fDocumentHandler.xmlDecl(version, encoding, standalone, null);
/*      */       }
/*      */     }
/*      */ 
/*  977 */     if (version != null) {
/*  978 */       this.fEntityScanner.setVersion(version);
/*  979 */       this.fEntityScanner.setXMLVersion(version);
/*      */     }
/*      */ 
/*  982 */     if ((encoding != null) && (!this.fEntityScanner.getCurrentEntity().isEncodingExternallySpecified()))
/*  983 */       this.fEntityScanner.setEncoding(encoding);
/*      */   }
/*      */ 
/*      */   public String getPITarget()
/*      */   {
/*  989 */     return this.fPITarget;
/*      */   }
/*      */ 
/*      */   public XMLStringBuffer getPIData() {
/*  993 */     return this.fContentBuffer;
/*      */   }
/*      */ 
/*      */   public XMLString getCharacterData()
/*      */   {
/*  998 */     if (this.fUsebuffer) {
/*  999 */       return this.fContentBuffer;
/*      */     }
/* 1001 */     return this.fTempString;
/*      */   }
/*      */ 
/*      */   protected void scanPIData(String target, XMLStringBuffer data)
/*      */     throws IOException, XNIException
/*      */   {
/* 1018 */     super.scanPIData(target, data);
/*      */ 
/* 1021 */     this.fPITarget = target;
/*      */ 
/* 1023 */     this.fMarkupDepth -= 1;
/*      */   }
/*      */ 
/*      */   protected void scanComment()
/*      */     throws IOException, XNIException
/*      */   {
/* 1037 */     this.fContentBuffer.clear();
/* 1038 */     scanComment(this.fContentBuffer);
/*      */ 
/* 1040 */     this.fUsebuffer = true;
/* 1041 */     this.fMarkupDepth -= 1;
/*      */   }
/*      */ 
/*      */   public String getComment()
/*      */   {
/* 1047 */     return this.fContentBuffer.toString();
/*      */   }
/*      */ 
/*      */   void addElement(String rawname) {
/* 1051 */     if (this.fElementPointer < 200)
/*      */     {
/* 1053 */       this.fElementArray[this.fElementPointer] = rawname;
/*      */ 
/* 1066 */       if (this.fElementStack.fDepth < 5) {
/* 1067 */         short column = storePointerForADepth(this.fElementPointer);
/* 1068 */         if (column > 0) {
/* 1069 */           short pointer = getElementPointer((short)this.fElementStack.fDepth, (short)(column - 1));
/*      */ 
/* 1072 */           if (rawname == this.fElementArray[pointer]) {
/* 1073 */             this.fShouldSkip = true;
/* 1074 */             this.fLastPointerLocation = pointer;
/*      */ 
/* 1076 */             resetPointer((short)this.fElementStack.fDepth, column);
/* 1077 */             this.fElementArray[this.fElementPointer] = null;
/* 1078 */             return;
/*      */           }
/* 1080 */           this.fShouldSkip = false;
/*      */         }
/*      */       }
/*      */ 
/* 1084 */       this.fElementPointer = ((short)(this.fElementPointer + 1));
/*      */     }
/*      */   }
/*      */ 
/*      */   void resetPointer(short depth, short column)
/*      */   {
/* 1090 */     this.fPointerInfo[depth][column] = 0;
/*      */   }
/*      */ 
/*      */   short storePointerForADepth(short elementPointer)
/*      */   {
/* 1095 */     short depth = (short)this.fElementStack.fDepth;
/*      */ 
/* 1099 */     for (short i = 0; i < 4; i = (short)(i + 1))
/*      */     {
/* 1101 */       if (canStore(depth, i)) {
/* 1102 */         this.fPointerInfo[depth][i] = elementPointer;
/*      */ 
/* 1111 */         return i;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1116 */     return -1;
/*      */   }
/*      */ 
/*      */   boolean canStore(short depth, short column)
/*      */   {
/* 1123 */     return this.fPointerInfo[depth][column] == 0;
/*      */   }
/*      */ 
/*      */   short getElementPointer(short depth, short column)
/*      */   {
/* 1131 */     return this.fPointerInfo[depth][column];
/*      */   }
/*      */ 
/*      */   boolean skipFromTheBuffer(String rawname)
/*      */     throws IOException
/*      */   {
/* 1137 */     if (this.fEntityScanner.skipString(rawname)) {
/* 1138 */       char c = (char)this.fEntityScanner.peekChar();
/*      */ 
/* 1141 */       if ((c == ' ') || (c == '/') || (c == '>')) {
/* 1142 */         this.fElementRawname = rawname;
/* 1143 */         return true;
/*      */       }
/* 1145 */       return false;
/*      */     }
/*      */ 
/* 1148 */     return false;
/*      */   }
/*      */ 
/*      */   boolean skipQElement(String rawname) throws IOException
/*      */   {
/* 1153 */     int c = this.fEntityScanner.getChar(rawname.length());
/*      */ 
/* 1155 */     if (XMLChar.isName(c)) {
/* 1156 */       return false;
/*      */     }
/* 1158 */     return this.fEntityScanner.skipString(rawname);
/*      */   }
/*      */ 
/*      */   protected boolean skipElement()
/*      */     throws IOException
/*      */   {
/* 1164 */     if (!this.fShouldSkip) return false;
/*      */ 
/* 1166 */     if (this.fLastPointerLocation != 0)
/*      */     {
/* 1168 */       String rawname = this.fElementArray[(this.fLastPointerLocation + 1)];
/* 1169 */       if ((rawname != null) && (skipFromTheBuffer(rawname))) {
/* 1170 */         this.fLastPointerLocation = ((short)(this.fLastPointerLocation + 1));
/*      */ 
/* 1174 */         return true;
/*      */       }
/*      */ 
/* 1177 */       this.fLastPointerLocation = 0;
/*      */     }
/*      */ 
/* 1185 */     return (this.fShouldSkip) && (skipElement((short)0));
/*      */   }
/*      */ 
/*      */   boolean skipElement(short column)
/*      */     throws IOException
/*      */   {
/* 1191 */     short depth = (short)this.fElementStack.fDepth;
/*      */ 
/* 1193 */     if (depth > 5) {
/* 1194 */       return this.fShouldSkip = 0;
/*      */     }
/* 1196 */     for (short i = column; i < 4; i = (short)(i + 1)) {
/* 1197 */       short pointer = getElementPointer(depth, i);
/*      */ 
/* 1199 */       if (pointer == 0) {
/* 1200 */         return this.fShouldSkip = 0;
/*      */       }
/*      */ 
/* 1203 */       if ((this.fElementArray[pointer] != null) && (skipFromTheBuffer(this.fElementArray[pointer])))
/*      */       {
/* 1209 */         this.fLastPointerLocation = pointer;
/* 1210 */         return this.fShouldSkip = 1;
/*      */       }
/*      */     }
/* 1213 */     return this.fShouldSkip = 0;
/*      */   }
/*      */ 
/*      */   protected boolean scanStartElement()
/*      */     throws IOException, XNIException
/*      */   {
/* 1245 */     if ((this.fSkip) && (!this.fAdd))
/*      */     {
/* 1249 */       QName name = this.fElementStack.getNext();
/*      */ 
/* 1256 */       this.fSkip = this.fEntityScanner.skipString(name.rawname);
/*      */ 
/* 1258 */       if (this.fSkip)
/*      */       {
/* 1262 */         this.fElementStack.push();
/* 1263 */         this.fElementQName = name;
/*      */       }
/*      */       else {
/* 1266 */         this.fElementStack.reposition();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1276 */     if ((!this.fSkip) || (this.fAdd))
/*      */     {
/* 1278 */       this.fElementQName = this.fElementStack.nextElement();
/*      */ 
/* 1280 */       if (this.fNamespaces) {
/* 1281 */         this.fEntityScanner.scanQName(this.fElementQName);
/*      */       } else {
/* 1283 */         String name = this.fEntityScanner.scanName();
/* 1284 */         this.fElementQName.setValues(null, name, name, null);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1297 */     if (this.fAdd)
/*      */     {
/* 1299 */       this.fElementStack.matchElement(this.fElementQName);
/*      */     }
/*      */ 
/* 1304 */     this.fCurrentElement = this.fElementQName;
/*      */ 
/* 1306 */     String rawname = this.fElementQName.rawname;
/*      */ 
/* 1308 */     this.fEmptyElement = false;
/*      */ 
/* 1310 */     this.fAttributes.removeAllAttributes();
/*      */ 
/* 1312 */     checkDepth(rawname);
/* 1313 */     if (!seekCloseOfStartTag()) {
/* 1314 */       this.fReadingAttributes = true;
/* 1315 */       this.fAttributeCacheUsedCount = 0;
/* 1316 */       this.fStringBufferIndex = 0;
/* 1317 */       this.fAddDefaultAttr = true;
/*      */       do {
/* 1319 */         scanAttribute(this.fAttributes);
/* 1320 */         if ((this.fSecurityManager != null) && (!this.fSecurityManager.isNoLimit(this.fElementAttributeLimit)) && (this.fAttributes.getLength() > this.fElementAttributeLimit))
/*      */         {
/* 1322 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ElementAttributeLimit", new Object[] { rawname, Integer.valueOf(this.fElementAttributeLimit) }, (short)2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1328 */       while (!seekCloseOfStartTag());
/* 1329 */       this.fReadingAttributes = false;
/*      */     }
/*      */ 
/* 1332 */     if (this.fEmptyElement)
/*      */     {
/* 1334 */       this.fMarkupDepth -= 1;
/*      */ 
/* 1337 */       if (this.fMarkupDepth < this.fEntityStack[(this.fEntityDepth - 1)]) {
/* 1338 */         reportFatalError("ElementEntityMismatch", new Object[] { this.fCurrentElement.rawname });
/*      */       }
/*      */ 
/* 1342 */       if (this.fDocumentHandler != null) {
/* 1343 */         this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
/*      */       }
/*      */ 
/* 1353 */       this.fElementStack.popElement();
/*      */     }
/*      */     else
/*      */     {
/* 1357 */       if (this.dtdGrammarUtil != null)
/* 1358 */         this.dtdGrammarUtil.startElement(this.fElementQName, this.fAttributes);
/* 1359 */       if (this.fDocumentHandler != null)
/*      */       {
/* 1363 */         this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1369 */     return this.fEmptyElement;
/*      */   }
/*      */ 
/*      */   protected boolean seekCloseOfStartTag()
/*      */     throws IOException, XNIException
/*      */   {
/* 1379 */     boolean sawSpace = this.fEntityScanner.skipSpaces();
/*      */ 
/* 1382 */     int c = this.fEntityScanner.peekChar();
/* 1383 */     if (c == 62) {
/* 1384 */       this.fEntityScanner.scanChar();
/* 1385 */       return true;
/* 1386 */     }if (c == 47) {
/* 1387 */       this.fEntityScanner.scanChar();
/* 1388 */       if (!this.fEntityScanner.skipChar(62)) {
/* 1389 */         reportFatalError("ElementUnterminated", new Object[] { this.fElementQName.rawname });
/*      */       }
/*      */ 
/* 1392 */       this.fEmptyElement = true;
/* 1393 */       return true;
/* 1394 */     }if ((!isValidNameStartChar(c)) || (!sawSpace)) {
/* 1395 */       reportFatalError("ElementUnterminated", new Object[] { this.fElementQName.rawname });
/*      */     }
/*      */ 
/* 1398 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean hasAttributes() {
/* 1402 */     return this.fAttributes.getLength() > 0;
/*      */   }
/*      */ 
/*      */   public XMLAttributesIteratorImpl getAttributeIterator()
/*      */   {
/* 1482 */     if ((this.dtdGrammarUtil != null) && (this.fAddDefaultAttr)) {
/* 1483 */       this.dtdGrammarUtil.addDTDDefaultAttrs(this.fElementQName, this.fAttributes);
/* 1484 */       this.fAddDefaultAttr = false;
/*      */     }
/* 1486 */     return this.fAttributes;
/*      */   }
/*      */ 
/*      */   public boolean standaloneSet()
/*      */   {
/* 1491 */     return this.fStandaloneSet;
/*      */   }
/*      */ 
/*      */   public boolean isStandAlone() {
/* 1495 */     return this.fStandalone;
/*      */   }
/*      */ 
/*      */   protected void scanAttribute(XMLAttributes attributes)
/*      */     throws IOException, XNIException
/*      */   {
/* 1520 */     if (this.fNamespaces) {
/* 1521 */       this.fEntityScanner.scanQName(this.fAttributeQName);
/*      */     } else {
/* 1523 */       String name = this.fEntityScanner.scanName();
/* 1524 */       this.fAttributeQName.setValues(null, name, name, null);
/*      */     }
/*      */ 
/* 1528 */     this.fEntityScanner.skipSpaces();
/* 1529 */     if (!this.fEntityScanner.skipChar(61)) {
/* 1530 */       reportFatalError("EqRequiredInAttribute", new Object[] { this.fCurrentElement.rawname, this.fAttributeQName.rawname });
/*      */     }
/*      */ 
/* 1533 */     this.fEntityScanner.skipSpaces();
/*      */ 
/* 1535 */     int attIndex = 0;
/*      */ 
/* 1537 */     boolean isVC = (this.fHasExternalDTD) && (!this.fStandalone);
/*      */ 
/* 1545 */     XMLString tmpStr = getString();
/*      */ 
/* 1547 */     scanAttributeValue(tmpStr, this.fTempString2, this.fAttributeQName.rawname, attributes, attIndex, isVC);
/*      */ 
/* 1552 */     int oldLen = attributes.getLength();
/*      */ 
/* 1554 */     attIndex = attributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
/*      */ 
/* 1559 */     if (oldLen == attributes.getLength()) {
/* 1560 */       reportFatalError("AttributeNotUnique", new Object[] { this.fCurrentElement.rawname, this.fAttributeQName.rawname });
/*      */     }
/*      */ 
/* 1567 */     attributes.setValue(attIndex, null, tmpStr);
/*      */ 
/* 1571 */     attributes.setSpecified(attIndex, true);
/*      */   }
/*      */ 
/*      */   protected int scanContent(XMLStringBuffer content)
/*      */     throws IOException, XNIException
/*      */   {
/* 1590 */     this.fTempString.length = 0;
/* 1591 */     int c = this.fEntityScanner.scanContent(this.fTempString);
/* 1592 */     content.append(this.fTempString);
/* 1593 */     this.fTempString.length = 0;
/* 1594 */     if (c == 13)
/*      */     {
/* 1597 */       this.fEntityScanner.scanChar();
/* 1598 */       content.append((char)c);
/* 1599 */       c = -1;
/* 1600 */     } else if (c == 93)
/*      */     {
/* 1603 */       content.append((char)this.fEntityScanner.scanChar());
/*      */ 
/* 1607 */       this.fInScanContent = true;
/*      */ 
/* 1612 */       if (this.fEntityScanner.skipChar(93)) {
/* 1613 */         content.append(']');
/* 1614 */         while (this.fEntityScanner.skipChar(93)) {
/* 1615 */           content.append(']');
/*      */         }
/* 1617 */         if (this.fEntityScanner.skipChar(62)) {
/* 1618 */           reportFatalError("CDEndInContent", null);
/*      */         }
/*      */       }
/* 1621 */       this.fInScanContent = false;
/* 1622 */       c = -1;
/*      */     }
/* 1624 */     if ((this.fDocumentHandler != null) && (content.length > 0));
/* 1627 */     return c;
/*      */   }
/*      */ 
/*      */   protected boolean scanCDATASection(XMLStringBuffer contentBuffer, boolean complete)
/*      */     throws IOException, XNIException
/*      */   {
/* 1648 */     if (this.fDocumentHandler != null);
/* 1654 */     while (this.fEntityScanner.scanData("]]>", contentBuffer))
/*      */     {
/* 1683 */       int c = this.fEntityScanner.peekChar();
/* 1684 */       if ((c != -1) && (isInvalidLiteral(c))) {
/* 1685 */         if (XMLChar.isHighSurrogate(c))
/*      */         {
/* 1688 */           scanSurrogates(contentBuffer);
/*      */         } else {
/* 1690 */           reportFatalError("InvalidCharInCDSect", new Object[] { Integer.toString(c, 16) });
/*      */ 
/* 1692 */           this.fEntityScanner.scanChar();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1696 */       if (this.fDocumentHandler == null);
/*      */     }
/*      */ 
/* 1701 */     this.fMarkupDepth -= 1;
/*      */ 
/* 1703 */     if (((this.fDocumentHandler == null) || (contentBuffer.length <= 0)) || 
/* 1708 */       (this.fDocumentHandler != null));
/* 1712 */     return true;
/*      */   }
/*      */ 
/*      */   protected int scanEndElement()
/*      */     throws IOException, XNIException
/*      */   {
/* 1734 */     QName endElementName = this.fElementStack.popElement();
/*      */ 
/* 1736 */     String rawname = endElementName.rawname;
/*      */ 
/* 1748 */     if (!this.fEntityScanner.skipString(endElementName.rawname)) {
/* 1749 */       reportFatalError("ETagRequired", new Object[] { rawname });
/*      */     }
/*      */ 
/* 1753 */     this.fEntityScanner.skipSpaces();
/* 1754 */     if (!this.fEntityScanner.skipChar(62)) {
/* 1755 */       reportFatalError("ETagUnterminated", new Object[] { rawname });
/*      */     }
/*      */ 
/* 1758 */     this.fMarkupDepth -= 1;
/*      */ 
/* 1761 */     this.fMarkupDepth -= 1;
/*      */ 
/* 1764 */     if (this.fMarkupDepth < this.fEntityStack[(this.fEntityDepth - 1)]) {
/* 1765 */       reportFatalError("ElementEntityMismatch", new Object[] { rawname });
/*      */     }
/*      */ 
/* 1777 */     if (this.fDocumentHandler != null)
/*      */     {
/* 1782 */       this.fDocumentHandler.endElement(endElementName, null);
/*      */     }
/* 1784 */     if (this.dtdGrammarUtil != null) {
/* 1785 */       this.dtdGrammarUtil.endElement(endElementName);
/*      */     }
/* 1787 */     return this.fMarkupDepth;
/*      */   }
/*      */ 
/*      */   protected void scanCharReference()
/*      */     throws IOException, XNIException
/*      */   {
/* 1801 */     this.fStringBuffer2.clear();
/* 1802 */     int ch = scanCharReferenceValue(this.fStringBuffer2, null);
/* 1803 */     this.fMarkupDepth -= 1;
/* 1804 */     if (ch != -1)
/*      */     {
/* 1807 */       if (this.fDocumentHandler != null) {
/* 1808 */         if (this.fNotifyCharRefs) {
/* 1809 */           this.fDocumentHandler.startGeneralEntity(this.fCharRefLiteral, null, null, null);
/*      */         }
/* 1811 */         Augmentations augs = null;
/* 1812 */         if ((this.fValidation) && (ch <= 32)) {
/* 1813 */           if (this.fTempAugmentations != null) {
/* 1814 */             this.fTempAugmentations.removeAllItems();
/*      */           }
/*      */           else {
/* 1817 */             this.fTempAugmentations = new AugmentationsImpl();
/*      */           }
/* 1819 */           augs = this.fTempAugmentations;
/* 1820 */           augs.putItem("CHAR_REF_PROBABLE_WS", Boolean.TRUE);
/*      */         }
/*      */ 
/* 1825 */         if (this.fNotifyCharRefs)
/* 1826 */           this.fDocumentHandler.endGeneralEntity(this.fCharRefLiteral, null);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void scanEntityReference(XMLStringBuffer content)
/*      */     throws IOException, XNIException
/*      */   {
/* 1844 */     String name = this.fEntityScanner.scanName();
/* 1845 */     if (name == null) {
/* 1846 */       reportFatalError("NameRequiredInReference", null);
/* 1847 */       return;
/*      */     }
/* 1849 */     if (!this.fEntityScanner.skipChar(59)) {
/* 1850 */       reportFatalError("SemicolonRequiredInReference", new Object[] { name });
/*      */     }
/* 1852 */     if (this.fEntityStore.isUnparsedEntity(name)) {
/* 1853 */       reportFatalError("ReferenceToUnparsedEntity", new Object[] { name });
/*      */     }
/* 1855 */     this.fMarkupDepth -= 1;
/* 1856 */     this.fCurrentEntityName = name;
/*      */ 
/* 1859 */     if (name == fAmpSymbol) {
/* 1860 */       handleCharacter('&', fAmpSymbol, content);
/* 1861 */       this.fScannerState = 41;
/* 1862 */       return;
/* 1863 */     }if (name == fLtSymbol) {
/* 1864 */       handleCharacter('<', fLtSymbol, content);
/* 1865 */       this.fScannerState = 41;
/* 1866 */       return;
/* 1867 */     }if (name == fGtSymbol) {
/* 1868 */       handleCharacter('>', fGtSymbol, content);
/* 1869 */       this.fScannerState = 41;
/* 1870 */       return;
/* 1871 */     }if (name == fQuotSymbol) {
/* 1872 */       handleCharacter('"', fQuotSymbol, content);
/* 1873 */       this.fScannerState = 41;
/* 1874 */       return;
/* 1875 */     }if (name == fAposSymbol) {
/* 1876 */       handleCharacter('\'', fAposSymbol, content);
/* 1877 */       this.fScannerState = 41;
/* 1878 */       return;
/*      */     }
/*      */ 
/* 1884 */     boolean isEE = this.fEntityStore.isExternalEntity(name);
/* 1885 */     if (((isEE) && (!this.fSupportExternalEntities)) || ((!isEE) && (!this.fReplaceEntityReferences)) || (this.foundBuiltInRefs)) {
/* 1886 */       this.fScannerState = 28;
/* 1887 */       return;
/*      */     }
/*      */ 
/* 1890 */     if (!this.fEntityStore.isDeclaredEntity(name))
/*      */     {
/* 1892 */       if ((!this.fSupportDTD) && (this.fReplaceEntityReferences)) {
/* 1893 */         reportFatalError("EntityNotDeclared", new Object[] { name });
/* 1894 */         return;
/*      */       }
/*      */ 
/* 1897 */       if ((this.fHasExternalDTD) && (!this.fStandalone)) {
/* 1898 */         if (this.fValidation)
/* 1899 */           this.fErrorReporter.reportError(this.fEntityScanner, "http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[] { name }, (short)1);
/*      */       }
/*      */       else {
/* 1902 */         reportFatalError("EntityNotDeclared", new Object[] { name });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1908 */     this.fEntityManager.startEntity(name, false);
/*      */   }
/*      */ 
/*      */   void checkDepth(String elementName)
/*      */   {
/* 1921 */     this.fLimitAnalyzer.addValue(XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT, elementName, this.fElementStack.fDepth);
/* 1922 */     if (this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT, this.fLimitAnalyzer)) {
/* 1923 */       this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
/* 1924 */       reportFatalError("MaxElementDepthLimit", new Object[] { elementName, Integer.valueOf(this.fLimitAnalyzer.getTotalValue(XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT)), Integer.valueOf(this.fSecurityManager.getLimit(XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT)), "maxElementDepth" });
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleCharacter(char c, String entity, XMLStringBuffer content)
/*      */     throws XNIException
/*      */   {
/* 1945 */     this.foundBuiltInRefs = true;
/* 1946 */     content.append(c);
/* 1947 */     if (this.fDocumentHandler != null) {
/* 1948 */       this.fSingleChar[0] = c;
/* 1949 */       if (this.fNotifyBuiltInRefs) {
/* 1950 */         this.fDocumentHandler.startGeneralEntity(entity, null, null, null);
/*      */       }
/* 1952 */       this.fTempString.setValues(this.fSingleChar, 0, 1);
/*      */ 
/* 1955 */       if (this.fNotifyBuiltInRefs)
/* 1956 */         this.fDocumentHandler.endGeneralEntity(entity, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void setScannerState(int state)
/*      */   {
/* 1970 */     this.fScannerState = state;
/*      */   }
/*      */ 
/*      */   protected final void setDriver(Driver driver)
/*      */   {
/* 1987 */     this.fDriver = driver;
/*      */   }
/*      */ 
/*      */   protected String getScannerStateName(int state)
/*      */   {
/* 2002 */     switch (state) { case 24:
/* 2003 */       return "SCANNER_STATE_DOCTYPE";
/*      */     case 26:
/* 2004 */       return "SCANNER_STATE_ROOT_ELEMENT";
/*      */     case 21:
/* 2005 */       return "SCANNER_STATE_START_OF_MARKUP";
/*      */     case 27:
/* 2006 */       return "SCANNER_STATE_COMMENT";
/*      */     case 23:
/* 2007 */       return "SCANNER_STATE_PI";
/*      */     case 22:
/* 2008 */       return "SCANNER_STATE_CONTENT";
/*      */     case 28:
/* 2009 */       return "SCANNER_STATE_REFERENCE";
/*      */     case 33:
/* 2010 */       return "SCANNER_STATE_END_OF_INPUT";
/*      */     case 34:
/* 2011 */       return "SCANNER_STATE_TERMINATED";
/*      */     case 35:
/* 2012 */       return "SCANNER_STATE_CDATA";
/*      */     case 36:
/* 2013 */       return "SCANNER_STATE_TEXT_DECL";
/*      */     case 29:
/* 2014 */       return "SCANNER_STATE_ATTRIBUTE";
/*      */     case 30:
/* 2015 */       return "SCANNER_STATE_ATTRIBUTE_VALUE";
/*      */     case 38:
/* 2016 */       return "SCANNER_STATE_START_ELEMENT_TAG";
/*      */     case 39:
/* 2017 */       return "SCANNER_STATE_END_ELEMENT_TAG";
/*      */     case 37:
/* 2018 */       return "SCANNER_STATE_CHARACTER_DATA";
/*      */     case 25:
/*      */     case 31:
/* 2021 */     case 32: } return "??? (" + state + ')';
/*      */   }
/*      */ 
/*      */   public String getEntityName()
/*      */   {
/* 2026 */     return this.fCurrentEntityName;
/*      */   }
/*      */ 
/*      */   public String getDriverName(Driver driver)
/*      */   {
/* 2046 */     return "null";
/*      */   }
/*      */ 
/*      */   String checkAccess(String systemId, String allowedProtocols)
/*      */     throws IOException
/*      */   {
/* 2058 */     String baseSystemId = this.fEntityScanner.getBaseSystemId();
/* 2059 */     String expandedSystemId = XMLEntityManager.expandSystemId(systemId, baseSystemId, this.fStrictURI);
/* 2060 */     return SecuritySupport.checkAccess(expandedSystemId, allowedProtocols, "all");
/*      */   }
/*      */ 
/*      */   static void pr(String str)
/*      */   {
/* 3269 */     System.out.println(str);
/*      */   }
/*      */ 
/*      */   protected XMLString getString()
/*      */   {
/* 3284 */     if ((this.fAttributeCacheUsedCount < this.initialCacheCount) || (this.fAttributeCacheUsedCount < this.attributeValueCache.size())) {
/* 3285 */       return (XMLString)this.attributeValueCache.get(this.fAttributeCacheUsedCount++);
/*      */     }
/* 3287 */     XMLString str = new XMLString();
/* 3288 */     this.fAttributeCacheUsedCount += 1;
/* 3289 */     this.attributeValueCache.add(str);
/* 3290 */     return str;
/*      */   }
/*      */ 
/*      */   public void refresh()
/*      */   {
/* 3299 */     refresh(0);
/*      */   }
/*      */ 
/*      */   public void refresh(int refreshPosition)
/*      */   {
/* 3310 */     if (this.fReadingAttributes) {
/* 3311 */       this.fAttributes.refresh();
/*      */     }
/* 3313 */     if (this.fScannerState == 37)
/*      */     {
/* 3316 */       this.fContentBuffer.append(this.fTempString);
/*      */ 
/* 3318 */       this.fTempString.length = 0;
/* 3319 */       this.fUsebuffer = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static abstract interface Driver
/*      */   {
/*      */     public abstract int next()
/*      */       throws IOException, XNIException;
/*      */   }
/*      */ 
/*      */   protected static final class Element
/*      */   {
/*      */     public QName qname;
/*      */     public char[] fRawname;
/*      */     public Element next;
/*      */ 
/*      */     public Element(QName qname, Element next)
/*      */     {
/* 2094 */       this.qname.setValues(qname);
/* 2095 */       this.fRawname = qname.rawname.toCharArray();
/* 2096 */       this.next = next;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ElementStack
/*      */   {
/*      */     protected QName[] fElements;
/* 2275 */     protected int[] fInt = new int[20];
/*      */     protected int fDepth;
/*      */     protected int fCount;
/*      */     protected int fPosition;
/*      */     protected int fMark;
/*      */     protected int fLastDepth;
/*      */ 
/*      */     public ElementStack()
/*      */     {
/* 2295 */       this.fElements = new QName[20];
/* 2296 */       for (int i = 0; i < this.fElements.length; i++)
/* 2297 */         this.fElements[i] = new QName();
/*      */     }
/*      */ 
/*      */     public QName pushElement(QName element)
/*      */     {
/* 2320 */       if (this.fDepth == this.fElements.length) {
/* 2321 */         QName[] array = new QName[this.fElements.length * 2];
/* 2322 */         System.arraycopy(this.fElements, 0, array, 0, this.fDepth);
/* 2323 */         this.fElements = array;
/* 2324 */         for (int i = this.fDepth; i < this.fElements.length; i++) {
/* 2325 */           this.fElements[i] = new QName();
/*      */         }
/*      */       }
/* 2328 */       this.fElements[this.fDepth].setValues(element);
/* 2329 */       return this.fElements[(this.fDepth++)];
/*      */     }
/*      */ 
/*      */     public QName getNext()
/*      */     {
/* 2339 */       if (this.fPosition == this.fCount) {
/* 2340 */         this.fPosition = this.fMark;
/*      */       }
/*      */ 
/* 2348 */       return this.fElements[this.fPosition];
/*      */     }
/*      */ 
/*      */     public void push()
/*      */     {
/* 2358 */       this.fInt[(++this.fDepth)] = (this.fPosition++);
/*      */     }
/*      */ 
/*      */     public boolean matchElement(QName element)
/*      */     {
/* 2374 */       boolean match = false;
/* 2375 */       if ((this.fLastDepth > this.fDepth) && (this.fDepth <= 3))
/*      */       {
/* 2380 */         if (element.rawname == this.fElements[(this.fDepth - 1)].rawname) {
/* 2381 */           XMLDocumentFragmentScannerImpl.this.fAdd = false;
/*      */ 
/* 2384 */           this.fMark = (this.fDepth - 1);
/*      */ 
/* 2386 */           this.fPosition = this.fMark;
/* 2387 */           match = true;
/*      */ 
/* 2389 */           this.fCount -= 1;
/*      */         }
/*      */         else
/*      */         {
/* 2400 */           XMLDocumentFragmentScannerImpl.this.fAdd = true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2408 */       if (match)
/*      */       {
/* 2410 */         this.fInt[this.fDepth] = (this.fPosition++);
/*      */       }
/*      */       else
/*      */       {
/* 2416 */         this.fInt[this.fDepth] = (this.fCount - 1);
/*      */       }
/*      */ 
/* 2421 */       if (this.fCount == this.fElements.length) {
/* 2422 */         XMLDocumentFragmentScannerImpl.this.fSkip = false;
/* 2423 */         XMLDocumentFragmentScannerImpl.this.fAdd = false;
/*      */ 
/* 2425 */         reposition();
/*      */ 
/* 2432 */         return false;
/*      */       }
/*      */ 
/* 2442 */       this.fLastDepth = this.fDepth;
/* 2443 */       return match;
/*      */     }
/*      */ 
/*      */     public QName nextElement()
/*      */     {
/* 2454 */       if (XMLDocumentFragmentScannerImpl.this.fSkip) {
/* 2455 */         this.fDepth += 1;
/*      */ 
/* 2457 */         return this.fElements[(this.fCount++)];
/* 2458 */       }if (this.fDepth == this.fElements.length) {
/* 2459 */         QName[] array = new QName[this.fElements.length * 2];
/* 2460 */         System.arraycopy(this.fElements, 0, array, 0, this.fDepth);
/* 2461 */         this.fElements = array;
/* 2462 */         for (int i = this.fDepth; i < this.fElements.length; i++) {
/* 2463 */           this.fElements[i] = new QName();
/*      */         }
/*      */       }
/*      */ 
/* 2467 */       return this.fElements[(this.fDepth++)];
/*      */     }
/*      */ 
/*      */     public QName popElement()
/*      */     {
/* 2484 */       if ((XMLDocumentFragmentScannerImpl.this.fSkip) || (XMLDocumentFragmentScannerImpl.this.fAdd))
/*      */       {
/* 2489 */         return this.fElements[this.fInt[(this.fDepth--)]];
/*      */       }
/*      */ 
/* 2494 */       return this.fElements[(--this.fDepth)];
/*      */     }
/*      */ 
/*      */     public void reposition()
/*      */     {
/* 2504 */       for (int i = 2; i <= this.fDepth; i++)
/* 2505 */         this.fElements[(i - 1)] = this.fElements[this.fInt[i]];
/*      */     }
/*      */ 
/*      */     public void clear()
/*      */     {
/* 2516 */       this.fDepth = 0;
/* 2517 */       this.fLastDepth = 0;
/* 2518 */       this.fCount = 0;
/* 2519 */       this.fPosition = (this.fMark = 1);
/*      */     }
/*      */ 
/*      */     public QName getLastPoppedElement()
/*      */     {
/* 2532 */       return this.fElements[this.fDepth];
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ElementStack2
/*      */   {
/* 2113 */     protected QName[] fQName = new QName[20];
/*      */     protected int fDepth;
/*      */     protected int fCount;
/*      */     protected int fPosition;
/*      */     protected int fMark;
/*      */     protected int fLastDepth;
/*      */ 
/*      */     public ElementStack2()
/*      */     {
/* 2132 */       for (int i = 0; i < this.fQName.length; i++) {
/* 2133 */         this.fQName[i] = new QName();
/*      */       }
/* 2135 */       this.fMark = (this.fPosition = 1);
/*      */     }
/*      */ 
/*      */     public void resize()
/*      */     {
/* 2146 */       int oldLength = this.fQName.length;
/* 2147 */       QName[] tmp = new QName[oldLength * 2];
/* 2148 */       System.arraycopy(this.fQName, 0, tmp, 0, oldLength);
/* 2149 */       this.fQName = tmp;
/*      */ 
/* 2151 */       for (int i = oldLength; i < this.fQName.length; i++)
/* 2152 */         this.fQName[i] = new QName();
/*      */     }
/*      */ 
/*      */     public boolean matchElement(QName element)
/*      */     {
/* 2174 */       boolean match = false;
/* 2175 */       if ((this.fLastDepth > this.fDepth) && (this.fDepth <= 2))
/*      */       {
/* 2179 */         if (element.rawname == this.fQName[this.fDepth].rawname) {
/* 2180 */           XMLDocumentFragmentScannerImpl.this.fAdd = false;
/*      */ 
/* 2183 */           this.fMark = (this.fDepth - 1);
/*      */ 
/* 2185 */           this.fPosition = (this.fMark + 1);
/* 2186 */           match = true;
/*      */ 
/* 2188 */           this.fCount -= 1;
/*      */         }
/*      */         else
/*      */         {
/* 2197 */           XMLDocumentFragmentScannerImpl.this.fAdd = true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2202 */       this.fLastDepth = (this.fDepth++);
/* 2203 */       return match;
/*      */     }
/*      */ 
/*      */     public QName nextElement()
/*      */     {
/* 2216 */       if (this.fCount == this.fQName.length) {
/* 2217 */         XMLDocumentFragmentScannerImpl.this.fShouldSkip = false;
/* 2218 */         XMLDocumentFragmentScannerImpl.this.fAdd = false;
/*      */ 
/* 2222 */         return this.fQName[(--this.fCount)];
/*      */       }
/*      */ 
/* 2227 */       return this.fQName[(this.fCount++)];
/*      */     }
/*      */ 
/*      */     public QName getNext()
/*      */     {
/* 2237 */       if (this.fPosition == this.fCount) {
/* 2238 */         this.fPosition = this.fMark;
/*      */       }
/* 2240 */       return this.fQName[(this.fPosition++)];
/*      */     }
/*      */ 
/*      */     public int popElement()
/*      */     {
/* 2246 */       return this.fDepth--;
/*      */     }
/*      */ 
/*      */     public void clear()
/*      */     {
/* 2252 */       this.fLastDepth = 0;
/* 2253 */       this.fDepth = 0;
/* 2254 */       this.fCount = 0;
/* 2255 */       this.fPosition = (this.fMark = 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class FragmentContentDriver
/*      */     implements XMLDocumentFragmentScannerImpl.Driver
/*      */   {
/* 2595 */     private boolean fContinueDispatching = true;
/* 2596 */     private boolean fScanningForMarkup = true;
/*      */ 
/*      */     protected FragmentContentDriver() {
/*      */     }
/*      */ 
/*      */     private void startOfMarkup() throws IOException {
/* 2602 */       XMLDocumentFragmentScannerImpl.this.fMarkupDepth += 1;
/* 2603 */       int ch = XMLDocumentFragmentScannerImpl.this.fEntityScanner.peekChar();
/*      */ 
/* 2605 */       switch (ch) {
/*      */       case 63:
/* 2607 */         XMLDocumentFragmentScannerImpl.this.setScannerState(23);
/* 2608 */         XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(ch);
/* 2609 */         break;
/*      */       case 33:
/* 2612 */         XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(ch);
/* 2613 */         if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(45)) {
/* 2614 */           if (!XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(45)) {
/* 2615 */             XMLDocumentFragmentScannerImpl.this.reportFatalError("InvalidCommentStart", null);
/*      */           }
/*      */ 
/* 2618 */           XMLDocumentFragmentScannerImpl.this.setScannerState(27);
/* 2619 */         } else if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipString(XMLDocumentFragmentScannerImpl.cdata)) {
/* 2620 */           XMLDocumentFragmentScannerImpl.this.setScannerState(35);
/* 2621 */         } else if (!scanForDoctypeHook()) {
/* 2622 */           XMLDocumentFragmentScannerImpl.this.reportFatalError("MarkupNotRecognizedInContent", null); } break;
/*      */       case 47:
/* 2628 */         XMLDocumentFragmentScannerImpl.this.setScannerState(39);
/* 2629 */         XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(ch);
/* 2630 */         break;
/*      */       default:
/* 2633 */         if (XMLDocumentFragmentScannerImpl.this.isValidNameStartChar(ch))
/* 2634 */           XMLDocumentFragmentScannerImpl.this.setScannerState(38);
/*      */         else
/* 2636 */           XMLDocumentFragmentScannerImpl.this.reportFatalError("MarkupNotRecognizedInContent", null);
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/*      */     private void startOfContent()
/*      */       throws IOException
/*      */     {
/* 2645 */       if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(60))
/* 2646 */         XMLDocumentFragmentScannerImpl.this.setScannerState(21);
/* 2647 */       else if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(38)) {
/* 2648 */         XMLDocumentFragmentScannerImpl.this.setScannerState(28);
/*      */       }
/*      */       else
/* 2651 */         XMLDocumentFragmentScannerImpl.this.setScannerState(37);
/*      */     }
/*      */ 
/*      */     public void decideSubState()
/*      */       throws IOException
/*      */     {
/* 2670 */       while ((XMLDocumentFragmentScannerImpl.this.fScannerState == 22) || (XMLDocumentFragmentScannerImpl.this.fScannerState == 21))
/*      */       {
/* 2672 */         switch (XMLDocumentFragmentScannerImpl.this.fScannerState)
/*      */         {
/*      */         case 22:
/* 2675 */           startOfContent();
/* 2676 */           break;
/*      */         case 21:
/* 2680 */           startOfMarkup();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int next()
/*      */       throws IOException, XNIException
/*      */     {
/*      */       try
/*      */       {
/*      */         while (true)
/*      */         {
/* 2716 */           switch (XMLDocumentFragmentScannerImpl.this.fScannerState) {
/*      */           case 22:
/* 2718 */             int ch = XMLDocumentFragmentScannerImpl.this.fEntityScanner.peekChar();
/* 2719 */             if (ch == 60) {
/* 2720 */               XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar();
/* 2721 */               XMLDocumentFragmentScannerImpl.this.setScannerState(21);
/* 2722 */             } else if (ch == 38) {
/* 2723 */               XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar();
/* 2724 */               XMLDocumentFragmentScannerImpl.this.setScannerState(28);
/*      */             }
/*      */             else
/*      */             {
/* 2728 */               XMLDocumentFragmentScannerImpl.this.setScannerState(37);
/* 2729 */             }break;
/*      */           case 21:
/* 2734 */             startOfMarkup();
/*      */           }
/*      */ 
/* 2742 */           if (XMLDocumentFragmentScannerImpl.this.fIsCoalesce) {
/* 2743 */             XMLDocumentFragmentScannerImpl.this.fUsebuffer = true;
/*      */ 
/* 2745 */             if (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData)
/*      */             {
/* 2749 */               if ((XMLDocumentFragmentScannerImpl.this.fScannerState != 35) && (XMLDocumentFragmentScannerImpl.this.fScannerState != 28) && (XMLDocumentFragmentScannerImpl.this.fScannerState != 37))
/*      */               {
/* 2751 */                 XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData = false;
/* 2752 */                 return 4;
/*      */               }
/*      */ 
/*      */             }
/* 2757 */             else if ((XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference))
/*      */             {
/* 2762 */               if ((XMLDocumentFragmentScannerImpl.this.fScannerState != 35) && (XMLDocumentFragmentScannerImpl.this.fScannerState != 28) && (XMLDocumentFragmentScannerImpl.this.fScannerState != 37))
/*      */               {
/* 2765 */                 XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData = false;
/* 2766 */                 XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference = false;
/* 2767 */                 return 4;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2777 */           switch (XMLDocumentFragmentScannerImpl.this.fScannerState)
/*      */           {
/*      */           case 7:
/* 2780 */             return 7;
/*      */           case 38:
/* 2786 */             XMLDocumentFragmentScannerImpl.this.fEmptyElement = XMLDocumentFragmentScannerImpl.this.scanStartElement();
/*      */ 
/* 2788 */             if (XMLDocumentFragmentScannerImpl.this.fEmptyElement) {
/* 2789 */               XMLDocumentFragmentScannerImpl.this.setScannerState(39);
/*      */             }
/*      */             else {
/* 2792 */               XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/*      */             }
/* 2794 */             return 1;
/*      */           case 37:
/* 2803 */             XMLDocumentFragmentScannerImpl.this.fUsebuffer = ((XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData));
/*      */ 
/* 2806 */             if ((XMLDocumentFragmentScannerImpl.this.fIsCoalesce) && ((XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData))) {
/* 2807 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference = false;
/* 2808 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData = false;
/* 2809 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData = true;
/* 2810 */               XMLDocumentFragmentScannerImpl.this.fUsebuffer = true;
/*      */             }
/*      */             else {
/* 2813 */               XMLDocumentFragmentScannerImpl.this.fContentBuffer.clear();
/*      */             }
/*      */ 
/* 2818 */             XMLDocumentFragmentScannerImpl.this.fTempString.length = 0;
/* 2819 */             int c = XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanContent(XMLDocumentFragmentScannerImpl.this.fTempString);
/*      */ 
/* 2823 */             if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(60))
/*      */             {
/* 2825 */               if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(47))
/*      */               {
/* 2827 */                 XMLDocumentFragmentScannerImpl.this.fMarkupDepth += 1;
/* 2828 */                 XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData = false;
/* 2829 */                 XMLDocumentFragmentScannerImpl.this.setScannerState(39);
/*      */               }
/* 2831 */               else if (XMLChar.isNameStart(XMLDocumentFragmentScannerImpl.this.fEntityScanner.peekChar())) {
/* 2832 */                 XMLDocumentFragmentScannerImpl.this.fMarkupDepth += 1;
/* 2833 */                 XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData = false;
/* 2834 */                 XMLDocumentFragmentScannerImpl.this.setScannerState(38);
/*      */               } else {
/* 2836 */                 XMLDocumentFragmentScannerImpl.this.setScannerState(21);
/*      */ 
/* 2838 */                 if (XMLDocumentFragmentScannerImpl.this.fIsCoalesce) {
/* 2839 */                   XMLDocumentFragmentScannerImpl.this.fUsebuffer = true;
/* 2840 */                   XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData = true;
/* 2841 */                   XMLDocumentFragmentScannerImpl.this.fContentBuffer.append(XMLDocumentFragmentScannerImpl.this.fTempString);
/* 2842 */                   XMLDocumentFragmentScannerImpl.this.fTempString.length = 0;
/* 2843 */                   continue;
/*      */                 }
/*      */               }
/*      */ 
/* 2847 */               if (XMLDocumentFragmentScannerImpl.this.fUsebuffer) {
/* 2848 */                 XMLDocumentFragmentScannerImpl.this.fContentBuffer.append(XMLDocumentFragmentScannerImpl.this.fTempString);
/* 2849 */                 XMLDocumentFragmentScannerImpl.this.fTempString.length = 0;
/*      */               }
/*      */ 
/* 2855 */               checkLimit(XMLDocumentFragmentScannerImpl.this.fContentBuffer);
/* 2856 */               if ((XMLDocumentFragmentScannerImpl.this.dtdGrammarUtil != null) && (XMLDocumentFragmentScannerImpl.this.dtdGrammarUtil.isIgnorableWhiteSpace(XMLDocumentFragmentScannerImpl.this.fContentBuffer)))
/*      */               {
/* 2858 */                 return 6;
/*      */               }
/* 2860 */               return 4;
/*      */             }
/*      */             else {
/* 2863 */               XMLDocumentFragmentScannerImpl.this.fUsebuffer = true;
/*      */ 
/* 2868 */               XMLDocumentFragmentScannerImpl.this.fContentBuffer.append(XMLDocumentFragmentScannerImpl.this.fTempString);
/* 2869 */               XMLDocumentFragmentScannerImpl.this.fTempString.length = 0;
/*      */ 
/* 2871 */               if (c == 13)
/*      */               {
/* 2877 */                 XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar();
/* 2878 */                 XMLDocumentFragmentScannerImpl.this.fUsebuffer = true;
/* 2879 */                 XMLDocumentFragmentScannerImpl.this.fContentBuffer.append((char)c);
/* 2880 */                 c = -1;
/* 2881 */               } else if (c == 93)
/*      */               {
/* 2884 */                 XMLDocumentFragmentScannerImpl.this.fUsebuffer = true;
/* 2885 */                 XMLDocumentFragmentScannerImpl.this.fContentBuffer.append((char)XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar());
/*      */ 
/* 2889 */                 XMLDocumentFragmentScannerImpl.this.fInScanContent = true;
/*      */ 
/* 2894 */                 if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(93)) {
/* 2895 */                   XMLDocumentFragmentScannerImpl.this.fContentBuffer.append(']');
/* 2896 */                   while (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(93)) {
/* 2897 */                     XMLDocumentFragmentScannerImpl.this.fContentBuffer.append(']');
/*      */                   }
/* 2899 */                   if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(62)) {
/* 2900 */                     XMLDocumentFragmentScannerImpl.this.reportFatalError("CDEndInContent", null);
/*      */                   }
/*      */                 }
/* 2903 */                 c = -1;
/* 2904 */                 XMLDocumentFragmentScannerImpl.this.fInScanContent = false;
/*      */               }
/*      */ 
/*      */               do
/*      */               {
/* 2911 */                 if (c == 60) {
/* 2912 */                   XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar();
/* 2913 */                   XMLDocumentFragmentScannerImpl.this.setScannerState(21);
/* 2914 */                   break;
/*      */                 }
/* 2916 */                 if (c == 38) {
/* 2917 */                   XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar();
/* 2918 */                   XMLDocumentFragmentScannerImpl.this.setScannerState(28);
/* 2919 */                   break;
/*      */                 }
/* 2921 */                 if ((c != -1) && (XMLDocumentFragmentScannerImpl.this.isInvalidLiteral(c))) {
/* 2922 */                   if (XMLChar.isHighSurrogate(c))
/*      */                   {
/* 2924 */                     XMLDocumentFragmentScannerImpl.this.scanSurrogates(XMLDocumentFragmentScannerImpl.this.fContentBuffer);
/* 2925 */                     XMLDocumentFragmentScannerImpl.this.setScannerState(22); break;
/*      */                   }
/* 2927 */                   XMLDocumentFragmentScannerImpl.this.reportFatalError("InvalidCharInContent", new Object[] { Integer.toString(c, 16) });
/*      */ 
/* 2930 */                   XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar();
/*      */ 
/* 2932 */                   break;
/*      */                 }
/*      */ 
/* 2935 */                 c = XMLDocumentFragmentScannerImpl.this.scanContent(XMLDocumentFragmentScannerImpl.this.fContentBuffer);
/*      */               }
/*      */ 
/* 2938 */               while (XMLDocumentFragmentScannerImpl.this.fIsCoalesce);
/* 2939 */               XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/*      */ 
/* 2950 */               if (XMLDocumentFragmentScannerImpl.this.fIsCoalesce) {
/* 2951 */                 XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData = true;
/*      */               }
/*      */               else
/*      */               {
/* 2955 */                 checkLimit(XMLDocumentFragmentScannerImpl.this.fContentBuffer);
/* 2956 */                 if ((XMLDocumentFragmentScannerImpl.this.dtdGrammarUtil != null) && (XMLDocumentFragmentScannerImpl.this.dtdGrammarUtil.isIgnorableWhiteSpace(XMLDocumentFragmentScannerImpl.this.fContentBuffer)))
/*      */                 {
/* 2958 */                   return 6;
/*      */                 }
/* 2960 */                 return 4;
/*      */               }
/*      */             }
/*      */             break;
/*      */           case 39:
/* 2965 */             if (XMLDocumentFragmentScannerImpl.this.fEmptyElement)
/*      */             {
/* 2967 */               XMLDocumentFragmentScannerImpl.this.fEmptyElement = false;
/* 2968 */               XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/*      */ 
/* 2971 */               return (XMLDocumentFragmentScannerImpl.this.fMarkupDepth == 0) && (elementDepthIsZeroHook()) ? 2 : 2;
/*      */             }
/* 2973 */             if (XMLDocumentFragmentScannerImpl.this.scanEndElement() == 0)
/*      */             {
/* 2975 */               if (elementDepthIsZeroHook())
/*      */               {
/* 2979 */                 return 2;
/*      */               }
/*      */             }
/*      */ 
/* 2983 */             XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 2984 */             return 2;
/*      */           case 27:
/* 2988 */             XMLDocumentFragmentScannerImpl.this.scanComment();
/* 2989 */             XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 2990 */             return 5;
/*      */           case 23:
/* 2995 */             XMLDocumentFragmentScannerImpl.this.fContentBuffer.clear();
/*      */ 
/* 2999 */             XMLDocumentFragmentScannerImpl.this.scanPI(XMLDocumentFragmentScannerImpl.this.fContentBuffer);
/* 3000 */             XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 3001 */             return 3;
/*      */           case 35:
/* 3010 */             if ((XMLDocumentFragmentScannerImpl.this.fIsCoalesce) && ((XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData))) {
/* 3011 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData = true;
/* 3012 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference = false;
/* 3013 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData = false;
/*      */             }
/*      */             else {
/* 3016 */               XMLDocumentFragmentScannerImpl.this.fContentBuffer.clear();
/*      */             }
/* 3018 */             XMLDocumentFragmentScannerImpl.this.fUsebuffer = true;
/*      */ 
/* 3020 */             XMLDocumentFragmentScannerImpl.this.scanCDATASection(XMLDocumentFragmentScannerImpl.this.fContentBuffer, true);
/* 3021 */             XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/*      */ 
/* 3029 */             if (XMLDocumentFragmentScannerImpl.this.fIsCoalesce) {
/* 3030 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData = true;
/*      */             }
/*      */             else {
/* 3033 */               if (XMLDocumentFragmentScannerImpl.this.fReportCdataEvent) {
/* 3034 */                 return 12;
/*      */               }
/* 3036 */               return 4;
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 28:
/* 3041 */             XMLDocumentFragmentScannerImpl.this.fMarkupDepth += 1;
/* 3042 */             XMLDocumentFragmentScannerImpl.this.foundBuiltInRefs = false;
/*      */ 
/* 3046 */             if ((XMLDocumentFragmentScannerImpl.this.fIsCoalesce) && ((XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData) || (XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData)))
/*      */             {
/* 3049 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference = true;
/* 3050 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasCData = false;
/* 3051 */               XMLDocumentFragmentScannerImpl.this.fLastSectionWasCharacterData = false;
/*      */             }
/*      */             else {
/* 3054 */               XMLDocumentFragmentScannerImpl.this.fContentBuffer.clear();
/*      */             }
/* 3056 */             XMLDocumentFragmentScannerImpl.this.fUsebuffer = true;
/*      */ 
/* 3058 */             if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(35)) {
/* 3059 */               XMLDocumentFragmentScannerImpl.this.scanCharReferenceValue(XMLDocumentFragmentScannerImpl.this.fContentBuffer, null);
/* 3060 */               XMLDocumentFragmentScannerImpl.this.fMarkupDepth -= 1;
/* 3061 */               if (!XMLDocumentFragmentScannerImpl.this.fIsCoalesce) {
/* 3062 */                 XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 3063 */                 return 4;
/*      */               }
/*      */             }
/*      */             else {
/* 3067 */               XMLDocumentFragmentScannerImpl.this.scanEntityReference(XMLDocumentFragmentScannerImpl.this.fContentBuffer);
/*      */ 
/* 3070 */               if ((XMLDocumentFragmentScannerImpl.this.fScannerState == 41) && (!XMLDocumentFragmentScannerImpl.this.fIsCoalesce)) {
/* 3071 */                 XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 3072 */                 return 4;
/*      */               }
/*      */ 
/* 3076 */               if (XMLDocumentFragmentScannerImpl.this.fScannerState == 36) {
/* 3077 */                 XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference = true;
/* 3078 */                 continue;
/*      */               }
/*      */ 
/* 3081 */               if (XMLDocumentFragmentScannerImpl.this.fScannerState == 28) {
/* 3082 */                 XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 3083 */                 if ((XMLDocumentFragmentScannerImpl.this.fReplaceEntityReferences) && (XMLDocumentFragmentScannerImpl.this.fEntityStore.isDeclaredEntity(XMLDocumentFragmentScannerImpl.this.fCurrentEntityName)))
/*      */                 {
/*      */                   continue;
/*      */                 }
/* 3087 */                 return 9;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/* 3092 */             XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 3093 */             XMLDocumentFragmentScannerImpl.this.fLastSectionWasEntityReference = true;
/* 3094 */             break;
/*      */           case 36:
/* 3099 */             if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipString("<?xml")) {
/* 3100 */               XMLDocumentFragmentScannerImpl.this.fMarkupDepth += 1;
/*      */ 
/* 3103 */               if (XMLDocumentFragmentScannerImpl.this.isValidNameChar(XMLDocumentFragmentScannerImpl.this.fEntityScanner.peekChar())) {
/* 3104 */                 XMLDocumentFragmentScannerImpl.this.fStringBuffer.clear();
/* 3105 */                 XMLDocumentFragmentScannerImpl.this.fStringBuffer.append("xml");
/*      */ 
/* 3107 */                 if (XMLDocumentFragmentScannerImpl.this.fNamespaces) {
/* 3108 */                   while (XMLDocumentFragmentScannerImpl.this.isValidNCName(XMLDocumentFragmentScannerImpl.this.fEntityScanner.peekChar())) {
/* 3109 */                     XMLDocumentFragmentScannerImpl.this.fStringBuffer.append((char)XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar());
/*      */                   }
/*      */                 }
/* 3112 */                 while (XMLDocumentFragmentScannerImpl.this.isValidNameChar(XMLDocumentFragmentScannerImpl.this.fEntityScanner.peekChar())) {
/* 3113 */                   XMLDocumentFragmentScannerImpl.this.fStringBuffer.append((char)XMLDocumentFragmentScannerImpl.this.fEntityScanner.scanChar());
/*      */                 }
/*      */ 
/* 3116 */                 String target = XMLDocumentFragmentScannerImpl.this.fSymbolTable.addSymbol(XMLDocumentFragmentScannerImpl.this.fStringBuffer.ch, XMLDocumentFragmentScannerImpl.this.fStringBuffer.offset, XMLDocumentFragmentScannerImpl.this.fStringBuffer.length);
/* 3117 */                 XMLDocumentFragmentScannerImpl.this.fContentBuffer.clear();
/* 3118 */                 XMLDocumentFragmentScannerImpl.this.scanPIData(target, XMLDocumentFragmentScannerImpl.this.fContentBuffer);
/*      */               }
/*      */               else
/*      */               {
/* 3124 */                 XMLDocumentFragmentScannerImpl.this.scanXMLDeclOrTextDecl(true);
/*      */               }
/*      */             }
/*      */ 
/* 3128 */             XMLDocumentFragmentScannerImpl.this.fEntityManager.fCurrentEntity.mayReadChunks = true;
/* 3129 */             XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/*      */           case 26:
/*      */           case 40:
/*      */           case 8:
/*      */           case 9:
/*      */           case 10:
/*      */           case 11:
/*      */           case 12:
/*      */           case 13:
/*      */           case 14:
/*      */           case 15:
/*      */           case 16:
/*      */           case 17:
/*      */           case 18:
/*      */           case 19:
/*      */           case 20:
/*      */           case 21:
/*      */           case 22:
/*      */           case 24:
/*      */           case 25:
/*      */           case 29:
/*      */           case 30:
/*      */           case 31:
/*      */           case 32:
/*      */           case 33:
/* 3138 */           case 34: }  } if (scanRootElementHook()) {
/* 3139 */           XMLDocumentFragmentScannerImpl.this.fEmptyElement = true;
/*      */ 
/* 3141 */           return 1;
/*      */         }
/* 3143 */         XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 3144 */         return 1;
/*      */ 
/* 3147 */         XMLDocumentFragmentScannerImpl.this.fContentBuffer.clear();
/* 3148 */         XMLDocumentFragmentScannerImpl.this.scanCharReferenceValue(XMLDocumentFragmentScannerImpl.this.fContentBuffer, null);
/* 3149 */         XMLDocumentFragmentScannerImpl.this.fMarkupDepth -= 1;
/* 3150 */         XMLDocumentFragmentScannerImpl.this.setScannerState(22);
/* 3151 */         return 4;
/*      */ 
/* 3154 */         throw new XNIException("Scanner State " + XMLDocumentFragmentScannerImpl.this.fScannerState + " not Recognized ");
/*      */       }
/*      */       catch (EOFException e)
/*      */       {
/* 3160 */         endOfFileHook(e);
/* 3161 */       }return -1;
/*      */     }
/*      */ 
/*      */     protected void checkLimit(XMLStringBuffer buffer)
/*      */     {
/* 3172 */       if (XMLDocumentFragmentScannerImpl.this.fLimitAnalyzer.isTracking(XMLDocumentFragmentScannerImpl.this.fCurrentEntityName)) {
/* 3173 */         XMLDocumentFragmentScannerImpl.this.fLimitAnalyzer.addValue(XMLSecurityManager.Limit.GENEAL_ENTITY_SIZE_LIMIT, XMLDocumentFragmentScannerImpl.this.fCurrentEntityName, buffer.length);
/* 3174 */         if (XMLDocumentFragmentScannerImpl.this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.GENEAL_ENTITY_SIZE_LIMIT, XMLDocumentFragmentScannerImpl.this.fLimitAnalyzer)) {
/* 3175 */           XMLDocumentFragmentScannerImpl.this.fSecurityManager.debugPrint(XMLDocumentFragmentScannerImpl.this.fLimitAnalyzer);
/* 3176 */           XMLDocumentFragmentScannerImpl.this.reportFatalError("MaxEntitySizeLimit", new Object[] { XMLDocumentFragmentScannerImpl.this.fCurrentEntityName, Integer.valueOf(XMLDocumentFragmentScannerImpl.this.fLimitAnalyzer.getValue(XMLSecurityManager.Limit.GENEAL_ENTITY_SIZE_LIMIT)), Integer.valueOf(XMLDocumentFragmentScannerImpl.this.fSecurityManager.getLimit(XMLSecurityManager.Limit.GENEAL_ENTITY_SIZE_LIMIT)), XMLDocumentFragmentScannerImpl.this.fSecurityManager.getStateLiteral(XMLSecurityManager.Limit.GENEAL_ENTITY_SIZE_LIMIT) });
/*      */         }
/*      */ 
/* 3181 */         if (XMLDocumentFragmentScannerImpl.this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT, XMLDocumentFragmentScannerImpl.this.fLimitAnalyzer)) {
/* 3182 */           XMLDocumentFragmentScannerImpl.this.fSecurityManager.debugPrint(XMLDocumentFragmentScannerImpl.this.fLimitAnalyzer);
/* 3183 */           XMLDocumentFragmentScannerImpl.this.reportFatalError("TotalEntitySizeLimit", new Object[] { Integer.valueOf(XMLDocumentFragmentScannerImpl.this.fLimitAnalyzer.getTotalValue(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)), Integer.valueOf(XMLDocumentFragmentScannerImpl.this.fSecurityManager.getLimit(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)), XMLDocumentFragmentScannerImpl.this.fSecurityManager.getStateLiteral(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT) });
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected boolean scanForDoctypeHook()
/*      */       throws IOException, XNIException
/*      */     {
/* 3210 */       return false;
/*      */     }
/*      */ 
/*      */     protected boolean elementDepthIsZeroHook()
/*      */       throws IOException, XNIException
/*      */     {
/* 3228 */       return false;
/*      */     }
/*      */ 
/*      */     protected boolean scanRootElementHook()
/*      */       throws IOException, XNIException
/*      */     {
/* 3245 */       return false;
/*      */     }
/*      */ 
/*      */     protected void endOfFileHook(EOFException e)
/*      */       throws IOException, XNIException
/*      */     {
/* 3260 */       if (XMLDocumentFragmentScannerImpl.this.fMarkupDepth != 0)
/* 3261 */         XMLDocumentFragmentScannerImpl.this.reportFatalError("PrematureEOF", null);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl
 * JD-Core Version:    0.6.2
 */