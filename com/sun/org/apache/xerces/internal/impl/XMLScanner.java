/*      */ package com.sun.org.apache.xerces.internal.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.Status;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.xml.internal.stream.Entity.ScannedEntity;
/*      */ import com.sun.xml.internal.stream.XMLEntityStorage;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import javax.xml.stream.events.XMLEvent;
/*      */ 
/*      */ public abstract class XMLScanner
/*      */   implements XMLComponent
/*      */ {
/*      */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
/*      */   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*      */   protected static final boolean DEBUG_ATTR_NORMALIZATION = false;
/*  122 */   private boolean fNeedNonNormalizedValue = false;
/*      */ 
/*  124 */   protected ArrayList attributeValueCache = new ArrayList();
/*  125 */   protected ArrayList stringBufferCache = new ArrayList();
/*  126 */   protected int fStringBufferIndex = 0;
/*  127 */   protected boolean fAttributeCacheInitDone = false;
/*  128 */   protected int fAttributeCacheUsedCount = 0;
/*      */ 
/*  140 */   protected boolean fValidation = false;
/*      */   protected boolean fNamespaces;
/*  146 */   protected boolean fNotifyCharRefs = false;
/*      */ 
/*  149 */   protected boolean fParserSettings = true;
/*      */ 
/*  153 */   protected PropertyManager fPropertyManager = null;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected XMLErrorReporter fErrorReporter;
/*  162 */   protected XMLEntityManager fEntityManager = null;
/*      */ 
/*  165 */   protected XMLEntityStorage fEntityStore = null;
/*      */ 
/*  168 */   protected XMLSecurityManager fSecurityManager = null;
/*      */ 
/*  171 */   protected XMLLimitAnalyzer fLimitAnalyzer = null;
/*      */   protected XMLEvent fEvent;
/*  179 */   protected XMLEntityScanner fEntityScanner = null;
/*      */   protected int fEntityDepth;
/*  185 */   protected String fCharRefLiteral = null;
/*      */   protected boolean fScanningAttribute;
/*      */   protected boolean fReportEntity;
/*  196 */   protected static final String fVersionSymbol = "version".intern();
/*      */ 
/*  199 */   protected static final String fEncodingSymbol = "encoding".intern();
/*      */ 
/*  202 */   protected static final String fStandaloneSymbol = "standalone".intern();
/*      */ 
/*  205 */   protected static final String fAmpSymbol = "amp".intern();
/*      */ 
/*  208 */   protected static final String fLtSymbol = "lt".intern();
/*      */ 
/*  211 */   protected static final String fGtSymbol = "gt".intern();
/*      */ 
/*  214 */   protected static final String fQuotSymbol = "quot".intern();
/*      */ 
/*  217 */   protected static final String fAposSymbol = "apos".intern();
/*      */ 
/*  228 */   private XMLString fString = new XMLString();
/*      */ 
/*  231 */   private XMLStringBuffer fStringBuffer = new XMLStringBuffer();
/*      */ 
/*  234 */   private XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
/*      */ 
/*  237 */   private XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
/*      */ 
/*  240 */   protected XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();
/*  241 */   int initialCacheCount = 6;
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/*  257 */     this.fParserSettings = componentManager.getFeature("http://apache.org/xml/features/internal/parser-settings", true);
/*      */ 
/*  259 */     if (!this.fParserSettings)
/*      */     {
/*  261 */       init();
/*  262 */       return;
/*      */     }
/*      */ 
/*  267 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*  268 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*  269 */     this.fEntityManager = ((XMLEntityManager)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
/*  270 */     this.fSecurityManager = ((XMLSecurityManager)componentManager.getProperty("http://apache.org/xml/properties/security-manager"));
/*      */ 
/*  273 */     this.fEntityStore = this.fEntityManager.getEntityStore();
/*      */ 
/*  276 */     this.fValidation = componentManager.getFeature("http://xml.org/sax/features/validation", false);
/*  277 */     this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
/*  278 */     this.fNotifyCharRefs = componentManager.getFeature("http://apache.org/xml/features/scanner/notify-char-refs", false);
/*      */ 
/*  280 */     init();
/*      */   }
/*      */ 
/*      */   protected void setPropertyManager(PropertyManager propertyManager) {
/*  284 */     this.fPropertyManager = propertyManager;
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  297 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/*  298 */       String property = propertyId.substring("http://apache.org/xml/properties/".length());
/*      */ 
/*  300 */       if (property.equals("internal/symbol-table"))
/*  301 */         this.fSymbolTable = ((SymbolTable)value);
/*  302 */       else if (property.equals("internal/error-reporter"))
/*  303 */         this.fErrorReporter = ((XMLErrorReporter)value);
/*  304 */       else if (property.equals("internal/entity-manager")) {
/*  305 */         this.fEntityManager = ((XMLEntityManager)value);
/*      */       }
/*      */     }
/*      */ 
/*  309 */     if (propertyId.equals("http://apache.org/xml/properties/security-manager"))
/*  310 */       this.fSecurityManager = ((XMLSecurityManager)value);
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  326 */     if ("http://xml.org/sax/features/validation".equals(featureId))
/*  327 */       this.fValidation = value;
/*  328 */     else if ("http://apache.org/xml/features/scanner/notify-char-refs".equals(featureId))
/*  329 */       this.fNotifyCharRefs = value;
/*      */   }
/*      */ 
/*      */   public boolean getFeature(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  339 */     if ("http://xml.org/sax/features/validation".equals(featureId))
/*  340 */       return this.fValidation;
/*  341 */     if ("http://apache.org/xml/features/scanner/notify-char-refs".equals(featureId)) {
/*  342 */       return this.fNotifyCharRefs;
/*      */     }
/*  344 */     throw new XMLConfigurationException(Status.NOT_RECOGNIZED, featureId);
/*      */   }
/*      */ 
/*      */   protected void reset()
/*      */   {
/*  353 */     init();
/*      */ 
/*  356 */     this.fValidation = true;
/*  357 */     this.fNotifyCharRefs = false;
/*      */   }
/*      */ 
/*      */   public void reset(PropertyManager propertyManager)
/*      */   {
/*  362 */     init();
/*      */ 
/*  364 */     this.fSymbolTable = ((SymbolTable)propertyManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*      */ 
/*  366 */     this.fErrorReporter = ((XMLErrorReporter)propertyManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*      */ 
/*  368 */     this.fEntityManager = ((XMLEntityManager)propertyManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
/*  369 */     this.fEntityStore = this.fEntityManager.getEntityStore();
/*  370 */     this.fEntityScanner = this.fEntityManager.getEntityScanner();
/*  371 */     this.fSecurityManager = ((XMLSecurityManager)propertyManager.getProperty("http://apache.org/xml/properties/security-manager"));
/*      */ 
/*  375 */     this.fValidation = false;
/*  376 */     this.fNotifyCharRefs = false;
/*      */   }
/*      */ 
/*      */   protected void scanXMLDeclOrTextDecl(boolean scanningTextDecl, String[] pseudoAttributeValues)
/*      */     throws IOException, XNIException
/*      */   {
/*  410 */     String version = null;
/*  411 */     String encoding = null;
/*  412 */     String standalone = null;
/*      */ 
/*  415 */     int STATE_VERSION = 0;
/*  416 */     int STATE_ENCODING = 1;
/*  417 */     int STATE_STANDALONE = 2;
/*  418 */     int STATE_DONE = 3;
/*  419 */     int state = 0;
/*      */ 
/*  421 */     boolean dataFoundForTarget = false;
/*  422 */     boolean sawSpace = this.fEntityScanner.skipSpaces();
/*      */ 
/*  430 */     Entity.ScannedEntity currEnt = this.fEntityManager.getCurrentEntity();
/*  431 */     boolean currLiteral = currEnt.literal;
/*  432 */     currEnt.literal = false;
/*  433 */     while (this.fEntityScanner.peekChar() != 63) {
/*  434 */       dataFoundForTarget = true;
/*  435 */       String name = scanPseudoAttribute(scanningTextDecl, this.fString);
/*  436 */       switch (state) {
/*      */       case 0:
/*  438 */         if (name.equals(fVersionSymbol)) {
/*  439 */           if (!sawSpace) {
/*  440 */             reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeVersionInTextDecl" : "SpaceRequiredBeforeVersionInXMLDecl", null);
/*      */           }
/*      */ 
/*  445 */           version = this.fString.toString();
/*  446 */           state = 1;
/*  447 */           if (!versionSupported(version)) {
/*  448 */             reportFatalError("VersionNotSupported", new Object[] { version });
/*      */           }
/*      */ 
/*  452 */           if (version.equals("1.1")) {
/*  453 */             Entity.ScannedEntity top = this.fEntityManager.getTopLevelEntity();
/*  454 */             if ((top != null) && ((top.version == null) || (top.version.equals("1.0")))) {
/*  455 */               reportFatalError("VersionMismatch", null);
/*      */             }
/*  457 */             this.fEntityManager.setScannerVersion((short)2);
/*      */           }
/*      */         }
/*  460 */         else if (name.equals(fEncodingSymbol)) {
/*  461 */           if (!scanningTextDecl) {
/*  462 */             reportFatalError("VersionInfoRequired", null);
/*      */           }
/*  464 */           if (!sawSpace) {
/*  465 */             reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
/*      */           }
/*      */ 
/*  470 */           encoding = this.fString.toString();
/*  471 */           state = scanningTextDecl ? 3 : 2;
/*      */         }
/*  473 */         else if (scanningTextDecl) {
/*  474 */           reportFatalError("EncodingDeclRequired", null);
/*      */         } else {
/*  476 */           reportFatalError("VersionInfoRequired", null);
/*      */         }
/*      */ 
/*  479 */         break;
/*      */       case 1:
/*  482 */         if (name.equals(fEncodingSymbol)) {
/*  483 */           if (!sawSpace) {
/*  484 */             reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
/*      */           }
/*      */ 
/*  489 */           encoding = this.fString.toString();
/*  490 */           state = scanningTextDecl ? 3 : 2;
/*      */         }
/*  493 */         else if ((!scanningTextDecl) && (name.equals(fStandaloneSymbol))) {
/*  494 */           if (!sawSpace) {
/*  495 */             reportFatalError("SpaceRequiredBeforeStandalone", null);
/*      */           }
/*      */ 
/*  498 */           standalone = this.fString.toString();
/*  499 */           state = 3;
/*  500 */           if ((!standalone.equals("yes")) && (!standalone.equals("no")))
/*  501 */             reportFatalError("SDDeclInvalid", new Object[] { standalone });
/*      */         }
/*      */         else {
/*  504 */           reportFatalError("EncodingDeclRequired", null);
/*      */         }
/*  506 */         break;
/*      */       case 2:
/*  509 */         if (name.equals(fStandaloneSymbol)) {
/*  510 */           if (!sawSpace) {
/*  511 */             reportFatalError("SpaceRequiredBeforeStandalone", null);
/*      */           }
/*      */ 
/*  514 */           standalone = this.fString.toString();
/*  515 */           state = 3;
/*  516 */           if ((!standalone.equals("yes")) && (!standalone.equals("no")))
/*  517 */             reportFatalError("SDDeclInvalid", new Object[] { standalone });
/*      */         }
/*      */         else {
/*  520 */           reportFatalError("SDDeclNameInvalid", null);
/*      */         }
/*  522 */         break;
/*      */       default:
/*  525 */         reportFatalError("NoMorePseudoAttributes", null);
/*      */       }
/*      */ 
/*  528 */       sawSpace = this.fEntityScanner.skipSpaces();
/*      */     }
/*      */ 
/*  531 */     if (currLiteral) {
/*  532 */       currEnt.literal = true;
/*      */     }
/*      */ 
/*  535 */     if ((scanningTextDecl) && (state != 3)) {
/*  536 */       reportFatalError("MorePseudoAttributes", null);
/*      */     }
/*      */ 
/*  541 */     if (scanningTextDecl) {
/*  542 */       if ((!dataFoundForTarget) && (encoding == null)) {
/*  543 */         reportFatalError("EncodingDeclRequired", null);
/*      */       }
/*      */     }
/*  546 */     else if ((!dataFoundForTarget) && (version == null)) {
/*  547 */       reportFatalError("VersionInfoRequired", null);
/*      */     }
/*      */ 
/*  552 */     if (!this.fEntityScanner.skipChar(63)) {
/*  553 */       reportFatalError("XMLDeclUnterminated", null);
/*      */     }
/*  555 */     if (!this.fEntityScanner.skipChar(62)) {
/*  556 */       reportFatalError("XMLDeclUnterminated", null);
/*      */     }
/*      */ 
/*  561 */     pseudoAttributeValues[0] = version;
/*  562 */     pseudoAttributeValues[1] = encoding;
/*  563 */     pseudoAttributeValues[2] = standalone;
/*      */   }
/*      */ 
/*      */   public String scanPseudoAttribute(boolean scanningTextDecl, XMLString value)
/*      */     throws IOException, XNIException
/*      */   {
/*  586 */     String name = scanPseudoAttributeName();
/*      */ 
/*  589 */     if (name == null) {
/*  590 */       reportFatalError("PseudoAttrNameExpected", null);
/*      */     }
/*  592 */     this.fEntityScanner.skipSpaces();
/*  593 */     if (!this.fEntityScanner.skipChar(61)) {
/*  594 */       reportFatalError(scanningTextDecl ? "EqRequiredInTextDecl" : "EqRequiredInXMLDecl", new Object[] { name });
/*      */     }
/*      */ 
/*  597 */     this.fEntityScanner.skipSpaces();
/*  598 */     int quote = this.fEntityScanner.peekChar();
/*  599 */     if ((quote != 39) && (quote != 34)) {
/*  600 */       reportFatalError(scanningTextDecl ? "QuoteRequiredInTextDecl" : "QuoteRequiredInXMLDecl", new Object[] { name });
/*      */     }
/*      */ 
/*  603 */     this.fEntityScanner.scanChar();
/*  604 */     int c = this.fEntityScanner.scanLiteral(quote, value);
/*  605 */     if (c != quote) {
/*  606 */       this.fStringBuffer2.clear();
/*      */       do {
/*  608 */         this.fStringBuffer2.append(value);
/*  609 */         if (c != -1) {
/*  610 */           if ((c == 38) || (c == 37) || (c == 60) || (c == 93)) {
/*  611 */             this.fStringBuffer2.append((char)this.fEntityScanner.scanChar());
/*  612 */           } else if (XMLChar.isHighSurrogate(c)) {
/*  613 */             scanSurrogates(this.fStringBuffer2);
/*  614 */           } else if (isInvalidLiteral(c)) {
/*  615 */             String key = scanningTextDecl ? "InvalidCharInTextDecl" : "InvalidCharInXMLDecl";
/*      */ 
/*  617 */             reportFatalError(key, new Object[] { Integer.toString(c, 16) });
/*      */ 
/*  619 */             this.fEntityScanner.scanChar();
/*      */           }
/*      */         }
/*  622 */         c = this.fEntityScanner.scanLiteral(quote, value);
/*  623 */       }while (c != quote);
/*  624 */       this.fStringBuffer2.append(value);
/*  625 */       value.setValues(this.fStringBuffer2);
/*      */     }
/*  627 */     if (!this.fEntityScanner.skipChar(quote)) {
/*  628 */       reportFatalError(scanningTextDecl ? "CloseQuoteMissingInTextDecl" : "CloseQuoteMissingInXMLDecl", new Object[] { name });
/*      */     }
/*      */ 
/*  634 */     return name;
/*      */   }
/*      */ 
/*      */   private String scanPseudoAttributeName()
/*      */     throws IOException, XNIException
/*      */   {
/*  646 */     int ch = this.fEntityScanner.peekChar();
/*  647 */     switch (ch) {
/*      */     case 118:
/*  649 */       if (this.fEntityScanner.skipString(fVersionSymbol)) {
/*  650 */         return fVersionSymbol;
/*      */       }
/*      */       break;
/*      */     case 101:
/*  654 */       if (this.fEntityScanner.skipString(fEncodingSymbol)) {
/*  655 */         return fEncodingSymbol;
/*      */       }
/*      */       break;
/*      */     case 115:
/*  659 */       if (this.fEntityScanner.skipString(fStandaloneSymbol)) {
/*  660 */         return fStandaloneSymbol;
/*      */       }
/*      */       break;
/*      */     }
/*  664 */     return null;
/*      */   }
/*      */ 
/*      */   protected void scanPI(XMLStringBuffer data)
/*      */     throws IOException, XNIException
/*      */   {
/*  684 */     this.fReportEntity = false;
/*  685 */     String target = this.fEntityScanner.scanName();
/*  686 */     if (target == null) {
/*  687 */       reportFatalError("PITargetRequired", null);
/*      */     }
/*      */ 
/*  691 */     scanPIData(target, data);
/*  692 */     this.fReportEntity = true;
/*      */   }
/*      */ 
/*      */   protected void scanPIData(String target, XMLStringBuffer data)
/*      */     throws IOException, XNIException
/*      */   {
/*  718 */     if (target.length() == 3) {
/*  719 */       char c0 = Character.toLowerCase(target.charAt(0));
/*  720 */       char c1 = Character.toLowerCase(target.charAt(1));
/*  721 */       char c2 = Character.toLowerCase(target.charAt(2));
/*  722 */       if ((c0 == 'x') && (c1 == 'm') && (c2 == 'l')) {
/*  723 */         reportFatalError("ReservedPITarget", null);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  728 */     if (!this.fEntityScanner.skipSpaces()) {
/*  729 */       if (this.fEntityScanner.skipString("?>"))
/*      */       {
/*  731 */         return;
/*      */       }
/*      */ 
/*  734 */       reportFatalError("SpaceRequiredInPI", null);
/*      */     }
/*      */ 
/*  741 */     if (this.fEntityScanner.scanData("?>", data))
/*      */       do {
/*  743 */         int c = this.fEntityScanner.peekChar();
/*  744 */         if (c != -1)
/*  745 */           if (XMLChar.isHighSurrogate(c)) {
/*  746 */             scanSurrogates(data);
/*  747 */           } else if (isInvalidLiteral(c)) {
/*  748 */             reportFatalError("InvalidCharInPI", new Object[] { Integer.toHexString(c) });
/*      */ 
/*  750 */             this.fEntityScanner.scanChar();
/*      */           }
/*      */       }
/*  753 */       while (this.fEntityScanner.scanData("?>", data));
/*      */   }
/*      */ 
/*      */   protected void scanComment(XMLStringBuffer text)
/*      */     throws IOException, XNIException
/*      */   {
/*  777 */     text.clear();
/*  778 */     while (this.fEntityScanner.scanData("--", text)) {
/*  779 */       int c = this.fEntityScanner.peekChar();
/*      */ 
/*  784 */       if (c != -1) {
/*  785 */         if (XMLChar.isHighSurrogate(c)) {
/*  786 */           scanSurrogates(text);
/*      */         }
/*  788 */         if (isInvalidLiteral(c)) {
/*  789 */           reportFatalError("InvalidCharInComment", new Object[] { Integer.toHexString(c) });
/*      */ 
/*  791 */           this.fEntityScanner.scanChar();
/*      */         }
/*      */       }
/*      */     }
/*  795 */     if (!this.fEntityScanner.skipChar(62))
/*  796 */       reportFatalError("DashDashInComment", null);
/*      */   }
/*      */ 
/*      */   protected void scanAttributeValue(XMLString value, XMLString nonNormalizedValue, String atName, XMLAttributes attributes, int attrIndex, boolean checkEntities)
/*      */     throws IOException, XNIException
/*      */   {
/*  825 */     XMLStringBuffer stringBuffer = null;
/*      */ 
/*  827 */     int quote = this.fEntityScanner.peekChar();
/*  828 */     if ((quote != 39) && (quote != 34)) {
/*  829 */       reportFatalError("OpenQuoteExpected", new Object[] { atName });
/*      */     }
/*      */ 
/*  832 */     this.fEntityScanner.scanChar();
/*  833 */     int entityDepth = this.fEntityDepth;
/*      */ 
/*  835 */     int c = this.fEntityScanner.scanLiteral(quote, value);
/*      */ 
/*  840 */     if (this.fNeedNonNormalizedValue) {
/*  841 */       this.fStringBuffer2.clear();
/*  842 */       this.fStringBuffer2.append(value);
/*      */     }
/*  844 */     if (this.fEntityScanner.whiteSpaceLen > 0) {
/*  845 */       normalizeWhitespace(value);
/*      */     }
/*      */ 
/*  850 */     if (c != quote) {
/*  851 */       this.fScanningAttribute = true;
/*  852 */       stringBuffer = getStringBuffer();
/*  853 */       stringBuffer.clear();
/*      */       do {
/*  855 */         stringBuffer.append(value);
/*      */ 
/*  860 */         if (c == 38) {
/*  861 */           this.fEntityScanner.skipChar(38);
/*  862 */           if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue)) {
/*  863 */             this.fStringBuffer2.append('&');
/*      */           }
/*  865 */           if (this.fEntityScanner.skipChar(35)) {
/*  866 */             if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue))
/*  867 */               this.fStringBuffer2.append('#');
/*      */             int ch;
/*      */             int ch;
/*  870 */             if (this.fNeedNonNormalizedValue)
/*  871 */               ch = scanCharReferenceValue(stringBuffer, this.fStringBuffer2);
/*      */             else {
/*  873 */               ch = scanCharReferenceValue(stringBuffer, null);
/*      */             }
/*      */ 
/*  875 */             if (ch == -1);
/*      */           }
/*      */           else
/*      */           {
/*  883 */             String entityName = this.fEntityScanner.scanName();
/*  884 */             if (entityName == null)
/*  885 */               reportFatalError("NameRequiredInReference", null);
/*  886 */             else if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue)) {
/*  887 */               this.fStringBuffer2.append(entityName);
/*      */             }
/*  889 */             if (!this.fEntityScanner.skipChar(59)) {
/*  890 */               reportFatalError("SemicolonRequiredInReference", new Object[] { entityName });
/*      */             }
/*  892 */             else if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue)) {
/*  893 */               this.fStringBuffer2.append(';');
/*      */             }
/*  895 */             if (entityName == fAmpSymbol) {
/*  896 */               stringBuffer.append('&');
/*      */             }
/*  902 */             else if (entityName == fAposSymbol) {
/*  903 */               stringBuffer.append('\'');
/*      */             }
/*  909 */             else if (entityName == fLtSymbol) {
/*  910 */               stringBuffer.append('<');
/*      */             }
/*  916 */             else if (entityName == fGtSymbol) {
/*  917 */               stringBuffer.append('>');
/*      */             }
/*  923 */             else if (entityName == fQuotSymbol) {
/*  924 */               stringBuffer.append('"');
/*      */             }
/*  931 */             else if (this.fEntityStore.isExternalEntity(entityName)) {
/*  932 */               reportFatalError("ReferenceToExternalEntity", new Object[] { entityName });
/*      */             }
/*      */             else {
/*  935 */               if (!this.fEntityStore.isDeclaredEntity(entityName))
/*      */               {
/*  937 */                 if (checkEntities) {
/*  938 */                   if (this.fValidation) {
/*  939 */                     this.fErrorReporter.reportError(this.fEntityScanner, "http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[] { entityName }, (short)1);
/*      */                   }
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*  945 */                   reportFatalError("EntityNotDeclared", new Object[] { entityName });
/*      */                 }
/*      */               }
/*      */ 
/*  949 */               this.fEntityManager.startEntity(entityName, true);
/*      */             }
/*      */           }
/*      */         }
/*  953 */         else if (c == 60) {
/*  954 */           reportFatalError("LessthanInAttValue", new Object[] { null, atName });
/*      */ 
/*  956 */           this.fEntityScanner.scanChar();
/*  957 */           if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue))
/*  958 */             this.fStringBuffer2.append((char)c);
/*      */         }
/*  960 */         else if ((c == 37) || (c == 93)) {
/*  961 */           this.fEntityScanner.scanChar();
/*  962 */           stringBuffer.append((char)c);
/*  963 */           if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue)) {
/*  964 */             this.fStringBuffer2.append((char)c);
/*      */           }
/*      */ 
/*      */         }
/*  970 */         else if ((c == 10) || (c == 13)) {
/*  971 */           this.fEntityScanner.scanChar();
/*  972 */           stringBuffer.append(' ');
/*  973 */           if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue))
/*  974 */             this.fStringBuffer2.append('\n');
/*      */         }
/*  976 */         else if ((c != -1) && (XMLChar.isHighSurrogate(c))) {
/*  977 */           if (scanSurrogates(this.fStringBuffer3)) {
/*  978 */             stringBuffer.append(this.fStringBuffer3);
/*  979 */             if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue)) {
/*  980 */               this.fStringBuffer2.append(this.fStringBuffer3);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*  988 */         else if ((c != -1) && (isInvalidLiteral(c))) {
/*  989 */           reportFatalError("InvalidCharInAttValue", new Object[] { Integer.toString(c, 16) });
/*      */ 
/*  991 */           this.fEntityScanner.scanChar();
/*  992 */           if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue)) {
/*  993 */             this.fStringBuffer2.append((char)c);
/*      */           }
/*      */         }
/*  996 */         c = this.fEntityScanner.scanLiteral(quote, value);
/*  997 */         if ((entityDepth == this.fEntityDepth) && (this.fNeedNonNormalizedValue)) {
/*  998 */           this.fStringBuffer2.append(value);
/*      */         }
/* 1000 */         if (this.fEntityScanner.whiteSpaceLen > 0) {
/* 1001 */           normalizeWhitespace(value);
/*      */         }
/*      */       }
/* 1004 */       while ((c != quote) || (entityDepth != this.fEntityDepth));
/* 1005 */       stringBuffer.append(value);
/*      */ 
/* 1010 */       value.setValues(stringBuffer);
/* 1011 */       this.fScanningAttribute = false;
/*      */     }
/* 1013 */     if (this.fNeedNonNormalizedValue) {
/* 1014 */       nonNormalizedValue.setValues(this.fStringBuffer2);
/*      */     }
/*      */ 
/* 1017 */     int cquote = this.fEntityScanner.scanChar();
/* 1018 */     if (cquote != quote)
/* 1019 */       reportFatalError("CloseQuoteExpected", new Object[] { atName });
/*      */   }
/*      */ 
/*      */   protected void scanExternalID(String[] identifiers, boolean optionalSystemId)
/*      */     throws IOException, XNIException
/*      */   {
/* 1038 */     String systemId = null;
/* 1039 */     String publicId = null;
/* 1040 */     if (this.fEntityScanner.skipString("PUBLIC")) {
/* 1041 */       if (!this.fEntityScanner.skipSpaces()) {
/* 1042 */         reportFatalError("SpaceRequiredAfterPUBLIC", null);
/*      */       }
/* 1044 */       scanPubidLiteral(this.fString);
/* 1045 */       publicId = this.fString.toString();
/*      */ 
/* 1047 */       if ((!this.fEntityScanner.skipSpaces()) && (!optionalSystemId)) {
/* 1048 */         reportFatalError("SpaceRequiredBetweenPublicAndSystem", null);
/*      */       }
/*      */     }
/*      */ 
/* 1052 */     if ((publicId != null) || (this.fEntityScanner.skipString("SYSTEM"))) {
/* 1053 */       if ((publicId == null) && (!this.fEntityScanner.skipSpaces())) {
/* 1054 */         reportFatalError("SpaceRequiredAfterSYSTEM", null);
/*      */       }
/* 1056 */       int quote = this.fEntityScanner.peekChar();
/* 1057 */       if ((quote != 39) && (quote != 34)) {
/* 1058 */         if ((publicId != null) && (optionalSystemId))
/*      */         {
/* 1061 */           identifiers[0] = null;
/* 1062 */           identifiers[1] = publicId;
/* 1063 */           return;
/*      */         }
/* 1065 */         reportFatalError("QuoteRequiredInSystemID", null);
/*      */       }
/* 1067 */       this.fEntityScanner.scanChar();
/* 1068 */       XMLString ident = this.fString;
/* 1069 */       if (this.fEntityScanner.scanLiteral(quote, ident) != quote) {
/* 1070 */         this.fStringBuffer.clear();
/*      */         do {
/* 1072 */           this.fStringBuffer.append(ident);
/* 1073 */           int c = this.fEntityScanner.peekChar();
/* 1074 */           if ((XMLChar.isMarkup(c)) || (c == 93))
/* 1075 */             this.fStringBuffer.append((char)this.fEntityScanner.scanChar());
/* 1076 */           else if ((c != -1) && (isInvalidLiteral(c))) {
/* 1077 */             reportFatalError("InvalidCharInSystemID", new Object[] { Integer.toString(c, 16) });
/*      */           }
/*      */         }
/* 1080 */         while (this.fEntityScanner.scanLiteral(quote, ident) != quote);
/* 1081 */         this.fStringBuffer.append(ident);
/* 1082 */         ident = this.fStringBuffer;
/*      */       }
/* 1084 */       systemId = ident.toString();
/* 1085 */       if (!this.fEntityScanner.skipChar(quote)) {
/* 1086 */         reportFatalError("SystemIDUnterminated", null);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1091 */     identifiers[0] = systemId;
/* 1092 */     identifiers[1] = publicId;
/*      */   }
/*      */ 
/*      */   protected boolean scanPubidLiteral(XMLString literal)
/*      */     throws IOException, XNIException
/*      */   {
/* 1117 */     int quote = this.fEntityScanner.scanChar();
/* 1118 */     if ((quote != 39) && (quote != 34)) {
/* 1119 */       reportFatalError("QuoteRequiredInPublicID", null);
/* 1120 */       return false;
/*      */     }
/*      */ 
/* 1123 */     this.fStringBuffer.clear();
/*      */ 
/* 1125 */     boolean skipSpace = true;
/* 1126 */     boolean dataok = true;
/*      */     while (true) {
/* 1128 */       int c = this.fEntityScanner.scanChar();
/* 1129 */       if ((c == 32) || (c == 10) || (c == 13)) {
/* 1130 */         if (!skipSpace)
/*      */         {
/* 1132 */           this.fStringBuffer.append(' ');
/* 1133 */           skipSpace = true;
/*      */         }
/*      */       } else { if (c == quote) {
/* 1136 */           if (skipSpace)
/*      */           {
/* 1138 */             this.fStringBuffer.length -= 1;
/*      */           }
/* 1140 */           literal.setValues(this.fStringBuffer);
/* 1141 */           break;
/* 1142 */         }if (XMLChar.isPubid(c)) {
/* 1143 */           this.fStringBuffer.append((char)c);
/* 1144 */           skipSpace = false; } else {
/* 1145 */           if (c == -1) {
/* 1146 */             reportFatalError("PublicIDUnterminated", null);
/* 1147 */             return false;
/*      */           }
/* 1149 */           dataok = false;
/* 1150 */           reportFatalError("InvalidCharInPublicID", new Object[] { Integer.toHexString(c) });
/*      */         }
/*      */       }
/*      */     }
/* 1154 */     return dataok;
/*      */   }
/*      */ 
/*      */   protected void normalizeWhitespace(XMLString value)
/*      */   {
/* 1163 */     int i = 0;
/* 1164 */     int j = 0;
/* 1165 */     int[] buff = this.fEntityScanner.whiteSpaceLookup;
/* 1166 */     int buffLen = this.fEntityScanner.whiteSpaceLen;
/* 1167 */     int end = value.offset + value.length;
/* 1168 */     while (i < buffLen) {
/* 1169 */       j = buff[i];
/* 1170 */       if (j < end) {
/* 1171 */         value.ch[j] = ' ';
/*      */       }
/* 1173 */       i++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1202 */     this.fEntityDepth += 1;
/*      */ 
/* 1204 */     this.fEntityScanner = this.fEntityManager.getEntityScanner();
/* 1205 */     this.fEntityStore = this.fEntityManager.getEntityStore();
/*      */   }
/*      */ 
/*      */   public void endEntity(String name, Augmentations augs)
/*      */     throws IOException, XNIException
/*      */   {
/* 1221 */     this.fEntityDepth -= 1;
/*      */   }
/*      */ 
/*      */   protected int scanCharReferenceValue(XMLStringBuffer buf, XMLStringBuffer buf2)
/*      */     throws IOException, XNIException
/*      */   {
/* 1245 */     boolean hex = false;
/* 1246 */     if (this.fEntityScanner.skipChar(120)) {
/* 1247 */       if (buf2 != null) buf2.append('x');
/* 1248 */       hex = true;
/* 1249 */       this.fStringBuffer3.clear();
/* 1250 */       boolean digit = true;
/*      */ 
/* 1252 */       int c = this.fEntityScanner.peekChar();
/* 1253 */       digit = ((c >= 48) && (c <= 57)) || ((c >= 97) && (c <= 102)) || ((c >= 65) && (c <= 70));
/*      */ 
/* 1256 */       if (digit) {
/* 1257 */         if (buf2 != null) buf2.append((char)c);
/* 1258 */         this.fEntityScanner.scanChar();
/* 1259 */         this.fStringBuffer3.append((char)c);
/*      */         do
/*      */         {
/* 1262 */           c = this.fEntityScanner.peekChar();
/* 1263 */           digit = ((c >= 48) && (c <= 57)) || ((c >= 97) && (c <= 102)) || ((c >= 65) && (c <= 70));
/*      */ 
/* 1266 */           if (digit) {
/* 1267 */             if (buf2 != null) buf2.append((char)c);
/* 1268 */             this.fEntityScanner.scanChar();
/* 1269 */             this.fStringBuffer3.append((char)c);
/*      */           }
/*      */         }
/* 1271 */         while (digit);
/*      */       } else {
/* 1273 */         reportFatalError("HexdigitRequiredInCharRef", null);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1279 */       this.fStringBuffer3.clear();
/* 1280 */       boolean digit = true;
/*      */ 
/* 1282 */       int c = this.fEntityScanner.peekChar();
/* 1283 */       digit = (c >= 48) && (c <= 57);
/* 1284 */       if (digit) {
/* 1285 */         if (buf2 != null) buf2.append((char)c);
/* 1286 */         this.fEntityScanner.scanChar();
/* 1287 */         this.fStringBuffer3.append((char)c);
/*      */         do
/*      */         {
/* 1290 */           c = this.fEntityScanner.peekChar();
/* 1291 */           digit = (c >= 48) && (c <= 57);
/* 1292 */           if (digit) {
/* 1293 */             if (buf2 != null) buf2.append((char)c);
/* 1294 */             this.fEntityScanner.scanChar();
/* 1295 */             this.fStringBuffer3.append((char)c);
/*      */           }
/*      */         }
/* 1297 */         while (digit);
/*      */       } else {
/* 1299 */         reportFatalError("DigitRequiredInCharRef", null);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1304 */     if (!this.fEntityScanner.skipChar(59)) {
/* 1305 */       reportFatalError("SemicolonRequiredInCharRef", null);
/*      */     }
/* 1307 */     if (buf2 != null) buf2.append(';');
/*      */ 
/* 1310 */     int value = -1;
/*      */     try {
/* 1312 */       value = Integer.parseInt(this.fStringBuffer3.toString(), hex ? 16 : 10);
/*      */ 
/* 1316 */       if (isInvalid(value)) {
/* 1317 */         StringBuffer errorBuf = new StringBuffer(this.fStringBuffer3.length + 1);
/* 1318 */         if (hex) errorBuf.append('x');
/* 1319 */         errorBuf.append(this.fStringBuffer3.ch, this.fStringBuffer3.offset, this.fStringBuffer3.length);
/* 1320 */         reportFatalError("InvalidCharRef", new Object[] { errorBuf.toString() });
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/* 1326 */       StringBuffer errorBuf = new StringBuffer(this.fStringBuffer3.length + 1);
/* 1327 */       if (hex) errorBuf.append('x');
/* 1328 */       errorBuf.append(this.fStringBuffer3.ch, this.fStringBuffer3.offset, this.fStringBuffer3.length);
/* 1329 */       reportFatalError("InvalidCharRef", new Object[] { errorBuf.toString() });
/*      */     }
/*      */ 
/* 1334 */     if (!XMLChar.isSupplemental(value)) {
/* 1335 */       buf.append((char)value);
/*      */     }
/*      */     else {
/* 1338 */       buf.append(XMLChar.highSurrogate(value));
/* 1339 */       buf.append(XMLChar.lowSurrogate(value));
/*      */     }
/*      */ 
/* 1343 */     if ((this.fNotifyCharRefs) && (value != -1)) {
/* 1344 */       String literal = "#" + (hex ? "x" : "") + this.fStringBuffer3.toString();
/* 1345 */       if (!this.fScanningAttribute) {
/* 1346 */         this.fCharRefLiteral = literal;
/*      */       }
/*      */     }
/*      */ 
/* 1350 */     return value;
/*      */   }
/*      */ 
/*      */   protected boolean isInvalid(int value)
/*      */   {
/* 1356 */     return XMLChar.isInvalid(value);
/*      */   }
/*      */ 
/*      */   protected boolean isInvalidLiteral(int value)
/*      */   {
/* 1363 */     return XMLChar.isInvalid(value);
/*      */   }
/*      */ 
/*      */   protected boolean isValidNameChar(int value)
/*      */   {
/* 1370 */     return XMLChar.isName(value);
/*      */   }
/*      */ 
/*      */   protected boolean isValidNCName(int value)
/*      */   {
/* 1377 */     return XMLChar.isNCName(value);
/*      */   }
/*      */ 
/*      */   protected boolean isValidNameStartChar(int value)
/*      */   {
/* 1384 */     return XMLChar.isNameStart(value);
/*      */   }
/*      */ 
/*      */   protected boolean versionSupported(String version) {
/* 1388 */     return (version.equals("1.0")) || (version.equals("1.1"));
/*      */   }
/*      */ 
/*      */   protected boolean scanSurrogates(XMLStringBuffer buf)
/*      */     throws IOException, XNIException
/*      */   {
/* 1403 */     int high = this.fEntityScanner.scanChar();
/* 1404 */     int low = this.fEntityScanner.peekChar();
/* 1405 */     if (!XMLChar.isLowSurrogate(low)) {
/* 1406 */       reportFatalError("InvalidCharInContent", new Object[] { Integer.toString(high, 16) });
/*      */ 
/* 1408 */       return false;
/*      */     }
/* 1410 */     this.fEntityScanner.scanChar();
/*      */ 
/* 1413 */     int c = XMLChar.supplemental((char)high, (char)low);
/*      */ 
/* 1416 */     if (isInvalid(c)) {
/* 1417 */       reportFatalError("InvalidCharInContent", new Object[] { Integer.toString(c, 16) });
/*      */ 
/* 1419 */       return false;
/*      */     }
/*      */ 
/* 1423 */     buf.append((char)high);
/* 1424 */     buf.append((char)low);
/*      */ 
/* 1426 */     return true;
/*      */   }
/*      */ 
/*      */   protected void reportFatalError(String msgId, Object[] args)
/*      */     throws XNIException
/*      */   {
/* 1436 */     this.fErrorReporter.reportError(this.fEntityScanner, "http://www.w3.org/TR/1998/REC-xml-19980210", msgId, args, (short)2);
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/* 1444 */     this.fEntityScanner = null;
/*      */ 
/* 1446 */     this.fEntityDepth = 0;
/* 1447 */     this.fReportEntity = true;
/* 1448 */     this.fResourceIdentifier.clear();
/*      */ 
/* 1450 */     if (!this.fAttributeCacheInitDone) {
/* 1451 */       for (int i = 0; i < this.initialCacheCount; i++) {
/* 1452 */         this.attributeValueCache.add(new XMLString());
/* 1453 */         this.stringBufferCache.add(new XMLStringBuffer());
/*      */       }
/* 1455 */       this.fAttributeCacheInitDone = true;
/*      */     }
/* 1457 */     this.fStringBufferIndex = 0;
/* 1458 */     this.fAttributeCacheUsedCount = 0;
/*      */   }
/*      */ 
/*      */   XMLStringBuffer getStringBuffer()
/*      */   {
/* 1463 */     if ((this.fStringBufferIndex < this.initialCacheCount) || (this.fStringBufferIndex < this.stringBufferCache.size())) {
/* 1464 */       return (XMLStringBuffer)this.stringBufferCache.get(this.fStringBufferIndex++);
/*      */     }
/* 1466 */     XMLStringBuffer tmpObj = new XMLStringBuffer();
/* 1467 */     this.fStringBufferIndex += 1;
/* 1468 */     this.stringBufferCache.add(tmpObj);
/* 1469 */     return tmpObj;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLScanner
 * JD-Core Version:    0.6.2
 */