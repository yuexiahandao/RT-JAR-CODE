package com.sun.corba.se.spi.legacy.connection;

public abstract interface LegacyServerSocketManager
{
  public abstract int legacyGetTransientServerPort(String paramString);

  public abstract int legacyGetPersistentServerPort(String paramString);

  public abstract int legacyGetTransientOrPersistentServerPort(String paramString);

  public abstract LegacyServerSocketEndPointInfo legacyGetEndpoint(String paramString);

  public abstract boolean legacyIsLocalServerPort(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager
 * JD-Core Version:    0.6.2
 */