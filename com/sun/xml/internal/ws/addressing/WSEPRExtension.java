/*    */ package com.sun.xml.internal.ws.addressing;
/*    */ 
/*    */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*    */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.EPRExtension;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class WSEPRExtension extends WSEndpointReference.EPRExtension
/*    */ {
/*    */   XMLStreamBuffer xsb;
/*    */   final QName qname;
/*    */ 
/*    */   public WSEPRExtension(XMLStreamBuffer xsb, QName qname)
/*    */   {
/* 45 */     this.xsb = xsb;
/* 46 */     this.qname = qname;
/*    */   }
/*    */ 
/*    */   public XMLStreamReader readAsXMLStreamReader() throws XMLStreamException
/*    */   {
/* 51 */     return this.xsb.readAsXMLStreamReader();
/*    */   }
/*    */ 
/*    */   public QName getQName() {
/* 55 */     return this.qname;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.WSEPRExtension
 * JD-Core Version:    0.6.2
 */