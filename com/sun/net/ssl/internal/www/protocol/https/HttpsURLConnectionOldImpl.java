/*     */ package com.sun.net.ssl.internal.www.protocol.https;
/*     */ 
/*     */ import com.sun.net.ssl.HttpsURLConnection;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.security.Permission;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.security.cert.X509Certificate;
/*     */ 
/*     */ public class HttpsURLConnectionOldImpl extends HttpsURLConnection
/*     */ {
/*     */   private DelegateHttpsURLConnection delegate;
/*     */ 
/*     */   HttpsURLConnectionOldImpl(URL paramURL, Handler paramHandler)
/*     */     throws IOException
/*     */   {
/*  75 */     this(paramURL, null, paramHandler);
/*     */   }
/*     */ 
/*     */   HttpsURLConnectionOldImpl(URL paramURL, Proxy paramProxy, Handler paramHandler)
/*     */     throws IOException
/*     */   {
/*  81 */     super(paramURL);
/*  82 */     this.delegate = new DelegateHttpsURLConnection(this.url, paramProxy, paramHandler, this);
/*     */   }
/*     */ 
/*     */   protected void setNewClient(URL paramURL)
/*     */     throws IOException
/*     */   {
/*  92 */     this.delegate.setNewClient(paramURL, false);
/*     */   }
/*     */ 
/*     */   protected void setNewClient(URL paramURL, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 104 */     this.delegate.setNewClient(paramURL, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void setProxiedClient(URL paramURL, String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 118 */     this.delegate.setProxiedClient(paramURL, paramString, paramInt);
/*     */   }
/*     */ 
/*     */   protected void setProxiedClient(URL paramURL, String paramString, int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 134 */     this.delegate.setProxiedClient(paramURL, paramString, paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void connect()
/*     */     throws IOException
/*     */   {
/* 142 */     this.delegate.connect();
/*     */   }
/*     */ 
/*     */   protected boolean isConnected()
/*     */   {
/* 151 */     return this.delegate.isConnected();
/*     */   }
/*     */ 
/*     */   protected void setConnected(boolean paramBoolean)
/*     */   {
/* 160 */     this.delegate.setConnected(paramBoolean);
/*     */   }
/*     */ 
/*     */   public String getCipherSuite()
/*     */   {
/* 167 */     return this.delegate.getCipherSuite();
/*     */   }
/*     */ 
/*     */   public Certificate[] getLocalCertificates()
/*     */   {
/* 176 */     return this.delegate.getLocalCertificates();
/*     */   }
/*     */ 
/*     */   public Certificate[] getServerCertificates()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 186 */     return this.delegate.getServerCertificates();
/*     */   }
/*     */ 
/*     */   public X509Certificate[] getServerCertificateChain()
/*     */   {
/*     */     try
/*     */     {
/* 199 */       return this.delegate.getServerCertificateChain();
/*     */     }
/*     */     catch (SSLPeerUnverifiedException localSSLPeerUnverifiedException)
/*     */     {
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 220 */     return this.delegate.getOutputStream();
/*     */   }
/*     */ 
/*     */   public synchronized InputStream getInputStream() throws IOException {
/* 224 */     return this.delegate.getInputStream();
/*     */   }
/*     */ 
/*     */   public InputStream getErrorStream() {
/* 228 */     return this.delegate.getErrorStream();
/*     */   }
/*     */ 
/*     */   public void disconnect()
/*     */   {
/* 235 */     this.delegate.disconnect();
/*     */   }
/*     */ 
/*     */   public boolean usingProxy() {
/* 239 */     return this.delegate.usingProxy();
/*     */   }
/*     */ 
/*     */   public Map<String, List<String>> getHeaderFields()
/*     */   {
/* 253 */     return this.delegate.getHeaderFields();
/*     */   }
/*     */ 
/*     */   public String getHeaderField(String paramString)
/*     */   {
/* 261 */     return this.delegate.getHeaderField(paramString);
/*     */   }
/*     */ 
/*     */   public String getHeaderField(int paramInt)
/*     */   {
/* 269 */     return this.delegate.getHeaderField(paramInt);
/*     */   }
/*     */ 
/*     */   public String getHeaderFieldKey(int paramInt)
/*     */   {
/* 277 */     return this.delegate.getHeaderFieldKey(paramInt);
/*     */   }
/*     */ 
/*     */   public void setRequestProperty(String paramString1, String paramString2)
/*     */   {
/* 286 */     this.delegate.setRequestProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void addRequestProperty(String paramString1, String paramString2)
/*     */   {
/* 301 */     this.delegate.addRequestProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public int getResponseCode()
/*     */     throws IOException
/*     */   {
/* 308 */     return this.delegate.getResponseCode();
/*     */   }
/*     */ 
/*     */   public String getRequestProperty(String paramString) {
/* 312 */     return this.delegate.getRequestProperty(paramString);
/*     */   }
/*     */ 
/*     */   public Map<String, List<String>> getRequestProperties()
/*     */   {
/* 328 */     return this.delegate.getRequestProperties();
/*     */   }
/*     */ 
/*     */   public void setInstanceFollowRedirects(boolean paramBoolean)
/*     */   {
/* 336 */     this.delegate.setInstanceFollowRedirects(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getInstanceFollowRedirects() {
/* 340 */     return this.delegate.getInstanceFollowRedirects();
/*     */   }
/*     */ 
/*     */   public void setRequestMethod(String paramString) throws ProtocolException {
/* 344 */     this.delegate.setRequestMethod(paramString);
/*     */   }
/*     */ 
/*     */   public String getRequestMethod() {
/* 348 */     return this.delegate.getRequestMethod();
/*     */   }
/*     */ 
/*     */   public String getResponseMessage() throws IOException {
/* 352 */     return this.delegate.getResponseMessage();
/*     */   }
/*     */ 
/*     */   public long getHeaderFieldDate(String paramString, long paramLong) {
/* 356 */     return this.delegate.getHeaderFieldDate(paramString, paramLong);
/*     */   }
/*     */ 
/*     */   public Permission getPermission() throws IOException {
/* 360 */     return this.delegate.getPermission();
/*     */   }
/*     */ 
/*     */   public URL getURL() {
/* 364 */     return this.delegate.getURL();
/*     */   }
/*     */ 
/*     */   public int getContentLength() {
/* 368 */     return this.delegate.getContentLength();
/*     */   }
/*     */ 
/*     */   public long getContentLengthLong() {
/* 372 */     return this.delegate.getContentLengthLong();
/*     */   }
/*     */ 
/*     */   public String getContentType() {
/* 376 */     return this.delegate.getContentType();
/*     */   }
/*     */ 
/*     */   public String getContentEncoding() {
/* 380 */     return this.delegate.getContentEncoding();
/*     */   }
/*     */ 
/*     */   public long getExpiration() {
/* 384 */     return this.delegate.getExpiration();
/*     */   }
/*     */ 
/*     */   public long getDate() {
/* 388 */     return this.delegate.getDate();
/*     */   }
/*     */ 
/*     */   public long getLastModified() {
/* 392 */     return this.delegate.getLastModified();
/*     */   }
/*     */ 
/*     */   public int getHeaderFieldInt(String paramString, int paramInt) {
/* 396 */     return this.delegate.getHeaderFieldInt(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public long getHeaderFieldLong(String paramString, long paramLong) {
/* 400 */     return this.delegate.getHeaderFieldLong(paramString, paramLong);
/*     */   }
/*     */ 
/*     */   public Object getContent() throws IOException {
/* 404 */     return this.delegate.getContent();
/*     */   }
/*     */ 
/*     */   public Object getContent(Class[] paramArrayOfClass) throws IOException {
/* 408 */     return this.delegate.getContent(paramArrayOfClass);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 412 */     return this.delegate.toString();
/*     */   }
/*     */ 
/*     */   public void setDoInput(boolean paramBoolean) {
/* 416 */     this.delegate.setDoInput(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getDoInput() {
/* 420 */     return this.delegate.getDoInput();
/*     */   }
/*     */ 
/*     */   public void setDoOutput(boolean paramBoolean) {
/* 424 */     this.delegate.setDoOutput(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getDoOutput() {
/* 428 */     return this.delegate.getDoOutput();
/*     */   }
/*     */ 
/*     */   public void setAllowUserInteraction(boolean paramBoolean) {
/* 432 */     this.delegate.setAllowUserInteraction(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getAllowUserInteraction() {
/* 436 */     return this.delegate.getAllowUserInteraction();
/*     */   }
/*     */ 
/*     */   public void setUseCaches(boolean paramBoolean) {
/* 440 */     this.delegate.setUseCaches(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getUseCaches() {
/* 444 */     return this.delegate.getUseCaches();
/*     */   }
/*     */ 
/*     */   public void setIfModifiedSince(long paramLong) {
/* 448 */     this.delegate.setIfModifiedSince(paramLong);
/*     */   }
/*     */ 
/*     */   public long getIfModifiedSince() {
/* 452 */     return this.delegate.getIfModifiedSince();
/*     */   }
/*     */ 
/*     */   public boolean getDefaultUseCaches() {
/* 456 */     return this.delegate.getDefaultUseCaches();
/*     */   }
/*     */ 
/*     */   public void setDefaultUseCaches(boolean paramBoolean) {
/* 460 */     this.delegate.setDefaultUseCaches(paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/* 469 */     this.delegate.dispose();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 473 */     return this.delegate.equals(paramObject);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 477 */     return this.delegate.hashCode();
/*     */   }
/*     */ 
/*     */   public void setConnectTimeout(int paramInt) {
/* 481 */     this.delegate.setConnectTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   public int getConnectTimeout() {
/* 485 */     return this.delegate.getConnectTimeout();
/*     */   }
/*     */ 
/*     */   public void setReadTimeout(int paramInt) {
/* 489 */     this.delegate.setReadTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   public int getReadTimeout() {
/* 493 */     return this.delegate.getReadTimeout();
/*     */   }
/*     */ 
/*     */   public void setFixedLengthStreamingMode(int paramInt) {
/* 497 */     this.delegate.setFixedLengthStreamingMode(paramInt);
/*     */   }
/*     */ 
/*     */   public void setFixedLengthStreamingMode(long paramLong) {
/* 501 */     this.delegate.setFixedLengthStreamingMode(paramLong);
/*     */   }
/*     */ 
/*     */   public void setChunkedStreamingMode(int paramInt) {
/* 505 */     this.delegate.setChunkedStreamingMode(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl
 * JD-Core Version:    0.6.2
 */