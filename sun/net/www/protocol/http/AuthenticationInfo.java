/*     */ package sun.net.www.protocol.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.util.HashMap;
/*     */ import sun.net.www.HeaderParser;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ 
/*     */ public abstract class AuthenticationInfo extends AuthCacheValue
/*     */   implements Cloneable
/*     */ {
/*     */   public static final char SERVER_AUTHENTICATION = 's';
/*     */   public static final char PROXY_AUTHENTICATION = 'p';
/*  68 */   static boolean serializeAuth = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("http.auth.serializeRequests"))).booleanValue();
/*     */   protected transient PasswordAuthentication pw;
/* 114 */   private static HashMap<String, Thread> requests = new HashMap();
/*     */   char type;
/*     */   AuthScheme authScheme;
/*     */   String protocol;
/*     */   String host;
/*     */   int port;
/*     */   String realm;
/*     */   String path;
/*     */   String s1;
/*     */   String s2;
/*     */ 
/*     */   public PasswordAuthentication credentials()
/*     */   {
/*  78 */     return this.pw;
/*     */   }
/*     */ 
/*     */   public AuthCacheValue.Type getAuthType() {
/*  82 */     return this.type == 's' ? AuthCacheValue.Type.Server : AuthCacheValue.Type.Proxy;
/*     */   }
/*     */ 
/*     */   AuthScheme getAuthScheme()
/*     */   {
/*  88 */     return this.authScheme;
/*     */   }
/*     */ 
/*     */   public String getHost() {
/*  92 */     return this.host;
/*     */   }
/*     */   public int getPort() {
/*  95 */     return this.port;
/*     */   }
/*     */   public String getRealm() {
/*  98 */     return this.realm;
/*     */   }
/*     */   public String getPath() {
/* 101 */     return this.path;
/*     */   }
/*     */   public String getProtocolScheme() {
/* 104 */     return this.protocol;
/*     */   }
/*     */ 
/*     */   private static boolean requestIsInProgress(String paramString)
/*     */   {
/* 121 */     if (!serializeAuth)
/*     */     {
/* 123 */       return false;
/*     */     }
/* 125 */     synchronized (requests)
/*     */     {
/* 127 */       Thread localThread2 = Thread.currentThread();
/*     */       Thread localThread1;
/* 128 */       if ((localThread1 = (Thread)requests.get(paramString)) == null) {
/* 129 */         requests.put(paramString, localThread2);
/* 130 */         return false;
/*     */       }
/* 132 */       if (localThread1 == localThread2) {
/* 133 */         return false;
/*     */       }
/* 135 */       while (requests.containsKey(paramString))
/*     */         try {
/* 137 */           requests.wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/*     */         }
/*     */     }
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */   private static void requestCompleted(String paramString)
/*     */   {
/* 149 */     synchronized (requests) {
/* 150 */       Thread localThread = (Thread)requests.get(paramString);
/* 151 */       if ((localThread != null) && (localThread == Thread.currentThread())) {
/* 152 */         int i = requests.remove(paramString) != null ? 1 : 0;
/* 153 */         assert (i != 0);
/*     */       }
/* 155 */       requests.notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   public AuthenticationInfo(char paramChar, AuthScheme paramAuthScheme, String paramString1, int paramInt, String paramString2)
/*     */   {
/* 193 */     this.type = paramChar;
/* 194 */     this.authScheme = paramAuthScheme;
/* 195 */     this.protocol = "";
/* 196 */     this.host = paramString1.toLowerCase();
/* 197 */     this.port = paramInt;
/* 198 */     this.realm = paramString2;
/* 199 */     this.path = null;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*     */     try {
/* 204 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 207 */     return null;
/*     */   }
/*     */ 
/*     */   public AuthenticationInfo(char paramChar, AuthScheme paramAuthScheme, URL paramURL, String paramString)
/*     */   {
/* 216 */     this.type = paramChar;
/* 217 */     this.authScheme = paramAuthScheme;
/* 218 */     this.protocol = paramURL.getProtocol().toLowerCase();
/* 219 */     this.host = paramURL.getHost().toLowerCase();
/* 220 */     this.port = paramURL.getPort();
/* 221 */     if (this.port == -1) {
/* 222 */       this.port = paramURL.getDefaultPort();
/*     */     }
/* 224 */     this.realm = paramString;
/*     */ 
/* 226 */     String str = paramURL.getPath();
/* 227 */     if (str.length() == 0)
/* 228 */       this.path = str;
/*     */     else
/* 230 */       this.path = reducePath(str);
/*     */   }
/*     */ 
/*     */   static String reducePath(String paramString)
/*     */   {
/* 241 */     int i = paramString.lastIndexOf('/');
/* 242 */     int j = paramString.lastIndexOf('.');
/* 243 */     if (i != -1) {
/* 244 */       if (i < j) {
/* 245 */         return paramString.substring(0, i + 1);
/*     */       }
/* 247 */       return paramString;
/*     */     }
/* 249 */     return paramString;
/*     */   }
/*     */ 
/*     */   static AuthenticationInfo getServerAuth(URL paramURL)
/*     */   {
/* 258 */     int i = paramURL.getPort();
/* 259 */     if (i == -1) {
/* 260 */       i = paramURL.getDefaultPort();
/*     */     }
/* 262 */     String str = "s:" + paramURL.getProtocol().toLowerCase() + ":" + paramURL.getHost().toLowerCase() + ":" + i;
/*     */ 
/* 264 */     return getAuth(str, paramURL);
/*     */   }
/*     */ 
/*     */   static String getServerAuthKey(URL paramURL, String paramString, AuthScheme paramAuthScheme)
/*     */   {
/* 274 */     int i = paramURL.getPort();
/* 275 */     if (i == -1) {
/* 276 */       i = paramURL.getDefaultPort();
/*     */     }
/* 278 */     String str = "s:" + paramAuthScheme + ":" + paramURL.getProtocol().toLowerCase() + ":" + paramURL.getHost().toLowerCase() + ":" + i + ":" + paramString;
/*     */ 
/* 280 */     return str;
/*     */   }
/*     */ 
/*     */   static AuthenticationInfo getServerAuth(String paramString) {
/* 284 */     AuthenticationInfo localAuthenticationInfo = getAuth(paramString, null);
/* 285 */     if ((localAuthenticationInfo == null) && (requestIsInProgress(paramString)))
/*     */     {
/* 287 */       localAuthenticationInfo = getAuth(paramString, null);
/*     */     }
/* 289 */     return localAuthenticationInfo;
/*     */   }
/*     */ 
/*     */   static AuthenticationInfo getAuth(String paramString, URL paramURL)
/*     */   {
/* 298 */     if (paramURL == null) {
/* 299 */       return (AuthenticationInfo)cache.get(paramString, null);
/*     */     }
/* 301 */     return (AuthenticationInfo)cache.get(paramString, paramURL.getPath());
/*     */   }
/*     */ 
/*     */   static AuthenticationInfo getProxyAuth(String paramString, int paramInt)
/*     */   {
/* 311 */     String str = "p::" + paramString.toLowerCase() + ":" + paramInt;
/* 312 */     AuthenticationInfo localAuthenticationInfo = (AuthenticationInfo)cache.get(str, null);
/* 313 */     return localAuthenticationInfo;
/*     */   }
/*     */ 
/*     */   static String getProxyAuthKey(String paramString1, int paramInt, String paramString2, AuthScheme paramAuthScheme)
/*     */   {
/* 322 */     String str = "p:" + paramAuthScheme + "::" + paramString1.toLowerCase() + ":" + paramInt + ":" + paramString2;
/*     */ 
/* 324 */     return str;
/*     */   }
/*     */ 
/*     */   static AuthenticationInfo getProxyAuth(String paramString) {
/* 328 */     AuthenticationInfo localAuthenticationInfo = (AuthenticationInfo)cache.get(paramString, null);
/* 329 */     if ((localAuthenticationInfo == null) && (requestIsInProgress(paramString)))
/*     */     {
/* 331 */       localAuthenticationInfo = (AuthenticationInfo)cache.get(paramString, null);
/*     */     }
/* 333 */     return localAuthenticationInfo;
/*     */   }
/*     */ 
/*     */   void addToCache()
/*     */   {
/* 341 */     String str = cacheKey(true);
/* 342 */     cache.put(str, this);
/* 343 */     if (supportsPreemptiveAuthorization()) {
/* 344 */       cache.put(cacheKey(false), this);
/*     */     }
/* 346 */     endAuthRequest(str);
/*     */   }
/*     */ 
/*     */   static void endAuthRequest(String paramString) {
/* 350 */     if (!serializeAuth) {
/* 351 */       return;
/*     */     }
/* 353 */     synchronized (requests) {
/* 354 */       requestCompleted(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeFromCache()
/*     */   {
/* 362 */     cache.remove(cacheKey(true), this);
/* 363 */     if (supportsPreemptiveAuthorization())
/* 364 */       cache.remove(cacheKey(false), this);
/*     */   }
/*     */ 
/*     */   public abstract boolean supportsPreemptiveAuthorization();
/*     */ 
/*     */   public String getHeaderName()
/*     */   {
/* 378 */     if (this.type == 's') {
/* 379 */       return "Authorization";
/*     */     }
/* 381 */     return "Proxy-authorization";
/*     */   }
/*     */ 
/*     */   public abstract String getHeaderValue(URL paramURL, String paramString);
/*     */ 
/*     */   public abstract boolean setHeaders(HttpURLConnection paramHttpURLConnection, HeaderParser paramHeaderParser, String paramString);
/*     */ 
/*     */   public abstract boolean isAuthorizationStale(String paramString);
/*     */ 
/*     */   String cacheKey(boolean paramBoolean)
/*     */   {
/* 425 */     if (paramBoolean) {
/* 426 */       return this.type + ":" + this.authScheme + ":" + this.protocol + ":" + this.host + ":" + this.port + ":" + this.realm;
/*     */     }
/*     */ 
/* 429 */     return this.type + ":" + this.protocol + ":" + this.host + ":" + this.port;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 438 */     paramObjectInputStream.defaultReadObject();
/* 439 */     this.pw = new PasswordAuthentication(this.s1, this.s2.toCharArray());
/* 440 */     this.s1 = null; this.s2 = null;
/*     */   }
/*     */ 
/*     */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 446 */     this.s1 = this.pw.getUserName();
/* 447 */     this.s2 = new String(this.pw.getPassword());
/* 448 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.AuthenticationInfo
 * JD-Core Version:    0.6.2
 */