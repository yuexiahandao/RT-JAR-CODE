package javax.sql;

import java.util.EventListener;

public abstract interface ConnectionEventListener extends EventListener
{
  public abstract void connectionClosed(ConnectionEvent paramConnectionEvent);

  public abstract void connectionErrorOccurred(ConnectionEvent paramConnectionEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.ConnectionEventListener
 * JD-Core Version:    0.6.2
 */