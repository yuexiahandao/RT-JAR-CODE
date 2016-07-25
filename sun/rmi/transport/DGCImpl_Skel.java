package sun.rmi.transport;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.MarshalException;
import java.rmi.Remote;
import java.rmi.UnmarshalException;
import java.rmi.dgc.Lease;
import java.rmi.dgc.VMID;
import java.rmi.server.ObjID;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.Skeleton;
import java.rmi.server.SkeletonMismatchException;

public final class DGCImpl_Skel
  implements Skeleton
{
  private static final Operation[] operations = { new Operation("void clean(java.rmi.server.ObjID[], long, java.rmi.dgc.VMID, boolean)"), new Operation("java.rmi.dgc.Lease dirty(java.rmi.server.ObjID[], long, java.rmi.dgc.Lease)") };
  private static final long interfaceHash = -669196253586618813L;

  public void dispatch(Remote paramRemote, RemoteCall paramRemoteCall, int paramInt, long paramLong)
    throws Exception
  {
    if (paramLong != -669196253586618813L)
      throw new SkeletonMismatchException("interface hash mismatch");
    DGCImpl localDGCImpl = (DGCImpl)paramRemote;
    ObjID[] arrayOfObjID;
    long l;
    Object localObject1;
    switch (paramInt)
    {
    case 0:
      boolean bool;
      try
      {
        ObjectInput localObjectInput2 = paramRemoteCall.getInputStream();
        arrayOfObjID = (ObjID[])localObjectInput2.readObject();
        l = localObjectInput2.readLong();
        localObject1 = (VMID)localObjectInput2.readObject();
        bool = localObjectInput2.readBoolean();
      }
      catch (IOException localIOException4)
      {
        throw new UnmarshalException("error unmarshalling arguments", localIOException4);
      }
      catch (ClassNotFoundException localClassNotFoundException2)
      {
        throw new UnmarshalException("error unmarshalling arguments", localClassNotFoundException2);
      }
      finally
      {
        paramRemoteCall.releaseInputStream();
      }
      localDGCImpl.clean(arrayOfObjID, l, (VMID)localObject1, bool);
      try
      {
        paramRemoteCall.getResultStream(true);
      }
      catch (IOException localIOException1)
      {
        throw new MarshalException("error marshalling return", localIOException1);
      }
    case 1:
      try
      {
        ObjectInput localObjectInput1 = paramRemoteCall.getInputStream();
        arrayOfObjID = (ObjID[])localObjectInput1.readObject();
        l = localObjectInput1.readLong();
        localObject1 = (Lease)localObjectInput1.readObject();
      }
      catch (IOException localIOException3)
      {
        throw new UnmarshalException("error unmarshalling arguments", localIOException3);
      }
      catch (ClassNotFoundException localClassNotFoundException1)
      {
        throw new UnmarshalException("error unmarshalling arguments", localClassNotFoundException1);
      }
      finally
      {
        paramRemoteCall.releaseInputStream();
      }
      Lease localLease = localDGCImpl.dirty(arrayOfObjID, l, (Lease)localObject1);
      try
      {
        ObjectOutput localObjectOutput = paramRemoteCall.getResultStream(true);
        localObjectOutput.writeObject(localLease);
      }
      catch (IOException localIOException2)
      {
        throw new MarshalException("error marshalling return", localIOException2);
      }
    default:
      throw new UnmarshalException("invalid method number");
    }
  }

  public Operation[] getOperations()
  {
    return (Operation[])operations.clone();
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.DGCImpl_Skel
 * JD-Core Version:    0.6.2
 */