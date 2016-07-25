package com.sun.corba.se.spi.resolver;

import java.util.Set;

public abstract interface Resolver
{
  public abstract org.omg.CORBA.Object resolve(String paramString);

  public abstract Set list();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.resolver.Resolver
 * JD-Core Version:    0.6.2
 */