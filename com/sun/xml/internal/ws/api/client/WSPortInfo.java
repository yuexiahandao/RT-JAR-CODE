package com.sun.xml.internal.ws.api.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.policy.PolicyMap;
import javax.xml.ws.handler.PortInfo;

public abstract interface WSPortInfo extends PortInfo
{
  @NotNull
  public abstract WSService getOwner();

  @NotNull
  public abstract BindingID getBindingId();

  @NotNull
  public abstract EndpointAddress getEndpointAddress();

  @Nullable
  public abstract WSDLPort getPort();

  /** @deprecated */
  public abstract PolicyMap getPolicyMap();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.client.WSPortInfo
 * JD-Core Version:    0.6.2
 */