package javax.naming.event;

import java.util.EventListener;

public abstract interface NamingListener extends EventListener
{
  public abstract void namingExceptionThrown(NamingExceptionEvent paramNamingExceptionEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.event.NamingListener
 * JD-Core Version:    0.6.2
 */