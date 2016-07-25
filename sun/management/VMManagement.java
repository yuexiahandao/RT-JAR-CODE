package sun.management;

import java.util.List;
import sun.management.counter.Counter;

public abstract interface VMManagement
{
  public abstract boolean isCompilationTimeMonitoringSupported();

  public abstract boolean isThreadContentionMonitoringSupported();

  public abstract boolean isThreadContentionMonitoringEnabled();

  public abstract boolean isCurrentThreadCpuTimeSupported();

  public abstract boolean isOtherThreadCpuTimeSupported();

  public abstract boolean isThreadCpuTimeEnabled();

  public abstract boolean isBootClassPathSupported();

  public abstract boolean isObjectMonitorUsageSupported();

  public abstract boolean isSynchronizerUsageSupported();

  public abstract boolean isThreadAllocatedMemorySupported();

  public abstract boolean isThreadAllocatedMemoryEnabled();

  public abstract boolean isGcNotificationSupported();

  public abstract long getTotalClassCount();

  public abstract int getLoadedClassCount();

  public abstract long getUnloadedClassCount();

  public abstract boolean getVerboseClass();

  public abstract boolean getVerboseGC();

  public abstract String getManagementVersion();

  public abstract String getVmId();

  public abstract String getVmName();

  public abstract String getVmVendor();

  public abstract String getVmVersion();

  public abstract String getVmSpecName();

  public abstract String getVmSpecVendor();

  public abstract String getVmSpecVersion();

  public abstract String getClassPath();

  public abstract String getLibraryPath();

  public abstract String getBootClassPath();

  public abstract List<String> getVmArguments();

  public abstract long getStartupTime();

  public abstract int getAvailableProcessors();

  public abstract String getCompilerName();

  public abstract long getTotalCompileTime();

  public abstract long getTotalThreadCount();

  public abstract int getLiveThreadCount();

  public abstract int getPeakThreadCount();

  public abstract int getDaemonThreadCount();

  public abstract String getOsName();

  public abstract String getOsArch();

  public abstract String getOsVersion();

  public abstract long getSafepointCount();

  public abstract long getTotalSafepointTime();

  public abstract long getSafepointSyncTime();

  public abstract long getTotalApplicationNonStoppedTime();

  public abstract long getLoadedClassSize();

  public abstract long getUnloadedClassSize();

  public abstract long getClassLoadingTime();

  public abstract long getMethodDataSize();

  public abstract long getInitializedClassCount();

  public abstract long getClassInitializationTime();

  public abstract long getClassVerificationTime();

  public abstract List<Counter> getInternalCounters(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.VMManagement
 * JD-Core Version:    0.6.2
 */