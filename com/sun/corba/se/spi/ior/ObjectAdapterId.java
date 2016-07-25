package com.sun.corba.se.spi.ior;

import java.util.Iterator;

public abstract interface ObjectAdapterId extends Writeable
{
  public abstract int getNumLevels();

  public abstract Iterator iterator();

  public abstract String[] getAdapterName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.ObjectAdapterId
 * JD-Core Version:    0.6.2
 */