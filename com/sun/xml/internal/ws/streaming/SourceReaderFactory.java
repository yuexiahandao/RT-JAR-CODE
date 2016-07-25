/*    */ package com.sun.xml.internal.ws.streaming;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*    */ import com.sun.xml.internal.ws.util.FastInfosetUtil;
/*    */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.Reader;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.URL;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.dom.DOMResult;
/*    */ import javax.xml.transform.dom.DOMSource;
/*    */ import javax.xml.transform.sax.SAXSource;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ 
/*    */ public class SourceReaderFactory
/*    */ {
/*    */   static Class fastInfosetSourceClass;
/*    */   static Method fastInfosetSource_getInputStream;
/*    */ 
/*    */   public static XMLStreamReader createSourceReader(Source source, boolean rejectDTDs)
/*    */   {
/* 74 */     return createSourceReader(source, rejectDTDs, null);
/*    */   }
/*    */ 
/*    */   public static XMLStreamReader createSourceReader(Source source, boolean rejectDTDs, String charsetName) {
/*    */     try {
/* 79 */       if ((source instanceof StreamSource)) {
/* 80 */         StreamSource streamSource = (StreamSource)source;
/* 81 */         InputStream is = streamSource.getInputStream();
/*    */ 
/* 83 */         if (is != null)
/*    */         {
/* 85 */           if (charsetName != null) {
/* 86 */             return XMLStreamReaderFactory.create(source.getSystemId(), new InputStreamReader(is, charsetName), rejectDTDs);
/*    */           }
/*    */ 
/* 90 */           return XMLStreamReaderFactory.create(source.getSystemId(), is, rejectDTDs);
/*    */         }
/*    */ 
/* 95 */         Reader reader = streamSource.getReader();
/* 96 */         if (reader != null) {
/* 97 */           return XMLStreamReaderFactory.create(source.getSystemId(), reader, rejectDTDs);
/*    */         }
/*    */ 
/* 101 */         return XMLStreamReaderFactory.create(source.getSystemId(), new URL(source.getSystemId()).openStream(), rejectDTDs);
/*    */       }
/*    */ 
/* 106 */       if (source.getClass() == fastInfosetSourceClass) {
/* 107 */         return FastInfosetUtil.createFIStreamReader((InputStream)fastInfosetSource_getInputStream.invoke(source, new Object[0]));
/*    */       }
/*    */ 
/* 110 */       if ((source instanceof DOMSource)) {
/* 111 */         DOMStreamReader dsr = new DOMStreamReader();
/* 112 */         dsr.setCurrentNode(((DOMSource)source).getNode());
/* 113 */         return dsr;
/*    */       }
/* 115 */       if ((source instanceof SAXSource))
/*    */       {
/* 117 */         Transformer tx = XmlUtil.newTransformer();
/* 118 */         DOMResult domResult = new DOMResult();
/* 119 */         tx.transform(source, domResult);
/* 120 */         return createSourceReader(new DOMSource(domResult.getNode()), rejectDTDs);
/*    */       }
/*    */ 
/* 125 */       throw new XMLReaderException("sourceReader.invalidSource", new Object[] { source.getClass().getName() });
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 130 */       throw new XMLReaderException(e);
/*    */     }
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 63 */       fastInfosetSourceClass = Class.forName("com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource");
/*    */ 
/* 65 */       fastInfosetSource_getInputStream = fastInfosetSourceClass.getMethod("getInputStream", new Class[0]);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 69 */       fastInfosetSourceClass = null;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.streaming.SourceReaderFactory
 * JD-Core Version:    0.6.2
 */