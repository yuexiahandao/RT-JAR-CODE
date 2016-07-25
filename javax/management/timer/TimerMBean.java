package javax.management.timer;

import java.util.Date;
import java.util.Vector;
import javax.management.InstanceNotFoundException;

public abstract interface TimerMBean
{
  public abstract void start();

  public abstract void stop();

  public abstract Integer addNotification(String paramString1, String paramString2, Object paramObject, Date paramDate, long paramLong1, long paramLong2, boolean paramBoolean)
    throws IllegalArgumentException;

  public abstract Integer addNotification(String paramString1, String paramString2, Object paramObject, Date paramDate, long paramLong1, long paramLong2)
    throws IllegalArgumentException;

  public abstract Integer addNotification(String paramString1, String paramString2, Object paramObject, Date paramDate, long paramLong)
    throws IllegalArgumentException;

  public abstract Integer addNotification(String paramString1, String paramString2, Object paramObject, Date paramDate)
    throws IllegalArgumentException;

  public abstract void removeNotification(Integer paramInteger)
    throws InstanceNotFoundException;

  public abstract void removeNotifications(String paramString)
    throws InstanceNotFoundException;

  public abstract void removeAllNotifications();

  public abstract int getNbNotifications();

  public abstract Vector<Integer> getAllNotificationIDs();

  public abstract Vector<Integer> getNotificationIDs(String paramString);

  public abstract String getNotificationType(Integer paramInteger);

  public abstract String getNotificationMessage(Integer paramInteger);

  public abstract Object getNotificationUserData(Integer paramInteger);

  public abstract Date getDate(Integer paramInteger);

  public abstract Long getPeriod(Integer paramInteger);

  public abstract Long getNbOccurences(Integer paramInteger);

  public abstract Boolean getFixedRate(Integer paramInteger);

  public abstract boolean getSendPastNotifications();

  public abstract void setSendPastNotifications(boolean paramBoolean);

  public abstract boolean isActive();

  public abstract boolean isEmpty();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.timer.TimerMBean
 * JD-Core Version:    0.6.2
 */