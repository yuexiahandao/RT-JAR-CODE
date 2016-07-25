/*    */ package com.sun.xml.internal.ws.util.xml;
/*    */ 
/*    */ import javax.xml.stream.XMLStreamWriter;
/*    */ import javax.xml.transform.sax.SAXResult;
/*    */ 
/*    */ public class StAXResult extends SAXResult
/*    */ {
/*    */   public StAXResult(XMLStreamWriter writer)
/*    */   {
/* 83 */     if (writer == null) {
/* 84 */       throw new IllegalArgumentException();
/*    */     }
/*    */ 
/* 87 */     super.setHandler(new ContentHandlerToXMLStreamWriter(writer));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.StAXResult
 * JD-Core Version:    0.6.2
 */