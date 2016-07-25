/*     */ package sun.net.www.protocol.https;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.security.Permission;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.security.cert.X509Certificate;
/*     */ 
/*     */ public class HttpsURLConnectionImpl extends HttpsURLConnection
/*     */ {
/*     */   protected DelegateHttpsURLConnection delegate;
/*     */ 
/*     */   HttpsURLConnectionImpl(URL paramURL, Handler paramHandler)
/*     */     throws IOException
/*     */   {
/*  79 */     this(paramURL, null, paramHandler);
/*     */   }
/*     */ 
/*     */   HttpsURLConnectionImpl(URL paramURL, Proxy paramProxy, Handler paramHandler)
/*     */     throws IOException
/*     */   {
/*  85 */     super(paramURL);
/*  86 */     this.delegate = new DelegateHttpsURLConnection(this.url, paramProxy, paramHandler, this);
/*     */   }
/*     */ 
/*     */   protected HttpsURLConnectionImpl(URL paramURL)
/*     */     throws IOException
/*     */   {
/*  93 */     super(paramURL);
/*     */   }
/*     */ 
/*     */   protected void setNewClient(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 103 */     this.delegate.setNewClient(paramURL, false);
/*     */   }
/*     */ 
/*     */   protected void setNewClient(URL paramURL, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 115 */     this.delegate.setNewClient(paramURL, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void setProxiedClient(URL paramURL, String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 129 */     this.delegate.setProxiedClient(paramURL, paramString, paramInt);
/*     */   }
/*     */ 
/*     */   protected void setProxiedClient(URL paramURL, String paramString, int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 145 */     this.delegate.setProxiedClient(paramURL, paramString, paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void connect()
/*     */     throws IOException
/*     */   {
/* 153 */     this.delegate.connect();
/*     */   }
/*     */ 
/*     */   protected boolean isConnected()
/*     */   {
/* 162 */     return this.delegate.isConnected();
/*     */   }
/*     */ 
/*     */   protected void setConnected(boolean paramBoolean)
/*     */   {
/* 171 */     this.delegate.setConnected(paramBoolean);
/*     */   }
/*     */ 
/*     */   public String getCipherSuite()
/*     */   {
/* 178 */     return this.delegate.getCipherSuite();
/*     */   }
/*     */ 
/*     */   public Certificate[] getLocalCertificates()
/*     */   {
/* 187 */     return this.delegate.getLocalCertificates();
/*     */   }
/*     */ 
/*     */   public Certificate[] getServerCertificates()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 197 */     return this.delegate.getServerCertificates();
/*     */   }
/*     */ 
/*     */   public X509Certificate[] getServerCertificateChain()
/*     */   {
/*     */     try
/*     */     {
/* 210 */       return this.delegate.getServerCertificateChain();
/*     */     }
/*     */     catch (SSLPeerUnverifiedException localSSLPeerUnverifiedException)
/*     */     {
/*     */     }
/* 215 */     return null;
/*     */   }
/*     */ 
/*     */   public Principal getPeerPrincipal()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 226 */     return this.delegate.getPeerPrincipal();
/*     */   }
/*     */ 
/*     */   public Principal getLocalPrincipal()
/*     */   {
/* 235 */     return this.delegate.getLocalPrincipal();
/*     */   }
/*     */ 
/*     */   public synchronized OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 250 */     return this.delegate.getOutputStream();
/*     */   }
/*     */ 
/*     */   public synchronized InputStream getInputStream() throws IOException {
/* 254 */     return this.delegate.getInputStream();
/*     */   }
/*     */ 
/*     */   public InputStream getErrorStream() {
/* 258 */     return this.delegate.getErrorStream();
/*     */   }
/*     */ 
/*     */   public void disconnect()
/*     */   {
/* 265 */     this.delegate.disconnect();
/*     */   }
/*     */ 
/*     */   public boolean usingProxy() {
/* 269 */     return this.delegate.usingProxy();
/*     */   }
/*     */ 
/*     */   public Map<String, List<String>> getHeaderFields()
/*     */   {
/* 283 */     return this.delegate.getHeaderFields();
/*     */   }
/*     */ 
/*     */   public String getHeaderField(String paramString)
/*     */   {
/* 291 */     return this.delegate.getHeaderField(paramString);
/*     */   }
/*     */ 
/*     */   public String getHeaderField(int paramInt)
/*     */   {
/* 299 */     return this.delegate.getHeaderField(paramInt);
/*     */   }
/*     */ 
/*     */   public String getHeaderFieldKey(int paramInt)
/*     */   {
/* 307 */     return this.delegate.getHeaderFieldKey(paramInt);
/*     */   }
/*     */ 
/*     */   public void setRequestProperty(String paramString1, String paramString2)
/*     */   {
/* 316 */     this.delegate.setRequestProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void addRequestProperty(String paramString1, String paramString2)
/*     */   {
/* 331 */     this.delegate.addRequestProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public int getResponseCode()
/*     */     throws IOException
/*     */   {
/* 338 */     return this.delegate.getResponseCode();
/*     */   }
/*     */ 
/*     */   public String getRequestProperty(String paramString) {
/* 342 */     return this.delegate.getRequestProperty(paramString);
/*     */   }
/*     */ 
/*     */   public Map<String, List<String>> getRequestProperties()
/*     */   {
/* 358 */     return this.delegate.getRequestProperties();
/*     */   }
/*     */ 
/*     */   public void setInstanceFollowRedirects(boolean paramBoolean)
/*     */   {
/* 366 */     this.delegate.setInstanceFollowRedirects(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getInstanceFollowRedirects() {
/* 370 */     return this.delegate.getInstanceFollowRedirects();
/*     */   }
/*     */ 
/*     */   public void setRequestMethod(String paramString) throws ProtocolException {
/* 374 */     this.delegate.setRequestMethod(paramString);
/*     */   }
/*     */ 
/*     */   public String getRequestMethod() {
/* 378 */     return this.delegate.getRequestMethod();
/*     */   }
/*     */ 
/*     */   public String getResponseMessage() throws IOException {
/* 382 */     return this.delegate.getResponseMessage();
/*     */   }
/*     */ 
/*     */   public long getHeaderFieldDate(String paramString, long paramLong) {
/* 386 */     return this.delegate.getHeaderFieldDate(paramString, paramLong);
/*     */   }
/*     */ 
/*     */   public Permission getPermission() throws IOException {
/* 390 */     return this.delegate.getPermission();
/*     */   }
/*     */ 
/*     */   public URL getURL() {
/* 394 */     return this.delegate.getURL();
/*     */   }
/*     */ 
/*     */   public int getContentLength() {
/* 398 */     return this.delegate.getContentLength();
/*     */   }
/*     */ 
/*     */   public long getContentLengthLong() {
/* 402 */     return this.delegate.getContentLengthLong();
/*     */   }
/*     */ 
/*     */   public String getContentType() {
/* 406 */     return this.delegate.getContentType();
/*     */   }
/*     */ 
/*     */   public String getContentEncoding() {
/* 410 */     return this.delegate.getContentEncoding();
/*     */   }
/*     */ 
/*     */   public long getExpiration() {
/* 414 */     return this.delegate.getExpiration();
/*     */   }
/*     */ 
/*     */   public long getDate() {
/* 418 */     return this.delegate.getDate();
/*     */   }
/*     */ 
/*     */   public long getLastModified() {
/* 422 */     return this.delegate.getLastModified();
/*     */   }
/*     */ 
/*     */   public int getHeaderFieldInt(String paramString, int paramInt) {
/* 426 */     return this.delegate.getHeaderFieldInt(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public long getHeaderFieldLong(String paramString, long paramLong) {
/* 430 */     return this.delegate.getHeaderFieldLong(paramString, paramLong);
/*     */   }
/*     */ 
/*     */   public Object getContent() throws IOException {
/* 434 */     return this.delegate.getContent();
/*     */   }
/*     */ 
/*     */   public Object getContent(Class[] paramArrayOfClass) throws IOException {
/* 438 */     return this.delegate.getContent(paramArrayOfClass);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 442 */     return this.delegate.toString();
/*     */   }
/*     */ 
/*     */   public void setDoInput(boolean paramBoolean) {
/* 446 */     this.delegate.setDoInput(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getDoInput() {
/* 450 */     return this.delegate.getDoInput();
/*     */   }
/*     */ 
/*     */   public void setDoOutput(boolean paramBoolean) {
/* 454 */     this.delegate.setDoOutput(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getDoOutput() {
/* 458 */     return this.delegate.getDoOutput();
/*     */   }
/*     */ 
/*     */   public void setAllowUserInteraction(boolean paramBoolean) {
/* 462 */     this.delegate.setAllowUserInteraction(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getAllowUserInteraction() {
/* 466 */     return this.delegate.getAllowUserInteraction();
/*     */   }
/*     */ 
/*     */   public void setUseCaches(boolean paramBoolean) {
/* 470 */     this.delegate.setUseCaches(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getUseCaches() {
/* 474 */     return this.delegate.getUseCaches();
/*     */   }
/*     */ 
/*     */   public void setIfModifiedSince(long paramLong) {
/* 478 */     this.delegate.setIfModifiedSince(paramLong);
/*     */   }
/*     */ 
/*     */   public long getIfModifiedSince() {
/* 482 */     return this.delegate.getIfModifiedSince();
/*     */   }
/*     */ 
/*     */   public boolean getDefaultUseCaches() {
/* 486 */     return this.delegate.getDefaultUseCaches();
/*     */   }
/*     */ 
/*     */   public void setDefaultUseCaches(boolean paramBoolean) {
/* 490 */     this.delegate.setDefaultUseCaches(paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/* 499 */     this.delegate.dispose();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 503 */     return this.delegate.equals(paramObject);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 507 */     return this.delegate.hashCode();
/*     */   }
/*     */ 
/*     */   public void setConnectTimeout(int paramInt) {
/* 511 */     this.delegate.setConnectTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   public int getConnectTimeout() {
/* 515 */     return this.delegate.getConnectTimeout();
/*     */   }
/*     */ 
/*     */   public void setReadTimeout(int paramInt) {
/* 519 */     this.delegate.setReadTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   public int getReadTimeout() {
/* 523 */     return this.delegate.getReadTimeout();
/*     */   }
/*     */ 
/*     */   public void setFixedLengthStreamingMode(int paramInt) {
/* 527 */     this.delegate.setFixedLengthStreamingMode(paramInt);
/*     */   }
/*     */ 
/*     */   public void setFixedLengthStreamingMode(long paramLong) {
/* 531 */     this.delegate.setFixedLengthStreamingMode(paramLong);
/*     */   }
/*     */ 
/*     */   public void setChunkedStreamingMode(int paramInt) {
/* 535 */     this.delegate.setChunkedStreamingMode(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.https.HttpsURLConnectionImpl
 * JD-Core Version:    0.6.2
 */