package com.sun.corba.se.spi.legacy.connection;

public abstract interface LegacyServerSocketEndPointInfo
{
  public static final String DEFAULT_ENDPOINT = "DEFAULT_ENDPOINT";
  public static final String BOOT_NAMING = "BOOT_NAMING";
  public static final String NO_NAME = "NO_NAME";

  public abstract String getType();

  public abstract String getHostName();

  public abstract int getPort();

  public abstract int getLocatorPort();

  public abstract void setLocatorPort(int paramInt);

  public abstract String getName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
 * JD-Core Version:    0.6.2
 */