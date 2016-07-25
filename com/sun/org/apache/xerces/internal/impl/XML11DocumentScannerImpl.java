/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class XML11DocumentScannerImpl extends XMLDocumentScannerImpl
/*     */ {
/* 106 */   private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
/* 107 */   private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
/* 108 */   private final XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
/*     */ 
/*     */   protected int scanContent(XMLStringBuffer content)
/*     */     throws IOException, XNIException
/*     */   {
/* 130 */     this.fTempString.length = 0;
/* 131 */     int c = this.fEntityScanner.scanContent(this.fTempString);
/* 132 */     content.append(this.fTempString);
/*     */ 
/* 134 */     if ((c == 13) || (c == 133) || (c == 8232))
/*     */     {
/* 138 */       this.fEntityScanner.scanChar();
/* 139 */       content.append((char)c);
/* 140 */       c = -1;
/*     */     }
/*     */ 
/* 146 */     if (c == 93) {
/* 147 */       content.append((char)this.fEntityScanner.scanChar());
/*     */ 
/* 151 */       this.fInScanContent = true;
/*     */ 
/* 156 */       if (this.fEntityScanner.skipChar(93)) {
/* 157 */         content.append(']');
/* 158 */         while (this.fEntityScanner.skipChar(93)) {
/* 159 */           content.append(']');
/*     */         }
/* 161 */         if (this.fEntityScanner.skipChar(62)) {
/* 162 */           reportFatalError("CDEndInContent", null);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 168 */       this.fInScanContent = false;
/* 169 */       c = -1;
/*     */     }
/* 171 */     return c;
/*     */   }
/*     */ 
/*     */   protected boolean scanAttributeValue(XMLString value, XMLString nonNormalizedValue, String atName, boolean checkEntities, String eleName)
/*     */     throws IOException, XNIException
/*     */   {
/* 201 */     int quote = this.fEntityScanner.peekChar();
/* 202 */     if ((quote != 39) && (quote != 34)) {
/* 203 */       reportFatalError("OpenQuoteExpected", new Object[] { eleName, atName });
/*     */     }
/*     */ 
/* 206 */     this.fEntityScanner.scanChar();
/* 207 */     int entityDepth = this.fEntityDepth;
/*     */ 
/* 209 */     int c = this.fEntityScanner.scanLiteral(quote, value);
/*     */ 
/* 215 */     int fromIndex = 0;
/* 216 */     if ((c == quote) && ((fromIndex = isUnchangedByNormalization(value)) == -1))
/*     */     {
/* 218 */       nonNormalizedValue.setValues(value);
/* 219 */       int cquote = this.fEntityScanner.scanChar();
/* 220 */       if (cquote != quote) {
/* 221 */         reportFatalError("CloseQuoteExpected", new Object[] { eleName, atName });
/*     */       }
/* 223 */       return true;
/*     */     }
/* 225 */     this.fStringBuffer2.clear();
/* 226 */     this.fStringBuffer2.append(value);
/* 227 */     normalizeWhitespace(value, fromIndex);
/*     */ 
/* 232 */     if (c != quote) {
/* 233 */       this.fScanningAttribute = true;
/* 234 */       this.fStringBuffer.clear();
/*     */       do {
/* 236 */         this.fStringBuffer.append(value);
/*     */ 
/* 241 */         if (c == 38) {
/* 242 */           this.fEntityScanner.skipChar(38);
/* 243 */           if (entityDepth == this.fEntityDepth) {
/* 244 */             this.fStringBuffer2.append('&');
/*     */           }
/* 246 */           if (this.fEntityScanner.skipChar(35)) {
/* 247 */             if (entityDepth == this.fEntityDepth) {
/* 248 */               this.fStringBuffer2.append('#');
/*     */             }
/*     */ 
/* 250 */             int ch = scanCharReferenceValue(this.fStringBuffer, this.fStringBuffer2);
/* 251 */             if (ch == -1);
/*     */           }
/*     */           else
/*     */           {
/* 260 */             String entityName = this.fEntityScanner.scanName();
/* 261 */             if (entityName == null) {
/* 262 */               reportFatalError("NameRequiredInReference", null);
/*     */             }
/* 264 */             else if (entityDepth == this.fEntityDepth) {
/* 265 */               this.fStringBuffer2.append(entityName);
/*     */             }
/* 267 */             if (!this.fEntityScanner.skipChar(59)) {
/* 268 */               reportFatalError("SemicolonRequiredInReference", new Object[] { entityName });
/*     */             }
/* 271 */             else if (entityDepth == this.fEntityDepth) {
/* 272 */               this.fStringBuffer2.append(';');
/*     */             }
/* 274 */             if (entityName == fAmpSymbol) {
/* 275 */               this.fStringBuffer.append('&');
/*     */             }
/* 282 */             else if (entityName == fAposSymbol) {
/* 283 */               this.fStringBuffer.append('\'');
/*     */             }
/* 290 */             else if (entityName == fLtSymbol) {
/* 291 */               this.fStringBuffer.append('<');
/*     */             }
/* 298 */             else if (entityName == fGtSymbol) {
/* 299 */               this.fStringBuffer.append('>');
/*     */             }
/* 306 */             else if (entityName == fQuotSymbol) {
/* 307 */               this.fStringBuffer.append('"');
/*     */             }
/* 315 */             else if (this.fEntityManager.isExternalEntity(entityName)) {
/* 316 */               reportFatalError("ReferenceToExternalEntity", new Object[] { entityName });
/*     */             }
/*     */             else
/*     */             {
/* 320 */               if (!this.fEntityManager.isDeclaredEntity(entityName))
/*     */               {
/* 322 */                 if (checkEntities) {
/* 323 */                   if (this.fValidation) {
/* 324 */                     this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[] { entityName }, (short)1);
/*     */                   }
/*     */ 
/*     */                 }
/*     */                 else
/*     */                 {
/* 331 */                   reportFatalError("EntityNotDeclared", new Object[] { entityName });
/*     */                 }
/*     */               }
/*     */ 
/* 335 */               this.fEntityManager.startEntity(entityName, true);
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/* 340 */         else if (c == 60) {
/* 341 */           reportFatalError("LessthanInAttValue", new Object[] { eleName, atName });
/*     */ 
/* 343 */           this.fEntityScanner.scanChar();
/* 344 */           if (entityDepth == this.fEntityDepth) {
/* 345 */             this.fStringBuffer2.append((char)c);
/*     */           }
/*     */         }
/* 348 */         else if ((c == 37) || (c == 93)) {
/* 349 */           this.fEntityScanner.scanChar();
/* 350 */           this.fStringBuffer.append((char)c);
/* 351 */           if (entityDepth == this.fEntityDepth) {
/* 352 */             this.fStringBuffer2.append((char)c);
/*     */           }
/*     */ 
/*     */         }
/* 362 */         else if ((c == 10) || (c == 13) || (c == 133) || (c == 8232)) {
/* 363 */           this.fEntityScanner.scanChar();
/* 364 */           this.fStringBuffer.append(' ');
/* 365 */           if (entityDepth == this.fEntityDepth) {
/* 366 */             this.fStringBuffer2.append('\n');
/*     */           }
/*     */         }
/* 369 */         else if ((c != -1) && (XMLChar.isHighSurrogate(c))) {
/* 370 */           this.fStringBuffer3.clear();
/* 371 */           if (scanSurrogates(this.fStringBuffer3)) {
/* 372 */             this.fStringBuffer.append(this.fStringBuffer3);
/* 373 */             if (entityDepth == this.fEntityDepth) {
/* 374 */               this.fStringBuffer2.append(this.fStringBuffer3);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/* 383 */         else if ((c != -1) && (isInvalidLiteral(c))) {
/* 384 */           reportFatalError("InvalidCharInAttValue", new Object[] { eleName, atName, Integer.toString(c, 16) });
/*     */ 
/* 386 */           this.fEntityScanner.scanChar();
/* 387 */           if (entityDepth == this.fEntityDepth) {
/* 388 */             this.fStringBuffer2.append((char)c);
/*     */           }
/*     */         }
/* 391 */         c = this.fEntityScanner.scanLiteral(quote, value);
/* 392 */         if (entityDepth == this.fEntityDepth) {
/* 393 */           this.fStringBuffer2.append(value);
/*     */         }
/* 395 */         normalizeWhitespace(value);
/* 396 */       }while ((c != quote) || (entityDepth != this.fEntityDepth));
/* 397 */       this.fStringBuffer.append(value);
/*     */ 
/* 402 */       value.setValues(this.fStringBuffer);
/* 403 */       this.fScanningAttribute = false;
/*     */     }
/* 405 */     nonNormalizedValue.setValues(this.fStringBuffer2);
/*     */ 
/* 408 */     int cquote = this.fEntityScanner.scanChar();
/* 409 */     if (cquote != quote) {
/* 410 */       reportFatalError("CloseQuoteExpected", new Object[] { eleName, atName });
/*     */     }
/* 412 */     return nonNormalizedValue.equals(value.ch, value.offset, value.length);
/*     */   }
/*     */ 
/*     */   protected boolean scanPubidLiteral(XMLString literal)
/*     */     throws IOException, XNIException
/*     */   {
/* 443 */     int quote = this.fEntityScanner.scanChar();
/* 444 */     if ((quote != 39) && (quote != 34)) {
/* 445 */       reportFatalError("QuoteRequiredInPublicID", null);
/* 446 */       return false;
/*     */     }
/*     */ 
/* 449 */     this.fStringBuffer.clear();
/*     */ 
/* 451 */     boolean skipSpace = true;
/* 452 */     boolean dataok = true;
/*     */     while (true) {
/* 454 */       int c = this.fEntityScanner.scanChar();
/*     */ 
/* 456 */       if ((c == 32) || (c == 10) || (c == 13) || (c == 133) || (c == 8232)) {
/* 457 */         if (!skipSpace)
/*     */         {
/* 459 */           this.fStringBuffer.append(' ');
/* 460 */           skipSpace = true;
/*     */         }
/*     */       } else {
/* 463 */         if (c == quote) {
/* 464 */           if (skipSpace)
/*     */           {
/* 466 */             this.fStringBuffer.length -= 1;
/*     */           }
/* 468 */           literal.setValues(this.fStringBuffer);
/* 469 */           break;
/*     */         }
/* 471 */         if (XMLChar.isPubid(c)) {
/* 472 */           this.fStringBuffer.append((char)c);
/* 473 */           skipSpace = false;
/*     */         } else {
/* 475 */           if (c == -1) {
/* 476 */             reportFatalError("PublicIDUnterminated", null);
/* 477 */             return false;
/*     */           }
/*     */ 
/* 480 */           dataok = false;
/* 481 */           reportFatalError("InvalidCharInPublicID", new Object[] { Integer.toHexString(c) });
/*     */         }
/*     */       }
/*     */     }
/* 485 */     return dataok;
/*     */   }
/*     */ 
/*     */   protected void normalizeWhitespace(XMLString value)
/*     */   {
/* 493 */     int end = value.offset + value.length;
/* 494 */     for (int i = value.offset; i < end; i++) {
/* 495 */       int c = value.ch[i];
/* 496 */       if (XMLChar.isSpace(c))
/* 497 */         value.ch[i] = ' ';
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void normalizeWhitespace(XMLString value, int fromIndex)
/*     */   {
/* 507 */     int end = value.offset + value.length;
/* 508 */     for (int i = value.offset + fromIndex; i < end; i++) {
/* 509 */       int c = value.ch[i];
/* 510 */       if (XMLChar.isSpace(c))
/* 511 */         value.ch[i] = ' ';
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int isUnchangedByNormalization(XMLString value)
/*     */   {
/* 524 */     int end = value.offset + value.length;
/* 525 */     for (int i = value.offset; i < end; i++) {
/* 526 */       int c = value.ch[i];
/* 527 */       if (XMLChar.isSpace(c)) {
/* 528 */         return i - value.offset;
/*     */       }
/*     */     }
/* 531 */     return -1;
/*     */   }
/*     */ 
/*     */   protected boolean isInvalid(int value)
/*     */   {
/* 538 */     return XML11Char.isXML11Invalid(value);
/*     */   }
/*     */ 
/*     */   protected boolean isInvalidLiteral(int value)
/*     */   {
/* 545 */     return !XML11Char.isXML11ValidLiteral(value);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNameChar(int value)
/*     */   {
/* 552 */     return XML11Char.isXML11Name(value);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNameStartChar(int value)
/*     */   {
/* 559 */     return XML11Char.isXML11NameStart(value);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNCName(int value)
/*     */   {
/* 566 */     return XML11Char.isXML11NCName(value);
/*     */   }
/*     */ 
/*     */   protected boolean isValidNameStartHighSurrogate(int value)
/*     */   {
/* 574 */     return XML11Char.isXML11NameHighSurrogate(value);
/*     */   }
/*     */ 
/*     */   protected boolean versionSupported(String version) {
/* 578 */     return (version.equals("1.1")) || (version.equals("1.0"));
/*     */   }
/*     */ 
/*     */   protected String getVersionNotSupportedKey()
/*     */   {
/* 585 */     return "VersionNotSupported11";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XML11DocumentScannerImpl
 * JD-Core Version:    0.6.2
 */