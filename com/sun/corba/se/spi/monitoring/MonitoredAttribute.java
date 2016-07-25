package com.sun.corba.se.spi.monitoring;

public abstract interface MonitoredAttribute
{
  public abstract MonitoredAttributeInfo getAttributeInfo();

  public abstract void setValue(Object paramObject);

  public abstract Object getValue();

  public abstract String getName();

  public abstract void clearState();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.monitoring.MonitoredAttribute
 * JD-Core Version:    0.6.2
 */