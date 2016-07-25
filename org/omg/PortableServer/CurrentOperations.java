package org.omg.PortableServer;

import org.omg.PortableServer.CurrentPackage.NoContext;

public abstract interface CurrentOperations extends org.omg.CORBA.CurrentOperations
{
  public abstract POA get_POA()
    throws NoContext;

  public abstract byte[] get_object_id()
    throws NoContext;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.CurrentOperations
 * JD-Core Version:    0.6.2
 */