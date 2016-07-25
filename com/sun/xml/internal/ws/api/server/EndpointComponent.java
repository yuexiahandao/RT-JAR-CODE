package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public abstract interface EndpointComponent
{
  @Nullable
  public abstract <T> T getSPI(@NotNull Class<T> paramClass);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.EndpointComponent
 * JD-Core Version:    0.6.2
 */