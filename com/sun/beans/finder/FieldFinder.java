/*     */ package com.sun.beans.finder;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public final class FieldFinder
/*     */ {
/*     */   public static Field findField(Class<?> paramClass, String paramString)
/*     */     throws NoSuchFieldException
/*     */   {
/*  54 */     if (paramString == null) {
/*  55 */       throw new IllegalArgumentException("Field name is not set");
/*     */     }
/*  57 */     Field localField = paramClass.getField(paramString);
/*  58 */     if (!Modifier.isPublic(localField.getModifiers())) {
/*  59 */       throw new NoSuchFieldException("Field '" + paramString + "' is not public");
/*     */     }
/*  61 */     paramClass = localField.getDeclaringClass();
/*  62 */     if ((!Modifier.isPublic(paramClass.getModifiers())) || (!ReflectUtil.isPackageAccessible(paramClass))) {
/*  63 */       throw new NoSuchFieldException("Field '" + paramString + "' is not accessible");
/*     */     }
/*  65 */     return localField;
/*     */   }
/*     */ 
/*     */   public static Field findInstanceField(Class<?> paramClass, String paramString)
/*     */     throws NoSuchFieldException
/*     */   {
/*  79 */     Field localField = findField(paramClass, paramString);
/*  80 */     if (Modifier.isStatic(localField.getModifiers())) {
/*  81 */       throw new NoSuchFieldException("Field '" + paramString + "' is static");
/*     */     }
/*  83 */     return localField;
/*     */   }
/*     */ 
/*     */   public static Field findStaticField(Class<?> paramClass, String paramString)
/*     */     throws NoSuchFieldException
/*     */   {
/*  97 */     Field localField = findField(paramClass, paramString);
/*  98 */     if (!Modifier.isStatic(localField.getModifiers())) {
/*  99 */       throw new NoSuchFieldException("Field '" + paramString + "' is not static");
/*     */     }
/* 101 */     return localField;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.FieldFinder
 * JD-Core Version:    0.6.2
 */