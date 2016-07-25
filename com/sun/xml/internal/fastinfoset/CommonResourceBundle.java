/*    */ package com.sun.xml.internal.fastinfoset;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class CommonResourceBundle extends AbstractResourceBundle
/*    */ {
/*    */   public static final String BASE_NAME = "com.sun.xml.internal.fastinfoset.resources.ResourceBundle";
/* 38 */   private static CommonResourceBundle instance = null;
/* 39 */   private static Locale locale = null;
/* 40 */   private ResourceBundle bundle = null;
/*    */ 
/*    */   protected CommonResourceBundle()
/*    */   {
/* 44 */     this.bundle = ResourceBundle.getBundle("com.sun.xml.internal.fastinfoset.resources.ResourceBundle");
/*    */   }
/*    */ 
/*    */   protected CommonResourceBundle(Locale locale)
/*    */   {
/* 49 */     this.bundle = ResourceBundle.getBundle("com.sun.xml.internal.fastinfoset.resources.ResourceBundle", locale);
/*    */   }
/*    */ 
/*    */   public static CommonResourceBundle getInstance() {
/* 53 */     if (instance == null) {
/* 54 */       synchronized (CommonResourceBundle.class) {
/* 55 */         instance = new CommonResourceBundle();
/*    */ 
/* 59 */         String localeString = null;
/* 60 */         locale = parseLocale(localeString);
/*    */       }
/*    */     }
/*    */ 
/* 64 */     return instance;
/*    */   }
/*    */ 
/*    */   public static CommonResourceBundle getInstance(Locale locale) {
/* 68 */     if (instance == null)
/* 69 */       synchronized (CommonResourceBundle.class) {
/* 70 */         instance = new CommonResourceBundle(locale);
/*    */       }
/*    */     else {
/* 73 */       synchronized (CommonResourceBundle.class) {
/* 74 */         if (locale != locale) {
/* 75 */           instance = new CommonResourceBundle(locale);
/*    */         }
/*    */       }
/*    */     }
/* 79 */     return instance;
/*    */   }
/*    */ 
/*    */   public ResourceBundle getBundle()
/*    */   {
/* 84 */     return this.bundle;
/*    */   }
/*    */   public ResourceBundle getBundle(Locale locale) {
/* 87 */     return ResourceBundle.getBundle("com.sun.xml.internal.fastinfoset.resources.ResourceBundle", locale);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.CommonResourceBundle
 * JD-Core Version:    0.6.2
 */