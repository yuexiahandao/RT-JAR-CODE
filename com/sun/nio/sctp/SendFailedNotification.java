package com.sun.nio.sctp;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public abstract class SendFailedNotification
  implements Notification
{
  public abstract Association association();

  public abstract SocketAddress address();

  public abstract ByteBuffer buffer();

  public abstract int errorCode();

  public abstract int streamNumber();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.SendFailedNotification
 * JD-Core Version:    0.6.2
 */