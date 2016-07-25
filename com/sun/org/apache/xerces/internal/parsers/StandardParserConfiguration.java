/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLNamespaceBinder;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.FeatureState;
/*     */ import com.sun.org.apache.xerces.internal.util.PropertyState;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class StandardParserConfiguration extends DTDConfiguration
/*     */ {
/*     */   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
/*     */   protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
/*     */   protected static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
/*     */   protected static final String XMLSCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
/*     */   protected static final String XMLSCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
/*     */   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
/*     */   protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
/*     */   protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
/*     */   protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
/*     */   protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*     */   protected static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
/*     */   protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
/*     */   protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
/*     */   protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
/*     */   protected XMLSchemaValidator fSchemaValidator;
/*     */ 
/*     */   public StandardParserConfiguration()
/*     */   {
/* 154 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   public StandardParserConfiguration(SymbolTable symbolTable)
/*     */   {
/* 163 */     this(symbolTable, null, null);
/*     */   }
/*     */ 
/*     */   public StandardParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 179 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public StandardParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 197 */     super(symbolTable, grammarPool, parentSettings);
/*     */ 
/* 200 */     String[] recognizedFeatures = { "http://apache.org/xml/features/validation/schema/normalized-value", "http://apache.org/xml/features/validation/schema/element-default", "http://apache.org/xml/features/validation/schema/augment-psvi", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/features/validate-annotations", "http://apache.org/xml/features/honour-all-schemaLocations", "http://apache.org/xml/features/namespace-growth", "http://apache.org/xml/features/internal/tolerate-duplicates", "http://apache.org/xml/features/validation/schema", "http://apache.org/xml/features/validation/schema-full-checking" };
/*     */ 
/* 216 */     addRecognizedFeatures(recognizedFeatures);
/*     */ 
/* 219 */     setFeature("http://apache.org/xml/features/validation/schema/element-default", true);
/* 220 */     setFeature("http://apache.org/xml/features/validation/schema/normalized-value", true);
/* 221 */     setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
/* 222 */     setFeature("http://apache.org/xml/features/generate-synthetic-annotations", false);
/* 223 */     setFeature("http://apache.org/xml/features/validate-annotations", false);
/* 224 */     setFeature("http://apache.org/xml/features/honour-all-schemaLocations", false);
/* 225 */     setFeature("http://apache.org/xml/features/namespace-growth", false);
/* 226 */     setFeature("http://apache.org/xml/features/internal/tolerate-duplicates", false);
/*     */ 
/* 230 */     String[] recognizedProperties = { "http://apache.org/xml/properties/schema/external-schemaLocation", "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "http://apache.org/xml/properties/internal/validation/schema/dv-factory" };
/*     */ 
/* 240 */     addRecognizedProperties(recognizedProperties);
/*     */   }
/*     */ 
/*     */   protected void configurePipeline()
/*     */   {
/* 250 */     super.configurePipeline();
/* 251 */     if (getFeature("http://apache.org/xml/features/validation/schema"))
/*     */     {
/* 253 */       if (this.fSchemaValidator == null) {
/* 254 */         this.fSchemaValidator = new XMLSchemaValidator();
/*     */ 
/* 257 */         this.fProperties.put("http://apache.org/xml/properties/internal/validator/schema", this.fSchemaValidator);
/* 258 */         addComponent(this.fSchemaValidator);
/*     */ 
/* 260 */         if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/* 261 */           XSMessageFormatter xmft = new XSMessageFormatter();
/* 262 */           this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", xmft);
/*     */         }
/*     */       }
/*     */ 
/* 266 */       this.fLastComponent = this.fSchemaValidator;
/* 267 */       this.fNamespaceBinder.setDocumentHandler(this.fSchemaValidator);
/*     */ 
/* 269 */       this.fSchemaValidator.setDocumentHandler(this.fDocumentHandler);
/* 270 */       this.fSchemaValidator.setDocumentSource(this.fNamespaceBinder);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected FeatureState checkFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 297 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 298 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*     */ 
/* 304 */       if ((suffixLength == "validation/schema".length()) && (featureId.endsWith("validation/schema")))
/*     */       {
/* 306 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 309 */       if ((suffixLength == "validation/schema-full-checking".length()) && (featureId.endsWith("validation/schema-full-checking")))
/*     */       {
/* 311 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 315 */       if ((suffixLength == "validation/schema/normalized-value".length()) && (featureId.endsWith("validation/schema/normalized-value")))
/*     */       {
/* 317 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 321 */       if ((suffixLength == "validation/schema/element-default".length()) && (featureId.endsWith("validation/schema/element-default")))
/*     */       {
/* 323 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 331 */     return super.checkFeature(featureId);
/*     */   }
/*     */ 
/*     */   protected PropertyState checkProperty(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 355 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 356 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*     */ 
/* 358 */       if ((suffixLength == "schema/external-schemaLocation".length()) && (propertyId.endsWith("schema/external-schemaLocation")))
/*     */       {
/* 360 */         return PropertyState.RECOGNIZED;
/*     */       }
/* 362 */       if ((suffixLength == "schema/external-noNamespaceSchemaLocation".length()) && (propertyId.endsWith("schema/external-noNamespaceSchemaLocation")))
/*     */       {
/* 364 */         return PropertyState.RECOGNIZED;
/*     */       }
/*     */     }
/*     */ 
/* 368 */     if (propertyId.startsWith("http://java.sun.com/xml/jaxp/properties/")) {
/* 369 */       int suffixLength = propertyId.length() - "http://java.sun.com/xml/jaxp/properties/".length();
/*     */ 
/* 371 */       if ((suffixLength == "schemaSource".length()) && (propertyId.endsWith("schemaSource")))
/*     */       {
/* 373 */         return PropertyState.RECOGNIZED;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 381 */     return super.checkProperty(propertyId);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.StandardParserConfiguration
 * JD-Core Version:    0.6.2
 */