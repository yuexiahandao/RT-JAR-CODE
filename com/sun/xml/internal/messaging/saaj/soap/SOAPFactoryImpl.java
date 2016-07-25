/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.ElementFactory;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Detail;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFactory;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public abstract class SOAPFactoryImpl extends SOAPFactory
/*     */ {
/*  48 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap", "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
/*     */ 
/*     */   protected abstract SOAPDocumentImpl createDocument();
/*     */ 
/*     */   public SOAPElement createElement(String tagName) throws SOAPException
/*     */   {
/*  54 */     if (tagName == null) {
/*  55 */       log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[] { "tagName", "SOAPFactory.createElement" });
/*     */ 
/*  58 */       throw new SOAPException("Null tagName argument passed to createElement");
/*     */     }
/*  60 */     return ElementFactory.createElement(createDocument(), NameImpl.createFromTagName(tagName));
/*     */   }
/*     */ 
/*     */   public SOAPElement createElement(Name name)
/*     */     throws SOAPException
/*     */   {
/*  67 */     if (name == null) {
/*  68 */       log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[] { "name", "SOAPFactory.createElement" });
/*     */ 
/*  70 */       throw new SOAPException("Null name argument passed to createElement");
/*     */     }
/*  72 */     return ElementFactory.createElement(createDocument(), name);
/*     */   }
/*     */ 
/*     */   public SOAPElement createElement(QName qname) throws SOAPException {
/*  76 */     if (qname == null) {
/*  77 */       log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[] { "qname", "SOAPFactory.createElement" });
/*     */ 
/*  79 */       throw new SOAPException("Null qname argument passed to createElement");
/*     */     }
/*  81 */     return ElementFactory.createElement(createDocument(), qname);
/*     */   }
/*     */ 
/*     */   public SOAPElement createElement(String localName, String prefix, String uri)
/*     */     throws SOAPException
/*     */   {
/*  92 */     if (localName == null) {
/*  93 */       log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[] { "localName", "SOAPFactory.createElement" });
/*     */ 
/*  95 */       throw new SOAPException("Null localName argument passed to createElement");
/*     */     }
/*  97 */     return ElementFactory.createElement(createDocument(), localName, prefix, uri);
/*     */   }
/*     */ 
/*     */   public Name createName(String localName, String prefix, String uri)
/*     */     throws SOAPException
/*     */   {
/* 105 */     if (localName == null) {
/* 106 */       log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[] { "localName", "SOAPFactory.createName" });
/*     */ 
/* 109 */       throw new SOAPException("Null localName argument passed to createName");
/*     */     }
/* 111 */     return NameImpl.create(localName, prefix, uri);
/*     */   }
/*     */ 
/*     */   public Name createName(String localName)
/*     */     throws SOAPException
/*     */   {
/* 118 */     if (localName == null) {
/* 119 */       log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[] { "localName", "SOAPFactory.createName" });
/*     */ 
/* 122 */       throw new SOAPException("Null localName argument passed to createName");
/*     */     }
/* 124 */     return NameImpl.createFromUnqualifiedName(localName);
/*     */   }
/*     */ 
/*     */   public SOAPElement createElement(Element domElement)
/*     */     throws SOAPException
/*     */   {
/* 130 */     if (domElement == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     return convertToSoapElement(domElement);
/*     */   }
/*     */ 
/*     */   private SOAPElement convertToSoapElement(Element element) throws SOAPException
/*     */   {
/* 138 */     if ((element instanceof SOAPElement)) {
/* 139 */       return (SOAPElement)element;
/*     */     }
/*     */ 
/* 142 */     SOAPElement copy = createElement(element.getLocalName(), element.getPrefix(), element.getNamespaceURI());
/*     */ 
/* 147 */     Document ownerDoc = copy.getOwnerDocument();
/*     */ 
/* 149 */     NamedNodeMap attrMap = element.getAttributes();
/* 150 */     for (int i = 0; i < attrMap.getLength(); i++) {
/* 151 */       Attr nextAttr = (Attr)attrMap.item(i);
/* 152 */       Attr importedAttr = (Attr)ownerDoc.importNode(nextAttr, true);
/* 153 */       copy.setAttributeNodeNS(importedAttr);
/*     */     }
/*     */ 
/* 157 */     NodeList nl = element.getChildNodes();
/* 158 */     for (int i = 0; i < nl.getLength(); i++) {
/* 159 */       Node next = nl.item(i);
/* 160 */       Node imported = ownerDoc.importNode(next, true);
/* 161 */       copy.appendChild(imported);
/*     */     }
/*     */ 
/* 164 */     return copy;
/*     */   }
/*     */ 
/*     */   public Detail createDetail() throws SOAPException {
/* 168 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public SOAPFault createFault(String reasonText, QName faultCode) throws SOAPException {
/* 172 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public SOAPFault createFault() throws SOAPException {
/* 176 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl
 * JD-Core Version:    0.6.2
 */