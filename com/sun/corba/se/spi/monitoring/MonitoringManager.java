package com.sun.corba.se.spi.monitoring;

import java.io.Closeable;

public abstract interface MonitoringManager extends Closeable
{
  public abstract MonitoredObject getRootMonitoredObject();

  public abstract void clearState();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.monitoring.MonitoringManager
 * JD-Core Version:    0.6.2
 */