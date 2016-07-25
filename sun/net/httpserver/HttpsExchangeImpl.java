/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import com.sun.net.httpserver.Headers;
/*     */ import com.sun.net.httpserver.HttpPrincipal;
/*     */ import com.sun.net.httpserver.HttpsExchange;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import javax.net.ssl.SSLSession;
/*     */ 
/*     */ class HttpsExchangeImpl extends HttpsExchange
/*     */ {
/*     */   ExchangeImpl impl;
/*     */ 
/*     */   HttpsExchangeImpl(ExchangeImpl paramExchangeImpl)
/*     */     throws IOException
/*     */   {
/*  43 */     this.impl = paramExchangeImpl;
/*     */   }
/*     */ 
/*     */   public Headers getRequestHeaders() {
/*  47 */     return this.impl.getRequestHeaders();
/*     */   }
/*     */ 
/*     */   public Headers getResponseHeaders() {
/*  51 */     return this.impl.getResponseHeaders();
/*     */   }
/*     */ 
/*     */   public URI getRequestURI() {
/*  55 */     return this.impl.getRequestURI();
/*     */   }
/*     */ 
/*     */   public String getRequestMethod() {
/*  59 */     return this.impl.getRequestMethod();
/*     */   }
/*     */ 
/*     */   public HttpContextImpl getHttpContext() {
/*  63 */     return this.impl.getHttpContext();
/*     */   }
/*     */ 
/*     */   public void close() {
/*  67 */     this.impl.close();
/*     */   }
/*     */ 
/*     */   public InputStream getRequestBody() {
/*  71 */     return this.impl.getRequestBody();
/*     */   }
/*     */ 
/*     */   public int getResponseCode() {
/*  75 */     return this.impl.getResponseCode();
/*     */   }
/*     */ 
/*     */   public OutputStream getResponseBody() {
/*  79 */     return this.impl.getResponseBody();
/*     */   }
/*     */ 
/*     */   public void sendResponseHeaders(int paramInt, long paramLong)
/*     */     throws IOException
/*     */   {
/*  86 */     this.impl.sendResponseHeaders(paramInt, paramLong);
/*     */   }
/*     */ 
/*     */   public InetSocketAddress getRemoteAddress() {
/*  90 */     return this.impl.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   public InetSocketAddress getLocalAddress() {
/*  94 */     return this.impl.getLocalAddress();
/*     */   }
/*     */ 
/*     */   public String getProtocol() {
/*  98 */     return this.impl.getProtocol();
/*     */   }
/*     */ 
/*     */   public SSLSession getSSLSession() {
/* 102 */     return this.impl.getSSLSession();
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String paramString) {
/* 106 */     return this.impl.getAttribute(paramString);
/*     */   }
/*     */ 
/*     */   public void setAttribute(String paramString, Object paramObject) {
/* 110 */     this.impl.setAttribute(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void setStreams(InputStream paramInputStream, OutputStream paramOutputStream) {
/* 114 */     this.impl.setStreams(paramInputStream, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public HttpPrincipal getPrincipal() {
/* 118 */     return this.impl.getPrincipal();
/*     */   }
/*     */ 
/*     */   ExchangeImpl getExchangeImpl() {
/* 122 */     return this.impl;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.HttpsExchangeImpl
 * JD-Core Version:    0.6.2
 */