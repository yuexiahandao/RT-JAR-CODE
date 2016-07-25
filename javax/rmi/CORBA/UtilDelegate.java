package javax.rmi.CORBA;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

public abstract interface UtilDelegate
{
  public abstract RemoteException mapSystemException(SystemException paramSystemException);

  public abstract void writeAny(OutputStream paramOutputStream, Object paramObject);

  public abstract Object readAny(InputStream paramInputStream);

  public abstract void writeRemoteObject(OutputStream paramOutputStream, Object paramObject);

  public abstract void writeAbstractObject(OutputStream paramOutputStream, Object paramObject);

  public abstract void registerTarget(Tie paramTie, Remote paramRemote);

  public abstract void unexportObject(Remote paramRemote)
    throws NoSuchObjectException;

  public abstract Tie getTie(Remote paramRemote);

  public abstract ValueHandler createValueHandler();

  public abstract String getCodebase(Class paramClass);

  public abstract Class loadClass(String paramString1, String paramString2, ClassLoader paramClassLoader)
    throws ClassNotFoundException;

  public abstract boolean isLocal(Stub paramStub)
    throws RemoteException;

  public abstract RemoteException wrapException(Throwable paramThrowable);

  public abstract Object copyObject(Object paramObject, ORB paramORB)
    throws RemoteException;

  public abstract Object[] copyObjects(Object[] paramArrayOfObject, ORB paramORB)
    throws RemoteException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.UtilDelegate
 * JD-Core Version:    0.6.2
 */