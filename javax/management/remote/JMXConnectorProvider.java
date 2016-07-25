package javax.management.remote;

import java.io.IOException;
import java.util.Map;

public abstract interface JMXConnectorProvider
{
  public abstract JMXConnector newJMXConnector(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXConnectorProvider
 * JD-Core Version:    0.6.2
 */