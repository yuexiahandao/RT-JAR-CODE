/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*    */ import java.util.Locale;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.Name;
/*    */ import javax.xml.soap.SOAPBodyElement;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPFault;
/*    */ 
/*    */ public class Body1_1Impl extends BodyImpl
/*    */ {
/*    */   public Body1_1Impl(SOAPDocumentImpl ownerDocument, String prefix)
/*    */   {
/* 44 */     super(ownerDocument, NameImpl.createBody1_1Name(prefix));
/*    */   }
/*    */ 
/*    */   public SOAPFault addSOAP12Fault(QName faultCode, String faultReason, Locale locale)
/*    */   {
/* 49 */     throw new UnsupportedOperationException("Not supported in SOAP 1.1");
/*    */   }
/*    */ 
/*    */   protected NameImpl getFaultName(String name)
/*    */   {
/* 54 */     return NameImpl.createFault1_1Name(null);
/*    */   }
/*    */ 
/*    */   protected SOAPBodyElement createBodyElement(Name name) {
/* 58 */     return new BodyElement1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument(), name);
/*    */   }
/*    */ 
/*    */   protected SOAPBodyElement createBodyElement(QName name)
/*    */   {
/* 64 */     return new BodyElement1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument(), name);
/*    */   }
/*    */ 
/*    */   protected QName getDefaultFaultCode()
/*    */   {
/* 70 */     return new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server");
/*    */   }
/*    */ 
/*    */   protected boolean isFault(SOAPElement child)
/*    */   {
/* 75 */     return child.getElementName().equals(getFaultName(null));
/*    */   }
/*    */ 
/*    */   protected SOAPFault createFaultElement() {
/* 79 */     return new Fault1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument(), getPrefix());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.Body1_1Impl
 * JD-Core Version:    0.6.2
 */