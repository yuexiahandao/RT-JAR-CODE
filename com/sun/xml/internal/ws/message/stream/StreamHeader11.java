/*    */ package com.sun.xml.internal.ws.message.stream;
/*    */ 
/*    */ import com.sun.istack.internal.FinalArrayList;
/*    */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*    */ import com.sun.xml.internal.ws.message.Util;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class StreamHeader11 extends StreamHeader
/*    */ {
/*    */   protected static final String SOAP_1_1_MUST_UNDERSTAND = "mustUnderstand";
/*    */   protected static final String SOAP_1_1_ROLE = "actor";
/*    */ 
/*    */   public StreamHeader11(XMLStreamReader reader, XMLStreamBuffer mark)
/*    */   {
/* 48 */     super(reader, mark);
/*    */   }
/*    */ 
/*    */   public StreamHeader11(XMLStreamReader reader) throws XMLStreamException {
/* 52 */     super(reader);
/*    */   }
/*    */ 
/*    */   protected final FinalArrayList<StreamHeader.Attribute> processHeaderAttributes(XMLStreamReader reader) {
/* 56 */     FinalArrayList atts = null;
/*    */ 
/* 58 */     this._role = "http://schemas.xmlsoap.org/soap/actor/next";
/*    */ 
/* 60 */     for (int i = 0; i < reader.getAttributeCount(); i++) {
/* 61 */       String localName = reader.getAttributeLocalName(i);
/* 62 */       String namespaceURI = reader.getAttributeNamespace(i);
/* 63 */       String value = reader.getAttributeValue(i);
/*    */ 
/* 65 */       if ("http://schemas.xmlsoap.org/soap/envelope/".equals(namespaceURI)) {
/* 66 */         if ("mustUnderstand".equals(localName))
/* 67 */           this._isMustUnderstand = Util.parseBool(value);
/* 68 */         else if (("actor".equals(localName)) && 
/* 69 */           (value != null) && (value.length() > 0)) {
/* 70 */           this._role = value;
/*    */         }
/*    */ 
/*    */       }
/*    */ 
/* 75 */       if (atts == null) {
/* 76 */         atts = new FinalArrayList();
/*    */       }
/* 78 */       atts.add(new StreamHeader.Attribute(namespaceURI, localName, value));
/*    */     }
/*    */ 
/* 81 */     return atts;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.stream.StreamHeader11
 * JD-Core Version:    0.6.2
 */