/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XML11DTDScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XML11DTDProcessor;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
/*     */ import com.sun.org.apache.xerces.internal.xinclude.XIncludeNamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class XIncludeParserConfiguration extends XML11Configuration
/*     */ {
/*     */   private XIncludeHandler fXIncludeHandler;
/*     */   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
/*     */   protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
/*     */   protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
/*     */   protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
/*     */   protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
/*     */ 
/*     */   public XIncludeParserConfiguration()
/*     */   {
/*  68 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   public XIncludeParserConfiguration(SymbolTable symbolTable)
/*     */   {
/*  77 */     this(symbolTable, null, null);
/*     */   }
/*     */ 
/*     */   public XIncludeParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/*  91 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public XIncludeParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 107 */     super(symbolTable, grammarPool, parentSettings);
/*     */ 
/* 109 */     this.fXIncludeHandler = new XIncludeHandler();
/* 110 */     addCommonComponent(this.fXIncludeHandler);
/*     */ 
/* 112 */     String[] recognizedFeatures = { "http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language" };
/*     */ 
/* 117 */     addRecognizedFeatures(recognizedFeatures);
/*     */ 
/* 120 */     String[] recognizedProperties = { "http://apache.org/xml/properties/internal/xinclude-handler", "http://apache.org/xml/properties/internal/namespace-context" };
/*     */ 
/* 122 */     addRecognizedProperties(recognizedProperties);
/*     */ 
/* 124 */     setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", true);
/* 125 */     setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
/* 126 */     setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
/*     */ 
/* 128 */     setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
/* 129 */     setProperty("http://apache.org/xml/properties/internal/namespace-context", new XIncludeNamespaceSupport());
/*     */   }
/*     */ 
/*     */   protected void configurePipeline()
/*     */   {
/* 135 */     super.configurePipeline();
/*     */ 
/* 138 */     this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
/* 139 */     this.fDTDProcessor.setDTDSource(this.fDTDScanner);
/* 140 */     this.fDTDProcessor.setDTDHandler(this.fXIncludeHandler);
/* 141 */     this.fXIncludeHandler.setDTDSource(this.fDTDProcessor);
/* 142 */     this.fXIncludeHandler.setDTDHandler(this.fDTDHandler);
/* 143 */     if (this.fDTDHandler != null) {
/* 144 */       this.fDTDHandler.setDTDSource(this.fXIncludeHandler);
/*     */     }
/*     */ 
/* 149 */     XMLDocumentSource prev = null;
/* 150 */     if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*     */     {
/* 153 */       prev = this.fSchemaValidator.getDocumentSource();
/*     */     }
/*     */     else
/*     */     {
/* 157 */       prev = this.fLastComponent;
/* 158 */       this.fLastComponent = this.fXIncludeHandler;
/*     */     }
/*     */ 
/* 161 */     if (prev != null) {
/* 162 */       XMLDocumentHandler next = prev.getDocumentHandler();
/* 163 */       prev.setDocumentHandler(this.fXIncludeHandler);
/* 164 */       this.fXIncludeHandler.setDocumentSource(prev);
/* 165 */       if (next != null) {
/* 166 */         this.fXIncludeHandler.setDocumentHandler(next);
/* 167 */         next.setDocumentSource(this.fXIncludeHandler);
/*     */       }
/*     */     }
/*     */     else {
/* 171 */       setDocumentHandler(this.fXIncludeHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void configureXML11Pipeline()
/*     */   {
/* 177 */     super.configureXML11Pipeline();
/*     */ 
/* 180 */     this.fXML11DTDScanner.setDTDHandler(this.fXML11DTDProcessor);
/* 181 */     this.fXML11DTDProcessor.setDTDSource(this.fXML11DTDScanner);
/* 182 */     this.fXML11DTDProcessor.setDTDHandler(this.fXIncludeHandler);
/* 183 */     this.fXIncludeHandler.setDTDSource(this.fXML11DTDProcessor);
/* 184 */     this.fXIncludeHandler.setDTDHandler(this.fDTDHandler);
/* 185 */     if (this.fDTDHandler != null) {
/* 186 */       this.fDTDHandler.setDTDSource(this.fXIncludeHandler);
/*     */     }
/*     */ 
/* 191 */     XMLDocumentSource prev = null;
/* 192 */     if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*     */     {
/* 195 */       prev = this.fSchemaValidator.getDocumentSource();
/*     */     }
/*     */     else
/*     */     {
/* 199 */       prev = this.fLastComponent;
/* 200 */       this.fLastComponent = this.fXIncludeHandler;
/*     */     }
/*     */ 
/* 203 */     XMLDocumentHandler next = prev.getDocumentHandler();
/* 204 */     prev.setDocumentHandler(this.fXIncludeHandler);
/* 205 */     this.fXIncludeHandler.setDocumentSource(prev);
/* 206 */     if (next != null) {
/* 207 */       this.fXIncludeHandler.setDocumentHandler(next);
/* 208 */       next.setDocumentSource(this.fXIncludeHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 216 */     if (propertyId.equals("http://apache.org/xml/properties/internal/xinclude-handler"));
/* 219 */     super.setProperty(propertyId, value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XIncludeParserConfiguration
 * JD-Core Version:    0.6.2
 */