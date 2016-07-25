/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_2;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.Name;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPException;
/*    */ 
/*    */ public class FaultElement1_2Impl extends FaultElementImpl
/*    */ {
/*    */   public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, NameImpl qname)
/*    */   {
/* 45 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, QName qname) {
/* 49 */     super(ownerDoc, qname);
/*    */   }
/*    */ 
/*    */   public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, String localName) {
/* 53 */     super(ownerDoc, NameImpl.createSOAP12Name(localName));
/*    */   }
/*    */ 
/*    */   protected boolean isStandardFaultElement() {
/* 57 */     String localName = this.elementQName.getLocalPart();
/* 58 */     if ((localName.equalsIgnoreCase("code")) || (localName.equalsIgnoreCase("reason")) || (localName.equalsIgnoreCase("node")) || (localName.equalsIgnoreCase("role")))
/*    */     {
/* 62 */       return true;
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   public SOAPElement setElementQName(QName newName) throws SOAPException {
/* 68 */     if (!isStandardFaultElement()) {
/* 69 */       FaultElement1_2Impl copy = new FaultElement1_2Impl((SOAPDocumentImpl)getOwnerDocument(), newName);
/*    */ 
/* 71 */       return replaceElementWithSOAPElement(this, copy);
/*    */     }
/* 73 */     return super.setElementQName(newName);
/*    */   }
/*    */ 
/*    */   public void setEncodingStyle(String encodingStyle) throws SOAPException
/*    */   {
/* 78 */     log.severe("SAAJ0408.ver1_2.no.encodingStyle.in.fault.child");
/* 79 */     throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on a Fault child element");
/*    */   }
/*    */ 
/*    */   public SOAPElement addAttribute(Name name, String value) throws SOAPException
/*    */   {
/* 84 */     if ((name.getLocalName().equals("encodingStyle")) && (name.getURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*    */     {
/* 86 */       setEncodingStyle(value);
/*    */     }
/* 88 */     return super.addAttribute(name, value);
/*    */   }
/*    */ 
/*    */   public SOAPElement addAttribute(QName name, String value) throws SOAPException
/*    */   {
/* 93 */     if ((name.getLocalPart().equals("encodingStyle")) && (name.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")))
/*    */     {
/* 95 */       setEncodingStyle(value);
/*    */     }
/* 97 */     return super.addAttribute(name, value);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_2.FaultElement1_2Impl
 * JD-Core Version:    0.6.2
 */