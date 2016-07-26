package java.lang.management;

public abstract interface GarbageCollectorMXBean extends MemoryManagerMXBean {
    public abstract long getCollectionCount();

    public abstract long getCollectionTime();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.GarbageCollectorMXBean
 * JD-Core Version:    0.6.2
 */