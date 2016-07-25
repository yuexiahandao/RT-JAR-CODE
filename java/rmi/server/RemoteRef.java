package java.rmi.server;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface RemoteRef extends Externalizable
{
  public static final long serialVersionUID = 3632638527362204081L;
  public static final String packagePrefix = "sun.rmi.server";

  public abstract Object invoke(Remote paramRemote, Method paramMethod, Object[] paramArrayOfObject, long paramLong)
    throws Exception;

  @Deprecated
  public abstract RemoteCall newCall(RemoteObject paramRemoteObject, Operation[] paramArrayOfOperation, int paramInt, long paramLong)
    throws RemoteException;

  @Deprecated
  public abstract void invoke(RemoteCall paramRemoteCall)
    throws Exception;

  @Deprecated
  public abstract void done(RemoteCall paramRemoteCall)
    throws RemoteException;

  public abstract String getRefClass(ObjectOutput paramObjectOutput);

  public abstract int remoteHashCode();

  public abstract boolean remoteEquals(RemoteRef paramRemoteRef);

  public abstract String remoteToString();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.RemoteRef
 * JD-Core Version:    0.6.2
 */