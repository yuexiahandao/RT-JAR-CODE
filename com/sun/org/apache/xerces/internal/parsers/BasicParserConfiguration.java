/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.FeatureState;
/*     */ import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
/*     */ import com.sun.org.apache.xerces.internal.util.PropertyState;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class BasicParserConfiguration extends ParserConfigurationSettings
/*     */   implements XMLParserConfiguration
/*     */ {
/*     */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*     */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*     */   protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
/*     */   protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
/*     */   protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
/*     */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*     */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*     */   protected SymbolTable fSymbolTable;
/*     */   protected Locale fLocale;
/*     */   protected ArrayList fComponents;
/*     */   protected XMLDocumentHandler fDocumentHandler;
/*     */   protected XMLDTDHandler fDTDHandler;
/*     */   protected XMLDTDContentModelHandler fDTDContentModelHandler;
/*     */   protected XMLDocumentSource fLastComponent;
/*     */ 
/*     */   protected BasicParserConfiguration()
/*     */   {
/* 181 */     this(null, null);
/*     */   }
/*     */ 
/*     */   protected BasicParserConfiguration(SymbolTable symbolTable)
/*     */   {
/* 190 */     this(symbolTable, null);
/*     */   }
/*     */ 
/*     */   protected BasicParserConfiguration(SymbolTable symbolTable, XMLComponentManager parentSettings)
/*     */   {
/* 202 */     super(parentSettings);
/*     */ 
/* 205 */     this.fComponents = new ArrayList();
/*     */ 
/* 208 */     this.fFeatures = new HashMap();
/* 209 */     this.fProperties = new HashMap();
/*     */ 
/* 212 */     String[] recognizedFeatures = { "http://apache.org/xml/features/internal/parser-settings", "http://xml.org/sax/features/validation", "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities" };
/*     */ 
/* 219 */     addRecognizedFeatures(recognizedFeatures);
/* 220 */     this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
/*     */ 
/* 222 */     this.fFeatures.put("http://xml.org/sax/features/validation", Boolean.FALSE);
/* 223 */     this.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
/* 224 */     this.fFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.TRUE);
/* 225 */     this.fFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.TRUE);
/*     */ 
/* 228 */     String[] recognizedProperties = { "http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver" };
/*     */ 
/* 234 */     addRecognizedProperties(recognizedProperties);
/*     */ 
/* 236 */     if (symbolTable == null) {
/* 237 */       symbolTable = new SymbolTable();
/*     */     }
/* 239 */     this.fSymbolTable = symbolTable;
/* 240 */     this.fProperties.put("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/*     */   }
/*     */ 
/*     */   protected void addComponent(XMLComponent component)
/*     */   {
/* 254 */     if (this.fComponents.contains(component)) {
/* 255 */       return;
/*     */     }
/* 257 */     this.fComponents.add(component);
/*     */ 
/* 260 */     String[] recognizedFeatures = component.getRecognizedFeatures();
/* 261 */     addRecognizedFeatures(recognizedFeatures);
/*     */ 
/* 264 */     String[] recognizedProperties = component.getRecognizedProperties();
/* 265 */     addRecognizedProperties(recognizedProperties);
/*     */ 
/* 268 */     if (recognizedFeatures != null) {
/* 269 */       for (int i = 0; i < recognizedFeatures.length; i++) {
/* 270 */         String featureId = recognizedFeatures[i];
/* 271 */         Boolean state = component.getFeatureDefault(featureId);
/* 272 */         if (state != null) {
/* 273 */           super.setFeature(featureId, state.booleanValue());
/*     */         }
/*     */       }
/*     */     }
/* 277 */     if (recognizedProperties != null)
/* 278 */       for (int i = 0; i < recognizedProperties.length; i++) {
/* 279 */         String propertyId = recognizedProperties[i];
/* 280 */         Object value = component.getPropertyDefault(propertyId);
/* 281 */         if (value != null)
/* 282 */           super.setProperty(propertyId, value);
/*     */       }
/*     */   }
/*     */ 
/*     */   public abstract void parse(XMLInputSource paramXMLInputSource)
/*     */     throws XNIException, IOException;
/*     */ 
/*     */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*     */   {
/* 327 */     this.fDocumentHandler = documentHandler;
/* 328 */     if (this.fLastComponent != null) {
/* 329 */       this.fLastComponent.setDocumentHandler(this.fDocumentHandler);
/* 330 */       if (this.fDocumentHandler != null)
/* 331 */         this.fDocumentHandler.setDocumentSource(this.fLastComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLDocumentHandler getDocumentHandler()
/*     */   {
/* 338 */     return this.fDocumentHandler;
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(XMLDTDHandler dtdHandler)
/*     */   {
/* 347 */     this.fDTDHandler = dtdHandler;
/*     */   }
/*     */ 
/*     */   public XMLDTDHandler getDTDHandler()
/*     */   {
/* 352 */     return this.fDTDHandler;
/*     */   }
/*     */ 
/*     */   public void setDTDContentModelHandler(XMLDTDContentModelHandler handler)
/*     */   {
/* 361 */     this.fDTDContentModelHandler = handler;
/*     */   }
/*     */ 
/*     */   public XMLDTDContentModelHandler getDTDContentModelHandler()
/*     */   {
/* 366 */     return this.fDTDContentModelHandler;
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(XMLEntityResolver resolver)
/*     */   {
/* 378 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", resolver);
/*     */   }
/*     */ 
/*     */   public XMLEntityResolver getEntityResolver()
/*     */   {
/* 390 */     return (XMLEntityResolver)this.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(XMLErrorHandler errorHandler)
/*     */   {
/* 413 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-handler", errorHandler);
/*     */   }
/*     */ 
/*     */   public XMLErrorHandler getErrorHandler()
/*     */   {
/* 425 */     return (XMLErrorHandler)this.fProperties.get("http://apache.org/xml/properties/internal/error-handler");
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */     throws XMLConfigurationException
/*     */   {
/* 445 */     int count = this.fComponents.size();
/* 446 */     for (int i = 0; i < count; i++) {
/* 447 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/* 448 */       c.setFeature(featureId, state);
/*     */     }
/*     */ 
/* 451 */     super.setFeature(featureId, state);
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 465 */     int count = this.fComponents.size();
/* 466 */     for (int i = 0; i < count; i++) {
/* 467 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/* 468 */       c.setProperty(propertyId, value);
/*     */     }
/*     */ 
/* 472 */     super.setProperty(propertyId, value);
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */     throws XNIException
/*     */   {
/* 485 */     this.fLocale = locale;
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 490 */     return this.fLocale;
/*     */   }
/*     */ 
/*     */   protected void reset()
/*     */     throws XNIException
/*     */   {
/* 503 */     int count = this.fComponents.size();
/* 504 */     for (int i = 0; i < count; i++) {
/* 505 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/* 506 */       c.reset(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected PropertyState checkProperty(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 524 */     if (propertyId.startsWith("http://xml.org/sax/properties/")) {
/* 525 */       int suffixLength = propertyId.length() - "http://xml.org/sax/properties/".length();
/*     */ 
/* 537 */       if ((suffixLength == "xml-string".length()) && (propertyId.endsWith("xml-string")))
/*     */       {
/* 542 */         return PropertyState.NOT_SUPPORTED;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 547 */     return super.checkProperty(propertyId);
/*     */   }
/*     */ 
/*     */   protected FeatureState checkFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 570 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 571 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*     */ 
/* 576 */       if ((suffixLength == "internal/parser-settings".length()) && (featureId.endsWith("internal/parser-settings")))
/*     */       {
/* 578 */         return FeatureState.NOT_SUPPORTED;
/*     */       }
/*     */     }
/*     */ 
/* 582 */     return super.checkFeature(featureId);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration
 * JD-Core Version:    0.6.2
 */