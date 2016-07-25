package com.sun.nio.sctp;

public abstract interface NotificationHandler<T>
{
  public abstract HandlerResult handleNotification(Notification paramNotification, T paramT);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.NotificationHandler
 * JD-Core Version:    0.6.2
 */