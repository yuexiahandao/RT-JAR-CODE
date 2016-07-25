package javax.rmi.CORBA;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface PortableRemoteObjectDelegate
{
  public abstract void exportObject(Remote paramRemote)
    throws RemoteException;

  public abstract Remote toStub(Remote paramRemote)
    throws NoSuchObjectException;

  public abstract void unexportObject(Remote paramRemote)
    throws NoSuchObjectException;

  public abstract Object narrow(Object paramObject, Class paramClass)
    throws ClassCastException;

  public abstract void connect(Remote paramRemote1, Remote paramRemote2)
    throws RemoteException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.PortableRemoteObjectDelegate
 * JD-Core Version:    0.6.2
 */