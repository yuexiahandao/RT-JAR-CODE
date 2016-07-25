/*    */ package com.sun.istack.internal.localization;
/*    */ 
/*    */ public final class LocalizableMessage
/*    */   implements Localizable
/*    */ {
/*    */   private final String _bundlename;
/*    */   private final String _key;
/*    */   private final Object[] _args;
/*    */ 
/*    */   public LocalizableMessage(String bundlename, String key, Object[] args)
/*    */   {
/* 38 */     this._bundlename = bundlename;
/* 39 */     this._key = key;
/* 40 */     if (args == null)
/* 41 */       args = new Object[0];
/* 42 */     this._args = args;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 46 */     return this._key;
/*    */   }
/*    */ 
/*    */   public Object[] getArguments() {
/* 50 */     return this._args;
/*    */   }
/*    */ 
/*    */   public String getResourceBundleName() {
/* 54 */     return this._bundlename;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.istack.internal.localization.LocalizableMessage
 * JD-Core Version:    0.6.2
 */