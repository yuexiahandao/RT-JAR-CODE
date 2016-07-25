package java.rmi.server;

import java.rmi.Remote;

@Deprecated
public abstract interface Skeleton
{
  @Deprecated
  public abstract void dispatch(Remote paramRemote, RemoteCall paramRemoteCall, int paramInt, long paramLong)
    throws Exception;

  @Deprecated
  public abstract Operation[] getOperations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.Skeleton
 * JD-Core Version:    0.6.2
 */