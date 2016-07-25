/*    */ package com.sun.xml.internal.ws.message.stream;
/*    */ 
/*    */ import com.sun.istack.internal.FinalArrayList;
/*    */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*    */ import com.sun.xml.internal.ws.message.Util;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class StreamHeader12 extends StreamHeader
/*    */ {
/*    */   protected static final String SOAP_1_2_MUST_UNDERSTAND = "mustUnderstand";
/*    */   protected static final String SOAP_1_2_ROLE = "role";
/*    */   protected static final String SOAP_1_2_RELAY = "relay";
/*    */ 
/*    */   public StreamHeader12(XMLStreamReader reader, XMLStreamBuffer mark)
/*    */   {
/* 50 */     super(reader, mark);
/*    */   }
/*    */ 
/*    */   public StreamHeader12(XMLStreamReader reader) throws XMLStreamException {
/* 54 */     super(reader);
/*    */   }
/*    */ 
/*    */   protected final FinalArrayList<StreamHeader.Attribute> processHeaderAttributes(XMLStreamReader reader) {
/* 58 */     FinalArrayList atts = null;
/*    */ 
/* 60 */     this._role = "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver";
/*    */ 
/* 62 */     for (int i = 0; i < reader.getAttributeCount(); i++) {
/* 63 */       String localName = reader.getAttributeLocalName(i);
/* 64 */       String namespaceURI = reader.getAttributeNamespace(i);
/* 65 */       String value = reader.getAttributeValue(i);
/*    */ 
/* 67 */       if ("http://www.w3.org/2003/05/soap-envelope".equals(namespaceURI)) {
/* 68 */         if ("mustUnderstand".equals(localName))
/* 69 */           this._isMustUnderstand = Util.parseBool(value);
/* 70 */         else if ("role".equals(localName)) {
/* 71 */           if ((value != null) && (value.length() > 0))
/* 72 */             this._role = value;
/*    */         }
/* 74 */         else if ("relay".equals(localName)) {
/* 75 */           this._isRelay = Util.parseBool(value);
/*    */         }
/*    */       }
/*    */ 
/* 79 */       if (atts == null) {
/* 80 */         atts = new FinalArrayList();
/*    */       }
/* 82 */       atts.add(new StreamHeader.Attribute(namespaceURI, localName, value));
/*    */     }
/*    */ 
/* 85 */     return atts;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.stream.StreamHeader12
 * JD-Core Version:    0.6.2
 */