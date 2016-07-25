package javax.xml.ws.handler.soap;

import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;

public abstract interface SOAPHandler<T extends SOAPMessageContext> extends Handler<T>
{
  public abstract Set<QName> getHeaders();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.handler.soap.SOAPHandler
 * JD-Core Version:    0.6.2
 */