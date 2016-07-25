/*     */ package com.sun.xml.internal.messaging.saaj.soap.ver1_2;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ 
/*     */ public class Envelope1_2Impl extends EnvelopeImpl
/*     */ {
/*  45 */   protected static final Logger log = Logger.getLogger(Envelope1_2Impl.class.getName(), "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");
/*     */ 
/*     */   public Envelope1_2Impl(SOAPDocumentImpl ownerDoc, String prefix)
/*     */   {
/*  50 */     super(ownerDoc, NameImpl.createEnvelope1_2Name(prefix));
/*     */   }
/*     */ 
/*     */   public Envelope1_2Impl(SOAPDocumentImpl ownerDoc, String prefix, boolean createHeader, boolean createBody)
/*     */     throws SOAPException
/*     */   {
/*  59 */     super(ownerDoc, NameImpl.createEnvelope1_2Name(prefix), createHeader, createBody);
/*     */   }
/*     */ 
/*     */   protected NameImpl getBodyName(String prefix)
/*     */   {
/*  67 */     return NameImpl.createBody1_2Name(prefix);
/*     */   }
/*     */ 
/*     */   protected NameImpl getHeaderName(String prefix) {
/*  71 */     return NameImpl.createHeader1_2Name(prefix);
/*     */   }
/*     */ 
/*     */   public void setEncodingStyle(String encodingStyle)
/*     */     throws SOAPException
/*     */   {
/*  79 */     log.severe("SAAJ0404.ver1_2.no.encodingStyle.in.envelope");
/*  80 */     throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Envelope");
/*     */   }
/*     */ 
/*     */   public SOAPElement addAttribute(Name name, String value)
/*     */     throws SOAPException
/*     */   {
/*  89 */     if ((name.getLocalName().equals("encodingStyle")) && (name.getURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*     */     {
/*  91 */       setEncodingStyle(value);
/*     */     }
/*  93 */     return super.addAttribute(name, value);
/*     */   }
/*     */ 
/*     */   public SOAPElement addAttribute(QName name, String value) throws SOAPException
/*     */   {
/*  98 */     if ((name.getLocalPart().equals("encodingStyle")) && (name.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*     */     {
/* 100 */       setEncodingStyle(value);
/*     */     }
/* 102 */     return super.addAttribute(name, value);
/*     */   }
/*     */ 
/*     */   public SOAPElement addChildElement(Name name)
/*     */     throws SOAPException
/*     */   {
/* 112 */     if (getBody() != null) {
/* 113 */       log.severe("SAAJ0405.ver1_2.body.must.last.in.envelope");
/* 114 */       throw new SOAPExceptionImpl("Body must be the last element in SOAP Envelope");
/*     */     }
/*     */ 
/* 117 */     return super.addChildElement(name);
/*     */   }
/*     */ 
/*     */   public SOAPElement addChildElement(QName name) throws SOAPException
/*     */   {
/* 122 */     if (getBody() != null) {
/* 123 */       log.severe("SAAJ0405.ver1_2.body.must.last.in.envelope");
/* 124 */       throw new SOAPExceptionImpl("Body must be the last element in SOAP Envelope");
/*     */     }
/*     */ 
/* 127 */     return super.addChildElement(name);
/*     */   }
/*     */ 
/*     */   public SOAPElement addTextNode(String text)
/*     */     throws SOAPException
/*     */   {
/* 141 */     log.log(Level.SEVERE, "SAAJ0416.ver1_2.adding.text.not.legal", getElementQName());
/*     */ 
/* 145 */     throw new SOAPExceptionImpl("Adding text to SOAP 1.2 Envelope is not legal");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_2.Envelope1_2Impl
 * JD-Core Version:    0.6.2
 */