/*     */ package sun.invoke.util;
/*     */ 
/*     */ import java.lang.invoke.MethodType;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ public class VerifyAccess
/*     */ {
/*     */   private static final int PACKAGE_ONLY = 0;
/*     */   private static final int PACKAGE_ALLOWED = 8;
/*     */   private static final int PROTECTED_OR_PACKAGE_ALLOWED = 12;
/*     */   private static final int ALL_ACCESS_MODES = 7;
/*     */   private static final boolean ALLOW_NESTMATE_ACCESS = false;
/*     */ 
/*     */   public static boolean isMemberAccessible(Class<?> paramClass1, Class<?> paramClass2, int paramInt1, Class<?> paramClass3, int paramInt2)
/*     */   {
/*  89 */     if (paramInt2 == 0) return false;
/*  90 */     assert (((paramInt2 & 0x1) != 0) && ((paramInt2 & 0xFFFFFFF0) == 0));
/*     */ 
/*  93 */     if (!isClassAccessible(paramClass1, paramClass3, paramInt2)) {
/*  94 */       return false;
/*     */     }
/*     */ 
/*  97 */     if ((paramClass2 == paramClass3) && ((paramInt2 & 0x2) != 0))
/*     */     {
/*  99 */       return true;
/* 100 */     }switch (paramInt1 & 0x7) {
/*     */     case 1:
/* 102 */       return true;
/*     */     case 4:
/* 104 */       if (((paramInt2 & 0xC) != 0) && (isSamePackage(paramClass2, paramClass3)))
/*     */       {
/* 106 */         return true;
/* 107 */       }if ((paramInt2 & 0x4) == 0)
/* 108 */         return false;
/* 109 */       if (((paramInt1 & 0x8) != 0) && (!isRelatedClass(paramClass1, paramClass3)))
/*     */       {
/* 111 */         return false;
/* 112 */       }if (((paramInt2 & 0x4) != 0) && (isSuperClass(paramClass2, paramClass3)))
/*     */       {
/* 114 */         return true;
/* 115 */       }return false;
/*     */     case 0:
/* 117 */       return ((paramInt2 & 0x8) != 0) && (isSamePackage(paramClass2, paramClass3));
/*     */     case 2:
/* 121 */       return false;
/*     */     case 3:
/*     */     }
/*     */ 
/* 125 */     throw new IllegalArgumentException("bad modifiers: " + Modifier.toString(paramInt1));
/*     */   }
/*     */ 
/*     */   static boolean isRelatedClass(Class<?> paramClass1, Class<?> paramClass2)
/*     */   {
/* 130 */     return (paramClass1 == paramClass2) || (paramClass1.isAssignableFrom(paramClass2)) || (paramClass2.isAssignableFrom(paramClass1));
/*     */   }
/*     */ 
/*     */   static boolean isSuperClass(Class<?> paramClass1, Class<?> paramClass2)
/*     */   {
/* 136 */     return paramClass1.isAssignableFrom(paramClass2);
/*     */   }
/*     */ 
/*     */   public static boolean isClassAccessible(Class<?> paramClass1, Class<?> paramClass2, int paramInt)
/*     */   {
/* 152 */     if (paramInt == 0) return false;
/* 153 */     assert (((paramInt & 0x1) != 0) && ((paramInt & 0xFFFFFFF0) == 0));
/*     */ 
/* 155 */     int i = paramClass1.getModifiers();
/* 156 */     if (Modifier.isPublic(i))
/* 157 */       return true;
/* 158 */     if (((paramInt & 0x8) != 0) && (isSamePackage(paramClass2, paramClass1)))
/*     */     {
/* 160 */       return true;
/* 161 */     }return false;
/*     */   }
/*     */ 
/*     */   public static boolean isTypeVisible(Class<?> paramClass1, Class<?> paramClass2)
/*     */   {
/* 171 */     if (paramClass1 == paramClass2) return true;
/* 172 */     while (paramClass1.isArray()) paramClass1 = paramClass1.getComponentType();
/* 173 */     if ((paramClass1.isPrimitive()) || (paramClass1 == Object.class)) return true;
/* 174 */     ClassLoader localClassLoader1 = paramClass1.getClassLoader();
/* 175 */     if (localClassLoader1 == null) return true;
/* 176 */     ClassLoader localClassLoader2 = paramClass2.getClassLoader();
/* 177 */     if (localClassLoader2 == null) return false;
/* 178 */     if ((localClassLoader1 == localClassLoader2) || (loadersAreRelated(localClassLoader1, localClassLoader2, true)))
/* 179 */       return true;
/*     */     try
/*     */     {
/* 182 */       Class localClass = localClassLoader2.loadClass(paramClass1.getName());
/* 183 */       return paramClass1 == localClass; } catch (ClassNotFoundException localClassNotFoundException) {
/*     */     }
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isTypeVisible(MethodType paramMethodType, Class<?> paramClass)
/*     */   {
/* 196 */     int i = -1; for (int j = paramMethodType.parameterCount(); i < j; i++) {
/* 197 */       Class localClass = i < 0 ? paramMethodType.returnType() : paramMethodType.parameterType(i);
/* 198 */       if (!isTypeVisible(localClass, paramClass))
/* 199 */         return false;
/*     */     }
/* 201 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isSamePackage(Class<?> paramClass1, Class<?> paramClass2)
/*     */   {
/* 211 */     assert ((!paramClass1.isArray()) && (!paramClass2.isArray()));
/* 212 */     if (paramClass1 == paramClass2)
/* 213 */       return true;
/* 214 */     if (paramClass1.getClassLoader() != paramClass2.getClassLoader())
/* 215 */       return false;
/* 216 */     String str1 = paramClass1.getName(); String str2 = paramClass2.getName();
/* 217 */     int i = str1.lastIndexOf('.');
/* 218 */     if (i != str2.lastIndexOf('.'))
/* 219 */       return false;
/* 220 */     for (int j = 0; j < i; j++) {
/* 221 */       if (str1.charAt(j) != str2.charAt(j))
/* 222 */         return false;
/*     */     }
/* 224 */     return true;
/*     */   }
/*     */ 
/*     */   public static String getPackageName(Class<?> paramClass)
/*     */   {
/* 230 */     assert (!paramClass.isArray());
/* 231 */     String str = paramClass.getName();
/* 232 */     int i = str.lastIndexOf('.');
/* 233 */     if (i < 0) return "";
/* 234 */     return str.substring(0, i);
/*     */   }
/*     */ 
/*     */   public static boolean isSamePackageMember(Class<?> paramClass1, Class<?> paramClass2)
/*     */   {
/* 245 */     if (paramClass1 == paramClass2)
/* 246 */       return true;
/* 247 */     if (!isSamePackage(paramClass1, paramClass2))
/* 248 */       return false;
/* 249 */     if (getOutermostEnclosingClass(paramClass1) != getOutermostEnclosingClass(paramClass2))
/* 250 */       return false;
/* 251 */     return true;
/*     */   }
/*     */ 
/*     */   private static Class<?> getOutermostEnclosingClass(Class<?> paramClass) {
/* 255 */     Object localObject1 = paramClass;
/* 256 */     for (Object localObject2 = paramClass; (localObject2 = ((Class)localObject2).getEnclosingClass()) != null; )
/* 257 */       localObject1 = localObject2;
/* 258 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private static boolean loadersAreRelated(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2, boolean paramBoolean)
/*     */   {
/* 263 */     if ((paramClassLoader1 == paramClassLoader2) || (paramClassLoader1 == null) || ((paramClassLoader2 == null) && (!paramBoolean)))
/*     */     {
/* 265 */       return true;
/*     */     }
/* 267 */     for (ClassLoader localClassLoader = paramClassLoader2; 
/* 268 */       localClassLoader != null; localClassLoader = localClassLoader.getParent()) {
/* 269 */       if (localClassLoader == paramClassLoader1) return true;
/*     */     }
/* 271 */     if (paramBoolean) return false;
/*     */ 
/* 273 */     for (localClassLoader = paramClassLoader1; 
/* 274 */       localClassLoader != null; localClassLoader = localClassLoader.getParent()) {
/* 275 */       if (localClassLoader == paramClassLoader2) return true;
/*     */     }
/* 277 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean classLoaderIsAncestor(Class<?> paramClass1, Class<?> paramClass2)
/*     */   {
/* 288 */     return loadersAreRelated(paramClass1.getClassLoader(), paramClass2.getClassLoader(), true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.util.VerifyAccess
 * JD-Core Version:    0.6.2
 */