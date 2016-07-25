/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*     */ import com.sun.xml.internal.ws.developer.StreamingAttachmentFeature;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.util.UUID;
/*     */ 
/*     */ abstract class MimeCodec
/*     */   implements Codec
/*     */ {
/*     */   public static final String MULTIPART_RELATED_MIME_TYPE = "multipart/related";
/*     */   private String boundary;
/*     */   private String messageContentType;
/*     */   private boolean hasAttachments;
/*     */   protected Codec rootCodec;
/*     */   protected final SOAPVersion version;
/*     */   protected final WSBinding binding;
/*     */ 
/*     */   protected MimeCodec(SOAPVersion version, WSBinding binding)
/*     */   {
/*  72 */     this.version = version;
/*  73 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */   public String getMimeType() {
/*  77 */     return "multipart/related";
/*     */   }
/*     */ 
/*     */   public ContentType encode(Packet packet, OutputStream out)
/*     */     throws IOException
/*     */   {
/*  83 */     Message msg = packet.getMessage();
/*  84 */     if (msg == null) {
/*  85 */       return null;
/*     */     }
/*     */ 
/*  88 */     if (this.hasAttachments) {
/*  89 */       writeln("--" + this.boundary, out);
/*  90 */       ContentType ct = this.rootCodec.getStaticContentType(packet);
/*  91 */       String ctStr = ct != null ? ct.getContentType() : this.rootCodec.getMimeType();
/*  92 */       writeln("Content-Type: " + ctStr, out);
/*  93 */       writeln(out);
/*     */     }
/*  95 */     ContentType primaryCt = this.rootCodec.encode(packet, out);
/*     */ 
/*  97 */     if (this.hasAttachments) {
/*  98 */       writeln(out);
/*     */ 
/* 100 */       for (Attachment att : msg.getAttachments()) {
/* 101 */         writeln("--" + this.boundary, out);
/*     */ 
/* 104 */         String cid = att.getContentId();
/* 105 */         if ((cid != null) && (cid.length() > 0) && (cid.charAt(0) != '<'))
/* 106 */           cid = '<' + cid + '>';
/* 107 */         writeln("Content-Id:" + cid, out);
/* 108 */         writeln("Content-Type: " + att.getContentType(), out);
/* 109 */         writeln("Content-Transfer-Encoding: binary", out);
/* 110 */         writeln(out);
/* 111 */         att.writeTo(out);
/* 112 */         writeln(out);
/*     */       }
/* 114 */       writeAsAscii("--" + this.boundary, out);
/* 115 */       writeAsAscii("--", out);
/*     */     }
/*     */ 
/* 118 */     return this.hasAttachments ? new ContentTypeImpl(this.messageContentType, packet.soapAction, null) : primaryCt;
/*     */   }
/*     */ 
/*     */   public ContentType getStaticContentType(Packet packet) {
/* 122 */     Message msg = packet.getMessage();
/* 123 */     this.hasAttachments = (!msg.getAttachments().isEmpty());
/*     */ 
/* 125 */     if (this.hasAttachments) {
/* 126 */       this.boundary = ("uuid:" + UUID.randomUUID().toString());
/* 127 */       String boundaryParameter = "boundary=\"" + this.boundary + "\"";
/*     */ 
/* 129 */       this.messageContentType = ("multipart/related; type=\"" + this.rootCodec.getMimeType() + "\"; " + boundaryParameter);
/*     */ 
/* 132 */       return new ContentTypeImpl(this.messageContentType, packet.soapAction, null);
/*     */     }
/* 134 */     return this.rootCodec.getStaticContentType(packet);
/*     */   }
/*     */ 
/*     */   protected MimeCodec(MimeCodec that)
/*     */   {
/* 142 */     this.version = that.version;
/* 143 */     this.binding = that.binding;
/*     */   }
/*     */ 
/*     */   public void decode(InputStream in, String contentType, Packet packet) throws IOException {
/* 147 */     MimeMultipartParser parser = new MimeMultipartParser(in, contentType, (StreamingAttachmentFeature)this.binding.getFeature(StreamingAttachmentFeature.class));
/* 148 */     decode(parser, packet);
/*     */   }
/*     */ 
/*     */   public void decode(ReadableByteChannel in, String contentType, Packet packet) {
/* 152 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   protected abstract void decode(MimeMultipartParser paramMimeMultipartParser, Packet paramPacket)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract MimeCodec copy();
/*     */ 
/*     */   public static void writeln(String s, OutputStream out)
/*     */     throws IOException
/*     */   {
/* 164 */     writeAsAscii(s, out);
/* 165 */     writeln(out);
/*     */   }
/*     */ 
/*     */   public static void writeAsAscii(String s, OutputStream out)
/*     */     throws IOException
/*     */   {
/* 172 */     int len = s.length();
/* 173 */     for (int i = 0; i < len; i++)
/* 174 */       out.write((byte)s.charAt(i));
/*     */   }
/*     */ 
/*     */   public static void writeln(OutputStream out) throws IOException {
/* 178 */     out.write(13);
/* 179 */     out.write(10);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.MimeCodec
 * JD-Core Version:    0.6.2
 */