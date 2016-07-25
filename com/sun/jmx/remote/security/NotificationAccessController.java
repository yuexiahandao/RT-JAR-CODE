package com.sun.jmx.remote.security;

import javax.management.Notification;
import javax.management.ObjectName;
import javax.security.auth.Subject;

public abstract interface NotificationAccessController
{
  public abstract void addNotificationListener(String paramString, ObjectName paramObjectName, Subject paramSubject)
    throws SecurityException;

  public abstract void removeNotificationListener(String paramString, ObjectName paramObjectName, Subject paramSubject)
    throws SecurityException;

  public abstract void fetchNotification(String paramString, ObjectName paramObjectName, Notification paramNotification, Subject paramSubject)
    throws SecurityException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.security.NotificationAccessController
 * JD-Core Version:    0.6.2
 */