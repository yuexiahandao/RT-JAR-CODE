/*     */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOMParser;
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.util.SAXLocatorWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ final class SchemaContentHandler
/*     */   implements ContentHandler
/*     */ {
/*     */   private SymbolTable fSymbolTable;
/*     */   private SchemaDOMParser fSchemaDOMParser;
/*  61 */   private final SAXLocatorWrapper fSAXLocatorWrapper = new SAXLocatorWrapper();
/*     */ 
/*  64 */   private NamespaceSupport fNamespaceContext = new NamespaceSupport();
/*     */   private boolean fNeedPushNSContext;
/*  70 */   private boolean fNamespacePrefixes = false;
/*     */ 
/*  73 */   private boolean fStringsInternalized = false;
/*     */ 
/*  76 */   private final QName fElementQName = new QName();
/*  77 */   private final QName fAttributeQName = new QName();
/*  78 */   private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
/*  79 */   private final XMLString fTempString = new XMLString();
/*     */ 
/*     */   public Document getDocument()
/*     */   {
/*  90 */     return this.fSchemaDOMParser.getDocument();
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/*  97 */     this.fSAXLocatorWrapper.setLocator(locator);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 104 */     this.fNeedPushNSContext = true;
/*     */     try {
/* 106 */       this.fSchemaDOMParser.startDocument(this.fSAXLocatorWrapper, null, this.fNamespaceContext, null);
/*     */     }
/*     */     catch (XMLParseException e) {
/* 109 */       convertToSAXParseException(e);
/*     */     }
/*     */     catch (XNIException e) {
/* 112 */       convertToSAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 120 */     this.fSAXLocatorWrapper.setLocator(null);
/*     */     try {
/* 122 */       this.fSchemaDOMParser.endDocument(null);
/*     */     }
/*     */     catch (XMLParseException e) {
/* 125 */       convertToSAXParseException(e);
/*     */     }
/*     */     catch (XNIException e) {
/* 128 */       convertToSAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 136 */     if (this.fNeedPushNSContext) {
/* 137 */       this.fNeedPushNSContext = false;
/* 138 */       this.fNamespaceContext.pushContext();
/*     */     }
/* 140 */     if (!this.fStringsInternalized) {
/* 141 */       prefix = prefix != null ? this.fSymbolTable.addSymbol(prefix) : XMLSymbols.EMPTY_STRING;
/* 142 */       uri = (uri != null) && (uri.length() > 0) ? this.fSymbolTable.addSymbol(uri) : null;
/*     */     }
/*     */     else {
/* 145 */       if (prefix == null) {
/* 146 */         prefix = XMLSymbols.EMPTY_STRING;
/*     */       }
/* 148 */       if ((uri != null) && (uri.length() == 0)) {
/* 149 */         uri = null;
/*     */       }
/*     */     }
/* 152 */     this.fNamespaceContext.declarePrefix(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 166 */     if (this.fNeedPushNSContext) {
/* 167 */       this.fNamespaceContext.pushContext();
/*     */     }
/* 169 */     this.fNeedPushNSContext = true;
/*     */ 
/* 172 */     fillQName(this.fElementQName, uri, localName, qName);
/* 173 */     fillXMLAttributes(atts);
/*     */ 
/* 176 */     if (!this.fNamespacePrefixes) {
/* 177 */       int prefixCount = this.fNamespaceContext.getDeclaredPrefixCount();
/* 178 */       if (prefixCount > 0) {
/* 179 */         addNamespaceDeclarations(prefixCount);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 184 */       this.fSchemaDOMParser.startElement(this.fElementQName, this.fAttributes, null);
/*     */     }
/*     */     catch (XMLParseException e) {
/* 187 */       convertToSAXParseException(e);
/*     */     }
/*     */     catch (XNIException e) {
/* 190 */       convertToSAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 198 */     fillQName(this.fElementQName, uri, localName, qName);
/*     */     try {
/* 200 */       this.fSchemaDOMParser.endElement(this.fElementQName, null);
/*     */     }
/*     */     catch (XMLParseException e) {
/* 203 */       convertToSAXParseException(e);
/*     */     }
/*     */     catch (XNIException e) {
/* 206 */       convertToSAXException(e);
/*     */     }
/*     */     finally {
/* 209 */       this.fNamespaceContext.popContext();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 218 */       this.fTempString.setValues(ch, start, length);
/* 219 */       this.fSchemaDOMParser.characters(this.fTempString, null);
/*     */     }
/*     */     catch (XMLParseException e) {
/* 222 */       convertToSAXParseException(e);
/*     */     }
/*     */     catch (XNIException e) {
/* 225 */       convertToSAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 234 */       this.fTempString.setValues(ch, start, length);
/* 235 */       this.fSchemaDOMParser.ignorableWhitespace(this.fTempString, null);
/*     */     }
/*     */     catch (XMLParseException e) {
/* 238 */       convertToSAXParseException(e);
/*     */     }
/*     */     catch (XNIException e) {
/* 241 */       convertToSAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 250 */       this.fTempString.setValues(data.toCharArray(), 0, data.length());
/* 251 */       this.fSchemaDOMParser.processingInstruction(target, this.fTempString, null);
/*     */     }
/*     */     catch (XMLParseException e) {
/* 254 */       convertToSAXParseException(e);
/*     */     }
/*     */     catch (XNIException e) {
/* 257 */       convertToSAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String arg)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   private void fillQName(QName toFill, String uri, String localpart, String rawname)
/*     */   {
/* 273 */     if (!this.fStringsInternalized) {
/* 274 */       uri = (uri != null) && (uri.length() > 0) ? this.fSymbolTable.addSymbol(uri) : null;
/* 275 */       localpart = localpart != null ? this.fSymbolTable.addSymbol(localpart) : XMLSymbols.EMPTY_STRING;
/* 276 */       rawname = rawname != null ? this.fSymbolTable.addSymbol(rawname) : XMLSymbols.EMPTY_STRING;
/*     */     }
/*     */     else {
/* 279 */       if ((uri != null) && (uri.length() == 0)) {
/* 280 */         uri = null;
/*     */       }
/* 282 */       if (localpart == null) {
/* 283 */         localpart = XMLSymbols.EMPTY_STRING;
/*     */       }
/* 285 */       if (rawname == null) {
/* 286 */         rawname = XMLSymbols.EMPTY_STRING;
/*     */       }
/*     */     }
/* 289 */     String prefix = XMLSymbols.EMPTY_STRING;
/* 290 */     int prefixIdx = rawname.indexOf(':');
/* 291 */     if (prefixIdx != -1) {
/* 292 */       prefix = this.fSymbolTable.addSymbol(rawname.substring(0, prefixIdx));
/*     */ 
/* 294 */       if (localpart == XMLSymbols.EMPTY_STRING) {
/* 295 */         localpart = this.fSymbolTable.addSymbol(rawname.substring(prefixIdx + 1));
/*     */       }
/*     */ 
/*     */     }
/* 299 */     else if (localpart == XMLSymbols.EMPTY_STRING) {
/* 300 */       localpart = rawname;
/*     */     }
/* 302 */     toFill.setValues(prefix, localpart, rawname, uri);
/*     */   }
/*     */ 
/*     */   private void fillXMLAttributes(Attributes atts) {
/* 306 */     this.fAttributes.removeAllAttributes();
/* 307 */     int attrCount = atts.getLength();
/* 308 */     for (int i = 0; i < attrCount; i++) {
/* 309 */       fillQName(this.fAttributeQName, atts.getURI(i), atts.getLocalName(i), atts.getQName(i));
/* 310 */       String type = atts.getType(i);
/* 311 */       this.fAttributes.addAttributeNS(this.fAttributeQName, type != null ? type : XMLSymbols.fCDATASymbol, atts.getValue(i));
/* 312 */       this.fAttributes.setSpecified(i, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addNamespaceDeclarations(int prefixCount) {
/* 317 */     String prefix = null;
/* 318 */     String localpart = null;
/* 319 */     String rawname = null;
/* 320 */     String nsPrefix = null;
/* 321 */     String nsURI = null;
/* 322 */     for (int i = 0; i < prefixCount; i++) {
/* 323 */       nsPrefix = this.fNamespaceContext.getDeclaredPrefixAt(i);
/* 324 */       nsURI = this.fNamespaceContext.getURI(nsPrefix);
/* 325 */       if (nsPrefix.length() > 0) {
/* 326 */         prefix = XMLSymbols.PREFIX_XMLNS;
/* 327 */         localpart = nsPrefix;
/* 328 */         rawname = this.fSymbolTable.addSymbol(prefix + ":" + localpart);
/*     */       }
/*     */       else {
/* 331 */         prefix = XMLSymbols.EMPTY_STRING;
/* 332 */         localpart = XMLSymbols.PREFIX_XMLNS;
/* 333 */         rawname = XMLSymbols.PREFIX_XMLNS;
/*     */       }
/* 335 */       this.fAttributeQName.setValues(prefix, localpart, rawname, NamespaceContext.XMLNS_URI);
/* 336 */       this.fAttributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, nsURI);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset(SchemaDOMParser schemaDOMParser, SymbolTable symbolTable, boolean namespacePrefixes, boolean stringsInternalized)
/*     */   {
/* 342 */     this.fSchemaDOMParser = schemaDOMParser;
/* 343 */     this.fSymbolTable = symbolTable;
/* 344 */     this.fNamespacePrefixes = namespacePrefixes;
/* 345 */     this.fStringsInternalized = stringsInternalized;
/*     */   }
/*     */ 
/*     */   static void convertToSAXParseException(XMLParseException e)
/*     */     throws SAXException
/*     */   {
/* 353 */     Exception ex = e.getException();
/* 354 */     if (ex == null)
/*     */     {
/* 357 */       LocatorImpl locatorImpl = new LocatorImpl();
/* 358 */       locatorImpl.setPublicId(e.getPublicId());
/* 359 */       locatorImpl.setSystemId(e.getExpandedSystemId());
/* 360 */       locatorImpl.setLineNumber(e.getLineNumber());
/* 361 */       locatorImpl.setColumnNumber(e.getColumnNumber());
/* 362 */       throw new SAXParseException(e.getMessage(), locatorImpl);
/*     */     }
/* 364 */     if ((ex instanceof SAXException))
/*     */     {
/* 366 */       throw ((SAXException)ex);
/*     */     }
/* 368 */     throw new SAXException(ex);
/*     */   }
/*     */ 
/*     */   static void convertToSAXException(XNIException e) throws SAXException {
/* 372 */     Exception ex = e.getException();
/* 373 */     if (ex == null) {
/* 374 */       throw new SAXException(e.getMessage());
/*     */     }
/* 376 */     if ((ex instanceof SAXException)) {
/* 377 */       throw ((SAXException)ex);
/*     */     }
/* 379 */     throw new SAXException(ex);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.SchemaContentHandler
 * JD-Core Version:    0.6.2
 */