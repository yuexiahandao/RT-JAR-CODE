package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.message.Packet;
import java.security.Principal;

public abstract interface WebServiceContextDelegate
{
  public abstract Principal getUserPrincipal(@NotNull Packet paramPacket);

  public abstract boolean isUserInRole(@NotNull Packet paramPacket, String paramString);

  @NotNull
  public abstract String getEPRAddress(@NotNull Packet paramPacket, @NotNull WSEndpoint paramWSEndpoint);

  @Nullable
  public abstract String getWSDLAddress(@NotNull Packet paramPacket, @NotNull WSEndpoint paramWSEndpoint);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.WebServiceContextDelegate
 * JD-Core Version:    0.6.2
 */