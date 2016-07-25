package com.sun.corba.se.spi.monitoring;

import java.util.Collection;

public abstract interface MonitoredObject
{
  public abstract String getName();

  public abstract String getDescription();

  public abstract void addChild(MonitoredObject paramMonitoredObject);

  public abstract void removeChild(String paramString);

  public abstract MonitoredObject getChild(String paramString);

  public abstract Collection getChildren();

  public abstract void setParent(MonitoredObject paramMonitoredObject);

  public abstract MonitoredObject getParent();

  public abstract void addAttribute(MonitoredAttribute paramMonitoredAttribute);

  public abstract void removeAttribute(String paramString);

  public abstract MonitoredAttribute getAttribute(String paramString);

  public abstract Collection getAttributes();

  public abstract void clearState();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.monitoring.MonitoredObject
 * JD-Core Version:    0.6.2
 */