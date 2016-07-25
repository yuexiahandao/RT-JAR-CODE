package sun.rmi.registry;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.MarshalException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.UnmarshalException;
import java.rmi.registry.Registry;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;

public final class RegistryImpl_Stub extends RemoteStub
  implements Registry, Remote
{
  private static final Operation[] operations = { new Operation("void bind(java.lang.String, java.rmi.Remote)"), new Operation("java.lang.String list()[]"), new Operation("java.rmi.Remote lookup(java.lang.String)"), new Operation("void rebind(java.lang.String, java.rmi.Remote)"), new Operation("void unbind(java.lang.String)") };
  private static final long interfaceHash = 4905912898345647071L;

  public RegistryImpl_Stub()
  {
  }

  public RegistryImpl_Stub(RemoteRef paramRemoteRef)
  {
    super(paramRemoteRef);
  }

  public void bind(String paramString, Remote paramRemote)
    throws AccessException, AlreadyBoundException, RemoteException
  {
    try
    {
      RemoteCall localRemoteCall = this.ref.newCall(this, operations, 0, 4905912898345647071L);
      try
      {
        ObjectOutput localObjectOutput = localRemoteCall.getOutputStream();
        localObjectOutput.writeObject(paramString);
        localObjectOutput.writeObject(paramRemote);
      }
      catch (IOException localIOException)
      {
        throw new MarshalException("error marshalling arguments", localIOException);
      }
      this.ref.invoke(localRemoteCall);
      this.ref.done(localRemoteCall);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException;
    }
    catch (AlreadyBoundException localAlreadyBoundException)
    {
      throw localAlreadyBoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public String[] list()
    throws AccessException, RemoteException
  {
    try
    {
      RemoteCall localRemoteCall = this.ref.newCall(this, operations, 1, 4905912898345647071L);
      this.ref.invoke(localRemoteCall);
      String[] arrayOfString;
      try
      {
        ObjectInput localObjectInput = localRemoteCall.getInputStream();
        arrayOfString = (String[])localObjectInput.readObject();
      }
      catch (IOException localIOException)
      {
        throw new UnmarshalException("error unmarshalling return", localIOException);
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        throw new UnmarshalException("error unmarshalling return", localClassNotFoundException);
      }
      finally
      {
        this.ref.done(localRemoteCall);
      }
      return arrayOfString;
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

  public Remote lookup(String paramString)
    throws AccessException, NotBoundException, RemoteException
  {
    try
    {
      RemoteCall localRemoteCall = this.ref.newCall(this, operations, 2, 4905912898345647071L);
      try
      {
        ObjectOutput localObjectOutput = localRemoteCall.getOutputStream();
        localObjectOutput.writeObject(paramString);
      }
      catch (IOException localIOException1)
      {
        throw new MarshalException("error marshalling arguments", localIOException1);
      }
      this.ref.invoke(localRemoteCall);
      Remote localRemote;
      try
      {
        ObjectInput localObjectInput = localRemoteCall.getInputStream();
        localRemote = (Remote)localObjectInput.readObject();
      }
      catch (IOException localIOException2)
      {
        throw new UnmarshalException("error unmarshalling return", localIOException2);
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        throw new UnmarshalException("error unmarshalling return", localClassNotFoundException);
      }
      finally
      {
        this.ref.done(localRemoteCall);
      }
      return localRemote;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException;
    }
    catch (NotBoundException localNotBoundException)
    {
      throw localNotBoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public void rebind(String paramString, Remote paramRemote)
    throws AccessException, RemoteException
  {
    try
    {
      RemoteCall localRemoteCall = this.ref.newCall(this, operations, 3, 4905912898345647071L);
      try
      {
        ObjectOutput localObjectOutput = localRemoteCall.getOutputStream();
        localObjectOutput.writeObject(paramString);
        localObjectOutput.writeObject(paramRemote);
      }
      catch (IOException localIOException)
      {
        throw new MarshalException("error marshalling arguments", localIOException);
      }
      this.ref.invoke(localRemoteCall);
      this.ref.done(localRemoteCall);
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

  public void unbind(String paramString)
    throws AccessException, NotBoundException, RemoteException
  {
    try
    {
      RemoteCall localRemoteCall = this.ref.newCall(this, operations, 4, 4905912898345647071L);
      try
      {
        ObjectOutput localObjectOutput = localRemoteCall.getOutputStream();
        localObjectOutput.writeObject(paramString);
      }
      catch (IOException localIOException)
      {
        throw new MarshalException("error marshalling arguments", localIOException);
      }
      this.ref.invoke(localRemoteCall);
      this.ref.done(localRemoteCall);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException;
    }
    catch (NotBoundException localNotBoundException)
    {
      throw localNotBoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.registry.RegistryImpl_Stub
 * JD-Core Version:    0.6.2
 */