package com.sun.xml.internal.ws.api.model;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.util.Pool.Marshaller;
import java.lang.reflect.Method;
import java.util.Collection;
import javax.xml.namespace.QName;

public abstract interface SEIModel
{
  public abstract Pool.Marshaller getMarshallerPool();

  public abstract JAXBRIContext getJAXBContext();

  public abstract JavaMethod getJavaMethod(Method paramMethod);

  public abstract JavaMethod getJavaMethod(QName paramQName);

  public abstract JavaMethod getJavaMethodForWsdlOperation(QName paramQName);

  public abstract Collection<? extends JavaMethod> getJavaMethods();

  @NotNull
  public abstract String getWSDLLocation();

  @NotNull
  public abstract QName getServiceQName();

  @NotNull
  public abstract WSDLPort getPort();

  @NotNull
  public abstract QName getPortName();

  @NotNull
  public abstract QName getPortTypeName();

  @NotNull
  public abstract QName getBoundPortTypeName();

  @NotNull
  public abstract String getTargetNamespace();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.SEIModel
 * JD-Core Version:    0.6.2
 */