package java.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface ServerRef extends RemoteRef
{
  public static final long serialVersionUID = -4557750989390278438L;

  public abstract RemoteStub exportObject(Remote paramRemote, Object paramObject)
    throws RemoteException;

  public abstract String getClientHost()
    throws ServerNotActiveException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.ServerRef
 * JD-Core Version:    0.6.2
 */