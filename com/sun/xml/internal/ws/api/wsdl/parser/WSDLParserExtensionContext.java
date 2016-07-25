package com.sun.xml.internal.ws.api.wsdl.parser;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.server.Container;

public abstract interface WSDLParserExtensionContext
{
  public abstract boolean isClientSide();

  public abstract WSDLModel getWSDLModel();

  @NotNull
  public abstract Container getContainer();

  @NotNull
  public abstract PolicyResolver getPolicyResolver();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext
 * JD-Core Version:    0.6.2
 */