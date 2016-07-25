package javax.xml.ws;

import java.util.List;
import javax.xml.ws.handler.Handler;

public abstract interface Binding
{
  public abstract List<Handler> getHandlerChain();

  public abstract void setHandlerChain(List<Handler> paramList);

  public abstract String getBindingID();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.Binding
 * JD-Core Version:    0.6.2
 */