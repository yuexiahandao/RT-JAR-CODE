/*     */ package com.sun.xml.internal.ws.server.sei;
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
/*  61 */     this.methodPos = methodPos;
/*     */   }
/*     */ 
/*     */   abstract void fillIn(Object[] paramArrayOfObject, Object paramObject, Message paramMessage);
/*     */ 
/*     */   private static boolean isXMLMimeType(String mimeType)
/*     */   {
/* 176 */     return (mimeType.equals("text/xml")) || (mimeType.equals("application/xml"));
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
/*  79 */       super();
/*  80 */       this.param = param;
/*  81 */       this.getter = getter;
/*  82 */       this.mimeType = param.getBinding().getMimeType();
/*     */       try {
/*  84 */         this.contentIdPart = (URLEncoder.encode(param.getPartName(), "UTF-8") + '=');
/*     */       } catch (UnsupportedEncodingException e) {
/*  86 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public static MessageFiller createAttachmentFiller(ParameterImpl param, ValueGetter getter)
/*     */     {
/* 100 */       Class type = (Class)param.getTypeReference().type;
/* 101 */       if ((DataHandler.class.isAssignableFrom(type)) || (Source.class.isAssignableFrom(type)))
/* 102 */         return new MessageFiller.DataHandlerFiller(param, getter);
/* 103 */       if ([B.class == type)
/* 104 */         return new MessageFiller.ByteArrayFiller(param, getter);
/* 105 */       if (MessageFiller.isXMLMimeType(param.getBinding().getMimeType())) {
/* 106 */         return new MessageFiller.JAXBFiller(param, getter);
/*     */       }
/* 108 */       return new MessageFiller.DataHandlerFiller(param, getter);
/*     */     }
/*     */ 
/*     */     String getContentId()
/*     */     {
/* 113 */       return this.contentIdPart + UUID.randomUUID() + "@jaxws.sun.com";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ByteArrayFiller extends MessageFiller.AttachmentFiller {
/*     */     protected ByteArrayFiller(ParameterImpl param, ValueGetter getter) {
/* 119 */       super(getter);
/*     */     }
/*     */     void fillIn(Object[] methodArgs, Object returnValue, Message msg) {
/* 122 */       String contentId = getContentId();
/* 123 */       Object obj = this.methodPos == -1 ? returnValue : this.getter.get(methodArgs[this.methodPos]);
/* 124 */       if (obj != null) {
/* 125 */         Attachment att = new ByteArrayAttachment(contentId, (byte[])obj, this.mimeType);
/* 126 */         msg.getAttachments().add(att);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class DataHandlerFiller extends MessageFiller.AttachmentFiller {
/*     */     protected DataHandlerFiller(ParameterImpl param, ValueGetter getter) {
/* 133 */       super(getter);
/*     */     }
/*     */     void fillIn(Object[] methodArgs, Object returnValue, Message msg) {
/* 136 */       String contentId = getContentId();
/* 137 */       Object obj = this.methodPos == -1 ? returnValue : this.getter.get(methodArgs[this.methodPos]);
/* 138 */       DataHandler dh = (obj instanceof DataHandler) ? (DataHandler)obj : new DataHandler(obj, this.mimeType);
/* 139 */       Attachment att = new DataHandlerAttachment(contentId, dh);
/* 140 */       msg.getAttachments().add(att);
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
/* 164 */       super();
/* 165 */       this.bridge = bridge;
/* 166 */       this.getter = getter;
/*     */     }
/*     */ 
/*     */     void fillIn(Object[] methodArgs, Object returnValue, Message msg) {
/* 170 */       Object value = this.methodPos == -1 ? returnValue : this.getter.get(methodArgs[this.methodPos]);
/* 171 */       msg.getHeaders().add(Headers.create(this.bridge, value));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class JAXBFiller extends MessageFiller.AttachmentFiller
/*     */   {
/*     */     protected JAXBFiller(ParameterImpl param, ValueGetter getter)
/*     */     {
/* 146 */       super(getter);
/*     */     }
/*     */     void fillIn(Object[] methodArgs, Object returnValue, Message msg) {
/* 149 */       String contentId = getContentId();
/* 150 */       Object obj = this.methodPos == -1 ? returnValue : this.getter.get(methodArgs[this.methodPos]);
/* 151 */       Attachment att = new JAXBAttachment(contentId, obj, this.param.getBridge(), this.mimeType);
/* 152 */       msg.getAttachments().add(att);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.sei.MessageFiller
 * JD-Core Version:    0.6.2
 */