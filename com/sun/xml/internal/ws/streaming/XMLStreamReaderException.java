/*    */ package com.sun.xml.internal.ws.streaming;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ 
/*    */ public class XMLStreamReaderException extends JAXWSExceptionBase
/*    */ {
/*    */   public XMLStreamReaderException(String key, Object[] args)
/*    */   {
/* 42 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public XMLStreamReaderException(Throwable throwable) {
/* 46 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public XMLStreamReaderException(Localizable arg) {
/* 50 */     super("xmlreader.nestedError", new Object[] { arg });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 54 */     return "com.sun.xml.internal.ws.resources.streaming";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.streaming.XMLStreamReaderException
 * JD-Core Version:    0.6.2
 */