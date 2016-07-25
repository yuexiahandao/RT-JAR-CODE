package org.omg.PortableInterceptor;

import org.omg.IOP.CodecFactory;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.omg.PortableInterceptor.ORBInitInfoPackage.InvalidName;

public abstract interface ORBInitInfoOperations
{
  public abstract String[] arguments();

  public abstract String orb_id();

  public abstract CodecFactory codec_factory();

  public abstract void register_initial_reference(String paramString, org.omg.CORBA.Object paramObject)
    throws InvalidName;

  public abstract org.omg.CORBA.Object resolve_initial_references(String paramString)
    throws InvalidName;

  public abstract void add_client_request_interceptor(ClientRequestInterceptor paramClientRequestInterceptor)
    throws DuplicateName;

  public abstract void add_server_request_interceptor(ServerRequestInterceptor paramServerRequestInterceptor)
    throws DuplicateName;

  public abstract void add_ior_interceptor(IORInterceptor paramIORInterceptor)
    throws DuplicateName;

  public abstract int allocate_slot_id();

  public abstract void register_policy_factory(int paramInt, PolicyFactory paramPolicyFactory);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ORBInitInfoOperations
 * JD-Core Version:    0.6.2
 */