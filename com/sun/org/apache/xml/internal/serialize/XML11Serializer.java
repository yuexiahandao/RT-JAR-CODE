/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import org.w3c.dom.DOMErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XML11Serializer extends XMLSerializer
/*     */ {
/*     */   protected static final boolean DEBUG = false;
/*     */   protected NamespaceSupport fNSBinder;
/*     */   protected NamespaceSupport fLocalNSBinder;
/*     */   protected SymbolTable fSymbolTable;
/* 111 */   protected boolean fDOML1 = false;
/*     */ 
/* 113 */   protected int fNamespaceCounter = 1;
/*     */   protected static final String PREFIX = "NS";
/* 123 */   protected boolean fNamespaces = false;
/*     */   private boolean fPreserveSpace;
/*     */ 
/*     */   public XML11Serializer()
/*     */   {
/* 136 */     this._format.setVersion("1.1");
/*     */   }
/*     */ 
/*     */   public XML11Serializer(OutputFormat format)
/*     */   {
/* 146 */     super(format);
/* 147 */     this._format.setVersion("1.1");
/*     */   }
/*     */ 
/*     */   public XML11Serializer(Writer writer, OutputFormat format)
/*     */   {
/* 160 */     super(writer, format);
/* 161 */     this._format.setVersion("1.1");
/*     */   }
/*     */ 
/*     */   public XML11Serializer(OutputStream output, OutputFormat format)
/*     */   {
/* 174 */     super(output, format != null ? format : new OutputFormat("xml", null, false));
/* 175 */     this._format.setVersion("1.1");
/*     */   }
/*     */ 
/*     */   public void characters(char[] chars, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 189 */       ElementState state = content();
/*     */ 
/* 195 */       if ((state.inCData) || (state.doCData))
/*     */       {
/* 201 */         if (!state.inCData) {
/* 202 */           this._printer.printText("<![CDATA[");
/* 203 */           state.inCData = true;
/*     */         }
/* 205 */         int saveIndent = this._printer.getNextIndent();
/* 206 */         this._printer.setNextIndent(0);
/*     */ 
/* 208 */         int end = start + length;
/* 209 */         for (int index = start; index < end; index++) {
/* 210 */           char ch = chars[index];
/* 211 */           if ((ch == ']') && (index + 2 < end) && (chars[(index + 1)] == ']') && (chars[(index + 2)] == '>'))
/*     */           {
/* 213 */             this._printer.printText("]]]]><![CDATA[>");
/* 214 */             index += 2;
/*     */           }
/* 217 */           else if (!XML11Char.isXML11Valid(ch))
/*     */           {
/* 219 */             index++; if (index < end) {
/* 220 */               surrogates(ch, chars[index]);
/*     */             }
/*     */             else {
/* 223 */               fatalError("The character '" + ch + "' is an invalid XML character");
/*     */             }
/*     */ 
/*     */           }
/* 227 */           else if ((this._encodingInfo.isPrintable(ch)) && (XML11Char.isXML11ValidLiteral(ch))) {
/* 228 */             this._printer.printText(ch);
/*     */           }
/*     */           else {
/* 231 */             this._printer.printText("]]>&#x");
/* 232 */             this._printer.printText(Integer.toHexString(ch));
/* 233 */             this._printer.printText(";<![CDATA[");
/*     */           }
/*     */         }
/*     */ 
/* 237 */         this._printer.setNextIndent(saveIndent);
/*     */       }
/* 243 */       else if (state.preserveSpace)
/*     */       {
/* 248 */         int saveIndent = this._printer.getNextIndent();
/* 249 */         this._printer.setNextIndent(0);
/* 250 */         printText(chars, start, length, true, state.unescaped);
/* 251 */         this._printer.setNextIndent(saveIndent);
/*     */       } else {
/* 253 */         printText(chars, start, length, false, state.unescaped);
/*     */       }
/*     */     }
/*     */     catch (IOException except) {
/* 257 */       throw new SAXException(except);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void printEscaped(String source)
/*     */     throws IOException
/*     */   {
/* 266 */     int length = source.length();
/* 267 */     for (int i = 0; i < length; i++) {
/* 268 */       int ch = source.charAt(i);
/* 269 */       if (!XML11Char.isXML11Valid(ch)) {
/* 270 */         i++; if (i < length)
/* 271 */           surrogates(ch, source.charAt(i));
/*     */         else {
/* 273 */           fatalError("The character '" + (char)ch + "' is an invalid XML character");
/*     */         }
/*     */ 
/*     */       }
/* 277 */       else if ((ch == 10) || (ch == 13) || (ch == 9) || (ch == 133) || (ch == 8232)) {
/* 278 */         printHex(ch);
/* 279 */       } else if (ch == 60) {
/* 280 */         this._printer.printText("&lt;");
/* 281 */       } else if (ch == 38) {
/* 282 */         this._printer.printText("&amp;");
/* 283 */       } else if (ch == 34) {
/* 284 */         this._printer.printText("&quot;");
/* 285 */       } else if ((ch >= 32) && (this._encodingInfo.isPrintable((char)ch))) {
/* 286 */         this._printer.printText((char)ch);
/*     */       } else {
/* 288 */         printHex(ch);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void printCDATAText(String text) throws IOException {
/* 294 */     int length = text.length();
/*     */ 
/* 297 */     for (int index = 0; index < length; index++) {
/* 298 */       char ch = text.charAt(index);
/*     */ 
/* 300 */       if ((ch == ']') && (index + 2 < length) && (text.charAt(index + 1) == ']') && (text.charAt(index + 2) == '>'))
/*     */       {
/* 304 */         if (this.fDOMErrorHandler != null)
/*     */         {
/* 307 */           if (((this.features & 0x10) == 0) && ((this.features & 0x2) == 0))
/*     */           {
/* 310 */             String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "EndingCDATA", null);
/*     */ 
/* 315 */             modifyDOMError(msg, (short)3, null, this.fCurrentNode);
/*     */ 
/* 319 */             boolean continueProcess = this.fDOMErrorHandler.handleError(this.fDOMError);
/*     */ 
/* 321 */             if (!continueProcess)
/* 322 */               throw new IOException();
/*     */           }
/*     */           else
/*     */           {
/* 326 */             String msg = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "SplittingCDATA", null);
/*     */ 
/* 331 */             modifyDOMError(msg, (short)1, null, this.fCurrentNode);
/*     */ 
/* 335 */             this.fDOMErrorHandler.handleError(this.fDOMError);
/*     */           }
/*     */         }
/*     */ 
/* 339 */         this._printer.printText("]]]]><![CDATA[>");
/* 340 */         index += 2;
/*     */       }
/* 344 */       else if (!XML11Char.isXML11Valid(ch))
/*     */       {
/* 346 */         index++; if (index < length)
/* 347 */           surrogates(ch, text.charAt(index));
/*     */         else {
/* 349 */           fatalError("The character '" + ch + "' is an invalid XML character");
/*     */         }
/*     */ 
/*     */       }
/* 356 */       else if ((this._encodingInfo.isPrintable(ch)) && (XML11Char.isXML11ValidLiteral(ch)))
/*     */       {
/* 358 */         this._printer.printText(ch);
/*     */       }
/*     */       else
/*     */       {
/* 362 */         this._printer.printText("]]>&#x");
/* 363 */         this._printer.printText(Integer.toHexString(ch));
/* 364 */         this._printer.printText(";<![CDATA[");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void printXMLChar(int ch)
/*     */     throws IOException
/*     */   {
/* 375 */     if ((ch == 13) || (ch == 133) || (ch == 8232))
/* 376 */       printHex(ch);
/* 377 */     else if (ch == 60)
/* 378 */       this._printer.printText("&lt;");
/* 379 */     else if (ch == 38)
/* 380 */       this._printer.printText("&amp;");
/* 381 */     else if (ch == 62)
/*     */     {
/* 384 */       this._printer.printText("&gt;");
/* 385 */     } else if ((this._encodingInfo.isPrintable((char)ch)) && (XML11Char.isXML11ValidLiteral(ch)))
/* 386 */       this._printer.printText((char)ch);
/*     */     else
/* 388 */       printHex(ch);
/*     */   }
/*     */ 
/*     */   protected final void surrogates(int high, int low)
/*     */     throws IOException
/*     */   {
/* 395 */     if (XMLChar.isHighSurrogate(high)) {
/* 396 */       if (!XMLChar.isLowSurrogate(low))
/*     */       {
/* 398 */         fatalError("The character '" + (char)low + "' is an invalid XML character");
/*     */       }
/*     */       else {
/* 401 */         int supplemental = XMLChar.supplemental((char)high, (char)low);
/* 402 */         if (!XML11Char.isXML11Valid(supplemental))
/*     */         {
/* 404 */           fatalError("The character '" + (char)supplemental + "' is an invalid XML character");
/*     */         }
/* 407 */         else if (content().inCData) {
/* 408 */           this._printer.printText("]]>&#x");
/* 409 */           this._printer.printText(Integer.toHexString(supplemental));
/* 410 */           this._printer.printText(";<![CDATA[");
/*     */         }
/*     */         else {
/* 413 */           printHex(supplemental);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/* 418 */       fatalError("The character '" + (char)high + "' is an invalid XML character");
/*     */   }
/*     */ 
/*     */   protected void printText(String text, boolean preserveSpace, boolean unescaped)
/*     */     throws IOException
/*     */   {
/* 428 */     int length = text.length();
/* 429 */     if (preserveSpace)
/*     */     {
/* 434 */       for (int index = 0; index < length; index++) {
/* 435 */         char ch = text.charAt(index);
/* 436 */         if (!XML11Char.isXML11Valid(ch))
/*     */         {
/* 438 */           index++; if (index < length)
/* 439 */             surrogates(ch, text.charAt(index));
/*     */           else {
/* 441 */             fatalError("The character '" + ch + "' is an invalid XML character");
/*     */           }
/*     */ 
/*     */         }
/* 445 */         else if ((unescaped) && (XML11Char.isXML11ValidLiteral(ch))) {
/* 446 */           this._printer.printText(ch);
/*     */         } else {
/* 448 */           printXMLChar(ch);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 456 */     for (int index = 0; index < length; index++) {
/* 457 */       char ch = text.charAt(index);
/* 458 */       if (!XML11Char.isXML11Valid(ch))
/*     */       {
/* 460 */         index++; if (index < length)
/* 461 */           surrogates(ch, text.charAt(index));
/*     */         else {
/* 463 */           fatalError("The character '" + ch + "' is an invalid XML character");
/*     */         }
/*     */ 
/*     */       }
/* 468 */       else if ((unescaped) && (XML11Char.isXML11ValidLiteral(ch))) {
/* 469 */         this._printer.printText(ch);
/*     */       } else {
/* 471 */         printXMLChar(ch);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void printText(char[] chars, int start, int length, boolean preserveSpace, boolean unescaped)
/*     */     throws IOException
/*     */   {
/* 483 */     if (preserveSpace)
/*     */     {
/* 488 */       while (length-- > 0) {
/* 489 */         char ch = chars[(start++)];
/* 490 */         if (!XML11Char.isXML11Valid(ch))
/*     */         {
/* 492 */           if (length-- > 0)
/* 493 */             surrogates(ch, chars[(start++)]);
/*     */           else {
/* 495 */             fatalError("The character '" + ch + "' is an invalid XML character");
/*     */           }
/*     */ 
/*     */         }
/* 499 */         else if ((unescaped) && (XML11Char.isXML11ValidLiteral(ch)))
/* 500 */           this._printer.printText(ch);
/*     */         else {
/* 502 */           printXMLChar(ch);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 510 */     while (length-- > 0) {
/* 511 */       char ch = chars[(start++)];
/* 512 */       if (!XML11Char.isXML11Valid(ch))
/*     */       {
/* 514 */         if (length-- > 0)
/* 515 */           surrogates(ch, chars[(start++)]);
/*     */         else {
/* 517 */           fatalError("The character '" + ch + "' is an invalid XML character");
/*     */         }
/*     */ 
/*     */       }
/* 522 */       else if ((unescaped) && (XML11Char.isXML11ValidLiteral(ch)))
/* 523 */         this._printer.printText(ch);
/*     */       else
/* 525 */         printXMLChar(ch);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean reset()
/*     */   {
/* 532 */     super.reset();
/* 533 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.XML11Serializer
 * JD-Core Version:    0.6.2
 */