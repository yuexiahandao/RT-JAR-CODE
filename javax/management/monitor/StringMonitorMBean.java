package javax.management.monitor;

import javax.management.ObjectName;

public abstract interface StringMonitorMBean extends MonitorMBean
{
  @Deprecated
  public abstract String getDerivedGauge();

  @Deprecated
  public abstract long getDerivedGaugeTimeStamp();

  public abstract String getDerivedGauge(ObjectName paramObjectName);

  public abstract long getDerivedGaugeTimeStamp(ObjectName paramObjectName);

  public abstract String getStringToCompare();

  public abstract void setStringToCompare(String paramString)
    throws IllegalArgumentException;

  public abstract boolean getNotifyMatch();

  public abstract void setNotifyMatch(boolean paramBoolean);

  public abstract boolean getNotifyDiffer();

  public abstract void setNotifyDiffer(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.monitor.StringMonitorMBean
 * JD-Core Version:    0.6.2
 */