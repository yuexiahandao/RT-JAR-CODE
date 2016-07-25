package com.sun.corba.se.pept.transport;

import java.util.Collection;

public abstract interface TransportManager
{
  public abstract ByteBufferPool getByteBufferPool(int paramInt);

  public abstract OutboundConnectionCache getOutboundConnectionCache(ContactInfo paramContactInfo);

  public abstract Collection getOutboundConnectionCaches();

  public abstract InboundConnectionCache getInboundConnectionCache(Acceptor paramAcceptor);

  public abstract Collection getInboundConnectionCaches();

  public abstract Selector getSelector(int paramInt);

  public abstract void registerAcceptor(Acceptor paramAcceptor);

  public abstract Collection getAcceptors();

  public abstract void unregisterAcceptor(Acceptor paramAcceptor);

  public abstract void close();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.TransportManager
 * JD-Core Version:    0.6.2
 */