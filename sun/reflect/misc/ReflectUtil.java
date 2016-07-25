/*     */ package sun.reflect.misc;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public final class ReflectUtil
/*     */ {
/*     */   public static final String PROXY_PACKAGE = "com.sun.proxy";
/*     */ 
/*     */   public static Class forName(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  44 */     checkPackageAccess(paramString);
/*  45 */     return Class.forName(paramString);
/*     */   }
/*     */ 
/*     */   public static Object newInstance(Class paramClass) throws InstantiationException, IllegalAccessException
/*     */   {
/*  50 */     checkPackageAccess(paramClass);
/*  51 */     return paramClass.newInstance();
/*     */   }
/*     */ 
/*     */   public static void ensureMemberAccess(Class paramClass1, Class paramClass2, Object paramObject, int paramInt)
/*     */     throws IllegalAccessException
/*     */   {
/*  64 */     if ((paramObject == null) && (Modifier.isProtected(paramInt))) {
/*  65 */       int i = paramInt;
/*  66 */       i &= -5;
/*  67 */       i |= 1;
/*     */ 
/*  72 */       Reflection.ensureMemberAccess(paramClass1, paramClass2, paramObject, i);
/*     */       try
/*     */       {
/*  81 */         i &= -2;
/*  82 */         Reflection.ensureMemberAccess(paramClass1, paramClass2, paramObject, i);
/*     */ 
/*  90 */         return;
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException)
/*     */       {
/*  96 */         if (isSubclassOf(paramClass1, paramClass2)) {
/*  97 */           return;
/*     */         }
/*  99 */         throw localIllegalAccessException;
/*     */       }
/*     */     }
/*     */ 
/* 103 */     Reflection.ensureMemberAccess(paramClass1, paramClass2, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   private static boolean isSubclassOf(Class paramClass1, Class paramClass2)
/*     */   {
/* 113 */     while (paramClass1 != null) {
/* 114 */       if (paramClass1 == paramClass2) {
/* 115 */         return true;
/*     */       }
/* 117 */       paramClass1 = paramClass1.getSuperclass();
/*     */     }
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   public static void conservativeCheckMemberAccess(Member paramMember)
/*     */     throws SecurityException
/*     */   {
/* 132 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 133 */     if (localSecurityManager == null) {
/* 134 */       return;
/*     */     }
/*     */ 
/* 144 */     Class localClass = paramMember.getDeclaringClass();
/*     */ 
/* 146 */     checkPackageAccess(localClass);
/*     */ 
/* 148 */     if ((Modifier.isPublic(paramMember.getModifiers())) && (Modifier.isPublic(localClass.getModifiers())))
/*     */     {
/* 150 */       return;
/*     */     }
/*     */ 
/* 153 */     localSecurityManager.checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
/*     */   }
/*     */ 
/*     */   public static void checkPackageAccess(Class<?> paramClass)
/*     */   {
/* 164 */     checkPackageAccess(paramClass.getName());
/* 165 */     if (isNonPublicProxyClass(paramClass))
/* 166 */       checkProxyPackageAccess(paramClass);
/*     */   }
/*     */ 
/*     */   public static void checkPackageAccess(String paramString)
/*     */   {
/* 177 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 178 */     if (localSecurityManager != null) {
/* 179 */       String str = paramString.replace('/', '.');
/* 180 */       if (str.startsWith("[")) {
/* 181 */         i = str.lastIndexOf('[') + 2;
/* 182 */         if ((i > 1) && (i < str.length())) {
/* 183 */           str = str.substring(i);
/*     */         }
/*     */       }
/* 186 */       int i = str.lastIndexOf('.');
/* 187 */       if (i != -1)
/* 188 */         localSecurityManager.checkPackageAccess(str.substring(0, i));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isPackageAccessible(Class paramClass)
/*     */   {
/*     */     try {
/* 195 */       checkPackageAccess(paramClass);
/*     */     } catch (SecurityException localSecurityException) {
/* 197 */       return false;
/*     */     }
/* 199 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean isAncestor(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
/*     */   {
/* 205 */     ClassLoader localClassLoader = paramClassLoader2;
/*     */     do {
/* 207 */       localClassLoader = localClassLoader.getParent();
/* 208 */       if (paramClassLoader1 == localClassLoader)
/* 209 */         return true;
/*     */     }
/* 211 */     while (localClassLoader != null);
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean needsPackageAccessCheck(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
/*     */   {
/* 227 */     if ((paramClassLoader1 == null) || (paramClassLoader1 == paramClassLoader2)) {
/* 228 */       return false;
/*     */     }
/* 230 */     if (paramClassLoader2 == null) {
/* 231 */       return true;
/*     */     }
/* 233 */     return !isAncestor(paramClassLoader1, paramClassLoader2);
/*     */   }
/*     */ 
/*     */   public static void checkProxyPackageAccess(Class<?> paramClass)
/*     */   {
/* 243 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 244 */     if (localSecurityManager != null)
/*     */     {
/* 246 */       if (Proxy.isProxyClass(paramClass))
/* 247 */         for (Class localClass : paramClass.getInterfaces())
/* 248 */           checkPackageAccess(localClass);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void checkProxyPackageAccess(ClassLoader paramClassLoader, Class<?>[] paramArrayOfClass)
/*     */   {
/* 265 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 266 */     if (localSecurityManager != null)
/* 267 */       for (Class<?> localClass : paramArrayOfClass) {
/* 268 */         ClassLoader localClassLoader = localClass.getClassLoader();
/* 269 */         if (needsPackageAccessCheck(paramClassLoader, localClassLoader))
/* 270 */           checkPackageAccess(localClass);
/*     */       }
/*     */   }
/*     */ 
/*     */   public static boolean isNonPublicProxyClass(Class<?> paramClass)
/*     */   {
/* 284 */     String str1 = paramClass.getName();
/* 285 */     int i = str1.lastIndexOf('.');
/* 286 */     String str2 = i != -1 ? str1.substring(0, i) : "";
/* 287 */     return (Proxy.isProxyClass(paramClass)) && (!str2.equals("com.sun.proxy"));
/*     */   }
/*     */ 
/*     */   public static void checkProxyMethod(Object paramObject, Method paramMethod)
/*     */   {
/* 301 */     if ((paramObject == null) || (!Proxy.isProxyClass(paramObject.getClass()))) {
/* 302 */       throw new IllegalArgumentException("Not a Proxy instance");
/*     */     }
/* 304 */     if (Modifier.isStatic(paramMethod.getModifiers())) {
/* 305 */       throw new IllegalArgumentException("Can't handle static method");
/*     */     }
/*     */ 
/* 308 */     Class localClass = paramMethod.getDeclaringClass();
/* 309 */     if (localClass == Object.class) {
/* 310 */       String str = paramMethod.getName();
/* 311 */       if ((str.equals("hashCode")) || (str.equals("equals")) || (str.equals("toString"))) {
/* 312 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 316 */     if (isSuperInterface(paramObject.getClass(), localClass)) {
/* 317 */       return;
/*     */     }
/*     */ 
/* 321 */     throw new IllegalArgumentException("Can't handle: " + paramMethod);
/*     */   }
/*     */ 
/*     */   private static boolean isSuperInterface(Class<?> paramClass1, Class<?> paramClass2) {
/* 325 */     for (Class localClass : paramClass1.getInterfaces()) {
/* 326 */       if (localClass == paramClass2) {
/* 327 */         return true;
/*     */       }
/* 329 */       if (isSuperInterface(localClass, paramClass2)) {
/* 330 */         return true;
/*     */       }
/*     */     }
/* 333 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.misc.ReflectUtil
 * JD-Core Version:    0.6.2
 */