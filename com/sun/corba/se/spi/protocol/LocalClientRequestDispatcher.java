package com.sun.corba.se.spi.protocol;

import org.omg.CORBA.portable.ServantObject;

public abstract interface LocalClientRequestDispatcher
{
  public abstract boolean useLocalInvocation(org.omg.CORBA.Object paramObject);

  public abstract boolean is_local(org.omg.CORBA.Object paramObject);

  public abstract ServantObject servant_preinvoke(org.omg.CORBA.Object paramObject, String paramString, Class paramClass);

  public abstract void servant_postinvoke(org.omg.CORBA.Object paramObject, ServantObject paramServantObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
 * JD-Core Version:    0.6.2
 */