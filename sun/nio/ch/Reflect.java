/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ class Reflect
/*     */ {
/*     */   private static void setAccessible(AccessibleObject paramAccessibleObject)
/*     */   {
/*  46 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/*  48 */         this.val$ao.setAccessible(true);
/*  49 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static Constructor lookupConstructor(String paramString, Class[] paramArrayOfClass)
/*     */   {
/*     */     try {
/*  57 */       Class localClass = Class.forName(paramString);
/*  58 */       Constructor localConstructor = localClass.getDeclaredConstructor(paramArrayOfClass);
/*  59 */       setAccessible(localConstructor);
/*  60 */       return localConstructor;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  62 */       throw new ReflectionError(localClassNotFoundException);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*  64 */       throw new ReflectionError(localNoSuchMethodException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object invoke(Constructor paramConstructor, Object[] paramArrayOfObject) {
/*     */     try {
/*  70 */       return paramConstructor.newInstance(paramArrayOfObject);
/*     */     } catch (InstantiationException localInstantiationException) {
/*  72 */       throw new ReflectionError(localInstantiationException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  74 */       throw new ReflectionError(localIllegalAccessException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/*  76 */       throw new ReflectionError(localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Method lookupMethod(String paramString1, String paramString2, Class[] paramArrayOfClass)
/*     */   {
/*     */     try
/*     */     {
/*  85 */       Class localClass = Class.forName(paramString1);
/*  86 */       Method localMethod = localClass.getDeclaredMethod(paramString2, paramArrayOfClass);
/*  87 */       setAccessible(localMethod);
/*  88 */       return localMethod;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  90 */       throw new ReflectionError(localClassNotFoundException);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*  92 */       throw new ReflectionError(localNoSuchMethodException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object invoke(Method paramMethod, Object paramObject, Object[] paramArrayOfObject) {
/*     */     try {
/*  98 */       return paramMethod.invoke(paramObject, paramArrayOfObject);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 100 */       throw new ReflectionError(localIllegalAccessException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 102 */       throw new ReflectionError(localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object invokeIO(Method paramMethod, Object paramObject, Object[] paramArrayOfObject) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 110 */       return paramMethod.invoke(paramObject, paramArrayOfObject);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 112 */       throw new ReflectionError(localIllegalAccessException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 114 */       if (IOException.class.isInstance(localInvocationTargetException.getCause()))
/* 115 */         throw ((IOException)localInvocationTargetException.getCause());
/* 116 */       throw new ReflectionError(localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Field lookupField(String paramString1, String paramString2) {
/*     */     try {
/* 122 */       Class localClass = Class.forName(paramString1);
/* 123 */       Field localField = localClass.getDeclaredField(paramString2);
/* 124 */       setAccessible(localField);
/* 125 */       return localField;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 127 */       throw new ReflectionError(localClassNotFoundException);
/*     */     } catch (NoSuchFieldException localNoSuchFieldException) {
/* 129 */       throw new ReflectionError(localNoSuchFieldException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object get(Object paramObject, Field paramField) {
/*     */     try {
/* 135 */       return paramField.get(paramObject);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 137 */       throw new ReflectionError(localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object get(Field paramField) {
/* 142 */     return get(null, paramField);
/*     */   }
/*     */ 
/*     */   static void set(Object paramObject1, Field paramField, Object paramObject2) {
/*     */     try {
/* 147 */       paramField.set(paramObject1, paramObject2);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 149 */       throw new ReflectionError(localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void setInt(Object paramObject, Field paramField, int paramInt) {
/*     */     try {
/* 155 */       paramField.setInt(paramObject, paramInt);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 157 */       throw new ReflectionError(localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void setBoolean(Object paramObject, Field paramField, boolean paramBoolean) {
/*     */     try {
/* 163 */       paramField.setBoolean(paramObject, paramBoolean);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 165 */       throw new ReflectionError(localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ReflectionError extends Error
/*     */   {
/*     */     private static final long serialVersionUID = -8659519328078164097L;
/*     */ 
/*     */     ReflectionError(Throwable paramThrowable)
/*     */     {
/*  41 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.Reflect
 * JD-Core Version:    0.6.2
 */