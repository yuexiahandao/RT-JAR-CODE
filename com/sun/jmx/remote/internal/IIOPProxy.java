package com.sun.jmx.remote.internal;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;

public abstract interface IIOPProxy
{
  public abstract boolean isStub(Object paramObject);

  public abstract Object getDelegate(Object paramObject);

  public abstract void setDelegate(Object paramObject1, Object paramObject2);

  public abstract Object getOrb(Object paramObject);

  public abstract void connect(Object paramObject1, Object paramObject2)
    throws RemoteException;

  public abstract boolean isOrb(Object paramObject);

  public abstract Object createOrb(String[] paramArrayOfString, Properties paramProperties);

  public abstract Object stringToObject(Object paramObject, String paramString);

  public abstract String objectToString(Object paramObject1, Object paramObject2);

  public abstract <T> T narrow(Object paramObject, Class<T> paramClass);

  public abstract void exportObject(Remote paramRemote)
    throws RemoteException;

  public abstract void unexportObject(Remote paramRemote)
    throws NoSuchObjectException;

  public abstract Remote toStub(Remote paramRemote)
    throws NoSuchObjectException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.IIOPProxy
 * JD-Core Version:    0.6.2
 */