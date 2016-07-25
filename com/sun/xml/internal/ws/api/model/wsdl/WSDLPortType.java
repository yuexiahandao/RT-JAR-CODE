package com.sun.xml.internal.ws.api.model.wsdl;

import javax.xml.namespace.QName;

public abstract interface WSDLPortType extends WSDLObject, WSDLExtensible
{
  public abstract QName getName();

  public abstract WSDLOperation get(String paramString);

  public abstract Iterable<? extends WSDLOperation> getOperations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType
 * JD-Core Version:    0.6.2
 */