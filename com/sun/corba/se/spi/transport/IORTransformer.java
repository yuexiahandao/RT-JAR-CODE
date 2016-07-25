package com.sun.corba.se.spi.transport;

import com.sun.corba.se.spi.encoding.CorbaInputObject;
import com.sun.corba.se.spi.encoding.CorbaOutputObject;
import com.sun.corba.se.spi.ior.IOR;

public abstract interface IORTransformer
{
  public abstract IOR unmarshal(CorbaInputObject paramCorbaInputObject);

  public abstract void marshal(CorbaOutputObject paramCorbaOutputObject, IOR paramIOR);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.transport.IORTransformer
 * JD-Core Version:    0.6.2
 */