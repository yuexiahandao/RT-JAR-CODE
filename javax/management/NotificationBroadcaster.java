package javax.management;

public abstract interface NotificationBroadcaster
{
  public abstract void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
    throws IllegalArgumentException;

  public abstract void removeNotificationListener(NotificationListener paramNotificationListener)
    throws ListenerNotFoundException;

  public abstract MBeanNotificationInfo[] getNotificationInfo();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.NotificationBroadcaster
 * JD-Core Version:    0.6.2
 */