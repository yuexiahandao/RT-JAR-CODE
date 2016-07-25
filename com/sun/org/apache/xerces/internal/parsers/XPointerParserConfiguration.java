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
/*     */ import com.sun.org.apache.xerces.internal.xpointer.XPointerHandler;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class XPointerParserConfiguration extends XML11Configuration
/*     */ {
/*     */   private XPointerHandler fXPointerHandler;
/*     */   private XIncludeHandler fXIncludeHandler;
/*     */   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
/*     */   protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
/*     */   protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
/*     */   protected static final String XPOINTER_HANDLER = "http://apache.org/xml/properties/internal/xpointer-handler";
/*     */   protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
/*     */   protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
/*     */ 
/*     */   public XPointerParserConfiguration()
/*     */   {
/*  74 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   public XPointerParserConfiguration(SymbolTable symbolTable)
/*     */   {
/*  83 */     this(symbolTable, null, null);
/*     */   }
/*     */ 
/*     */   public XPointerParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/*  97 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public XPointerParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 113 */     super(symbolTable, grammarPool, parentSettings);
/*     */ 
/* 115 */     this.fXIncludeHandler = new XIncludeHandler();
/* 116 */     addCommonComponent(this.fXIncludeHandler);
/*     */ 
/* 118 */     this.fXPointerHandler = new XPointerHandler();
/* 119 */     addCommonComponent(this.fXPointerHandler);
/*     */ 
/* 121 */     String[] recognizedFeatures = { "http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language" };
/*     */ 
/* 126 */     addRecognizedFeatures(recognizedFeatures);
/*     */ 
/* 129 */     String[] recognizedProperties = { "http://apache.org/xml/properties/internal/xinclude-handler", "http://apache.org/xml/properties/internal/xpointer-handler", "http://apache.org/xml/properties/internal/namespace-context" };
/*     */ 
/* 131 */     addRecognizedProperties(recognizedProperties);
/*     */ 
/* 133 */     setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", true);
/* 134 */     setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
/* 135 */     setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
/*     */ 
/* 137 */     setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
/* 138 */     setProperty("http://apache.org/xml/properties/internal/xpointer-handler", this.fXPointerHandler);
/* 139 */     setProperty("http://apache.org/xml/properties/internal/namespace-context", new XIncludeNamespaceSupport());
/*     */   }
/*     */ 
/*     */   protected void configurePipeline()
/*     */   {
/* 147 */     super.configurePipeline();
/*     */ 
/* 150 */     this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
/* 151 */     this.fDTDProcessor.setDTDSource(this.fDTDScanner);
/*     */ 
/* 153 */     this.fDTDProcessor.setDTDHandler(this.fXIncludeHandler);
/* 154 */     this.fXIncludeHandler.setDTDSource(this.fDTDProcessor);
/* 155 */     this.fXIncludeHandler.setDTDHandler(this.fXPointerHandler);
/* 156 */     this.fXPointerHandler.setDTDSource(this.fXIncludeHandler);
/* 157 */     this.fXPointerHandler.setDTDHandler(this.fDTDHandler);
/* 158 */     if (this.fDTDHandler != null) {
/* 159 */       this.fDTDHandler.setDTDSource(this.fXPointerHandler);
/*     */     }
/*     */ 
/* 164 */     XMLDocumentSource prev = null;
/* 165 */     if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*     */     {
/* 168 */       prev = this.fSchemaValidator.getDocumentSource();
/*     */     }
/*     */     else
/*     */     {
/* 172 */       prev = this.fLastComponent;
/* 173 */       this.fLastComponent = this.fXPointerHandler;
/*     */     }
/*     */ 
/* 176 */     XMLDocumentHandler next = prev.getDocumentHandler();
/* 177 */     prev.setDocumentHandler(this.fXIncludeHandler);
/* 178 */     this.fXIncludeHandler.setDocumentSource(prev);
/*     */ 
/* 180 */     if (next != null) {
/* 181 */       this.fXIncludeHandler.setDocumentHandler(next);
/* 182 */       next.setDocumentSource(this.fXIncludeHandler);
/*     */     }
/*     */ 
/* 185 */     this.fXIncludeHandler.setDocumentHandler(this.fXPointerHandler);
/* 186 */     this.fXPointerHandler.setDocumentSource(this.fXIncludeHandler);
/*     */   }
/*     */ 
/*     */   protected void configureXML11Pipeline() {
/* 190 */     super.configureXML11Pipeline();
/*     */ 
/* 193 */     this.fXML11DTDScanner.setDTDHandler(this.fXML11DTDProcessor);
/* 194 */     this.fXML11DTDProcessor.setDTDSource(this.fXML11DTDScanner);
/*     */ 
/* 196 */     this.fDTDProcessor.setDTDHandler(this.fXIncludeHandler);
/* 197 */     this.fXIncludeHandler.setDTDSource(this.fXML11DTDProcessor);
/* 198 */     this.fXIncludeHandler.setDTDHandler(this.fXPointerHandler);
/* 199 */     this.fXPointerHandler.setDTDSource(this.fXIncludeHandler);
/* 200 */     this.fXPointerHandler.setDTDHandler(this.fDTDHandler);
/* 201 */     if (this.fDTDHandler != null) {
/* 202 */       this.fDTDHandler.setDTDSource(this.fXPointerHandler);
/*     */     }
/*     */ 
/* 208 */     XMLDocumentSource prev = null;
/* 209 */     if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*     */     {
/* 212 */       prev = this.fSchemaValidator.getDocumentSource();
/*     */     }
/*     */     else
/*     */     {
/* 216 */       prev = this.fLastComponent;
/* 217 */       this.fLastComponent = this.fXPointerHandler;
/*     */     }
/*     */ 
/* 220 */     XMLDocumentHandler next = prev.getDocumentHandler();
/* 221 */     prev.setDocumentHandler(this.fXIncludeHandler);
/* 222 */     this.fXIncludeHandler.setDocumentSource(prev);
/*     */ 
/* 224 */     if (next != null) {
/* 225 */       this.fXIncludeHandler.setDocumentHandler(next);
/* 226 */       next.setDocumentSource(this.fXIncludeHandler);
/*     */     }
/*     */ 
/* 229 */     this.fXIncludeHandler.setDocumentHandler(this.fXPointerHandler);
/* 230 */     this.fXPointerHandler.setDocumentSource(this.fXIncludeHandler);
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 241 */     super.setProperty(propertyId, value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XPointerParserConfiguration
 * JD-Core Version:    0.6.2
 */