/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidatorFilter;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLAttributesIteratorImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ import com.sun.xml.internal.stream.dtd.DTDGrammarUtil;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class XML11NSDocumentScannerImpl extends XML11DocumentScannerImpl
/*     */ {
/*     */   protected boolean fBindNamespaces;
/*     */   protected boolean fPerformValidation;
/*     */   private XMLDTDValidatorFilter fDTDValidator;
/*     */   private boolean fSawSpace;
/*     */ 
/*     */   public void setDTDValidator(XMLDTDValidatorFilter validator)
/*     */   {
/* 150 */     this.fDTDValidator = validator;
/*     */   }
/*     */ 
/*     */   protected boolean scanStartElement()
/*     */     throws IOException, XNIException
/*     */   {
/* 179 */     this.fEntityScanner.scanQName(this.fElementQName);
/*     */ 
/* 181 */     String rawname = this.fElementQName.rawname;
/* 182 */     if (this.fBindNamespaces) {
/* 183 */       this.fNamespaceContext.pushContext();
/* 184 */       if ((this.fScannerState == 26) && 
/* 185 */         (this.fPerformValidation)) {
/* 186 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[] { rawname }, (short)1);
/*     */ 
/* 192 */         if ((this.fDoctypeName == null) || (!this.fDoctypeName.equals(rawname)))
/*     */         {
/* 194 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[] { this.fDoctypeName, rawname }, (short)1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 205 */     this.fCurrentElement = this.fElementStack.pushElement(this.fElementQName);
/*     */ 
/* 208 */     boolean empty = false;
/* 209 */     this.fAttributes.removeAllAttributes();
/*     */     while (true)
/*     */     {
/* 212 */       boolean sawSpace = this.fEntityScanner.skipSpaces();
/*     */ 
/* 215 */       int c = this.fEntityScanner.peekChar();
/* 216 */       if (c == 62) {
/* 217 */         this.fEntityScanner.scanChar();
/* 218 */         break;
/* 219 */       }if (c == 47) {
/* 220 */         this.fEntityScanner.scanChar();
/* 221 */         if (!this.fEntityScanner.skipChar(62)) {
/* 222 */           reportFatalError("ElementUnterminated", new Object[] { rawname });
/*     */         }
/*     */ 
/* 226 */         empty = true;
/* 227 */         break;
/* 228 */       }if ((!isValidNameStartChar(c)) || (!sawSpace))
/*     */       {
/* 231 */         if ((!isValidNameStartHighSurrogate(c)) || (!sawSpace)) {
/* 232 */           reportFatalError("ElementUnterminated", new Object[] { rawname });
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 239 */       scanAttribute(this.fAttributes);
/* 240 */       if ((this.fSecurityManager != null) && (!this.fSecurityManager.isNoLimit(this.fElementAttributeLimit)) && (this.fAttributes.getLength() > this.fElementAttributeLimit))
/*     */       {
/* 242 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ElementAttributeLimit", new Object[] { rawname, new Integer(this.fElementAttributeLimit) }, (short)2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 250 */     if (this.fBindNamespaces)
/*     */     {
/* 252 */       if (this.fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
/* 253 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { this.fElementQName.rawname }, (short)2);
/*     */       }
/*     */ 
/* 261 */       String prefix = this.fElementQName.prefix != null ? this.fElementQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 266 */       this.fElementQName.uri = this.fNamespaceContext.getURI(prefix);
/*     */ 
/* 268 */       this.fCurrentElement.uri = this.fElementQName.uri;
/*     */ 
/* 270 */       if ((this.fElementQName.prefix == null) && (this.fElementQName.uri != null)) {
/* 271 */         this.fElementQName.prefix = XMLSymbols.EMPTY_STRING;
/*     */ 
/* 273 */         this.fCurrentElement.prefix = XMLSymbols.EMPTY_STRING;
/*     */       }
/* 275 */       if ((this.fElementQName.prefix != null) && (this.fElementQName.uri == null)) {
/* 276 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { this.fElementQName.prefix, this.fElementQName.rawname }, (short)2);
/*     */       }
/*     */ 
/* 286 */       int length = this.fAttributes.getLength();
/* 287 */       for (int i = 0; i < length; i++) {
/* 288 */         this.fAttributes.getName(i, this.fAttributeQName);
/*     */ 
/* 290 */         String aprefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 294 */         String uri = this.fNamespaceContext.getURI(aprefix);
/*     */ 
/* 297 */         if ((this.fAttributeQName.uri == null) || (this.fAttributeQName.uri != uri))
/*     */         {
/* 301 */           if (aprefix != XMLSymbols.EMPTY_STRING) {
/* 302 */             this.fAttributeQName.uri = uri;
/* 303 */             if (uri == null) {
/* 304 */               this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { this.fElementQName.rawname, this.fAttributeQName.rawname, aprefix }, (short)2);
/*     */             }
/*     */ 
/* 313 */             this.fAttributes.setURI(i, uri);
/*     */           }
/*     */         }
/*     */       }
/* 317 */       if (length > 1) {
/* 318 */         QName name = this.fAttributes.checkDuplicatesNS();
/* 319 */         if (name != null) {
/* 320 */           if (name.uri != null) {
/* 321 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { this.fElementQName.rawname, name.localpart, name.uri }, (short)2);
/*     */           }
/*     */           else
/*     */           {
/* 330 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[] { this.fElementQName.rawname, name.rawname }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 344 */     if (empty)
/*     */     {
/* 347 */       this.fMarkupDepth -= 1;
/*     */ 
/* 350 */       if (this.fMarkupDepth < this.fEntityStack[(this.fEntityDepth - 1)]) {
/* 351 */         reportFatalError("ElementEntityMismatch", new Object[] { this.fCurrentElement.rawname });
/*     */       }
/*     */ 
/* 356 */       this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
/*     */ 
/* 361 */       this.fScanEndElement = true;
/*     */ 
/* 364 */       this.fElementStack.popElement();
/*     */     }
/*     */     else {
/* 367 */       if (this.dtdGrammarUtil != null) {
/* 368 */         this.dtdGrammarUtil.startElement(this.fElementQName, this.fAttributes);
/*     */       }
/* 370 */       if (this.fDocumentHandler != null) {
/* 371 */         this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 376 */     return empty;
/*     */   }
/*     */ 
/*     */   protected void scanStartElementName()
/*     */     throws IOException, XNIException
/*     */   {
/* 388 */     this.fEntityScanner.scanQName(this.fElementQName);
/*     */ 
/* 391 */     this.fSawSpace = this.fEntityScanner.skipSpaces();
/*     */   }
/*     */ 
/*     */   protected boolean scanStartElementAfterName()
/*     */     throws IOException, XNIException
/*     */   {
/* 404 */     String rawname = this.fElementQName.rawname;
/* 405 */     if (this.fBindNamespaces) {
/* 406 */       this.fNamespaceContext.pushContext();
/* 407 */       if ((this.fScannerState == 26) && 
/* 408 */         (this.fPerformValidation)) {
/* 409 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[] { rawname }, (short)1);
/*     */ 
/* 415 */         if ((this.fDoctypeName == null) || (!this.fDoctypeName.equals(rawname)))
/*     */         {
/* 417 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[] { this.fDoctypeName, rawname }, (short)1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 428 */     this.fCurrentElement = this.fElementStack.pushElement(this.fElementQName);
/*     */ 
/* 431 */     boolean empty = false;
/* 432 */     this.fAttributes.removeAllAttributes();
/*     */     while (true)
/*     */     {
/* 436 */       int c = this.fEntityScanner.peekChar();
/* 437 */       if (c == 62) {
/* 438 */         this.fEntityScanner.scanChar();
/* 439 */         break;
/* 440 */       }if (c == 47) {
/* 441 */         this.fEntityScanner.scanChar();
/* 442 */         if (!this.fEntityScanner.skipChar(62)) {
/* 443 */           reportFatalError("ElementUnterminated", new Object[] { rawname });
/*     */         }
/*     */ 
/* 447 */         empty = true;
/* 448 */         break;
/* 449 */       }if ((!isValidNameStartChar(c)) || (!this.fSawSpace))
/*     */       {
/* 452 */         if ((!isValidNameStartHighSurrogate(c)) || (!this.fSawSpace)) {
/* 453 */           reportFatalError("ElementUnterminated", new Object[] { rawname });
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 460 */       scanAttribute(this.fAttributes);
/*     */ 
/* 463 */       this.fSawSpace = this.fEntityScanner.skipSpaces();
/*     */     }
/*     */ 
/* 467 */     if (this.fBindNamespaces)
/*     */     {
/* 469 */       if (this.fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
/* 470 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { this.fElementQName.rawname }, (short)2);
/*     */       }
/*     */ 
/* 478 */       String prefix = this.fElementQName.prefix != null ? this.fElementQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 483 */       this.fElementQName.uri = this.fNamespaceContext.getURI(prefix);
/*     */ 
/* 485 */       this.fCurrentElement.uri = this.fElementQName.uri;
/*     */ 
/* 487 */       if ((this.fElementQName.prefix == null) && (this.fElementQName.uri != null)) {
/* 488 */         this.fElementQName.prefix = XMLSymbols.EMPTY_STRING;
/*     */ 
/* 490 */         this.fCurrentElement.prefix = XMLSymbols.EMPTY_STRING;
/*     */       }
/* 492 */       if ((this.fElementQName.prefix != null) && (this.fElementQName.uri == null)) {
/* 493 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { this.fElementQName.prefix, this.fElementQName.rawname }, (short)2);
/*     */       }
/*     */ 
/* 503 */       int length = this.fAttributes.getLength();
/* 504 */       for (int i = 0; i < length; i++) {
/* 505 */         this.fAttributes.getName(i, this.fAttributeQName);
/*     */ 
/* 507 */         String aprefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 511 */         String uri = this.fNamespaceContext.getURI(aprefix);
/*     */ 
/* 514 */         if ((this.fAttributeQName.uri == null) || (this.fAttributeQName.uri != uri))
/*     */         {
/* 518 */           if (aprefix != XMLSymbols.EMPTY_STRING) {
/* 519 */             this.fAttributeQName.uri = uri;
/* 520 */             if (uri == null) {
/* 521 */               this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { this.fElementQName.rawname, this.fAttributeQName.rawname, aprefix }, (short)2);
/*     */             }
/*     */ 
/* 530 */             this.fAttributes.setURI(i, uri);
/*     */           }
/*     */         }
/*     */       }
/* 534 */       if (length > 1) {
/* 535 */         QName name = this.fAttributes.checkDuplicatesNS();
/* 536 */         if (name != null) {
/* 537 */           if (name.uri != null) {
/* 538 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { this.fElementQName.rawname, name.localpart, name.uri }, (short)2);
/*     */           }
/*     */           else
/*     */           {
/* 547 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[] { this.fElementQName.rawname, name.rawname }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 560 */     if (this.fDocumentHandler != null) {
/* 561 */       if (empty)
/*     */       {
/* 564 */         this.fMarkupDepth -= 1;
/*     */ 
/* 567 */         if (this.fMarkupDepth < this.fEntityStack[(this.fEntityDepth - 1)]) {
/* 568 */           reportFatalError("ElementEntityMismatch", new Object[] { this.fCurrentElement.rawname });
/*     */         }
/*     */ 
/* 573 */         this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
/*     */ 
/* 575 */         if (this.fBindNamespaces) {
/* 576 */           this.fNamespaceContext.popContext();
/*     */         }
/*     */ 
/* 579 */         this.fElementStack.popElement();
/*     */       } else {
/* 581 */         this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 587 */     return empty;
/*     */   }
/*     */ 
/*     */   protected void scanAttribute(XMLAttributesImpl attributes)
/*     */     throws IOException, XNIException
/*     */   {
/* 614 */     this.fEntityScanner.scanQName(this.fAttributeQName);
/*     */ 
/* 617 */     this.fEntityScanner.skipSpaces();
/* 618 */     if (!this.fEntityScanner.skipChar(61)) {
/* 619 */       reportFatalError("EqRequiredInAttribute", new Object[] { this.fCurrentElement.rawname, this.fAttributeQName.rawname });
/*     */     }
/*     */ 
/* 625 */     this.fEntityScanner.skipSpaces();
/*     */     int attrIndex;
/* 630 */     if (this.fBindNamespaces) {
/* 631 */       int attrIndex = attributes.getLength();
/* 632 */       attributes.addAttributeNS(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
/*     */     }
/*     */     else
/*     */     {
/* 637 */       int oldLen = attributes.getLength();
/* 638 */       attrIndex = attributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
/*     */ 
/* 645 */       if (oldLen == attributes.getLength()) {
/* 646 */         reportFatalError("AttributeNotUnique", new Object[] { this.fCurrentElement.rawname, this.fAttributeQName.rawname });
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 655 */     boolean isVC = (this.fHasExternalDTD) && (!this.fStandalone);
/*     */ 
/* 658 */     scanAttributeValue(this.fTempString, this.fTempString2, this.fAttributeQName.rawname, isVC, this.fCurrentElement.rawname);
/*     */ 
/* 664 */     String value = this.fTempString.toString();
/* 665 */     attributes.setValue(attrIndex, value);
/* 666 */     attributes.setNonNormalizedValue(attrIndex, this.fTempString2.toString());
/* 667 */     attributes.setSpecified(attrIndex, true);
/*     */ 
/* 670 */     if (this.fBindNamespaces)
/*     */     {
/* 672 */       String localpart = this.fAttributeQName.localpart;
/* 673 */       String prefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 679 */       if ((prefix == XMLSymbols.PREFIX_XMLNS) || ((prefix == XMLSymbols.EMPTY_STRING) && (localpart == XMLSymbols.PREFIX_XMLNS)))
/*     */       {
/* 684 */         String uri = this.fSymbolTable.addSymbol(value);
/*     */ 
/* 687 */         if ((prefix == XMLSymbols.PREFIX_XMLNS) && (localpart == XMLSymbols.PREFIX_XMLNS))
/*     */         {
/* 689 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { this.fAttributeQName }, (short)2);
/*     */         }
/*     */ 
/* 697 */         if (uri == NamespaceContext.XMLNS_URI) {
/* 698 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { this.fAttributeQName }, (short)2);
/*     */         }
/*     */ 
/* 706 */         if (localpart == XMLSymbols.PREFIX_XML) {
/* 707 */           if (uri != NamespaceContext.XML_URI) {
/* 708 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { this.fAttributeQName }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/* 717 */         else if (uri == NamespaceContext.XML_URI) {
/* 718 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { this.fAttributeQName }, (short)2);
/*     */         }
/*     */ 
/* 726 */         prefix = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 734 */         this.fNamespaceContext.declarePrefix(prefix, uri.length() != 0 ? uri : null);
/*     */ 
/* 738 */         attributes.setURI(attrIndex, this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS));
/*     */       }
/* 744 */       else if (this.fAttributeQName.prefix != null) {
/* 745 */         attributes.setURI(attrIndex, this.fNamespaceContext.getURI(this.fAttributeQName.prefix));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int scanEndElement()
/*     */     throws IOException, XNIException
/*     */   {
/* 775 */     QName endElementName = this.fElementStack.popElement();
/*     */ 
/* 787 */     if (!this.fEntityScanner.skipString(endElementName.rawname)) {
/* 788 */       reportFatalError("ETagRequired", new Object[] { endElementName.rawname });
/*     */     }
/*     */ 
/* 794 */     this.fEntityScanner.skipSpaces();
/* 795 */     if (!this.fEntityScanner.skipChar(62)) {
/* 796 */       reportFatalError("ETagUnterminated", new Object[] { endElementName.rawname });
/*     */     }
/*     */ 
/* 800 */     this.fMarkupDepth -= 1;
/*     */ 
/* 803 */     this.fMarkupDepth -= 1;
/*     */ 
/* 806 */     if (this.fMarkupDepth < this.fEntityStack[(this.fEntityDepth - 1)]) {
/* 807 */       reportFatalError("ElementEntityMismatch", new Object[] { endElementName.rawname });
/*     */     }
/*     */ 
/* 813 */     if (this.fDocumentHandler != null) {
/* 814 */       this.fDocumentHandler.endElement(endElementName, null);
/*     */     }
/*     */ 
/* 822 */     if (this.dtdGrammarUtil != null) {
/* 823 */       this.dtdGrammarUtil.endElement(endElementName);
/*     */     }
/* 825 */     return this.fMarkupDepth;
/*     */   }
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager)
/*     */     throws XMLConfigurationException
/*     */   {
/* 832 */     super.reset(componentManager);
/* 833 */     this.fPerformValidation = false;
/* 834 */     this.fBindNamespaces = false;
/*     */   }
/*     */ 
/*     */   protected XMLDocumentFragmentScannerImpl.Driver createContentDriver()
/*     */   {
/* 839 */     return new NS11ContentDriver();
/*     */   }
/*     */ 
/*     */   public int next()
/*     */     throws IOException, XNIException
/*     */   {
/* 852 */     if ((this.fScannerLastState == 2) && (this.fBindNamespaces)) {
/* 853 */       this.fScannerLastState = -1;
/* 854 */       this.fNamespaceContext.popContext();
/*     */     }
/*     */ 
/* 857 */     return this.fScannerLastState = super.next();
/*     */   }
/*     */ 
/*     */   protected final class NS11ContentDriver extends XMLDocumentScannerImpl.ContentDriver
/*     */   {
/*     */     protected NS11ContentDriver()
/*     */     {
/* 864 */       super();
/*     */     }
/*     */ 
/*     */     protected boolean scanRootElementHook()
/*     */       throws IOException, XNIException
/*     */     {
/* 881 */       if ((XML11NSDocumentScannerImpl.this.fExternalSubsetResolver != null) && (!XML11NSDocumentScannerImpl.this.fSeenDoctypeDecl) && (!XML11NSDocumentScannerImpl.this.fDisallowDoctype) && ((XML11NSDocumentScannerImpl.this.fValidation) || (XML11NSDocumentScannerImpl.this.fLoadExternalDTD)))
/*     */       {
/* 883 */         XML11NSDocumentScannerImpl.this.scanStartElementName();
/* 884 */         resolveExternalSubsetAndRead();
/* 885 */         reconfigurePipeline();
/* 886 */         if (XML11NSDocumentScannerImpl.this.scanStartElementAfterName()) {
/* 887 */           XML11NSDocumentScannerImpl.this.setScannerState(44);
/* 888 */           XML11NSDocumentScannerImpl.this.setDriver(XML11NSDocumentScannerImpl.this.fTrailingMiscDriver);
/* 889 */           return true;
/*     */         }
/*     */       }
/*     */       else {
/* 893 */         reconfigurePipeline();
/* 894 */         if (XML11NSDocumentScannerImpl.this.scanStartElement()) {
/* 895 */           XML11NSDocumentScannerImpl.this.setScannerState(44);
/* 896 */           XML11NSDocumentScannerImpl.this.setDriver(XML11NSDocumentScannerImpl.this.fTrailingMiscDriver);
/* 897 */           return true;
/*     */         }
/*     */       }
/* 900 */       return false;
/*     */     }
/*     */ 
/*     */     private void reconfigurePipeline()
/*     */     {
/* 911 */       if (XML11NSDocumentScannerImpl.this.fDTDValidator == null) {
/* 912 */         XML11NSDocumentScannerImpl.this.fBindNamespaces = true;
/*     */       }
/* 914 */       else if (!XML11NSDocumentScannerImpl.this.fDTDValidator.hasGrammar()) {
/* 915 */         XML11NSDocumentScannerImpl.this.fBindNamespaces = true;
/* 916 */         XML11NSDocumentScannerImpl.this.fPerformValidation = XML11NSDocumentScannerImpl.this.fDTDValidator.validate();
/*     */ 
/* 918 */         XMLDocumentSource source = XML11NSDocumentScannerImpl.this.fDTDValidator.getDocumentSource();
/* 919 */         XMLDocumentHandler handler = XML11NSDocumentScannerImpl.this.fDTDValidator.getDocumentHandler();
/* 920 */         source.setDocumentHandler(handler);
/* 921 */         if (handler != null)
/* 922 */           handler.setDocumentSource(source);
/* 923 */         XML11NSDocumentScannerImpl.this.fDTDValidator.setDocumentSource(null);
/* 924 */         XML11NSDocumentScannerImpl.this.fDTDValidator.setDocumentHandler(null);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XML11NSDocumentScannerImpl
 * JD-Core Version:    0.6.2
 */