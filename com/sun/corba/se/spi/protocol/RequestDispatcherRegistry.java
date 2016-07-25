package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import java.util.Set;

public abstract interface RequestDispatcherRegistry
{
  public abstract void registerClientRequestDispatcher(ClientRequestDispatcher paramClientRequestDispatcher, int paramInt);

  public abstract ClientRequestDispatcher getClientRequestDispatcher(int paramInt);

  public abstract void registerLocalClientRequestDispatcherFactory(LocalClientRequestDispatcherFactory paramLocalClientRequestDispatcherFactory, int paramInt);

  public abstract LocalClientRequestDispatcherFactory getLocalClientRequestDispatcherFactory(int paramInt);

  public abstract void registerServerRequestDispatcher(CorbaServerRequestDispatcher paramCorbaServerRequestDispatcher, int paramInt);

  public abstract CorbaServerRequestDispatcher getServerRequestDispatcher(int paramInt);

  public abstract void registerServerRequestDispatcher(CorbaServerRequestDispatcher paramCorbaServerRequestDispatcher, String paramString);

  public abstract CorbaServerRequestDispatcher getServerRequestDispatcher(String paramString);

  public abstract void registerObjectAdapterFactory(ObjectAdapterFactory paramObjectAdapterFactory, int paramInt);

  public abstract ObjectAdapterFactory getObjectAdapterFactory(int paramInt);

  public abstract Set<ObjectAdapterFactory> getObjectAdapterFactories();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
 * JD-Core Version:    0.6.2
 */