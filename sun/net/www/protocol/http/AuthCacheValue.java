/*    */ package sun.net.www.protocol.http;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.net.PasswordAuthentication;
/*    */ 
/*    */ public abstract class AuthCacheValue
/*    */   implements Serializable
/*    */ {
/* 48 */   protected static AuthCache cache = new AuthCacheImpl();
/*    */ 
/*    */   public static void setAuthCache(AuthCache paramAuthCache) {
/* 51 */     cache = paramAuthCache;
/*    */   }
/*    */ 
/*    */   abstract Type getAuthType();
/*    */ 
/*    */   abstract AuthScheme getAuthScheme();
/*    */ 
/*    */   abstract String getHost();
/*    */ 
/*    */   abstract int getPort();
/*    */ 
/*    */   abstract String getRealm();
/*    */ 
/*    */   abstract String getPath();
/*    */ 
/*    */   abstract String getProtocolScheme();
/*    */ 
/*    */   abstract PasswordAuthentication credentials();
/*    */ 
/*    */   public static enum Type
/*    */   {
/* 41 */     Proxy, 
/* 42 */     Server;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.AuthCacheValue
 * JD-Core Version:    0.6.2
 */