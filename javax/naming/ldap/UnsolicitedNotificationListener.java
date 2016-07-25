package javax.naming.ldap;

import javax.naming.event.NamingListener;

public abstract interface UnsolicitedNotificationListener extends NamingListener
{
  public abstract void notificationReceived(UnsolicitedNotificationEvent paramUnsolicitedNotificationEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.UnsolicitedNotificationListener
 * JD-Core Version:    0.6.2
 */