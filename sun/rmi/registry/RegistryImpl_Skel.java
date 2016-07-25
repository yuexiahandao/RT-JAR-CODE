package sun.rmi.registry;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.MarshalException;
import java.rmi.Remote;
import java.rmi.UnmarshalException;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.Skeleton;
import java.rmi.server.SkeletonMismatchException;

public final class RegistryImpl_Skel
  implements Skeleton
{
  private static final Operation[] operations = { new Operation("void bind(java.lang.String, java.rmi.Remote)"), new Operation("java.lang.String list()[]"), new Operation("java.rmi.Remote lookup(java.lang.String)"), new Operation("void rebind(java.lang.String, java.rmi.Remote)"), new Operation("void unbind(java.lang.String)") };
  private static final long interfaceHash = 4905912898345647071L;

  public void dispatch(Remote paramRemote, RemoteCall paramRemoteCall, int paramInt, long paramLong)
    throws Exception
  {
    if (paramLong != 4905912898345647071L)
      throw new SkeletonMismatchException("interface hash mismatch");
    RegistryImpl localRegistryImpl = (RegistryImpl)paramRemote;
    Object localObject1;
    Object localObject2;
    Remote localRemote;
    switch (paramInt)
    {
    case 0:
      try
      {
        ObjectInput localObjectInput3 = paramRemoteCall.getInputStream();
        localObject1 = (String)localObjectInput3.readObject();
        localObject2 = (Remote)localObjectInput3.readObject();
      }
      catch (IOException localIOException8)
      {
        throw new UnmarshalException("error unmarshalling arguments", localIOException8);
      }
      catch (ClassNotFoundException localClassNotFoundException3)
      {
        throw new UnmarshalException("error unmarshalling arguments", localClassNotFoundException3);
      }
      finally
      {
        paramRemoteCall.releaseInputStream();
      }
      localRegistryImpl.bind((String)localObject1, (Remote)localObject2);
      try
      {
        paramRemoteCall.getResultStream(true);
      }
      catch (IOException localIOException3)
      {
        throw new MarshalException("error marshalling return", localIOException3);
      }
    case 1:
      paramRemoteCall.releaseInputStream();
      localObject1 = localRegistryImpl.list();
      try
      {
        localObject2 = paramRemoteCall.getResultStream(true);
        ((ObjectOutput)localObject2).writeObject(localObject1);
      }
      catch (IOException localIOException1)
      {
        throw new MarshalException("error marshalling return", localIOException1);
      }
    case 2:
      try
      {
        ObjectInput localObjectInput1 = paramRemoteCall.getInputStream();
        localObject1 = (String)localObjectInput1.readObject();
      }
      catch (IOException localIOException6)
      {
        throw new UnmarshalException("error unmarshalling arguments", localIOException6);
      }
      catch (ClassNotFoundException localClassNotFoundException1)
      {
        throw new UnmarshalException("error unmarshalling arguments", localClassNotFoundException1);
      }
      finally
      {
        paramRemoteCall.releaseInputStream();
      }
      localRemote = localRegistryImpl.lookup((String)localObject1);
      try
      {
        ObjectOutput localObjectOutput = paramRemoteCall.getResultStream(true);
        localObjectOutput.writeObject(localRemote);
      }
      catch (IOException localIOException4)
      {
        throw new MarshalException("error marshalling return", localIOException4);
      }
    case 3:
      try
      {
        ObjectInput localObjectInput4 = paramRemoteCall.getInputStream();
        localObject1 = (String)localObjectInput4.readObject();
        localRemote = (Remote)localObjectInput4.readObject();
      }
      catch (IOException localIOException9)
      {
        throw new UnmarshalException("error unmarshalling arguments", localIOException9);
      }
      catch (ClassNotFoundException localClassNotFoundException4)
      {
        throw new UnmarshalException("error unmarshalling arguments", localClassNotFoundException4);
      }
      finally
      {
        paramRemoteCall.releaseInputStream();
      }
      localRegistryImpl.rebind((String)localObject1, localRemote);
      try
      {
        paramRemoteCall.getResultStream(true);
      }
      catch (IOException localIOException5)
      {
        throw new MarshalException("error marshalling return", localIOException5);
      }
    case 4:
      try
      {
        ObjectInput localObjectInput2 = paramRemoteCall.getInputStream();
        localObject1 = (String)localObjectInput2.readObject();
      }
      catch (IOException localIOException7)
      {
        throw new UnmarshalException("error unmarshalling arguments", localIOException7);
      }
      catch (ClassNotFoundException localClassNotFoundException2)
      {
        throw new UnmarshalException("error unmarshalling arguments", localClassNotFoundException2);
      }
      finally
      {
        paramRemoteCall.releaseInputStream();
      }
      localRegistryImpl.unbind((String)localObject1);
      try
      {
        paramRemoteCall.getResultStream(true);
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
 * Qualified Name:     sun.rmi.registry.RegistryImpl_Skel
 * JD-Core Version:    0.6.2
 */