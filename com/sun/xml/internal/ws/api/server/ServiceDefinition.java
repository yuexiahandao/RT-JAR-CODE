package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;

public abstract interface ServiceDefinition extends Iterable<SDDocument>
{
  @NotNull
  public abstract SDDocument getPrimary();

  public abstract void addFilter(@NotNull SDDocumentFilter paramSDDocumentFilter);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.ServiceDefinition
 * JD-Core Version:    0.6.2
 */