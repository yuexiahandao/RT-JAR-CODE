/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public class ReflectionFactory
/*     */ {
/*  50 */   private static boolean initted = false;
/*  51 */   private static Permission reflectionFactoryAccessPerm = new RuntimePermission("reflectionFactoryAccess");
/*     */ 
/*  53 */   private static ReflectionFactory soleInstance = new ReflectionFactory();
/*     */   private static volatile LangReflectAccess langReflectAccess;
/*  71 */   private static boolean noInflation = false;
/*  72 */   private static int inflationThreshold = 15;
/*     */ 
/*     */   public static ReflectionFactory getReflectionFactory()
/*     */   {
/* 112 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 113 */     if (localSecurityManager != null)
/*     */     {
/* 115 */       localSecurityManager.checkPermission(reflectionFactoryAccessPerm);
/*     */     }
/* 117 */     return soleInstance;
/*     */   }
/*     */ 
/*     */   public void setLangReflectAccess(LangReflectAccess paramLangReflectAccess)
/*     */   {
/* 128 */     langReflectAccess = paramLangReflectAccess;
/*     */   }
/*     */ 
/*     */   public FieldAccessor newFieldAccessor(Field paramField, boolean paramBoolean)
/*     */   {
/* 139 */     checkInitted();
/* 140 */     return UnsafeFieldAccessorFactory.newFieldAccessor(paramField, paramBoolean);
/*     */   }
/*     */ 
/*     */   public MethodAccessor newMethodAccessor(Method paramMethod) {
/* 144 */     checkInitted();
/*     */ 
/* 146 */     if (noInflation) {
/* 147 */       return new MethodAccessorGenerator().generateMethod(paramMethod.getDeclaringClass(), paramMethod.getName(), paramMethod.getParameterTypes(), paramMethod.getReturnType(), paramMethod.getExceptionTypes(), paramMethod.getModifiers());
/*     */     }
/*     */ 
/* 155 */     NativeMethodAccessorImpl localNativeMethodAccessorImpl = new NativeMethodAccessorImpl(paramMethod);
/*     */ 
/* 157 */     DelegatingMethodAccessorImpl localDelegatingMethodAccessorImpl = new DelegatingMethodAccessorImpl(localNativeMethodAccessorImpl);
/*     */ 
/* 159 */     localNativeMethodAccessorImpl.setParent(localDelegatingMethodAccessorImpl);
/* 160 */     return localDelegatingMethodAccessorImpl;
/*     */   }
/*     */ 
/*     */   public ConstructorAccessor newConstructorAccessor(Constructor paramConstructor)
/*     */   {
/* 165 */     checkInitted();
/*     */ 
/* 167 */     Class localClass = paramConstructor.getDeclaringClass();
/* 168 */     if (Modifier.isAbstract(localClass.getModifiers())) {
/* 169 */       return new InstantiationExceptionConstructorAccessorImpl(null);
/*     */     }
/* 171 */     if (localClass == Class.class) {
/* 172 */       return new InstantiationExceptionConstructorAccessorImpl("Can not instantiate java.lang.Class");
/*     */     }
/*     */ 
/* 178 */     if (Reflection.isSubclassOf(localClass, ConstructorAccessorImpl.class))
/*     */     {
/* 180 */       return new BootstrapConstructorAccessorImpl(paramConstructor);
/*     */     }
/*     */ 
/* 183 */     if (noInflation) {
/* 184 */       return new MethodAccessorGenerator().generateConstructor(paramConstructor.getDeclaringClass(), paramConstructor.getParameterTypes(), paramConstructor.getExceptionTypes(), paramConstructor.getModifiers());
/*     */     }
/*     */ 
/* 190 */     NativeConstructorAccessorImpl localNativeConstructorAccessorImpl = new NativeConstructorAccessorImpl(paramConstructor);
/*     */ 
/* 192 */     DelegatingConstructorAccessorImpl localDelegatingConstructorAccessorImpl = new DelegatingConstructorAccessorImpl(localNativeConstructorAccessorImpl);
/*     */ 
/* 194 */     localNativeConstructorAccessorImpl.setParent(localDelegatingConstructorAccessorImpl);
/* 195 */     return localDelegatingConstructorAccessorImpl;
/*     */   }
/*     */ 
/*     */   public Field newField(Class<?> paramClass1, String paramString1, Class<?> paramClass2, int paramInt1, int paramInt2, String paramString2, byte[] paramArrayOfByte)
/*     */   {
/* 215 */     return langReflectAccess().newField(paramClass1, paramString1, paramClass2, paramInt1, paramInt2, paramString2, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public Method newMethod(Class<?> paramClass1, String paramString1, Class<?>[] paramArrayOfClass1, Class<?> paramClass2, Class<?>[] paramArrayOfClass2, int paramInt1, int paramInt2, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*     */   {
/* 238 */     return langReflectAccess().newMethod(paramClass1, paramString1, paramArrayOfClass1, paramClass2, paramArrayOfClass2, paramInt1, paramInt2, paramString2, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
/*     */   }
/*     */ 
/*     */   public Constructor newConstructor(Class<?> paramClass, Class<?>[] paramArrayOfClass1, Class<?>[] paramArrayOfClass2, int paramInt1, int paramInt2, String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 262 */     return langReflectAccess().newConstructor(paramClass, paramArrayOfClass1, paramArrayOfClass2, paramInt1, paramInt2, paramString, paramArrayOfByte1, paramArrayOfByte2);
/*     */   }
/*     */ 
/*     */   public MethodAccessor getMethodAccessor(Method paramMethod)
/*     */   {
/* 274 */     return langReflectAccess().getMethodAccessor(paramMethod);
/*     */   }
/*     */ 
/*     */   public void setMethodAccessor(Method paramMethod, MethodAccessor paramMethodAccessor)
/*     */   {
/* 279 */     langReflectAccess().setMethodAccessor(paramMethod, paramMethodAccessor);
/*     */   }
/*     */ 
/*     */   public ConstructorAccessor getConstructorAccessor(Constructor paramConstructor)
/*     */   {
/* 285 */     return langReflectAccess().getConstructorAccessor(paramConstructor);
/*     */   }
/*     */ 
/*     */   public void setConstructorAccessor(Constructor paramConstructor, ConstructorAccessor paramConstructorAccessor)
/*     */   {
/* 293 */     langReflectAccess().setConstructorAccessor(paramConstructor, paramConstructorAccessor);
/*     */   }
/*     */ 
/*     */   public Method copyMethod(Method paramMethod)
/*     */   {
/* 300 */     return langReflectAccess().copyMethod(paramMethod);
/*     */   }
/*     */ 
/*     */   public Field copyField(Field paramField)
/*     */   {
/* 307 */     return langReflectAccess().copyField(paramField);
/*     */   }
/*     */ 
/*     */   public <T> Constructor<T> copyConstructor(Constructor<T> paramConstructor)
/*     */   {
/* 314 */     return langReflectAccess().copyConstructor(paramConstructor);
/*     */   }
/*     */ 
/*     */   public Constructor newConstructorForSerialization(Class<?> paramClass, Constructor paramConstructor)
/*     */   {
/* 327 */     if (paramConstructor.getDeclaringClass() == paramClass) {
/* 328 */       return paramConstructor;
/*     */     }
/*     */ 
/* 331 */     SerializationConstructorAccessorImpl localSerializationConstructorAccessorImpl = new MethodAccessorGenerator().generateSerializationConstructor(paramClass, paramConstructor.getParameterTypes(), paramConstructor.getExceptionTypes(), paramConstructor.getModifiers(), paramConstructor.getDeclaringClass());
/*     */ 
/* 337 */     Constructor localConstructor = newConstructor(paramConstructor.getDeclaringClass(), paramConstructor.getParameterTypes(), paramConstructor.getExceptionTypes(), paramConstructor.getModifiers(), langReflectAccess().getConstructorSlot(paramConstructor), langReflectAccess().getConstructorSignature(paramConstructor), langReflectAccess().getConstructorAnnotations(paramConstructor), langReflectAccess().getConstructorParameterAnnotations(paramConstructor));
/*     */ 
/* 349 */     setConstructorAccessor(localConstructor, localSerializationConstructorAccessorImpl);
/* 350 */     return localConstructor;
/*     */   }
/*     */ 
/*     */   static int inflationThreshold()
/*     */   {
/* 359 */     return inflationThreshold;
/*     */   }
/*     */ 
/*     */   private static void checkInitted()
/*     */   {
/* 368 */     if (initted) return;
/* 369 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run()
/*     */       {
/* 381 */         if (System.out == null)
/*     */         {
/* 383 */           return null;
/*     */         }
/*     */ 
/* 386 */         String str = System.getProperty("sun.reflect.noInflation");
/* 387 */         if ((str != null) && (str.equals("true"))) {
/* 388 */           ReflectionFactory.access$002(true);
/*     */         }
/*     */ 
/* 391 */         str = System.getProperty("sun.reflect.inflationThreshold");
/* 392 */         if (str != null) {
/*     */           try {
/* 394 */             ReflectionFactory.access$102(Integer.parseInt(str));
/*     */           } catch (NumberFormatException localNumberFormatException) {
/* 396 */             throw ((RuntimeException)new RuntimeException("Unable to parse property sun.reflect.inflationThreshold").initCause(localNumberFormatException));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 402 */         ReflectionFactory.access$202(true);
/* 403 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static LangReflectAccess langReflectAccess() {
/* 409 */     if (langReflectAccess == null)
/*     */     {
/* 414 */       Modifier.isPublic(1);
/*     */     }
/* 416 */     return langReflectAccess;
/*     */   }
/*     */ 
/*     */   public static final class GetReflectionFactoryAction
/*     */     implements PrivilegedAction<ReflectionFactory>
/*     */   {
/*     */     public ReflectionFactory run()
/*     */     {
/*  89 */       return ReflectionFactory.getReflectionFactory();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.ReflectionFactory
 * JD-Core Version:    0.6.2
 */