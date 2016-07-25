/*     */ package java.lang.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.AnnotationFormatError;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Map;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.MethodAccessor;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.ReflectionFactory;
/*     */ import sun.reflect.annotation.AnnotationParser;
/*     */ import sun.reflect.annotation.AnnotationType;
/*     */ import sun.reflect.annotation.ExceptionProxy;
/*     */ import sun.reflect.generics.factory.CoreReflectionFactory;
/*     */ import sun.reflect.generics.factory.GenericsFactory;
/*     */ import sun.reflect.generics.repository.MethodRepository;
/*     */ import sun.reflect.generics.scope.MethodScope;
/*     */ 
/*     */ public final class Method extends AccessibleObject
/*     */   implements GenericDeclaration, Member
/*     */ {
/*     */   private Class<?> clazz;
/*     */   private int slot;
/*     */   private String name;
/*     */   private Class<?> returnType;
/*     */   private Class<?>[] parameterTypes;
/*     */   private Class<?>[] exceptionTypes;
/*     */   private int modifiers;
/*     */   private transient String signature;
/*     */   private transient MethodRepository genericInfo;
/*     */   private byte[] annotations;
/*     */   private byte[] parameterAnnotations;
/*     */   private byte[] annotationDefault;
/*     */   private volatile MethodAccessor methodAccessor;
/*     */   private Method root;
/*     */   private transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;
/*     */ 
/*     */   private String getGenericSignature()
/*     */   {
/*  89 */     return this.signature;
/*     */   }
/*     */ 
/*     */   private GenericsFactory getFactory()
/*     */   {
/*  94 */     return CoreReflectionFactory.make(this, MethodScope.make(this));
/*     */   }
/*     */ 
/*     */   private MethodRepository getGenericInfo()
/*     */   {
/* 100 */     if (this.genericInfo == null)
/*     */     {
/* 102 */       this.genericInfo = MethodRepository.make(getGenericSignature(), getFactory());
/*     */     }
/*     */ 
/* 105 */     return this.genericInfo;
/*     */   }
/*     */ 
/*     */   Method(Class<?> paramClass1, String paramString1, Class<?>[] paramArrayOfClass1, Class<?> paramClass2, Class<?>[] paramArrayOfClass2, int paramInt1, int paramInt2, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*     */   {
/* 125 */     this.clazz = paramClass1;
/* 126 */     this.name = paramString1;
/* 127 */     this.parameterTypes = paramArrayOfClass1;
/* 128 */     this.returnType = paramClass2;
/* 129 */     this.exceptionTypes = paramArrayOfClass2;
/* 130 */     this.modifiers = paramInt1;
/* 131 */     this.slot = paramInt2;
/* 132 */     this.signature = paramString2;
/* 133 */     this.annotations = paramArrayOfByte1;
/* 134 */     this.parameterAnnotations = paramArrayOfByte2;
/* 135 */     this.annotationDefault = paramArrayOfByte3;
/*     */   }
/*     */ 
/*     */   Method copy()
/*     */   {
/* 151 */     Method localMethod = new Method(this.clazz, this.name, this.parameterTypes, this.returnType, this.exceptionTypes, this.modifiers, this.slot, this.signature, this.annotations, this.parameterAnnotations, this.annotationDefault);
/*     */ 
/* 154 */     localMethod.root = this;
/*     */ 
/* 156 */     localMethod.methodAccessor = this.methodAccessor;
/* 157 */     return localMethod;
/*     */   }
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 165 */     return this.clazz;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 173 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getModifiers()
/*     */   {
/* 184 */     return this.modifiers;
/*     */   }
/*     */ 
/*     */   public TypeVariable<Method>[] getTypeParameters()
/*     */   {
/* 203 */     if (getGenericSignature() != null) {
/* 204 */       return (TypeVariable[])getGenericInfo().getTypeParameters();
/*     */     }
/* 206 */     return (TypeVariable[])new TypeVariable[0];
/*     */   }
/*     */ 
/*     */   public Class<?> getReturnType()
/*     */   {
/* 216 */     return this.returnType;
/*     */   }
/*     */ 
/*     */   public Type getGenericReturnType()
/*     */   {
/* 244 */     if (getGenericSignature() != null)
/* 245 */       return getGenericInfo().getReturnType();
/* 246 */     return getReturnType();
/*     */   }
/*     */ 
/*     */   public Class<?>[] getParameterTypes()
/*     */   {
/* 260 */     return (Class[])this.parameterTypes.clone();
/*     */   }
/*     */ 
/*     */   public Type[] getGenericParameterTypes()
/*     */   {
/* 291 */     if (getGenericSignature() != null) {
/* 292 */       return getGenericInfo().getParameterTypes();
/*     */     }
/* 294 */     return getParameterTypes();
/*     */   }
/*     */ 
/*     */   public Class<?>[] getExceptionTypes()
/*     */   {
/* 309 */     return (Class[])this.exceptionTypes.clone();
/*     */   }
/*     */ 
/*     */   public Type[] getGenericExceptionTypes()
/*     */   {
/*     */     Type[] arrayOfType;
/* 336 */     if ((getGenericSignature() != null) && ((arrayOfType = getGenericInfo().getExceptionTypes()).length > 0))
/*     */     {
/* 338 */       return arrayOfType;
/*     */     }
/* 340 */     return getExceptionTypes();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 350 */     if ((paramObject != null) && ((paramObject instanceof Method))) {
/* 351 */       Method localMethod = (Method)paramObject;
/* 352 */       if ((getDeclaringClass() == localMethod.getDeclaringClass()) && (getName() == localMethod.getName()))
/*     */       {
/* 354 */         if (!this.returnType.equals(localMethod.getReturnType())) {
/* 355 */           return false;
/*     */         }
/* 357 */         Class[] arrayOfClass1 = this.parameterTypes;
/* 358 */         Class[] arrayOfClass2 = localMethod.parameterTypes;
/* 359 */         if (arrayOfClass1.length == arrayOfClass2.length) {
/* 360 */           for (int i = 0; i < arrayOfClass1.length; i++) {
/* 361 */             if (arrayOfClass1[i] != arrayOfClass2[i])
/* 362 */               return false;
/*     */           }
/* 364 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 368 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 377 */     return getDeclaringClass().getName().hashCode() ^ getName().hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     try
/*     */     {
/* 404 */       StringBuilder localStringBuilder = new StringBuilder();
/* 405 */       int i = getModifiers() & Modifier.methodModifiers();
/* 406 */       if (i != 0) {
/* 407 */         localStringBuilder.append(Modifier.toString(i)).append(' ');
/*     */       }
/* 409 */       localStringBuilder.append(Field.getTypeName(getReturnType())).append(' ');
/* 410 */       localStringBuilder.append(Field.getTypeName(getDeclaringClass())).append('.');
/* 411 */       localStringBuilder.append(getName()).append('(');
/* 412 */       Class[] arrayOfClass1 = this.parameterTypes;
/* 413 */       for (int j = 0; j < arrayOfClass1.length; j++) {
/* 414 */         localStringBuilder.append(Field.getTypeName(arrayOfClass1[j]));
/* 415 */         if (j < arrayOfClass1.length - 1)
/* 416 */           localStringBuilder.append(',');
/*     */       }
/* 418 */       localStringBuilder.append(')');
/* 419 */       Class[] arrayOfClass2 = this.exceptionTypes;
/* 420 */       if (arrayOfClass2.length > 0) {
/* 421 */         localStringBuilder.append(" throws ");
/* 422 */         for (int k = 0; k < arrayOfClass2.length; k++) {
/* 423 */           localStringBuilder.append(arrayOfClass2[k].getName());
/* 424 */           if (k < arrayOfClass2.length - 1)
/* 425 */             localStringBuilder.append(',');
/*     */         }
/*     */       }
/* 428 */       return localStringBuilder.toString();
/*     */     } catch (Exception localException) {
/* 430 */       return "<" + localException + ">";
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toGenericString()
/*     */   {
/*     */     try
/*     */     {
/* 474 */       StringBuilder localStringBuilder = new StringBuilder();
/* 475 */       int i = getModifiers() & Modifier.methodModifiers();
/* 476 */       if (i != 0) {
/* 477 */         localStringBuilder.append(Modifier.toString(i)).append(' ');
/*     */       }
/* 479 */       TypeVariable[] arrayOfTypeVariable = getTypeParameters();
/* 480 */       if (arrayOfTypeVariable.length > 0) {
/* 481 */         int j = 1;
/* 482 */         localStringBuilder.append('<');
/* 483 */         for (Object localObject2 : arrayOfTypeVariable) {
/* 484 */           if (j == 0) {
/* 485 */             localStringBuilder.append(',');
/*     */           }
/*     */ 
/* 488 */           localStringBuilder.append(localObject2.toString());
/* 489 */           j = 0;
/*     */         }
/* 491 */         localStringBuilder.append("> ");
/*     */       }
/*     */ 
/* 494 */       Type localType = getGenericReturnType();
/* 495 */       localStringBuilder.append((localType instanceof Class) ? Field.getTypeName((Class)localType) : localType.toString()).append(' ');
/*     */ 
/* 499 */       localStringBuilder.append(Field.getTypeName(getDeclaringClass())).append('.');
/* 500 */       localStringBuilder.append(getName()).append('(');
/* 501 */       ??? = getGenericParameterTypes();
/* 502 */       for (??? = 0; ??? < ???.length; ???++) {
/* 503 */         String str = (???[???] instanceof Class) ? Field.getTypeName((Class)???[???]) : ???[???].toString();
/*     */ 
/* 506 */         if ((isVarArgs()) && (??? == ???.length - 1))
/* 507 */           str = str.replaceFirst("\\[\\]$", "...");
/* 508 */         localStringBuilder.append(str);
/* 509 */         if (??? < ???.length - 1)
/* 510 */           localStringBuilder.append(',');
/*     */       }
/* 512 */       localStringBuilder.append(')');
/* 513 */       Type[] arrayOfType = getGenericExceptionTypes();
/* 514 */       if (arrayOfType.length > 0) {
/* 515 */         localStringBuilder.append(" throws ");
/* 516 */         for (int n = 0; n < arrayOfType.length; n++) {
/* 517 */           localStringBuilder.append((arrayOfType[n] instanceof Class) ? ((Class)arrayOfType[n]).getName() : arrayOfType[n].toString());
/*     */ 
/* 520 */           if (n < arrayOfType.length - 1)
/* 521 */             localStringBuilder.append(',');
/*     */         }
/*     */       }
/* 524 */       return localStringBuilder.toString();
/*     */     } catch (Exception localException) {
/* 526 */       return "<" + localException + ">";
/*     */     }
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public Object invoke(Object paramObject, Object[] paramArrayOfObject)
/*     */     throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
/*     */   {
/* 592 */     if ((!this.override) && 
/* 593 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)))
/*     */     {
/* 598 */       localObject = getCallerClass();
/* 599 */       checkAccess((Class)localObject, this.clazz, paramObject, this.modifiers);
/*     */     }
/*     */ 
/* 602 */     Object localObject = this.methodAccessor;
/* 603 */     if (localObject == null) {
/* 604 */       localObject = acquireMethodAccessor();
/*     */     }
/* 606 */     return ((MethodAccessor)localObject).invoke(paramObject, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   private Class<?> getCallerClass()
/*     */   {
/* 616 */     return Reflection.getCallerClass();
/*     */   }
/*     */ 
/*     */   public boolean isBridge()
/*     */   {
/* 628 */     return (getModifiers() & 0x40) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isVarArgs()
/*     */   {
/* 641 */     return (getModifiers() & 0x80) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isSynthetic()
/*     */   {
/* 653 */     return Modifier.isSynthetic(getModifiers());
/*     */   }
/*     */ 
/*     */   private MethodAccessor acquireMethodAccessor()
/*     */   {
/* 663 */     MethodAccessor localMethodAccessor = null;
/* 664 */     if (this.root != null) localMethodAccessor = this.root.getMethodAccessor();
/* 665 */     if (localMethodAccessor != null) {
/* 666 */       this.methodAccessor = localMethodAccessor;
/*     */     }
/*     */     else {
/* 669 */       localMethodAccessor = reflectionFactory.newMethodAccessor(this);
/* 670 */       setMethodAccessor(localMethodAccessor);
/*     */     }
/*     */ 
/* 673 */     return localMethodAccessor;
/*     */   }
/*     */ 
/*     */   MethodAccessor getMethodAccessor()
/*     */   {
/* 679 */     return this.methodAccessor;
/*     */   }
/*     */ 
/*     */   void setMethodAccessor(MethodAccessor paramMethodAccessor)
/*     */   {
/* 685 */     this.methodAccessor = paramMethodAccessor;
/*     */ 
/* 687 */     if (this.root != null)
/* 688 */       this.root.setMethodAccessor(paramMethodAccessor);
/*     */   }
/*     */ 
/*     */   public <T extends Annotation> T getAnnotation(Class<T> paramClass)
/*     */   {
/* 697 */     if (paramClass == null) {
/* 698 */       throw new NullPointerException();
/*     */     }
/* 700 */     return (Annotation)declaredAnnotations().get(paramClass);
/*     */   }
/*     */ 
/*     */   public Annotation[] getDeclaredAnnotations()
/*     */   {
/* 707 */     return AnnotationParser.toArray(declaredAnnotations());
/*     */   }
/*     */ 
/*     */   private synchronized Map<Class<? extends Annotation>, Annotation> declaredAnnotations()
/*     */   {
/* 713 */     if (this.declaredAnnotations == null) {
/* 714 */       this.declaredAnnotations = AnnotationParser.parseAnnotations(this.annotations, SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
/*     */     }
/*     */ 
/* 719 */     return this.declaredAnnotations;
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue()
/*     */   {
/* 737 */     if (this.annotationDefault == null)
/* 738 */       return null;
/* 739 */     Class localClass = AnnotationType.invocationHandlerReturnType(getReturnType());
/*     */ 
/* 741 */     Object localObject = AnnotationParser.parseMemberValue(localClass, ByteBuffer.wrap(this.annotationDefault), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
/*     */ 
/* 746 */     if ((localObject instanceof ExceptionProxy))
/* 747 */       throw new AnnotationFormatError("Invalid default: " + this);
/* 748 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Annotation[][] getParameterAnnotations()
/*     */   {
/* 768 */     int i = this.parameterTypes.length;
/* 769 */     if (this.parameterAnnotations == null) {
/* 770 */       return new Annotation[i][0];
/*     */     }
/* 772 */     Annotation[][] arrayOfAnnotation = AnnotationParser.parseParameterAnnotations(this.parameterAnnotations, SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
/*     */ 
/* 777 */     if (arrayOfAnnotation.length != i) {
/* 778 */       throw new AnnotationFormatError("Parameter annotations don't match number of parameters");
/*     */     }
/* 780 */     return arrayOfAnnotation;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.Method
 * JD-Core Version:    0.6.2
 */