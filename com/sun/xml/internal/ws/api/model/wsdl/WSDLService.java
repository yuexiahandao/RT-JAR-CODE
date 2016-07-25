package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import javax.xml.namespace.QName;

public abstract interface WSDLService extends WSDLObject, WSDLExtensible
{
  @NotNull
  public abstract WSDLModel getParent();

  @NotNull
  public abstract QName getName();

  public abstract WSDLPort get(QName paramQName);

  public abstract WSDLPort getFirstPort();

  public abstract Iterable<? extends WSDLPort> getPorts();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLService
 * JD-Core Version:    0.6.2
 */