/*     */ package com.sun.org.apache.xerces.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ 
/*     */ class TeeXMLDocumentFilterImpl
/*     */   implements XMLDocumentFilter
/*     */ {
/*     */   private XMLDocumentHandler next;
/*     */   private XMLDocumentHandler side;
/*     */   private XMLDocumentSource source;
/*     */ 
/*     */   public XMLDocumentHandler getSide()
/*     */   {
/*  60 */     return this.side;
/*     */   }
/*     */ 
/*     */   public void setSide(XMLDocumentHandler side) {
/*  64 */     this.side = side;
/*     */   }
/*     */ 
/*     */   public XMLDocumentSource getDocumentSource() {
/*  68 */     return this.source;
/*     */   }
/*     */ 
/*     */   public void setDocumentSource(XMLDocumentSource source) {
/*  72 */     this.source = source;
/*     */   }
/*     */ 
/*     */   public XMLDocumentHandler getDocumentHandler() {
/*  76 */     return this.next;
/*     */   }
/*     */ 
/*     */   public void setDocumentHandler(XMLDocumentHandler handler) {
/*  80 */     this.next = handler;
/*     */   }
/*     */ 
/*     */   public void characters(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*  90 */     this.side.characters(text, augs);
/*  91 */     this.next.characters(text, augs);
/*     */   }
/*     */ 
/*     */   public void comment(XMLString text, Augmentations augs) throws XNIException {
/*  95 */     this.side.comment(text, augs);
/*  96 */     this.next.comment(text, augs);
/*     */   }
/*     */ 
/*     */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException
/*     */   {
/* 101 */     this.side.doctypeDecl(rootElement, publicId, systemId, augs);
/* 102 */     this.next.doctypeDecl(rootElement, publicId, systemId, augs);
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 106 */     this.side.emptyElement(element, attributes, augs);
/* 107 */     this.next.emptyElement(element, attributes, augs);
/*     */   }
/*     */ 
/*     */   public void endCDATA(Augmentations augs) throws XNIException {
/* 111 */     this.side.endCDATA(augs);
/* 112 */     this.next.endCDATA(augs);
/*     */   }
/*     */ 
/*     */   public void endDocument(Augmentations augs) throws XNIException {
/* 116 */     this.side.endDocument(augs);
/* 117 */     this.next.endDocument(augs);
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, Augmentations augs) throws XNIException {
/* 121 */     this.side.endElement(element, augs);
/* 122 */     this.next.endElement(element, augs);
/*     */   }
/*     */ 
/*     */   public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
/* 126 */     this.side.endGeneralEntity(name, augs);
/* 127 */     this.next.endGeneralEntity(name, augs);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
/* 131 */     this.side.ignorableWhitespace(text, augs);
/* 132 */     this.next.ignorableWhitespace(text, augs);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
/* 136 */     this.side.processingInstruction(target, data, augs);
/* 137 */     this.next.processingInstruction(target, data, augs);
/*     */   }
/*     */ 
/*     */   public void startCDATA(Augmentations augs) throws XNIException {
/* 141 */     this.side.startCDATA(augs);
/* 142 */     this.next.startCDATA(augs);
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 151 */     this.side.startDocument(locator, encoding, namespaceContext, augs);
/* 152 */     this.next.startDocument(locator, encoding, namespaceContext, augs);
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 156 */     this.side.startElement(element, attributes, augs);
/* 157 */     this.next.startElement(element, attributes, augs);
/*     */   }
/*     */ 
/*     */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException
/*     */   {
/* 162 */     this.side.startGeneralEntity(name, identifier, encoding, augs);
/* 163 */     this.next.startGeneralEntity(name, identifier, encoding, augs);
/*     */   }
/*     */ 
/*     */   public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
/* 167 */     this.side.textDecl(version, encoding, augs);
/* 168 */     this.next.textDecl(version, encoding, augs);
/*     */   }
/*     */ 
/*     */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
/* 172 */     this.side.xmlDecl(version, encoding, standalone, augs);
/* 173 */     this.next.xmlDecl(version, encoding, standalone, augs);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.TeeXMLDocumentFilterImpl
 * JD-Core Version:    0.6.2
 */