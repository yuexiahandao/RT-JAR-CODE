/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ 
/*     */ public abstract class HttpsURLConnection extends HttpURLConnection
/*     */ {
/* 191 */   private static HostnameVerifier defaultHostnameVerifier = new DefaultHostnameVerifier(null);
/*     */ 
/* 209 */   protected HostnameVerifier hostnameVerifier = defaultHostnameVerifier;
/*     */ 
/* 285 */   private static SSLSocketFactory defaultSSLSocketFactory = null;
/*     */ 
/* 291 */   private SSLSocketFactory sslSocketFactory = getDefaultSSLSocketFactory();
/*     */ 
/*     */   protected HttpsURLConnection(URL paramURL)
/*     */   {
/*  66 */     super(paramURL);
/*     */   }
/*     */ 
/*     */   public abstract String getCipherSuite();
/*     */ 
/*     */   public abstract Certificate[] getLocalCertificates();
/*     */ 
/*     */   public abstract Certificate[] getServerCertificates()
/*     */     throws SSLPeerUnverifiedException;
/*     */ 
/*     */   public Principal getPeerPrincipal()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 146 */     Certificate[] arrayOfCertificate = getServerCertificates();
/* 147 */     return ((X509Certificate)arrayOfCertificate[0]).getSubjectX500Principal();
/*     */   }
/*     */ 
/*     */   public Principal getLocalPrincipal()
/*     */   {
/* 174 */     Certificate[] arrayOfCertificate = getLocalCertificates();
/* 175 */     if (arrayOfCertificate != null) {
/* 176 */       return ((X509Certificate)arrayOfCertificate[0]).getSubjectX500Principal();
/*     */     }
/*     */ 
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   public static void setDefaultHostnameVerifier(HostnameVerifier paramHostnameVerifier)
/*     */   {
/* 228 */     if (paramHostnameVerifier == null) {
/* 229 */       throw new IllegalArgumentException("no default HostnameVerifier specified");
/*     */     }
/*     */ 
/* 233 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 234 */     if (localSecurityManager != null) {
/* 235 */       localSecurityManager.checkPermission(new SSLPermission("setHostnameVerifier"));
/*     */     }
/* 237 */     defaultHostnameVerifier = paramHostnameVerifier;
/*     */   }
/*     */ 
/*     */   public static HostnameVerifier getDefaultHostnameVerifier()
/*     */   {
/* 248 */     return defaultHostnameVerifier;
/*     */   }
/*     */ 
/*     */   public void setHostnameVerifier(HostnameVerifier paramHostnameVerifier)
/*     */   {
/* 266 */     if (paramHostnameVerifier == null) {
/* 267 */       throw new IllegalArgumentException("no HostnameVerifier specified");
/*     */     }
/*     */ 
/* 271 */     this.hostnameVerifier = paramHostnameVerifier;
/*     */   }
/*     */ 
/*     */   public HostnameVerifier getHostnameVerifier()
/*     */   {
/* 282 */     return this.hostnameVerifier;
/*     */   }
/*     */ 
/*     */   public static void setDefaultSSLSocketFactory(SSLSocketFactory paramSSLSocketFactory)
/*     */   {
/* 309 */     if (paramSSLSocketFactory == null) {
/* 310 */       throw new IllegalArgumentException("no default SSLSocketFactory specified");
/*     */     }
/*     */ 
/* 314 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 315 */     if (localSecurityManager != null) {
/* 316 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 318 */     defaultSSLSocketFactory = paramSSLSocketFactory;
/*     */   }
/*     */ 
/*     */   public static SSLSocketFactory getDefaultSSLSocketFactory()
/*     */   {
/* 332 */     if (defaultSSLSocketFactory == null) {
/* 333 */       defaultSSLSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */     }
/*     */ 
/* 336 */     return defaultSSLSocketFactory;
/*     */   }
/*     */ 
/*     */   public void setSSLSocketFactory(SSLSocketFactory paramSSLSocketFactory)
/*     */   {
/* 355 */     if (paramSSLSocketFactory == null) {
/* 356 */       throw new IllegalArgumentException("no SSLSocketFactory specified");
/*     */     }
/*     */ 
/* 360 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 361 */     if (localSecurityManager != null) {
/* 362 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 364 */     this.sslSocketFactory = paramSSLSocketFactory;
/*     */   }
/*     */ 
/*     */   public SSLSocketFactory getSSLSocketFactory()
/*     */   {
/* 375 */     return this.sslSocketFactory;
/*     */   }
/*     */ 
/*     */   private static class DefaultHostnameVerifier
/*     */     implements HostnameVerifier
/*     */   {
/*     */     public boolean verify(String paramString, SSLSession paramSSLSession)
/*     */     {
/* 202 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.HttpsURLConnection
 * JD-Core Version:    0.6.2
 */