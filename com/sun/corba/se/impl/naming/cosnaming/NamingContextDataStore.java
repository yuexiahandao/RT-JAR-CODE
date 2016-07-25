package com.sun.corba.se.impl.naming.cosnaming;

import org.omg.CORBA.SystemException;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.BindingTypeHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.PortableServer.POA;

public abstract interface NamingContextDataStore
{
  public abstract void Bind(NameComponent paramNameComponent, org.omg.CORBA.Object paramObject, BindingType paramBindingType)
    throws SystemException;

  public abstract org.omg.CORBA.Object Resolve(NameComponent paramNameComponent, BindingTypeHolder paramBindingTypeHolder)
    throws SystemException;

  public abstract org.omg.CORBA.Object Unbind(NameComponent paramNameComponent)
    throws SystemException;

  public abstract void List(int paramInt, BindingListHolder paramBindingListHolder, BindingIteratorHolder paramBindingIteratorHolder)
    throws SystemException;

  public abstract NamingContext NewContext()
    throws SystemException;

  public abstract void Destroy()
    throws SystemException;

  public abstract boolean IsEmpty();

  public abstract POA getNSPOA();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
 * JD-Core Version:    0.6.2
 */