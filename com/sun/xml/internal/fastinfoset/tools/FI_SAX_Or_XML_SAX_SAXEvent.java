/*    */ package com.sun.xml.internal.fastinfoset.tools;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.Decoder;
/*    */ import com.sun.xml.internal.fastinfoset.sax.SAXDocumentParser;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.XMLReader;
/*    */ 
/*    */ public class FI_SAX_Or_XML_SAX_SAXEvent extends TransformInputOutput
/*    */ {
/*    */   public void parse(InputStream document, OutputStream events, String workingDirectory)
/*    */     throws Exception
/*    */   {
/* 47 */     if (!document.markSupported()) {
/* 48 */       document = new BufferedInputStream(document);
/*    */     }
/*    */ 
/* 51 */     document.mark(4);
/* 52 */     boolean isFastInfosetDocument = Decoder.isFastInfosetDocument(document);
/* 53 */     document.reset();
/*    */ 
/* 55 */     if (isFastInfosetDocument) {
/* 56 */       SAXDocumentParser parser = new SAXDocumentParser();
/* 57 */       SAXEventSerializer ses = new SAXEventSerializer(events);
/* 58 */       parser.setContentHandler(ses);
/* 59 */       parser.setProperty("http://xml.org/sax/properties/lexical-handler", ses);
/* 60 */       parser.parse(document);
/*    */     } else {
/* 62 */       SAXParserFactory parserFactory = SAXParserFactory.newInstance();
/* 63 */       parserFactory.setNamespaceAware(true);
/* 64 */       SAXParser parser = parserFactory.newSAXParser();
/* 65 */       SAXEventSerializer ses = new SAXEventSerializer(events);
/*    */ 
/* 67 */       XMLReader reader = parser.getXMLReader();
/* 68 */       reader.setProperty("http://xml.org/sax/properties/lexical-handler", ses);
/* 69 */       reader.setContentHandler(ses);
/* 70 */       if (workingDirectory != null) {
/* 71 */         reader.setEntityResolver(createRelativePathResolver(workingDirectory));
/*    */       }
/* 73 */       reader.parse(new InputSource(document));
/*    */     }
/*    */   }
/*    */ 
/*    */   public void parse(InputStream document, OutputStream events) throws Exception {
/* 78 */     parse(document, events, null);
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) throws Exception {
/* 82 */     FI_SAX_Or_XML_SAX_SAXEvent p = new FI_SAX_Or_XML_SAX_SAXEvent();
/* 83 */     p.parse(args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.tools.FI_SAX_Or_XML_SAX_SAXEvent
 * JD-Core Version:    0.6.2
 */