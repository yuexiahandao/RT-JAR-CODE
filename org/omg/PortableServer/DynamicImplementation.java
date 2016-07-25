package org.omg.PortableServer;

import org.omg.CORBA.ServerRequest;

public abstract class DynamicImplementation extends Servant
{
  public abstract void invoke(ServerRequest paramServerRequest);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.DynamicImplementation
 * JD-Core Version:    0.6.2
 */