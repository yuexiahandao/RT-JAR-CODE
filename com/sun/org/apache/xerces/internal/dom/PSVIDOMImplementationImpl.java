/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class PSVIDOMImplementationImpl extends CoreDOMImplementationImpl
/*     */ {
/*  51 */   static PSVIDOMImplementationImpl singleton = new PSVIDOMImplementationImpl();
/*     */ 
/*     */   public static DOMImplementation getDOMImplementation()
/*     */   {
/*  59 */     return singleton;
/*     */   }
/*     */ 
/*     */   public boolean hasFeature(String feature, String version)
/*     */   {
/*  82 */     return (super.hasFeature(feature, version)) || (feature.equalsIgnoreCase("psvi"));
/*     */   }
/*     */ 
/*     */   public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype)
/*     */     throws DOMException
/*     */   {
/* 111 */     if ((doctype != null) && (doctype.getOwnerDocument() != null)) {
/* 112 */       throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "WRONG_DOCUMENT_ERR", null));
/*     */     }
/*     */ 
/* 117 */     DocumentImpl doc = new PSVIDocumentImpl(doctype);
/* 118 */     Element e = doc.createElementNS(namespaceURI, qualifiedName);
/* 119 */     doc.appendChild(e);
/* 120 */     return doc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.PSVIDOMImplementationImpl
 * JD-Core Version:    0.6.2
 */