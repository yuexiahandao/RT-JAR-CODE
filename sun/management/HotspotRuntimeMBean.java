package sun.management;

import java.util.List;
import sun.management.counter.Counter;

public abstract interface HotspotRuntimeMBean
{
  public abstract long getSafepointCount();

  public abstract long getTotalSafepointTime();

  public abstract long getSafepointSyncTime();

  public abstract List<Counter> getInternalRuntimeCounters();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotspotRuntimeMBean
 * JD-Core Version:    0.6.2
 */