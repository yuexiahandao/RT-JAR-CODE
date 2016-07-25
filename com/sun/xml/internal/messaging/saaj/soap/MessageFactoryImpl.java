/*     */ package com.sun.xml.internal.messaging.saaj.soap;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Message1_1Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.ver1_2.Message1_2Impl;
/*     */ import com.sun.xml.internal.messaging.saaj.util.TeeInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.soap.MessageFactory;
/*     */ import javax.xml.soap.MimeHeaders;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ 
/*     */ public class MessageFactoryImpl extends MessageFactory
/*     */ {
/*  51 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap", "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
/*     */   protected OutputStream listener;
/*  57 */   protected boolean lazyAttachments = false;
/*     */ 
/*     */   public OutputStream listen(OutputStream newListener) {
/*  60 */     OutputStream oldListener = this.listener;
/*  61 */     this.listener = newListener;
/*  62 */     return oldListener;
/*     */   }
/*     */ 
/*     */   public SOAPMessage createMessage() throws SOAPException {
/*  66 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public SOAPMessage createMessage(boolean isFastInfoset, boolean acceptFastInfoset)
/*     */     throws SOAPException
/*     */   {
/*  72 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public SOAPMessage createMessage(MimeHeaders headers, InputStream in) throws SOAPException, IOException
/*     */   {
/*  77 */     String contentTypeString = MessageImpl.getContentType(headers);
/*     */ 
/*  79 */     if (this.listener != null) {
/*  80 */       in = new TeeInputStream(in, this.listener);
/*     */     }
/*     */     try
/*     */     {
/*  84 */       ContentType contentType = new ContentType(contentTypeString);
/*  85 */       int stat = MessageImpl.identifyContentType(contentType);
/*     */ 
/*  87 */       if (MessageImpl.isSoap1_1Content(stat))
/*  88 */         return new Message1_1Impl(headers, contentType, stat, in);
/*  89 */       if (MessageImpl.isSoap1_2Content(stat)) {
/*  90 */         return new Message1_2Impl(headers, contentType, stat, in);
/*     */       }
/*  92 */       log.severe("SAAJ0530.soap.unknown.Content-Type");
/*  93 */       throw new SOAPExceptionImpl("Unrecognized Content-Type");
/*     */     }
/*     */     catch (ParseException e) {
/*  96 */       log.severe("SAAJ0531.soap.cannot.parse.Content-Type");
/*  97 */       throw new SOAPExceptionImpl("Unable to parse content type: " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static final String getContentType(MimeHeaders headers)
/*     */   {
/* 103 */     String[] values = headers.getHeader("Content-Type");
/* 104 */     if (values == null) {
/* 105 */       return null;
/*     */     }
/* 107 */     return values[0];
/*     */   }
/*     */ 
/*     */   public void setLazyAttachmentOptimization(boolean flag) {
/* 111 */     this.lazyAttachments = flag;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.MessageFactoryImpl
 * JD-Core Version:    0.6.2
 */