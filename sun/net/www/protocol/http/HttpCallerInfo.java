/*     */ package sun.net.www.protocol.http;
/*     */ 
/*     */ import java.net.Authenticator.RequestorType;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URL;
/*     */ 
/*     */ public final class HttpCallerInfo
/*     */ {
/*     */   public final URL url;
/*     */   public final String host;
/*     */   public final String protocol;
/*     */   public final String prompt;
/*     */   public final String scheme;
/*     */   public final int port;
/*     */   public final InetAddress addr;
/*     */   public final Authenticator.RequestorType authType;
/*     */ 
/*     */   public HttpCallerInfo(HttpCallerInfo paramHttpCallerInfo, String paramString)
/*     */   {
/*  57 */     this.url = paramHttpCallerInfo.url;
/*  58 */     this.host = paramHttpCallerInfo.host;
/*  59 */     this.protocol = paramHttpCallerInfo.protocol;
/*  60 */     this.prompt = paramHttpCallerInfo.prompt;
/*  61 */     this.port = paramHttpCallerInfo.port;
/*  62 */     this.addr = paramHttpCallerInfo.addr;
/*  63 */     this.authType = paramHttpCallerInfo.authType;
/*  64 */     this.scheme = paramString;
/*     */   }
/*     */ 
/*     */   public HttpCallerInfo(URL paramURL)
/*     */   {
/*  71 */     this.url = paramURL;
/*  72 */     this.prompt = "";
/*  73 */     this.host = paramURL.getHost();
/*     */ 
/*  75 */     int i = paramURL.getPort();
/*  76 */     if (i == -1)
/*  77 */       this.port = paramURL.getDefaultPort();
/*     */     else {
/*  79 */       this.port = i;
/*     */     }
/*     */     InetAddress localInetAddress;
/*     */     try
/*     */     {
/*  84 */       localInetAddress = InetAddress.getByName(paramURL.getHost());
/*     */     } catch (Exception localException) {
/*  86 */       localInetAddress = null;
/*     */     }
/*  88 */     this.addr = localInetAddress;
/*     */ 
/*  90 */     this.protocol = paramURL.getProtocol();
/*  91 */     this.authType = Authenticator.RequestorType.SERVER;
/*  92 */     this.scheme = "";
/*     */   }
/*     */ 
/*     */   public HttpCallerInfo(URL paramURL, String paramString, int paramInt)
/*     */   {
/*  99 */     this.url = paramURL;
/* 100 */     this.host = paramString;
/* 101 */     this.port = paramInt;
/* 102 */     this.prompt = "";
/* 103 */     this.addr = null;
/* 104 */     this.protocol = paramURL.getProtocol();
/* 105 */     this.authType = Authenticator.RequestorType.PROXY;
/* 106 */     this.scheme = "";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.HttpCallerInfo
 * JD-Core Version:    0.6.2
 */