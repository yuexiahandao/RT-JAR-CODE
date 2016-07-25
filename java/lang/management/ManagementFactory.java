/*     */ package java.lang.management;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.JMX;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.MBeanServerFactory;
/*     */ import javax.management.MBeanServerPermission;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.NotificationEmitter;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.StandardEmitterMBean;
/*     */ import javax.management.StandardMBean;
/*     */ import sun.management.ManagementFactoryHelper;
/*     */ 
/*     */ public class ManagementFactory
/*     */ {
/*     */   public static final String CLASS_LOADING_MXBEAN_NAME = "java.lang:type=ClassLoading";
/*     */   public static final String COMPILATION_MXBEAN_NAME = "java.lang:type=Compilation";
/*     */   public static final String MEMORY_MXBEAN_NAME = "java.lang:type=Memory";
/*     */   public static final String OPERATING_SYSTEM_MXBEAN_NAME = "java.lang:type=OperatingSystem";
/*     */   public static final String RUNTIME_MXBEAN_NAME = "java.lang:type=Runtime";
/*     */   public static final String THREAD_MXBEAN_NAME = "java.lang:type=Threading";
/*     */   public static final String GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE = "java.lang:type=GarbageCollector";
/*     */   public static final String MEMORY_MANAGER_MXBEAN_DOMAIN_TYPE = "java.lang:type=MemoryManager";
/*     */   public static final String MEMORY_POOL_MXBEAN_DOMAIN_TYPE = "java.lang:type=MemoryPool";
/*     */   private static MBeanServer platformMBeanServer;
/*     */   private static final String NOTIF_EMITTER = "javax.management.NotificationEmitter";
/*     */ 
/*     */   public static ClassLoadingMXBean getClassLoadingMXBean()
/*     */   {
/* 316 */     return ManagementFactoryHelper.getClassLoadingMXBean();
/*     */   }
/*     */ 
/*     */   public static MemoryMXBean getMemoryMXBean()
/*     */   {
/* 326 */     return ManagementFactoryHelper.getMemoryMXBean();
/*     */   }
/*     */ 
/*     */   public static ThreadMXBean getThreadMXBean()
/*     */   {
/* 336 */     return ManagementFactoryHelper.getThreadMXBean();
/*     */   }
/*     */ 
/*     */   public static RuntimeMXBean getRuntimeMXBean()
/*     */   {
/* 347 */     return ManagementFactoryHelper.getRuntimeMXBean();
/*     */   }
/*     */ 
/*     */   public static CompilationMXBean getCompilationMXBean()
/*     */   {
/* 360 */     return ManagementFactoryHelper.getCompilationMXBean();
/*     */   }
/*     */ 
/*     */   public static OperatingSystemMXBean getOperatingSystemMXBean()
/*     */   {
/* 371 */     return ManagementFactoryHelper.getOperatingSystemMXBean();
/*     */   }
/*     */ 
/*     */   public static List<MemoryPoolMXBean> getMemoryPoolMXBeans()
/*     */   {
/* 384 */     return ManagementFactoryHelper.getMemoryPoolMXBeans();
/*     */   }
/*     */ 
/*     */   public static List<MemoryManagerMXBean> getMemoryManagerMXBeans()
/*     */   {
/* 397 */     return ManagementFactoryHelper.getMemoryManagerMXBeans();
/*     */   }
/*     */ 
/*     */   public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans()
/*     */   {
/* 413 */     return ManagementFactoryHelper.getGarbageCollectorMXBeans();
/*     */   }
/*     */ 
/*     */   public static synchronized MBeanServer getPlatformMBeanServer()
/*     */   {
/* 459 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*     */     Object localObject1;
/* 460 */     if (localSecurityManager != null) {
/* 461 */       localObject1 = new MBeanServerPermission("createMBeanServer");
/* 462 */       localSecurityManager.checkPermission((Permission)localObject1);
/*     */     }
/*     */ 
/* 465 */     if (platformMBeanServer == null) {
/* 466 */       platformMBeanServer = MBeanServerFactory.createMBeanServer();
/* 467 */       for (Object localObject2 : PlatformComponent.values()) {
/* 468 */         List localList = localObject2.getMXBeans(localObject2.getMXBeanInterface());
/*     */ 
/* 470 */         for (PlatformManagedObject localPlatformManagedObject : localList)
/*     */         {
/* 480 */           if (!platformMBeanServer.isRegistered(localPlatformManagedObject.getObjectName())) {
/* 481 */             addMXBean(platformMBeanServer, localPlatformManagedObject);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 486 */     return platformMBeanServer;
/*     */   }
/*     */ 
/*     */   public static <T> T newPlatformMXBeanProxy(MBeanServerConnection paramMBeanServerConnection, String paramString, Class<T> paramClass)
/*     */     throws IOException
/*     */   {
/* 579 */     Class<T> localClass = paramClass;
/*     */ 
/* 582 */     ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 585 */         return this.val$interfaceClass.getClassLoader();
/*     */       }
/*     */     });
/* 588 */     if (localClassLoader != null) {
/* 589 */       throw new IllegalArgumentException(paramString + " is not a platform MXBean");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 594 */       ObjectName localObjectName = new ObjectName(paramString);
/*     */ 
/* 596 */       String str = localClass.getName();
/* 597 */       if (!paramMBeanServerConnection.isInstanceOf(localObjectName, str)) {
/* 598 */         throw new IllegalArgumentException(paramString + " is not an instance of " + localClass);
/*     */       }
/*     */ 
/* 604 */       boolean bool = paramMBeanServerConnection.isInstanceOf(localObjectName, "javax.management.NotificationEmitter");
/*     */ 
/* 607 */       return JMX.newMXBeanProxy(paramMBeanServerConnection, localObjectName, paramClass, bool);
/*     */     }
/*     */     catch (InstanceNotFoundException|MalformedObjectNameException localInstanceNotFoundException) {
/* 610 */       throw new IllegalArgumentException(localInstanceNotFoundException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static <T extends PlatformManagedObject> T getPlatformMXBean(Class<T> paramClass)
/*     */   {
/* 643 */     PlatformComponent localPlatformComponent = PlatformComponent.getPlatformComponent(paramClass);
/* 644 */     if (localPlatformComponent == null) {
/* 645 */       throw new IllegalArgumentException(paramClass.getName() + " is not a platform management interface");
/*     */     }
/* 647 */     if (!localPlatformComponent.isSingleton()) {
/* 648 */       throw new IllegalArgumentException(paramClass.getName() + " can have zero or more than one instances");
/*     */     }
/*     */ 
/* 651 */     return localPlatformComponent.getSingletonMXBean(paramClass);
/*     */   }
/*     */ 
/*     */   public static <T extends PlatformManagedObject> List<T> getPlatformMXBeans(Class<T> paramClass)
/*     */   {
/* 677 */     PlatformComponent localPlatformComponent = PlatformComponent.getPlatformComponent(paramClass);
/* 678 */     if (localPlatformComponent == null) {
/* 679 */       throw new IllegalArgumentException(paramClass.getName() + " is not a platform management interface");
/*     */     }
/* 681 */     return Collections.unmodifiableList(localPlatformComponent.getMXBeans(paramClass));
/*     */   }
/*     */ 
/*     */   public static <T extends PlatformManagedObject> T getPlatformMXBean(MBeanServerConnection paramMBeanServerConnection, Class<T> paramClass)
/*     */     throws IOException
/*     */   {
/* 723 */     PlatformComponent localPlatformComponent = PlatformComponent.getPlatformComponent(paramClass);
/* 724 */     if (localPlatformComponent == null) {
/* 725 */       throw new IllegalArgumentException(paramClass.getName() + " is not a platform management interface");
/*     */     }
/* 727 */     if (!localPlatformComponent.isSingleton()) {
/* 728 */       throw new IllegalArgumentException(paramClass.getName() + " can have zero or more than one instances");
/*     */     }
/* 730 */     return localPlatformComponent.getSingletonMXBean(paramMBeanServerConnection, paramClass);
/*     */   }
/*     */ 
/*     */   public static <T extends PlatformManagedObject> List<T> getPlatformMXBeans(MBeanServerConnection paramMBeanServerConnection, Class<T> paramClass)
/*     */     throws IOException
/*     */   {
/* 765 */     PlatformComponent localPlatformComponent = PlatformComponent.getPlatformComponent(paramClass);
/* 766 */     if (localPlatformComponent == null) {
/* 767 */       throw new IllegalArgumentException(paramClass.getName() + " is not a platform management interface");
/*     */     }
/*     */ 
/* 770 */     return Collections.unmodifiableList(localPlatformComponent.getMXBeans(paramMBeanServerConnection, paramClass));
/*     */   }
/*     */ 
/*     */   public static Set<Class<? extends PlatformManagedObject>> getPlatformManagementInterfaces()
/*     */   {
/* 789 */     HashSet localHashSet = new HashSet();
/*     */ 
/* 791 */     for (PlatformComponent localPlatformComponent : PlatformComponent.values()) {
/* 792 */       localHashSet.add(localPlatformComponent.getMXBeanInterface());
/*     */     }
/* 794 */     return Collections.unmodifiableSet(localHashSet);
/*     */   }
/*     */ 
/*     */   private static void addMXBean(final MBeanServer paramMBeanServer, PlatformManagedObject paramPlatformManagedObject)
/*     */   {
/*     */     try
/*     */     {
/* 806 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run()
/*     */           throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
/*     */         {
/*     */           Object localObject;
/* 811 */           if ((this.val$pmo instanceof DynamicMBean))
/* 812 */             localObject = (DynamicMBean)DynamicMBean.class.cast(this.val$pmo);
/* 813 */           else if ((this.val$pmo instanceof NotificationEmitter))
/* 814 */             localObject = new StandardEmitterMBean(this.val$pmo, null, true, (NotificationEmitter)this.val$pmo);
/*     */           else {
/* 816 */             localObject = new StandardMBean(this.val$pmo, null, true);
/*     */           }
/*     */ 
/* 819 */           paramMBeanServer.registerMBean(localObject, this.val$pmo.getObjectName());
/* 820 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 824 */       throw new RuntimeException(localPrivilegedActionException.getException());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.ManagementFactory
 * JD-Core Version:    0.6.2
 */