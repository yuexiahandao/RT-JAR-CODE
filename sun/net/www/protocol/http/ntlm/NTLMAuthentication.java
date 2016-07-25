/*     */ package sun.net.www.protocol.http.ntlm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.net.www.HeaderParser;
/*     */ import sun.net.www.protocol.http.AuthScheme;
/*     */ import sun.net.www.protocol.http.AuthenticationInfo;
/*     */ import sun.net.www.protocol.http.HttpURLConnection;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class NTLMAuthentication extends AuthenticationInfo
/*     */ {
/*     */   private static final long serialVersionUID = 100L;
/*  48 */   private static final NTLMAuthenticationCallback NTLMAuthCallback = NTLMAuthenticationCallback.getNTLMAuthenticationCallback();
/*     */   private String hostname;
/*  55 */   private static String defaultDomain = (String)AccessController.doPrivileged(new GetPropertyAction("http.auth.ntlm.domain", "domain"));
/*     */   String username;
/*     */   String ntdomain;
/*     */   String password;
/*     */ 
/*     */   private void init0()
/*     */   {
/*  62 */     this.hostname = ((String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/*     */         String str;
/*     */         try {
/*  67 */           str = InetAddress.getLocalHost().getHostName().toUpperCase();
/*     */         } catch (UnknownHostException localUnknownHostException) {
/*  69 */           str = "localhost";
/*     */         }
/*  71 */         return str;
/*     */       }
/*     */     }));
/*  74 */     int i = this.hostname.indexOf('.');
/*  75 */     if (i != -1)
/*  76 */       this.hostname = this.hostname.substring(0, i);
/*     */   }
/*     */ 
/*     */   public NTLMAuthentication(boolean paramBoolean, URL paramURL, PasswordAuthentication paramPasswordAuthentication)
/*     */   {
/*  91 */     super(paramBoolean ? 'p' : 's', AuthScheme.NTLM, paramURL, "");
/*     */ 
/*  95 */     init(paramPasswordAuthentication);
/*     */   }
/*     */ 
/*     */   private void init(PasswordAuthentication paramPasswordAuthentication) {
/*  99 */     this.pw = paramPasswordAuthentication;
/* 100 */     if (paramPasswordAuthentication != null) {
/* 101 */       String str = paramPasswordAuthentication.getUserName();
/* 102 */       int i = str.indexOf('\\');
/* 103 */       if (i == -1) {
/* 104 */         this.username = str;
/* 105 */         this.ntdomain = defaultDomain;
/*     */       } else {
/* 107 */         this.ntdomain = str.substring(0, i).toUpperCase();
/* 108 */         this.username = str.substring(i + 1);
/*     */       }
/* 110 */       this.password = new String(paramPasswordAuthentication.getPassword());
/*     */     }
/*     */     else {
/* 113 */       this.username = null;
/* 114 */       this.ntdomain = null;
/* 115 */       this.password = null;
/*     */     }
/* 117 */     init0();
/*     */   }
/*     */ 
/*     */   public NTLMAuthentication(boolean paramBoolean, String paramString, int paramInt, PasswordAuthentication paramPasswordAuthentication)
/*     */   {
/* 125 */     super(paramBoolean ? 'p' : 's', AuthScheme.NTLM, paramString, paramInt, "");
/*     */ 
/* 130 */     init(paramPasswordAuthentication);
/*     */   }
/*     */ 
/*     */   public boolean supportsPreemptiveAuthorization()
/*     */   {
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean supportsTransparentAuth()
/*     */   {
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isTrustedSite(URL paramURL)
/*     */   {
/* 153 */     return NTLMAuthCallback.isTrustedSite(paramURL);
/*     */   }
/*     */ 
/*     */   public String getHeaderValue(URL paramURL, String paramString)
/*     */   {
/* 161 */     throw new RuntimeException("getHeaderValue not supported");
/*     */   }
/*     */ 
/*     */   public boolean isAuthorizationStale(String paramString)
/*     */   {
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized boolean setHeaders(HttpURLConnection paramHttpURLConnection, HeaderParser paramHeaderParser, String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 190 */       NTLMAuthSequence localNTLMAuthSequence = (NTLMAuthSequence)paramHttpURLConnection.authObj();
/* 191 */       if (localNTLMAuthSequence == null) {
/* 192 */         localNTLMAuthSequence = new NTLMAuthSequence(this.username, this.password, this.ntdomain);
/* 193 */         paramHttpURLConnection.authObj(localNTLMAuthSequence);
/*     */       }
/* 195 */       String str = "NTLM " + localNTLMAuthSequence.getAuthHeader(paramString.length() > 6 ? paramString.substring(5) : null);
/* 196 */       paramHttpURLConnection.setAuthenticationProperty(getHeaderName(), str);
/* 197 */       if (localNTLMAuthSequence.isComplete()) {
/* 198 */         paramHttpURLConnection.authObj(null);
/*     */       }
/* 200 */       return true;
/*     */     } catch (IOException localIOException) {
/* 202 */       paramHttpURLConnection.authObj(null);
/* 203 */     }return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.ntlm.NTLMAuthentication
 * JD-Core Version:    0.6.2
 */