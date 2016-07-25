/*     */ package com.sun.xml.internal.ws.client.sei;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Headers;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*     */ import com.sun.xml.internal.ws.message.ByteArrayAttachment;
/*     */ import com.sun.xml.internal.ws.message.DataHandlerAttachment;
/*     */ import com.sun.xml.internal.ws.message.JAXBAttachment;
/*     */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.UUID;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ abstract class MessageFiller
/*     */ {
/*     */   protected final int methodPos;
/*     */ 
/*     */   protected MessageFiller(int methodPos)
/*     */   {
/*  62 */     this.methodPos = methodPos;
/*     */   }
/*     */ 
/*     */   abstract void fillIn(Object[] paramArrayOfObject, Message paramMessage);
/*     */ 
/*     */   private static boolean isXMLMimeType(String mimeType)
/*     */   {
/* 175 */     return (mimeType.equals("text/xml")) || (mimeType.equals("application/xml"));
/*     */   }
/*     */ 
/*     */   static abstract class AttachmentFiller extends MessageFiller
/*     */   {
/*     */     protected final ParameterImpl param;
/*     */     protected final ValueGetter getter;
/*     */     protected final String mimeType;
/*     */     private final String contentIdPart;
/*     */ 
/*     */     protected AttachmentFiller(ParameterImpl param, ValueGetter getter)
/*     */     {
/*  80 */       super();
/*  81 */       this.param = param;
/*  82 */       this.getter = getter;
/*  83 */       this.mimeType = param.getBinding().getMimeType();
/*     */       try {
/*  85 */         this.contentIdPart = (URLEncoder.encode(param.getPartName(), "UTF-8") + '=');
/*     */       } catch (UnsupportedEncodingException e) {
/*  87 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public static MessageFiller createAttachmentFiller(ParameterImpl param, ValueGetter getter)
/*     */     {
/* 101 */       Class type = (Class)param.getTypeReference().type;
/* 102 */       if ((DataHandler.class.isAssignableFrom(type)) || (Source.class.isAssignableFrom(type)))
/* 103 */         return new MessageFiller.DataHandlerFiller(param, getter);
/* 104 */       if ([B.class == type)
/* 105 */         return new MessageFiller.ByteArrayFiller(param, getter);
/* 106 */       if (MessageFiller.isXMLMimeType(param.getBinding().getMimeType())) {
/* 107 */         return new MessageFiller.JAXBFiller(param, getter);
/*     */       }
/* 109 */       return new MessageFiller.DataHandlerFiller(param, getter);
/*     */     }
/*     */ 
/*     */     String getContentId()
/*     */     {
/* 114 */       return this.contentIdPart + UUID.randomUUID() + "@jaxws.sun.com";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ByteArrayFiller extends MessageFiller.AttachmentFiller {
/*     */     protected ByteArrayFiller(ParameterImpl param, ValueGetter getter) {
/* 120 */       super(getter);
/*     */     }
/*     */     void fillIn(Object[] methodArgs, Message msg) {
/* 123 */       String contentId = getContentId();
/* 124 */       Object obj = this.getter.get(methodArgs[this.methodPos]);
/* 125 */       Attachment att = new ByteArrayAttachment(contentId, (byte[])obj, this.mimeType);
/* 126 */       msg.getAttachments().add(att);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class DataHandlerFiller extends MessageFiller.AttachmentFiller {
/*     */     protected DataHandlerFiller(ParameterImpl param, ValueGetter getter) {
/* 132 */       super(getter);
/*     */     }
/*     */     void fillIn(Object[] methodArgs, Message msg) {
/* 135 */       String contentId = getContentId();
/* 136 */       Object obj = this.getter.get(methodArgs[this.methodPos]);
/* 137 */       DataHandler dh = (obj instanceof DataHandler) ? (DataHandler)obj : new DataHandler(obj, this.mimeType);
/* 138 */       Attachment att = new DataHandlerAttachment(contentId, dh);
/* 139 */       msg.getAttachments().add(att);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Header extends MessageFiller
/*     */   {
/*     */     private final Bridge bridge;
/*     */     private final ValueGetter getter;
/*     */ 
/*     */     protected Header(int methodPos, Bridge bridge, ValueGetter getter)
/*     */     {
/* 163 */       super();
/* 164 */       this.bridge = bridge;
/* 165 */       this.getter = getter;
/*     */     }
/*     */ 
/*     */     void fillIn(Object[] methodArgs, Message msg) {
/* 169 */       Object value = this.getter.get(methodArgs[this.methodPos]);
/* 170 */       msg.getHeaders().add(Headers.create(this.bridge, value));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class JAXBFiller extends MessageFiller.AttachmentFiller
/*     */   {
/*     */     protected JAXBFiller(ParameterImpl param, ValueGetter getter)
/*     */     {
/* 145 */       super(getter);
/*     */     }
/*     */     void fillIn(Object[] methodArgs, Message msg) {
/* 148 */       String contentId = getContentId();
/* 149 */       Object obj = this.getter.get(methodArgs[this.methodPos]);
/* 150 */       Attachment att = new JAXBAttachment(contentId, obj, this.param.getBridge(), this.mimeType);
/* 151 */       msg.getAttachments().add(att);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.MessageFiller
 * JD-Core Version:    0.6.2
 */