/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
/*     */ import com.sun.org.apache.xerces.internal.jaxp.validation.WrappedSAXException;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class SAX2XNI
/*     */   implements ContentHandler, XMLDocumentSource
/*     */ {
/*     */   private XMLDocumentHandler fCore;
/*  90 */   private final NamespaceSupport nsContext = new NamespaceSupport();
/*  91 */   private final SymbolTable symbolTable = new SymbolTable();
/*     */   private Locator locator;
/* 243 */   private final XMLAttributes xa = new XMLAttributesImpl();
/*     */ 
/*     */   public SAX2XNI(XMLDocumentHandler core)
/*     */   {
/*  85 */     this.fCore = core;
/*     */   }
/*     */ 
/*     */   public void setDocumentHandler(XMLDocumentHandler handler)
/*     */   {
/*  95 */     this.fCore = handler;
/*     */   }
/*     */ 
/*     */   public XMLDocumentHandler getDocumentHandler() {
/*  99 */     return this.fCore;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 110 */       this.nsContext.reset();
/*     */       XMLLocator xmlLocator;
/*     */       XMLLocator xmlLocator;
/* 113 */       if (this.locator == null)
/*     */       {
/* 118 */         xmlLocator = new SimpleLocator(null, null, -1, -1);
/*     */       }
/* 120 */       else xmlLocator = new LocatorWrapper(this.locator);
/*     */ 
/* 122 */       this.fCore.startDocument(xmlLocator, null, this.nsContext, null);
/*     */     }
/*     */     catch (WrappedSAXException e)
/*     */     {
/* 128 */       throw e.exception;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException {
/*     */     try {
/* 134 */       this.fCore.endDocument(null);
/*     */     } catch (WrappedSAXException e) {
/* 136 */       throw e.exception;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String local, String qname, Attributes att) throws SAXException {
/*     */     try {
/* 142 */       this.fCore.startElement(createQName(uri, local, qname), createAttributes(att), null);
/*     */     } catch (WrappedSAXException e) {
/* 144 */       throw e.exception;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String local, String qname) throws SAXException {
/*     */     try {
/* 150 */       this.fCore.endElement(createQName(uri, local, qname), null);
/*     */     } catch (WrappedSAXException e) {
/* 152 */       throw e.exception;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] buf, int offset, int len) throws SAXException {
/*     */     try {
/* 158 */       this.fCore.characters(new XMLString(buf, offset, len), null);
/*     */     } catch (WrappedSAXException e) {
/* 160 */       throw e.exception;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] buf, int offset, int len) throws SAXException {
/*     */     try {
/* 166 */       this.fCore.ignorableWhitespace(new XMLString(buf, offset, len), null);
/*     */     } catch (WrappedSAXException e) {
/* 168 */       throw e.exception;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) {
/* 173 */     this.nsContext.pushContext();
/* 174 */     this.nsContext.declarePrefix(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) {
/* 178 */     this.nsContext.popContext();
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/*     */     try {
/* 183 */       this.fCore.processingInstruction(symbolize(target), createXMLString(data), null);
/*     */     }
/*     */     catch (WrappedSAXException e) {
/* 186 */       throw e.exception;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator _loc) {
/* 195 */     this.locator = _loc;
/*     */   }
/*     */ 
/*     */   private QName createQName(String uri, String local, String raw)
/*     */   {
/* 201 */     int idx = raw.indexOf(':');
/*     */ 
/* 203 */     if (local.length() == 0)
/*     */     {
/* 206 */       uri = "";
/* 207 */       if (idx < 0)
/* 208 */         local = raw;
/*     */       else
/* 210 */         local = raw.substring(idx + 1);
/*     */     }
/*     */     String prefix;
/*     */     String prefix;
/* 214 */     if (idx < 0)
/* 215 */       prefix = null;
/*     */     else {
/* 217 */       prefix = raw.substring(0, idx);
/*     */     }
/* 219 */     if ((uri != null) && (uri.length() == 0)) {
/* 220 */       uri = null;
/*     */     }
/* 222 */     return new QName(symbolize(prefix), symbolize(local), symbolize(raw), symbolize(uri));
/*     */   }
/*     */ 
/*     */   private String symbolize(String s)
/*     */   {
/* 227 */     if (s == null) {
/* 228 */       return null;
/*     */     }
/* 230 */     return this.symbolTable.addSymbol(s);
/*     */   }
/*     */ 
/*     */   private XMLString createXMLString(String str)
/*     */   {
/* 238 */     return new XMLString(str.toCharArray(), 0, str.length());
/*     */   }
/*     */ 
/*     */   private XMLAttributes createAttributes(Attributes att)
/*     */   {
/* 247 */     this.xa.removeAllAttributes();
/* 248 */     int len = att.getLength();
/* 249 */     for (int i = 0; i < len; i++) {
/* 250 */       this.xa.addAttribute(createQName(att.getURI(i), att.getLocalName(i), att.getQName(i)), att.getType(i), att.getValue(i));
/*     */     }
/*     */ 
/* 254 */     return this.xa;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.SAX2XNI
 * JD-Core Version:    0.6.2
 */