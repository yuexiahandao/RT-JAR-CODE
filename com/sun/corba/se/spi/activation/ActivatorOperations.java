package com.sun.corba.se.spi.activation;

public abstract interface ActivatorOperations
{
  public abstract void active(int paramInt, Server paramServer)
    throws ServerNotRegistered;

  public abstract void registerEndpoints(int paramInt, String paramString, EndPointInfo[] paramArrayOfEndPointInfo)
    throws ServerNotRegistered, NoSuchEndPoint, ORBAlreadyRegistered;

  public abstract int[] getActiveServers();

  public abstract void activate(int paramInt)
    throws ServerAlreadyActive, ServerNotRegistered, ServerHeldDown;

  public abstract void shutdown(int paramInt)
    throws ServerNotActive, ServerNotRegistered;

  public abstract void install(int paramInt)
    throws ServerNotRegistered, ServerHeldDown, ServerAlreadyInstalled;

  public abstract String[] getORBNames(int paramInt)
    throws ServerNotRegistered;

  public abstract void uninstall(int paramInt)
    throws ServerNotRegistered, ServerHeldDown, ServerAlreadyUninstalled;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ActivatorOperations
 * JD-Core Version:    0.6.2
 */