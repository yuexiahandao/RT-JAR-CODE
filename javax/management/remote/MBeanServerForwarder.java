package javax.management.remote;

import javax.management.MBeanServer;

public abstract interface MBeanServerForwarder extends MBeanServer
{
  public abstract MBeanServer getMBeanServer();

  public abstract void setMBeanServer(MBeanServer paramMBeanServer);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.MBeanServerForwarder
 * JD-Core Version:    0.6.2
 */