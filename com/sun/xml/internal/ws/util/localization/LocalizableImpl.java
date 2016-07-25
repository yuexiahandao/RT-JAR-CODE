/*    */ package com.sun.xml.internal.ws.util.localization;
/*    */ 
/*    */ public final class LocalizableImpl
/*    */   implements Localizable
/*    */ {
/*    */   private final String key;
/*    */   private final Object[] arguments;
/*    */   private final String resourceBundleName;
/*    */ 
/*    */   public LocalizableImpl(String key, Object[] arguments, String resourceBundleName)
/*    */   {
/* 39 */     this.key = key;
/* 40 */     this.arguments = arguments;
/* 41 */     this.resourceBundleName = resourceBundleName;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 45 */     return this.key;
/*    */   }
/*    */ 
/*    */   public Object[] getArguments() {
/* 49 */     return this.arguments;
/*    */   }
/*    */ 
/*    */   public String getResourceBundleName() {
/* 53 */     return this.resourceBundleName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.localization.LocalizableImpl
 * JD-Core Version:    0.6.2
 */