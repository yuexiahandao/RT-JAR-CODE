package sun.management;

import java.util.List;
import sun.management.counter.Counter;

public abstract interface HotspotClassLoadingMBean
{
  public abstract long getLoadedClassSize();

  public abstract long getUnloadedClassSize();

  public abstract long getClassLoadingTime();

  public abstract long getMethodDataSize();

  public abstract long getInitializedClassCount();

  public abstract long getClassInitializationTime();

  public abstract long getClassVerificationTime();

  public abstract List<Counter> getInternalClassLoadingCounters();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotspotClassLoadingMBean
 * JD-Core Version:    0.6.2
 */