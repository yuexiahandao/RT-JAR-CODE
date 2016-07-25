/*    */ package com.sun.org.apache.xalan.internal.res;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*    */ import com.sun.org.apache.xpath.internal.res.XPATHMessages;
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ public class XSLMessages extends XPATHMessages
/*    */ {
/* 41 */   private static ListResourceBundle XSLTBundle = null;
/*    */   private static final String XSLT_ERROR_RESOURCES = "com.sun.org.apache.xalan.internal.res.XSLTErrorResources";
/*    */ 
/*    */   public static String createMessage(String msgKey, Object[] args)
/*    */   {
/* 60 */     if (XSLTBundle == null) {
/* 61 */       XSLTBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xalan.internal.res.XSLTErrorResources");
/*    */     }
/*    */ 
/* 64 */     if (XSLTBundle != null) {
/* 65 */       return createMsg(XSLTBundle, msgKey, args);
/*    */     }
/* 67 */     return "Could not load any resource bundles.";
/*    */   }
/*    */ 
/*    */   public static String createWarning(String msgKey, Object[] args)
/*    */   {
/* 83 */     if (XSLTBundle == null) {
/* 84 */       XSLTBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xalan.internal.res.XSLTErrorResources");
/*    */     }
/*    */ 
/* 87 */     if (XSLTBundle != null) {
/* 88 */       return createMsg(XSLTBundle, msgKey, args);
/*    */     }
/* 90 */     return "Could not load any resource bundles.";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.res.XSLMessages
 * JD-Core Version:    0.6.2
 */