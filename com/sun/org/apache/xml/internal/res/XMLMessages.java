/*     */ package com.sun.org.apache.xml.internal.res;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ListResourceBundle;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class XMLMessages
/*     */ {
/*  37 */   protected Locale fLocale = Locale.getDefault();
/*     */ 
/*  40 */   private static ListResourceBundle XMLBundle = null;
/*     */   private static final String XML_ERROR_RESOURCES = "com.sun.org.apache.xml.internal.res.XMLErrorResources";
/*     */   protected static final String BAD_CODE = "BAD_CODE";
/*     */   protected static final String FORMAT_FAILED = "FORMAT_FAILED";
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/*  59 */     this.fLocale = locale;
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/*  69 */     return this.fLocale;
/*     */   }
/*     */ 
/*     */   public static final String createXMLMessage(String msgKey, Object[] args)
/*     */   {
/*  84 */     if (XMLBundle == null) {
/*  85 */       XMLBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xml.internal.res.XMLErrorResources");
/*     */     }
/*     */ 
/*  88 */     if (XMLBundle != null)
/*     */     {
/*  90 */       return createMsg(XMLBundle, msgKey, args);
/*     */     }
/*     */ 
/*  93 */     return "Could not load any resource bundles.";
/*     */   }
/*     */ 
/*     */   public static final String createMsg(ListResourceBundle fResourceBundle, String msgKey, Object[] args)
/*     */   {
/* 111 */     String fmsg = null;
/* 112 */     boolean throwex = false;
/* 113 */     String msg = null;
/*     */ 
/* 115 */     if (msgKey != null) {
/* 116 */       msg = fResourceBundle.getString(msgKey);
/*     */     }
/* 118 */     if (msg == null)
/*     */     {
/* 120 */       msg = fResourceBundle.getString("BAD_CODE");
/* 121 */       throwex = true;
/*     */     }
/*     */ 
/* 124 */     if (args != null)
/*     */     {
/*     */       try
/*     */       {
/* 132 */         int n = args.length;
/*     */ 
/* 134 */         for (int i = 0; i < n; i++)
/*     */         {
/* 136 */           if (null == args[i]) {
/* 137 */             args[i] = "";
/*     */           }
/*     */         }
/* 140 */         fmsg = MessageFormat.format(msg, args);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 144 */         fmsg = fResourceBundle.getString("FORMAT_FAILED");
/* 145 */         fmsg = fmsg + " " + msg;
/*     */       }
/*     */     }
/*     */     else {
/* 149 */       fmsg = msg;
/*     */     }
/* 151 */     if (throwex)
/*     */     {
/* 153 */       throw new RuntimeException(fmsg);
/*     */     }
/*     */ 
/* 156 */     return fmsg;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.res.XMLMessages
 * JD-Core Version:    0.6.2
 */