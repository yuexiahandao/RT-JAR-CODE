package java.lang.management;

public abstract interface ClassLoadingMXBean extends PlatformManagedObject
{
  public abstract long getTotalLoadedClassCount();

  public abstract int getLoadedClassCount();

  public abstract long getUnloadedClassCount();

  public abstract boolean isVerbose();

  public abstract void setVerbose(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.ClassLoadingMXBean
 * JD-Core Version:    0.6.2
 */