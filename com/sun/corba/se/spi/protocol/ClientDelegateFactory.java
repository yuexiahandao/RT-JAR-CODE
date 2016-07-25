package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.spi.transport.CorbaContactInfoList;

public abstract interface ClientDelegateFactory
{
  public abstract CorbaClientDelegate create(CorbaContactInfoList paramCorbaContactInfoList);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.protocol.ClientDelegateFactory
 * JD-Core Version:    0.6.2
 */