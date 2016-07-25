/*      */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelFilter;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDFilter;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ public class XMLDTDProcessor
/*      */   implements XMLComponent, XMLDTDFilter, XMLDTDContentModelFilter
/*      */ {
/*      */   private static final int TOP_LEVEL_SCOPE = -1;
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
/*      */   protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
/*      */   protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
/*      */   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
/*  170 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/warn-on-duplicate-attdef", "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", "http://apache.org/xml/features/scanner/notify-char-refs" };
/*      */ 
/*  178 */   private static final Boolean[] FEATURE_DEFAULTS = { null, Boolean.FALSE, Boolean.FALSE, null };
/*      */ 
/*  186 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/validator/dtd" };
/*      */ 
/*  194 */   private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null };
/*      */   protected boolean fValidation;
/*      */   protected boolean fDTDValidation;
/*      */   protected boolean fWarnDuplicateAttdef;
/*      */   protected boolean fWarnOnUndeclaredElemdef;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected XMLErrorReporter fErrorReporter;
/*      */   protected DTDGrammarBucket fGrammarBucket;
/*      */   protected XMLDTDValidator fValidator;
/*      */   protected XMLGrammarPool fGrammarPool;
/*      */   protected Locale fLocale;
/*      */   protected XMLDTDHandler fDTDHandler;
/*      */   protected XMLDTDSource fDTDSource;
/*      */   protected XMLDTDContentModelHandler fDTDContentModelHandler;
/*      */   protected XMLDTDContentModelSource fDTDContentModelSource;
/*      */   protected DTDGrammar fDTDGrammar;
/*      */   private boolean fPerformValidation;
/*      */   protected boolean fInDTDIgnore;
/*      */   private boolean fMixed;
/*  280 */   private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
/*      */ 
/*  283 */   private final HashMap fNDataDeclNotations = new HashMap();
/*      */ 
/*  286 */   private String fDTDElementDeclName = null;
/*      */ 
/*  289 */   private final ArrayList fMixedElementTypes = new ArrayList();
/*      */ 
/*  292 */   private final ArrayList fDTDElementDecls = new ArrayList();
/*      */   private HashMap fTableOfIDAttributeNames;
/*      */   private HashMap fTableOfNOTATIONAttributeNames;
/*      */   private HashMap fNotationEnumVals;
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/*  337 */     boolean parser_settings = componentManager.getFeature("http://apache.org/xml/features/internal/parser-settings", true);
/*      */ 
/*  339 */     if (!parser_settings)
/*      */     {
/*  341 */       reset();
/*  342 */       return;
/*      */     }
/*      */ 
/*  346 */     this.fValidation = componentManager.getFeature("http://xml.org/sax/features/validation", false);
/*      */ 
/*  348 */     this.fDTDValidation = (!componentManager.getFeature("http://apache.org/xml/features/validation/schema", false));
/*      */ 
/*  355 */     this.fWarnDuplicateAttdef = componentManager.getFeature("http://apache.org/xml/features/validation/warn-on-duplicate-attdef", false);
/*  356 */     this.fWarnOnUndeclaredElemdef = componentManager.getFeature("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", false);
/*      */ 
/*  359 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*      */ 
/*  362 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*      */ 
/*  366 */     this.fGrammarPool = ((XMLGrammarPool)componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null));
/*      */     try
/*      */     {
/*  369 */       this.fValidator = ((XMLDTDValidator)componentManager.getProperty("http://apache.org/xml/properties/internal/validator/dtd", null));
/*      */     } catch (ClassCastException e) {
/*  371 */       this.fValidator = null;
/*      */     }
/*      */ 
/*  374 */     if (this.fValidator != null)
/*  375 */       this.fGrammarBucket = this.fValidator.getGrammarBucket();
/*      */     else {
/*  377 */       this.fGrammarBucket = null;
/*      */     }
/*  379 */     reset();
/*      */   }
/*      */ 
/*      */   protected void reset()
/*      */   {
/*  385 */     this.fDTDGrammar = null;
/*      */ 
/*  387 */     this.fInDTDIgnore = false;
/*      */ 
/*  389 */     this.fNDataDeclNotations.clear();
/*      */ 
/*  392 */     if (this.fValidation)
/*      */     {
/*  394 */       if (this.fNotationEnumVals == null) {
/*  395 */         this.fNotationEnumVals = new HashMap();
/*      */       }
/*  397 */       this.fNotationEnumVals.clear();
/*      */ 
/*  399 */       this.fTableOfIDAttributeNames = new HashMap();
/*  400 */       this.fTableOfNOTATIONAttributeNames = new HashMap();
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/*  410 */     return (String[])RECOGNIZED_FEATURES.clone();
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/*  438 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/*  470 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/*  471 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/*  472 */         return FEATURE_DEFAULTS[i];
/*      */       }
/*      */     }
/*  475 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/*  488 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/*  489 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/*  490 */         return PROPERTY_DEFAULTS[i];
/*      */       }
/*      */     }
/*  493 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(XMLDTDHandler dtdHandler)
/*      */   {
/*  506 */     this.fDTDHandler = dtdHandler;
/*      */   }
/*      */ 
/*      */   public XMLDTDHandler getDTDHandler()
/*      */   {
/*  515 */     return this.fDTDHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDContentModelHandler(XMLDTDContentModelHandler dtdContentModelHandler)
/*      */   {
/*  528 */     this.fDTDContentModelHandler = dtdContentModelHandler;
/*      */   }
/*      */ 
/*      */   public XMLDTDContentModelHandler getDTDContentModelHandler()
/*      */   {
/*  537 */     return this.fDTDContentModelHandler;
/*      */   }
/*      */ 
/*      */   public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  554 */     if (this.fDTDGrammar != null)
/*  555 */       this.fDTDGrammar.startExternalSubset(identifier, augs);
/*  556 */     if (this.fDTDHandler != null)
/*  557 */       this.fDTDHandler.startExternalSubset(identifier, augs);
/*      */   }
/*      */ 
/*      */   public void endExternalSubset(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  570 */     if (this.fDTDGrammar != null)
/*  571 */       this.fDTDGrammar.endExternalSubset(augs);
/*  572 */     if (this.fDTDHandler != null)
/*  573 */       this.fDTDHandler.endExternalSubset(augs);
/*      */   }
/*      */ 
/*      */   protected static void checkStandaloneEntityRef(String name, DTDGrammar grammar, XMLEntityDecl tempEntityDecl, XMLErrorReporter errorReporter)
/*      */     throws XNIException
/*      */   {
/*  591 */     int entIndex = grammar.getEntityDeclIndex(name);
/*  592 */     if (entIndex > -1) {
/*  593 */       grammar.getEntityDecl(entIndex, tempEntityDecl);
/*  594 */       if (tempEntityDecl.inExternal)
/*  595 */         errorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { name }, (short)1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  613 */     if (this.fDTDGrammar != null)
/*  614 */       this.fDTDGrammar.comment(text, augs);
/*  615 */     if (this.fDTDHandler != null)
/*  616 */       this.fDTDHandler.comment(text, augs);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  643 */     if (this.fDTDGrammar != null)
/*  644 */       this.fDTDGrammar.processingInstruction(target, data, augs);
/*  645 */     if (this.fDTDHandler != null)
/*  646 */       this.fDTDHandler.processingInstruction(target, data, augs);
/*      */   }
/*      */ 
/*      */   public void startDTD(XMLLocator locator, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  672 */     this.fNDataDeclNotations.clear();
/*  673 */     this.fDTDElementDecls.clear();
/*      */ 
/*  679 */     if (!this.fGrammarBucket.getActiveGrammar().isImmutable()) {
/*  680 */       this.fDTDGrammar = this.fGrammarBucket.getActiveGrammar();
/*      */     }
/*      */ 
/*  684 */     if (this.fDTDGrammar != null)
/*  685 */       this.fDTDGrammar.startDTD(locator, augs);
/*  686 */     if (this.fDTDHandler != null)
/*  687 */       this.fDTDHandler.startDTD(locator, augs);
/*      */   }
/*      */ 
/*      */   public void ignoredCharacters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  704 */     if (this.fDTDGrammar != null)
/*  705 */       this.fDTDGrammar.ignoredCharacters(text, augs);
/*  706 */     if (this.fDTDHandler != null)
/*  707 */       this.fDTDHandler.ignoredCharacters(text, augs);
/*      */   }
/*      */ 
/*      */   public void textDecl(String version, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  728 */     if (this.fDTDGrammar != null)
/*  729 */       this.fDTDGrammar.textDecl(version, encoding, augs);
/*  730 */     if (this.fDTDHandler != null)
/*  731 */       this.fDTDHandler.textDecl(version, encoding, augs);
/*      */   }
/*      */ 
/*      */   public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  755 */     if ((this.fPerformValidation) && (this.fDTDGrammar != null) && (this.fGrammarBucket.getStandalone()))
/*      */     {
/*  757 */       checkStandaloneEntityRef(name, this.fDTDGrammar, this.fEntityDecl, this.fErrorReporter);
/*      */     }
/*      */ 
/*  760 */     if (this.fDTDGrammar != null)
/*  761 */       this.fDTDGrammar.startParameterEntity(name, identifier, encoding, augs);
/*  762 */     if (this.fDTDHandler != null)
/*  763 */       this.fDTDHandler.startParameterEntity(name, identifier, encoding, augs);
/*      */   }
/*      */ 
/*      */   public void endParameterEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  780 */     if (this.fDTDGrammar != null)
/*  781 */       this.fDTDGrammar.endParameterEntity(name, augs);
/*  782 */     if (this.fDTDHandler != null)
/*  783 */       this.fDTDHandler.endParameterEntity(name, augs);
/*      */   }
/*      */ 
/*      */   public void elementDecl(String name, String contentModel, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  801 */     if (this.fValidation) {
/*  802 */       if (this.fDTDElementDecls.contains(name)) {
/*  803 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_ALREADY_DECLARED", new Object[] { name }, (short)1);
/*      */       }
/*      */       else
/*      */       {
/*  809 */         this.fDTDElementDecls.add(name);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  814 */     if (this.fDTDGrammar != null)
/*  815 */       this.fDTDGrammar.elementDecl(name, contentModel, augs);
/*  816 */     if (this.fDTDHandler != null)
/*  817 */       this.fDTDHandler.elementDecl(name, contentModel, augs);
/*      */   }
/*      */ 
/*      */   public void startAttlist(String elementName, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  836 */     if (this.fDTDGrammar != null)
/*  837 */       this.fDTDGrammar.startAttlist(elementName, augs);
/*  838 */     if (this.fDTDHandler != null)
/*  839 */       this.fDTDHandler.startAttlist(elementName, augs);
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  874 */     if ((type != XMLSymbols.fCDATASymbol) && (defaultValue != null)) {
/*  875 */       normalizeDefaultAttrValue(defaultValue);
/*      */     }
/*      */ 
/*  878 */     if (this.fValidation)
/*      */     {
/*  880 */       boolean duplicateAttributeDef = false;
/*      */ 
/*  883 */       DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
/*  884 */       int elementIndex = grammar.getElementDeclIndex(elementName);
/*  885 */       if (grammar.getAttributeDeclIndex(elementIndex, attributeName) != -1)
/*      */       {
/*  887 */         duplicateAttributeDef = true;
/*      */ 
/*  890 */         if (this.fWarnDuplicateAttdef) {
/*  891 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ATTRIBUTE_DEFINITION", new Object[] { elementName, attributeName }, (short)0);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  903 */       if (type == XMLSymbols.fIDSymbol) {
/*  904 */         if ((defaultValue != null) && (defaultValue.length != 0) && (
/*  905 */           (defaultType == null) || ((defaultType != XMLSymbols.fIMPLIEDSymbol) && (defaultType != XMLSymbols.fREQUIREDSymbol))))
/*      */         {
/*  908 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IDDefaultTypeInvalid", new Object[] { attributeName }, (short)1);
/*      */         }
/*      */ 
/*  915 */         if (!this.fTableOfIDAttributeNames.containsKey(elementName)) {
/*  916 */           this.fTableOfIDAttributeNames.put(elementName, attributeName);
/*      */         }
/*  931 */         else if (!duplicateAttributeDef) {
/*  932 */           String previousIDAttributeName = (String)this.fTableOfIDAttributeNames.get(elementName);
/*  933 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_MORE_THAN_ONE_ID_ATTRIBUTE", new Object[] { elementName, previousIDAttributeName, attributeName }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  945 */       if (type == XMLSymbols.fNOTATIONSymbol)
/*      */       {
/*  948 */         for (int i = 0; i < enumeration.length; i++) {
/*  949 */           this.fNotationEnumVals.put(enumeration[i], attributeName);
/*      */         }
/*      */ 
/*  952 */         if (!this.fTableOfNOTATIONAttributeNames.containsKey(elementName)) {
/*  953 */           this.fTableOfNOTATIONAttributeNames.put(elementName, attributeName);
/*      */         }
/*  963 */         else if (!duplicateAttributeDef)
/*      */         {
/*  965 */           String previousNOTATIONAttributeName = (String)this.fTableOfNOTATIONAttributeNames.get(elementName);
/*  966 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_MORE_THAN_ONE_NOTATION_ATTRIBUTE", new Object[] { elementName, previousNOTATIONAttributeName, attributeName }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  976 */       if ((type == XMLSymbols.fENUMERATIONSymbol) || (type == XMLSymbols.fNOTATIONSymbol))
/*      */       {
/*  978 */         for (int i = 0; i < enumeration.length; i++) {
/*  979 */           for (int j = i + 1; j < enumeration.length; j++) {
/*  980 */             if (enumeration[i].equals(enumeration[j]))
/*      */             {
/*  984 */               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", type == XMLSymbols.fENUMERATIONSymbol ? "MSG_DISTINCT_TOKENS_IN_ENUMERATION" : "MSG_DISTINCT_NOTATION_IN_ENUMERATION", new Object[] { elementName, enumeration[i], attributeName }, (short)1);
/*      */ 
/*  990 */               break label466;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  997 */       label466: boolean ok = true;
/*  998 */       if ((defaultValue != null) && ((defaultType == null) || ((defaultType != null) && (defaultType == XMLSymbols.fFIXEDSymbol))))
/*      */       {
/* 1002 */         String value = defaultValue.toString();
/* 1003 */         if ((type == XMLSymbols.fNMTOKENSSymbol) || (type == XMLSymbols.fENTITIESSymbol) || (type == XMLSymbols.fIDREFSSymbol))
/*      */         {
/* 1007 */           StringTokenizer tokenizer = new StringTokenizer(value, " ");
/* 1008 */           if (tokenizer.hasMoreTokens()) {
/*      */             while (true) {
/* 1010 */               String nmtoken = tokenizer.nextToken();
/* 1011 */               if (type == XMLSymbols.fNMTOKENSSymbol) {
/* 1012 */                 if (!isValidNmtoken(nmtoken)) {
/* 1013 */                   ok = false;
/* 1014 */                   break;
/*      */                 }
/*      */               }
/* 1017 */               else if ((type == XMLSymbols.fENTITIESSymbol) || (type == XMLSymbols.fIDREFSSymbol))
/*      */               {
/* 1019 */                 if (!isValidName(nmtoken)) {
/* 1020 */                   ok = false;
/* 1021 */                   break;
/*      */                 }
/*      */               }
/* 1024 */               if (!tokenizer.hasMoreTokens()) {
/*      */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1032 */           if ((type == XMLSymbols.fENTITYSymbol) || (type == XMLSymbols.fIDSymbol) || (type == XMLSymbols.fIDREFSymbol) || (type == XMLSymbols.fNOTATIONSymbol))
/*      */           {
/* 1037 */             if (!isValidName(value)) {
/* 1038 */               ok = false;
/*      */             }
/*      */ 
/*      */           }
/* 1042 */           else if ((type == XMLSymbols.fNMTOKENSymbol) || (type == XMLSymbols.fENUMERATIONSymbol))
/*      */           {
/* 1045 */             if (!isValidNmtoken(value)) {
/* 1046 */               ok = false;
/*      */             }
/*      */           }
/*      */ 
/* 1050 */           if ((type == XMLSymbols.fNOTATIONSymbol) || (type == XMLSymbols.fENUMERATIONSymbol))
/*      */           {
/* 1052 */             ok = false;
/* 1053 */             for (int i = 0; i < enumeration.length; i++) {
/* 1054 */               if (defaultValue.equals(enumeration[i])) {
/* 1055 */                 ok = true;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1061 */         if (!ok) {
/* 1062 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATT_DEFAULT_INVALID", new Object[] { attributeName, value }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1071 */     if (this.fDTDGrammar != null) {
/* 1072 */       this.fDTDGrammar.attributeDecl(elementName, attributeName, type, enumeration, defaultType, defaultValue, nonNormalizedDefaultValue, augs);
/*      */     }
/*      */ 
/* 1075 */     if (this.fDTDHandler != null)
/* 1076 */       this.fDTDHandler.attributeDecl(elementName, attributeName, type, enumeration, defaultType, defaultValue, nonNormalizedDefaultValue, augs);
/*      */   }
/*      */ 
/*      */   public void endAttlist(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1094 */     if (this.fDTDGrammar != null)
/* 1095 */       this.fDTDGrammar.endAttlist(augs);
/* 1096 */     if (this.fDTDHandler != null)
/* 1097 */       this.fDTDHandler.endAttlist(augs);
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1122 */     DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
/* 1123 */     int index = grammar.getEntityDeclIndex(name);
/*      */ 
/* 1133 */     if (index == -1)
/*      */     {
/* 1135 */       if (this.fDTDGrammar != null) {
/* 1136 */         this.fDTDGrammar.internalEntityDecl(name, text, nonNormalizedText, augs);
/*      */       }
/* 1138 */       if (this.fDTDHandler != null)
/* 1139 */         this.fDTDHandler.internalEntityDecl(name, text, nonNormalizedText, augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1162 */     DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
/* 1163 */     int index = grammar.getEntityDeclIndex(name);
/*      */ 
/* 1173 */     if (index == -1)
/*      */     {
/* 1175 */       if (this.fDTDGrammar != null) {
/* 1176 */         this.fDTDGrammar.externalEntityDecl(name, identifier, augs);
/*      */       }
/* 1178 */       if (this.fDTDHandler != null)
/* 1179 */         this.fDTDHandler.externalEntityDecl(name, identifier, augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1202 */     if (this.fValidation) {
/* 1203 */       this.fNDataDeclNotations.put(name, notation);
/*      */     }
/*      */ 
/* 1207 */     if (this.fDTDGrammar != null)
/* 1208 */       this.fDTDGrammar.unparsedEntityDecl(name, identifier, notation, augs);
/* 1209 */     if (this.fDTDHandler != null)
/* 1210 */       this.fDTDHandler.unparsedEntityDecl(name, identifier, notation, augs);
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1230 */     if (this.fValidation) {
/* 1231 */       DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
/* 1232 */       if (grammar.getNotationDeclIndex(name) != -1) {
/* 1233 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "UniqueNotationName", new Object[] { name }, (short)1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1241 */     if (this.fDTDGrammar != null)
/* 1242 */       this.fDTDGrammar.notationDecl(name, identifier, augs);
/* 1243 */     if (this.fDTDHandler != null)
/* 1244 */       this.fDTDHandler.notationDecl(name, identifier, augs);
/*      */   }
/*      */ 
/*      */   public void startConditional(short type, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1265 */     this.fInDTDIgnore = (type == 1);
/*      */ 
/* 1268 */     if (this.fDTDGrammar != null)
/* 1269 */       this.fDTDGrammar.startConditional(type, augs);
/* 1270 */     if (this.fDTDHandler != null)
/* 1271 */       this.fDTDHandler.startConditional(type, augs);
/*      */   }
/*      */ 
/*      */   public void endConditional(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1287 */     this.fInDTDIgnore = false;
/*      */ 
/* 1290 */     if (this.fDTDGrammar != null)
/* 1291 */       this.fDTDGrammar.endConditional(augs);
/* 1292 */     if (this.fDTDHandler != null)
/* 1293 */       this.fDTDHandler.endConditional(augs);
/*      */   }
/*      */ 
/*      */   public void endDTD(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1310 */     if (this.fDTDGrammar != null) {
/* 1311 */       this.fDTDGrammar.endDTD(augs);
/* 1312 */       if (this.fGrammarPool != null)
/* 1313 */         this.fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[] { this.fDTDGrammar });
/*      */     }
/* 1315 */     if (this.fValidation) {
/* 1316 */       DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
/*      */ 
/* 1319 */       Iterator entities = this.fNDataDeclNotations.entrySet().iterator();
/* 1320 */       while (entities.hasNext()) {
/* 1321 */         Map.Entry entry = (Map.Entry)entities.next();
/* 1322 */         String notation = (String)entry.getValue();
/* 1323 */         if (grammar.getNotationDeclIndex(notation) == -1) {
/* 1324 */           String entity = (String)entry.getKey();
/* 1325 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_NOTATION_NOT_DECLARED_FOR_UNPARSED_ENTITYDECL", new Object[] { entity, notation }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1334 */       Iterator notationVals = this.fNotationEnumVals.entrySet().iterator();
/* 1335 */       while (notationVals.hasNext()) {
/* 1336 */         Map.Entry entry = (Map.Entry)notationVals.next();
/* 1337 */         String notation = (String)entry.getKey();
/* 1338 */         if (grammar.getNotationDeclIndex(notation) == -1) {
/* 1339 */           String attributeName = (String)entry.getValue();
/* 1340 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_NOTATION_NOT_DECLARED_FOR_NOTATIONTYPE_ATTRIBUTE", new Object[] { attributeName, notation }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1349 */       Iterator elementsWithNotations = this.fTableOfNOTATIONAttributeNames.entrySet().iterator();
/* 1350 */       while (elementsWithNotations.hasNext()) {
/* 1351 */         Map.Entry entry = (Map.Entry)elementsWithNotations.next();
/* 1352 */         String elementName = (String)entry.getKey();
/* 1353 */         int elementIndex = grammar.getElementDeclIndex(elementName);
/* 1354 */         if (grammar.getContentSpecType(elementIndex) == 1) {
/* 1355 */           String attributeName = (String)entry.getValue();
/* 1356 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NoNotationOnEmptyElement", new Object[] { elementName, attributeName }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1364 */       this.fTableOfIDAttributeNames = null;
/* 1365 */       this.fTableOfNOTATIONAttributeNames = null;
/*      */ 
/* 1368 */       if (this.fWarnOnUndeclaredElemdef) {
/* 1369 */         checkDeclaredElements(grammar);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1374 */     if (this.fDTDHandler != null)
/* 1375 */       this.fDTDHandler.endDTD(augs);
/*      */   }
/*      */ 
/*      */   public void setDTDSource(XMLDTDSource source)
/*      */   {
/* 1382 */     this.fDTDSource = source;
/*      */   }
/*      */ 
/*      */   public XMLDTDSource getDTDSource()
/*      */   {
/* 1387 */     return this.fDTDSource;
/*      */   }
/*      */ 
/*      */   public void setDTDContentModelSource(XMLDTDContentModelSource source)
/*      */   {
/* 1396 */     this.fDTDContentModelSource = source;
/*      */   }
/*      */ 
/*      */   public XMLDTDContentModelSource getDTDContentModelSource()
/*      */   {
/* 1401 */     return this.fDTDContentModelSource;
/*      */   }
/*      */ 
/*      */   public void startContentModel(String elementName, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1419 */     if (this.fValidation) {
/* 1420 */       this.fDTDElementDeclName = elementName;
/* 1421 */       this.fMixedElementTypes.clear();
/*      */     }
/*      */ 
/* 1425 */     if (this.fDTDGrammar != null)
/* 1426 */       this.fDTDGrammar.startContentModel(elementName, augs);
/* 1427 */     if (this.fDTDContentModelHandler != null)
/* 1428 */       this.fDTDContentModelHandler.startContentModel(elementName, augs);
/*      */   }
/*      */ 
/*      */   public void any(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1445 */     if (this.fDTDGrammar != null)
/* 1446 */       this.fDTDGrammar.any(augs);
/* 1447 */     if (this.fDTDContentModelHandler != null)
/* 1448 */       this.fDTDContentModelHandler.any(augs);
/*      */   }
/*      */ 
/*      */   public void empty(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1464 */     if (this.fDTDGrammar != null)
/* 1465 */       this.fDTDGrammar.empty(augs);
/* 1466 */     if (this.fDTDContentModelHandler != null)
/* 1467 */       this.fDTDContentModelHandler.empty(augs);
/*      */   }
/*      */ 
/*      */   public void startGroup(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1487 */     this.fMixed = false;
/*      */ 
/* 1489 */     if (this.fDTDGrammar != null)
/* 1490 */       this.fDTDGrammar.startGroup(augs);
/* 1491 */     if (this.fDTDContentModelHandler != null)
/* 1492 */       this.fDTDContentModelHandler.startGroup(augs);
/*      */   }
/*      */ 
/*      */   public void pcdata(Augmentations augs)
/*      */   {
/* 1510 */     this.fMixed = true;
/* 1511 */     if (this.fDTDGrammar != null)
/* 1512 */       this.fDTDGrammar.pcdata(augs);
/* 1513 */     if (this.fDTDContentModelHandler != null)
/* 1514 */       this.fDTDContentModelHandler.pcdata(augs);
/*      */   }
/*      */ 
/*      */   public void element(String elementName, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1530 */     if ((this.fMixed) && (this.fValidation)) {
/* 1531 */       if (this.fMixedElementTypes.contains(elementName)) {
/* 1532 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "DuplicateTypeInMixedContent", new Object[] { this.fDTDElementDeclName, elementName }, (short)1);
/*      */       }
/*      */       else
/*      */       {
/* 1538 */         this.fMixedElementTypes.add(elementName);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1543 */     if (this.fDTDGrammar != null)
/* 1544 */       this.fDTDGrammar.element(elementName, augs);
/* 1545 */     if (this.fDTDContentModelHandler != null)
/* 1546 */       this.fDTDContentModelHandler.element(elementName, augs);
/*      */   }
/*      */ 
/*      */   public void separator(short separator, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1568 */     if (this.fDTDGrammar != null)
/* 1569 */       this.fDTDGrammar.separator(separator, augs);
/* 1570 */     if (this.fDTDContentModelHandler != null)
/* 1571 */       this.fDTDContentModelHandler.separator(separator, augs);
/*      */   }
/*      */ 
/*      */   public void occurrence(short occurrence, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1595 */     if (this.fDTDGrammar != null)
/* 1596 */       this.fDTDGrammar.occurrence(occurrence, augs);
/* 1597 */     if (this.fDTDContentModelHandler != null)
/* 1598 */       this.fDTDContentModelHandler.occurrence(occurrence, augs);
/*      */   }
/*      */ 
/*      */   public void endGroup(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1614 */     if (this.fDTDGrammar != null)
/* 1615 */       this.fDTDGrammar.endGroup(augs);
/* 1616 */     if (this.fDTDContentModelHandler != null)
/* 1617 */       this.fDTDContentModelHandler.endGroup(augs);
/*      */   }
/*      */ 
/*      */   public void endContentModel(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1633 */     if (this.fDTDGrammar != null)
/* 1634 */       this.fDTDGrammar.endContentModel(augs);
/* 1635 */     if (this.fDTDContentModelHandler != null)
/* 1636 */       this.fDTDContentModelHandler.endContentModel(augs);
/*      */   }
/*      */ 
/*      */   private boolean normalizeDefaultAttrValue(XMLString value)
/*      */   {
/* 1654 */     boolean skipSpace = true;
/* 1655 */     int current = value.offset;
/* 1656 */     int end = value.offset + value.length;
/* 1657 */     for (int i = value.offset; i < end; i++) {
/* 1658 */       if (value.ch[i] == ' ') {
/* 1659 */         if (!skipSpace)
/*      */         {
/* 1661 */           value.ch[(current++)] = ' ';
/* 1662 */           skipSpace = true;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1670 */         if (current != i) {
/* 1671 */           value.ch[current] = value.ch[i];
/*      */         }
/* 1673 */         current++;
/* 1674 */         skipSpace = false;
/*      */       }
/*      */     }
/* 1677 */     if (current != end) {
/* 1678 */       if (skipSpace)
/*      */       {
/* 1680 */         current--;
/*      */       }
/*      */ 
/* 1683 */       value.length = (current - value.offset);
/* 1684 */       return true;
/*      */     }
/* 1686 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean isValidNmtoken(String nmtoken) {
/* 1690 */     return XMLChar.isValidNmtoken(nmtoken);
/*      */   }
/*      */ 
/*      */   protected boolean isValidName(String name) {
/* 1694 */     return XMLChar.isValidName(name);
/*      */   }
/*      */ 
/*      */   private void checkDeclaredElements(DTDGrammar grammar)
/*      */   {
/* 1703 */     int elementIndex = grammar.getFirstElementDeclIndex();
/* 1704 */     XMLContentSpec contentSpec = new XMLContentSpec();
/* 1705 */     while (elementIndex >= 0) {
/* 1706 */       int type = grammar.getContentSpecType(elementIndex);
/* 1707 */       if ((type == 3) || (type == 2)) {
/* 1708 */         checkDeclaredElements(grammar, elementIndex, grammar.getContentSpecIndex(elementIndex), contentSpec);
/*      */       }
/*      */ 
/* 1713 */       elementIndex = grammar.getNextElementDeclIndex(elementIndex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkDeclaredElements(DTDGrammar grammar, int elementIndex, int contentSpecIndex, XMLContentSpec contentSpec)
/*      */   {
/* 1724 */     grammar.getContentSpec(contentSpecIndex, contentSpec);
/* 1725 */     if (contentSpec.type == 0) {
/* 1726 */       String value = (String)contentSpec.value;
/* 1727 */       if ((value != null) && (grammar.getElementDeclIndex(value) == -1)) {
/* 1728 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "UndeclaredElementInContentSpec", new Object[] { grammar.getElementDeclName(elementIndex).rawname, value }, (short)0);
/*      */       }
/*      */ 
/*      */     }
/* 1736 */     else if ((contentSpec.type == 4) || (contentSpec.type == 5))
/*      */     {
/* 1738 */       int leftNode = ((int[])(int[])contentSpec.value)[0];
/* 1739 */       int rightNode = ((int[])(int[])contentSpec.otherValue)[0];
/*      */ 
/* 1741 */       checkDeclaredElements(grammar, elementIndex, leftNode, contentSpec);
/* 1742 */       checkDeclaredElements(grammar, elementIndex, rightNode, contentSpec);
/*      */     }
/* 1744 */     else if ((contentSpec.type == 2) || (contentSpec.type == 1) || (contentSpec.type == 3))
/*      */     {
/* 1747 */       int leftNode = ((int[])(int[])contentSpec.value)[0];
/* 1748 */       checkDeclaredElements(grammar, elementIndex, leftNode, contentSpec);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor
 * JD-Core Version:    0.6.2
 */