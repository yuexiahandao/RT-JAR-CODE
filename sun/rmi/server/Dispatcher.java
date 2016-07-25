package sun.rmi.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.server.RemoteCall;

public abstract interface Dispatcher
{
  public abstract void dispatch(Remote paramRemote, RemoteCall paramRemoteCall)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.Dispatcher
 * JD-Core Version:    0.6.2
 */