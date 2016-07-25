/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.Name;
/*    */ import javax.xml.soap.SOAPException;
/*    */ import javax.xml.soap.SOAPHeaderElement;
/*    */ 
/*    */ public class Header1_1Impl extends HeaderImpl
/*    */ {
/* 48 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.ver1_1", "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");
/*    */ 
/*    */   public Header1_1Impl(SOAPDocumentImpl ownerDocument, String prefix)
/*    */   {
/* 53 */     super(ownerDocument, NameImpl.createHeader1_1Name(prefix));
/*    */   }
/*    */ 
/*    */   protected NameImpl getNotUnderstoodName() {
/* 57 */     log.log(Level.SEVERE, "SAAJ0301.ver1_1.hdr.op.unsupported.in.SOAP1.1", new String[] { "getNotUnderstoodName" });
/*    */ 
/* 61 */     throw new UnsupportedOperationException("Not supported by SOAP 1.1");
/*    */   }
/*    */ 
/*    */   protected NameImpl getUpgradeName() {
/* 65 */     return NameImpl.create("Upgrade", getPrefix(), "http://schemas.xmlsoap.org/soap/envelope/");
/*    */   }
/*    */ 
/*    */   protected NameImpl getSupportedEnvelopeName()
/*    */   {
/* 72 */     return NameImpl.create("SupportedEnvelope", getPrefix(), "http://schemas.xmlsoap.org/soap/envelope/");
/*    */   }
/*    */ 
/*    */   public SOAPHeaderElement addNotUnderstoodHeaderElement(QName name)
/*    */     throws SOAPException
/*    */   {
/* 80 */     log.log(Level.SEVERE, "SAAJ0301.ver1_1.hdr.op.unsupported.in.SOAP1.1", new String[] { "addNotUnderstoodHeaderElement" });
/*    */ 
/* 84 */     throw new UnsupportedOperationException("Not supported by SOAP 1.1");
/*    */   }
/*    */ 
/*    */   protected SOAPHeaderElement createHeaderElement(Name name) {
/* 88 */     return new HeaderElement1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument(), name);
/*    */   }
/*    */ 
/*    */   protected SOAPHeaderElement createHeaderElement(QName name)
/*    */   {
/* 93 */     return new HeaderElement1_1Impl(((SOAPDocument)getOwnerDocument()).getDocument(), name);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.Header1_1Impl
 * JD-Core Version:    0.6.2
 */