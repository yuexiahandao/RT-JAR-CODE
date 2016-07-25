/*    */ package com.sun.xml.internal.ws.resources;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*    */ 
/*    */ public final class HttpserverMessages
/*    */ {
/* 40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.httpserver");
/* 41 */   private static final Localizer localizer = new Localizer();
/*    */ 
/*    */   public static Localizable localizableUNEXPECTED_HTTP_METHOD(Object arg0) {
/* 44 */     return messageFactory.getMessage("unexpected.http.method", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */   public static String UNEXPECTED_HTTP_METHOD(Object arg0)
/*    */   {
/* 52 */     return localizer.localize(localizableUNEXPECTED_HTTP_METHOD(arg0));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.HttpserverMessages
 * JD-Core Version:    0.6.2
 */