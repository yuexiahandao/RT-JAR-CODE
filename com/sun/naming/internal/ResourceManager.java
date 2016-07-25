/*     */ package com.sun.naming.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.naming.ConfigurationException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public final class ResourceManager
/*     */ {
/*     */   private static final String PROVIDER_RESOURCE_FILE_NAME = "jndiprovider.properties";
/*     */   private static final String APP_RESOURCE_FILE_NAME = "jndi.properties";
/*     */   private static final String JRELIB_PROPERTY_FILE_NAME = "jndi.properties";
/*     */   private static final String DISABLE_APP_RESOURCE_FILES = "com.sun.naming.disable.app.resource.files";
/*  82 */   private static final String[] listProperties = { "java.naming.factory.object", "java.naming.factory.url.pkgs", "java.naming.factory.state", "java.naming.factory.control" };
/*     */ 
/*  90 */   private static final VersionHelper helper = VersionHelper.getVersionHelper();
/*     */ 
/* 100 */   private static final WeakHashMap propertiesCache = new WeakHashMap(11);
/*     */ 
/* 110 */   private static final WeakHashMap factoryCache = new WeakHashMap(11);
/*     */ 
/* 121 */   private static final WeakHashMap urlFactoryCache = new WeakHashMap(11);
/* 122 */   private static final WeakReference NO_FACTORY = new WeakReference(null);
/*     */ 
/*     */   public static Hashtable getInitialEnvironment(Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 197 */     String[] arrayOfString1 = VersionHelper.PROPS;
/* 198 */     if (paramHashtable == null) {
/* 199 */       paramHashtable = new Hashtable(11);
/*     */     }
/* 201 */     Object localObject1 = paramHashtable.get("java.naming.applet");
/*     */ 
/* 210 */     String[] arrayOfString2 = helper.getJndiProperties();
/* 211 */     for (int i = 0; i < arrayOfString1.length; i++) {
/* 212 */       Object localObject2 = paramHashtable.get(arrayOfString1[i]);
/* 213 */       if (localObject2 == null) {
/* 214 */         if (localObject1 != null) {
/* 215 */           localObject2 = AppletParameter.get(localObject1, arrayOfString1[i]);
/*     */         }
/* 217 */         if (localObject2 == null)
/*     */         {
/* 219 */           localObject2 = arrayOfString2 != null ? arrayOfString2[i] : helper.getJndiProperty(i);
/*     */         }
/*     */ 
/* 223 */         if (localObject2 != null) {
/* 224 */           paramHashtable.put(arrayOfString1[i], localObject2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 231 */     String str = (String)paramHashtable.get("com.sun.naming.disable.app.resource.files");
/* 232 */     if ((str != null) && (str.equalsIgnoreCase("true"))) {
/* 233 */       return paramHashtable;
/*     */     }
/*     */ 
/* 238 */     mergeTables(paramHashtable, getApplicationResources());
/* 239 */     return paramHashtable;
/*     */   }
/*     */ 
/*     */   public static String getProperty(String paramString, Hashtable paramHashtable, Context paramContext, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 266 */     String str1 = paramHashtable != null ? (String)paramHashtable.get(paramString) : null;
/* 267 */     if ((paramContext == null) || ((str1 != null) && (!paramBoolean)))
/*     */     {
/* 269 */       return str1;
/*     */     }
/* 271 */     String str2 = (String)getProviderResource(paramContext).get(paramString);
/* 272 */     if (str1 == null)
/* 273 */       return str2;
/* 274 */     if ((str2 == null) || (!paramBoolean)) {
/* 275 */       return str1;
/*     */     }
/* 277 */     return str1 + ":" + str2;
/*     */   }
/*     */ 
/*     */   public static FactoryEnumeration getFactories(String paramString, Hashtable paramHashtable, Context paramContext)
/*     */     throws NamingException
/*     */   {
/* 326 */     String str1 = getProperty(paramString, paramHashtable, paramContext, true);
/* 327 */     if (str1 == null) {
/* 328 */       return null;
/*     */     }
/*     */ 
/* 331 */     ClassLoader localClassLoader = helper.getContextClassLoader();
/*     */ 
/* 333 */     Object localObject1 = null;
/* 334 */     synchronized (factoryCache) {
/* 335 */       localObject1 = (Map)factoryCache.get(localClassLoader);
/* 336 */       if (localObject1 == null) {
/* 337 */         localObject1 = new HashMap(11);
/* 338 */         factoryCache.put(localClassLoader, localObject1);
/*     */       }
/*     */     }
/*     */ 
/* 342 */     synchronized (localObject1) {
/* 343 */       Object localObject3 = (List)((Map)localObject1).get(str1);
/* 344 */       if (localObject3 != null)
/*     */       {
/* 346 */         return ((List)localObject3).size() == 0 ? null : new FactoryEnumeration((List)localObject3, localClassLoader);
/*     */       }
/*     */ 
/* 351 */       StringTokenizer localStringTokenizer = new StringTokenizer(str1, ":");
/* 352 */       localObject3 = new ArrayList(5);
/* 353 */       while (localStringTokenizer.hasMoreTokens()) {
/*     */         try
/*     */         {
/* 356 */           String str2 = localStringTokenizer.nextToken();
/* 357 */           Class localClass = helper.loadClass(str2, localClassLoader);
/* 358 */           ((List)localObject3).add(new NamedWeakReference(localClass, str2));
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */       }
/* 364 */       ((Map)localObject1).put(str1, localObject3);
/* 365 */       return new FactoryEnumeration((List)localObject3, localClassLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object getFactory(String paramString1, Hashtable paramHashtable, Context paramContext, String paramString2, String paramString3)
/*     */     throws NamingException
/*     */   {
/* 410 */     String str1 = getProperty(paramString1, paramHashtable, paramContext, true);
/* 411 */     if (str1 != null)
/* 412 */       str1 = str1 + ":" + paramString3;
/*     */     else {
/* 414 */       str1 = paramString3;
/*     */     }
/*     */ 
/* 418 */     ClassLoader localClassLoader = helper.getContextClassLoader();
/* 419 */     String str2 = paramString2 + " " + str1;
/*     */ 
/* 421 */     Object localObject1 = null;
/* 422 */     synchronized (urlFactoryCache) {
/* 423 */       localObject1 = (Map)urlFactoryCache.get(localClassLoader);
/* 424 */       if (localObject1 == null) {
/* 425 */         localObject1 = new HashMap(11);
/* 426 */         urlFactoryCache.put(localClassLoader, localObject1);
/*     */       }
/*     */     }
/*     */ 
/* 430 */     synchronized (localObject1) {
/* 431 */       Object localObject3 = null;
/*     */ 
/* 433 */       WeakReference localWeakReference = (WeakReference)((Map)localObject1).get(str2);
/* 434 */       if (localWeakReference == NO_FACTORY)
/* 435 */         return null;
/* 436 */       if (localWeakReference != null) {
/* 437 */         localObject3 = localWeakReference.get();
/* 438 */         if (localObject3 != null) {
/* 439 */           return localObject3;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 444 */       StringTokenizer localStringTokenizer = new StringTokenizer(str1, ":");
/*     */ 
/* 446 */       while ((localObject3 == null) && (localStringTokenizer.hasMoreTokens())) {
/* 447 */         String str3 = localStringTokenizer.nextToken() + paramString2;
/*     */         try
/*     */         {
/* 450 */           localObject3 = helper.loadClass(str3, localClassLoader).newInstance();
/*     */         } catch (InstantiationException localInstantiationException) {
/* 452 */           localNamingException = new NamingException("Cannot instantiate " + str3);
/*     */ 
/* 454 */           localNamingException.setRootCause(localInstantiationException);
/* 455 */           throw localNamingException;
/*     */         } catch (IllegalAccessException localIllegalAccessException) {
/* 457 */           NamingException localNamingException = new NamingException("Cannot access " + str3);
/*     */ 
/* 459 */           localNamingException.setRootCause(localIllegalAccessException);
/* 460 */           throw localNamingException;
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 468 */       ((Map)localObject1).put(str2, localObject3 != null ? new WeakReference(localObject3) : NO_FACTORY);
/*     */ 
/* 471 */       return localObject3;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Hashtable getProviderResource(Object paramObject)
/*     */     throws NamingException
/*     */   {
/* 489 */     if (paramObject == null) {
/* 490 */       return new Hashtable(1);
/*     */     }
/* 492 */     synchronized (propertiesCache) {
/* 493 */       Class localClass = paramObject.getClass();
/*     */ 
/* 495 */       Object localObject1 = (Hashtable)propertiesCache.get(localClass);
/* 496 */       if (localObject1 != null) {
/* 497 */         return localObject1;
/*     */       }
/* 499 */       localObject1 = new Properties();
/*     */ 
/* 501 */       InputStream localInputStream = helper.getResourceAsStream(localClass, "jndiprovider.properties");
/*     */ 
/* 504 */       if (localInputStream != null) {
/*     */         try {
/* 506 */           ((Properties)localObject1).load(localInputStream);
/*     */         } catch (IOException localIOException) {
/* 508 */           ConfigurationException localConfigurationException = new ConfigurationException("Error reading provider resource file for " + localClass);
/*     */ 
/* 510 */           localConfigurationException.setRootCause(localIOException);
/* 511 */           throw localConfigurationException;
/*     */         }
/*     */       }
/* 514 */       propertiesCache.put(localClass, localObject1);
/* 515 */       return localObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Hashtable getApplicationResources()
/*     */     throws NamingException
/*     */   {
/* 538 */     ClassLoader localClassLoader = helper.getContextClassLoader();
/*     */ 
/* 540 */     synchronized (propertiesCache) {
/* 541 */       Object localObject1 = (Hashtable)propertiesCache.get(localClassLoader);
/* 542 */       if (localObject1 != null) {
/* 543 */         return localObject1;
/*     */       }
/*     */       try
/*     */       {
/* 547 */         NamingEnumeration localNamingEnumeration = helper.getResources(localClassLoader, "jndi.properties");
/*     */ 
/* 549 */         while (localNamingEnumeration.hasMore()) {
/* 550 */           localObject2 = new Properties();
/* 551 */           ((Properties)localObject2).load((InputStream)localNamingEnumeration.next());
/*     */ 
/* 553 */           if (localObject1 == null)
/* 554 */             localObject1 = localObject2;
/*     */           else {
/* 556 */             mergeTables((Hashtable)localObject1, (Hashtable)localObject2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 561 */         localObject2 = helper.getJavaHomeLibStream("jndi.properties");
/*     */ 
/* 563 */         if (localObject2 != null) {
/* 564 */           Properties localProperties = new Properties();
/* 565 */           localProperties.load((InputStream)localObject2);
/*     */ 
/* 567 */           if (localObject1 == null)
/* 568 */             localObject1 = localProperties;
/*     */           else
/* 570 */             mergeTables((Hashtable)localObject1, localProperties);
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 575 */         Object localObject2 = new ConfigurationException("Error reading application resource file");
/*     */ 
/* 577 */         ((NamingException)localObject2).setRootCause(localIOException);
/* 578 */         throw ((Throwable)localObject2);
/*     */       }
/* 580 */       if (localObject1 == null) {
/* 581 */         localObject1 = new Hashtable(11);
/*     */       }
/* 583 */       propertiesCache.put(localClassLoader, localObject1);
/* 584 */       return localObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void mergeTables(Hashtable paramHashtable1, Hashtable paramHashtable2)
/*     */   {
/* 596 */     Enumeration localEnumeration = paramHashtable2.keys();
/*     */ 
/* 598 */     while (localEnumeration.hasMoreElements()) {
/* 599 */       String str1 = (String)localEnumeration.nextElement();
/* 600 */       Object localObject = paramHashtable1.get(str1);
/* 601 */       if (localObject == null) {
/* 602 */         paramHashtable1.put(str1, paramHashtable2.get(str1));
/* 603 */       } else if (isListProperty(str1)) {
/* 604 */         String str2 = (String)paramHashtable2.get(str1);
/* 605 */         paramHashtable1.put(str1, (String)localObject + ":" + str2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isListProperty(String paramString)
/*     */   {
/* 615 */     paramString = paramString.intern();
/* 616 */     for (int i = 0; i < listProperties.length; i++) {
/* 617 */       if (paramString == listProperties[i]) {
/* 618 */         return true;
/*     */       }
/*     */     }
/* 621 */     return false;
/*     */   }
/*     */ 
/*     */   private static class AppletParameter
/*     */   {
/* 129 */     private static final Class<?> clazz = getClass("java.applet.Applet");
/* 130 */     private static final Method getMethod = getMethod(clazz, "getParameter", new Class[] { String.class });
/*     */ 
/*     */     private static Class<?> getClass(String paramString) {
/*     */       try {
/* 134 */         return Class.forName(paramString, true, null); } catch (ClassNotFoundException localClassNotFoundException) {
/*     */       }
/* 136 */       return null;
/*     */     }
/*     */ 
/*     */     private static Method getMethod(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass)
/*     */     {
/* 143 */       if (paramClass != null) {
/*     */         try {
/* 145 */           return paramClass.getMethod(paramString, paramArrayOfClass);
/*     */         } catch (NoSuchMethodException localNoSuchMethodException) {
/* 147 */           throw new AssertionError(localNoSuchMethodException);
/*     */         }
/*     */       }
/* 150 */       return null;
/*     */     }
/*     */ 
/*     */     static Object get(Object paramObject, String paramString)
/*     */     {
/* 159 */       if ((clazz == null) || (!clazz.isInstance(paramObject)))
/* 160 */         throw new ClassCastException(paramObject.getClass().getName());
/*     */       try {
/* 162 */         return getMethod.invoke(paramObject, new Object[] { paramString });
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 164 */         throw new AssertionError(localInvocationTargetException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 166 */         throw new AssertionError(localIllegalAccessException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.naming.internal.ResourceManager
 * JD-Core Version:    0.6.2
 */