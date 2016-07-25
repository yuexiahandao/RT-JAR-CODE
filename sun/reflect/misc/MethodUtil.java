/*     */ package sun.reflect.misc;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.AllPermission;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.SecureClassLoader;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import sun.misc.IOUtils;
/*     */ 
/*     */ public final class MethodUtil extends SecureClassLoader
/*     */ {
/*  83 */   private static String MISC_PKG = "sun.reflect.misc.";
/*  84 */   private static String TRAMPOLINE = MISC_PKG + "Trampoline";
/*  85 */   private static Method bounce = getTrampoline();
/*     */ 
/*     */   public static Method getMethod(Class<?> paramClass, String paramString, Class[] paramArrayOfClass)
/*     */     throws NoSuchMethodException
/*     */   {
/*  93 */     ReflectUtil.checkPackageAccess(paramClass);
/*  94 */     return paramClass.getMethod(paramString, paramArrayOfClass);
/*     */   }
/*     */ 
/*     */   public static Method[] getMethods(Class paramClass) {
/*  98 */     ReflectUtil.checkPackageAccess(paramClass);
/*  99 */     return paramClass.getMethods();
/*     */   }
/*     */ 
/*     */   public static Method[] getPublicMethods(Class paramClass)
/*     */   {
/* 110 */     if (System.getSecurityManager() == null) {
/* 111 */       return paramClass.getMethods();
/*     */     }
/* 113 */     HashMap localHashMap = new HashMap();
/* 114 */     while (paramClass != null) {
/* 115 */       boolean bool = getInternalPublicMethods(paramClass, localHashMap);
/* 116 */       if (bool) {
/*     */         break;
/*     */       }
/* 119 */       getInterfaceMethods(paramClass, localHashMap);
/* 120 */       paramClass = paramClass.getSuperclass();
/*     */     }
/* 122 */     return (Method[])localHashMap.values().toArray(new Method[localHashMap.size()]);
/*     */   }
/*     */ 
/*     */   private static void getInterfaceMethods(Class paramClass, Map<Signature, Method> paramMap)
/*     */   {
/* 130 */     Class[] arrayOfClass = paramClass.getInterfaces();
/* 131 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 132 */       Class localClass = arrayOfClass[i];
/* 133 */       boolean bool = getInternalPublicMethods(localClass, paramMap);
/* 134 */       if (!bool)
/* 135 */         getInterfaceMethods(localClass, paramMap);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean getInternalPublicMethods(Class paramClass, Map<Signature, Method> paramMap)
/*     */   {
/* 146 */     Method[] arrayOfMethod = null;
/*     */     try
/*     */     {
/* 153 */       if (!Modifier.isPublic(paramClass.getModifiers())) {
/* 154 */         return false;
/*     */       }
/* 156 */       if (!ReflectUtil.isPackageAccessible(paramClass)) {
/* 157 */         return false;
/*     */       }
/*     */ 
/* 160 */       arrayOfMethod = paramClass.getMethods();
/*     */     } catch (SecurityException localSecurityException) {
/* 162 */       return false;
/*     */     }
/*     */ 
/* 171 */     boolean bool = true;
/*     */     Class localClass;
/* 172 */     for (int i = 0; i < arrayOfMethod.length; i++) {
/* 173 */       localClass = arrayOfMethod[i].getDeclaringClass();
/* 174 */       if (!Modifier.isPublic(localClass.getModifiers())) {
/* 175 */         bool = false;
/* 176 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 180 */     if (bool)
/*     */     {
/* 185 */       for (i = 0; i < arrayOfMethod.length; i++) {
/* 186 */         addMethod(paramMap, arrayOfMethod[i]);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 193 */       for (i = 0; i < arrayOfMethod.length; i++) {
/* 194 */         localClass = arrayOfMethod[i].getDeclaringClass();
/* 195 */         if (paramClass.equals(localClass)) {
/* 196 */           addMethod(paramMap, arrayOfMethod[i]);
/*     */         }
/*     */       }
/*     */     }
/* 200 */     return bool;
/*     */   }
/*     */ 
/*     */   private static void addMethod(Map<Signature, Method> paramMap, Method paramMethod) {
/* 204 */     Signature localSignature = new Signature(paramMethod);
/* 205 */     if (!paramMap.containsKey(localSignature)) {
/* 206 */       paramMap.put(localSignature, paramMethod);
/* 207 */     } else if (!paramMethod.getDeclaringClass().isInterface())
/*     */     {
/* 211 */       Method localMethod = (Method)paramMap.get(localSignature);
/* 212 */       if (localMethod.getDeclaringClass().isInterface())
/* 213 */         paramMap.put(localSignature, paramMethod);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object invoke(Method paramMethod, Object paramObject, Object[] paramArrayOfObject)
/*     */     throws InvocationTargetException, IllegalAccessException
/*     */   {
/*     */     try
/*     */     {
/* 279 */       return bounce.invoke(null, new Object[] { paramMethod, paramObject, paramArrayOfObject });
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 281 */       Throwable localThrowable = localInvocationTargetException.getCause();
/*     */ 
/* 283 */       if ((localThrowable instanceof InvocationTargetException))
/* 284 */         throw ((InvocationTargetException)localThrowable);
/* 285 */       if ((localThrowable instanceof IllegalAccessException))
/* 286 */         throw ((IllegalAccessException)localThrowable);
/* 287 */       if ((localThrowable instanceof RuntimeException))
/* 288 */         throw ((RuntimeException)localThrowable);
/* 289 */       if ((localThrowable instanceof Error)) {
/* 290 */         throw ((Error)localThrowable);
/*     */       }
/* 292 */       throw new Error("Unexpected invocation error", localThrowable);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 296 */       throw new Error("Unexpected invocation error", localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Method getTrampoline() {
/*     */     try {
/* 302 */       return (Method)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Method run() throws Exception {
/* 305 */           Class localClass = MethodUtil.access$000();
/* 306 */           Class[] arrayOfClass = { Method.class, Object.class, [Ljava.lang.Object.class };
/*     */ 
/* 309 */           Method localMethod = localClass.getDeclaredMethod("invoke", arrayOfClass);
/* 310 */           localMethod.setAccessible(true);
/* 311 */           return localMethod;
/*     */         } } );
/*     */     } catch (Exception localException) {
/*     */     }
/* 315 */     throw new InternalError("bouncer cannot be found");
/*     */   }
/*     */ 
/*     */   protected synchronized Class loadClass(String paramString, boolean paramBoolean)
/*     */     throws ClassNotFoundException
/*     */   {
/* 324 */     ReflectUtil.checkPackageAccess(paramString);
/* 325 */     Class localClass = findLoadedClass(paramString);
/* 326 */     if (localClass == null) {
/*     */       try {
/* 328 */         localClass = findClass(paramString);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {
/*     */       }
/* 332 */       if (localClass == null) {
/* 333 */         localClass = getParent().loadClass(paramString);
/*     */       }
/*     */     }
/* 336 */     if (paramBoolean) {
/* 337 */       resolveClass(localClass);
/*     */     }
/* 339 */     return localClass;
/*     */   }
/*     */ 
/*     */   protected Class findClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 346 */     if (!paramString.startsWith(MISC_PKG)) {
/* 347 */       throw new ClassNotFoundException(paramString);
/*     */     }
/* 349 */     String str = paramString.replace('.', '/').concat(".class");
/* 350 */     URL localURL = getResource(str);
/* 351 */     if (localURL != null) {
/*     */       try {
/* 353 */         return defineClass(paramString, localURL);
/*     */       } catch (IOException localIOException) {
/* 355 */         throw new ClassNotFoundException(paramString, localIOException);
/*     */       }
/*     */     }
/* 358 */     throw new ClassNotFoundException(paramString);
/*     */   }
/*     */ 
/*     */   private Class defineClass(String paramString, URL paramURL)
/*     */     throws IOException
/*     */   {
/* 367 */     byte[] arrayOfByte = getBytes(paramURL);
/* 368 */     CodeSource localCodeSource = new CodeSource(null, (Certificate[])null);
/* 369 */     if (!paramString.equals(TRAMPOLINE)) {
/* 370 */       throw new IOException("MethodUtil: bad name " + paramString);
/*     */     }
/* 372 */     return defineClass(paramString, arrayOfByte, 0, arrayOfByte.length, localCodeSource);
/*     */   }
/*     */ 
/*     */   private static byte[] getBytes(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 380 */     URLConnection localURLConnection = paramURL.openConnection();
/* 381 */     if ((localURLConnection instanceof HttpURLConnection)) {
/* 382 */       HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURLConnection;
/* 383 */       int j = localHttpURLConnection.getResponseCode();
/* 384 */       if (j >= 400) {
/* 385 */         throw new IOException("open HTTP connection failed.");
/*     */       }
/*     */     }
/* 388 */     int i = localURLConnection.getContentLength();
/* 389 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(localURLConnection.getInputStream());
/*     */     byte[] arrayOfByte;
/*     */     try { arrayOfByte = IOUtils.readFully(localBufferedInputStream, i, true);
/*     */     } finally {
/* 395 */       localBufferedInputStream.close();
/*     */     }
/* 397 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   protected PermissionCollection getPermissions(CodeSource paramCodeSource)
/*     */   {
/* 403 */     PermissionCollection localPermissionCollection = super.getPermissions(paramCodeSource);
/* 404 */     localPermissionCollection.add(new AllPermission());
/* 405 */     return localPermissionCollection;
/*     */   }
/*     */ 
/*     */   private static Class getTrampolineClass() {
/*     */     try {
/* 410 */       return Class.forName(TRAMPOLINE, true, new MethodUtil());
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*     */     }
/* 413 */     return null;
/*     */   }
/*     */ 
/*     */   private static class Signature
/*     */   {
/*     */     private String methodName;
/*     */     private Class[] argClasses;
/* 226 */     private volatile int hashCode = 0;
/*     */ 
/*     */     Signature(Method paramMethod) {
/* 229 */       this.methodName = paramMethod.getName();
/* 230 */       this.argClasses = paramMethod.getParameterTypes();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 234 */       if (this == paramObject) {
/* 235 */         return true;
/*     */       }
/* 237 */       Signature localSignature = (Signature)paramObject;
/* 238 */       if (!this.methodName.equals(localSignature.methodName)) {
/* 239 */         return false;
/*     */       }
/* 241 */       if (this.argClasses.length != localSignature.argClasses.length) {
/* 242 */         return false;
/*     */       }
/* 244 */       for (int i = 0; i < this.argClasses.length; i++) {
/* 245 */         if (this.argClasses[i] != localSignature.argClasses[i]) {
/* 246 */           return false;
/*     */         }
/*     */       }
/* 249 */       return true;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 257 */       if (this.hashCode == 0) {
/* 258 */         int i = 17;
/* 259 */         i = 37 * i + this.methodName.hashCode();
/* 260 */         if (this.argClasses != null) {
/* 261 */           for (int j = 0; j < this.argClasses.length; j++) {
/* 262 */             i = 37 * i + (this.argClasses[j] == null ? 0 : this.argClasses[j].hashCode());
/*     */           }
/*     */         }
/*     */ 
/* 266 */         this.hashCode = i;
/*     */       }
/* 268 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.misc.MethodUtil
 * JD-Core Version:    0.6.2
 */