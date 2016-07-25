/*     */ package java.beans;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class IndexedPropertyDescriptor extends PropertyDescriptor
/*     */ {
/*     */   private Reference<Class> indexedPropertyTypeRef;
/*  44 */   private final MethodRef indexedReadMethodRef = new MethodRef();
/*  45 */   private final MethodRef indexedWriteMethodRef = new MethodRef();
/*     */   private String indexedReadMethodName;
/*     */   private String indexedWriteMethodName;
/*     */ 
/*     */   public IndexedPropertyDescriptor(String paramString, Class<?> paramClass)
/*     */     throws IntrospectionException
/*     */   {
/*  67 */     this(paramString, paramClass, "get" + NameGenerator.capitalize(paramString), "set" + NameGenerator.capitalize(paramString), "get" + NameGenerator.capitalize(paramString), "set" + NameGenerator.capitalize(paramString));
/*     */   }
/*     */ 
/*     */   public IndexedPropertyDescriptor(String paramString1, Class<?> paramClass, String paramString2, String paramString3, String paramString4, String paramString5)
/*     */     throws IntrospectionException
/*     */   {
/* 100 */     super(paramString1, paramClass, paramString2, paramString3);
/*     */ 
/* 102 */     this.indexedReadMethodName = paramString4;
/* 103 */     if ((paramString4 != null) && (getIndexedReadMethod() == null)) {
/* 104 */       throw new IntrospectionException("Method not found: " + paramString4);
/*     */     }
/*     */ 
/* 107 */     this.indexedWriteMethodName = paramString5;
/* 108 */     if ((paramString5 != null) && (getIndexedWriteMethod() == null)) {
/* 109 */       throw new IntrospectionException("Method not found: " + paramString5);
/*     */     }
/*     */ 
/* 112 */     findIndexedPropertyType(getIndexedReadMethod(), getIndexedWriteMethod());
/*     */   }
/*     */ 
/*     */   public IndexedPropertyDescriptor(String paramString, Method paramMethod1, Method paramMethod2, Method paramMethod3, Method paramMethod4)
/*     */     throws IntrospectionException
/*     */   {
/* 134 */     super(paramString, paramMethod1, paramMethod2);
/*     */ 
/* 136 */     setIndexedReadMethod0(paramMethod3);
/* 137 */     setIndexedWriteMethod0(paramMethod4);
/*     */ 
/* 140 */     setIndexedPropertyType(findIndexedPropertyType(paramMethod3, paramMethod4));
/*     */   }
/*     */ 
/*     */   IndexedPropertyDescriptor(Class<?> paramClass, String paramString, Method paramMethod1, Method paramMethod2, Method paramMethod3, Method paramMethod4)
/*     */     throws IntrospectionException
/*     */   {
/* 158 */     super(paramClass, paramString, paramMethod1, paramMethod2);
/*     */ 
/* 160 */     setIndexedReadMethod0(paramMethod3);
/* 161 */     setIndexedWriteMethod0(paramMethod4);
/*     */ 
/* 164 */     setIndexedPropertyType(findIndexedPropertyType(paramMethod3, paramMethod4));
/*     */   }
/*     */ 
/*     */   public synchronized Method getIndexedReadMethod()
/*     */   {
/* 176 */     Method localMethod = this.indexedReadMethodRef.get();
/* 177 */     if (localMethod == null) {
/* 178 */       Class localClass = getClass0();
/* 179 */       if ((localClass == null) || ((this.indexedReadMethodName == null) && (!this.indexedReadMethodRef.isSet())))
/*     */       {
/* 182 */         return null;
/*     */       }
/* 184 */       String str = "get" + getBaseName();
/* 185 */       if (this.indexedReadMethodName == null) {
/* 186 */         localObject = getIndexedPropertyType0();
/* 187 */         if ((localObject == Boolean.TYPE) || (localObject == null))
/* 188 */           this.indexedReadMethodName = ("is" + getBaseName());
/*     */         else {
/* 190 */           this.indexedReadMethodName = str;
/*     */         }
/*     */       }
/*     */ 
/* 194 */       Object localObject = { Integer.TYPE };
/* 195 */       localMethod = Introspector.findMethod(localClass, this.indexedReadMethodName, 1, (Class[])localObject);
/* 196 */       if ((localMethod == null) && (!this.indexedReadMethodName.equals(str)))
/*     */       {
/* 198 */         this.indexedReadMethodName = str;
/* 199 */         localMethod = Introspector.findMethod(localClass, this.indexedReadMethodName, 1, (Class[])localObject);
/*     */       }
/* 201 */       setIndexedReadMethod0(localMethod);
/*     */     }
/* 203 */     return localMethod;
/*     */   }
/*     */ 
/*     */   public synchronized void setIndexedReadMethod(Method paramMethod)
/*     */     throws IntrospectionException
/*     */   {
/* 215 */     setIndexedPropertyType(findIndexedPropertyType(paramMethod, this.indexedWriteMethodRef.get()));
/*     */ 
/* 217 */     setIndexedReadMethod0(paramMethod);
/*     */   }
/*     */ 
/*     */   private void setIndexedReadMethod0(Method paramMethod) {
/* 221 */     this.indexedReadMethodRef.set(paramMethod);
/* 222 */     if (paramMethod == null) {
/* 223 */       this.indexedReadMethodName = null;
/* 224 */       return;
/*     */     }
/* 226 */     setClass0(paramMethod.getDeclaringClass());
/*     */ 
/* 228 */     this.indexedReadMethodName = paramMethod.getName();
/* 229 */     setTransient((Transient)paramMethod.getAnnotation(Transient.class));
/*     */   }
/*     */ 
/*     */   public synchronized Method getIndexedWriteMethod()
/*     */   {
/* 241 */     Method localMethod = this.indexedWriteMethodRef.get();
/* 242 */     if (localMethod == null) {
/* 243 */       Class localClass1 = getClass0();
/* 244 */       if ((localClass1 == null) || ((this.indexedWriteMethodName == null) && (!this.indexedWriteMethodRef.isSet())))
/*     */       {
/* 247 */         return null;
/*     */       }
/*     */ 
/* 253 */       Class localClass2 = getIndexedPropertyType0();
/* 254 */       if (localClass2 == null) {
/*     */         try {
/* 256 */           localClass2 = findIndexedPropertyType(getIndexedReadMethod(), null);
/* 257 */           setIndexedPropertyType(localClass2);
/*     */         }
/*     */         catch (IntrospectionException localIntrospectionException) {
/* 260 */           Class localClass3 = getPropertyType();
/* 261 */           if (localClass3.isArray()) {
/* 262 */             localClass2 = localClass3.getComponentType();
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 267 */       if (this.indexedWriteMethodName == null) {
/* 268 */         this.indexedWriteMethodName = ("set" + getBaseName());
/*     */       }
/*     */ 
/* 271 */       Class[] arrayOfClass = { Integer.TYPE, localClass2 == null ? null : localClass2 };
/* 272 */       localMethod = Introspector.findMethod(localClass1, this.indexedWriteMethodName, 2, arrayOfClass);
/* 273 */       if ((localMethod != null) && 
/* 274 */         (!localMethod.getReturnType().equals(Void.TYPE))) {
/* 275 */         localMethod = null;
/*     */       }
/*     */ 
/* 278 */       setIndexedWriteMethod0(localMethod);
/*     */     }
/* 280 */     return localMethod;
/*     */   }
/*     */ 
/*     */   public synchronized void setIndexedWriteMethod(Method paramMethod)
/*     */     throws IntrospectionException
/*     */   {
/* 292 */     Class localClass = findIndexedPropertyType(getIndexedReadMethod(), paramMethod);
/*     */ 
/* 294 */     setIndexedPropertyType(localClass);
/* 295 */     setIndexedWriteMethod0(paramMethod);
/*     */   }
/*     */ 
/*     */   private void setIndexedWriteMethod0(Method paramMethod) {
/* 299 */     this.indexedWriteMethodRef.set(paramMethod);
/* 300 */     if (paramMethod == null) {
/* 301 */       this.indexedWriteMethodName = null;
/* 302 */       return;
/*     */     }
/* 304 */     setClass0(paramMethod.getDeclaringClass());
/*     */ 
/* 306 */     this.indexedWriteMethodName = paramMethod.getName();
/* 307 */     setTransient((Transient)paramMethod.getAnnotation(Transient.class));
/*     */   }
/*     */ 
/*     */   public synchronized Class<?> getIndexedPropertyType()
/*     */   {
/* 321 */     Class localClass = getIndexedPropertyType0();
/* 322 */     if (localClass == null)
/*     */       try {
/* 324 */         localClass = findIndexedPropertyType(getIndexedReadMethod(), getIndexedWriteMethod());
/*     */ 
/* 326 */         setIndexedPropertyType(localClass);
/*     */       }
/*     */       catch (IntrospectionException localIntrospectionException)
/*     */       {
/*     */       }
/* 331 */     return localClass;
/*     */   }
/*     */ 
/*     */   private void setIndexedPropertyType(Class paramClass)
/*     */   {
/* 337 */     this.indexedPropertyTypeRef = getWeakReference(paramClass);
/*     */   }
/*     */ 
/*     */   private Class getIndexedPropertyType0() {
/* 341 */     return this.indexedPropertyTypeRef != null ? (Class)this.indexedPropertyTypeRef.get() : null;
/*     */   }
/*     */ 
/*     */   private Class findIndexedPropertyType(Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 349 */     Class localClass = null;
/*     */ 
/* 351 */     if (paramMethod1 != null) {
/* 352 */       localObject = getParameterTypes(getClass0(), paramMethod1);
/* 353 */       if (localObject.length != 1) {
/* 354 */         throw new IntrospectionException("bad indexed read method arg count");
/*     */       }
/* 356 */       if (localObject[0] != Integer.TYPE) {
/* 357 */         throw new IntrospectionException("non int index to indexed read method");
/*     */       }
/* 359 */       localClass = getReturnType(getClass0(), paramMethod1);
/* 360 */       if (localClass == Void.TYPE) {
/* 361 */         throw new IntrospectionException("indexed read method returns void");
/*     */       }
/*     */     }
/* 364 */     if (paramMethod2 != null) {
/* 365 */       localObject = getParameterTypes(getClass0(), paramMethod2);
/* 366 */       if (localObject.length != 2) {
/* 367 */         throw new IntrospectionException("bad indexed write method arg count");
/*     */       }
/* 369 */       if (localObject[0] != Integer.TYPE) {
/* 370 */         throw new IntrospectionException("non int index to indexed write method");
/*     */       }
/* 372 */       if ((localClass != null) && (localClass != localObject[1])) {
/* 373 */         throw new IntrospectionException("type mismatch between indexed read and indexed write methods: " + getName());
/*     */       }
/*     */ 
/* 377 */       localClass = localObject[1];
/*     */     }
/* 379 */     Object localObject = getPropertyType();
/* 380 */     if ((localObject != null) && ((!((Class)localObject).isArray()) || (((Class)localObject).getComponentType() != localClass)))
/*     */     {
/* 382 */       throw new IntrospectionException("type mismatch between indexed and non-indexed methods: " + getName());
/*     */     }
/*     */ 
/* 385 */     return localClass;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 399 */     if (this == paramObject) {
/* 400 */       return true;
/*     */     }
/*     */ 
/* 403 */     if ((paramObject != null) && ((paramObject instanceof IndexedPropertyDescriptor))) {
/* 404 */       IndexedPropertyDescriptor localIndexedPropertyDescriptor = (IndexedPropertyDescriptor)paramObject;
/* 405 */       Method localMethod1 = localIndexedPropertyDescriptor.getIndexedReadMethod();
/* 406 */       Method localMethod2 = localIndexedPropertyDescriptor.getIndexedWriteMethod();
/*     */ 
/* 408 */       if (!compareMethods(getIndexedReadMethod(), localMethod1)) {
/* 409 */         return false;
/*     */       }
/*     */ 
/* 412 */       if (!compareMethods(getIndexedWriteMethod(), localMethod2)) {
/* 413 */         return false;
/*     */       }
/*     */ 
/* 416 */       if (getIndexedPropertyType() != localIndexedPropertyDescriptor.getIndexedPropertyType()) {
/* 417 */         return false;
/*     */       }
/* 419 */       return super.equals(paramObject);
/*     */     }
/* 421 */     return false;
/*     */   }
/*     */ 
/*     */   IndexedPropertyDescriptor(PropertyDescriptor paramPropertyDescriptor1, PropertyDescriptor paramPropertyDescriptor2)
/*     */   {
/* 434 */     super(paramPropertyDescriptor1, paramPropertyDescriptor2);
/*     */     IndexedPropertyDescriptor localIndexedPropertyDescriptor;
/*     */     Method localMethod3;
/* 435 */     if ((paramPropertyDescriptor1 instanceof IndexedPropertyDescriptor)) {
/* 436 */       localIndexedPropertyDescriptor = (IndexedPropertyDescriptor)paramPropertyDescriptor1;
/*     */       try {
/* 438 */         Method localMethod1 = localIndexedPropertyDescriptor.getIndexedReadMethod();
/* 439 */         if (localMethod1 != null) {
/* 440 */           setIndexedReadMethod(localMethod1);
/*     */         }
/*     */ 
/* 443 */         localMethod3 = localIndexedPropertyDescriptor.getIndexedWriteMethod();
/* 444 */         if (localMethod3 != null)
/* 445 */           setIndexedWriteMethod(localMethod3);
/*     */       }
/*     */       catch (IntrospectionException localIntrospectionException1)
/*     */       {
/* 449 */         throw new AssertionError(localIntrospectionException1);
/*     */       }
/*     */     }
/* 452 */     if ((paramPropertyDescriptor2 instanceof IndexedPropertyDescriptor)) {
/* 453 */       localIndexedPropertyDescriptor = (IndexedPropertyDescriptor)paramPropertyDescriptor2;
/*     */       try {
/* 455 */         Method localMethod2 = localIndexedPropertyDescriptor.getIndexedReadMethod();
/* 456 */         if ((localMethod2 != null) && (localMethod2.getDeclaringClass() == getClass0())) {
/* 457 */           setIndexedReadMethod(localMethod2);
/*     */         }
/*     */ 
/* 460 */         localMethod3 = localIndexedPropertyDescriptor.getIndexedWriteMethod();
/* 461 */         if ((localMethod3 != null) && (localMethod3.getDeclaringClass() == getClass0()))
/* 462 */           setIndexedWriteMethod(localMethod3);
/*     */       }
/*     */       catch (IntrospectionException localIntrospectionException2)
/*     */       {
/* 466 */         throw new AssertionError(localIntrospectionException2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   IndexedPropertyDescriptor(IndexedPropertyDescriptor paramIndexedPropertyDescriptor)
/*     */   {
/* 476 */     super(paramIndexedPropertyDescriptor);
/* 477 */     this.indexedReadMethodRef.set(paramIndexedPropertyDescriptor.indexedReadMethodRef.get());
/* 478 */     this.indexedWriteMethodRef.set(paramIndexedPropertyDescriptor.indexedWriteMethodRef.get());
/* 479 */     this.indexedPropertyTypeRef = paramIndexedPropertyDescriptor.indexedPropertyTypeRef;
/* 480 */     this.indexedWriteMethodName = paramIndexedPropertyDescriptor.indexedWriteMethodName;
/* 481 */     this.indexedReadMethodName = paramIndexedPropertyDescriptor.indexedReadMethodName;
/*     */   }
/*     */ 
/*     */   void updateGenericsFor(Class<?> paramClass) {
/* 485 */     super.updateGenericsFor(paramClass);
/*     */     try {
/* 487 */       setIndexedPropertyType(findIndexedPropertyType(this.indexedReadMethodRef.get(), this.indexedWriteMethodRef.get()));
/*     */     }
/*     */     catch (IntrospectionException localIntrospectionException) {
/* 490 */       setIndexedPropertyType(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 502 */     int i = super.hashCode();
/*     */ 
/* 504 */     i = 37 * i + (this.indexedWriteMethodName == null ? 0 : this.indexedWriteMethodName.hashCode());
/*     */ 
/* 506 */     i = 37 * i + (this.indexedReadMethodName == null ? 0 : this.indexedReadMethodName.hashCode());
/*     */ 
/* 508 */     i = 37 * i + (getIndexedPropertyType() == null ? 0 : getIndexedPropertyType().hashCode());
/*     */ 
/* 511 */     return i;
/*     */   }
/*     */ 
/*     */   void appendTo(StringBuilder paramStringBuilder) {
/* 515 */     super.appendTo(paramStringBuilder);
/* 516 */     appendTo(paramStringBuilder, "indexedPropertyType", this.indexedPropertyTypeRef);
/* 517 */     appendTo(paramStringBuilder, "indexedReadMethod", this.indexedReadMethodRef.get());
/* 518 */     appendTo(paramStringBuilder, "indexedWriteMethod", this.indexedWriteMethodRef.get());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.IndexedPropertyDescriptor
 * JD-Core Version:    0.6.2
 */