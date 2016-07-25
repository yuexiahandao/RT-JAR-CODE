/*    */ package com.sun.xml.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*    */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver;
/*    */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver.Parser;
/*    */ import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ final class EntityResolverWrapper
/*    */   implements XMLEntityResolver
/*    */ {
/*    */   private final EntityResolver core;
/*    */ 
/*    */   public EntityResolverWrapper(EntityResolver core)
/*    */   {
/* 48 */     this.core = core;
/*    */   }
/*    */ 
/*    */   public XMLEntityResolver.Parser resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 52 */     InputSource source = this.core.resolveEntity(publicId, systemId);
/* 53 */     if (source == null) {
/* 54 */       return null;
/*    */     }
/*    */ 
/* 59 */     if (source.getSystemId() != null) {
/* 60 */       systemId = source.getSystemId();
/*    */     }
/* 62 */     URL url = new URL(systemId);
/* 63 */     InputStream stream = url.openStream();
/* 64 */     return new XMLEntityResolver.Parser(url, new TidyXMLStreamReader(XMLStreamReaderFactory.create(url.toExternalForm(), stream, true), stream));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.EntityResolverWrapper
 * JD-Core Version:    0.6.2
 */