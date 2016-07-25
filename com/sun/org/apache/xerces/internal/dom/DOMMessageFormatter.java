/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class DOMMessageFormatter
/*     */ {
/*     */   public static final String DOM_DOMAIN = "http://www.w3.org/dom/DOMTR";
/*     */   public static final String XML_DOMAIN = "http://www.w3.org/TR/1998/REC-xml-19980210";
/*     */   public static final String SERIALIZER_DOMAIN = "http://apache.org/xml/serializer";
/*  41 */   private static ResourceBundle domResourceBundle = null;
/*  42 */   private static ResourceBundle xmlResourceBundle = null;
/*  43 */   private static ResourceBundle serResourceBundle = null;
/*  44 */   private static Locale locale = null;
/*     */ 
/*     */   DOMMessageFormatter()
/*     */   {
/*  48 */     locale = Locale.getDefault();
/*     */   }
/*     */ 
/*     */   public static String formatMessage(String domain, String key, Object[] arguments)
/*     */     throws MissingResourceException
/*     */   {
/*  68 */     ResourceBundle resourceBundle = getResourceBundle(domain);
/*  69 */     if (resourceBundle == null) {
/*  70 */       init();
/*  71 */       resourceBundle = getResourceBundle(domain);
/*  72 */       if (resourceBundle == null)
/*  73 */         throw new MissingResourceException("Unknown domain" + domain, null, key);
/*     */     }
/*     */     String msg;
/*     */     try
/*     */     {
/*  78 */       msg = key + ": " + resourceBundle.getString(key);
/*  79 */       if (arguments != null)
/*     */         try {
/*  81 */           msg = MessageFormat.format(msg, arguments);
/*     */         }
/*     */         catch (Exception e) {
/*  84 */           msg = resourceBundle.getString("FormatFailed");
/*  85 */           msg = msg + " " + resourceBundle.getString(key);
/*     */         }
/*     */     }
/*     */     catch (MissingResourceException e)
/*     */     {
/*  90 */       msg = resourceBundle.getString("BadMessageKey");
/*  91 */       throw new MissingResourceException(key, msg, key);
/*     */     }
/*     */ 
/*  95 */     if (msg == null) {
/*  96 */       msg = key;
/*  97 */       if (arguments.length > 0) {
/*  98 */         StringBuffer str = new StringBuffer(msg);
/*  99 */         str.append('?');
/* 100 */         for (int i = 0; i < arguments.length; i++) {
/* 101 */           if (i > 0) {
/* 102 */             str.append('&');
/*     */           }
/* 104 */           str.append(String.valueOf(arguments[i]));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 109 */     return msg;
/*     */   }
/*     */ 
/*     */   static ResourceBundle getResourceBundle(String domain) {
/* 113 */     if ((domain == "http://www.w3.org/dom/DOMTR") || (domain.equals("http://www.w3.org/dom/DOMTR")))
/* 114 */       return domResourceBundle;
/* 115 */     if ((domain == "http://www.w3.org/TR/1998/REC-xml-19980210") || (domain.equals("http://www.w3.org/TR/1998/REC-xml-19980210")))
/* 116 */       return xmlResourceBundle;
/* 117 */     if ((domain == "http://apache.org/xml/serializer") || (domain.equals("http://apache.org/xml/serializer")))
/* 118 */       return serResourceBundle;
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   public static void init()
/*     */   {
/* 125 */     if (locale != null) {
/* 126 */       domResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.DOMMessages", locale);
/* 127 */       serResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSerializerMessages", locale);
/* 128 */       xmlResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLMessages", locale);
/*     */     } else {
/* 130 */       domResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.DOMMessages");
/* 131 */       serResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSerializerMessages");
/* 132 */       xmlResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLMessages");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void setLocale(Locale dlocale)
/*     */   {
/* 141 */     locale = dlocale;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter
 * JD-Core Version:    0.6.2
 */