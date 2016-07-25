package sun.rmi.transport;

import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.UnmarshalException;
import java.rmi.dgc.DGC;
import java.rmi.dgc.Lease;
import java.rmi.dgc.VMID;
import java.rmi.server.ObjID;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;

public final class DGCImpl_Stub extends RemoteStub
  implements DGC
{
  private static final Operation[] operations = { new Operation("void clean(java.rmi.server.ObjID[], long, java.rmi.dgc.VMID, boolean)"), new Operation("java.rmi.dgc.Lease dirty(java.rmi.server.ObjID[], long, java.rmi.dgc.Lease)") };
  private static final long interfaceHash = -669196253586618813L;

  public DGCImpl_Stub()
  {
  }

  public DGCImpl_Stub(RemoteRef paramRemoteRef)
  {
    super(paramRemoteRef);
  }

  public void clean(ObjID[] paramArrayOfObjID, long paramLong, VMID paramVMID, boolean paramBoolean)
    throws RemoteException
  {
    try
    {
      RemoteCall localRemoteCall = this.ref.newCall(this, operations, 0, -669196253586618813L);
      try
      {
        ObjectOutput localObjectOutput = localRemoteCall.getOutputStream();
        localObjectOutput.writeObject(paramArrayOfObjID);
        localObjectOutput.writeLong(paramLong);
        localObjectOutput.writeObject(paramVMID);
        localObjectOutput.writeBoolean(paramBoolean);
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

  public Lease dirty(ObjID[] paramArrayOfObjID, long paramLong, Lease paramLease)
    throws RemoteException
  {
    try
    {
      RemoteCall localRemoteCall = this.ref.newCall(this, operations, 1, -669196253586618813L);
      try
      {
        ObjectOutput localObjectOutput = localRemoteCall.getOutputStream();
        localObjectOutput.writeObject(paramArrayOfObjID);
        localObjectOutput.writeLong(paramLong);
        localObjectOutput.writeObject(paramLease);
      }
      catch (IOException localIOException1)
      {
        throw new MarshalException("error marshalling arguments", localIOException1);
      }
      this.ref.invoke(localRemoteCall);
      Lease localLease;
      try
      {
        ObjectInput localObjectInput = localRemoteCall.getInputStream();
        localLease = (Lease)localObjectInput.readObject();
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
      return localLease;
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
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.DGCImpl_Stub
 * JD-Core Version:    0.6.2
 */