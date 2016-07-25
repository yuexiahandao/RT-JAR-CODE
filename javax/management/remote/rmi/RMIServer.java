package javax.management.remote.rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface RMIServer extends Remote
{
  public abstract String getVersion()
    throws RemoteException;

  public abstract RMIConnection newClient(Object paramObject)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIServer
 * JD-Core Version:    0.6.2
 */