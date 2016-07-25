/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.CDATAImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.CommentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.ElementFactory;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.TextImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.EntityReference;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class SOAPDocumentImpl extends DocumentImpl
/*     */   implements SOAPDocument
/*     */ {
/*  43 */   private static final String XMLNS = "xmlns".intern();
/*  44 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap", "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
/*     */   SOAPPartImpl enclosingSOAPPart;
/*     */ 
/*     */   public SOAPDocumentImpl(SOAPPartImpl enclosingDocument)
/*     */   {
/*  51 */     this.enclosingSOAPPart = enclosingDocument;
/*     */   }
/*     */ 
/*     */   public SOAPPartImpl getSOAPPart()
/*     */   {
/*  67 */     if (this.enclosingSOAPPart == null) {
/*  68 */       log.severe("SAAJ0541.soap.fragment.not.bound.to.part");
/*  69 */       throw new RuntimeException("Could not complete operation. Fragment not bound to SOAP part.");
/*     */     }
/*  71 */     return this.enclosingSOAPPart;
/*     */   }
/*     */ 
/*     */   public SOAPDocumentImpl getDocument() {
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */   public DocumentType getDoctype()
/*     */   {
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   public DOMImplementation getImplementation() {
/*  84 */     return super.getImplementation();
/*     */   }
/*     */ 
/*     */   public Element getDocumentElement()
/*     */   {
/*  89 */     getSOAPPart().doGetDocumentElement();
/*  90 */     return doGetDocumentElement();
/*     */   }
/*     */ 
/*     */   protected Element doGetDocumentElement() {
/*  94 */     return super.getDocumentElement();
/*     */   }
/*     */ 
/*     */   public Element createElement(String tagName) throws DOMException {
/*  98 */     return ElementFactory.createElement(this, NameImpl.getLocalNameFromTagName(tagName), NameImpl.getPrefixFromTagName(tagName), null);
/*     */   }
/*     */ 
/*     */   public DocumentFragment createDocumentFragment()
/*     */   {
/* 106 */     return new SOAPDocumentFragment(this);
/*     */   }
/*     */ 
/*     */   public Text createTextNode(String data) {
/* 110 */     return new TextImpl(this, data);
/*     */   }
/*     */ 
/*     */   public Comment createComment(String data) {
/* 114 */     return new CommentImpl(this, data);
/*     */   }
/*     */ 
/*     */   public CDATASection createCDATASection(String data) throws DOMException {
/* 118 */     return new CDATAImpl(this, data);
/*     */   }
/*     */ 
/*     */   public ProcessingInstruction createProcessingInstruction(String target, String data)
/*     */     throws DOMException
/*     */   {
/* 125 */     log.severe("SAAJ0542.soap.proc.instructions.not.allowed.in.docs");
/* 126 */     throw new UnsupportedOperationException("Processing Instructions are not allowed in SOAP documents");
/*     */   }
/*     */ 
/*     */   public Attr createAttribute(String name) throws DOMException {
/* 130 */     boolean isQualifiedName = name.indexOf(":") > 0;
/* 131 */     if (isQualifiedName) {
/* 132 */       String nsUri = null;
/* 133 */       String prefix = name.substring(0, name.indexOf(":"));
/*     */ 
/* 136 */       if (XMLNS.equals(prefix)) {
/* 137 */         nsUri = ElementImpl.XMLNS_URI;
/* 138 */         return createAttributeNS(nsUri, name);
/*     */       }
/*     */     }
/*     */ 
/* 142 */     return super.createAttribute(name);
/*     */   }
/*     */ 
/*     */   public EntityReference createEntityReference(String name) throws DOMException
/*     */   {
/* 147 */     log.severe("SAAJ0543.soap.entity.refs.not.allowed.in.docs");
/* 148 */     throw new UnsupportedOperationException("Entity References are not allowed in SOAP documents");
/*     */   }
/*     */ 
/*     */   public NodeList getElementsByTagName(String tagname) {
/* 152 */     return super.getElementsByTagName(tagname);
/*     */   }
/*     */ 
/*     */   public Node importNode(Node importedNode, boolean deep) throws DOMException
/*     */   {
/* 157 */     return super.importNode(importedNode, deep);
/*     */   }
/*     */ 
/*     */   public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException
/*     */   {
/* 162 */     return ElementFactory.createElement(this, NameImpl.getLocalNameFromTagName(qualifiedName), NameImpl.getPrefixFromTagName(qualifiedName), namespaceURI);
/*     */   }
/*     */ 
/*     */   public Attr createAttributeNS(String namespaceURI, String qualifiedName)
/*     */     throws DOMException
/*     */   {
/* 171 */     return super.createAttributeNS(namespaceURI, qualifiedName);
/*     */   }
/*     */ 
/*     */   public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
/*     */   {
/* 177 */     return super.getElementsByTagNameNS(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public Element getElementById(String elementId) {
/* 181 */     return super.getElementById(elementId);
/*     */   }
/*     */ 
/*     */   public Node cloneNode(boolean deep) {
/* 185 */     SOAPPartImpl newSoapPart = getSOAPPart().doCloneNode();
/* 186 */     super.cloneNode(newSoapPart.getDocument(), deep);
/* 187 */     return newSoapPart;
/*     */   }
/*     */ 
/*     */   public void cloneNode(SOAPDocumentImpl newdoc, boolean deep) {
/* 191 */     super.cloneNode(newdoc, deep);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl
 * JD-Core Version:    0.6.2
 */