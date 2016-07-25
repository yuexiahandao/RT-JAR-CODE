/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.security.Provider.Service;
/*     */ import sun.security.jca.ProviderList;
/*     */ import sun.security.jca.Providers;
/*     */ 
/*     */ final class SSLSecurity
/*     */ {
/*     */   private static Provider.Service getService(String paramString1, String paramString2)
/*     */   {
/*  62 */     ProviderList localProviderList = Providers.getProviderList();
/*  63 */     for (Provider localProvider : localProviderList.providers()) {
/*  64 */       Provider.Service localService = localProvider.getService(paramString1, paramString2);
/*  65 */       if (localService != null) {
/*  66 */         return localService;
/*     */       }
/*     */     }
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   private static Object[] getImpl1(String paramString1, String paramString2, Provider.Service paramService)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  78 */     Provider localProvider = paramService.getProvider();
/*  79 */     String str = paramService.getClassName();
/*     */     Class localClass1;
/*     */     try
/*     */     {
/*  82 */       ClassLoader localClassLoader = localProvider.getClass().getClassLoader();
/*  83 */       if (localClassLoader == null)
/*     */       {
/*  85 */         localClass1 = Class.forName(str);
/*     */       }
/*  87 */       else localClass1 = localClassLoader.loadClass(str); 
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1)
/*     */     {
/*  90 */       throw new NoSuchAlgorithmException("Class " + str + " configured for " + paramString2 + " not found: " + localClassNotFoundException1.getMessage());
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/*  96 */       throw new NoSuchAlgorithmException("Class " + str + " configured for " + paramString2 + " cannot be accessed: " + localSecurityException.getMessage());
/*     */     }
/*     */ 
/*     */     Object localObject1;
/*     */     try
/*     */     {
/* 138 */       Object localObject2 = null;
/*     */       Class localClass2;
/* 144 */       if (((localClass2 = Class.forName("javax.net.ssl." + paramString2 + "Spi")) != null) && (checkSuperclass(localClass1, localClass2)))
/*     */       {
/* 148 */         if (paramString2.equals("SSLContext"))
/* 149 */           localObject2 = new SSLContextSpiWrapper(paramString1, localProvider);
/* 150 */         else if (paramString2.equals("TrustManagerFactory"))
/* 151 */           localObject2 = new TrustManagerFactorySpiWrapper(paramString1, localProvider);
/* 152 */         else if (paramString2.equals("KeyManagerFactory")) {
/* 153 */           localObject2 = new KeyManagerFactorySpiWrapper(paramString1, localProvider);
/*     */         }
/*     */         else
/*     */         {
/* 160 */           throw new IllegalStateException("Class " + localClass1.getName() + " unknown engineType wrapper:" + paramString2);
/*     */         }
/*     */ 
/*     */       }
/* 165 */       else if (((localObject1 = Class.forName("com.sun.net.ssl." + paramString2 + "Spi")) != null) && (checkSuperclass(localClass1, (Class)localObject1)))
/*     */       {
/* 168 */         localObject2 = paramService.newInstance(null);
/*     */       }
/*     */ 
/* 171 */       if (localObject2 != null) {
/* 172 */         return new Object[] { localObject2, localProvider };
/*     */       }
/* 174 */       throw new NoSuchAlgorithmException("Couldn't locate correct object or wrapper: " + paramString2 + " " + paramString1);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException2)
/*     */     {
/* 180 */       localObject1 = new IllegalStateException("Engine Class Not Found for " + paramString2);
/*     */ 
/* 182 */       ((IllegalStateException)localObject1).initCause(localClassNotFoundException2);
/* 183 */     }throw ((Throwable)localObject1);
/*     */   }
/*     */ 
/*     */   static Object[] getImpl(String paramString1, String paramString2, String paramString3)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/*     */     Provider.Service localService;
/* 199 */     if (paramString3 != null) {
/* 200 */       ProviderList localProviderList = Providers.getProviderList();
/* 201 */       Provider localProvider = localProviderList.getProvider(paramString3);
/* 202 */       if (localProvider == null) {
/* 203 */         throw new NoSuchProviderException("No such provider: " + paramString3);
/*     */       }
/*     */ 
/* 206 */       localService = localProvider.getService(paramString2, paramString1);
/*     */     } else {
/* 208 */       localService = getService(paramString2, paramString1);
/*     */     }
/* 210 */     if (localService == null) {
/* 211 */       throw new NoSuchAlgorithmException("Algorithm " + paramString1 + " not available");
/*     */     }
/*     */ 
/* 214 */     return getImpl1(paramString1, paramString2, localService);
/*     */   }
/*     */ 
/*     */   static Object[] getImpl(String paramString1, String paramString2, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 229 */     Provider.Service localService = paramProvider.getService(paramString2, paramString1);
/* 230 */     if (localService == null) {
/* 231 */       throw new NoSuchAlgorithmException("No such algorithm: " + paramString1);
/*     */     }
/*     */ 
/* 234 */     return getImpl1(paramString1, paramString2, localService);
/*     */   }
/*     */ 
/*     */   private static boolean checkSuperclass(Class paramClass1, Class paramClass2)
/*     */   {
/* 241 */     if ((paramClass1 == null) || (paramClass2 == null)) {
/* 242 */       return false;
/*     */     }
/* 244 */     while (!paramClass1.equals(paramClass2)) {
/* 245 */       paramClass1 = paramClass1.getSuperclass();
/* 246 */       if (paramClass1 == null) {
/* 247 */         return false;
/*     */       }
/*     */     }
/* 250 */     return true;
/*     */   }
/*     */ 
/*     */   static Object[] truncateArray(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2)
/*     */   {
/* 260 */     for (int i = 0; i < paramArrayOfObject2.length; i++) {
/* 261 */       paramArrayOfObject2[i] = paramArrayOfObject1[i];
/*     */     }
/*     */ 
/* 264 */     return paramArrayOfObject2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.SSLSecurity
 * JD-Core Version:    0.6.2
 */