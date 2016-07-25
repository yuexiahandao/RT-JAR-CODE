package javax.management;

public abstract interface NotificationEmitter extends NotificationBroadcaster
{
  public abstract void removeNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
    throws ListenerNotFoundException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.NotificationEmitter
 * JD-Core Version:    0.6.2
 */