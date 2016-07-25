package javax.management.monitor;

import javax.management.ObjectName;

public abstract interface MonitorMBean
{
  public abstract void start();

  public abstract void stop();

  public abstract void addObservedObject(ObjectName paramObjectName)
    throws IllegalArgumentException;

  public abstract void removeObservedObject(ObjectName paramObjectName);

  public abstract boolean containsObservedObject(ObjectName paramObjectName);

  public abstract ObjectName[] getObservedObjects();

  @Deprecated
  public abstract ObjectName getObservedObject();

  @Deprecated
  public abstract void setObservedObject(ObjectName paramObjectName);

  public abstract String getObservedAttribute();

  public abstract void setObservedAttribute(String paramString);

  public abstract long getGranularityPeriod();

  public abstract void setGranularityPeriod(long paramLong)
    throws IllegalArgumentException;

  public abstract boolean isActive();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.monitor.MonitorMBean
 * JD-Core Version:    0.6.2
 */