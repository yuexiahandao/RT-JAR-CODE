package com.sun.corba.se.spi.oa;

import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.orb.ORB;

public abstract interface ObjectAdapterFactory
{
  public abstract void init(ORB paramORB);

  public abstract void shutdown(boolean paramBoolean);

  public abstract ObjectAdapter find(ObjectAdapterId paramObjectAdapterId);

  public abstract ORB getORB();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.oa.ObjectAdapterFactory
 * JD-Core Version:    0.6.2
 */