package com.sun.xml.internal.ws.api.model.wsdl;

import javax.xml.namespace.QName;

public abstract interface WSDLMessage extends WSDLObject, WSDLExtensible
{
  public abstract QName getName();

  public abstract Iterable<? extends WSDLPart> parts();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage
 * JD-Core Version:    0.6.2
 */