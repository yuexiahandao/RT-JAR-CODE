/*    */ package com.sun.xml.internal.ws.encoding.fastinfoset;
/*    */ 
/*    */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*    */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*    */ import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
/*    */ import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
/*    */ import com.sun.xml.internal.ws.message.stream.StreamHeader;
/*    */ import com.sun.xml.internal.ws.message.stream.StreamHeader12;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ final class FastInfosetStreamSOAP12Codec extends FastInfosetStreamSOAPCodec
/*    */ {
/*    */   FastInfosetStreamSOAP12Codec(StreamSOAPCodec soapCodec, boolean retainState)
/*    */   {
/* 47 */     super(soapCodec, SOAPVersion.SOAP_12, retainState, retainState ? "application/vnd.sun.stateful.soap+fastinfoset" : "application/soap+fastinfoset");
/*    */   }
/*    */ 
/*    */   private FastInfosetStreamSOAP12Codec(FastInfosetStreamSOAPCodec that)
/*    */   {
/* 52 */     super(that);
/*    */   }
/*    */ 
/*    */   public Codec copy() {
/* 56 */     return new FastInfosetStreamSOAP12Codec(this);
/*    */   }
/*    */ 
/*    */   protected final StreamHeader createHeader(XMLStreamReader reader, XMLStreamBuffer mark) {
/* 60 */     return new StreamHeader12(reader, mark);
/*    */   }
/*    */ 
/*    */   protected ContentType getContentType(String soapAction) {
/* 64 */     if (soapAction == null) {
/* 65 */       return this._defaultContentType;
/*    */     }
/* 67 */     return new ContentTypeImpl(this._defaultContentType.getContentType() + ";action=\"" + soapAction + "\"");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamSOAP12Codec
 * JD-Core Version:    0.6.2
 */