/*    */ package com.sun.xml.internal.ws.encoding.soap;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ 
/*    */ public class DeserializationException extends JAXWSExceptionBase
/*    */ {
/*    */   public DeserializationException(String key, Object[] args)
/*    */   {
/* 42 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public DeserializationException(Throwable throwable) {
/* 46 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public DeserializationException(Localizable arg) {
/* 50 */     super("nestedDeserializationError", new Object[] { arg });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 54 */     return "com.sun.xml.internal.ws.resources.encoding";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.soap.DeserializationException
 * JD-Core Version:    0.6.2
 */