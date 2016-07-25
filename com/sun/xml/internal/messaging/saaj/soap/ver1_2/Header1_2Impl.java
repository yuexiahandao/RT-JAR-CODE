/*     */ package com.sun.xml.internal.messaging.saaj.soap.ver1_2;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeaderElement;
/*     */ 
/*     */ public class Header1_2Impl extends HeaderImpl
/*     */ {
/*  51 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.ver1_2", "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");
/*     */ 
/*     */   public Header1_2Impl(SOAPDocumentImpl ownerDocument, String prefix)
/*     */   {
/*  57 */     super(ownerDocument, NameImpl.createHeader1_2Name(prefix));
/*     */   }
/*     */ 
/*     */   protected NameImpl getNotUnderstoodName() {
/*  61 */     return NameImpl.createNotUnderstood1_2Name(null);
/*     */   }
/*     */ 
/*     */   protected NameImpl getUpgradeName() {
/*  65 */     return NameImpl.createUpgrade1_2Name(null);
/*     */   }
/*     */ 
/*     */   protected NameImpl getSupportedEnvelopeName() {
/*  69 */     return NameImpl.createSupportedEnvelope1_2Name(null);
/*     */   }
/*     */ 
/*     */   public SOAPHeaderElement addNotUnderstoodHeaderElement(QName sourceName)
/*     */     throws SOAPException
/*     */   {
/*  75 */     if (sourceName == null) {
/*  76 */       log.severe("SAAJ0410.ver1_2.no.null.to.addNotUnderstoodHeader");
/*  77 */       throw new SOAPException("Cannot pass NULL to addNotUnderstoodHeaderElement");
/*     */     }
/*  79 */     if ("".equals(sourceName.getNamespaceURI())) {
/*  80 */       log.severe("SAAJ0417.ver1_2.qname.not.ns.qualified");
/*  81 */       throw new SOAPException("The qname passed to addNotUnderstoodHeaderElement must be namespace-qualified");
/*     */     }
/*  83 */     String prefix = sourceName.getPrefix();
/*  84 */     if ("".equals(prefix)) {
/*  85 */       prefix = "ns1";
/*     */     }
/*  87 */     Name notunderstoodName = getNotUnderstoodName();
/*  88 */     SOAPHeaderElement notunderstoodHeaderElement = (SOAPHeaderElement)addChildElement(notunderstoodName);
/*     */ 
/*  90 */     notunderstoodHeaderElement.addAttribute(NameImpl.createFromUnqualifiedName("qname"), getQualifiedName(new QName(sourceName.getNamespaceURI(), sourceName.getLocalPart(), prefix)));
/*     */ 
/*  97 */     notunderstoodHeaderElement.addNamespaceDeclaration(prefix, sourceName.getNamespaceURI());
/*     */ 
/* 100 */     return notunderstoodHeaderElement;
/*     */   }
/*     */ 
/*     */   public SOAPElement addTextNode(String text) throws SOAPException {
/* 104 */     log.log(Level.SEVERE, "SAAJ0416.ver1_2.adding.text.not.legal", getElementQName());
/*     */ 
/* 108 */     throw new SOAPExceptionImpl("Adding text to SOAP 1.2 Header is not legal");
/*     */   }
/*     */ 
/*     */   protected SOAPHeaderElement createHeaderElement(Name name) throws SOAPException
/*     */   {
/* 113 */     String uri = name.getURI();
/* 114 */     if ((uri == null) || (uri.equals(""))) {
/* 115 */       log.severe("SAAJ0413.ver1_2.header.elems.must.be.ns.qualified");
/* 116 */       throw new SOAPExceptionImpl("SOAP 1.2 header elements must be namespace qualified");
/*     */     }
/* 118 */     return new HeaderElement1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument(), name);
/*     */   }
/*     */ 
/*     */   protected SOAPHeaderElement createHeaderElement(QName name)
/*     */     throws SOAPException
/*     */   {
/* 125 */     String uri = name.getNamespaceURI();
/* 126 */     if ((uri == null) || (uri.equals(""))) {
/* 127 */       log.severe("SAAJ0413.ver1_2.header.elems.must.be.ns.qualified");
/* 128 */       throw new SOAPExceptionImpl("SOAP 1.2 header elements must be namespace qualified");
/*     */     }
/* 130 */     return new HeaderElement1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument(), name);
/*     */   }
/*     */ 
/*     */   public void setEncodingStyle(String encodingStyle)
/*     */     throws SOAPException
/*     */   {
/* 136 */     log.severe("SAAJ0409.ver1_2.no.encodingstyle.in.header");
/* 137 */     throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Header");
/*     */   }
/*     */ 
/*     */   public SOAPElement addAttribute(Name name, String value) throws SOAPException
/*     */   {
/* 142 */     if ((name.getLocalName().equals("encodingStyle")) && (name.getURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*     */     {
/* 145 */       setEncodingStyle(value);
/*     */     }
/* 147 */     return super.addAttribute(name, value);
/*     */   }
/*     */ 
/*     */   public SOAPElement addAttribute(QName name, String value) throws SOAPException
/*     */   {
/* 152 */     if ((name.getLocalPart().equals("encodingStyle")) && (name.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*     */     {
/* 155 */       setEncodingStyle(value);
/*     */     }
/* 157 */     return super.addAttribute(name, value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_2.Header1_2Impl
 * JD-Core Version:    0.6.2
 */