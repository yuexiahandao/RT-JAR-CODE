/*     */
package java.lang.reflect;
/*     */ 
/*     */

import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.AnnotationFormatError;
/*     */ import java.util.Map;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.ConstructorAccessor;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.ReflectionFactory;
/*     */ import sun.reflect.annotation.AnnotationParser;
/*     */ import sun.reflect.generics.factory.CoreReflectionFactory;
/*     */ import sun.reflect.generics.factory.GenericsFactory;
/*     */ import sun.reflect.generics.repository.ConstructorRepository;
/*     */ import sun.reflect.generics.scope.ConstructorScope;

/*     */
/*     */ public final class Constructor<T> extends AccessibleObject
/*     */ implements GenericDeclaration, Member
/*     */ {
    /*     */   private Class<T> clazz;
    /*     */   private int slot;
    /*     */   private Class<?>[] parameterTypes;
    /*     */   private Class<?>[] exceptionTypes;
    /*     */   private int modifiers;
    /*     */   private transient String signature;
    /*     */   private transient ConstructorRepository genericInfo;
    /*     */   private byte[] annotations;
    /*     */   private byte[] parameterAnnotations;
    /*     */   private volatile ConstructorAccessor constructorAccessor;
    /*     */   private Constructor<T> root;
    /*     */   private transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

    /*     */
/*     */
    private GenericsFactory getFactory()
/*     */ {
/*  82 */
        return CoreReflectionFactory.make(this, ConstructorScope.make(this));
/*     */
    }

    /*     */
/*     */
    private ConstructorRepository getGenericInfo()
/*     */ {
/*  88 */
        if (this.genericInfo == null)
/*     */ {
/*  90 */
            this.genericInfo = ConstructorRepository.make(getSignature(), getFactory());
/*     */
        }
/*     */ 
/*  94 */
        return this.genericInfo;
/*     */
    }

    /*     */
/*     */   Constructor(Class<T> paramClass, Class<?>[] paramArrayOfClass1, Class<?>[] paramArrayOfClass2, int paramInt1, int paramInt2, String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */ {
/* 117 */
        this.clazz = paramClass;
/* 118 */
        this.parameterTypes = paramArrayOfClass1;
/* 119 */
        this.exceptionTypes = paramArrayOfClass2;
/* 120 */
        this.modifiers = paramInt1;
/* 121 */
        this.slot = paramInt2;
/* 122 */
        this.signature = paramString;
/* 123 */
        this.annotations = paramArrayOfByte1;
/* 124 */
        this.parameterAnnotations = paramArrayOfByte2;
/*     */
    }

    /*     */
/*     */   Constructor<T> copy()
/*     */ {
/* 140 */
        Constructor localConstructor = new Constructor(this.clazz, this.parameterTypes, this.exceptionTypes, this.modifiers, this.slot, this.signature, this.annotations, this.parameterAnnotations);
/*     */ 
/* 146 */
        localConstructor.root = this;
/*     */ 
/* 148 */
        localConstructor.constructorAccessor = this.constructorAccessor;
/* 149 */
        return localConstructor;
/*     */
    }

    /*     */
/*     */
    public Class<T> getDeclaringClass()
/*     */ {
/* 157 */
        return this.clazz;
/*     */
    }

    /*     */
/*     */
    public String getName()
/*     */ {
/* 165 */
        return getDeclaringClass().getName();
/*     */
    }

    /*     */
/*     */
    public int getModifiers()
/*     */ {
/* 176 */
        return this.modifiers;
/*     */
    }

    /*     */
/*     */
    public TypeVariable<Constructor<T>>[] getTypeParameters()
/*     */ {
/* 195 */
        if (getSignature() != null) {
/* 196 */
            return (TypeVariable[]) getGenericInfo().getTypeParameters();
/*     */
        }
/* 198 */
        return (TypeVariable[]) new TypeVariable[0];
/*     */
    }

    /*     */
/*     */
    public Class<?>[] getParameterTypes()
/*     */ {
/* 212 */
        return (Class[]) this.parameterTypes.clone();
/*     */
    }

    /*     */
/*     */
    public Type[] getGenericParameterTypes()
/*     */ {
/* 244 */
        if (getSignature() != null) {
/* 245 */
            return getGenericInfo().getParameterTypes();
/*     */
        }
/* 247 */
        return getParameterTypes();
/*     */
    }

    /*     */
/*     */
    public Class<?>[] getExceptionTypes()
/*     */ {
/* 261 */
        return (Class[]) this.exceptionTypes.clone();
/*     */
    }

    /*     */
/*     */
    public Type[] getGenericExceptionTypes()
/*     */ {
/*     */
        Type[] arrayOfType;
/* 289 */
        if ((getSignature() != null) && ((arrayOfType = getGenericInfo().getExceptionTypes()).length > 0))
/*     */ {
/* 291 */
            return arrayOfType;
/*     */
        }
/* 293 */
        return getExceptionTypes();
/*     */
    }

    /*     */
/*     */
    public boolean equals(Object paramObject)
/*     */ {
/* 303 */
        if ((paramObject != null) && ((paramObject instanceof Constructor))) {
/* 304 */
            Constructor localConstructor = (Constructor) paramObject;
/* 305 */
            if (getDeclaringClass() == localConstructor.getDeclaringClass())
/*     */ {
/* 307 */
                Class[] arrayOfClass1 = this.parameterTypes;
/* 308 */
                Class[] arrayOfClass2 = localConstructor.parameterTypes;
/* 309 */
                if (arrayOfClass1.length == arrayOfClass2.length) {
/* 310 */
                    for (int i = 0; i < arrayOfClass1.length; i++) {
/* 311 */
                        if (arrayOfClass1[i] != arrayOfClass2[i])
/* 312 */ return false;
/*     */
                    }
/* 314 */
                    return true;
/*     */
                }
/*     */
            }
/*     */
        }
/* 318 */
        return false;
/*     */
    }

    /*     */
/*     */
    public int hashCode()
/*     */ {
/* 327 */
        return getDeclaringClass().getName().hashCode();
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/*     */
        try
/*     */ {
/* 347 */
            StringBuffer localStringBuffer = new StringBuffer();
/* 348 */
            int i = getModifiers() & Modifier.constructorModifiers();
/* 349 */
            if (i != 0) {
/* 350 */
                localStringBuffer.append(Modifier.toString(i) + " ");
/*     */
            }
/* 352 */
            localStringBuffer.append(Field.getTypeName(getDeclaringClass()));
/* 353 */
            localStringBuffer.append("(");
/* 354 */
            Class[] arrayOfClass1 = this.parameterTypes;
/* 355 */
            for (int j = 0; j < arrayOfClass1.length; j++) {
/* 356 */
                localStringBuffer.append(Field.getTypeName(arrayOfClass1[j]));
/* 357 */
                if (j < arrayOfClass1.length - 1)
/* 358 */ localStringBuffer.append(",");
/*     */
            }
/* 360 */
            localStringBuffer.append(")");
/* 361 */
            Class[] arrayOfClass2 = this.exceptionTypes;
/* 362 */
            if (arrayOfClass2.length > 0) {
/* 363 */
                localStringBuffer.append(" throws ");
/* 364 */
                for (int k = 0; k < arrayOfClass2.length; k++) {
/* 365 */
                    localStringBuffer.append(arrayOfClass2[k].getName());
/* 366 */
                    if (k < arrayOfClass2.length - 1)
/* 367 */ localStringBuffer.append(",");
/*     */
                }
/*     */
            }
/* 370 */
            return localStringBuffer.toString();
/*     */
        } catch (Exception localException) {
/* 372 */
            return "<" + localException + ">";
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public String toGenericString()
/*     */ {
/*     */
        try
/*     */ {
/* 411 */
            StringBuilder localStringBuilder = new StringBuilder();
/* 412 */
            int i = getModifiers() & Modifier.constructorModifiers();
/* 413 */
            if (i != 0) {
/* 414 */
                localStringBuilder.append(Modifier.toString(i) + " ");
/*     */
            }
/* 416 */
            TypeVariable[] arrayOfTypeVariable1 = getTypeParameters();
/* 417 */
            if (arrayOfTypeVariable1.length > 0) {
/* 418 */
                int j = 1;
/* 419 */
                localStringBuilder.append("<");
/* 420 */
                for (TypeVariable localTypeVariable : arrayOfTypeVariable1) {
/* 421 */
                    if (j == 0) {
/* 422 */
                        localStringBuilder.append(",");
/*     */
                    }
/*     */ 
/* 425 */
                    localStringBuilder.append(localTypeVariable.toString());
/* 426 */
                    j = 0;
/*     */
                }
/* 428 */
                localStringBuilder.append("> ");
/*     */
            }
/* 430 */
            localStringBuilder.append(Field.getTypeName(getDeclaringClass()));
/* 431 */
            localStringBuilder.append("(");
/* 432 */
            Type[] arrayOfType1 = getGenericParameterTypes();
/* 433 */
            for (int k = 0; k < arrayOfType1.length; k++) {
/* 434 */
                String str = (arrayOfType1[k] instanceof Class) ? Field.getTypeName((Class) arrayOfType1[k]) : arrayOfType1[k].toString();
/*     */ 
/* 437 */
                if ((isVarArgs()) && (k == arrayOfType1.length - 1))
/* 438 */ str = str.replaceFirst("\\[\\]$", "...");
/* 439 */
                localStringBuilder.append(str);
/* 440 */
                if (k < arrayOfType1.length - 1)
/* 441 */ localStringBuilder.append(",");
/*     */
            }
/* 443 */
            localStringBuilder.append(")");
/* 444 */
            Type[] arrayOfType2 = getGenericExceptionTypes();
/* 445 */
            if (arrayOfType2.length > 0) {
/* 446 */
                localStringBuilder.append(" throws ");
/* 447 */
                for (int n = 0; n < arrayOfType2.length; n++) {
/* 448 */
                    localStringBuilder.append((arrayOfType2[n] instanceof Class) ? ((Class) arrayOfType2[n]).getName() : arrayOfType2[n].toString());
/*     */ 
/* 451 */
                    if (n < arrayOfType2.length - 1)
/* 452 */ localStringBuilder.append(",");
/*     */
                }
/*     */
            }
/* 455 */
            return localStringBuilder.toString();
/*     */
        } catch (Exception localException) {
/* 457 */
            return "<" + localException + ">";
/*     */
        }
/*     */
    }

    /*     */
/*     */
    @CallerSensitive
/*     */ public T newInstance(Object[] paramArrayOfObject)
/*     */     throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
/*     */ {
/* 514 */
        if ((!this.override) &&
/* 515 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/* 516 */
            localObject = Reflection.getCallerClass();
/* 517 */
            checkAccess((Class) localObject, this.clazz, null, this.modifiers);
/*     */
        }
/*     */ 
/* 520 */
        if ((this.clazz.getModifiers() & 0x4000) != 0)
/* 521 */ throw new IllegalArgumentException("Cannot reflectively create enum objects");
/* 522 */
        Object localObject = this.constructorAccessor;
/* 523 */
        if (localObject == null) {
/* 524 */
            localObject = acquireConstructorAccessor();
/*     */
        }
/* 526 */
        return ((ConstructorAccessor) localObject).newInstance(paramArrayOfObject);
/*     */
    }

    /*     */
/*     */
    public boolean isVarArgs()
/*     */ {
/* 539 */
        return (getModifiers() & 0x80) != 0;
/*     */
    }

    /*     */
/*     */
    public boolean isSynthetic()
/*     */ {
/* 552 */
        return Modifier.isSynthetic(getModifiers());
/*     */
    }

    /*     */
/*     */
    private ConstructorAccessor acquireConstructorAccessor()
/*     */ {
/* 563 */
        ConstructorAccessor localConstructorAccessor = null;
/* 564 */
        if (this.root != null) localConstructorAccessor = this.root.getConstructorAccessor();
/* 565 */
        if (localConstructorAccessor != null) {
/* 566 */
            this.constructorAccessor = localConstructorAccessor;
/*     */
        }
/*     */
        else {
/* 569 */
            localConstructorAccessor = reflectionFactory.newConstructorAccessor(this);
/* 570 */
            setConstructorAccessor(localConstructorAccessor);
/*     */
        }
/*     */ 
/* 573 */
        return localConstructorAccessor;
/*     */
    }

    /*     */
/*     */   ConstructorAccessor getConstructorAccessor()
/*     */ {
/* 579 */
        return this.constructorAccessor;
/*     */
    }

    /*     */
/*     */   void setConstructorAccessor(ConstructorAccessor paramConstructorAccessor)
/*     */ {
/* 585 */
        this.constructorAccessor = paramConstructorAccessor;
/*     */ 
/* 587 */
        if (this.root != null)
/* 588 */ this.root.setConstructorAccessor(paramConstructorAccessor);
/*     */
    }

    /*     */
/*     */   int getSlot()
/*     */ {
/* 593 */
        return this.slot;
/*     */
    }

    /*     */
/*     */   String getSignature() {
/* 597 */
        return this.signature;
/*     */
    }

    /*     */
/*     */   byte[] getRawAnnotations() {
/* 601 */
        return this.annotations;
/*     */
    }

    /*     */
/*     */   byte[] getRawParameterAnnotations() {
/* 605 */
        return this.parameterAnnotations;
/*     */
    }

    /*     */
/*     */
    public <T extends Annotation> T getAnnotation(Class<T> paramClass)
/*     */ {
/* 613 */
        if (paramClass == null) {
/* 614 */
            throw new NullPointerException();
/*     */
        }
/* 616 */
        return (Annotation) declaredAnnotations().get(paramClass);
/*     */
    }

    /*     */
/*     */
    public Annotation[] getDeclaredAnnotations()
/*     */ {
/* 623 */
        return AnnotationParser.toArray(declaredAnnotations());
/*     */
    }

    /*     */
/*     */
    private synchronized Map<Class<? extends Annotation>, Annotation> declaredAnnotations()
/*     */ {
/* 629 */
        if (this.declaredAnnotations == null) {
/* 630 */
            this.declaredAnnotations = AnnotationParser.parseAnnotations(this.annotations, SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
/*     */
        }
/*     */ 
/* 635 */
        return this.declaredAnnotations;
/*     */
    }

    /*     */
/*     */
    public Annotation[][] getParameterAnnotations()
/*     */ {
/* 655 */
        int i = this.parameterTypes.length;
/* 656 */
        if (this.parameterAnnotations == null) {
/* 657 */
            return new Annotation[i][0];
/*     */
        }
/* 659 */
        Annotation[][] arrayOfAnnotation = AnnotationParser.parseParameterAnnotations(this.parameterAnnotations, SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
/*     */ 
/* 664 */
        if (arrayOfAnnotation.length != i) {
/* 665 */
            Class localClass = getDeclaringClass();
/* 666 */
            if ((!localClass.isEnum()) && (!localClass.isAnonymousClass()) && (!localClass.isLocalClass()))
/*     */ {
/* 671 */
                if ((!localClass.isMemberClass()) || ((localClass.isMemberClass()) && ((localClass.getModifiers() & 0x8) == 0) && (arrayOfAnnotation.length + 1 != i)))
/*     */ {
/* 677 */
                    throw new AnnotationFormatError("Parameter annotations don't match number of parameters");
/*     */
                }
/*     */
            }
/*     */
        }
/*     */ 
/* 682 */
        return arrayOfAnnotation;
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.Constructor
 * JD-Core Version:    0.6.2
 */