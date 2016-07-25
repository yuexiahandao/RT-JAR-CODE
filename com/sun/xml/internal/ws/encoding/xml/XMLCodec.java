/*    */ package com.sun.xml.internal.ws.encoding.xml;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*    */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*    */ import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
/*    */ import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.channels.ReadableByteChannel;
/*    */ import java.nio.channels.WritableByteChannel;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamWriter;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public final class XMLCodec
/*    */   implements Codec
/*    */ {
/*    */   public static final String XML_APPLICATION_MIME_TYPE = "application/xml";
/*    */   public static final String XML_TEXT_MIME_TYPE = "text/xml";
/* 50 */   private static final ContentType contentType = new ContentTypeImpl("text/xml");
/*    */   private final WSBinding binding;
/*    */ 
/*    */   public XMLCodec(WSBinding binding)
/*    */   {
/* 55 */     this.binding = binding;
/*    */   }
/*    */ 
/*    */   public String getMimeType() {
/* 59 */     return "application/xml";
/*    */   }
/*    */ 
/*    */   public ContentType getStaticContentType(Packet packet) {
/* 63 */     return contentType;
/*    */   }
/*    */ 
/*    */   public ContentType encode(Packet packet, OutputStream out) {
/* 67 */     XMLStreamWriter writer = XMLStreamWriterFactory.create(out);
/*    */     try {
/* 69 */       if (packet.getMessage().hasPayload()) {
/* 70 */         packet.getMessage().writePayloadTo(writer);
/* 71 */         writer.flush();
/*    */       }
/*    */     } catch (XMLStreamException e) {
/* 74 */       throw new WebServiceException(e);
/*    */     }
/* 76 */     return contentType;
/*    */   }
/*    */ 
/*    */   public ContentType encode(Packet packet, WritableByteChannel buffer)
/*    */   {
/* 81 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   public Codec copy() {
/* 85 */     return this;
/*    */   }
/*    */ 
/*    */   public void decode(InputStream in, String contentType, Packet packet) throws IOException {
/* 89 */     Message message = XMLMessage.create(contentType, in, this.binding);
/* 90 */     packet.setMessage(message);
/*    */   }
/*    */ 
/*    */   public void decode(ReadableByteChannel in, String contentType, Packet packet)
/*    */   {
/* 95 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.xml.XMLCodec
 * JD-Core Version:    0.6.2
 */