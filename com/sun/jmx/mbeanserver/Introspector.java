/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.DescriptorKey;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.ImmutableDescriptor;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class Introspector
/*     */ {
/*     */   public static final boolean isDynamic(Class<?> paramClass)
/*     */   {
/* 112 */     return DynamicMBean.class.isAssignableFrom(paramClass);
/*     */   }
/*     */ 
/*     */   public static void testCreation(Class<?> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/* 132 */     int i = paramClass.getModifiers();
/* 133 */     if ((Modifier.isAbstract(i)) || (Modifier.isInterface(i))) {
/* 134 */       throw new NotCompliantMBeanException("MBean class must be concrete");
/*     */     }
/*     */ 
/* 138 */     Constructor[] arrayOfConstructor = paramClass.getConstructors();
/* 139 */     if (arrayOfConstructor.length == 0)
/* 140 */       throw new NotCompliantMBeanException("MBean class must have public constructor");
/*     */   }
/*     */ 
/*     */   public static void checkCompliance(Class<?> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/* 148 */     if (DynamicMBean.class.isAssignableFrom(paramClass)) {
/* 149 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 154 */       getStandardMBeanInterface(paramClass);
/* 155 */       return;
/*     */     } catch (NotCompliantMBeanException localNotCompliantMBeanException2) {
/* 157 */       NotCompliantMBeanException localNotCompliantMBeanException1 = localNotCompliantMBeanException2;
/*     */       try
/*     */       {
/* 163 */         getMXBeanInterface(paramClass);
/* 164 */         return;
/*     */       } catch (NotCompliantMBeanException localNotCompliantMBeanException3) {
/* 166 */         Object localObject = localNotCompliantMBeanException3;
/*     */ 
/* 168 */         String str = "MBean class " + paramClass.getName() + " does not implement " + "DynamicMBean, and neither follows the Standard MBean conventions (" + localNotCompliantMBeanException1.toString() + ") nor the MXBean conventions (" + localObject.toString() + ")";
/*     */ 
/* 173 */         throw new NotCompliantMBeanException(str);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 178 */   public static <T> DynamicMBean makeDynamicMBean(T paramT) throws NotCompliantMBeanException { if ((paramT instanceof DynamicMBean))
/* 179 */       return (DynamicMBean)paramT;
/* 180 */     Class localClass1 = paramT.getClass();
/* 181 */     Class localClass2 = null;
/*     */     try {
/* 183 */       localClass2 = (Class)Util.cast(getStandardMBeanInterface(localClass1));
/*     */     }
/*     */     catch (NotCompliantMBeanException localNotCompliantMBeanException1)
/*     */     {
/*     */     }
/* 188 */     if (localClass2 != null)
/* 189 */       return new StandardMBeanSupport(paramT, localClass2);
/*     */     try
/*     */     {
/* 192 */       localClass2 = (Class)Util.cast(getMXBeanInterface(localClass1));
/*     */     }
/*     */     catch (NotCompliantMBeanException localNotCompliantMBeanException2)
/*     */     {
/*     */     }
/*     */ 
/* 198 */     if (localClass2 != null)
/* 199 */       return new MXBeanSupport(paramT, localClass2);
/* 200 */     checkCompliance(localClass1);
/* 201 */     throw new NotCompliantMBeanException("Not compliant");
/*     */   }
/*     */ 
/*     */   public static MBeanInfo testCompliance(Class<?> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/* 222 */     if (isDynamic(paramClass)) {
/* 223 */       return null;
/*     */     }
/* 225 */     return testCompliance(paramClass, null);
/*     */   }
/*     */ 
/*     */   public static void testComplianceMXBeanInterface(Class<?> paramClass) throws NotCompliantMBeanException
/*     */   {
/* 230 */     MXBeanIntrospector.getInstance().getAnalyzer(paramClass);
/*     */   }
/*     */ 
/*     */   public static void testComplianceMBeanInterface(Class<?> paramClass) throws NotCompliantMBeanException
/*     */   {
/* 235 */     StandardMBeanIntrospector.getInstance().getAnalyzer(paramClass);
/*     */   }
/*     */ 
/*     */   public static synchronized MBeanInfo testCompliance(Class<?> paramClass1, Class<?> paramClass2)
/*     */     throws NotCompliantMBeanException
/*     */   {
/* 256 */     if (paramClass2 == null)
/* 257 */       paramClass2 = getStandardMBeanInterface(paramClass1);
/* 258 */     ReflectUtil.checkPackageAccess(paramClass2);
/* 259 */     StandardMBeanIntrospector localStandardMBeanIntrospector = StandardMBeanIntrospector.getInstance();
/* 260 */     return getClassMBeanInfo(localStandardMBeanIntrospector, paramClass1, paramClass2);
/*     */   }
/*     */ 
/*     */   private static <M> MBeanInfo getClassMBeanInfo(MBeanIntrospector<M> paramMBeanIntrospector, Class<?> paramClass1, Class<?> paramClass2)
/*     */     throws NotCompliantMBeanException
/*     */   {
/* 267 */     PerInterface localPerInterface = paramMBeanIntrospector.getPerInterface(paramClass2);
/* 268 */     return paramMBeanIntrospector.getClassMBeanInfo(paramClass1, localPerInterface);
/*     */   }
/*     */ 
/*     */   public static Class<?> getMBeanInterface(Class<?> paramClass)
/*     */   {
/* 285 */     if (isDynamic(paramClass)) return null; try
/*     */     {
/* 287 */       return getStandardMBeanInterface(paramClass); } catch (NotCompliantMBeanException localNotCompliantMBeanException) {
/*     */     }
/* 289 */     return null;
/*     */   }
/*     */ 
/*     */   public static <T> Class<? super T> getStandardMBeanInterface(Class<T> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/* 305 */     Object localObject = paramClass;
/* 306 */     Class localClass = null;
/* 307 */     while (localObject != null) {
/* 308 */       localClass = findMBeanInterface((Class)localObject, ((Class)localObject).getName());
/*     */ 
/* 310 */       if (localClass != null) break;
/* 311 */       localObject = ((Class)localObject).getSuperclass();
/*     */     }
/* 313 */     if (localClass != null) {
/* 314 */       return localClass;
/*     */     }
/* 316 */     String str = "Class " + paramClass.getName() + " is not a JMX compliant Standard MBean";
/*     */ 
/* 319 */     throw new NotCompliantMBeanException(str);
/*     */   }
/*     */ 
/*     */   public static <T> Class<? super T> getMXBeanInterface(Class<T> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/*     */     try
/*     */     {
/* 336 */       return MXBeanSupport.findMXBeanInterface(paramClass);
/*     */     } catch (Exception localException) {
/* 338 */       throw throwException(paramClass, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static <T> Class<? super T> findMBeanInterface(Class<T> paramClass, String paramString)
/*     */   {
/* 355 */     Object localObject = paramClass;
/* 356 */     while (localObject != null) {
/* 357 */       Class[] arrayOfClass = ((Class)localObject).getInterfaces();
/* 358 */       int i = arrayOfClass.length;
/* 359 */       for (int j = 0; j < i; j++) {
/* 360 */         Class localClass = (Class)Util.cast(arrayOfClass[j]);
/* 361 */         localClass = implementsMBean(localClass, paramString);
/* 362 */         if (localClass != null) return localClass;
/*     */       }
/* 364 */       localObject = ((Class)localObject).getSuperclass();
/*     */     }
/* 366 */     return null;
/*     */   }
/*     */ 
/*     */   public static Descriptor descriptorForElement(AnnotatedElement paramAnnotatedElement) {
/* 370 */     if (paramAnnotatedElement == null)
/* 371 */       return ImmutableDescriptor.EMPTY_DESCRIPTOR;
/* 372 */     Annotation[] arrayOfAnnotation = paramAnnotatedElement.getAnnotations();
/* 373 */     return descriptorForAnnotations(arrayOfAnnotation);
/*     */   }
/*     */ 
/*     */   public static Descriptor descriptorForAnnotations(Annotation[] paramArrayOfAnnotation) {
/* 377 */     if (paramArrayOfAnnotation.length == 0)
/* 378 */       return ImmutableDescriptor.EMPTY_DESCRIPTOR;
/* 379 */     HashMap localHashMap = new HashMap();
/* 380 */     for (Annotation localAnnotation : paramArrayOfAnnotation) {
/* 381 */       Class localClass = localAnnotation.annotationType();
/* 382 */       Method[] arrayOfMethod1 = localClass.getMethods();
/* 383 */       int k = 0;
/* 384 */       for (Method localMethod : arrayOfMethod1) {
/* 385 */         DescriptorKey localDescriptorKey = (DescriptorKey)localMethod.getAnnotation(DescriptorKey.class);
/* 386 */         if (localDescriptorKey != null) {
/* 387 */           String str1 = localDescriptorKey.value();
/*     */           try
/*     */           {
/* 391 */             if (k == 0) {
/* 392 */               ReflectUtil.checkPackageAccess(localClass);
/* 393 */               k = 1;
/*     */             }
/* 395 */             localObject1 = MethodUtil.invoke(localMethod, localAnnotation, null);
/*     */           }
/*     */           catch (RuntimeException localRuntimeException)
/*     */           {
/* 402 */             throw localRuntimeException;
/*     */           }
/*     */           catch (Exception localException) {
/* 405 */             throw new UndeclaredThrowableException(localException);
/*     */           }
/* 407 */           Object localObject1 = annotationToField(localObject1);
/* 408 */           Object localObject2 = localHashMap.put(str1, localObject1);
/* 409 */           if ((localObject2 != null) && (!equals(localObject2, localObject1))) {
/* 410 */             String str2 = "Inconsistent values for descriptor field " + str1 + " from annotations: " + localObject1 + " :: " + localObject2;
/*     */ 
/* 413 */             throw new IllegalArgumentException(str2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 419 */     if (localHashMap.isEmpty()) {
/* 420 */       return ImmutableDescriptor.EMPTY_DESCRIPTOR;
/*     */     }
/* 422 */     return new ImmutableDescriptor(localHashMap);
/*     */   }
/*     */ 
/*     */   static NotCompliantMBeanException throwException(Class<?> paramClass, Throwable paramThrowable)
/*     */     throws NotCompliantMBeanException, SecurityException
/*     */   {
/* 439 */     if ((paramThrowable instanceof SecurityException))
/* 440 */       throw ((SecurityException)paramThrowable);
/* 441 */     if ((paramThrowable instanceof NotCompliantMBeanException))
/* 442 */       throw ((NotCompliantMBeanException)paramThrowable);
/* 443 */     String str1 = paramClass == null ? "null class" : paramClass.getName();
/*     */ 
/* 445 */     String str2 = paramThrowable == null ? "Not compliant" : paramThrowable.getMessage();
/*     */ 
/* 447 */     NotCompliantMBeanException localNotCompliantMBeanException = new NotCompliantMBeanException(str1 + ": " + str2);
/*     */ 
/* 449 */     localNotCompliantMBeanException.initCause(paramThrowable);
/* 450 */     throw localNotCompliantMBeanException;
/*     */   }
/*     */ 
/*     */   private static Object annotationToField(Object paramObject)
/*     */   {
/* 458 */     if (paramObject == null)
/* 459 */       return null;
/* 460 */     if (((paramObject instanceof Number)) || ((paramObject instanceof String)) || ((paramObject instanceof Character)) || ((paramObject instanceof Boolean)) || ((paramObject instanceof String[])))
/*     */     {
/* 463 */       return paramObject;
/*     */     }
/*     */ 
/* 466 */     Class localClass = paramObject.getClass();
/* 467 */     if (localClass.isArray()) {
/* 468 */       if (localClass.getComponentType().isPrimitive())
/* 469 */         return paramObject;
/* 470 */       Object[] arrayOfObject = (Object[])paramObject;
/* 471 */       String[] arrayOfString = new String[arrayOfObject.length];
/* 472 */       for (int i = 0; i < arrayOfObject.length; i++)
/* 473 */         arrayOfString[i] = ((String)annotationToField(arrayOfObject[i]));
/* 474 */       return arrayOfString;
/*     */     }
/* 476 */     if ((paramObject instanceof Class))
/* 477 */       return ((Class)paramObject).getName();
/* 478 */     if ((paramObject instanceof Enum)) {
/* 479 */       return ((Enum)paramObject).name();
/*     */     }
/*     */ 
/* 486 */     if (Proxy.isProxyClass(localClass))
/* 487 */       localClass = localClass.getInterfaces()[0];
/* 488 */     throw new IllegalArgumentException("Illegal type for annotation element using @DescriptorKey: " + localClass.getName());
/*     */   }
/*     */ 
/*     */   private static boolean equals(Object paramObject1, Object paramObject2)
/*     */   {
/* 496 */     return Arrays.deepEquals(new Object[] { paramObject1 }, new Object[] { paramObject2 });
/*     */   }
/*     */ 
/*     */   private static <T> Class<? super T> implementsMBean(Class<T> paramClass, String paramString)
/*     */   {
/* 506 */     String str = paramString + "MBean";
/* 507 */     if (paramClass.getName().equals(str)) {
/* 508 */       return paramClass;
/*     */     }
/* 510 */     Class[] arrayOfClass = paramClass.getInterfaces();
/* 511 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 512 */       if (arrayOfClass[i].getName().equals(str)) {
/* 513 */         return (Class)Util.cast(arrayOfClass[i]);
/*     */       }
/*     */     }
/* 516 */     return null;
/*     */   }
/*     */ 
/*     */   public static Object elementFromComplex(Object paramObject, String paramString) throws AttributeNotFoundException
/*     */   {
/*     */     try {
/* 522 */       if ((paramObject.getClass().isArray()) && (paramString.equals("length")))
/* 523 */         return Integer.valueOf(Array.getLength(paramObject));
/* 524 */       if ((paramObject instanceof CompositeData)) {
/* 525 */         return ((CompositeData)paramObject).get(paramString);
/*     */       }
/*     */ 
/* 529 */       Class localClass = paramObject.getClass();
/* 530 */       Method localMethod = null;
/* 531 */       if (BeansHelper.isAvailable()) {
/* 532 */         Object localObject1 = BeansHelper.getBeanInfo(localClass);
/* 533 */         Object[] arrayOfObject1 = BeansHelper.getPropertyDescriptors(localObject1);
/* 534 */         for (Object localObject2 : arrayOfObject1) {
/* 535 */           if (BeansHelper.getPropertyName(localObject2).equals(paramString)) {
/* 536 */             localMethod = BeansHelper.getReadMethod(localObject2);
/* 537 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 543 */         localMethod = SimpleIntrospector.getReadMethod(localClass, paramString);
/*     */       }
/* 545 */       if (localMethod != null) {
/* 546 */         ReflectUtil.checkPackageAccess(localMethod.getDeclaringClass());
/* 547 */         return MethodUtil.invoke(localMethod, paramObject, new Class[0]);
/*     */       }
/*     */ 
/* 550 */       throw new AttributeNotFoundException("Could not find the getter method for the property " + paramString + " using the Java Beans introspector");
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException)
/*     */     {
/* 555 */       throw new IllegalArgumentException(localInvocationTargetException);
/*     */     } catch (AttributeNotFoundException localAttributeNotFoundException) {
/* 557 */       throw localAttributeNotFoundException;
/*     */     } catch (Exception localException) {
/* 559 */       throw ((AttributeNotFoundException)EnvHelp.initCause(new AttributeNotFoundException(localException.getMessage()), localException));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class BeansHelper
/*     */   {
/* 683 */     private static final Class<?> introspectorClass = getClass("java.beans.Introspector");
/*     */ 
/* 685 */     private static final Class<?> beanInfoClass = introspectorClass == null ? null : getClass("java.beans.BeanInfo");
/*     */ 
/* 687 */     private static final Class<?> getPropertyDescriptorClass = beanInfoClass == null ? null : getClass("java.beans.PropertyDescriptor");
/*     */ 
/* 690 */     private static final Method getBeanInfo = getMethod(introspectorClass, "getBeanInfo", new Class[] { Class.class });
/*     */ 
/* 692 */     private static final Method getPropertyDescriptors = getMethod(beanInfoClass, "getPropertyDescriptors", new Class[0]);
/*     */ 
/* 694 */     private static final Method getPropertyName = getMethod(getPropertyDescriptorClass, "getName", new Class[0]);
/*     */ 
/* 696 */     private static final Method getReadMethod = getMethod(getPropertyDescriptorClass, "getReadMethod", new Class[0]);
/*     */ 
/*     */     private static Class<?> getClass(String paramString)
/*     */     {
/*     */       try {
/* 701 */         return Class.forName(paramString, true, null); } catch (ClassNotFoundException localClassNotFoundException) {
/*     */       }
/* 703 */       return null;
/*     */     }
/*     */ 
/*     */     private static Method getMethod(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass)
/*     */     {
/* 710 */       if (paramClass != null) {
/*     */         try {
/* 712 */           return paramClass.getMethod(paramString, paramArrayOfClass);
/*     */         } catch (NoSuchMethodException localNoSuchMethodException) {
/* 714 */           throw new AssertionError(localNoSuchMethodException);
/*     */         }
/*     */       }
/* 717 */       return null;
/*     */     }
/*     */ 
/*     */     static boolean isAvailable()
/*     */     {
/* 727 */       return introspectorClass != null;
/*     */     }
/*     */ 
/*     */     static Object getBeanInfo(Class<?> paramClass)
/*     */       throws Exception
/*     */     {
/*     */       try
/*     */       {
/* 735 */         return getBeanInfo.invoke(null, new Object[] { paramClass });
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 737 */         Throwable localThrowable = localInvocationTargetException.getCause();
/* 738 */         if ((localThrowable instanceof Exception))
/* 739 */           throw ((Exception)localThrowable);
/* 740 */         throw new AssertionError(localInvocationTargetException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 742 */         throw new AssertionError(localIllegalAccessException);
/*     */       }
/*     */     }
/*     */ 
/*     */     static Object[] getPropertyDescriptors(Object paramObject)
/*     */     {
/*     */       try
/*     */       {
/* 751 */         return (Object[])getPropertyDescriptors.invoke(paramObject, new Object[0]);
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 753 */         Throwable localThrowable = localInvocationTargetException.getCause();
/* 754 */         if ((localThrowable instanceof RuntimeException))
/* 755 */           throw ((RuntimeException)localThrowable);
/* 756 */         throw new AssertionError(localInvocationTargetException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 758 */         throw new AssertionError(localIllegalAccessException);
/*     */       }
/*     */     }
/*     */ 
/*     */     static String getPropertyName(Object paramObject)
/*     */     {
/*     */       try
/*     */       {
/* 767 */         return (String)getPropertyName.invoke(paramObject, new Object[0]);
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 769 */         Throwable localThrowable = localInvocationTargetException.getCause();
/* 770 */         if ((localThrowable instanceof RuntimeException))
/* 771 */           throw ((RuntimeException)localThrowable);
/* 772 */         throw new AssertionError(localInvocationTargetException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 774 */         throw new AssertionError(localIllegalAccessException);
/*     */       }
/*     */     }
/*     */ 
/*     */     static Method getReadMethod(Object paramObject)
/*     */     {
/*     */       try
/*     */       {
/* 783 */         return (Method)getReadMethod.invoke(paramObject, new Object[0]);
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 785 */         Throwable localThrowable = localInvocationTargetException.getCause();
/* 786 */         if ((localThrowable instanceof RuntimeException))
/* 787 */           throw ((RuntimeException)localThrowable);
/* 788 */         throw new AssertionError(localInvocationTargetException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 790 */         throw new AssertionError(localIllegalAccessException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SimpleIntrospector
/*     */   {
/*     */     private static final String GET_METHOD_PREFIX = "get";
/*     */     private static final String IS_METHOD_PREFIX = "is";
/* 577 */     private static final Map<Class<?>, SoftReference<List<Method>>> cache = Collections.synchronizedMap(new WeakHashMap());
/*     */ 
/*     */     private static List<Method> getCachedMethods(Class<?> paramClass)
/*     */     {
/* 587 */       SoftReference localSoftReference = (SoftReference)cache.get(paramClass);
/* 588 */       if (localSoftReference != null) {
/* 589 */         List localList = (List)localSoftReference.get();
/* 590 */         if (localList != null)
/* 591 */           return localList;
/*     */       }
/* 593 */       return null;
/*     */     }
/*     */ 
/*     */     static boolean isReadMethod(Method paramMethod)
/*     */     {
/* 603 */       int i = paramMethod.getModifiers();
/* 604 */       if (Modifier.isStatic(i)) {
/* 605 */         return false;
/*     */       }
/* 607 */       String str = paramMethod.getName();
/* 608 */       Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 609 */       int j = arrayOfClass.length;
/*     */ 
/* 611 */       if ((j == 0) && (str.length() > 2))
/*     */       {
/* 613 */         if (str.startsWith("is")) {
/* 614 */           return paramMethod.getReturnType() == Boolean.TYPE;
/*     */         }
/* 616 */         if ((str.length() > 3) && (str.startsWith("get")))
/* 617 */           return paramMethod.getReturnType() != Void.TYPE;
/*     */       }
/* 619 */       return false;
/*     */     }
/*     */ 
/*     */     static List<Method> getReadMethods(Class<?> paramClass)
/*     */     {
/* 629 */       List localList1 = getCachedMethods(paramClass);
/* 630 */       if (localList1 != null) {
/* 631 */         return localList1;
/*     */       }
/*     */ 
/* 635 */       List localList2 = StandardMBeanIntrospector.getInstance().getMethods(paramClass);
/*     */ 
/* 637 */       localList2 = MBeanAnalyzer.eliminateCovariantMethods(localList2);
/*     */ 
/* 640 */       LinkedList localLinkedList = new LinkedList();
/* 641 */       for (Method localMethod : localList2) {
/* 642 */         if (isReadMethod(localMethod))
/*     */         {
/* 644 */           if (localMethod.getName().startsWith("is"))
/* 645 */             localLinkedList.add(0, localMethod);
/*     */           else {
/* 647 */             localLinkedList.add(localMethod);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 653 */       cache.put(paramClass, new SoftReference(localLinkedList));
/*     */ 
/* 655 */       return localLinkedList;
/*     */     }
/*     */ 
/*     */     static Method getReadMethod(Class<?> paramClass, String paramString)
/*     */     {
/* 664 */       paramString = paramString.substring(0, 1).toUpperCase(Locale.ENGLISH) + paramString.substring(1);
/*     */ 
/* 666 */       String str1 = "get" + paramString;
/* 667 */       String str2 = "is" + paramString;
/* 668 */       for (Method localMethod : getReadMethods(paramClass)) {
/* 669 */         String str3 = localMethod.getName();
/* 670 */         if ((str3.equals(str2)) || (str3.equals(str1))) {
/* 671 */           return localMethod;
/*     */         }
/*     */       }
/* 674 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.Introspector
 * JD-Core Version:    0.6.2
 */