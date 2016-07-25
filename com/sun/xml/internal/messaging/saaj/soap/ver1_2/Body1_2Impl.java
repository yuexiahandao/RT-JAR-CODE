/*     */ package com.sun.xml.internal.messaging.saaj.soap.ver1_2;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPBodyElement;
/*     */ import javax.xml.soap.SOAPConstants;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class Body1_2Impl extends BodyImpl
/*     */ {
/*  48 */   protected static final Logger log = Logger.getLogger(Body1_2Impl.class.getName(), "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");
/*     */ 
/*     */   public Body1_2Impl(SOAPDocumentImpl ownerDocument, String prefix)
/*     */   {
/*  53 */     super(ownerDocument, NameImpl.createBody1_2Name(prefix));
/*     */   }
/*     */ 
/*     */   protected NameImpl getFaultName(String name) {
/*  57 */     return NameImpl.createFault1_2Name(name, null);
/*     */   }
/*     */ 
/*     */   protected SOAPBodyElement createBodyElement(Name name) {
/*  61 */     return new BodyElement1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument(), name);
/*     */   }
/*     */ 
/*     */   protected SOAPBodyElement createBodyElement(QName name)
/*     */   {
/*  66 */     return new BodyElement1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument(), name);
/*     */   }
/*     */ 
/*     */   protected QName getDefaultFaultCode()
/*     */   {
/*  72 */     return SOAPConstants.SOAP_RECEIVER_FAULT;
/*     */   }
/*     */ 
/*     */   public SOAPFault addFault() throws SOAPException {
/*  76 */     if (hasAnyChildElement()) {
/*  77 */       log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
/*  78 */       throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
/*     */     }
/*     */ 
/*  81 */     return super.addFault();
/*     */   }
/*     */ 
/*     */   public void setEncodingStyle(String encodingStyle)
/*     */     throws SOAPException
/*     */   {
/*  89 */     log.severe("SAAJ0401.ver1_2.no.encodingstyle.in.body");
/*  90 */     throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Body");
/*     */   }
/*     */ 
/*     */   public SOAPElement addAttribute(Name name, String value)
/*     */     throws SOAPException
/*     */   {
/*  99 */     if ((name.getLocalName().equals("encodingStyle")) && (name.getURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*     */     {
/* 102 */       setEncodingStyle(value);
/*     */     }
/* 104 */     return super.addAttribute(name, value);
/*     */   }
/*     */ 
/*     */   public SOAPElement addAttribute(QName name, String value) throws SOAPException
/*     */   {
/* 109 */     if ((name.getLocalPart().equals("encodingStyle")) && (name.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*     */     {
/* 112 */       setEncodingStyle(value);
/*     */     }
/* 114 */     return super.addAttribute(name, value);
/*     */   }
/*     */ 
/*     */   protected boolean isFault(SOAPElement child) {
/* 118 */     return (child.getElementName().getURI().equals("http://www.w3.org/2003/05/soap-envelope")) && (child.getElementName().getLocalName().equals("Fault"));
/*     */   }
/*     */ 
/*     */   protected SOAPFault createFaultElement()
/*     */   {
/* 125 */     return new Fault1_2Impl(((SOAPDocument)getOwnerDocument()).getDocument(), getPrefix());
/*     */   }
/*     */ 
/*     */   public SOAPBodyElement addBodyElement(Name name)
/*     */     throws SOAPException
/*     */   {
/* 135 */     if (hasFault()) {
/* 136 */       log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
/* 137 */       throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
/*     */     }
/*     */ 
/* 140 */     return super.addBodyElement(name);
/*     */   }
/*     */ 
/*     */   public SOAPBodyElement addBodyElement(QName name) throws SOAPException {
/* 144 */     if (hasFault()) {
/* 145 */       log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
/* 146 */       throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
/*     */     }
/*     */ 
/* 149 */     return super.addBodyElement(name);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(Name name) throws SOAPException {
/* 153 */     if (hasFault()) {
/* 154 */       log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
/* 155 */       throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
/*     */     }
/*     */ 
/* 158 */     return super.addElement(name);
/*     */   }
/*     */ 
/*     */   protected SOAPElement addElement(QName name) throws SOAPException {
/* 162 */     if (hasFault()) {
/* 163 */       log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
/* 164 */       throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
/*     */     }
/*     */ 
/* 167 */     return super.addElement(name);
/*     */   }
/*     */ 
/*     */   public SOAPElement addChildElement(Name name) throws SOAPException {
/* 171 */     if (hasFault()) {
/* 172 */       log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
/* 173 */       throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
/*     */     }
/*     */ 
/* 176 */     return super.addChildElement(name);
/*     */   }
/*     */ 
/*     */   public SOAPElement addChildElement(QName name) throws SOAPException {
/* 180 */     if (hasFault()) {
/* 181 */       log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
/* 182 */       throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
/*     */     }
/*     */ 
/* 185 */     return super.addChildElement(name);
/*     */   }
/*     */ 
/*     */   private boolean hasAnyChildElement() {
/* 189 */     Node currentNode = getFirstChild();
/* 190 */     while (currentNode != null) {
/* 191 */       if (currentNode.getNodeType() == 1)
/* 192 */         return true;
/* 193 */       currentNode = currentNode.getNextSibling();
/*     */     }
/* 195 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_2.Body1_2Impl
 * JD-Core Version:    0.6.2
 */