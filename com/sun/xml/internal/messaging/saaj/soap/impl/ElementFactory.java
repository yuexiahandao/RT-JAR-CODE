/*     */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Body1_1Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Detail1_1Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Envelope1_1Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Fault1_1Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_1.FaultElement1_1Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Header1_1Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPPart1_1Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_2.Body1_2Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_2.Detail1_2Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_2.Envelope1_2Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_2.Fault1_2Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_2.Header1_2Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_2.SOAPPart1_2Impl;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ 
/*     */ public class ElementFactory
/*     */ {
/*     */   public static SOAPElement createElement(SOAPDocumentImpl ownerDocument, Name name)
/*     */   {
/*  41 */     return createElement(ownerDocument, name.getLocalName(), name.getPrefix(), name.getURI());
/*     */   }
/*     */ 
/*     */   public static SOAPElement createElement(SOAPDocumentImpl ownerDocument, QName name)
/*     */   {
/*  50 */     return createElement(ownerDocument, name.getLocalPart(), name.getPrefix(), name.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   public static SOAPElement createElement(SOAPDocumentImpl ownerDocument, String localName, String prefix, String namespaceUri)
/*     */   {
/*  64 */     if (ownerDocument == null) {
/*  65 */       if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceUri))
/*  66 */         ownerDocument = new SOAPPart1_1Impl().getDocument();
/*  67 */       else if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceUri))
/*  68 */         ownerDocument = new SOAPPart1_2Impl().getDocument();
/*     */       else {
/*  70 */         ownerDocument = new SOAPDocumentImpl(null);
/*     */       }
/*     */     }
/*     */ 
/*  74 */     SOAPElement newElement = createNamedElement(ownerDocument, localName, prefix, namespaceUri);
/*     */ 
/*  77 */     return newElement != null ? newElement : new ElementImpl(ownerDocument, namespaceUri, NameImpl.createQName(prefix, localName));
/*     */   }
/*     */ 
/*     */   public static SOAPElement createNamedElement(SOAPDocumentImpl ownerDocument, String localName, String prefix, String namespaceUri)
/*     */   {
/*  91 */     if (prefix == null) {
/*  92 */       prefix = "SOAP-ENV";
/*     */     }
/*     */ 
/*  95 */     if (localName.equalsIgnoreCase("Envelope")) {
/*  96 */       if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceUri))
/*  97 */         return new Envelope1_1Impl(ownerDocument, prefix);
/*  98 */       if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceUri)) {
/*  99 */         return new Envelope1_2Impl(ownerDocument, prefix);
/*     */       }
/*     */     }
/* 102 */     if (localName.equalsIgnoreCase("Body")) {
/* 103 */       if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceUri))
/* 104 */         return new Body1_1Impl(ownerDocument, prefix);
/* 105 */       if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceUri)) {
/* 106 */         return new Body1_2Impl(ownerDocument, prefix);
/*     */       }
/*     */     }
/* 109 */     if (localName.equalsIgnoreCase("Header")) {
/* 110 */       if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceUri))
/* 111 */         return new Header1_1Impl(ownerDocument, prefix);
/* 112 */       if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceUri)) {
/* 113 */         return new Header1_2Impl(ownerDocument, prefix);
/*     */       }
/*     */     }
/* 116 */     if (localName.equalsIgnoreCase("Fault")) {
/* 117 */       SOAPFault fault = null;
/* 118 */       if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceUri))
/* 119 */         fault = new Fault1_1Impl(ownerDocument, prefix);
/* 120 */       else if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceUri)) {
/* 121 */         fault = new Fault1_2Impl(ownerDocument, prefix);
/*     */       }
/*     */ 
/* 124 */       if (fault != null)
/*     */       {
/* 138 */         return fault;
/*     */       }
/*     */     }
/*     */ 
/* 142 */     if (localName.equalsIgnoreCase("Detail")) {
/* 143 */       if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceUri))
/* 144 */         return new Detail1_1Impl(ownerDocument, prefix);
/* 145 */       if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceUri)) {
/* 146 */         return new Detail1_2Impl(ownerDocument, prefix);
/*     */       }
/*     */     }
/* 149 */     if ((localName.equalsIgnoreCase("faultcode")) || (localName.equalsIgnoreCase("faultstring")) || (localName.equalsIgnoreCase("faultactor")))
/*     */     {
/* 154 */       if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceUri)) {
/* 155 */         return new FaultElement1_1Impl(ownerDocument, localName, prefix);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   protected static void invalidCreate(String msg) {
/* 165 */     throw new TreeException(msg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.ElementFactory
 * JD-Core Version:    0.6.2
 */