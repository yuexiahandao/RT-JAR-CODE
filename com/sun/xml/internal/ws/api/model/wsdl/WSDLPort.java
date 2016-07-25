package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.EndpointAddress;
import javax.xml.namespace.QName;

public abstract interface WSDLPort extends WSDLFeaturedObject, WSDLExtensible
{
  public abstract QName getName();

  @NotNull
  public abstract WSDLBoundPortType getBinding();

  public abstract EndpointAddress getAddress();

  @NotNull
  public abstract WSDLService getOwner();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLPort
 * JD-Core Version:    0.6.2
 */