/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public abstract class ProxySelector
/*     */ {
/*     */   private static ProxySelector theProxySelector;
/*     */ 
/*     */   public static ProxySelector getDefault()
/*     */   {
/*  92 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  93 */     if (localSecurityManager != null) {
/*  94 */       localSecurityManager.checkPermission(SecurityConstants.GET_PROXYSELECTOR_PERMISSION);
/*     */     }
/*  96 */     return theProxySelector;
/*     */   }
/*     */ 
/*     */   public static void setDefault(ProxySelector paramProxySelector)
/*     */   {
/* 115 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 116 */     if (localSecurityManager != null) {
/* 117 */       localSecurityManager.checkPermission(SecurityConstants.SET_PROXYSELECTOR_PERMISSION);
/*     */     }
/* 119 */     theProxySelector = paramProxySelector;
/*     */   }
/*     */ 
/*     */   public abstract List<Proxy> select(URI paramURI);
/*     */ 
/*     */   public abstract void connectFailed(URI paramURI, SocketAddress paramSocketAddress, IOException paramIOException);
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  72 */       Class localClass = Class.forName("sun.net.spi.DefaultProxySelector");
/*  73 */       if ((localClass != null) && (ProxySelector.class.isAssignableFrom(localClass)))
/*  74 */         theProxySelector = (ProxySelector)localClass.newInstance();
/*     */     }
/*     */     catch (Exception localException) {
/*  77 */       theProxySelector = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.ProxySelector
 * JD-Core Version:    0.6.2
 */