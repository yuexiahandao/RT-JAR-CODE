/*    */ package com.sun.org.apache.xerces.internal.impl.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*    */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*    */ import java.text.MessageFormat;
/*    */ import java.util.Locale;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class XSMessageFormatter
/*    */   implements MessageFormatter
/*    */ {
/*    */   public static final String SCHEMA_DOMAIN = "http://www.w3.org/TR/xml-schema-1";
/* 47 */   private Locale fLocale = null;
/* 48 */   private ResourceBundle fResourceBundle = null;
/*    */ 
/*    */   public String formatMessage(Locale locale, String key, Object[] arguments)
/*    */     throws MissingResourceException
/*    */   {
/* 68 */     if ((this.fResourceBundle == null) || (locale != this.fLocale)) {
/* 69 */       if (locale != null) {
/* 70 */         this.fResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages", locale);
/*    */ 
/* 72 */         this.fLocale = locale;
/*    */       }
/* 74 */       if (this.fResourceBundle == null) {
/* 75 */         this.fResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages");
/*    */       }
/*    */     }
/* 78 */     String msg = this.fResourceBundle.getString(key);
/* 79 */     if (arguments != null) {
/*    */       try {
/* 81 */         msg = MessageFormat.format(msg, arguments);
/*    */       } catch (Exception e) {
/* 83 */         msg = this.fResourceBundle.getString("FormatFailed");
/* 84 */         msg = msg + " " + this.fResourceBundle.getString(key);
/*    */       }
/*    */     }
/*    */ 
/* 88 */     if (msg == null) {
/* 89 */       msg = this.fResourceBundle.getString("BadMessageKey");
/* 90 */       throw new MissingResourceException(msg, "com.sun.org.apache.xerces.internal.impl.msg.SchemaMessages", key);
/*    */     }
/*    */ 
/* 93 */     return msg;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter
 * JD-Core Version:    0.6.2
 */