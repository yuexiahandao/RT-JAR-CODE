/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ 
/*     */ public class XML11NSDTDValidator extends XML11DTDValidator
/*     */ {
/* 107 */   private QName fAttributeQName = new QName();
/*     */ 
/*     */   protected final void startNamespaceScope(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 114 */     this.fNamespaceContext.pushContext();
/*     */ 
/* 116 */     if (element.prefix == XMLSymbols.PREFIX_XMLNS) {
/* 117 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { element.rawname }, (short)2);
/*     */     }
/*     */ 
/* 125 */     int length = attributes.getLength();
/* 126 */     for (int i = 0; i < length; i++) {
/* 127 */       String localpart = attributes.getLocalName(i);
/* 128 */       String prefix = attributes.getPrefix(i);
/*     */ 
/* 131 */       if ((prefix == XMLSymbols.PREFIX_XMLNS) || ((prefix == XMLSymbols.EMPTY_STRING) && (localpart == XMLSymbols.PREFIX_XMLNS)))
/*     */       {
/* 135 */         String uri = this.fSymbolTable.addSymbol(attributes.getValue(i));
/*     */ 
/* 138 */         if ((prefix == XMLSymbols.PREFIX_XMLNS) && (localpart == XMLSymbols.PREFIX_XMLNS)) {
/* 139 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { attributes.getQName(i) }, (short)2);
/*     */         }
/*     */ 
/* 147 */         if (uri == NamespaceContext.XMLNS_URI) {
/* 148 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { attributes.getQName(i) }, (short)2);
/*     */         }
/*     */ 
/* 156 */         if (localpart == XMLSymbols.PREFIX_XML) {
/* 157 */           if (uri != NamespaceContext.XML_URI) {
/* 158 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { attributes.getQName(i) }, (short)2);
/*     */           }
/*     */ 
/*     */         }
/* 167 */         else if (uri == NamespaceContext.XML_URI) {
/* 168 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { attributes.getQName(i) }, (short)2);
/*     */         }
/*     */ 
/* 176 */         prefix = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;
/*     */ 
/* 181 */         this.fNamespaceContext.declarePrefix(prefix, uri.length() != 0 ? uri : null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 186 */     String prefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
/* 187 */     element.uri = this.fNamespaceContext.getURI(prefix);
/* 188 */     if ((element.prefix == null) && (element.uri != null)) {
/* 189 */       element.prefix = XMLSymbols.EMPTY_STRING;
/*     */     }
/* 191 */     if ((element.prefix != null) && (element.uri == null)) {
/* 192 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { element.prefix, element.rawname }, (short)2);
/*     */     }
/*     */ 
/* 200 */     for (int i = 0; i < length; i++) {
/* 201 */       attributes.getName(i, this.fAttributeQName);
/* 202 */       String aprefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
/* 203 */       String arawname = this.fAttributeQName.rawname;
/* 204 */       if (arawname == XMLSymbols.PREFIX_XMLNS) {
/* 205 */         this.fAttributeQName.uri = this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
/* 206 */         attributes.setName(i, this.fAttributeQName);
/* 207 */       } else if (aprefix != XMLSymbols.EMPTY_STRING) {
/* 208 */         this.fAttributeQName.uri = this.fNamespaceContext.getURI(aprefix);
/* 209 */         if (this.fAttributeQName.uri == null) {
/* 210 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { element.rawname, arawname, aprefix }, (short)2);
/*     */         }
/*     */ 
/* 216 */         attributes.setName(i, this.fAttributeQName);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 222 */     int attrCount = attributes.getLength();
/* 223 */     for (int i = 0; i < attrCount - 1; i++) {
/* 224 */       String auri = attributes.getURI(i);
/* 225 */       if ((auri != null) && (auri != NamespaceContext.XMLNS_URI))
/*     */       {
/* 228 */         String alocalpart = attributes.getLocalName(i);
/* 229 */         for (int j = i + 1; j < attrCount; j++) {
/* 230 */           String blocalpart = attributes.getLocalName(j);
/* 231 */           String buri = attributes.getURI(j);
/* 232 */           if ((alocalpart == blocalpart) && (auri == buri))
/* 233 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { element.rawname, alocalpart, auri }, (short)2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void endNamespaceScope(QName element, Augmentations augs, boolean isEmpty)
/*     */     throws XNIException
/*     */   {
/* 249 */     String eprefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
/* 250 */     element.uri = this.fNamespaceContext.getURI(eprefix);
/* 251 */     if (element.uri != null) {
/* 252 */       element.prefix = eprefix;
/*     */     }
/*     */ 
/* 256 */     if ((this.fDocumentHandler != null) && 
/* 257 */       (!isEmpty)) {
/* 258 */       this.fDocumentHandler.endElement(element, augs);
/*     */     }
/*     */ 
/* 263 */     this.fNamespaceContext.popContext();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XML11NSDTDValidator
 * JD-Core Version:    0.6.2
 */