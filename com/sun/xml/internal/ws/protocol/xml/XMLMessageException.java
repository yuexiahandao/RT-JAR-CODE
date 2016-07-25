/*    */ package com.sun.xml.internal.ws.protocol.xml;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ 
/*    */ public class XMLMessageException extends JAXWSExceptionBase
/*    */ {
/*    */   public XMLMessageException(String key, Object[] args)
/*    */   {
/* 37 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public XMLMessageException(Throwable throwable) {
/* 41 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public XMLMessageException(Localizable arg) {
/* 45 */     super("server.rt.err", new Object[] { arg });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 49 */     return "com.sun.xml.internal.ws.resources.xmlmessage";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.protocol.xml.XMLMessageException
 * JD-Core Version:    0.6.2
 */