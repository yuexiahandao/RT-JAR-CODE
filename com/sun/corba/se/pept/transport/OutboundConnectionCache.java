package com.sun.corba.se.pept.transport;

public abstract interface OutboundConnectionCache extends ConnectionCache
{
  public abstract Connection get(ContactInfo paramContactInfo);

  public abstract void put(ContactInfo paramContactInfo, Connection paramConnection);

  public abstract void remove(ContactInfo paramContactInfo);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.OutboundConnectionCache
 * JD-Core Version:    0.6.2
 */