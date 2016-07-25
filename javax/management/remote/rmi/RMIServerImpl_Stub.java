package javax.management.remote.rmi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;

public final class RMIServerImpl_Stub extends RemoteStub
  implements RMIServer
{
  private static final long serialVersionUID = 2L;
  private static Method $method_getVersion_0;
  private static Method $method_newClient_1;

  static
  {
    try
    {
      $method_getVersion_0 = RMIServer.class.getMethod("getVersion", new Class[0]);
      $method_newClient_1 = RMIServer.class.getMethod("newClient", new Class[] { Object.class });
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new NoSuchMethodError("stub class initialization failed");
    }
  }

  public RMIServerImpl_Stub(RemoteRef paramRemoteRef)
  {
    super(paramRemoteRef);
  }

  public String getVersion()
    throws RemoteException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getVersion_0, null, -8081107751519807347L);
      return (String)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public RMIConnection newClient(Object paramObject)
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_newClient_1, new Object[] { paramObject }, -1089742558549201240L);
      return (RMIConnection)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIServerImpl_Stub
 * JD-Core Version:    0.6.2
 */