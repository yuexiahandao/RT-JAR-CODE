package com.sun.corba.se.spi.orbutil.proxy;

import java.lang.reflect.InvocationHandler;

public abstract interface InvocationHandlerFactory
{
  public abstract InvocationHandler getInvocationHandler();

  public abstract Class[] getProxyInterfaces();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory
 * JD-Core Version:    0.6.2
 */