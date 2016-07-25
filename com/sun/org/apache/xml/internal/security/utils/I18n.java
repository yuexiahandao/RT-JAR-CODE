/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.Init;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class I18n
/*     */ {
/*     */   public static final String NOT_INITIALIZED_MSG = "You must initialize the xml-security library correctly before you use it. Call the static method \"com.sun.org.apache.xml.internal.security.Init.init();\" to do that before you use any functionality from that library.";
/*     */   private static ResourceBundle resourceBundle;
/*  46 */   private static boolean alreadyInitialized = false;
/*     */ 
/*     */   public static String translate(String paramString, Object[] paramArrayOfObject)
/*     */   {
/*  70 */     return getExceptionMessage(paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public static String translate(String paramString)
/*     */   {
/*  83 */     return getExceptionMessage(paramString);
/*     */   }
/*     */ 
/*     */   public static String getExceptionMessage(String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  95 */       return resourceBundle.getString(paramString);
/*     */     } catch (Throwable localThrowable) {
/*  97 */       if (Init.isInitialized()) {
/*  98 */         return "No message with ID \"" + paramString + "\" found in resource bundle \"" + "com/sun/org/apache/xml/internal/security/resource/xmlsecurity" + "\"";
/*     */       }
/*     */     }
/*     */ 
/* 102 */     return "You must initialize the xml-security library correctly before you use it. Call the static method \"com.sun.org.apache.xml.internal.security.Init.init();\" to do that before you use any functionality from that library.";
/*     */   }
/*     */ 
/*     */   public static String getExceptionMessage(String paramString, Exception paramException)
/*     */   {
/*     */     try
/*     */     {
/* 115 */       Object[] arrayOfObject = { paramException.getMessage() };
/* 116 */       return MessageFormat.format(resourceBundle.getString(paramString), arrayOfObject);
/*     */     } catch (Throwable localThrowable) {
/* 118 */       if (Init.isInitialized()) {
/* 119 */         return "No message with ID \"" + paramString + "\" found in resource bundle \"" + "com/sun/org/apache/xml/internal/security/resource/xmlsecurity" + "\". Original Exception was a " + paramException.getClass().getName() + " and message " + paramException.getMessage();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 126 */     return "You must initialize the xml-security library correctly before you use it. Call the static method \"com.sun.org.apache.xml.internal.security.Init.init();\" to do that before you use any functionality from that library.";
/*     */   }
/*     */ 
/*     */   public static String getExceptionMessage(String paramString, Object[] paramArrayOfObject)
/*     */   {
/*     */     try
/*     */     {
/* 139 */       return MessageFormat.format(resourceBundle.getString(paramString), paramArrayOfObject);
/*     */     } catch (Throwable localThrowable) {
/* 141 */       if (Init.isInitialized()) {
/* 142 */         return "No message with ID \"" + paramString + "\" found in resource bundle \"" + "com/sun/org/apache/xml/internal/security/resource/xmlsecurity" + "\"";
/*     */       }
/*     */     }
/*     */ 
/* 146 */     return "You must initialize the xml-security library correctly before you use it. Call the static method \"com.sun.org.apache.xml.internal.security.Init.init();\" to do that before you use any functionality from that library.";
/*     */   }
/*     */ 
/*     */   public static synchronized void init(String paramString1, String paramString2)
/*     */   {
/* 157 */     if (alreadyInitialized) {
/* 158 */       return;
/*     */     }
/*     */ 
/* 161 */     resourceBundle = ResourceBundle.getBundle("com/sun/org/apache/xml/internal/security/resource/xmlsecurity", new Locale(paramString1, paramString2));
/*     */ 
/* 166 */     alreadyInitialized = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.I18n
 * JD-Core Version:    0.6.2
 */