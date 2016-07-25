/*     */ package sun.rmi.server;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.StubNotFoundException;
/*     */ import java.rmi.server.LogStream;
/*     */ import java.rmi.server.RemoteObjectInvocationHandler;
/*     */ import java.rmi.server.RemoteRef;
/*     */ import java.rmi.server.RemoteStub;
/*     */ import java.rmi.server.Skeleton;
/*     */ import java.rmi.server.SkeletonNotFoundException;
/*     */ import java.security.AccessController;
/*     */ import java.security.DigestOutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public final class Util
/*     */ {
/*  69 */   static final int logLevel = LogStream.parseLevel((String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.server.logLevel")));
/*     */ 
/*  74 */   public static final Log serverRefLog = Log.getLog("sun.rmi.server.ref", "transport", logLevel);
/*     */ 
/*  78 */   private static final boolean ignoreStubClasses = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("java.rmi.server.ignoreStubClasses"))).booleanValue();
/*     */ 
/*  84 */   private static final Map<Class<?>, Void> withoutStubs = Collections.synchronizedMap(new WeakHashMap(11));
/*     */ 
/*  88 */   private static final Class[] stubConsParamTypes = { RemoteRef.class };
/*     */ 
/*     */   public static Remote createProxy(Class<?> paramClass, RemoteRef paramRemoteRef, boolean paramBoolean)
/*     */     throws StubNotFoundException
/*     */   {
/*     */     Class localClass;
/*     */     try
/*     */     {
/* 130 */       localClass = getRemoteClass(paramClass);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 132 */       throw new StubNotFoundException("object does not implement a remote interface: " + paramClass.getName());
/*     */     }
/*     */ 
/* 137 */     if ((paramBoolean) || ((!ignoreStubClasses) && (stubClassExists(localClass))))
/*     */     {
/* 140 */       return createStub(localClass, paramRemoteRef);
/*     */     }
/*     */ 
/* 143 */     ClassLoader localClassLoader = paramClass.getClassLoader();
/* 144 */     Class[] arrayOfClass = getRemoteInterfaces(paramClass);
/* 145 */     RemoteObjectInvocationHandler localRemoteObjectInvocationHandler = new RemoteObjectInvocationHandler(paramRemoteRef);
/*     */     try
/*     */     {
/* 151 */       return (Remote)Proxy.newProxyInstance(localClassLoader, arrayOfClass, localRemoteObjectInvocationHandler);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 155 */       throw new StubNotFoundException("unable to create proxy", localIllegalArgumentException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean stubClassExists(Class<?> paramClass)
/*     */   {
/* 166 */     if (!withoutStubs.containsKey(paramClass)) {
/*     */       try {
/* 168 */         Class.forName(paramClass.getName() + "_Stub", false, paramClass.getClassLoader());
/*     */ 
/* 171 */         return true;
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {
/* 174 */         withoutStubs.put(paramClass, null);
/*     */       }
/*     */     }
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   private static Class<?> getRemoteClass(Class<?> paramClass)
/*     */     throws ClassNotFoundException
/*     */   {
/* 188 */     while (paramClass != null) {
/* 189 */       Class[] arrayOfClass = paramClass.getInterfaces();
/* 190 */       for (int i = arrayOfClass.length - 1; i >= 0; i--) {
/* 191 */         if (Remote.class.isAssignableFrom(arrayOfClass[i]))
/* 192 */           return paramClass;
/*     */       }
/* 194 */       paramClass = paramClass.getSuperclass();
/*     */     }
/* 196 */     throw new ClassNotFoundException("class does not implement java.rmi.Remote");
/*     */   }
/*     */ 
/*     */   private static Class<?>[] getRemoteInterfaces(Class<?> paramClass)
/*     */   {
/* 210 */     ArrayList localArrayList = new ArrayList();
/* 211 */     getRemoteInterfaces(localArrayList, paramClass);
/* 212 */     return (Class[])localArrayList.toArray(new Class[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private static void getRemoteInterfaces(ArrayList<Class<?>> paramArrayList, Class<?> paramClass)
/*     */   {
/* 224 */     Class localClass1 = paramClass.getSuperclass();
/* 225 */     if (localClass1 != null) {
/* 226 */       getRemoteInterfaces(paramArrayList, localClass1);
/*     */     }
/*     */ 
/* 229 */     Class[] arrayOfClass = paramClass.getInterfaces();
/* 230 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 231 */       Class localClass2 = arrayOfClass[i];
/*     */ 
/* 237 */       if ((Remote.class.isAssignableFrom(localClass2)) && 
/* 238 */         (!paramArrayList.contains(localClass2))) {
/* 239 */         Method[] arrayOfMethod = localClass2.getMethods();
/* 240 */         for (int j = 0; j < arrayOfMethod.length; j++) {
/* 241 */           checkMethod(arrayOfMethod[j]);
/*     */         }
/* 243 */         paramArrayList.add(localClass2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkMethod(Method paramMethod)
/*     */   {
/* 257 */     Class[] arrayOfClass = paramMethod.getExceptionTypes();
/* 258 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 259 */       if (arrayOfClass[i].isAssignableFrom(RemoteException.class))
/* 260 */         return;
/*     */     }
/* 262 */     throw new IllegalArgumentException("illegal remote method encountered: " + paramMethod);
/*     */   }
/*     */ 
/*     */   private static RemoteStub createStub(Class<?> paramClass, RemoteRef paramRemoteRef)
/*     */     throws StubNotFoundException
/*     */   {
/* 278 */     String str = paramClass.getName() + "_Stub";
/*     */     try
/*     */     {
/* 286 */       Class localClass = Class.forName(str, false, paramClass.getClassLoader());
/*     */ 
/* 288 */       Constructor localConstructor = localClass.getConstructor(stubConsParamTypes);
/* 289 */       return (RemoteStub)localConstructor.newInstance(new Object[] { paramRemoteRef });
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 292 */       throw new StubNotFoundException("Stub class not found: " + str, localClassNotFoundException);
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException) {
/* 295 */       throw new StubNotFoundException("Stub class missing constructor: " + str, localNoSuchMethodException);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 298 */       throw new StubNotFoundException("Can't create instance of stub class: " + str, localInstantiationException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 301 */       throw new StubNotFoundException("Stub class constructor not public: " + str, localIllegalAccessException);
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException) {
/* 304 */       throw new StubNotFoundException("Exception creating instance of stub class: " + str, localInvocationTargetException);
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {
/* 307 */       throw new StubNotFoundException("Stub class not instance of RemoteStub: " + str, localClassCastException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Skeleton createSkeleton(Remote paramRemote)
/*     */     throws SkeletonNotFoundException
/*     */   {
/*     */     Class localClass1;
/*     */     try
/*     */     {
/* 320 */       localClass1 = getRemoteClass(paramRemote.getClass());
/*     */     } catch (ClassNotFoundException localClassNotFoundException1) {
/* 322 */       throw new SkeletonNotFoundException("object does not implement a remote interface: " + paramRemote.getClass().getName());
/*     */     }
/*     */ 
/* 328 */     String str = localClass1.getName() + "_Skel";
/*     */     try {
/* 330 */       Class localClass2 = Class.forName(str, false, localClass1.getClassLoader());
/*     */ 
/* 332 */       return (Skeleton)localClass2.newInstance();
/*     */     } catch (ClassNotFoundException localClassNotFoundException2) {
/* 334 */       throw new SkeletonNotFoundException("Skeleton class not found: " + str, localClassNotFoundException2);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 337 */       throw new SkeletonNotFoundException("Can't create skeleton: " + str, localInstantiationException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 340 */       throw new SkeletonNotFoundException("No public constructor: " + str, localIllegalAccessException);
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {
/* 343 */       throw new SkeletonNotFoundException("Skeleton not of correct class: " + str, localClassCastException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static long computeMethodHash(Method paramMethod)
/*     */   {
/* 354 */     long l = 0L;
/* 355 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(127);
/*     */     try {
/* 357 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 358 */       DataOutputStream localDataOutputStream = new DataOutputStream(new DigestOutputStream(localByteArrayOutputStream, localMessageDigest));
/*     */ 
/* 361 */       String str = getMethodNameAndDescriptor(paramMethod);
/* 362 */       if (serverRefLog.isLoggable(Log.VERBOSE)) {
/* 363 */         serverRefLog.log(Log.VERBOSE, "string used for method hash: \"" + str + "\"");
/*     */       }
/*     */ 
/* 366 */       localDataOutputStream.writeUTF(str);
/*     */ 
/* 369 */       localDataOutputStream.flush();
/* 370 */       byte[] arrayOfByte = localMessageDigest.digest();
/* 371 */       for (int i = 0; i < Math.min(8, arrayOfByte.length); i++)
/* 372 */         l += ((arrayOfByte[i] & 0xFF) << i * 8);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 376 */       l = -1L;
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 378 */       throw new SecurityException(localNoSuchAlgorithmException.getMessage());
/*     */     }
/* 380 */     return l;
/*     */   }
/*     */ 
/*     */   private static String getMethodNameAndDescriptor(Method paramMethod)
/*     */   {
/* 392 */     StringBuffer localStringBuffer = new StringBuffer(paramMethod.getName());
/* 393 */     localStringBuffer.append('(');
/* 394 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 395 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 396 */       localStringBuffer.append(getTypeDescriptor(arrayOfClass[i]));
/*     */     }
/* 398 */     localStringBuffer.append(')');
/* 399 */     Class localClass = paramMethod.getReturnType();
/* 400 */     if (localClass == Void.TYPE)
/* 401 */       localStringBuffer.append('V');
/*     */     else {
/* 403 */       localStringBuffer.append(getTypeDescriptor(localClass));
/*     */     }
/* 405 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String getTypeDescriptor(Class<?> paramClass)
/*     */   {
/* 413 */     if (paramClass.isPrimitive()) {
/* 414 */       if (paramClass == Integer.TYPE)
/* 415 */         return "I";
/* 416 */       if (paramClass == Boolean.TYPE)
/* 417 */         return "Z";
/* 418 */       if (paramClass == Byte.TYPE)
/* 419 */         return "B";
/* 420 */       if (paramClass == Character.TYPE)
/* 421 */         return "C";
/* 422 */       if (paramClass == Short.TYPE)
/* 423 */         return "S";
/* 424 */       if (paramClass == Long.TYPE)
/* 425 */         return "J";
/* 426 */       if (paramClass == Float.TYPE)
/* 427 */         return "F";
/* 428 */       if (paramClass == Double.TYPE)
/* 429 */         return "D";
/* 430 */       if (paramClass == Void.TYPE) {
/* 431 */         return "V";
/*     */       }
/* 433 */       throw new Error("unrecognized primitive type: " + paramClass);
/*     */     }
/* 435 */     if (paramClass.isArray())
/*     */     {
/* 443 */       return paramClass.getName().replace('.', '/');
/*     */     }
/* 445 */     return "L" + paramClass.getName().replace('.', '/') + ";";
/*     */   }
/*     */ 
/*     */   public static String getUnqualifiedName(Class<?> paramClass)
/*     */   {
/* 458 */     String str = paramClass.getName();
/* 459 */     return str.substring(str.lastIndexOf('.') + 1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.Util
 * JD-Core Version:    0.6.2
 */