/*    */ package com.sun.xml.internal.txw2.output;
/*    */ 
/*    */ import javax.xml.transform.Result;
/*    */ import javax.xml.transform.dom.DOMResult;
/*    */ import javax.xml.transform.sax.SAXResult;
/*    */ import javax.xml.transform.stream.StreamResult;
/*    */ 
/*    */ public abstract class ResultFactory
/*    */ {
/*    */   public static XmlSerializer createSerializer(Result result)
/*    */   {
/* 55 */     if ((result instanceof SAXResult))
/* 56 */       return new SaxSerializer((SAXResult)result);
/* 57 */     if ((result instanceof DOMResult))
/* 58 */       return new DomSerializer((DOMResult)result);
/* 59 */     if ((result instanceof StreamResult))
/* 60 */       return new StreamSerializer((StreamResult)result);
/* 61 */     if ((result instanceof TXWResult)) {
/* 62 */       return new TXWSerializer(((TXWResult)result).getWriter());
/*    */     }
/* 64 */     throw new UnsupportedOperationException("Unsupported Result type: " + result.getClass().getName());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.ResultFactory
 * JD-Core Version:    0.6.2
 */