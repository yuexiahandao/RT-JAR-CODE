/*    */ package java.net;
/*    */ 
/*    */ public abstract interface CookiePolicy
/*    */ {
/* 42 */   public static final CookiePolicy ACCEPT_ALL = new CookiePolicy() {
/*    */     public boolean shouldAccept(URI paramAnonymousURI, HttpCookie paramAnonymousHttpCookie) {
/* 44 */       return true;
/*    */     }
/* 42 */   };
/*    */ 
/* 51 */   public static final CookiePolicy ACCEPT_NONE = new CookiePolicy() {
/*    */     public boolean shouldAccept(URI paramAnonymousURI, HttpCookie paramAnonymousHttpCookie) {
/* 53 */       return false;
/*    */     }
/* 51 */   };
/*    */ 
/* 60 */   public static final CookiePolicy ACCEPT_ORIGINAL_SERVER = new CookiePolicy() {
/*    */     public boolean shouldAccept(URI paramAnonymousURI, HttpCookie paramAnonymousHttpCookie) {
/* 62 */       return HttpCookie.domainMatches(paramAnonymousHttpCookie.getDomain(), paramAnonymousURI.getHost());
/*    */     }
/* 60 */   };
/*    */ 
/*    */   public abstract boolean shouldAccept(URI paramURI, HttpCookie paramHttpCookie);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.CookiePolicy
 * JD-Core Version:    0.6.2
 */