/*     */ package javax.management.remote;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class JMXConnectorFactory
/*     */ {
/*     */   public static final String DEFAULT_CLASS_LOADER = "jmx.remote.default.class.loader";
/*     */   public static final String PROTOCOL_PROVIDER_PACKAGES = "jmx.remote.protocol.provider.pkgs";
/*     */   public static final String PROTOCOL_PROVIDER_CLASS_LOADER = "jmx.remote.protocol.provider.class.loader";
/*     */   private static final String PROTOCOL_PROVIDER_DEFAULT_PACKAGE = "com.sun.jmx.remote.protocol";
/* 197 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "JMXConnectorFactory");
/*     */ 
/*     */   public static JMXConnector connect(JMXServiceURL paramJMXServiceURL)
/*     */     throws IOException
/*     */   {
/* 227 */     return connect(paramJMXServiceURL, null);
/*     */   }
/*     */ 
/*     */   public static JMXConnector connect(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 265 */     if (paramJMXServiceURL == null)
/* 266 */       throw new NullPointerException("Null JMXServiceURL");
/* 267 */     JMXConnector localJMXConnector = newJMXConnector(paramJMXServiceURL, paramMap);
/* 268 */     localJMXConnector.connect(paramMap);
/* 269 */     return localJMXConnector;
/*     */   }
/*     */ 
/*     */   private static <K, V> Map<K, V> newHashMap() {
/* 273 */     return new HashMap();
/*     */   }
/*     */ 
/*     */   private static <K> Map<K, Object> newHashMap(Map<K, ?> paramMap) {
/* 277 */     return new HashMap(paramMap);
/*     */   }
/*     */ 
/*     */   public static JMXConnector newJMXConnector(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/*     */     Map localMap;
/* 314 */     if (paramMap == null) {
/* 315 */       localMap = newHashMap();
/*     */     } else {
/* 317 */       EnvHelp.checkAttributes(paramMap);
/* 318 */       localMap = newHashMap(paramMap);
/*     */     }
/*     */ 
/* 321 */     ClassLoader localClassLoader = resolveClassLoader(localMap);
/* 322 */     JMXConnectorProvider localJMXConnectorProvider1 = JMXConnectorProvider.class;
/*     */ 
/* 324 */     String str = paramJMXServiceURL.getProtocol();
/*     */ 
/* 326 */     JMXServiceURL localJMXServiceURL = paramJMXServiceURL;
/*     */ 
/* 328 */     JMXConnectorProvider localJMXConnectorProvider2 = (JMXConnectorProvider)getProvider(localJMXServiceURL, localMap, "ClientProvider", localJMXConnectorProvider1, localClassLoader);
/*     */ 
/* 333 */     Object localObject1 = null;
/* 334 */     if (localJMXConnectorProvider2 == null)
/*     */     {
/* 339 */       if (localClassLoader != null) {
/*     */         try {
/* 341 */           JMXConnector localJMXConnector = getConnectorAsService(localClassLoader, localJMXServiceURL, localMap);
/*     */ 
/* 343 */           if (localJMXConnector != null)
/* 344 */             return localJMXConnector;
/*     */         } catch (JMXProviderException localJMXProviderException) {
/* 346 */           throw localJMXProviderException;
/*     */         } catch (IOException localIOException) {
/* 348 */           localObject1 = localIOException;
/*     */         }
/*     */       }
/* 351 */       localJMXConnectorProvider2 = (JMXConnectorProvider)getProvider(str, "com.sun.jmx.remote.protocol", JMXConnectorFactory.class.getClassLoader(), "ClientProvider", localJMXConnectorProvider1);
/*     */     }
/*     */ 
/* 356 */     if (localJMXConnectorProvider2 == null) {
/* 357 */       localObject2 = new MalformedURLException("Unsupported protocol: " + str);
/*     */ 
/* 359 */       if (localObject1 == null) {
/* 360 */         throw ((Throwable)localObject2);
/*     */       }
/* 362 */       throw ((MalformedURLException)EnvHelp.initCause((Throwable)localObject2, localObject1));
/*     */     }
/*     */ 
/* 366 */     Object localObject2 = Collections.unmodifiableMap(localMap);
/*     */ 
/* 369 */     return localJMXConnectorProvider2.newJMXConnector(paramJMXServiceURL, (Map)localObject2);
/*     */   }
/*     */ 
/*     */   private static String resolvePkgs(Map<String, ?> paramMap)
/*     */     throws JMXProviderException
/*     */   {
/* 375 */     Object localObject = null;
/*     */ 
/* 377 */     if (paramMap != null) {
/* 378 */       localObject = paramMap.get("jmx.remote.protocol.provider.pkgs");
/*     */     }
/* 380 */     if (localObject == null) {
/* 381 */       localObject = AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run() {
/* 384 */           return System.getProperty("jmx.remote.protocol.provider.pkgs");
/*     */         }
/*     */       });
/*     */     }
/* 388 */     if (localObject == null) {
/* 389 */       return null;
/*     */     }
/* 391 */     if (!(localObject instanceof String)) {
/* 392 */       str1 = "Value of jmx.remote.protocol.provider.pkgs parameter is not a String: " + localObject.getClass().getName();
/*     */ 
/* 395 */       throw new JMXProviderException(str1);
/*     */     }
/*     */ 
/* 398 */     String str1 = (String)localObject;
/* 399 */     if (str1.trim().equals("")) {
/* 400 */       return null;
/*     */     }
/*     */ 
/* 403 */     if ((str1.startsWith("|")) || (str1.endsWith("|")) || (str1.indexOf("||") >= 0))
/*     */     {
/* 405 */       String str2 = "Value of jmx.remote.protocol.provider.pkgs contains an empty element: " + str1;
/*     */ 
/* 407 */       throw new JMXProviderException(str2);
/*     */     }
/*     */ 
/* 410 */     return str1;
/*     */   }
/*     */ 
/*     */   static <T> T getProvider(JMXServiceURL paramJMXServiceURL, Map<String, Object> paramMap, String paramString, Class<T> paramClass, ClassLoader paramClassLoader)
/*     */     throws IOException
/*     */   {
/* 420 */     String str1 = paramJMXServiceURL.getProtocol();
/*     */ 
/* 422 */     String str2 = resolvePkgs(paramMap);
/*     */ 
/* 424 */     Object localObject = null;
/*     */ 
/* 426 */     if (str2 != null) {
/* 427 */       localObject = getProvider(str1, str2, paramClassLoader, paramString, paramClass);
/*     */ 
/* 431 */       if (localObject != null) {
/* 432 */         int i = paramClassLoader != localObject.getClass().getClassLoader() ? 1 : 0;
/* 433 */         paramMap.put("jmx.remote.protocol.provider.class.loader", i != 0 ? wrap(paramClassLoader) : paramClassLoader);
/*     */       }
/*     */     }
/*     */ 
/* 437 */     return localObject;
/*     */   }
/*     */ 
/*     */   static <T> Iterator<T> getProviderIterator(Class<T> paramClass, ClassLoader paramClassLoader)
/*     */   {
/* 442 */     ServiceLoader localServiceLoader = ServiceLoader.load(paramClass, paramClassLoader);
/*     */ 
/* 444 */     return localServiceLoader.iterator();
/*     */   }
/*     */ 
/*     */   private static ClassLoader wrap(ClassLoader paramClassLoader) {
/* 448 */     return paramClassLoader != null ? (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 451 */         return new ClassLoader(this.val$parent)
/*     */         {
/*     */           protected Class<?> loadClass(String paramAnonymous2String, boolean paramAnonymous2Boolean) throws ClassNotFoundException {
/* 454 */             ReflectUtil.checkPackageAccess(paramAnonymous2String);
/* 455 */             return super.loadClass(paramAnonymous2String, paramAnonymous2Boolean);
/*     */           }
/*     */         };
/*     */       }
/*     */     }) : null;
/*     */   }
/*     */ 
/*     */   private static JMXConnector getConnectorAsService(ClassLoader paramClassLoader, JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 467 */     Iterator localIterator = getProviderIterator(JMXConnectorProvider.class, paramClassLoader);
/*     */ 
/* 470 */     IOException localIOException = null;
/* 471 */     while (localIterator.hasNext()) {
/* 472 */       JMXConnectorProvider localJMXConnectorProvider = (JMXConnectorProvider)localIterator.next();
/*     */       try {
/* 474 */         return localJMXConnectorProvider.newJMXConnector(paramJMXServiceURL, paramMap);
/*     */       }
/*     */       catch (JMXProviderException localJMXProviderException) {
/* 477 */         throw localJMXProviderException;
/*     */       } catch (Exception localException) {
/* 479 */         if (logger.traceOn()) {
/* 480 */           logger.trace("getConnectorAsService", "URL[" + paramJMXServiceURL + "] Service provider exception: " + localException);
/*     */         }
/*     */ 
/* 483 */         if ((!(localException instanceof MalformedURLException)) && 
/* 484 */           (localIOException == null)) {
/* 485 */           if ((localException instanceof IOException))
/* 486 */             localIOException = (IOException)localException;
/*     */           else {
/* 488 */             localIOException = (IOException)EnvHelp.initCause(new IOException(localException.getMessage()), localException);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 496 */     if (localIOException == null) {
/* 497 */       return null;
/*     */     }
/* 499 */     throw localIOException;
/*     */   }
/*     */ 
/*     */   static <T> T getProvider(String paramString1, String paramString2, ClassLoader paramClassLoader, String paramString3, Class<T> paramClass)
/*     */     throws IOException
/*     */   {
/* 509 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString2, "|");
/*     */ 
/* 511 */     while (localStringTokenizer.hasMoreTokens()) { String str1 = localStringTokenizer.nextToken();
/* 513 */       String str2 = str1 + "." + protocol2package(paramString1) + "." + paramString3;
/*     */       Class localClass;
/*     */       try {
/* 517 */         localClass = Class.forName(str2, true, paramClassLoader);
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/*     */       }
/* 520 */       continue;
/*     */ 
/* 523 */       if (!paramClass.isAssignableFrom(localClass)) {
/* 524 */         localObject = "Provider class does not implement " + paramClass.getName() + ": " + localClass.getName();
/*     */ 
/* 528 */         throw new JMXProviderException((String)localObject);
/*     */       }
/*     */ 
/* 532 */       Object localObject = (Class)Util.cast(localClass);
/*     */       try {
/* 534 */         return ((Class)localObject).newInstance();
/*     */       } catch (Exception localException) {
/* 536 */         String str3 = "Exception when instantiating provider [" + str2 + "]";
/*     */ 
/* 539 */         throw new JMXProviderException(str3, localException);
/*     */       }
/*     */     }
/*     */ 
/* 543 */     return null;
/*     */   }
/*     */ 
/*     */   static ClassLoader resolveClassLoader(Map<String, ?> paramMap) {
/* 547 */     ClassLoader localClassLoader = null;
/*     */ 
/* 549 */     if (paramMap != null) {
/*     */       try {
/* 551 */         localClassLoader = (ClassLoader)paramMap.get("jmx.remote.protocol.provider.class.loader");
/*     */       }
/*     */       catch (ClassCastException localClassCastException)
/*     */       {
/* 558 */         throw new IllegalArgumentException("The ClassLoader supplied in the environment map using the jmx.remote.protocol.provider.class.loader attribute is not an instance of java.lang.ClassLoader");
/*     */       }
/*     */     }
/*     */ 
/* 562 */     if (localClassLoader == null) {
/* 563 */       localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     }
/*     */ 
/* 566 */     return localClassLoader;
/*     */   }
/*     */ 
/*     */   private static String protocol2package(String paramString) {
/* 570 */     return paramString.replace('+', '.').replace('-', '_');
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXConnectorFactory
 * JD-Core Version:    0.6.2
 */