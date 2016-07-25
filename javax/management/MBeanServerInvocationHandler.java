/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.MXBeanProxy;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ public class MBeanServerInvocationHandler
/*     */   implements InvocationHandler
/*     */ {
/* 339 */   private static final WeakHashMap<Class<?>, WeakReference<MXBeanProxy>> mxbeanProxies = new WeakHashMap();
/*     */   private final MBeanServerConnection connection;
/*     */   private final ObjectName objectName;
/*     */   private final boolean isMXBean;
/*     */ 
/*     */   public MBeanServerInvocationHandler(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName)
/*     */   {
/* 114 */     this(paramMBeanServerConnection, paramObjectName, false);
/*     */   }
/*     */ 
/*     */   public MBeanServerInvocationHandler(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, boolean paramBoolean)
/*     */   {
/* 141 */     if (paramMBeanServerConnection == null) {
/* 142 */       throw new IllegalArgumentException("Null connection");
/*     */     }
/* 144 */     if (paramObjectName == null) {
/* 145 */       throw new IllegalArgumentException("Null object name");
/*     */     }
/* 147 */     this.connection = paramMBeanServerConnection;
/* 148 */     this.objectName = paramObjectName;
/* 149 */     this.isMXBean = paramBoolean;
/*     */   }
/*     */ 
/*     */   public MBeanServerConnection getMBeanServerConnection()
/*     */   {
/* 161 */     return this.connection;
/*     */   }
/*     */ 
/*     */   public ObjectName getObjectName()
/*     */   {
/* 173 */     return this.objectName;
/*     */   }
/*     */ 
/*     */   public boolean isMXBean()
/*     */   {
/* 185 */     return this.isMXBean;
/*     */   }
/*     */ 
/*     */   public static <T> T newProxyInstance(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Class<T> paramClass, boolean paramBoolean)
/*     */   {
/* 234 */     return JMX.newMBeanProxy(paramMBeanServerConnection, paramObjectName, paramClass, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable
/*     */   {
/* 239 */     Class localClass1 = paramMethod.getDeclaringClass();
/*     */ 
/* 241 */     if ((localClass1.equals(NotificationBroadcaster.class)) || (localClass1.equals(NotificationEmitter.class)))
/*     */     {
/* 243 */       return invokeBroadcasterMethod(paramObject, paramMethod, paramArrayOfObject);
/*     */     }
/*     */ 
/* 246 */     if (shouldDoLocally(paramObject, paramMethod))
/* 247 */       return doLocally(paramObject, paramMethod, paramArrayOfObject);
/*     */     try
/*     */     {
/* 250 */       if (isMXBean()) {
/* 251 */         localObject1 = findMXBeanProxy(localClass1);
/* 252 */         return ((MXBeanProxy)localObject1).invoke(this.connection, this.objectName, paramMethod, paramArrayOfObject);
/*     */       }
/* 254 */       Object localObject1 = paramMethod.getName();
/* 255 */       Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 256 */       Class localClass2 = paramMethod.getReturnType();
/*     */ 
/* 261 */       int i = paramArrayOfObject == null ? 0 : paramArrayOfObject.length;
/*     */ 
/* 263 */       if ((((String)localObject1).startsWith("get")) && (((String)localObject1).length() > 3) && (i == 0) && (!localClass2.equals(Void.TYPE)))
/*     */       {
/* 267 */         return this.connection.getAttribute(this.objectName, ((String)localObject1).substring(3));
/*     */       }
/*     */ 
/* 271 */       if ((((String)localObject1).startsWith("is")) && (((String)localObject1).length() > 2) && (i == 0) && ((localClass2.equals(Boolean.TYPE)) || (localClass2.equals(Boolean.class))))
/*     */       {
/* 276 */         return this.connection.getAttribute(this.objectName, ((String)localObject1).substring(2));
/*     */       }
/*     */ 
/* 280 */       if ((((String)localObject1).startsWith("set")) && (((String)localObject1).length() > 3) && (i == 1) && (localClass2.equals(Void.TYPE)))
/*     */       {
/* 284 */         localObject2 = new Attribute(((String)localObject1).substring(3), paramArrayOfObject[0]);
/* 285 */         this.connection.setAttribute(this.objectName, (Attribute)localObject2);
/* 286 */         return null;
/*     */       }
/*     */ 
/* 289 */       Object localObject2 = new String[arrayOfClass.length];
/* 290 */       for (int j = 0; j < arrayOfClass.length; j++)
/* 291 */         localObject2[j] = arrayOfClass[j].getName();
/* 292 */       return this.connection.invoke(this.objectName, (String)localObject1, paramArrayOfObject, (String[])localObject2);
/*     */     }
/*     */     catch (MBeanException localMBeanException)
/*     */     {
/* 296 */       throw localMBeanException.getTargetException();
/*     */     } catch (RuntimeMBeanException localRuntimeMBeanException) {
/* 298 */       throw localRuntimeMBeanException.getTargetException();
/*     */     } catch (RuntimeErrorException localRuntimeErrorException) {
/* 300 */       throw localRuntimeErrorException.getTargetError();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static MXBeanProxy findMXBeanProxy(Class<?> paramClass)
/*     */   {
/* 317 */     synchronized (mxbeanProxies) {
/* 318 */       WeakReference localWeakReference = (WeakReference)mxbeanProxies.get(paramClass);
/*     */ 
/* 320 */       MXBeanProxy localMXBeanProxy = localWeakReference == null ? null : (MXBeanProxy)localWeakReference.get();
/* 321 */       if (localMXBeanProxy == null) {
/*     */         try {
/* 323 */           localMXBeanProxy = new MXBeanProxy(paramClass);
/*     */         } catch (IllegalArgumentException localIllegalArgumentException1) {
/* 325 */           String str = "Cannot make MXBean proxy for " + paramClass.getName() + ": " + localIllegalArgumentException1.getMessage();
/*     */ 
/* 327 */           IllegalArgumentException localIllegalArgumentException2 = new IllegalArgumentException(str, localIllegalArgumentException1.getCause());
/*     */ 
/* 329 */           localIllegalArgumentException2.setStackTrace(localIllegalArgumentException1.getStackTrace());
/* 330 */           throw localIllegalArgumentException2;
/*     */         }
/* 332 */         mxbeanProxies.put(paramClass, new WeakReference(localMXBeanProxy));
/*     */       }
/*     */ 
/* 335 */       return localMXBeanProxy;
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object invokeBroadcasterMethod(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*     */     throws Exception
/*     */   {
/* 343 */     String str1 = paramMethod.getName();
/* 344 */     int i = paramArrayOfObject == null ? 0 : paramArrayOfObject.length;
/*     */     Object localObject1;
/*     */     NotificationFilter localNotificationFilter;
/*     */     Object localObject2;
/* 346 */     if (str1.equals("addNotificationListener"))
/*     */     {
/* 351 */       if (i != 3) {
/* 352 */         localObject1 = "Bad arg count to addNotificationListener: " + i;
/*     */ 
/* 354 */         throw new IllegalArgumentException((String)localObject1);
/*     */       }
/*     */ 
/* 359 */       localObject1 = (NotificationListener)paramArrayOfObject[0];
/* 360 */       localNotificationFilter = (NotificationFilter)paramArrayOfObject[1];
/* 361 */       localObject2 = paramArrayOfObject[2];
/* 362 */       this.connection.addNotificationListener(this.objectName, (NotificationListener)localObject1, localNotificationFilter, localObject2);
/*     */ 
/* 366 */       return null;
/*     */     }
/* 368 */     if (str1.equals("removeNotificationListener"))
/*     */     {
/* 372 */       localObject1 = (NotificationListener)paramArrayOfObject[0];
/*     */ 
/* 374 */       switch (i) {
/*     */       case 1:
/* 376 */         this.connection.removeNotificationListener(this.objectName, (NotificationListener)localObject1);
/* 377 */         return null;
/*     */       case 3:
/* 380 */         localNotificationFilter = (NotificationFilter)paramArrayOfObject[1];
/* 381 */         localObject2 = paramArrayOfObject[2];
/* 382 */         this.connection.removeNotificationListener(this.objectName, (NotificationListener)localObject1, localNotificationFilter, localObject2);
/*     */ 
/* 386 */         return null;
/*     */       }
/*     */ 
/* 389 */       String str2 = "Bad arg count to removeNotificationListener: " + i;
/*     */ 
/* 391 */       throw new IllegalArgumentException(str2);
/*     */     }
/*     */ 
/* 394 */     if (str1.equals("getNotificationInfo"))
/*     */     {
/* 396 */       if (paramArrayOfObject != null) {
/* 397 */         throw new IllegalArgumentException("getNotificationInfo has args");
/*     */       }
/*     */ 
/* 401 */       localObject1 = this.connection.getMBeanInfo(this.objectName);
/* 402 */       return ((MBeanInfo)localObject1).getNotifications();
/*     */     }
/*     */ 
/* 405 */     throw new IllegalArgumentException("Bad method name: " + str1);
/*     */   }
/*     */ 
/*     */   private boolean shouldDoLocally(Object paramObject, Method paramMethod)
/*     */   {
/* 411 */     String str = paramMethod.getName();
/* 412 */     if (((str.equals("hashCode")) || (str.equals("toString"))) && (paramMethod.getParameterTypes().length == 0) && (isLocal(paramObject, paramMethod)))
/*     */     {
/* 415 */       return true;
/* 416 */     }if (str.equals("equals")) if ((Arrays.equals(paramMethod.getParameterTypes(), new Class[] { Object.class })) && (isLocal(paramObject, paramMethod)))
/*     */       {
/* 420 */         return true;
/*     */       } return false;
/*     */   }
/*     */ 
/*     */   private Object doLocally(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) {
/* 425 */     String str = paramMethod.getName();
/*     */ 
/* 427 */     if (str.equals("equals"))
/*     */     {
/* 429 */       if (this == paramArrayOfObject[0]) {
/* 430 */         return Boolean.valueOf(true);
/*     */       }
/*     */ 
/* 433 */       if (!(paramArrayOfObject[0] instanceof Proxy)) {
/* 434 */         return Boolean.valueOf(false);
/*     */       }
/*     */ 
/* 437 */       InvocationHandler localInvocationHandler = Proxy.getInvocationHandler(paramArrayOfObject[0]);
/*     */ 
/* 440 */       if ((localInvocationHandler == null) || (!(localInvocationHandler instanceof MBeanServerInvocationHandler)))
/*     */       {
/* 442 */         return Boolean.valueOf(false);
/*     */       }
/*     */ 
/* 445 */       MBeanServerInvocationHandler localMBeanServerInvocationHandler = (MBeanServerInvocationHandler)localInvocationHandler;
/*     */ 
/* 448 */       return Boolean.valueOf((this.connection.equals(localMBeanServerInvocationHandler.connection)) && (this.objectName.equals(localMBeanServerInvocationHandler.objectName)) && (paramObject.getClass().equals(paramArrayOfObject[0].getClass())));
/*     */     }
/*     */ 
/* 451 */     if (str.equals("toString")) {
/* 452 */       return (isMXBean() ? "MX" : "M") + "BeanProxy(" + this.connection + "[" + this.objectName + "])";
/*     */     }
/* 454 */     if (str.equals("hashCode")) {
/* 455 */       return Integer.valueOf(this.objectName.hashCode() + this.connection.hashCode());
/*     */     }
/*     */ 
/* 458 */     throw new RuntimeException("Unexpected method name: " + str);
/*     */   }
/*     */ 
/*     */   private static boolean isLocal(Object paramObject, Method paramMethod) {
/* 462 */     Class[] arrayOfClass1 = paramObject.getClass().getInterfaces();
/* 463 */     if (arrayOfClass1 == null) {
/* 464 */       return true;
/*     */     }
/*     */ 
/* 467 */     String str = paramMethod.getName();
/* 468 */     Class[] arrayOfClass2 = paramMethod.getParameterTypes();
/* 469 */     for (Class localClass : arrayOfClass1) {
/*     */       try {
/* 471 */         localClass.getMethod(str, arrayOfClass2);
/* 472 */         return false;
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException)
/*     */       {
/*     */       }
/*     */     }
/* 478 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanServerInvocationHandler
 * JD-Core Version:    0.6.2
 */