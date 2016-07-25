/*     */ package sun.security.jca;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.AccessController;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.ProviderException;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.PropertyExpander;
/*     */ 
/*     */ final class ProviderConfig
/*     */ {
/*  45 */   private static final Debug debug = Debug.getInstance("jca", "ProviderConfig");
/*     */   private static final String P11_SOL_NAME = "sun.security.pkcs11.SunPKCS11";
/*     */   private static final String P11_SOL_ARG = "${java.home}/lib/security/sunpkcs11-solaris.cfg";
/*     */   private static final int MAX_LOAD_TRIES = 30;
/*  61 */   private static final Class[] CL_STRING = { String.class };
/*     */   private final String className;
/*     */   private final String argument;
/*     */   private int tries;
/*     */   private volatile Provider provider;
/*     */   private boolean isLoading;
/*     */ 
/*     */   ProviderConfig(String paramString1, String paramString2)
/*     */   {
/*  81 */     if ((paramString1.equals("sun.security.pkcs11.SunPKCS11")) && (paramString2.equals("${java.home}/lib/security/sunpkcs11-solaris.cfg"))) {
/*  82 */       checkSunPKCS11Solaris();
/*     */     }
/*  84 */     this.className = paramString1;
/*  85 */     this.argument = expand(paramString2);
/*     */   }
/*     */ 
/*     */   ProviderConfig(String paramString) {
/*  89 */     this(paramString, "");
/*     */   }
/*     */ 
/*     */   ProviderConfig(Provider paramProvider) {
/*  93 */     this.className = paramProvider.getClass().getName();
/*  94 */     this.argument = "";
/*  95 */     this.provider = paramProvider;
/*     */   }
/*     */ 
/*     */   private void checkSunPKCS11Solaris()
/*     */   {
/* 102 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Boolean run() {
/* 105 */         File localFile = new File("/usr/lib/libpkcs11.so");
/* 106 */         if (!localFile.exists()) {
/* 107 */           return Boolean.FALSE;
/*     */         }
/* 109 */         if ("false".equalsIgnoreCase(System.getProperty("sun.security.pkcs11.enable-solaris")))
/*     */         {
/* 111 */           return Boolean.FALSE;
/*     */         }
/* 113 */         return Boolean.TRUE;
/*     */       }
/*     */     });
/* 116 */     if (localBoolean == Boolean.FALSE)
/* 117 */       this.tries = 30;
/*     */   }
/*     */ 
/*     */   private boolean hasArgument()
/*     */   {
/* 122 */     return this.argument.length() != 0;
/*     */   }
/*     */ 
/*     */   private boolean shouldLoad()
/*     */   {
/* 127 */     return this.tries < 30;
/*     */   }
/*     */ 
/*     */   private void disableLoad()
/*     */   {
/* 132 */     this.tries = 30;
/*     */   }
/*     */ 
/*     */   boolean isLoaded() {
/* 136 */     return this.provider != null;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 140 */     if (this == paramObject) {
/* 141 */       return true;
/*     */     }
/* 143 */     if (!(paramObject instanceof ProviderConfig)) {
/* 144 */       return false;
/*     */     }
/* 146 */     ProviderConfig localProviderConfig = (ProviderConfig)paramObject;
/* 147 */     return (this.className.equals(localProviderConfig.className)) && (this.argument.equals(localProviderConfig.argument));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 152 */     return this.className.hashCode() + this.argument.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 156 */     if (hasArgument()) {
/* 157 */       return this.className + "('" + this.argument + "')";
/*     */     }
/* 159 */     return this.className;
/*     */   }
/*     */ 
/*     */   synchronized Provider getProvider()
/*     */   {
/* 168 */     Provider localProvider = this.provider;
/* 169 */     if (localProvider != null) {
/* 170 */       return localProvider;
/*     */     }
/* 172 */     if (!shouldLoad()) {
/* 173 */       return null;
/*     */     }
/* 175 */     if (this.isLoading)
/*     */     {
/* 178 */       if (debug != null) {
/* 179 */         debug.println("Recursion loading provider: " + this);
/* 180 */         new Exception("Call trace").printStackTrace();
/*     */       }
/* 182 */       return null;
/*     */     }
/*     */     try {
/* 185 */       this.isLoading = true;
/* 186 */       this.tries += 1;
/* 187 */       localProvider = doLoadProvider();
/*     */     } finally {
/* 189 */       this.isLoading = false;
/*     */     }
/* 191 */     this.provider = localProvider;
/* 192 */     return localProvider;
/*     */   }
/*     */ 
/*     */   private Provider doLoadProvider()
/*     */   {
/* 206 */     return (Provider)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Provider run() {
/* 208 */         if (ProviderConfig.debug != null)
/* 209 */           ProviderConfig.debug.println("Loading provider: " + ProviderConfig.this);
/*     */         try
/*     */         {
/* 212 */           ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/*     */ 
/* 214 */           if (localClassLoader != null)
/* 215 */             localObject1 = localClassLoader.loadClass(ProviderConfig.this.className);
/*     */           else
/* 217 */             localObject1 = Class.forName(ProviderConfig.this.className);
/*     */           Object localObject2;
/* 220 */           if (!ProviderConfig.this.hasArgument()) {
/* 221 */             localObject2 = ((Class)localObject1).newInstance();
/*     */           } else {
/* 223 */             Constructor localConstructor = ((Class)localObject1).getConstructor(ProviderConfig.CL_STRING);
/* 224 */             localObject2 = localConstructor.newInstance(new Object[] { ProviderConfig.this.argument });
/*     */           }
/* 226 */           if ((localObject2 instanceof Provider)) {
/* 227 */             if (ProviderConfig.debug != null) {
/* 228 */               ProviderConfig.debug.println("Loaded provider " + localObject2);
/*     */             }
/* 230 */             return (Provider)localObject2;
/*     */           }
/* 232 */           if (ProviderConfig.debug != null) {
/* 233 */             ProviderConfig.debug.println(ProviderConfig.this.className + " is not a provider");
/*     */           }
/* 235 */           ProviderConfig.this.disableLoad();
/* 236 */           return null;
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */           Object localObject1;
/* 240 */           if ((localException instanceof InvocationTargetException))
/* 241 */             localObject1 = ((InvocationTargetException)localException).getCause();
/*     */           else {
/* 243 */             localObject1 = localException;
/*     */           }
/* 245 */           if (ProviderConfig.debug != null) {
/* 246 */             ProviderConfig.debug.println("Error loading provider " + ProviderConfig.this);
/* 247 */             ((Throwable)localObject1).printStackTrace();
/*     */           }
/*     */ 
/* 250 */           if ((localObject1 instanceof ProviderException)) {
/* 251 */             throw ((ProviderException)localObject1);
/*     */           }
/*     */ 
/* 254 */           if ((localObject1 instanceof UnsupportedOperationException))
/* 255 */             ProviderConfig.this.disableLoad();
/*     */         }
/* 257 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static String expand(String paramString)
/*     */   {
/* 270 */     if (!paramString.contains("${")) {
/* 271 */       return paramString;
/*     */     }
/* 273 */     return (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/*     */         try {
/* 276 */           return PropertyExpander.expand(this.val$value);
/*     */         } catch (GeneralSecurityException localGeneralSecurityException) {
/* 278 */           throw new ProviderException(localGeneralSecurityException);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jca.ProviderConfig
 * JD-Core Version:    0.6.2
 */