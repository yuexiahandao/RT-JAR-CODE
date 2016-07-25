package javax.rmi.CORBA;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.InvokeHandler;

public abstract interface Tie extends InvokeHandler
{
  public abstract org.omg.CORBA.Object thisObject();

  public abstract void deactivate()
    throws NoSuchObjectException;

  public abstract ORB orb();

  public abstract void orb(ORB paramORB);

  public abstract void setTarget(Remote paramRemote);

  public abstract Remote getTarget();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.Tie
 * JD-Core Version:    0.6.2
 */