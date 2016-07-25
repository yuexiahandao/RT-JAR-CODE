package com.sun.xml.internal.ws.api.wsdl.parser;

import com.sun.istack.internal.NotNull;
import java.util.List;
import javax.xml.transform.Source;

public abstract class ServiceDescriptor
{
  @NotNull
  public abstract List<? extends Source> getWSDLs();

  @NotNull
  public abstract List<? extends Source> getSchemas();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.wsdl.parser.ServiceDescriptor
 * JD-Core Version:    0.6.2
 */