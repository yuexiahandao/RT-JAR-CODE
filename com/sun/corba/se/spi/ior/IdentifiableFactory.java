package com.sun.corba.se.spi.ior;

import org.omg.CORBA_2_3.portable.InputStream;

public abstract interface IdentifiableFactory
{
  public abstract int getId();

  public abstract Identifiable create(InputStream paramInputStream);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.IdentifiableFactory
 * JD-Core Version:    0.6.2
 */