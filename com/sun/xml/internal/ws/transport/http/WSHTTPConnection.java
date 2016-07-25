/*     */ package com.sun.xml.internal.ws.transport.http;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.PropertySet;
/*     */ import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class WSHTTPConnection extends PropertySet
/*     */ {
/*     */   public static final int OK = 200;
/*     */   public static final int ONEWAY = 202;
/*     */   public static final int UNSUPPORTED_MEDIA = 415;
/*     */   public static final int MALFORMED_XML = 400;
/*     */   public static final int INTERNAL_ERR = 500;
/*     */   private volatile boolean closed;
/*     */ 
/*     */   public abstract void setResponseHeaders(@NotNull Map<String, List<String>> paramMap);
/*     */ 
/*     */   public abstract void setContentTypeResponseHeader(@NotNull String paramString);
/*     */ 
/*     */   public abstract void setStatus(int paramInt);
/*     */ 
/*     */   public abstract int getStatus();
/*     */ 
/*     */   @NotNull
/*     */   public abstract InputStream getInput()
/*     */     throws IOException;
/*     */ 
/*     */   @NotNull
/*     */   public abstract OutputStream getOutput()
/*     */     throws IOException;
/*     */ 
/*     */   @NotNull
/*     */   public abstract WebServiceContextDelegate getWebServiceContextDelegate();
/*     */ 
/*     */   @NotNull
/*     */   public abstract String getRequestMethod();
/*     */ 
/*     */   /** @deprecated */
/*     */   @NotNull
/*     */   public abstract Map<String, List<String>> getRequestHeaders();
/*     */ 
/*     */   public abstract Map<String, List<String>> getResponseHeaders();
/*     */ 
/*     */   @Nullable
/*     */   public abstract String getRequestHeader(@NotNull String paramString);
/*     */ 
/*     */   @Nullable
/*     */   public abstract String getQueryString();
/*     */ 
/*     */   @Nullable
/*     */   public abstract String getPathInfo();
/*     */ 
/*     */   @NotNull
/*     */   public String getBaseAddress()
/*     */   {
/* 224 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract boolean isSecure();
/*     */ 
/*     */   public void close()
/*     */   {
/* 241 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 248 */     return this.closed;
/*     */   }
/*     */ 
/*     */   public String getProtocol()
/*     */   {
/* 257 */     return "HTTP/1.1";
/*     */   }
/*     */ 
/*     */   public String getCookie(String name)
/*     */   {
/* 267 */     return null;
/*     */   }
/*     */ 
/*     */   public void setCookie(String name, String value)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setContentLengthResponseHeader(int value)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.WSHTTPConnection
 * JD-Core Version:    0.6.2
 */