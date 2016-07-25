package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.Policy;
import org.omg.IOP.ServiceContext;
import org.omg.IOP.TaggedComponent;
import org.omg.IOP.TaggedProfile;

public abstract interface ClientRequestInfoOperations extends RequestInfoOperations
{
  public abstract org.omg.CORBA.Object target();

  public abstract org.omg.CORBA.Object effective_target();

  public abstract TaggedProfile effective_profile();

  public abstract Any received_exception();

  public abstract String received_exception_id();

  public abstract TaggedComponent get_effective_component(int paramInt);

  public abstract TaggedComponent[] get_effective_components(int paramInt);

  public abstract Policy get_request_policy(int paramInt);

  public abstract void add_request_service_context(ServiceContext paramServiceContext, boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ClientRequestInfoOperations
 * JD-Core Version:    0.6.2
 */