package com.sun.corba.se.pept.transport;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public abstract interface EventHandler
{
  public abstract void setUseSelectThreadToWait(boolean paramBoolean);

  public abstract boolean shouldUseSelectThreadToWait();

  public abstract SelectableChannel getChannel();

  public abstract int getInterestOps();

  public abstract void setSelectionKey(SelectionKey paramSelectionKey);

  public abstract SelectionKey getSelectionKey();

  public abstract void handleEvent();

  public abstract void setUseWorkerThreadForEvent(boolean paramBoolean);

  public abstract boolean shouldUseWorkerThreadForEvent();

  public abstract void setWork(Work paramWork);

  public abstract Work getWork();

  public abstract Acceptor getAcceptor();

  public abstract Connection getConnection();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.EventHandler
 * JD-Core Version:    0.6.2
 */