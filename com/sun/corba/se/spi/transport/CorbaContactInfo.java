package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.IIOPProfile;

public abstract interface CorbaContactInfo extends ContactInfo
{
  public abstract IOR getTargetIOR();

  public abstract IOR getEffectiveTargetIOR();

  public abstract IIOPProfile getEffectiveProfile();

  public abstract void setAddressingDisposition(short paramShort);

  public abstract short getAddressingDisposition();

  public abstract String getMonitoringName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.transport.CorbaContactInfo
 * JD-Core Version:    0.6.2
 */