/*     */ package sun.net.www.protocol.https;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Proxy;
/*     */ import java.net.SecureCacheResponse;
/*     */ import java.net.URL;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.security.cert.X509Certificate;
/*     */ import sun.net.www.http.HttpClient;
/*     */ import sun.net.www.protocol.http.Handler;
/*     */ import sun.net.www.protocol.http.HttpURLConnection;
/*     */ 
/*     */ public abstract class AbstractDelegateHttpsURLConnection extends HttpURLConnection
/*     */ {
/*     */   protected AbstractDelegateHttpsURLConnection(URL paramURL, Handler paramHandler)
/*     */     throws IOException
/*     */   {
/*  50 */     this(paramURL, null, paramHandler);
/*     */   }
/*     */ 
/*     */   protected AbstractDelegateHttpsURLConnection(URL paramURL, Proxy paramProxy, Handler paramHandler) throws IOException
/*     */   {
/*  55 */     super(paramURL, paramProxy, paramHandler);
/*     */   }
/*     */ 
/*     */   protected abstract SSLSocketFactory getSSLSocketFactory();
/*     */ 
/*     */   protected abstract HostnameVerifier getHostnameVerifier();
/*     */ 
/*     */   public void setNewClient(URL paramURL)
/*     */     throws IOException
/*     */   {
/*  80 */     setNewClient(paramURL, false);
/*     */   }
/*     */ 
/*     */   public void setNewClient(URL paramURL, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  96 */     this.http = HttpsClient.New(getSSLSocketFactory(), paramURL, getHostnameVerifier(), paramBoolean, this);
/*     */ 
/* 100 */     ((HttpsClient)this.http).afterConnect();
/*     */   }
/*     */ 
/*     */   public void setProxiedClient(URL paramURL, String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 118 */     setProxiedClient(paramURL, paramString, paramInt, false);
/*     */   }
/*     */ 
/*     */   public void setProxiedClient(URL paramURL, String paramString, int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 138 */     proxiedConnect(paramURL, paramString, paramInt, paramBoolean);
/* 139 */     if (!this.http.isCachedConnection()) {
/* 140 */       doTunneling();
/*     */     }
/* 142 */     ((HttpsClient)this.http).afterConnect();
/*     */   }
/*     */ 
/*     */   protected void proxiedConnect(URL paramURL, String paramString, int paramInt, boolean paramBoolean) throws IOException
/*     */   {
/* 147 */     if (this.connected)
/* 148 */       return;
/* 149 */     this.http = HttpsClient.New(getSSLSocketFactory(), paramURL, getHostnameVerifier(), paramString, paramInt, paramBoolean, this);
/*     */ 
/* 153 */     this.connected = true;
/*     */   }
/*     */ 
/*     */   public boolean isConnected()
/*     */   {
/* 160 */     return this.connected;
/*     */   }
/*     */ 
/*     */   public void setConnected(boolean paramBoolean)
/*     */   {
/* 167 */     this.connected = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void connect()
/*     */     throws IOException
/*     */   {
/* 175 */     if (this.connected)
/* 176 */       return;
/* 177 */     plainConnect();
/* 178 */     if (this.cachedResponse != null)
/*     */     {
/* 180 */       return;
/*     */     }
/* 182 */     if ((!this.http.isCachedConnection()) && (this.http.needsTunneling())) {
/* 183 */       doTunneling();
/*     */     }
/* 185 */     ((HttpsClient)this.http).afterConnect();
/*     */   }
/*     */ 
/*     */   protected HttpClient getNewHttpClient(URL paramURL, Proxy paramProxy, int paramInt)
/*     */     throws IOException
/*     */   {
/* 191 */     return HttpsClient.New(getSSLSocketFactory(), paramURL, getHostnameVerifier(), paramProxy, true, paramInt, this);
/*     */   }
/*     */ 
/*     */   protected HttpClient getNewHttpClient(URL paramURL, Proxy paramProxy, int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 200 */     return HttpsClient.New(getSSLSocketFactory(), paramURL, getHostnameVerifier(), paramProxy, paramBoolean, paramInt, this);
/*     */   }
/*     */ 
/*     */   public String getCipherSuite()
/*     */   {
/* 209 */     if (this.cachedResponse != null) {
/* 210 */       return ((SecureCacheResponse)this.cachedResponse).getCipherSuite();
/*     */     }
/* 212 */     if (this.http == null) {
/* 213 */       throw new IllegalStateException("connection not yet open");
/*     */     }
/* 215 */     return ((HttpsClient)this.http).getCipherSuite();
/*     */   }
/*     */ 
/*     */   public Certificate[] getLocalCertificates()
/*     */   {
/* 224 */     if (this.cachedResponse != null) {
/* 225 */       List localList = ((SecureCacheResponse)this.cachedResponse).getLocalCertificateChain();
/* 226 */       if (localList == null) {
/* 227 */         return null;
/*     */       }
/* 229 */       return (Certificate[])localList.toArray();
/*     */     }
/*     */ 
/* 232 */     if (this.http == null) {
/* 233 */       throw new IllegalStateException("connection not yet open");
/*     */     }
/* 235 */     return ((HttpsClient)this.http).getLocalCertificates();
/*     */   }
/*     */ 
/*     */   public Certificate[] getServerCertificates()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 246 */     if (this.cachedResponse != null) {
/* 247 */       List localList = ((SecureCacheResponse)this.cachedResponse).getServerCertificateChain();
/* 248 */       if (localList == null) {
/* 249 */         return null;
/*     */       }
/* 251 */       return (Certificate[])localList.toArray();
/*     */     }
/*     */ 
/* 255 */     if (this.http == null) {
/* 256 */       throw new IllegalStateException("connection not yet open");
/*     */     }
/* 258 */     return ((HttpsClient)this.http).getServerCertificates();
/*     */   }
/*     */ 
/*     */   public X509Certificate[] getServerCertificateChain()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 268 */     if (this.cachedResponse != null) {
/* 269 */       throw new UnsupportedOperationException("this method is not supported when using cache");
/*     */     }
/* 271 */     if (this.http == null) {
/* 272 */       throw new IllegalStateException("connection not yet open");
/*     */     }
/* 274 */     return ((HttpsClient)this.http).getServerCertificateChain();
/*     */   }
/*     */ 
/*     */   Principal getPeerPrincipal()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 285 */     if (this.cachedResponse != null) {
/* 286 */       return ((SecureCacheResponse)this.cachedResponse).getPeerPrincipal();
/*     */     }
/*     */ 
/* 289 */     if (this.http == null) {
/* 290 */       throw new IllegalStateException("connection not yet open");
/*     */     }
/* 292 */     return ((HttpsClient)this.http).getPeerPrincipal();
/*     */   }
/*     */ 
/*     */   Principal getLocalPrincipal()
/*     */   {
/* 302 */     if (this.cachedResponse != null) {
/* 303 */       return ((SecureCacheResponse)this.cachedResponse).getLocalPrincipal();
/*     */     }
/*     */ 
/* 306 */     if (this.http == null) {
/* 307 */       throw new IllegalStateException("connection not yet open");
/*     */     }
/* 309 */     return ((HttpsClient)this.http).getLocalPrincipal();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection
 * JD-Core Version:    0.6.2
 */