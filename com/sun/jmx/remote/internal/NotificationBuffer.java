package com.sun.jmx.remote.internal;

import javax.management.remote.NotificationResult;

public abstract interface NotificationBuffer
{
  public abstract NotificationResult fetchNotifications(NotificationBufferFilter paramNotificationBufferFilter, long paramLong1, long paramLong2, int paramInt)
    throws InterruptedException;

  public abstract void dispose();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.NotificationBuffer
 * JD-Core Version:    0.6.2
 */