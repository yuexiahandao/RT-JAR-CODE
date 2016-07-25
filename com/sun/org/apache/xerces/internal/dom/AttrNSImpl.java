/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import org.w3c.dom.DOMException;
/*     */ 
/*     */ public class AttrNSImpl extends AttrImpl
/*     */ {
/*     */   static final long serialVersionUID = -781906615369795414L;
/*     */   static final String xmlnsURI = "http://www.w3.org/2000/xmlns/";
/*     */   static final String xmlURI = "http://www.w3.org/XML/1998/namespace";
/*     */   protected String namespaceURI;
/*     */   protected String localName;
/*     */ 
/*     */   public AttrNSImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected AttrNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName)
/*     */   {
/*  77 */     super(ownerDocument, qualifiedName);
/*  78 */     setName(namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   private void setName(String namespaceURI, String qname) {
/*  82 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*     */ 
/*  85 */     this.namespaceURI = namespaceURI;
/*  86 */     if (namespaceURI != null) {
/*  87 */       this.namespaceURI = (namespaceURI.length() == 0 ? null : namespaceURI);
/*     */     }
/*     */ 
/*  91 */     int colon1 = qname.indexOf(':');
/*  92 */     int colon2 = qname.lastIndexOf(':');
/*  93 */     ownerDocument.checkNamespaceWF(qname, colon1, colon2);
/*  94 */     if (colon1 < 0)
/*     */     {
/*  96 */       this.localName = qname;
/*  97 */       if (ownerDocument.errorChecking) {
/*  98 */         ownerDocument.checkQName(null, this.localName);
/*     */ 
/* 100 */         if (((qname.equals("xmlns")) && ((namespaceURI == null) || (!namespaceURI.equals(NamespaceContext.XMLNS_URI)))) || ((namespaceURI != null) && (namespaceURI.equals(NamespaceContext.XMLNS_URI)) && (!qname.equals("xmlns"))))
/*     */         {
/* 104 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/*     */ 
/* 109 */           throw new DOMException((short)14, msg);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 114 */       String prefix = qname.substring(0, colon1);
/* 115 */       this.localName = qname.substring(colon2 + 1);
/* 116 */       ownerDocument.checkQName(prefix, this.localName);
/* 117 */       ownerDocument.checkDOMNSErr(prefix, namespaceURI);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AttrNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName)
/*     */   {
/* 126 */     super(ownerDocument, qualifiedName);
/*     */ 
/* 128 */     this.localName = localName;
/* 129 */     this.namespaceURI = namespaceURI;
/*     */   }
/*     */ 
/*     */   protected AttrNSImpl(CoreDocumentImpl ownerDocument, String value)
/*     */   {
/* 135 */     super(ownerDocument, value);
/*     */   }
/*     */ 
/*     */   void rename(String namespaceURI, String qualifiedName)
/*     */   {
/* 143 */     if (needsSyncData()) {
/* 144 */       synchronizeData();
/*     */     }
/* 146 */     this.name = qualifiedName;
/* 147 */     setName(namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public void setValues(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName)
/*     */   {
/* 163 */     this.textNode = null;
/* 164 */     this.flags = 0;
/* 165 */     isSpecified(true);
/* 166 */     hasStringValue(true);
/* 167 */     super.setOwnerDocument(ownerDocument);
/* 168 */     this.localName = localName;
/* 169 */     this.namespaceURI = namespaceURI;
/* 170 */     this.name = qualifiedName;
/* 171 */     this.value = null;
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI()
/*     */   {
/* 193 */     if (needsSyncData()) {
/* 194 */       synchronizeData();
/*     */     }
/*     */ 
/* 199 */     return this.namespaceURI;
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 214 */     if (needsSyncData()) {
/* 215 */       synchronizeData();
/*     */     }
/* 217 */     int index = this.name.indexOf(':');
/* 218 */     return index < 0 ? null : this.name.substring(0, index);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */     throws DOMException
/*     */   {
/* 239 */     if (needsSyncData()) {
/* 240 */       synchronizeData();
/*     */     }
/* 242 */     if (ownerDocument().errorChecking) {
/* 243 */       if (isReadOnly()) {
/* 244 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 245 */         throw new DOMException((short)7, msg);
/*     */       }
/* 247 */       if ((prefix != null) && (prefix.length() != 0))
/*     */       {
/* 249 */         if (!CoreDocumentImpl.isXMLName(prefix, ownerDocument().isXML11Version())) {
/* 250 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
/* 251 */           throw new DOMException((short)5, msg);
/*     */         }
/* 253 */         if ((this.namespaceURI == null) || (prefix.indexOf(':') >= 0)) {
/* 254 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/* 255 */           throw new DOMException((short)14, msg);
/*     */         }
/*     */ 
/* 258 */         if (prefix.equals("xmlns")) {
/* 259 */           if (!this.namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
/* 260 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/* 261 */             throw new DOMException((short)14, msg);
/*     */           }
/* 263 */         } else if (prefix.equals("xml")) {
/* 264 */           if (!this.namespaceURI.equals("http://www.w3.org/XML/1998/namespace")) {
/* 265 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/* 266 */             throw new DOMException((short)14, msg);
/*     */           }
/* 268 */         } else if (this.name.equals("xmlns")) {
/* 269 */           String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
/* 270 */           throw new DOMException((short)14, msg);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 276 */     if ((prefix != null) && (prefix.length() != 0)) {
/* 277 */       this.name = (prefix + ":" + this.localName);
/*     */     }
/*     */     else
/* 280 */       this.name = this.localName;
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 292 */     if (needsSyncData()) {
/* 293 */       synchronizeData();
/*     */     }
/* 295 */     return this.localName;
/*     */   }
/*     */ 
/*     */   public String getTypeName()
/*     */   {
/* 303 */     if (this.type != null) {
/* 304 */       if ((this.type instanceof XSSimpleTypeDecl)) {
/* 305 */         return ((XSSimpleTypeDecl)this.type).getName();
/*     */       }
/* 307 */       return (String)this.type;
/*     */     }
/* 309 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod)
/*     */   {
/* 330 */     if ((this.type != null) && 
/* 331 */       ((this.type instanceof XSSimpleTypeDecl))) {
/* 332 */       return ((XSSimpleTypeDecl)this.type).isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
/*     */     }
/*     */ 
/* 336 */     return false;
/*     */   }
/*     */ 
/*     */   public String getTypeNamespace()
/*     */   {
/* 343 */     if (this.type != null) {
/* 344 */       if ((this.type instanceof XSSimpleTypeDecl)) {
/* 345 */         return ((XSSimpleTypeDecl)this.type).getNamespace();
/*     */       }
/* 347 */       return "http://www.w3.org/TR/REC-xml";
/*     */     }
/* 349 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.AttrNSImpl
 * JD-Core Version:    0.6.2
 */