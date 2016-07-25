/*    */ package com.sun.script.javascript;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import sun.org.mozilla.javascript.internal.ClassShutter;
/*    */ 
/*    */ final class RhinoClassShutter
/*    */   implements ClassShutter
/*    */ {
/*    */   private static Map<String, Boolean> protectedClasses;
/*    */   private static RhinoClassShutter theInstance;
/*    */ 
/*    */   static synchronized ClassShutter getInstance()
/*    */   {
/* 47 */     if (theInstance == null) {
/* 48 */       theInstance = new RhinoClassShutter();
/* 49 */       protectedClasses = new HashMap();
/*    */ 
/* 54 */       protectedClasses.put("java.security.AccessController", Boolean.TRUE);
/*    */     }
/* 56 */     return theInstance;
/*    */   }
/*    */ 
/*    */   public boolean visibleToScripts(String paramString)
/*    */   {
/* 61 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 62 */     if (localSecurityManager != null) {
/* 63 */       int i = paramString.lastIndexOf(".");
/* 64 */       if (i != -1) {
/*    */         try {
/* 66 */           localSecurityManager.checkPackageAccess(paramString.substring(0, i));
/*    */         } catch (SecurityException localSecurityException) {
/* 68 */           return false;
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 73 */     return protectedClasses.get(paramString) == null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.RhinoClassShutter
 * JD-Core Version:    0.6.2
 */