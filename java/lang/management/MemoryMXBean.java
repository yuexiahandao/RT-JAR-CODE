package java.lang.management;

public abstract interface MemoryMXBean extends PlatformManagedObject {
    public abstract int getObjectPendingFinalizationCount();

    public abstract MemoryUsage getHeapMemoryUsage();

    public abstract MemoryUsage getNonHeapMemoryUsage();

    public abstract boolean isVerbose();

    public abstract void setVerbose(boolean paramBoolean);

    public abstract void gc();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.MemoryMXBean
 * JD-Core Version:    0.6.2
 */