/*      */ package sun.rmi.server;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.net.JarURLConnection;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.SocketPermission;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.net.URLConnection;
/*      */ import java.rmi.server.LogStream;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.CodeSource;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Permissions;
/*      */ import java.security.Policy;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.PropertyPermission;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.WeakHashMap;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.rmi.runtime.Log;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public final class LoaderHandler
/*      */ {
/*   73 */   static final int logLevel = LogStream.parseLevel((String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.loader.logLevel")));
/*      */ 
/*   78 */   static final Log loaderLog = Log.getLog("sun.rmi.loader", "loader", logLevel);
/*      */ 
/*   85 */   private static String codebaseProperty = null;
/*      */   private static URL[] codebaseURLs;
/*      */   private static final Map<ClassLoader, Void> codebaseLoaders;
/*  115 */   private static final HashMap<LoaderKey, LoaderEntry> loaderTable = new HashMap(5);
/*      */ 
/*  119 */   private static final ReferenceQueue<Loader> refQueue = new ReferenceQueue();
/*      */ 
/*  779 */   private static final Map<String, Object[]> pathToURLsCache = new WeakHashMap(5);
/*      */ 
/*      */   private static synchronized URL[] getDefaultCodebaseURLs()
/*      */     throws MalformedURLException
/*      */   {
/*  138 */     if (codebaseURLs == null) {
/*  139 */       if (codebaseProperty != null)
/*  140 */         codebaseURLs = pathToURLs(codebaseProperty);
/*      */       else {
/*  142 */         codebaseURLs = new URL[0];
/*      */       }
/*      */     }
/*  145 */     return codebaseURLs;
/*      */   }
/*      */ 
/*      */   public static Class<?> loadClass(String paramString1, String paramString2, ClassLoader paramClassLoader)
/*      */     throws MalformedURLException, ClassNotFoundException
/*      */   {
/*  157 */     if (loaderLog.isLoggable(Log.BRIEF))
/*  158 */       loaderLog.log(Log.BRIEF, "name = \"" + paramString2 + "\", " + "codebase = \"" + (paramString1 != null ? paramString1 : "") + "\"" + (paramClassLoader != null ? ", defaultLoader = " + paramClassLoader : ""));
/*      */     URL[] arrayOfURL;
/*  166 */     if (paramString1 != null)
/*  167 */       arrayOfURL = pathToURLs(paramString1);
/*      */     else {
/*  169 */       arrayOfURL = getDefaultCodebaseURLs();
/*      */     }
/*      */ 
/*  172 */     if (paramClassLoader != null)
/*      */       try {
/*  174 */         Class localClass = loadClassForName(paramString2, false, paramClassLoader);
/*  175 */         if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  176 */           loaderLog.log(Log.VERBOSE, "class \"" + paramString2 + "\" found via defaultLoader, " + "defined by " + localClass.getClassLoader());
/*      */         }
/*      */ 
/*  180 */         return localClass;
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException)
/*      */       {
/*      */       }
/*  185 */     return loadClass(arrayOfURL, paramString2);
/*      */   }
/*      */ 
/*      */   public static String getClassAnnotation(Class<?> paramClass)
/*      */   {
/*  194 */     String str1 = paramClass.getName();
/*      */ 
/*  203 */     int i = str1.length();
/*  204 */     if ((i > 0) && (str1.charAt(0) == '['))
/*      */     {
/*  206 */       int j = 1;
/*  207 */       while ((i > j) && (str1.charAt(j) == '[')) {
/*  208 */         j++;
/*      */       }
/*  210 */       if ((i > j) && (str1.charAt(j) != 'L')) {
/*  211 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  221 */     ClassLoader localClassLoader = paramClass.getClassLoader();
/*  222 */     if ((localClassLoader == null) || (codebaseLoaders.containsKey(localClassLoader))) {
/*  223 */       return codebaseProperty;
/*      */     }
/*      */ 
/*  230 */     String str2 = null;
/*  231 */     if ((localClassLoader instanceof Loader))
/*      */     {
/*  237 */       str2 = ((Loader)localClassLoader).getClassAnnotation();
/*      */     }
/*  239 */     else if ((localClassLoader instanceof URLClassLoader)) {
/*      */       try {
/*  241 */         URL[] arrayOfURL = ((URLClassLoader)localClassLoader).getURLs();
/*  242 */         if (arrayOfURL != null)
/*      */         {
/*  248 */           SecurityManager localSecurityManager = System.getSecurityManager();
/*  249 */           if (localSecurityManager != null) {
/*  250 */             Permissions localPermissions = new Permissions();
/*  251 */             for (int k = 0; k < arrayOfURL.length; k++) {
/*  252 */               Permission localPermission = arrayOfURL[k].openConnection().getPermission();
/*      */ 
/*  254 */               if ((localPermission != null) && 
/*  255 */                 (!localPermissions.implies(localPermission))) {
/*  256 */                 localSecurityManager.checkPermission(localPermission);
/*  257 */                 localPermissions.add(localPermission);
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  263 */           str2 = urlsToPath(arrayOfURL);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (SecurityException|IOException localSecurityException)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  278 */     if (str2 != null) {
/*  279 */       return str2;
/*      */     }
/*  281 */     return codebaseProperty;
/*      */   }
/*      */ 
/*      */   public static ClassLoader getClassLoader(String paramString)
/*      */     throws MalformedURLException
/*      */   {
/*  293 */     ClassLoader localClassLoader = getRMIContextClassLoader();
/*      */     URL[] arrayOfURL;
/*  296 */     if (paramString != null)
/*  297 */       arrayOfURL = pathToURLs(paramString);
/*      */     else {
/*  299 */       arrayOfURL = getDefaultCodebaseURLs();
/*      */     }
/*      */ 
/*  306 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  307 */     if (localSecurityManager != null) {
/*  308 */       localSecurityManager.checkPermission(new RuntimePermission("getClassLoader"));
/*      */     }
/*      */     else
/*      */     {
/*  314 */       return localClassLoader;
/*      */     }
/*      */ 
/*  317 */     Loader localLoader = lookupLoader(arrayOfURL, localClassLoader);
/*      */ 
/*  322 */     if (localLoader != null) {
/*  323 */       localLoader.checkPermissions();
/*      */     }
/*      */ 
/*  326 */     return localLoader;
/*      */   }
/*      */ 
/*      */   public static Object getSecurityContext(ClassLoader paramClassLoader)
/*      */   {
/*  339 */     if ((paramClassLoader instanceof Loader)) {
/*  340 */       URL[] arrayOfURL = ((Loader)paramClassLoader).getURLs();
/*  341 */       if (arrayOfURL.length > 0) {
/*  342 */         return arrayOfURL[0];
/*      */       }
/*      */     }
/*  345 */     return null;
/*      */   }
/*      */ 
/*      */   public static void registerCodebaseLoader(ClassLoader paramClassLoader)
/*      */   {
/*  353 */     codebaseLoaders.put(paramClassLoader, null);
/*      */   }
/*      */ 
/*      */   private static Class<?> loadClass(URL[] paramArrayOfURL, String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/*  363 */     ClassLoader localClassLoader = getRMIContextClassLoader();
/*  364 */     if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  365 */       loaderLog.log(Log.VERBOSE, "(thread context class loader: " + localClassLoader + ")");
/*      */     }
/*      */ 
/*  374 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  375 */     if (localSecurityManager == null) {
/*      */       try {
/*  377 */         Class localClass1 = loadClassForName(paramString, false, localClassLoader);
/*  378 */         if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  379 */           loaderLog.log(Log.VERBOSE, "class \"" + paramString + "\" found via " + "thread context class loader " + "(no security manager: codebase disabled), " + "defined by " + localClass1.getClassLoader());
/*      */         }
/*      */ 
/*  385 */         return localClass1;
/*      */       } catch (ClassNotFoundException localClassNotFoundException1) {
/*  387 */         if (loaderLog.isLoggable(Log.BRIEF)) {
/*  388 */           loaderLog.log(Log.BRIEF, "class \"" + paramString + "\" not found via " + "thread context class loader " + "(no security manager: codebase disabled)", localClassNotFoundException1);
/*      */         }
/*      */ 
/*  393 */         throw new ClassNotFoundException(localClassNotFoundException1.getMessage() + " (no security manager: RMI class loader disabled)", localClassNotFoundException1.getException());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  403 */     Loader localLoader = lookupLoader(paramArrayOfURL, localClassLoader);
/*      */     try
/*      */     {
/*  406 */       if (localLoader != null)
/*      */       {
/*  410 */         localLoader.checkPermissions();
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SecurityException localSecurityException)
/*      */     {
/*      */       try
/*      */       {
/*  426 */         Class localClass3 = loadClassForName(paramString, false, localClassLoader);
/*  427 */         if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  428 */           loaderLog.log(Log.VERBOSE, "class \"" + paramString + "\" found via " + "thread context class loader " + "(access to codebase denied), " + "defined by " + localClass3.getClassLoader());
/*      */         }
/*      */ 
/*  434 */         return localClass3;
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException3)
/*      */       {
/*  440 */         if (loaderLog.isLoggable(Log.BRIEF)) {
/*  441 */           loaderLog.log(Log.BRIEF, "class \"" + paramString + "\" not found via " + "thread context class loader " + "(access to codebase denied)", localSecurityException);
/*      */         }
/*      */ 
/*  446 */         throw new ClassNotFoundException("access to class loader denied", localSecurityException);
/*      */       }
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  452 */       Class localClass2 = loadClassForName(paramString, false, localLoader);
/*  453 */       if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  454 */         loaderLog.log(Log.VERBOSE, "class \"" + paramString + "\" " + "found via codebase, " + "defined by " + localClass2.getClassLoader());
/*      */       }
/*      */ 
/*  458 */       return localClass2;
/*      */     } catch (ClassNotFoundException localClassNotFoundException2) {
/*  460 */       if (loaderLog.isLoggable(Log.BRIEF)) {
/*  461 */         loaderLog.log(Log.BRIEF, "class \"" + paramString + "\" not found via codebase", localClassNotFoundException2);
/*      */       }
/*      */ 
/*  464 */       throw localClassNotFoundException2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Class<?> loadProxyClass(String paramString, String[] paramArrayOfString, ClassLoader paramClassLoader)
/*      */     throws MalformedURLException, ClassNotFoundException
/*      */   {
/*  478 */     if (loaderLog.isLoggable(Log.BRIEF)) {
/*  479 */       loaderLog.log(Log.BRIEF, "interfaces = " + Arrays.asList(paramArrayOfString) + ", " + "codebase = \"" + (paramString != null ? paramString : "") + "\"" + (paramClassLoader != null ? ", defaultLoader = " + paramClassLoader : ""));
/*      */     }
/*      */ 
/*  519 */     ClassLoader localClassLoader = getRMIContextClassLoader();
/*  520 */     if (loaderLog.isLoggable(Log.VERBOSE))
/*  521 */       loaderLog.log(Log.VERBOSE, "(thread context class loader: " + localClassLoader + ")");
/*      */     URL[] arrayOfURL;
/*  526 */     if (paramString != null)
/*  527 */       arrayOfURL = pathToURLs(paramString);
/*      */     else {
/*  529 */       arrayOfURL = getDefaultCodebaseURLs();
/*      */     }
/*      */ 
/*  536 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  537 */     if (localSecurityManager == null) {
/*      */       try {
/*  539 */         Class localClass1 = loadProxyClass(paramArrayOfString, paramClassLoader, localClassLoader, false);
/*      */ 
/*  541 */         if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  542 */           loaderLog.log(Log.VERBOSE, "(no security manager: codebase disabled) proxy class defined by " + localClass1.getClassLoader());
/*      */         }
/*      */ 
/*  546 */         return localClass1;
/*      */       } catch (ClassNotFoundException localClassNotFoundException1) {
/*  548 */         if (loaderLog.isLoggable(Log.BRIEF)) {
/*  549 */           loaderLog.log(Log.BRIEF, "(no security manager: codebase disabled) proxy class resolution failed", localClassNotFoundException1);
/*      */         }
/*      */ 
/*  553 */         throw new ClassNotFoundException(localClassNotFoundException1.getMessage() + " (no security manager: RMI class loader disabled)", localClassNotFoundException1.getException());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  563 */     Loader localLoader = lookupLoader(arrayOfURL, localClassLoader);
/*      */     try
/*      */     {
/*  566 */       if (localLoader != null)
/*      */       {
/*  570 */         localLoader.checkPermissions();
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SecurityException localSecurityException)
/*      */     {
/*      */       try
/*      */       {
/*  586 */         Class localClass3 = loadProxyClass(paramArrayOfString, paramClassLoader, localClassLoader, false);
/*      */ 
/*  588 */         if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  589 */           loaderLog.log(Log.VERBOSE, "(access to codebase denied) proxy class defined by " + localClass3.getClassLoader());
/*      */         }
/*      */ 
/*  593 */         return localClass3;
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException3)
/*      */       {
/*  599 */         if (loaderLog.isLoggable(Log.BRIEF)) {
/*  600 */           loaderLog.log(Log.BRIEF, "(access to codebase denied) proxy class resolution failed", localSecurityException);
/*      */         }
/*      */ 
/*  604 */         throw new ClassNotFoundException("access to class loader denied", localSecurityException);
/*      */       }
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  610 */       Class localClass2 = loadProxyClass(paramArrayOfString, paramClassLoader, localLoader, true);
/*  611 */       if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  612 */         loaderLog.log(Log.VERBOSE, "proxy class defined by " + localClass2.getClassLoader());
/*      */       }
/*      */ 
/*  615 */       return localClass2;
/*      */     } catch (ClassNotFoundException localClassNotFoundException2) {
/*  617 */       if (loaderLog.isLoggable(Log.BRIEF)) {
/*  618 */         loaderLog.log(Log.BRIEF, "proxy class resolution failed", localClassNotFoundException2);
/*      */       }
/*      */ 
/*  621 */       throw localClassNotFoundException2;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Class<?> loadProxyClass(String[] paramArrayOfString, ClassLoader paramClassLoader1, ClassLoader paramClassLoader2, boolean paramBoolean)
/*      */     throws ClassNotFoundException
/*      */   {
/*  637 */     ClassLoader localClassLoader = null;
/*  638 */     Class[] arrayOfClass = new Class[paramArrayOfString.length];
/*  639 */     boolean[] arrayOfBoolean = { false };
/*      */     int i;
/*  642 */     if (paramClassLoader1 != null) {
/*      */       try {
/*  644 */         localClassLoader = loadProxyInterfaces(paramArrayOfString, paramClassLoader1, arrayOfClass, arrayOfBoolean);
/*      */ 
/*  647 */         if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  648 */           ClassLoader[] arrayOfClassLoader1 = new ClassLoader[arrayOfClass.length];
/*      */ 
/*  650 */           for (i = 0; i < arrayOfClassLoader1.length; i++) {
/*  651 */             arrayOfClassLoader1[i] = arrayOfClass[i].getClassLoader();
/*      */           }
/*  653 */           loaderLog.log(Log.VERBOSE, "proxy interfaces found via defaultLoader, defined by " + Arrays.asList(arrayOfClassLoader1));
/*      */         }
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException)
/*      */       {
/*  658 */         break label155;
/*      */       }
/*  660 */       if (arrayOfBoolean[0] == 0) {
/*  661 */         if (paramBoolean)
/*      */           try {
/*  663 */             return Proxy.getProxyClass(paramClassLoader2, arrayOfClass);
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException) {
/*      */           }
/*  667 */         localClassLoader = paramClassLoader1;
/*      */       }
/*  669 */       return loadProxyClass(localClassLoader, arrayOfClass);
/*      */     }
/*      */ 
/*  672 */     label155: arrayOfBoolean[0] = false;
/*  673 */     localClassLoader = loadProxyInterfaces(paramArrayOfString, paramClassLoader2, arrayOfClass, arrayOfBoolean);
/*      */ 
/*  675 */     if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  676 */       ClassLoader[] arrayOfClassLoader2 = new ClassLoader[arrayOfClass.length];
/*  677 */       for (i = 0; i < arrayOfClassLoader2.length; i++) {
/*  678 */         arrayOfClassLoader2[i] = arrayOfClass[i].getClassLoader();
/*      */       }
/*  680 */       loaderLog.log(Log.VERBOSE, "proxy interfaces found via codebase, defined by " + Arrays.asList(arrayOfClassLoader2));
/*      */     }
/*      */ 
/*  684 */     if (arrayOfBoolean[0] == 0) {
/*  685 */       localClassLoader = paramClassLoader2;
/*      */     }
/*  687 */     return loadProxyClass(localClassLoader, arrayOfClass);
/*      */   }
/*      */ 
/*      */   private static Class<?> loadProxyClass(ClassLoader paramClassLoader, Class[] paramArrayOfClass)
/*      */     throws ClassNotFoundException
/*      */   {
/*      */     try
/*      */     {
/*  698 */       return Proxy.getProxyClass(paramClassLoader, paramArrayOfClass);
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*  700 */       throw new ClassNotFoundException("error creating dynamic proxy class", localIllegalArgumentException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static ClassLoader loadProxyInterfaces(String[] paramArrayOfString, ClassLoader paramClassLoader, Class[] paramArrayOfClass, boolean[] paramArrayOfBoolean)
/*      */     throws ClassNotFoundException
/*      */   {
/*  726 */     Object localObject = null;
/*      */ 
/*  728 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  729 */       Class localClass = paramArrayOfClass[i] =  = loadClassForName(paramArrayOfString[i], false, paramClassLoader);
/*      */ 
/*  732 */       if (!Modifier.isPublic(localClass.getModifiers())) {
/*  733 */         ClassLoader localClassLoader = localClass.getClassLoader();
/*  734 */         if (loaderLog.isLoggable(Log.VERBOSE)) {
/*  735 */           loaderLog.log(Log.VERBOSE, "non-public interface \"" + paramArrayOfString[i] + "\" defined by " + localClassLoader);
/*      */         }
/*      */ 
/*  739 */         if (paramArrayOfBoolean[0] == 0) {
/*  740 */           localObject = localClassLoader;
/*  741 */           paramArrayOfBoolean[0] = true;
/*  742 */         } else if (localClassLoader != localObject) {
/*  743 */           throw new IllegalAccessError("non-public interfaces defined in different class loaders");
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  749 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static URL[] pathToURLs(String paramString)
/*      */     throws MalformedURLException
/*      */   {
/*  760 */     synchronized (pathToURLsCache) {
/*  761 */       localObject1 = (Object[])pathToURLsCache.get(paramString);
/*  762 */       if (localObject1 != null) {
/*  763 */         return (URL[])localObject1[0];
/*      */       }
/*      */     }
/*  766 */     ??? = new StringTokenizer(paramString);
/*  767 */     Object localObject1 = new URL[((StringTokenizer)???).countTokens()];
/*  768 */     for (int i = 0; ((StringTokenizer)???).hasMoreTokens(); i++) {
/*  769 */       localObject1[i] = new URL(((StringTokenizer)???).nextToken());
/*      */     }
/*  771 */     synchronized (pathToURLsCache) {
/*  772 */       pathToURLsCache.put(paramString, new Object[] { localObject1, new SoftReference(paramString) });
/*      */     }
/*      */ 
/*  775 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static String urlsToPath(URL[] paramArrayOfURL)
/*      */   {
/*  790 */     if (paramArrayOfURL.length == 0)
/*  791 */       return null;
/*  792 */     if (paramArrayOfURL.length == 1) {
/*  793 */       return paramArrayOfURL[0].toExternalForm();
/*      */     }
/*  795 */     StringBuffer localStringBuffer = new StringBuffer(paramArrayOfURL[0].toExternalForm());
/*  796 */     for (int i = 1; i < paramArrayOfURL.length; i++) {
/*  797 */       localStringBuffer.append(' ');
/*  798 */       localStringBuffer.append(paramArrayOfURL[i].toExternalForm());
/*      */     }
/*  800 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static ClassLoader getRMIContextClassLoader()
/*      */   {
/*  813 */     return Thread.currentThread().getContextClassLoader();
/*      */   }
/*      */ 
/*      */   private static Loader lookupLoader(URL[] paramArrayOfURL, final ClassLoader paramClassLoader)
/*      */   {
/*      */     Loader localLoader;
/*  842 */     synchronized (LoaderHandler.class)
/*      */     {
/*  847 */       while ((localLoaderEntry = (LoaderEntry)refQueue.poll()) != null) {
/*  848 */         if (!localLoaderEntry.removed) {
/*  849 */           loaderTable.remove(localLoaderEntry.key);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  857 */       LoaderKey localLoaderKey = new LoaderKey(paramArrayOfURL, paramClassLoader);
/*  858 */       LoaderEntry localLoaderEntry = (LoaderEntry)loaderTable.get(localLoaderKey);
/*      */ 
/*  860 */       if ((localLoaderEntry == null) || ((localLoader = (Loader)localLoaderEntry.get()) == null))
/*      */       {
/*  868 */         if (localLoaderEntry != null) {
/*  869 */           loaderTable.remove(localLoaderKey);
/*  870 */           localLoaderEntry.removed = true;
/*      */         }
/*      */ 
/*  880 */         AccessControlContext localAccessControlContext = getLoaderAccessControlContext(paramArrayOfURL);
/*  881 */         localLoader = (Loader)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public LoaderHandler.Loader run() {
/*  884 */             return new LoaderHandler.Loader(this.val$urls, paramClassLoader, null);
/*      */           }
/*      */         }
/*      */         , localAccessControlContext);
/*      */ 
/*  892 */         localLoaderEntry = new LoaderEntry(localLoaderKey, localLoader);
/*  893 */         loaderTable.put(localLoaderKey, localLoaderEntry);
/*      */       }
/*      */     }
/*      */ 
/*  897 */     return localLoader;
/*      */   }
/*      */ 
/*      */   private static AccessControlContext getLoaderAccessControlContext(URL[] paramArrayOfURL)
/*      */   {
/*  988 */     PermissionCollection localPermissionCollection = (PermissionCollection)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public PermissionCollection run()
/*      */       {
/*  992 */         CodeSource localCodeSource = new CodeSource(null, (Certificate[])null);
/*      */ 
/*  994 */         Policy localPolicy = Policy.getPolicy();
/*  995 */         if (localPolicy != null) {
/*  996 */           return localPolicy.getPermissions(localCodeSource);
/*      */         }
/*  998 */         return new Permissions();
/*      */       }
/*      */     });
/* 1004 */     localPermissionCollection.add(new RuntimePermission("createClassLoader"));
/*      */ 
/* 1007 */     localPermissionCollection.add(new PropertyPermission("java.*", "read"));
/*      */ 
/* 1010 */     addPermissionsForURLs(paramArrayOfURL, localPermissionCollection, true);
/*      */ 
/* 1016 */     ProtectionDomain localProtectionDomain = new ProtectionDomain(new CodeSource(paramArrayOfURL.length > 0 ? paramArrayOfURL[0] : null, (Certificate[])null), localPermissionCollection);
/*      */ 
/* 1020 */     return new AccessControlContext(new ProtectionDomain[] { localProtectionDomain });
/*      */   }
/*      */ 
/*      */   public static void addPermissionsForURLs(URL[] paramArrayOfURL, PermissionCollection paramPermissionCollection, boolean paramBoolean)
/*      */   {
/* 1037 */     for (int i = 0; i < paramArrayOfURL.length; i++) {
/* 1038 */       URL localURL = paramArrayOfURL[i];
/*      */       try {
/* 1040 */         URLConnection localURLConnection = localURL.openConnection();
/* 1041 */         Permission localPermission = localURLConnection.getPermission();
/* 1042 */         if (localPermission != null)
/*      */         {
/*      */           Object localObject1;
/*      */           Object localObject3;
/* 1043 */           if ((localPermission instanceof FilePermission))
/*      */           {
/* 1055 */             localObject1 = localPermission.getName();
/* 1056 */             int j = ((String)localObject1).lastIndexOf(File.separatorChar);
/* 1057 */             if (j != -1) {
/* 1058 */               localObject1 = ((String)localObject1).substring(0, j + 1);
/* 1059 */               if (((String)localObject1).endsWith(File.separator)) {
/* 1060 */                 localObject1 = (String)localObject1 + "-";
/*      */               }
/* 1062 */               localObject3 = new FilePermission((String)localObject1, "read");
/* 1063 */               if (!paramPermissionCollection.implies((Permission)localObject3)) {
/* 1064 */                 paramPermissionCollection.add((Permission)localObject3);
/*      */               }
/* 1066 */               paramPermissionCollection.add(new FilePermission((String)localObject1, "read"));
/*      */             }
/* 1072 */             else if (!paramPermissionCollection.implies(localPermission)) {
/* 1073 */               paramPermissionCollection.add(localPermission);
/*      */             }
/*      */           }
/*      */           else {
/* 1077 */             if (!paramPermissionCollection.implies(localPermission)) {
/* 1078 */               paramPermissionCollection.add(localPermission);
/*      */             }
/*      */ 
/* 1089 */             if (paramBoolean)
/*      */             {
/* 1091 */               localObject1 = localURL;
/* 1092 */               Object localObject2 = localURLConnection;
/* 1093 */               while ((localObject2 instanceof JarURLConnection))
/*      */               {
/* 1095 */                 localObject1 = ((JarURLConnection)localObject2).getJarFileURL();
/*      */ 
/* 1097 */                 localObject2 = ((URL)localObject1).openConnection();
/*      */               }
/* 1099 */               localObject2 = ((URL)localObject1).getHost();
/* 1100 */               if ((localObject2 != null) && (localPermission.implies(new SocketPermission((String)localObject2, "resolve"))))
/*      */               {
/* 1104 */                 localObject3 = new SocketPermission((String)localObject2, "connect,accept");
/*      */ 
/* 1107 */                 if (!paramPermissionCollection.implies((Permission)localObject3))
/* 1108 */                   paramPermissionCollection.add((Permission)localObject3);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Class<?> loadClassForName(String paramString, boolean paramBoolean, ClassLoader paramClassLoader)
/*      */     throws ClassNotFoundException
/*      */   {
/* 1216 */     if (paramClassLoader == null) {
/* 1217 */       ReflectUtil.checkPackageAccess(paramString);
/*      */     }
/* 1219 */     return Class.forName(paramString, paramBoolean, paramClassLoader);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   87 */     Object localObject = (String)AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.codebase"));
/*      */ 
/*   89 */     if ((localObject != null) && (((String)localObject).trim().length() > 0)) {
/*   90 */       codebaseProperty = (String)localObject;
/*      */     }
/*      */ 
/*   95 */     codebaseURLs = null;
/*      */ 
/*   98 */     codebaseLoaders = Collections.synchronizedMap(new IdentityHashMap(5));
/*      */ 
/*  101 */     for (localObject = ClassLoader.getSystemClassLoader(); 
/*  102 */       localObject != null; 
/*  103 */       localObject = ((ClassLoader)localObject).getParent())
/*      */     {
/*  105 */       codebaseLoaders.put(localObject, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Loader extends URLClassLoader
/*      */   {
/*      */     private ClassLoader parent;
/*      */     private String annotation;
/*      */     private Permissions permissions;
/*      */ 
/*      */     private Loader(URL[] paramArrayOfURL, ClassLoader paramClassLoader)
/*      */     {
/* 1141 */       super(paramClassLoader);
/* 1142 */       this.parent = paramClassLoader;
/*      */ 
/* 1147 */       this.permissions = new Permissions();
/* 1148 */       LoaderHandler.addPermissionsForURLs(paramArrayOfURL, this.permissions, false);
/*      */ 
/* 1155 */       this.annotation = LoaderHandler.urlsToPath(paramArrayOfURL);
/*      */     }
/*      */ 
/*      */     public String getClassAnnotation()
/*      */     {
/* 1163 */       return this.annotation;
/*      */     }
/*      */ 
/*      */     private void checkPermissions()
/*      */     {
/* 1171 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 1172 */       if (localSecurityManager != null) {
/* 1173 */         Enumeration localEnumeration = this.permissions.elements();
/* 1174 */         while (localEnumeration.hasMoreElements())
/* 1175 */           localSecurityManager.checkPermission((Permission)localEnumeration.nextElement());
/*      */       }
/*      */     }
/*      */ 
/*      */     protected PermissionCollection getPermissions(CodeSource paramCodeSource)
/*      */     {
/* 1185 */       PermissionCollection localPermissionCollection = super.getPermissions(paramCodeSource);
/*      */ 
/* 1189 */       return localPermissionCollection;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1197 */       return super.toString() + "[\"" + this.annotation + "\"]";
/*      */     }
/*      */ 
/*      */     protected Class<?> loadClass(String paramString, boolean paramBoolean)
/*      */       throws ClassNotFoundException
/*      */     {
/* 1203 */       if (this.parent == null) {
/* 1204 */         ReflectUtil.checkPackageAccess(paramString);
/*      */       }
/* 1206 */       return super.loadClass(paramString, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LoaderEntry extends WeakReference<LoaderHandler.Loader>
/*      */   {
/*      */     public LoaderHandler.LoaderKey key;
/*  968 */     public boolean removed = false;
/*      */ 
/*      */     public LoaderEntry(LoaderHandler.LoaderKey paramLoaderKey, LoaderHandler.Loader paramLoader) {
/*  971 */       super(LoaderHandler.refQueue);
/*  972 */       this.key = paramLoaderKey;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LoaderKey
/*      */   {
/*      */     private URL[] urls;
/*      */     private ClassLoader parent;
/*      */     private int hashValue;
/*      */ 
/*      */     public LoaderKey(URL[] paramArrayOfURL, ClassLoader paramClassLoader)
/*      */     {
/*  913 */       this.urls = paramArrayOfURL;
/*  914 */       this.parent = paramClassLoader;
/*      */ 
/*  916 */       if (paramClassLoader != null) {
/*  917 */         this.hashValue = paramClassLoader.hashCode();
/*      */       }
/*  919 */       for (int i = 0; i < paramArrayOfURL.length; i++)
/*  920 */         this.hashValue ^= paramArrayOfURL[i].hashCode();
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  925 */       return this.hashValue;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  929 */       if ((paramObject instanceof LoaderKey)) {
/*  930 */         LoaderKey localLoaderKey = (LoaderKey)paramObject;
/*  931 */         if (this.parent != localLoaderKey.parent) {
/*  932 */           return false;
/*      */         }
/*  934 */         if (this.urls == localLoaderKey.urls) {
/*  935 */           return true;
/*      */         }
/*  937 */         if (this.urls.length != localLoaderKey.urls.length) {
/*  938 */           return false;
/*      */         }
/*  940 */         for (int i = 0; i < this.urls.length; i++) {
/*  941 */           if (!this.urls[i].equals(localLoaderKey.urls[i])) {
/*  942 */             return false;
/*      */           }
/*      */         }
/*  945 */         return true;
/*      */       }
/*  947 */       return false;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.LoaderHandler
 * JD-Core Version:    0.6.2
 */