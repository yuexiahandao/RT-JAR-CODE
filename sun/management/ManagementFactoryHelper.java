/*     */ package sun.management;
/*     */ 
/*     */ import com.sun.management.HotSpotDiagnosticMXBean;
/*     */ import com.sun.management.OSMBeanFactory;
/*     */ import java.lang.management.BufferPoolMXBean;
/*     */ import java.lang.management.ClassLoadingMXBean;
/*     */ import java.lang.management.CompilationMXBean;
/*     */ import java.lang.management.GarbageCollectorMXBean;
/*     */ import java.lang.management.MemoryMXBean;
/*     */ import java.lang.management.MemoryManagerMXBean;
/*     */ import java.lang.management.MemoryPoolMXBean;
/*     */ import java.lang.management.OperatingSystemMXBean;
/*     */ import java.lang.management.PlatformLoggingMXBean;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.LoggingMXBean;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import sun.misc.JavaNioAccess;
/*     */ import sun.misc.JavaNioAccess.BufferPool;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.misc.VM;
/*     */ import sun.nio.ch.FileChannelImpl;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ import sun.util.logging.LoggingSupport;
/*     */ 
/*     */ public class ManagementFactoryHelper
/*     */ {
/* 426 */   private static VMManagement jvm = new VMManagementImpl();
/*     */ 
/*  61 */   private static ClassLoadingImpl classMBean = null;
/*  62 */   private static MemoryImpl memoryMBean = null;
/*  63 */   private static ThreadImpl threadMBean = null;
/*  64 */   private static RuntimeImpl runtimeMBean = null;
/*  65 */   private static CompilationImpl compileMBean = null;
/*  66 */   private static OperatingSystemImpl osMBean = null;
/*     */ 
/* 205 */   private static List<BufferPoolMXBean> bufferPools = null;
/*     */   private static final String BUFFER_POOL_MXBEAN_NAME = "java.nio:type=BufferPool";
/* 261 */   private static HotSpotDiagnostic hsDiagMBean = null;
/* 262 */   private static HotspotRuntime hsRuntimeMBean = null;
/* 263 */   private static HotspotClassLoading hsClassMBean = null;
/* 264 */   private static HotspotThread hsThreadMBean = null;
/* 265 */   private static HotspotCompilation hsCompileMBean = null;
/* 266 */   private static HotspotMemory hsMemoryMBean = null;
/*     */   private static final String HOTSPOT_CLASS_LOADING_MBEAN_NAME = "sun.management:type=HotspotClassLoading";
/*     */   private static final String HOTSPOT_COMPILATION_MBEAN_NAME = "sun.management:type=HotspotCompilation";
/*     */   private static final String HOTSPOT_MEMORY_MBEAN_NAME = "sun.management:type=HotspotMemory";
/*     */   private static final String HOTSPOT_RUNTIME_MBEAN_NAME = "sun.management:type=HotspotRuntime";
/*     */   private static final String HOTSPOT_THREAD_MBEAN_NAME = "sun.management:type=HotspotThreading";
/*     */   private static final int JMM_THREAD_STATE_FLAG_MASK = -1048576;
/*     */   private static final int JMM_THREAD_STATE_FLAG_SUSPENDED = 1048576;
/*     */   private static final int JMM_THREAD_STATE_FLAG_NATIVE = 4194304;
/*     */ 
/*     */   public static synchronized ClassLoadingMXBean getClassLoadingMXBean()
/*     */   {
/*  69 */     if (classMBean == null) {
/*  70 */       classMBean = new ClassLoadingImpl(jvm);
/*     */     }
/*  72 */     return classMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized MemoryMXBean getMemoryMXBean() {
/*  76 */     if (memoryMBean == null) {
/*  77 */       memoryMBean = new MemoryImpl(jvm);
/*     */     }
/*  79 */     return memoryMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized ThreadMXBean getThreadMXBean() {
/*  83 */     if (threadMBean == null) {
/*  84 */       threadMBean = new ThreadImpl(jvm);
/*     */     }
/*  86 */     return threadMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized RuntimeMXBean getRuntimeMXBean() {
/*  90 */     if (runtimeMBean == null) {
/*  91 */       runtimeMBean = new RuntimeImpl(jvm);
/*     */     }
/*  93 */     return runtimeMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized CompilationMXBean getCompilationMXBean() {
/*  97 */     if ((compileMBean == null) && (jvm.getCompilerName() != null)) {
/*  98 */       compileMBean = new CompilationImpl(jvm);
/*     */     }
/* 100 */     return compileMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized OperatingSystemMXBean getOperatingSystemMXBean() {
/* 104 */     if (osMBean == null) {
/* 105 */       osMBean = (OperatingSystemImpl)OSMBeanFactory.getOperatingSystemMXBean(jvm);
/*     */     }
/*     */ 
/* 108 */     return osMBean;
/*     */   }
/*     */ 
/*     */   public static List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
/* 112 */     MemoryPoolMXBean[] arrayOfMemoryPoolMXBean1 = MemoryImpl.getMemoryPools();
/* 113 */     ArrayList localArrayList = new ArrayList(arrayOfMemoryPoolMXBean1.length);
/* 114 */     for (MemoryPoolMXBean localMemoryPoolMXBean : arrayOfMemoryPoolMXBean1) {
/* 115 */       localArrayList.add(localMemoryPoolMXBean);
/*     */     }
/* 117 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
/* 121 */     MemoryManagerMXBean[] arrayOfMemoryManagerMXBean1 = MemoryImpl.getMemoryManagers();
/* 122 */     ArrayList localArrayList = new ArrayList(arrayOfMemoryManagerMXBean1.length);
/* 123 */     for (MemoryManagerMXBean localMemoryManagerMXBean : arrayOfMemoryManagerMXBean1) {
/* 124 */       localArrayList.add(localMemoryManagerMXBean);
/*     */     }
/* 126 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
/* 130 */     MemoryManagerMXBean[] arrayOfMemoryManagerMXBean1 = MemoryImpl.getMemoryManagers();
/* 131 */     ArrayList localArrayList = new ArrayList(arrayOfMemoryManagerMXBean1.length);
/* 132 */     for (MemoryManagerMXBean localMemoryManagerMXBean : arrayOfMemoryManagerMXBean1) {
/* 133 */       if (GarbageCollectorMXBean.class.isInstance(localMemoryManagerMXBean)) {
/* 134 */         localArrayList.add(GarbageCollectorMXBean.class.cast(localMemoryManagerMXBean));
/*     */       }
/*     */     }
/* 137 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static PlatformLoggingMXBean getPlatformLoggingMXBean() {
/* 141 */     if (LoggingSupport.isAvailable()) {
/* 142 */       return PlatformLoggingImpl.instance;
/*     */     }
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */   public static synchronized List<BufferPoolMXBean> getBufferPoolMXBeans()
/*     */   {
/* 207 */     if (bufferPools == null) {
/* 208 */       bufferPools = new ArrayList(2);
/* 209 */       bufferPools.add(createBufferPoolMXBean(SharedSecrets.getJavaNioAccess().getDirectBufferPool()));
/*     */ 
/* 211 */       bufferPools.add(createBufferPoolMXBean(FileChannelImpl.getMappedBufferPool()));
/*     */     }
/*     */ 
/* 214 */     return bufferPools;
/*     */   }
/*     */ 
/*     */   private static BufferPoolMXBean createBufferPoolMXBean(JavaNioAccess.BufferPool paramBufferPool)
/*     */   {
/* 225 */     return new BufferPoolMXBean() {
/*     */       private volatile ObjectName objname;
/*     */ 
/*     */       public ObjectName getObjectName() {
/* 229 */         ObjectName localObjectName = this.objname;
/* 230 */         if (localObjectName == null) {
/* 231 */           synchronized (this) {
/* 232 */             localObjectName = this.objname;
/* 233 */             if (localObjectName == null) {
/* 234 */               localObjectName = Util.newObjectName("java.nio:type=BufferPool,name=" + this.val$pool.getName());
/*     */ 
/* 236 */               this.objname = localObjectName;
/*     */             }
/*     */           }
/*     */         }
/* 240 */         return localObjectName;
/*     */       }
/*     */ 
/*     */       public String getName() {
/* 244 */         return this.val$pool.getName();
/*     */       }
/*     */ 
/*     */       public long getCount() {
/* 248 */         return this.val$pool.getCount();
/*     */       }
/*     */ 
/*     */       public long getTotalCapacity() {
/* 252 */         return this.val$pool.getTotalCapacity();
/*     */       }
/*     */ 
/*     */       public long getMemoryUsed() {
/* 256 */         return this.val$pool.getMemoryUsed();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static synchronized HotSpotDiagnosticMXBean getDiagnosticMXBean()
/*     */   {
/* 269 */     if (hsDiagMBean == null) {
/* 270 */       hsDiagMBean = new HotSpotDiagnostic();
/*     */     }
/* 272 */     return hsDiagMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized HotspotRuntimeMBean getHotspotRuntimeMBean()
/*     */   {
/* 279 */     if (hsRuntimeMBean == null) {
/* 280 */       hsRuntimeMBean = new HotspotRuntime(jvm);
/*     */     }
/* 282 */     return hsRuntimeMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized HotspotClassLoadingMBean getHotspotClassLoadingMBean()
/*     */   {
/* 289 */     if (hsClassMBean == null) {
/* 290 */       hsClassMBean = new HotspotClassLoading(jvm);
/*     */     }
/* 292 */     return hsClassMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized HotspotThreadMBean getHotspotThreadMBean()
/*     */   {
/* 299 */     if (hsThreadMBean == null) {
/* 300 */       hsThreadMBean = new HotspotThread(jvm);
/*     */     }
/* 302 */     return hsThreadMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized HotspotMemoryMBean getHotspotMemoryMBean()
/*     */   {
/* 309 */     if (hsMemoryMBean == null) {
/* 310 */       hsMemoryMBean = new HotspotMemory(jvm);
/*     */     }
/* 312 */     return hsMemoryMBean;
/*     */   }
/*     */ 
/*     */   public static synchronized HotspotCompilationMBean getHotspotCompilationMBean()
/*     */   {
/* 319 */     if (hsCompileMBean == null) {
/* 320 */       hsCompileMBean = new HotspotCompilation(jvm);
/*     */     }
/* 322 */     return hsCompileMBean;
/*     */   }
/*     */ 
/*     */   private static void addMBean(MBeanServer paramMBeanServer, Object paramObject, String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 331 */       final ObjectName localObjectName = Util.newObjectName(paramString);
/*     */ 
/* 334 */       MBeanServer localMBeanServer = paramMBeanServer;
/* 335 */       final Object localObject = paramObject;
/* 336 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws MBeanRegistrationException, NotCompliantMBeanException {
/*     */           try {
/* 340 */             this.val$mbs0.registerMBean(localObject, localObjectName);
/* 341 */             return null;
/*     */           }
/*     */           catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException)
/*     */           {
/*     */           }
/* 346 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 350 */       throw Util.newException(localPrivilegedActionException.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   static void registerInternalMBeans(MBeanServer paramMBeanServer)
/*     */   {
/* 373 */     addMBean(paramMBeanServer, getHotspotClassLoadingMBean(), "sun.management:type=HotspotClassLoading");
/*     */ 
/* 375 */     addMBean(paramMBeanServer, getHotspotMemoryMBean(), "sun.management:type=HotspotMemory");
/*     */ 
/* 377 */     addMBean(paramMBeanServer, getHotspotRuntimeMBean(), "sun.management:type=HotspotRuntime");
/*     */ 
/* 379 */     addMBean(paramMBeanServer, getHotspotThreadMBean(), "sun.management:type=HotspotThreading");
/*     */ 
/* 383 */     if (getCompilationMXBean() != null)
/* 384 */       addMBean(paramMBeanServer, getHotspotCompilationMBean(), "sun.management:type=HotspotCompilation");
/*     */   }
/*     */ 
/*     */   private static void unregisterMBean(MBeanServer paramMBeanServer, String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 391 */       final ObjectName localObjectName = Util.newObjectName(paramString);
/*     */ 
/* 394 */       MBeanServer localMBeanServer = paramMBeanServer;
/* 395 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws MBeanRegistrationException, RuntimeOperationsException {
/*     */           try {
/* 399 */             this.val$mbs0.unregisterMBean(localObjectName);
/*     */           }
/*     */           catch (InstanceNotFoundException localInstanceNotFoundException) {
/*     */           }
/* 403 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 407 */       throw Util.newException(localPrivilegedActionException.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   static void unregisterInternalMBeans(MBeanServer paramMBeanServer)
/*     */   {
/* 413 */     unregisterMBean(paramMBeanServer, "sun.management:type=HotspotClassLoading");
/* 414 */     unregisterMBean(paramMBeanServer, "sun.management:type=HotspotMemory");
/* 415 */     unregisterMBean(paramMBeanServer, "sun.management:type=HotspotRuntime");
/* 416 */     unregisterMBean(paramMBeanServer, "sun.management:type=HotspotThreading");
/*     */ 
/* 419 */     if (getCompilationMXBean() != null)
/* 420 */       unregisterMBean(paramMBeanServer, "sun.management:type=HotspotCompilation");
/*     */   }
/*     */ 
/*     */   public static boolean isThreadSuspended(int paramInt)
/*     */   {
/* 430 */     return (paramInt & 0x100000) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean isThreadRunningNative(int paramInt) {
/* 434 */     return (paramInt & 0x400000) != 0;
/*     */   }
/*     */ 
/*     */   public static Thread.State toThreadState(int paramInt)
/*     */   {
/* 439 */     int i = paramInt & 0xFFFFF;
/* 440 */     return VM.toThreadState(i);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 425 */     AccessController.doPrivileged(new LoadLibraryAction("management"));
/*     */   }
/*     */ 
/*     */   static abstract interface LoggingMXBean extends PlatformLoggingMXBean, LoggingMXBean
/*     */   {
/*     */   }
/*     */ 
/*     */   static class PlatformLoggingImpl
/*     */     implements ManagementFactoryHelper.LoggingMXBean
/*     */   {
/* 165 */     static final PlatformLoggingMXBean instance = new PlatformLoggingImpl();
/*     */     static final String LOGGING_MXBEAN_NAME = "java.util.logging:type=Logging";
/*     */     private volatile ObjectName objname;
/*     */ 
/*     */     public ObjectName getObjectName()
/*     */     {
/* 171 */       ObjectName localObjectName = this.objname;
/* 172 */       if (localObjectName == null) {
/* 173 */         synchronized (this) {
/* 174 */           localObjectName = this.objname;
/* 175 */           if (localObjectName == null) {
/* 176 */             localObjectName = Util.newObjectName("java.util.logging:type=Logging");
/* 177 */             this.objname = localObjectName;
/*     */           }
/*     */         }
/*     */       }
/* 181 */       return localObjectName;
/*     */     }
/*     */ 
/*     */     public List<String> getLoggerNames()
/*     */     {
/* 186 */       return LoggingSupport.getLoggerNames();
/*     */     }
/*     */ 
/*     */     public String getLoggerLevel(String paramString)
/*     */     {
/* 191 */       return LoggingSupport.getLoggerLevel(paramString);
/*     */     }
/*     */ 
/*     */     public void setLoggerLevel(String paramString1, String paramString2)
/*     */     {
/* 196 */       LoggingSupport.setLoggerLevel(paramString1, paramString2);
/*     */     }
/*     */ 
/*     */     public String getParentLoggerName(String paramString)
/*     */     {
/* 201 */       return LoggingSupport.getParentLoggerName(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.ManagementFactoryHelper
 * JD-Core Version:    0.6.2
 */