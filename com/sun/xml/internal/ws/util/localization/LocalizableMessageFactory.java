/*    */ package com.sun.xml.internal.ws.util.localization;
/*    */ 
/*    */ public class LocalizableMessageFactory
/*    */ {
/*    */   private final String _bundlename;
/*    */ 
/*    */   public LocalizableMessageFactory(String bundlename)
/*    */   {
/* 36 */     this._bundlename = bundlename;
/*    */   }
/*    */ 
/*    */   public Localizable getMessage(String key, Object[] args) {
/* 40 */     return new LocalizableMessage(this._bundlename, key, args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory
 * JD-Core Version:    0.6.2
 */