/*     */ package javax.security.auth.login;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.Objects;
/*     */ import javax.security.auth.AuthPermission;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public abstract class Configuration
/*     */ {
/*     */   private static Configuration configuration;
/* 195 */   private final AccessControlContext acc = AccessController.getContext();
/*     */ 
/*     */   private static void checkPermission(String paramString)
/*     */   {
/* 199 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 200 */     if (localSecurityManager != null)
/* 201 */       localSecurityManager.checkPermission(new AuthPermission("createLoginConfiguration." + paramString));
/*     */   }
/*     */ 
/*     */   public static Configuration getConfiguration()
/*     */   {
/* 229 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 230 */     if (localSecurityManager != null) {
/* 231 */       localSecurityManager.checkPermission(new AuthPermission("getLoginConfiguration"));
/*     */     }
/* 233 */     synchronized (Configuration.class) {
/* 234 */       if (configuration == null) {
/* 235 */         String str1 = null;
/* 236 */         str1 = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public String run() {
/* 239 */             return Security.getProperty("login.configuration.provider");
/*     */           }
/*     */         });
/* 243 */         if (str1 == null) {
/* 244 */           str1 = "com.sun.security.auth.login.ConfigFile";
/*     */         }
/*     */         try
/*     */         {
/* 248 */           String str2 = str1;
/* 249 */           localObject1 = (Configuration)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Configuration run()
/*     */               throws ClassNotFoundException, InstantiationException, IllegalAccessException
/*     */             {
/* 254 */               Class localClass = Class.forName(this.val$finalClass, false, Thread.currentThread().getContextClassLoader()).asSubclass(Configuration.class);
/*     */ 
/* 258 */               return (Configuration)localClass.newInstance();
/*     */             }
/*     */           });
/* 261 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Void run() {
/* 264 */               Configuration.setConfiguration(this.val$untrustedImpl);
/* 265 */               return null;
/*     */             }
/*     */           }
/*     */           , (AccessControlContext)Objects.requireNonNull(((Configuration)localObject1).acc));
/*     */         }
/*     */         catch (PrivilegedActionException localPrivilegedActionException)
/*     */         {
/* 270 */           Object localObject1 = localPrivilegedActionException.getException();
/* 271 */           if ((localObject1 instanceof InstantiationException)) {
/* 272 */             throw ((SecurityException)new SecurityException("Configuration error:" + ((Exception)localObject1).getCause().getMessage() + "\n").initCause(((Exception)localObject1).getCause()));
/*     */           }
/*     */ 
/* 278 */           throw ((SecurityException)new SecurityException("Configuration error: " + ((Exception)localObject1).toString() + "\n").initCause((Throwable)localObject1));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 286 */       return configuration;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void setConfiguration(Configuration paramConfiguration)
/*     */   {
/* 303 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 304 */     if (localSecurityManager != null)
/* 305 */       localSecurityManager.checkPermission(new AuthPermission("setLoginConfiguration"));
/* 306 */     configuration = paramConfiguration;
/*     */   }
/*     */ 
/*     */   public static Configuration getInstance(String paramString, Parameters paramParameters)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 350 */     checkPermission(paramString);
/*     */     try {
/* 352 */       GetInstance.Instance localInstance = GetInstance.getInstance("Configuration", ConfigurationSpi.class, paramString, paramParameters);
/*     */ 
/* 357 */       return new ConfigDelegate((ConfigurationSpi)localInstance.impl, localInstance.provider, paramString, paramParameters, null);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 362 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Configuration getInstance(String paramString1, Parameters paramParameters, String paramString2)
/*     */     throws NoSuchProviderException, NoSuchAlgorithmException
/*     */   {
/* 414 */     if ((paramString2 == null) || (paramString2.length() == 0)) {
/* 415 */       throw new IllegalArgumentException("missing provider");
/*     */     }
/*     */ 
/* 418 */     checkPermission(paramString1);
/*     */     try {
/* 420 */       GetInstance.Instance localInstance = GetInstance.getInstance("Configuration", ConfigurationSpi.class, paramString1, paramParameters, paramString2);
/*     */ 
/* 426 */       return new ConfigDelegate((ConfigurationSpi)localInstance.impl, localInstance.provider, paramString1, paramParameters, null);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 431 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Configuration getInstance(String paramString, Parameters paramParameters, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 476 */     if (paramProvider == null) {
/* 477 */       throw new IllegalArgumentException("missing provider");
/*     */     }
/*     */ 
/* 480 */     checkPermission(paramString);
/*     */     try {
/* 482 */       GetInstance.Instance localInstance = GetInstance.getInstance("Configuration", ConfigurationSpi.class, paramString, paramParameters, paramProvider);
/*     */ 
/* 488 */       return new ConfigDelegate((ConfigurationSpi)localInstance.impl, localInstance.provider, paramString, paramParameters, null);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 493 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Configuration handleException(NoSuchAlgorithmException paramNoSuchAlgorithmException) throws NoSuchAlgorithmException
/*     */   {
/* 499 */     Throwable localThrowable = paramNoSuchAlgorithmException.getCause();
/* 500 */     if ((localThrowable instanceof IllegalArgumentException)) {
/* 501 */       throw ((IllegalArgumentException)localThrowable);
/*     */     }
/* 503 */     throw paramNoSuchAlgorithmException;
/*     */   }
/*     */ 
/*     */   public Provider getProvider()
/*     */   {
/* 518 */     return null;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 533 */     return null;
/*     */   }
/*     */ 
/*     */   public Parameters getParameters()
/*     */   {
/* 548 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract AppConfigurationEntry[] getAppConfigurationEntry(String paramString);
/*     */ 
/*     */   public void refresh()
/*     */   {
/*     */   }
/*     */ 
/*     */   private static class ConfigDelegate extends Configuration
/*     */   {
/*     */     private ConfigurationSpi spi;
/*     */     private Provider p;
/*     */     private String type;
/*     */     private Configuration.Parameters params;
/*     */ 
/*     */     private ConfigDelegate(ConfigurationSpi paramConfigurationSpi, Provider paramProvider, String paramString, Configuration.Parameters paramParameters)
/*     */     {
/* 596 */       this.spi = paramConfigurationSpi;
/* 597 */       this.p = paramProvider;
/* 598 */       this.type = paramString;
/* 599 */       this.params = paramParameters;
/*     */     }
/*     */     public String getType() {
/* 602 */       return this.type;
/*     */     }
/* 604 */     public Configuration.Parameters getParameters() { return this.params; } 
/*     */     public Provider getProvider() {
/* 606 */       return this.p;
/*     */     }
/*     */     public AppConfigurationEntry[] getAppConfigurationEntry(String paramString) {
/* 609 */       return this.spi.engineGetAppConfigurationEntry(paramString);
/*     */     }
/*     */ 
/*     */     public void refresh() {
/* 613 */       this.spi.engineRefresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Parameters
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.login.Configuration
 * JD-Core Version:    0.6.2
 */