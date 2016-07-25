/*     */ package com.sun.org.apache.xerces.internal.util;
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
/*     */ public class TeeXMLDocumentFilterImpl
/*     */   implements XMLDocumentFilter
/*     */ {
/*     */   private XMLDocumentHandler next;
/*     */   private XMLDocumentHandler side;
/*     */   private XMLDocumentSource source;
/*     */ 
/*     */   public XMLDocumentHandler getSide()
/*     */   {
/* 102 */     return this.side;
/*     */   }
/*     */ 
/*     */   public void setSide(XMLDocumentHandler side) {
/* 106 */     this.side = side;
/*     */   }
/*     */ 
/*     */   public XMLDocumentSource getDocumentSource() {
/* 110 */     return this.source;
/*     */   }
/*     */ 
/*     */   public void setDocumentSource(XMLDocumentSource source) {
/* 114 */     this.source = source;
/*     */   }
/*     */ 
/*     */   public XMLDocumentHandler getDocumentHandler() {
/* 118 */     return this.next;
/*     */   }
/*     */ 
/*     */   public void setDocumentHandler(XMLDocumentHandler handler) {
/* 122 */     this.next = handler;
/*     */   }
/*     */ 
/*     */   public void characters(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 131 */     this.side.characters(text, augs);
/* 132 */     this.next.characters(text, augs);
/*     */   }
/*     */ 
/*     */   public void comment(XMLString text, Augmentations augs) throws XNIException {
/* 136 */     this.side.comment(text, augs);
/* 137 */     this.next.comment(text, augs);
/*     */   }
/*     */ 
/*     */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException
/*     */   {
/* 142 */     this.side.doctypeDecl(rootElement, publicId, systemId, augs);
/* 143 */     this.next.doctypeDecl(rootElement, publicId, systemId, augs);
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 147 */     this.side.emptyElement(element, attributes, augs);
/* 148 */     this.next.emptyElement(element, attributes, augs);
/*     */   }
/*     */ 
/*     */   public void endCDATA(Augmentations augs) throws XNIException {
/* 152 */     this.side.endCDATA(augs);
/* 153 */     this.next.endCDATA(augs);
/*     */   }
/*     */ 
/*     */   public void endDocument(Augmentations augs) throws XNIException {
/* 157 */     this.side.endDocument(augs);
/* 158 */     this.next.endDocument(augs);
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, Augmentations augs) throws XNIException {
/* 162 */     this.side.endElement(element, augs);
/* 163 */     this.next.endElement(element, augs);
/*     */   }
/*     */ 
/*     */   public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
/* 167 */     this.side.endGeneralEntity(name, augs);
/* 168 */     this.next.endGeneralEntity(name, augs);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
/* 172 */     this.side.ignorableWhitespace(text, augs);
/* 173 */     this.next.ignorableWhitespace(text, augs);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
/* 177 */     this.side.processingInstruction(target, data, augs);
/* 178 */     this.next.processingInstruction(target, data, augs);
/*     */   }
/*     */ 
/*     */   public void startCDATA(Augmentations augs) throws XNIException {
/* 182 */     this.side.startCDATA(augs);
/* 183 */     this.next.startCDATA(augs);
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 192 */     this.side.startDocument(locator, encoding, namespaceContext, augs);
/* 193 */     this.next.startDocument(locator, encoding, namespaceContext, augs);
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 197 */     this.side.startElement(element, attributes, augs);
/* 198 */     this.next.startElement(element, attributes, augs);
/*     */   }
/*     */ 
/*     */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException
/*     */   {
/* 203 */     this.side.startGeneralEntity(name, identifier, encoding, augs);
/* 204 */     this.next.startGeneralEntity(name, identifier, encoding, augs);
/*     */   }
/*     */ 
/*     */   public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
/* 208 */     this.side.textDecl(version, encoding, augs);
/* 209 */     this.next.textDecl(version, encoding, augs);
/*     */   }
/*     */ 
/*     */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
/* 213 */     this.side.xmlDecl(version, encoding, standalone, augs);
/* 214 */     this.next.xmlDecl(version, encoding, standalone, augs);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.TeeXMLDocumentFilterImpl
 * JD-Core Version:    0.6.2
 */