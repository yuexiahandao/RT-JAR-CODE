/*    */ package com.sun.xml.internal.ws.message.jaxb;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.message.Attachment;
/*    */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*    */ import com.sun.xml.internal.ws.message.DataHandlerAttachment;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.net.URL;
/*    */ import java.net.URLEncoder;
/*    */ import java.util.UUID;
/*    */ import javax.activation.DataHandler;
/*    */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ final class AttachmentMarshallerImpl extends AttachmentMarshaller
/*    */ {
/*    */   private AttachmentSet attachments;
/*    */ 
/*    */   public AttachmentMarshallerImpl(AttachmentSet attachemnts)
/*    */   {
/* 53 */     this.attachments = attachemnts;
/*    */   }
/*    */ 
/*    */   void cleanup()
/*    */   {
/* 60 */     this.attachments = null;
/*    */   }
/*    */ 
/*    */   public String addMtomAttachment(DataHandler data, String elementNamespace, String elementLocalName)
/*    */   {
/* 65 */     throw new IllegalStateException();
/*    */   }
/*    */ 
/*    */   public String addMtomAttachment(byte[] data, int offset, int length, String mimeType, String elementNamespace, String elementLocalName)
/*    */   {
/* 70 */     throw new IllegalStateException();
/*    */   }
/*    */ 
/*    */   public String addSwaRefAttachment(DataHandler data) {
/* 74 */     String cid = encodeCid(null);
/* 75 */     Attachment att = new DataHandlerAttachment(cid, data);
/* 76 */     this.attachments.add(att);
/* 77 */     cid = "cid:" + cid;
/* 78 */     return cid;
/*    */   }
/*    */ 
/*    */   private String encodeCid(String ns) {
/* 82 */     String cid = "example.jaxws.sun.com";
/* 83 */     String name = UUID.randomUUID() + "@";
/* 84 */     if ((ns != null) && (ns.length() > 0)) {
/*    */       try {
/* 86 */         URI uri = new URI(ns);
/* 87 */         cid = uri.toURL().getHost();
/*    */       } catch (URISyntaxException e) {
/* 89 */         e.printStackTrace();
/* 90 */         return null;
/*    */       } catch (MalformedURLException e) {
/*    */         try {
/* 93 */           cid = URLEncoder.encode(ns, "UTF-8");
/*    */         } catch (UnsupportedEncodingException e1) {
/* 95 */           throw new WebServiceException(e);
/*    */         }
/*    */       }
/*    */     }
/* 99 */     return name + cid;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.jaxb.AttachmentMarshallerImpl
 * JD-Core Version:    0.6.2
 */