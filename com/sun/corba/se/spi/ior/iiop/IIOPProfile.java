package com.sun.corba.se.spi.ior.iiop;

import com.sun.corba.se.spi.ior.TaggedProfile;
import com.sun.corba.se.spi.orb.ORBVersion;

public abstract interface IIOPProfile extends TaggedProfile
{
  public abstract ORBVersion getORBVersion();

  public abstract Object getServant();

  public abstract GIOPVersion getGIOPVersion();

  public abstract String getCodebase();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.iiop.IIOPProfile
 * JD-Core Version:    0.6.2
 */