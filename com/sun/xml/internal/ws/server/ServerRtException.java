/*    */ package com.sun.xml.internal.ws.server;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ 
/*    */ public class ServerRtException extends JAXWSExceptionBase
/*    */ {
/*    */   public ServerRtException(String key, Object[] args)
/*    */   {
/* 36 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public ServerRtException(Throwable throwable) {
/* 40 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public ServerRtException(Localizable arg) {
/* 44 */     super("server.rt.err", new Object[] { arg });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 48 */     return "com.sun.xml.internal.ws.resources.server";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.ServerRtException
 * JD-Core Version:    0.6.2
 */