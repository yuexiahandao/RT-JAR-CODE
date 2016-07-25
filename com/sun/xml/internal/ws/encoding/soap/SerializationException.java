/*    */ package com.sun.xml.internal.ws.encoding.soap;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ 
/*    */ public class SerializationException extends JAXWSExceptionBase
/*    */ {
/*    */   public SerializationException(String key, Object[] args)
/*    */   {
/* 41 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public SerializationException(Localizable arg) {
/* 45 */     super("nestedSerializationError", new Object[] { arg });
/*    */   }
/*    */ 
/*    */   public SerializationException(Throwable throwable) {
/* 49 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 53 */     return "com.sun.xml.internal.ws.resources.encoding";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.soap.SerializationException
 * JD-Core Version:    0.6.2
 */