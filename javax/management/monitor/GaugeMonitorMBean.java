package javax.management.monitor;

import javax.management.ObjectName;

public abstract interface GaugeMonitorMBean extends MonitorMBean
{
  @Deprecated
  public abstract Number getDerivedGauge();

  @Deprecated
  public abstract long getDerivedGaugeTimeStamp();

  public abstract Number getDerivedGauge(ObjectName paramObjectName);

  public abstract long getDerivedGaugeTimeStamp(ObjectName paramObjectName);

  public abstract Number getHighThreshold();

  public abstract Number getLowThreshold();

  public abstract void setThresholds(Number paramNumber1, Number paramNumber2)
    throws IllegalArgumentException;

  public abstract boolean getNotifyHigh();

  public abstract void setNotifyHigh(boolean paramBoolean);

  public abstract boolean getNotifyLow();

  public abstract void setNotifyLow(boolean paramBoolean);

  public abstract boolean getDifferenceMode();

  public abstract void setDifferenceMode(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.monitor.GaugeMonitorMBean
 * JD-Core Version:    0.6.2
 */