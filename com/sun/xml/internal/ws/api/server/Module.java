package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import java.util.List;

public abstract class Module
{
  @NotNull
  public abstract List<BoundEndpoint> getBoundEndpoints();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.Module
 * JD-Core Version:    0.6.2
 */