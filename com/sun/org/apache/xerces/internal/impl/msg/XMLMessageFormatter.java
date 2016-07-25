/*     */ package com.sun.org.apache.xerces.internal.impl.msg;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class XMLMessageFormatter
/*     */   implements MessageFormatter
/*     */ {
/*     */   public static final String XML_DOMAIN = "http://www.w3.org/TR/1998/REC-xml-19980210";
/*     */   public static final String XMLNS_DOMAIN = "http://www.w3.org/TR/1999/REC-xml-names-19990114";
/*  47 */   private Locale fLocale = null;
/*  48 */   private ResourceBundle fResourceBundle = null;
/*     */ 
/*     */   public String formatMessage(Locale locale, String key, Object[] arguments)
/*     */     throws MissingResourceException
/*     */   {
/*  72 */     if ((this.fResourceBundle == null) || (locale != this.fLocale)) {
/*  73 */       if (locale != null) {
/*  74 */         this.fResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLMessages", locale);
/*     */ 
/*  76 */         this.fLocale = locale;
/*     */       }
/*  78 */       if (this.fResourceBundle == null) {
/*  79 */         this.fResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLMessages");
/*     */       }
/*     */     }
/*     */     String msg;
/*     */     try
/*     */     {
/*  85 */       msg = this.fResourceBundle.getString(key);
/*  86 */       if (arguments != null) {
/*     */         try {
/*  88 */           msg = MessageFormat.format(msg, arguments);
/*     */         }
/*     */         catch (Exception e) {
/*  91 */           msg = this.fResourceBundle.getString("FormatFailed");
/*  92 */           msg = msg + " " + this.fResourceBundle.getString(key);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (MissingResourceException e)
/*     */     {
/*  99 */       msg = this.fResourceBundle.getString("BadMessageKey");
/* 100 */       throw new MissingResourceException(key, msg, key);
/*     */     }
/*     */ 
/* 104 */     if (msg == null) {
/* 105 */       msg = key;
/* 106 */       if (arguments.length > 0) {
/* 107 */         StringBuffer str = new StringBuffer(msg);
/* 108 */         str.append('?');
/* 109 */         for (int i = 0; i < arguments.length; i++) {
/* 110 */           if (i > 0) {
/* 111 */             str.append('&');
/*     */           }
/* 113 */           str.append(String.valueOf(arguments[i]));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 118 */     return msg;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter
 * JD-Core Version:    0.6.2
 */