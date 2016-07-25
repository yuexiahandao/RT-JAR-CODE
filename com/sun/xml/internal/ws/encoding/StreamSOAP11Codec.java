/*    */ package com.sun.xml.internal.ws.encoding;
/*    */ 
/*    */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*    */ import com.sun.xml.internal.ws.message.stream.StreamHeader;
/*    */ import com.sun.xml.internal.ws.message.stream.StreamHeader11;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ final class StreamSOAP11Codec extends StreamSOAPCodec
/*    */ {
/*    */   public static final String SOAP11_MIME_TYPE = "text/xml";
/*    */   public static final String SOAP11_CONTENT_TYPE = "text/xml; charset=utf-8";
/* 47 */   private static final List<String> expectedContentTypes = Collections.singletonList("text/xml");
/*    */ 
/* 62 */   public static final ContentTypeImpl defaultContentType = new ContentTypeImpl("text/xml; charset=utf-8", "");
/*    */ 
/*    */   StreamSOAP11Codec()
/*    */   {
/* 50 */     super(SOAPVersion.SOAP_11);
/*    */   }
/*    */ 
/*    */   public String getMimeType() {
/* 54 */     return "text/xml";
/*    */   }
/*    */ 
/*    */   protected final StreamHeader createHeader(XMLStreamReader reader, XMLStreamBuffer mark)
/*    */   {
/* 59 */     return new StreamHeader11(reader, mark);
/*    */   }
/*    */ 
/*    */   protected ContentType getContentType(String soapAction)
/*    */   {
/* 67 */     return new ContentTypeImpl("text/xml; charset=utf-8", soapAction);
/*    */   }
/*    */ 
/*    */   protected List<String> getExpectedContentTypes() {
/* 71 */     return expectedContentTypes;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.StreamSOAP11Codec
 * JD-Core Version:    0.6.2
 */