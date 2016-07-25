package com.sun.corba.se.pept.protocol;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.transport.ContactInfoList;

public abstract interface ClientDelegate
{
  public abstract Broker getBroker();

  public abstract ContactInfoList getContactInfoList();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.protocol.ClientDelegate
 * JD-Core Version:    0.6.2
 */