/*    */ package com.sun.xml.internal.org.jvnet.fastinfoset;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.sax.SAXDocumentParser;
/*    */ import java.io.InputStream;
/*    */ import javax.xml.transform.sax.SAXSource;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.XMLReader;
/*    */ 
/*    */ public class FastInfosetSource extends SAXSource
/*    */ {
/*    */   public FastInfosetSource(InputStream inputStream)
/*    */   {
/* 65 */     super(new InputSource(inputStream));
/*    */   }
/*    */ 
/*    */   public XMLReader getXMLReader() {
/* 69 */     XMLReader reader = super.getXMLReader();
/* 70 */     if (reader == null) {
/* 71 */       reader = new SAXDocumentParser();
/* 72 */       setXMLReader(reader);
/*    */     }
/* 74 */     ((SAXDocumentParser)reader).setInputStream(getInputStream());
/* 75 */     return reader;
/*    */   }
/*    */ 
/*    */   public InputStream getInputStream() {
/* 79 */     return getInputSource().getByteStream();
/*    */   }
/*    */ 
/*    */   public void setInputStream(InputStream inputStream) {
/* 83 */     setInputSource(new InputSource(inputStream));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource
 * JD-Core Version:    0.6.2
 */