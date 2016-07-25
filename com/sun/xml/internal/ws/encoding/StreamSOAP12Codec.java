/*    */ package com.sun.xml.internal.ws.encoding;
/*    */ 
/*    */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.message.stream.StreamHeader;
/*    */ import com.sun.xml.internal.ws.message.stream.StreamHeader12;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ final class StreamSOAP12Codec extends StreamSOAPCodec
/*    */ {
/*    */   public static final String SOAP12_MIME_TYPE = "application/soap+xml";
/*    */   public static final String SOAP12_CONTENT_TYPE = "application/soap+xml; charset=utf-8";
/* 51 */   private static final List<String> expectedContentTypes = Collections.singletonList("application/soap+xml");
/*    */ 
/* 66 */   public static final ContentTypeImpl defaultContentType = new ContentTypeImpl("application/soap+xml; charset=utf-8");
/*    */ 
/*    */   StreamSOAP12Codec()
/*    */   {
/* 54 */     super(SOAPVersion.SOAP_12);
/*    */   }
/*    */ 
/*    */   public String getMimeType() {
/* 58 */     return "application/soap+xml";
/*    */   }
/*    */ 
/*    */   protected final StreamHeader createHeader(XMLStreamReader reader, XMLStreamBuffer mark)
/*    */   {
/* 63 */     return new StreamHeader12(reader, mark);
/*    */   }
/*    */ 
/*    */   protected com.sun.xml.internal.ws.api.pipe.ContentType getContentType(String soapAction)
/*    */   {
/* 72 */     if (soapAction == null) {
/* 73 */       return defaultContentType;
/*    */     }
/* 75 */     return new ContentTypeImpl("application/soap+xml; charset=utf-8;action=" + fixQuotesAroundSoapAction(soapAction));
/*    */   }
/*    */ 
/*    */   public void decode(InputStream in, String contentType, Packet packet, AttachmentSet att)
/*    */     throws IOException
/*    */   {
/* 81 */     ContentType ct = new ContentType(contentType);
/* 82 */     packet.soapAction = fixQuotesAroundSoapAction(ct.getParameter("action"));
/* 83 */     super.decode(in, contentType, packet, att);
/*    */   }
/*    */ 
/*    */   private String fixQuotesAroundSoapAction(String soapAction) {
/* 87 */     if ((soapAction != null) && ((!soapAction.startsWith("\"")) || (!soapAction.endsWith("\"")))) {
/* 88 */       String fixedSoapAction = soapAction;
/* 89 */       if (!soapAction.startsWith("\""))
/* 90 */         fixedSoapAction = "\"" + fixedSoapAction;
/* 91 */       if (!soapAction.endsWith("\""))
/* 92 */         fixedSoapAction = fixedSoapAction + "\"";
/* 93 */       return fixedSoapAction;
/*    */     }
/* 95 */     return soapAction;
/*    */   }
/*    */ 
/*    */   protected List<String> getExpectedContentTypes() {
/* 99 */     return expectedContentTypes;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.StreamSOAP12Codec
 * JD-Core Version:    0.6.2
 */