package com.sun.xml.internal.ws.api.model.wsdl;

public abstract interface WSDLExtensible extends WSDLObject
{
  public abstract Iterable<WSDLExtension> getExtensions();

  public abstract <T extends WSDLExtension> Iterable<T> getExtensions(Class<T> paramClass);

  public abstract <T extends WSDLExtension> T getExtension(Class<T> paramClass);

  public abstract void addExtension(WSDLExtension paramWSDLExtension);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
 * JD-Core Version:    0.6.2
 */