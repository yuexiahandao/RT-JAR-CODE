package java.lang.management;

public abstract interface CompilationMXBean extends PlatformManagedObject
{
  public abstract String getName();

  public abstract boolean isCompilationTimeMonitoringSupported();

  public abstract long getTotalCompilationTime();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.CompilationMXBean
 * JD-Core Version:    0.6.2
 */