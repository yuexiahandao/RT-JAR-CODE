package com.sun.jmx.snmp;

import java.net.InetAddress;
import java.util.Enumeration;

public abstract interface InetAddressAcl
{
  public abstract String getName();

  public abstract boolean checkReadPermission(InetAddress paramInetAddress);

  public abstract boolean checkReadPermission(InetAddress paramInetAddress, String paramString);

  public abstract boolean checkCommunity(String paramString);

  public abstract boolean checkWritePermission(InetAddress paramInetAddress);

  public abstract boolean checkWritePermission(InetAddress paramInetAddress, String paramString);

  public abstract Enumeration getTrapDestinations();

  public abstract Enumeration getTrapCommunities(InetAddress paramInetAddress);

  public abstract Enumeration getInformDestinations();

  public abstract Enumeration getInformCommunities(InetAddress paramInetAddress);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.InetAddressAcl
 * JD-Core Version:    0.6.2
 */