/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.xml.internal.stream.Entity.ScannedEntity;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class XMLVersionDetector
/*     */ {
/*  91 */   private static final char[] XML11_VERSION = { '1', '.', '1' };
/*     */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/* 113 */   protected static final String fVersionSymbol = "version".intern();
/*     */ 
/* 116 */   protected static final String fXMLSymbol = "[xml]".intern();
/*     */   protected SymbolTable fSymbolTable;
/*     */   protected XMLErrorReporter fErrorReporter;
/*     */   protected XMLEntityManager fEntityManager;
/* 127 */   protected String fEncoding = null;
/*     */ 
/* 129 */   private XMLString fVersionNum = new XMLString();
/*     */ 
/* 131 */   private final char[] fExpectedVersionString = { '<', '?', 'x', 'm', 'l', ' ', 'v', 'e', 'r', 's', 'i', 'o', 'n', '=', ' ', ' ', ' ', ' ', ' ' };
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager)
/*     */     throws XMLConfigurationException
/*     */   {
/* 146 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/* 147 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/* 148 */     this.fEntityManager = ((XMLEntityManager)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
/* 149 */     for (int i = 14; i < this.fExpectedVersionString.length; i++)
/* 150 */       this.fExpectedVersionString[i] = ' ';
/*     */   }
/*     */ 
/*     */   public void startDocumentParsing(XMLEntityHandler scanner, short version)
/*     */   {
/* 161 */     if (version == 1) {
/* 162 */       this.fEntityManager.setScannerVersion((short)1);
/*     */     }
/*     */     else {
/* 165 */       this.fEntityManager.setScannerVersion((short)2);
/*     */     }
/*     */ 
/* 168 */     this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
/*     */ 
/* 173 */     this.fEntityManager.setEntityHandler(scanner);
/*     */ 
/* 175 */     scanner.startEntity(fXMLSymbol, this.fEntityManager.getCurrentResourceIdentifier(), this.fEncoding, null);
/*     */   }
/*     */ 
/*     */   public short determineDocVersion(XMLInputSource inputSource)
/*     */     throws IOException
/*     */   {
/* 189 */     this.fEncoding = this.fEntityManager.setupCurrentEntity(fXMLSymbol, inputSource, false, true);
/*     */ 
/* 193 */     this.fEntityManager.setScannerVersion((short)1);
/* 194 */     XMLEntityScanner scanner = this.fEntityManager.getEntityScanner();
/*     */     try {
/* 196 */       if (!scanner.skipString("<?xml"))
/*     */       {
/* 198 */         return 1;
/*     */       }
/* 200 */       if (!scanner.skipDeclSpaces()) {
/* 201 */         fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 5);
/* 202 */         return 1;
/*     */       }
/* 204 */       if (!scanner.skipString("version")) {
/* 205 */         fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 6);
/* 206 */         return 1;
/*     */       }
/* 208 */       scanner.skipDeclSpaces();
/*     */ 
/* 210 */       if (scanner.peekChar() != 61) {
/* 211 */         fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 13);
/* 212 */         return 1;
/*     */       }
/* 214 */       scanner.scanChar();
/* 215 */       scanner.skipDeclSpaces();
/* 216 */       int quoteChar = scanner.scanChar();
/* 217 */       this.fExpectedVersionString[14] = ((char)quoteChar);
/* 218 */       for (int versionPos = 0; versionPos < XML11_VERSION.length; versionPos++) {
/* 219 */         this.fExpectedVersionString[(15 + versionPos)] = ((char)scanner.scanChar());
/*     */       }
/*     */ 
/* 222 */       this.fExpectedVersionString[18] = ((char)scanner.scanChar());
/* 223 */       fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 19);
/* 224 */       int matched = 0;
/* 225 */       while ((matched < XML11_VERSION.length) && 
/* 226 */         (this.fExpectedVersionString[(15 + matched)] == XML11_VERSION[matched])) {
/* 225 */         matched++;
/*     */       }
/*     */ 
/* 229 */       if (matched == XML11_VERSION.length)
/* 230 */         return 2;
/* 231 */       return 1;
/*     */     }
/*     */     catch (EOFException e)
/*     */     {
/* 235 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "PrematureEOF", null, (short)2);
/*     */     }
/*     */ 
/* 240 */     return 1;
/*     */   }
/*     */ 
/*     */   private void fixupCurrentEntity(XMLEntityManager manager, char[] scannedChars, int length)
/*     */   {
/* 250 */     Entity.ScannedEntity currentEntity = manager.getCurrentEntity();
/* 251 */     if (currentEntity.count - currentEntity.position + length > currentEntity.ch.length)
/*     */     {
/* 253 */       char[] tempCh = currentEntity.ch;
/* 254 */       currentEntity.ch = new char[length + currentEntity.count - currentEntity.position + 1];
/* 255 */       System.arraycopy(tempCh, 0, currentEntity.ch, 0, tempCh.length);
/*     */     }
/* 257 */     if (currentEntity.position < length)
/*     */     {
/* 259 */       System.arraycopy(currentEntity.ch, currentEntity.position, currentEntity.ch, length, currentEntity.count - currentEntity.position);
/* 260 */       currentEntity.count += length - currentEntity.position;
/*     */     }
/*     */     else {
/* 263 */       for (int i = length; i < currentEntity.position; i++) {
/* 264 */         currentEntity.ch[i] = ' ';
/*     */       }
/*     */     }
/* 267 */     System.arraycopy(scannedChars, 0, currentEntity.ch, 0, length);
/* 268 */     currentEntity.position = 0;
/* 269 */     currentEntity.baseCharOffset = 0;
/* 270 */     currentEntity.startPosition = 0;
/* 271 */     currentEntity.columnNumber = (currentEntity.lineNumber = 1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLVersionDetector
 * JD-Core Version:    0.6.2
 */