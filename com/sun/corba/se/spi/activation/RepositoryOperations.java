package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;

public abstract interface RepositoryOperations
{
  public abstract int registerServer(ServerDef paramServerDef)
    throws ServerAlreadyRegistered, BadServerDefinition;

  public abstract void unregisterServer(int paramInt)
    throws ServerNotRegistered;

  public abstract ServerDef getServer(int paramInt)
    throws ServerNotRegistered;

  public abstract boolean isInstalled(int paramInt)
    throws ServerNotRegistered;

  public abstract void install(int paramInt)
    throws ServerNotRegistered, ServerAlreadyInstalled;

  public abstract void uninstall(int paramInt)
    throws ServerNotRegistered, ServerAlreadyUninstalled;

  public abstract int[] listRegisteredServers();

  public abstract String[] getApplicationNames();

  public abstract int getServerID(String paramString)
    throws ServerNotRegistered;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.RepositoryOperations
 * JD-Core Version:    0.6.2
 */