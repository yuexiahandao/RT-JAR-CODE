/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class DOMImplementationImpl extends CoreDOMImplementationImpl
/*     */   implements DOMImplementation
/*     */ {
/*  54 */   static DOMImplementationImpl singleton = new DOMImplementationImpl();
/*     */ 
/*     */   public static DOMImplementation getDOMImplementation()
/*     */   {
/*  63 */     return singleton;
/*     */   }
/*     */ 
/*     */   public boolean hasFeature(String feature, String version)
/*     */   {
/*  87 */     boolean result = super.hasFeature(feature, version);
/*  88 */     if (!result) {
/*  89 */       boolean anyVersion = (version == null) || (version.length() == 0);
/*  90 */       if (feature.startsWith("+")) {
/*  91 */         feature = feature.substring(1);
/*     */       }
/*  93 */       return ((feature.equalsIgnoreCase("Events")) && ((anyVersion) || (version.equals("2.0")))) || ((feature.equalsIgnoreCase("MutationEvents")) && ((anyVersion) || (version.equals("2.0")))) || ((feature.equalsIgnoreCase("Traversal")) && ((anyVersion) || (version.equals("2.0")))) || ((feature.equalsIgnoreCase("Range")) && ((anyVersion) || (version.equals("2.0")))) || ((feature.equalsIgnoreCase("MutationEvents")) && ((anyVersion) || (version.equals("2.0"))));
/*     */     }
/*     */ 
/* 105 */     return result;
/*     */   }
/*     */ 
/*     */   public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype)
/*     */     throws DOMException
/*     */   {
/* 135 */     if ((namespaceURI == null) && (qualifiedName == null) && (doctype == null))
/*     */     {
/* 138 */       return new DocumentImpl();
/*     */     }
/* 140 */     if ((doctype != null) && (doctype.getOwnerDocument() != null)) {
/* 141 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/* 142 */       throw new DOMException((short)4, msg);
/*     */     }
/* 144 */     DocumentImpl doc = new DocumentImpl(doctype);
/* 145 */     Element e = doc.createElementNS(namespaceURI, qualifiedName);
/* 146 */     doc.appendChild(e);
/* 147 */     return doc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl
 * JD-Core Version:    0.6.2
 */