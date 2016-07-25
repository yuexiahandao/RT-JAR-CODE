/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public abstract class ResponseCache
/*     */ {
/*     */   private static ResponseCache theResponseCache;
/*     */ 
/*     */   public static synchronized ResponseCache getDefault()
/*     */   {
/*  84 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  85 */     if (localSecurityManager != null) {
/*  86 */       localSecurityManager.checkPermission(SecurityConstants.GET_RESPONSECACHE_PERMISSION);
/*     */     }
/*  88 */     return theResponseCache;
/*     */   }
/*     */ 
/*     */   public static synchronized void setDefault(ResponseCache paramResponseCache)
/*     */   {
/* 107 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 108 */     if (localSecurityManager != null) {
/* 109 */       localSecurityManager.checkPermission(SecurityConstants.SET_RESPONSECACHE_PERMISSION);
/*     */     }
/* 111 */     theResponseCache = paramResponseCache;
/*     */   }
/*     */ 
/*     */   public abstract CacheResponse get(URI paramURI, String paramString, Map<String, List<String>> paramMap)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract CacheRequest put(URI paramURI, URLConnection paramURLConnection)
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.ResponseCache
 * JD-Core Version:    0.6.2
 */