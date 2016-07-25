/*      */ package java.lang;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.URL;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.CodeSource;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import sun.misc.ClassFileTransformer;
/*      */ import sun.misc.CompoundEnumeration;
/*      */ import sun.misc.Launcher;
/*      */ import sun.misc.PerfCounter;
/*      */ import sun.misc.Resource;
/*      */ import sun.misc.URLClassPath;
/*      */ import sun.misc.VM;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.Reflection;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ public abstract class ClassLoader
/*      */ {
/*      */   private final ClassLoader parent;
/*      */   private final ConcurrentHashMap<String, Object> parallelLockMap;
/*      */   private final Map<String, Certificate[]> package2certs;
/*  247 */   private static final Certificate[] nocerts = new Certificate[0];
/*      */ 
/*  251 */   private final Vector<Class<?>> classes = new Vector();
/*      */ 
/*  255 */   private final ProtectionDomain defaultDomain = new ProtectionDomain(new CodeSource(null, (Certificate[])null), null, this, null);
/*      */   private final Set<ProtectionDomain> domains;
/*  270 */   private final HashMap<String, Package> packages = new HashMap();
/*      */   private static ClassLoader scl;
/*      */   private static boolean sclSet;
/* 1793 */   private static Vector<String> loadedLibraryNames = new Vector();
/*      */ 
/* 1796 */   private static Vector<NativeLibrary> systemNativeLibraries = new Vector();
/*      */ 
/* 1800 */   private Vector<NativeLibrary> nativeLibraries = new Vector();
/*      */ 
/* 1803 */   private static Stack<NativeLibrary> nativeLibraryContext = new Stack();
/*      */   private static String[] usr_paths;
/*      */   private static String[] sys_paths;
/*      */   final Object assertionLock;
/* 2002 */   private boolean defaultAssertionStatus = false;
/*      */ 
/* 2010 */   private Map<String, Boolean> packageAssertionStatus = null;
/*      */ 
/* 2017 */   Map<String, Boolean> classAssertionStatus = null;
/*      */ 
/*      */   private static native void registerNatives();
/*      */ 
/*      */   void addClass(Class paramClass)
/*      */   {
/*  264 */     this.classes.addElement(paramClass);
/*      */   }
/*      */ 
/*      */   private static Void checkCreateClassLoader()
/*      */   {
/*  273 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  274 */     if (localSecurityManager != null) {
/*  275 */       localSecurityManager.checkCreateClassLoader();
/*      */     }
/*  277 */     return null;
/*      */   }
/*      */ 
/*      */   private ClassLoader(Void paramVoid, ClassLoader paramClassLoader) {
/*  281 */     this.parent = paramClassLoader;
/*  282 */     if (ParallelLoaders.isRegistered(getClass())) {
/*  283 */       this.parallelLockMap = new ConcurrentHashMap();
/*  284 */       this.package2certs = new ConcurrentHashMap();
/*  285 */       this.domains = Collections.synchronizedSet(new HashSet());
/*      */ 
/*  287 */       this.assertionLock = new Object();
/*      */     }
/*      */     else {
/*  290 */       this.parallelLockMap = null;
/*  291 */       this.package2certs = new Hashtable();
/*  292 */       this.domains = new HashSet();
/*  293 */       this.assertionLock = this;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ClassLoader(ClassLoader paramClassLoader)
/*      */   {
/*  317 */     this(checkCreateClassLoader(), paramClassLoader);
/*      */   }
/*      */ 
/*      */   protected ClassLoader()
/*      */   {
/*  336 */     this(checkCreateClassLoader(), getSystemClassLoader());
/*      */   }
/*      */ 
/*      */   public Class<?> loadClass(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/*  358 */     return loadClass(paramString, false);
/*      */   }
/*      */ 
/*      */   protected Class<?> loadClass(String paramString, boolean paramBoolean)
/*      */     throws ClassNotFoundException
/*      */   {
/*  405 */     synchronized (getClassLoadingLock(paramString))
/*      */     {
/*  407 */       Class localClass = findLoadedClass(paramString);
/*  408 */       if (localClass == null) {
/*  409 */         long l1 = System.nanoTime();
/*      */         try {
/*  411 */           if (this.parent != null)
/*  412 */             localClass = this.parent.loadClass(paramString, false);
/*      */           else {
/*  414 */             localClass = findBootstrapClassOrNull(paramString);
/*      */           }
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException)
/*      */         {
/*      */         }
/*      */ 
/*  421 */         if (localClass == null)
/*      */         {
/*  424 */           long l2 = System.nanoTime();
/*  425 */           localClass = findClass(paramString);
/*      */ 
/*  428 */           PerfCounter.getParentDelegationTime().addTime(l2 - l1);
/*  429 */           PerfCounter.getFindClassTime().addElapsedTimeFrom(l2);
/*  430 */           PerfCounter.getFindClasses().increment();
/*      */         }
/*      */       }
/*  433 */       if (paramBoolean) {
/*  434 */         resolveClass(localClass);
/*      */       }
/*  436 */       return localClass;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Object getClassLoadingLock(String paramString)
/*      */   {
/*  461 */     Object localObject1 = this;
/*  462 */     if (this.parallelLockMap != null) {
/*  463 */       Object localObject2 = new Object();
/*  464 */       localObject1 = this.parallelLockMap.putIfAbsent(paramString, localObject2);
/*  465 */       if (localObject1 == null) {
/*  466 */         localObject1 = localObject2;
/*      */       }
/*      */     }
/*  469 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private Class loadClassInternal(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/*  478 */     if (this.parallelLockMap == null) {
/*  479 */       synchronized (this) {
/*  480 */         return loadClass(paramString);
/*      */       }
/*      */     }
/*  483 */     return loadClass(paramString);
/*      */   }
/*      */ 
/*      */   private void checkPackageAccess(Class paramClass, ProtectionDomain paramProtectionDomain)
/*      */   {
/*  489 */     final SecurityManager localSecurityManager = System.getSecurityManager();
/*  490 */     if (localSecurityManager != null) {
/*  491 */       if (ReflectUtil.isNonPublicProxyClass(paramClass)) {
/*  492 */         for (Class localClass : paramClass.getInterfaces()) {
/*  493 */           checkPackageAccess(localClass, paramProtectionDomain);
/*      */         }
/*  495 */         return;
/*      */       }
/*      */ 
/*  498 */       ??? = paramClass.getName();
/*  499 */       ??? = ((String)???).lastIndexOf('.');
/*  500 */       if (??? != -1) {
/*  501 */         AccessController.doPrivileged(new PrivilegedAction() {
/*      */           public Void run() {
/*  503 */             localSecurityManager.checkPackageAccess(this.val$name.substring(0, i));
/*  504 */             return null;
/*      */           }
/*      */         }
/*      */         , new AccessControlContext(new ProtectionDomain[] { paramProtectionDomain }));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  509 */     this.domains.add(paramProtectionDomain);
/*      */   }
/*      */ 
/*      */   protected Class<?> findClass(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/*  531 */     throw new ClassNotFoundException(paramString);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected final Class<?> defineClass(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws ClassFormatError
/*      */   {
/*  579 */     return defineClass(null, paramArrayOfByte, paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */   protected final Class<?> defineClass(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws ClassFormatError
/*      */   {
/*  643 */     return defineClass(paramString, paramArrayOfByte, paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */   private ProtectionDomain preDefineClass(String paramString, ProtectionDomain paramProtectionDomain)
/*      */   {
/*  654 */     if (!checkName(paramString)) {
/*  655 */       throw new NoClassDefFoundError("IllegalName: " + paramString);
/*      */     }
/*  657 */     if ((paramString != null) && (paramString.startsWith("java."))) {
/*  658 */       throw new SecurityException("Prohibited package name: " + paramString.substring(0, paramString.lastIndexOf('.')));
/*      */     }
/*      */ 
/*  662 */     if (paramProtectionDomain == null) {
/*  663 */       paramProtectionDomain = this.defaultDomain;
/*      */     }
/*      */ 
/*  666 */     if (paramString != null) checkCerts(paramString, paramProtectionDomain.getCodeSource());
/*      */ 
/*  668 */     return paramProtectionDomain;
/*      */   }
/*      */ 
/*      */   private String defineClassSourceLocation(ProtectionDomain paramProtectionDomain)
/*      */   {
/*  673 */     CodeSource localCodeSource = paramProtectionDomain.getCodeSource();
/*  674 */     String str = null;
/*  675 */     if ((localCodeSource != null) && (localCodeSource.getLocation() != null)) {
/*  676 */       str = localCodeSource.getLocation().toString();
/*      */     }
/*  678 */     return str;
/*      */   }
/*      */ 
/*      */   private Class defineTransformedClass(String paramString1, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ProtectionDomain paramProtectionDomain, ClassFormatError paramClassFormatError, String paramString2)
/*      */     throws ClassFormatError
/*      */   {
/*  689 */     ClassFileTransformer[] arrayOfClassFileTransformer1 = ClassFileTransformer.getTransformers();
/*      */ 
/*  691 */     Class localClass = null;
/*      */ 
/*  693 */     if (arrayOfClassFileTransformer1 != null) {
/*  694 */       for (ClassFileTransformer localClassFileTransformer : arrayOfClassFileTransformer1) {
/*      */         try
/*      */         {
/*  697 */           byte[] arrayOfByte = localClassFileTransformer.transform(paramArrayOfByte, paramInt1, paramInt2);
/*  698 */           localClass = defineClass1(paramString1, arrayOfByte, 0, arrayOfByte.length, paramProtectionDomain, paramString2);
/*      */         }
/*      */         catch (ClassFormatError localClassFormatError)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  710 */     if (localClass == null) {
/*  711 */       throw paramClassFormatError;
/*      */     }
/*  713 */     return localClass;
/*      */   }
/*      */ 
/*      */   private void postDefineClass(Class paramClass, ProtectionDomain paramProtectionDomain)
/*      */   {
/*  718 */     if (paramProtectionDomain.getCodeSource() != null) {
/*  719 */       Certificate[] arrayOfCertificate = paramProtectionDomain.getCodeSource().getCertificates();
/*  720 */       if (arrayOfCertificate != null)
/*  721 */         setSigners(paramClass, arrayOfCertificate);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final Class<?> defineClass(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ProtectionDomain paramProtectionDomain)
/*      */     throws ClassFormatError
/*      */   {
/*  794 */     paramProtectionDomain = preDefineClass(paramString, paramProtectionDomain);
/*      */ 
/*  796 */     Class localClass = null;
/*  797 */     String str = defineClassSourceLocation(paramProtectionDomain);
/*      */     try
/*      */     {
/*  800 */       localClass = defineClass1(paramString, paramArrayOfByte, paramInt1, paramInt2, paramProtectionDomain, str);
/*      */     } catch (ClassFormatError localClassFormatError) {
/*  802 */       localClass = defineTransformedClass(paramString, paramArrayOfByte, paramInt1, paramInt2, paramProtectionDomain, localClassFormatError, str);
/*      */     }
/*      */ 
/*  806 */     postDefineClass(localClass, paramProtectionDomain);
/*  807 */     return localClass;
/*      */   }
/*      */ 
/*      */   protected final Class<?> defineClass(String paramString, ByteBuffer paramByteBuffer, ProtectionDomain paramProtectionDomain)
/*      */     throws ClassFormatError
/*      */   {
/*  876 */     int i = paramByteBuffer.remaining();
/*      */ 
/*  879 */     if (!paramByteBuffer.isDirect()) {
/*  880 */       if (paramByteBuffer.hasArray()) {
/*  881 */         return defineClass(paramString, paramByteBuffer.array(), paramByteBuffer.position() + paramByteBuffer.arrayOffset(), i, paramProtectionDomain);
/*      */       }
/*      */ 
/*  886 */       localObject = new byte[i];
/*  887 */       paramByteBuffer.get((byte[])localObject);
/*  888 */       return defineClass(paramString, (byte[])localObject, 0, i, paramProtectionDomain);
/*      */     }
/*      */ 
/*  892 */     paramProtectionDomain = preDefineClass(paramString, paramProtectionDomain);
/*      */ 
/*  894 */     Object localObject = null;
/*  895 */     String str = defineClassSourceLocation(paramProtectionDomain);
/*      */     try
/*      */     {
/*  898 */       localObject = defineClass2(paramString, paramByteBuffer, paramByteBuffer.position(), i, paramProtectionDomain, str);
/*      */     }
/*      */     catch (ClassFormatError localClassFormatError) {
/*  901 */       byte[] arrayOfByte = new byte[i];
/*  902 */       paramByteBuffer.get(arrayOfByte);
/*  903 */       localObject = defineTransformedClass(paramString, arrayOfByte, 0, i, paramProtectionDomain, localClassFormatError, str);
/*      */     }
/*      */ 
/*  907 */     postDefineClass((Class)localObject, paramProtectionDomain);
/*  908 */     return localObject;
/*      */   }
/*      */ 
/*      */   private native Class defineClass0(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ProtectionDomain paramProtectionDomain);
/*      */ 
/*      */   private native Class defineClass1(String paramString1, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ProtectionDomain paramProtectionDomain, String paramString2);
/*      */ 
/*      */   private native Class defineClass2(String paramString1, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, ProtectionDomain paramProtectionDomain, String paramString2);
/*      */ 
/*      */   private boolean checkName(String paramString)
/*      */   {
/*  923 */     if ((paramString == null) || (paramString.length() == 0))
/*  924 */       return true;
/*  925 */     if ((paramString.indexOf('/') != -1) || ((!VM.allowArraySyntax()) && (paramString.charAt(0) == '[')))
/*      */     {
/*  927 */       return false;
/*  928 */     }return true;
/*      */   }
/*      */ 
/*      */   private void checkCerts(String paramString, CodeSource paramCodeSource) {
/*  932 */     int i = paramString.lastIndexOf('.');
/*  933 */     String str = i == -1 ? "" : paramString.substring(0, i);
/*      */ 
/*  935 */     Certificate[] arrayOfCertificate1 = null;
/*  936 */     if (paramCodeSource != null) {
/*  937 */       arrayOfCertificate1 = paramCodeSource.getCertificates();
/*      */     }
/*  939 */     Certificate[] arrayOfCertificate2 = null;
/*  940 */     if (this.parallelLockMap == null) {
/*  941 */       synchronized (this) {
/*  942 */         arrayOfCertificate2 = (Certificate[])this.package2certs.get(str);
/*  943 */         if (arrayOfCertificate2 == null)
/*  944 */           this.package2certs.put(str, arrayOfCertificate1 == null ? nocerts : arrayOfCertificate1);
/*      */       }
/*      */     }
/*      */     else {
/*  948 */       arrayOfCertificate2 = (Certificate[])((ConcurrentHashMap)this.package2certs).putIfAbsent(str, arrayOfCertificate1 == null ? nocerts : arrayOfCertificate1);
/*      */     }
/*      */ 
/*  951 */     if ((arrayOfCertificate2 != null) && (!compareCerts(arrayOfCertificate2, arrayOfCertificate1)))
/*  952 */       throw new SecurityException("class \"" + paramString + "\"'s signer information does not match signer information of other classes in the same package");
/*      */   }
/*      */ 
/*      */   private boolean compareCerts(Certificate[] paramArrayOfCertificate1, Certificate[] paramArrayOfCertificate2)
/*      */   {
/*  965 */     if ((paramArrayOfCertificate2 == null) || (paramArrayOfCertificate2.length == 0)) {
/*  966 */       return paramArrayOfCertificate1.length == 0;
/*      */     }
/*      */ 
/*  970 */     if (paramArrayOfCertificate2.length != paramArrayOfCertificate1.length)
/*  971 */       return false;
/*      */     int i;
/*      */     int k;
/*  976 */     for (int j = 0; j < paramArrayOfCertificate2.length; j++) {
/*  977 */       i = 0;
/*  978 */       for (k = 0; k < paramArrayOfCertificate1.length; k++) {
/*  979 */         if (paramArrayOfCertificate2[j].equals(paramArrayOfCertificate1[k])) {
/*  980 */           i = 1;
/*  981 */           break;
/*      */         }
/*      */       }
/*  984 */       if (i == 0) return false;
/*      */ 
/*      */     }
/*      */ 
/*  988 */     for (j = 0; j < paramArrayOfCertificate1.length; j++) {
/*  989 */       i = 0;
/*  990 */       for (k = 0; k < paramArrayOfCertificate2.length; k++) {
/*  991 */         if (paramArrayOfCertificate1[j].equals(paramArrayOfCertificate2[k])) {
/*  992 */           i = 1;
/*  993 */           break;
/*      */         }
/*      */       }
/*  996 */       if (i == 0) return false;
/*      */     }
/*      */ 
/*  999 */     return true;
/*      */   }
/*      */ 
/*      */   protected final void resolveClass(Class<?> paramClass)
/*      */   {
/* 1019 */     resolveClass0(paramClass);
/*      */   }
/*      */ 
/*      */   private native void resolveClass0(Class paramClass);
/*      */ 
/*      */   protected final Class<?> findSystemClass(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/* 1049 */     ClassLoader localClassLoader = getSystemClassLoader();
/* 1050 */     if (localClassLoader == null) {
/* 1051 */       if (!checkName(paramString))
/* 1052 */         throw new ClassNotFoundException(paramString);
/* 1053 */       Class localClass = findBootstrapClass(paramString);
/* 1054 */       if (localClass == null) {
/* 1055 */         throw new ClassNotFoundException(paramString);
/*      */       }
/* 1057 */       return localClass;
/*      */     }
/* 1059 */     return localClassLoader.loadClass(paramString);
/*      */   }
/*      */ 
/*      */   private Class findBootstrapClassOrNull(String paramString)
/*      */   {
/* 1068 */     if (!checkName(paramString)) return null;
/*      */ 
/* 1070 */     return findBootstrapClass(paramString);
/*      */   }
/*      */ 
/*      */   private native Class findBootstrapClass(String paramString);
/*      */ 
/*      */   protected final Class<?> findLoadedClass(String paramString)
/*      */   {
/* 1091 */     if (!checkName(paramString))
/* 1092 */       return null;
/* 1093 */     return findLoadedClass0(paramString);
/*      */   }
/*      */ 
/*      */   private final native Class findLoadedClass0(String paramString);
/*      */ 
/*      */   protected final void setSigners(Class<?> paramClass, Object[] paramArrayOfObject)
/*      */   {
/* 1111 */     paramClass.setSigners(paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public URL getResource(String paramString)
/*      */   {
/*      */     URL localURL;
/* 1141 */     if (this.parent != null)
/* 1142 */       localURL = this.parent.getResource(paramString);
/*      */     else {
/* 1144 */       localURL = getBootstrapResource(paramString);
/*      */     }
/* 1146 */     if (localURL == null) {
/* 1147 */       localURL = findResource(paramString);
/*      */     }
/* 1149 */     return localURL;
/*      */   }
/*      */ 
/*      */   public Enumeration<URL> getResources(String paramString)
/*      */     throws IOException
/*      */   {
/* 1179 */     Enumeration[] arrayOfEnumeration = new Enumeration[2];
/* 1180 */     if (this.parent != null)
/* 1181 */       arrayOfEnumeration[0] = this.parent.getResources(paramString);
/*      */     else {
/* 1183 */       arrayOfEnumeration[0] = getBootstrapResources(paramString);
/*      */     }
/* 1185 */     arrayOfEnumeration[1] = findResources(paramString);
/*      */ 
/* 1187 */     return new CompoundEnumeration(arrayOfEnumeration);
/*      */   }
/*      */ 
/*      */   protected URL findResource(String paramString)
/*      */   {
/* 1203 */     return null;
/*      */   }
/*      */ 
/*      */   protected Enumeration<URL> findResources(String paramString)
/*      */     throws IOException
/*      */   {
/* 1224 */     return Collections.emptyEnumeration();
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   protected static boolean registerAsParallelCapable()
/*      */   {
/* 1244 */     Class localClass = Reflection.getCallerClass().asSubclass(ClassLoader.class);
/*      */ 
/* 1246 */     return ParallelLoaders.register(localClass);
/*      */   }
/*      */ 
/*      */   public static URL getSystemResource(String paramString)
/*      */   {
/* 1263 */     ClassLoader localClassLoader = getSystemClassLoader();
/* 1264 */     if (localClassLoader == null) {
/* 1265 */       return getBootstrapResource(paramString);
/*      */     }
/* 1267 */     return localClassLoader.getResource(paramString);
/*      */   }
/*      */ 
/*      */   public static Enumeration<URL> getSystemResources(String paramString)
/*      */     throws IOException
/*      */   {
/* 1293 */     ClassLoader localClassLoader = getSystemClassLoader();
/* 1294 */     if (localClassLoader == null) {
/* 1295 */       return getBootstrapResources(paramString);
/*      */     }
/* 1297 */     return localClassLoader.getResources(paramString);
/*      */   }
/*      */ 
/*      */   private static URL getBootstrapResource(String paramString)
/*      */   {
/* 1304 */     URLClassPath localURLClassPath = getBootstrapClassPath();
/* 1305 */     Resource localResource = localURLClassPath.getResource(paramString);
/* 1306 */     return localResource != null ? localResource.getURL() : null;
/*      */   }
/*      */ 
/*      */   private static Enumeration<URL> getBootstrapResources(String paramString)
/*      */     throws IOException
/*      */   {
/* 1315 */     Enumeration localEnumeration = getBootstrapClassPath().getResources(paramString);
/*      */ 
/* 1317 */     return new Enumeration() {
/*      */       public URL nextElement() {
/* 1319 */         return ((Resource)this.val$e.nextElement()).getURL();
/*      */       }
/*      */       public boolean hasMoreElements() {
/* 1322 */         return this.val$e.hasMoreElements();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   static URLClassPath getBootstrapClassPath()
/*      */   {
/* 1329 */     return Launcher.getBootstrapClassPath();
/*      */   }
/*      */ 
/*      */   public InputStream getResourceAsStream(String paramString)
/*      */   {
/* 1348 */     URL localURL = getResource(paramString);
/*      */     try {
/* 1350 */       return localURL != null ? localURL.openStream() : null; } catch (IOException localIOException) {
/*      */     }
/* 1352 */     return null;
/*      */   }
/*      */ 
/*      */   public static InputStream getSystemResourceAsStream(String paramString)
/*      */   {
/* 1370 */     URL localURL = getSystemResource(paramString);
/*      */     try {
/* 1372 */       return localURL != null ? localURL.openStream() : null; } catch (IOException localIOException) {
/*      */     }
/* 1374 */     return null;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public final ClassLoader getParent()
/*      */   {
/* 1408 */     if (this.parent == null)
/* 1409 */       return null;
/* 1410 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1411 */     if (localSecurityManager != null) {
/* 1412 */       checkClassLoaderPermission(this.parent, Reflection.getCallerClass());
/*      */     }
/* 1414 */     return this.parent;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static ClassLoader getSystemClassLoader()
/*      */   {
/* 1474 */     initSystemClassLoader();
/* 1475 */     if (scl == null) {
/* 1476 */       return null;
/*      */     }
/* 1478 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1479 */     if (localSecurityManager != null) {
/* 1480 */       checkClassLoaderPermission(scl, Reflection.getCallerClass());
/*      */     }
/* 1482 */     return scl;
/*      */   }
/*      */ 
/*      */   private static synchronized void initSystemClassLoader() {
/* 1486 */     if (!sclSet) {
/* 1487 */       if (scl != null)
/* 1488 */         throw new IllegalStateException("recursive invocation");
/* 1489 */       Launcher localLauncher = Launcher.getLauncher();
/* 1490 */       if (localLauncher != null) {
/* 1491 */         Throwable localThrowable = null;
/* 1492 */         scl = localLauncher.getClassLoader();
/*      */         try {
/* 1494 */           scl = (ClassLoader)AccessController.doPrivileged(new SystemClassLoaderAction(scl));
/*      */         }
/*      */         catch (PrivilegedActionException localPrivilegedActionException) {
/* 1497 */           localThrowable = localPrivilegedActionException.getCause();
/* 1498 */           if ((localThrowable instanceof InvocationTargetException)) {
/* 1499 */             localThrowable = localThrowable.getCause();
/*      */           }
/*      */         }
/* 1502 */         if (localThrowable != null) {
/* 1503 */           if ((localThrowable instanceof Error)) {
/* 1504 */             throw ((Error)localThrowable);
/*      */           }
/*      */ 
/* 1507 */           throw new Error(localThrowable);
/*      */         }
/*      */       }
/*      */ 
/* 1511 */       sclSet = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isAncestor(ClassLoader paramClassLoader)
/*      */   {
/* 1518 */     ClassLoader localClassLoader = this;
/*      */     do {
/* 1520 */       localClassLoader = localClassLoader.parent;
/* 1521 */       if (paramClassLoader == localClassLoader)
/* 1522 */         return true;
/*      */     }
/* 1524 */     while (localClassLoader != null);
/* 1525 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean needsClassLoaderPermissionCheck(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
/*      */   {
/* 1536 */     if (paramClassLoader1 == paramClassLoader2) {
/* 1537 */       return false;
/*      */     }
/* 1539 */     if (paramClassLoader1 == null) {
/* 1540 */       return false;
/*      */     }
/* 1542 */     return !paramClassLoader2.isAncestor(paramClassLoader1);
/*      */   }
/*      */ 
/*      */   static ClassLoader getClassLoader(Class<?> paramClass)
/*      */   {
/* 1548 */     if (paramClass == null) {
/* 1549 */       return null;
/*      */     }
/*      */ 
/* 1552 */     return paramClass.getClassLoader0();
/*      */   }
/*      */ 
/*      */   static void checkClassLoaderPermission(ClassLoader paramClassLoader, Class<?> paramClass) {
/* 1556 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1557 */     if (localSecurityManager != null)
/*      */     {
/* 1559 */       ClassLoader localClassLoader = getClassLoader(paramClass);
/* 1560 */       if (needsClassLoaderPermissionCheck(localClassLoader, paramClassLoader))
/* 1561 */         localSecurityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Package definePackage(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, URL paramURL)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1624 */     synchronized (this.packages) {
/* 1625 */       Package localPackage = getPackage(paramString1);
/* 1626 */       if (localPackage != null) {
/* 1627 */         throw new IllegalArgumentException(paramString1);
/*      */       }
/* 1629 */       localPackage = new Package(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramURL, this);
/*      */ 
/* 1632 */       this.packages.put(paramString1, localPackage);
/* 1633 */       return localPackage;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Package getPackage(String paramString)
/*      */   {
/*      */     Object localObject1;
/* 1651 */     synchronized (this.packages) {
/* 1652 */       localObject1 = (Package)this.packages.get(paramString);
/*      */     }
/* 1654 */     if (localObject1 == null) {
/* 1655 */       if (this.parent != null)
/* 1656 */         localObject1 = this.parent.getPackage(paramString);
/*      */       else {
/* 1658 */         localObject1 = Package.getSystemPackage(paramString);
/*      */       }
/* 1660 */       if (localObject1 != null) {
/* 1661 */         synchronized (this.packages) {
/* 1662 */           Package localPackage = (Package)this.packages.get(paramString);
/* 1663 */           if (localPackage == null)
/* 1664 */             this.packages.put(paramString, localObject1);
/*      */           else {
/* 1666 */             localObject1 = localPackage;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1671 */     return localObject1;
/*      */   }
/*      */ 
/*      */   protected Package[] getPackages()
/*      */   {
/*      */     HashMap localHashMap;
/* 1685 */     synchronized (this.packages) {
/* 1686 */       localHashMap = new HashMap(this.packages);
/*      */     }
/*      */ 
/* 1689 */     if (this.parent != null)
/* 1690 */       ??? = this.parent.getPackages();
/*      */     else {
/* 1692 */       ??? = Package.getSystemPackages();
/*      */     }
/* 1694 */     if (??? != null) {
/* 1695 */       for (int i = 0; i < ???.length; i++) {
/* 1696 */         String str = ???[i].getName();
/* 1697 */         if (localHashMap.get(str) == null) {
/* 1698 */           localHashMap.put(str, ???[i]);
/*      */         }
/*      */       }
/*      */     }
/* 1702 */     return (Package[])localHashMap.values().toArray(new Package[localHashMap.size()]);
/*      */   }
/*      */ 
/*      */   protected String findLibrary(String paramString)
/*      */   {
/* 1726 */     return null;
/*      */   }
/*      */ 
/*      */   private static String[] initializePath(String paramString)
/*      */   {
/* 1810 */     String str1 = System.getProperty(paramString, "");
/* 1811 */     String str2 = File.pathSeparator;
/* 1812 */     int i = str1.length();
/*      */ 
/* 1815 */     int j = str1.indexOf(str2);
/* 1816 */     int m = 0;
/* 1817 */     while (j >= 0) {
/* 1818 */       m++;
/* 1819 */       j = str1.indexOf(str2, j + 1);
/*      */     }
/*      */ 
/* 1823 */     String[] arrayOfString = new String[m + 1];
/*      */ 
/* 1826 */     m = j = 0;
/* 1827 */     int k = str1.indexOf(str2);
/* 1828 */     while (k >= 0) {
/* 1829 */       if (k - j > 0)
/* 1830 */         arrayOfString[(m++)] = str1.substring(j, k);
/* 1831 */       else if (k - j == 0) {
/* 1832 */         arrayOfString[(m++)] = ".";
/*      */       }
/* 1834 */       j = k + 1;
/* 1835 */       k = str1.indexOf(str2, j);
/*      */     }
/* 1837 */     arrayOfString[m] = str1.substring(j, i);
/* 1838 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   static void loadLibrary(Class paramClass, String paramString, boolean paramBoolean)
/*      */   {
/* 1844 */     ClassLoader localClassLoader = paramClass == null ? null : paramClass.getClassLoader();
/*      */ 
/* 1846 */     if (sys_paths == null) {
/* 1847 */       usr_paths = initializePath("java.library.path");
/* 1848 */       sys_paths = initializePath("sun.boot.library.path");
/*      */     }
/* 1850 */     if (paramBoolean) {
/* 1851 */       if (loadLibrary0(paramClass, new File(paramString))) {
/* 1852 */         return;
/*      */       }
/* 1854 */       throw new UnsatisfiedLinkError("Can't load library: " + paramString);
/*      */     }
/*      */     File localFile;
/* 1856 */     if (localClassLoader != null) {
/* 1857 */       String str = localClassLoader.findLibrary(paramString);
/* 1858 */       if (str != null) {
/* 1859 */         localFile = new File(str);
/* 1860 */         if (!localFile.isAbsolute()) {
/* 1861 */           throw new UnsatisfiedLinkError("ClassLoader.findLibrary failed to return an absolute path: " + str);
/*      */         }
/*      */ 
/* 1864 */         if (loadLibrary0(paramClass, localFile)) {
/* 1865 */           return;
/*      */         }
/* 1867 */         throw new UnsatisfiedLinkError("Can't load " + str);
/*      */       }
/*      */     }
/* 1870 */     for (int i = 0; i < sys_paths.length; i++) {
/* 1871 */       localFile = new File(sys_paths[i], System.mapLibraryName(paramString));
/* 1872 */       if (loadLibrary0(paramClass, localFile)) {
/* 1873 */         return;
/*      */       }
/*      */     }
/* 1876 */     if (localClassLoader != null) {
/* 1877 */       for (i = 0; i < usr_paths.length; i++) {
/* 1878 */         localFile = new File(usr_paths[i], System.mapLibraryName(paramString));
/*      */ 
/* 1880 */         if (loadLibrary0(paramClass, localFile)) {
/* 1881 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1886 */     throw new UnsatisfiedLinkError("no " + paramString + " in java.library.path");
/*      */   }
/*      */ 
/*      */   private static boolean loadLibrary0(Class paramClass, File paramFile) {
/* 1890 */     if (loadLibrary1(paramClass, paramFile)) {
/* 1891 */       return true;
/*      */     }
/* 1893 */     File localFile = ClassLoaderHelper.mapAlternativeName(paramFile);
/* 1894 */     if ((localFile != null) && (loadLibrary1(paramClass, localFile))) {
/* 1895 */       return true;
/*      */     }
/* 1897 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean loadLibrary1(Class paramClass, File paramFile) {
/* 1901 */     int i = AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 1904 */         return this.val$file.exists() ? Boolean.TRUE : null;
/*      */       }
/*      */     }) != null ? 1 : 0;
/*      */ 
/* 1907 */     if (i == 0)
/* 1908 */       return false;
/*      */     String str;
/*      */     try
/*      */     {
/* 1912 */       str = paramFile.getCanonicalPath();
/*      */     } catch (IOException localIOException) {
/* 1914 */       return false;
/*      */     }
/* 1916 */     ClassLoader localClassLoader = paramClass == null ? null : paramClass.getClassLoader();
/*      */ 
/* 1918 */     Vector localVector = localClassLoader != null ? localClassLoader.nativeLibraries : systemNativeLibraries;
/*      */ 
/* 1920 */     synchronized (localVector) {
/* 1921 */       int j = localVector.size();
/* 1922 */       for (int k = 0; k < j; k++) {
/* 1923 */         NativeLibrary localNativeLibrary1 = (NativeLibrary)localVector.elementAt(k);
/* 1924 */         if (str.equals(localNativeLibrary1.name)) {
/* 1925 */           return true;
/*      */         }
/*      */       }
/*      */ 
/* 1929 */       synchronized (loadedLibraryNames) {
/* 1930 */         if (loadedLibraryNames.contains(str)) {
/* 1931 */           throw new UnsatisfiedLinkError("Native Library " + str + " already loaded in another classloader");
/*      */         }
/*      */ 
/* 1948 */         int m = nativeLibraryContext.size();
/* 1949 */         for (int n = 0; n < m; n++) {
/* 1950 */           NativeLibrary localNativeLibrary3 = (NativeLibrary)nativeLibraryContext.elementAt(n);
/* 1951 */           if (str.equals(localNativeLibrary3.name)) {
/* 1952 */             if (localClassLoader == localNativeLibrary3.fromClass.getClassLoader()) {
/* 1953 */               return true;
/*      */             }
/* 1955 */             throw new UnsatisfiedLinkError("Native Library " + str + " is being loaded in another classloader");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1962 */         NativeLibrary localNativeLibrary2 = new NativeLibrary(paramClass, str);
/* 1963 */         nativeLibraryContext.push(localNativeLibrary2);
/*      */         try {
/* 1965 */           localNativeLibrary2.load(str);
/*      */         } finally {
/* 1967 */           nativeLibraryContext.pop();
/*      */         }
/* 1969 */         if (localNativeLibrary2.handle != 0L) {
/* 1970 */           loadedLibraryNames.addElement(str);
/* 1971 */           localVector.addElement(localNativeLibrary2);
/* 1972 */           return true;
/*      */         }
/* 1974 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static long findNative(ClassLoader paramClassLoader, String paramString)
/*      */   {
/* 1981 */     Vector localVector = paramClassLoader != null ? paramClassLoader.nativeLibraries : systemNativeLibraries;
/*      */ 
/* 1983 */     synchronized (localVector) {
/* 1984 */       int i = localVector.size();
/* 1985 */       for (int j = 0; j < i; j++) {
/* 1986 */         NativeLibrary localNativeLibrary = (NativeLibrary)localVector.elementAt(j);
/* 1987 */         long l = localNativeLibrary.find(paramString);
/* 1988 */         if (l != 0L)
/* 1989 */           return l;
/*      */       }
/*      */     }
/* 1992 */     return 0L;
/*      */   }
/*      */ 
/*      */   public void setDefaultAssertionStatus(boolean paramBoolean)
/*      */   {
/* 2035 */     synchronized (this.assertionLock) {
/* 2036 */       if (this.classAssertionStatus == null) {
/* 2037 */         initializeJavaAssertionMaps();
/*      */       }
/* 2039 */       this.defaultAssertionStatus = paramBoolean;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPackageAssertionStatus(String paramString, boolean paramBoolean)
/*      */   {
/* 2082 */     synchronized (this.assertionLock) {
/* 2083 */       if (this.packageAssertionStatus == null) {
/* 2084 */         initializeJavaAssertionMaps();
/*      */       }
/* 2086 */       this.packageAssertionStatus.put(paramString, Boolean.valueOf(paramBoolean));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setClassAssertionStatus(String paramString, boolean paramBoolean)
/*      */   {
/* 2113 */     synchronized (this.assertionLock) {
/* 2114 */       if (this.classAssertionStatus == null) {
/* 2115 */         initializeJavaAssertionMaps();
/*      */       }
/* 2117 */       this.classAssertionStatus.put(paramString, Boolean.valueOf(paramBoolean));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearAssertionStatus()
/*      */   {
/* 2136 */     synchronized (this.assertionLock) {
/* 2137 */       this.classAssertionStatus = new HashMap();
/* 2138 */       this.packageAssertionStatus = new HashMap();
/* 2139 */       this.defaultAssertionStatus = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean desiredAssertionStatus(String paramString)
/*      */   {
/* 2166 */     synchronized (this.assertionLock)
/*      */     {
/* 2171 */       Boolean localBoolean = (Boolean)this.classAssertionStatus.get(paramString);
/* 2172 */       if (localBoolean != null) {
/* 2173 */         return localBoolean.booleanValue();
/*      */       }
/*      */ 
/* 2176 */       int i = paramString.lastIndexOf(".");
/* 2177 */       if (i < 0) {
/* 2178 */         localBoolean = (Boolean)this.packageAssertionStatus.get(null);
/* 2179 */         if (localBoolean != null)
/* 2180 */           return localBoolean.booleanValue();
/*      */       }
/* 2182 */       while (i > 0) {
/* 2183 */         paramString = paramString.substring(0, i);
/* 2184 */         localBoolean = (Boolean)this.packageAssertionStatus.get(paramString);
/* 2185 */         if (localBoolean != null)
/* 2186 */           return localBoolean.booleanValue();
/* 2187 */         i = paramString.lastIndexOf(".", i - 1);
/*      */       }
/*      */ 
/* 2191 */       return this.defaultAssertionStatus;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initializeJavaAssertionMaps()
/*      */   {
/* 2200 */     this.classAssertionStatus = new HashMap();
/* 2201 */     this.packageAssertionStatus = new HashMap();
/* 2202 */     AssertionStatusDirectives localAssertionStatusDirectives = retrieveDirectives();
/*      */ 
/* 2204 */     for (int i = 0; i < localAssertionStatusDirectives.classes.length; i++) {
/* 2205 */       this.classAssertionStatus.put(localAssertionStatusDirectives.classes[i], Boolean.valueOf(localAssertionStatusDirectives.classEnabled[i]));
/*      */     }
/*      */ 
/* 2208 */     for (i = 0; i < localAssertionStatusDirectives.packages.length; i++) {
/* 2209 */       this.packageAssertionStatus.put(localAssertionStatusDirectives.packages[i], Boolean.valueOf(localAssertionStatusDirectives.packageEnabled[i]));
/*      */     }
/*      */ 
/* 2212 */     this.defaultAssertionStatus = localAssertionStatusDirectives.deflt;
/*      */   }
/*      */ 
/*      */   private static native AssertionStatusDirectives retrieveDirectives();
/*      */ 
/*      */   static
/*      */   {
/*  183 */     registerNatives();
/*      */   }
/*      */ 
/*      */   static class NativeLibrary
/*      */   {
/*      */     long handle;
/*      */     private int jniVersion;
/*      */     private Class fromClass;
/*      */     String name;
/*      */ 
/*      */     native void load(String paramString);
/*      */ 
/*      */     native long find(String paramString);
/*      */ 
/*      */     native void unload();
/*      */ 
/*      */     public NativeLibrary(Class paramClass, String paramString)
/*      */     {
/* 1760 */       this.name = paramString;
/* 1761 */       this.fromClass = paramClass;
/*      */     }
/*      */ 
/*      */     protected void finalize() {
/* 1765 */       synchronized (ClassLoader.loadedLibraryNames) {
/* 1766 */         if ((this.fromClass.getClassLoader() != null) && (this.handle != 0L))
/*      */         {
/* 1768 */           int i = ClassLoader.loadedLibraryNames.size();
/* 1769 */           for (int j = 0; j < i; j++) {
/* 1770 */             if (this.name.equals(ClassLoader.loadedLibraryNames.elementAt(j))) {
/* 1771 */               ClassLoader.loadedLibraryNames.removeElementAt(j);
/* 1772 */               break;
/*      */             }
/*      */           }
/*      */ 
/* 1776 */           ClassLoader.nativeLibraryContext.push(this);
/*      */           try {
/* 1778 */             unload();
/*      */           } finally {
/* 1780 */             ClassLoader.nativeLibraryContext.pop();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static Class getFromClass()
/*      */     {
/* 1788 */       return ((NativeLibrary)ClassLoader.nativeLibraryContext.peek()).fromClass;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ParallelLoaders
/*      */   {
/*  198 */     private static final Set<Class<? extends ClassLoader>> loaderTypes = Collections.newSetFromMap(new WeakHashMap());
/*      */ 
/*      */     static boolean register(Class<? extends ClassLoader> paramClass)
/*      */     {
/*  211 */       synchronized (loaderTypes) {
/*  212 */         if (loaderTypes.contains(paramClass.getSuperclass()))
/*      */         {
/*  218 */           loaderTypes.add(paramClass);
/*  219 */           return true;
/*      */         }
/*  221 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*      */     static boolean isRegistered(Class<? extends ClassLoader> paramClass)
/*      */     {
/*  231 */       synchronized (loaderTypes) {
/*  232 */         return loaderTypes.contains(paramClass);
/*      */       }
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/*  202 */       synchronized (loaderTypes) { loaderTypes.add(ClassLoader.class); }
/*      */ 
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ClassLoader
 * JD-Core Version:    0.6.2
 */