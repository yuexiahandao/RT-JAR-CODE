/*    */ package com.sun.xml.internal.fastinfoset.tools;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.Decoder;
/*    */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.TransformerFactory;
/*    */ import javax.xml.transform.dom.DOMResult;
/*    */ import javax.xml.transform.dom.DOMSource;
/*    */ import javax.xml.transform.sax.SAXResult;
/*    */ import javax.xml.transform.sax.SAXSource;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.XMLReader;
/*    */ 
/*    */ public class FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent extends TransformInputOutput
/*    */ {
/*    */   public void parse(InputStream document, OutputStream events, String workingDirectory)
/*    */     throws Exception
/*    */   {
/* 53 */     if (!document.markSupported()) {
/* 54 */       document = new BufferedInputStream(document);
/*    */     }
/*    */ 
/* 57 */     document.mark(4);
/* 58 */     boolean isFastInfosetDocument = Decoder.isFastInfosetDocument(document);
/* 59 */     document.reset();
/*    */ 
/* 61 */     TransformerFactory tf = TransformerFactory.newInstance();
/* 62 */     Transformer t = tf.newTransformer();
/* 63 */     DOMResult dr = new DOMResult();
/*    */ 
/* 65 */     if (isFastInfosetDocument) {
/* 66 */       t.transform(new FastInfosetSource(document), dr);
/* 67 */     } else if (workingDirectory != null) {
/* 68 */       SAXParser parser = getParser();
/* 69 */       XMLReader reader = parser.getXMLReader();
/* 70 */       reader.setEntityResolver(createRelativePathResolver(workingDirectory));
/* 71 */       SAXSource source = new SAXSource(reader, new InputSource(document));
/*    */ 
/* 73 */       t.transform(source, dr);
/*    */     } else {
/* 75 */       t.transform(new StreamSource(document), dr);
/*    */     }
/*    */ 
/* 78 */     SAXEventSerializer ses = new SAXEventSerializer(events);
/* 79 */     t.transform(new DOMSource(dr.getNode()), new SAXResult(ses));
/*    */   }
/*    */ 
/*    */   public void parse(InputStream document, OutputStream events) throws Exception {
/* 83 */     parse(document, events, null);
/*    */   }
/*    */ 
/*    */   private SAXParser getParser() {
/* 87 */     SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
/* 88 */     saxParserFactory.setNamespaceAware(true);
/*    */     try {
/* 90 */       return saxParserFactory.newSAXParser(); } catch (Exception e) {
/*    */     }
/* 92 */     return null;
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) throws Exception
/*    */   {
/* 97 */     FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent p = new FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent();
/* 98 */     p.parse(args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.tools.FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent
 * JD-Core Version:    0.6.2
 */