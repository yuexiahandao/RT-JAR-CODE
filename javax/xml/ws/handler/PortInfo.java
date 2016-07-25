package javax.xml.ws.handler;

import javax.xml.namespace.QName;

public abstract interface PortInfo
{
  public abstract QName getServiceName();

  public abstract QName getPortName();

  public abstract String getBindingID();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.handler.PortInfo
 * JD-Core Version:    0.6.2
 */