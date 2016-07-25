/*     */ package sun.security.jca;
/*     */ 
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.security.Provider.Service;
/*     */ import java.util.List;
/*     */ 
/*     */ public class GetInstance
/*     */ {
/*     */   public static Provider.Service getService(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  66 */     ProviderList localProviderList = Providers.getProviderList();
/*  67 */     Provider.Service localService = localProviderList.getService(paramString1, paramString2);
/*  68 */     if (localService == null) {
/*  69 */       throw new NoSuchAlgorithmException(paramString2 + " " + paramString1 + " not available");
/*     */     }
/*     */ 
/*  72 */     return localService;
/*     */   }
/*     */ 
/*     */   public static Provider.Service getService(String paramString1, String paramString2, String paramString3)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/*  78 */     if ((paramString3 == null) || (paramString3.length() == 0)) {
/*  79 */       throw new IllegalArgumentException("missing provider");
/*     */     }
/*  81 */     Provider localProvider = Providers.getProviderList().getProvider(paramString3);
/*  82 */     if (localProvider == null) {
/*  83 */       throw new NoSuchProviderException("no such provider: " + paramString3);
/*     */     }
/*  85 */     Provider.Service localService = localProvider.getService(paramString1, paramString2);
/*  86 */     if (localService == null) {
/*  87 */       throw new NoSuchAlgorithmException("no such algorithm: " + paramString2 + " for provider " + paramString3);
/*     */     }
/*     */ 
/*  90 */     return localService;
/*     */   }
/*     */ 
/*     */   public static Provider.Service getService(String paramString1, String paramString2, Provider paramProvider) throws NoSuchAlgorithmException
/*     */   {
/*  95 */     if (paramProvider == null) {
/*  96 */       throw new IllegalArgumentException("missing provider");
/*     */     }
/*  98 */     Provider.Service localService = paramProvider.getService(paramString1, paramString2);
/*  99 */     if (localService == null) {
/* 100 */       throw new NoSuchAlgorithmException("no such algorithm: " + paramString2 + " for provider " + paramProvider.getName());
/*     */     }
/*     */ 
/* 103 */     return localService;
/*     */   }
/*     */ 
/*     */   public static List<Provider.Service> getServices(String paramString1, String paramString2)
/*     */   {
/* 113 */     ProviderList localProviderList = Providers.getProviderList();
/* 114 */     return localProviderList.getServices(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static List<Provider.Service> getServices(String paramString, List<String> paramList)
/*     */   {
/* 125 */     ProviderList localProviderList = Providers.getProviderList();
/* 126 */     return localProviderList.getServices(paramString, paramList);
/*     */   }
/*     */ 
/*     */   public static List<Provider.Service> getServices(List<ServiceId> paramList)
/*     */   {
/* 134 */     ProviderList localProviderList = Providers.getProviderList();
/* 135 */     return localProviderList.getServices(paramList);
/*     */   }
/*     */ 
/*     */   public static Instance getInstance(String paramString1, Class paramClass, String paramString2)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 156 */     ProviderList localProviderList = Providers.getProviderList();
/* 157 */     Provider.Service localService1 = localProviderList.getService(paramString1, paramString2);
/* 158 */     if (localService1 == null) {
/* 159 */       throw new NoSuchAlgorithmException(paramString2 + " " + paramString1 + " not available");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 164 */       return getInstance(localService1, paramClass);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException1) {
/* 166 */       Object localObject = localNoSuchAlgorithmException1;
/*     */ 
/* 170 */       for (Provider.Service localService2 : localProviderList.getServices(paramString1, paramString2))
/* 171 */         if (localService2 != localService1)
/*     */         {
/*     */           try
/*     */           {
/* 176 */             return getInstance(localService2, paramClass);
/*     */           } catch (NoSuchAlgorithmException localNoSuchAlgorithmException2) {
/* 178 */             localObject = localNoSuchAlgorithmException2;
/*     */           }
/*     */         }
/* 181 */       throw ((Throwable)localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Instance getInstance(String paramString1, Class paramClass, String paramString2, Object paramObject) throws NoSuchAlgorithmException {
/* 186 */     List localList = getServices(paramString1, paramString2);
/* 187 */     Object localObject = null;
/* 188 */     for (Provider.Service localService : localList) {
/*     */       try {
/* 190 */         return getInstance(localService, paramClass, paramObject);
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 192 */         localObject = localNoSuchAlgorithmException;
/*     */       }
/*     */     }
/* 195 */     if (localObject != null) {
/* 196 */       throw localObject;
/*     */     }
/* 198 */     throw new NoSuchAlgorithmException(paramString2 + " " + paramString1 + " not available");
/*     */   }
/*     */ 
/*     */   public static Instance getInstance(String paramString1, Class paramClass, String paramString2, String paramString3)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 206 */     return getInstance(getService(paramString1, paramString2, paramString3), paramClass);
/*     */   }
/*     */ 
/*     */   public static Instance getInstance(String paramString1, Class paramClass, String paramString2, Object paramObject, String paramString3)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 212 */     return getInstance(getService(paramString1, paramString2, paramString3), paramClass, paramObject);
/*     */   }
/*     */ 
/*     */   public static Instance getInstance(String paramString1, Class paramClass, String paramString2, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 218 */     return getInstance(getService(paramString1, paramString2, paramProvider), paramClass);
/*     */   }
/*     */ 
/*     */   public static Instance getInstance(String paramString1, Class paramClass, String paramString2, Object paramObject, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 224 */     return getInstance(getService(paramString1, paramString2, paramProvider), paramClass, paramObject);
/*     */   }
/*     */ 
/*     */   public static Instance getInstance(Provider.Service paramService, Class paramClass)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 236 */     Object localObject = paramService.newInstance(null);
/* 237 */     checkSuperClass(paramService, localObject.getClass(), paramClass);
/* 238 */     return new Instance(paramService.getProvider(), localObject, null);
/*     */   }
/*     */ 
/*     */   public static Instance getInstance(Provider.Service paramService, Class paramClass, Object paramObject) throws NoSuchAlgorithmException
/*     */   {
/* 243 */     Object localObject = paramService.newInstance(paramObject);
/* 244 */     checkSuperClass(paramService, localObject.getClass(), paramClass);
/* 245 */     return new Instance(paramService.getProvider(), localObject, null);
/*     */   }
/*     */ 
/*     */   public static void checkSuperClass(Provider.Service paramService, Class paramClass1, Class paramClass2)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 254 */     if (paramClass2 == null) {
/* 255 */       return;
/*     */     }
/* 257 */     if (!paramClass2.isAssignableFrom(paramClass1))
/* 258 */       throw new NoSuchAlgorithmException("class configured for " + paramService.getType() + ": " + paramService.getClassName() + " not a " + paramService.getType());
/*     */   }
/*     */ 
/*     */   public static final class Instance
/*     */   {
/*     */     public final Provider provider;
/*     */     public final Object impl;
/*     */ 
/*     */     private Instance(Provider paramProvider, Object paramObject)
/*     */     {
/*  54 */       this.provider = paramProvider;
/*  55 */       this.impl = paramObject;
/*     */     }
/*     */ 
/*     */     public Object[] toArray()
/*     */     {
/*  60 */       return new Object[] { this.impl, this.provider };
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jca.GetInstance
 * JD-Core Version:    0.6.2
 */