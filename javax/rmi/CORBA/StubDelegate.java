package javax.rmi.CORBA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import org.omg.CORBA.ORB;

public abstract interface StubDelegate
{
  public abstract int hashCode(Stub paramStub);

  public abstract boolean equals(Stub paramStub, Object paramObject);

  public abstract String toString(Stub paramStub);

  public abstract void connect(Stub paramStub, ORB paramORB)
    throws RemoteException;

  public abstract void readObject(Stub paramStub, ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException;

  public abstract void writeObject(Stub paramStub, ObjectOutputStream paramObjectOutputStream)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.StubDelegate
 * JD-Core Version:    0.6.2
 */