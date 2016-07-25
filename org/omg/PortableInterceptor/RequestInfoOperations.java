package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.omg.Dynamic.Parameter;
import org.omg.IOP.ServiceContext;

public abstract interface RequestInfoOperations
{
  public abstract int request_id();

  public abstract String operation();

  public abstract Parameter[] arguments();

  public abstract TypeCode[] exceptions();

  public abstract String[] contexts();

  public abstract String[] operation_context();

  public abstract Any result();

  public abstract boolean response_expected();

  public abstract short sync_scope();

  public abstract short reply_status();

  public abstract org.omg.CORBA.Object forward_reference();

  public abstract Any get_slot(int paramInt)
    throws InvalidSlot;

  public abstract ServiceContext get_request_service_context(int paramInt);

  public abstract ServiceContext get_reply_service_context(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.RequestInfoOperations
 * JD-Core Version:    0.6.2
 */