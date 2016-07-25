package sun.rmi.transport;

import java.rmi.RemoteException;

public abstract interface Channel
{
  public abstract Connection newConnection()
    throws RemoteException;

  public abstract Endpoint getEndpoint();

  public abstract void free(Connection paramConnection, boolean paramBoolean)
    throws RemoteException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.Channel
 * JD-Core Version:    0.6.2
 */