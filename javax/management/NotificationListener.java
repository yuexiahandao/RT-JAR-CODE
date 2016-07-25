package javax.management;

import java.util.EventListener;

public abstract interface NotificationListener extends EventListener
{
  public abstract void handleNotification(Notification paramNotification, Object paramObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.NotificationListener
 * JD-Core Version:    0.6.2
 */