/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.AttrImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.dom.DocumentTypeImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.ElementImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.EntityImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.NotationImpl;
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
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Entity;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Notation;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ final class DOMResultBuilder
/*     */   implements DOMDocumentHandler
/*     */ {
/*  77 */   private static final int[] kidOK = new int[13];
/*     */   private Document fDocument;
/*     */   private CoreDocumentImpl fDocumentImpl;
/*     */   private boolean fStorePSVI;
/*     */   private Node fTarget;
/*     */   private Node fNextSibling;
/*     */   private Node fCurrentNode;
/*     */   private Node fFragmentRoot;
/* 111 */   private final ArrayList fTargetChildren = new ArrayList();
/*     */   private boolean fIgnoreChars;
/* 115 */   private final QName fAttributeQName = new QName();
/*     */ 
/*     */   public void setDOMResult(DOMResult result)
/*     */   {
/* 124 */     this.fCurrentNode = null;
/* 125 */     this.fFragmentRoot = null;
/* 126 */     this.fIgnoreChars = false;
/* 127 */     this.fTargetChildren.clear();
/* 128 */     if (result != null) {
/* 129 */       this.fTarget = result.getNode();
/* 130 */       this.fNextSibling = result.getNextSibling();
/* 131 */       this.fDocument = (this.fTarget.getNodeType() == 9 ? (Document)this.fTarget : this.fTarget.getOwnerDocument());
/* 132 */       this.fDocumentImpl = ((this.fDocument instanceof CoreDocumentImpl) ? (CoreDocumentImpl)this.fDocument : null);
/* 133 */       this.fStorePSVI = (this.fDocument instanceof PSVIDocumentImpl);
/* 134 */       return;
/*     */     }
/* 136 */     this.fTarget = null;
/* 137 */     this.fNextSibling = null;
/* 138 */     this.fDocument = null;
/* 139 */     this.fDocumentImpl = null;
/* 140 */     this.fStorePSVI = false;
/*     */   }
/*     */ 
/*     */   public void doctypeDecl(DocumentType node) throws XNIException
/*     */   {
/* 145 */     if (this.fDocumentImpl != null) {
/* 146 */       DocumentType docType = this.fDocumentImpl.createDocumentType(node.getName(), node.getPublicId(), node.getSystemId());
/* 147 */       String internalSubset = node.getInternalSubset();
/*     */ 
/* 149 */       if (internalSubset != null) {
/* 150 */         ((DocumentTypeImpl)docType).setInternalSubset(internalSubset);
/*     */       }
/*     */ 
/* 153 */       NamedNodeMap oldMap = node.getEntities();
/* 154 */       NamedNodeMap newMap = docType.getEntities();
/* 155 */       int length = oldMap.getLength();
/* 156 */       for (int i = 0; i < length; i++) {
/* 157 */         Entity oldEntity = (Entity)oldMap.item(i);
/* 158 */         EntityImpl newEntity = (EntityImpl)this.fDocumentImpl.createEntity(oldEntity.getNodeName());
/* 159 */         newEntity.setPublicId(oldEntity.getPublicId());
/* 160 */         newEntity.setSystemId(oldEntity.getSystemId());
/* 161 */         newEntity.setNotationName(oldEntity.getNotationName());
/* 162 */         newMap.setNamedItem(newEntity);
/*     */       }
/*     */ 
/* 165 */       oldMap = node.getNotations();
/* 166 */       newMap = docType.getNotations();
/* 167 */       length = oldMap.getLength();
/* 168 */       for (int i = 0; i < length; i++) {
/* 169 */         Notation oldNotation = (Notation)oldMap.item(i);
/* 170 */         NotationImpl newNotation = (NotationImpl)this.fDocumentImpl.createNotation(oldNotation.getNodeName());
/* 171 */         newNotation.setPublicId(oldNotation.getPublicId());
/* 172 */         newNotation.setSystemId(oldNotation.getSystemId());
/* 173 */         newMap.setNamedItem(newNotation);
/*     */       }
/* 175 */       append(docType);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(Text node) throws XNIException
/*     */   {
/* 181 */     append(this.fDocument.createTextNode(node.getNodeValue()));
/*     */   }
/*     */ 
/*     */   public void cdata(CDATASection node) throws XNIException
/*     */   {
/* 186 */     append(this.fDocument.createCDATASection(node.getNodeValue()));
/*     */   }
/*     */ 
/*     */   public void comment(Comment node) throws XNIException
/*     */   {
/* 191 */     append(this.fDocument.createComment(node.getNodeValue()));
/*     */   }
/*     */ 
/*     */   public void processingInstruction(ProcessingInstruction node)
/*     */     throws XNIException
/*     */   {
/* 197 */     append(this.fDocument.createProcessingInstruction(node.getTarget(), node.getData()));
/*     */   }
/*     */ 
/*     */   public void setIgnoringCharacters(boolean ignore) {
/* 201 */     this.fIgnoreChars = ignore;
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void comment(XMLString text, Augmentations augs) throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException
/*     */   {
/* 226 */     int attrCount = attributes.getLength();
/*     */     Element elem;
/* 227 */     if (this.fDocumentImpl == null) {
/* 228 */       Element elem = this.fDocument.createElementNS(element.uri, element.rawname);
/* 229 */       for (int i = 0; i < attrCount; i++) {
/* 230 */         attributes.getName(i, this.fAttributeQName);
/* 231 */         elem.setAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, attributes.getValue(i));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 236 */       elem = this.fDocumentImpl.createElementNS(element.uri, element.rawname, element.localpart);
/* 237 */       for (int i = 0; i < attrCount; i++) {
/* 238 */         attributes.getName(i, this.fAttributeQName);
/* 239 */         AttrImpl attr = (AttrImpl)this.fDocumentImpl.createAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, this.fAttributeQName.localpart);
/*     */ 
/* 241 */         attr.setValue(attributes.getValue(i));
/*     */ 
/* 244 */         AttributePSVI attrPSVI = (AttributePSVI)attributes.getAugmentations(i).getItem("ATTRIBUTE_PSVI");
/* 245 */         if (attrPSVI != null) {
/* 246 */           if (this.fStorePSVI) {
/* 247 */             ((PSVIAttrNSImpl)attr).setPSVI(attrPSVI);
/*     */           }
/* 249 */           Object type = attrPSVI.getMemberTypeDefinition();
/* 250 */           if (type == null) {
/* 251 */             type = attrPSVI.getTypeDefinition();
/* 252 */             if (type != null) {
/* 253 */               attr.setType(type);
/* 254 */               if (((XSSimpleType)type).isIDType())
/* 255 */                 ((ElementImpl)elem).setIdAttributeNode(attr, true);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 260 */             attr.setType(type);
/* 261 */             if (((XSSimpleType)type).isIDType()) {
/* 262 */               ((ElementImpl)elem).setIdAttributeNode(attr, true);
/*     */             }
/*     */           }
/*     */         }
/* 266 */         attr.setSpecified(attributes.isSpecified(i));
/* 267 */         elem.setAttributeNode(attr);
/*     */       }
/*     */     }
/* 270 */     append(elem);
/* 271 */     this.fCurrentNode = elem;
/* 272 */     if (this.fFragmentRoot == null)
/* 273 */       this.fFragmentRoot = elem;
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 279 */     startElement(element, attributes, augs);
/* 280 */     endElement(element, augs);
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
/* 295 */     if (!this.fIgnoreChars)
/* 296 */       append(this.fDocument.createTextNode(text.toString()));
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 302 */     characters(text, augs);
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 308 */     if ((augs != null) && (this.fDocumentImpl != null)) {
/* 309 */       ElementPSVI elementPSVI = (ElementPSVI)augs.getItem("ELEMENT_PSVI");
/* 310 */       if (elementPSVI != null) {
/* 311 */         if (this.fStorePSVI) {
/* 312 */           ((PSVIElementNSImpl)this.fCurrentNode).setPSVI(elementPSVI);
/*     */         }
/* 314 */         XSTypeDefinition type = elementPSVI.getMemberTypeDefinition();
/* 315 */         if (type == null) {
/* 316 */           type = elementPSVI.getTypeDefinition();
/*     */         }
/* 318 */         ((ElementNSImpl)this.fCurrentNode).setType(type);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 323 */     if (this.fCurrentNode == this.fFragmentRoot) {
/* 324 */       this.fCurrentNode = null;
/* 325 */       this.fFragmentRoot = null;
/* 326 */       return;
/*     */     }
/* 328 */     this.fCurrentNode = this.fCurrentNode.getParentNode();
/*     */   }
/*     */   public void startCDATA(Augmentations augs) throws XNIException {
/*     */   }
/*     */   public void endCDATA(Augmentations augs) throws XNIException {
/*     */   }
/*     */ 
/*     */   public void endDocument(Augmentations augs) throws XNIException {
/* 336 */     int length = this.fTargetChildren.size();
/* 337 */     if (this.fNextSibling == null) {
/* 338 */       for (int i = 0; i < length; i++) {
/* 339 */         this.fTarget.appendChild((Node)this.fTargetChildren.get(i));
/*     */       }
/*     */     }
/*     */     else
/* 343 */       for (int i = 0; i < length; i++)
/* 344 */         this.fTarget.insertBefore((Node)this.fTargetChildren.get(i), this.fNextSibling);
/*     */   }
/*     */ 
/*     */   public void setDocumentSource(XMLDocumentSource source)
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLDocumentSource getDocumentSource() {
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */   private void append(Node node)
/*     */     throws XNIException
/*     */   {
/* 360 */     if (this.fCurrentNode != null) {
/* 361 */       this.fCurrentNode.appendChild(node);
/*     */     }
/*     */     else
/*     */     {
/* 365 */       if ((kidOK[this.fTarget.getNodeType()] & 1 << node.getNodeType()) == 0) {
/* 366 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
/* 367 */         throw new XNIException(msg);
/*     */       }
/* 369 */       this.fTargetChildren.add(node);
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  78 */     kidOK[9] = 1410;
/*     */     short tmp41_40 = (kidOK[5] = kidOK[1] = 442); kidOK[6] = tmp41_40; kidOK[11] = tmp41_40;
/*     */ 
/*  88 */     kidOK[2] = 40;
/*  89 */     kidOK[10] = 0;
/*  90 */     kidOK[7] = 0;
/*  91 */     kidOK[8] = 0;
/*  92 */     kidOK[3] = 0;
/*  93 */     kidOK[4] = 0;
/*  94 */     kidOK[12] = 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.DOMResultBuilder
 * JD-Core Version:    0.6.2
 */