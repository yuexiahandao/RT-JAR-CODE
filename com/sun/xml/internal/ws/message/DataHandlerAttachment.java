/*     */ package com.sun.xml.internal.ws.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.soap.AttachmentPart;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class DataHandlerAttachment
/*     */   implements Attachment
/*     */ {
/*     */   private final DataHandler dh;
/*     */   private final String contentId;
/*     */ 
/*     */   public DataHandlerAttachment(@NotNull String contentId, @NotNull DataHandler dh)
/*     */   {
/*  56 */     this.dh = dh;
/*  57 */     this.contentId = contentId;
/*     */   }
/*     */ 
/*     */   public String getContentId() {
/*  61 */     return this.contentId;
/*     */   }
/*     */ 
/*     */   public String getContentType() {
/*  65 */     return this.dh.getContentType();
/*     */   }
/*     */ 
/*     */   public byte[] asByteArray() {
/*     */     try {
/*  70 */       ByteArrayOutputStream os = new ByteArrayOutputStream();
/*  71 */       this.dh.writeTo(os);
/*  72 */       return os.toByteArray();
/*     */     } catch (IOException e) {
/*  74 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DataHandler asDataHandler() {
/*  79 */     return this.dh;
/*     */   }
/*     */ 
/*     */   public Source asSource() {
/*     */     try {
/*  84 */       return new StreamSource(this.dh.getInputStream());
/*     */     } catch (IOException e) {
/*  86 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream asInputStream() {
/*     */     try {
/*  92 */       return this.dh.getInputStream();
/*     */     } catch (IOException e) {
/*  94 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream os) throws IOException {
/*  99 */     this.dh.writeTo(os);
/*     */   }
/*     */ 
/*     */   public void writeTo(SOAPMessage saaj) throws SOAPException {
/* 103 */     AttachmentPart part = saaj.createAttachmentPart();
/* 104 */     part.setDataHandler(this.dh);
/* 105 */     part.setContentId(this.contentId);
/* 106 */     saaj.addAttachmentPart(part);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.DataHandlerAttachment
 * JD-Core Version:    0.6.2
 */