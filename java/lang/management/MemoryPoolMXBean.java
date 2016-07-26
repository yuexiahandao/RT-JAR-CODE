package java.lang.management;

public abstract interface MemoryPoolMXBean extends PlatformManagedObject {
    public abstract String getName();

    public abstract MemoryType getType();

    public abstract MemoryUsage getUsage();

    public abstract MemoryUsage getPeakUsage();

    public abstract void resetPeakUsage();

    public abstract boolean isValid();

    public abstract String[] getMemoryManagerNames();

    public abstract long getUsageThreshold();

    public abstract void setUsageThreshold(long paramLong);

    public abstract boolean isUsageThresholdExceeded();

    public abstract long getUsageThresholdCount();

    public abstract boolean isUsageThresholdSupported();

    public abstract long getCollectionUsageThreshold();

    public abstract void setCollectionUsageThreshold(long paramLong);

    public abstract boolean isCollectionUsageThresholdExceeded();

    public abstract long getCollectionUsageThresholdCount();

    public abstract MemoryUsage getCollectionUsage();

    public abstract boolean isCollectionUsageThresholdSupported();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.MemoryPoolMXBean
 * JD-Core Version:    0.6.2
 */