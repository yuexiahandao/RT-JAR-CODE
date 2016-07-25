/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.AttrImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.ElementImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.PSVIElementNSImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*     */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ final class DOMResultAugmentor
/*     */   implements DOMDocumentHandler
/*     */ {
/*     */   private DOMValidatorHelper fDOMValidatorHelper;
/*     */   private Document fDocument;
/*     */   private CoreDocumentImpl fDocumentImpl;
/*     */   private boolean fStorePSVI;
/*     */   private boolean fIgnoreChars;
/*  75 */   private final QName fAttributeQName = new QName();
/*     */ 
/*     */   public DOMResultAugmentor(DOMValidatorHelper helper) {
/*  78 */     this.fDOMValidatorHelper = helper;
/*     */   }
/*     */ 
/*     */   public void setDOMResult(DOMResult result) {
/*  82 */     this.fIgnoreChars = false;
/*  83 */     if (result != null) {
/*  84 */       Node target = result.getNode();
/*  85 */       this.fDocument = (target.getNodeType() == 9 ? (Document)target : target.getOwnerDocument());
/*  86 */       this.fDocumentImpl = ((this.fDocument instanceof CoreDocumentImpl) ? (CoreDocumentImpl)this.fDocument : null);
/*  87 */       this.fStorePSVI = (this.fDocument instanceof PSVIDocumentImpl);
/*  88 */       return;
/*     */     }
/*  90 */     this.fDocument = null;
/*  91 */     this.fDocumentImpl = null;
/*  92 */     this.fStorePSVI = false;
/*     */   }
/*     */   public void doctypeDecl(DocumentType node) throws XNIException {
/*     */   }
/*     */   public void characters(Text node) throws XNIException {
/*     */   }
/*     */   public void cdata(CDATASection node) throws XNIException {
/*     */   }
/*     */   public void comment(Comment node) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(ProcessingInstruction node) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void setIgnoringCharacters(boolean ignore) {
/* 107 */     this.fIgnoreChars = ignore;
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void comment(XMLString text, Augmentations augs) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 127 */     Element currentElement = (Element)this.fDOMValidatorHelper.getCurrentElement();
/* 128 */     NamedNodeMap attrMap = currentElement.getAttributes();
/*     */ 
/* 130 */     int oldLength = attrMap.getLength();
/*     */ 
/* 132 */     if (this.fDocumentImpl != null)
/*     */     {
/* 134 */       for (int i = 0; i < oldLength; i++) {
/* 135 */         AttrImpl attr = (AttrImpl)attrMap.item(i);
/*     */ 
/* 138 */         AttributePSVI attrPSVI = (AttributePSVI)attributes.getAugmentations(i).getItem("ATTRIBUTE_PSVI");
/* 139 */         if ((attrPSVI != null) && 
/* 140 */           (processAttributePSVI(attr, attrPSVI))) {
/* 141 */           ((ElementImpl)currentElement).setIdAttributeNode(attr, true);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 147 */     int newLength = attributes.getLength();
/*     */ 
/* 149 */     if (newLength > oldLength)
/* 150 */       if (this.fDocumentImpl == null) {
/* 151 */         for (int i = oldLength; i < newLength; i++) {
/* 152 */           attributes.getName(i, this.fAttributeQName);
/* 153 */           currentElement.setAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, attributes.getValue(i));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 158 */         for (int i = oldLength; i < newLength; i++) {
/* 159 */           attributes.getName(i, this.fAttributeQName);
/* 160 */           AttrImpl attr = (AttrImpl)this.fDocumentImpl.createAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, this.fAttributeQName.localpart);
/*     */ 
/* 162 */           attr.setValue(attributes.getValue(i));
/*     */ 
/* 165 */           AttributePSVI attrPSVI = (AttributePSVI)attributes.getAugmentations(i).getItem("ATTRIBUTE_PSVI");
/* 166 */           if ((attrPSVI != null) && 
/* 167 */             (processAttributePSVI(attr, attrPSVI))) {
/* 168 */             ((ElementImpl)currentElement).setIdAttributeNode(attr, true);
/*     */           }
/*     */ 
/* 171 */           attr.setSpecified(false);
/* 172 */           currentElement.setAttributeNode(attr);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 180 */     startElement(element, attributes, augs);
/* 181 */     endElement(element, augs);
/*     */   }
/*     */ 
/*     */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void textDecl(String version, String encoding, Augmentations augs) throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void characters(XMLString text, Augmentations augs) throws XNIException {
/* 196 */     if (!this.fIgnoreChars) {
/* 197 */       Element currentElement = (Element)this.fDOMValidatorHelper.getCurrentElement();
/* 198 */       currentElement.appendChild(this.fDocument.createTextNode(text.toString()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException
/*     */   {
/* 204 */     characters(text, augs);
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, Augmentations augs) throws XNIException
/*     */   {
/* 209 */     Node currentElement = this.fDOMValidatorHelper.getCurrentElement();
/*     */ 
/* 211 */     if ((augs != null) && (this.fDocumentImpl != null)) {
/* 212 */       ElementPSVI elementPSVI = (ElementPSVI)augs.getItem("ELEMENT_PSVI");
/* 213 */       if (elementPSVI != null) {
/* 214 */         if (this.fStorePSVI) {
/* 215 */           ((PSVIElementNSImpl)currentElement).setPSVI(elementPSVI);
/*     */         }
/* 217 */         XSTypeDefinition type = elementPSVI.getMemberTypeDefinition();
/* 218 */         if (type == null) {
/* 219 */           type = elementPSVI.getTypeDefinition();
/*     */         }
/* 221 */         ((ElementNSImpl)currentElement).setType(type);
/*     */       }
/*     */     }
/*     */   }
/*     */   public void startCDATA(Augmentations augs) throws XNIException {
/*     */   }
/*     */   public void endCDATA(Augmentations augs) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void endDocument(Augmentations augs) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void setDocumentSource(XMLDocumentSource source) {  }
/*     */ 
/*     */ 
/* 235 */   public XMLDocumentSource getDocumentSource() { return null; }
/*     */ 
/*     */ 
/*     */   private boolean processAttributePSVI(AttrImpl attr, AttributePSVI attrPSVI)
/*     */   {
/* 240 */     if (this.fStorePSVI) {
/* 241 */       ((PSVIAttrNSImpl)attr).setPSVI(attrPSVI);
/*     */     }
/* 243 */     Object type = attrPSVI.getMemberTypeDefinition();
/* 244 */     if (type == null) {
/* 245 */       type = attrPSVI.getTypeDefinition();
/* 246 */       if (type != null) {
/* 247 */         attr.setType(type);
/* 248 */         return ((XSSimpleType)type).isIDType();
/*     */       }
/*     */     }
/*     */     else {
/* 252 */       attr.setType(type);
/* 253 */       return ((XSSimpleType)type).isIDType();
/*     */     }
/* 255 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.DOMResultAugmentor
 * JD-Core Version:    0.6.2
 */