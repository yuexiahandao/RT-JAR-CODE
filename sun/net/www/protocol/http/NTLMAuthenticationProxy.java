/*     */ package sun.net.www.protocol.http;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.URL;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ class NTLMAuthenticationProxy
/*     */ {
/*     */   private static Method supportsTA;
/*     */   private static Method isTrustedSite;
/*     */   private static final String clazzStr = "sun.net.www.protocol.http.ntlm.NTLMAuthentication";
/*     */   private static final String supportsTAStr = "supportsTransparentAuth";
/*     */   private static final String isTrustedSiteStr = "isTrustedSite";
/*  44 */   static final NTLMAuthenticationProxy proxy = tryLoadNTLMAuthentication();
/*  45 */   static final boolean supported = proxy != null;
/*  46 */   static final boolean supportsTransparentAuth = supported ? supportsTransparentAuth() : false;
/*     */   private final Constructor<? extends AuthenticationInfo> threeArgCtr;
/*     */   private final Constructor<? extends AuthenticationInfo> fiveArgCtr;
/*     */ 
/*     */   private NTLMAuthenticationProxy(Constructor<? extends AuthenticationInfo> paramConstructor1, Constructor<? extends AuthenticationInfo> paramConstructor2)
/*     */   {
/*  53 */     this.threeArgCtr = paramConstructor1;
/*  54 */     this.fiveArgCtr = paramConstructor2;
/*     */   }
/*     */ 
/*     */   AuthenticationInfo create(boolean paramBoolean, URL paramURL, PasswordAuthentication paramPasswordAuthentication)
/*     */   {
/*     */     try
/*     */     {
/*  62 */       return (AuthenticationInfo)this.threeArgCtr.newInstance(new Object[] { Boolean.valueOf(paramBoolean), paramURL, paramPasswordAuthentication });
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  64 */       finest(localReflectiveOperationException);
/*     */     }
/*     */ 
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   AuthenticationInfo create(boolean paramBoolean, String paramString, int paramInt, PasswordAuthentication paramPasswordAuthentication)
/*     */   {
/*     */     try
/*     */     {
/*  75 */       return (AuthenticationInfo)this.fiveArgCtr.newInstance(new Object[] { Boolean.valueOf(paramBoolean), paramString, Integer.valueOf(paramInt), paramPasswordAuthentication });
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  77 */       finest(localReflectiveOperationException);
/*     */     }
/*     */ 
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   private static boolean supportsTransparentAuth()
/*     */   {
/*     */     try
/*     */     {
/*  89 */       return ((Boolean)supportsTA.invoke(null, new Object[0])).booleanValue();
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/*  91 */       finest(localReflectiveOperationException);
/*     */     }
/*     */ 
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isTrustedSite(URL paramURL)
/*     */   {
/*     */     try
/*     */     {
/* 102 */       return ((Boolean)isTrustedSite.invoke(null, new Object[] { paramURL })).booleanValue();
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 104 */       finest(localReflectiveOperationException);
/*     */     }
/*     */ 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   private static NTLMAuthenticationProxy tryLoadNTLMAuthentication()
/*     */   {
/*     */     try
/*     */     {
/* 120 */       Class localClass = Class.forName("sun.net.www.protocol.http.ntlm.NTLMAuthentication", true, null);
/* 121 */       if (localClass != null) {
/* 122 */         Constructor localConstructor1 = localClass.getConstructor(new Class[] { Boolean.TYPE, URL.class, PasswordAuthentication.class });
/*     */ 
/* 125 */         Constructor localConstructor2 = localClass.getConstructor(new Class[] { Boolean.TYPE, String.class, Integer.TYPE, PasswordAuthentication.class });
/*     */ 
/* 129 */         supportsTA = localClass.getDeclaredMethod("supportsTransparentAuth", new Class[0]);
/* 130 */         isTrustedSite = localClass.getDeclaredMethod("isTrustedSite", new Class[] { URL.class });
/* 131 */         return new NTLMAuthenticationProxy(localConstructor1, localConstructor2);
/*     */       }
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 135 */       finest(localClassNotFoundException);
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 137 */       throw new AssertionError(localReflectiveOperationException);
/*     */     }
/*     */ 
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   static void finest(Exception paramException) {
/* 144 */     PlatformLogger localPlatformLogger = HttpURLConnection.getHttpLogger();
/* 145 */     localPlatformLogger.finest("NTLMAuthenticationProxy: " + paramException);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.NTLMAuthenticationProxy
 * JD-Core Version:    0.6.2
 */