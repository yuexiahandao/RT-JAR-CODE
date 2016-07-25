/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class DatatypeMessageFormatter
/*     */ {
/*     */   private static final String BASE_NAME = "com.sun.org.apache.xerces.internal.impl.msg.DatatypeMessages";
/*     */ 
/*     */   public static String formatMessage(Locale locale, String key, Object[] arguments)
/*     */     throws MissingResourceException
/*     */   {
/*  57 */     ResourceBundle resourceBundle = null;
/*  58 */     if (locale != null) {
/*  59 */       resourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.DatatypeMessages", locale);
/*     */     }
/*     */     else
/*     */     {
/*  63 */       resourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.DatatypeMessages");
/*     */     }
/*     */ 
/*     */     String msg;
/*     */     try
/*     */     {
/*  70 */       msg = resourceBundle.getString(key);
/*  71 */       if (arguments != null) {
/*     */         try {
/*  73 */           msg = MessageFormat.format(msg, arguments);
/*     */         }
/*     */         catch (Exception e) {
/*  76 */           msg = resourceBundle.getString("FormatFailed");
/*  77 */           msg = msg + " " + resourceBundle.getString(key);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (MissingResourceException e)
/*     */     {
/*  84 */       msg = resourceBundle.getString("BadMessageKey");
/*  85 */       throw new MissingResourceException(key, msg, key);
/*     */     }
/*     */ 
/*  89 */     if (msg == null) {
/*  90 */       msg = key;
/*  91 */       if (arguments.length > 0) {
/*  92 */         StringBuffer str = new StringBuffer(msg);
/*  93 */         str.append('?');
/*  94 */         for (int i = 0; i < arguments.length; i++) {
/*  95 */           if (i > 0) {
/*  96 */             str.append('&');
/*     */           }
/*  98 */           str.append(String.valueOf(arguments[i]));
/*     */         }
/*     */       }
/*     */     }
/* 102 */     return msg;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.DatatypeMessageFormatter
 * JD-Core Version:    0.6.2
 */