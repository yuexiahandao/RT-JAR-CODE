package com.sun.corba.se.spi.monitoring;

public abstract interface MonitoringManagerFactory
{
  public abstract MonitoringManager createMonitoringManager(String paramString1, String paramString2);

  public abstract void remove(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.monitoring.MonitoringManagerFactory
 * JD-Core Version:    0.6.2
 */