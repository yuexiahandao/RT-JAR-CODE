package com.sun.jmx.mbeanserver;

import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;

public abstract interface SunJmxMBeanServer extends MBeanServer
{
  public abstract MBeanInstantiator getMBeanInstantiator();

  public abstract boolean interceptorsEnabled();

  public abstract MBeanServer getMBeanServerInterceptor();

  public abstract void setMBeanServerInterceptor(MBeanServer paramMBeanServer);

  public abstract MBeanServerDelegate getMBeanServerDelegate();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.SunJmxMBeanServer
 * JD-Core Version:    0.6.2
 */