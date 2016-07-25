package com.sun.corba.se.impl.protocol.giopmsgheaders;

public abstract interface CancelRequestMessage extends Message
{
  public static final int CANCEL_REQ_MSG_SIZE = 4;

  public abstract int getRequestId();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.CancelRequestMessage
 * JD-Core Version:    0.6.2
 */