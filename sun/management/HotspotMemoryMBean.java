package sun.management;

import java.util.List;
import sun.management.counter.Counter;

public abstract interface HotspotMemoryMBean
{
  public abstract List<Counter> getInternalMemoryCounters();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotspotMemoryMBean
 * JD-Core Version:    0.6.2
 */