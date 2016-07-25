package com.sun.corba.se.impl.oa.poa;

import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantManager;

public abstract interface POAPolicyMediator
{
  public abstract Policies getPolicies();

  public abstract int getScid();

  public abstract int getServerId();

  public abstract Object getInvocationServant(byte[] paramArrayOfByte, String paramString)
    throws ForwardRequest;

  public abstract void returnServant();

  public abstract void etherealizeAll();

  public abstract void clearAOM();

  public abstract ServantManager getServantManager()
    throws WrongPolicy;

  public abstract void setServantManager(ServantManager paramServantManager)
    throws WrongPolicy;

  public abstract Servant getDefaultServant()
    throws NoServant, WrongPolicy;

  public abstract void setDefaultServant(Servant paramServant)
    throws WrongPolicy;

  public abstract void activateObject(byte[] paramArrayOfByte, Servant paramServant)
    throws ObjectAlreadyActive, ServantAlreadyActive, WrongPolicy;

  public abstract Servant deactivateObject(byte[] paramArrayOfByte)
    throws ObjectNotActive, WrongPolicy;

  public abstract byte[] newSystemId()
    throws WrongPolicy;

  public abstract byte[] servantToId(Servant paramServant)
    throws ServantNotActive, WrongPolicy;

  public abstract Servant idToServant(byte[] paramArrayOfByte)
    throws ObjectNotActive, WrongPolicy;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediator
 * JD-Core Version:    0.6.2
 */