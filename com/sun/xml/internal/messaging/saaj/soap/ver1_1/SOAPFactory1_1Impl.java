/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.Detail;
/*    */ import javax.xml.soap.SOAPException;
/*    */ import javax.xml.soap.SOAPFault;
/*    */ 
/*    */ public class SOAPFactory1_1Impl extends SOAPFactoryImpl
/*    */ {
/*    */   protected SOAPDocumentImpl createDocument()
/*    */   {
/* 43 */     return new SOAPPart1_1Impl().getDocument();
/*    */   }
/*    */ 
/*    */   public Detail createDetail() throws SOAPException {
/* 47 */     return new Detail1_1Impl(createDocument());
/*    */   }
/*    */ 
/*    */   public SOAPFault createFault(String reasonText, QName faultCode) throws SOAPException
/*    */   {
/* 52 */     if (faultCode == null) {
/* 53 */       throw new IllegalArgumentException("faultCode argument for createFault was passed NULL");
/*    */     }
/* 55 */     if (reasonText == null) {
/* 56 */       throw new IllegalArgumentException("reasonText argument for createFault was passed NULL");
/*    */     }
/* 58 */     Fault1_1Impl fault = new Fault1_1Impl(createDocument(), null);
/* 59 */     fault.setFaultCode(faultCode);
/* 60 */     fault.setFaultString(reasonText);
/* 61 */     return fault;
/*    */   }
/*    */ 
/*    */   public SOAPFault createFault() throws SOAPException {
/* 65 */     Fault1_1Impl fault = new Fault1_1Impl(createDocument(), null);
/* 66 */     fault.setFaultCode(fault.getDefaultFaultCode());
/* 67 */     fault.setFaultString("Fault string, and possibly fault code, not set");
/* 68 */     return fault;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl
 * JD-Core Version:    0.6.2
 */