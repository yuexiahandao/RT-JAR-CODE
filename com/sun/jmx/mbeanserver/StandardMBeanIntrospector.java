/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.ImmutableDescriptor;
/*     */ import javax.management.IntrospectionException;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.NotificationBroadcaster;
/*     */ import javax.management.NotificationBroadcasterSupport;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ 
/*     */ class StandardMBeanIntrospector extends MBeanIntrospector<Method>
/*     */ {
/*  47 */   private static final StandardMBeanIntrospector instance = new StandardMBeanIntrospector();
/*     */ 
/* 186 */   private static final WeakHashMap<Class<?>, Boolean> definitelyImmutable = new WeakHashMap();
/*     */ 
/* 190 */   private static final MBeanIntrospector.PerInterfaceMap<Method> perInterfaceMap = new MBeanIntrospector.PerInterfaceMap();
/*     */ 
/* 192 */   private static final MBeanIntrospector.MBeanInfoMap mbeanInfoMap = new MBeanIntrospector.MBeanInfoMap();
/*     */ 
/*     */   static StandardMBeanIntrospector getInstance()
/*     */   {
/*  51 */     return instance;
/*     */   }
/*     */ 
/*     */   MBeanIntrospector.PerInterfaceMap<Method> getPerInterfaceMap()
/*     */   {
/*  56 */     return perInterfaceMap;
/*     */   }
/*     */ 
/*     */   MBeanIntrospector.MBeanInfoMap getMBeanInfoMap()
/*     */   {
/*  61 */     return mbeanInfoMap;
/*     */   }
/*     */ 
/*     */   MBeanAnalyzer<Method> getAnalyzer(Class<?> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/*  67 */     return MBeanAnalyzer.analyzer(paramClass, this);
/*     */   }
/*     */ 
/*     */   boolean isMXBean()
/*     */   {
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   Method mFrom(Method paramMethod)
/*     */   {
/*  77 */     return paramMethod;
/*     */   }
/*     */ 
/*     */   String getName(Method paramMethod)
/*     */   {
/*  82 */     return paramMethod.getName();
/*     */   }
/*     */ 
/*     */   Type getGenericReturnType(Method paramMethod)
/*     */   {
/*  87 */     return paramMethod.getGenericReturnType();
/*     */   }
/*     */ 
/*     */   Type[] getGenericParameterTypes(Method paramMethod)
/*     */   {
/*  92 */     return paramMethod.getGenericParameterTypes();
/*     */   }
/*     */ 
/*     */   String[] getSignature(Method paramMethod)
/*     */   {
/*  97 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/*  98 */     String[] arrayOfString = new String[arrayOfClass.length];
/*  99 */     for (int i = 0; i < arrayOfClass.length; i++)
/* 100 */       arrayOfString[i] = arrayOfClass[i].getName();
/* 101 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   void checkMethod(Method paramMethod)
/*     */   {
/*     */   }
/*     */ 
/*     */   Object invokeM2(Method paramMethod, Object paramObject1, Object[] paramArrayOfObject, Object paramObject2)
/*     */     throws InvocationTargetException, IllegalAccessException, MBeanException
/*     */   {
/* 112 */     return MethodUtil.invoke(paramMethod, paramObject1, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   boolean validParameter(Method paramMethod, Object paramObject1, int paramInt, Object paramObject2)
/*     */   {
/* 117 */     return isValidParameter(paramMethod, paramObject1, paramInt);
/*     */   }
/*     */ 
/*     */   MBeanAttributeInfo getMBeanAttributeInfo(String paramString, Method paramMethod1, Method paramMethod2)
/*     */   {
/*     */     try
/*     */     {
/* 126 */       return new MBeanAttributeInfo(paramString, "Attribute exposed for management", paramMethod1, paramMethod2);
/*     */     }
/*     */     catch (IntrospectionException localIntrospectionException) {
/* 129 */       throw new RuntimeException(localIntrospectionException);
/*     */     }
/*     */   }
/*     */ 
/*     */   MBeanOperationInfo getMBeanOperationInfo(String paramString, Method paramMethod)
/*     */   {
/* 137 */     return new MBeanOperationInfo("Operation exposed for management", paramMethod);
/*     */   }
/*     */ 
/*     */   Descriptor getBasicMBeanDescriptor()
/*     */   {
/* 145 */     return ImmutableDescriptor.EMPTY_DESCRIPTOR;
/*     */   }
/*     */ 
/*     */   Descriptor getMBeanDescriptor(Class<?> paramClass)
/*     */   {
/* 150 */     boolean bool = isDefinitelyImmutableInfo(paramClass);
/* 151 */     return new ImmutableDescriptor(new String[] { "mxbean=false", "immutableInfo=" + bool });
/*     */   }
/*     */ 
/*     */   static boolean isDefinitelyImmutableInfo(Class<?> paramClass)
/*     */   {
/* 164 */     if (!NotificationBroadcaster.class.isAssignableFrom(paramClass))
/* 165 */       return true;
/* 166 */     synchronized (definitelyImmutable) {
/* 167 */       Boolean localBoolean = (Boolean)definitelyImmutable.get(paramClass);
/* 168 */       if (localBoolean == null) {
/* 169 */         NotificationBroadcasterSupport localNotificationBroadcasterSupport = NotificationBroadcasterSupport.class;
/*     */ 
/* 171 */         if (localNotificationBroadcasterSupport.isAssignableFrom(paramClass))
/*     */           try {
/* 173 */             Method localMethod = paramClass.getMethod("getNotificationInfo", new Class[0]);
/* 174 */             localBoolean = Boolean.valueOf(localMethod.getDeclaringClass() == localNotificationBroadcasterSupport);
/*     */           }
/*     */           catch (Exception localException) {
/* 177 */             return false;
/*     */           }
/*     */         else
/* 180 */           localBoolean = Boolean.valueOf(false);
/* 181 */         definitelyImmutable.put(paramClass, localBoolean);
/*     */       }
/* 183 */       return localBoolean.booleanValue();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.StandardMBeanIntrospector
 * JD-Core Version:    0.6.2
 */