/*     */ package sun.net.www.protocol.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Authenticator.RequestorType;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.misc.BASE64Encoder;
/*     */ import sun.net.www.HeaderParser;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ import sun.util.logging.PlatformLogger.Level;
/*     */ 
/*     */ class NegotiateAuthentication extends AuthenticationInfo
/*     */ {
/*     */   private static final long serialVersionUID = 100L;
/*  49 */   private static final PlatformLogger logger = HttpURLConnection.getHttpLogger();
/*     */   private final HttpCallerInfo hci;
/*  58 */   static HashMap<String, Boolean> supported = null;
/*  59 */   static HashMap<String, Negotiator> cache = null;
/*     */ 
/*  62 */   private Negotiator negotiator = null;
/*     */ 
/*     */   public NegotiateAuthentication(HttpCallerInfo paramHttpCallerInfo)
/*     */   {
/*  69 */     super(Authenticator.RequestorType.PROXY == paramHttpCallerInfo.authType ? 'p' : 's', paramHttpCallerInfo.scheme.equalsIgnoreCase("Negotiate") ? AuthScheme.NEGOTIATE : AuthScheme.KERBEROS, paramHttpCallerInfo.url, "");
/*     */ 
/*  73 */     this.hci = paramHttpCallerInfo;
/*     */   }
/*     */ 
/*     */   public boolean supportsPreemptiveAuthorization()
/*     */   {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isSupported(HttpCallerInfo paramHttpCallerInfo)
/*     */   {
/*  89 */     ClassLoader localClassLoader = null;
/*     */     try {
/*  91 */       localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     } catch (SecurityException localSecurityException) {
/*  93 */       if (logger.isLoggable(PlatformLogger.Level.FINER)) {
/*  94 */         logger.finer("NegotiateAuthentication: Attempt to get the context class loader failed - " + localSecurityException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  99 */     if (localClassLoader != null)
/*     */     {
/* 102 */       synchronized (localClassLoader) {
/* 103 */         return isSupportedImpl(paramHttpCallerInfo);
/*     */       }
/*     */     }
/* 106 */     return isSupportedImpl(paramHttpCallerInfo);
/*     */   }
/*     */ 
/*     */   private static synchronized boolean isSupportedImpl(HttpCallerInfo paramHttpCallerInfo)
/*     */   {
/* 121 */     if (supported == null) {
/* 122 */       supported = new HashMap();
/* 123 */       cache = new HashMap();
/*     */     }
/* 125 */     String str = paramHttpCallerInfo.host;
/* 126 */     str = str.toLowerCase();
/* 127 */     if (supported.containsKey(str)) {
/* 128 */       return ((Boolean)supported.get(str)).booleanValue();
/*     */     }
/*     */ 
/* 131 */     Negotiator localNegotiator = Negotiator.getNegotiator(paramHttpCallerInfo);
/* 132 */     if (localNegotiator != null) {
/* 133 */       supported.put(str, Boolean.valueOf(true));
/*     */ 
/* 136 */       cache.put(str, localNegotiator);
/* 137 */       return true;
/*     */     }
/* 139 */     supported.put(str, Boolean.valueOf(false));
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   public String getHeaderValue(URL paramURL, String paramString)
/*     */   {
/* 149 */     throw new RuntimeException("getHeaderValue not supported");
/*     */   }
/*     */ 
/*     */   public boolean isAuthorizationStale(String paramString)
/*     */   {
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized boolean setHeaders(HttpURLConnection paramHttpURLConnection, HeaderParser paramHeaderParser, String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 178 */       byte[] arrayOfByte = null;
/* 179 */       String[] arrayOfString = paramString.split("\\s+");
/* 180 */       if (arrayOfString.length > 1) {
/* 181 */         arrayOfByte = new BASE64Decoder().decodeBuffer(arrayOfString[1]);
/*     */       }
/* 183 */       String str = this.hci.scheme + " " + new B64Encoder().encode(arrayOfByte == null ? firstToken() : nextToken(arrayOfByte));
/*     */ 
/* 186 */       paramHttpURLConnection.setAuthenticationProperty(getHeaderName(), str);
/* 187 */       return true; } catch (IOException localIOException) {
/*     */     }
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */   private byte[] firstToken()
/*     */     throws IOException
/*     */   {
/* 200 */     this.negotiator = null;
/* 201 */     if (cache != null) {
/* 202 */       synchronized (cache) {
/* 203 */         this.negotiator = ((Negotiator)cache.get(getHost()));
/* 204 */         if (this.negotiator != null) {
/* 205 */           cache.remove(getHost());
/*     */         }
/*     */       }
/*     */     }
/* 209 */     if (this.negotiator == null) {
/* 210 */       this.negotiator = Negotiator.getNegotiator(this.hci);
/* 211 */       if (this.negotiator == null) {
/* 212 */         ??? = new IOException("Cannot initialize Negotiator");
/* 213 */         throw ((Throwable)???);
/*     */       }
/*     */     }
/*     */ 
/* 217 */     return this.negotiator.firstToken();
/*     */   }
/*     */ 
/*     */   private byte[] nextToken(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 228 */     return this.negotiator.nextToken(paramArrayOfByte);
/*     */   }
/*     */   class B64Encoder extends BASE64Encoder {
/*     */     B64Encoder() {
/*     */     }
/* 233 */     protected int bytesPerLine() { return 100000; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.NegotiateAuthentication
 * JD-Core Version:    0.6.2
 */