/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.util.URI;
/*     */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMException;
/*     */ 
/*     */ public class ElementNSImpl extends ElementImpl
/*     */ {
/*     */   static final long serialVersionUID = -9142310625494392642L;
/*     */   static final String xmlURI = "http://www.w3.org/XML/1998/namespace";
/*     */   protected String namespaceURI;
/*     */   protected String localName;
/*     */   transient XSTypeDefinition type;
/*     */ 
/*     */   protected ElementNSImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected ElementNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName)
/*     */     throws DOMException
/*     */   {
/*  83 */     super(ownerDocument, qualifiedName);
/*  84 */     setName(namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   private void setName(String namespaceURI, String qname)
/*     */   {
/*  91 */     this.namespaceURI = namespaceURI;
/*  92 */     if (namespaceURI != null)
/*     */     {
/*  94 */       this.namespaceURI = (namespaceURI.length() == 0 ? null : namespaceURI);
/*     */     }
/*     */ 
/* 103 */     if (qname == null) {
/* 104 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*     */ 
/* 109 */       throw new DOMException((short)14, msg);
/*     */     }
/*     */ 
/* 112 */     int colon1 = qname.indexOf(':');
/* 113 */     int colon2 = qname.lastIndexOf(':');
/*     */ 
/* 116 */     this.ownerDocument.checkNamespaceWF(qname, colon1, colon2);
/* 117 */     if (colon1 < 0)
/*     */     {
/* 119 */       this.localName = qname;
/* 120 */       if (this.ownerDocument.errorChecking) {
/* 121 */         this.ownerDocument.checkQName(null, this.localName);
/* 122 */         if (((qname.equals("xmlns")) && ((namespaceURI == null) || (!namespaceURI.equals(NamespaceContext.XMLNS_URI)))) || ((namespaceURI != null) && (namespaceURI.equals(NamespaceContext.XMLNS_URI)) && (!qname.equals("xmlns"))))
/*     */         {
/* 127 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*     */ 
/* 132 */           throw new DOMException((short)14, msg);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 137 */       String prefix = qname.substring(0, colon1);
/* 138 */       this.localName = qname.substring(colon2 + 1);
/*     */ 
/* 146 */       if (this.ownerDocument.errorChecking) {
/* 147 */         if ((namespaceURI == null) || ((prefix.equals("xml")) && (!namespaceURI.equals(NamespaceContext.XML_URI)))) {
/* 148 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*     */ 
/* 153 */           throw new DOMException((short)14, msg);
/*     */         }
/*     */ 
/* 156 */         this.ownerDocument.checkQName(prefix, this.localName);
/* 157 */         this.ownerDocument.checkDOMNSErr(prefix, namespaceURI);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ElementNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName)
/*     */     throws DOMException
/*     */   {
/* 168 */     super(ownerDocument, qualifiedName);
/*     */ 
/* 170 */     this.localName = localName;
/* 171 */     this.namespaceURI = namespaceURI;
/*     */   }
/*     */ 
/*     */   protected ElementNSImpl(CoreDocumentImpl ownerDocument, String value)
/*     */   {
/* 177 */     super(ownerDocument, value);
/*     */   }
/*     */ 
/*     */   void rename(String namespaceURI, String qualifiedName)
/*     */   {
/* 185 */     if (needsSyncData()) {
/* 186 */       synchronizeData();
/*     */     }
/* 188 */     this.name = qualifiedName;
/* 189 */     setName(namespaceURI, qualifiedName);
/* 190 */     reconcileDefaultAttributes();
/*     */   }
/*     */ 
/*     */   protected void setValues(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName)
/*     */   {
/* 206 */     this.firstChild = null;
/* 207 */     this.previousSibling = null;
/* 208 */     this.nextSibling = null;
/* 209 */     this.fNodeListCache = null;
/*     */ 
/* 212 */     this.attributes = null;
/* 213 */     this.flags = 0;
/* 214 */     setOwnerDocument(ownerDocument);
/*     */ 
/* 217 */     needsSyncData(true);
/* 218 */     this.name = qualifiedName;
/* 219 */     this.localName = localName;
/* 220 */     this.namespaceURI = namespaceURI;
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI()
/*     */   {
/* 249 */     if (needsSyncData()) {
/* 250 */       synchronizeData();
/*     */     }
/* 252 */     return this.namespaceURI;
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 268 */     if (needsSyncData()) {
/* 269 */       synchronizeData();
/*     */     }
/* 271 */     int index = this.name.indexOf(':');
/* 272 */     return index < 0 ? null : this.name.substring(0, index);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */     throws DOMException
/*     */   {
/* 293 */     if (needsSyncData()) {
/* 294 */       synchronizeData();
/*     */     }
/* 296 */     if (this.ownerDocument.errorChecking) {
/* 297 */       if (isReadOnly()) {
/* 298 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 299 */         throw new DOMException((short)7, msg);
/*     */       }
/*     */ 
/* 303 */       if ((prefix != null) && (prefix.length() != 0)) {
/* 304 */         if (!CoreDocumentImpl.isXMLName(prefix, this.ownerDocument.isXML11Version())) {
/* 305 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/* 306 */           throw new DOMException((short)5, msg);
/*     */         }
/* 308 */         if ((this.namespaceURI == null) || (prefix.indexOf(':') >= 0)) {
/* 309 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/* 310 */           throw new DOMException((short)14, msg);
/* 311 */         }if ((prefix.equals("xml")) && 
/* 312 */           (!this.namespaceURI.equals("http://www.w3.org/XML/1998/namespace"))) {
/* 313 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/* 314 */           throw new DOMException((short)14, msg);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 321 */     if ((prefix != null) && (prefix.length() != 0)) {
/* 322 */       this.name = (prefix + ":" + this.localName);
/*     */     }
/*     */     else
/* 325 */       this.name = this.localName;
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 337 */     if (needsSyncData()) {
/* 338 */       synchronizeData();
/*     */     }
/* 340 */     return this.localName;
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 350 */     if (needsSyncData()) {
/* 351 */       synchronizeData();
/*     */     }
/*     */ 
/* 357 */     if (this.attributes != null) {
/* 358 */       Attr attrNode = (Attr)this.attributes.getNamedItemNS("http://www.w3.org/XML/1998/namespace", "base");
/* 359 */       if (attrNode != null) {
/* 360 */         String uri = attrNode.getNodeValue();
/* 361 */         if (uri.length() != 0) {
/*     */           try {
/* 363 */             uri = new URI(uri).toString();
/*     */           }
/*     */           catch (URI.MalformedURIException e)
/*     */           {
/* 369 */             NodeImpl parentOrOwner = parentNode() != null ? parentNode() : this.ownerNode;
/*     */ 
/* 372 */             String parentBaseURI = parentOrOwner != null ? parentOrOwner.getBaseURI() : null;
/*     */ 
/* 374 */             if (parentBaseURI != null) {
/*     */               try {
/* 376 */                 uri = new URI(new URI(parentBaseURI), uri).toString();
/*     */               }
/*     */               catch (URI.MalformedURIException ex)
/*     */               {
/* 380 */                 return null;
/*     */               }
/* 382 */               return uri;
/*     */             }
/*     */ 
/* 385 */             return null;
/*     */           }
/* 387 */           return uri;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 394 */     String parentElementBaseURI = parentNode() != null ? parentNode().getBaseURI() : null;
/*     */ 
/* 396 */     if (parentElementBaseURI != null) {
/*     */       try
/*     */       {
/* 399 */         return new URI(parentElementBaseURI).toString();
/*     */       }
/*     */       catch (URI.MalformedURIException e)
/*     */       {
/* 403 */         return null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 408 */     String baseURI = this.ownerNode != null ? this.ownerNode.getBaseURI() : null;
/*     */ 
/* 410 */     if (baseURI != null) {
/*     */       try
/*     */       {
/* 413 */         return new URI(baseURI).toString();
/*     */       }
/*     */       catch (URI.MalformedURIException e)
/*     */       {
/* 417 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 421 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeName()
/*     */   {
/* 430 */     if (this.type != null) {
/* 431 */       if ((this.type instanceof XSSimpleTypeDecl))
/* 432 */         return ((XSSimpleTypeDecl)this.type).getTypeName();
/* 433 */       if ((this.type instanceof XSComplexTypeDecl)) {
/* 434 */         return ((XSComplexTypeDecl)this.type).getTypeName();
/*     */       }
/*     */     }
/* 437 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeNamespace()
/*     */   {
/* 444 */     if (this.type != null) {
/* 445 */       return this.type.getNamespace();
/*     */     }
/* 447 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod)
/*     */   {
/* 467 */     if (needsSyncData()) {
/* 468 */       synchronizeData();
/*     */     }
/* 470 */     if (this.type != null) {
/* 471 */       if ((this.type instanceof XSSimpleTypeDecl)) {
/* 472 */         return ((XSSimpleTypeDecl)this.type).isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
/*     */       }
/* 474 */       if ((this.type instanceof XSComplexTypeDecl)) {
/* 475 */         return ((XSComplexTypeDecl)this.type).isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
/*     */       }
/*     */     }
/*     */ 
/* 479 */     return false;
/*     */   }
/*     */ 
/*     */   public void setType(XSTypeDefinition type)
/*     */   {
/* 487 */     this.type = type;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.ElementNSImpl
 * JD-Core Version:    0.6.2
 */