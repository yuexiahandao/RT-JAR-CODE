package com.sun.corba.se.pept.transport;

public abstract interface ListenerThread
{
  public abstract Acceptor getAcceptor();

  public abstract void close();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.ListenerThread
 * JD-Core Version:    0.6.2
 */