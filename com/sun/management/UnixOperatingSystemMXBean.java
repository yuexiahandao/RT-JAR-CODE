package com.sun.management;

public abstract interface UnixOperatingSystemMXBean extends OperatingSystemMXBean
{
  public abstract long getOpenFileDescriptorCount();

  public abstract long getMaxFileDescriptorCount();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.UnixOperatingSystemMXBean
 * JD-Core Version:    0.6.2
 */