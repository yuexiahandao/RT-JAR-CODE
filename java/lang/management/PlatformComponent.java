/*     */ package java.lang.management;
/*     */ 
/*     */ import com.sun.management.HotSpotDiagnosticMXBean;
/*     */ import com.sun.management.UnixOperatingSystemMXBean;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.ObjectName;
/*     */ import sun.management.ManagementFactoryHelper;
/*     */ import sun.management.Util;
/*     */ 
/*     */  enum PlatformComponent
/*     */ {
/*  66 */   CLASS_LOADING("java.lang.management.ClassLoadingMXBean", "java.lang", "ClassLoading", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<ClassLoadingMXBean> getMXBeans()
/*     */     {
/*  72 */       return Collections.singletonList(ManagementFactoryHelper.getClassLoadingMXBean());
/*     */     }
/*     */   }
/*  66 */   , new PlatformComponent[0]), 
/*     */ 
/*  79 */   COMPILATION("java.lang.management.CompilationMXBean", "java.lang", "Compilation", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<CompilationMXBean> getMXBeans()
/*     */     {
/*  85 */       CompilationMXBean localCompilationMXBean = ManagementFactoryHelper.getCompilationMXBean();
/*  86 */       if (localCompilationMXBean == null) {
/*  87 */         return Collections.emptyList();
/*     */       }
/*  89 */       return Collections.singletonList(localCompilationMXBean);
/*     */     }
/*     */   }
/*  79 */   , new PlatformComponent[0]), 
/*     */ 
/*  97 */   MEMORY("java.lang.management.MemoryMXBean", "java.lang", "Memory", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<MemoryMXBean> getMXBeans()
/*     */     {
/* 103 */       return Collections.singletonList(ManagementFactoryHelper.getMemoryMXBean());
/*     */     }
/*     */   }
/*  97 */   , new PlatformComponent[0]), 
/*     */ 
/* 110 */   GARBAGE_COLLECTOR("java.lang.management.GarbageCollectorMXBean", "java.lang", "GarbageCollector", keyProperties(new String[] { "name" }), false, new MXBeanFetcher()
/*     */   {
/*     */     public List<GarbageCollectorMXBean> getMXBeans()
/*     */     {
/* 116 */       return ManagementFactoryHelper.getGarbageCollectorMXBeans();
/*     */     }
/*     */   }
/* 110 */   , new PlatformComponent[0]), 
/*     */ 
/* 124 */   MEMORY_MANAGER("java.lang.management.MemoryManagerMXBean", "java.lang", "MemoryManager", keyProperties(new String[] { "name" }), false, new MXBeanFetcher()
/*     */   {
/*     */     public List<MemoryManagerMXBean> getMXBeans()
/*     */     {
/* 130 */       return ManagementFactoryHelper.getMemoryManagerMXBeans();
/*     */     }
/*     */   }
/* 124 */   , new PlatformComponent[] { GARBAGE_COLLECTOR }), 
/*     */ 
/* 138 */   MEMORY_POOL("java.lang.management.MemoryPoolMXBean", "java.lang", "MemoryPool", keyProperties(new String[] { "name" }), false, new MXBeanFetcher()
/*     */   {
/*     */     public List<MemoryPoolMXBean> getMXBeans()
/*     */     {
/* 144 */       return ManagementFactoryHelper.getMemoryPoolMXBeans();
/*     */     }
/*     */   }
/* 138 */   , new PlatformComponent[0]), 
/*     */ 
/* 151 */   OPERATING_SYSTEM("java.lang.management.OperatingSystemMXBean", "java.lang", "OperatingSystem", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<OperatingSystemMXBean> getMXBeans()
/*     */     {
/* 157 */       return Collections.singletonList(ManagementFactoryHelper.getOperatingSystemMXBean());
/*     */     }
/*     */   }
/* 151 */   , new PlatformComponent[0]), 
/*     */ 
/* 164 */   RUNTIME("java.lang.management.RuntimeMXBean", "java.lang", "Runtime", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<RuntimeMXBean> getMXBeans()
/*     */     {
/* 170 */       return Collections.singletonList(ManagementFactoryHelper.getRuntimeMXBean());
/*     */     }
/*     */   }
/* 164 */   , new PlatformComponent[0]), 
/*     */ 
/* 177 */   THREADING("java.lang.management.ThreadMXBean", "java.lang", "Threading", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<ThreadMXBean> getMXBeans()
/*     */     {
/* 183 */       return Collections.singletonList(ManagementFactoryHelper.getThreadMXBean());
/*     */     }
/*     */   }
/* 177 */   , new PlatformComponent[0]), 
/*     */ 
/* 191 */   LOGGING("java.lang.management.PlatformLoggingMXBean", "java.util.logging", "Logging", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<PlatformLoggingMXBean> getMXBeans()
/*     */     {
/* 197 */       PlatformLoggingMXBean localPlatformLoggingMXBean = ManagementFactoryHelper.getPlatformLoggingMXBean();
/* 198 */       if (localPlatformLoggingMXBean == null) {
/* 199 */         return Collections.emptyList();
/*     */       }
/* 201 */       return Collections.singletonList(localPlatformLoggingMXBean);
/*     */     }
/*     */   }
/* 191 */   , new PlatformComponent[0]), 
/*     */ 
/* 209 */   BUFFER_POOL("java.lang.management.BufferPoolMXBean", "java.nio", "BufferPool", keyProperties(new String[] { "name" }), false, new MXBeanFetcher()
/*     */   {
/*     */     public List<BufferPoolMXBean> getMXBeans()
/*     */     {
/* 215 */       return ManagementFactoryHelper.getBufferPoolMXBeans();
/*     */     }
/*     */   }
/* 209 */   , new PlatformComponent[0]), 
/*     */ 
/* 225 */   SUN_GARBAGE_COLLECTOR("com.sun.management.GarbageCollectorMXBean", "java.lang", "GarbageCollector", keyProperties(new String[] { "name" }), false, new MXBeanFetcher()
/*     */   {
/*     */     public List<com.sun.management.GarbageCollectorMXBean> getMXBeans()
/*     */     {
/* 231 */       return PlatformComponent.getGcMXBeanList(com.sun.management.GarbageCollectorMXBean.class);
/*     */     }
/*     */   }
/* 225 */   , new PlatformComponent[0]), 
/*     */ 
/* 239 */   SUN_OPERATING_SYSTEM("com.sun.management.OperatingSystemMXBean", "java.lang", "OperatingSystem", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<com.sun.management.OperatingSystemMXBean> getMXBeans()
/*     */     {
/* 245 */       return PlatformComponent.getOSMXBeanList(com.sun.management.OperatingSystemMXBean.class);
/*     */     }
/*     */   }
/* 239 */   , new PlatformComponent[0]), 
/*     */ 
/* 252 */   SUN_UNIX_OPERATING_SYSTEM("com.sun.management.UnixOperatingSystemMXBean", "java.lang", "OperatingSystem", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<UnixOperatingSystemMXBean> getMXBeans()
/*     */     {
/* 258 */       return PlatformComponent.getOSMXBeanList(UnixOperatingSystemMXBean.class);
/*     */     }
/*     */   }
/* 252 */   , new PlatformComponent[0]), 
/*     */ 
/* 265 */   HOTSPOT_DIAGNOSTIC("com.sun.management.HotSpotDiagnosticMXBean", "com.sun.management", "HotSpotDiagnostic", defaultKeyProperties(), true, new MXBeanFetcher()
/*     */   {
/*     */     public List<HotSpotDiagnosticMXBean> getMXBeans()
/*     */     {
/* 271 */       return Collections.singletonList(ManagementFactoryHelper.getDiagnosticMXBean());
/*     */     }
/*     */   }
/* 265 */   , new PlatformComponent[0]);
/*     */ 
/*     */   private final String mxbeanInterfaceName;
/*     */   private final String domain;
/*     */   private final String type;
/*     */   private final Set<String> keyProperties;
/*     */   private final MXBeanFetcher fetcher;
/*     */   private final PlatformComponent[] subComponents;
/*     */   private final boolean singleton;
/*     */   private static Set<String> defaultKeyProps;
/*     */   private static Map<String, PlatformComponent> enumMap;
/*     */   private static final long serialVersionUID = 6992337162326171013L;
/*     */ 
/*     */   private static <T extends GarbageCollectorMXBean> List<T> getGcMXBeanList(Class<T> paramClass) {
/* 288 */     List localList = ManagementFactoryHelper.getGarbageCollectorMXBeans();
/*     */ 
/* 290 */     ArrayList localArrayList = new ArrayList(localList.size());
/* 291 */     for (GarbageCollectorMXBean localGarbageCollectorMXBean : localList) {
/* 292 */       if (paramClass.isInstance(localGarbageCollectorMXBean)) {
/* 293 */         localArrayList.add(paramClass.cast(localGarbageCollectorMXBean));
/*     */       }
/*     */     }
/* 296 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static <T extends OperatingSystemMXBean> List<T> getOSMXBeanList(Class<T> paramClass)
/*     */   {
/* 304 */     OperatingSystemMXBean localOperatingSystemMXBean = ManagementFactoryHelper.getOperatingSystemMXBean();
/*     */ 
/* 306 */     if (paramClass.isInstance(localOperatingSystemMXBean)) {
/* 307 */       return Collections.singletonList(paramClass.cast(localOperatingSystemMXBean));
/*     */     }
/* 309 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   private PlatformComponent(String paramString1, String paramString2, String paramString3, Set<String> paramSet, boolean paramBoolean, MXBeanFetcher paramMXBeanFetcher, PlatformComponent[] paramArrayOfPlatformComponent)
/*     */   {
/* 327 */     this.mxbeanInterfaceName = paramString1;
/* 328 */     this.domain = paramString2;
/* 329 */     this.type = paramString3;
/* 330 */     this.keyProperties = paramSet;
/* 331 */     this.singleton = paramBoolean;
/* 332 */     this.fetcher = paramMXBeanFetcher;
/* 333 */     this.subComponents = paramArrayOfPlatformComponent;
/*     */   }
/*     */ 
/*     */   private static Set<String> defaultKeyProperties()
/*     */   {
/* 338 */     if (defaultKeyProps == null) {
/* 339 */       defaultKeyProps = Collections.singleton("type");
/*     */     }
/* 341 */     return defaultKeyProps;
/*     */   }
/*     */ 
/*     */   private static Set<String> keyProperties(String[] paramArrayOfString) {
/* 345 */     HashSet localHashSet = new HashSet();
/* 346 */     localHashSet.add("type");
/* 347 */     for (String str : paramArrayOfString) {
/* 348 */       localHashSet.add(str);
/*     */     }
/* 350 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   boolean isSingleton() {
/* 354 */     return this.singleton;
/*     */   }
/*     */ 
/*     */   String getMXBeanInterfaceName() {
/* 358 */     return this.mxbeanInterfaceName;
/*     */   }
/*     */ 
/*     */   Class<? extends PlatformManagedObject> getMXBeanInterface()
/*     */   {
/*     */     try
/*     */     {
/* 365 */       return Class.forName(this.mxbeanInterfaceName, false, null);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 368 */       throw new AssertionError(localClassNotFoundException);
/*     */     }
/*     */   }
/*     */ 
/*     */   <T extends PlatformManagedObject> List<T> getMXBeans(Class<T> paramClass)
/*     */   {
/* 376 */     return this.fetcher.getMXBeans();
/*     */   }
/*     */ 
/*     */   <T extends PlatformManagedObject> T getSingletonMXBean(Class<T> paramClass)
/*     */   {
/* 381 */     if (!this.singleton) {
/* 382 */       throw new IllegalArgumentException(this.mxbeanInterfaceName + " can have zero or more than one instances");
/*     */     }
/*     */ 
/* 385 */     List localList = this.fetcher.getMXBeans();
/* 386 */     assert (localList.size() == 1);
/* 387 */     return localList.isEmpty() ? null : (PlatformManagedObject)localList.get(0);
/*     */   }
/*     */ 
/*     */   <T extends PlatformManagedObject> T getSingletonMXBean(MBeanServerConnection paramMBeanServerConnection, Class<T> paramClass)
/*     */     throws IOException
/*     */   {
/* 394 */     if (!this.singleton) {
/* 395 */       throw new IllegalArgumentException(this.mxbeanInterfaceName + " can have zero or more than one instances");
/*     */     }
/*     */ 
/* 399 */     assert (this.keyProperties.size() == 1);
/* 400 */     String str = this.domain + ":type=" + this.type;
/* 401 */     return (PlatformManagedObject)ManagementFactory.newPlatformMXBeanProxy(paramMBeanServerConnection, str, paramClass);
/*     */   }
/*     */ 
/*     */   <T extends PlatformManagedObject> List<T> getMXBeans(MBeanServerConnection paramMBeanServerConnection, Class<T> paramClass)
/*     */     throws IOException
/*     */   {
/* 410 */     ArrayList localArrayList = new ArrayList();
/* 411 */     for (ObjectName localObjectName : getObjectNames(paramMBeanServerConnection)) {
/* 412 */       localArrayList.add(ManagementFactory.newPlatformMXBeanProxy(paramMBeanServerConnection, localObjectName.getCanonicalName(), paramClass));
/*     */     }
/*     */ 
/* 418 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private Set<ObjectName> getObjectNames(MBeanServerConnection paramMBeanServerConnection)
/*     */     throws IOException
/*     */   {
/* 424 */     String str = this.domain + ":type=" + this.type;
/* 425 */     if (this.keyProperties.size() > 1)
/*     */     {
/* 427 */       str = str + ",*";
/*     */     }
/* 429 */     ObjectName localObjectName = Util.newObjectName(str);
/* 430 */     Set localSet = paramMBeanServerConnection.queryNames(localObjectName, null);
/* 431 */     for (PlatformComponent localPlatformComponent : this.subComponents) {
/* 432 */       localSet.addAll(localPlatformComponent.getObjectNames(paramMBeanServerConnection));
/*     */     }
/* 434 */     return localSet;
/*     */   }
/*     */ 
/*     */   private static synchronized void ensureInitialized()
/*     */   {
/* 440 */     if (enumMap == null) {
/* 441 */       enumMap = new HashMap();
/* 442 */       for (PlatformComponent localPlatformComponent : values())
/*     */       {
/* 445 */         enumMap.put(localPlatformComponent.getMXBeanInterfaceName(), localPlatformComponent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean isPlatformMXBean(String paramString) {
/* 451 */     ensureInitialized();
/* 452 */     return enumMap.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   static <T extends PlatformManagedObject> PlatformComponent getPlatformComponent(Class<T> paramClass)
/*     */   {
/* 458 */     ensureInitialized();
/* 459 */     String str = paramClass.getName();
/* 460 */     PlatformComponent localPlatformComponent = (PlatformComponent)enumMap.get(str);
/* 461 */     if ((localPlatformComponent != null) && (localPlatformComponent.getMXBeanInterface() == paramClass))
/* 462 */       return localPlatformComponent;
/* 463 */     return null;
/*     */   }
/*     */ 
/*     */   static abstract interface MXBeanFetcher<T extends PlatformManagedObject>
/*     */   {
/*     */     public abstract List<T> getMXBeans();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.PlatformComponent
 * JD-Core Version:    0.6.2
 */