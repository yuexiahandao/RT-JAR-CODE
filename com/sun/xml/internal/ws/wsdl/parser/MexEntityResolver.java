/*    */ package com.sun.xml.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
/*    */ import com.sun.xml.internal.ws.api.server.SDDocumentSource;
/*    */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver;
/*    */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver.Parser;
/*    */ import com.sun.xml.internal.ws.util.JAXWSUtils;
/*    */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class MexEntityResolver
/*    */   implements XMLEntityResolver
/*    */ {
/* 51 */   private final Map<String, SDDocumentSource> wsdls = new HashMap();
/*    */ 
/*    */   public MexEntityResolver(List<? extends Source> wsdls) throws IOException {
/* 54 */     Transformer transformer = XmlUtil.newTransformer();
/* 55 */     for (Source source : wsdls) {
/* 56 */       XMLStreamBufferResult xsbr = new XMLStreamBufferResult();
/*    */       try {
/* 58 */         transformer.transform(source, xsbr);
/*    */       } catch (TransformerException e) {
/* 60 */         throw new WebServiceException(e);
/*    */       }
/* 62 */       String systemId = source.getSystemId();
/*    */ 
/* 65 */       if (systemId != null) {
/* 66 */         SDDocumentSource doc = SDDocumentSource.create(JAXWSUtils.getFileOrURL(systemId), xsbr.getXMLStreamBuffer());
/* 67 */         this.wsdls.put(systemId, doc);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public XMLEntityResolver.Parser resolveEntity(String publicId, String systemId) throws SAXException, IOException, XMLStreamException {
/* 73 */     if (systemId != null) {
/* 74 */       SDDocumentSource src = (SDDocumentSource)this.wsdls.get(systemId);
/* 75 */       if (src != null)
/* 76 */         return new XMLEntityResolver.Parser(src);
/*    */     }
/* 78 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.MexEntityResolver
 * JD-Core Version:    0.6.2
 */