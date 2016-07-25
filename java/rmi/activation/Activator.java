package java.rmi.activation;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface Activator extends Remote
{
  public abstract MarshalledObject<? extends Remote> activate(ActivationID paramActivationID, boolean paramBoolean)
    throws ActivationException, UnknownObjectException, RemoteException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.Activator
 * JD-Core Version:    0.6.2
 */