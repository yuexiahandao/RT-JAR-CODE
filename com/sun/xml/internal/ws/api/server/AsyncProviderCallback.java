package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public abstract interface AsyncProviderCallback<T>
{
  public abstract void send(@Nullable T paramT);

  public abstract void sendError(@NotNull Throwable paramThrowable);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.AsyncProviderCallback
 * JD-Core Version:    0.6.2
 */