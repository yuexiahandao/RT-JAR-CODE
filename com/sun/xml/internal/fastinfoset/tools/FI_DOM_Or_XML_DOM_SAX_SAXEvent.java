/*    */ package com.sun.xml.internal.fastinfoset.tools;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.Decoder;
/*    */ import com.sun.xml.internal.fastinfoset.dom.DOMDocumentParser;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.xml.parsers.DocumentBuilder;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.TransformerFactory;
/*    */ import javax.xml.transform.dom.DOMSource;
/*    */ import javax.xml.transform.sax.SAXResult;
/*    */ import org.w3c.dom.Document;
/*    */ 
/*    */ public class FI_DOM_Or_XML_DOM_SAX_SAXEvent extends TransformInputOutput
/*    */ {
/*    */   public void parse(InputStream document, OutputStream events, String workingDirectory)
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
/* 54 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 55 */     dbf.setNamespaceAware(true);
/* 56 */     DocumentBuilder db = dbf.newDocumentBuilder();
/*    */     Document d;
/* 59 */     if (isFastInfosetDocument) {
/* 60 */       Document d = db.newDocument();
/* 61 */       DOMDocumentParser ddp = new DOMDocumentParser();
/* 62 */       ddp.parse(d, document);
/*    */     } else {
/* 64 */       if (workingDirectory != null) {
/* 65 */         db.setEntityResolver(createRelativePathResolver(workingDirectory));
/*    */       }
/* 67 */       d = db.parse(document);
/*    */     }
/*    */ 
/* 70 */     SAXEventSerializer ses = new SAXEventSerializer(events);
/*    */ 
/* 72 */     TransformerFactory tf = TransformerFactory.newInstance();
/* 73 */     Transformer t = tf.newTransformer();
/* 74 */     t.transform(new DOMSource(d), new SAXResult(ses));
/*    */   }
/*    */ 
/*    */   public void parse(InputStream document, OutputStream events) throws Exception {
/* 78 */     parse(document, events, null);
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) throws Exception {
/* 82 */     FI_DOM_Or_XML_DOM_SAX_SAXEvent p = new FI_DOM_Or_XML_DOM_SAX_SAXEvent();
/* 83 */     p.parse(args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent
 * JD-Core Version:    0.6.2
 */