package org.omg.PortableServer.portable;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

public abstract interface Delegate
{
  public abstract ORB orb(Servant paramServant);

  public abstract org.omg.CORBA.Object this_object(Servant paramServant);

  public abstract POA poa(Servant paramServant);

  public abstract byte[] object_id(Servant paramServant);

  public abstract POA default_POA(Servant paramServant);

  public abstract boolean is_a(Servant paramServant, String paramString);

  public abstract boolean non_existent(Servant paramServant);

  public abstract org.omg.CORBA.Object get_interface_def(Servant paramServant);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.portable.Delegate
 * JD-Core Version:    0.6.2
 */