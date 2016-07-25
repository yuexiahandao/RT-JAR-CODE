package com.sun.corba.se.spi.presentation.rmi;

import java.rmi.RemoteException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.OutputStream;

public abstract interface DynamicStub extends org.omg.CORBA.Object
{
  public abstract void setDelegate(Delegate paramDelegate);

  public abstract Delegate getDelegate();

  public abstract ORB getORB();

  public abstract String[] getTypeIds();

  public abstract void connect(ORB paramORB)
    throws RemoteException;

  public abstract boolean isLocal();

  public abstract OutputStream request(String paramString, boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.presentation.rmi.DynamicStub
 * JD-Core Version:    0.6.2
 */