/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ 
/*    */ public class UtilException extends JAXWSExceptionBase
/*    */ {
/*    */   public UtilException(String key, Object[] args)
/*    */   {
/* 41 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public UtilException(Throwable throwable) {
/* 45 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public UtilException(Localizable arg) {
/* 49 */     super("nestedUtilError", new Object[] { arg });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 53 */     return "com.sun.xml.internal.ws.resources.util";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.UtilException
 * JD-Core Version:    0.6.2
 */