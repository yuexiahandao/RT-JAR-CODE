package com.sun.corba.se.spi.oa;

import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Policy;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;

public abstract interface ObjectAdapter
{
  public abstract ORB getORB();

  public abstract Policy getEffectivePolicy(int paramInt);

  public abstract IORTemplate getIORTemplate();

  public abstract int getManagerId();

  public abstract short getState();

  public abstract ObjectReferenceTemplate getAdapterTemplate();

  public abstract ObjectReferenceFactory getCurrentFactory();

  public abstract void setCurrentFactory(ObjectReferenceFactory paramObjectReferenceFactory);

  public abstract org.omg.CORBA.Object getLocalServant(byte[] paramArrayOfByte);

  public abstract void getInvocationServant(OAInvocationInfo paramOAInvocationInfo);

  public abstract void enter()
    throws OADestroyed;

  public abstract void exit();

  public abstract void returnServant();

  public abstract OAInvocationInfo makeInvocationInfo(byte[] paramArrayOfByte);

  public abstract String[] getInterfaces(java.lang.Object paramObject, byte[] paramArrayOfByte);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.oa.ObjectAdapter
 * JD-Core Version:    0.6.2
 */