package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;

public abstract interface ObjectKey extends Writeable
{
  public abstract ObjectId getId();

  public abstract ObjectKeyTemplate getTemplate();

  public abstract byte[] getBytes(org.omg.CORBA.ORB paramORB);

  public abstract CorbaServerRequestDispatcher getServerRequestDispatcher(com.sun.corba.se.spi.orb.ORB paramORB);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.ObjectKey
 * JD-Core Version:    0.6.2
 */