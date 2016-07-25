package com.sun.xml.internal.ws.api.model.wsdl;

import javax.xml.namespace.QName;

public abstract interface WSDLPartDescriptor extends WSDLObject
{
  public abstract QName name();

  public abstract WSDLDescriptorKind type();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor
 * JD-Core Version:    0.6.2
 */