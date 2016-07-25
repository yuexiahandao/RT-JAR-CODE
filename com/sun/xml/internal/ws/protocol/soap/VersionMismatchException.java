/*    */ package com.sun.xml.internal.ws.protocol.soap;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.encoding.soap.SOAP12Constants;
/*    */ import com.sun.xml.internal.ws.encoding.soap.SOAPConstants;
/*    */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class VersionMismatchException extends ExceptionHasMessage
/*    */ {
/*    */   private final SOAPVersion soapVersion;
/*    */ 
/*    */   public VersionMismatchException(SOAPVersion soapVersion, Object[] args)
/*    */   {
/* 49 */     super("soap.version.mismatch.err", args);
/* 50 */     this.soapVersion = soapVersion;
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 54 */     return "com.sun.xml.internal.ws.resources.soap";
/*    */   }
/*    */ 
/*    */   public Message getFaultMessage() {
/* 58 */     QName faultCode = this.soapVersion == SOAPVersion.SOAP_11 ? SOAPConstants.FAULT_CODE_VERSION_MISMATCH : SOAP12Constants.FAULT_CODE_VERSION_MISMATCH;
/*    */ 
/* 61 */     return SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, getLocalizedMessage(), faultCode);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.protocol.soap.VersionMismatchException
 * JD-Core Version:    0.6.2
 */