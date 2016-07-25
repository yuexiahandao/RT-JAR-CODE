package javax.management.remote;

import java.io.IOException;
import java.util.Map;

public abstract interface JMXConnectorServerMBean
{
  public abstract void start()
    throws IOException;

  public abstract void stop()
    throws IOException;

  public abstract boolean isActive();

  public abstract void setMBeanServerForwarder(MBeanServerForwarder paramMBeanServerForwarder);

  public abstract String[] getConnectionIds();

  public abstract JMXServiceURL getAddress();

  public abstract Map<String, ?> getAttributes();

  public abstract JMXConnector toJMXConnector(Map<String, ?> paramMap)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXConnectorServerMBean
 * JD-Core Version:    0.6.2
 */