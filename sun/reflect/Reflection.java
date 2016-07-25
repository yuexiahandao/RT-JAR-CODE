/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import sun.misc.VM;
/*     */ 
/*     */ public class Reflection
/*     */ {
/*  50 */   private static volatile Map<Class, String[]> fieldFilterMap = localHashMap;
/*     */ 
/*  52 */   private static volatile Map<Class, String[]> methodFilterMap = new HashMap();
/*     */ 
/*     */   @CallerSensitive
/*     */   public static native Class getCallerClass();
/*     */ 
/*     */   @Deprecated
/*     */   @CallerSensitive
/*     */   public static Class getCallerClass(int paramInt)
/*     */   {
/*  68 */     if (VM.allowGetCallerClass()) {
/*  69 */       return getCallerClass0(paramInt + 1);
/*     */     }
/*  71 */     throw new UnsupportedOperationException("This method has been disabled by a system property");
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   private static native Class getCallerClass0(int paramInt);
/*     */ 
/*     */   private static native int getClassAccessFlags(Class paramClass);
/*     */ 
/*     */   public static boolean quickCheckMemberAccess(Class paramClass, int paramInt)
/*     */   {
/*  95 */     return Modifier.isPublic(getClassAccessFlags(paramClass) & paramInt);
/*     */   }
/*     */ 
/*     */   public static void ensureMemberAccess(Class paramClass1, Class paramClass2, Object paramObject, int paramInt)
/*     */     throws IllegalAccessException
/*     */   {
/* 104 */     if ((paramClass1 == null) || (paramClass2 == null)) {
/* 105 */       throw new InternalError();
/*     */     }
/*     */ 
/* 108 */     if (!verifyMemberAccess(paramClass1, paramClass2, paramObject, paramInt))
/* 109 */       throw new IllegalAccessException("Class " + paramClass1.getName() + " can not access a member of class " + paramClass2.getName() + " with modifiers \"" + Modifier.toString(paramInt) + "\"");
/*     */   }
/*     */ 
/*     */   public static boolean verifyMemberAccess(Class paramClass1, Class paramClass2, Object paramObject, int paramInt)
/*     */   {
/* 130 */     int i = 0;
/* 131 */     boolean bool = false;
/*     */ 
/* 133 */     if (paramClass1 == paramClass2)
/*     */     {
/* 135 */       return true;
/*     */     }
/*     */ 
/* 138 */     if (!Modifier.isPublic(getClassAccessFlags(paramClass2))) {
/* 139 */       bool = isSameClassPackage(paramClass1, paramClass2);
/* 140 */       i = 1;
/* 141 */       if (!bool) {
/* 142 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 148 */     if (Modifier.isPublic(paramInt)) {
/* 149 */       return true;
/*     */     }
/*     */ 
/* 152 */     int j = 0;
/*     */ 
/* 154 */     if (Modifier.isProtected(paramInt))
/*     */     {
/* 156 */       if (isSubclassOf(paramClass1, paramClass2)) {
/* 157 */         j = 1;
/*     */       }
/*     */     }
/*     */ 
/* 161 */     if ((j == 0) && (!Modifier.isPrivate(paramInt))) {
/* 162 */       if (i == 0) {
/* 163 */         bool = isSameClassPackage(paramClass1, paramClass2);
/*     */ 
/* 165 */         i = 1;
/*     */       }
/*     */ 
/* 168 */       if (bool) {
/* 169 */         j = 1;
/*     */       }
/*     */     }
/*     */ 
/* 173 */     if (j == 0) {
/* 174 */       return false;
/*     */     }
/*     */ 
/* 177 */     if (Modifier.isProtected(paramInt))
/*     */     {
/* 179 */       Class localClass = paramObject == null ? paramClass2 : paramObject.getClass();
/* 180 */       if (localClass != paramClass1) {
/* 181 */         if (i == 0) {
/* 182 */           bool = isSameClassPackage(paramClass1, paramClass2);
/* 183 */           i = 1;
/*     */         }
/* 185 */         if ((!bool) && 
/* 186 */           (!isSubclassOf(localClass, paramClass1))) {
/* 187 */           return false;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 193 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean isSameClassPackage(Class paramClass1, Class paramClass2) {
/* 197 */     return isSameClassPackage(paramClass1.getClassLoader(), paramClass1.getName(), paramClass2.getClassLoader(), paramClass2.getName());
/*     */   }
/*     */ 
/*     */   private static boolean isSameClassPackage(ClassLoader paramClassLoader1, String paramString1, ClassLoader paramClassLoader2, String paramString2)
/*     */   {
/* 206 */     if (paramClassLoader1 != paramClassLoader2) {
/* 207 */       return false;
/*     */     }
/* 209 */     int i = paramString1.lastIndexOf('.');
/* 210 */     int j = paramString2.lastIndexOf('.');
/* 211 */     if ((i == -1) || (j == -1))
/*     */     {
/* 214 */       return i == j;
/*     */     }
/* 216 */     int k = 0;
/* 217 */     int m = 0;
/*     */ 
/* 220 */     if (paramString1.charAt(k) == '[') {
/*     */       do
/* 222 */         k++;
/* 223 */       while (paramString1.charAt(k) == '[');
/* 224 */       if (paramString1.charAt(k) != 'L')
/*     */       {
/* 226 */         throw new InternalError("Illegal class name " + paramString1);
/*     */       }
/*     */     }
/* 229 */     if (paramString2.charAt(m) == '[') {
/*     */       do
/* 231 */         m++;
/* 232 */       while (paramString2.charAt(m) == '[');
/* 233 */       if (paramString2.charAt(m) != 'L')
/*     */       {
/* 235 */         throw new InternalError("Illegal class name " + paramString2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 240 */     int n = i - k;
/* 241 */     int i1 = j - m;
/*     */ 
/* 243 */     if (n != i1) {
/* 244 */       return false;
/*     */     }
/* 246 */     return paramString1.regionMatches(false, k, paramString2, m, n);
/*     */   }
/*     */ 
/*     */   static boolean isSubclassOf(Class paramClass1, Class paramClass2)
/*     */   {
/* 254 */     while (paramClass1 != null) {
/* 255 */       if (paramClass1 == paramClass2) {
/* 256 */         return true;
/*     */       }
/* 258 */       paramClass1 = paramClass1.getSuperclass();
/*     */     }
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */   public static synchronized void registerFieldsToFilter(Class paramClass, String[] paramArrayOfString)
/*     */   {
/* 266 */     fieldFilterMap = registerFilter(fieldFilterMap, paramClass, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static synchronized void registerMethodsToFilter(Class paramClass, String[] paramArrayOfString)
/*     */   {
/* 273 */     methodFilterMap = registerFilter(methodFilterMap, paramClass, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   private static Map<Class, String[]> registerFilter(Map<Class, String[]> paramMap, Class paramClass, String[] paramArrayOfString)
/*     */   {
/* 279 */     if (paramMap.get(paramClass) != null) {
/* 280 */       throw new IllegalArgumentException("Filter already registered: " + paramClass);
/*     */     }
/*     */ 
/* 283 */     paramMap = new HashMap(paramMap);
/* 284 */     paramMap.put(paramClass, paramArrayOfString);
/* 285 */     return paramMap;
/*     */   }
/*     */ 
/*     */   public static Field[] filterFields(Class paramClass, Field[] paramArrayOfField)
/*     */   {
/* 290 */     if (fieldFilterMap == null)
/*     */     {
/* 292 */       return paramArrayOfField;
/*     */     }
/* 294 */     return (Field[])filter(paramArrayOfField, (String[])fieldFilterMap.get(paramClass));
/*     */   }
/*     */ 
/*     */   public static Method[] filterMethods(Class paramClass, Method[] paramArrayOfMethod) {
/* 298 */     if (methodFilterMap == null)
/*     */     {
/* 300 */       return paramArrayOfMethod;
/*     */     }
/* 302 */     return (Method[])filter(paramArrayOfMethod, (String[])methodFilterMap.get(paramClass));
/*     */   }
/*     */ 
/*     */   private static Member[] filter(Member[] paramArrayOfMember, String[] paramArrayOfString) {
/* 306 */     if ((paramArrayOfString == null) || (paramArrayOfMember.length == 0)) {
/* 307 */       return paramArrayOfMember;
/*     */     }
/* 309 */     int i = 0;
/*     */     int n;
/* 310 */     for (Member localMember : paramArrayOfMember) {
/* 311 */       n = 0;
/* 312 */       for (Object localObject2 : paramArrayOfString) {
/* 313 */         if (localMember.getName() == localObject2) {
/* 314 */           n = 1;
/* 315 */           break;
/*     */         }
/*     */       }
/* 318 */       if (n == 0) {
/* 319 */         i++;
/*     */       }
/*     */     }
/* 322 */     ??? = (Member[])Array.newInstance(paramArrayOfMember[0].getClass(), i);
/*     */ 
/* 324 */     ??? = 0;
/* 325 */     for (??? : paramArrayOfMember) {
/* 326 */       ??? = 0;
/* 327 */       for (String str : paramArrayOfString) {
/* 328 */         if (((Member)???).getName() == str) {
/* 329 */           ??? = 1;
/* 330 */           break;
/*     */         }
/*     */       }
/* 333 */       if (??? == 0) {
/* 334 */         ???[(???++)] = ???;
/*     */       }
/*     */     }
/* 337 */     return ???;
/*     */   }
/*     */ 
/*     */   public static boolean isCallerSensitive(Method paramMethod)
/*     */   {
/* 345 */     ClassLoader localClassLoader = paramMethod.getDeclaringClass().getClassLoader();
/* 346 */     if ((localClassLoader == null) || (isExtClassLoader(localClassLoader))) {
/* 347 */       return paramMethod.isAnnotationPresent(CallerSensitive.class);
/*     */     }
/* 349 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isExtClassLoader(ClassLoader paramClassLoader) {
/* 353 */     ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/* 354 */     while (localClassLoader != null) {
/* 355 */       if ((localClassLoader.getParent() == null) && (localClassLoader == paramClassLoader)) {
/* 356 */         return true;
/*     */       }
/* 358 */       localClassLoader = localClassLoader.getParent();
/*     */     }
/* 360 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  46 */     HashMap localHashMap = new HashMap();
/*  47 */     localHashMap.put(Reflection.class, new String[] { "fieldFilterMap", "methodFilterMap" });
/*     */ 
/*  49 */     localHashMap.put(System.class, new String[] { "security" });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.Reflection
 * JD-Core Version:    0.6.2
 */