package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.ResponseWaitingRoom;
import org.omg.CORBA.SystemException;

public abstract interface CorbaResponseWaitingRoom extends ResponseWaitingRoom
{
  public abstract void signalExceptionToAllWaiters(SystemException paramSystemException);

  public abstract MessageMediator getMessageMediator(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.transport.CorbaResponseWaitingRoom
 * JD-Core Version:    0.6.2
 */