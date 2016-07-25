/*    */ package com.sun.xml.internal.ws.api.wsdl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.server.SDDocumentSource;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract interface XMLEntityResolver
/*    */ {
/*    */   public abstract Parser resolveEntity(String paramString1, String paramString2)
/*    */     throws SAXException, IOException, XMLStreamException;
/*    */ 
/*    */   public static final class Parser
/*    */   {
/*    */     public final URL systemId;
/*    */     public final XMLStreamReader parser;
/*    */ 
/*    */     public Parser(URL systemId, XMLStreamReader parser)
/*    */     {
/* 63 */       assert (parser != null);
/* 64 */       this.systemId = systemId;
/* 65 */       this.parser = parser;
/*    */     }
/*    */ 
/*    */     public Parser(SDDocumentSource doc)
/*    */       throws IOException, XMLStreamException
/*    */     {
/* 72 */       this.systemId = doc.getSystemId();
/* 73 */       this.parser = doc.read();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver
 * JD-Core Version:    0.6.2
 */