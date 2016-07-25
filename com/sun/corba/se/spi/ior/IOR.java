package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;
import java.util.List;

public abstract interface IOR extends List, Writeable, MakeImmutable
{
  public abstract ORB getORB();

  public abstract String getTypeId();

  public abstract Iterator iteratorById(int paramInt);

  public abstract String stringify();

  public abstract org.omg.IOP.IOR getIOPIOR();

  public abstract boolean isNil();

  public abstract boolean isEquivalent(IOR paramIOR);

  public abstract IORTemplateList getIORTemplates();

  public abstract IIOPProfile getProfile();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.IOR
 * JD-Core Version:    0.6.2
 */