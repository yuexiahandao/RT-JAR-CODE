package java.rmi.activation;

import java.lang.reflect.Method;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;

public final class ActivationGroup_Stub extends RemoteStub
  implements ActivationInstantiator, Remote
{
  private static final long serialVersionUID = 2L;
  private static Method $method_newInstance_0;

  static
  {
    try
    {
      $method_newInstance_0 = Remote.class.getMethod("newInstance", new Class[] { ActivationID.class, ActivationDesc.class });
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new NoSuchMethodError("stub class initialization failed");
    }
  }

  public ActivationGroup_Stub(RemoteRef paramRemoteRef)
  {
    super(paramRemoteRef);
  }

  public MarshalledObject newInstance(ActivationID paramActivationID, ActivationDesc paramActivationDesc)
    throws RemoteException, ActivationException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_newInstance_0, new Object[] { paramActivationID, paramActivationDesc }, -5274445189091581345L);
      return (MarshalledObject)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException;
    }
    catch (ActivationException localActivationException)
    {
      throw localActivationException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivationGroup_Stub
 * JD-Core Version:    0.6.2
 */