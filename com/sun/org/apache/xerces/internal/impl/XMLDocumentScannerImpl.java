/*      */ package com.sun.org.apache.xerces.internal.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDDescription;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*      */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.xml.internal.stream.Entity;
/*      */ import com.sun.xml.internal.stream.Entity.ScannedEntity;
/*      */ import com.sun.xml.internal.stream.StaxXMLInputSource;
/*      */ import com.sun.xml.internal.stream.XMLEntityStorage;
/*      */ import com.sun.xml.internal.stream.dtd.DTDGrammarUtil;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.util.NoSuchElementException;
/*      */ 
/*      */ public class XMLDocumentScannerImpl extends XMLDocumentFragmentScannerImpl
/*      */ {
/*      */   protected static final int SCANNER_STATE_XML_DECL = 42;
/*      */   protected static final int SCANNER_STATE_PROLOG = 43;
/*      */   protected static final int SCANNER_STATE_TRAILING_MISC = 44;
/*      */   protected static final int SCANNER_STATE_DTD_INTERNAL_DECLS = 45;
/*      */   protected static final int SCANNER_STATE_DTD_EXTERNAL = 46;
/*      */   protected static final int SCANNER_STATE_DTD_EXTERNAL_DECLS = 47;
/*      */   protected static final int SCANNER_STATE_NO_SUCH_ELEMENT_EXCEPTION = 48;
/*      */   protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
/*      */   protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
/*      */   protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
/*      */   protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
/*      */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*      */   protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
/*  131 */   private static final String[] RECOGNIZED_FEATURES = { "http://apache.org/xml/features/nonvalidating/load-external-dtd", "http://apache.org/xml/features/disallow-doctype-decl" };
/*      */ 
/*  137 */   private static final Boolean[] FEATURE_DEFAULTS = { Boolean.TRUE, Boolean.FALSE };
/*      */ 
/*  143 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/validation-manager" };
/*      */ 
/*  149 */   private static final Object[] PROPERTY_DEFAULTS = { null, null };
/*      */ 
/*  161 */   protected XMLDTDScanner fDTDScanner = null;
/*      */   protected ValidationManager fValidationManager;
/*  167 */   protected XMLStringBuffer fDTDDecl = null;
/*  168 */   protected boolean fReadingDTD = false;
/*  169 */   protected boolean fAddedListener = false;
/*      */   protected String fDoctypeName;
/*      */   protected String fDoctypePublicId;
/*      */   protected String fDoctypeSystemId;
/*  185 */   protected NamespaceContext fNamespaceContext = new NamespaceSupport();
/*      */ 
/*  190 */   protected boolean fLoadExternalDTD = true;
/*      */   protected boolean fSeenDoctypeDecl;
/*      */   protected boolean fScanEndElement;
/*  204 */   protected XMLDocumentFragmentScannerImpl.Driver fXMLDeclDriver = new XMLDeclDriver();
/*      */ 
/*  207 */   protected XMLDocumentFragmentScannerImpl.Driver fPrologDriver = new PrologDriver();
/*      */ 
/*  210 */   protected XMLDocumentFragmentScannerImpl.Driver fDTDDriver = null;
/*      */ 
/*  213 */   protected XMLDocumentFragmentScannerImpl.Driver fTrailingMiscDriver = new TrailingMiscDriver();
/*  214 */   protected int fStartPos = 0;
/*  215 */   protected int fEndPos = 0;
/*  216 */   protected boolean fSeenInternalSubset = false;
/*      */ 
/*  220 */   private String[] fStrings = new String[3];
/*      */ 
/*  223 */   private XMLInputSource fExternalSubsetSource = null;
/*      */ 
/*  226 */   private final XMLDTDDescription fDTDDescription = new XMLDTDDescription(null, null, null, null, null);
/*      */ 
/*  229 */   private XMLString fString = new XMLString();
/*      */ 
/*  231 */   private static final char[] DOCTYPE = { 'D', 'O', 'C', 'T', 'Y', 'P', 'E' };
/*  232 */   private static final char[] COMMENTSTRING = { '-', '-' };
/*      */ 
/*      */   public void setInputSource(XMLInputSource inputSource)
/*      */     throws IOException
/*      */   {
/*  255 */     this.fEntityManager.setEntityHandler(this);
/*      */ 
/*  257 */     this.fEntityManager.startDocumentEntity(inputSource);
/*      */ 
/*  259 */     setScannerState(7);
/*      */   }
/*      */ 
/*      */   public int getScannetState()
/*      */   {
/*  266 */     return this.fScannerState;
/*      */   }
/*      */ 
/*      */   public void reset(PropertyManager propertyManager)
/*      */   {
/*  273 */     super.reset(propertyManager);
/*      */ 
/*  275 */     this.fDoctypeName = null;
/*  276 */     this.fDoctypePublicId = null;
/*  277 */     this.fDoctypeSystemId = null;
/*  278 */     this.fSeenDoctypeDecl = false;
/*  279 */     this.fNamespaceContext.reset();
/*  280 */     this.fSupportDTD = ((Boolean)propertyManager.getProperty("javax.xml.stream.supportDTD")).booleanValue();
/*      */ 
/*  283 */     this.fLoadExternalDTD = (!((Boolean)propertyManager.getProperty("http://java.sun.com/xml/stream/properties/ignore-external-dtd")).booleanValue());
/*  284 */     setScannerState(7);
/*  285 */     setDriver(this.fXMLDeclDriver);
/*  286 */     this.fSeenInternalSubset = false;
/*  287 */     if (this.fDTDScanner != null) {
/*  288 */       ((XMLDTDScannerImpl)this.fDTDScanner).reset(propertyManager);
/*      */     }
/*  290 */     this.fEndPos = 0;
/*  291 */     this.fStartPos = 0;
/*  292 */     if (this.fDTDDecl != null)
/*  293 */       this.fDTDDecl.clear();
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/*  315 */     super.reset(componentManager);
/*      */ 
/*  318 */     this.fDoctypeName = null;
/*  319 */     this.fDoctypePublicId = null;
/*  320 */     this.fDoctypeSystemId = null;
/*  321 */     this.fSeenDoctypeDecl = false;
/*  322 */     this.fExternalSubsetSource = null;
/*      */ 
/*  325 */     this.fLoadExternalDTD = componentManager.getFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
/*  326 */     this.fDisallowDoctype = componentManager.getFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
/*      */ 
/*  328 */     this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
/*      */ 
/*  330 */     this.fSeenInternalSubset = false;
/*      */ 
/*  332 */     this.fDTDScanner = ((XMLDTDScanner)componentManager.getProperty("http://apache.org/xml/properties/internal/dtd-scanner"));
/*      */ 
/*  334 */     this.fValidationManager = ((ValidationManager)componentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager", null));
/*      */     try
/*      */     {
/*  337 */       this.fNamespaceContext = ((NamespaceContext)componentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context"));
/*      */     } catch (XMLConfigurationException e) {
/*      */     }
/*  340 */     if (this.fNamespaceContext == null) {
/*  341 */       this.fNamespaceContext = new NamespaceSupport();
/*      */     }
/*  343 */     this.fNamespaceContext.reset();
/*      */ 
/*  345 */     this.fEndPos = 0;
/*  346 */     this.fStartPos = 0;
/*  347 */     if (this.fDTDDecl != null) {
/*  348 */       this.fDTDDecl.clear();
/*      */     }
/*      */ 
/*  354 */     setScannerState(42);
/*  355 */     setDriver(this.fXMLDeclDriver);
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/*  366 */     String[] featureIds = super.getRecognizedFeatures();
/*  367 */     int length = featureIds != null ? featureIds.length : 0;
/*  368 */     String[] combinedFeatureIds = new String[length + RECOGNIZED_FEATURES.length];
/*  369 */     if (featureIds != null) {
/*  370 */       System.arraycopy(featureIds, 0, combinedFeatureIds, 0, featureIds.length);
/*      */     }
/*  372 */     System.arraycopy(RECOGNIZED_FEATURES, 0, combinedFeatureIds, length, RECOGNIZED_FEATURES.length);
/*  373 */     return combinedFeatureIds;
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  394 */     super.setFeature(featureId, state);
/*      */ 
/*  397 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/*  398 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*      */ 
/*  400 */       if ((suffixLength == "nonvalidating/load-external-dtd".length()) && (featureId.endsWith("nonvalidating/load-external-dtd")))
/*      */       {
/*  402 */         this.fLoadExternalDTD = state;
/*  403 */         return;
/*      */       }
/*  405 */       if ((suffixLength == "disallow-doctype-decl".length()) && (featureId.endsWith("disallow-doctype-decl")))
/*      */       {
/*  407 */         this.fDisallowDoctype = state;
/*  408 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/*  420 */     String[] propertyIds = super.getRecognizedProperties();
/*  421 */     int length = propertyIds != null ? propertyIds.length : 0;
/*  422 */     String[] combinedPropertyIds = new String[length + RECOGNIZED_PROPERTIES.length];
/*  423 */     if (propertyIds != null) {
/*  424 */       System.arraycopy(propertyIds, 0, combinedPropertyIds, 0, propertyIds.length);
/*      */     }
/*  426 */     System.arraycopy(RECOGNIZED_PROPERTIES, 0, combinedPropertyIds, length, RECOGNIZED_PROPERTIES.length);
/*  427 */     return combinedPropertyIds;
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  448 */     super.setProperty(propertyId, value);
/*      */ 
/*  451 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/*  452 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*      */ 
/*  454 */       if ((suffixLength == "internal/dtd-scanner".length()) && (propertyId.endsWith("internal/dtd-scanner")))
/*      */       {
/*  456 */         this.fDTDScanner = ((XMLDTDScanner)value);
/*      */       }
/*  458 */       if ((suffixLength == "internal/namespace-context".length()) && (propertyId.endsWith("internal/namespace-context")))
/*      */       {
/*  460 */         if (value != null) {
/*  461 */           this.fNamespaceContext = ((NamespaceContext)value);
/*      */         }
/*      */       }
/*      */ 
/*  465 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/*  481 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/*  482 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/*  483 */         return FEATURE_DEFAULTS[i];
/*      */       }
/*      */     }
/*  486 */     return super.getFeatureDefault(featureId);
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/*  499 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/*  500 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/*  501 */         return PROPERTY_DEFAULTS[i];
/*      */       }
/*      */     }
/*  504 */     return super.getPropertyDefault(propertyId);
/*      */   }
/*      */ 
/*      */   public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  530 */     super.startEntity(name, identifier, encoding, augs);
/*      */ 
/*  533 */     this.fEntityScanner.registerListener(this);
/*      */ 
/*  536 */     if ((!name.equals("[xml]")) && (this.fEntityScanner.isExternal()))
/*      */     {
/*  538 */       if ((augs == null) || (!((Boolean)augs.getItem("ENTITY_SKIPPED")).booleanValue())) {
/*  539 */         setScannerState(36);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  545 */     if ((this.fDocumentHandler != null) && (name.equals("[xml]")))
/*  546 */       this.fDocumentHandler.startDocument(this.fEntityScanner, encoding, this.fNamespaceContext, null);
/*      */   }
/*      */ 
/*      */   public void endEntity(String name, Augmentations augs)
/*      */     throws IOException, XNIException
/*      */   {
/*  563 */     super.endEntity(name, augs);
/*      */ 
/*  565 */     if (name.equals("[xml]"))
/*      */     {
/*  570 */       if ((this.fMarkupDepth == 0) && (this.fDriver == this.fTrailingMiscDriver))
/*      */       {
/*  572 */         setScannerState(34);
/*      */       }
/*      */       else
/*      */       {
/*  576 */         throw new EOFException();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public XMLStringBuffer getDTDDecl()
/*      */   {
/*  589 */     Entity entity = this.fEntityScanner.getCurrentEntity();
/*  590 */     this.fDTDDecl.append(((Entity.ScannedEntity)entity).ch, this.fStartPos, this.fEndPos - this.fStartPos);
/*  591 */     if (this.fSeenInternalSubset)
/*  592 */       this.fDTDDecl.append("]>");
/*  593 */     return this.fDTDDecl;
/*      */   }
/*      */ 
/*      */   public String getCharacterEncodingScheme() {
/*  597 */     return this.fDeclaredEncoding;
/*      */   }
/*      */ 
/*      */   public int next()
/*      */     throws IOException, XNIException
/*      */   {
/*  606 */     return this.fDriver.next();
/*      */   }
/*      */ 
/*      */   public NamespaceContext getNamespaceContext()
/*      */   {
/*  611 */     return this.fNamespaceContext;
/*      */   }
/*      */ 
/*      */   protected XMLDocumentFragmentScannerImpl.Driver createContentDriver()
/*      */   {
/*  624 */     return new ContentDriver();
/*      */   }
/*      */ 
/*      */   protected boolean scanDoctypeDecl(boolean supportDTD)
/*      */     throws IOException, XNIException
/*      */   {
/*  633 */     if (!this.fEntityScanner.skipSpaces()) {
/*  634 */       reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ROOT_ELEMENT_TYPE_IN_DOCTYPEDECL", null);
/*      */     }
/*      */ 
/*  639 */     this.fDoctypeName = this.fEntityScanner.scanName();
/*  640 */     if (this.fDoctypeName == null) {
/*  641 */       reportFatalError("MSG_ROOT_ELEMENT_TYPE_REQUIRED", null);
/*      */     }
/*      */ 
/*  645 */     if (this.fEntityScanner.skipSpaces()) {
/*  646 */       scanExternalID(this.fStrings, false);
/*  647 */       this.fDoctypeSystemId = this.fStrings[0];
/*  648 */       this.fDoctypePublicId = this.fStrings[1];
/*  649 */       this.fEntityScanner.skipSpaces();
/*      */     }
/*      */ 
/*  652 */     this.fHasExternalDTD = (this.fDoctypeSystemId != null);
/*      */ 
/*  655 */     if ((supportDTD) && (!this.fHasExternalDTD) && (this.fExternalSubsetResolver != null)) {
/*  656 */       this.fDTDDescription.setValues(null, null, this.fEntityManager.getCurrentResourceIdentifier().getExpandedSystemId(), null);
/*  657 */       this.fDTDDescription.setRootName(this.fDoctypeName);
/*  658 */       this.fExternalSubsetSource = this.fExternalSubsetResolver.getExternalSubset(this.fDTDDescription);
/*  659 */       this.fHasExternalDTD = (this.fExternalSubsetSource != null);
/*      */     }
/*      */ 
/*  663 */     if ((supportDTD) && (this.fDocumentHandler != null))
/*      */     {
/*  669 */       if (this.fExternalSubsetSource == null) {
/*  670 */         this.fDocumentHandler.doctypeDecl(this.fDoctypeName, this.fDoctypePublicId, this.fDoctypeSystemId, null);
/*      */       }
/*      */       else {
/*  673 */         this.fDocumentHandler.doctypeDecl(this.fDoctypeName, this.fExternalSubsetSource.getPublicId(), this.fExternalSubsetSource.getSystemId(), null);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  678 */     boolean internalSubset = true;
/*  679 */     if (!this.fEntityScanner.skipChar(91)) {
/*  680 */       internalSubset = false;
/*  681 */       this.fEntityScanner.skipSpaces();
/*  682 */       if (!this.fEntityScanner.skipChar(62)) {
/*  683 */         reportFatalError("DoctypedeclUnterminated", new Object[] { this.fDoctypeName });
/*      */       }
/*  685 */       this.fMarkupDepth -= 1;
/*      */     }
/*  687 */     return internalSubset;
/*      */   }
/*      */ 
/*      */   protected void setEndDTDScanState()
/*      */   {
/*  696 */     setScannerState(43);
/*  697 */     setDriver(this.fPrologDriver);
/*  698 */     this.fEntityManager.setEntityHandler(this);
/*  699 */     this.fReadingDTD = false;
/*      */   }
/*      */ 
/*      */   protected String getScannerStateName(int state)
/*      */   {
/*  705 */     switch (state) { case 42:
/*  706 */       return "SCANNER_STATE_XML_DECL";
/*      */     case 43:
/*  707 */       return "SCANNER_STATE_PROLOG";
/*      */     case 44:
/*  708 */       return "SCANNER_STATE_TRAILING_MISC";
/*      */     case 45:
/*  709 */       return "SCANNER_STATE_DTD_INTERNAL_DECLS";
/*      */     case 46:
/*  710 */       return "SCANNER_STATE_DTD_EXTERNAL";
/*      */     case 47:
/*  711 */       return "SCANNER_STATE_DTD_EXTERNAL_DECLS";
/*      */     }
/*  713 */     return super.getScannerStateName(state);
/*      */   }
/*      */ 
/*      */   public void refresh(int refreshPosition)
/*      */   {
/* 1490 */     super.refresh(refreshPosition);
/* 1491 */     if (this.fReadingDTD) {
/* 1492 */       Entity entity = this.fEntityScanner.getCurrentEntity();
/* 1493 */       if ((entity instanceof Entity.ScannedEntity)) {
/* 1494 */         this.fEndPos = ((Entity.ScannedEntity)entity).position;
/*      */       }
/* 1496 */       this.fDTDDecl.append(((Entity.ScannedEntity)entity).ch, this.fStartPos, this.fEndPos - this.fStartPos);
/* 1497 */       this.fStartPos = refreshPosition;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ContentDriver extends XMLDocumentFragmentScannerImpl.FragmentContentDriver
/*      */   {
/*      */     protected ContentDriver()
/*      */     {
/* 1223 */       super();
/*      */     }
/*      */ 
/*      */     protected boolean scanForDoctypeHook()
/*      */       throws IOException, XNIException
/*      */     {
/* 1246 */       if (XMLDocumentScannerImpl.this.fEntityScanner.skipString(XMLDocumentScannerImpl.DOCTYPE)) {
/* 1247 */         XMLDocumentScannerImpl.this.setScannerState(24);
/*      */ 
/* 1249 */         return true;
/*      */       }
/* 1251 */       return false;
/*      */     }
/*      */ 
/*      */     protected boolean elementDepthIsZeroHook()
/*      */       throws IOException, XNIException
/*      */     {
/* 1271 */       XMLDocumentScannerImpl.this.setScannerState(44);
/* 1272 */       XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fTrailingMiscDriver);
/* 1273 */       return true;
/*      */     }
/*      */ 
/*      */     protected boolean scanRootElementHook()
/*      */       throws IOException, XNIException
/*      */     {
/* 1292 */       if (XMLDocumentScannerImpl.this.scanStartElement()) {
/* 1293 */         XMLDocumentScannerImpl.this.setScannerState(44);
/* 1294 */         XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fTrailingMiscDriver);
/* 1295 */         return true;
/*      */       }
/* 1297 */       return false;
/*      */     }
/*      */ 
/*      */     protected void endOfFileHook(EOFException e)
/*      */       throws IOException, XNIException
/*      */     {
/* 1311 */       XMLDocumentScannerImpl.this.reportFatalError("PrematureEOF", null);
/*      */     }
/*      */ 
/*      */     protected void resolveExternalSubsetAndRead()
/*      */       throws IOException, XNIException
/*      */     {
/* 1320 */       XMLDocumentScannerImpl.this.fDTDDescription.setValues(null, null, XMLDocumentScannerImpl.this.fEntityManager.getCurrentResourceIdentifier().getExpandedSystemId(), null);
/* 1321 */       XMLDocumentScannerImpl.this.fDTDDescription.setRootName(XMLDocumentScannerImpl.this.fElementQName.rawname);
/* 1322 */       XMLInputSource src = XMLDocumentScannerImpl.this.fExternalSubsetResolver.getExternalSubset(XMLDocumentScannerImpl.this.fDTDDescription);
/*      */ 
/* 1324 */       if (src != null) {
/* 1325 */         XMLDocumentScannerImpl.this.fDoctypeName = XMLDocumentScannerImpl.this.fElementQName.rawname;
/* 1326 */         XMLDocumentScannerImpl.this.fDoctypePublicId = src.getPublicId();
/* 1327 */         XMLDocumentScannerImpl.this.fDoctypeSystemId = src.getSystemId();
/*      */ 
/* 1329 */         if (XMLDocumentScannerImpl.this.fDocumentHandler != null)
/*      */         {
/* 1332 */           XMLDocumentScannerImpl.this.fDocumentHandler.doctypeDecl(XMLDocumentScannerImpl.this.fDoctypeName, XMLDocumentScannerImpl.this.fDoctypePublicId, XMLDocumentScannerImpl.this.fDoctypeSystemId, null);
/*      */         }
/*      */         try { XMLDocumentScannerImpl.this.fDTDScanner.setInputSource(src);
/* 1336 */           while (XMLDocumentScannerImpl.this.fDTDScanner.scanDTDExternalSubset(true));
/*      */         } finally {
/* 1338 */           XMLDocumentScannerImpl.this.fEntityManager.setEntityHandler(XMLDocumentScannerImpl.this);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final class DTDDriver
/*      */     implements XMLDocumentFragmentScannerImpl.Driver
/*      */   {
/*      */     protected DTDDriver()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next()
/*      */       throws IOException, XNIException
/*      */     {
/* 1050 */       dispatch(true);
/*      */ 
/* 1058 */       if (XMLDocumentScannerImpl.this.fPropertyManager != null) {
/* 1059 */         XMLDocumentScannerImpl.this.dtdGrammarUtil = new DTDGrammarUtil(((XMLDTDScannerImpl)XMLDocumentScannerImpl.this.fDTDScanner).getGrammar(), XMLDocumentScannerImpl.this.fSymbolTable, XMLDocumentScannerImpl.this.fNamespaceContext);
/*      */       }
/*      */ 
/* 1062 */       return 11;
/*      */     }
/*      */ 
/*      */     public boolean dispatch(boolean complete)
/*      */       throws IOException, XNIException
/*      */     {
/* 1079 */       XMLDocumentScannerImpl.this.fEntityManager.setEntityHandler(null);
/*      */       try
/*      */       {
/* 1082 */         resourceIdentifier = new XMLResourceIdentifierImpl();
/* 1083 */         if (XMLDocumentScannerImpl.this.fDTDScanner == null)
/*      */         {
/* 1085 */           if ((XMLDocumentScannerImpl.this.fEntityManager.getEntityScanner() instanceof XML11EntityScanner)) {
/* 1086 */             XMLDocumentScannerImpl.this.fDTDScanner = new XML11DTDScannerImpl();
/*      */           }
/*      */           else {
/* 1089 */             XMLDocumentScannerImpl.this.fDTDScanner = new XMLDTDScannerImpl();
/*      */           }
/* 1091 */           ((XMLDTDScannerImpl)XMLDocumentScannerImpl.this.fDTDScanner).reset(XMLDocumentScannerImpl.this.fPropertyManager);
/* 1094 */         }
/*      */ XMLDocumentScannerImpl.this.fDTDScanner.setLimitAnalyzer(XMLDocumentScannerImpl.this.fLimitAnalyzer);
/*      */         boolean again;
/*      */         do { again = false;
/*      */           String accessError;
/*      */           boolean completeDTD;
/* 1097 */           switch (XMLDocumentScannerImpl.this.fScannerState)
/*      */           {
/*      */           case 45:
/* 1101 */             boolean completeDTD = true;
/*      */ 
/* 1103 */             boolean moreToScan = XMLDocumentScannerImpl.this.fDTDScanner.scanDTDInternalSubset(completeDTD, XMLDocumentScannerImpl.this.fStandalone, (XMLDocumentScannerImpl.this.fHasExternalDTD) && (XMLDocumentScannerImpl.this.fLoadExternalDTD));
/* 1104 */             Entity entity = XMLDocumentScannerImpl.this.fEntityScanner.getCurrentEntity();
/* 1105 */             if ((entity instanceof Entity.ScannedEntity)) {
/* 1106 */               XMLDocumentScannerImpl.this.fEndPos = ((Entity.ScannedEntity)entity).position;
/*      */             }
/* 1108 */             XMLDocumentScannerImpl.this.fReadingDTD = false;
/* 1109 */             if (!moreToScan)
/*      */             {
/* 1111 */               if (!XMLDocumentScannerImpl.this.fEntityScanner.skipChar(93)) {
/* 1112 */                 XMLDocumentScannerImpl.this.reportFatalError("EXPECTED_SQUARE_BRACKET_TO_CLOSE_INTERNAL_SUBSET", null);
/*      */               }
/*      */ 
/* 1115 */               XMLDocumentScannerImpl.this.fEntityScanner.skipSpaces();
/* 1116 */               if (!XMLDocumentScannerImpl.this.fEntityScanner.skipChar(62)) {
/* 1117 */                 XMLDocumentScannerImpl.this.reportFatalError("DoctypedeclUnterminated", new Object[] { XMLDocumentScannerImpl.this.fDoctypeName });
/*      */               }
/* 1119 */               XMLDocumentScannerImpl.this.fMarkupDepth -= 1;
/*      */ 
/* 1121 */               if (!XMLDocumentScannerImpl.this.fSupportDTD)
/*      */               {
/* 1124 */                 XMLDocumentScannerImpl.this.fEntityStore = XMLDocumentScannerImpl.this.fEntityManager.getEntityStore();
/* 1125 */                 XMLDocumentScannerImpl.this.fEntityStore.reset();
/*      */               }
/* 1128 */               else if ((XMLDocumentScannerImpl.this.fDoctypeSystemId != null) && ((XMLDocumentScannerImpl.this.fValidation) || (XMLDocumentScannerImpl.this.fLoadExternalDTD))) {
/* 1129 */                 XMLDocumentScannerImpl.this.setScannerState(46);
/* 1130 */                 continue;
/*      */               }
/*      */ 
/* 1134 */               XMLDocumentScannerImpl.this.setEndDTDScanState();
/* 1135 */               return true;
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 46:
/* 1152 */             resourceIdentifier.setValues(XMLDocumentScannerImpl.this.fDoctypePublicId, XMLDocumentScannerImpl.this.fDoctypeSystemId, null, null);
/* 1153 */             XMLInputSource xmlInputSource = null;
/* 1154 */             StaxXMLInputSource staxInputSource = XMLDocumentScannerImpl.this.fEntityManager.resolveEntityAsPerStax(resourceIdentifier);
/*      */ 
/* 1157 */             if (!staxInputSource.hasResolver()) {
/* 1158 */               accessError = XMLDocumentScannerImpl.this.checkAccess(XMLDocumentScannerImpl.this.fDoctypeSystemId, XMLDocumentScannerImpl.this.fAccessExternalDTD);
/* 1159 */               if (accessError != null) {
/* 1160 */                 XMLDocumentScannerImpl.this.reportFatalError("AccessExternalDTD", new Object[] { SecuritySupport.sanitizePath(XMLDocumentScannerImpl.this.fDoctypeSystemId), accessError });
/*      */               }
/*      */             }
/* 1163 */             xmlInputSource = staxInputSource.getXMLInputSource();
/* 1164 */             XMLDocumentScannerImpl.this.fDTDScanner.setInputSource(xmlInputSource);
/* 1165 */             if (XMLDocumentScannerImpl.this.fEntityScanner.fCurrentEntity != null)
/* 1166 */               XMLDocumentScannerImpl.this.setScannerState(47);
/*      */             else {
/* 1168 */               XMLDocumentScannerImpl.this.setScannerState(43);
/*      */             }
/* 1170 */             again = true;
/* 1171 */             break;
/*      */           case 47:
/* 1176 */             completeDTD = true;
/* 1177 */             boolean moreToScan = XMLDocumentScannerImpl.this.fDTDScanner.scanDTDExternalSubset(completeDTD);
/* 1178 */             if (!moreToScan) {
/* 1179 */               XMLDocumentScannerImpl.this.setEndDTDScanState();
/* 1180 */               return 1;
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 43:
/* 1186 */             XMLDocumentScannerImpl.this.setEndDTDScanState();
/* 1187 */             return true;
/*      */           case 44:
/*      */           default:
/* 1190 */             throw new XNIException("DTDDriver#dispatch: scanner state=" + XMLDocumentScannerImpl.this.fScannerState + " (" + XMLDocumentScannerImpl.this.getScannerStateName(XMLDocumentScannerImpl.this.fScannerState) + ')');
/*      */           }
/*      */         }
/* 1193 */         while ((complete) || (again));
/*      */       }
/*      */       catch (EOFException e)
/*      */       {
/*      */         XMLResourceIdentifierImpl resourceIdentifier;
/* 1198 */         e.printStackTrace();
/* 1199 */         XMLDocumentScannerImpl.this.reportFatalError("PrematureEOF", null);
/* 1200 */         return 0;
/*      */       }
/*      */       finally
/*      */       {
/* 1206 */         XMLDocumentScannerImpl.this.fEntityManager.setEntityHandler(XMLDocumentScannerImpl.this);
/*      */       }
/*      */ 
/* 1209 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final class PrologDriver
/*      */     implements XMLDocumentFragmentScannerImpl.Driver
/*      */   {
/*      */     protected PrologDriver()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next()
/*      */       throws IOException, XNIException
/*      */     {
/*      */       try
/*      */       {
/*      */         do {
/*  836 */           switch (XMLDocumentScannerImpl.this.fScannerState) {
/*      */           case 43:
/*  838 */             XMLDocumentScannerImpl.this.fEntityScanner.skipSpaces();
/*  839 */             if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(60))
/*  840 */               XMLDocumentScannerImpl.this.setScannerState(21);
/*  841 */             else if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(38))
/*  842 */               XMLDocumentScannerImpl.this.setScannerState(28);
/*      */             else {
/*  844 */               XMLDocumentScannerImpl.this.setScannerState(22);
/*      */             }
/*  846 */             break;
/*      */           case 21:
/*  850 */             XMLDocumentScannerImpl.this.fMarkupDepth += 1;
/*      */ 
/*  852 */             if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(63)) {
/*  853 */               XMLDocumentScannerImpl.this.setScannerState(23);
/*  854 */             } else if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(33)) {
/*  855 */               if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(45)) {
/*  856 */                 if (!XMLDocumentScannerImpl.this.fEntityScanner.skipChar(45)) {
/*  857 */                   XMLDocumentScannerImpl.this.reportFatalError("InvalidCommentStart", null);
/*      */                 }
/*      */ 
/*  860 */                 XMLDocumentScannerImpl.this.setScannerState(27);
/*  861 */               } else if (XMLDocumentScannerImpl.this.fEntityScanner.skipString(XMLDocumentScannerImpl.DOCTYPE)) {
/*  862 */                 XMLDocumentScannerImpl.this.setScannerState(24);
/*  863 */                 Entity entity = XMLDocumentScannerImpl.this.fEntityScanner.getCurrentEntity();
/*  864 */                 if ((entity instanceof Entity.ScannedEntity)) {
/*  865 */                   XMLDocumentScannerImpl.this.fStartPos = ((Entity.ScannedEntity)entity).position;
/*      */                 }
/*  867 */                 XMLDocumentScannerImpl.this.fReadingDTD = true;
/*  868 */                 if (XMLDocumentScannerImpl.this.fDTDDecl == null)
/*  869 */                   XMLDocumentScannerImpl.this.fDTDDecl = new XMLStringBuffer();
/*  870 */                 XMLDocumentScannerImpl.this.fDTDDecl.append("<!DOCTYPE");
/*      */               }
/*      */               else {
/*  873 */                 XMLDocumentScannerImpl.this.reportFatalError("MarkupNotRecognizedInProlog", null);
/*      */               }
/*      */             } else {
/*  876 */               if (XMLChar.isNameStart(XMLDocumentScannerImpl.this.fEntityScanner.peekChar())) {
/*  877 */                 XMLDocumentScannerImpl.this.setScannerState(26);
/*  878 */                 XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fContentDriver);
/*      */ 
/*  880 */                 return XMLDocumentScannerImpl.this.fContentDriver.next();
/*      */               }
/*      */ 
/*  883 */               XMLDocumentScannerImpl.this.reportFatalError("MarkupNotRecognizedInProlog", null);
/*      */             }
/*      */             break;
/*      */           }
/*      */         }
/*      */ 
/*  889 */         while ((XMLDocumentScannerImpl.this.fScannerState == 43) || (XMLDocumentScannerImpl.this.fScannerState == 21));
/*      */ 
/*  891 */         switch (XMLDocumentScannerImpl.this.fScannerState)
/*      */         {
/*      */         case 27:
/*  904 */           XMLDocumentScannerImpl.this.scanComment();
/*  905 */           XMLDocumentScannerImpl.this.setScannerState(43);
/*  906 */           return 5;
/*      */         case 23:
/*  911 */           XMLDocumentScannerImpl.this.fContentBuffer.clear();
/*  912 */           XMLDocumentScannerImpl.this.scanPI(XMLDocumentScannerImpl.this.fContentBuffer);
/*  913 */           XMLDocumentScannerImpl.this.setScannerState(43);
/*  914 */           return 3;
/*      */         case 24:
/*  918 */           if (XMLDocumentScannerImpl.this.fDisallowDoctype) {
/*  919 */             XMLDocumentScannerImpl.this.reportFatalError("DoctypeNotAllowed", null);
/*      */           }
/*      */ 
/*  922 */           if (XMLDocumentScannerImpl.this.fSeenDoctypeDecl) {
/*  923 */             XMLDocumentScannerImpl.this.reportFatalError("AlreadySeenDoctype", null);
/*      */           }
/*  925 */           XMLDocumentScannerImpl.this.fSeenDoctypeDecl = true;
/*      */ 
/*  929 */           if (XMLDocumentScannerImpl.this.scanDoctypeDecl(XMLDocumentScannerImpl.this.fSupportDTD))
/*      */           {
/*  931 */             XMLDocumentScannerImpl.this.setScannerState(45);
/*  932 */             XMLDocumentScannerImpl.this.fSeenInternalSubset = true;
/*  933 */             if (XMLDocumentScannerImpl.this.fDTDDriver == null) {
/*  934 */               XMLDocumentScannerImpl.this.fDTDDriver = new XMLDocumentScannerImpl.DTDDriver(XMLDocumentScannerImpl.this);
/*      */             }
/*  936 */             XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fContentDriver);
/*      */ 
/*  938 */             return XMLDocumentScannerImpl.this.fDTDDriver.next();
/*      */           }
/*      */ 
/*  941 */           if (XMLDocumentScannerImpl.this.fSeenDoctypeDecl) {
/*  942 */             Entity entity = XMLDocumentScannerImpl.this.fEntityScanner.getCurrentEntity();
/*  943 */             if ((entity instanceof Entity.ScannedEntity)) {
/*  944 */               XMLDocumentScannerImpl.this.fEndPos = ((Entity.ScannedEntity)entity).position;
/*      */             }
/*  946 */             XMLDocumentScannerImpl.this.fReadingDTD = false;
/*      */           }
/*      */ 
/*  950 */           if (XMLDocumentScannerImpl.this.fDoctypeSystemId != null) {
/*  951 */             if (((XMLDocumentScannerImpl.this.fValidation) || (XMLDocumentScannerImpl.this.fLoadExternalDTD)) && ((XMLDocumentScannerImpl.this.fValidationManager == null) || (!XMLDocumentScannerImpl.this.fValidationManager.isCachedDTD())))
/*      */             {
/*  953 */               if (XMLDocumentScannerImpl.this.fSupportDTD)
/*  954 */                 XMLDocumentScannerImpl.this.setScannerState(46);
/*      */               else {
/*  956 */                 XMLDocumentScannerImpl.this.setScannerState(43);
/*      */               }
/*      */ 
/*  959 */               XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fContentDriver);
/*  960 */               if (XMLDocumentScannerImpl.this.fDTDDriver == null) {
/*  961 */                 XMLDocumentScannerImpl.this.fDTDDriver = new XMLDocumentScannerImpl.DTDDriver(XMLDocumentScannerImpl.this);
/*      */               }
/*      */ 
/*  964 */               return XMLDocumentScannerImpl.this.fDTDDriver.next();
/*      */             }
/*      */           }
/*  967 */           else if ((XMLDocumentScannerImpl.this.fExternalSubsetSource != null) && 
/*  968 */             ((XMLDocumentScannerImpl.this.fValidation) || (XMLDocumentScannerImpl.this.fLoadExternalDTD)) && (
/*  968 */             (XMLDocumentScannerImpl.this.fValidationManager == null) || (!XMLDocumentScannerImpl.this.fValidationManager.isCachedDTD())))
/*      */           {
/*  971 */             XMLDocumentScannerImpl.this.fDTDScanner.setInputSource(XMLDocumentScannerImpl.this.fExternalSubsetSource);
/*  972 */             XMLDocumentScannerImpl.this.fExternalSubsetSource = null;
/*  973 */             if (XMLDocumentScannerImpl.this.fSupportDTD)
/*  974 */               XMLDocumentScannerImpl.this.setScannerState(47);
/*      */             else
/*  976 */               XMLDocumentScannerImpl.this.setScannerState(43);
/*  977 */             XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fContentDriver);
/*  978 */             if (XMLDocumentScannerImpl.this.fDTDDriver == null)
/*  979 */               XMLDocumentScannerImpl.this.fDTDDriver = new XMLDocumentScannerImpl.DTDDriver(XMLDocumentScannerImpl.this);
/*  980 */             return XMLDocumentScannerImpl.this.fDTDDriver.next();
/*      */           }
/*      */ 
/*  991 */           if (XMLDocumentScannerImpl.this.fDTDScanner != null) {
/*  992 */             XMLDocumentScannerImpl.this.fDTDScanner.setInputSource(null);
/*      */           }
/*  994 */           XMLDocumentScannerImpl.this.setScannerState(43);
/*  995 */           return 11;
/*      */         case 22:
/*  999 */           XMLDocumentScannerImpl.this.reportFatalError("ContentIllegalInProlog", null);
/* 1000 */           XMLDocumentScannerImpl.this.fEntityScanner.scanChar();
/*      */         case 28:
/* 1003 */           XMLDocumentScannerImpl.this.reportFatalError("ReferenceIllegalInProlog", null);
/*      */         case 25:
/*      */         case 26:
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (EOFException e)
/*      */       {
/* 1019 */         XMLDocumentScannerImpl.this.reportFatalError("PrematureEOF", null);
/*      */ 
/* 1021 */         return -1;
/*      */       }
/*      */ 
/* 1025 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final class TrailingMiscDriver
/*      */     implements XMLDocumentFragmentScannerImpl.Driver
/*      */   {
/*      */     protected TrailingMiscDriver()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next()
/*      */       throws IOException, XNIException
/*      */     {
/* 1362 */       if (XMLDocumentScannerImpl.this.fEmptyElement) {
/* 1363 */         XMLDocumentScannerImpl.this.fEmptyElement = false;
/* 1364 */         return 2;
/*      */       }
/*      */       try
/*      */       {
/* 1368 */         if (XMLDocumentScannerImpl.this.fScannerState == 34)
/* 1369 */           return 8;
/*      */         do {
/* 1371 */           switch (XMLDocumentScannerImpl.this.fScannerState)
/*      */           {
/*      */           case 44:
/* 1374 */             XMLDocumentScannerImpl.this.fEntityScanner.skipSpaces();
/*      */ 
/* 1377 */             if (XMLDocumentScannerImpl.this.fScannerState == 34) {
/* 1378 */               return 8;
/*      */             }
/* 1380 */             if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(60))
/* 1381 */               XMLDocumentScannerImpl.this.setScannerState(21);
/*      */             else {
/* 1383 */               XMLDocumentScannerImpl.this.setScannerState(22);
/*      */             }
/* 1385 */             break;
/*      */           case 21:
/* 1388 */             XMLDocumentScannerImpl.this.fMarkupDepth += 1;
/* 1389 */             if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(63)) {
/* 1390 */               XMLDocumentScannerImpl.this.setScannerState(23);
/* 1391 */             } else if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(33)) {
/* 1392 */               XMLDocumentScannerImpl.this.setScannerState(27);
/* 1393 */             } else if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(47)) {
/* 1394 */               XMLDocumentScannerImpl.this.reportFatalError("MarkupNotRecognizedInMisc", null);
/*      */             }
/* 1396 */             else if (XMLChar.isNameStart(XMLDocumentScannerImpl.this.fEntityScanner.peekChar())) {
/* 1397 */               XMLDocumentScannerImpl.this.reportFatalError("MarkupNotRecognizedInMisc", null);
/*      */ 
/* 1399 */               XMLDocumentScannerImpl.this.scanStartElement();
/* 1400 */               XMLDocumentScannerImpl.this.setScannerState(22);
/*      */             } else {
/* 1402 */               XMLDocumentScannerImpl.this.reportFatalError("MarkupNotRecognizedInMisc", null);
/*      */             }
/*      */             break;
/*      */           }
/*      */         }
/*      */ 
/* 1408 */         while ((XMLDocumentScannerImpl.this.fScannerState == 21) || (XMLDocumentScannerImpl.this.fScannerState == 44));
/*      */ 
/* 1412 */         switch (XMLDocumentScannerImpl.this.fScannerState) {
/*      */         case 23:
/* 1414 */           XMLDocumentScannerImpl.this.fContentBuffer.clear();
/* 1415 */           XMLDocumentScannerImpl.this.scanPI(XMLDocumentScannerImpl.this.fContentBuffer);
/* 1416 */           XMLDocumentScannerImpl.this.setScannerState(44);
/* 1417 */           return 3;
/*      */         case 27:
/* 1420 */           if (!XMLDocumentScannerImpl.this.fEntityScanner.skipString(XMLDocumentScannerImpl.COMMENTSTRING)) {
/* 1421 */             XMLDocumentScannerImpl.this.reportFatalError("InvalidCommentStart", null);
/*      */           }
/* 1423 */           XMLDocumentScannerImpl.this.scanComment();
/* 1424 */           XMLDocumentScannerImpl.this.setScannerState(44);
/* 1425 */           return 5;
/*      */         case 22:
/* 1428 */           int ch = XMLDocumentScannerImpl.this.fEntityScanner.peekChar();
/* 1429 */           if (ch == -1) {
/* 1430 */             XMLDocumentScannerImpl.this.setScannerState(34);
/* 1431 */             return 8;
/*      */           }
/* 1433 */           XMLDocumentScannerImpl.this.reportFatalError("ContentIllegalInTrailingMisc", null);
/*      */ 
/* 1435 */           XMLDocumentScannerImpl.this.fEntityScanner.scanChar();
/* 1436 */           XMLDocumentScannerImpl.this.setScannerState(44);
/* 1437 */           return 4;
/*      */         case 28:
/* 1442 */           XMLDocumentScannerImpl.this.reportFatalError("ReferenceIllegalInTrailingMisc", null);
/*      */ 
/* 1444 */           XMLDocumentScannerImpl.this.setScannerState(44);
/* 1445 */           return 9;
/*      */         case 34:
/* 1450 */           XMLDocumentScannerImpl.this.setScannerState(48);
/*      */ 
/* 1452 */           return 8;
/*      */         case 48:
/* 1455 */           throw new NoSuchElementException("No more events to be parsed");
/*      */         }
/* 1457 */         throw new XNIException("Scanner State " + XMLDocumentScannerImpl.this.fScannerState + " not Recognized ");
/*      */       }
/*      */       catch (EOFException e)
/*      */       {
/* 1464 */         if (XMLDocumentScannerImpl.this.fMarkupDepth != 0) {
/* 1465 */           XMLDocumentScannerImpl.this.reportFatalError("PrematureEOF", null);
/* 1466 */           return -1;
/*      */         }
/*      */ 
/* 1470 */         XMLDocumentScannerImpl.this.setScannerState(34);
/*      */       }
/*      */ 
/* 1473 */       return 8;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final class XMLDeclDriver
/*      */     implements XMLDocumentFragmentScannerImpl.Driver
/*      */   {
/*      */     protected XMLDeclDriver()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next()
/*      */       throws IOException, XNIException
/*      */     {
/*  747 */       XMLDocumentScannerImpl.this.setScannerState(43);
/*  748 */       XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fPrologDriver);
/*      */       try
/*      */       {
/*  753 */         if (XMLDocumentScannerImpl.this.fEntityScanner.skipString(XMLDocumentFragmentScannerImpl.xmlDecl)) {
/*  754 */           XMLDocumentScannerImpl.this.fMarkupDepth += 1;
/*      */ 
/*  757 */           if (XMLChar.isName(XMLDocumentScannerImpl.this.fEntityScanner.peekChar())) {
/*  758 */             XMLDocumentScannerImpl.this.fStringBuffer.clear();
/*  759 */             XMLDocumentScannerImpl.this.fStringBuffer.append("xml");
/*  760 */             while (XMLChar.isName(XMLDocumentScannerImpl.this.fEntityScanner.peekChar())) {
/*  761 */               XMLDocumentScannerImpl.this.fStringBuffer.append((char)XMLDocumentScannerImpl.this.fEntityScanner.scanChar());
/*      */             }
/*  763 */             String target = XMLDocumentScannerImpl.this.fSymbolTable.addSymbol(XMLDocumentScannerImpl.this.fStringBuffer.ch, XMLDocumentScannerImpl.this.fStringBuffer.offset, XMLDocumentScannerImpl.this.fStringBuffer.length);
/*      */ 
/*  765 */             XMLDocumentScannerImpl.this.fContentBuffer.clear();
/*  766 */             XMLDocumentScannerImpl.this.scanPIData(target, XMLDocumentScannerImpl.this.fContentBuffer);
/*      */ 
/*  768 */             XMLDocumentScannerImpl.this.fEntityManager.fCurrentEntity.mayReadChunks = true;
/*      */ 
/*  770 */             return 3;
/*      */           }
/*      */ 
/*  774 */           XMLDocumentScannerImpl.this.scanXMLDeclOrTextDecl(false);
/*      */ 
/*  776 */           XMLDocumentScannerImpl.this.fEntityManager.fCurrentEntity.mayReadChunks = true;
/*  777 */           return 7;
/*      */         }
/*      */ 
/*  781 */         XMLDocumentScannerImpl.this.fEntityManager.fCurrentEntity.mayReadChunks = true;
/*      */ 
/*  784 */         return 7;
/*      */       }
/*      */       catch (EOFException e)
/*      */       {
/*  795 */         XMLDocumentScannerImpl.this.reportFatalError("PrematureEOF", null);
/*  796 */       }return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl
 * JD-Core Version:    0.6.2
 */