/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.security.cert.X509Certificate;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class HttpsURLConnection extends HttpURLConnection
/*     */ {
/*  83 */   private static HostnameVerifier defaultHostnameVerifier = new HostnameVerifier()
/*     */   {
/*     */     public boolean verify(String paramAnonymousString1, String paramAnonymousString2) {
/*  86 */       return false;
/*     */     }
/*  83 */   };
/*     */ 
/*  90 */   protected HostnameVerifier hostnameVerifier = defaultHostnameVerifier;
/*     */ 
/* 139 */   private static SSLSocketFactory defaultSSLSocketFactory = null;
/*     */ 
/* 141 */   private SSLSocketFactory sslSocketFactory = getDefaultSSLSocketFactory();
/*     */ 
/*     */   public HttpsURLConnection(URL paramURL)
/*     */     throws IOException
/*     */   {
/*  59 */     super(paramURL);
/*     */   }
/*     */ 
/*     */   public abstract String getCipherSuite();
/*     */ 
/*     */   public abstract X509Certificate[] getServerCertificateChain();
/*     */ 
/*     */   public static void setDefaultHostnameVerifier(HostnameVerifier paramHostnameVerifier)
/*     */   {
/*  98 */     if (paramHostnameVerifier == null) {
/*  99 */       throw new IllegalArgumentException("no default HostnameVerifier specified");
/*     */     }
/*     */ 
/* 103 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 104 */     if (localSecurityManager != null) {
/* 105 */       localSecurityManager.checkPermission(new SSLPermission("setHostnameVerifier"));
/*     */     }
/* 107 */     defaultHostnameVerifier = paramHostnameVerifier;
/*     */   }
/*     */ 
/*     */   public static HostnameVerifier getDefaultHostnameVerifier()
/*     */   {
/* 115 */     return defaultHostnameVerifier;
/*     */   }
/*     */ 
/*     */   public void setHostnameVerifier(HostnameVerifier paramHostnameVerifier)
/*     */   {
/* 123 */     if (paramHostnameVerifier == null) {
/* 124 */       throw new IllegalArgumentException("no HostnameVerifier specified");
/*     */     }
/*     */ 
/* 128 */     this.hostnameVerifier = paramHostnameVerifier;
/*     */   }
/*     */ 
/*     */   public HostnameVerifier getHostnameVerifier()
/*     */   {
/* 136 */     return this.hostnameVerifier;
/*     */   }
/*     */ 
/*     */   public static void setDefaultSSLSocketFactory(SSLSocketFactory paramSSLSocketFactory)
/*     */   {
/* 149 */     if (paramSSLSocketFactory == null) {
/* 150 */       throw new IllegalArgumentException("no default SSLSocketFactory specified");
/*     */     }
/*     */ 
/* 154 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 155 */     if (localSecurityManager != null) {
/* 156 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 158 */     defaultSSLSocketFactory = paramSSLSocketFactory;
/*     */   }
/*     */ 
/*     */   public static SSLSocketFactory getDefaultSSLSocketFactory()
/*     */   {
/* 166 */     if (defaultSSLSocketFactory == null) {
/* 167 */       defaultSSLSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */     }
/*     */ 
/* 170 */     return defaultSSLSocketFactory;
/*     */   }
/*     */ 
/*     */   public void setSSLSocketFactory(SSLSocketFactory paramSSLSocketFactory)
/*     */   {
/* 178 */     if (paramSSLSocketFactory == null) {
/* 179 */       throw new IllegalArgumentException("no SSLSocketFactory specified");
/*     */     }
/*     */ 
/* 183 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 184 */     if (localSecurityManager != null) {
/* 185 */       localSecurityManager.checkSetFactory();
/*     */     }
/*     */ 
/* 188 */     this.sslSocketFactory = paramSSLSocketFactory;
/*     */   }
/*     */ 
/*     */   public SSLSocketFactory getSSLSocketFactory()
/*     */   {
/* 196 */     return this.sslSocketFactory;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.HttpsURLConnection
 * JD-Core Version:    0.6.2
 */