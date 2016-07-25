/*    */ package com.sun.org.apache.xerces.internal.xinclude;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*    */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*    */ import java.text.MessageFormat;
/*    */ import java.util.Locale;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class XIncludeMessageFormatter
/*    */   implements MessageFormatter
/*    */ {
/*    */   public static final String XINCLUDE_DOMAIN = "http://www.w3.org/TR/xinclude";
/* 42 */   private Locale fLocale = null;
/* 43 */   private ResourceBundle fResourceBundle = null;
/*    */ 
/*    */   public String formatMessage(Locale locale, String key, Object[] arguments)
/*    */     throws MissingResourceException
/*    */   {
/* 63 */     if ((this.fResourceBundle == null) || (locale != this.fLocale)) {
/* 64 */       if (locale != null) {
/* 65 */         this.fResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages", locale);
/*    */ 
/* 67 */         this.fLocale = locale;
/*    */       }
/* 69 */       if (this.fResourceBundle == null) {
/* 70 */         this.fResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages");
/*    */       }
/*    */     }
/* 73 */     String msg = this.fResourceBundle.getString(key);
/* 74 */     if (arguments != null) {
/*    */       try {
/* 76 */         msg = MessageFormat.format(msg, arguments);
/*    */       } catch (Exception e) {
/* 78 */         msg = this.fResourceBundle.getString("FormatFailed");
/* 79 */         msg = msg + " " + this.fResourceBundle.getString(key);
/*    */       }
/*    */     }
/*    */ 
/* 83 */     if (msg == null) {
/* 84 */       msg = this.fResourceBundle.getString("BadMessageKey");
/* 85 */       throw new MissingResourceException(msg, "com.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages", key);
/*    */     }
/*    */ 
/* 88 */     return msg;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xinclude.XIncludeMessageFormatter
 * JD-Core Version:    0.6.2
 */