/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Introspector;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class JMX
/*     */ {
/*  43 */   static final JMX proof = new JMX();
/*     */   public static final String DEFAULT_VALUE_FIELD = "defaultValue";
/*     */   public static final String IMMUTABLE_INFO_FIELD = "immutableInfo";
/*     */   public static final String INTERFACE_CLASS_NAME_FIELD = "interfaceClassName";
/*     */   public static final String LEGAL_VALUES_FIELD = "legalValues";
/*     */   public static final String MAX_VALUE_FIELD = "maxValue";
/*     */   public static final String MIN_VALUE_FIELD = "minValue";
/*     */   public static final String MXBEAN_FIELD = "mxbean";
/*     */   public static final String OPEN_TYPE_FIELD = "openType";
/*     */   public static final String ORIGINAL_TYPE_FIELD = "originalType";
/*     */ 
/*     */   public static <T> T newMBeanProxy(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Class<T> paramClass)
/*     */   {
/* 167 */     return newMBeanProxy(paramMBeanServerConnection, paramObjectName, paramClass, false);
/*     */   }
/*     */ 
/*     */   public static <T> T newMBeanProxy(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Class<T> paramClass, boolean paramBoolean)
/*     */   {
/* 208 */     return createProxy(paramMBeanServerConnection, paramObjectName, paramClass, paramBoolean, false);
/*     */   }
/*     */ 
/*     */   public static <T> T newMXBeanProxy(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Class<T> paramClass)
/*     */   {
/* 305 */     return newMXBeanProxy(paramMBeanServerConnection, paramObjectName, paramClass, false);
/*     */   }
/*     */ 
/*     */   public static <T> T newMXBeanProxy(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Class<T> paramClass, boolean paramBoolean)
/*     */   {
/* 346 */     return createProxy(paramMBeanServerConnection, paramObjectName, paramClass, paramBoolean, true);
/*     */   }
/*     */ 
/*     */   public static boolean isMXBeanInterface(Class<?> paramClass)
/*     */   {
/* 364 */     if (!paramClass.isInterface())
/* 365 */       return false;
/* 366 */     MXBean localMXBean = (MXBean)paramClass.getAnnotation(MXBean.class);
/* 367 */     if (localMXBean != null)
/* 368 */       return localMXBean.value();
/* 369 */     return paramClass.getName().endsWith("MXBean");
/*     */   }
/*     */ 
/*     */   private static <T> T createProxy(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 392 */     if (System.getSecurityManager() != null)
/* 393 */       checkProxyInterface(paramClass);
/*     */     try
/*     */     {
/* 396 */       if (paramBoolean2)
/*     */       {
/* 398 */         Introspector.testComplianceMXBeanInterface(paramClass);
/*     */       }
/*     */       else
/* 401 */         Introspector.testComplianceMBeanInterface(paramClass);
/*     */     }
/*     */     catch (NotCompliantMBeanException localNotCompliantMBeanException) {
/* 404 */       throw new IllegalArgumentException(localNotCompliantMBeanException);
/*     */     }
/*     */ 
/* 407 */     MBeanServerInvocationHandler localMBeanServerInvocationHandler = new MBeanServerInvocationHandler(paramMBeanServerConnection, paramObjectName, paramBoolean2);
/*     */     Class[] arrayOfClass;
/* 410 */     if (paramBoolean1) {
/* 411 */       arrayOfClass = new Class[] { paramClass, NotificationEmitter.class };
/*     */     }
/*     */     else {
/* 414 */       arrayOfClass = new Class[] { paramClass };
/*     */     }
/* 416 */     Object localObject = Proxy.newProxyInstance(paramClass.getClassLoader(), arrayOfClass, localMBeanServerInvocationHandler);
/*     */ 
/* 420 */     return paramClass.cast(localObject);
/*     */   }
/*     */ 
/*     */   private static void checkProxyInterface(Class<?> paramClass)
/*     */   {
/* 430 */     if (!Modifier.isPublic(paramClass.getModifiers())) {
/* 431 */       throw new SecurityException("mbean proxy interface non-public");
/*     */     }
/* 433 */     ReflectUtil.checkPackageAccess(paramClass);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.JMX
 * JD-Core Version:    0.6.2
 */