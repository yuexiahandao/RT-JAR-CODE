/*     */ package java.lang.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.ReflectionFactory;
/*     */ import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
/*     */ 
/*     */ public class AccessibleObject
/*     */   implements AnnotatedElement
/*     */ {
/*  64 */   private static final Permission ACCESS_PERMISSION = new ReflectPermission("suppressAccessChecks");
/*     */   boolean override;
/* 171 */   static final ReflectionFactory reflectionFactory = (ReflectionFactory)AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());
/*     */   volatile Object securityCheckCache;
/*     */ 
/*     */   public static void setAccessible(AccessibleObject[] paramArrayOfAccessibleObject, boolean paramBoolean)
/*     */     throws SecurityException
/*     */   {
/*  94 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  95 */     if (localSecurityManager != null) localSecurityManager.checkPermission(ACCESS_PERMISSION);
/*  96 */     for (int i = 0; i < paramArrayOfAccessibleObject.length; i++)
/*  97 */       setAccessible0(paramArrayOfAccessibleObject[i], paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setAccessible(boolean paramBoolean)
/*     */     throws SecurityException
/*     */   {
/* 127 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 128 */     if (localSecurityManager != null) localSecurityManager.checkPermission(ACCESS_PERMISSION);
/* 129 */     setAccessible0(this, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static void setAccessible0(AccessibleObject paramAccessibleObject, boolean paramBoolean)
/*     */     throws SecurityException
/*     */   {
/* 136 */     if (((paramAccessibleObject instanceof Constructor)) && (paramBoolean == true)) {
/* 137 */       Constructor localConstructor = (Constructor)paramAccessibleObject;
/* 138 */       if (localConstructor.getDeclaringClass() == Class.class) {
/* 139 */         throw new SecurityException("Can not make a java.lang.Class constructor accessible");
/*     */       }
/*     */     }
/*     */ 
/* 143 */     paramAccessibleObject.override = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isAccessible()
/*     */   {
/* 152 */     return this.override;
/*     */   }
/*     */ 
/*     */   public <T extends Annotation> T getAnnotation(Class<T> paramClass)
/*     */   {
/* 180 */     throw new AssertionError("All subclasses should override this method");
/*     */   }
/*     */ 
/*     */   public boolean isAnnotationPresent(Class<? extends Annotation> paramClass)
/*     */   {
/* 189 */     return getAnnotation(paramClass) != null;
/*     */   }
/*     */ 
/*     */   public Annotation[] getAnnotations()
/*     */   {
/* 196 */     return getDeclaredAnnotations();
/*     */   }
/*     */ 
/*     */   public Annotation[] getDeclaredAnnotations()
/*     */   {
/* 203 */     throw new AssertionError("All subclasses should override this method");
/*     */   }
/*     */ 
/*     */   void checkAccess(Class<?> paramClass1, Class<?> paramClass2, Object paramObject, int paramInt)
/*     */     throws IllegalAccessException
/*     */   {
/* 229 */     if (paramClass1 == paramClass2) {
/* 230 */       return;
/*     */     }
/* 232 */     Object localObject1 = this.securityCheckCache;
/* 233 */     Object localObject2 = paramClass2;
/* 234 */     if ((paramObject != null) && (Modifier.isProtected(paramInt)) && ((localObject2 = paramObject.getClass()) != paramClass2))
/*     */     {
/* 238 */       if ((localObject1 instanceof Class[])) {
/* 239 */         Class[] arrayOfClass = (Class[])localObject1;
/* 240 */         if ((arrayOfClass[1] == localObject2) && (arrayOfClass[0] == paramClass1))
/*     */         {
/* 242 */           return;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/* 247 */     else if (localObject1 == paramClass1)
/*     */     {
/* 249 */       return;
/*     */     }
/*     */ 
/* 253 */     slowCheckMemberAccess(paramClass1, paramClass2, paramObject, paramInt, (Class)localObject2);
/*     */   }
/*     */ 
/*     */   void slowCheckMemberAccess(Class<?> paramClass1, Class<?> paramClass2, Object paramObject, int paramInt, Class<?> paramClass3)
/*     */     throws IllegalAccessException
/*     */   {
/* 261 */     Reflection.ensureMemberAccess(paramClass1, paramClass2, paramObject, paramInt);
/*     */ 
/* 264 */     Class[] arrayOfClass = { paramClass1, paramClass3 == paramClass2 ? paramClass1 : paramClass3 };
/*     */ 
/* 272 */     this.securityCheckCache = arrayOfClass;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.AccessibleObject
 * JD-Core Version:    0.6.2
 */