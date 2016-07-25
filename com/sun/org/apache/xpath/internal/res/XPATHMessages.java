/*     */ package com.sun.org.apache.xpath.internal.res;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ListResourceBundle;
/*     */ 
/*     */ public class XPATHMessages extends XMLMessages
/*     */ {
/*  39 */   private static ListResourceBundle XPATHBundle = null;
/*     */   private static final String XPATH_ERROR_RESOURCES = "com.sun.org.apache.xpath.internal.res.XPATHErrorResources";
/*     */ 
/*     */   public static final String createXPATHMessage(String msgKey, Object[] args)
/*     */   {
/*  58 */     if (XPATHBundle == null) {
/*  59 */       XPATHBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xpath.internal.res.XPATHErrorResources");
/*     */     }
/*     */ 
/*  62 */     if (XPATHBundle != null) {
/*  63 */       return createXPATHMsg(XPATHBundle, msgKey, args);
/*     */     }
/*  65 */     return "Could not load any resource bundles.";
/*     */   }
/*     */ 
/*     */   public static final String createXPATHWarning(String msgKey, Object[] args)
/*     */   {
/*  81 */     if (XPATHBundle == null) {
/*  82 */       XPATHBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xpath.internal.res.XPATHErrorResources");
/*     */     }
/*     */ 
/*  85 */     if (XPATHBundle != null) {
/*  86 */       return createXPATHMsg(XPATHBundle, msgKey, args);
/*     */     }
/*  88 */     return "Could not load any resource bundles.";
/*     */   }
/*     */ 
/*     */   public static final String createXPATHMsg(ListResourceBundle fResourceBundle, String msgKey, Object[] args)
/*     */   {
/* 107 */     String fmsg = null;
/* 108 */     boolean throwex = false;
/* 109 */     String msg = null;
/*     */ 
/* 111 */     if (msgKey != null) {
/* 112 */       msg = fResourceBundle.getString(msgKey);
/*     */     }
/*     */ 
/* 115 */     if (msg == null) {
/* 116 */       msg = fResourceBundle.getString("BAD_CODE");
/* 117 */       throwex = true;
/*     */     }
/*     */ 
/* 120 */     if (args != null)
/*     */     {
/*     */       try
/*     */       {
/* 126 */         int n = args.length;
/*     */ 
/* 128 */         for (int i = 0; i < n; i++) {
/* 129 */           if (null == args[i]) {
/* 130 */             args[i] = "";
/*     */           }
/*     */         }
/*     */ 
/* 134 */         fmsg = MessageFormat.format(msg, args);
/*     */       } catch (Exception e) {
/* 136 */         fmsg = fResourceBundle.getString("FORMAT_FAILED");
/* 137 */         fmsg = fmsg + " " + msg;
/*     */       }
/*     */     }
/* 140 */     else fmsg = msg;
/*     */ 
/* 143 */     if (throwex) {
/* 144 */       throw new RuntimeException(fmsg);
/*     */     }
/*     */ 
/* 147 */     return fmsg;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.res.XPATHMessages
 * JD-Core Version:    0.6.2
 */