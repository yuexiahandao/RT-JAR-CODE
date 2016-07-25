package com.sun.jmx.remote.internal;

import java.util.List;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.management.remote.TargetedNotification;

public abstract interface NotificationBufferFilter
{
  public abstract void apply(List<TargetedNotification> paramList, ObjectName paramObjectName, Notification paramNotification);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.NotificationBufferFilter
 * JD-Core Version:    0.6.2
 */