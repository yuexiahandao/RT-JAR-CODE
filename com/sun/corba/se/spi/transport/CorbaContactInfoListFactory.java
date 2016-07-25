package com.sun.corba.se.spi.transport;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;

public abstract interface CorbaContactInfoListFactory
{
  public abstract void setORB(ORB paramORB);

  public abstract CorbaContactInfoList create(IOR paramIOR);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.transport.CorbaContactInfoListFactory
 * JD-Core Version:    0.6.2
 */