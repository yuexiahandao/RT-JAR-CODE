/*     */ package com.sun.org.apache.xerces.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer;
/*     */ import com.sun.org.apache.xerces.internal.util.FeatureState;
/*     */ import com.sun.org.apache.xerces.internal.util.PropertyState;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ 
/*     */ final class SchemaValidatorConfiguration
/*     */   implements XMLComponentManager
/*     */ {
/*     */   private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
/*     */   private static final String VALIDATION = "http://xml.org/sax/features/validation";
/*     */   private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
/*     */   private static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*     */   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*     */   private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*     */   private final XMLComponentManager fParentComponentManager;
/*     */   private final XMLGrammarPool fGrammarPool;
/*     */   private final boolean fUseGrammarPoolOnly;
/*     */   private final ValidationManager fValidationManager;
/*     */ 
/*     */   public SchemaValidatorConfiguration(XMLComponentManager parentManager, XSGrammarPoolContainer grammarContainer, ValidationManager validationManager)
/*     */   {
/*  94 */     this.fParentComponentManager = parentManager;
/*  95 */     this.fGrammarPool = grammarContainer.getGrammarPool();
/*  96 */     this.fUseGrammarPoolOnly = grammarContainer.isFullyComposed();
/*  97 */     this.fValidationManager = validationManager;
/*     */     try
/*     */     {
/* 100 */       XMLErrorReporter errorReporter = (XMLErrorReporter)this.fParentComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
/* 101 */       if (errorReporter != null)
/* 102 */         errorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
/*     */     }
/*     */     catch (XMLConfigurationException exc)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 123 */     FeatureState state = getFeatureState(featureId);
/* 124 */     if (state.isExceptional()) {
/* 125 */       throw new XMLConfigurationException(state.status, featureId);
/*     */     }
/* 127 */     return state.state;
/*     */   }
/*     */ 
/*     */   public FeatureState getFeatureState(String featureId) {
/* 131 */     if ("http://apache.org/xml/features/internal/parser-settings".equals(featureId)) {
/* 132 */       return this.fParentComponentManager.getFeatureState(featureId);
/*     */     }
/* 134 */     if (("http://xml.org/sax/features/validation".equals(featureId)) || ("http://apache.org/xml/features/validation/schema".equals(featureId))) {
/* 135 */       return FeatureState.is(true);
/*     */     }
/* 137 */     if ("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(featureId)) {
/* 138 */       return FeatureState.is(this.fUseGrammarPoolOnly);
/*     */     }
/* 140 */     return this.fParentComponentManager.getFeatureState(featureId);
/*     */   }
/*     */ 
/*     */   public PropertyState getPropertyState(String propertyId) {
/* 144 */     if ("http://apache.org/xml/properties/internal/grammar-pool".equals(propertyId)) {
/* 145 */       return PropertyState.is(this.fGrammarPool);
/*     */     }
/* 147 */     if ("http://apache.org/xml/properties/internal/validation-manager".equals(propertyId)) {
/* 148 */       return PropertyState.is(this.fValidationManager);
/*     */     }
/* 150 */     return this.fParentComponentManager.getPropertyState(propertyId);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 167 */     PropertyState state = getPropertyState(propertyId);
/* 168 */     if (state.isExceptional()) {
/* 169 */       throw new XMLConfigurationException(state.status, propertyId);
/*     */     }
/* 171 */     return state.state;
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String featureId, boolean defaultValue) {
/* 175 */     FeatureState state = getFeatureState(featureId);
/* 176 */     if (state.isExceptional()) {
/* 177 */       return defaultValue;
/*     */     }
/* 179 */     return state.state;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String propertyId, Object defaultValue) {
/* 183 */     PropertyState state = getPropertyState(propertyId);
/* 184 */     if (state.isExceptional()) {
/* 185 */       return defaultValue;
/*     */     }
/* 187 */     return state.state;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.SchemaValidatorConfiguration
 * JD-Core Version:    0.6.2
 */