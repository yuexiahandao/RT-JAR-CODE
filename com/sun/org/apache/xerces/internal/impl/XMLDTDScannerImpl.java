/*      */ package com.sun.org.apache.xerces.internal.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.Limit;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.xml.internal.stream.Entity.ScannedEntity;
/*      */ import com.sun.xml.internal.stream.XMLEntityStorage;
/*      */ import com.sun.xml.internal.stream.dtd.nonvalidating.DTDGrammar;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ 
/*      */ public class XMLDTDScannerImpl extends XMLScanner
/*      */   implements XMLDTDScanner, XMLComponent, XMLEntityHandler
/*      */ {
/*      */   protected static final int SCANNER_STATE_END_OF_INPUT = 0;
/*      */   protected static final int SCANNER_STATE_TEXT_DECL = 1;
/*      */   protected static final int SCANNER_STATE_MARKUP_DECL = 2;
/*   96 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://apache.org/xml/features/scanner/notify-char-refs" };
/*      */ 
/*  102 */   private static final Boolean[] FEATURE_DEFAULTS = { null, Boolean.FALSE };
/*      */ 
/*  108 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager" };
/*      */ 
/*  115 */   private static final Object[] PROPERTY_DEFAULTS = { null, null, null };
/*      */   private static final boolean DEBUG_SCANNER_STATE = false;
/*  133 */   public XMLDTDHandler fDTDHandler = null;
/*      */   protected XMLDTDContentModelHandler fDTDContentModelHandler;
/*      */   protected int fScannerState;
/*      */   protected boolean fStandalone;
/*      */   protected boolean fSeenExternalDTD;
/*      */   protected boolean fSeenExternalPE;
/*      */   private boolean fStartDTDCalled;
/*  158 */   private XMLAttributesImpl fAttributes = new XMLAttributesImpl();
/*      */ 
/*  164 */   private int[] fContentStack = new int[5];
/*      */   private int fContentDepth;
/*  170 */   private int[] fPEStack = new int[5];
/*      */ 
/*  174 */   private boolean[] fPEReport = new boolean[5];
/*      */   private int fPEDepth;
/*      */   private int fMarkUpDepth;
/*      */   private int fExtEntityDepth;
/*      */   private int fIncludeSectDepth;
/*  191 */   private String[] fStrings = new String[3];
/*      */ 
/*  194 */   private XMLString fString = new XMLString();
/*      */ 
/*  197 */   private XMLStringBuffer fStringBuffer = new XMLStringBuffer();
/*      */ 
/*  200 */   private XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
/*      */ 
/*  203 */   private XMLString fLiteral = new XMLString();
/*      */ 
/*  206 */   private XMLString fLiteral2 = new XMLString();
/*      */ 
/*  209 */   private String[] fEnumeration = new String[5];
/*      */   private int fEnumerationCount;
/*  215 */   private XMLStringBuffer fIgnoreConditionalBuffer = new XMLStringBuffer(128);
/*      */ 
/*  218 */   DTDGrammar nvGrammarInfo = null;
/*      */ 
/*  220 */   boolean nonValidatingMode = false;
/*      */ 
/*      */   public XMLDTDScannerImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   public XMLDTDScannerImpl(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityManager)
/*      */   {
/*  232 */     this.fSymbolTable = symbolTable;
/*  233 */     this.fErrorReporter = errorReporter;
/*  234 */     this.fEntityManager = entityManager;
/*  235 */     entityManager.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/*      */   }
/*      */ 
/*      */   public void setInputSource(XMLInputSource inputSource)
/*      */     throws IOException
/*      */   {
/*  250 */     if (inputSource == null)
/*      */     {
/*  252 */       if (this.fDTDHandler != null) {
/*  253 */         this.fDTDHandler.startDTD(null, null);
/*  254 */         this.fDTDHandler.endDTD(null);
/*      */       }
/*  256 */       if (this.nonValidatingMode) {
/*  257 */         this.nvGrammarInfo.startDTD(null, null);
/*  258 */         this.nvGrammarInfo.endDTD(null);
/*      */       }
/*  260 */       return;
/*      */     }
/*  262 */     this.fEntityManager.setEntityHandler(this);
/*  263 */     this.fEntityManager.startDTDEntity(inputSource);
/*      */   }
/*      */ 
/*      */   public void setLimitAnalyzer(XMLLimitAnalyzer limitAnalyzer)
/*      */   {
/*  268 */     this.fLimitAnalyzer = limitAnalyzer;
/*      */   }
/*      */ 
/*      */   public boolean scanDTDExternalSubset(boolean complete)
/*      */     throws IOException, XNIException
/*      */   {
/*  287 */     this.fEntityManager.setEntityHandler(this);
/*  288 */     if (this.fScannerState == 1) {
/*  289 */       this.fSeenExternalDTD = true;
/*  290 */       boolean textDecl = scanTextDecl();
/*  291 */       if (this.fScannerState == 0) {
/*  292 */         return false;
/*      */       }
/*      */ 
/*  297 */       setScannerState(2);
/*  298 */       if ((textDecl) && (!complete)) {
/*  299 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*      */     do
/*      */     {
/*  305 */       if (!scanDecls(complete))
/*  306 */         return false;
/*      */     }
/*  308 */     while (complete);
/*      */ 
/*  311 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean scanDTDInternalSubset(boolean complete, boolean standalone, boolean hasExternalSubset)
/*      */     throws IOException, XNIException
/*      */   {
/*  342 */     this.fEntityScanner = this.fEntityManager.getEntityScanner();
/*  343 */     this.fEntityManager.setEntityHandler(this);
/*  344 */     this.fStandalone = standalone;
/*      */ 
/*  346 */     if (this.fScannerState == 1)
/*      */     {
/*  348 */       if (this.fDTDHandler != null) {
/*  349 */         this.fDTDHandler.startDTD(this.fEntityScanner, null);
/*  350 */         this.fStartDTDCalled = true;
/*      */       }
/*      */ 
/*  353 */       if (this.nonValidatingMode) {
/*  354 */         this.fStartDTDCalled = true;
/*  355 */         this.nvGrammarInfo.startDTD(this.fEntityScanner, null);
/*      */       }
/*      */ 
/*  358 */       setScannerState(2);
/*      */     }
/*      */     do
/*      */     {
/*  362 */       if (!scanDecls(complete))
/*      */       {
/*  364 */         if ((this.fDTDHandler != null) && (!hasExternalSubset)) {
/*  365 */           this.fDTDHandler.endDTD(null);
/*      */         }
/*  367 */         if ((this.nonValidatingMode) && (!hasExternalSubset)) {
/*  368 */           this.nvGrammarInfo.endDTD(null);
/*      */         }
/*      */ 
/*  371 */         setScannerState(1);
/*  372 */         return false;
/*      */       }
/*      */     }
/*  374 */     while (complete);
/*      */ 
/*  377 */     return true;
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/*  393 */     super.reset(componentManager);
/*  394 */     init();
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  400 */     super.reset();
/*  401 */     init();
/*      */   }
/*      */ 
/*      */   public void reset(PropertyManager props)
/*      */   {
/*  406 */     setPropertyManager(props);
/*  407 */     super.reset(props);
/*  408 */     init();
/*  409 */     this.nonValidatingMode = true;
/*      */ 
/*  411 */     this.nvGrammarInfo = new DTDGrammar(this.fSymbolTable);
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/*  419 */     return (String[])RECOGNIZED_FEATURES.clone();
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/*  428 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/*  441 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/*  442 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/*  443 */         return FEATURE_DEFAULTS[i];
/*      */       }
/*      */     }
/*  446 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/*  459 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/*  460 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/*  461 */         return PROPERTY_DEFAULTS[i];
/*      */       }
/*      */     }
/*  464 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(XMLDTDHandler dtdHandler)
/*      */   {
/*  477 */     this.fDTDHandler = dtdHandler;
/*      */   }
/*      */ 
/*      */   public XMLDTDHandler getDTDHandler()
/*      */   {
/*  486 */     return this.fDTDHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDContentModelHandler(XMLDTDContentModelHandler dtdContentModelHandler)
/*      */   {
/*  500 */     this.fDTDContentModelHandler = dtdContentModelHandler;
/*      */   }
/*      */ 
/*      */   public XMLDTDContentModelHandler getDTDContentModelHandler()
/*      */   {
/*  509 */     return this.fDTDContentModelHandler;
/*      */   }
/*      */ 
/*      */   public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  536 */     super.startEntity(name, identifier, encoding, augs);
/*      */ 
/*  538 */     boolean dtdEntity = name.equals("[dtd]");
/*  539 */     if (dtdEntity)
/*      */     {
/*  541 */       if ((this.fDTDHandler != null) && (!this.fStartDTDCalled)) {
/*  542 */         this.fDTDHandler.startDTD(this.fEntityScanner, null);
/*      */       }
/*  544 */       if (this.fDTDHandler != null) {
/*  545 */         this.fDTDHandler.startExternalSubset(identifier, null);
/*      */       }
/*  547 */       this.fEntityManager.startExternalSubset();
/*  548 */       this.fEntityStore.startExternalSubset();
/*  549 */       this.fExtEntityDepth += 1;
/*      */     }
/*  551 */     else if (name.charAt(0) == '%') {
/*  552 */       pushPEStack(this.fMarkUpDepth, this.fReportEntity);
/*  553 */       if (this.fEntityScanner.isExternal()) {
/*  554 */         this.fExtEntityDepth += 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  559 */     if ((this.fDTDHandler != null) && (!dtdEntity) && (this.fReportEntity))
/*  560 */       this.fDTDHandler.startParameterEntity(name, identifier, encoding, null);
/*      */   }
/*      */ 
/*      */   public void endEntity(String name, Augmentations augs)
/*      */     throws XNIException, IOException
/*      */   {
/*  577 */     super.endEntity(name, augs);
/*      */ 
/*  581 */     if (this.fScannerState == 0) {
/*  582 */       return;
/*      */     }
/*      */ 
/*  585 */     boolean reportEntity = this.fReportEntity;
/*  586 */     if (name.startsWith("%")) {
/*  587 */       reportEntity = peekReportEntity();
/*      */ 
/*  589 */       int startMarkUpDepth = popPEStack();
/*      */ 
/*  592 */       if ((startMarkUpDepth == 0) && (startMarkUpDepth < this.fMarkUpDepth))
/*      */       {
/*  594 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ILL_FORMED_PARAMETER_ENTITY_WHEN_USED_IN_DECL", new Object[] { this.fEntityManager.fCurrentEntity.name }, (short)2);
/*      */       }
/*      */ 
/*  599 */       if (startMarkUpDepth != this.fMarkUpDepth) {
/*  600 */         reportEntity = false;
/*  601 */         if (this.fValidation)
/*      */         {
/*  604 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ImproperDeclarationNesting", new Object[] { name }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  610 */       if (this.fEntityScanner.isExternal()) {
/*  611 */         this.fExtEntityDepth -= 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  616 */     boolean dtdEntity = name.equals("[dtd]");
/*  617 */     if ((this.fDTDHandler != null) && (!dtdEntity) && (reportEntity)) {
/*  618 */       this.fDTDHandler.endParameterEntity(name, null);
/*      */     }
/*      */ 
/*  622 */     if (dtdEntity) {
/*  623 */       if (this.fIncludeSectDepth != 0) {
/*  624 */         reportFatalError("IncludeSectUnterminated", null);
/*      */       }
/*  626 */       this.fScannerState = 0;
/*      */ 
/*  628 */       this.fEntityManager.endExternalSubset();
/*  629 */       this.fEntityStore.endExternalSubset();
/*      */ 
/*  631 */       if (this.fDTDHandler != null) {
/*  632 */         this.fDTDHandler.endExternalSubset(null);
/*  633 */         this.fDTDHandler.endDTD(null);
/*      */       }
/*  635 */       this.fExtEntityDepth -= 1;
/*      */     }
/*      */ 
/*  645 */     if ((augs != null) && (Boolean.TRUE.equals(augs.getItem("LAST_ENTITY"))) && ((this.fMarkUpDepth != 0) || (this.fExtEntityDepth != 0) || (this.fIncludeSectDepth != 0)))
/*      */     {
/*  647 */       throw new EOFException();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void setScannerState(int state)
/*      */   {
/*  661 */     this.fScannerState = state;
/*      */   }
/*      */ 
/*      */   private static String getScannerStateName(int state)
/*      */   {
/*  685 */     return "??? (" + state + ')';
/*      */   }
/*      */ 
/*      */   protected final boolean scanningInternalSubset()
/*      */   {
/*  690 */     return this.fExtEntityDepth == 0;
/*      */   }
/*      */ 
/*      */   protected void startPE(String name, boolean literal)
/*      */     throws IOException, XNIException
/*      */   {
/*  701 */     int depth = this.fPEDepth;
/*  702 */     String pName = "%" + name;
/*  703 */     if ((this.fValidation) && (!this.fEntityStore.isDeclaredEntity(pName))) {
/*  704 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[] { name }, (short)1);
/*      */     }
/*      */ 
/*  707 */     this.fEntityManager.startEntity(this.fSymbolTable.addSymbol(pName), literal);
/*      */ 
/*  711 */     if ((depth != this.fPEDepth) && (this.fEntityScanner.isExternal()))
/*  712 */       scanTextDecl();
/*      */   }
/*      */ 
/*      */   protected final boolean scanTextDecl()
/*      */     throws IOException, XNIException
/*      */   {
/*  732 */     boolean textDecl = false;
/*  733 */     if (this.fEntityScanner.skipString("<?xml")) {
/*  734 */       this.fMarkUpDepth += 1;
/*      */ 
/*  737 */       if (isValidNameChar(this.fEntityScanner.peekChar())) {
/*  738 */         this.fStringBuffer.clear();
/*  739 */         this.fStringBuffer.append("xml");
/*  740 */         while (isValidNameChar(this.fEntityScanner.peekChar())) {
/*  741 */           this.fStringBuffer.append((char)this.fEntityScanner.scanChar());
/*      */         }
/*  743 */         String target = this.fSymbolTable.addSymbol(this.fStringBuffer.ch, this.fStringBuffer.offset, this.fStringBuffer.length);
/*      */ 
/*  747 */         scanPIData(target, this.fString);
/*      */       }
/*      */       else
/*      */       {
/*  753 */         String version = null;
/*  754 */         String encoding = null;
/*      */ 
/*  756 */         scanXMLDeclOrTextDecl(true, this.fStrings);
/*  757 */         textDecl = true;
/*  758 */         this.fMarkUpDepth -= 1;
/*      */ 
/*  760 */         version = this.fStrings[0];
/*  761 */         encoding = this.fStrings[1];
/*      */ 
/*  763 */         this.fEntityScanner.setEncoding(encoding);
/*      */ 
/*  766 */         if (this.fDTDHandler != null) {
/*  767 */           this.fDTDHandler.textDecl(version, encoding, null);
/*      */         }
/*      */       }
/*      */     }
/*  771 */     this.fEntityManager.fCurrentEntity.mayReadChunks = true;
/*      */ 
/*  773 */     return textDecl;
/*      */   }
/*      */ 
/*      */   protected final void scanPIData(String target, XMLString data)
/*      */     throws IOException, XNIException
/*      */   {
/*  789 */     this.fMarkUpDepth -= 1;
/*      */ 
/*  792 */     if (this.fDTDHandler != null)
/*  793 */       this.fDTDHandler.processingInstruction(target, data, null);
/*      */   }
/*      */ 
/*      */   protected final void scanComment()
/*      */     throws IOException, XNIException
/*      */   {
/*  809 */     this.fReportEntity = false;
/*  810 */     scanComment(this.fStringBuffer);
/*  811 */     this.fMarkUpDepth -= 1;
/*      */ 
/*  814 */     if (this.fDTDHandler != null) {
/*  815 */       this.fDTDHandler.comment(this.fStringBuffer, null);
/*      */     }
/*  817 */     this.fReportEntity = true;
/*      */   }
/*      */ 
/*      */   protected final void scanElementDecl()
/*      */     throws IOException, XNIException
/*      */   {
/*  834 */     this.fReportEntity = false;
/*  835 */     if (!skipSeparator(true, !scanningInternalSubset())) {
/*  836 */       reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ELEMENTDECL", null);
/*      */     }
/*      */ 
/*  841 */     String name = this.fEntityScanner.scanName();
/*  842 */     if (name == null) {
/*  843 */       reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_ELEMENTDECL", null);
/*      */     }
/*      */ 
/*  848 */     if (!skipSeparator(true, !scanningInternalSubset())) {
/*  849 */       reportFatalError("MSG_SPACE_REQUIRED_BEFORE_CONTENTSPEC_IN_ELEMENTDECL", new Object[] { name });
/*      */     }
/*      */ 
/*  854 */     if (this.fDTDContentModelHandler != null) {
/*  855 */       this.fDTDContentModelHandler.startContentModel(name, null);
/*      */     }
/*  857 */     String contentModel = null;
/*  858 */     this.fReportEntity = true;
/*  859 */     if (this.fEntityScanner.skipString("EMPTY")) {
/*  860 */       contentModel = "EMPTY";
/*      */ 
/*  862 */       if (this.fDTDContentModelHandler != null) {
/*  863 */         this.fDTDContentModelHandler.empty(null);
/*      */       }
/*      */     }
/*  866 */     else if (this.fEntityScanner.skipString("ANY")) {
/*  867 */       contentModel = "ANY";
/*      */ 
/*  869 */       if (this.fDTDContentModelHandler != null)
/*  870 */         this.fDTDContentModelHandler.any(null);
/*      */     }
/*      */     else
/*      */     {
/*  874 */       if (!this.fEntityScanner.skipChar(40)) {
/*  875 */         reportFatalError("MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN", new Object[] { name });
/*      */       }
/*      */ 
/*  878 */       if (this.fDTDContentModelHandler != null) {
/*  879 */         this.fDTDContentModelHandler.startGroup(null);
/*      */       }
/*  881 */       this.fStringBuffer.clear();
/*  882 */       this.fStringBuffer.append('(');
/*  883 */       this.fMarkUpDepth += 1;
/*  884 */       skipSeparator(false, !scanningInternalSubset());
/*      */ 
/*  887 */       if (this.fEntityScanner.skipString("#PCDATA")) {
/*  888 */         scanMixed(name);
/*      */       }
/*      */       else {
/*  891 */         scanChildren(name);
/*      */       }
/*  893 */       contentModel = this.fStringBuffer.toString();
/*      */     }
/*      */ 
/*  897 */     if (this.fDTDContentModelHandler != null) {
/*  898 */       this.fDTDContentModelHandler.endContentModel(null);
/*      */     }
/*      */ 
/*  901 */     this.fReportEntity = false;
/*  902 */     skipSeparator(false, !scanningInternalSubset());
/*      */ 
/*  904 */     if (!this.fEntityScanner.skipChar(62)) {
/*  905 */       reportFatalError("ElementDeclUnterminated", new Object[] { name });
/*      */     }
/*  907 */     this.fReportEntity = true;
/*  908 */     this.fMarkUpDepth -= 1;
/*      */ 
/*  911 */     if (this.fDTDHandler != null) {
/*  912 */       this.fDTDHandler.elementDecl(name, contentModel, null);
/*      */     }
/*  914 */     if (this.nonValidatingMode) this.nvGrammarInfo.elementDecl(name, contentModel, null);
/*      */   }
/*      */ 
/*      */   private final void scanMixed(String elName)
/*      */     throws IOException, XNIException
/*      */   {
/*  933 */     String childName = null;
/*      */ 
/*  935 */     this.fStringBuffer.append("#PCDATA");
/*      */ 
/*  937 */     if (this.fDTDContentModelHandler != null) {
/*  938 */       this.fDTDContentModelHandler.pcdata(null);
/*      */     }
/*  940 */     skipSeparator(false, !scanningInternalSubset());
/*  941 */     while (this.fEntityScanner.skipChar(124)) {
/*  942 */       this.fStringBuffer.append('|');
/*      */ 
/*  944 */       if (this.fDTDContentModelHandler != null) {
/*  945 */         this.fDTDContentModelHandler.separator((short)0, null);
/*      */       }
/*      */ 
/*  948 */       skipSeparator(false, !scanningInternalSubset());
/*      */ 
/*  950 */       childName = this.fEntityScanner.scanName();
/*  951 */       if (childName == null) {
/*  952 */         reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_MIXED_CONTENT", new Object[] { elName });
/*      */       }
/*      */ 
/*  955 */       this.fStringBuffer.append(childName);
/*      */ 
/*  957 */       if (this.fDTDContentModelHandler != null) {
/*  958 */         this.fDTDContentModelHandler.element(childName, null);
/*      */       }
/*  960 */       skipSeparator(false, !scanningInternalSubset());
/*      */     }
/*      */ 
/*  966 */     if (this.fEntityScanner.skipString(")*")) {
/*  967 */       this.fStringBuffer.append(")*");
/*      */ 
/*  969 */       if (this.fDTDContentModelHandler != null) {
/*  970 */         this.fDTDContentModelHandler.endGroup(null);
/*  971 */         this.fDTDContentModelHandler.occurrence((short)3, null);
/*      */       }
/*      */ 
/*      */     }
/*  975 */     else if (childName != null) {
/*  976 */       reportFatalError("MixedContentUnterminated", new Object[] { elName });
/*      */     }
/*  979 */     else if (this.fEntityScanner.skipChar(41)) {
/*  980 */       this.fStringBuffer.append(')');
/*      */ 
/*  982 */       if (this.fDTDContentModelHandler != null)
/*  983 */         this.fDTDContentModelHandler.endGroup(null);
/*      */     }
/*      */     else
/*      */     {
/*  987 */       reportFatalError("MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN", new Object[] { elName });
/*      */     }
/*      */ 
/*  990 */     this.fMarkUpDepth -= 1;
/*      */   }
/*      */ 
/*      */   private final void scanChildren(String elName)
/*      */     throws IOException, XNIException
/*      */   {
/* 1012 */     this.fContentDepth = 0;
/* 1013 */     pushContentStack(0);
/* 1014 */     int currentOp = 0;
/*      */     while (true)
/*      */     {
/* 1017 */       if (this.fEntityScanner.skipChar(40)) {
/* 1018 */         this.fMarkUpDepth += 1;
/* 1019 */         this.fStringBuffer.append('(');
/*      */ 
/* 1021 */         if (this.fDTDContentModelHandler != null) {
/* 1022 */           this.fDTDContentModelHandler.startGroup(null);
/*      */         }
/*      */ 
/* 1025 */         pushContentStack(currentOp);
/* 1026 */         currentOp = 0;
/* 1027 */         skipSeparator(false, !scanningInternalSubset());
/*      */       }
/*      */       else {
/* 1030 */         skipSeparator(false, !scanningInternalSubset());
/* 1031 */         String childName = this.fEntityScanner.scanName();
/* 1032 */         if (childName == null) {
/* 1033 */           reportFatalError("MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN", new Object[] { elName });
/*      */ 
/* 1035 */           return;
/*      */         }
/*      */ 
/* 1038 */         if (this.fDTDContentModelHandler != null) {
/* 1039 */           this.fDTDContentModelHandler.element(childName, null);
/*      */         }
/* 1041 */         this.fStringBuffer.append(childName);
/* 1042 */         int c = this.fEntityScanner.peekChar();
/* 1043 */         if ((c == 63) || (c == 42) || (c == 43))
/*      */         {
/* 1045 */           if (this.fDTDContentModelHandler != null)
/*      */           {
/*      */             short oc;
/*      */             short oc;
/* 1047 */             if (c == 63) {
/* 1048 */               oc = 2;
/*      */             }
/*      */             else
/*      */             {
/*      */               short oc;
/* 1050 */               if (c == 42) {
/* 1051 */                 oc = 3;
/*      */               }
/*      */               else
/* 1054 */                 oc = 4;
/*      */             }
/* 1056 */             this.fDTDContentModelHandler.occurrence(oc, null);
/*      */           }
/* 1058 */           this.fEntityScanner.scanChar();
/* 1059 */           this.fStringBuffer.append((char)c);
/*      */         }
/*      */         do {
/* 1062 */           skipSeparator(false, !scanningInternalSubset());
/* 1063 */           c = this.fEntityScanner.peekChar();
/* 1064 */           if ((c == 44) && (currentOp != 124)) {
/* 1065 */             currentOp = c;
/*      */ 
/* 1067 */             if (this.fDTDContentModelHandler != null) {
/* 1068 */               this.fDTDContentModelHandler.separator((short)1, null);
/*      */             }
/*      */ 
/* 1071 */             this.fEntityScanner.scanChar();
/* 1072 */             this.fStringBuffer.append(',');
/* 1073 */             break;
/*      */           }
/* 1075 */           if ((c == 124) && (currentOp != 44)) {
/* 1076 */             currentOp = c;
/*      */ 
/* 1078 */             if (this.fDTDContentModelHandler != null) {
/* 1079 */               this.fDTDContentModelHandler.separator((short)0, null);
/*      */             }
/*      */ 
/* 1082 */             this.fEntityScanner.scanChar();
/* 1083 */             this.fStringBuffer.append('|');
/* 1084 */             break;
/*      */           }
/* 1086 */           if (c != 41) {
/* 1087 */             reportFatalError("MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN", new Object[] { elName });
/*      */           }
/*      */ 
/* 1091 */           if (this.fDTDContentModelHandler != null) {
/* 1092 */             this.fDTDContentModelHandler.endGroup(null);
/*      */           }
/*      */ 
/* 1095 */           currentOp = popContentStack();
/*      */ 
/* 1102 */           if (this.fEntityScanner.skipString(")?")) {
/* 1103 */             this.fStringBuffer.append(")?");
/*      */ 
/* 1105 */             if (this.fDTDContentModelHandler != null) {
/* 1106 */               short oc = 2;
/* 1107 */               this.fDTDContentModelHandler.occurrence(oc, null);
/*      */             }
/*      */           }
/* 1110 */           else if (this.fEntityScanner.skipString(")+")) {
/* 1111 */             this.fStringBuffer.append(")+");
/*      */ 
/* 1113 */             if (this.fDTDContentModelHandler != null) {
/* 1114 */               short oc = 4;
/* 1115 */               this.fDTDContentModelHandler.occurrence(oc, null);
/*      */             }
/*      */           }
/* 1118 */           else if (this.fEntityScanner.skipString(")*")) {
/* 1119 */             this.fStringBuffer.append(")*");
/*      */ 
/* 1121 */             if (this.fDTDContentModelHandler != null) {
/* 1122 */               short oc = 3;
/* 1123 */               this.fDTDContentModelHandler.occurrence(oc, null);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1128 */             this.fEntityScanner.scanChar();
/* 1129 */             this.fStringBuffer.append(')');
/*      */           }
/* 1131 */           this.fMarkUpDepth -= 1;
/* 1132 */         }while (this.fContentDepth != 0);
/* 1133 */         return;
/*      */ 
/* 1136 */         skipSeparator(false, !scanningInternalSubset());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void scanAttlistDecl()
/*      */     throws IOException, XNIException
/*      */   {
/* 1153 */     this.fReportEntity = false;
/* 1154 */     if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1155 */       reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ATTLISTDECL", null);
/*      */     }
/*      */ 
/* 1160 */     String elName = this.fEntityScanner.scanName();
/* 1161 */     if (elName == null) {
/* 1162 */       reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_ATTLISTDECL", null);
/*      */     }
/*      */ 
/* 1167 */     if (this.fDTDHandler != null) {
/* 1168 */       this.fDTDHandler.startAttlist(elName, null);
/*      */     }
/*      */ 
/* 1172 */     if (!skipSeparator(true, !scanningInternalSubset()))
/*      */     {
/* 1174 */       if (this.fEntityScanner.skipChar(62))
/*      */       {
/* 1177 */         if (this.fDTDHandler != null) {
/* 1178 */           this.fDTDHandler.endAttlist(null);
/*      */         }
/* 1180 */         this.fMarkUpDepth -= 1;
/* 1181 */         return;
/*      */       }
/*      */ 
/* 1184 */       reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ATTRIBUTE_NAME_IN_ATTDEF", new Object[] { elName });
/*      */     }
/*      */ 
/* 1190 */     while (!this.fEntityScanner.skipChar(62)) {
/* 1191 */       String name = this.fEntityScanner.scanName();
/* 1192 */       if (name == null) {
/* 1193 */         reportFatalError("AttNameRequiredInAttDef", new Object[] { elName });
/*      */       }
/*      */ 
/* 1197 */       if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1198 */         reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ATTTYPE_IN_ATTDEF", new Object[] { elName, name });
/*      */       }
/*      */ 
/* 1202 */       String type = scanAttType(elName, name);
/*      */ 
/* 1205 */       if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1206 */         reportFatalError("MSG_SPACE_REQUIRED_BEFORE_DEFAULTDECL_IN_ATTDEF", new Object[] { elName, name });
/*      */       }
/*      */ 
/* 1211 */       String defaultType = scanAttDefaultDecl(elName, name, type, this.fLiteral, this.fLiteral2);
/*      */ 
/* 1217 */       String[] enumr = null;
/* 1218 */       if (((this.fDTDHandler != null) || (this.nonValidatingMode)) && 
/* 1219 */         (this.fEnumerationCount != 0)) {
/* 1220 */         enumr = new String[this.fEnumerationCount];
/* 1221 */         System.arraycopy(this.fEnumeration, 0, enumr, 0, this.fEnumerationCount);
/*      */       }
/*      */ 
/* 1228 */       if ((defaultType != null) && ((defaultType.equals("#REQUIRED")) || (defaultType.equals("#IMPLIED"))))
/*      */       {
/* 1230 */         if (this.fDTDHandler != null) {
/* 1231 */           this.fDTDHandler.attributeDecl(elName, name, type, enumr, defaultType, null, null, null);
/*      */         }
/*      */ 
/* 1234 */         if (this.nonValidatingMode) {
/* 1235 */           this.nvGrammarInfo.attributeDecl(elName, name, type, enumr, defaultType, null, null, null);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1241 */         if (this.fDTDHandler != null) {
/* 1242 */           this.fDTDHandler.attributeDecl(elName, name, type, enumr, defaultType, this.fLiteral, this.fLiteral2, null);
/*      */         }
/*      */ 
/* 1245 */         if (this.nonValidatingMode) {
/* 1246 */           this.nvGrammarInfo.attributeDecl(elName, name, type, enumr, defaultType, this.fLiteral, this.fLiteral2, null);
/*      */         }
/*      */       }
/*      */ 
/* 1250 */       skipSeparator(false, !scanningInternalSubset());
/*      */     }
/*      */ 
/* 1254 */     if (this.fDTDHandler != null) {
/* 1255 */       this.fDTDHandler.endAttlist(null);
/*      */     }
/* 1257 */     this.fMarkUpDepth -= 1;
/* 1258 */     this.fReportEntity = true;
/*      */   }
/*      */ 
/*      */   private final String scanAttType(String elName, String atName)
/*      */     throws IOException, XNIException
/*      */   {
/* 1288 */     String type = null;
/* 1289 */     this.fEnumerationCount = 0;
/*      */ 
/* 1295 */     if (this.fEntityScanner.skipString("CDATA")) {
/* 1296 */       type = "CDATA";
/*      */     }
/* 1298 */     else if (this.fEntityScanner.skipString("IDREFS")) {
/* 1299 */       type = "IDREFS";
/*      */     }
/* 1301 */     else if (this.fEntityScanner.skipString("IDREF")) {
/* 1302 */       type = "IDREF";
/*      */     }
/* 1304 */     else if (this.fEntityScanner.skipString("ID")) {
/* 1305 */       type = "ID";
/*      */     }
/* 1307 */     else if (this.fEntityScanner.skipString("ENTITY")) {
/* 1308 */       type = "ENTITY";
/*      */     }
/* 1310 */     else if (this.fEntityScanner.skipString("ENTITIES")) {
/* 1311 */       type = "ENTITIES";
/*      */     }
/* 1313 */     else if (this.fEntityScanner.skipString("NMTOKENS")) {
/* 1314 */       type = "NMTOKENS";
/*      */     }
/* 1316 */     else if (this.fEntityScanner.skipString("NMTOKEN")) {
/* 1317 */       type = "NMTOKEN";
/*      */     }
/* 1319 */     else if (this.fEntityScanner.skipString("NOTATION")) {
/* 1320 */       type = "NOTATION";
/*      */ 
/* 1322 */       if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1323 */         reportFatalError("MSG_SPACE_REQUIRED_AFTER_NOTATION_IN_NOTATIONTYPE", new Object[] { elName, atName });
/*      */       }
/*      */ 
/* 1327 */       int c = this.fEntityScanner.scanChar();
/* 1328 */       if (c != 40) {
/* 1329 */         reportFatalError("MSG_OPEN_PAREN_REQUIRED_IN_NOTATIONTYPE", new Object[] { elName, atName });
/*      */       }
/*      */ 
/* 1332 */       this.fMarkUpDepth += 1;
/*      */       do {
/* 1334 */         skipSeparator(false, !scanningInternalSubset());
/* 1335 */         String aName = this.fEntityScanner.scanName();
/* 1336 */         if (aName == null) {
/* 1337 */           reportFatalError("MSG_NAME_REQUIRED_IN_NOTATIONTYPE", new Object[] { elName, atName });
/*      */         }
/*      */ 
/* 1340 */         ensureEnumerationSize(this.fEnumerationCount + 1);
/* 1341 */         this.fEnumeration[(this.fEnumerationCount++)] = aName;
/* 1342 */         skipSeparator(false, !scanningInternalSubset());
/* 1343 */         c = this.fEntityScanner.scanChar();
/* 1344 */       }while (c == 124);
/* 1345 */       if (c != 41) {
/* 1346 */         reportFatalError("NotationTypeUnterminated", new Object[] { elName, atName });
/*      */       }
/*      */ 
/* 1349 */       this.fMarkUpDepth -= 1;
/*      */     }
/*      */     else {
/* 1352 */       type = "ENUMERATION";
/*      */ 
/* 1354 */       int c = this.fEntityScanner.scanChar();
/* 1355 */       if (c != 40)
/*      */       {
/* 1357 */         reportFatalError("AttTypeRequiredInAttDef", new Object[] { elName, atName });
/*      */       }
/*      */ 
/* 1360 */       this.fMarkUpDepth += 1;
/*      */       do {
/* 1362 */         skipSeparator(false, !scanningInternalSubset());
/* 1363 */         String token = this.fEntityScanner.scanNmtoken();
/* 1364 */         if (token == null) {
/* 1365 */           reportFatalError("MSG_NMTOKEN_REQUIRED_IN_ENUMERATION", new Object[] { elName, atName });
/*      */         }
/*      */ 
/* 1368 */         ensureEnumerationSize(this.fEnumerationCount + 1);
/* 1369 */         this.fEnumeration[(this.fEnumerationCount++)] = token;
/* 1370 */         skipSeparator(false, !scanningInternalSubset());
/* 1371 */         c = this.fEntityScanner.scanChar();
/* 1372 */       }while (c == 124);
/* 1373 */       if (c != 41) {
/* 1374 */         reportFatalError("EnumerationUnterminated", new Object[] { elName, atName });
/*      */       }
/*      */ 
/* 1377 */       this.fMarkUpDepth -= 1;
/*      */     }
/* 1379 */     return type;
/*      */   }
/*      */ 
/*      */   protected final String scanAttDefaultDecl(String elName, String atName, String type, XMLString defaultVal, XMLString nonNormalizedDefaultVal)
/*      */     throws IOException, XNIException
/*      */   {
/* 1400 */     String defaultType = null;
/* 1401 */     this.fString.clear();
/* 1402 */     defaultVal.clear();
/* 1403 */     if (this.fEntityScanner.skipString("#REQUIRED")) {
/* 1404 */       defaultType = "#REQUIRED";
/*      */     }
/* 1406 */     else if (this.fEntityScanner.skipString("#IMPLIED")) {
/* 1407 */       defaultType = "#IMPLIED";
/*      */     }
/*      */     else {
/* 1410 */       if (this.fEntityScanner.skipString("#FIXED")) {
/* 1411 */         defaultType = "#FIXED";
/*      */ 
/* 1413 */         if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1414 */           reportFatalError("MSG_SPACE_REQUIRED_AFTER_FIXED_IN_DEFAULTDECL", new Object[] { elName, atName });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1419 */       boolean isVC = (!this.fStandalone) && ((this.fSeenExternalDTD) || (this.fSeenExternalPE));
/* 1420 */       scanAttributeValue(defaultVal, nonNormalizedDefaultVal, atName, this.fAttributes, 0, isVC);
/*      */     }
/*      */ 
/* 1423 */     return defaultType;
/*      */   }
/*      */ 
/*      */   private final void scanEntityDecl()
/*      */     throws IOException, XNIException
/*      */   {
/* 1445 */     boolean isPEDecl = false;
/* 1446 */     boolean sawPERef = false;
/* 1447 */     this.fReportEntity = false;
/* 1448 */     if (this.fEntityScanner.skipSpaces()) {
/* 1449 */       if (!this.fEntityScanner.skipChar(37)) {
/* 1450 */         isPEDecl = false;
/*      */       }
/* 1452 */       else if (skipSeparator(true, !scanningInternalSubset()))
/*      */       {
/* 1454 */         isPEDecl = true;
/*      */       }
/* 1456 */       else if (scanningInternalSubset()) {
/* 1457 */         reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_ENTITYDECL", null);
/*      */ 
/* 1459 */         isPEDecl = true;
/*      */       }
/* 1461 */       else if (this.fEntityScanner.peekChar() == 37)
/*      */       {
/* 1463 */         skipSeparator(false, !scanningInternalSubset());
/* 1464 */         isPEDecl = true;
/*      */       }
/*      */       else {
/* 1467 */         sawPERef = true;
/*      */       }
/*      */     }
/* 1470 */     else if ((scanningInternalSubset()) || (!this.fEntityScanner.skipChar(37)))
/*      */     {
/* 1472 */       reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_ENTITYDECL", null);
/*      */ 
/* 1474 */       isPEDecl = false;
/*      */     }
/* 1476 */     else if (this.fEntityScanner.skipSpaces())
/*      */     {
/* 1478 */       reportFatalError("MSG_SPACE_REQUIRED_BEFORE_PERCENT_IN_PEDECL", null);
/*      */ 
/* 1480 */       isPEDecl = false;
/*      */     }
/*      */     else {
/* 1483 */       sawPERef = true;
/*      */     }
/* 1485 */     if (sawPERef) {
/*      */       while (true) {
/* 1487 */         String peName = this.fEntityScanner.scanName();
/* 1488 */         if (peName == null) {
/* 1489 */           reportFatalError("NameRequiredInPEReference", null);
/*      */         }
/* 1491 */         else if (!this.fEntityScanner.skipChar(59)) {
/* 1492 */           reportFatalError("SemicolonRequiredInPEReference", new Object[] { peName });
/*      */         }
/*      */         else
/*      */         {
/* 1496 */           startPE(peName, false);
/*      */         }
/* 1498 */         this.fEntityScanner.skipSpaces();
/* 1499 */         if (!this.fEntityScanner.skipChar(37))
/*      */           break;
/* 1501 */         if (!isPEDecl) {
/* 1502 */           if (skipSeparator(true, !scanningInternalSubset())) {
/* 1503 */             isPEDecl = true;
/* 1504 */             break;
/*      */           }
/* 1506 */           isPEDecl = this.fEntityScanner.skipChar(37);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1512 */     String name = this.fEntityScanner.scanName();
/* 1513 */     if (name == null) {
/* 1514 */       reportFatalError("MSG_ENTITY_NAME_REQUIRED_IN_ENTITYDECL", null);
/*      */     }
/*      */ 
/* 1518 */     if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1519 */       reportFatalError("MSG_SPACE_REQUIRED_AFTER_ENTITY_NAME_IN_ENTITYDECL", new Object[] { name });
/*      */     }
/*      */ 
/* 1524 */     scanExternalID(this.fStrings, false);
/* 1525 */     String systemId = this.fStrings[0];
/* 1526 */     String publicId = this.fStrings[1];
/*      */ 
/* 1528 */     if ((isPEDecl) && (systemId != null)) {
/* 1529 */       this.fSeenExternalPE = true;
/*      */     }
/*      */ 
/* 1532 */     String notation = null;
/*      */ 
/* 1534 */     boolean sawSpace = skipSeparator(true, !scanningInternalSubset());
/* 1535 */     if ((!isPEDecl) && (this.fEntityScanner.skipString("NDATA")))
/*      */     {
/* 1537 */       if (!sawSpace) {
/* 1538 */         reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NDATA_IN_UNPARSED_ENTITYDECL", new Object[] { name });
/*      */       }
/*      */ 
/* 1543 */       if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1544 */         reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_UNPARSED_ENTITYDECL", new Object[] { name });
/*      */       }
/*      */ 
/* 1547 */       notation = this.fEntityScanner.scanName();
/* 1548 */       if (notation == null) {
/* 1549 */         reportFatalError("MSG_NOTATION_NAME_REQUIRED_FOR_UNPARSED_ENTITYDECL", new Object[] { name });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1555 */     if (systemId == null) {
/* 1556 */       scanEntityValue(name, isPEDecl, this.fLiteral, this.fLiteral2);
/*      */ 
/* 1559 */       this.fStringBuffer.clear();
/* 1560 */       this.fStringBuffer2.clear();
/* 1561 */       this.fStringBuffer.append(this.fLiteral.ch, this.fLiteral.offset, this.fLiteral.length);
/* 1562 */       this.fStringBuffer2.append(this.fLiteral2.ch, this.fLiteral2.offset, this.fLiteral2.length);
/*      */     }
/*      */ 
/* 1566 */     skipSeparator(false, !scanningInternalSubset());
/*      */ 
/* 1569 */     if (!this.fEntityScanner.skipChar(62)) {
/* 1570 */       reportFatalError("EntityDeclUnterminated", new Object[] { name });
/*      */     }
/* 1572 */     this.fMarkUpDepth -= 1;
/*      */ 
/* 1575 */     if (isPEDecl) {
/* 1576 */       name = "%" + name;
/*      */     }
/* 1578 */     if (systemId != null) {
/* 1579 */       String baseSystemId = this.fEntityScanner.getBaseSystemId();
/* 1580 */       if (notation != null) {
/* 1581 */         this.fEntityStore.addUnparsedEntity(name, publicId, systemId, baseSystemId, notation);
/*      */       }
/*      */       else {
/* 1584 */         this.fEntityStore.addExternalEntity(name, publicId, systemId, baseSystemId);
/*      */       }
/*      */ 
/* 1587 */       if (this.fDTDHandler != null)
/*      */       {
/* 1589 */         this.fResourceIdentifier.setValues(publicId, systemId, baseSystemId, XMLEntityManager.expandSystemId(systemId, baseSystemId));
/*      */ 
/* 1591 */         if (notation != null) {
/* 1592 */           this.fDTDHandler.unparsedEntityDecl(name, this.fResourceIdentifier, notation, null);
/*      */         }
/*      */         else
/*      */         {
/* 1596 */           this.fDTDHandler.externalEntityDecl(name, this.fResourceIdentifier, null);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1601 */       this.fEntityStore.addInternalEntity(name, this.fStringBuffer.toString());
/* 1602 */       if (this.fDTDHandler != null) {
/* 1603 */         this.fDTDHandler.internalEntityDecl(name, this.fStringBuffer, this.fStringBuffer2, null);
/*      */       }
/*      */     }
/* 1606 */     this.fReportEntity = true;
/*      */   }
/*      */ 
/*      */   protected final void scanEntityValue(String entityName, boolean isPEDecl, XMLString value, XMLString nonNormalizedValue)
/*      */     throws IOException, XNIException
/*      */   {
/* 1624 */     int quote = this.fEntityScanner.scanChar();
/* 1625 */     if ((quote != 39) && (quote != 34)) {
/* 1626 */       reportFatalError("OpenQuoteMissingInDecl", null);
/*      */     }
/*      */ 
/* 1629 */     int entityDepth = this.fEntityDepth;
/*      */ 
/* 1631 */     XMLString literal = this.fString;
/* 1632 */     XMLString literal2 = this.fString;
/* 1633 */     int countChar = 0;
/* 1634 */     if (this.fLimitAnalyzer == null) {
/* 1635 */       this.fLimitAnalyzer = new XMLLimitAnalyzer();
/*      */     }
/* 1637 */     this.fLimitAnalyzer.startEntity(entityName);
/*      */ 
/* 1639 */     if (this.fEntityScanner.scanLiteral(quote, this.fString) != quote) {
/* 1640 */       this.fStringBuffer.clear();
/* 1641 */       this.fStringBuffer2.clear();
/*      */       do {
/* 1643 */         if ((isPEDecl) && (this.fLimitAnalyzer != null)) {
/* 1644 */           checkLimit("%" + entityName, this.fString.length + countChar);
/*      */         }
/* 1646 */         countChar = 0;
/* 1647 */         this.fStringBuffer.append(this.fString);
/* 1648 */         this.fStringBuffer2.append(this.fString);
/* 1649 */         if (this.fEntityScanner.skipChar(38)) {
/* 1650 */           if (this.fEntityScanner.skipChar(35)) {
/* 1651 */             this.fStringBuffer2.append("&#");
/* 1652 */             scanCharReferenceValue(this.fStringBuffer, this.fStringBuffer2);
/*      */           }
/*      */           else {
/* 1655 */             this.fStringBuffer.append('&');
/* 1656 */             this.fStringBuffer2.append('&');
/* 1657 */             String eName = this.fEntityScanner.scanName();
/* 1658 */             if (eName == null) {
/* 1659 */               reportFatalError("NameRequiredInReference", null);
/*      */             }
/*      */             else
/*      */             {
/* 1663 */               this.fStringBuffer.append(eName);
/* 1664 */               this.fStringBuffer2.append(eName);
/*      */             }
/* 1666 */             if (!this.fEntityScanner.skipChar(59)) {
/* 1667 */               reportFatalError("SemicolonRequiredInReference", new Object[] { eName });
/*      */             }
/*      */             else
/*      */             {
/* 1671 */               this.fStringBuffer.append(';');
/* 1672 */               this.fStringBuffer2.append(';');
/*      */             }
/*      */           }
/*      */         } else {
/* 1676 */           if (this.fEntityScanner.skipChar(37)) {
/*      */             while (true) {
/* 1678 */               this.fStringBuffer2.append('%');
/* 1679 */               String peName = this.fEntityScanner.scanName();
/* 1680 */               if (peName == null) {
/* 1681 */                 reportFatalError("NameRequiredInPEReference", null);
/*      */               }
/* 1684 */               else if (!this.fEntityScanner.skipChar(59)) {
/* 1685 */                 reportFatalError("SemicolonRequiredInPEReference", new Object[] { peName });
/*      */               }
/*      */               else
/*      */               {
/* 1689 */                 if (scanningInternalSubset()) {
/* 1690 */                   reportFatalError("PEReferenceWithinMarkup", new Object[] { peName });
/*      */                 }
/*      */ 
/* 1693 */                 this.fStringBuffer2.append(peName);
/* 1694 */                 this.fStringBuffer2.append(';');
/*      */               }
/* 1696 */               startPE(peName, true);
/*      */ 
/* 1700 */               this.fEntityScanner.skipSpaces();
/* 1701 */               if (!this.fEntityScanner.skipChar(37)) {
/*      */                 break;
/*      */               }
/*      */             }
/*      */           }
/* 1706 */           countChar++;
/* 1707 */           int c = this.fEntityScanner.peekChar();
/* 1708 */           if (XMLChar.isHighSurrogate(c)) {
/* 1709 */             scanSurrogates(this.fStringBuffer2);
/*      */           }
/* 1711 */           else if (isInvalidLiteral(c)) {
/* 1712 */             reportFatalError("InvalidCharInLiteral", new Object[] { Integer.toHexString(c) });
/*      */ 
/* 1714 */             this.fEntityScanner.scanChar();
/*      */           }
/* 1719 */           else if ((c != quote) || (entityDepth != this.fEntityDepth)) {
/* 1720 */             this.fStringBuffer.append((char)c);
/* 1721 */             this.fStringBuffer2.append((char)c);
/* 1722 */             this.fEntityScanner.scanChar();
/*      */           }
/*      */         }
/*      */       }
/* 1725 */       while (this.fEntityScanner.scanLiteral(quote, this.fString) != quote);
/* 1726 */       this.fStringBuffer.append(this.fString);
/* 1727 */       this.fStringBuffer2.append(this.fString);
/* 1728 */       literal = this.fStringBuffer;
/* 1729 */       literal2 = this.fStringBuffer2;
/*      */     }
/* 1731 */     else if (isPEDecl) {
/* 1732 */       checkLimit("%" + entityName, literal);
/*      */     }
/*      */ 
/* 1735 */     value.setValues(literal);
/* 1736 */     nonNormalizedValue.setValues(literal2);
/* 1737 */     if (this.fLimitAnalyzer != null) {
/* 1738 */       this.fLimitAnalyzer.endEntity(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT, entityName);
/*      */     }
/*      */ 
/* 1741 */     if (!this.fEntityScanner.skipChar(quote))
/* 1742 */       reportFatalError("CloseQuoteMissingInDecl", null);
/*      */   }
/*      */ 
/*      */   private final void scanNotationDecl()
/*      */     throws IOException, XNIException
/*      */   {
/* 1759 */     this.fReportEntity = false;
/* 1760 */     if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1761 */       reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_NOTATIONDECL", null);
/*      */     }
/*      */ 
/* 1766 */     String name = this.fEntityScanner.scanName();
/* 1767 */     if (name == null) {
/* 1768 */       reportFatalError("MSG_NOTATION_NAME_REQUIRED_IN_NOTATIONDECL", null);
/*      */     }
/*      */ 
/* 1773 */     if (!skipSeparator(true, !scanningInternalSubset())) {
/* 1774 */       reportFatalError("MSG_SPACE_REQUIRED_AFTER_NOTATION_NAME_IN_NOTATIONDECL", new Object[] { name });
/*      */     }
/*      */ 
/* 1779 */     scanExternalID(this.fStrings, true);
/* 1780 */     String systemId = this.fStrings[0];
/* 1781 */     String publicId = this.fStrings[1];
/* 1782 */     String baseSystemId = this.fEntityScanner.getBaseSystemId();
/*      */ 
/* 1784 */     if ((systemId == null) && (publicId == null)) {
/* 1785 */       reportFatalError("ExternalIDorPublicIDRequired", new Object[] { name });
/*      */     }
/*      */ 
/* 1790 */     skipSeparator(false, !scanningInternalSubset());
/*      */ 
/* 1793 */     if (!this.fEntityScanner.skipChar(62)) {
/* 1794 */       reportFatalError("NotationDeclUnterminated", new Object[] { name });
/*      */     }
/* 1796 */     this.fMarkUpDepth -= 1;
/*      */ 
/* 1798 */     this.fResourceIdentifier.setValues(publicId, systemId, baseSystemId, XMLEntityManager.expandSystemId(systemId, baseSystemId));
/* 1799 */     if (this.nonValidatingMode) this.nvGrammarInfo.notationDecl(name, this.fResourceIdentifier, null);
/*      */ 
/* 1801 */     if (this.fDTDHandler != null)
/*      */     {
/* 1804 */       this.fDTDHandler.notationDecl(name, this.fResourceIdentifier, null);
/*      */     }
/* 1806 */     this.fReportEntity = true;
/*      */   }
/*      */ 
/*      */   private final void scanConditionalSect(int currPEDepth)
/*      */     throws IOException, XNIException
/*      */   {
/* 1829 */     this.fReportEntity = false;
/* 1830 */     skipSeparator(false, !scanningInternalSubset());
/*      */ 
/* 1832 */     if (this.fEntityScanner.skipString("INCLUDE")) {
/* 1833 */       skipSeparator(false, !scanningInternalSubset());
/* 1834 */       if ((currPEDepth != this.fPEDepth) && (this.fValidation)) {
/* 1835 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "INVALID_PE_IN_CONDITIONAL", new Object[] { this.fEntityManager.fCurrentEntity.name }, (short)1);
/*      */       }
/*      */ 
/* 1841 */       if (!this.fEntityScanner.skipChar(91)) {
/* 1842 */         reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
/*      */       }
/*      */ 
/* 1845 */       if (this.fDTDHandler != null) {
/* 1846 */         this.fDTDHandler.startConditional((short)0, null);
/*      */       }
/*      */ 
/* 1849 */       this.fIncludeSectDepth += 1;
/*      */ 
/* 1851 */       this.fReportEntity = true;
/*      */     } else {
/* 1853 */       if (this.fEntityScanner.skipString("IGNORE")) {
/* 1854 */         skipSeparator(false, !scanningInternalSubset());
/* 1855 */         if ((currPEDepth != this.fPEDepth) && (this.fValidation)) {
/* 1856 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "INVALID_PE_IN_CONDITIONAL", new Object[] { this.fEntityManager.fCurrentEntity.name }, (short)1);
/*      */         }
/*      */ 
/* 1862 */         if (this.fDTDHandler != null) {
/* 1863 */           this.fDTDHandler.startConditional((short)1, null);
/*      */         }
/*      */ 
/* 1866 */         if (!this.fEntityScanner.skipChar(91)) {
/* 1867 */           reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
/*      */         }
/* 1869 */         this.fReportEntity = true;
/* 1870 */         int initialDepth = ++this.fIncludeSectDepth;
/* 1871 */         if (this.fDTDHandler != null) {
/* 1872 */           this.fIgnoreConditionalBuffer.clear();
/*      */         }
/*      */         while (true) {
/* 1875 */           if (this.fEntityScanner.skipChar(60)) {
/* 1876 */             if (this.fDTDHandler != null) {
/* 1877 */               this.fIgnoreConditionalBuffer.append('<');
/*      */             }
/*      */ 
/* 1883 */             if (this.fEntityScanner.skipChar(33)) {
/* 1884 */               if (this.fEntityScanner.skipChar(91)) {
/* 1885 */                 if (this.fDTDHandler != null) {
/* 1886 */                   this.fIgnoreConditionalBuffer.append("![");
/*      */                 }
/* 1888 */                 this.fIncludeSectDepth += 1;
/*      */               }
/* 1890 */               else if (this.fDTDHandler != null) {
/* 1891 */                 this.fIgnoreConditionalBuffer.append("!");
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/* 1896 */           else if (this.fEntityScanner.skipChar(93)) {
/* 1897 */             if (this.fDTDHandler != null) {
/* 1898 */               this.fIgnoreConditionalBuffer.append(']');
/*      */             }
/*      */ 
/* 1903 */             if (this.fEntityScanner.skipChar(93)) {
/* 1904 */               if (this.fDTDHandler != null) {
/* 1905 */                 this.fIgnoreConditionalBuffer.append(']');
/*      */               }
/* 1907 */               while (this.fEntityScanner.skipChar(93))
/*      */               {
/* 1909 */                 if (this.fDTDHandler != null) {
/* 1910 */                   this.fIgnoreConditionalBuffer.append(']');
/*      */                 }
/*      */               }
/* 1913 */               if (this.fEntityScanner.skipChar(62)) {
/* 1914 */                 if (this.fIncludeSectDepth-- == initialDepth) {
/* 1915 */                   this.fMarkUpDepth -= 1;
/*      */ 
/* 1917 */                   if (this.fDTDHandler != null) {
/* 1918 */                     this.fLiteral.setValues(this.fIgnoreConditionalBuffer.ch, 0, this.fIgnoreConditionalBuffer.length - 2);
/*      */ 
/* 1920 */                     this.fDTDHandler.ignoredCharacters(this.fLiteral, null);
/* 1921 */                     this.fDTDHandler.endConditional(null);
/*      */                   }
/* 1923 */                   return;
/* 1924 */                 }if (this.fDTDHandler != null)
/* 1925 */                   this.fIgnoreConditionalBuffer.append('>');
/*      */               }
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1931 */             int c = this.fEntityScanner.scanChar();
/* 1932 */             if (this.fScannerState == 0) {
/* 1933 */               reportFatalError("IgnoreSectUnterminated", null);
/* 1934 */               return;
/*      */             }
/* 1936 */             if (this.fDTDHandler != null) {
/* 1937 */               this.fIgnoreConditionalBuffer.append((char)c);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1943 */       reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final boolean scanDecls(boolean complete)
/*      */     throws IOException, XNIException
/*      */   {
/* 1963 */     skipSeparator(false, true);
/* 1964 */     boolean again = true;
/*      */ 
/* 1966 */     while ((again) && (this.fScannerState == 2)) {
/* 1967 */       again = complete;
/* 1968 */       if (this.fEntityScanner.skipChar(60)) {
/* 1969 */         this.fMarkUpDepth += 1;
/* 1970 */         if (this.fEntityScanner.skipChar(63)) {
/* 1971 */           this.fStringBuffer.clear();
/* 1972 */           scanPI(this.fStringBuffer);
/* 1973 */           this.fMarkUpDepth -= 1;
/*      */         }
/* 1975 */         else if (this.fEntityScanner.skipChar(33)) {
/* 1976 */           if (this.fEntityScanner.skipChar(45)) {
/* 1977 */             if (!this.fEntityScanner.skipChar(45)) {
/* 1978 */               reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
/*      */             }
/*      */             else {
/* 1981 */               scanComment();
/*      */             }
/*      */           }
/* 1984 */           else if (this.fEntityScanner.skipString("ELEMENT")) {
/* 1985 */             scanElementDecl();
/*      */           }
/* 1987 */           else if (this.fEntityScanner.skipString("ATTLIST")) {
/* 1988 */             scanAttlistDecl();
/*      */           }
/* 1990 */           else if (this.fEntityScanner.skipString("ENTITY")) {
/* 1991 */             scanEntityDecl();
/*      */           }
/* 1993 */           else if (this.fEntityScanner.skipString("NOTATION")) {
/* 1994 */             scanNotationDecl();
/*      */           }
/* 1996 */           else if ((this.fEntityScanner.skipChar(91)) && (!scanningInternalSubset()))
/*      */           {
/* 1998 */             scanConditionalSect(this.fPEDepth);
/*      */           }
/*      */           else {
/* 2001 */             this.fMarkUpDepth -= 1;
/* 2002 */             reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2007 */           this.fMarkUpDepth -= 1;
/* 2008 */           reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
/*      */         }
/*      */       }
/* 2011 */       else if ((this.fIncludeSectDepth > 0) && (this.fEntityScanner.skipChar(93)))
/*      */       {
/* 2013 */         if ((!this.fEntityScanner.skipChar(93)) || (!this.fEntityScanner.skipChar(62)))
/*      */         {
/* 2015 */           reportFatalError("IncludeSectUnterminated", null);
/*      */         }
/*      */ 
/* 2018 */         if (this.fDTDHandler != null) {
/* 2019 */           this.fDTDHandler.endConditional(null);
/*      */         }
/*      */ 
/* 2022 */         this.fIncludeSectDepth -= 1;
/* 2023 */         this.fMarkUpDepth -= 1;
/*      */       } else {
/* 2025 */         if ((scanningInternalSubset()) && (this.fEntityScanner.peekChar() == 93))
/*      */         {
/* 2028 */           return false;
/*      */         }
/* 2030 */         if (!this.fEntityScanner.skipSpaces())
/*      */         {
/* 2034 */           reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
/*      */         }
/*      */       }
/* 2036 */       skipSeparator(false, true);
/*      */     }
/* 2038 */     return this.fScannerState != 0;
/*      */   }
/*      */ 
/*      */   private boolean skipSeparator(boolean spaceRequired, boolean lookForPERefs)
/*      */     throws IOException, XNIException
/*      */   {
/* 2059 */     int depth = this.fPEDepth;
/* 2060 */     boolean sawSpace = this.fEntityScanner.skipSpaces();
/* 2061 */     if ((!lookForPERefs) || (!this.fEntityScanner.skipChar(37)))
/* 2062 */       return (!spaceRequired) || (sawSpace) || (depth != this.fPEDepth);
/*      */     while (true)
/*      */     {
/* 2065 */       String name = this.fEntityScanner.scanName();
/* 2066 */       if (name == null) {
/* 2067 */         reportFatalError("NameRequiredInPEReference", null);
/*      */       }
/* 2069 */       else if (!this.fEntityScanner.skipChar(59)) {
/* 2070 */         reportFatalError("SemicolonRequiredInPEReference", new Object[] { name });
/*      */       }
/*      */ 
/* 2073 */       startPE(name, false);
/* 2074 */       this.fEntityScanner.skipSpaces();
/* 2075 */       if (!this.fEntityScanner.skipChar(37))
/* 2076 */         return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void pushContentStack(int c)
/*      */   {
/* 2085 */     if (this.fContentStack.length == this.fContentDepth) {
/* 2086 */       int[] newStack = new int[this.fContentDepth * 2];
/* 2087 */       System.arraycopy(this.fContentStack, 0, newStack, 0, this.fContentDepth);
/* 2088 */       this.fContentStack = newStack;
/*      */     }
/* 2090 */     this.fContentStack[(this.fContentDepth++)] = c;
/*      */   }
/*      */ 
/*      */   private final int popContentStack() {
/* 2094 */     return this.fContentStack[(--this.fContentDepth)];
/*      */   }
/*      */ 
/*      */   private final void pushPEStack(int depth, boolean report)
/*      */   {
/* 2102 */     if (this.fPEStack.length == this.fPEDepth) {
/* 2103 */       int[] newIntStack = new int[this.fPEDepth * 2];
/* 2104 */       System.arraycopy(this.fPEStack, 0, newIntStack, 0, this.fPEDepth);
/* 2105 */       this.fPEStack = newIntStack;
/*      */ 
/* 2107 */       boolean[] newBooleanStack = new boolean[this.fPEDepth * 2];
/* 2108 */       System.arraycopy(this.fPEReport, 0, newBooleanStack, 0, this.fPEDepth);
/* 2109 */       this.fPEReport = newBooleanStack;
/*      */     }
/*      */ 
/* 2112 */     this.fPEReport[this.fPEDepth] = report;
/* 2113 */     this.fPEStack[(this.fPEDepth++)] = depth;
/*      */   }
/*      */ 
/*      */   private final int popPEStack()
/*      */   {
/* 2118 */     return this.fPEStack[(--this.fPEDepth)];
/*      */   }
/*      */ 
/*      */   private final boolean peekReportEntity()
/*      */   {
/* 2123 */     return this.fPEReport[(this.fPEDepth - 1)];
/*      */   }
/*      */ 
/*      */   private final void ensureEnumerationSize(int size)
/*      */   {
/* 2131 */     if (this.fEnumeration.length == size) {
/* 2132 */       String[] newEnum = new String[size * 2];
/* 2133 */       System.arraycopy(this.fEnumeration, 0, newEnum, 0, size);
/* 2134 */       this.fEnumeration = newEnum;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/* 2141 */     this.fStartDTDCalled = false;
/* 2142 */     this.fExtEntityDepth = 0;
/* 2143 */     this.fIncludeSectDepth = 0;
/* 2144 */     this.fMarkUpDepth = 0;
/* 2145 */     this.fPEDepth = 0;
/*      */ 
/* 2147 */     this.fStandalone = false;
/* 2148 */     this.fSeenExternalDTD = false;
/* 2149 */     this.fSeenExternalPE = false;
/*      */ 
/* 2152 */     setScannerState(1);
/*      */ 
/* 2155 */     this.fLimitAnalyzer = new XMLLimitAnalyzer();
/*      */   }
/*      */ 
/*      */   private void checkLimit(String entityName, XMLString buffer)
/*      */   {
/* 2165 */     checkLimit(entityName, buffer.length);
/*      */   }
/*      */ 
/*      */   private void checkLimit(String entityName, int len)
/*      */   {
/* 2174 */     if (this.fLimitAnalyzer == null) {
/* 2175 */       this.fLimitAnalyzer = new XMLLimitAnalyzer();
/*      */     }
/* 2177 */     this.fLimitAnalyzer.addValue(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT, entityName, len);
/* 2178 */     if (this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT, this.fLimitAnalyzer)) {
/* 2179 */       this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
/* 2180 */       reportFatalError("MaxEntitySizeLimit", new Object[] { entityName, Integer.valueOf(this.fLimitAnalyzer.getValue(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT)), Integer.valueOf(this.fSecurityManager.getLimit(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT)), this.fSecurityManager.getStateLiteral(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT) });
/*      */     }
/*      */ 
/* 2185 */     if (this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT, this.fLimitAnalyzer)) {
/* 2186 */       this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
/* 2187 */       reportFatalError("TotalEntitySizeLimit", new Object[] { Integer.valueOf(this.fLimitAnalyzer.getTotalValue(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)), Integer.valueOf(this.fSecurityManager.getLimit(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)), this.fSecurityManager.getStateLiteral(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT) });
/*      */     }
/*      */   }
/*      */ 
/*      */   public DTDGrammar getGrammar()
/*      */   {
/* 2196 */     return this.nvGrammarInfo;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl
 * JD-Core Version:    0.6.2
 */