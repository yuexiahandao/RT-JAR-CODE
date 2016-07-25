/*    */ package com.sun.imageio.plugins.common;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.PropertyResourceBundle;
/*    */ 
/*    */ public class I18NImpl
/*    */ {
/*    */   protected static final String getString(String paramString1, String paramString2, String paramString3)
/*    */   {
/* 51 */     PropertyResourceBundle localPropertyResourceBundle = null;
/*    */     try {
/* 53 */       InputStream localInputStream = Class.forName(paramString1).getResourceAsStream(paramString2);
/*    */ 
/* 55 */       localPropertyResourceBundle = new PropertyResourceBundle(localInputStream);
/*    */     } catch (Throwable localThrowable) {
/* 57 */       throw new RuntimeException(localThrowable);
/*    */     }
/*    */ 
/* 60 */     return (String)localPropertyResourceBundle.handleGetObject(paramString3);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.common.I18NImpl
 * JD-Core Version:    0.6.2
 */