package java.lang.management;

public abstract interface MemoryManagerMXBean extends PlatformManagedObject {
    public abstract String getName();

    public abstract boolean isValid();

    public abstract String[] getMemoryPoolNames();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.MemoryManagerMXBean
 * JD-Core Version:    0.6.2
 */