/*     */ package java.lang.reflect;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import sun.misc.ProxyGenerator;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public class Proxy
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2222568056686623797L;
/* 230 */   private static final Class<?>[] constructorParams = { InvocationHandler.class };
/*     */ 
/* 237 */   private static final WeakCache<ClassLoader, Class<?>[], Class<?>> proxyClassCache = new WeakCache(new KeyFactory(null), new ProxyClassFactory(null));
/*     */   protected InvocationHandler h;
/* 461 */   private static final Object key0 = new Object();
/*     */ 
/*     */   private Proxy()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected Proxy(InvocationHandler paramInvocationHandler)
/*     */   {
/* 259 */     doNewInstanceCheck();
/* 260 */     this.h = paramInvocationHandler;
/*     */   }
/*     */ 
/*     */   private void doNewInstanceCheck()
/*     */   {
/* 308 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 309 */     Class localClass = getClass();
/* 310 */     if ((localSecurityManager != null) && (ProxyAccessHelper.needsNewInstanceCheck(localClass)))
/*     */       try {
/* 312 */         localSecurityManager.checkPermission(ProxyAccessHelper.PROXY_PERMISSION);
/*     */       } catch (SecurityException localSecurityException) {
/* 314 */         throw new SecurityException("Not allowed to construct a Proxy instance that implements a non-public interface", localSecurityException);
/*     */       }
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static Class<?> getProxyClass(ClassLoader paramClassLoader, Class<?>[] paramArrayOfClass)
/*     */     throws IllegalArgumentException
/*     */   {
/* 399 */     Class[] arrayOfClass = (Class[])paramArrayOfClass.clone();
/* 400 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 401 */     if (localSecurityManager != null) {
/* 402 */       checkProxyAccess(Reflection.getCallerClass(), paramClassLoader, arrayOfClass);
/*     */     }
/*     */ 
/* 405 */     return getProxyClass0(paramClassLoader, arrayOfClass);
/*     */   }
/*     */ 
/*     */   private static void checkProxyAccess(Class<?> paramClass, ClassLoader paramClassLoader, Class<?>[] paramArrayOfClass)
/*     */   {
/* 430 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 431 */     if (localSecurityManager != null) {
/* 432 */       ClassLoader localClassLoader = paramClass.getClassLoader();
/* 433 */       if ((paramClassLoader == null) && (localClassLoader != null) && 
/* 434 */         (!ProxyAccessHelper.allowNullLoader)) {
/* 435 */         localSecurityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
/*     */       }
/*     */ 
/* 438 */       ReflectUtil.checkProxyPackageAccess(localClassLoader, paramArrayOfClass);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Class<?> getProxyClass0(ClassLoader paramClassLoader, Class<?>[] paramArrayOfClass)
/*     */   {
/* 448 */     if (paramArrayOfClass.length > 65535) {
/* 449 */       throw new IllegalArgumentException("interface limit exceeded");
/*     */     }
/*     */ 
/* 455 */     return (Class)proxyClassCache.get(paramClassLoader, paramArrayOfClass);
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static Object newProxyInstance(ClassLoader paramClassLoader, Class<?>[] paramArrayOfClass, InvocationHandler paramInvocationHandler)
/*     */     throws IllegalArgumentException
/*     */   {
/* 725 */     if (paramInvocationHandler == null) {
/* 726 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 729 */     Class[] arrayOfClass = (Class[])paramArrayOfClass.clone();
/* 730 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 731 */     if (localSecurityManager != null) {
/* 732 */       checkProxyAccess(Reflection.getCallerClass(), paramClassLoader, arrayOfClass);
/*     */     }
/*     */ 
/* 738 */     Class localClass = getProxyClass0(paramClassLoader, arrayOfClass);
/*     */     try
/*     */     {
/* 744 */       Constructor localConstructor = localClass.getConstructor(constructorParams);
/* 745 */       final InvocationHandler localInvocationHandler = paramInvocationHandler;
/* 746 */       if ((localSecurityManager != null) && (ProxyAccessHelper.needsNewInstanceCheck(localClass)))
/*     */       {
/* 749 */         return AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/* 751 */             return Proxy.newInstance(this.val$cons, localInvocationHandler);
/*     */           }
/*     */         });
/*     */       }
/* 755 */       return newInstance(localConstructor, localInvocationHandler);
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException) {
/* 758 */       throw new InternalError(localNoSuchMethodException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object newInstance(Constructor<?> paramConstructor, InvocationHandler paramInvocationHandler) {
/*     */     try {
/* 764 */       return paramConstructor.newInstance(new Object[] { paramInvocationHandler });
/*     */     } catch (IllegalAccessException|InstantiationException localIllegalAccessException) {
/* 766 */       throw new InternalError(localIllegalAccessException.toString());
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 768 */       Throwable localThrowable = localInvocationTargetException.getCause();
/* 769 */       if ((localThrowable instanceof RuntimeException)) {
/* 770 */         throw ((RuntimeException)localThrowable);
/*     */       }
/* 772 */       throw new InternalError(localThrowable.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isProxyClass(Class<?> paramClass)
/*     */   {
/* 792 */     return (Proxy.class.isAssignableFrom(paramClass)) && (proxyClassCache.containsValue(paramClass));
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static InvocationHandler getInvocationHandler(Object paramObject)
/*     */     throws IllegalArgumentException
/*     */   {
/* 810 */     if (!isProxyClass(paramObject.getClass())) {
/* 811 */       throw new IllegalArgumentException("not a proxy instance");
/*     */     }
/*     */ 
/* 814 */     Proxy localProxy = (Proxy)paramObject;
/* 815 */     InvocationHandler localInvocationHandler = localProxy.h;
/* 816 */     if (System.getSecurityManager() != null) {
/* 817 */       Class localClass1 = localInvocationHandler.getClass();
/* 818 */       Class localClass2 = Reflection.getCallerClass();
/* 819 */       if (ReflectUtil.needsPackageAccessCheck(localClass2.getClassLoader(), localClass1.getClassLoader()))
/*     */       {
/* 822 */         ReflectUtil.checkPackageAccess(localClass1);
/*     */       }
/*     */     }
/*     */ 
/* 826 */     return localInvocationHandler;
/*     */   }
/*     */ 
/*     */   private static native Class defineClass0(ClassLoader paramClassLoader, String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */ 
/*     */   private static final class Key1 extends WeakReference<Class<?>>
/*     */   {
/*     */     private final int hash;
/*     */ 
/*     */     Key1(Class<?> paramClass)
/*     */     {
/* 475 */       super();
/* 476 */       this.hash = paramClass.hashCode();
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 481 */       return this.hash;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/*     */       Class localClass;
/* 487 */       return (this == paramObject) || ((paramObject != null) && (paramObject.getClass() == Key1.class) && ((localClass = (Class)get()) != null) && (localClass == ((Key1)paramObject).get()));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Key2 extends WeakReference<Class<?>>
/*     */   {
/*     */     private final int hash;
/*     */     private final WeakReference<Class<?>> ref2;
/*     */ 
/*     */     Key2(Class<?> paramClass1, Class<?> paramClass2)
/*     */     {
/* 503 */       super();
/* 504 */       this.hash = (31 * paramClass1.hashCode() + paramClass2.hashCode());
/* 505 */       this.ref2 = new WeakReference(paramClass2);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 510 */       return this.hash;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/*     */       Class localClass1;
/*     */       Class localClass2;
/* 516 */       return (this == paramObject) || ((paramObject != null) && (paramObject.getClass() == Key2.class) && ((localClass1 = (Class)get()) != null) && (localClass1 == ((Key2)paramObject).get()) && ((localClass2 = (Class)this.ref2.get()) != null) && (localClass2 == ((Key2)paramObject).ref2.get()));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class KeyFactory
/*     */     implements WeakCache.BiFunction<ClassLoader, Class<?>[], Object>
/*     */   {
/*     */     public Object apply(ClassLoader paramClassLoader, Class<?>[] paramArrayOfClass)
/*     */     {
/* 579 */       switch (paramArrayOfClass.length) { case 1:
/* 580 */         return new Proxy.Key1(paramArrayOfClass[0]);
/*     */       case 2:
/* 581 */         return new Proxy.Key2(paramArrayOfClass[0], paramArrayOfClass[1]);
/*     */       case 0:
/* 582 */         return Proxy.key0; }
/* 583 */       return new Proxy.KeyX(paramArrayOfClass);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class KeyX
/*     */   {
/*     */     private final int hash;
/*     */     private final WeakReference<Class<?>>[] refs;
/*     */ 
/*     */     KeyX(Class<?>[] paramArrayOfClass)
/*     */     {
/* 535 */       this.hash = Arrays.hashCode(paramArrayOfClass);
/* 536 */       this.refs = new WeakReference[paramArrayOfClass.length];
/* 537 */       for (int i = 0; i < paramArrayOfClass.length; i++)
/* 538 */         this.refs[i] = new WeakReference(paramArrayOfClass[i]);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 544 */       return this.hash;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 549 */       return (this == paramObject) || ((paramObject != null) && (paramObject.getClass() == KeyX.class) && (equals(this.refs, ((KeyX)paramObject).refs)));
/*     */     }
/*     */ 
/*     */     private static boolean equals(WeakReference<Class<?>>[] paramArrayOfWeakReference1, WeakReference<Class<?>>[] paramArrayOfWeakReference2)
/*     */     {
/* 557 */       if (paramArrayOfWeakReference1.length != paramArrayOfWeakReference2.length) {
/* 558 */         return false;
/*     */       }
/* 560 */       for (int i = 0; i < paramArrayOfWeakReference1.length; i++) {
/* 561 */         Class localClass = (Class)paramArrayOfWeakReference1[i].get();
/* 562 */         if ((localClass == null) || (localClass != paramArrayOfWeakReference2[i].get())) {
/* 563 */           return false;
/*     */         }
/*     */       }
/* 566 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ProxyAccessHelper
/*     */   {
/* 265 */     static final Permission PROXY_PERMISSION = new ReflectPermission("proxyConstructorNewInstance");
/*     */ 
/* 272 */     static final boolean allowNewInstance = getBooleanProperty("sun.reflect.proxy.allowsNewInstance");
/* 273 */     static final boolean allowNullLoader = getBooleanProperty("sun.reflect.proxy.allowsNullLoader");
/*     */ 
/*     */     private static boolean getBooleanProperty(String paramString)
/*     */     {
/* 277 */       String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public String run() {
/* 279 */           return System.getProperty(this.val$key);
/*     */         }
/*     */       });
/* 282 */       return Boolean.valueOf(str).booleanValue();
/*     */     }
/*     */ 
/*     */     static boolean needsNewInstanceCheck(Class<?> paramClass) {
/* 286 */       if ((!Proxy.isProxyClass(paramClass)) || (allowNewInstance)) {
/* 287 */         return false;
/*     */       }
/*     */ 
/* 290 */       if (ReflectUtil.isNonPublicProxyClass(paramClass)) {
/* 291 */         for (Class localClass : paramClass.getInterfaces()) {
/* 292 */           if (!Modifier.isPublic(localClass.getModifiers())) {
/* 293 */             return true;
/*     */           }
/*     */         }
/*     */       }
/* 297 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ProxyClassFactory
/*     */     implements WeakCache.BiFunction<ClassLoader, Class<?>[], Class<?>>
/*     */   {
/*     */     private static final String proxyClassNamePrefix = "$Proxy";
/* 599 */     private static final AtomicLong nextUniqueNumber = new AtomicLong();
/*     */ 
/*     */     public Class<?> apply(ClassLoader paramClassLoader, Class<?>[] paramArrayOfClass)
/*     */     {
/* 604 */       IdentityHashMap localIdentityHashMap = new IdentityHashMap(paramArrayOfClass.length);
/* 605 */       for (Object localObject2 : paramArrayOfClass)
/*     */       {
/* 610 */         localObject3 = null;
/*     */         try {
/* 612 */           localObject3 = Class.forName(localObject2.getName(), false, paramClassLoader);
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/*     */         }
/* 615 */         if (localObject3 != localObject2) {
/* 616 */           throw new IllegalArgumentException(localObject2 + " is not visible from class loader");
/*     */         }
/*     */ 
/* 623 */         if (!((Class)localObject3).isInterface()) {
/* 624 */           throw new IllegalArgumentException(((Class)localObject3).getName() + " is not an interface");
/*     */         }
/*     */ 
/* 630 */         if (localIdentityHashMap.put(localObject3, Boolean.TRUE) != null) {
/* 631 */           throw new IllegalArgumentException("repeated interface: " + ((Class)localObject3).getName());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 636 */       ??? = null;
/*     */ 
/* 643 */       for (localObject3 : paramArrayOfClass) {
/* 644 */         int m = ((Class)localObject3).getModifiers();
/* 645 */         if (!Modifier.isPublic(m)) {
/* 646 */           String str2 = ((Class)localObject3).getName();
/* 647 */           int n = str2.lastIndexOf('.');
/* 648 */           String str3 = n == -1 ? "" : str2.substring(0, n + 1);
/* 649 */           if (??? == null)
/* 650 */             ??? = str3;
/* 651 */           else if (!str3.equals(???)) {
/* 652 */             throw new IllegalArgumentException("non-public interfaces from different packages");
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 658 */       if (??? == null)
/*     */       {
/* 660 */         ??? = "com.sun.proxy.";
/*     */       }
/*     */ 
/* 666 */       long l = nextUniqueNumber.getAndIncrement();
/* 667 */       String str1 = (String)??? + "$Proxy" + l;
/*     */ 
/* 672 */       Object localObject3 = ProxyGenerator.generateProxyClass(str1, paramArrayOfClass);
/*     */       try
/*     */       {
/* 675 */         return Proxy.defineClass0(paramClassLoader, str1, (byte[])localObject3, 0, localObject3.length);
/*     */       }
/*     */       catch (ClassFormatError localClassFormatError)
/*     */       {
/* 685 */         throw new IllegalArgumentException(localClassFormatError.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.Proxy
 * JD-Core Version:    0.6.2
 */