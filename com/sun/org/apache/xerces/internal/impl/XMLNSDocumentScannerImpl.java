/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidatorFilter;
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
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
/*     */ public class XMLNSDocumentScannerImpl extends XMLDocumentScannerImpl
/*     */ {
/*     */   protected boolean fBindNamespaces;
/*     */   protected boolean fPerformValidation;
/*     */   protected boolean fNotAddNSDeclAsAttribute;
/*     */   private XMLDTDValidatorFilter fDTDValidator;
/*     */   private boolean fXmlnsDeclared;
/*     */ 
/*     */   public XMLNSDocumentScannerImpl()
/*     */   {
/*  78 */     this.fNotAddNSDeclAsAttribute = false;
/*     */ 
/*  84 */     this.fXmlnsDeclared = false;
/*     */   }
/*     */ 
/*     */   public void reset(PropertyManager propertyManager)
/*     */   {
/*  89 */     setPropertyManager(propertyManager);
/*  90 */     super.reset(propertyManager);
/*  91 */     this.fBindNamespaces = false;
/*  92 */     this.fNotAddNSDeclAsAttribute = (!((Boolean)propertyManager.getProperty("add-namespacedecl-as-attrbiute")).booleanValue());
/*     */   }
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager) throws XMLConfigurationException
/*     */   {
/*  97 */     super.reset(componentManager);
/*  98 */     this.fNotAddNSDeclAsAttribute = false;
/*  99 */     this.fPerformValidation = false;
/* 100 */     this.fBindNamespaces = false;
/*     */   }
/*     */ 
/*     */   public int next()
/*     */     throws IOException, XNIException
/*     */   {
/* 112 */     if ((this.fScannerLastState == 2) && (this.fBindNamespaces)) {
/* 113 */       this.fScannerLastState = -1;
/* 114 */       this.fNamespaceContext.popContext();
/*     */     }
/*     */ 
/* 117 */     return this.fScannerLastState = super.next();
/*     */   }
/*     */ 
/*     */   public void setDTDValidator(XMLDTDValidatorFilter dtd)
/*     */   {
/* 130 */     this.fDTDValidator = dtd;
/*     */   }
/*     */ 
/*     */   protected boolean scanStartElement()
/*     */     throws IOException, XNIException
/*     */   {
/* 161 */     if ((this.fSkip) && (!this.fAdd))
/*     */     {
/* 165 */       QName name = this.fElementStack.getNext();
/*     */ 
/* 172 */       this.fSkip = this.fEntityScanner.skipString(name.rawname);
/*     */ 
/* 174 */       if (this.fSkip)
/*     */       {
/* 178 */         this.fElementStack.push();
/* 179 */         this.fElementQName = name;
/*     */       }
/*     */       else {
/* 182 */         this.fElementStack.reposition();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 192 */     if ((!this.fSkip) || (this.fAdd))
/*     */     {
/* 194 */       this.fElementQName = this.fElementStack.nextElement();
/*     */ 
/* 197 */       if (this.fNamespaces) {
/* 198 */         this.fEntityScanner.scanQName(this.fElementQName);
/*     */       } else {
/* 200 */         String name = this.fEntityScanner.scanName();
/* 201 */         this.fElementQName.setValues(null, name, name, null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 214 */     if (this.fAdd)
/*     */     {
/* 216 */       this.fElementStack.matchElement(this.fElementQName);
/*     */     }
/*     */ 
/* 220 */     this.fCurrentElement = this.fElementQName;
/*     */ 
/* 222 */     String rawname = this.fElementQName.rawname;
/* 223 */     checkDepth(rawname);
/* 224 */     if (this.fBindNamespaces) {
/* 225 */       this.fNamespaceContext.pushContext();
/* 226 */       if ((this.fScannerState == 26) && 
/* 227 */         (this.fPerformValidation)) {
/* 228 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[] { rawname }, (short)1);
/*     */ 
/* 233 */         if ((this.fDoctypeName == null) || (!this.fDoctypeName.equals(rawname))) {
/* 234 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[] { this.fDoctypeName, rawname }, (short)1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 244 */     this.fEmptyElement = false;
/* 245 */     this.fAttributes.removeAllAttributes();
/*     */ 
/* 247 */     if (!seekCloseOfStartTag()) {
/* 248 */       this.fReadingAttributes = true;
/* 249 */       this.fAttributeCacheUsedCount = 0;
/* 250 */       this.fStringBufferIndex = 0;
/* 251 */       this.fAddDefaultAttr = true;
/* 252 */       this.fXmlnsDeclared = false;
/*     */       do
/*     */       {
/* 255 */         scanAttribute(this.fAttributes);
/* 256 */         if ((this.fSecurityManager != null) && (!this.fSecurityManager.isNoLimit(this.fElementAttributeLimit)) && (this.fAttributes.getLength() > this.fElementAttributeLimit))
/*     */         {
/* 258 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ElementAttributeLimit", new Object[] { rawname, Integer.valueOf(this.fElementAttributeLimit) }, (short)2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 264 */       while (!seekCloseOfStartTag());
/* 265 */       this.fReadingAttributes = false;
/*     */     }
/*     */ 
/* 268 */     if (this.fBindNamespaces)
/*     */     {
/* 270 */       if (this.fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
/* 271 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { this.fElementQName.rawname }, (short)2);
/*     */       }
/*     */ 
/* 278 */       String prefix = this.fElementQName.prefix != null ? this.fElementQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 281 */       this.fElementQName.uri = this.fNamespaceContext.getURI(prefix);
/*     */ 
/* 283 */       this.fCurrentElement.uri = this.fElementQName.uri;
/*     */ 
/* 285 */       if ((this.fElementQName.prefix == null) && (this.fElementQName.uri != null)) {
/* 286 */         this.fElementQName.prefix = XMLSymbols.EMPTY_STRING;
/*     */       }
/* 288 */       if ((this.fElementQName.prefix != null) && (this.fElementQName.uri == null)) {
/* 289 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { this.fElementQName.prefix, this.fElementQName.rawname }, (short)2);
/*     */       }
/*     */ 
/* 296 */       int length = this.fAttributes.getLength();
/*     */ 
/* 298 */       for (int i = 0; i < length; i++) {
/* 299 */         this.fAttributes.getName(i, this.fAttributeQName);
/*     */ 
/* 301 */         String aprefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 303 */         String uri = this.fNamespaceContext.getURI(aprefix);
/*     */ 
/* 306 */         if ((this.fAttributeQName.uri == null) || (this.fAttributeQName.uri != uri))
/*     */         {
/* 310 */           if (aprefix != XMLSymbols.EMPTY_STRING) {
/* 311 */             this.fAttributeQName.uri = uri;
/* 312 */             if (uri == null) {
/* 313 */               this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { this.fElementQName.rawname, this.fAttributeQName.rawname, aprefix }, (short)2);
/*     */             }
/*     */ 
/* 318 */             this.fAttributes.setURI(i, uri);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 323 */       if (length > 1) {
/* 324 */         QName name = this.fAttributes.checkDuplicatesNS();
/* 325 */         if (name != null) {
/* 326 */           if (name.uri != null) {
/* 327 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { this.fElementQName.rawname, name.localpart, name.uri }, (short)2);
/*     */           }
/*     */           else
/*     */           {
/* 332 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[] { this.fElementQName.rawname, name.rawname }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 342 */     if (this.fEmptyElement)
/*     */     {
/* 344 */       this.fMarkupDepth -= 1;
/*     */ 
/* 347 */       if (this.fMarkupDepth < this.fEntityStack[(this.fEntityDepth - 1)]) {
/* 348 */         reportFatalError("ElementEntityMismatch", new Object[] { this.fCurrentElement.rawname });
/*     */       }
/*     */ 
/* 352 */       if (this.fDocumentHandler != null)
/*     */       {
/* 356 */         this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
/*     */       }
/*     */ 
/* 361 */       this.fScanEndElement = true;
/*     */ 
/* 367 */       this.fElementStack.popElement();
/*     */     }
/*     */     else
/*     */     {
/* 371 */       if (this.dtdGrammarUtil != null)
/* 372 */         this.dtdGrammarUtil.startElement(this.fElementQName, this.fAttributes);
/* 373 */       if (this.fDocumentHandler != null)
/*     */       {
/* 379 */         this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 385 */     return this.fEmptyElement;
/*     */   }
/*     */ 
/*     */   protected void scanAttribute(XMLAttributesImpl attributes)
/*     */     throws IOException, XNIException
/*     */   {
/* 413 */     this.fEntityScanner.scanQName(this.fAttributeQName);
/*     */ 
/* 416 */     this.fEntityScanner.skipSpaces();
/* 417 */     if (!this.fEntityScanner.skipChar(61)) {
/* 418 */       reportFatalError("EqRequiredInAttribute", new Object[] { this.fCurrentElement.rawname, this.fAttributeQName.rawname });
/*     */     }
/*     */ 
/* 421 */     this.fEntityScanner.skipSpaces();
/*     */ 
/* 424 */     int attrIndex = 0;
/*     */ 
/* 428 */     boolean isVC = (this.fHasExternalDTD) && (!this.fStandalone);
/*     */ 
/* 438 */     XMLString tmpStr = getString();
/* 439 */     scanAttributeValue(tmpStr, this.fTempString2, this.fAttributeQName.rawname, attributes, attrIndex, isVC);
/*     */ 
/* 443 */     String value = null;
/*     */ 
/* 447 */     if (this.fBindNamespaces)
/*     */     {
/* 449 */       String localpart = this.fAttributeQName.localpart;
/* 450 */       String prefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 454 */       if ((prefix == XMLSymbols.PREFIX_XMLNS) || ((prefix == XMLSymbols.EMPTY_STRING) && (localpart == XMLSymbols.PREFIX_XMLNS)))
/*     */       {
/* 458 */         String uri = this.fSymbolTable.addSymbol(tmpStr.ch, tmpStr.offset, tmpStr.length);
/* 459 */         value = uri;
/*     */ 
/* 461 */         if ((prefix == XMLSymbols.PREFIX_XMLNS) && (localpart == XMLSymbols.PREFIX_XMLNS)) {
/* 462 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { this.fAttributeQName }, (short)2);
/*     */         }
/*     */ 
/* 469 */         if (uri == NamespaceContext.XMLNS_URI) {
/* 470 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { this.fAttributeQName }, (short)2);
/*     */         }
/*     */ 
/* 477 */         if (localpart == XMLSymbols.PREFIX_XML) {
/* 478 */           if (uri != NamespaceContext.XML_URI) {
/* 479 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { this.fAttributeQName }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/* 487 */         else if (uri == NamespaceContext.XML_URI) {
/* 488 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { this.fAttributeQName }, (short)2);
/*     */         }
/*     */ 
/* 494 */         prefix = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 498 */         if ((prefix == XMLSymbols.EMPTY_STRING) && (localpart == XMLSymbols.PREFIX_XMLNS)) {
/* 499 */           this.fAttributeQName.prefix = XMLSymbols.PREFIX_XMLNS;
/*     */         }
/*     */ 
/* 504 */         if ((uri == XMLSymbols.EMPTY_STRING) && (localpart != XMLSymbols.PREFIX_XMLNS)) {
/* 505 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "EmptyPrefixedAttName", new Object[] { this.fAttributeQName }, (short)2);
/*     */         }
/*     */ 
/* 512 */         if (((NamespaceSupport)this.fNamespaceContext).containsPrefixInCurrentContext(prefix)) {
/* 513 */           reportFatalError("AttributeNotUnique", new Object[] { this.fCurrentElement.rawname, this.fAttributeQName.rawname });
/*     */         }
/*     */ 
/* 519 */         boolean declared = this.fNamespaceContext.declarePrefix(prefix, uri.length() != 0 ? uri : null);
/*     */ 
/* 522 */         if (!declared)
/*     */         {
/* 524 */           if (this.fXmlnsDeclared) {
/* 525 */             reportFatalError("AttributeNotUnique", new Object[] { this.fCurrentElement.rawname, this.fAttributeQName.rawname });
/*     */           }
/*     */ 
/* 531 */           this.fXmlnsDeclared = true;
/*     */         }
/*     */ 
/* 539 */         if (this.fNotAddNSDeclAsAttribute) {
/* 540 */           return;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 546 */     if (this.fBindNamespaces) {
/* 547 */       attrIndex = attributes.getLength();
/* 548 */       attributes.addAttributeNS(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
/*     */     } else {
/* 550 */       int oldLen = attributes.getLength();
/* 551 */       attrIndex = attributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
/*     */ 
/* 554 */       if (oldLen == attributes.getLength()) {
/* 555 */         reportFatalError("AttributeNotUnique", new Object[] { this.fCurrentElement.rawname, this.fAttributeQName.rawname });
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 561 */     attributes.setValue(attrIndex, value, tmpStr);
/*     */ 
/* 564 */     attributes.setSpecified(attrIndex, true);
/*     */ 
/* 567 */     if (this.fAttributeQName.prefix != null)
/* 568 */       attributes.setURI(attrIndex, this.fNamespaceContext.getURI(this.fAttributeQName.prefix));
/*     */   }
/*     */ 
/*     */   protected XMLDocumentFragmentScannerImpl.Driver createContentDriver()
/*     */   {
/* 580 */     return new NSContentDriver();
/*     */   }
/*     */ 
/*     */   protected final class NSContentDriver extends XMLDocumentScannerImpl.ContentDriver
/*     */   {
/*     */     protected NSContentDriver() {
/* 586 */       super();
/*     */     }
/*     */ 
/*     */     protected boolean scanRootElementHook()
/*     */       throws IOException, XNIException
/*     */     {
/* 604 */       reconfigurePipeline();
/* 605 */       if (XMLNSDocumentScannerImpl.this.scanStartElement()) {
/* 606 */         XMLNSDocumentScannerImpl.this.setScannerState(44);
/* 607 */         XMLNSDocumentScannerImpl.this.setDriver(XMLNSDocumentScannerImpl.this.fTrailingMiscDriver);
/* 608 */         return true;
/*     */       }
/* 610 */       return false;
/*     */     }
/*     */ 
/*     */     private void reconfigurePipeline()
/*     */     {
/* 622 */       if ((XMLNSDocumentScannerImpl.this.fNamespaces) && (XMLNSDocumentScannerImpl.this.fDTDValidator == null)) {
/* 623 */         XMLNSDocumentScannerImpl.this.fBindNamespaces = true;
/*     */       }
/* 625 */       else if ((XMLNSDocumentScannerImpl.this.fNamespaces) && (!XMLNSDocumentScannerImpl.this.fDTDValidator.hasGrammar())) {
/* 626 */         XMLNSDocumentScannerImpl.this.fBindNamespaces = true;
/* 627 */         XMLNSDocumentScannerImpl.this.fPerformValidation = XMLNSDocumentScannerImpl.this.fDTDValidator.validate();
/*     */ 
/* 629 */         XMLDocumentSource source = XMLNSDocumentScannerImpl.this.fDTDValidator.getDocumentSource();
/* 630 */         XMLDocumentHandler handler = XMLNSDocumentScannerImpl.this.fDTDValidator.getDocumentHandler();
/* 631 */         source.setDocumentHandler(handler);
/* 632 */         if (handler != null)
/* 633 */           handler.setDocumentSource(source);
/* 634 */         XMLNSDocumentScannerImpl.this.fDTDValidator.setDocumentSource(null);
/* 635 */         XMLNSDocumentScannerImpl.this.fDTDValidator.setDocumentHandler(null);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl
 * JD-Core Version:    0.6.2
 */