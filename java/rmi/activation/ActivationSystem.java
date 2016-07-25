package java.rmi.activation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface ActivationSystem extends Remote
{
  public static final int SYSTEM_PORT = 1098;

  public abstract ActivationID registerObject(ActivationDesc paramActivationDesc)
    throws ActivationException, UnknownGroupException, RemoteException;

  public abstract void unregisterObject(ActivationID paramActivationID)
    throws ActivationException, UnknownObjectException, RemoteException;

  public abstract ActivationGroupID registerGroup(ActivationGroupDesc paramActivationGroupDesc)
    throws ActivationException, RemoteException;

  public abstract ActivationMonitor activeGroup(ActivationGroupID paramActivationGroupID, ActivationInstantiator paramActivationInstantiator, long paramLong)
    throws UnknownGroupException, ActivationException, RemoteException;

  public abstract void unregisterGroup(ActivationGroupID paramActivationGroupID)
    throws ActivationException, UnknownGroupException, RemoteException;

  public abstract void shutdown()
    throws RemoteException;

  public abstract ActivationDesc setActivationDesc(ActivationID paramActivationID, ActivationDesc paramActivationDesc)
    throws ActivationException, UnknownObjectException, UnknownGroupException, RemoteException;

  public abstract ActivationGroupDesc setActivationGroupDesc(ActivationGroupID paramActivationGroupID, ActivationGroupDesc paramActivationGroupDesc)
    throws ActivationException, UnknownGroupException, RemoteException;

  public abstract ActivationDesc getActivationDesc(ActivationID paramActivationID)
    throws ActivationException, UnknownObjectException, RemoteException;

  public abstract ActivationGroupDesc getActivationGroupDesc(ActivationGroupID paramActivationGroupID)
    throws ActivationException, UnknownGroupException, RemoteException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivationSystem
 * JD-Core Version:    0.6.2
 */