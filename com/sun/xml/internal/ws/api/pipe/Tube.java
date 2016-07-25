package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;

public abstract interface Tube
{
  @NotNull
  public abstract NextAction processRequest(@NotNull Packet paramPacket);

  @NotNull
  public abstract NextAction processResponse(@NotNull Packet paramPacket);

  @NotNull
  public abstract NextAction processException(@NotNull Throwable paramThrowable);

  public abstract void preDestroy();

  public abstract Tube copy(TubeCloner paramTubeCloner);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.Tube
 * JD-Core Version:    0.6.2
 */