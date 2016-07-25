/*    */ package java.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import sun.security.util.SecurityConstants;
/*    */ 
/*    */ public abstract class CookieHandler
/*    */ {
/*    */   private static CookieHandler cookieHandler;
/*    */ 
/*    */   public static synchronized CookieHandler getDefault()
/*    */   {
/* 73 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 74 */     if (localSecurityManager != null) {
/* 75 */       localSecurityManager.checkPermission(SecurityConstants.GET_COOKIEHANDLER_PERMISSION);
/*    */     }
/* 77 */     return cookieHandler;
/*    */   }
/*    */ 
/*    */   public static synchronized void setDefault(CookieHandler paramCookieHandler)
/*    */   {
/* 93 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 94 */     if (localSecurityManager != null) {
/* 95 */       localSecurityManager.checkPermission(SecurityConstants.SET_COOKIEHANDLER_PERMISSION);
/*    */     }
/* 97 */     cookieHandler = paramCookieHandler;
/*    */   }
/*    */ 
/*    */   public abstract Map<String, List<String>> get(URI paramURI, Map<String, List<String>> paramMap)
/*    */     throws IOException;
/*    */ 
/*    */   public abstract void put(URI paramURI, Map<String, List<String>> paramMap)
/*    */     throws IOException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.CookieHandler
 * JD-Core Version:    0.6.2
 */