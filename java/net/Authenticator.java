/*     */ package java.net;
/*     */ 
/*     */ import java.security.Permission;
/*     */ 
/*     */ public abstract class Authenticator
/*     */ {
/*     */   private static Authenticator theAuthenticator;
/*     */   private String requestingHost;
/*     */   private InetAddress requestingSite;
/*     */   private int requestingPort;
/*     */   private String requestingProtocol;
/*     */   private String requestingPrompt;
/*     */   private String requestingScheme;
/*     */   private URL requestingURL;
/*     */   private RequestorType requestingAuthType;
/*     */ 
/*     */   private void reset()
/*     */   {
/*  91 */     this.requestingHost = null;
/*  92 */     this.requestingSite = null;
/*  93 */     this.requestingPort = -1;
/*  94 */     this.requestingProtocol = null;
/*  95 */     this.requestingPrompt = null;
/*  96 */     this.requestingScheme = null;
/*  97 */     this.requestingURL = null;
/*  98 */     this.requestingAuthType = RequestorType.SERVER;
/*     */   }
/*     */ 
/*     */   public static synchronized void setDefault(Authenticator paramAuthenticator)
/*     */   {
/* 123 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 124 */     if (localSecurityManager != null) {
/* 125 */       NetPermission localNetPermission = new NetPermission("setDefaultAuthenticator");
/*     */ 
/* 127 */       localSecurityManager.checkPermission(localNetPermission);
/*     */     }
/*     */ 
/* 130 */     theAuthenticator = paramAuthenticator;
/*     */   }
/*     */ 
/*     */   public static PasswordAuthentication requestPasswordAuthentication(InetAddress paramInetAddress, int paramInt, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 167 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 168 */     if (localSecurityManager != null) {
/* 169 */       localObject1 = new NetPermission("requestPasswordAuthentication");
/*     */ 
/* 171 */       localSecurityManager.checkPermission((Permission)localObject1);
/*     */     }
/*     */ 
/* 174 */     Object localObject1 = theAuthenticator;
/* 175 */     if (localObject1 == null) {
/* 176 */       return null;
/*     */     }
/* 178 */     synchronized (localObject1) {
/* 179 */       ((Authenticator)localObject1).reset();
/* 180 */       ((Authenticator)localObject1).requestingSite = paramInetAddress;
/* 181 */       ((Authenticator)localObject1).requestingPort = paramInt;
/* 182 */       ((Authenticator)localObject1).requestingProtocol = paramString1;
/* 183 */       ((Authenticator)localObject1).requestingPrompt = paramString2;
/* 184 */       ((Authenticator)localObject1).requestingScheme = paramString3;
/* 185 */       return ((Authenticator)localObject1).getPasswordAuthentication();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static PasswordAuthentication requestPasswordAuthentication(String paramString1, InetAddress paramInetAddress, int paramInt, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 229 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 230 */     if (localSecurityManager != null) {
/* 231 */       localObject1 = new NetPermission("requestPasswordAuthentication");
/*     */ 
/* 233 */       localSecurityManager.checkPermission((Permission)localObject1);
/*     */     }
/*     */ 
/* 236 */     Object localObject1 = theAuthenticator;
/* 237 */     if (localObject1 == null) {
/* 238 */       return null;
/*     */     }
/* 240 */     synchronized (localObject1) {
/* 241 */       ((Authenticator)localObject1).reset();
/* 242 */       ((Authenticator)localObject1).requestingHost = paramString1;
/* 243 */       ((Authenticator)localObject1).requestingSite = paramInetAddress;
/* 244 */       ((Authenticator)localObject1).requestingPort = paramInt;
/* 245 */       ((Authenticator)localObject1).requestingProtocol = paramString2;
/* 246 */       ((Authenticator)localObject1).requestingPrompt = paramString3;
/* 247 */       ((Authenticator)localObject1).requestingScheme = paramString4;
/* 248 */       return ((Authenticator)localObject1).getPasswordAuthentication();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static PasswordAuthentication requestPasswordAuthentication(String paramString1, InetAddress paramInetAddress, int paramInt, String paramString2, String paramString3, String paramString4, URL paramURL, RequestorType paramRequestorType)
/*     */   {
/* 296 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 297 */     if (localSecurityManager != null) {
/* 298 */       localObject1 = new NetPermission("requestPasswordAuthentication");
/*     */ 
/* 300 */       localSecurityManager.checkPermission((Permission)localObject1);
/*     */     }
/*     */ 
/* 303 */     Object localObject1 = theAuthenticator;
/* 304 */     if (localObject1 == null) {
/* 305 */       return null;
/*     */     }
/* 307 */     synchronized (localObject1) {
/* 308 */       ((Authenticator)localObject1).reset();
/* 309 */       ((Authenticator)localObject1).requestingHost = paramString1;
/* 310 */       ((Authenticator)localObject1).requestingSite = paramInetAddress;
/* 311 */       ((Authenticator)localObject1).requestingPort = paramInt;
/* 312 */       ((Authenticator)localObject1).requestingProtocol = paramString2;
/* 313 */       ((Authenticator)localObject1).requestingPrompt = paramString3;
/* 314 */       ((Authenticator)localObject1).requestingScheme = paramString4;
/* 315 */       ((Authenticator)localObject1).requestingURL = paramURL;
/* 316 */       ((Authenticator)localObject1).requestingAuthType = paramRequestorType;
/* 317 */       return ((Authenticator)localObject1).getPasswordAuthentication();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final String getRequestingHost()
/*     */   {
/* 332 */     return this.requestingHost;
/*     */   }
/*     */ 
/*     */   protected final InetAddress getRequestingSite()
/*     */   {
/* 344 */     return this.requestingSite;
/*     */   }
/*     */ 
/*     */   protected final int getRequestingPort()
/*     */   {
/* 353 */     return this.requestingPort;
/*     */   }
/*     */ 
/*     */   protected final String getRequestingProtocol()
/*     */   {
/* 367 */     return this.requestingProtocol;
/*     */   }
/*     */ 
/*     */   protected final String getRequestingPrompt()
/*     */   {
/* 377 */     return this.requestingPrompt;
/*     */   }
/*     */ 
/*     */   protected final String getRequestingScheme()
/*     */   {
/* 388 */     return this.requestingScheme;
/*     */   }
/*     */ 
/*     */   protected PasswordAuthentication getPasswordAuthentication()
/*     */   {
/* 398 */     return null;
/*     */   }
/*     */ 
/*     */   protected URL getRequestingURL()
/*     */   {
/* 411 */     return this.requestingURL;
/*     */   }
/*     */ 
/*     */   protected RequestorType getRequestorType()
/*     */   {
/* 423 */     return this.requestingAuthType;
/*     */   }
/*     */ 
/*     */   public static enum RequestorType
/*     */   {
/*  83 */     PROXY, 
/*     */ 
/*  87 */     SERVER;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.Authenticator
 * JD-Core Version:    0.6.2
 */