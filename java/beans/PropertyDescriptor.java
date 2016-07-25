/*     */ package java.beans;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class PropertyDescriptor extends FeatureDescriptor
/*     */ {
/*     */   private Reference<Class> propertyTypeRef;
/*  39 */   private final MethodRef readMethodRef = new MethodRef();
/*  40 */   private final MethodRef writeMethodRef = new MethodRef();
/*     */   private Reference<Class> propertyEditorClassRef;
/*     */   private boolean bound;
/*     */   private boolean constrained;
/*     */   private String baseName;
/*     */   private String writeMethodName;
/*     */   private String readMethodName;
/*     */ 
/*     */   public PropertyDescriptor(String paramString, Class<?> paramClass)
/*     */     throws IntrospectionException
/*     */   {
/*  70 */     this(paramString, paramClass, "is" + NameGenerator.capitalize(paramString), "set" + NameGenerator.capitalize(paramString));
/*     */   }
/*     */ 
/*     */   public PropertyDescriptor(String paramString1, Class<?> paramClass, String paramString2, String paramString3)
/*     */     throws IntrospectionException
/*     */   {
/*  92 */     if (paramClass == null) {
/*  93 */       throw new IntrospectionException("Target Bean class is null");
/*     */     }
/*  95 */     if ((paramString1 == null) || (paramString1.length() == 0)) {
/*  96 */       throw new IntrospectionException("bad property name");
/*     */     }
/*  98 */     if (("".equals(paramString2)) || ("".equals(paramString3))) {
/*  99 */       throw new IntrospectionException("read or write method name should not be the empty string");
/*     */     }
/* 101 */     setName(paramString1);
/* 102 */     setClass0(paramClass);
/*     */ 
/* 104 */     this.readMethodName = paramString2;
/* 105 */     if ((paramString2 != null) && (getReadMethod() == null)) {
/* 106 */       throw new IntrospectionException("Method not found: " + paramString2);
/*     */     }
/* 108 */     this.writeMethodName = paramString3;
/* 109 */     if ((paramString3 != null) && (getWriteMethod() == null)) {
/* 110 */       throw new IntrospectionException("Method not found: " + paramString3);
/*     */     }
/*     */ 
/* 115 */     Class[] arrayOfClass = { PropertyChangeListener.class };
/* 116 */     this.bound = (null != Introspector.findMethod(paramClass, "addPropertyChangeListener", arrayOfClass.length, arrayOfClass));
/*     */   }
/*     */ 
/*     */   public PropertyDescriptor(String paramString, Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 133 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 134 */       throw new IntrospectionException("bad property name");
/*     */     }
/* 136 */     setName(paramString);
/* 137 */     setReadMethod(paramMethod1);
/* 138 */     setWriteMethod(paramMethod2);
/*     */   }
/*     */ 
/*     */   PropertyDescriptor(Class<?> paramClass, String paramString, Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 154 */     if (paramClass == null) {
/* 155 */       throw new IntrospectionException("Target Bean class is null");
/*     */     }
/* 157 */     setClass0(paramClass);
/* 158 */     setName(Introspector.decapitalize(paramString));
/* 159 */     setReadMethod(paramMethod1);
/* 160 */     setWriteMethod(paramMethod2);
/* 161 */     this.baseName = paramString;
/*     */   }
/*     */ 
/*     */   public synchronized Class<?> getPropertyType()
/*     */   {
/* 177 */     Class localClass = getPropertyType0();
/* 178 */     if (localClass == null)
/*     */       try {
/* 180 */         localClass = findPropertyType(getReadMethod(), getWriteMethod());
/* 181 */         setPropertyType(localClass);
/*     */       }
/*     */       catch (IntrospectionException localIntrospectionException)
/*     */       {
/*     */       }
/* 186 */     return localClass;
/*     */   }
/*     */ 
/*     */   private void setPropertyType(Class paramClass) {
/* 190 */     this.propertyTypeRef = getWeakReference(paramClass);
/*     */   }
/*     */ 
/*     */   private Class getPropertyType0() {
/* 194 */     return this.propertyTypeRef != null ? (Class)this.propertyTypeRef.get() : null;
/*     */   }
/*     */ 
/*     */   public synchronized Method getReadMethod()
/*     */   {
/* 206 */     Method localMethod = this.readMethodRef.get();
/* 207 */     if (localMethod == null) {
/* 208 */       Class localClass1 = getClass0();
/* 209 */       if ((localClass1 == null) || ((this.readMethodName == null) && (!this.readMethodRef.isSet())))
/*     */       {
/* 211 */         return null;
/*     */       }
/* 213 */       String str = "get" + getBaseName();
/* 214 */       if (this.readMethodName == null) {
/* 215 */         Class localClass2 = getPropertyType0();
/* 216 */         if ((localClass2 == Boolean.TYPE) || (localClass2 == null))
/* 217 */           this.readMethodName = ("is" + getBaseName());
/*     */         else {
/* 219 */           this.readMethodName = str;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 228 */       localMethod = Introspector.findMethod(localClass1, this.readMethodName, 0);
/* 229 */       if ((localMethod == null) && (!this.readMethodName.equals(str))) {
/* 230 */         this.readMethodName = str;
/* 231 */         localMethod = Introspector.findMethod(localClass1, this.readMethodName, 0);
/*     */       }
/*     */       try {
/* 234 */         setReadMethod(localMethod);
/*     */       }
/*     */       catch (IntrospectionException localIntrospectionException) {
/*     */       }
/*     */     }
/* 239 */     return localMethod;
/*     */   }
/*     */ 
/*     */   public synchronized void setReadMethod(Method paramMethod)
/*     */     throws IntrospectionException
/*     */   {
/* 249 */     this.readMethodRef.set(paramMethod);
/* 250 */     if (paramMethod == null) {
/* 251 */       this.readMethodName = null;
/* 252 */       return;
/*     */     }
/*     */ 
/* 255 */     setPropertyType(findPropertyType(paramMethod, this.writeMethodRef.get()));
/* 256 */     setClass0(paramMethod.getDeclaringClass());
/*     */ 
/* 258 */     this.readMethodName = paramMethod.getName();
/* 259 */     setTransient((Transient)paramMethod.getAnnotation(Transient.class));
/*     */   }
/*     */ 
/*     */   public synchronized Method getWriteMethod()
/*     */   {
/* 269 */     Method localMethod = this.writeMethodRef.get();
/* 270 */     if (localMethod == null) {
/* 271 */       Class localClass1 = getClass0();
/* 272 */       if ((localClass1 == null) || ((this.writeMethodName == null) && (!this.writeMethodRef.isSet())))
/*     */       {
/* 274 */         return null;
/*     */       }
/*     */ 
/* 278 */       Class localClass2 = getPropertyType0();
/* 279 */       if (localClass2 == null) {
/*     */         try
/*     */         {
/* 282 */           localClass2 = findPropertyType(getReadMethod(), null);
/* 283 */           setPropertyType(localClass2);
/*     */         }
/*     */         catch (IntrospectionException localIntrospectionException1)
/*     */         {
/* 287 */           return null;
/*     */         }
/*     */       }
/*     */ 
/* 291 */       if (this.writeMethodName == null) {
/* 292 */         this.writeMethodName = ("set" + getBaseName());
/*     */       }
/*     */ 
/* 295 */       Class[] arrayOfClass = { localClass2 == null ? null : localClass2 };
/* 296 */       localMethod = Introspector.findMethod(localClass1, this.writeMethodName, 1, arrayOfClass);
/* 297 */       if ((localMethod != null) && 
/* 298 */         (!localMethod.getReturnType().equals(Void.TYPE))) {
/* 299 */         localMethod = null;
/*     */       }
/*     */       try
/*     */       {
/* 303 */         setWriteMethod(localMethod);
/*     */       }
/*     */       catch (IntrospectionException localIntrospectionException2) {
/*     */       }
/*     */     }
/* 308 */     return localMethod;
/*     */   }
/*     */ 
/*     */   public synchronized void setWriteMethod(Method paramMethod)
/*     */     throws IntrospectionException
/*     */   {
/* 318 */     this.writeMethodRef.set(paramMethod);
/* 319 */     if (paramMethod == null) {
/* 320 */       this.writeMethodName = null;
/* 321 */       return;
/*     */     }
/*     */ 
/* 324 */     setPropertyType(findPropertyType(getReadMethod(), paramMethod));
/* 325 */     setClass0(paramMethod.getDeclaringClass());
/*     */ 
/* 327 */     this.writeMethodName = paramMethod.getName();
/* 328 */     setTransient((Transient)paramMethod.getAnnotation(Transient.class));
/*     */   }
/*     */ 
/*     */   void setClass0(Class paramClass)
/*     */   {
/* 335 */     if ((getClass0() != null) && (paramClass.isAssignableFrom(getClass0())))
/*     */     {
/* 337 */       return;
/*     */     }
/* 339 */     super.setClass0(paramClass);
/*     */   }
/*     */ 
/*     */   public boolean isBound()
/*     */   {
/* 349 */     return this.bound;
/*     */   }
/*     */ 
/*     */   public void setBound(boolean paramBoolean)
/*     */   {
/* 359 */     this.bound = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isConstrained()
/*     */   {
/* 369 */     return this.constrained;
/*     */   }
/*     */ 
/*     */   public void setConstrained(boolean paramBoolean)
/*     */   {
/* 379 */     this.constrained = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setPropertyEditorClass(Class<?> paramClass)
/*     */   {
/* 392 */     this.propertyEditorClassRef = getWeakReference(paramClass);
/*     */   }
/*     */ 
/*     */   public Class<?> getPropertyEditorClass()
/*     */   {
/* 406 */     return this.propertyEditorClassRef != null ? (Class)this.propertyEditorClassRef.get() : null;
/*     */   }
/*     */ 
/*     */   public PropertyEditor createPropertyEditor(Object paramObject)
/*     */   {
/* 425 */     Object localObject = null;
/*     */ 
/* 427 */     Class localClass = getPropertyEditorClass();
/* 428 */     if (localClass != null) {
/* 429 */       Constructor localConstructor = null;
/* 430 */       if (paramObject != null)
/*     */         try {
/* 432 */           localConstructor = localClass.getConstructor(new Class[] { Object.class });
/*     */         }
/*     */         catch (Exception localException1)
/*     */         {
/*     */         }
/*     */       try {
/* 438 */         if (localConstructor == null)
/* 439 */           localObject = localClass.newInstance();
/*     */         else {
/* 441 */           localObject = localConstructor.newInstance(new Object[] { paramObject });
/*     */         }
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/* 446 */         throw new RuntimeException("PropertyEditor not instantiated", localException2);
/*     */       }
/*     */     }
/*     */ 
/* 450 */     return (PropertyEditor)localObject;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 463 */     if (this == paramObject) {
/* 464 */       return true;
/*     */     }
/* 466 */     if ((paramObject != null) && ((paramObject instanceof PropertyDescriptor))) {
/* 467 */       PropertyDescriptor localPropertyDescriptor = (PropertyDescriptor)paramObject;
/* 468 */       Method localMethod1 = localPropertyDescriptor.getReadMethod();
/* 469 */       Method localMethod2 = localPropertyDescriptor.getWriteMethod();
/*     */ 
/* 471 */       if (!compareMethods(getReadMethod(), localMethod1)) {
/* 472 */         return false;
/*     */       }
/*     */ 
/* 475 */       if (!compareMethods(getWriteMethod(), localMethod2)) {
/* 476 */         return false;
/*     */       }
/*     */ 
/* 479 */       if ((getPropertyType() == localPropertyDescriptor.getPropertyType()) && (getPropertyEditorClass() == localPropertyDescriptor.getPropertyEditorClass()) && (this.bound == localPropertyDescriptor.isBound()) && (this.constrained == localPropertyDescriptor.isConstrained()) && (this.writeMethodName == localPropertyDescriptor.writeMethodName) && (this.readMethodName == localPropertyDescriptor.readMethodName))
/*     */       {
/* 484 */         return true;
/*     */       }
/*     */     }
/* 487 */     return false;
/*     */   }
/*     */ 
/*     */   boolean compareMethods(Method paramMethod1, Method paramMethod2)
/*     */   {
/* 499 */     if ((paramMethod1 == null ? 1 : 0) != (paramMethod2 == null ? 1 : 0)) {
/* 500 */       return false;
/*     */     }
/*     */ 
/* 503 */     if ((paramMethod1 != null) && (paramMethod2 != null) && 
/* 504 */       (!paramMethod1.equals(paramMethod2))) {
/* 505 */       return false;
/*     */     }
/*     */ 
/* 508 */     return true;
/*     */   }
/*     */ 
/*     */   PropertyDescriptor(PropertyDescriptor paramPropertyDescriptor1, PropertyDescriptor paramPropertyDescriptor2)
/*     */   {
/* 520 */     super(paramPropertyDescriptor1, paramPropertyDescriptor2);
/*     */ 
/* 522 */     if (paramPropertyDescriptor2.baseName != null)
/* 523 */       this.baseName = paramPropertyDescriptor2.baseName;
/*     */     else {
/* 525 */       this.baseName = paramPropertyDescriptor1.baseName;
/*     */     }
/*     */ 
/* 528 */     if (paramPropertyDescriptor2.readMethodName != null)
/* 529 */       this.readMethodName = paramPropertyDescriptor2.readMethodName;
/*     */     else {
/* 531 */       this.readMethodName = paramPropertyDescriptor1.readMethodName;
/*     */     }
/*     */ 
/* 534 */     if (paramPropertyDescriptor2.writeMethodName != null)
/* 535 */       this.writeMethodName = paramPropertyDescriptor2.writeMethodName;
/*     */     else {
/* 537 */       this.writeMethodName = paramPropertyDescriptor1.writeMethodName;
/*     */     }
/*     */ 
/* 540 */     if (paramPropertyDescriptor2.propertyTypeRef != null)
/* 541 */       this.propertyTypeRef = paramPropertyDescriptor2.propertyTypeRef;
/*     */     else {
/* 543 */       this.propertyTypeRef = paramPropertyDescriptor1.propertyTypeRef;
/*     */     }
/*     */ 
/* 547 */     Method localMethod1 = paramPropertyDescriptor1.getReadMethod();
/* 548 */     Method localMethod2 = paramPropertyDescriptor2.getReadMethod();
/*     */     try
/*     */     {
/* 552 */       if (isAssignable(localMethod1, localMethod2))
/* 553 */         setReadMethod(localMethod2);
/*     */       else {
/* 555 */         setReadMethod(localMethod1);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (IntrospectionException localIntrospectionException1)
/*     */     {
/*     */     }
/*     */ 
/* 563 */     if ((localMethod1 != null) && (localMethod2 != null) && (localMethod1.getDeclaringClass() == localMethod2.getDeclaringClass()) && (getReturnType(getClass0(), localMethod1) == Boolean.TYPE) && (getReturnType(getClass0(), localMethod2) == Boolean.TYPE) && (localMethod1.getName().indexOf("is") == 0) && (localMethod2.getName().indexOf("get") == 0))
/*     */     {
/*     */       try
/*     */       {
/* 570 */         setReadMethod(localMethod1);
/*     */       }
/*     */       catch (IntrospectionException localIntrospectionException2)
/*     */       {
/*     */       }
/*     */     }
/* 576 */     Method localMethod3 = paramPropertyDescriptor1.getWriteMethod();
/* 577 */     Method localMethod4 = paramPropertyDescriptor2.getWriteMethod();
/*     */     try
/*     */     {
/* 580 */       if (localMethod4 != null)
/* 581 */         setWriteMethod(localMethod4);
/*     */       else {
/* 583 */         setWriteMethod(localMethod3);
/*     */       }
/*     */     }
/*     */     catch (IntrospectionException localIntrospectionException3)
/*     */     {
/*     */     }
/* 589 */     if (paramPropertyDescriptor2.getPropertyEditorClass() != null)
/* 590 */       setPropertyEditorClass(paramPropertyDescriptor2.getPropertyEditorClass());
/*     */     else {
/* 592 */       setPropertyEditorClass(paramPropertyDescriptor1.getPropertyEditorClass());
/*     */     }
/*     */ 
/* 596 */     paramPropertyDescriptor1.bound |= paramPropertyDescriptor2.bound;
/* 597 */     paramPropertyDescriptor1.constrained |= paramPropertyDescriptor2.constrained;
/*     */   }
/*     */ 
/*     */   PropertyDescriptor(PropertyDescriptor paramPropertyDescriptor)
/*     */   {
/* 605 */     super(paramPropertyDescriptor);
/* 606 */     this.propertyTypeRef = paramPropertyDescriptor.propertyTypeRef;
/* 607 */     this.readMethodRef.set(paramPropertyDescriptor.readMethodRef.get());
/* 608 */     this.writeMethodRef.set(paramPropertyDescriptor.writeMethodRef.get());
/* 609 */     this.propertyEditorClassRef = paramPropertyDescriptor.propertyEditorClassRef;
/*     */ 
/* 611 */     this.writeMethodName = paramPropertyDescriptor.writeMethodName;
/* 612 */     this.readMethodName = paramPropertyDescriptor.readMethodName;
/* 613 */     this.baseName = paramPropertyDescriptor.baseName;
/*     */ 
/* 615 */     this.bound = paramPropertyDescriptor.bound;
/* 616 */     this.constrained = paramPropertyDescriptor.constrained;
/*     */   }
/*     */ 
/*     */   void updateGenericsFor(Class<?> paramClass) {
/* 620 */     setClass0(paramClass);
/*     */     try {
/* 622 */       setPropertyType(findPropertyType(this.readMethodRef.get(), this.writeMethodRef.get()));
/*     */     }
/*     */     catch (IntrospectionException localIntrospectionException) {
/* 625 */       setPropertyType(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Class findPropertyType(Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 639 */     Class localClass = null;
/*     */     try
/*     */     {
/*     */       Class[] arrayOfClass;
/* 641 */       if (paramMethod1 != null) {
/* 642 */         arrayOfClass = getParameterTypes(getClass0(), paramMethod1);
/* 643 */         if (arrayOfClass.length != 0) {
/* 644 */           throw new IntrospectionException("bad read method arg count: " + paramMethod1);
/*     */         }
/*     */ 
/* 647 */         localClass = getReturnType(getClass0(), paramMethod1);
/* 648 */         if (localClass == Void.TYPE) {
/* 649 */           throw new IntrospectionException("read method " + paramMethod1.getName() + " returns void");
/*     */         }
/*     */       }
/*     */ 
/* 653 */       if (paramMethod2 != null) {
/* 654 */         arrayOfClass = getParameterTypes(getClass0(), paramMethod2);
/* 655 */         if (arrayOfClass.length != 1) {
/* 656 */           throw new IntrospectionException("bad write method arg count: " + paramMethod2);
/*     */         }
/*     */ 
/* 659 */         if ((localClass != null) && (!arrayOfClass[0].isAssignableFrom(localClass))) {
/* 660 */           throw new IntrospectionException("type mismatch between read and write methods");
/*     */         }
/* 662 */         localClass = arrayOfClass[0];
/*     */       }
/*     */     } catch (IntrospectionException localIntrospectionException) {
/* 665 */       throw localIntrospectionException;
/*     */     }
/* 667 */     return localClass;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 679 */     int i = 7;
/*     */ 
/* 681 */     i = 37 * i + (getPropertyType() == null ? 0 : getPropertyType().hashCode());
/*     */ 
/* 683 */     i = 37 * i + (getReadMethod() == null ? 0 : getReadMethod().hashCode());
/*     */ 
/* 685 */     i = 37 * i + (getWriteMethod() == null ? 0 : getWriteMethod().hashCode());
/*     */ 
/* 687 */     i = 37 * i + (getPropertyEditorClass() == null ? 0 : getPropertyEditorClass().hashCode());
/*     */ 
/* 689 */     i = 37 * i + (this.writeMethodName == null ? 0 : this.writeMethodName.hashCode());
/*     */ 
/* 691 */     i = 37 * i + (this.readMethodName == null ? 0 : this.readMethodName.hashCode());
/*     */ 
/* 693 */     i = 37 * i + getName().hashCode();
/* 694 */     i = 37 * i + (!this.bound ? 0 : 1);
/* 695 */     i = 37 * i + (!this.constrained ? 0 : 1);
/*     */ 
/* 697 */     return i;
/*     */   }
/*     */ 
/*     */   String getBaseName()
/*     */   {
/* 702 */     if (this.baseName == null) {
/* 703 */       this.baseName = NameGenerator.capitalize(getName());
/*     */     }
/* 705 */     return this.baseName;
/*     */   }
/*     */ 
/*     */   void appendTo(StringBuilder paramStringBuilder) {
/* 709 */     appendTo(paramStringBuilder, "bound", this.bound);
/* 710 */     appendTo(paramStringBuilder, "constrained", this.constrained);
/* 711 */     appendTo(paramStringBuilder, "propertyEditorClass", this.propertyEditorClassRef);
/* 712 */     appendTo(paramStringBuilder, "propertyType", this.propertyTypeRef);
/* 713 */     appendTo(paramStringBuilder, "readMethod", this.readMethodRef.get());
/* 714 */     appendTo(paramStringBuilder, "writeMethod", this.writeMethodRef.get());
/*     */   }
/*     */ 
/*     */   private boolean isAssignable(Method paramMethod1, Method paramMethod2) {
/* 718 */     if (paramMethod1 == null) {
/* 719 */       return true;
/*     */     }
/* 721 */     if (paramMethod2 == null) {
/* 722 */       return false;
/*     */     }
/* 724 */     if (!paramMethod1.getName().equals(paramMethod2.getName())) {
/* 725 */       return true;
/*     */     }
/* 727 */     Class localClass1 = paramMethod1.getDeclaringClass();
/* 728 */     Class localClass2 = paramMethod2.getDeclaringClass();
/* 729 */     if (!localClass1.isAssignableFrom(localClass2)) {
/* 730 */       return false;
/*     */     }
/* 732 */     localClass1 = getReturnType(getClass0(), paramMethod1);
/* 733 */     localClass2 = getReturnType(getClass0(), paramMethod2);
/* 734 */     if (!localClass1.isAssignableFrom(localClass2)) {
/* 735 */       return false;
/*     */     }
/* 737 */     Class[] arrayOfClass1 = getParameterTypes(getClass0(), paramMethod1);
/* 738 */     Class[] arrayOfClass2 = getParameterTypes(getClass0(), paramMethod2);
/* 739 */     if (arrayOfClass1.length != arrayOfClass2.length) {
/* 740 */       return true;
/*     */     }
/* 742 */     for (int i = 0; i < arrayOfClass1.length; i++) {
/* 743 */       if (!arrayOfClass1[i].isAssignableFrom(arrayOfClass2[i])) {
/* 744 */         return false;
/*     */       }
/*     */     }
/* 747 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PropertyDescriptor
 * JD-Core Version:    0.6.2
 */