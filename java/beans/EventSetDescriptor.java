/*     */ package java.beans;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ public class EventSetDescriptor extends FeatureDescriptor
/*     */ {
/*     */   private MethodDescriptor[] listenerMethodDescriptors;
/*     */   private MethodDescriptor addMethodDescriptor;
/*     */   private MethodDescriptor removeMethodDescriptor;
/*     */   private MethodDescriptor getMethodDescriptor;
/*     */   private Reference<Method[]> listenerMethodsRef;
/*     */   private Reference<Class> listenerTypeRef;
/*     */   private boolean unicast;
/*  51 */   private boolean inDefaultEventSet = true;
/*     */ 
/*     */   public EventSetDescriptor(Class<?> paramClass1, String paramString1, Class<?> paramClass2, String paramString2)
/*     */     throws IntrospectionException
/*     */   {
/*  75 */     this(paramClass1, paramString1, paramClass2, new String[] { paramString2 }, "add" + getListenerClassName(paramClass2), "remove" + getListenerClassName(paramClass2), "get" + getListenerClassName(paramClass2) + "s");
/*     */ 
/*  81 */     String str = NameGenerator.capitalize(paramString1) + "Event";
/*  82 */     Method[] arrayOfMethod = getListenerMethods();
/*  83 */     if (arrayOfMethod.length > 0) {
/*  84 */       Class[] arrayOfClass = getParameterTypes(getClass0(), arrayOfMethod[0]);
/*     */ 
/*  86 */       if ((!"vetoableChange".equals(paramString1)) && (!arrayOfClass[0].getName().endsWith(str)))
/*  87 */         throw new IntrospectionException("Method \"" + paramString2 + "\" should have argument \"" + str + "\"");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getListenerClassName(Class paramClass)
/*     */   {
/*  95 */     String str = paramClass.getName();
/*  96 */     return str.substring(str.lastIndexOf('.') + 1);
/*     */   }
/*     */ 
/*     */   public EventSetDescriptor(Class<?> paramClass1, String paramString1, Class<?> paramClass2, String[] paramArrayOfString, String paramString2, String paramString3)
/*     */     throws IntrospectionException
/*     */   {
/* 124 */     this(paramClass1, paramString1, paramClass2, paramArrayOfString, paramString2, paramString3, null);
/*     */   }
/*     */ 
/*     */   public EventSetDescriptor(Class<?> paramClass1, String paramString1, Class<?> paramClass2, String[] paramArrayOfString, String paramString2, String paramString3, String paramString4)
/*     */     throws IntrospectionException
/*     */   {
/* 158 */     if ((paramClass1 == null) || (paramString1 == null) || (paramClass2 == null)) {
/* 159 */       throw new NullPointerException();
/*     */     }
/* 161 */     setName(paramString1);
/* 162 */     setClass0(paramClass1);
/* 163 */     setListenerType(paramClass2);
/*     */ 
/* 165 */     Method[] arrayOfMethod = new Method[paramArrayOfString.length];
/* 166 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/* 168 */       if (paramArrayOfString[i] == null) {
/* 169 */         throw new NullPointerException();
/*     */       }
/* 171 */       arrayOfMethod[i] = getMethod(paramClass2, paramArrayOfString[i], 1);
/*     */     }
/* 173 */     setListenerMethods(arrayOfMethod);
/*     */ 
/* 175 */     setAddListenerMethod(getMethod(paramClass1, paramString2, 1));
/* 176 */     setRemoveListenerMethod(getMethod(paramClass1, paramString3, 1));
/*     */ 
/* 179 */     Method localMethod = Introspector.findMethod(paramClass1, paramString4, 0);
/* 180 */     if (localMethod != null)
/* 181 */       setGetListenerMethod(localMethod);
/*     */   }
/*     */ 
/*     */   private static Method getMethod(Class paramClass, String paramString, int paramInt)
/*     */     throws IntrospectionException
/*     */   {
/* 187 */     if (paramString == null) {
/* 188 */       return null;
/*     */     }
/* 190 */     Method localMethod = Introspector.findMethod(paramClass, paramString, paramInt);
/* 191 */     if ((localMethod == null) || (Modifier.isStatic(localMethod.getModifiers()))) {
/* 192 */       throw new IntrospectionException("Method not found: " + paramString + " on class " + paramClass.getName());
/*     */     }
/*     */ 
/* 195 */     return localMethod;
/*     */   }
/*     */ 
/*     */   public EventSetDescriptor(String paramString, Class<?> paramClass, Method[] paramArrayOfMethod, Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 219 */     this(paramString, paramClass, paramArrayOfMethod, paramMethod1, paramMethod2, null);
/*     */   }
/*     */ 
/*     */   public EventSetDescriptor(String paramString, Class<?> paramClass, Method[] paramArrayOfMethod, Method paramMethod1, Method paramMethod2, Method paramMethod3)
/*     */     throws IntrospectionException
/*     */   {
/* 248 */     setName(paramString);
/* 249 */     setListenerMethods(paramArrayOfMethod);
/* 250 */     setAddListenerMethod(paramMethod1);
/* 251 */     setRemoveListenerMethod(paramMethod2);
/* 252 */     setGetListenerMethod(paramMethod3);
/* 253 */     setListenerType(paramClass);
/*     */   }
/*     */ 
/*     */   public EventSetDescriptor(String paramString, Class<?> paramClass, MethodDescriptor[] paramArrayOfMethodDescriptor, Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 279 */     setName(paramString);
/* 280 */     this.listenerMethodDescriptors = paramArrayOfMethodDescriptor;
/* 281 */     setAddListenerMethod(paramMethod1);
/* 282 */     setRemoveListenerMethod(paramMethod2);
/* 283 */     setListenerType(paramClass);
/*     */   }
/*     */ 
/*     */   public Class<?> getListenerType()
/*     */   {
/* 293 */     return this.listenerTypeRef != null ? (Class)this.listenerTypeRef.get() : null;
/*     */   }
/*     */ 
/*     */   private void setListenerType(Class paramClass)
/*     */   {
/* 299 */     this.listenerTypeRef = getWeakReference(paramClass);
/*     */   }
/*     */ 
/*     */   public synchronized Method[] getListenerMethods()
/*     */   {
/* 310 */     Method[] arrayOfMethod = getListenerMethods0();
/* 311 */     if (arrayOfMethod == null) {
/* 312 */       if (this.listenerMethodDescriptors != null) {
/* 313 */         arrayOfMethod = new Method[this.listenerMethodDescriptors.length];
/* 314 */         for (int i = 0; i < arrayOfMethod.length; i++) {
/* 315 */           arrayOfMethod[i] = this.listenerMethodDescriptors[i].getMethod();
/*     */         }
/*     */       }
/* 318 */       setListenerMethods(arrayOfMethod);
/*     */     }
/* 320 */     return arrayOfMethod;
/*     */   }
/*     */ 
/*     */   private void setListenerMethods(Method[] paramArrayOfMethod) {
/* 324 */     if (paramArrayOfMethod == null) {
/* 325 */       return;
/*     */     }
/* 327 */     if (this.listenerMethodDescriptors == null) {
/* 328 */       this.listenerMethodDescriptors = new MethodDescriptor[paramArrayOfMethod.length];
/* 329 */       for (int i = 0; i < paramArrayOfMethod.length; i++) {
/* 330 */         this.listenerMethodDescriptors[i] = new MethodDescriptor(paramArrayOfMethod[i]);
/*     */       }
/*     */     }
/* 333 */     this.listenerMethodsRef = getSoftReference(paramArrayOfMethod);
/*     */   }
/*     */ 
/*     */   private Method[] getListenerMethods0() {
/* 337 */     return this.listenerMethodsRef != null ? (Method[])this.listenerMethodsRef.get() : null;
/*     */   }
/*     */ 
/*     */   public synchronized MethodDescriptor[] getListenerMethodDescriptors()
/*     */   {
/* 350 */     return this.listenerMethodDescriptors;
/*     */   }
/*     */ 
/*     */   public synchronized Method getAddListenerMethod()
/*     */   {
/* 359 */     return getMethod(this.addMethodDescriptor);
/*     */   }
/*     */ 
/*     */   private synchronized void setAddListenerMethod(Method paramMethod) {
/* 363 */     if (paramMethod == null) {
/* 364 */       return;
/*     */     }
/* 366 */     if (getClass0() == null) {
/* 367 */       setClass0(paramMethod.getDeclaringClass());
/*     */     }
/* 369 */     this.addMethodDescriptor = new MethodDescriptor(paramMethod);
/* 370 */     setTransient((Transient)paramMethod.getAnnotation(Transient.class));
/*     */   }
/*     */ 
/*     */   public synchronized Method getRemoveListenerMethod()
/*     */   {
/* 379 */     return getMethod(this.removeMethodDescriptor);
/*     */   }
/*     */ 
/*     */   private synchronized void setRemoveListenerMethod(Method paramMethod) {
/* 383 */     if (paramMethod == null) {
/* 384 */       return;
/*     */     }
/* 386 */     if (getClass0() == null) {
/* 387 */       setClass0(paramMethod.getDeclaringClass());
/*     */     }
/* 389 */     this.removeMethodDescriptor = new MethodDescriptor(paramMethod);
/* 390 */     setTransient((Transient)paramMethod.getAnnotation(Transient.class));
/*     */   }
/*     */ 
/*     */   public synchronized Method getGetListenerMethod()
/*     */   {
/* 401 */     return getMethod(this.getMethodDescriptor);
/*     */   }
/*     */ 
/*     */   private synchronized void setGetListenerMethod(Method paramMethod) {
/* 405 */     if (paramMethod == null) {
/* 406 */       return;
/*     */     }
/* 408 */     if (getClass0() == null) {
/* 409 */       setClass0(paramMethod.getDeclaringClass());
/*     */     }
/* 411 */     this.getMethodDescriptor = new MethodDescriptor(paramMethod);
/* 412 */     setTransient((Transient)paramMethod.getAnnotation(Transient.class));
/*     */   }
/*     */ 
/*     */   public void setUnicast(boolean paramBoolean)
/*     */   {
/* 421 */     this.unicast = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isUnicast()
/*     */   {
/* 432 */     return this.unicast;
/*     */   }
/*     */ 
/*     */   public void setInDefaultEventSet(boolean paramBoolean)
/*     */   {
/* 444 */     this.inDefaultEventSet = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isInDefaultEventSet()
/*     */   {
/* 454 */     return this.inDefaultEventSet;
/*     */   }
/*     */ 
/*     */   EventSetDescriptor(EventSetDescriptor paramEventSetDescriptor1, EventSetDescriptor paramEventSetDescriptor2)
/*     */   {
/* 466 */     super(paramEventSetDescriptor1, paramEventSetDescriptor2);
/* 467 */     this.listenerMethodDescriptors = paramEventSetDescriptor1.listenerMethodDescriptors;
/* 468 */     if (paramEventSetDescriptor2.listenerMethodDescriptors != null) {
/* 469 */       this.listenerMethodDescriptors = paramEventSetDescriptor2.listenerMethodDescriptors;
/*     */     }
/*     */ 
/* 472 */     this.listenerTypeRef = paramEventSetDescriptor1.listenerTypeRef;
/* 473 */     if (paramEventSetDescriptor2.listenerTypeRef != null) {
/* 474 */       this.listenerTypeRef = paramEventSetDescriptor2.listenerTypeRef;
/*     */     }
/*     */ 
/* 477 */     this.addMethodDescriptor = paramEventSetDescriptor1.addMethodDescriptor;
/* 478 */     if (paramEventSetDescriptor2.addMethodDescriptor != null) {
/* 479 */       this.addMethodDescriptor = paramEventSetDescriptor2.addMethodDescriptor;
/*     */     }
/*     */ 
/* 482 */     this.removeMethodDescriptor = paramEventSetDescriptor1.removeMethodDescriptor;
/* 483 */     if (paramEventSetDescriptor2.removeMethodDescriptor != null) {
/* 484 */       this.removeMethodDescriptor = paramEventSetDescriptor2.removeMethodDescriptor;
/*     */     }
/*     */ 
/* 487 */     this.getMethodDescriptor = paramEventSetDescriptor1.getMethodDescriptor;
/* 488 */     if (paramEventSetDescriptor2.getMethodDescriptor != null) {
/* 489 */       this.getMethodDescriptor = paramEventSetDescriptor2.getMethodDescriptor;
/*     */     }
/*     */ 
/* 492 */     this.unicast = paramEventSetDescriptor2.unicast;
/* 493 */     if ((!paramEventSetDescriptor1.inDefaultEventSet) || (!paramEventSetDescriptor2.inDefaultEventSet))
/* 494 */       this.inDefaultEventSet = false;
/*     */   }
/*     */ 
/*     */   EventSetDescriptor(EventSetDescriptor paramEventSetDescriptor)
/*     */   {
/* 503 */     super(paramEventSetDescriptor);
/* 504 */     if (paramEventSetDescriptor.listenerMethodDescriptors != null) {
/* 505 */       int i = paramEventSetDescriptor.listenerMethodDescriptors.length;
/* 506 */       this.listenerMethodDescriptors = new MethodDescriptor[i];
/* 507 */       for (int j = 0; j < i; j++) {
/* 508 */         this.listenerMethodDescriptors[j] = new MethodDescriptor(paramEventSetDescriptor.listenerMethodDescriptors[j]);
/*     */       }
/*     */     }
/*     */ 
/* 512 */     this.listenerTypeRef = paramEventSetDescriptor.listenerTypeRef;
/*     */ 
/* 514 */     this.addMethodDescriptor = paramEventSetDescriptor.addMethodDescriptor;
/* 515 */     this.removeMethodDescriptor = paramEventSetDescriptor.removeMethodDescriptor;
/* 516 */     this.getMethodDescriptor = paramEventSetDescriptor.getMethodDescriptor;
/*     */ 
/* 518 */     this.unicast = paramEventSetDescriptor.unicast;
/* 519 */     this.inDefaultEventSet = paramEventSetDescriptor.inDefaultEventSet;
/*     */   }
/*     */ 
/*     */   void appendTo(StringBuilder paramStringBuilder) {
/* 523 */     appendTo(paramStringBuilder, "unicast", this.unicast);
/* 524 */     appendTo(paramStringBuilder, "inDefaultEventSet", this.inDefaultEventSet);
/* 525 */     appendTo(paramStringBuilder, "listenerType", this.listenerTypeRef);
/* 526 */     appendTo(paramStringBuilder, "getListenerMethod", getMethod(this.getMethodDescriptor));
/* 527 */     appendTo(paramStringBuilder, "addListenerMethod", getMethod(this.addMethodDescriptor));
/* 528 */     appendTo(paramStringBuilder, "removeListenerMethod", getMethod(this.removeMethodDescriptor));
/*     */   }
/*     */ 
/*     */   private static Method getMethod(MethodDescriptor paramMethodDescriptor) {
/* 532 */     return paramMethodDescriptor != null ? paramMethodDescriptor.getMethod() : null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.EventSetDescriptor
 * JD-Core Version:    0.6.2
 */