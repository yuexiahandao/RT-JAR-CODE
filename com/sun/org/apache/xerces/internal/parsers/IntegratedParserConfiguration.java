/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLNSDTDValidator;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class IntegratedParserConfiguration extends StandardParserConfiguration
/*     */ {
/*     */   protected XMLNSDocumentScannerImpl fNamespaceScanner;
/*     */   protected XMLDocumentScannerImpl fNonNSScanner;
/*     */   protected XMLDTDValidator fNonNSDTDValidator;
/*     */ 
/*     */   public IntegratedParserConfiguration()
/*     */   {
/*  96 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   public IntegratedParserConfiguration(SymbolTable symbolTable)
/*     */   {
/* 105 */     this(symbolTable, null, null);
/*     */   }
/*     */ 
/*     */   public IntegratedParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 121 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public IntegratedParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 139 */     super(symbolTable, grammarPool, parentSettings);
/*     */ 
/* 142 */     this.fNonNSScanner = new XMLDocumentScannerImpl();
/* 143 */     this.fNonNSDTDValidator = new XMLDTDValidator();
/*     */ 
/* 146 */     addComponent(this.fNonNSScanner);
/* 147 */     addComponent(this.fNonNSDTDValidator);
/*     */   }
/*     */ 
/*     */   protected void configurePipeline()
/*     */   {
/* 156 */     setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fDatatypeValidatorFactory);
/*     */ 
/* 159 */     configureDTDPipeline();
/*     */ 
/* 162 */     if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/* 163 */       this.fProperties.put("http://apache.org/xml/properties/internal/namespace-binder", this.fNamespaceBinder);
/* 164 */       this.fScanner = this.fNamespaceScanner;
/* 165 */       this.fProperties.put("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/* 166 */       if (this.fDTDValidator != null) {
/* 167 */         this.fProperties.put("http://apache.org/xml/properties/internal/validator/dtd", this.fDTDValidator);
/* 168 */         this.fNamespaceScanner.setDTDValidator(this.fDTDValidator);
/* 169 */         this.fNamespaceScanner.setDocumentHandler(this.fDTDValidator);
/* 170 */         this.fDTDValidator.setDocumentSource(this.fNamespaceScanner);
/* 171 */         this.fDTDValidator.setDocumentHandler(this.fDocumentHandler);
/* 172 */         if (this.fDocumentHandler != null) {
/* 173 */           this.fDocumentHandler.setDocumentSource(this.fDTDValidator);
/*     */         }
/* 175 */         this.fLastComponent = this.fDTDValidator;
/*     */       }
/*     */       else {
/* 178 */         this.fNamespaceScanner.setDocumentHandler(this.fDocumentHandler);
/* 179 */         this.fNamespaceScanner.setDTDValidator(null);
/* 180 */         if (this.fDocumentHandler != null) {
/* 181 */           this.fDocumentHandler.setDocumentSource(this.fNamespaceScanner);
/*     */         }
/* 183 */         this.fLastComponent = this.fNamespaceScanner;
/*     */       }
/*     */     }
/*     */     else {
/* 187 */       this.fScanner = this.fNonNSScanner;
/* 188 */       this.fProperties.put("http://apache.org/xml/properties/internal/document-scanner", this.fNonNSScanner);
/* 189 */       if (this.fNonNSDTDValidator != null) {
/* 190 */         this.fProperties.put("http://apache.org/xml/properties/internal/validator/dtd", this.fNonNSDTDValidator);
/* 191 */         this.fNonNSScanner.setDocumentHandler(this.fNonNSDTDValidator);
/* 192 */         this.fNonNSDTDValidator.setDocumentSource(this.fNonNSScanner);
/* 193 */         this.fNonNSDTDValidator.setDocumentHandler(this.fDocumentHandler);
/* 194 */         if (this.fDocumentHandler != null) {
/* 195 */           this.fDocumentHandler.setDocumentSource(this.fNonNSDTDValidator);
/*     */         }
/* 197 */         this.fLastComponent = this.fNonNSDTDValidator;
/*     */       }
/*     */       else {
/* 200 */         this.fScanner.setDocumentHandler(this.fDocumentHandler);
/* 201 */         if (this.fDocumentHandler != null) {
/* 202 */           this.fDocumentHandler.setDocumentSource(this.fScanner);
/*     */         }
/* 204 */         this.fLastComponent = this.fScanner;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 209 */     if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*     */     {
/* 211 */       if (this.fSchemaValidator == null) {
/* 212 */         this.fSchemaValidator = new XMLSchemaValidator();
/*     */ 
/* 215 */         this.fProperties.put("http://apache.org/xml/properties/internal/validator/schema", this.fSchemaValidator);
/* 216 */         addComponent(this.fSchemaValidator);
/*     */ 
/* 218 */         if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/* 219 */           XSMessageFormatter xmft = new XSMessageFormatter();
/* 220 */           this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", xmft);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 225 */       this.fLastComponent.setDocumentHandler(this.fSchemaValidator);
/* 226 */       this.fSchemaValidator.setDocumentSource(this.fLastComponent);
/* 227 */       this.fSchemaValidator.setDocumentHandler(this.fDocumentHandler);
/* 228 */       if (this.fDocumentHandler != null) {
/* 229 */         this.fDocumentHandler.setDocumentSource(this.fSchemaValidator);
/*     */       }
/* 231 */       this.fLastComponent = this.fSchemaValidator;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected XMLDocumentScanner createDocumentScanner()
/*     */   {
/* 240 */     this.fNamespaceScanner = new XMLNSDocumentScannerImpl();
/* 241 */     return this.fNamespaceScanner;
/*     */   }
/*     */ 
/*     */   protected XMLDTDValidator createDTDValidator()
/*     */   {
/* 248 */     return new XMLNSDTDValidator();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.IntegratedParserConfiguration
 * JD-Core Version:    0.6.2
 */