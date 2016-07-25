package com.sun.corba.se.spi.ior;

import org.omg.CORBA.ORB;

public abstract interface TaggedComponent extends Identifiable
{
  public abstract org.omg.IOP.TaggedComponent getIOPComponent(ORB paramORB);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.TaggedComponent
 * JD-Core Version:    0.6.2
 */