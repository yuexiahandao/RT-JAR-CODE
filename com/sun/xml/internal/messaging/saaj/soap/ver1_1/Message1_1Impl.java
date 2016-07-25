/*     */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.soap.MimeHeaders;
/*     */ import javax.xml.soap.SOAPConstants;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ 
/*     */ public class Message1_1Impl extends MessageImpl
/*     */   implements SOAPConstants
/*     */ {
/*  46 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.ver1_1", "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");
/*     */ 
/*     */   public Message1_1Impl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Message1_1Impl(boolean isFastInfoset, boolean acceptFastInfoset)
/*     */   {
/*  55 */     super(isFastInfoset, acceptFastInfoset);
/*     */   }
/*     */ 
/*     */   public Message1_1Impl(SOAPMessage msg) {
/*  59 */     super(msg);
/*     */   }
/*     */ 
/*     */   public Message1_1Impl(MimeHeaders headers, InputStream in)
/*     */     throws IOException, SOAPExceptionImpl
/*     */   {
/*  65 */     super(headers, in);
/*     */   }
/*     */ 
/*     */   public Message1_1Impl(MimeHeaders headers, ContentType ct, int stat, InputStream in) throws SOAPExceptionImpl
/*     */   {
/*  70 */     super(headers, ct, stat, in);
/*     */   }
/*     */ 
/*     */   public SOAPPart getSOAPPart() {
/*  74 */     if (this.soapPartImpl == null) {
/*  75 */       this.soapPartImpl = new SOAPPart1_1Impl(this);
/*     */     }
/*  77 */     return this.soapPartImpl;
/*     */   }
/*     */ 
/*     */   protected boolean isCorrectSoapVersion(int contentTypeId) {
/*  81 */     return (contentTypeId & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public String getAction() {
/*  85 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", new String[] { "Action" });
/*     */ 
/*  89 */     throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public void setAction(String type) {
/*  93 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", new String[] { "Action" });
/*     */ 
/*  97 */     throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public String getCharset() {
/* 101 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", new String[] { "Charset" });
/*     */ 
/* 105 */     throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
/*     */   }
/*     */ 
/*     */   public void setCharset(String charset) {
/* 109 */     log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", new String[] { "Charset" });
/*     */ 
/* 113 */     throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
/*     */   }
/*     */ 
/*     */   protected String getExpectedContentType() {
/* 117 */     return this.isFastInfoset ? "application/fastinfoset" : "text/xml";
/*     */   }
/*     */ 
/*     */   protected String getExpectedAcceptHeader() {
/* 121 */     String accept = "text/xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
/* 122 */     return this.acceptFastInfoset ? "application/fastinfoset, " + accept : accept;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.Message1_1Impl
 * JD-Core Version:    0.6.2
 */