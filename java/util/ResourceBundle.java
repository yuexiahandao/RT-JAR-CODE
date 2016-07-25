/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.net.JarURLConnection;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.jar.JarEntry;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.Reflection;
/*      */ import sun.util.locale.BaseLocale;
/*      */ import sun.util.locale.LocaleObjectCache;
/*      */ 
/*      */ public abstract class ResourceBundle
/*      */ {
/*      */   private static final int INITIAL_CACHE_SIZE = 32;
/*  274 */   private static final ResourceBundle NONEXISTENT_BUNDLE = new ResourceBundle() {
/*  275 */     public Enumeration<String> getKeys() { return null; } 
/*  276 */     protected Object handleGetObject(String paramAnonymousString) { return null; } 
/*  277 */     public String toString() { return "NONEXISTENT_BUNDLE"; }
/*      */ 
/*  274 */   };
/*      */ 
/*  293 */   private static final ConcurrentMap<CacheKey, BundleReference> cacheList = new ConcurrentHashMap(32);
/*      */ 
/*  299 */   private static final ReferenceQueue referenceQueue = new ReferenceQueue();
/*      */ 
/*  306 */   protected ResourceBundle parent = null;
/*      */ 
/*  311 */   private Locale locale = null;
/*      */   private String name;
/*      */   private volatile boolean expired;
/*      */   private volatile CacheKey cacheKey;
/*      */   private volatile Set<String> keySet;
/*      */ 
/*      */   public final String getString(String paramString)
/*      */   {
/*  355 */     return (String)getObject(paramString);
/*      */   }
/*      */ 
/*      */   public final String[] getStringArray(String paramString)
/*      */   {
/*  372 */     return (String[])getObject(paramString);
/*      */   }
/*      */ 
/*      */   public final Object getObject(String paramString)
/*      */   {
/*  389 */     Object localObject = handleGetObject(paramString);
/*  390 */     if (localObject == null) {
/*  391 */       if (this.parent != null) {
/*  392 */         localObject = this.parent.getObject(paramString);
/*      */       }
/*  394 */       if (localObject == null) {
/*  395 */         throw new MissingResourceException("Can't find resource for bundle " + getClass().getName() + ", key " + paramString, getClass().getName(), paramString);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  401 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  412 */     return this.locale;
/*      */   }
/*      */ 
/*      */   private static ClassLoader getLoader(Class<?> paramClass)
/*      */   {
/*  420 */     Object localObject = paramClass == null ? null : paramClass.getClassLoader();
/*  421 */     if (localObject == null)
/*      */     {
/*  429 */       localObject = RBClassLoader.INSTANCE;
/*      */     }
/*  431 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected void setParent(ResourceBundle paramResourceBundle)
/*      */   {
/*  476 */     assert (paramResourceBundle != NONEXISTENT_BUNDLE);
/*  477 */     this.parent = paramResourceBundle;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static final ResourceBundle getBundle(String paramString)
/*      */   {
/*  721 */     return getBundleImpl(paramString, Locale.getDefault(), getLoader(Reflection.getCallerClass()), Control.INSTANCE);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static final ResourceBundle getBundle(String paramString, Control paramControl)
/*      */   {
/*  764 */     return getBundleImpl(paramString, Locale.getDefault(), getLoader(Reflection.getCallerClass()), paramControl);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static final ResourceBundle getBundle(String paramString, Locale paramLocale)
/*      */   {
/*  795 */     return getBundleImpl(paramString, paramLocale, getLoader(Reflection.getCallerClass()), Control.INSTANCE);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static final ResourceBundle getBundle(String paramString, Locale paramLocale, Control paramControl)
/*      */   {
/*  841 */     return getBundleImpl(paramString, paramLocale, getLoader(Reflection.getCallerClass()), paramControl);
/*      */   }
/*      */ 
/*      */   public static ResourceBundle getBundle(String paramString, Locale paramLocale, ClassLoader paramClassLoader)
/*      */   {
/* 1025 */     if (paramClassLoader == null) {
/* 1026 */       throw new NullPointerException();
/*      */     }
/* 1028 */     return getBundleImpl(paramString, paramLocale, paramClassLoader, Control.INSTANCE);
/*      */   }
/*      */ 
/*      */   public static ResourceBundle getBundle(String paramString, Locale paramLocale, ClassLoader paramClassLoader, Control paramControl)
/*      */   {
/* 1243 */     if ((paramClassLoader == null) || (paramControl == null)) {
/* 1244 */       throw new NullPointerException();
/*      */     }
/* 1246 */     return getBundleImpl(paramString, paramLocale, paramClassLoader, paramControl);
/*      */   }
/*      */ 
/*      */   private static ResourceBundle getBundleImpl(String paramString, Locale paramLocale, ClassLoader paramClassLoader, Control paramControl)
/*      */   {
/* 1251 */     if ((paramLocale == null) || (paramControl == null)) {
/* 1252 */       throw new NullPointerException();
/*      */     }
/*      */ 
/* 1259 */     CacheKey localCacheKey = new CacheKey(paramString, paramLocale, paramClassLoader);
/* 1260 */     Object localObject1 = null;
/*      */ 
/* 1263 */     BundleReference localBundleReference = (BundleReference)cacheList.get(localCacheKey);
/* 1264 */     if (localBundleReference != null) {
/* 1265 */       localObject1 = (ResourceBundle)localBundleReference.get();
/* 1266 */       localBundleReference = null;
/*      */     }
/*      */ 
/* 1273 */     if ((isValidBundle((ResourceBundle)localObject1)) && (hasValidParentChain((ResourceBundle)localObject1))) {
/* 1274 */       return localObject1;
/*      */     }
/*      */ 
/* 1280 */     int i = (paramControl == Control.INSTANCE) || ((paramControl instanceof SingleFormatControl)) ? 1 : 0;
/*      */ 
/* 1282 */     List localList1 = paramControl.getFormats(paramString);
/* 1283 */     if ((i == 0) && (!checkList(localList1))) {
/* 1284 */       throw new IllegalArgumentException("Invalid Control: getFormats");
/*      */     }
/*      */ 
/* 1287 */     Object localObject2 = null;
/* 1288 */     for (Locale localLocale = paramLocale; 
/* 1289 */       localLocale != null; 
/* 1290 */       localLocale = paramControl.getFallbackLocale(paramString, localLocale)) {
/* 1291 */       List localList2 = paramControl.getCandidateLocales(paramString, localLocale);
/* 1292 */       if ((i == 0) && (!checkList(localList2))) {
/* 1293 */         throw new IllegalArgumentException("Invalid Control: getCandidateLocales");
/*      */       }
/*      */ 
/* 1296 */       localObject1 = findBundle(localCacheKey, localList2, localList1, 0, paramControl, (ResourceBundle)localObject2);
/*      */ 
/* 1303 */       if (isValidBundle((ResourceBundle)localObject1)) {
/* 1304 */         boolean bool = Locale.ROOT.equals(((ResourceBundle)localObject1).locale);
/* 1305 */         if ((!bool) || (((ResourceBundle)localObject1).locale.equals(paramLocale)) || ((localList2.size() == 1) && (((ResourceBundle)localObject1).locale.equals(localList2.get(0)))))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/* 1314 */         if ((bool) && (localObject2 == null)) {
/* 1315 */           localObject2 = localObject1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1320 */     if (localObject1 == null) {
/* 1321 */       if (localObject2 == null) {
/* 1322 */         throwMissingResourceException(paramString, paramLocale, localCacheKey.getCause());
/*      */       }
/* 1324 */       localObject1 = localObject2;
/*      */     }
/*      */ 
/* 1327 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static final boolean checkList(List paramList)
/*      */   {
/* 1335 */     boolean bool = (paramList != null) && (paramList.size() != 0);
/* 1336 */     if (bool) {
/* 1337 */       int i = paramList.size();
/* 1338 */       for (int j = 0; (bool) && (j < i); j++) {
/* 1339 */         bool = paramList.get(j) != null;
/*      */       }
/*      */     }
/* 1342 */     return bool;
/*      */   }
/*      */ 
/*      */   private static final ResourceBundle findBundle(CacheKey paramCacheKey, List<Locale> paramList, List<String> paramList1, int paramInt, Control paramControl, ResourceBundle paramResourceBundle)
/*      */   {
/* 1351 */     Locale localLocale = (Locale)paramList.get(paramInt);
/* 1352 */     ResourceBundle localResourceBundle1 = null;
/* 1353 */     if (paramInt != paramList.size() - 1) {
/* 1354 */       localResourceBundle1 = findBundle(paramCacheKey, paramList, paramList1, paramInt + 1, paramControl, paramResourceBundle);
/*      */     }
/* 1356 */     else if ((paramResourceBundle != null) && (Locale.ROOT.equals(localLocale)))
/* 1357 */       return paramResourceBundle;
/*      */     Reference localReference;
/* 1365 */     while ((localReference = referenceQueue.poll()) != null) {
/* 1366 */       cacheList.remove(((CacheKeyReference)localReference).getCacheKey());
/*      */     }
/*      */ 
/* 1370 */     boolean bool = false;
/*      */ 
/* 1374 */     paramCacheKey.setLocale(localLocale);
/* 1375 */     ResourceBundle localResourceBundle2 = findBundleInCache(paramCacheKey, paramControl);
/*      */     Object localObject1;
/* 1376 */     if (isValidBundle(localResourceBundle2)) {
/* 1377 */       bool = localResourceBundle2.expired;
/* 1378 */       if (!bool)
/*      */       {
/* 1384 */         if (localResourceBundle2.parent == localResourceBundle1) {
/* 1385 */           return localResourceBundle2;
/*      */         }
/*      */ 
/* 1389 */         localObject1 = (BundleReference)cacheList.get(paramCacheKey);
/* 1390 */         if ((localObject1 != null) && (((BundleReference)localObject1).get() == localResourceBundle2)) {
/* 1391 */           cacheList.remove(paramCacheKey, localObject1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1396 */     if (localResourceBundle2 != NONEXISTENT_BUNDLE) {
/* 1397 */       localObject1 = (CacheKey)paramCacheKey.clone();
/*      */       try
/*      */       {
/* 1400 */         localResourceBundle2 = loadBundle(paramCacheKey, paramList1, paramControl, bool);
/* 1401 */         if (localResourceBundle2 != null) {
/* 1402 */           if (localResourceBundle2.parent == null) {
/* 1403 */             localResourceBundle2.setParent(localResourceBundle1);
/*      */           }
/* 1405 */           localResourceBundle2.locale = localLocale;
/* 1406 */           localResourceBundle2 = putBundleInCache(paramCacheKey, localResourceBundle2, paramControl);
/* 1407 */           return localResourceBundle2;
/*      */         }
/*      */ 
/* 1412 */         putBundleInCache(paramCacheKey, NONEXISTENT_BUNDLE, paramControl);
/*      */       } finally {
/* 1414 */         if ((((CacheKey)localObject1).getCause() instanceof InterruptedException)) {
/* 1415 */           Thread.currentThread().interrupt();
/*      */         }
/*      */       }
/*      */     }
/* 1419 */     return localResourceBundle1;
/*      */   }
/*      */ 
/*      */   private static final ResourceBundle loadBundle(CacheKey paramCacheKey, List<String> paramList, Control paramControl, boolean paramBoolean)
/*      */   {
/* 1429 */     Locale localLocale = paramCacheKey.getLocale();
/*      */ 
/* 1431 */     ResourceBundle localResourceBundle = null;
/* 1432 */     int i = paramList.size();
/* 1433 */     for (int j = 0; j < i; j++) {
/* 1434 */       String str = (String)paramList.get(j);
/*      */       try {
/* 1436 */         localResourceBundle = paramControl.newBundle(paramCacheKey.getName(), localLocale, str, paramCacheKey.getLoader(), paramBoolean);
/*      */       }
/*      */       catch (LinkageError localLinkageError)
/*      */       {
/* 1442 */         paramCacheKey.setCause(localLinkageError);
/*      */       } catch (Exception localException) {
/* 1444 */         paramCacheKey.setCause(localException);
/*      */       }
/* 1446 */       if (localResourceBundle != null)
/*      */       {
/* 1449 */         paramCacheKey.setFormat(str);
/* 1450 */         localResourceBundle.name = paramCacheKey.getName();
/* 1451 */         localResourceBundle.locale = localLocale;
/*      */ 
/* 1454 */         localResourceBundle.expired = false;
/* 1455 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1459 */     return localResourceBundle;
/*      */   }
/*      */ 
/*      */   private static final boolean isValidBundle(ResourceBundle paramResourceBundle) {
/* 1463 */     return (paramResourceBundle != null) && (paramResourceBundle != NONEXISTENT_BUNDLE);
/*      */   }
/*      */ 
/*      */   private static final boolean hasValidParentChain(ResourceBundle paramResourceBundle)
/*      */   {
/* 1471 */     long l1 = System.currentTimeMillis();
/* 1472 */     while (paramResourceBundle != null) {
/* 1473 */       if (paramResourceBundle.expired) {
/* 1474 */         return false;
/*      */       }
/* 1476 */       CacheKey localCacheKey = paramResourceBundle.cacheKey;
/* 1477 */       if (localCacheKey != null) {
/* 1478 */         long l2 = localCacheKey.expirationTime;
/* 1479 */         if ((l2 >= 0L) && (l2 <= l1)) {
/* 1480 */           return false;
/*      */         }
/*      */       }
/* 1483 */       paramResourceBundle = paramResourceBundle.parent;
/*      */     }
/* 1485 */     return true;
/*      */   }
/*      */ 
/*      */   private static final void throwMissingResourceException(String paramString, Locale paramLocale, Throwable paramThrowable)
/*      */   {
/* 1496 */     if ((paramThrowable instanceof MissingResourceException)) {
/* 1497 */       paramThrowable = null;
/*      */     }
/* 1499 */     throw new MissingResourceException("Can't find bundle for base name " + paramString + ", locale " + paramLocale, paramString + "_" + paramLocale, "", paramThrowable);
/*      */   }
/*      */ 
/*      */   private static final ResourceBundle findBundleInCache(CacheKey paramCacheKey, Control paramControl)
/*      */   {
/* 1518 */     BundleReference localBundleReference = (BundleReference)cacheList.get(paramCacheKey);
/* 1519 */     if (localBundleReference == null) {
/* 1520 */       return null;
/*      */     }
/* 1522 */     ResourceBundle localResourceBundle1 = (ResourceBundle)localBundleReference.get();
/* 1523 */     if (localResourceBundle1 == null) {
/* 1524 */       return null;
/*      */     }
/* 1526 */     ResourceBundle localResourceBundle2 = localResourceBundle1.parent;
/* 1527 */     assert (localResourceBundle2 != NONEXISTENT_BUNDLE);
/*      */ 
/* 1561 */     if ((localResourceBundle2 != null) && (localResourceBundle2.expired)) {
/* 1562 */       assert (localResourceBundle1 != NONEXISTENT_BUNDLE);
/* 1563 */       localResourceBundle1.expired = true;
/* 1564 */       localResourceBundle1.cacheKey = null;
/* 1565 */       cacheList.remove(paramCacheKey, localBundleReference);
/* 1566 */       localResourceBundle1 = null;
/*      */     } else {
/* 1568 */       CacheKey localCacheKey = localBundleReference.getCacheKey();
/* 1569 */       long l = localCacheKey.expirationTime;
/* 1570 */       if ((!localResourceBundle1.expired) && (l >= 0L) && (l <= System.currentTimeMillis()))
/*      */       {
/* 1573 */         if (localResourceBundle1 != NONEXISTENT_BUNDLE)
/*      */         {
/* 1576 */           synchronized (localResourceBundle1) {
/* 1577 */             l = localCacheKey.expirationTime;
/* 1578 */             if ((!localResourceBundle1.expired) && (l >= 0L) && (l <= System.currentTimeMillis()))
/*      */             {
/*      */               try {
/* 1581 */                 localResourceBundle1.expired = paramControl.needsReload(localCacheKey.getName(), localCacheKey.getLocale(), localCacheKey.getFormat(), localCacheKey.getLoader(), localResourceBundle1, localCacheKey.loadTime);
/*      */               }
/*      */               catch (Exception localException)
/*      */               {
/* 1588 */                 paramCacheKey.setCause(localException);
/*      */               }
/* 1590 */               if (localResourceBundle1.expired)
/*      */               {
/* 1595 */                 localResourceBundle1.cacheKey = null;
/* 1596 */                 cacheList.remove(paramCacheKey, localBundleReference);
/*      */               }
/*      */               else
/*      */               {
/* 1600 */                 setExpirationTime(localCacheKey, paramControl);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/* 1606 */           cacheList.remove(paramCacheKey, localBundleReference);
/* 1607 */           localResourceBundle1 = null;
/*      */         }
/*      */       }
/*      */     }
/* 1611 */     return localResourceBundle1;
/*      */   }
/*      */ 
/*      */   private static final ResourceBundle putBundleInCache(CacheKey paramCacheKey, ResourceBundle paramResourceBundle, Control paramControl)
/*      */   {
/* 1626 */     setExpirationTime(paramCacheKey, paramControl);
/* 1627 */     if (paramCacheKey.expirationTime != -1L) {
/* 1628 */       CacheKey localCacheKey = (CacheKey)paramCacheKey.clone();
/* 1629 */       BundleReference localBundleReference1 = new BundleReference(paramResourceBundle, referenceQueue, localCacheKey);
/* 1630 */       paramResourceBundle.cacheKey = localCacheKey;
/*      */ 
/* 1633 */       BundleReference localBundleReference2 = (BundleReference)cacheList.putIfAbsent(localCacheKey, localBundleReference1);
/*      */ 
/* 1637 */       if (localBundleReference2 != null) {
/* 1638 */         ResourceBundle localResourceBundle = (ResourceBundle)localBundleReference2.get();
/* 1639 */         if ((localResourceBundle != null) && (!localResourceBundle.expired))
/*      */         {
/* 1641 */           paramResourceBundle.cacheKey = null;
/* 1642 */           paramResourceBundle = localResourceBundle;
/*      */ 
/* 1645 */           localBundleReference1.clear();
/*      */         }
/*      */         else
/*      */         {
/* 1649 */           cacheList.put(localCacheKey, localBundleReference1);
/*      */         }
/*      */       }
/*      */     }
/* 1653 */     return paramResourceBundle;
/*      */   }
/*      */ 
/*      */   private static final void setExpirationTime(CacheKey paramCacheKey, Control paramControl) {
/* 1657 */     long l1 = paramControl.getTimeToLive(paramCacheKey.getName(), paramCacheKey.getLocale());
/*      */ 
/* 1659 */     if (l1 >= 0L)
/*      */     {
/* 1662 */       long l2 = System.currentTimeMillis();
/* 1663 */       paramCacheKey.loadTime = l2;
/* 1664 */       paramCacheKey.expirationTime = (l2 + l1);
/* 1665 */     } else if (l1 >= -2L) {
/* 1666 */       paramCacheKey.expirationTime = l1;
/*      */     } else {
/* 1668 */       throw new IllegalArgumentException("Invalid Control: TTL=" + l1);
/*      */     }
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static final void clearCache()
/*      */   {
/* 1681 */     clearCache(getLoader(Reflection.getCallerClass()));
/*      */   }
/*      */ 
/*      */   public static final void clearCache(ClassLoader paramClassLoader)
/*      */   {
/* 1694 */     if (paramClassLoader == null) {
/* 1695 */       throw new NullPointerException();
/*      */     }
/* 1697 */     Set localSet = cacheList.keySet();
/* 1698 */     for (CacheKey localCacheKey : localSet)
/* 1699 */       if (localCacheKey.getLoader() == paramClassLoader)
/* 1700 */         localSet.remove(localCacheKey);
/*      */   }
/*      */ 
/*      */   protected abstract Object handleGetObject(String paramString);
/*      */ 
/*      */   public abstract Enumeration<String> getKeys();
/*      */ 
/*      */   public boolean containsKey(String paramString)
/*      */   {
/* 1738 */     if (paramString == null) {
/* 1739 */       throw new NullPointerException();
/*      */     }
/* 1741 */     for (ResourceBundle localResourceBundle = this; localResourceBundle != null; localResourceBundle = localResourceBundle.parent) {
/* 1742 */       if (localResourceBundle.handleKeySet().contains(paramString)) {
/* 1743 */         return true;
/*      */       }
/*      */     }
/* 1746 */     return false;
/*      */   }
/*      */ 
/*      */   public Set<String> keySet()
/*      */   {
/* 1758 */     HashSet localHashSet = new HashSet();
/* 1759 */     for (ResourceBundle localResourceBundle = this; localResourceBundle != null; localResourceBundle = localResourceBundle.parent) {
/* 1760 */       localHashSet.addAll(localResourceBundle.handleKeySet());
/*      */     }
/* 1762 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   protected Set<String> handleKeySet()
/*      */   {
/* 1783 */     if (this.keySet == null) {
/* 1784 */       synchronized (this) {
/* 1785 */         if (this.keySet == null) {
/* 1786 */           HashSet localHashSet = new HashSet();
/* 1787 */           Enumeration localEnumeration = getKeys();
/* 1788 */           while (localEnumeration.hasMoreElements()) {
/* 1789 */             String str = (String)localEnumeration.nextElement();
/* 1790 */             if (handleGetObject(str) != null) {
/* 1791 */               localHashSet.add(str);
/*      */             }
/*      */           }
/* 1794 */           this.keySet = localHashSet;
/*      */         }
/*      */       }
/*      */     }
/* 1798 */     return this.keySet;
/*      */   }
/*      */ 
/*      */   private static final class BundleReference extends SoftReference<ResourceBundle>
/*      */     implements ResourceBundle.CacheKeyReference
/*      */   {
/*      */     private ResourceBundle.CacheKey cacheKey;
/*      */ 
/*      */     BundleReference(ResourceBundle paramResourceBundle, ReferenceQueue paramReferenceQueue, ResourceBundle.CacheKey paramCacheKey)
/*      */     {
/*  691 */       super(paramReferenceQueue);
/*  692 */       this.cacheKey = paramCacheKey;
/*      */     }
/*      */ 
/*      */     public ResourceBundle.CacheKey getCacheKey() {
/*  696 */       return this.cacheKey;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class CacheKey
/*      */     implements Cloneable
/*      */   {
/*      */     private String name;
/*      */     private Locale locale;
/*      */     private ResourceBundle.LoaderReference loaderRef;
/*      */     private String format;
/*      */     private volatile long loadTime;
/*      */     private volatile long expirationTime;
/*      */     private Throwable cause;
/*      */     private int hashCodeCache;
/*      */ 
/*      */     CacheKey(String paramString, Locale paramLocale, ClassLoader paramClassLoader)
/*      */     {
/*  515 */       this.name = paramString;
/*  516 */       this.locale = paramLocale;
/*  517 */       if (paramClassLoader == null)
/*  518 */         this.loaderRef = null;
/*      */       else {
/*  520 */         this.loaderRef = new ResourceBundle.LoaderReference(paramClassLoader, ResourceBundle.referenceQueue, this);
/*      */       }
/*  522 */       calculateHashCode();
/*      */     }
/*      */ 
/*      */     String getName() {
/*  526 */       return this.name;
/*      */     }
/*      */ 
/*      */     CacheKey setName(String paramString) {
/*  530 */       if (!this.name.equals(paramString)) {
/*  531 */         this.name = paramString;
/*  532 */         calculateHashCode();
/*      */       }
/*  534 */       return this;
/*      */     }
/*      */ 
/*      */     Locale getLocale() {
/*  538 */       return this.locale;
/*      */     }
/*      */ 
/*      */     CacheKey setLocale(Locale paramLocale) {
/*  542 */       if (!this.locale.equals(paramLocale)) {
/*  543 */         this.locale = paramLocale;
/*  544 */         calculateHashCode();
/*      */       }
/*  546 */       return this;
/*      */     }
/*      */ 
/*      */     ClassLoader getLoader() {
/*  550 */       return this.loaderRef != null ? (ClassLoader)this.loaderRef.get() : null;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  554 */       if (this == paramObject)
/*  555 */         return true;
/*      */       try
/*      */       {
/*  558 */         CacheKey localCacheKey = (CacheKey)paramObject;
/*      */ 
/*  560 */         if (this.hashCodeCache != localCacheKey.hashCodeCache) {
/*  561 */           return false;
/*      */         }
/*      */ 
/*  564 */         if (!this.name.equals(localCacheKey.name)) {
/*  565 */           return false;
/*      */         }
/*      */ 
/*  568 */         if (!this.locale.equals(localCacheKey.locale)) {
/*  569 */           return false;
/*      */         }
/*      */ 
/*  572 */         if (this.loaderRef == null) {
/*  573 */           return localCacheKey.loaderRef == null;
/*      */         }
/*  575 */         ClassLoader localClassLoader = (ClassLoader)this.loaderRef.get();
/*  576 */         return (localCacheKey.loaderRef != null) && (localClassLoader != null) && (localClassLoader == localCacheKey.loaderRef.get());
/*      */       }
/*      */       catch (NullPointerException localNullPointerException)
/*      */       {
/*      */       }
/*      */       catch (ClassCastException localClassCastException)
/*      */       {
/*      */       }
/*      */ 
/*  585 */       return false;
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/*  589 */       return this.hashCodeCache;
/*      */     }
/*      */ 
/*      */     private void calculateHashCode() {
/*  593 */       this.hashCodeCache = (this.name.hashCode() << 3);
/*  594 */       this.hashCodeCache ^= this.locale.hashCode();
/*  595 */       ClassLoader localClassLoader = getLoader();
/*  596 */       if (localClassLoader != null)
/*  597 */         this.hashCodeCache ^= localClassLoader.hashCode();
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */     {
/*      */       try {
/*  603 */         CacheKey localCacheKey = (CacheKey)super.clone();
/*  604 */         if (this.loaderRef != null) {
/*  605 */           localCacheKey.loaderRef = new ResourceBundle.LoaderReference((ClassLoader)this.loaderRef.get(), ResourceBundle.referenceQueue, localCacheKey);
/*      */         }
/*      */ 
/*  609 */         localCacheKey.cause = null;
/*  610 */         return localCacheKey;
/*      */       } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */       }
/*  613 */       throw new InternalError();
/*      */     }
/*      */ 
/*      */     String getFormat()
/*      */     {
/*  618 */       return this.format;
/*      */     }
/*      */ 
/*      */     void setFormat(String paramString) {
/*  622 */       this.format = paramString;
/*      */     }
/*      */ 
/*      */     private void setCause(Throwable paramThrowable) {
/*  626 */       if (this.cause == null) {
/*  627 */         this.cause = paramThrowable;
/*      */       }
/*  631 */       else if ((this.cause instanceof ClassNotFoundException))
/*  632 */         this.cause = paramThrowable;
/*      */     }
/*      */ 
/*      */     private Throwable getCause()
/*      */     {
/*  638 */       return this.cause;
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  642 */       String str = this.locale.toString();
/*  643 */       if (str.length() == 0) {
/*  644 */         if (this.locale.getVariant().length() != 0)
/*  645 */           str = "__" + this.locale.getVariant();
/*      */         else {
/*  647 */           str = "\"\"";
/*      */         }
/*      */       }
/*  650 */       return "CacheKey[" + this.name + ", lc=" + str + ", ldr=" + getLoader() + "(format=" + this.format + ")]";
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract interface CacheKeyReference
/*      */   {
/*      */     public abstract ResourceBundle.CacheKey getCacheKey();
/*      */   }
/*      */ 
/*      */   public static class Control
/*      */   {
/* 1959 */     public static final List<String> FORMAT_DEFAULT = Collections.unmodifiableList(Arrays.asList(new String[] { "java.class", "java.properties" }));
/*      */ 
/* 1970 */     public static final List<String> FORMAT_CLASS = Collections.unmodifiableList(Arrays.asList(new String[] { "java.class" }));
/*      */ 
/* 1980 */     public static final List<String> FORMAT_PROPERTIES = Collections.unmodifiableList(Arrays.asList(new String[] { "java.properties" }));
/*      */     public static final long TTL_DONT_CACHE = -1L;
/*      */     public static final long TTL_NO_EXPIRATION_CONTROL = -2L;
/* 1999 */     private static final Control INSTANCE = new Control();
/*      */ 
/* 2307 */     private static final CandidateListCache CANDIDATES_CACHE = new CandidateListCache(null);
/*      */ 
/*      */     public static final Control getControl(List<String> paramList)
/*      */     {
/* 2032 */       if (paramList.equals(FORMAT_PROPERTIES)) {
/* 2033 */         return ResourceBundle.SingleFormatControl.access$800();
/*      */       }
/* 2035 */       if (paramList.equals(FORMAT_CLASS)) {
/* 2036 */         return ResourceBundle.SingleFormatControl.access$900();
/*      */       }
/* 2038 */       if (paramList.equals(FORMAT_DEFAULT)) {
/* 2039 */         return INSTANCE;
/*      */       }
/* 2041 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*      */     public static final Control getNoFallbackControl(List<String> paramList)
/*      */     {
/* 2067 */       if (paramList.equals(FORMAT_DEFAULT)) {
/* 2068 */         return ResourceBundle.NoFallbackControl.access$1000();
/*      */       }
/* 2070 */       if (paramList.equals(FORMAT_PROPERTIES)) {
/* 2071 */         return ResourceBundle.NoFallbackControl.access$1100();
/*      */       }
/* 2073 */       if (paramList.equals(FORMAT_CLASS)) {
/* 2074 */         return ResourceBundle.NoFallbackControl.access$1200();
/*      */       }
/* 2076 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*      */     public List<String> getFormats(String paramString)
/*      */     {
/* 2114 */       if (paramString == null) {
/* 2115 */         throw new NullPointerException();
/*      */       }
/* 2117 */       return FORMAT_DEFAULT;
/*      */     }
/*      */ 
/*      */     public List<Locale> getCandidateLocales(String paramString, Locale paramLocale)
/*      */     {
/* 2301 */       if (paramString == null) {
/* 2302 */         throw new NullPointerException();
/*      */       }
/* 2304 */       return new ArrayList((Collection)CANDIDATES_CACHE.get(paramLocale.getBaseLocale()));
/*      */     }
/*      */ 
/*      */     public Locale getFallbackLocale(String paramString, Locale paramLocale)
/*      */     {
/* 2456 */       if (paramString == null) {
/* 2457 */         throw new NullPointerException();
/*      */       }
/* 2459 */       Locale localLocale = Locale.getDefault();
/* 2460 */       return paramLocale.equals(localLocale) ? null : localLocale;
/*      */     }
/*      */ 
/*      */     public ResourceBundle newBundle(String paramString1, Locale paramLocale, String paramString2, ClassLoader paramClassLoader, boolean paramBoolean)
/*      */       throws IllegalAccessException, InstantiationException, IOException
/*      */     {
/* 2562 */       String str1 = toBundleName(paramString1, paramLocale);
/* 2563 */       Object localObject1 = null;
/* 2564 */       if (paramString2.equals("java.class")) {
/*      */         try {
/* 2566 */           Class localClass = paramClassLoader.loadClass(str1);
/*      */ 
/* 2571 */           if (ResourceBundle.class.isAssignableFrom(localClass))
/* 2572 */             localObject1 = (ResourceBundle)localClass.newInstance();
/*      */           else
/* 2574 */             throw new ClassCastException(localClass.getName() + " cannot be cast to ResourceBundle");
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException) {
/*      */         }
/*      */       }
/* 2579 */       else if (paramString2.equals("java.properties")) {
/* 2580 */         final String str2 = toResourceName0(str1, "properties");
/* 2581 */         if (str2 == null) {
/* 2582 */           return localObject1;
/*      */         }
/* 2584 */         final ClassLoader localClassLoader = paramClassLoader;
/* 2585 */         final boolean bool = paramBoolean;
/* 2586 */         InputStream localInputStream = null;
/*      */         try {
/* 2588 */           localInputStream = (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public InputStream run() throws IOException {
/* 2591 */               InputStream localInputStream = null;
/* 2592 */               if (bool) {
/* 2593 */                 URL localURL = localClassLoader.getResource(str2);
/* 2594 */                 if (localURL != null) {
/* 2595 */                   URLConnection localURLConnection = localURL.openConnection();
/* 2596 */                   if (localURLConnection != null)
/*      */                   {
/* 2599 */                     localURLConnection.setUseCaches(false);
/* 2600 */                     localInputStream = localURLConnection.getInputStream();
/*      */                   }
/*      */                 }
/*      */               } else {
/* 2604 */                 localInputStream = localClassLoader.getResourceAsStream(str2);
/*      */               }
/* 2606 */               return localInputStream;
/*      */             } } );
/*      */         }
/*      */         catch (PrivilegedActionException localPrivilegedActionException) {
/* 2610 */           throw ((IOException)localPrivilegedActionException.getException());
/*      */         }
/* 2612 */         if (localInputStream != null)
/*      */           try {
/* 2614 */             localObject1 = new PropertyResourceBundle(localInputStream);
/*      */           } finally {
/* 2616 */             localInputStream.close();
/*      */           }
/*      */       }
/*      */       else {
/* 2620 */         throw new IllegalArgumentException("unknown format: " + paramString2);
/*      */       }
/* 2622 */       return localObject1;
/*      */     }
/*      */ 
/*      */     public long getTimeToLive(String paramString, Locale paramLocale)
/*      */     {
/* 2672 */       if ((paramString == null) || (paramLocale == null)) {
/* 2673 */         throw new NullPointerException();
/*      */       }
/* 2675 */       return -2L;
/*      */     }
/*      */ 
/*      */     public boolean needsReload(String paramString1, Locale paramLocale, String paramString2, ClassLoader paramClassLoader, ResourceBundle paramResourceBundle, long paramLong)
/*      */     {
/* 2729 */       if (paramResourceBundle == null) {
/* 2730 */         throw new NullPointerException();
/*      */       }
/* 2732 */       if ((paramString2.equals("java.class")) || (paramString2.equals("java.properties"))) {
/* 2733 */         paramString2 = paramString2.substring(5);
/*      */       }
/* 2735 */       boolean bool = false;
/*      */       try {
/* 2737 */         String str = toResourceName0(toBundleName(paramString1, paramLocale), paramString2);
/* 2738 */         if (str == null) {
/* 2739 */           return bool;
/*      */         }
/* 2741 */         URL localURL = paramClassLoader.getResource(str);
/* 2742 */         if (localURL != null) {
/* 2743 */           long l = 0L;
/* 2744 */           URLConnection localURLConnection = localURL.openConnection();
/* 2745 */           if (localURLConnection != null)
/*      */           {
/* 2747 */             localURLConnection.setUseCaches(false);
/* 2748 */             if ((localURLConnection instanceof JarURLConnection)) {
/* 2749 */               JarEntry localJarEntry = ((JarURLConnection)localURLConnection).getJarEntry();
/* 2750 */               if (localJarEntry != null) {
/* 2751 */                 l = localJarEntry.getTime();
/* 2752 */                 if (l == -1L)
/* 2753 */                   l = 0L;
/*      */               }
/*      */             }
/*      */             else {
/* 2757 */               l = localURLConnection.getLastModified();
/*      */             }
/*      */           }
/* 2760 */           bool = l >= paramLong;
/*      */         }
/*      */       } catch (NullPointerException localNullPointerException) {
/* 2763 */         throw localNullPointerException;
/*      */       }
/*      */       catch (Exception localException) {
/*      */       }
/* 2767 */       return bool;
/*      */     }
/*      */ 
/*      */     public String toBundleName(String paramString, Locale paramLocale)
/*      */     {
/* 2813 */       if (paramLocale == Locale.ROOT) {
/* 2814 */         return paramString;
/*      */       }
/*      */ 
/* 2817 */       String str1 = paramLocale.getLanguage();
/* 2818 */       String str2 = paramLocale.getScript();
/* 2819 */       String str3 = paramLocale.getCountry();
/* 2820 */       String str4 = paramLocale.getVariant();
/*      */ 
/* 2822 */       if ((str1 == "") && (str3 == "") && (str4 == "")) {
/* 2823 */         return paramString;
/*      */       }
/*      */ 
/* 2826 */       StringBuilder localStringBuilder = new StringBuilder(paramString);
/* 2827 */       localStringBuilder.append('_');
/* 2828 */       if (str2 != "") {
/* 2829 */         if (str4 != "")
/* 2830 */           localStringBuilder.append(str1).append('_').append(str2).append('_').append(str3).append('_').append(str4);
/* 2831 */         else if (str3 != "")
/* 2832 */           localStringBuilder.append(str1).append('_').append(str2).append('_').append(str3);
/*      */         else {
/* 2834 */           localStringBuilder.append(str1).append('_').append(str2);
/*      */         }
/*      */       }
/* 2837 */       else if (str4 != "")
/* 2838 */         localStringBuilder.append(str1).append('_').append(str3).append('_').append(str4);
/* 2839 */       else if (str3 != "")
/* 2840 */         localStringBuilder.append(str1).append('_').append(str3);
/*      */       else {
/* 2842 */         localStringBuilder.append(str1);
/*      */       }
/*      */ 
/* 2845 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     public final String toResourceName(String paramString1, String paramString2)
/*      */     {
/* 2870 */       StringBuilder localStringBuilder = new StringBuilder(paramString1.length() + 1 + paramString2.length());
/* 2871 */       localStringBuilder.append(paramString1.replace('.', '/')).append('.').append(paramString2);
/* 2872 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     private String toResourceName0(String paramString1, String paramString2)
/*      */     {
/* 2877 */       if (paramString1.contains("://")) {
/* 2878 */         return null;
/*      */       }
/* 2880 */       return toResourceName(paramString1, paramString2);
/*      */     }
/*      */ 
/*      */     private static class CandidateListCache extends LocaleObjectCache<BaseLocale, List<Locale>>
/*      */     {
/*      */       protected List<Locale> createObject(BaseLocale paramBaseLocale)
/*      */       {
/* 2311 */         String str1 = paramBaseLocale.getLanguage();
/* 2312 */         String str2 = paramBaseLocale.getScript();
/* 2313 */         String str3 = paramBaseLocale.getRegion();
/* 2314 */         String str4 = paramBaseLocale.getVariant();
/*      */ 
/* 2317 */         int i = 0;
/* 2318 */         int j = 0;
/* 2319 */         if (str1.equals("no"))
/* 2320 */           if ((str3.equals("NO")) && (str4.equals("NY"))) {
/* 2321 */             str4 = "";
/* 2322 */             j = 1;
/*      */           } else {
/* 2324 */             i = 1;
/*      */           }
/*      */         List localList;
/* 2327 */         if ((str1.equals("nb")) || (i != 0)) {
/* 2328 */           localList = getDefaultList("nb", str2, str3, str4);
/*      */ 
/* 2330 */           LinkedList localLinkedList = new LinkedList();
/* 2331 */           for (Locale localLocale : localList) {
/* 2332 */             localLinkedList.add(localLocale);
/* 2333 */             if (localLocale.getLanguage().length() == 0) {
/*      */               break;
/*      */             }
/* 2336 */             localLinkedList.add(Locale.getInstance("no", localLocale.getScript(), localLocale.getCountry(), localLocale.getVariant(), null));
/*      */           }
/*      */ 
/* 2339 */           return localLinkedList;
/* 2340 */         }if ((str1.equals("nn")) || (j != 0))
/*      */         {
/* 2342 */           localList = getDefaultList("nn", str2, str3, str4);
/* 2343 */           int k = localList.size() - 1;
/* 2344 */           localList.add(k++, Locale.getInstance("no", "NO", "NY"));
/* 2345 */           localList.add(k++, Locale.getInstance("no", "NO", ""));
/* 2346 */           localList.add(k++, Locale.getInstance("no", "", ""));
/* 2347 */           return localList;
/*      */         }
/*      */ 
/* 2350 */         if (str1.equals("zh")) {
/* 2351 */           if ((str2.length() == 0) && (str3.length() > 0))
/*      */           {
/* 2354 */             if ((str3.equals("TW")) || (str3.equals("HK")) || (str3.equals("MO")))
/* 2355 */               str2 = "Hant";
/* 2356 */             else if ((str3.equals("CN")) || (str3.equals("SG")))
/* 2357 */               str2 = "Hans";
/*      */           }
/* 2359 */           else if ((str2.length() > 0) && (str3.length() == 0))
/*      */           {
/* 2362 */             if (str2.equals("Hans"))
/* 2363 */               str3 = "CN";
/* 2364 */             else if (str2.equals("Hant")) {
/* 2365 */               str3 = "TW";
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 2370 */         return getDefaultList(str1, str2, str3, str4);
/*      */       }
/*      */ 
/*      */       private static List<Locale> getDefaultList(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 2374 */         LinkedList localLinkedList1 = null;
/*      */ 
/* 2376 */         if (paramString4.length() > 0) {
/* 2377 */           localLinkedList1 = new LinkedList();
/* 2378 */           int i = paramString4.length();
/* 2379 */           while (i != -1) {
/* 2380 */             localLinkedList1.add(paramString4.substring(0, i));
/* 2381 */             i = paramString4.lastIndexOf('_', --i);
/*      */           }
/*      */         }
/*      */ 
/* 2385 */         LinkedList localLinkedList2 = new LinkedList();
/*      */         Iterator localIterator;
/* 2387 */         if (localLinkedList1 != null)
/* 2388 */           for (localIterator = localLinkedList1.iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/* 2389 */             localLinkedList2.add(Locale.getInstance(paramString1, paramString2, paramString3, str, null));
/*      */           }
/*      */         String str;
/* 2392 */         if (paramString3.length() > 0) {
/* 2393 */           localLinkedList2.add(Locale.getInstance(paramString1, paramString2, paramString3, "", null));
/*      */         }
/* 2395 */         if (paramString2.length() > 0) {
/* 2396 */           localLinkedList2.add(Locale.getInstance(paramString1, paramString2, "", "", null));
/*      */ 
/* 2400 */           if (localLinkedList1 != null) {
/* 2401 */             for (localIterator = localLinkedList1.iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/* 2402 */               localLinkedList2.add(Locale.getInstance(paramString1, "", paramString3, str, null));
/*      */             }
/*      */           }
/* 2405 */           if (paramString3.length() > 0) {
/* 2406 */             localLinkedList2.add(Locale.getInstance(paramString1, "", paramString3, "", null));
/*      */           }
/*      */         }
/* 2409 */         if (paramString1.length() > 0) {
/* 2410 */           localLinkedList2.add(Locale.getInstance(paramString1, "", "", "", null));
/*      */         }
/*      */ 
/* 2413 */         localLinkedList2.add(Locale.ROOT);
/*      */ 
/* 2415 */         return localLinkedList2;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class LoaderReference extends WeakReference<ClassLoader>
/*      */     implements ResourceBundle.CacheKeyReference
/*      */   {
/*      */     private ResourceBundle.CacheKey cacheKey;
/*      */ 
/*      */     LoaderReference(ClassLoader paramClassLoader, ReferenceQueue paramReferenceQueue, ResourceBundle.CacheKey paramCacheKey)
/*      */     {
/*  673 */       super(paramReferenceQueue);
/*  674 */       this.cacheKey = paramCacheKey;
/*      */     }
/*      */ 
/*      */     public ResourceBundle.CacheKey getCacheKey() {
/*  678 */       return this.cacheKey;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class NoFallbackControl extends ResourceBundle.SingleFormatControl
/*      */   {
/* 2907 */     private static final ResourceBundle.Control NO_FALLBACK = new NoFallbackControl(FORMAT_DEFAULT);
/*      */ 
/* 2910 */     private static final ResourceBundle.Control PROPERTIES_ONLY_NO_FALLBACK = new NoFallbackControl(FORMAT_PROPERTIES);
/*      */ 
/* 2913 */     private static final ResourceBundle.Control CLASS_ONLY_NO_FALLBACK = new NoFallbackControl(FORMAT_CLASS);
/*      */ 
/*      */     protected NoFallbackControl(List<String> paramList)
/*      */     {
/* 2917 */       super();
/*      */     }
/*      */ 
/*      */     public Locale getFallbackLocale(String paramString, Locale paramLocale) {
/* 2921 */       if ((paramString == null) || (paramLocale == null)) {
/* 2922 */         throw new NullPointerException();
/*      */       }
/* 2924 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class RBClassLoader extends ClassLoader
/*      */   {
/*  438 */     private static final RBClassLoader INSTANCE = (RBClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public ResourceBundle.RBClassLoader run() {
/*  441 */         return new ResourceBundle.RBClassLoader(null);
/*      */       }
/*      */     });
/*      */ 
/*  444 */     private static final ClassLoader loader = ClassLoader.getSystemClassLoader();
/*      */ 
/*      */     public Class<?> loadClass(String paramString)
/*      */       throws ClassNotFoundException
/*      */     {
/*  449 */       if (loader != null) {
/*  450 */         return loader.loadClass(paramString);
/*      */       }
/*  452 */       return Class.forName(paramString);
/*      */     }
/*      */     public URL getResource(String paramString) {
/*  455 */       if (loader != null) {
/*  456 */         return loader.getResource(paramString);
/*      */       }
/*  458 */       return ClassLoader.getSystemResource(paramString);
/*      */     }
/*      */     public InputStream getResourceAsStream(String paramString) {
/*  461 */       if (loader != null) {
/*  462 */         return loader.getResourceAsStream(paramString);
/*      */       }
/*  464 */       return ClassLoader.getSystemResourceAsStream(paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SingleFormatControl extends ResourceBundle.Control
/*      */   {
/* 2886 */     private static final ResourceBundle.Control PROPERTIES_ONLY = new SingleFormatControl(FORMAT_PROPERTIES);
/*      */ 
/* 2889 */     private static final ResourceBundle.Control CLASS_ONLY = new SingleFormatControl(FORMAT_CLASS);
/*      */     private final List<String> formats;
/*      */ 
/*      */     protected SingleFormatControl(List<String> paramList)
/*      */     {
/* 2895 */       this.formats = paramList;
/*      */     }
/*      */ 
/*      */     public List<String> getFormats(String paramString) {
/* 2899 */       if (paramString == null) {
/* 2900 */         throw new NullPointerException();
/*      */       }
/* 2902 */       return this.formats;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.ResourceBundle
 * JD-Core Version:    0.6.2
 */