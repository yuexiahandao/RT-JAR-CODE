package com.sun.corba.se.spi.orbutil.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public abstract interface LinkedInvocationHandler extends InvocationHandler
{
  public abstract void setProxy(Proxy paramProxy);

  public abstract Proxy getProxy();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler
 * JD-Core Version:    0.6.2
 */