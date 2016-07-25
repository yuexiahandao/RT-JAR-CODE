/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import com.sun.xml.internal.ws.streaming.XMLReaderException;
/*    */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderException;
/*    */ import java.io.InputStream;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class FastInfosetUtil
/*    */ {
/*    */   public static XMLStreamReader createFIStreamReader(InputStream in)
/*    */   {
/* 41 */     if (FastInfosetReflection.fiStAXDocumentParser_new == null) {
/* 42 */       throw new XMLReaderException("fastinfoset.noImplementation", new Object[0]);
/*    */     }
/*    */ 
/*    */     try
/*    */     {
/* 47 */       Object sdp = FastInfosetReflection.fiStAXDocumentParser_new.newInstance(new Object[0]);
/* 48 */       FastInfosetReflection.fiStAXDocumentParser_setStringInterning.invoke(sdp, new Object[] { Boolean.TRUE });
/* 49 */       FastInfosetReflection.fiStAXDocumentParser_setInputStream.invoke(sdp, new Object[] { in });
/* 50 */       return (XMLStreamReader)sdp;
/*    */     } catch (Exception e) {
/* 52 */       throw new XMLStreamReaderException(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.FastInfosetUtil
 * JD-Core Version:    0.6.2
 */