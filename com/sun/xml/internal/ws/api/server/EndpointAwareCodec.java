package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.pipe.Codec;

public abstract interface EndpointAwareCodec extends Codec
{
  public abstract void setEndpoint(@NotNull WSEndpoint paramWSEndpoint);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.EndpointAwareCodec
 * JD-Core Version:    0.6.2
 */