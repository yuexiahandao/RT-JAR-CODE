package com.sun.corba.se.pept.transport;

public abstract interface ReaderThread
{
  public abstract Connection getConnection();

  public abstract void close();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.ReaderThread
 * JD-Core Version:    0.6.2
 */