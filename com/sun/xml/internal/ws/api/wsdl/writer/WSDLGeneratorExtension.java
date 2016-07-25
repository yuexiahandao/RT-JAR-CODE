package com.sun.xml.internal.ws.api.wsdl.writer;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.model.CheckedException;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.server.Container;

public abstract class WSDLGeneratorExtension
{
  /** @deprecated */
  public void start(@NotNull TypedXmlWriter root, @NotNull SEIModel model, @NotNull WSBinding binding, @NotNull Container container)
  {
  }

  public void end(@NotNull WSDLGenExtnContext ctxt)
  {
  }

  public void start(WSDLGenExtnContext ctxt)
  {
  }

  public void addDefinitionsExtension(TypedXmlWriter definitions)
  {
  }

  public void addServiceExtension(TypedXmlWriter service)
  {
  }

  public void addPortExtension(TypedXmlWriter port)
  {
  }

  public void addPortTypeExtension(TypedXmlWriter portType)
  {
  }

  public void addBindingExtension(TypedXmlWriter binding)
  {
  }

  public void addOperationExtension(TypedXmlWriter operation, JavaMethod method)
  {
  }

  public void addBindingOperationExtension(TypedXmlWriter operation, JavaMethod method)
  {
  }

  public void addInputMessageExtension(TypedXmlWriter message, JavaMethod method)
  {
  }

  public void addOutputMessageExtension(TypedXmlWriter message, JavaMethod method)
  {
  }

  public void addOperationInputExtension(TypedXmlWriter input, JavaMethod method)
  {
  }

  public void addOperationOutputExtension(TypedXmlWriter output, JavaMethod method)
  {
  }

  public void addBindingOperationInputExtension(TypedXmlWriter input, JavaMethod method)
  {
  }

  public void addBindingOperationOutputExtension(TypedXmlWriter output, JavaMethod method)
  {
  }

  public void addBindingOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException ce)
  {
  }

  public void addFaultMessageExtension(TypedXmlWriter message, JavaMethod method, CheckedException ce)
  {
  }

  public void addOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException ce)
  {
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
 * JD-Core Version:    0.6.2
 */