/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XML11DTDScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ 
/*     */ public class XML11DTDProcessor extends XMLDTDLoader
/*     */ {
/*     */   public XML11DTDProcessor()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XML11DTDProcessor(SymbolTable symbolTable)
/*     */   {
/*  95 */     super(symbolTable);
/*     */   }
/*     */ 
/*     */   public XML11DTDProcessor(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 100 */     super(symbolTable, grammarPool);
/*     */   }
/*     */ 
/*     */   XML11DTDProcessor(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLErrorReporter errorReporter, XMLEntityResolver entityResolver)
/*     */   {
/* 106 */     super(symbolTable, grammarPool, errorReporter, entityResolver);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNmtoken(String nmtoken)
/*     */   {
/* 112 */     return XML11Char.isXML11ValidNmtoken(nmtoken);
/*     */   }
/*     */ 
/*     */   protected boolean isValidName(String name) {
/* 116 */     return XML11Char.isXML11ValidName(name);
/*     */   }
/*     */ 
/*     */   protected XMLDTDScannerImpl createDTDScanner(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityManager)
/*     */   {
/* 121 */     return new XML11DTDScannerImpl(symbolTable, errorReporter, entityManager);
/*     */   }
/*     */ 
/*     */   protected short getScannerVersion() {
/* 125 */     return 2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XML11DTDProcessor
 * JD-Core Version:    0.6.2
 */