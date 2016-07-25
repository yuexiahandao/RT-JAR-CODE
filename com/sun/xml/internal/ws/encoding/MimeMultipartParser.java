/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
/*     */ import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.developer.StreamingAttachmentFeature;
/*     */ import com.sun.xml.internal.ws.util.ByteArrayBuffer;
/*     */ import com.sun.xml.internal.ws.util.ByteArrayDataSource;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.soap.AttachmentPart;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class MimeMultipartParser
/*     */ {
/*     */   private final String start;
/*     */   private final MIMEMessage message;
/*     */   private Attachment root;
/*  67 */   private final Map<String, Attachment> attachments = new HashMap();
/*     */   private boolean gotAll;
/*     */ 
/*     */   public MimeMultipartParser(InputStream in, String contentType, StreamingAttachmentFeature feature)
/*     */   {
/*  72 */     ContentType ct = new ContentType(contentType);
/*  73 */     String boundary = ct.getParameter("boundary");
/*  74 */     if ((boundary == null) || (boundary.equals(""))) {
/*  75 */       throw new WebServiceException("MIME boundary parameter not found" + contentType);
/*     */     }
/*  77 */     this.message = (feature != null ? new MIMEMessage(in, boundary, feature.getConfig()) : new MIMEMessage(in, boundary));
/*     */ 
/*  81 */     String st = ct.getParameter("start");
/*  82 */     if ((st != null) && (st.length() > 2) && (st.charAt(0) == '<') && (st.charAt(st.length() - 1) == '>')) {
/*  83 */       st = st.substring(1, st.length() - 1);
/*     */     }
/*  85 */     this.start = st;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public Attachment getRootPart()
/*     */   {
/*  98 */     if (this.root == null) {
/*  99 */       this.root = new PartAttachment(this.start != null ? this.message.getPart(this.start) : this.message.getPart(0));
/*     */     }
/* 101 */     return this.root;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Map<String, Attachment> getAttachmentParts()
/*     */   {
/* 110 */     if (!this.gotAll) {
/* 111 */       MIMEPart rootPart = this.start != null ? this.message.getPart(this.start) : this.message.getPart(0);
/* 112 */       List parts = this.message.getAttachments();
/* 113 */       for (MIMEPart part : parts) {
/* 114 */         if (part != rootPart) {
/* 115 */           PartAttachment attach = new PartAttachment(part);
/* 116 */           this.attachments.put(attach.getContentId(), attach);
/*     */         }
/*     */       }
/* 119 */       this.gotAll = true;
/*     */     }
/* 121 */     return this.attachments;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public Attachment getAttachmentPart(String contentId)
/*     */     throws IOException
/*     */   {
/* 133 */     Attachment attach = (Attachment)this.attachments.get(contentId);
/* 134 */     if (attach == null) {
/* 135 */       MIMEPart part = this.message.getPart(contentId);
/* 136 */       attach = new PartAttachment(part);
/* 137 */       this.attachments.put(contentId, attach);
/*     */     }
/* 139 */     return attach;
/*     */   }
/*     */ 
/*     */   static class PartAttachment implements Attachment {
/*     */     final MIMEPart part;
/*     */     byte[] buf;
/*     */ 
/*     */     PartAttachment(MIMEPart part) {
/* 148 */       this.part = part;
/*     */     }
/*     */     @NotNull
/*     */     public String getContentId() {
/* 152 */       return this.part.getContentId();
/*     */     }
/*     */     @NotNull
/*     */     public String getContentType() {
/* 156 */       return this.part.getContentType();
/*     */     }
/*     */ 
/*     */     public byte[] asByteArray() {
/* 160 */       if (this.buf == null) {
/* 161 */         ByteArrayBuffer baf = new ByteArrayBuffer();
/*     */         try {
/* 163 */           baf.write(this.part.readOnce());
/*     */         } catch (IOException ioe) {
/* 165 */           throw new WebServiceException(ioe);
/*     */         }
/* 167 */         this.buf = baf.toByteArray();
/*     */       }
/* 169 */       return this.buf;
/*     */     }
/*     */ 
/*     */     public DataHandler asDataHandler() {
/* 173 */       return this.buf != null ? new DataSourceStreamingDataHandler(new ByteArrayDataSource(this.buf, getContentType())) : new MIMEPartStreamingDataHandler(this.part);
/*     */     }
/*     */ 
/*     */     public Source asSource()
/*     */     {
/* 179 */       return this.buf != null ? new StreamSource(new ByteArrayInputStream(this.buf)) : new StreamSource(this.part.read());
/*     */     }
/*     */ 
/*     */     public InputStream asInputStream()
/*     */     {
/* 185 */       return this.buf != null ? new ByteArrayInputStream(this.buf) : this.part.read();
/*     */     }
/*     */ 
/*     */     public void writeTo(OutputStream os) throws IOException
/*     */     {
/* 190 */       if (this.buf != null) {
/* 191 */         os.write(this.buf);
/*     */       } else {
/* 193 */         InputStream in = this.part.read();
/* 194 */         byte[] temp = new byte[8192];
/*     */         int len;
/* 196 */         while ((len = in.read(temp)) != -1) {
/* 197 */           os.write(temp, 0, len);
/*     */         }
/* 199 */         in.close();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeTo(SOAPMessage saaj) throws SOAPException {
/* 204 */       saaj.createAttachmentPart().setDataHandler(asDataHandler());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.MimeMultipartParser
 * JD-Core Version:    0.6.2
 */