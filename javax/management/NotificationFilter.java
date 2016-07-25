package javax.management;

import java.io.Serializable;

public abstract interface NotificationFilter extends Serializable
{
  public abstract boolean isNotificationEnabled(Notification paramNotification);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.NotificationFilter
 * JD-Core Version:    0.6.2
 */