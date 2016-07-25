package com.sun.xml.internal.ws.api.model;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
import java.lang.reflect.Method;
import javax.xml.namespace.QName;

public abstract interface JavaMethod
{
  public abstract SEIModel getOwner();

  @NotNull
  public abstract Method getMethod();

  @NotNull
  public abstract Method getSEIMethod();

  public abstract MEP getMEP();

  public abstract SOAPBinding getBinding();

  @NotNull
  public abstract String getOperationName();

  @NotNull
  public abstract String getRequestMessageName();

  @Nullable
  public abstract String getResponseMessageName();

  @Nullable
  public abstract QName getRequestPayloadName();

  @Nullable
  public abstract QName getResponsePayloadName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.JavaMethod
 * JD-Core Version:    0.6.2
 */