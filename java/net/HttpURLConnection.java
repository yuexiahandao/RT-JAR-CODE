/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.Permission;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class HttpURLConnection extends URLConnection
/*     */ {
/*  63 */   protected String method = "GET";
/*     */ 
/*  70 */   protected int chunkLength = -1;
/*     */ 
/*  82 */   protected int fixedContentLength = -1;
/*     */ 
/*  91 */   protected long fixedContentLengthLong = -1L;
/*     */   private static final int DEFAULT_CHUNK_SIZE = 4096;
/* 270 */   protected int responseCode = -1;
/*     */ 
/* 275 */   protected String responseMessage = null;
/*     */ 
/* 280 */   private static boolean followRedirects = true;
/*     */ 
/* 298 */   protected boolean instanceFollowRedirects = followRedirects;
/*     */ 
/* 301 */   private static final String[] methods = { "GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE" };
/*     */   public static final int HTTP_OK = 200;
/*     */   public static final int HTTP_CREATED = 201;
/*     */   public static final int HTTP_ACCEPTED = 202;
/*     */   public static final int HTTP_NOT_AUTHORITATIVE = 203;
/*     */   public static final int HTTP_NO_CONTENT = 204;
/*     */   public static final int HTTP_RESET = 205;
/*     */   public static final int HTTP_PARTIAL = 206;
/*     */   public static final int HTTP_MULT_CHOICE = 300;
/*     */   public static final int HTTP_MOVED_PERM = 301;
/*     */   public static final int HTTP_MOVED_TEMP = 302;
/*     */   public static final int HTTP_SEE_OTHER = 303;
/*     */   public static final int HTTP_NOT_MODIFIED = 304;
/*     */   public static final int HTTP_USE_PROXY = 305;
/*     */   public static final int HTTP_BAD_REQUEST = 400;
/*     */   public static final int HTTP_UNAUTHORIZED = 401;
/*     */   public static final int HTTP_PAYMENT_REQUIRED = 402;
/*     */   public static final int HTTP_FORBIDDEN = 403;
/*     */   public static final int HTTP_NOT_FOUND = 404;
/*     */   public static final int HTTP_BAD_METHOD = 405;
/*     */   public static final int HTTP_NOT_ACCEPTABLE = 406;
/*     */   public static final int HTTP_PROXY_AUTH = 407;
/*     */   public static final int HTTP_CLIENT_TIMEOUT = 408;
/*     */   public static final int HTTP_CONFLICT = 409;
/*     */   public static final int HTTP_GONE = 410;
/*     */   public static final int HTTP_LENGTH_REQUIRED = 411;
/*     */   public static final int HTTP_PRECON_FAILED = 412;
/*     */   public static final int HTTP_ENTITY_TOO_LARGE = 413;
/*     */   public static final int HTTP_REQ_TOO_LONG = 414;
/*     */   public static final int HTTP_UNSUPPORTED_TYPE = 415;
/*     */ 
/*     */   @Deprecated
/*     */   public static final int HTTP_SERVER_ERROR = 500;
/*     */   public static final int HTTP_INTERNAL_ERROR = 500;
/*     */   public static final int HTTP_NOT_IMPLEMENTED = 501;
/*     */   public static final int HTTP_BAD_GATEWAY = 502;
/*     */   public static final int HTTP_UNAVAILABLE = 503;
/*     */   public static final int HTTP_GATEWAY_TIMEOUT = 504;
/*     */   public static final int HTTP_VERSION = 505;
/*     */ 
/*     */   public String getHeaderFieldKey(int paramInt)
/*     */   {
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   public void setFixedLengthStreamingMode(int paramInt)
/*     */   {
/* 142 */     if (this.connected) {
/* 143 */       throw new IllegalStateException("Already connected");
/*     */     }
/* 145 */     if (this.chunkLength != -1) {
/* 146 */       throw new IllegalStateException("Chunked encoding streaming mode set");
/*     */     }
/* 148 */     if (paramInt < 0) {
/* 149 */       throw new IllegalArgumentException("invalid content length");
/*     */     }
/* 151 */     this.fixedContentLength = paramInt;
/*     */   }
/*     */ 
/*     */   public void setFixedLengthStreamingMode(long paramLong)
/*     */   {
/* 187 */     if (this.connected) {
/* 188 */       throw new IllegalStateException("Already connected");
/*     */     }
/* 190 */     if (this.chunkLength != -1) {
/* 191 */       throw new IllegalStateException("Chunked encoding streaming mode set");
/*     */     }
/*     */ 
/* 194 */     if (paramLong < 0L) {
/* 195 */       throw new IllegalArgumentException("invalid content length");
/*     */     }
/* 197 */     this.fixedContentLengthLong = paramLong;
/*     */   }
/*     */ 
/*     */   public void setChunkedStreamingMode(int paramInt)
/*     */   {
/* 232 */     if (this.connected) {
/* 233 */       throw new IllegalStateException("Can't set streaming mode: already connected");
/*     */     }
/* 235 */     if ((this.fixedContentLength != -1) || (this.fixedContentLengthLong != -1L)) {
/* 236 */       throw new IllegalStateException("Fixed length streaming mode set");
/*     */     }
/* 238 */     this.chunkLength = (paramInt <= 0 ? 4096 : paramInt);
/*     */   }
/*     */ 
/*     */   public String getHeaderField(int paramInt)
/*     */   {
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   protected HttpURLConnection(URL paramURL)
/*     */   {
/* 310 */     super(paramURL);
/*     */   }
/*     */ 
/*     */   public static void setFollowRedirects(boolean paramBoolean)
/*     */   {
/* 332 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 333 */     if (localSecurityManager != null)
/*     */     {
/* 335 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 337 */     followRedirects = paramBoolean;
/*     */   }
/*     */ 
/*     */   public static boolean getFollowRedirects()
/*     */   {
/* 350 */     return followRedirects;
/*     */   }
/*     */ 
/*     */   public void setInstanceFollowRedirects(boolean paramBoolean)
/*     */   {
/* 369 */     this.instanceFollowRedirects = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getInstanceFollowRedirects()
/*     */   {
/* 383 */     return this.instanceFollowRedirects;
/*     */   }
/*     */ 
/*     */   public void setRequestMethod(String paramString)
/*     */     throws ProtocolException
/*     */   {
/* 408 */     if (this.connected) {
/* 409 */       throw new ProtocolException("Can't reset method: already connected");
/*     */     }
/*     */ 
/* 416 */     for (int i = 0; i < methods.length; i++) {
/* 417 */       if (methods[i].equals(paramString)) {
/* 418 */         if (paramString.equals("TRACE")) {
/* 419 */           SecurityManager localSecurityManager = System.getSecurityManager();
/* 420 */           if (localSecurityManager != null) {
/* 421 */             localSecurityManager.checkPermission(new NetPermission("allowHttpTrace"));
/*     */           }
/*     */         }
/* 424 */         this.method = paramString;
/* 425 */         return;
/*     */       }
/*     */     }
/* 428 */     throw new ProtocolException("Invalid HTTP method: " + paramString);
/*     */   }
/*     */ 
/*     */   public String getRequestMethod()
/*     */   {
/* 437 */     return this.method;
/*     */   }
/*     */ 
/*     */   public int getResponseCode()
/*     */     throws IOException
/*     */   {
/* 457 */     if (this.responseCode != -1) {
/* 458 */       return this.responseCode;
/*     */     }
/*     */ 
/* 466 */     Object localObject = null;
/*     */     try {
/* 468 */       getInputStream();
/*     */     } catch (Exception localException) {
/* 470 */       localObject = localException;
/*     */     }
/*     */ 
/* 477 */     String str = getHeaderField(0);
/* 478 */     if (str == null) {
/* 479 */       if (localObject != null) {
/* 480 */         if ((localObject instanceof RuntimeException)) {
/* 481 */           throw ((RuntimeException)localObject);
/*     */         }
/* 483 */         throw ((IOException)localObject);
/*     */       }
/* 485 */       return -1;
/*     */     }
/*     */ 
/* 496 */     if (str.startsWith("HTTP/1.")) {
/* 497 */       int i = str.indexOf(' ');
/* 498 */       if (i > 0)
/*     */       {
/* 500 */         int j = str.indexOf(' ', i + 1);
/* 501 */         if ((j > 0) && (j < str.length())) {
/* 502 */           this.responseMessage = str.substring(j + 1);
/*     */         }
/*     */ 
/* 507 */         if (j < 0)
/* 508 */           j = str.length();
/*     */         try
/*     */         {
/* 511 */           this.responseCode = Integer.parseInt(str.substring(i + 1, j));
/*     */ 
/* 513 */           return this.responseCode; } catch (NumberFormatException localNumberFormatException) {
/*     */         }
/*     */       }
/*     */     }
/* 517 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getResponseMessage()
/*     */     throws IOException
/*     */   {
/* 534 */     getResponseCode();
/* 535 */     return this.responseMessage;
/*     */   }
/*     */ 
/*     */   public long getHeaderFieldDate(String paramString, long paramLong) {
/* 539 */     String str = getHeaderField(paramString);
/*     */     try {
/* 541 */       if (str.indexOf("GMT") == -1) {
/* 542 */         str = str + " GMT";
/*     */       }
/* 544 */       return Date.parse(str);
/*     */     } catch (Exception localException) {
/*     */     }
/* 547 */     return paramLong;
/*     */   }
/*     */ 
/*     */   public abstract void disconnect();
/*     */ 
/*     */   public abstract boolean usingProxy();
/*     */ 
/*     */   public Permission getPermission()
/*     */     throws IOException
/*     */   {
/* 578 */     int i = this.url.getPort();
/* 579 */     i = i < 0 ? 80 : i;
/* 580 */     String str = this.url.getHost() + ":" + i;
/* 581 */     SocketPermission localSocketPermission = new SocketPermission(str, "connect");
/* 582 */     return localSocketPermission;
/*     */   }
/*     */ 
/*     */   public InputStream getErrorStream()
/*     */   {
/* 604 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.HttpURLConnection
 * JD-Core Version:    0.6.2
 */