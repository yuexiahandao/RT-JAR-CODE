/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XML11DTDScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XML11DTDProcessor;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.util.FeatureState;
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
/*     */ import com.sun.org.apache.xerces.internal.xinclude.XIncludeNamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class XIncludeAwareParserConfiguration extends XML11Configuration
/*     */ {
/*     */   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
/*     */   protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
/*     */   protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
/*     */   protected static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
/*     */   protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
/*     */   protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
/*     */   protected XIncludeHandler fXIncludeHandler;
/*     */   protected NamespaceSupport fNonXIncludeNSContext;
/*     */   protected XIncludeNamespaceSupport fXIncludeNSContext;
/*     */   protected NamespaceContext fCurrentNSContext;
/*  87 */   protected boolean fXIncludeEnabled = false;
/*     */ 
/*     */   public XIncludeAwareParserConfiguration()
/*     */   {
/*  91 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   public XIncludeAwareParserConfiguration(SymbolTable symbolTable)
/*     */   {
/* 100 */     this(symbolTable, null, null);
/*     */   }
/*     */ 
/*     */   public XIncludeAwareParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 114 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public XIncludeAwareParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 130 */     super(symbolTable, grammarPool, parentSettings);
/*     */ 
/* 132 */     String[] recognizedFeatures = { "http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language" };
/*     */ 
/* 137 */     addRecognizedFeatures(recognizedFeatures);
/*     */ 
/* 140 */     String[] recognizedProperties = { "http://apache.org/xml/properties/internal/xinclude-handler", "http://apache.org/xml/properties/internal/namespace-context" };
/*     */ 
/* 142 */     addRecognizedProperties(recognizedProperties);
/*     */ 
/* 144 */     setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", true);
/* 145 */     setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
/* 146 */     setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
/*     */ 
/* 148 */     this.fNonXIncludeNSContext = new NamespaceSupport();
/* 149 */     this.fCurrentNSContext = this.fNonXIncludeNSContext;
/* 150 */     setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
/*     */   }
/*     */ 
/*     */   protected void configurePipeline()
/*     */   {
/* 156 */     super.configurePipeline();
/* 157 */     if (this.fXIncludeEnabled)
/*     */     {
/* 159 */       if (this.fXIncludeHandler == null) {
/* 160 */         this.fXIncludeHandler = new XIncludeHandler();
/*     */ 
/* 162 */         setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
/* 163 */         addCommonComponent(this.fXIncludeHandler);
/* 164 */         this.fXIncludeHandler.reset(this);
/*     */       }
/*     */ 
/* 167 */       if (this.fCurrentNSContext != this.fXIncludeNSContext) {
/* 168 */         if (this.fXIncludeNSContext == null) {
/* 169 */           this.fXIncludeNSContext = new XIncludeNamespaceSupport();
/*     */         }
/* 171 */         this.fCurrentNSContext = this.fXIncludeNSContext;
/* 172 */         setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fXIncludeNSContext);
/*     */       }
/*     */ 
/* 175 */       this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
/* 176 */       this.fDTDProcessor.setDTDSource(this.fDTDScanner);
/* 177 */       this.fDTDProcessor.setDTDHandler(this.fXIncludeHandler);
/* 178 */       this.fXIncludeHandler.setDTDSource(this.fDTDProcessor);
/* 179 */       this.fXIncludeHandler.setDTDHandler(this.fDTDHandler);
/* 180 */       if (this.fDTDHandler != null) {
/* 181 */         this.fDTDHandler.setDTDSource(this.fXIncludeHandler);
/*     */       }
/*     */ 
/* 186 */       XMLDocumentSource prev = null;
/* 187 */       if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*     */       {
/* 190 */         prev = this.fSchemaValidator.getDocumentSource();
/*     */       }
/*     */       else
/*     */       {
/* 194 */         prev = this.fLastComponent;
/* 195 */         this.fLastComponent = this.fXIncludeHandler;
/*     */       }
/*     */ 
/* 198 */       XMLDocumentHandler next = prev.getDocumentHandler();
/* 199 */       prev.setDocumentHandler(this.fXIncludeHandler);
/* 200 */       this.fXIncludeHandler.setDocumentSource(prev);
/* 201 */       if (next != null) {
/* 202 */         this.fXIncludeHandler.setDocumentHandler(next);
/* 203 */         next.setDocumentSource(this.fXIncludeHandler);
/*     */       }
/*     */ 
/*     */     }
/* 208 */     else if (this.fCurrentNSContext != this.fNonXIncludeNSContext) {
/* 209 */       this.fCurrentNSContext = this.fNonXIncludeNSContext;
/* 210 */       setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void configureXML11Pipeline()
/*     */   {
/* 216 */     super.configureXML11Pipeline();
/* 217 */     if (this.fXIncludeEnabled)
/*     */     {
/* 219 */       if (this.fXIncludeHandler == null) {
/* 220 */         this.fXIncludeHandler = new XIncludeHandler();
/*     */ 
/* 222 */         setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
/* 223 */         addCommonComponent(this.fXIncludeHandler);
/* 224 */         this.fXIncludeHandler.reset(this);
/*     */       }
/*     */ 
/* 227 */       if (this.fCurrentNSContext != this.fXIncludeNSContext) {
/* 228 */         if (this.fXIncludeNSContext == null) {
/* 229 */           this.fXIncludeNSContext = new XIncludeNamespaceSupport();
/*     */         }
/* 231 */         this.fCurrentNSContext = this.fXIncludeNSContext;
/* 232 */         setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fXIncludeNSContext);
/*     */       }
/*     */ 
/* 235 */       this.fXML11DTDScanner.setDTDHandler(this.fXML11DTDProcessor);
/* 236 */       this.fXML11DTDProcessor.setDTDSource(this.fXML11DTDScanner);
/* 237 */       this.fXML11DTDProcessor.setDTDHandler(this.fXIncludeHandler);
/* 238 */       this.fXIncludeHandler.setDTDSource(this.fXML11DTDProcessor);
/* 239 */       this.fXIncludeHandler.setDTDHandler(this.fDTDHandler);
/* 240 */       if (this.fDTDHandler != null) {
/* 241 */         this.fDTDHandler.setDTDSource(this.fXIncludeHandler);
/*     */       }
/*     */ 
/* 246 */       XMLDocumentSource prev = null;
/* 247 */       if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*     */       {
/* 250 */         prev = this.fSchemaValidator.getDocumentSource();
/*     */       }
/*     */       else
/*     */       {
/* 254 */         prev = this.fLastComponent;
/* 255 */         this.fLastComponent = this.fXIncludeHandler;
/*     */       }
/*     */ 
/* 258 */       XMLDocumentHandler next = prev.getDocumentHandler();
/* 259 */       prev.setDocumentHandler(this.fXIncludeHandler);
/* 260 */       this.fXIncludeHandler.setDocumentSource(prev);
/* 261 */       if (next != null) {
/* 262 */         this.fXIncludeHandler.setDocumentHandler(next);
/* 263 */         next.setDocumentSource(this.fXIncludeHandler);
/*     */       }
/*     */ 
/*     */     }
/* 268 */     else if (this.fCurrentNSContext != this.fNonXIncludeNSContext) {
/* 269 */       this.fCurrentNSContext = this.fNonXIncludeNSContext;
/* 270 */       setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FeatureState getFeatureState(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 277 */     if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
/* 278 */       return FeatureState.is(this.fConfigUpdated);
/*     */     }
/* 280 */     if (featureId.equals("http://apache.org/xml/features/xinclude")) {
/* 281 */       return FeatureState.is(this.fXIncludeEnabled);
/*     */     }
/* 283 */     return super.getFeatureState0(featureId);
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */     throws XMLConfigurationException
/*     */   {
/* 289 */     if (featureId.equals("http://apache.org/xml/features/xinclude")) {
/* 290 */       this.fXIncludeEnabled = state;
/* 291 */       this.fConfigUpdated = true;
/* 292 */       return;
/*     */     }
/* 294 */     super.setFeature(featureId, state);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XIncludeAwareParserConfiguration
 * JD-Core Version:    0.6.2
 */