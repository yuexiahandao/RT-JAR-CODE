/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.FeatureState;
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
/*     */ import com.sun.org.apache.xerces.internal.util.PropertyState;
/*     */ import com.sun.org.apache.xerces.internal.util.Status;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.State;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.Property;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.State;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ 
/*     */ final class XMLSchemaValidatorComponentManager extends ParserConfigurationSettings
/*     */   implements XMLComponentManager
/*     */ {
/*     */   private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
/*     */   private static final String VALIDATION = "http://xml.org/sax/features/validation";
/*     */   private static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
/*     */   private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
/*     */   private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*     */   private static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*     */   private static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*     */   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
/*     */   private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*     */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*     */   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*     */   private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*     */   private static final String LOCALE = "http://apache.org/xml/properties/locale";
/* 137 */   private boolean _isSecureMode = false;
/*     */ 
/* 143 */   private boolean fConfigUpdated = true;
/*     */   private boolean fUseGrammarPoolOnly;
/* 152 */   private final HashMap fComponents = new HashMap();
/*     */   private XMLEntityManager fEntityManager;
/*     */   private XMLErrorReporter fErrorReporter;
/*     */   private NamespaceContext fNamespaceContext;
/*     */   private XMLSchemaValidator fSchemaValidator;
/*     */   private ValidationManager fValidationManager;
/* 178 */   private final HashMap fInitFeatures = new HashMap();
/*     */ 
/* 181 */   private final HashMap fInitProperties = new HashMap();
/*     */   private XMLSecurityManager fInitSecurityManager;
/*     */   private final XMLSecurityPropertyManager fSecurityPropertyMgr;
/* 194 */   private ErrorHandler fErrorHandler = null;
/*     */ 
/* 197 */   private LSResourceResolver fResourceResolver = null;
/*     */ 
/* 200 */   private Locale fLocale = null;
/*     */ 
/*     */   public XMLSchemaValidatorComponentManager(XSGrammarPoolContainer grammarContainer)
/*     */   {
/* 205 */     this.fEntityManager = new XMLEntityManager();
/* 206 */     this.fComponents.put("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/*     */ 
/* 208 */     this.fErrorReporter = new XMLErrorReporter();
/* 209 */     this.fComponents.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*     */ 
/* 211 */     this.fNamespaceContext = new NamespaceSupport();
/* 212 */     this.fComponents.put("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
/*     */ 
/* 214 */     this.fSchemaValidator = new XMLSchemaValidator();
/* 215 */     this.fComponents.put("http://apache.org/xml/properties/internal/validator/schema", this.fSchemaValidator);
/*     */ 
/* 217 */     this.fValidationManager = new ValidationManager();
/* 218 */     this.fComponents.put("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
/*     */ 
/* 221 */     this.fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", null);
/* 222 */     this.fComponents.put("http://apache.org/xml/properties/internal/error-handler", null);
/*     */ 
/* 224 */     this.fComponents.put("http://apache.org/xml/properties/internal/symbol-table", new SymbolTable());
/*     */ 
/* 227 */     this.fComponents.put("http://apache.org/xml/properties/internal/grammar-pool", grammarContainer.getGrammarPool());
/* 228 */     this.fUseGrammarPoolOnly = grammarContainer.isFullyComposed();
/*     */ 
/* 231 */     this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
/*     */ 
/* 234 */     addRecognizedParamsAndSetDefaults(this.fEntityManager, grammarContainer);
/* 235 */     addRecognizedParamsAndSetDefaults(this.fErrorReporter, grammarContainer);
/* 236 */     addRecognizedParamsAndSetDefaults(this.fSchemaValidator, grammarContainer);
/*     */ 
/* 238 */     boolean secureProcessing = grammarContainer.getFeature("http://javax.xml.XMLConstants/feature/secure-processing").booleanValue();
/* 239 */     if (System.getSecurityManager() != null) {
/* 240 */       this._isSecureMode = true;
/* 241 */       secureProcessing = true;
/*     */     }
/*     */ 
/* 244 */     this.fInitSecurityManager = ((XMLSecurityManager)grammarContainer.getProperty("http://apache.org/xml/properties/security-manager"));
/*     */ 
/* 246 */     if (this.fInitSecurityManager != null)
/* 247 */       this.fInitSecurityManager.setSecureProcessing(secureProcessing);
/*     */     else {
/* 249 */       this.fInitSecurityManager = new XMLSecurityManager(secureProcessing);
/*     */     }
/*     */ 
/* 252 */     setProperty("http://apache.org/xml/properties/security-manager", this.fInitSecurityManager);
/*     */ 
/* 255 */     this.fSecurityPropertyMgr = ((XMLSecurityPropertyManager)grammarContainer.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"));
/*     */ 
/* 257 */     setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/*     */   }
/*     */ 
/*     */   public FeatureState getFeatureState(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 274 */     if ("http://apache.org/xml/features/internal/parser-settings".equals(featureId)) {
/* 275 */       return FeatureState.is(this.fConfigUpdated);
/*     */     }
/* 277 */     if (("http://xml.org/sax/features/validation".equals(featureId)) || ("http://apache.org/xml/features/validation/schema".equals(featureId))) {
/* 278 */       return FeatureState.is(true);
/*     */     }
/* 280 */     if ("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(featureId)) {
/* 281 */       return FeatureState.is(this.fUseGrammarPoolOnly);
/*     */     }
/* 283 */     if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(featureId)) {
/* 284 */       return FeatureState.is(this.fInitSecurityManager.isSecureProcessing());
/*     */     }
/* 286 */     if ("http://apache.org/xml/features/validation/schema/element-default".equals(featureId)) {
/* 287 */       return FeatureState.is(true);
/*     */     }
/* 289 */     return super.getFeatureState(featureId);
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 301 */     if ("http://apache.org/xml/features/internal/parser-settings".equals(featureId)) {
/* 302 */       throw new XMLConfigurationException(Status.NOT_SUPPORTED, featureId);
/*     */     }
/* 304 */     if ((!value) && (("http://xml.org/sax/features/validation".equals(featureId)) || ("http://apache.org/xml/features/validation/schema".equals(featureId)))) {
/* 305 */       throw new XMLConfigurationException(Status.NOT_SUPPORTED, featureId);
/*     */     }
/* 307 */     if (("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(featureId)) && (value != this.fUseGrammarPoolOnly)) {
/* 308 */       throw new XMLConfigurationException(Status.NOT_SUPPORTED, featureId);
/*     */     }
/* 310 */     if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(featureId)) {
/* 311 */       if ((this._isSecureMode) && (!value)) {
/* 312 */         throw new XMLConfigurationException(Status.NOT_ALLOWED, "http://javax.xml.XMLConstants/feature/secure-processing");
/*     */       }
/* 314 */       this.fInitSecurityManager.setSecureProcessing(value);
/* 315 */       setProperty("http://apache.org/xml/properties/security-manager", this.fInitSecurityManager);
/*     */ 
/* 317 */       if ((value) && (Constants.IS_JDK8_OR_ABOVE)) {
/* 318 */         this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, XMLSecurityPropertyManager.State.FSP, "");
/*     */ 
/* 320 */         this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA, XMLSecurityPropertyManager.State.FSP, "");
/*     */ 
/* 322 */         setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/*     */       }
/*     */ 
/* 325 */       return;
/*     */     }
/* 327 */     this.fConfigUpdated = true;
/* 328 */     this.fEntityManager.setFeature(featureId, value);
/* 329 */     this.fErrorReporter.setFeature(featureId, value);
/* 330 */     this.fSchemaValidator.setFeature(featureId, value);
/* 331 */     if (!this.fInitFeatures.containsKey(featureId)) {
/* 332 */       boolean current = super.getFeature(featureId);
/* 333 */       this.fInitFeatures.put(featureId, current ? Boolean.TRUE : Boolean.FALSE);
/*     */     }
/* 335 */     super.setFeature(featureId, value);
/*     */   }
/*     */ 
/*     */   public PropertyState getPropertyState(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 352 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/* 353 */       return PropertyState.is(getLocale());
/*     */     }
/* 355 */     Object component = this.fComponents.get(propertyId);
/* 356 */     if (component != null) {
/* 357 */       return PropertyState.is(component);
/*     */     }
/* 359 */     if (this.fComponents.containsKey(propertyId)) {
/* 360 */       return PropertyState.is(null);
/*     */     }
/* 362 */     return super.getPropertyState(propertyId);
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 374 */     if (("http://apache.org/xml/properties/internal/entity-manager".equals(propertyId)) || ("http://apache.org/xml/properties/internal/error-reporter".equals(propertyId)) || ("http://apache.org/xml/properties/internal/namespace-context".equals(propertyId)) || ("http://apache.org/xml/properties/internal/validator/schema".equals(propertyId)) || ("http://apache.org/xml/properties/internal/symbol-table".equals(propertyId)) || ("http://apache.org/xml/properties/internal/validation-manager".equals(propertyId)) || ("http://apache.org/xml/properties/internal/grammar-pool".equals(propertyId)))
/*     */     {
/* 378 */       throw new XMLConfigurationException(Status.NOT_SUPPORTED, propertyId);
/*     */     }
/* 380 */     this.fConfigUpdated = true;
/* 381 */     this.fEntityManager.setProperty(propertyId, value);
/* 382 */     this.fErrorReporter.setProperty(propertyId, value);
/* 383 */     this.fSchemaValidator.setProperty(propertyId, value);
/* 384 */     if (("http://apache.org/xml/properties/internal/entity-resolver".equals(propertyId)) || ("http://apache.org/xml/properties/internal/error-handler".equals(propertyId)) || ("http://apache.org/xml/properties/security-manager".equals(propertyId)))
/*     */     {
/* 386 */       this.fComponents.put(propertyId, value);
/* 387 */       return;
/*     */     }
/* 389 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/* 390 */       setLocale((Locale)value);
/* 391 */       this.fComponents.put(propertyId, value);
/* 392 */       return;
/*     */     }
/*     */ 
/* 396 */     if ((this.fInitSecurityManager == null) || (!this.fInitSecurityManager.setLimit(propertyId, XMLSecurityManager.State.APIPROPERTY, value)))
/*     */     {
/* 399 */       if ((this.fSecurityPropertyMgr == null) || (!this.fSecurityPropertyMgr.setValue(propertyId, XMLSecurityPropertyManager.State.APIPROPERTY, value)))
/*     */       {
/* 402 */         if (!this.fInitProperties.containsKey(propertyId)) {
/* 403 */           this.fInitProperties.put(propertyId, super.getProperty(propertyId));
/*     */         }
/* 405 */         super.setProperty(propertyId, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addRecognizedParamsAndSetDefaults(XMLComponent component, XSGrammarPoolContainer grammarContainer)
/*     */   {
/* 422 */     String[] recognizedFeatures = component.getRecognizedFeatures();
/* 423 */     addRecognizedFeatures(recognizedFeatures);
/*     */ 
/* 426 */     String[] recognizedProperties = component.getRecognizedProperties();
/* 427 */     addRecognizedProperties(recognizedProperties);
/*     */ 
/* 430 */     setFeatureDefaults(component, recognizedFeatures, grammarContainer);
/* 431 */     setPropertyDefaults(component, recognizedProperties);
/*     */   }
/*     */ 
/*     */   public void reset() throws XNIException
/*     */   {
/* 436 */     this.fNamespaceContext.reset();
/* 437 */     this.fValidationManager.reset();
/* 438 */     this.fEntityManager.reset(this);
/* 439 */     this.fErrorReporter.reset(this);
/* 440 */     this.fSchemaValidator.reset(this);
/*     */ 
/* 442 */     this.fConfigUpdated = false;
/*     */   }
/*     */ 
/*     */   void setErrorHandler(ErrorHandler errorHandler) {
/* 446 */     this.fErrorHandler = errorHandler;
/* 447 */     setProperty("http://apache.org/xml/properties/internal/error-handler", errorHandler != null ? new ErrorHandlerWrapper(errorHandler) : new ErrorHandlerWrapper(DraconianErrorHandler.getInstance()));
/*     */   }
/*     */ 
/*     */   ErrorHandler getErrorHandler()
/*     */   {
/* 452 */     return this.fErrorHandler;
/*     */   }
/*     */ 
/*     */   void setResourceResolver(LSResourceResolver resourceResolver) {
/* 456 */     this.fResourceResolver = resourceResolver;
/* 457 */     setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper(resourceResolver));
/*     */   }
/*     */ 
/*     */   LSResourceResolver getResourceResolver() {
/* 461 */     return this.fResourceResolver;
/*     */   }
/*     */ 
/*     */   void setLocale(Locale locale) {
/* 465 */     this.fLocale = locale;
/* 466 */     this.fErrorReporter.setLocale(locale);
/*     */   }
/*     */ 
/*     */   Locale getLocale() {
/* 470 */     return this.fLocale;
/*     */   }
/*     */ 
/*     */   void restoreInitialState()
/*     */   {
/* 475 */     this.fConfigUpdated = true;
/*     */ 
/* 478 */     this.fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", null);
/* 479 */     this.fComponents.put("http://apache.org/xml/properties/internal/error-handler", null);
/*     */ 
/* 482 */     setLocale(null);
/* 483 */     this.fComponents.put("http://apache.org/xml/properties/locale", null);
/*     */ 
/* 486 */     this.fComponents.put("http://apache.org/xml/properties/security-manager", this.fInitSecurityManager);
/*     */ 
/* 489 */     setLocale(null);
/* 490 */     this.fComponents.put("http://apache.org/xml/properties/locale", null);
/*     */ 
/* 493 */     if (!this.fInitFeatures.isEmpty()) {
/* 494 */       Iterator iter = this.fInitFeatures.entrySet().iterator();
/* 495 */       while (iter.hasNext()) {
/* 496 */         Map.Entry entry = (Map.Entry)iter.next();
/* 497 */         String name = (String)entry.getKey();
/* 498 */         boolean value = ((Boolean)entry.getValue()).booleanValue();
/* 499 */         super.setFeature(name, value);
/*     */       }
/* 501 */       this.fInitFeatures.clear();
/*     */     }
/* 503 */     if (!this.fInitProperties.isEmpty()) {
/* 504 */       Iterator iter = this.fInitProperties.entrySet().iterator();
/* 505 */       while (iter.hasNext()) {
/* 506 */         Map.Entry entry = (Map.Entry)iter.next();
/* 507 */         String name = (String)entry.getKey();
/* 508 */         Object value = entry.getValue();
/* 509 */         super.setProperty(name, value);
/*     */       }
/* 511 */       this.fInitProperties.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setFeatureDefaults(XMLComponent component, String[] recognizedFeatures, XSGrammarPoolContainer grammarContainer)
/*     */   {
/* 518 */     if (recognizedFeatures != null)
/* 519 */       for (int i = 0; i < recognizedFeatures.length; i++) {
/* 520 */         String featureId = recognizedFeatures[i];
/* 521 */         Boolean state = grammarContainer.getFeature(featureId);
/* 522 */         if (state == null) {
/* 523 */           state = component.getFeatureDefault(featureId);
/*     */         }
/* 525 */         if (state != null)
/*     */         {
/* 527 */           if (!this.fFeatures.containsKey(featureId)) {
/* 528 */             this.fFeatures.put(featureId, state);
/*     */ 
/* 533 */             this.fConfigUpdated = true;
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private void setPropertyDefaults(XMLComponent component, String[] recognizedProperties)
/*     */   {
/* 542 */     if (recognizedProperties != null)
/* 543 */       for (int i = 0; i < recognizedProperties.length; i++) {
/* 544 */         String propertyId = recognizedProperties[i];
/* 545 */         Object value = component.getPropertyDefault(propertyId);
/* 546 */         if (value != null)
/*     */         {
/* 548 */           if (!this.fProperties.containsKey(propertyId)) {
/* 549 */             this.fProperties.put(propertyId, value);
/*     */ 
/* 554 */             this.fConfigUpdated = true;
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaValidatorComponentManager
 * JD-Core Version:    0.6.2
 */