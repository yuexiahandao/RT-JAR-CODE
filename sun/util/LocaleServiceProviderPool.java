/*     */ package sun.util;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.text.spi.BreakIteratorProvider;
/*     */ import java.text.spi.CollatorProvider;
/*     */ import java.text.spi.DateFormatProvider;
/*     */ import java.text.spi.DateFormatSymbolsProvider;
/*     */ import java.text.spi.DecimalFormatSymbolsProvider;
/*     */ import java.text.spi.NumberFormatProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.IllformedLocaleException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Locale.Builder;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle.Control;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.spi.CurrencyNameProvider;
/*     */ import java.util.spi.LocaleNameProvider;
/*     */ import java.util.spi.LocaleServiceProvider;
/*     */ import java.util.spi.TimeZoneNameProvider;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ import sun.util.resources.LocaleData;
/*     */ import sun.util.resources.OpenListResourceBundle;
/*     */ 
/*     */ public final class LocaleServiceProviderPool
/*     */ {
/*  61 */   private static ConcurrentMap<Class<? extends LocaleServiceProvider>, LocaleServiceProviderPool> poolOfPools = new ConcurrentHashMap();
/*     */ 
/*  68 */   private Set<LocaleServiceProvider> providers = new LinkedHashSet();
/*     */ 
/*  74 */   private Map<Locale, LocaleServiceProvider> providersCache = new ConcurrentHashMap();
/*     */ 
/*  81 */   private Set<Locale> availableLocales = null;
/*     */ 
/*  88 */   private static volatile List<Locale> availableJRELocales = null;
/*     */ 
/*  93 */   private Set<Locale> providerLocales = null;
/*     */ 
/*  98 */   private static Locale locale_ja_JP_JP = new Locale("ja", "JP", "JP");
/*     */ 
/* 103 */   private static Locale locale_th_TH_TH = new Locale("th", "TH", "TH");
/*     */ 
/*     */   public static LocaleServiceProviderPool getPool(Class<? extends LocaleServiceProvider> paramClass)
/*     */   {
/* 109 */     Object localObject = (LocaleServiceProviderPool)poolOfPools.get(paramClass);
/* 110 */     if (localObject == null) {
/* 111 */       LocaleServiceProviderPool localLocaleServiceProviderPool = new LocaleServiceProviderPool(paramClass);
/*     */ 
/* 113 */       localObject = (LocaleServiceProviderPool)poolOfPools.putIfAbsent(paramClass, localLocaleServiceProviderPool);
/* 114 */       if (localObject == null) {
/* 115 */         localObject = localLocaleServiceProviderPool;
/*     */       }
/*     */     }
/*     */ 
/* 119 */     return localObject;
/*     */   }
/*     */ 
/*     */   private LocaleServiceProviderPool(final Class<? extends LocaleServiceProvider> paramClass)
/*     */   {
/*     */     try
/*     */     {
/* 129 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Object run() {
/* 131 */           for (LocaleServiceProvider localLocaleServiceProvider : ServiceLoader.loadInstalled(paramClass)) {
/* 132 */             LocaleServiceProviderPool.this.providers.add(localLocaleServiceProvider);
/*     */           }
/* 134 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 138 */       config(localPrivilegedActionException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void config(String paramString) {
/* 143 */     PlatformLogger localPlatformLogger = PlatformLogger.getLogger("sun.util.LocaleServiceProviderPool");
/* 144 */     localPlatformLogger.config(paramString);
/*     */   }
/*     */ 
/*     */   public static Locale[] getAllAvailableLocales()
/*     */   {
/* 201 */     return (Locale[])AllAvailableLocales.allAvailableLocales.clone();
/*     */   }
/*     */ 
/*     */   public synchronized Locale[] getAvailableLocales()
/*     */   {
/* 212 */     if (this.availableLocales == null) {
/* 213 */       this.availableLocales = new HashSet(getJRELocales());
/* 214 */       if (hasProviders()) {
/* 215 */         this.availableLocales.addAll(getProviderLocales());
/*     */       }
/*     */     }
/* 218 */     Locale[] arrayOfLocale = new Locale[this.availableLocales.size()];
/* 219 */     this.availableLocales.toArray(arrayOfLocale);
/* 220 */     return arrayOfLocale;
/*     */   }
/*     */ 
/*     */   private synchronized Set<Locale> getProviderLocales()
/*     */   {
/* 231 */     if (this.providerLocales == null) {
/* 232 */       this.providerLocales = new HashSet();
/* 233 */       if (hasProviders()) {
/* 234 */         for (LocaleServiceProvider localLocaleServiceProvider : this.providers) {
/* 235 */           Locale[] arrayOfLocale1 = localLocaleServiceProvider.getAvailableLocales();
/* 236 */           for (Locale localLocale : arrayOfLocale1) {
/* 237 */             this.providerLocales.add(getLookupLocale(localLocale));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 242 */     return this.providerLocales;
/*     */   }
/*     */ 
/*     */   public boolean hasProviders()
/*     */   {
/* 252 */     return !this.providers.isEmpty();
/*     */   }
/*     */ 
/*     */   private List<Locale> getJRELocales()
/*     */   {
/* 263 */     if (availableJRELocales == null) {
/* 264 */       synchronized (LocaleServiceProviderPool.class) {
/* 265 */         if (availableJRELocales == null) {
/* 266 */           Locale[] arrayOfLocale1 = LocaleData.getAvailableLocales();
/* 267 */           ArrayList localArrayList = new ArrayList(arrayOfLocale1.length);
/* 268 */           for (Locale localLocale : arrayOfLocale1) {
/* 269 */             localArrayList.add(getLookupLocale(localLocale));
/*     */           }
/* 271 */           availableJRELocales = localArrayList;
/*     */         }
/*     */       }
/*     */     }
/* 275 */     return availableJRELocales;
/*     */   }
/*     */ 
/*     */   private boolean isJRESupported(Locale paramLocale)
/*     */   {
/* 286 */     List localList = getJRELocales();
/* 287 */     return localList.contains(getLookupLocale(paramLocale));
/*     */   }
/*     */ 
/*     */   public <P, S> S getLocalizedObject(LocalizedObjectGetter<P, S> paramLocalizedObjectGetter, Locale paramLocale, Object[] paramArrayOfObject)
/*     */   {
/* 303 */     return getLocalizedObjectImpl(paramLocalizedObjectGetter, paramLocale, true, null, null, null, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public <P, S> S getLocalizedObject(LocalizedObjectGetter<P, S> paramLocalizedObjectGetter, Locale paramLocale, OpenListResourceBundle paramOpenListResourceBundle, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 324 */     return getLocalizedObjectImpl(paramLocalizedObjectGetter, paramLocale, false, null, paramOpenListResourceBundle, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public <P, S> S getLocalizedObject(LocalizedObjectGetter<P, S> paramLocalizedObjectGetter, Locale paramLocale, String paramString1, OpenListResourceBundle paramOpenListResourceBundle, String paramString2, Object[] paramArrayOfObject)
/*     */   {
/* 348 */     return getLocalizedObjectImpl(paramLocalizedObjectGetter, paramLocale, false, paramString1, paramOpenListResourceBundle, paramString2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private <P, S> S getLocalizedObjectImpl(LocalizedObjectGetter<P, S> paramLocalizedObjectGetter, Locale paramLocale, boolean paramBoolean, String paramString1, OpenListResourceBundle paramOpenListResourceBundle, String paramString2, Object[] paramArrayOfObject)
/*     */   {
/* 358 */     if (hasProviders()) {
/* 359 */       if (paramString1 == null) {
/* 360 */         paramString1 = paramString2;
/*     */       }
/* 362 */       Object localObject1 = paramOpenListResourceBundle != null ? paramOpenListResourceBundle.getLocale() : null;
/* 363 */       List localList = getLookupLocales(paramLocale);
/* 364 */       Object localObject2 = null;
/*     */ 
/* 370 */       Set localSet = getProviderLocales();
/* 371 */       for (int i = 0; i < localList.size(); i++) {
/* 372 */         Locale localLocale = (Locale)localList.get(i);
/* 373 */         if (localObject1 != null ? 
/* 374 */           localLocale.equals(localObject1) : 
/* 378 */           isJRESupported(localLocale))
/*     */         {
/*     */           break;
/*     */         }
/* 382 */         if (localSet.contains(localLocale))
/*     */         {
/* 385 */           LocaleServiceProvider localLocaleServiceProvider2 = findProvider(localLocale);
/* 386 */           if (localLocaleServiceProvider2 != null) {
/* 387 */             localObject2 = paramLocalizedObjectGetter.getObject(localLocaleServiceProvider2, paramLocale, paramString2, paramArrayOfObject);
/* 388 */             if (localObject2 != null)
/* 389 */               return localObject2;
/* 390 */             if (paramBoolean) {
/* 391 */               config("A locale sensitive service provider returned null for a localized objects,  which should not happen.  provider: " + localLocaleServiceProvider2 + " locale: " + paramLocale);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 400 */       while (paramOpenListResourceBundle != null) {
/* 401 */         localObject1 = paramOpenListResourceBundle.getLocale();
/*     */ 
/* 403 */         if (paramOpenListResourceBundle.handleGetKeys().contains(paramString1))
/*     */         {
/* 405 */           return null;
/*     */         }
/*     */ 
/* 409 */         LocaleServiceProvider localLocaleServiceProvider1 = findProvider((Locale)localObject1);
/* 410 */         if (localLocaleServiceProvider1 != null) {
/* 411 */           localObject2 = paramLocalizedObjectGetter.getObject(localLocaleServiceProvider1, paramLocale, paramString2, paramArrayOfObject);
/* 412 */           if (localObject2 != null) {
/* 413 */             return localObject2;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 419 */         paramOpenListResourceBundle = paramOpenListResourceBundle.getParent();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 424 */     return null;
/*     */   }
/*     */ 
/*     */   private LocaleServiceProvider findProvider(Locale paramLocale)
/*     */   {
/* 436 */     if (!hasProviders())
/* 437 */       return null;
/*     */     Object localObject;
/* 440 */     if (this.providersCache.containsKey(paramLocale)) {
/* 441 */       localObject = (LocaleServiceProvider)this.providersCache.get(paramLocale);
/* 442 */       if (localObject != NullProvider.INSTANCE)
/* 443 */         return localObject;
/*     */     }
/*     */     else {
/* 446 */       for (localObject = this.providers.iterator(); ((Iterator)localObject).hasNext(); ) { LocaleServiceProvider localLocaleServiceProvider1 = (LocaleServiceProvider)((Iterator)localObject).next();
/* 447 */         Locale[] arrayOfLocale1 = localLocaleServiceProvider1.getAvailableLocales();
/* 448 */         for (Locale localLocale : arrayOfLocale1)
/*     */         {
/* 450 */           localLocale = getLookupLocale(localLocale);
/* 451 */           if (paramLocale.equals(localLocale)) {
/* 452 */             LocaleServiceProvider localLocaleServiceProvider2 = (LocaleServiceProvider)this.providersCache.put(paramLocale, localLocaleServiceProvider1);
/*     */ 
/* 454 */             return localLocaleServiceProvider2 != null ? localLocaleServiceProvider2 : localLocaleServiceProvider1;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 460 */       this.providersCache.put(paramLocale, NullProvider.INSTANCE);
/*     */     }
/* 462 */     return null;
/*     */   }
/*     */ 
/*     */   private static List<Locale> getLookupLocales(Locale paramLocale)
/*     */   {
/* 475 */     List localList = new ResourceBundle.Control() {  } .getCandidateLocales("", paramLocale);
/* 476 */     return localList;
/*     */   }
/*     */ 
/*     */   private static Locale getLookupLocale(Locale paramLocale)
/*     */   {
/* 488 */     Locale localLocale = paramLocale;
/* 489 */     Set localSet = paramLocale.getExtensionKeys();
/* 490 */     if ((!localSet.isEmpty()) && (!paramLocale.equals(locale_ja_JP_JP)) && (!paramLocale.equals(locale_th_TH_TH)))
/*     */     {
/* 494 */       Locale.Builder localBuilder = new Locale.Builder();
/*     */       try {
/* 496 */         localBuilder.setLocale(paramLocale);
/* 497 */         localBuilder.clearExtensions();
/* 498 */         localLocale = localBuilder.build();
/*     */       }
/*     */       catch (IllformedLocaleException localIllformedLocaleException)
/*     */       {
/* 504 */         config("A locale(" + paramLocale + ") has non-empty extensions, but has illformed fields.");
/*     */ 
/* 507 */         localLocale = new Locale(paramLocale.getLanguage(), paramLocale.getCountry(), paramLocale.getVariant());
/*     */       }
/*     */     }
/* 510 */     return localLocale;
/*     */   }
/*     */ 
/*     */   private static class AllAvailableLocales
/*     */   {
/* 189 */     static final Locale[] allAvailableLocales = (Locale[])localHashSet.toArray(new Locale[0]);
/*     */ 
/*     */     static
/*     */     {
/* 164 */       Class[] arrayOfClass = (Class[])new Class[] { BreakIteratorProvider.class, CollatorProvider.class, DateFormatProvider.class, DateFormatSymbolsProvider.class, DecimalFormatSymbolsProvider.class, NumberFormatProvider.class, CurrencyNameProvider.class, LocaleNameProvider.class, TimeZoneNameProvider.class };
/*     */ 
/* 177 */       Locale[] arrayOfLocale = LocaleData.getAvailableLocales();
/* 178 */       HashSet localHashSet = new HashSet(arrayOfLocale.length);
/*     */       Locale localLocale;
/* 179 */       for (localLocale : arrayOfLocale) {
/* 180 */         localHashSet.add(LocaleServiceProviderPool.getLookupLocale(localLocale));
/*     */       }
/*     */ 
/* 183 */       for (localLocale : arrayOfClass) {
/* 184 */         LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(localLocale);
/*     */ 
/* 186 */         localHashSet.addAll(localLocaleServiceProviderPool.getProviderLocales());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface LocalizedObjectGetter<P, S>
/*     */   {
/*     */     public abstract S getObject(P paramP, Locale paramLocale, String paramString, Object[] paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private static class NullProvider extends LocaleServiceProvider
/*     */   {
/* 518 */     private static final NullProvider INSTANCE = new NullProvider();
/*     */ 
/*     */     public Locale[] getAvailableLocales() {
/* 521 */       throw new RuntimeException("Should not get called.");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.LocaleServiceProviderPool
 * JD-Core Version:    0.6.2
 */