package com.sun.management;

public abstract interface OperatingSystemMXBean extends java.lang.management.OperatingSystemMXBean
{
  public abstract long getCommittedVirtualMemorySize();

  public abstract long getTotalSwapSpaceSize();

  public abstract long getFreeSwapSpaceSize();

  public abstract long getProcessCpuTime();

  public abstract long getFreePhysicalMemorySize();

  public abstract long getTotalPhysicalMemorySize();

  public abstract double getSystemCpuLoad();

  public abstract double getProcessCpuLoad();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.OperatingSystemMXBean
 * JD-Core Version:    0.6.2
 */