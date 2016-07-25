/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class LocaleUtility
/*    */ {
/*    */   public static final char IETF_SEPARATOR = '-';
/*    */   public static final String EMPTY_STRING = "";
/*    */ 
/*    */   public static Locale langToLocale(String lang)
/*    */   {
/* 42 */     if ((lang == null) || (lang.equals(""))) {
/* 43 */       return Locale.getDefault();
/*    */     }
/* 45 */     String language = "";
/* 46 */     String country = "";
/* 47 */     String variant = "";
/*    */ 
/* 49 */     int i1 = lang.indexOf('-');
/* 50 */     if (i1 < 0) {
/* 51 */       language = lang;
/*    */     } else {
/* 53 */       language = lang.substring(0, i1);
/* 54 */       i1++;
/* 55 */       int i2 = lang.indexOf('-', i1);
/* 56 */       if (i2 < 0) {
/* 57 */         country = lang.substring(i1);
/*    */       } else {
/* 59 */         country = lang.substring(i1, i2);
/* 60 */         variant = lang.substring(i2 + 1);
/*    */       }
/*    */     }
/*    */ 
/* 64 */     if (language.length() == 2)
/* 65 */       language = language.toLowerCase();
/*    */     else {
/* 67 */       language = "";
/*    */     }
/*    */ 
/* 70 */     if (country.length() == 2)
/* 71 */       country = country.toUpperCase();
/*    */     else {
/* 73 */       country = "";
/*    */     }
/*    */ 
/* 76 */     if ((variant.length() > 0) && ((language.length() == 2) || (country.length() == 2)))
/*    */     {
/* 78 */       variant = variant.toUpperCase();
/*    */     }
/* 80 */     else variant = "";
/*    */ 
/* 83 */     return new Locale(language, country, variant);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.LocaleUtility
 * JD-Core Version:    0.6.2
 */