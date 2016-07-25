package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.namespace.QName;

public abstract interface WSDLBoundPortType extends WSDLFeaturedObject, WSDLExtensible
{
  public abstract QName getName();

  @NotNull
  public abstract WSDLModel getOwner();

  public abstract WSDLBoundOperation get(QName paramQName);

  public abstract QName getPortTypeName();

  public abstract WSDLPortType getPortType();

  public abstract Iterable<? extends WSDLBoundOperation> getBindingOperations();

  @NotNull
  public abstract SOAPBinding.Style getStyle();

  public abstract BindingID getBindingId();

  @Nullable
  public abstract WSDLBoundOperation getOperation(String paramString1, String paramString2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
 * JD-Core Version:    0.6.2
 */