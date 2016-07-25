package sun.management.snmp.jvminstr;

import java.net.InetAddress;

public abstract interface NotificationTarget
{
  public abstract InetAddress getAddress();

  public abstract int getPort();

  public abstract String getCommunity();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.NotificationTarget
 * JD-Core Version:    0.6.2
 */