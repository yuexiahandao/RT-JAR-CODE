/*     */ package sun.management;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import sun.management.counter.Counter;
/*     */ import sun.management.counter.perf.PerfInstrumentation;
/*     */ import sun.misc.Perf;
/*     */ import sun.misc.Perf.GetPerfAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class VMManagementImpl
/*     */   implements VMManagement
/*     */ {
/*  63 */   private static String version = getVersion0();
/*     */   private static boolean compTimeMonitoringSupport;
/*     */   private static boolean threadContentionMonitoringSupport;
/*     */   private static boolean currentThreadCpuTimeSupport;
/*     */   private static boolean otherThreadCpuTimeSupport;
/*     */   private static boolean bootClassPathSupport;
/*     */   private static boolean objectMonitorUsageSupport;
/*     */   private static boolean synchronizerUsageSupport;
/*     */   private static boolean threadAllocatedMemorySupport;
/*     */   private static boolean gcNotificationSupport;
/* 177 */   private List<String> vmArgs = null;
/*     */ 
/* 236 */   private PerfInstrumentation perfInstr = null;
/* 237 */   private boolean noPerfData = false;
/*     */ 
/*     */   private static native String getVersion0();
/*     */ 
/*     */   private static native void initOptionalSupportFields();
/*     */ 
/*     */   public boolean isCompilationTimeMonitoringSupported()
/*     */   {
/*  74 */     return compTimeMonitoringSupport;
/*     */   }
/*     */ 
/*     */   public boolean isThreadContentionMonitoringSupported() {
/*  78 */     return threadContentionMonitoringSupport;
/*     */   }
/*     */ 
/*     */   public boolean isCurrentThreadCpuTimeSupported() {
/*  82 */     return currentThreadCpuTimeSupport;
/*     */   }
/*     */ 
/*     */   public boolean isOtherThreadCpuTimeSupported() {
/*  86 */     return otherThreadCpuTimeSupport;
/*     */   }
/*     */ 
/*     */   public boolean isBootClassPathSupported() {
/*  90 */     return bootClassPathSupport;
/*     */   }
/*     */ 
/*     */   public boolean isObjectMonitorUsageSupported() {
/*  94 */     return objectMonitorUsageSupport;
/*     */   }
/*     */ 
/*     */   public boolean isSynchronizerUsageSupported() {
/*  98 */     return synchronizerUsageSupport;
/*     */   }
/*     */ 
/*     */   public boolean isThreadAllocatedMemorySupported() {
/* 102 */     return threadAllocatedMemorySupport;
/*     */   }
/*     */ 
/*     */   public boolean isGcNotificationSupported() {
/* 106 */     return gcNotificationSupport;
/*     */   }
/*     */   public native boolean isThreadContentionMonitoringEnabled();
/*     */ 
/*     */   public native boolean isThreadCpuTimeEnabled();
/*     */ 
/*     */   public native boolean isThreadAllocatedMemoryEnabled();
/*     */ 
/*     */   public int getLoadedClassCount() {
/* 115 */     long l = getTotalClassCount() - getUnloadedClassCount();
/* 116 */     return (int)l;
/*     */   }
/*     */ 
/*     */   public native long getTotalClassCount();
/*     */ 
/*     */   public native long getUnloadedClassCount();
/*     */ 
/*     */   public native boolean getVerboseClass();
/*     */ 
/*     */   public native boolean getVerboseGC();
/*     */ 
/*     */   public String getManagementVersion() {
/* 128 */     return version;
/*     */   }
/*     */ 
/*     */   public String getVmId() {
/* 132 */     int i = getProcessId();
/* 133 */     String str = "localhost";
/*     */     try {
/* 135 */       str = InetAddress.getLocalHost().getHostName();
/*     */     }
/*     */     catch (UnknownHostException localUnknownHostException)
/*     */     {
/*     */     }
/* 140 */     return i + "@" + str;
/*     */   }
/*     */   private native int getProcessId();
/*     */ 
/*     */   public String getVmName() {
/* 145 */     return System.getProperty("java.vm.name");
/*     */   }
/*     */ 
/*     */   public String getVmVendor() {
/* 149 */     return System.getProperty("java.vm.vendor");
/*     */   }
/*     */   public String getVmVersion() {
/* 152 */     return System.getProperty("java.vm.version");
/*     */   }
/*     */   public String getVmSpecName() {
/* 155 */     return System.getProperty("java.vm.specification.name");
/*     */   }
/*     */   public String getVmSpecVendor() {
/* 158 */     return System.getProperty("java.vm.specification.vendor");
/*     */   }
/*     */   public String getVmSpecVersion() {
/* 161 */     return System.getProperty("java.vm.specification.version");
/*     */   }
/*     */   public String getClassPath() {
/* 164 */     return System.getProperty("java.class.path");
/*     */   }
/*     */   public String getLibraryPath() {
/* 167 */     return System.getProperty("java.library.path");
/*     */   }
/*     */ 
/*     */   public String getBootClassPath() {
/* 171 */     GetPropertyAction localGetPropertyAction = new GetPropertyAction("sun.boot.class.path");
/*     */ 
/* 173 */     String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 174 */     return str;
/*     */   }
/*     */ 
/*     */   public synchronized List<String> getVmArguments()
/*     */   {
/* 179 */     if (this.vmArgs == null) {
/* 180 */       String[] arrayOfString = getVmArguments0();
/* 181 */       List localList = (arrayOfString != null) && (arrayOfString.length != 0) ? Arrays.asList(arrayOfString) : Collections.emptyList();
/*     */ 
/* 183 */       this.vmArgs = Collections.unmodifiableList(localList);
/*     */     }
/* 185 */     return this.vmArgs;
/*     */   }
/*     */   public native String[] getVmArguments0();
/*     */ 
/*     */   public native long getStartupTime();
/*     */ 
/*     */   public native int getAvailableProcessors();
/*     */ 
/*     */   public String getCompilerName() {
/* 194 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/* 197 */         return System.getProperty("sun.management.compiler");
/*     */       }
/*     */     });
/* 200 */     return str;
/*     */   }
/*     */   public native long getTotalCompileTime();
/*     */ 
/*     */   public native long getTotalThreadCount();
/*     */ 
/*     */   public native int getLiveThreadCount();
/*     */ 
/*     */   public native int getPeakThreadCount();
/*     */ 
/*     */   public native int getDaemonThreadCount();
/*     */ 
/* 212 */   public String getOsName() { return System.getProperty("os.name"); }
/*     */ 
/*     */   public String getOsArch() {
/* 215 */     return System.getProperty("os.arch");
/*     */   }
/*     */   public String getOsVersion() {
/* 218 */     return System.getProperty("os.version"); } 
/*     */   public native long getSafepointCount();
/*     */ 
/*     */   public native long getTotalSafepointTime();
/*     */ 
/*     */   public native long getSafepointSyncTime();
/*     */ 
/*     */   public native long getTotalApplicationNonStoppedTime();
/*     */ 
/*     */   public native long getLoadedClassSize();
/*     */ 
/*     */   public native long getUnloadedClassSize();
/*     */ 
/*     */   public native long getClassLoadingTime();
/*     */ 
/*     */   public native long getMethodDataSize();
/*     */ 
/*     */   public native long getInitializedClassCount();
/*     */ 
/*     */   public native long getClassInitializationTime();
/*     */ 
/*     */   public native long getClassVerificationTime();
/*     */ 
/* 240 */   private synchronized PerfInstrumentation getPerfInstrumentation() { if ((this.noPerfData) || (this.perfInstr != null)) {
/* 241 */       return this.perfInstr;
/*     */     }
/*     */ 
/* 245 */     Perf localPerf = (Perf)AccessController.doPrivileged(new Perf.GetPerfAction());
/*     */     try {
/* 247 */       ByteBuffer localByteBuffer = localPerf.attach(0, "r");
/* 248 */       if (localByteBuffer.capacity() == 0) {
/* 249 */         this.noPerfData = true;
/* 250 */         return null;
/*     */       }
/* 252 */       this.perfInstr = new PerfInstrumentation(localByteBuffer);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 256 */       this.noPerfData = true;
/*     */     } catch (IOException localIOException) {
/* 258 */       throw new AssertionError(localIOException);
/*     */     }
/* 260 */     return this.perfInstr; }
/*     */ 
/*     */   public List<Counter> getInternalCounters(String paramString)
/*     */   {
/* 264 */     PerfInstrumentation localPerfInstrumentation = getPerfInstrumentation();
/* 265 */     if (localPerfInstrumentation != null) {
/* 266 */       return localPerfInstrumentation.findByPattern(paramString);
/*     */     }
/* 268 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  64 */     if (version == null) {
/*  65 */       throw new AssertionError("Invalid Management Version");
/*     */     }
/*  67 */     initOptionalSupportFields();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.VMManagementImpl
 * JD-Core Version:    0.6.2
 */