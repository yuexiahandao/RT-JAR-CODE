package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.spi.resolver.Resolver;

public abstract interface InitialServerRequestDispatcher extends CorbaServerRequestDispatcher
{
  public abstract void init(Resolver paramResolver);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.protocol.InitialServerRequestDispatcher
 * JD-Core Version:    0.6.2
 */