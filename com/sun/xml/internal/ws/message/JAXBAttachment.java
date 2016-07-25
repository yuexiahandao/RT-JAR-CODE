/*     */ package com.sun.xml.internal.ws.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.encoding.DataSourceStreamingDataHandler;
/*     */ import com.sun.xml.internal.ws.util.ByteArrayBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.soap.AttachmentPart;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class JAXBAttachment
/*     */   implements Attachment, DataSource
/*     */ {
/*     */   private final String contentId;
/*     */   private final String mimeType;
/*     */   private final Object jaxbObject;
/*     */   private final Bridge bridge;
/*     */ 
/*     */   public JAXBAttachment(@NotNull String contentId, Object jaxbObject, Bridge bridge, String mimeType)
/*     */   {
/*  59 */     this.contentId = contentId;
/*  60 */     this.jaxbObject = jaxbObject;
/*  61 */     this.bridge = bridge;
/*  62 */     this.mimeType = mimeType;
/*     */   }
/*     */ 
/*     */   public String getContentId() {
/*  66 */     return this.contentId;
/*     */   }
/*     */ 
/*     */   public String getContentType() {
/*  70 */     return this.mimeType;
/*     */   }
/*     */ 
/*     */   public byte[] asByteArray() {
/*  74 */     ByteArrayBuffer bab = new ByteArrayBuffer();
/*     */     try {
/*  76 */       writeTo(bab);
/*     */     } catch (IOException e) {
/*  78 */       throw new WebServiceException(e);
/*     */     }
/*  80 */     return bab.getRawData();
/*     */   }
/*     */ 
/*     */   public DataHandler asDataHandler() {
/*  84 */     return new DataSourceStreamingDataHandler(this);
/*     */   }
/*     */ 
/*     */   public Source asSource() {
/*  88 */     return new StreamSource(asInputStream());
/*     */   }
/*     */ 
/*     */   public InputStream asInputStream() {
/*  92 */     ByteArrayBuffer bab = new ByteArrayBuffer();
/*     */     try {
/*  94 */       writeTo(bab);
/*     */     } catch (IOException e) {
/*  96 */       throw new WebServiceException(e);
/*     */     }
/*  98 */     return bab.newInputStream();
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream os) throws IOException {
/*     */     try {
/* 103 */       this.bridge.marshal(this.jaxbObject, os, null);
/*     */     } catch (JAXBException e) {
/* 105 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj) throws SOAPException {
/* 110 */     AttachmentPart part = saaj.createAttachmentPart();
/* 111 */     part.setDataHandler(asDataHandler());
/* 112 */     part.setContentId(this.contentId);
/* 113 */     saaj.addAttachmentPart(part);
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream() throws IOException {
/* 117 */     return asInputStream();
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream() throws IOException {
/* 121 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 125 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.JAXBAttachment
 * JD-Core Version:    0.6.2
 */