package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;

public abstract interface CurrentOperations extends org.omg.CORBA.CurrentOperations
{
  public abstract Any get_slot(int paramInt)
    throws InvalidSlot;

  public abstract void set_slot(int paramInt, Any paramAny)
    throws InvalidSlot;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.CurrentOperations
 * JD-Core Version:    0.6.2
 */