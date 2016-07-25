/*    */ package com.sun.xml.internal.fastinfoset.tools;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.Decoder;
/*    */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ 
/*    */ public class FI_StAX_SAX_Or_XML_SAX_SAXEvent extends TransformInputOutput
/*    */ {
/*    */   public void parse(InputStream document, OutputStream events)
/*    */     throws Exception
/*    */   {
/* 46 */     if (!document.markSupported()) {
/* 47 */       document = new BufferedInputStream(document);
/*    */     }
/*    */ 
/* 50 */     document.mark(4);
/* 51 */     boolean isFastInfosetDocument = Decoder.isFastInfosetDocument(document);
/* 52 */     document.reset();
/*    */ 
/* 54 */     if (isFastInfosetDocument) {
/* 55 */       StAXDocumentParser parser = new StAXDocumentParser();
/* 56 */       parser.setInputStream(document);
/* 57 */       SAXEventSerializer ses = new SAXEventSerializer(events);
/* 58 */       StAX2SAXReader reader = new StAX2SAXReader(parser, ses);
/* 59 */       reader.setLexicalHandler(ses);
/* 60 */       reader.adapt();
/*    */     } else {
/* 62 */       SAXParserFactory parserFactory = SAXParserFactory.newInstance();
/* 63 */       parserFactory.setNamespaceAware(true);
/* 64 */       SAXParser parser = parserFactory.newSAXParser();
/* 65 */       SAXEventSerializer ses = new SAXEventSerializer(events);
/* 66 */       parser.setProperty("http://xml.org/sax/properties/lexical-handler", ses);
/* 67 */       parser.parse(document, ses);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) throws Exception {
/* 72 */     FI_StAX_SAX_Or_XML_SAX_SAXEvent p = new FI_StAX_SAX_Or_XML_SAX_SAXEvent();
/* 73 */     p.parse(args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.tools.FI_StAX_SAX_Or_XML_SAX_SAXEvent
 * JD-Core Version:    0.6.2
 */