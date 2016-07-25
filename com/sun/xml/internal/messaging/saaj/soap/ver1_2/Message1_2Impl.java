/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_2;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.xml.soap.MimeHeaders;
/*    */ import javax.xml.soap.SOAPConstants;
/*    */ import javax.xml.soap.SOAPMessage;
/*    */ import javax.xml.soap.SOAPPart;
/*    */ 
/*    */ public class Message1_2Impl extends MessageImpl
/*    */   implements SOAPConstants
/*    */ {
/*    */   public Message1_2Impl()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Message1_2Impl(SOAPMessage msg)
/*    */   {
/* 48 */     super(msg);
/*    */   }
/*    */ 
/*    */   public Message1_2Impl(boolean isFastInfoset, boolean acceptFastInfoset) {
/* 52 */     super(isFastInfoset, acceptFastInfoset);
/*    */   }
/*    */ 
/*    */   public Message1_2Impl(MimeHeaders headers, InputStream in)
/*    */     throws IOException, SOAPExceptionImpl
/*    */   {
/* 58 */     super(headers, in);
/*    */   }
/*    */ 
/*    */   public Message1_2Impl(MimeHeaders headers, ContentType ct, int stat, InputStream in) throws SOAPExceptionImpl
/*    */   {
/* 63 */     super(headers, ct, stat, in);
/*    */   }
/*    */ 
/*    */   public SOAPPart getSOAPPart() {
/* 67 */     if (this.soapPartImpl == null) {
/* 68 */       this.soapPartImpl = new SOAPPart1_2Impl(this);
/*    */     }
/* 70 */     return this.soapPartImpl;
/*    */   }
/*    */ 
/*    */   protected boolean isCorrectSoapVersion(int contentTypeId) {
/* 74 */     return (contentTypeId & 0x8) != 0;
/*    */   }
/*    */ 
/*    */   protected String getExpectedContentType() {
/* 78 */     return this.isFastInfoset ? "application/soap+fastinfoset" : "application/soap+xml";
/*    */   }
/*    */ 
/*    */   protected String getExpectedAcceptHeader() {
/* 82 */     String accept = "application/soap+xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
/* 83 */     return this.acceptFastInfoset ? "application/soap+fastinfoset, " + accept : accept;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_2.Message1_2Impl
 * JD-Core Version:    0.6.2
 */