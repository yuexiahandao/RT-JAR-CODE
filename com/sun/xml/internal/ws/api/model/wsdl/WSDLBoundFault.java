package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javax.xml.namespace.QName;

public abstract interface WSDLBoundFault extends WSDLObject, WSDLExtensible
{
  @NotNull
  public abstract String getName();

  @Nullable
  public abstract QName getQName();

  @Nullable
  public abstract WSDLFault getFault();

  @NotNull
  public abstract WSDLBoundOperation getBoundOperation();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault
 * JD-Core Version:    0.6.2
 */