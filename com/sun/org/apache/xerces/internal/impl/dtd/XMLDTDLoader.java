/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.DefaultErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.util.Status;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class XMLDTDLoader extends XMLDTDProcessor
/*     */   implements XMLGrammarLoader
/*     */ {
/*     */   protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
/*     */   protected static final String BALANCE_SYNTAX_TREES = "http://apache.org/xml/features/validation/balance-syntax-trees";
/* 132 */   private static final String[] LOADER_RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/warn-on-duplicate-attdef", "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", "http://apache.org/xml/features/scanner/notify-char-refs", "http://apache.org/xml/features/standard-uri-conformant", "http://apache.org/xml/features/validation/balance-syntax-trees" };
/*     */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*     */   public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*     */   public static final String LOCALE = "http://apache.org/xml/properties/locale";
/* 156 */   private static final String[] LOADER_RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/locale" };
/*     */ 
/* 167 */   private boolean fStrictURI = false;
/*     */ 
/* 170 */   private boolean fBalanceSyntaxTrees = false;
/*     */   protected XMLEntityResolver fEntityResolver;
/*     */   protected XMLDTDScannerImpl fDTDScanner;
/*     */   protected XMLEntityManager fEntityManager;
/*     */   protected Locale fLocale;
/*     */ 
/*     */   public XMLDTDLoader()
/*     */   {
/* 190 */     this(new SymbolTable());
/*     */   }
/*     */ 
/*     */   public XMLDTDLoader(SymbolTable symbolTable) {
/* 194 */     this(symbolTable, null);
/*     */   }
/*     */ 
/*     */   public XMLDTDLoader(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 199 */     this(symbolTable, grammarPool, null, new XMLEntityManager());
/*     */   }
/*     */ 
/*     */   XMLDTDLoader(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLErrorReporter errorReporter, XMLEntityResolver entityResolver)
/*     */   {
/* 205 */     this.fSymbolTable = symbolTable;
/* 206 */     this.fGrammarPool = grammarPool;
/* 207 */     if (errorReporter == null) {
/* 208 */       errorReporter = new XMLErrorReporter();
/* 209 */       errorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", new DefaultErrorHandler());
/*     */     }
/* 211 */     this.fErrorReporter = errorReporter;
/*     */ 
/* 213 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/* 214 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/* 215 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/* 216 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*     */     }
/* 218 */     this.fEntityResolver = entityResolver;
/* 219 */     if ((this.fEntityResolver instanceof XMLEntityManager))
/* 220 */       this.fEntityManager = ((XMLEntityManager)this.fEntityResolver);
/*     */     else {
/* 222 */       this.fEntityManager = new XMLEntityManager();
/*     */     }
/* 224 */     this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/error-reporter", errorReporter);
/* 225 */     this.fDTDScanner = createDTDScanner(this.fSymbolTable, this.fErrorReporter, this.fEntityManager);
/* 226 */     this.fDTDScanner.setDTDHandler(this);
/* 227 */     this.fDTDScanner.setDTDContentModelHandler(this);
/* 228 */     reset();
/*     */   }
/*     */ 
/*     */   public String[] getRecognizedFeatures()
/*     */   {
/* 239 */     return (String[])LOADER_RECOGNIZED_FEATURES.clone();
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */     throws XMLConfigurationException
/*     */   {
/* 259 */     if (featureId.equals("http://xml.org/sax/features/validation")) {
/* 260 */       this.fValidation = state;
/*     */     }
/* 262 */     else if (featureId.equals("http://apache.org/xml/features/validation/warn-on-duplicate-attdef")) {
/* 263 */       this.fWarnDuplicateAttdef = state;
/*     */     }
/* 265 */     else if (featureId.equals("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef")) {
/* 266 */       this.fWarnOnUndeclaredElemdef = state;
/*     */     }
/* 268 */     else if (featureId.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
/* 269 */       this.fDTDScanner.setFeature(featureId, state);
/*     */     }
/* 271 */     else if (featureId.equals("http://apache.org/xml/features/standard-uri-conformant")) {
/* 272 */       this.fStrictURI = state;
/*     */     }
/* 274 */     else if (featureId.equals("http://apache.org/xml/features/validation/balance-syntax-trees")) {
/* 275 */       this.fBalanceSyntaxTrees = state;
/*     */     }
/*     */     else
/* 278 */       throw new XMLConfigurationException(Status.NOT_RECOGNIZED, featureId);
/*     */   }
/*     */ 
/*     */   public String[] getRecognizedProperties()
/*     */   {
/* 288 */     return (String[])LOADER_RECOGNIZED_PROPERTIES.clone();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 300 */     if (propertyId.equals("http://apache.org/xml/properties/internal/symbol-table")) {
/* 301 */       return this.fSymbolTable;
/*     */     }
/* 303 */     if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
/* 304 */       return this.fErrorReporter;
/*     */     }
/* 306 */     if (propertyId.equals("http://apache.org/xml/properties/internal/error-handler")) {
/* 307 */       return this.fErrorReporter.getErrorHandler();
/*     */     }
/* 309 */     if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
/* 310 */       return this.fEntityResolver;
/*     */     }
/* 312 */     if (propertyId.equals("http://apache.org/xml/properties/locale")) {
/* 313 */       return getLocale();
/*     */     }
/* 315 */     if (propertyId.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
/* 316 */       return this.fGrammarPool;
/*     */     }
/* 318 */     if (propertyId.equals("http://apache.org/xml/properties/internal/validator/dtd")) {
/* 319 */       return this.fValidator;
/*     */     }
/* 321 */     throw new XMLConfigurationException(Status.NOT_RECOGNIZED, propertyId);
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 341 */     if (propertyId.equals("http://apache.org/xml/properties/internal/symbol-table")) {
/* 342 */       this.fSymbolTable = ((SymbolTable)value);
/* 343 */       this.fDTDScanner.setProperty(propertyId, value);
/* 344 */       this.fEntityManager.setProperty(propertyId, value);
/*     */     }
/* 346 */     else if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
/* 347 */       this.fErrorReporter = ((XMLErrorReporter)value);
/*     */ 
/* 349 */       if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/* 350 */         XMLMessageFormatter xmft = new XMLMessageFormatter();
/* 351 */         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/* 352 */         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*     */       }
/* 354 */       this.fDTDScanner.setProperty(propertyId, value);
/* 355 */       this.fEntityManager.setProperty(propertyId, value);
/*     */     }
/* 357 */     else if (propertyId.equals("http://apache.org/xml/properties/internal/error-handler")) {
/* 358 */       this.fErrorReporter.setProperty(propertyId, value);
/*     */     }
/* 360 */     else if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
/* 361 */       this.fEntityResolver = ((XMLEntityResolver)value);
/* 362 */       this.fEntityManager.setProperty(propertyId, value);
/*     */     }
/* 364 */     else if (propertyId.equals("http://apache.org/xml/properties/locale")) {
/* 365 */       setLocale((Locale)value);
/*     */     }
/* 367 */     else if (propertyId.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
/* 368 */       this.fGrammarPool = ((XMLGrammarPool)value);
/*     */     }
/*     */     else {
/* 371 */       throw new XMLConfigurationException(Status.NOT_RECOGNIZED, propertyId);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 384 */     if (featureId.equals("http://xml.org/sax/features/validation")) {
/* 385 */       return this.fValidation;
/*     */     }
/* 387 */     if (featureId.equals("http://apache.org/xml/features/validation/warn-on-duplicate-attdef")) {
/* 388 */       return this.fWarnDuplicateAttdef;
/*     */     }
/* 390 */     if (featureId.equals("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef")) {
/* 391 */       return this.fWarnOnUndeclaredElemdef;
/*     */     }
/* 393 */     if (featureId.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
/* 394 */       return this.fDTDScanner.getFeature(featureId);
/*     */     }
/* 396 */     if (featureId.equals("http://apache.org/xml/features/standard-uri-conformant")) {
/* 397 */       return this.fStrictURI;
/*     */     }
/* 399 */     if (featureId.equals("http://apache.org/xml/features/validation/balance-syntax-trees")) {
/* 400 */       return this.fBalanceSyntaxTrees;
/*     */     }
/* 402 */     throw new XMLConfigurationException(Status.NOT_RECOGNIZED, featureId);
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/* 414 */     this.fLocale = locale;
/* 415 */     this.fErrorReporter.setLocale(locale);
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 420 */     return this.fLocale;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(XMLErrorHandler errorHandler)
/*     */   {
/* 430 */     this.fErrorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", errorHandler);
/*     */   }
/*     */ 
/*     */   public XMLErrorHandler getErrorHandler()
/*     */   {
/* 435 */     return this.fErrorReporter.getErrorHandler();
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(XMLEntityResolver entityResolver)
/*     */   {
/* 444 */     this.fEntityResolver = entityResolver;
/* 445 */     this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", entityResolver);
/*     */   }
/*     */ 
/*     */   public XMLEntityResolver getEntityResolver()
/*     */   {
/* 450 */     return this.fEntityResolver;
/*     */   }
/*     */ 
/*     */   public Grammar loadGrammar(XMLInputSource source)
/*     */     throws IOException, XNIException
/*     */   {
/* 465 */     reset();
/*     */ 
/* 467 */     String eid = XMLEntityManager.expandSystemId(source.getSystemId(), source.getBaseSystemId(), this.fStrictURI);
/* 468 */     XMLDTDDescription desc = new XMLDTDDescription(source.getPublicId(), source.getSystemId(), source.getBaseSystemId(), eid, null);
/* 469 */     if (!this.fBalanceSyntaxTrees) {
/* 470 */       this.fDTDGrammar = new DTDGrammar(this.fSymbolTable, desc);
/*     */     }
/*     */     else {
/* 473 */       this.fDTDGrammar = new BalancedDTDGrammar(this.fSymbolTable, desc);
/*     */     }
/* 475 */     this.fGrammarBucket = new DTDGrammarBucket();
/* 476 */     this.fGrammarBucket.setStandalone(false);
/* 477 */     this.fGrammarBucket.setActiveGrammar(this.fDTDGrammar);
/*     */     try
/*     */     {
/* 483 */       this.fDTDScanner.setInputSource(source);
/* 484 */       this.fDTDScanner.scanDTDExternalSubset(true);
/*     */     }
/*     */     catch (EOFException e)
/*     */     {
/*     */     }
/*     */     finally {
/* 490 */       this.fEntityManager.closeReaders();
/*     */     }
/* 492 */     if ((this.fDTDGrammar != null) && (this.fGrammarPool != null)) {
/* 493 */       this.fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[] { this.fDTDGrammar });
/*     */     }
/* 495 */     return this.fDTDGrammar;
/*     */   }
/*     */ 
/*     */   public void loadGrammarWithContext(XMLDTDValidator validator, String rootName, String publicId, String systemId, String baseSystemId, String internalSubset)
/*     */     throws IOException, XNIException
/*     */   {
/* 505 */     DTDGrammarBucket grammarBucket = validator.getGrammarBucket();
/* 506 */     DTDGrammar activeGrammar = grammarBucket.getActiveGrammar();
/* 507 */     if ((activeGrammar != null) && (!activeGrammar.isImmutable())) {
/* 508 */       this.fGrammarBucket = grammarBucket;
/* 509 */       this.fEntityManager.setScannerVersion(getScannerVersion());
/* 510 */       reset();
/*     */       try
/*     */       {
/* 513 */         if (internalSubset != null)
/*     */         {
/* 517 */           StringBuffer buffer = new StringBuffer(internalSubset.length() + 2);
/* 518 */           buffer.append(internalSubset).append("]>");
/* 519 */           XMLInputSource is = new XMLInputSource(null, baseSystemId, null, new StringReader(buffer.toString()), null);
/*     */ 
/* 521 */           this.fEntityManager.startDocumentEntity(is);
/* 522 */           this.fDTDScanner.scanDTDInternalSubset(true, false, systemId != null);
/*     */         }
/*     */ 
/* 525 */         if (systemId != null) {
/* 526 */           XMLDTDDescription desc = new XMLDTDDescription(publicId, systemId, baseSystemId, null, rootName);
/* 527 */           XMLInputSource source = this.fEntityManager.resolveEntity(desc);
/* 528 */           this.fDTDScanner.setInputSource(source);
/* 529 */           this.fDTDScanner.scanDTDExternalSubset(true);
/*     */         }
/*     */       }
/*     */       catch (EOFException e)
/*     */       {
/*     */       }
/*     */       finally
/*     */       {
/* 537 */         this.fEntityManager.closeReaders();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void reset()
/*     */   {
/* 544 */     super.reset();
/* 545 */     this.fDTDScanner.reset();
/* 546 */     this.fEntityManager.reset();
/* 547 */     this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
/*     */   }
/*     */ 
/*     */   protected XMLDTDScannerImpl createDTDScanner(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityManager)
/*     */   {
/* 552 */     return new XMLDTDScannerImpl(symbolTable, errorReporter, entityManager);
/*     */   }
/*     */ 
/*     */   protected short getScannerVersion() {
/* 556 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDLoader
 * JD-Core Version:    0.6.2
 */