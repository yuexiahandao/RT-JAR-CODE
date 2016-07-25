package sun.rmi.transport;

import java.rmi.RemoteException;

public abstract interface Endpoint
{
  public abstract Channel getChannel();

  public abstract void exportObject(Target paramTarget)
    throws RemoteException;

  public abstract Transport getInboundTransport();

  public abstract Transport getOutboundTransport();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.Endpoint
 * JD-Core Version:    0.6.2
 */