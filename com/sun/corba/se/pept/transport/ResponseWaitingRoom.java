package com.sun.corba.se.pept.transport;

import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;

public abstract interface ResponseWaitingRoom
{
  public abstract void registerWaiter(MessageMediator paramMessageMediator);

  public abstract InputObject waitForResponse(MessageMediator paramMessageMediator);

  public abstract void responseReceived(InputObject paramInputObject);

  public abstract void unregisterWaiter(MessageMediator paramMessageMediator);

  public abstract int numberRegistered();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.ResponseWaitingRoom
 * JD-Core Version:    0.6.2
 */