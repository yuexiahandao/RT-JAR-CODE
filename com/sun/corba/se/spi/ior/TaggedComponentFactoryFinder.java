package com.sun.corba.se.spi.ior;

import org.omg.CORBA.ORB;

public abstract interface TaggedComponentFactoryFinder extends IdentifiableFactoryFinder
{
  public abstract TaggedComponent create(ORB paramORB, org.omg.IOP.TaggedComponent paramTaggedComponent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder
 * JD-Core Version:    0.6.2
 */